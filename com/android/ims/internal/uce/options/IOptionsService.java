package com.android.ims.internal.uce.options;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.CapInfo;
import com.android.ims.internal.uce.common.StatusCode;
import com.android.ims.internal.uce.common.UceLong;

public abstract interface IOptionsService
  extends IInterface
{
  public abstract StatusCode addListener(int paramInt, IOptionsListener paramIOptionsListener, UceLong paramUceLong)
    throws RemoteException;
  
  public abstract StatusCode getContactCap(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract StatusCode getContactListCap(int paramInt1, String[] paramArrayOfString, int paramInt2)
    throws RemoteException;
  
  public abstract StatusCode getMyInfo(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract StatusCode getVersion(int paramInt)
    throws RemoteException;
  
  public abstract StatusCode removeListener(int paramInt, UceLong paramUceLong)
    throws RemoteException;
  
  public abstract StatusCode responseIncomingOptions(int paramInt1, int paramInt2, int paramInt3, String paramString, OptionsCapInfo paramOptionsCapInfo, boolean paramBoolean)
    throws RemoteException;
  
  public abstract StatusCode setMyInfo(int paramInt1, CapInfo paramCapInfo, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOptionsService
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.uce.options.IOptionsService";
    static final int TRANSACTION_addListener = 2;
    static final int TRANSACTION_getContactCap = 6;
    static final int TRANSACTION_getContactListCap = 7;
    static final int TRANSACTION_getMyInfo = 5;
    static final int TRANSACTION_getVersion = 1;
    static final int TRANSACTION_removeListener = 3;
    static final int TRANSACTION_responseIncomingOptions = 8;
    static final int TRANSACTION_setMyInfo = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.uce.options.IOptionsService");
    }
    
    public static IOptionsService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.uce.options.IOptionsService");
      if ((localIInterface != null) && ((localIInterface instanceof IOptionsService))) {
        return (IOptionsService)localIInterface;
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          int i = paramParcel1.readInt();
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (OptionsCapInfo)OptionsCapInfo.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramParcel1 = responseIncomingOptions(paramInt2, paramInt1, i, str, (OptionsCapInfo)localObject3, bool);
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
          paramParcel1 = getMyInfo(paramParcel1.readInt(), paramParcel1.readInt());
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (CapInfo)CapInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject1;
          }
          paramParcel1 = setMyInfo(paramInt1, (CapInfo)localObject3, paramParcel1.readInt());
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
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
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
          paramInt1 = paramParcel1.readInt();
          localObject3 = IOptionsListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UceLong)UceLong.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          localObject3 = addListener(paramInt1, (IOptionsListener)localObject3, paramParcel1);
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
        paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsService");
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
      paramParcel2.writeString("com.android.ims.internal.uce.options.IOptionsService");
      return true;
    }
    
    private static class Proxy
      implements IOptionsService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public StatusCode addListener(int paramInt, IOptionsListener paramIOptionsListener, UceLong paramUceLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
          localParcel1.writeInt(paramInt);
          Object localObject = null;
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
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIOptionsListener = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIOptionsListener = localObject;
          }
          if (localParcel2.readInt() != 0) {
            paramUceLong.readFromParcel(localParcel2);
          }
          return paramIOptionsListener;
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
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
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
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
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
        return "com.android.ims.internal.uce.options.IOptionsService";
      }
      
      public StatusCode getMyInfo(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public StatusCode getVersion(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
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
      
      public StatusCode removeListener(int paramInt, UceLong paramUceLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
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
      
      public StatusCode responseIncomingOptions(int paramInt1, int paramInt2, int paramInt3, String paramString, OptionsCapInfo paramOptionsCapInfo, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString);
          if (paramOptionsCapInfo != null)
          {
            localParcel1.writeInt(1);
            paramOptionsCapInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
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
      
      public StatusCode setMyInfo(int paramInt1, CapInfo paramCapInfo, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsService");
          localParcel1.writeInt(paramInt1);
          if (paramCapInfo != null)
          {
            localParcel1.writeInt(1);
            paramCapInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramCapInfo = (StatusCode)StatusCode.CREATOR.createFromParcel(localParcel2);
          } else {
            paramCapInfo = null;
          }
          return paramCapInfo;
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
