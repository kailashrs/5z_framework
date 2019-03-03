package android.content;

import android.accounts.Account;
import android.database.IContentObserver;
import android.database.IContentObserver.Stub;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IContentService
  extends IInterface
{
  public abstract void addPeriodicSync(Account paramAccount, String paramString, Bundle paramBundle, long paramLong)
    throws RemoteException;
  
  public abstract void addStatusChangeListener(int paramInt, ISyncStatusObserver paramISyncStatusObserver)
    throws RemoteException;
  
  public abstract void cancelRequest(SyncRequest paramSyncRequest)
    throws RemoteException;
  
  public abstract void cancelSync(Account paramAccount, String paramString, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void cancelSyncAsUser(Account paramAccount, String paramString, ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract Bundle getCache(String paramString, Uri paramUri, int paramInt)
    throws RemoteException;
  
  public abstract List<SyncInfo> getCurrentSyncs()
    throws RemoteException;
  
  public abstract List<SyncInfo> getCurrentSyncsAsUser(int paramInt)
    throws RemoteException;
  
  public abstract int getIsSyncable(Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract int getIsSyncableAsUser(Account paramAccount, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean getMasterSyncAutomatically()
    throws RemoteException;
  
  public abstract boolean getMasterSyncAutomaticallyAsUser(int paramInt)
    throws RemoteException;
  
  public abstract List<PeriodicSync> getPeriodicSyncs(Account paramAccount, String paramString, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract String[] getSyncAdapterPackagesForAuthorityAsUser(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract SyncAdapterType[] getSyncAdapterTypes()
    throws RemoteException;
  
  public abstract SyncAdapterType[] getSyncAdapterTypesAsUser(int paramInt)
    throws RemoteException;
  
  public abstract boolean getSyncAutomatically(Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract boolean getSyncAutomaticallyAsUser(Account paramAccount, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract SyncStatusInfo getSyncStatus(Account paramAccount, String paramString, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract SyncStatusInfo getSyncStatusAsUser(Account paramAccount, String paramString, ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean isSyncActive(Account paramAccount, String paramString, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isSyncPending(Account paramAccount, String paramString, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isSyncPendingAsUser(Account paramAccount, String paramString, ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract void notifyChange(Uri paramUri, IContentObserver paramIContentObserver, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void putCache(String paramString, Uri paramUri, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract void registerContentObserver(Uri paramUri, boolean paramBoolean, IContentObserver paramIContentObserver, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void removePeriodicSync(Account paramAccount, String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void removeStatusChangeListener(ISyncStatusObserver paramISyncStatusObserver)
    throws RemoteException;
  
  public abstract void requestSync(Account paramAccount, String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void resetTodayStats()
    throws RemoteException;
  
  public abstract void setIsSyncable(Account paramAccount, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setMasterSyncAutomatically(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setMasterSyncAutomaticallyAsUser(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setSyncAutomatically(Account paramAccount, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSyncAutomaticallyAsUser(Account paramAccount, String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void sync(SyncRequest paramSyncRequest)
    throws RemoteException;
  
  public abstract void syncAsUser(SyncRequest paramSyncRequest, int paramInt)
    throws RemoteException;
  
  public abstract void unregisterContentObserver(IContentObserver paramIContentObserver)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IContentService
  {
    private static final String DESCRIPTOR = "android.content.IContentService";
    static final int TRANSACTION_addPeriodicSync = 15;
    static final int TRANSACTION_addStatusChangeListener = 34;
    static final int TRANSACTION_cancelRequest = 9;
    static final int TRANSACTION_cancelSync = 7;
    static final int TRANSACTION_cancelSyncAsUser = 8;
    static final int TRANSACTION_getCache = 37;
    static final int TRANSACTION_getCurrentSyncs = 24;
    static final int TRANSACTION_getCurrentSyncsAsUser = 25;
    static final int TRANSACTION_getIsSyncable = 17;
    static final int TRANSACTION_getIsSyncableAsUser = 18;
    static final int TRANSACTION_getMasterSyncAutomatically = 22;
    static final int TRANSACTION_getMasterSyncAutomaticallyAsUser = 23;
    static final int TRANSACTION_getPeriodicSyncs = 14;
    static final int TRANSACTION_getSyncAdapterPackagesForAuthorityAsUser = 28;
    static final int TRANSACTION_getSyncAdapterTypes = 26;
    static final int TRANSACTION_getSyncAdapterTypesAsUser = 27;
    static final int TRANSACTION_getSyncAutomatically = 10;
    static final int TRANSACTION_getSyncAutomaticallyAsUser = 11;
    static final int TRANSACTION_getSyncStatus = 30;
    static final int TRANSACTION_getSyncStatusAsUser = 31;
    static final int TRANSACTION_isSyncActive = 29;
    static final int TRANSACTION_isSyncPending = 32;
    static final int TRANSACTION_isSyncPendingAsUser = 33;
    static final int TRANSACTION_notifyChange = 3;
    static final int TRANSACTION_putCache = 36;
    static final int TRANSACTION_registerContentObserver = 2;
    static final int TRANSACTION_removePeriodicSync = 16;
    static final int TRANSACTION_removeStatusChangeListener = 35;
    static final int TRANSACTION_requestSync = 4;
    static final int TRANSACTION_resetTodayStats = 38;
    static final int TRANSACTION_setIsSyncable = 19;
    static final int TRANSACTION_setMasterSyncAutomatically = 20;
    static final int TRANSACTION_setMasterSyncAutomaticallyAsUser = 21;
    static final int TRANSACTION_setSyncAutomatically = 12;
    static final int TRANSACTION_setSyncAutomaticallyAsUser = 13;
    static final int TRANSACTION_sync = 5;
    static final int TRANSACTION_syncAsUser = 6;
    static final int TRANSACTION_unregisterContentObserver = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.IContentService");
    }
    
    public static IContentService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.IContentService");
      if ((localIInterface != null) && ((localIInterface instanceof IContentService))) {
        return (IContentService)localIInterface;
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
        boolean bool3 = false;
        boolean bool4 = false;
        Object localObject1 = null;
        String str1 = null;
        String str2 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        Object localObject17 = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 38: 
          paramParcel1.enforceInterface("android.content.IContentService");
          resetTodayStats();
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.content.IContentService");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          }
          paramParcel1 = getCache((String)localObject1, (Uri)localObject20, paramParcel1.readInt());
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
        case 36: 
          paramParcel1.enforceInterface("android.content.IContentService");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          putCache(str1, (Uri)localObject20, (Bundle)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.content.IContentService");
          removeStatusChangeListener(ISyncStatusObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.content.IContentService");
          addStatusChangeListener(paramParcel1.readInt(), ISyncStatusObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = str1;
          }
          paramInt1 = isSyncPendingAsUser((Account)localObject20, str2, (ComponentName)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          paramInt1 = isSyncPending((Account)localObject20, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject2;
          }
          paramParcel1 = getSyncStatusAsUser((Account)localObject20, str1, (ComponentName)localObject1, paramParcel1.readInt());
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
        case 30: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramParcel1 = getSyncStatus((Account)localObject20, (String)localObject1, paramParcel1);
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
        case 29: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = isSyncActive((Account)localObject20, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.content.IContentService");
          paramParcel1 = getSyncAdapterPackagesForAuthorityAsUser(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.content.IContentService");
          paramParcel1 = getSyncAdapterTypesAsUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.content.IContentService");
          paramParcel1 = getSyncAdapterTypes();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.content.IContentService");
          paramParcel1 = getCurrentSyncsAsUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.content.IContentService");
          paramParcel1 = getCurrentSyncs();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.content.IContentService");
          paramInt1 = getMasterSyncAutomaticallyAsUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.content.IContentService");
          paramInt1 = getMasterSyncAutomatically();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setMasterSyncAutomaticallyAsUser(bool4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.content.IContentService");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setMasterSyncAutomatically(bool4);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject5;
          }
          setIsSyncable((Account)localObject20, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject6;
          }
          paramInt1 = getIsSyncableAsUser((Account)localObject20, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject7;
          }
          paramInt1 = getIsSyncable((Account)localObject20, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          removePeriodicSync((Account)localObject20, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          addPeriodicSync((Account)localObject20, str1, (Bundle)localObject1, paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          paramParcel1 = getPeriodicSyncs((Account)localObject20, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject10;
          }
          localObject1 = paramParcel1.readString();
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setSyncAutomaticallyAsUser((Account)localObject20, (String)localObject1, bool4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject11;
          }
          localObject1 = paramParcel1.readString();
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setSyncAutomatically((Account)localObject20, (String)localObject1, bool4);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject12;
          }
          paramInt1 = getSyncAutomaticallyAsUser((Account)localObject20, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject13;
          }
          paramInt1 = getSyncAutomatically((Account)localObject20, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SyncRequest)SyncRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          cancelRequest(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject15;
          }
          cancelSyncAsUser((Account)localObject20, str1, (ComponentName)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          cancelSync((Account)localObject20, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (SyncRequest)SyncRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = localObject17;
          }
          syncAsUser((SyncRequest)localObject20, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SyncRequest)SyncRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          sync(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject19;
          }
          requestSync((Account)localObject20, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          localObject1 = IContentObserver.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          notifyChange((Uri)localObject20, (IContentObserver)localObject1, bool4, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.IContentService");
          if (paramParcel1.readInt() != 0) {
            localObject20 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject20 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          registerContentObserver((Uri)localObject20, bool4, IContentObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.content.IContentService");
        unregisterContentObserver(IContentObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.IContentService");
      return true;
    }
    
    private static class Proxy
      implements IContentService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addPeriodicSync(Account paramAccount, String paramString, Bundle paramBundle, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong);
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
      
      public void addStatusChangeListener(int paramInt, ISyncStatusObserver paramISyncStatusObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          localParcel1.writeInt(paramInt);
          if (paramISyncStatusObserver != null) {
            paramISyncStatusObserver = paramISyncStatusObserver.asBinder();
          } else {
            paramISyncStatusObserver = null;
          }
          localParcel1.writeStrongBinder(paramISyncStatusObserver);
          mRemote.transact(34, localParcel1, localParcel2, 0);
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
      
      public void cancelRequest(SyncRequest paramSyncRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramSyncRequest != null)
          {
            localParcel1.writeInt(1);
            paramSyncRequest.writeToParcel(localParcel1, 0);
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
      
      public void cancelSync(Account paramAccount, String paramString, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void cancelSyncAsUser(Account paramAccount, String paramString, ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public Bundle getCache(String paramString, Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
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
          localParcel1.writeInt(paramInt);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
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
      
      public List<SyncInfo> getCurrentSyncs()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(SyncInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<SyncInfo> getCurrentSyncsAsUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(SyncInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.IContentService";
      }
      
      public int getIsSyncable(Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
      
      public int getIsSyncableAsUser(Account paramAccount, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
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
      
      public boolean getMasterSyncAutomatically()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(22, localParcel1, localParcel2, 0);
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
      
      public boolean getMasterSyncAutomaticallyAsUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(23, localParcel1, localParcel2, 0);
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
      
      public List<PeriodicSync> getPeriodicSyncs(Account paramAccount, String paramString, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAccount = localParcel2.createTypedArrayList(PeriodicSync.CREATOR);
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getSyncAdapterPackagesForAuthorityAsUser(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SyncAdapterType[] getSyncAdapterTypes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          SyncAdapterType[] arrayOfSyncAdapterType = (SyncAdapterType[])localParcel2.createTypedArray(SyncAdapterType.CREATOR);
          return arrayOfSyncAdapterType;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SyncAdapterType[] getSyncAdapterTypesAsUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          SyncAdapterType[] arrayOfSyncAdapterType = (SyncAdapterType[])localParcel2.createTypedArray(SyncAdapterType.CREATOR);
          return arrayOfSyncAdapterType;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getSyncAutomatically(Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public boolean getSyncAutomaticallyAsUser(Account paramAccount, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public SyncStatusInfo getSyncStatus(Account paramAccount, String paramString, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramAccount = (SyncStatusInfo)SyncStatusInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramAccount = null;
          }
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SyncStatusInfo getSyncStatusAsUser(Account paramAccount, String paramString, ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramAccount = (SyncStatusInfo)SyncStatusInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramAccount = null;
          }
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSyncActive(Account paramAccount, String paramString, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public boolean isSyncPending(Account paramAccount, String paramString, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public boolean isSyncPendingAsUser(Account paramAccount, String paramString, ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public void notifyChange(Uri paramUri, IContentObserver paramIContentObserver, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIContentObserver != null) {
            paramUri = paramIContentObserver.asBinder();
          } else {
            paramUri = null;
          }
          localParcel1.writeStrongBinder(paramUri);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void putCache(String paramString, Uri paramUri, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
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
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerContentObserver(Uri paramUri, boolean paramBoolean, IContentObserver paramIContentObserver, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          if (paramIContentObserver != null) {
            paramUri = paramIContentObserver.asBinder();
          } else {
            paramUri = null;
          }
          localParcel1.writeStrongBinder(paramUri);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void removePeriodicSync(Account paramAccount, String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
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
      
      public void removeStatusChangeListener(ISyncStatusObserver paramISyncStatusObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramISyncStatusObserver != null) {
            paramISyncStatusObserver = paramISyncStatusObserver.asBinder();
          } else {
            paramISyncStatusObserver = null;
          }
          localParcel1.writeStrongBinder(paramISyncStatusObserver);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestSync(Account paramAccount, String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
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
      
      public void resetTodayStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setIsSyncable(Account paramAccount, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public void setMasterSyncAutomatically(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setMasterSyncAutomaticallyAsUser(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setSyncAutomatically(Account paramAccount, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSyncAutomaticallyAsUser(Account paramAccount, String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
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
      
      public void sync(SyncRequest paramSyncRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramSyncRequest != null)
          {
            localParcel1.writeInt(1);
            paramSyncRequest.writeToParcel(localParcel1, 0);
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
      
      public void syncAsUser(SyncRequest paramSyncRequest, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramSyncRequest != null)
          {
            localParcel1.writeInt(1);
            paramSyncRequest.writeToParcel(localParcel1, 0);
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
      
      public void unregisterContentObserver(IContentObserver paramIContentObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IContentService");
          if (paramIContentObserver != null) {
            paramIContentObserver = paramIContentObserver.asBinder();
          } else {
            paramIContentObserver = null;
          }
          localParcel1.writeStrongBinder(paramIContentObserver);
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
