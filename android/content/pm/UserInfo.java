package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.os.UserManager;

public class UserInfo
  implements Parcelable
{
  public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator()
  {
    public UserInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UserInfo(paramAnonymousParcel, null);
    }
    
    public UserInfo[] newArray(int paramAnonymousInt)
    {
      return new UserInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_ADMIN = 2;
  public static final int FLAG_DEMO = 512;
  public static final int FLAG_DISABLED = 64;
  public static final int FLAG_EPHEMERAL = 256;
  public static final int FLAG_GUEST = 4;
  public static final int FLAG_INITIALIZED = 16;
  public static final int FLAG_MANAGED_PROFILE = 32;
  public static final int FLAG_MASK_USER_TYPE = 65535;
  public static final int FLAG_PRIMARY = 1;
  public static final int FLAG_QUIET_MODE = 128;
  public static final int FLAG_RESTRICTED = 8;
  public static final int FLAG_TWINAPPS = 2048;
  public static final int FLAG_TWINAPPS_M = 129;
  public static final int FLAG_TWINAPPS_N = 512;
  public static final int NO_PROFILE_GROUP_ID = -10000;
  private static final int TWINAPPS_USER_ID = 2357;
  public long creationTime;
  public int flags;
  public boolean guestToRemove;
  public String iconPath;
  public int id;
  public String lastLoggedInFingerprint;
  public long lastLoggedInTime;
  public String name;
  public boolean partial;
  public int profileBadge;
  public int profileGroupId;
  public int restrictedProfileParentId;
  public int serialNumber;
  
  public UserInfo() {}
  
  public UserInfo(int paramInt1, String paramString, int paramInt2)
  {
    this(paramInt1, paramString, null, paramInt2);
  }
  
  public UserInfo(int paramInt1, String paramString1, String paramString2, int paramInt2)
  {
    id = paramInt1;
    name = paramString1;
    flags = paramInt2;
    iconPath = paramString2;
    profileGroupId = 55536;
    restrictedProfileParentId = 55536;
  }
  
  public UserInfo(UserInfo paramUserInfo)
  {
    name = name;
    iconPath = iconPath;
    id = id;
    flags = flags;
    serialNumber = serialNumber;
    creationTime = creationTime;
    lastLoggedInTime = lastLoggedInTime;
    lastLoggedInFingerprint = lastLoggedInFingerprint;
    partial = partial;
    profileGroupId = profileGroupId;
    restrictedProfileParentId = restrictedProfileParentId;
    guestToRemove = guestToRemove;
    profileBadge = profileBadge;
  }
  
  private UserInfo(Parcel paramParcel)
  {
    id = paramParcel.readInt();
    name = paramParcel.readString();
    iconPath = paramParcel.readString();
    flags = paramParcel.readInt();
    serialNumber = paramParcel.readInt();
    creationTime = paramParcel.readLong();
    lastLoggedInTime = paramParcel.readLong();
    lastLoggedInFingerprint = paramParcel.readString();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    partial = bool2;
    profileGroupId = paramParcel.readInt();
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    guestToRemove = bool2;
    restrictedProfileParentId = paramParcel.readInt();
    profileBadge = paramParcel.readInt();
  }
  
  public static boolean isSystemOnly(int paramInt)
  {
    boolean bool;
    if ((paramInt == 0) && (UserManager.isSplitSystemUser())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canHaveProfile()
  {
    boolean bool1 = isManagedProfile();
    boolean bool2 = false;
    boolean bool3 = false;
    if ((!bool1) && (!isGuest()) && (!isRestricted()) && (!isTwinApps()))
    {
      if (UserManager.isSplitSystemUser())
      {
        if (id != 0) {
          bool3 = true;
        }
        return bool3;
      }
      bool3 = bool2;
      if (id == 0) {
        bool3 = true;
      }
      return bool3;
    }
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public UserHandle getUserHandle()
  {
    return new UserHandle(id);
  }
  
  public boolean isAdmin()
  {
    boolean bool;
    if ((flags & 0x2) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDemo()
  {
    int i = flags;
    boolean bool = false;
    if (((i & 0x200) == 512) && (id == 2357)) {
      return false;
    }
    if ((flags & 0x200) == 512) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isEnabled()
  {
    boolean bool;
    if ((flags & 0x40) != 64) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEphemeral()
  {
    boolean bool;
    if ((flags & 0x100) == 256) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGuest()
  {
    boolean bool;
    if ((flags & 0x4) == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInitialized()
  {
    boolean bool;
    if ((flags & 0x10) == 16) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isManagedProfile()
  {
    boolean bool;
    if ((flags & 0x20) == 32) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPrimary()
  {
    int i = flags;
    boolean bool = true;
    if ((i & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isQuietModeEnabled()
  {
    int i = flags;
    boolean bool = false;
    if (((i & 0x81) == 129) && (id == 2357)) {
      return false;
    }
    if ((flags & 0x80) == 128) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isRestricted()
  {
    boolean bool;
    if ((flags & 0x8) == 8) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSystemOnly()
  {
    return isSystemOnly(id);
  }
  
  public boolean isTwinApps()
  {
    int i = flags;
    boolean bool = true;
    if (((i & 0x81) == 129) && (id == 2357)) {
      return true;
    }
    if (((flags & 0x200) == 512) && (id == 2357)) {
      return true;
    }
    if ((flags & 0x800) != 2048) {
      bool = false;
    }
    return bool;
  }
  
  public boolean supportsSwitchTo()
  {
    boolean bool1 = isEphemeral();
    boolean bool2 = false;
    if ((bool1) && (!isEnabled())) {
      return false;
    }
    if ((isManagedProfile()) && (isTwinApps())) {
      return bool2;
    }
    bool2 = true;
    return bool2;
  }
  
  public boolean supportsSwitchToByUser()
  {
    boolean bool;
    if (((!UserManager.isSplitSystemUser()) || (id != 0)) && (supportsSwitchTo())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UserInfo{");
    localStringBuilder.append(id);
    localStringBuilder.append(":");
    localStringBuilder.append(name);
    localStringBuilder.append(":");
    localStringBuilder.append(Integer.toHexString(flags));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(id);
    paramParcel.writeString(name);
    paramParcel.writeString(iconPath);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(serialNumber);
    paramParcel.writeLong(creationTime);
    paramParcel.writeLong(lastLoggedInTime);
    paramParcel.writeString(lastLoggedInFingerprint);
    paramParcel.writeInt(partial);
    paramParcel.writeInt(profileGroupId);
    paramParcel.writeInt(guestToRemove);
    paramParcel.writeInt(restrictedProfileParentId);
    paramParcel.writeInt(profileBadge);
  }
}
