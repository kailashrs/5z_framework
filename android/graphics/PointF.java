package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PointF
  implements Parcelable
{
  public static final Parcelable.Creator<PointF> CREATOR = new Parcelable.Creator()
  {
    public PointF createFromParcel(Parcel paramAnonymousParcel)
    {
      PointF localPointF = new PointF();
      localPointF.readFromParcel(paramAnonymousParcel);
      return localPointF;
    }
    
    public PointF[] newArray(int paramAnonymousInt)
    {
      return new PointF[paramAnonymousInt];
    }
  };
  public float x;
  public float y;
  
  public PointF() {}
  
  public PointF(float paramFloat1, float paramFloat2)
  {
    x = paramFloat1;
    y = paramFloat2;
  }
  
  public PointF(Point paramPoint)
  {
    x = x;
    y = y;
  }
  
  public static float length(float paramFloat1, float paramFloat2)
  {
    return (float)Math.hypot(paramFloat1, paramFloat2);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public final boolean equals(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if ((x == paramFloat1) && (y == paramFloat2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (PointF)paramObject;
      if (Float.compare(x, x) != 0) {
        return false;
      }
      return Float.compare(y, y) == 0;
    }
    return false;
  }
  
  public int hashCode()
  {
    float f = x;
    int i = 0;
    int j;
    if (f != 0.0F) {
      j = Float.floatToIntBits(x);
    } else {
      j = 0;
    }
    if (y != 0.0F) {
      i = Float.floatToIntBits(y);
    }
    return 31 * j + i;
  }
  
  public final float length()
  {
    return length(x, y);
  }
  
  public final void negate()
  {
    x = (-x);
    y = (-y);
  }
  
  public final void offset(float paramFloat1, float paramFloat2)
  {
    x += paramFloat1;
    y += paramFloat2;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    x = paramParcel.readFloat();
    y = paramParcel.readFloat();
  }
  
  public final void set(float paramFloat1, float paramFloat2)
  {
    x = paramFloat1;
    y = paramFloat2;
  }
  
  public final void set(PointF paramPointF)
  {
    x = x;
    y = y;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PointF(");
    localStringBuilder.append(x);
    localStringBuilder.append(", ");
    localStringBuilder.append(y);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(x);
    paramParcel.writeFloat(y);
  }
}
