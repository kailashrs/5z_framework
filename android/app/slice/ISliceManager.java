package android.app.slice;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISliceManager
  extends IInterface
{
  public abstract void applyRestore(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract int checkSlicePermission(Uri paramUri, String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract byte[] getBackupPayload(int paramInt)
    throws RemoteException;
  
  public abstract Uri[] getPinnedSlices(String paramString)
    throws RemoteException;
  
  public abstract SliceSpec[] getPinnedSpecs(Uri paramUri, String paramString)
    throws RemoteException;
  
  public abstract void grantPermissionFromUser(Uri paramUri, String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void grantSlicePermission(String paramString1, String paramString2, Uri paramUri)
    throws RemoteException;
  
  public abstract boolean hasSliceAccess(String paramString)
    throws RemoteException;
  
  public abstract void pinSlice(String paramString, Uri paramUri, SliceSpec[] paramArrayOfSliceSpec, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void revokeSlicePermission(String paramString1, String paramString2, Uri paramUri)
    throws RemoteException;
  
  public abstract void unpinSlice(String paramString, Uri paramUri, IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISliceManager
  {
    private static final String DESCRIPTOR = "android.app.slice.ISliceManager";
    static final int TRANSACTION_applyRestore = 7;
    static final int TRANSACTION_checkSlicePermission = 10;
    static final int TRANSACTION_getBackupPayload = 6;
    static final int TRANSACTION_getPinnedSlices = 5;
    static final int TRANSACTION_getPinnedSpecs = 4;
    static final int TRANSACTION_grantPermissionFromUser = 11;
    static final int TRANSACTION_grantSlicePermission = 8;
    static final int TRANSACTION_hasSliceAccess = 3;
    static final int TRANSACTION_pinSlice = 1;
    static final int TRANSACTION_revokeSlicePermission = 9;
    static final int TRANSACTION_unpinSlice = 2;
    
    public Stub()
    {
      attachInterface(this, "android.app.slice.ISliceManager");
    }
    
    public static ISliceManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.slice.ISliceManager");
      if ((localIInterface != null) && ((localIInterface instanceof ISliceManager))) {
        return (ISliceManager)localIInterface;
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
        String str1 = null;
        Object localObject3 = null;
        String str2 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject5;
          }
          str2 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          grantPermissionFromUser((Uri)localObject2, str2, str1, bool);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject1) {
            break;
          }
          paramInt1 = checkSlicePermission((Uri)localObject2, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          str2 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          revokeSlicePermission(str2, str1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          str2 = paramParcel1.readString();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          grantSlicePermission(str2, (String)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          applyRestore(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          paramParcel1 = getBackupPayload(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          paramParcel1 = getPinnedSlices(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject3;
          }
          paramParcel1 = getPinnedSpecs((Uri)localObject2, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          paramInt1 = hasSliceAccess(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.slice.ISliceManager");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = str2;
          }
          unpinSlice(str1, (Uri)localObject2, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.slice.ISliceManager");
        str1 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject2 = localObject4;
        }
        pinSlice(str1, (Uri)localObject2, (SliceSpec[])paramParcel1.createTypedArray(SliceSpec.CREATOR), paramParcel1.readStrongBinder());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.slice.ISliceManager");
      return true;
    }
    
    private static class Proxy
      implements ISliceManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void applyRestore(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public int checkSlicePermission(Uri paramUri, String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
      
      public byte[] getBackupPayload(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.slice.ISliceManager";
      }
      
      public Uri[] getPinnedSlices(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = (Uri[])localParcel2.createTypedArray(Uri.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SliceSpec[] getPinnedSpecs(Uri paramUri, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramUri = (SliceSpec[])localParcel2.createTypedArray(SliceSpec.CREATOR);
          return paramUri;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantPermissionFromUser(Uri paramUri, String paramString1, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean);
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
      
      public void grantSlicePermission(String paramString1, String paramString2, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public boolean hasSliceAccess(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(3, localParcel1, localParcel2, 0);
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
      
      public void pinSlice(String paramString, Uri paramUri, SliceSpec[] paramArrayOfSliceSpec, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeTypedArray(paramArrayOfSliceSpec, 0);
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void revokeSlicePermission(String paramString1, String paramString2, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void unpinSlice(String paramString, Uri paramUri, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.slice.ISliceManager");
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
    }
  }
}
