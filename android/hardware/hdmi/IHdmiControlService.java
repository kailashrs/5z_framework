package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IHdmiControlService
  extends IInterface
{
  public abstract void addDeviceEventListener(IHdmiDeviceEventListener paramIHdmiDeviceEventListener)
    throws RemoteException;
  
  public abstract void addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener paramIHdmiMhlVendorCommandListener)
    throws RemoteException;
  
  public abstract void addHotplugEventListener(IHdmiHotplugEventListener paramIHdmiHotplugEventListener)
    throws RemoteException;
  
  public abstract void addSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener paramIHdmiSystemAudioModeChangeListener)
    throws RemoteException;
  
  public abstract void addVendorCommandListener(IHdmiVendorCommandListener paramIHdmiVendorCommandListener, int paramInt)
    throws RemoteException;
  
  public abstract boolean canChangeSystemAudioMode()
    throws RemoteException;
  
  public abstract void clearTimerRecording(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void deviceSelect(int paramInt, IHdmiControlCallback paramIHdmiControlCallback)
    throws RemoteException;
  
  public abstract HdmiDeviceInfo getActiveSource()
    throws RemoteException;
  
  public abstract List<HdmiDeviceInfo> getDeviceList()
    throws RemoteException;
  
  public abstract List<HdmiDeviceInfo> getInputDevices()
    throws RemoteException;
  
  public abstract List<HdmiPortInfo> getPortInfo()
    throws RemoteException;
  
  public abstract int[] getSupportedTypes()
    throws RemoteException;
  
  public abstract boolean getSystemAudioMode()
    throws RemoteException;
  
  public abstract void oneTouchPlay(IHdmiControlCallback paramIHdmiControlCallback)
    throws RemoteException;
  
  public abstract void portSelect(int paramInt, IHdmiControlCallback paramIHdmiControlCallback)
    throws RemoteException;
  
  public abstract void queryDisplayStatus(IHdmiControlCallback paramIHdmiControlCallback)
    throws RemoteException;
  
  public abstract void removeHotplugEventListener(IHdmiHotplugEventListener paramIHdmiHotplugEventListener)
    throws RemoteException;
  
  public abstract void removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener paramIHdmiSystemAudioModeChangeListener)
    throws RemoteException;
  
  public abstract void sendKeyEvent(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void sendMhlVendorCommand(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void sendStandby(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void sendVendorCommand(int paramInt1, int paramInt2, byte[] paramArrayOfByte, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setArcMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setHdmiRecordListener(IHdmiRecordListener paramIHdmiRecordListener)
    throws RemoteException;
  
  public abstract void setInputChangeListener(IHdmiInputChangeListener paramIHdmiInputChangeListener)
    throws RemoteException;
  
  public abstract void setProhibitMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setStandbyMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSystemAudioMode(boolean paramBoolean, IHdmiControlCallback paramIHdmiControlCallback)
    throws RemoteException;
  
  public abstract void setSystemAudioMute(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSystemAudioVolume(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void startOneTouchRecord(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void startTimerRecording(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void stopOneTouchRecord(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHdmiControlService
  {
    private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiControlService";
    static final int TRANSACTION_addDeviceEventListener = 7;
    static final int TRANSACTION_addHdmiMhlVendorCommandListener = 33;
    static final int TRANSACTION_addHotplugEventListener = 5;
    static final int TRANSACTION_addSystemAudioModeChangeListener = 15;
    static final int TRANSACTION_addVendorCommandListener = 25;
    static final int TRANSACTION_canChangeSystemAudioMode = 12;
    static final int TRANSACTION_clearTimerRecording = 31;
    static final int TRANSACTION_deviceSelect = 8;
    static final int TRANSACTION_getActiveSource = 2;
    static final int TRANSACTION_getDeviceList = 23;
    static final int TRANSACTION_getInputDevices = 22;
    static final int TRANSACTION_getPortInfo = 11;
    static final int TRANSACTION_getSupportedTypes = 1;
    static final int TRANSACTION_getSystemAudioMode = 13;
    static final int TRANSACTION_oneTouchPlay = 3;
    static final int TRANSACTION_portSelect = 9;
    static final int TRANSACTION_queryDisplayStatus = 4;
    static final int TRANSACTION_removeHotplugEventListener = 6;
    static final int TRANSACTION_removeSystemAudioModeChangeListener = 16;
    static final int TRANSACTION_sendKeyEvent = 10;
    static final int TRANSACTION_sendMhlVendorCommand = 32;
    static final int TRANSACTION_sendStandby = 26;
    static final int TRANSACTION_sendVendorCommand = 24;
    static final int TRANSACTION_setArcMode = 17;
    static final int TRANSACTION_setHdmiRecordListener = 27;
    static final int TRANSACTION_setInputChangeListener = 21;
    static final int TRANSACTION_setProhibitMode = 18;
    static final int TRANSACTION_setStandbyMode = 34;
    static final int TRANSACTION_setSystemAudioMode = 14;
    static final int TRANSACTION_setSystemAudioMute = 20;
    static final int TRANSACTION_setSystemAudioVolume = 19;
    static final int TRANSACTION_startOneTouchRecord = 28;
    static final int TRANSACTION_startTimerRecording = 30;
    static final int TRANSACTION_stopOneTouchRecord = 29;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.hdmi.IHdmiControlService");
    }
    
    public static IHdmiControlService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.hdmi.IHdmiControlService");
      if ((localIInterface != null) && ((localIInterface instanceof IHdmiControlService))) {
        return (IHdmiControlService)localIInterface;
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
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 34: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          setStandbyMode(bool7);
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          sendMhlVendorCommand(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          clearTimerRecording(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          startTimerRecording(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          stopOneTouchRecord(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          startOneTouchRecord(paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          setHdmiRecordListener(IHdmiRecordListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          sendStandby(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          addVendorCommandListener(IHdmiVendorCommandListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          byte[] arrayOfByte = paramParcel1.createByteArray();
          bool7 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          sendVendorCommand(paramInt2, paramInt1, arrayOfByte, bool7);
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramParcel1 = getDeviceList();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramParcel1 = getInputDevices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          setInputChangeListener(IHdmiInputChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          bool7 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          setSystemAudioMute(bool7);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          setSystemAudioVolume(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          bool7 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          setProhibitMode(bool7);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          bool7 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          setArcMode(bool7);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          addSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          bool7 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          setSystemAudioMode(bool7, IHdmiControlCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramInt1 = getSystemAudioMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramInt1 = canChangeSystemAudioMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramParcel1 = getPortInfo();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          bool7 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          sendKeyEvent(paramInt2, paramInt1, bool7);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          portSelect(paramParcel1.readInt(), IHdmiControlCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          deviceSelect(paramParcel1.readInt(), IHdmiControlCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          addDeviceEventListener(IHdmiDeviceEventListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          removeHotplugEventListener(IHdmiHotplugEventListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          addHotplugEventListener(IHdmiHotplugEventListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          queryDisplayStatus(IHdmiControlCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          oneTouchPlay(IHdmiControlCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
          paramParcel1 = getActiveSource();
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
        }
        paramParcel1.enforceInterface("android.hardware.hdmi.IHdmiControlService");
        paramParcel1 = getSupportedTypes();
        paramParcel2.writeNoException();
        paramParcel2.writeIntArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.hdmi.IHdmiControlService");
      return true;
    }
    
    private static class Proxy
      implements IHdmiControlService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addDeviceEventListener(IHdmiDeviceEventListener paramIHdmiDeviceEventListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiDeviceEventListener != null) {
            paramIHdmiDeviceEventListener = paramIHdmiDeviceEventListener.asBinder();
          } else {
            paramIHdmiDeviceEventListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiDeviceEventListener);
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
      
      public void addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener paramIHdmiMhlVendorCommandListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiMhlVendorCommandListener != null) {
            paramIHdmiMhlVendorCommandListener = paramIHdmiMhlVendorCommandListener.asBinder();
          } else {
            paramIHdmiMhlVendorCommandListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiMhlVendorCommandListener);
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
      
      public void addHotplugEventListener(IHdmiHotplugEventListener paramIHdmiHotplugEventListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiHotplugEventListener != null) {
            paramIHdmiHotplugEventListener = paramIHdmiHotplugEventListener.asBinder();
          } else {
            paramIHdmiHotplugEventListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiHotplugEventListener);
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
      
      public void addSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener paramIHdmiSystemAudioModeChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiSystemAudioModeChangeListener != null) {
            paramIHdmiSystemAudioModeChangeListener = paramIHdmiSystemAudioModeChangeListener.asBinder();
          } else {
            paramIHdmiSystemAudioModeChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiSystemAudioModeChangeListener);
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
      
      public void addVendorCommandListener(IHdmiVendorCommandListener paramIHdmiVendorCommandListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiVendorCommandListener != null) {
            paramIHdmiVendorCommandListener = paramIHdmiVendorCommandListener.asBinder();
          } else {
            paramIHdmiVendorCommandListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiVendorCommandListener);
          localParcel1.writeInt(paramInt);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean canChangeSystemAudioMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearTimerRecording(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void deviceSelect(int paramInt, IHdmiControlCallback paramIHdmiControlCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt);
          if (paramIHdmiControlCallback != null) {
            paramIHdmiControlCallback = paramIHdmiControlCallback.asBinder();
          } else {
            paramIHdmiControlCallback = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiControlCallback);
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
      
      public HdmiDeviceInfo getActiveSource()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          HdmiDeviceInfo localHdmiDeviceInfo;
          if (localParcel2.readInt() != 0) {
            localHdmiDeviceInfo = (HdmiDeviceInfo)HdmiDeviceInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localHdmiDeviceInfo = null;
          }
          return localHdmiDeviceInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<HdmiDeviceInfo> getDeviceList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(HdmiDeviceInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<HdmiDeviceInfo> getInputDevices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(HdmiDeviceInfo.CREATOR);
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
        return "android.hardware.hdmi.IHdmiControlService";
      }
      
      public List<HdmiPortInfo> getPortInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(HdmiPortInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getSupportedTypes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public boolean getSystemAudioMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void oneTouchPlay(IHdmiControlCallback paramIHdmiControlCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiControlCallback != null) {
            paramIHdmiControlCallback = paramIHdmiControlCallback.asBinder();
          } else {
            paramIHdmiControlCallback = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiControlCallback);
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
      
      public void portSelect(int paramInt, IHdmiControlCallback paramIHdmiControlCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt);
          if (paramIHdmiControlCallback != null) {
            paramIHdmiControlCallback = paramIHdmiControlCallback.asBinder();
          } else {
            paramIHdmiControlCallback = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiControlCallback);
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
      
      public void queryDisplayStatus(IHdmiControlCallback paramIHdmiControlCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiControlCallback != null) {
            paramIHdmiControlCallback = paramIHdmiControlCallback.asBinder();
          } else {
            paramIHdmiControlCallback = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiControlCallback);
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
      
      public void removeHotplugEventListener(IHdmiHotplugEventListener paramIHdmiHotplugEventListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiHotplugEventListener != null) {
            paramIHdmiHotplugEventListener = paramIHdmiHotplugEventListener.asBinder();
          } else {
            paramIHdmiHotplugEventListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiHotplugEventListener);
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
      
      public void removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener paramIHdmiSystemAudioModeChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiSystemAudioModeChangeListener != null) {
            paramIHdmiSystemAudioModeChangeListener = paramIHdmiSystemAudioModeChangeListener.asBinder();
          } else {
            paramIHdmiSystemAudioModeChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiSystemAudioModeChangeListener);
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
      
      public void sendKeyEvent(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void sendMhlVendorCommand(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void sendStandby(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void sendVendorCommand(int paramInt1, int paramInt2, byte[] paramArrayOfByte, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void setArcMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setHdmiRecordListener(IHdmiRecordListener paramIHdmiRecordListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiRecordListener != null) {
            paramIHdmiRecordListener = paramIHdmiRecordListener.asBinder();
          } else {
            paramIHdmiRecordListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiRecordListener);
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
      
      public void setInputChangeListener(IHdmiInputChangeListener paramIHdmiInputChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          if (paramIHdmiInputChangeListener != null) {
            paramIHdmiInputChangeListener = paramIHdmiInputChangeListener.asBinder();
          } else {
            paramIHdmiInputChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiInputChangeListener);
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
      
      public void setProhibitMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setStandbyMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSystemAudioMode(boolean paramBoolean, IHdmiControlCallback paramIHdmiControlCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramBoolean);
          if (paramIHdmiControlCallback != null) {
            paramIHdmiControlCallback = paramIHdmiControlCallback.asBinder();
          } else {
            paramIHdmiControlCallback = null;
          }
          localParcel1.writeStrongBinder(paramIHdmiControlCallback);
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
      
      public void setSystemAudioMute(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setSystemAudioVolume(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void startOneTouchRecord(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void startTimerRecording(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void stopOneTouchRecord(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.hdmi.IHdmiControlService");
          localParcel1.writeInt(paramInt);
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
    }
  }
}
