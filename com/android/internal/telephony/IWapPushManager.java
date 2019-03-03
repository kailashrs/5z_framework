package com.android.internal.telephony;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWapPushManager
  extends IInterface
{
  public abstract boolean addPackage(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract boolean deletePackage(String paramString1, String paramString2, String paramString3, String paramString4)
    throws RemoteException;
  
  public abstract int processMessage(String paramString1, String paramString2, Intent paramIntent)
    throws RemoteException;
  
  public abstract boolean updatePackage(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWapPushManager
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.IWapPushManager";
    static final int TRANSACTION_addPackage = 2;
    static final int TRANSACTION_deletePackage = 4;
    static final int TRANSACTION_processMessage = 1;
    static final int TRANSACTION_updatePackage = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.IWapPushManager");
    }
    
    public static IWapPushManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.IWapPushManager");
      if ((localIInterface != null) && ((localIInterface instanceof IWapPushManager))) {
        return (IWapPushManager)localIInterface;
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
        String str3;
        String str4;
        boolean bool1;
        boolean bool2;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IWapPushManager");
          paramInt1 = deletePackage(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IWapPushManager");
          str1 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          str3 = paramParcel1.readString();
          str4 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramInt1 = updatePackage(str1, str2, str3, str4, paramInt1, bool1, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IWapPushManager");
          str4 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          str3 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramInt1 = addPackage(str4, str2, str3, str1, paramInt1, bool1, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.IWapPushManager");
        String str1 = paramParcel1.readString();
        String str2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        paramInt1 = processMessage(str1, str2, paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.IWapPushManager");
      return true;
    }
    
    private static class Proxy
      implements IWapPushManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean addPackage(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IWapPushManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(2, localParcel1, localParcel2, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean deletePackage(String paramString1, String paramString2, String paramString3, String paramString4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IWapPushManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(4, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.IWapPushManager";
      }
      
      public int processMessage(String paramString1, String paramString2, Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IWapPushManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public boolean updatePackage(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IWapPushManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(3, localParcel1, localParcel2, 0);
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
    }
  }
}
