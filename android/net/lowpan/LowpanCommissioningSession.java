package android.net.lowpan;

import android.net.IpPrefix;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

public class LowpanCommissioningSession
{
  private final LowpanBeaconInfo mBeaconInfo;
  private final ILowpanInterface mBinder;
  private Callback mCallback = null;
  private Handler mHandler;
  private final ILowpanInterfaceListener mInternalCallback = new InternalCallback(null);
  private volatile boolean mIsClosed = false;
  private final Looper mLooper;
  
  LowpanCommissioningSession(ILowpanInterface paramILowpanInterface, LowpanBeaconInfo paramLowpanBeaconInfo, Looper paramLooper)
  {
    mBinder = paramILowpanInterface;
    mBeaconInfo = paramLowpanBeaconInfo;
    mLooper = paramLooper;
    if (mLooper != null) {
      mHandler = new Handler(mLooper);
    } else {
      mHandler = new Handler();
    }
    try
    {
      mBinder.addListener(mInternalCallback);
      return;
    }
    catch (RemoteException paramILowpanInterface)
    {
      throw paramILowpanInterface.rethrowAsRuntimeException();
    }
  }
  
  private void lockedCleanup()
  {
    if (!mIsClosed)
    {
      try
      {
        mBinder.removeListener(mInternalCallback);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowAsRuntimeException();
      }
      catch (DeadObjectException localDeadObjectException) {}
      if (mCallback != null) {
        mHandler.post(new _..Lambda.LowpanCommissioningSession.jqpl_iUq_e7YuWqkG33P8PNe7Ag(this));
      }
    }
    mCallback = null;
    mIsClosed = true;
  }
  
  public void close()
  {
    try
    {
      boolean bool = mIsClosed;
      if (!bool) {
        try
        {
          mBinder.closeCommissioningSession();
          lockedCleanup();
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowAsRuntimeException();
        }
        catch (DeadObjectException localDeadObjectException) {}
      }
      return;
    }
    finally {}
  }
  
  public LowpanBeaconInfo getBeaconInfo()
  {
    return mBeaconInfo;
  }
  
  public void sendToCommissioner(byte[] paramArrayOfByte)
  {
    if (!mIsClosed) {
      try
      {
        mBinder.sendToCommissioner(paramArrayOfByte);
      }
      catch (RemoteException paramArrayOfByte)
      {
        throw paramArrayOfByte.rethrowAsRuntimeException();
      }
      catch (DeadObjectException paramArrayOfByte) {}
    }
  }
  
  public void setCallback(Callback paramCallback, Handler paramHandler)
  {
    try
    {
      if (!mIsClosed)
      {
        if (paramHandler != null)
        {
          mHandler = paramHandler;
        }
        else if (mLooper != null)
        {
          paramHandler = new android/os/Handler;
          paramHandler.<init>(mLooper);
          mHandler = paramHandler;
        }
        else
        {
          paramHandler = new android/os/Handler;
          paramHandler.<init>();
          mHandler = paramHandler;
        }
        mCallback = paramCallback;
      }
      return;
    }
    finally {}
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onClosed() {}
    
    public void onReceiveFromCommissioner(byte[] paramArrayOfByte) {}
  }
  
  private class InternalCallback
    extends ILowpanInterfaceListener.Stub
  {
    private InternalCallback() {}
    
    public void onConnectedChanged(boolean paramBoolean) {}
    
    public void onEnabledChanged(boolean paramBoolean) {}
    
    public void onLinkAddressAdded(String paramString) {}
    
    public void onLinkAddressRemoved(String paramString) {}
    
    public void onLinkNetworkAdded(IpPrefix paramIpPrefix) {}
    
    public void onLinkNetworkRemoved(IpPrefix paramIpPrefix) {}
    
    public void onLowpanIdentityChanged(LowpanIdentity paramLowpanIdentity) {}
    
    public void onReceiveFromCommissioner(byte[] paramArrayOfByte)
    {
      mHandler.post(new _..Lambda.LowpanCommissioningSession.InternalCallback.TrrmDykqIWeXNdgrXO7t2_rqCTo(this, paramArrayOfByte));
    }
    
    public void onRoleChanged(String paramString) {}
    
    public void onStateChanged(String paramString)
    {
      if (!mIsClosed)
      {
        int i = -1;
        int j = paramString.hashCode();
        if (j != -1548612125)
        {
          if ((j == 97204770) && (paramString.equals("fault"))) {
            i = 1;
          }
        }
        else if (paramString.equals("offline")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          break;
        case 0: 
        case 1: 
          synchronized (LowpanCommissioningSession.this)
          {
            LowpanCommissioningSession.this.lockedCleanup();
          }
        }
      }
    }
    
    public void onUpChanged(boolean paramBoolean) {}
  }
}
