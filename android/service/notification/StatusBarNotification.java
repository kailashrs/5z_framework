package android.service.notification;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;

public class StatusBarNotification
  implements Parcelable
{
  public static final Parcelable.Creator<StatusBarNotification> CREATOR = new Parcelable.Creator()
  {
    public StatusBarNotification createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StatusBarNotification(paramAnonymousParcel);
    }
    
    public StatusBarNotification[] newArray(int paramAnonymousInt)
    {
      return new StatusBarNotification[paramAnonymousInt];
    }
  };
  private String groupKey;
  private final int id;
  private final int initialPid;
  private final String key;
  private Context mContext;
  private final Notification notification;
  private final String opPkg;
  private String overrideGroupKey;
  private final String pkg;
  private final long postTime;
  private final String tag;
  private final int uid;
  private final UserHandle user;
  
  public StatusBarNotification(Parcel paramParcel)
  {
    pkg = paramParcel.readString();
    opPkg = paramParcel.readString();
    id = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      tag = paramParcel.readString();
    } else {
      tag = null;
    }
    uid = paramParcel.readInt();
    initialPid = paramParcel.readInt();
    notification = new Notification(paramParcel);
    user = UserHandle.readFromParcel(paramParcel);
    postTime = paramParcel.readLong();
    if (paramParcel.readInt() != 0) {
      overrideGroupKey = paramParcel.readString();
    } else {
      overrideGroupKey = null;
    }
    key = key();
    groupKey = groupKey();
  }
  
  @Deprecated
  public StatusBarNotification(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2, int paramInt3, int paramInt4, Notification paramNotification, UserHandle paramUserHandle, long paramLong)
  {
    if (paramString1 != null)
    {
      if (paramNotification != null)
      {
        pkg = paramString1;
        opPkg = paramString2;
        id = paramInt1;
        tag = paramString3;
        uid = paramInt2;
        initialPid = paramInt3;
        notification = paramNotification;
        user = paramUserHandle;
        postTime = paramLong;
        key = key();
        groupKey = groupKey();
        return;
      }
      throw new NullPointerException();
    }
    throw new NullPointerException();
  }
  
  public StatusBarNotification(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2, int paramInt3, Notification paramNotification, UserHandle paramUserHandle, String paramString4, long paramLong)
  {
    if (paramString1 != null)
    {
      if (paramNotification != null)
      {
        pkg = paramString1;
        opPkg = paramString2;
        id = paramInt1;
        tag = paramString3;
        uid = paramInt2;
        initialPid = paramInt3;
        notification = paramNotification;
        user = paramUserHandle;
        postTime = paramLong;
        overrideGroupKey = paramString4;
        key = key();
        groupKey = groupKey();
        return;
      }
      throw new NullPointerException();
    }
    throw new NullPointerException();
  }
  
  private String groupKey()
  {
    if (overrideGroupKey != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(user.getIdentifier());
      ((StringBuilder)localObject1).append("|");
      ((StringBuilder)localObject1).append(pkg);
      ((StringBuilder)localObject1).append("|g:");
      ((StringBuilder)localObject1).append(overrideGroupKey);
      return ((StringBuilder)localObject1).toString();
    }
    Object localObject1 = getNotification().getGroup();
    Object localObject2 = getNotification().getSortKey();
    if ((localObject1 == null) && (localObject2 == null)) {
      return key;
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append(user.getIdentifier());
    ((StringBuilder)localObject2).append("|");
    ((StringBuilder)localObject2).append(pkg);
    ((StringBuilder)localObject2).append("|");
    if (localObject1 == null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("c:");
      ((StringBuilder)localObject1).append(notification.getChannelId());
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("g:");
      localStringBuilder.append((String)localObject1);
      localObject1 = localStringBuilder.toString();
    }
    ((StringBuilder)localObject2).append((String)localObject1);
    return ((StringBuilder)localObject2).toString();
  }
  
  private String key()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(user.getIdentifier());
    ((StringBuilder)localObject).append("|");
    ((StringBuilder)localObject).append(pkg);
    ((StringBuilder)localObject).append("|");
    ((StringBuilder)localObject).append(id);
    ((StringBuilder)localObject).append("|");
    ((StringBuilder)localObject).append(tag);
    ((StringBuilder)localObject).append("|");
    ((StringBuilder)localObject).append(uid);
    String str = ((StringBuilder)localObject).toString();
    localObject = str;
    if (overrideGroupKey != null)
    {
      localObject = str;
      if (getNotification().isGroupSummary())
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(str);
        ((StringBuilder)localObject).append("|");
        ((StringBuilder)localObject).append(overrideGroupKey);
        localObject = ((StringBuilder)localObject).toString();
      }
    }
    return localObject;
  }
  
  public StatusBarNotification clone()
  {
    return new StatusBarNotification(pkg, opPkg, id, tag, uid, initialPid, notification.clone(), user, overrideGroupKey, postTime);
  }
  
  public StatusBarNotification cloneLight()
  {
    Notification localNotification = new Notification();
    notification.cloneInto(localNotification, false);
    return new StatusBarNotification(pkg, opPkg, id, tag, uid, initialPid, localNotification, user, overrideGroupKey, postTime);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getGroup()
  {
    if (overrideGroupKey != null) {
      return overrideGroupKey;
    }
    return getNotification().getGroup();
  }
  
  public String getGroupKey()
  {
    return groupKey;
  }
  
  public int getId()
  {
    return id;
  }
  
  public int getInitialPid()
  {
    return initialPid;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public Notification getNotification()
  {
    return notification;
  }
  
  public String getOpPkg()
  {
    return opPkg;
  }
  
  public String getOverrideGroupKey()
  {
    return overrideGroupKey;
  }
  
  public Context getPackageContext(Context paramContext)
  {
    if (mContext == null) {
      try
      {
        mContext = paramContext.createApplicationContext(paramContext.getPackageManager().getApplicationInfoAsUser(pkg, 8192, getUserId()), 4);
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        mContext = null;
      }
    }
    if (mContext == null) {
      mContext = paramContext;
    }
    return mContext;
  }
  
  public String getPackageName()
  {
    return pkg;
  }
  
  public long getPostTime()
  {
    return postTime;
  }
  
  public String getTag()
  {
    return tag;
  }
  
  public int getUid()
  {
    return uid;
  }
  
  public UserHandle getUser()
  {
    return user;
  }
  
  @Deprecated
  public int getUserId()
  {
    return user.getIdentifier();
  }
  
  public boolean isAppGroup()
  {
    return (getNotification().getGroup() != null) || (getNotification().getSortKey() != null);
  }
  
  public boolean isClearable()
  {
    boolean bool;
    if (((notification.flags & 0x2) == 0) && ((notification.flags & 0x20) == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGroup()
  {
    return (overrideGroupKey != null) || (isAppGroup());
  }
  
  public boolean isOngoing()
  {
    boolean bool;
    if ((notification.flags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setOverrideGroupKey(String paramString)
  {
    overrideGroupKey = paramString;
    groupKey = groupKey();
  }
  
  public String toString()
  {
    return String.format("StatusBarNotification(pkg=%s user=%s id=%d tag=%s key=%s: %s)", new Object[] { pkg, user, Integer.valueOf(id), tag, key, notification });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(pkg);
    paramParcel.writeString(opPkg);
    paramParcel.writeInt(id);
    if (tag != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(tag);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(uid);
    paramParcel.writeInt(initialPid);
    notification.writeToParcel(paramParcel, paramInt);
    user.writeToParcel(paramParcel, paramInt);
    paramParcel.writeLong(postTime);
    if (overrideGroupKey != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(overrideGroupKey);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
