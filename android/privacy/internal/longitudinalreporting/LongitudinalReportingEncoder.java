package android.privacy.internal.longitudinalreporting;

import android.privacy.DifferentialPrivacyEncoder;
import android.privacy.internal.rappor.RapporConfig;
import android.privacy.internal.rappor.RapporEncoder;
import com.android.internal.annotations.VisibleForTesting;

public class LongitudinalReportingEncoder
  implements DifferentialPrivacyEncoder
{
  private static final boolean DEBUG = false;
  private static final String PRR1_ENCODER_ID = "prr1_encoder_id";
  private static final String PRR2_ENCODER_ID = "prr2_encoder_id";
  private static final String TAG = "LongitudinalEncoder";
  private final LongitudinalReportingConfig mConfig;
  private final Boolean mFakeValue;
  private final RapporEncoder mIRREncoder;
  private final boolean mIsSecure;
  
  private LongitudinalReportingEncoder(LongitudinalReportingConfig paramLongitudinalReportingConfig, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    mConfig = paramLongitudinalReportingConfig;
    mIsSecure = paramBoolean;
    double d = paramLongitudinalReportingConfig.getProbabilityP();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramLongitudinalReportingConfig.getEncoderId());
    localStringBuilder.append("prr1_encoder_id");
    if (getLongTermRandomizedResult(d, paramBoolean, paramArrayOfByte, localStringBuilder.toString()))
    {
      d = paramLongitudinalReportingConfig.getProbabilityQ();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramLongitudinalReportingConfig.getEncoderId());
      localStringBuilder.append("prr2_encoder_id");
      mFakeValue = Boolean.valueOf(getLongTermRandomizedResult(d, paramBoolean, paramArrayOfByte, localStringBuilder.toString()));
    }
    else
    {
      mFakeValue = null;
    }
    paramLongitudinalReportingConfig = paramLongitudinalReportingConfig.getIRRConfig();
    if (paramBoolean) {
      paramLongitudinalReportingConfig = RapporEncoder.createEncoder(paramLongitudinalReportingConfig, paramArrayOfByte);
    } else {
      paramLongitudinalReportingConfig = RapporEncoder.createInsecureEncoderForTest(paramLongitudinalReportingConfig);
    }
    mIRREncoder = paramLongitudinalReportingConfig;
  }
  
  public static LongitudinalReportingEncoder createEncoder(LongitudinalReportingConfig paramLongitudinalReportingConfig, byte[] paramArrayOfByte)
  {
    return new LongitudinalReportingEncoder(paramLongitudinalReportingConfig, true, paramArrayOfByte);
  }
  
  @VisibleForTesting
  public static LongitudinalReportingEncoder createInsecureEncoderForTest(LongitudinalReportingConfig paramLongitudinalReportingConfig)
  {
    return new LongitudinalReportingEncoder(paramLongitudinalReportingConfig, false, null);
  }
  
  @VisibleForTesting
  public static boolean getLongTermRandomizedResult(double paramDouble, boolean paramBoolean, byte[] paramArrayOfByte, String paramString)
  {
    if (paramDouble < 0.5D) {}
    for (double d = paramDouble * 2.0D;; d = (1.0D - paramDouble) * 2.0D) {
      break;
    }
    boolean bool1 = true;
    boolean bool2;
    if (paramDouble < 0.5D) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    paramString = new RapporConfig(paramString, 1, d, 0.0D, 1.0D, 1, 1);
    if (paramBoolean) {
      paramArrayOfByte = RapporEncoder.createEncoder(paramString, paramArrayOfByte);
    } else {
      paramArrayOfByte = RapporEncoder.createInsecureEncoderForTest(paramString);
    }
    if (paramArrayOfByte.encodeBoolean(bool2)[0] > 0) {
      paramBoolean = bool1;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  public byte[] encodeBits(byte[] paramArrayOfByte)
  {
    throw new UnsupportedOperationException();
  }
  
  public byte[] encodeBoolean(boolean paramBoolean)
  {
    if (mFakeValue != null) {
      paramBoolean = mFakeValue.booleanValue();
    }
    return mIRREncoder.encodeBoolean(paramBoolean);
  }
  
  public byte[] encodeString(String paramString)
  {
    throw new UnsupportedOperationException();
  }
  
  public LongitudinalReportingConfig getConfig()
  {
    return mConfig;
  }
  
  public boolean isInsecureEncoderForTest()
  {
    return mIsSecure ^ true;
  }
}
