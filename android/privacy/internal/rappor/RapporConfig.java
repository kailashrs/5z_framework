package android.privacy.internal.rappor;

import android.privacy.DifferentialPrivacyConfig;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;

public class RapporConfig
  implements DifferentialPrivacyConfig
{
  private static final String ALGORITHM_NAME = "Rappor";
  final String mEncoderId;
  final int mNumBits;
  final int mNumBloomHashes;
  final int mNumCohorts;
  final double mProbabilityF;
  final double mProbabilityP;
  final double mProbabilityQ;
  
  public RapporConfig(String paramString, int paramInt1, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt2, int paramInt3)
  {
    Preconditions.checkArgument(TextUtils.isEmpty(paramString) ^ true, "encoderId cannot be empty");
    mEncoderId = paramString;
    boolean bool1 = false;
    if (paramInt1 > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "numBits needs to be > 0");
    mNumBits = paramInt1;
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
    if ((paramDouble3 >= 0.0D) && (paramDouble3 <= 1.0D)) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "probabilityQ must be in range [0.0, 1.0]");
    mProbabilityQ = paramDouble3;
    if (paramInt2 > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2, "numCohorts needs to be > 0");
    mNumCohorts = paramInt2;
    boolean bool2 = bool1;
    if (paramInt3 > 0) {
      bool2 = true;
    }
    Preconditions.checkArgument(bool2, "numBloomHashes needs to be > 0");
    mNumBloomHashes = paramInt3;
  }
  
  public String getAlgorithm()
  {
    return "Rappor";
  }
  
  public String toString()
  {
    return String.format("EncoderId: %s, NumBits: %d, ProbabilityF: %.3f, ProbabilityP: %.3f, ProbabilityQ: %.3f, NumCohorts: %d, NumBloomHashes: %d", new Object[] { mEncoderId, Integer.valueOf(mNumBits), Double.valueOf(mProbabilityF), Double.valueOf(mProbabilityP), Double.valueOf(mProbabilityQ), Integer.valueOf(mNumCohorts), Integer.valueOf(mNumBloomHashes) });
  }
}
