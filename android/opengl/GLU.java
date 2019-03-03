package android.opengl;

import javax.microedition.khronos.opengles.GL10;

public class GLU
{
  private static final float[] sScratch = new float[32];
  
  public GLU() {}
  
  public static String gluErrorString(int paramInt)
  {
    if (paramInt != 0)
    {
      switch (paramInt)
      {
      default: 
        return null;
      case 1285: 
        return "out of memory";
      case 1284: 
        return "stack underflow";
      case 1283: 
        return "stack overflow";
      case 1282: 
        return "invalid operation";
      case 1281: 
        return "invalid value";
      }
      return "invalid enum";
    }
    return "no error";
  }
  
  public static void gluLookAt(GL10 paramGL10, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    synchronized (sScratch)
    {
      Matrix.setLookAtM(???, 0, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9);
      try
      {
        paramGL10.glMultMatrixf(???, 0);
        return;
      }
      finally {}
    }
  }
  
  public static void gluOrtho2D(GL10 paramGL10, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    paramGL10.glOrthof(paramFloat1, paramFloat2, paramFloat3, paramFloat4, -1.0F, 1.0F);
  }
  
  public static void gluPerspective(GL10 paramGL10, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    paramFloat1 = (float)Math.tan(paramFloat1 * 0.008726646259971648D) * paramFloat3;
    float f = -paramFloat1;
    paramGL10.glFrustumf(f * paramFloat2, paramFloat1 * paramFloat2, f, paramFloat1, paramFloat3, paramFloat4);
  }
  
  public static int gluProject(float paramFloat1, float paramFloat2, float paramFloat3, float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, int[] paramArrayOfInt, int paramInt3, float[] paramArrayOfFloat3, int paramInt4)
  {
    synchronized (sScratch)
    {
      Matrix.multiplyMM(???, 0, paramArrayOfFloat2, paramInt2, paramArrayOfFloat1, paramInt1);
      ???[16] = paramFloat1;
      ???[17] = paramFloat2;
      ???[18] = paramFloat3;
      ???[19] = 1.0F;
      Matrix.multiplyMV(???, 20, ???, 0, ???, 16);
      paramFloat1 = ???[23];
      if (paramFloat1 == 0.0F) {
        return 0;
      }
      paramFloat1 = 1.0F / paramFloat1;
      paramArrayOfFloat3[paramInt4] = (paramArrayOfInt[paramInt3] + paramArrayOfInt[(paramInt3 + 2)] * (???[20] * paramFloat1 + 1.0F) * 0.5F);
      paramArrayOfFloat3[(paramInt4 + 1)] = (paramArrayOfInt[(paramInt3 + 1)] + paramArrayOfInt[(paramInt3 + 3)] * (???[21] * paramFloat1 + 1.0F) * 0.5F);
      paramArrayOfFloat3[(paramInt4 + 2)] = ((???[22] * paramFloat1 + 1.0F) * 0.5F);
      return 1;
    }
  }
  
  public static int gluUnProject(float paramFloat1, float paramFloat2, float paramFloat3, float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, int[] paramArrayOfInt, int paramInt3, float[] paramArrayOfFloat3, int paramInt4)
  {
    synchronized (sScratch)
    {
      Matrix.multiplyMM(???, 0, paramArrayOfFloat2, paramInt2, paramArrayOfFloat1, paramInt1);
      if (!Matrix.invertM(???, 16, ???, 0)) {
        return 0;
      }
      ???[0] = ((paramFloat1 - paramArrayOfInt[(paramInt3 + 0)]) * 2.0F / paramArrayOfInt[(paramInt3 + 2)] - 1.0F);
      ???[1] = ((paramFloat2 - paramArrayOfInt[(paramInt3 + 1)]) * 2.0F / paramArrayOfInt[(paramInt3 + 3)] - 1.0F);
      ???[2] = (2.0F * paramFloat3 - 1.0F);
      ???[3] = 1.0F;
      Matrix.multiplyMV(paramArrayOfFloat3, paramInt4, ???, 16, ???, 0);
      return 1;
    }
  }
}
