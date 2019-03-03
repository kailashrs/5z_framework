package android.view.accessibility;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public abstract class AccessibilityRequestPreparer
{
  public static final int REQUEST_TYPE_EXTRA_DATA = 1;
  private final int mRequestTypes;
  private final WeakReference<View> mViewRef;
  
  public AccessibilityRequestPreparer(View paramView, int paramInt)
  {
    if (paramView.isAttachedToWindow())
    {
      mViewRef = new WeakReference(paramView);
      mRequestTypes = paramInt;
      paramView.addOnAttachStateChangeListener(new ViewAttachStateListener(null));
      return;
    }
    throw new IllegalStateException("View must be attached to a window");
  }
  
  public View getView()
  {
    return (View)mViewRef.get();
  }
  
  public abstract void onPrepareExtraData(int paramInt, String paramString, Bundle paramBundle, Message paramMessage);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RequestTypes {}
  
  private class ViewAttachStateListener
    implements View.OnAttachStateChangeListener
  {
    private ViewAttachStateListener() {}
    
    public void onViewAttachedToWindow(View paramView) {}
    
    public void onViewDetachedFromWindow(View paramView)
    {
      Context localContext = paramView.getContext();
      if (localContext != null) {
        ((AccessibilityManager)localContext.getSystemService(AccessibilityManager.class)).removeAccessibilityRequestPreparer(AccessibilityRequestPreparer.this);
      }
      paramView.removeOnAttachStateChangeListener(this);
    }
  }
}
