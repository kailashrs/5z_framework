package android.hardware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.MemoryFile;
import android.os.MessageQueue;
import android.util.Log;
import android.util.SeempLog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class SystemSensorManager
  extends SensorManager
{
  private static final boolean DEBUG_DYNAMIC_SENSOR = true;
  private static final int MAX_LISTENER_COUNT = 128;
  private static final int MIN_DIRECT_CHANNEL_BUFFER_SIZE = 104;
  @GuardedBy("sLock")
  private static InjectEventQueue sInjectEventQueue = null;
  private static final Object sLock = new Object();
  @GuardedBy("sLock")
  private static boolean sNativeClassInited = false;
  private final Context mContext;
  private BroadcastReceiver mDynamicSensorBroadcastReceiver;
  private HashMap<SensorManager.DynamicSensorCallback, Handler> mDynamicSensorCallbacks = new HashMap();
  private boolean mDynamicSensorListDirty = true;
  private List<Sensor> mFullDynamicSensorsList = new ArrayList();
  private final ArrayList<Sensor> mFullSensorsList = new ArrayList();
  private final HashMap<Integer, Sensor> mHandleToSensor = new HashMap();
  private final Looper mMainLooper;
  private final long mNativeInstance;
  private final HashMap<SensorEventListener, SensorEventQueue> mSensorListeners = new HashMap();
  private final int mTargetSdkLevel;
  private final HashMap<TriggerEventListener, TriggerEventQueue> mTriggerListeners = new HashMap();
  
  public SystemSensorManager(Context paramContext, Looper paramLooper)
  {
    synchronized (sLock)
    {
      if (!sNativeClassInited)
      {
        sNativeClassInited = true;
        nativeClassInit();
      }
      mMainLooper = paramLooper;
      mTargetSdkLevel = getApplicationInfotargetSdkVersion;
      mContext = paramContext;
      mNativeInstance = nativeCreate(paramContext.getOpPackageName());
      int i = 0;
      paramContext = new Sensor();
      if (!nativeGetSensorAtIndex(mNativeInstance, paramContext, i)) {
        return;
      }
      mFullSensorsList.add(paramContext);
      mHandleToSensor.put(Integer.valueOf(paramContext.getHandle()), paramContext);
      i++;
    }
  }
  
  private void cleanupSensorConnection(Sensor paramSensor)
  {
    mHandleToSensor.remove(Integer.valueOf(paramSensor.getHandle()));
    Object localObject1;
    Object localObject2;
    Object localObject3;
    if (paramSensor.getReportingMode() == 2) {
      synchronized (mTriggerListeners)
      {
        localObject1 = new java/util/HashMap;
        ((HashMap)localObject1).<init>(mTriggerListeners);
        localObject2 = ((HashMap)localObject1).keySet().iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject3 = (TriggerEventListener)((Iterator)localObject2).next();
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("removed trigger listener");
          ((StringBuilder)localObject1).append(localObject3.toString());
          ((StringBuilder)localObject1).append(" due to sensor disconnection");
          Log.i("SensorManager", ((StringBuilder)localObject1).toString());
          cancelTriggerSensorImpl((TriggerEventListener)localObject3, paramSensor, true);
        }
      }
    }
    synchronized (mSensorListeners)
    {
      localObject1 = new java/util/HashMap;
      ((HashMap)localObject1).<init>(mSensorListeners);
      localObject1 = ((HashMap)localObject1).keySet().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject3 = (SensorEventListener)((Iterator)localObject1).next();
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("removed event listener");
        ((StringBuilder)localObject2).append(localObject3.toString());
        ((StringBuilder)localObject2).append(" due to sensor disconnection");
        Log.i("SensorManager", ((StringBuilder)localObject2).toString());
        unregisterListenerImpl((SensorEventListener)localObject3, paramSensor);
      }
      return;
    }
  }
  
  private static boolean diffSortedSensorList(List<Sensor> paramList1, List<Sensor> paramList2, List<Sensor> paramList3, List<Sensor> paramList4, List<Sensor> paramList5)
  {
    boolean bool = false;
    int i = 0;
    int j = 0;
    for (;;)
    {
      if ((j < paramList1.size()) && ((i >= paramList2.size()) || (((Sensor)paramList2.get(i)).getHandle() > ((Sensor)paramList1.get(j)).getHandle())))
      {
        bool = true;
        if (paramList5 != null) {
          paramList5.add((Sensor)paramList1.get(j));
        }
        j++;
      }
      else if ((i < paramList2.size()) && ((j >= paramList1.size()) || (((Sensor)paramList2.get(i)).getHandle() < ((Sensor)paramList1.get(j)).getHandle())))
      {
        bool = true;
        if (paramList4 != null) {
          paramList4.add((Sensor)paramList2.get(i));
        }
        if (paramList3 != null) {
          paramList3.add((Sensor)paramList2.get(i));
        }
        i++;
      }
      else
      {
        if ((i >= paramList2.size()) || (j >= paramList1.size()) || (((Sensor)paramList2.get(i)).getHandle() != ((Sensor)paramList1.get(j)).getHandle())) {
          break;
        }
        if (paramList3 != null) {
          paramList3.add((Sensor)paramList1.get(j));
        }
        i++;
        j++;
      }
    }
    return bool;
  }
  
  private static native void nativeClassInit();
  
  private static native int nativeConfigDirectChannel(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  private static native long nativeCreate(String paramString);
  
  private static native int nativeCreateDirectChannel(long paramLong1, long paramLong2, int paramInt1, int paramInt2, HardwareBuffer paramHardwareBuffer);
  
  private static native void nativeDestroyDirectChannel(long paramLong, int paramInt);
  
  private static native void nativeGetDynamicSensors(long paramLong, List<Sensor> paramList);
  
  private static native boolean nativeGetSensorAtIndex(long paramLong, Sensor paramSensor, int paramInt);
  
  private static native boolean nativeIsDataInjectionEnabled(long paramLong);
  
  private static native int nativeSetOperationParameter(long paramLong, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int[] paramArrayOfInt);
  
  private void setupDynamicSensorBroadcastReceiver()
  {
    if (mDynamicSensorBroadcastReceiver == null)
    {
      mDynamicSensorBroadcastReceiver = new BroadcastReceiver()
      {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
          if (paramAnonymousIntent.getAction() == "android.intent.action.DYNAMIC_SENSOR_CHANGED")
          {
            Log.i("SensorManager", "DYNS received DYNAMIC_SENSOR_CHANED broadcast");
            SystemSensorManager.access$002(SystemSensorManager.this, true);
            SystemSensorManager.this.updateDynamicSensorList();
          }
        }
      };
      IntentFilter localIntentFilter = new IntentFilter("dynamic_sensor_change");
      localIntentFilter.addAction("android.intent.action.DYNAMIC_SENSOR_CHANGED");
      mContext.registerReceiver(mDynamicSensorBroadcastReceiver, localIntentFilter);
    }
  }
  
  private void teardownDynamicSensorBroadcastReceiver()
  {
    mDynamicSensorCallbacks.clear();
    mContext.unregisterReceiver(mDynamicSensorBroadcastReceiver);
    mDynamicSensorBroadcastReceiver = null;
  }
  
  private void updateDynamicSensorList()
  {
    synchronized (mFullDynamicSensorsList)
    {
      if (mDynamicSensorListDirty)
      {
        Object localObject1 = new java/util/ArrayList;
        ((ArrayList)localObject1).<init>();
        nativeGetDynamicSensors(mNativeInstance, (List)localObject1);
        Object localObject3 = new java/util/ArrayList;
        ((ArrayList)localObject3).<init>();
        ArrayList localArrayList1 = new java/util/ArrayList;
        localArrayList1.<init>();
        ArrayList localArrayList2 = new java/util/ArrayList;
        localArrayList2.<init>();
        if (diffSortedSensorList(mFullDynamicSensorsList, (List)localObject1, (List)localObject3, localArrayList1, localArrayList2))
        {
          Log.i("SensorManager", "DYNS dynamic sensor list cached should be updated");
          mFullDynamicSensorsList = ((List)localObject3);
          localObject3 = localArrayList1.iterator();
          while (((Iterator)localObject3).hasNext())
          {
            localObject1 = (Sensor)((Iterator)localObject3).next();
            mHandleToSensor.put(Integer.valueOf(((Sensor)localObject1).getHandle()), localObject1);
          }
          localObject3 = new android/os/Handler;
          ((Handler)localObject3).<init>(mContext.getMainLooper());
          Iterator localIterator = mDynamicSensorCallbacks.entrySet().iterator();
          while (localIterator.hasNext())
          {
            localObject1 = (Map.Entry)localIterator.next();
            SensorManager.DynamicSensorCallback localDynamicSensorCallback = (SensorManager.DynamicSensorCallback)((Map.Entry)localObject1).getKey();
            if (((Map.Entry)localObject1).getValue() == null) {
              localObject1 = localObject3;
            } else {
              localObject1 = (Handler)((Map.Entry)localObject1).getValue();
            }
            Runnable local1 = new android/hardware/SystemSensorManager$1;
            local1.<init>(this, localArrayList1, localDynamicSensorCallback, localArrayList2);
            ((Handler)localObject1).post(local1);
          }
          localObject1 = localArrayList2.iterator();
          while (((Iterator)localObject1).hasNext()) {
            cleanupSensorConnection((Sensor)((Iterator)localObject1).next());
          }
        }
        mDynamicSensorListDirty = false;
      }
      return;
    }
  }
  
  protected boolean cancelTriggerSensorImpl(TriggerEventListener paramTriggerEventListener, Sensor paramSensor, boolean paramBoolean)
  {
    if ((paramSensor != null) && (paramSensor.getReportingMode() != 2)) {
      return false;
    }
    synchronized (mTriggerListeners)
    {
      TriggerEventQueue localTriggerEventQueue = (TriggerEventQueue)mTriggerListeners.get(paramTriggerEventListener);
      if (localTriggerEventQueue != null)
      {
        if (paramSensor == null) {
          paramBoolean = localTriggerEventQueue.removeAllSensors();
        } else {
          paramBoolean = localTriggerEventQueue.removeSensor(paramSensor, paramBoolean);
        }
        if ((paramBoolean) && (!localTriggerEventQueue.hasSensors()))
        {
          mTriggerListeners.remove(paramTriggerEventListener);
          localTriggerEventQueue.dispose();
        }
        return paramBoolean;
      }
      return false;
    }
  }
  
  protected int configureDirectChannelImpl(SensorDirectChannel paramSensorDirectChannel, Sensor paramSensor, int paramInt)
  {
    if (paramSensorDirectChannel.isOpen())
    {
      if ((paramInt >= 0) && (paramInt <= 3))
      {
        if ((paramSensor == null) && (paramInt != 0)) {
          throw new IllegalArgumentException("when sensor is null, rate can only be DIRECT_RATE_STOP");
        }
        if (paramSensor == null) {
          i = -1;
        } else {
          i = paramSensor.getHandle();
        }
        int i = nativeConfigDirectChannel(mNativeInstance, paramSensorDirectChannel.getNativeHandle(), i, paramInt);
        int j = 0;
        int k = 0;
        if (paramInt == 0)
        {
          paramInt = k;
          if (i == 0) {
            paramInt = 1;
          }
          return paramInt;
        }
        paramInt = j;
        if (i > 0) {
          paramInt = i;
        }
        return paramInt;
      }
      throw new IllegalArgumentException("rate parameter invalid");
    }
    throw new IllegalStateException("channel is closed");
  }
  
  protected SensorDirectChannel createDirectChannelImpl(MemoryFile paramMemoryFile, HardwareBuffer paramHardwareBuffer)
  {
    if (paramMemoryFile != null) {}
    int i;
    long l;
    int j;
    for (;;)
    {
      try
      {
        i = paramMemoryFile.getFileDescriptor().getInt$();
        if (paramMemoryFile.length() >= 104)
        {
          l = paramMemoryFile.length();
          j = nativeCreateDirectChannel(mNativeInstance, l, 1, i, null);
          if (j > 0)
          {
            i = 1;
            break;
          }
          paramMemoryFile = new StringBuilder();
          paramMemoryFile.append("create MemoryFile direct channel failed ");
          paramMemoryFile.append(j);
          throw new UncheckedIOException(new IOException(paramMemoryFile.toString()));
        }
        throw new IllegalArgumentException("Size of MemoryFile has to be greater than 104");
      }
      catch (IOException paramMemoryFile)
      {
        throw new IllegalArgumentException("MemoryFile object is not valid");
      }
      if (paramHardwareBuffer == null) {
        break label291;
      }
      if (paramHardwareBuffer.getFormat() != 33) {
        break label280;
      }
      if (paramHardwareBuffer.getHeight() != 1) {
        break label269;
      }
      if (paramHardwareBuffer.getWidth() < 104) {
        break label258;
      }
      if ((paramHardwareBuffer.getUsage() & 0x800000) == 0L) {
        break label247;
      }
      l = paramHardwareBuffer.getWidth();
      j = nativeCreateDirectChannel(mNativeInstance, l, 2, -1, paramHardwareBuffer);
      if (j <= 0) {
        break label205;
      }
      i = 2;
    }
    return new SensorDirectChannel(this, j, i, l);
    label205:
    paramMemoryFile = new StringBuilder();
    paramMemoryFile.append("create HardwareBuffer direct channel failed ");
    paramMemoryFile.append(j);
    throw new UncheckedIOException(new IOException(paramMemoryFile.toString()));
    label247:
    throw new IllegalArgumentException("HardwareBuffer must set usage flag USAGE_SENSOR_DIRECT_DATA");
    label258:
    throw new IllegalArgumentException("Width if HaradwareBuffer must be greater than 104");
    label269:
    throw new IllegalArgumentException("Height of HardwareBuffer must be 1");
    label280:
    throw new IllegalArgumentException("Format of HardwareBuffer must be BLOB");
    label291:
    throw new NullPointerException("shared memory object cannot be null");
  }
  
  protected void destroyDirectChannelImpl(SensorDirectChannel paramSensorDirectChannel)
  {
    if (paramSensorDirectChannel != null) {
      nativeDestroyDirectChannel(mNativeInstance, paramSensorDirectChannel.getNativeHandle());
    }
  }
  
  protected boolean flushImpl(SensorEventListener paramSensorEventListener)
  {
    if (paramSensorEventListener != null) {
      synchronized (mSensorListeners)
      {
        paramSensorEventListener = (SensorEventQueue)mSensorListeners.get(paramSensorEventListener);
        boolean bool = false;
        if (paramSensorEventListener == null) {
          return false;
        }
        if (paramSensorEventListener.flush() == 0) {
          bool = true;
        }
        return bool;
      }
    }
    throw new IllegalArgumentException("listener cannot be null");
  }
  
  protected List<Sensor> getFullDynamicSensorList()
  {
    setupDynamicSensorBroadcastReceiver();
    updateDynamicSensorList();
    return mFullDynamicSensorsList;
  }
  
  protected List<Sensor> getFullSensorList()
  {
    return mFullSensorsList;
  }
  
  protected boolean initDataInjectionImpl(boolean paramBoolean)
  {
    Object localObject1 = sLock;
    boolean bool = true;
    if (paramBoolean) {
      try
      {
        if (!nativeIsDataInjectionEnabled(mNativeInstance))
        {
          Log.e("SensorManager", "Data Injection mode not enabled");
          return false;
        }
        InjectEventQueue localInjectEventQueue = sInjectEventQueue;
        if (localInjectEventQueue == null) {
          try
          {
            localInjectEventQueue = new android/hardware/SystemSensorManager$InjectEventQueue;
            localInjectEventQueue.<init>(this, mMainLooper, this, mContext.getPackageName());
            sInjectEventQueue = localInjectEventQueue;
          }
          catch (RuntimeException localRuntimeException)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Cannot create InjectEventQueue: ");
            localStringBuilder.append(localRuntimeException);
            Log.e("SensorManager", localStringBuilder.toString());
          }
        }
        if (sInjectEventQueue != null) {
          paramBoolean = bool;
        } else {
          paramBoolean = false;
        }
        return paramBoolean;
      }
      finally
      {
        break label158;
      }
    }
    if (sInjectEventQueue != null)
    {
      sInjectEventQueue.dispose();
      sInjectEventQueue = null;
    }
    return true;
    label158:
    throw localObject2;
  }
  
  protected boolean injectSensorDataImpl(Sensor paramSensor, float[] paramArrayOfFloat, int paramInt, long paramLong)
  {
    synchronized (sLock)
    {
      InjectEventQueue localInjectEventQueue = sInjectEventQueue;
      boolean bool = false;
      if (localInjectEventQueue == null)
      {
        Log.e("SensorManager", "Data injection mode not activated before calling injectSensorData");
        return false;
      }
      paramInt = sInjectEventQueue.injectSensorData(paramSensor.getHandle(), paramArrayOfFloat, paramInt, paramLong);
      if (paramInt != 0)
      {
        sInjectEventQueue.dispose();
        sInjectEventQueue = null;
      }
      if (paramInt == 0) {
        bool = true;
      }
      return bool;
    }
  }
  
  protected void registerDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback paramDynamicSensorCallback, Handler paramHandler)
  {
    Log.i("SensorManager", "DYNS Register dynamic sensor callback");
    if (paramDynamicSensorCallback != null)
    {
      if (mDynamicSensorCallbacks.containsKey(paramDynamicSensorCallback)) {
        return;
      }
      setupDynamicSensorBroadcastReceiver();
      mDynamicSensorCallbacks.put(paramDynamicSensorCallback, paramHandler);
      return;
    }
    throw new IllegalArgumentException("callback cannot be null");
  }
  
  protected boolean registerListenerImpl(SensorEventListener paramSensorEventListener, Sensor paramSensor, int paramInt1, Handler paramHandler, int paramInt2, int paramInt3)
  {
    SeempLog.record_sensor_rate(381, paramSensor, paramInt1);
    if ((paramSensorEventListener != null) && (paramSensor != null))
    {
      if (paramSensor.getReportingMode() == 2)
      {
        Log.e("SensorManager", "Trigger Sensors should use the requestTriggerSensor.");
        return false;
      }
      if ((paramInt2 >= 0) && (paramInt1 >= 0))
      {
        if (mSensorListeners.size() < 128) {
          synchronized (mSensorListeners)
          {
            Object localObject = (SensorEventQueue)mSensorListeners.get(paramSensorEventListener);
            if (localObject == null)
            {
              if (paramHandler != null) {
                paramHandler = paramHandler.getLooper();
              } else {
                paramHandler = mMainLooper;
              }
              if (paramSensorEventListener.getClass().getEnclosingClass() != null) {
                localObject = paramSensorEventListener.getClass().getEnclosingClass().getName();
              } else {
                localObject = paramSensorEventListener.getClass().getName();
              }
              SensorEventQueue localSensorEventQueue = new android/hardware/SystemSensorManager$SensorEventQueue;
              localSensorEventQueue.<init>(paramSensorEventListener, paramHandler, this, (String)localObject);
              if (!localSensorEventQueue.addSensor(paramSensor, paramInt1, paramInt2))
              {
                localSensorEventQueue.dispose();
                return false;
              }
              mSensorListeners.put(paramSensorEventListener, localSensorEventQueue);
              return true;
            }
            boolean bool = ((SensorEventQueue)localObject).addSensor(paramSensor, paramInt1, paramInt2);
            return bool;
          }
        }
        throw new IllegalStateException("register failed, the sensor listeners size has exceeded the maximum limit 128");
      }
      Log.e("SensorManager", "maxBatchReportLatencyUs and delayUs should be non-negative");
      return false;
    }
    Log.e("SensorManager", "sensor or listener is null");
    return false;
  }
  
  protected boolean requestTriggerSensorImpl(TriggerEventListener paramTriggerEventListener, Sensor paramSensor)
  {
    if (paramSensor != null)
    {
      if (paramTriggerEventListener != null)
      {
        if (paramSensor.getReportingMode() != 2) {
          return false;
        }
        if (mTriggerListeners.size() < 128) {
          synchronized (mTriggerListeners)
          {
            Object localObject = (TriggerEventQueue)mTriggerListeners.get(paramTriggerEventListener);
            if (localObject == null)
            {
              if (paramTriggerEventListener.getClass().getEnclosingClass() != null) {
                localObject = paramTriggerEventListener.getClass().getEnclosingClass().getName();
              } else {
                localObject = paramTriggerEventListener.getClass().getName();
              }
              TriggerEventQueue localTriggerEventQueue = new android/hardware/SystemSensorManager$TriggerEventQueue;
              localTriggerEventQueue.<init>(paramTriggerEventListener, mMainLooper, this, (String)localObject);
              if (!localTriggerEventQueue.addSensor(paramSensor, 0, 0))
              {
                localTriggerEventQueue.dispose();
                return false;
              }
              mTriggerListeners.put(paramTriggerEventListener, localTriggerEventQueue);
              return true;
            }
            boolean bool = ((TriggerEventQueue)localObject).addSensor(paramSensor, 0, 0);
            return bool;
          }
        }
        throw new IllegalStateException("request failed, the trigger listeners size has exceeded the maximum limit 128");
      }
      throw new IllegalArgumentException("listener cannot be null");
    }
    throw new IllegalArgumentException("sensor cannot be null");
  }
  
  protected boolean setOperationParameterImpl(SensorAdditionalInfo paramSensorAdditionalInfo)
  {
    int i = -1;
    if (sensor != null) {
      i = sensor.getHandle();
    }
    boolean bool;
    if (nativeSetOperationParameter(mNativeInstance, i, type, floatValues, intValues) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void unregisterDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback paramDynamicSensorCallback)
  {
    Log.i("SensorManager", "Removing dynamic sensor listerner");
    mDynamicSensorCallbacks.remove(paramDynamicSensorCallback);
  }
  
  protected void unregisterListenerImpl(SensorEventListener paramSensorEventListener, Sensor paramSensor)
  {
    SeempLog.record_sensor(382, paramSensor);
    if ((paramSensor != null) && (paramSensor.getReportingMode() == 2)) {
      return;
    }
    synchronized (mSensorListeners)
    {
      SensorEventQueue localSensorEventQueue = (SensorEventQueue)mSensorListeners.get(paramSensorEventListener);
      if (localSensorEventQueue != null)
      {
        boolean bool;
        if (paramSensor == null) {
          bool = localSensorEventQueue.removeAllSensors();
        } else {
          bool = localSensorEventQueue.removeSensor(paramSensor, true);
        }
        if ((bool) && (!localSensorEventQueue.hasSensors()))
        {
          mSensorListeners.remove(paramSensorEventListener);
          localSensorEventQueue.dispose();
        }
      }
      return;
    }
  }
  
  private static abstract class BaseEventQueue
  {
    protected static final int OPERATING_MODE_DATA_INJECTION = 1;
    protected static final int OPERATING_MODE_NORMAL = 0;
    private final SparseBooleanArray mActiveSensors = new SparseBooleanArray();
    private final CloseGuard mCloseGuard = CloseGuard.get();
    protected final SystemSensorManager mManager;
    private long mNativeSensorEventQueue;
    protected final SparseIntArray mSensorAccuracies = new SparseIntArray();
    
    BaseEventQueue(Looper paramLooper, SystemSensorManager paramSystemSensorManager, int paramInt, String paramString)
    {
      String str = paramString;
      if (paramString == null) {
        str = "";
      }
      mNativeSensorEventQueue = nativeInitBaseEventQueue(mNativeInstance, new WeakReference(this), paramLooper.getQueue(), str, paramInt, mContext.getOpPackageName());
      mCloseGuard.open("dispose");
      mManager = paramSystemSensorManager;
    }
    
    private int disableSensor(Sensor paramSensor)
    {
      if (mNativeSensorEventQueue != 0L)
      {
        if (paramSensor != null) {
          return nativeDisableSensor(mNativeSensorEventQueue, paramSensor.getHandle());
        }
        throw new NullPointerException();
      }
      throw new NullPointerException();
    }
    
    private void dispose(boolean paramBoolean)
    {
      if (mCloseGuard != null)
      {
        if (paramBoolean) {
          mCloseGuard.warnIfOpen();
        }
        mCloseGuard.close();
      }
      if (mNativeSensorEventQueue != 0L)
      {
        nativeDestroySensorEventQueue(mNativeSensorEventQueue);
        mNativeSensorEventQueue = 0L;
      }
    }
    
    private int enableSensor(Sensor paramSensor, int paramInt1, int paramInt2)
    {
      if (mNativeSensorEventQueue != 0L)
      {
        if (paramSensor != null) {
          return nativeEnableSensor(mNativeSensorEventQueue, paramSensor.getHandle(), paramInt1, paramInt2);
        }
        throw new NullPointerException();
      }
      throw new NullPointerException();
    }
    
    private static native void nativeDestroySensorEventQueue(long paramLong);
    
    private static native int nativeDisableSensor(long paramLong, int paramInt);
    
    private static native int nativeEnableSensor(long paramLong, int paramInt1, int paramInt2, int paramInt3);
    
    private static native int nativeFlushSensor(long paramLong);
    
    private static native long nativeInitBaseEventQueue(long paramLong, WeakReference<BaseEventQueue> paramWeakReference, MessageQueue paramMessageQueue, String paramString1, int paramInt, String paramString2);
    
    private static native int nativeInjectSensorData(long paramLong1, int paramInt1, float[] paramArrayOfFloat, int paramInt2, long paramLong2);
    
    public boolean addSensor(Sensor paramSensor, int paramInt1, int paramInt2)
    {
      int i = paramSensor.getHandle();
      if (mActiveSensors.get(i)) {
        return false;
      }
      mActiveSensors.put(i, true);
      addSensorEvent(paramSensor);
      if ((enableSensor(paramSensor, paramInt1, paramInt2) != 0) && ((paramInt2 == 0) || ((paramInt2 > 0) && (enableSensor(paramSensor, paramInt1, 0) != 0))))
      {
        removeSensor(paramSensor, false);
        return false;
      }
      return true;
    }
    
    protected abstract void addSensorEvent(Sensor paramSensor);
    
    protected void dispatchAdditionalInfoEvent(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat, int[] paramArrayOfInt) {}
    
    protected abstract void dispatchFlushCompleteEvent(int paramInt);
    
    protected abstract void dispatchSensorEvent(int paramInt1, float[] paramArrayOfFloat, int paramInt2, long paramLong);
    
    public void dispose()
    {
      dispose(false);
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        dispose(true);
        return;
      }
      finally
      {
        super.finalize();
      }
    }
    
    public int flush()
    {
      if (mNativeSensorEventQueue != 0L) {
        return nativeFlushSensor(mNativeSensorEventQueue);
      }
      throw new NullPointerException();
    }
    
    public boolean hasSensors()
    {
      SparseBooleanArray localSparseBooleanArray = mActiveSensors;
      boolean bool = true;
      if (localSparseBooleanArray.indexOfValue(true) < 0) {
        bool = false;
      }
      return bool;
    }
    
    protected int injectSensorDataBase(int paramInt1, float[] paramArrayOfFloat, int paramInt2, long paramLong)
    {
      return nativeInjectSensorData(mNativeSensorEventQueue, paramInt1, paramArrayOfFloat, paramInt2, paramLong);
    }
    
    public boolean removeAllSensors()
    {
      for (int i = 0; i < mActiveSensors.size(); i++) {
        if (mActiveSensors.valueAt(i) == true)
        {
          int j = mActiveSensors.keyAt(i);
          Sensor localSensor = (Sensor)mManager.mHandleToSensor.get(Integer.valueOf(j));
          if (localSensor != null)
          {
            disableSensor(localSensor);
            mActiveSensors.put(j, false);
            removeSensorEvent(localSensor);
          }
        }
      }
      return true;
    }
    
    public boolean removeSensor(Sensor paramSensor, boolean paramBoolean)
    {
      int i = paramSensor.getHandle();
      if (mActiveSensors.get(i))
      {
        if (paramBoolean) {
          disableSensor(paramSensor);
        }
        mActiveSensors.put(paramSensor.getHandle(), false);
        removeSensorEvent(paramSensor);
        return true;
      }
      return false;
    }
    
    protected abstract void removeSensorEvent(Sensor paramSensor);
  }
  
  final class InjectEventQueue
    extends SystemSensorManager.BaseEventQueue
  {
    public InjectEventQueue(Looper paramLooper, SystemSensorManager paramSystemSensorManager, String paramString)
    {
      super(paramSystemSensorManager, 1, paramString);
    }
    
    protected void addSensorEvent(Sensor paramSensor) {}
    
    protected void dispatchFlushCompleteEvent(int paramInt) {}
    
    protected void dispatchSensorEvent(int paramInt1, float[] paramArrayOfFloat, int paramInt2, long paramLong) {}
    
    int injectSensorData(int paramInt1, float[] paramArrayOfFloat, int paramInt2, long paramLong)
    {
      return injectSensorDataBase(paramInt1, paramArrayOfFloat, paramInt2, paramLong);
    }
    
    protected void removeSensorEvent(Sensor paramSensor) {}
  }
  
  static final class SensorEventQueue
    extends SystemSensorManager.BaseEventQueue
  {
    private final SensorEventListener mListener;
    private final SparseArray<SensorEvent> mSensorsEvents = new SparseArray();
    
    public SensorEventQueue(SensorEventListener paramSensorEventListener, Looper paramLooper, SystemSensorManager paramSystemSensorManager, String paramString)
    {
      super(paramSystemSensorManager, 0, paramString);
      mListener = paramSensorEventListener;
    }
    
    public void addSensorEvent(Sensor paramSensor)
    {
      SensorEvent localSensorEvent = new SensorEvent(Sensor.getMaxLengthValuesArray(paramSensor, mManager.mTargetSdkLevel));
      synchronized (mSensorsEvents)
      {
        mSensorsEvents.put(paramSensor.getHandle(), localSensorEvent);
        return;
      }
    }
    
    protected void dispatchAdditionalInfoEvent(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat, int[] paramArrayOfInt)
    {
      if ((mListener instanceof SensorEventCallback))
      {
        Sensor localSensor = (Sensor)mManager.mHandleToSensor.get(Integer.valueOf(paramInt1));
        if (localSensor == null) {
          return;
        }
        paramArrayOfFloat = new SensorAdditionalInfo(localSensor, paramInt2, paramInt3, paramArrayOfInt, paramArrayOfFloat);
        ((SensorEventCallback)mListener).onSensorAdditionalInfo(paramArrayOfFloat);
      }
    }
    
    protected void dispatchFlushCompleteEvent(int paramInt)
    {
      if ((mListener instanceof SensorEventListener2))
      {
        Sensor localSensor = (Sensor)mManager.mHandleToSensor.get(Integer.valueOf(paramInt));
        if (localSensor == null) {
          return;
        }
        ((SensorEventListener2)mListener).onFlushCompleted(localSensor);
      }
    }
    
    protected void dispatchSensorEvent(int paramInt1, float[] paramArrayOfFloat, int paramInt2, long paramLong)
    {
      Sensor localSensor = (Sensor)mManager.mHandleToSensor.get(Integer.valueOf(paramInt1));
      if (localSensor == null) {
        return;
      }
      synchronized (mSensorsEvents)
      {
        SensorEvent localSensorEvent = (SensorEvent)mSensorsEvents.get(paramInt1);
        if (localSensorEvent == null) {
          return;
        }
        System.arraycopy(paramArrayOfFloat, 0, values, 0, values.length);
        timestamp = paramLong;
        accuracy = paramInt2;
        sensor = localSensor;
        paramInt2 = mSensorAccuracies.get(paramInt1);
        if ((accuracy >= 0) && (paramInt2 != accuracy))
        {
          mSensorAccuracies.put(paramInt1, accuracy);
          mListener.onAccuracyChanged(sensor, accuracy);
        }
        mListener.onSensorChanged(localSensorEvent);
        return;
      }
    }
    
    public void removeSensorEvent(Sensor paramSensor)
    {
      synchronized (mSensorsEvents)
      {
        mSensorsEvents.delete(paramSensor.getHandle());
        return;
      }
    }
  }
  
  static final class TriggerEventQueue
    extends SystemSensorManager.BaseEventQueue
  {
    private final TriggerEventListener mListener;
    private final SparseArray<TriggerEvent> mTriggerEvents = new SparseArray();
    
    public TriggerEventQueue(TriggerEventListener paramTriggerEventListener, Looper paramLooper, SystemSensorManager paramSystemSensorManager, String paramString)
    {
      super(paramSystemSensorManager, 0, paramString);
      mListener = paramTriggerEventListener;
    }
    
    public void addSensorEvent(Sensor paramSensor)
    {
      TriggerEvent localTriggerEvent = new TriggerEvent(Sensor.getMaxLengthValuesArray(paramSensor, mManager.mTargetSdkLevel));
      synchronized (mTriggerEvents)
      {
        mTriggerEvents.put(paramSensor.getHandle(), localTriggerEvent);
        return;
      }
    }
    
    protected void dispatchFlushCompleteEvent(int paramInt) {}
    
    protected void dispatchSensorEvent(int paramInt1, float[] paramArrayOfFloat, int paramInt2, long paramLong)
    {
      Sensor localSensor = (Sensor)mManager.mHandleToSensor.get(Integer.valueOf(paramInt1));
      if (localSensor == null) {
        return;
      }
      synchronized (mTriggerEvents)
      {
        TriggerEvent localTriggerEvent = (TriggerEvent)mTriggerEvents.get(paramInt1);
        if (localTriggerEvent == null)
        {
          paramArrayOfFloat = new StringBuilder();
          paramArrayOfFloat.append("Error: Trigger Event is null for Sensor: ");
          paramArrayOfFloat.append(localSensor);
          Log.e("SensorManager", paramArrayOfFloat.toString());
          return;
        }
        System.arraycopy(paramArrayOfFloat, 0, values, 0, values.length);
        timestamp = paramLong;
        sensor = localSensor;
        mManager.cancelTriggerSensorImpl(mListener, localSensor, false);
        mListener.onTrigger(localTriggerEvent);
        return;
      }
    }
    
    public void removeSensorEvent(Sensor paramSensor)
    {
      synchronized (mTriggerEvents)
      {
        mTriggerEvents.delete(paramSensor.getHandle());
        return;
      }
    }
  }
}
