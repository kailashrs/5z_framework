package android.hardware.display;

import android.graphics.Rect;
import android.text.TextUtils;

public final class DisplayViewport
{
  public int deviceHeight;
  public int deviceWidth;
  public int displayId;
  public final Rect logicalFrame = new Rect();
  public int orientation;
  public final Rect physicalFrame = new Rect();
  public String uniqueId;
  public boolean valid;
  
  public DisplayViewport() {}
  
  public void copyFrom(DisplayViewport paramDisplayViewport)
  {
    valid = valid;
    displayId = displayId;
    orientation = orientation;
    logicalFrame.set(logicalFrame);
    physicalFrame.set(physicalFrame);
    deviceWidth = deviceWidth;
    deviceHeight = deviceHeight;
    uniqueId = uniqueId;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof DisplayViewport)) {
      return false;
    }
    paramObject = (DisplayViewport)paramObject;
    if ((valid != valid) || (displayId != displayId) || (orientation != orientation) || (!logicalFrame.equals(logicalFrame)) || (!physicalFrame.equals(physicalFrame)) || (deviceWidth != deviceWidth) || (deviceHeight != deviceHeight) || (!TextUtils.equals(uniqueId, uniqueId))) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    int i = 1 + (31 * 1 + valid);
    i += 31 * i + displayId;
    i += 31 * i + orientation;
    i += 31 * i + logicalFrame.hashCode();
    i += 31 * i + physicalFrame.hashCode();
    i += 31 * i + deviceWidth;
    i += 31 * i + deviceHeight;
    return i + (31 * i + uniqueId.hashCode());
  }
  
  public DisplayViewport makeCopy()
  {
    DisplayViewport localDisplayViewport = new DisplayViewport();
    localDisplayViewport.copyFrom(this);
    return localDisplayViewport;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DisplayViewport{valid=");
    localStringBuilder.append(valid);
    localStringBuilder.append(", displayId=");
    localStringBuilder.append(displayId);
    localStringBuilder.append(", uniqueId='");
    localStringBuilder.append(uniqueId);
    localStringBuilder.append("', orientation=");
    localStringBuilder.append(orientation);
    localStringBuilder.append(", logicalFrame=");
    localStringBuilder.append(logicalFrame);
    localStringBuilder.append(", physicalFrame=");
    localStringBuilder.append(physicalFrame);
    localStringBuilder.append(", deviceWidth=");
    localStringBuilder.append(deviceWidth);
    localStringBuilder.append(", deviceHeight=");
    localStringBuilder.append(deviceHeight);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
