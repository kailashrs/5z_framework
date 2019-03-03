package android.hardware.input;

import android.app.IInputForwarder;
import android.app.IInputForwarder.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.PointerIcon;

public abstract interface IInputManager
  extends IInterface
{
  public abstract void addKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
    throws RemoteException;
  
  public abstract void cancelVibrate(int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract IInputForwarder createInputForwarder(int paramInt)
    throws RemoteException;
  
  public abstract void disableInputDevice(int paramInt)
    throws RemoteException;
  
  public abstract void enableInputDevice(int paramInt)
    throws RemoteException;
  
  public abstract String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
    throws RemoteException;
  
  public abstract String[] getEnabledKeyboardLayoutsForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
    throws RemoteException;
  
  public abstract InputDevice getInputDevice(int paramInt)
    throws RemoteException;
  
  public abstract int[] getInputDeviceIds()
    throws RemoteException;
  
  public abstract KeyboardLayout getKeyboardLayout(String paramString)
    throws RemoteException;
  
  public abstract KeyboardLayout[] getKeyboardLayouts()
    throws RemoteException;
  
  public abstract KeyboardLayout[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
    throws RemoteException;
  
  public abstract TouchCalibration getTouchCalibrationForInputDevice(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasKeys(int paramInt1, int paramInt2, int[] paramArrayOfInt, boolean[] paramArrayOfBoolean)
    throws RemoteException;
  
  public abstract boolean injectInputEvent(InputEvent paramInputEvent, int paramInt)
    throws RemoteException;
  
  public abstract int isInTabletMode()
    throws RemoteException;
  
  public abstract boolean isInputDeviceEnabled(int paramInt)
    throws RemoteException;
  
  public abstract void registerInputDevicesChangedListener(IInputDevicesChangedListener paramIInputDevicesChangedListener)
    throws RemoteException;
  
  public abstract void registerTabletModeChangedListener(ITabletModeChangedListener paramITabletModeChangedListener)
    throws RemoteException;
  
  public abstract void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
    throws RemoteException;
  
  public abstract void requestPointerCapture(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
    throws RemoteException;
  
  public abstract void setCustomPointerIcon(PointerIcon paramPointerIcon)
    throws RemoteException;
  
  public abstract void setPointerIconType(int paramInt)
    throws RemoteException;
  
  public abstract void setTouchCalibrationForInputDevice(String paramString, int paramInt, TouchCalibration paramTouchCalibration)
    throws RemoteException;
  
  public abstract void tryPointerSpeed(int paramInt)
    throws RemoteException;
  
  public abstract void vibrate(int paramInt1, long[] paramArrayOfLong, int paramInt2, IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputManager
  {
    private static final String DESCRIPTOR = "android.hardware.input.IInputManager";
    static final int TRANSACTION_addKeyboardLayoutForInputDevice = 17;
    static final int TRANSACTION_cancelVibrate = 23;
    static final int TRANSACTION_createInputForwarder = 27;
    static final int TRANSACTION_disableInputDevice = 5;
    static final int TRANSACTION_enableInputDevice = 4;
    static final int TRANSACTION_getCurrentKeyboardLayoutForInputDevice = 14;
    static final int TRANSACTION_getEnabledKeyboardLayoutsForInputDevice = 16;
    static final int TRANSACTION_getInputDevice = 1;
    static final int TRANSACTION_getInputDeviceIds = 2;
    static final int TRANSACTION_getKeyboardLayout = 13;
    static final int TRANSACTION_getKeyboardLayouts = 11;
    static final int TRANSACTION_getKeyboardLayoutsForInputDevice = 12;
    static final int TRANSACTION_getTouchCalibrationForInputDevice = 9;
    static final int TRANSACTION_hasKeys = 6;
    static final int TRANSACTION_injectInputEvent = 8;
    static final int TRANSACTION_isInTabletMode = 20;
    static final int TRANSACTION_isInputDeviceEnabled = 3;
    static final int TRANSACTION_registerInputDevicesChangedListener = 19;
    static final int TRANSACTION_registerTabletModeChangedListener = 21;
    static final int TRANSACTION_removeKeyboardLayoutForInputDevice = 18;
    static final int TRANSACTION_requestPointerCapture = 26;
    static final int TRANSACTION_setCurrentKeyboardLayoutForInputDevice = 15;
    static final int TRANSACTION_setCustomPointerIcon = 25;
    static final int TRANSACTION_setPointerIconType = 24;
    static final int TRANSACTION_setTouchCalibrationForInputDevice = 10;
    static final int TRANSACTION_tryPointerSpeed = 7;
    static final int TRANSACTION_vibrate = 22;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.input.IInputManager");
    }
    
    public static IInputManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.input.IInputManager");
      if ((localIInterface != null) && ((localIInterface instanceof IInputManager))) {
        return (IInputManager)localIInterface;
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
        IInputForwarder localIInputForwarder = null;
        Object localObject8 = null;
        Object localObject9 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 27: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          localIInputForwarder = createInputForwarder(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject9;
          if (localIInputForwarder != null) {
            paramParcel1 = localIInputForwarder.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          localObject9 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          requestPointerCapture((IBinder)localObject9, bool);
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PointerIcon)PointerIcon.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          setCustomPointerIcon(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          setPointerIconType(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          cancelVibrate(paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          vibrate(paramParcel1.readInt(), paramParcel1.createLongArray(), paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          registerTabletModeChangedListener(ITabletModeChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          paramInt1 = isInTabletMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          registerInputDevicesChangedListener(IInputDevicesChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (InputDeviceIdentifier)InputDeviceIdentifier.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject2;
          }
          removeKeyboardLayoutForInputDevice((InputDeviceIdentifier)localObject9, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (InputDeviceIdentifier)InputDeviceIdentifier.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject3;
          }
          addKeyboardLayoutForInputDevice((InputDeviceIdentifier)localObject9, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputDeviceIdentifier)InputDeviceIdentifier.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramParcel1 = getEnabledKeyboardLayoutsForInputDevice(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (InputDeviceIdentifier)InputDeviceIdentifier.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject5;
          }
          setCurrentKeyboardLayoutForInputDevice((InputDeviceIdentifier)localObject9, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputDeviceIdentifier)InputDeviceIdentifier.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramParcel1 = getCurrentKeyboardLayoutForInputDevice(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          paramParcel1 = getKeyboardLayout(paramParcel1.readString());
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
        case 12: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputDeviceIdentifier)InputDeviceIdentifier.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramParcel1 = getKeyboardLayoutsForInputDevice(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          paramParcel1 = getKeyboardLayouts();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          localObject9 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TouchCalibration)TouchCalibration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIInputForwarder;
          }
          setTouchCalibrationForInputDevice((String)localObject9, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          paramParcel1 = getTouchCalibrationForInputDevice(paramParcel1.readString(), paramParcel1.readInt());
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
        case 8: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          if (paramParcel1.readInt() != 0) {
            localObject9 = (InputEvent)InputEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = localObject8;
          }
          paramInt1 = injectInputEvent((InputEvent)localObject9, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          tryPointerSpeed(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          int i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          localObject9 = paramParcel1.createIntArray();
          paramInt2 = paramParcel1.readInt();
          if (paramInt2 < 0) {
            paramParcel1 = null;
          } else {
            paramParcel1 = new boolean[paramInt2];
          }
          paramInt1 = hasKeys(i, paramInt1, (int[])localObject9, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeBooleanArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          disableInputDevice(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          enableInputDevice(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          paramInt1 = isInputDeviceEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.input.IInputManager");
          paramParcel1 = getInputDeviceIds();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.input.IInputManager");
        paramParcel1 = getInputDevice(paramParcel1.readInt());
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
      paramParcel2.writeString("android.hardware.input.IInputManager");
      return true;
    }
    
    private static class Proxy
      implements IInputManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramInputDeviceIdentifier != null)
          {
            localParcel1.writeInt(1);
            paramInputDeviceIdentifier.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void cancelVibrate(int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public IInputForwarder createInputForwarder(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IInputForwarder localIInputForwarder = IInputForwarder.Stub.asInterface(localParcel2.readStrongBinder());
          return localIInputForwarder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableInputDevice(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
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
      
      public void enableInputDevice(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
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
      
      public String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramInputDeviceIdentifier != null)
          {
            localParcel1.writeInt(1);
            paramInputDeviceIdentifier.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInputDeviceIdentifier = localParcel2.readString();
          return paramInputDeviceIdentifier;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getEnabledKeyboardLayoutsForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramInputDeviceIdentifier != null)
          {
            localParcel1.writeInt(1);
            paramInputDeviceIdentifier.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInputDeviceIdentifier = localParcel2.createStringArray();
          return paramInputDeviceIdentifier;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public InputDevice getInputDevice(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          InputDevice localInputDevice;
          if (localParcel2.readInt() != 0) {
            localInputDevice = (InputDevice)InputDevice.CREATOR.createFromParcel(localParcel2);
          } else {
            localInputDevice = null;
          }
          return localInputDevice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getInputDeviceIds()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.input.IInputManager";
      }
      
      public KeyboardLayout getKeyboardLayout(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeString(paramString);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (KeyboardLayout)KeyboardLayout.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public KeyboardLayout[] getKeyboardLayouts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          KeyboardLayout[] arrayOfKeyboardLayout = (KeyboardLayout[])localParcel2.createTypedArray(KeyboardLayout.CREATOR);
          return arrayOfKeyboardLayout;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public KeyboardLayout[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramInputDeviceIdentifier != null)
          {
            localParcel1.writeInt(1);
            paramInputDeviceIdentifier.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInputDeviceIdentifier = (KeyboardLayout[])localParcel2.createTypedArray(KeyboardLayout.CREATOR);
          return paramInputDeviceIdentifier;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public TouchCalibration getTouchCalibrationForInputDevice(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (TouchCalibration)TouchCalibration.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasKeys(int paramInt1, int paramInt2, int[] paramArrayOfInt, boolean[] paramArrayOfBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeIntArray(paramArrayOfInt);
          if (paramArrayOfBoolean == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfBoolean.length);
          }
          paramArrayOfInt = mRemote;
          boolean bool = false;
          paramArrayOfInt.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            bool = true;
          }
          localParcel2.readBooleanArray(paramArrayOfBoolean);
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean injectInputEvent(InputEvent paramInputEvent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          boolean bool = true;
          if (paramInputEvent != null)
          {
            localParcel1.writeInt(1);
            paramInputEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public int isInTabletMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
      
      public boolean isInputDeviceEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void registerInputDevicesChangedListener(IInputDevicesChangedListener paramIInputDevicesChangedListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramIInputDevicesChangedListener != null) {
            paramIInputDevicesChangedListener = paramIInputDevicesChangedListener.asBinder();
          } else {
            paramIInputDevicesChangedListener = null;
          }
          localParcel1.writeStrongBinder(paramIInputDevicesChangedListener);
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
      
      public void registerTabletModeChangedListener(ITabletModeChangedListener paramITabletModeChangedListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramITabletModeChangedListener != null) {
            paramITabletModeChangedListener = paramITabletModeChangedListener.asBinder();
          } else {
            paramITabletModeChangedListener = null;
          }
          localParcel1.writeStrongBinder(paramITabletModeChangedListener);
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
      
      public void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramInputDeviceIdentifier != null)
          {
            localParcel1.writeInt(1);
            paramInputDeviceIdentifier.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public void requestPointerCapture(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier paramInputDeviceIdentifier, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramInputDeviceIdentifier != null)
          {
            localParcel1.writeInt(1);
            paramInputDeviceIdentifier.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public void setCustomPointerIcon(PointerIcon paramPointerIcon)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          if (paramPointerIcon != null)
          {
            localParcel1.writeInt(1);
            paramPointerIcon.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setPointerIconType(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
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
      
      public void setTouchCalibrationForInputDevice(String paramString, int paramInt, TouchCalibration paramTouchCalibration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramTouchCalibration != null)
          {
            localParcel1.writeInt(1);
            paramTouchCalibration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void tryPointerSpeed(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt);
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
      
      public void vibrate(int paramInt1, long[] paramArrayOfLong, int paramInt2, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.input.IInputManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLongArray(paramArrayOfLong);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStrongBinder(paramIBinder);
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
    }
  }
}
