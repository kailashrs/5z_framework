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

public abstract interface IImsMMTelFeature
  extends IInterface
{
  public abstract void addRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException;
  
  public abstract ImsCallProfile createCallProfile(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract IImsCallSession createCallSession(int paramInt, ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void endSession(int paramInt)
    throws RemoteException;
  
  public abstract IImsConfig getConfigInterface()
    throws RemoteException;
  
  public abstract IImsEcbm getEcbmInterface()
    throws RemoteException;
  
  public abstract int getFeatureStatus()
    throws RemoteException;
  
  public abstract IImsMultiEndpoint getMultiEndpointInterface()
    throws RemoteException;
  
  public abstract IImsCallSession getPendingCallSession(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract IImsUt getUtInterface()
    throws RemoteException;
  
  public abstract boolean isConnected(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean isOpened()
    throws RemoteException;
  
  public abstract void removeRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException;
  
  public abstract void setUiTTYMode(int paramInt, Message paramMessage)
    throws RemoteException;
  
  public abstract int startSession(PendingIntent paramPendingIntent, IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException;
  
  public abstract void turnOffIms()
    throws RemoteException;
  
  public abstract void turnOnIms()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsMMTelFeature
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsMMTelFeature";
    static final int TRANSACTION_addRegistrationListener = 6;
    static final int TRANSACTION_createCallProfile = 8;
    static final int TRANSACTION_createCallSession = 9;
    static final int TRANSACTION_endSession = 2;
    static final int TRANSACTION_getConfigInterface = 12;
    static final int TRANSACTION_getEcbmInterface = 15;
    static final int TRANSACTION_getFeatureStatus = 5;
    static final int TRANSACTION_getMultiEndpointInterface = 17;
    static final int TRANSACTION_getPendingCallSession = 10;
    static final int TRANSACTION_getUtInterface = 11;
    static final int TRANSACTION_isConnected = 3;
    static final int TRANSACTION_isOpened = 4;
    static final int TRANSACTION_removeRegistrationListener = 7;
    static final int TRANSACTION_setUiTTYMode = 16;
    static final int TRANSACTION_startSession = 1;
    static final int TRANSACTION_turnOffIms = 14;
    static final int TRANSACTION_turnOnIms = 13;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsMMTelFeature");
    }
    
    public static IImsMMTelFeature asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsMMTelFeature");
      if ((localIInterface != null) && ((localIInterface instanceof IImsMMTelFeature))) {
        return (IImsMMTelFeature)localIInterface;
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
        Object localObject6 = null;
        Object localObject7 = null;
        IImsCallSession localIImsCallSession = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 17: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          localObject6 = getMultiEndpointInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localIImsCallSession;
          if (localObject6 != null) {
            paramParcel1 = ((IImsMultiEndpoint)localObject6).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Message)Message.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          setUiTTYMode(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          localObject6 = getEcbmInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localObject2;
          if (localObject6 != null) {
            paramParcel1 = ((IImsEcbm)localObject6).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          turnOffIms();
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          turnOnIms();
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          localObject6 = getConfigInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localObject3;
          if (localObject6 != null) {
            paramParcel1 = ((IImsConfig)localObject6).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          localObject6 = getUtInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localObject4;
          if (localObject6 != null) {
            paramParcel1 = ((IImsUt)localObject6).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          localObject6 = getPendingCallSession(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = localObject5;
          if (localObject6 != null) {
            paramParcel1 = ((IImsCallSession)localObject6).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localIImsCallSession = createCallSession(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject6;
          if (localIImsCallSession != null) {
            paramParcel1 = localIImsCallSession.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
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
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          removeRegistrationListener(IImsRegistrationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          addRegistrationListener(IImsRegistrationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          paramInt1 = getFeatureStatus();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          paramInt1 = isOpened();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          paramInt1 = isConnected(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
          endSession(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsMMTelFeature");
        if (paramParcel1.readInt() != 0) {
          localObject6 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject6 = localObject7;
        }
        paramInt1 = startSession((PendingIntent)localObject6, IImsRegistrationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsMMTelFeature");
      return true;
    }
    
    private static class Proxy
      implements IImsMMTelFeature
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
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
      
      public ImsCallProfile createCallProfile(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public IImsCallSession createCallSession(int paramInt, ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
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
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public void endSession(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
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
      
      public IImsConfig getConfigInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          mRemote.transact(12, localParcel1, localParcel2, 0);
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
      
      public IImsEcbm getEcbmInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
      
      public int getFeatureStatus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.IImsMMTelFeature";
      }
      
      public IImsMultiEndpoint getMultiEndpointInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
      
      public IImsUt getUtInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          mRemote.transact(11, localParcel1, localParcel2, 0);
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
      
      public boolean isConnected(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public boolean isOpened()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
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
      
      public void removeRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          if (paramIImsRegistrationListener != null) {
            paramIImsRegistrationListener = paramIImsRegistrationListener.asBinder();
          } else {
            paramIImsRegistrationListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsRegistrationListener);
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
      
      public void setUiTTYMode(int paramInt, Message paramMessage)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          localParcel1.writeInt(paramInt);
          if (paramMessage != null)
          {
            localParcel1.writeInt(1);
            paramMessage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startSession(PendingIntent paramPendingIntent, IImsRegistrationListener paramIImsRegistrationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
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
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void turnOffIms()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void turnOnIms()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsMMTelFeature");
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
    }
  }
}
