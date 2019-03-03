package android.opengl;

public class Visibility
{
  public Visibility() {}
  
  public static native void computeBoundingSphere(float[] paramArrayOfFloat1, int paramInt1, int paramInt2, float[] paramArrayOfFloat2, int paramInt3);
  
  public static native int frustumCullSpheres(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4, int paramInt5);
  
  public static native int visibilityTest(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4);
}
