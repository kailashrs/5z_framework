package android.app;

import android.app.backup.IBackupManager;
import android.app.backup.IBackupManager.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IBackupAgent
  extends IInterface
{
  public abstract void doBackup(ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2, ParcelFileDescriptor paramParcelFileDescriptor3, long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
    throws RemoteException;
  
  public abstract void doFullBackup(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
    throws RemoteException;
  
  public abstract void doMeasureFullBackup(long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
    throws RemoteException;
  
  public abstract void doQuotaExceeded(long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract void doRestore(ParcelFileDescriptor paramParcelFileDescriptor1, long paramLong, ParcelFileDescriptor paramParcelFileDescriptor2, int paramInt, IBackupManager paramIBackupManager)
    throws RemoteException;
  
  public abstract void doRestoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt1, String paramString1, String paramString2, long paramLong2, long paramLong3, int paramInt2, IBackupManager paramIBackupManager)
    throws RemoteException;
  
  public abstract void doRestoreFinished(int paramInt, IBackupManager paramIBackupManager)
    throws RemoteException;
  
  public abstract void fail(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBackupAgent
  {
    private static final String DESCRIPTOR = "android.app.IBackupAgent";
    static final int TRANSACTION_doBackup = 1;
    static final int TRANSACTION_doFullBackup = 3;
    static final int TRANSACTION_doMeasureFullBackup = 4;
    static final int TRANSACTION_doQuotaExceeded = 5;
    static final int TRANSACTION_doRestore = 2;
    static final int TRANSACTION_doRestoreFile = 6;
    static final int TRANSACTION_doRestoreFinished = 7;
    static final int TRANSACTION_fail = 8;
    
    public Stub()
    {
      attachInterface(this, "android.app.IBackupAgent");
    }
    
    public static IBackupAgent asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IBackupAgent");
      if ((localIInterface != null) && ((localIInterface instanceof IBackupAgent))) {
        return (IBackupAgent)localIInterface;
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
        ParcelFileDescriptor localParcelFileDescriptor1 = null;
        ParcelFileDescriptor localParcelFileDescriptor2 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.app.IBackupAgent");
          fail(paramParcel1.readString());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.IBackupAgent");
          doRestoreFinished(paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.IBackupAgent");
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject2) {
            break;
          }
          doRestoreFile(paramParcel2, paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.IBackupAgent");
          doQuotaExceeded(paramParcel1.readLong(), paramParcel1.readLong());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IBackupAgent");
          doMeasureFullBackup(paramParcel1.readLong(), paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IBackupAgent");
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject1) {
            break;
          }
          doFullBackup(paramParcel2, paramParcel1.readLong(), paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IBackupAgent");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          long l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            localParcelFileDescriptor1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          doRestore(paramParcel2, l, localParcelFileDescriptor1, paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.app.IBackupAgent");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localParcelFileDescriptor1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
        } else {
          localParcelFileDescriptor1 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localParcelFileDescriptor2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
        }
        for (;;)
        {
          break;
        }
        doBackup(paramParcel2, localParcelFileDescriptor1, localParcelFileDescriptor2, paramParcel1.readLong(), paramParcel1.readInt(), IBackupManager.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.app.IBackupAgent");
      return true;
    }
    
    private static class Proxy
      implements IBackupAgent
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
      
      public void doBackup(ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2, ParcelFileDescriptor paramParcelFileDescriptor3, long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IBackupAgent");
          if (paramParcelFileDescriptor1 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramParcelFileDescriptor2 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor2.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramParcelFileDescriptor3 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor3.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt1);
          if (paramIBackupManager != null) {
            paramParcelFileDescriptor1 = paramIBackupManager.asBinder();
          } else {
            paramParcelFileDescriptor1 = null;
          }
          localParcel.writeStrongBinder(paramParcelFileDescriptor1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void doFullBackup(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IBackupAgent");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt1);
          if (paramIBackupManager != null) {
            paramParcelFileDescriptor = paramIBackupManager.asBinder();
          } else {
            paramParcelFileDescriptor = null;
          }
          localParcel.writeStrongBinder(paramParcelFileDescriptor);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void doMeasureFullBackup(long paramLong, int paramInt1, IBackupManager paramIBackupManager, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IBackupAgent");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt1);
          if (paramIBackupManager != null) {
            paramIBackupManager = paramIBackupManager.asBinder();
          } else {
            paramIBackupManager = null;
          }
          localParcel.writeStrongBinder(paramIBackupManager);
          localParcel.writeInt(paramInt2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void doQuotaExceeded(long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IBackupAgent");
          localParcel.writeLong(paramLong1);
          localParcel.writeLong(paramLong2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void doRestore(ParcelFileDescriptor paramParcelFileDescriptor1, long paramLong, ParcelFileDescriptor paramParcelFileDescriptor2, int paramInt, IBackupManager paramIBackupManager)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IBackupAgent");
          if (paramParcelFileDescriptor1 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeLong(paramLong);
          if (paramParcelFileDescriptor2 != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor2.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramIBackupManager != null) {
            paramParcelFileDescriptor1 = paramIBackupManager.asBinder();
          } else {
            paramParcelFileDescriptor1 = null;
          }
          localParcel.writeStrongBinder(paramParcelFileDescriptor1);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void doRestoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt1, String paramString1, String paramString2, long paramLong2, long paramLong3, int paramInt2, IBackupManager paramIBackupManager)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 13
        //   5: aload 13
        //   7: ldc 34
        //   9: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   12: aload_1
        //   13: ifnull +19 -> 32
        //   16: aload 13
        //   18: iconst_1
        //   19: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   22: aload_1
        //   23: aload 13
        //   25: iconst_0
        //   26: invokevirtual 48	android/os/ParcelFileDescriptor:writeToParcel	(Landroid/os/Parcel;I)V
        //   29: goto +9 -> 38
        //   32: aload 13
        //   34: iconst_0
        //   35: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   38: aload 13
        //   40: lload_2
        //   41: invokevirtual 52	android/os/Parcel:writeLong	(J)V
        //   44: aload 13
        //   46: iload 4
        //   48: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   51: aload 13
        //   53: aload 5
        //   55: invokevirtual 82	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   58: aload 13
        //   60: aload 6
        //   62: invokevirtual 82	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   65: aload 13
        //   67: lload 7
        //   69: invokevirtual 52	android/os/Parcel:writeLong	(J)V
        //   72: aload 13
        //   74: lload 9
        //   76: invokevirtual 52	android/os/Parcel:writeLong	(J)V
        //   79: aload 13
        //   81: iload 11
        //   83: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   86: aload 12
        //   88: ifnull +14 -> 102
        //   91: aload 12
        //   93: invokeinterface 56 1 0
        //   98: astore_1
        //   99: goto +5 -> 104
        //   102: aconst_null
        //   103: astore_1
        //   104: aload 13
        //   106: aload_1
        //   107: invokevirtual 59	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   110: aload_0
        //   111: getfield 19	android/app/IBackupAgent$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   114: bipush 6
        //   116: aload 13
        //   118: aconst_null
        //   119: iconst_1
        //   120: invokeinterface 65 5 0
        //   125: pop
        //   126: aload 13
        //   128: invokevirtual 68	android/os/Parcel:recycle	()V
        //   131: return
        //   132: astore_1
        //   133: goto +28 -> 161
        //   136: astore_1
        //   137: goto +24 -> 161
        //   140: astore_1
        //   141: goto +20 -> 161
        //   144: astore_1
        //   145: goto +16 -> 161
        //   148: astore_1
        //   149: goto +12 -> 161
        //   152: astore_1
        //   153: goto +8 -> 161
        //   156: astore_1
        //   157: goto +4 -> 161
        //   160: astore_1
        //   161: aload 13
        //   163: invokevirtual 68	android/os/Parcel:recycle	()V
        //   166: aload_1
        //   167: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	168	0	this	Proxy
        //   0	168	1	paramParcelFileDescriptor	ParcelFileDescriptor
        //   0	168	2	paramLong1	long
        //   0	168	4	paramInt1	int
        //   0	168	5	paramString1	String
        //   0	168	6	paramString2	String
        //   0	168	7	paramLong2	long
        //   0	168	9	paramLong3	long
        //   0	168	11	paramInt2	int
        //   0	168	12	paramIBackupManager	IBackupManager
        //   3	159	13	localParcel	Parcel
        // Exception table:
        //   from	to	target	type
        //   110	126	132	finally
        //   79	86	136	finally
        //   91	99	136	finally
        //   104	110	136	finally
        //   72	79	140	finally
        //   65	72	144	finally
        //   58	65	148	finally
        //   51	58	152	finally
        //   44	51	156	finally
        //   5	12	160	finally
        //   16	29	160	finally
        //   32	38	160	finally
        //   38	44	160	finally
      }
      
      public void doRestoreFinished(int paramInt, IBackupManager paramIBackupManager)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IBackupAgent");
          localParcel.writeInt(paramInt);
          if (paramIBackupManager != null) {
            paramIBackupManager = paramIBackupManager.asBinder();
          } else {
            paramIBackupManager = null;
          }
          localParcel.writeStrongBinder(paramIBackupManager);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void fail(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IBackupAgent");
          localParcel.writeString(paramString);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IBackupAgent";
      }
    }
  }
}
