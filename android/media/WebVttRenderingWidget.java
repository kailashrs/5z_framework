package android.media;

import android.content.Context;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.view.accessibility.CaptioningManager.CaptioningChangeListener;
import android.widget.LinearLayout;
import com.android.internal.widget.SubtitleView;
import java.util.ArrayList;
import java.util.Vector;

class WebVttRenderingWidget
  extends ViewGroup
  implements SubtitleTrack.RenderingWidget
{
  private static final boolean DEBUG = false;
  private static final int DEBUG_CUE_BACKGROUND = -2130771968;
  private static final int DEBUG_REGION_BACKGROUND = -2147483393;
  private static final CaptioningManager.CaptionStyle DEFAULT_CAPTION_STYLE = CaptioningManager.CaptionStyle.DEFAULT;
  private static final float LINE_HEIGHT_RATIO = 0.0533F;
  private CaptioningManager.CaptionStyle mCaptionStyle;
  private final CaptioningManager.CaptioningChangeListener mCaptioningListener = new CaptioningManager.CaptioningChangeListener()
  {
    public void onFontScaleChanged(float paramAnonymousFloat)
    {
      float f = getHeight();
      WebVttRenderingWidget.this.setCaptionStyle(mCaptionStyle, f * paramAnonymousFloat * 0.0533F);
    }
    
    public void onUserStyleChanged(CaptioningManager.CaptionStyle paramAnonymousCaptionStyle)
    {
      WebVttRenderingWidget.this.setCaptionStyle(paramAnonymousCaptionStyle, mFontSize);
    }
  };
  private final ArrayMap<TextTrackCue, CueLayout> mCueBoxes = new ArrayMap();
  private float mFontSize;
  private boolean mHasChangeListener;
  private SubtitleTrack.RenderingWidget.OnChangedListener mListener;
  private final CaptioningManager mManager;
  private final ArrayMap<TextTrackRegion, RegionLayout> mRegionBoxes = new ArrayMap();
  
  public WebVttRenderingWidget(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public WebVttRenderingWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public WebVttRenderingWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public WebVttRenderingWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setLayerType(1, null);
    mManager = ((CaptioningManager)paramContext.getSystemService("captioning"));
    mCaptionStyle = mManager.getUserStyle();
    mFontSize = (mManager.getFontScale() * getHeight() * 0.0533F);
  }
  
  private int calculateLinePosition(CueLayout paramCueLayout)
  {
    TextTrackCue localTextTrackCue = paramCueLayout.getCue();
    Integer localInteger = mLinePosition;
    boolean bool = mSnapToLines;
    int i;
    if (localInteger == null) {
      i = 1;
    } else {
      i = 0;
    }
    if ((!bool) && (i == 0) && ((localInteger.intValue() < 0) || (localInteger.intValue() > 100))) {
      return 100;
    }
    if (i == 0) {
      return localInteger.intValue();
    }
    if (!bool) {
      return 100;
    }
    return -(mOrder + 1);
  }
  
  private void layoutCue(int paramInt1, int paramInt2, CueLayout paramCueLayout)
  {
    TextTrackCue localTextTrackCue = paramCueLayout.getCue();
    int i = getLayoutDirection();
    int j = resolveCueAlignment(i, mAlignment);
    boolean bool = mSnapToLines;
    int k = paramCueLayout.getMeasuredWidth() * 100 / paramInt1;
    switch (j)
    {
    default: 
      m = mTextPosition - k / 2;
      break;
    case 204: 
      m = mTextPosition - k;
      break;
    case 203: 
      m = mTextPosition;
    }
    j = m;
    if (i == 1) {
      j = 100 - m;
    }
    int n = k;
    int i1 = j;
    if (bool)
    {
      n = getPaddingLeft() * 100 / paramInt1;
      int i2 = getPaddingRight() * 100 / paramInt1;
      i = k;
      m = j;
      if (j < n)
      {
        i = k;
        m = j;
        if (j + k > n)
        {
          m = j + n;
          i = k - n;
        }
      }
      float f = 100 - i2;
      n = i;
      i1 = m;
      if (m < f)
      {
        n = i;
        i1 = m;
        if (m + i > f)
        {
          n = i - i2;
          i1 = m;
        }
      }
    }
    j = i1 * paramInt1 / 100;
    i = n * paramInt1 / 100;
    paramInt1 = calculateLinePosition(paramCueLayout);
    int m = paramCueLayout.getMeasuredHeight();
    if (paramInt1 < 0) {
      paramInt1 = paramInt2 + paramInt1 * m;
    } else {
      paramInt1 = (paramInt2 - m) * paramInt1 / 100;
    }
    paramCueLayout.layout(j, paramInt1, j + i, paramInt1 + m);
  }
  
  private void layoutRegion(int paramInt1, int paramInt2, RegionLayout paramRegionLayout)
  {
    TextTrackRegion localTextTrackRegion = paramRegionLayout.getRegion();
    int i = paramRegionLayout.getMeasuredHeight();
    int j = paramRegionLayout.getMeasuredWidth();
    float f1 = mViewportAnchorPointX;
    float f2 = mViewportAnchorPointY;
    paramInt1 = (int)((paramInt1 - j) * f1 / 100.0F);
    paramInt2 = (int)((paramInt2 - i) * f2 / 100.0F);
    paramRegionLayout.layout(paramInt1, paramInt2, paramInt1 + j, paramInt2 + i);
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
      if (bool)
      {
        mManager.addCaptioningChangeListener(mCaptioningListener);
        setCaptionStyle(mManager.getUserStyle(), mManager.getFontScale() * getHeight() * 0.0533F);
      }
      else
      {
        mManager.removeCaptioningChangeListener(mCaptioningListener);
      }
    }
  }
  
  private void prepForPrune()
  {
    int i = mRegionBoxes.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      ((RegionLayout)mRegionBoxes.valueAt(k)).prepForPrune();
    }
    i = mCueBoxes.size();
    for (k = j; k < i; k++) {
      ((CueLayout)mCueBoxes.valueAt(k)).prepForPrune();
    }
  }
  
  private void prune()
  {
    int i = mRegionBoxes.size();
    int j = 0;
    int k = 0;
    Object localObject;
    int n;
    while (k < i)
    {
      localObject = (RegionLayout)mRegionBoxes.valueAt(k);
      m = k;
      n = i;
      if (((RegionLayout)localObject).prune())
      {
        removeView((View)localObject);
        mRegionBoxes.removeAt(k);
        n = i - 1;
        m = k - 1;
      }
      k = m + 1;
      i = n;
    }
    int m = mCueBoxes.size();
    k = j;
    while (k < m)
    {
      localObject = (CueLayout)mCueBoxes.valueAt(k);
      i = m;
      n = k;
      if (!((CueLayout)localObject).isActive())
      {
        removeView((View)localObject);
        mCueBoxes.removeAt(k);
        i = m - 1;
        n = k - 1;
      }
      k = n + 1;
      m = i;
    }
  }
  
  private static int resolveCueAlignment(int paramInt1, int paramInt2)
  {
    int i = 203;
    switch (paramInt2)
    {
    default: 
      return paramInt2;
    case 202: 
      if (paramInt1 == 0) {
        i = 204;
      }
      return i;
    }
    if (paramInt1 != 0) {
      i = 204;
    }
    return i;
  }
  
  private void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle, float paramFloat)
  {
    paramCaptionStyle = DEFAULT_CAPTION_STYLE.applyStyle(paramCaptionStyle);
    mCaptionStyle = paramCaptionStyle;
    mFontSize = paramFloat;
    int i = mCueBoxes.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      ((CueLayout)mCueBoxes.valueAt(k)).setCaptionStyle(paramCaptionStyle, paramFloat);
    }
    i = mRegionBoxes.size();
    for (k = j; k < i; k++) {
      ((RegionLayout)mRegionBoxes.valueAt(k)).setCaptionStyle(paramCaptionStyle, paramFloat);
    }
  }
  
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
    paramInt3 -= paramInt1;
    paramInt4 -= paramInt2;
    setCaptionStyle(mCaptionStyle, mManager.getFontScale() * 0.0533F * paramInt4);
    int i = mRegionBoxes.size();
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
      layoutRegion(paramInt3, paramInt4, (RegionLayout)mRegionBoxes.valueAt(paramInt1));
    }
    i = mCueBoxes.size();
    for (paramInt1 = paramInt2; paramInt1 < i; paramInt1++) {
      layoutCue(paramInt3, paramInt4, (CueLayout)mCueBoxes.valueAt(paramInt1));
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = mRegionBoxes.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      ((RegionLayout)mRegionBoxes.valueAt(k)).measureForParent(paramInt1, paramInt2);
    }
    i = mCueBoxes.size();
    for (k = j; k < i; k++) {
      ((CueLayout)mCueBoxes.valueAt(k)).measureForParent(paramInt1, paramInt2);
    }
  }
  
  public void setActiveCues(Vector<SubtitleTrack.Cue> paramVector)
  {
    Context localContext = getContext();
    CaptioningManager.CaptionStyle localCaptionStyle = mCaptionStyle;
    float f = mFontSize;
    prepForPrune();
    int i = paramVector.size();
    for (int j = 0; j < i; j++)
    {
      TextTrackCue localTextTrackCue = (TextTrackCue)paramVector.get(j);
      TextTrackRegion localTextTrackRegion = mRegion;
      Object localObject1;
      Object localObject2;
      if (localTextTrackRegion != null)
      {
        localObject1 = (RegionLayout)mRegionBoxes.get(localTextTrackRegion);
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new RegionLayout(localContext, localTextTrackRegion, localCaptionStyle, f);
          mRegionBoxes.put(localTextTrackRegion, localObject2);
          addView((View)localObject2, -2, -2);
        }
        ((RegionLayout)localObject2).put(localTextTrackCue);
      }
      else
      {
        localObject1 = (CueLayout)mCueBoxes.get(localTextTrackCue);
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new CueLayout(localContext, localTextTrackCue, localCaptionStyle, f);
          mCueBoxes.put(localTextTrackCue, localObject2);
          addView((View)localObject2, -2, -2);
        }
        ((CueLayout)localObject2).update();
        ((CueLayout)localObject2).setOrder(j);
      }
    }
    prune();
    setSize(getWidth(), getHeight());
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
    manageChangeListener();
  }
  
  private static class CueLayout
    extends LinearLayout
  {
    private boolean mActive;
    private CaptioningManager.CaptionStyle mCaptionStyle;
    public final TextTrackCue mCue;
    private float mFontSize;
    private int mOrder;
    
    public CueLayout(Context paramContext, TextTrackCue paramTextTrackCue, CaptioningManager.CaptionStyle paramCaptionStyle, float paramFloat)
    {
      super();
      mCue = paramTextTrackCue;
      mCaptionStyle = paramCaptionStyle;
      mFontSize = paramFloat;
      int i = mWritingDirection;
      int j = 0;
      int k = 1;
      if (i == 100) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        j = 1;
      }
      setOrientation(j);
      switch (mAlignment)
      {
      default: 
        break;
      case 204: 
        setGravity(5);
        break;
      case 203: 
        setGravity(3);
        break;
      case 202: 
        setGravity(8388613);
        break;
      case 201: 
        setGravity(8388611);
        break;
      case 200: 
        if (i != 0) {
          i = k;
        } else {
          i = 16;
        }
        setGravity(i);
      }
      update();
    }
    
    public TextTrackCue getCue()
    {
      return mCue;
    }
    
    public boolean isActive()
    {
      return mActive;
    }
    
    public void measureForParent(int paramInt1, int paramInt2)
    {
      TextTrackCue localTextTrackCue = mCue;
      int i = View.MeasureSpec.getSize(paramInt1);
      paramInt2 = View.MeasureSpec.getSize(paramInt2);
      paramInt1 = WebVttRenderingWidget.resolveCueAlignment(getLayoutDirection(), mAlignment);
      if (paramInt1 != 200) {
        switch (paramInt1)
        {
        default: 
          paramInt1 = 0;
          break;
        case 204: 
          paramInt1 = mTextPosition;
          break;
        case 203: 
          paramInt1 = 100 - mTextPosition;
          break;
        }
      } else if (mTextPosition <= 50) {
        paramInt1 = mTextPosition * 2;
      } else {
        paramInt1 = (100 - mTextPosition) * 2;
      }
      measure(View.MeasureSpec.makeMeasureSpec(Math.min(mSize, paramInt1) * i / 100, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(paramInt2, Integer.MIN_VALUE));
    }
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      super.onMeasure(paramInt1, paramInt2);
    }
    
    public void prepForPrune()
    {
      mActive = false;
    }
    
    public void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle, float paramFloat)
    {
      mCaptionStyle = paramCaptionStyle;
      mFontSize = paramFloat;
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        if ((localView instanceof WebVttRenderingWidget.SpanLayout)) {
          ((WebVttRenderingWidget.SpanLayout)localView).setCaptionStyle(paramCaptionStyle, paramFloat);
        }
      }
    }
    
    public void setOrder(int paramInt)
    {
      mOrder = paramInt;
    }
    
    public void update()
    {
      mActive = true;
      removeAllViews();
      Layout.Alignment localAlignment;
      switch (WebVttRenderingWidget.resolveCueAlignment(getLayoutDirection(), mCue.mAlignment))
      {
      default: 
        localAlignment = Layout.Alignment.ALIGN_CENTER;
        break;
      case 204: 
        localAlignment = Layout.Alignment.ALIGN_RIGHT;
        break;
      case 203: 
        localAlignment = Layout.Alignment.ALIGN_LEFT;
      }
      CaptioningManager.CaptionStyle localCaptionStyle = mCaptionStyle;
      float f = mFontSize;
      TextTrackCueSpan[][] arrayOfTextTrackCueSpan = mCue.mLines;
      int i = arrayOfTextTrackCueSpan.length;
      for (int j = 0; j < i; j++)
      {
        WebVttRenderingWidget.SpanLayout localSpanLayout = new WebVttRenderingWidget.SpanLayout(getContext(), arrayOfTextTrackCueSpan[j]);
        localSpanLayout.setAlignment(localAlignment);
        localSpanLayout.setCaptionStyle(localCaptionStyle, f);
        addView(localSpanLayout, -2, -2);
      }
    }
  }
  
  private static class RegionLayout
    extends LinearLayout
  {
    private CaptioningManager.CaptionStyle mCaptionStyle;
    private float mFontSize;
    private final TextTrackRegion mRegion;
    private final ArrayList<WebVttRenderingWidget.CueLayout> mRegionCueBoxes = new ArrayList();
    
    public RegionLayout(Context paramContext, TextTrackRegion paramTextTrackRegion, CaptioningManager.CaptionStyle paramCaptionStyle, float paramFloat)
    {
      super();
      mRegion = paramTextTrackRegion;
      mCaptionStyle = paramCaptionStyle;
      mFontSize = paramFloat;
      setOrientation(1);
      setBackgroundColor(windowColor);
    }
    
    public TextTrackRegion getRegion()
    {
      return mRegion;
    }
    
    public void measureForParent(int paramInt1, int paramInt2)
    {
      TextTrackRegion localTextTrackRegion = mRegion;
      paramInt1 = View.MeasureSpec.getSize(paramInt1);
      paramInt2 = View.MeasureSpec.getSize(paramInt2);
      measure(View.MeasureSpec.makeMeasureSpec((int)mWidth * paramInt1 / 100, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(paramInt2, Integer.MIN_VALUE));
    }
    
    public void prepForPrune()
    {
      int i = mRegionCueBoxes.size();
      for (int j = 0; j < i; j++) {
        ((WebVttRenderingWidget.CueLayout)mRegionCueBoxes.get(j)).prepForPrune();
      }
    }
    
    public boolean prune()
    {
      int i = mRegionCueBoxes.size();
      int j = 0;
      while (j < i)
      {
        WebVttRenderingWidget.CueLayout localCueLayout = (WebVttRenderingWidget.CueLayout)mRegionCueBoxes.get(j);
        int k = i;
        int m = j;
        if (!localCueLayout.isActive())
        {
          mRegionCueBoxes.remove(j);
          removeView(localCueLayout);
          k = i - 1;
          m = j - 1;
        }
        j = m + 1;
        i = k;
      }
      return mRegionCueBoxes.isEmpty();
    }
    
    public void put(TextTrackCue paramTextTrackCue)
    {
      int i = mRegionCueBoxes.size();
      for (int j = 0; j < i; j++)
      {
        WebVttRenderingWidget.CueLayout localCueLayout = (WebVttRenderingWidget.CueLayout)mRegionCueBoxes.get(j);
        if (localCueLayout.getCue() == paramTextTrackCue)
        {
          localCueLayout.update();
          return;
        }
      }
      paramTextTrackCue = new WebVttRenderingWidget.CueLayout(getContext(), paramTextTrackCue, mCaptionStyle, mFontSize);
      mRegionCueBoxes.add(paramTextTrackCue);
      addView(paramTextTrackCue, -2, -2);
      if (getChildCount() > mRegion.mLines) {
        removeViewAt(0);
      }
    }
    
    public void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle, float paramFloat)
    {
      mCaptionStyle = paramCaptionStyle;
      mFontSize = paramFloat;
      int i = mRegionCueBoxes.size();
      for (int j = 0; j < i; j++) {
        ((WebVttRenderingWidget.CueLayout)mRegionCueBoxes.get(j)).setCaptionStyle(paramCaptionStyle, paramFloat);
      }
      setBackgroundColor(windowColor);
    }
  }
  
  private static class SpanLayout
    extends SubtitleView
  {
    private final SpannableStringBuilder mBuilder = new SpannableStringBuilder();
    private final TextTrackCueSpan[] mSpans;
    
    public SpanLayout(Context paramContext, TextTrackCueSpan[] paramArrayOfTextTrackCueSpan)
    {
      super();
      mSpans = paramArrayOfTextTrackCueSpan;
      update();
    }
    
    public void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle, float paramFloat)
    {
      setBackgroundColor(backgroundColor);
      setForegroundColor(foregroundColor);
      setEdgeColor(edgeColor);
      setEdgeType(edgeType);
      setTypeface(paramCaptionStyle.getTypeface());
      setTextSize(paramFloat);
    }
    
    public void update()
    {
      SpannableStringBuilder localSpannableStringBuilder = mBuilder;
      TextTrackCueSpan[] arrayOfTextTrackCueSpan = mSpans;
      localSpannableStringBuilder.clear();
      localSpannableStringBuilder.clearSpans();
      int i = arrayOfTextTrackCueSpan.length;
      for (int j = 0; j < i; j++) {
        if (mEnabled) {
          localSpannableStringBuilder.append(mText);
        }
      }
      setText(localSpannableStringBuilder);
    }
  }
}
