package android.filterpacks.videosink;

public class MediaRecorderStopException
  extends RuntimeException
{
  private static final String TAG = "MediaRecorderStopException";
  
  public MediaRecorderStopException() {}
  
  public MediaRecorderStopException(String paramString)
  {
    super(paramString);
  }
  
  public MediaRecorderStopException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public MediaRecorderStopException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}
