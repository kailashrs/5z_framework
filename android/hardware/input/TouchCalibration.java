package android.hardware.input;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TouchCalibration
  implements Parcelable
{
  public static final Parcelable.Creator<TouchCalibration> CREATOR = new Parcelable.Creator()
  {
    public TouchCalibration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TouchCalibration(paramAnonymousParcel);
    }
    
    public TouchCalibration[] newArray(int paramAnonymousInt)
    {
      return new TouchCalibration[paramAnonymousInt];
    }
  };
  public static final TouchCalibration IDENTITY = new TouchCalibration();
  private final float mXOffset;
  private final float mXScale;
  private final float mXYMix;
  private final float mYOffset;
  private final float mYScale;
  private final float mYXMix;
  
  public TouchCalibration()
  {
    this(1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
  }
  
  public TouchCalibration(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    mXScale = paramFloat1;
    mXYMix = paramFloat2;
    mXOffset = paramFloat3;
    mYXMix = paramFloat4;
    mYScale = paramFloat5;
    mYOffset = paramFloat6;
  }
  
  public TouchCalibration(Parcel paramParcel)
  {
    mXScale = paramParcel.readFloat();
    mXYMix = paramParcel.readFloat();
    mXOffset = paramParcel.readFloat();
    mYXMix = paramParcel.readFloat();
    mYScale = paramParcel.readFloat();
    mYOffset = paramParcel.readFloat();
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
    if ((paramObject instanceof TouchCalibration))
    {
      paramObject = (TouchCalibration)paramObject;
      if ((mXScale != mXScale) || (mXYMix != mXYMix) || (mXOffset != mXOffset) || (mYXMix != mYXMix) || (mYScale != mYScale) || (mYOffset != mYOffset)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public float[] getAffineTransform()
  {
    return new float[] { mXScale, mXYMix, mXOffset, mYXMix, mYScale, mYOffset };
  }
  
  public int hashCode()
  {
    return Float.floatToIntBits(mXScale) ^ Float.floatToIntBits(mXYMix) ^ Float.floatToIntBits(mXOffset) ^ Float.floatToIntBits(mYXMix) ^ Float.floatToIntBits(mYScale) ^ Float.floatToIntBits(mYOffset);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(mXScale);
    paramParcel.writeFloat(mXYMix);
    paramParcel.writeFloat(mXOffset);
    paramParcel.writeFloat(mYXMix);
    paramParcel.writeFloat(mYScale);
    paramParcel.writeFloat(mYOffset);
  }
}
