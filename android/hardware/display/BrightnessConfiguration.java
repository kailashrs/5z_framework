package android.hardware.display;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pair;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.Objects;

@SystemApi
public final class BrightnessConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<BrightnessConfiguration> CREATOR = new Parcelable.Creator()
  {
    public BrightnessConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      BrightnessConfiguration.Builder localBuilder = new BrightnessConfiguration.Builder(paramAnonymousParcel.createFloatArray(), paramAnonymousParcel.createFloatArray());
      localBuilder.setDescription(paramAnonymousParcel.readString());
      return localBuilder.build();
    }
    
    public BrightnessConfiguration[] newArray(int paramAnonymousInt)
    {
      return new BrightnessConfiguration[paramAnonymousInt];
    }
  };
  private final String mDescription;
  private final float[] mLux;
  private final float[] mNits;
  
  private BrightnessConfiguration(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, String paramString)
  {
    mLux = paramArrayOfFloat1;
    mNits = paramArrayOfFloat2;
    mDescription = paramString;
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
    if (!(paramObject instanceof BrightnessConfiguration)) {
      return false;
    }
    paramObject = (BrightnessConfiguration)paramObject;
    if ((!Arrays.equals(mLux, mLux)) || (!Arrays.equals(mNits, mNits)) || (!Objects.equals(mDescription, mDescription))) {
      bool = false;
    }
    return bool;
  }
  
  public Pair<float[], float[]> getCurve()
  {
    return Pair.create(Arrays.copyOf(mLux, mLux.length), Arrays.copyOf(mNits, mNits.length));
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public int hashCode()
  {
    int i = (1 * 31 + Arrays.hashCode(mLux)) * 31 + Arrays.hashCode(mNits);
    int j = i;
    if (mDescription != null) {
      j = i * 31 + mDescription.hashCode();
    }
    return j;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("BrightnessConfiguration{[");
    int i = mLux.length;
    for (int j = 0; j < i; j++)
    {
      if (j != 0) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append("(");
      localStringBuilder.append(mLux[j]);
      localStringBuilder.append(", ");
      localStringBuilder.append(mNits[j]);
      localStringBuilder.append(")");
    }
    localStringBuilder.append("], '");
    if (mDescription != null) {
      localStringBuilder.append(mDescription);
    }
    localStringBuilder.append("'}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloatArray(mLux);
    paramParcel.writeFloatArray(mNits);
    paramParcel.writeString(mDescription);
  }
  
  public static class Builder
  {
    private float[] mCurveLux;
    private float[] mCurveNits;
    private String mDescription;
    
    public Builder() {}
    
    public Builder(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
    {
      setCurve(paramArrayOfFloat1, paramArrayOfFloat2);
    }
    
    private static void checkMonotonic(float[] paramArrayOfFloat, boolean paramBoolean, String paramString)
    {
      int i = paramArrayOfFloat.length;
      int j = 1;
      if (i <= 1) {
        return;
      }
      float f = paramArrayOfFloat[0];
      while (j < paramArrayOfFloat.length) {
        if ((f <= paramArrayOfFloat[j]) && ((f != paramArrayOfFloat[j]) || (!paramBoolean)))
        {
          f = paramArrayOfFloat[j];
          j++;
        }
        else
        {
          if (paramBoolean) {
            paramArrayOfFloat = "strictly increasing";
          } else {
            paramArrayOfFloat = "monotonic";
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(paramString);
          localStringBuilder.append(" values must be ");
          localStringBuilder.append(paramArrayOfFloat);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
      }
    }
    
    public BrightnessConfiguration build()
    {
      if ((mCurveLux != null) && (mCurveNits != null)) {
        return new BrightnessConfiguration(mCurveLux, mCurveNits, mDescription, null);
      }
      throw new IllegalStateException("A curve must be set!");
    }
    
    public Builder setCurve(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
    {
      Preconditions.checkNotNull(paramArrayOfFloat1);
      Preconditions.checkNotNull(paramArrayOfFloat2);
      if ((paramArrayOfFloat1.length != 0) && (paramArrayOfFloat2.length != 0))
      {
        if (paramArrayOfFloat1.length == paramArrayOfFloat2.length)
        {
          if (paramArrayOfFloat1[0] == 0.0F)
          {
            Preconditions.checkArrayElementsInRange(paramArrayOfFloat1, 0.0F, Float.MAX_VALUE, "lux");
            Preconditions.checkArrayElementsInRange(paramArrayOfFloat2, 0.0F, Float.MAX_VALUE, "nits");
            checkMonotonic(paramArrayOfFloat1, true, "lux");
            checkMonotonic(paramArrayOfFloat2, false, "nits");
            mCurveLux = paramArrayOfFloat1;
            mCurveNits = paramArrayOfFloat2;
            return this;
          }
          throw new IllegalArgumentException("Initial control point must be for 0 lux");
        }
        throw new IllegalArgumentException("Lux and nits arrays must be the same length");
      }
      throw new IllegalArgumentException("Lux and nits arrays must not be empty");
    }
    
    public Builder setDescription(String paramString)
    {
      mDescription = paramString;
      return this;
    }
  }
}
