package android.companion;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface ICompanionDeviceManager
  extends IInterface
{
  public abstract void associate(AssociationRequest paramAssociationRequest, IFindDeviceCallback paramIFindDeviceCallback, String paramString)
    throws RemoteException;
  
  public abstract void disassociate(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract List<String> getAssociations(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasNotificationAccess(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract PendingIntent requestNotificationAccess(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void stopScan(AssociationRequest paramAssociationRequest, IFindDeviceCallback paramIFindDeviceCallback, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICompanionDeviceManager
  {
    private static final String DESCRIPTOR = "android.companion.ICompanionDeviceManager";
    static final int TRANSACTION_associate = 1;
    static final int TRANSACTION_disassociate = 4;
    static final int TRANSACTION_getAssociations = 3;
    static final int TRANSACTION_hasNotificationAccess = 5;
    static final int TRANSACTION_requestNotificationAccess = 6;
    static final int TRANSACTION_stopScan = 2;
    
    public Stub()
    {
      attachInterface(this, "android.companion.ICompanionDeviceManager");
    }
    
    public static ICompanionDeviceManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.companion.ICompanionDeviceManager");
      if ((localIInterface != null) && ((localIInterface instanceof ICompanionDeviceManager))) {
        return (ICompanionDeviceManager)localIInterface;
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
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.companion.ICompanionDeviceManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject4;
          }
          paramParcel1 = requestNotificationAccess(paramParcel1);
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
          paramParcel1.enforceInterface("android.companion.ICompanionDeviceManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = hasNotificationAccess(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.companion.ICompanionDeviceManager");
          disassociate(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.companion.ICompanionDeviceManager");
          paramParcel1 = getAssociations(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.companion.ICompanionDeviceManager");
          if (paramParcel1.readInt() != 0) {
            localObject4 = (AssociationRequest)AssociationRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject2;
          }
          stopScan((AssociationRequest)localObject4, IFindDeviceCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.companion.ICompanionDeviceManager");
        if (paramParcel1.readInt() != 0) {
          localObject4 = (AssociationRequest)AssociationRequest.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject4 = localObject3;
        }
        associate((AssociationRequest)localObject4, IFindDeviceCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.companion.ICompanionDeviceManager");
      return true;
    }
    
    private static class Proxy
      implements ICompanionDeviceManager
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
      
      public void associate(AssociationRequest paramAssociationRequest, IFindDeviceCallback paramIFindDeviceCallback, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.companion.ICompanionDeviceManager");
          if (paramAssociationRequest != null)
          {
            localParcel1.writeInt(1);
            paramAssociationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIFindDeviceCallback != null) {
            paramAssociationRequest = paramIFindDeviceCallback.asBinder();
          } else {
            paramAssociationRequest = null;
          }
          localParcel1.writeStrongBinder(paramAssociationRequest);
          localParcel1.writeString(paramString);
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
      
      public void disassociate(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.companion.ICompanionDeviceManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public List<String> getAssociations(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.companion.ICompanionDeviceManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArrayList();
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
        return "android.companion.ICompanionDeviceManager";
      }
      
      public boolean hasNotificationAccess(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.companion.ICompanionDeviceManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public PendingIntent requestNotificationAccess(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.companion.ICompanionDeviceManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (PendingIntent)PendingIntent.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopScan(AssociationRequest paramAssociationRequest, IFindDeviceCallback paramIFindDeviceCallback, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.companion.ICompanionDeviceManager");
          if (paramAssociationRequest != null)
          {
            localParcel1.writeInt(1);
            paramAssociationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIFindDeviceCallback != null) {
            paramAssociationRequest = paramIFindDeviceCallback.asBinder();
          } else {
            paramAssociationRequest = null;
          }
          localParcel1.writeStrongBinder(paramAssociationRequest);
          localParcel1.writeString(paramString);
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
    }
  }
}
