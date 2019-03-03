package android.widget;

import android.content.Context;
import android.media.SessionToken2;
import android.media.session.MediaController;
import android.media.update.MediaControlView2Provider;
import android.media.update.ViewGroupHelper;
import android.util.AttributeSet;
import android.view.View;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MediaControlView2
  extends ViewGroupHelper<MediaControlView2Provider>
{
  public static final int BUTTON_ASPECT_RATIO = 10;
  public static final int BUTTON_FFWD = 2;
  public static final int BUTTON_FULL_SCREEN = 7;
  public static final int BUTTON_MUTE = 9;
  public static final int BUTTON_NEXT = 4;
  public static final int BUTTON_OVERFLOW = 8;
  public static final int BUTTON_PLAY_PAUSE = 1;
  public static final int BUTTON_PREV = 5;
  public static final int BUTTON_REW = 3;
  public static final int BUTTON_SETTINGS = 11;
  public static final int BUTTON_SUBTITLE = 6;
  
  public MediaControlView2(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public MediaControlView2(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public MediaControlView2(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public MediaControlView2(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(new _..Lambda.MediaControlView2.RI38ILmx2NwSJumbm0C4a0I_utM(paramAttributeSet, paramInt1, paramInt2), paramContext, paramAttributeSet, paramInt1, paramInt2);
    ((MediaControlView2Provider)mProvider).initialize(paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ((MediaControlView2Provider)mProvider).onLayout_impl(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void requestPlayButtonFocus()
  {
    ((MediaControlView2Provider)mProvider).requestPlayButtonFocus_impl();
  }
  
  public void setButtonVisibility(int paramInt1, int paramInt2)
  {
    ((MediaControlView2Provider)mProvider).setButtonVisibility_impl(paramInt1, paramInt2);
  }
  
  public void setController(MediaController paramMediaController)
  {
    ((MediaControlView2Provider)mProvider).setController_impl(paramMediaController);
  }
  
  public void setMediaSessionToken(SessionToken2 paramSessionToken2)
  {
    ((MediaControlView2Provider)mProvider).setMediaSessionToken_impl(paramSessionToken2);
  }
  
  public void setOnFullScreenListener(OnFullScreenListener paramOnFullScreenListener)
  {
    ((MediaControlView2Provider)mProvider).setOnFullScreenListener_impl(paramOnFullScreenListener);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Button {}
  
  public static abstract interface OnFullScreenListener
  {
    public abstract void onFullScreen(View paramView, boolean paramBoolean);
  }
}
