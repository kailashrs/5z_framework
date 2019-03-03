package android.drm;

public class DrmConvertedStatus
{
  public static final int STATUS_ERROR = 3;
  public static final int STATUS_INPUTDATA_ERROR = 2;
  public static final int STATUS_OK = 1;
  public final byte[] convertedData;
  public final int offset;
  public final int statusCode;
  
  public DrmConvertedStatus(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    if (isValidStatusCode(paramInt1))
    {
      statusCode = paramInt1;
      convertedData = paramArrayOfByte;
      offset = paramInt2;
      return;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Unsupported status code: ");
    paramArrayOfByte.append(paramInt1);
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  private boolean isValidStatusCode(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1)
    {
      bool2 = bool1;
      if (paramInt != 2) {
        if (paramInt == 3) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
}
