package android.os.storage;

import android.content.pm.IPackageMoveObserver;
import android.content.pm.IPackageMoveObserver.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IVoldTaskListener;
import android.os.IVoldTaskListener.Stub;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.os.AppFuseMount;

public abstract interface IStorageManager
  extends IInterface
{
  public abstract void abortIdleMaintenance()
    throws RemoteException;
  
  public abstract void addUserKeyAuth(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract void allocateBytes(String paramString1, long paramLong, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract void benchmark(String paramString, IVoldTaskListener paramIVoldTaskListener)
    throws RemoteException;
  
  public abstract int changeEncryptionPassword(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void clearPassword()
    throws RemoteException;
  
  public abstract void clearUserKeyAuth(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract void createUserKey(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int decryptStorage(String paramString)
    throws RemoteException;
  
  public abstract void destroyUserKey(int paramInt)
    throws RemoteException;
  
  public abstract void destroyUserStorage(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int encryptStorage(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void fixateNewestUserKeyAuth(int paramInt)
    throws RemoteException;
  
  public abstract void forgetAllVolumes()
    throws RemoteException;
  
  public abstract void forgetVolume(String paramString)
    throws RemoteException;
  
  public abstract void format(String paramString)
    throws RemoteException;
  
  public abstract void fstrim(int paramInt, IVoldTaskListener paramIVoldTaskListener)
    throws RemoteException;
  
  public abstract long getAllocatableBytes(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract long getCacheQuotaBytes(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract long getCacheSizeBytes(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract DiskInfo[] getDisks()
    throws RemoteException;
  
  public abstract int getEncryptionState()
    throws RemoteException;
  
  public abstract String getField(String paramString)
    throws RemoteException;
  
  public abstract String getMountedObbPath(String paramString)
    throws RemoteException;
  
  public abstract String getPassword()
    throws RemoteException;
  
  public abstract int getPasswordType()
    throws RemoteException;
  
  public abstract String getPrimaryStorageUuid()
    throws RemoteException;
  
  public abstract StorageVolume[] getVolumeList(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract VolumeRecord[] getVolumeRecords(int paramInt)
    throws RemoteException;
  
  public abstract VolumeInfo[] getVolumes(int paramInt)
    throws RemoteException;
  
  public abstract boolean isConvertibleToFBE()
    throws RemoteException;
  
  public abstract boolean isObbMounted(String paramString)
    throws RemoteException;
  
  public abstract boolean isUserKeyUnlocked(int paramInt)
    throws RemoteException;
  
  public abstract long lastMaintenance()
    throws RemoteException;
  
  public abstract void lockUserKey(int paramInt)
    throws RemoteException;
  
  public abstract void mkdirs(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void mount(String paramString)
    throws RemoteException;
  
  public abstract void mountObb(String paramString1, String paramString2, String paramString3, IObbActionListener paramIObbActionListener, int paramInt)
    throws RemoteException;
  
  public abstract AppFuseMount mountProxyFileDescriptorBridge()
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openProxyFileDescriptor(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void partitionMixed(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void partitionPrivate(String paramString)
    throws RemoteException;
  
  public abstract void partitionPublic(String paramString)
    throws RemoteException;
  
  public abstract void prepareUserStorage(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void registerListener(IStorageEventListener paramIStorageEventListener)
    throws RemoteException;
  
  public abstract void runIdleMaintenance()
    throws RemoteException;
  
  public abstract void runMaintenance()
    throws RemoteException;
  
  public abstract void setDebugFlags(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setField(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setPrimaryStorageUuid(String paramString, IPackageMoveObserver paramIPackageMoveObserver)
    throws RemoteException;
  
  public abstract void setVolumeNickname(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setVolumeUserFlags(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void shutdown(IStorageShutdownObserver paramIStorageShutdownObserver)
    throws RemoteException;
  
  public abstract void unlockUserKey(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract void unmount(String paramString)
    throws RemoteException;
  
  public abstract void unmountObb(String paramString, boolean paramBoolean, IObbActionListener paramIObbActionListener, int paramInt)
    throws RemoteException;
  
  public abstract void unregisterListener(IStorageEventListener paramIStorageEventListener)
    throws RemoteException;
  
  public abstract int verifyEncryptionPassword(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStorageManager
  {
    private static final String DESCRIPTOR = "android.os.storage.IStorageManager";
    static final int TRANSACTION_abortIdleMaintenance = 81;
    static final int TRANSACTION_addUserKeyAuth = 71;
    static final int TRANSACTION_allocateBytes = 79;
    static final int TRANSACTION_benchmark = 60;
    static final int TRANSACTION_changeEncryptionPassword = 29;
    static final int TRANSACTION_clearPassword = 38;
    static final int TRANSACTION_clearUserKeyAuth = 82;
    static final int TRANSACTION_createUserKey = 62;
    static final int TRANSACTION_decryptStorage = 27;
    static final int TRANSACTION_destroyUserKey = 63;
    static final int TRANSACTION_destroyUserStorage = 68;
    static final int TRANSACTION_encryptStorage = 28;
    static final int TRANSACTION_fixateNewestUserKeyAuth = 72;
    static final int TRANSACTION_forgetAllVolumes = 57;
    static final int TRANSACTION_forgetVolume = 56;
    static final int TRANSACTION_format = 50;
    static final int TRANSACTION_fstrim = 73;
    static final int TRANSACTION_getAllocatableBytes = 78;
    static final int TRANSACTION_getCacheQuotaBytes = 76;
    static final int TRANSACTION_getCacheSizeBytes = 77;
    static final int TRANSACTION_getDisks = 45;
    static final int TRANSACTION_getEncryptionState = 32;
    static final int TRANSACTION_getField = 40;
    static final int TRANSACTION_getMountedObbPath = 25;
    static final int TRANSACTION_getPassword = 37;
    static final int TRANSACTION_getPasswordType = 36;
    static final int TRANSACTION_getPrimaryStorageUuid = 58;
    static final int TRANSACTION_getVolumeList = 30;
    static final int TRANSACTION_getVolumeRecords = 47;
    static final int TRANSACTION_getVolumes = 46;
    static final int TRANSACTION_isConvertibleToFBE = 69;
    static final int TRANSACTION_isObbMounted = 24;
    static final int TRANSACTION_isUserKeyUnlocked = 66;
    static final int TRANSACTION_lastMaintenance = 42;
    static final int TRANSACTION_lockUserKey = 65;
    static final int TRANSACTION_mkdirs = 35;
    static final int TRANSACTION_mount = 48;
    static final int TRANSACTION_mountObb = 22;
    static final int TRANSACTION_mountProxyFileDescriptorBridge = 74;
    static final int TRANSACTION_openProxyFileDescriptor = 75;
    static final int TRANSACTION_partitionMixed = 53;
    static final int TRANSACTION_partitionPrivate = 52;
    static final int TRANSACTION_partitionPublic = 51;
    static final int TRANSACTION_prepareUserStorage = 67;
    static final int TRANSACTION_registerListener = 1;
    static final int TRANSACTION_runIdleMaintenance = 80;
    static final int TRANSACTION_runMaintenance = 43;
    static final int TRANSACTION_setDebugFlags = 61;
    static final int TRANSACTION_setField = 39;
    static final int TRANSACTION_setPrimaryStorageUuid = 59;
    static final int TRANSACTION_setVolumeNickname = 54;
    static final int TRANSACTION_setVolumeUserFlags = 55;
    static final int TRANSACTION_shutdown = 20;
    static final int TRANSACTION_unlockUserKey = 64;
    static final int TRANSACTION_unmount = 49;
    static final int TRANSACTION_unmountObb = 23;
    static final int TRANSACTION_unregisterListener = 2;
    static final int TRANSACTION_verifyEncryptionPassword = 33;
    
    public Stub()
    {
      attachInterface(this, "android.os.storage.IStorageManager");
    }
    
    public static IStorageManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.storage.IStorageManager");
      if ((localIInterface != null) && ((localIInterface instanceof IStorageManager))) {
        return (IStorageManager)localIInterface;
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
      if (paramInt1 != 20)
      {
        if (paramInt1 != 1598968902)
        {
          switch (paramInt1)
          {
          default: 
            boolean bool1 = false;
            boolean bool2 = false;
            switch (paramInt1)
            {
            default: 
              switch (paramInt1)
              {
              default: 
                switch (paramInt1)
                {
                default: 
                  switch (paramInt1)
                  {
                  default: 
                    switch (paramInt1)
                    {
                    default: 
                      switch (paramInt1)
                      {
                      default: 
                        switch (paramInt1)
                        {
                        default: 
                          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
                        case 82: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          clearUserKeyAuth(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
                          paramParcel2.writeNoException();
                          return true;
                        case 81: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          abortIdleMaintenance();
                          paramParcel2.writeNoException();
                          return true;
                        case 80: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          runIdleMaintenance();
                          paramParcel2.writeNoException();
                          return true;
                        case 79: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          allocateBytes(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readString());
                          paramParcel2.writeNoException();
                          return true;
                        case 78: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          l = getAllocatableBytes(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
                          paramParcel2.writeNoException();
                          paramParcel2.writeLong(l);
                          return true;
                        case 77: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          l = getCacheSizeBytes(paramParcel1.readString(), paramParcel1.readInt());
                          paramParcel2.writeNoException();
                          paramParcel2.writeLong(l);
                          return true;
                        case 76: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          l = getCacheQuotaBytes(paramParcel1.readString(), paramParcel1.readInt());
                          paramParcel2.writeNoException();
                          paramParcel2.writeLong(l);
                          return true;
                        case 75: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          paramParcel1 = openProxyFileDescriptor(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
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
                        case 74: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          paramParcel1 = mountProxyFileDescriptorBridge();
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
                        case 73: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          fstrim(paramParcel1.readInt(), IVoldTaskListener.Stub.asInterface(paramParcel1.readStrongBinder()));
                          paramParcel2.writeNoException();
                          return true;
                        case 72: 
                          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                          fixateNewestUserKeyAuth(paramParcel1.readInt());
                          paramParcel2.writeNoException();
                          return true;
                        }
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        addUserKeyAuth(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
                        paramParcel2.writeNoException();
                        return true;
                      case 69: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        paramInt1 = isConvertibleToFBE();
                        paramParcel2.writeNoException();
                        paramParcel2.writeInt(paramInt1);
                        return true;
                      case 68: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        destroyUserStorage(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        return true;
                      case 67: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        prepareUserStorage(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        return true;
                      case 66: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        paramInt1 = isUserKeyUnlocked(paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        paramParcel2.writeInt(paramInt1);
                        return true;
                      case 65: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        lockUserKey(paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        return true;
                      case 64: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        unlockUserKey(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
                        paramParcel2.writeNoException();
                        return true;
                      case 63: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        destroyUserKey(paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        return true;
                      case 62: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        paramInt2 = paramParcel1.readInt();
                        paramInt1 = paramParcel1.readInt();
                        if (paramParcel1.readInt() != 0) {
                          bool2 = true;
                        }
                        createUserKey(paramInt2, paramInt1, bool2);
                        paramParcel2.writeNoException();
                        return true;
                      case 61: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        setDebugFlags(paramParcel1.readInt(), paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        return true;
                      case 60: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        benchmark(paramParcel1.readString(), IVoldTaskListener.Stub.asInterface(paramParcel1.readStrongBinder()));
                        paramParcel2.writeNoException();
                        return true;
                      case 59: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        setPrimaryStorageUuid(paramParcel1.readString(), IPackageMoveObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
                        paramParcel2.writeNoException();
                        return true;
                      case 58: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        paramParcel1 = getPrimaryStorageUuid();
                        paramParcel2.writeNoException();
                        paramParcel2.writeString(paramParcel1);
                        return true;
                      case 57: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        forgetAllVolumes();
                        paramParcel2.writeNoException();
                        return true;
                      case 56: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        forgetVolume(paramParcel1.readString());
                        paramParcel2.writeNoException();
                        return true;
                      case 55: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        setVolumeUserFlags(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        return true;
                      case 54: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        setVolumeNickname(paramParcel1.readString(), paramParcel1.readString());
                        paramParcel2.writeNoException();
                        return true;
                      case 53: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        partitionMixed(paramParcel1.readString(), paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        return true;
                      case 52: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        partitionPrivate(paramParcel1.readString());
                        paramParcel2.writeNoException();
                        return true;
                      case 51: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        partitionPublic(paramParcel1.readString());
                        paramParcel2.writeNoException();
                        return true;
                      case 50: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        format(paramParcel1.readString());
                        paramParcel2.writeNoException();
                        return true;
                      case 49: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        unmount(paramParcel1.readString());
                        paramParcel2.writeNoException();
                        return true;
                      case 48: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        mount(paramParcel1.readString());
                        paramParcel2.writeNoException();
                        return true;
                      case 47: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        paramParcel1 = getVolumeRecords(paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        paramParcel2.writeTypedArray(paramParcel1, 1);
                        return true;
                      case 46: 
                        paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                        paramParcel1 = getVolumes(paramParcel1.readInt());
                        paramParcel2.writeNoException();
                        paramParcel2.writeTypedArray(paramParcel1, 1);
                        return true;
                      }
                      paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                      paramParcel1 = getDisks();
                      paramParcel2.writeNoException();
                      paramParcel2.writeTypedArray(paramParcel1, 1);
                      return true;
                    case 43: 
                      paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                      runMaintenance();
                      paramParcel2.writeNoException();
                      return true;
                    }
                    paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                    long l = lastMaintenance();
                    paramParcel2.writeNoException();
                    paramParcel2.writeLong(l);
                    return true;
                  case 40: 
                    paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                    paramParcel1 = getField(paramParcel1.readString());
                    paramParcel2.writeNoException();
                    paramParcel2.writeString(paramParcel1);
                    return true;
                  case 39: 
                    paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                    setField(paramParcel1.readString(), paramParcel1.readString());
                    return true;
                  case 38: 
                    paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                    clearPassword();
                    return true;
                  case 37: 
                    paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                    paramParcel1 = getPassword();
                    paramParcel2.writeNoException();
                    paramParcel2.writeString(paramParcel1);
                    return true;
                  case 36: 
                    paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                    paramInt1 = getPasswordType();
                    paramParcel2.writeNoException();
                    paramParcel2.writeInt(paramInt1);
                    return true;
                  }
                  paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                  mkdirs(paramParcel1.readString(), paramParcel1.readString());
                  paramParcel2.writeNoException();
                  return true;
                case 33: 
                  paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                  paramInt1 = verifyEncryptionPassword(paramParcel1.readString());
                  paramParcel2.writeNoException();
                  paramParcel2.writeInt(paramInt1);
                  return true;
                }
                paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                paramInt1 = getEncryptionState();
                paramParcel2.writeNoException();
                paramParcel2.writeInt(paramInt1);
                return true;
              case 30: 
                paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                paramParcel1 = getVolumeList(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
                paramParcel2.writeNoException();
                paramParcel2.writeTypedArray(paramParcel1, 1);
                return true;
              case 29: 
                paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                paramInt1 = changeEncryptionPassword(paramParcel1.readInt(), paramParcel1.readString());
                paramParcel2.writeNoException();
                paramParcel2.writeInt(paramInt1);
                return true;
              case 28: 
                paramParcel1.enforceInterface("android.os.storage.IStorageManager");
                paramInt1 = encryptStorage(paramParcel1.readInt(), paramParcel1.readString());
                paramParcel2.writeNoException();
                paramParcel2.writeInt(paramInt1);
                return true;
              }
              paramParcel1.enforceInterface("android.os.storage.IStorageManager");
              paramInt1 = decryptStorage(paramParcel1.readString());
              paramParcel2.writeNoException();
              paramParcel2.writeInt(paramInt1);
              return true;
            case 25: 
              paramParcel1.enforceInterface("android.os.storage.IStorageManager");
              paramParcel1 = getMountedObbPath(paramParcel1.readString());
              paramParcel2.writeNoException();
              paramParcel2.writeString(paramParcel1);
              return true;
            case 24: 
              paramParcel1.enforceInterface("android.os.storage.IStorageManager");
              paramInt1 = isObbMounted(paramParcel1.readString());
              paramParcel2.writeNoException();
              paramParcel2.writeInt(paramInt1);
              return true;
            case 23: 
              paramParcel1.enforceInterface("android.os.storage.IStorageManager");
              String str = paramParcel1.readString();
              bool2 = bool1;
              if (paramParcel1.readInt() != 0) {
                bool2 = true;
              }
              unmountObb(str, bool2, IObbActionListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
              paramParcel2.writeNoException();
              return true;
            }
            paramParcel1.enforceInterface("android.os.storage.IStorageManager");
            mountObb(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), IObbActionListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
            paramParcel2.writeNoException();
            return true;
          case 2: 
            paramParcel1.enforceInterface("android.os.storage.IStorageManager");
            unregisterListener(IStorageEventListener.Stub.asInterface(paramParcel1.readStrongBinder()));
            paramParcel2.writeNoException();
            return true;
          }
          paramParcel1.enforceInterface("android.os.storage.IStorageManager");
          registerListener(IStorageEventListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel2.writeString("android.os.storage.IStorageManager");
        return true;
      }
      paramParcel1.enforceInterface("android.os.storage.IStorageManager");
      shutdown(IStorageShutdownObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IStorageManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void abortIdleMaintenance()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addUserKeyAuth(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void allocateBytes(String paramString1, long paramLong, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(79, localParcel1, localParcel2, 0);
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
      
      public void benchmark(String paramString, IVoldTaskListener paramIVoldTaskListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          if (paramIVoldTaskListener != null) {
            paramString = paramIVoldTaskListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int changeEncryptionPassword(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(29, localParcel1, localParcel2, 0);
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
      
      public void clearPassword()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(38, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void clearUserKeyAuth(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void createUserKey(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int decryptStorage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
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
      
      public void destroyUserKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void destroyUserStorage(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int encryptStorage(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public void fixateNewestUserKeyAuth(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forgetAllVolumes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forgetVolume(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void format(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void fstrim(int paramInt, IVoldTaskListener paramIVoldTaskListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          if (paramIVoldTaskListener != null) {
            paramIVoldTaskListener = paramIVoldTaskListener.asBinder();
          } else {
            paramIVoldTaskListener = null;
          }
          localParcel1.writeStrongBinder(paramIVoldTaskListener);
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getAllocatableBytes(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(78, localParcel1, localParcel2, 0);
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
      
      public long getCacheQuotaBytes(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(76, localParcel1, localParcel2, 0);
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
      
      public long getCacheSizeBytes(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(77, localParcel1, localParcel2, 0);
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
      
      public DiskInfo[] getDisks()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          DiskInfo[] arrayOfDiskInfo = (DiskInfo[])localParcel2.createTypedArray(DiskInfo.CREATOR);
          return arrayOfDiskInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getEncryptionState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(32, localParcel1, localParcel2, 0);
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
      
      public String getField(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(40, localParcel1, localParcel2, 0);
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
        return "android.os.storage.IStorageManager";
      }
      
      public String getMountedObbPath(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(25, localParcel1, localParcel2, 0);
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
      
      public String getPassword()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(37, localParcel1, localParcel2, 0);
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
      
      public int getPasswordType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(36, localParcel1, localParcel2, 0);
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
      
      public String getPrimaryStorageUuid()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(58, localParcel1, localParcel2, 0);
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
      
      public StorageVolume[] getVolumeList(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = (StorageVolume[])localParcel2.createTypedArray(StorageVolume.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VolumeRecord[] getVolumeRecords(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VolumeRecord[] arrayOfVolumeRecord = (VolumeRecord[])localParcel2.createTypedArray(VolumeRecord.CREATOR);
          return arrayOfVolumeRecord;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VolumeInfo[] getVolumes(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VolumeInfo[] arrayOfVolumeInfo = (VolumeInfo[])localParcel2.createTypedArray(VolumeInfo.CREATOR);
          return arrayOfVolumeInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isConvertibleToFBE()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(69, localParcel1, localParcel2, 0);
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
      
      public boolean isObbMounted(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(24, localParcel1, localParcel2, 0);
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
      
      public boolean isUserKeyUnlocked(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(66, localParcel1, localParcel2, 0);
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
      
      public long lastMaintenance()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(42, localParcel1, localParcel2, 0);
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
      
      public void lockUserKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void mkdirs(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void mount(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void mountObb(String paramString1, String paramString2, String paramString3, IObbActionListener paramIObbActionListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          if (paramIObbActionListener != null) {
            paramString1 = paramIObbActionListener.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
          localParcel1.writeInt(paramInt);
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
      
      public AppFuseMount mountProxyFileDescriptorBridge()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          AppFuseMount localAppFuseMount;
          if (localParcel2.readInt() != 0) {
            localAppFuseMount = (AppFuseMount)AppFuseMount.CREATOR.createFromParcel(localParcel2);
          } else {
            localAppFuseMount = null;
          }
          return localAppFuseMount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor openProxyFileDescriptor(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParcelFileDescriptor localParcelFileDescriptor;
          if (localParcel2.readInt() != 0) {
            localParcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            localParcelFileDescriptor = null;
          }
          return localParcelFileDescriptor;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void partitionMixed(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void partitionPrivate(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void partitionPublic(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void prepareUserStorage(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerListener(IStorageEventListener paramIStorageEventListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          if (paramIStorageEventListener != null) {
            paramIStorageEventListener = paramIStorageEventListener.asBinder();
          } else {
            paramIStorageEventListener = null;
          }
          localParcel1.writeStrongBinder(paramIStorageEventListener);
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
      
      public void runIdleMaintenance()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void runMaintenance()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDebugFlags(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setField(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(39, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPrimaryStorageUuid(String paramString, IPackageMoveObserver paramIPackageMoveObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          if (paramIPackageMoveObserver != null) {
            paramString = paramIPackageMoveObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVolumeNickname(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVolumeUserFlags(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void shutdown(IStorageShutdownObserver paramIStorageShutdownObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          if (paramIStorageShutdownObserver != null) {
            paramIStorageShutdownObserver = paramIStorageShutdownObserver.asBinder();
          } else {
            paramIStorageShutdownObserver = null;
          }
          localParcel1.writeStrongBinder(paramIStorageShutdownObserver);
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
      
      public void unlockUserKey(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          mRemote.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unmount(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unmountObb(String paramString, boolean paramBoolean, IObbActionListener paramIObbActionListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          if (paramIObbActionListener != null) {
            paramString = paramIObbActionListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void unregisterListener(IStorageEventListener paramIStorageEventListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          if (paramIStorageEventListener != null) {
            paramIStorageEventListener = paramIStorageEventListener.asBinder();
          } else {
            paramIStorageEventListener = null;
          }
          localParcel1.writeStrongBinder(paramIStorageEventListener);
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
      
      public int verifyEncryptionPassword(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.storage.IStorageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(33, localParcel1, localParcel2, 0);
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
    }
  }
}
