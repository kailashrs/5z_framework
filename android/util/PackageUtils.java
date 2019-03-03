package android.util;

import android.content.pm.Signature;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class PackageUtils
{
  private PackageUtils() {}
  
  public static String computeSha256Digest(byte[] paramArrayOfByte)
  {
    return ByteStringUtils.toHexString(computeSha256DigestBytes(paramArrayOfByte));
  }
  
  public static byte[] computeSha256DigestBytes(byte[] paramArrayOfByte)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA256");
      localMessageDigest.update(paramArrayOfByte);
      return localMessageDigest.digest();
    }
    catch (NoSuchAlgorithmException paramArrayOfByte) {}
    return null;
  }
  
  public static String computeSignaturesSha256Digest(Signature[] paramArrayOfSignature)
  {
    if (paramArrayOfSignature.length == 1) {
      return computeSha256Digest(paramArrayOfSignature[0].toByteArray());
    }
    return computeSignaturesSha256Digest(computeSignaturesSha256Digests(paramArrayOfSignature));
  }
  
  public static String computeSignaturesSha256Digest(String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    int j = 0;
    if (i == 1) {
      return paramArrayOfString[0];
    }
    Arrays.sort(paramArrayOfString);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    i = paramArrayOfString.length;
    while (j < i)
    {
      String str = paramArrayOfString[j];
      try
      {
        localByteArrayOutputStream.write(str.getBytes());
      }
      catch (IOException localIOException) {}
      j++;
    }
    return computeSha256Digest(localByteArrayOutputStream.toByteArray());
  }
  
  public static String[] computeSignaturesSha256Digests(Signature[] paramArrayOfSignature)
  {
    int i = paramArrayOfSignature.length;
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++) {
      arrayOfString[j] = computeSha256Digest(paramArrayOfSignature[j].toByteArray());
    }
    return arrayOfString;
  }
}
