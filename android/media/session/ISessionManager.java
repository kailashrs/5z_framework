package android.media.session;

import android.content.ComponentName;
import android.media.IRemoteVolumeController;
import android.media.IRemoteVolumeController.Stub;
import android.media.ISessionTokensListener;
import android.media.ISessionTokensListener.Stub;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.KeyEvent;
import java.util.List;

public abstract interface ISessionManager
  extends IInterface
{
  public abstract void addSessionTokensListener(ISessionTokensListener paramISessionTokensListener, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void addSessionsListener(IActiveSessionsListener paramIActiveSessionsListener, ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract ISession createSession(String paramString1, ISessionCallback paramISessionCallback, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract boolean createSession2(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void destroySession2(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void dispatchAdjustVolume(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void dispatchMediaKeyEvent(String paramString, boolean paramBoolean1, KeyEvent paramKeyEvent, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void dispatchVolumeKeyEvent(String paramString, boolean paramBoolean1, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract List<Bundle> getSessionTokens(boolean paramBoolean1, boolean paramBoolean2, String paramString)
    throws RemoteException;
  
  public abstract List<IBinder> getSessions(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean isGlobalPriorityActive()
    throws RemoteException;
  
  public abstract boolean isTrusted(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void removeSessionTokensListener(ISessionTokensListener paramISessionTokensListener, String paramString)
    throws RemoteException;
  
  public abstract void removeSessionsListener(IActiveSessionsListener paramIActiveSessionsListener)
    throws RemoteException;
  
  public abstract void setCallback(ICallback paramICallback)
    throws RemoteException;
  
  public abstract void setOnMediaKeyListener(IOnMediaKeyListener paramIOnMediaKeyListener)
    throws RemoteException;
  
  public abstract void setOnVolumeKeyLongPressListener(IOnVolumeKeyLongPressListener paramIOnVolumeKeyLongPressListener)
    throws RemoteException;
  
  public abstract void setRemoteVolumeController(IRemoteVolumeController paramIRemoteVolumeController)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISessionManager
  {
    private static final String DESCRIPTOR = "android.media.session.ISessionManager";
    static final int TRANSACTION_addSessionTokensListener = 17;
    static final int TRANSACTION_addSessionsListener = 6;
    static final int TRANSACTION_createSession = 1;
    static final int TRANSACTION_createSession2 = 14;
    static final int TRANSACTION_destroySession2 = 15;
    static final int TRANSACTION_dispatchAdjustVolume = 5;
    static final int TRANSACTION_dispatchMediaKeyEvent = 3;
    static final int TRANSACTION_dispatchVolumeKeyEvent = 4;
    static final int TRANSACTION_getSessionTokens = 16;
    static final int TRANSACTION_getSessions = 2;
    static final int TRANSACTION_isGlobalPriorityActive = 9;
    static final int TRANSACTION_isTrusted = 13;
    static final int TRANSACTION_removeSessionTokensListener = 18;
    static final int TRANSACTION_removeSessionsListener = 7;
    static final int TRANSACTION_setCallback = 10;
    static final int TRANSACTION_setOnMediaKeyListener = 12;
    static final int TRANSACTION_setOnVolumeKeyLongPressListener = 11;
    static final int TRANSACTION_setRemoteVolumeController = 8;
    
    public Stub()
    {
      attachInterface(this, "android.media.session.ISessionManager");
    }
    
    public static ISessionManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.session.ISessionManager");
      if ((localIInterface != null) && ((localIInterface instanceof ISessionManager))) {
        return (ISessionManager)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        String str = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        boolean bool3;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 18: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          removeSessionTokensListener(ISessionTokensListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          addSessionTokensListener(ISessionTokensListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramParcel1 = getSessionTokens(bool3, bool2, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          destroySession2(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = createSession2(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          paramInt1 = isTrusted(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          setOnMediaKeyListener(IOnMediaKeyListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          setOnVolumeKeyLongPressListener(IOnVolumeKeyLongPressListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          setCallback(ICallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          paramInt1 = isGlobalPriorityActive();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          setRemoteVolumeController(IRemoteVolumeController.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          removeSessionsListener(IActiveSessionsListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          localObject3 = IActiveSessionsListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          }
          addSessionsListener((IActiveSessionsListener)localObject3, (ComponentName)localObject2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          dispatchAdjustVolume(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject3) {
            break;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          dispatchVolumeKeyEvent(str, bool3, (KeyEvent)localObject2, paramInt1, bool2);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = str;
          }
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          dispatchMediaKeyEvent((String)localObject3, bool3, (KeyEvent)localObject2, bool2);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.session.ISessionManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject4;
          }
          paramParcel1 = getSessions((ComponentName)localObject2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeBinderList(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.session.ISessionManager");
        localObject2 = createSession(paramParcel1.readString(), ISessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel1 = localObject5;
        if (localObject2 != null) {
          paramParcel1 = ((ISession)localObject2).asBinder();
        }
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.session.ISessionManager");
      return true;
    }
    
    private static class Proxy
      implements ISessionManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addSessionTokensListener(ISessionTokensListener paramISessionTokensListener, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramISessionTokensListener != null) {
            paramISessionTokensListener = paramISessionTokensListener.asBinder();
          } else {
            paramISessionTokensListener = null;
          }
          localParcel1.writeStrongBinder(paramISessionTokensListener);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addSessionsListener(IActiveSessionsListener paramIActiveSessionsListener, ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramIActiveSessionsListener != null) {
            paramIActiveSessionsListener = paramIActiveSessionsListener.asBinder();
          } else {
            paramIActiveSessionsListener = null;
          }
          localParcel1.writeStrongBinder(paramIActiveSessionsListener);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public ISession createSession(String paramString1, ISessionCallback paramISessionCallback, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          localParcel1.writeString(paramString1);
          if (paramISessionCallback != null) {
            paramString1 = paramISessionCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = ISession.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean createSession2(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void destroySession2(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dispatchAdjustVolume(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dispatchMediaKeyEvent(String paramString, boolean paramBoolean1, KeyEvent paramKeyEvent, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean1);
          if (paramKeyEvent != null)
          {
            localParcel1.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dispatchVolumeKeyEvent(String paramString, boolean paramBoolean1, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean1);
          if (paramKeyEvent != null)
          {
            localParcel1.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.session.ISessionManager";
      }
      
      public List<Bundle> getSessionTokens(boolean paramBoolean1, boolean paramBoolean2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeString(paramString);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(Bundle.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<IBinder> getSessions(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createBinderArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isGlobalPriorityActive()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isTrusted(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeSessionTokensListener(ISessionTokensListener paramISessionTokensListener, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramISessionTokensListener != null) {
            paramISessionTokensListener = paramISessionTokensListener.asBinder();
          } else {
            paramISessionTokensListener = null;
          }
          localParcel1.writeStrongBinder(paramISessionTokensListener);
          localParcel1.writeString(paramString);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeSessionsListener(IActiveSessionsListener paramIActiveSessionsListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramIActiveSessionsListener != null) {
            paramIActiveSessionsListener = paramIActiveSessionsListener.asBinder();
          } else {
            paramIActiveSessionsListener = null;
          }
          localParcel1.writeStrongBinder(paramIActiveSessionsListener);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCallback(ICallback paramICallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramICallback != null) {
            paramICallback = paramICallback.asBinder();
          } else {
            paramICallback = null;
          }
          localParcel1.writeStrongBinder(paramICallback);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOnMediaKeyListener(IOnMediaKeyListener paramIOnMediaKeyListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramIOnMediaKeyListener != null) {
            paramIOnMediaKeyListener = paramIOnMediaKeyListener.asBinder();
          } else {
            paramIOnMediaKeyListener = null;
          }
          localParcel1.writeStrongBinder(paramIOnMediaKeyListener);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOnVolumeKeyLongPressListener(IOnVolumeKeyLongPressListener paramIOnVolumeKeyLongPressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramIOnVolumeKeyLongPressListener != null) {
            paramIOnVolumeKeyLongPressListener = paramIOnVolumeKeyLongPressListener.asBinder();
          } else {
            paramIOnVolumeKeyLongPressListener = null;
          }
          localParcel1.writeStrongBinder(paramIOnVolumeKeyLongPressListener);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRemoteVolumeController(IRemoteVolumeController paramIRemoteVolumeController)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionManager");
          if (paramIRemoteVolumeController != null) {
            paramIRemoteVolumeController = paramIRemoteVolumeController.asBinder();
          } else {
            paramIRemoteVolumeController = null;
          }
          localParcel1.writeStrongBinder(paramIRemoteVolumeController);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
