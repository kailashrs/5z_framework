package android.app;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class AutomaticZenRule
  implements Parcelable
{
  public static final Parcelable.Creator<AutomaticZenRule> CREATOR = new Parcelable.Creator()
  {
    public AutomaticZenRule createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AutomaticZenRule(paramAnonymousParcel);
    }
    
    public AutomaticZenRule[] newArray(int paramAnonymousInt)
    {
      return new AutomaticZenRule[paramAnonymousInt];
    }
  };
  private Uri conditionId;
  private long creationTime;
  private boolean enabled;
  private int interruptionFilter;
  private String name;
  private ComponentName owner;
  
  public AutomaticZenRule(Parcel paramParcel)
  {
    boolean bool = false;
    enabled = false;
    if (paramParcel.readInt() == 1) {
      bool = true;
    }
    enabled = bool;
    if (paramParcel.readInt() == 1) {
      name = paramParcel.readString();
    }
    interruptionFilter = paramParcel.readInt();
    conditionId = ((Uri)paramParcel.readParcelable(null));
    owner = ((ComponentName)paramParcel.readParcelable(null));
    creationTime = paramParcel.readLong();
  }
  
  public AutomaticZenRule(String paramString, ComponentName paramComponentName, Uri paramUri, int paramInt, boolean paramBoolean)
  {
    enabled = false;
    name = paramString;
    owner = paramComponentName;
    conditionId = paramUri;
    interruptionFilter = paramInt;
    enabled = paramBoolean;
  }
  
  public AutomaticZenRule(String paramString, ComponentName paramComponentName, Uri paramUri, int paramInt, boolean paramBoolean, long paramLong)
  {
    this(paramString, paramComponentName, paramUri, paramInt, paramBoolean);
    creationTime = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof AutomaticZenRule)) {
      return false;
    }
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    paramObject = (AutomaticZenRule)paramObject;
    if ((enabled != enabled) || (!Objects.equals(name, name)) || (interruptionFilter != interruptionFilter) || (!Objects.equals(conditionId, conditionId)) || (!Objects.equals(owner, owner)) || (creationTime != creationTime)) {
      bool = false;
    }
    return bool;
  }
  
  public Uri getConditionId()
  {
    return conditionId;
  }
  
  public long getCreationTime()
  {
    return creationTime;
  }
  
  public int getInterruptionFilter()
  {
    return interruptionFilter;
  }
  
  public String getName()
  {
    return name;
  }
  
  public ComponentName getOwner()
  {
    return owner;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Boolean.valueOf(enabled), name, Integer.valueOf(interruptionFilter), conditionId, owner, Long.valueOf(creationTime) });
  }
  
  public boolean isEnabled()
  {
    return enabled;
  }
  
  public void setConditionId(Uri paramUri)
  {
    conditionId = paramUri;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    enabled = paramBoolean;
  }
  
  public void setInterruptionFilter(int paramInt)
  {
    interruptionFilter = paramInt;
  }
  
  public void setName(String paramString)
  {
    name = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(AutomaticZenRule.class.getSimpleName());
    localStringBuilder.append('[');
    localStringBuilder.append("enabled=");
    localStringBuilder.append(enabled);
    localStringBuilder.append(",name=");
    localStringBuilder.append(name);
    localStringBuilder.append(",interruptionFilter=");
    localStringBuilder.append(interruptionFilter);
    localStringBuilder.append(",conditionId=");
    localStringBuilder.append(conditionId);
    localStringBuilder.append(",owner=");
    localStringBuilder.append(owner);
    localStringBuilder.append(",creationTime=");
    localStringBuilder.append(creationTime);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(enabled);
    if (name != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(name);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(interruptionFilter);
    paramParcel.writeParcelable(conditionId, 0);
    paramParcel.writeParcelable(owner, 0);
    paramParcel.writeLong(creationTime);
  }
}
