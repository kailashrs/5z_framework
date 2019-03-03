package android.accessibilityservice;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.util.Preconditions;

public final class AccessibilityButtonController
{
  private static final String LOG_TAG = "A11yButtonController";
  private ArrayMap<AccessibilityButtonCallback, Handler> mCallbacks;
  private final Object mLock;
  private final IAccessibilityServiceConnection mServiceConnection;
  
  AccessibilityButtonController(IAccessibilityServiceConnection paramIAccessibilityServiceConnection)
  {
    mServiceConnection = paramIAccessibilityServiceConnection;
    mLock = new Object();
  }
  
  void dispatchAccessibilityButtonAvailabilityChanged(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      if ((mCallbacks != null) && (!mCallbacks.isEmpty()))
      {
        ArrayMap localArrayMap = new android/util/ArrayMap;
        localArrayMap.<init>(mCallbacks);
        int i = 0;
        int j = localArrayMap.size();
        while (i < j)
        {
          ??? = (AccessibilityButtonCallback)localArrayMap.keyAt(i);
          ((Handler)localArrayMap.valueAt(i)).post(new _..Lambda.AccessibilityButtonController.RskKrfcSyUz7I9Sqaziy1P990ZM(this, (AccessibilityButtonCallback)???, paramBoolean));
          i++;
        }
        return;
      }
      Slog.w("A11yButtonController", "Received accessibility button availability change with no callbacks!");
      return;
    }
  }
  
  void dispatchAccessibilityButtonClicked()
  {
    synchronized (mLock)
    {
      if ((mCallbacks != null) && (!mCallbacks.isEmpty()))
      {
        ArrayMap localArrayMap = new android/util/ArrayMap;
        localArrayMap.<init>(mCallbacks);
        int i = 0;
        int j = localArrayMap.size();
        while (i < j)
        {
          ??? = (AccessibilityButtonCallback)localArrayMap.keyAt(i);
          ((Handler)localArrayMap.valueAt(i)).post(new _..Lambda.AccessibilityButtonController.b_UAM9QJWcH4KQOC_odiN0t_boU(this, (AccessibilityButtonCallback)???));
          i++;
        }
        return;
      }
      Slog.w("A11yButtonController", "Received accessibility button click with no callbacks!");
      return;
    }
  }
  
  public boolean isAccessibilityButtonAvailable()
  {
    try
    {
      boolean bool = mServiceConnection.isAccessibilityButtonAvailable();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Slog.w("A11yButtonController", "Failed to get accessibility button availability.", localRemoteException);
      localRemoteException.rethrowFromSystemServer();
    }
    return false;
  }
  
  public void registerAccessibilityButtonCallback(AccessibilityButtonCallback paramAccessibilityButtonCallback)
  {
    registerAccessibilityButtonCallback(paramAccessibilityButtonCallback, new Handler(Looper.getMainLooper()));
  }
  
  public void registerAccessibilityButtonCallback(AccessibilityButtonCallback paramAccessibilityButtonCallback, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramAccessibilityButtonCallback);
    Preconditions.checkNotNull(paramHandler);
    synchronized (mLock)
    {
      if (mCallbacks == null)
      {
        ArrayMap localArrayMap = new android/util/ArrayMap;
        localArrayMap.<init>();
        mCallbacks = localArrayMap;
      }
      mCallbacks.put(paramAccessibilityButtonCallback, paramHandler);
      return;
    }
  }
  
  public void unregisterAccessibilityButtonCallback(AccessibilityButtonCallback paramAccessibilityButtonCallback)
  {
    Preconditions.checkNotNull(paramAccessibilityButtonCallback);
    synchronized (mLock)
    {
      if (mCallbacks == null) {
        return;
      }
      int i = mCallbacks.indexOfKey(paramAccessibilityButtonCallback);
      int j;
      if (i >= 0) {
        j = 1;
      } else {
        j = 0;
      }
      if (j != 0) {
        mCallbacks.removeAt(i);
      }
      return;
    }
  }
  
  public static abstract class AccessibilityButtonCallback
  {
    public AccessibilityButtonCallback() {}
    
    public void onAvailabilityChanged(AccessibilityButtonController paramAccessibilityButtonController, boolean paramBoolean) {}
    
    public void onClicked(AccessibilityButtonController paramAccessibilityButtonController) {}
  }
}
