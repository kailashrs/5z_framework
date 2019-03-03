package android.media.audiofx;

import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.StringTokenizer;

public final class DynamicsProcessing
  extends AudioEffect
{
  private static final int CHANNEL_COUNT_MAX = 32;
  private static final float CHANNEL_DEFAULT_INPUT_GAIN = 0.0F;
  private static final int CONFIG_DEFAULT_MBC_BANDS = 6;
  private static final int CONFIG_DEFAULT_POSTEQ_BANDS = 6;
  private static final int CONFIG_DEFAULT_PREEQ_BANDS = 6;
  private static final boolean CONFIG_DEFAULT_USE_LIMITER = true;
  private static final boolean CONFIG_DEFAULT_USE_MBC = true;
  private static final boolean CONFIG_DEFAULT_USE_POSTEQ = true;
  private static final boolean CONFIG_DEFAULT_USE_PREEQ = true;
  private static final int CONFIG_DEFAULT_VARIANT = 0;
  private static final float CONFIG_PREFERRED_FRAME_DURATION_MS = 10.0F;
  private static final float DEFAULT_MAX_FREQUENCY = 20000.0F;
  private static final float DEFAULT_MIN_FREQUENCY = 220.0F;
  private static final float EQ_DEFAULT_GAIN = 0.0F;
  private static final float LIMITER_DEFAULT_ATTACK_TIME = 1.0F;
  private static final boolean LIMITER_DEFAULT_ENABLED = true;
  private static final int LIMITER_DEFAULT_LINK_GROUP = 0;
  private static final float LIMITER_DEFAULT_POST_GAIN = 0.0F;
  private static final float LIMITER_DEFAULT_RATIO = 10.0F;
  private static final float LIMITER_DEFAULT_RELEASE_TIME = 60.0F;
  private static final float LIMITER_DEFAULT_THRESHOLD = -2.0F;
  private static final float MBC_DEFAULT_ATTACK_TIME = 3.0F;
  private static final boolean MBC_DEFAULT_ENABLED = true;
  private static final float MBC_DEFAULT_EXPANDER_RATIO = 1.0F;
  private static final float MBC_DEFAULT_KNEE_WIDTH = 0.0F;
  private static final float MBC_DEFAULT_NOISE_GATE_THRESHOLD = -90.0F;
  private static final float MBC_DEFAULT_POST_GAIN = 0.0F;
  private static final float MBC_DEFAULT_PRE_GAIN = 0.0F;
  private static final float MBC_DEFAULT_RATIO = 1.0F;
  private static final float MBC_DEFAULT_RELEASE_TIME = 80.0F;
  private static final float MBC_DEFAULT_THRESHOLD = -45.0F;
  private static final int PARAM_ENGINE_ARCHITECTURE = 48;
  private static final int PARAM_GET_CHANNEL_COUNT = 16;
  private static final int PARAM_INPUT_GAIN = 32;
  private static final int PARAM_LIMITER = 112;
  private static final int PARAM_MBC = 80;
  private static final int PARAM_MBC_BAND = 85;
  private static final int PARAM_POST_EQ = 96;
  private static final int PARAM_POST_EQ_BAND = 101;
  private static final int PARAM_PRE_EQ = 64;
  private static final int PARAM_PRE_EQ_BAND = 69;
  private static final boolean POSTEQ_DEFAULT_ENABLED = true;
  private static final boolean PREEQ_DEFAULT_ENABLED = true;
  private static final String TAG = "DynamicsProcessing";
  public static final int VARIANT_FAVOR_FREQUENCY_RESOLUTION = 0;
  public static final int VARIANT_FAVOR_TIME_RESOLUTION = 1;
  private static final float mMaxFreqLog = (float)Math.log10(20000.0D);
  private static final float mMinFreqLog = (float)Math.log10(220.0D);
  private BaseParameterListener mBaseParamListener;
  private int mChannelCount;
  private OnParameterChangeListener mParamListener;
  private final Object mParamListenerLock;
  
  public DynamicsProcessing(int paramInt)
  {
    this(0, paramInt);
  }
  
  public DynamicsProcessing(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, null);
  }
  
  public DynamicsProcessing(int paramInt1, int paramInt2, Config paramConfig)
  {
    super(EFFECT_TYPE_DYNAMICS_PROCESSING, EFFECT_TYPE_NULL, paramInt1, paramInt2);
    paramInt1 = 0;
    mChannelCount = 0;
    mParamListener = null;
    mBaseParamListener = null;
    mParamListenerLock = new Object();
    if (paramInt2 == 0) {
      Log.w("DynamicsProcessing", "WARNING: attaching a DynamicsProcessing to global output mix isdeprecated!");
    }
    mChannelCount = getChannelCount();
    if (paramConfig == null) {
      paramConfig = new DynamicsProcessing.Config.Builder(0, mChannelCount, true, 6, true, 6, true, 6, true).build();
    } else {
      paramConfig = new Config(mChannelCount, paramConfig);
    }
    setEngineArchitecture(paramConfig.getVariant(), paramConfig.getPreferredFrameDuration(), paramConfig.isPreEqInUse(), paramConfig.getPreEqBandCount(), paramConfig.isMbcInUse(), paramConfig.getMbcBandCount(), paramConfig.isPostEqInUse(), paramConfig.getPostEqBandCount(), paramConfig.isLimiterInUse());
    while (paramInt1 < mChannelCount)
    {
      updateEngineChannelByChannelIndex(paramInt1, paramConfig.getChannelByChannelIndex(paramInt1));
      paramInt1++;
    }
  }
  
  private void byteArrayToNumberArray(byte[] paramArrayOfByte, Number[] paramArrayOfNumber)
  {
    int i = 0;
    int j = 0;
    if ((i < paramArrayOfByte.length) && (j < paramArrayOfNumber.length))
    {
      int k;
      if ((paramArrayOfNumber[j] instanceof Integer))
      {
        k = j + 1;
        paramArrayOfNumber[j] = Integer.valueOf(byteArrayToInt(paramArrayOfByte, i));
        i += 4;
      }
      for (j = k;; j = k)
      {
        break;
        if (!(paramArrayOfNumber[j] instanceof Float)) {
          break label94;
        }
        k = j + 1;
        paramArrayOfNumber[j] = Float.valueOf(byteArrayToFloat(paramArrayOfByte, i));
        i += 4;
      }
      label94:
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("can't convert ");
      paramArrayOfByte.append(paramArrayOfNumber[j].getClass());
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    if (j == paramArrayOfNumber.length) {
      return;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("only converted ");
    paramArrayOfByte.append(j);
    paramArrayOfByte.append(" values out of ");
    paramArrayOfByte.append(paramArrayOfNumber.length);
    paramArrayOfByte.append(" expected");
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  private int getOneInt(int paramInt)
  {
    int[] arrayOfInt = new int[1];
    checkStatus(getParameter(new int[] { paramInt }, arrayOfInt));
    return arrayOfInt[0];
  }
  
  private float getTwoFloat(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[4];
    checkStatus(getParameter(new int[] { paramInt1, paramInt2 }, arrayOfByte));
    return byteArrayToFloat(arrayOfByte);
  }
  
  private byte[] numberArrayToByteArray(Number[] paramArrayOfNumber)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    while (k < paramArrayOfNumber.length)
    {
      if ((paramArrayOfNumber[k] instanceof Integer))
      {
        j += 4;
      }
      else
      {
        if (!(paramArrayOfNumber[k] instanceof Float)) {
          break label49;
        }
        j += 4;
      }
      k++;
      continue;
      label49:
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("unknown value type ");
      ((StringBuilder)localObject).append(paramArrayOfNumber[k].getClass());
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    Object localObject = ByteBuffer.allocate(j);
    ((ByteBuffer)localObject).order(ByteOrder.nativeOrder());
    for (j = i; j < paramArrayOfNumber.length; j++) {
      if ((paramArrayOfNumber[j] instanceof Integer)) {
        ((ByteBuffer)localObject).putInt(paramArrayOfNumber[j].intValue());
      } else if ((paramArrayOfNumber[j] instanceof Float)) {
        ((ByteBuffer)localObject).putFloat(paramArrayOfNumber[j].floatValue());
      }
    }
    return ((ByteBuffer)localObject).array();
  }
  
  private Channel queryEngineByChannelIndex(int paramInt)
  {
    float f = getTwoFloat(32, paramInt);
    Eq localEq1 = queryEngineEqByChannelIndex(64, paramInt);
    Mbc localMbc = queryEngineMbcByChannelIndex(paramInt);
    Eq localEq2 = queryEngineEqByChannelIndex(96, paramInt);
    Limiter localLimiter = queryEngineLimiterByChannelIndex(paramInt);
    Channel localChannel = new Channel(f, localEq1.isInUse(), localEq1.getBandCount(), localMbc.isInUse(), localMbc.getBandCount(), localEq2.isInUse(), localEq2.getBandCount(), localLimiter.isInUse());
    localChannel.setInputGain(f);
    localChannel.setPreEq(localEq1);
    localChannel.setMbc(localMbc);
    localChannel.setPostEq(localEq2);
    localChannel.setLimiter(localLimiter);
    return localChannel;
  }
  
  private EqBand queryEngineEqBandByChannelIndex(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool = false;
    Number[] arrayOfNumber = new Number[3];
    arrayOfNumber[0] = Integer.valueOf(0);
    arrayOfNumber[1] = Float.valueOf(0.0F);
    arrayOfNumber[2] = Float.valueOf(0.0F);
    byte[] arrayOfByte1 = numberArrayToByteArray(new Number[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
    byte[] arrayOfByte2 = numberArrayToByteArray(arrayOfNumber);
    getParameter(arrayOfByte1, arrayOfByte2);
    byteArrayToNumberArray(arrayOfByte2, arrayOfNumber);
    if (arrayOfNumber[0].intValue() > 0) {
      bool = true;
    }
    return new EqBand(bool, arrayOfNumber[1].floatValue(), arrayOfNumber[2].floatValue());
  }
  
  private Eq queryEngineEqByChannelIndex(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 64) {
      i = 64;
    } else {
      i = 96;
    }
    int j = 0;
    boolean bool1 = true;
    Number[] arrayOfNumber = new Number[3];
    arrayOfNumber[0] = Integer.valueOf(0);
    arrayOfNumber[1] = Integer.valueOf(0);
    arrayOfNumber[2] = Integer.valueOf(0);
    Object localObject = numberArrayToByteArray(new Number[] { Integer.valueOf(i), Integer.valueOf(paramInt2) });
    byte[] arrayOfByte = numberArrayToByteArray(arrayOfNumber);
    getParameter((byte[])localObject, arrayOfByte);
    byteArrayToNumberArray(arrayOfByte, arrayOfNumber);
    int k = arrayOfNumber[2].intValue();
    boolean bool2;
    if (arrayOfNumber[0].intValue() > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    if (arrayOfNumber[1].intValue() <= 0) {
      bool1 = false;
    }
    localObject = new Eq(bool2, bool1, k);
    for (int i = j; i < k; i++)
    {
      if (paramInt1 == 64) {
        j = 69;
      } else {
        j = 101;
      }
      ((Eq)localObject).setBand(i, queryEngineEqBandByChannelIndex(j, paramInt2, i));
    }
    return localObject;
  }
  
  private Limiter queryEngineLimiterByChannelIndex(int paramInt)
  {
    Number[] arrayOfNumber = new Number[8];
    arrayOfNumber[0] = Integer.valueOf(0);
    arrayOfNumber[1] = Integer.valueOf(0);
    arrayOfNumber[2] = Integer.valueOf(0);
    arrayOfNumber[3] = Float.valueOf(0.0F);
    arrayOfNumber[4] = Float.valueOf(0.0F);
    arrayOfNumber[5] = Float.valueOf(0.0F);
    arrayOfNumber[6] = Float.valueOf(0.0F);
    arrayOfNumber[7] = Float.valueOf(0.0F);
    byte[] arrayOfByte1 = numberArrayToByteArray(new Number[] { Integer.valueOf(112), Integer.valueOf(paramInt) });
    byte[] arrayOfByte2 = numberArrayToByteArray(arrayOfNumber);
    getParameter(arrayOfByte1, arrayOfByte2);
    byteArrayToNumberArray(arrayOfByte2, arrayOfNumber);
    boolean bool1;
    if (arrayOfNumber[0].intValue() > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if (arrayOfNumber[1].intValue() > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    return new Limiter(bool1, bool2, arrayOfNumber[2].intValue(), arrayOfNumber[3].floatValue(), arrayOfNumber[4].floatValue(), arrayOfNumber[5].floatValue(), arrayOfNumber[6].floatValue(), arrayOfNumber[7].floatValue());
  }
  
  private MbcBand queryEngineMbcBandByChannelIndex(int paramInt1, int paramInt2)
  {
    Number[] arrayOfNumber = new Number[11];
    arrayOfNumber[0] = Integer.valueOf(0);
    arrayOfNumber[1] = Float.valueOf(0.0F);
    arrayOfNumber[2] = Float.valueOf(0.0F);
    arrayOfNumber[3] = Float.valueOf(0.0F);
    arrayOfNumber[4] = Float.valueOf(0.0F);
    arrayOfNumber[5] = Float.valueOf(0.0F);
    arrayOfNumber[6] = Float.valueOf(0.0F);
    arrayOfNumber[7] = Float.valueOf(0.0F);
    arrayOfNumber[8] = Float.valueOf(0.0F);
    arrayOfNumber[9] = Float.valueOf(0.0F);
    arrayOfNumber[10] = Float.valueOf(0.0F);
    byte[] arrayOfByte1 = numberArrayToByteArray(new Number[] { Integer.valueOf(85), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    byte[] arrayOfByte2 = numberArrayToByteArray(arrayOfNumber);
    getParameter(arrayOfByte1, arrayOfByte2);
    byteArrayToNumberArray(arrayOfByte2, arrayOfNumber);
    boolean bool;
    if (arrayOfNumber[0].intValue() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return new MbcBand(bool, arrayOfNumber[1].floatValue(), arrayOfNumber[2].floatValue(), arrayOfNumber[3].floatValue(), arrayOfNumber[4].floatValue(), arrayOfNumber[5].floatValue(), arrayOfNumber[6].floatValue(), arrayOfNumber[7].floatValue(), arrayOfNumber[8].floatValue(), arrayOfNumber[9].floatValue(), arrayOfNumber[10].floatValue());
  }
  
  private Mbc queryEngineMbcByChannelIndex(int paramInt)
  {
    int i = 0;
    boolean bool1 = true;
    Number[] arrayOfNumber = new Number[3];
    arrayOfNumber[0] = Integer.valueOf(0);
    arrayOfNumber[1] = Integer.valueOf(0);
    arrayOfNumber[2] = Integer.valueOf(0);
    byte[] arrayOfByte = numberArrayToByteArray(new Number[] { Integer.valueOf(80), Integer.valueOf(paramInt) });
    Object localObject = numberArrayToByteArray(arrayOfNumber);
    getParameter(arrayOfByte, (byte[])localObject);
    byteArrayToNumberArray((byte[])localObject, arrayOfNumber);
    int j = arrayOfNumber[2].intValue();
    boolean bool2;
    if (arrayOfNumber[0].intValue() > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    if (arrayOfNumber[1].intValue() <= 0) {
      bool1 = false;
    }
    localObject = new Mbc(bool2, bool1, j);
    while (i < j)
    {
      ((Mbc)localObject).setBand(i, queryEngineMbcBandByChannelIndex(paramInt, i));
      i++;
    }
    return localObject;
  }
  
  private void setEngineArchitecture(int paramInt1, float paramFloat, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3, boolean paramBoolean3, int paramInt4, boolean paramBoolean4)
  {
    setNumberArray(new Number[] { Integer.valueOf(48) }, new Number[] { Integer.valueOf(paramInt1), Float.valueOf(paramFloat), Integer.valueOf(paramBoolean1), Integer.valueOf(paramInt2), Integer.valueOf(paramBoolean2), Integer.valueOf(paramInt3), Integer.valueOf(paramBoolean3), Integer.valueOf(paramInt4), Integer.valueOf(paramBoolean4) });
  }
  
  private void setNumberArray(Number[] paramArrayOfNumber1, Number[] paramArrayOfNumber2)
  {
    checkStatus(setParameter(numberArrayToByteArray(paramArrayOfNumber1), numberArrayToByteArray(paramArrayOfNumber2)));
  }
  
  private void setTwoFloat(int paramInt1, int paramInt2, float paramFloat)
  {
    byte[] arrayOfByte = floatToByteArray(paramFloat);
    checkStatus(setParameter(new int[] { paramInt1, paramInt2 }, arrayOfByte));
  }
  
  private void updateEffectArchitecture()
  {
    mChannelCount = getChannelCount();
  }
  
  private void updateEngineChannelByChannelIndex(int paramInt, Channel paramChannel)
  {
    setTwoFloat(32, paramInt, paramChannel.getInputGain());
    updateEngineEqByChannelIndex(64, paramInt, paramChannel.getPreEq());
    updateEngineMbcByChannelIndex(paramInt, paramChannel.getMbc());
    updateEngineEqByChannelIndex(96, paramInt, paramChannel.getPostEq());
    updateEngineLimiterByChannelIndex(paramInt, paramChannel.getLimiter());
  }
  
  private void updateEngineEqBandByChannelIndex(int paramInt1, int paramInt2, int paramInt3, EqBand paramEqBand)
  {
    int i = paramEqBand.isEnabled();
    float f1 = paramEqBand.getCutoffFrequency();
    float f2 = paramEqBand.getGain();
    setNumberArray(new Number[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) }, new Number[] { Integer.valueOf(i), Float.valueOf(f1), Float.valueOf(f2) });
  }
  
  private void updateEngineEqByChannelIndex(int paramInt1, int paramInt2, Eq paramEq)
  {
    int i = paramEq.getBandCount();
    int j = 0;
    int k = paramEq.isInUse();
    int m = paramEq.isEnabled();
    setNumberArray(new Number[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }, new Number[] { Integer.valueOf(k), Integer.valueOf(m), Integer.valueOf(i) });
    while (j < i)
    {
      EqBand localEqBand = paramEq.getBand(j);
      int n;
      if (paramInt1 == 64) {
        n = 69;
      } else {
        n = 101;
      }
      updateEngineEqBandByChannelIndex(n, paramInt2, j, localEqBand);
      j++;
    }
  }
  
  private void updateEngineLimiterByChannelIndex(int paramInt, Limiter paramLimiter)
  {
    int i = paramLimiter.isInUse();
    int j = paramLimiter.isEnabled();
    int k = paramLimiter.getLinkGroup();
    float f1 = paramLimiter.getAttackTime();
    float f2 = paramLimiter.getReleaseTime();
    float f3 = paramLimiter.getRatio();
    float f4 = paramLimiter.getThreshold();
    float f5 = paramLimiter.getPostGain();
    setNumberArray(new Number[] { Integer.valueOf(112), Integer.valueOf(paramInt) }, new Number[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k), Float.valueOf(f1), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5) });
  }
  
  private void updateEngineMbcBandByChannelIndex(int paramInt1, int paramInt2, MbcBand paramMbcBand)
  {
    int i = paramMbcBand.isEnabled();
    float f1 = paramMbcBand.getCutoffFrequency();
    float f2 = paramMbcBand.getAttackTime();
    float f3 = paramMbcBand.getReleaseTime();
    float f4 = paramMbcBand.getRatio();
    float f5 = paramMbcBand.getThreshold();
    float f6 = paramMbcBand.getKneeWidth();
    float f7 = paramMbcBand.getNoiseGateThreshold();
    float f8 = paramMbcBand.getExpanderRatio();
    float f9 = paramMbcBand.getPreGain();
    float f10 = paramMbcBand.getPostGain();
    setNumberArray(new Number[] { Integer.valueOf(85), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }, new Number[] { Integer.valueOf(i), Float.valueOf(f1), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5), Float.valueOf(f6), Float.valueOf(f7), Float.valueOf(f8), Float.valueOf(f9), Float.valueOf(f10) });
  }
  
  private void updateEngineMbcByChannelIndex(int paramInt, Mbc paramMbc)
  {
    int i = paramMbc.getBandCount();
    int j = 0;
    int k = paramMbc.isInUse();
    int m = paramMbc.isEnabled();
    setNumberArray(new Number[] { Integer.valueOf(80), Integer.valueOf(paramInt) }, new Number[] { Integer.valueOf(k), Integer.valueOf(m), Integer.valueOf(i) });
    while (j < i)
    {
      updateEngineMbcBandByChannelIndex(paramInt, j, paramMbc.getBand(j));
      j++;
    }
  }
  
  public Channel getChannelByChannelIndex(int paramInt)
  {
    return queryEngineByChannelIndex(paramInt);
  }
  
  public int getChannelCount()
  {
    return getOneInt(16);
  }
  
  public Config getConfig()
  {
    int i = 0;
    Object localObject = new Number[9];
    localObject[0] = Integer.valueOf(0);
    localObject[1] = Float.valueOf(0.0F);
    localObject[2] = Integer.valueOf(0);
    localObject[3] = Integer.valueOf(0);
    localObject[4] = Integer.valueOf(0);
    localObject[5] = Integer.valueOf(0);
    localObject[6] = Integer.valueOf(0);
    localObject[7] = Integer.valueOf(0);
    localObject[8] = Integer.valueOf(0);
    byte[] arrayOfByte1 = numberArrayToByteArray(new Number[] { Integer.valueOf(48) });
    byte[] arrayOfByte2 = numberArrayToByteArray((Number[])localObject);
    getParameter(arrayOfByte1, arrayOfByte2);
    byteArrayToNumberArray(arrayOfByte2, (Number[])localObject);
    int j = localObject[0].intValue();
    int k = mChannelCount;
    boolean bool1;
    if (localObject[2].intValue() > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    int m = localObject[3].intValue();
    boolean bool2;
    if (localObject[4].intValue() > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    int n = localObject[5].intValue();
    boolean bool3;
    if (localObject[6].intValue() > 0) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    int i1 = localObject[7].intValue();
    boolean bool4;
    if (localObject[8].intValue() > 0) {
      bool4 = true;
    } else {
      bool4 = false;
    }
    localObject = new DynamicsProcessing.Config.Builder(j, k, bool1, m, bool2, n, bool3, i1, bool4).setPreferredFrameDuration(localObject[1].floatValue()).build();
    while (i < mChannelCount)
    {
      ((Config)localObject).setChannelTo(i, queryEngineByChannelIndex(i));
      i++;
    }
    return localObject;
  }
  
  public float getInputGainByChannelIndex(int paramInt)
  {
    return getTwoFloat(32, paramInt);
  }
  
  public Limiter getLimiterByChannelIndex(int paramInt)
  {
    return queryEngineLimiterByChannelIndex(paramInt);
  }
  
  public MbcBand getMbcBandByChannelIndex(int paramInt1, int paramInt2)
  {
    return queryEngineMbcBandByChannelIndex(paramInt1, paramInt2);
  }
  
  public Mbc getMbcByChannelIndex(int paramInt)
  {
    return queryEngineMbcByChannelIndex(paramInt);
  }
  
  public EqBand getPostEqBandByChannelIndex(int paramInt1, int paramInt2)
  {
    return queryEngineEqBandByChannelIndex(101, paramInt1, paramInt2);
  }
  
  public Eq getPostEqByChannelIndex(int paramInt)
  {
    return queryEngineEqByChannelIndex(96, paramInt);
  }
  
  public EqBand getPreEqBandByChannelIndex(int paramInt1, int paramInt2)
  {
    return queryEngineEqBandByChannelIndex(69, paramInt1, paramInt2);
  }
  
  public Eq getPreEqByChannelIndex(int paramInt)
  {
    return queryEngineEqByChannelIndex(64, paramInt);
  }
  
  public Settings getProperties()
  {
    Settings localSettings = new Settings();
    channelCount = getChannelCount();
    if (channelCount <= 32)
    {
      inputGain = new float[channelCount];
      for (int i = 0; i < channelCount; i++) {}
      return localSettings;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("too many channels Settings:");
    localStringBuilder.append(localSettings);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setAllChannelsTo(Channel paramChannel)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setChannelTo(i, paramChannel);
    }
  }
  
  public void setChannelTo(int paramInt, Channel paramChannel)
  {
    updateEngineChannelByChannelIndex(paramInt, paramChannel);
  }
  
  public void setInputGainAllChannelsTo(float paramFloat)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setInputGainbyChannel(i, paramFloat);
    }
  }
  
  public void setInputGainbyChannel(int paramInt, float paramFloat)
  {
    setTwoFloat(32, paramInt, paramFloat);
  }
  
  public void setLimiterAllChannelsTo(Limiter paramLimiter)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setLimiterByChannelIndex(i, paramLimiter);
    }
  }
  
  public void setLimiterByChannelIndex(int paramInt, Limiter paramLimiter)
  {
    updateEngineLimiterByChannelIndex(paramInt, paramLimiter);
  }
  
  public void setMbcAllChannelsTo(Mbc paramMbc)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setMbcByChannelIndex(i, paramMbc);
    }
  }
  
  public void setMbcBandAllChannelsTo(int paramInt, MbcBand paramMbcBand)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setMbcBandByChannelIndex(i, paramInt, paramMbcBand);
    }
  }
  
  public void setMbcBandByChannelIndex(int paramInt1, int paramInt2, MbcBand paramMbcBand)
  {
    updateEngineMbcBandByChannelIndex(paramInt1, paramInt2, paramMbcBand);
  }
  
  public void setMbcByChannelIndex(int paramInt, Mbc paramMbc)
  {
    updateEngineMbcByChannelIndex(paramInt, paramMbc);
  }
  
  public void setParameterListener(OnParameterChangeListener paramOnParameterChangeListener)
  {
    synchronized (mParamListenerLock)
    {
      if (mParamListener == null)
      {
        BaseParameterListener localBaseParameterListener = new android/media/audiofx/DynamicsProcessing$BaseParameterListener;
        localBaseParameterListener.<init>(this, null);
        mBaseParamListener = localBaseParameterListener;
        super.setParameterListener(mBaseParamListener);
      }
      mParamListener = paramOnParameterChangeListener;
      return;
    }
  }
  
  public void setPostEqAllChannelsTo(Eq paramEq)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setPostEqByChannelIndex(i, paramEq);
    }
  }
  
  public void setPostEqBandAllChannelsTo(int paramInt, EqBand paramEqBand)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setPostEqBandByChannelIndex(i, paramInt, paramEqBand);
    }
  }
  
  public void setPostEqBandByChannelIndex(int paramInt1, int paramInt2, EqBand paramEqBand)
  {
    updateEngineEqBandByChannelIndex(101, paramInt1, paramInt2, paramEqBand);
  }
  
  public void setPostEqByChannelIndex(int paramInt, Eq paramEq)
  {
    updateEngineEqByChannelIndex(96, paramInt, paramEq);
  }
  
  public void setPreEqAllChannelsTo(Eq paramEq)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setPreEqByChannelIndex(i, paramEq);
    }
  }
  
  public void setPreEqBandAllChannelsTo(int paramInt, EqBand paramEqBand)
  {
    for (int i = 0; i < mChannelCount; i++) {
      setPreEqBandByChannelIndex(i, paramInt, paramEqBand);
    }
  }
  
  public void setPreEqBandByChannelIndex(int paramInt1, int paramInt2, EqBand paramEqBand)
  {
    updateEngineEqBandByChannelIndex(69, paramInt1, paramInt2, paramEqBand);
  }
  
  public void setPreEqByChannelIndex(int paramInt, Eq paramEq)
  {
    updateEngineEqByChannelIndex(64, paramInt, paramEq);
  }
  
  public void setProperties(Settings paramSettings)
  {
    if ((channelCount == inputGain.length) && (channelCount == mChannelCount))
    {
      for (int i = 0; i < mChannelCount; i++) {}
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("settings invalid channel count: ");
    localStringBuilder.append(channelCount);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static class BandBase
  {
    private float mCutoffFrequency;
    private boolean mEnabled;
    
    public BandBase(boolean paramBoolean, float paramFloat)
    {
      mEnabled = paramBoolean;
      mCutoffFrequency = paramFloat;
    }
    
    public float getCutoffFrequency()
    {
      return mCutoffFrequency;
    }
    
    public boolean isEnabled()
    {
      return mEnabled;
    }
    
    public void setCutoffFrequency(float paramFloat)
    {
      mCutoffFrequency = paramFloat;
    }
    
    public void setEnabled(boolean paramBoolean)
    {
      mEnabled = paramBoolean;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(String.format(" Enabled: %b\n", new Object[] { Boolean.valueOf(mEnabled) }));
      localStringBuilder.append(String.format(" CutoffFrequency: %f\n", new Object[] { Float.valueOf(mCutoffFrequency) }));
      return localStringBuilder.toString();
    }
  }
  
  public static class BandStage
    extends DynamicsProcessing.Stage
  {
    private int mBandCount;
    
    public BandStage(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
    {
      super(paramBoolean2);
      if (!isInUse()) {
        paramInt = 0;
      }
      mBandCount = paramInt;
    }
    
    public int getBandCount()
    {
      return mBandCount;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(super.toString());
      if (isInUse()) {
        localStringBuilder.append(String.format(" Band Count: %d\n", new Object[] { Integer.valueOf(mBandCount) }));
      }
      return localStringBuilder.toString();
    }
  }
  
  private class BaseParameterListener
    implements AudioEffect.OnParameterChangeListener
  {
    private BaseParameterListener() {}
    
    public void onParameterChange(AudioEffect paramAudioEffect, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      if (paramInt != 0) {
        return;
      }
      paramAudioEffect = null;
      synchronized (mParamListenerLock)
      {
        if (mParamListener != null) {
          paramAudioEffect = mParamListener;
        }
        if (paramAudioEffect != null)
        {
          paramInt = -1;
          int i = Integer.MIN_VALUE;
          if (paramArrayOfByte1.length == 4) {
            paramInt = AudioEffect.byteArrayToInt(paramArrayOfByte1, 0);
          }
          if (paramArrayOfByte2.length == 4) {
            i = AudioEffect.byteArrayToInt(paramArrayOfByte2, 0);
          }
          if ((paramInt != -1) && (i != Integer.MIN_VALUE)) {
            paramAudioEffect.onParameterChange(DynamicsProcessing.this, paramInt, i);
          }
        }
        return;
      }
    }
  }
  
  public static final class Channel
  {
    private float mInputGain;
    private DynamicsProcessing.Limiter mLimiter;
    private DynamicsProcessing.Mbc mMbc;
    private DynamicsProcessing.Eq mPostEq;
    private DynamicsProcessing.Eq mPreEq;
    
    public Channel(float paramFloat, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2, boolean paramBoolean3, int paramInt3, boolean paramBoolean4)
    {
      mInputGain = paramFloat;
      mPreEq = new DynamicsProcessing.Eq(paramBoolean1, true, paramInt1);
      mMbc = new DynamicsProcessing.Mbc(paramBoolean2, true, paramInt2);
      mPostEq = new DynamicsProcessing.Eq(paramBoolean3, true, paramInt3);
      mLimiter = new DynamicsProcessing.Limiter(paramBoolean4, true, 0, 1.0F, 60.0F, 10.0F, -2.0F, 0.0F);
    }
    
    public Channel(Channel paramChannel)
    {
      mInputGain = mInputGain;
      mPreEq = new DynamicsProcessing.Eq(mPreEq);
      mMbc = new DynamicsProcessing.Mbc(mMbc);
      mPostEq = new DynamicsProcessing.Eq(mPostEq);
      mLimiter = new DynamicsProcessing.Limiter(mLimiter);
    }
    
    public float getInputGain()
    {
      return mInputGain;
    }
    
    public DynamicsProcessing.Limiter getLimiter()
    {
      return mLimiter;
    }
    
    public DynamicsProcessing.Mbc getMbc()
    {
      return mMbc;
    }
    
    public DynamicsProcessing.MbcBand getMbcBand(int paramInt)
    {
      return mMbc.getBand(paramInt);
    }
    
    public DynamicsProcessing.Eq getPostEq()
    {
      return mPostEq;
    }
    
    public DynamicsProcessing.EqBand getPostEqBand(int paramInt)
    {
      return mPostEq.getBand(paramInt);
    }
    
    public DynamicsProcessing.Eq getPreEq()
    {
      return mPreEq;
    }
    
    public DynamicsProcessing.EqBand getPreEqBand(int paramInt)
    {
      return mPreEq.getBand(paramInt);
    }
    
    public void setInputGain(float paramFloat)
    {
      mInputGain = paramFloat;
    }
    
    public void setLimiter(DynamicsProcessing.Limiter paramLimiter)
    {
      mLimiter = new DynamicsProcessing.Limiter(paramLimiter);
    }
    
    public void setMbc(DynamicsProcessing.Mbc paramMbc)
    {
      if (paramMbc.getBandCount() == mMbc.getBandCount())
      {
        mMbc = new DynamicsProcessing.Mbc(paramMbc);
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MbcBandCount changed from ");
      localStringBuilder.append(mMbc.getBandCount());
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramMbc.getBandCount());
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void setMbcBand(int paramInt, DynamicsProcessing.MbcBand paramMbcBand)
    {
      mMbc.setBand(paramInt, paramMbcBand);
    }
    
    public void setPostEq(DynamicsProcessing.Eq paramEq)
    {
      if (paramEq.getBandCount() == mPostEq.getBandCount())
      {
        mPostEq = new DynamicsProcessing.Eq(paramEq);
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PostEqBandCount changed from ");
      localStringBuilder.append(mPostEq.getBandCount());
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramEq.getBandCount());
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void setPostEqBand(int paramInt, DynamicsProcessing.EqBand paramEqBand)
    {
      mPostEq.setBand(paramInt, paramEqBand);
    }
    
    public void setPreEq(DynamicsProcessing.Eq paramEq)
    {
      if (paramEq.getBandCount() == mPreEq.getBandCount())
      {
        mPreEq = new DynamicsProcessing.Eq(paramEq);
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PreEqBandCount changed from ");
      localStringBuilder.append(mPreEq.getBandCount());
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramEq.getBandCount());
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void setPreEqBand(int paramInt, DynamicsProcessing.EqBand paramEqBand)
    {
      mPreEq.setBand(paramInt, paramEqBand);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(String.format(" InputGain: %f\n", new Object[] { Float.valueOf(mInputGain) }));
      localStringBuilder.append("-->PreEq\n");
      localStringBuilder.append(mPreEq.toString());
      localStringBuilder.append("-->MBC\n");
      localStringBuilder.append(mMbc.toString());
      localStringBuilder.append("-->PostEq\n");
      localStringBuilder.append(mPostEq.toString());
      localStringBuilder.append("-->Limiter\n");
      localStringBuilder.append(mLimiter.toString());
      return localStringBuilder.toString();
    }
  }
  
  public static final class Config
  {
    private final DynamicsProcessing.Channel[] mChannel;
    private final int mChannelCount;
    private final boolean mLimiterInUse;
    private final int mMbcBandCount;
    private final boolean mMbcInUse;
    private final int mPostEqBandCount;
    private final boolean mPostEqInUse;
    private final int mPreEqBandCount;
    private final boolean mPreEqInUse;
    private final float mPreferredFrameDuration;
    private final int mVariant;
    
    public Config(int paramInt1, float paramFloat, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2, int paramInt4, boolean paramBoolean3, int paramInt5, boolean paramBoolean4, DynamicsProcessing.Channel[] paramArrayOfChannel)
    {
      mVariant = paramInt1;
      mPreferredFrameDuration = paramFloat;
      mChannelCount = paramInt2;
      mPreEqInUse = paramBoolean1;
      mPreEqBandCount = paramInt3;
      mMbcInUse = paramBoolean2;
      mMbcBandCount = paramInt4;
      mPostEqInUse = paramBoolean3;
      mPostEqBandCount = paramInt5;
      mLimiterInUse = paramBoolean4;
      mChannel = new DynamicsProcessing.Channel[mChannelCount];
      for (paramInt1 = 0; paramInt1 < mChannelCount; paramInt1++) {
        if (paramInt1 < paramArrayOfChannel.length) {
          mChannel[paramInt1] = new DynamicsProcessing.Channel(paramArrayOfChannel[paramInt1]);
        }
      }
    }
    
    public Config(int paramInt, Config paramConfig)
    {
      mVariant = mVariant;
      mPreferredFrameDuration = mPreferredFrameDuration;
      mChannelCount = mChannelCount;
      mPreEqInUse = mPreEqInUse;
      mPreEqBandCount = mPreEqBandCount;
      mMbcInUse = mMbcInUse;
      mMbcBandCount = mMbcBandCount;
      mPostEqInUse = mPostEqInUse;
      mPostEqBandCount = mPostEqBandCount;
      mLimiterInUse = mLimiterInUse;
      if (mChannelCount == mChannel.length)
      {
        if (paramInt >= 1)
        {
          mChannel = new DynamicsProcessing.Channel[paramInt];
          for (int i = 0; i < paramInt; i++) {
            if (i < mChannelCount) {
              mChannel[i] = new DynamicsProcessing.Channel(mChannel[i]);
            } else {
              mChannel[i] = new DynamicsProcessing.Channel(mChannel[(mChannelCount - 1)]);
            }
          }
          return;
        }
        throw new IllegalArgumentException("channel resizing less than 1 not allowed");
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("configuration channel counts differ ");
      localStringBuilder.append(mChannelCount);
      localStringBuilder.append(" !=");
      localStringBuilder.append(mChannel.length);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Config(Config paramConfig)
    {
      this(mChannelCount, paramConfig);
    }
    
    private void checkChannel(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < mChannel.length)) {
        return;
      }
      throw new IllegalArgumentException("ChannelIndex out of bounds");
    }
    
    public DynamicsProcessing.Channel getChannelByChannelIndex(int paramInt)
    {
      checkChannel(paramInt);
      return mChannel[paramInt];
    }
    
    public float getInputGainByChannelIndex(int paramInt)
    {
      checkChannel(paramInt);
      return mChannel[paramInt].getInputGain();
    }
    
    public DynamicsProcessing.Limiter getLimiterByChannelIndex(int paramInt)
    {
      checkChannel(paramInt);
      return mChannel[paramInt].getLimiter();
    }
    
    public DynamicsProcessing.MbcBand getMbcBandByChannelIndex(int paramInt1, int paramInt2)
    {
      checkChannel(paramInt1);
      return mChannel[paramInt1].getMbcBand(paramInt2);
    }
    
    public int getMbcBandCount()
    {
      return mMbcBandCount;
    }
    
    public DynamicsProcessing.Mbc getMbcByChannelIndex(int paramInt)
    {
      checkChannel(paramInt);
      return mChannel[paramInt].getMbc();
    }
    
    public DynamicsProcessing.EqBand getPostEqBandByChannelIndex(int paramInt1, int paramInt2)
    {
      checkChannel(paramInt1);
      return mChannel[paramInt1].getPostEqBand(paramInt2);
    }
    
    public int getPostEqBandCount()
    {
      return mPostEqBandCount;
    }
    
    public DynamicsProcessing.Eq getPostEqByChannelIndex(int paramInt)
    {
      checkChannel(paramInt);
      return mChannel[paramInt].getPostEq();
    }
    
    public DynamicsProcessing.EqBand getPreEqBandByChannelIndex(int paramInt1, int paramInt2)
    {
      checkChannel(paramInt1);
      return mChannel[paramInt1].getPreEqBand(paramInt2);
    }
    
    public int getPreEqBandCount()
    {
      return mPreEqBandCount;
    }
    
    public DynamicsProcessing.Eq getPreEqByChannelIndex(int paramInt)
    {
      checkChannel(paramInt);
      return mChannel[paramInt].getPreEq();
    }
    
    public float getPreferredFrameDuration()
    {
      return mPreferredFrameDuration;
    }
    
    public int getVariant()
    {
      return mVariant;
    }
    
    public boolean isLimiterInUse()
    {
      return mLimiterInUse;
    }
    
    public boolean isMbcInUse()
    {
      return mMbcInUse;
    }
    
    public boolean isPostEqInUse()
    {
      return mPostEqInUse;
    }
    
    public boolean isPreEqInUse()
    {
      return mPreEqInUse;
    }
    
    public void setAllChannelsTo(DynamicsProcessing.Channel paramChannel)
    {
      for (int i = 0; i < mChannel.length; i++) {
        setChannelTo(i, paramChannel);
      }
    }
    
    public void setChannelTo(int paramInt, DynamicsProcessing.Channel paramChannel)
    {
      checkChannel(paramInt);
      if (mMbcBandCount == paramChannel.getMbc().getBandCount())
      {
        if (mPreEqBandCount == paramChannel.getPreEq().getBandCount())
        {
          if (mPostEqBandCount == paramChannel.getPostEq().getBandCount())
          {
            mChannel[paramInt] = new DynamicsProcessing.Channel(paramChannel);
            return;
          }
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("PostEqBandCount changed from ");
          localStringBuilder.append(mPostEqBandCount);
          localStringBuilder.append(" to ");
          localStringBuilder.append(paramChannel.getPostEq().getBandCount());
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("PreEqBandCount changed from ");
        localStringBuilder.append(mPreEqBandCount);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramChannel.getPreEq().getBandCount());
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MbcBandCount changed from ");
      localStringBuilder.append(mMbcBandCount);
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramChannel.getPreEq().getBandCount());
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void setInputGainAllChannelsTo(float paramFloat)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setInputGain(paramFloat);
      }
    }
    
    public void setInputGainByChannelIndex(int paramInt, float paramFloat)
    {
      checkChannel(paramInt);
      mChannel[paramInt].setInputGain(paramFloat);
    }
    
    public void setLimiterAllChannelsTo(DynamicsProcessing.Limiter paramLimiter)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setLimiter(paramLimiter);
      }
    }
    
    public void setLimiterByChannelIndex(int paramInt, DynamicsProcessing.Limiter paramLimiter)
    {
      checkChannel(paramInt);
      mChannel[paramInt].setLimiter(paramLimiter);
    }
    
    public void setMbcAllChannelsTo(DynamicsProcessing.Mbc paramMbc)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setMbc(paramMbc);
      }
    }
    
    public void setMbcBandAllChannelsTo(int paramInt, DynamicsProcessing.MbcBand paramMbcBand)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setMbcBand(paramInt, paramMbcBand);
      }
    }
    
    public void setMbcBandByChannelIndex(int paramInt1, int paramInt2, DynamicsProcessing.MbcBand paramMbcBand)
    {
      checkChannel(paramInt1);
      mChannel[paramInt1].setMbcBand(paramInt2, paramMbcBand);
    }
    
    public void setMbcByChannelIndex(int paramInt, DynamicsProcessing.Mbc paramMbc)
    {
      checkChannel(paramInt);
      mChannel[paramInt].setMbc(paramMbc);
    }
    
    public void setPostEqAllChannelsTo(DynamicsProcessing.Eq paramEq)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setPostEq(paramEq);
      }
    }
    
    public void setPostEqBandAllChannelsTo(int paramInt, DynamicsProcessing.EqBand paramEqBand)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setPostEqBand(paramInt, paramEqBand);
      }
    }
    
    public void setPostEqBandByChannelIndex(int paramInt1, int paramInt2, DynamicsProcessing.EqBand paramEqBand)
    {
      checkChannel(paramInt1);
      mChannel[paramInt1].setPostEqBand(paramInt2, paramEqBand);
    }
    
    public void setPostEqByChannelIndex(int paramInt, DynamicsProcessing.Eq paramEq)
    {
      checkChannel(paramInt);
      mChannel[paramInt].setPostEq(paramEq);
    }
    
    public void setPreEqAllChannelsTo(DynamicsProcessing.Eq paramEq)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setPreEq(paramEq);
      }
    }
    
    public void setPreEqBandAllChannelsTo(int paramInt, DynamicsProcessing.EqBand paramEqBand)
    {
      for (int i = 0; i < mChannel.length; i++) {
        mChannel[i].setPreEqBand(paramInt, paramEqBand);
      }
    }
    
    public void setPreEqBandByChannelIndex(int paramInt1, int paramInt2, DynamicsProcessing.EqBand paramEqBand)
    {
      checkChannel(paramInt1);
      mChannel[paramInt1].setPreEqBand(paramInt2, paramEqBand);
    }
    
    public void setPreEqByChannelIndex(int paramInt, DynamicsProcessing.Eq paramEq)
    {
      checkChannel(paramInt);
      mChannel[paramInt].setPreEq(paramEq);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(String.format("Variant: %d\n", new Object[] { Integer.valueOf(mVariant) }));
      localStringBuilder.append(String.format("PreferredFrameDuration: %f\n", new Object[] { Float.valueOf(mPreferredFrameDuration) }));
      localStringBuilder.append(String.format("ChannelCount: %d\n", new Object[] { Integer.valueOf(mChannelCount) }));
      localStringBuilder.append(String.format("PreEq inUse: %b, bandCount:%d\n", new Object[] { Boolean.valueOf(mPreEqInUse), Integer.valueOf(mPreEqBandCount) }));
      localStringBuilder.append(String.format("Mbc inUse: %b, bandCount: %d\n", new Object[] { Boolean.valueOf(mMbcInUse), Integer.valueOf(mMbcBandCount) }));
      localStringBuilder.append(String.format("PostEq inUse: %b, bandCount: %d\n", new Object[] { Boolean.valueOf(mPostEqInUse), Integer.valueOf(mPostEqBandCount) }));
      localStringBuilder.append(String.format("Limiter inUse: %b\n", new Object[] { Boolean.valueOf(mLimiterInUse) }));
      for (int i = 0; i < mChannel.length; i++)
      {
        localStringBuilder.append(String.format("==Channel %d\n", new Object[] { Integer.valueOf(i) }));
        localStringBuilder.append(mChannel[i].toString());
      }
      return localStringBuilder.toString();
    }
    
    public static final class Builder
    {
      private DynamicsProcessing.Channel[] mChannel;
      private int mChannelCount;
      private boolean mLimiterInUse;
      private int mMbcBandCount;
      private boolean mMbcInUse;
      private int mPostEqBandCount;
      private boolean mPostEqInUse;
      private int mPreEqBandCount;
      private boolean mPreEqInUse;
      private float mPreferredFrameDuration = 10.0F;
      private int mVariant;
      
      public Builder(int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2, int paramInt4, boolean paramBoolean3, int paramInt5, boolean paramBoolean4)
      {
        mVariant = paramInt1;
        mChannelCount = paramInt2;
        mPreEqInUse = paramBoolean1;
        mPreEqBandCount = paramInt3;
        mMbcInUse = paramBoolean2;
        mMbcBandCount = paramInt4;
        mPostEqInUse = paramBoolean3;
        mPostEqBandCount = paramInt5;
        mLimiterInUse = paramBoolean4;
        mChannel = new DynamicsProcessing.Channel[mChannelCount];
        for (paramInt1 = 0; paramInt1 < mChannelCount; paramInt1++) {
          mChannel[paramInt1] = new DynamicsProcessing.Channel(0.0F, mPreEqInUse, mPreEqBandCount, mMbcInUse, mMbcBandCount, mPostEqInUse, mPostEqBandCount, mLimiterInUse);
        }
      }
      
      private void checkChannel(int paramInt)
      {
        if ((paramInt >= 0) && (paramInt < mChannel.length)) {
          return;
        }
        throw new IllegalArgumentException("ChannelIndex out of bounds");
      }
      
      public DynamicsProcessing.Config build()
      {
        return new DynamicsProcessing.Config(mVariant, mPreferredFrameDuration, mChannelCount, mPreEqInUse, mPreEqBandCount, mMbcInUse, mMbcBandCount, mPostEqInUse, mPostEqBandCount, mLimiterInUse, mChannel);
      }
      
      public Builder setAllChannelsTo(DynamicsProcessing.Channel paramChannel)
      {
        for (int i = 0; i < mChannel.length; i++) {
          setChannelTo(i, paramChannel);
        }
        return this;
      }
      
      public Builder setChannelTo(int paramInt, DynamicsProcessing.Channel paramChannel)
      {
        checkChannel(paramInt);
        if (mMbcBandCount == paramChannel.getMbc().getBandCount())
        {
          if (mPreEqBandCount == paramChannel.getPreEq().getBandCount())
          {
            if (mPostEqBandCount == paramChannel.getPostEq().getBandCount())
            {
              mChannel[paramInt] = new DynamicsProcessing.Channel(paramChannel);
              return this;
            }
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("PostEqBandCount changed from ");
            localStringBuilder.append(mPostEqBandCount);
            localStringBuilder.append(" to ");
            localStringBuilder.append(paramChannel.getPostEq().getBandCount());
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("PreEqBandCount changed from ");
          localStringBuilder.append(mPreEqBandCount);
          localStringBuilder.append(" to ");
          localStringBuilder.append(paramChannel.getPreEq().getBandCount());
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("MbcBandCount changed from ");
        localStringBuilder.append(mMbcBandCount);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramChannel.getPreEq().getBandCount());
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      
      public Builder setInputGainAllChannelsTo(float paramFloat)
      {
        for (int i = 0; i < mChannel.length; i++) {
          mChannel[i].setInputGain(paramFloat);
        }
        return this;
      }
      
      public Builder setInputGainByChannelIndex(int paramInt, float paramFloat)
      {
        checkChannel(paramInt);
        mChannel[paramInt].setInputGain(paramFloat);
        return this;
      }
      
      public Builder setLimiterAllChannelsTo(DynamicsProcessing.Limiter paramLimiter)
      {
        for (int i = 0; i < mChannel.length; i++) {
          setLimiterByChannelIndex(i, paramLimiter);
        }
        return this;
      }
      
      public Builder setLimiterByChannelIndex(int paramInt, DynamicsProcessing.Limiter paramLimiter)
      {
        checkChannel(paramInt);
        mChannel[paramInt].setLimiter(paramLimiter);
        return this;
      }
      
      public Builder setMbcAllChannelsTo(DynamicsProcessing.Mbc paramMbc)
      {
        for (int i = 0; i < mChannel.length; i++) {
          setMbcByChannelIndex(i, paramMbc);
        }
        return this;
      }
      
      public Builder setMbcByChannelIndex(int paramInt, DynamicsProcessing.Mbc paramMbc)
      {
        checkChannel(paramInt);
        mChannel[paramInt].setMbc(paramMbc);
        return this;
      }
      
      public Builder setPostEqAllChannelsTo(DynamicsProcessing.Eq paramEq)
      {
        for (int i = 0; i < mChannel.length; i++) {
          setPostEqByChannelIndex(i, paramEq);
        }
        return this;
      }
      
      public Builder setPostEqByChannelIndex(int paramInt, DynamicsProcessing.Eq paramEq)
      {
        checkChannel(paramInt);
        mChannel[paramInt].setPostEq(paramEq);
        return this;
      }
      
      public Builder setPreEqAllChannelsTo(DynamicsProcessing.Eq paramEq)
      {
        for (int i = 0; i < mChannel.length; i++) {
          setPreEqByChannelIndex(i, paramEq);
        }
        return this;
      }
      
      public Builder setPreEqByChannelIndex(int paramInt, DynamicsProcessing.Eq paramEq)
      {
        checkChannel(paramInt);
        mChannel[paramInt].setPreEq(paramEq);
        return this;
      }
      
      public Builder setPreferredFrameDuration(float paramFloat)
      {
        if (paramFloat >= 0.0F)
        {
          mPreferredFrameDuration = paramFloat;
          return this;
        }
        throw new IllegalArgumentException("Expected positive frameDuration");
      }
    }
  }
  
  public static final class Eq
    extends DynamicsProcessing.BandStage
  {
    private final DynamicsProcessing.EqBand[] mBands;
    
    public Eq(Eq paramEq)
    {
      super(paramEq.isEnabled(), paramEq.getBandCount());
      if (isInUse())
      {
        mBands = new DynamicsProcessing.EqBand[mBands.length];
        for (int i = 0; i < mBands.length; i++) {
          mBands[i] = new DynamicsProcessing.EqBand(mBands[i]);
        }
      }
      mBands = null;
    }
    
    public Eq(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
    {
      super(paramBoolean2, paramInt);
      if (isInUse())
      {
        mBands = new DynamicsProcessing.EqBand[paramInt];
        for (int i = 0; i < paramInt; i++)
        {
          float f = 20000.0F;
          if (paramInt > 1) {
            f = (float)Math.pow(10.0D, DynamicsProcessing.mMinFreqLog + i * (DynamicsProcessing.mMaxFreqLog - DynamicsProcessing.mMinFreqLog) / (paramInt - 1));
          }
          mBands[i] = new DynamicsProcessing.EqBand(true, f, 0.0F);
        }
      }
      mBands = null;
    }
    
    private void checkBand(int paramInt)
    {
      if ((mBands != null) && (paramInt >= 0) && (paramInt < mBands.length)) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("band index ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" out of bounds");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public DynamicsProcessing.EqBand getBand(int paramInt)
    {
      checkBand(paramInt);
      return mBands[paramInt];
    }
    
    public void setBand(int paramInt, DynamicsProcessing.EqBand paramEqBand)
    {
      checkBand(paramInt);
      mBands[paramInt] = new DynamicsProcessing.EqBand(paramEqBand);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append(super.toString());
      if (isInUse())
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("--->EqBands: ");
        localStringBuilder2.append(mBands.length);
        localStringBuilder2.append("\n");
        localStringBuilder1.append(localStringBuilder2.toString());
        for (int i = 0; i < mBands.length; i++)
        {
          localStringBuilder1.append(String.format("  Band %d\n", new Object[] { Integer.valueOf(i) }));
          localStringBuilder1.append(mBands[i].toString());
        }
      }
      return localStringBuilder1.toString();
    }
  }
  
  public static final class EqBand
    extends DynamicsProcessing.BandBase
  {
    private float mGain;
    
    public EqBand(EqBand paramEqBand)
    {
      super(paramEqBand.getCutoffFrequency());
      mGain = mGain;
    }
    
    public EqBand(boolean paramBoolean, float paramFloat1, float paramFloat2)
    {
      super(paramFloat1);
      mGain = paramFloat2;
    }
    
    public float getGain()
    {
      return mGain;
    }
    
    public void setGain(float paramFloat)
    {
      mGain = paramFloat;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(super.toString());
      localStringBuilder.append(String.format(" Gain: %f\n", new Object[] { Float.valueOf(mGain) }));
      return localStringBuilder.toString();
    }
  }
  
  public static final class Limiter
    extends DynamicsProcessing.Stage
  {
    private float mAttackTime;
    private int mLinkGroup;
    private float mPostGain;
    private float mRatio;
    private float mReleaseTime;
    private float mThreshold;
    
    public Limiter(Limiter paramLimiter)
    {
      super(paramLimiter.isEnabled());
      mLinkGroup = mLinkGroup;
      mAttackTime = mAttackTime;
      mReleaseTime = mReleaseTime;
      mRatio = mRatio;
      mThreshold = mThreshold;
      mPostGain = mPostGain;
    }
    
    public Limiter(boolean paramBoolean1, boolean paramBoolean2, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
    {
      super(paramBoolean2);
      mLinkGroup = paramInt;
      mAttackTime = paramFloat1;
      mReleaseTime = paramFloat2;
      mRatio = paramFloat3;
      mThreshold = paramFloat4;
      mPostGain = paramFloat5;
    }
    
    public float getAttackTime()
    {
      return mAttackTime;
    }
    
    public int getLinkGroup()
    {
      return mLinkGroup;
    }
    
    public float getPostGain()
    {
      return mPostGain;
    }
    
    public float getRatio()
    {
      return mRatio;
    }
    
    public float getReleaseTime()
    {
      return mReleaseTime;
    }
    
    public float getThreshold()
    {
      return mThreshold;
    }
    
    public void setAttackTime(float paramFloat)
    {
      mAttackTime = paramFloat;
    }
    
    public void setLinkGroup(int paramInt)
    {
      mLinkGroup = paramInt;
    }
    
    public void setPostGain(float paramFloat)
    {
      mPostGain = paramFloat;
    }
    
    public void setRatio(float paramFloat)
    {
      mRatio = paramFloat;
    }
    
    public void setReleaseTime(float paramFloat)
    {
      mReleaseTime = paramFloat;
    }
    
    public void setThreshold(float paramFloat)
    {
      mThreshold = paramFloat;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(super.toString());
      if (isInUse())
      {
        localStringBuilder.append(String.format(" LinkGroup: %d (group)\n", new Object[] { Integer.valueOf(mLinkGroup) }));
        localStringBuilder.append(String.format(" AttackTime: %f (ms)\n", new Object[] { Float.valueOf(mAttackTime) }));
        localStringBuilder.append(String.format(" ReleaseTime: %f (ms)\n", new Object[] { Float.valueOf(mReleaseTime) }));
        localStringBuilder.append(String.format(" Ratio: 1:%f\n", new Object[] { Float.valueOf(mRatio) }));
        localStringBuilder.append(String.format(" Threshold: %f (dB)\n", new Object[] { Float.valueOf(mThreshold) }));
        localStringBuilder.append(String.format(" PostGain: %f (dB)\n", new Object[] { Float.valueOf(mPostGain) }));
      }
      return localStringBuilder.toString();
    }
  }
  
  public static final class Mbc
    extends DynamicsProcessing.BandStage
  {
    private final DynamicsProcessing.MbcBand[] mBands;
    
    public Mbc(Mbc paramMbc)
    {
      super(paramMbc.isEnabled(), paramMbc.getBandCount());
      if (isInUse())
      {
        mBands = new DynamicsProcessing.MbcBand[mBands.length];
        for (int i = 0; i < mBands.length; i++) {
          mBands[i] = new DynamicsProcessing.MbcBand(mBands[i]);
        }
      }
      mBands = null;
    }
    
    public Mbc(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
    {
      super(paramBoolean2, paramInt);
      if (isInUse())
      {
        mBands = new DynamicsProcessing.MbcBand[paramInt];
        for (int i = 0; i < paramInt; i++)
        {
          float f = 20000.0F;
          if (paramInt > 1) {
            f = (float)Math.pow(10.0D, DynamicsProcessing.mMinFreqLog + i * (DynamicsProcessing.mMaxFreqLog - DynamicsProcessing.mMinFreqLog) / (paramInt - 1));
          }
          mBands[i] = new DynamicsProcessing.MbcBand(true, f, 3.0F, 80.0F, 1.0F, -45.0F, 0.0F, -90.0F, 1.0F, 0.0F, 0.0F);
        }
      }
      mBands = null;
    }
    
    private void checkBand(int paramInt)
    {
      if ((mBands != null) && (paramInt >= 0) && (paramInt < mBands.length)) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("band index ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" out of bounds");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public DynamicsProcessing.MbcBand getBand(int paramInt)
    {
      checkBand(paramInt);
      return mBands[paramInt];
    }
    
    public void setBand(int paramInt, DynamicsProcessing.MbcBand paramMbcBand)
    {
      checkBand(paramInt);
      mBands[paramInt] = new DynamicsProcessing.MbcBand(paramMbcBand);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append(super.toString());
      if (isInUse())
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("--->MbcBands: ");
        localStringBuilder2.append(mBands.length);
        localStringBuilder2.append("\n");
        localStringBuilder1.append(localStringBuilder2.toString());
        for (int i = 0; i < mBands.length; i++)
        {
          localStringBuilder1.append(String.format("  Band %d\n", new Object[] { Integer.valueOf(i) }));
          localStringBuilder1.append(mBands[i].toString());
        }
      }
      return localStringBuilder1.toString();
    }
  }
  
  public static final class MbcBand
    extends DynamicsProcessing.BandBase
  {
    private float mAttackTime;
    private float mExpanderRatio;
    private float mKneeWidth;
    private float mNoiseGateThreshold;
    private float mPostGain;
    private float mPreGain;
    private float mRatio;
    private float mReleaseTime;
    private float mThreshold;
    
    public MbcBand(MbcBand paramMbcBand)
    {
      super(paramMbcBand.getCutoffFrequency());
      mAttackTime = mAttackTime;
      mReleaseTime = mReleaseTime;
      mRatio = mRatio;
      mThreshold = mThreshold;
      mKneeWidth = mKneeWidth;
      mNoiseGateThreshold = mNoiseGateThreshold;
      mExpanderRatio = mExpanderRatio;
      mPreGain = mPreGain;
      mPostGain = mPostGain;
    }
    
    public MbcBand(boolean paramBoolean, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10)
    {
      super(paramFloat1);
      mAttackTime = paramFloat2;
      mReleaseTime = paramFloat3;
      mRatio = paramFloat4;
      mThreshold = paramFloat5;
      mKneeWidth = paramFloat6;
      mNoiseGateThreshold = paramFloat7;
      mExpanderRatio = paramFloat8;
      mPreGain = paramFloat9;
      mPostGain = paramFloat10;
    }
    
    public float getAttackTime()
    {
      return mAttackTime;
    }
    
    public float getExpanderRatio()
    {
      return mExpanderRatio;
    }
    
    public float getKneeWidth()
    {
      return mKneeWidth;
    }
    
    public float getNoiseGateThreshold()
    {
      return mNoiseGateThreshold;
    }
    
    public float getPostGain()
    {
      return mPostGain;
    }
    
    public float getPreGain()
    {
      return mPreGain;
    }
    
    public float getRatio()
    {
      return mRatio;
    }
    
    public float getReleaseTime()
    {
      return mReleaseTime;
    }
    
    public float getThreshold()
    {
      return mThreshold;
    }
    
    public void setAttackTime(float paramFloat)
    {
      mAttackTime = paramFloat;
    }
    
    public void setExpanderRatio(float paramFloat)
    {
      mExpanderRatio = paramFloat;
    }
    
    public void setKneeWidth(float paramFloat)
    {
      mKneeWidth = paramFloat;
    }
    
    public void setNoiseGateThreshold(float paramFloat)
    {
      mNoiseGateThreshold = paramFloat;
    }
    
    public void setPostGain(float paramFloat)
    {
      mPostGain = paramFloat;
    }
    
    public void setPreGain(float paramFloat)
    {
      mPreGain = paramFloat;
    }
    
    public void setRatio(float paramFloat)
    {
      mRatio = paramFloat;
    }
    
    public void setReleaseTime(float paramFloat)
    {
      mReleaseTime = paramFloat;
    }
    
    public void setThreshold(float paramFloat)
    {
      mThreshold = paramFloat;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(super.toString());
      localStringBuilder.append(String.format(" AttackTime: %f (ms)\n", new Object[] { Float.valueOf(mAttackTime) }));
      localStringBuilder.append(String.format(" ReleaseTime: %f (ms)\n", new Object[] { Float.valueOf(mReleaseTime) }));
      localStringBuilder.append(String.format(" Ratio: 1:%f\n", new Object[] { Float.valueOf(mRatio) }));
      localStringBuilder.append(String.format(" Threshold: %f (dB)\n", new Object[] { Float.valueOf(mThreshold) }));
      localStringBuilder.append(String.format(" NoiseGateThreshold: %f(dB)\n", new Object[] { Float.valueOf(mNoiseGateThreshold) }));
      localStringBuilder.append(String.format(" ExpanderRatio: %f:1\n", new Object[] { Float.valueOf(mExpanderRatio) }));
      localStringBuilder.append(String.format(" PreGain: %f (dB)\n", new Object[] { Float.valueOf(mPreGain) }));
      localStringBuilder.append(String.format(" PostGain: %f (dB)\n", new Object[] { Float.valueOf(mPostGain) }));
      return localStringBuilder.toString();
    }
  }
  
  public static abstract interface OnParameterChangeListener
  {
    public abstract void onParameterChange(DynamicsProcessing paramDynamicsProcessing, int paramInt1, int paramInt2);
  }
  
  public static class Settings
  {
    public int channelCount;
    public float[] inputGain;
    
    public Settings() {}
    
    public Settings(String paramString)
    {
      Object localObject1 = new StringTokenizer(paramString, "=;");
      if (((StringTokenizer)localObject1).countTokens() == 3)
      {
        localObject2 = ((StringTokenizer)localObject1).nextToken();
        if (((String)localObject2).equals("DynamicsProcessing")) {
          try
          {
            Object localObject3 = ((StringTokenizer)localObject1).nextToken();
            localObject2 = localObject3;
            if (((String)localObject3).equals("channelCount"))
            {
              localObject2 = localObject3;
              channelCount = Short.parseShort(((StringTokenizer)localObject1).nextToken());
              localObject2 = localObject3;
              if (channelCount <= 32)
              {
                localObject2 = localObject3;
                if (((StringTokenizer)localObject1).countTokens() == channelCount * 1)
                {
                  localObject2 = localObject3;
                  inputGain = new float[channelCount];
                  int i = 0;
                  paramString = (String)localObject3;
                  for (;;)
                  {
                    localObject2 = paramString;
                    if (i >= channelCount) {
                      break label257;
                    }
                    localObject2 = paramString;
                    paramString = ((StringTokenizer)localObject1).nextToken();
                    localObject2 = paramString;
                    localObject3 = new java/lang/StringBuilder;
                    localObject2 = paramString;
                    ((StringBuilder)localObject3).<init>();
                    localObject2 = paramString;
                    ((StringBuilder)localObject3).append(i);
                    localObject2 = paramString;
                    ((StringBuilder)localObject3).append("_inputGain");
                    localObject2 = paramString;
                    if (!paramString.equals(((StringBuilder)localObject3).toString())) {
                      break;
                    }
                    localObject2 = paramString;
                    inputGain[i] = Float.parseFloat(((StringTokenizer)localObject1).nextToken());
                    i++;
                  }
                  localObject2 = paramString;
                  localObject3 = new java/lang/IllegalArgumentException;
                  localObject2 = paramString;
                  localObject1 = new java/lang/StringBuilder;
                  localObject2 = paramString;
                  ((StringBuilder)localObject1).<init>();
                  localObject2 = paramString;
                  ((StringBuilder)localObject1).append("invalid key name: ");
                  localObject2 = paramString;
                  ((StringBuilder)localObject1).append(paramString);
                  localObject2 = paramString;
                  ((IllegalArgumentException)localObject3).<init>(((StringBuilder)localObject1).toString());
                  localObject2 = paramString;
                  throw ((Throwable)localObject3);
                  label257:
                  return;
                }
                localObject2 = localObject3;
                localObject1 = new java/lang/IllegalArgumentException;
                localObject2 = localObject3;
                localObject4 = new java/lang/StringBuilder;
                localObject2 = localObject3;
                ((StringBuilder)localObject4).<init>();
                localObject2 = localObject3;
                ((StringBuilder)localObject4).append("settings: ");
                localObject2 = localObject3;
                ((StringBuilder)localObject4).append(paramString);
                localObject2 = localObject3;
                ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject4).toString());
                localObject2 = localObject3;
                throw ((Throwable)localObject1);
              }
              localObject2 = localObject3;
              Object localObject4 = new java/lang/IllegalArgumentException;
              localObject2 = localObject3;
              localObject1 = new java/lang/StringBuilder;
              localObject2 = localObject3;
              ((StringBuilder)localObject1).<init>();
              localObject2 = localObject3;
              ((StringBuilder)localObject1).append("too many channels Settings:");
              localObject2 = localObject3;
              ((StringBuilder)localObject1).append(paramString);
              localObject2 = localObject3;
              ((IllegalArgumentException)localObject4).<init>(((StringBuilder)localObject1).toString());
              localObject2 = localObject3;
              throw ((Throwable)localObject4);
            }
            localObject2 = localObject3;
            paramString = new java/lang/IllegalArgumentException;
            localObject2 = localObject3;
            localObject1 = new java/lang/StringBuilder;
            localObject2 = localObject3;
            ((StringBuilder)localObject1).<init>();
            localObject2 = localObject3;
            ((StringBuilder)localObject1).append("invalid key name: ");
            localObject2 = localObject3;
            ((StringBuilder)localObject1).append((String)localObject3);
            localObject2 = localObject3;
            paramString.<init>(((StringBuilder)localObject1).toString());
            localObject2 = localObject3;
            throw paramString;
          }
          catch (NumberFormatException paramString)
          {
            paramString = new StringBuilder();
            paramString.append("invalid value for key: ");
            paramString.append((String)localObject2);
            throw new IllegalArgumentException(paramString.toString());
          }
        }
        paramString = new StringBuilder();
        paramString.append("invalid settings for DynamicsProcessing: ");
        paramString.append((String)localObject2);
        throw new IllegalArgumentException(paramString.toString());
      }
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("settings: ");
      ((StringBuilder)localObject2).append(paramString);
      throw new IllegalArgumentException(((StringBuilder)localObject2).toString());
    }
    
    public String toString()
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("DynamicsProcessing;channelCount=");
      ((StringBuilder)localObject).append(Integer.toString(channelCount));
      localObject = new String(((StringBuilder)localObject).toString());
      for (int i = 0; i < channelCount; i++)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(";");
        localStringBuilder.append(i);
        localStringBuilder.append("_inputGain=");
        localStringBuilder.append(Float.toString(inputGain[i]));
        localObject = ((String)localObject).concat(localStringBuilder.toString());
      }
      return localObject;
    }
  }
  
  public static class Stage
  {
    private boolean mEnabled;
    private boolean mInUse;
    
    public Stage(boolean paramBoolean1, boolean paramBoolean2)
    {
      mInUse = paramBoolean1;
      mEnabled = paramBoolean2;
    }
    
    public boolean isEnabled()
    {
      return mEnabled;
    }
    
    public boolean isInUse()
    {
      return mInUse;
    }
    
    public void setEnabled(boolean paramBoolean)
    {
      mEnabled = paramBoolean;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(String.format(" Stage InUse: %b\n", new Object[] { Boolean.valueOf(isInUse()) }));
      if (isInUse()) {
        localStringBuilder.append(String.format(" Stage Enabled: %b\n", new Object[] { Boolean.valueOf(mEnabled) }));
      }
      return localStringBuilder.toString();
    }
  }
}
