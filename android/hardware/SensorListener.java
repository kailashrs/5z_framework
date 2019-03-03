package android.hardware;

@Deprecated
public abstract interface SensorListener
{
  public abstract void onAccuracyChanged(int paramInt1, int paramInt2);
  
  public abstract void onSensorChanged(int paramInt, float[] paramArrayOfFloat);
}
