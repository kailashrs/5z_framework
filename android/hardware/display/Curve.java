package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Curve
  implements Parcelable
{
  public static final Parcelable.Creator<Curve> CREATOR = new Parcelable.Creator()
  {
    public Curve createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Curve(paramAnonymousParcel.createFloatArray(), paramAnonymousParcel.createFloatArray());
    }
    
    public Curve[] newArray(int paramAnonymousInt)
    {
      return new Curve[paramAnonymousInt];
    }
  };
  private final float[] mX;
  private final float[] mY;
  
  public Curve(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    mX = paramArrayOfFloat1;
    mY = paramArrayOfFloat2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public float[] getX()
  {
    return mX;
  }
  
  public float[] getY()
  {
    return mY;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloatArray(mX);
    paramParcel.writeFloatArray(mY);
  }
}
