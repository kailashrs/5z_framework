package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Geofence
  implements Parcelable
{
  public static final Parcelable.Creator<Geofence> CREATOR = new Parcelable.Creator()
  {
    public Geofence createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      double d1 = paramAnonymousParcel.readDouble();
      double d2 = paramAnonymousParcel.readDouble();
      float f = paramAnonymousParcel.readFloat();
      Geofence.checkType(i);
      return Geofence.createCircle(d1, d2, f);
    }
    
    public Geofence[] newArray(int paramAnonymousInt)
    {
      return new Geofence[paramAnonymousInt];
    }
  };
  public static final int TYPE_HORIZONTAL_CIRCLE = 1;
  private final double mLatitude;
  private final double mLongitude;
  private final float mRadius;
  private final int mType;
  
  private Geofence(double paramDouble1, double paramDouble2, float paramFloat)
  {
    checkRadius(paramFloat);
    checkLatLong(paramDouble1, paramDouble2);
    mType = 1;
    mLatitude = paramDouble1;
    mLongitude = paramDouble2;
    mRadius = paramFloat;
  }
  
  private static void checkLatLong(double paramDouble1, double paramDouble2)
  {
    if ((paramDouble1 <= 90.0D) && (paramDouble1 >= -90.0D))
    {
      if ((paramDouble2 <= 180.0D) && (paramDouble2 >= -180.0D)) {
        return;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid longitude: ");
      localStringBuilder.append(paramDouble2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid latitude: ");
    localStringBuilder.append(paramDouble1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkRadius(float paramFloat)
  {
    if (paramFloat > 0.0F) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid radius: ");
    localStringBuilder.append(paramFloat);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkType(int paramInt)
  {
    if (paramInt == 1) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid type: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static Geofence createCircle(double paramDouble1, double paramDouble2, float paramFloat)
  {
    return new Geofence(paramDouble1, paramDouble2, paramFloat);
  }
  
  private static String typeToString(int paramInt)
  {
    if (paramInt != 1)
    {
      checkType(paramInt);
      return null;
    }
    return "CIRCLE";
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
    if (!(paramObject instanceof Geofence)) {
      return false;
    }
    paramObject = (Geofence)paramObject;
    if (mRadius != mRadius) {
      return false;
    }
    if (mLatitude != mLatitude) {
      return false;
    }
    if (mLongitude != mLongitude) {
      return false;
    }
    return mType == mType;
  }
  
  public double getLatitude()
  {
    return mLatitude;
  }
  
  public double getLongitude()
  {
    return mLongitude;
  }
  
  public float getRadius()
  {
    return mRadius;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    long l = Double.doubleToLongBits(mLatitude);
    int i = (int)(l >>> 32 ^ l);
    l = Double.doubleToLongBits(mLongitude);
    return 31 * (31 * (31 * (31 * 1 + i) + (int)(l >>> 32 ^ l)) + Float.floatToIntBits(mRadius)) + mType;
  }
  
  public String toString()
  {
    return String.format("Geofence[%s %.6f, %.6f %.0fm]", new Object[] { typeToString(mType), Double.valueOf(mLatitude), Double.valueOf(mLongitude), Float.valueOf(mRadius) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeDouble(mLatitude);
    paramParcel.writeDouble(mLongitude);
    paramParcel.writeFloat(mRadius);
  }
}
