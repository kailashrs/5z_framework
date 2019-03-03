package android.content.res;

public final class ResourceId
{
  public static final int ID_NULL = 0;
  
  public ResourceId() {}
  
  public static boolean isValid(int paramInt)
  {
    boolean bool;
    if ((paramInt != -1) && ((0xFF000000 & paramInt) != 0) && ((0xFF0000 & paramInt) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
