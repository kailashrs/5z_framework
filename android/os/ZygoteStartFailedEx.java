package android.os;

class ZygoteStartFailedEx
  extends Exception
{
  ZygoteStartFailedEx(String paramString)
  {
    super(paramString);
  }
  
  ZygoteStartFailedEx(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  ZygoteStartFailedEx(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
