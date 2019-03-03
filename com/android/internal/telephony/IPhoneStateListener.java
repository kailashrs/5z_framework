package com.android.internal.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.CellInfo;
import android.telephony.DataConnectionRealTimeInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.PreciseCallState;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.VoLteServiceState;
import java.util.List;

public abstract interface IPhoneStateListener
  extends IInterface
{
  public abstract void onCallForwardingIndicatorChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onCallStateChanged(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void onCarrierNetworkChange(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onCellInfoChanged(List<CellInfo> paramList)
    throws RemoteException;
  
  public abstract void onCellLocationChanged(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onDataActivationStateChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onDataActivity(int paramInt)
    throws RemoteException;
  
  public abstract void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo paramDataConnectionRealTimeInfo)
    throws RemoteException;
  
  public abstract void onDataConnectionStateChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onMessageWaitingIndicatorChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onOemHookRawEvent(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onOtaspChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> paramList)
    throws RemoteException;
  
  public abstract void onPreciseCallStateChanged(PreciseCallState paramPreciseCallState)
    throws RemoteException;
  
  public abstract void onPreciseDataConnectionStateChanged(PreciseDataConnectionState paramPreciseDataConnectionState)
    throws RemoteException;
  
  public abstract void onServiceStateChanged(ServiceState paramServiceState)
    throws RemoteException;
  
  public abstract void onSignalStrengthChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onSignalStrengthsChanged(SignalStrength paramSignalStrength)
    throws RemoteException;
  
  public abstract void onUserMobileDataStateChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onVoLteServiceStateChanged(VoLteServiceState paramVoLteServiceState)
    throws RemoteException;
  
  public abstract void onVoiceActivationStateChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPhoneStateListener
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneStateListener";
    static final int TRANSACTION_onCallForwardingIndicatorChanged = 4;
    static final int TRANSACTION_onCallStateChanged = 6;
    static final int TRANSACTION_onCarrierNetworkChange = 20;
    static final int TRANSACTION_onCellInfoChanged = 12;
    static final int TRANSACTION_onCellLocationChanged = 5;
    static final int TRANSACTION_onDataActivationStateChanged = 18;
    static final int TRANSACTION_onDataActivity = 8;
    static final int TRANSACTION_onDataConnectionRealTimeInfoChanged = 15;
    static final int TRANSACTION_onDataConnectionStateChanged = 7;
    static final int TRANSACTION_onMessageWaitingIndicatorChanged = 3;
    static final int TRANSACTION_onOemHookRawEvent = 19;
    static final int TRANSACTION_onOtaspChanged = 11;
    static final int TRANSACTION_onPhysicalChannelConfigurationChanged = 10;
    static final int TRANSACTION_onPreciseCallStateChanged = 13;
    static final int TRANSACTION_onPreciseDataConnectionStateChanged = 14;
    static final int TRANSACTION_onServiceStateChanged = 1;
    static final int TRANSACTION_onSignalStrengthChanged = 2;
    static final int TRANSACTION_onSignalStrengthsChanged = 9;
    static final int TRANSACTION_onUserMobileDataStateChanged = 21;
    static final int TRANSACTION_onVoLteServiceStateChanged = 16;
    static final int TRANSACTION_onVoiceActivationStateChanged = 17;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.IPhoneStateListener");
    }
    
    public static IPhoneStateListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.IPhoneStateListener");
      if ((localIInterface != null) && ((localIInterface instanceof IPhoneStateListener))) {
        return (IPhoneStateListener)localIInterface;
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
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onUserMobileDataStateChanged(bool4);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onCarrierNetworkChange(bool4);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onOemHookRawEvent(paramParcel1.createByteArray());
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onDataActivationStateChanged(paramParcel1.readInt());
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onVoiceActivationStateChanged(paramParcel1.readInt());
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VoLteServiceState)VoLteServiceState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          onVoLteServiceStateChanged(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DataConnectionRealTimeInfo)DataConnectionRealTimeInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onDataConnectionRealTimeInfoChanged(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PreciseDataConnectionState)PreciseDataConnectionState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onPreciseDataConnectionStateChanged(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PreciseCallState)PreciseCallState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onPreciseCallStateChanged(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onCellInfoChanged(paramParcel1.createTypedArrayList(CellInfo.CREATOR));
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onOtaspChanged(paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onPhysicalChannelConfigurationChanged(paramParcel1.createTypedArrayList(PhysicalChannelConfig.CREATOR));
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SignalStrength)SignalStrength.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onSignalStrengthsChanged(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onDataActivity(paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onDataConnectionStateChanged(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onCallStateChanged(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          onCellLocationChanged(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onCallForwardingIndicatorChanged(bool4);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onMessageWaitingIndicatorChanged(bool4);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
          onSignalStrengthChanged(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneStateListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ServiceState)ServiceState.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject6;
        }
        onServiceStateChanged(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.IPhoneStateListener");
      return true;
    }
    
    private static class Proxy
      implements IPhoneStateListener
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
        return "com.android.internal.telephony.IPhoneStateListener";
      }
      
      public void onCallForwardingIndicatorChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCallStateChanged(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCarrierNetworkChange(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCellInfoChanged(List<CellInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeTypedList(paramList);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCellLocationChanged(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDataActivationStateChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDataActivity(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo paramDataConnectionRealTimeInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          if (paramDataConnectionRealTimeInfo != null)
          {
            localParcel.writeInt(1);
            paramDataConnectionRealTimeInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDataConnectionStateChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMessageWaitingIndicatorChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onOemHookRawEvent(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onOtaspChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeTypedList(paramList);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPreciseCallStateChanged(PreciseCallState paramPreciseCallState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          if (paramPreciseCallState != null)
          {
            localParcel.writeInt(1);
            paramPreciseCallState.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState paramPreciseDataConnectionState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          if (paramPreciseDataConnectionState != null)
          {
            localParcel.writeInt(1);
            paramPreciseDataConnectionState.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onServiceStateChanged(ServiceState paramServiceState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          if (paramServiceState != null)
          {
            localParcel.writeInt(1);
            paramServiceState.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSignalStrengthChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSignalStrengthsChanged(SignalStrength paramSignalStrength)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          if (paramSignalStrength != null)
          {
            localParcel.writeInt(1);
            paramSignalStrength.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUserMobileDataStateChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVoLteServiceStateChanged(VoLteServiceState paramVoLteServiceState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          if (paramVoLteServiceState != null)
          {
            localParcel.writeInt(1);
            paramVoLteServiceState.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVoiceActivationStateChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.IPhoneStateListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(17, localParcel, null, 1);
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
