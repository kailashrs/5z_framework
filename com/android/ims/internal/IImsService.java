package com.android.ims.internal;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;

public abstract interface IImsService
  extends IInterface
{
  public abstract void addRegistrationListener(int paramInt1, int paramInt2, IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException;
  
  public abstract void close(int paramInt)
    throws RemoteException;
  
  public abstract ImsCallProfile createCallProfile(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract IImsCallSession createCallSession(int paramInt, ImsCallProfile paramImsCallProfile, IImsCallSessionListener paramIImsCallSessionListener)
    throws RemoteException;
  
  public abstract IImsConfig getConfigInterface(int paramInt)
    throws RemoteException;
  
  public abstract IImsEcbm getEcbmInterface(int paramInt)
    throws RemoteException;
  
  public abstract IImsMultiEndpoint getMultiEndpointInterface(int paramInt)
    throws RemoteException;
  
  public abstract IImsCallSession getPendingCallSession(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract IImsUt getUtInterface(int paramInt)
    throws RemoteException;
  
  public abstract boolean isConnected(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean isOpened(int paramInt)
    throws RemoteException;
  
  public abstract int open(int paramInt1, int paramInt2, PendingIntent paramPendingIntent, IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException;
  
  public abstract void setRegistrationListener(int paramInt, IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException;
  
  public abstract void setUiTTYMode(int paramInt1, int paramInt2, Message paramMessage)
    throws RemoteException;
  
  public abstract void turnOffIms(int paramInt)
    throws RemoteException;
  
  public abstract void turnOnIms(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsService
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsService";
    static final int TRANSACTION_addRegistrationListener = 6;
    static final int TRANSACTION_close = 2;
    static final int TRANSACTION_createCallProfile = 7;
    static final int TRANSACTION_createCallSession = 8;
    static final int TRANSACTION_getConfigInterface = 11;
    static final int TRANSACTION_getEcbmInterface = 14;
    static final int TRANSACTION_getMultiEndpointInterface = 16;
    static final int TRANSACTION_getPendingCallSession = 9;
    static final int TRANSACTION_getUtInterface = 10;
    static final int TRANSACTION_isConnected = 3;
    static final int TRANSACTION_isOpened = 4;
    static final int TRANSACTION_open = 1;
    static final int TRANSACTION_setRegistrationListener = 5;
    static final int TRANSACTION_setUiTTYMode = 15;
    static final int TRANSACTION_turnOffIms = 13;
    static final int TRANSACTION_turnOnIms = 12;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsService");
    }
    
    public static IImsService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsService");
      if ((localIInterface != null) && ((localIInterface instanceof IImsService))) {
        return (IImsService)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        IImsMultiEndpoint localIImsMultiEndpoint = null;
        Object localObject6 = null;
        Object localObject7 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 16: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          localIImsMultiEndpoint = getMultiEndpointInterface(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject7;
          if (localIImsMultiEndpoint != null) {
            paramParcel1 = localIImsMultiEndpoint.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Message)Message.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          setUiTTYMode(paramInt1, paramInt2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          localObject7 = getEcbmInterface(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localObject2;
          if (localObject7 != null) {
            paramParcel1 = ((IImsEcbm)localObject7).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          turnOffIms(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          turnOnIms(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          localObject7 = getConfigInterface(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localObject3;
          if (localObject7 != null) {
            paramParcel1 = ((IImsConfig)localObject7).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          localObject7 = getUtInterface(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localObject4;
          if (localObject7 != null) {
            paramParcel1 = ((IImsUt)localObject7).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          localObject7 = getPendingCallSession(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = localObject5;
          if (localObject7 != null) {
            paramParcel1 = ((IImsCallSession)localObject7).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject7 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject7 = null;
          }
          localObject7 = createCallSession(paramInt1, (ImsCallProfile)localObject7, IImsCallSessionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localIImsMultiEndpoint;
          if (localObject7 != null) {
            paramParcel1 = ((IImsCallSession)localObject7).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          paramParcel1 = createCallProfile(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          addRegistrationListener(paramParcel1.readInt(), paramParcel1.readInt(), IImsRegistrationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          setRegistrationListener(paramParcel1.readInt(), IImsRegistrationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          paramInt1 = isOpened(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          paramInt1 = isConnected(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
          close(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsService");
        paramInt2 = paramParcel1.readInt();
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          localObject7 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject7 = localObject6;
        }
        paramInt1 = open(paramInt2, paramInt1, (PendingIntent)localObject7, IImsRegistrationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsService");
      return true;
    }
    
    private static class Proxy
      implements IImsService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addRegistrationListener(int paramInt1, int paramInt2, IImsRegistrationListener paramIImsRegistrationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIImsRegistrationListener != null) {
            paramIImsRegistrationListener = paramIImsRegistrationListener.asBinder();
          } else {
            paramIImsRegistrationListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsRegistrationListener);
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
      
      public void close(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ImsCallProfile createCallProfile(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ImsCallProfile localImsCallProfile;
          if (localParcel2.readInt() != 0) {
            localImsCallProfile = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(localParcel2);
          } else {
            localImsCallProfile = null;
          }
          return localImsCallProfile;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsCallSession createCallSession(int paramInt, ImsCallProfile paramImsCallProfile, IImsCallSessionListener paramIImsCallSessionListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          if (paramImsCallProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIImsCallSessionListener != null) {
            paramImsCallProfile = paramIImsCallSessionListener.asBinder();
          } else {
            paramImsCallProfile = null;
          }
          localParcel1.writeStrongBinder(paramImsCallProfile);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramImsCallProfile = IImsCallSession.Stub.asInterface(localParcel2.readStrongBinder());
          return paramImsCallProfile;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsConfig getConfigInterface(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsConfig localIImsConfig = IImsConfig.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsEcbm getEcbmInterface(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsEcbm localIImsEcbm = IImsEcbm.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsEcbm;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.IImsService";
      }
      
      public IImsMultiEndpoint getMultiEndpointInterface(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsMultiEndpoint localIImsMultiEndpoint = IImsMultiEndpoint.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsMultiEndpoint;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsCallSession getPendingCallSession(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IImsCallSession.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsUt getUtInterface(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsUt localIImsUt = IImsUt.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsUt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isConnected(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean isOpened(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public int open(int paramInt1, int paramInt2, PendingIntent paramPendingIntent, IImsRegistrationListener paramIImsRegistrationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIImsRegistrationListener != null) {
            paramPendingIntent = paramIImsRegistrationListener.asBinder();
          } else {
            paramPendingIntent = null;
          }
          localParcel1.writeStrongBinder(paramPendingIntent);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRegistrationListener(int paramInt, IImsRegistrationListener paramIImsRegistrationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          if (paramIImsRegistrationListener != null) {
            paramIImsRegistrationListener = paramIImsRegistrationListener.asBinder();
          } else {
            paramIImsRegistrationListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsRegistrationListener);
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
      
      public void setUiTTYMode(int paramInt1, int paramInt2, Message paramMessage)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramMessage != null)
          {
            localParcel1.writeInt(1);
            paramMessage.writeToParcel(localParcel1, 0);
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
      
      public void turnOffIms(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void turnOnIms(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsService");
          localParcel1.writeInt(paramInt);
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
    }
  }
}
