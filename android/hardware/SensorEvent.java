package android.hardware;

public class SensorEvent
{
  public int accuracy;
  public Sensor sensor;
  public long timestamp;
  public final float[] values;
  
  SensorEvent(int paramInt)
  {
    values = new float[paramInt];
  }
}
