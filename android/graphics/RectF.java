package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.FastMath;
import java.io.PrintWriter;

public class RectF
  implements Parcelable
{
  public static final Parcelable.Creator<RectF> CREATOR = new Parcelable.Creator()
  {
    public RectF createFromParcel(Parcel paramAnonymousParcel)
    {
      RectF localRectF = new RectF();
      localRectF.readFromParcel(paramAnonymousParcel);
      return localRectF;
    }
    
    public RectF[] newArray(int paramAnonymousInt)
    {
      return new RectF[paramAnonymousInt];
    }
  };
  public float bottom;
  public float left;
  public float right;
  public float top;
  
  public RectF() {}
  
  public RectF(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    left = paramFloat1;
    top = paramFloat2;
    right = paramFloat3;
    bottom = paramFloat4;
  }
  
  public RectF(Rect paramRect)
  {
    if (paramRect == null)
    {
      bottom = 0.0F;
      right = 0.0F;
      top = 0.0F;
      left = 0.0F;
    }
    else
    {
      left = left;
      top = top;
      right = right;
      bottom = bottom;
    }
  }
  
  public RectF(RectF paramRectF)
  {
    if (paramRectF == null)
    {
      bottom = 0.0F;
      right = 0.0F;
      top = 0.0F;
      left = 0.0F;
    }
    else
    {
      left = left;
      top = top;
      right = right;
      bottom = bottom;
    }
  }
  
  public static boolean intersects(RectF paramRectF1, RectF paramRectF2)
  {
    boolean bool;
    if ((left < right) && (left < right) && (top < bottom) && (top < bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final float centerX()
  {
    return (left + right) * 0.5F;
  }
  
  public final float centerY()
  {
    return (top + bottom) * 0.5F;
  }
  
  public boolean contains(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if ((left < right) && (top < bottom) && (paramFloat1 >= left) && (paramFloat1 < right) && (paramFloat2 >= top) && (paramFloat2 < bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean contains(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    boolean bool;
    if ((left < right) && (top < bottom) && (left <= paramFloat1) && (top <= paramFloat2) && (right >= paramFloat3) && (bottom >= paramFloat4)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean contains(RectF paramRectF)
  {
    boolean bool;
    if ((left < right) && (top < bottom) && (left <= left) && (top <= top) && (right >= right) && (bottom >= bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
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
      paramObject = (RectF)paramObject;
      if ((left != left) || (top != top) || (right != right) || (bottom != bottom)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    float f = left;
    int i = 0;
    int j;
    if (f != 0.0F) {
      j = Float.floatToIntBits(left);
    } else {
      j = 0;
    }
    int k;
    if (top != 0.0F) {
      k = Float.floatToIntBits(top);
    } else {
      k = 0;
    }
    int m;
    if (right != 0.0F) {
      m = Float.floatToIntBits(right);
    } else {
      m = 0;
    }
    if (bottom != 0.0F) {
      i = Float.floatToIntBits(bottom);
    }
    return 31 * (31 * (31 * j + k) + m) + i;
  }
  
  public final float height()
  {
    return bottom - top;
  }
  
  public void inset(float paramFloat1, float paramFloat2)
  {
    left += paramFloat1;
    top += paramFloat2;
    right -= paramFloat1;
    bottom -= paramFloat2;
  }
  
  public boolean intersect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if ((left < paramFloat3) && (paramFloat1 < right) && (top < paramFloat4) && (paramFloat2 < bottom))
    {
      if (left < paramFloat1) {
        left = paramFloat1;
      }
      if (top < paramFloat2) {
        top = paramFloat2;
      }
      if (right > paramFloat3) {
        right = paramFloat3;
      }
      if (bottom > paramFloat4) {
        bottom = paramFloat4;
      }
      return true;
    }
    return false;
  }
  
  public boolean intersect(RectF paramRectF)
  {
    return intersect(left, top, right, bottom);
  }
  
  public boolean intersects(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    boolean bool;
    if ((left < paramFloat3) && (paramFloat1 < right) && (top < paramFloat4) && (paramFloat2 < bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isEmpty()
  {
    boolean bool;
    if ((left < right) && (top < bottom)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void offset(float paramFloat1, float paramFloat2)
  {
    left += paramFloat1;
    top += paramFloat2;
    right += paramFloat1;
    bottom += paramFloat2;
  }
  
  public void offsetTo(float paramFloat1, float paramFloat2)
  {
    right += paramFloat1 - left;
    bottom += paramFloat2 - top;
    left = paramFloat1;
    top = paramFloat2;
  }
  
  public void printShortString(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print('[');
    paramPrintWriter.print(left);
    paramPrintWriter.print(',');
    paramPrintWriter.print(top);
    paramPrintWriter.print("][");
    paramPrintWriter.print(right);
    paramPrintWriter.print(',');
    paramPrintWriter.print(bottom);
    paramPrintWriter.print(']');
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    left = paramParcel.readFloat();
    top = paramParcel.readFloat();
    right = paramParcel.readFloat();
    bottom = paramParcel.readFloat();
  }
  
  public void round(Rect paramRect)
  {
    paramRect.set(FastMath.round(left), FastMath.round(top), FastMath.round(right), FastMath.round(bottom));
  }
  
  public void roundOut(Rect paramRect)
  {
    paramRect.set((int)Math.floor(left), (int)Math.floor(top), (int)Math.ceil(right), (int)Math.ceil(bottom));
  }
  
  public void scale(float paramFloat)
  {
    if (paramFloat != 1.0F)
    {
      left *= paramFloat;
      top *= paramFloat;
      right *= paramFloat;
      bottom *= paramFloat;
    }
  }
  
  public void set(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    left = paramFloat1;
    top = paramFloat2;
    right = paramFloat3;
    bottom = paramFloat4;
  }
  
  public void set(Rect paramRect)
  {
    left = left;
    top = top;
    right = right;
    bottom = bottom;
  }
  
  public void set(RectF paramRectF)
  {
    left = left;
    top = top;
    right = right;
    bottom = bottom;
  }
  
  public void setEmpty()
  {
    bottom = 0.0F;
    top = 0.0F;
    right = 0.0F;
    left = 0.0F;
  }
  
  public boolean setIntersect(RectF paramRectF1, RectF paramRectF2)
  {
    if ((left < right) && (left < right) && (top < bottom) && (top < bottom))
    {
      left = Math.max(left, left);
      top = Math.max(top, top);
      right = Math.min(right, right);
      bottom = Math.min(bottom, bottom);
      return true;
    }
    return false;
  }
  
  public void sort()
  {
    float f;
    if (left > right)
    {
      f = left;
      left = right;
      right = f;
    }
    if (top > bottom)
    {
      f = top;
      top = bottom;
      bottom = f;
    }
  }
  
  public String toShortString()
  {
    return toShortString(new StringBuilder(32));
  }
  
  public String toShortString(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.setLength(0);
    paramStringBuilder.append('[');
    paramStringBuilder.append(left);
    paramStringBuilder.append(',');
    paramStringBuilder.append(top);
    paramStringBuilder.append("][");
    paramStringBuilder.append(right);
    paramStringBuilder.append(',');
    paramStringBuilder.append(bottom);
    paramStringBuilder.append(']');
    return paramStringBuilder.toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RectF(");
    localStringBuilder.append(left);
    localStringBuilder.append(", ");
    localStringBuilder.append(top);
    localStringBuilder.append(", ");
    localStringBuilder.append(right);
    localStringBuilder.append(", ");
    localStringBuilder.append(bottom);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void union(float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 < left) {
      left = paramFloat1;
    } else if (paramFloat1 > right) {
      right = paramFloat1;
    }
    if (paramFloat2 < top) {
      top = paramFloat2;
    } else if (paramFloat2 > bottom) {
      bottom = paramFloat2;
    }
  }
  
  public void union(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if ((paramFloat1 < paramFloat3) && (paramFloat2 < paramFloat4)) {
      if ((left < right) && (top < bottom))
      {
        if (left > paramFloat1) {
          left = paramFloat1;
        }
        if (top > paramFloat2) {
          top = paramFloat2;
        }
        if (right < paramFloat3) {
          right = paramFloat3;
        }
        if (bottom < paramFloat4) {
          bottom = paramFloat4;
        }
      }
      else
      {
        left = paramFloat1;
        top = paramFloat2;
        right = paramFloat3;
        bottom = paramFloat4;
      }
    }
  }
  
  public void union(RectF paramRectF)
  {
    union(left, top, right, bottom);
  }
  
  public final float width()
  {
    return right - left;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(left);
    paramParcel.writeFloat(top);
    paramParcel.writeFloat(right);
    paramParcel.writeFloat(bottom);
  }
}
