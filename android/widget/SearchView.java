package android.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.app.SearchableInfo.ActionKeyInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.CollapsibleActionView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import java.util.WeakHashMap;

public class SearchView
  extends LinearLayout
  implements CollapsibleActionView
{
  private static final boolean DBG = false;
  private static final String IME_OPTION_NO_MICROPHONE = "nm";
  private static final String LOG_TAG = "SearchView";
  private Bundle mAppSearchData;
  private boolean mClearingFocus;
  private final ImageView mCloseButton;
  private final ImageView mCollapsedIcon;
  private int mCollapsedImeOptions;
  private final CharSequence mDefaultQueryHint;
  private final View mDropDownAnchor;
  private boolean mExpandedInActionView;
  private final ImageView mGoButton;
  private boolean mIconified;
  private boolean mIconifiedByDefault;
  private int mMaxWidth;
  private CharSequence mOldQueryText;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (paramAnonymousView == mSearchButton) {
        SearchView.this.onSearchClicked();
      } else if (paramAnonymousView == mCloseButton) {
        SearchView.this.onCloseClicked();
      } else if (paramAnonymousView == mGoButton) {
        SearchView.this.onSubmitQuery();
      } else if (paramAnonymousView == mVoiceButton) {
        SearchView.this.onVoiceClicked();
      } else if (paramAnonymousView == mSearchSrcTextView) {
        SearchView.this.forceSuggestionQuery();
      }
    }
  };
  private OnCloseListener mOnCloseListener;
  private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
  {
    public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
    {
      SearchView.this.onSubmitQuery();
      return true;
    }
  };
  private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      SearchView.this.onItemClicked(paramAnonymousInt, 0, null);
    }
  };
  private final AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener()
  {
    public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      SearchView.this.onItemSelected(paramAnonymousInt);
    }
    
    public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {}
  };
  private OnQueryTextListener mOnQueryChangeListener;
  private View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
  private View.OnClickListener mOnSearchClickListener;
  private OnSuggestionListener mOnSuggestionListener;
  private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache = new WeakHashMap();
  private CharSequence mQueryHint;
  private boolean mQueryRefinement;
  private Runnable mReleaseCursorRunnable = new Runnable()
  {
    public void run()
    {
      if ((mSuggestionsAdapter != null) && ((mSuggestionsAdapter instanceof SuggestionsAdapter))) {
        mSuggestionsAdapter.changeCursor(null);
      }
    }
  };
  private final ImageView mSearchButton;
  private final View mSearchEditFrame;
  private final Drawable mSearchHintIcon;
  private final View mSearchPlate;
  private final SearchAutoComplete mSearchSrcTextView;
  private Rect mSearchSrcTextViewBounds = new Rect();
  private Rect mSearchSrtTextViewBoundsExpanded = new Rect();
  private SearchableInfo mSearchable;
  private final View mSubmitArea;
  private boolean mSubmitButtonEnabled;
  private final int mSuggestionCommitIconResId;
  private final int mSuggestionRowLayout;
  private CursorAdapter mSuggestionsAdapter;
  private int[] mTemp = new int[2];
  private int[] mTemp2 = new int[2];
  View.OnKeyListener mTextKeyListener = new View.OnKeyListener()
  {
    public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
    {
      if (mSearchable == null) {
        return false;
      }
      if ((mSearchSrcTextView.isPopupShowing()) && (mSearchSrcTextView.getListSelection() != -1)) {
        return SearchView.this.onSuggestionsKey(paramAnonymousView, paramAnonymousInt, paramAnonymousKeyEvent);
      }
      if ((!SearchView.SearchAutoComplete.access$1700(mSearchSrcTextView)) && (paramAnonymousKeyEvent.hasNoModifiers()))
      {
        if ((paramAnonymousKeyEvent.getAction() == 1) && (paramAnonymousInt == 66))
        {
          paramAnonymousView.cancelLongPress();
          SearchView.this.launchQuerySearch(0, null, mSearchSrcTextView.getText().toString());
          return true;
        }
        if (paramAnonymousKeyEvent.getAction() == 0)
        {
          paramAnonymousView = mSearchable.findActionKey(paramAnonymousInt);
          if ((paramAnonymousView != null) && (paramAnonymousView.getQueryActionMsg() != null))
          {
            SearchView.this.launchQuerySearch(paramAnonymousInt, paramAnonymousView.getQueryActionMsg(), mSearchSrcTextView.getText().toString());
            return true;
          }
        }
      }
      return false;
    }
  };
  private TextWatcher mTextWatcher = new TextWatcher()
  {
    public void afterTextChanged(Editable paramAnonymousEditable) {}
    
    public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    
    public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      SearchView.this.onTextChanged(paramAnonymousCharSequence);
    }
  };
  private UpdatableTouchDelegate mTouchDelegate;
  private Runnable mUpdateDrawableStateRunnable = new Runnable()
  {
    public void run()
    {
      SearchView.this.updateFocusedState();
    }
  };
  private CharSequence mUserQuery;
  private final Intent mVoiceAppSearchIntent;
  private final ImageView mVoiceButton;
  private boolean mVoiceButtonEnabled;
  private final Intent mVoiceWebSearchIntent;
  
  public SearchView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SearchView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843904);
  }
  
  public SearchView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public SearchView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SearchView, paramInt1, paramInt2);
    ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(paramAttributeSet.getResourceId(0, 17367299), this, true);
    mSearchSrcTextView = ((SearchAutoComplete)findViewById(16909328));
    mSearchSrcTextView.setSearchView(this);
    mSearchEditFrame = findViewById(16909324);
    mSearchPlate = findViewById(16909327);
    mSubmitArea = findViewById(16909420);
    mSearchButton = ((ImageView)findViewById(16909322));
    mGoButton = ((ImageView)findViewById(16909325));
    mCloseButton = ((ImageView)findViewById(16909323));
    mVoiceButton = ((ImageView)findViewById(16909330));
    mCollapsedIcon = ((ImageView)findViewById(16909326));
    mSearchPlate.setBackground(paramAttributeSet.getDrawable(12));
    mSubmitArea.setBackground(paramAttributeSet.getDrawable(13));
    mSearchButton.setImageDrawable(paramAttributeSet.getDrawable(8));
    mGoButton.setImageDrawable(paramAttributeSet.getDrawable(7));
    mCloseButton.setImageDrawable(paramAttributeSet.getDrawable(6));
    mVoiceButton.setImageDrawable(paramAttributeSet.getDrawable(9));
    mCollapsedIcon.setImageDrawable(paramAttributeSet.getDrawable(8));
    if (paramAttributeSet.hasValueOrEmpty(14)) {
      mSearchHintIcon = paramAttributeSet.getDrawable(14);
    } else {
      mSearchHintIcon = paramAttributeSet.getDrawable(8);
    }
    mSuggestionRowLayout = paramAttributeSet.getResourceId(11, 17367298);
    mSuggestionCommitIconResId = paramAttributeSet.getResourceId(10, 0);
    mSearchButton.setOnClickListener(mOnClickListener);
    mCloseButton.setOnClickListener(mOnClickListener);
    mGoButton.setOnClickListener(mOnClickListener);
    mVoiceButton.setOnClickListener(mOnClickListener);
    mSearchSrcTextView.setOnClickListener(mOnClickListener);
    mSearchSrcTextView.addTextChangedListener(mTextWatcher);
    mSearchSrcTextView.setOnEditorActionListener(mOnEditorActionListener);
    mSearchSrcTextView.setOnItemClickListener(mOnItemClickListener);
    mSearchSrcTextView.setOnItemSelectedListener(mOnItemSelectedListener);
    mSearchSrcTextView.setOnKeyListener(mTextKeyListener);
    mSearchSrcTextView.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (mOnQueryTextFocusChangeListener != null) {
          mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, paramAnonymousBoolean);
        }
      }
    });
    setIconifiedByDefault(paramAttributeSet.getBoolean(4, true));
    paramInt1 = paramAttributeSet.getDimensionPixelSize(1, -1);
    if (paramInt1 != -1) {
      setMaxWidth(paramInt1);
    }
    mDefaultQueryHint = paramAttributeSet.getText(15);
    mQueryHint = paramAttributeSet.getText(5);
    paramInt1 = paramAttributeSet.getInt(3, -1);
    if (paramInt1 != -1) {
      setImeOptions(paramInt1);
    }
    paramInt1 = paramAttributeSet.getInt(2, -1);
    if (paramInt1 != -1) {
      setInputType(paramInt1);
    }
    if (getFocusable() == 16) {
      setFocusable(1);
    }
    paramAttributeSet.recycle();
    mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH");
    mVoiceWebSearchIntent.addFlags(268435456);
    mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
    mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
    mVoiceAppSearchIntent.addFlags(268435456);
    mDropDownAnchor = findViewById(mSearchSrcTextView.getDropDownAnchor());
    if (mDropDownAnchor != null) {
      mDropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
      {
        public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
        {
          SearchView.this.adjustDropDownSizeAndPosition();
        }
      });
    }
    updateViewsVisibility(mIconifiedByDefault);
    updateQueryHint();
  }
  
  private void adjustDropDownSizeAndPosition()
  {
    if (mDropDownAnchor.getWidth() > 1)
    {
      Resources localResources = getContext().getResources();
      int i = mSearchPlate.getPaddingLeft();
      Rect localRect = new Rect();
      boolean bool = isLayoutRtl();
      int j;
      if (mIconifiedByDefault) {
        j = localResources.getDimensionPixelSize(17105152) + localResources.getDimensionPixelSize(17105153);
      } else {
        j = 0;
      }
      mSearchSrcTextView.getDropDownBackground().getPadding(localRect);
      if (bool) {
        k = -left;
      } else {
        k = i - (left + j);
      }
      mSearchSrcTextView.setDropDownHorizontalOffset(k);
      int m = mDropDownAnchor.getWidth();
      int k = left;
      int n = right;
      mSearchSrcTextView.setDropDownWidth(m + k + n + j - i);
    }
  }
  
  private Intent createIntent(String paramString1, Uri paramUri, String paramString2, String paramString3, int paramInt, String paramString4)
  {
    paramString1 = new Intent(paramString1);
    paramString1.addFlags(268435456);
    if (paramUri != null) {
      paramString1.setData(paramUri);
    }
    paramString1.putExtra("user_query", mUserQuery);
    if (paramString3 != null) {
      paramString1.putExtra("query", paramString3);
    }
    if (paramString2 != null) {
      paramString1.putExtra("intent_extra_data_key", paramString2);
    }
    if (mAppSearchData != null) {
      paramString1.putExtra("app_data", mAppSearchData);
    }
    if (paramInt != 0)
    {
      paramString1.putExtra("action_key", paramInt);
      paramString1.putExtra("action_msg", paramString4);
    }
    paramString1.setComponent(mSearchable.getSearchActivity());
    return paramString1;
  }
  
  private Intent createIntentFromSuggestion(Cursor paramCursor, int paramInt, String paramString)
  {
    try
    {
      Object localObject1 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_action");
      Object localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = mSearchable.getSuggestIntentAction();
      }
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = "android.intent.action.SEARCH";
      }
      Object localObject3 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data");
      localObject2 = localObject3;
      if (localObject3 == null) {
        localObject2 = mSearchable.getSuggestIntentData();
      }
      localObject3 = localObject2;
      if (localObject2 != null)
      {
        String str = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data_id");
        localObject3 = localObject2;
        if (str != null)
        {
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append((String)localObject2);
          ((StringBuilder)localObject3).append("/");
          ((StringBuilder)localObject3).append(Uri.encode(str));
          localObject3 = ((StringBuilder)localObject3).toString();
        }
      }
      if (localObject3 == null) {
        localObject2 = null;
      } else {
        localObject2 = Uri.parse((String)localObject3);
      }
      localObject3 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_query");
      paramString = createIntent((String)localObject1, (Uri)localObject2, SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_extra_data"), (String)localObject3, paramInt, paramString);
      return paramString;
    }
    catch (RuntimeException paramString)
    {
      try
      {
        paramInt = paramCursor.getPosition();
      }
      catch (RuntimeException paramCursor)
      {
        paramInt = -1;
      }
      paramCursor = new StringBuilder();
      paramCursor.append("Search suggestions cursor at row ");
      paramCursor.append(paramInt);
      paramCursor.append(" returned exception.");
      Log.w("SearchView", paramCursor.toString(), paramString);
    }
    return null;
  }
  
  private Intent createVoiceAppSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo)
  {
    ComponentName localComponentName = paramSearchableInfo.getSearchActivity();
    Object localObject = new Intent("android.intent.action.SEARCH");
    ((Intent)localObject).setComponent(localComponentName);
    PendingIntent localPendingIntent = PendingIntent.getActivity(getContext(), 0, (Intent)localObject, 1073741824);
    Bundle localBundle = new Bundle();
    if (mAppSearchData != null) {
      localBundle.putParcelable("app_data", mAppSearchData);
    }
    Intent localIntent = new Intent(paramIntent);
    paramIntent = "free_form";
    localObject = null;
    String str = null;
    int i = 1;
    Resources localResources = getResources();
    if (paramSearchableInfo.getVoiceLanguageModeId() != 0) {
      paramIntent = localResources.getString(paramSearchableInfo.getVoiceLanguageModeId());
    }
    if (paramSearchableInfo.getVoicePromptTextId() != 0) {
      localObject = localResources.getString(paramSearchableInfo.getVoicePromptTextId());
    }
    if (paramSearchableInfo.getVoiceLanguageId() != 0) {
      str = localResources.getString(paramSearchableInfo.getVoiceLanguageId());
    }
    if (paramSearchableInfo.getVoiceMaxResults() != 0) {
      i = paramSearchableInfo.getVoiceMaxResults();
    }
    localIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", paramIntent);
    localIntent.putExtra("android.speech.extra.PROMPT", (String)localObject);
    localIntent.putExtra("android.speech.extra.LANGUAGE", str);
    localIntent.putExtra("android.speech.extra.MAX_RESULTS", i);
    if (localComponentName == null) {
      paramIntent = null;
    } else {
      paramIntent = localComponentName.flattenToShortString();
    }
    localIntent.putExtra("calling_package", paramIntent);
    localIntent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", localPendingIntent);
    localIntent.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", localBundle);
    return localIntent;
  }
  
  private Intent createVoiceWebSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo)
  {
    Intent localIntent = new Intent(paramIntent);
    paramIntent = paramSearchableInfo.getSearchActivity();
    if (paramIntent == null) {
      paramIntent = null;
    } else {
      paramIntent = paramIntent.flattenToShortString();
    }
    localIntent.putExtra("calling_package", paramIntent);
    return localIntent;
  }
  
  private void dismissSuggestions()
  {
    mSearchSrcTextView.dismissDropDown();
  }
  
  private void forceSuggestionQuery()
  {
    mSearchSrcTextView.doBeforeTextChanged();
    mSearchSrcTextView.doAfterTextChanged();
  }
  
  private static String getActionKeyMessage(Cursor paramCursor, SearchableInfo.ActionKeyInfo paramActionKeyInfo)
  {
    String str1 = null;
    String str2 = paramActionKeyInfo.getSuggestActionMsgColumn();
    if (str2 != null) {
      str1 = SuggestionsAdapter.getColumnString(paramCursor, str2);
    }
    paramCursor = str1;
    if (str1 == null) {
      paramCursor = paramActionKeyInfo.getSuggestActionMsg();
    }
    return paramCursor;
  }
  
  private void getChildBoundsWithinSearchView(View paramView, Rect paramRect)
  {
    paramView.getLocationInWindow(mTemp);
    getLocationInWindow(mTemp2);
    int i = mTemp[1] - mTemp2[1];
    int j = mTemp[0] - mTemp2[0];
    paramRect.set(j, i, paramView.getWidth() + j, paramView.getHeight() + i);
  }
  
  private CharSequence getDecoratedHint(CharSequence paramCharSequence)
  {
    if ((mIconifiedByDefault) && (mSearchHintIcon != null))
    {
      int i = (int)(mSearchSrcTextView.getTextSize() * 1.25D);
      mSearchHintIcon.setBounds(0, 0, i, i);
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder("   ");
      localSpannableStringBuilder.setSpan(new ImageSpan(mSearchHintIcon), 1, 2, 33);
      localSpannableStringBuilder.append(paramCharSequence);
      return localSpannableStringBuilder;
    }
    return paramCharSequence;
  }
  
  private int getPreferredHeight()
  {
    return getContext().getResources().getDimensionPixelSize(17105412);
  }
  
  private int getPreferredWidth()
  {
    return getContext().getResources().getDimensionPixelSize(17105413);
  }
  
  private boolean hasVoiceSearch()
  {
    Object localObject = mSearchable;
    boolean bool = false;
    if ((localObject != null) && (mSearchable.getVoiceSearchEnabled()))
    {
      localObject = null;
      if (mSearchable.getVoiceSearchLaunchWebSearch()) {
        localObject = mVoiceWebSearchIntent;
      } else if (mSearchable.getVoiceSearchLaunchRecognizer()) {
        localObject = mVoiceAppSearchIntent;
      }
      if (localObject != null)
      {
        if (getContext().getPackageManager().resolveActivity((Intent)localObject, 65536) != null) {
          bool = true;
        }
        return bool;
      }
    }
    return false;
  }
  
  static boolean isLandscapeMode(Context paramContext)
  {
    boolean bool;
    if (getResourcesgetConfigurationorientation == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isSubmitAreaEnabled()
  {
    boolean bool;
    if (((mSubmitButtonEnabled) || (mVoiceButtonEnabled)) && (!isIconified())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void launchIntent(Intent paramIntent)
  {
    if (paramIntent == null) {
      return;
    }
    try
    {
      getContext().startActivity(paramIntent);
    }
    catch (RuntimeException localRuntimeException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed launch activity: ");
      localStringBuilder.append(paramIntent);
      Log.e("SearchView", localStringBuilder.toString(), localRuntimeException);
    }
  }
  
  private void launchQuerySearch(int paramInt, String paramString1, String paramString2)
  {
    paramString1 = createIntent("android.intent.action.SEARCH", null, null, paramString2, paramInt, paramString1);
    getContext().startActivity(paramString1);
  }
  
  private boolean launchSuggestion(int paramInt1, int paramInt2, String paramString)
  {
    Cursor localCursor = mSuggestionsAdapter.getCursor();
    if ((localCursor != null) && (localCursor.moveToPosition(paramInt1)))
    {
      launchIntent(createIntentFromSuggestion(localCursor, paramInt2, paramString));
      return true;
    }
    return false;
  }
  
  private void onCloseClicked()
  {
    if (TextUtils.isEmpty(mSearchSrcTextView.getText()))
    {
      if ((mIconifiedByDefault) && ((mOnCloseListener == null) || (!mOnCloseListener.onClose())))
      {
        clearFocus();
        updateViewsVisibility(true);
      }
    }
    else
    {
      mSearchSrcTextView.setText("");
      mSearchSrcTextView.requestFocus();
      mSearchSrcTextView.setImeVisibility(true);
    }
  }
  
  private boolean onItemClicked(int paramInt1, int paramInt2, String paramString)
  {
    if ((mOnSuggestionListener != null) && (mOnSuggestionListener.onSuggestionClick(paramInt1))) {
      return false;
    }
    launchSuggestion(paramInt1, 0, null);
    mSearchSrcTextView.setImeVisibility(false);
    dismissSuggestions();
    return true;
  }
  
  private boolean onItemSelected(int paramInt)
  {
    if ((mOnSuggestionListener != null) && (mOnSuggestionListener.onSuggestionSelect(paramInt))) {
      return false;
    }
    rewriteQueryFromSuggestion(paramInt);
    return true;
  }
  
  private void onSearchClicked()
  {
    updateViewsVisibility(false);
    mSearchSrcTextView.requestFocus();
    mSearchSrcTextView.setImeVisibility(true);
    if (mOnSearchClickListener != null) {
      mOnSearchClickListener.onClick(this);
    }
  }
  
  private void onSubmitQuery()
  {
    Editable localEditable = mSearchSrcTextView.getText();
    if ((localEditable != null) && (TextUtils.getTrimmedLength(localEditable) > 0) && ((mOnQueryChangeListener == null) || (!mOnQueryChangeListener.onQueryTextSubmit(localEditable.toString()))))
    {
      if (mSearchable != null) {
        launchQuerySearch(0, null, localEditable.toString());
      }
      mSearchSrcTextView.setImeVisibility(false);
      dismissSuggestions();
    }
  }
  
  private boolean onSuggestionsKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    if (mSearchable == null) {
      return false;
    }
    if (mSuggestionsAdapter == null) {
      return false;
    }
    if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.hasNoModifiers())) {
      if ((paramInt != 66) && (paramInt != 84) && (paramInt != 61))
      {
        if ((paramInt != 21) && (paramInt != 22))
        {
          if ((paramInt == 19) && (mSearchSrcTextView.getListSelection() == 0)) {
            return false;
          }
          paramView = mSearchable.findActionKey(paramInt);
          if ((paramView != null) && ((paramView.getSuggestActionMsg() != null) || (paramView.getSuggestActionMsgColumn() != null)))
          {
            int i = mSearchSrcTextView.getListSelection();
            if (i != -1)
            {
              paramKeyEvent = mSuggestionsAdapter.getCursor();
              if (paramKeyEvent.moveToPosition(i))
              {
                paramView = getActionKeyMessage(paramKeyEvent, paramView);
                if ((paramView != null) && (paramView.length() > 0)) {
                  return onItemClicked(i, paramInt, paramView);
                }
              }
            }
          }
        }
        else
        {
          if (paramInt == 21) {
            paramInt = 0;
          } else {
            paramInt = mSearchSrcTextView.length();
          }
          mSearchSrcTextView.setSelection(paramInt);
          mSearchSrcTextView.setListSelection(0);
          mSearchSrcTextView.clearListSelection();
          mSearchSrcTextView.ensureImeVisible(true);
          return true;
        }
      }
      else {
        return onItemClicked(mSearchSrcTextView.getListSelection(), 0, null);
      }
    }
    return false;
  }
  
  private void onTextChanged(CharSequence paramCharSequence)
  {
    Editable localEditable = mSearchSrcTextView.getText();
    mUserQuery = localEditable;
    boolean bool1 = TextUtils.isEmpty(localEditable);
    boolean bool2 = true;
    bool1 ^= true;
    updateSubmitButton(bool1);
    if (bool1) {
      bool2 = false;
    }
    updateVoiceButton(bool2);
    updateCloseButton();
    updateSubmitArea();
    if ((mOnQueryChangeListener != null) && (!TextUtils.equals(paramCharSequence, mOldQueryText))) {
      mOnQueryChangeListener.onQueryTextChange(paramCharSequence.toString());
    }
    mOldQueryText = paramCharSequence.toString();
  }
  
  private void onVoiceClicked()
  {
    if (mSearchable == null) {
      return;
    }
    Object localObject = mSearchable;
    try
    {
      if (((SearchableInfo)localObject).getVoiceSearchLaunchWebSearch())
      {
        localObject = createVoiceWebSearchIntent(mVoiceWebSearchIntent, (SearchableInfo)localObject);
        getContext().startActivity((Intent)localObject);
      }
      else if (((SearchableInfo)localObject).getVoiceSearchLaunchRecognizer())
      {
        localObject = createVoiceAppSearchIntent(mVoiceAppSearchIntent, (SearchableInfo)localObject);
        getContext().startActivity((Intent)localObject);
      }
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Log.w("SearchView", "Could not find voice search activity");
    }
  }
  
  private void postUpdateFocusedState()
  {
    post(mUpdateDrawableStateRunnable);
  }
  
  private void rewriteQueryFromSuggestion(int paramInt)
  {
    Editable localEditable = mSearchSrcTextView.getText();
    Object localObject = mSuggestionsAdapter.getCursor();
    if (localObject == null) {
      return;
    }
    if (((Cursor)localObject).moveToPosition(paramInt))
    {
      localObject = mSuggestionsAdapter.convertToString((Cursor)localObject);
      if (localObject != null) {
        setQuery((CharSequence)localObject);
      } else {
        setQuery(localEditable);
      }
    }
    else
    {
      setQuery(localEditable);
    }
  }
  
  private void setQuery(CharSequence paramCharSequence)
  {
    mSearchSrcTextView.setText(paramCharSequence, true);
    SearchAutoComplete localSearchAutoComplete = mSearchSrcTextView;
    int i;
    if (TextUtils.isEmpty(paramCharSequence)) {
      i = 0;
    } else {
      i = paramCharSequence.length();
    }
    localSearchAutoComplete.setSelection(i);
  }
  
  private void updateCloseButton()
  {
    boolean bool = TextUtils.isEmpty(mSearchSrcTextView.getText());
    int i = 1;
    int j = bool ^ true;
    int k = 0;
    int m = i;
    if (j == 0) {
      if ((mIconifiedByDefault) && (!mExpandedInActionView)) {
        m = i;
      } else {
        m = 0;
      }
    }
    Object localObject = mCloseButton;
    if (m != 0) {
      m = k;
    } else {
      m = 8;
    }
    ((ImageView)localObject).setVisibility(m);
    Drawable localDrawable = mCloseButton.getDrawable();
    if (localDrawable != null)
    {
      if (j != 0) {
        localObject = ENABLED_STATE_SET;
      } else {
        localObject = EMPTY_STATE_SET;
      }
      localDrawable.setState((int[])localObject);
    }
  }
  
  private void updateFocusedState()
  {
    int[] arrayOfInt;
    if (mSearchSrcTextView.hasFocus()) {
      arrayOfInt = FOCUSED_STATE_SET;
    } else {
      arrayOfInt = EMPTY_STATE_SET;
    }
    Drawable localDrawable = mSearchPlate.getBackground();
    if (localDrawable != null) {
      localDrawable.setState(arrayOfInt);
    }
    localDrawable = mSubmitArea.getBackground();
    if (localDrawable != null) {
      localDrawable.setState(arrayOfInt);
    }
    invalidate();
  }
  
  private void updateQueryHint()
  {
    Object localObject = getQueryHint();
    SearchAutoComplete localSearchAutoComplete = mSearchSrcTextView;
    if (localObject == null) {
      localObject = "";
    }
    localSearchAutoComplete.setHint(getDecoratedHint((CharSequence)localObject));
  }
  
  private void updateSearchAutoComplete()
  {
    mSearchSrcTextView.setDropDownAnimationStyle(0);
    mSearchSrcTextView.setThreshold(mSearchable.getSuggestThreshold());
    mSearchSrcTextView.setImeOptions(mSearchable.getImeOptions());
    int i = mSearchable.getInputType();
    int j = 1;
    int k = i;
    if ((i & 0xF) == 1)
    {
      i &= 0xFFFEFFFF;
      k = i;
      if (mSearchable.getSuggestAuthority() != null) {
        k = i | 0x10000 | 0x80000;
      }
    }
    mSearchSrcTextView.setInputType(k);
    if (mSuggestionsAdapter != null) {
      mSuggestionsAdapter.changeCursor(null);
    }
    if (mSearchable.getSuggestAuthority() != null)
    {
      mSuggestionsAdapter = new SuggestionsAdapter(getContext(), this, mSearchable, mOutsideDrawablesCache);
      mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
      SuggestionsAdapter localSuggestionsAdapter = (SuggestionsAdapter)mSuggestionsAdapter;
      if (mQueryRefinement) {
        k = 2;
      } else {
        k = j;
      }
      localSuggestionsAdapter.setQueryRefinement(k);
    }
  }
  
  private void updateSubmitArea()
  {
    int i = 8;
    int j = i;
    if (isSubmitAreaEnabled()) {
      if (mGoButton.getVisibility() != 0)
      {
        j = i;
        if (mVoiceButton.getVisibility() != 0) {}
      }
      else
      {
        j = 0;
      }
    }
    mSubmitArea.setVisibility(j);
  }
  
  private void updateSubmitButton(boolean paramBoolean)
  {
    int i = 8;
    int j = i;
    if (mSubmitButtonEnabled)
    {
      j = i;
      if (isSubmitAreaEnabled())
      {
        j = i;
        if (hasFocus()) {
          if (!paramBoolean)
          {
            j = i;
            if (mVoiceButtonEnabled) {}
          }
          else
          {
            j = 0;
          }
        }
      }
    }
    mGoButton.setVisibility(j);
  }
  
  private void updateViewsVisibility(boolean paramBoolean)
  {
    mIconified = paramBoolean;
    int i = 8;
    boolean bool1 = false;
    int j;
    if (paramBoolean) {
      j = 0;
    } else {
      j = 8;
    }
    boolean bool2 = TextUtils.isEmpty(mSearchSrcTextView.getText()) ^ true;
    mSearchButton.setVisibility(j);
    updateSubmitButton(bool2);
    View localView = mSearchEditFrame;
    if (paramBoolean) {
      j = i;
    } else {
      j = 0;
    }
    localView.setVisibility(j);
    if ((mCollapsedIcon.getDrawable() != null) && (!mIconifiedByDefault)) {
      j = 0;
    } else {
      j = 8;
    }
    mCollapsedIcon.setVisibility(j);
    updateCloseButton();
    paramBoolean = bool1;
    if (!bool2) {
      paramBoolean = true;
    }
    updateVoiceButton(paramBoolean);
    updateSubmitArea();
  }
  
  private void updateVoiceButton(boolean paramBoolean)
  {
    int i = 8;
    int j = i;
    if (mVoiceButtonEnabled)
    {
      j = i;
      if (!isIconified())
      {
        j = i;
        if (paramBoolean)
        {
          j = 0;
          mGoButton.setVisibility(8);
        }
      }
    }
    mVoiceButton.setVisibility(j);
  }
  
  public void clearFocus()
  {
    mClearingFocus = true;
    super.clearFocus();
    mSearchSrcTextView.clearFocus();
    mSearchSrcTextView.setImeVisibility(false);
    mClearingFocus = false;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return SearchView.class.getName();
  }
  
  public int getImeOptions()
  {
    return mSearchSrcTextView.getImeOptions();
  }
  
  public int getInputType()
  {
    return mSearchSrcTextView.getInputType();
  }
  
  public int getMaxWidth()
  {
    return mMaxWidth;
  }
  
  public CharSequence getQuery()
  {
    return mSearchSrcTextView.getText();
  }
  
  public CharSequence getQueryHint()
  {
    CharSequence localCharSequence;
    if (mQueryHint != null) {
      localCharSequence = mQueryHint;
    }
    for (;;)
    {
      break;
      if ((mSearchable != null) && (mSearchable.getHintId() != 0)) {
        localCharSequence = getContext().getText(mSearchable.getHintId());
      } else {
        localCharSequence = mDefaultQueryHint;
      }
    }
    return localCharSequence;
  }
  
  int getSuggestionCommitIconResId()
  {
    return mSuggestionCommitIconResId;
  }
  
  int getSuggestionRowLayout()
  {
    return mSuggestionRowLayout;
  }
  
  public CursorAdapter getSuggestionsAdapter()
  {
    return mSuggestionsAdapter;
  }
  
  public boolean isIconfiedByDefault()
  {
    return mIconifiedByDefault;
  }
  
  public boolean isIconified()
  {
    return mIconified;
  }
  
  public boolean isQueryRefinementEnabled()
  {
    return mQueryRefinement;
  }
  
  public boolean isSubmitButtonEnabled()
  {
    return mSubmitButtonEnabled;
  }
  
  public void onActionViewCollapsed()
  {
    setQuery("", false);
    clearFocus();
    updateViewsVisibility(true);
    mSearchSrcTextView.setImeOptions(mCollapsedImeOptions);
    mExpandedInActionView = false;
  }
  
  public void onActionViewExpanded()
  {
    if (mExpandedInActionView) {
      return;
    }
    mExpandedInActionView = true;
    mCollapsedImeOptions = mSearchSrcTextView.getImeOptions();
    mSearchSrcTextView.setImeOptions(mCollapsedImeOptions | 0x2000000);
    mSearchSrcTextView.setText("");
    setIconified(false);
  }
  
  protected void onDetachedFromWindow()
  {
    removeCallbacks(mUpdateDrawableStateRunnable);
    post(mReleaseCursorRunnable);
    super.onDetachedFromWindow();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (mSearchable == null) {
      return false;
    }
    SearchableInfo.ActionKeyInfo localActionKeyInfo = mSearchable.findActionKey(paramInt);
    if ((localActionKeyInfo != null) && (localActionKeyInfo.getQueryActionMsg() != null))
    {
      launchQuerySearch(paramInt, localActionKeyInfo.getQueryActionMsg(), mSearchSrcTextView.getText().toString());
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean)
    {
      getChildBoundsWithinSearchView(mSearchSrcTextView, mSearchSrcTextViewBounds);
      mSearchSrtTextViewBoundsExpanded.set(mSearchSrcTextViewBounds.left, 0, mSearchSrcTextViewBounds.right, paramInt4 - paramInt2);
      if (mTouchDelegate == null)
      {
        mTouchDelegate = new UpdatableTouchDelegate(mSearchSrtTextViewBoundsExpanded, mSearchSrcTextViewBounds, mSearchSrcTextView);
        setTouchDelegate(mTouchDelegate);
      }
      else
      {
        mTouchDelegate.setBounds(mSearchSrtTextViewBoundsExpanded, mSearchSrcTextViewBounds);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (isIconified())
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    if (i != Integer.MIN_VALUE)
    {
      if (i != 0)
      {
        if (i != 1073741824)
        {
          paramInt1 = j;
        }
        else
        {
          paramInt1 = j;
          if (mMaxWidth > 0) {
            paramInt1 = Math.min(mMaxWidth, j);
          }
        }
      }
      else if (mMaxWidth > 0) {
        paramInt1 = mMaxWidth;
      } else {
        paramInt1 = getPreferredWidth();
      }
    }
    else if (mMaxWidth > 0) {
      paramInt1 = Math.min(mMaxWidth, j);
    } else {
      paramInt1 = Math.min(getPreferredWidth(), j);
    }
    j = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    if (j != Integer.MIN_VALUE)
    {
      if (j == 0) {
        paramInt2 = getPreferredHeight();
      }
    }
    else {
      paramInt2 = Math.min(getPreferredHeight(), paramInt2);
    }
    super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824));
  }
  
  void onQueryRefine(CharSequence paramCharSequence)
  {
    setQuery(paramCharSequence);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    updateViewsVisibility(isIconified);
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    isIconified = isIconified();
    return localSavedState;
  }
  
  void onTextFocusChanged()
  {
    updateViewsVisibility(isIconified());
    postUpdateFocusedState();
    if (mSearchSrcTextView.hasFocus()) {
      forceSuggestionQuery();
    }
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    postUpdateFocusedState();
  }
  
  public boolean requestFocus(int paramInt, Rect paramRect)
  {
    if (mClearingFocus) {
      return false;
    }
    if (!isFocusable()) {
      return false;
    }
    if (!isIconified())
    {
      boolean bool = mSearchSrcTextView.requestFocus(paramInt, paramRect);
      if (bool) {
        updateViewsVisibility(false);
      }
      return bool;
    }
    return super.requestFocus(paramInt, paramRect);
  }
  
  public void setAppSearchData(Bundle paramBundle)
  {
    mAppSearchData = paramBundle;
  }
  
  public void setIconified(boolean paramBoolean)
  {
    if (paramBoolean) {
      onCloseClicked();
    } else {
      onSearchClicked();
    }
  }
  
  public void setIconifiedByDefault(boolean paramBoolean)
  {
    if (mIconifiedByDefault == paramBoolean) {
      return;
    }
    mIconifiedByDefault = paramBoolean;
    updateViewsVisibility(paramBoolean);
    updateQueryHint();
  }
  
  public void setImeOptions(int paramInt)
  {
    mSearchSrcTextView.setImeOptions(paramInt);
  }
  
  public void setInputType(int paramInt)
  {
    mSearchSrcTextView.setInputType(paramInt);
  }
  
  public void setMaxWidth(int paramInt)
  {
    mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setOnCloseListener(OnCloseListener paramOnCloseListener)
  {
    mOnCloseListener = paramOnCloseListener;
  }
  
  public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener paramOnFocusChangeListener)
  {
    mOnQueryTextFocusChangeListener = paramOnFocusChangeListener;
  }
  
  public void setOnQueryTextListener(OnQueryTextListener paramOnQueryTextListener)
  {
    mOnQueryChangeListener = paramOnQueryTextListener;
  }
  
  public void setOnSearchClickListener(View.OnClickListener paramOnClickListener)
  {
    mOnSearchClickListener = paramOnClickListener;
  }
  
  public void setOnSuggestionListener(OnSuggestionListener paramOnSuggestionListener)
  {
    mOnSuggestionListener = paramOnSuggestionListener;
  }
  
  public void setQuery(CharSequence paramCharSequence, boolean paramBoolean)
  {
    mSearchSrcTextView.setText(paramCharSequence);
    if (paramCharSequence != null)
    {
      mSearchSrcTextView.setSelection(mSearchSrcTextView.length());
      mUserQuery = paramCharSequence;
    }
    if ((paramBoolean) && (!TextUtils.isEmpty(paramCharSequence))) {
      onSubmitQuery();
    }
  }
  
  public void setQueryHint(CharSequence paramCharSequence)
  {
    mQueryHint = paramCharSequence;
    updateQueryHint();
  }
  
  public void setQueryRefinementEnabled(boolean paramBoolean)
  {
    mQueryRefinement = paramBoolean;
    if ((mSuggestionsAdapter instanceof SuggestionsAdapter))
    {
      SuggestionsAdapter localSuggestionsAdapter = (SuggestionsAdapter)mSuggestionsAdapter;
      int i;
      if (paramBoolean) {
        i = 2;
      } else {
        i = 1;
      }
      localSuggestionsAdapter.setQueryRefinement(i);
    }
  }
  
  public void setSearchableInfo(SearchableInfo paramSearchableInfo)
  {
    mSearchable = paramSearchableInfo;
    if (mSearchable != null)
    {
      updateSearchAutoComplete();
      updateQueryHint();
    }
    mVoiceButtonEnabled = hasVoiceSearch();
    if (mVoiceButtonEnabled) {
      mSearchSrcTextView.setPrivateImeOptions("nm");
    }
    updateViewsVisibility(isIconified());
  }
  
  public void setSubmitButtonEnabled(boolean paramBoolean)
  {
    mSubmitButtonEnabled = paramBoolean;
    updateViewsVisibility(isIconified());
  }
  
  public void setSuggestionsAdapter(CursorAdapter paramCursorAdapter)
  {
    mSuggestionsAdapter = paramCursorAdapter;
    mSearchSrcTextView.setAdapter(mSuggestionsAdapter);
  }
  
  public static abstract interface OnCloseListener
  {
    public abstract boolean onClose();
  }
  
  public static abstract interface OnQueryTextListener
  {
    public abstract boolean onQueryTextChange(String paramString);
    
    public abstract boolean onQueryTextSubmit(String paramString);
  }
  
  public static abstract interface OnSuggestionListener
  {
    public abstract boolean onSuggestionClick(int paramInt);
    
    public abstract boolean onSuggestionSelect(int paramInt);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public SearchView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new SearchView.SavedState(paramAnonymousParcel);
      }
      
      public SearchView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new SearchView.SavedState[paramAnonymousInt];
      }
    };
    boolean isIconified;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      isIconified = ((Boolean)paramParcel.readValue(null)).booleanValue();
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SearchView.SavedState{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" isIconified=");
      localStringBuilder.append(isIconified);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeValue(Boolean.valueOf(isIconified));
    }
  }
  
  public static class SearchAutoComplete
    extends AutoCompleteTextView
  {
    private boolean mHasPendingShowSoftInputRequest;
    final Runnable mRunShowSoftInputIfNecessary = new _..Lambda.SearchView.SearchAutoComplete.qdPU54FiW6QTzCbsg7P4cSs3cJ8(this);
    private SearchView mSearchView;
    private int mThreshold = getThreshold();
    
    public SearchAutoComplete(Context paramContext)
    {
      super();
    }
    
    public SearchAutoComplete(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public SearchAutoComplete(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
      super(paramAttributeSet, paramInt);
    }
    
    public SearchAutoComplete(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
      super(paramAttributeSet, paramInt1, paramInt2);
    }
    
    private int getSearchViewTextMinWidthDp()
    {
      Configuration localConfiguration = getResources().getConfiguration();
      int i = screenWidthDp;
      int j = screenHeightDp;
      int k = orientation;
      if ((i >= 960) && (j >= 720) && (k == 2)) {
        return 256;
      }
      if ((i < 600) && ((i < 640) || (j < 480))) {
        return 160;
      }
      return 192;
    }
    
    private boolean isEmpty()
    {
      boolean bool;
      if (TextUtils.getTrimmedLength(getText()) == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void setImeVisibility(boolean paramBoolean)
    {
      InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService(InputMethodManager.class);
      if (!paramBoolean)
      {
        mHasPendingShowSoftInputRequest = false;
        removeCallbacks(mRunShowSoftInputIfNecessary);
        localInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        return;
      }
      if (localInputMethodManager.isActive(this))
      {
        mHasPendingShowSoftInputRequest = false;
        removeCallbacks(mRunShowSoftInputIfNecessary);
        localInputMethodManager.showSoftInput(this, 0);
        return;
      }
      mHasPendingShowSoftInputRequest = true;
    }
    
    private void showSoftInputIfNecessary()
    {
      if (mHasPendingShowSoftInputRequest)
      {
        ((InputMethodManager)getContext().getSystemService(InputMethodManager.class)).showSoftInput(this, 0);
        mHasPendingShowSoftInputRequest = false;
      }
    }
    
    public boolean enoughToFilter()
    {
      boolean bool;
      if ((mThreshold > 0) && (!super.enoughToFilter())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo)
    {
      paramEditorInfo = super.onCreateInputConnection(paramEditorInfo);
      if (mHasPendingShowSoftInputRequest)
      {
        removeCallbacks(mRunShowSoftInputIfNecessary);
        post(mRunShowSoftInputIfNecessary);
      }
      return paramEditorInfo;
    }
    
    protected void onFinishInflate()
    {
      super.onFinishInflate();
      DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
      setMinWidth((int)TypedValue.applyDimension(1, getSearchViewTextMinWidthDp(), localDisplayMetrics));
    }
    
    protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
    {
      super.onFocusChanged(paramBoolean, paramInt, paramRect);
      mSearchView.onTextFocusChanged();
    }
    
    public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent)
    {
      boolean bool = super.onKeyPreIme(paramInt, paramKeyEvent);
      if ((bool) && (paramInt == 4) && (paramKeyEvent.getAction() == 1)) {
        setImeVisibility(false);
      }
      return bool;
    }
    
    public void onWindowFocusChanged(boolean paramBoolean)
    {
      super.onWindowFocusChanged(paramBoolean);
      if ((paramBoolean) && (mSearchView.hasFocus()) && (getVisibility() == 0))
      {
        mHasPendingShowSoftInputRequest = true;
        if (SearchView.isLandscapeMode(getContext())) {
          ensureImeVisible(true);
        }
      }
    }
    
    public void performCompletion() {}
    
    protected void replaceText(CharSequence paramCharSequence) {}
    
    void setSearchView(SearchView paramSearchView)
    {
      mSearchView = paramSearchView;
    }
    
    public void setThreshold(int paramInt)
    {
      super.setThreshold(paramInt);
      mThreshold = paramInt;
    }
  }
  
  private static class UpdatableTouchDelegate
    extends TouchDelegate
  {
    private final Rect mActualBounds;
    private boolean mDelegateTargeted;
    private final View mDelegateView;
    private final int mSlop;
    private final Rect mSlopBounds;
    private final Rect mTargetBounds;
    
    public UpdatableTouchDelegate(Rect paramRect1, Rect paramRect2, View paramView)
    {
      super(paramView);
      mSlop = ViewConfiguration.get(paramView.getContext()).getScaledTouchSlop();
      mTargetBounds = new Rect();
      mSlopBounds = new Rect();
      mActualBounds = new Rect();
      setBounds(paramRect1, paramRect2);
      mDelegateView = paramView;
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      boolean bool1 = false;
      int k = 1;
      boolean bool2 = false;
      int m;
      switch (paramMotionEvent.getAction())
      {
      default: 
        m = k;
        break;
      case 3: 
        bool1 = mDelegateTargeted;
        mDelegateTargeted = false;
        m = k;
        break;
      case 1: 
      case 2: 
        boolean bool3 = mDelegateTargeted;
        bool1 = bool3;
        m = k;
        if (bool3)
        {
          bool1 = bool3;
          m = k;
          if (!mSlopBounds.contains(i, j))
          {
            m = 0;
            bool1 = bool3;
          }
        }
        break;
      case 0: 
        m = k;
        if (mTargetBounds.contains(i, j))
        {
          mDelegateTargeted = true;
          bool1 = true;
          m = k;
        }
        break;
      }
      if (bool1)
      {
        if ((m != 0) && (!mActualBounds.contains(i, j))) {
          paramMotionEvent.setLocation(mDelegateView.getWidth() / 2, mDelegateView.getHeight() / 2);
        } else {
          paramMotionEvent.setLocation(i - mActualBounds.left, j - mActualBounds.top);
        }
        bool2 = mDelegateView.dispatchTouchEvent(paramMotionEvent);
      }
      return bool2;
    }
    
    public void setBounds(Rect paramRect1, Rect paramRect2)
    {
      mTargetBounds.set(paramRect1);
      mSlopBounds.set(paramRect1);
      mSlopBounds.inset(-mSlop, -mSlop);
      mActualBounds.set(paramRect2);
    }
  }
}
