package android.widget;

import android.R.styleable;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.RemoteAction;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.UndoManager;
import android.content.UndoOperation;
import android.content.UndoOwner;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.ParcelableParcel;
import android.os.SystemClock;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.WordIterator;
import android.text.style.EasyEditSpan;
import android.text.style.SuggestionRangeSpan;
import android.text.style.SuggestionSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ActionMode.Callback2;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.DisplayListCanvas;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.RenderNode;
import android.view.SubMenu;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.CursorAnchorInfo.Builder;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationConstants;
import android.view.textclassifier.TextClassificationManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.widget.EditableInputConnection;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Editor
{
  static final int BLINK = 500;
  private static final boolean DEBUG_UNDO = false;
  private static final int DRAG_SHADOW_MAX_TEXT_LENGTH = 20;
  static final int EXTRACT_NOTHING = -2;
  static final int EXTRACT_UNKNOWN = -1;
  private static final boolean FLAG_USE_MAGNIFIER = true;
  public static final int HANDLE_TYPE_SELECTION_END = 1;
  public static final int HANDLE_TYPE_SELECTION_START = 0;
  private static final float LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS = 0.5F;
  private static final int MENU_ITEM_ORDER_ASSIST = 0;
  private static final int MENU_ITEM_ORDER_AUTOFILL = 10;
  private static final int MENU_ITEM_ORDER_COPY = 5;
  private static final int MENU_ITEM_ORDER_CUT = 4;
  private static final int MENU_ITEM_ORDER_PASTE = 6;
  private static final int MENU_ITEM_ORDER_PASTE_AS_PLAIN_TEXT = 11;
  private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
  private static final int MENU_ITEM_ORDER_REDO = 3;
  private static final int MENU_ITEM_ORDER_REPLACE = 9;
  private static final int MENU_ITEM_ORDER_SECONDARY_ASSIST_ACTIONS_START = 50;
  private static final int MENU_ITEM_ORDER_SELECT_ALL = 8;
  private static final int MENU_ITEM_ORDER_SHARE = 7;
  private static final int MENU_ITEM_ORDER_UNDO = 2;
  private static final String TAG = "Editor";
  private static final int TAP_STATE_DOUBLE_TAP = 2;
  private static final int TAP_STATE_FIRST_TAP = 1;
  private static final int TAP_STATE_INITIAL = 0;
  private static final int TAP_STATE_TRIPLE_CLICK = 3;
  private static final String UNDO_OWNER_TAG = "Editor";
  private static final int UNSET_LINE = -1;
  private static final int UNSET_X_VALUE = -1;
  boolean mAllowUndo = true;
  private Blink mBlink;
  private float mContextMenuAnchorX;
  private float mContextMenuAnchorY;
  private CorrectionHighlighter mCorrectionHighlighter;
  boolean mCreatedWithASelection;
  private final CursorAnchorInfoNotifier mCursorAnchorInfoNotifier = new CursorAnchorInfoNotifier(null);
  private int mCursorHorizontalOffset;
  private boolean mCursorInLineEnd = false;
  boolean mCursorVisible = true;
  ActionMode.Callback mCustomInsertionActionModeCallback;
  ActionMode.Callback mCustomSelectionActionModeCallback;
  boolean mDiscardNextActionUp;
  Drawable mDrawableForCursor = null;
  CharSequence mError;
  private ErrorPopup mErrorPopup;
  boolean mErrorWasChanged;
  boolean mFrozenWithFocus;
  private final boolean mHapticTextHandleEnabled;
  boolean mIgnoreActionUpEvent;
  boolean mInBatchEditControllers;
  InputContentType mInputContentType;
  InputMethodState mInputMethodState;
  int mInputType = 0;
  private Runnable mInsertionActionModeRunnable;
  private boolean mInsertionControllerEnabled;
  private InsertionPointCursorController mInsertionPointCursorController;
  boolean mIsBeingLongClicked;
  boolean mIsInsertionActionModeStartPending = false;
  KeyListener mKeyListener;
  private int mLastButtonState;
  private float mLastDownPositionX;
  private float mLastDownPositionY;
  private long mLastTouchUpTime = 0L;
  private float mLastUpPositionX;
  private float mLastUpPositionY;
  private final MagnifierMotionAnimator mMagnifierAnimator;
  private final ViewTreeObserver.OnDrawListener mMagnifierOnDrawListener = new ViewTreeObserver.OnDrawListener()
  {
    public void onDraw()
    {
      if (mMagnifierAnimator != null) {
        mTextView.post(mUpdateMagnifierRunnable);
      }
    }
  };
  private final MetricsLogger mMetricsLogger = new MetricsLogger();
  private final MenuItem.OnMenuItemClickListener mOnContextMenuItemClickListener = new MenuItem.OnMenuItemClickListener()
  {
    public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
    {
      if (mProcessTextIntentActionsHandler.performMenuItemAction(paramAnonymousMenuItem)) {
        return true;
      }
      return mTextView.onTextContextMenuItem(paramAnonymousMenuItem.getItemId());
    }
  };
  private PositionListener mPositionListener;
  private boolean mPreserveSelection;
  final ProcessTextIntentActionsHandler mProcessTextIntentActionsHandler;
  private boolean mRenderCursorRegardlessTiming;
  private boolean mRequestingLinkActionMode;
  private boolean mRestartActionModeOnNextRefresh;
  boolean mSelectAllOnFocus;
  private Drawable mSelectHandleCenter;
  private Drawable mSelectHandleLeft;
  private Drawable mSelectHandleRight;
  private SelectionActionModeHelper mSelectionActionModeHelper;
  private boolean mSelectionControllerEnabled;
  SelectionModifierCursorController mSelectionModifierCursorController;
  boolean mSelectionMoved;
  private long mShowCursor;
  private boolean mShowErrorAfterAttach;
  private final Runnable mShowFloatingToolbar = new Runnable()
  {
    public void run()
    {
      if (mTextActionMode != null) {
        mTextActionMode.hide(0L);
      }
    }
  };
  boolean mShowSoftInputOnFocus = true;
  private Runnable mShowSuggestionRunnable;
  private SpanController mSpanController;
  SpellChecker mSpellChecker;
  private final SuggestionHelper mSuggestionHelper = new SuggestionHelper(null);
  SuggestionRangeSpan mSuggestionRangeSpan;
  private SuggestionsPopupWindow mSuggestionsPopupWindow;
  private int mTapState = 0;
  private Rect mTempRect;
  private ActionMode mTextActionMode;
  boolean mTextIsSelectable;
  private TextRenderNode[] mTextRenderNodes;
  private final TextView mTextView;
  boolean mTouchFocusSelected;
  final UndoInputFilter mUndoInputFilter = new UndoInputFilter(this);
  private final UndoManager mUndoManager = new UndoManager();
  private UndoOwner mUndoOwner = mUndoManager.getOwner("Editor", this);
  private final Runnable mUpdateMagnifierRunnable = new Runnable()
  {
    public void run()
    {
      Editor.MagnifierMotionAnimator.access$100(mMagnifierAnimator);
    }
  };
  private boolean mUpdateWordIteratorText;
  private WordIterator mWordIterator;
  private WordIterator mWordIteratorWithText;
  
  Editor(TextView paramTextView)
  {
    mTextView = paramTextView;
    mTextView.setFilters(mTextView.getFilters());
    mProcessTextIntentActionsHandler = new ProcessTextIntentActionsHandler(this, null);
    mHapticTextHandleEnabled = mTextView.getContext().getResources().getBoolean(17956957);
    mMagnifierAnimator = new MagnifierMotionAnimator(new Magnifier(mTextView), null);
  }
  
  private int calculateTheFakeOffset(float paramFloat1, float paramFloat2)
  {
    int i = mTextView.getOffsetForPosition(paramFloat1, paramFloat2);
    return calculateTheFakeOffsetInternal(mTextView.getLayout().getLineForOffset(i), paramFloat1, i);
  }
  
  private int calculateTheFakeOffsetAdjustedForSlop(int paramInt, float paramFloat)
  {
    return calculateTheFakeOffsetInternal(paramInt, paramFloat, mTextView.getOffsetAtCoordinate(paramInt, paramFloat));
  }
  
  private int calculateTheFakeOffsetInternal(int paramInt1, float paramFloat, int paramInt2)
  {
    int i = mTextView.getLayout().getLineForOffset(paramInt2 + 1);
    boolean bool1 = false;
    if (paramInt1 != i) {
      i = 1;
    } else {
      i = 0;
    }
    if ((paramFloat > mTextView.getLayout().getLineRight(paramInt1)) && (i != 0)) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    }
    if ((paramInt2 < mTextView.getText().toString().length()) && ("\n".equals(mTextView.getText().toString().substring(paramInt2, paramInt2 + 1)))) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if ((paramInt2 < mTextView.getText().toString().length()) && (" ".equals(mTextView.getText().toString().substring(paramInt2, paramInt2 + 1)))) {
      j = 1;
    } else {
      j = 0;
    }
    boolean bool2 = bool1;
    if (paramInt1 != 0)
    {
      bool2 = bool1;
      if (i == 0)
      {
        bool2 = bool1;
        if (j == 0) {
          bool2 = true;
        }
      }
    }
    mCursorInLineEnd = bool2;
    if (mCursorInLineEnd) {
      paramInt2++;
    }
    return paramInt2;
  }
  
  private void chooseSize(PopupWindow paramPopupWindow, CharSequence paramCharSequence, TextView paramTextView)
  {
    int i = paramTextView.getPaddingLeft();
    int j = paramTextView.getPaddingRight();
    int k = paramTextView.getPaddingTop();
    int m = paramTextView.getPaddingBottom();
    int n = mTextView.getResources().getDimensionPixelSize(17105458);
    int i1 = paramCharSequence.length();
    TextPaint localTextPaint = paramTextView.getPaint();
    int i2 = 0;
    paramCharSequence = StaticLayout.Builder.obtain(paramCharSequence, 0, i1, localTextPaint, n).setUseLineSpacingFromFallbacks(mUseFallbackLineSpacing).build();
    float f = 0.0F;
    while (i2 < paramCharSequence.getLineCount())
    {
      f = Math.max(f, paramCharSequence.getLineWidth(i2));
      i2++;
    }
    paramPopupWindow.setWidth((int)Math.ceil(f) + (i + j));
    paramPopupWindow.setHeight(paramCharSequence.getHeight() + (k + m));
  }
  
  private int clampHorizontalPosition(Drawable paramDrawable, float paramFloat)
  {
    float f = Math.max(0.5F, paramFloat - 0.5F);
    if (mTempRect == null) {
      mTempRect = new Rect();
    }
    int i = 0;
    if (paramDrawable != null)
    {
      paramDrawable.getPadding(mTempRect);
      i = paramDrawable.getIntrinsicWidth();
    }
    else
    {
      mTempRect.setEmpty();
    }
    int j = mTextView.getScrollX();
    paramFloat = f - j;
    int k = mTextView.getWidth() - mTextView.getCompoundPaddingLeft() - mTextView.getCompoundPaddingRight();
    if (paramFloat >= k - 1.0F) {}
    for (i = k + j - (i - mTempRect.right);; i = j - mTempRect.left)
    {
      break;
      if ((Math.abs(paramFloat) > 1.0F) && ((!TextUtils.isEmpty(mTextView.getText())) || (1048576 - j > k + 1.0F) || (f > 1.0F)))
      {
        i = (int)f - mTempRect.left;
        break;
      }
    }
    return i;
  }
  
  private void discardTextDisplayLists()
  {
    if (mTextRenderNodes != null) {
      for (int i = 0; i < mTextRenderNodes.length; i++)
      {
        RenderNode localRenderNode;
        if (mTextRenderNodes[i] != null) {
          localRenderNode = mTextRenderNodes[i].renderNode;
        } else {
          localRenderNode = null;
        }
        if ((localRenderNode != null) && (localRenderNode.isValid())) {
          localRenderNode.discardDisplayList();
        }
      }
    }
  }
  
  private void downgradeEasyCorrectionSpans()
  {
    Object localObject = mTextView.getText();
    if ((localObject instanceof Spannable))
    {
      localObject = (Spannable)localObject;
      int i = ((Spannable)localObject).length();
      int j = 0;
      localObject = (SuggestionSpan[])((Spannable)localObject).getSpans(0, i, SuggestionSpan.class);
      while (j < localObject.length)
      {
        i = localObject[j].getFlags();
        if (((i & 0x1) != 0) && ((i & 0x2) == 0)) {
          localObject[j].setFlags(i & 0xFFFFFFFE);
        }
        j++;
      }
    }
  }
  
  private void drawCursor(Canvas paramCanvas, int paramInt)
  {
    int i;
    if (paramInt != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      paramCanvas.translate(0.0F, paramInt);
    }
    if (mDrawableForCursor != null) {
      mDrawableForCursor.draw(paramCanvas);
    }
    if (i != 0) {
      paramCanvas.translate(0.0F, -paramInt);
    }
  }
  
  private void drawHardwareAccelerated(Canvas paramCanvas, Layout paramLayout, Path paramPath, Paint paramPaint, int paramInt)
  {
    Object localObject1 = this;
    Object localObject2 = paramCanvas;
    Object localObject3 = paramLayout;
    long l = ((Layout)localObject3).getLineRangeForDraw((Canvas)localObject2);
    int i = TextUtils.unpackRangeStartFromLong(l);
    int j = TextUtils.unpackRangeEndFromLong(l);
    if (j < 0) {
      return;
    }
    ((Layout)localObject3).drawBackground((Canvas)localObject2, paramPath, paramPaint, paramInt, i, j);
    if ((localObject3 instanceof DynamicLayout))
    {
      if (mTextRenderNodes == null) {
        mTextRenderNodes = ((TextRenderNode[])ArrayUtils.emptyArray(TextRenderNode.class));
      }
      localObject2 = (DynamicLayout)localObject3;
      int[] arrayOfInt1 = ((DynamicLayout)localObject2).getBlockEndLines();
      int[] arrayOfInt2 = ((DynamicLayout)localObject2).getBlockIndices();
      int k = ((DynamicLayout)localObject2).getNumberOfBlocks();
      int m = ((DynamicLayout)localObject2).getIndexFirstChangedBlock();
      localObject3 = ((DynamicLayout)localObject2).getBlocksAlwaysNeedToBeRedrawn();
      int n = 1;
      if (localObject3 != null) {
        for (i1 = 0; i1 < ((ArraySet)localObject3).size(); i1++)
        {
          i2 = ((DynamicLayout)localObject2).getBlockIndex(((Integer)((ArraySet)localObject3).valueAt(i1)).intValue());
          if ((i2 != -1) && (mTextRenderNodes[i2] != null)) {
            mTextRenderNodes[i2].needsToBeShifted = true;
          }
        }
      }
      int i3 = 0;
      int i2 = Arrays.binarySearch(arrayOfInt1, 0, k, i);
      int i1 = i2;
      if (i2 < 0) {
        i1 = -(i2 + 1);
      }
      i2 = Math.min(m, i1);
      i1 = 0;
      int i4 = k;
      while (i2 < i4)
      {
        i3 = arrayOfInt2[i2];
        if (i2 >= m) {
          if ((i3 != -1) && (mTextRenderNodes[i3] != null))
          {
            localObject4 = mTextRenderNodes[i3];
            n = 1;
            needsToBeShifted = true;
          }
          else
          {
            n = 1;
          }
        }
        if (arrayOfInt1[i2] >= i)
        {
          int i5 = 0;
          int i6 = i2;
          i3 = ((Editor)localObject1).drawHardwareAcceleratedInner(paramCanvas, paramLayout, paramPath, paramPaint, paramInt, arrayOfInt1, arrayOfInt2, i6, i4, i1);
          i1 = i3;
          if (arrayOfInt1[i6] >= j)
          {
            i1 = Math.max(m, i6 + 1);
            i2 = i3;
            i3 = i5;
            break label411;
          }
        }
        i2++;
        i3 = 0;
      }
      i2 = i1;
      i1 = k;
      label411:
      localObject1 = localObject2;
      k = i1;
      i = j;
      Object localObject5 = localObject1;
      i = m;
      Object localObject4 = localObject3;
      if (localObject3 != null)
      {
        localObject2 = localObject3;
        localObject3 = localObject1;
        for (;;)
        {
          localObject1 = this;
          k = i1;
          i = j;
          localObject5 = localObject3;
          i = m;
          localObject4 = localObject2;
          if (i3 >= ((ArraySet)localObject2).size()) {
            break;
          }
          k = ((Integer)((ArraySet)localObject2).valueAt(i3)).intValue();
          i = ((DynamicLayout)localObject3).getBlockIndex(k);
          if ((i != -1) && (mTextRenderNodes[i] != null) && (!mTextRenderNodes[i].needsToBeShifted)) {
            break label566;
          }
          i2 = ((Editor)localObject1).drawHardwareAcceleratedInner(paramCanvas, paramLayout, paramPath, paramPaint, paramInt, arrayOfInt1, arrayOfInt2, k, i4, i2);
          label566:
          i3++;
        }
      }
      localObject5.setIndexFirstChangedBlock(k);
    }
    else
    {
      ((Layout)localObject3).drawText((Canvas)localObject2, i, j);
    }
  }
  
  private int drawHardwareAcceleratedInner(Canvas paramCanvas, Layout paramLayout, Path paramPath, Paint paramPaint, int paramInt1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramArrayOfInt1[paramInt2];
    int j = paramArrayOfInt2[paramInt2];
    if (j == -1) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    }
    if (paramInt1 != 0)
    {
      paramInt3 = getAvailableDisplayListIndex(paramArrayOfInt2, paramInt3, paramInt4);
      paramArrayOfInt2[paramInt2] = paramInt3;
      if (mTextRenderNodes[paramInt3] != null) {
        mTextRenderNodes[paramInt3].isDirty = true;
      }
      paramInt4 = paramInt3 + 1;
    }
    else
    {
      paramInt3 = j;
    }
    if (mTextRenderNodes[paramInt3] == null)
    {
      paramPath = mTextRenderNodes;
      paramPaint = new StringBuilder();
      paramPaint.append("Text ");
      paramPaint.append(paramInt3);
      paramPath[paramInt3] = new TextRenderNode(paramPaint.toString());
    }
    boolean bool = mTextRenderNodes[paramInt3].needsRecord();
    paramPath = mTextRenderNodes[paramInt3].renderNode;
    if ((!mTextRenderNodes[paramInt3].needsToBeShifted) && (!bool)) {
      break label437;
    }
    if (paramInt2 == 0) {
      paramInt1 = 0;
    } else {
      paramInt1 = paramArrayOfInt1[(paramInt2 - 1)] + 1;
    }
    int k = paramLayout.getLineTop(paramInt1);
    int m = paramLayout.getLineBottom(i);
    j = mTextView.getWidth();
    float f1;
    float f2;
    if (mTextView.getHorizontallyScrolling())
    {
      f1 = Float.MAX_VALUE;
      paramInt2 = paramInt1;
      f2 = Float.MIN_VALUE;
      while (paramInt2 <= i)
      {
        f1 = Math.min(f1, paramLayout.getLineLeft(paramInt2));
        f2 = Math.max(f2, paramLayout.getLineRight(paramInt2));
        paramInt2++;
      }
      paramInt2 = (int)f1;
      j = (int)(0.5F + f2);
    }
    else
    {
      paramInt2 = 0;
    }
    if (bool)
    {
      paramPaint = paramPath.start(j - paramInt2, m - k);
      f2 = -paramInt2;
      f1 = -k;
    }
    try
    {
      paramPaint.translate(f2, f1);
      paramLayout.drawText(paramPaint, paramInt1, i);
      mTextRenderNodes[paramInt3].isDirty = false;
      paramPath.end(paramPaint);
      paramPath.setClipToBounds(false);
    }
    finally
    {
      paramPath.end(paramPaint);
      paramPath.setClipToBounds(false);
    }
    label437:
    ((DisplayListCanvas)paramCanvas).drawRenderNode(paramPath);
    return paramInt4;
  }
  
  private void ensureNoSelectionIfNonSelectable()
  {
    if ((!mTextView.textCanBeSelected()) && (mTextView.hasSelection())) {
      Selection.setSelection((Spannable)mTextView.getText(), mTextView.length(), mTextView.length());
    }
  }
  
  private boolean extractTextInternal(ExtractedTextRequest paramExtractedTextRequest, int paramInt1, int paramInt2, int paramInt3, ExtractedText paramExtractedText)
  {
    if ((paramExtractedTextRequest != null) && (paramExtractedText != null))
    {
      CharSequence localCharSequence = mTextView.getText();
      if (localCharSequence == null) {
        return false;
      }
      if (paramInt1 != -2)
      {
        int i = localCharSequence.length();
        if (paramInt1 < 0)
        {
          partialEndOffset = -1;
          partialStartOffset = -1;
          paramInt3 = 0;
          paramInt2 = i;
        }
        else
        {
          paramInt2 += paramInt3;
          int j = paramInt1;
          int k = paramInt2;
          if ((localCharSequence instanceof Spanned))
          {
            Spanned localSpanned = (Spanned)localCharSequence;
            Object[] arrayOfObject = localSpanned.getSpans(paramInt1, paramInt2, ParcelableSpan.class);
            int m = arrayOfObject.length;
            for (;;)
            {
              j = paramInt1;
              k = paramInt2;
              if (m <= 0) {
                break;
              }
              m--;
              j = localSpanned.getSpanStart(arrayOfObject[m]);
              k = paramInt1;
              if (j < paramInt1) {
                k = j;
              }
              paramInt1 = localSpanned.getSpanEnd(arrayOfObject[m]);
              j = paramInt2;
              if (paramInt1 > paramInt2) {
                j = paramInt1;
              }
              paramInt1 = k;
              paramInt2 = j;
            }
          }
          partialStartOffset = j;
          partialEndOffset = (k - paramInt3);
          if (j > i)
          {
            paramInt1 = i;
          }
          else
          {
            paramInt1 = j;
            if (j < 0) {
              paramInt1 = 0;
            }
          }
          if (k > i)
          {
            paramInt2 = i;
            paramInt3 = paramInt1;
          }
          else
          {
            paramInt3 = paramInt1;
            paramInt2 = k;
            if (k < 0)
            {
              paramInt2 = 0;
              paramInt3 = paramInt1;
            }
          }
        }
        if ((flags & 0x1) != 0) {
          text = localCharSequence.subSequence(paramInt3, paramInt2);
        } else {
          text = TextUtils.substring(localCharSequence, paramInt3, paramInt2);
        }
      }
      else
      {
        partialStartOffset = 0;
        partialEndOffset = 0;
        text = "";
      }
      flags = 0;
      if (MetaKeyKeyListener.getMetaState(localCharSequence, 2048) != 0) {
        flags |= 0x2;
      }
      if (mTextView.isSingleLine()) {
        flags |= 0x1;
      }
      startOffset = 0;
      selectionStart = mTextView.getSelectionStart();
      selectionEnd = mTextView.getSelectionEnd();
      hint = mTextView.getHint();
      return true;
    }
    return false;
  }
  
  private boolean extractedTextModeWillBeStarted()
  {
    boolean bool1 = mTextView.isInExtractedMode();
    boolean bool2 = false;
    if (!bool1)
    {
      InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
      bool1 = bool2;
      if (localInputMethodManager != null)
      {
        bool1 = bool2;
        if (localInputMethodManager.isFullscreenMode()) {
          bool1 = true;
        }
      }
      return bool1;
    }
    return false;
  }
  
  private SuggestionSpan findEquivalentSuggestionSpan(SuggestionSpanInfo paramSuggestionSpanInfo)
  {
    Editable localEditable = (Editable)mTextView.getText();
    if (localEditable.getSpanStart(mSuggestionSpan) >= 0) {
      return mSuggestionSpan;
    }
    for (SuggestionSpan localSuggestionSpan : (SuggestionSpan[])localEditable.getSpans(mSpanStart, mSpanEnd, SuggestionSpan.class)) {
      if ((localEditable.getSpanStart(localSuggestionSpan) == mSpanStart) && (localEditable.getSpanEnd(localSuggestionSpan) == mSpanEnd) && (localSuggestionSpan.equals(mSuggestionSpan))) {
        return localSuggestionSpan;
      }
    }
    return null;
  }
  
  private int getAvailableDisplayListIndex(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int i = mTextRenderNodes.length;
    while (paramInt2 < i)
    {
      int j = 0;
      int m;
      for (int k = 0;; k++)
      {
        m = j;
        if (k >= paramInt1) {
          break;
        }
        if (paramArrayOfInt[k] == paramInt2)
        {
          m = 1;
          break;
        }
      }
      if (m != 0) {
        paramInt2++;
      } else {
        return paramInt2;
      }
    }
    mTextRenderNodes = ((TextRenderNode[])GrowingArrayUtils.append(mTextRenderNodes, i, null));
    return i;
  }
  
  private long getCharClusterRange(int paramInt)
  {
    if (paramInt < mTextView.getText().length())
    {
      paramInt = getNextCursorOffset(paramInt, true);
      return TextUtils.packRangeInLong(getNextCursorOffset(paramInt, false), paramInt);
    }
    if (paramInt - 1 >= 0)
    {
      paramInt = getNextCursorOffset(paramInt, false);
      return TextUtils.packRangeInLong(paramInt, getNextCursorOffset(paramInt, true));
    }
    return TextUtils.packRangeInLong(paramInt, paramInt);
  }
  
  private int getCurrentLineAdjustedForSlop(Layout paramLayout, int paramInt, float paramFloat)
  {
    int i = mTextView.getLineAtCoordinate(paramFloat);
    if ((paramLayout != null) && (paramInt <= paramLayout.getLineCount()) && (paramLayout.getLineCount() > 0) && (paramInt >= 0))
    {
      if (Math.abs(i - paramInt) >= 2) {
        return i;
      }
      float f1 = mTextView.viewportToContentVerticalOffset();
      i = paramLayout.getLineCount();
      float f2 = mTextView.getLineHeight() * 0.5F;
      float f3 = paramLayout.getLineTop(0);
      f3 = Math.max(paramLayout.getLineTop(paramInt) + f1 - f2, f3 + f1 + f2);
      float f4 = paramLayout.getLineBottom(i - 1);
      f2 = Math.min(paramLayout.getLineBottom(paramInt) + f1 + f2, f4 + f1 - f2);
      if (paramFloat <= f3) {}
      for (paramInt = Math.max(paramInt - 1, 0);; paramInt = Math.min(paramInt + 1, i - 1))
      {
        break;
        if (paramFloat < f2) {
          break;
        }
      }
      return paramInt;
    }
    return i;
  }
  
  private int getErrorX()
  {
    float f = mTextView.getResources().getDisplayMetrics().density;
    TextView.Drawables localDrawables = mTextView.mDrawables;
    int i = mTextView.getLayoutDirection();
    int j = 0;
    int k = 0;
    if (i != 1)
    {
      if (localDrawables != null) {
        k = mDrawableSizeRight;
      }
      k = -k / 2;
      j = (int)(25.0F * f + 0.5F);
      k = mTextView.getWidth() - mErrorPopup.getWidth() - mTextView.getPaddingRight() + (k + j);
    }
    else
    {
      k = j;
      if (localDrawables != null) {
        k = mDrawableSizeLeft;
      }
      k /= 2;
      j = (int)(25.0F * f + 0.5F);
      k = mTextView.getPaddingLeft() + (k - j);
    }
    return k;
  }
  
  private int getErrorY()
  {
    int i = mTextView.getCompoundPaddingTop();
    int j = mTextView.getBottom();
    int k = mTextView.getTop();
    int m = mTextView.getCompoundPaddingBottom();
    TextView.Drawables localDrawables = mTextView.mDrawables;
    int n = mTextView.getLayoutDirection();
    int i1 = 0;
    int i2 = 0;
    if (n != 1)
    {
      if (localDrawables != null) {
        i2 = mDrawableHeightRight;
      }
    }
    else
    {
      i2 = i1;
      if (localDrawables != null) {
        i2 = mDrawableHeightLeft;
      }
    }
    i1 = (j - k - m - i - i2) / 2;
    float f = mTextView.getResources().getDisplayMetrics().density;
    return i1 + i + i2 - mTextView.getHeight() - (int)(2.0F * f + 0.5F);
  }
  
  private InsertionPointCursorController getInsertionController()
  {
    if (!mInsertionControllerEnabled) {
      return null;
    }
    if (mInsertionPointCursorController == null)
    {
      mInsertionPointCursorController = new InsertionPointCursorController(null);
      mTextView.getViewTreeObserver().addOnTouchModeChangeListener(mInsertionPointCursorController);
    }
    return mInsertionPointCursorController;
  }
  
  private int getLastTapPosition()
  {
    if (mSelectionModifierCursorController != null)
    {
      int i = mSelectionModifierCursorController.getMinTouchOffset();
      if (i >= 0)
      {
        int j = i;
        if (i > mTextView.getText().length()) {
          j = mTextView.getText().length();
        }
        return j;
      }
    }
    return -1;
  }
  
  private long getLastTouchOffsets()
  {
    SelectionModifierCursorController localSelectionModifierCursorController = getSelectionController();
    return TextUtils.packRangeInLong(localSelectionModifierCursorController.getMinTouchOffset(), localSelectionModifierCursorController.getMaxTouchOffset());
  }
  
  private int getNextCursorOffset(int paramInt, boolean paramBoolean)
  {
    Layout localLayout = mTextView.getLayout();
    if (localLayout == null) {
      return paramInt;
    }
    if (paramBoolean == localLayout.isRtlCharAt(paramInt)) {
      paramInt = localLayout.getOffsetToLeftOf(paramInt);
    } else {
      paramInt = localLayout.getOffsetToRightOf(paramInt);
    }
    return paramInt;
  }
  
  private long getParagraphsRange(int paramInt1, int paramInt2)
  {
    Layout localLayout = mTextView.getLayout();
    if (localLayout == null) {
      return TextUtils.packRangeInLong(-1, -1);
    }
    CharSequence localCharSequence = mTextView.getText();
    for (paramInt1 = localLayout.getLineForOffset(paramInt1); (paramInt1 > 0) && (localCharSequence.charAt(localLayout.getLineEnd(paramInt1 - 1) - 1) != '\n'); paramInt1--) {}
    for (paramInt2 = localLayout.getLineForOffset(paramInt2); (paramInt2 < localLayout.getLineCount() - 1) && (localCharSequence.charAt(localLayout.getLineEnd(paramInt2) - 1) != '\n'); paramInt2++) {}
    return TextUtils.packRangeInLong(localLayout.getLineStart(paramInt1), localLayout.getLineEnd(paramInt2));
  }
  
  private PositionListener getPositionListener()
  {
    if (mPositionListener == null) {
      mPositionListener = new PositionListener(null);
    }
    return mPositionListener;
  }
  
  private SelectionActionModeHelper getSelectionActionModeHelper()
  {
    if (mSelectionActionModeHelper == null) {
      mSelectionActionModeHelper = new SelectionActionModeHelper(this);
    }
    return mSelectionActionModeHelper;
  }
  
  private View.DragShadowBuilder getTextThumbnailBuilder(int paramInt1, int paramInt2)
  {
    TextView localTextView = (TextView)View.inflate(mTextView.getContext(), 17367329, null);
    if (localTextView != null)
    {
      int i = paramInt2;
      if (paramInt2 - paramInt1 > 20) {
        i = TextUtils.unpackRangeEndFromLong(getCharClusterRange(paramInt1 + 20));
      }
      localTextView.setText(mTextView.getTransformedText(paramInt1, i));
      localTextView.setTextColor(mTextView.getTextColors());
      localTextView.setTextAppearance(16);
      localTextView.setGravity(17);
      localTextView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
      paramInt1 = View.MeasureSpec.makeMeasureSpec(0, 0);
      localTextView.measure(paramInt1, paramInt1);
      localTextView.layout(0, 0, localTextView.getMeasuredWidth(), localTextView.getMeasuredHeight());
      localTextView.invalidate();
      return new View.DragShadowBuilder(localTextView);
    }
    throw new IllegalArgumentException("Unable to inflate text drag thumbnail");
  }
  
  private int getWordEnd(int paramInt)
  {
    int i = getWordIteratorWithText().nextBoundary(paramInt);
    if (getWordIteratorWithText().isAfterPunctuation(i)) {
      i = getWordIteratorWithText().getPunctuationEnd(paramInt);
    } else {
      i = getWordIteratorWithText().getNextWordEndOnTwoWordBoundary(paramInt);
    }
    if (i == -1) {
      return paramInt;
    }
    return i;
  }
  
  private WordIterator getWordIteratorWithText()
  {
    if (mWordIteratorWithText == null)
    {
      mWordIteratorWithText = new WordIterator(mTextView.getTextServicesLocale());
      mUpdateWordIteratorText = true;
    }
    if (mUpdateWordIteratorText)
    {
      CharSequence localCharSequence = mTextView.getText();
      mWordIteratorWithText.setCharSequence(localCharSequence, 0, localCharSequence.length());
      mUpdateWordIteratorText = false;
    }
    return mWordIteratorWithText;
  }
  
  private int getWordStart(int paramInt)
  {
    int i = getWordIteratorWithText().prevBoundary(paramInt);
    if (getWordIteratorWithText().isOnPunctuation(i)) {
      i = getWordIteratorWithText().getPunctuationBeginning(paramInt);
    } else {
      i = getWordIteratorWithText().getPrevWordBeginningOnTwoWordsBoundary(paramInt);
    }
    if (i == -1) {
      return paramInt;
    }
    return i;
  }
  
  private void hideCursorControllers()
  {
    if ((mSuggestionsPopupWindow != null) && ((mTextView.isInExtractedMode()) || (!mSuggestionsPopupWindow.isShowingUp()))) {
      mSuggestionsPopupWindow.hide();
    }
    hideInsertionPointCursorController();
  }
  
  private void hideError()
  {
    if ((mErrorPopup != null) && (mErrorPopup.isShowing())) {
      mErrorPopup.dismiss();
    }
    mShowErrorAfterAttach = false;
  }
  
  private void hideSpanControllers()
  {
    if (mSpanController != null) {
      mSpanController.hide();
    }
  }
  
  private void invalidateActionMode()
  {
    if (mTextActionMode != null) {
      mTextActionMode.invalidate();
    }
  }
  
  private boolean isCursorInsideEasyCorrectionSpan()
  {
    SuggestionSpan[] arrayOfSuggestionSpan = (SuggestionSpan[])((Spannable)mTextView.getText()).getSpans(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), SuggestionSpan.class);
    for (int i = 0; i < arrayOfSuggestionSpan.length; i++) {
      if ((arrayOfSuggestionSpan[i].getFlags() & 0x1) != 0) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isCursorVisible()
  {
    boolean bool;
    if ((mCursorVisible) && (mTextView.isTextEditable())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isOffsetVisible(int paramInt)
  {
    Layout localLayout = mTextView.getLayout();
    if (localLayout == null) {
      return false;
    }
    int i = localLayout.getLineBottom(localLayout.getLineForOffset(paramInt));
    paramInt = (int)localLayout.getPrimaryHorizontal(paramInt);
    return mTextView.isPositionVisible(mTextView.viewportToContentHorizontalOffset() + paramInt, mTextView.viewportToContentVerticalOffset() + i);
  }
  
  private boolean isPositionOnText(float paramFloat1, float paramFloat2)
  {
    Layout localLayout = mTextView.getLayout();
    if (localLayout == null) {
      return false;
    }
    int i = mTextView.getLineAtCoordinate(paramFloat2);
    paramFloat1 = mTextView.convertToLocalHorizontalCoordinate(paramFloat1);
    if (paramFloat1 < localLayout.getLineLeft(i)) {
      return false;
    }
    return paramFloat1 <= localLayout.getLineRight(i);
  }
  
  private static boolean isValidRange(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 >= 0) && (paramInt1 <= paramInt2) && (paramInt2 <= paramCharSequence.length())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean needsToSelectAllToSelectWordOrParagraph()
  {
    if (mTextView.hasPasswordTransformationMethod()) {
      return true;
    }
    int i = mTextView.getInputType();
    int j = i & 0xF;
    i &= 0xFF0;
    return (j == 2) || (j == 3) || (j == 4) || (i == 16) || (i == 32) || (i == 208) || (i == 176);
  }
  
  private void replaceWithSuggestion(SuggestionInfo paramSuggestionInfo)
  {
    SuggestionSpan localSuggestionSpan = findEquivalentSuggestionSpan(mSuggestionSpanInfo);
    if (localSuggestionSpan == null) {
      return;
    }
    Editable localEditable = (Editable)mTextView.getText();
    int i = localEditable.getSpanStart(localSuggestionSpan);
    int j = localEditable.getSpanEnd(localSuggestionSpan);
    if ((i >= 0) && (j > i))
    {
      String str = TextUtils.substring(localEditable, i, j);
      SuggestionSpan[] arrayOfSuggestionSpan = (SuggestionSpan[])localEditable.getSpans(i, j, SuggestionSpan.class);
      int k = arrayOfSuggestionSpan.length;
      int[] arrayOfInt1 = new int[k];
      int[] arrayOfInt2 = new int[k];
      int[] arrayOfInt3 = new int[k];
      for (int m = 0; m < k; m++)
      {
        localObject = arrayOfSuggestionSpan[m];
        arrayOfInt1[m] = localEditable.getSpanStart(localObject);
        arrayOfInt2[m] = localEditable.getSpanEnd(localObject);
        arrayOfInt3[m] = localEditable.getSpanFlags(localObject);
        n = ((SuggestionSpan)localObject).getFlags();
        if ((n & 0x2) != 0) {
          ((SuggestionSpan)localObject).setFlags(n & 0xFFFFFFFD & 0xFFFFFFFE);
        }
      }
      localSuggestionSpan.notifySelection(mTextView.getContext(), str, mSuggestionIndex);
      m = mSuggestionStart;
      int n = mSuggestionEnd;
      Object localObject = mText.subSequence(m, n).toString();
      mTextView.replaceText_internal(i, j, (CharSequence)localObject);
      localSuggestionSpan.getSuggestions()[mSuggestionIndex] = str;
      int i1 = ((String)localObject).length() - (j - i);
      n = 0;
      paramSuggestionInfo = arrayOfInt1;
      m = k;
      while (n < m)
      {
        if ((paramSuggestionInfo[n] <= i) && (arrayOfInt2[n] >= j)) {
          mTextView.setSpan_internal(arrayOfSuggestionSpan[n], paramSuggestionInfo[n], Math.min(arrayOfInt2[n] + i1, localEditable.length()), arrayOfInt3[n]);
        }
        n++;
      }
      m = Math.min(j + i1, localEditable.length());
      mTextView.setCursorPosition_internal(m, m);
      return;
    }
  }
  
  private void resumeBlink()
  {
    if (mBlink != null)
    {
      mBlink.uncancel();
      makeBlink();
    }
  }
  
  private boolean selectCurrentParagraph()
  {
    if (!mTextView.canSelectText()) {
      return false;
    }
    if (needsToSelectAllToSelectWordOrParagraph()) {
      return mTextView.selectAllText();
    }
    long l = getLastTouchOffsets();
    l = getParagraphsRange(TextUtils.unpackRangeStartFromLong(l), TextUtils.unpackRangeEndFromLong(l));
    int i = TextUtils.unpackRangeStartFromLong(l);
    int j = TextUtils.unpackRangeEndFromLong(l);
    if (i < j)
    {
      Selection.setSelection((Spannable)mTextView.getText(), i, j);
      return true;
    }
    return false;
  }
  
  private boolean selectCurrentWordAndStartDrag()
  {
    if (mInsertionActionModeRunnable != null) {
      mTextView.removeCallbacks(mInsertionActionModeRunnable);
    }
    if (extractedTextModeWillBeStarted()) {
      return false;
    }
    if (!checkField()) {
      return false;
    }
    if ((!mTextView.hasSelection()) && (!selectCurrentWord())) {
      return false;
    }
    stopTextActionModeWithPreservingSelection();
    getSelectionController().enterDrag(2);
    return true;
  }
  
  private void sendUpdateSelection()
  {
    if ((mInputMethodState != null) && (mInputMethodState.mBatchEditNesting <= 0))
    {
      InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
      if (localInputMethodManager != null)
      {
        int i = mTextView.getSelectionStart();
        int j = mTextView.getSelectionEnd();
        int k = -1;
        int m = -1;
        if ((mTextView.getText() instanceof Spannable))
        {
          Spannable localSpannable = (Spannable)mTextView.getText();
          k = EditableInputConnection.getComposingSpanStart(localSpannable);
          m = EditableInputConnection.getComposingSpanEnd(localSpannable);
        }
        localInputMethodManager.updateSelection(mTextView, i, j, k, m);
      }
    }
  }
  
  private void setErrorIcon(Drawable paramDrawable)
  {
    TextView.Drawables localDrawables1 = mTextView.mDrawables;
    TextView.Drawables localDrawables2 = localDrawables1;
    if (localDrawables1 == null)
    {
      TextView localTextView = mTextView;
      localDrawables1 = new TextView.Drawables(mTextView.getContext());
      localDrawables2 = localDrawables1;
      mDrawables = localDrawables1;
    }
    localDrawables2.setErrorDrawable(paramDrawable, mTextView);
    mTextView.resetResolvedDrawables();
    mTextView.invalidate();
    mTextView.requestLayout();
  }
  
  private boolean shouldBlink()
  {
    boolean bool1 = isCursorVisible();
    boolean bool2 = false;
    if ((bool1) && (mTextView.isFocused()))
    {
      int i = mTextView.getSelectionStart();
      if (i < 0) {
        return false;
      }
      int j = mTextView.getSelectionEnd();
      if (j < 0) {
        return false;
      }
      if (i == j) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  private boolean shouldFilterOutTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!paramMotionEvent.isFromSource(8194)) {
      return false;
    }
    int i;
    if (((mLastButtonState ^ paramMotionEvent.getButtonState()) & 0x1) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int j = paramMotionEvent.getActionMasked();
    if (((j == 0) || (j == 1)) && (i == 0)) {
      return true;
    }
    return (j == 2) && (!paramMotionEvent.isButtonPressed(1));
  }
  
  private boolean shouldOfferToShowSuggestions()
  {
    Object localObject = mTextView.getText();
    if (!(localObject instanceof Spannable)) {
      return false;
    }
    Spannable localSpannable = (Spannable)localObject;
    int i = mTextView.getSelectionStart();
    int j = mTextView.getSelectionEnd();
    localObject = (SuggestionSpan[])localSpannable.getSpans(i, j, SuggestionSpan.class);
    if (localObject.length == 0) {
      return false;
    }
    if (i == j)
    {
      for (j = 0; j < localObject.length; j++) {
        if (localObject[j].getSuggestions().length > 0) {
          return true;
        }
      }
      return false;
    }
    int k = mTextView.getText().length();
    int m = mTextView.getText().length();
    j = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    while (i2 < localObject.length)
    {
      int i3 = localSpannable.getSpanStart(localObject[i2]);
      int i4 = localSpannable.getSpanEnd(localObject[i2]);
      k = Math.min(k, i3);
      i1 = Math.max(i1, i4);
      int i5 = m;
      int i6 = j;
      int i7 = n;
      if (i >= i3) {
        if (i > i4)
        {
          i5 = m;
          i6 = j;
          i7 = n;
        }
        else
        {
          if ((j == 0) && (localObject[i2].getSuggestions().length <= 0)) {
            j = 0;
          } else {
            j = 1;
          }
          i5 = Math.min(m, i3);
          i7 = Math.max(n, i4);
          i6 = j;
        }
      }
      i2++;
      m = i5;
      j = i6;
      n = i7;
    }
    if (j == 0) {
      return false;
    }
    if (m >= n) {
      return false;
    }
    return (k >= m) && (i1 <= n);
  }
  
  private void showError()
  {
    if (mTextView.getWindowToken() == null)
    {
      mShowErrorAfterAttach = true;
      return;
    }
    if (mErrorPopup == null)
    {
      localTextView = (TextView)LayoutInflater.from(mTextView.getContext()).inflate(17367340, null);
      float f = mTextView.getResources().getDisplayMetrics().density;
      mErrorPopup = new ErrorPopup(localTextView, (int)(200.0F * f + 0.5F), (int)(50.0F * f + 0.5F));
      mErrorPopup.setFocusable(false);
      mErrorPopup.setInputMethodMode(1);
    }
    TextView localTextView = (TextView)mErrorPopup.getContentView();
    chooseSize(mErrorPopup, mError, localTextView);
    localTextView.setText(mError);
    mErrorPopup.showAsDropDown(mTextView, getErrorX(), getErrorY(), 51);
    mErrorPopup.fixDirection(mErrorPopup.isAboveAnchor());
  }
  
  private void showFloatingToolbar()
  {
    if (mTextActionMode != null)
    {
      int i = ViewConfiguration.getDoubleTapTimeout();
      mTextView.postDelayed(mShowFloatingToolbar, i);
      invalidateActionModeAsync();
    }
  }
  
  private void startDragAndDrop()
  {
    getSelectionActionModeHelper().onSelectionDrag();
    if (mTextView.isInExtractedMode()) {
      return;
    }
    int i = mTextView.getSelectionStart();
    int j = mTextView.getSelectionEnd();
    ClipData localClipData = ClipData.newPlainText(null, mTextView.getTransformedText(i, j));
    DragLocalState localDragLocalState = new DragLocalState(mTextView, i, j);
    mTextView.startDragAndDrop(localClipData, getTextThumbnailBuilder(i, j), localDragLocalState, 256);
    stopTextActionMode();
    if (hasSelectionController()) {
      getSelectionController().resetTouchOffsets();
    }
  }
  
  private void stopTextActionModeWithPreservingSelection()
  {
    if (mTextActionMode != null) {
      mRestartActionModeOnNextRefresh = true;
    }
    mPreserveSelection = true;
    stopTextActionMode();
    mPreserveSelection = false;
  }
  
  private void suspendBlink()
  {
    if (mBlink != null) {
      mBlink.cancel();
    }
  }
  
  private boolean touchPositionIsInSelection()
  {
    int i = mTextView.getSelectionStart();
    int j = mTextView.getSelectionEnd();
    boolean bool1 = false;
    if (i == j) {
      return false;
    }
    int k = i;
    int m = j;
    if (i > j)
    {
      k = j;
      m = i;
      Selection.setSelection((Spannable)mTextView.getText(), k, m);
    }
    SelectionModifierCursorController localSelectionModifierCursorController = getSelectionController();
    i = localSelectionModifierCursorController.getMinTouchOffset();
    j = localSelectionModifierCursorController.getMaxTouchOffset();
    boolean bool2 = bool1;
    if (i >= k)
    {
      bool2 = bool1;
      if (j < m) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  private void updateCursorPosition(int paramInt1, int paramInt2, float paramFloat)
  {
    if (mDrawableForCursor == null) {
      mDrawableForCursor = mTextView.getContext().getDrawable(mTextView.mCursorDrawableRes);
    }
    int i = clampHorizontalPosition(mDrawableForCursor, paramFloat);
    int j = mDrawableForCursor.getIntrinsicWidth();
    mDrawableForCursor.setBounds(i, paramInt1 - mTempRect.top, i + j, mTempRect.bottom + paramInt2);
  }
  
  private void updateFloatingToolbarVisibility(MotionEvent paramMotionEvent)
  {
    if (mTextActionMode != null) {
      switch (paramMotionEvent.getActionMasked())
      {
      default: 
        break;
      case 2: 
        hideFloatingToolbar(-1);
        break;
      case 1: 
      case 3: 
        showFloatingToolbar();
      }
    }
  }
  
  private void updateSpellCheckSpans(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    mTextView.removeAdjacentSuggestionSpans(paramInt1);
    mTextView.removeAdjacentSuggestionSpans(paramInt2);
    if ((mTextView.isTextEditable()) && (mTextView.isSuggestionsEnabled()) && (!mTextView.isInExtractedMode()))
    {
      if ((mSpellChecker == null) && (paramBoolean)) {
        mSpellChecker = new SpellChecker(mTextView);
      }
      if (mSpellChecker != null) {
        mSpellChecker.spellCheck(paramInt1, paramInt2);
      }
    }
  }
  
  private void updateTapState(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
    {
      boolean bool = paramMotionEvent.isFromSource(8194);
      if (((mTapState == 1) || ((mTapState == 2) && (bool))) && (SystemClock.uptimeMillis() - mLastTouchUpTime <= ViewConfiguration.getDoubleTapTimeout()))
      {
        if (mTapState == 1) {
          mTapState = 2;
        } else {
          mTapState = 3;
        }
      }
      else {
        mTapState = 1;
      }
    }
    if (i == 1) {
      mLastTouchUpTime = SystemClock.uptimeMillis();
    }
  }
  
  public void addSpanWatchers(Spannable paramSpannable)
  {
    int i = paramSpannable.length();
    if (mKeyListener != null) {
      paramSpannable.setSpan(mKeyListener, 0, i, 18);
    }
    if (mSpanController == null) {
      mSpanController = new SpanController(null);
    }
    paramSpannable.setSpan(mSpanController, 0, i, 18);
  }
  
  void adjustInputType(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    if ((mInputType & 0xF) == 1)
    {
      if ((paramBoolean1) || (paramBoolean2)) {
        mInputType = (mInputType & 0xF00F | 0x80);
      }
      if (paramBoolean3) {
        mInputType = (mInputType & 0xF00F | 0xE0);
      }
    }
    else if (((mInputType & 0xF) == 2) && (paramBoolean4))
    {
      mInputType = (mInputType & 0xF00F | 0x10);
    }
  }
  
  public void beginBatchEdit()
  {
    mInBatchEditControllers = true;
    InputMethodState localInputMethodState = mInputMethodState;
    if (localInputMethodState != null)
    {
      int i = mBatchEditNesting + 1;
      mBatchEditNesting = i;
      if (i == 1)
      {
        mCursorChanged = false;
        mChangedDelta = 0;
        if (mContentChanged)
        {
          mChangedStart = 0;
          mChangedEnd = mTextView.getText().length();
        }
        else
        {
          mChangedStart = -1;
          mChangedEnd = -1;
          mContentChanged = false;
        }
        mUndoInputFilter.beginBatchEdit();
        mTextView.onBeginBatchEdit();
      }
    }
  }
  
  boolean canRedo()
  {
    boolean bool = true;
    UndoOwner localUndoOwner = mUndoOwner;
    if (mAllowUndo)
    {
      if (mUndoManager.countRedos(new UndoOwner[] { localUndoOwner }) > 0) {}
    }
    else {
      bool = false;
    }
    return bool;
  }
  
  boolean canUndo()
  {
    boolean bool = true;
    UndoOwner localUndoOwner = mUndoOwner;
    if (mAllowUndo)
    {
      if (mUndoManager.countUndos(new UndoOwner[] { localUndoOwner }) > 0) {}
    }
    else {
      bool = false;
    }
    return bool;
  }
  
  boolean checkField()
  {
    if ((mTextView.canSelectText()) && (mTextView.requestFocus())) {
      return true;
    }
    Log.w("TextView", "TextView does not support text selection. Selection cancelled.");
    return false;
  }
  
  void createInputContentTypeIfNeeded()
  {
    if (mInputContentType == null) {
      mInputContentType = new InputContentType();
    }
  }
  
  void createInputMethodStateIfNeeded()
  {
    if (mInputMethodState == null) {
      mInputMethodState = new InputMethodState();
    }
  }
  
  public void endBatchEdit()
  {
    mInBatchEditControllers = false;
    InputMethodState localInputMethodState = mInputMethodState;
    if (localInputMethodState != null)
    {
      int i = mBatchEditNesting - 1;
      mBatchEditNesting = i;
      if (i == 0) {
        finishBatchEdit(localInputMethodState);
      }
    }
  }
  
  void ensureEndedBatchEdit()
  {
    InputMethodState localInputMethodState = mInputMethodState;
    if ((localInputMethodState != null) && (mBatchEditNesting != 0))
    {
      mBatchEditNesting = 0;
      finishBatchEdit(localInputMethodState);
    }
  }
  
  boolean extractText(ExtractedTextRequest paramExtractedTextRequest, ExtractedText paramExtractedText)
  {
    return extractTextInternal(paramExtractedTextRequest, -1, -1, -1, paramExtractedText);
  }
  
  void finishBatchEdit(InputMethodState paramInputMethodState)
  {
    mTextView.onEndBatchEdit();
    mUndoInputFilter.endBatchEdit();
    if ((!mContentChanged) && (!mSelectionModeChanged))
    {
      if (mCursorChanged) {
        mTextView.invalidateCursor();
      }
    }
    else
    {
      mTextView.updateAfterEdit();
      reportExtractedText();
    }
    sendUpdateSelection();
    if (mTextActionMode != null)
    {
      if (mTextView.hasSelection()) {
        paramInputMethodState = getSelectionController();
      } else {
        paramInputMethodState = getInsertionController();
      }
      if ((paramInputMethodState != null) && (!paramInputMethodState.isActive()) && (!paramInputMethodState.isCursorBeingModified())) {
        paramInputMethodState.show();
      }
    }
  }
  
  void forgetUndoRedo()
  {
    UndoOwner[] arrayOfUndoOwner = new UndoOwner[1];
    arrayOfUndoOwner[0] = mUndoOwner;
    mUndoManager.forgetUndos(arrayOfUndoOwner, -1);
    mUndoManager.forgetRedos(arrayOfUndoOwner, -1);
  }
  
  @VisibleForTesting
  public Drawable getCursorDrawable()
  {
    return mDrawableForCursor;
  }
  
  float getLastUpPositionX()
  {
    return mLastUpPositionX;
  }
  
  float getLastUpPositionY()
  {
    return mLastUpPositionY;
  }
  
  SelectionModifierCursorController getSelectionController()
  {
    if (!mSelectionControllerEnabled) {
      return null;
    }
    if (mSelectionModifierCursorController == null)
    {
      mSelectionModifierCursorController = new SelectionModifierCursorController();
      mTextView.getViewTreeObserver().addOnTouchModeChangeListener(mSelectionModifierCursorController);
    }
    return mSelectionModifierCursorController;
  }
  
  @VisibleForTesting
  public SuggestionsPopupWindow getSuggestionsPopupWindowForTesting()
  {
    return mSuggestionsPopupWindow;
  }
  
  ActionMode getTextActionMode()
  {
    return mTextActionMode;
  }
  
  TextView getTextView()
  {
    return mTextView;
  }
  
  public WordIterator getWordIterator()
  {
    if (mWordIterator == null) {
      mWordIterator = new WordIterator(mTextView.getTextServicesLocale());
    }
    return mWordIterator;
  }
  
  boolean hasInsertionController()
  {
    return mInsertionControllerEnabled;
  }
  
  boolean hasSelectionController()
  {
    return mSelectionControllerEnabled;
  }
  
  void hideCursorAndSpanControllers()
  {
    hideCursorControllers();
    hideSpanControllers();
  }
  
  void hideFloatingToolbar(int paramInt)
  {
    if (mTextActionMode != null)
    {
      mTextView.removeCallbacks(mShowFloatingToolbar);
      mTextActionMode.hide(paramInt);
    }
  }
  
  void hideInsertionPointCursorController()
  {
    if (mInsertionPointCursorController != null) {
      mInsertionPointCursorController.hide();
    }
  }
  
  void invalidateActionModeAsync()
  {
    getSelectionActionModeHelper().invalidateActionModeAsync();
  }
  
  void invalidateHandlesAndActionMode()
  {
    if (mSelectionModifierCursorController != null) {
      mSelectionModifierCursorController.invalidateHandles();
    }
    if (mInsertionPointCursorController != null) {
      mInsertionPointCursorController.invalidateHandle();
    }
    if (mTextActionMode != null) {
      invalidateActionMode();
    }
  }
  
  void invalidateTextDisplayList()
  {
    if (mTextRenderNodes != null) {
      for (int i = 0; i < mTextRenderNodes.length; i++) {
        if (mTextRenderNodes[i] != null) {
          mTextRenderNodes[i].isDirty = true;
        }
      }
    }
  }
  
  void invalidateTextDisplayList(Layout paramLayout, int paramInt1, int paramInt2)
  {
    if ((mTextRenderNodes != null) && ((paramLayout instanceof DynamicLayout)))
    {
      int i = paramLayout.getLineForOffset(paramInt1);
      int j = paramLayout.getLineForOffset(paramInt2);
      DynamicLayout localDynamicLayout = (DynamicLayout)paramLayout;
      int[] arrayOfInt = localDynamicLayout.getBlockEndLines();
      paramLayout = localDynamicLayout.getBlockIndices();
      int k = localDynamicLayout.getNumberOfBlocks();
      for (paramInt1 = 0;; paramInt1++)
      {
        paramInt2 = paramInt1;
        if (paramInt1 >= k) {
          break;
        }
        if (arrayOfInt[paramInt1] >= i)
        {
          paramInt2 = paramInt1;
          break;
        }
      }
      while (paramInt2 < k)
      {
        paramInt1 = paramLayout[paramInt2];
        if (paramInt1 != -1) {
          mTextRenderNodes[paramInt1].isDirty = true;
        }
        if (arrayOfInt[paramInt2] >= j) {
          break;
        }
        paramInt2++;
      }
    }
  }
  
  void makeBlink()
  {
    if (shouldBlink())
    {
      mShowCursor = SystemClock.uptimeMillis();
      if (mBlink == null) {
        mBlink = new Blink(null);
      }
      mTextView.removeCallbacks(mBlink);
      mTextView.postDelayed(mBlink, 500L);
    }
    else if (mBlink != null)
    {
      mTextView.removeCallbacks(mBlink);
    }
  }
  
  void onAttachedToWindow()
  {
    if (mShowErrorAfterAttach)
    {
      showError();
      mShowErrorAfterAttach = false;
    }
    ViewTreeObserver localViewTreeObserver = mTextView.getViewTreeObserver();
    if (localViewTreeObserver.isAlive())
    {
      if (mInsertionPointCursorController != null) {
        localViewTreeObserver.addOnTouchModeChangeListener(mInsertionPointCursorController);
      }
      if (mSelectionModifierCursorController != null)
      {
        mSelectionModifierCursorController.resetTouchOffsets();
        localViewTreeObserver.addOnTouchModeChangeListener(mSelectionModifierCursorController);
      }
      localViewTreeObserver.addOnDrawListener(mMagnifierOnDrawListener);
    }
    updateSpellCheckSpans(0, mTextView.getText().length(), true);
    if (mTextView.hasSelection()) {
      refreshTextActionMode();
    }
    getPositionListener().addSubscriber(mCursorAnchorInfoNotifier, true);
    resumeBlink();
  }
  
  public void onCommitCorrection(CorrectionInfo paramCorrectionInfo)
  {
    if (mCorrectionHighlighter == null) {
      mCorrectionHighlighter = new CorrectionHighlighter();
    } else {
      mCorrectionHighlighter.invalidate(false);
    }
    mCorrectionHighlighter.highlight(paramCorrectionInfo);
    mUndoInputFilter.freezeLastEdit();
  }
  
  void onCreateContextMenu(ContextMenu paramContextMenu)
  {
    if ((!mIsBeingLongClicked) && (!Float.isNaN(mContextMenuAnchorX)) && (!Float.isNaN(mContextMenuAnchorY)))
    {
      int i = mTextView.getOffsetForPosition(mContextMenuAnchorX, mContextMenuAnchorY);
      if (i == -1) {
        return;
      }
      stopTextActionModeWithPreservingSelection();
      int j;
      if (mTextView.canSelectText())
      {
        if ((mTextView.hasSelection()) && (i >= mTextView.getSelectionStart()) && (i <= mTextView.getSelectionEnd())) {
          j = 1;
        } else {
          j = 0;
        }
        if (j == 0)
        {
          Selection.setSelection((Spannable)mTextView.getText(), i);
          stopTextActionMode();
        }
      }
      if (shouldOfferToShowSuggestions())
      {
        SuggestionInfo[] arrayOfSuggestionInfo = new SuggestionInfo[5];
        for (j = 0; j < arrayOfSuggestionInfo.length; j++) {
          arrayOfSuggestionInfo[j] = new SuggestionInfo(null);
        }
        SubMenu localSubMenu = paramContextMenu.addSubMenu(0, 0, 9, 17040902);
        i = mSuggestionHelper.getSuggestionInfo(arrayOfSuggestionInfo, null);
        for (j = 0; j < i; j++)
        {
          final SuggestionInfo localSuggestionInfo = arrayOfSuggestionInfo[j];
          localSubMenu.add(0, 0, j, mText).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
          {
            public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
            {
              Editor.this.replaceWithSuggestion(localSuggestionInfo);
              return true;
            }
          });
        }
      }
      paramContextMenu.add(0, 16908338, 2, 17041132).setAlphabeticShortcut('z').setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canUndo());
      paramContextMenu.add(0, 16908339, 3, 17040884).setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canRedo());
      paramContextMenu.add(0, 16908320, 4, 17039363).setAlphabeticShortcut('x').setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canCut());
      paramContextMenu.add(0, 16908321, 5, 17039361).setAlphabeticShortcut('c').setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canCopy());
      paramContextMenu.add(0, 16908322, 6, 17039371).setAlphabeticShortcut('v').setEnabled(mTextView.canPaste()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
      paramContextMenu.add(0, 16908337, 11, 17039385).setEnabled(mTextView.canPasteAsPlainText()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
      paramContextMenu.add(0, 16908341, 7, 17040987).setEnabled(mTextView.canShare()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
      paramContextMenu.add(0, 16908319, 8, 17039373).setAlphabeticShortcut('a').setEnabled(mTextView.canSelectAllText()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
      paramContextMenu.add(0, 16908355, 10, 17039386).setEnabled(mTextView.canRequestAutofill()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
      mPreserveSelection = true;
      return;
    }
  }
  
  void onDetachedFromWindow()
  {
    getPositionListener().removeSubscriber(mCursorAnchorInfoNotifier);
    if (mError != null) {
      hideError();
    }
    suspendBlink();
    if (mInsertionPointCursorController != null) {
      mInsertionPointCursorController.onDetached();
    }
    if (mSelectionModifierCursorController != null) {
      mSelectionModifierCursorController.onDetached();
    }
    if (mShowSuggestionRunnable != null) {
      mTextView.removeCallbacks(mShowSuggestionRunnable);
    }
    if (mInsertionActionModeRunnable != null) {
      mTextView.removeCallbacks(mInsertionActionModeRunnable);
    }
    mTextView.removeCallbacks(mShowFloatingToolbar);
    discardTextDisplayLists();
    if (mSpellChecker != null)
    {
      mSpellChecker.closeSession();
      mSpellChecker = null;
    }
    ViewTreeObserver localViewTreeObserver = mTextView.getViewTreeObserver();
    if (localViewTreeObserver.isAlive()) {
      localViewTreeObserver.removeOnDrawListener(mMagnifierOnDrawListener);
    }
    hideCursorAndSpanControllers();
    stopTextActionModeWithPreservingSelection();
  }
  
  void onDraw(Canvas paramCanvas, Layout paramLayout, Path paramPath, Paint paramPaint, int paramInt)
  {
    int i = mTextView.getSelectionStart();
    int j = mTextView.getSelectionEnd();
    Object localObject = mInputMethodState;
    if ((localObject != null) && (mBatchEditNesting == 0))
    {
      InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
      if ((localInputMethodManager != null) && (localInputMethodManager.isActive(mTextView)) && ((mContentChanged) || (mSelectionModeChanged))) {
        reportExtractedText();
      }
    }
    if (mCorrectionHighlighter != null) {
      mCorrectionHighlighter.draw(paramCanvas, paramInt);
    }
    localObject = paramPath;
    if (paramPath != null)
    {
      localObject = paramPath;
      if (i == j)
      {
        localObject = paramPath;
        if (mDrawableForCursor != null)
        {
          drawCursor(paramCanvas, paramInt);
          localObject = null;
        }
      }
    }
    paramPath = (Path)localObject;
    if (mSelectionActionModeHelper != null)
    {
      mSelectionActionModeHelper.onDraw(paramCanvas);
      paramPath = (Path)localObject;
      if (mSelectionActionModeHelper.isDrawingHighlight()) {
        paramPath = null;
      }
    }
    if ((mTextView.canHaveDisplayList()) && (paramCanvas.isHardwareAccelerated())) {
      drawHardwareAccelerated(paramCanvas, paramLayout, paramPath, paramPaint, paramInt);
    } else {
      paramLayout.draw(paramCanvas, paramPath, paramPaint, paramInt);
    }
  }
  
  /* Error */
  void onDrop(android.view.DragEvent paramDragEvent)
  {
    // Byte code:
    //   0: new 1435	android/text/SpannableStringBuilder
    //   3: dup
    //   4: invokespecial 2011	android/text/SpannableStringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_1
    //   9: invokestatic 2016	android/view/DragAndDropPermissions:obtain	(Landroid/view/DragEvent;)Landroid/view/DragAndDropPermissions;
    //   12: astore_3
    //   13: aload_3
    //   14: ifnull +8 -> 22
    //   17: aload_3
    //   18: invokevirtual 2019	android/view/DragAndDropPermissions:takeTransient	()Z
    //   21: pop
    //   22: aload_1
    //   23: invokevirtual 2025	android/view/DragEvent:getClipData	()Landroid/content/ClipData;
    //   26: astore 4
    //   28: aload 4
    //   30: invokevirtual 2028	android/content/ClipData:getItemCount	()I
    //   33: istore 5
    //   35: iconst_0
    //   36: istore 6
    //   38: iload 6
    //   40: iload 5
    //   42: if_icmpge +31 -> 73
    //   45: aload_2
    //   46: aload 4
    //   48: iload 6
    //   50: invokevirtual 2032	android/content/ClipData:getItemAt	(I)Landroid/content/ClipData$Item;
    //   53: aload_0
    //   54: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   57: invokevirtual 388	android/widget/TextView:getContext	()Landroid/content/Context;
    //   60: invokevirtual 2038	android/content/ClipData$Item:coerceToStyledText	(Landroid/content/Context;)Ljava/lang/CharSequence;
    //   63: invokevirtual 2041	android/text/SpannableStringBuilder:append	(Ljava/lang/CharSequence;)Landroid/text/SpannableStringBuilder;
    //   66: pop
    //   67: iinc 6 1
    //   70: goto -32 -> 38
    //   73: aload_3
    //   74: ifnull +7 -> 81
    //   77: aload_3
    //   78: invokevirtual 2044	android/view/DragAndDropPermissions:release	()V
    //   81: aload_0
    //   82: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   85: invokevirtual 2045	android/widget/TextView:beginBatchEdit	()V
    //   88: aload_0
    //   89: getfield 326	android/widget/Editor:mUndoInputFilter	Landroid/widget/Editor$UndoInputFilter;
    //   92: invokevirtual 1862	android/widget/Editor$UndoInputFilter:freezeLastEdit	()V
    //   95: aload_0
    //   96: aload_0
    //   97: getfield 600	android/widget/Editor:mLastDownPositionX	F
    //   100: aload_0
    //   101: getfield 603	android/widget/Editor:mLastDownPositionY	F
    //   104: invokespecial 570	android/widget/Editor:calculateTheFakeOffset	(FF)I
    //   107: istore 7
    //   109: aload_1
    //   110: invokevirtual 2049	android/view/DragEvent:getLocalState	()Ljava/lang/Object;
    //   113: astore_3
    //   114: aconst_null
    //   115: astore_1
    //   116: aload_3
    //   117: instanceof 28
    //   120: ifeq +8 -> 128
    //   123: aload_3
    //   124: checkcast 28	android/widget/Editor$DragLocalState
    //   127: astore_1
    //   128: aload_1
    //   129: ifnull +20 -> 149
    //   132: aload_1
    //   133: getfield 2052	android/widget/Editor$DragLocalState:sourceTextView	Landroid/widget/TextView;
    //   136: aload_0
    //   137: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   140: if_acmpne +9 -> 149
    //   143: iconst_1
    //   144: istore 6
    //   146: goto +6 -> 152
    //   149: iconst_0
    //   150: istore 6
    //   152: iload 6
    //   154: ifeq +40 -> 194
    //   157: iload 7
    //   159: aload_1
    //   160: getfield 2054	android/widget/Editor$DragLocalState:start	I
    //   163: if_icmplt +31 -> 194
    //   166: aload_1
    //   167: getfield 2056	android/widget/Editor$DragLocalState:end	I
    //   170: istore 5
    //   172: iload 7
    //   174: iload 5
    //   176: if_icmpge +18 -> 194
    //   179: aload_0
    //   180: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   183: invokevirtual 2057	android/widget/TextView:endBatchEdit	()V
    //   186: aload_0
    //   187: getfield 326	android/widget/Editor:mUndoInputFilter	Landroid/widget/Editor$UndoInputFilter;
    //   190: invokevirtual 1862	android/widget/Editor$UndoInputFilter:freezeLastEdit	()V
    //   193: return
    //   194: aload_0
    //   195: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   198: invokevirtual 667	android/widget/TextView:getText	()Ljava/lang/CharSequence;
    //   201: invokeinterface 711 1 0
    //   206: istore 8
    //   208: aload_0
    //   209: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   212: invokevirtual 667	android/widget/TextView:getText	()Ljava/lang/CharSequence;
    //   215: checkcast 824	android/text/Spannable
    //   218: iload 7
    //   220: invokestatic 1879	android/text/Selection:setSelection	(Landroid/text/Spannable;I)V
    //   223: aload_0
    //   224: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   227: iload 7
    //   229: iload 7
    //   231: aload_2
    //   232: invokevirtual 1440	android/widget/TextView:replaceText_internal	(IILjava/lang/CharSequence;)V
    //   235: iload 6
    //   237: ifeq +163 -> 400
    //   240: aload_1
    //   241: getfield 2054	android/widget/Editor$DragLocalState:start	I
    //   244: istore 9
    //   246: aload_1
    //   247: getfield 2056	android/widget/Editor$DragLocalState:end	I
    //   250: istore 10
    //   252: iload 9
    //   254: istore 6
    //   256: iload 10
    //   258: istore 5
    //   260: iload 7
    //   262: iload 9
    //   264: if_icmpgt +34 -> 298
    //   267: aload_0
    //   268: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   271: invokevirtual 667	android/widget/TextView:getText	()Ljava/lang/CharSequence;
    //   274: invokeinterface 711 1 0
    //   279: iload 8
    //   281: isub
    //   282: istore 5
    //   284: iload 9
    //   286: iload 5
    //   288: iadd
    //   289: istore 6
    //   291: iload 10
    //   293: iload 5
    //   295: iadd
    //   296: istore 5
    //   298: aload_0
    //   299: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   302: iload 6
    //   304: iload 5
    //   306: invokevirtual 2060	android/widget/TextView:deleteText_internal	(II)V
    //   309: iconst_0
    //   310: iload 6
    //   312: iconst_1
    //   313: isub
    //   314: invokestatic 930	java/lang/Math:max	(II)I
    //   317: istore 5
    //   319: aload_0
    //   320: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   323: invokevirtual 667	android/widget/TextView:getText	()Ljava/lang/CharSequence;
    //   326: invokeinterface 711 1 0
    //   331: iload 6
    //   333: iconst_1
    //   334: iadd
    //   335: invokestatic 924	java/lang/Math:min	(II)I
    //   338: istore 6
    //   340: iload 6
    //   342: iload 5
    //   344: iconst_1
    //   345: iadd
    //   346: if_icmple +54 -> 400
    //   349: aload_0
    //   350: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   353: iload 5
    //   355: iload 6
    //   357: invokevirtual 1257	android/widget/TextView:getTransformedText	(II)Ljava/lang/CharSequence;
    //   360: astore_1
    //   361: aload_1
    //   362: iconst_0
    //   363: invokeinterface 1232 2 0
    //   368: invokestatic 2066	java/lang/Character:isSpaceChar	(C)Z
    //   371: ifeq +29 -> 400
    //   374: aload_1
    //   375: iconst_1
    //   376: invokeinterface 1232 2 0
    //   381: invokestatic 2066	java/lang/Character:isSpaceChar	(C)Z
    //   384: ifeq +16 -> 400
    //   387: aload_0
    //   388: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   391: iload 5
    //   393: iload 5
    //   395: iconst_1
    //   396: iadd
    //   397: invokevirtual 2060	android/widget/TextView:deleteText_internal	(II)V
    //   400: aload_0
    //   401: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   404: invokevirtual 2057	android/widget/TextView:endBatchEdit	()V
    //   407: aload_0
    //   408: getfield 326	android/widget/Editor:mUndoInputFilter	Landroid/widget/Editor$UndoInputFilter;
    //   411: invokevirtual 1862	android/widget/Editor$UndoInputFilter:freezeLastEdit	()V
    //   414: return
    //   415: astore_1
    //   416: aload_0
    //   417: getfield 371	android/widget/Editor:mTextView	Landroid/widget/TextView;
    //   420: invokevirtual 2057	android/widget/TextView:endBatchEdit	()V
    //   423: aload_0
    //   424: getfield 326	android/widget/Editor:mUndoInputFilter	Landroid/widget/Editor$UndoInputFilter;
    //   427: invokevirtual 1862	android/widget/Editor$UndoInputFilter:freezeLastEdit	()V
    //   430: aload_1
    //   431: athrow
    //   432: astore_1
    //   433: aload_3
    //   434: ifnull +7 -> 441
    //   437: aload_3
    //   438: invokevirtual 2044	android/view/DragAndDropPermissions:release	()V
    //   441: aload_1
    //   442: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	443	0	this	Editor
    //   0	443	1	paramDragEvent	android.view.DragEvent
    //   7	225	2	localSpannableStringBuilder	SpannableStringBuilder
    //   12	426	3	localObject	Object
    //   26	21	4	localClipData	ClipData
    //   33	364	5	i	int
    //   36	320	6	j	int
    //   107	158	7	k	int
    //   206	76	8	m	int
    //   244	45	9	n	int
    //   250	46	10	i1	int
    // Exception table:
    //   from	to	target	type
    //   95	114	415	finally
    //   116	128	415	finally
    //   132	143	415	finally
    //   157	172	415	finally
    //   194	235	415	finally
    //   240	252	415	finally
    //   267	284	415	finally
    //   298	340	415	finally
    //   349	400	415	finally
    //   22	35	432	finally
    //   45	67	432	finally
  }
  
  void onFocusChanged(boolean paramBoolean, int paramInt)
  {
    mShowCursor = SystemClock.uptimeMillis();
    ensureEndedBatchEdit();
    if (paramBoolean)
    {
      int i = mTextView.getSelectionStart();
      int j = mTextView.getSelectionEnd();
      int k;
      if ((mSelectAllOnFocus) && (i == 0) && (j == mTextView.getText().length())) {
        k = 1;
      } else {
        k = 0;
      }
      if ((mFrozenWithFocus) && (mTextView.hasSelection()) && (k == 0)) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      mCreatedWithASelection = paramBoolean;
      if ((!mFrozenWithFocus) || (i < 0) || (j < 0))
      {
        k = getLastTapPosition();
        if (k >= 0) {
          Selection.setSelection((Spannable)mTextView.getText(), k);
        }
        MovementMethod localMovementMethod = mTextView.getMovementMethod();
        if (localMovementMethod != null) {
          localMovementMethod.onTakeFocus(mTextView, (Spannable)mTextView.getText(), paramInt);
        }
        if (((mTextView.isInExtractedMode()) || (mSelectionMoved)) && (i >= 0) && (j >= 0)) {
          Selection.setSelection((Spannable)mTextView.getText(), i, j);
        }
        if (mSelectAllOnFocus) {
          mTextView.selectAllText();
        }
        mTouchFocusSelected = true;
      }
      mFrozenWithFocus = false;
      mSelectionMoved = false;
      if (mError != null) {
        showError();
      }
      makeBlink();
    }
    else
    {
      if (mError != null) {
        hideError();
      }
      mTextView.onEndBatchEdit();
      if (mTextView.isInExtractedMode())
      {
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
      }
      else
      {
        hideCursorAndSpanControllers();
        if (mTextView.isTemporarilyDetached()) {
          stopTextActionModeWithPreservingSelection();
        } else {
          stopTextActionMode();
        }
        downgradeEasyCorrectionSpans();
      }
      if (mSelectionModifierCursorController != null) {
        mSelectionModifierCursorController.resetTouchOffsets();
      }
      ensureNoSelectionIfNonSelectable();
    }
  }
  
  void onLocaleChanged()
  {
    mWordIterator = null;
    mWordIteratorWithText = null;
  }
  
  void onScreenStateChanged(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 1: 
      resumeBlink();
      break;
    case 0: 
      suspendBlink();
    }
  }
  
  void onScrollChanged()
  {
    if (mPositionListener != null) {
      mPositionListener.onScrollChanged();
    }
    if (mTextActionMode != null) {
      mTextActionMode.invalidateContentRect();
    }
  }
  
  void onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = shouldFilterOutTouchEvent(paramMotionEvent);
    mLastButtonState = paramMotionEvent.getButtonState();
    if (bool)
    {
      if (paramMotionEvent.getActionMasked() == 1) {
        mDiscardNextActionUp = true;
      }
      return;
    }
    updateTapState(paramMotionEvent);
    updateFloatingToolbarVisibility(paramMotionEvent);
    if (hasSelectionController()) {
      getSelectionController().onTouchEvent(paramMotionEvent);
    }
    if (mShowSuggestionRunnable != null)
    {
      mTextView.removeCallbacks(mShowSuggestionRunnable);
      mShowSuggestionRunnable = null;
    }
    if (paramMotionEvent.getActionMasked() == 1)
    {
      mLastUpPositionX = paramMotionEvent.getX();
      mLastUpPositionY = paramMotionEvent.getY();
    }
    if (paramMotionEvent.getActionMasked() == 0)
    {
      mLastDownPositionX = paramMotionEvent.getX();
      mLastDownPositionY = paramMotionEvent.getY();
      mTouchFocusSelected = false;
      mIgnoreActionUpEvent = false;
    }
  }
  
  void onTouchUpEvent(MotionEvent paramMotionEvent)
  {
    if (getSelectionActionModeHelper().resetSelection(getTextView().getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY()))) {
      return;
    }
    int i;
    if ((mSelectAllOnFocus) && (mTextView.didTouchFocusSelect())) {
      i = 1;
    } else {
      i = 0;
    }
    hideCursorAndSpanControllers();
    stopTextActionMode();
    CharSequence localCharSequence = mTextView.getText();
    if ((i == 0) && (localCharSequence.length() > 0))
    {
      i = calculateTheFakeOffset(paramMotionEvent.getX(), paramMotionEvent.getY());
      boolean bool = true ^ mRequestingLinkActionMode;
      if (bool)
      {
        Selection.setSelection((Spannable)localCharSequence, i);
        if (mSpellChecker != null) {
          mSpellChecker.onSelectionChanged();
        }
      }
      if (!extractedTextModeWillBeStarted()) {
        if (isCursorInsideEasyCorrectionSpan())
        {
          if (mInsertionActionModeRunnable != null) {
            mTextView.removeCallbacks(mInsertionActionModeRunnable);
          }
          mShowSuggestionRunnable = new _..Lambda.DZXn7FbDDFyBvNjI_iG9_hfa7kw(this);
          mTextView.postDelayed(mShowSuggestionRunnable, ViewConfiguration.getDoubleTapTimeout());
        }
        else if (hasInsertionController())
        {
          if (bool) {
            getInsertionController().show();
          } else {
            getInsertionController().hide();
          }
        }
      }
    }
  }
  
  void onWindowFocusChanged(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (mBlink != null)
      {
        mBlink.uncancel();
        makeBlink();
      }
      if ((mTextView.hasSelection()) && (!extractedTextModeWillBeStarted())) {
        refreshTextActionMode();
      }
    }
    else
    {
      if (mBlink != null) {
        mBlink.cancel();
      }
      if (mInputContentType != null) {
        mInputContentType.enterDown = false;
      }
      hideCursorAndSpanControllers();
      stopTextActionModeWithPreservingSelection();
      if (mSuggestionsPopupWindow != null) {
        mSuggestionsPopupWindow.onParentLostFocus();
      }
      ensureEndedBatchEdit();
      ensureNoSelectionIfNonSelectable();
    }
  }
  
  public boolean performLongClick(boolean paramBoolean)
  {
    boolean bool = paramBoolean;
    if (!paramBoolean)
    {
      bool = paramBoolean;
      if (!isPositionOnText(mLastDownPositionX, mLastDownPositionY))
      {
        bool = paramBoolean;
        if (mInsertionControllerEnabled)
        {
          int i = calculateTheFakeOffset(mLastDownPositionX, mLastDownPositionY);
          Selection.setSelection((Spannable)mTextView.getText(), i);
          getInsertionController().show();
          mIsInsertionActionModeStartPending = true;
          bool = true;
          MetricsLogger.action(mTextView.getContext(), 629, 0);
        }
      }
    }
    paramBoolean = bool;
    if (!bool)
    {
      paramBoolean = bool;
      if (mTextActionMode != null)
      {
        if (touchPositionIsInSelection())
        {
          startDragAndDrop();
          MetricsLogger.action(mTextView.getContext(), 629, 2);
        }
        else
        {
          stopTextActionMode();
          selectCurrentWordAndStartDrag();
          MetricsLogger.action(mTextView.getContext(), 629, 1);
        }
        paramBoolean = true;
      }
    }
    bool = paramBoolean;
    if (!paramBoolean)
    {
      paramBoolean = selectCurrentWordAndStartDrag();
      bool = paramBoolean;
      if (paramBoolean)
      {
        MetricsLogger.action(mTextView.getContext(), 629, 1);
        bool = paramBoolean;
      }
    }
    return bool;
  }
  
  void prepareCursorControllers()
  {
    int i = 0;
    Object localObject = mTextView.getRootView().getLayoutParams();
    boolean bool1 = localObject instanceof WindowManager.LayoutParams;
    boolean bool2 = true;
    if (bool1)
    {
      localObject = (WindowManager.LayoutParams)localObject;
      if ((type >= 1000) && (type <= 1999)) {
        i = 0;
      } else {
        i = 1;
      }
    }
    if ((i != 0) && (mTextView.getLayout() != null)) {
      i = 1;
    } else {
      i = 0;
    }
    if ((i != 0) && (isCursorVisible())) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    mInsertionControllerEnabled = bool1;
    if ((i != 0) && (mTextView.textCanBeSelected())) {
      bool1 = bool2;
    } else {
      bool1 = false;
    }
    mSelectionControllerEnabled = bool1;
    if (!mInsertionControllerEnabled)
    {
      hideInsertionPointCursorController();
      if (mInsertionPointCursorController != null)
      {
        mInsertionPointCursorController.onDetached();
        mInsertionPointCursorController = null;
      }
    }
    if (!mSelectionControllerEnabled)
    {
      stopTextActionMode();
      if (mSelectionModifierCursorController != null)
      {
        mSelectionModifierCursorController.onDetached();
        mSelectionModifierCursorController = null;
      }
    }
  }
  
  void redo()
  {
    if (!mAllowUndo) {
      return;
    }
    UndoOwner localUndoOwner = mUndoOwner;
    mUndoManager.redo(new UndoOwner[] { localUndoOwner }, 1);
  }
  
  void refreshTextActionMode()
  {
    if (extractedTextModeWillBeStarted())
    {
      mRestartActionModeOnNextRefresh = false;
      return;
    }
    boolean bool = mTextView.hasSelection();
    SelectionModifierCursorController localSelectionModifierCursorController = getSelectionController();
    InsertionPointCursorController localInsertionPointCursorController = getInsertionController();
    if (((localSelectionModifierCursorController != null) && (localSelectionModifierCursorController.isCursorBeingModified())) || ((localInsertionPointCursorController != null) && (localInsertionPointCursorController.isCursorBeingModified())))
    {
      mRestartActionModeOnNextRefresh = false;
      return;
    }
    if (bool)
    {
      hideInsertionPointCursorController();
      if (mTextActionMode == null)
      {
        if (mRestartActionModeOnNextRefresh) {
          startSelectionActionModeAsync(false);
        }
      }
      else if ((localSelectionModifierCursorController != null) && (localSelectionModifierCursorController.isActive()))
      {
        mTextActionMode.invalidateContentRect();
      }
      else
      {
        stopTextActionModeWithPreservingSelection();
        startSelectionActionModeAsync(false);
      }
    }
    else if ((localInsertionPointCursorController != null) && (localInsertionPointCursorController.isActive()))
    {
      if (mTextActionMode != null) {
        mTextActionMode.invalidateContentRect();
      }
    }
    else
    {
      stopTextActionMode();
    }
    mRestartActionModeOnNextRefresh = false;
  }
  
  void replace()
  {
    if (mSuggestionsPopupWindow == null) {
      mSuggestionsPopupWindow = new SuggestionsPopupWindow();
    }
    hideCursorAndSpanControllers();
    mSuggestionsPopupWindow.show();
    int i = (mTextView.getSelectionStart() + mTextView.getSelectionEnd()) / 2;
    Selection.setSelection((Spannable)mTextView.getText(), i);
  }
  
  boolean reportExtractedText()
  {
    InputMethodState localInputMethodState = mInputMethodState;
    if (localInputMethodState == null) {
      return false;
    }
    boolean bool = mContentChanged;
    if ((!bool) && (!mSelectionModeChanged)) {
      return false;
    }
    mContentChanged = false;
    mSelectionModeChanged = false;
    ExtractedTextRequest localExtractedTextRequest = mExtractedTextRequest;
    if (localExtractedTextRequest == null) {
      return false;
    }
    InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
    if (localInputMethodManager == null) {
      return false;
    }
    if ((mChangedStart < 0) && (!bool)) {
      mChangedStart = -2;
    }
    if (extractTextInternal(localExtractedTextRequest, mChangedStart, mChangedEnd, mChangedDelta, mExtractedText))
    {
      localInputMethodManager.updateExtractedText(mTextView, token, mExtractedText);
      mChangedStart = -1;
      mChangedEnd = -1;
      mChangedDelta = 0;
      mContentChanged = false;
      return true;
    }
    return false;
  }
  
  void restoreInstanceState(ParcelableParcel paramParcelableParcel)
  {
    Parcel localParcel = paramParcelableParcel.getParcel();
    mUndoManager.restoreInstanceState(localParcel, paramParcelableParcel.getClassLoader());
    mUndoInputFilter.restoreInstanceState(localParcel);
    mUndoOwner = mUndoManager.getOwner("Editor", this);
  }
  
  ParcelableParcel saveInstanceState()
  {
    ParcelableParcel localParcelableParcel = new ParcelableParcel(getClass().getClassLoader());
    Parcel localParcel = localParcelableParcel.getParcel();
    mUndoManager.saveInstanceState(localParcel);
    mUndoInputFilter.saveInstanceState(localParcel);
    return localParcelableParcel;
  }
  
  boolean selectCurrentWord()
  {
    boolean bool1 = mTextView.canSelectText();
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if (needsToSelectAllToSelectWordOrParagraph()) {
      return mTextView.selectAllText();
    }
    long l = getLastTouchOffsets();
    int i = TextUtils.unpackRangeStartFromLong(l);
    int j = TextUtils.unpackRangeEndFromLong(l);
    if ((i >= 0) && (i <= mTextView.getText().length()))
    {
      if ((j >= 0) && (j <= mTextView.getText().length()))
      {
        Object localObject = (URLSpan[])((Spanned)mTextView.getText()).getSpans(i, j, URLSpan.class);
        int k;
        if (localObject.length >= 1)
        {
          localObject = localObject[0];
          k = ((Spanned)mTextView.getText()).getSpanStart(localObject);
          j = ((Spanned)mTextView.getText()).getSpanEnd(localObject);
        }
        else
        {
          localObject = getWordIterator();
          ((WordIterator)localObject).setCharSequence(mTextView.getText(), i, j);
          k = ((WordIterator)localObject).getBeginning(i);
          j = ((WordIterator)localObject).getEnd(j);
          if ((k != -1) && (j != -1) && (k != j)) {
            break label263;
          }
          l = getCharClusterRange(i);
          k = TextUtils.unpackRangeStartFromLong(l);
          j = TextUtils.unpackRangeEndFromLong(l);
        }
        label263:
        Selection.setSelection((Spannable)mTextView.getText(), k, j);
        if (j > k) {
          bool2 = true;
        }
        return bool2;
      }
      return false;
    }
    return false;
  }
  
  void sendOnTextChanged(int paramInt1, int paramInt2, int paramInt3)
  {
    getSelectionActionModeHelper().onTextChanged(paramInt1, paramInt1 + paramInt2);
    updateSpellCheckSpans(paramInt1, paramInt1 + paramInt3, false);
    mUpdateWordIteratorText = true;
    hideCursorControllers();
    if (mSelectionModifierCursorController != null) {
      mSelectionModifierCursorController.resetTouchOffsets();
    }
    stopTextActionMode();
  }
  
  void setContextMenuAnchor(float paramFloat1, float paramFloat2)
  {
    mContextMenuAnchorX = paramFloat1;
    mContextMenuAnchorY = paramFloat2;
  }
  
  public void setError(CharSequence paramCharSequence, Drawable paramDrawable)
  {
    mError = TextUtils.stringOrSpannedString(paramCharSequence);
    mErrorWasChanged = true;
    if (mError == null)
    {
      setErrorIcon(null);
      if (mErrorPopup != null)
      {
        if (mErrorPopup.isShowing()) {
          mErrorPopup.dismiss();
        }
        mErrorPopup = null;
      }
      mShowErrorAfterAttach = false;
    }
    else
    {
      setErrorIcon(paramDrawable);
      if (mTextView.isFocused()) {
        showError();
      }
    }
  }
  
  void setFrame()
  {
    if (mErrorPopup != null)
    {
      TextView localTextView = (TextView)mErrorPopup.getContentView();
      chooseSize(mErrorPopup, mError, localTextView);
      mErrorPopup.update(mTextView, getErrorX(), getErrorY(), mErrorPopup.getWidth(), mErrorPopup.getHeight());
    }
  }
  
  void setRestartActionModeOnNextRefresh(boolean paramBoolean)
  {
    mRestartActionModeOnNextRefresh = paramBoolean;
  }
  
  boolean shouldRenderCursor()
  {
    boolean bool1 = isCursorVisible();
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if (mRenderCursorRegardlessTiming) {
      return true;
    }
    if ((SystemClock.uptimeMillis() - mShowCursor) % 1000L < 500L) {
      bool2 = true;
    }
    return bool2;
  }
  
  boolean startActionModeInternal(@TextActionMode int paramInt)
  {
    if (extractedTextModeWillBeStarted()) {
      return false;
    }
    if (mTextActionMode != null)
    {
      invalidateActionMode();
      return false;
    }
    if ((paramInt != 2) && ((!checkField()) || (!mTextView.hasSelection()))) {
      return false;
    }
    Object localObject = new TextActionModeCallback(paramInt);
    TextView localTextView = mTextView;
    boolean bool = true;
    mTextActionMode = localTextView.startActionMode((ActionMode.Callback)localObject, 1);
    int i;
    if ((!mTextView.isTextEditable()) && (!mTextView.isTextSelectable())) {
      i = 0;
    } else {
      i = 1;
    }
    if ((paramInt == 2) && (i == 0) && ((mTextActionMode instanceof FloatingActionMode))) {
      ((FloatingActionMode)mTextActionMode).setOutsideTouchable(true, new _..Lambda.Editor.TdqUlJ6RRep0wXYHaRH51nTa08I(this));
    }
    if (mTextActionMode == null) {
      bool = false;
    }
    if ((bool) && (mTextView.isTextEditable()) && (!mTextView.isTextSelectable()) && (mShowSoftInputOnFocus))
    {
      localObject = InputMethodManager.peekInstance();
      if (localObject != null) {
        ((InputMethodManager)localObject).showSoftInput(mTextView, 0, null);
      }
    }
    return bool;
  }
  
  void startInsertionActionMode()
  {
    if (mInsertionActionModeRunnable != null) {
      mTextView.removeCallbacks(mInsertionActionModeRunnable);
    }
    if (extractedTextModeWillBeStarted()) {
      return;
    }
    stopTextActionMode();
    TextActionModeCallback localTextActionModeCallback = new TextActionModeCallback(1);
    mTextActionMode = mTextView.startActionMode(localTextActionModeCallback, 1);
    if ((mTextActionMode != null) && (getInsertionController() != null)) {
      getInsertionController().show();
    }
  }
  
  void startLinkActionModeAsync(int paramInt1, int paramInt2)
  {
    if (!(mTextView.getText() instanceof Spannable)) {
      return;
    }
    stopTextActionMode();
    mRequestingLinkActionMode = true;
    getSelectionActionModeHelper().startLinkActionModeAsync(paramInt1, paramInt2);
  }
  
  void startSelectionActionModeAsync(boolean paramBoolean)
  {
    getSelectionActionModeHelper().startSelectionActionModeAsync(paramBoolean);
  }
  
  protected void stopTextActionMode()
  {
    if (mTextActionMode != null) {
      mTextActionMode.finish();
    }
  }
  
  void undo()
  {
    if (!mAllowUndo) {
      return;
    }
    UndoOwner localUndoOwner = mUndoOwner;
    mUndoManager.undo(new UndoOwner[] { localUndoOwner }, 1);
  }
  
  void updateCursorPosition()
  {
    if (mTextView.mCursorDrawableRes == 0)
    {
      mDrawableForCursor = null;
      return;
    }
    Layout localLayout = mTextView.getLayout();
    int i = mTextView.getSelectionStart();
    int j = localLayout.getLineForOffset(i);
    int k = localLayout.getLineTop(j);
    int m = localLayout.getLineBottomWithoutSpacing(j);
    boolean bool = mTextView.getResetCursorFlag();
    int n = 0;
    if (bool) {
      bool = false;
    } else {
      bool = mCursorInLineEnd;
    }
    mCursorInLineEnd = bool;
    mTextView.setResetCursorFlag(false);
    if ((mCursorInLineEnd) && (i > 0)) {
      i--;
    }
    int i1;
    if ((mCursorInLineEnd) && (j > 0)) {
      i1 = j - 1;
    } else {
      i1 = j;
    }
    if (mCursorInLineEnd) {
      k = localLayout.getLineTop(i1);
    }
    if (mCursorInLineEnd) {
      i1 = localLayout.getLineTop(i1 + 1);
    } else {
      i1 = m;
    }
    if (mCursorInLineEnd) {
      n = (int)mTextView.getPaint().measureText(mTextView.getText().toString().substring(i, i + 1));
    }
    mCursorHorizontalOffset = n;
    bool = localLayout.shouldClampCursor(j);
    updateCursorPosition(k, i1, localLayout.getPrimaryHorizontal(i, bool) + mCursorHorizontalOffset);
  }
  
  private class Blink
    implements Runnable
  {
    private boolean mCancelled;
    
    private Blink() {}
    
    void cancel()
    {
      if (!mCancelled)
      {
        mTextView.removeCallbacks(this);
        mCancelled = true;
      }
    }
    
    public void run()
    {
      if (mCancelled) {
        return;
      }
      mTextView.removeCallbacks(this);
      if (Editor.this.shouldBlink())
      {
        if (mTextView.getLayout() != null) {
          mTextView.invalidateCursorPath();
        }
        mTextView.postDelayed(this, 500L);
      }
    }
    
    void uncancel()
    {
      mCancelled = false;
    }
  }
  
  private class CorrectionHighlighter
  {
    private static final int FADE_OUT_DURATION = 400;
    private int mEnd;
    private long mFadingStartTime;
    private final Paint mPaint = new Paint(1);
    private final Path mPath = new Path();
    private int mStart;
    private RectF mTempRectF;
    
    public CorrectionHighlighter()
    {
      mPaint.setCompatibilityScaling(mTextView.getResources().getCompatibilityInfo().applicationScale);
      mPaint.setStyle(Paint.Style.FILL);
    }
    
    private void invalidate(boolean paramBoolean)
    {
      if (mTextView.getLayout() == null) {
        return;
      }
      if (mTempRectF == null) {
        mTempRectF = new RectF();
      }
      mPath.computeBounds(mTempRectF, false);
      int i = mTextView.getCompoundPaddingLeft();
      int j = mTextView.getExtendedPaddingTop() + mTextView.getVerticalOffset(true);
      if (paramBoolean) {
        mTextView.postInvalidateOnAnimation((int)mTempRectF.left + i, (int)mTempRectF.top + j, (int)mTempRectF.right + i, (int)mTempRectF.bottom + j);
      } else {
        mTextView.postInvalidate((int)mTempRectF.left, (int)mTempRectF.top, (int)mTempRectF.right, (int)mTempRectF.bottom);
      }
    }
    
    private void stopAnimation()
    {
      Editor.access$8502(Editor.this, null);
    }
    
    private boolean updatePaint()
    {
      long l = SystemClock.uptimeMillis() - mFadingStartTime;
      if (l > 400L) {
        return false;
      }
      float f = (float)l / 400.0F;
      int i = Color.alpha(mTextView.mHighlightColor);
      int j = mTextView.mHighlightColor;
      i = (int)(i * (1.0F - f));
      mPaint.setColor((j & 0xFFFFFF) + (i << 24));
      return true;
    }
    
    private boolean updatePath()
    {
      Layout localLayout = mTextView.getLayout();
      if (localLayout == null) {
        return false;
      }
      int i = mTextView.getText().length();
      int j = Math.min(i, mStart);
      i = Math.min(i, mEnd);
      mPath.reset();
      localLayout.getSelectionPath(j, i, mPath);
      return true;
    }
    
    public void draw(Canvas paramCanvas, int paramInt)
    {
      if ((updatePath()) && (updatePaint()))
      {
        if (paramInt != 0) {
          paramCanvas.translate(0.0F, paramInt);
        }
        paramCanvas.drawPath(mPath, mPaint);
        if (paramInt != 0) {
          paramCanvas.translate(0.0F, -paramInt);
        }
        invalidate(true);
      }
      else
      {
        stopAnimation();
        invalidate(false);
      }
    }
    
    public void highlight(CorrectionInfo paramCorrectionInfo)
    {
      mStart = paramCorrectionInfo.getOffset();
      mEnd = (mStart + paramCorrectionInfo.getNewText().length());
      mFadingStartTime = SystemClock.uptimeMillis();
      if ((mStart < 0) || (mEnd < 0)) {
        stopAnimation();
      }
    }
  }
  
  private final class CursorAnchorInfoNotifier
    implements Editor.TextViewPositionListener
  {
    final CursorAnchorInfo.Builder mSelectionInfoBuilder = new CursorAnchorInfo.Builder();
    final int[] mTmpIntOffset = new int[2];
    final Matrix mViewToScreenMatrix = new Matrix();
    
    private CursorAnchorInfoNotifier() {}
    
    public void updatePosition(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
      Object localObject1 = mInputMethodState;
      if ((localObject1 != null) && (mBatchEditNesting <= 0))
      {
        localObject1 = InputMethodManager.peekInstance();
        if (localObject1 == null) {
          return;
        }
        if (!((InputMethodManager)localObject1).isActive(mTextView)) {
          return;
        }
        if (!((InputMethodManager)localObject1).isCursorAnchorInfoEnabled()) {
          return;
        }
        Layout localLayout = mTextView.getLayout();
        if (localLayout == null) {
          return;
        }
        CursorAnchorInfo.Builder localBuilder = mSelectionInfoBuilder;
        localBuilder.reset();
        int i = mTextView.getSelectionStart();
        localBuilder.setSelectionRange(i, mTextView.getSelectionEnd());
        mViewToScreenMatrix.set(mTextView.getMatrix());
        mTextView.getLocationOnScreen(mTmpIntOffset);
        Object localObject2 = mViewToScreenMatrix;
        Object localObject3 = mTmpIntOffset;
        int j = 0;
        ((Matrix)localObject2).postTranslate(localObject3[0], mTmpIntOffset[1]);
        localBuilder.setMatrix(mViewToScreenMatrix);
        float f1 = mTextView.viewportToContentHorizontalOffset();
        float f2 = mTextView.viewportToContentVerticalOffset();
        localObject2 = mTextView.getText();
        if ((localObject2 instanceof Spannable))
        {
          localObject3 = (Spannable)localObject2;
          int k = EditableInputConnection.getComposingSpanStart((Spannable)localObject3);
          int m = EditableInputConnection.getComposingSpanEnd((Spannable)localObject3);
          paramInt2 = k;
          paramInt1 = m;
          if (m < k)
          {
            paramInt1 = k;
            paramInt2 = m;
          }
          m = j;
          if (paramInt2 >= 0)
          {
            m = j;
            if (paramInt2 < paramInt1) {
              m = 1;
            }
          }
          if (m != 0)
          {
            localBuilder.setComposingText(paramInt2, ((CharSequence)localObject2).subSequence(paramInt2, paramInt1));
            mTextView.populateCharacterBounds(localBuilder, paramInt2, paramInt1, f1, f2);
          }
        }
        if (i >= 0)
        {
          paramInt1 = localLayout.getLineForOffset(i);
          float f3 = localLayout.getPrimaryHorizontal(i) + f1;
          float f4 = localLayout.getLineTop(paramInt1) + f2;
          float f5 = localLayout.getLineBaseline(paramInt1);
          f1 = localLayout.getLineBottomWithoutSpacing(paramInt1) + f2;
          paramBoolean2 = mTextView.isPositionVisible(f3, f4);
          paramBoolean1 = mTextView.isPositionVisible(f3, f1);
          paramInt2 = 0;
          if ((paramBoolean2) || (paramBoolean1)) {
            paramInt2 = 0x0 | 0x1;
          }
          if (paramBoolean2)
          {
            paramInt1 = paramInt2;
            if (paramBoolean1) {}
          }
          else
          {
            paramInt1 = paramInt2 | 0x2;
          }
          paramInt2 = paramInt1;
          if (localLayout.isRtlCharAt(i)) {
            paramInt2 = paramInt1 | 0x4;
          }
          localBuilder.setInsertionMarkerLocation(f3, f4, f5 + f2, f1, paramInt2);
        }
        ((InputMethodManager)localObject1).updateCursorAnchorInfo(mTextView, localBuilder.build());
        return;
      }
    }
  }
  
  private static abstract interface CursorController
    extends ViewTreeObserver.OnTouchModeChangeListener
  {
    public abstract void hide();
    
    public abstract boolean isActive();
    
    public abstract boolean isCursorBeingModified();
    
    public abstract void onDetached();
    
    public abstract void show();
  }
  
  private static class DragLocalState
  {
    public int end;
    public TextView sourceTextView;
    public int start;
    
    public DragLocalState(TextView paramTextView, int paramInt1, int paramInt2)
    {
      sourceTextView = paramTextView;
      start = paramInt1;
      end = paramInt2;
    }
  }
  
  private static abstract interface EasyEditDeleteListener
  {
    public abstract void onDeleteClick(EasyEditSpan paramEasyEditSpan);
  }
  
  private class EasyEditPopupWindow
    extends Editor.PinnedPopupWindow
    implements View.OnClickListener
  {
    private static final int POPUP_TEXT_LAYOUT = 17367330;
    private TextView mDeleteTextView;
    private EasyEditSpan mEasyEditSpan;
    private Editor.EasyEditDeleteListener mOnDeleteListener;
    
    private EasyEditPopupWindow()
    {
      super();
    }
    
    private void setOnDeleteListener(Editor.EasyEditDeleteListener paramEasyEditDeleteListener)
    {
      mOnDeleteListener = paramEasyEditDeleteListener;
    }
    
    protected int clipVertically(int paramInt)
    {
      return paramInt;
    }
    
    protected void createPopupWindow()
    {
      mPopupWindow = new PopupWindow(mTextView.getContext(), null, 16843464);
      mPopupWindow.setInputMethodMode(2);
      mPopupWindow.setClippingEnabled(true);
    }
    
    protected int getTextOffset()
    {
      return ((Editable)mTextView.getText()).getSpanEnd(mEasyEditSpan);
    }
    
    protected int getVerticalLocalPosition(int paramInt)
    {
      return mTextView.getLayout().getLineBottomWithoutSpacing(paramInt);
    }
    
    public void hide()
    {
      if (mEasyEditSpan != null) {
        mEasyEditSpan.setDeleteEnabled(false);
      }
      mOnDeleteListener = null;
      super.hide();
    }
    
    protected void initContentView()
    {
      Object localObject = new LinearLayout(mTextView.getContext());
      ((LinearLayout)localObject).setOrientation(0);
      mContentView = ((ViewGroup)localObject);
      mContentView.setBackgroundResource(17303898);
      LayoutInflater localLayoutInflater = (LayoutInflater)mTextView.getContext().getSystemService("layout_inflater");
      localObject = new ViewGroup.LayoutParams(-2, -2);
      mDeleteTextView = ((TextView)localLayoutInflater.inflate(17367330, null));
      mDeleteTextView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      mDeleteTextView.setText(17039876);
      mDeleteTextView.setOnClickListener(this);
      mContentView.addView(mDeleteTextView);
    }
    
    public void onClick(View paramView)
    {
      if ((paramView == mDeleteTextView) && (mEasyEditSpan != null) && (mEasyEditSpan.isDeleteEnabled()) && (mOnDeleteListener != null)) {
        mOnDeleteListener.onDeleteClick(mEasyEditSpan);
      }
    }
    
    public void setEasyEditSpan(EasyEditSpan paramEasyEditSpan)
    {
      mEasyEditSpan = paramEasyEditSpan;
    }
  }
  
  public static class EditOperation
    extends UndoOperation<Editor>
  {
    public static final Parcelable.ClassLoaderCreator<EditOperation> CREATOR = new Parcelable.ClassLoaderCreator()
    {
      public Editor.EditOperation createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Editor.EditOperation(paramAnonymousParcel, null);
      }
      
      public Editor.EditOperation createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new Editor.EditOperation(paramAnonymousParcel, paramAnonymousClassLoader);
      }
      
      public Editor.EditOperation[] newArray(int paramAnonymousInt)
      {
        return new Editor.EditOperation[paramAnonymousInt];
      }
    };
    private static final int TYPE_DELETE = 1;
    private static final int TYPE_INSERT = 0;
    private static final int TYPE_REPLACE = 2;
    private boolean mFrozen;
    private boolean mIsComposition;
    private int mNewCursorPos;
    private String mNewText;
    private int mOldCursorPos;
    private String mOldText;
    private int mStart;
    private int mType;
    
    public EditOperation(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super(paramClassLoader);
      mType = paramParcel.readInt();
      mOldText = paramParcel.readString();
      mNewText = paramParcel.readString();
      mStart = paramParcel.readInt();
      mOldCursorPos = paramParcel.readInt();
      mNewCursorPos = paramParcel.readInt();
      int i = paramParcel.readInt();
      boolean bool1 = false;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mFrozen = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      }
      mIsComposition = bool2;
    }
    
    public EditOperation(Editor paramEditor, String paramString1, int paramInt, String paramString2, boolean paramBoolean)
    {
      super();
      mOldText = paramString1;
      mNewText = paramString2;
      if ((mNewText.length() > 0) && (mOldText.length() == 0)) {
        mType = 0;
      } else if ((mNewText.length() == 0) && (mOldText.length() > 0)) {
        mType = 1;
      } else {
        mType = 2;
      }
      mStart = paramInt;
      mOldCursorPos = mTextView.getSelectionStart();
      mNewCursorPos = (mNewText.length() + paramInt);
      mIsComposition = paramBoolean;
    }
    
    private int getNewTextEnd()
    {
      return mStart + mNewText.length();
    }
    
    private int getOldTextEnd()
    {
      return mStart + mOldText.length();
    }
    
    private String getTypeString()
    {
      switch (mType)
      {
      default: 
        return "";
      case 2: 
        return "replace";
      case 1: 
        return "delete";
      }
      return "insert";
    }
    
    private boolean mergeDeleteWith(EditOperation paramEditOperation)
    {
      if (mType != 1) {
        return false;
      }
      if (mStart != paramEditOperation.getOldTextEnd()) {
        return false;
      }
      mStart = mStart;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(mOldText);
      localStringBuilder.append(mOldText);
      mOldText = localStringBuilder.toString();
      mNewCursorPos = mNewCursorPos;
      mIsComposition = mIsComposition;
      return true;
    }
    
    private boolean mergeInsertWith(EditOperation paramEditOperation)
    {
      StringBuilder localStringBuilder;
      if (mType == 0)
      {
        if (getNewTextEnd() != mStart) {
          return false;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(mNewText);
        localStringBuilder.append(mNewText);
        mNewText = localStringBuilder.toString();
        mNewCursorPos = mNewCursorPos;
        mFrozen = mFrozen;
        mIsComposition = mIsComposition;
        return true;
      }
      if ((mIsComposition) && (mType == 2) && (mStart <= mStart) && (getNewTextEnd() >= paramEditOperation.getOldTextEnd()))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(mNewText.substring(0, mStart - mStart));
        localStringBuilder.append(mNewText);
        localStringBuilder.append(mNewText.substring(paramEditOperation.getOldTextEnd() - mStart, mNewText.length()));
        mNewText = localStringBuilder.toString();
        mNewCursorPos = mNewCursorPos;
        mIsComposition = mIsComposition;
        return true;
      }
      return false;
    }
    
    private boolean mergeReplaceWith(EditOperation paramEditOperation)
    {
      StringBuilder localStringBuilder;
      if ((mType == 0) && (getNewTextEnd() == mStart))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(mNewText);
        localStringBuilder.append(mNewText);
        mNewText = localStringBuilder.toString();
        mNewCursorPos = mNewCursorPos;
        return true;
      }
      if (!mIsComposition) {
        return false;
      }
      if ((mType == 1) && (mStart <= mStart) && (getNewTextEnd() >= paramEditOperation.getOldTextEnd()))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(mNewText.substring(0, mStart - mStart));
        localStringBuilder.append(mNewText.substring(paramEditOperation.getOldTextEnd() - mStart, mNewText.length()));
        mNewText = localStringBuilder.toString();
        if (mNewText.isEmpty()) {
          mType = 1;
        }
        mNewCursorPos = mNewCursorPos;
        mIsComposition = mIsComposition;
        return true;
      }
      if ((mType == 2) && (mStart == mStart) && (TextUtils.equals(mNewText, mOldText)))
      {
        mNewText = mNewText;
        mNewCursorPos = mNewCursorPos;
        mIsComposition = mIsComposition;
        return true;
      }
      return false;
    }
    
    private boolean mergeWith(EditOperation paramEditOperation)
    {
      if (mFrozen) {
        return false;
      }
      switch (mType)
      {
      default: 
        return false;
      case 2: 
        return mergeReplaceWith(paramEditOperation);
      case 1: 
        return mergeDeleteWith(paramEditOperation);
      }
      return mergeInsertWith(paramEditOperation);
    }
    
    private static void modifyText(Editable paramEditable, int paramInt1, int paramInt2, CharSequence paramCharSequence, int paramInt3, int paramInt4)
    {
      if ((Editor.isValidRange(paramEditable, paramInt1, paramInt2)) && (paramInt3 <= paramEditable.length() - (paramInt2 - paramInt1)))
      {
        if (paramInt1 != paramInt2) {
          paramEditable.delete(paramInt1, paramInt2);
        }
        if (paramCharSequence.length() != 0) {
          paramEditable.insert(paramInt3, paramCharSequence);
        }
      }
      if ((paramInt4 >= 0) && (paramInt4 <= paramEditable.length())) {
        Selection.setSelection(paramEditable, paramInt4);
      }
    }
    
    public void commit() {}
    
    public void forceMergeWith(EditOperation paramEditOperation)
    {
      if (mergeWith(paramEditOperation)) {
        return;
      }
      Object localObject = (Editable)getOwnerDatamTextView.getText();
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(localObject.toString());
      modifyText(localSpannableStringBuilder, mStart, getNewTextEnd(), mOldText, mStart, mOldCursorPos);
      localObject = new SpannableStringBuilder(localObject.toString());
      modifyText((Editable)localObject, mStart, paramEditOperation.getOldTextEnd(), mNewText, mStart, mNewCursorPos);
      mType = 2;
      mNewText = localObject.toString();
      mOldText = localSpannableStringBuilder.toString();
      mStart = 0;
      mNewCursorPos = mNewCursorPos;
      mIsComposition = mIsComposition;
    }
    
    public void redo()
    {
      modifyText((Editable)getOwnerDatamTextView.getText(), mStart, getOldTextEnd(), mNewText, mStart, mNewCursorPos);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[mType=");
      localStringBuilder.append(getTypeString());
      localStringBuilder.append(", mOldText=");
      localStringBuilder.append(mOldText);
      localStringBuilder.append(", mNewText=");
      localStringBuilder.append(mNewText);
      localStringBuilder.append(", mStart=");
      localStringBuilder.append(mStart);
      localStringBuilder.append(", mOldCursorPos=");
      localStringBuilder.append(mOldCursorPos);
      localStringBuilder.append(", mNewCursorPos=");
      localStringBuilder.append(mNewCursorPos);
      localStringBuilder.append(", mFrozen=");
      localStringBuilder.append(mFrozen);
      localStringBuilder.append(", mIsComposition=");
      localStringBuilder.append(mIsComposition);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void undo()
    {
      modifyText((Editable)getOwnerDatamTextView.getText(), mStart, getNewTextEnd(), mOldText, mStart, mOldCursorPos);
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mType);
      paramParcel.writeString(mOldText);
      paramParcel.writeString(mNewText);
      paramParcel.writeInt(mStart);
      paramParcel.writeInt(mOldCursorPos);
      paramParcel.writeInt(mNewCursorPos);
      paramParcel.writeInt(mFrozen);
      paramParcel.writeInt(mIsComposition);
    }
  }
  
  private static class ErrorPopup
    extends PopupWindow
  {
    private boolean mAbove = false;
    private int mPopupInlineErrorAboveBackgroundId = 0;
    private int mPopupInlineErrorBackgroundId = 0;
    private final TextView mView;
    
    ErrorPopup(TextView paramTextView, int paramInt1, int paramInt2)
    {
      super(paramInt1, paramInt2);
      mView = paramTextView;
      mPopupInlineErrorBackgroundId = getResourceId(mPopupInlineErrorBackgroundId, 298);
      mView.setBackgroundResource(mPopupInlineErrorBackgroundId);
    }
    
    private int getResourceId(int paramInt1, int paramInt2)
    {
      int i = paramInt1;
      if (paramInt1 == 0)
      {
        TypedArray localTypedArray = mView.getContext().obtainStyledAttributes(R.styleable.Theme);
        i = localTypedArray.getResourceId(paramInt2, 0);
        localTypedArray.recycle();
      }
      return i;
    }
    
    void fixDirection(boolean paramBoolean)
    {
      mAbove = paramBoolean;
      if (paramBoolean) {
        mPopupInlineErrorAboveBackgroundId = getResourceId(mPopupInlineErrorAboveBackgroundId, 297);
      } else {
        mPopupInlineErrorBackgroundId = getResourceId(mPopupInlineErrorBackgroundId, 298);
      }
      TextView localTextView = mView;
      int i;
      if (paramBoolean) {
        i = mPopupInlineErrorAboveBackgroundId;
      } else {
        i = mPopupInlineErrorBackgroundId;
      }
      localTextView.setBackgroundResource(i);
    }
    
    public void update(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
    {
      super.update(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
      paramBoolean = isAboveAnchor();
      if (paramBoolean != mAbove) {
        fixDirection(paramBoolean);
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface HandleType {}
  
  @VisibleForTesting
  public abstract class HandleView
    extends View
    implements Editor.TextViewPositionListener
  {
    private static final int HISTORY_SIZE = 5;
    private static final int TOUCH_UP_FILTER_DELAY_AFTER = 150;
    private static final int TOUCH_UP_FILTER_DELAY_BEFORE = 350;
    private final PopupWindow mContainer;
    protected Drawable mDrawable;
    protected Drawable mDrawableLtr;
    protected Drawable mDrawableRtl;
    private final Magnifier.Callback mHandlesVisibilityCallback = new Magnifier.Callback()
    {
      public void onOperationComplete()
      {
        Object localObject = Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).getWindowCoords();
        if (localObject == null) {
          return;
        }
        localObject = new Rect(x, y, x + Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).getWidth(), y + Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).getHeight());
        Editor.HandleView.this.setVisible(Editor.HandleView.access$5700(Editor.HandleView.this, Editor.HandleView.this, (Rect)localObject) ^ true);
        Editor.HandleView localHandleView = Editor.HandleView.this.getOtherSelectionHandle();
        if (localHandleView != null) {
          localHandleView.setVisible(Editor.HandleView.access$5700(Editor.HandleView.this, localHandleView, (Rect)localObject) ^ true);
        }
      }
    };
    protected int mHorizontalGravity;
    protected int mHotspotX;
    private float mIdealVerticalOffset;
    private boolean mIsDragging;
    private int mLastParentX;
    private int mLastParentXOnScreen;
    private int mLastParentY;
    private int mLastParentYOnScreen;
    private int mMinSize;
    private int mNumberPreviousOffsets = 0;
    private boolean mPositionHasChanged = true;
    private int mPositionX;
    private int mPositionY;
    protected int mPrevLine = -1;
    protected int mPreviousLineTouched = -1;
    protected int mPreviousOffset = -1;
    private int mPreviousOffsetIndex = 0;
    private final int[] mPreviousOffsets = new int[5];
    private final long[] mPreviousOffsetsTimes = new long[5];
    private float mTouchOffsetY;
    private float mTouchToWindowOffsetX;
    private float mTouchToWindowOffsetY;
    
    private HandleView(Drawable paramDrawable1, Drawable paramDrawable2, int paramInt)
    {
      super();
      setId(paramInt);
      mContainer = new PopupWindow(mTextView.getContext(), null, 16843464);
      mContainer.setSplitTouchEnabled(true);
      mContainer.setClippingEnabled(false);
      mContainer.setWindowLayoutType(1002);
      mContainer.setWidth(-2);
      mContainer.setHeight(-2);
      mContainer.setContentView(this);
      mDrawableLtr = paramDrawable1;
      mDrawableRtl = paramDrawable2;
      mMinSize = mTextView.getContext().getResources().getDimensionPixelSize(17105438);
      updateDrawable();
      paramInt = getPreferredHeight();
      mTouchOffsetY = (-0.3F * paramInt);
      mIdealVerticalOffset = (0.7F * paramInt);
    }
    
    private void addPositionToTouchUpFilter(int paramInt)
    {
      mPreviousOffsetIndex = ((mPreviousOffsetIndex + 1) % 5);
      mPreviousOffsets[mPreviousOffsetIndex] = paramInt;
      mPreviousOffsetsTimes[mPreviousOffsetIndex] = SystemClock.uptimeMillis();
      mNumberPreviousOffsets += 1;
    }
    
    private void filterOnTouchUp(boolean paramBoolean)
    {
      long l = SystemClock.uptimeMillis();
      int i = 0;
      int j = mPreviousOffsetIndex;
      int k = Math.min(mNumberPreviousOffsets, 5);
      while ((i < k) && (l - mPreviousOffsetsTimes[j] < 150L))
      {
        i++;
        j = (mPreviousOffsetIndex - i + 5) % 5;
      }
      if ((i > 0) && (i < k) && (l - mPreviousOffsetsTimes[j] > 350L)) {
        positionAtCursorOffset(mPreviousOffsets[j], false, paramBoolean);
      }
    }
    
    private int getHorizontalOffset()
    {
      int i = getPreferredWidth();
      int j = mDrawable.getIntrinsicWidth();
      int k = mHorizontalGravity;
      if (k != 3)
      {
        if (k != 5) {
          i = (i - j) / 2;
        } else {
          i -= j;
        }
      }
      else {
        i = 0;
      }
      return i;
    }
    
    private HandleView getOtherSelectionHandle()
    {
      Object localObject = getSelectionController();
      if ((localObject != null) && (((Editor.SelectionModifierCursorController)localObject).isActive()))
      {
        if (Editor.SelectionModifierCursorController.access$4100((Editor.SelectionModifierCursorController)localObject) != this) {
          localObject = Editor.SelectionModifierCursorController.access$4100((Editor.SelectionModifierCursorController)localObject);
        } else {
          localObject = Editor.SelectionModifierCursorController.access$5600((Editor.SelectionModifierCursorController)localObject);
        }
        return localObject;
      }
      return null;
    }
    
    private int getPreferredHeight()
    {
      return Math.max(mDrawable.getIntrinsicHeight(), mMinSize);
    }
    
    private int getPreferredWidth()
    {
      return Math.max(mDrawable.getIntrinsicWidth(), mMinSize);
    }
    
    private boolean handleOverlapsMagnifier(HandleView paramHandleView, Rect paramRect)
    {
      paramHandleView = mContainer;
      if (!paramHandleView.hasDecorView()) {
        return false;
      }
      return Rect.intersects(new Rect(getDecorViewLayoutParamsx, getDecorViewLayoutParamsy, getDecorViewLayoutParamsx + paramHandleView.getContentView().getWidth(), getDecorViewLayoutParamsy + paramHandleView.getContentView().getHeight()), paramRect);
    }
    
    private boolean obtainMagnifierShowCoordinates(MotionEvent paramMotionEvent, PointF paramPointF)
    {
      int i = getMagnifierHandleTrigger();
      int k;
      switch (i)
      {
      default: 
        j = -1;
        k = -1;
        break;
      case 2: 
        j = mTextView.getSelectionEnd();
        k = mTextView.getSelectionStart();
        break;
      case 1: 
        j = mTextView.getSelectionStart();
        k = mTextView.getSelectionEnd();
        break;
      case 0: 
        j = mTextView.getSelectionStart();
        k = -1;
      }
      if (j == -1) {
        return false;
      }
      Object localObject = mTextView.getLayout();
      int m = ((Layout)localObject).getLineForOffset(j);
      if ((mCursorInLineEnd) && (m > 0)) {
        m--;
      }
      int n;
      if ((k != -1) && (m == ((Layout)localObject).getLineForOffset(k))) {
        n = 1;
      } else {
        n = 0;
      }
      int i1;
      if (n != 0)
      {
        if (j < k) {
          i1 = 1;
        } else {
          i1 = 0;
        }
        if (getHorizontal(mTextView.getLayout(), j) < getHorizontal(mTextView.getLayout(), k)) {
          j = 1;
        } else {
          j = 0;
        }
        if (i1 != j)
        {
          j = 1;
          break label269;
        }
      }
      int j = 0;
      label269:
      localObject = new int[2];
      mTextView.getLocationOnScreen((int[])localObject);
      float f1 = paramMotionEvent.getRawX() - localObject[0];
      float f2 = mTextView.getTotalPaddingLeft() - mTextView.getScrollX();
      float f3 = mTextView.getTotalPaddingLeft() - mTextView.getScrollX();
      if (n != 0)
      {
        if (i == 2) {
          i1 = 1;
        } else {
          i1 = 0;
        }
        if ((i1 ^ j) != 0)
        {
          f2 += getHorizontal(mTextView.getLayout(), k);
          break label417;
        }
      }
      f2 += mTextView.getLayout().getLineLeft(m);
      label417:
      if (n != 0)
      {
        if (i == 1) {
          n = 1;
        } else {
          n = 0;
        }
        if ((n ^ j) != 0)
        {
          f3 += getHorizontal(mTextView.getLayout(), k);
          break label488;
        }
      }
      f3 += mTextView.getLayout().getLineRight(m);
      label488:
      float f4 = Math.round(Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).getWidth() / Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).getZoom());
      if ((f1 >= f2 - f4 / 2.0F) && (f1 <= f3 + f4 / 2.0F))
      {
        x = Math.max(f2, Math.min(f3, f1));
        y = ((mTextView.getLayout().getLineTop(m) + mTextView.getLayout().getLineBottom(m)) / 2.0F + mTextView.getTotalPaddingTop() - mTextView.getScrollY());
        return true;
      }
      return false;
    }
    
    private void setVisible(boolean paramBoolean)
    {
      View localView = mContainer.getContentView();
      int i;
      if (paramBoolean) {
        i = 0;
      } else {
        i = 4;
      }
      localView.setVisibility(i);
    }
    
    private boolean shouldShow()
    {
      if (mIsDragging) {
        return true;
      }
      if (mTextView.isInBatchEditMode()) {
        return false;
      }
      return mTextView.isPositionVisible(mPositionX + mHotspotX + getHorizontalOffset(), mPositionY);
    }
    
    private void startTouchUpFilter(int paramInt)
    {
      mNumberPreviousOffsets = 0;
      addPositionToTouchUpFilter(paramInt);
    }
    
    private boolean tooLargeTextForMagnifier()
    {
      float f = Math.round(Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).getHeight() / Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).getZoom());
      Paint.FontMetrics localFontMetrics = mTextView.getPaint().getFontMetrics();
      boolean bool;
      if (descent - ascent > f) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    protected void dismiss()
    {
      mIsDragging = false;
      mContainer.dismiss();
      onDetached();
    }
    
    protected final void dismissMagnifier()
    {
      if (mMagnifierAnimator != null)
      {
        Editor.MagnifierMotionAnimator.access$6300(mMagnifierAnimator);
        Editor.access$6002(Editor.this, false);
        Editor.this.resumeBlink();
        setVisible(true);
        HandleView localHandleView = getOtherSelectionHandle();
        if (localHandleView != null) {
          localHandleView.setVisible(true);
        }
      }
    }
    
    public abstract int getCurrentCursorOffset();
    
    int getCursorHorizontalPosition(Layout paramLayout, int paramInt)
    {
      return (int)(getHorizontal(paramLayout, paramInt) - 0.5F);
    }
    
    protected int getCursorOffset()
    {
      return 0;
    }
    
    @VisibleForTesting
    public float getHorizontal(Layout paramLayout, int paramInt)
    {
      return paramLayout.getPrimaryHorizontal(paramInt);
    }
    
    protected abstract int getHorizontalGravity(boolean paramBoolean);
    
    protected abstract int getHotspotX(Drawable paramDrawable, boolean paramBoolean);
    
    public float getIdealVerticalOffset()
    {
      return mIdealVerticalOffset;
    }
    
    protected abstract int getMagnifierHandleTrigger();
    
    protected int getOffsetAtCoordinate(Layout paramLayout, int paramInt, float paramFloat)
    {
      return mTextView.getOffsetAtCoordinate(paramInt, paramFloat);
    }
    
    public void hide()
    {
      dismiss();
      Editor.this.getPositionListener().removeSubscriber(this);
    }
    
    public void invalidate()
    {
      super.invalidate();
      if (isShowing()) {
        positionAtCursorOffset(getCurrentCursorOffset(), true, false);
      }
    }
    
    protected boolean isAtRtlRun(Layout paramLayout, int paramInt)
    {
      return paramLayout.isRtlCharAt(paramInt);
    }
    
    public boolean isDragging()
    {
      return mIsDragging;
    }
    
    public boolean isShowing()
    {
      return mContainer.isShowing();
    }
    
    public boolean offsetHasBeenChanged()
    {
      int i = mNumberPreviousOffsets;
      boolean bool = true;
      if (i <= 1) {
        bool = false;
      }
      return bool;
    }
    
    public void onDetached() {}
    
    protected void onDraw(Canvas paramCanvas)
    {
      int i = mDrawable.getIntrinsicWidth();
      int j = getHorizontalOffset();
      mDrawable.setBounds(j, 0, j + i, mDrawable.getIntrinsicHeight());
      mDrawable.draw(paramCanvas);
    }
    
    void onHandleMoved() {}
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      setMeasuredDimension(getPreferredWidth(), getPreferredHeight());
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      Editor.this.updateFloatingToolbarVisibility(paramMotionEvent);
      float f1;
      float f2;
      float f3;
      float f4;
      float f5;
      float f6;
      switch (paramMotionEvent.getActionMasked())
      {
      default: 
        break;
      case 2: 
        f1 = paramMotionEvent.getRawX();
        f2 = mLastParentXOnScreen;
        f3 = mLastParentX;
        f4 = paramMotionEvent.getRawY() - mLastParentYOnScreen + mLastParentY;
        f5 = mTouchToWindowOffsetY - mLastParentY;
        f6 = f4 - mPositionY - mLastParentY;
        if (f5 < mIdealVerticalOffset) {
          f5 = Math.max(Math.min(f6, mIdealVerticalOffset), f5);
        } else {
          f5 = Math.min(Math.max(f6, mIdealVerticalOffset), f5);
        }
        mTouchToWindowOffsetY = (mLastParentY + f5);
        float f7 = mTouchToWindowOffsetX;
        f6 = mHotspotX;
        float f8 = getHorizontalOffset();
        f5 = mTouchToWindowOffsetY;
        float f9 = mTouchOffsetY;
        updatePosition(f1 - f2 + f3 - f7 + f6 + f8, f4 - f5 + f9, paramMotionEvent.isFromSource(4098));
        break;
      case 1: 
        filterOnTouchUp(paramMotionEvent.isFromSource(4098));
      case 3: 
        mIsDragging = false;
        updateDrawable();
        break;
      case 0: 
        startTouchUpFilter(getCurrentCursorOffset());
        Editor.PositionListener localPositionListener = Editor.this.getPositionListener();
        mLastParentX = localPositionListener.getPositionX();
        mLastParentY = localPositionListener.getPositionY();
        mLastParentXOnScreen = localPositionListener.getPositionXOnScreen();
        mLastParentYOnScreen = localPositionListener.getPositionYOnScreen();
        f4 = paramMotionEvent.getRawX();
        f5 = mLastParentXOnScreen;
        f3 = mLastParentX;
        f2 = paramMotionEvent.getRawY();
        f6 = mLastParentYOnScreen;
        f1 = mLastParentY;
        mTouchToWindowOffsetX = (f4 - f5 + f3 - mPositionX);
        mTouchToWindowOffsetY = (f2 - f6 + f1 - mPositionY);
        mIsDragging = true;
        mPreviousLineTouched = -1;
      }
      return true;
    }
    
    protected void positionAtCursorOffset(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    {
      if (mTextView.getLayout() == null)
      {
        prepareCursorControllers();
        return;
      }
      Layout localLayout = mTextView.getLayout();
      int i = mPreviousOffset;
      int j = 0;
      if (paramInt != i) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i != 0) || (paramBoolean1))
      {
        if (i != 0)
        {
          updateSelection(paramInt);
          if ((paramBoolean2) && (mHapticTextHandleEnabled)) {
            mTextView.performHapticFeedback(9);
          }
          addPositionToTouchUpFilter(paramInt);
        }
        i = localLayout.getLineForOffset(paramInt);
        int k;
        if ((mCursorInLineEnd) && (i > 0)) {
          k = i - 1;
        } else {
          k = i;
        }
        mPrevLine = k;
        mPositionX = (getCursorHorizontalPosition(localLayout, paramInt) - mHotspotX - getHorizontalOffset() + getCursorOffset());
        mPositionY = localLayout.getLineBottomWithoutSpacing(i);
        if ((mCursorInLineEnd) && (paramInt > 0)) {
          k = paramInt - 1;
        } else {
          k = paramInt;
        }
        if (mCursorInLineEnd) {
          j = (int)mTextView.getPaint().measureText(mTextView.getText().toString().substring(k, k + 1));
        }
        mPositionX = ((int)(localLayout.getPrimaryHorizontal(k) - 0.5F - mHotspotX - getHorizontalOffset() + getCursorOffset()));
        mPositionX += j;
        if ((mCursorInLineEnd) && (i > 0)) {
          i--;
        }
        mPositionY = localLayout.getLineBottom(i);
        mPositionX += mTextView.viewportToContentHorizontalOffset();
        mPositionY += mTextView.viewportToContentVerticalOffset();
        mPreviousOffset = paramInt;
        mPositionHasChanged = true;
      }
    }
    
    public void show()
    {
      if (isShowing()) {
        return;
      }
      Editor.this.getPositionListener().addSubscriber(this, true);
      mPreviousOffset = -1;
      positionAtCursorOffset(getCurrentCursorOffset(), false, false);
    }
    
    protected void updateDrawable()
    {
      if (mIsDragging) {
        return;
      }
      Layout localLayout = mTextView.getLayout();
      if (localLayout == null) {
        return;
      }
      int i = getCurrentCursorOffset();
      boolean bool = isAtRtlRun(localLayout, i);
      Drawable localDrawable1 = mDrawable;
      Drawable localDrawable2;
      if (bool) {
        localDrawable2 = mDrawableRtl;
      } else {
        localDrawable2 = mDrawableLtr;
      }
      mDrawable = localDrawable2;
      mHotspotX = getHotspotX(mDrawable, bool);
      mHorizontalGravity = getHorizontalGravity(bool);
      if ((localDrawable1 != mDrawable) && (isShowing()))
      {
        mPositionX = (getCursorHorizontalPosition(localLayout, i) - mHotspotX - getHorizontalOffset() + getCursorOffset());
        mPositionX += mTextView.viewportToContentHorizontalOffset();
        mPositionHasChanged = true;
        updatePosition(mLastParentX, mLastParentY, false, false);
        postInvalidate();
      }
    }
    
    protected final void updateMagnifier(MotionEvent paramMotionEvent)
    {
      if (mMagnifierAnimator == null) {
        return;
      }
      PointF localPointF = new PointF();
      int i;
      if ((!tooLargeTextForMagnifier()) && (obtainMagnifierShowCoordinates(paramMotionEvent, localPointF))) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0)
      {
        Editor.access$6002(Editor.this, true);
        mTextView.invalidateCursorPath();
        Editor.this.suspendBlink();
        Editor.MagnifierMotionAnimator.access$5500(mMagnifierAnimator).setOnOperationCompleteCallback(mHandlesVisibilityCallback);
        Editor.MagnifierMotionAnimator.access$6200(mMagnifierAnimator, x, y);
      }
      else
      {
        dismissMagnifier();
      }
    }
    
    protected abstract void updatePosition(float paramFloat1, float paramFloat2, boolean paramBoolean);
    
    public void updatePosition(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
      positionAtCursorOffset(getCurrentCursorOffset(), paramBoolean2, false);
      if ((paramBoolean1) || (mPositionHasChanged))
      {
        if (mIsDragging)
        {
          if ((paramInt1 != mLastParentX) || (paramInt2 != mLastParentY))
          {
            mTouchToWindowOffsetX += paramInt1 - mLastParentX;
            mTouchToWindowOffsetY += paramInt2 - mLastParentY;
            mLastParentX = paramInt1;
            mLastParentY = paramInt2;
          }
          onHandleMoved();
        }
        if (shouldShow())
        {
          int[] arrayOfInt = new int[2];
          arrayOfInt[0] = (mPositionX + mHotspotX + getHorizontalOffset());
          arrayOfInt[1] = mPositionY;
          mTextView.transformFromViewToWindowSpace(arrayOfInt);
          arrayOfInt[0] -= mHotspotX + getHorizontalOffset();
          if (isShowing()) {
            mContainer.update(arrayOfInt[0], arrayOfInt[1], -1, -1);
          } else {
            mContainer.showAtLocation(mTextView, 0, arrayOfInt[0], arrayOfInt[1]);
          }
        }
        else if (isShowing())
        {
          dismiss();
        }
        mPositionHasChanged = false;
      }
    }
    
    protected abstract void updateSelection(int paramInt);
  }
  
  static class InputContentType
  {
    boolean enterDown;
    Bundle extras;
    int imeActionId;
    CharSequence imeActionLabel;
    LocaleList imeHintLocales;
    int imeOptions = 0;
    TextView.OnEditorActionListener onEditorActionListener;
    String privateImeOptions;
    
    InputContentType() {}
  }
  
  static class InputMethodState
  {
    int mBatchEditNesting;
    int mChangedDelta;
    int mChangedEnd;
    int mChangedStart;
    boolean mContentChanged;
    boolean mCursorChanged;
    final ExtractedText mExtractedText = new ExtractedText();
    ExtractedTextRequest mExtractedTextRequest;
    boolean mSelectionModeChanged;
    
    InputMethodState() {}
  }
  
  private class InsertionHandleView
    extends Editor.HandleView
  {
    private static final int DELAY_BEFORE_HANDLE_FADES_OUT = 4000;
    private static final int RECENT_CUT_COPY_DURATION = 15000;
    private float mDownPositionX;
    private float mDownPositionY;
    private Runnable mHider;
    
    public InsertionHandleView(Drawable paramDrawable)
    {
      super(paramDrawable, paramDrawable, 16909047, null);
    }
    
    private void hideAfterDelay()
    {
      if (mHider == null) {
        mHider = new Runnable()
        {
          public void run()
          {
            hide();
          }
        };
      } else {
        removeHiderCallback();
      }
      mTextView.postDelayed(mHider, 4000L);
    }
    
    private void removeHiderCallback()
    {
      if (mHider != null) {
        mTextView.removeCallbacks(mHider);
      }
    }
    
    public int getCurrentCursorOffset()
    {
      return mTextView.getSelectionStart();
    }
    
    int getCursorHorizontalPosition(Layout paramLayout, int paramInt)
    {
      if (mDrawableForCursor != null)
      {
        float f = getHorizontal(paramLayout, paramInt);
        return Editor.this.clampHorizontalPosition(mDrawableForCursor, f) + mTempRect.left;
      }
      return super.getCursorHorizontalPosition(paramLayout, paramInt);
    }
    
    protected int getCursorOffset()
    {
      int i = super.getCursorOffset();
      int j = i;
      if (mDrawableForCursor != null)
      {
        mDrawableForCursor.getPadding(mTempRect);
        j = i + (mDrawableForCursor.getIntrinsicWidth() - mTempRect.left - mTempRect.right) / 2;
      }
      return j;
    }
    
    protected int getHorizontalGravity(boolean paramBoolean)
    {
      return 1;
    }
    
    protected int getHotspotX(Drawable paramDrawable, boolean paramBoolean)
    {
      return paramDrawable.getIntrinsicWidth() / 2;
    }
    
    protected int getMagnifierHandleTrigger()
    {
      return 0;
    }
    
    public void onDetached()
    {
      super.onDetached();
      removeHiderCallback();
    }
    
    void onHandleMoved()
    {
      super.onHandleMoved();
      removeHiderCallback();
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      switch (paramMotionEvent.getActionMasked())
      {
      default: 
        break;
      case 2: 
        updateMagnifier(paramMotionEvent);
        break;
      case 1: 
        if (!offsetHasBeenChanged())
        {
          float f1 = mDownPositionX - paramMotionEvent.getRawX();
          float f2 = mDownPositionY - paramMotionEvent.getRawY();
          int i = ViewConfiguration.get(mTextView.getContext()).getScaledTouchSlop();
          if (f1 * f1 + f2 * f2 < i * i) {
            if (mTextActionMode != null) {
              stopTextActionMode();
            } else {
              startInsertionActionMode();
            }
          }
        }
        else if (mTextActionMode != null)
        {
          mTextActionMode.invalidateContentRect();
        }
      case 3: 
        hideAfterDelay();
        dismissMagnifier();
        break;
      }
      mDownPositionX = paramMotionEvent.getRawX();
      mDownPositionY = paramMotionEvent.getRawY();
      updateMagnifier(paramMotionEvent);
      return bool;
    }
    
    public void show()
    {
      super.show();
      long l1 = SystemClock.uptimeMillis();
      long l2 = TextView.sLastCutCopyOrTextChangedTime;
      if ((mInsertionActionModeRunnable != null) && ((mTapState == 2) || (mTapState == 3) || (Editor.this.isCursorInsideEasyCorrectionSpan()))) {
        mTextView.removeCallbacks(mInsertionActionModeRunnable);
      }
      if ((mTapState != 2) && (mTapState != 3) && (!Editor.this.isCursorInsideEasyCorrectionSpan()) && (l1 - l2 < 15000L) && (mTextActionMode == null))
      {
        if (mInsertionActionModeRunnable == null) {
          Editor.access$6702(Editor.this, new Runnable()
          {
            public void run()
            {
              startInsertionActionMode();
            }
          });
        }
        mTextView.postDelayed(mInsertionActionModeRunnable, ViewConfiguration.getDoubleTapTimeout() + 1);
      }
      hideAfterDelay();
    }
    
    protected void updatePosition(float paramFloat1, float paramFloat2, boolean paramBoolean)
    {
      Layout localLayout = mTextView.getLayout();
      int j;
      if (localLayout != null)
      {
        if (mPreviousLineTouched == -1) {
          mPreviousLineTouched = mTextView.getLineAtCoordinate(paramFloat2);
        }
        int i = Editor.this.getCurrentLineAdjustedForSlop(localLayout, mPreviousLineTouched, paramFloat2);
        j = Editor.this.calculateTheFakeOffsetAdjustedForSlop(i, paramFloat1);
        mPreviousLineTouched = i;
      }
      else
      {
        j = Editor.this.calculateTheFakeOffset(paramFloat1, paramFloat2);
      }
      positionAtCursorOffset(j, false, paramBoolean);
      if (mTextActionMode != null) {
        Editor.this.invalidateActionMode();
      }
    }
    
    public void updateSelection(int paramInt)
    {
      Selection.setSelection((Spannable)mTextView.getText(), paramInt);
    }
  }
  
  private class InsertionPointCursorController
    implements Editor.CursorController
  {
    private Editor.InsertionHandleView mHandle;
    
    private InsertionPointCursorController() {}
    
    private Editor.InsertionHandleView getHandle()
    {
      if (mSelectHandleCenter == null) {
        Editor.access$4702(Editor.this, mTextView.getContext().getDrawable(mTextView.mTextSelectHandleRes));
      }
      if (mHandle == null) {
        mHandle = new Editor.InsertionHandleView(Editor.this, mSelectHandleCenter);
      }
      return mHandle;
    }
    
    public void hide()
    {
      if (mHandle != null) {
        mHandle.hide();
      }
    }
    
    public void invalidateHandle()
    {
      if (mHandle != null) {
        mHandle.invalidate();
      }
    }
    
    public boolean isActive()
    {
      boolean bool;
      if ((mHandle != null) && (mHandle.isShowing())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isCursorBeingModified()
    {
      boolean bool;
      if ((mHandle != null) && (mHandle.isDragging())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onDetached()
    {
      mTextView.getViewTreeObserver().removeOnTouchModeChangeListener(this);
      if (mHandle != null) {
        mHandle.onDetached();
      }
    }
    
    public void onTouchModeChanged(boolean paramBoolean)
    {
      if (!paramBoolean) {
        hide();
      }
    }
    
    public void show()
    {
      getHandle().show();
      if (mSelectionModifierCursorController != null) {
        mSelectionModifierCursorController.hide();
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface MagnifierHandleTrigger
  {
    public static final int INSERTION = 0;
    public static final int SELECTION_END = 2;
    public static final int SELECTION_START = 1;
  }
  
  private static class MagnifierMotionAnimator
  {
    private static final long DURATION = 100L;
    private float mAnimationCurrentX;
    private float mAnimationCurrentY;
    private float mAnimationStartX;
    private float mAnimationStartY;
    private final ValueAnimator mAnimator;
    private float mLastX;
    private float mLastY;
    private final Magnifier mMagnifier;
    private boolean mMagnifierIsShowing;
    
    private MagnifierMotionAnimator(Magnifier paramMagnifier)
    {
      mMagnifier = paramMagnifier;
      mAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
      mAnimator.setDuration(100L);
      mAnimator.setInterpolator(new LinearInterpolator());
      mAnimator.addUpdateListener(new _..Lambda.Editor.MagnifierMotionAnimator.E_RaelOMgCHAzvKgSSZE_hDYeIg(this));
    }
    
    private void dismiss()
    {
      mMagnifier.dismiss();
      mAnimator.cancel();
      mMagnifierIsShowing = false;
    }
    
    private void show(float paramFloat1, float paramFloat2)
    {
      int i;
      if ((mMagnifierIsShowing) && (paramFloat2 != mLastY)) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0)
      {
        if (mAnimator.isRunning())
        {
          mAnimator.cancel();
          mAnimationStartX = mAnimationCurrentX;
          mAnimationStartY = mAnimationCurrentY;
        }
        else
        {
          mAnimationStartX = mLastX;
          mAnimationStartY = mLastY;
        }
        mAnimator.start();
      }
      else if (!mAnimator.isRunning())
      {
        mMagnifier.show(paramFloat1, paramFloat2);
      }
      mLastX = paramFloat1;
      mLastY = paramFloat2;
      mMagnifierIsShowing = true;
    }
    
    private void update()
    {
      mMagnifier.update();
    }
  }
  
  private abstract class PinnedPopupWindow
    implements Editor.TextViewPositionListener
  {
    int mClippingLimitLeft;
    int mClippingLimitRight;
    protected ViewGroup mContentView;
    protected PopupWindow mPopupWindow;
    int mPositionX;
    int mPositionY;
    
    public PinnedPopupWindow()
    {
      setUp();
      createPopupWindow();
      mPopupWindow.setWindowLayoutType(1005);
      mPopupWindow.setWidth(-2);
      mPopupWindow.setHeight(-2);
      initContentView();
      this$1 = new ViewGroup.LayoutParams(-2, -2);
      mContentView.setLayoutParams(Editor.this);
      mPopupWindow.setContentView(mContentView);
    }
    
    private void computeLocalPosition()
    {
      measureContent();
      int i = mContentView.getMeasuredWidth();
      int j = getTextOffset();
      mPositionX = ((int)(mTextView.getLayout().getPrimaryHorizontal(j) - i / 2.0F));
      mPositionX += mTextView.viewportToContentHorizontalOffset();
      mPositionY = getVerticalLocalPosition(mTextView.getLayout().getLineForOffset(j));
      mPositionY += mTextView.viewportToContentVerticalOffset();
    }
    
    private void updatePosition(int paramInt1, int paramInt2)
    {
      int i = mPositionX;
      paramInt2 = clipVertically(mPositionY + paramInt2);
      DisplayMetrics localDisplayMetrics = mTextView.getResources().getDisplayMetrics();
      int j = mContentView.getMeasuredWidth();
      paramInt1 = Math.min(widthPixels - j + mClippingLimitRight, i + paramInt1);
      paramInt1 = Math.max(-mClippingLimitLeft, paramInt1);
      if (isShowing()) {
        mPopupWindow.update(paramInt1, paramInt2, -1, -1);
      } else {
        mPopupWindow.showAtLocation(mTextView, 0, paramInt1, paramInt2);
      }
    }
    
    protected abstract int clipVertically(int paramInt);
    
    protected abstract void createPopupWindow();
    
    protected abstract int getTextOffset();
    
    protected abstract int getVerticalLocalPosition(int paramInt);
    
    public void hide()
    {
      if (!isShowing()) {
        return;
      }
      mPopupWindow.dismiss();
      Editor.this.getPositionListener().removeSubscriber(this);
    }
    
    protected abstract void initContentView();
    
    public boolean isShowing()
    {
      return mPopupWindow.isShowing();
    }
    
    protected void measureContent()
    {
      DisplayMetrics localDisplayMetrics = mTextView.getResources().getDisplayMetrics();
      mContentView.measure(View.MeasureSpec.makeMeasureSpec(widthPixels, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(heightPixels, Integer.MIN_VALUE));
    }
    
    protected void setUp() {}
    
    public void show()
    {
      Editor.this.getPositionListener().addSubscriber(this, false);
      computeLocalPosition();
      Editor.PositionListener localPositionListener = Editor.this.getPositionListener();
      updatePosition(localPositionListener.getPositionX(), localPositionListener.getPositionY());
    }
    
    public void updatePosition(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
      if ((isShowing()) && (Editor.this.isOffsetVisible(getTextOffset())))
      {
        if (paramBoolean2) {
          computeLocalPosition();
        }
        updatePosition(paramInt1, paramInt2);
      }
      else
      {
        hide();
      }
    }
  }
  
  private class PositionListener
    implements ViewTreeObserver.OnPreDrawListener
  {
    private static final int MAXIMUM_NUMBER_OF_LISTENERS = 7;
    private boolean[] mCanMove = new boolean[7];
    private int mNumberOfListeners;
    private boolean mPositionHasChanged = true;
    private Editor.TextViewPositionListener[] mPositionListeners = new Editor.TextViewPositionListener[7];
    private int mPositionX;
    private int mPositionXOnScreen;
    private int mPositionY;
    private int mPositionYOnScreen;
    private boolean mScrollHasChanged;
    final int[] mTempCoords = new int[2];
    
    private PositionListener() {}
    
    private void updatePosition()
    {
      mTextView.getLocationInWindow(mTempCoords);
      boolean bool;
      if ((mTempCoords[0] == mPositionX) && (mTempCoords[1] == mPositionY)) {
        bool = false;
      } else {
        bool = true;
      }
      mPositionHasChanged = bool;
      mPositionX = mTempCoords[0];
      mPositionY = mTempCoords[1];
      mTextView.getLocationOnScreen(mTempCoords);
      mPositionXOnScreen = mTempCoords[0];
      mPositionYOnScreen = mTempCoords[1];
    }
    
    public void addSubscriber(Editor.TextViewPositionListener paramTextViewPositionListener, boolean paramBoolean)
    {
      if (mNumberOfListeners == 0)
      {
        updatePosition();
        mTextView.getViewTreeObserver().addOnPreDrawListener(this);
      }
      int i = -1;
      int j = 0;
      while (j < 7)
      {
        Editor.TextViewPositionListener localTextViewPositionListener = mPositionListeners[j];
        if (localTextViewPositionListener == paramTextViewPositionListener) {
          return;
        }
        int k = i;
        if (i < 0)
        {
          k = i;
          if (localTextViewPositionListener == null) {
            k = j;
          }
        }
        j++;
        i = k;
      }
      mPositionListeners[i] = paramTextViewPositionListener;
      mCanMove[i] = paramBoolean;
      mNumberOfListeners += 1;
    }
    
    public int getPositionX()
    {
      return mPositionX;
    }
    
    public int getPositionXOnScreen()
    {
      return mPositionXOnScreen;
    }
    
    public int getPositionY()
    {
      return mPositionY;
    }
    
    public int getPositionYOnScreen()
    {
      return mPositionYOnScreen;
    }
    
    public boolean onPreDraw()
    {
      updatePosition();
      for (int i = 0; i < 7; i++) {
        if ((mPositionHasChanged) || (mScrollHasChanged) || (mCanMove[i] != 0))
        {
          Editor.TextViewPositionListener localTextViewPositionListener = mPositionListeners[i];
          if (localTextViewPositionListener != null) {
            localTextViewPositionListener.updatePosition(mPositionX, mPositionY, mPositionHasChanged, mScrollHasChanged);
          }
        }
      }
      mScrollHasChanged = false;
      return true;
    }
    
    public void onScrollChanged()
    {
      mScrollHasChanged = true;
    }
    
    public void removeSubscriber(Editor.TextViewPositionListener paramTextViewPositionListener)
    {
      for (int i = 0; i < 7; i++) {
        if (mPositionListeners[i] == paramTextViewPositionListener)
        {
          mPositionListeners[i] = null;
          mNumberOfListeners -= 1;
          break;
        }
      }
      if (mNumberOfListeners == 0) {
        mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
      }
    }
  }
  
  static final class ProcessTextIntentActionsHandler
  {
    private final SparseArray<AccessibilityNodeInfo.AccessibilityAction> mAccessibilityActions = new SparseArray();
    private final SparseArray<Intent> mAccessibilityIntents = new SparseArray();
    private final Context mContext;
    private final Editor mEditor;
    private final PackageManager mPackageManager;
    private final String mPackageName;
    private final List<ResolveInfo> mSupportedActivities = new ArrayList();
    private final TextView mTextView;
    
    private ProcessTextIntentActionsHandler(Editor paramEditor)
    {
      mEditor = ((Editor)Preconditions.checkNotNull(paramEditor));
      mTextView = ((TextView)Preconditions.checkNotNull(mEditor.mTextView));
      mContext = ((Context)Preconditions.checkNotNull(mTextView.getContext()));
      mPackageManager = ((PackageManager)Preconditions.checkNotNull(mContext.getPackageManager()));
      mPackageName = ((String)Preconditions.checkNotNull(mContext.getPackageName()));
    }
    
    private Intent createProcessTextIntent()
    {
      return new Intent().setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
    }
    
    private Intent createProcessTextIntentForResolveInfo(ResolveInfo paramResolveInfo)
    {
      return createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", mTextView.isTextEditable() ^ true).setClassName(activityInfo.packageName, activityInfo.name);
    }
    
    private boolean fireIntent(Intent paramIntent)
    {
      if ((paramIntent != null) && ("android.intent.action.PROCESS_TEXT".equals(paramIntent.getAction())))
      {
        paramIntent.putExtra("android.intent.extra.PROCESS_TEXT", (String)TextUtils.trimToParcelableSize(mTextView.getSelectedText()));
        Editor.access$5002(mEditor, true);
        mTextView.startActivityForResult(paramIntent, 100);
        return true;
      }
      return false;
    }
    
    private CharSequence getLabel(ResolveInfo paramResolveInfo)
    {
      return paramResolveInfo.loadLabel(mPackageManager);
    }
    
    private boolean isSupportedActivity(ResolveInfo paramResolveInfo)
    {
      boolean bool;
      if ((!mPackageName.equals(activityInfo.packageName)) && ((!activityInfo.exported) || ((activityInfo.permission != null) && (mContext.checkSelfPermission(activityInfo.permission) != 0)))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    private void loadSupportedActivities()
    {
      mSupportedActivities.clear();
      if (!mContext.canStartActivityForResult()) {
        return;
      }
      Object localObject = mTextView.getContext().getPackageManager();
      Iterator localIterator = ((PackageManager)localObject).queryIntentActivities(createProcessTextIntent(), 0).iterator();
      while (localIterator.hasNext())
      {
        localObject = (ResolveInfo)localIterator.next();
        if (isSupportedActivity((ResolveInfo)localObject)) {
          mSupportedActivities.add(localObject);
        }
      }
    }
    
    public void initializeAccessibilityActions()
    {
      mAccessibilityIntents.clear();
      mAccessibilityActions.clear();
      int i = 0;
      loadSupportedActivities();
      Iterator localIterator = mSupportedActivities.iterator();
      while (localIterator.hasNext())
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localIterator.next();
        int j = 268435712 + i;
        mAccessibilityActions.put(j, new AccessibilityNodeInfo.AccessibilityAction(j, getLabel(localResolveInfo)));
        mAccessibilityIntents.put(j, createProcessTextIntentForResolveInfo(localResolveInfo));
        i++;
      }
    }
    
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      for (int i = 0; i < mAccessibilityActions.size(); i++) {
        paramAccessibilityNodeInfo.addAction((AccessibilityNodeInfo.AccessibilityAction)mAccessibilityActions.valueAt(i));
      }
    }
    
    public void onInitializeMenu(Menu paramMenu)
    {
      loadSupportedActivities();
      int i = mSupportedActivities.size();
      for (int j = 0; j < i; j++)
      {
        ResolveInfo localResolveInfo = (ResolveInfo)mSupportedActivities.get(j);
        paramMenu.add(0, 0, 100 + j, getLabel(localResolveInfo)).setIntent(createProcessTextIntentForResolveInfo(localResolveInfo)).setShowAsAction(0);
      }
    }
    
    public boolean performAccessibilityAction(int paramInt)
    {
      return fireIntent((Intent)mAccessibilityIntents.get(paramInt));
    }
    
    public boolean performMenuItemAction(MenuItem paramMenuItem)
    {
      return fireIntent(paramMenuItem.getIntent());
    }
  }
  
  @VisibleForTesting
  public final class SelectionHandleView
    extends Editor.HandleView
  {
    private final int mHandleType;
    private boolean mInWord = false;
    private boolean mLanguageDirectionChanged = false;
    private float mPrevX;
    private final float mTextViewEdgeSlop;
    private final int[] mTextViewLocation = new int[2];
    private float mTouchWordDelta;
    
    public SelectionHandleView(Drawable paramDrawable1, Drawable paramDrawable2, int paramInt1, int paramInt2)
    {
      super(paramDrawable1, paramDrawable2, paramInt1, null);
      mHandleType = paramInt2;
      mTextViewEdgeSlop = (ViewConfiguration.get(mTextView.getContext()).getScaledTouchSlop() * 4);
    }
    
    private float getHorizontal(Layout paramLayout, int paramInt, boolean paramBoolean)
    {
      int i = paramLayout.getLineForOffset(paramInt);
      boolean bool1 = false;
      int j;
      if (paramBoolean) {
        j = paramInt;
      } else {
        j = Math.max(paramInt - 1, 0);
      }
      boolean bool2 = paramLayout.isRtlCharAt(j);
      paramBoolean = bool1;
      if (paramLayout.getParagraphDirection(i) == -1) {
        paramBoolean = true;
      }
      float f;
      if (bool2 == paramBoolean) {
        f = paramLayout.getPrimaryHorizontal(paramInt);
      } else {
        f = paramLayout.getSecondaryHorizontal(paramInt);
      }
      return f;
    }
    
    private boolean isStartHandle()
    {
      boolean bool;
      if (mHandleType == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void positionAndAdjustForCrossingHandles(int paramInt, boolean paramBoolean)
    {
      int i;
      if (isStartHandle()) {
        i = mTextView.getSelectionEnd();
      } else {
        i = mTextView.getSelectionStart();
      }
      int j;
      if ((!isStartHandle()) || (paramInt < i))
      {
        j = paramInt;
        if (!isStartHandle())
        {
          j = paramInt;
          if (paramInt > i) {}
        }
      }
      else
      {
        mTouchWordDelta = 0.0F;
        Layout localLayout = mTextView.getLayout();
        if ((localLayout != null) && (paramInt != i))
        {
          float f1 = getHorizontal(localLayout, paramInt);
          float f2 = getHorizontal(localLayout, i, isStartHandle() ^ true);
          float f3 = getHorizontal(localLayout, mPreviousOffset);
          if (((f3 < f2) && (f1 < f2)) || ((f3 > f2) && (f1 > f2)))
          {
            paramInt = getCurrentCursorOffset();
            if (!isStartHandle()) {
              paramInt = Math.max(paramInt - 1, 0);
            }
            long l = localLayout.getRunRange(paramInt);
            if (isStartHandle()) {
              paramInt = TextUtils.unpackRangeStartFromLong(l);
            } else {
              paramInt = TextUtils.unpackRangeEndFromLong(l);
            }
            positionAtCursorOffset(paramInt, false, paramBoolean);
            return;
          }
        }
        j = Editor.this.getNextCursorOffset(i, isStartHandle() ^ true);
      }
      positionAtCursorOffset(j, false, paramBoolean);
    }
    
    private boolean positionNearEdgeOfScrollingView(float paramFloat, boolean paramBoolean)
    {
      mTextView.getLocationOnScreen(mTextViewLocation);
      boolean bool1 = isStartHandle();
      boolean bool2 = true;
      boolean bool3 = true;
      if (paramBoolean == bool1)
      {
        if (paramFloat > mTextViewLocation[0] + mTextView.getWidth() - mTextView.getPaddingRight() - mTextViewEdgeSlop) {
          paramBoolean = bool3;
        } else {
          paramBoolean = false;
        }
      }
      else if (paramFloat < mTextViewLocation[0] + mTextView.getPaddingLeft() + mTextViewEdgeSlop) {
        paramBoolean = bool2;
      } else {
        paramBoolean = false;
      }
      return paramBoolean;
    }
    
    public int getCurrentCursorOffset()
    {
      int i;
      if (isStartHandle()) {
        i = mTextView.getSelectionStart();
      } else {
        i = mTextView.getSelectionEnd();
      }
      return i;
    }
    
    public float getHorizontal(Layout paramLayout, int paramInt)
    {
      return getHorizontal(paramLayout, paramInt, isStartHandle());
    }
    
    protected int getHorizontalGravity(boolean paramBoolean)
    {
      int i;
      if (paramBoolean == isStartHandle()) {
        i = 3;
      } else {
        i = 5;
      }
      return i;
    }
    
    protected int getHotspotX(Drawable paramDrawable, boolean paramBoolean)
    {
      if (paramBoolean == isStartHandle()) {
        return paramDrawable.getIntrinsicWidth() / 4;
      }
      return paramDrawable.getIntrinsicWidth() * 3 / 4;
    }
    
    protected int getMagnifierHandleTrigger()
    {
      int i;
      if (isStartHandle()) {
        i = 1;
      } else {
        i = 2;
      }
      return i;
    }
    
    protected int getOffsetAtCoordinate(Layout paramLayout, int paramInt, float paramFloat)
    {
      paramFloat = mTextView.convertToLocalHorizontalCoordinate(paramFloat);
      boolean bool1 = true;
      int i = paramLayout.getOffsetForHorizontal(paramInt, paramFloat, true);
      if (!paramLayout.isLevelBoundary(i)) {
        return i;
      }
      int j = paramLayout.getOffsetForHorizontal(paramInt, paramFloat, false);
      int k = getCurrentCursorOffset();
      int m = Math.abs(i - k);
      int n = Math.abs(j - k);
      if (m < n) {
        return i;
      }
      if (m > n) {
        return j;
      }
      if (!isStartHandle()) {
        k = Math.max(k - 1, 0);
      }
      boolean bool2 = paramLayout.isRtlCharAt(k);
      if (paramLayout.getParagraphDirection(paramInt) != -1) {
        bool1 = false;
      }
      if (bool2 == bool1) {
        paramInt = i;
      } else {
        paramInt = j;
      }
      return paramInt;
    }
    
    protected boolean isAtRtlRun(Layout paramLayout, int paramInt)
    {
      if (!isStartHandle()) {
        paramInt = Math.max(paramInt - 1, 0);
      }
      return paramLayout.isRtlCharAt(paramInt);
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      switch (paramMotionEvent.getActionMasked())
      {
      default: 
        break;
      case 2: 
        updateMagnifier(paramMotionEvent);
        break;
      case 1: 
      case 3: 
        dismissMagnifier();
        break;
      case 0: 
        mTouchWordDelta = 0.0F;
        mPrevX = -1.0F;
        updateMagnifier(paramMotionEvent);
      }
      return bool;
    }
    
    protected void positionAtCursorOffset(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    {
      super.positionAtCursorOffset(paramInt, paramBoolean1, paramBoolean2);
      if ((paramInt != -1) && (!Editor.this.getWordIteratorWithText().isBoundary(paramInt))) {
        paramBoolean1 = true;
      } else {
        paramBoolean1 = false;
      }
      mInWord = paramBoolean1;
    }
    
    protected void updatePosition(float paramFloat1, float paramFloat2, boolean paramBoolean)
    {
      Layout localLayout = mTextView.getLayout();
      if (localLayout == null)
      {
        positionAndAdjustForCrossingHandles(Editor.this.calculateTheFakeOffset(paramFloat1, paramFloat2), paramBoolean);
        return;
      }
      if (mPreviousLineTouched == -1) {
        mPreviousLineTouched = mTextView.getLineAtCoordinate(paramFloat2);
      }
      if (isStartHandle()) {
        i = mTextView.getSelectionEnd();
      } else {
        i = mTextView.getSelectionStart();
      }
      int j = Editor.this.getCurrentLineAdjustedForSlop(localLayout, mPreviousLineTouched, paramFloat2);
      int k = Editor.this.calculateTheFakeOffsetAdjustedForSlop(j, paramFloat1);
      int m;
      int n;
      if ((!isStartHandle()) || (k < i))
      {
        m = j;
        n = k;
        if (!isStartHandle())
        {
          m = j;
          n = k;
          if (k > i) {}
        }
      }
      else
      {
        m = localLayout.getLineForOffset(i);
        n = Editor.this.calculateTheFakeOffsetAdjustedForSlop(m, paramFloat1);
      }
      int i = n;
      k = Editor.this.getWordEnd(i);
      j = Editor.this.getWordStart(i);
      if (mPrevX == -1.0F) {
        mPrevX = paramFloat1;
      }
      int i1 = getCurrentCursorOffset();
      boolean bool1 = isAtRtlRun(localLayout, i1);
      boolean bool2 = isAtRtlRun(localLayout, i);
      boolean bool3 = localLayout.isLevelBoundary(i);
      if (!bool3)
      {
        if ((bool1) && (!bool2)) {}
        for (;;)
        {
          break;
          if ((bool1) || (!bool2))
          {
            if ((mLanguageDirectionChanged) && (!bool3))
            {
              positionAndAdjustForCrossingHandles(i, paramBoolean);
              mTouchWordDelta = 0.0F;
              mLanguageDirectionChanged = false;
              return;
            }
            paramFloat2 = paramFloat1 - mPrevX;
            int i2;
            if (isStartHandle())
            {
              if (m < mPreviousLineTouched) {
                i2 = 1;
              } else {
                i2 = 0;
              }
            }
            else if (m > mPreviousLineTouched) {
              i2 = 1;
            } else {
              i2 = 0;
            }
            int i3;
            if (bool2 == isStartHandle())
            {
              if (paramFloat2 > 0.0F) {
                i3 = 1;
              } else {
                i3 = 0;
              }
              i2 |= i3;
            }
            else
            {
              if (paramFloat2 < 0.0F) {
                i3 = 1;
              } else {
                i3 = 0;
              }
              i2 |= i3;
            }
            if ((mTextView.getHorizontallyScrolling()) && (positionNearEdgeOfScrollingView(paramFloat1, bool2)))
            {
              if ((isStartHandle()) && (mTextView.getScrollX() != 0)) {
                break label520;
              }
              if (!isStartHandle())
              {
                TextView localTextView = mTextView;
                if (bool2) {
                  i3 = -1;
                } else {
                  i3 = 1;
                }
                if (localTextView.canScrollHorizontally(i3)) {
                  label520:
                  if (((i2 != 0) && (((isStartHandle()) && (i < i1)) || ((!isStartHandle()) && (i > i1)))) || (i2 == 0))
                  {
                    mTouchWordDelta = 0.0F;
                    if (bool2 == isStartHandle()) {
                      i = localLayout.getOffsetToRightOf(mPreviousOffset);
                    } else {
                      i = localLayout.getOffsetToLeftOf(mPreviousOffset);
                    }
                    positionAndAdjustForCrossingHandles(i, paramBoolean);
                    return;
                  }
                }
              }
            }
            if (i2 != 0)
            {
              if (isStartHandle()) {
                i2 = j;
              } else {
                i2 = k;
              }
              if (((!mInWord) || (isStartHandle() ? m < mPrevLine : m > mPrevLine)) && (bool2 == isAtRtlRun(localLayout, i2))) {
                i3 = 1;
              } else {
                i3 = 0;
              }
              if (i3 != 0)
              {
                i3 = i2;
                if (localLayout.getLineForOffset(i2) != m)
                {
                  if (isStartHandle()) {
                    i2 = localLayout.getLineStart(m);
                  } else {
                    i2 = localLayout.getLineEnd(m);
                  }
                  i3 = i2;
                }
                if (isStartHandle()) {
                  i2 = k - (k - i3) / 2;
                } else {
                  i2 = (i3 - j) / 2 + j;
                }
                if (isStartHandle())
                {
                  if (i > i2) {
                    if (m >= mPrevLine) {
                      break label805;
                    }
                  }
                  i = j;
                }
                for (;;)
                {
                  break;
                  label805:
                  if ((!isStartHandle()) && ((i >= i2) || (m > mPrevLine))) {
                    i = k;
                  } else {
                    i = mPreviousOffset;
                  }
                }
                Editor.access$5402(Editor.this, false);
                j = i;
              }
              else
              {
                j = i;
              }
              if (((isStartHandle()) && (j < n)) || ((!isStartHandle()) && (j > n)))
              {
                paramFloat2 = getHorizontal(localLayout, j);
                mTouchWordDelta = (mTextView.convertToLocalHorizontalCoordinate(paramFloat1) - paramFloat2);
              }
              else
              {
                mTouchWordDelta = 0.0F;
              }
              i = 1;
            }
            else
            {
              i3 = Editor.this.calculateTheFakeOffsetAdjustedForSlop(m, paramFloat1 - mTouchWordDelta);
              if (isStartHandle()) {
                if ((i3 <= mPreviousOffset) && (m <= mPrevLine))
                {
                  i2 = 0;
                  break label1016;
                }
              }
              for (;;)
              {
                i2 = 1;
                break label1016;
                if ((i3 >= mPreviousOffset) && (m >= mPrevLine)) {
                  break;
                }
              }
              label1016:
              if (i2 != 0)
              {
                if (m != mPrevLine)
                {
                  if (isStartHandle()) {
                    i = j;
                  } else {
                    i = k;
                  }
                  Editor.access$5402(Editor.this, false);
                  if (((isStartHandle()) && (i < n)) || ((!isStartHandle()) && (i > n)))
                  {
                    paramFloat2 = getHorizontal(localLayout, i);
                    mTouchWordDelta = (mTextView.convertToLocalHorizontalCoordinate(paramFloat1) - paramFloat2);
                  }
                  else
                  {
                    mTouchWordDelta = 0.0F;
                  }
                  j = i;
                }
                else
                {
                  j = i3;
                }
                i = 1;
              }
              else
              {
                if (((isStartHandle()) && (i3 < mPreviousOffset)) || ((!isStartHandle()) && (i3 > mPreviousOffset))) {
                  mTouchWordDelta = (mTextView.convertToLocalHorizontalCoordinate(paramFloat1) - getHorizontal(localLayout, mPreviousOffset));
                }
                n = 0;
                j = i;
                i = n;
              }
            }
            if (i != 0)
            {
              mPreviousLineTouched = m;
              positionAndAdjustForCrossingHandles(j, paramBoolean);
            }
            mPrevX = paramFloat1;
            return;
          }
        }
      }
      mLanguageDirectionChanged = true;
      mTouchWordDelta = 0.0F;
      positionAndAdjustForCrossingHandles(i, paramBoolean);
    }
    
    protected void updateSelection(int paramInt)
    {
      if (isStartHandle()) {
        Selection.setSelection((Spannable)mTextView.getText(), paramInt, mTextView.getSelectionEnd());
      } else {
        Selection.setSelection((Spannable)mTextView.getText(), mTextView.getSelectionStart(), paramInt);
      }
      updateDrawable();
      if (mTextActionMode != null) {
        Editor.this.invalidateActionMode();
      }
    }
  }
  
  class SelectionModifierCursorController
    implements Editor.CursorController
  {
    private static final int DRAG_ACCELERATOR_MODE_CHARACTER = 1;
    private static final int DRAG_ACCELERATOR_MODE_INACTIVE = 0;
    private static final int DRAG_ACCELERATOR_MODE_PARAGRAPH = 3;
    private static final int DRAG_ACCELERATOR_MODE_WORD = 2;
    private float mDownPositionX;
    private float mDownPositionY;
    private int mDragAcceleratorMode = 0;
    private Editor.SelectionHandleView mEndHandle;
    private boolean mGestureStayedInTapRegion;
    private boolean mHaventMovedEnoughToStartDrag;
    private int mLineSelectionIsOn = -1;
    private int mMaxTouchOffset;
    private int mMinTouchOffset;
    private Editor.SelectionHandleView mStartHandle;
    private int mStartOffset = -1;
    private boolean mSwitchedLines = false;
    
    SelectionModifierCursorController()
    {
      resetTouchOffsets();
    }
    
    private void initDrawables()
    {
      if (mSelectHandleLeft == null) {
        Editor.access$4402(Editor.this, mTextView.getContext().getDrawable(mTextView.mTextSelectHandleLeftRes));
      }
      if (mSelectHandleRight == null) {
        Editor.access$4502(Editor.this, mTextView.getContext().getDrawable(mTextView.mTextSelectHandleRightRes));
      }
    }
    
    private void initHandles()
    {
      if (mStartHandle == null) {
        mStartHandle = new Editor.SelectionHandleView(Editor.this, mSelectHandleLeft, mSelectHandleRight, 16909337, 0);
      }
      if (mEndHandle == null) {
        mEndHandle = new Editor.SelectionHandleView(Editor.this, mSelectHandleRight, mSelectHandleLeft, 16909336, 1);
      }
      mStartHandle.show();
      mEndHandle.show();
      hideInsertionPointCursorController();
    }
    
    private void resetDragAcceleratorState()
    {
      mStartOffset = -1;
      mDragAcceleratorMode = 0;
      mSwitchedLines = false;
      int i = mTextView.getSelectionStart();
      int j = mTextView.getSelectionEnd();
      if ((i >= 0) && (j >= 0))
      {
        if (i > j) {
          Selection.setSelection((Spannable)mTextView.getText(), j, i);
        }
      }
      else {
        Selection.removeSelection((Spannable)mTextView.getText());
      }
    }
    
    private boolean selectCurrentParagraphAndStartDrag()
    {
      if (mInsertionActionModeRunnable != null) {
        mTextView.removeCallbacks(mInsertionActionModeRunnable);
      }
      stopTextActionMode();
      if (!Editor.this.selectCurrentParagraph()) {
        return false;
      }
      enterDrag(3);
      return true;
    }
    
    private void updateCharacterBasedSelection(MotionEvent paramMotionEvent)
    {
      int i = mTextView.getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY());
      updateSelectionInternal(mStartOffset, i, paramMotionEvent.isFromSource(4098));
    }
    
    private void updateMinAndMaxOffsets(MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getPointerCount();
      for (int j = 0; j < i; j++)
      {
        int k = Editor.this.calculateTheFakeOffset(paramMotionEvent.getX(j), paramMotionEvent.getY(j));
        if (k < mMinTouchOffset) {
          mMinTouchOffset = k;
        }
        if (k > mMaxTouchOffset) {
          mMaxTouchOffset = k;
        }
      }
    }
    
    private void updateParagraphBasedSelection(MotionEvent paramMotionEvent)
    {
      int i = mTextView.getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY());
      int j = Math.min(i, mStartOffset);
      i = Math.max(i, mStartOffset);
      long l = Editor.this.getParagraphsRange(j, i);
      i = TextUtils.unpackRangeStartFromLong(l);
      j = TextUtils.unpackRangeEndFromLong(l);
      updateSelectionInternal(i, j, paramMotionEvent.isFromSource(4098));
    }
    
    private void updateSelection(MotionEvent paramMotionEvent)
    {
      if (mTextView.getLayout() != null) {
        switch (mDragAcceleratorMode)
        {
        default: 
          break;
        case 3: 
          updateParagraphBasedSelection(paramMotionEvent);
          break;
        case 2: 
          updateWordBasedSelection(paramMotionEvent);
          break;
        case 1: 
          updateCharacterBasedSelection(paramMotionEvent);
        }
      }
    }
    
    private void updateSelectionInternal(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      int i;
      if ((paramBoolean) && (mHapticTextHandleEnabled) && ((mTextView.getSelectionStart() != paramInt1) || (mTextView.getSelectionEnd() != paramInt2))) {
        i = 1;
      } else {
        i = 0;
      }
      Selection.setSelection((Spannable)mTextView.getText(), paramInt1, paramInt2);
      if (i != 0) {
        mTextView.performHapticFeedback(9);
      }
    }
    
    private void updateWordBasedSelection(MotionEvent paramMotionEvent)
    {
      if (mHaventMovedEnoughToStartDrag) {
        return;
      }
      boolean bool = paramMotionEvent.isFromSource(8194);
      ViewConfiguration localViewConfiguration = ViewConfiguration.get(mTextView.getContext());
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      int i;
      if (bool)
      {
        i = mTextView.getLineAtCoordinate(f2);
      }
      else
      {
        float f3 = f2;
        if (mSwitchedLines)
        {
          j = localViewConfiguration.getScaledTouchSlop();
          if (mStartHandle != null) {
            f3 = mStartHandle.getIdealVerticalOffset();
          } else {
            f3 = j;
          }
          f3 = f2 - f3;
        }
        i = Editor.this.getCurrentLineAdjustedForSlop(mTextView.getLayout(), mLineSelectionIsOn, f3);
        if ((!mSwitchedLines) && (i != mLineSelectionIsOn))
        {
          mSwitchedLines = true;
          return;
        }
      }
      int j = mTextView.getOffsetAtCoordinate(i, f1);
      int k;
      if (mStartOffset < j)
      {
        j = Editor.this.getWordEnd(j);
        k = Editor.this.getWordStart(mStartOffset);
      }
      else
      {
        int m = Editor.this.getWordStart(j);
        int n = Editor.this.getWordEnd(mStartOffset);
        j = m;
        k = n;
        if (n == m)
        {
          j = Editor.this.getNextCursorOffset(m, false);
          k = n;
        }
      }
      mLineSelectionIsOn = i;
      updateSelectionInternal(k, j, paramMotionEvent.isFromSource(4098));
    }
    
    public void enterDrag(int paramInt)
    {
      show();
      mDragAcceleratorMode = paramInt;
      mStartOffset = mTextView.getOffsetForPosition(mLastDownPositionX, mLastDownPositionY);
      mLineSelectionIsOn = mTextView.getLineAtCoordinate(mLastDownPositionY);
      hide();
      mTextView.getParent().requestDisallowInterceptTouchEvent(true);
      mTextView.cancelLongPress();
    }
    
    public int getMaxTouchOffset()
    {
      return mMaxTouchOffset;
    }
    
    public int getMinTouchOffset()
    {
      return mMinTouchOffset;
    }
    
    public void hide()
    {
      if (mStartHandle != null) {
        mStartHandle.hide();
      }
      if (mEndHandle != null) {
        mEndHandle.hide();
      }
    }
    
    public void invalidateHandles()
    {
      if (mStartHandle != null) {
        mStartHandle.invalidate();
      }
      if (mEndHandle != null) {
        mEndHandle.invalidate();
      }
    }
    
    public boolean isActive()
    {
      boolean bool;
      if ((mStartHandle != null) && (mStartHandle.isShowing())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isCursorBeingModified()
    {
      boolean bool;
      if ((!isDragAcceleratorActive()) && (!isSelectionStartDragged()) && ((mEndHandle == null) || (!mEndHandle.isDragging()))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean isDragAcceleratorActive()
    {
      boolean bool;
      if (mDragAcceleratorMode != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isSelectionStartDragged()
    {
      boolean bool;
      if ((mStartHandle != null) && (mStartHandle.isDragging())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onDetached()
    {
      mTextView.getViewTreeObserver().removeOnTouchModeChangeListener(this);
      if (mStartHandle != null) {
        mStartHandle.onDetached();
      }
      if (mEndHandle != null) {
        mEndHandle.onDetached();
      }
    }
    
    public void onTouchEvent(MotionEvent paramMotionEvent)
    {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      boolean bool1 = paramMotionEvent.isFromSource(8194);
      int i = paramMotionEvent.getActionMasked();
      int j = 0;
      float f3;
      float f4;
      switch (i)
      {
      case 3: 
      case 4: 
      default: 
        break;
      case 5: 
      case 6: 
        if (mTextView.getContext().getPackageManager().hasSystemFeature("android.hardware.touchscreen.multitouch.distinct")) {
          updateMinAndMaxOffsets(paramMotionEvent);
        }
        break;
      case 2: 
        ViewConfiguration localViewConfiguration = ViewConfiguration.get(mTextView.getContext());
        j = localViewConfiguration.getScaledTouchSlop();
        if ((mGestureStayedInTapRegion) || (mHaventMovedEnoughToStartDrag))
        {
          f3 = f1 - mDownPositionX;
          f4 = f2 - mDownPositionY;
          f4 = f3 * f3 + f4 * f4;
          boolean bool2;
          if (mGestureStayedInTapRegion)
          {
            i = localViewConfiguration.getScaledDoubleTapTouchSlop();
            if (f4 <= i * i) {
              bool2 = true;
            } else {
              bool2 = false;
            }
            mGestureStayedInTapRegion = bool2;
          }
          if (mHaventMovedEnoughToStartDrag)
          {
            if (f4 <= j * j) {
              bool2 = true;
            } else {
              bool2 = false;
            }
            mHaventMovedEnoughToStartDrag = bool2;
          }
        }
        if ((bool1) && (!isDragAcceleratorActive()))
        {
          j = mTextView.getOffsetForPosition(f1, f2);
          if ((mTextView.hasSelection()) && ((!mHaventMovedEnoughToStartDrag) || (mStartOffset != j)) && (j >= mTextView.getSelectionStart()) && (j <= mTextView.getSelectionEnd()))
          {
            Editor.this.startDragAndDrop();
            break;
          }
          if (mStartOffset != j)
          {
            stopTextActionMode();
            enterDrag(1);
            mDiscardNextActionUp = true;
            mHaventMovedEnoughToStartDrag = false;
          }
        }
        if ((mStartHandle == null) || (!mStartHandle.isShowing())) {
          updateSelection(paramMotionEvent);
        }
        break;
      case 1: 
        if (isDragAcceleratorActive())
        {
          updateSelection(paramMotionEvent);
          mTextView.getParent().requestDisallowInterceptTouchEvent(false);
          resetDragAcceleratorState();
          if (mTextView.hasSelection()) {
            startSelectionActionModeAsync(mHaventMovedEnoughToStartDrag);
          }
        }
        break;
      case 0: 
        if (Editor.this.extractedTextModeWillBeStarted())
        {
          hide();
        }
        else
        {
          i = Editor.this.calculateTheFakeOffset(f1, f2);
          mMaxTouchOffset = i;
          mMinTouchOffset = i;
          if ((mGestureStayedInTapRegion) && ((mTapState == 2) || (mTapState == 3)))
          {
            f4 = f1 - mDownPositionX;
            f3 = f2 - mDownPositionY;
            i = ViewConfiguration.get(mTextView.getContext()).getScaledDoubleTapSlop();
            if (f4 * f4 + f3 * f3 < i * i) {
              j = 1;
            }
            if ((j != 0) && ((bool1) || (Editor.this.isPositionOnText(f1, f2))))
            {
              if (mTapState == 2) {
                Editor.this.selectCurrentWordAndStartDrag();
              } else if (mTapState == 3) {
                selectCurrentParagraphAndStartDrag();
              }
              mDiscardNextActionUp = true;
            }
          }
          mDownPositionX = f1;
          mDownPositionY = f2;
          mGestureStayedInTapRegion = true;
          mHaventMovedEnoughToStartDrag = true;
        }
        break;
      }
    }
    
    public void onTouchModeChanged(boolean paramBoolean)
    {
      if (!paramBoolean) {
        hide();
      }
    }
    
    public void resetTouchOffsets()
    {
      mMaxTouchOffset = -1;
      mMinTouchOffset = -1;
      resetDragAcceleratorState();
    }
    
    public void show()
    {
      if (mTextView.isInBatchEditMode()) {
        return;
      }
      initDrawables();
      initHandles();
    }
  }
  
  private class SpanController
    implements SpanWatcher
  {
    private static final int DISPLAY_TIMEOUT_MS = 3000;
    private Runnable mHidePopup;
    private Editor.EasyEditPopupWindow mPopupWindow;
    
    private SpanController() {}
    
    private boolean isNonIntermediateSelectionSpan(Spannable paramSpannable, Object paramObject)
    {
      boolean bool;
      if (((Selection.SELECTION_START == paramObject) || (Selection.SELECTION_END == paramObject)) && ((paramSpannable.getSpanFlags(paramObject) & 0x200) == 0)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void sendEasySpanNotification(int paramInt, EasyEditSpan paramEasyEditSpan)
    {
      try
      {
        PendingIntent localPendingIntent = paramEasyEditSpan.getPendingIntent();
        if (localPendingIntent != null)
        {
          paramEasyEditSpan = new android/content/Intent;
          paramEasyEditSpan.<init>();
          paramEasyEditSpan.putExtra("android.text.style.EXTRA_TEXT_CHANGED_TYPE", paramInt);
          localPendingIntent.send(mTextView.getContext(), 0, paramEasyEditSpan);
        }
      }
      catch (PendingIntent.CanceledException paramEasyEditSpan)
      {
        Log.w("Editor", "PendingIntent for notification cannot be sent", paramEasyEditSpan);
      }
    }
    
    public void hide()
    {
      if (mPopupWindow != null)
      {
        mPopupWindow.hide();
        mTextView.removeCallbacks(mHidePopup);
      }
    }
    
    public void onSpanAdded(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2)
    {
      if (isNonIntermediateSelectionSpan(paramSpannable, paramObject))
      {
        Editor.this.sendUpdateSelection();
      }
      else if ((paramObject instanceof EasyEditSpan))
      {
        if (mPopupWindow == null)
        {
          mPopupWindow = new Editor.EasyEditPopupWindow(Editor.this, null);
          mHidePopup = new Runnable()
          {
            public void run()
            {
              hide();
            }
          };
        }
        if (mPopupWindow.mEasyEditSpan != null) {
          mPopupWindow.mEasyEditSpan.setDeleteEnabled(false);
        }
        mPopupWindow.setEasyEditSpan((EasyEditSpan)paramObject);
        mPopupWindow.setOnDeleteListener(new Editor.EasyEditDeleteListener()
        {
          public void onDeleteClick(EasyEditSpan paramAnonymousEasyEditSpan)
          {
            Editable localEditable = (Editable)mTextView.getText();
            int i = localEditable.getSpanStart(paramAnonymousEasyEditSpan);
            int j = localEditable.getSpanEnd(paramAnonymousEasyEditSpan);
            if ((i >= 0) && (j >= 0))
            {
              Editor.SpanController.this.sendEasySpanNotification(1, paramAnonymousEasyEditSpan);
              mTextView.deleteText_internal(i, j);
            }
            localEditable.removeSpan(paramAnonymousEasyEditSpan);
          }
        });
        if (mTextView.getWindowVisibility() != 0) {
          return;
        }
        if (mTextView.getLayout() == null) {
          return;
        }
        if (Editor.this.extractedTextModeWillBeStarted()) {
          return;
        }
        mPopupWindow.show();
        mTextView.removeCallbacks(mHidePopup);
        mTextView.postDelayed(mHidePopup, 3000L);
      }
    }
    
    public void onSpanChanged(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if (isNonIntermediateSelectionSpan(paramSpannable, paramObject))
      {
        Editor.this.sendUpdateSelection();
      }
      else if ((mPopupWindow != null) && ((paramObject instanceof EasyEditSpan)))
      {
        paramObject = (EasyEditSpan)paramObject;
        sendEasySpanNotification(2, paramObject);
        paramSpannable.removeSpan(paramObject);
      }
    }
    
    public void onSpanRemoved(Spannable paramSpannable, Object paramObject, int paramInt1, int paramInt2)
    {
      if (isNonIntermediateSelectionSpan(paramSpannable, paramObject)) {
        Editor.this.sendUpdateSelection();
      } else if ((mPopupWindow != null) && (paramObject == mPopupWindow.mEasyEditSpan)) {
        hide();
      }
    }
  }
  
  private class SuggestionHelper
  {
    private final HashMap<SuggestionSpan, Integer> mSpansLengths = new HashMap();
    private final Comparator<SuggestionSpan> mSuggestionSpanComparator = new SuggestionSpanComparator(null);
    
    private SuggestionHelper() {}
    
    private SuggestionSpan[] getSortedSuggestionSpans()
    {
      int i = mTextView.getSelectionStart();
      Spannable localSpannable = (Spannable)mTextView.getText();
      SuggestionSpan[] arrayOfSuggestionSpan = (SuggestionSpan[])localSpannable.getSpans(i, i, SuggestionSpan.class);
      mSpansLengths.clear();
      int j = arrayOfSuggestionSpan.length;
      for (i = 0; i < j; i++)
      {
        SuggestionSpan localSuggestionSpan = arrayOfSuggestionSpan[i];
        int k = localSpannable.getSpanStart(localSuggestionSpan);
        int m = localSpannable.getSpanEnd(localSuggestionSpan);
        mSpansLengths.put(localSuggestionSpan, Integer.valueOf(m - k));
      }
      Arrays.sort(arrayOfSuggestionSpan, mSuggestionSpanComparator);
      mSpansLengths.clear();
      return arrayOfSuggestionSpan;
    }
    
    public int getSuggestionInfo(Editor.SuggestionInfo[] paramArrayOfSuggestionInfo, Editor.SuggestionSpanInfo paramSuggestionSpanInfo)
    {
      Spannable localSpannable = (Spannable)mTextView.getText();
      SuggestionSpan[] arrayOfSuggestionSpan = getSortedSuggestionSpans();
      int i = arrayOfSuggestionSpan.length;
      int j = 0;
      if (i == 0) {
        return 0;
      }
      int k = arrayOfSuggestionSpan.length;
      i = 0;
      for (int m = 0;; m++)
      {
        Object localObject1 = paramSuggestionSpanInfo;
        if (m >= k) {
          break;
        }
        SuggestionSpan localSuggestionSpan = arrayOfSuggestionSpan[m];
        int n = localSpannable.getSpanStart(localSuggestionSpan);
        int i1 = localSpannable.getSpanEnd(localSuggestionSpan);
        if ((localObject1 != null) && ((localSuggestionSpan.getFlags() & 0x2) != 0))
        {
          mSuggestionSpan = localSuggestionSpan;
          mSpanStart = n;
          mSpanEnd = i1;
        }
        localObject1 = localSuggestionSpan.getSuggestions();
        int i2 = localObject1.length;
        int i3 = j;
        while (i3 < i2)
        {
          Object localObject2 = localObject1[i3];
          for (j = 0; j < i; j++)
          {
            localSuggestionInfo = paramArrayOfSuggestionInfo[j];
            if (mText.toString().equals(localObject2))
            {
              int i4 = mSuggestionSpanInfo.mSpanStart;
              int i5 = mSuggestionSpanInfo.mSpanEnd;
              if ((n == i4) && (i1 == i5)) {
                break label304;
              }
            }
          }
          Editor.SuggestionInfo localSuggestionInfo = paramArrayOfSuggestionInfo[i];
          localSuggestionInfo.setSpanInfo(localSuggestionSpan, n, i1);
          mSuggestionIndex = i3;
          mSuggestionStart = 0;
          mSuggestionEnd = localObject2.length();
          mText.replace(0, mText.length(), localObject2);
          j = i + 1;
          i = j;
          if (j >= paramArrayOfSuggestionInfo.length) {
            return j;
          }
          label304:
          i3++;
          j = 0;
        }
      }
      return i;
    }
    
    private class SuggestionSpanComparator
      implements Comparator<SuggestionSpan>
    {
      private SuggestionSpanComparator() {}
      
      public int compare(SuggestionSpan paramSuggestionSpan1, SuggestionSpan paramSuggestionSpan2)
      {
        int i = paramSuggestionSpan1.getFlags();
        int j = paramSuggestionSpan2.getFlags();
        if (i != j)
        {
          int k = 0;
          int m;
          if ((i & 0x1) != 0) {
            m = 1;
          } else {
            m = 0;
          }
          int n;
          if ((j & 0x1) != 0) {
            n = 1;
          } else {
            n = 0;
          }
          if ((i & 0x2) != 0) {
            i = 1;
          } else {
            i = 0;
          }
          if ((j & 0x2) != 0) {
            k = 1;
          }
          if ((m != 0) && (i == 0)) {
            return -1;
          }
          if ((n != 0) && (k == 0)) {
            return 1;
          }
          if (i != 0) {
            return -1;
          }
          if (k != 0) {
            return 1;
          }
        }
        return ((Integer)mSpansLengths.get(paramSuggestionSpan1)).intValue() - ((Integer)mSpansLengths.get(paramSuggestionSpan2)).intValue();
      }
    }
  }
  
  private static final class SuggestionInfo
  {
    int mSuggestionEnd;
    int mSuggestionIndex;
    final Editor.SuggestionSpanInfo mSuggestionSpanInfo = new Editor.SuggestionSpanInfo(null);
    int mSuggestionStart;
    final SpannableStringBuilder mText = new SpannableStringBuilder();
    
    private SuggestionInfo() {}
    
    void clear()
    {
      mSuggestionSpanInfo.clear();
      mText.clear();
    }
    
    void setSpanInfo(SuggestionSpan paramSuggestionSpan, int paramInt1, int paramInt2)
    {
      mSuggestionSpanInfo.mSuggestionSpan = paramSuggestionSpan;
      mSuggestionSpanInfo.mSpanStart = paramInt1;
      mSuggestionSpanInfo.mSpanEnd = paramInt2;
    }
  }
  
  private static final class SuggestionSpanInfo
  {
    int mSpanEnd;
    int mSpanStart;
    SuggestionSpan mSuggestionSpan;
    
    private SuggestionSpanInfo() {}
    
    void clear()
    {
      mSuggestionSpan = null;
    }
  }
  
  @VisibleForTesting
  public class SuggestionsPopupWindow
    extends Editor.PinnedPopupWindow
    implements AdapterView.OnItemClickListener
  {
    private static final int MAX_NUMBER_SUGGESTIONS = 5;
    private static final String USER_DICTIONARY_EXTRA_LOCALE = "locale";
    private static final String USER_DICTIONARY_EXTRA_WORD = "word";
    private TextView mAddToDictionaryButton;
    private int mContainerMarginTop;
    private int mContainerMarginWidth;
    private LinearLayout mContainerView;
    private Context mContext;
    private boolean mCursorWasVisibleBeforeSuggestions = mCursorVisible;
    private TextView mDeleteButton;
    private TextAppearanceSpan mHighlightSpan;
    private boolean mIsShowingUp = false;
    private final Editor.SuggestionSpanInfo mMisspelledSpanInfo = new Editor.SuggestionSpanInfo(null);
    private int mNumberOfSuggestions;
    private Editor.SuggestionInfo[] mSuggestionInfos;
    private ListView mSuggestionListView;
    private SuggestionAdapter mSuggestionsAdapter;
    
    public SuggestionsPopupWindow()
    {
      super();
    }
    
    private Context applyDefaultTheme(Context paramContext)
    {
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(new int[] { 17891418 });
      int i;
      if (localTypedArray.getBoolean(0, true)) {
        i = 16974410;
      } else {
        i = 16974411;
      }
      localTypedArray.recycle();
      return new ContextThemeWrapper(paramContext, i);
    }
    
    private void hideWithCleanUp()
    {
      Editor.SuggestionInfo[] arrayOfSuggestionInfo = mSuggestionInfos;
      int i = arrayOfSuggestionInfo.length;
      for (int j = 0; j < i; j++) {
        arrayOfSuggestionInfo[j].clear();
      }
      mMisspelledSpanInfo.clear();
      hide();
    }
    
    private void highlightTextDifferences(Editor.SuggestionInfo paramSuggestionInfo, int paramInt1, int paramInt2)
    {
      Object localObject = (Spannable)mTextView.getText();
      int i = mSuggestionSpanInfo.mSpanStart;
      int j = mSuggestionSpanInfo.mSpanEnd;
      mSuggestionStart = (i - paramInt1);
      mSuggestionEnd = (mSuggestionStart + mText.length());
      mText.setSpan(mHighlightSpan, 0, mText.length(), 33);
      localObject = localObject.toString();
      mText.insert(0, ((String)localObject).substring(paramInt1, i));
      mText.append(((String)localObject).substring(j, paramInt2));
    }
    
    private boolean updateSuggestions()
    {
      Spannable localSpannable = (Spannable)mTextView.getText();
      mNumberOfSuggestions = mSuggestionHelper.getSuggestionInfo(mSuggestionInfos, mMisspelledSpanInfo);
      if ((mNumberOfSuggestions == 0) && (mMisspelledSpanInfo.mSuggestionSpan == null)) {
        return false;
      }
      int i = mTextView.getText().length();
      int j = 0;
      for (int k = 0; k < mNumberOfSuggestions; k++)
      {
        Editor.SuggestionSpanInfo localSuggestionSpanInfo = mSuggestionInfos[k].mSuggestionSpanInfo;
        i = Math.min(i, mSpanStart);
        j = Math.max(j, mSpanEnd);
      }
      int m = i;
      k = j;
      if (mMisspelledSpanInfo.mSuggestionSpan != null)
      {
        m = Math.min(i, mMisspelledSpanInfo.mSpanStart);
        k = Math.max(j, mMisspelledSpanInfo.mSpanEnd);
      }
      for (j = 0; j < mNumberOfSuggestions; j++) {
        highlightTextDifferences(mSuggestionInfos[j], m, k);
      }
      i = 8;
      j = i;
      if (mMisspelledSpanInfo.mSuggestionSpan != null)
      {
        j = i;
        if (mMisspelledSpanInfo.mSpanStart >= 0)
        {
          j = i;
          if (mMisspelledSpanInfo.mSpanEnd > mMisspelledSpanInfo.mSpanStart) {
            j = 0;
          }
        }
      }
      mAddToDictionaryButton.setVisibility(j);
      if (mSuggestionRangeSpan == null) {
        mSuggestionRangeSpan = new SuggestionRangeSpan();
      }
      if (mNumberOfSuggestions != 0) {
        j = mSuggestionInfos[0].mSuggestionSpanInfo.mSuggestionSpan.getUnderlineColor();
      } else {
        j = mMisspelledSpanInfo.mSuggestionSpan.getUnderlineColor();
      }
      if (j == 0)
      {
        mSuggestionRangeSpan.setBackgroundColor(mTextView.mHighlightColor);
      }
      else
      {
        i = (int)(Color.alpha(j) * 0.4F);
        mSuggestionRangeSpan.setBackgroundColor((0xFFFFFF & j) + (i << 24));
      }
      localSpannable.setSpan(mSuggestionRangeSpan, m, k, 33);
      mSuggestionsAdapter.notifyDataSetChanged();
      return true;
    }
    
    protected int clipVertically(int paramInt)
    {
      int i = mContentView.getMeasuredHeight();
      return Math.min(paramInt, mTextView.getResources().getDisplayMetrics().heightPixels - i);
    }
    
    protected void createPopupWindow()
    {
      mPopupWindow = new CustomPopupWindow(null);
      mPopupWindow.setInputMethodMode(2);
      mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
      mPopupWindow.setFocusable(true);
      mPopupWindow.setClippingEnabled(false);
    }
    
    @VisibleForTesting
    public ViewGroup getContentViewForTesting()
    {
      return mContentView;
    }
    
    protected int getTextOffset()
    {
      return (mTextView.getSelectionStart() + mTextView.getSelectionStart()) / 2;
    }
    
    protected int getVerticalLocalPosition(int paramInt)
    {
      return mTextView.getLayout().getLineBottomWithoutSpacing(paramInt) - mContainerMarginTop;
    }
    
    protected void initContentView()
    {
      mContentView = ((ViewGroup)((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(mTextView.mTextEditSuggestionContainerLayout, null));
      mContainerView = ((LinearLayout)mContentView.findViewById(16909422));
      ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)mContainerView.getLayoutParams();
      mContainerMarginWidth = (leftMargin + rightMargin);
      mContainerMarginTop = topMargin;
      mClippingLimitLeft = leftMargin;
      mClippingLimitRight = rightMargin;
      mSuggestionListView = ((ListView)mContentView.findViewById(16909421));
      mSuggestionsAdapter = new SuggestionAdapter(null);
      mSuggestionListView.setAdapter(mSuggestionsAdapter);
      mSuggestionListView.setOnItemClickListener(this);
      mSuggestionInfos = new Editor.SuggestionInfo[5];
      for (int i = 0; i < mSuggestionInfos.length; i++) {
        mSuggestionInfos[i] = new Editor.SuggestionInfo(null);
      }
      mAddToDictionaryButton = ((TextView)mContentView.findViewById(16908713));
      mAddToDictionaryButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Object localObject = Editor.this.findEquivalentSuggestionSpan(mMisspelledSpanInfo);
          if (localObject == null) {
            return;
          }
          paramAnonymousView = (Editable)mTextView.getText();
          int i = paramAnonymousView.getSpanStart(localObject);
          int j = paramAnonymousView.getSpanEnd(localObject);
          if ((i >= 0) && (j > i))
          {
            String str = TextUtils.substring(paramAnonymousView, i, j);
            localObject = new Intent("com.android.settings.USER_DICTIONARY_INSERT");
            ((Intent)localObject).putExtra("word", str);
            ((Intent)localObject).putExtra("locale", mTextView.getTextServicesLocale().toString());
            ((Intent)localObject).setFlags(((Intent)localObject).getFlags() | 0x10000000);
            mTextView.getContext().startActivity((Intent)localObject);
            paramAnonymousView.removeSpan(mMisspelledSpanInfo.mSuggestionSpan);
            Selection.setSelection(paramAnonymousView, j);
            Editor.this.updateSpellCheckSpans(i, j, false);
            Editor.SuggestionsPopupWindow.this.hideWithCleanUp();
            return;
          }
        }
      });
      mDeleteButton = ((TextView)mContentView.findViewById(16908901));
      mDeleteButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView = (Editable)mTextView.getText();
          int i = paramAnonymousView.getSpanStart(mSuggestionRangeSpan);
          int j = paramAnonymousView.getSpanEnd(mSuggestionRangeSpan);
          if ((i >= 0) && (j > i))
          {
            int k = j;
            if (j < paramAnonymousView.length())
            {
              k = j;
              if (Character.isSpaceChar(paramAnonymousView.charAt(j))) {
                if (i != 0)
                {
                  k = j;
                  if (!Character.isSpaceChar(paramAnonymousView.charAt(i - 1))) {}
                }
                else
                {
                  k = j + 1;
                }
              }
            }
            mTextView.deleteText_internal(i, k);
          }
          Editor.SuggestionsPopupWindow.this.hideWithCleanUp();
        }
      });
    }
    
    public boolean isShowingUp()
    {
      return mIsShowingUp;
    }
    
    protected void measureContent()
    {
      Object localObject = mTextView.getResources().getDisplayMetrics();
      int i = View.MeasureSpec.makeMeasureSpec(widthPixels, Integer.MIN_VALUE);
      int j = View.MeasureSpec.makeMeasureSpec(heightPixels, Integer.MIN_VALUE);
      int k = 0;
      localObject = null;
      for (int m = 0; m < mNumberOfSuggestions; m++)
      {
        localObject = mSuggestionsAdapter.getView(m, (View)localObject, mContentView);
        getLayoutParamswidth = -2;
        ((View)localObject).measure(i, j);
        k = Math.max(k, ((View)localObject).getMeasuredWidth());
      }
      m = k;
      if (mAddToDictionaryButton.getVisibility() != 8)
      {
        mAddToDictionaryButton.measure(i, j);
        m = Math.max(k, mAddToDictionaryButton.getMeasuredWidth());
      }
      mDeleteButton.measure(i, j);
      m = Math.max(m, mDeleteButton.getMeasuredWidth()) + (mContainerView.getPaddingLeft() + mContainerView.getPaddingRight() + mContainerMarginWidth);
      mContentView.measure(View.MeasureSpec.makeMeasureSpec(m, 1073741824), j);
      localObject = mPopupWindow.getBackground();
      k = m;
      if (localObject != null)
      {
        if (mTempRect == null) {
          Editor.access$3902(Editor.this, new Rect());
        }
        ((Drawable)localObject).getPadding(mTempRect);
        k = m + (mTempRect.left + mTempRect.right);
      }
      mPopupWindow.setWidth(k);
    }
    
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      paramAdapterView = mSuggestionInfos[paramInt];
      Editor.this.replaceWithSuggestion(paramAdapterView);
      hideWithCleanUp();
    }
    
    public void onParentLostFocus()
    {
      mIsShowingUp = false;
    }
    
    protected void setUp()
    {
      mContext = applyDefaultTheme(mTextView.getContext());
      mHighlightSpan = new TextAppearanceSpan(mContext, mTextView.mTextEditSuggestionHighlightStyle);
    }
    
    public void show()
    {
      if (!(mTextView.getText() instanceof Editable)) {
        return;
      }
      if (Editor.this.extractedTextModeWillBeStarted()) {
        return;
      }
      boolean bool = updateSuggestions();
      int i = 0;
      if (bool)
      {
        mCursorWasVisibleBeforeSuggestions = mCursorVisible;
        mTextView.setCursorVisible(false);
        mIsShowingUp = true;
        super.show();
      }
      ListView localListView = mSuggestionListView;
      if (mNumberOfSuggestions == 0) {
        i = 8;
      }
      localListView.setVisibility(i);
    }
    
    private class CustomPopupWindow
      extends PopupWindow
    {
      private CustomPopupWindow() {}
      
      public void dismiss()
      {
        if (!isShowing()) {
          return;
        }
        super.dismiss();
        Editor.this.getPositionListener().removeSubscriber(Editor.SuggestionsPopupWindow.this);
        ((Spannable)mTextView.getText()).removeSpan(mSuggestionRangeSpan);
        mTextView.setCursorVisible(mCursorWasVisibleBeforeSuggestions);
        if ((hasInsertionController()) && (!Editor.this.extractedTextModeWillBeStarted())) {
          Editor.this.getInsertionController().show();
        }
      }
    }
    
    private class SuggestionAdapter
      extends BaseAdapter
    {
      private LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService("layout_inflater");
      
      private SuggestionAdapter() {}
      
      public int getCount()
      {
        return mNumberOfSuggestions;
      }
      
      public Object getItem(int paramInt)
      {
        return mSuggestionInfos[paramInt];
      }
      
      public long getItemId(int paramInt)
      {
        return paramInt;
      }
      
      public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
      {
        TextView localTextView = (TextView)paramView;
        paramView = localTextView;
        if (localTextView == null) {
          paramView = (TextView)mInflater.inflate(mTextView.mTextEditSuggestionItemLayout, paramViewGroup, false);
        }
        paramView.setText(mSuggestionInfos[paramInt].mText);
        return paramView;
      }
    }
  }
  
  static @interface TextActionMode
  {
    public static final int INSERTION = 1;
    public static final int SELECTION = 0;
    public static final int TEXT_LINK = 2;
  }
  
  private class TextActionModeCallback
    extends ActionMode.Callback2
  {
    private final Map<MenuItem, View.OnClickListener> mAssistClickHandlers = new HashMap();
    private final int mHandleHeight;
    private final boolean mHasSelection;
    private final RectF mSelectionBounds = new RectF();
    private final Path mSelectionPath = new Path();
    
    TextActionModeCallback(int paramInt)
    {
      boolean bool;
      if ((paramInt != 0) && ((!mTextIsSelectable) || (paramInt != 2))) {
        bool = false;
      } else {
        bool = true;
      }
      mHasSelection = bool;
      Object localObject;
      if (mHasSelection)
      {
        localObject = getSelectionController();
        if (mStartHandle == null)
        {
          ((Editor.SelectionModifierCursorController)localObject).initDrawables();
          ((Editor.SelectionModifierCursorController)localObject).initHandles();
          ((Editor.SelectionModifierCursorController)localObject).hide();
        }
        mHandleHeight = Math.max(mSelectHandleLeft.getMinimumHeight(), mSelectHandleRight.getMinimumHeight());
      }
      else
      {
        localObject = Editor.this.getInsertionController();
        if (localObject != null)
        {
          ((Editor.InsertionPointCursorController)localObject).getHandle();
          mHandleHeight = mSelectHandleCenter.getMinimumHeight();
        }
        else
        {
          mHandleHeight = 0;
        }
      }
    }
    
    private MenuItem addAssistMenuItem(Menu paramMenu, RemoteAction paramRemoteAction, int paramInt1, int paramInt2, int paramInt3)
    {
      paramMenu = paramMenu.add(16908353, paramInt1, paramInt2, paramRemoteAction.getTitle()).setContentDescription(paramRemoteAction.getContentDescription());
      if (paramRemoteAction.shouldShowIcon()) {
        paramMenu.setIcon(paramRemoteAction.getIcon().loadDrawable(mTextView.getContext()));
      }
      paramMenu.setShowAsAction(paramInt3);
      mAssistClickHandlers.put(paramMenu, TextClassification.createIntentOnClickListener(paramRemoteAction.getActionIntent()));
      return paramMenu;
    }
    
    private void clearAssistMenuItems(Menu paramMenu)
    {
      int i = 0;
      while (i < paramMenu.size())
      {
        MenuItem localMenuItem = paramMenu.getItem(i);
        if (localMenuItem.getGroupId() == 16908353) {
          paramMenu.removeItem(localMenuItem.getItemId());
        } else {
          i++;
        }
      }
    }
    
    private int createAssistMenuItemPendingIntentRequestCode()
    {
      int i;
      if (mTextView.hasSelection()) {
        i = mTextView.getText().subSequence(mTextView.getSelectionStart(), mTextView.getSelectionEnd()).hashCode();
      } else {
        i = 0;
      }
      return i;
    }
    
    private ActionMode.Callback getCustomCallback()
    {
      ActionMode.Callback localCallback;
      if (mHasSelection) {
        localCallback = mCustomSelectionActionModeCallback;
      } else {
        localCallback = mCustomInsertionActionModeCallback;
      }
      return localCallback;
    }
    
    private boolean hasLegacyAssistItem(TextClassification paramTextClassification)
    {
      boolean bool;
      if (((paramTextClassification.getIcon() == null) && (TextUtils.isEmpty(paramTextClassification.getLabel()))) || ((paramTextClassification.getIntent() == null) && (paramTextClassification.getOnClickListener() == null))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    private boolean onAssistMenuItemClicked(MenuItem paramMenuItem)
    {
      boolean bool;
      if (paramMenuItem.getGroupId() == 16908353) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool);
      Object localObject = Editor.this.getSelectionActionModeHelper().getTextClassification();
      if ((shouldEnableAssistMenuItems()) && (localObject != null))
      {
        View.OnClickListener localOnClickListener = (View.OnClickListener)mAssistClickHandlers.get(paramMenuItem);
        localObject = localOnClickListener;
        if (localOnClickListener == null)
        {
          paramMenuItem = paramMenuItem.getIntent();
          localObject = localOnClickListener;
          if (paramMenuItem != null) {
            localObject = TextClassification.createIntentOnClickListener(TextClassification.createPendingIntent(mTextView.getContext(), paramMenuItem, createAssistMenuItemPendingIntentRequestCode()));
          }
        }
        if (localObject != null)
        {
          ((View.OnClickListener)localObject).onClick(mTextView);
          stopTextActionMode();
        }
        return true;
      }
      return true;
    }
    
    private void populateMenuWithItems(Menu paramMenu)
    {
      if (mTextView.canCut()) {
        paramMenu.add(0, 16908320, 4, 17039363).setAlphabeticShortcut('x').setShowAsAction(2);
      }
      if (mTextView.canCopy()) {
        paramMenu.add(0, 16908321, 5, 17039361).setAlphabeticShortcut('c').setShowAsAction(2);
      }
      if (mTextView.canPaste()) {
        paramMenu.add(0, 16908322, 6, 17039371).setAlphabeticShortcut('v').setShowAsAction(2);
      }
      if (mTextView.canShare()) {
        paramMenu.add(0, 16908341, 7, 17040987).setShowAsAction(1);
      }
      if (mTextView.canRequestAutofill())
      {
        String str = mTextView.getSelectedText();
        if ((str == null) || (str.isEmpty())) {
          paramMenu.add(0, 16908355, 10, 17039386).setShowAsAction(0);
        }
      }
      if (mTextView.canPasteAsPlainText()) {
        paramMenu.add(0, 16908337, 11, 17039385).setShowAsAction(1);
      }
      updateSelectAllItem(paramMenu);
      updateReplaceItem(paramMenu);
      updateAssistMenuItems(paramMenu);
    }
    
    private boolean shouldEnableAssistMenuItems()
    {
      boolean bool;
      if ((mTextView.isDeviceProvisioned()) && (TextClassificationManager.getSettings(mTextView.getContext()).isSmartTextShareEnabled())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void updateAssistMenuItems(Menu paramMenu)
    {
      clearAssistMenuItems(paramMenu);
      if (!shouldEnableAssistMenuItems()) {
        return;
      }
      TextClassification localTextClassification = Editor.this.getSelectionActionModeHelper().getTextClassification();
      if (localTextClassification == null) {
        return;
      }
      if (!localTextClassification.getActions().isEmpty())
      {
        addAssistMenuItem(paramMenu, (RemoteAction)localTextClassification.getActions().get(0), 16908353, 0, 2).setIntent(localTextClassification.getIntent());
      }
      else if (hasLegacyAssistItem(localTextClassification))
      {
        MenuItem localMenuItem = paramMenu.add(16908353, 16908353, 0, localTextClassification.getLabel()).setIcon(localTextClassification.getIcon()).setIntent(localTextClassification.getIntent());
        localMenuItem.setShowAsAction(2);
        mAssistClickHandlers.put(localMenuItem, TextClassification.createIntentOnClickListener(TextClassification.createPendingIntent(mTextView.getContext(), localTextClassification.getIntent(), createAssistMenuItemPendingIntentRequestCode())));
      }
      int i = localTextClassification.getActions().size();
      for (int j = 1; j < i; j++) {
        addAssistMenuItem(paramMenu, (RemoteAction)localTextClassification.getActions().get(j), 0, 50 + j - 1, 0);
      }
    }
    
    private void updateReplaceItem(Menu paramMenu)
    {
      int i;
      if ((mTextView.isSuggestionsEnabled()) && (Editor.this.shouldOfferToShowSuggestions())) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if (paramMenu.findItem(16908340) != null) {
        j = 1;
      } else {
        j = 0;
      }
      if ((i != 0) && (j == 0)) {
        paramMenu.add(0, 16908340, 9, 17040902).setShowAsAction(1);
      } else if ((i == 0) && (j != 0)) {
        paramMenu.removeItem(16908340);
      }
    }
    
    private void updateSelectAllItem(Menu paramMenu)
    {
      boolean bool = mTextView.canSelectAllText();
      int i;
      if (paramMenu.findItem(16908319) != null) {
        i = 1;
      } else {
        i = 0;
      }
      if ((bool) && (i == 0)) {
        paramMenu.add(0, 16908319, 8, 17039373).setShowAsAction(1);
      } else if ((!bool) && (i != 0)) {
        paramMenu.removeItem(16908319);
      }
    }
    
    public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
    {
      Editor.this.getSelectionActionModeHelper().onSelectionAction(paramMenuItem.getItemId());
      if (mProcessTextIntentActionsHandler.performMenuItemAction(paramMenuItem)) {
        return true;
      }
      ActionMode.Callback localCallback = getCustomCallback();
      if ((localCallback != null) && (localCallback.onActionItemClicked(paramActionMode, paramMenuItem))) {
        return true;
      }
      if ((paramMenuItem.getGroupId() == 16908353) && (onAssistMenuItemClicked(paramMenuItem))) {
        return true;
      }
      return mTextView.onTextContextMenuItem(paramMenuItem.getItemId());
    }
    
    public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      mAssistClickHandlers.clear();
      paramActionMode.setTitle(null);
      paramActionMode.setSubtitle(null);
      paramActionMode.setTitleOptionalHint(true);
      populateMenuWithItems(paramMenu);
      ActionMode.Callback localCallback = getCustomCallback();
      if ((localCallback != null) && (!localCallback.onCreateActionMode(paramActionMode, paramMenu)))
      {
        Selection.setSelection((Spannable)mTextView.getText(), mTextView.getSelectionEnd());
        return false;
      }
      if (mTextView.canProcessText()) {
        mProcessTextIntentActionsHandler.onInitializeMenu(paramMenu);
      }
      if ((mHasSelection) && (!mTextView.hasTransientState())) {
        mTextView.setHasTransientState(true);
      }
      return true;
    }
    
    public void onDestroyActionMode(ActionMode paramActionMode)
    {
      Editor.this.getSelectionActionModeHelper().onDestroyActionMode();
      Editor.access$502(Editor.this, null);
      ActionMode.Callback localCallback = getCustomCallback();
      if (localCallback != null) {
        localCallback.onDestroyActionMode(paramActionMode);
      }
      if (!mPreserveSelection) {
        Selection.setSelection((Spannable)mTextView.getText(), mTextView.getSelectionEnd());
      }
      if (mSelectionModifierCursorController != null) {
        mSelectionModifierCursorController.hide();
      }
      mAssistClickHandlers.clear();
      Editor.access$5102(Editor.this, false);
    }
    
    public void onGetContentRect(ActionMode paramActionMode, View paramView, Rect paramRect)
    {
      if ((paramView.equals(mTextView)) && (mTextView.getLayout() != null))
      {
        if (mTextView.getSelectionStart() != mTextView.getSelectionEnd())
        {
          mSelectionPath.reset();
          mTextView.getLayout().getSelectionPath(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), mSelectionPath);
          mSelectionPath.computeBounds(mSelectionBounds, true);
          paramActionMode = mSelectionBounds;
          bottom += mHandleHeight;
        }
        else
        {
          paramActionMode = mTextView.getLayout();
          i = paramActionMode.getLineForOffset(mTextView.getSelectionStart());
          float f = Editor.this.clampHorizontalPosition(null, paramActionMode.getPrimaryHorizontal(mTextView.getSelectionStart()));
          mSelectionBounds.set(f, paramActionMode.getLineTop(i), f, paramActionMode.getLineBottom(i) + mHandleHeight);
        }
        int i = mTextView.viewportToContentHorizontalOffset();
        int j = mTextView.viewportToContentVerticalOffset();
        paramRect.set((int)Math.floor(mSelectionBounds.left + i), (int)Math.floor(mSelectionBounds.top + j), (int)Math.ceil(mSelectionBounds.right + i), (int)Math.ceil(mSelectionBounds.bottom + j));
        return;
      }
      super.onGetContentRect(paramActionMode, paramView, paramRect);
    }
    
    public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      updateSelectAllItem(paramMenu);
      updateReplaceItem(paramMenu);
      updateAssistMenuItems(paramMenu);
      ActionMode.Callback localCallback = getCustomCallback();
      if (localCallback != null) {
        return localCallback.onPrepareActionMode(paramActionMode, paramMenu);
      }
      return true;
    }
  }
  
  private static class TextRenderNode
  {
    boolean isDirty;
    boolean needsToBeShifted;
    RenderNode renderNode;
    
    public TextRenderNode(String paramString)
    {
      renderNode = RenderNode.create(paramString, null);
      isDirty = true;
      needsToBeShifted = true;
    }
    
    boolean needsRecord()
    {
      boolean bool;
      if ((!isDirty) && (renderNode.isValid())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
  }
  
  private static abstract interface TextViewPositionListener
  {
    public abstract void updatePosition(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2);
  }
  
  public static class UndoInputFilter
    implements InputFilter
  {
    private static final int MERGE_EDIT_MODE_FORCE_MERGE = 0;
    private static final int MERGE_EDIT_MODE_NEVER_MERGE = 1;
    private static final int MERGE_EDIT_MODE_NORMAL = 2;
    private final Editor mEditor;
    private boolean mExpanding;
    private boolean mHasComposition;
    private boolean mIsUserEdit;
    private boolean mPreviousOperationWasInSameBatchEdit;
    
    public UndoInputFilter(Editor paramEditor)
    {
      mEditor = paramEditor;
    }
    
    private boolean canUndoEdit(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      if (!mEditor.mAllowUndo) {
        return false;
      }
      if (mEditor.mUndoManager.isInUndo()) {
        return false;
      }
      if ((Editor.isValidRange(paramCharSequence, paramInt1, paramInt2)) && (Editor.isValidRange(paramSpanned, paramInt3, paramInt4))) {
        return (paramInt1 != paramInt2) || (paramInt3 != paramInt4);
      }
      return false;
    }
    
    private Editor.EditOperation getLastEdit()
    {
      return (Editor.EditOperation)mEditor.mUndoManager.getLastOperation(Editor.EditOperation.class, mEditor.mUndoOwner, 1);
    }
    
    private void handleEdit(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4, boolean paramBoolean)
    {
      int i;
      if ((!isInTextWatcher()) && (!mPreviousOperationWasInSameBatchEdit))
      {
        if (paramBoolean) {
          i = 1;
        } else {
          i = 2;
        }
      }
      else {
        i = 0;
      }
      paramCharSequence = TextUtils.substring(paramCharSequence, paramInt1, paramInt2);
      paramSpanned = TextUtils.substring(paramSpanned, paramInt3, paramInt4);
      paramCharSequence = new Editor.EditOperation(mEditor, paramSpanned, paramInt3, paramCharSequence, mHasComposition);
      if ((mHasComposition) && (TextUtils.equals(mNewText, mOldText))) {
        return;
      }
      recordEdit(paramCharSequence, i);
    }
    
    private static boolean isComposition(CharSequence paramCharSequence)
    {
      boolean bool1 = paramCharSequence instanceof Spannable;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramCharSequence = (Spannable)paramCharSequence;
      if (EditableInputConnection.getComposingSpanStart(paramCharSequence) < EditableInputConnection.getComposingSpanEnd(paramCharSequence)) {
        bool2 = true;
      }
      return bool2;
    }
    
    private boolean isInTextWatcher()
    {
      CharSequence localCharSequence = mEditor.mTextView.getText();
      boolean bool;
      if (((localCharSequence instanceof SpannableStringBuilder)) && (((SpannableStringBuilder)localCharSequence).getTextWatcherDepth() > 0)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void recordEdit(Editor.EditOperation paramEditOperation, int paramInt)
    {
      UndoManager localUndoManager = mEditor.mUndoManager;
      localUndoManager.beginUpdate("Edit text");
      Editor.EditOperation localEditOperation = getLastEdit();
      if (localEditOperation == null)
      {
        localUndoManager.addOperation(paramEditOperation, 0);
      }
      else if (paramInt == 0)
      {
        localEditOperation.forceMergeWith(paramEditOperation);
      }
      else if (!mIsUserEdit)
      {
        localUndoManager.commitState(mEditor.mUndoOwner);
        localUndoManager.addOperation(paramEditOperation, 0);
      }
      else if ((paramInt != 2) || (!localEditOperation.mergeWith(paramEditOperation)))
      {
        localUndoManager.commitState(mEditor.mUndoOwner);
        localUndoManager.addOperation(paramEditOperation, 0);
      }
      mPreviousOperationWasInSameBatchEdit = mIsUserEdit;
      localUndoManager.endUpdate();
    }
    
    public void beginBatchEdit()
    {
      mIsUserEdit = true;
    }
    
    public void endBatchEdit()
    {
      mIsUserEdit = false;
      mPreviousOperationWasInSameBatchEdit = false;
    }
    
    public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      if (!canUndoEdit(paramCharSequence, paramInt1, paramInt2, paramSpanned, paramInt3, paramInt4)) {
        return null;
      }
      boolean bool1 = mHasComposition;
      mHasComposition = isComposition(paramCharSequence);
      boolean bool2 = mExpanding;
      boolean bool3 = false;
      boolean bool4 = bool3;
      if (paramInt2 - paramInt1 != paramInt4 - paramInt3)
      {
        if (paramInt2 - paramInt1 > paramInt4 - paramInt3) {
          bool4 = true;
        } else {
          bool4 = false;
        }
        mExpanding = bool4;
        bool4 = bool3;
        if (bool1)
        {
          bool4 = bool3;
          if (mExpanding != bool2) {
            bool4 = true;
          }
        }
      }
      handleEdit(paramCharSequence, paramInt1, paramInt2, paramSpanned, paramInt3, paramInt4, bool4);
      return null;
    }
    
    void freezeLastEdit()
    {
      mEditor.mUndoManager.beginUpdate("Edit text");
      Editor.EditOperation localEditOperation = getLastEdit();
      if (localEditOperation != null) {
        Editor.EditOperation.access$8702(localEditOperation, true);
      }
      mEditor.mUndoManager.endUpdate();
    }
    
    public void restoreInstanceState(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mIsUserEdit = bool2;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mHasComposition = bool2;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mExpanding = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      }
      mPreviousOperationWasInSameBatchEdit = bool2;
    }
    
    public void saveInstanceState(Parcel paramParcel)
    {
      paramParcel.writeInt(mIsUserEdit);
      paramParcel.writeInt(mHasComposition);
      paramParcel.writeInt(mExpanding);
      paramParcel.writeInt(mPreviousOperationWasInSameBatchEdit);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private static @interface MergeMode {}
  }
}
