package android.filterpacks.videosrc;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;

public class MediaSource
  extends Filter
{
  private static final int NEWFRAME_TIMEOUT = 100;
  private static final int NEWFRAME_TIMEOUT_REPEAT = 10;
  private static final int PREP_TIMEOUT = 100;
  private static final int PREP_TIMEOUT_REPEAT = 100;
  private static final String TAG = "MediaSource";
  private static final float[] mSourceCoords_0 = { 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
  private static final float[] mSourceCoords_180 = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F };
  private static final float[] mSourceCoords_270 = { 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F };
  private static final float[] mSourceCoords_90 = { 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F };
  private boolean mCompleted;
  @GenerateFieldPort(hasDefault=true, name="context")
  private Context mContext = null;
  private ShaderProgram mFrameExtractor;
  private final String mFrameShader = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n}\n";
  private boolean mGotSize;
  private int mHeight;
  private final boolean mLogVerbose = Log.isLoggable("MediaSource", 2);
  @GenerateFieldPort(hasDefault=true, name="loop")
  private boolean mLooping = true;
  private GLFrame mMediaFrame;
  private MediaPlayer mMediaPlayer;
  private boolean mNewFrameAvailable = false;
  @GenerateFieldPort(hasDefault=true, name="orientation")
  private int mOrientation = 0;
  private boolean mOrientationUpdated;
  private MutableFrameFormat mOutputFormat;
  private boolean mPaused;
  private boolean mPlaying;
  private boolean mPrepared;
  @GenerateFieldPort(hasDefault=true, name="sourceIsUrl")
  private boolean mSelectedIsUrl = false;
  @GenerateFieldPort(hasDefault=true, name="sourceAsset")
  private AssetFileDescriptor mSourceAsset = null;
  @GenerateFieldPort(hasDefault=true, name="sourceUrl")
  private String mSourceUrl = "";
  private SurfaceTexture mSurfaceTexture;
  @GenerateFieldPort(hasDefault=true, name="volume")
  private float mVolume = 0.0F;
  @GenerateFinalPort(hasDefault=true, name="waitForNewFrame")
  private boolean mWaitForNewFrame = true;
  private int mWidth;
  private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener()
  {
    public void onCompletion(MediaPlayer arg1)
    {
      if (mLogVerbose) {
        Log.v("MediaSource", "MediaPlayer has completed playback");
      }
      synchronized (MediaSource.this)
      {
        MediaSource.access$702(MediaSource.this, true);
        return;
      }
    }
  };
  private SurfaceTexture.OnFrameAvailableListener onMediaFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener()
  {
    public void onFrameAvailable(SurfaceTexture arg1)
    {
      if (mLogVerbose) {
        Log.v("MediaSource", "New frame from media player");
      }
      synchronized (MediaSource.this)
      {
        if (mLogVerbose) {
          Log.v("MediaSource", "New frame: notify");
        }
        MediaSource.access$802(MediaSource.this, true);
        notify();
        if (mLogVerbose) {
          Log.v("MediaSource", "New frame: notify done");
        }
        return;
      }
    }
  };
  private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener()
  {
    public void onPrepared(MediaPlayer arg1)
    {
      if (mLogVerbose) {
        Log.v("MediaSource", "MediaPlayer is prepared");
      }
      synchronized (MediaSource.this)
      {
        MediaSource.access$602(MediaSource.this, true);
        notify();
        return;
      }
    }
  };
  private MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener()
  {
    public void onVideoSizeChanged(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (mLogVerbose)
      {
        paramAnonymousMediaPlayer = new StringBuilder();
        paramAnonymousMediaPlayer.append("MediaPlayer sent dimensions: ");
        paramAnonymousMediaPlayer.append(paramAnonymousInt1);
        paramAnonymousMediaPlayer.append(" x ");
        paramAnonymousMediaPlayer.append(paramAnonymousInt2);
        Log.v("MediaSource", paramAnonymousMediaPlayer.toString());
      }
      if (!mGotSize)
      {
        if ((mOrientation != 0) && (mOrientation != 180)) {
          mOutputFormat.setDimensions(paramAnonymousInt2, paramAnonymousInt1);
        } else {
          mOutputFormat.setDimensions(paramAnonymousInt1, paramAnonymousInt2);
        }
        MediaSource.access$402(MediaSource.this, paramAnonymousInt1);
        MediaSource.access$502(MediaSource.this, paramAnonymousInt2);
      }
      else if ((mOutputFormat.getWidth() != paramAnonymousInt1) || (mOutputFormat.getHeight() != paramAnonymousInt2))
      {
        Log.e("MediaSource", "Multiple video size change events received!");
      }
      synchronized (MediaSource.this)
      {
        MediaSource.access$102(MediaSource.this, true);
        notify();
        return;
      }
    }
  };
  
  public MediaSource(String paramString)
  {
    super(paramString);
  }
  
  private void createFormats()
  {
    mOutputFormat = ImageFormat.create(3, 3);
  }
  
  private boolean setupMediaPlayer(boolean paramBoolean)
  {
    try
    {
      mPrepared = false;
      mGotSize = false;
      mPlaying = false;
      mPaused = false;
      mCompleted = false;
      mNewFrameAvailable = false;
      if (mLogVerbose) {
        Log.v("MediaSource", "Setting up playback.");
      }
      if (mMediaPlayer != null)
      {
        if (mLogVerbose) {
          Log.v("MediaSource", "Resetting existing MediaPlayer.");
        }
        mMediaPlayer.reset();
      }
      else
      {
        if (mLogVerbose) {
          Log.v("MediaSource", "Creating new MediaPlayer.");
        }
        localObject1 = new android/media/MediaPlayer;
        ((MediaPlayer)localObject1).<init>();
        mMediaPlayer = ((MediaPlayer)localObject1);
      }
      Object localObject1 = mMediaPlayer;
      if (localObject1 != null)
      {
        if (paramBoolean) {
          try
          {
            if (mLogVerbose)
            {
              localObject1 = new java/lang/StringBuilder;
              ((StringBuilder)localObject1).<init>();
              ((StringBuilder)localObject1).append("Setting MediaPlayer source to URI ");
              ((StringBuilder)localObject1).append(mSourceUrl);
              Log.v("MediaSource", ((StringBuilder)localObject1).toString());
            }
            if (mContext == null) {
              mMediaPlayer.setDataSource(mSourceUrl);
            } else {
              mMediaPlayer.setDataSource(mContext, Uri.parse(mSourceUrl.toString()));
            }
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            break label404;
          }
          catch (IOException localIOException)
          {
            break label476;
          }
        }
        if (mLogVerbose)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Setting MediaPlayer source to asset ");
          ((StringBuilder)localObject2).append(mSourceAsset);
          Log.v("MediaSource", ((StringBuilder)localObject2).toString());
        }
        mMediaPlayer.setDataSource(mSourceAsset.getFileDescriptor(), mSourceAsset.getStartOffset(), mSourceAsset.getLength());
        mMediaPlayer.setLooping(mLooping);
        mMediaPlayer.setVolume(mVolume, mVolume);
        localObject2 = new android/view/Surface;
        ((Surface)localObject2).<init>(mSurfaceTexture);
        mMediaPlayer.setSurface((Surface)localObject2);
        ((Surface)localObject2).release();
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        mSurfaceTexture.setOnFrameAvailableListener(onMediaFrameAvailableListener);
        if (mLogVerbose) {
          Log.v("MediaSource", "Preparing MediaPlayer.");
        }
        mMediaPlayer.prepareAsync();
        return true;
        label404:
        mMediaPlayer.release();
        mMediaPlayer = null;
        if (paramBoolean)
        {
          localRuntimeException = new java/lang/RuntimeException;
          localRuntimeException.<init>(String.format("Unable to set MediaPlayer to URL %s!", new Object[] { mSourceUrl }), (Throwable)localObject2);
          throw localRuntimeException;
        }
        RuntimeException localRuntimeException = new java/lang/RuntimeException;
        localRuntimeException.<init>(String.format("Unable to set MediaPlayer to asset %s!", new Object[] { mSourceAsset }), (Throwable)localObject2);
        throw localRuntimeException;
        label476:
        mMediaPlayer.release();
        mMediaPlayer = null;
        if (paramBoolean)
        {
          localRuntimeException = new java/lang/RuntimeException;
          localRuntimeException.<init>(String.format("Unable to set MediaPlayer to URL %s!", new Object[] { mSourceUrl }), (Throwable)localObject2);
          throw localRuntimeException;
        }
        localRuntimeException = new java/lang/RuntimeException;
        localRuntimeException.<init>(String.format("Unable to set MediaPlayer to asset %s!", new Object[] { mSourceAsset }), (Throwable)localObject2);
        throw localRuntimeException;
      }
      Object localObject2 = new java/lang/RuntimeException;
      ((RuntimeException)localObject2).<init>("Unable to create a MediaPlayer!");
      throw ((Throwable)localObject2);
    }
    finally {}
  }
  
  public void close(FilterContext paramFilterContext)
  {
    if (mMediaPlayer.isPlaying()) {
      mMediaPlayer.stop();
    }
    mPrepared = false;
    mGotSize = false;
    mPlaying = false;
    mPaused = false;
    mCompleted = false;
    mNewFrameAvailable = false;
    mMediaPlayer.release();
    mMediaPlayer = null;
    mSurfaceTexture.release();
    mSurfaceTexture = null;
    if (mLogVerbose) {
      Log.v("MediaSource", "MediaSource closed");
    }
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaSource", "Parameter update");
    }
    if (paramString.equals("sourceUrl"))
    {
      if (isOpen())
      {
        if (mLogVerbose) {
          Log.v("MediaSource", "Opening new source URL");
        }
        if (mSelectedIsUrl) {
          setupMediaPlayer(mSelectedIsUrl);
        }
      }
    }
    else if (paramString.equals("sourceAsset"))
    {
      if (isOpen())
      {
        if (mLogVerbose) {
          Log.v("MediaSource", "Opening new source FD");
        }
        if (!mSelectedIsUrl) {
          setupMediaPlayer(mSelectedIsUrl);
        }
      }
    }
    else if (paramString.equals("loop"))
    {
      if (isOpen()) {
        mMediaPlayer.setLooping(mLooping);
      }
    }
    else if (paramString.equals("sourceIsUrl"))
    {
      if (isOpen())
      {
        if (mSelectedIsUrl)
        {
          if (mLogVerbose) {
            Log.v("MediaSource", "Opening new source URL");
          }
        }
        else if (mLogVerbose) {
          Log.v("MediaSource", "Opening new source Asset");
        }
        setupMediaPlayer(mSelectedIsUrl);
      }
    }
    else if (paramString.equals("volume"))
    {
      if (isOpen()) {
        mMediaPlayer.setVolume(mVolume, mVolume);
      }
    }
    else if ((paramString.equals("orientation")) && (mGotSize))
    {
      if ((mOrientation != 0) && (mOrientation != 180)) {
        mOutputFormat.setDimensions(mHeight, mWidth);
      } else {
        mOutputFormat.setDimensions(mWidth, mHeight);
      }
      mOrientationUpdated = true;
    }
  }
  
  public void open(FilterContext paramFilterContext)
  {
    if (mLogVerbose)
    {
      Log.v("MediaSource", "Opening MediaSource");
      if (mSelectedIsUrl)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Current URL is ");
        localStringBuilder.append(mSourceUrl);
        Log.v("MediaSource", localStringBuilder.toString());
      }
      else
      {
        Log.v("MediaSource", "Current source is Asset!");
      }
    }
    mMediaFrame = ((GLFrame)paramFilterContext.getFrameManager().newBoundFrame(mOutputFormat, 104, 0L));
    mSurfaceTexture = new SurfaceTexture(mMediaFrame.getTextureId());
    if (setupMediaPlayer(mSelectedIsUrl)) {
      return;
    }
    throw new RuntimeException("Error setting up MediaPlayer!");
  }
  
  public void pauseVideo(boolean paramBoolean)
  {
    try
    {
      if (isOpen()) {
        if ((paramBoolean) && (!mPaused)) {
          mMediaPlayer.pause();
        } else if ((!paramBoolean) && (mPaused)) {
          mMediaPlayer.start();
        }
      }
      mPaused = paramBoolean;
      return;
    }
    finally {}
  }
  
  protected void prepare(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaSource", "Preparing MediaSource");
    }
    mFrameExtractor = new ShaderProgram(paramFilterContext, "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES tex_sampler_0;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_FragColor = texture2D(tex_sampler_0, v_texcoord);\n}\n");
    mFrameExtractor.setSourceRect(0.0F, 1.0F, 1.0F, -1.0F);
    createFormats();
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaSource", "Processing new frame");
    }
    if (mMediaPlayer != null)
    {
      if (mCompleted)
      {
        closeOutputPort("video");
        return;
      }
      int i;
      if (!mPlaying)
      {
        if (mLogVerbose) {
          Log.v("MediaSource", "Waiting for preparation to complete");
        }
        i = 0;
        do
        {
          if ((mGotSize) && (mPrepared))
          {
            if (mLogVerbose) {
              Log.v("MediaSource", "Starting playback");
            }
            mMediaPlayer.start();
            break;
          }
          try
          {
            wait(100L);
          }
          catch (InterruptedException localInterruptedException1) {}
          if (mCompleted)
          {
            closeOutputPort("video");
            return;
          }
          i++;
        } while (i != 100);
        mMediaPlayer.release();
        throw new RuntimeException("MediaPlayer timed out while preparing!");
      }
      if ((!mPaused) || (!mPlaying))
      {
        if (mWaitForNewFrame)
        {
          if (mLogVerbose) {
            Log.v("MediaSource", "Waiting for new frame");
          }
          for (i = 0; !mNewFrameAvailable; i++)
          {
            if (i == 10)
            {
              if (mCompleted)
              {
                closeOutputPort("video");
                return;
              }
              throw new RuntimeException("Timeout waiting for new frame!");
            }
            try
            {
              wait(100L);
            }
            catch (InterruptedException localInterruptedException2)
            {
              if (mLogVerbose) {
                Log.v("MediaSource", "interrupted");
              }
            }
          }
          mNewFrameAvailable = false;
          if (mLogVerbose) {
            Log.v("MediaSource", "Got new frame");
          }
        }
        mSurfaceTexture.updateTexImage();
        mOrientationUpdated = true;
      }
      Object localObject1;
      if (mOrientationUpdated)
      {
        Object localObject2 = new float[16];
        mSurfaceTexture.getTransformMatrix((float[])localObject2);
        localObject1 = new float[16];
        i = mOrientation;
        if (i != 90)
        {
          if (i != 180)
          {
            if (i != 270) {
              Matrix.multiplyMM((float[])localObject1, 0, (float[])localObject2, 0, mSourceCoords_0, 0);
            } else {
              Matrix.multiplyMM((float[])localObject1, 0, (float[])localObject2, 0, mSourceCoords_270, 0);
            }
          }
          else {
            Matrix.multiplyMM((float[])localObject1, 0, (float[])localObject2, 0, mSourceCoords_180, 0);
          }
        }
        else {
          Matrix.multiplyMM((float[])localObject1, 0, (float[])localObject2, 0, mSourceCoords_90, 0);
        }
        if (mLogVerbose)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("OrientationHint = ");
          ((StringBuilder)localObject2).append(mOrientation);
          Log.v("MediaSource", ((StringBuilder)localObject2).toString());
          Log.v("MediaSource", String.format("SetSourceRegion: %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f", new Object[] { Float.valueOf(localObject1[4]), Float.valueOf(localObject1[5]), Float.valueOf(localObject1[0]), Float.valueOf(localObject1[1]), Float.valueOf(localObject1[12]), Float.valueOf(localObject1[13]), Float.valueOf(localObject1[8]), Float.valueOf(localObject1[9]) }));
        }
        mFrameExtractor.setSourceRegion(localObject1[4], localObject1[5], localObject1[0], localObject1[1], localObject1[12], localObject1[13], localObject1[8], localObject1[9]);
        mOrientationUpdated = false;
      }
      paramFilterContext = paramFilterContext.getFrameManager().newFrame(mOutputFormat);
      mFrameExtractor.process(mMediaFrame, paramFilterContext);
      long l = mSurfaceTexture.getTimestamp();
      if (mLogVerbose)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Timestamp: ");
        ((StringBuilder)localObject1).append(l / 1.0E9D);
        ((StringBuilder)localObject1).append(" s");
        Log.v("MediaSource", ((StringBuilder)localObject1).toString());
      }
      paramFilterContext.setTimestamp(l);
      pushOutput("video", paramFilterContext);
      paramFilterContext.release();
      mPlaying = true;
      return;
    }
    throw new NullPointerException("Unexpected null media player!");
  }
  
  public void setupPorts()
  {
    addOutputPort("video", ImageFormat.create(3, 3));
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mMediaFrame != null) {
      mMediaFrame.release();
    }
  }
}
