package android.util;

import java.io.Writer;

public class LogWriter
  extends Writer
{
  private final int mBuffer;
  private StringBuilder mBuilder = new StringBuilder(128);
  private final int mPriority;
  private final String mTag;
  
  public LogWriter(int paramInt, String paramString)
  {
    mPriority = paramInt;
    mTag = paramString;
    mBuffer = 0;
  }
  
  public LogWriter(int paramInt1, String paramString, int paramInt2)
  {
    mPriority = paramInt1;
    mTag = paramString;
    mBuffer = paramInt2;
  }
  
  private void flushBuilder()
  {
    if (mBuilder.length() > 0)
    {
      Log.println_native(mBuffer, mPriority, mTag, mBuilder.toString());
      mBuilder.delete(0, mBuilder.length());
    }
  }
  
  public void close()
  {
    flushBuilder();
  }
  
  public void flush()
  {
    flushBuilder();
  }
  
  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    for (int i = 0; i < paramInt2; i++)
    {
      char c = paramArrayOfChar[(paramInt1 + i)];
      if (c == '\n') {
        flushBuilder();
      } else {
        mBuilder.append(c);
      }
    }
  }
}
