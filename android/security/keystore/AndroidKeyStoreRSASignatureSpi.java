package android.security.keystore;

import android.security.keymaster.KeymasterArguments;
import java.security.InvalidKeyException;

abstract class AndroidKeyStoreRSASignatureSpi
  extends AndroidKeyStoreSignatureSpiBase
{
  private final int mKeymasterDigest;
  private final int mKeymasterPadding;
  
  AndroidKeyStoreRSASignatureSpi(int paramInt1, int paramInt2)
  {
    mKeymasterDigest = paramInt1;
    mKeymasterPadding = paramInt2;
  }
  
  protected final void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
  {
    paramKeymasterArguments.addEnum(268435458, 1);
    paramKeymasterArguments.addEnum(536870917, mKeymasterDigest);
    paramKeymasterArguments.addEnum(536870918, mKeymasterPadding);
  }
  
  protected final void initKey(AndroidKeyStoreKey paramAndroidKeyStoreKey)
    throws InvalidKeyException
  {
    if ("RSA".equalsIgnoreCase(paramAndroidKeyStoreKey.getAlgorithm()))
    {
      super.initKey(paramAndroidKeyStoreKey);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported key algorithm: ");
    localStringBuilder.append(paramAndroidKeyStoreKey.getAlgorithm());
    localStringBuilder.append(". Only");
    localStringBuilder.append("RSA");
    localStringBuilder.append(" supported");
    throw new InvalidKeyException(localStringBuilder.toString());
  }
  
  protected final void resetAll()
  {
    super.resetAll();
  }
  
  protected final void resetWhilePreservingInitState()
  {
    super.resetWhilePreservingInitState();
  }
  
  public static final class MD5WithPKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi.PKCS1Padding
  {
    public MD5WithPKCS1Padding()
    {
      super();
    }
  }
  
  public static final class NONEWithPKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi.PKCS1Padding
  {
    public NONEWithPKCS1Padding()
    {
      super();
    }
  }
  
  static abstract class PKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi
  {
    PKCS1Padding(int paramInt)
    {
      super(5);
    }
    
    protected final int getAdditionalEntropyAmountForSign()
    {
      return 0;
    }
  }
  
  static abstract class PSSPadding
    extends AndroidKeyStoreRSASignatureSpi
  {
    private static final int SALT_LENGTH_BYTES = 20;
    
    PSSPadding(int paramInt)
    {
      super(3);
    }
    
    protected final int getAdditionalEntropyAmountForSign()
    {
      return 20;
    }
  }
  
  public static final class SHA1WithPKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi.PKCS1Padding
  {
    public SHA1WithPKCS1Padding()
    {
      super();
    }
  }
  
  public static final class SHA1WithPSSPadding
    extends AndroidKeyStoreRSASignatureSpi.PSSPadding
  {
    public SHA1WithPSSPadding()
    {
      super();
    }
  }
  
  public static final class SHA224WithPKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi.PKCS1Padding
  {
    public SHA224WithPKCS1Padding()
    {
      super();
    }
  }
  
  public static final class SHA224WithPSSPadding
    extends AndroidKeyStoreRSASignatureSpi.PSSPadding
  {
    public SHA224WithPSSPadding()
    {
      super();
    }
  }
  
  public static final class SHA256WithPKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi.PKCS1Padding
  {
    public SHA256WithPKCS1Padding()
    {
      super();
    }
  }
  
  public static final class SHA256WithPSSPadding
    extends AndroidKeyStoreRSASignatureSpi.PSSPadding
  {
    public SHA256WithPSSPadding()
    {
      super();
    }
  }
  
  public static final class SHA384WithPKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi.PKCS1Padding
  {
    public SHA384WithPKCS1Padding()
    {
      super();
    }
  }
  
  public static final class SHA384WithPSSPadding
    extends AndroidKeyStoreRSASignatureSpi.PSSPadding
  {
    public SHA384WithPSSPadding()
    {
      super();
    }
  }
  
  public static final class SHA512WithPKCS1Padding
    extends AndroidKeyStoreRSASignatureSpi.PKCS1Padding
  {
    public SHA512WithPKCS1Padding()
    {
      super();
    }
  }
  
  public static final class SHA512WithPSSPadding
    extends AndroidKeyStoreRSASignatureSpi.PSSPadding
  {
    public SHA512WithPSSPadding()
    {
      super();
    }
  }
}
