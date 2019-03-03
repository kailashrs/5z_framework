package android.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.view.IDragAndDropPermissions;
import com.android.internal.view.IDragAndDropPermissions.Stub;

public class DragEvent
  implements Parcelable
{
  public static final int ACTION_DRAG_ENDED = 4;
  public static final int ACTION_DRAG_ENTERED = 5;
  public static final int ACTION_DRAG_EXITED = 6;
  public static final int ACTION_DRAG_LOCATION = 2;
  public static final int ACTION_DRAG_STARTED = 1;
  public static final int ACTION_DROP = 3;
  public static final Parcelable.Creator<DragEvent> CREATOR = new Parcelable.Creator()
  {
    public DragEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      DragEvent localDragEvent = DragEvent.obtain();
      mAction = paramAnonymousParcel.readInt();
      mX = paramAnonymousParcel.readFloat();
      mY = paramAnonymousParcel.readFloat();
      boolean bool;
      if (paramAnonymousParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mDragResult = bool;
      if (paramAnonymousParcel.readInt() != 0) {
        mClipData = ((ClipData)ClipData.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      if (paramAnonymousParcel.readInt() != 0) {
        mClipDescription = ((ClipDescription)ClipDescription.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      if (paramAnonymousParcel.readInt() != 0) {
        mDragAndDropPermissions = IDragAndDropPermissions.Stub.asInterface(paramAnonymousParcel.readStrongBinder());
      }
      return localDragEvent;
    }
    
    public DragEvent[] newArray(int paramAnonymousInt)
    {
      return new DragEvent[paramAnonymousInt];
    }
  };
  private static final int MAX_RECYCLED = 10;
  private static final boolean TRACK_RECYCLED_LOCATION = false;
  private static final Object gRecyclerLock = new Object();
  private static DragEvent gRecyclerTop;
  private static int gRecyclerUsed = 0;
  int mAction;
  ClipData mClipData;
  ClipDescription mClipDescription;
  IDragAndDropPermissions mDragAndDropPermissions;
  boolean mDragResult;
  boolean mEventHandlerWasCalled;
  Object mLocalState;
  private DragEvent mNext;
  private boolean mRecycled;
  private RuntimeException mRecycledLocation;
  float mX;
  float mY;
  
  static
  {
    gRecyclerTop = null;
  }
  
  private DragEvent() {}
  
  private void init(int paramInt, float paramFloat1, float paramFloat2, ClipDescription paramClipDescription, ClipData paramClipData, IDragAndDropPermissions paramIDragAndDropPermissions, Object paramObject, boolean paramBoolean)
  {
    mAction = paramInt;
    mX = paramFloat1;
    mY = paramFloat2;
    mClipDescription = paramClipDescription;
    mClipData = paramClipData;
    mDragAndDropPermissions = paramIDragAndDropPermissions;
    mLocalState = paramObject;
    mDragResult = paramBoolean;
  }
  
  static DragEvent obtain()
  {
    return obtain(0, 0.0F, 0.0F, null, null, null, null, false);
  }
  
  public static DragEvent obtain(int paramInt, float paramFloat1, float paramFloat2, Object paramObject, ClipDescription paramClipDescription, ClipData paramClipData, IDragAndDropPermissions paramIDragAndDropPermissions, boolean paramBoolean)
  {
    synchronized (gRecyclerLock)
    {
      if (gRecyclerTop == null)
      {
        localDragEvent = new android/view/DragEvent;
        localDragEvent.<init>();
        localDragEvent.init(paramInt, paramFloat1, paramFloat2, paramClipDescription, paramClipData, paramIDragAndDropPermissions, paramObject, paramBoolean);
        return localDragEvent;
      }
      DragEvent localDragEvent = gRecyclerTop;
      gRecyclerTop = mNext;
      gRecyclerUsed -= 1;
      mRecycledLocation = null;
      mRecycled = false;
      mNext = null;
      localDragEvent.init(paramInt, paramFloat1, paramFloat2, paramClipDescription, paramClipData, paramIDragAndDropPermissions, paramObject, paramBoolean);
      return localDragEvent;
    }
  }
  
  public static DragEvent obtain(DragEvent paramDragEvent)
  {
    return obtain(mAction, mX, mY, mLocalState, mClipDescription, mClipData, mDragAndDropPermissions, mDragResult);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAction()
  {
    return mAction;
  }
  
  public ClipData getClipData()
  {
    return mClipData;
  }
  
  public ClipDescription getClipDescription()
  {
    return mClipDescription;
  }
  
  public IDragAndDropPermissions getDragAndDropPermissions()
  {
    return mDragAndDropPermissions;
  }
  
  public Object getLocalState()
  {
    return mLocalState;
  }
  
  public boolean getResult()
  {
    return mDragResult;
  }
  
  public float getX()
  {
    return mX;
  }
  
  public float getY()
  {
    return mY;
  }
  
  public final void recycle()
  {
    if (!mRecycled)
    {
      mRecycled = true;
      mClipData = null;
      mClipDescription = null;
      mLocalState = null;
      mEventHandlerWasCalled = false;
      synchronized (gRecyclerLock)
      {
        if (gRecyclerUsed < 10)
        {
          gRecyclerUsed += 1;
          mNext = gRecyclerTop;
          gRecyclerTop = this;
        }
        return;
      }
    }
    ??? = new StringBuilder();
    ((StringBuilder)???).append(toString());
    ((StringBuilder)???).append(" recycled twice!");
    throw new RuntimeException(((StringBuilder)???).toString());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DragEvent{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" action=");
    localStringBuilder.append(mAction);
    localStringBuilder.append(" @ (");
    localStringBuilder.append(mX);
    localStringBuilder.append(", ");
    localStringBuilder.append(mY);
    localStringBuilder.append(") desc=");
    localStringBuilder.append(mClipDescription);
    localStringBuilder.append(" data=");
    localStringBuilder.append(mClipData);
    localStringBuilder.append(" local=");
    localStringBuilder.append(mLocalState);
    localStringBuilder.append(" result=");
    localStringBuilder.append(mDragResult);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAction);
    paramParcel.writeFloat(mX);
    paramParcel.writeFloat(mY);
    paramParcel.writeInt(mDragResult);
    if (mClipData == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mClipData.writeToParcel(paramParcel, paramInt);
    }
    if (mClipDescription == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mClipDescription.writeToParcel(paramParcel, paramInt);
    }
    if (mDragAndDropPermissions == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      paramParcel.writeStrongBinder(mDragAndDropPermissions.asBinder());
    }
  }
}
