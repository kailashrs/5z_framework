package android.hardware;

import android.annotation.SystemApi;
import android.os.Handler;
import android.os.MemoryFile;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class SensorManager
{
  public static final int AXIS_MINUS_X = 129;
  public static final int AXIS_MINUS_Y = 130;
  public static final int AXIS_MINUS_Z = 131;
  public static final int AXIS_X = 1;
  public static final int AXIS_Y = 2;
  public static final int AXIS_Z = 3;
  @Deprecated
  public static final int DATA_X = 0;
  @Deprecated
  public static final int DATA_Y = 1;
  @Deprecated
  public static final int DATA_Z = 2;
  public static final float GRAVITY_DEATH_STAR_I = 3.5303614E-7F;
  public static final float GRAVITY_EARTH = 9.80665F;
  public static final float GRAVITY_JUPITER = 23.12F;
  public static final float GRAVITY_MARS = 3.71F;
  public static final float GRAVITY_MERCURY = 3.7F;
  public static final float GRAVITY_MOON = 1.6F;
  public static final float GRAVITY_NEPTUNE = 11.0F;
  public static final float GRAVITY_PLUTO = 0.6F;
  public static final float GRAVITY_SATURN = 8.96F;
  public static final float GRAVITY_SUN = 275.0F;
  public static final float GRAVITY_THE_ISLAND = 4.815162F;
  public static final float GRAVITY_URANUS = 8.69F;
  public static final float GRAVITY_VENUS = 8.87F;
  public static final float LIGHT_CLOUDY = 100.0F;
  public static final float LIGHT_FULLMOON = 0.25F;
  public static final float LIGHT_NO_MOON = 0.001F;
  public static final float LIGHT_OVERCAST = 10000.0F;
  public static final float LIGHT_SHADE = 20000.0F;
  public static final float LIGHT_SUNLIGHT = 110000.0F;
  public static final float LIGHT_SUNLIGHT_MAX = 120000.0F;
  public static final float LIGHT_SUNRISE = 400.0F;
  public static final float MAGNETIC_FIELD_EARTH_MAX = 60.0F;
  public static final float MAGNETIC_FIELD_EARTH_MIN = 30.0F;
  public static final float PRESSURE_STANDARD_ATMOSPHERE = 1013.25F;
  @Deprecated
  public static final int RAW_DATA_INDEX = 3;
  @Deprecated
  public static final int RAW_DATA_X = 3;
  @Deprecated
  public static final int RAW_DATA_Y = 4;
  @Deprecated
  public static final int RAW_DATA_Z = 5;
  @Deprecated
  public static final int SENSOR_ACCELEROMETER = 2;
  @Deprecated
  public static final int SENSOR_ALL = 127;
  public static final int SENSOR_DELAY_FASTEST = 0;
  public static final int SENSOR_DELAY_GAME = 1;
  public static final int SENSOR_DELAY_NORMAL = 3;
  public static final int SENSOR_DELAY_UI = 2;
  @Deprecated
  public static final int SENSOR_LIGHT = 16;
  @Deprecated
  public static final int SENSOR_MAGNETIC_FIELD = 8;
  @Deprecated
  public static final int SENSOR_MAX = 64;
  @Deprecated
  public static final int SENSOR_MIN = 1;
  @Deprecated
  public static final int SENSOR_ORIENTATION = 1;
  @Deprecated
  public static final int SENSOR_ORIENTATION_RAW = 128;
  @Deprecated
  public static final int SENSOR_PROXIMITY = 32;
  public static final int SENSOR_STATUS_ACCURACY_HIGH = 3;
  public static final int SENSOR_STATUS_ACCURACY_LOW = 1;
  public static final int SENSOR_STATUS_ACCURACY_MEDIUM = 2;
  public static final int SENSOR_STATUS_NO_CONTACT = -1;
  public static final int SENSOR_STATUS_UNRELIABLE = 0;
  @Deprecated
  public static final int SENSOR_TEMPERATURE = 4;
  @Deprecated
  public static final int SENSOR_TRICORDER = 64;
  public static final float STANDARD_GRAVITY = 9.80665F;
  protected static final String TAG = "SensorManager";
  private static final float[] sTempMatrix = new float[16];
  private LegacySensorManager mLegacySensorManager;
  private final SparseArray<List<Sensor>> mSensorListByType = new SparseArray();
  
  public SensorManager() {}
  
  public static float getAltitude(float paramFloat1, float paramFloat2)
  {
    return 44330.0F * (1.0F - (float)Math.pow(paramFloat2 / paramFloat1, 0.19029495120048523D));
  }
  
  public static void getAngleChange(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3)
  {
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    float f7 = 0.0F;
    float f8 = 0.0F;
    float f9 = 0.0F;
    float f10 = 0.0F;
    float f11 = 0.0F;
    float f12 = 0.0F;
    float f13 = 0.0F;
    float f14 = 0.0F;
    float f15 = 0.0F;
    float f16 = 0.0F;
    float f17 = 0.0F;
    float f18 = 0.0F;
    if (paramArrayOfFloat2.length == 9)
    {
      f1 = paramArrayOfFloat2[0];
      f2 = paramArrayOfFloat2[1];
      f3 = paramArrayOfFloat2[2];
      f4 = paramArrayOfFloat2[3];
      f5 = paramArrayOfFloat2[4];
      f6 = paramArrayOfFloat2[5];
      f7 = paramArrayOfFloat2[6];
      f8 = paramArrayOfFloat2[7];
      f9 = paramArrayOfFloat2[8];
    }
    else if (paramArrayOfFloat2.length == 16)
    {
      f1 = paramArrayOfFloat2[0];
      f2 = paramArrayOfFloat2[1];
      f3 = paramArrayOfFloat2[2];
      f4 = paramArrayOfFloat2[4];
      f5 = paramArrayOfFloat2[5];
      f6 = paramArrayOfFloat2[6];
      f7 = paramArrayOfFloat2[8];
      f8 = paramArrayOfFloat2[9];
      f9 = paramArrayOfFloat2[10];
    }
    if (paramArrayOfFloat3.length == 9)
    {
      f10 = paramArrayOfFloat3[0];
      f11 = paramArrayOfFloat3[1];
      f12 = paramArrayOfFloat3[2];
      f13 = paramArrayOfFloat3[3];
      f14 = paramArrayOfFloat3[4];
      f15 = paramArrayOfFloat3[5];
      f16 = paramArrayOfFloat3[6];
      f17 = paramArrayOfFloat3[7];
      f18 = paramArrayOfFloat3[8];
    }
    else if (paramArrayOfFloat3.length == 16)
    {
      f10 = paramArrayOfFloat3[0];
      f11 = paramArrayOfFloat3[1];
      f12 = paramArrayOfFloat3[2];
      f13 = paramArrayOfFloat3[4];
      f14 = paramArrayOfFloat3[5];
      f15 = paramArrayOfFloat3[6];
      f16 = paramArrayOfFloat3[8];
      f17 = paramArrayOfFloat3[9];
      f18 = paramArrayOfFloat3[10];
    }
    paramArrayOfFloat1[0] = ((float)Math.atan2(f10 * f2 + f13 * f5 + f16 * f8, f11 * f2 + f14 * f5 + f17 * f8));
    paramArrayOfFloat1[1] = ((float)Math.asin(-(f12 * f2 + f15 * f5 + f18 * f8)));
    paramArrayOfFloat1[2] = ((float)Math.atan2(-(f12 * f1 + f15 * f4 + f18 * f7), f12 * f3 + f15 * f6 + f18 * f9));
  }
  
  private static int getDelay(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 3: 
      paramInt = 200000;
      break;
    case 2: 
      paramInt = 66667;
      break;
    case 1: 
      paramInt = 20000;
      break;
    case 0: 
      paramInt = 0;
    }
    return paramInt;
  }
  
  public static float getInclination(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length == 9) {
      return (float)Math.atan2(paramArrayOfFloat[5], paramArrayOfFloat[4]);
    }
    return (float)Math.atan2(paramArrayOfFloat[6], paramArrayOfFloat[5]);
  }
  
  private LegacySensorManager getLegacySensorManager()
  {
    synchronized (mSensorListByType)
    {
      if (mLegacySensorManager == null)
      {
        Log.i("SensorManager", "This application is using deprecated SensorManager API which will be removed someday.  Please consider switching to the new API.");
        localLegacySensorManager = new android/hardware/LegacySensorManager;
        localLegacySensorManager.<init>(this);
        mLegacySensorManager = localLegacySensorManager;
      }
      LegacySensorManager localLegacySensorManager = mLegacySensorManager;
      return localLegacySensorManager;
    }
  }
  
  public static float[] getOrientation(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if (paramArrayOfFloat1.length == 9)
    {
      paramArrayOfFloat2[0] = ((float)Math.atan2(paramArrayOfFloat1[1], paramArrayOfFloat1[4]));
      paramArrayOfFloat2[1] = ((float)Math.asin(-paramArrayOfFloat1[7]));
      paramArrayOfFloat2[2] = ((float)Math.atan2(-paramArrayOfFloat1[6], paramArrayOfFloat1[8]));
    }
    else
    {
      paramArrayOfFloat2[0] = ((float)Math.atan2(paramArrayOfFloat1[1], paramArrayOfFloat1[5]));
      paramArrayOfFloat2[1] = ((float)Math.asin(-paramArrayOfFloat1[9]));
      paramArrayOfFloat2[2] = ((float)Math.atan2(-paramArrayOfFloat1[8], paramArrayOfFloat1[10]));
    }
    return paramArrayOfFloat2;
  }
  
  public static void getQuaternionFromVector(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if (paramArrayOfFloat2.length >= 4)
    {
      paramArrayOfFloat1[0] = paramArrayOfFloat2[3];
    }
    else
    {
      paramArrayOfFloat1[0] = (1.0F - paramArrayOfFloat2[0] * paramArrayOfFloat2[0] - paramArrayOfFloat2[1] * paramArrayOfFloat2[1] - paramArrayOfFloat2[2] * paramArrayOfFloat2[2]);
      float f1 = paramArrayOfFloat1[0];
      float f2 = 0.0F;
      if (f1 > 0.0F) {
        f2 = (float)Math.sqrt(paramArrayOfFloat1[0]);
      }
      paramArrayOfFloat1[0] = f2;
    }
    paramArrayOfFloat1[1] = paramArrayOfFloat2[0];
    paramArrayOfFloat1[2] = paramArrayOfFloat2[1];
    paramArrayOfFloat1[3] = paramArrayOfFloat2[2];
  }
  
  public static boolean getRotationMatrix(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
  {
    float f1 = paramArrayOfFloat3[0];
    float f2 = paramArrayOfFloat3[1];
    float f3 = paramArrayOfFloat3[2];
    if (f1 * f1 + f2 * f2 + f3 * f3 < 0.96236104F) {
      return false;
    }
    float f4 = paramArrayOfFloat4[0];
    float f5 = paramArrayOfFloat4[1];
    float f6 = paramArrayOfFloat4[2];
    float f7 = f5 * f3 - f6 * f2;
    float f8 = f6 * f1 - f4 * f3;
    float f9 = f4 * f2 - f5 * f1;
    float f10 = (float)Math.sqrt(f7 * f7 + f8 * f8 + f9 * f9);
    if (f10 < 0.1F) {
      return false;
    }
    f10 = 1.0F / f10;
    f7 *= f10;
    f8 *= f10;
    f9 *= f10;
    f10 = 1.0F / (float)Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
    f1 *= f10;
    f2 *= f10;
    f3 *= f10;
    float f11 = f2 * f9 - f3 * f8;
    float f12 = f3 * f7 - f1 * f9;
    f10 = f1 * f8 - f2 * f7;
    if (paramArrayOfFloat1 != null) {
      if (paramArrayOfFloat1.length == 9)
      {
        paramArrayOfFloat1[0] = f7;
        paramArrayOfFloat1[1] = f8;
        paramArrayOfFloat1[2] = f9;
        paramArrayOfFloat1[3] = f11;
        paramArrayOfFloat1[4] = f12;
        paramArrayOfFloat1[5] = f10;
        paramArrayOfFloat1[6] = f1;
        paramArrayOfFloat1[7] = f2;
        paramArrayOfFloat1[8] = f3;
      }
      else if (paramArrayOfFloat1.length == 16)
      {
        paramArrayOfFloat1[0] = f7;
        paramArrayOfFloat1[1] = f8;
        paramArrayOfFloat1[2] = f9;
        paramArrayOfFloat1[3] = 0.0F;
        paramArrayOfFloat1[4] = f11;
        paramArrayOfFloat1[5] = f12;
        paramArrayOfFloat1[6] = f10;
        paramArrayOfFloat1[7] = 0.0F;
        paramArrayOfFloat1[8] = f1;
        paramArrayOfFloat1[9] = f2;
        paramArrayOfFloat1[10] = f3;
        paramArrayOfFloat1[11] = 0.0F;
        paramArrayOfFloat1[12] = 0.0F;
        paramArrayOfFloat1[13] = 0.0F;
        paramArrayOfFloat1[14] = 0.0F;
        paramArrayOfFloat1[15] = 1.0F;
      }
    }
    if (paramArrayOfFloat2 != null)
    {
      f7 = 1.0F / (float)Math.sqrt(f4 * f4 + f5 * f5 + f6 * f6);
      f8 = (f4 * f11 + f5 * f12 + f6 * f10) * f7;
      f5 = (f4 * f1 + f5 * f2 + f6 * f3) * f7;
      if (paramArrayOfFloat2.length == 9)
      {
        paramArrayOfFloat2[0] = 1.0F;
        paramArrayOfFloat2[1] = 0.0F;
        paramArrayOfFloat2[2] = 0.0F;
        paramArrayOfFloat2[3] = 0.0F;
        paramArrayOfFloat2[4] = f8;
        paramArrayOfFloat2[5] = f5;
        paramArrayOfFloat2[6] = 0.0F;
        paramArrayOfFloat2[7] = (-f5);
        paramArrayOfFloat2[8] = f8;
      }
      else if (paramArrayOfFloat2.length == 16)
      {
        paramArrayOfFloat2[0] = 1.0F;
        paramArrayOfFloat2[1] = 0.0F;
        paramArrayOfFloat2[2] = 0.0F;
        paramArrayOfFloat2[4] = 0.0F;
        paramArrayOfFloat2[5] = f8;
        paramArrayOfFloat2[6] = f5;
        paramArrayOfFloat2[8] = 0.0F;
        paramArrayOfFloat2[9] = (-f5);
        paramArrayOfFloat2[10] = f8;
        paramArrayOfFloat2[14] = 0.0F;
        paramArrayOfFloat2[13] = 0.0F;
        paramArrayOfFloat2[12] = 0.0F;
        paramArrayOfFloat2[11] = 0.0F;
        paramArrayOfFloat2[7] = 0.0F;
        paramArrayOfFloat2[3] = 0.0F;
        paramArrayOfFloat2[15] = 1.0F;
      }
    }
    return true;
  }
  
  public static void getRotationMatrixFromVector(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f1 = paramArrayOfFloat2[0];
    float f2 = paramArrayOfFloat2[1];
    float f3 = paramArrayOfFloat2[2];
    if (paramArrayOfFloat2.length >= 4)
    {
      f4 = paramArrayOfFloat2[3];
    }
    else
    {
      f4 = 1.0F - f1 * f1 - f2 * f2 - f3 * f3;
      if (f4 > 0.0F) {
        f4 = (float)Math.sqrt(f4);
      } else {
        f4 = 0.0F;
      }
    }
    float f5 = 2.0F * f1 * f1;
    float f6 = 2.0F * f2 * f2;
    float f7 = 2.0F * f3 * f3;
    float f8 = 2.0F * f1 * f2;
    float f9 = 2.0F * f3 * f4;
    float f10 = 2.0F * f1 * f3;
    float f11 = 2.0F * f2 * f4;
    f2 = 2.0F * f2 * f3;
    float f4 = 2.0F * f1 * f4;
    if (paramArrayOfFloat1.length == 9)
    {
      paramArrayOfFloat1[0] = (1.0F - f6 - f7);
      paramArrayOfFloat1[1] = (f8 - f9);
      paramArrayOfFloat1[2] = (f10 + f11);
      paramArrayOfFloat1[3] = (f8 + f9);
      paramArrayOfFloat1[4] = (1.0F - f5 - f7);
      paramArrayOfFloat1[5] = (f2 - f4);
      paramArrayOfFloat1[6] = (f10 - f11);
      paramArrayOfFloat1[7] = (f2 + f4);
      paramArrayOfFloat1[8] = (1.0F - f5 - f6);
    }
    else if (paramArrayOfFloat1.length == 16)
    {
      paramArrayOfFloat1[0] = (1.0F - f6 - f7);
      paramArrayOfFloat1[1] = (f8 - f9);
      paramArrayOfFloat1[2] = (f10 + f11);
      paramArrayOfFloat1[3] = 0.0F;
      paramArrayOfFloat1[4] = (f8 + f9);
      paramArrayOfFloat1[5] = (1.0F - f5 - f7);
      paramArrayOfFloat1[6] = (f2 - f4);
      paramArrayOfFloat1[7] = 0.0F;
      paramArrayOfFloat1[8] = (f10 - f11);
      paramArrayOfFloat1[9] = (f2 + f4);
      paramArrayOfFloat1[10] = (1.0F - f5 - f6);
      paramArrayOfFloat1[11] = 0.0F;
      paramArrayOfFloat1[14] = 0.0F;
      paramArrayOfFloat1[13] = 0.0F;
      paramArrayOfFloat1[12] = 0.0F;
      paramArrayOfFloat1[15] = 1.0F;
    }
  }
  
  public static boolean remapCoordinateSystem(float[] paramArrayOfFloat1, int paramInt1, int paramInt2, float[] paramArrayOfFloat2)
  {
    if (paramArrayOfFloat1 == paramArrayOfFloat2) {
      synchronized (sTempMatrix)
      {
        if (remapCoordinateSystemImpl(paramArrayOfFloat1, paramInt1, paramInt2, ???))
        {
          paramInt2 = paramArrayOfFloat2.length;
          for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
            paramArrayOfFloat2[paramInt1] = ???[paramInt1];
          }
          return true;
        }
      }
    }
    return remapCoordinateSystemImpl(paramArrayOfFloat1, paramInt1, paramInt2, paramArrayOfFloat2);
  }
  
  private static boolean remapCoordinateSystemImpl(float[] paramArrayOfFloat1, int paramInt1, int paramInt2, float[] paramArrayOfFloat2)
  {
    int i = paramArrayOfFloat2.length;
    if (paramArrayOfFloat1.length != i) {
      return false;
    }
    if (((paramInt1 & 0x7C) == 0) && ((paramInt2 & 0x7C) == 0))
    {
      if (((paramInt1 & 0x3) != 0) && ((paramInt2 & 0x3) != 0))
      {
        if ((paramInt1 & 0x3) == (paramInt2 & 0x3)) {
          return false;
        }
        int j = paramInt1 ^ paramInt2;
        int k = (paramInt1 & 0x3) - 1;
        int m = (paramInt2 & 0x3) - 1;
        int n = (j & 0x3) - 1;
        int i1 = j;
        if ((k ^ (n + 1) % 3 | m ^ (n + 2) % 3) != 0) {
          i1 = j ^ 0x80;
        }
        if (paramInt1 >= 128) {
          paramInt1 = 1;
        } else {
          paramInt1 = 0;
        }
        if (paramInt2 >= 128) {
          paramInt2 = 1;
        } else {
          paramInt2 = 0;
        }
        if (i1 >= 128) {
          i1 = 1;
        } else {
          i1 = 0;
        }
        if (i == 16) {
          j = 4;
        } else {
          j = 3;
        }
        for (int i2 = 0; i2 < 3; i2++)
        {
          int i3 = i2 * j;
          for (int i4 = 0; i4 < 3; i4++)
          {
            float f;
            if (k == i4)
            {
              if (paramInt1 != 0) {
                f = -paramArrayOfFloat1[(i3 + 0)];
              } else {
                f = paramArrayOfFloat1[(i3 + 0)];
              }
              paramArrayOfFloat2[(i3 + i4)] = f;
            }
            if (m == i4)
            {
              if (paramInt2 != 0) {
                f = -paramArrayOfFloat1[(i3 + 1)];
              } else {
                f = paramArrayOfFloat1[(i3 + 1)];
              }
              paramArrayOfFloat2[(i3 + i4)] = f;
            }
            if (n == i4)
            {
              if (i1 != 0) {
                f = -paramArrayOfFloat1[(i3 + 2)];
              } else {
                f = paramArrayOfFloat1[(i3 + 2)];
              }
              paramArrayOfFloat2[(i3 + i4)] = f;
            }
          }
        }
        if (i == 16)
        {
          paramArrayOfFloat2[14] = 0.0F;
          paramArrayOfFloat2[13] = 0.0F;
          paramArrayOfFloat2[12] = 0.0F;
          paramArrayOfFloat2[11] = 0.0F;
          paramArrayOfFloat2[7] = 0.0F;
          paramArrayOfFloat2[3] = 0.0F;
          paramArrayOfFloat2[15] = 1.0F;
        }
        return true;
      }
      return false;
    }
    return false;
  }
  
  public boolean cancelTriggerSensor(TriggerEventListener paramTriggerEventListener, Sensor paramSensor)
  {
    return cancelTriggerSensorImpl(paramTriggerEventListener, paramSensor, true);
  }
  
  protected abstract boolean cancelTriggerSensorImpl(TriggerEventListener paramTriggerEventListener, Sensor paramSensor, boolean paramBoolean);
  
  @Deprecated
  public int configureDirectChannel(SensorDirectChannel paramSensorDirectChannel, Sensor paramSensor, int paramInt)
  {
    return configureDirectChannelImpl(paramSensorDirectChannel, paramSensor, paramInt);
  }
  
  protected abstract int configureDirectChannelImpl(SensorDirectChannel paramSensorDirectChannel, Sensor paramSensor, int paramInt);
  
  public SensorDirectChannel createDirectChannel(HardwareBuffer paramHardwareBuffer)
  {
    return createDirectChannelImpl(null, paramHardwareBuffer);
  }
  
  public SensorDirectChannel createDirectChannel(MemoryFile paramMemoryFile)
  {
    return createDirectChannelImpl(paramMemoryFile, null);
  }
  
  protected abstract SensorDirectChannel createDirectChannelImpl(MemoryFile paramMemoryFile, HardwareBuffer paramHardwareBuffer);
  
  void destroyDirectChannel(SensorDirectChannel paramSensorDirectChannel)
  {
    destroyDirectChannelImpl(paramSensorDirectChannel);
  }
  
  protected abstract void destroyDirectChannelImpl(SensorDirectChannel paramSensorDirectChannel);
  
  public boolean flush(SensorEventListener paramSensorEventListener)
  {
    return flushImpl(paramSensorEventListener);
  }
  
  protected abstract boolean flushImpl(SensorEventListener paramSensorEventListener);
  
  public Sensor getDefaultSensor(int paramInt)
  {
    Object localObject = getSensorList(paramInt);
    int i = 0;
    if ((paramInt == 8) || (paramInt == 17) || (paramInt == 22) || (paramInt == 23) || (paramInt == 24) || (paramInt == 25) || (paramInt == 26) || (paramInt == 32)) {
      i = 1;
    }
    Iterator localIterator = ((List)localObject).iterator();
    while (localIterator.hasNext())
    {
      localObject = (Sensor)localIterator.next();
      if (((Sensor)localObject).isWakeUpSensor() == i) {
        return localObject;
      }
    }
    return null;
  }
  
  public Sensor getDefaultSensor(int paramInt, boolean paramBoolean)
  {
    Iterator localIterator = getSensorList(paramInt).iterator();
    while (localIterator.hasNext())
    {
      Sensor localSensor = (Sensor)localIterator.next();
      if (localSensor.isWakeUpSensor() == paramBoolean) {
        return localSensor;
      }
    }
    return null;
  }
  
  public List<Sensor> getDynamicSensorList(int paramInt)
  {
    Object localObject = getFullDynamicSensorList();
    if (paramInt == -1) {
      return Collections.unmodifiableList((List)localObject);
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = ((List)localObject).iterator();
    while (localIterator.hasNext())
    {
      localObject = (Sensor)localIterator.next();
      if (((Sensor)localObject).getType() == paramInt) {
        localArrayList.add(localObject);
      }
    }
    return Collections.unmodifiableList(localArrayList);
  }
  
  protected abstract List<Sensor> getFullDynamicSensorList();
  
  protected abstract List<Sensor> getFullSensorList();
  
  public List<Sensor> getSensorList(int paramInt)
  {
    Object localObject1 = getFullSensorList();
    synchronized (mSensorListByType)
    {
      Object localObject2 = (List)mSensorListByType.get(paramInt);
      Object localObject3 = localObject2;
      if (localObject2 == null)
      {
        if (paramInt != -1)
        {
          localObject3 = new java/util/ArrayList;
          ((ArrayList)localObject3).<init>();
          localObject2 = ((List)localObject1).iterator();
          for (;;)
          {
            localObject1 = localObject3;
            if (!((Iterator)localObject2).hasNext()) {
              break;
            }
            localObject1 = (Sensor)((Iterator)localObject2).next();
            if (((Sensor)localObject1).getType() == paramInt) {
              ((List)localObject3).add(localObject1);
            }
          }
        }
        localObject3 = Collections.unmodifiableList((List)localObject1);
        mSensorListByType.append(paramInt, localObject3);
      }
      return localObject3;
    }
  }
  
  @Deprecated
  public int getSensors()
  {
    return getLegacySensorManager().getSensors();
  }
  
  @SystemApi
  public boolean initDataInjection(boolean paramBoolean)
  {
    return initDataInjectionImpl(paramBoolean);
  }
  
  protected abstract boolean initDataInjectionImpl(boolean paramBoolean);
  
  @SystemApi
  public boolean injectSensorData(Sensor paramSensor, float[] paramArrayOfFloat, int paramInt, long paramLong)
  {
    if (paramSensor != null)
    {
      if (paramSensor.isDataInjectionSupported())
      {
        if (paramArrayOfFloat != null)
        {
          int i = Sensor.getMaxLengthValuesArray(paramSensor, 23);
          if (paramArrayOfFloat.length == i)
          {
            if ((paramInt >= -1) && (paramInt <= 3))
            {
              if (paramLong > 0L) {
                return injectSensorDataImpl(paramSensor, paramArrayOfFloat, paramInt, paramLong);
              }
              throw new IllegalArgumentException("Negative or zero sensor timestamp");
            }
            throw new IllegalArgumentException("Invalid sensor accuracy");
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Wrong number of values for sensor ");
          localStringBuilder.append(paramSensor.getName());
          localStringBuilder.append(" actual=");
          localStringBuilder.append(paramArrayOfFloat.length);
          localStringBuilder.append(" expected=");
          localStringBuilder.append(i);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        throw new IllegalArgumentException("sensor data cannot be null");
      }
      throw new IllegalArgumentException("sensor does not support data injection");
    }
    throw new IllegalArgumentException("sensor cannot be null");
  }
  
  protected abstract boolean injectSensorDataImpl(Sensor paramSensor, float[] paramArrayOfFloat, int paramInt, long paramLong);
  
  public boolean isDynamicSensorDiscoverySupported()
  {
    boolean bool;
    if (getSensorList(32).size() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void registerDynamicSensorCallback(DynamicSensorCallback paramDynamicSensorCallback)
  {
    registerDynamicSensorCallback(paramDynamicSensorCallback, null);
  }
  
  public void registerDynamicSensorCallback(DynamicSensorCallback paramDynamicSensorCallback, Handler paramHandler)
  {
    registerDynamicSensorCallbackImpl(paramDynamicSensorCallback, paramHandler);
  }
  
  protected abstract void registerDynamicSensorCallbackImpl(DynamicSensorCallback paramDynamicSensorCallback, Handler paramHandler);
  
  public boolean registerListener(SensorEventListener paramSensorEventListener, Sensor paramSensor, int paramInt)
  {
    return registerListener(paramSensorEventListener, paramSensor, paramInt, null);
  }
  
  public boolean registerListener(SensorEventListener paramSensorEventListener, Sensor paramSensor, int paramInt1, int paramInt2)
  {
    return registerListenerImpl(paramSensorEventListener, paramSensor, getDelay(paramInt1), null, paramInt2, 0);
  }
  
  public boolean registerListener(SensorEventListener paramSensorEventListener, Sensor paramSensor, int paramInt1, int paramInt2, Handler paramHandler)
  {
    return registerListenerImpl(paramSensorEventListener, paramSensor, getDelay(paramInt1), paramHandler, paramInt2, 0);
  }
  
  public boolean registerListener(SensorEventListener paramSensorEventListener, Sensor paramSensor, int paramInt, Handler paramHandler)
  {
    return registerListenerImpl(paramSensorEventListener, paramSensor, getDelay(paramInt), paramHandler, 0, 0);
  }
  
  @Deprecated
  public boolean registerListener(SensorListener paramSensorListener, int paramInt)
  {
    return registerListener(paramSensorListener, paramInt, 3);
  }
  
  @Deprecated
  public boolean registerListener(SensorListener paramSensorListener, int paramInt1, int paramInt2)
  {
    return getLegacySensorManager().registerListener(paramSensorListener, paramInt1, paramInt2);
  }
  
  protected abstract boolean registerListenerImpl(SensorEventListener paramSensorEventListener, Sensor paramSensor, int paramInt1, Handler paramHandler, int paramInt2, int paramInt3);
  
  public boolean requestTriggerSensor(TriggerEventListener paramTriggerEventListener, Sensor paramSensor)
  {
    return requestTriggerSensorImpl(paramTriggerEventListener, paramSensor);
  }
  
  protected abstract boolean requestTriggerSensorImpl(TriggerEventListener paramTriggerEventListener, Sensor paramSensor);
  
  public boolean setOperationParameter(SensorAdditionalInfo paramSensorAdditionalInfo)
  {
    return setOperationParameterImpl(paramSensorAdditionalInfo);
  }
  
  protected abstract boolean setOperationParameterImpl(SensorAdditionalInfo paramSensorAdditionalInfo);
  
  public void unregisterDynamicSensorCallback(DynamicSensorCallback paramDynamicSensorCallback)
  {
    unregisterDynamicSensorCallbackImpl(paramDynamicSensorCallback);
  }
  
  protected abstract void unregisterDynamicSensorCallbackImpl(DynamicSensorCallback paramDynamicSensorCallback);
  
  public void unregisterListener(SensorEventListener paramSensorEventListener)
  {
    if (paramSensorEventListener == null) {
      return;
    }
    unregisterListenerImpl(paramSensorEventListener, null);
  }
  
  public void unregisterListener(SensorEventListener paramSensorEventListener, Sensor paramSensor)
  {
    if ((paramSensorEventListener != null) && (paramSensor != null))
    {
      unregisterListenerImpl(paramSensorEventListener, paramSensor);
      return;
    }
  }
  
  @Deprecated
  public void unregisterListener(SensorListener paramSensorListener)
  {
    unregisterListener(paramSensorListener, 255);
  }
  
  @Deprecated
  public void unregisterListener(SensorListener paramSensorListener, int paramInt)
  {
    getLegacySensorManager().unregisterListener(paramSensorListener, paramInt);
  }
  
  protected abstract void unregisterListenerImpl(SensorEventListener paramSensorEventListener, Sensor paramSensor);
  
  public static abstract class DynamicSensorCallback
  {
    public DynamicSensorCallback() {}
    
    public void onDynamicSensorConnected(Sensor paramSensor) {}
    
    public void onDynamicSensorDisconnected(Sensor paramSensor) {}
  }
}
