package android.hardware;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.IRotationWatcher.Stub;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

final class LegacySensorManager
{
  private static boolean sInitialized;
  private static int sRotation = 0;
  private static IWindowManager sWindowManager;
  private final HashMap<SensorListener, LegacyListener> mLegacyListenersMap = new HashMap();
  private final SensorManager mSensorManager;
  
  public LegacySensorManager(SensorManager paramSensorManager)
  {
    mSensorManager = paramSensorManager;
    try
    {
      if (!sInitialized)
      {
        sWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
        paramSensorManager = sWindowManager;
        if (paramSensorManager != null) {
          try
          {
            IWindowManager localIWindowManager = sWindowManager;
            paramSensorManager = new android/hardware/LegacySensorManager$1;
            paramSensorManager.<init>(this);
            sRotation = localIWindowManager.watchRotation(paramSensorManager, 0);
          }
          catch (RemoteException paramSensorManager) {}
        }
      }
      return;
    }
    finally {}
  }
  
  static int getRotation()
  {
    try
    {
      int i = sRotation;
      return i;
    }
    finally {}
  }
  
  static void onRotationChanged(int paramInt)
  {
    try
    {
      sRotation = paramInt;
      return;
    }
    finally {}
  }
  
  private boolean registerLegacyListener(int paramInt1, int paramInt2, SensorListener paramSensorListener, int paramInt3, int paramInt4)
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if ((paramInt3 & paramInt1) != 0)
    {
      Sensor localSensor = mSensorManager.getDefaultSensor(paramInt2);
      bool2 = bool1;
      if (localSensor != null) {
        synchronized (mLegacyListenersMap)
        {
          LegacyListener localLegacyListener1 = (LegacyListener)mLegacyListenersMap.get(paramSensorListener);
          LegacyListener localLegacyListener2 = localLegacyListener1;
          if (localLegacyListener1 == null)
          {
            localLegacyListener2 = new android/hardware/LegacySensorManager$LegacyListener;
            localLegacyListener2.<init>(paramSensorListener);
            mLegacyListenersMap.put(paramSensorListener, localLegacyListener2);
          }
          if (localLegacyListener2.registerSensor(paramInt1)) {
            bool2 = mSensorManager.registerListener(localLegacyListener2, localSensor, paramInt4);
          } else {
            bool2 = true;
          }
        }
      }
    }
    return bool2;
  }
  
  private void unregisterLegacyListener(int paramInt1, int paramInt2, SensorListener paramSensorListener, int paramInt3)
  {
    if ((paramInt3 & paramInt1) != 0)
    {
      Sensor localSensor = mSensorManager.getDefaultSensor(paramInt2);
      if (localSensor != null) {
        synchronized (mLegacyListenersMap)
        {
          LegacyListener localLegacyListener = (LegacyListener)mLegacyListenersMap.get(paramSensorListener);
          if ((localLegacyListener != null) && (localLegacyListener.unregisterSensor(paramInt1)))
          {
            mSensorManager.unregisterListener(localLegacyListener, localSensor);
            if (!localLegacyListener.hasSensors()) {
              mLegacyListenersMap.remove(paramSensorListener);
            }
          }
        }
      }
    }
  }
  
  public int getSensors()
  {
    int i = 0;
    Iterator localIterator = mSensorManager.getFullSensorList().iterator();
    while (localIterator.hasNext()) {
      switch (((Sensor)localIterator.next()).getType())
      {
      default: 
        break;
      case 3: 
        i |= 0x81;
        break;
      case 2: 
        i |= 0x8;
        break;
      case 1: 
        i |= 0x2;
      }
    }
    return i;
  }
  
  public boolean registerListener(SensorListener paramSensorListener, int paramInt1, int paramInt2)
  {
    boolean bool = false;
    if (paramSensorListener == null) {
      return false;
    }
    int i;
    if ((!registerLegacyListener(2, 1, paramSensorListener, paramInt1, paramInt2)) && (0 == 0)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((!registerLegacyListener(8, 2, paramSensorListener, paramInt1, paramInt2)) && (i == 0)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((!registerLegacyListener(128, 3, paramSensorListener, paramInt1, paramInt2)) && (i == 0)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((!registerLegacyListener(1, 3, paramSensorListener, paramInt1, paramInt2)) && (i == 0)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((!registerLegacyListener(4, 7, paramSensorListener, paramInt1, paramInt2)) && (i == 0)) {
      break label154;
    }
    bool = true;
    label154:
    return bool;
  }
  
  public void unregisterListener(SensorListener paramSensorListener, int paramInt)
  {
    if (paramSensorListener == null) {
      return;
    }
    unregisterLegacyListener(2, 1, paramSensorListener, paramInt);
    unregisterLegacyListener(8, 2, paramSensorListener, paramInt);
    unregisterLegacyListener(128, 3, paramSensorListener, paramInt);
    unregisterLegacyListener(1, 3, paramSensorListener, paramInt);
    unregisterLegacyListener(4, 7, paramSensorListener, paramInt);
  }
  
  private static final class LegacyListener
    implements SensorEventListener
  {
    private int mSensors;
    private SensorListener mTarget;
    private float[] mValues = new float[6];
    private final LegacySensorManager.LmsFilter mYawfilter = new LegacySensorManager.LmsFilter();
    
    LegacyListener(SensorListener paramSensorListener)
    {
      mTarget = paramSensorListener;
      mSensors = 0;
    }
    
    private static int getLegacySensorType(int paramInt)
    {
      if (paramInt != 7)
      {
        switch (paramInt)
        {
        default: 
          return 0;
        case 3: 
          return 128;
        case 2: 
          return 8;
        }
        return 2;
      }
      return 4;
    }
    
    private static boolean hasOrientationSensor(int paramInt)
    {
      boolean bool;
      if ((paramInt & 0x81) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void mapSensorDataToWindow(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
    {
      float f1 = paramArrayOfFloat[0];
      float f2 = paramArrayOfFloat[1];
      float f3 = paramArrayOfFloat[2];
      if (paramInt1 != 8)
      {
        if (paramInt1 != 128) {
          switch (paramInt1)
          {
          default: 
            break;
          case 2: 
            f1 = -f1;
            f2 = -f2;
            f3 = -f3;
            break;
          }
        } else {
          f3 = -f3;
        }
      }
      else
      {
        f1 = -f1;
        f2 = -f2;
      }
      paramArrayOfFloat[0] = f1;
      paramArrayOfFloat[1] = f2;
      paramArrayOfFloat[2] = f3;
      paramArrayOfFloat[3] = f1;
      paramArrayOfFloat[4] = f2;
      paramArrayOfFloat[5] = f3;
      if ((paramInt2 & 0x1) != 0) {
        if (paramInt1 != 8)
        {
          if (paramInt1 != 128) {}
          switch (paramInt1)
          {
          default: 
            break;
          case 1: 
            int i;
            if (f1 < 270.0F) {
              i = 90;
            } else {
              i = 65266;
            }
            paramArrayOfFloat[0] = (i + f1);
            paramArrayOfFloat[1] = f3;
            paramArrayOfFloat[2] = f2;
            break;
          }
        }
        else
        {
          paramArrayOfFloat[0] = (-f2);
          paramArrayOfFloat[1] = f1;
          paramArrayOfFloat[2] = f3;
        }
      }
      if ((paramInt2 & 0x2) != 0)
      {
        f1 = paramArrayOfFloat[0];
        f3 = paramArrayOfFloat[1];
        f2 = paramArrayOfFloat[2];
        if (paramInt1 != 8)
        {
          if (paramInt1 != 128) {}
          switch (paramInt1)
          {
          default: 
            break;
          case 1: 
            if (f1 >= 180.0F) {
              f1 -= 180.0F;
            } else {
              f1 = 180.0F + f1;
            }
            paramArrayOfFloat[0] = f1;
            paramArrayOfFloat[1] = (-f3);
            paramArrayOfFloat[2] = (-f2);
            break;
          }
        }
        else
        {
          paramArrayOfFloat[0] = (-f1);
          paramArrayOfFloat[1] = (-f3);
          paramArrayOfFloat[2] = f2;
        }
      }
    }
    
    boolean hasSensors()
    {
      boolean bool;
      if (mSensors != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onAccuracyChanged(Sensor paramSensor, int paramInt)
    {
      try
      {
        mTarget.onAccuracyChanged(getLegacySensorType(paramSensor.getType()), paramInt);
      }
      catch (AbstractMethodError paramSensor) {}
    }
    
    public void onSensorChanged(SensorEvent paramSensorEvent)
    {
      float[] arrayOfFloat = mValues;
      arrayOfFloat[0] = values[0];
      arrayOfFloat[1] = values[1];
      arrayOfFloat[2] = values[2];
      int i = sensor.getType();
      int j = getLegacySensorType(i);
      mapSensorDataToWindow(j, arrayOfFloat, LegacySensorManager.getRotation());
      if (i == 3)
      {
        if ((mSensors & 0x80) != 0) {
          mTarget.onSensorChanged(128, arrayOfFloat);
        }
        if ((mSensors & 0x1) != 0)
        {
          arrayOfFloat[0] = mYawfilter.filter(timestamp, arrayOfFloat[0]);
          mTarget.onSensorChanged(1, arrayOfFloat);
        }
      }
      else
      {
        mTarget.onSensorChanged(j, arrayOfFloat);
      }
    }
    
    boolean registerSensor(int paramInt)
    {
      if ((mSensors & paramInt) != 0) {
        return false;
      }
      boolean bool = hasOrientationSensor(mSensors);
      mSensors |= paramInt;
      return (!bool) || (!hasOrientationSensor(paramInt));
    }
    
    boolean unregisterSensor(int paramInt)
    {
      if ((mSensors & paramInt) == 0) {
        return false;
      }
      mSensors &= paramInt;
      return (!hasOrientationSensor(paramInt)) || (!hasOrientationSensor(mSensors));
    }
  }
  
  private static final class LmsFilter
  {
    private static final int COUNT = 12;
    private static final float PREDICTION_RATIO = 0.33333334F;
    private static final float PREDICTION_TIME = 0.08F;
    private static final int SENSORS_RATE_MS = 20;
    private int mIndex = 12;
    private long[] mT = new long[24];
    private float[] mV = new float[24];
    
    public LmsFilter() {}
    
    public float filter(long paramLong, float paramFloat)
    {
      LmsFilter localLmsFilter = this;
      float f1 = paramFloat;
      float f2 = mV[mIndex];
      if (f1 - f2 > 180.0F)
      {
        paramFloat = f1 - 360.0F;
      }
      else
      {
        paramFloat = f1;
        if (f2 - f1 > 180.0F) {
          paramFloat = f1 + 360.0F;
        }
      }
      mIndex += 1;
      if (mIndex >= 24) {
        mIndex = 12;
      }
      mV[mIndex] = paramFloat;
      mT[mIndex] = paramLong;
      mV[(mIndex - 12)] = paramFloat;
      mT[(mIndex - 12)] = paramLong;
      f2 = 0.0F;
      float f3 = 0.0F;
      f1 = 0.0F;
      float f4 = 0.0F;
      paramFloat = 0.0F;
      for (int i = 0;; i++)
      {
        localLmsFilter = this;
        if (i >= 11) {
          break;
        }
        int j = mIndex - 1 - i;
        float f5 = mV[j];
        float f6 = (float)(mT[j] / 2L + mT[(j + 1)] / 2L - paramLong) * 1.0E-9F;
        float f7 = (float)(mT[j] - mT[(j + 1)]) * 1.0E-9F;
        f7 *= f7;
        paramFloat += f5 * f7;
        f4 += f6 * f7 * f6;
        f1 += f6 * f7;
        f3 += f6 * f7 * f5;
        f2 += f7;
      }
      f3 = (paramFloat * f4 + f1 * f3) / (f2 * f4 + f1 * f1);
      f1 = (0.08F * ((f2 * f3 - paramFloat) / f1) + f3) * 0.0027777778F;
      if (f1 >= 0.0F) {
        f2 = f1;
      } else {
        f2 = -f1;
      }
      paramFloat = f1;
      if (f2 >= 0.5F) {
        paramFloat = f1 - (float)Math.ceil(0.5F + f1) + 1.0F;
      }
      f1 = paramFloat;
      if (paramFloat < 0.0F) {
        f1 = paramFloat + 1.0F;
      }
      return f1 * 360.0F;
    }
  }
}
