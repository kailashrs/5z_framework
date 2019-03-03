package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class SyncAdapterType
  implements Parcelable
{
  public static final Parcelable.Creator<SyncAdapterType> CREATOR = new Parcelable.Creator()
  {
    public SyncAdapterType createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SyncAdapterType(paramAnonymousParcel);
    }
    
    public SyncAdapterType[] newArray(int paramAnonymousInt)
    {
      return new SyncAdapterType[paramAnonymousInt];
    }
  };
  public final String accountType;
  private final boolean allowParallelSyncs;
  public final String authority;
  private final boolean isAlwaysSyncable;
  public final boolean isKey;
  private final String packageName;
  private final String settingsActivity;
  private final boolean supportsUploading;
  private final boolean userVisible;
  
  public SyncAdapterType(Parcel paramParcel)
  {
    this(str1, str2, bool1, bool2, bool3, bool4, paramParcel.readString(), paramParcel.readString());
  }
  
  private SyncAdapterType(String paramString1, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString2))
      {
        authority = paramString1;
        accountType = paramString2;
        userVisible = true;
        supportsUploading = true;
        isAlwaysSyncable = false;
        allowParallelSyncs = false;
        settingsActivity = null;
        isKey = true;
        packageName = null;
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("the accountType must not be empty: ");
      paramString1.append(paramString2);
      throw new IllegalArgumentException(paramString1.toString());
    }
    paramString2 = new StringBuilder();
    paramString2.append("the authority must not be empty: ");
    paramString2.append(paramString1);
    throw new IllegalArgumentException(paramString2.toString());
  }
  
  public SyncAdapterType(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString2))
      {
        authority = paramString1;
        accountType = paramString2;
        userVisible = paramBoolean1;
        supportsUploading = paramBoolean2;
        isAlwaysSyncable = false;
        allowParallelSyncs = false;
        settingsActivity = null;
        isKey = false;
        packageName = null;
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("the accountType must not be empty: ");
      paramString1.append(paramString2);
      throw new IllegalArgumentException(paramString1.toString());
    }
    paramString2 = new StringBuilder();
    paramString2.append("the authority must not be empty: ");
    paramString2.append(paramString1);
    throw new IllegalArgumentException(paramString2.toString());
  }
  
  public SyncAdapterType(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String paramString3, String paramString4)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString2))
      {
        authority = paramString1;
        accountType = paramString2;
        userVisible = paramBoolean1;
        supportsUploading = paramBoolean2;
        isAlwaysSyncable = paramBoolean3;
        allowParallelSyncs = paramBoolean4;
        settingsActivity = paramString3;
        isKey = false;
        packageName = paramString4;
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("the accountType must not be empty: ");
      paramString1.append(paramString2);
      throw new IllegalArgumentException(paramString1.toString());
    }
    paramString2 = new StringBuilder();
    paramString2.append("the authority must not be empty: ");
    paramString2.append(paramString1);
    throw new IllegalArgumentException(paramString2.toString());
  }
  
  public static SyncAdapterType newKey(String paramString1, String paramString2)
  {
    return new SyncAdapterType(paramString1, paramString2);
  }
  
  public boolean allowParallelSyncs()
  {
    if (!isKey) {
      return allowParallelSyncs;
    }
    throw new IllegalStateException("this method is not allowed to be called when this is a key");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof SyncAdapterType)) {
      return false;
    }
    paramObject = (SyncAdapterType)paramObject;
    if ((!authority.equals(authority)) || (!accountType.equals(accountType))) {
      bool = false;
    }
    return bool;
  }
  
  public String getPackageName()
  {
    return packageName;
  }
  
  public String getSettingsActivity()
  {
    if (!isKey) {
      return settingsActivity;
    }
    throw new IllegalStateException("this method is not allowed to be called when this is a key");
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + authority.hashCode()) + accountType.hashCode();
  }
  
  public boolean isAlwaysSyncable()
  {
    if (!isKey) {
      return isAlwaysSyncable;
    }
    throw new IllegalStateException("this method is not allowed to be called when this is a key");
  }
  
  public boolean isUserVisible()
  {
    if (!isKey) {
      return userVisible;
    }
    throw new IllegalStateException("this method is not allowed to be called when this is a key");
  }
  
  public boolean supportsUploading()
  {
    if (!isKey) {
      return supportsUploading;
    }
    throw new IllegalStateException("this method is not allowed to be called when this is a key");
  }
  
  public String toString()
  {
    if (isKey)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("SyncAdapterType Key {name=");
      localStringBuilder.append(authority);
      localStringBuilder.append(", type=");
      localStringBuilder.append(accountType);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SyncAdapterType {name=");
    localStringBuilder.append(authority);
    localStringBuilder.append(", type=");
    localStringBuilder.append(accountType);
    localStringBuilder.append(", userVisible=");
    localStringBuilder.append(userVisible);
    localStringBuilder.append(", supportsUploading=");
    localStringBuilder.append(supportsUploading);
    localStringBuilder.append(", isAlwaysSyncable=");
    localStringBuilder.append(isAlwaysSyncable);
    localStringBuilder.append(", allowParallelSyncs=");
    localStringBuilder.append(allowParallelSyncs);
    localStringBuilder.append(", settingsActivity=");
    localStringBuilder.append(settingsActivity);
    localStringBuilder.append(", packageName=");
    localStringBuilder.append(packageName);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (!isKey)
    {
      paramParcel.writeString(authority);
      paramParcel.writeString(accountType);
      paramParcel.writeInt(userVisible);
      paramParcel.writeInt(supportsUploading);
      paramParcel.writeInt(isAlwaysSyncable);
      paramParcel.writeInt(allowParallelSyncs);
      paramParcel.writeString(settingsActivity);
      paramParcel.writeString(packageName);
      return;
    }
    throw new IllegalStateException("keys aren't parcelable");
  }
}
