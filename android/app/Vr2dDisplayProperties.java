package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.PrintWriter;

public final class Vr2dDisplayProperties
  implements Parcelable
{
  public static final Parcelable.Creator<Vr2dDisplayProperties> CREATOR = new Parcelable.Creator()
  {
    public Vr2dDisplayProperties createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Vr2dDisplayProperties(paramAnonymousParcel, null);
    }
    
    public Vr2dDisplayProperties[] newArray(int paramAnonymousInt)
    {
      return new Vr2dDisplayProperties[paramAnonymousInt];
    }
  };
  public static final int FLAG_VIRTUAL_DISPLAY_ENABLED = 1;
  private final int mAddedFlags;
  private final int mDpi;
  private final int mHeight;
  private final int mRemovedFlags;
  private final int mWidth;
  
  public Vr2dDisplayProperties(int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramInt1, paramInt2, paramInt3, 0, 0);
  }
  
  private Vr2dDisplayProperties(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mWidth = paramInt1;
    mHeight = paramInt2;
    mDpi = paramInt3;
    mAddedFlags = paramInt4;
    mRemovedFlags = paramInt5;
  }
  
  private Vr2dDisplayProperties(Parcel paramParcel)
  {
    mWidth = paramParcel.readInt();
    mHeight = paramParcel.readInt();
    mDpi = paramParcel.readInt();
    mAddedFlags = paramParcel.readInt();
    mRemovedFlags = paramParcel.readInt();
  }
  
  private static String toReadableFlags(int paramInt)
  {
    Object localObject = "{";
    if ((paramInt & 0x1) == 1)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("{");
      ((StringBuilder)localObject).append("enabled");
      localObject = ((StringBuilder)localObject).toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)localObject);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(toString());
    paramPrintWriter.println(localStringBuilder.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (Vr2dDisplayProperties)paramObject;
      if (getFlags() != paramObject.getFlags()) {
        return false;
      }
      if (getRemovedFlags() != paramObject.getRemovedFlags()) {
        return false;
      }
      if (getWidth() != paramObject.getWidth()) {
        return false;
      }
      if (getHeight() != paramObject.getHeight()) {
        return false;
      }
      if (getDpi() != paramObject.getDpi()) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getDpi()
  {
    return mDpi;
  }
  
  public int getFlags()
  {
    return mAddedFlags;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getRemovedFlags()
  {
    return mRemovedFlags;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public int hashCode()
  {
    return 31 * (31 * getWidth() + getHeight()) + getDpi();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Vr2dDisplayProperties{mWidth=");
    localStringBuilder.append(mWidth);
    localStringBuilder.append(", mHeight=");
    localStringBuilder.append(mHeight);
    localStringBuilder.append(", mDpi=");
    localStringBuilder.append(mDpi);
    localStringBuilder.append(", flags=");
    localStringBuilder.append(toReadableFlags(mAddedFlags));
    localStringBuilder.append(", removed_flags=");
    localStringBuilder.append(toReadableFlags(mRemovedFlags));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mWidth);
    paramParcel.writeInt(mHeight);
    paramParcel.writeInt(mDpi);
    paramParcel.writeInt(mAddedFlags);
    paramParcel.writeInt(mRemovedFlags);
  }
  
  public static class Builder
  {
    private int mAddedFlags = 0;
    private int mDpi = -1;
    private int mHeight = -1;
    private int mRemovedFlags = 0;
    private int mWidth = -1;
    
    public Builder() {}
    
    public Builder addFlags(int paramInt)
    {
      mAddedFlags |= paramInt;
      mRemovedFlags &= paramInt;
      return this;
    }
    
    public Vr2dDisplayProperties build()
    {
      return new Vr2dDisplayProperties(mWidth, mHeight, mDpi, mAddedFlags, mRemovedFlags, null);
    }
    
    public Builder removeFlags(int paramInt)
    {
      mRemovedFlags |= paramInt;
      mAddedFlags &= paramInt;
      return this;
    }
    
    public Builder setDimensions(int paramInt1, int paramInt2, int paramInt3)
    {
      mWidth = paramInt1;
      mHeight = paramInt2;
      mDpi = paramInt3;
      return this;
    }
    
    public Builder setEnabled(boolean paramBoolean)
    {
      if (paramBoolean) {
        addFlags(1);
      } else {
        removeFlags(1);
      }
      return this;
    }
  }
}
