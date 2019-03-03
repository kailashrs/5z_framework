package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Rect
  implements Parcelable
{
  public static final Parcelable.Creator<Rect> CREATOR = new Parcelable.Creator()
  {
    public Rect createFromParcel(Parcel paramAnonymousParcel)
    {
      Rect localRect = new Rect();
      localRect.readFromParcel(paramAnonymousParcel);
      return localRect;
    }
    
    public Rect[] newArray(int paramAnonymousInt)
    {
      return new Rect[paramAnonymousInt];
    }
  };
  public int bottom;
  public int left;
  public int right;
  public int top;
  
  public Rect() {}
  
  public Rect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    left = paramInt1;
    top = paramInt2;
    right = paramInt3;
    bottom = paramInt4;
  }
  
  public Rect(Rect paramRect)
  {
    if (paramRect == null)
    {
      bottom = 0;
      right = 0;
      top = 0;
      left = 0;
    }
    else
    {
      left = left;
      top = top;
      right = right;
      bottom = bottom;
    }
  }
  
  public static Rect copyOrNull(Rect paramRect)
  {
    if (paramRect == null) {
      paramRect = null;
    } else {
      paramRect = new Rect(paramRect);
    }
    return paramRect;
  }
  
  public static boolean intersects(Rect paramRect1, Rect paramRect2)
  {
    boolean bool;
    if ((left < right) && (left < right) && (top < bottom) && (top < bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static Rect unflattenFromString(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    paramString = UnflattenHelper.getMatcher(paramString);
    if (!paramString.matches()) {
      return null;
    }
    return new Rect(Integer.parseInt(paramString.group(1)), Integer.parseInt(paramString.group(2)), Integer.parseInt(paramString.group(3)), Integer.parseInt(paramString.group(4)));
  }
  
  public final int centerX()
  {
    return left + right >> 1;
  }
  
  public final int centerY()
  {
    return top + bottom >> 1;
  }
  
  public boolean contains(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((left < right) && (top < bottom) && (paramInt1 >= left) && (paramInt1 < right) && (paramInt2 >= top) && (paramInt2 < bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool;
    if ((left < right) && (top < bottom) && (left <= paramInt1) && (top <= paramInt2) && (right >= paramInt3) && (bottom >= paramInt4)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean contains(Rect paramRect)
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
      paramObject = (Rect)paramObject;
      if ((left != left) || (top != top) || (right != right) || (bottom != bottom)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public final float exactCenterX()
  {
    return (left + right) * 0.5F;
  }
  
  public final float exactCenterY()
  {
    return (top + bottom) * 0.5F;
  }
  
  public String flattenToString()
  {
    StringBuilder localStringBuilder = new StringBuilder(32);
    localStringBuilder.append(left);
    localStringBuilder.append(' ');
    localStringBuilder.append(top);
    localStringBuilder.append(' ');
    localStringBuilder.append(right);
    localStringBuilder.append(' ');
    localStringBuilder.append(bottom);
    return localStringBuilder.toString();
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * left + top) + right) + bottom;
  }
  
  public final int height()
  {
    return bottom - top;
  }
  
  public void inset(int paramInt1, int paramInt2)
  {
    left += paramInt1;
    top += paramInt2;
    right -= paramInt1;
    bottom -= paramInt2;
  }
  
  public void inset(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    left += paramInt1;
    top += paramInt2;
    right -= paramInt3;
    bottom -= paramInt4;
  }
  
  public void inset(Rect paramRect)
  {
    left += left;
    top += top;
    right -= right;
    bottom -= bottom;
  }
  
  public boolean intersect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((left < paramInt3) && (paramInt1 < right) && (top < paramInt4) && (paramInt2 < bottom))
    {
      if (left < paramInt1) {
        left = paramInt1;
      }
      if (top < paramInt2) {
        top = paramInt2;
      }
      if (right > paramInt3) {
        right = paramInt3;
      }
      if (bottom > paramInt4) {
        bottom = paramInt4;
      }
      return true;
    }
    return false;
  }
  
  public boolean intersect(Rect paramRect)
  {
    return intersect(left, top, right, bottom);
  }
  
  public void intersectUnchecked(Rect paramRect)
  {
    left = Math.max(left, left);
    top = Math.max(top, top);
    right = Math.min(right, right);
    bottom = Math.min(bottom, bottom);
  }
  
  public boolean intersects(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool;
    if ((left < paramInt3) && (paramInt1 < right) && (top < paramInt4) && (paramInt2 < bottom)) {
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
  
  public void offset(int paramInt1, int paramInt2)
  {
    left += paramInt1;
    top += paramInt2;
    right += paramInt1;
    bottom += paramInt2;
  }
  
  public void offsetTo(int paramInt1, int paramInt2)
  {
    right += paramInt1 - left;
    bottom += paramInt2 - top;
    left = paramInt1;
    top = paramInt2;
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
    left = paramParcel.readInt();
    top = paramParcel.readInt();
    right = paramParcel.readInt();
    bottom = paramParcel.readInt();
  }
  
  public void scale(float paramFloat)
  {
    if (paramFloat != 1.0F)
    {
      left = ((int)(left * paramFloat + 0.5F));
      top = ((int)(top * paramFloat + 0.5F));
      right = ((int)(right * paramFloat + 0.5F));
      bottom = ((int)(bottom * paramFloat + 0.5F));
    }
  }
  
  public void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    left = paramInt1;
    top = paramInt2;
    right = paramInt3;
    bottom = paramInt4;
  }
  
  public void set(Rect paramRect)
  {
    left = left;
    top = top;
    right = right;
    bottom = bottom;
  }
  
  public void setEmpty()
  {
    bottom = 0;
    top = 0;
    right = 0;
    left = 0;
  }
  
  public boolean setIntersect(Rect paramRect1, Rect paramRect2)
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
    int i;
    if (left > right)
    {
      i = left;
      left = right;
      right = i;
    }
    if (top > bottom)
    {
      i = top;
      top = bottom;
      bottom = i;
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
    StringBuilder localStringBuilder = new StringBuilder(32);
    localStringBuilder.append("Rect(");
    localStringBuilder.append(left);
    localStringBuilder.append(", ");
    localStringBuilder.append(top);
    localStringBuilder.append(" - ");
    localStringBuilder.append(right);
    localStringBuilder.append(", ");
    localStringBuilder.append(bottom);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void union(int paramInt1, int paramInt2)
  {
    if (paramInt1 < left) {
      left = paramInt1;
    } else if (paramInt1 > right) {
      right = paramInt1;
    }
    if (paramInt2 < top) {
      top = paramInt2;
    } else if (paramInt2 > bottom) {
      bottom = paramInt2;
    }
  }
  
  public void union(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt1 < paramInt3) && (paramInt2 < paramInt4)) {
      if ((left < right) && (top < bottom))
      {
        if (left > paramInt1) {
          left = paramInt1;
        }
        if (top > paramInt2) {
          top = paramInt2;
        }
        if (right < paramInt3) {
          right = paramInt3;
        }
        if (bottom < paramInt4) {
          bottom = paramInt4;
        }
      }
      else
      {
        left = paramInt1;
        top = paramInt2;
        right = paramInt3;
        bottom = paramInt4;
      }
    }
  }
  
  public void union(Rect paramRect)
  {
    union(left, top, right, bottom);
  }
  
  public final int width()
  {
    return right - left;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(left);
    paramParcel.writeInt(top);
    paramParcel.writeInt(right);
    paramParcel.writeInt(bottom);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1120986464257L, left);
    paramProtoOutputStream.write(1120986464258L, top);
    paramProtoOutputStream.write(1120986464259L, right);
    paramProtoOutputStream.write(1120986464260L, bottom);
    paramProtoOutputStream.end(paramLong);
  }
  
  private static final class UnflattenHelper
  {
    private static final Pattern FLATTENED_PATTERN = Pattern.compile("(-?\\d+) (-?\\d+) (-?\\d+) (-?\\d+)");
    
    private UnflattenHelper() {}
    
    static Matcher getMatcher(String paramString)
    {
      return FLATTENED_PATTERN.matcher(paramString);
    }
  }
}
