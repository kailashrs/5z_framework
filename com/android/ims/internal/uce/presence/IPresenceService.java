package com.android.ims.internal.uce.presence;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.StatusCode;
import com.android.ims.internal.uce.common.UceLong;

public abstract interface IPresenceService
  extends IInterface
{
  public abstract StatusCode addListener(int paramInt, IPresenceListener paramIPresenceListener, UceLong paramUceLong)
    throws RemoteException;
  
  public abstract StatusCode getContactCap(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract StatusCode getContactListCap(int paramInt1, String[] paramArrayOfString, int paramInt2)
    throws RemoteException;
  
  public abstract StatusCode getVersion(int paramInt)
    throws RemoteException;
  
  public abstract StatusCode publishMyCap(int paramInt1, PresCapInfo paramPresCapInfo, int paramInt2)
    throws RemoteException;
  
  public abstract StatusCode reenableService(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract StatusCode removeListener(int paramInt, UceLong paramUceLong)
    throws RemoteException;
  
  public abstract StatusCode setNewFeatureTag(int paramInt1, String paramString, PresServiceInfo paramPresServiceInfo, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPresenceService
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.uce.presence.IPresenceService";
    static final int TRANSACTION_addListener = 2;
    static final int TRANSACTION_getContactCap = 6;
    static final int TRANSACTION_getContactListCap = 7;
    static final int TRANSACTION_getVersion = 1;
    static final int TRANSACTION_publishMyCap = 5;
    static final int TRANSACTION_reenableService = 4;
    static final int TRANSACTION_removeListener = 3;
    static final int TRANSACTION_setNewFeatureTag = 8;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.uce.presence.IPresenceService");
    }
    
    public static IPresenceService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.uce.presence.IPresenceService");
      if ((localIInterface != null) && ((localIInterface instanceof IPresenceService))) {
        return (IPresenceService)localIInterface;
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
        String str = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
          paramInt1 = paramParcel1.readInt();
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (PresServiceInfo)PresServiceInfo.CREATOR.createFromParcel(paramParcel1);
          }
          paramParcel1 = setNewFeatureTag(paramInt1, str, (PresServiceInfo)localObject3, paramParcel1.readInt());
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
          paramParcel1 = getContactListCap(paramParcel1.readInt(), paramParcel1.createStringArray(), paramParcel1.readInt());
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
          paramParcel1 = getContactCap(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
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
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (PresCapInfo)PresCapInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject1;
          }
          paramParcel1 = publishMyCap(paramInt1, (PresCapInfo)localObject3, paramParcel1.readInt());
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
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
          paramParcel1 = reenableService(paramParcel1.readInt(), paramParcel1.readInt());
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
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UceLong)UceLong.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramParcel1 = removeListener(paramInt1, paramParcel1);
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
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
          paramInt1 = paramParcel1.readInt();
          localObject3 = IPresenceListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UceLong)UceLong.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          localObject3 = addListener(paramInt1, (IPresenceListener)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          if (localObject3 != null)
          {
            paramParcel2.writeInt(1);
            ((StatusCode)localObject3).writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
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
        }
        paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceService");
        paramParcel1 = getVersion(paramParcel1.readInt());
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
      }
      paramParcel2.writeString("com.android.ims.internal.uce.presence.IPresenceService");
      return true;
    }
    
    private static class Proxy
      implements IPresenceService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public StatusCode addListener(int paramInt, IPresenceListener paramIPresenceListener, UceLong paramUceLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt);
          Object localObject = null;
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
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIPresenceListener = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIPresenceListener = localObject;
          }
          if (localParcel2.readInt() != 0) {
            paramUceLong.readFromParcel(localParcel2);
          }
          return paramIPresenceListener;
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
      
      public StatusCode getContactCap(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public StatusCode getContactListCap(int paramInt1, String[] paramArrayOfString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfString = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfString = null;
          }
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.uce.presence.IPresenceService";
      }
      
      public StatusCode getVersion(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          StatusCode localStatusCode;
          if (localParcel2.readInt() != 0) {
            localStatusCode = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            localStatusCode = null;
          }
          return localStatusCode;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public StatusCode publishMyCap(int paramInt1, PresCapInfo paramPresCapInfo, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt1);
          if (paramPresCapInfo != null)
          {
            localParcel1.writeInt(1);
            paramPresCapInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPresCapInfo = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramPresCapInfo = null;
          }
          return paramPresCapInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public StatusCode reenableService(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          StatusCode localStatusCode;
          if (localParcel2.readInt() != 0) {
            localStatusCode = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            localStatusCode = null;
          }
          return localStatusCode;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public StatusCode removeListener(int paramInt, UceLong paramUceLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt);
          if (paramUceLong != null)
          {
            localParcel1.writeInt(1);
            paramUceLong.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramUceLong = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramUceLong = null;
          }
          return paramUceLong;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public StatusCode setNewFeatureTag(int paramInt1, String paramString, PresServiceInfo paramPresServiceInfo, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          if (paramPresServiceInfo != null)
          {
            localParcel1.writeInt(1);
            paramPresServiceInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
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
