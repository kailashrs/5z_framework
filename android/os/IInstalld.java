package android.os;

import java.io.FileDescriptor;

public abstract interface IInstalld
  extends IInterface
{
  public abstract void assertFsverityRootHashMatches(String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void clearAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public abstract void clearAppProfiles(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean copySystemProfile(String paramString1, int paramInt, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract long createAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, int paramInt4)
    throws RemoteException;
  
  public abstract void createOatDir(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean createProfileSnapshot(int paramInt, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void createUserData(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void deleteOdex(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void destroyAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public abstract void destroyAppProfiles(String paramString)
    throws RemoteException;
  
  public abstract void destroyProfileSnapshot(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void destroyUserData(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void dexopt(String paramString1, int paramInt1, String paramString2, String paramString3, int paramInt2, String paramString4, int paramInt3, String paramString5, String paramString6, String paramString7, String paramString8, boolean paramBoolean, int paramInt4, String paramString9, String paramString10, String paramString11)
    throws RemoteException;
  
  public abstract boolean dumpProfiles(int paramInt, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void fixupAppData(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void freeCache(String paramString, long paramLong1, long paramLong2, int paramInt)
    throws RemoteException;
  
  public abstract long[] getAppSize(String paramString, String[] paramArrayOfString1, int paramInt1, int paramInt2, int paramInt3, long[] paramArrayOfLong, String[] paramArrayOfString2)
    throws RemoteException;
  
  public abstract long[] getExternalSize(String paramString, int paramInt1, int paramInt2, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract long[] getUserSize(String paramString, int paramInt1, int paramInt2, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract byte[] hashSecondaryDexFile(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
    throws RemoteException;
  
  public abstract void idmap(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void installApkVerity(String paramString, FileDescriptor paramFileDescriptor, int paramInt)
    throws RemoteException;
  
  public abstract void invalidateMounts()
    throws RemoteException;
  
  public abstract boolean isQuotaSupported(String paramString)
    throws RemoteException;
  
  public abstract void linkFile(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void linkNativeLibraryDirectory(String paramString1, String paramString2, String paramString3, int paramInt)
    throws RemoteException;
  
  public abstract void markBootComplete(String paramString)
    throws RemoteException;
  
  public abstract boolean mergeProfiles(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void migrateAppData(String paramString1, String paramString2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void moveAb(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void moveCompleteApp(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, String paramString5, int paramInt2)
    throws RemoteException;
  
  public abstract boolean prepareAppProfile(String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, String paramString4)
    throws RemoteException;
  
  public abstract boolean reconcileSecondaryDexFile(String paramString1, String paramString2, int paramInt1, String[] paramArrayOfString, String paramString3, int paramInt2)
    throws RemoteException;
  
  public abstract void removeIdmap(String paramString)
    throws RemoteException;
  
  public abstract void restoreconAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3)
    throws RemoteException;
  
  public abstract void rmPackageDir(String paramString)
    throws RemoteException;
  
  public abstract void rmdex(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setAppQuota(String paramString, int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInstalld
  {
    private static final String DESCRIPTOR = "android.os.IInstalld";
    static final int TRANSACTION_assertFsverityRootHashMatches = 34;
    static final int TRANSACTION_clearAppData = 6;
    static final int TRANSACTION_clearAppProfiles = 19;
    static final int TRANSACTION_copySystemProfile = 18;
    static final int TRANSACTION_createAppData = 3;
    static final int TRANSACTION_createOatDir = 29;
    static final int TRANSACTION_createProfileSnapshot = 21;
    static final int TRANSACTION_createUserData = 1;
    static final int TRANSACTION_deleteOdex = 32;
    static final int TRANSACTION_destroyAppData = 7;
    static final int TRANSACTION_destroyAppProfiles = 20;
    static final int TRANSACTION_destroyProfileSnapshot = 22;
    static final int TRANSACTION_destroyUserData = 2;
    static final int TRANSACTION_dexopt = 14;
    static final int TRANSACTION_dumpProfiles = 17;
    static final int TRANSACTION_fixupAppData = 8;
    static final int TRANSACTION_freeCache = 27;
    static final int TRANSACTION_getAppSize = 9;
    static final int TRANSACTION_getExternalSize = 11;
    static final int TRANSACTION_getUserSize = 10;
    static final int TRANSACTION_hashSecondaryDexFile = 36;
    static final int TRANSACTION_idmap = 23;
    static final int TRANSACTION_installApkVerity = 33;
    static final int TRANSACTION_invalidateMounts = 37;
    static final int TRANSACTION_isQuotaSupported = 38;
    static final int TRANSACTION_linkFile = 30;
    static final int TRANSACTION_linkNativeLibraryDirectory = 28;
    static final int TRANSACTION_markBootComplete = 26;
    static final int TRANSACTION_mergeProfiles = 16;
    static final int TRANSACTION_migrateAppData = 5;
    static final int TRANSACTION_moveAb = 31;
    static final int TRANSACTION_moveCompleteApp = 13;
    static final int TRANSACTION_prepareAppProfile = 39;
    static final int TRANSACTION_reconcileSecondaryDexFile = 35;
    static final int TRANSACTION_removeIdmap = 24;
    static final int TRANSACTION_restoreconAppData = 4;
    static final int TRANSACTION_rmPackageDir = 25;
    static final int TRANSACTION_rmdex = 15;
    static final int TRANSACTION_setAppQuota = 12;
    
    public Stub()
    {
      attachInterface(this, "android.os.IInstalld");
    }
    
    public static IInstalld asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IInstalld");
      if ((localIInterface != null) && ((localIInterface instanceof IInstalld))) {
        return (IInstalld)localIInterface;
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
        case 39: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramInt1 = prepareAppProfile(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramInt1 = isQuotaSupported(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          invalidateMounts();
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramParcel1 = hashSecondaryDexFile(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramInt1 = reconcileSecondaryDexFile(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.createStringArray(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          assertFsverityRootHashMatches(paramParcel1.readString(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          installApkVerity(paramParcel1.readString(), paramParcel1.readRawFileDescriptor(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          deleteOdex(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          moveAb(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          linkFile(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          createOatDir(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          linkNativeLibraryDirectory(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          freeCache(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          markBootComplete(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          rmPackageDir(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          removeIdmap(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          idmap(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          destroyProfileSnapshot(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramInt1 = createProfileSnapshot(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          destroyAppProfiles(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          clearAppProfiles(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramInt1 = copySystemProfile(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramInt1 = dumpProfiles(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramInt1 = mergeProfiles(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          rmdex(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          String str1 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          String str2 = paramParcel1.readString();
          String str3 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          String str4 = paramParcel1.readString();
          int i = paramParcel1.readInt();
          String str5 = paramParcel1.readString();
          String str6 = paramParcel1.readString();
          String str7 = paramParcel1.readString();
          String str8 = paramParcel1.readString();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          dexopt(str1, paramInt1, str2, str3, paramInt2, str4, i, str5, str6, str7, str8, bool, paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          moveCompleteApp(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          setAppQuota(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramParcel1 = getExternalSize(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeLongArray(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramParcel1 = getUserSize(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeLongArray(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          paramParcel1 = getAppSize(paramParcel1.readString(), paramParcel1.createStringArray(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createLongArray(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeLongArray(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          fixupAppData(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          destroyAppData(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          clearAppData(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          migrateAppData(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          restoreconAppData(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          long l = createAppData(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IInstalld");
          destroyUserData(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IInstalld");
        createUserData(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IInstalld");
      return true;
    }
    
    private static class Proxy
      implements IInstalld
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
      
      public void assertFsverityRootHashMatches(String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void clearAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeLong(paramLong);
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
      
      public void clearAppProfiles(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public boolean copySystemProfile(String paramString1, int paramInt, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(18, localParcel1, localParcel2, 0);
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
      
      public long createAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public void createOatDir(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean createProfileSnapshot(int paramInt, String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(21, localParcel1, localParcel2, 0);
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
      
      public void createUserData(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void deleteOdex(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void destroyAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeLong(paramLong);
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
      
      public void destroyAppProfiles(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
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
      
      public void destroyProfileSnapshot(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void destroyUserData(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
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
      
      /* Error */
      public void dexopt(String paramString1, int paramInt1, String paramString2, String paramString3, int paramInt2, String paramString4, int paramInt3, String paramString5, String paramString6, String paramString7, String paramString8, boolean paramBoolean, int paramInt4, String paramString9, String paramString10, String paramString11)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 17
        //   5: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 18
        //   10: aload 17
        //   12: ldc 34
        //   14: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 17
        //   19: aload_1
        //   20: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   23: aload 17
        //   25: iload_2
        //   26: invokevirtual 64	android/os/Parcel:writeInt	(I)V
        //   29: aload 17
        //   31: aload_3
        //   32: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   35: aload 17
        //   37: aload 4
        //   39: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   42: aload 17
        //   44: iload 5
        //   46: invokevirtual 64	android/os/Parcel:writeInt	(I)V
        //   49: aload 17
        //   51: aload 6
        //   53: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   56: aload 17
        //   58: iload 7
        //   60: invokevirtual 64	android/os/Parcel:writeInt	(I)V
        //   63: aload 17
        //   65: aload 8
        //   67: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   70: aload 17
        //   72: aload 9
        //   74: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   77: aload 17
        //   79: aload 10
        //   81: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   84: aload 17
        //   86: aload 11
        //   88: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   91: aload 17
        //   93: iload 12
        //   95: invokevirtual 64	android/os/Parcel:writeInt	(I)V
        //   98: aload 17
        //   100: iload 13
        //   102: invokevirtual 64	android/os/Parcel:writeInt	(I)V
        //   105: aload 17
        //   107: aload 14
        //   109: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   112: aload 17
        //   114: aload 15
        //   116: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   119: aload 17
        //   121: aload 16
        //   123: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   126: aload_0
        //   127: getfield 19	android/os/IInstalld$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   130: bipush 14
        //   132: aload 17
        //   134: aload 18
        //   136: iconst_0
        //   137: invokeinterface 51 5 0
        //   142: pop
        //   143: aload 18
        //   145: invokevirtual 54	android/os/Parcel:readException	()V
        //   148: aload 18
        //   150: invokevirtual 57	android/os/Parcel:recycle	()V
        //   153: aload 17
        //   155: invokevirtual 57	android/os/Parcel:recycle	()V
        //   158: return
        //   159: astore_1
        //   160: goto +44 -> 204
        //   163: astore_1
        //   164: goto +40 -> 204
        //   167: astore_1
        //   168: goto +36 -> 204
        //   171: astore_1
        //   172: goto +32 -> 204
        //   175: astore_1
        //   176: goto +28 -> 204
        //   179: astore_1
        //   180: goto +24 -> 204
        //   183: astore_1
        //   184: goto +20 -> 204
        //   187: astore_1
        //   188: goto +16 -> 204
        //   191: astore_1
        //   192: goto +12 -> 204
        //   195: astore_1
        //   196: goto +8 -> 204
        //   199: astore_1
        //   200: goto +4 -> 204
        //   203: astore_1
        //   204: aload 18
        //   206: invokevirtual 57	android/os/Parcel:recycle	()V
        //   209: aload 17
        //   211: invokevirtual 57	android/os/Parcel:recycle	()V
        //   214: aload_1
        //   215: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	216	0	this	Proxy
        //   0	216	1	paramString1	String
        //   0	216	2	paramInt1	int
        //   0	216	3	paramString2	String
        //   0	216	4	paramString3	String
        //   0	216	5	paramInt2	int
        //   0	216	6	paramString4	String
        //   0	216	7	paramInt3	int
        //   0	216	8	paramString5	String
        //   0	216	9	paramString6	String
        //   0	216	10	paramString7	String
        //   0	216	11	paramString8	String
        //   0	216	12	paramBoolean	boolean
        //   0	216	13	paramInt4	int
        //   0	216	14	paramString9	String
        //   0	216	15	paramString10	String
        //   0	216	16	paramString11	String
        //   3	207	17	localParcel1	Parcel
        //   8	197	18	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   98	148	159	finally
        //   91	98	163	finally
        //   84	91	167	finally
        //   77	84	171	finally
        //   70	77	175	finally
        //   63	70	179	finally
        //   56	63	183	finally
        //   49	56	187	finally
        //   42	49	191	finally
        //   35	42	195	finally
        //   29	35	199	finally
        //   10	29	203	finally
      }
      
      public boolean dumpProfiles(int paramInt, String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(17, localParcel1, localParcel2, 0);
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
      
      public void fixupAppData(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
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
      
      public void freeCache(String paramString, long paramLong1, long paramLong2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long[] getAppSize(String paramString, String[] paramArrayOfString1, int paramInt1, int paramInt2, int paramInt3, long[] paramArrayOfLong, String[] paramArrayOfString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeLongArray(paramArrayOfLong);
          localParcel1.writeStringArray(paramArrayOfString2);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createLongArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long[] getExternalSize(String paramString, int paramInt1, int paramInt2, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createLongArray();
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
        return "android.os.IInstalld";
      }
      
      public long[] getUserSize(String paramString, int paramInt1, int paramInt2, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createLongArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] hashSecondaryDexFile(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.createByteArray();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void idmap(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
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
      
      public void installApkVerity(String paramString, FileDescriptor paramFileDescriptor, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeRawFileDescriptor(paramFileDescriptor);
          localParcel1.writeInt(paramInt);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void invalidateMounts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
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
      
      public boolean isQuotaSupported(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(38, localParcel1, localParcel2, 0);
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
      
      public void linkFile(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
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
      
      public void linkNativeLibraryDirectory(String paramString1, String paramString2, String paramString3, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void markBootComplete(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean mergeProfiles(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(16, localParcel1, localParcel2, 0);
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
      
      public void migrateAppData(String paramString1, String paramString2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void moveAb(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
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
      
      public void moveCompleteApp(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, String paramString5, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString5);
          localParcel1.writeInt(paramInt2);
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
      
      public boolean prepareAppProfile(String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, String paramString4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(39, localParcel1, localParcel2, 0);
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
      
      public boolean reconcileSecondaryDexFile(String paramString1, String paramString2, int paramInt1, String[] paramArrayOfString, String paramString3, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(35, localParcel1, localParcel2, 0);
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
      
      public void removeIdmap(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
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
      
      public void restoreconAppData(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString3);
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
      
      public void rmPackageDir(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
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
      
      public void rmdex(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void setAppQuota(String paramString, int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IInstalld");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeLong(paramLong);
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
    }
  }
}
