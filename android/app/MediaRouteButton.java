package android.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteGroup;
import android.media.MediaRouter.RouteInfo;
import android.media.MediaRouter.SimpleCallback;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import com.android.internal.R.styleable;
import com.android.internal.app.MediaRouteDialogPresenter;

public class MediaRouteButton
  extends View
{
  private static final int[] ACTIVATED_STATE_SET = { 16843518 };
  private static final int[] CHECKED_STATE_SET = { 16842912 };
  private boolean mAttachedToWindow;
  private final MediaRouterCallback mCallback;
  private View.OnClickListener mExtendedSettingsClickListener;
  private boolean mIsConnecting;
  private int mMinHeight;
  private int mMinWidth;
  private boolean mRemoteActive;
  private Drawable mRemoteIndicator;
  private int mRouteTypes;
  private final MediaRouter mRouter;
  
  public MediaRouteButton(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public MediaRouteButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843693);
  }
  
  public MediaRouteButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public MediaRouteButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mRouter = ((MediaRouter)paramContext.getSystemService("media_router"));
    mCallback = new MediaRouterCallback(null);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MediaRouteButton, paramInt1, paramInt2);
    setRemoteIndicatorDrawable(paramContext.getDrawable(3));
    mMinWidth = paramContext.getDimensionPixelSize(0, 0);
    mMinHeight = paramContext.getDimensionPixelSize(1, 0);
    paramInt1 = paramContext.getInteger(2, 1);
    paramContext.recycle();
    setClickable(true);
    setRouteTypes(paramInt1);
  }
  
  private Activity getActivity()
  {
    for (Context localContext = getContext(); (localContext instanceof ContextWrapper); localContext = ((ContextWrapper)localContext).getBaseContext()) {
      if ((localContext instanceof Activity)) {
        return (Activity)localContext;
      }
    }
    throw new IllegalStateException("The MediaRouteButton's Context is not an Activity.");
  }
  
  private void refreshRoute()
  {
    Object localObject = mRouter.getSelectedRoute();
    boolean bool1 = ((MediaRouter.RouteInfo)localObject).isDefault();
    boolean bool2 = false;
    if ((!bool1) && (((MediaRouter.RouteInfo)localObject).matchesTypes(mRouteTypes))) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool3 = bool2;
    if (bool1)
    {
      bool3 = bool2;
      if (((MediaRouter.RouteInfo)localObject).isConnecting()) {
        bool3 = true;
      }
    }
    int i = 0;
    if (mRemoteActive != bool1)
    {
      mRemoteActive = bool1;
      i = 1;
    }
    if (mIsConnecting != bool3)
    {
      mIsConnecting = bool3;
      i = 1;
    }
    if (i != 0) {
      refreshDrawableState();
    }
    if (mAttachedToWindow) {
      setEnabled(mRouter.isRouteAvailable(mRouteTypes, 1));
    }
    if ((mRemoteIndicator != null) && ((mRemoteIndicator.getCurrent() instanceof AnimationDrawable)))
    {
      localObject = (AnimationDrawable)mRemoteIndicator.getCurrent();
      if (mAttachedToWindow)
      {
        if (((i != 0) || (bool3)) && (!((AnimationDrawable)localObject).isRunning())) {
          ((AnimationDrawable)localObject).start();
        }
      }
      else if ((bool1) && (!bool3))
      {
        if (((AnimationDrawable)localObject).isRunning()) {
          ((AnimationDrawable)localObject).stop();
        }
        ((AnimationDrawable)localObject).selectDrawable(((AnimationDrawable)localObject).getNumberOfFrames() - 1);
      }
    }
  }
  
  private void setRemoteIndicatorDrawable(Drawable paramDrawable)
  {
    if (mRemoteIndicator != null)
    {
      mRemoteIndicator.setCallback(null);
      unscheduleDrawable(mRemoteIndicator);
    }
    mRemoteIndicator = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      paramDrawable.setState(getDrawableState());
      boolean bool;
      if (getVisibility() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      paramDrawable.setVisible(bool, false);
    }
    refreshDrawableState();
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = mRemoteIndicator;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
  }
  
  public int getRouteTypes()
  {
    return mRouteTypes;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mRemoteIndicator != null) {
      mRemoteIndicator.jumpToCurrentState();
    }
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    mAttachedToWindow = true;
    if (mRouteTypes != 0) {
      mRouter.addCallback(mRouteTypes, mCallback, 8);
    }
    refreshRoute();
  }
  
  protected int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (mIsConnecting) {
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET);
    } else if (mRemoteActive) {
      mergeDrawableStates(arrayOfInt, ACTIVATED_STATE_SET);
    }
    return arrayOfInt;
  }
  
  public void onDetachedFromWindow()
  {
    mAttachedToWindow = false;
    if (mRouteTypes != 0) {
      mRouter.removeCallback(mCallback);
    }
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (mRemoteIndicator == null) {
      return;
    }
    int i = getPaddingLeft();
    int j = getWidth();
    int k = getPaddingRight();
    int m = getPaddingTop();
    int n = getHeight();
    int i1 = getPaddingBottom();
    int i2 = mRemoteIndicator.getIntrinsicWidth();
    int i3 = mRemoteIndicator.getIntrinsicHeight();
    i = (j - k - i - i2) / 2 + i;
    i1 = (n - i1 - m - i3) / 2 + m;
    mRemoteIndicator.setBounds(i, i1, i + i2, i1 + i3);
    mRemoteIndicator.draw(paramCanvas);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = mMinWidth;
    Drawable localDrawable = mRemoteIndicator;
    paramInt2 = 0;
    if (localDrawable != null) {
      paramInt1 = mRemoteIndicator.getIntrinsicWidth() + getPaddingLeft() + getPaddingRight();
    } else {
      paramInt1 = 0;
    }
    n = Math.max(n, paramInt1);
    int i1 = mMinHeight;
    paramInt1 = paramInt2;
    if (mRemoteIndicator != null) {
      paramInt1 = mRemoteIndicator.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom();
    }
    paramInt2 = Math.max(i1, paramInt1);
    if (k != Integer.MIN_VALUE)
    {
      if (k != 1073741824) {
        paramInt1 = n;
      } else {
        paramInt1 = i;
      }
    }
    else {
      paramInt1 = Math.min(i, n);
    }
    if (m != Integer.MIN_VALUE)
    {
      if (m == 1073741824) {
        paramInt2 = j;
      }
    }
    else {
      paramInt2 = Math.min(j, paramInt2);
    }
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public boolean performClick()
  {
    boolean bool1 = super.performClick();
    boolean bool2 = false;
    if (!bool1) {
      playSoundEffect(0);
    }
    if ((!showDialogInternal()) && (!bool1)) {
      return bool2;
    }
    bool2 = true;
    return bool2;
  }
  
  public void setContentDescription(CharSequence paramCharSequence)
  {
    super.setContentDescription(paramCharSequence);
    setTooltipText(paramCharSequence);
  }
  
  public void setExtendedSettingsClickListener(View.OnClickListener paramOnClickListener)
  {
    mExtendedSettingsClickListener = paramOnClickListener;
  }
  
  public void setRouteTypes(int paramInt)
  {
    if (mRouteTypes != paramInt)
    {
      if ((mAttachedToWindow) && (mRouteTypes != 0)) {
        mRouter.removeCallback(mCallback);
      }
      mRouteTypes = paramInt;
      if ((mAttachedToWindow) && (paramInt != 0)) {
        mRouter.addCallback(paramInt, mCallback, 8);
      }
      refreshRoute();
    }
  }
  
  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    if (mRemoteIndicator != null)
    {
      Drawable localDrawable = mRemoteIndicator;
      boolean bool;
      if (getVisibility() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      localDrawable.setVisible(bool, false);
    }
  }
  
  public void showDialog()
  {
    showDialogInternal();
  }
  
  boolean showDialogInternal()
  {
    boolean bool1 = mAttachedToWindow;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if (MediaRouteDialogPresenter.showDialogFragment(getActivity(), mRouteTypes, mExtendedSettingsClickListener) != null) {
      bool2 = true;
    }
    return bool2;
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((!super.verifyDrawable(paramDrawable)) && (paramDrawable != mRemoteIndicator)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private final class MediaRouterCallback
    extends MediaRouter.SimpleCallback
  {
    private MediaRouterCallback() {}
    
    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }
    
    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }
    
    public void onRouteGrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup, int paramInt)
    {
      MediaRouteButton.this.refreshRoute();
    }
    
    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }
    
    public void onRouteSelected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }
    
    public void onRouteUngrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup)
    {
      MediaRouteButton.this.refreshRoute();
    }
    
    public void onRouteUnselected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }
  }
}
