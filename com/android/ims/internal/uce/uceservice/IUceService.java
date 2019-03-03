package com.android.ims.internal.uce.uceservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.UceLong;
import com.android.ims.internal.uce.options.IOptionsListener;
import com.android.ims.internal.uce.options.IOptionsListener.Stub;
import com.android.ims.internal.uce.options.IOptionsService;
import com.android.ims.internal.uce.options.IOptionsService.Stub;
import com.android.ims.internal.uce.presence.IPresenceListener;
import com.android.ims.internal.uce.presence.IPresenceListener.Stub;
import com.android.ims.internal.uce.presence.IPresenceService;
import com.android.ims.internal.uce.presence.IPresenceService.Stub;

public abstract interface IUceService
  extends IInterface
{
  public abstract int createOptionsService(IOptionsListener paramIOptionsListener, UceLong paramUceLong)
    throws RemoteException;
  
  public abstract int createOptionsServiceForSubscription(IOptionsListener paramIOptionsListener, UceLong paramUceLong, String paramString)
    throws RemoteException;
  
  public abstract int createPresenceService(IPresenceListener paramIPresenceListener, UceLong paramUceLong)
    throws RemoteException;
  
  public abstract int createPresenceServiceForSubscription(IPresenceListener paramIPresenceListener, UceLong paramUceLong, String paramString)
    throws RemoteException;
  
  public abstract void destroyOptionsService(int paramInt)
    throws RemoteException;
  
  public abstract void destroyPresenceService(int paramInt)
    throws RemoteException;
  
  public abstract IOptionsService getOptionsService()
    throws RemoteException;
  
  public abstract IOptionsService getOptionsServiceForSubscription(String paramString)
    throws RemoteException;
  
  public abstract IPresenceService getPresenceService()
    throws RemoteException;
  
  public abstract IPresenceService getPresenceServiceForSubscription(String paramString)
    throws RemoteException;
  
  public abstract boolean getServiceStatus()
    throws RemoteException;
  
  public abstract boolean isServiceStarted()
    throws RemoteException;
  
  public abstract boolean startService(IUceListener paramIUceListener)
    throws RemoteException;
  
  public abstract boolean stopService()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUceService
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.uce.uceservice.IUceService";
    static final int TRANSACTION_createOptionsService = 4;
    static final int TRANSACTION_createOptionsServiceForSubscription = 5;
    static final int TRANSACTION_createPresenceService = 7;
    static final int TRANSACTION_createPresenceServiceForSubscription = 8;
    static final int TRANSACTION_destroyOptionsService = 6;
    static final int TRANSACTION_destroyPresenceService = 9;
    static final int TRANSACTION_getOptionsService = 13;
    static final int TRANSACTION_getOptionsServiceForSubscription = 14;
    static final int TRANSACTION_getPresenceService = 11;
    static final int TRANSACTION_getPresenceServiceForSubscription = 12;
    static final int TRANSACTION_getServiceStatus = 10;
    static final int TRANSACTION_isServiceStarted = 3;
    static final int TRANSACTION_startService = 1;
    static final int TRANSACTION_stopService = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.uce.uceservice.IUceService");
    }
    
    public static IUceService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.uce.uceservice.IUceService");
      if ((localIInterface != null) && ((localIInterface instanceof IUceService))) {
        return (IUceService)localIInterface;
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
        Object localObject8 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 14: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject1 = getOptionsServiceForSubscription(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject8;
          if (localObject1 != null) {
            paramParcel1 = ((IOptionsService)localObject1).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject8 = getOptionsService();
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject1;
          if (localObject8 != null) {
            paramParcel1 = ((IOptionsService)localObject8).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject8 = getPresenceServiceForSubscription(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = localObject2;
          if (localObject8 != null) {
            paramParcel1 = ((IPresenceService)localObject8).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject8 = getPresenceService();
          paramParcel2.writeNoException();
          paramParcel1 = localObject3;
          if (localObject8 != null) {
            paramParcel1 = ((IPresenceService)localObject8).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          paramInt1 = getServiceStatus();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          destroyPresenceService(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject1 = IPresenceListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UceLong)UceLong.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject4;
          }
          paramInt1 = createPresenceServiceForSubscription((IPresenceListener)localObject1, (UceLong)localObject8, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          if (localObject8 != null)
          {
            paramParcel2.writeInt(1);
            ((UceLong)localObject8).writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject8 = IPresenceListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UceLong)UceLong.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramInt1 = createPresenceService((IPresenceListener)localObject8, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          destroyOptionsService(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject1 = IOptionsListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UceLong)UceLong.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject6;
          }
          paramInt1 = createOptionsServiceForSubscription((IOptionsListener)localObject1, (UceLong)localObject8, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          if (localObject8 != null)
          {
            paramParcel2.writeInt(1);
            ((UceLong)localObject8).writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          localObject8 = IOptionsListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UceLong)UceLong.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramInt1 = createOptionsService((IOptionsListener)localObject8, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
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
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          paramInt1 = isServiceStarted();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
          paramInt1 = stopService();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.uce.uceservice.IUceService");
        paramInt1 = startService(IUceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.uce.uceservice.IUceService");
      return true;
    }
    
    private static class Proxy
      implements IUceService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public int createOptionsService(IOptionsListener paramIOptionsListener, UceLong paramUceLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          if (paramIOptionsListener != null) {
            paramIOptionsListener = paramIOptionsListener.asBinder();
          } else {
            paramIOptionsListener = null;
          }
          localParcel1.writeStrongBinder(paramIOptionsListener);
          if (paramUceLong != null)
          {
            localParcel1.writeInt(1);
            paramUceLong.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramUceLong.readFromParcel(localParcel2);
          }
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int createOptionsServiceForSubscription(IOptionsListener paramIOptionsListener, UceLong paramUceLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          if (paramIOptionsListener != null) {
            paramIOptionsListener = paramIOptionsListener.asBinder();
          } else {
            paramIOptionsListener = null;
          }
          localParcel1.writeStrongBinder(paramIOptionsListener);
          if (paramUceLong != null)
          {
            localParcel1.writeInt(1);
            paramUceLong.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramUceLong.readFromParcel(localParcel2);
          }
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int createPresenceService(IPresenceListener paramIPresenceListener, UceLong paramUceLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          if (paramIPresenceListener != null) {
            paramIPresenceListener = paramIPresenceListener.asBinder();
          } else {
            paramIPresenceListener = null;
          }
          localParcel1.writeStrongBinder(paramIPresenceListener);
          if (paramUceLong != null)
          {
            localParcel1.writeInt(1);
            paramUceLong.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramUceLong.readFromParcel(localParcel2);
          }
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int createPresenceServiceForSubscription(IPresenceListener paramIPresenceListener, UceLong paramUceLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          if (paramIPresenceListener != null) {
            paramIPresenceListener = paramIPresenceListener.asBinder();
          } else {
            paramIPresenceListener = null;
          }
          localParcel1.writeStrongBinder(paramIPresenceListener);
          if (paramUceLong != null)
          {
            localParcel1.writeInt(1);
            paramUceLong.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramUceLong.readFromParcel(localParcel2);
          }
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void destroyOptionsService(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
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
      
      public void destroyPresenceService(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
        return "com.android.ims.internal.uce.uceservice.IUceService";
      }
      
      public IOptionsService getOptionsService()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IOptionsService localIOptionsService = IOptionsService.Stub.asInterface(localParcel2.readStrongBinder());
          return localIOptionsService;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IOptionsService getOptionsServiceForSubscription(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IOptionsService.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IPresenceService getPresenceService()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IPresenceService localIPresenceService = IPresenceService.Stub.asInterface(localParcel2.readStrongBinder());
          return localIPresenceService;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IPresenceService getPresenceServiceForSubscription(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          localParcel1.writeString(paramString);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IPresenceService.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getServiceStatus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
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
      
      public boolean isServiceStarted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean startService(IUceListener paramIUceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          if (paramIUceListener != null) {
            paramIUceListener = paramIUceListener.asBinder();
          } else {
            paramIUceListener = null;
          }
          localParcel1.writeStrongBinder(paramIUceListener);
          paramIUceListener = mRemote;
          boolean bool = false;
          paramIUceListener.transact(1, localParcel1, localParcel2, 0);
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
      
      public boolean stopService()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.uceservice.IUceService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
    }
  }
}
