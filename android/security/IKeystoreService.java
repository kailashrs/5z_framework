package android.security;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterBlob;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keymaster.OperationResult;

public abstract interface IKeystoreService
  extends IInterface
{
  public abstract int abort(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int addAuthToken(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract int addRngEntropy(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract int attestDeviceIds(KeymasterArguments paramKeymasterArguments, KeymasterCertificateChain paramKeymasterCertificateChain)
    throws RemoteException;
  
  public abstract int attestKey(String paramString, KeymasterArguments paramKeymasterArguments, KeymasterCertificateChain paramKeymasterCertificateChain)
    throws RemoteException;
  
  public abstract OperationResult begin(IBinder paramIBinder, String paramString, int paramInt1, boolean paramBoolean, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte, int paramInt2)
    throws RemoteException;
  
  public abstract int cancelConfirmationPrompt(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int clear_uid(long paramLong)
    throws RemoteException;
  
  public abstract int del(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int exist(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ExportResult exportKey(String paramString, int paramInt1, KeymasterBlob paramKeymasterBlob1, KeymasterBlob paramKeymasterBlob2, int paramInt2)
    throws RemoteException;
  
  public abstract OperationResult finish(IBinder paramIBinder, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract int generate(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, KeystoreArguments paramKeystoreArguments)
    throws RemoteException;
  
  public abstract int generateKey(String paramString, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte, int paramInt1, int paramInt2, KeyCharacteristics paramKeyCharacteristics)
    throws RemoteException;
  
  public abstract byte[] get(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getKeyCharacteristics(String paramString, KeymasterBlob paramKeymasterBlob1, KeymasterBlob paramKeymasterBlob2, int paramInt, KeyCharacteristics paramKeyCharacteristics)
    throws RemoteException;
  
  public abstract int getState(int paramInt)
    throws RemoteException;
  
  public abstract byte[] get_pubkey(String paramString)
    throws RemoteException;
  
  public abstract long getmtime(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract String grant(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int importKey(String paramString, KeymasterArguments paramKeymasterArguments, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, KeyCharacteristics paramKeyCharacteristics)
    throws RemoteException;
  
  public abstract int importWrappedKey(String paramString1, byte[] paramArrayOfByte1, String paramString2, byte[] paramArrayOfByte2, KeymasterArguments paramKeymasterArguments, long paramLong1, long paramLong2, KeyCharacteristics paramKeyCharacteristics)
    throws RemoteException;
  
  public abstract int import_key(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int insert(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean isConfirmationPromptSupported()
    throws RemoteException;
  
  public abstract int isEmpty(int paramInt)
    throws RemoteException;
  
  public abstract boolean isOperationAuthorized(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int is_hardware_backed(String paramString)
    throws RemoteException;
  
  public abstract String[] list(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int lock(int paramInt)
    throws RemoteException;
  
  public abstract int onDeviceOffBody()
    throws RemoteException;
  
  public abstract int onKeyguardVisibilityChanged(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract int onUserAdded(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int onUserPasswordChanged(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int onUserRemoved(int paramInt)
    throws RemoteException;
  
  public abstract int presentConfirmationPrompt(IBinder paramIBinder, String paramString1, byte[] paramArrayOfByte, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract int reset()
    throws RemoteException;
  
  public abstract byte[] sign(String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract int ungrant(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int unlock(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract OperationResult update(IBinder paramIBinder, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract int verify(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeystoreService
  {
    private static final String DESCRIPTOR = "android.security.IKeystoreService";
    static final int TRANSACTION_abort = 30;
    static final int TRANSACTION_addAuthToken = 32;
    static final int TRANSACTION_addRngEntropy = 22;
    static final int TRANSACTION_attestDeviceIds = 36;
    static final int TRANSACTION_attestKey = 35;
    static final int TRANSACTION_begin = 27;
    static final int TRANSACTION_cancelConfirmationPrompt = 40;
    static final int TRANSACTION_clear_uid = 21;
    static final int TRANSACTION_del = 4;
    static final int TRANSACTION_exist = 5;
    static final int TRANSACTION_exportKey = 26;
    static final int TRANSACTION_finish = 29;
    static final int TRANSACTION_generate = 12;
    static final int TRANSACTION_generateKey = 23;
    static final int TRANSACTION_get = 2;
    static final int TRANSACTION_getKeyCharacteristics = 24;
    static final int TRANSACTION_getState = 1;
    static final int TRANSACTION_get_pubkey = 16;
    static final int TRANSACTION_getmtime = 19;
    static final int TRANSACTION_grant = 17;
    static final int TRANSACTION_importKey = 25;
    static final int TRANSACTION_importWrappedKey = 38;
    static final int TRANSACTION_import_key = 13;
    static final int TRANSACTION_insert = 3;
    static final int TRANSACTION_isConfirmationPromptSupported = 41;
    static final int TRANSACTION_isEmpty = 11;
    static final int TRANSACTION_isOperationAuthorized = 31;
    static final int TRANSACTION_is_hardware_backed = 20;
    static final int TRANSACTION_list = 6;
    static final int TRANSACTION_lock = 9;
    static final int TRANSACTION_onDeviceOffBody = 37;
    static final int TRANSACTION_onKeyguardVisibilityChanged = 42;
    static final int TRANSACTION_onUserAdded = 33;
    static final int TRANSACTION_onUserPasswordChanged = 8;
    static final int TRANSACTION_onUserRemoved = 34;
    static final int TRANSACTION_presentConfirmationPrompt = 39;
    static final int TRANSACTION_reset = 7;
    static final int TRANSACTION_sign = 14;
    static final int TRANSACTION_ungrant = 18;
    static final int TRANSACTION_unlock = 10;
    static final int TRANSACTION_update = 28;
    static final int TRANSACTION_verify = 15;
    
    public Stub()
    {
      attachInterface(this, "android.security.IKeystoreService");
    }
    
    public static IKeystoreService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.security.IKeystoreService");
      if ((localIInterface != null) && ((localIInterface instanceof IKeystoreService))) {
        return (IKeystoreService)localIInterface;
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
        String str = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        long l1;
        int i;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 42: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          paramInt1 = onKeyguardVisibilityChanged(bool, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = isConfirmationPromptSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = cancelConfirmationPrompt(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = presentConfirmationPrompt(paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject7 = paramParcel1.readString();
          localObject4 = paramParcel1.createByteArray();
          str = paramParcel1.readString();
          localObject5 = paramParcel1.createByteArray();
          if (paramParcel1.readInt() != 0) {
            localObject10 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          l1 = paramParcel1.readLong();
          long l2 = paramParcel1.readLong();
          paramParcel1 = new KeyCharacteristics();
          paramInt1 = importWrappedKey((String)localObject7, (byte[])localObject4, str, (byte[])localObject5, (KeymasterArguments)localObject10, l1, l2, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = onDeviceOffBody();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          localObject10 = new KeymasterCertificateChain();
          paramInt1 = attestDeviceIds(paramParcel1, (KeymasterCertificateChain)localObject10);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          ((KeymasterCertificateChain)localObject10).writeToParcel(paramParcel2, 1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject10 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          localObject5 = new KeymasterCertificateChain();
          paramInt1 = attestKey((String)localObject10, paramParcel1, (KeymasterCertificateChain)localObject5);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          ((KeymasterCertificateChain)localObject5).writeToParcel(paramParcel2, 1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = onUserRemoved(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = onUserAdded(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = addAuthToken(paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = isOperationAuthorized(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = abort(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject5 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject10 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject2;
          }
          paramParcel1 = finish((IBinder)localObject5, (KeymasterArguments)localObject10, paramParcel1.createByteArray(), paramParcel1.createByteArray());
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
        case 28: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject5 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject10 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject3;
          }
          paramParcel1 = update((IBinder)localObject5, (KeymasterArguments)localObject10, paramParcel1.createByteArray());
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
        case 27: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject5 = paramParcel1.readStrongBinder();
          localObject7 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject10 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);; localObject10 = localObject4) {
            break;
          }
          paramParcel1 = begin((IBinder)localObject5, (String)localObject7, paramInt1, bool, (KeymasterArguments)localObject10, paramParcel1.createByteArray(), paramParcel1.readInt());
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
        case 26: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject7 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject10 = (KeymasterBlob)KeymasterBlob.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject5 = (KeymasterBlob)KeymasterBlob.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = exportKey((String)localObject7, paramInt1, (KeymasterBlob)localObject10, (KeymasterBlob)localObject5, paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject5 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (localObject10 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);; localObject10 = localObject6) {
            break;
          }
          paramInt2 = paramParcel1.readInt();
          localObject7 = paramParcel1.createByteArray();
          i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          paramParcel1 = new KeyCharacteristics();
          paramInt1 = importKey((String)localObject5, (KeymasterArguments)localObject10, paramInt2, (byte[])localObject7, i, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject10 = (KeymasterBlob)KeymasterBlob.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject5 = (KeymasterBlob)KeymasterBlob.CREATOR.createFromParcel(paramParcel1);; localObject5 = localObject7) {
            break;
          }
          paramInt1 = paramParcel1.readInt();
          paramParcel1 = new KeyCharacteristics();
          paramInt1 = getKeyCharacteristics((String)localObject4, (KeymasterBlob)localObject10, (KeymasterBlob)localObject5, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject5 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (localObject10 = (KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel1);; localObject10 = localObject8) {
            break;
          }
          localObject7 = paramParcel1.createByteArray();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          paramParcel1 = new KeyCharacteristics();
          paramInt1 = generateKey((String)localObject5, (KeymasterArguments)localObject10, (byte[])localObject7, paramInt1, paramInt2, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = addRngEntropy(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = clear_uid(paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = is_hardware_backed(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          l1 = getmtime(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = ungrant(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramParcel1 = grant(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramParcel1 = get_pubkey(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = verify(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramParcel1 = sign(paramParcel1.readString(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = import_key(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          localObject10 = paramParcel1.readString();
          i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          int j = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (KeystoreArguments)KeystoreArguments.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject9) {
            break;
          }
          paramInt1 = generate((String)localObject10, i, paramInt1, j, paramInt2, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = isEmpty(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = unlock(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = lock(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = onUserPasswordChanged(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = reset();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramParcel1 = list(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = exist(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = del(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramInt1 = insert(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.security.IKeystoreService");
          paramParcel1 = get(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.security.IKeystoreService");
        paramInt1 = getState(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.security.IKeystoreService");
      return true;
    }
    
    private static class Proxy
      implements IKeystoreService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int abort(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(30, localParcel1, localParcel2, 0);
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
      
      public int addAuthToken(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(32, localParcel1, localParcel2, 0);
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
      
      public int addRngEntropy(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public int attestDeviceIds(KeymasterArguments paramKeymasterArguments, KeymasterCertificateChain paramKeymasterCertificateChain)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(36, localParcel1, localParcel2, 0);
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
      
      public int attestKey(String paramString, KeymasterArguments paramKeymasterArguments, KeymasterCertificateChain paramKeymasterCertificateChain)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      public OperationResult begin(IBinder paramIBinder, String paramString, int paramInt1, boolean paramBoolean, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramBoolean);
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (OperationResult)OperationResult.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int cancelConfirmationPrompt(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(40, localParcel1, localParcel2, 0);
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
      
      public int clear_uid(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeLong(paramLong);
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
      
      public int del(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public int exist(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public ExportResult exportKey(String paramString, int paramInt1, KeymasterBlob paramKeymasterBlob1, KeymasterBlob paramKeymasterBlob2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          if (paramKeymasterBlob1 != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterBlob1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramKeymasterBlob2 != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterBlob2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ExportResult)ExportResult.CREATOR.createFromParcel(localParcel2);
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
      
      public OperationResult finish(IBinder paramIBinder, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (OperationResult)OperationResult.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int generate(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, KeystoreArguments paramKeystoreArguments)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          if (paramKeystoreArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeystoreArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(12, localParcel1, localParcel2, 0);
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
      
      public int generateKey(String paramString, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte, int paramInt1, int paramInt2, KeyCharacteristics paramKeyCharacteristics)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramKeyCharacteristics.readFromParcel(localParcel2);
          }
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] get(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.security.IKeystoreService";
      }
      
      public int getKeyCharacteristics(String paramString, KeymasterBlob paramKeymasterBlob1, KeymasterBlob paramKeymasterBlob2, int paramInt, KeyCharacteristics paramKeyCharacteristics)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          if (paramKeymasterBlob1 != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterBlob1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramKeymasterBlob2 != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterBlob2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramKeyCharacteristics.readFromParcel(localParcel2);
          }
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public byte[] get_pubkey(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          mRemote.transact(16, localParcel1, localParcel2, 0);
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
      
      public long getmtime(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
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
      
      public String grant(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
      
      public int importKey(String paramString, KeymasterArguments paramKeymasterArguments, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, KeyCharacteristics paramKeyCharacteristics)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramKeyCharacteristics.readFromParcel(localParcel2);
          }
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int importWrappedKey(String paramString1, byte[] paramArrayOfByte1, String paramString2, byte[] paramArrayOfByte2, KeymasterArguments paramKeymasterArguments, long paramLong1, long paramLong2, KeyCharacteristics paramKeyCharacteristics)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString1);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeString(paramString2);
          localParcel1.writeByteArray(paramArrayOfByte2);
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (localParcel2.readInt() != 0) {
            paramKeyCharacteristics.readFromParcel(localParcel2);
          }
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int import_key(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public int insert(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean isConfirmationPromptSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(41, localParcel1, localParcel2, 0);
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
      
      public int isEmpty(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeInt(paramInt);
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
      
      public boolean isOperationAuthorized(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(31, localParcel1, localParcel2, 0);
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
      
      public int is_hardware_backed(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
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
      
      public String[] list(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int lock(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
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
      
      public int onDeviceOffBody()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          mRemote.transact(37, localParcel1, localParcel2, 0);
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
      
      public int onKeyguardVisibilityChanged(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int onUserAdded(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(33, localParcel1, localParcel2, 0);
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
      
      public int onUserPasswordChanged(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public int onUserRemoved(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(34, localParcel1, localParcel2, 0);
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
      
      public int presentConfirmationPrompt(IBinder paramIBinder, String paramString1, byte[] paramArrayOfByte, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString1);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(39, localParcel1, localParcel2, 0);
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
      
      public int reset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
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
      
      public byte[] sign(String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public int ungrant(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
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
      
      public int unlock(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
      
      public OperationResult update(IBinder paramIBinder, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramKeymasterArguments != null)
          {
            localParcel1.writeInt(1);
            paramKeymasterArguments.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (OperationResult)OperationResult.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int verify(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.IKeystoreService");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
    }
  }
}
