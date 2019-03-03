package android.security;

import android.os.Environment;
import android.os.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import libcore.io.IoUtils;

public class SystemKeyStore
{
  private static final String KEY_FILE_EXTENSION = ".sks";
  private static final String SYSTEM_KEYSTORE_DIRECTORY = "misc/systemkeys";
  private static SystemKeyStore mInstance = new SystemKeyStore();
  
  private SystemKeyStore() {}
  
  public static SystemKeyStore getInstance()
  {
    return mInstance;
  }
  
  private File getKeyFile(String paramString)
  {
    File localFile = new File(Environment.getDataDirectory(), "misc/systemkeys");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(".sks");
    return new File(localFile, localStringBuilder.toString());
  }
  
  public static String toHexString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    int i = paramArrayOfByte.length;
    StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 2);
    for (i = 0; i < paramArrayOfByte.length; i++)
    {
      String str = Integer.toString(paramArrayOfByte[i] & 0xFF, 16);
      Object localObject = str;
      if (str.length() == 1)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("0");
        ((StringBuilder)localObject).append(str);
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
    }
    return localStringBuilder.toString();
  }
  
  public void deleteKey(String paramString)
  {
    paramString = getKeyFile(paramString);
    if (paramString.exists())
    {
      paramString.delete();
      return;
    }
    throw new IllegalArgumentException();
  }
  
  public byte[] generateNewKey(int paramInt, String paramString1, String paramString2)
    throws NoSuchAlgorithmException
  {
    paramString2 = getKeyFile(paramString2);
    if (!paramString2.exists())
    {
      paramString1 = KeyGenerator.getInstance(paramString1);
      paramString1.init(paramInt, SecureRandom.getInstance("SHA1PRNG"));
      paramString1 = paramString1.generateKey().getEncoded();
      try
      {
        if (paramString2.createNewFile())
        {
          FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
          localFileOutputStream.<init>(paramString2);
          localFileOutputStream.write(paramString1);
          localFileOutputStream.flush();
          FileUtils.sync(localFileOutputStream);
          localFileOutputStream.close();
          FileUtils.setPermissions(paramString2.getName(), 384, -1, -1);
          return paramString1;
        }
        paramString1 = new java/lang/IllegalArgumentException;
        paramString1.<init>();
        throw paramString1;
      }
      catch (IOException paramString1)
      {
        return null;
      }
    }
    throw new IllegalArgumentException();
  }
  
  public String generateNewKeyHexString(int paramInt, String paramString1, String paramString2)
    throws NoSuchAlgorithmException
  {
    return toHexString(generateNewKey(paramInt, paramString1, paramString2));
  }
  
  public byte[] retrieveKey(String paramString)
    throws IOException
  {
    paramString = getKeyFile(paramString);
    if (!paramString.exists()) {
      return null;
    }
    return IoUtils.readFileAsByteArray(paramString.toString());
  }
  
  public String retrieveKeyHexString(String paramString)
    throws IOException
  {
    return toHexString(retrieveKey(paramString));
  }
}
