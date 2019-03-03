package android.view;

import android.graphics.Rect;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pools.SynchronizedPool;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.ArrayList;
import java.util.List;

public class WindowInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WindowInfo> CREATOR = new Parcelable.Creator()
  {
    public WindowInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      WindowInfo localWindowInfo = WindowInfo.obtain();
      localWindowInfo.initFromParcel(paramAnonymousParcel);
      return localWindowInfo;
    }
    
    public WindowInfo[] newArray(int paramAnonymousInt)
    {
      return new WindowInfo[paramAnonymousInt];
    }
  };
  private static final int MAX_POOL_SIZE = 10;
  private static final Pools.SynchronizedPool<WindowInfo> sPool = new Pools.SynchronizedPool(10);
  public long accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
  public IBinder activityToken;
  public final Rect boundsInScreen = new Rect();
  public List<IBinder> childTokens;
  public boolean focused;
  public boolean inPictureInPicture;
  public int layer;
  public IBinder parentToken;
  public CharSequence title;
  public IBinder token;
  public int type;
  
  private WindowInfo() {}
  
  private void clear()
  {
    type = 0;
    layer = 0;
    token = null;
    parentToken = null;
    activityToken = null;
    focused = false;
    boundsInScreen.setEmpty();
    if (childTokens != null) {
      childTokens.clear();
    }
    inPictureInPicture = false;
  }
  
  private void initFromParcel(Parcel paramParcel)
  {
    type = paramParcel.readInt();
    layer = paramParcel.readInt();
    token = paramParcel.readStrongBinder();
    parentToken = paramParcel.readStrongBinder();
    activityToken = paramParcel.readStrongBinder();
    int i = paramParcel.readInt();
    int j = 0;
    boolean bool;
    if (i == 1) {
      bool = true;
    } else {
      bool = false;
    }
    focused = bool;
    boundsInScreen.readFromParcel(paramParcel);
    title = paramParcel.readCharSequence();
    accessibilityIdOfAnchor = paramParcel.readLong();
    if (paramParcel.readInt() == 1) {
      bool = true;
    } else {
      bool = false;
    }
    inPictureInPicture = bool;
    if (paramParcel.readInt() == 1) {
      j = 1;
    }
    if (j != 0)
    {
      if (childTokens == null) {
        childTokens = new ArrayList();
      }
      paramParcel.readBinderList(childTokens);
    }
  }
  
  public static WindowInfo obtain()
  {
    WindowInfo localWindowInfo1 = (WindowInfo)sPool.acquire();
    WindowInfo localWindowInfo2 = localWindowInfo1;
    if (localWindowInfo1 == null) {
      localWindowInfo2 = new WindowInfo();
    }
    return localWindowInfo2;
  }
  
  public static WindowInfo obtain(WindowInfo paramWindowInfo)
  {
    WindowInfo localWindowInfo = obtain();
    type = type;
    layer = layer;
    token = token;
    parentToken = parentToken;
    activityToken = activityToken;
    focused = focused;
    boundsInScreen.set(boundsInScreen);
    title = title;
    accessibilityIdOfAnchor = accessibilityIdOfAnchor;
    inPictureInPicture = inPictureInPicture;
    if ((childTokens != null) && (!childTokens.isEmpty())) {
      if (childTokens == null) {
        childTokens = new ArrayList(childTokens);
      } else {
        childTokens.addAll(childTokens);
      }
    }
    return localWindowInfo;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void recycle()
  {
    clear();
    sPool.release(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WindowInfo[");
    localStringBuilder.append("title=");
    localStringBuilder.append(title);
    localStringBuilder.append(", type=");
    localStringBuilder.append(type);
    localStringBuilder.append(", layer=");
    localStringBuilder.append(layer);
    localStringBuilder.append(", token=");
    localStringBuilder.append(token);
    localStringBuilder.append(", bounds=");
    localStringBuilder.append(boundsInScreen);
    localStringBuilder.append(", parent=");
    localStringBuilder.append(parentToken);
    localStringBuilder.append(", focused=");
    localStringBuilder.append(focused);
    localStringBuilder.append(", children=");
    localStringBuilder.append(childTokens);
    localStringBuilder.append(", accessibility anchor=");
    localStringBuilder.append(accessibilityIdOfAnchor);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(type);
    paramParcel.writeInt(layer);
    paramParcel.writeStrongBinder(token);
    paramParcel.writeStrongBinder(parentToken);
    paramParcel.writeStrongBinder(activityToken);
    paramParcel.writeInt(focused);
    boundsInScreen.writeToParcel(paramParcel, paramInt);
    paramParcel.writeCharSequence(title);
    paramParcel.writeLong(accessibilityIdOfAnchor);
    paramParcel.writeInt(inPictureInPicture);
    if ((childTokens != null) && (!childTokens.isEmpty()))
    {
      paramParcel.writeInt(1);
      paramParcel.writeBinderList(childTokens);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
