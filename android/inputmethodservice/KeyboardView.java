package android.inputmethodservice;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardView
  extends View
  implements View.OnClickListener
{
  private static final int DEBOUNCE_TIME = 70;
  private static final boolean DEBUG = false;
  private static final int DELAY_AFTER_PREVIEW = 70;
  private static final int DELAY_BEFORE_PREVIEW = 0;
  private static final int[] KEY_DELETE = { -5 };
  private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
  private static final int[] LONG_PRESSABLE_STATE_SET = { 16843324 };
  private static int MAX_NEARBY_KEYS = 12;
  private static final int MSG_LONGPRESS = 4;
  private static final int MSG_REMOVE_PREVIEW = 2;
  private static final int MSG_REPEAT = 3;
  private static final int MSG_SHOW_PREVIEW = 1;
  private static final int MULTITAP_INTERVAL = 800;
  private static final int NOT_A_KEY = -1;
  private static final int REPEAT_INTERVAL = 50;
  private static final int REPEAT_START_DELAY = 400;
  private boolean mAbortKey;
  private AccessibilityManager mAccessibilityManager;
  private AudioManager mAudioManager;
  private float mBackgroundDimAmount;
  private Bitmap mBuffer;
  private Canvas mCanvas;
  private Rect mClipRegion = new Rect(0, 0, 0, 0);
  private final int[] mCoordinates = new int[2];
  private int mCurrentKey = -1;
  private int mCurrentKeyIndex = -1;
  private long mCurrentKeyTime;
  private Rect mDirtyRect = new Rect();
  private boolean mDisambiguateSwipe;
  private int[] mDistances = new int[MAX_NEARBY_KEYS];
  private int mDownKey = -1;
  private long mDownTime;
  private boolean mDrawPending;
  private GestureDetector mGestureDetector;
  Handler mHandler;
  private boolean mHeadsetRequiredToHearPasswordsAnnounced;
  private boolean mInMultiTap;
  private Keyboard.Key mInvalidatedKey;
  private Drawable mKeyBackground;
  private int[] mKeyIndices = new int[12];
  private int mKeyTextColor;
  private int mKeyTextSize;
  private Keyboard mKeyboard;
  private OnKeyboardActionListener mKeyboardActionListener;
  private boolean mKeyboardChanged;
  private Keyboard.Key[] mKeys;
  private int mLabelTextSize;
  private int mLastCodeX;
  private int mLastCodeY;
  private int mLastKey;
  private long mLastKeyTime;
  private long mLastMoveTime;
  private int mLastSentIndex;
  private long mLastTapTime;
  private int mLastX;
  private int mLastY;
  private KeyboardView mMiniKeyboard;
  private Map<Keyboard.Key, View> mMiniKeyboardCache;
  private View mMiniKeyboardContainer;
  private int mMiniKeyboardOffsetX;
  private int mMiniKeyboardOffsetY;
  private boolean mMiniKeyboardOnScreen;
  private int mOldPointerCount = 1;
  private float mOldPointerX;
  private float mOldPointerY;
  private Rect mPadding;
  private Paint mPaint;
  private PopupWindow mPopupKeyboard;
  private int mPopupLayout;
  private View mPopupParent;
  private int mPopupPreviewX;
  private int mPopupPreviewY;
  private int mPopupX;
  private int mPopupY;
  private boolean mPossiblePoly;
  private boolean mPreviewCentered = false;
  private int mPreviewHeight;
  private StringBuilder mPreviewLabel = new StringBuilder(1);
  private int mPreviewOffset;
  private PopupWindow mPreviewPopup;
  private TextView mPreviewText;
  private int mPreviewTextSizeLarge;
  private boolean mProximityCorrectOn;
  private int mProximityThreshold;
  private int mRepeatKeyIndex = -1;
  private int mShadowColor;
  private float mShadowRadius;
  private boolean mShowPreview = true;
  private boolean mShowTouchPoints = true;
  private int mStartX;
  private int mStartY;
  private int mSwipeThreshold;
  private SwipeTracker mSwipeTracker = new SwipeTracker(null);
  private int mTapCount;
  private int mVerticalCorrection;
  
  public KeyboardView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 17891423);
  }
  
  public KeyboardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public KeyboardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, android.R.styleable.KeyboardView, paramInt1, paramInt2);
    LayoutInflater localLayoutInflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
    int i = paramAttributeSet.getIndexCount();
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < i; paramInt1++)
    {
      int j = paramAttributeSet.getIndex(paramInt1);
      switch (j)
      {
      default: 
        break;
      case 10: 
        mPopupLayout = paramAttributeSet.getResourceId(j, 0);
        break;
      case 9: 
        mVerticalCorrection = paramAttributeSet.getDimensionPixelOffset(j, 0);
        break;
      case 8: 
        mPreviewHeight = paramAttributeSet.getDimensionPixelSize(j, 80);
        break;
      case 7: 
        mPreviewOffset = paramAttributeSet.getDimensionPixelOffset(j, 0);
        break;
      case 6: 
        paramInt2 = paramAttributeSet.getResourceId(j, 0);
        break;
      case 5: 
        mKeyTextColor = paramAttributeSet.getColor(j, -16777216);
        break;
      case 4: 
        mLabelTextSize = paramAttributeSet.getDimensionPixelSize(j, 14);
        break;
      case 3: 
        mKeyTextSize = paramAttributeSet.getDimensionPixelSize(j, 18);
        break;
      case 2: 
        mKeyBackground = paramAttributeSet.getDrawable(j);
        break;
      case 1: 
        mShadowRadius = paramAttributeSet.getFloat(j, 0.0F);
        break;
      case 0: 
        mShadowColor = paramAttributeSet.getColor(j, 0);
      }
    }
    mBackgroundDimAmount = mContext.obtainStyledAttributes(com.android.internal.R.styleable.Theme).getFloat(2, 0.5F);
    mPreviewPopup = new PopupWindow(paramContext);
    if (paramInt2 != 0)
    {
      mPreviewText = ((TextView)localLayoutInflater.inflate(paramInt2, null));
      mPreviewTextSizeLarge = ((int)mPreviewText.getTextSize());
      mPreviewPopup.setContentView(mPreviewText);
      mPreviewPopup.setBackgroundDrawable(null);
    }
    else
    {
      mShowPreview = false;
    }
    mPreviewPopup.setTouchable(false);
    mPopupKeyboard = new PopupWindow(paramContext);
    mPopupKeyboard.setBackgroundDrawable(null);
    mPopupParent = this;
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setTextSize(0);
    mPaint.setTextAlign(Paint.Align.CENTER);
    mPaint.setAlpha(255);
    mPadding = new Rect(0, 0, 0, 0);
    mMiniKeyboardCache = new HashMap();
    mKeyBackground.getPadding(mPadding);
    mSwipeThreshold = ((int)(500.0F * getResourcesgetDisplayMetricsdensity));
    mDisambiguateSwipe = getResources().getBoolean(17957054);
    mAccessibilityManager = AccessibilityManager.getInstance(paramContext);
    mAudioManager = ((AudioManager)paramContext.getSystemService("audio"));
    resetMultiTap();
  }
  
  private CharSequence adjustCase(CharSequence paramCharSequence)
  {
    Object localObject = paramCharSequence;
    if (mKeyboard.isShifted())
    {
      localObject = paramCharSequence;
      if (paramCharSequence != null)
      {
        localObject = paramCharSequence;
        if (paramCharSequence.length() < 3)
        {
          localObject = paramCharSequence;
          if (Character.isLowerCase(paramCharSequence.charAt(0))) {
            localObject = paramCharSequence.toString().toUpperCase();
          }
        }
      }
    }
    return localObject;
  }
  
  private void checkMultiTap(long paramLong, int paramInt)
  {
    if (paramInt == -1) {
      return;
    }
    Keyboard.Key localKey = mKeys[paramInt];
    if (codes.length > 1)
    {
      mInMultiTap = true;
      if ((paramLong < mLastTapTime + 800L) && (paramInt == mLastSentIndex))
      {
        mTapCount = ((mTapCount + 1) % codes.length);
        return;
      }
      mTapCount = -1;
      return;
    }
    if ((paramLong > mLastTapTime + 800L) || (paramInt != mLastSentIndex)) {
      resetMultiTap();
    }
  }
  
  private void computeProximityThreshold(Keyboard paramKeyboard)
  {
    if (paramKeyboard == null) {
      return;
    }
    paramKeyboard = mKeys;
    if (paramKeyboard == null) {
      return;
    }
    int i = paramKeyboard.length;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      Object localObject = paramKeyboard[k];
      j += Math.min(width, height) + gap;
    }
    if ((j >= 0) && (i != 0))
    {
      mProximityThreshold = ((int)(j * 1.4F / i));
      mProximityThreshold *= mProximityThreshold;
      return;
    }
  }
  
  private void detectAndSendKey(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    if ((paramInt1 != -1) && (paramInt1 < mKeys.length))
    {
      Keyboard.Key localKey = mKeys[paramInt1];
      if (text != null)
      {
        mKeyboardActionListener.onText(text);
        mKeyboardActionListener.onRelease(-1);
      }
      else
      {
        int i = codes[0];
        int[] arrayOfInt = new int[MAX_NEARBY_KEYS];
        Arrays.fill(arrayOfInt, -1);
        getKeyIndices(paramInt2, paramInt3, arrayOfInt);
        paramInt2 = i;
        if (mInMultiTap)
        {
          if (mTapCount != -1) {
            mKeyboardActionListener.onKey(-5, KEY_DELETE);
          } else {
            mTapCount = 0;
          }
          paramInt2 = codes[mTapCount];
        }
        mKeyboardActionListener.onKey(paramInt2, arrayOfInt);
        mKeyboardActionListener.onRelease(paramInt2);
      }
      mLastSentIndex = paramInt1;
      mLastTapTime = paramLong;
    }
  }
  
  private void dismissPopupKeyboard()
  {
    if (mPopupKeyboard.isShowing())
    {
      mPopupKeyboard.dismiss();
      mMiniKeyboardOnScreen = false;
      invalidateAllKeys();
    }
  }
  
  private int getKeyIndices(int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    Keyboard.Key[] arrayOfKey = mKeys;
    int i = mProximityThreshold;
    Arrays.fill(mDistances, Integer.MAX_VALUE);
    int[] arrayOfInt = mKeyboard.getNearestKeys(paramInt1, paramInt2);
    int j = arrayOfInt.length;
    int k = i + 1;
    i = -1;
    int m = -1;
    int n = 0;
    for (;;)
    {
      int i1 = paramInt2;
      int i2 = paramInt1;
      if (n >= j) {
        break;
      }
      Keyboard.Key localKey = arrayOfKey[arrayOfInt[n]];
      int i3 = 0;
      boolean bool = localKey.isInside(i2, i1);
      int i4 = m;
      if (bool) {
        i4 = arrayOfInt[n];
      }
      if (mProximityCorrectOn)
      {
        i1 = localKey.squaredDistanceFrom(i2, i1);
        m = i1;
        i3 = m;
        if (i1 >= mProximityThreshold) {
          i3 = m;
        }
      }
      else
      {
        i1 = i;
        i2 = k;
        if (!bool) {
          break label369;
        }
      }
      i1 = i;
      i2 = k;
      if (codes[0] > 32)
      {
        int i5 = codes.length;
        m = i;
        i = k;
        if (i3 < k)
        {
          i = i3;
          m = arrayOfInt[n];
        }
        if (paramArrayOfInt == null)
        {
          i1 = m;
          k = i;
        }
        else
        {
          for (k = 0;; k++)
          {
            i1 = m;
            i2 = i;
            if (k >= mDistances.length) {
              break;
            }
            if (mDistances[k] > i3)
            {
              System.arraycopy(mDistances, k, mDistances, k + i5, mDistances.length - k - i5);
              System.arraycopy(paramArrayOfInt, k, paramArrayOfInt, k + i5, paramArrayOfInt.length - k - i5);
              for (i1 = 0; i1 < i5; i1++)
              {
                paramArrayOfInt[(k + i1)] = codes[i1];
                mDistances[(k + i1)] = i3;
              }
              i1 = m;
              k = i;
              break label373;
            }
          }
        }
      }
      else
      {
        label369:
        k = i2;
      }
      label373:
      n++;
      m = i4;
      i = i1;
    }
    paramInt1 = m;
    if (m == -1) {
      paramInt1 = i;
    }
    return paramInt1;
  }
  
  private CharSequence getPreviewText(Keyboard.Key paramKey)
  {
    if (mInMultiTap)
    {
      StringBuilder localStringBuilder = mPreviewLabel;
      int i = 0;
      localStringBuilder.setLength(0);
      localStringBuilder = mPreviewLabel;
      paramKey = codes;
      if (mTapCount >= 0) {
        i = mTapCount;
      }
      localStringBuilder.append((char)paramKey[i]);
      return adjustCase(mPreviewLabel);
    }
    return adjustCase(label);
  }
  
  private void initGestureDetector()
  {
    if (mGestureDetector == null)
    {
      mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
      {
        public boolean onFling(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
        {
          if (mPossiblePoly) {
            return false;
          }
          float f1 = Math.abs(paramAnonymousFloat1);
          float f2 = Math.abs(paramAnonymousFloat2);
          float f3 = paramAnonymousMotionEvent2.getX() - paramAnonymousMotionEvent1.getX();
          float f4 = paramAnonymousMotionEvent2.getY() - paramAnonymousMotionEvent1.getY();
          int i = getWidth() / 2;
          int j = getHeight() / 2;
          mSwipeTracker.computeCurrentVelocity(1000);
          float f5 = mSwipeTracker.getXVelocity();
          float f6 = mSwipeTracker.getYVelocity();
          int k = 0;
          if ((paramAnonymousFloat1 > mSwipeThreshold) && (f2 < f1) && (f3 > i))
          {
            if ((mDisambiguateSwipe) && (f5 < paramAnonymousFloat1 / 4.0F))
            {
              i = 1;
            }
            else
            {
              swipeRight();
              return true;
            }
          }
          else if ((paramAnonymousFloat1 < -mSwipeThreshold) && (f2 < f1) && (f3 < -i))
          {
            if ((mDisambiguateSwipe) && (f5 > paramAnonymousFloat1 / 4.0F))
            {
              i = 1;
            }
            else
            {
              swipeLeft();
              return true;
            }
          }
          else if ((paramAnonymousFloat2 < -mSwipeThreshold) && (f1 < f2) && (f4 < -j))
          {
            if ((mDisambiguateSwipe) && (f6 > paramAnonymousFloat2 / 4.0F))
            {
              i = 1;
            }
            else
            {
              swipeUp();
              return true;
            }
          }
          else
          {
            i = k;
            if (paramAnonymousFloat2 > mSwipeThreshold)
            {
              i = k;
              if (f1 < f2 / 2.0F)
              {
                i = k;
                if (f4 > j) {
                  if ((mDisambiguateSwipe) && (f6 < paramAnonymousFloat2 / 4.0F))
                  {
                    i = 1;
                  }
                  else
                  {
                    swipeDown();
                    return true;
                  }
                }
              }
            }
          }
          if (i != 0) {
            KeyboardView.this.detectAndSendKey(mDownKey, mStartX, mStartY, paramAnonymousMotionEvent1.getEventTime());
          }
          return false;
        }
      });
      mGestureDetector.setIsLongpressEnabled(false);
    }
  }
  
  private void onBufferDraw()
  {
    if ((mBuffer == null) || (mKeyboardChanged))
    {
      if ((mBuffer == null) || ((mKeyboardChanged) && ((mBuffer.getWidth() != getWidth()) || (mBuffer.getHeight() != getHeight()))))
      {
        mBuffer = Bitmap.createBitmap(Math.max(1, getWidth()), Math.max(1, getHeight()), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBuffer);
      }
      invalidateAllKeys();
      mKeyboardChanged = false;
    }
    if (mKeyboard == null) {
      return;
    }
    mCanvas.save();
    Canvas localCanvas = mCanvas;
    localCanvas.clipRect(mDirtyRect);
    Paint localPaint = mPaint;
    Drawable localDrawable = mKeyBackground;
    Object localObject = mClipRegion;
    Rect localRect1 = mPadding;
    int i = mPaddingLeft;
    int j = mPaddingTop;
    Keyboard.Key[] arrayOfKey = mKeys;
    Keyboard.Key localKey1 = mInvalidatedKey;
    localPaint.setColor(mKeyTextColor);
    int k = 0;
    int m = k;
    if (localKey1 != null)
    {
      m = k;
      if (localCanvas.getClipBounds((Rect)localObject))
      {
        m = k;
        if (x + i - 1 <= left)
        {
          m = k;
          if (y + j - 1 <= top)
          {
            m = k;
            if (x + width + i + 1 >= right)
            {
              m = k;
              if (y + height + j + 1 >= bottom) {
                m = 1;
              }
            }
          }
        }
      }
    }
    localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    k = arrayOfKey.length;
    for (int n = 0; n < k; n++)
    {
      Keyboard.Key localKey2 = arrayOfKey[n];
      if ((m == 0) || (localKey1 == localKey2))
      {
        localDrawable.setState(localKey2.getCurrentDrawableState());
        if (label == null) {
          localObject = null;
        } else {
          localObject = adjustCase(label).toString();
        }
        Rect localRect2 = localDrawable.getBounds();
        if ((width == right) && (height == bottom)) {
          break label457;
        }
        localDrawable.setBounds(0, 0, width, height);
        label457:
        localCanvas.translate(x + i, y + j);
        localDrawable.draw(localCanvas);
        if (localObject != null)
        {
          if ((((String)localObject).length() > 1) && (codes.length < 2))
          {
            localPaint.setTextSize(mLabelTextSize);
            localPaint.setTypeface(Typeface.DEFAULT_BOLD);
          }
          else
          {
            localPaint.setTextSize(mKeyTextSize);
            localPaint.setTypeface(Typeface.DEFAULT);
          }
          localPaint.setShadowLayer(mShadowRadius, 0.0F, 0.0F, mShadowColor);
          localCanvas.drawText((String)localObject, (width - left - right) / 2 + left, (height - top - bottom) / 2 + (localPaint.getTextSize() - localPaint.descent()) / 2.0F + top, localPaint);
          localPaint.setShadowLayer(0.0F, 0.0F, 0.0F, 0);
        }
        else if (icon != null)
        {
          int i1 = (width - left - right - icon.getIntrinsicWidth()) / 2 + left;
          int i2 = (height - top - bottom - icon.getIntrinsicHeight()) / 2 + top;
          localCanvas.translate(i1, i2);
          icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
          icon.draw(localCanvas);
          localCanvas.translate(-i1, -i2);
        }
        localCanvas.translate(-x - i, -y - j);
      }
    }
    mInvalidatedKey = null;
    if (mMiniKeyboardOnScreen)
    {
      localPaint.setColor((int)(mBackgroundDimAmount * 255.0F) << 24);
      localCanvas.drawRect(0.0F, 0.0F, getWidth(), getHeight(), localPaint);
    }
    mCanvas.restore();
    mDrawPending = false;
    mDirtyRect.setEmpty();
  }
  
  private boolean onModifiedTouchEvent(MotionEvent paramMotionEvent, boolean paramBoolean)
  {
    int i = (int)paramMotionEvent.getX() - mPaddingLeft;
    int j = (int)paramMotionEvent.getY() - mPaddingTop;
    int k = j;
    if (j >= -mVerticalCorrection) {
      k = j + mVerticalCorrection;
    }
    j = paramMotionEvent.getAction();
    long l = paramMotionEvent.getEventTime();
    int m = getKeyIndices(i, k, null);
    mPossiblePoly = paramBoolean;
    if (j == 0) {
      mSwipeTracker.clear();
    }
    mSwipeTracker.addMovement(paramMotionEvent);
    if ((mAbortKey) && (j != 0) && (j != 3)) {
      return true;
    }
    if (mGestureDetector.onTouchEvent(paramMotionEvent))
    {
      showPreview(-1);
      mHandler.removeMessages(3);
      mHandler.removeMessages(4);
      return true;
    }
    if ((mMiniKeyboardOnScreen) && (j != 3)) {
      return true;
    }
    switch (j)
    {
    default: 
      break;
    case 3: 
      removeMessages();
      dismissPopupKeyboard();
      mAbortKey = true;
      showPreview(-1);
      invalidateKey(mCurrentKey);
      break;
    case 2: 
      n = 0;
      j = n;
      if (m != -1) {
        if (mCurrentKey == -1)
        {
          mCurrentKey = m;
          mCurrentKeyTime = (l - mDownTime);
          j = n;
        }
        else if (m == mCurrentKey)
        {
          mCurrentKeyTime += l - mLastMoveTime;
          j = 1;
        }
        else
        {
          j = n;
          if (mRepeatKeyIndex == -1)
          {
            resetMultiTap();
            mLastKey = mCurrentKey;
            mLastCodeX = mLastX;
            mLastCodeY = mLastY;
            mLastKeyTime = (mCurrentKeyTime + l - mLastMoveTime);
            mCurrentKey = m;
            mCurrentKeyTime = 0L;
            j = n;
          }
        }
      }
      if (j == 0)
      {
        mHandler.removeMessages(4);
        if (m != -1)
        {
          paramMotionEvent = mHandler.obtainMessage(4, paramMotionEvent);
          mHandler.sendMessageDelayed(paramMotionEvent, LONGPRESS_TIMEOUT);
        }
      }
      showPreview(mCurrentKey);
      mLastMoveTime = l;
      break;
    case 1: 
      removeMessages();
      if (m == mCurrentKey)
      {
        mCurrentKeyTime += l - mLastMoveTime;
      }
      else
      {
        resetMultiTap();
        mLastKey = mCurrentKey;
        mLastKeyTime = (mCurrentKeyTime + l - mLastMoveTime);
        mCurrentKey = m;
        mCurrentKeyTime = 0L;
      }
      n = i;
      j = k;
      if (mCurrentKeyTime < mLastKeyTime)
      {
        n = i;
        j = k;
        if (mCurrentKeyTime < 70L)
        {
          n = i;
          j = k;
          if (mLastKey != -1)
          {
            mCurrentKey = mLastKey;
            n = mLastCodeX;
            j = mLastCodeY;
          }
        }
      }
      showPreview(-1);
      Arrays.fill(mKeyIndices, -1);
      if ((mRepeatKeyIndex == -1) && (!mMiniKeyboardOnScreen) && (!mAbortKey)) {
        detectAndSendKey(mCurrentKey, n, j, l);
      }
      invalidateKey(m);
      mRepeatKeyIndex = -1;
      break;
    case 0: 
      j = 0;
      mAbortKey = false;
      mStartX = i;
      mStartY = k;
      mLastCodeX = i;
      mLastCodeY = k;
      mLastKeyTime = 0L;
      mCurrentKeyTime = 0L;
      mLastKey = -1;
      mCurrentKey = m;
      mDownKey = m;
      mDownTime = paramMotionEvent.getEventTime();
      mLastMoveTime = mDownTime;
      checkMultiTap(l, m);
      Object localObject = mKeyboardActionListener;
      if (m != -1) {
        j = mKeys[m].codes[0];
      }
      ((OnKeyboardActionListener)localObject).onPress(j);
      if ((mCurrentKey >= 0) && (mKeys[mCurrentKey].repeatable))
      {
        mRepeatKeyIndex = mCurrentKey;
        localObject = mHandler.obtainMessage(3);
        mHandler.sendMessageDelayed((Message)localObject, 400L);
        repeatKey();
        if (mAbortKey)
        {
          mRepeatKeyIndex = -1;
          break;
        }
      }
      if (mCurrentKey != -1)
      {
        paramMotionEvent = mHandler.obtainMessage(4, paramMotionEvent);
        mHandler.sendMessageDelayed(paramMotionEvent, LONGPRESS_TIMEOUT);
      }
      showPreview(m);
    }
    j = k;
    int n = i;
    mLastX = n;
    mLastY = j;
    return true;
  }
  
  private boolean openPopupIfRequired(MotionEvent paramMotionEvent)
  {
    if (mPopupLayout == 0) {
      return false;
    }
    if ((mCurrentKey >= 0) && (mCurrentKey < mKeys.length))
    {
      boolean bool = onLongPress(mKeys[mCurrentKey]);
      if (bool)
      {
        mAbortKey = true;
        showPreview(-1);
      }
      return bool;
    }
    return false;
  }
  
  private void removeMessages()
  {
    if (mHandler != null)
    {
      mHandler.removeMessages(3);
      mHandler.removeMessages(4);
      mHandler.removeMessages(1);
    }
  }
  
  private boolean repeatKey()
  {
    Keyboard.Key localKey = mKeys[mRepeatKeyIndex];
    detectAndSendKey(mCurrentKey, x, y, mLastTapTime);
    return true;
  }
  
  private void resetMultiTap()
  {
    mLastSentIndex = -1;
    mTapCount = 0;
    mLastTapTime = -1L;
    mInMultiTap = false;
  }
  
  private void sendAccessibilityEventForUnicodeCharacter(int paramInt1, int paramInt2)
  {
    if (mAccessibilityManager.isEnabled())
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt1);
      onInitializeAccessibilityEvent(localAccessibilityEvent);
      String str;
      if (paramInt2 != 10) {
        switch (paramInt2)
        {
        default: 
          str = String.valueOf((char)paramInt2);
          break;
        case -1: 
          str = mContext.getString(17040158);
          break;
        case -2: 
          str = mContext.getString(17040157);
          break;
        case -3: 
          str = mContext.getString(17040153);
          break;
        case -4: 
          str = mContext.getString(17040155);
          break;
        case -5: 
          str = mContext.getString(17040154);
          break;
        case -6: 
          str = mContext.getString(17040152);
          break;
        }
      } else {
        str = mContext.getString(17040156);
      }
      localAccessibilityEvent.getText().add(str);
      mAccessibilityManager.sendAccessibilityEvent(localAccessibilityEvent);
    }
  }
  
  private void showKey(int paramInt)
  {
    PopupWindow localPopupWindow = mPreviewPopup;
    Object localObject1 = mKeys;
    if ((paramInt >= 0) && (paramInt < mKeys.length))
    {
      Keyboard.Key localKey = localObject1[paramInt];
      if (icon != null)
      {
        localObject2 = mPreviewText;
        if (iconPreview != null) {
          localObject1 = iconPreview;
        } else {
          localObject1 = icon;
        }
        ((TextView)localObject2).setCompoundDrawables(null, null, null, (Drawable)localObject1);
        mPreviewText.setText(null);
      }
      else
      {
        mPreviewText.setCompoundDrawables(null, null, null, null);
        mPreviewText.setText(getPreviewText(localKey));
        if ((label.length() > 1) && (codes.length < 2))
        {
          mPreviewText.setTextSize(0, mKeyTextSize);
          mPreviewText.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else
        {
          mPreviewText.setTextSize(0, mPreviewTextSizeLarge);
          mPreviewText.setTypeface(Typeface.DEFAULT);
        }
      }
      mPreviewText.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
      int i = Math.max(mPreviewText.getMeasuredWidth(), width + mPreviewText.getPaddingLeft() + mPreviewText.getPaddingRight());
      paramInt = mPreviewHeight;
      localObject1 = mPreviewText.getLayoutParams();
      if (localObject1 != null)
      {
        width = i;
        height = paramInt;
      }
      if (!mPreviewCentered)
      {
        mPopupPreviewX = (x - mPreviewText.getPaddingLeft() + mPaddingLeft);
        mPopupPreviewY = (y - paramInt + mPreviewOffset);
      }
      else
      {
        mPopupPreviewX = (160 - mPreviewText.getMeasuredWidth() / 2);
        mPopupPreviewY = (-mPreviewText.getMeasuredHeight());
      }
      mHandler.removeMessages(2);
      getLocationInWindow(mCoordinates);
      localObject1 = mCoordinates;
      localObject1[0] += mMiniKeyboardOffsetX;
      localObject1 = mCoordinates;
      localObject1[1] += mMiniKeyboardOffsetY;
      Object localObject2 = mPreviewText.getBackground();
      if (popupResId != 0) {
        localObject1 = LONG_PRESSABLE_STATE_SET;
      } else {
        localObject1 = EMPTY_STATE_SET;
      }
      ((Drawable)localObject2).setState((int[])localObject1);
      mPopupPreviewX += mCoordinates[0];
      mPopupPreviewY += mCoordinates[1];
      getLocationOnScreen(mCoordinates);
      if (mPopupPreviewY + mCoordinates[1] < 0)
      {
        if (x + width <= getWidth() / 2) {
          mPopupPreviewX += (int)(width * 2.5D);
        } else {
          mPopupPreviewX -= (int)(width * 2.5D);
        }
        mPopupPreviewY += paramInt;
      }
      if (localPopupWindow.isShowing())
      {
        localPopupWindow.update(mPopupPreviewX, mPopupPreviewY, i, paramInt);
      }
      else
      {
        localPopupWindow.setWidth(i);
        localPopupWindow.setHeight(paramInt);
        localPopupWindow.showAtLocation(mPopupParent, 0, mPopupPreviewX, mPopupPreviewY);
      }
      mPreviewText.setVisibility(0);
      return;
    }
  }
  
  private void showPreview(int paramInt)
  {
    int i = mCurrentKeyIndex;
    PopupWindow localPopupWindow = mPreviewPopup;
    mCurrentKeyIndex = paramInt;
    Object localObject1 = mKeys;
    if (i != mCurrentKeyIndex)
    {
      int j;
      if ((i != -1) && (localObject1.length > i))
      {
        Object localObject2 = localObject1[i];
        boolean bool;
        if (mCurrentKeyIndex == -1) {
          bool = true;
        } else {
          bool = false;
        }
        localObject2.onReleased(bool);
        invalidateKey(i);
        j = codes[0];
        sendAccessibilityEventForUnicodeCharacter(256, j);
        sendAccessibilityEventForUnicodeCharacter(65536, j);
      }
      if ((mCurrentKeyIndex != -1) && (localObject1.length > mCurrentKeyIndex))
      {
        localObject1 = localObject1[mCurrentKeyIndex];
        ((Keyboard.Key)localObject1).onPressed();
        invalidateKey(mCurrentKeyIndex);
        j = codes[0];
        sendAccessibilityEventForUnicodeCharacter(128, j);
        sendAccessibilityEventForUnicodeCharacter(32768, j);
      }
    }
    if ((i != mCurrentKeyIndex) && (mShowPreview))
    {
      mHandler.removeMessages(1);
      if ((localPopupWindow.isShowing()) && (paramInt == -1)) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(2), 70L);
      }
      if (paramInt != -1) {
        if ((localPopupWindow.isShowing()) && (mPreviewText.getVisibility() == 0)) {
          showKey(paramInt);
        } else {
          mHandler.sendMessageDelayed(mHandler.obtainMessage(1, paramInt, 0), 0L);
        }
      }
    }
  }
  
  public void closing()
  {
    if (mPreviewPopup.isShowing()) {
      mPreviewPopup.dismiss();
    }
    removeMessages();
    dismissPopupKeyboard();
    mBuffer = null;
    mCanvas = null;
    mMiniKeyboardCache.clear();
  }
  
  public Keyboard getKeyboard()
  {
    return mKeyboard;
  }
  
  protected OnKeyboardActionListener getOnKeyboardActionListener()
  {
    return mKeyboardActionListener;
  }
  
  public boolean handleBack()
  {
    if (mPopupKeyboard.isShowing())
    {
      dismissPopupKeyboard();
      return true;
    }
    return false;
  }
  
  public void invalidateAllKeys()
  {
    mDirtyRect.union(0, 0, getWidth(), getHeight());
    mDrawPending = true;
    invalidate();
  }
  
  public void invalidateKey(int paramInt)
  {
    if (mKeys == null) {
      return;
    }
    if ((paramInt >= 0) && (paramInt < mKeys.length))
    {
      Keyboard.Key localKey = mKeys[paramInt];
      mInvalidatedKey = localKey;
      mDirtyRect.union(x + mPaddingLeft, y + mPaddingTop, x + width + mPaddingLeft, y + height + mPaddingTop);
      onBufferDraw();
      invalidate(x + mPaddingLeft, y + mPaddingTop, x + width + mPaddingLeft, y + height + mPaddingTop);
      return;
    }
  }
  
  public boolean isPreviewEnabled()
  {
    return mShowPreview;
  }
  
  public boolean isProximityCorrectionEnabled()
  {
    return mProximityCorrectOn;
  }
  
  public boolean isShifted()
  {
    if (mKeyboard != null) {
      return mKeyboard.isShifted();
    }
    return false;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    initGestureDetector();
    if (mHandler == null) {
      mHandler = new Handler()
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          switch (what)
          {
          default: 
            break;
          case 4: 
            KeyboardView.this.openPopupIfRequired((MotionEvent)obj);
            break;
          case 3: 
            if (KeyboardView.this.repeatKey()) {
              sendMessageDelayed(Message.obtain(this, 3), 50L);
            }
            break;
          case 2: 
            mPreviewText.setVisibility(4);
            break;
          case 1: 
            KeyboardView.this.showKey(arg1);
          }
        }
      };
    }
  }
  
  public void onClick(View paramView)
  {
    dismissPopupKeyboard();
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    closing();
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if ((mDrawPending) || (mBuffer == null) || (mKeyboardChanged)) {
      onBufferDraw();
    }
    paramCanvas.drawBitmap(mBuffer, 0.0F, 0.0F, null);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    if ((mAccessibilityManager.isTouchExplorationEnabled()) && (paramMotionEvent.getPointerCount() == 1))
    {
      int i = paramMotionEvent.getAction();
      if (i != 7) {
        switch (i)
        {
        default: 
          break;
        case 10: 
          paramMotionEvent.setAction(1);
          break;
        case 9: 
          paramMotionEvent.setAction(0);
          break;
        }
      } else {
        paramMotionEvent.setAction(2);
      }
      return onTouchEvent(paramMotionEvent);
    }
    return true;
  }
  
  protected boolean onLongPress(Keyboard.Key paramKey)
  {
    int i = popupResId;
    if (i != 0)
    {
      mMiniKeyboardContainer = ((View)mMiniKeyboardCache.get(paramKey));
      if (mMiniKeyboardContainer == null)
      {
        mMiniKeyboardContainer = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(mPopupLayout, null);
        mMiniKeyboard = ((KeyboardView)mMiniKeyboardContainer.findViewById(16908326));
        Object localObject = mMiniKeyboardContainer.findViewById(16908327);
        if (localObject != null) {
          ((View)localObject).setOnClickListener(this);
        }
        mMiniKeyboard.setOnKeyboardActionListener(new OnKeyboardActionListener()
        {
          public void onKey(int paramAnonymousInt, int[] paramAnonymousArrayOfInt)
          {
            mKeyboardActionListener.onKey(paramAnonymousInt, paramAnonymousArrayOfInt);
            KeyboardView.this.dismissPopupKeyboard();
          }
          
          public void onPress(int paramAnonymousInt)
          {
            mKeyboardActionListener.onPress(paramAnonymousInt);
          }
          
          public void onRelease(int paramAnonymousInt)
          {
            mKeyboardActionListener.onRelease(paramAnonymousInt);
          }
          
          public void onText(CharSequence paramAnonymousCharSequence)
          {
            mKeyboardActionListener.onText(paramAnonymousCharSequence);
            KeyboardView.this.dismissPopupKeyboard();
          }
          
          public void swipeDown() {}
          
          public void swipeLeft() {}
          
          public void swipeRight() {}
          
          public void swipeUp() {}
        });
        if (popupCharacters != null) {
          localObject = new Keyboard(getContext(), i, popupCharacters, -1, getPaddingLeft() + getPaddingRight());
        } else {
          localObject = new Keyboard(getContext(), i);
        }
        mMiniKeyboard.setKeyboard((Keyboard)localObject);
        mMiniKeyboard.setPopupParent(this);
        mMiniKeyboardContainer.measure(View.MeasureSpec.makeMeasureSpec(getWidth(), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE));
        mMiniKeyboardCache.put(paramKey, mMiniKeyboardContainer);
      }
      else
      {
        mMiniKeyboard = ((KeyboardView)mMiniKeyboardContainer.findViewById(16908326));
      }
      getLocationInWindow(mCoordinates);
      mPopupX = (x + mPaddingLeft);
      mPopupY = (y + mPaddingTop);
      mPopupX = (mPopupX + width - mMiniKeyboardContainer.getMeasuredWidth());
      mPopupY -= mMiniKeyboardContainer.getMeasuredHeight();
      int j = mPopupX + mMiniKeyboardContainer.getPaddingRight() + mCoordinates[0];
      int k = mPopupY + mMiniKeyboardContainer.getPaddingBottom() + mCoordinates[1];
      paramKey = mMiniKeyboard;
      if (j < 0) {
        i = 0;
      } else {
        i = j;
      }
      paramKey.setPopupOffset(i, k);
      mMiniKeyboard.setShifted(isShifted());
      mPopupKeyboard.setContentView(mMiniKeyboardContainer);
      mPopupKeyboard.setWidth(mMiniKeyboardContainer.getMeasuredWidth());
      mPopupKeyboard.setHeight(mMiniKeyboardContainer.getMeasuredHeight());
      mPopupKeyboard.showAtLocation(this, 0, j, k);
      mMiniKeyboardOnScreen = true;
      invalidateAllKeys();
      return true;
    }
    return false;
  }
  
  public void onMeasure(int paramInt1, int paramInt2)
  {
    if (mKeyboard == null)
    {
      setMeasuredDimension(mPaddingLeft + mPaddingRight, mPaddingTop + mPaddingBottom);
    }
    else
    {
      int i = mKeyboard.getMinWidth() + mPaddingLeft + mPaddingRight;
      paramInt2 = i;
      if (View.MeasureSpec.getSize(paramInt1) < i + 10) {
        paramInt2 = View.MeasureSpec.getSize(paramInt1);
      }
      setMeasuredDimension(paramInt2, mKeyboard.getHeight() + mPaddingTop + mPaddingBottom);
    }
  }
  
  public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (mKeyboard != null) {
      mKeyboard.resize(paramInt1, paramInt2);
    }
    mBuffer = null;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getPointerCount();
    int j = paramMotionEvent.getAction();
    long l = paramMotionEvent.getEventTime();
    boolean bool;
    if (i != mOldPointerCount)
    {
      if (i == 1)
      {
        MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 0, paramMotionEvent.getX(), paramMotionEvent.getY(), paramMotionEvent.getMetaState());
        bool = onModifiedTouchEvent(localMotionEvent, false);
        localMotionEvent.recycle();
        if (j == 1) {
          bool = onModifiedTouchEvent(paramMotionEvent, true);
        }
      }
      else
      {
        paramMotionEvent = MotionEvent.obtain(l, l, 1, mOldPointerX, mOldPointerY, paramMotionEvent.getMetaState());
        bool = onModifiedTouchEvent(paramMotionEvent, true);
        paramMotionEvent.recycle();
      }
    }
    else if (i == 1)
    {
      bool = onModifiedTouchEvent(paramMotionEvent, false);
      mOldPointerX = paramMotionEvent.getX();
      mOldPointerY = paramMotionEvent.getY();
    }
    else
    {
      bool = true;
    }
    mOldPointerCount = i;
    return bool;
  }
  
  public void setKeyboard(Keyboard paramKeyboard)
  {
    if (mKeyboard != null) {
      showPreview(-1);
    }
    removeMessages();
    mKeyboard = paramKeyboard;
    List localList = mKeyboard.getKeys();
    mKeys = ((Keyboard.Key[])localList.toArray(new Keyboard.Key[localList.size()]));
    requestLayout();
    mKeyboardChanged = true;
    invalidateAllKeys();
    computeProximityThreshold(paramKeyboard);
    mMiniKeyboardCache.clear();
    mAbortKey = true;
  }
  
  public void setOnKeyboardActionListener(OnKeyboardActionListener paramOnKeyboardActionListener)
  {
    mKeyboardActionListener = paramOnKeyboardActionListener;
  }
  
  public void setPopupOffset(int paramInt1, int paramInt2)
  {
    mMiniKeyboardOffsetX = paramInt1;
    mMiniKeyboardOffsetY = paramInt2;
    if (mPreviewPopup.isShowing()) {
      mPreviewPopup.dismiss();
    }
  }
  
  public void setPopupParent(View paramView)
  {
    mPopupParent = paramView;
  }
  
  public void setPreviewEnabled(boolean paramBoolean)
  {
    mShowPreview = paramBoolean;
  }
  
  public void setProximityCorrectionEnabled(boolean paramBoolean)
  {
    mProximityCorrectOn = paramBoolean;
  }
  
  public boolean setShifted(boolean paramBoolean)
  {
    if ((mKeyboard != null) && (mKeyboard.setShifted(paramBoolean)))
    {
      invalidateAllKeys();
      return true;
    }
    return false;
  }
  
  public void setVerticalCorrection(int paramInt) {}
  
  protected void swipeDown()
  {
    mKeyboardActionListener.swipeDown();
  }
  
  protected void swipeLeft()
  {
    mKeyboardActionListener.swipeLeft();
  }
  
  protected void swipeRight()
  {
    mKeyboardActionListener.swipeRight();
  }
  
  protected void swipeUp()
  {
    mKeyboardActionListener.swipeUp();
  }
  
  public static abstract interface OnKeyboardActionListener
  {
    public abstract void onKey(int paramInt, int[] paramArrayOfInt);
    
    public abstract void onPress(int paramInt);
    
    public abstract void onRelease(int paramInt);
    
    public abstract void onText(CharSequence paramCharSequence);
    
    public abstract void swipeDown();
    
    public abstract void swipeLeft();
    
    public abstract void swipeRight();
    
    public abstract void swipeUp();
  }
  
  private static class SwipeTracker
  {
    static final int LONGEST_PAST_TIME = 200;
    static final int NUM_PAST = 4;
    final long[] mPastTime = new long[4];
    final float[] mPastX = new float[4];
    final float[] mPastY = new float[4];
    float mXVelocity;
    float mYVelocity;
    
    private SwipeTracker() {}
    
    private void addPoint(float paramFloat1, float paramFloat2, long paramLong)
    {
      long[] arrayOfLong = mPastTime;
      int i = -1;
      for (int j = 0; (j < 4) && (arrayOfLong[j] != 0L); j++) {
        if (arrayOfLong[j] < paramLong - 200L) {
          i = j;
        }
      }
      int k = i;
      if (j == 4)
      {
        k = i;
        if (i < 0) {
          k = 0;
        }
      }
      i = k;
      if (k == j) {
        i = k - 1;
      }
      float[] arrayOfFloat1 = mPastX;
      float[] arrayOfFloat2 = mPastY;
      k = j;
      if (i >= 0)
      {
        k = i + 1;
        int m = 4 - i - 1;
        System.arraycopy(arrayOfFloat1, k, arrayOfFloat1, 0, m);
        System.arraycopy(arrayOfFloat2, k, arrayOfFloat2, 0, m);
        System.arraycopy(arrayOfLong, k, arrayOfLong, 0, m);
        k = j - (i + 1);
      }
      arrayOfFloat1[k] = paramFloat1;
      arrayOfFloat2[k] = paramFloat2;
      arrayOfLong[k] = paramLong;
      j = k + 1;
      if (j < 4) {
        arrayOfLong[j] = 0L;
      }
    }
    
    public void addMovement(MotionEvent paramMotionEvent)
    {
      long l = paramMotionEvent.getEventTime();
      int i = paramMotionEvent.getHistorySize();
      for (int j = 0; j < i; j++) {
        addPoint(paramMotionEvent.getHistoricalX(j), paramMotionEvent.getHistoricalY(j), paramMotionEvent.getHistoricalEventTime(j));
      }
      addPoint(paramMotionEvent.getX(), paramMotionEvent.getY(), l);
    }
    
    public void clear()
    {
      mPastTime[0] = 0L;
    }
    
    public void computeCurrentVelocity(int paramInt)
    {
      computeCurrentVelocity(paramInt, Float.MAX_VALUE);
    }
    
    public void computeCurrentVelocity(int paramInt, float paramFloat)
    {
      float[] arrayOfFloat1 = mPastX;
      float[] arrayOfFloat2 = mPastY;
      long[] arrayOfLong = mPastTime;
      int i = 0;
      float f1 = arrayOfFloat1[0];
      float f2 = arrayOfFloat2[0];
      long l = arrayOfLong[0];
      float f3 = 0.0F;
      float f4 = 0.0F;
      while ((i < 4) && (arrayOfLong[i] != 0L)) {
        i++;
      }
      for (int j = 1; j < i; j++)
      {
        int k = (int)(arrayOfLong[j] - l);
        if (k != 0)
        {
          float f5 = (arrayOfFloat1[j] - f1) / k * paramInt;
          if (f3 == 0.0F) {
            f3 = f5;
          } else {
            f3 = (f3 + f5) * 0.5F;
          }
          f5 = (arrayOfFloat2[j] - f2) / k * paramInt;
          if (f4 == 0.0F) {}
          for (f4 = f5;; f4 = (f4 + f5) * 0.5F) {
            break;
          }
        }
      }
      if (f3 < 0.0F) {
        f3 = Math.max(f3, -paramFloat);
      } else {
        f3 = Math.min(f3, paramFloat);
      }
      mXVelocity = f3;
      if (f4 < 0.0F) {
        paramFloat = Math.max(f4, -paramFloat);
      } else {
        paramFloat = Math.min(f4, paramFloat);
      }
      mYVelocity = paramFloat;
    }
    
    public float getXVelocity()
    {
      return mXVelocity;
    }
    
    public float getYVelocity()
    {
      return mYVelocity;
    }
  }
}
