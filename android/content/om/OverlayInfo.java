package android.content.om;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class OverlayInfo
  implements Parcelable
{
  public static final String CATEGORY_THEME = "android.theme";
  public static final Parcelable.Creator<OverlayInfo> CREATOR = new Parcelable.Creator()
  {
    public OverlayInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OverlayInfo(paramAnonymousParcel);
    }
    
    public OverlayInfo[] newArray(int paramAnonymousInt)
    {
      return new OverlayInfo[paramAnonymousInt];
    }
  };
  public static final int STATE_DISABLED = 2;
  public static final int STATE_ENABLED = 3;
  public static final int STATE_ENABLED_STATIC = 6;
  public static final int STATE_MISSING_TARGET = 0;
  public static final int STATE_NO_IDMAP = 1;
  public static final int STATE_OVERLAY_UPGRADING = 5;
  public static final int STATE_TARGET_UPGRADING = 4;
  public static final int STATE_UNKNOWN = -1;
  public final String baseCodePath;
  public final String category;
  public final boolean isStatic;
  public final String packageName;
  public final int priority;
  public final int state;
  public final String targetPackageName;
  public final int userId;
  
  public OverlayInfo(OverlayInfo paramOverlayInfo, int paramInt)
  {
    this(packageName, targetPackageName, category, baseCodePath, paramInt, userId, priority, isStatic);
  }
  
  public OverlayInfo(Parcel paramParcel)
  {
    packageName = paramParcel.readString();
    targetPackageName = paramParcel.readString();
    category = paramParcel.readString();
    baseCodePath = paramParcel.readString();
    state = paramParcel.readInt();
    userId = paramParcel.readInt();
    priority = paramParcel.readInt();
    isStatic = paramParcel.readBoolean();
    ensureValidState();
  }
  
  public OverlayInfo(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    packageName = paramString1;
    targetPackageName = paramString2;
    category = paramString3;
    baseCodePath = paramString4;
    state = paramInt1;
    userId = paramInt2;
    priority = paramInt3;
    isStatic = paramBoolean;
    ensureValidState();
  }
  
  private void ensureValidState()
  {
    if (packageName != null)
    {
      if (targetPackageName != null)
      {
        if (baseCodePath != null)
        {
          switch (state)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("State ");
            localStringBuilder.append(state);
            localStringBuilder.append(" is not a valid state");
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
          return;
        }
        throw new IllegalArgumentException("baseCodePath must not be null");
      }
      throw new IllegalArgumentException("targetPackageName must not be null");
    }
    throw new IllegalArgumentException("packageName must not be null");
  }
  
  public static String stateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "<unknown state>";
    case 6: 
      return "STATE_ENABLED_STATIC";
    case 5: 
      return "STATE_OVERLAY_UPGRADING";
    case 4: 
      return "STATE_TARGET_UPGRADING";
    case 3: 
      return "STATE_ENABLED";
    case 2: 
      return "STATE_DISABLED";
    case 1: 
      return "STATE_NO_IDMAP";
    case 0: 
      return "STATE_MISSING_TARGET";
    }
    return "STATE_UNKNOWN";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (OverlayInfo)paramObject;
    if (userId != userId) {
      return false;
    }
    if (state != state) {
      return false;
    }
    if (!packageName.equals(packageName)) {
      return false;
    }
    if (!targetPackageName.equals(targetPackageName)) {
      return false;
    }
    if (!category.equals(category)) {
      return false;
    }
    return baseCodePath.equals(baseCodePath);
  }
  
  public int hashCode()
  {
    int i = userId;
    int j = state;
    String str = packageName;
    int k = 0;
    int m;
    if (str == null) {
      m = 0;
    } else {
      m = packageName.hashCode();
    }
    int n;
    if (targetPackageName == null) {
      n = 0;
    } else {
      n = targetPackageName.hashCode();
    }
    int i1;
    if (category == null) {
      i1 = 0;
    } else {
      i1 = category.hashCode();
    }
    if (baseCodePath != null) {
      k = baseCodePath.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * 1 + i) + j) + m) + n) + i1) + k;
  }
  
  public boolean isEnabled()
  {
    int i = state;
    return (i == 3) || (i == 6);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("OverlayInfo { overlay=");
    localStringBuilder.append(packageName);
    localStringBuilder.append(", target=");
    localStringBuilder.append(targetPackageName);
    localStringBuilder.append(", state=");
    localStringBuilder.append(state);
    localStringBuilder.append(" (");
    localStringBuilder.append(stateToString(state));
    localStringBuilder.append("), userId=");
    localStringBuilder.append(userId);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(packageName);
    paramParcel.writeString(targetPackageName);
    paramParcel.writeString(category);
    paramParcel.writeString(baseCodePath);
    paramParcel.writeInt(state);
    paramParcel.writeInt(userId);
    paramParcel.writeInt(priority);
    paramParcel.writeBoolean(isStatic);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}
