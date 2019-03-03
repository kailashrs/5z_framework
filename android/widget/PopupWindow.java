package android.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.IBinder;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.transition.TransitionInflater;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.KeyboardShortcutGroup;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import com.android.internal.R.styleable;
import java.lang.ref.WeakReference;
import java.util.List;

public class PopupWindow
{
  private static final int[] ABOVE_ANCHOR_STATE_SET = { 16842922 };
  private static final int ANIMATION_STYLE_DEFAULT = -1;
  private static final int DEFAULT_ANCHORED_GRAVITY = 8388659;
  public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
  public static final int INPUT_METHOD_NEEDED = 1;
  public static final int INPUT_METHOD_NOT_NEEDED = 2;
  private boolean mAboveAnchor;
  private Drawable mAboveAnchorBackgroundDrawable;
  private boolean mAllowScrollingAnchorParent = true;
  private WeakReference<View> mAnchor;
  private WeakReference<View> mAnchorRoot;
  private int mAnchorXoff;
  private int mAnchorYoff;
  private int mAnchoredGravity;
  private int mAnimationStyle = -1;
  private boolean mAttachedInDecor = true;
  private boolean mAttachedInDecorSet = false;
  private Drawable mBackground;
  private View mBackgroundView;
  private Drawable mBelowAnchorBackgroundDrawable;
  private boolean mClipToScreen;
  private boolean mClippingEnabled = true;
  private View mContentView;
  private Context mContext;
  private PopupDecorView mDecorView;
  private float mElevation;
  private Transition mEnterTransition;
  private Rect mEpicenterBounds;
  private Transition mExitTransition;
  private boolean mFocusable;
  private int mGravity = 0;
  private int mHeight = -2;
  private int mHeightMode;
  private boolean mIgnoreCheekPress = false;
  private int mInputMethodMode = 0;
  private boolean mIsAnchorRootAttached;
  private boolean mIsDropdown;
  private boolean mIsShowing;
  private boolean mIsTransitioningToDismiss;
  private int mLastHeight;
  private int mLastWidth;
  private boolean mLayoutInScreen;
  private boolean mLayoutInsetDecor = false;
  private boolean mNotTouchModal;
  private final View.OnAttachStateChangeListener mOnAnchorDetachedListener = new View.OnAttachStateChangeListener()
  {
    public void onViewAttachedToWindow(View paramAnonymousView)
    {
      PopupWindow.this.alignToAnchor();
    }
    
    public void onViewDetachedFromWindow(View paramAnonymousView) {}
  };
  private final View.OnAttachStateChangeListener mOnAnchorRootDetachedListener = new View.OnAttachStateChangeListener()
  {
    public void onViewAttachedToWindow(View paramAnonymousView) {}
    
    public void onViewDetachedFromWindow(View paramAnonymousView)
    {
      PopupWindow.access$102(PopupWindow.this, false);
    }
  };
  private OnDismissListener mOnDismissListener;
  private final View.OnLayoutChangeListener mOnLayoutChangeListener = new _..Lambda.PopupWindow.8Gc2stI5cSJZbuKX7X4Qr_vU2nI(this);
  private final ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new _..Lambda.PopupWindow.nV1HS3Nc6Ck5JRIbIHe3mkyHWzc(this);
  private boolean mOutsideTouchable = false;
  private boolean mOverlapAnchor;
  private WeakReference<View> mParentRootView;
  private boolean mPopupViewInitialLayoutDirectionInherited;
  private int mSoftInputMode = 1;
  private int mSplitTouchEnabled = -1;
  private final Rect mTempRect = new Rect();
  private final int[] mTmpAppLocation = new int[2];
  private final int[] mTmpDrawingLocation = new int[2];
  private final int[] mTmpScreenLocation = new int[2];
  private View.OnTouchListener mTouchInterceptor;
  private boolean mTouchable = true;
  private int mWidth = -2;
  private int mWidthMode;
  private int mWindowLayoutType = 1000;
  private WindowManager mWindowManager;
  
  public PopupWindow()
  {
    this(null, 0, 0);
  }
  
  public PopupWindow(int paramInt1, int paramInt2)
  {
    this(null, paramInt1, paramInt2);
  }
  
  public PopupWindow(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PopupWindow(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842870);
  }
  
