package android.location;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import java.util.HashMap;

public class CountryDetector
{
  private static final String TAG = "CountryDetector";
  private final HashMap<CountryListener, ListenerTransport> mListeners;
  private final ICountryDetector mService;
  
  public CountryDetector(ICountryDetector paramICountryDetector)
  {
    mService = paramICountryDetector;
    mListeners = new HashMap();
  }
  
  public void addCountryListener(CountryListener paramCountryListener, Looper paramLooper)
  {
    synchronized (mListeners)
    {
      if (!mListeners.containsKey(paramCountryListener))
      {
        ListenerTransport localListenerTransport = new android/location/CountryDetector$ListenerTransport;
        localListenerTransport.<init>(paramCountryListener, paramLooper);
        try
        {
          mService.addCountryListener(localListenerTransport);
          mListeners.put(paramCountryListener, localListenerTransport);
        }
        catch (RemoteException paramCountryListener)
        {
          Log.e("CountryDetector", "addCountryListener: RemoteException", paramCountryListener);
        }
      }
      return;
    }
  }
  
  public Country detectCountry()
  {
    try
    {
      Country localCountry = mService.detectCountry();
      return localCountry;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("CountryDetector", "detectCountry: RemoteException", localRemoteException);
    }
    return null;
  }
  
  public void removeCountryListener(CountryListener paramCountryListener)
  {
    synchronized (mListeners)
    {
      ListenerTransport localListenerTransport = (ListenerTransport)mListeners.get(paramCountryListener);
      if (localListenerTransport != null) {
        try
        {
          mListeners.remove(paramCountryListener);
          mService.removeCountryListener(localListenerTransport);
        }
        catch (RemoteException paramCountryListener)
        {
          Log.e("CountryDetector", "removeCountryListener: RemoteException", paramCountryListener);
        }
      }
      return;
    }
  }
  
  private static final class ListenerTransport
    extends ICountryListener.Stub
  {
    private final Handler mHandler;
    private final CountryListener mListener;
    
    public ListenerTransport(CountryListener paramCountryListener, Looper paramLooper)
    {
      mListener = paramCountryListener;
      if (paramLooper != null) {
        mHandler = new Handler(paramLooper);
      } else {
        mHandler = new Handler();
      }
    }
    
    public void onCountryDetected(final Country paramCountry)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mListener.onCountryDetected(paramCountry);
        }
      });
    }
  }
}
