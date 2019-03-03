package android.accounts;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAccountAuthenticator
  extends IInterface
{
  public abstract void addAccount(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void addAccountFromCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void confirmCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void editProperties(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString)
    throws RemoteException;
  
  public abstract void finishSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void getAccountCredentialsForCloning(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount)
    throws RemoteException;
  
  public abstract void getAccountRemovalAllowed(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount)
    throws RemoteException;
  
  public abstract void getAuthToken(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void getAuthTokenLabel(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString)
    throws RemoteException;
  
  public abstract void hasFeatures(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void isCredentialsUpdateSuggested(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract void startAddAccountSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void startUpdateCredentialsSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void updateCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccountAuthenticator
  {
    private static final String DESCRIPTOR = "android.accounts.IAccountAuthenticator";
    static final int TRANSACTION_addAccount = 1;
    static final int TRANSACTION_addAccountFromCredentials = 10;
    static final int TRANSACTION_confirmCredentials = 2;
    static final int TRANSACTION_editProperties = 6;
    static final int TRANSACTION_finishSession = 13;
    static final int TRANSACTION_getAccountCredentialsForCloning = 9;
    static final int TRANSACTION_getAccountRemovalAllowed = 8;
    static final int TRANSACTION_getAuthToken = 3;
    static final int TRANSACTION_getAuthTokenLabel = 4;
    static final int TRANSACTION_hasFeatures = 7;
    static final int TRANSACTION_isCredentialsUpdateSuggested = 14;
    static final int TRANSACTION_startAddAccountSession = 11;
    static final int TRANSACTION_startUpdateCredentialsSession = 12;
    static final int TRANSACTION_updateCredentials = 5;
    
    public Stub()
    {
      attachInterface(this, "android.accounts.IAccountAuthenticator");
    }
    
    public static IAccountAuthenticator asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.accounts.IAccountAuthenticator");
      if ((localIInterface != null) && ((localIInterface instanceof IAccountAuthenticator))) {
        return (IAccountAuthenticator)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 14: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject12;
          }
          isCredentialsUpdateSuggested((IAccountAuthenticatorResponse)localObject2, paramParcel2, paramParcel1.readString());
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          paramParcel2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          finishSession(paramParcel2, (String)localObject2, paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject8 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          startUpdateCredentialsSession((IAccountAuthenticatorResponse)localObject8, paramParcel2, (String)localObject9, paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject8 = paramParcel1.readString();
          paramParcel2 = paramParcel1.readString();
          localObject9 = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject3) {
            break;
          }
          startAddAccountSession((IAccountAuthenticatorResponse)localObject2, (String)localObject8, paramParcel2, (String[])localObject9, paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          addAccountFromCredentials((IAccountAuthenticatorResponse)localObject2, paramParcel2, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          paramParcel2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          getAccountCredentialsForCloning(paramParcel2, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          paramParcel2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          getAccountRemovalAllowed(paramParcel2, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject7;
          }
          hasFeatures((IAccountAuthenticatorResponse)localObject2, paramParcel2, paramParcel1.createStringArray());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          editProperties(IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject8;
          }
          updateCredentials((IAccountAuthenticatorResponse)localObject2, paramParcel2, (String)localObject9, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          getAuthTokenLabel(IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          localObject8 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject9;
          }
          getAuthToken((IAccountAuthenticatorResponse)localObject2, paramParcel2, (String)localObject8, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
          localObject2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          confirmCredentials((IAccountAuthenticatorResponse)localObject2, paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.accounts.IAccountAuthenticator");
        paramParcel2 = IAccountAuthenticatorResponse.Stub.asInterface(paramParcel1.readStrongBinder());
        localObject2 = paramParcel1.readString();
        localObject8 = paramParcel1.readString();
        localObject9 = paramParcel1.createStringArray();
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject11) {
          break;
        }
        addAccount(paramParcel2, (String)localObject2, (String)localObject8, (String[])localObject9, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.accounts.IAccountAuthenticator");
      return true;
    }
    
    private static class Proxy
      implements IAccountAuthenticator
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addAccount(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeStringArray(paramArrayOfString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void addAccountFromCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void confirmCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void editProperties(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          localParcel.writeString(paramString);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void finishSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getAccountCredentialsForCloning(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getAccountRemovalAllowed(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getAuthToken(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getAuthTokenLabel(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          localParcel.writeString(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.accounts.IAccountAuthenticator";
      }
      
      public void hasFeatures(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void isCredentialsUpdateSuggested(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startAddAccountSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, String paramString1, String paramString2, String[] paramArrayOfString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeStringArray(paramArrayOfString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startUpdateCredentialsSession(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateCredentials(IAccountAuthenticatorResponse paramIAccountAuthenticatorResponse, Account paramAccount, String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountAuthenticator");
          if (paramIAccountAuthenticatorResponse != null) {
            paramIAccountAuthenticatorResponse = paramIAccountAuthenticatorResponse.asBinder();
          } else {
            paramIAccountAuthenticatorResponse = null;
          }
          localParcel.writeStrongBinder(paramIAccountAuthenticatorResponse);
          if (paramAccount != null)
          {
            localParcel.writeInt(1);
            paramAccount.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
