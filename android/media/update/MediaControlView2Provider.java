package android.media.update;

import android.media.SessionToken2;
import android.media.session.MediaController;
import android.util.AttributeSet;
import android.widget.MediaControlView2.OnFullScreenListener;

public abstract interface MediaControlView2Provider
  extends ViewGroupProvider
{
  public abstract void initialize(AttributeSet paramAttributeSet, int paramInt1, int paramInt2);
  
  public abstract void requestPlayButtonFocus_impl();
  
  public abstract void setButtonVisibility_impl(int paramInt1, int paramInt2);
  
  public abstract void setController_impl(MediaController paramMediaController);
  
  public abstract void setMediaSessionToken_impl(SessionToken2 paramSessionToken2);
  
  public abstract void setOnFullScreenListener_impl(MediaControlView2.OnFullScreenListener paramOnFullScreenListener);
}
