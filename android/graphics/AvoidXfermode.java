package android.graphics;

@Deprecated
public class AvoidXfermode
  extends Xfermode
{
  public AvoidXfermode(int paramInt1, int paramInt2, Mode paramMode)
  {
    if ((paramInt2 >= 0) && (paramInt2 <= 255)) {
      return;
    }
    throw new IllegalArgumentException("tolerance must be 0..255");
  }
  
  public static enum Mode
  {
    AVOID(0),  TARGET(1);
    
    final int nativeInt;
    
    private Mode(int paramInt)
    {
      nativeInt = paramInt;
    }
  }
}
