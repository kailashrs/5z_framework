package android.telephony;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Rlog
{
  private static final boolean USER_BUILD = Build.IS_USER;
  
  private Rlog() {}
  
  public static int d(String paramString1, String paramString2)
  {
    return Log.println_native(1, 3, paramString1, paramString2);
  }
  
  public static int d(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(1, 3, paramString1, localStringBuilder.toString());
  }
  
  public static int e(String paramString1, String paramString2)
  {
    return Log.println_native(1, 6, paramString1, paramString2);
  }
  
  public static int e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(1, 6, paramString1, localStringBuilder.toString());
  }
  
  public static int i(String paramString1, String paramString2)
  {
    return Log.println_native(1, 4, paramString1, paramString2);
  }
  
  public static int i(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(1, 4, paramString1, localStringBuilder.toString());
  }
  
  public static boolean isLoggable(String paramString, int paramInt)
  {
    return Log.isLoggable(paramString, paramInt);
  }
  
  public static String pii(String paramString, Object paramObject)
  {
    String str = String.valueOf(paramObject);
    if ((paramObject != null) && (!TextUtils.isEmpty(str)) && (!isLoggable(paramString, 2)))
    {
      paramString = new StringBuilder();
      paramString.append("[");
      paramString.append(secureHash(str.getBytes()));
      paramString.append("]");
      return paramString.toString();
    }
    return str;
  }
  
  public static String pii(boolean paramBoolean, Object paramObject)
  {
    String str = String.valueOf(paramObject);
    if ((paramObject != null) && (!TextUtils.isEmpty(str)) && (!paramBoolean))
    {
      paramObject = new StringBuilder();
      paramObject.append("[");
      paramObject.append(secureHash(str.getBytes()));
      paramObject.append("]");
      return paramObject.toString();
    }
    return str;
  }
  
  public static int println(int paramInt, String paramString1, String paramString2)
  {
    return Log.println_native(1, paramInt, paramString1, paramString2);
  }
  
  private static String secureHash(byte[] paramArrayOfByte)
  {
    if (USER_BUILD) {
      return "****";
    }
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-1");
      return Base64.encodeToString(localMessageDigest.digest(paramArrayOfByte), 11);
    }
    catch (NoSuchAlgorithmException paramArrayOfByte) {}
    return "####";
  }
  
  public static int v(String paramString1, String paramString2)
  {
    return Log.println_native(1, 2, paramString1, paramString2);
  }
  
  public static int v(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(1, 2, paramString1, localStringBuilder.toString());
  }
  
  public static int w(String paramString1, String paramString2)
  {
    return Log.println_native(1, 5, paramString1, paramString2);
  }
  
  public static int w(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append('\n');
    localStringBuilder.append(Log.getStackTraceString(paramThrowable));
    return Log.println_native(1, 5, paramString1, localStringBuilder.toString());
  }
  
  public static int w(String paramString, Throwable paramThrowable)
  {
    return Log.println_native(1, 5, paramString, Log.getStackTraceString(paramThrowable));
  }
}
