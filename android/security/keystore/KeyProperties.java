package android.security.keystore;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import libcore.util.EmptyArray;

public abstract class KeyProperties
{
  public static final String BLOCK_MODE_CBC = "CBC";
  public static final String BLOCK_MODE_CTR = "CTR";
  public static final String BLOCK_MODE_ECB = "ECB";
  public static final String BLOCK_MODE_GCM = "GCM";
  public static final String DIGEST_MD5 = "MD5";
  public static final String DIGEST_NONE = "NONE";
  public static final String DIGEST_SHA1 = "SHA-1";
  public static final String DIGEST_SHA224 = "SHA-224";
  public static final String DIGEST_SHA256 = "SHA-256";
  public static final String DIGEST_SHA384 = "SHA-384";
  public static final String DIGEST_SHA512 = "SHA-512";
  public static final String ENCRYPTION_PADDING_NONE = "NoPadding";
  public static final String ENCRYPTION_PADDING_PKCS7 = "PKCS7Padding";
  public static final String ENCRYPTION_PADDING_RSA_OAEP = "OAEPPadding";
  public static final String ENCRYPTION_PADDING_RSA_PKCS1 = "PKCS1Padding";
  @Deprecated
  public static final String KEY_ALGORITHM_3DES = "DESede";
  public static final String KEY_ALGORITHM_AES = "AES";
  public static final String KEY_ALGORITHM_EC = "EC";
  public static final String KEY_ALGORITHM_HMAC_SHA1 = "HmacSHA1";
  public static final String KEY_ALGORITHM_HMAC_SHA224 = "HmacSHA224";
  public static final String KEY_ALGORITHM_HMAC_SHA256 = "HmacSHA256";
  public static final String KEY_ALGORITHM_HMAC_SHA384 = "HmacSHA384";
  public static final String KEY_ALGORITHM_HMAC_SHA512 = "HmacSHA512";
  public static final String KEY_ALGORITHM_RSA = "RSA";
  public static final int ORIGIN_GENERATED = 1;
  public static final int ORIGIN_IMPORTED = 2;
  public static final int ORIGIN_SECURELY_IMPORTED = 8;
  public static final int ORIGIN_UNKNOWN = 4;
  public static final int PURPOSE_DECRYPT = 2;
  public static final int PURPOSE_ENCRYPT = 1;
  public static final int PURPOSE_SIGN = 4;
  public static final int PURPOSE_VERIFY = 8;
  public static final int PURPOSE_WRAP_KEY = 32;
  public static final String SIGNATURE_PADDING_RSA_PKCS1 = "PKCS1";
  public static final String SIGNATURE_PADDING_RSA_PSS = "PSS";
  
  private KeyProperties() {}
  
  private static int getSetBitCount(int paramInt)
  {
    int i = 0;
    if (paramInt == 0) {
      return 0;
    }
    while (paramInt != 0)
    {
      int j = i;
      if ((paramInt & 0x1) != 0) {
        j = i + 1;
      }
      paramInt >>>= 1;
      i = j;
    }
    return i;
  }
  
  private static int[] getSetFlags(int paramInt)
  {
    if (paramInt == 0) {
      return EmptyArray.INT;
    }
    int[] arrayOfInt = new int[getSetBitCount(paramInt)];
    int i = 0;
    int j = 1;
    int k = paramInt;
    paramInt = j;
    while (k != 0)
    {
      j = i;
      if ((k & 0x1) != 0)
      {
        arrayOfInt[i] = paramInt;
        j = i + 1;
      }
      k >>>= 1;
      paramInt <<= 1;
      i = j;
    }
    return arrayOfInt;
  }
  
  public static abstract class BlockMode
  {
    private BlockMode() {}
    
    public static String[] allFromKeymaster(Collection<Integer> paramCollection)
    {
      if ((paramCollection != null) && (!paramCollection.isEmpty()))
      {
        String[] arrayOfString = new String[paramCollection.size()];
        int i = 0;
        paramCollection = paramCollection.iterator();
        while (paramCollection.hasNext())
        {
          arrayOfString[i] = fromKeymaster(((Integer)paramCollection.next()).intValue());
          i++;
        }
        return arrayOfString;
      }
      return EmptyArray.STRING;
    }
    
