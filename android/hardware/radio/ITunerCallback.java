package android.hardware.radio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.Map;

public abstract interface ITunerCallback
  extends IInterface
{
  public abstract void onAntennaState(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onBackgroundScanAvailabilityChange(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onBackgroundScanComplete()
    throws RemoteException;
  
  public abstract void onConfigurationChanged(RadioManager.BandConfig paramBandConfig)
    throws RemoteException;
  
  public abstract void onCurrentProgramInfoChanged(RadioManager.ProgramInfo paramProgramInfo)
    throws RemoteException;
  
  public abstract void onEmergencyAnnouncement(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onError(int paramInt)
    throws RemoteException;
  
  public abstract void onParametersUpdated(Map paramMap)
    throws RemoteException;
  
  public abstract void onProgramListChanged()
    throws RemoteException;
  
  public abstract void onProgramListUpdated(ProgramList.Chunk paramChunk)
    throws RemoteException;
  
  public abstract void onTrafficAnnouncement(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onTuneFailed(int paramInt, ProgramSelector paramProgramSelector)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITunerCallback
  {
    private static final String DESCRIPTOR = "android.hardware.radio.ITunerCallback";
    static final int TRANSACTION_onAntennaState = 7;
    static final int TRANSACTION_onBackgroundScanAvailabilityChange = 8;
    static final int TRANSACTION_onBackgroundScanComplete = 9;
    static final int TRANSACTION_onConfigurationChanged = 3;
    static final int TRANSACTION_onCurrentProgramInfoChanged = 4;
    static final int TRANSACTION_onEmergencyAnnouncement = 6;
    static final int TRANSACTION_onError = 1;
    static final int TRANSACTION_onParametersUpdated = 12;
    static final int TRANSACTION_onProgramListChanged = 10;
    static final int TRANSACTION_onProgramListUpdated = 11;
    static final int TRANSACTION_onTrafficAnnouncement = 5;
    static final int TRANSACTION_onTuneFailed = 2;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.radio.ITunerCallback");
    }
    
    public static ITunerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.radio.ITunerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITunerCallback))) {
        return (ITunerCallback)localIInterface;
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
        boolean bool3 = false;
        boolean bool4 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 12: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          onParametersUpdated(paramParcel1.readHashMap(getClass().getClassLoader()));
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ProgramList.Chunk)ProgramList.Chunk.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onProgramListUpdated(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          onProgramListChanged();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          onBackgroundScanComplete();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onBackgroundScanAvailabilityChange(bool4);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onAntennaState(bool4);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onEmergencyAnnouncement(bool4);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onTrafficAnnouncement(bool4);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RadioManager.ProgramInfo)RadioManager.ProgramInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onCurrentProgramInfoChanged(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RadioManager.BandConfig)RadioManager.BandConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onConfigurationChanged(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ProgramSelector)ProgramSelector.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onTuneFailed(paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.radio.ITunerCallback");
        onError(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.hardware.radio.ITunerCallback");
      return true;
    }
    
    private static class Proxy
      implements ITunerCallback
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
        return "android.hardware.radio.ITunerCallback";
      }
      
      public void onAntennaState(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onBackgroundScanAvailabilityChange(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onBackgroundScanComplete()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onConfigurationChanged(RadioManager.BandConfig paramBandConfig)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          if (paramBandConfig != null)
          {
            localParcel.writeInt(1);
            paramBandConfig.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCurrentProgramInfoChanged(RadioManager.ProgramInfo paramProgramInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          if (paramProgramInfo != null)
          {
            localParcel.writeInt(1);
            paramProgramInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEmergencyAnnouncement(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onError(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onParametersUpdated(Map paramMap)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          localParcel.writeMap(paramMap);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onProgramListChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onProgramListUpdated(ProgramList.Chunk paramChunk)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          if (paramChunk != null)
          {
            localParcel.writeInt(1);
            paramChunk.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTrafficAnnouncement(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTuneFailed(int paramInt, ProgramSelector paramProgramSelector)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.ITunerCallback");
          localParcel.writeInt(paramInt);
          if (paramProgramSelector != null)
          {
            localParcel.writeInt(1);
            paramProgramSelector.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
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
