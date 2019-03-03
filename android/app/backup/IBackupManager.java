package android.app.backup;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBackupManager
  extends IInterface
{
  public abstract void acknowledgeFullBackupOrRestore(int paramInt, boolean paramBoolean, String paramString1, String paramString2, IFullBackupRestoreObserver paramIFullBackupRestoreObserver)
    throws RemoteException;
  
  public abstract void adbBackup(ParcelFileDescriptor paramParcelFileDescriptor, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void adbRestore(ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public abstract void agentConnected(String paramString, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void agentDisconnected(String paramString)
    throws RemoteException;
  
  public abstract void backupNow()
    throws RemoteException;
  
  public abstract IRestoreSession beginRestoreSession(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void cancelBackups()
    throws RemoteException;
  
  public abstract void clearBackupData(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void dataChanged(String paramString)
    throws RemoteException;
  
  public abstract String[] filterAppsEligibleForBackup(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract int fullBackupForAsusTransfer(String paramString, IBackupObserver paramIBackupObserver)
    throws RemoteException;
  
  public abstract int fullRestoreForAsusTransfer(String paramString, IRestoreObserver paramIRestoreObserver)
    throws RemoteException;
  
  public abstract void fullTransportBackup(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract long getAvailableRestoreToken(String paramString)
    throws RemoteException;
  
  public abstract Intent getConfigurationIntent(String paramString)
    throws RemoteException;
  
  public abstract String getCurrentTransport()
    throws RemoteException;
  
  public abstract Intent getDataManagementIntent(String paramString)
    throws RemoteException;
  
  public abstract String getDataManagementLabel(String paramString)
    throws RemoteException;
  
  public abstract String getDestinationString(String paramString)
    throws RemoteException;
  
  public abstract String[] getTransportWhitelist()
    throws RemoteException;
  
  public abstract boolean hasBackupPassword()
    throws RemoteException;
  
  public abstract void initializeTransports(String[] paramArrayOfString, IBackupObserver paramIBackupObserver)
    throws RemoteException;
  
  public abstract boolean isAppEligibleForBackup(String paramString)
    throws RemoteException;
  
  public abstract boolean isBackupEnabled()
    throws RemoteException;
  
  public abstract boolean isBackupServiceActive(int paramInt)
    throws RemoteException;
  
  public abstract ComponentName[] listAllTransportComponents()
    throws RemoteException;
  
  public abstract String[] listAllTransports()
    throws RemoteException;
  
  public abstract void opComplete(int paramInt, long paramLong)
    throws RemoteException;
  
  public abstract int requestBackup(String[] paramArrayOfString, IBackupObserver paramIBackupObserver, IBackupManagerMonitor paramIBackupManagerMonitor, int paramInt)
    throws RemoteException;
  
  public abstract void restoreAtInstall(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract String selectBackupTransport(String paramString)
    throws RemoteException;
  
  public abstract void selectBackupTransportAsync(ComponentName paramComponentName, ISelectBackupTransportCallback paramISelectBackupTransportCallback)
    throws RemoteException;
  
  public abstract int setAsusFileDestination(String paramString)
    throws RemoteException;
  
  public abstract void setAutoRestore(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setBackupEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setBackupPassword(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setBackupProvisioned(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setBackupServiceActive(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void updateTransportAttributes(ComponentName paramComponentName, String paramString1, Intent paramIntent1, String paramString2, Intent paramIntent2, String paramString3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBackupManager
  {
    private static final String DESCRIPTOR = "android.app.backup.IBackupManager";
    static final int TRANSACTION_acknowledgeFullBackupOrRestore = 17;
    static final int TRANSACTION_adbBackup = 14;
    static final int TRANSACTION_adbRestore = 16;
    static final int TRANSACTION_agentConnected = 4;
    static final int TRANSACTION_agentDisconnected = 5;
    static final int TRANSACTION_backupNow = 13;
    static final int TRANSACTION_beginRestoreSession = 29;
    static final int TRANSACTION_cancelBackups = 37;
    static final int TRANSACTION_clearBackupData = 2;
    static final int TRANSACTION_dataChanged = 1;
    static final int TRANSACTION_filterAppsEligibleForBackup = 35;
    static final int TRANSACTION_fullBackupForAsusTransfer = 39;
    static final int TRANSACTION_fullRestoreForAsusTransfer = 40;
    static final int TRANSACTION_fullTransportBackup = 15;
    static final int TRANSACTION_getAvailableRestoreToken = 33;
    static final int TRANSACTION_getConfigurationIntent = 25;
    static final int TRANSACTION_getCurrentTransport = 19;
    static final int TRANSACTION_getDataManagementIntent = 27;
    static final int TRANSACTION_getDataManagementLabel = 28;
    static final int TRANSACTION_getDestinationString = 26;
    static final int TRANSACTION_getTransportWhitelist = 22;
    static final int TRANSACTION_hasBackupPassword = 12;
    static final int TRANSACTION_initializeTransports = 3;
    static final int TRANSACTION_isAppEligibleForBackup = 34;
    static final int TRANSACTION_isBackupEnabled = 10;
    static final int TRANSACTION_isBackupServiceActive = 32;
    static final int TRANSACTION_listAllTransportComponents = 21;
    static final int TRANSACTION_listAllTransports = 20;
    static final int TRANSACTION_opComplete = 30;
    static final int TRANSACTION_requestBackup = 36;
    static final int TRANSACTION_restoreAtInstall = 6;
    static final int TRANSACTION_selectBackupTransport = 23;
    static final int TRANSACTION_selectBackupTransportAsync = 24;
    static final int TRANSACTION_setAsusFileDestination = 38;
    static final int TRANSACTION_setAutoRestore = 8;
    static final int TRANSACTION_setBackupEnabled = 7;
    static final int TRANSACTION_setBackupPassword = 11;
    static final int TRANSACTION_setBackupProvisioned = 9;
    static final int TRANSACTION_setBackupServiceActive = 31;
    static final int TRANSACTION_updateTransportAttributes = 18;
    
    public Stub()
    {
      attachInterface(this, "android.app.backup.IBackupManager");
    }
    
    public static IBackupManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.backup.IBackupManager");
      if ((localIInterface != null) && ((localIInterface instanceof IBackupManager))) {
        return (IBackupManager)localIInterface;
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
        Intent localIntent = null;
        String str1 = null;
        String str2 = null;
        Object localObject2 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 40: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = fullRestoreForAsusTransfer(paramParcel1.readString(), IRestoreObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = fullBackupForAsusTransfer(paramParcel1.readString(), IBackupObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = setAsusFileDestination(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          cancelBackups();
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = requestBackup(paramParcel1.createStringArray(), IBackupObserver.Stub.asInterface(paramParcel1.readStrongBinder()), IBackupManagerMonitor.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = filterAppsEligibleForBackup(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = isAppEligibleForBackup(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          long l = getAvailableRestoreToken(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = isBackupServiceActive(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setBackupServiceActive(paramInt1, bool5);
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          opComplete(paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          localObject1 = beginRestoreSession(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject2;
          if (localObject1 != null) {
            paramParcel1 = ((IRestoreSession)localObject1).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = getDataManagementLabel(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = getDataManagementIntent(paramParcel1.readString());
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
        case 26: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = getDestinationString(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = getConfigurationIntent(paramParcel1.readString());
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
        case 24: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject1;
          }
          selectBackupTransportAsync((ComponentName)localObject2, ISelectBackupTransportCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = selectBackupTransport(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = getTransportWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = listAllTransportComponents();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = listAllTransports();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramParcel1 = getCurrentTransport();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          updateTransportAttributes((ComponentName)localObject2, str1, (Intent)localObject1, str2, localIntent, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          acknowledgeFullBackupOrRestore(paramInt1, bool5, paramParcel1.readString(), paramParcel1.readString(), IFullBackupRestoreObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          adbRestore(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          fullTransportBackup(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);; localObject2 = str2) {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          boolean bool6;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          boolean bool7;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          } else {
            bool7 = false;
          }
          boolean bool8;
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          } else {
            bool8 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          adbBackup((ParcelFileDescriptor)localObject2, bool5, bool2, bool3, bool4, bool6, bool7, bool8, bool1, paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          backupNow();
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = hasBackupPassword();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = setBackupPassword(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          paramInt1 = isBackupEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          bool5 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setBackupProvisioned(bool5);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          bool5 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setAutoRestore(bool5);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          bool5 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setBackupEnabled(bool5);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          restoreAtInstall(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          agentDisconnected(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          agentConnected(paramParcel1.readString(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          initializeTransports(paramParcel1.createStringArray(), IBackupObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.backup.IBackupManager");
          clearBackupData(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.backup.IBackupManager");
        dataChanged(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.backup.IBackupManager");
      return true;
    }
    
    private static class Proxy
      implements IBackupManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void acknowledgeFullBackupOrRestore(int paramInt, boolean paramBoolean, String paramString1, String paramString2, IFullBackupRestoreObserver paramIFullBackupRestoreObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramIFullBackupRestoreObserver != null) {
            paramString1 = paramIFullBackupRestoreObserver.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
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
      
      public void adbBackup(ParcelFileDescriptor paramParcelFileDescriptor, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeInt(paramBoolean3);
          localParcel1.writeInt(paramBoolean4);
          localParcel1.writeInt(paramBoolean5);
          localParcel1.writeInt(paramBoolean6);
          localParcel1.writeInt(paramBoolean7);
          localParcel1.writeInt(paramBoolean8);
          localParcel1.writeStringArray(paramArrayOfString);
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
      
      public void adbRestore(ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
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
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void agentConnected(String paramString, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void agentDisconnected(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void backupNow()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IRestoreSession beginRestoreSession(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = IRestoreSession.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void cancelBackups()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearBackupData(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void dataChanged(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
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
      
      public String[] filterAppsEligibleForBackup(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfString = localParcel2.createStringArray();
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int fullBackupForAsusTransfer(String paramString, IBackupObserver paramIBackupObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          if (paramIBackupObserver != null) {
            paramString = paramIBackupObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(39, localParcel1, localParcel2, 0);
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
      
      public int fullRestoreForAsusTransfer(String paramString, IRestoreObserver paramIRestoreObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          if (paramIRestoreObserver != null) {
            paramString = paramIRestoreObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(40, localParcel1, localParcel2, 0);
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
      
      public void fullTransportBackup(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeStringArray(paramArrayOfString);
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
      
      public long getAvailableRestoreToken(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          mRemote.transact(33, localParcel1, localParcel2, 0);
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
      
      public Intent getConfigurationIntent(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
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
      
      public String getCurrentTransport()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          mRemote.transact(19, localParcel1, localParcel2, 0);
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
      
      public Intent getDataManagementIntent(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
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
      
      public String getDataManagementLabel(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDestinationString(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.backup.IBackupManager";
      }
      
      public String[] getTransportWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasBackupPassword()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(12, localParcel1, localParcel2, 0);
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
      
      public void initializeTransports(String[] paramArrayOfString, IBackupObserver paramIBackupObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeStringArray(paramArrayOfString);
          if (paramIBackupObserver != null) {
            paramArrayOfString = paramIBackupObserver.asBinder();
          } else {
            paramArrayOfString = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfString);
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
      
      public boolean isAppEligibleForBackup(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(34, localParcel1, localParcel2, 0);
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
      
      public boolean isBackupEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
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
      
      public boolean isBackupServiceActive(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(32, localParcel1, localParcel2, 0);
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
      
      public ComponentName[] listAllTransportComponents()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName[] arrayOfComponentName = (ComponentName[])localParcel2.createTypedArray(ComponentName.CREATOR);
          return arrayOfComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] listAllTransports()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void opComplete(int paramInt, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int requestBackup(String[] paramArrayOfString, IBackupObserver paramIBackupObserver, IBackupManagerMonitor paramIBackupManagerMonitor, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeStringArray(paramArrayOfString);
          Object localObject = null;
          if (paramIBackupObserver != null) {
            paramArrayOfString = paramIBackupObserver.asBinder();
          } else {
            paramArrayOfString = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfString);
          paramArrayOfString = localObject;
          if (paramIBackupManagerMonitor != null) {
            paramArrayOfString = paramIBackupManagerMonitor.asBinder();
          }
          localParcel1.writeStrongBinder(paramArrayOfString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(36, localParcel1, localParcel2, 0);
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
      
      public void restoreAtInstall(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
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
      
      public String selectBackupTransport(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void selectBackupTransportAsync(ComponentName paramComponentName, ISelectBackupTransportCallback paramISelectBackupTransportCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramISelectBackupTransportCallback != null) {
            paramComponentName = paramISelectBackupTransportCallback.asBinder();
          } else {
            paramComponentName = null;
          }
          localParcel1.writeStrongBinder(paramComponentName);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString);
          mRemote.transact(38, localParcel1, localParcel2, 0);
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
      
      public void setAutoRestore(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setBackupEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public boolean setBackupPassword(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(11, localParcel1, localParcel2, 0);
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
      
      public void setBackupProvisioned(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setBackupServiceActive(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateTransportAttributes(ComponentName paramComponentName, String paramString1, Intent paramIntent1, String paramString2, Intent paramIntent2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.backup.IBackupManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          if (paramIntent1 != null)
          {
            localParcel1.writeInt(1);
            paramIntent1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          if (paramIntent2 != null)
          {
            localParcel1.writeInt(1);
            paramIntent2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString3);
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
    }
  }
}
