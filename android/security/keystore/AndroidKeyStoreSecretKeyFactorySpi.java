package android.security.keystore;

import android.security.GateKeeper;
import android.security.KeyStore;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.SecretKeySpec;

public class AndroidKeyStoreSecretKeyFactorySpi
  extends SecretKeyFactorySpi
{
  private final KeyStore mKeyStore = KeyStore.getInstance();
  
  public AndroidKeyStoreSecretKeyFactorySpi() {}
  
  private static BigInteger getGateKeeperSecureUserId()
    throws ProviderException
  {
    try
    {
      BigInteger localBigInteger = BigInteger.valueOf(GateKeeper.getSecureUserId());
      return localBigInteger;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      throw new ProviderException("Failed to get GateKeeper secure user ID", localIllegalStateException);
    }
  }
  
  static KeyInfo getKeyInfo(KeyStore paramKeyStore, String paramString1, String paramString2, int paramInt)
  {
    KeyCharacteristics localKeyCharacteristics = new KeyCharacteristics();
    paramInt = paramKeyStore.getKeyCharacteristics(paramString2, null, null, paramInt, localKeyCharacteristics);
    boolean bool1 = true;
    if (paramInt == 1) {
      try
      {
        boolean bool2;
        if (hwEnforced.containsTag(268436158)) {
          bool2 = true;
        }
        for (paramInt = KeyProperties.Origin.fromKeymaster(hwEnforced.getEnum(268436158, -1));; paramInt = KeyProperties.Origin.fromKeymaster(swEnforced.getEnum(268436158, -1)))
        {
          break;
          if (!swEnforced.containsTag(268436158)) {
            break label677;
          }
          bool2 = false;
        }
        long l = localKeyCharacteristics.getUnsignedInt(805306371, -1L);
        if (l != -1L)
        {
          if (l <= 2147483647L)
          {
            int i = (int)l;
            int j = KeyProperties.Purpose.allFromKeymaster(localKeyCharacteristics.getEnums(536870913));
            paramKeyStore = new java/util/ArrayList;
            paramKeyStore.<init>();
            paramString2 = new java/util/ArrayList;
            paramString2.<init>();
            Object localObject = localKeyCharacteristics.getEnums(536870918).iterator();
            while (((Iterator)localObject).hasNext())
            {
              k = ((Integer)((Iterator)localObject).next()).intValue();
              try
              {
                paramKeyStore.add(KeyProperties.EncryptionPadding.fromKeymaster(k));
              }
              catch (IllegalArgumentException localIllegalArgumentException) {}
              try
              {
                paramString2.add(KeyProperties.SignaturePadding.fromKeymaster(k));
              }
              catch (IllegalArgumentException paramKeyStore)
              {
                paramString1 = new java/security/ProviderException;
                paramKeyStore = new java/lang/StringBuilder;
                paramKeyStore.<init>();
                paramKeyStore.append("Unsupported encryption padding: ");
                paramKeyStore.append(k);
                paramString1.<init>(paramKeyStore.toString());
                throw paramString1;
              }
            }
            paramKeyStore = (String[])paramKeyStore.toArray(new String[paramKeyStore.size()]);
            localObject = (String[])paramString2.toArray(new String[paramString2.size()]);
            String[] arrayOfString2 = KeyProperties.Digest.allFromKeymaster(localKeyCharacteristics.getEnums(536870917));
            String[] arrayOfString1 = KeyProperties.BlockMode.allFromKeymaster(localKeyCharacteristics.getEnums(536870916));
            int k = swEnforced.getEnum(268435960, 0);
            int m = hwEnforced.getEnum(268435960, 0);
            List localList = localKeyCharacteristics.getUnsignedLongs(-1610612234);
            Date localDate1 = localKeyCharacteristics.getDate(1610613136);
            paramString2 = localKeyCharacteristics.getDate(1610613137);
            Date localDate2 = localKeyCharacteristics.getDate(1610613138);
            boolean bool3 = localKeyCharacteristics.getBoolean(1879048695) ^ true;
            l = localKeyCharacteristics.getUnsignedInt(805306873, -1L);
            if (l <= 2147483647L)
            {
              boolean bool4;
              if ((bool3) && (m != 0) && (k == 0)) {
                bool4 = true;
              } else {
                bool4 = false;
              }
              boolean bool5 = hwEnforced.getBoolean(1879048698);
              boolean bool6 = hwEnforced.getBoolean(1879048699);
              if ((k != 2) && (m != 2)) {
                bool1 = false;
              } else if ((localList == null) || (localList.isEmpty()) || (localList.contains(getGateKeeperSecureUserId()))) {
                bool1 = false;
              }
              boolean bool7 = hwEnforced.getBoolean(1879048700);
              return new KeyInfo(paramString1, bool2, paramInt, i, localDate1, paramString2, localDate2, j, paramKeyStore, (String[])localObject, arrayOfString2, arrayOfString1, bool3, (int)l, bool4, bool5, bool6, bool1, bool7);
            }
            paramKeyStore = new StringBuilder();
            paramKeyStore.append("User authentication timeout validity too long: ");
            paramKeyStore.append(l);
            paramKeyStore.append(" seconds");
            throw new ProviderException(paramKeyStore.toString());
          }
          paramKeyStore = new java/security/ProviderException;
          paramString1 = new java/lang/StringBuilder;
          paramString1.<init>();
          paramString1.append("Key too large: ");
          paramString1.append(l);
          paramString1.append(" bits");
          paramKeyStore.<init>(paramString1.toString());
          throw paramKeyStore;
        }
        paramKeyStore = new java/security/ProviderException;
        paramKeyStore.<init>("Key size not available");
        throw paramKeyStore;
        label677:
        paramKeyStore = new java/security/ProviderException;
        paramKeyStore.<init>("Key origin not available");
        throw paramKeyStore;
      }
      catch (IllegalArgumentException paramKeyStore)
      {
        throw new ProviderException("Unsupported key characteristic", paramKeyStore);
      }
    }
    paramKeyStore = new StringBuilder();
    paramKeyStore.append("Failed to obtain information about key. Keystore error: ");
    paramKeyStore.append(paramInt);
    throw new ProviderException(paramKeyStore.toString());
  }
  
  protected SecretKey engineGenerateSecret(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    paramKeySpec = new StringBuilder();
    paramKeySpec.append("To generate secret key in Android Keystore, use KeyGenerator initialized with ");
    paramKeySpec.append(KeyGenParameterSpec.class.getName());
    throw new InvalidKeySpecException(paramKeySpec.toString());
  }
  
  protected KeySpec engineGetKeySpec(SecretKey paramSecretKey, Class paramClass)
    throws InvalidKeySpecException
  {
    if (paramClass != null)
    {
      if (!(paramSecretKey instanceof AndroidKeyStoreSecretKey))
      {
        paramClass = new StringBuilder();
        paramClass.append("Only Android KeyStore secret keys supported: ");
        if (paramSecretKey != null) {
          paramSecretKey = paramSecretKey.getClass().getName();
        } else {
          paramSecretKey = "null";
        }
        paramClass.append(paramSecretKey);
        throw new InvalidKeySpecException(paramClass.toString());
      }
      if (!SecretKeySpec.class.isAssignableFrom(paramClass))
      {
        if (KeyInfo.class.equals(paramClass))
        {
          AndroidKeyStoreKey localAndroidKeyStoreKey = (AndroidKeyStoreKey)paramSecretKey;
          paramClass = localAndroidKeyStoreKey.getAlias();
          if (paramClass.startsWith("USRPKEY_")) {}
          for (paramSecretKey = paramClass.substring("USRPKEY_".length());; paramSecretKey = paramClass.substring("USRSKEY_".length()))
          {
            break;
            if (!paramClass.startsWith("USRSKEY_")) {
              break label154;
            }
          }
          return getKeyInfo(mKeyStore, paramSecretKey, paramClass, localAndroidKeyStoreKey.getUid());
          label154:
          paramSecretKey = new StringBuilder();
          paramSecretKey.append("Invalid key alias: ");
          paramSecretKey.append(paramClass);
          throw new InvalidKeySpecException(paramSecretKey.toString());
        }
        paramSecretKey = new StringBuilder();
        paramSecretKey.append("Unsupported key spec: ");
        paramSecretKey.append(paramClass.getName());
        throw new InvalidKeySpecException(paramSecretKey.toString());
      }
      throw new InvalidKeySpecException("Key material export of Android KeyStore keys is not supported");
    }
    throw new InvalidKeySpecException("keySpecClass == null");
  }
  
  protected SecretKey engineTranslateKey(SecretKey paramSecretKey)
    throws InvalidKeyException
  {
    if (paramSecretKey != null)
    {
      if ((paramSecretKey instanceof AndroidKeyStoreSecretKey)) {
        return paramSecretKey;
      }
      throw new InvalidKeyException("To import a secret key into Android Keystore, use KeyStore.setEntry");
    }
    throw new InvalidKeyException("key == null");
  }
}
