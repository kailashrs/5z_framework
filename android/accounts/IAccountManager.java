package android.accounts;

import android.content.IntentSender;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.Map;

public abstract interface IAccountManager
  extends IInterface
{
  public abstract boolean accountAuthenticated(Account paramAccount)
    throws RemoteException;
  
  public abstract void addAccount(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2, String[] paramArrayOfString, boolean paramBoolean, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void addAccountAsUser(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2, String[] paramArrayOfString, boolean paramBoolean, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract boolean addAccountExplicitly(Account paramAccount, String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean addAccountExplicitlyWithVisibility(Account paramAccount, String paramString, Bundle paramBundle, Map paramMap)
    throws RemoteException;
  
  public abstract void addSharedAccountsFromParentUser(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void clearPassword(Account paramAccount)
    throws RemoteException;
  
  public abstract void confirmCredentialsAsUser(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, Bundle paramBundle, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void copyAccountToUser(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract IntentSender createRequestAccountAccessIntentSenderAsUser(Account paramAccount, String paramString, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void editProperties(IAccountManagerResponse paramIAccountManagerResponse, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void finishSessionAsUser(IAccountManagerResponse paramIAccountManagerResponse, Bundle paramBundle1, boolean paramBoolean, Bundle paramBundle2, int paramInt)
    throws RemoteException;
  
  public abstract void getAccountByTypeAndFeatures(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String[] paramArrayOfString, String paramString2)
    throws RemoteException;
  
  public abstract int getAccountVisibility(Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract Account[] getAccounts(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract Map getAccountsAndVisibilityForPackage(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract Account[] getAccountsAsUser(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract void getAccountsByFeatures(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String[] paramArrayOfString, String paramString2)
    throws RemoteException;
  
  public abstract Account[] getAccountsByTypeForPackage(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract Account[] getAccountsForPackage(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract void getAuthToken(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString, boolean paramBoolean1, boolean paramBoolean2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void getAuthTokenLabel(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract AuthenticatorDescription[] getAuthenticatorTypes(int paramInt)
    throws RemoteException;
  
  public abstract Map getPackagesAndVisibilityForAccount(Account paramAccount)
    throws RemoteException;
  
  public abstract String getPassword(Account paramAccount)
    throws RemoteException;
  
  public abstract String getPreviousName(Account paramAccount)
    throws RemoteException;
  
  public abstract Account[] getSharedAccountsAsUser(int paramInt)
    throws RemoteException;
  
  public abstract String getUserData(Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract boolean hasAccountAccess(Account paramAccount, String paramString, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void hasFeatures(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String[] paramArrayOfString, String paramString)
    throws RemoteException;
  
  public abstract void invalidateAuthToken(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void isCredentialsUpdateSuggested(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract void onAccountAccessed(String paramString)
    throws RemoteException;
  
  public abstract String peekAuthToken(Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract void registerAccountListener(String[] paramArrayOfString, String paramString)
    throws RemoteException;
  
  public abstract void removeAccount(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void removeAccountAsUser(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract boolean removeAccountExplicitly(Account paramAccount)
    throws RemoteException;
  
  public abstract boolean removeSharedAccountAsUser(Account paramAccount, int paramInt)
    throws RemoteException;
  
  public abstract void renameAccount(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract boolean renameSharedAccountAsUser(Account paramAccount, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean setAccountVisibility(Account paramAccount, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setAuthToken(Account paramAccount, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setPassword(Account paramAccount, String paramString)
    throws RemoteException;
  
  public abstract void setUserData(Account paramAccount, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean someUserHasAccount(Account paramAccount)
    throws RemoteException;
  
  public abstract void startAddAccountSession(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2, String[] paramArrayOfString, boolean paramBoolean, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void startUpdateCredentialsSession(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString, boolean paramBoolean, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void unregisterAccountListener(String[] paramArrayOfString, String paramString)
    throws RemoteException;
  
  public abstract void updateAppPermission(Account paramAccount, String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void updateCredentials(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString, boolean paramBoolean, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccountManager
  {
    private static final String DESCRIPTOR = "android.accounts.IAccountManager";
    static final int TRANSACTION_accountAuthenticated = 29;
    static final int TRANSACTION_addAccount = 24;
    static final int TRANSACTION_addAccountAsUser = 25;
    static final int TRANSACTION_addAccountExplicitly = 11;
    static final int TRANSACTION_addAccountExplicitlyWithVisibility = 43;
    static final int TRANSACTION_addSharedAccountsFromParentUser = 33;
    static final int TRANSACTION_clearPassword = 20;
    static final int TRANSACTION_confirmCredentialsAsUser = 28;
    static final int TRANSACTION_copyAccountToUser = 15;
    static final int TRANSACTION_createRequestAccountAccessIntentSenderAsUser = 50;
    static final int TRANSACTION_editProperties = 27;
    static final int TRANSACTION_finishSessionAsUser = 39;
    static final int TRANSACTION_getAccountByTypeAndFeatures = 9;
    static final int TRANSACTION_getAccountVisibility = 45;
    static final int TRANSACTION_getAccounts = 4;
    static final int TRANSACTION_getAccountsAndVisibilityForPackage = 46;
    static final int TRANSACTION_getAccountsAsUser = 7;
    static final int TRANSACTION_getAccountsByFeatures = 10;
    static final int TRANSACTION_getAccountsByTypeForPackage = 6;
    static final int TRANSACTION_getAccountsForPackage = 5;
    static final int TRANSACTION_getAuthToken = 23;
    static final int TRANSACTION_getAuthTokenLabel = 30;
    static final int TRANSACTION_getAuthenticatorTypes = 3;
    static final int TRANSACTION_getPackagesAndVisibilityForAccount = 42;
    static final int TRANSACTION_getPassword = 1;
    static final int TRANSACTION_getPreviousName = 35;
    static final int TRANSACTION_getSharedAccountsAsUser = 31;
    static final int TRANSACTION_getUserData = 2;
    static final int TRANSACTION_hasAccountAccess = 49;
    static final int TRANSACTION_hasFeatures = 8;
    static final int TRANSACTION_invalidateAuthToken = 16;
    static final int TRANSACTION_isCredentialsUpdateSuggested = 41;
    static final int TRANSACTION_onAccountAccessed = 51;
    static final int TRANSACTION_peekAuthToken = 17;
    static final int TRANSACTION_registerAccountListener = 47;
    static final int TRANSACTION_removeAccount = 12;
    static final int TRANSACTION_removeAccountAsUser = 13;
    static final int TRANSACTION_removeAccountExplicitly = 14;
    static final int TRANSACTION_removeSharedAccountAsUser = 32;
    static final int TRANSACTION_renameAccount = 34;
    static final int TRANSACTION_renameSharedAccountAsUser = 36;
    static final int TRANSACTION_setAccountVisibility = 44;
    static final int TRANSACTION_setAuthToken = 18;
    static final int TRANSACTION_setPassword = 19;
    static final int TRANSACTION_setUserData = 21;
    static final int TRANSACTION_someUserHasAccount = 40;
    static final int TRANSACTION_startAddAccountSession = 37;
    static final int TRANSACTION_startUpdateCredentialsSession = 38;
    static final int TRANSACTION_unregisterAccountListener = 48;
    static final int TRANSACTION_updateAppPermission = 22;
    static final int TRANSACTION_updateCredentials = 26;
    
    public Stub()
    {
      attachInterface(this, "android.accounts.IAccountManager");
    }
    
    public static IAccountManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.accounts.IAccountManager");
      if ((localIInterface != null) && ((localIInterface instanceof IAccountManager))) {
        return (IAccountManager)localIInterface;
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
        String[] arrayOfString = null;
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
        Object localObject25 = null;
        Object localObject26 = null;
        Object localObject27 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 51: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          onAccountAccessed(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject27;
          }
          paramParcel1 = createRequestAccountAccessIntentSenderAsUser((Account)localObject6, (String)localObject1, paramParcel1);
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
        case 49: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          localObject27 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          paramInt1 = hasAccountAccess((Account)localObject6, (String)localObject27, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          unregisterAccountListener(paramParcel1.createStringArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          registerAccountListener(paramParcel1.createStringArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          paramParcel1 = getAccountsAndVisibilityForPackage(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeMap(paramParcel1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject2;
          }
          paramInt1 = getAccountVisibility((Account)localObject6, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject3;
          }
          paramInt1 = setAccountVisibility((Account)localObject6, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject27 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject27 = localObject4;
          }
          paramInt1 = addAccountExplicitlyWithVisibility((Account)localObject6, (String)localObject1, (Bundle)localObject27, paramParcel1.readHashMap(getClass().getClassLoader()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramParcel1 = getPackagesAndVisibilityForAccount(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeMap(paramParcel1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          }
          isCredentialsUpdateSuggested((IAccountManagerResponse)localObject27, (Account)localObject6, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramInt1 = someUserHasAccount(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject1 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            localObject27 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject27 = null;
          }
          finishSessionAsUser((IAccountManagerResponse)localObject1, (Bundle)localObject6, bool4, (Bundle)localObject27, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          startUpdateCredentialsSession((IAccountManagerResponse)localObject27, (Account)localObject6, (String)localObject1, bool4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject1 = paramParcel1.readString();
          localObject4 = paramParcel1.readString();
          localObject6 = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          startAddAccountSession((IAccountManagerResponse)localObject27, (String)localObject1, (String)localObject4, (String[])localObject6, bool4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject8;
          }
          paramInt1 = renameSharedAccountAsUser((Account)localObject6, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          paramParcel1 = getPreviousName(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject10;
          }
          renameAccount((IAccountManagerResponse)localObject27, (Account)localObject6, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          addSharedAccountsFromParentUser(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject11;
          }
          paramInt1 = removeSharedAccountAsUser((Account)localObject6, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          paramParcel1 = getSharedAccountsAsUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          getAuthTokenLabel(IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          paramInt1 = accountAuthenticated(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject1 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject27 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localObject27 = arrayOfString) {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          confirmCredentialsAsUser((IAccountManagerResponse)localObject1, (Account)localObject6, (Bundle)localObject27, bool4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject6 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          editProperties((IAccountManagerResponse)localObject27, (String)localObject6, bool4);
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          updateCredentials((IAccountManagerResponse)localObject27, (Account)localObject6, (String)localObject1, bool4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject1 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject4 = paramParcel1.readString();
          localObject27 = paramParcel1.readString();
          arrayOfString = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          addAccountAsUser((IAccountManagerResponse)localObject1, (String)localObject4, (String)localObject27, arrayOfString, bool4, (Bundle)localObject6, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject6 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject27 = paramParcel1.readString();
          localObject1 = paramParcel1.readString();
          localObject4 = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          addAccount((IAccountManagerResponse)localObject6, (String)localObject27, (String)localObject1, (String[])localObject4, bool4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          getAuthToken((IAccountManagerResponse)localObject27, (Account)localObject6, (String)localObject1, bool4, bool1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject13;
          }
          localObject27 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          updateAppPermission((Account)localObject6, (String)localObject27, paramInt1, bool4);
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject14;
          }
          setUserData((Account)localObject6, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          clearPassword(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject16;
          }
          setPassword((Account)localObject6, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject17;
          }
          setAuthToken((Account)localObject6, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject18;
          }
          paramParcel1 = peekAuthToken((Account)localObject6, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          invalidateAuthToken(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject19;
          }
          copyAccountToUser((IAccountManagerResponse)localObject27, (Account)localObject6, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          paramInt1 = removeAccountExplicitly(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject21;
          }
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          removeAccountAsUser((IAccountManagerResponse)localObject27, (Account)localObject6, bool4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject22;
          }
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          removeAccount((IAccountManagerResponse)localObject27, (Account)localObject6, bool4);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          localObject27 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject23;
          }
          paramInt1 = addAccountExplicitly((Account)localObject6, (String)localObject27, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          getAccountsByFeatures(IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.createStringArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          getAccountByTypeAndFeatures(IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.createStringArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          localObject27 = IAccountManagerResponse.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject24;
          }
          hasFeatures((IAccountManagerResponse)localObject27, (Account)localObject6, paramParcel1.createStringArray(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          paramParcel1 = getAccountsAsUser(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          paramParcel1 = getAccountsByTypeForPackage(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          paramParcel1 = getAccountsForPackage(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          paramParcel1 = getAccounts(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          paramParcel1 = getAuthenticatorTypes(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.accounts.IAccountManager");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject25;
          }
          paramParcel1 = getUserData((Account)localObject6, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.accounts.IAccountManager");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Account)Account.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject26;
        }
        paramParcel1 = getPassword(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeString(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.accounts.IAccountManager");
      return true;
    }
    
    private static class Proxy
      implements IAccountManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean accountAuthenticated(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(29, localParcel1, localParcel2, 0);
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
      
      public void addAccount(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2, String[] paramArrayOfString, boolean paramBoolean, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramBoolean);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void addAccountAsUser(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2, String[] paramArrayOfString, boolean paramBoolean, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramBoolean);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public boolean addAccountExplicitly(Account paramAccount, String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
          mRemote.transact(11, localParcel1, localParcel2, 0);
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
      
      public boolean addAccountExplicitlyWithVisibility(Account paramAccount, String paramString, Bundle paramBundle, Map paramMap)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
          localParcel1.writeMap(paramMap);
          mRemote.transact(43, localParcel1, localParcel2, 0);
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
      
      public void addSharedAccountsFromParentUser(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void clearPassword(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void confirmCredentialsAsUser(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, Bundle paramBundle, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
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
      
      public void copyAccountToUser(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public IntentSender createRequestAccountAccessIntentSenderAsUser(Account paramAccount, String paramString, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramAccount = (IntentSender)IntentSender.CREATOR.createFromParcel(localParcel2);
          } else {
            paramAccount = null;
          }
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void editProperties(IAccountManagerResponse paramIAccountManagerResponse, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void finishSessionAsUser(IAccountManagerResponse paramIAccountManagerResponse, Bundle paramBundle1, boolean paramBoolean, Bundle paramBundle2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramBundle1 != null)
          {
            localParcel1.writeInt(1);
            paramBundle1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          if (paramBundle2 != null)
          {
            localParcel1.writeInt(1);
            paramBundle2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public void getAccountByTypeAndFeatures(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String[] paramArrayOfString, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          localParcel1.writeString(paramString1);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeString(paramString2);
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
      
      public int getAccountVisibility(Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(45, localParcel1, localParcel2, 0);
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
      
      public Account[] getAccounts(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = (Account[])localParcel2.createTypedArray(Account.CREATOR);
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Map getAccountsAndVisibilityForPackage(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readHashMap(getClass().getClassLoader());
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Account[] getAccountsAsUser(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = (Account[])localParcel2.createTypedArray(Account.CREATOR);
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getAccountsByFeatures(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String[] paramArrayOfString, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          localParcel1.writeString(paramString1);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeString(paramString2);
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
      
      public Account[] getAccountsByTypeForPackage(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = (Account[])localParcel2.createTypedArray(Account.CREATOR);
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Account[] getAccountsForPackage(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = (Account[])localParcel2.createTypedArray(Account.CREATOR);
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getAuthToken(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString, boolean paramBoolean1, boolean paramBoolean2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void getAuthTokenLabel(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public AuthenticatorDescription[] getAuthenticatorTypes(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          AuthenticatorDescription[] arrayOfAuthenticatorDescription = (AuthenticatorDescription[])localParcel2.createTypedArray(AuthenticatorDescription.CREATOR);
          return arrayOfAuthenticatorDescription;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.accounts.IAccountManager";
      }
      
      public Map getPackagesAndVisibilityForAccount(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAccount = localParcel2.readHashMap(getClass().getClassLoader());
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getPassword(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAccount = localParcel2.readString();
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getPreviousName(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAccount = localParcel2.readString();
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Account[] getSharedAccountsAsUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Account[] arrayOfAccount = (Account[])localParcel2.createTypedArray(Account.CREATOR);
          return arrayOfAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getUserData(Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAccount = localParcel2.readString();
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasAccountAccess(Account paramAccount, String paramString, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(49, localParcel1, localParcel2, 0);
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
      
      public void hasFeatures(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String[] paramArrayOfString, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeString(paramString);
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
      
      public void invalidateAuthToken(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void isCredentialsUpdateSuggested(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public void onAccountAccessed(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeString(paramString);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String peekAuthToken(Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAccount = localParcel2.readString();
          return paramAccount;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerAccountListener(String[] paramArrayOfString, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeString(paramString);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeAccount(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
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
      
      public void removeAccountAsUser(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
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
      
      public boolean removeAccountExplicitly(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
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
      
      public boolean removeSharedAccountAsUser(Account paramAccount, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(32, localParcel1, localParcel2, 0);
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
      
      public void renameAccount(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public boolean renameSharedAccountAsUser(Account paramAccount, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(36, localParcel1, localParcel2, 0);
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
      
      public boolean setAccountVisibility(Account paramAccount, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(44, localParcel1, localParcel2, 0);
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
      
      public void setAuthToken(Account paramAccount, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void setPassword(Account paramAccount, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public void setUserData(Account paramAccount, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public boolean someUserHasAccount(Account paramAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          boolean bool = true;
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(40, localParcel1, localParcel2, 0);
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
      
      public void startAddAccountSession(IAccountManagerResponse paramIAccountManagerResponse, String paramString1, String paramString2, String[] paramArrayOfString, boolean paramBoolean, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramBoolean);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startUpdateCredentialsSession(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString, boolean paramBoolean, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void unregisterAccountListener(String[] paramArrayOfString, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeString(paramString);
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateAppPermission(Account paramAccount, String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void updateCredentials(IAccountManagerResponse paramIAccountManagerResponse, Account paramAccount, String paramString, boolean paramBoolean, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accounts.IAccountManager");
          if (paramIAccountManagerResponse != null) {
            paramIAccountManagerResponse = paramIAccountManagerResponse.asBinder();
          } else {
            paramIAccountManagerResponse = null;
          }
          localParcel1.writeStrongBinder(paramIAccountManagerResponse);
          if (paramAccount != null)
          {
            localParcel1.writeInt(1);
            paramAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
