package android.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.os.LocaleList;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.SelectionSessionLogger;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassification.Request;
import android.view.textclassifier.TextClassification.Request.Builder;
import android.view.textclassifier.TextClassificationConstants;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextSelection;
import android.view.textclassifier.TextSelection.Request;
import android.view.textclassifier.TextSelection.Request.Builder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public final class SelectionActionModeHelper
{
  private static final String LOG_TAG = "SelectActionModeHelper";
  private final Editor mEditor;
  private final SelectionTracker mSelectionTracker;
  private final SmartSelectSprite mSmartSelectSprite;
  private TextClassification mTextClassification;
  private AsyncTask mTextClassificationAsyncTask;
  private final TextClassificationHelper mTextClassificationHelper;
  private final TextView mTextView;
  
  SelectionActionModeHelper(Editor paramEditor)
  {
    mEditor = ((Editor)Preconditions.checkNotNull(paramEditor));
    mTextView = mEditor.getTextView();
    Context localContext = mTextView.getContext();
    TextView localTextView = mTextView;
    Objects.requireNonNull(localTextView);
    mTextClassificationHelper = new TextClassificationHelper(localContext, new _..Lambda.yIdmBO6ZxaY03PGN08RySVVQXuE(localTextView), getText(mTextView), 0, 1, mTextView.getTextLocales());
    mSelectionTracker = new SelectionTracker(mTextView);
    if (getTextClassificationSettings().isSmartSelectionAnimationEnabled())
    {
      localContext = mTextView.getContext();
      int i = getTextViewmHighlightColor;
      paramEditor = mTextView;
      Objects.requireNonNull(paramEditor);
      mSmartSelectSprite = new SmartSelectSprite(localContext, i, new _..Lambda.IfzAW5fP9thoftErKAjo9SLZufw(paramEditor));
    }
    else
    {
      mSmartSelectSprite = null;
    }
  }
  
  private void cancelAsyncTask()
  {
    if (mTextClassificationAsyncTask != null)
    {
      mTextClassificationAsyncTask.cancel(true);
      mTextClassificationAsyncTask = null;
    }
    mTextClassification = null;
  }
  
  private void cancelSmartSelectAnimation()
  {
    if (mSmartSelectSprite != null) {
      mSmartSelectSprite.cancelAnimation();
    }
  }
  
  private List<SmartSelectSprite.RectangleWithTextSelectionLayout> convertSelectionToRectangles(Layout paramLayout, int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = new ArrayList();
    paramLayout.getSelection(paramInt1, paramInt2, new _..Lambda.SelectionActionModeHelper.cMbIRcH_yFkksR3CQmROa0_hmgM(localArrayList));
    localArrayList.sort(Comparator.comparing(_..Lambda.ChL7kntlZCrPaPVdRfaSzGdk1JU.INSTANCE, SmartSelectSprite.RECTANGLE_COMPARATOR));
    return localArrayList;
  }
  
  private static int getActionType(int paramInt)
  {
    if (paramInt != 16908337)
    {
      if (paramInt != 16908341) {
        if (paramInt == 16908353) {}
      }
      switch (paramInt)
      {
      default: 
        return 108;
      case 16908321: 
        return 101;
      case 16908320: 
        return 103;
      case 16908319: 
        return 200;
        return 105;
        return 104;
      }
    }
    return 102;
  }
  
  private static CharSequence getText(TextView paramTextView)
  {
    paramTextView = paramTextView.getText();
    if (paramTextView != null) {
      return paramTextView;
    }
    return "";
  }
  
  private TextClassificationConstants getTextClassificationSettings()
  {
    return TextClassificationManager.getSettings(mTextView.getContext());
  }
  
  private void invalidateActionMode(SelectionResult paramSelectionResult)
  {
    cancelSmartSelectAnimation();
    if (paramSelectionResult != null) {
      paramSelectionResult = mClassification;
    } else {
      paramSelectionResult = null;
    }
    mTextClassification = paramSelectionResult;
    paramSelectionResult = mEditor.getTextActionMode();
    if (paramSelectionResult != null) {
      paramSelectionResult.invalidate();
    }
    mSelectionTracker.onSelectionUpdated(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), mTextClassification);
    mTextClassificationAsyncTask = null;
  }
  
  @VisibleForTesting
  public static <T> void mergeRectangleIntoList(List<T> paramList, RectF paramRectF, Function<T, RectF> paramFunction, Function<RectF, T> paramFunction1)
  {
    if (paramRectF.isEmpty()) {
      return;
    }
    int i = paramList.size();
    for (int j = 0; j < i; j++)
    {
      RectF localRectF = (RectF)paramFunction.apply(paramList.get(j));
      if (localRectF.contains(paramRectF)) {
        return;
      }
      if (paramRectF.contains(localRectF))
      {
        localRectF.setEmpty();
      }
      else
      {
        float f1 = left;
        float f2 = right;
        int k = 1;
        int m;
        if ((f1 != f2) && (right != left)) {
          m = 0;
        } else {
          m = 1;
        }
        if ((top == top) && (bottom == bottom) && ((RectF.intersects(paramRectF, localRectF)) || (m != 0))) {
          m = k;
        } else {
          m = 0;
        }
        if (m != 0)
        {
          paramRectF.union(localRectF);
          localRectF.setEmpty();
        }
      }
    }
    for (j = i - 1; j >= 0; j--) {
      if (((RectF)paramFunction.apply(paramList.get(j))).isEmpty()) {
        paramList.remove(j);
      }
    }
    paramList.add(paramFunction1.apply(paramRectF));
  }
  
  @VisibleForTesting
  public static <T> PointF movePointInsideNearestRectangle(PointF paramPointF, List<T> paramList, Function<T, RectF> paramFunction)
  {
    float f1 = -1.0F;
    float f2 = -1.0F;
    double d1 = Double.MAX_VALUE;
    int i = paramList.size();
    int j = 0;
    while (j < i)
    {
      RectF localRectF = (RectF)paramFunction.apply(paramList.get(j));
      float f3 = localRectF.centerY();
      float f4;
      if (x > right) {
        f4 = right;
      }
      for (;;)
      {
        break;
        if (x < left) {
          f4 = left;
        } else {
          f4 = x;
        }
      }
      double d2 = Math.pow(x - f4, 2.0D) + Math.pow(y - f3, 2.0D);
      double d3 = d1;
      if (d2 < d1)
      {
        f2 = f3;
        d3 = d2;
        f1 = f4;
      }
      j++;
      d1 = d3;
    }
    return new PointF(f1, f2);
  }
  
  private void resetTextClassificationHelper()
  {
    resetTextClassificationHelper(-1, -1);
  }
  
  private void resetTextClassificationHelper(int paramInt1, int paramInt2)
  {
    int i;
    if (paramInt1 >= 0)
    {
      i = paramInt1;
      paramInt1 = paramInt2;
      if (paramInt2 >= 0) {}
    }
    else
    {
      i = mTextView.getSelectionStart();
      paramInt1 = mTextView.getSelectionEnd();
    }
    TextClassificationHelper localTextClassificationHelper = mTextClassificationHelper;
    TextView localTextView = mTextView;
    Objects.requireNonNull(localTextView);
    localTextClassificationHelper.init(new _..Lambda.yIdmBO6ZxaY03PGN08RySVVQXuE(localTextView), getText(mTextView), i, paramInt1, mTextView.getTextLocales());
  }
  
  private boolean skipTextClassification()
  {
    boolean bool1 = mTextView.usesNoOpTextClassifier();
    int i = mTextView.getSelectionEnd();
    int j = mTextView.getSelectionStart();
    boolean bool2 = false;
    if (i == j) {
      i = 1;
    } else {
      i = 0;
    }
    if ((!mTextView.hasPasswordTransformationMethod()) && (!TextView.isPasswordInputType(mTextView.getInputType()))) {
      j = 0;
    } else {
      j = 1;
    }
    if ((!bool1) && (i == 0) && (j == 0)) {
      break label93;
    }
    bool2 = true;
    label93:
    return bool2;
  }
  
  private void startActionMode(@Editor.TextActionMode int paramInt, SelectionResult paramSelectionResult)
  {
    Object localObject = getText(mTextView);
    if ((paramSelectionResult != null) && ((localObject instanceof Spannable)) && ((mTextView.isTextSelectable()) || (mTextView.isTextEditable())))
    {
      if (!getTextClassificationSettings().isModelDarkLaunchEnabled())
      {
        Selection.setSelection((Spannable)localObject, mStart, mEnd);
        mTextView.invalidate();
      }
      mTextClassification = mClassification;
    }
    else if ((paramSelectionResult != null) && (paramInt == 2))
    {
      mTextClassification = mClassification;
    }
    else
    {
      mTextClassification = null;
    }
    if (mEditor.startActionModeInternal(paramInt))
    {
      localObject = mEditor.getSelectionController();
      if ((localObject != null) && ((mTextView.isTextSelectable()) || (mTextView.isTextEditable()))) {
        ((Editor.SelectionModifierCursorController)localObject).show();
      }
      if (paramSelectionResult != null) {
        if (paramInt != 0)
        {
          if (paramInt == 2) {
            mSelectionTracker.onLinkSelected(paramSelectionResult);
          }
        }
        else {
          mSelectionTracker.onSmartSelection(paramSelectionResult);
        }
      }
    }
    mEditor.setRestartActionModeOnNextRefresh(false);
    mTextClassificationAsyncTask = null;
  }
  
  private void startLinkActionMode(SelectionResult paramSelectionResult)
  {
    startActionMode(2, paramSelectionResult);
  }
  
  private void startSelectionActionMode(SelectionResult paramSelectionResult)
  {
    startActionMode(0, paramSelectionResult);
  }
  
  private void startSelectionActionModeWithSmartSelectAnimation(SelectionResult paramSelectionResult)
  {
    Object localObject = mTextView.getLayout();
    _..Lambda.SelectionActionModeHelper.xdBRwQcbRdz8duQr0RBo4YKAnOA localXdBRwQcbRdz8duQr0RBo4YKAnOA = new _..Lambda.SelectionActionModeHelper.xdBRwQcbRdz8duQr0RBo4YKAnOA(this, paramSelectionResult);
    int i;
    if ((paramSelectionResult != null) && ((mTextView.getSelectionStart() != mStart) || (mTextView.getSelectionEnd() != mEnd))) {
      i = 1;
    } else {
      i = 0;
    }
    if (i == 0)
    {
      localXdBRwQcbRdz8duQr0RBo4YKAnOA.run();
      return;
    }
    localObject = convertSelectionToRectangles((Layout)localObject, mStart, mEnd);
    paramSelectionResult = movePointInsideNearestRectangle(new PointF(mEditor.getLastUpPositionX(), mEditor.getLastUpPositionY()), (List)localObject, _..Lambda.ChL7kntlZCrPaPVdRfaSzGdk1JU.INSTANCE);
    mSmartSelectSprite.startAnimation(paramSelectionResult, (List)localObject, localXdBRwQcbRdz8duQr0RBo4YKAnOA);
  }
  
  public TextClassification getTextClassification()
  {
    return mTextClassification;
  }
  
  public void invalidateActionModeAsync()
  {
    cancelAsyncTask();
    if (skipTextClassification())
    {
      invalidateActionMode(null);
    }
    else
    {
      resetTextClassificationHelper();
      TextView localTextView = mTextView;
      int i = mTextClassificationHelper.getTimeoutDuration();
      TextClassificationHelper localTextClassificationHelper = mTextClassificationHelper;
      Objects.requireNonNull(localTextClassificationHelper);
      _..Lambda.aOGBsMC_jnvTDjezYLRtz35nAPI localAOGBsMC_jnvTDjezYLRtz35nAPI = new _..Lambda.aOGBsMC_jnvTDjezYLRtz35nAPI(localTextClassificationHelper);
      _..Lambda.SelectionActionModeHelper.Lwzg10CkEpNBaAXBpjnWEpIlTzQ localLwzg10CkEpNBaAXBpjnWEpIlTzQ = new _..Lambda.SelectionActionModeHelper.Lwzg10CkEpNBaAXBpjnWEpIlTzQ(this);
      localTextClassificationHelper = mTextClassificationHelper;
      Objects.requireNonNull(localTextClassificationHelper);
      mTextClassificationAsyncTask = new TextClassificationAsyncTask(localTextView, i, localAOGBsMC_jnvTDjezYLRtz35nAPI, localLwzg10CkEpNBaAXBpjnWEpIlTzQ, new _..Lambda.etfJkiCJnT2dqM2O4M2TCm9i_oA(localTextClassificationHelper)).execute(new Void[0]);
    }
  }
  
  public boolean isDrawingHighlight()
  {
    boolean bool;
    if ((mSmartSelectSprite != null) && (mSmartSelectSprite.isAnimationActive())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onDestroyActionMode()
  {
    cancelSmartSelectAnimation();
    mSelectionTracker.onSelectionDestroyed();
    cancelAsyncTask();
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    if ((isDrawingHighlight()) && (mSmartSelectSprite != null)) {
      mSmartSelectSprite.draw(paramCanvas);
    }
  }
  
  public void onSelectionAction(int paramInt)
  {
    mSelectionTracker.onSelectionAction(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), getActionType(paramInt), mTextClassification);
  }
  
  public void onSelectionDrag()
  {
    mSelectionTracker.onSelectionAction(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), 106, mTextClassification);
  }
  
  public void onTextChanged(int paramInt1, int paramInt2)
  {
    mSelectionTracker.onTextChanged(paramInt1, paramInt2, mTextClassification);
  }
  
  public boolean resetSelection(int paramInt)
  {
    if (mSelectionTracker.resetSelection(paramInt, mEditor))
    {
      invalidateActionModeAsync();
      return true;
    }
    return false;
  }
  
  public void startLinkActionModeAsync(int paramInt1, int paramInt2)
  {
    mSelectionTracker.onOriginalSelection(getText(mTextView), paramInt1, paramInt2, true);
    cancelAsyncTask();
    if (skipTextClassification())
    {
      startLinkActionMode(null);
    }
    else
    {
      resetTextClassificationHelper(paramInt1, paramInt2);
      TextView localTextView = mTextView;
      paramInt1 = mTextClassificationHelper.getTimeoutDuration();
      TextClassificationHelper localTextClassificationHelper = mTextClassificationHelper;
      Objects.requireNonNull(localTextClassificationHelper);
      _..Lambda.aOGBsMC_jnvTDjezYLRtz35nAPI localAOGBsMC_jnvTDjezYLRtz35nAPI = new _..Lambda.aOGBsMC_jnvTDjezYLRtz35nAPI(localTextClassificationHelper);
      _..Lambda.SelectionActionModeHelper.WnFw1_gP20c3ltvTN6OPqQ5XUns localWnFw1_gP20c3ltvTN6OPqQ5XUns = new _..Lambda.SelectionActionModeHelper.WnFw1_gP20c3ltvTN6OPqQ5XUns(this);
      localTextClassificationHelper = mTextClassificationHelper;
      Objects.requireNonNull(localTextClassificationHelper);
      mTextClassificationAsyncTask = new TextClassificationAsyncTask(localTextView, paramInt1, localAOGBsMC_jnvTDjezYLRtz35nAPI, localWnFw1_gP20c3ltvTN6OPqQ5XUns, new _..Lambda.etfJkiCJnT2dqM2O4M2TCm9i_oA(localTextClassificationHelper)).execute(new Void[0]);
    }
  }
  
  public void startSelectionActionModeAsync(boolean paramBoolean)
  {
    boolean bool = getTextClassificationSettings().isSmartSelectionEnabled();
    mSelectionTracker.onOriginalSelection(getText(mTextView), mTextView.getSelectionStart(), mTextView.getSelectionEnd(), false);
    cancelAsyncTask();
    if (skipTextClassification())
    {
      startSelectionActionMode(null);
    }
    else
    {
      resetTextClassificationHelper();
      TextView localTextView = mTextView;
      int i = mTextClassificationHelper.getTimeoutDuration();
      if ((paramBoolean & bool))
      {
        localObject1 = mTextClassificationHelper;
        Objects.requireNonNull(localObject1);
      }
      for (Object localObject1 = new _..Lambda.E_XesXLNXm7BCuVAnjZcIGfnQJQ((TextClassificationHelper)localObject1);; localObject1 = new _..Lambda.aOGBsMC_jnvTDjezYLRtz35nAPI((TextClassificationHelper)localObject1))
      {
        break;
        localObject1 = mTextClassificationHelper;
        Objects.requireNonNull(localObject1);
      }
      if (mSmartSelectSprite != null) {}
      for (Object localObject2 = new _..Lambda.SelectionActionModeHelper.l1f1_V5lw6noQxI_3u11qF753Iw(this);; localObject2 = new _..Lambda.SelectionActionModeHelper.CcJ0IF8nDFsmkuaqvOxFqYGazzY(this)) {
        break;
      }
      TextClassificationHelper localTextClassificationHelper = mTextClassificationHelper;
      Objects.requireNonNull(localTextClassificationHelper);
      mTextClassificationAsyncTask = new TextClassificationAsyncTask(localTextView, i, (Supplier)localObject1, (Consumer)localObject2, new _..Lambda.etfJkiCJnT2dqM2O4M2TCm9i_oA(localTextClassificationHelper)).execute(new Void[0]);
    }
  }
  
  private static final class SelectionMetricsLogger
  {
    private static final String LOG_TAG = "SelectionMetricsLogger";
    private static final Pattern PATTERN_WHITESPACE = Pattern.compile("\\s+");
    private TextClassifier mClassificationSession;
    private final boolean mEditTextLogger;
    private int mStartIndex;
    private String mText;
    private final BreakIterator mTokenIterator;
    
    SelectionMetricsLogger(TextView paramTextView)
    {
      Preconditions.checkNotNull(paramTextView);
      mEditTextLogger = paramTextView.isTextEditable();
      mTokenIterator = SelectionSessionLogger.getTokenIterator(paramTextView.getTextLocale());
    }
    
    private int countWordsBackward(int paramInt)
    {
      boolean bool;
      if (paramInt >= mStartIndex) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool);
      int i = 0;
      int j = paramInt;
      for (paramInt = i; j > mStartIndex; paramInt = i)
      {
        int k = mTokenIterator.preceding(j);
        i = paramInt;
        if (!isWhitespace(k, j)) {
          i = paramInt + 1;
        }
        j = k;
      }
      return paramInt;
    }
    
    private int countWordsForward(int paramInt)
    {
      boolean bool;
      if (paramInt <= mStartIndex) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool);
      int i = 0;
      int j = paramInt;
      while (j < mStartIndex)
      {
        int k = mTokenIterator.following(j);
        paramInt = i;
        if (!isWhitespace(j, k)) {
          paramInt = i + 1;
        }
        j = k;
        i = paramInt;
      }
      return i;
    }
    
    private static String getWidetType(TextView paramTextView)
    {
      if (paramTextView.isTextEditable()) {
        return "edittext";
      }
      if (paramTextView.isTextSelectable()) {
        return "textview";
      }
      return "nosel-textview";
    }
    
    private int[] getWordDelta(int paramInt1, int paramInt2)
    {
      int[] arrayOfInt = new int[2];
      if (paramInt1 == mStartIndex)
      {
        arrayOfInt[0] = 0;
      }
      else if (paramInt1 < mStartIndex)
      {
        arrayOfInt[0] = (-countWordsForward(paramInt1));
      }
      else
      {
        arrayOfInt[0] = countWordsBackward(paramInt1);
        if ((!mTokenIterator.isBoundary(paramInt1)) && (!isWhitespace(mTokenIterator.preceding(paramInt1), mTokenIterator.following(paramInt1)))) {
          arrayOfInt[0] -= 1;
        }
      }
      if (paramInt2 == mStartIndex) {
        arrayOfInt[1] = 0;
      } else if (paramInt2 < mStartIndex) {
        arrayOfInt[1] = (-countWordsForward(paramInt2));
      } else {
        arrayOfInt[1] = countWordsBackward(paramInt2);
      }
      return arrayOfInt;
    }
    
    private boolean hasActiveClassificationSession()
    {
      boolean bool;
      if ((mClassificationSession != null) && (!mClassificationSession.isDestroyed())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isWhitespace(int paramInt1, int paramInt2)
    {
      return PATTERN_WHITESPACE.matcher(mText.substring(paramInt1, paramInt2)).matches();
    }
    
    public void endTextClassificationSession()
    {
      if (hasActiveClassificationSession()) {
        mClassificationSession.destroy();
      }
    }
    
    public boolean isEditTextLogger()
    {
      return mEditTextLogger;
    }
    
    public void logSelectionAction(int paramInt1, int paramInt2, int paramInt3, TextClassification paramTextClassification)
    {
      try
      {
        if (hasActiveClassificationSession())
        {
          Preconditions.checkArgumentInRange(paramInt1, 0, mText.length(), "start");
          Preconditions.checkArgumentInRange(paramInt2, paramInt1, mText.length(), "end");
          int[] arrayOfInt = getWordDelta(paramInt1, paramInt2);
          if (paramTextClassification != null) {
            mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionActionEvent(arrayOfInt[0], arrayOfInt[1], paramInt3, paramTextClassification));
          } else {
            mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionActionEvent(arrayOfInt[0], arrayOfInt[1], paramInt3));
          }
          if (SelectionEvent.isTerminal(paramInt3)) {
            endTextClassificationSession();
          }
        }
      }
      catch (Exception localException)
      {
        paramTextClassification = new StringBuilder();
        paramTextClassification.append("");
        paramTextClassification.append(localException.getMessage());
        Log.e("SelectionMetricsLogger", paramTextClassification.toString(), localException);
      }
    }
    
    public void logSelectionModified(int paramInt1, int paramInt2, TextClassification paramTextClassification, TextSelection paramTextSelection)
    {
      try
      {
        if (hasActiveClassificationSession())
        {
          Preconditions.checkArgumentInRange(paramInt1, 0, mText.length(), "start");
          Preconditions.checkArgumentInRange(paramInt2, paramInt1, mText.length(), "end");
          int[] arrayOfInt = getWordDelta(paramInt1, paramInt2);
          if (paramTextSelection != null) {
            mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionModifiedEvent(arrayOfInt[0], arrayOfInt[1], paramTextSelection));
          } else if (paramTextClassification != null) {
            mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionModifiedEvent(arrayOfInt[0], arrayOfInt[1], paramTextClassification));
          } else {
            mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionModifiedEvent(arrayOfInt[0], arrayOfInt[1]));
          }
        }
      }
      catch (Exception paramTextClassification)
      {
        paramTextSelection = new StringBuilder();
        paramTextSelection.append("");
        paramTextSelection.append(paramTextClassification.getMessage());
        Log.e("SelectionMetricsLogger", paramTextSelection.toString(), paramTextClassification);
      }
    }
    
    public void logSelectionStarted(TextClassifier paramTextClassifier, CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      try
      {
        Preconditions.checkNotNull(paramCharSequence);
        Preconditions.checkArgumentInRange(paramInt1, 0, paramCharSequence.length(), "index");
        if ((mText == null) || (!mText.contentEquals(paramCharSequence))) {
          mText = paramCharSequence.toString();
        }
        mTokenIterator.setText(mText);
        mStartIndex = paramInt1;
        mClassificationSession = paramTextClassifier;
        if (hasActiveClassificationSession()) {
          mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionStartedEvent(paramInt2, 0));
        }
      }
      catch (Exception paramCharSequence)
      {
        paramTextClassifier = new StringBuilder();
        paramTextClassifier.append("");
        paramTextClassifier.append(paramCharSequence.getMessage());
        Log.e("SelectionMetricsLogger", paramTextClassifier.toString(), paramCharSequence);
      }
    }
  }
  
  private static final class SelectionResult
  {
    private final TextClassification mClassification;
    private final int mEnd;
    private final TextSelection mSelection;
    private final int mStart;
    
    SelectionResult(int paramInt1, int paramInt2, TextClassification paramTextClassification, TextSelection paramTextSelection)
    {
      mStart = paramInt1;
      mEnd = paramInt2;
      mClassification = paramTextClassification;
      mSelection = paramTextSelection;
    }
  }
  
  private static final class SelectionTracker
  {
    private boolean mAllowReset;
    private final LogAbandonRunnable mDelayedLogAbandon = new LogAbandonRunnable(null);
    private SelectionActionModeHelper.SelectionMetricsLogger mLogger;
    private int mOriginalEnd;
    private int mOriginalStart;
    private int mSelectionEnd;
    private int mSelectionStart;
    private final TextView mTextView;
    
    SelectionTracker(TextView paramTextView)
    {
      mTextView = ((TextView)Preconditions.checkNotNull(paramTextView));
      mLogger = new SelectionActionModeHelper.SelectionMetricsLogger(paramTextView);
    }
    
    private boolean isSelectionStarted()
    {
      boolean bool;
      if ((mSelectionStart >= 0) && (mSelectionEnd >= 0) && (mSelectionStart != mSelectionEnd)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void maybeInvalidateLogger()
    {
      if (mLogger.isEditTextLogger() != mTextView.isTextEditable()) {
        mLogger = new SelectionActionModeHelper.SelectionMetricsLogger(mTextView);
      }
    }
    
    private void onClassifiedSelection(SelectionActionModeHelper.SelectionResult paramSelectionResult)
    {
      if (isSelectionStarted())
      {
        mSelectionStart = mStart;
        mSelectionEnd = mEnd;
        boolean bool;
        if ((mSelectionStart == mOriginalStart) && (mSelectionEnd == mOriginalEnd)) {
          bool = false;
        } else {
          bool = true;
        }
        mAllowReset = bool;
      }
    }
    
    public void onLinkSelected(SelectionActionModeHelper.SelectionResult paramSelectionResult)
    {
      onClassifiedSelection(paramSelectionResult);
    }
    
    public void onOriginalSelection(CharSequence paramCharSequence, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      mDelayedLogAbandon.flush();
      mSelectionStart = paramInt1;
      mOriginalStart = paramInt1;
      mSelectionEnd = paramInt2;
      mOriginalEnd = paramInt2;
      mAllowReset = false;
      maybeInvalidateLogger();
      SelectionActionModeHelper.SelectionMetricsLogger localSelectionMetricsLogger = mLogger;
      TextClassifier localTextClassifier = mTextView.getTextClassificationSession();
      if (paramBoolean) {
        paramInt2 = 2;
      } else {
        paramInt2 = 1;
      }
      localSelectionMetricsLogger.logSelectionStarted(localTextClassifier, paramCharSequence, paramInt1, paramInt2);
    }
    
    public void onSelectionAction(int paramInt1, int paramInt2, int paramInt3, TextClassification paramTextClassification)
    {
      if (isSelectionStarted())
      {
        mAllowReset = false;
        mLogger.logSelectionAction(paramInt1, paramInt2, paramInt3, paramTextClassification);
      }
    }
    
    public void onSelectionDestroyed()
    {
      mAllowReset = false;
      mDelayedLogAbandon.schedule(100);
    }
    
    public void onSelectionUpdated(int paramInt1, int paramInt2, TextClassification paramTextClassification)
    {
      if (isSelectionStarted())
      {
        mSelectionStart = paramInt1;
        mSelectionEnd = paramInt2;
        mAllowReset = false;
        mLogger.logSelectionModified(paramInt1, paramInt2, paramTextClassification, null);
      }
    }
    
    public void onSmartSelection(SelectionActionModeHelper.SelectionResult paramSelectionResult)
    {
      onClassifiedSelection(paramSelectionResult);
      mLogger.logSelectionModified(mStart, mEnd, mClassification, mSelection);
    }
    
    public void onTextChanged(int paramInt1, int paramInt2, TextClassification paramTextClassification)
    {
      if ((isSelectionStarted()) && (paramInt1 == mSelectionStart) && (paramInt2 == mSelectionEnd)) {
        onSelectionAction(paramInt1, paramInt2, 100, paramTextClassification);
      }
    }
    
    public boolean resetSelection(int paramInt, Editor paramEditor)
    {
      TextView localTextView = paramEditor.getTextView();
      if ((isSelectionStarted()) && (mAllowReset) && (paramInt >= mSelectionStart) && (paramInt <= mSelectionEnd) && ((SelectionActionModeHelper.getText(localTextView) instanceof Spannable)))
      {
        mAllowReset = false;
        boolean bool = paramEditor.selectCurrentWord();
        if (bool)
        {
          mSelectionStart = paramEditor.getTextView().getSelectionStart();
          mSelectionEnd = paramEditor.getTextView().getSelectionEnd();
          mLogger.logSelectionAction(localTextView.getSelectionStart(), localTextView.getSelectionEnd(), 201, null);
        }
        return bool;
      }
      return false;
    }
    
    private final class LogAbandonRunnable
      implements Runnable
    {
      private boolean mIsPending;
      
      private LogAbandonRunnable() {}
      
      void flush()
      {
        mTextView.removeCallbacks(this);
        run();
      }
      
      public void run()
      {
        if (mIsPending)
        {
          mLogger.logSelectionAction(mSelectionStart, mSelectionEnd, 107, null);
          SelectionActionModeHelper.SelectionTracker.access$702(SelectionActionModeHelper.SelectionTracker.this, SelectionActionModeHelper.SelectionTracker.access$802(SelectionActionModeHelper.SelectionTracker.this, -1));
          mLogger.endTextClassificationSession();
          mIsPending = false;
        }
      }
      
      void schedule(int paramInt)
      {
        if (mIsPending)
        {
          Log.e("SelectActionModeHelper", "Force flushing abandon due to new scheduling request");
          flush();
        }
        mIsPending = true;
        mTextView.postDelayed(this, paramInt);
      }
    }
  }
  
  private static final class TextClassificationAsyncTask
    extends AsyncTask<Void, Void, SelectionActionModeHelper.SelectionResult>
  {
    private final String mOriginalText;
    private final Consumer<SelectionActionModeHelper.SelectionResult> mSelectionResultCallback;
    private final Supplier<SelectionActionModeHelper.SelectionResult> mSelectionResultSupplier;
    private final TextView mTextView;
    private final int mTimeOutDuration;
    private final Supplier<SelectionActionModeHelper.SelectionResult> mTimeOutResultSupplier;
    
    TextClassificationAsyncTask(TextView paramTextView, int paramInt, Supplier<SelectionActionModeHelper.SelectionResult> paramSupplier1, Consumer<SelectionActionModeHelper.SelectionResult> paramConsumer, Supplier<SelectionActionModeHelper.SelectionResult> paramSupplier2)
    {
      super();
      mTextView = ((TextView)Preconditions.checkNotNull(paramTextView));
      mTimeOutDuration = paramInt;
      mSelectionResultSupplier = ((Supplier)Preconditions.checkNotNull(paramSupplier1));
      mSelectionResultCallback = ((Consumer)Preconditions.checkNotNull(paramConsumer));
      mTimeOutResultSupplier = ((Supplier)Preconditions.checkNotNull(paramSupplier2));
      mOriginalText = SelectionActionModeHelper.getText(mTextView).toString();
    }
    
    private void onTimeOut()
    {
      if (getStatus() == AsyncTask.Status.RUNNING) {
        onPostExecute((SelectionActionModeHelper.SelectionResult)mTimeOutResultSupplier.get());
      }
      cancel(true);
    }
    
    protected SelectionActionModeHelper.SelectionResult doInBackground(Void... paramVarArgs)
    {
      paramVarArgs = new _..Lambda.SelectionActionModeHelper.TextClassificationAsyncTask.D5tkmK_caFBtl9ux2L0aUfUee4E(this);
      mTextView.postDelayed(paramVarArgs, mTimeOutDuration);
      SelectionActionModeHelper.SelectionResult localSelectionResult = (SelectionActionModeHelper.SelectionResult)mSelectionResultSupplier.get();
      mTextView.removeCallbacks(paramVarArgs);
      return localSelectionResult;
    }
    
    protected void onPostExecute(SelectionActionModeHelper.SelectionResult paramSelectionResult)
    {
      if (!TextUtils.equals(mOriginalText, SelectionActionModeHelper.getText(mTextView))) {
        paramSelectionResult = null;
      }
      mSelectionResultCallback.accept(paramSelectionResult);
    }
  }
  
  private static final class TextClassificationHelper
  {
    private static final int TRIM_DELTA = 120;
    private final Context mContext;
    private LocaleList mDefaultLocales;
    private boolean mHot;
    private LocaleList mLastClassificationLocales;
    private SelectionActionModeHelper.SelectionResult mLastClassificationResult;
    private int mLastClassificationSelectionEnd;
    private int mLastClassificationSelectionStart;
    private CharSequence mLastClassificationText;
    private int mRelativeEnd;
    private int mRelativeStart;
    private int mSelectionEnd;
    private int mSelectionStart;
    private String mText;
    private Supplier<TextClassifier> mTextClassifier;
    private int mTrimStart;
    private CharSequence mTrimmedText;
    
    TextClassificationHelper(Context paramContext, Supplier<TextClassifier> paramSupplier, CharSequence paramCharSequence, int paramInt1, int paramInt2, LocaleList paramLocaleList)
    {
      init(paramSupplier, paramCharSequence, paramInt1, paramInt2, paramLocaleList);
      mContext = ((Context)Preconditions.checkNotNull(paramContext));
    }
    
    private boolean isDarkLaunchEnabled()
    {
      return TextClassificationManager.getSettings(mContext).isModelDarkLaunchEnabled();
    }
    
    private SelectionActionModeHelper.SelectionResult performClassification(TextSelection paramTextSelection)
    {
      if ((!Objects.equals(mText, mLastClassificationText)) || (mSelectionStart != mLastClassificationSelectionStart) || (mSelectionEnd != mLastClassificationSelectionEnd) || (!Objects.equals(mDefaultLocales, mLastClassificationLocales)))
      {
        mLastClassificationText = mText;
        mLastClassificationSelectionStart = mSelectionStart;
        mLastClassificationSelectionEnd = mSelectionEnd;
        mLastClassificationLocales = mDefaultLocales;
        trimText();
        Object localObject;
        if (mContext.getApplicationInfo().targetSdkVersion >= 28)
        {
          localObject = new TextClassification.Request.Builder(mTrimmedText, mRelativeStart, mRelativeEnd).setDefaultLocales(mDefaultLocales).build();
          localObject = ((TextClassifier)mTextClassifier.get()).classifyText((TextClassification.Request)localObject);
        }
        else
        {
          localObject = ((TextClassifier)mTextClassifier.get()).classifyText(mTrimmedText, mRelativeStart, mRelativeEnd, mDefaultLocales);
        }
        mLastClassificationResult = new SelectionActionModeHelper.SelectionResult(mSelectionStart, mSelectionEnd, (TextClassification)localObject, paramTextSelection);
      }
      return mLastClassificationResult;
    }
    
    private void trimText()
    {
      mTrimStart = Math.max(0, mSelectionStart - 120);
      int i = Math.min(mText.length(), mSelectionEnd + 120);
      mTrimmedText = mText.subSequence(mTrimStart, i);
      mRelativeStart = (mSelectionStart - mTrimStart);
      mRelativeEnd = (mSelectionEnd - mTrimStart);
    }
    
    public SelectionActionModeHelper.SelectionResult classifyText()
    {
      mHot = true;
      return performClassification(null);
    }
    
    public SelectionActionModeHelper.SelectionResult getOriginalSelection()
    {
      return new SelectionActionModeHelper.SelectionResult(mSelectionStart, mSelectionEnd, null, null);
    }
    
    public int getTimeoutDuration()
    {
      if (mHot) {
        return 200;
      }
      return 500;
    }
    
    public void init(Supplier<TextClassifier> paramSupplier, CharSequence paramCharSequence, int paramInt1, int paramInt2, LocaleList paramLocaleList)
    {
      mTextClassifier = ((Supplier)Preconditions.checkNotNull(paramSupplier));
      mText = ((CharSequence)Preconditions.checkNotNull(paramCharSequence)).toString();
      mLastClassificationText = null;
      boolean bool;
      if (paramInt2 > paramInt1) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool);
      mSelectionStart = paramInt1;
      mSelectionEnd = paramInt2;
      mDefaultLocales = paramLocaleList;
    }
    
    public SelectionActionModeHelper.SelectionResult suggestSelection()
    {
      mHot = true;
      trimText();
      Object localObject;
      if (mContext.getApplicationInfo().targetSdkVersion >= 28)
      {
        localObject = new TextSelection.Request.Builder(mTrimmedText, mRelativeStart, mRelativeEnd).setDefaultLocales(mDefaultLocales).setDarkLaunchAllowed(true).build();
        localObject = ((TextClassifier)mTextClassifier.get()).suggestSelection((TextSelection.Request)localObject);
      }
      else
      {
        localObject = ((TextClassifier)mTextClassifier.get()).suggestSelection(mTrimmedText, mRelativeStart, mRelativeEnd, mDefaultLocales);
      }
      if (!isDarkLaunchEnabled())
      {
        mSelectionStart = Math.max(0, ((TextSelection)localObject).getSelectionStartIndex() + mTrimStart);
        mSelectionEnd = Math.min(mText.length(), ((TextSelection)localObject).getSelectionEndIndex() + mTrimStart);
      }
      return performClassification((TextSelection)localObject);
    }
  }
}
