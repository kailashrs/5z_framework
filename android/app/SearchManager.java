package android.app;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;

public class SearchManager
  implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener
{
  public static final String ACTION_KEY = "action_key";
  public static final String ACTION_MSG = "action_msg";
  public static final String APP_DATA = "app_data";
  public static final String CONTEXT_IS_VOICE = "android.search.CONTEXT_IS_VOICE";
  public static final String CURSOR_EXTRA_KEY_IN_PROGRESS = "in_progress";
  private static final boolean DBG = false;
  public static final String DISABLE_VOICE_SEARCH = "android.search.DISABLE_VOICE_SEARCH";
  public static final String EXTRA_DATA_KEY = "intent_extra_data_key";
  public static final String EXTRA_NEW_SEARCH = "new_search";
  public static final String EXTRA_SELECT_QUERY = "select_query";
  public static final String EXTRA_WEB_SEARCH_PENDINGINTENT = "web_search_pendingintent";
  public static final int FLAG_QUERY_REFINEMENT = 1;
  public static final String INTENT_ACTION_GLOBAL_SEARCH = "android.search.action.GLOBAL_SEARCH";
  public static final String INTENT_ACTION_SEARCHABLES_CHANGED = "android.search.action.SEARCHABLES_CHANGED";
  public static final String INTENT_ACTION_SEARCH_SETTINGS = "android.search.action.SEARCH_SETTINGS";
  public static final String INTENT_ACTION_SEARCH_SETTINGS_CHANGED = "android.search.action.SETTINGS_CHANGED";
  public static final String INTENT_ACTION_WEB_SEARCH_SETTINGS = "android.search.action.WEB_SEARCH_SETTINGS";
  public static final String INTENT_GLOBAL_SEARCH_ACTIVITY_CHANGED = "android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED";
  public static final char MENU_KEY = 's';
  public static final int MENU_KEYCODE = 47;
  public static final String QUERY = "query";
  public static final String SEARCH_MODE = "search_mode";
  public static final String SHORTCUT_MIME_TYPE = "vnd.android.cursor.item/vnd.android.search.suggest";
  public static final String SUGGEST_COLUMN_AUDIO_CHANNEL_CONFIG = "suggest_audio_channel_config";
  public static final String SUGGEST_COLUMN_CONTENT_TYPE = "suggest_content_type";
  public static final String SUGGEST_COLUMN_DURATION = "suggest_duration";
  public static final String SUGGEST_COLUMN_FLAGS = "suggest_flags";
  public static final String SUGGEST_COLUMN_FORMAT = "suggest_format";
  public static final String SUGGEST_COLUMN_ICON_1 = "suggest_icon_1";
  public static final String SUGGEST_COLUMN_ICON_2 = "suggest_icon_2";
  public static final String SUGGEST_COLUMN_INTENT_ACTION = "suggest_intent_action";
  public static final String SUGGEST_COLUMN_INTENT_DATA = "suggest_intent_data";
  public static final String SUGGEST_COLUMN_INTENT_DATA_ID = "suggest_intent_data_id";
  public static final String SUGGEST_COLUMN_INTENT_EXTRA_DATA = "suggest_intent_extra_data";
  public static final String SUGGEST_COLUMN_IS_LIVE = "suggest_is_live";
  public static final String SUGGEST_COLUMN_LAST_ACCESS_HINT = "suggest_last_access_hint";
  public static final String SUGGEST_COLUMN_PRODUCTION_YEAR = "suggest_production_year";
  public static final String SUGGEST_COLUMN_PURCHASE_PRICE = "suggest_purchase_price";
  public static final String SUGGEST_COLUMN_QUERY = "suggest_intent_query";
  public static final String SUGGEST_COLUMN_RATING_SCORE = "suggest_rating_score";
  public static final String SUGGEST_COLUMN_RATING_STYLE = "suggest_rating_style";
  public static final String SUGGEST_COLUMN_RENTAL_PRICE = "suggest_rental_price";
  public static final String SUGGEST_COLUMN_RESULT_CARD_IMAGE = "suggest_result_card_image";
  public static final String SUGGEST_COLUMN_SHORTCUT_ID = "suggest_shortcut_id";
  public static final String SUGGEST_COLUMN_SPINNER_WHILE_REFRESHING = "suggest_spinner_while_refreshing";
  public static final String SUGGEST_COLUMN_TEXT_1 = "suggest_text_1";
  public static final String SUGGEST_COLUMN_TEXT_2 = "suggest_text_2";
  public static final String SUGGEST_COLUMN_TEXT_2_URL = "suggest_text_2_url";
  public static final String SUGGEST_COLUMN_VIDEO_HEIGHT = "suggest_video_height";
  public static final String SUGGEST_COLUMN_VIDEO_WIDTH = "suggest_video_width";
  public static final String SUGGEST_MIME_TYPE = "vnd.android.cursor.dir/vnd.android.search.suggest";
  public static final String SUGGEST_NEVER_MAKE_SHORTCUT = "_-1";
  public static final String SUGGEST_PARAMETER_LIMIT = "limit";
  public static final String SUGGEST_URI_PATH_QUERY = "search_suggest_query";
  public static final String SUGGEST_URI_PATH_SHORTCUT = "search_suggest_shortcut";
  private static final String TAG = "SearchManager";
  public static final String USER_QUERY = "user_query";
  OnCancelListener mCancelListener = null;
  private final Context mContext;
  OnDismissListener mDismissListener = null;
  final Handler mHandler;
  private SearchDialog mSearchDialog;
  private final ISearchManager mService;
  
