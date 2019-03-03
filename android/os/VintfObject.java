package android.os;

import java.util.Map;

public class VintfObject
{
  public VintfObject() {}
  
  public static native String[] getHalNamesAndVersions();
  
  public static native String getSepolicyVersion();
  
  public static native Long getTargetFrameworkCompatibilityMatrixVersion();
  
  public static native Map<String, String[]> getVndkSnapshots();
  
  public static native String[] report();
  
  public static native int verify(String[] paramArrayOfString);
  
  public static native int verifyWithoutAvb();
}
