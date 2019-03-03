package com.android.internal.app;

import android.content.pm.PackageInfoLite;
import android.content.res.ObbInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.os.IParcelFileDescriptorFactory;
import com.android.internal.os.IParcelFileDescriptorFactory.Stub;

public abstract interface IMediaContainerService
  extends IInterface
{
  public abstract long calculateInstalledSize(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void clearDirectory(String paramString)
    throws RemoteException;
  
  public abstract int copyPackage(String paramString, IParcelFileDescriptorFactory paramIParcelFileDescriptorFactory)
    throws RemoteException;
  
  public abstract PackageInfoLite getMinimalPackageInfo(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract ObbInfo getObbInfo(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaContainerService
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IMediaContainerService";
    static final int TRANSACTION_calculateInstalledSize = 5;
    static final int TRANSACTION_clearDirectory = 4;
    static final int TRANSACTION_copyPackage = 1;
    static final int TRANSACTION_getMinimalPackageInfo = 2;
    static final int TRANSACTION_getObbInfo = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IMediaContainerService");
    }
    
    public static IMediaContainerService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IMediaContainerService");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaContainerService))) {
        return (IMediaContainerService)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.app.IMediaContainerService");
          long l = calculateInstalledSize(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.app.IMediaContainerService");
          clearDirectory(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.app.IMediaContainerService");
          paramParcel1 = getObbInfo(paramParcel1.readString());
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
          paramParcel1.enforceInterface("com.android.internal.app.IMediaContainerService");
          paramParcel1 = getMinimalPackageInfo(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
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
        paramParcel1.enforceInterface("com.android.internal.app.IMediaContainerService");
        paramInt1 = copyPackage(paramParcel1.readString(), IParcelFileDescriptorFactory.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IMediaContainerService");
      return true;
    }
    
    private static class Proxy
      implements IMediaContainerService
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
      
      public long calculateInstalledSize(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IMediaContainerService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearDirectory(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IMediaContainerService");
          localParcel1.writeString(paramString);
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
      
      public int copyPackage(String paramString, IParcelFileDescriptorFactory paramIParcelFileDescriptorFactory)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IMediaContainerService");
          localParcel1.writeString(paramString);
          if (paramIParcelFileDescriptorFactory != null) {
            paramString = paramIParcelFileDescriptorFactory.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.IMediaContainerService";
      }
      
      public PackageInfoLite getMinimalPackageInfo(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IMediaContainerService");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (PackageInfoLite)PackageInfoLite.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ObbInfo getObbInfo(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IMediaContainerService");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ObbInfo)ObbInfo.CREATOR.createFromParcel(localParcel2);
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
