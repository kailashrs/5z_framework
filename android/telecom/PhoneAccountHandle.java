package android.telecom;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.UserHandle;
import java.util.Objects;

public final class PhoneAccountHandle
  implements Parcelable
{
  public static final Parcelable.Creator<PhoneAccountHandle> CREATOR = new Parcelable.Creator()
  {
    public PhoneAccountHandle createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PhoneAccountHandle(paramAnonymousParcel, null);
    }
    
    public PhoneAccountHandle[] newArray(int paramAnonymousInt)
    {
      return new PhoneAccountHandle[paramAnonymousInt];
    }
  };
  private final ComponentName mComponentName;
  private final String mId;
  private final UserHandle mUserHandle;
  
  public PhoneAccountHandle(ComponentName paramComponentName, String paramString)
  {
    this(paramComponentName, paramString, Process.myUserHandle());
  }
  
  public PhoneAccountHandle(ComponentName paramComponentName, String paramString, UserHandle paramUserHandle)
  {
    checkParameters(paramComponentName, paramUserHandle);
    mComponentName = paramComponentName;
    mId = paramString;
    mUserHandle = paramUserHandle;
  }
  
  private PhoneAccountHandle(Parcel paramParcel)
  {
    this((ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel), paramParcel.readString(), (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel));
  }
  
  private void checkParameters(ComponentName paramComponentName, UserHandle paramUserHandle)
  {
    if (paramComponentName == null) {
      android.util.Log.w("PhoneAccountHandle", new Exception("PhoneAccountHandle has been created with null ComponentName!"));
    }
    if (paramUserHandle == null) {
      android.util.Log.w("PhoneAccountHandle", new Exception("PhoneAccountHandle has been created with null UserHandle!"));
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if ((paramObject != null) && ((paramObject instanceof PhoneAccountHandle)) && (Objects.equals(((PhoneAccountHandle)paramObject).getComponentName(), getComponentName())) && (Objects.equals(((PhoneAccountHandle)paramObject).getId(), getId())) && (Objects.equals(((PhoneAccountHandle)paramObject).getUserHandle(), getUserHandle()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public ComponentName getComponentName()
  {
    return mComponentName;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public UserHandle getUserHandle()
  {
    return mUserHandle;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mComponentName, mId, mUserHandle });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mComponentName);
    localStringBuilder.append(", ");
    localStringBuilder.append(Log.pii(mId));
    localStringBuilder.append(", ");
    localStringBuilder.append(mUserHandle);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mComponentName.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mId);
    mUserHandle.writeToParcel(paramParcel, paramInt);
  }
}
