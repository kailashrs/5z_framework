package com.android.internal.telephony;

import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.CellInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.VoLteServiceState;
import java.util.List;

public abstract interface ITelephonyRegistry
  extends IInterface
{
  public abstract void addOnSubscriptionsChangedListener(String paramString, IOnSubscriptionsChangedListener paramIOnSubscriptionsChangedListener)
    throws RemoteException;
  
  public abstract void listen(String paramString, IPhoneStateListener paramIPhoneStateListener, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void listenForSubscriber(int paramInt1, String paramString, IPhoneStateListener paramIPhoneStateListener, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyCallForwardingChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyCallForwardingChangedForSubscriber(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyCallState(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void notifyCallStateForPhoneId(int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws RemoteException;
  
  public abstract void notifyCarrierNetworkChange(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyCellInfo(List<CellInfo> paramList)
    throws RemoteException;
  
  public abstract void notifyCellInfoForSubscriber(int paramInt, List<CellInfo> paramList)
    throws RemoteException;
  
  public abstract void notifyCellLocation(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void notifyCellLocationForSubscriber(int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void notifyDataActivity(int paramInt)
    throws RemoteException;
  
  public abstract void notifyDataActivityForSubscriber(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void notifyDataConnection(int paramInt1, boolean paramBoolean1, String paramString1, String paramString2, String paramString3, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, int paramInt2, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void notifyDataConnectionFailed(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void notifyDataConnectionFailedForSubscriber(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void notifyDataConnectionForSubscriber(int paramInt1, int paramInt2, boolean paramBoolean1, String paramString1, String paramString2, String paramString3, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, int paramInt3, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void notifyDisconnectCause(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void notifyMessageWaitingChangedForPhoneId(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyOemHookRawEventForSubscriber(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void notifyOtaspChanged(int paramInt)
    throws RemoteException;
  
  public abstract void notifyPhysicalChannelConfiguration(List<PhysicalChannelConfig> paramList)
    throws RemoteException;
  
  public abstract void notifyPhysicalChannelConfigurationForSubscriber(int paramInt, List<PhysicalChannelConfig> paramList)
    throws RemoteException;
  
  public abstract void notifyPreciseCallState(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void notifyPreciseDataConnectionFailed(String paramString1, String paramString2, String paramString3, String paramString4)
    throws RemoteException;
  
  public abstract void notifyServiceStateForPhoneId(int paramInt1, int paramInt2, ServiceState paramServiceState)
    throws RemoteException;
  
  public abstract void notifySignalStrengthForPhoneId(int paramInt1, int paramInt2, SignalStrength paramSignalStrength)
    throws RemoteException;
  
  public abstract void notifySimActivationStateChangedForPhoneId(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void notifySubscriptionInfoChanged()
    throws RemoteException;
  
  public abstract void notifyUserMobileDataStateChangedForPhoneId(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyVoLteServiceStateChanged(VoLteServiceState paramVoLteServiceState)
    throws RemoteException;
  
  public abstract void removeOnSubscriptionsChangedListener(String paramString, IOnSubscriptionsChangedListener paramIOnSubscriptionsChangedListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITelephonyRegistry
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephonyRegistry";
    static final int TRANSACTION_addOnSubscriptionsChangedListener = 1;
    static final int TRANSACTION_listen = 3;
    static final int TRANSACTION_listenForSubscriber = 4;
    static final int TRANSACTION_notifyCallForwardingChanged = 10;
    static final int TRANSACTION_notifyCallForwardingChangedForSubscriber = 11;
    static final int TRANSACTION_notifyCallState = 5;
    static final int TRANSACTION_notifyCallStateForPhoneId = 6;
    static final int TRANSACTION_notifyCarrierNetworkChange = 32;
    static final int TRANSACTION_notifyCellInfo = 21;
    static final int TRANSACTION_notifyCellInfoForSubscriber = 27;
    static final int TRANSACTION_notifyCellLocation = 18;
    static final int TRANSACTION_notifyCellLocationForSubscriber = 19;
    static final int TRANSACTION_notifyDataActivity = 12;
    static final int TRANSACTION_notifyDataActivityForSubscriber = 13;
    static final int TRANSACTION_notifyDataConnection = 14;
    static final int TRANSACTION_notifyDataConnectionFailed = 16;
    static final int TRANSACTION_notifyDataConnectionFailedForSubscriber = 17;
    static final int TRANSACTION_notifyDataConnectionForSubscriber = 15;
    static final int TRANSACTION_notifyDisconnectCause = 25;
    static final int TRANSACTION_notifyMessageWaitingChangedForPhoneId = 9;
    static final int TRANSACTION_notifyOemHookRawEventForSubscriber = 30;
    static final int TRANSACTION_notifyOtaspChanged = 20;
    static final int TRANSACTION_notifyPhysicalChannelConfiguration = 22;
    static final int TRANSACTION_notifyPhysicalChannelConfigurationForSubscriber = 23;
    static final int TRANSACTION_notifyPreciseCallState = 24;
    static final int TRANSACTION_notifyPreciseDataConnectionFailed = 26;
    static final int TRANSACTION_notifyServiceStateForPhoneId = 7;
    static final int TRANSACTION_notifySignalStrengthForPhoneId = 8;
    static final int TRANSACTION_notifySimActivationStateChangedForPhoneId = 29;
    static final int TRANSACTION_notifySubscriptionInfoChanged = 31;
    static final int TRANSACTION_notifyUserMobileDataStateChangedForPhoneId = 33;
    static final int TRANSACTION_notifyVoLteServiceStateChanged = 28;
    static final int TRANSACTION_removeOnSubscriptionsChangedListener = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.ITelephonyRegistry");
    }
    
    public static ITelephonyRegistry asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.ITelephonyRegistry");
      if ((localIInterface != null) && ((localIInterface instanceof ITelephonyRegistry))) {
        return (ITelephonyRegistry)localIInterface;
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
        Object localObject1 = null;
        String str3 = null;
        String str4 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          notifyUserMobileDataStateChangedForPhoneId(paramInt1, paramInt2, bool6);
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          bool6 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          notifyCarrierNetworkChange(bool6);
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifySubscriptionInfoChanged();
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyOemHookRawEventForSubscriber(paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifySimActivationStateChangedForPhoneId(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VoLteServiceState)VoLteServiceState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject3;
          }
          notifyVoLteServiceStateChanged(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyCellInfoForSubscriber(paramParcel1.readInt(), paramParcel1.createTypedArrayList(CellInfo.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyPreciseDataConnectionFailed(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyDisconnectCause(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyPreciseCallState(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyPhysicalChannelConfigurationForSubscriber(paramParcel1.readInt(), paramParcel1.createTypedArrayList(PhysicalChannelConfig.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyPhysicalChannelConfiguration(paramParcel1.createTypedArrayList(PhysicalChannelConfig.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyCellInfo(paramParcel1.createTypedArrayList(CellInfo.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyOtaspChanged(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          notifyCellLocationForSubscriber(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          notifyCellLocation(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyDataConnectionFailedForSubscriber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyDataConnectionFailed(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          str1 = paramParcel1.readString();
          str3 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (LinkProperties)LinkProperties.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          int i = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          notifyDataConnectionForSubscriber(paramInt1, paramInt2, bool6, str1, str3, str2, (LinkProperties)localObject3, (NetworkCapabilities)localObject1, i, bool1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          str2 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          str4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (LinkProperties)LinkProperties.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject1 = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(paramParcel1);; localObject1 = str3) {
            break;
          }
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          notifyDataConnection(paramInt1, bool6, str2, str1, str4, (LinkProperties)localObject3, (NetworkCapabilities)localObject1, paramInt2, bool1);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyDataActivityForSubscriber(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyDataActivity(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt1 = paramParcel1.readInt();
          bool6 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          notifyCallForwardingChangedForSubscriber(paramInt1, bool6);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          bool6 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          notifyCallForwardingChanged(bool6);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          bool6 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          notifyMessageWaitingChangedForPhoneId(paramInt1, paramInt2, bool6);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SignalStrength)SignalStrength.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str4;
          }
          notifySignalStrengthForPhoneId(paramInt1, paramInt2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ServiceState)ServiceState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          notifyServiceStateForPhoneId(paramInt2, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyCallStateForPhoneId(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          notifyCallState(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          paramInt2 = paramParcel1.readInt();
          localObject3 = paramParcel1.readString();
          localObject1 = IPhoneStateListener.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          listenForSubscriber(paramInt2, (String)localObject3, (IPhoneStateListener)localObject1, paramInt1, bool6);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          localObject1 = paramParcel1.readString();
          localObject3 = IPhoneStateListener.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          bool6 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          listen((String)localObject1, (IPhoneStateListener)localObject3, paramInt1, bool6);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
          removeOnSubscriptionsChangedListener(paramParcel1.readString(), IOnSubscriptionsChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.ITelephonyRegistry");
        addOnSubscriptionsChangedListener(paramParcel1.readString(), IOnSubscriptionsChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.ITelephonyRegistry");
      return true;
    }
    
    private static class Proxy
      implements ITelephonyRegistry
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addOnSubscriptionsChangedListener(String paramString, IOnSubscriptionsChangedListener paramIOnSubscriptionsChangedListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeString(paramString);
          if (paramIOnSubscriptionsChangedListener != null) {
            paramString = paramIOnSubscriptionsChangedListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.ITelephonyRegistry";
      }
      
      public void listen(String paramString, IPhoneStateListener paramIPhoneStateListener, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeString(paramString);
          if (paramIPhoneStateListener != null) {
            paramString = paramIPhoneStateListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void listenForSubscriber(int paramInt1, String paramString, IPhoneStateListener paramIPhoneStateListener, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          if (paramIPhoneStateListener != null) {
            paramString = paramIPhoneStateListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
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
      
      public void notifyCallForwardingChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramBoolean);
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
      
      public void notifyCallForwardingChangedForSubscriber(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void notifyCallState(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyCallStateForPhoneId(int paramInt1, int paramInt2, int paramInt3, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyCarrierNetworkChange(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyCellInfo(List<CellInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeTypedList(paramList);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyCellInfoForSubscriber(int paramInt, List<CellInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyCellLocation(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void notifyCellLocationForSubscriber(int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void notifyDataActivity(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
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
      
      public void notifyDataActivityForSubscriber(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void notifyDataConnection(int paramInt1, boolean paramBoolean1, String paramString1, String paramString2, String paramString3, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, int paramInt2, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          if (paramLinkProperties != null)
          {
            localParcel1.writeInt(1);
            paramLinkProperties.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramNetworkCapabilities != null)
          {
            localParcel1.writeInt(1);
            paramNetworkCapabilities.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void notifyDataConnectionFailed(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void notifyDataConnectionFailedForSubscriber(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyDataConnectionForSubscriber(int paramInt1, int paramInt2, boolean paramBoolean1, String paramString1, String paramString2, String paramString3, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, int paramInt3, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          if (paramLinkProperties != null)
          {
            localParcel1.writeInt(1);
            paramLinkProperties.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramNetworkCapabilities != null)
          {
            localParcel1.writeInt(1);
            paramNetworkCapabilities.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void notifyDisconnectCause(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyMessageWaitingChangedForPhoneId(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyOemHookRawEventForSubscriber(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyOtaspChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
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
      
      public void notifyPhysicalChannelConfiguration(List<PhysicalChannelConfig> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeTypedList(paramList);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyPhysicalChannelConfigurationForSubscriber(int paramInt, List<PhysicalChannelConfig> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyPreciseCallState(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyPreciseDataConnectionFailed(String paramString1, String paramString2, String paramString3, String paramString4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyServiceStateForPhoneId(int paramInt1, int paramInt2, ServiceState paramServiceState)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramServiceState != null)
          {
            localParcel1.writeInt(1);
            paramServiceState.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifySignalStrengthForPhoneId(int paramInt1, int paramInt2, SignalStrength paramSignalStrength)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramSignalStrength != null)
          {
            localParcel1.writeInt(1);
            paramSignalStrength.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifySimActivationStateChangedForPhoneId(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifySubscriptionInfoChanged()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyUserMobileDataStateChangedForPhoneId(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyVoLteServiceStateChanged(VoLteServiceState paramVoLteServiceState)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          if (paramVoLteServiceState != null)
          {
            localParcel1.writeInt(1);
            paramVoLteServiceState.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeOnSubscriptionsChangedListener(String paramString, IOnSubscriptionsChangedListener paramIOnSubscriptionsChangedListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephonyRegistry");
          localParcel1.writeString(paramString);
          if (paramIOnSubscriptionsChangedListener != null) {
            paramString = paramIOnSubscriptionsChangedListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
