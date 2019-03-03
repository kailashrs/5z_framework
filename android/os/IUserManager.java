package android.os;

import android.content.IntentSender;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

public abstract interface IUserManager
  extends IInterface
{
  public abstract boolean canAddMoreManagedProfiles(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean canHaveRestrictedProfile(int paramInt)
    throws RemoteException;
  
  public abstract void clearSeedAccountData()
    throws RemoteException;
  
  public abstract UserInfo createProfileForUser(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract UserInfo createProfileForUserEvenWhenDisallowed(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract UserInfo createRestrictedProfile(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract UserInfo createUser(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void evictCredentialEncryptionKey(int paramInt)
    throws RemoteException;
  
  public abstract Bundle getApplicationRestrictions(String paramString)
    throws RemoteException;
  
  public abstract Bundle getApplicationRestrictionsForUser(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getCredentialOwnerProfile(int paramInt)
    throws RemoteException;
  
  public abstract Bundle getDefaultGuestRestrictions()
    throws RemoteException;
  
  public abstract int getManagedProfileBadge(int paramInt)
    throws RemoteException;
  
  public abstract UserInfo getPrimaryUser()
    throws RemoteException;
  
  public abstract int[] getProfileIds(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract UserInfo getProfileParent(int paramInt)
    throws RemoteException;
  
  public abstract int getProfileParentId(int paramInt)
    throws RemoteException;
  
  public abstract List<UserInfo> getProfiles(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String getSeedAccountName()
    throws RemoteException;
  
  public abstract PersistableBundle getSeedAccountOptions()
    throws RemoteException;
  
  public abstract String getSeedAccountType()
    throws RemoteException;
  
  public abstract int getTwinAppsId()
    throws RemoteException;
  
  public abstract String getUserAccount(int paramInt)
    throws RemoteException;
  
  public abstract long getUserCreationTime(int paramInt)
    throws RemoteException;
  
  public abstract int getUserHandle(int paramInt)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor getUserIcon(int paramInt)
    throws RemoteException;
  
  public abstract UserInfo getUserInfo(int paramInt)
    throws RemoteException;
  
  public abstract int getUserRestrictionSource(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract List<UserManager.EnforcingUser> getUserRestrictionSources(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract Bundle getUserRestrictions(int paramInt)
    throws RemoteException;
  
  public abstract int getUserSerialNumber(int paramInt)
    throws RemoteException;
  
  public abstract long getUserStartRealtime()
    throws RemoteException;
  
  public abstract long getUserUnlockRealtime()
    throws RemoteException;
  
  public abstract List<UserInfo> getUsers(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean hasBaseUserRestriction(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasRestrictedProfiles()
    throws RemoteException;
  
  public abstract boolean hasUserRestriction(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasUserRestrictionOnAnyUser(String paramString)
    throws RemoteException;
  
  public abstract boolean isDemoUser(int paramInt)
    throws RemoteException;
  
  public abstract boolean isManagedProfile(int paramInt)
    throws RemoteException;
  
  public abstract boolean isMultiUserExistNoCheck()
    throws RemoteException;
  
  public abstract boolean isQuietModeEnabled(int paramInt)
    throws RemoteException;
  
  public abstract boolean isRestricted()
    throws RemoteException;
  
  public abstract boolean isSameProfileGroup(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean isTwinApps(int paramInt)
    throws RemoteException;
  
  public abstract boolean isUserNameSet(int paramInt)
    throws RemoteException;
  
  public abstract boolean isUserRunning(int paramInt)
    throws RemoteException;
  
  public abstract boolean isUserUnlocked(int paramInt)
    throws RemoteException;
  
  public abstract boolean isUserUnlockingOrUnlocked(int paramInt)
    throws RemoteException;
  
  public abstract boolean markGuestForDeletion(int paramInt)
    throws RemoteException;
  
  public abstract boolean removeUser(int paramInt)
    throws RemoteException;
  
  public abstract boolean removeUserEvenWhenDisallowed(int paramInt)
    throws RemoteException;
  
  public abstract boolean requestQuietModeEnabled(String paramString, boolean paramBoolean, int paramInt, IntentSender paramIntentSender)
    throws RemoteException;
  
  public abstract void setApplicationRestrictions(String paramString, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract void setDefaultGuestRestrictions(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void setSeedAccountData(int paramInt, String paramString1, String paramString2, PersistableBundle paramPersistableBundle, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUserAccount(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setUserAdmin(int paramInt)
    throws RemoteException;
  
  public abstract void setUserEnabled(int paramInt)
    throws RemoteException;
  
  public abstract void setUserIcon(int paramInt, Bitmap paramBitmap)
    throws RemoteException;
  
  public abstract void setUserName(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setUserRestriction(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract boolean someUserHasSeedAccount(String paramString1, String paramString2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUserManager
  {
    private static final String DESCRIPTOR = "android.os.IUserManager";
    static final int TRANSACTION_canAddMoreManagedProfiles = 18;
    static final int TRANSACTION_canHaveRestrictedProfile = 26;
    static final int TRANSACTION_clearSeedAccountData = 47;
    static final int TRANSACTION_createProfileForUser = 4;
    static final int TRANSACTION_createProfileForUserEvenWhenDisallowed = 51;
    static final int TRANSACTION_createRestrictedProfile = 5;
    static final int TRANSACTION_createUser = 3;
    static final int TRANSACTION_evictCredentialEncryptionKey = 8;
    static final int TRANSACTION_getApplicationRestrictions = 37;
    static final int TRANSACTION_getApplicationRestrictionsForUser = 38;
    static final int TRANSACTION_getCredentialOwnerProfile = 1;
    static final int TRANSACTION_getDefaultGuestRestrictions = 40;
    static final int TRANSACTION_getManagedProfileBadge = 53;
    static final int TRANSACTION_getPrimaryUser = 14;
    static final int TRANSACTION_getProfileIds = 17;
    static final int TRANSACTION_getProfileParent = 19;
    static final int TRANSACTION_getProfileParentId = 2;
    static final int TRANSACTION_getProfiles = 16;
    static final int TRANSACTION_getSeedAccountName = 44;
    static final int TRANSACTION_getSeedAccountOptions = 46;
    static final int TRANSACTION_getSeedAccountType = 45;
    static final int TRANSACTION_getTwinAppsId = 62;
    static final int TRANSACTION_getUserAccount = 22;
    static final int TRANSACTION_getUserCreationTime = 24;
    static final int TRANSACTION_getUserHandle = 28;
    static final int TRANSACTION_getUserIcon = 13;
    static final int TRANSACTION_getUserInfo = 21;
    static final int TRANSACTION_getUserRestrictionSource = 29;
    static final int TRANSACTION_getUserRestrictionSources = 30;
    static final int TRANSACTION_getUserRestrictions = 31;
    static final int TRANSACTION_getUserSerialNumber = 27;
    static final int TRANSACTION_getUserStartRealtime = 59;
    static final int TRANSACTION_getUserUnlockRealtime = 60;
    static final int TRANSACTION_getUsers = 15;
    static final int TRANSACTION_hasBaseUserRestriction = 32;
    static final int TRANSACTION_hasRestrictedProfiles = 57;
    static final int TRANSACTION_hasUserRestriction = 33;
    static final int TRANSACTION_hasUserRestrictionOnAnyUser = 34;
    static final int TRANSACTION_isDemoUser = 50;
    static final int TRANSACTION_isManagedProfile = 49;
    static final int TRANSACTION_isMultiUserExistNoCheck = 61;
    static final int TRANSACTION_isQuietModeEnabled = 42;
    static final int TRANSACTION_isRestricted = 25;
    static final int TRANSACTION_isSameProfileGroup = 20;
    static final int TRANSACTION_isTwinApps = 63;
    static final int TRANSACTION_isUserNameSet = 56;
    static final int TRANSACTION_isUserRunning = 55;
    static final int TRANSACTION_isUserUnlocked = 54;
    static final int TRANSACTION_isUserUnlockingOrUnlocked = 52;
    static final int TRANSACTION_markGuestForDeletion = 41;
    static final int TRANSACTION_removeUser = 9;
    static final int TRANSACTION_removeUserEvenWhenDisallowed = 10;
    static final int TRANSACTION_requestQuietModeEnabled = 58;
    static final int TRANSACTION_setApplicationRestrictions = 36;
    static final int TRANSACTION_setDefaultGuestRestrictions = 39;
    static final int TRANSACTION_setSeedAccountData = 43;
    static final int TRANSACTION_setUserAccount = 23;
    static final int TRANSACTION_setUserAdmin = 7;
    static final int TRANSACTION_setUserEnabled = 6;
    static final int TRANSACTION_setUserIcon = 12;
    static final int TRANSACTION_setUserName = 11;
    static final int TRANSACTION_setUserRestriction = 35;
    static final int TRANSACTION_someUserHasSeedAccount = 48;
    
    public Stub()
    {
      attachInterface(this, "android.os.IUserManager");
    }
    
    public static IUserManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IUserManager");
      if ((localIInterface != null) && ((localIInterface instanceof IUserManager))) {
        return (IUserManager)localIInterface;
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
        String str1 = null;
        Object localObject3 = null;
        String str2 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 63: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isTwinApps(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = getTwinAppsId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isMultiUserExistNoCheck();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          l = getUserUnlockRealtime();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          l = getUserStartRealtime();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          paramInt1 = requestQuietModeEnabled((String)localObject1, bool6, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = hasRestrictedProfiles();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isUserNameSet(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isUserRunning(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isUserUnlocked(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = getManagedProfileBadge(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isUserUnlockingOrUnlocked(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = createProfileForUserEvenWhenDisallowed(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createStringArray());
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
        case 50: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isDemoUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isManagedProfile(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = someUserHasSeedAccount(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          clearSeedAccountData();
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getSeedAccountOptions();
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
        case 45: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getSeedAccountType();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getSeedAccountName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = paramParcel1.readInt();
          str2 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          setSeedAccountData(paramInt1, str2, str1, (PersistableBundle)localObject1, bool6);
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isQuietModeEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = markGuestForDeletion(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getDefaultGuestRestrictions();
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
        case 39: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          setDefaultGuestRestrictions(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getApplicationRestrictionsForUser(paramParcel1.readString(), paramParcel1.readInt());
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
        case 37: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getApplicationRestrictions(paramParcel1.readString());
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
        case 36: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = str1;
          }
          setApplicationRestrictions(str2, (Bundle)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          localObject1 = paramParcel1.readString();
          bool6 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          setUserRestriction((String)localObject1, bool6, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = hasUserRestrictionOnAnyUser(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = hasUserRestriction(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = hasBaseUserRestriction(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getUserRestrictions(paramParcel1.readInt());
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
        case 30: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getUserRestrictionSources(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = getUserRestrictionSource(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = getUserHandle(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = getUserSerialNumber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = canHaveRestrictedProfile(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isRestricted();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          l = getUserCreationTime(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          setUserAccount(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getUserAccount(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getUserInfo(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = isSameProfileGroup(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getProfileParent(paramParcel1.readInt());
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
        case 18: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = paramParcel1.readInt();
          bool6 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          paramInt1 = canAddMoreManagedProfiles(paramInt1, bool6);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = paramParcel1.readInt();
          bool6 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          paramParcel1 = getProfileIds(paramInt1, bool6);
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = paramParcel1.readInt();
          bool6 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          paramParcel1 = getProfiles(paramInt1, bool6);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          bool6 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          paramParcel1 = getUsers(bool6);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getPrimaryUser();
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
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = getUserIcon(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          setUserIcon(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          setUserName(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = removeUserEvenWhenDisallowed(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = removeUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          evictCredentialEncryptionKey(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          setUserAdmin(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          setUserEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = createRestrictedProfile(paramParcel1.readString(), paramParcel1.readInt());
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
        case 4: 
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = createProfileForUser(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createStringArray());
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
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramParcel1 = createUser(paramParcel1.readString(), paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.os.IUserManager");
          paramInt1 = getProfileParentId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IUserManager");
        paramInt1 = getCredentialOwnerProfile(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.os.IUserManager");
      return true;
    }
    
    private static class Proxy
      implements IUserManager
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
      
      public boolean canAddMoreManagedProfiles(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(18, localParcel1, localParcel2, 0);
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
      
      public boolean canHaveRestrictedProfile(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(26, localParcel1, localParcel2, 0);
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
      
      public void clearSeedAccountData()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
      
      public UserInfo createProfileForUser(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public UserInfo createProfileForUserEvenWhenDisallowed(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public UserInfo createRestrictedProfile(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public UserInfo createUser(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public void evictCredentialEncryptionKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
      
      public Bundle getApplicationRestrictions(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
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
      
      public Bundle getApplicationRestrictionsForUser(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
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
      
      public int getCredentialOwnerProfile(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
      
      public Bundle getDefaultGuestRestrictions()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bundle localBundle;
          if (localParcel2.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localBundle = null;
          }
          return localBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IUserManager";
      }
      
      public int getManagedProfileBadge(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(53, localParcel1, localParcel2, 0);
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
      
      public UserInfo getPrimaryUser()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UserInfo localUserInfo;
          if (localParcel2.readInt() != 0) {
            localUserInfo = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localUserInfo = null;
          }
          return localUserInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getProfileIds(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
      
      public UserInfo getProfileParent(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UserInfo localUserInfo;
          if (localParcel2.readInt() != 0) {
            localUserInfo = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localUserInfo = null;
          }
          return localUserInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getProfileParentId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
      
      public List<UserInfo> getProfiles(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(UserInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSeedAccountName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(44, localParcel1, localParcel2, 0);
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
      
      public PersistableBundle getSeedAccountOptions()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PersistableBundle localPersistableBundle;
          if (localParcel2.readInt() != 0) {
            localPersistableBundle = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localPersistableBundle = null;
          }
          return localPersistableBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSeedAccountType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(45, localParcel1, localParcel2, 0);
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
      
      public int getTwinAppsId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(62, localParcel1, localParcel2, 0);
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
      
      public String getUserAccount(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
      
      public long getUserCreationTime(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(24, localParcel1, localParcel2, 0);
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
      
      public int getUserHandle(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public ParcelFileDescriptor getUserIcon(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
      
      public UserInfo getUserInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UserInfo localUserInfo;
          if (localParcel2.readInt() != 0) {
            localUserInfo = (UserInfo)UserInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localUserInfo = null;
          }
          return localUserInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUserRestrictionSource(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(29, localParcel1, localParcel2, 0);
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
      
      public List<UserManager.EnforcingUser> getUserRestrictionSources(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(UserManager.EnforcingUser.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getUserRestrictions(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bundle localBundle;
          if (localParcel2.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localBundle = null;
          }
          return localBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUserSerialNumber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(27, localParcel1, localParcel2, 0);
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
      
      public long getUserStartRealtime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(59, localParcel1, localParcel2, 0);
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
      
      public long getUserUnlockRealtime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          mRemote.transact(60, localParcel1, localParcel2, 0);
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
      
      public List<UserInfo> getUsers(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(UserInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasBaseUserRestriction(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(32, localParcel1, localParcel2, 0);
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
      
      public boolean hasRestrictedProfiles()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(57, localParcel1, localParcel2, 0);
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
      
      public boolean hasUserRestriction(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(33, localParcel1, localParcel2, 0);
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
      
      public boolean hasUserRestrictionOnAnyUser(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(34, localParcel1, localParcel2, 0);
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
      
      public boolean isDemoUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(50, localParcel1, localParcel2, 0);
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
      
      public boolean isManagedProfile(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(49, localParcel1, localParcel2, 0);
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
      
      public boolean isMultiUserExistNoCheck()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(61, localParcel1, localParcel2, 0);
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
      
      public boolean isQuietModeEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(42, localParcel1, localParcel2, 0);
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
      
      public boolean isRestricted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(25, localParcel1, localParcel2, 0);
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
      
      public boolean isSameProfileGroup(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public boolean isTwinApps(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(63, localParcel1, localParcel2, 0);
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
      
      public boolean isUserNameSet(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(56, localParcel1, localParcel2, 0);
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
      
      public boolean isUserRunning(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(55, localParcel1, localParcel2, 0);
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
      
      public boolean isUserUnlocked(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(54, localParcel1, localParcel2, 0);
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
      
      public boolean isUserUnlockingOrUnlocked(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(52, localParcel1, localParcel2, 0);
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
      
      public boolean markGuestForDeletion(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(41, localParcel1, localParcel2, 0);
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
      
      public boolean removeUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(9, localParcel1, localParcel2, 0);
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
      
      public boolean removeUserEvenWhenDisallowed(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
      
      public boolean requestQuietModeEnabled(String paramString, boolean paramBoolean, int paramInt, IntentSender paramIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          boolean bool = true;
          if (paramIntentSender != null)
          {
            localParcel1.writeInt(1);
            paramIntentSender.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
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
      
      public void setApplicationRestrictions(String paramString, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
          localParcel1.writeInt(paramInt);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDefaultGuestRestrictions(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setSeedAccountData(int paramInt, String paramString1, String paramString2, PersistableBundle paramPersistableBundle, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramPersistableBundle != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserAccount(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void setUserAdmin(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
      
      public void setUserEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
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
      
      public void setUserIcon(int paramInt, Bitmap paramBitmap)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          if (paramBitmap != null)
          {
            localParcel1.writeInt(1);
            paramBitmap.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setUserName(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void setUserRestriction(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean someUserHasSeedAccount(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUserManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(48, localParcel1, localParcel2, 0);
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
    }
  }
}
