package android.widget;

class RtlSpacingHelper
{
  public static final int UNDEFINED = Integer.MIN_VALUE;
  private int mEnd = Integer.MIN_VALUE;
  private int mExplicitLeft = 0;
  private int mExplicitRight = 0;
  private boolean mIsRelative = false;
  private boolean mIsRtl = false;
  private int mLeft = 0;
  private int mRight = 0;
  private int mStart = Integer.MIN_VALUE;
  
  RtlSpacingHelper() {}
  
  public int getEnd()
  {
    int i;
    if (mIsRtl) {
      i = mLeft;
    } else {
      i = mRight;
    }
    return i;
  }
  
  public int getLeft()
  {
    return mLeft;
  }
  
  public int getRight()
  {
    return mRight;
  }
  
  public int getStart()
  {
    int i;
    if (mIsRtl) {
      i = mRight;
    } else {
      i = mLeft;
    }
    return i;
  }
  
  public void setAbsolute(int paramInt1, int paramInt2)
  {
    mIsRelative = false;
    if (paramInt1 != Integer.MIN_VALUE)
    {
      mExplicitLeft = paramInt1;
      mLeft = paramInt1;
    }
    if (paramInt2 != Integer.MIN_VALUE)
    {
      mExplicitRight = paramInt2;
      mRight = paramInt2;
    }
  }
  
  public void setDirection(boolean paramBoolean)
  {
    if (paramBoolean == mIsRtl) {
      return;
    }
    mIsRtl = paramBoolean;
    if (mIsRelative)
    {
      int i;
      if (paramBoolean)
      {
        if (mEnd != Integer.MIN_VALUE) {
          i = mEnd;
        } else {
          i = mExplicitLeft;
        }
        mLeft = i;
        if (mStart != Integer.MIN_VALUE) {
          i = mStart;
        } else {
          i = mExplicitRight;
        }
        mRight = i;
      }
      else
      {
        if (mStart != Integer.MIN_VALUE) {
          i = mStart;
        } else {
          i = mExplicitLeft;
        }
        mLeft = i;
        if (mEnd != Integer.MIN_VALUE) {
          i = mEnd;
        } else {
          i = mExplicitRight;
        }
        mRight = i;
      }
    }
    else
    {
      mLeft = mExplicitLeft;
      mRight = mExplicitRight;
    }
  }
  
  public void setRelative(int paramInt1, int paramInt2)
  {
    mStart = paramInt1;
    mEnd = paramInt2;
    mIsRelative = true;
    if (mIsRtl)
    {
      if (paramInt2 != Integer.MIN_VALUE) {
        mLeft = paramInt2;
      }
      if (paramInt1 != Integer.MIN_VALUE) {
        mRight = paramInt1;
      }
    }
    else
    {
      if (paramInt1 != Integer.MIN_VALUE) {
        mLeft = paramInt1;
      }
      if (paramInt2 != Integer.MIN_VALUE) {
        mRight = paramInt2;
      }
    }
  }
}
