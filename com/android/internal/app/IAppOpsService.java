package com.android.internal.app;

import android.app.AppOpsManager.PackageOps;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IAppOpsService
  extends IInterface
{
  public abstract int checkAudioOperation(int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws RemoteException;
  
  public abstract int checkOperation(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract int checkPackage(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void finishOperation(IBinder paramIBinder, int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract List<AppOpsManager.PackageOps> getOpsForPackage(int paramInt, String paramString, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract List<AppOpsManager.PackageOps> getPackagesForOps(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract IBinder getToken(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract List<AppOpsManager.PackageOps> getUidOps(int paramInt, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract boolean isOperationActive(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract int noteOperation(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract int noteProxyOperation(int paramInt1, String paramString1, int paramInt2, String paramString2)
    throws RemoteException;
  
  public abstract int permissionToOpCode(String paramString)
    throws RemoteException;
  
  public abstract void removeUser(int paramInt)
    throws RemoteException;
  
  public abstract void resetAllModes(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setAudioRestriction(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void setMode(int paramInt1, int paramInt2, String paramString, int paramInt3)
    throws RemoteException;
  
  public abstract void setUidMode(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setUserRestriction(int paramInt1, boolean paramBoolean, IBinder paramIBinder, int paramInt2, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void setUserRestrictions(Bundle paramBundle, IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract int startOperation(IBinder paramIBinder, int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void startWatchingActive(int[] paramArrayOfInt, IAppOpsActiveCallback paramIAppOpsActiveCallback)
    throws RemoteException;
  
  public abstract void startWatchingMode(int paramInt, String paramString, IAppOpsCallback paramIAppOpsCallback)
    throws RemoteException;
  
  public abstract void startWatchingModeWithFlags(int paramInt1, String paramString, int paramInt2, IAppOpsCallback paramIAppOpsCallback)
    throws RemoteException;
  
  public abstract void stopWatchingActive(IAppOpsActiveCallback paramIAppOpsActiveCallback)
    throws RemoteException;
  
  public abstract void stopWatchingMode(IAppOpsCallback paramIAppOpsCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAppOpsService
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IAppOpsService";
    static final int TRANSACTION_checkAudioOperation = 17;
    static final int TRANSACTION_checkOperation = 1;
    static final int TRANSACTION_checkPackage = 10;
    static final int TRANSACTION_finishOperation = 4;
    static final int TRANSACTION_getOpsForPackage = 12;
    static final int TRANSACTION_getPackagesForOps = 11;
    static final int TRANSACTION_getToken = 7;
    static final int TRANSACTION_getUidOps = 13;
    static final int TRANSACTION_isOperationActive = 24;
    static final int TRANSACTION_noteOperation = 2;
    static final int TRANSACTION_noteProxyOperation = 9;
    static final int TRANSACTION_permissionToOpCode = 8;
    static final int TRANSACTION_removeUser = 21;
    static final int TRANSACTION_resetAllModes = 16;
    static final int TRANSACTION_setAudioRestriction = 18;
    static final int TRANSACTION_setMode = 15;
    static final int TRANSACTION_setUidMode = 14;
    static final int TRANSACTION_setUserRestriction = 20;
    static final int TRANSACTION_setUserRestrictions = 19;
    static final int TRANSACTION_startOperation = 3;
    static final int TRANSACTION_startWatchingActive = 22;
    static final int TRANSACTION_startWatchingMode = 5;
    static final int TRANSACTION_startWatchingModeWithFlags = 25;
    static final int TRANSACTION_stopWatchingActive = 23;
    static final int TRANSACTION_stopWatchingMode = 6;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IAppOpsService");
    }
    
    public static IAppOpsService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IAppOpsService");
      if ((localIInterface != null) && ((localIInterface instanceof IAppOpsService))) {
        return (IAppOpsService)localIInterface;
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
        boolean bool;
        Object localObject;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          startWatchingModeWithFlags(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), IAppOpsCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramInt1 = isOperationActive(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          stopWatchingActive(IAppOpsActiveCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          startWatchingActive(paramParcel1.createIntArray(), IAppOpsActiveCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          removeUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          setUserRestriction(paramInt1, bool, paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          if (paramParcel1.readInt() != 0) {
            localObject = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = null;
          }
          setUserRestrictions((Bundle)localObject, paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          setAudioRestriction(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramInt1 = checkAudioOperation(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          resetAllModes(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          setMode(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          setUidMode(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramParcel1 = getUidOps(paramParcel1.readInt(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramParcel1 = getOpsForPackage(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramParcel1 = getPackagesForOps(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramInt1 = checkPackage(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramInt1 = noteProxyOperation(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramInt1 = permissionToOpCode(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramParcel1 = getToken(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          stopWatchingMode(IAppOpsCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          startWatchingMode(paramParcel1.readInt(), paramParcel1.readString(), IAppOpsCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          finishOperation(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          localObject = paramParcel1.readStrongBinder();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          String str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramInt1 = startOperation((IBinder)localObject, paramInt1, paramInt2, str, bool);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
          paramInt1 = noteOperation(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.IAppOpsService");
        paramInt1 = checkOperation(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IAppOpsService");
      return true;
    }
    
    private static class Proxy
      implements IAppOpsService
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
      
      public int checkAudioOperation(int paramInt1, int paramInt2, int paramInt3, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
      
      public int checkOperation(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
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
      
      public int checkPackage(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finishOperation(IBinder paramIBinder, int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.IAppOpsService";
      }
      
      public List<AppOpsManager.PackageOps> getOpsForPackage(int paramInt, String paramString, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(AppOpsManager.PackageOps.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<AppOpsManager.PackageOps> getPackagesForOps(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createTypedArrayList(AppOpsManager.PackageOps.CREATOR);
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder getToken(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = localParcel2.readStrongBinder();
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<AppOpsManager.PackageOps> getUidOps(int paramInt, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createTypedArrayList(AppOpsManager.PackageOps.CREATOR);
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isOperationActive(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(24, localParcel1, localParcel2, 0);
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
      
      public int noteOperation(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public int noteProxyOperation(int paramInt1, String paramString1, int paramInt2, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString2);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public int permissionToOpCode(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeString(paramString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public void removeUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resetAllModes(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void setAudioRestriction(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMode(int paramInt1, int paramInt2, String paramString, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt3);
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
      
      public void setUidMode(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void setUserRestriction(int paramInt1, boolean paramBoolean, IBinder paramIBinder, int paramInt2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserRestrictions(Bundle paramBundle, IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startOperation(IBinder paramIBinder, int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public void startWatchingActive(int[] paramArrayOfInt, IAppOpsActiveCallback paramIAppOpsActiveCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeIntArray(paramArrayOfInt);
          if (paramIAppOpsActiveCallback != null) {
            paramArrayOfInt = paramIAppOpsActiveCallback.asBinder();
          } else {
            paramArrayOfInt = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfInt);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startWatchingMode(int paramInt, String paramString, IAppOpsCallback paramIAppOpsCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          if (paramIAppOpsCallback != null) {
            paramString = paramIAppOpsCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void startWatchingModeWithFlags(int paramInt1, String paramString, int paramInt2, IAppOpsCallback paramIAppOpsCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          if (paramIAppOpsCallback != null) {
            paramString = paramIAppOpsCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopWatchingActive(IAppOpsActiveCallback paramIAppOpsActiveCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          if (paramIAppOpsActiveCallback != null) {
            paramIAppOpsActiveCallback = paramIAppOpsActiveCallback.asBinder();
          } else {
            paramIAppOpsActiveCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAppOpsActiveCallback);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopWatchingMode(IAppOpsCallback paramIAppOpsCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IAppOpsService");
          if (paramIAppOpsCallback != null) {
            paramIAppOpsCallback = paramIAppOpsCallback.asBinder();
          } else {
            paramIAppOpsCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAppOpsCallback);
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
    }
  }
}