  SearchManager(Context paramContext, Handler paramHandler)
    throws ServiceManager.ServiceNotFoundException
  {
    mContext = paramContext;
    mHandler = paramHandler;
    mService = ISearchManager.Stub.asInterface(ServiceManager.getServiceOrThrow("search"));
  }
  
  private void ensureSearchDialog()
  {
    if (mSearchDialog == null)
    {
      mSearchDialog = new SearchDialog(mContext, this);
      mSearchDialog.setOnCancelListener(this);
      mSearchDialog.setOnDismissListener(this);
    }
  }
  
  public Intent getAssistIntent(boolean paramBoolean)
  {
    try
    {
      Intent localIntent = new android/content/Intent;
      localIntent.<init>("android.intent.action.ASSIST");
      if (paramBoolean)
      {
        Bundle localBundle = ActivityManager.getService().getAssistContextExtras(0);
        if (localBundle != null) {
          localIntent.replaceExtras(localBundle);
        }
      }
      return localIntent;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ResolveInfo> getGlobalSearchActivities()
  {
    try
    {
      List localList = mService.getGlobalSearchActivities();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ComponentName getGlobalSearchActivity()
  {
    try
    {
      ComponentName localComponentName = mService.getGlobalSearchActivity();
      return localComponentName;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public SearchableInfo getSearchableInfo(ComponentName paramComponentName)
  {
    try
    {
      paramComponentName = mService.getSearchableInfo(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public List<SearchableInfo> getSearchablesInGlobalSearch()
  {
    try
    {
      List localList = mService.getSearchablesInGlobalSearch();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Cursor getSuggestions(SearchableInfo paramSearchableInfo, String paramString)
  {
    return getSuggestions(paramSearchableInfo, paramString, -1);
  }
  
  public Cursor getSuggestions(SearchableInfo paramSearchableInfo, String paramString, int paramInt)
  {
    if (paramSearchableInfo == null) {
      return null;
    }
    Object localObject = paramSearchableInfo.getSuggestAuthority();
    if (localObject == null) {
      return null;
    }
    localObject = new Uri.Builder().scheme("content").authority((String)localObject).query("").fragment("");
    String str = paramSearchableInfo.getSuggestPath();
    if (str != null) {
      ((Uri.Builder)localObject).appendEncodedPath(str);
    }
    ((Uri.Builder)localObject).appendPath("search_suggest_query");
    str = paramSearchableInfo.getSuggestSelection();
    paramSearchableInfo = null;
    if (str != null) {
      paramSearchableInfo = new String[] { paramString };
    }
    for (;;)
    {
      break;
      ((Uri.Builder)localObject).appendPath(paramString);
    }
    if (paramInt > 0) {
      ((Uri.Builder)localObject).appendQueryParameter("limit", String.valueOf(paramInt));
    }
    paramString = ((Uri.Builder)localObject).build();
    return mContext.getContentResolver().query(paramString, null, str, paramSearchableInfo, null);
  }
  
  public ComponentName getWebSearchActivity()
  {
    try
    {
      ComponentName localComponentName = mService.getWebSearchActivity();
      return localComponentName;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isVisible()
  {
    boolean bool;
    if (mSearchDialog == null) {
      bool = false;
    } else {
      bool = mSearchDialog.isShowing();
    }
    return bool;
  }
  
  public void launchAssist(Bundle paramBundle)
  {
    try
    {
      if (mService == null) {
        return;
      }
      mService.launchAssist(paramBundle);
      return;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle.rethrowFromSystemServer();
    }
  }
  
  public boolean launchLegacyAssist(String paramString, int paramInt, Bundle paramBundle)
  {
    try
    {
      if (mService == null) {
        return false;
      }
      boolean bool = mService.launchLegacyAssist(paramString, paramInt, paramBundle);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void onCancel(DialogInterface paramDialogInterface)
  {
    if (mCancelListener != null) {
      mCancelListener.onCancel();
    }
  }
  
  @Deprecated
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    if (mDismissListener != null) {
      mDismissListener.onDismiss();
    }
  }
  
  public void setOnCancelListener(OnCancelListener paramOnCancelListener)
  {
    mCancelListener = paramOnCancelListener;
  }
  
  public void setOnDismissListener(OnDismissListener paramOnDismissListener)
  {
    mDismissListener = paramOnDismissListener;
  }
  
  void startGlobalSearch(String paramString, boolean paramBoolean, Bundle paramBundle, Rect paramRect)
  {
    ComponentName localComponentName = getGlobalSearchActivity();
    if (localComponentName == null)
    {
      Log.w("SearchManager", "No global search activity found.");
      return;
    }
    Intent localIntent = new Intent("android.search.action.GLOBAL_SEARCH");
    localIntent.addFlags(268435456);
    localIntent.setComponent(localComponentName);
    if (paramBundle == null) {
      paramBundle = new Bundle();
    } else {
      paramBundle = new Bundle(paramBundle);
    }
    if (!paramBundle.containsKey("source")) {
      paramBundle.putString("source", mContext.getPackageName());
    }
    localIntent.putExtra("app_data", paramBundle);
    if (!TextUtils.isEmpty(paramString)) {
      localIntent.putExtra("query", paramString);
    }
    if (paramBoolean) {
      localIntent.putExtra("select_query", paramBoolean);
    }
    localIntent.setSourceBounds(paramRect);
    try
    {
      mContext.startActivity(localIntent);
    }
    catch (ActivityNotFoundException paramString)
    {
      paramString = new StringBuilder();
      paramString.append("Global search activity not found: ");
      paramString.append(localComponentName);
      Log.e("SearchManager", paramString.toString());
    }
  }
  
  public void startSearch(String paramString, boolean paramBoolean1, ComponentName paramComponentName, Bundle paramBundle, boolean paramBoolean2)
  {
    startSearch(paramString, paramBoolean1, paramComponentName, paramBundle, paramBoolean2, null);
  }
  
  public void startSearch(String paramString, boolean paramBoolean1, ComponentName paramComponentName, Bundle paramBundle, boolean paramBoolean2, Rect paramRect)
  {
    if (paramBoolean2)
    {
      startGlobalSearch(paramString, paramBoolean1, paramBundle, paramRect);
      return;
    }
    if (((UiModeManager)mContext.getSystemService(UiModeManager.class)).getCurrentModeType() != 4)
    {
      ensureSearchDialog();
      mSearchDialog.show(paramString, paramBoolean1, paramComponentName, paramBundle);
    }
  }
  
  public void stopSearch()
  {
    if (mSearchDialog != null) {
      mSearchDialog.cancel();
    }
  }
  
  public void triggerSearch(String paramString, ComponentName paramComponentName, Bundle paramBundle)
  {
    if ((paramString != null) && (TextUtils.getTrimmedLength(paramString) != 0))
    {
      startSearch(paramString, false, paramComponentName, paramBundle, false);
      mSearchDialog.launchQuerySearch();
      return;
    }
    Log.w("SearchManager", "triggerSearch called with empty query, ignoring.");
  }
  
  public static abstract interface OnCancelListener
  {
    public abstract void onCancel();
  }
  
  public static abstract interface OnDismissListener
  {
    public abstract void onDismiss();
  }
}
