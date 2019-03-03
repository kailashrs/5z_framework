package android.os;

public class Temperature
  implements Parcelable
{
  public static final Parcelable.Creator<Temperature> CREATOR = new Parcelable.Creator()
  {
    public Temperature createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Temperature(paramAnonymousParcel, null);
    }
    
    public Temperature[] newArray(int paramAnonymousInt)
    {
      return new Temperature[paramAnonymousInt];
    }
  };
  private int mType;
  private float mValue;
  
  public Temperature()
  {
    this(-3.4028235E38F, Integer.MIN_VALUE);
  }
  
  public Temperature(float paramFloat, int paramInt)
  {
    mValue = paramFloat;
    mType = paramInt;
  }
  
  private Temperature(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public float getValue()
  {
    return mValue;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mValue = paramParcel.readFloat();
    mType = paramParcel.readInt();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(mValue);
    paramParcel.writeInt(mType);
  }
}
