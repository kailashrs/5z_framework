package android.media;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Vector;

class TtmlRenderingWidget
  extends LinearLayout
  implements SubtitleTrack.RenderingWidget
{
  private SubtitleTrack.RenderingWidget.OnChangedListener mListener;
  private final TextView mTextView;
  
  public TtmlRenderingWidget(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public TtmlRenderingWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TtmlRenderingWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TtmlRenderingWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setLayerType(1, null);
    paramAttributeSet = (CaptioningManager)paramContext.getSystemService("captioning");
    mTextView = new TextView(paramContext);
    mTextView.setTextColor(getUserStyleforegroundColor);
    addView(mTextView, -1, -1);
    mTextView.setGravity(81);
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
  }
  
  public void setActiveCues(Vector<SubtitleTrack.Cue> paramVector)
  {
    int i = paramVector.size();
    String str = "";
    for (int j = 0; j < i; j++)
    {
      TtmlCue localTtmlCue = (TtmlCue)paramVector.get(j);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str);
      localStringBuilder.append(mText);
      localStringBuilder.append("\n");
      str = localStringBuilder.toString();
    }
    mTextView.setText(str);
    if (mListener != null) {
      mListener.onChanged(this);
    }
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
  }
}
