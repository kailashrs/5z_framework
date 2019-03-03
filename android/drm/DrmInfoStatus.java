package android.drm;

public class DrmInfoStatus
{
  public static final int STATUS_ERROR = 2;
  public static final int STATUS_OK = 1;
  public final ProcessedData data;
  public final int infoType;
  public final String mimeType;
  public final int statusCode;
  
  public DrmInfoStatus(int paramInt1, int paramInt2, ProcessedData paramProcessedData, String paramString)
  {
    if (DrmInfoRequest.isValidType(paramInt2))
    {
      if (isValidStatusCode(paramInt1))
      {
        if ((paramString != null) && (paramString != ""))
        {
          statusCode = paramInt1;
          infoType = paramInt2;
          data = paramProcessedData;
          mimeType = paramString;
          return;
        }
        throw new IllegalArgumentException("mimeType is null or an empty string");
      }
      paramProcessedData = new StringBuilder();
      paramProcessedData.append("Unsupported status code: ");
      paramProcessedData.append(paramInt1);
      throw new IllegalArgumentException(paramProcessedData.toString());
    }
    paramProcessedData = new StringBuilder();
    paramProcessedData.append("infoType: ");
    paramProcessedData.append(paramInt2);
    throw new IllegalArgumentException(paramProcessedData.toString());
  }
  
  private boolean isValidStatusCode(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1) {
      if (paramInt == 2) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
}
