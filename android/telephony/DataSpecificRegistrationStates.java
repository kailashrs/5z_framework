package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class DataSpecificRegistrationStates
  implements Parcelable
{
  public static final Parcelable.Creator<DataSpecificRegistrationStates> CREATOR = new Parcelable.Creator()
  {
    public DataSpecificRegistrationStates createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DataSpecificRegistrationStates(paramAnonymousParcel, null);
    }
    
    public DataSpecificRegistrationStates[] newArray(int paramAnonymousInt)
    {
      return new DataSpecificRegistrationStates[paramAnonymousInt];
    }
  };
  public final int maxDataCalls;
  
  DataSpecificRegistrationStates(int paramInt)
  {
    maxDataCalls = paramInt;
  }
  
  private DataSpecificRegistrationStates(Parcel paramParcel)
  {
    maxDataCalls = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof DataSpecificRegistrationStates)))
    {
      paramObject = (DataSpecificRegistrationStates)paramObject;
      if (maxDataCalls != maxDataCalls) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(maxDataCalls) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DataSpecificRegistrationStates { mMaxDataCalls=");
    localStringBuilder.append(maxDataCalls);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(maxDataCalls);
  }
}