  public PopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public PopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    mContext = paramContext;
    mWindowManager = ((WindowManager)paramContext.getSystemService("window"));
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PopupWindow, paramInt1, paramInt2);
    paramAttributeSet = localTypedArray.getDrawable(0);
    mElevation = localTypedArray.getDimension(3, 0.0F);
    mOverlapAnchor = localTypedArray.getBoolean(2, false);
    if (localTypedArray.hasValueOrEmpty(1))
    {
      paramInt1 = localTypedArray.getResourceId(1, 0);
      if (paramInt1 == 16974590) {
        mAnimationStyle = -1;
      } else {
        mAnimationStyle = paramInt1;
      }
    }
    else
    {
      mAnimationStyle = -1;
    }
    Transition localTransition = getTransition(localTypedArray.getResourceId(4, 0));
    if (localTypedArray.hasValueOrEmpty(5)) {
      paramContext = getTransition(localTypedArray.getResourceId(5, 0));
    } else if (localTransition == null) {
      paramContext = null;
    } else {
      paramContext = localTransition.clone();
    }
    localTypedArray.recycle();
    setEnterTransition(localTransition);
    setExitTransition(paramContext);
    setBackgroundDrawable(paramAttributeSet);
  }
  
  public PopupWindow(View paramView)
  {
    this(paramView, 0, 0);
  }
  
  public PopupWindow(View paramView, int paramInt1, int paramInt2)
  {
    this(paramView, paramInt1, paramInt2, false);
  }
  
  public PopupWindow(View paramView, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramView != null)
    {
      mContext = paramView.getContext();
      mWindowManager = ((WindowManager)mContext.getSystemService("window"));
    }
    setContentView(paramView);
    setWidth(paramInt1);
    setHeight(paramInt2);
    setFocusable(paramBoolean);
  }
  
  private void alignToAnchor()
  {
    View localView;
    if (mAnchor != null) {
      localView = (View)mAnchor.get();
    } else {
      localView = null;
    }
    if ((localView != null) && (localView.isAttachedToWindow()) && (hasDecorView()))
    {
      WindowManager.LayoutParams localLayoutParams = getDecorViewLayoutParams();
      updateAboveAnchor(findDropDownPosition(localView, localLayoutParams, mAnchorXoff, mAnchorYoff, width, height, mAnchoredGravity, false));
      update(x, y, -1, -1, true);
    }
  }
  
  private int computeAnimationResource()
  {
    if (mAnimationStyle == -1)
    {
      if (mIsDropdown)
      {
        int i;
        if (mAboveAnchor) {
          i = 16974578;
        } else {
          i = 16974577;
        }
        return i;
      }
      return 0;
    }
    return mAnimationStyle;
  }
  
  private int computeFlags(int paramInt)
  {
    paramInt &= 0xFF797DE7;
    int i = paramInt;
    if (mIgnoreCheekPress) {
      i = paramInt | 0x8000;
    }
    if (!mFocusable)
    {
      i |= 0x8;
      paramInt = i;
      if (mInputMethodMode == 1) {
        paramInt = i | 0x20000;
      }
    }
    else
    {
      paramInt = i;
      if (mInputMethodMode == 2) {
        paramInt = i | 0x20000;
      }
    }
    i = paramInt;
    if (!mTouchable) {
      i = paramInt | 0x10;
    }
    int j = i;
    if (mOutsideTouchable) {
      j = i | 0x40000;
    }
    if (mClippingEnabled)
    {
      paramInt = j;
      if (!mClipToScreen) {}
    }
    else
    {
      paramInt = j | 0x200;
    }
    i = paramInt;
    if (isSplitTouchEnabled()) {
      i = paramInt | 0x800000;
    }
    paramInt = i;
    if (mLayoutInScreen) {
      paramInt = i | 0x100;
    }
    i = paramInt;
    if (mLayoutInsetDecor) {
      i = paramInt | 0x10000;
    }
    paramInt = i;
    if (mNotTouchModal) {
      paramInt = i | 0x20;
    }
    i = paramInt;
    if (mAttachedInDecor) {
      i = paramInt | 0x40000000;
    }
    return i;
  }
  
  private int computeGravity()
  {
    int i;
    if (mGravity == 0) {
      i = 8388659;
    } else {
      i = mGravity;
    }
    int j = i;
    if (mIsDropdown) {
      if (!mClipToScreen)
      {
        j = i;
        if (!mClippingEnabled) {}
      }
      else
      {
        j = i | 0x10000000;
      }
    }
    return j;
  }
  
  private PopupBackgroundView createBackgroundView(View paramView)
  {
    Object localObject = mContentView.getLayoutParams();
    int i;
    if ((localObject != null) && (height == -2)) {
      i = -2;
    } else {
      i = -1;
    }
    localObject = new PopupBackgroundView(mContext);
    ((PopupBackgroundView)localObject).addView(paramView, new FrameLayout.LayoutParams(-1, i));
    return localObject;
  }
  
  private PopupDecorView createDecorView(View paramView)
  {
    Object localObject = mContentView.getLayoutParams();
    int i;
    if ((localObject != null) && (height == -2)) {
      i = -2;
    } else {
      i = -1;
    }
    localObject = new PopupDecorView(mContext);
    ((PopupDecorView)localObject).addView(paramView, -1, i);
    ((PopupDecorView)localObject).setClipChildren(false);
    ((PopupDecorView)localObject).setClipToPadding(false);
    return localObject;
  }
  
  private void dismissImmediate(View paramView1, ViewGroup paramViewGroup, View paramView2)
  {
    if (paramView1.getParent() != null) {
      mWindowManager.removeViewImmediate(paramView1);
    }
    if (paramViewGroup != null) {
      paramViewGroup.removeView(paramView2);
    }
    mDecorView = null;
    mBackgroundView = null;
    mIsTransitioningToDismiss = false;
  }
  
  private View getAppRootView(View paramView)
  {
    View localView = WindowManagerGlobal.getInstance().getWindowView(paramView.getApplicationWindowToken());
    if (localView != null) {
      return localView;
    }
    return paramView.getRootView();
  }
  
  private Transition getTransition(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 17760256))
    {
      Transition localTransition = TransitionInflater.from(mContext).inflateTransition(paramInt);
      if (localTransition != null)
      {
        if (((localTransition instanceof TransitionSet)) && (((TransitionSet)localTransition).getTransitionCount() == 0)) {
          paramInt = 1;
        } else {
          paramInt = 0;
        }
        if (paramInt == 0) {
          return localTransition;
        }
      }
    }
    return null;
  }
  
  private void invokePopup(WindowManager.LayoutParams paramLayoutParams)
  {
    if (mContext != null) {
      packageName = mContext.getPackageName();
    }
    PopupDecorView localPopupDecorView = mDecorView;
    localPopupDecorView.setFitsSystemWindows(mLayoutInsetDecor);
    setLayoutDirectionFromAnchor();
    mWindowManager.addView(localPopupDecorView, paramLayoutParams);
    if (mEnterTransition != null) {
      localPopupDecorView.requestEnterTransition(mEnterTransition);
    }
  }
  
  private boolean positionInDisplayHorizontal(WindowManager.LayoutParams paramLayoutParams, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
  {
    boolean bool1 = true;
    paramInt2 = paramInt3 - paramInt2;
    x += paramInt2;
    paramInt3 = x + paramInt1;
    if (paramInt3 > paramInt5) {
      x -= paramInt3 - paramInt5;
    }
    boolean bool2 = bool1;
    if (x < paramInt4)
    {
      x = paramInt4;
      paramInt3 = paramInt5 - paramInt4;
      if ((paramBoolean) && (paramInt1 > paramInt3))
      {
        width = paramInt3;
        bool2 = bool1;
      }
      else
      {
        bool2 = false;
      }
    }
    x -= paramInt2;
    return bool2;
  }
  
  private boolean positionInDisplayVertical(WindowManager.LayoutParams paramLayoutParams, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
  {
    boolean bool1 = true;
    paramInt2 = paramInt3 - paramInt2;
    y += paramInt2;
    height = paramInt1;
    paramInt3 = y + paramInt1;
    if (paramInt3 > paramInt5) {
      y -= paramInt3 - paramInt5;
    }
    boolean bool2 = bool1;
    if (y < paramInt4)
    {
      y = paramInt4;
      paramInt3 = paramInt5 - paramInt4;
      if ((paramBoolean) && (paramInt1 > paramInt3))
      {
        height = paramInt3;
        bool2 = bool1;
      }
      else
      {
        bool2 = false;
      }
    }
    y -= paramInt2;
    return bool2;
  }
  
  private void preparePopup(WindowManager.LayoutParams paramLayoutParams)
  {
    if ((mContentView != null) && (mContext != null) && (mWindowManager != null))
    {
      if (accessibilityTitle == null) {
        accessibilityTitle = mContext.getString(17040848);
      }
      if (mDecorView != null) {
        mDecorView.cancelTransitions();
      }
      if (mBackground != null)
      {
        mBackgroundView = createBackgroundView(mContentView);
        mBackgroundView.setBackground(mBackground);
      }
      else
      {
        mBackgroundView = mContentView;
      }
      mDecorView = createDecorView(mBackgroundView);
      PopupDecorView localPopupDecorView = mDecorView;
      boolean bool = true;
      localPopupDecorView.setIsRootNamespace(true);
      mBackgroundView.setElevation(mElevation);
      paramLayoutParams.setSurfaceInsets(mBackgroundView, true, true);
      if (mContentView.getRawLayoutDirection() != 2) {
        bool = false;
      }
      mPopupViewInitialLayoutDirectionInherited = bool;
      return;
    }
    throw new IllegalStateException("You must specify a valid content view by calling setContentView() before attempting to show the popup.");
  }
  
  private void setLayoutDirectionFromAnchor()
  {
    if (mAnchor != null)
    {
      View localView = (View)mAnchor.get();
      if ((localView != null) && (mPopupViewInitialLayoutDirectionInherited)) {
        mDecorView.setLayoutDirection(localView.getLayoutDirection());
      }
    }
  }
  
  private boolean tryFitHorizontal(WindowManager.LayoutParams paramLayoutParams, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean)
  {
    paramInt1 = x + (paramInt5 - paramInt4);
    if ((paramInt1 >= 0) && (paramInt2 <= paramInt7 - paramInt1)) {
      return true;
    }
    return positionInDisplayHorizontal(paramLayoutParams, paramInt2, paramInt4, paramInt5, paramInt6, paramInt7, paramBoolean);
  }
  
  private boolean tryFitVertical(WindowManager.LayoutParams paramLayoutParams, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean)
  {
    int i = y + (paramInt5 - paramInt4);
    if ((i >= 0) && (paramInt2 <= paramInt7 - i)) {
      return true;
    }
    if (paramInt2 <= i - paramInt3 - paramInt6)
    {
      if (mOverlapAnchor) {
        paramInt1 += paramInt3;
      }
      y = (paramInt4 - paramInt2 + paramInt1);
      return true;
    }
    return positionInDisplayVertical(paramLayoutParams, paramInt2, paramInt4, paramInt5, paramInt6, paramInt7, paramBoolean);
  }
  
  private void update(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((isShowing()) && (hasContentView()))
    {
      Object localObject = mAnchor;
      int i = mAnchoredGravity;
      if ((paramBoolean) && ((mAnchorXoff != paramInt1) || (mAnchorYoff != paramInt2))) {
        j = 1;
      } else {
        j = 0;
      }
      if ((localObject != null) && (((WeakReference)localObject).get() == paramView) && ((j == 0) || (mIsDropdown)))
      {
        if (j != 0)
        {
          mAnchorXoff = paramInt1;
          mAnchorYoff = paramInt2;
        }
      }
      else {
        attachToAnchor(paramView, paramInt1, paramInt2, i);
      }
      localObject = getDecorViewLayoutParams();
      int k = gravity;
      int j = width;
      int m = height;
      int n = x;
      int i1 = y;
      if (paramInt3 < 0) {
        paramInt2 = mWidth;
      } else {
        paramInt2 = paramInt3;
      }
      if (paramInt4 < 0) {
        paramInt1 = mHeight;
      } else {
        paramInt1 = paramInt4;
      }
      updateAboveAnchor(findDropDownPosition(paramView, (WindowManager.LayoutParams)localObject, mAnchorXoff, mAnchorYoff, paramInt2, paramInt1, i, mAllowScrollingAnchorParent));
      if ((k == gravity) && (n == x) && (i1 == y) && (j == width) && (m == height)) {
        paramBoolean = false;
      } else {
        paramBoolean = true;
      }
      if (paramInt2 >= 0) {
        paramInt2 = width;
      }
      if (paramInt1 >= 0) {
        paramInt1 = height;
      }
      update(x, y, paramInt2, paramInt1, paramBoolean);
      return;
    }
  }
  
  protected void attachToAnchor(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    detachFromAnchor();
    Object localObject = paramView.getViewTreeObserver();
    if (localObject != null) {
      ((ViewTreeObserver)localObject).addOnScrollChangedListener(mOnScrollChangedListener);
    }
    paramView.addOnAttachStateChangeListener(mOnAnchorDetachedListener);
    localObject = paramView.getRootView();
    ((View)localObject).addOnAttachStateChangeListener(mOnAnchorRootDetachedListener);
    ((View)localObject).addOnLayoutChangeListener(mOnLayoutChangeListener);
    mAnchor = new WeakReference(paramView);
    mAnchorRoot = new WeakReference(localObject);
    mIsAnchorRootAttached = ((View)localObject).isAttachedToWindow();
    mParentRootView = mAnchorRoot;
    mAnchorXoff = paramInt1;
    mAnchorYoff = paramInt2;
    mAnchoredGravity = paramInt3;
  }
  
  protected final WindowManager.LayoutParams createPopupLayoutParams(IBinder paramIBinder)
  {
    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
    gravity = computeGravity();
    flags = computeFlags(flags);
    type = mWindowLayoutType;
    token = paramIBinder;
    softInputMode = mSoftInputMode;
    windowAnimations = computeAnimationResource();
    if (mBackground != null) {
      format = mBackground.getOpacity();
    } else {
      format = -3;
    }
    int i;
    if (mHeightMode < 0)
    {
      i = mHeightMode;
      mLastHeight = i;
      height = i;
    }
    else
    {
      i = mHeight;
      mLastHeight = i;
      height = i;
    }
    if (mWidthMode < 0)
    {
      i = mWidthMode;
      mLastWidth = i;
      width = i;
    }
    else
    {
      i = mWidth;
      mLastWidth = i;
      width = i;
    }
    privateFlags = 98304;
    paramIBinder = new StringBuilder();
    paramIBinder.append("PopupWindow:");
    paramIBinder.append(Integer.toHexString(hashCode()));
    localLayoutParams.setTitle(paramIBinder.toString());
    return localLayoutParams;
  }
  
  protected void detachFromAnchor()
  {
    View localView = getAnchor();
    if (localView != null)
    {
      localView.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
      localView.removeOnAttachStateChangeListener(mOnAnchorDetachedListener);
    }
    if (mAnchorRoot != null) {
      localView = (View)mAnchorRoot.get();
    } else {
      localView = null;
    }
    if (localView != null)
    {
      localView.removeOnAttachStateChangeListener(mOnAnchorRootDetachedListener);
      localView.removeOnLayoutChangeListener(mOnLayoutChangeListener);
    }
    mAnchor = null;
    mAnchorRoot = null;
    mIsAnchorRootAttached = false;
  }
  
  public void dismiss()
  {
    if ((isShowing()) && (!isTransitioningToDismiss()))
    {
      final PopupDecorView localPopupDecorView = mDecorView;
      final View localView1 = mContentView;
      Object localObject = localView1.getParent();
      boolean bool = localObject instanceof ViewGroup;
      View localView2 = null;
      if (bool) {
        localObject = (ViewGroup)localObject;
      } else {
        localObject = null;
      }
      localPopupDecorView.cancelTransitions();
      mIsShowing = false;
      mIsTransitioningToDismiss = true;
      Transition localTransition = mExitTransition;
      if ((localTransition != null) && (localPopupDecorView.isLaidOut()) && ((mIsAnchorRootAttached) || (mAnchorRoot == null)))
      {
        WindowManager.LayoutParams localLayoutParams = (WindowManager.LayoutParams)localPopupDecorView.getLayoutParams();
        flags |= 0x10;
        flags |= 0x8;
        flags &= 0xFFFDFFFF;
        mWindowManager.updateViewLayout(localPopupDecorView, localLayoutParams);
        if (mAnchorRoot != null) {
          localView2 = (View)mAnchorRoot.get();
        }
        localPopupDecorView.startExitTransition(localTransition, localView2, getTransitionEpicenter(), new TransitionListenerAdapter()
        {
          public void onTransitionEnd(Transition paramAnonymousTransition)
          {
            PopupWindow.this.dismissImmediate(localPopupDecorView, val$contentHolder, localView1);
          }
        });
      }
      else
      {
        dismissImmediate(localPopupDecorView, (ViewGroup)localObject, localView1);
      }
      detachFromAnchor();
      if (mOnDismissListener != null) {
        mOnDismissListener.onDismiss();
      }
      return;
    }
  }
  
  protected boolean findDropDownPosition(View paramView, WindowManager.LayoutParams paramLayoutParams, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
  {
    int i = paramView.getHeight();
    int j = paramView.getWidth();
    if (mOverlapAnchor) {
      paramInt2 -= i;
    }
    int[] arrayOfInt1 = mTmpAppLocation;
    Object localObject = getAppRootView(paramView);
    ((View)localObject).getLocationOnScreen(arrayOfInt1);
    int[] arrayOfInt2 = mTmpScreenLocation;
    paramView.getLocationOnScreen(arrayOfInt2);
    int[] arrayOfInt3 = mTmpDrawingLocation;
    arrayOfInt2[0] -= arrayOfInt1[0];
    arrayOfInt2[1] -= arrayOfInt1[1];
    x = (arrayOfInt3[0] + paramInt1);
    y = (arrayOfInt3[1] + i + paramInt2);
    Rect localRect = new Rect();
    ((View)localObject).getWindowVisibleDisplayFrame(localRect);
    int k = paramInt3;
    paramInt3 = k;
    if (k == -1) {
      paramInt3 = right - left;
    }
    if (paramInt4 == -1) {
      paramInt4 = bottom - top;
    }
    gravity = computeGravity();
    width = paramInt3;
    height = paramInt4;
    k = Gravity.getAbsoluteGravity(paramInt5, paramView.getLayoutDirection()) & 0x7;
    if (k == 5) {
      x -= paramInt3 - j;
    }
    boolean bool1 = tryFitVertical(paramLayoutParams, paramInt2, paramInt4, i, arrayOfInt3[1], arrayOfInt2[1], top, bottom, false);
    boolean bool2 = tryFitHorizontal(paramLayoutParams, paramInt1, paramInt3, j, arrayOfInt3[0], arrayOfInt2[0], left, right, false);
    if ((bool1) && (bool2))
    {
      paramInt1 = 1;
    }
    else
    {
      int m = paramView.getScrollX();
      paramInt5 = paramView.getScrollY();
      localObject = new Rect(m, paramInt5, m + paramInt3 + paramInt1, paramInt5 + paramInt4 + i + paramInt2);
      if (paramBoolean) {
        if (paramView.requestRectangleOnScreen((Rect)localObject, true))
        {
          paramView.getLocationOnScreen(arrayOfInt2);
          arrayOfInt2[0] -= arrayOfInt1[0];
          arrayOfInt2[1] -= arrayOfInt1[1];
          x = (arrayOfInt3[0] + paramInt1);
          y = (arrayOfInt3[1] + i + paramInt2);
          if (k == 5) {
            x -= paramInt3 - j;
          }
        }
        else {}
      }
      int n = arrayOfInt3[1];
      int i1 = arrayOfInt2[1];
      m = top;
      k = bottom;
      paramBoolean = mClipToScreen;
      paramInt5 = 1;
      tryFitVertical(paramLayoutParams, paramInt2, paramInt4, i, n, i1, m, k, paramBoolean);
      tryFitHorizontal(paramLayoutParams, paramInt1, paramInt3, j, arrayOfInt3[0], arrayOfInt2[0], left, right, mClipToScreen);
      paramInt1 = paramInt5;
    }
    if (y >= arrayOfInt3[paramInt1]) {
      paramInt1 = 0;
    }
    return paramInt1;
  }
  
  protected final boolean getAllowScrollingAnchorParent()
  {
    return mAllowScrollingAnchorParent;
  }
  
  protected View getAnchor()
  {
    View localView;
    if (mAnchor != null) {
      localView = (View)mAnchor.get();
    } else {
      localView = null;
    }
    return localView;
  }
  
  public int getAnimationStyle()
  {
    return mAnimationStyle;
  }
  
  public Drawable getBackground()
  {
    return mBackground;
  }
  
  public View getContentView()
  {
    return mContentView;
  }
  
  protected WindowManager.LayoutParams getDecorViewLayoutParams()
  {
    return (WindowManager.LayoutParams)mDecorView.getLayoutParams();
  }
  
  public float getElevation()
  {
    return mElevation;
  }
  
  public Transition getEnterTransition()
  {
    return mEnterTransition;
  }
  
  public Transition getExitTransition()
  {
    return mExitTransition;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getInputMethodMode()
  {
    return mInputMethodMode;
  }
  
  public int getMaxAvailableHeight(View paramView)
  {
    return getMaxAvailableHeight(paramView, 0);
  }
  
  public int getMaxAvailableHeight(View paramView, int paramInt)
  {
    return getMaxAvailableHeight(paramView, paramInt, false);
  }
  
  public int getMaxAvailableHeight(View paramView, int paramInt, boolean paramBoolean)
  {
    Object localObject1 = new Rect();
    getAppRootView(paramView).getWindowVisibleDisplayFrame((Rect)localObject1);
    if (paramBoolean)
    {
      localObject2 = new Rect();
      paramView.getWindowDisplayFrame((Rect)localObject2);
      top = top;
      right = right;
      left = left;
      localObject1 = localObject2;
    }
    Object localObject2 = mTmpDrawingLocation;
    paramView.getLocationOnScreen((int[])localObject2);
    int i = bottom;
    if (mOverlapAnchor) {
      i = i - localObject2[1] - paramInt;
    } else {
      i = i - (localObject2[1] + paramView.getHeight()) - paramInt;
    }
    i = Math.max(i, localObject2[1] - top + paramInt);
    paramInt = i;
    if (mBackground != null)
    {
      mBackground.getPadding(mTempRect);
      paramInt = i - (mTempRect.top + mTempRect.bottom);
    }
    return paramInt;
  }
  
  protected final OnDismissListener getOnDismissListener()
  {
    return mOnDismissListener;
  }
  
  public boolean getOverlapAnchor()
  {
    return mOverlapAnchor;
  }
  
  public int getSoftInputMode()
  {
    return mSoftInputMode;
  }
  
  protected final Rect getTransitionEpicenter()
  {
    Object localObject1;
    if (mAnchor != null) {
      localObject1 = (View)mAnchor.get();
    } else {
      localObject1 = null;
    }
    Object localObject2 = mDecorView;
    if ((localObject1 != null) && (localObject2 != null))
    {
      int[] arrayOfInt = ((View)localObject1).getLocationOnScreen();
      localObject2 = mDecorView.getLocationOnScreen();
      localObject1 = new Rect(0, 0, ((View)localObject1).getWidth(), ((View)localObject1).getHeight());
      ((Rect)localObject1).offset(arrayOfInt[0] - localObject2[0], arrayOfInt[1] - localObject2[1]);
      if (mEpicenterBounds != null)
      {
        int i = left;
        int j = top;
        ((Rect)localObject1).set(mEpicenterBounds);
        ((Rect)localObject1).offset(i, j);
      }
      return localObject1;
    }
    return null;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public int getWindowLayoutType()
  {
    return mWindowLayoutType;
  }
  
  protected boolean hasContentView()
  {
    boolean bool;
    if (mContentView != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean hasDecorView()
  {
    boolean bool;
    if (mDecorView != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAboveAnchor()
  {
    return mAboveAnchor;
  }
  
  public boolean isAttachedInDecor()
  {
    return mAttachedInDecor;
  }
  
  public boolean isClippingEnabled()
  {
    return mClippingEnabled;
  }
  
  public boolean isFocusable()
  {
    return mFocusable;
  }
  
  public boolean isLayoutInScreenEnabled()
  {
    return mLayoutInScreen;
  }
  
  protected final boolean isLayoutInsetDecor()
  {
    return mLayoutInsetDecor;
  }
  
  public boolean isOutsideTouchable()
  {
    return mOutsideTouchable;
  }
  
  public boolean isShowing()
  {
    return mIsShowing;
  }
  
  public boolean isSplitTouchEnabled()
  {
    int i = mSplitTouchEnabled;
    boolean bool1 = false;
    boolean bool2 = false;
    if ((i < 0) && (mContext != null))
    {
      if (mContext.getApplicationInfo().targetSdkVersion >= 11) {
        bool2 = true;
      }
      return bool2;
    }
    bool2 = bool1;
    if (mSplitTouchEnabled == 1) {
      bool2 = true;
    }
    return bool2;
  }
  
  public boolean isTouchable()
  {
    return mTouchable;
  }
  
  protected final boolean isTransitioningToDismiss()
  {
    return mIsTransitioningToDismiss;
  }
  
  void setAllowScrollingAnchorParent(boolean paramBoolean)
  {
    mAllowScrollingAnchorParent = paramBoolean;
  }
  
  public void setAnimationStyle(int paramInt)
  {
    mAnimationStyle = paramInt;
  }
  
  public void setAttachedInDecor(boolean paramBoolean)
  {
    mAttachedInDecor = paramBoolean;
    mAttachedInDecorSet = true;
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    mBackground = paramDrawable;
    if ((mBackground instanceof StateListDrawable))
    {
      paramDrawable = (StateListDrawable)mBackground;
      int i = paramDrawable.getStateDrawableIndex(ABOVE_ANCHOR_STATE_SET);
      int j = paramDrawable.getStateCount();
      int k = -1;
      int n;
      for (int m = 0;; m++)
      {
        n = k;
        if (m >= j) {
          break;
        }
        if (m != i)
        {
          n = m;
          break;
        }
      }
      if ((i != -1) && (n != -1))
      {
        mAboveAnchorBackgroundDrawable = paramDrawable.getStateDrawable(i);
        mBelowAnchorBackgroundDrawable = paramDrawable.getStateDrawable(n);
      }
      else
      {
        mBelowAnchorBackgroundDrawable = null;
        mAboveAnchorBackgroundDrawable = null;
      }
    }
  }
  
  public void setClipToScreenEnabled(boolean paramBoolean)
  {
    mClipToScreen = paramBoolean;
  }
  
  public void setClippingEnabled(boolean paramBoolean)
  {
    mClippingEnabled = paramBoolean;
  }
  
  public void setContentView(View paramView)
  {
    if (isShowing()) {
      return;
    }
    mContentView = paramView;
    if ((mContext == null) && (mContentView != null)) {
      mContext = mContentView.getContext();
    }
    if ((mWindowManager == null) && (mContentView != null)) {
      mWindowManager = ((WindowManager)mContext.getSystemService("window"));
    }
    if ((mContext != null) && (!mAttachedInDecorSet))
    {
      boolean bool;
      if (mContext.getApplicationInfo().targetSdkVersion >= 22) {
        bool = true;
      } else {
        bool = false;
      }
      setAttachedInDecor(bool);
    }
  }
  
  protected final void setDropDown(boolean paramBoolean)
  {
    mIsDropdown = paramBoolean;
  }
  
  public void setElevation(float paramFloat)
  {
    mElevation = paramFloat;
  }
  
  public void setEnterTransition(Transition paramTransition)
  {
    mEnterTransition = paramTransition;
  }
  
  public void setEpicenterBounds(Rect paramRect)
  {
    mEpicenterBounds = paramRect;
  }
  
  public void setExitTransition(Transition paramTransition)
  {
    mExitTransition = paramTransition;
  }
  
  public void setFocusable(boolean paramBoolean)
  {
    mFocusable = paramBoolean;
  }
  
  public void setHeight(int paramInt)
  {
    mHeight = paramInt;
  }
  
  public void setIgnoreCheekPress()
  {
    mIgnoreCheekPress = true;
  }
  
  public void setInputMethodMode(int paramInt)
  {
    mInputMethodMode = paramInt;
  }
  
  public void setLayoutInScreenEnabled(boolean paramBoolean)
  {
    mLayoutInScreen = paramBoolean;
  }
  
  public void setLayoutInsetDecor(boolean paramBoolean)
  {
    mLayoutInsetDecor = paramBoolean;
  }
  
  public void setOnDismissListener(OnDismissListener paramOnDismissListener)
  {
    mOnDismissListener = paramOnDismissListener;
  }
  
  public void setOutsideTouchable(boolean paramBoolean)
  {
    mOutsideTouchable = paramBoolean;
  }
  
  public void setOverlapAnchor(boolean paramBoolean)
  {
    mOverlapAnchor = paramBoolean;
  }
  
  protected final void setShowing(boolean paramBoolean)
  {
    mIsShowing = paramBoolean;
  }
  
  public void setSoftInputMode(int paramInt)
  {
    mSoftInputMode = paramInt;
  }
  
  public void setSplitTouchEnabled(boolean paramBoolean)
  {
    mSplitTouchEnabled = paramBoolean;
  }
  
  public void setTouchInterceptor(View.OnTouchListener paramOnTouchListener)
  {
    mTouchInterceptor = paramOnTouchListener;
  }
  
  public void setTouchModal(boolean paramBoolean)
  {
    mNotTouchModal = (paramBoolean ^ true);
  }
  
  public void setTouchable(boolean paramBoolean)
  {
    mTouchable = paramBoolean;
  }
  
  protected final void setTransitioningToDismiss(boolean paramBoolean)
  {
    mIsTransitioningToDismiss = paramBoolean;
  }
  
  public void setWidth(int paramInt)
  {
    mWidth = paramInt;
  }
  
  @Deprecated
  public void setWindowLayoutMode(int paramInt1, int paramInt2)
  {
    mWidthMode = paramInt1;
    mHeightMode = paramInt2;
  }
  
  public void setWindowLayoutType(int paramInt)
  {
    mWindowLayoutType = paramInt;
  }
  
  public void showAsDropDown(View paramView)
  {
    showAsDropDown(paramView, 0, 0);
  }
  
  public void showAsDropDown(View paramView, int paramInt1, int paramInt2)
  {
    showAsDropDown(paramView, paramInt1, paramInt2, 8388659);
  }
  
  public void showAsDropDown(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((!isShowing()) && (hasContentView()))
    {
      TransitionManager.endTransitions(mDecorView);
      attachToAnchor(paramView, paramInt1, paramInt2, paramInt3);
      mIsShowing = true;
      mIsDropdown = true;
      WindowManager.LayoutParams localLayoutParams = createPopupLayoutParams(paramView.getApplicationWindowToken());
      preparePopup(localLayoutParams);
      updateAboveAnchor(findDropDownPosition(paramView, localLayoutParams, paramInt1, paramInt2, width, height, paramInt3, mAllowScrollingAnchorParent));
      long l;
      if (paramView != null) {
        l = paramView.getAccessibilityViewId();
      } else {
        l = -1L;
      }
      accessibilityIdOfAnchor = l;
      invokePopup(localLayoutParams);
      return;
    }
  }
  
  public void showAtLocation(IBinder paramIBinder, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((!isShowing()) && (mContentView != null))
    {
      TransitionManager.endTransitions(mDecorView);
      detachFromAnchor();
      mIsShowing = true;
      mIsDropdown = false;
      mGravity = paramInt1;
      paramIBinder = createPopupLayoutParams(paramIBinder);
      preparePopup(paramIBinder);
      x = paramInt2;
      y = paramInt3;
      invokePopup(paramIBinder);
      return;
    }
  }
  
  public void showAtLocation(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    mParentRootView = new WeakReference(paramView.getRootView());
    showAtLocation(paramView.getWindowToken(), paramInt1, paramInt2, paramInt3);
  }
  
  public void update()
  {
    if ((isShowing()) && (hasContentView()))
    {
      WindowManager.LayoutParams localLayoutParams = getDecorViewLayoutParams();
      int i = 0;
      int j = computeAnimationResource();
      if (j != windowAnimations)
      {
        windowAnimations = j;
        i = 1;
      }
      j = computeFlags(flags);
      if (j != flags)
      {
        flags = j;
        i = 1;
      }
      j = computeGravity();
      if (j != gravity)
      {
        gravity = j;
        i = 1;
      }
      if (i != 0)
      {
        View localView;
        if (mAnchor != null) {
          localView = (View)mAnchor.get();
        } else {
          localView = null;
        }
        update(localView, localLayoutParams);
      }
      return;
    }
  }
  
  public void update(int paramInt1, int paramInt2)
  {
    WindowManager.LayoutParams localLayoutParams = getDecorViewLayoutParams();
    update(x, y, paramInt1, paramInt2, false);
  }
  
  public void update(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    update(paramInt1, paramInt2, paramInt3, paramInt4, false);
  }
  
  public void update(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    if (paramInt3 >= 0)
    {
      mLastWidth = paramInt3;
      setWidth(paramInt3);
    }
    if (paramInt4 >= 0)
    {
      mLastHeight = paramInt4;
      setHeight(paramInt4);
    }
    if ((isShowing()) && (hasContentView()))
    {
      WindowManager.LayoutParams localLayoutParams = getDecorViewLayoutParams();
      boolean bool = paramBoolean;
      int i;
      if (mWidthMode < 0) {
        i = mWidthMode;
      } else {
        i = mLastWidth;
      }
      paramBoolean = bool;
      if (paramInt3 != -1)
      {
        paramBoolean = bool;
        if (width != i)
        {
          mLastWidth = i;
          width = i;
          paramBoolean = true;
        }
      }
      if (mHeightMode < 0) {
        paramInt3 = mHeightMode;
      } else {
        paramInt3 = mLastHeight;
      }
      bool = paramBoolean;
      if (paramInt4 != -1)
      {
        bool = paramBoolean;
        if (height != paramInt3)
        {
          mLastHeight = paramInt3;
          height = paramInt3;
          bool = true;
        }
      }
      paramBoolean = bool;
      if (x != paramInt1)
      {
        x = paramInt1;
        paramBoolean = true;
      }
      if (y != paramInt2)
      {
        y = paramInt2;
        paramBoolean = true;
      }
      paramInt1 = computeAnimationResource();
      if (paramInt1 != windowAnimations)
      {
        windowAnimations = paramInt1;
        paramBoolean = true;
      }
      paramInt1 = computeFlags(flags);
      if (paramInt1 != flags)
      {
        flags = paramInt1;
        paramBoolean = true;
      }
      paramInt1 = computeGravity();
      if (paramInt1 != gravity)
      {
        gravity = paramInt1;
        paramBoolean = true;
      }
      Object localObject1 = null;
      paramInt2 = -1;
      Object localObject2 = localObject1;
      paramInt1 = paramInt2;
      if (mAnchor != null)
      {
        localObject2 = localObject1;
        paramInt1 = paramInt2;
        if (mAnchor.get() != null)
        {
          localObject2 = (View)mAnchor.get();
          paramInt1 = ((View)localObject2).getAccessibilityViewId();
        }
      }
      if (paramInt1 != accessibilityIdOfAnchor)
      {
        accessibilityIdOfAnchor = paramInt1;
        paramBoolean = true;
      }
      if (paramBoolean) {
        update((View)localObject2, localLayoutParams);
      }
      return;
    }
  }
  
  public void update(View paramView, int paramInt1, int paramInt2)
  {
    update(paramView, false, 0, 0, paramInt1, paramInt2);
  }
  
  public void update(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    update(paramView, true, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void update(View paramView, WindowManager.LayoutParams paramLayoutParams)
  {
    setLayoutDirectionFromAnchor();
    mWindowManager.updateViewLayout(mDecorView, paramLayoutParams);
  }
  
  protected final void updateAboveAnchor(boolean paramBoolean)
  {
    if (paramBoolean != mAboveAnchor)
    {
      mAboveAnchor = paramBoolean;
      if ((mBackground != null) && (mBackgroundView != null)) {
        if (mAboveAnchorBackgroundDrawable != null)
        {
          if (mAboveAnchor) {
            mBackgroundView.setBackground(mAboveAnchorBackgroundDrawable);
          } else {
            mBackgroundView.setBackground(mBelowAnchorBackgroundDrawable);
          }
        }
        else {
          mBackgroundView.refreshDrawableState();
        }
      }
    }
  }
  
  public static abstract interface OnDismissListener
  {
    public abstract void onDismiss();
  }
  
  private class PopupBackgroundView
    extends FrameLayout
  {
    public PopupBackgroundView(Context paramContext)
    {
      super();
    }
    
    protected int[] onCreateDrawableState(int paramInt)
    {
      if (mAboveAnchor)
      {
        int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
        View.mergeDrawableStates(arrayOfInt, PopupWindow.ABOVE_ANCHOR_STATE_SET);
        return arrayOfInt;
      }
      return super.onCreateDrawableState(paramInt);
    }
  }
  
  private class PopupDecorView
    extends FrameLayout
  {
    private Runnable mCleanupAfterExit;
    private final View.OnAttachStateChangeListener mOnAnchorRootDetachedListener = new View.OnAttachStateChangeListener()
    {
      public void onViewAttachedToWindow(View paramAnonymousView) {}
      
      public void onViewDetachedFromWindow(View paramAnonymousView)
      {
        paramAnonymousView.removeOnAttachStateChangeListener(this);
        if (isAttachedToWindow()) {
          TransitionManager.endTransitions(PopupWindow.PopupDecorView.this);
        }
      }
    };
    
    public PopupDecorView(Context paramContext)
    {
      super();
    }
    
    private void startEnterTransition(Transition paramTransition)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        paramTransition.addTarget(localView);
        localView.setTransitionVisibility(4);
      }
      TransitionManager.beginDelayedTransition(this, paramTransition);
      for (j = 0; j < i; j++) {
        getChildAt(j).setTransitionVisibility(0);
      }
    }
    
    public void cancelTransitions()
    {
      TransitionManager.endTransitions(this);
      if (mCleanupAfterExit != null) {
        mCleanupAfterExit.run();
      }
    }
    
    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
    {
      if (paramKeyEvent.getKeyCode() == 4)
      {
        if (getKeyDispatcherState() == null) {
          return super.dispatchKeyEvent(paramKeyEvent);
        }
        KeyEvent.DispatcherState localDispatcherState;
        if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
        {
          localDispatcherState = getKeyDispatcherState();
          if (localDispatcherState != null) {
            localDispatcherState.startTracking(paramKeyEvent, this);
          }
          return true;
        }
        if (paramKeyEvent.getAction() == 1)
        {
          localDispatcherState = getKeyDispatcherState();
          if ((localDispatcherState != null) && (localDispatcherState.isTracking(paramKeyEvent)) && (!paramKeyEvent.isCanceled()))
          {
            dismiss();
            return true;
          }
        }
        return super.dispatchKeyEvent(paramKeyEvent);
      }
      return super.dispatchKeyEvent(paramKeyEvent);
    }
    
    public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
    {
      if ((mTouchInterceptor != null) && (mTouchInterceptor.onTouch(this, paramMotionEvent))) {
        return true;
      }
      return super.dispatchTouchEvent(paramMotionEvent);
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      if ((paramMotionEvent.getAction() == 0) && ((i < 0) || (i >= getWidth()) || (j < 0) || (j >= getHeight())))
      {
        dismiss();
        return true;
      }
      if (paramMotionEvent.getAction() == 4)
      {
        dismiss();
        return true;
      }
      return super.onTouchEvent(paramMotionEvent);
    }
    
    public void requestEnterTransition(Transition paramTransition)
    {
      ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
      if ((localViewTreeObserver != null) && (paramTransition != null)) {
        localViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
          public void onGlobalLayout()
          {
            Object localObject = getViewTreeObserver();
            if (localObject != null) {
              ((ViewTreeObserver)localObject).removeOnGlobalLayoutListener(this);
            }
            localObject = getTransitionEpicenter();
            val$enterTransition.setEpicenterCallback(new Transition.EpicenterCallback()
            {
              public Rect onGetEpicenter(Transition paramAnonymous2Transition)
              {
                return val$epicenter;
              }
            });
            PopupWindow.PopupDecorView.this.startEnterTransition(val$enterTransition);
          }
        });
      }
    }
    
    public void requestKeyboardShortcuts(List<KeyboardShortcutGroup> paramList, int paramInt)
    {
      if (mParentRootView != null)
      {
        View localView = (View)mParentRootView.get();
        if (localView != null) {
          localView.requestKeyboardShortcuts(paramList, paramInt);
        }
      }
    }
    
    public void startExitTransition(Transition paramTransition, View paramView, final Rect paramRect, Transition.TransitionListener paramTransitionListener)
    {
      if (paramTransition == null) {
        return;
      }
      if (paramView != null) {
        paramView.addOnAttachStateChangeListener(mOnAnchorRootDetachedListener);
      }
      mCleanupAfterExit = new _..Lambda.PopupWindow.PopupDecorView.T99WKEnQefOCXbbKvW95WY38p_I(this, paramTransitionListener, paramTransition, paramView);
      paramTransition = paramTransition.clone();
      paramTransition.addListener(new TransitionListenerAdapter()
      {
        public void onTransitionEnd(Transition paramAnonymousTransition)
        {
          paramAnonymousTransition.removeListener(this);
          if (mCleanupAfterExit != null) {
            mCleanupAfterExit.run();
          }
        }
      });
      paramTransition.setEpicenterCallback(new Transition.EpicenterCallback()
      {
        public Rect onGetEpicenter(Transition paramAnonymousTransition)
        {
          return paramRect;
        }
      });
      int i = getChildCount();
      int j = 0;
      for (int k = 0; k < i; k++) {
        paramTransition.addTarget(getChildAt(k));
      }
      TransitionManager.beginDelayedTransition(this, paramTransition);
      for (k = j; k < i; k++) {
        getChildAt(k).setVisibility(4);
      }
    }
  }
}
