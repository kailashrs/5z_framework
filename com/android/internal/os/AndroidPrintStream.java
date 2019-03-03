package com.android.internal.os;

import android.util.Log;

class AndroidPrintStream
  extends LoggingPrintStream
{
  private final int priority;
  private final String tag;
  
  public AndroidPrintStream(int paramInt, String paramString)
  {
    if (paramString != null)
    {
      priority = paramInt;
      tag = paramString;
      return;
    }
    throw new NullPointerException("tag");
  }
  
  protected void log(String paramString)
  {
    Log.println(priority, tag, paramString);
  }
}
