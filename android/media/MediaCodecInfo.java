package android.media;

import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class MediaCodecInfo
{
  private static final Range<Integer> BITRATE_RANGE = Range.create(Integer.valueOf(0), Integer.valueOf(500000000));
  private static final int DEFAULT_MAX_SUPPORTED_INSTANCES = 32;
  private static final int ERROR_NONE_SUPPORTED = 4;
  private static final int ERROR_UNRECOGNIZED = 1;
  private static final int ERROR_UNSUPPORTED = 2;
  private static final Range<Integer> FRAME_RATE_RANGE;
  private static final int MAX_SUPPORTED_INSTANCES_LIMIT = 256;
  private static final Range<Integer> POSITIVE_INTEGERS = Range.create(Integer.valueOf(1), Integer.valueOf(Integer.MAX_VALUE));
  private static final Range<Long> POSITIVE_LONGS = Range.create(Long.valueOf(1L), Long.valueOf(Long.MAX_VALUE));
  private static final Range<Rational> POSITIVE_RATIONALS = Range.create(new Rational(1, Integer.MAX_VALUE), new Rational(Integer.MAX_VALUE, 1));
  private static final Range<Integer> SIZE_RANGE = Range.create(Integer.valueOf(1), Integer.valueOf(32768));
  private Map<String, CodecCapabilities> mCaps;
  private boolean mIsEncoder;
  private String mName;
  
  static
  {
    FRAME_RATE_RANGE = Range.create(Integer.valueOf(0), Integer.valueOf(960));
  }
  
  MediaCodecInfo(String paramString, boolean paramBoolean, CodecCapabilities[] paramArrayOfCodecCapabilities)
  {
    mName = paramString;
    mIsEncoder = paramBoolean;
    mCaps = new HashMap();
    int i = paramArrayOfCodecCapabilities.length;
    for (int j = 0; j < i; j++)
    {
      paramString = paramArrayOfCodecCapabilities[j];
      mCaps.put(paramString.getMimeType(), paramString);
    }
  }
  
  private static int checkPowerOfTwo(int paramInt, String paramString)
  {
    if ((paramInt - 1 & paramInt) == 0) {
      return paramInt;
    }
    throw new IllegalArgumentException(paramString);
  }
  
  public final CodecCapabilities getCapabilitiesForType(String paramString)
  {
    paramString = (CodecCapabilities)mCaps.get(paramString);
    if (paramString != null) {
      return paramString.dup();
    }
    throw new IllegalArgumentException("codec does not support type");
  }
  
  public final String getName()
  {
    return mName;
  }
  
  public final String[] getSupportedTypes()
  {
    Object localObject = mCaps.keySet();
    localObject = (String[])((Set)localObject).toArray(new String[((Set)localObject).size()]);
    Arrays.sort((Object[])localObject);
    return localObject;
  }
  
  public final boolean isEncoder()
  {
    return mIsEncoder;
  }
  
  public MediaCodecInfo makeRegular()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = mCaps.values().iterator();
    while (localIterator.hasNext())
    {
      CodecCapabilities localCodecCapabilities = (CodecCapabilities)localIterator.next();
      if (localCodecCapabilities.isRegular()) {
        localArrayList.add(localCodecCapabilities);
      }
    }
    if (localArrayList.size() == 0) {
      return null;
    }
    if (localArrayList.size() == mCaps.size()) {
      return this;
    }
    return new MediaCodecInfo(mName, mIsEncoder, (CodecCapabilities[])localArrayList.toArray(new CodecCapabilities[localArrayList.size()]));
  }
  
  public static final class AudioCapabilities
  {
    private static final int MAX_INPUT_CHANNEL_COUNT = 30;
    private static final String TAG = "AudioCapabilities";
    private Range<Integer> mBitrateRange;
    private int mMaxInputChannelCount;
    private MediaCodecInfo.CodecCapabilities mParent;
    private Range<Integer>[] mSampleRateRanges;
    private int[] mSampleRates;
    
    private AudioCapabilities() {}
    
    private void applyLevelLimits()
    {
      int[] arrayOfInt = null;
      Range localRange1 = null;
      Range localRange2 = null;
      int i = 30;
      Object localObject = mParent.getMimeType();
      if (((String)localObject).equalsIgnoreCase("audio/mpeg"))
      {
        arrayOfInt = new int[] { 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000 };
        localRange2 = Range.create(Integer.valueOf(8000), Integer.valueOf(320000));
        i = 2;
      }
      else if (((String)localObject).equalsIgnoreCase("audio/3gpp"))
      {
        arrayOfInt = new int[] { 8000 };
        localRange2 = Range.create(Integer.valueOf(4750), Integer.valueOf(12200));
        i = 1;
      }
      else if (((String)localObject).equalsIgnoreCase("audio/amr-wb"))
      {
        arrayOfInt = new int[] { 16000 };
        localRange2 = Range.create(Integer.valueOf(6600), Integer.valueOf(23850));
        i = 1;
      }
      else if (((String)localObject).equalsIgnoreCase("audio/mp4a-latm"))
      {
        arrayOfInt = new int[] { 7350, 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000, 64000, 88200, 96000 };
        localRange2 = Range.create(Integer.valueOf(8000), Integer.valueOf(510000));
        i = 48;
      }
      else if (((String)localObject).equalsIgnoreCase("audio/vorbis"))
      {
        localRange2 = Range.create(Integer.valueOf(32000), Integer.valueOf(500000));
        localRange1 = Range.create(Integer.valueOf(8000), Integer.valueOf(192000));
        i = 255;
      }
      else if (((String)localObject).equalsIgnoreCase("audio/opus"))
      {
        localRange2 = Range.create(Integer.valueOf(6000), Integer.valueOf(510000));
        arrayOfInt = new int[] { 8000, 12000, 16000, 24000, 48000 };
        i = 255;
      }
      else if (((String)localObject).equalsIgnoreCase("audio/raw"))
      {
        localRange1 = Range.create(Integer.valueOf(1), Integer.valueOf(96000));
        localRange2 = Range.create(Integer.valueOf(1), Integer.valueOf(10000000));
        i = AudioTrack.CHANNEL_COUNT_MAX;
      }
      else if (((String)localObject).equalsIgnoreCase("audio/flac"))
      {
        localRange1 = Range.create(Integer.valueOf(1), Integer.valueOf(655350));
        i = 255;
      }
      else if ((!((String)localObject).equalsIgnoreCase("audio/g711-alaw")) && (!((String)localObject).equalsIgnoreCase("audio/g711-mlaw")))
      {
        if (((String)localObject).equalsIgnoreCase("audio/gsm"))
        {
          arrayOfInt = new int[] { 8000 };
          localRange2 = Range.create(Integer.valueOf(13000), Integer.valueOf(13000));
          i = 1;
        }
        else if (((String)localObject).equalsIgnoreCase("audio/ac3"))
        {
          i = 6;
        }
        else if (((String)localObject).equalsIgnoreCase("audio/eac3"))
        {
          i = 16;
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unsupported mime ");
          localStringBuilder.append((String)localObject);
          Log.w("AudioCapabilities", localStringBuilder.toString());
          localObject = mParent;
          mError |= 0x2;
        }
      }
      else
      {
        arrayOfInt = new int[] { 8000 };
        localRange2 = Range.create(Integer.valueOf(64000), Integer.valueOf(64000));
      }
      if (arrayOfInt != null) {
        limitSampleRates(arrayOfInt);
      } else if (localRange1 != null) {
        limitSampleRates(new Range[] { localRange1 });
      }
      applyLimits(i, localRange2);
    }
    
    private void applyLimits(int paramInt, Range<Integer> paramRange)
    {
      mMaxInputChannelCount = ((Integer)Range.create(Integer.valueOf(1), Integer.valueOf(mMaxInputChannelCount)).clamp(Integer.valueOf(paramInt))).intValue();
      if (paramRange != null) {
        mBitrateRange = mBitrateRange.intersect(paramRange);
      }
    }
    
    public static AudioCapabilities create(MediaFormat paramMediaFormat, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      AudioCapabilities localAudioCapabilities = new AudioCapabilities();
      localAudioCapabilities.init(paramMediaFormat, paramCodecCapabilities);
      return localAudioCapabilities;
    }
    
    private void createDiscreteSampleRates()
    {
      mSampleRates = new int[mSampleRateRanges.length];
      for (int i = 0; i < mSampleRateRanges.length; i++) {
        mSampleRates[i] = ((Integer)mSampleRateRanges[i].getLower()).intValue();
      }
    }
    
    private void init(MediaFormat paramMediaFormat, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      mParent = paramCodecCapabilities;
      initWithPlatformLimits();
      applyLevelLimits();
      parseFromInfo(paramMediaFormat);
    }
    
    private void initWithPlatformLimits()
    {
      mBitrateRange = Range.create(Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE));
      mMaxInputChannelCount = 30;
      mSampleRateRanges = new Range[] { Range.create(Integer.valueOf(8000), Integer.valueOf(96000)) };
      mSampleRates = null;
    }
    
    private void limitSampleRates(int[] paramArrayOfInt)
    {
      Arrays.sort(paramArrayOfInt);
      ArrayList localArrayList = new ArrayList();
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++)
      {
        int k = paramArrayOfInt[j];
        if (supports(Integer.valueOf(k), null)) {
          localArrayList.add(Range.create(Integer.valueOf(k), Integer.valueOf(k)));
        }
      }
      mSampleRateRanges = ((Range[])localArrayList.toArray(new Range[localArrayList.size()]));
      createDiscreteSampleRates();
    }
    
    private void limitSampleRates(Range<Integer>[] paramArrayOfRange)
    {
      Utils.sortDistinctRanges(paramArrayOfRange);
      mSampleRateRanges = Utils.intersectSortedDistinctRanges(mSampleRateRanges, paramArrayOfRange);
      for (Range<Integer> localRange : mSampleRateRanges) {
        if (!((Integer)localRange.getLower()).equals(localRange.getUpper()))
        {
          mSampleRates = null;
          return;
        }
      }
      createDiscreteSampleRates();
    }
    
    private void parseFromInfo(MediaFormat paramMediaFormat)
    {
      int i = 30;
      Range localRange = MediaCodecInfo.POSITIVE_INTEGERS;
      int j;
      if (paramMediaFormat.containsKey("sample-rate-ranges"))
      {
        localObject = paramMediaFormat.getString("sample-rate-ranges").split(",");
        Range[] arrayOfRange = new Range[localObject.length];
        for (j = 0; j < localObject.length; j++) {
          arrayOfRange[j] = Utils.parseIntRange(localObject[j], null);
        }
        limitSampleRates(arrayOfRange);
      }
      if (paramMediaFormat.containsKey("max-channel-count"))
      {
        j = Utils.parseIntSafely(paramMediaFormat.getString("max-channel-count"), 30);
      }
      else
      {
        j = i;
        if ((mParent.mError & 0x2) != 0) {
          j = 0;
        }
      }
      Object localObject = localRange;
      if (paramMediaFormat.containsKey("bitrate-range")) {
        localObject = localRange.intersect(Utils.parseIntRange(paramMediaFormat.getString("bitrate-range"), localRange));
      }
      applyLimits(j, (Range)localObject);
    }
    
    private boolean supports(Integer paramInteger1, Integer paramInteger2)
    {
      if ((paramInteger2 != null) && ((paramInteger2.intValue() < 1) || (paramInteger2.intValue() > mMaxInputChannelCount))) {
        return false;
      }
      return (paramInteger1 == null) || (Utils.binarySearchDistinctRanges(mSampleRateRanges, paramInteger1) >= 0);
    }
    
    public Range<Integer> getBitrateRange()
    {
      return mBitrateRange;
    }
    
    public void getDefaultFormat(MediaFormat paramMediaFormat)
    {
      if (((Integer)mBitrateRange.getLower()).equals(mBitrateRange.getUpper())) {
        paramMediaFormat.setInteger("bitrate", ((Integer)mBitrateRange.getLower()).intValue());
      }
      if (mMaxInputChannelCount == 1) {
        paramMediaFormat.setInteger("channel-count", 1);
      }
      if ((mSampleRates != null) && (mSampleRates.length == 1)) {
        paramMediaFormat.setInteger("sample-rate", mSampleRates[0]);
      }
    }
    
    public int getMaxInputChannelCount()
    {
      return mMaxInputChannelCount;
    }
    
    public Range<Integer>[] getSupportedSampleRateRanges()
    {
      return (Range[])Arrays.copyOf(mSampleRateRanges, mSampleRateRanges.length);
    }
    
    public int[] getSupportedSampleRates()
    {
      return Arrays.copyOf(mSampleRates, mSampleRates.length);
    }
    
    public boolean isSampleRateSupported(int paramInt)
    {
      return supports(Integer.valueOf(paramInt), null);
    }
    
    public boolean supportsFormat(MediaFormat paramMediaFormat)
    {
      Map localMap = paramMediaFormat.getMap();
      if (!supports((Integer)localMap.get("sample-rate"), (Integer)localMap.get("channel-count"))) {
        return false;
      }
      return MediaCodecInfo.CodecCapabilities.access$100(mBitrateRange, paramMediaFormat);
    }
  }
  
  public static final class CodecCapabilities
  {
    public static final int COLOR_Format12bitRGB444 = 3;
    public static final int COLOR_Format16bitARGB1555 = 5;
    public static final int COLOR_Format16bitARGB4444 = 4;
    public static final int COLOR_Format16bitBGR565 = 7;
    public static final int COLOR_Format16bitRGB565 = 6;
    public static final int COLOR_Format18BitBGR666 = 41;
    public static final int COLOR_Format18bitARGB1665 = 9;
    public static final int COLOR_Format18bitRGB666 = 8;
    public static final int COLOR_Format19bitARGB1666 = 10;
    public static final int COLOR_Format24BitABGR6666 = 43;
    public static final int COLOR_Format24BitARGB6666 = 42;
    public static final int COLOR_Format24bitARGB1887 = 13;
    public static final int COLOR_Format24bitBGR888 = 12;
    public static final int COLOR_Format24bitRGB888 = 11;
    public static final int COLOR_Format25bitARGB1888 = 14;
    public static final int COLOR_Format32bitABGR8888 = 2130747392;
    public static final int COLOR_Format32bitARGB8888 = 16;
    public static final int COLOR_Format32bitBGRA8888 = 15;
    public static final int COLOR_Format8bitRGB332 = 2;
    public static final int COLOR_FormatCbYCrY = 27;
    public static final int COLOR_FormatCrYCbY = 28;
    public static final int COLOR_FormatL16 = 36;
    public static final int COLOR_FormatL2 = 33;
    public static final int COLOR_FormatL24 = 37;
    public static final int COLOR_FormatL32 = 38;
    public static final int COLOR_FormatL4 = 34;
    public static final int COLOR_FormatL8 = 35;
    public static final int COLOR_FormatMonochrome = 1;
    public static final int COLOR_FormatRGBAFlexible = 2134288520;
    public static final int COLOR_FormatRGBFlexible = 2134292616;
    public static final int COLOR_FormatRawBayer10bit = 31;
    public static final int COLOR_FormatRawBayer8bit = 30;
    public static final int COLOR_FormatRawBayer8bitcompressed = 32;
    public static final int COLOR_FormatSurface = 2130708361;
    public static final int COLOR_FormatYCbYCr = 25;
    public static final int COLOR_FormatYCrYCb = 26;
    public static final int COLOR_FormatYUV411PackedPlanar = 18;
    public static final int COLOR_FormatYUV411Planar = 17;
    public static final int COLOR_FormatYUV420Flexible = 2135033992;
    public static final int COLOR_FormatYUV420PackedPlanar = 20;
    public static final int COLOR_FormatYUV420PackedSemiPlanar = 39;
    public static final int COLOR_FormatYUV420Planar = 19;
    public static final int COLOR_FormatYUV420SemiPlanar = 21;
    public static final int COLOR_FormatYUV422Flexible = 2135042184;
    public static final int COLOR_FormatYUV422PackedPlanar = 23;
    public static final int COLOR_FormatYUV422PackedSemiPlanar = 40;
    public static final int COLOR_FormatYUV422Planar = 22;
    public static final int COLOR_FormatYUV422SemiPlanar = 24;
    public static final int COLOR_FormatYUV444Flexible = 2135181448;
    public static final int COLOR_FormatYUV444Interleaved = 29;
    public static final int COLOR_QCOM_FormatYUV420SemiPlanar = 2141391872;
    public static final int COLOR_TI_FormatYUV420PackedSemiPlanar = 2130706688;
    public static final String FEATURE_AdaptivePlayback = "adaptive-playback";
    public static final String FEATURE_IntraRefresh = "intra-refresh";
    public static final String FEATURE_PartialFrame = "partial-frame";
    public static final String FEATURE_SecurePlayback = "secure-playback";
    public static final String FEATURE_TunneledPlayback = "tunneled-playback";
    private static final String TAG = "CodecCapabilities";
    private static final MediaCodecInfo.Feature[] decoderFeatures = { new MediaCodecInfo.Feature("adaptive-playback", 1, true), new MediaCodecInfo.Feature("secure-playback", 2, false), new MediaCodecInfo.Feature("tunneled-playback", 4, false), new MediaCodecInfo.Feature("partial-frame", 8, false) };
    private static final MediaCodecInfo.Feature[] encoderFeatures = { new MediaCodecInfo.Feature("intra-refresh", 1, false) };
    public int[] colorFormats;
    private MediaCodecInfo.AudioCapabilities mAudioCaps;
    private MediaFormat mCapabilitiesInfo;
    private MediaFormat mDefaultFormat;
    private MediaCodecInfo.EncoderCapabilities mEncoderCaps;
    int mError;
    private int mFlagsRequired;
    private int mFlagsSupported;
    private int mFlagsVerified;
    private int mMaxSupportedInstances;
    private String mMime;
    private MediaCodecInfo.VideoCapabilities mVideoCaps;
    public MediaCodecInfo.CodecProfileLevel[] profileLevels;
    
    public CodecCapabilities() {}
    
    CodecCapabilities(MediaCodecInfo.CodecProfileLevel[] paramArrayOfCodecProfileLevel, int[] paramArrayOfInt, boolean paramBoolean, int paramInt, MediaFormat paramMediaFormat1, MediaFormat paramMediaFormat2)
    {
      Object localObject = paramMediaFormat2.getMap();
      colorFormats = paramArrayOfInt;
      mFlagsVerified = paramInt;
      mDefaultFormat = paramMediaFormat1;
      mCapabilitiesInfo = paramMediaFormat2;
      mMime = mDefaultFormat.getString("mime");
      int i = paramArrayOfCodecProfileLevel.length;
      paramInt = 0;
      paramArrayOfInt = paramArrayOfCodecProfileLevel;
      if (i == 0)
      {
        paramArrayOfInt = paramArrayOfCodecProfileLevel;
        if (mMime.equalsIgnoreCase("video/x-vnd.on2.vp9"))
        {
          paramArrayOfCodecProfileLevel = new MediaCodecInfo.CodecProfileLevel();
          profile = 1;
          level = MediaCodecInfo.VideoCapabilities.equivalentVP9Level(paramMediaFormat2);
          paramArrayOfInt = new MediaCodecInfo.CodecProfileLevel[] { paramArrayOfCodecProfileLevel };
        }
      }
      profileLevels = paramArrayOfInt;
      if (mMime.toLowerCase().startsWith("audio/"))
      {
        mAudioCaps = MediaCodecInfo.AudioCapabilities.create(paramMediaFormat2, this);
        mAudioCaps.getDefaultFormat(mDefaultFormat);
      }
      else if ((mMime.toLowerCase().startsWith("video/")) || (mMime.equalsIgnoreCase("image/vnd.android.heic")))
      {
        mVideoCaps = MediaCodecInfo.VideoCapabilities.create(paramMediaFormat2, this);
      }
      if (paramBoolean)
      {
        mEncoderCaps = MediaCodecInfo.EncoderCapabilities.create(paramMediaFormat2, this);
        mEncoderCaps.getDefaultFormat(mDefaultFormat);
      }
      mMaxSupportedInstances = Utils.parseIntSafely(MediaCodecList.getGlobalSettings().get("max-concurrent-instances"), 32);
      i = Utils.parseIntSafely(((Map)localObject).get("max-concurrent-instances"), mMaxSupportedInstances);
      mMaxSupportedInstances = ((Integer)Range.create(Integer.valueOf(1), Integer.valueOf(256)).clamp(Integer.valueOf(i))).intValue();
      paramArrayOfInt = getValidFeatures();
      i = paramArrayOfInt.length;
      paramArrayOfCodecProfileLevel = (MediaCodecInfo.CodecProfileLevel[])localObject;
      while (paramInt < i)
      {
        paramMediaFormat1 = paramArrayOfInt[paramInt];
        paramMediaFormat2 = new StringBuilder();
        paramMediaFormat2.append("feature-");
        paramMediaFormat2.append(mName);
        paramMediaFormat2 = paramMediaFormat2.toString();
        localObject = (Integer)paramArrayOfCodecProfileLevel.get(paramMediaFormat2);
        if (localObject != null)
        {
          if (((Integer)localObject).intValue() > 0) {
            mFlagsRequired |= mValue;
          }
          mFlagsSupported |= mValue;
          mDefaultFormat.setInteger(paramMediaFormat2, 1);
        }
        paramInt++;
      }
    }
    
    CodecCapabilities(MediaCodecInfo.CodecProfileLevel[] paramArrayOfCodecProfileLevel, int[] paramArrayOfInt, boolean paramBoolean, int paramInt, Map<String, Object> paramMap1, Map<String, Object> paramMap2)
    {
      this(paramArrayOfCodecProfileLevel, paramArrayOfInt, paramBoolean, paramInt, new MediaFormat(paramMap1), new MediaFormat(paramMap2));
    }
    
    private boolean checkFeature(String paramString, int paramInt)
    {
      MediaCodecInfo.Feature[] arrayOfFeature = getValidFeatures();
      int i = arrayOfFeature.length;
      boolean bool = false;
      for (int j = 0; j < i; j++)
      {
        MediaCodecInfo.Feature localFeature = arrayOfFeature[j];
        if (mName.equals(paramString))
        {
          if ((mValue & paramInt) != 0) {
            bool = true;
          }
          return bool;
        }
      }
      return false;
    }
    
    public static CodecCapabilities createFromProfileLevel(String paramString, int paramInt1, int paramInt2)
    {
      MediaCodecInfo.CodecProfileLevel localCodecProfileLevel = new MediaCodecInfo.CodecProfileLevel();
      profile = paramInt1;
      level = paramInt2;
      MediaFormat localMediaFormat = new MediaFormat();
      localMediaFormat.setString("mime", paramString);
      paramString = new MediaFormat();
      paramString = new CodecCapabilities(new MediaCodecInfo.CodecProfileLevel[] { localCodecProfileLevel }, new int[0], true, 0, localMediaFormat, paramString);
      if (mError != 0) {
        return null;
      }
      return paramString;
    }
    
    private MediaCodecInfo.Feature[] getValidFeatures()
    {
      if (!isEncoder()) {
        return decoderFeatures;
      }
      return encoderFeatures;
    }
    
    private boolean isAudio()
    {
      boolean bool;
      if (mAudioCaps != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isEncoder()
    {
      boolean bool;
      if (mEncoderCaps != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isVideo()
    {
      boolean bool;
      if (mVideoCaps != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private static boolean supportsBitrate(Range<Integer> paramRange, MediaFormat paramMediaFormat)
    {
      paramMediaFormat = paramMediaFormat.getMap();
      Integer localInteger1 = (Integer)paramMediaFormat.get("max-bitrate");
      Integer localInteger2 = (Integer)paramMediaFormat.get("bitrate");
      if (localInteger2 == null)
      {
        paramMediaFormat = localInteger1;
      }
      else
      {
        paramMediaFormat = localInteger2;
        if (localInteger1 != null) {
          paramMediaFormat = Integer.valueOf(Math.max(localInteger2.intValue(), localInteger1.intValue()));
        }
      }
      if ((paramMediaFormat != null) && (paramMediaFormat.intValue() > 0)) {
        return paramRange.contains(paramMediaFormat);
      }
      return true;
    }
    
    private boolean supportsProfileLevel(int paramInt, Integer paramInteger)
    {
      MediaCodecInfo.CodecProfileLevel[] arrayOfCodecProfileLevel = profileLevels;
      int i = arrayOfCodecProfileLevel.length;
      boolean bool = false;
      int j = 0;
      while (j < i)
      {
        MediaCodecInfo.CodecProfileLevel localCodecProfileLevel = arrayOfCodecProfileLevel[j];
        if (profile == paramInt)
        {
          if ((paramInteger == null) || (mMime.equalsIgnoreCase("audio/mp4a-latm"))) {
            break label275;
          }
          if (((!mMime.equalsIgnoreCase("video/3gpp")) || (level == paramInteger.intValue()) || (level != 16) || (paramInteger.intValue() <= 1)) && ((!mMime.equalsIgnoreCase("video/mp4v-es")) || (level == paramInteger.intValue()) || (level != 4) || (paramInteger.intValue() <= 1))) {
            if (mMime.equalsIgnoreCase("video/hevc"))
            {
              int k;
              if ((level & 0x2AAAAAA) != 0) {
                k = 1;
              } else {
                k = 0;
              }
              int m;
              if ((0x2AAAAAA & paramInteger.intValue()) != 0) {
                m = 1;
              } else {
                m = 0;
              }
              if ((m != 0) && (k == 0)) {}
            }
            else if (level >= paramInteger.intValue())
            {
              if (createFromProfileLevel(mMime, paramInt, level) != null)
              {
                if (createFromProfileLevel(mMime, paramInt, paramInteger.intValue()) != null) {
                  bool = true;
                }
                return bool;
              }
              return true;
            }
          }
        }
        j++;
        continue;
        label275:
        return true;
      }
      return false;
    }
    
    public CodecCapabilities dup()
    {
      CodecCapabilities localCodecCapabilities = new CodecCapabilities();
      profileLevels = ((MediaCodecInfo.CodecProfileLevel[])Arrays.copyOf(profileLevels, profileLevels.length));
      colorFormats = Arrays.copyOf(colorFormats, colorFormats.length);
      mMime = mMime;
      mMaxSupportedInstances = mMaxSupportedInstances;
      mFlagsRequired = mFlagsRequired;
      mFlagsSupported = mFlagsSupported;
      mFlagsVerified = mFlagsVerified;
      mAudioCaps = mAudioCaps;
      mVideoCaps = mVideoCaps;
      mEncoderCaps = mEncoderCaps;
      mDefaultFormat = mDefaultFormat;
      mCapabilitiesInfo = mCapabilitiesInfo;
      return localCodecCapabilities;
    }
    
    public MediaCodecInfo.AudioCapabilities getAudioCapabilities()
    {
      return mAudioCaps;
    }
    
    public MediaFormat getDefaultFormat()
    {
      return mDefaultFormat;
    }
    
    public MediaCodecInfo.EncoderCapabilities getEncoderCapabilities()
    {
      return mEncoderCaps;
    }
    
    public int getMaxSupportedInstances()
    {
      return mMaxSupportedInstances;
    }
    
    public String getMimeType()
    {
      return mMime;
    }
    
    public MediaCodecInfo.VideoCapabilities getVideoCapabilities()
    {
      return mVideoCaps;
    }
    
    public final boolean isFeatureRequired(String paramString)
    {
      return checkFeature(paramString, mFlagsRequired);
    }
    
    public final boolean isFeatureSupported(String paramString)
    {
      return checkFeature(paramString, mFlagsSupported);
    }
    
    public final boolean isFormatSupported(MediaFormat paramMediaFormat)
    {
      Object localObject1 = paramMediaFormat.getMap();
      Object localObject2 = (String)((Map)localObject1).get("mime");
      if ((localObject2 != null) && (!mMime.equalsIgnoreCase((String)localObject2))) {
        return false;
      }
      Object localObject4;
      for (localObject2 : getValidFeatures())
      {
        localObject4 = new StringBuilder();
        ((StringBuilder)localObject4).append("feature-");
        ((StringBuilder)localObject4).append(mName);
        localObject4 = (Integer)((Map)localObject1).get(((StringBuilder)localObject4).toString());
        if ((localObject4 != null) && (((((Integer)localObject4).intValue() == 1) && (!isFeatureSupported(mName))) || ((((Integer)localObject4).intValue() == 0) && (isFeatureRequired(mName))))) {
          return false;
        }
      }
      localObject2 = (Integer)((Map)localObject1).get("profile");
      ??? = (Integer)((Map)localObject1).get("level");
      if (localObject2 != null)
      {
        if (!supportsProfileLevel(((Integer)localObject2).intValue(), (Integer)???)) {
          return false;
        }
        ??? = profileLevels;
        int k = ???.length;
        int m = 0;
        ??? = 0;
        while (??? < k)
        {
          localObject4 = ???[???];
          ??? = m;
          if (profile == ((Integer)localObject2).intValue())
          {
            ??? = m;
            if (level > m) {
              ??? = level;
            }
          }
          ???++;
          m = ???;
        }
        localObject2 = createFromProfileLevel(mMime, ((Integer)localObject2).intValue(), m);
        localObject1 = new HashMap((Map)localObject1);
        ((Map)localObject1).remove("profile");
        localObject1 = new MediaFormat((Map)localObject1);
        if ((localObject2 != null) && (!((CodecCapabilities)localObject2).isFormatSupported((MediaFormat)localObject1))) {
          return false;
        }
      }
      if ((mAudioCaps != null) && (!mAudioCaps.supportsFormat(paramMediaFormat))) {
        return false;
      }
      if ((mVideoCaps != null) && (!mVideoCaps.supportsFormat(paramMediaFormat))) {
        return false;
      }
      return (mEncoderCaps == null) || (mEncoderCaps.supportsFormat(paramMediaFormat));
    }
    
    public boolean isRegular()
    {
      for (MediaCodecInfo.Feature localFeature : getValidFeatures()) {
        if ((!mDefault) && (isFeatureRequired(mName))) {
          return false;
        }
      }
      return true;
    }
    
    public String[] validFeatures()
    {
      MediaCodecInfo.Feature[] arrayOfFeature = getValidFeatures();
      String[] arrayOfString = new String[arrayOfFeature.length];
      for (int i = 0; i < arrayOfString.length; i++) {
        arrayOfString[i] = mName;
      }
      return arrayOfString;
    }
  }
  
  public static final class CodecProfileLevel
  {
    public static final int AACObjectELD = 39;
    public static final int AACObjectERLC = 17;
    public static final int AACObjectERScalable = 20;
    public static final int AACObjectHE = 5;
    public static final int AACObjectHE_PS = 29;
    public static final int AACObjectLC = 2;
    public static final int AACObjectLD = 23;
    public static final int AACObjectLTP = 4;
    public static final int AACObjectMain = 1;
    public static final int AACObjectSSR = 3;
    public static final int AACObjectScalable = 6;
    public static final int AACObjectXHE = 42;
    public static final int AVCLevel1 = 1;
    public static final int AVCLevel11 = 4;
    public static final int AVCLevel12 = 8;
    public static final int AVCLevel13 = 16;
    public static final int AVCLevel1b = 2;
    public static final int AVCLevel2 = 32;
    public static final int AVCLevel21 = 64;
    public static final int AVCLevel22 = 128;
    public static final int AVCLevel3 = 256;
    public static final int AVCLevel31 = 512;
    public static final int AVCLevel32 = 1024;
    public static final int AVCLevel4 = 2048;
    public static final int AVCLevel41 = 4096;
    public static final int AVCLevel42 = 8192;
    public static final int AVCLevel5 = 16384;
    public static final int AVCLevel51 = 32768;
    public static final int AVCLevel52 = 65536;
    public static final int AVCLevel6 = 131072;
    public static final int AVCLevel61 = 262144;
    public static final int AVCLevel62 = 524288;
    public static final int AVCProfileBaseline = 1;
    public static final int AVCProfileConstrainedBaseline = 65536;
    public static final int AVCProfileConstrainedHigh = 524288;
    public static final int AVCProfileExtended = 4;
    public static final int AVCProfileHigh = 8;
    public static final int AVCProfileHigh10 = 16;
    public static final int AVCProfileHigh422 = 32;
    public static final int AVCProfileHigh444 = 64;
    public static final int AVCProfileMain = 2;
    public static final int DolbyVisionLevelFhd24 = 4;
    public static final int DolbyVisionLevelFhd30 = 8;
    public static final int DolbyVisionLevelFhd60 = 16;
    public static final int DolbyVisionLevelHd24 = 1;
    public static final int DolbyVisionLevelHd30 = 2;
    public static final int DolbyVisionLevelUhd24 = 32;
    public static final int DolbyVisionLevelUhd30 = 64;
    public static final int DolbyVisionLevelUhd48 = 128;
    public static final int DolbyVisionLevelUhd60 = 256;
    public static final int DolbyVisionProfileDvavPen = 2;
    public static final int DolbyVisionProfileDvavPer = 1;
    public static final int DolbyVisionProfileDvavSe = 512;
    public static final int DolbyVisionProfileDvheDen = 8;
    public static final int DolbyVisionProfileDvheDer = 4;
    public static final int DolbyVisionProfileDvheDtb = 128;
    public static final int DolbyVisionProfileDvheDth = 64;
    public static final int DolbyVisionProfileDvheDtr = 16;
    public static final int DolbyVisionProfileDvheSt = 256;
    public static final int DolbyVisionProfileDvheStn = 32;
    public static final int H263Level10 = 1;
    public static final int H263Level20 = 2;
    public static final int H263Level30 = 4;
    public static final int H263Level40 = 8;
    public static final int H263Level45 = 16;
    public static final int H263Level50 = 32;
    public static final int H263Level60 = 64;
    public static final int H263Level70 = 128;
    public static final int H263ProfileBackwardCompatible = 4;
    public static final int H263ProfileBaseline = 1;
    public static final int H263ProfileH320Coding = 2;
    public static final int H263ProfileHighCompression = 32;
    public static final int H263ProfileHighLatency = 256;
    public static final int H263ProfileISWV2 = 8;
    public static final int H263ProfileISWV3 = 16;
    public static final int H263ProfileInterlace = 128;
    public static final int H263ProfileInternet = 64;
    public static final int HEVCHighTierLevel1 = 2;
    public static final int HEVCHighTierLevel2 = 8;
    public static final int HEVCHighTierLevel21 = 32;
    public static final int HEVCHighTierLevel3 = 128;
    public static final int HEVCHighTierLevel31 = 512;
    public static final int HEVCHighTierLevel4 = 2048;
    public static final int HEVCHighTierLevel41 = 8192;
    public static final int HEVCHighTierLevel5 = 32768;
    public static final int HEVCHighTierLevel51 = 131072;
    public static final int HEVCHighTierLevel52 = 524288;
    public static final int HEVCHighTierLevel6 = 2097152;
    public static final int HEVCHighTierLevel61 = 8388608;
    public static final int HEVCHighTierLevel62 = 33554432;
    private static final int HEVCHighTierLevels = 44739242;
    public static final int HEVCMainTierLevel1 = 1;
    public static final int HEVCMainTierLevel2 = 4;
    public static final int HEVCMainTierLevel21 = 16;
    public static final int HEVCMainTierLevel3 = 64;
    public static final int HEVCMainTierLevel31 = 256;
    public static final int HEVCMainTierLevel4 = 1024;
    public static final int HEVCMainTierLevel41 = 4096;
    public static final int HEVCMainTierLevel5 = 16384;
    public static final int HEVCMainTierLevel51 = 65536;
    public static final int HEVCMainTierLevel52 = 262144;
    public static final int HEVCMainTierLevel6 = 1048576;
    public static final int HEVCMainTierLevel61 = 4194304;
    public static final int HEVCMainTierLevel62 = 16777216;
    public static final int HEVCProfileMain = 1;
    public static final int HEVCProfileMain10 = 2;
    public static final int HEVCProfileMain10HDR10 = 4096;
    public static final int HEVCProfileMainStill = 4;
    public static final int MPEG2LevelH14 = 2;
    public static final int MPEG2LevelHL = 3;
    public static final int MPEG2LevelHP = 4;
    public static final int MPEG2LevelLL = 0;
    public static final int MPEG2LevelML = 1;
    public static final int MPEG2Profile422 = 2;
    public static final int MPEG2ProfileHigh = 5;
    public static final int MPEG2ProfileMain = 1;
    public static final int MPEG2ProfileSNR = 3;
    public static final int MPEG2ProfileSimple = 0;
    public static final int MPEG2ProfileSpatial = 4;
    public static final int MPEG4Level0 = 1;
    public static final int MPEG4Level0b = 2;
    public static final int MPEG4Level1 = 4;
    public static final int MPEG4Level2 = 8;
    public static final int MPEG4Level3 = 16;
    public static final int MPEG4Level3b = 24;
    public static final int MPEG4Level4 = 32;
    public static final int MPEG4Level4a = 64;
    public static final int MPEG4Level5 = 128;
    public static final int MPEG4Level6 = 256;
    public static final int MPEG4ProfileAdvancedCoding = 4096;
    public static final int MPEG4ProfileAdvancedCore = 8192;
    public static final int MPEG4ProfileAdvancedRealTime = 1024;
    public static final int MPEG4ProfileAdvancedScalable = 16384;
    public static final int MPEG4ProfileAdvancedSimple = 32768;
    public static final int MPEG4ProfileBasicAnimated = 256;
    public static final int MPEG4ProfileCore = 4;
    public static final int MPEG4ProfileCoreScalable = 2048;
    public static final int MPEG4ProfileHybrid = 512;
    public static final int MPEG4ProfileMain = 8;
    public static final int MPEG4ProfileNbit = 16;
    public static final int MPEG4ProfileScalableTexture = 32;
    public static final int MPEG4ProfileSimple = 1;
    public static final int MPEG4ProfileSimpleFBA = 128;
    public static final int MPEG4ProfileSimpleFace = 64;
    public static final int MPEG4ProfileSimpleScalable = 2;
    public static final int VP8Level_Version0 = 1;
    public static final int VP8Level_Version1 = 2;
    public static final int VP8Level_Version2 = 4;
    public static final int VP8Level_Version3 = 8;
    public static final int VP8ProfileMain = 1;
    public static final int VP9Level1 = 1;
    public static final int VP9Level11 = 2;
    public static final int VP9Level2 = 4;
    public static final int VP9Level21 = 8;
    public static final int VP9Level3 = 16;
    public static final int VP9Level31 = 32;
    public static final int VP9Level4 = 64;
    public static final int VP9Level41 = 128;
    public static final int VP9Level5 = 256;
    public static final int VP9Level51 = 512;
    public static final int VP9Level52 = 1024;
    public static final int VP9Level6 = 2048;
    public static final int VP9Level61 = 4096;
    public static final int VP9Level62 = 8192;
    public static final int VP9Profile0 = 1;
    public static final int VP9Profile1 = 2;
    public static final int VP9Profile2 = 4;
    public static final int VP9Profile2HDR = 4096;
    public static final int VP9Profile3 = 8;
    public static final int VP9Profile3HDR = 8192;
    public int level;
    public int profile;
    
    public CodecProfileLevel() {}
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = false;
      if (paramObject == null) {
        return false;
      }
      if ((paramObject instanceof CodecProfileLevel))
      {
        paramObject = (CodecProfileLevel)paramObject;
        boolean bool2 = bool1;
        if (profile == profile)
        {
          bool2 = bool1;
          if (level == level) {
            bool2 = true;
          }
        }
        return bool2;
      }
      return false;
    }
    
    public int hashCode()
    {
      return Long.hashCode(profile << 32 | level);
    }
  }
  
  public static final class EncoderCapabilities
  {
    public static final int BITRATE_MODE_CBR = 2;
    public static final int BITRATE_MODE_CQ = 0;
    public static final int BITRATE_MODE_VBR = 1;
    private static final MediaCodecInfo.Feature[] bitrates = { new MediaCodecInfo.Feature("VBR", 1, true), new MediaCodecInfo.Feature("CBR", 2, false), new MediaCodecInfo.Feature("CQ", 0, false) };
    private int mBitControl;
    private Range<Integer> mComplexityRange;
    private Integer mDefaultComplexity;
    private Integer mDefaultQuality;
    private MediaCodecInfo.CodecCapabilities mParent;
    private Range<Integer> mQualityRange;
    private String mQualityScale;
    
    private EncoderCapabilities() {}
    
    private void applyLevelLimits()
    {
      String str = mParent.getMimeType();
      if (str.equalsIgnoreCase("audio/flac"))
      {
        mComplexityRange = Range.create(Integer.valueOf(0), Integer.valueOf(8));
        mBitControl = 1;
      }
      else if ((str.equalsIgnoreCase("audio/3gpp")) || (str.equalsIgnoreCase("audio/amr-wb")) || (str.equalsIgnoreCase("audio/g711-alaw")) || (str.equalsIgnoreCase("audio/g711-mlaw")) || (str.equalsIgnoreCase("audio/gsm")))
      {
        mBitControl = 4;
      }
    }
    
    public static EncoderCapabilities create(MediaFormat paramMediaFormat, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      EncoderCapabilities localEncoderCapabilities = new EncoderCapabilities();
      localEncoderCapabilities.init(paramMediaFormat, paramCodecCapabilities);
      return localEncoderCapabilities;
    }
    
    private void init(MediaFormat paramMediaFormat, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      mParent = paramCodecCapabilities;
      mComplexityRange = Range.create(Integer.valueOf(0), Integer.valueOf(0));
      mQualityRange = Range.create(Integer.valueOf(0), Integer.valueOf(0));
      mBitControl = 2;
      applyLevelLimits();
      parseFromInfo(paramMediaFormat);
    }
    
    private static int parseBitrateMode(String paramString)
    {
      for (MediaCodecInfo.Feature localFeature : bitrates) {
        if (mName.equalsIgnoreCase(paramString)) {
          return mValue;
        }
      }
      return 0;
    }
    
    private void parseFromInfo(MediaFormat paramMediaFormat)
    {
      Map localMap = paramMediaFormat.getMap();
      if (paramMediaFormat.containsKey("complexity-range")) {
        mComplexityRange = Utils.parseIntRange(paramMediaFormat.getString("complexity-range"), mComplexityRange);
      }
      if (paramMediaFormat.containsKey("quality-range")) {
        mQualityRange = Utils.parseIntRange(paramMediaFormat.getString("quality-range"), mQualityRange);
      }
      if (paramMediaFormat.containsKey("feature-bitrate-modes")) {
        for (paramMediaFormat : paramMediaFormat.getString("feature-bitrate-modes").split(",")) {
          mBitControl |= 1 << parseBitrateMode(paramMediaFormat);
        }
      }
      try
      {
        mDefaultComplexity = Integer.valueOf(Integer.parseInt((String)localMap.get("complexity-default")));
      }
      catch (NumberFormatException paramMediaFormat) {}
      try
      {
        mDefaultQuality = Integer.valueOf(Integer.parseInt((String)localMap.get("quality-default")));
      }
      catch (NumberFormatException paramMediaFormat) {}
      mQualityScale = ((String)localMap.get("quality-scale"));
    }
    
    private boolean supports(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3)
    {
      boolean bool1 = true;
      boolean bool2 = bool1;
      if (1 != 0)
      {
        bool2 = bool1;
        if (paramInteger1 != null) {
          bool2 = mComplexityRange.contains(paramInteger1);
        }
      }
      bool1 = bool2;
      if (bool2)
      {
        bool1 = bool2;
        if (paramInteger2 != null) {
          bool1 = mQualityRange.contains(paramInteger2);
        }
      }
      bool2 = bool1;
      if (bool1)
      {
        bool2 = bool1;
        if (paramInteger3 != null)
        {
          paramInteger2 = mParent.profileLevels;
          int i = paramInteger2.length;
          bool2 = false;
          for (int j = 0;; j++)
          {
            paramInteger1 = paramInteger3;
            if (j >= i) {
              break;
            }
            if (profile == paramInteger3.intValue())
            {
              paramInteger1 = null;
              break;
            }
          }
          if (paramInteger1 == null) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    
    public Range<Integer> getComplexityRange()
    {
      return mComplexityRange;
    }
    
    public void getDefaultFormat(MediaFormat paramMediaFormat)
    {
      if ((!((Integer)mQualityRange.getUpper()).equals(mQualityRange.getLower())) && (mDefaultQuality != null)) {
        paramMediaFormat.setInteger("quality", mDefaultQuality.intValue());
      }
      if ((!((Integer)mComplexityRange.getUpper()).equals(mComplexityRange.getLower())) && (mDefaultComplexity != null)) {
        paramMediaFormat.setInteger("complexity", mDefaultComplexity.intValue());
      }
      for (MediaCodecInfo.Feature localFeature : bitrates) {
        if ((mBitControl & 1 << mValue) != 0)
        {
          paramMediaFormat.setInteger("bitrate-mode", mValue);
          break;
        }
      }
    }
    
    public Range<Integer> getQualityRange()
    {
      return mQualityRange;
    }
    
    public boolean isBitrateModeSupported(int paramInt)
    {
      MediaCodecInfo.Feature[] arrayOfFeature = bitrates;
      int i = arrayOfFeature.length;
      for (int j = 0; j < i; j++) {
        if (paramInt == mValue)
        {
          j = mBitControl;
          boolean bool = true;
          if ((j & 1 << paramInt) == 0) {
            bool = false;
          }
          return bool;
        }
      }
      return false;
    }
    
    public boolean supportsFormat(MediaFormat paramMediaFormat)
    {
      Map localMap = paramMediaFormat.getMap();
      Object localObject1 = mParent.getMimeType();
      paramMediaFormat = (Integer)localMap.get("bitrate-mode");
      if ((paramMediaFormat != null) && (!isBitrateModeSupported(paramMediaFormat.intValue()))) {
        return false;
      }
      Integer localInteger = (Integer)localMap.get("complexity");
      paramMediaFormat = localInteger;
      if ("audio/flac".equalsIgnoreCase((String)localObject1))
      {
        localObject2 = (Integer)localMap.get("flac-compression-level");
        if (localInteger == null)
        {
          paramMediaFormat = (MediaFormat)localObject2;
        }
        else
        {
          paramMediaFormat = localInteger;
          if (localObject2 != null) {
            if (localInteger.equals(localObject2)) {
              paramMediaFormat = localInteger;
            } else {
              throw new IllegalArgumentException("conflicting values for complexity and flac-compression-level");
            }
          }
        }
      }
      localInteger = (Integer)localMap.get("profile");
      Object localObject2 = localInteger;
      if ("audio/mp4a-latm".equalsIgnoreCase((String)localObject1))
      {
        localObject1 = (Integer)localMap.get("aac-profile");
        if (localInteger == null)
        {
          localObject2 = localObject1;
        }
        else
        {
          localObject2 = localInteger;
          if (localObject1 != null) {
            if (((Integer)localObject1).equals(localInteger)) {
              localObject2 = localInteger;
            } else {
              throw new IllegalArgumentException("conflicting values for profile and aac-profile");
            }
          }
        }
      }
      return supports(paramMediaFormat, (Integer)localMap.get("quality"), (Integer)localObject2);
    }
  }
  
  private static class Feature
  {
    public boolean mDefault;
    public String mName;
    public int mValue;
    
    public Feature(String paramString, int paramInt, boolean paramBoolean)
    {
      mName = paramString;
      mValue = paramInt;
      mDefault = paramBoolean;
    }
  }
  
  public static final class VideoCapabilities
  {
    private static final String TAG = "VideoCapabilities";
    private boolean mAllowMbOverride;
    private Range<Rational> mAspectRatioRange;
    private Range<Integer> mBitrateRange;
    private Range<Rational> mBlockAspectRatioRange;
    private Range<Integer> mBlockCountRange;
    private int mBlockHeight;
    private int mBlockWidth;
    private Range<Long> mBlocksPerSecondRange;
    private Range<Integer> mFrameRateRange;
    private int mHeightAlignment;
    private Range<Integer> mHeightRange;
    private Range<Integer> mHorizontalBlockRange;
    private Map<Size, Range<Long>> mMeasuredFrameRates;
    private MediaCodecInfo.CodecCapabilities mParent;
    private int mSmallerDimensionUpperLimit;
    private Range<Integer> mVerticalBlockRange;
    private int mWidthAlignment;
    private Range<Integer> mWidthRange;
    
    private VideoCapabilities() {}
    
    private void applyAlignment(int paramInt1, int paramInt2)
    {
      MediaCodecInfo.checkPowerOfTwo(paramInt1, "widthAlignment must be a power of two");
      MediaCodecInfo.checkPowerOfTwo(paramInt2, "heightAlignment must be a power of two");
      if ((paramInt1 > mBlockWidth) || (paramInt2 > mBlockHeight)) {
        applyBlockLimits(Math.max(paramInt1, mBlockWidth), Math.max(paramInt2, mBlockHeight), MediaCodecInfo.POSITIVE_INTEGERS, MediaCodecInfo.POSITIVE_LONGS, MediaCodecInfo.POSITIVE_RATIONALS);
      }
      mWidthAlignment = Math.max(paramInt1, mWidthAlignment);
      mHeightAlignment = Math.max(paramInt2, mHeightAlignment);
      mWidthRange = Utils.alignRange(mWidthRange, mWidthAlignment);
      mHeightRange = Utils.alignRange(mHeightRange, mHeightAlignment);
    }
    
    private void applyBlockLimits(int paramInt1, int paramInt2, Range<Integer> paramRange, Range<Long> paramRange1, Range<Rational> paramRange2)
    {
      MediaCodecInfo.checkPowerOfTwo(paramInt1, "blockWidth must be a power of two");
      MediaCodecInfo.checkPowerOfTwo(paramInt2, "blockHeight must be a power of two");
      int i = Math.max(paramInt1, mBlockWidth);
      int j = Math.max(paramInt2, mBlockHeight);
      int k = i * j / mBlockWidth / mBlockHeight;
      if (k != 1)
      {
        mBlockCountRange = Utils.factorRange(mBlockCountRange, k);
        mBlocksPerSecondRange = Utils.factorRange(mBlocksPerSecondRange, k);
        mBlockAspectRatioRange = Utils.scaleRange(mBlockAspectRatioRange, j / mBlockHeight, i / mBlockWidth);
        mHorizontalBlockRange = Utils.factorRange(mHorizontalBlockRange, i / mBlockWidth);
        mVerticalBlockRange = Utils.factorRange(mVerticalBlockRange, j / mBlockHeight);
      }
      k = i * j / paramInt1 / paramInt2;
      Object localObject1 = paramRange;
      Object localObject2 = paramRange1;
      Object localObject3 = paramRange2;
      if (k != 1)
      {
        localObject1 = Utils.factorRange(paramRange, k);
        localObject2 = Utils.factorRange(paramRange1, k);
        localObject3 = Utils.scaleRange(paramRange2, j / paramInt2, i / paramInt1);
      }
      mBlockCountRange = mBlockCountRange.intersect((Range)localObject1);
      mBlocksPerSecondRange = mBlocksPerSecondRange.intersect((Range)localObject2);
      mBlockAspectRatioRange = mBlockAspectRatioRange.intersect((Range)localObject3);
      mBlockWidth = i;
      mBlockHeight = j;
    }
    
    private void applyLevelLimits()
    {
      Object localObject1 = this;
      int i = 0;
      Object localObject2 = mParent.profileLevels;
      Object localObject3 = mParent.getMimeType();
      int j;
      int k;
      int m;
      int n;
      int i1;
      long l1;
      int i2;
      Object localObject4;
      int i3;
      int i4;
      int i5;
      int i6;
      int i7;
      int i8;
      StringBuilder localStringBuilder;
      int i9;
      if (((String)localObject3).equalsIgnoreCase("video/avc"))
      {
        j = localObject2.length;
        k = 4;
        m = 0;
        n = 99;
        i1 = 64000;
        l1 = 1485L;
        i2 = 396;
        while (m < j)
        {
          localObject4 = localObject2[m];
          i3 = 0;
          i4 = 0;
          i5 = 0;
          i6 = 0;
          i7 = 1;
          i8 = 1;
          switch (level)
          {
          default: 
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unrecognized level ");
            localStringBuilder.append(level);
            localStringBuilder.append(" for ");
            localStringBuilder.append((String)localObject3);
            Log.w("VideoCapabilities", localStringBuilder.toString());
            i9 = k | 0x1;
            k = i4;
          }
          for (;;)
          {
            break;
            i3 = 16711680;
            i4 = 139264;
            i5 = 800000;
            i6 = 696320;
            i9 = k;
            k = i4;
            continue;
            i3 = 8355840;
            i4 = 139264;
            i5 = 480000;
            i6 = 696320;
            i9 = k;
            k = i4;
            continue;
            i3 = 4177920;
            i4 = 139264;
            i5 = 240000;
            i6 = 696320;
            i9 = k;
            k = i4;
            continue;
            i3 = 2073600;
            i4 = 36864;
            i5 = 240000;
            i6 = 184320;
            i9 = k;
            k = i4;
            continue;
            i3 = 983040;
            i4 = 36864;
            i5 = 240000;
            i6 = 184320;
            i9 = k;
            k = i4;
            continue;
            i3 = 589824;
            i4 = 22080;
            i5 = 135000;
            i6 = 110400;
            i9 = k;
            k = i4;
            continue;
            i3 = 522240;
            i4 = 8704;
            i5 = 50000;
            i6 = 34816;
            i9 = k;
            k = i4;
            continue;
            i3 = 245760;
            i4 = 8192;
            i5 = 50000;
            i6 = 32768;
            i9 = k;
            k = i4;
            continue;
            i3 = 245760;
            i4 = 8192;
            i5 = 20000;
            i6 = 32768;
            i9 = k;
            k = i4;
            continue;
            i3 = 216000;
            i4 = 5120;
            i5 = 20000;
            i6 = 20480;
            i9 = k;
            k = i4;
            continue;
            i3 = 108000;
            i4 = 3600;
            i5 = 14000;
            i6 = 18000;
            i9 = k;
            k = i4;
            continue;
            i3 = 40500;
            i4 = 1620;
            i5 = 10000;
            i6 = 8100;
            i9 = k;
            k = i4;
            continue;
            i3 = 20250;
            i4 = 1620;
            i5 = 4000;
            i6 = 8100;
            i9 = k;
            k = i4;
            continue;
            i3 = 19800;
            i4 = 792;
            i5 = 4000;
            i6 = 4752;
            i9 = k;
            k = i4;
            continue;
            i3 = 11880;
            i4 = 396;
            i5 = 2000;
            i6 = 2376;
            i9 = k;
            k = i4;
            continue;
            i3 = 11880;
            i4 = 396;
            i5 = 768;
            i6 = 2376;
            i9 = k;
            k = i4;
            continue;
            i3 = 6000;
            i4 = 396;
            i5 = 384;
            i6 = 2376;
            i9 = k;
            k = i4;
            continue;
            i3 = 3000;
            i4 = 396;
            i5 = 192;
            i6 = 900;
            i9 = k;
            k = i4;
            continue;
            i3 = 1485;
            i4 = 99;
            i5 = 128;
            i6 = 396;
            i9 = k;
            k = i4;
            continue;
            i3 = 1485;
            i4 = 99;
            i5 = 64;
            i6 = 396;
            i9 = k;
            k = i4;
          }
          i4 = profile;
          if (i4 != 4)
          {
            if (i4 != 8)
            {
              if (i4 == 16) {
                break label1077;
              }
              if ((i4 == 32) || (i4 == 64)) {
                break label1074;
              }
              if (i4 != 65536)
              {
                if (i4 != 524288) {
                  switch (i4)
                  {
                  default: 
                    localStringBuilder = new StringBuilder();
                    localStringBuilder.append("Unrecognized profile ");
                    localStringBuilder.append(profile);
                    localStringBuilder.append(" for ");
                    localStringBuilder.append((String)localObject3);
                    Log.w("VideoCapabilities", localStringBuilder.toString());
                    i9 |= 0x1;
                    i5 *= 1000;
                    break;
                  }
                }
              }
              else {
                break label1163;
              }
            }
            break label1088;
          }
          label1074:
          break label1099;
          label1077:
          i5 *= 3000;
          break label1175;
          label1088:
          i5 *= 1250;
          break label1175;
          label1099:
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unsupported profile ");
          localStringBuilder.append(profile);
          localStringBuilder.append(" for ");
          localStringBuilder.append((String)localObject3);
          Log.w("VideoCapabilities", localStringBuilder.toString());
          i9 |= 0x2;
          i8 = 0;
          label1163:
          i5 *= 1000;
          i7 = i8;
          label1175:
          i8 = i9;
          if (i7 != 0) {
            i8 = i9 & 0xFFFFFFFB;
          }
          l1 = Math.max(i3, l1);
          n = Math.max(k, n);
          i1 = Math.max(i5, i1);
          i2 = Math.max(i2, i6);
          m++;
          k = i8;
        }
        i3 = (int)Math.sqrt(n * 8);
        ((VideoCapabilities)localObject1).applyMacroBlockLimits(i3, i3, n, l1, 16, 16, 1, 1);
        i3 = i1;
      }
      else
      {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        label1908:
        label1934:
        int i15;
        if (((String)localObject3).equalsIgnoreCase("video/mpeg2"))
        {
          localObject4 = localObject2;
          n = localObject4.length;
          i8 = 4;
          i2 = 11;
          m = 0;
          l1 = 1485L;
          i10 = 99;
          j = 64000;
          i4 = 15;
          i7 = 9;
          localObject2 = localObject3;
          localObject3 = localObject4;
          while (m < n)
          {
            localObject4 = localObject3[m];
            i11 = 1;
            k = 1;
            switch (profile)
            {
            default: 
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("Unrecognized profile ");
              localStringBuilder.append(profile);
              localStringBuilder.append(" for ");
              localStringBuilder.append((String)localObject2);
              Log.w("VideoCapabilities", localStringBuilder.toString());
              i3 = i8 | 0x1;
            }
            for (;;)
            {
              i12 = 0;
              i13 = 0;
              i6 = 0;
              i1 = 0;
              i9 = 0;
              i14 = 0;
              i5 = i3;
              i11 = k;
              break label1962;
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("Unsupported profile ");
              localStringBuilder.append(profile);
              localStringBuilder.append(" for ");
              localStringBuilder.append((String)localObject2);
              Log.i("VideoCapabilities", localStringBuilder.toString());
              i3 = i8 | 0x2;
              k = 0;
              continue;
              switch (level)
              {
              default: 
                localStringBuilder = new StringBuilder();
                localStringBuilder.append("Unrecognized profile/level ");
                localStringBuilder.append(profile);
                localStringBuilder.append("/");
                localStringBuilder.append(level);
                localStringBuilder.append(" for ");
                localStringBuilder.append((String)localObject2);
                Log.w("VideoCapabilities", localStringBuilder.toString());
                i3 = i8 | 0x1;
                break;
              case 4: 
                i6 = 60;
                i9 = 120;
                i1 = 68;
                i3 = 489600;
                k = 8160;
                i5 = 80000;
                break;
              case 3: 
                i6 = 60;
                i9 = 120;
                i1 = 68;
                i3 = 244800;
                k = 8160;
                i5 = 80000;
                break;
              case 2: 
                i6 = 60;
                i9 = 90;
                i1 = 68;
                i3 = 183600;
                k = 6120;
                i5 = 60000;
                break;
              case 1: 
                i6 = 30;
                i9 = 45;
                i1 = 36;
                i3 = 40500;
                k = 1620;
                i5 = 15000;
                break;
              case 0: 
                i6 = 30;
                i9 = 22;
                i1 = 18;
                i3 = 11880;
                k = 396;
                i5 = 4000;
                break label1934;
                if (level == 1) {
                  break label1908;
                }
                localStringBuilder = new StringBuilder();
                localStringBuilder.append("Unrecognized profile/level ");
                localStringBuilder.append(profile);
                localStringBuilder.append("/");
                localStringBuilder.append(level);
                localStringBuilder.append(" for ");
                localStringBuilder.append((String)localObject2);
                Log.w("VideoCapabilities", localStringBuilder.toString());
                i3 = i8 | 0x1;
              }
            }
            i6 = 30;
            i9 = 45;
            i1 = 36;
            i3 = 40500;
            k = 1620;
            i5 = 15000;
            i13 = i5;
            i15 = i9;
            i9 = i1;
            i5 = i8;
            i12 = k;
            i14 = i3;
            i1 = i15;
            label1962:
            i8 = i5;
            if (i11 != 0) {
              i8 = i5 & 0xFFFFFFFB;
            }
            l1 = Math.max(i14, l1);
            i10 = Math.max(i12, i10);
            j = Math.max(i13 * 1000, j);
            i2 = Math.max(i1, i2);
            i7 = Math.max(i9, i7);
            i4 = Math.max(i6, i4);
            m++;
          }
          ((VideoCapabilities)localObject1).applyMacroBlockLimits(i2, i7, i10, l1, 16, 16, 1, 1);
          mFrameRateRange = mFrameRateRange.intersect(Integer.valueOf(12), Integer.valueOf(i4));
          i3 = j;
          k = i8;
        }
        else if (((String)localObject3).equalsIgnoreCase("video/mp4v-es"))
        {
          n = localObject2.length;
          i8 = 4;
          i10 = 11;
          i2 = 0;
          l1 = 1485L;
          i13 = 99;
          m = 64000;
          i7 = 15;
          i4 = 9;
          while (i2 < n)
          {
            localObject4 = localObject2[i2];
            i11 = 0;
            j = 0;
            i15 = 1;
            k = 1;
            switch (profile)
            {
            default: 
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Unrecognized profile ");
              ((StringBuilder)localObject1).append(profile);
              ((StringBuilder)localObject1).append(" for ");
              ((StringBuilder)localObject1).append((String)localObject3);
              Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
              i3 = i8 | 0x1;
            }
            for (;;)
            {
              i9 = 0;
              i12 = 0;
              i1 = 0;
              i5 = 0;
              i14 = 0;
              i6 = 0;
              j = i11;
              break label3185;
              i3 = level;
              if ((i3 != 1) && (i3 != 4))
              {
                if (i3 != 8)
                {
                  if (i3 != 16)
                  {
                    if (i3 != 24)
                    {
                      if (i3 != 32)
                      {
                        if (i3 != 128)
                        {
                          localObject1 = new StringBuilder();
                          ((StringBuilder)localObject1).append("Unrecognized profile/level ");
                          ((StringBuilder)localObject1).append(profile);
                          ((StringBuilder)localObject1).append("/");
                          ((StringBuilder)localObject1).append(level);
                          ((StringBuilder)localObject1).append(" for ");
                          ((StringBuilder)localObject1).append((String)localObject3);
                          Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
                          i3 = i8 | 0x1;
                        }
                        else
                        {
                          i6 = 30;
                          i9 = 45;
                          i1 = 36;
                          i3 = 48600;
                          k = 1620;
                          i5 = 8000;
                          break;
                        }
                      }
                      else
                      {
                        i6 = 30;
                        i9 = 44;
                        i1 = 36;
                        i3 = 23760;
                        k = 792;
                        i5 = 3000;
                        break;
                      }
                    }
                    else
                    {
                      i6 = 30;
                      i9 = 22;
                      i1 = 18;
                      i3 = 11880;
                      k = 396;
                      i5 = 1500;
                      break;
                    }
                  }
                  else
                  {
                    i6 = 30;
                    i9 = 22;
                    i1 = 18;
                    i3 = 11880;
                    k = 396;
                    i5 = 768;
                    break;
                  }
                }
                else
                {
                  i6 = 30;
                  i9 = 22;
                  i1 = 18;
                  i3 = 5940;
                  k = 396;
                  i5 = 384;
                  break;
                }
              }
              else
              {
                i6 = 30;
                i9 = 11;
                i1 = 9;
                i3 = 2970;
                k = 99;
                i5 = 128;
                break;
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Unsupported profile ");
                ((StringBuilder)localObject1).append(profile);
                ((StringBuilder)localObject1).append(" for ");
                ((StringBuilder)localObject1).append((String)localObject3);
                Log.i("VideoCapabilities", ((StringBuilder)localObject1).toString());
                i3 = i8 | 0x2;
                k = 0;
                continue;
                i3 = level;
                if (i3 == 4) {
                  break label3157;
                }
                if (i3 == 8) {
                  break label3127;
                }
                if (i3 == 16) {
                  break label3097;
                }
                if (i3 == 64) {
                  break label3068;
                }
                if (i3 == 128) {
                  break label3039;
                }
                if (i3 == 256) {
                  break label3010;
                }
                switch (i3)
                {
                default: 
                  localObject1 = new StringBuilder();
                  ((StringBuilder)localObject1).append("Unrecognized profile/level ");
                  ((StringBuilder)localObject1).append(profile);
                  ((StringBuilder)localObject1).append("/");
                  ((StringBuilder)localObject1).append(level);
                  ((StringBuilder)localObject1).append(" for ");
                  ((StringBuilder)localObject1).append((String)localObject3);
                  Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
                  i3 = i8 | 0x1;
                }
              }
            }
            j = 1;
            i6 = 15;
            i9 = 11;
            i1 = 9;
            i3 = 1485;
            k = 99;
            i5 = 128;
            break label2967;
            j = 1;
            i6 = 15;
            i9 = 11;
            i1 = 9;
            i3 = 1485;
            k = 99;
            i5 = 64;
            for (;;)
            {
              label2967:
              i14 = k;
              i12 = i5;
              k = i6;
              i5 = i9;
              i6 = i3;
              i9 = i14;
              i14 = i1;
              i1 = k;
              i3 = i8;
              k = i15;
              break;
              label3010:
              i6 = 30;
              i9 = 80;
              i1 = 45;
              i3 = 108000;
              k = 3600;
              i5 = 12000;
              continue;
              label3039:
              i6 = 30;
              i9 = 45;
              i1 = 36;
              i3 = 40500;
              k = 1620;
              i5 = 8000;
              continue;
              label3068:
              i6 = 30;
              i9 = 40;
              i1 = 30;
              i3 = 36000;
              k = 1200;
              i5 = 4000;
              continue;
              label3097:
              i6 = 30;
              i9 = 22;
              i1 = 18;
              i3 = 11880;
              k = 396;
              i5 = 384;
              continue;
              label3127:
              i6 = 30;
              i9 = 22;
              i1 = 18;
              i3 = 5940;
              k = 396;
              i5 = 128;
              continue;
              label3157:
              i6 = 30;
              i9 = 11;
              i1 = 9;
              i3 = 1485;
              k = 99;
              i5 = 64;
            }
            label3185:
            i8 = i3;
            if (k != 0) {
              i8 = i3 & 0xFFFFFFFB;
            }
            l1 = Math.max(i6, l1);
            i13 = Math.max(i9, i13);
            m = Math.max(i12 * 1000, m);
            if (j != 0)
            {
              i3 = Math.max(i5, i10);
              k = Math.max(i14, i4);
              i5 = Math.max(i1, i7);
            }
            else
            {
              k = (int)Math.sqrt(i9 * 2);
              i3 = Math.max(k, i10);
              k = Math.max(k, i4);
              i5 = Math.max(Math.max(i1, 60), i7);
            }
            i2++;
            i7 = i5;
            i4 = k;
            i10 = i3;
          }
          applyMacroBlockLimits(i10, i4, i13, l1, 16, 16, 1, 1);
          mFrameRateRange = mFrameRateRange.intersect(Integer.valueOf(12), Integer.valueOf(i7));
          i3 = m;
          k = i8;
        }
        else if (((String)localObject3).equalsIgnoreCase("video/3gpp"))
        {
          i14 = 9;
          i13 = 11;
          n = 9;
          i10 = localObject2.length;
          k = 4;
          m = 16;
          i2 = i13;
          l1 = 1485L;
          i12 = 0;
          i11 = 99;
          i7 = 64000;
          i15 = 15;
          while (i12 < i10)
          {
            localObject4 = localObject2[i12];
            i3 = 0;
            int i16 = 0;
            i5 = 0;
            i6 = 0;
            i9 = 0;
            i1 = i2;
            i8 = n;
            j = 0;
            i4 = level;
            if (i4 != 4) {
              if (i4 != 8) {
                if (i4 != 16) {
                  if (i4 != 32) {
                    if (i4 != 64) {
                      if (i4 != 128) {
                        switch (i4)
                        {
                        default: 
                          localObject1 = new StringBuilder();
                          ((StringBuilder)localObject1).append("Unrecognized profile/level ");
                          ((StringBuilder)localObject1).append(profile);
                          ((StringBuilder)localObject1).append("/");
                          ((StringBuilder)localObject1).append(level);
                          ((StringBuilder)localObject1).append(" for ");
                          ((StringBuilder)localObject1).append((String)localObject3);
                          Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
                          i4 = k | 0x1;
                          k = i16;
                        }
                      }
                    }
                  }
                }
              }
            }
            for (;;)
            {
              break;
              j = 1;
              i5 = 30;
              i6 = 22;
              i9 = 18;
              i16 = 2;
              i3 = 22 * 18 * 15;
              i4 = k;
              k = i16;
              continue;
              j = 1;
              i5 = 15;
              i6 = 11;
              i9 = 9;
              i16 = 1;
              i3 = 11 * 9 * 15;
              i4 = k;
              k = i16;
              continue;
              i1 = 1;
              i8 = 1;
              m = 4;
              i5 = 60;
              i6 = 45;
              i9 = 36;
              i16 = 256;
              i3 = 45 * 36 * 50;
              i4 = k;
              k = i16;
              continue;
              i1 = 1;
              i8 = 1;
              m = 4;
              i5 = 60;
              i6 = 45;
              i9 = 18;
              i16 = 128;
              i3 = 45 * 18 * 50;
              i4 = k;
              k = i16;
              continue;
              i1 = 1;
              i8 = 1;
              m = 4;
              i5 = 60;
              i6 = 22;
              i9 = 18;
              i16 = 64;
              i3 = 22 * 18 * 50;
              i4 = k;
              k = i16;
              continue;
              if ((profile != 1) && (profile != 4)) {
                i3 = 0;
              } else {
                i3 = 1;
              }
              j = i3;
              if (j == 0)
              {
                i1 = 1;
                i8 = 1;
                m = 4;
              }
              i5 = 15;
              i6 = 11;
              i9 = 9;
              i16 = 2;
              i3 = 11 * 9 * 15;
              i4 = k;
              k = i16;
              continue;
              j = 1;
              i5 = 30;
              i6 = 22;
              i9 = 18;
              i16 = 32;
              i3 = 22 * 18 * 30;
              i4 = k;
              k = i16;
              continue;
              j = 1;
              i5 = 30;
              i6 = 22;
              i9 = 18;
              i16 = 6;
              i3 = 22 * 18 * 30;
              i4 = k;
              k = i16;
            }
            i16 = profile;
            if ((i16 != 4) && (i16 != 8) && (i16 != 16) && (i16 != 32) && (i16 != 64) && (i16 != 128) && (i16 != 256)) {
              switch (i16)
              {
              default: 
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Unrecognized profile ");
                ((StringBuilder)localObject1).append(profile);
                ((StringBuilder)localObject1).append(" for ");
                ((StringBuilder)localObject1).append((String)localObject3);
                Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
                i4 |= 0x1;
                break;
              }
            }
            if (j != 0)
            {
              i8 = 11;
              i1 = 9;
            }
            else
            {
              mAllowMbOverride = true;
              j = i1;
              i1 = i8;
              i8 = j;
            }
            j = i4 & 0xFFFFFFFB;
            l1 = Math.max(i3, l1);
            i11 = Math.max(i6 * i9, i11);
            i7 = Math.max(64000 * k, i7);
            i13 = Math.max(i6, i13);
            i14 = Math.max(i9, i14);
            i15 = Math.max(i5, i15);
            i2 = Math.min(i8, i2);
            n = Math.min(i1, n);
            i12++;
            k = j;
          }
          if (!mAllowMbOverride) {
            mBlockAspectRatioRange = Range.create(new Rational(11, 9), new Rational(11, 9));
          }
          applyMacroBlockLimits(i2, n, i13, i14, i11, l1, 16, 16, m, m);
          mFrameRateRange = Range.create(Integer.valueOf(1), Integer.valueOf(i15));
          i3 = i7;
        }
        else if (((String)localObject3).equalsIgnoreCase("video/x-vnd.on2.vp8"))
        {
          i5 = 100000000;
          i6 = localObject2.length;
          k = 4;
          for (i3 = 0; i3 < i6; i3++)
          {
            localObject4 = localObject2[i3];
            i9 = level;
            if ((i9 != 4) && (i9 != 8)) {
              switch (i9)
              {
              default: 
                localStringBuilder = new StringBuilder();
                localStringBuilder.append("Unrecognized level ");
                localStringBuilder.append(level);
                localStringBuilder.append(" for ");
                localStringBuilder.append((String)localObject3);
                Log.w("VideoCapabilities", localStringBuilder.toString());
                k |= 0x1;
                break;
              }
            }
            if (profile != 1)
            {
              localStringBuilder = new StringBuilder();
              localStringBuilder.append("Unrecognized profile ");
              localStringBuilder.append(profile);
              localStringBuilder.append(" for ");
              localStringBuilder.append((String)localObject3);
              Log.w("VideoCapabilities", localStringBuilder.toString());
              k |= 0x1;
            }
            k &= 0xFFFFFFFB;
          }
          ((VideoCapabilities)localObject1).applyMacroBlockLimits(32767, 32767, Integer.MAX_VALUE, 2147483647L, 16, 16, 1, 1);
          i3 = i5;
        }
        else if (((String)localObject3).equalsIgnoreCase("video/x-vnd.on2.vp9"))
        {
          i8 = 36864;
          i1 = localObject2.length;
          i9 = 200000;
          i6 = 4;
          j = 512;
          long l2 = 829440L;
          for (m = 0; m < i1; m++)
          {
            localObject4 = localObject2[m];
            k = 0;
            i5 = 0;
            switch (level)
            {
            default: 
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Unrecognized level ");
              ((StringBuilder)localObject1).append(level);
              ((StringBuilder)localObject1).append(" for ");
              ((StringBuilder)localObject1).append((String)localObject3);
              Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
              i6 |= 0x1;
              i3 = 0;
              l1 = 0L;
              break;
            case 8192: 
              l1 = 4706009088L;
              i3 = 35651584;
              k = 480000;
              i5 = 16832;
              break;
            case 4096: 
              l1 = 2353004544L;
              i3 = 35651584;
              k = 240000;
              i5 = 16832;
              break;
            case 2048: 
              l1 = 1176502272L;
              i3 = 35651584;
              k = 180000;
              i5 = 16832;
              break;
            case 1024: 
              l1 = 1176502272L;
              i3 = 8912896;
              k = 180000;
              i5 = 8384;
              break;
            case 512: 
              l1 = 588251136L;
              i3 = 8912896;
              k = 120000;
              i5 = 8384;
              break;
            case 256: 
              l1 = 311951360L;
              i3 = 8912896;
              k = 60000;
              i5 = 8384;
              break;
            case 128: 
              l1 = 160432128L;
              i3 = 2228224;
              k = 30000;
              i5 = 4160;
              break;
            case 64: 
              l1 = 83558400L;
              i3 = 2228224;
              k = 18000;
              i5 = 4160;
              break;
            case 32: 
              l1 = 36864000L;
              i3 = 983040;
              k = 12000;
              i5 = 2752;
              break;
            case 16: 
              l1 = 20736000L;
              i3 = 552960;
              k = 7200;
              i5 = 2048;
              break;
            case 8: 
              l1 = 9216000L;
              i3 = 245760;
              k = 3600;
              i5 = 1344;
              break;
            case 4: 
              l1 = 4608000L;
              i3 = 122880;
              k = 1800;
              i5 = 960;
              break;
            case 2: 
              l1 = 2764800L;
              i3 = 73728;
              k = 800;
              i5 = 768;
              break;
            case 1: 
              l1 = 829440L;
              i3 = 36864;
              k = 200;
              i5 = 512;
            }
            n = profile;
            if ((n != 4) && (n != 8) && (n != 4096) && (n != 8192)) {
              switch (n)
              {
              default: 
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Unrecognized profile ");
                ((StringBuilder)localObject1).append(profile);
                ((StringBuilder)localObject1).append(" for ");
                ((StringBuilder)localObject1).append((String)localObject3);
                Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
                i6 |= 0x1;
                break;
              }
            }
            i6 &= 0xFFFFFFFB;
            l2 = Math.max(l1, l2);
            i8 = Math.max(i3, i8);
            i9 = Math.max(k * 1000, i9);
            j = Math.max(i5, j);
          }
          i3 = Utils.divUp(j, 8);
          applyMacroBlockLimits(i3, i3, Utils.divUp(i8, 64), Utils.divUp(l2, 64L), 8, 8, 1, 1);
          i3 = i9;
          k = i6;
        }
        else if (((String)localObject3).equalsIgnoreCase("video/hevc"))
        {
          l1 = 'ɀ' * 15;
          i8 = localObject2.length;
          i9 = 576;
          i6 = 128000;
          i5 = 4;
          for (i1 = 0; i1 < i8; i1++)
          {
            localObject4 = localObject2[i1];
            double d = 0.0D;
            i3 = 0;
            k = 0;
            switch (level)
            {
            default: 
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Unrecognized level ");
              ((StringBuilder)localObject1).append(level);
              ((StringBuilder)localObject1).append(" for ");
              ((StringBuilder)localObject1).append((String)localObject3);
              Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
              i5 |= 0x1;
              break;
            case 33554432: 
              d = 120.0D;
              i3 = 35651584;
              k = 800000;
              break;
            case 16777216: 
              d = 120.0D;
              i3 = 35651584;
              k = 240000;
              break;
            case 8388608: 
              d = 60.0D;
              i3 = 35651584;
              k = 480000;
              break;
            case 4194304: 
              d = 60.0D;
              i3 = 35651584;
              k = 120000;
              break;
            case 2097152: 
              d = 30.0D;
              i3 = 35651584;
              k = 240000;
              break;
            case 1048576: 
              d = 30.0D;
              i3 = 35651584;
              k = 60000;
              break;
            case 524288: 
              d = 120.0D;
              i3 = 8912896;
              k = 240000;
              break;
            case 262144: 
              d = 120.0D;
              i3 = 8912896;
              k = 60000;
              break;
            case 131072: 
              d = 60.0D;
              i3 = 8912896;
              k = 160000;
              break;
            case 65536: 
              d = 60.0D;
              i3 = 8912896;
              k = 40000;
              break;
            case 32768: 
              d = 30.0D;
              i3 = 8912896;
              k = 100000;
              break;
            case 16384: 
              d = 30.0D;
              i3 = 8912896;
              k = 25000;
              break;
            case 8192: 
              d = 60.0D;
              i3 = 2228224;
              k = 50000;
              break;
            case 4096: 
              d = 60.0D;
              i3 = 2228224;
              k = 20000;
              break;
            case 2048: 
              d = 30.0D;
              i3 = 2228224;
              k = 30000;
              break;
            case 1024: 
              d = 30.0D;
              i3 = 2228224;
              k = 12000;
              break;
            case 256: 
            case 512: 
              d = 33.75D;
              i3 = 983040;
              k = 10000;
              break;
            case 64: 
            case 128: 
              d = 30.0D;
              i3 = 552960;
              k = 6000;
              break;
            case 16: 
            case 32: 
              d = 30.0D;
              i3 = 245760;
              k = 3000;
              break;
            case 4: 
            case 8: 
              d = 30.0D;
              i3 = 122880;
              k = 1500;
              break;
            case 1: 
            case 2: 
              d = 15.0D;
              i3 = 36864;
              k = 128;
            }
            j = profile;
            if (j != 4096) {
              switch (j)
              {
              default: 
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Unrecognized profile ");
                ((StringBuilder)localObject1).append(profile);
                ((StringBuilder)localObject1).append(" for ");
                ((StringBuilder)localObject1).append((String)localObject3);
                Log.w("VideoCapabilities", ((StringBuilder)localObject1).toString());
                i5 |= 0x1;
                break;
              }
            }
            i3 >>= 6;
            i5 &= 0xFFFFFFFB;
            l1 = Math.max((int)(i3 * d), l1);
            i9 = Math.max(i3, i9);
            i6 = Math.max(k * 1000, i6);
          }
          i3 = (int)Math.sqrt(i9 * 8);
          applyMacroBlockLimits(i3, i3, i9, l1, 8, 8, 1, 1);
          i3 = i6;
          k = i5;
        }
        else
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Unsupported mime ");
          ((StringBuilder)localObject2).append((String)localObject3);
          Log.w("VideoCapabilities", ((StringBuilder)localObject2).toString());
          k = 0x4 | 0x2;
          i3 = 64000;
        }
      }
      mBitrateRange = Range.create(Integer.valueOf(1), Integer.valueOf(i3));
      localObject2 = mParent;
      mError |= k;
    }
    
    private void applyMacroBlockLimits(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
    {
      applyAlignment(paramInt8, paramInt9);
      applyBlockLimits(paramInt6, paramInt7, Range.create(Integer.valueOf(1), Integer.valueOf(paramInt5)), Range.create(Long.valueOf(1L), Long.valueOf(paramLong)), Range.create(new Rational(1, paramInt4), new Rational(paramInt3, 1)));
      mHorizontalBlockRange = mHorizontalBlockRange.intersect(Integer.valueOf(Utils.divUp(paramInt1, mBlockWidth / paramInt6)), Integer.valueOf(paramInt3 / (mBlockWidth / paramInt6)));
      mVerticalBlockRange = mVerticalBlockRange.intersect(Integer.valueOf(Utils.divUp(paramInt2, mBlockHeight / paramInt7)), Integer.valueOf(paramInt4 / (mBlockHeight / paramInt7)));
    }
    
    private void applyMacroBlockLimits(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
    {
      applyMacroBlockLimits(1, 1, paramInt1, paramInt2, paramInt3, paramLong, paramInt4, paramInt5, paramInt6, paramInt7);
    }
    
    public static VideoCapabilities create(MediaFormat paramMediaFormat, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      VideoCapabilities localVideoCapabilities = new VideoCapabilities();
      localVideoCapabilities.init(paramMediaFormat, paramCodecCapabilities);
      return localVideoCapabilities;
    }
    
    public static int equivalentVP9Level(MediaFormat paramMediaFormat)
    {
      paramMediaFormat = paramMediaFormat.getMap();
      Object localObject = Utils.parseSize(paramMediaFormat.get("block-size"), new Size(8, 8));
      int i = ((Size)localObject).getWidth() * ((Size)localObject).getHeight();
      localObject = Utils.parseIntRange(paramMediaFormat.get("block-count-range"), null);
      int j = 0;
      int k;
      if (localObject == null) {
        k = 0;
      } else {
        k = ((Integer)((Range)localObject).getUpper()).intValue() * i;
      }
      localObject = Utils.parseLongRange(paramMediaFormat.get("blocks-per-second-range"), null);
      long l;
      if (localObject == null) {
        l = 0L;
      } else {
        l = i * ((Long)((Range)localObject).getUpper()).longValue();
      }
      localObject = parseWidthHeightRanges(paramMediaFormat.get("size-range"));
      if (localObject == null) {
        i = 0;
      } else {
        i = Math.max(((Integer)((Range)first).getUpper()).intValue(), ((Integer)((Range)second).getUpper()).intValue());
      }
      paramMediaFormat = Utils.parseIntRange(paramMediaFormat.get("bitrate-range"), null);
      if (paramMediaFormat != null) {
        j = Utils.divUp(((Integer)paramMediaFormat.getUpper()).intValue(), 1000);
      }
      if ((l <= 829440L) && (k <= 36864) && (j <= 200) && (i <= 512)) {
        return 1;
      }
      if ((l <= 2764800L) && (k <= 73728) && (j <= 800) && (i <= 768)) {
        return 2;
      }
      if ((l <= 4608000L) && (k <= 122880) && (j <= 1800) && (i <= 960)) {
        return 4;
      }
      if ((l <= 9216000L) && (k <= 245760) && (j <= 3600) && (i <= 1344)) {
        return 8;
      }
      if ((l <= 20736000L) && (k <= 552960) && (j <= 7200) && (i <= 2048)) {
        return 16;
      }
      if ((l <= 36864000L) && (k <= 983040) && (j <= 12000) && (i <= 2752)) {
        return 32;
      }
      if ((l <= 83558400L) && (k <= 2228224) && (j <= 18000) && (i <= 4160)) {
        return 64;
      }
      if ((l <= 160432128L) && (k <= 2228224) && (j <= 30000) && (i <= 4160)) {
        return 128;
      }
      if ((l <= 311951360L) && (k <= 8912896) && (j <= 60000) && (i <= 8384)) {
        return 256;
      }
      if ((l <= 588251136L) && (k <= 8912896) && (j <= 120000) && (i <= 8384)) {
        return 512;
      }
      if ((l <= 1176502272L) && (k <= 8912896) && (j <= 180000) && (i <= 8384)) {
        return 1024;
      }
      if ((l <= 1176502272L) && (k <= 35651584) && (j <= 180000) && (i <= 16832)) {
        return 2048;
      }
      if ((l <= 2353004544L) && (k <= 35651584) && (j <= 240000) && (i <= 16832)) {
        return 4096;
      }
      if ((l <= 4706009088L) && (k <= 35651584) && (j <= 480000) && (i <= 16832)) {
        return 8192;
      }
      return 8192;
    }
    
    private Range<Double> estimateFrameRatesFor(int paramInt1, int paramInt2)
    {
      Object localObject = findClosestSize(paramInt1, paramInt2);
      Range localRange = (Range)mMeasuredFrameRates.get(localObject);
      localObject = Double.valueOf(getBlockCount(((Size)localObject).getWidth(), ((Size)localObject).getHeight()) / Math.max(getBlockCount(paramInt1, paramInt2), 1));
      return Range.create(Double.valueOf(((Long)localRange.getLower()).longValue() * ((Double)localObject).doubleValue()), Double.valueOf(((Long)localRange.getUpper()).longValue() * ((Double)localObject).doubleValue()));
    }
    
    private Size findClosestSize(int paramInt1, int paramInt2)
    {
      int i = getBlockCount(paramInt1, paramInt2);
      Object localObject = null;
      paramInt2 = Integer.MAX_VALUE;
      Iterator localIterator = mMeasuredFrameRates.keySet().iterator();
      while (localIterator.hasNext())
      {
        Size localSize = (Size)localIterator.next();
        int j = Math.abs(i - getBlockCount(localSize.getWidth(), localSize.getHeight()));
        paramInt1 = paramInt2;
        if (j < paramInt2)
        {
          paramInt1 = j;
          localObject = localSize;
        }
        paramInt2 = paramInt1;
      }
      return localObject;
    }
    
    private int getBlockCount(int paramInt1, int paramInt2)
    {
      return Utils.divUp(paramInt1, mBlockWidth) * Utils.divUp(paramInt2, mBlockHeight);
    }
    
    private Map<Size, Range<Long>> getMeasuredFrameRates(Map<String, Object> paramMap)
    {
      HashMap localHashMap = new HashMap();
      Iterator localIterator = paramMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        Object localObject1 = (String)localIterator.next();
        if (((String)localObject1).startsWith("measured-frame-rate-"))
        {
          ((String)localObject1).substring("measured-frame-rate-".length());
          Object localObject2 = ((String)localObject1).split("-");
          if (localObject2.length == 5)
          {
            localObject2 = Utils.parseSize(localObject2[3], null);
            if ((localObject2 != null) && (((Size)localObject2).getWidth() * ((Size)localObject2).getHeight() > 0))
            {
              localObject1 = Utils.parseLongRange(paramMap.get(localObject1), null);
              if ((localObject1 != null) && (((Long)((Range)localObject1).getLower()).longValue() >= 0L) && (((Long)((Range)localObject1).getUpper()).longValue() >= 0L)) {
                localHashMap.put(localObject2, localObject1);
              }
            }
          }
        }
      }
      return localHashMap;
    }
    
    private void init(MediaFormat paramMediaFormat, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      mParent = paramCodecCapabilities;
      initWithPlatformLimits();
      applyLevelLimits();
      parseFromInfo(paramMediaFormat);
      updateLimits();
    }
    
    private void initWithPlatformLimits()
    {
      mBitrateRange = MediaCodecInfo.BITRATE_RANGE;
      mWidthRange = MediaCodecInfo.SIZE_RANGE;
      mHeightRange = MediaCodecInfo.SIZE_RANGE;
      mFrameRateRange = MediaCodecInfo.FRAME_RATE_RANGE;
      mHorizontalBlockRange = MediaCodecInfo.SIZE_RANGE;
      mVerticalBlockRange = MediaCodecInfo.SIZE_RANGE;
      mBlockCountRange = MediaCodecInfo.POSITIVE_INTEGERS;
      mBlocksPerSecondRange = MediaCodecInfo.POSITIVE_LONGS;
      mBlockAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS;
      mAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS;
      mWidthAlignment = 2;
      mHeightAlignment = 2;
      mBlockWidth = 2;
      mBlockHeight = 2;
      mSmallerDimensionUpperLimit = ((Integer)MediaCodecInfo.SIZE_RANGE.getUpper()).intValue();
    }
    
    private void parseFromInfo(MediaFormat paramMediaFormat)
    {
      Object localObject1 = paramMediaFormat.getMap();
      Object localObject2 = new Size(mBlockWidth, mBlockHeight);
      Object localObject3 = new Size(mWidthAlignment, mHeightAlignment);
      Object localObject4 = null;
      paramMediaFormat = null;
      Size localSize1 = Utils.parseSize(((Map)localObject1).get("block-size"), (Size)localObject2);
      Size localSize2 = Utils.parseSize(((Map)localObject1).get("alignment"), (Size)localObject3);
      Range localRange1 = Utils.parseIntRange(((Map)localObject1).get("block-count-range"), null);
      Range localRange2 = Utils.parseLongRange(((Map)localObject1).get("blocks-per-second-range"), null);
      mMeasuredFrameRates = getMeasuredFrameRates((Map)localObject1);
      localObject3 = parseWidthHeightRanges(((Map)localObject1).get("size-range"));
      if (localObject3 != null)
      {
        localObject4 = (Range)first;
        paramMediaFormat = (Range)second;
      }
      localObject3 = localObject4;
      localObject2 = paramMediaFormat;
      if (((Map)localObject1).containsKey("feature-can-swap-width-height")) {
        if (localObject4 != null)
        {
          mSmallerDimensionUpperLimit = Math.min(((Integer)((Range)localObject4).getUpper()).intValue(), ((Integer)paramMediaFormat.getUpper()).intValue());
          localObject3 = ((Range)localObject4).extend(paramMediaFormat);
          localObject2 = localObject3;
        }
        else
        {
          Log.w("VideoCapabilities", "feature can-swap-width-height is best used with size-range");
          mSmallerDimensionUpperLimit = Math.min(((Integer)mWidthRange.getUpper()).intValue(), ((Integer)mHeightRange.getUpper()).intValue());
          localObject3 = mWidthRange.extend(mHeightRange);
          mHeightRange = ((Range)localObject3);
          mWidthRange = ((Range)localObject3);
          localObject2 = paramMediaFormat;
          localObject3 = localObject4;
        }
      }
      Range localRange3 = Utils.parseRationalRange(((Map)localObject1).get("block-aspect-ratio-range"), null);
      Range localRange4 = Utils.parseRationalRange(((Map)localObject1).get("pixel-aspect-ratio-range"), null);
      localObject4 = Utils.parseIntRange(((Map)localObject1).get("frame-rate-range"), null);
      paramMediaFormat = (MediaFormat)localObject4;
      if (localObject4 != null) {
        try
        {
          paramMediaFormat = ((Range)localObject4).intersect(MediaCodecInfo.FRAME_RATE_RANGE);
        }
        catch (IllegalArgumentException paramMediaFormat)
        {
          paramMediaFormat = new StringBuilder();
          paramMediaFormat.append("frame rate range (");
          paramMediaFormat.append(localObject4);
          paramMediaFormat.append(") is out of limits: ");
          paramMediaFormat.append(MediaCodecInfo.FRAME_RATE_RANGE);
          Log.w("VideoCapabilities", paramMediaFormat.toString());
          paramMediaFormat = null;
        }
      }
      localObject1 = Utils.parseIntRange(((Map)localObject1).get("bitrate-range"), null);
      localObject4 = localObject1;
      StringBuilder localStringBuilder;
      if (localObject1 != null) {
        try
        {
          localObject4 = ((Range)localObject1).intersect(MediaCodecInfo.BITRATE_RANGE);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("bitrate range (");
          localStringBuilder.append(localObject1);
          localStringBuilder.append(") is out of limits: ");
          localStringBuilder.append(MediaCodecInfo.BITRATE_RANGE);
          Log.w("VideoCapabilities", localStringBuilder.toString());
          localStringBuilder = null;
        }
      }
      MediaCodecInfo.checkPowerOfTwo(localSize1.getWidth(), "block-size width must be power of two");
      MediaCodecInfo.checkPowerOfTwo(localSize1.getHeight(), "block-size height must be power of two");
      MediaCodecInfo.checkPowerOfTwo(localSize2.getWidth(), "alignment width must be power of two");
      MediaCodecInfo.checkPowerOfTwo(localSize2.getHeight(), "alignment height must be power of two");
      applyMacroBlockLimits(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, localSize1.getWidth(), localSize1.getHeight(), localSize2.getWidth(), localSize2.getHeight());
      if (((mParent.mError & 0x2) == 0) && (!mAllowMbOverride))
      {
        if (localObject3 != null) {
          mWidthRange = mWidthRange.intersect((Range)localObject3);
        }
        if (localObject2 != null) {
          mHeightRange = mHeightRange.intersect((Range)localObject2);
        }
        if (localRange1 != null) {
          mBlockCountRange = mBlockCountRange.intersect(Utils.factorRange(localRange1, mBlockWidth * mBlockHeight / localSize1.getWidth() / localSize1.getHeight()));
        }
        if (localRange2 != null) {
          mBlocksPerSecondRange = mBlocksPerSecondRange.intersect(Utils.factorRange(localRange2, mBlockWidth * mBlockHeight / localSize1.getWidth() / localSize1.getHeight()));
        }
        if (localRange4 != null) {
          mBlockAspectRatioRange = mBlockAspectRatioRange.intersect(Utils.scaleRange(localRange4, mBlockHeight / localSize1.getHeight(), mBlockWidth / localSize1.getWidth()));
        }
        if (localRange3 != null) {
          mAspectRatioRange = mAspectRatioRange.intersect(localRange3);
        }
        if (paramMediaFormat != null) {
          mFrameRateRange = mFrameRateRange.intersect(paramMediaFormat);
        }
        if (localStringBuilder != null) {
          mBitrateRange = mBitrateRange.intersect(localStringBuilder);
        }
      }
      else
      {
        if (localObject3 != null) {
          mWidthRange = MediaCodecInfo.SIZE_RANGE.intersect((Range)localObject3);
        }
        if (localObject2 != null) {
          mHeightRange = MediaCodecInfo.SIZE_RANGE.intersect((Range)localObject2);
        }
        if (localRange1 != null) {
          mBlockCountRange = MediaCodecInfo.POSITIVE_INTEGERS.intersect(Utils.factorRange(localRange1, mBlockWidth * mBlockHeight / localSize1.getWidth() / localSize1.getHeight()));
        }
        if (localRange2 != null) {
          mBlocksPerSecondRange = MediaCodecInfo.POSITIVE_LONGS.intersect(Utils.factorRange(localRange2, mBlockWidth * mBlockHeight / localSize1.getWidth() / localSize1.getHeight()));
        }
        if (localRange4 != null) {
          mBlockAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS.intersect(Utils.scaleRange(localRange4, mBlockHeight / localSize1.getHeight(), mBlockWidth / localSize1.getWidth()));
        }
        if (localRange3 != null) {
          mAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS.intersect(localRange3);
        }
        if (paramMediaFormat != null) {
          mFrameRateRange = MediaCodecInfo.FRAME_RATE_RANGE.intersect(paramMediaFormat);
        }
        if (localStringBuilder != null) {
          if ((mParent.mError & 0x2) != 0) {
            mBitrateRange = MediaCodecInfo.BITRATE_RANGE.intersect(localStringBuilder);
          } else {
            mBitrateRange = mBitrateRange.intersect(localStringBuilder);
          }
        }
      }
      updateLimits();
    }
    
    private static Pair<Range<Integer>, Range<Integer>> parseWidthHeightRanges(Object paramObject)
    {
      Pair localPair = Utils.parseSizeRange(paramObject);
      if (localPair != null) {
        try
        {
          localPair = Pair.create(Range.create(Integer.valueOf(((Size)first).getWidth()), Integer.valueOf(((Size)second).getWidth())), Range.create(Integer.valueOf(((Size)first).getHeight()), Integer.valueOf(((Size)second).getHeight())));
          return localPair;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("could not parse size range '");
          localStringBuilder.append(paramObject);
          localStringBuilder.append("'");
          Log.w("VideoCapabilities", localStringBuilder.toString());
        }
      }
      return null;
    }
    
    private boolean supports(Integer paramInteger1, Integer paramInteger2, Number paramNumber)
    {
      boolean bool1 = true;
      boolean bool2 = false;
      boolean bool3 = bool1;
      if (1 != 0)
      {
        bool3 = bool1;
        if (paramInteger1 != null) {
          if ((mWidthRange.contains(paramInteger1)) && (paramInteger1.intValue() % mWidthAlignment == 0)) {
            bool3 = true;
          } else {
            bool3 = false;
          }
        }
      }
      bool1 = bool3;
      if (bool3)
      {
        bool1 = bool3;
        if (paramInteger2 != null)
        {
          if ((mHeightRange.contains(paramInteger2)) && (paramInteger2.intValue() % mHeightAlignment == 0)) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          bool1 = bool3;
        }
      }
      bool3 = bool1;
      if (bool1)
      {
        bool3 = bool1;
        if (paramNumber != null) {
          bool3 = mFrameRateRange.contains(Utils.intRangeFor(paramNumber.doubleValue()));
        }
      }
      bool1 = bool3;
      if (bool3)
      {
        bool1 = bool3;
        if (paramInteger2 != null)
        {
          bool1 = bool3;
          if (paramInteger1 != null)
          {
            int i;
            if (Math.min(paramInteger2.intValue(), paramInteger1.intValue()) <= mSmallerDimensionUpperLimit) {
              i = 1;
            } else {
              i = 0;
            }
            int j = Utils.divUp(paramInteger1.intValue(), mBlockWidth);
            int k = Utils.divUp(paramInteger2.intValue(), mBlockHeight);
            int m = j * k;
            bool3 = bool2;
            if (i != 0)
            {
              bool3 = bool2;
              if (mBlockCountRange.contains(Integer.valueOf(m)))
              {
                bool3 = bool2;
                if (mBlockAspectRatioRange.contains(new Rational(j, k)))
                {
                  bool3 = bool2;
                  if (mAspectRatioRange.contains(new Rational(paramInteger1.intValue(), paramInteger2.intValue()))) {
                    bool3 = true;
                  }
                }
              }
            }
            bool1 = bool3;
            if (bool3)
            {
              bool1 = bool3;
              if (paramNumber != null)
              {
                double d1 = m;
                double d2 = paramNumber.doubleValue();
                bool1 = mBlocksPerSecondRange.contains(Utils.longRangeFor(d1 * d2));
              }
            }
          }
        }
      }
      return bool1;
    }
    
    private void updateLimits()
    {
      mHorizontalBlockRange = mHorizontalBlockRange.intersect(Utils.factorRange(mWidthRange, mBlockWidth));
      mHorizontalBlockRange = mHorizontalBlockRange.intersect(Range.create(Integer.valueOf(((Integer)mBlockCountRange.getLower()).intValue() / ((Integer)mVerticalBlockRange.getUpper()).intValue()), Integer.valueOf(((Integer)mBlockCountRange.getUpper()).intValue() / ((Integer)mVerticalBlockRange.getLower()).intValue())));
      mVerticalBlockRange = mVerticalBlockRange.intersect(Utils.factorRange(mHeightRange, mBlockHeight));
      mVerticalBlockRange = mVerticalBlockRange.intersect(Range.create(Integer.valueOf(((Integer)mBlockCountRange.getLower()).intValue() / ((Integer)mHorizontalBlockRange.getUpper()).intValue()), Integer.valueOf(((Integer)mBlockCountRange.getUpper()).intValue() / ((Integer)mHorizontalBlockRange.getLower()).intValue())));
      mBlockCountRange = mBlockCountRange.intersect(Range.create(Integer.valueOf(((Integer)mHorizontalBlockRange.getLower()).intValue() * ((Integer)mVerticalBlockRange.getLower()).intValue()), Integer.valueOf(((Integer)mHorizontalBlockRange.getUpper()).intValue() * ((Integer)mVerticalBlockRange.getUpper()).intValue())));
      mBlockAspectRatioRange = mBlockAspectRatioRange.intersect(new Rational(((Integer)mHorizontalBlockRange.getLower()).intValue(), ((Integer)mVerticalBlockRange.getUpper()).intValue()), new Rational(((Integer)mHorizontalBlockRange.getUpper()).intValue(), ((Integer)mVerticalBlockRange.getLower()).intValue()));
      mWidthRange = mWidthRange.intersect(Integer.valueOf((((Integer)mHorizontalBlockRange.getLower()).intValue() - 1) * mBlockWidth + mWidthAlignment), Integer.valueOf(((Integer)mHorizontalBlockRange.getUpper()).intValue() * mBlockWidth));
      mHeightRange = mHeightRange.intersect(Integer.valueOf((((Integer)mVerticalBlockRange.getLower()).intValue() - 1) * mBlockHeight + mHeightAlignment), Integer.valueOf(((Integer)mVerticalBlockRange.getUpper()).intValue() * mBlockHeight));
      mAspectRatioRange = mAspectRatioRange.intersect(new Rational(((Integer)mWidthRange.getLower()).intValue(), ((Integer)mHeightRange.getUpper()).intValue()), new Rational(((Integer)mWidthRange.getUpper()).intValue(), ((Integer)mHeightRange.getLower()).intValue()));
      mSmallerDimensionUpperLimit = Math.min(mSmallerDimensionUpperLimit, Math.min(((Integer)mWidthRange.getUpper()).intValue(), ((Integer)mHeightRange.getUpper()).intValue()));
      mBlocksPerSecondRange = mBlocksPerSecondRange.intersect(Long.valueOf(((Integer)mBlockCountRange.getLower()).intValue() * ((Integer)mFrameRateRange.getLower()).intValue()), Long.valueOf(((Integer)mBlockCountRange.getUpper()).intValue() * ((Integer)mFrameRateRange.getUpper()).intValue()));
      mFrameRateRange = mFrameRateRange.intersect(Integer.valueOf((int)(((Long)mBlocksPerSecondRange.getLower()).longValue() / ((Integer)mBlockCountRange.getUpper()).intValue())), Integer.valueOf((int)(((Long)mBlocksPerSecondRange.getUpper()).longValue() / ((Integer)mBlockCountRange.getLower()).intValue())));
    }
    
    public boolean areSizeAndRateSupported(int paramInt1, int paramInt2, double paramDouble)
    {
      return supports(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Double.valueOf(paramDouble));
    }
    
    public Range<Double> getAchievableFrameRatesFor(int paramInt1, int paramInt2)
    {
      if (supports(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), null))
      {
        if ((mMeasuredFrameRates != null) && (mMeasuredFrameRates.size() > 0)) {
          return estimateFrameRatesFor(paramInt1, paramInt2);
        }
        Log.w("VideoCapabilities", "Codec did not publish any measurement data.");
        return null;
      }
      throw new IllegalArgumentException("unsupported size");
    }
    
    public Range<Rational> getAspectRatioRange(boolean paramBoolean)
    {
      Range localRange;
      if (paramBoolean) {
        localRange = mBlockAspectRatioRange;
      } else {
        localRange = mAspectRatioRange;
      }
      return localRange;
    }
    
    public Range<Integer> getBitrateRange()
    {
      return mBitrateRange;
    }
    
    public Range<Integer> getBlockCountRange()
    {
      return mBlockCountRange;
    }
    
    public Size getBlockSize()
    {
      return new Size(mBlockWidth, mBlockHeight);
    }
    
    public Range<Long> getBlocksPerSecondRange()
    {
      return mBlocksPerSecondRange;
    }
    
    public int getHeightAlignment()
    {
      return mHeightAlignment;
    }
    
    public int getSmallerDimensionUpperLimit()
    {
      return mSmallerDimensionUpperLimit;
    }
    
    public Range<Integer> getSupportedFrameRates()
    {
      return mFrameRateRange;
    }
    
    public Range<Double> getSupportedFrameRatesFor(int paramInt1, int paramInt2)
    {
      Range localRange = mHeightRange;
      if (supports(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), null))
      {
        paramInt1 = Utils.divUp(paramInt1, mBlockWidth) * Utils.divUp(paramInt2, mBlockHeight);
        return Range.create(Double.valueOf(Math.max(((Long)mBlocksPerSecondRange.getLower()).longValue() / paramInt1, ((Integer)mFrameRateRange.getLower()).intValue())), Double.valueOf(Math.min(((Long)mBlocksPerSecondRange.getUpper()).longValue() / paramInt1, ((Integer)mFrameRateRange.getUpper()).intValue())));
      }
      throw new IllegalArgumentException("unsupported size");
    }
    
    public Range<Integer> getSupportedHeights()
    {
      return mHeightRange;
    }
    
    public Range<Integer> getSupportedHeightsFor(int paramInt)
    {
      try
      {
        Object localObject = mHeightRange;
        if ((mWidthRange.contains(Integer.valueOf(paramInt))) && (paramInt % mWidthAlignment == 0))
        {
          int i = Utils.divUp(paramInt, mBlockWidth);
          int j = Math.max(Utils.divUp(((Integer)mBlockCountRange.getLower()).intValue(), i), (int)Math.ceil(i / ((Rational)mBlockAspectRatioRange.getUpper()).doubleValue()));
          i = Math.min(((Integer)mBlockCountRange.getUpper()).intValue() / i, (int)(i / ((Rational)mBlockAspectRatioRange.getLower()).doubleValue()));
          Range localRange = ((Range)localObject).intersect(Integer.valueOf((j - 1) * mBlockHeight + mHeightAlignment), Integer.valueOf(mBlockHeight * i));
          localObject = localRange;
          if (paramInt > mSmallerDimensionUpperLimit) {
            localObject = localRange.intersect(Integer.valueOf(1), Integer.valueOf(mSmallerDimensionUpperLimit));
          }
          return ((Range)localObject).intersect(Integer.valueOf((int)Math.ceil(paramInt / ((Rational)mAspectRatioRange.getUpper()).doubleValue())), Integer.valueOf((int)(paramInt / ((Rational)mAspectRatioRange.getLower()).doubleValue())));
        }
        localObject = new java/lang/IllegalArgumentException;
        ((IllegalArgumentException)localObject).<init>("unsupported width");
        throw ((Throwable)localObject);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("could not get supported heights for ");
        localStringBuilder.append(paramInt);
        Log.v("VideoCapabilities", localStringBuilder.toString());
        throw new IllegalArgumentException("unsupported width");
      }
    }
    
    public Range<Integer> getSupportedWidths()
    {
      return mWidthRange;
    }
    
    public Range<Integer> getSupportedWidthsFor(int paramInt)
    {
      try
      {
        Object localObject = mWidthRange;
        if ((mHeightRange.contains(Integer.valueOf(paramInt))) && (paramInt % mHeightAlignment == 0))
        {
          int i = Utils.divUp(paramInt, mBlockHeight);
          int j = Math.max(Utils.divUp(((Integer)mBlockCountRange.getLower()).intValue(), i), (int)Math.ceil(((Rational)mBlockAspectRatioRange.getLower()).doubleValue() * i));
          i = Math.min(((Integer)mBlockCountRange.getUpper()).intValue() / i, (int)(((Rational)mBlockAspectRatioRange.getUpper()).doubleValue() * i));
          Range localRange = ((Range)localObject).intersect(Integer.valueOf((j - 1) * mBlockWidth + mWidthAlignment), Integer.valueOf(mBlockWidth * i));
          localObject = localRange;
          if (paramInt > mSmallerDimensionUpperLimit) {
            localObject = localRange.intersect(Integer.valueOf(1), Integer.valueOf(mSmallerDimensionUpperLimit));
          }
          return ((Range)localObject).intersect(Integer.valueOf((int)Math.ceil(((Rational)mAspectRatioRange.getLower()).doubleValue() * paramInt)), Integer.valueOf((int)(((Rational)mAspectRatioRange.getUpper()).doubleValue() * paramInt)));
        }
        localObject = new java/lang/IllegalArgumentException;
        ((IllegalArgumentException)localObject).<init>("unsupported height");
        throw ((Throwable)localObject);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("could not get supported widths for ");
        localStringBuilder.append(paramInt);
        Log.v("VideoCapabilities", localStringBuilder.toString());
        throw new IllegalArgumentException("unsupported height");
      }
    }
    
    public int getWidthAlignment()
    {
      return mWidthAlignment;
    }
    
    public boolean isSizeSupported(int paramInt1, int paramInt2)
    {
      return supports(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), null);
    }
    
    public boolean supportsFormat(MediaFormat paramMediaFormat)
    {
      Map localMap = paramMediaFormat.getMap();
      if (!supports((Integer)localMap.get("width"), (Integer)localMap.get("height"), (Number)localMap.get("frame-rate"))) {
        return false;
      }
      return MediaCodecInfo.CodecCapabilities.supportsBitrate(mBitrateRange, paramMediaFormat);
    }
  }
}
