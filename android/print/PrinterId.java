package android.print;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public final class PrinterId
  implements Parcelable
{
  public static final Parcelable.Creator<PrinterId> CREATOR = new Parcelable.Creator()
  {
    public PrinterId createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PrinterId(paramAnonymousParcel, null);
    }
    
    public PrinterId[] newArray(int paramAnonymousInt)
    {
      return new PrinterId[paramAnonymousInt];
    }
  };
  private final String mLocalId;
  private final ComponentName mServiceName;
  
  public PrinterId(ComponentName paramComponentName, String paramString)
  {
    mServiceName = paramComponentName;
    mLocalId = paramString;
  }
  
  private PrinterId(Parcel paramParcel)
  {
    mServiceName = ((ComponentName)Preconditions.checkNotNull((ComponentName)paramParcel.readParcelable(null)));
    mLocalId = ((String)Preconditions.checkNotNull(paramParcel.readString()));
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
    paramObject = (PrinterId)paramObject;
    if (!mServiceName.equals(mServiceName)) {
      return false;
    }
    return mLocalId.equals(mLocalId);
  }
  
  public String getLocalId()
  {
    return mLocalId;
  }
  
  public ComponentName getServiceName()
  {
    return mServiceName;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 1 + mServiceName.hashCode()) + mLocalId.hashCode();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PrinterId{");
    localStringBuilder.append("serviceName=");
    localStringBuilder.append(mServiceName.flattenToString());
    localStringBuilder.append(", localId=");
    localStringBuilder.append(mLocalId);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mServiceName, paramInt);
    paramParcel.writeString(mLocalId);
  }
}
