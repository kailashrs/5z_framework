package android.gesture;

import java.util.ArrayList;

class Instance
{
  private static final float[] ORIENTATIONS = { 0.0F, 0.7853982F, 1.5707964F, 2.3561945F, 3.1415927F, 0.0F, -0.7853982F, -1.5707964F, -2.3561945F, -3.1415927F };
  private static final int PATCH_SAMPLE_SIZE = 16;
  private static final int SEQUENCE_SAMPLE_SIZE = 16;
  final long id;
  final String label;
  final float[] vector;
  
  private Instance(long paramLong, float[] paramArrayOfFloat, String paramString)
  {
    id = paramLong;
    vector = paramArrayOfFloat;
    label = paramString;
  }
  
  static Instance createInstance(int paramInt1, int paramInt2, Gesture paramGesture, String paramString)
  {
    float[] arrayOfFloat;
    if (paramInt1 == 2)
    {
      arrayOfFloat = temporalSampler(paramInt2, paramGesture);
      paramGesture = new Instance(paramGesture.getID(), arrayOfFloat, paramString);
      paramGesture.normalize();
    }
    else
    {
      arrayOfFloat = spatialSampler(paramGesture);
      paramGesture = new Instance(paramGesture.getID(), arrayOfFloat, paramString);
    }
    return paramGesture;
  }
  
  private void normalize()
  {
    float[] arrayOfFloat = vector;
    int i = arrayOfFloat.length;
    int j = 0;
    float f = 0.0F;
    for (int k = 0; k < i; k++) {
      f += arrayOfFloat[k] * arrayOfFloat[k];
    }
    f = (float)Math.sqrt(f);
    for (k = j; k < i; k++) {
      arrayOfFloat[k] /= f;
    }
  }
  
  private static float[] spatialSampler(Gesture paramGesture)
  {
    return GestureUtils.spatialSampling(paramGesture, 16, false);
  }
  
  private static float[] temporalSampler(int paramInt, Gesture paramGesture)
  {
    float[] arrayOfFloat = GestureUtils.temporalSampling((GestureStroke)paramGesture.getStrokes().get(0), 16);
    paramGesture = GestureUtils.computeCentroid(arrayOfFloat);
    float f1 = (float)Math.atan2(arrayOfFloat[1] - paramGesture[1], arrayOfFloat[0] - paramGesture[0]);
    float f2 = -f1;
    float f3 = f2;
    if (paramInt != 1)
    {
      int i = ORIENTATIONS.length;
      paramInt = 0;
      while (paramInt < i)
      {
        float f4 = ORIENTATIONS[paramInt] - f1;
        f3 = f2;
        if (Math.abs(f4) < Math.abs(f2)) {
          f3 = f4;
        }
        paramInt++;
        f2 = f3;
      }
      f3 = f2;
    }
    GestureUtils.translate(arrayOfFloat, -paramGesture[0], -paramGesture[1]);
    GestureUtils.rotate(arrayOfFloat, f3);
    return arrayOfFloat;
  }
}
