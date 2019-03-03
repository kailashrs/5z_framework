package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IContextHubService
  extends IInterface
{
  public abstract IContextHubClient createClient(IContextHubClientCallback paramIContextHubClientCallback, int paramInt)
    throws RemoteException;
  
  public abstract void disableNanoApp(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, long paramLong)
    throws RemoteException;
  
  public abstract void enableNanoApp(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, long paramLong)
    throws RemoteException;
  
  public abstract int[] findNanoAppOnHub(int paramInt, NanoAppFilter paramNanoAppFilter)
    throws RemoteException;
  
  public abstract int[] getContextHubHandles()
    throws RemoteException;
  
  public abstract ContextHubInfo getContextHubInfo(int paramInt)
    throws RemoteException;
  
  public abstract List<ContextHubInfo> getContextHubs()
    throws RemoteException;
  
  public abstract NanoAppInstanceInfo getNanoAppInstanceInfo(int paramInt)
    throws RemoteException;
  
  public abstract int loadNanoApp(int paramInt, NanoApp paramNanoApp)
    throws RemoteException;
  
  public abstract void loadNanoAppOnHub(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, NanoAppBinary paramNanoAppBinary)
    throws RemoteException;
  
  public abstract void queryNanoApps(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback)
    throws RemoteException;
  
  public abstract int registerCallback(IContextHubCallback paramIContextHubCallback)
    throws RemoteException;
  
  public abstract int sendMessage(int paramInt1, int paramInt2, ContextHubMessage paramContextHubMessage)
    throws RemoteException;
  
  public abstract int unloadNanoApp(int paramInt)
    throws RemoteException;
  
  public abstract void unloadNanoAppFromHub(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IContextHubService
  {
    private static final String DESCRIPTOR = "android.hardware.location.IContextHubService";
    static final int TRANSACTION_createClient = 9;
    static final int TRANSACTION_disableNanoApp = 14;
    static final int TRANSACTION_enableNanoApp = 13;
    static final int TRANSACTION_findNanoAppOnHub = 7;
    static final int TRANSACTION_getContextHubHandles = 2;
    static final int TRANSACTION_getContextHubInfo = 3;
    static final int TRANSACTION_getContextHubs = 10;
    static final int TRANSACTION_getNanoAppInstanceInfo = 6;
    static final int TRANSACTION_loadNanoApp = 4;
    static final int TRANSACTION_loadNanoAppOnHub = 11;
    static final int TRANSACTION_queryNanoApps = 15;
    static final int TRANSACTION_registerCallback = 1;
    static final int TRANSACTION_sendMessage = 8;
    static final int TRANSACTION_unloadNanoApp = 5;
    static final int TRANSACTION_unloadNanoAppFromHub = 12;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IContextHubService");
    }
    
    public static IContextHubService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IContextHubService");
      if ((localIInterface != null) && ((localIInterface instanceof IContextHubService))) {
        return (IContextHubService)localIInterface;
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
        IContextHubTransactionCallback localIContextHubTransactionCallback = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        IContextHubClient localIContextHubClient = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 15: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          queryNanoApps(paramParcel1.readInt(), IContextHubTransactionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          disableNanoApp(paramParcel1.readInt(), IContextHubTransactionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          enableNanoApp(paramParcel1.readInt(), IContextHubTransactionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          unloadNanoAppFromHub(paramParcel1.readInt(), IContextHubTransactionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramInt1 = paramParcel1.readInt();
          localIContextHubTransactionCallback = IContextHubTransactionCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NanoAppBinary)NanoAppBinary.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIContextHubClient;
          }
          loadNanoAppOnHub(paramInt1, localIContextHubTransactionCallback, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramParcel1 = getContextHubs();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          localIContextHubClient = createClient(IContextHubClientCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localIContextHubTransactionCallback;
          if (localIContextHubClient != null) {
            paramParcel1 = localIContextHubClient.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ContextHubMessage)ContextHubMessage.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = sendMessage(paramInt2, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NanoAppFilter)NanoAppFilter.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramParcel1 = findNanoAppOnHub(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramParcel1 = getNanoAppInstanceInfo(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramInt1 = unloadNanoApp(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NanoApp)NanoApp.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = loadNanoApp(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramParcel1 = getContextHubInfo(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
          paramParcel1 = getContextHubHandles();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.location.IContextHubService");
        paramInt1 = registerCallback(IContextHubCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.hardware.location.IContextHubService");
      return true;
    }
    
    private static class Proxy
      implements IContextHubService
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
      
      public IContextHubClient createClient(IContextHubClientCallback paramIContextHubClientCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          if (paramIContextHubClientCallback != null) {
            paramIContextHubClientCallback = paramIContextHubClientCallback.asBinder();
          } else {
            paramIContextHubClientCallback = null;
          }
          localParcel1.writeStrongBinder(paramIContextHubClientCallback);
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIContextHubClientCallback = IContextHubClient.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIContextHubClientCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableNanoApp(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          if (paramIContextHubTransactionCallback != null) {
            paramIContextHubTransactionCallback = paramIContextHubTransactionCallback.asBinder();
          } else {
            paramIContextHubTransactionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIContextHubTransactionCallback);
          localParcel1.writeLong(paramLong);
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
      
      public void enableNanoApp(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          if (paramIContextHubTransactionCallback != null) {
            paramIContextHubTransactionCallback = paramIContextHubTransactionCallback.asBinder();
          } else {
            paramIContextHubTransactionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIContextHubTransactionCallback);
          localParcel1.writeLong(paramLong);
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
      
      public int[] findNanoAppOnHub(int paramInt, NanoAppFilter paramNanoAppFilter)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          if (paramNanoAppFilter != null)
          {
            localParcel1.writeInt(1);
            paramNanoAppFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramNanoAppFilter = localParcel2.createIntArray();
          return paramNanoAppFilter;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getContextHubHandles()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ContextHubInfo getContextHubInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ContextHubInfo localContextHubInfo;
          if (localParcel2.readInt() != 0) {
            localContextHubInfo = (ContextHubInfo)ContextHubInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localContextHubInfo = null;
          }
          return localContextHubInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ContextHubInfo> getContextHubs()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ContextHubInfo.CREATOR);
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
        return "android.hardware.location.IContextHubService";
      }
      
      public NanoAppInstanceInfo getNanoAppInstanceInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NanoAppInstanceInfo localNanoAppInstanceInfo;
          if (localParcel2.readInt() != 0) {
            localNanoAppInstanceInfo = (NanoAppInstanceInfo)NanoAppInstanceInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localNanoAppInstanceInfo = null;
          }
          return localNanoAppInstanceInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int loadNanoApp(int paramInt, NanoApp paramNanoApp)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          if (paramNanoApp != null)
          {
            localParcel1.writeInt(1);
            paramNanoApp.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public void loadNanoAppOnHub(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, NanoAppBinary paramNanoAppBinary)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          if (paramIContextHubTransactionCallback != null) {
            paramIContextHubTransactionCallback = paramIContextHubTransactionCallback.asBinder();
          } else {
            paramIContextHubTransactionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIContextHubTransactionCallback);
          if (paramNanoAppBinary != null)
          {
            localParcel1.writeInt(1);
            paramNanoAppBinary.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void queryNanoApps(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          if (paramIContextHubTransactionCallback != null) {
            paramIContextHubTransactionCallback = paramIContextHubTransactionCallback.asBinder();
          } else {
            paramIContextHubTransactionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIContextHubTransactionCallback);
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
      
      public int registerCallback(IContextHubCallback paramIContextHubCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          if (paramIContextHubCallback != null) {
            paramIContextHubCallback = paramIContextHubCallback.asBinder();
          } else {
            paramIContextHubCallback = null;
          }
          localParcel1.writeStrongBinder(paramIContextHubCallback);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public int sendMessage(int paramInt1, int paramInt2, ContextHubMessage paramContextHubMessage)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramContextHubMessage != null)
          {
            localParcel1.writeInt(1);
            paramContextHubMessage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int unloadNanoApp(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public void unloadNanoAppFromHub(int paramInt, IContextHubTransactionCallback paramIContextHubTransactionCallback, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IContextHubService");
          localParcel1.writeInt(paramInt);
          if (paramIContextHubTransactionCallback != null) {
            paramIContextHubTransactionCallback = paramIContextHubTransactionCallback.asBinder();
          } else {
            paramIContextHubTransactionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIContextHubTransactionCallback);
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
