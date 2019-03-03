package android.view;

import android.graphics.GraphicBuffer;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AppTransitionAnimationSpec
  implements Parcelable
{
  public static final Parcelable.Creator<AppTransitionAnimationSpec> CREATOR = new Parcelable.Creator()
  {
    public AppTransitionAnimationSpec createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AppTransitionAnimationSpec(paramAnonymousParcel);
    }
    
    public AppTransitionAnimationSpec[] newArray(int paramAnonymousInt)
    {
      return new AppTransitionAnimationSpec[paramAnonymousInt];
    }
  };
  public final GraphicBuffer buffer;
  public final Rect rect;
  public final int taskId;
  
  public AppTransitionAnimationSpec(int paramInt, GraphicBuffer paramGraphicBuffer, Rect paramRect)
  {
    taskId = paramInt;
    rect = paramRect;
    buffer = paramGraphicBuffer;
  }
  
  public AppTransitionAnimationSpec(Parcel paramParcel)
  {
    taskId = paramParcel.readInt();
    rect = ((Rect)paramParcel.readParcelable(null));
    buffer = ((GraphicBuffer)paramParcel.readParcelable(null));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{taskId: ");
    localStringBuilder.append(taskId);
    localStringBuilder.append(", buffer: ");
    localStringBuilder.append(buffer);
    localStringBuilder.append(", rect: ");
    localStringBuilder.append(rect);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(taskId);
    paramParcel.writeParcelable(rect, 0);
    paramParcel.writeParcelable(buffer, 0);
  }
}
