package android.security.keystore;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.security.GateKeeper;
import android.security.KeyStore;
import android.security.keymaster.KeymasterArguments;
import com.android.internal.util.ArrayUtils;
import java.security.ProviderException;

public abstract class KeymasterUtils
{
  private KeymasterUtils() {}
  
  public static void addMinMacLengthAuthorizationIfNecessary(KeymasterArguments paramKeymasterArguments, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if (paramInt != 32)
    {
      if (paramInt == 128) {
        if (paramArrayOfInt2.length == 1)
        {
          paramInt = paramArrayOfInt2[0];
          int i = getDigestOutputSizeBits(paramInt);
          if (i != -1)
          {
            paramKeymasterArguments.addUnsignedInt(805306376, i);
          }
          else
          {
            paramKeymasterArguments = new StringBuilder();
            paramKeymasterArguments.append("HMAC key authorized for unsupported digest: ");
            paramKeymasterArguments.append(KeyProperties.Digest.fromKeymaster(paramInt));
            throw new ProviderException(paramKeymasterArguments.toString());
          }
        }
        else
        {
          paramKeymasterArguments = new StringBuilder();
          paramKeymasterArguments.append("Unsupported number of authorized digests for HMAC key: ");
          paramKeymasterArguments.append(paramArrayOfInt2.length);
          paramKeymasterArguments.append(". Exactly one digest must be authorized");
          throw new ProviderException(paramKeymasterArguments.toString());
        }
      }
    }
    else if (ArrayUtils.contains(paramArrayOfInt1, 32)) {
      paramKeymasterArguments.addUnsignedInt(805306376, 96L);
    }
  }
  
  public static void addUserAuthArgs(KeymasterArguments paramKeymasterArguments, UserAuthArgs paramUserAuthArgs)
  {
    if (paramUserAuthArgs.isUserConfirmationRequired()) {
      paramKeymasterArguments.addBoolean(1879048700);
    }
    if (paramUserAuthArgs.isUserPresenceRequired()) {
      paramKeymasterArguments.addBoolean(1879048699);
    }
    if (paramUserAuthArgs.isUnlockedDeviceRequired()) {
      paramKeymasterArguments.addBoolean(1879048701);
    }
    if (!paramUserAuthArgs.isUserAuthenticationRequired())
    {
      paramKeymasterArguments.addBoolean(1879048695);
      return;
    }
    long l;
    if (paramUserAuthArgs.getUserAuthenticationValidityDurationSeconds() == -1)
    {
      FingerprintManager localFingerprintManager = (FingerprintManager)KeyStore.getApplicationContext().getSystemService(FingerprintManager.class);
      if (localFingerprintManager != null) {
        l = localFingerprintManager.getAuthenticatorId();
      } else {
        l = 0L;
      }
      if (l != 0L)
      {
        if (paramUserAuthArgs.getBoundToSpecificSecureUserId() != 0L) {
          l = paramUserAuthArgs.getBoundToSpecificSecureUserId();
        }
        for (;;)
        {
          break;
          if (!paramUserAuthArgs.isInvalidatedByBiometricEnrollment()) {
            l = getRootSid();
          }
        }
        paramKeymasterArguments.addUnsignedLong(-1610612234, KeymasterArguments.toUint64(l));
        paramKeymasterArguments.addEnum(268435960, 2);
        if (paramUserAuthArgs.isUserAuthenticationValidWhileOnBody()) {
          throw new ProviderException("Key validity extension while device is on-body is not supported for keys requiring fingerprint authentication");
        }
      }
      else
      {
        throw new IllegalStateException("At least one fingerprint must be enrolled to create keys requiring user authentication for every use");
      }
    }
    else
    {
      if (paramUserAuthArgs.getBoundToSpecificSecureUserId() != 0L) {
        l = paramUserAuthArgs.getBoundToSpecificSecureUserId();
      } else {
        l = getRootSid();
      }
      paramKeymasterArguments.addUnsignedLong(-1610612234, KeymasterArguments.toUint64(l));
      paramKeymasterArguments.addEnum(268435960, 3);
      paramKeymasterArguments.addUnsignedInt(805306873, paramUserAuthArgs.getUserAuthenticationValidityDurationSeconds());
      if (paramUserAuthArgs.isUserAuthenticationValidWhileOnBody()) {
        paramKeymasterArguments.addBoolean(1879048698);
      }
    }
  }
  
  public static int getDigestOutputSizeBits(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown digest: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 6: 
      return 512;
    case 5: 
      return 384;
    case 4: 
      return 256;
    case 3: 
      return 224;
    case 2: 
      return 160;
    case 1: 
      return 128;
    }
    return -1;
  }
  
  private static long getRootSid()
  {
    long l = GateKeeper.getSecureUserId();
    if (l != 0L) {
      return l;
    }
    throw new IllegalStateException("Secure lock screen must be enabled to create keys requiring user authentication");
  }
  
  public static boolean isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(int paramInt)
  {
    if (paramInt != 32) {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported block mode: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 1: 
        return false;
      }
    }
    return true;
  }
  
  public static boolean isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(int paramInt)
  {
    if (paramInt != 4) {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported asymmetric encryption padding scheme: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 1: 
        return false;
      }
    }
    return true;
  }
}
