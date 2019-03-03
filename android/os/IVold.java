package android.os;

import java.io.FileDescriptor;

public abstract interface IVold
  extends IInterface
{
  public static final int ENCRYPTION_FLAG_NO_UI = 4;
  public static final int ENCRYPTION_STATE_ERROR_CORRUPT = -4;
  public static final int ENCRYPTION_STATE_ERROR_INCOMPLETE = -2;
  public static final int ENCRYPTION_STATE_ERROR_INCONSISTENT = -3;
  public static final int ENCRYPTION_STATE_ERROR_UNKNOWN = -1;
  public static final int ENCRYPTION_STATE_NONE = 1;
  public static final int ENCRYPTION_STATE_OK = 0;
  public static final int FSTRIM_FLAG_DEEP_TRIM = 1;
  public static final int MOUNT_FLAG_PRIMARY = 1;
  public static final int MOUNT_FLAG_VISIBLE = 2;
  public static final int PARTITION_TYPE_MIXED = 2;
  public static final int PARTITION_TYPE_PRIVATE = 1;
  public static final int PARTITION_TYPE_PUBLIC = 0;
  public static final int PASSWORD_TYPE_DEFAULT = 1;
  public static final int PASSWORD_TYPE_PASSWORD = 0;
  public static final int PASSWORD_TYPE_PATTERN = 2;
  public static final int PASSWORD_TYPE_PIN = 3;
  public static final int REMOUNT_MODE_DEFAULT = 1;
  public static final int REMOUNT_MODE_NONE = 0;
  public static final int REMOUNT_MODE_READ = 2;
  public static final int REMOUNT_MODE_WRITE = 3;
  public static final int STORAGE_FLAG_CE = 2;
  public static final int STORAGE_FLAG_DE = 1;
  public static final int VOLUME_STATE_BAD_REMOVAL = 8;
  public static final int VOLUME_STATE_CHECKING = 1;
  public static final int VOLUME_STATE_EJECTING = 5;
  public static final int VOLUME_STATE_FORMATTING = 4;
  public static final int VOLUME_STATE_MOUNTED = 2;
  public static final int VOLUME_STATE_MOUNTED_READ_ONLY = 3;
  public static final int VOLUME_STATE_REMOVED = 7;
  public static final int VOLUME_STATE_UNMOUNTABLE = 6;
  public static final int VOLUME_STATE_UNMOUNTED = 0;
  public static final int VOLUME_TYPE_ASEC = 3;
  public static final int VOLUME_TYPE_EMULATED = 2;
  public static final int VOLUME_TYPE_OBB = 4;
  public static final int VOLUME_TYPE_PRIVATE = 1;
  public static final int VOLUME_TYPE_PUBLIC = 0;
  
  public abstract void abortIdleMaint(IVoldTaskListener paramIVoldTaskListener)
    throws RemoteException;
  
  public abstract void addUserKeyAuth(int paramInt1, int paramInt2, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void benchmark(String paramString, IVoldTaskListener paramIVoldTaskListener)
    throws RemoteException;
  
  public abstract void checkEncryption(String paramString)
    throws RemoteException;
  
  public abstract void clearUserKeyAuth(int paramInt1, int paramInt2, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract String createObb(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void createUserKey(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void destroyObb(String paramString)
    throws RemoteException;
  
  public abstract void destroyUserKey(int paramInt)
    throws RemoteException;
  
  public abstract void destroyUserStorage(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void encryptFstab(String paramString)
    throws RemoteException;
  
  public abstract void fbeEnable()
    throws RemoteException;
  
  public abstract void fdeChangePassword(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void fdeCheckPassword(String paramString)
    throws RemoteException;
  
  public abstract void fdeClearPassword()
    throws RemoteException;
  
  public abstract int fdeComplete()
    throws RemoteException;
  
  public abstract void fdeEnable(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract String fdeGetField(String paramString)
    throws RemoteException;
  
  public abstract String fdeGetPassword()
    throws RemoteException;
  
  public abstract int fdeGetPasswordType()
    throws RemoteException;
  
  public abstract void fdeRestart()
    throws RemoteException;
  
  public abstract void fdeSetField(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void fdeVerifyPassword(String paramString)
    throws RemoteException;
  
  public abstract void fixateNewestUserKeyAuth(int paramInt)
    throws RemoteException;
  
  public abstract void forgetPartition(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void format(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void fstrim(int paramInt, IVoldTaskListener paramIVoldTaskListener)
    throws RemoteException;
  
  public abstract void initUser0()
    throws RemoteException;
  
  public abstract boolean isConvertibleToFbe()
    throws RemoteException;
  
  public abstract void lockUserKey(int paramInt)
    throws RemoteException;
  
  public abstract void mkdirs(String paramString)
    throws RemoteException;
  
  public abstract void monitor()
    throws RemoteException;
  
  public abstract void mount(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract FileDescriptor mountAppFuse(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void mountDefaultEncrypted()
    throws RemoteException;
  
  public abstract void mountFstab(String paramString)
    throws RemoteException;
  
  public abstract void moveStorage(String paramString1, String paramString2, IVoldTaskListener paramIVoldTaskListener)
    throws RemoteException;
  
  public abstract void onSecureKeyguardStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onUserAdded(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onUserRemoved(int paramInt)
    throws RemoteException;
  
  public abstract void onUserStarted(int paramInt)
    throws RemoteException;
  
  public abstract void onUserStopped(int paramInt)
    throws RemoteException;
  
  public abstract void partition(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void prepareUserStorage(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void remountUid(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void reset()
    throws RemoteException;
  
  public abstract void runIdleMaint(IVoldTaskListener paramIVoldTaskListener)
    throws RemoteException;
  
  public abstract void setListener(IVoldListener paramIVoldListener)
    throws RemoteException;
  
  public abstract void shutdown()
    throws RemoteException;
  
  public abstract void unlockUserKey(int paramInt1, int paramInt2, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void unmount(String paramString)
    throws RemoteException;
  
  public abstract void unmountAppFuse(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVold
  {
    private static final String DESCRIPTOR = "android.os.IVold";
    static final int TRANSACTION_abortIdleMaint = 24;
    static final int TRANSACTION_addUserKeyAuth = 46;
    static final int TRANSACTION_benchmark = 15;
    static final int TRANSACTION_checkEncryption = 16;
    static final int TRANSACTION_clearUserKeyAuth = 47;
    static final int TRANSACTION_createObb = 20;
    static final int TRANSACTION_createUserKey = 44;
    static final int TRANSACTION_destroyObb = 21;
    static final int TRANSACTION_destroyUserKey = 45;
    static final int TRANSACTION_destroyUserStorage = 52;
    static final int TRANSACTION_encryptFstab = 43;
    static final int TRANSACTION_fbeEnable = 38;
    static final int TRANSACTION_fdeChangePassword = 31;
    static final int TRANSACTION_fdeCheckPassword = 27;
    static final int TRANSACTION_fdeClearPassword = 37;
    static final int TRANSACTION_fdeComplete = 29;
    static final int TRANSACTION_fdeEnable = 30;
    static final int TRANSACTION_fdeGetField = 33;
    static final int TRANSACTION_fdeGetPassword = 36;
    static final int TRANSACTION_fdeGetPasswordType = 35;
    static final int TRANSACTION_fdeRestart = 28;
    static final int TRANSACTION_fdeSetField = 34;
    static final int TRANSACTION_fdeVerifyPassword = 32;
    static final int TRANSACTION_fixateNewestUserKeyAuth = 48;
    static final int TRANSACTION_forgetPartition = 11;
    static final int TRANSACTION_format = 14;
    static final int TRANSACTION_fstrim = 22;
    static final int TRANSACTION_initUser0 = 40;
    static final int TRANSACTION_isConvertibleToFbe = 41;
    static final int TRANSACTION_lockUserKey = 50;
    static final int TRANSACTION_mkdirs = 19;
    static final int TRANSACTION_monitor = 2;
    static final int TRANSACTION_mount = 12;
    static final int TRANSACTION_mountAppFuse = 25;
    static final int TRANSACTION_mountDefaultEncrypted = 39;
    static final int TRANSACTION_mountFstab = 42;
    static final int TRANSACTION_moveStorage = 17;
    static final int TRANSACTION_onSecureKeyguardStateChanged = 9;
    static final int TRANSACTION_onUserAdded = 5;
    static final int TRANSACTION_onUserRemoved = 6;
    static final int TRANSACTION_onUserStarted = 7;
    static final int TRANSACTION_onUserStopped = 8;
    static final int TRANSACTION_partition = 10;
    static final int TRANSACTION_prepareUserStorage = 51;
    static final int TRANSACTION_remountUid = 18;
    static final int TRANSACTION_reset = 3;
    static final int TRANSACTION_runIdleMaint = 23;
    static final int TRANSACTION_setListener = 1;
    static final int TRANSACTION_shutdown = 4;
    static final int TRANSACTION_unlockUserKey = 49;
    static final int TRANSACTION_unmount = 13;
    static final int TRANSACTION_unmountAppFuse = 26;
    
    public Stub()
    {
      attachInterface(this, "android.os.IVold");
    }
    
    public static IVold asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IVold");
      if ((localIInterface != null) && ((localIInterface instanceof IVold))) {
        return (IVold)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 52: 
          paramParcel1.enforceInterface("android.os.IVold");
          destroyUserStorage(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.os.IVold");
          prepareUserStorage(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.os.IVold");
          lockUserKey(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.os.IVold");
          unlockUserKey(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.os.IVold");
          fixateNewestUserKeyAuth(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.os.IVold");
          clearUserKeyAuth(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.os.IVold");
          addUserKeyAuth(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.os.IVold");
          destroyUserKey(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          createUserKey(paramInt1, paramInt2, bool2);
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.os.IVold");
          encryptFstab(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.os.IVold");
          mountFstab(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramInt1 = isConvertibleToFbe();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.os.IVold");
          initUser0();
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.os.IVold");
          mountDefaultEncrypted();
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.os.IVold");
          fbeEnable();
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.os.IVold");
          fdeClearPassword();
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramParcel1 = fdeGetPassword();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramInt1 = fdeGetPasswordType();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.os.IVold");
          fdeSetField(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramParcel1 = fdeGetField(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.os.IVold");
          fdeVerifyPassword(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.os.IVold");
          fdeChangePassword(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.os.IVold");
          fdeEnable(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramInt1 = fdeComplete();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.os.IVold");
          fdeRestart();
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.os.IVold");
          fdeCheckPassword(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.os.IVold");
          unmountAppFuse(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramParcel1 = mountAppFuse(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeRawFileDescriptor(paramParcel1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.os.IVold");
          abortIdleMaint(IVoldTaskListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.os.IVold");
          runIdleMaint(IVoldTaskListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.os.IVold");
          fstrim(paramParcel1.readInt(), IVoldTaskListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.os.IVold");
          destroyObb(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.os.IVold");
          paramParcel1 = createObb(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.os.IVold");
          mkdirs(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.os.IVold");
          remountUid(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.os.IVold");
          moveStorage(paramParcel1.readString(), paramParcel1.readString(), IVoldTaskListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.os.IVold");
          checkEncryption(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.os.IVold");
          benchmark(paramParcel1.readString(), IVoldTaskListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.os.IVold");
          format(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.os.IVold");
          unmount(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.os.IVold");
          mount(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.os.IVold");
          forgetPartition(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.IVold");
          partition(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IVold");
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onSecureKeyguardStateChanged(bool2);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IVold");
          onUserStopped(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IVold");
          onUserStarted(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IVold");
          onUserRemoved(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IVold");
          onUserAdded(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IVold");
          shutdown();
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IVold");
          reset();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IVold");
          monitor();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IVold");
        setListener(IVoldListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IVold");
      return true;
    }
    
    private static class Proxy
      implements IVold
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void abortIdleMaint(IVoldTaskListener paramIVoldTaskListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          if (paramIVoldTaskListener != null) {
            paramIVoldTaskListener = paramIVoldTaskListener.asBinder();
          } else {
            paramIVoldTaskListener = null;
          }
          localParcel1.writeStrongBinder(paramIVoldTaskListener);
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
      
      public void addUserKeyAuth(int paramInt1, int paramInt2, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(46, localParcel1, localParcel2, 0);
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
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
          if (paramIVoldTaskListener != null) {
            paramString = paramIVoldTaskListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void checkEncryption(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void clearUserKeyAuth(int paramInt1, int paramInt2, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String createObb(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readString();
          return paramString1;
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
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void destroyObb(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
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
      
      public void destroyUserKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt);
          mRemote.transact(45, localParcel1, localParcel2, 0);
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
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void encryptFstab(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
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
      
      public void fbeEnable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void fdeChangePassword(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void fdeCheckPassword(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
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
      
      public void fdeClearPassword()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public int fdeComplete()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          mRemote.transact(29, localParcel1, localParcel2, 0);
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
      
      public void fdeEnable(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
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
      
      public String fdeGetField(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
          mRemote.transact(33, localParcel1, localParcel2, 0);
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
      
      public String fdeGetPassword()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          mRemote.transact(36, localParcel1, localParcel2, 0);
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
      
      public int fdeGetPasswordType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      public void fdeRestart()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void fdeSetField(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void fdeVerifyPassword(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
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
      
      public void fixateNewestUserKeyAuth(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt);
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
      
      public void forgetPartition(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void format(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void fstrim(int paramInt, IVoldTaskListener paramIVoldTaskListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt);
          if (paramIVoldTaskListener != null) {
            paramIVoldTaskListener = paramIVoldTaskListener.asBinder();
          } else {
            paramIVoldTaskListener = null;
          }
          localParcel1.writeStrongBinder(paramIVoldTaskListener);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IVold";
      }
      
      public void initUser0()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isConvertibleToFbe()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(41, localParcel1, localParcel2, 0);
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
      
      public void lockUserKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt);
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
      
      public void mkdirs(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
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
      
      public void monitor()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void mount(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public FileDescriptor mountAppFuse(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          FileDescriptor localFileDescriptor = localParcel2.readRawFileDescriptor();
          return localFileDescriptor;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void mountDefaultEncrypted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void mountFstab(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void moveStorage(String paramString1, String paramString2, IVoldTaskListener paramIVoldTaskListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramIVoldTaskListener != null) {
            paramString1 = paramIVoldTaskListener.asBinder();
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
      
      public void onSecureKeyguardStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void onUserAdded(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void onUserRemoved(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void onUserStarted(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void onUserStopped(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void partition(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void remountUid(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void reset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void runIdleMaint(IVoldTaskListener paramIVoldTaskListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          if (paramIVoldTaskListener != null) {
            paramIVoldTaskListener = paramIVoldTaskListener.asBinder();
          } else {
            paramIVoldTaskListener = null;
          }
          localParcel1.writeStrongBinder(paramIVoldTaskListener);
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
      
      public void setListener(IVoldListener paramIVoldListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          if (paramIVoldListener != null) {
            paramIVoldListener = paramIVoldListener.asBinder();
          } else {
            paramIVoldListener = null;
          }
          localParcel1.writeStrongBinder(paramIVoldListener);
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
      
      public void shutdown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
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
      
      public void unlockUserKey(int paramInt1, int paramInt2, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void unmount(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeString(paramString);
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
      
      public void unmountAppFuse(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVold");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
    }
  }
}
