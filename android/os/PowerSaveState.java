package android.os;

public class PowerSaveState
  implements Parcelable
{
  public static final Parcelable.Creator<PowerSaveState> CREATOR = new Parcelable.Creator()
  {
    public PowerSaveState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PowerSaveState(paramAnonymousParcel);
    }
    
    public PowerSaveState[] newArray(int paramAnonymousInt)
    {
      return new PowerSaveState[paramAnonymousInt];
    }
  };
  public final boolean batterySaverEnabled;
  public final float brightnessFactor;
  public final boolean globalBatterySaverEnabled;
  public final int gpsMode;
  
  public PowerSaveState(Parcel paramParcel)
  {
    int i = paramParcel.readByte();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    batterySaverEnabled = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    }
    globalBatterySaverEnabled = bool2;
    gpsMode = paramParcel.readInt();
    brightnessFactor = paramParcel.readFloat();
  }
  
  public PowerSaveState(Builder paramBuilder)
  {
    batterySaverEnabled = mBatterySaverEnabled;
    gpsMode = mGpsMode;
    brightnessFactor = mBrightnessFactor;
    globalBatterySaverEnabled = mGlobalBatterySaverEnabled;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte((byte)batterySaverEnabled);
    paramParcel.writeByte((byte)globalBatterySaverEnabled);
    paramParcel.writeInt(gpsMode);
    paramParcel.writeFloat(brightnessFactor);
  }
  
  public static final class Builder
  {
    private boolean mBatterySaverEnabled = false;
    private float mBrightnessFactor = 0.5F;
    private boolean mGlobalBatterySaverEnabled = false;
    private int mGpsMode = 0;
    
    public Builder() {}
    
    public PowerSaveState build()
    {
      return new PowerSaveState(this);
    }
    
    public Builder setBatterySaverEnabled(boolean paramBoolean)
    {
      mBatterySaverEnabled = paramBoolean;
      return this;
    }
    
    public Builder setBrightnessFactor(float paramFloat)
    {
      mBrightnessFactor = paramFloat;
      return this;
    }
    
    public Builder setGlobalBatterySaverEnabled(boolean paramBoolean)
    {
      mGlobalBatterySaverEnabled = paramBoolean;
      return this;
    }
    
    public Builder setGpsMode(int paramInt)
    {
      mGpsMode = paramInt;
      return this;
    }
  }
}
