package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;

public abstract interface IAlarmManager
  extends IInterface
{
  public abstract long currentNetworkTimeMillis()
    throws RemoteException;
  
  public abstract AlarmManager.AlarmClockInfo getNextAlarmClock(int paramInt)
    throws RemoteException;
  
  public abstract long getNextWakeFromIdleTime()
    throws RemoteException;
  
  public abstract void remove(PendingIntent paramPendingIntent, IAlarmListener paramIAlarmListener)
    throws RemoteException;
  
  public abstract void set(String paramString1, int paramInt1, long paramLong1, long paramLong2, long paramLong3, int paramInt2, PendingIntent paramPendingIntent, IAlarmListener paramIAlarmListener, String paramString2, WorkSource paramWorkSource, AlarmManager.AlarmClockInfo paramAlarmClockInfo)
    throws RemoteException;
  
  public abstract boolean setTime(long paramLong)
    throws RemoteException;
  
  public abstract void setTimeZone(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAlarmManager
  {
    private static final String DESCRIPTOR = "android.app.IAlarmManager";
    static final int TRANSACTION_currentNetworkTimeMillis = 7;
    static final int TRANSACTION_getNextAlarmClock = 6;
    static final int TRANSACTION_getNextWakeFromIdleTime = 5;
    static final int TRANSACTION_remove = 4;
    static final int TRANSACTION_set = 1;
    static final int TRANSACTION_setTime = 2;
    static final int TRANSACTION_setTimeZone = 3;
    
    public Stub()
    {
      attachInterface(this, "android.app.IAlarmManager");
    }
    
    public static IAlarmManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IAlarmManager");
      if ((localIInterface != null) && ((localIInterface instanceof IAlarmManager))) {
        return (IAlarmManager)localIInterface;
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
        PendingIntent localPendingIntent = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.app.IAlarmManager");
          l1 = currentNetworkTimeMillis();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.IAlarmManager");
          paramParcel1 = getNextAlarmClock(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.app.IAlarmManager");
          l1 = getNextWakeFromIdleTime();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IAlarmManager");
          if (paramParcel1.readInt() != 0) {
            localPendingIntent = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          }
          remove(localPendingIntent, IAlarmListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IAlarmManager");
          setTimeZone(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IAlarmManager");
          paramInt1 = setTime(paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.IAlarmManager");
        String str1 = paramParcel1.readString();
        paramInt2 = paramParcel1.readInt();
        long l2 = paramParcel1.readLong();
        long l1 = paramParcel1.readLong();
        long l3 = paramParcel1.readLong();
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          localPendingIntent = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
        } else {
          localPendingIntent = null;
        }
        IAlarmListener localIAlarmListener = IAlarmListener.Stub.asInterface(paramParcel1.readStrongBinder());
        String str2 = paramParcel1.readString();
        WorkSource localWorkSource;
        if (paramParcel1.readInt() != 0) {
          localWorkSource = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
        } else {
          localWorkSource = null;
        }
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel1 = (AlarmManager.AlarmClockInfo)AlarmManager.AlarmClockInfo.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject) {
          break;
        }
        set(str1, paramInt2, l2, l1, l3, paramInt1, localPendingIntent, localIAlarmListener, str2, localWorkSource, paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.IAlarmManager");
      return true;
    }
    
    private static class Proxy
      implements IAlarmManager
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
      
      public long currentNetworkTimeMillis()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAlarmManager");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IAlarmManager";
      }
      
      public AlarmManager.AlarmClockInfo getNextAlarmClock(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAlarmManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          AlarmManager.AlarmClockInfo localAlarmClockInfo;
          if (localParcel2.readInt() != 0) {
            localAlarmClockInfo = (AlarmManager.AlarmClockInfo)AlarmManager.AlarmClockInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localAlarmClockInfo = null;
          }
          return localAlarmClockInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getNextWakeFromIdleTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAlarmManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public void remove(PendingIntent paramPendingIntent, IAlarmListener paramIAlarmListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAlarmManager");
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIAlarmListener != null) {
            paramPendingIntent = paramIAlarmListener.asBinder();
          } else {
            paramPendingIntent = null;
          }
          localParcel1.writeStrongBinder(paramPendingIntent);
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
      
      /* Error */
      public void set(String paramString1, int paramInt1, long paramLong1, long paramLong2, long paramLong3, int paramInt2, PendingIntent paramPendingIntent, IAlarmListener paramIAlarmListener, String paramString2, WorkSource paramWorkSource, AlarmManager.AlarmClockInfo paramAlarmClockInfo)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 15
        //   5: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 16
        //   10: aload 15
        //   12: ldc 34
        //   14: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 15
        //   19: aload_1
        //   20: invokevirtual 99	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   23: aload 15
        //   25: iload_2
        //   26: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   29: aload 15
        //   31: lload_3
        //   32: invokevirtual 103	android/os/Parcel:writeLong	(J)V
        //   35: aload 15
        //   37: lload 5
        //   39: invokevirtual 103	android/os/Parcel:writeLong	(J)V
        //   42: aload 15
        //   44: lload 7
        //   46: invokevirtual 103	android/os/Parcel:writeLong	(J)V
        //   49: aload 15
        //   51: iload 9
        //   53: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   56: aload 10
        //   58: ifnull +20 -> 78
        //   61: aload 15
        //   63: iconst_1
        //   64: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   67: aload 10
        //   69: aload 15
        //   71: iconst_0
        //   72: invokevirtual 87	android/app/PendingIntent:writeToParcel	(Landroid/os/Parcel;I)V
        //   75: goto +9 -> 84
        //   78: aload 15
        //   80: iconst_0
        //   81: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   84: aload 11
        //   86: ifnull +14 -> 100
        //   89: aload 11
        //   91: invokeinterface 91 1 0
        //   96: astore_1
        //   97: goto +5 -> 102
        //   100: aconst_null
        //   101: astore_1
        //   102: aload 15
        //   104: aload_1
        //   105: invokevirtual 94	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   108: aload 15
        //   110: aload 12
        //   112: invokevirtual 99	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   115: aload 13
        //   117: ifnull +20 -> 137
        //   120: aload 15
        //   122: iconst_1
        //   123: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   126: aload 13
        //   128: aload 15
        //   130: iconst_0
        //   131: invokevirtual 106	android/os/WorkSource:writeToParcel	(Landroid/os/Parcel;I)V
        //   134: goto +9 -> 143
        //   137: aload 15
        //   139: iconst_0
        //   140: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   143: aload 14
        //   145: ifnull +20 -> 165
        //   148: aload 15
        //   150: iconst_1
        //   151: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   154: aload 14
        //   156: aload 15
        //   158: iconst_0
        //   159: invokevirtual 107	android/app/AlarmManager$AlarmClockInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   162: goto +9 -> 171
        //   165: aload 15
        //   167: iconst_0
        //   168: invokevirtual 62	android/os/Parcel:writeInt	(I)V
        //   171: aload_0
        //   172: getfield 19	android/app/IAlarmManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   175: iconst_1
        //   176: aload 15
        //   178: aload 16
        //   180: iconst_0
        //   181: invokeinterface 44 5 0
        //   186: pop
        //   187: aload 16
        //   189: invokevirtual 47	android/os/Parcel:readException	()V
        //   192: aload 16
        //   194: invokevirtual 53	android/os/Parcel:recycle	()V
        //   197: aload 15
        //   199: invokevirtual 53	android/os/Parcel:recycle	()V
        //   202: return
        //   203: astore_1
        //   204: goto +28 -> 232
        //   207: astore_1
        //   208: goto +24 -> 232
        //   211: astore_1
        //   212: goto +20 -> 232
        //   215: astore_1
        //   216: goto +16 -> 232
        //   219: astore_1
        //   220: goto +12 -> 232
        //   223: astore_1
        //   224: goto +8 -> 232
        //   227: astore_1
        //   228: goto +4 -> 232
        //   231: astore_1
        //   232: aload 16
        //   234: invokevirtual 53	android/os/Parcel:recycle	()V
        //   237: aload 15
        //   239: invokevirtual 53	android/os/Parcel:recycle	()V
        //   242: aload_1
        //   243: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	244	0	this	Proxy
        //   0	244	1	paramString1	String
        //   0	244	2	paramInt1	int
        //   0	244	3	paramLong1	long
        //   0	244	5	paramLong2	long
        //   0	244	7	paramLong3	long
        //   0	244	9	paramInt2	int
        //   0	244	10	paramPendingIntent	PendingIntent
        //   0	244	11	paramIAlarmListener	IAlarmListener
        //   0	244	12	paramString2	String
        //   0	244	13	paramWorkSource	WorkSource
        //   0	244	14	paramAlarmClockInfo	AlarmManager.AlarmClockInfo
        //   3	235	15	localParcel1	Parcel
        //   8	225	16	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   108	115	203	finally
        //   120	134	203	finally
        //   137	143	203	finally
        //   148	162	203	finally
        //   165	171	203	finally
        //   171	192	203	finally
        //   49	56	207	finally
        //   61	75	207	finally
        //   78	84	207	finally
        //   89	97	207	finally
        //   102	108	207	finally
        //   42	49	211	finally
        //   35	42	215	finally
        //   29	35	219	finally
        //   23	29	223	finally
        //   17	23	227	finally
        //   10	17	231	finally
      }
      
      public boolean setTime(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAlarmManager");
          localParcel1.writeLong(paramLong);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
      
      public void setTimeZone(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAlarmManager");
          localParcel1.writeString(paramString);
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
    }
  }
}
