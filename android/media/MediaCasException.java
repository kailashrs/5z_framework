package android.media;

public class MediaCasException
  extends Exception
{
  private MediaCasException(String paramString)
  {
    super(paramString);
  }
  
  static void throwExceptionIfNeeded(int paramInt)
    throws MediaCasException
  {
    if (paramInt == 0) {
      return;
    }
    if (paramInt != 7)
    {
      if (paramInt != 8)
      {
        if (paramInt != 11)
        {
          MediaCasStateException.throwExceptionIfNeeded(paramInt);
          return;
        }
        throw new DeniedByServerException(null);
      }
      throw new ResourceBusyException(null);
    }
    throw new NotProvisionedException(null);
  }
  
  public static final class DeniedByServerException
    extends MediaCasException
  {
    public DeniedByServerException(String paramString)
    {
      super(null);
    }
  }
  
  public static final class NotProvisionedException
    extends MediaCasException
  {
    public NotProvisionedException(String paramString)
    {
      super(null);
    }
  }
  
  public static final class ResourceBusyException
    extends MediaCasException
  {
    public ResourceBusyException(String paramString)
    {
      super(null);
    }
  }
  
  public static final class UnsupportedCasException
    extends MediaCasException
  {
    public UnsupportedCasException(String paramString)
    {
      super(null);
    }
  }
}