    public static int[] allToKeymaster(String[] paramArrayOfString)
    {
      if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
      {
        int[] arrayOfInt = new int[paramArrayOfString.length];
        for (int i = 0; i < paramArrayOfString.length; i++) {
          arrayOfInt[i] = toKeymaster(paramArrayOfString[i]);
        }
        return arrayOfInt;
      }
      return EmptyArray.INT;
    }
    
    public static String fromKeymaster(int paramInt)
    {
      if (paramInt != 32)
      {
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unsupported block mode: ");
          localStringBuilder.append(paramInt);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 3: 
          return "CTR";
        case 2: 
          return "CBC";
        }
        return "ECB";
      }
      return "GCM";
    }
    
    public static int toKeymaster(String paramString)
    {
      if ("ECB".equalsIgnoreCase(paramString)) {
        return 1;
      }
      if ("CBC".equalsIgnoreCase(paramString)) {
        return 2;
      }
      if ("CTR".equalsIgnoreCase(paramString)) {
        return 3;
      }
      if ("GCM".equalsIgnoreCase(paramString)) {
        return 32;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported block mode: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BlockModeEnum {}
  
  public static abstract class Digest
  {
    private Digest() {}
    
    public static String[] allFromKeymaster(Collection<Integer> paramCollection)
    {
      if (paramCollection.isEmpty()) {
        return EmptyArray.STRING;
      }
      String[] arrayOfString = new String[paramCollection.size()];
      int i = 0;
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext())
      {
        arrayOfString[i] = fromKeymaster(((Integer)paramCollection.next()).intValue());
        i++;
      }
      return arrayOfString;
    }
    
    public static int[] allToKeymaster(String[] paramArrayOfString)
    {
      if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
      {
        int[] arrayOfInt = new int[paramArrayOfString.length];
        int i = 0;
        int j = paramArrayOfString.length;
        for (int k = 0; k < j; k++)
        {
          arrayOfInt[i] = toKeymaster(paramArrayOfString[k]);
          i++;
        }
        return arrayOfInt;
      }
      return EmptyArray.INT;
    }
    
    public static String fromKeymaster(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported digest algorithm: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 6: 
        return "SHA-512";
      case 5: 
        return "SHA-384";
      case 4: 
        return "SHA-256";
      case 3: 
        return "SHA-224";
      case 2: 
        return "SHA-1";
      case 1: 
        return "MD5";
      }
      return "NONE";
    }
    
    public static String fromKeymasterToSignatureAlgorithmDigest(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported digest algorithm: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 6: 
        return "SHA512";
      case 5: 
        return "SHA384";
      case 4: 
        return "SHA256";
      case 3: 
        return "SHA224";
      case 2: 
        return "SHA1";
      case 1: 
        return "MD5";
      }
      return "NONE";
    }
    
    public static int toKeymaster(String paramString)
    {
      Object localObject = paramString.toUpperCase(Locale.US);
      switch (((String)localObject).hashCode())
      {
      default: 
        break;
      case 78861104: 
        if (((String)localObject).equals("SHA-1")) {
          i = 0;
        }
        break;
      case 2402104: 
        if (((String)localObject).equals("NONE")) {
          i = 5;
        }
        break;
      case 76158: 
        if (((String)localObject).equals("MD5")) {
          i = 6;
        }
        break;
      case -1523884971: 
        if (((String)localObject).equals("SHA-512")) {
          i = 4;
        }
        break;
      case -1523886674: 
        if (((String)localObject).equals("SHA-384")) {
          i = 3;
        }
        break;
      case -1523887726: 
        if (((String)localObject).equals("SHA-256")) {
          i = 2;
        }
        break;
      case -1523887821: 
        if (((String)localObject).equals("SHA-224")) {
          i = 1;
        }
        break;
      }
      int i = -1;
      switch (i)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unsupported digest algorithm: ");
        ((StringBuilder)localObject).append(paramString);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      case 6: 
        return 1;
      case 5: 
        return 0;
      case 4: 
        return 6;
      case 3: 
        return 5;
      case 2: 
        return 4;
      case 1: 
        return 3;
      }
      return 2;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DigestEnum {}
  
