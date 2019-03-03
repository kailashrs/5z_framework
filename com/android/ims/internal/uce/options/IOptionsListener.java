package com.android.ims.internal.uce.options;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.StatusCode;

public abstract interface IOptionsListener
  extends IInterface
{
  public abstract void cmdStatus(OptionsCmdStatus paramOptionsCmdStatus)
    throws RemoteException;
  
  public abstract void getVersionCb(String paramString)
    throws RemoteException;
  
  public abstract void incomingOptions(String paramString, OptionsCapInfo paramOptionsCapInfo, int paramInt)
    throws RemoteException;
  
  public abstract void serviceAvailable(StatusCode paramStatusCode)
    throws RemoteException;
  
  public abstract void serviceUnavailable(StatusCode paramStatusCode)
    throws RemoteException;
  
  public abstract void sipResponseReceived(String paramString, OptionsSipResponse paramOptionsSipResponse, OptionsCapInfo paramOptionsCapInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOptionsListener
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.uce.options.IOptionsListener";
    static final int TRANSACTION_cmdStatus = 5;
    static final int TRANSACTION_getVersionCb = 1;
    static final int TRANSACTION_incomingOptions = 6;
    static final int TRANSACTION_serviceAvailable = 2;
    static final int TRANSACTION_serviceUnavailable = 3;
    static final int TRANSACTION_sipResponseReceived = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.uce.options.IOptionsListener");
    }
    
    public static IOptionsListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.uce.options.IOptionsListener");
      if ((localIInterface != null) && ((localIInterface instanceof IOptionsListener))) {
        return (IOptionsListener)localIInterface;
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
        String str1 = null;
        String str2 = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsListener");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (OptionsCapInfo)OptionsCapInfo.CREATOR.createFromParcel(paramParcel1);
          }
          incomingOptions(str2, (OptionsCapInfo)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (OptionsCmdStatus)OptionsCmdStatus.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          cmdStatus(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsListener");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (OptionsSipResponse)OptionsSipResponse.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (OptionsCapInfo)OptionsCapInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          sipResponseReceived(str1, (OptionsSipResponse)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (StatusCode)StatusCode.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          serviceUnavailable(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (StatusCode)StatusCode.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          serviceAvailable(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.uce.options.IOptionsListener");
        getVersionCb(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.uce.options.IOptionsListener");
      return true;
    }
    
    private static class Proxy
      implements IOptionsListener
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
      
      public void cmdStatus(OptionsCmdStatus paramOptionsCmdStatus)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsListener");
          if (paramOptionsCmdStatus != null)
          {
            localParcel1.writeInt(1);
            paramOptionsCmdStatus.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.uce.options.IOptionsListener";
      }
      
      public void getVersionCb(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsListener");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void incomingOptions(String paramString, OptionsCapInfo paramOptionsCapInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsListener");
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
      
      public void serviceAvailable(StatusCode paramStatusCode)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsListener");
          if (paramStatusCode != null)
          {
            localParcel1.writeInt(1);
            paramStatusCode.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void serviceUnavailable(StatusCode paramStatusCode)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsListener");
          if (paramStatusCode != null)
          {
            localParcel1.writeInt(1);
            paramStatusCode.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void sipResponseReceived(String paramString, OptionsSipResponse paramOptionsSipResponse, OptionsCapInfo paramOptionsCapInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.options.IOptionsListener");
          localParcel1.writeString(paramString);
          if (paramOptionsSipResponse != null)
          {
            localParcel1.writeInt(1);
            paramOptionsSipResponse.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramOptionsCapInfo != null)
          {
            localParcel1.writeInt(1);
            paramOptionsCapInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
