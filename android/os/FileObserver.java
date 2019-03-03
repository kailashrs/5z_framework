package android.os;

import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public abstract class FileObserver
{
  public static final int ACCESS = 1;
  public static final int ALL_EVENTS = 4095;
  public static final int ATTRIB = 4;
  public static final int CLOSE_NOWRITE = 16;
  public static final int CLOSE_WRITE = 8;
  public static final int CREATE = 256;
  public static final int DELETE = 512;
  public static final int DELETE_SELF = 1024;
  private static final String LOG_TAG = "FileObserver";
  public static final int MODIFY = 2;
  public static final int MOVED_FROM = 64;
  public static final int MOVED_TO = 128;
  public static final int MOVE_SELF = 2048;
  public static final int OPEN = 32;
  private static ObserverThread s_observerThread = new ObserverThread();
  private Integer m_descriptor;
  private int m_mask;
  private String m_path;
  
  static
  {
    s_observerThread.start();
  }
  
  public FileObserver(String paramString)
  {
    this(paramString, 4095);
  }
  
  public FileObserver(String paramString, int paramInt)
  {
    m_path = paramString;
    m_mask = paramInt;
    m_descriptor = Integer.valueOf(-1);
  }
  
  protected void finalize()
  {
    stopWatching();
  }
  
  public abstract void onEvent(int paramInt, String paramString);
  
  public void startWatching()
  {
    if (m_descriptor.intValue() < 0) {
      m_descriptor = Integer.valueOf(s_observerThread.startWatching(m_path, m_mask, this));
    }
  }
  
  public void stopWatching()
  {
    if (m_descriptor.intValue() >= 0)
    {
      s_observerThread.stopWatching(m_descriptor.intValue());
      m_descriptor = Integer.valueOf(-1);
    }
  }
  
  private static class ObserverThread
    extends Thread
  {
    private int m_fd = init();
    private HashMap<Integer, WeakReference> m_observers = new HashMap();
    
    public ObserverThread()
    {
      super();
    }
    
    private native int init();
    
    private native void observe(int paramInt);
    
    private native int startWatching(int paramInt1, String paramString, int paramInt2);
    
    private native void stopWatching(int paramInt1, int paramInt2);
    
    public void onEvent(int paramInt1, int paramInt2, String paramString)
    {
      Object localObject1 = null;
      synchronized (m_observers)
      {
        Object localObject2 = (WeakReference)m_observers.get(Integer.valueOf(paramInt1));
        if (localObject2 != null)
        {
          localObject2 = (FileObserver)((WeakReference)localObject2).get();
          localObject1 = localObject2;
          if (localObject2 == null)
          {
            m_observers.remove(Integer.valueOf(paramInt1));
            localObject1 = localObject2;
          }
        }
        if (localObject1 != null) {
          try
          {
            localObject1.onEvent(paramInt2, paramString);
          }
          catch (Throwable localThrowable)
          {
            paramString = new StringBuilder();
            paramString.append("Unhandled exception in FileObserver ");
            paramString.append(localObject1);
            Log.wtf("FileObserver", paramString.toString(), localThrowable);
          }
        }
        return;
      }
    }
    
    public void run()
    {
      observe(m_fd);
    }
    
    public int startWatching(String arg1, int paramInt, FileObserver paramFileObserver)
    {
      paramInt = startWatching(m_fd, ???, paramInt);
      Integer localInteger = new Integer(paramInt);
      if (paramInt >= 0) {
        synchronized (m_observers)
        {
          HashMap localHashMap = m_observers;
          WeakReference localWeakReference = new java/lang/ref/WeakReference;
          localWeakReference.<init>(paramFileObserver);
          localHashMap.put(localInteger, localWeakReference);
        }
      }
      return localInteger.intValue();
    }
    
    public void stopWatching(int paramInt)
    {
      stopWatching(m_fd, paramInt);
    }
  }
}
