package android.service.autofill;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAutoFillService
  extends IInterface
{
  public abstract void onConnectedStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onFillRequest(FillRequest paramFillRequest, IFillCallback paramIFillCallback)
    throws RemoteException;
  
  public abstract void onSaveRequest(SaveRequest paramSaveRequest, ISaveCallback paramISaveCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAutoFillService
  {
    private static final String DESCRIPTOR = "android.service.autofill.IAutoFillService";
    static final int TRANSACTION_onConnectedStateChanged = 1;
    static final int TRANSACTION_onFillRequest = 2;
    static final int TRANSACTION_onSaveRequest = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.autofill.IAutoFillService");
    }
    
    public static IAutoFillService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.autofill.IAutoFillService");
      if ((localIInterface != null) && ((localIInterface instanceof IAutoFillService))) {
        return (IAutoFillService)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.service.autofill.IAutoFillService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (SaveRequest)SaveRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          onSaveRequest(paramParcel2, ISaveCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.autofill.IAutoFillService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (FillRequest)FillRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          onFillRequest(paramParcel2, IFillCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.service.autofill.IAutoFillService");
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        onConnectedStateChanged(bool);
        return true;
      }
      paramParcel2.writeString("android.service.autofill.IAutoFillService");
      return true;
    }
    
    private static class Proxy
      implements IAutoFillService
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
      
      public String getInterfaceDescriptor()
      {
        return "android.service.autofill.IAutoFillService";
      }
      
      public void onConnectedStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.autofill.IAutoFillService");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFillRequest(FillRequest paramFillRequest, IFillCallback paramIFillCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.autofill.IAutoFillService");
          if (paramFillRequest != null)
          {
            localParcel.writeInt(1);
            paramFillRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIFillCallback != null) {
            paramFillRequest = paramIFillCallback.asBinder();
          } else {
            paramFillRequest = null;
          }
          localParcel.writeStrongBinder(paramFillRequest);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSaveRequest(SaveRequest paramSaveRequest, ISaveCallback paramISaveCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.autofill.IAutoFillService");
          if (paramSaveRequest != null)
          {
            localParcel.writeInt(1);
            paramSaveRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramISaveCallback != null) {
            paramSaveRequest = paramISaveCallback.asBinder();
          } else {
            paramSaveRequest = null;
          }
          localParcel.writeStrongBinder(paramSaveRequest);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
