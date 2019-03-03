package com.android.internal.telephony.cat;

import android.telephony.Rlog;

public abstract class CatLog
{
  static final boolean DEBUG = true;
  
  public CatLog() {}
  
  public static void d(Object paramObject, String paramString)
  {
    String str = paramObject.getClass().getName();
    paramObject = new StringBuilder();
    paramObject.append(str.substring(str.lastIndexOf('.') + 1));
    paramObject.append(": ");
    paramObject.append(paramString);
    Rlog.d("CAT", paramObject.toString());
  }
  
  public static void d(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(": ");
    localStringBuilder.append(paramString2);
    Rlog.d("CAT", localStringBuilder.toString());
  }
  
  public static void e(Object paramObject, String paramString)
  {
    String str = paramObject.getClass().getName();
    paramObject = new StringBuilder();
    paramObject.append(str.substring(str.lastIndexOf('.') + 1));
    paramObject.append(": ");
    paramObject.append(paramString);
    Rlog.e("CAT", paramObject.toString());
  }
  
  public static void e(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(": ");
    localStringBuilder.append(paramString2);
    Rlog.e("CAT", localStringBuilder.toString());
  }
}
