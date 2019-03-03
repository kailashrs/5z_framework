package android.os;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class UEventObserver
{
  private static final boolean DEBUG = false;
  private static final String TAG = "UEventObserver";
  private static UEventThread sThread;
  
  public UEventObserver() {}
  
  private static UEventThread getThread()
  {
    try
    {
      if (sThread == null)
      {
        localUEventThread = new android/os/UEventObserver$UEventThread;
        localUEventThread.<init>();
        sThread = localUEventThread;
        sThread.start();
      }
      UEventThread localUEventThread = sThread;
      return localUEventThread;
    }
    finally {}
  }
  
  private static native void nativeAddMatch(String paramString);
  
  private static native void nativeRemoveMatch(String paramString);
  
  private static native void nativeSetup();
  
  private static native String nativeWaitForNextEvent();
  
  private static UEventThread peekThread()
  {
    try
    {
      UEventThread localUEventThread = sThread;
      return localUEventThread;
    }
    finally {}
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      stopObserving();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public abstract void onUEvent(UEvent paramUEvent);
  
  public final void startObserving(String paramString)
  {
    if ((paramString != null) && (!paramString.isEmpty()))
    {
      getThread().addObserver(paramString, this);
      return;
    }
    throw new IllegalArgumentException("match substring must be non-empty");
  }
  
  public final void stopObserving()
  {
    UEventThread localUEventThread = peekThread();
    if (localUEventThread != null) {
      localUEventThread.removeObserver(this);
    }
  }
  
  public static final class UEvent
  {
    private final HashMap<String, String> mMap = new HashMap();
    
    public UEvent(String paramString)
    {
      int i = 0;
      int j = paramString.length();
      while (i < j)
      {
        int k = paramString.indexOf('=', i);
        int m = paramString.indexOf(0, i);
        if (m < 0) {
          break;
        }
        if ((k > i) && (k < m)) {
          mMap.put(paramString.substring(i, k), paramString.substring(k + 1, m));
        }
        i = m + 1;
      }
    }
    
    public String get(String paramString)
    {
      return (String)mMap.get(paramString);
    }
    
    public String get(String paramString1, String paramString2)
    {
      paramString1 = (String)mMap.get(paramString1);
      if (paramString1 == null) {
        paramString1 = paramString2;
      }
      return paramString1;
    }
    
    public String toString()
    {
      return mMap.toString();
    }
  }
  
  private static final class UEventThread
    extends Thread
  {
    private final ArrayList<Object> mKeysAndObservers = new ArrayList();
    private final ArrayList<UEventObserver> mTempObserversToSignal = new ArrayList();
    
    public UEventThread()
    {
      super();
    }
    
    private void sendEvent(String paramString)
    {
      synchronized (mKeysAndObservers)
      {
        int i = mKeysAndObservers.size();
        int j = 0;
        for (int k = 0; k < i; k += 2) {
          if (paramString.contains((String)mKeysAndObservers.get(k)))
          {
            UEventObserver localUEventObserver = (UEventObserver)mKeysAndObservers.get(k + 1);
            mTempObserversToSignal.add(localUEventObserver);
          }
        }
        if (!mTempObserversToSignal.isEmpty())
        {
          paramString = new UEventObserver.UEvent(paramString);
          i = mTempObserversToSignal.size();
          for (k = j; k < i; k++) {
            ((UEventObserver)mTempObserversToSignal.get(k)).onUEvent(paramString);
          }
          mTempObserversToSignal.clear();
        }
        return;
      }
    }
    
    public void addObserver(String paramString, UEventObserver paramUEventObserver)
    {
      synchronized (mKeysAndObservers)
      {
        mKeysAndObservers.add(paramString);
        mKeysAndObservers.add(paramUEventObserver);
        UEventObserver.nativeAddMatch(paramString);
        return;
      }
    }
    
    public void removeObserver(UEventObserver paramUEventObserver)
    {
      ArrayList localArrayList = mKeysAndObservers;
      int i = 0;
      try
      {
        while (i < mKeysAndObservers.size()) {
          if (mKeysAndObservers.get(i + 1) == paramUEventObserver)
          {
            mKeysAndObservers.remove(i + 1);
            UEventObserver.nativeRemoveMatch((String)mKeysAndObservers.remove(i));
          }
          else
          {
            i += 2;
          }
        }
        return;
      }
      finally {}
    }
    
    public void run()
    {
      
      for (;;)
      {
        String str = UEventObserver.access$100();
        if (str != null) {
          sendEvent(str);
        }
      }
    }
  }
}
