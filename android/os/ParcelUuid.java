package android.os;

import java.util.UUID;

public final class ParcelUuid
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelUuid> CREATOR = new Parcelable.Creator()
  {
    public ParcelUuid createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParcelUuid(new UUID(paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong()));
    }
    
    public ParcelUuid[] newArray(int paramAnonymousInt)
    {
      return new ParcelUuid[paramAnonymousInt];
    }
  };
  private final UUID mUuid;
  
  public ParcelUuid(UUID paramUUID)
  {
    mUuid = paramUUID;
  }
  
  public static ParcelUuid fromString(String paramString)
  {
    return new ParcelUuid(UUID.fromString(paramString));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof ParcelUuid)) {
      return false;
    }
    paramObject = (ParcelUuid)paramObject;
    return mUuid.equals(mUuid);
  }
  
  public UUID getUuid()
  {
    return mUuid;
  }
  
  public int hashCode()
  {
    return mUuid.hashCode();
  }
  
  public String toString()
  {
    return mUuid.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mUuid.getMostSignificantBits());
    paramParcel.writeLong(mUuid.getLeastSignificantBits());
  }
}