  public static abstract class EncryptionPadding
  {
    private EncryptionPadding() {}
    
    public static int[] allToKeymaster(String[] paramArrayOfString)
    {
      if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
      {
        int[] arrayOfInt = new int[paramArrayOfString.length];
        for (int i = 0; i < paramArrayOfString.length; i++) {
          arrayOfInt[i] = toKeymaster(paramArrayOfString[i]);
        }
        return arrayOfInt;
      }
      return EmptyArray.INT;
    }
    
    public static String fromKeymaster(int paramInt)
    {
      if (paramInt != 4)
      {
        if (paramInt != 64)
        {
          switch (paramInt)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unsupported encryption padding: ");
            localStringBuilder.append(paramInt);
            throw new IllegalArgumentException(localStringBuilder.toString());
          case 2: 
            return "OAEPPadding";
          }
          return "NoPadding";
        }
        return "PKCS7Padding";
      }
      return "PKCS1Padding";
    }
    
    public static int toKeymaster(String paramString)
    {
      if ("NoPadding".equalsIgnoreCase(paramString)) {
        return 1;
      }
      if ("PKCS7Padding".equalsIgnoreCase(paramString)) {
        return 64;
      }
      if ("PKCS1Padding".equalsIgnoreCase(paramString)) {
        return 4;
      }
      if ("OAEPPadding".equalsIgnoreCase(paramString)) {
        return 2;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported encryption padding scheme: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EncryptionPaddingEnum {}
  
  public static abstract class KeyAlgorithm
  {
    private KeyAlgorithm() {}
    
    public static String fromKeymasterAsymmetricKeyAlgorithm(int paramInt)
    {
      if (paramInt != 1)
      {
        if (paramInt == 3) {
          return "EC";
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported key algorithm: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      return "RSA";
    }
    
    public static String fromKeymasterSecretKeyAlgorithm(int paramInt1, int paramInt2)
    {
      StringBuilder localStringBuilder;
      if (paramInt1 != 128)
      {
        switch (paramInt1)
        {
        default: 
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unsupported key algorithm: ");
          localStringBuilder.append(paramInt1);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 33: 
          return "DESede";
        }
        return "AES";
      }
      switch (paramInt2)
      {
      default: 
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported HMAC digest: ");
        localStringBuilder.append(KeyProperties.Digest.fromKeymaster(paramInt2));
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 6: 
        return "HmacSHA512";
      case 5: 
        return "HmacSHA384";
      case 4: 
        return "HmacSHA256";
      case 3: 
        return "HmacSHA224";
      }
      return "HmacSHA1";
    }
    
    public static int toKeymasterAsymmetricKeyAlgorithm(String paramString)
    {
      if ("EC".equalsIgnoreCase(paramString)) {
        return 3;
      }
      if ("RSA".equalsIgnoreCase(paramString)) {
        return 1;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported key algorithm: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public static int toKeymasterDigest(String paramString)
    {
      paramString = paramString.toUpperCase(Locale.US);
      boolean bool = paramString.startsWith("HMAC");
      int i = -1;
      if (bool)
      {
        String str = paramString.substring("HMAC".length());
        switch (str.hashCode())
        {
        default: 
          break;
        case 2543909: 
          if (str.equals("SHA1")) {
            i = 0;
          }
          break;
        case -1850265334: 
          if (str.equals("SHA512")) {
            i = 4;
          }
          break;
        case -1850267037: 
          if (str.equals("SHA384")) {
            i = 3;
          }
          break;
        case -1850268089: 
          if (str.equals("SHA256")) {
            i = 2;
          }
          break;
        case -1850268184: 
          if (str.equals("SHA224")) {
            i = 1;
          }
          break;
        }
        switch (i)
        {
        default: 
          paramString = new StringBuilder();
          paramString.append("Unsupported HMAC digest: ");
          paramString.append(str);
          throw new IllegalArgumentException(paramString.toString());
        case 4: 
          return 6;
        case 3: 
          return 5;
        case 2: 
          return 4;
        case 1: 
          return 3;
        }
        return 2;
      }
      return -1;
    }
    
    public static int toKeymasterSecretKeyAlgorithm(String paramString)
    {
      if ("AES".equalsIgnoreCase(paramString)) {
        return 32;
      }
      if ("DESede".equalsIgnoreCase(paramString)) {
        return 33;
      }
      if (paramString.toUpperCase(Locale.US).startsWith("HMAC")) {
        return 128;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported secret key algorithm: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface KeyAlgorithmEnum {}
  
  public static abstract class Origin
  {
    private Origin() {}
    
    public static int fromKeymaster(int paramInt)
    {
      if (paramInt != 0)
      {
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown origin: ");
          localStringBuilder.append(paramInt);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 4: 
          return 8;
        case 3: 
          return 4;
        }
        return 2;
      }
      return 1;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OriginEnum {}
  
  public static abstract class Purpose
  {
    private Purpose() {}
    
    public static int allFromKeymaster(Collection<Integer> paramCollection)
    {
      int i = 0;
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext()) {
        i |= fromKeymaster(((Integer)paramCollection.next()).intValue());
      }
      return i;
    }
    
    public static int[] allToKeymaster(int paramInt)
    {
      int[] arrayOfInt = KeyProperties.getSetFlags(paramInt);
      for (paramInt = 0; paramInt < arrayOfInt.length; paramInt++) {
        arrayOfInt[paramInt] = toKeymaster(arrayOfInt[paramInt]);
      }
      return arrayOfInt;
    }
    
    public static int fromKeymaster(int paramInt)
    {
      if (paramInt != 5)
      {
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown purpose: ");
          localStringBuilder.append(paramInt);
          throw new IllegalArgumentException(localStringBuilder.toString());
        case 3: 
          return 8;
        case 2: 
          return 4;
        case 1: 
          return 2;
        }
        return 1;
      }
      return 32;
    }
    
    public static int toKeymaster(int paramInt)
    {
      if (paramInt != 4)
      {
        if (paramInt != 8)
        {
          if (paramInt != 32)
          {
            switch (paramInt)
            {
            default: 
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("Unknown purpose: ");
              localStringBuilder.append(paramInt);
              throw new IllegalArgumentException(localStringBuilder.toString());
            case 2: 
              return 1;
            }
            return 0;
          }
          return 5;
        }
        return 3;
      }
      return 2;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PurposeEnum {}
  
  static abstract class SignaturePadding
  {
    private SignaturePadding() {}
    
    static int[] allToKeymaster(String[] paramArrayOfString)
    {
      if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
      {
        int[] arrayOfInt = new int[paramArrayOfString.length];
        for (int i = 0; i < paramArrayOfString.length; i++) {
          arrayOfInt[i] = toKeymaster(paramArrayOfString[i]);
        }
        return arrayOfInt;
      }
      return EmptyArray.INT;
    }
    
    static String fromKeymaster(int paramInt)
    {
      if (paramInt != 3)
      {
        if (paramInt == 5) {
          return "PKCS1";
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported signature padding: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      return "PSS";
    }
    
    static int toKeymaster(String paramString)
    {
      Object localObject = paramString.toUpperCase(Locale.US);
      int i = ((String)localObject).hashCode();
      if (i != 79536)
      {
        if ((i == 76183014) && (((String)localObject).equals("PKCS1")))
        {
          i = 0;
          break label58;
        }
      }
      else if (((String)localObject).equals("PSS"))
      {
        i = 1;
        break label58;
      }
      i = -1;
      switch (i)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unsupported signature padding scheme: ");
        ((StringBuilder)localObject).append(paramString);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      case 1: 
        label58:
        return 3;
      }
      return 5;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SignaturePaddingEnum {}
}
