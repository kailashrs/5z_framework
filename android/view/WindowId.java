package android.view;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.HashMap;

public class WindowId
  implements Parcelable
{
  public static final Parcelable.Creator<WindowId> CREATOR = new Parcelable.Creator()
  {
    public WindowId createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel = paramAnonymousParcel.readStrongBinder();
      if (paramAnonymousParcel != null) {
        paramAnonymousParcel = new WindowId(paramAnonymousParcel);
      } else {
        paramAnonymousParcel = null;
      }
      return paramAnonymousParcel;
    }
    
    public WindowId[] newArray(int paramAnonymousInt)
    {
      return new WindowId[paramAnonymousInt];
    }
  };
  private final IWindowId mToken;
  
  public WindowId(IBinder paramIBinder)
  {
    mToken = IWindowId.Stub.asInterface(paramIBinder);
  }
  
  public WindowId(IWindowId paramIWindowId)
  {
    mToken = paramIWindowId;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof WindowId)) {
      return mToken.asBinder().equals(mToken.asBinder());
    }
    return false;
  }
  
  public IWindowId getTarget()
  {
    return mToken;
  }
  
  public int hashCode()
  {
    return mToken.asBinder().hashCode();
  }
  
  public boolean isFocused()
  {
    try
    {
      boolean bool = mToken.isFocused();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void registerFocusObserver(FocusObserver paramFocusObserver)
  {
    synchronized (mRegistrations)
    {
      if (!mRegistrations.containsKey(mToken.asBinder()))
      {
        mRegistrations.put(mToken.asBinder(), this);
        try
        {
          mToken.registerFocusObserver(mIObserver);
        }
        catch (RemoteException paramFocusObserver) {}
        return;
      }
      paramFocusObserver = new java/lang/IllegalStateException;
      paramFocusObserver.<init>("Focus observer already registered with input token");
      throw paramFocusObserver;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("IntentSender{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(": ");
    localStringBuilder.append(mToken.asBinder());
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void unregisterFocusObserver(FocusObserver paramFocusObserver)
  {
    synchronized (mRegistrations)
    {
      Object localObject = mRegistrations.remove(mToken.asBinder());
      if (localObject != null)
      {
        try
        {
          mToken.unregisterFocusObserver(mIObserver);
        }
        catch (RemoteException paramFocusObserver) {}
        return;
      }
      paramFocusObserver = new java/lang/IllegalStateException;
      paramFocusObserver.<init>("Focus observer not registered with input token");
      throw paramFocusObserver;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mToken.asBinder());
  }
  
  public static abstract class FocusObserver
  {
    final Handler mHandler = new H();
    final IWindowFocusObserver.Stub mIObserver = new IWindowFocusObserver.Stub()
    {
      public void focusGained(IBinder paramAnonymousIBinder)
      {
        synchronized (mRegistrations)
        {
          paramAnonymousIBinder = (WindowId)mRegistrations.get(paramAnonymousIBinder);
          if (mHandler != null) {
            mHandler.sendMessage(mHandler.obtainMessage(1, paramAnonymousIBinder));
          } else {
            onFocusGained(paramAnonymousIBinder);
          }
          return;
        }
      }
      
      public void focusLost(IBinder paramAnonymousIBinder)
      {
        synchronized (mRegistrations)
        {
          paramAnonymousIBinder = (WindowId)mRegistrations.get(paramAnonymousIBinder);
          if (mHandler != null) {
            mHandler.sendMessage(mHandler.obtainMessage(2, paramAnonymousIBinder));
          } else {
            onFocusLost(paramAnonymousIBinder);
          }
          return;
        }
      }
    };
    final HashMap<IBinder, WindowId> mRegistrations = new HashMap();
    
    public FocusObserver() {}
    
    public abstract void onFocusGained(WindowId paramWindowId);
    
    public abstract void onFocusLost(WindowId paramWindowId);
    
    class H
      extends Handler
    {
      H() {}
      
      public void handleMessage(Message paramMessage)
      {
        switch (what)
        {
        default: 
          super.handleMessage(paramMessage);
          break;
        case 2: 
          onFocusLost((WindowId)obj);
          break;
        case 1: 
          onFocusGained((WindowId)obj);
        }
      }
    }
  }
}
