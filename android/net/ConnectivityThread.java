package android.net;

import android.os.HandlerThread;
import android.os.Looper;

public final class ConnectivityThread
  extends HandlerThread
{
  private ConnectivityThread()
  {
    super("ConnectivityThread");
  }
  
  private static ConnectivityThread createInstance()
  {
    ConnectivityThread localConnectivityThread = new ConnectivityThread();
    localConnectivityThread.start();
    return localConnectivityThread;
  }
  
  public static ConnectivityThread get()
  {
    return Singleton.INSTANCE;
  }
  
  public static Looper getInstanceLooper()
  {
    return Singleton.INSTANCE.getLooper();
  }
  
  private static class Singleton
  {
    private static final ConnectivityThread INSTANCE = ;
    
    private Singleton() {}
  }
}
