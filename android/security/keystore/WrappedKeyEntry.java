package android.security.keystore;

import java.security.KeyStore.Entry;
import java.security.spec.AlgorithmParameterSpec;

public class WrappedKeyEntry
  implements KeyStore.Entry
{
  private final AlgorithmParameterSpec mAlgorithmParameterSpec;
  private final String mTransformation;
  private final byte[] mWrappedKeyBytes;
  private final String mWrappingKeyAlias;
  
  public WrappedKeyEntry(byte[] paramArrayOfByte, String paramString1, String paramString2, AlgorithmParameterSpec paramAlgorithmParameterSpec)
  {
    mWrappedKeyBytes = paramArrayOfByte;
    mWrappingKeyAlias = paramString1;
    mTransformation = paramString2;
    mAlgorithmParameterSpec = paramAlgorithmParameterSpec;
  }
  
  public AlgorithmParameterSpec getAlgorithmParameterSpec()
  {
    return mAlgorithmParameterSpec;
  }
  
  public String getTransformation()
  {
    return mTransformation;
  }
  
  public byte[] getWrappedKeyBytes()
  {
    return mWrappedKeyBytes;
  }
  
  public String getWrappingKeyAlias()
  {
    return mWrappingKeyAlias;
  }
}
