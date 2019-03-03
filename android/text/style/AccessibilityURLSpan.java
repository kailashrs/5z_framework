package android.text.style;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

public class AccessibilityURLSpan
  extends URLSpan
  implements Parcelable
{
  final AccessibilityClickableSpan mAccessibilityClickableSpan;
  
  public AccessibilityURLSpan(Parcel paramParcel)
  {
    super(paramParcel);
    mAccessibilityClickableSpan = new AccessibilityClickableSpan(paramParcel);
  }
  
  public AccessibilityURLSpan(URLSpan paramURLSpan)
  {
    super(paramURLSpan.getURL());
    mAccessibilityClickableSpan = new AccessibilityClickableSpan(paramURLSpan.getId());
  }
  
  public void copyConnectionDataFrom(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    mAccessibilityClickableSpan.copyConnectionDataFrom(paramAccessibilityNodeInfo);
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 26;
  }
  
  public void onClick(View paramView)
  {
    mAccessibilityClickableSpan.onClick(paramView);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    super.writeToParcelInternal(paramParcel, paramInt);
    mAccessibilityClickableSpan.writeToParcel(paramParcel, paramInt);
  }
}
