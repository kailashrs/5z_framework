package android.nfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INfcTag
  extends IInterface
{
  public abstract boolean canMakeReadOnly(int paramInt)
    throws RemoteException;
  
  public abstract int connect(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int formatNdef(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean getExtendedLengthApdusSupported()
    throws RemoteException;
  
  public abstract int getMaxTransceiveLength(int paramInt)
    throws RemoteException;
  
  public abstract int[] getTechList(int paramInt)
    throws RemoteException;
  
  public abstract int getTimeout(int paramInt)
    throws RemoteException;
  
  public abstract boolean isNdef(int paramInt)
    throws RemoteException;
  
  public abstract boolean isPresent(int paramInt)
    throws RemoteException;
  
  public abstract boolean ndefIsWritable(int paramInt)
    throws RemoteException;
  
  public abstract int ndefMakeReadOnly(int paramInt)
    throws RemoteException;
  
  public abstract NdefMessage ndefRead(int paramInt)
    throws RemoteException;
  
  public abstract int ndefWrite(int paramInt, NdefMessage paramNdefMessage)
    throws RemoteException;
  
  public abstract int reconnect(int paramInt)
    throws RemoteException;
  
  public abstract Tag rediscover(int paramInt)
    throws RemoteException;
  
  public abstract void resetTimeouts()
    throws RemoteException;
  
  public abstract int setTimeout(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract TransceiveResult transceive(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INfcTag
  {
    private static final String DESCRIPTOR = "android.nfc.INfcTag";
    static final int TRANSACTION_canMakeReadOnly = 16;
    static final int TRANSACTION_connect = 1;
    static final int TRANSACTION_formatNdef = 11;
    static final int TRANSACTION_getExtendedLengthApdusSupported = 18;
    static final int TRANSACTION_getMaxTransceiveLength = 17;
    static final int TRANSACTION_getTechList = 3;
    static final int TRANSACTION_getTimeout = 14;
    static final int TRANSACTION_isNdef = 4;
    static final int TRANSACTION_isPresent = 5;
    static final int TRANSACTION_ndefIsWritable = 10;
    static final int TRANSACTION_ndefMakeReadOnly = 9;
    static final int TRANSACTION_ndefRead = 7;
    static final int TRANSACTION_ndefWrite = 8;
    static final int TRANSACTION_reconnect = 2;
    static final int TRANSACTION_rediscover = 12;
    static final int TRANSACTION_resetTimeouts = 15;
    static final int TRANSACTION_setTimeout = 13;
    static final int TRANSACTION_transceive = 6;
    
    public Stub()
    {
      attachInterface(this, "android.nfc.INfcTag");
    }
    
    public static INfcTag asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.nfc.INfcTag");
      if ((localIInterface != null) && ((localIInterface instanceof INfcTag))) {
        return (INfcTag)localIInterface;
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
        case 18: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = getExtendedLengthApdusSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = getMaxTransceiveLength(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = canMakeReadOnly(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          resetTimeouts();
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = getTimeout(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = setTimeout(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramParcel1 = rediscover(paramParcel1.readInt());
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
        case 11: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = formatNdef(paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = ndefIsWritable(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = ndefMakeReadOnly(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NdefMessage)NdefMessage.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          paramInt1 = ndefWrite(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramParcel1 = ndefRead(paramParcel1.readInt());
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
        case 6: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = paramParcel1.readInt();
          byte[] arrayOfByte = paramParcel1.createByteArray();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramParcel1 = transceive(paramInt1, arrayOfByte, bool);
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
        case 5: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = isPresent(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = isNdef(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramParcel1 = getTechList(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.nfc.INfcTag");
          paramInt1 = reconnect(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.nfc.INfcTag");
        paramInt1 = connect(paramParcel1.readInt(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.nfc.INfcTag");
      return true;
    }
    
    private static class Proxy
      implements INfcTag
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
      
      public boolean canMakeReadOnly(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(16, localParcel1, localParcel2, 0);
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
      
      public int connect(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int formatNdef(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getExtendedLengthApdusSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(18, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.nfc.INfcTag";
      }
      
      public int getMaxTransceiveLength(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getTechList(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public int getTimeout(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isNdef(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
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
      
      public boolean isPresent(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
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
      
      public boolean ndefIsWritable(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
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
      
      public int ndefMakeReadOnly(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NdefMessage ndefRead(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NdefMessage localNdefMessage;
          if (localParcel2.readInt() != 0) {
            localNdefMessage = (NdefMessage)NdefMessage.CREATOR.createFromParcel(localParcel2);
          } else {
            localNdefMessage = null;
          }
          return localNdefMessage;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int ndefWrite(int paramInt, NdefMessage paramNdefMessage)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          if (paramNdefMessage != null)
          {
            localParcel1.writeInt(1);
            paramNdefMessage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int reconnect(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Tag rediscover(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Tag localTag;
          if (localParcel2.readInt() != 0) {
            localTag = (Tag)Tag.CREATOR.createFromParcel(localParcel2);
          } else {
            localTag = null;
          }
          return localTag;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resetTimeouts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
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
      
      public int setTimeout(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public TransceiveResult transceive(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcTag");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfByte = (TransceiveResult)TransceiveResult.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfByte = null;
          }
          return paramArrayOfByte;
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
