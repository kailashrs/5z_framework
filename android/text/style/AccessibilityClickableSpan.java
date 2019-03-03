package android.text.style;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.ParcelableSpan;
import android.text.Spanned;
import android.view.View;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;

public class AccessibilityClickableSpan
  extends ClickableSpan
  implements ParcelableSpan
{
  public static final Parcelable.Creator<AccessibilityClickableSpan> CREATOR = new Parcelable.Creator()
  {
    public AccessibilityClickableSpan createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AccessibilityClickableSpan(paramAnonymousParcel);
    }
    
    public AccessibilityClickableSpan[] newArray(int paramAnonymousInt)
    {
      return new AccessibilityClickableSpan[paramAnonymousInt];
    }
  };
  private int mConnectionId = -1;
  private final int mOriginalClickableSpanId;
  private long mSourceNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
  private int mWindowId = -1;
  
  public AccessibilityClickableSpan(int paramInt)
  {
    mOriginalClickableSpanId = paramInt;
  }
  
  public AccessibilityClickableSpan(Parcel paramParcel)
  {
    mOriginalClickableSpanId = paramParcel.readInt();
  }
  
  public void copyConnectionDataFrom(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    mConnectionId = paramAccessibilityNodeInfo.getConnectionId();
    mWindowId = paramAccessibilityNodeInfo.getWindowId();
    mSourceNodeId = paramAccessibilityNodeInfo.getSourceNodeId();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ClickableSpan findClickableSpan(CharSequence paramCharSequence)
  {
    if (!(paramCharSequence instanceof Spanned)) {
      return null;
    }
    Spanned localSpanned = (Spanned)paramCharSequence;
    int i = paramCharSequence.length();
    int j = 0;
    paramCharSequence = (ClickableSpan[])localSpanned.getSpans(0, i, ClickableSpan.class);
    while (j < paramCharSequence.length)
    {
      if (paramCharSequence[j].getId() == mOriginalClickableSpanId) {
        return paramCharSequence[j];
      }
      j++;
    }
    return null;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 25;
  }
  
  public void onClick(View paramView)
  {
    paramView = new Bundle();
    paramView.putParcelable("android.view.accessibility.action.ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN", this);
    if ((mWindowId != -1) && (mSourceNodeId != AccessibilityNodeInfo.UNDEFINED_NODE_ID) && (mConnectionId != -1))
    {
      AccessibilityInteractionClient.getInstance().performAccessibilityAction(mConnectionId, mWindowId, mSourceNodeId, 16908676, paramView);
      return;
    }
    throw new RuntimeException("ClickableSpan for accessibility service not properly initialized");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mOriginalClickableSpanId);
  }
}
