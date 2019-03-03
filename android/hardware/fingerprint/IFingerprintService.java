package android.hardware.fingerprint;

import android.hardware.biometrics.IBiometricPromptReceiver;
import android.hardware.biometrics.IBiometricPromptReceiver.Stub;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IFingerprintService
  extends IInterface
{
  public abstract void addClientActiveCallback(IFingerprintClientActiveCallback paramIFingerprintClientActiveCallback)
    throws RemoteException;
  
  public abstract void addLockoutResetCallback(IFingerprintServiceLockoutResetCallback paramIFingerprintServiceLockoutResetCallback)
    throws RemoteException;
  
  public abstract void authenticate(IBinder paramIBinder, long paramLong, int paramInt1, IFingerprintServiceReceiver paramIFingerprintServiceReceiver, int paramInt2, String paramString, Bundle paramBundle, IBiometricPromptReceiver paramIBiometricPromptReceiver)
    throws RemoteException;
  
  public abstract void cancelAuthentication(IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void cancelEnrollment(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void enroll(IBinder paramIBinder, byte[] paramArrayOfByte, int paramInt1, IFingerprintServiceReceiver paramIFingerprintServiceReceiver, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void enumerate(IBinder paramIBinder, int paramInt, IFingerprintServiceReceiver paramIFingerprintServiceReceiver)
    throws RemoteException;
  
  public abstract long getAuthenticatorId(String paramString)
    throws RemoteException;
  
  public abstract List<Fingerprint> getEnrolledFingerprints(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean hasEnrolledFingerprints(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isClientActive()
    throws RemoteException;
  
  public abstract boolean isHardwareDetected(long paramLong, String paramString)
    throws RemoteException;
  
  public abstract int postEnroll(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract long preEnroll(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void remove(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3, IFingerprintServiceReceiver paramIFingerprintServiceReceiver)
    throws RemoteException;
  
  public abstract void removeClientActiveCallback(IFingerprintClientActiveCallback paramIFingerprintClientActiveCallback)
    throws RemoteException;
  
  public abstract void rename(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void resetTimeout(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void setActiveUser(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IFingerprintService
  {
    private static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintService";
    static final int TRANSACTION_addClientActiveCallback = 18;
    static final int TRANSACTION_addLockoutResetCallback = 14;
    static final int TRANSACTION_authenticate = 1;
    static final int TRANSACTION_cancelAuthentication = 2;
    static final int TRANSACTION_cancelEnrollment = 4;
    static final int TRANSACTION_enroll = 3;
    static final int TRANSACTION_enumerate = 16;
    static final int TRANSACTION_getAuthenticatorId = 12;
    static final int TRANSACTION_getEnrolledFingerprints = 7;
    static final int TRANSACTION_hasEnrolledFingerprints = 11;
    static final int TRANSACTION_isClientActive = 17;
    static final int TRANSACTION_isHardwareDetected = 8;
    static final int TRANSACTION_postEnroll = 10;
    static final int TRANSACTION_preEnroll = 9;
    static final int TRANSACTION_remove = 5;
    static final int TRANSACTION_removeClientActiveCallback = 19;
    static final int TRANSACTION_rename = 6;
    static final int TRANSACTION_resetTimeout = 13;
    static final int TRANSACTION_setActiveUser = 15;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.fingerprint.IFingerprintService");
    }
    
    public static IFingerprintService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.fingerprint.IFingerprintService");
      if ((localIInterface != null) && ((localIInterface instanceof IFingerprintService))) {
        return (IFingerprintService)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 19: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          removeClientActiveCallback(IFingerprintClientActiveCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          addClientActiveCallback(IFingerprintClientActiveCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          paramInt1 = isClientActive();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          enumerate(paramParcel1.readStrongBinder(), paramParcel1.readInt(), IFingerprintServiceReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          setActiveUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          addLockoutResetCallback(IFingerprintServiceLockoutResetCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          resetTimeout(paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          l = getAuthenticatorId(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          paramInt1 = hasEnrolledFingerprints(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          paramInt1 = postEnroll(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          l = preEnroll(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          paramInt1 = isHardwareDetected(paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          paramParcel1 = getEnrolledFingerprints(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          rename(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          remove(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), IFingerprintServiceReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          cancelEnrollment(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          enroll(paramParcel1.readStrongBinder(), paramParcel1.createByteArray(), paramParcel1.readInt(), IFingerprintServiceReceiver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
          cancelAuthentication(paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.fingerprint.IFingerprintService");
        IBinder localIBinder = paramParcel1.readStrongBinder();
        long l = paramParcel1.readLong();
        paramInt1 = paramParcel1.readInt();
        IFingerprintServiceReceiver localIFingerprintServiceReceiver = IFingerprintServiceReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
        paramInt2 = paramParcel1.readInt();
        String str = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {}
        for (Bundle localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localBundle = null) {
          break;
        }
        authenticate(localIBinder, l, paramInt1, localIFingerprintServiceReceiver, paramInt2, str, localBundle, IBiometricPromptReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.hardware.fingerprint.IFingerprintService");
      return true;
    }
    
    private static class Proxy
      implements IFingerprintService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addClientActiveCallback(IFingerprintClientActiveCallback paramIFingerprintClientActiveCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          if (paramIFingerprintClientActiveCallback != null) {
            paramIFingerprintClientActiveCallback = paramIFingerprintClientActiveCallback.asBinder();
          } else {
            paramIFingerprintClientActiveCallback = null;
          }
          localParcel1.writeStrongBinder(paramIFingerprintClientActiveCallback);
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
      
      public void addLockoutResetCallback(IFingerprintServiceLockoutResetCallback paramIFingerprintServiceLockoutResetCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          if (paramIFingerprintServiceLockoutResetCallback != null) {
            paramIFingerprintServiceLockoutResetCallback = paramIFingerprintServiceLockoutResetCallback.asBinder();
          } else {
            paramIFingerprintServiceLockoutResetCallback = null;
          }
          localParcel1.writeStrongBinder(paramIFingerprintServiceLockoutResetCallback);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void authenticate(IBinder paramIBinder, long paramLong, int paramInt1, IFingerprintServiceReceiver paramIFingerprintServiceReceiver, int paramInt2, String paramString, Bundle paramBundle, IBiometricPromptReceiver paramIBiometricPromptReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt1);
          Object localObject = null;
          if (paramIFingerprintServiceReceiver != null) {
            paramIBinder = paramIFingerprintServiceReceiver.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          paramIBinder = localObject;
          if (paramIBiometricPromptReceiver != null) {
            paramIBinder = paramIBiometricPromptReceiver.asBinder();
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void cancelAuthentication(IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void cancelEnrollment(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void enroll(IBinder paramIBinder, byte[] paramArrayOfByte, int paramInt1, IFingerprintServiceReceiver paramIFingerprintServiceReceiver, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt1);
          if (paramIFingerprintServiceReceiver != null) {
            paramIBinder = paramIFingerprintServiceReceiver.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
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
      
      public void enumerate(IBinder paramIBinder, int paramInt, IFingerprintServiceReceiver paramIFingerprintServiceReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          if (paramIFingerprintServiceReceiver != null) {
            paramIBinder = paramIFingerprintServiceReceiver.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public long getAuthenticatorId(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeString(paramString);
          mRemote.transact(12, localParcel1, localParcel2, 0);
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
      
      public List<Fingerprint> getEnrolledFingerprints(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(Fingerprint.CREATOR);
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
        return "android.hardware.fingerprint.IFingerprintService";
      }
      
      public boolean hasEnrolledFingerprints(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(11, localParcel1, localParcel2, 0);
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
      
      public boolean isClientActive()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
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
      
      public boolean isHardwareDetected(long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(8, localParcel1, localParcel2, 0);
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
      
      public int postEnroll(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
      
      public long preEnroll(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public void remove(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3, IFingerprintServiceReceiver paramIFingerprintServiceReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          if (paramIFingerprintServiceReceiver != null) {
            paramIBinder = paramIFingerprintServiceReceiver.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void removeClientActiveCallback(IFingerprintClientActiveCallback paramIFingerprintClientActiveCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          if (paramIFingerprintClientActiveCallback != null) {
            paramIFingerprintClientActiveCallback = paramIFingerprintClientActiveCallback.asBinder();
          } else {
            paramIFingerprintClientActiveCallback = null;
          }
          localParcel1.writeStrongBinder(paramIFingerprintClientActiveCallback);
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
      
      public void rename(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void resetTimeout(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void setActiveUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.fingerprint.IFingerprintService");
          localParcel1.writeInt(paramInt);
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
    }
  }
}
