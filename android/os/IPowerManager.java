package android.os;

import java.util.ArrayList;
import java.util.List;

public abstract interface IPowerManager
  extends IInterface
{
  public abstract void acquireWakeLock(IBinder paramIBinder, int paramInt, String paramString1, String paramString2, WorkSource paramWorkSource, String paramString3)
    throws RemoteException;
  
  public abstract void acquireWakeLockWithUid(IBinder paramIBinder, int paramInt1, String paramString1, String paramString2, int paramInt2)
    throws RemoteException;
  
  public abstract void boostScreenBrightness(long paramLong)
    throws RemoteException;
  
  public abstract void crash(String paramString)
    throws RemoteException;
  
  public abstract void forceReleaseSuspiciousWakelocks(String paramString, long paramLong)
    throws RemoteException;
  
  public abstract int getLastShutdownReason()
    throws RemoteException;
  
  public abstract PowerSaveState getPowerSaveState(int paramInt)
    throws RemoteException;
  
  public abstract List<String> getSuspiciousWakelocks(long paramLong)
    throws RemoteException;
  
  public abstract void goToSleep(long paramLong, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean isDeviceIdleMode()
    throws RemoteException;
  
  public abstract boolean isInteractive()
    throws RemoteException;
  
  public abstract boolean isLightDeviceIdleMode()
    throws RemoteException;
  
  public abstract boolean isPowerSaveMode()
    throws RemoteException;
  
  public abstract boolean isScreenBrightnessBoosted()
    throws RemoteException;
  
  public abstract boolean isWakeLockLevelSupported(int paramInt)
    throws RemoteException;
  
  public abstract void nap(long paramLong)
    throws RemoteException;
  
  public abstract void powerHint(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void reboot(boolean paramBoolean1, String paramString, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void rebootSafeMode(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void releaseWakeLock(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void setAttentionLight(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setDozeAfterScreenOff(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setPowerSaveMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setStayOnSetting(int paramInt)
    throws RemoteException;
  
  public abstract void shutdown(boolean paramBoolean1, String paramString, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void updateWakeLockUids(IBinder paramIBinder, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void updateWakeLockWorkSource(IBinder paramIBinder, WorkSource paramWorkSource, String paramString)
    throws RemoteException;
  
  public abstract void userActivity(long paramLong, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void wakeUp(long paramLong, String paramString1, String paramString2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPowerManager
  {
    private static final String DESCRIPTOR = "android.os.IPowerManager";
    static final int TRANSACTION_acquireWakeLock = 1;
    static final int TRANSACTION_acquireWakeLockWithUid = 2;
    static final int TRANSACTION_boostScreenBrightness = 24;
    static final int TRANSACTION_crash = 21;
    static final int TRANSACTION_forceReleaseSuspiciousWakelocks = 27;
    static final int TRANSACTION_getLastShutdownReason = 22;
    static final int TRANSACTION_getPowerSaveState = 14;
    static final int TRANSACTION_getSuspiciousWakelocks = 26;
    static final int TRANSACTION_goToSleep = 10;
    static final int TRANSACTION_isDeviceIdleMode = 16;
    static final int TRANSACTION_isInteractive = 12;
    static final int TRANSACTION_isLightDeviceIdleMode = 17;
    static final int TRANSACTION_isPowerSaveMode = 13;
    static final int TRANSACTION_isScreenBrightnessBoosted = 25;
    static final int TRANSACTION_isWakeLockLevelSupported = 7;
    static final int TRANSACTION_nap = 11;
    static final int TRANSACTION_powerHint = 5;
    static final int TRANSACTION_reboot = 18;
    static final int TRANSACTION_rebootSafeMode = 19;
    static final int TRANSACTION_releaseWakeLock = 3;
    static final int TRANSACTION_setAttentionLight = 28;
    static final int TRANSACTION_setDozeAfterScreenOff = 29;
    static final int TRANSACTION_setPowerSaveMode = 15;
    static final int TRANSACTION_setStayOnSetting = 23;
    static final int TRANSACTION_shutdown = 20;
    static final int TRANSACTION_updateWakeLockUids = 4;
    static final int TRANSACTION_updateWakeLockWorkSource = 6;
    static final int TRANSACTION_userActivity = 8;
    static final int TRANSACTION_wakeUp = 9;
    
    public Stub()
    {
      attachInterface(this, "android.os.IPowerManager");
    }
    
    public static IPowerManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IPowerManager");
      if ((localIInterface != null) && ((localIInterface instanceof IPowerManager))) {
        return (IPowerManager)localIInterface;
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
        Object localObject = null;
        String str1 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 29: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          setDozeAfterScreenOff(bool6);
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          bool6 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          setAttentionLight(bool6, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          forceReleaseSuspiciousWakelocks(paramParcel1.readString(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramParcel1 = getSuspiciousWakelocks(paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramInt1 = isScreenBrightnessBoosted();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          boostScreenBrightness(paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          setStayOnSetting(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramInt1 = getLastShutdownReason();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          crash(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          localObject = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          shutdown(bool6, (String)localObject, bool2);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          bool2 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          rebootSafeMode(bool6, bool2);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          localObject = paramParcel1.readString();
          bool2 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          reboot(bool6, (String)localObject, bool2);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramInt1 = isLightDeviceIdleMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramInt1 = isDeviceIdleMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          bool6 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          paramInt1 = setPowerSaveMode(bool6);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramParcel1 = getPowerSaveState(paramParcel1.readInt());
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
        case 13: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramInt1 = isPowerSaveMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramInt1 = isInteractive();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          nap(paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          goToSleep(paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          wakeUp(paramParcel1.readLong(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          userActivity(paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          paramInt1 = isWakeLockLevelSupported(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          localIBinder = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = str1;
          }
          updateWakeLockWorkSource(localIBinder, (WorkSource)localObject, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          powerHint(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          updateWakeLockUids(paramParcel1.readStrongBinder(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          releaseWakeLock(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IPowerManager");
          acquireWakeLockWithUid(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.IPowerManager");
        IBinder localIBinder = paramParcel1.readStrongBinder();
        paramInt1 = paramParcel1.readInt();
        str1 = paramParcel1.readString();
        String str2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
        }
        for (;;)
        {
          break;
        }
        acquireWakeLock(localIBinder, paramInt1, str1, str2, (WorkSource)localObject, paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IPowerManager");
      return true;
    }
    
    private static class Proxy
      implements IPowerManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void acquireWakeLock(IBinder paramIBinder, int paramInt, String paramString1, String paramString2, WorkSource paramWorkSource, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString3);
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
      
      public void acquireWakeLockWithUid(IBinder paramIBinder, int paramInt1, String paramString1, String paramString2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void boostScreenBrightness(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeLong(paramLong);
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
      
      public void crash(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
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
      
      public void forceReleaseSuspiciousWakelocks(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IPowerManager";
      }
      
      public int getLastShutdownReason()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
      
      public PowerSaveState getPowerSaveState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PowerSaveState localPowerSaveState;
          if (localParcel2.readInt() != 0) {
            localPowerSaveState = (PowerSaveState)PowerSaveState.CREATOR.createFromParcel(localParcel2);
          } else {
            localPowerSaveState = null;
          }
          return localPowerSaveState;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getSuspiciousWakelocks(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeLong(paramLong);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createStringArrayList();
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void goToSleep(long paramLong, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeLong(paramLong);
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
      
      public boolean isDeviceIdleMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(16, localParcel1, localParcel2, 0);
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
      
      public boolean isInteractive()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
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
      
      public boolean isLightDeviceIdleMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
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
      
      public boolean isPowerSaveMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(13, localParcel1, localParcel2, 0);
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
      
      public boolean isScreenBrightnessBoosted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(25, localParcel1, localParcel2, 0);
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
      
      public boolean isWakeLockLevelSupported(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
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
      
      public void nap(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeLong(paramLong);
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
      
      public void powerHint(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IPowerManager");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void reboot(boolean paramBoolean1, String paramString, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void rebootSafeMode(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void releaseWakeLock(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
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
      
      public void setAttentionLight(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setDozeAfterScreenOff(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public boolean setPowerSaveMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public void setStayOnSetting(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
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
      
      public void shutdown(boolean paramBoolean1, String paramString, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void updateWakeLockUids(IBinder paramIBinder, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeIntArray(paramArrayOfInt);
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
      
      public void updateWakeLockWorkSource(IBinder paramIBinder, WorkSource paramWorkSource, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void userActivity(long paramLong, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void wakeUp(long paramLong, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPowerManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
    }
  }
}
