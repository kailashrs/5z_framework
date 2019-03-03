package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IClipboard
  extends IInterface
{
  public abstract void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener paramIOnPrimaryClipChangedListener, String paramString)
    throws RemoteException;
  
  public abstract void clearPrimaryClip(String paramString)
    throws RemoteException;
  
  public abstract ClipData getPrimaryClip(String paramString)
    throws RemoteException;
  
  public abstract ClipDescription getPrimaryClipDescription(String paramString)
    throws RemoteException;
  
  public abstract boolean hasClipboardText(String paramString)
    throws RemoteException;
  
  public abstract boolean hasPrimaryClip(String paramString)
    throws RemoteException;
  
  public abstract void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener paramIOnPrimaryClipChangedListener)
    throws RemoteException;
  
  public abstract void setPrimaryClip(ClipData paramClipData, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IClipboard
  {
    private static final String DESCRIPTOR = "android.content.IClipboard";
    static final int TRANSACTION_addPrimaryClipChangedListener = 6;
    static final int TRANSACTION_clearPrimaryClip = 2;
    static final int TRANSACTION_getPrimaryClip = 3;
    static final int TRANSACTION_getPrimaryClipDescription = 4;
    static final int TRANSACTION_hasClipboardText = 8;
    static final int TRANSACTION_hasPrimaryClip = 5;
    static final int TRANSACTION_removePrimaryClipChangedListener = 7;
    static final int TRANSACTION_setPrimaryClip = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.IClipboard");
    }
    
    public static IClipboard asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.IClipboard");
      if ((localIInterface != null) && ((localIInterface instanceof IClipboard))) {
        return (IClipboard)localIInterface;
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
        case 8: 
          paramParcel1.enforceInterface("android.content.IClipboard");
          paramInt1 = hasClipboardText(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.content.IClipboard");
          removePrimaryClipChangedListener(IOnPrimaryClipChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.content.IClipboard");
          addPrimaryClipChangedListener(IOnPrimaryClipChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.IClipboard");
          paramInt1 = hasPrimaryClip(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.IClipboard");
          paramParcel1 = getPrimaryClipDescription(paramParcel1.readString());
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
          paramParcel1.enforceInterface("android.content.IClipboard");
          paramParcel1 = getPrimaryClip(paramParcel1.readString());
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
          paramParcel1.enforceInterface("android.content.IClipboard");
          clearPrimaryClip(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.content.IClipboard");
        ClipData localClipData;
        if (paramParcel1.readInt() != 0) {
          localClipData = (ClipData)ClipData.CREATOR.createFromParcel(paramParcel1);
        } else {
          localClipData = null;
        }
        setPrimaryClip(localClipData, paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.IClipboard");
      return true;
    }
    
    private static class Proxy
      implements IClipboard
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener paramIOnPrimaryClipChangedListener, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          if (paramIOnPrimaryClipChangedListener != null) {
            paramIOnPrimaryClipChangedListener = paramIOnPrimaryClipChangedListener.asBinder();
          } else {
            paramIOnPrimaryClipChangedListener = null;
          }
          localParcel1.writeStrongBinder(paramIOnPrimaryClipChangedListener);
          localParcel1.writeString(paramString);
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
      
      public void clearPrimaryClip(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          localParcel1.writeString(paramString);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.content.IClipboard";
      }
      
      public ClipData getPrimaryClip(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ClipData)ClipData.CREATOR.createFromParcel(localParcel2);
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
      
      public ClipDescription getPrimaryClipDescription(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ClipDescription)ClipDescription.CREATOR.createFromParcel(localParcel2);
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
      
      public boolean hasClipboardText(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(8, localParcel1, localParcel2, 0);
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
      
      public boolean hasPrimaryClip(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(5, localParcel1, localParcel2, 0);
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
      
      public void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener paramIOnPrimaryClipChangedListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          if (paramIOnPrimaryClipChangedListener != null) {
            paramIOnPrimaryClipChangedListener = paramIOnPrimaryClipChangedListener.asBinder();
          } else {
            paramIOnPrimaryClipChangedListener = null;
          }
          localParcel1.writeStrongBinder(paramIOnPrimaryClipChangedListener);
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
      
      public void setPrimaryClip(ClipData paramClipData, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IClipboard");
          if (paramClipData != null)
          {
            localParcel1.writeInt(1);
            paramClipData.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
