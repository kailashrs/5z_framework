package android.security.keystore;

public abstract interface UserAuthArgs
{
  public abstract long getBoundToSpecificSecureUserId();
  
  public abstract int getUserAuthenticationValidityDurationSeconds();
  
  public abstract boolean isInvalidatedByBiometricEnrollment();
  
  public abstract boolean isUnlockedDeviceRequired();
  
  public abstract boolean isUserAuthenticationRequired();
  
  public abstract boolean isUserAuthenticationValidWhileOnBody();
  
  public abstract boolean isUserConfirmationRequired();
  
  public abstract boolean isUserPresenceRequired();
}
