package android.filterpacks.videoproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLEnvironment;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;
import java.util.Arrays;
import java.util.List;

public class BackDropperFilter
  extends Filter
{
  private static final float DEFAULT_ACCEPT_STDDEV = 0.85F;
  private static final float DEFAULT_ADAPT_RATE_BG = 0.0F;
  private static final float DEFAULT_ADAPT_RATE_FG = 0.0F;
  private static final String DEFAULT_AUTO_WB_SCALE = "0.25";
  private static final float[] DEFAULT_BG_FIT_TRANSFORM = { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
  private static final float DEFAULT_EXPOSURE_CHANGE = 1.0F;
  private static final int DEFAULT_HIER_LRG_EXPONENT = 3;
  private static final float DEFAULT_HIER_LRG_SCALE = 0.7F;
  private static final int DEFAULT_HIER_MID_EXPONENT = 2;
  private static final float DEFAULT_HIER_MID_SCALE = 0.6F;
  private static final int DEFAULT_HIER_SML_EXPONENT = 0;
  private static final float DEFAULT_HIER_SML_SCALE = 0.5F;
  private static final float DEFAULT_LEARNING_ADAPT_RATE = 0.2F;
  private static final int DEFAULT_LEARNING_DONE_THRESHOLD = 20;
  private static final int DEFAULT_LEARNING_DURATION = 40;
  private static final int DEFAULT_LEARNING_VERIFY_DURATION = 10;
  private static final float DEFAULT_MASK_BLEND_BG = 0.65F;
  private static final float DEFAULT_MASK_BLEND_FG = 0.95F;
  private static final int DEFAULT_MASK_HEIGHT_EXPONENT = 8;
  private static final float DEFAULT_MASK_VERIFY_RATE = 0.25F;
  private static final int DEFAULT_MASK_WIDTH_EXPONENT = 8;
  private static final float DEFAULT_UV_SCALE_FACTOR = 1.35F;
  private static final float DEFAULT_WHITE_BALANCE_BLUE_CHANGE = 0.0F;
  private static final float DEFAULT_WHITE_BALANCE_RED_CHANGE = 0.0F;
  private static final int DEFAULT_WHITE_BALANCE_TOGGLE = 0;
  private static final float DEFAULT_Y_SCALE_FACTOR = 0.4F;
  private static final String DISTANCE_STORAGE_SCALE = "0.6";
  private static final String MASK_SMOOTH_EXPONENT = "2.0";
  private static final String MIN_VARIANCE = "3.0";
  private static final String RGB_TO_YUV_MATRIX = "0.299, -0.168736,  0.5,      0.000, 0.587, -0.331264, -0.418688, 0.000, 0.114,  0.5,      -0.081312, 0.000, 0.000,  0.5,       0.5,      1.000 ";
  private static final String TAG = "BackDropperFilter";
  private static final String VARIANCE_STORAGE_SCALE = "5.0";
  private static final String mAutomaticWhiteBalance = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float pyramid_depth;\nuniform bool autowb_toggle;\nvarying vec2 v_texcoord;\nvoid main() {\n   vec4 mean_video = texture2D(tex_sampler_0, v_texcoord, pyramid_depth);\n   vec4 mean_bg = texture2D(tex_sampler_1, v_texcoord, pyramid_depth);\n   float green_normalizer = mean_video.g / mean_bg.g;\n   vec4 adjusted_value = vec4(mean_bg.r / mean_video.r * green_normalizer, 1., \n                         mean_bg.b / mean_video.b * green_normalizer, 1.) * auto_wb_scale; \n   gl_FragColor = autowb_toggle ? adjusted_value : vec4(auto_wb_scale);\n}\n";
  private static final String mBgDistanceShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 variance = inv_var_scale * texture2D(tex_sampler_2, v_texcoord);\n\n  float dist_y = gauss_dist_y(fg.r, mean.r, variance.r);\n  float dist_uv = gauss_dist_uv(fg.gb, mean.gb, variance.gb);\n  gl_FragColor = vec4(0.5*fg.rg, dist_scale*dist_y, dist_scale*dist_uv);\n}\n";
  private static final String mBgMaskShader = "uniform sampler2D tex_sampler_0;\nuniform float accept_variance;\nuniform vec2 yuv_weights;\nuniform float scale_lrg;\nuniform float scale_mid;\nuniform float scale_sml;\nuniform float exp_lrg;\nuniform float exp_mid;\nuniform float exp_sml;\nvarying vec2 v_texcoord;\nbool is_fg(vec2 dist_yc, float accept_variance) {\n  return ( dot(yuv_weights, dist_yc) >= accept_variance );\n}\nvoid main() {\n  vec4 dist_lrg_sc = texture2D(tex_sampler_0, v_texcoord, exp_lrg);\n  vec4 dist_mid_sc = texture2D(tex_sampler_0, v_texcoord, exp_mid);\n  vec4 dist_sml_sc = texture2D(tex_sampler_0, v_texcoord, exp_sml);\n  vec2 dist_lrg = inv_dist_scale * dist_lrg_sc.ba;\n  vec2 dist_mid = inv_dist_scale * dist_mid_sc.ba;\n  vec2 dist_sml = inv_dist_scale * dist_sml_sc.ba;\n  vec2 norm_dist = 0.75 * dist_sml / accept_variance;\n  bool is_fg_lrg = is_fg(dist_lrg, accept_variance * scale_lrg);\n  bool is_fg_mid = is_fg_lrg || is_fg(dist_mid, accept_variance * scale_mid);\n  float is_fg_sml =\n      float(is_fg_mid || is_fg(dist_sml, accept_variance * scale_sml));\n  float alpha = 0.5 * is_fg_sml + 0.3 * float(is_fg_mid) + 0.2 * float(is_fg_lrg);\n  gl_FragColor = vec4(alpha, norm_dist, is_fg_sml);\n}\n";
  private static final String mBgSubtractForceShader = "  vec4 ghost_rgb = (fg_adjusted * 0.7 + vec4(0.3,0.3,0.4,0.))*0.65 + \n                   0.35*bg_rgb;\n  float glow_start = 0.75 * mask_blend_bg; \n  float glow_max   = mask_blend_bg; \n  gl_FragColor = mask.a < glow_start ? bg_rgb : \n                 mask.a < glow_max ? mix(bg_rgb, vec4(0.9,0.9,1.0,1.0), \n                                     (mask.a - glow_start) / (glow_max - glow_start) ) : \n                 mask.a < mask_blend_fg ? mix(vec4(0.9,0.9,1.0,1.0), ghost_rgb, \n                                    (mask.a - glow_max) / (mask_blend_fg - glow_max) ) : \n                 ghost_rgb;\n}\n";
  private static final String mBgSubtractShader = "uniform mat3 bg_fit_transform;\nuniform float mask_blend_bg;\nuniform float mask_blend_fg;\nuniform float exposure_change;\nuniform float whitebalancered_change;\nuniform float whitebalanceblue_change;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform sampler2D tex_sampler_3;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec2 bg_texcoord = (bg_fit_transform * vec3(v_texcoord, 1.)).xy;\n  vec4 bg_rgb = texture2D(tex_sampler_1, bg_texcoord);\n  vec4 wb_auto_scale = texture2D(tex_sampler_3, v_texcoord) * exposure_change / auto_wb_scale;\n  vec4 wb_manual_scale = vec4(1. + whitebalancered_change, 1., 1. + whitebalanceblue_change, 1.);\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord);\n  vec4 fg_adjusted = fg_rgb * wb_manual_scale * wb_auto_scale;\n  vec4 mask = texture2D(tex_sampler_2, v_texcoord, \n                      2.0);\n  float alpha = smoothstep(mask_blend_bg, mask_blend_fg, mask.a);\n  gl_FragColor = mix(bg_rgb, fg_adjusted, alpha);\n";
  private static final String[] mDebugOutputNames = { "debug1", "debug2" };
  private static final String[] mInputNames = { "video", "background" };
  private static final String mMaskVerifyShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float verify_rate;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 lastmask = texture2D(tex_sampler_0, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n  float newmask = mix(lastmask.a, mask.a, verify_rate);\n  gl_FragColor = vec4(0., 0., 0., newmask);\n}\n";
  private static final String[] mOutputNames = { "video" };
  private static String mSharedUtilShader = "precision mediump float;\nuniform float fg_adapt_rate;\nuniform float bg_adapt_rate;\nconst mat4 coeff_yuv = mat4(0.299, -0.168736,  0.5,      0.000, 0.587, -0.331264, -0.418688, 0.000, 0.114,  0.5,      -0.081312, 0.000, 0.000,  0.5,       0.5,      1.000 );\nconst float dist_scale = 0.6;\nconst float inv_dist_scale = 1. / dist_scale;\nconst float var_scale=5.0;\nconst float inv_var_scale = 1. / var_scale;\nconst float min_variance = inv_var_scale *3.0/ 256.;\nconst float auto_wb_scale = 0.25;\n\nfloat gauss_dist_y(float y, float mean, float variance) {\n  float dist = (y - mean) * (y - mean) / variance;\n  return dist;\n}\nfloat gauss_dist_uv(vec2 uv, vec2 mean, vec2 variance) {\n  vec2 dist = (uv - mean) * (uv - mean) / variance;\n  return dist.r + dist.g;\n}\nfloat local_adapt_rate(float alpha) {\n  return mix(bg_adapt_rate, fg_adapt_rate, alpha);\n}\n\n";
  private static final String mUpdateBgModelMeanShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_2, v_texcoord, \n                      2.0);\n\n  float alpha = local_adapt_rate(mask.a);\n  vec4 new_mean = mix(mean, fg, alpha);\n  gl_FragColor = new_mean;\n}\n";
  private static final String mUpdateBgModelVarianceShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform sampler2D tex_sampler_3;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 variance = inv_var_scale * texture2D(tex_sampler_2, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_3, v_texcoord, \n                      2.0);\n\n  float alpha = local_adapt_rate(mask.a);\n  vec4 cur_variance = (fg-mean)*(fg-mean);\n  vec4 new_variance = mix(variance, cur_variance, alpha);\n  new_variance = max(new_variance, vec4(min_variance));\n  gl_FragColor = var_scale * new_variance;\n}\n";
  private final int BACKGROUND_FILL_CROP = 2;
  private final int BACKGROUND_FIT = 1;
  private final int BACKGROUND_STRETCH = 0;
  private ShaderProgram copyShaderProgram;
  private boolean isOpen;
  @GenerateFieldPort(hasDefault=true, name="acceptStddev")
  private float mAcceptStddev = 0.85F;
  @GenerateFieldPort(hasDefault=true, name="adaptRateBg")
  private float mAdaptRateBg = 0.0F;
  @GenerateFieldPort(hasDefault=true, name="adaptRateFg")
  private float mAdaptRateFg = 0.0F;
  @GenerateFieldPort(hasDefault=true, name="learningAdaptRate")
  private float mAdaptRateLearning = 0.2F;
  private GLFrame mAutoWB;
  @GenerateFieldPort(hasDefault=true, name="autowbToggle")
  private int mAutoWBToggle = 0;
  private ShaderProgram mAutomaticWhiteBalanceProgram;
  private MutableFrameFormat mAverageFormat;
  @GenerateFieldPort(hasDefault=true, name="backgroundFitMode")
  private int mBackgroundFitMode = 2;
  private boolean mBackgroundFitModeChanged;
  private ShaderProgram mBgDistProgram;
  private GLFrame mBgInput;
  private ShaderProgram mBgMaskProgram;
  private GLFrame[] mBgMean;
  private ShaderProgram mBgSubtractProgram;
  private ShaderProgram mBgUpdateMeanProgram;
  private ShaderProgram mBgUpdateVarianceProgram;
  private GLFrame[] mBgVariance;
  @GenerateFieldPort(hasDefault=true, name="chromaScale")
  private float mChromaScale = 1.35F;
  private ShaderProgram mCopyOutProgram;
  private GLFrame mDistance;
  @GenerateFieldPort(hasDefault=true, name="exposureChange")
  private float mExposureChange = 1.0F;
  private int mFrameCount;
  @GenerateFieldPort(hasDefault=true, name="hierLrgExp")
  private int mHierarchyLrgExp = 3;
  @GenerateFieldPort(hasDefault=true, name="hierLrgScale")
  private float mHierarchyLrgScale = 0.7F;
  @GenerateFieldPort(hasDefault=true, name="hierMidExp")
  private int mHierarchyMidExp = 2;
  @GenerateFieldPort(hasDefault=true, name="hierMidScale")
  private float mHierarchyMidScale = 0.6F;
  @GenerateFieldPort(hasDefault=true, name="hierSmlExp")
  private int mHierarchySmlExp = 0;
  @GenerateFieldPort(hasDefault=true, name="hierSmlScale")
  private float mHierarchySmlScale = 0.5F;
  @GenerateFieldPort(hasDefault=true, name="learningDoneListener")
  private LearningDoneListener mLearningDoneListener = null;
  @GenerateFieldPort(hasDefault=true, name="learningDuration")
  private int mLearningDuration = 40;
  @GenerateFieldPort(hasDefault=true, name="learningVerifyDuration")
  private int mLearningVerifyDuration = 10;
  private final boolean mLogVerbose = Log.isLoggable("BackDropperFilter", 2);
  @GenerateFieldPort(hasDefault=true, name="lumScale")
  private float mLumScale = 0.4F;
  private GLFrame mMask;
  private GLFrame mMaskAverage;
  @GenerateFieldPort(hasDefault=true, name="maskBg")
  private float mMaskBg = 0.65F;
  @GenerateFieldPort(hasDefault=true, name="maskFg")
  private float mMaskFg = 0.95F;
  private MutableFrameFormat mMaskFormat;
  @GenerateFieldPort(hasDefault=true, name="maskHeightExp")
  private int mMaskHeightExp = 8;
  private GLFrame[] mMaskVerify;
  private ShaderProgram mMaskVerifyProgram;
  @GenerateFieldPort(hasDefault=true, name="maskWidthExp")
  private int mMaskWidthExp = 8;
  private MutableFrameFormat mMemoryFormat;
  @GenerateFieldPort(hasDefault=true, name="mirrorBg")
  private boolean mMirrorBg = false;
  @GenerateFieldPort(hasDefault=true, name="orientation")
  private int mOrientation = 0;
  private FrameFormat mOutputFormat;
  private boolean mPingPong;
  @GenerateFinalPort(hasDefault=true, name="provideDebugOutputs")
  private boolean mProvideDebugOutputs = false;
  private int mPyramidDepth;
  private float mRelativeAspect;
  private boolean mStartLearning;
  private int mSubsampleLevel;
  @GenerateFieldPort(hasDefault=true, name="useTheForce")
  private boolean mUseTheForce = false;
  @GenerateFieldPort(hasDefault=true, name="maskVerifyRate")
  private float mVerifyRate = 0.25F;
  private GLFrame mVideoInput;
  @GenerateFieldPort(hasDefault=true, name="whitebalanceblueChange")
  private float mWhiteBalanceBlueChange = 0.0F;
  @GenerateFieldPort(hasDefault=true, name="whitebalanceredChange")
  private float mWhiteBalanceRedChange = 0.0F;
  private long startTime = -1L;
  
  public BackDropperFilter(String paramString)
  {
    super(paramString);
    paramString = SystemProperties.get("ro.media.effect.bgdropper.adj");
    if (paramString.length() > 0) {
      try
      {
        mAcceptStddev += Float.parseFloat(paramString);
        if (mLogVerbose)
        {
          StringBuilder localStringBuilder1 = new java/lang/StringBuilder;
          localStringBuilder1.<init>();
          localStringBuilder1.append("Adjusting accept threshold by ");
          localStringBuilder1.append(paramString);
          localStringBuilder1.append(", now ");
          localStringBuilder1.append(mAcceptStddev);
          Log.v("BackDropperFilter", localStringBuilder1.toString());
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("Badly formatted property ro.media.effect.bgdropper.adj: ");
        localStringBuilder2.append(paramString);
        Log.e("BackDropperFilter", localStringBuilder2.toString());
      }
    }
  }
  
  private void allocateFrames(FrameFormat paramFrameFormat, FilterContext paramFilterContext)
  {
    if (!createMemoryFormat(paramFrameFormat)) {
      return;
    }
    if (mLogVerbose) {
      Log.v("BackDropperFilter", "Allocating BackDropperFilter frames");
    }
    int i = mMaskFormat.getSize();
    paramFrameFormat = new byte[i];
    byte[] arrayOfByte1 = new byte[i];
    byte[] arrayOfByte2 = new byte[i];
    for (int j = 0; j < i; j++)
    {
      paramFrameFormat[j] = ((byte)Byte.MIN_VALUE);
      arrayOfByte1[j] = ((byte)10);
      arrayOfByte2[j] = ((byte)0);
    }
    for (j = 0; j < 2; j++)
    {
      mBgMean[j] = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mMaskFormat));
      mBgMean[j].setData(paramFrameFormat, 0, i);
      mBgVariance[j] = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mMaskFormat));
      mBgVariance[j].setData(arrayOfByte1, 0, i);
      mMaskVerify[j] = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mMaskFormat));
      mMaskVerify[j].setData(arrayOfByte2, 0, i);
    }
    if (mLogVerbose) {
      Log.v("BackDropperFilter", "Done allocating texture for Mean and Variance objects!");
    }
    mDistance = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mMaskFormat));
    mMask = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mMaskFormat));
    mAutoWB = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mAverageFormat));
    mVideoInput = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mMemoryFormat));
    mBgInput = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mMemoryFormat));
    mMaskAverage = ((GLFrame)paramFilterContext.getFrameManager().newFrame(mAverageFormat));
    paramFrameFormat = new StringBuilder();
    paramFrameFormat.append(mSharedUtilShader);
    paramFrameFormat.append("uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 variance = inv_var_scale * texture2D(tex_sampler_2, v_texcoord);\n\n  float dist_y = gauss_dist_y(fg.r, mean.r, variance.r);\n  float dist_uv = gauss_dist_uv(fg.gb, mean.gb, variance.gb);\n  gl_FragColor = vec4(0.5*fg.rg, dist_scale*dist_y, dist_scale*dist_uv);\n}\n");
    mBgDistProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    mBgDistProgram.setHostValue("subsample_level", Float.valueOf(mSubsampleLevel));
    paramFrameFormat = new StringBuilder();
    paramFrameFormat.append(mSharedUtilShader);
    paramFrameFormat.append("uniform sampler2D tex_sampler_0;\nuniform float accept_variance;\nuniform vec2 yuv_weights;\nuniform float scale_lrg;\nuniform float scale_mid;\nuniform float scale_sml;\nuniform float exp_lrg;\nuniform float exp_mid;\nuniform float exp_sml;\nvarying vec2 v_texcoord;\nbool is_fg(vec2 dist_yc, float accept_variance) {\n  return ( dot(yuv_weights, dist_yc) >= accept_variance );\n}\nvoid main() {\n  vec4 dist_lrg_sc = texture2D(tex_sampler_0, v_texcoord, exp_lrg);\n  vec4 dist_mid_sc = texture2D(tex_sampler_0, v_texcoord, exp_mid);\n  vec4 dist_sml_sc = texture2D(tex_sampler_0, v_texcoord, exp_sml);\n  vec2 dist_lrg = inv_dist_scale * dist_lrg_sc.ba;\n  vec2 dist_mid = inv_dist_scale * dist_mid_sc.ba;\n  vec2 dist_sml = inv_dist_scale * dist_sml_sc.ba;\n  vec2 norm_dist = 0.75 * dist_sml / accept_variance;\n  bool is_fg_lrg = is_fg(dist_lrg, accept_variance * scale_lrg);\n  bool is_fg_mid = is_fg_lrg || is_fg(dist_mid, accept_variance * scale_mid);\n  float is_fg_sml =\n      float(is_fg_mid || is_fg(dist_sml, accept_variance * scale_sml));\n  float alpha = 0.5 * is_fg_sml + 0.3 * float(is_fg_mid) + 0.2 * float(is_fg_lrg);\n  gl_FragColor = vec4(alpha, norm_dist, is_fg_sml);\n}\n");
    mBgMaskProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    mBgMaskProgram.setHostValue("accept_variance", Float.valueOf(mAcceptStddev * mAcceptStddev));
    float f1 = mLumScale;
    float f2 = mChromaScale;
    mBgMaskProgram.setHostValue("yuv_weights", new float[] { f1, f2 });
    mBgMaskProgram.setHostValue("scale_lrg", Float.valueOf(mHierarchyLrgScale));
    mBgMaskProgram.setHostValue("scale_mid", Float.valueOf(mHierarchyMidScale));
    mBgMaskProgram.setHostValue("scale_sml", Float.valueOf(mHierarchySmlScale));
    mBgMaskProgram.setHostValue("exp_lrg", Float.valueOf(mSubsampleLevel + mHierarchyLrgExp));
    mBgMaskProgram.setHostValue("exp_mid", Float.valueOf(mSubsampleLevel + mHierarchyMidExp));
    mBgMaskProgram.setHostValue("exp_sml", Float.valueOf(mSubsampleLevel + mHierarchySmlExp));
    if (mUseTheForce)
    {
      paramFrameFormat = new StringBuilder();
      paramFrameFormat.append(mSharedUtilShader);
      paramFrameFormat.append("uniform mat3 bg_fit_transform;\nuniform float mask_blend_bg;\nuniform float mask_blend_fg;\nuniform float exposure_change;\nuniform float whitebalancered_change;\nuniform float whitebalanceblue_change;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform sampler2D tex_sampler_3;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec2 bg_texcoord = (bg_fit_transform * vec3(v_texcoord, 1.)).xy;\n  vec4 bg_rgb = texture2D(tex_sampler_1, bg_texcoord);\n  vec4 wb_auto_scale = texture2D(tex_sampler_3, v_texcoord) * exposure_change / auto_wb_scale;\n  vec4 wb_manual_scale = vec4(1. + whitebalancered_change, 1., 1. + whitebalanceblue_change, 1.);\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord);\n  vec4 fg_adjusted = fg_rgb * wb_manual_scale * wb_auto_scale;\n  vec4 mask = texture2D(tex_sampler_2, v_texcoord, \n                      2.0);\n  float alpha = smoothstep(mask_blend_bg, mask_blend_fg, mask.a);\n  gl_FragColor = mix(bg_rgb, fg_adjusted, alpha);\n");
      paramFrameFormat.append("  vec4 ghost_rgb = (fg_adjusted * 0.7 + vec4(0.3,0.3,0.4,0.))*0.65 + \n                   0.35*bg_rgb;\n  float glow_start = 0.75 * mask_blend_bg; \n  float glow_max   = mask_blend_bg; \n  gl_FragColor = mask.a < glow_start ? bg_rgb : \n                 mask.a < glow_max ? mix(bg_rgb, vec4(0.9,0.9,1.0,1.0), \n                                     (mask.a - glow_start) / (glow_max - glow_start) ) : \n                 mask.a < mask_blend_fg ? mix(vec4(0.9,0.9,1.0,1.0), ghost_rgb, \n                                    (mask.a - glow_max) / (mask_blend_fg - glow_max) ) : \n                 ghost_rgb;\n}\n");
      mBgSubtractProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    }
    else
    {
      paramFrameFormat = new StringBuilder();
      paramFrameFormat.append(mSharedUtilShader);
      paramFrameFormat.append("uniform mat3 bg_fit_transform;\nuniform float mask_blend_bg;\nuniform float mask_blend_fg;\nuniform float exposure_change;\nuniform float whitebalancered_change;\nuniform float whitebalanceblue_change;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform sampler2D tex_sampler_3;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec2 bg_texcoord = (bg_fit_transform * vec3(v_texcoord, 1.)).xy;\n  vec4 bg_rgb = texture2D(tex_sampler_1, bg_texcoord);\n  vec4 wb_auto_scale = texture2D(tex_sampler_3, v_texcoord) * exposure_change / auto_wb_scale;\n  vec4 wb_manual_scale = vec4(1. + whitebalancered_change, 1., 1. + whitebalanceblue_change, 1.);\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord);\n  vec4 fg_adjusted = fg_rgb * wb_manual_scale * wb_auto_scale;\n  vec4 mask = texture2D(tex_sampler_2, v_texcoord, \n                      2.0);\n  float alpha = smoothstep(mask_blend_bg, mask_blend_fg, mask.a);\n  gl_FragColor = mix(bg_rgb, fg_adjusted, alpha);\n");
      paramFrameFormat.append("}\n");
      mBgSubtractProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    }
    mBgSubtractProgram.setHostValue("bg_fit_transform", DEFAULT_BG_FIT_TRANSFORM);
    mBgSubtractProgram.setHostValue("mask_blend_bg", Float.valueOf(mMaskBg));
    mBgSubtractProgram.setHostValue("mask_blend_fg", Float.valueOf(mMaskFg));
    mBgSubtractProgram.setHostValue("exposure_change", Float.valueOf(mExposureChange));
    mBgSubtractProgram.setHostValue("whitebalanceblue_change", Float.valueOf(mWhiteBalanceBlueChange));
    mBgSubtractProgram.setHostValue("whitebalancered_change", Float.valueOf(mWhiteBalanceRedChange));
    paramFrameFormat = new StringBuilder();
    paramFrameFormat.append(mSharedUtilShader);
    paramFrameFormat.append("uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_2, v_texcoord, \n                      2.0);\n\n  float alpha = local_adapt_rate(mask.a);\n  vec4 new_mean = mix(mean, fg, alpha);\n  gl_FragColor = new_mean;\n}\n");
    mBgUpdateMeanProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    mBgUpdateMeanProgram.setHostValue("subsample_level", Float.valueOf(mSubsampleLevel));
    paramFrameFormat = new StringBuilder();
    paramFrameFormat.append(mSharedUtilShader);
    paramFrameFormat.append("uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform sampler2D tex_sampler_3;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 variance = inv_var_scale * texture2D(tex_sampler_2, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_3, v_texcoord, \n                      2.0);\n\n  float alpha = local_adapt_rate(mask.a);\n  vec4 cur_variance = (fg-mean)*(fg-mean);\n  vec4 new_variance = mix(variance, cur_variance, alpha);\n  new_variance = max(new_variance, vec4(min_variance));\n  gl_FragColor = var_scale * new_variance;\n}\n");
    mBgUpdateVarianceProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    mBgUpdateVarianceProgram.setHostValue("subsample_level", Float.valueOf(mSubsampleLevel));
    mCopyOutProgram = ShaderProgram.createIdentity(paramFilterContext);
    paramFrameFormat = new StringBuilder();
    paramFrameFormat.append(mSharedUtilShader);
    paramFrameFormat.append("uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float pyramid_depth;\nuniform bool autowb_toggle;\nvarying vec2 v_texcoord;\nvoid main() {\n   vec4 mean_video = texture2D(tex_sampler_0, v_texcoord, pyramid_depth);\n   vec4 mean_bg = texture2D(tex_sampler_1, v_texcoord, pyramid_depth);\n   float green_normalizer = mean_video.g / mean_bg.g;\n   vec4 adjusted_value = vec4(mean_bg.r / mean_video.r * green_normalizer, 1., \n                         mean_bg.b / mean_video.b * green_normalizer, 1.) * auto_wb_scale; \n   gl_FragColor = autowb_toggle ? adjusted_value : vec4(auto_wb_scale);\n}\n");
    mAutomaticWhiteBalanceProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    mAutomaticWhiteBalanceProgram.setHostValue("pyramid_depth", Float.valueOf(mPyramidDepth));
    mAutomaticWhiteBalanceProgram.setHostValue("autowb_toggle", Integer.valueOf(mAutoWBToggle));
    paramFrameFormat = new StringBuilder();
    paramFrameFormat.append(mSharedUtilShader);
    paramFrameFormat.append("uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float verify_rate;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 lastmask = texture2D(tex_sampler_0, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n  float newmask = mix(lastmask.a, mask.a, verify_rate);\n  gl_FragColor = vec4(0., 0., 0., newmask);\n}\n");
    mMaskVerifyProgram = new ShaderProgram(paramFilterContext, paramFrameFormat.toString());
    mMaskVerifyProgram.setHostValue("verify_rate", Float.valueOf(mVerifyRate));
    if (mLogVerbose)
    {
      paramFrameFormat = new StringBuilder();
      paramFrameFormat.append("Shader width set to ");
      paramFrameFormat.append(mMemoryFormat.getWidth());
      Log.v("BackDropperFilter", paramFrameFormat.toString());
    }
    mRelativeAspect = 1.0F;
    mFrameCount = 0;
    mStartLearning = true;
  }
  
  private boolean createMemoryFormat(FrameFormat paramFrameFormat)
  {
    if (mMemoryFormat != null) {
      return false;
    }
    if ((paramFrameFormat.getWidth() != 0) && (paramFrameFormat.getHeight() != 0))
    {
      mMaskFormat = paramFrameFormat.mutableCopy();
      int i = (int)Math.pow(2.0D, mMaskWidthExp);
      int j = (int)Math.pow(2.0D, mMaskHeightExp);
      mMaskFormat.setDimensions(i, j);
      mPyramidDepth = Math.max(mMaskWidthExp, mMaskHeightExp);
      mMemoryFormat = mMaskFormat.mutableCopy();
      int k = Math.max(mMaskWidthExp, pyramidLevel(paramFrameFormat.getWidth()));
      int m = Math.max(mMaskHeightExp, pyramidLevel(paramFrameFormat.getHeight()));
      mPyramidDepth = Math.max(k, m);
      int n = Math.max(i, (int)Math.pow(2.0D, k));
      int i1 = Math.max(j, (int)Math.pow(2.0D, m));
      mMemoryFormat.setDimensions(n, i1);
      mSubsampleLevel = (mPyramidDepth - Math.max(mMaskWidthExp, mMaskHeightExp));
      if (mLogVerbose)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Mask frames size ");
        localStringBuilder.append(i);
        localStringBuilder.append(" x ");
        localStringBuilder.append(j);
        Log.v("BackDropperFilter", localStringBuilder.toString());
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Pyramid levels ");
        localStringBuilder.append(k);
        localStringBuilder.append(" x ");
        localStringBuilder.append(m);
        Log.v("BackDropperFilter", localStringBuilder.toString());
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Memory frames size ");
        localStringBuilder.append(n);
        localStringBuilder.append(" x ");
        localStringBuilder.append(i1);
        Log.v("BackDropperFilter", localStringBuilder.toString());
      }
      mAverageFormat = paramFrameFormat.mutableCopy();
      mAverageFormat.setDimensions(1, 1);
      return true;
    }
    throw new RuntimeException("Attempting to process input frame with unknown size");
  }
  
  private int pyramidLevel(int paramInt)
  {
    return (int)Math.floor(Math.log10(paramInt) / Math.log10(2.0D)) - 1;
  }
  
  private void updateBgScaling(Frame paramFrame1, Frame paramFrame2, boolean paramBoolean)
  {
    float f1 = paramFrame1.getFormat().getWidth() / paramFrame1.getFormat().getHeight() / (paramFrame2.getFormat().getWidth() / paramFrame2.getFormat().getHeight());
    if ((f1 != mRelativeAspect) || (paramBoolean))
    {
      mRelativeAspect = f1;
      f1 = 0.0F;
      float f2 = 1.0F;
      float f3 = 0.0F;
      float f4 = 1.0F;
      switch (mBackgroundFitMode)
      {
      default: 
        break;
      case 2: 
        if (mRelativeAspect > 1.0F)
        {
          f3 = 0.5F - 0.5F / mRelativeAspect;
          f4 = 1.0F / mRelativeAspect;
        }
        else
        {
          f1 = 0.5F - mRelativeAspect * 0.5F;
          f2 = mRelativeAspect;
        }
        break;
      case 1: 
        if (mRelativeAspect > 1.0F)
        {
          f1 = 0.5F - mRelativeAspect * 0.5F;
          f2 = 1.0F * mRelativeAspect;
        }
        else
        {
          f3 = 0.5F - 0.5F / mRelativeAspect;
          f4 = 1.0F / mRelativeAspect;
        }
        break;
      }
      float f5 = f1;
      float f6 = f2;
      float f7 = f3;
      float f8 = f4;
      if (mMirrorBg)
      {
        if (mLogVerbose) {
          Log.v("BackDropperFilter", "Mirroring the background!");
        }
        if ((mOrientation != 0) && (mOrientation != 180))
        {
          f8 = -f4;
          f7 = 1.0F - f3;
          f5 = f1;
          f6 = f2;
        }
        else
        {
          f6 = -f2;
          f5 = 1.0F - f1;
          f8 = f4;
          f7 = f3;
        }
      }
      if (mLogVerbose)
      {
        paramFrame1 = new StringBuilder();
        paramFrame1.append("bgTransform: xMin, yMin, xWidth, yWidth : ");
        paramFrame1.append(f5);
        paramFrame1.append(", ");
        paramFrame1.append(f7);
        paramFrame1.append(", ");
        paramFrame1.append(f6);
        paramFrame1.append(", ");
        paramFrame1.append(f8);
        paramFrame1.append(", mRelAspRatio = ");
        paramFrame1.append(mRelativeAspect);
        Log.v("BackDropperFilter", paramFrame1.toString());
      }
      mBgSubtractProgram.setHostValue("bg_fit_transform", new float[] { f6, 0.0F, 0.0F, 0.0F, f8, 0.0F, f5, f7, 1.0F });
    }
  }
  
  public void close(FilterContext paramFilterContext)
  {
    if (mMemoryFormat == null) {
      return;
    }
    if (mLogVerbose) {
      Log.v("BackDropperFilter", "Filter Closing!");
    }
    for (int i = 0; i < 2; i++)
    {
      mBgMean[i].release();
      mBgVariance[i].release();
      mMaskVerify[i].release();
    }
    mDistance.release();
    mMask.release();
    mAutoWB.release();
    mVideoInput.release();
    mBgInput.release();
    mMaskAverage.release();
    mMemoryFormat = null;
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (paramString.equals("backgroundFitMode"))
    {
      mBackgroundFitModeChanged = true;
    }
    else if (paramString.equals("acceptStddev"))
    {
      mBgMaskProgram.setHostValue("accept_variance", Float.valueOf(mAcceptStddev * mAcceptStddev));
    }
    else if (paramString.equals("hierLrgScale"))
    {
      mBgMaskProgram.setHostValue("scale_lrg", Float.valueOf(mHierarchyLrgScale));
    }
    else if (paramString.equals("hierMidScale"))
    {
      mBgMaskProgram.setHostValue("scale_mid", Float.valueOf(mHierarchyMidScale));
    }
    else if (paramString.equals("hierSmlScale"))
    {
      mBgMaskProgram.setHostValue("scale_sml", Float.valueOf(mHierarchySmlScale));
    }
    else if (paramString.equals("hierLrgExp"))
    {
      mBgMaskProgram.setHostValue("exp_lrg", Float.valueOf(mSubsampleLevel + mHierarchyLrgExp));
    }
    else if (paramString.equals("hierMidExp"))
    {
      mBgMaskProgram.setHostValue("exp_mid", Float.valueOf(mSubsampleLevel + mHierarchyMidExp));
    }
    else if (paramString.equals("hierSmlExp"))
    {
      mBgMaskProgram.setHostValue("exp_sml", Float.valueOf(mSubsampleLevel + mHierarchySmlExp));
    }
    else if ((!paramString.equals("lumScale")) && (!paramString.equals("chromaScale")))
    {
      if (paramString.equals("maskBg")) {
        mBgSubtractProgram.setHostValue("mask_blend_bg", Float.valueOf(mMaskBg));
      } else if (paramString.equals("maskFg")) {
        mBgSubtractProgram.setHostValue("mask_blend_fg", Float.valueOf(mMaskFg));
      } else if (paramString.equals("exposureChange")) {
        mBgSubtractProgram.setHostValue("exposure_change", Float.valueOf(mExposureChange));
      } else if (paramString.equals("whitebalanceredChange")) {
        mBgSubtractProgram.setHostValue("whitebalancered_change", Float.valueOf(mWhiteBalanceRedChange));
      } else if (paramString.equals("whitebalanceblueChange")) {
        mBgSubtractProgram.setHostValue("whitebalanceblue_change", Float.valueOf(mWhiteBalanceBlueChange));
      } else if (paramString.equals("autowbToggle")) {
        mAutomaticWhiteBalanceProgram.setHostValue("autowb_toggle", Integer.valueOf(mAutoWBToggle));
      }
    }
    else
    {
      float f1 = mLumScale;
      float f2 = mChromaScale;
      mBgMaskProgram.setHostValue("yuv_weights", new float[] { f1, f2 });
    }
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    paramFrameFormat = paramFrameFormat.mutableCopy();
    if (!Arrays.asList(mOutputNames).contains(paramString)) {
      paramFrameFormat.setDimensions(0, 0);
    }
    return paramFrameFormat;
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("BackDropperFilter", "Preparing BackDropperFilter!");
    }
    mBgMean = new GLFrame[2];
    mBgVariance = new GLFrame[2];
    mMaskVerify = new GLFrame[2];
    copyShaderProgram = ShaderProgram.createIdentity(paramFilterContext);
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("video");
    Object localObject1 = pullInput("background");
    allocateFrames(localFrame.getFormat(), paramFilterContext);
    if (mStartLearning)
    {
      if (mLogVerbose) {
        Log.v("BackDropperFilter", "Starting learning");
      }
      mBgUpdateMeanProgram.setHostValue("bg_adapt_rate", Float.valueOf(mAdaptRateLearning));
      mBgUpdateMeanProgram.setHostValue("fg_adapt_rate", Float.valueOf(mAdaptRateLearning));
      mBgUpdateVarianceProgram.setHostValue("bg_adapt_rate", Float.valueOf(mAdaptRateLearning));
      mBgUpdateVarianceProgram.setHostValue("fg_adapt_rate", Float.valueOf(mAdaptRateLearning));
      mFrameCount = 0;
    }
    boolean bool1 = mPingPong ^ true;
    boolean bool2 = mPingPong;
    mPingPong ^= true;
    updateBgScaling(localFrame, (Frame)localObject1, mBackgroundFitModeChanged);
    mBackgroundFitModeChanged = false;
    copyShaderProgram.process(localFrame, mVideoInput);
    copyShaderProgram.process((Frame)localObject1, mBgInput);
    mVideoInput.generateMipMap();
    mVideoInput.setTextureParameter(10241, 9985);
    mBgInput.generateMipMap();
    mBgInput.setTextureParameter(10241, 9985);
    if (mStartLearning)
    {
      copyShaderProgram.process(mVideoInput, mBgMean[bool1]);
      mStartLearning = false;
    }
    GLFrame localGLFrame1 = mVideoInput;
    GLFrame localGLFrame2 = mBgMean[bool1];
    Object localObject2 = mBgVariance[bool1];
    Object localObject3 = mBgDistProgram;
    GLFrame localGLFrame3 = mDistance;
    ((ShaderProgram)localObject3).process(new Frame[] { localGLFrame1, localGLFrame2, localObject2 }, localGLFrame3);
    mDistance.generateMipMap();
    mDistance.setTextureParameter(10241, 9985);
    mBgMaskProgram.process(mDistance, mMask);
    mMask.generateMipMap();
    mMask.setTextureParameter(10241, 9985);
    localGLFrame1 = mVideoInput;
    localGLFrame3 = mBgInput;
    localObject2 = mAutomaticWhiteBalanceProgram;
    localObject3 = mAutoWB;
    ((ShaderProgram)localObject2).process(new Frame[] { localGLFrame1, localGLFrame3 }, (Frame)localObject3);
    if (mFrameCount <= mLearningDuration)
    {
      pushOutput("video", localFrame);
      if (mFrameCount == mLearningDuration - mLearningVerifyDuration)
      {
        copyShaderProgram.process(mMask, mMaskVerify[bool2]);
        mBgUpdateMeanProgram.setHostValue("bg_adapt_rate", Float.valueOf(mAdaptRateBg));
        mBgUpdateMeanProgram.setHostValue("fg_adapt_rate", Float.valueOf(mAdaptRateFg));
        mBgUpdateVarianceProgram.setHostValue("bg_adapt_rate", Float.valueOf(mAdaptRateBg));
        mBgUpdateVarianceProgram.setHostValue("fg_adapt_rate", Float.valueOf(mAdaptRateFg));
      }
      else if (mFrameCount > mLearningDuration - mLearningVerifyDuration)
      {
        localObject2 = mMaskVerify[bool1];
        localGLFrame1 = mMask;
        localObject1 = mMaskVerifyProgram;
        localObject3 = mMaskVerify[bool2];
        ((ShaderProgram)localObject1).process(new Frame[] { localObject2, localGLFrame1 }, (Frame)localObject3);
        mMaskVerify[bool2].generateMipMap();
        mMaskVerify[bool2].setTextureParameter(10241, 9985);
      }
      if (mFrameCount == mLearningDuration)
      {
        copyShaderProgram.process(mMaskVerify[bool2], mMaskAverage);
        int i = mMaskAverage.getData().array()[3] & 0xFF;
        if (mLogVerbose) {
          Log.v("BackDropperFilter", String.format("Mask_average is %d, threshold is %d", new Object[] { Integer.valueOf(i), Integer.valueOf(20) }));
        }
        if (i >= 20)
        {
          mStartLearning = true;
        }
        else
        {
          if (mLogVerbose) {
            Log.v("BackDropperFilter", "Learning done");
          }
          if (mLearningDoneListener != null) {
            mLearningDoneListener.onLearningDone(this);
          }
        }
      }
    }
    else
    {
      localObject3 = paramFilterContext.getFrameManager().newFrame(localFrame.getFormat());
      localGLFrame1 = mMask;
      localObject2 = mAutoWB;
      mBgSubtractProgram.process(new Frame[] { localFrame, localObject1, localGLFrame1, localObject2 }, (Frame)localObject3);
      pushOutput("video", (Frame)localObject3);
      ((Frame)localObject3).release();
    }
    if ((mFrameCount < mLearningDuration - mLearningVerifyDuration) || (mAdaptRateBg > 0.0D) || (mAdaptRateFg > 0.0D))
    {
      localObject2 = mVideoInput;
      localObject3 = mBgMean[bool1];
      localGLFrame1 = mMask;
      localObject1 = mBgUpdateMeanProgram;
      localGLFrame3 = mBgMean[bool2];
      ((ShaderProgram)localObject1).process(new Frame[] { localObject2, localObject3, localGLFrame1 }, localGLFrame3);
      mBgMean[bool2].generateMipMap();
      mBgMean[bool2].setTextureParameter(10241, 9985);
      localObject3 = mVideoInput;
      localGLFrame3 = mBgMean[bool1];
      localGLFrame2 = mBgVariance[bool1];
      localGLFrame1 = mMask;
      localObject2 = mBgUpdateVarianceProgram;
      localObject1 = mBgVariance[bool2];
      ((ShaderProgram)localObject2).process(new Frame[] { localObject3, localGLFrame3, localGLFrame2, localGLFrame1 }, (Frame)localObject1);
      mBgVariance[bool2].generateMipMap();
      mBgVariance[bool2].setTextureParameter(10241, 9985);
    }
    if (mProvideDebugOutputs)
    {
      localObject1 = paramFilterContext.getFrameManager().newFrame(localFrame.getFormat());
      mCopyOutProgram.process(localFrame, (Frame)localObject1);
      pushOutput("debug1", (Frame)localObject1);
      ((Frame)localObject1).release();
      localFrame = paramFilterContext.getFrameManager().newFrame(mMemoryFormat);
      mCopyOutProgram.process(mMask, localFrame);
      pushOutput("debug2", localFrame);
      localFrame.release();
    }
    mFrameCount += 1;
    if ((mLogVerbose) && (mFrameCount % 30 == 0)) {
      if (startTime == -1L)
      {
        paramFilterContext.getGLEnvironment().activate();
        GLES20.glFinish();
        startTime = SystemClock.elapsedRealtime();
      }
      else
      {
        paramFilterContext.getGLEnvironment().activate();
        GLES20.glFinish();
        long l = SystemClock.elapsedRealtime();
        paramFilterContext = new StringBuilder();
        paramFilterContext.append("Avg. frame duration: ");
        paramFilterContext.append(String.format("%.2f", new Object[] { Double.valueOf((l - startTime) / 30.0D) }));
        paramFilterContext.append(" ms. Avg. fps: ");
        paramFilterContext.append(String.format("%.2f", new Object[] { Double.valueOf(1000.0D / ((l - startTime) / 30.0D)) }));
        Log.v("BackDropperFilter", paramFilterContext.toString());
        startTime = l;
      }
    }
  }
  
  public void relearn()
  {
    try
    {
      mStartLearning = true;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setupPorts()
  {
    int i = 0;
    MutableFrameFormat localMutableFrameFormat = ImageFormat.create(3, 0);
    String[] arrayOfString = mInputNames;
    int j = arrayOfString.length;
    for (int k = 0; k < j; k++) {
      addMaskedInputPort(arrayOfString[k], localMutableFrameFormat);
    }
    arrayOfString = mOutputNames;
    j = arrayOfString.length;
    for (k = 0; k < j; k++) {
      addOutputBasedOnInput(arrayOfString[k], "video");
    }
    if (mProvideDebugOutputs)
    {
      arrayOfString = mDebugOutputNames;
      j = arrayOfString.length;
      for (k = i; k < j; k++) {
        addOutputBasedOnInput(arrayOfString[k], "video");
      }
    }
  }
  
  public static abstract interface LearningDoneListener
  {
    public abstract void onLearningDone(BackDropperFilter paramBackDropperFilter);
  }
}
