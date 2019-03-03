package android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R.styleable;
import java.lang.ref.WeakReference;

@RemoteViews.RemoteView
public final class ViewStub
  extends View
{
  private OnInflateListener mInflateListener;
  private int mInflatedId;
  private WeakReference<View> mInflatedViewRef;
  private LayoutInflater mInflater;
  private int mLayoutResource;
  
  public ViewStub(Context paramContext)
  {
    this(paramContext, 0);
  }
  
  public ViewStub(Context paramContext, int paramInt)
  {
    this(paramContext, null);
    mLayoutResource = paramInt;
  }
  
  public ViewStub(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ViewStub(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ViewStub(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ViewStub, paramInt1, paramInt2);
    mInflatedId = paramContext.getResourceId(2, -1);
    mLayoutResource = paramContext.getResourceId(1, 0);
    mID = paramContext.getResourceId(0, -1);
    paramContext.recycle();
    setVisibility(8);
    setWillNotDraw(true);
  }
  
  private View inflateViewNoAdd(ViewGroup paramViewGroup)
  {
    LayoutInflater localLayoutInflater;
    if (mInflater != null) {
      localLayoutInflater = mInflater;
    } else {
      localLayoutInflater = LayoutInflater.from(mContext);
    }
    paramViewGroup = localLayoutInflater.inflate(mLayoutResource, paramViewGroup, false);
    if (mInflatedId != -1) {
      paramViewGroup.setId(mInflatedId);
    }
    return paramViewGroup;
  }
  
  private void replaceSelfWithView(View paramView, ViewGroup paramViewGroup)
  {
    int i = paramViewGroup.indexOfChild(this);
    paramViewGroup.removeViewInLayout(this);
    ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
    if (localLayoutParams != null) {
      paramViewGroup.addView(paramView, i, localLayoutParams);
    } else {
      paramViewGroup.addView(paramView, i);
    }
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {}
  
  public void draw(Canvas paramCanvas) {}
  
  public int getInflatedId()
  {
    return mInflatedId;
  }
  
  public LayoutInflater getLayoutInflater()
  {
    return mInflater;
  }
  
  public int getLayoutResource()
  {
    return mLayoutResource;
  }
  
  public View inflate()
  {
    Object localObject = getParent();
    if ((localObject != null) && ((localObject instanceof ViewGroup)))
    {
      if (mLayoutResource != 0)
      {
        ViewGroup localViewGroup = (ViewGroup)localObject;
        localObject = inflateViewNoAdd(localViewGroup);
        replaceSelfWithView((View)localObject, localViewGroup);
        mInflatedViewRef = new WeakReference(localObject);
        if (mInflateListener != null) {
          mInflateListener.onInflate(this, (View)localObject);
        }
        return localObject;
      }
      throw new IllegalArgumentException("ViewStub must have a valid layoutResource");
    }
    throw new IllegalStateException("ViewStub must have a non-null ViewGroup viewParent");
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(0, 0);
  }
  
  @RemotableViewMethod(asyncImpl="setInflatedIdAsync")
  public void setInflatedId(int paramInt)
  {
    mInflatedId = paramInt;
  }
  
  public Runnable setInflatedIdAsync(int paramInt)
  {
    mInflatedId = paramInt;
    return null;
  }
  
  public void setLayoutInflater(LayoutInflater paramLayoutInflater)
  {
    mInflater = paramLayoutInflater;
  }
  
  @RemotableViewMethod(asyncImpl="setLayoutResourceAsync")
  public void setLayoutResource(int paramInt)
  {
    mLayoutResource = paramInt;
  }
  
  public Runnable setLayoutResourceAsync(int paramInt)
  {
    mLayoutResource = paramInt;
    return null;
  }
  
  public void setOnInflateListener(OnInflateListener paramOnInflateListener)
  {
    mInflateListener = paramOnInflateListener;
  }
  
  @RemotableViewMethod(asyncImpl="setVisibilityAsync")
  public void setVisibility(int paramInt)
  {
    if (mInflatedViewRef != null)
    {
      View localView = (View)mInflatedViewRef.get();
      if (localView != null) {
        localView.setVisibility(paramInt);
      } else {
        throw new IllegalStateException("setVisibility called on un-referenced view");
      }
    }
    else
    {
      super.setVisibility(paramInt);
      if ((paramInt == 0) || (paramInt == 4)) {
        inflate();
      }
    }
  }
  
  public Runnable setVisibilityAsync(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 4)) {
      return null;
    }
    return new ViewReplaceRunnable(inflateViewNoAdd((ViewGroup)getParent()));
  }
  
  public static abstract interface OnInflateListener
  {
    public abstract void onInflate(ViewStub paramViewStub, View paramView);
  }
  
  public class ViewReplaceRunnable
    implements Runnable
  {
    public final View view;
    
    ViewReplaceRunnable(View paramView)
    {
      view = paramView;
    }
    
    public void run()
    {
      ViewStub.this.replaceSelfWithView(view, (ViewGroup)getParent());
    }
  }
}
