package android.hardware.usb;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IUsbManager
  extends IInterface
{
  public abstract void allowUsbDebugging(boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void clearDefaults(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void clearUsbDebuggingKeys()
    throws RemoteException;
  
  public abstract void denyUsbDebugging()
    throws RemoteException;
  
  public abstract ParcelFileDescriptor getControlFd(long paramLong)
    throws RemoteException;
  
  public abstract UsbAccessory getCurrentAccessory()
    throws RemoteException;
  
  public abstract long getCurrentFunctions()
    throws RemoteException;
  
  public abstract void getDeviceList(Bundle paramBundle)
    throws RemoteException;
  
  public abstract UsbPortStatus getPortStatus(String paramString)
    throws RemoteException;
  
  public abstract UsbPort[] getPorts()
    throws RemoteException;
  
  public abstract long getScreenUnlockedFunctions()
    throws RemoteException;
  
  public abstract void grantAccessoryPermission(UsbAccessory paramUsbAccessory, int paramInt)
    throws RemoteException;
  
  public abstract void grantDevicePermission(UsbDevice paramUsbDevice, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasAccessoryPermission(UsbAccessory paramUsbAccessory)
    throws RemoteException;
  
  public abstract boolean hasDefaults(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasDevicePermission(UsbDevice paramUsbDevice, String paramString)
    throws RemoteException;
  
  public abstract boolean isFunctionEnabled(String paramString)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openAccessory(UsbAccessory paramUsbAccessory)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openDevice(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void requestAccessoryPermission(UsbAccessory paramUsbAccessory, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void requestDevicePermission(UsbDevice paramUsbDevice, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void setAccessoryPackage(UsbAccessory paramUsbAccessory, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setCurrentFunction(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setCurrentFunctions(long paramLong)
    throws RemoteException;
  
  public abstract void setDevicePackage(UsbDevice paramUsbDevice, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setPortRoles(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setScreenUnlockedFunctions(long paramLong)
    throws RemoteException;
  
  public abstract void setUsbDeviceConnectionHandler(ComponentName paramComponentName)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUsbManager
  {
    private static final String DESCRIPTOR = "android.hardware.usb.IUsbManager";
    static final int TRANSACTION_allowUsbDebugging = 22;
    static final int TRANSACTION_clearDefaults = 14;
    static final int TRANSACTION_clearUsbDebuggingKeys = 24;
    static final int TRANSACTION_denyUsbDebugging = 23;
    static final int TRANSACTION_getControlFd = 21;
    static final int TRANSACTION_getCurrentAccessory = 3;
    static final int TRANSACTION_getCurrentFunctions = 18;
    static final int TRANSACTION_getDeviceList = 1;
    static final int TRANSACTION_getPortStatus = 26;
    static final int TRANSACTION_getPorts = 25;
    static final int TRANSACTION_getScreenUnlockedFunctions = 20;
    static final int TRANSACTION_grantAccessoryPermission = 12;
    static final int TRANSACTION_grantDevicePermission = 11;
    static final int TRANSACTION_hasAccessoryPermission = 8;
    static final int TRANSACTION_hasDefaults = 13;
    static final int TRANSACTION_hasDevicePermission = 7;
    static final int TRANSACTION_isFunctionEnabled = 15;
    static final int TRANSACTION_openAccessory = 4;
    static final int TRANSACTION_openDevice = 2;
    static final int TRANSACTION_requestAccessoryPermission = 10;
    static final int TRANSACTION_requestDevicePermission = 9;
    static final int TRANSACTION_setAccessoryPackage = 6;
    static final int TRANSACTION_setCurrentFunction = 17;
    static final int TRANSACTION_setCurrentFunctions = 16;
    static final int TRANSACTION_setDevicePackage = 5;
    static final int TRANSACTION_setPortRoles = 27;
    static final int TRANSACTION_setScreenUnlockedFunctions = 19;
    static final int TRANSACTION_setUsbDeviceConnectionHandler = 28;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.usb.IUsbManager");
    }
    
    public static IUsbManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.usb.IUsbManager");
      if ((localIInterface != null) && ((localIInterface instanceof IUsbManager))) {
        return (IUsbManager)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        String str1 = null;
        String str2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 28: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject8;
          }
          setUsbDeviceConnectionHandler(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          setPortRoles(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          paramParcel1 = getPortStatus(paramParcel1.readString());
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
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          paramParcel1 = getPorts();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          clearUsbDebuggingKeys();
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          denyUsbDebugging();
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          allowUsbDebugging(bool2, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          paramParcel1 = getControlFd(paramParcel1.readLong());
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
        case 20: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          l = getScreenUnlockedFunctions();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          setScreenUnlockedFunctions(paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          l = getCurrentFunctions();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          localObject8 = paramParcel1.readString();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setCurrentFunction((String)localObject8, bool2);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          setCurrentFunctions(paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          paramInt1 = isFunctionEnabled(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          clearDefaults(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          paramInt1 = hasDefaults(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UsbAccessory)UsbAccessory.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject1;
          }
          grantAccessoryPermission((UsbAccessory)localObject8, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UsbDevice)UsbDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject2;
          }
          grantDevicePermission((UsbDevice)localObject8, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UsbAccessory)UsbAccessory.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = null;
          }
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          requestAccessoryPermission((UsbAccessory)localObject8, str2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UsbDevice)UsbDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          requestDevicePermission((UsbDevice)localObject8, str1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UsbAccessory)UsbAccessory.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = hasAccessoryPermission(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UsbDevice)UsbDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject4;
          }
          paramInt1 = hasDevicePermission((UsbDevice)localObject8, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UsbAccessory)UsbAccessory.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject5;
          }
          setAccessoryPackage((UsbAccessory)localObject8, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (UsbDevice)UsbDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject6;
          }
          setDevicePackage((UsbDevice)localObject8, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UsbAccessory)UsbAccessory.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramParcel1 = openAccessory(paramParcel1);
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
        case 3: 
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          paramParcel1 = getCurrentAccessory();
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
          paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
          paramParcel1 = openDevice(paramParcel1.readString(), paramParcel1.readString());
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
        paramParcel1.enforceInterface("android.hardware.usb.IUsbManager");
        paramParcel1 = new Bundle();
        getDeviceList(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
        return true;
      }
      paramParcel2.writeString("android.hardware.usb.IUsbManager");
      return true;
    }
    
    private static class Proxy
      implements IUsbManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void allowUsbDebugging(boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void clearDefaults(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void clearUsbDebuggingKeys()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
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
      
      public void denyUsbDebugging()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
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
      
      public ParcelFileDescriptor getControlFd(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeLong(paramLong);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParcelFileDescriptor localParcelFileDescriptor;
          if (localParcel2.readInt() != 0) {
            localParcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            localParcelFileDescriptor = null;
          }
          return localParcelFileDescriptor;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public UsbAccessory getCurrentAccessory()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UsbAccessory localUsbAccessory;
          if (localParcel2.readInt() != 0) {
            localUsbAccessory = (UsbAccessory)UsbAccessory.CREATOR.createFromParcel(localParcel2);
          } else {
            localUsbAccessory = null;
          }
          return localUsbAccessory;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getCurrentFunctions()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getDeviceList(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramBundle.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.usb.IUsbManager";
      }
      
      public UsbPortStatus getPortStatus(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeString(paramString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UsbPortStatus)UsbPortStatus.CREATOR.createFromParcel(localParcel2);
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
      
      public UsbPort[] getPorts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UsbPort[] arrayOfUsbPort = (UsbPort[])localParcel2.createTypedArray(UsbPort.CREATOR);
          return arrayOfUsbPort;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getScreenUnlockedFunctions()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantAccessoryPermission(UsbAccessory paramUsbAccessory, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramUsbAccessory != null)
          {
            localParcel1.writeInt(1);
            paramUsbAccessory.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void grantDevicePermission(UsbDevice paramUsbDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramUsbDevice != null)
          {
            localParcel1.writeInt(1);
            paramUsbDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public boolean hasAccessoryPermission(UsbAccessory paramUsbAccessory)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          boolean bool = true;
          if (paramUsbAccessory != null)
          {
            localParcel1.writeInt(1);
            paramUsbAccessory.writeToParcel(localParcel1, 0);
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
      
      public boolean hasDefaults(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(13, localParcel1, localParcel2, 0);
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
      
      public boolean hasDevicePermission(UsbDevice paramUsbDevice, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          boolean bool = true;
          if (paramUsbDevice != null)
          {
            localParcel1.writeInt(1);
            paramUsbDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean isFunctionEnabled(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(15, localParcel1, localParcel2, 0);
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
      
      public ParcelFileDescriptor openAccessory(UsbAccessory paramUsbAccessory)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramUsbAccessory != null)
          {
            localParcel1.writeInt(1);
            paramUsbAccessory.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramUsbAccessory = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramUsbAccessory = null;
          }
          return paramUsbAccessory;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor openDevice(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestAccessoryPermission(UsbAccessory paramUsbAccessory, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramUsbAccessory != null)
          {
            localParcel1.writeInt(1);
            paramUsbAccessory.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
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
      
      public void requestDevicePermission(UsbDevice paramUsbDevice, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramUsbDevice != null)
          {
            localParcel1.writeInt(1);
            paramUsbDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setAccessoryPackage(UsbAccessory paramUsbAccessory, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramUsbAccessory != null)
          {
            localParcel1.writeInt(1);
            paramUsbAccessory.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void setCurrentFunction(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeString(paramString);
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
      
      public void setCurrentFunctions(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeLong(paramLong);
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
      
      public void setDevicePackage(UsbDevice paramUsbDevice, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramUsbDevice != null)
          {
            localParcel1.writeInt(1);
            paramUsbDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public void setPortRoles(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void setScreenUnlockedFunctions(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          localParcel1.writeLong(paramLong);
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
      
      public void setUsbDeviceConnectionHandler(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.usb.IUsbManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
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
    }
  }
}
