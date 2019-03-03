package android.accessibilityservice;

import android.os.Handler;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;

public final class FingerprintGestureController
{
  public static final int FINGERPRINT_GESTURE_SWIPE_DOWN = 8;
  public static final int FINGERPRINT_GESTURE_SWIPE_LEFT = 2;
  public static final int FINGERPRINT_GESTURE_SWIPE_RIGHT = 1;
  public static final int FINGERPRINT_GESTURE_SWIPE_UP = 4;
  private static final String LOG_TAG = "FingerprintGestureController";
  private final IAccessibilityServiceConnection mAccessibilityServiceConnection;
  private final ArrayMap<FingerprintGestureCallback, Handler> mCallbackHandlerMap = new ArrayMap(1);
  private final Object mLock = new Object();
  
  @VisibleForTesting
  public FingerprintGestureController(IAccessibilityServiceConnection paramIAccessibilityServiceConnection)
  {
    mAccessibilityServiceConnection = paramIAccessibilityServiceConnection;
  }
  
  public boolean isGestureDetectionAvailable()
  {
    try
    {
      boolean bool = mAccessibilityServiceConnection.isFingerprintGestureDetectionAvailable();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("FingerprintGestureController", "Failed to check if fingerprint gestures are active", localRemoteException);
      localRemoteException.rethrowFromSystemServer();
    }
    return false;
  }
  
  public void onGesture(int paramInt)
  {
    synchronized (mLock)
    {
      ArrayMap localArrayMap = new android/util/ArrayMap;
      localArrayMap.<init>(mCallbackHandlerMap);
      int i = localArrayMap.size();
      for (int j = 0; j < i; j++)
      {
        ??? = (FingerprintGestureCallback)localArrayMap.keyAt(j);
        Handler localHandler = (Handler)localArrayMap.valueAt(j);
        if (localHandler != null) {
          localHandler.post(new _..Lambda.FingerprintGestureController.BQjrQQom4K3C98FNiI0fi7SvHfY((FingerprintGestureCallback)???, paramInt));
        } else {
          ((FingerprintGestureCallback)???).onGestureDetected(paramInt);
        }
      }
      return;
    }
  }
  
  public void onGestureDetectionActiveChanged(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      ArrayMap localArrayMap = new android/util/ArrayMap;
      localArrayMap.<init>(mCallbackHandlerMap);
      int i = localArrayMap.size();
      for (int j = 0; j < i; j++)
      {
        FingerprintGestureCallback localFingerprintGestureCallback = (FingerprintGestureCallback)localArrayMap.keyAt(j);
        ??? = (Handler)localArrayMap.valueAt(j);
        if (??? != null) {
          ((Handler)???).post(new _..Lambda.FingerprintGestureController.M_ZApqp96G6ZF2WdWrGDJ8Qsfck(localFingerprintGestureCallback, paramBoolean));
        } else {
          localFingerprintGestureCallback.onGestureDetectionAvailabilityChanged(paramBoolean);
        }
      }
      return;
    }
  }
  
  public void registerFingerprintGestureCallback(FingerprintGestureCallback paramFingerprintGestureCallback, Handler paramHandler)
  {
    synchronized (mLock)
    {
      mCallbackHandlerMap.put(paramFingerprintGestureCallback, paramHandler);
      return;
    }
  }
  
  public void unregisterFingerprintGestureCallback(FingerprintGestureCallback paramFingerprintGestureCallback)
  {
    synchronized (mLock)
    {
      mCallbackHandlerMap.remove(paramFingerprintGestureCallback);
      return;
    }
  }
  
  public static abstract class FingerprintGestureCallback
  {
    public FingerprintGestureCallback() {}
    
    public void onGestureDetected(int paramInt) {}
    
    public void onGestureDetectionAvailabilityChanged(boolean paramBoolean) {}
  }
}
