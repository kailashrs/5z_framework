package android.app.usage;

import android.app.PendingIntent;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IUsageStatsManager
  extends IInterface
{
  public abstract int getAppStandbyBucket(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getAppStandbyBuckets(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isAppInactive(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onCarrierPrivilegedAppsChanged()
    throws RemoteException;
  
  public abstract ParceledListSlice queryConfigurationStats(int paramInt, long paramLong1, long paramLong2, String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice queryEventStats(int paramInt, long paramLong1, long paramLong2, String paramString)
    throws RemoteException;
  
  public abstract UsageEvents queryEvents(long paramLong1, long paramLong2, String paramString)
    throws RemoteException;
  
  public abstract UsageEvents queryEventsAsUser(long paramLong1, long paramLong2, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract UsageEvents queryEventsForPackage(long paramLong1, long paramLong2, String paramString)
    throws RemoteException;
  
  public abstract UsageEvents queryEventsForPackageForUser(long paramLong1, long paramLong2, int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract UsageEvents queryEventsForUser(long paramLong1, long paramLong2, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice queryUsageStats(int paramInt, long paramLong1, long paramLong2, String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice queryUsageStatsAsUser(int paramInt1, long paramLong1, long paramLong2, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void registerAppUsageObserver(int paramInt, String[] paramArrayOfString, long paramLong, PendingIntent paramPendingIntent, String paramString)
    throws RemoteException;
  
  public abstract void reportChooserSelection(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString, String paramString3)
    throws RemoteException;
  
  public abstract void setAppInactive(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setAppStandbyBucket(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setAppStandbyBuckets(ParceledListSlice paramParceledListSlice, int paramInt)
    throws RemoteException;
  
  public abstract void unregisterAppUsageObserver(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void whitelistAppTemporarily(String paramString, long paramLong, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUsageStatsManager
  {
    private static final String DESCRIPTOR = "android.app.usage.IUsageStatsManager";
    static final int TRANSACTION_getAppStandbyBucket = 15;
    static final int TRANSACTION_getAppStandbyBuckets = 17;
    static final int TRANSACTION_isAppInactive = 11;
    static final int TRANSACTION_onCarrierPrivilegedAppsChanged = 13;
    static final int TRANSACTION_queryConfigurationStats = 3;
    static final int TRANSACTION_queryEventStats = 4;
    static final int TRANSACTION_queryEvents = 5;
    static final int TRANSACTION_queryEventsAsUser = 9;
    static final int TRANSACTION_queryEventsForPackage = 6;
    static final int TRANSACTION_queryEventsForPackageForUser = 8;
    static final int TRANSACTION_queryEventsForUser = 7;
    static final int TRANSACTION_queryUsageStats = 1;
    static final int TRANSACTION_queryUsageStatsAsUser = 2;
    static final int TRANSACTION_registerAppUsageObserver = 19;
    static final int TRANSACTION_reportChooserSelection = 14;
    static final int TRANSACTION_setAppInactive = 10;
    static final int TRANSACTION_setAppStandbyBucket = 16;
    static final int TRANSACTION_setAppStandbyBuckets = 18;
    static final int TRANSACTION_unregisterAppUsageObserver = 20;
    static final int TRANSACTION_whitelistAppTemporarily = 12;
    
    public Stub()
    {
      attachInterface(this, "android.app.usage.IUsageStatsManager");
    }
    
    public static IUsageStatsManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.usage.IUsageStatsManager");
      if ((localIInterface != null) && ((localIInterface instanceof IUsageStatsManager))) {
        return (IUsageStatsManager)localIInterface;
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
        String[] arrayOfString = null;
        Object localObject = null;
        boolean bool = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 20: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          unregisterAppUsageObserver(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramInt1 = paramParcel1.readInt();
          arrayOfString = paramParcel1.createStringArray();
          long l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            localObject = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          registerAppUsageObserver(paramInt1, arrayOfString, l, (PendingIntent)localObject, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          if (paramParcel1.readInt() != 0) {
            localObject = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = arrayOfString;
          }
          setAppStandbyBuckets((ParceledListSlice)localObject, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = getAppStandbyBuckets(paramParcel1.readString(), paramParcel1.readInt());
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
        case 16: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          setAppStandbyBucket(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramInt1 = getAppStandbyBucket(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          reportChooserSelection(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createStringArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          onCarrierPrivilegedAppsChanged();
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          whitelistAppTemporarily(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramInt1 = isAppInactive(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          localObject = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setAppInactive((String)localObject, bool, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryEventsAsUser(paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString(), paramParcel1.readInt());
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
        case 8: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryEventsForPackageForUser(paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
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
        case 7: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryEventsForUser(paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readString());
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
        case 6: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryEventsForPackage(paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString());
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
        case 5: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryEvents(paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString());
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
        case 4: 
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryEventStats(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString());
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
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryConfigurationStats(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString());
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
          paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
          paramParcel1 = queryUsageStatsAsUser(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString(), paramParcel1.readInt());
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
        paramParcel1.enforceInterface("android.app.usage.IUsageStatsManager");
        paramParcel1 = queryUsageStats(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readString());
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
      paramParcel2.writeString("android.app.usage.IUsageStatsManager");
      return true;
    }
    
    private static class Proxy
      implements IUsageStatsManager
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
      
      public int getAppStandbyBucket(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getAppStandbyBuckets(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.app.usage.IUsageStatsManager";
      }
      
      public boolean isAppInactive(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(11, localParcel1, localParcel2, 0);
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
      
      public void onCarrierPrivilegedAppsChanged()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
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
      
      public ParceledListSlice queryConfigurationStats(int paramInt, long paramLong1, long paramLong2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public ParceledListSlice queryEventStats(int paramInt, long paramLong1, long paramLong2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public UsageEvents queryEvents(long paramLong1, long paramLong2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UsageEvents)UsageEvents.CREATOR.createFromParcel(localParcel2);
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
      
      public UsageEvents queryEventsAsUser(long paramLong1, long paramLong2, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UsageEvents)UsageEvents.CREATOR.createFromParcel(localParcel2);
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
      
      public UsageEvents queryEventsForPackage(long paramLong1, long paramLong2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UsageEvents)UsageEvents.CREATOR.createFromParcel(localParcel2);
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
      
      public UsageEvents queryEventsForPackageForUser(long paramLong1, long paramLong2, int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (UsageEvents)UsageEvents.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public UsageEvents queryEventsForUser(long paramLong1, long paramLong2, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UsageEvents)UsageEvents.CREATOR.createFromParcel(localParcel2);
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
      
      public ParceledListSlice queryUsageStats(int paramInt, long paramLong1, long paramLong2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public ParceledListSlice queryUsageStatsAsUser(int paramInt1, long paramLong1, long paramLong2, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public void registerAppUsageObserver(int paramInt, String[] paramArrayOfString, long paramLong, PendingIntent paramPendingIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeLong(paramLong);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void reportChooserSelection(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeString(paramString3);
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
      
      public void setAppInactive(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
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
      
      public void setAppStandbyBucket(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void setAppStandbyBuckets(ParceledListSlice paramParceledListSlice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public void unregisterAppUsageObserver(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeInt(paramInt);
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
      
      public void whitelistAppTemporarily(String paramString, long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.usage.IUsageStatsManager");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
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
