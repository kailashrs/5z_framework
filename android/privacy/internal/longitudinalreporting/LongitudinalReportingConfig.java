package android.privacy.internal.longitudinalreporting;

import android.privacy.DifferentialPrivacyConfig;
import android.privacy.internal.rappor.RapporConfig;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;

public class LongitudinalReportingConfig
  implements DifferentialPrivacyConfig
{
  private static final String ALGORITHM_NAME = "LongitudinalReporting";
  private final String mEncoderId;
  private final RapporConfig mIRRConfig;
  private final double mProbabilityF;
  private final double mProbabilityP;
  private final double mProbabilityQ;
  
  public LongitudinalReportingConfig(String paramString, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    boolean bool1 = false;
    if ((paramDouble1 >= 0.0D) && (paramDouble1 <= 1.0D)) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "probabilityF must be in range [0.0, 1.0]");
    mProbabilityF = paramDouble1;
    if ((paramDouble2 >= 0.0D) && (paramDouble2 <= 1.0D)) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "probabilityP must be in range [0.0, 1.0]");
    mProbabilityP = paramDouble2;
    boolean bool2 = bool1;
    if (paramDouble3 >= 0.0D)
    {
      bool2 = bool1;
      if (paramDouble3 <= 1.0D) {
        bool2 = true;
      }
    }
    Preconditions.checkArgument(bool2, "probabilityQ must be in range [0.0, 1.0]");
    mProbabilityQ = paramDouble3;
    Preconditions.checkArgument(TextUtils.isEmpty(paramString) ^ true, "encoderId cannot be empty");
    mEncoderId = paramString;
    mIRRConfig = new RapporConfig(paramString, 1, 0.0D, paramDouble1, 1.0D - paramDouble1, 1, 1);
  }
  
  public String getAlgorithm()
  {
    return "LongitudinalReporting";
  }
  
  String getEncoderId()
  {
    return mEncoderId;
  }
  
  RapporConfig getIRRConfig()
  {
    return mIRRConfig;
  }
  
  double getProbabilityP()
  {
    return mProbabilityP;
  }
  
  double getProbabilityQ()
  {
    return mProbabilityQ;
  }
  
  public String toString()
  {
    return String.format("EncoderId: %s, ProbabilityF: %.3f, ProbabilityP: %.3f, ProbabilityQ: %.3f", new Object[] { mEncoderId, Double.valueOf(mProbabilityF), Double.valueOf(mProbabilityP), Double.valueOf(mProbabilityQ) });
  }
}
