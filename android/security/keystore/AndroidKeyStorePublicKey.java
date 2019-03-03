package android.security.keystore;

import java.security.PublicKey;
import java.util.Arrays;

public class AndroidKeyStorePublicKey
  extends AndroidKeyStoreKey
  implements PublicKey
{
  private final byte[] mEncoded;
  
  public AndroidKeyStorePublicKey(String paramString1, int paramInt, String paramString2, byte[] paramArrayOfByte)
  {
    super(paramString1, paramInt, paramString2);
    mEncoded = ArrayUtils.cloneIfNotEmpty(paramArrayOfByte);
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!super.equals(paramObject)) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (AndroidKeyStorePublicKey)paramObject;
    return Arrays.equals(mEncoded, mEncoded);
  }
  
  public byte[] getEncoded()
  {
    return ArrayUtils.cloneIfNotEmpty(mEncoded);
  }
  
  public String getFormat()
  {
    return "X.509";
  }
  
  public int hashCode()
  {
    return 31 * super.hashCode() + Arrays.hashCode(mEncoded);
  }
}
