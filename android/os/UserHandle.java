package android.os;

import android.annotation.SystemApi;
import java.io.PrintWriter;

public final class UserHandle
  implements Parcelable
{
  public static final int AID_APP_END = 19999;
  public static final int AID_APP_START = 10000;
  public static final int AID_CACHE_GID_START = 20000;
  public static final int AID_ROOT = 0;
  public static final int AID_SHARED_GID_START = 50000;
  public static final UserHandle ALL = new UserHandle(-1);
  public static final Parcelable.Creator<UserHandle> CREATOR = new Parcelable.Creator()
  {
    public UserHandle createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UserHandle(paramAnonymousParcel);
    }
    
    public UserHandle[] newArray(int paramAnonymousInt)
    {
      return new UserHandle[paramAnonymousInt];
    }
  };
  public static final UserHandle CURRENT = new UserHandle(-2);
  public static final UserHandle CURRENT_OR_SELF = new UserHandle(-3);
  public static final int ERR_GID = -1;
  public static final boolean MU_ENABLED = true;
  @Deprecated
  public static final UserHandle OWNER = new UserHandle(0);
  public static final int PER_USER_RANGE = 100000;
  public static final UserHandle SYSTEM = new UserHandle(0);
  public static final int USER_ALL = -1;
  public static final int USER_CURRENT = -2;
  public static final int USER_CURRENT_OR_SELF = -3;
  public static final int USER_NULL = -10000;
  @Deprecated
  public static final int USER_OWNER = 0;
  public static final int USER_SERIAL_SYSTEM = 0;
  public static final int USER_SYSTEM = 0;
  final int mHandle;
  
  public UserHandle(int paramInt)
  {
    mHandle = paramInt;
  }
  
  public UserHandle(Parcel paramParcel)
  {
    mHandle = paramParcel.readInt();
  }
  
  public static String formatUid(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    formatUid(localStringBuilder, paramInt);
    return localStringBuilder.toString();
  }
  
  public static void formatUid(PrintWriter paramPrintWriter, int paramInt)
  {
    if (paramInt < 10000)
    {
      paramPrintWriter.print(paramInt);
    }
    else
    {
      paramPrintWriter.print('u');
      paramPrintWriter.print(getUserId(paramInt));
      paramInt = getAppId(paramInt);
      if ((paramInt >= 99000) && (paramInt <= 99999))
      {
        paramPrintWriter.print('i');
        paramPrintWriter.print(paramInt - 99000);
      }
      else if (paramInt >= 10000)
      {
        paramPrintWriter.print('a');
        paramPrintWriter.print(paramInt - 10000);
      }
      else
      {
        paramPrintWriter.print('s');
        paramPrintWriter.print(paramInt);
      }
    }
  }
  
  public static void formatUid(StringBuilder paramStringBuilder, int paramInt)
  {
    if (paramInt < 10000)
    {
      paramStringBuilder.append(paramInt);
    }
    else
    {
      paramStringBuilder.append('u');
      paramStringBuilder.append(getUserId(paramInt));
      paramInt = getAppId(paramInt);
      if ((paramInt >= 99000) && (paramInt <= 99999))
      {
        paramStringBuilder.append('i');
        paramStringBuilder.append(paramInt - 99000);
      }
      else if (paramInt >= 10000)
      {
        paramStringBuilder.append('a');
        paramStringBuilder.append(paramInt - 10000);
      }
      else
      {
        paramStringBuilder.append('s');
        paramStringBuilder.append(paramInt);
      }
    }
  }
  
  public static int getAppId(int paramInt)
  {
    return paramInt % 100000;
  }
  
  public static int getAppIdFromSharedAppGid(int paramInt)
  {
    paramInt = getAppId(paramInt) + 10000 - 50000;
    if ((paramInt >= 0) && (paramInt < 50000)) {
      return paramInt;
    }
    return -1;
  }
  
  public static int getCacheAppGid(int paramInt)
  {
    return getCacheAppGid(getUserId(paramInt), getAppId(paramInt));
  }
  
  public static int getCacheAppGid(int paramInt1, int paramInt2)
  {
    if ((paramInt2 >= 10000) && (paramInt2 <= 19999)) {
      return getUid(paramInt1, paramInt2 - 10000 + 20000);
    }
    return -1;
  }
  
  public static int getCallingAppId()
  {
    return getAppId(Binder.getCallingUid());
  }
  
  public static int getCallingUserId()
  {
    return getUserId(Binder.getCallingUid());
  }
  
  public static int getSharedAppGid(int paramInt)
  {
    return getSharedAppGid(getUserId(paramInt), getAppId(paramInt));
  }
  
  public static int getSharedAppGid(int paramInt1, int paramInt2)
  {
    if ((paramInt2 >= 10000) && (paramInt2 <= 19999)) {
      return paramInt2 - 10000 + 50000;
    }
    if ((paramInt2 >= 0) && (paramInt2 <= 10000)) {
      return paramInt2;
    }
    return -1;
  }
  
  public static int getUid(int paramInt1, int paramInt2)
  {
    return paramInt1 * 100000 + paramInt2 % 100000;
  }
  
  public static int getUserGid(int paramInt)
  {
    return getUid(paramInt, 9997);
  }
  
  public static UserHandle getUserHandleForUid(int paramInt)
  {
    return of(getUserId(paramInt));
  }
  
  public static int getUserId(int paramInt)
  {
    return paramInt / 100000;
  }
  
  public static boolean isApp(int paramInt)
  {
    boolean bool1 = false;
    if (paramInt > 0)
    {
      paramInt = getAppId(paramInt);
      boolean bool2 = bool1;
      if (paramInt >= 10000)
      {
        bool2 = bool1;
        if (paramInt <= 19999) {
          bool2 = true;
        }
      }
      return bool2;
    }
    return false;
  }
  
  public static boolean isCore(int paramInt)
  {
    boolean bool = false;
    if (paramInt >= 0)
    {
      if (getAppId(paramInt) < 10000) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public static boolean isIsolated(int paramInt)
  {
    boolean bool1 = false;
    if (paramInt > 0)
    {
      paramInt = getAppId(paramInt);
      boolean bool2 = bool1;
      if (paramInt >= 99000)
      {
        bool2 = bool1;
        if (paramInt <= 99999) {
          bool2 = true;
        }
      }
      return bool2;
    }
    return false;
  }
  
  public static boolean isSameApp(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (getAppId(paramInt1) == getAppId(paramInt2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isSameUser(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (getUserId(paramInt1) == getUserId(paramInt2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public static int myUserId()
  {
    return getUserId(Process.myUid());
  }
  
  @SystemApi
  public static UserHandle of(int paramInt)
  {
    UserHandle localUserHandle;
    if (paramInt == 0) {
      localUserHandle = SYSTEM;
    } else {
      localUserHandle = new UserHandle(paramInt);
    }
    return localUserHandle;
  }
  
  public static int parseUserArg(String paramString)
  {
    if ("all".equals(paramString)) {}
    for (int i = -1;; i = -2)
    {
      break;
      if ((!"current".equals(paramString)) && (!"cur".equals(paramString))) {
        try
        {
          i = Integer.parseInt(paramString);
        }
        catch (NumberFormatException localNumberFormatException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Bad user number: ");
          localStringBuilder.append(paramString);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
      }
    }
    return i;
  }
  
  public static UserHandle readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if (i != 55536) {
      paramParcel = new UserHandle(i);
    } else {
      paramParcel = null;
    }
    return paramParcel;
  }
  
  public static void writeToParcel(UserHandle paramUserHandle, Parcel paramParcel)
  {
    if (paramUserHandle != null) {
      paramUserHandle.writeToParcel(paramParcel, 0);
    } else {
      paramParcel.writeInt(55536);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (paramObject != null) {
      try
      {
        paramObject = (UserHandle)paramObject;
        int i = mHandle;
        int j = mHandle;
        if (i == j) {
          bool = true;
        }
        return bool;
      }
      catch (ClassCastException paramObject) {}
    }
    return false;
  }
  
  @SystemApi
  public int getIdentifier()
  {
    return mHandle;
  }
  
  public int hashCode()
  {
    return mHandle;
  }
  
  @SystemApi
  @Deprecated
  public boolean isOwner()
  {
    return equals(OWNER);
  }
  
  @SystemApi
  public boolean isSystem()
  {
    return equals(SYSTEM);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UserHandle{");
    localStringBuilder.append(mHandle);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mHandle);
  }
}
