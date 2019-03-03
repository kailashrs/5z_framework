package com.android.internal.telephony;

import android.os.Bundle;

public class VisualVoicemailSmsParser
{
  private static final String[] ALLOWED_ALTERNATIVE_FORMAT_EVENT = { "MBOXUPDATE", "UNRECOGNIZED" };
  
  public VisualVoicemailSmsParser() {}
  
  private static boolean isAllowedAlternativeFormatEvent(String paramString)
  {
    String[] arrayOfString = ALLOWED_ALTERNATIVE_FORMAT_EVENT;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (arrayOfString[j].equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public static WrappedMessageData parse(String paramString1, String paramString2)
  {
    try
    {
      if (!paramString2.startsWith(paramString1)) {
        return null;
      }
      int i = paramString1.length();
      if (paramString2.charAt(i) != ':') {
        return null;
      }
      int j = paramString2.indexOf(":", i + 1);
      if (j == -1) {
        return null;
      }
      paramString1 = paramString2.substring(i + 1, j);
      paramString2 = parseSmsBody(paramString2.substring(j + 1));
      if (paramString2 == null) {
        return null;
      }
      paramString1 = new WrappedMessageData(paramString1, paramString2);
      return paramString1;
    }
    catch (IndexOutOfBoundsException paramString1) {}
    return null;
  }
  
  public static WrappedMessageData parseAlternativeFormat(String paramString)
  {
    try
    {
      int i = paramString.indexOf("?");
      if (i == -1) {
        return null;
      }
      String str = paramString.substring(0, i);
      if (!isAllowedAlternativeFormatEvent(str)) {
        return null;
      }
      paramString = parseSmsBody(paramString.substring(i + 1));
      if (paramString == null) {
        return null;
      }
      paramString = new WrappedMessageData(str, paramString);
      return paramString;
    }
    catch (IndexOutOfBoundsException paramString) {}
    return null;
  }
  
  private static Bundle parseSmsBody(String paramString)
  {
    Bundle localBundle = new Bundle();
    paramString = paramString.split(";");
    int i = paramString.length;
    int j = 0;
    while (j < i)
    {
      Object localObject = paramString[j];
      if (localObject.length() != 0)
      {
        int k = localObject.indexOf("=");
        if ((k != -1) && (k != 0)) {
          localBundle.putString(localObject.substring(0, k), localObject.substring(k + 1));
        }
      }
      else
      {
        j++;
        continue;
      }
      return null;
    }
    return localBundle;
  }
  
  public static class WrappedMessageData
  {
    public final Bundle fields;
    public final String prefix;
    
    WrappedMessageData(String paramString, Bundle paramBundle)
    {
      prefix = paramString;
      fields = paramBundle;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("WrappedMessageData [type=");
      localStringBuilder.append(prefix);
      localStringBuilder.append(" fields=");
      localStringBuilder.append(fields);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
  }
}
