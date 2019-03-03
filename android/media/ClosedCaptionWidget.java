package android.media;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.view.accessibility.CaptioningManager.CaptioningChangeListener;

abstract class ClosedCaptionWidget
  extends ViewGroup
  implements SubtitleTrack.RenderingWidget
{
  private static final CaptioningManager.CaptionStyle DEFAULT_CAPTION_STYLE = CaptioningManager.CaptionStyle.DEFAULT;
  protected CaptioningManager.CaptionStyle mCaptionStyle;
  private final CaptioningManager.CaptioningChangeListener mCaptioningListener = new CaptioningManager.CaptioningChangeListener()
  {
    public void onFontScaleChanged(float paramAnonymousFloat)
    {
      mClosedCaptionLayout.setFontScale(paramAnonymousFloat);
    }
    
    public void onUserStyleChanged(CaptioningManager.CaptionStyle paramAnonymousCaptionStyle)
    {
      mCaptionStyle = ClosedCaptionWidget.DEFAULT_CAPTION_STYLE.applyStyle(paramAnonymousCaptionStyle);
      mClosedCaptionLayout.setCaptionStyle(mCaptionStyle);
    }
  };
  protected ClosedCaptionLayout mClosedCaptionLayout;
  private boolean mHasChangeListener;
  protected SubtitleTrack.RenderingWidget.OnChangedListener mListener;
  private final CaptioningManager mManager;
  
  public ClosedCaptionWidget(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ClosedCaptionWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ClosedCaptionWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ClosedCaptionWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setLayerType(1, null);
    mManager = ((CaptioningManager)paramContext.getSystemService("captioning"));
    mCaptionStyle = DEFAULT_CAPTION_STYLE.applyStyle(mManager.getUserStyle());
    mClosedCaptionLayout = createCaptionLayout(paramContext);
    mClosedCaptionLayout.setCaptionStyle(mCaptionStyle);
    mClosedCaptionLayout.setFontScale(mManager.getFontScale());
    addView((ViewGroup)mClosedCaptionLayout, -1, -1);
    requestLayout();
  }
  
  private void manageChangeListener()
  {
    boolean bool;
    if ((isAttachedToWindow()) && (getVisibility() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    if (mHasChangeListener != bool)
    {
      mHasChangeListener = bool;
      if (bool) {
        mManager.addCaptioningChangeListener(mCaptioningListener);
      } else {
        mManager.removeCaptioningChangeListener(mCaptioningListener);
      }
    }
  }
  
  public abstract ClosedCaptionLayout createCaptionLayout(Context paramContext);
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    manageChangeListener();
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    manageChangeListener();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ((ViewGroup)mClosedCaptionLayout).layout(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    ((ViewGroup)mClosedCaptionLayout).measure(paramInt1, paramInt2);
  }
  
  public void setOnChangedListener(SubtitleTrack.RenderingWidget.OnChangedListener paramOnChangedListener)
  {
    mListener = paramOnChangedListener;
  }
  
  public void setSize(int paramInt1, int paramInt2)
  {
    measure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824));
    layout(0, 0, paramInt1, paramInt2);
  }
  
  public void setVisible(boolean paramBoolean)
  {
    if (paramBoolean) {
      setVisibility(0);
    } else {
      setVisibility(8);
    }
    manageChangeListener();
  }
  
  static abstract interface ClosedCaptionLayout
  {
    public abstract void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle);
    
    public abstract void setFontScale(float paramFloat);
  }
}
