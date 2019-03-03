package android.widget;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import java.lang.ref.WeakReference;

public class AutoCompleteTextView
  extends EditText
  implements Filter.FilterListener
{
  static final boolean DEBUG = false;
  static final int EXPAND_MAX = 3;
  static final String TAG = "AutoCompleteTextView";
  private ListAdapter mAdapter;
  private boolean mBlockCompletion;
  private int mDropDownAnchorId;
  private boolean mDropDownDismissedOnCompletion = true;
  private Filter mFilter;
  private int mHintResource;
  private CharSequence mHintText;
  private TextView mHintView;
  private AdapterView.OnItemClickListener mItemClickListener;
  private AdapterView.OnItemSelectedListener mItemSelectedListener;
  private int mLastKeyCode = 0;
  private PopupDataSetObserver mObserver;
  private boolean mOpenBefore;
  private final PassThroughClickListener mPassThroughClickListener;
  private final ListPopupWindow mPopup;
  private boolean mPopupCanBeUpdated = true;
  private final Context mPopupContext;
  private int mThreshold;
  private Validator mValidator = null;
  
  public AutoCompleteTextView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842859);
  }
  
  public AutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    this(paramContext, paramAttributeSet, paramInt1, paramInt2, null);
  }
  
  public AutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, Resources.Theme paramTheme)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AutoCompleteTextView, paramInt1, paramInt2);
    if (paramTheme != null)
    {
      mPopupContext = new ContextThemeWrapper(paramContext, paramTheme);
    }
    else
    {
      i = localTypedArray.getResourceId(8, 0);
      if (i != 0) {
        mPopupContext = new ContextThemeWrapper(paramContext, i);
      } else {
        mPopupContext = paramContext;
      }
    }
    if (mPopupContext != paramContext) {
      paramContext = mPopupContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AutoCompleteTextView, paramInt1, paramInt2);
    } else {
      paramContext = localTypedArray;
    }
    Drawable localDrawable = paramContext.getDrawable(3);
    int i = paramContext.getLayoutDimension(5, -2);
    int j = paramContext.getLayoutDimension(7, -2);
    int k = paramContext.getResourceId(1, 17367311);
    paramTheme = paramContext.getText(0);
    if (paramContext != localTypedArray) {
      paramContext.recycle();
    }
    mPopup = new ListPopupWindow(mPopupContext, paramAttributeSet, paramInt1, paramInt2);
    mPopup.setSoftInputMode(16);
    mPopup.setPromptPosition(1);
    mPopup.setListSelector(localDrawable);
    mPopup.setOnItemClickListener(new DropDownItemClickListener(null));
    mPopup.setWidth(i);
    mPopup.setHeight(j);
    mHintResource = k;
    setCompletionHint(paramTheme);
    mDropDownAnchorId = localTypedArray.getResourceId(6, -1);
    mThreshold = localTypedArray.getInt(2, 2);
    localTypedArray.recycle();
    paramInt1 = getInputType();
    if ((paramInt1 & 0xF) == 1) {
      setRawInputType(paramInt1 | 0x10000);
    }
    setFocusable(true);
    addTextChangedListener(new MyWatcher(null));
    mPassThroughClickListener = new PassThroughClickListener(null);
    super.setOnClickListener(mPassThroughClickListener);
  }
  
  private void buildImeCompletions()
  {
    Object localObject1 = mAdapter;
    if (localObject1 != null)
    {
      InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
      if (localInputMethodManager != null)
      {
        int i = Math.min(((ListAdapter)localObject1).getCount(), 20);
        CompletionInfo[] arrayOfCompletionInfo = new CompletionInfo[i];
        int j = 0;
        int k = 0;
        while (k < i)
        {
          int m = j;
          if (((ListAdapter)localObject1).isEnabled(k))
          {
            Object localObject2 = ((ListAdapter)localObject1).getItem(k);
            arrayOfCompletionInfo[j] = new CompletionInfo(((ListAdapter)localObject1).getItemId(k), j, convertSelectionToString(localObject2));
            m = j + 1;
          }
          k++;
          j = m;
        }
        localObject1 = arrayOfCompletionInfo;
        if (j != i)
        {
          localObject1 = new CompletionInfo[j];
          System.arraycopy(arrayOfCompletionInfo, 0, localObject1, 0, j);
        }
        localInputMethodManager.displayCompletions(this, (CompletionInfo[])localObject1);
      }
    }
  }
  
  private void onClickImpl()
  {
    if (isPopupShowing()) {
      ensureImeVisible(true);
    }
  }
  
  private void performCompletion(View paramView, int paramInt, long paramLong)
  {
    if (isPopupShowing())
    {
      Object localObject;
      if (paramInt < 0) {
        localObject = mPopup.getSelectedItem();
      } else {
        localObject = mAdapter.getItem(paramInt);
      }
      if (localObject == null)
      {
        Log.w("AutoCompleteTextView", "performCompletion: no selected item");
        return;
      }
      mBlockCompletion = true;
      replaceText(convertSelectionToString(localObject));
      mBlockCompletion = false;
      if (mItemClickListener != null)
      {
        localObject = mPopup;
        int i;
        if (paramView != null)
        {
          i = paramInt;
          if (paramInt >= 0) {}
        }
        else
        {
          paramView = ((ListPopupWindow)localObject).getSelectedView();
          i = ((ListPopupWindow)localObject).getSelectedItemPosition();
          paramLong = ((ListPopupWindow)localObject).getSelectedItemId();
        }
        mItemClickListener.onItemClick(((ListPopupWindow)localObject).getListView(), paramView, i, paramLong);
      }
    }
    if ((mDropDownDismissedOnCompletion) && (!mPopup.isDropDownAlwaysVisible())) {
      dismissDropDown();
    }
  }
  
  private void updateDropDownForFilter(int paramInt)
  {
    if (getWindowVisibility() == 8) {
      return;
    }
    boolean bool1 = mPopup.isDropDownAlwaysVisible();
    boolean bool2 = enoughToFilter();
    if (((paramInt > 0) || (bool1)) && (bool2))
    {
      if ((hasFocus()) && (hasWindowFocus()) && (mPopupCanBeUpdated)) {
        showDropDown();
      }
    }
    else if ((!bool1) && (isPopupShowing()))
    {
      dismissDropDown();
      mPopupCanBeUpdated = true;
    }
  }
  
  public void clearListSelection()
  {
    mPopup.clearListSelection();
  }
  
  protected CharSequence convertSelectionToString(Object paramObject)
  {
    return mFilter.convertResultToString(paramObject);
  }
  
  public void dismissDropDown()
  {
    InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
    if (localInputMethodManager != null) {
      localInputMethodManager.displayCompletions(this, null);
    }
    mPopup.dismiss();
    mPopupCanBeUpdated = false;
  }
  
  void doAfterTextChanged()
  {
    if (mBlockCompletion) {
      return;
    }
    if ((mOpenBefore) && (!isPopupShowing())) {
      return;
    }
    if (enoughToFilter())
    {
      if (mFilter != null)
      {
        mPopupCanBeUpdated = true;
        performFiltering(getText(), mLastKeyCode);
      }
    }
    else
    {
      if (!mPopup.isDropDownAlwaysVisible()) {
        dismissDropDown();
      }
      if (mFilter != null) {
        mFilter.filter(null);
      }
    }
  }
  
  void doBeforeTextChanged()
  {
    if (mBlockCompletion) {
      return;
    }
    mOpenBefore = isPopupShowing();
  }
  
  public boolean enoughToFilter()
  {
    boolean bool;
    if (getText().length() >= mThreshold) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void ensureImeVisible(boolean paramBoolean)
  {
    ListPopupWindow localListPopupWindow = mPopup;
    int i;
    if (paramBoolean) {
      i = 1;
    } else {
      i = 2;
    }
    localListPopupWindow.setInputMethodMode(i);
    if ((mPopup.isDropDownAlwaysVisible()) || ((mFilter != null) && (enoughToFilter()))) {
      showDropDown();
    }
  }
  
  public ListAdapter getAdapter()
  {
    return mAdapter;
  }
  
  public CharSequence getCompletionHint()
  {
    return mHintText;
  }
  
  public int getDropDownAnchor()
  {
    return mDropDownAnchorId;
  }
  
  public int getDropDownAnimationStyle()
  {
    return mPopup.getAnimationStyle();
  }
  
  public Drawable getDropDownBackground()
  {
    return mPopup.getBackground();
  }
  
  public int getDropDownHeight()
  {
    return mPopup.getHeight();
  }
  
  public int getDropDownHorizontalOffset()
  {
    return mPopup.getHorizontalOffset();
  }
  
  public int getDropDownVerticalOffset()
  {
    return mPopup.getVerticalOffset();
  }
  
  public int getDropDownWidth()
  {
    return mPopup.getWidth();
  }
  
  protected Filter getFilter()
  {
    return mFilter;
  }
  
  @Deprecated
  public AdapterView.OnItemClickListener getItemClickListener()
  {
    return mItemClickListener;
  }
  
  @Deprecated
  public AdapterView.OnItemSelectedListener getItemSelectedListener()
  {
    return mItemSelectedListener;
  }
  
  public int getListSelection()
  {
    return mPopup.getSelectedItemPosition();
  }
  
  public AdapterView.OnItemClickListener getOnItemClickListener()
  {
    return mItemClickListener;
  }
  
  public AdapterView.OnItemSelectedListener getOnItemSelectedListener()
  {
    return mItemSelectedListener;
  }
  
  public int getThreshold()
  {
    return mThreshold;
  }
  
  public Validator getValidator()
  {
    return mValidator;
  }
  
  public boolean isDropDownAlwaysVisible()
  {
    return mPopup.isDropDownAlwaysVisible();
  }
  
  public boolean isDropDownDismissedOnCompletion()
  {
    return mDropDownDismissedOnCompletion;
  }
  
  public boolean isInputMethodNotNeeded()
  {
    boolean bool;
    if (mPopup.getInputMethodMode() == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPerformingCompletion()
  {
    return mBlockCompletion;
  }
  
  public boolean isPopupShowing()
  {
    return mPopup.isShowing();
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
  }
  
  public void onCommitCompletion(CompletionInfo paramCompletionInfo)
  {
    if (isPopupShowing()) {
      mPopup.performItemClick(paramCompletionInfo.getPosition());
    }
  }
  
  protected void onDetachedFromWindow()
  {
    dismissDropDown();
    super.onDetachedFromWindow();
  }
  
  protected void onDisplayHint(int paramInt)
  {
    super.onDisplayHint(paramInt);
    if ((paramInt == 4) && (!mPopup.isDropDownAlwaysVisible())) {
      dismissDropDown();
    }
  }
  
  public void onFilterComplete(int paramInt)
  {
    updateDropDownForFilter(paramInt);
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (isTemporarilyDetached()) {
      return;
    }
    if (!paramBoolean) {
      performValidation();
    }
    if ((!paramBoolean) && (!mPopup.isDropDownAlwaysVisible())) {
      dismissDropDown();
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (mPopup.onKeyDown(paramInt, paramKeyEvent)) {
      return true;
    }
    if ((!isPopupShowing()) && (paramInt == 20) && (paramKeyEvent.hasNoModifiers())) {
      performValidation();
    }
    if ((isPopupShowing()) && (paramInt == 61) && (paramKeyEvent.hasNoModifiers())) {
      return true;
    }
    mLastKeyCode = paramInt;
    boolean bool = super.onKeyDown(paramInt, paramKeyEvent);
    mLastKeyCode = 0;
    if ((bool) && (isPopupShowing())) {
      clearListSelection();
    }
    return bool;
  }
  
  public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && (isPopupShowing()) && (!mPopup.isDropDownAlwaysVisible()))
    {
      KeyEvent.DispatcherState localDispatcherState;
      if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
      {
        localDispatcherState = getKeyDispatcherState();
        if (localDispatcherState != null) {
          localDispatcherState.startTracking(paramKeyEvent, this);
        }
        return true;
      }
      if (paramKeyEvent.getAction() == 1)
      {
        localDispatcherState = getKeyDispatcherState();
        if (localDispatcherState != null) {
          localDispatcherState.handleUpEvent(paramKeyEvent);
        }
        if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
        {
          dismissDropDown();
          return true;
        }
      }
    }
    return super.onKeyPreIme(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((mPopup.onKeyUp(paramInt, paramKeyEvent)) && ((paramInt == 23) || (paramInt == 61) || (paramInt == 66)))
    {
      if (paramKeyEvent.hasNoModifiers()) {
        performCompletion();
      }
      return true;
    }
    if ((isPopupShowing()) && (paramInt == 61) && (paramKeyEvent.hasNoModifiers()))
    {
      performCompletion();
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    if ((!paramBoolean) && (!mPopup.isDropDownAlwaysVisible())) {
      dismissDropDown();
    }
  }
  
  public void performCompletion()
  {
    performCompletion(null, -1, -1L);
  }
  
  protected void performFiltering(CharSequence paramCharSequence, int paramInt)
  {
    mFilter.filter(paramCharSequence, this);
  }
  
  public void performValidation()
  {
    if (mValidator == null) {
      return;
    }
    Editable localEditable = getText();
    if ((!TextUtils.isEmpty(localEditable)) && (!mValidator.isValid(localEditable))) {
      setText(mValidator.fixText(localEditable));
    }
  }
  
  protected void replaceText(CharSequence paramCharSequence)
  {
    clearComposingText();
    setText(paramCharSequence);
    paramCharSequence = getText();
    Selection.setSelection(paramCharSequence, paramCharSequence.length());
  }
  
  public <T extends ListAdapter,  extends Filterable> void setAdapter(T paramT)
  {
    if (mObserver == null) {
      mObserver = new PopupDataSetObserver(this, null);
    } else if (mAdapter != null) {
      mAdapter.unregisterDataSetObserver(mObserver);
    }
    mAdapter = paramT;
    if (mAdapter != null)
    {
      mFilter = ((Filterable)mAdapter).getFilter();
      paramT.registerDataSetObserver(mObserver);
    }
    else
    {
      mFilter = null;
    }
    mPopup.setAdapter(mAdapter);
  }
  
  public void setCompletionHint(CharSequence paramCharSequence)
  {
    mHintText = paramCharSequence;
    if (paramCharSequence != null)
    {
      if (mHintView == null)
      {
        paramCharSequence = (TextView)LayoutInflater.from(mPopupContext).inflate(mHintResource, null).findViewById(16908308);
        paramCharSequence.setText(mHintText);
        mHintView = paramCharSequence;
        mPopup.setPromptView(paramCharSequence);
      }
      else
      {
        mHintView.setText(paramCharSequence);
      }
    }
    else
    {
      mPopup.setPromptView(null);
      mHintView = null;
    }
  }
  
  public void setDropDownAlwaysVisible(boolean paramBoolean)
  {
    mPopup.setDropDownAlwaysVisible(paramBoolean);
  }
  
  public void setDropDownAnchor(int paramInt)
  {
    mDropDownAnchorId = paramInt;
    mPopup.setAnchorView(null);
  }
  
  public void setDropDownAnimationStyle(int paramInt)
  {
    mPopup.setAnimationStyle(paramInt);
  }
  
  public void setDropDownBackgroundDrawable(Drawable paramDrawable)
  {
    mPopup.setBackgroundDrawable(paramDrawable);
  }
  
  public void setDropDownBackgroundResource(int paramInt)
  {
    mPopup.setBackgroundDrawable(getContext().getDrawable(paramInt));
  }
  
  public void setDropDownDismissedOnCompletion(boolean paramBoolean)
  {
    mDropDownDismissedOnCompletion = paramBoolean;
  }
  
  public void setDropDownHeight(int paramInt)
  {
    mPopup.setHeight(paramInt);
  }
  
  public void setDropDownHorizontalOffset(int paramInt)
  {
    mPopup.setHorizontalOffset(paramInt);
  }
  
  public void setDropDownVerticalOffset(int paramInt)
  {
    mPopup.setVerticalOffset(paramInt);
  }
  
  public void setDropDownWidth(int paramInt)
  {
    mPopup.setWidth(paramInt);
  }
  
  public void setForceIgnoreOutsideTouch(boolean paramBoolean)
  {
    mPopup.setForceIgnoreOutsideTouch(paramBoolean);
  }
  
  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    if (isPopupShowing()) {
      showDropDown();
    }
    return bool;
  }
  
  public void setListSelection(int paramInt)
  {
    mPopup.setSelection(paramInt);
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    PassThroughClickListener.access$302(mPassThroughClickListener, paramOnClickListener);
  }
  
  public void setOnDismissListener(final OnDismissListener paramOnDismissListener)
  {
    PopupWindow.OnDismissListener local1 = null;
    if (paramOnDismissListener != null) {
      local1 = new PopupWindow.OnDismissListener()
      {
        public void onDismiss()
        {
          paramOnDismissListener.onDismiss();
        }
      };
    }
    mPopup.setOnDismissListener(local1);
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    mItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
  {
    mItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public void setText(CharSequence paramCharSequence, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setText(paramCharSequence);
    }
    else
    {
      mBlockCompletion = true;
      setText(paramCharSequence);
      mBlockCompletion = false;
    }
  }
  
  public void setThreshold(int paramInt)
  {
    int i = paramInt;
    if (paramInt <= 0) {
      i = 1;
    }
    mThreshold = i;
  }
  
  public void setValidator(Validator paramValidator)
  {
    mValidator = paramValidator;
  }
  
  public void showDropDown()
  {
    buildImeCompletions();
    if (mPopup.getAnchorView() == null) {
      if (mDropDownAnchorId != -1) {
        mPopup.setAnchorView(getRootView().findViewById(mDropDownAnchorId));
      } else {
        mPopup.setAnchorView(this);
      }
    }
    if (!isPopupShowing())
    {
      mPopup.setInputMethodMode(1);
      mPopup.setListItemExpandMax(3);
    }
    mPopup.show();
    mPopup.getListView().setOverScrollMode(0);
  }
  
  public void showDropDownAfterLayout()
  {
    mPopup.postShow();
  }
  
  private class DropDownItemClickListener
    implements AdapterView.OnItemClickListener
  {
    private DropDownItemClickListener() {}
    
    public void onItemClick(AdapterView paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      AutoCompleteTextView.this.performCompletion(paramView, paramInt, paramLong);
    }
  }
  
  private class MyWatcher
    implements TextWatcher
  {
    private MyWatcher() {}
    
    public void afterTextChanged(Editable paramEditable)
    {
      doAfterTextChanged();
    }
    
    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      doBeforeTextChanged();
    }
    
    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  }
  
  public static abstract interface OnDismissListener
  {
    public abstract void onDismiss();
  }
  
  private class PassThroughClickListener
    implements View.OnClickListener
  {
    private View.OnClickListener mWrapped;
    
    private PassThroughClickListener() {}
    
    public void onClick(View paramView)
    {
      AutoCompleteTextView.this.onClickImpl();
      if (mWrapped != null) {
        mWrapped.onClick(paramView);
      }
    }
  }
  
  private static class PopupDataSetObserver
    extends DataSetObserver
  {
    private final WeakReference<AutoCompleteTextView> mViewReference;
    private final Runnable updateRunnable = new Runnable()
    {
      public void run()
      {
        AutoCompleteTextView localAutoCompleteTextView = (AutoCompleteTextView)mViewReference.get();
        if (localAutoCompleteTextView == null) {
          return;
        }
        ListAdapter localListAdapter = mAdapter;
        if (localListAdapter == null) {
          return;
        }
        localAutoCompleteTextView.updateDropDownForFilter(localListAdapter.getCount());
      }
    };
    
    private PopupDataSetObserver(AutoCompleteTextView paramAutoCompleteTextView)
    {
      mViewReference = new WeakReference(paramAutoCompleteTextView);
    }
    
    public void onChanged()
    {
      AutoCompleteTextView localAutoCompleteTextView = (AutoCompleteTextView)mViewReference.get();
      if ((localAutoCompleteTextView != null) && (mAdapter != null)) {
        localAutoCompleteTextView.post(updateRunnable);
      }
    }
  }
  
  public static abstract interface Validator
  {
    public abstract CharSequence fixText(CharSequence paramCharSequence);
    
    public abstract boolean isValid(CharSequence paramCharSequence);
  }
}
