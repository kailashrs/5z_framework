package android.security.net.config;

import java.util.Arrays;

public final class Pin
{
  public final byte[] digest;
  public final String digestAlgorithm;
  private final int mHashCode;
  
  public Pin(String paramString, byte[] paramArrayOfByte)
  {
    digestAlgorithm = paramString;
    digest = paramArrayOfByte;
    mHashCode = (Arrays.hashCode(paramArrayOfByte) ^ paramString.hashCode());
  }
  
  public static int getDigestLength(String paramString)
  {
    if ("SHA-256".equalsIgnoreCase(paramString)) {
      return 32;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported digest algorithm: ");
    localStringBuilder.append(paramString);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static boolean isSupportedDigestAlgorithm(String paramString)
  {
    return "SHA-256".equalsIgnoreCase(paramString);
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof Pin)) {
      return false;
    }
    paramObject = (Pin)paramObject;
    if (paramObject.hashCode() != mHashCode) {
      return false;
    }
    if (!Arrays.equals(digest, digest)) {
      return false;
    }
    return digestAlgorithm.equals(digestAlgorithm);
  }
  
  public int hashCode()
  {
    return mHashCode;
  }
}
