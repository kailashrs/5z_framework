package android.content.pm;

public final class SELinuxUtil
{
  public static final String COMPLETE_STR = ":complete";
  private static final String INSTANT_APP_STR = ":ephemeralapp";
  
  public SELinuxUtil() {}
  
  public static String assignSeinfoUser(PackageUserState paramPackageUserState)
  {
    if (instantApp) {
      return ":ephemeralapp:complete";
    }
    return ":complete";
  }
}
