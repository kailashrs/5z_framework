package android.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

public class EthernetManager
{
  public static final int ETHERNET_POLICY_DISCONNECT_WHEN_SUSPEND = 0;
  public static final int ETHERNET_POLICY_KEEP_ON_WHEN_SUSPEND = 1;
  public static final String ETHERNET_STATE_CHANGED_ACTION = "android.net.ethernet.ETHERNET_STATE_CHANGED";
  public static final int ETHERNET_STATE_DISABLED = 1;
  public static final int ETHERNET_STATE_DISABLING = 0;
  public static final int ETHERNET_STATE_ENABLED = 3;
  public static final int ETHERNET_STATE_ENABLING = 2;
  public static final int ETHERNET_STATE_UNKNOWN = 4;
  public static final String EXTRA_ETHERNET_STATE = "ethernet_state";
  public static final String EXTRA_PREVIOUS_ETHERNET_STATE = "previous_ethernet_state";
  private static final int MSG_AVAILABILITY_CHANGED = 1000;
  private static final String TAG = "EthernetManager";
  private final Context mContext;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what == 1000)
      {
        int i = arg1;
        boolean bool = true;
        if (i != 1) {
          bool = false;
        }
        Iterator localIterator = mListeners.iterator();
        while (localIterator.hasNext()) {
          ((EthernetManager.Listener)localIterator.next()).onAvailabilityChanged((String)obj, bool);
        }
      }
    }
  };
  private final ArrayList<Listener> mListeners = new ArrayList();
  private final IEthernetManager mService;
  private final IEthernetServiceListener.Stub mServiceListener = new IEthernetServiceListener.Stub()
  {
    public void onAvailabilityChanged(String paramAnonymousString, boolean paramAnonymousBoolean)
    {
      mHandler.obtainMessage(1000, paramAnonymousBoolean, 0, paramAnonymousString).sendToTarget();
    }
  };
  
  public EthernetManager(Context paramContext, IEthernetManager paramIEthernetManager)
  {
    mContext = paramContext;
    mService = paramIEthernetManager;
  }
  
  public void addListener(Listener paramListener)
  {
    if (paramListener != null)
    {
      mListeners.add(paramListener);
      if (mListeners.size() == 1) {
        try
        {
          mService.addListener(mServiceListener);
        }
        catch (RemoteException paramListener)
        {
          throw paramListener.rethrowFromSystemServer();
        }
      }
      return;
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  public String[] getAvailableInterfaces()
  {
    try
    {
      String[] arrayOfString = mService.getAvailableInterfaces();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public IpConfiguration getConfiguration(String paramString)
  {
    try
    {
      paramString = mService.getConfiguration(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int getEthernetSleepPolicy()
  {
    try
    {
      int i = mService.getEthernetSleepPolicy();
      return i;
    }
    catch (NullPointerException|RemoteException localNullPointerException) {}
    return -1;
  }
  
  public int getEthernetState()
  {
    try
    {
      int i = mService.getEthernetState();
      return i;
    }
    catch (NullPointerException|RemoteException localNullPointerException) {}
    return 4;
  }
  
  public boolean isAvailable()
  {
    boolean bool;
    if (getAvailableInterfaces().length > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAvailable(String paramString)
  {
    try
    {
      boolean bool = mService.isAvailable(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void removeListener(Listener paramListener)
  {
    if (paramListener != null)
    {
      mListeners.remove(paramListener);
      if (mListeners.isEmpty()) {
        try
        {
          mService.removeListener(mServiceListener);
        }
        catch (RemoteException paramListener)
        {
          throw paramListener.rethrowFromSystemServer();
        }
      }
      return;
    }
    throw new IllegalArgumentException("listener must not be null");
  }
  
  public void setConfiguration(String paramString, IpConfiguration paramIpConfiguration)
  {
    try
    {
      mService.setConfiguration(paramString, paramIpConfiguration);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean setEthernetEnabled(boolean paramBoolean)
  {
    try
    {
      paramBoolean = mService.setEthernetEnabled(paramBoolean);
      return paramBoolean;
    }
    catch (NullPointerException|RemoteException localNullPointerException) {}
    return false;
  }
  
  public boolean setEthernetSleepPolicy(int paramInt)
  {
    try
    {
      mService.setEthernetSleepPolicy(paramInt);
      return true;
    }
    catch (NullPointerException|RemoteException localNullPointerException) {}
    return false;
  }
  
  public static abstract interface Listener
  {
    public abstract void onAvailabilityChanged(String paramString, boolean paramBoolean);
  }
}
