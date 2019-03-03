package android.security.keystore;

import java.security.Key;

public class AndroidKeyStoreKey
  implements Key
{
  private final String mAlgorithm;
  private final String mAlias;
  private final int mUid;
  
  public AndroidKeyStoreKey(String paramString1, int paramInt, String paramString2)
  {
    mAlias = paramString1;
    mUid = paramInt;
    mAlgorithm = paramString2;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (AndroidKeyStoreKey)paramObject;
    if (mAlgorithm == null)
    {
      if (mAlgorithm != null) {
        return false;
      }
    }
    else if (!mAlgorithm.equals(mAlgorithm)) {
      return false;
    }
    if (mAlias == null)
    {
      if (mAlias != null) {
        return false;
      }
    }
    else if (!mAlias.equals(mAlias)) {
      return false;
    }
    return mUid == mUid;
  }
  
  public String getAlgorithm()
  {
    return mAlgorithm;
  }
  
  String getAlias()
  {
    return mAlias;
  }
  
  public byte[] getEncoded()
  {
    return null;
  }
  
  public String getFormat()
  {
    return null;
  }
  
  int getUid()
  {
    return mUid;
  }
  
  public int hashCode()
  {
    String str = mAlgorithm;
    int i = 0;
    int j;
    if (str == null) {
      j = 0;
    } else {
      j = mAlgorithm.hashCode();
    }
    if (mAlias != null) {
      i = mAlias.hashCode();
    }
    return 31 * (31 * (31 * 1 + j) + i) + mUid;
  }
}
