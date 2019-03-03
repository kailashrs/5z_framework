package android.view;

import android.os.Build.FEATURES;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pools.SynchronizedPool;

public class MagnificationSpec
  implements Parcelable
{
  public static final Parcelable.Creator<MagnificationSpec> CREATOR = new Parcelable.Creator()
  {
    public MagnificationSpec createFromParcel(Parcel paramAnonymousParcel)
    {
      MagnificationSpec localMagnificationSpec = MagnificationSpec.obtain();
      localMagnificationSpec.initFromParcel(paramAnonymousParcel);
      return localMagnificationSpec;
    }
    
    public MagnificationSpec[] newArray(int paramAnonymousInt)
    {
      return new MagnificationSpec[paramAnonymousInt];
    }
  };
  private static final int MAX_POOL_SIZE = 20;
  private static final Pools.SynchronizedPool<MagnificationSpec> sPool = new Pools.SynchronizedPool(20);
  public float offsetX;
  public float offsetY;
  public float scale = 1.0F;
  
  private MagnificationSpec() {}
  
  private void initFromParcel(Parcel paramParcel)
  {
    scale = paramParcel.readFloat();
    offsetX = paramParcel.readFloat();
    offsetY = paramParcel.readFloat();
  }
  
  public static MagnificationSpec obtain()
  {
    MagnificationSpec localMagnificationSpec = (MagnificationSpec)sPool.acquire();
    if (localMagnificationSpec == null) {
      localMagnificationSpec = new MagnificationSpec();
    }
    return localMagnificationSpec;
  }
  
  public static MagnificationSpec obtain(MagnificationSpec paramMagnificationSpec)
  {
    MagnificationSpec localMagnificationSpec = obtain();
    scale = scale;
    offsetX = offsetX;
    offsetY = offsetY;
    return localMagnificationSpec;
  }
  
  public void clear()
  {
    scale = 1.0F;
    offsetX = 0.0F;
    offsetY = 0.0F;
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
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (MagnificationSpec)paramObject;
      if ((scale != scale) || (offsetX != offsetX) || (offsetY != offsetY)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    float f = scale;
    int i = 0;
    int j;
    if (f != 0.0F) {
      j = Float.floatToIntBits(scale);
    } else {
      j = 0;
    }
    int k;
    if (offsetX != 0.0F) {
      k = Float.floatToIntBits(offsetX);
    } else {
      k = 0;
    }
    if (offsetY != 0.0F) {
      i = Float.floatToIntBits(offsetY);
    }
    return 31 * (31 * j + k) + i;
  }
  
  public void initialize(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if ((Build.FEATURES.ENABLE_ONE_HAND_CTRL) || (paramFloat1 >= 1.0F))
    {
      scale = paramFloat1;
      offsetX = paramFloat2;
      offsetY = paramFloat3;
      return;
    }
    throw new IllegalArgumentException("Scale must be greater than or equal to one!");
  }
  
  public boolean isNop()
  {
    boolean bool;
    if ((scale == 1.0F) && (offsetX == 0.0F) && (offsetY == 0.0F)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOneHandControlling()
  {
    boolean bool;
    if (scale < 1.0F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void recycle()
  {
    clear();
    sPool.release(this);
  }
  
  public void setTo(MagnificationSpec paramMagnificationSpec)
  {
    scale = scale;
    offsetX = offsetX;
    offsetY = offsetY;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<scale:");
    localStringBuilder.append(Float.toString(scale));
    localStringBuilder.append(",offsetX:");
    localStringBuilder.append(Float.toString(offsetX));
    localStringBuilder.append(",offsetY:");
    localStringBuilder.append(Float.toString(offsetY));
    localStringBuilder.append(">");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(scale);
    paramParcel.writeFloat(offsetX);
    paramParcel.writeFloat(offsetY);
    recycle();
  }
}
