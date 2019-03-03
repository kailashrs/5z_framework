package android.se.omapi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;

public final class SEService
{
  public static final int IO_ERROR = 1;
  public static final int NO_SUCH_ELEMENT_ERROR = 2;
  private static final String TAG = "OMAPI.SEService";
  private ServiceConnection mConnection;
  private final Context mContext;
  private final Object mLock = new Object();
  private final HashMap<String, Reader> mReaders = new HashMap();
  private SEListener mSEListener = new SEListener(null);
  private volatile ISecureElementService mSecureElementService;
  
  public SEService(Context paramContext, Executor paramExecutor, OnConnectedListener paramOnConnectedListener)
  {
    if ((paramContext != null) && (paramOnConnectedListener != null) && (paramExecutor != null))
    {
      mContext = paramContext;
      mSEListener.mListener = paramOnConnectedListener;
      mSEListener.mExecutor = paramExecutor;
      mConnection = new ServiceConnection()
      {
        public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
        {
          try
          {
            SEService.access$102(SEService.this, ISecureElementService.Stub.asInterface(paramAnonymousIBinder));
            if (mSEListener != null) {
              mSEListener.onConnected();
            }
            Log.i("OMAPI.SEService", "Service onServiceConnected");
            return;
          }
          finally {}
        }
        
        public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
        {
          SEService.access$102(SEService.this, null);
          Log.i("OMAPI.SEService", "Service onServiceDisconnected");
        }
      };
      paramContext = new Intent(ISecureElementService.class.getName());
      paramContext.setClassName("com.android.se", "com.android.se.SecureElementService");
      if (mContext.bindService(paramContext, mConnection, 1)) {
        Log.i("OMAPI.SEService", "bindService successful");
      }
      return;
    }
    throw new NullPointerException("Arguments must not be null");
  }
  
  private ISecureElementReader getReader(String paramString)
  {
    try
    {
      paramString = mSecureElementService.getReader(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw new IllegalStateException(paramString.getMessage());
    }
  }
  
  ISecureElementListener getListener()
  {
    return mSEListener;
  }
  
  public Reader[] getReaders()
  {
    if (mSecureElementService != null) {
      try
      {
        String[] arrayOfString = mSecureElementService.getReaders();
        Reader[] arrayOfReader = new Reader[arrayOfString.length];
        int i = 0;
        int j = arrayOfString.length;
        for (int k = 0; k < j; k++)
        {
          String str = arrayOfString[k];
          int m;
          if (mReaders.get(str) == null)
          {
            Object localObject;
            try
            {
              HashMap localHashMap = mReaders;
              localObject = new android/se/omapi/Reader;
              ((Reader)localObject).<init>(this, str, getReader(str));
              localHashMap.put(str, localObject);
              m = i + 1;
              try
              {
                arrayOfReader[i] = ((Reader)mReaders.get(str));
                i = m;
              }
              catch (Exception localException1)
              {
                i = m;
              }
              localObject = new StringBuilder();
            }
            catch (Exception localException2) {}
            ((StringBuilder)localObject).append("Error adding Reader: ");
            ((StringBuilder)localObject).append(str);
            Log.e("OMAPI.SEService", ((StringBuilder)localObject).toString(), localException2);
          }
          else
          {
            m = i + 1;
            arrayOfReader[i] = ((Reader)mReaders.get(str));
            i = m;
          }
        }
        return arrayOfReader;
      }
      catch (RemoteException localRemoteException)
      {
        throw new RuntimeException(localRemoteException);
      }
    }
    throw new IllegalStateException("service not connected to system");
  }
  
  public String getVersion()
  {
    return "3.3";
  }
  
  public boolean isConnected()
  {
    boolean bool;
    if (mSecureElementService != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void shutdown()
  {
    synchronized (mLock)
    {
      if (mSecureElementService != null)
      {
        Iterator localIterator = mReaders.values().iterator();
        while (localIterator.hasNext())
        {
          Reader localReader = (Reader)localIterator.next();
          try
          {
            localReader.closeSessions();
          }
          catch (Exception localException) {}
        }
      }
      try
      {
        mContext.unbindService(mConnection);
      }
      catch (IllegalArgumentException localIllegalArgumentException) {}
      mSecureElementService = null;
      return;
    }
  }
  
  public static abstract interface OnConnectedListener
  {
    public abstract void onConnected();
  }
  
  private class SEListener
    extends ISecureElementListener.Stub
  {
    public Executor mExecutor = null;
    public SEService.OnConnectedListener mListener = null;
    
    private SEListener() {}
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public void onConnected()
    {
      if ((mListener != null) && (mExecutor != null)) {
        mExecutor.execute(new Runnable()
        {
          public void run()
          {
            mListener.onConnected();
          }
        });
      }
    }
  }
}
