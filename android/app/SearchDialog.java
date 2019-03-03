package android.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.TextView;

public class SearchDialog
  extends Dialog
{
  private static final boolean DBG = false;
  private static final String IME_OPTION_NO_MICROPHONE = "nm";
  private static final String INSTANCE_KEY_APPDATA = "data";
  private static final String INSTANCE_KEY_COMPONENT = "comp";
  private static final String INSTANCE_KEY_USER_QUERY = "uQry";
  private static final String LOG_TAG = "SearchDialog";
  private static final int SEARCH_PLATE_LEFT_PADDING_NON_GLOBAL = 7;
  private Context mActivityContext;
  private ImageView mAppIcon;
  private Bundle mAppSearchData;
  private TextView mBadgeLabel;
  private View mCloseSearch;
  private BroadcastReceiver mConfChangeListener = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
        onConfigurationChanged();
      }
    }
  };
  private ComponentName mLaunchComponent;
  private final SearchView.OnCloseListener mOnCloseListener = new SearchView.OnCloseListener()
  {
    public boolean onClose()
    {
      return SearchDialog.this.onClosePressed();
    }
  };
  private final SearchView.OnQueryTextListener mOnQueryChangeListener = new SearchView.OnQueryTextListener()
  {
    public boolean onQueryTextChange(String paramAnonymousString)
    {
      return false;
    }
    
    public boolean onQueryTextSubmit(String paramAnonymousString)
    {
      dismiss();
      return false;
    }
  };
  private final SearchView.OnSuggestionListener mOnSuggestionSelectionListener = new SearchView.OnSuggestionListener()
  {
    public boolean onSuggestionClick(int paramAnonymousInt)
    {
      dismiss();
      return false;
    }
    
    public boolean onSuggestionSelect(int paramAnonymousInt)
    {
      return false;
    }
  };
  private AutoCompleteTextView mSearchAutoComplete;
  private int mSearchAutoCompleteImeOptions;
  private View mSearchPlate;
  private SearchView mSearchView;
  private SearchableInfo mSearchable;
  private String mUserQuery;
  private final Intent mVoiceAppSearchIntent;
  private final Intent mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH");
  private Drawable mWorkingSpinner;
  
  public SearchDialog(Context paramContext, SearchManager paramSearchManager)
  {
    super(paramContext, resolveDialogTheme(paramContext));
    mVoiceWebSearchIntent.addFlags(268435456);
    mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
    mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
    mVoiceAppSearchIntent.addFlags(268435456);
  }
  
  private void createContentView()
  {
    setContentView(17367297);
    mSearchView = ((SearchView)findViewById(16909329));
    mSearchView.setIconified(false);
    mSearchView.setOnCloseListener(mOnCloseListener);
    mSearchView.setOnQueryTextListener(mOnQueryChangeListener);
    mSearchView.setOnSuggestionListener(mOnSuggestionSelectionListener);
    mSearchView.onActionViewExpanded();
    mCloseSearch = findViewById(16908327);
    mCloseSearch.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        dismiss();
      }
    });
    mBadgeLabel = ((TextView)mSearchView.findViewById(16909320));
    mSearchAutoComplete = ((AutoCompleteTextView)mSearchView.findViewById(16909328));
    mAppIcon = ((ImageView)findViewById(16909319));
    mSearchPlate = mSearchView.findViewById(16909327);
    mWorkingSpinner = getContext().getDrawable(17303624);
    setWorking(false);
    mBadgeLabel.setVisibility(8);
    mSearchAutoCompleteImeOptions = mSearchAutoComplete.getImeOptions();
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
  
  private boolean doShow(String paramString, boolean paramBoolean, ComponentName paramComponentName, Bundle paramBundle)
  {
    if (!show(paramComponentName, paramBundle)) {
      return false;
    }
    setUserQuery(paramString);
    if (paramBoolean) {
      mSearchAutoComplete.selectAll();
    }
    return true;
  }
  
  private boolean isEmpty(AutoCompleteTextView paramAutoCompleteTextView)
  {
    boolean bool;
    if (TextUtils.getTrimmedLength(paramAutoCompleteTextView.getText()) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
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
  
  private boolean isOutOfBounds(View paramView, MotionEvent paramMotionEvent)
  {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    int k = ViewConfiguration.get(mContext).getScaledWindowTouchSlop();
    boolean bool;
    if ((i >= -k) && (j >= -k) && (i <= paramView.getWidth() + k) && (j <= paramView.getHeight() + k)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void launchIntent(Intent paramIntent)
  {
    if (paramIntent == null) {
      return;
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("launching ");
    localStringBuilder1.append(paramIntent);
    Log.d("SearchDialog", localStringBuilder1.toString());
    try
    {
      getContext().startActivity(paramIntent);
      dismiss();
    }
    catch (RuntimeException localRuntimeException)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Failed launch activity: ");
      localStringBuilder2.append(paramIntent);
      Log.e("SearchDialog", localStringBuilder2.toString(), localRuntimeException);
    }
  }
  
  private boolean onClosePressed()
  {
    if (isEmpty(mSearchAutoComplete))
    {
      dismiss();
      return true;
    }
    return false;
  }
  
  static int resolveDialogTheme(Context paramContext)
  {
    TypedValue localTypedValue = new TypedValue();
    paramContext.getTheme().resolveAttribute(17891519, localTypedValue, true);
    return resourceId;
  }
  
  private void setUserQuery(String paramString)
  {
    String str = paramString;
    if (paramString == null) {
      str = "";
    }
    mUserQuery = str;
    mSearchAutoComplete.setText(str);
    mSearchAutoComplete.setSelection(str.length());
  }
  
  private boolean show(ComponentName paramComponentName, Bundle paramBundle)
  {
    mSearchable = ((SearchManager)mContext.getSystemService("search")).getSearchableInfo(paramComponentName);
    if (mSearchable == null) {
      return false;
    }
    mLaunchComponent = paramComponentName;
    mAppSearchData = paramBundle;
    mActivityContext = mSearchable.getActivityContext(getContext());
    if (!isShowing())
    {
      createContentView();
      mSearchView.setSearchableInfo(mSearchable);
      mSearchView.setAppSearchData(mAppSearchData);
      show();
    }
    updateUI();
    return true;
  }
  
  private void updateSearchAppIcon()
  {
    Object localObject = getContext().getPackageManager();
    Drawable localDrawable2;
    try
    {
      Drawable localDrawable1 = ((PackageManager)localObject).getApplicationIcon(getActivityInfomLaunchComponent, 0).applicationInfo);
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      localDrawable2 = ((PackageManager)localObject).getDefaultActivityIcon();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(mLaunchComponent);
      ((StringBuilder)localObject).append(" not found, using generic app icon");
      Log.w("SearchDialog", ((StringBuilder)localObject).toString());
    }
    mAppIcon.setImageDrawable(localDrawable2);
    mAppIcon.setVisibility(0);
    mSearchPlate.setPadding(7, mSearchPlate.getPaddingTop(), mSearchPlate.getPaddingRight(), mSearchPlate.getPaddingBottom());
  }
  
  private void updateSearchAutoComplete()
  {
    mSearchAutoComplete.setDropDownDismissedOnCompletion(false);
    mSearchAutoComplete.setForceIgnoreOutsideTouch(false);
  }
  
  private void updateSearchBadge()
  {
    int i = 8;
    Object localObject1 = null;
    String str = null;
    Object localObject2;
    if (mSearchable.useBadgeIcon())
    {
      localObject2 = mActivityContext.getDrawable(mSearchable.getIconId());
      i = 0;
    }
    else
    {
      localObject2 = localObject1;
      if (mSearchable.useBadgeLabel())
      {
        str = mActivityContext.getResources().getText(mSearchable.getLabelId()).toString();
        i = 0;
        localObject2 = localObject1;
      }
    }
    mBadgeLabel.setCompoundDrawablesWithIntrinsicBounds((Drawable)localObject2, null, null, null);
    mBadgeLabel.setText(str);
    mBadgeLabel.setVisibility(i);
  }
  
  private void updateUI()
  {
    if (mSearchable != null)
    {
      mDecor.setVisibility(0);
      updateSearchAutoComplete();
      updateSearchAppIcon();
      updateSearchBadge();
      int i = mSearchable.getInputType();
      int j = i;
      if ((i & 0xF) == 1)
      {
        i &= 0xFFFEFFFF;
        j = i;
        if (mSearchable.getSuggestAuthority() != null) {
          j = i | 0x10000;
        }
      }
      mSearchAutoComplete.setInputType(j);
      mSearchAutoCompleteImeOptions = mSearchable.getImeOptions();
      mSearchAutoComplete.setImeOptions(mSearchAutoCompleteImeOptions);
      if (mSearchable.getVoiceSearchEnabled()) {
        mSearchAutoComplete.setPrivateImeOptions("nm");
      } else {
        mSearchAutoComplete.setPrivateImeOptions(null);
      }
    }
  }
  
  public void hide()
  {
    if (!isShowing()) {
      return;
    }
    InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService(InputMethodManager.class);
    if (localInputMethodManager != null) {
      localInputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
    super.hide();
  }
  
  public void launchQuerySearch()
  {
    launchQuerySearch(0, null);
  }
  
  protected void launchQuerySearch(int paramInt, String paramString)
  {
    launchIntent(createIntent("android.intent.action.SEARCH", null, null, mSearchAutoComplete.getText().toString(), paramInt, paramString));
  }
  
  public void onBackPressed()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService(InputMethodManager.class);
    if ((localInputMethodManager != null) && (localInputMethodManager.isFullscreenMode()) && (localInputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0))) {
      return;
    }
    cancel();
  }
  
  public void onConfigurationChanged()
  {
    if ((mSearchable != null) && (isShowing()))
    {
      updateSearchAppIcon();
      updateSearchBadge();
      if (isLandscapeMode(getContext())) {
        mSearchAutoComplete.ensureImeVisible(true);
      }
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Window localWindow = getWindow();
    paramBundle = localWindow.getAttributes();
    width = -1;
    height = -1;
    gravity = 55;
    softInputMode = 16;
    localWindow.setAttributes(paramBundle);
    setCanceledOnTouchOutside(true);
  }
  
  public void onRestoreInstanceState(Bundle paramBundle)
  {
    if (paramBundle == null) {
      return;
    }
    ComponentName localComponentName = (ComponentName)paramBundle.getParcelable("comp");
    Bundle localBundle = paramBundle.getBundle("data");
    if (!doShow(paramBundle.getString("uQry"), false, localComponentName, localBundle)) {}
  }
  
  public Bundle onSaveInstanceState()
  {
    if (!isShowing()) {
      return null;
    }
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("comp", mLaunchComponent);
    localBundle.putBundle("data", mAppSearchData);
    localBundle.putString("uQry", mUserQuery);
    return localBundle;
  }
  
  public void onStart()
  {
    super.onStart();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
    getContext().registerReceiver(mConfChangeListener, localIntentFilter);
  }
  
  public void onStop()
  {
    super.onStop();
    getContext().unregisterReceiver(mConfChangeListener);
    mLaunchComponent = null;
    mAppSearchData = null;
    mSearchable = null;
    mUserQuery = null;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((!mSearchAutoComplete.isPopupShowing()) && (isOutOfBounds(mSearchPlate, paramMotionEvent)))
    {
      cancel();
      return true;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setListSelection(int paramInt)
  {
    mSearchAutoComplete.setListSelection(paramInt);
  }
  
  public void setWorking(boolean paramBoolean)
  {
    Drawable localDrawable = mWorkingSpinner;
    int i;
    if (paramBoolean) {
      i = 255;
    } else {
      i = 0;
    }
    localDrawable.setAlpha(i);
    mWorkingSpinner.setVisible(paramBoolean, false);
    mWorkingSpinner.invalidateSelf();
  }
  
  public boolean show(String paramString, boolean paramBoolean, ComponentName paramComponentName, Bundle paramBundle)
  {
    paramBoolean = doShow(paramString, paramBoolean, paramComponentName, paramBundle);
    if (paramBoolean) {
      mSearchAutoComplete.showDropDownAfterLayout();
    }
    return paramBoolean;
  }
  
  public static class SearchBar
    extends LinearLayout
  {
    public SearchBar(Context paramContext)
    {
      super();
    }
    
    public SearchBar(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback, int paramInt)
    {
      if (paramInt != 0) {
        return super.startActionModeForChild(paramView, paramCallback, paramInt);
      }
      return null;
    }
  }
}
