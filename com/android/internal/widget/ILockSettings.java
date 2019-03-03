package com.android.internal.widget;

import android.app.PendingIntent;
import android.app.trust.IStrongAuthTracker;
import android.app.trust.IStrongAuthTracker.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.security.keystore.recovery.KeyChainProtectionParams;
import android.security.keystore.recovery.KeyChainSnapshot;
import android.security.keystore.recovery.RecoveryCertPath;
import android.security.keystore.recovery.WrappedApplicationKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract interface ILockSettings
  extends IInterface
{
  public abstract VerifyCredentialResponse checkCredential(String paramString, int paramInt1, int paramInt2, ICheckCredentialProgressCallback paramICheckCredentialProgressCallback)
    throws RemoteException;
  
  public abstract boolean checkVoldPassword(int paramInt)
    throws RemoteException;
  
  public abstract void closeSession(String paramString)
    throws RemoteException;
  
  public abstract String generateKey(String paramString)
    throws RemoteException;
  
  public abstract String getAsusString(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean getBoolean(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract byte[] getHashFactor(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract String getKey(String paramString)
    throws RemoteException;
  
  public abstract KeyChainSnapshot getKeyChainSnapshot()
    throws RemoteException;
  
  public abstract long getLong(String paramString, long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract String getPassword()
    throws RemoteException;
  
  public abstract int[] getRecoverySecretTypes()
    throws RemoteException;
  
  public abstract Map getRecoveryStatus()
    throws RemoteException;
  
  public abstract boolean getSeparateProfileChallengeEnabled(int paramInt)
    throws RemoteException;
  
  public abstract String getString(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract int getStrongAuthForUser(int paramInt)
    throws RemoteException;
  
  public abstract boolean havePassword(int paramInt)
    throws RemoteException;
  
  public abstract boolean havePattern(int paramInt)
    throws RemoteException;
  
  public abstract String importKey(String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void initRecoveryServiceWithSigFile(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract Map recoverKeyChainSnapshot(String paramString, byte[] paramArrayOfByte, List<WrappedApplicationKey> paramList)
    throws RemoteException;
  
  public abstract void registerStrongAuthTracker(IStrongAuthTracker paramIStrongAuthTracker)
    throws RemoteException;
  
  public abstract void removeKey(String paramString)
    throws RemoteException;
  
  public abstract void requireStrongAuth(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void resetKeyStore(int paramInt)
    throws RemoteException;
  
  public abstract void sanitizePassword()
    throws RemoteException;
  
  public abstract void setAsusString(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setBoolean(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setLockCredential(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setLong(String paramString, long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void setRecoverySecretTypes(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void setRecoveryStatus(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setSeparateProfileChallengeEnabled(int paramInt, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void setServerParams(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void setSnapshotCreatedPendingIntent(PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void setString(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract byte[] startRecoverySessionWithCertPath(String paramString1, String paramString2, RecoveryCertPath paramRecoveryCertPath, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, List<KeyChainProtectionParams> paramList)
    throws RemoteException;
  
  public abstract void systemReady()
    throws RemoteException;
  
  public abstract void unregisterStrongAuthTracker(IStrongAuthTracker paramIStrongAuthTracker)
    throws RemoteException;
  
  public abstract void userPresent(int paramInt)
    throws RemoteException;
  
  public abstract VerifyCredentialResponse verifyCredential(String paramString, int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public abstract VerifyCredentialResponse verifyTiedProfileChallenge(String paramString, int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILockSettings
  {
    private static final String DESCRIPTOR = "com.android.internal.widget.ILockSettings";
    static final int TRANSACTION_checkCredential = 9;
    static final int TRANSACTION_checkVoldPassword = 12;
    static final int TRANSACTION_closeSession = 38;
    static final int TRANSACTION_generateKey = 26;
    static final int TRANSACTION_getAsusString = 42;
    static final int TRANSACTION_getBoolean = 4;
    static final int TRANSACTION_getHashFactor = 15;
    static final int TRANSACTION_getKey = 28;
    static final int TRANSACTION_getKeyChainSnapshot = 25;
    static final int TRANSACTION_getLong = 5;
    static final int TRANSACTION_getPassword = 40;
    static final int TRANSACTION_getRecoverySecretTypes = 35;
    static final int TRANSACTION_getRecoveryStatus = 33;
    static final int TRANSACTION_getSeparateProfileChallengeEnabled = 17;
    static final int TRANSACTION_getString = 6;
    static final int TRANSACTION_getStrongAuthForUser = 23;
    static final int TRANSACTION_havePassword = 14;
    static final int TRANSACTION_havePattern = 13;
    static final int TRANSACTION_importKey = 27;
    static final int TRANSACTION_initRecoveryServiceWithSigFile = 24;
    static final int TRANSACTION_recoverKeyChainSnapshot = 37;
    static final int TRANSACTION_registerStrongAuthTracker = 18;
    static final int TRANSACTION_removeKey = 29;
    static final int TRANSACTION_requireStrongAuth = 20;
    static final int TRANSACTION_resetKeyStore = 8;
    static final int TRANSACTION_sanitizePassword = 39;
    static final int TRANSACTION_setAsusString = 41;
    static final int TRANSACTION_setBoolean = 1;
    static final int TRANSACTION_setLockCredential = 7;
    static final int TRANSACTION_setLong = 2;
    static final int TRANSACTION_setRecoverySecretTypes = 34;
    static final int TRANSACTION_setRecoveryStatus = 32;
    static final int TRANSACTION_setSeparateProfileChallengeEnabled = 16;
    static final int TRANSACTION_setServerParams = 31;
    static final int TRANSACTION_setSnapshotCreatedPendingIntent = 30;
    static final int TRANSACTION_setString = 3;
    static final int TRANSACTION_startRecoverySessionWithCertPath = 36;
    static final int TRANSACTION_systemReady = 21;
    static final int TRANSACTION_unregisterStrongAuthTracker = 19;
    static final int TRANSACTION_userPresent = 22;
    static final int TRANSACTION_verifyCredential = 10;
    static final int TRANSACTION_verifyTiedProfileChallenge = 11;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.widget.ILockSettings");
    }
    
    public static ILockSettings asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.widget.ILockSettings");
      if ((localIInterface != null) && ((localIInterface instanceof ILockSettings))) {
        return (ILockSettings)localIInterface;
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
        Object localObject = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 42: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getAsusString(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          setAsusString(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getPassword();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          sanitizePassword();
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          closeSession(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = recoverKeyChainSnapshot(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.createTypedArrayList(WrappedApplicationKey.CREATOR));
          paramParcel2.writeNoException();
          paramParcel2.writeMap(paramParcel1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          str1 = paramParcel1.readString();
          String str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject = (RecoveryCertPath)RecoveryCertPath.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = startRecoverySessionWithCertPath(str1, str2, (RecoveryCertPath)localObject, paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.createTypedArrayList(KeyChainProtectionParams.CREATOR));
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getRecoverySecretTypes();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          setRecoverySecretTypes(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getRecoveryStatus();
          paramParcel2.writeNoException();
          paramParcel2.writeMap(paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          setRecoveryStatus(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          setServerParams(paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          setSnapshotCreatedPendingIntent(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          removeKey(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getKey(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = importKey(paramParcel1.readString(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = generateKey(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getKeyChainSnapshot();
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
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          initRecoveryServiceWithSigFile(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramInt1 = getStrongAuthForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          userPresent(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          systemReady();
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          requireStrongAuth(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          unregisterStrongAuthTracker(IStrongAuthTracker.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          registerStrongAuthTracker(IStrongAuthTracker.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramInt1 = getSeparateProfileChallengeEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          setSeparateProfileChallengeEnabled(paramInt1, bool3, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getHashFactor(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramInt1 = havePassword(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramInt1 = havePattern(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramInt1 = checkVoldPassword(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = verifyTiedProfileChallenge(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt());
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
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = verifyCredential(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt());
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
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = checkCredential(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ICheckCredentialProgressCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
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
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          resetKeyStore(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          setLockCredential(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          paramParcel1 = getString(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          long l = getLong(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          localObject = paramParcel1.readString();
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          paramInt1 = getBoolean((String)localObject, bool3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          setString(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
          setLong(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.widget.ILockSettings");
        localObject = paramParcel1.readString();
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        setBoolean((String)localObject, bool3, paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.widget.ILockSettings");
      return true;
    }
    
    private static class Proxy
      implements ILockSettings
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
      
      public VerifyCredentialResponse checkCredential(String paramString, int paramInt1, int paramInt2, ICheckCredentialProgressCallback paramICheckCredentialProgressCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          Object localObject = null;
          if (paramICheckCredentialProgressCallback != null) {
            paramString = paramICheckCredentialProgressCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (VerifyCredentialResponse)VerifyCredentialResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = localObject;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean checkVoldPassword(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(12, localParcel1, localParcel2, 0);
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
      
      public void closeSession(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String generateKey(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
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
      
      public String getAsusString(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readString();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getBoolean(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public byte[] getHashFactor(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
        return "com.android.internal.widget.ILockSettings";
      }
      
      public String getKey(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public KeyChainSnapshot getKeyChainSnapshot()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          KeyChainSnapshot localKeyChainSnapshot;
          if (localParcel2.readInt() != 0) {
            localKeyChainSnapshot = (KeyChainSnapshot)KeyChainSnapshot.CREATOR.createFromParcel(localParcel2);
          } else {
            localKeyChainSnapshot = null;
          }
          return localKeyChainSnapshot;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getLong(String paramString, long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramLong = localParcel2.readLong();
          return paramLong;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getPassword()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getRecoverySecretTypes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      public Map getRecoveryStatus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          HashMap localHashMap = localParcel2.readHashMap(getClass().getClassLoader());
          return localHashMap;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getSeparateProfileChallengeEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
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
      
      public String getString(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readString();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getStrongAuthForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
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
      
      public boolean havePassword(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(14, localParcel1, localParcel2, 0);
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
      
      public boolean havePattern(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(13, localParcel1, localParcel2, 0);
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
      
      public String importKey(String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(27, localParcel1, localParcel2, 0);
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
      
      public void initRecoveryServiceWithSigFile(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
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
      
      public Map recoverKeyChainSnapshot(String paramString, byte[] paramArrayOfByte, List<WrappedApplicationKey> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readHashMap(getClass().getClassLoader());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerStrongAuthTracker(IStrongAuthTracker paramIStrongAuthTracker)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          if (paramIStrongAuthTracker != null) {
            paramIStrongAuthTracker = paramIStrongAuthTracker.asBinder();
          } else {
            paramIStrongAuthTracker = null;
          }
          localParcel1.writeStrongBinder(paramIStrongAuthTracker);
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
      
      public void removeKey(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
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
      
      public void requireStrongAuth(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void resetKeyStore(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
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
      
      public void sanitizePassword()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAsusString(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBoolean(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
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
      
      public void setLockCredential(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void setLong(String paramString, long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
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
      
      public void setRecoverySecretTypes(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeIntArray(paramArrayOfInt);
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
      
      public void setRecoveryStatus(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void setSeparateProfileChallengeEnabled(int paramInt, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
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
      
      public void setServerParams(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
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
      
      public void setSnapshotCreatedPendingIntent(PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setString(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
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
      
      public byte[] startRecoverySessionWithCertPath(String paramString1, String paramString2, RecoveryCertPath paramRecoveryCertPath, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, List<KeyChainProtectionParams> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramRecoveryCertPath != null)
          {
            localParcel1.writeInt(1);
            paramRecoveryCertPath.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.createByteArray();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void systemReady()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
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
      
      public void unregisterStrongAuthTracker(IStrongAuthTracker paramIStrongAuthTracker)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          if (paramIStrongAuthTracker != null) {
            paramIStrongAuthTracker = paramIStrongAuthTracker.asBinder();
          } else {
            paramIStrongAuthTracker = null;
          }
          localParcel1.writeStrongBinder(paramIStrongAuthTracker);
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
      
      public void userPresent(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeInt(paramInt);
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
      
      public VerifyCredentialResponse verifyCredential(String paramString, int paramInt1, long paramLong, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (VerifyCredentialResponse)VerifyCredentialResponse.CREATOR.createFromParcel(localParcel2);
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
      
      public VerifyCredentialResponse verifyTiedProfileChallenge(String paramString, int paramInt1, long paramLong, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.ILockSettings");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (VerifyCredentialResponse)VerifyCredentialResponse.CREATOR.createFromParcel(localParcel2);
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
    }
  }
}
