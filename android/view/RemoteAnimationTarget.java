package android.view;

import android.app.WindowConfiguration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RemoteAnimationTarget
  implements Parcelable
{
  public static final Parcelable.Creator<RemoteAnimationTarget> CREATOR = new Parcelable.Creator()
  {
    public RemoteAnimationTarget createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RemoteAnimationTarget(paramAnonymousParcel);
    }
    
    public RemoteAnimationTarget[] newArray(int paramAnonymousInt)
    {
      return new RemoteAnimationTarget[paramAnonymousInt];
    }
  };
  public static final int MODE_CLOSING = 1;
  public static final int MODE_OPENING = 0;
  public final Rect clipRect;
  public final Rect contentInsets;
  public boolean isNotInRecents;
  public final boolean isTranslucent;
  public final SurfaceControl leash;
  public final int mode;
  public final Point position;
  public final int prefixOrderIndex;
  public final Rect sourceContainerBounds;
  public final int taskId;
  public final WindowConfiguration windowConfiguration;
  
  public RemoteAnimationTarget(int paramInt1, int paramInt2, SurfaceControl paramSurfaceControl, boolean paramBoolean1, Rect paramRect1, Rect paramRect2, int paramInt3, Point paramPoint, Rect paramRect3, WindowConfiguration paramWindowConfiguration, boolean paramBoolean2)
  {
    mode = paramInt2;
    taskId = paramInt1;
    leash = paramSurfaceControl;
    isTranslucent = paramBoolean1;
    clipRect = new Rect(paramRect1);
    contentInsets = new Rect(paramRect2);
    prefixOrderIndex = paramInt3;
    position = new Point(paramPoint);
    sourceContainerBounds = new Rect(paramRect3);
    windowConfiguration = paramWindowConfiguration;
    isNotInRecents = paramBoolean2;
  }
  
  public RemoteAnimationTarget(Parcel paramParcel)
  {
    taskId = paramParcel.readInt();
    mode = paramParcel.readInt();
    leash = ((SurfaceControl)paramParcel.readParcelable(null));
    isTranslucent = paramParcel.readBoolean();
    clipRect = ((Rect)paramParcel.readParcelable(null));
    contentInsets = ((Rect)paramParcel.readParcelable(null));
    prefixOrderIndex = paramParcel.readInt();
    position = ((Point)paramParcel.readParcelable(null));
    sourceContainerBounds = ((Rect)paramParcel.readParcelable(null));
    windowConfiguration = ((WindowConfiguration)paramParcel.readParcelable(null));
    isNotInRecents = paramParcel.readBoolean();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mode=");
    paramPrintWriter.print(mode);
    paramPrintWriter.print(" taskId=");
    paramPrintWriter.print(taskId);
    paramPrintWriter.print(" isTranslucent=");
    paramPrintWriter.print(isTranslucent);
    paramPrintWriter.print(" clipRect=");
    clipRect.printShortString(paramPrintWriter);
    paramPrintWriter.print(" contentInsets=");
    contentInsets.printShortString(paramPrintWriter);
    paramPrintWriter.print(" prefixOrderIndex=");
    paramPrintWriter.print(prefixOrderIndex);
    paramPrintWriter.print(" position=");
    position.printShortString(paramPrintWriter);
    paramPrintWriter.print(" sourceContainerBounds=");
    sourceContainerBounds.printShortString(paramPrintWriter);
    paramPrintWriter.println();
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("windowConfiguration=");
    paramPrintWriter.println(windowConfiguration);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("leash=");
    paramPrintWriter.println(leash);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(taskId);
    paramParcel.writeInt(mode);
    paramParcel.writeParcelable(leash, 0);
    paramParcel.writeBoolean(isTranslucent);
    paramParcel.writeParcelable(clipRect, 0);
    paramParcel.writeParcelable(contentInsets, 0);
    paramParcel.writeInt(prefixOrderIndex);
    paramParcel.writeParcelable(position, 0);
    paramParcel.writeParcelable(sourceContainerBounds, 0);
    paramParcel.writeParcelable(windowConfiguration, 0);
    paramParcel.writeBoolean(isNotInRecents);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1120986464257L, taskId);
    paramProtoOutputStream.write(1120986464258L, mode);
    leash.writeToProto(paramProtoOutputStream, 1146756268035L);
    paramProtoOutputStream.write(1133871366148L, isTranslucent);
    clipRect.writeToProto(paramProtoOutputStream, 1146756268037L);
    contentInsets.writeToProto(paramProtoOutputStream, 1146756268038L);
    paramProtoOutputStream.write(1120986464263L, prefixOrderIndex);
    position.writeToProto(paramProtoOutputStream, 1146756268040L);
    sourceContainerBounds.writeToProto(paramProtoOutputStream, 1146756268041L);
    windowConfiguration.writeToProto(paramProtoOutputStream, 1146756268042L);
    paramProtoOutputStream.end(paramLong);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Mode {}
}
