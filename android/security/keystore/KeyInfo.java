package android.security.keystore;

import java.security.spec.KeySpec;
import java.util.Date;

public class KeyInfo
  implements KeySpec
{
  private final String[] mBlockModes;
  private final String[] mDigests;
  private final String[] mEncryptionPaddings;
  private final boolean mInsideSecureHardware;
  private final boolean mInvalidatedByBiometricEnrollment;
  private final int mKeySize;
  private final Date mKeyValidityForConsumptionEnd;
  private final Date mKeyValidityForOriginationEnd;
  private final Date mKeyValidityStart;
  private final String mKeystoreAlias;
  private final int mOrigin;
  private final int mPurposes;
  private final String[] mSignaturePaddings;
  private final boolean mTrustedUserPresenceRequired;
  private final boolean mUserAuthenticationRequired;
  private final boolean mUserAuthenticationRequirementEnforcedBySecureHardware;
  private final boolean mUserAuthenticationValidWhileOnBody;
  private final int mUserAuthenticationValidityDurationSeconds;
  private final boolean mUserConfirmationRequired;
  
  public KeyInfo(String paramString, boolean paramBoolean1, int paramInt1, int paramInt2, Date paramDate1, Date paramDate2, Date paramDate3, int paramInt3, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4, boolean paramBoolean2, int paramInt4, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7)
  {
    mKeystoreAlias = paramString;
    mInsideSecureHardware = paramBoolean1;
    mOrigin = paramInt1;
    mKeySize = paramInt2;
    mKeyValidityStart = Utils.cloneIfNotNull(paramDate1);
    mKeyValidityForOriginationEnd = Utils.cloneIfNotNull(paramDate2);
    mKeyValidityForConsumptionEnd = Utils.cloneIfNotNull(paramDate3);
    mPurposes = paramInt3;
    mEncryptionPaddings = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(paramArrayOfString1));
    mSignaturePaddings = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(paramArrayOfString2));
    mDigests = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(paramArrayOfString3));
    mBlockModes = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(paramArrayOfString4));
    mUserAuthenticationRequired = paramBoolean2;
    mUserAuthenticationValidityDurationSeconds = paramInt4;
    mUserAuthenticationRequirementEnforcedBySecureHardware = paramBoolean3;
    mUserAuthenticationValidWhileOnBody = paramBoolean4;
    mTrustedUserPresenceRequired = paramBoolean5;
    mInvalidatedByBiometricEnrollment = paramBoolean6;
    mUserConfirmationRequired = paramBoolean7;
  }
  
  public String[] getBlockModes()
  {
    return ArrayUtils.cloneIfNotEmpty(mBlockModes);
  }
  
  public String[] getDigests()
  {
    return ArrayUtils.cloneIfNotEmpty(mDigests);
  }
  
  public String[] getEncryptionPaddings()
  {
    return ArrayUtils.cloneIfNotEmpty(mEncryptionPaddings);
  }
  
  public int getKeySize()
  {
    return mKeySize;
  }
  
  public Date getKeyValidityForConsumptionEnd()
  {
    return Utils.cloneIfNotNull(mKeyValidityForConsumptionEnd);
  }
  
  public Date getKeyValidityForOriginationEnd()
  {
    return Utils.cloneIfNotNull(mKeyValidityForOriginationEnd);
  }
  
  public Date getKeyValidityStart()
  {
    return Utils.cloneIfNotNull(mKeyValidityStart);
  }
  
  public String getKeystoreAlias()
  {
    return mKeystoreAlias;
  }
  
  public int getOrigin()
  {
    return mOrigin;
  }
  
  public int getPurposes()
  {
    return mPurposes;
  }
  
  public String[] getSignaturePaddings()
  {
    return ArrayUtils.cloneIfNotEmpty(mSignaturePaddings);
  }
  
  public int getUserAuthenticationValidityDurationSeconds()
  {
    return mUserAuthenticationValidityDurationSeconds;
  }
  
  public boolean isInsideSecureHardware()
  {
    return mInsideSecureHardware;
  }
  
  public boolean isInvalidatedByBiometricEnrollment()
  {
    return mInvalidatedByBiometricEnrollment;
  }
  
  public boolean isTrustedUserPresenceRequired()
  {
    return mTrustedUserPresenceRequired;
  }
  
  public boolean isUserAuthenticationRequired()
  {
    return mUserAuthenticationRequired;
  }
  
  public boolean isUserAuthenticationRequirementEnforcedBySecureHardware()
  {
    return mUserAuthenticationRequirementEnforcedBySecureHardware;
  }
  
  public boolean isUserAuthenticationValidWhileOnBody()
  {
    return mUserAuthenticationValidWhileOnBody;
  }
  
  public boolean isUserConfirmationRequired()
  {
    return mUserConfirmationRequired;
  }
}
