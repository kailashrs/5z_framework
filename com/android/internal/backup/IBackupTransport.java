package com.android.internal.backup;

import android.app.backup.RestoreDescription;
import android.app.backup.RestoreSet;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBackupTransport
  extends IInterface
{
  public abstract int abortFullRestore()
    throws RemoteException;
  
  public abstract void cancelFullBackup()
    throws RemoteException;
  
  public abstract int checkFullBackupSize(long paramLong)
    throws RemoteException;
  
  public abstract int clearBackupData(PackageInfo paramPackageInfo)
    throws RemoteException;
  
  public abstract Intent configurationIntent()
    throws RemoteException;
  
  public abstract String currentDestinationString()
    throws RemoteException;
  
  public abstract Intent dataManagementIntent()
    throws RemoteException;
  
  public abstract String dataManagementLabel()
    throws RemoteException;
  
  public abstract int finishBackup()
    throws RemoteException;
  
  public abstract void finishRestore()
    throws RemoteException;
  
  public abstract RestoreSet[] getAvailableRestoreSets()
    throws RemoteException;
  
  public abstract long getBackupQuota(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract long getCurrentRestoreSet()
    throws RemoteException;
  
  public abstract int getNextFullRestoreDataChunk(ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract int getRestoreData(ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract int getTransportFlags()
    throws RemoteException;
  
  public abstract int initializeDevice()
    throws RemoteException;
  
  public abstract boolean isAppEligibleForBackup(PackageInfo paramPackageInfo, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String name()
    throws RemoteException;
  
  public abstract RestoreDescription nextRestorePackage()
    throws RemoteException;
  
  public abstract int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
    throws RemoteException;
  
  public abstract int performFullBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
    throws RemoteException;
  
  public abstract long requestBackupTime()
    throws RemoteException;
  
  public abstract long requestFullBackupTime()
    throws RemoteException;
  
  public abstract int sendBackupData(int paramInt)
    throws RemoteException;
  
  public abstract int setAsusFileDestination(String paramString)
    throws RemoteException;
  
  public abstract int startRestore(long paramLong, PackageInfo[] paramArrayOfPackageInfo)
    throws RemoteException;
  
  public abstract String transportDirName()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBackupTransport
  {
    private static final String DESCRIPTOR = "com.android.internal.backup.IBackupTransport";
    static final int TRANSACTION_abortFullRestore = 26;
    static final int TRANSACTION_cancelFullBackup = 22;
    static final int TRANSACTION_checkFullBackupSize = 20;
    static final int TRANSACTION_clearBackupData = 10;
    static final int TRANSACTION_configurationIntent = 2;
    static final int TRANSACTION_currentDestinationString = 3;
    static final int TRANSACTION_dataManagementIntent = 4;
    static final int TRANSACTION_dataManagementLabel = 5;
    static final int TRANSACTION_finishBackup = 11;
    static final int TRANSACTION_finishRestore = 17;
    static final int TRANSACTION_getAvailableRestoreSets = 12;
    static final int TRANSACTION_getBackupQuota = 24;
    static final int TRANSACTION_getCurrentRestoreSet = 13;
    static final int TRANSACTION_getNextFullRestoreDataChunk = 25;
    static final int TRANSACTION_getRestoreData = 16;
    static final int TRANSACTION_getTransportFlags = 27;
    static final int TRANSACTION_initializeDevice = 8;
    static final int TRANSACTION_isAppEligibleForBackup = 23;
    static final int TRANSACTION_name = 1;
    static final int TRANSACTION_nextRestorePackage = 15;
    static final int TRANSACTION_performBackup = 9;
    static final int TRANSACTION_performFullBackup = 19;
    static final int TRANSACTION_requestBackupTime = 7;
    static final int TRANSACTION_requestFullBackupTime = 18;
    static final int TRANSACTION_sendBackupData = 21;
    static final int TRANSACTION_setAsusFileDestination = 28;
    static final int TRANSACTION_startRestore = 14;
    static final int TRANSACTION_transportDirName = 6;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.backup.IBackupTransport");
    }
    
    public static IBackupTransport asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.backup.IBackupTransport");
      if ((localIInterface != null) && ((localIInterface instanceof IBackupTransport))) {
        return (IBackupTransport)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = setAsusFileDestination(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = getTransportFlags();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = abortFullRestore();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject6;
          }
          paramInt1 = getNextFullRestoreDataChunk(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          l = getBackupQuota((String)localObject6, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PackageInfo)PackageInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject1;
          }
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramInt1 = isAppEligibleForBackup((PackageInfo)localObject6, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          cancelFullBackup();
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = sendBackupData(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = checkFullBackupSize(paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PackageInfo)PackageInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          }
          paramInt1 = performFullBackup((PackageInfo)localObject6, (ParcelFileDescriptor)localObject2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          l = requestFullBackupTime();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          finishRestore();
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = getRestoreData(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramParcel1 = nextRestorePackage();
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
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = startRestore(paramParcel1.readLong(), (PackageInfo[])paramParcel1.createTypedArray(PackageInfo.CREATOR));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          l = getCurrentRestoreSet();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramParcel1 = getAvailableRestoreSets();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = finishBackup();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PackageInfo)PackageInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = clearBackupData(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PackageInfo)PackageInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject5;
          }
          paramInt1 = performBackup((PackageInfo)localObject6, (ParcelFileDescriptor)localObject2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramInt1 = initializeDevice();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          l = requestBackupTime();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramParcel1 = transportDirName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramParcel1 = dataManagementLabel();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramParcel1 = dataManagementIntent();
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
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramParcel1 = currentDestinationString();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
          paramParcel1 = configurationIntent();
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
        paramParcel1.enforceInterface("com.android.internal.backup.IBackupTransport");
        paramParcel1 = name();
        paramParcel2.writeNoException();
        paramParcel2.writeString(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.backup.IBackupTransport");
      return true;
    }
    
    private static class Proxy
      implements IBackupTransport
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int abortFullRestore()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(26, localParcel1, localParcel2, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void cancelFullBackup()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
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
      
      public int checkFullBackupSize(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          localParcel1.writeLong(paramLong);
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
      
      public int clearBackupData(PackageInfo paramPackageInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          if (paramPackageInfo != null)
          {
            localParcel1.writeInt(1);
            paramPackageInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
      
      public Intent configurationIntent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Intent localIntent;
          if (localParcel2.readInt() != 0) {
            localIntent = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            localIntent = null;
          }
          return localIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String currentDestinationString()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Intent dataManagementIntent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Intent localIntent;
          if (localParcel2.readInt() != 0) {
            localIntent = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            localIntent = null;
          }
          return localIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String dataManagementLabel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int finishBackup()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(11, localParcel1, localParcel2, 0);
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
      
      public void finishRestore()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public RestoreSet[] getAvailableRestoreSets()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          RestoreSet[] arrayOfRestoreSet = (RestoreSet[])localParcel2.createTypedArray(RestoreSet.CREATOR);
          return arrayOfRestoreSet;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getBackupQuota(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(24, localParcel1, localParcel2, 0);
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
      
      public long getCurrentRestoreSet()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.backup.IBackupTransport";
      }
      
      public int getNextFullRestoreDataChunk(ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(25, localParcel1, localParcel2, 0);
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
      
      public int getRestoreData(ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(16, localParcel1, localParcel2, 0);
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
      
      public int getTransportFlags()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(27, localParcel1, localParcel2, 0);
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
      
      public int initializeDevice()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
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
      
      public boolean isAppEligibleForBackup(PackageInfo paramPackageInfo, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          boolean bool = true;
          if (paramPackageInfo != null)
          {
            localParcel1.writeInt(1);
            paramPackageInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String name()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public RestoreDescription nextRestorePackage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          RestoreDescription localRestoreDescription;
          if (localParcel2.readInt() != 0) {
            localRestoreDescription = (RestoreDescription)RestoreDescription.CREATOR.createFromParcel(localParcel2);
          } else {
            localRestoreDescription = null;
          }
          return localRestoreDescription;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          if (paramPackageInfo != null)
          {
            localParcel1.writeInt(1);
            paramPackageInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public int performFullBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          if (paramPackageInfo != null)
          {
            localParcel1.writeInt(1);
            paramPackageInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
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
      
      public long requestBackupTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public long requestFullBackupTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(18, localParcel1, localParcel2, 0);
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
      
      public int sendBackupData(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
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
      
      public int setAsusFileDestination(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          localParcel1.writeString(paramString);
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public int startRestore(long paramLong, PackageInfo[] paramArrayOfPackageInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          localParcel1.writeLong(paramLong);
          localParcel1.writeTypedArray(paramArrayOfPackageInfo, 0);
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public String transportDirName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.backup.IBackupTransport");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
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
