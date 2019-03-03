package com.android.internal.backup;

import android.app.backup.IBackupManager;
import android.app.backup.IBackupManager.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IObbBackupService
  extends IInterface
{
  public abstract void backupObbs(String paramString, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt, IBackupManager paramIBackupManager)
    throws RemoteException;
  
  public abstract void restoreObbFile(String paramString1, ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt1, String paramString2, long paramLong2, long paramLong3, int paramInt2, IBackupManager paramIBackupManager)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IObbBackupService
  {
    private static final String DESCRIPTOR = "com.android.internal.backup.IObbBackupService";
    static final int TRANSACTION_backupObbs = 1;
    static final int TRANSACTION_restoreObbFile = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.backup.IObbBackupService");
    }
    
    public static IObbBackupService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.backup.IObbBackupService");
      if ((localIInterface != null) && ((localIInterface instanceof IObbBackupService))) {
        return (IObbBackupService)localIInterface;
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
        String str1 = null;
        String str2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.backup.IObbBackupService");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = str2) {
            break;
          }
          restoreObbFile(str1, paramParcel2, paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.backup.IObbBackupService");
        str2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = str1;
        }
        backupObbs(str2, paramParcel2, paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("com.android.internal.backup.IObbBackupService");
      return true;
    }
    
    private static class Proxy
      implements IObbBackupService
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
      
      public void backupObbs(String paramString, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt, IBackupManager paramIBackupManager)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.backup.IObbBackupService");
          localParcel.writeString(paramString);
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramIBackupManager != null) {
            paramString = paramIBackupManager.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.backup.IObbBackupService";
      }
      
      /* Error */
      public void restoreObbFile(String paramString1, ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt1, String paramString2, long paramLong2, long paramLong3, int paramInt2, IBackupManager paramIBackupManager)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 13
        //   5: aload 13
        //   7: ldc 34
        //   9: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload 13
        //   14: aload_1
        //   15: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   18: aload_2
        //   19: ifnull +19 -> 38
        //   22: aload 13
        //   24: iconst_1
        //   25: invokevirtual 45	android/os/Parcel:writeInt	(I)V
        //   28: aload_2
        //   29: aload 13
        //   31: iconst_0
        //   32: invokevirtual 51	android/os/ParcelFileDescriptor:writeToParcel	(Landroid/os/Parcel;I)V
        //   35: goto +9 -> 44
        //   38: aload 13
        //   40: iconst_0
        //   41: invokevirtual 45	android/os/Parcel:writeInt	(I)V
        //   44: aload 13
        //   46: lload_3
        //   47: invokevirtual 76	android/os/Parcel:writeLong	(J)V
        //   50: aload 13
        //   52: iload 5
        //   54: invokevirtual 45	android/os/Parcel:writeInt	(I)V
        //   57: aload 13
        //   59: aload 6
        //   61: invokevirtual 41	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   64: aload 13
        //   66: lload 7
        //   68: invokevirtual 76	android/os/Parcel:writeLong	(J)V
        //   71: aload 13
        //   73: lload 9
        //   75: invokevirtual 76	android/os/Parcel:writeLong	(J)V
        //   78: aload 13
        //   80: iload 11
        //   82: invokevirtual 45	android/os/Parcel:writeInt	(I)V
        //   85: aload 12
        //   87: ifnull +14 -> 101
        //   90: aload 12
        //   92: invokeinterface 55 1 0
        //   97: astore_1
        //   98: goto +5 -> 103
        //   101: aconst_null
        //   102: astore_1
        //   103: aload 13
        //   105: aload_1
        //   106: invokevirtual 58	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   109: aload_0
        //   110: getfield 19	com/android/internal/backup/IObbBackupService$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   113: iconst_2
        //   114: aload 13
        //   116: aconst_null
        //   117: iconst_1
        //   118: invokeinterface 64 5 0
        //   123: pop
        //   124: aload 13
        //   126: invokevirtual 67	android/os/Parcel:recycle	()V
        //   129: return
        //   130: astore_1
        //   131: goto +28 -> 159
        //   134: astore_1
        //   135: goto +24 -> 159
        //   138: astore_1
        //   139: goto +20 -> 159
        //   142: astore_1
        //   143: goto +16 -> 159
        //   146: astore_1
        //   147: goto +12 -> 159
        //   150: astore_1
        //   151: goto +8 -> 159
        //   154: astore_1
        //   155: goto +4 -> 159
        //   158: astore_1
        //   159: aload 13
        //   161: invokevirtual 67	android/os/Parcel:recycle	()V
        //   164: aload_1
        //   165: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	166	0	this	Proxy
        //   0	166	1	paramString1	String
        //   0	166	2	paramParcelFileDescriptor	ParcelFileDescriptor
        //   0	166	3	paramLong1	long
        //   0	166	5	paramInt1	int
        //   0	166	6	paramString2	String
        //   0	166	7	paramLong2	long
        //   0	166	9	paramLong3	long
        //   0	166	11	paramInt2	int
        //   0	166	12	paramIBackupManager	IBackupManager
        //   3	157	13	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   109	124	130	finally
        //   78	85	134	finally
        //   90	98	134	finally
        //   103	109	134	finally
        //   71	78	138	finally
        //   64	71	142	finally
        //   57	64	146	finally
        //   50	57	150	finally
        //   44	50	154	finally
        //   5	18	158	finally
        //   22	35	158	finally
        //   38	44	158	finally
      }
    }
  }
}
