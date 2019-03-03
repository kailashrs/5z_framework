package android.graphics;

public class TableMaskFilter
  extends MaskFilter
{
  private TableMaskFilter(long paramLong)
  {
    native_instance = paramLong;
  }
  
  public TableMaskFilter(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length >= 256)
    {
      native_instance = nativeNewTable(paramArrayOfByte);
      return;
    }
    throw new RuntimeException("table.length must be >= 256");
  }
  
  public static TableMaskFilter CreateClipTable(int paramInt1, int paramInt2)
  {
    return new TableMaskFilter(nativeNewClip(paramInt1, paramInt2));
  }
  
  public static TableMaskFilter CreateGammaTable(float paramFloat)
  {
    return new TableMaskFilter(nativeNewGamma(paramFloat));
  }
  
  private static native long nativeNewClip(int paramInt1, int paramInt2);
  
  private static native long nativeNewGamma(float paramFloat);
  
  private static native long nativeNewTable(byte[] paramArrayOfByte);
}
