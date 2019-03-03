package com.android.internal.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteGroup;
import android.media.MediaRouter.RouteInfo;
import android.media.MediaRouter.SimpleCallback;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MediaRouteControllerDialog
  extends AlertDialog
{
  private static final int VOLUME_UPDATE_DELAY_MILLIS = 250;
  private boolean mAttachedToWindow;
  private final MediaRouterCallback mCallback;
  private View mControlView;
  private boolean mCreated;
  private Drawable mCurrentIconDrawable;
  private Drawable mMediaRouteButtonDrawable;
  private int[] mMediaRouteConnectingState = { 16842912, 16842910 };
  private int[] mMediaRouteOnState = { 16843518, 16842910 };
  private final MediaRouter.RouteInfo mRoute;
  private final MediaRouter mRouter;
  private boolean mVolumeControlEnabled = true;
  private LinearLayout mVolumeLayout;
  private SeekBar mVolumeSlider;
  private boolean mVolumeSliderTouched;
  
  public MediaRouteControllerDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
    mRouter = ((MediaRouter)paramContext.getSystemService("media_router"));
    mCallback = new MediaRouterCallback(null);
    mRoute = mRouter.getSelectedRoute();
  }
  
  private Drawable getIconDrawable()
  {
    if (!(mMediaRouteButtonDrawable instanceof StateListDrawable)) {
      return mMediaRouteButtonDrawable;
    }
    if (mRoute.isConnecting())
    {
      localStateListDrawable = (StateListDrawable)mMediaRouteButtonDrawable;
      localStateListDrawable.setState(mMediaRouteConnectingState);
      return localStateListDrawable.getCurrent();
    }
    StateListDrawable localStateListDrawable = (StateListDrawable)mMediaRouteButtonDrawable;
    localStateListDrawable.setState(mMediaRouteOnState);
    return localStateListDrawable.getCurrent();
  }
  
  private boolean isVolumeControlAvailable()
  {
    boolean bool1 = mVolumeControlEnabled;
    boolean bool2 = true;
    if ((!bool1) || (mRoute.getVolumeHandling() != 1)) {
      bool2 = false;
    }
    return bool2;
  }
  
  private Drawable obtainMediaRouteButtonDrawable()
  {
    Object localObject1 = getContext();
    Object localObject2 = new TypedValue();
    if (!((Context)localObject1).getTheme().resolveAttribute(16843693, (TypedValue)localObject2, true)) {
      return null;
    }
    localObject2 = ((Context)localObject1).obtainStyledAttributes(data, new int[] { 17891383 });
    localObject1 = ((TypedArray)localObject2).getDrawable(0);
    ((TypedArray)localObject2).recycle();
    return localObject1;
  }
  
  private boolean update()
  {
    if ((mRoute.isSelected()) && (!mRoute.isDefault()))
    {
      setTitle(mRoute.getName());
      updateVolume();
      Drawable localDrawable1 = getIconDrawable();
      if (localDrawable1 != mCurrentIconDrawable)
      {
        mCurrentIconDrawable = localDrawable1;
        Drawable localDrawable2 = localDrawable1;
        if ((localDrawable1 instanceof AnimationDrawable))
        {
          AnimationDrawable localAnimationDrawable = (AnimationDrawable)localDrawable1;
          if ((!mAttachedToWindow) && (!mRoute.isConnecting()))
          {
            if (localAnimationDrawable.isRunning()) {
              localAnimationDrawable.stop();
            }
            localDrawable2 = localAnimationDrawable.getFrame(localAnimationDrawable.getNumberOfFrames() - 1);
          }
          else
          {
            localDrawable2 = localDrawable1;
            if (!localAnimationDrawable.isRunning())
            {
              localAnimationDrawable.start();
              localDrawable2 = localDrawable1;
            }
          }
        }
        setIcon(localDrawable2);
      }
      return true;
    }
    dismiss();
    return false;
  }
  
  private void updateVolume()
  {
    if (!mVolumeSliderTouched) {
      if (isVolumeControlAvailable())
      {
        mVolumeLayout.setVisibility(0);
        mVolumeSlider.setMax(mRoute.getVolumeMax());
        mVolumeSlider.setProgress(mRoute.getVolume());
      }
      else
      {
        mVolumeLayout.setVisibility(8);
      }
    }
  }
  
  public View getMediaControlView()
  {
    return mControlView;
  }
  
  public MediaRouter.RouteInfo getRoute()
  {
    return mRoute;
  }
  
  public boolean isVolumeControlEnabled()
  {
    return mVolumeControlEnabled;
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    mAttachedToWindow = true;
    mRouter.addCallback(0, mCallback, 2);
    update();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    setTitle(mRoute.getName());
    setButton(-2, getContext().getResources().getString(17040335), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        if (mRoute.isSelected()) {
          if (mRoute.isBluetooth()) {
            mRouter.getDefaultRoute().select();
          } else {
            mRouter.getFallbackRoute().select();
          }
        }
        dismiss();
      }
    });
    View localView1 = getLayoutInflater().inflate(17367206, null);
    setView(localView1, 0, 0, 0, 0);
    super.onCreate(paramBundle);
    View localView2 = getWindow().findViewById(16908880);
    if (localView2 != null) {
      localView2.setMinimumHeight(0);
    }
    mVolumeLayout = ((LinearLayout)localView1.findViewById(16909116));
    mVolumeSlider = ((SeekBar)localView1.findViewById(16909117));
    mVolumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      private final Runnable mStopTrackingTouch = new Runnable()
      {
        public void run()
        {
          if (mVolumeSliderTouched)
          {
            MediaRouteControllerDialog.access$302(MediaRouteControllerDialog.this, false);
            MediaRouteControllerDialog.this.updateVolume();
          }
        }
      };
      
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean) {
          mRoute.requestSetVolume(paramAnonymousInt);
        }
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        if (mVolumeSliderTouched) {
          mVolumeSlider.removeCallbacks(mStopTrackingTouch);
        } else {
          MediaRouteControllerDialog.access$302(MediaRouteControllerDialog.this, true);
        }
      }
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        mVolumeSlider.postDelayed(mStopTrackingTouch, 250L);
      }
    });
    mMediaRouteButtonDrawable = obtainMediaRouteButtonDrawable();
    mCreated = true;
    if (update())
    {
      mControlView = onCreateMediaControlView(paramBundle);
      paramBundle = (FrameLayout)localView1.findViewById(16909113);
      if (mControlView != null)
      {
        paramBundle.addView(mControlView);
        paramBundle.setVisibility(0);
      }
      else
      {
        paramBundle.setVisibility(8);
      }
    }
  }
  
  public View onCreateMediaControlView(Bundle paramBundle)
  {
    return null;
  }
  
  public void onDetachedFromWindow()
  {
    mRouter.removeCallback(mCallback);
    mAttachedToWindow = false;
    super.onDetachedFromWindow();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt != 25) && (paramInt != 24)) {
      return super.onKeyDown(paramInt, paramKeyEvent);
    }
    paramKeyEvent = mRoute;
    if (paramInt == 25) {
      paramInt = -1;
    } else {
      paramInt = 1;
    }
    paramKeyEvent.requestUpdateVolume(paramInt);
    return true;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt != 25) && (paramInt != 24)) {
      return super.onKeyUp(paramInt, paramKeyEvent);
    }
    return true;
  }
  
  public void setVolumeControlEnabled(boolean paramBoolean)
  {
    if (mVolumeControlEnabled != paramBoolean)
    {
      mVolumeControlEnabled = paramBoolean;
      if (mCreated) {
        updateVolume();
      }
    }
  }
  
  private final class MediaRouterCallback
    extends MediaRouter.SimpleCallback
  {
    private MediaRouterCallback() {}
    
    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteControllerDialog.this.update();
    }
    
    public void onRouteGrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup, int paramInt)
    {
      MediaRouteControllerDialog.this.update();
    }
    
    public void onRouteUngrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup)
    {
      MediaRouteControllerDialog.this.update();
    }
    
    public void onRouteUnselected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteControllerDialog.this.update();
    }
    
    public void onRouteVolumeChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      if (paramRouteInfo == mRoute) {
        MediaRouteControllerDialog.this.updateVolume();
      }
    }
  }
}
