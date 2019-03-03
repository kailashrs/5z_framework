package android.security;

import android.content.pm.StringParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keystore.ParcelableKeyGenParameterSpec;
import java.util.List;

public abstract interface IKeyChainService
  extends IInterface
{
  public abstract int attestKey(String paramString, byte[] paramArrayOfByte, int[] paramArrayOfInt, KeymasterCertificateChain paramKeymasterCertificateChain)
    throws RemoteException;
  
  public abstract boolean containsCaAlias(String paramString)
    throws RemoteException;
  
  public abstract boolean deleteCaCertificate(String paramString)
    throws RemoteException;
  
  public abstract int generateKeyPair(String paramString, ParcelableKeyGenParameterSpec paramParcelableKeyGenParameterSpec)
    throws RemoteException;
  
  public abstract List<String> getCaCertificateChainAliases(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract byte[] getCaCertificates(String paramString)
    throws RemoteException;
  
  public abstract byte[] getCertificate(String paramString)
    throws RemoteException;
  
  public abstract byte[] getEncodedCaCertificate(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract StringParceledListSlice getSystemCaAliases()
    throws RemoteException;
  
  public abstract StringParceledListSlice getUserCaAliases()
    throws RemoteException;
  
  public abstract boolean hasGrant(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String installCaCertificate(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean installKeyPair(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, String paramString)
    throws RemoteException;
  
  public abstract boolean isUserSelectable(String paramString)
    throws RemoteException;
  
  public abstract boolean removeKeyPair(String paramString)
    throws RemoteException;
  
  public abstract String requestPrivateKey(String paramString)
    throws RemoteException;
  
  public abstract boolean reset()
    throws RemoteException;
  
  public abstract void setGrant(int paramInt, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setKeyPairCertificate(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract void setUserSelectable(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyChainService
  {
    private static final String DESCRIPTOR = "android.security.IKeyChainService";
    static final int TRANSACTION_attestKey = 7;
    static final int TRANSACTION_containsCaAlias = 16;
    static final int TRANSACTION_deleteCaCertificate = 12;
    static final int TRANSACTION_generateKeyPair = 6;
    static final int TRANSACTION_getCaCertificateChainAliases = 18;
    static final int TRANSACTION_getCaCertificates = 3;
    static final int TRANSACTION_getCertificate = 2;
    static final int TRANSACTION_getEncodedCaCertificate = 17;
    static final int TRANSACTION_getSystemCaAliases = 15;
    static final int TRANSACTION_getUserCaAliases = 14;
    static final int TRANSACTION_hasGrant = 20;
    static final int TRANSACTION_installCaCertificate = 9;
    static final int TRANSACTION_installKeyPair = 10;
    static final int TRANSACTION_isUserSelectable = 4;
    static final int TRANSACTION_removeKeyPair = 11;
    static final int TRANSACTION_requestPrivateKey = 1;
    static final int TRANSACTION_reset = 13;
    static final int TRANSACTION_setGrant = 19;
    static final int TRANSACTION_setKeyPairCertificate = 8;
    static final int TRANSACTION_setUserSelectable = 5;
    
    public Stub()
    {
      attachInterface(this, "android.security.IKeyChainService");
    }
    
    public static IKeyChainService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.security.IKeyChainService");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyChainService))) {
        return (IKeyChainService)localIInterface;
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
        Object localObject;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 20: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = hasGrant(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = paramParcel1.readInt();
          localObject = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setGrant(paramInt1, (String)localObject, bool4);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          localObject = paramParcel1.readString();
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          paramParcel1 = getCaCertificateChainAliases((String)localObject, bool4);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          localObject = paramParcel1.readString();
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          paramParcel1 = getEncodedCaCertificate((String)localObject, bool4);
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = containsCaAlias(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramParcel1 = getSystemCaAliases();
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
        case 14: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramParcel1 = getUserCaAliases();
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
        case 13: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = reset();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = deleteCaCertificate(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = removeKeyPair(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = installKeyPair(paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramParcel1 = installCaCertificate(paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = setKeyPairCertificate(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          String str = paramParcel1.readString();
          localObject = paramParcel1.createByteArray();
          int[] arrayOfInt = paramParcel1.createIntArray();
          paramParcel1 = new KeymasterCertificateChain();
          paramInt1 = attestKey(str, (byte[])localObject, arrayOfInt, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          localObject = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelableKeyGenParameterSpec)ParcelableKeyGenParameterSpec.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          paramInt1 = generateKeyPair((String)localObject, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          localObject = paramParcel1.readString();
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setUserSelectable((String)localObject, bool4);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramInt1 = isUserSelectable(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramParcel1 = getCaCertificates(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.security.IKeyChainService");
          paramParcel1 = getCertificate(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.security.IKeyChainService");
        paramParcel1 = requestPrivateKey(paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeString(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.security.IKeyChainService");
      return true;
    }
    
    private static class Proxy
      implements IKeyChainService
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
      
      public int attestKey(String paramString, byte[] paramArrayOfByte, int[] paramArrayOfInt, KeymasterCertificateChain paramKeymasterCertificateChain)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramKeymasterCertificateChain.readFromParcel(localParcel2);
          }
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean containsCaAlias(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(16, localParcel1, localParcel2, 0);
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
      
      public boolean deleteCaCertificate(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(12, localParcel1, localParcel2, 0);
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
      
      public int generateKeyPair(String paramString, ParcelableKeyGenParameterSpec paramParcelableKeyGenParameterSpec)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          if (paramParcelableKeyGenParameterSpec != null)
          {
            localParcel1.writeInt(1);
            paramParcelableKeyGenParameterSpec.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
      
      public List<String> getCaCertificateChainAliases(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArrayList();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getCaCertificates(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createByteArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getCertificate(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createByteArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getEncodedCaCertificate(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createByteArray();
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
        return "android.security.IKeyChainService";
      }
      
      public StringParceledListSlice getSystemCaAliases()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          StringParceledListSlice localStringParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localStringParceledListSlice = (StringParceledListSlice)StringParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localStringParceledListSlice = null;
          }
          return localStringParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public StringParceledListSlice getUserCaAliases()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          StringParceledListSlice localStringParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localStringParceledListSlice = (StringParceledListSlice)StringParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localStringParceledListSlice = null;
          }
          return localStringParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasGrant(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(20, localParcel1, localParcel2, 0);
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
      
      public String installCaCertificate(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfByte = localParcel2.readString();
          return paramArrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean installKeyPair(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          localParcel1.writeByteArray(paramArrayOfByte3);
          localParcel1.writeString(paramString);
          paramArrayOfByte1 = mRemote;
          boolean bool = false;
          paramArrayOfByte1.transact(10, localParcel1, localParcel2, 0);
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
      
      public boolean isUserSelectable(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(4, localParcel1, localParcel2, 0);
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
      
      public boolean removeKeyPair(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(11, localParcel1, localParcel2, 0);
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
      
      public String requestPrivateKey(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean reset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
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
      
      public void setGrant(int paramInt, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public boolean setKeyPairCertificate(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
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
      
      public void setUserSelectable(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeyChainService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
    }
  }
}
