package android.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;

public class Point
  implements Parcelable
{
  public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator()
  {
    public Point createFromParcel(Parcel paramAnonymousParcel)
    {
      Point localPoint = new Point();
      localPoint.readFromParcel(paramAnonymousParcel);
      return localPoint;
    }
    
    public Point[] newArray(int paramAnonymousInt)
    {
      return new Point[paramAnonymousInt];
    }
  };
  public int x;
  public int y;
  
  public Point() {}
  
  public Point(int paramInt1, int paramInt2)
  {
    x = paramInt1;
    y = paramInt2;
  }
  
  public Point(Point paramPoint)
  {
    x = x;
    y = y;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public final boolean equals(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((x == paramInt1) && (y == paramInt2)) {
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
      paramObject = (Point)paramObject;
      if (x != x) {
        return false;
      }
      return y == y;
    }
    return false;
  }
  
  public int hashCode()
  {
    return 31 * x + y;
  }
  
  public final void negate()
  {
    x = (-x);
    y = (-y);
  }
  
  public final void offset(int paramInt1, int paramInt2)
  {
    x += paramInt1;
    y += paramInt2;
  }
  
  public void printShortString(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print("[");
    paramPrintWriter.print(x);
    paramPrintWriter.print(",");
    paramPrintWriter.print(y);
    paramPrintWriter.print("]");
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    x = paramParcel.readInt();
    y = paramParcel.readInt();
  }
  
  public void set(int paramInt1, int paramInt2)
  {
    x = paramInt1;
    y = paramInt2;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Point(");
    localStringBuilder.append(x);
    localStringBuilder.append(", ");
    localStringBuilder.append(y);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(x);
    paramParcel.writeInt(y);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1120986464257L, x);
    paramProtoOutputStream.write(1120986464258L, y);
    paramProtoOutputStream.end(paramLong);
  }
}
