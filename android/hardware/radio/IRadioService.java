package android.hardware.radio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IRadioService
  extends IInterface
{
  public abstract ICloseHandle addAnnouncementListener(int[] paramArrayOfInt, IAnnouncementListener paramIAnnouncementListener)
    throws RemoteException;
  
  public abstract List<RadioManager.ModuleProperties> listModules()
    throws RemoteException;
  
  public abstract ITuner openTuner(int paramInt, RadioManager.BandConfig paramBandConfig, boolean paramBoolean, ITunerCallback paramITunerCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRadioService
  {
    private static final String DESCRIPTOR = "android.hardware.radio.IRadioService";
    static final int TRANSACTION_addAnnouncementListener = 3;
    static final int TRANSACTION_listModules = 1;
    static final int TRANSACTION_openTuner = 2;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.radio.IRadioService");
    }
    
    public static IRadioService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.radio.IRadioService");
      if ((localIInterface != null) && ((localIInterface instanceof IRadioService))) {
        return (IRadioService)localIInterface;
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
        ICloseHandle localICloseHandle = null;
        Object localObject = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.hardware.radio.IRadioService");
          localICloseHandle = addAnnouncementListener(paramParcel1.createIntArray(), IAnnouncementListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject;
          if (localICloseHandle != null) {
            paramParcel1 = localICloseHandle.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.radio.IRadioService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject = (RadioManager.BandConfig)RadioManager.BandConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = null;
          }
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          localObject = openTuner(paramInt1, (RadioManager.BandConfig)localObject, bool, ITunerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localICloseHandle;
          if (localObject != null) {
            paramParcel1 = ((ITuner)localObject).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.radio.IRadioService");
        paramParcel1 = listModules();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.radio.IRadioService");
      return true;
    }
    
    private static class Proxy
      implements IRadioService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public ICloseHandle addAnnouncementListener(int[] paramArrayOfInt, IAnnouncementListener paramIAnnouncementListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.IRadioService");
          localParcel1.writeIntArray(paramArrayOfInt);
          if (paramIAnnouncementListener != null) {
            paramArrayOfInt = paramIAnnouncementListener.asBinder();
          } else {
            paramArrayOfInt = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = ICloseHandle.Stub.asInterface(localParcel2.readStrongBinder());
          return paramArrayOfInt;
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
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.radio.IRadioService";
      }
      
      public List<RadioManager.ModuleProperties> listModules()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.IRadioService");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(RadioManager.ModuleProperties.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ITuner openTuner(int paramInt, RadioManager.BandConfig paramBandConfig, boolean paramBoolean, ITunerCallback paramITunerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.IRadioService");
          localParcel1.writeInt(paramInt);
          if (paramBandConfig != null)
          {
            localParcel1.writeInt(1);
            paramBandConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          if (paramITunerCallback != null) {
            paramBandConfig = paramITunerCallback.asBinder();
          } else {
            paramBandConfig = null;
          }
          localParcel1.writeStrongBinder(paramBandConfig);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBandConfig = ITuner.Stub.asInterface(localParcel2.readStrongBinder());
          return paramBandConfig;
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
