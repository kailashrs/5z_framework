package android.media;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.android.internal.widget.SubtitleView;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

class Cea708CCWidget
  extends ClosedCaptionWidget
  implements Cea708CCParser.DisplayListener
{
  private final CCHandler mCCHandler = new CCHandler((CCLayout)mClosedCaptionLayout);
  
  public Cea708CCWidget(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Cea708CCWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public Cea708CCWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Cea708CCWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public ClosedCaptionWidget.ClosedCaptionLayout createCaptionLayout(Context paramContext)
  {
    return new CCLayout(paramContext);
  }
  
  public void emitEvent(Cea708CCParser.CaptionEvent paramCaptionEvent)
  {
    mCCHandler.processCaptionEvent(paramCaptionEvent);
    setSize(getWidth(), getHeight());
    if (mListener != null) {
      mListener.onChanged(this);
    }
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    ((ViewGroup)mClosedCaptionLayout).draw(paramCanvas);
  }
  
  static class CCHandler
    implements Handler.Callback
  {
    private static final int CAPTION_ALL_WINDOWS_BITMAP = 255;
    private static final long CAPTION_CLEAR_INTERVAL_MS = 60000L;
    private static final int CAPTION_WINDOWS_MAX = 8;
    private static final boolean DEBUG = false;
    private static final int MSG_CAPTION_CLEAR = 2;
    private static final int MSG_DELAY_CANCEL = 1;
    private static final String TAG = "CCHandler";
    private static final int TENTHS_OF_SECOND_IN_MILLIS = 100;
    private final Cea708CCWidget.CCLayout mCCLayout;
    private final Cea708CCWidget.CCWindowLayout[] mCaptionWindowLayouts = new Cea708CCWidget.CCWindowLayout[8];
    private Cea708CCWidget.CCWindowLayout mCurrentWindowLayout;
    private final Handler mHandler;
    private boolean mIsDelayed = false;
    private final ArrayList<Cea708CCParser.CaptionEvent> mPendingCaptionEvents = new ArrayList();
    
    public CCHandler(Cea708CCWidget.CCLayout paramCCLayout)
    {
      mCCLayout = paramCCLayout;
      mHandler = new Handler(this);
    }
    
    private void clearWindows(int paramInt)
    {
      if (paramInt == 0) {
        return;
      }
      Iterator localIterator = getWindowsFromBitmap(paramInt).iterator();
      while (localIterator.hasNext()) {
        ((Cea708CCWidget.CCWindowLayout)localIterator.next()).clear();
      }
    }
    
    private void defineWindow(Cea708CCParser.CaptionWindow paramCaptionWindow)
    {
      if (paramCaptionWindow == null) {
        return;
      }
      int i = id;
      if ((i >= 0) && (i < mCaptionWindowLayouts.length))
      {
        Cea708CCWidget.CCWindowLayout localCCWindowLayout1 = mCaptionWindowLayouts[i];
        Cea708CCWidget.CCWindowLayout localCCWindowLayout2 = localCCWindowLayout1;
        if (localCCWindowLayout1 == null) {
          localCCWindowLayout2 = new Cea708CCWidget.CCWindowLayout(mCCLayout.getContext());
        }
        localCCWindowLayout2.initWindow(mCCLayout, paramCaptionWindow);
        mCaptionWindowLayouts[i] = localCCWindowLayout2;
        mCurrentWindowLayout = localCCWindowLayout2;
        return;
      }
    }
    
    private void delay(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 255))
      {
        mIsDelayed = true;
        mHandler.sendMessageDelayed(mHandler.obtainMessage(1), paramInt * 100);
        return;
      }
    }
    
    private void delayCancel()
    {
      mIsDelayed = false;
      processPendingBuffer();
    }
    
    private void deleteWindows(int paramInt)
    {
      if (paramInt == 0) {
        return;
      }
      Iterator localIterator = getWindowsFromBitmap(paramInt).iterator();
      while (localIterator.hasNext())
      {
        Cea708CCWidget.CCWindowLayout localCCWindowLayout = (Cea708CCWidget.CCWindowLayout)localIterator.next();
        localCCWindowLayout.removeFromCaptionView();
        mCaptionWindowLayouts[localCCWindowLayout.getCaptionWindowId()] = null;
      }
    }
    
    private void displayWindows(int paramInt)
    {
      if (paramInt == 0) {
        return;
      }
      Iterator localIterator = getWindowsFromBitmap(paramInt).iterator();
      while (localIterator.hasNext()) {
        ((Cea708CCWidget.CCWindowLayout)localIterator.next()).show();
      }
    }
    
    private ArrayList<Cea708CCWidget.CCWindowLayout> getWindowsFromBitmap(int paramInt)
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < 8; i++) {
        if ((1 << i & paramInt) != 0)
        {
          Cea708CCWidget.CCWindowLayout localCCWindowLayout = mCaptionWindowLayouts[i];
          if (localCCWindowLayout != null) {
            localArrayList.add(localCCWindowLayout);
          }
        }
      }
      return localArrayList;
    }
    
    private void hideWindows(int paramInt)
    {
      if (paramInt == 0) {
        return;
      }
      Iterator localIterator = getWindowsFromBitmap(paramInt).iterator();
      while (localIterator.hasNext()) {
        ((Cea708CCWidget.CCWindowLayout)localIterator.next()).hide();
      }
    }
    
    private void processPendingBuffer()
    {
      Iterator localIterator = mPendingCaptionEvents.iterator();
      while (localIterator.hasNext()) {
        processCaptionEvent((Cea708CCParser.CaptionEvent)localIterator.next());
      }
      mPendingCaptionEvents.clear();
    }
    
    private void sendBufferToCurrentWindow(String paramString)
    {
      if (mCurrentWindowLayout != null)
      {
        mCurrentWindowLayout.sendBuffer(paramString);
        mHandler.removeMessages(2);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(2), 60000L);
      }
    }
    
    private void sendControlToCurrentWindow(char paramChar)
    {
      if (mCurrentWindowLayout != null) {
        mCurrentWindowLayout.sendControl(paramChar);
      }
    }
    
    private void setCurrentWindowLayout(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < mCaptionWindowLayouts.length))
      {
        Cea708CCWidget.CCWindowLayout localCCWindowLayout = mCaptionWindowLayouts[paramInt];
        if (localCCWindowLayout == null) {
          return;
        }
        mCurrentWindowLayout = localCCWindowLayout;
        return;
      }
    }
    
    private void setPenAttr(Cea708CCParser.CaptionPenAttr paramCaptionPenAttr)
    {
      if (mCurrentWindowLayout != null) {
        mCurrentWindowLayout.setPenAttr(paramCaptionPenAttr);
      }
    }
    
    private void setPenColor(Cea708CCParser.CaptionPenColor paramCaptionPenColor)
    {
      if (mCurrentWindowLayout != null) {
        mCurrentWindowLayout.setPenColor(paramCaptionPenColor);
      }
    }
    
    private void setPenLocation(Cea708CCParser.CaptionPenLocation paramCaptionPenLocation)
    {
      if (mCurrentWindowLayout != null) {
        mCurrentWindowLayout.setPenLocation(row, column);
      }
    }
    
    private void setWindowAttr(Cea708CCParser.CaptionWindowAttr paramCaptionWindowAttr)
    {
      if (mCurrentWindowLayout != null) {
        mCurrentWindowLayout.setWindowAttr(paramCaptionWindowAttr);
      }
    }
    
    private void toggleWindows(int paramInt)
    {
      if (paramInt == 0) {
        return;
      }
      Iterator localIterator = getWindowsFromBitmap(paramInt).iterator();
      while (localIterator.hasNext())
      {
        Cea708CCWidget.CCWindowLayout localCCWindowLayout = (Cea708CCWidget.CCWindowLayout)localIterator.next();
        if (localCCWindowLayout.isShown()) {
          localCCWindowLayout.hide();
        } else {
          localCCWindowLayout.show();
        }
      }
    }
    
    public boolean handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        return false;
      case 2: 
        clearWindows(255);
        return true;
      }
      delayCancel();
      return true;
    }
    
    public void processCaptionEvent(Cea708CCParser.CaptionEvent paramCaptionEvent)
    {
      if (mIsDelayed)
      {
        mPendingCaptionEvents.add(paramCaptionEvent);
        return;
      }
      switch (type)
      {
      default: 
        break;
      case 16: 
        defineWindow((Cea708CCParser.CaptionWindow)obj);
        break;
      case 15: 
        setWindowAttr((Cea708CCParser.CaptionWindowAttr)obj);
        break;
      case 14: 
        setPenLocation((Cea708CCParser.CaptionPenLocation)obj);
        break;
      case 13: 
        setPenColor((Cea708CCParser.CaptionPenColor)obj);
        break;
      case 12: 
        setPenAttr((Cea708CCParser.CaptionPenAttr)obj);
        break;
      case 11: 
        reset();
        break;
      case 10: 
        delayCancel();
        break;
      case 9: 
        delay(((Integer)obj).intValue());
        break;
      case 8: 
        deleteWindows(((Integer)obj).intValue());
        break;
      case 7: 
        toggleWindows(((Integer)obj).intValue());
        break;
      case 6: 
        hideWindows(((Integer)obj).intValue());
        break;
      case 5: 
        displayWindows(((Integer)obj).intValue());
        break;
      case 4: 
        clearWindows(((Integer)obj).intValue());
        break;
      case 3: 
        setCurrentWindowLayout(((Integer)obj).intValue());
        break;
      case 2: 
        sendControlToCurrentWindow(((Character)obj).charValue());
        break;
      case 1: 
        sendBufferToCurrentWindow((String)obj);
      }
    }
    
    public void reset()
    {
      mCurrentWindowLayout = null;
      int i = 0;
      mIsDelayed = false;
      mPendingCaptionEvents.clear();
      while (i < 8)
      {
        if (mCaptionWindowLayouts[i] != null) {
          mCaptionWindowLayouts[i].removeFromCaptionView();
        }
        mCaptionWindowLayouts[i] = null;
        i++;
      }
      mCCLayout.setVisibility(4);
      mHandler.removeMessages(2);
    }
  }
  
  static class CCLayout
    extends Cea708CCWidget.ScaledLayout
    implements ClosedCaptionWidget.ClosedCaptionLayout
  {
    private static final float SAFE_TITLE_AREA_SCALE_END_X = 0.9F;
    private static final float SAFE_TITLE_AREA_SCALE_END_Y = 0.9F;
    private static final float SAFE_TITLE_AREA_SCALE_START_X = 0.1F;
    private static final float SAFE_TITLE_AREA_SCALE_START_Y = 0.1F;
    private final Cea708CCWidget.ScaledLayout mSafeTitleAreaLayout;
    
    public CCLayout(Context paramContext)
    {
      super();
      mSafeTitleAreaLayout = new Cea708CCWidget.ScaledLayout(paramContext);
      addView(mSafeTitleAreaLayout, new Cea708CCWidget.ScaledLayout.ScaledLayoutParams(0.1F, 0.9F, 0.1F, 0.9F));
    }
    
    public void addOrUpdateViewToSafeTitleArea(Cea708CCWidget.CCWindowLayout paramCCWindowLayout, Cea708CCWidget.ScaledLayout.ScaledLayoutParams paramScaledLayoutParams)
    {
      if (mSafeTitleAreaLayout.indexOfChild(paramCCWindowLayout) < 0)
      {
        mSafeTitleAreaLayout.addView(paramCCWindowLayout, paramScaledLayoutParams);
        return;
      }
      mSafeTitleAreaLayout.updateViewLayout(paramCCWindowLayout, paramScaledLayoutParams);
    }
    
    public void removeViewFromSafeTitleArea(Cea708CCWidget.CCWindowLayout paramCCWindowLayout)
    {
      mSafeTitleAreaLayout.removeView(paramCCWindowLayout);
    }
    
    public void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle)
    {
      int i = mSafeTitleAreaLayout.getChildCount();
      for (int j = 0; j < i; j++) {
        ((Cea708CCWidget.CCWindowLayout)mSafeTitleAreaLayout.getChildAt(j)).setCaptionStyle(paramCaptionStyle);
      }
    }
    
    public void setFontScale(float paramFloat)
    {
      int i = mSafeTitleAreaLayout.getChildCount();
      for (int j = 0; j < i; j++) {
        ((Cea708CCWidget.CCWindowLayout)mSafeTitleAreaLayout.getChildAt(j)).setFontScale(paramFloat);
      }
    }
  }
  
  static class CCView
    extends SubtitleView
  {
    private static final CaptioningManager.CaptionStyle DEFAULT_CAPTION_STYLE = CaptioningManager.CaptionStyle.DEFAULT;
    
    public CCView(Context paramContext)
    {
      this(paramContext, null);
    }
    
    public CCView(Context paramContext, AttributeSet paramAttributeSet)
    {
      this(paramContext, paramAttributeSet, 0);
    }
    
    public CCView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
      this(paramContext, paramAttributeSet, paramInt, 0);
    }
    
    public CCView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
      super(paramAttributeSet, paramInt1, paramInt2);
    }
    
    public void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle)
    {
      int i;
      if (paramCaptionStyle.hasForegroundColor()) {
        i = foregroundColor;
      } else {
        i = DEFAULT_CAPTION_STYLEforegroundColor;
      }
      setForegroundColor(i);
      if (paramCaptionStyle.hasBackgroundColor()) {
        i = backgroundColor;
      } else {
        i = DEFAULT_CAPTION_STYLEbackgroundColor;
      }
      setBackgroundColor(i);
      if (paramCaptionStyle.hasEdgeType()) {
        i = edgeType;
      } else {
        i = DEFAULT_CAPTION_STYLEedgeType;
      }
      setEdgeType(i);
      if (paramCaptionStyle.hasEdgeColor()) {
        i = edgeColor;
      } else {
        i = DEFAULT_CAPTION_STYLEedgeColor;
      }
      setEdgeColor(i);
      setTypeface(paramCaptionStyle.getTypeface());
    }
  }
  
  static class CCWindowLayout
    extends RelativeLayout
    implements View.OnLayoutChangeListener
  {
    private static final int ANCHOR_HORIZONTAL_16_9_MAX = 209;
    private static final int ANCHOR_HORIZONTAL_MODE_CENTER = 1;
    private static final int ANCHOR_HORIZONTAL_MODE_LEFT = 0;
    private static final int ANCHOR_HORIZONTAL_MODE_RIGHT = 2;
    private static final int ANCHOR_MODE_DIVIDER = 3;
    private static final int ANCHOR_RELATIVE_POSITIONING_MAX = 99;
    private static final int ANCHOR_VERTICAL_MAX = 74;
    private static final int ANCHOR_VERTICAL_MODE_BOTTOM = 2;
    private static final int ANCHOR_VERTICAL_MODE_CENTER = 1;
    private static final int ANCHOR_VERTICAL_MODE_TOP = 0;
    private static final int MAX_COLUMN_COUNT_16_9 = 42;
    private static final float PROPORTION_PEN_SIZE_LARGE = 1.25F;
    private static final float PROPORTION_PEN_SIZE_SMALL = 0.75F;
    private static final String TAG = "CCWindowLayout";
    private final SpannableStringBuilder mBuilder = new SpannableStringBuilder();
    private Cea708CCWidget.CCLayout mCCLayout;
    private Cea708CCWidget.CCView mCCView;
    private CaptioningManager.CaptionStyle mCaptionStyle;
    private int mCaptionWindowId;
    private final List<CharacterStyle> mCharacterStyles = new ArrayList();
    private float mFontScale;
    private int mLastCaptionLayoutHeight;
    private int mLastCaptionLayoutWidth;
    private int mRow = -1;
    private int mRowLimit = 0;
    private float mTextSize;
    private String mWidestChar;
    
    public CCWindowLayout(Context paramContext)
    {
      this(paramContext, null);
    }
    
    public CCWindowLayout(Context paramContext, AttributeSet paramAttributeSet)
    {
      this(paramContext, paramAttributeSet, 0);
    }
    
    public CCWindowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
      this(paramContext, paramAttributeSet, paramInt, 0);
    }
    
    public CCWindowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
      super(paramAttributeSet, paramInt1, paramInt2);
      mCCView = new Cea708CCWidget.CCView(paramContext);
      paramAttributeSet = new RelativeLayout.LayoutParams(-2, -2);
      addView(mCCView, paramAttributeSet);
      paramContext = (CaptioningManager)paramContext.getSystemService("captioning");
      mFontScale = paramContext.getFontScale();
      setCaptionStyle(paramContext.getUserStyle());
      mCCView.setText("");
      updateWidestChar();
    }
    
    private int getScreenColumnCount()
    {
      return 42;
    }
    
    private void updateText(String paramString, boolean paramBoolean)
    {
      if (!paramBoolean) {
        mBuilder.clear();
      }
      if ((paramString != null) && (paramString.length() > 0))
      {
        i = mBuilder.length();
        mBuilder.append(paramString);
        paramString = mCharacterStyles.iterator();
        while (paramString.hasNext())
        {
          CharacterStyle localCharacterStyle = (CharacterStyle)paramString.next();
          mBuilder.setSpan(localCharacterStyle, i, mBuilder.length(), 33);
        }
      }
      paramString = TextUtils.split(mBuilder.toString(), "\n");
      paramString = TextUtils.join("\n", Arrays.copyOfRange(paramString, Math.max(0, paramString.length - (mRowLimit + 1)), paramString.length));
      mBuilder.delete(0, mBuilder.length() - paramString.length());
      int j = mBuilder.length() - 1;
      int k = 0;
      int i = j;
      int m;
      for (;;)
      {
        m = i;
        if (k > i) {
          break;
        }
        m = i;
        if (mBuilder.charAt(k) > ' ') {
          break;
        }
        k++;
      }
      while ((m >= k) && (mBuilder.charAt(m) <= ' ')) {
        m--;
      }
      if ((k == 0) && (m == j))
      {
        mCCView.setText(mBuilder);
      }
      else
      {
        paramString = new SpannableStringBuilder();
        paramString.append(mBuilder);
        if (m < j) {
          paramString.delete(m + 1, j + 1);
        }
        if (k > 0) {
          paramString.delete(0, k);
        }
        mCCView.setText(paramString);
      }
    }
    
    private void updateTextSize()
    {
      if (mCCLayout == null) {
        return;
      }
      Object localObject = new StringBuilder();
      int i = getScreenColumnCount();
      for (int j = 0; j < i; j++) {
        ((StringBuilder)localObject).append(mWidestChar);
      }
      localObject = ((StringBuilder)localObject).toString();
      Paint localPaint = new Paint();
      localPaint.setTypeface(mCaptionStyle.getTypeface());
      float f1 = 0.0F;
      float f2 = 255.0F;
      while (f1 < f2)
      {
        float f3 = (f1 + f2) / 2.0F;
        localPaint.setTextSize(f3);
        float f4 = localPaint.measureText((String)localObject);
        if (mCCLayout.getWidth() * 0.8F > f4) {
          f1 = 0.01F + f3;
        } else {
          f2 = f3 - 0.01F;
        }
      }
      mTextSize = (mFontScale * f2);
      mCCView.setTextSize(mTextSize);
    }
    
    private void updateWidestChar()
    {
      Paint localPaint = new Paint();
      localPaint.setTypeface(mCaptionStyle.getTypeface());
      Charset localCharset = Charset.forName("ISO-8859-1");
      float f1 = 0.0F;
      int i = 0;
      while (i < 256)
      {
        String str = new String(new byte[] { (byte)i }, localCharset);
        float f2 = localPaint.measureText(str);
        float f3 = f1;
        if (f1 < f2)
        {
          f3 = f2;
          mWidestChar = str;
        }
        i++;
        f1 = f3;
      }
      updateTextSize();
    }
    
    public void appendText(String paramString)
    {
      updateText(paramString, true);
    }
    
    public void clear()
    {
      clearText();
      hide();
    }
    
    public void clearText()
    {
      mBuilder.clear();
      mCCView.setText("");
    }
    
    public int getCaptionWindowId()
    {
      return mCaptionWindowId;
    }
    
    public void hide()
    {
      setVisibility(4);
      requestLayout();
    }
    
    public void initWindow(Cea708CCWidget.CCLayout paramCCLayout, Cea708CCParser.CaptionWindow paramCaptionWindow)
    {
      if (mCCLayout != paramCCLayout)
      {
        if (mCCLayout != null) {
          mCCLayout.removeOnLayoutChangeListener(this);
        }
        mCCLayout = paramCCLayout;
        mCCLayout.addOnLayoutChangeListener(this);
        updateWidestChar();
      }
      float f1 = anchorVertical;
      boolean bool = relativePositioning;
      int i = 99;
      if (bool) {
        j = 99;
      } else {
        j = 74;
      }
      float f2 = f1 / j;
      f1 = anchorHorizontal;
      if (relativePositioning) {
        j = i;
      } else {
        j = 209;
      }
      float f3 = f1 / j;
      if (f2 >= 0.0F)
      {
        f1 = f2;
        if (f2 <= 1.0F) {}
      }
      else
      {
        paramCCLayout = new StringBuilder();
        paramCCLayout.append("The vertical position of the anchor point should be at the range of 0 and 1 but ");
        paramCCLayout.append(f2);
        Log.i("CCWindowLayout", paramCCLayout.toString());
        f1 = Math.max(0.0F, Math.min(f2, 1.0F));
      }
      if (f3 >= 0.0F)
      {
        f2 = f3;
        if (f3 <= 1.0F) {}
      }
      else
      {
        paramCCLayout = new StringBuilder();
        paramCCLayout.append("The horizontal position of the anchor point should be at the range of 0 and 1 but ");
        paramCCLayout.append(f3);
        Log.i("CCWindowLayout", paramCCLayout.toString());
        f2 = Math.max(0.0F, Math.min(f3, 1.0F));
      }
      int j = 17;
      int k = anchorId;
      i = anchorId / 3;
      float f4 = 0.0F;
      float f5 = 1.0F;
      float f6 = 0.0F;
      f3 = 1.0F;
      switch (k % 3)
      {
      default: 
        f2 = f6;
        break;
      case 2: 
        j = 5;
        mCCView.setAlignment(Layout.Alignment.ALIGN_RIGHT);
        f3 = f2;
        f2 = f6;
        break;
      case 1: 
        f6 = Math.min(1.0F - f2, f2);
        j = columnCount;
        k = Math.min(getScreenColumnCount(), j + 1);
        StringBuilder localStringBuilder = new StringBuilder();
        for (j = 0; j < k; j++) {
          localStringBuilder.append(mWidestChar);
        }
        paramCCLayout = new Paint();
        paramCCLayout.setTypeface(mCaptionStyle.getTypeface());
        paramCCLayout.setTextSize(mTextSize);
        f3 = paramCCLayout.measureText(localStringBuilder.toString());
        if (mCCLayout.getWidth() > 0) {
          f3 = f3 / 2.0F / (mCCLayout.getWidth() * 0.8F);
        } else {
          f3 = 0.0F;
        }
        if ((f3 > 0.0F) && (f3 < f2))
        {
          mCCView.setAlignment(Layout.Alignment.ALIGN_NORMAL);
          f2 -= f3;
          f3 = 1.0F;
          j = 3;
        }
        else
        {
          j = 1;
          mCCView.setAlignment(Layout.Alignment.ALIGN_CENTER);
          f3 = f2 - f6;
          f6 = f2 + f6;
          f2 = f3;
          f3 = f6;
        }
        break;
      case 0: 
        j = 3;
        mCCView.setAlignment(Layout.Alignment.ALIGN_NORMAL);
      }
      switch (i)
      {
      default: 
        f1 = f4;
        break;
      case 2: 
        j |= 0x50;
        f5 = f1;
        f1 = f4;
        break;
      case 1: 
        j |= 0x10;
        f4 = Math.min(1.0F - f1, f1);
        f5 = f1 - f4;
        f4 = f1 + f4;
        f1 = f5;
        f5 = f4;
        break;
      case 0: 
        j |= 0x30;
      }
      mCCLayout.addOrUpdateViewToSafeTitleArea(this, new Cea708CCWidget.ScaledLayout.ScaledLayoutParams(f1, f5, f2, f3));
      setCaptionWindowId(id);
      setRowLimit(rowCount);
      setGravity(j);
      if (visible) {
        show();
      } else {
        hide();
      }
    }
    
    public void onLayoutChange(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
    {
      paramInt1 = paramInt3 - paramInt1;
      paramInt2 = paramInt4 - paramInt2;
      if ((paramInt1 != mLastCaptionLayoutWidth) || (paramInt2 != mLastCaptionLayoutHeight))
      {
        mLastCaptionLayoutWidth = paramInt1;
        mLastCaptionLayoutHeight = paramInt2;
        updateTextSize();
      }
    }
    
    public void removeFromCaptionView()
    {
      if (mCCLayout != null)
      {
        mCCLayout.removeViewFromSafeTitleArea(this);
        mCCLayout.removeOnLayoutChangeListener(this);
        mCCLayout = null;
      }
    }
    
    public void sendBuffer(String paramString)
    {
      appendText(paramString);
    }
    
    public void sendControl(char paramChar) {}
    
    public void setCaptionStyle(CaptioningManager.CaptionStyle paramCaptionStyle)
    {
      mCaptionStyle = paramCaptionStyle;
      mCCView.setCaptionStyle(paramCaptionStyle);
    }
    
    public void setCaptionWindowId(int paramInt)
    {
      mCaptionWindowId = paramInt;
    }
    
    public void setFontScale(float paramFloat)
    {
      mFontScale = paramFloat;
      updateTextSize();
    }
    
    public void setPenAttr(Cea708CCParser.CaptionPenAttr paramCaptionPenAttr)
    {
      mCharacterStyles.clear();
      if (italic) {
        mCharacterStyles.add(new StyleSpan(2));
      }
      if (underline) {
        mCharacterStyles.add(new UnderlineSpan());
      }
      int i = penSize;
      if (i != 0)
      {
        if (i == 2) {
          mCharacterStyles.add(new RelativeSizeSpan(1.25F));
        }
      }
      else {
        mCharacterStyles.add(new RelativeSizeSpan(0.75F));
      }
      i = penOffset;
      if (i != 0)
      {
        if (i == 2) {
          mCharacterStyles.add(new SuperscriptSpan());
        }
      }
      else {
        mCharacterStyles.add(new SubscriptSpan());
      }
    }
    
    public void setPenColor(Cea708CCParser.CaptionPenColor paramCaptionPenColor) {}
    
    public void setPenLocation(int paramInt1, int paramInt2)
    {
      if (mRow >= 0) {
        for (paramInt2 = mRow; paramInt2 < paramInt1; paramInt2++) {
          appendText("\n");
        }
      }
      mRow = paramInt1;
    }
    
    public void setRowLimit(int paramInt)
    {
      if (paramInt >= 0)
      {
        mRowLimit = paramInt;
        return;
      }
      throw new IllegalArgumentException("A rowLimit should have a positive number");
    }
    
    public void setText(String paramString)
    {
      updateText(paramString, false);
    }
    
    public void setWindowAttr(Cea708CCParser.CaptionWindowAttr paramCaptionWindowAttr) {}
    
    public void show()
    {
      setVisibility(0);
      requestLayout();
    }
  }
  
  static class ScaledLayout
    extends ViewGroup
  {
    private static final boolean DEBUG = false;
    private static final String TAG = "ScaledLayout";
    private static final Comparator<Rect> mRectTopLeftSorter = new Comparator()
    {
      public int compare(Rect paramAnonymousRect1, Rect paramAnonymousRect2)
      {
        if (top != top) {
          return top - top;
        }
        return left - left;
      }
    };
    private Rect[] mRectArray;
    
    public ScaledLayout(Context paramContext)
    {
      super();
    }
    
    protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      return paramLayoutParams instanceof ScaledLayoutParams;
    }
    
    public void dispatchDraw(Canvas paramCanvas)
    {
      int i = getPaddingLeft();
      int j = getPaddingTop();
      int k = getChildCount();
      for (int m = 0; m < k; m++)
      {
        View localView = getChildAt(m);
        if (localView.getVisibility() != 8)
        {
          if (m >= mRectArray.length) {
            break;
          }
          int n = mRectArray[m].left;
          int i1 = mRectArray[m].top;
          int i2 = paramCanvas.save();
          paramCanvas.translate(n + i, i1 + j);
          localView.draw(paramCanvas);
          paramCanvas.restoreToCount(i2);
        }
      }
    }
    
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
    {
      return new ScaledLayoutParams(getContext(), paramAttributeSet);
    }
    
    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      paramInt3 = getPaddingLeft();
      paramInt4 = getPaddingTop();
      paramInt2 = getChildCount();
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
      {
        View localView = getChildAt(paramInt1);
        if (localView.getVisibility() != 8)
        {
          int i = mRectArray[paramInt1].left;
          int j = mRectArray[paramInt1].top;
          int k = mRectArray[paramInt1].bottom;
          localView.layout(i + paramInt3, j + paramInt4, mRectArray[paramInt1].right + paramInt4, k + paramInt3);
        }
      }
    }
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      int i = View.MeasureSpec.getSize(paramInt1);
      paramInt1 = View.MeasureSpec.getSize(paramInt2);
      int j = i - getPaddingLeft() - getPaddingRight();
      int k = paramInt1 - getPaddingTop() - getPaddingBottom();
      int m = getChildCount();
      mRectArray = new Rect[m];
      int n = 0;
      paramInt2 = i;
      while (n < m)
      {
        localObject1 = getChildAt(n);
        localObject2 = ((View)localObject1).getLayoutParams();
        if ((localObject2 instanceof ScaledLayoutParams))
        {
          float f1 = scaleStartRow;
          float f2 = scaleEndRow;
          float f3 = scaleStartCol;
          float f4 = scaleEndCol;
          if ((f1 >= 0.0F) && (f1 <= 1.0F))
          {
            if ((f2 >= f1) && (f1 <= 1.0F))
            {
              if ((f4 >= 0.0F) && (f4 <= 1.0F))
              {
                if ((f4 >= f3) && (f4 <= 1.0F))
                {
                  mRectArray[n] = new Rect((int)(j * f3), (int)(k * f1), (int)(j * f4), (int)(k * f2));
                  i = View.MeasureSpec.makeMeasureSpec((int)(j * (f4 - f3)), 1073741824);
                  ((View)localObject1).measure(i, View.MeasureSpec.makeMeasureSpec(0, 0));
                  if (((View)localObject1).getMeasuredHeight() > mRectArray[n].height())
                  {
                    int i1 = (((View)localObject1).getMeasuredHeight() - mRectArray[n].height() + 1) / 2;
                    localObject2 = mRectArray[n];
                    bottom += i1;
                    localObject2 = mRectArray[n];
                    top -= i1;
                    if (mRectArray[n].top < 0)
                    {
                      localObject2 = mRectArray[n];
                      bottom -= mRectArray[n].top;
                      mRectArray[n].top = 0;
                    }
                    if (mRectArray[n].bottom > k)
                    {
                      localObject2 = mRectArray[n];
                      top -= mRectArray[n].bottom - k;
                      mRectArray[n].bottom = k;
                    }
                  }
                  ((View)localObject1).measure(i, View.MeasureSpec.makeMeasureSpec((int)(k * (f2 - f1)), 1073741824));
                  n++;
                }
                else
                {
                  throw new RuntimeException("A child of ScaledLayout should have a range of scaleEndCol between scaleStartCol and 1");
                }
              }
              else {
                throw new RuntimeException("A child of ScaledLayout should have a range of scaleStartCol between 0 and 1");
              }
            }
            else {
              throw new RuntimeException("A child of ScaledLayout should have a range of scaleEndRow between scaleStartRow and 1");
            }
          }
          else {
            throw new RuntimeException("A child of ScaledLayout should have a range of scaleStartRow between 0 and 1");
          }
        }
        else
        {
          throw new RuntimeException("A child of ScaledLayout cannot have the UNSPECIFIED scale factors");
        }
      }
      Object localObject1 = new int[m];
      Object localObject2 = new Rect[m];
      n = 0;
      i = 0;
      while (i < m)
      {
        j = n;
        if (getChildAt(i).getVisibility() == 0)
        {
          localObject1[n] = n;
          localObject2[n] = mRectArray[i];
          j = n + 1;
        }
        i++;
        n = j;
      }
      Arrays.sort((Object[])localObject2, 0, n, mRectTopLeftSorter);
      for (i = 0; i < n - 1; i++) {
        for (j = i + 1; j < n; j++) {
          if (Rect.intersects(localObject2[i], localObject2[j]))
          {
            localObject1[j] = localObject1[i];
            localObject2[j].set(left, bottom, right, bottom + localObject2[j].height());
          }
        }
      }
      n--;
      while (n >= 0)
      {
        if (bottom > k)
        {
          j = bottom - k;
          for (i = 0; i <= n; i++) {
            if (localObject1[n] == localObject1[i]) {
              localObject2[i].set(left, top - j, right, bottom - j);
            }
          }
        }
        n--;
      }
      setMeasuredDimension(paramInt2, paramInt1);
    }
    
    static class ScaledLayoutParams
      extends ViewGroup.LayoutParams
    {
      public static final float SCALE_UNSPECIFIED = -1.0F;
      public float scaleEndCol;
      public float scaleEndRow;
      public float scaleStartCol;
      public float scaleStartRow;
      
      public ScaledLayoutParams(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
      {
        super(-1);
        scaleStartRow = paramFloat1;
        scaleEndRow = paramFloat2;
        scaleStartCol = paramFloat3;
        scaleEndCol = paramFloat4;
      }
      
      public ScaledLayoutParams(Context paramContext, AttributeSet paramAttributeSet)
      {
        super(-1);
      }
    }
  }
}
