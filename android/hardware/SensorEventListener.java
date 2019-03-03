package android.hardware;

public abstract interface SensorEventListener
{
  public abstract void onAccuracyChanged(Sensor paramSensor, int paramInt);
  
  public abstract void onSensorChanged(SensorEvent paramSensorEvent);
}
