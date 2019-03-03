package android.hardware;

public abstract class SensorEventCallback
  implements SensorEventListener2
{
  public SensorEventCallback() {}
  
  public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}
  
  public void onFlushCompleted(Sensor paramSensor) {}
  
  public void onSensorAdditionalInfo(SensorAdditionalInfo paramSensorAdditionalInfo) {}
  
  public void onSensorChanged(SensorEvent paramSensorEvent) {}
}
