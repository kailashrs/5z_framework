package android.security.keystore;

import java.security.KeyStore.ProtectionParameter;
import java.util.Date;

public final class KeyProtection
  implements KeyStore.ProtectionParameter, UserAuthArgs
{
  private final String[] mBlockModes;
  private final long mBoundToSecureUserId;
  private final boolean mCriticalToDeviceEncryption;
  private final String[] mDigests;
  private final String[] mEncryptionPaddings;
  private final boolean mInvalidatedByBiometricEnrollment;
  private final boolean mIsStrongBoxBacked;
  private final Date mKeyValidityForConsumptionEnd;
  private final Date mKeyValidityForOriginationEnd;
  private final Date mKeyValidityStart;
  private final int mPurposes;
  private final boolean mRandomizedEncryptionRequired;
  private final String[] mSignaturePaddings;
  private final boolean mUnlockedDeviceRequired;
  private final boolean mUserAuthenticationRequired;
  private final boolean mUserAuthenticationValidWhileOnBody;
  private final int mUserAuthenticationValidityDurationSeconds;
  private final boolean mUserConfirmationRequired;
  private final boolean mUserPresenceRequred;
  
  private KeyProtection(Date paramDate1, Date paramDate2, Date paramDate3, int paramInt1, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, long paramLong, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, boolean paramBoolean9)
  {
    mKeyValidityStart = Utils.cloneIfNotNull(paramDate1);
    mKeyValidityForOriginationEnd = Utils.cloneIfNotNull(paramDate2);
    mKeyValidityForConsumptionEnd = Utils.cloneIfNotNull(paramDate3);
    mPurposes = paramInt1;
    mEncryptionPaddings = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(paramArrayOfString1));
    mSignaturePaddings = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(paramArrayOfString2));
    mDigests = ArrayUtils.cloneIfNotEmpty(paramArrayOfString3);
    mBlockModes = ArrayUtils.cloneIfNotEmpty(ArrayUtils.nullToEmpty(paramArrayOfString4));
    mRandomizedEncryptionRequired = paramBoolean1;
    mUserAuthenticationRequired = paramBoolean2;
    mUserAuthenticationValidityDurationSeconds = paramInt2;
    mUserPresenceRequred = paramBoolean3;
    mUserAuthenticationValidWhileOnBody = paramBoolean4;
    mInvalidatedByBiometricEnrollment = paramBoolean5;
    mBoundToSecureUserId = paramLong;
    mCriticalToDeviceEncryption = paramBoolean6;
    mUserConfirmationRequired = paramBoolean7;
    mUnlockedDeviceRequired = paramBoolean8;
    mIsStrongBoxBacked = paramBoolean9;
  }
  
  public String[] getBlockModes()
  {
    return ArrayUtils.cloneIfNotEmpty(mBlockModes);
  }
  
  public long getBoundToSpecificSecureUserId()
  {
    return mBoundToSecureUserId;
  }
  
  public String[] getDigests()
  {
    if (mDigests != null) {
      return ArrayUtils.cloneIfNotEmpty(mDigests);
    }
    throw new IllegalStateException("Digests not specified");
  }
  
  public String[] getEncryptionPaddings()
  {
    return ArrayUtils.cloneIfNotEmpty(mEncryptionPaddings);
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
  
  public boolean isCriticalToDeviceEncryption()
  {
    return mCriticalToDeviceEncryption;
  }
  
  public boolean isDigestsSpecified()
  {
    boolean bool;
    if (mDigests != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInvalidatedByBiometricEnrollment()
  {
    return mInvalidatedByBiometricEnrollment;
  }
  
  public boolean isRandomizedEncryptionRequired()
  {
    return mRandomizedEncryptionRequired;
  }
  
  public boolean isStrongBoxBacked()
  {
    return mIsStrongBoxBacked;
  }
  
  public boolean isUnlockedDeviceRequired()
  {
    return mUnlockedDeviceRequired;
  }
  
  public boolean isUserAuthenticationRequired()
  {
    return mUserAuthenticationRequired;
  }
  
  public boolean isUserAuthenticationValidWhileOnBody()
  {
    return mUserAuthenticationValidWhileOnBody;
  }
  
  public boolean isUserConfirmationRequired()
  {
    return mUserConfirmationRequired;
  }
  
  public boolean isUserPresenceRequired()
  {
    return mUserPresenceRequred;
  }
  
  public static final class Builder
  {
    private String[] mBlockModes;
    private long mBoundToSecureUserId = 0L;
    private boolean mCriticalToDeviceEncryption = false;
    private String[] mDigests;
    private String[] mEncryptionPaddings;
    private boolean mInvalidatedByBiometricEnrollment = true;
    private boolean mIsStrongBoxBacked = false;
    private Date mKeyValidityForConsumptionEnd;
    private Date mKeyValidityForOriginationEnd;
    private Date mKeyValidityStart;
    private int mPurposes;
    private boolean mRandomizedEncryptionRequired = true;
    private String[] mSignaturePaddings;
    private boolean mUnlockedDeviceRequired = false;
    private boolean mUserAuthenticationRequired;
    private boolean mUserAuthenticationValidWhileOnBody;
    private int mUserAuthenticationValidityDurationSeconds = -1;
    private boolean mUserConfirmationRequired;
    private boolean mUserPresenceRequired = false;
    
    public Builder(int paramInt)
    {
      mPurposes = paramInt;
    }
    
    public KeyProtection build()
    {
      return new KeyProtection(mKeyValidityStart, mKeyValidityForOriginationEnd, mKeyValidityForConsumptionEnd, mPurposes, mEncryptionPaddings, mSignaturePaddings, mDigests, mBlockModes, mRandomizedEncryptionRequired, mUserAuthenticationRequired, mUserAuthenticationValidityDurationSeconds, mUserPresenceRequired, mUserAuthenticationValidWhileOnBody, mInvalidatedByBiometricEnrollment, mBoundToSecureUserId, mCriticalToDeviceEncryption, mUserConfirmationRequired, mUnlockedDeviceRequired, mIsStrongBoxBacked, null);
    }
    
    public Builder setBlockModes(String... paramVarArgs)
    {
      mBlockModes = ArrayUtils.cloneIfNotEmpty(paramVarArgs);
      return this;
    }
    
    public Builder setBoundToSpecificSecureUserId(long paramLong)
    {
      mBoundToSecureUserId = paramLong;
      return this;
    }
    
    public Builder setCriticalToDeviceEncryption(boolean paramBoolean)
    {
      mCriticalToDeviceEncryption = paramBoolean;
      return this;
    }
    
    public Builder setDigests(String... paramVarArgs)
    {
      mDigests = ArrayUtils.cloneIfNotEmpty(paramVarArgs);
      return this;
    }
    
    public Builder setEncryptionPaddings(String... paramVarArgs)
    {
      mEncryptionPaddings = ArrayUtils.cloneIfNotEmpty(paramVarArgs);
      return this;
    }
    
    public Builder setInvalidatedByBiometricEnrollment(boolean paramBoolean)
    {
      mInvalidatedByBiometricEnrollment = paramBoolean;
      return this;
    }
    
    public Builder setIsStrongBoxBacked(boolean paramBoolean)
    {
      mIsStrongBoxBacked = paramBoolean;
      return this;
    }
    
    public Builder setKeyValidityEnd(Date paramDate)
    {
      setKeyValidityForOriginationEnd(paramDate);
      setKeyValidityForConsumptionEnd(paramDate);
      return this;
    }
    
    public Builder setKeyValidityForConsumptionEnd(Date paramDate)
    {
      mKeyValidityForConsumptionEnd = Utils.cloneIfNotNull(paramDate);
      return this;
    }
    
    public Builder setKeyValidityForOriginationEnd(Date paramDate)
    {
      mKeyValidityForOriginationEnd = Utils.cloneIfNotNull(paramDate);
      return this;
    }
    
    public Builder setKeyValidityStart(Date paramDate)
    {
      mKeyValidityStart = Utils.cloneIfNotNull(paramDate);
      return this;
    }
    
    public Builder setRandomizedEncryptionRequired(boolean paramBoolean)
    {
      mRandomizedEncryptionRequired = paramBoolean;
      return this;
    }
    
    public Builder setSignaturePaddings(String... paramVarArgs)
    {
      mSignaturePaddings = ArrayUtils.cloneIfNotEmpty(paramVarArgs);
      return this;
    }
    
    public Builder setUnlockedDeviceRequired(boolean paramBoolean)
    {
      mUnlockedDeviceRequired = paramBoolean;
      return this;
    }
    
    public Builder setUserAuthenticationRequired(boolean paramBoolean)
    {
      mUserAuthenticationRequired = paramBoolean;
      return this;
    }
    
    public Builder setUserAuthenticationValidWhileOnBody(boolean paramBoolean)
    {
      mUserAuthenticationValidWhileOnBody = paramBoolean;
      return this;
    }
    
    public Builder setUserAuthenticationValidityDurationSeconds(int paramInt)
    {
      if (paramInt >= -1)
      {
        mUserAuthenticationValidityDurationSeconds = paramInt;
        return this;
      }
      throw new IllegalArgumentException("seconds must be -1 or larger");
    }
    
    public Builder setUserConfirmationRequired(boolean paramBoolean)
    {
      mUserConfirmationRequired = paramBoolean;
      return this;
    }
    
    public Builder setUserPresenceRequired(boolean paramBoolean)
    {
      mUserPresenceRequired = paramBoolean;
      return this;
    }
  }
}
