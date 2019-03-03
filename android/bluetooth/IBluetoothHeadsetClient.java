package android.bluetooth;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IBluetoothHeadsetClient
  extends IInterface
{
  public abstract boolean acceptCall(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean connect(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean connectAudio(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract BluetoothHeadsetClientCall dial(BluetoothDevice paramBluetoothDevice, String paramString)
    throws RemoteException;
  
  public abstract boolean disconnect(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean disconnectAudio(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean enterPrivateMode(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean explicitCallTransfer(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean getAudioRouteAllowed(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getAudioState(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getConnectedDevices()
    throws RemoteException;
  
  public abstract int getConnectionState(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract Bundle getCurrentAgEvents(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract Bundle getCurrentAgFeatures(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothHeadsetClientCall> getCurrentCalls(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract boolean getLastVoiceTagNumber(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract int getPriority(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean holdCall(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean rejectCall(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean sendDTMF(BluetoothDevice paramBluetoothDevice, byte paramByte)
    throws RemoteException;
  
  public abstract void setAudioRouteAllowed(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean startVoiceRecognition(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean stopVoiceRecognition(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean terminateCall(BluetoothDevice paramBluetoothDevice, BluetoothHeadsetClientCall paramBluetoothHeadsetClientCall)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBluetoothHeadsetClient
  {
    private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHeadsetClient";
    static final int TRANSACTION_acceptCall = 12;
    static final int TRANSACTION_connect = 1;
    static final int TRANSACTION_connectAudio = 22;
    static final int TRANSACTION_dial = 18;
    static final int TRANSACTION_disconnect = 2;
    static final int TRANSACTION_disconnectAudio = 23;
    static final int TRANSACTION_enterPrivateMode = 16;
    static final int TRANSACTION_explicitCallTransfer = 17;
    static final int TRANSACTION_getAudioRouteAllowed = 25;
    static final int TRANSACTION_getAudioState = 21;
    static final int TRANSACTION_getConnectedDevices = 3;
    static final int TRANSACTION_getConnectionState = 5;
    static final int TRANSACTION_getCurrentAgEvents = 11;
    static final int TRANSACTION_getCurrentAgFeatures = 26;
    static final int TRANSACTION_getCurrentCalls = 10;
    static final int TRANSACTION_getDevicesMatchingConnectionStates = 4;
    static final int TRANSACTION_getLastVoiceTagNumber = 20;
    static final int TRANSACTION_getPriority = 7;
    static final int TRANSACTION_holdCall = 13;
    static final int TRANSACTION_rejectCall = 14;
    static final int TRANSACTION_sendDTMF = 19;
    static final int TRANSACTION_setAudioRouteAllowed = 24;
    static final int TRANSACTION_setPriority = 6;
    static final int TRANSACTION_startVoiceRecognition = 8;
    static final int TRANSACTION_stopVoiceRecognition = 9;
    static final int TRANSACTION_terminateCall = 15;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.IBluetoothHeadsetClient");
    }
    
    public static IBluetoothHeadsetClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.IBluetoothHeadsetClient");
      if ((localIInterface != null) && ((localIInterface instanceof IBluetoothHeadsetClient))) {
        return (IBluetoothHeadsetClient)localIInterface;
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
        boolean bool = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        Object localObject17 = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        Object localObject21 = null;
        Object localObject22 = null;
        Object localObject23 = null;
        Object localObject24 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 26: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject24;
          }
          paramParcel1 = getCurrentAgFeatures(paramParcel1);
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
        case 25: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = getAudioRouteAllowed(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            localObject24 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject24 = localObject2;
          }
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setAudioRouteAllowed((BluetoothDevice)localObject24, bool);
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = disconnectAudio(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = connectAudio(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramInt1 = getAudioState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramInt1 = getLastVoiceTagNumber(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            localObject24 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject24 = localObject7;
          }
          paramInt1 = sendDTMF((BluetoothDevice)localObject24, paramParcel1.readByte());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            localObject24 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject24 = localObject8;
          }
          paramParcel1 = dial((BluetoothDevice)localObject24, paramParcel1.readString());
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
        case 17: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          paramInt1 = explicitCallTransfer(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            localObject24 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject24 = localObject10;
          }
          paramInt1 = enterPrivateMode((BluetoothDevice)localObject24, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            localObject24 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject24 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothHeadsetClientCall)BluetoothHeadsetClientCall.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          paramInt1 = terminateCall((BluetoothDevice)localObject24, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          paramInt1 = rejectCall(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          paramInt1 = holdCall(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            localObject24 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject24 = localObject14;
          }
          paramInt1 = acceptCall((BluetoothDevice)localObject24, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          paramParcel1 = getCurrentAgEvents(paramParcel1);
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
        case 10: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          paramParcel1 = getCurrentCalls(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject17;
          }
          paramInt1 = stopVoiceRecognition(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          paramInt1 = startVoiceRecognition(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject19;
          }
          paramInt1 = getPriority(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            localObject24 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject24 = localObject20;
          }
          paramInt1 = setPriority((BluetoothDevice)localObject24, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          paramInt1 = getConnectionState(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          paramParcel1 = getDevicesMatchingConnectionStates(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          paramParcel1 = getConnectedDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject22;
          }
          paramInt1 = disconnect(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.IBluetoothHeadsetClient");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject23;
        }
        paramInt1 = connect(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.bluetooth.IBluetoothHeadsetClient");
      return true;
    }
    
    private static class Proxy
      implements IBluetoothHeadsetClient
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean acceptCall(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean connect(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public boolean connectAudio(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
      
      public BluetoothHeadsetClientCall dial(BluetoothDevice paramBluetoothDevice, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramBluetoothDevice = (BluetoothHeadsetClientCall)BluetoothHeadsetClientCall.CREATOR.createFromParcel(localParcel2);
          } else {
            paramBluetoothDevice = null;
          }
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean disconnect(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean disconnectAudio(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(23, localParcel1, localParcel2, 0);
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
      
      public boolean enterPrivateMode(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public boolean explicitCallTransfer(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
      
      public boolean getAudioRouteAllowed(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(25, localParcel1, localParcel2, 0);
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
      
      public int getAudioState(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(21, localParcel1, localParcel2, 0);
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
      
      public List<BluetoothDevice> getConnectedDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(BluetoothDevice.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getConnectionState(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public Bundle getCurrentAgEvents(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramBluetoothDevice = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramBluetoothDevice = null;
          }
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getCurrentAgFeatures(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramBluetoothDevice = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramBluetoothDevice = null;
          }
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<BluetoothHeadsetClientCall> getCurrentCalls(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBluetoothDevice = localParcel2.createTypedArrayList(BluetoothHeadsetClientCall.CREATOR);
          return paramBluetoothDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createTypedArrayList(BluetoothDevice.CREATOR);
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.bluetooth.IBluetoothHeadsetClient";
      }
      
      public boolean getLastVoiceTagNumber(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
      
      public int getPriority(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean holdCall(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
      
      public boolean rejectCall(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public boolean sendDTMF(BluetoothDevice paramBluetoothDevice, byte paramByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByte(paramByte);
          mRemote.transact(19, localParcel1, localParcel2, 0);
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
      
      public void setAudioRouteAllowed(BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
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
      
      public boolean setPriority(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public boolean startVoiceRecognition(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public boolean stopVoiceRecognition(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public boolean terminateCall(BluetoothDevice paramBluetoothDevice, BluetoothHeadsetClientCall paramBluetoothHeadsetClientCall)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.bluetooth.IBluetoothHeadsetClient");
          boolean bool = true;
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBluetoothHeadsetClientCall != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothHeadsetClientCall.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
    }
  }
}
