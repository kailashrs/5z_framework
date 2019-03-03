package android.hardware;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SensorAdditionalInfo
{
  public static final int TYPE_CUSTOM_INFO = 268435456;
  public static final int TYPE_DEBUG_INFO = 1073741824;
  public static final int TYPE_DOCK_STATE = 196610;
  public static final int TYPE_FRAME_BEGIN = 0;
  public static final int TYPE_FRAME_END = 1;
  public static final int TYPE_HIGH_PERFORMANCE_MODE = 196611;
  public static final int TYPE_INTERNAL_TEMPERATURE = 65537;
  public static final int TYPE_LOCAL_GEOMAGNETIC_FIELD = 196608;
  public static final int TYPE_LOCAL_GRAVITY = 196609;
  public static final int TYPE_MAGNETIC_FIELD_CALIBRATION = 196612;
  public static final int TYPE_SAMPLING = 65540;
  public static final int TYPE_SENSOR_PLACEMENT = 65539;
  public static final int TYPE_UNTRACKED_DELAY = 65536;
  public static final int TYPE_VEC3_CALIBRATION = 65538;
  public final float[] floatValues;
  public final int[] intValues;
  public final Sensor sensor;
  public final int serial;
  public final int type;
  
  SensorAdditionalInfo(Sensor paramSensor, int paramInt1, int paramInt2, int[] paramArrayOfInt, float[] paramArrayOfFloat)
  {
    sensor = paramSensor;
    type = paramInt1;
    serial = paramInt2;
    intValues = paramArrayOfInt;
    floatValues = paramArrayOfFloat;
  }
  
  public static SensorAdditionalInfo createCustomInfo(Sensor paramSensor, int paramInt, float[] paramArrayOfFloat)
  {
    if ((paramInt >= 268435456) && (paramInt < 1073741824) && (paramSensor != null)) {
      return new SensorAdditionalInfo(paramSensor, paramInt, 0, null, paramArrayOfFloat);
    }
    paramArrayOfFloat = new StringBuilder();
    paramArrayOfFloat.append("invalid parameter(s): type: ");
    paramArrayOfFloat.append(paramInt);
    paramArrayOfFloat.append("; sensor: ");
    paramArrayOfFloat.append(paramSensor);
    throw new IllegalArgumentException(paramArrayOfFloat.toString());
  }
  
  public static SensorAdditionalInfo createLocalGeomagneticField(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if ((paramFloat1 >= 10.0F) && (paramFloat1 <= 100.0F) && (paramFloat2 >= 0.0F) && (paramFloat2 <= 3.141592653589793D) && (paramFloat3 >= -1.5707963267948966D) && (paramFloat3 <= 1.5707963267948966D)) {
      return new SensorAdditionalInfo(null, 196608, 0, null, new float[] { paramFloat1, paramFloat2, paramFloat3 });
    }
    throw new IllegalArgumentException("Geomagnetic field info out of range");
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AdditionalInfoType {}
}
