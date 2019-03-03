package android.opengl;

import java.nio.IntBuffer;

public class GLES10Ext
{
  static {}
  
  public GLES10Ext() {}
  
  private static native void _nativeClassInit();
  
  public static native int glQueryMatrixxOES(IntBuffer paramIntBuffer1, IntBuffer paramIntBuffer2);
  
  public static native int glQueryMatrixxOES(int[] paramArrayOfInt1, int paramInt1, int[] paramArrayOfInt2, int paramInt2);
}
