package android.view;

import android.graphics.Rect;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public final class WindowInsets
{
  public static final WindowInsets CONSUMED = new WindowInsets(null, null, null, false, false, null);
  private static final Rect EMPTY_RECT = new Rect(0, 0, 0, 0);
  private boolean mAlwaysConsumeNavBar;
  private DisplayCutout mDisplayCutout;
  private boolean mDisplayCutoutConsumed;
  private boolean mIsRound;
  private Rect mStableInsets;
  private boolean mStableInsetsConsumed;
  private Rect mSystemWindowInsets;
  private boolean mSystemWindowInsetsConsumed;
  private Rect mTempRect;
  private Rect mWindowDecorInsets;
  private boolean mWindowDecorInsetsConsumed;
  
  public WindowInsets(Rect paramRect)
  {
    this(paramRect, null, null, false, false, null);
  }
  
  public WindowInsets(Rect paramRect1, Rect paramRect2, Rect paramRect3, boolean paramBoolean1, boolean paramBoolean2, DisplayCutout paramDisplayCutout)
  {
    boolean bool1 = false;
    mSystemWindowInsetsConsumed = false;
    mWindowDecorInsetsConsumed = false;
    mStableInsetsConsumed = false;
    mDisplayCutoutConsumed = false;
    boolean bool2;
    if (paramRect1 == null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mSystemWindowInsetsConsumed = bool2;
    if (mSystemWindowInsetsConsumed) {
      paramRect1 = EMPTY_RECT;
    } else {
      paramRect1 = new Rect(paramRect1);
    }
    mSystemWindowInsets = paramRect1;
    if (paramRect2 == null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mWindowDecorInsetsConsumed = bool2;
    if (mWindowDecorInsetsConsumed) {
      paramRect1 = EMPTY_RECT;
    } else {
      paramRect1 = new Rect(paramRect2);
    }
    mWindowDecorInsets = paramRect1;
    if (paramRect3 == null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mStableInsetsConsumed = bool2;
    if (mStableInsetsConsumed) {
      paramRect1 = EMPTY_RECT;
    } else {
      paramRect1 = new Rect(paramRect3);
    }
    mStableInsets = paramRect1;
    mIsRound = paramBoolean1;
    mAlwaysConsumeNavBar = paramBoolean2;
    paramBoolean1 = bool1;
    if (paramDisplayCutout == null) {
      paramBoolean1 = true;
    }
    mDisplayCutoutConsumed = paramBoolean1;
    if ((!mDisplayCutoutConsumed) && (!paramDisplayCutout.isEmpty())) {
      break label222;
    }
    paramDisplayCutout = null;
    label222:
    mDisplayCutout = paramDisplayCutout;
  }
  
  public WindowInsets(WindowInsets paramWindowInsets)
  {
    mSystemWindowInsetsConsumed = false;
    mWindowDecorInsetsConsumed = false;
    mStableInsetsConsumed = false;
    mDisplayCutoutConsumed = false;
    mSystemWindowInsets = mSystemWindowInsets;
    mWindowDecorInsets = mWindowDecorInsets;
    mStableInsets = mStableInsets;
    mSystemWindowInsetsConsumed = mSystemWindowInsetsConsumed;
    mWindowDecorInsetsConsumed = mWindowDecorInsetsConsumed;
    mStableInsetsConsumed = mStableInsetsConsumed;
    mIsRound = mIsRound;
    mAlwaysConsumeNavBar = mAlwaysConsumeNavBar;
    mDisplayCutout = mDisplayCutout;
    mDisplayCutoutConsumed = mDisplayCutoutConsumed;
  }
  
  private static Rect insetInsets(Rect paramRect, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = Math.max(0, left - paramInt1);
    int j = Math.max(0, top - paramInt2);
    int k = Math.max(0, right - paramInt3);
    int m = Math.max(0, bottom - paramInt4);
    if ((i == paramInt1) && (j == paramInt2) && (k == paramInt3) && (m == paramInt4)) {
      return paramRect;
    }
    return new Rect(i, j, k, m);
  }
  
  public WindowInsets consumeDisplayCutout()
  {
    WindowInsets localWindowInsets = new WindowInsets(this);
    mDisplayCutout = null;
    mDisplayCutoutConsumed = true;
    return localWindowInsets;
  }
  
  public WindowInsets consumeStableInsets()
  {
    WindowInsets localWindowInsets = new WindowInsets(this);
    mStableInsets = EMPTY_RECT;
    mStableInsetsConsumed = true;
    return localWindowInsets;
  }
  
  public WindowInsets consumeSystemWindowInsets()
  {
    WindowInsets localWindowInsets = new WindowInsets(this);
    mSystemWindowInsets = EMPTY_RECT;
    mSystemWindowInsetsConsumed = true;
    return localWindowInsets;
  }
  
  public WindowInsets consumeSystemWindowInsets(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    if ((!paramBoolean1) && (!paramBoolean2) && (!paramBoolean3) && (!paramBoolean4)) {
      return this;
    }
    WindowInsets localWindowInsets = new WindowInsets(this);
    int i = 0;
    int j;
    if (paramBoolean1) {
      j = 0;
    } else {
      j = mSystemWindowInsets.left;
    }
    int k;
    if (paramBoolean2) {
      k = 0;
    } else {
      k = mSystemWindowInsets.top;
    }
    int m;
    if (paramBoolean3) {
      m = 0;
    } else {
      m = mSystemWindowInsets.right;
    }
    if (!paramBoolean4) {
      i = mSystemWindowInsets.bottom;
    }
    mSystemWindowInsets = new Rect(j, k, m, i);
    return localWindowInsets;
  }
  
  public WindowInsets consumeWindowDecorInsets()
  {
    WindowInsets localWindowInsets = new WindowInsets(this);
    mWindowDecorInsets.set(0, 0, 0, 0);
    mWindowDecorInsetsConsumed = true;
    return localWindowInsets;
  }
  
  public WindowInsets consumeWindowDecorInsets(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    if ((!paramBoolean1) && (!paramBoolean2) && (!paramBoolean3) && (!paramBoolean4)) {
      return this;
    }
    WindowInsets localWindowInsets = new WindowInsets(this);
    int i = 0;
    int j;
    if (paramBoolean1) {
      j = 0;
    } else {
      j = mWindowDecorInsets.left;
    }
    int k;
    if (paramBoolean2) {
      k = 0;
    } else {
      k = mWindowDecorInsets.top;
    }
    int m;
    if (paramBoolean3) {
      m = 0;
    } else {
      m = mWindowDecorInsets.right;
    }
    if (!paramBoolean4) {
      i = mWindowDecorInsets.bottom;
    }
    mWindowDecorInsets = new Rect(j, k, m, i);
    return localWindowInsets;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof WindowInsets)))
    {
      paramObject = (WindowInsets)paramObject;
      if ((mIsRound != mIsRound) || (mAlwaysConsumeNavBar != mAlwaysConsumeNavBar) || (mSystemWindowInsetsConsumed != mSystemWindowInsetsConsumed) || (mWindowDecorInsetsConsumed != mWindowDecorInsetsConsumed) || (mStableInsetsConsumed != mStableInsetsConsumed) || (mDisplayCutoutConsumed != mDisplayCutoutConsumed) || (!Objects.equals(mSystemWindowInsets, mSystemWindowInsets)) || (!Objects.equals(mWindowDecorInsets, mWindowDecorInsets)) || (!Objects.equals(mStableInsets, mStableInsets)) || (!Objects.equals(mDisplayCutout, mDisplayCutout))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public DisplayCutout getDisplayCutout()
  {
    return mDisplayCutout;
  }
  
  public int getStableInsetBottom()
  {
    return mStableInsets.bottom;
  }
  
  public int getStableInsetLeft()
  {
    return mStableInsets.left;
  }
  
  public int getStableInsetRight()
  {
    return mStableInsets.right;
  }
  
  public int getStableInsetTop()
  {
    return mStableInsets.top;
  }
  
  public int getSystemWindowInsetBottom()
  {
    return mSystemWindowInsets.bottom;
  }
  
  public int getSystemWindowInsetLeft()
  {
    return mSystemWindowInsets.left;
  }
  
  public int getSystemWindowInsetRight()
  {
    return mSystemWindowInsets.right;
  }
  
  public int getSystemWindowInsetTop()
  {
    return mSystemWindowInsets.top;
  }
  
  public Rect getSystemWindowInsets()
  {
    if (mTempRect == null) {
      mTempRect = new Rect();
    }
    if (mSystemWindowInsets != null) {
      mTempRect.set(mSystemWindowInsets);
    } else {
      mTempRect.setEmpty();
    }
    return mTempRect;
  }
  
  public int getWindowDecorInsetBottom()
  {
    return mWindowDecorInsets.bottom;
  }
  
  public int getWindowDecorInsetLeft()
  {
    return mWindowDecorInsets.left;
  }
  
  public int getWindowDecorInsetRight()
  {
    return mWindowDecorInsets.right;
  }
  
  public int getWindowDecorInsetTop()
  {
    return mWindowDecorInsets.top;
  }
  
  public boolean hasInsets()
  {
    boolean bool;
    if ((!hasSystemWindowInsets()) && (!hasWindowDecorInsets()) && (!hasStableInsets()) && (mDisplayCutout == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasStableInsets()
  {
    boolean bool;
    if ((mStableInsets.top == 0) && (mStableInsets.left == 0) && (mStableInsets.right == 0) && (mStableInsets.bottom == 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasSystemWindowInsets()
  {
    boolean bool;
    if ((mSystemWindowInsets.left == 0) && (mSystemWindowInsets.top == 0) && (mSystemWindowInsets.right == 0) && (mSystemWindowInsets.bottom == 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasWindowDecorInsets()
  {
    boolean bool;
    if ((mWindowDecorInsets.left == 0) && (mWindowDecorInsets.top == 0) && (mWindowDecorInsets.right == 0) && (mWindowDecorInsets.bottom == 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mSystemWindowInsets, mWindowDecorInsets, mStableInsets, Boolean.valueOf(mIsRound), mDisplayCutout, Boolean.valueOf(mAlwaysConsumeNavBar), Boolean.valueOf(mSystemWindowInsetsConsumed), Boolean.valueOf(mWindowDecorInsetsConsumed), Boolean.valueOf(mStableInsetsConsumed), Boolean.valueOf(mDisplayCutoutConsumed) });
  }
  
  public WindowInsets inset(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Preconditions.checkArgumentNonnegative(paramInt1);
    Preconditions.checkArgumentNonnegative(paramInt2);
    Preconditions.checkArgumentNonnegative(paramInt3);
    Preconditions.checkArgumentNonnegative(paramInt4);
    WindowInsets localWindowInsets = new WindowInsets(this);
    if (!mSystemWindowInsetsConsumed) {
      mSystemWindowInsets = insetInsets(mSystemWindowInsets, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    if (!mWindowDecorInsetsConsumed) {
      mWindowDecorInsets = insetInsets(mWindowDecorInsets, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    if (!mStableInsetsConsumed) {
      mStableInsets = insetInsets(mStableInsets, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    if (mDisplayCutout != null)
    {
      mDisplayCutout = mDisplayCutout.inset(paramInt1, paramInt2, paramInt3, paramInt4);
      if (mDisplayCutout.isEmpty()) {
        mDisplayCutout = null;
      }
    }
    return localWindowInsets;
  }
  
  public WindowInsets inset(Rect paramRect)
  {
    return inset(left, top, right, bottom);
  }
  
  public boolean isConsumed()
  {
    boolean bool;
    if ((mSystemWindowInsetsConsumed) && (mWindowDecorInsetsConsumed) && (mStableInsetsConsumed) && (mDisplayCutoutConsumed)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRound()
  {
    return mIsRound;
  }
  
  boolean isSystemWindowInsetsConsumed()
  {
    return mSystemWindowInsetsConsumed;
  }
  
  public WindowInsets replaceSystemWindowInsets(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    WindowInsets localWindowInsets = new WindowInsets(this);
    mSystemWindowInsets = new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
    return localWindowInsets;
  }
  
  public WindowInsets replaceSystemWindowInsets(Rect paramRect)
  {
    WindowInsets localWindowInsets = new WindowInsets(this);
    mSystemWindowInsets = new Rect(paramRect);
    return localWindowInsets;
  }
  
  public WindowInsets replaceWindowDecorInsets(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    WindowInsets localWindowInsets = new WindowInsets(this);
    mWindowDecorInsets = new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
    return localWindowInsets;
  }
  
  public boolean shouldAlwaysConsumeNavBar()
  {
    return mAlwaysConsumeNavBar;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WindowInsets{systemWindowInsets=");
    localStringBuilder.append(mSystemWindowInsets);
    localStringBuilder.append(" windowDecorInsets=");
    localStringBuilder.append(mWindowDecorInsets);
    localStringBuilder.append(" stableInsets=");
    localStringBuilder.append(mStableInsets);
    Object localObject;
    if (mDisplayCutout != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" cutout=");
      ((StringBuilder)localObject).append(mDisplayCutout);
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    if (isRound()) {
      localObject = " round";
    } else {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
