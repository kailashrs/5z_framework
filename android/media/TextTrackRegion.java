package android.media;

class TextTrackRegion
{
  static final int SCROLL_VALUE_NONE = 300;
  static final int SCROLL_VALUE_SCROLL_UP = 301;
  float mAnchorPointX = 0.0F;
  float mAnchorPointY = 100.0F;
  String mId = "";
  int mLines = 3;
  int mScrollValue = 300;
  float mViewportAnchorPointX = 0.0F;
  float mViewportAnchorPointY = 100.0F;
  float mWidth = 100.0F;
  
  TextTrackRegion() {}
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(" {id:\"");
    localStringBuilder.append(mId);
    localStringBuilder.append("\", width:");
    localStringBuilder.append(mWidth);
    localStringBuilder.append(", lines:");
    localStringBuilder.append(mLines);
    localStringBuilder.append(", anchorPoint:(");
    localStringBuilder.append(mAnchorPointX);
    localStringBuilder.append(", ");
    localStringBuilder.append(mAnchorPointY);
    localStringBuilder.append("), viewportAnchorPoints:");
    localStringBuilder.append(mViewportAnchorPointX);
    localStringBuilder.append(", ");
    localStringBuilder.append(mViewportAnchorPointY);
    localStringBuilder.append("), scrollValue:");
    String str;
    if (mScrollValue == 300) {
      str = "none";
    } else if (mScrollValue == 301) {
      str = "scroll_up";
    } else {
      str = "INVALID";
    }
    localStringBuilder.append(str);
    return "}";
  }
}
