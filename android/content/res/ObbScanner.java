package android.content.res;

import java.io.File;
import java.io.IOException;

public class ObbScanner
{
  private ObbScanner() {}
  
  public static ObbInfo getObbInfo(String paramString)
    throws IOException
  {
    if (paramString != null)
    {
      Object localObject = new File(paramString);
      if (((File)localObject).exists())
      {
        paramString = ((File)localObject).getCanonicalPath();
        localObject = new ObbInfo();
        filename = paramString;
        getObbInfo_native(paramString, (ObbInfo)localObject);
        return localObject;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("OBB file does not exist: ");
      ((StringBuilder)localObject).append(paramString);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    throw new IllegalArgumentException("file path cannot be null");
  }
  
  private static native void getObbInfo_native(String paramString, ObbInfo paramObbInfo)
    throws IOException;
}
