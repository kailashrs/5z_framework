package android.media;

public class MediaCasStateException
  extends IllegalStateException
{
  private final String mDiagnosticInfo;
  private final int mErrorCode;
  
  private MediaCasStateException(int paramInt, String paramString1, String paramString2)
  {
    super(paramString1);
    mErrorCode = paramInt;
    mDiagnosticInfo = paramString2;
  }
  
  static void throwExceptionIfNeeded(int paramInt)
  {
    throwExceptionIfNeeded(paramInt, null);
  }
  
  static void throwExceptionIfNeeded(int paramInt, String paramString)
  {
    if (paramInt == 0) {
      return;
    }
    if (paramInt != 6)
    {
      String str;
      switch (paramInt)
      {
      case 6: 
      case 7: 
      case 8: 
      case 11: 
      default: 
        str = "Unknown CAS state exception";
        break;
      case 14: 
        str = "General CAS error";
        break;
      case 13: 
        str = "Decrypt error";
        break;
      case 12: 
        str = "Not initialized";
        break;
      case 10: 
        str = "Tamper detected";
        break;
      case 9: 
        str = "Insufficient output protection";
        break;
      case 5: 
        str = "Invalid CAS state";
        break;
      case 4: 
        str = "Unsupported scheme or data format";
        break;
      case 3: 
        str = "Session not opened";
        break;
      case 2: 
        str = "License expired";
        break;
      case 1: 
        str = "No license";
      }
      throw new MediaCasStateException(paramInt, paramString, String.format("%s (err=%d)", new Object[] { str, Integer.valueOf(paramInt) }));
    }
    throw new IllegalArgumentException();
  }
  
  public String getDiagnosticInfo()
  {
    return mDiagnosticInfo;
  }
  
  public int getErrorCode()
  {
    return mErrorCode;
  }
}
