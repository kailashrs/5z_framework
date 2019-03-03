package android.util;

public class LogPrinter
  implements Printer
{
  private final int mBuffer;
  private final int mPriority;
  private final String mTag;
  
  public LogPrinter(int paramInt, String paramString)
  {
    mPriority = paramInt;
    mTag = paramString;
    mBuffer = 0;
  }
  
  public LogPrinter(int paramInt1, String paramString, int paramInt2)
  {
    mPriority = paramInt1;
    mTag = paramString;
    mBuffer = paramInt2;
  }
  
  public void println(String paramString)
  {
    Log.println_native(mBuffer, mPriority, mTag, paramString);
  }
}
