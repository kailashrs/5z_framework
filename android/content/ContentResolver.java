package android.content;

import android.accounts.Account;
import android.annotation.RequiresPermission.Read;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.IActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ParceledListSlice;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.ContentObserver;
import android.database.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.database.IContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.SeempLog;
import com.android.internal.util.MimeIconUtils;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ContentResolver
{
  public static final Intent ACTION_SYNC_CONN_STATUS_CHANGED = new Intent("com.android.sync.SYNC_CONN_STATUS_CHANGED");
  public static final String ANY_CURSOR_ITEM_TYPE = "vnd.android.cursor.item/*";
  public static final String CONTENT_SERVICE_NAME = "content";
  public static final String CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir";
  public static final String CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item";
  private static final boolean ENABLE_CONTENT_SAMPLE = false;
  public static final String EXTRA_HONORED_ARGS = "android.content.extra.HONORED_ARGS";
  public static final String EXTRA_REFRESH_SUPPORTED = "android.content.extra.REFRESH_SUPPORTED";
  public static final String EXTRA_SIZE = "android.content.extra.SIZE";
  public static final String EXTRA_TOTAL_COUNT = "android.content.extra.TOTAL_COUNT";
  public static final int NOTIFY_SKIP_NOTIFY_FOR_DESCENDANTS = 2;
  public static final int NOTIFY_SYNC_TO_NETWORK = 1;
  public static final String QUERY_ARG_LIMIT = "android:query-arg-limit";
  public static final String QUERY_ARG_OFFSET = "android:query-arg-offset";
  public static final String QUERY_ARG_SORT_COLLATION = "android:query-arg-sort-collation";
  public static final String QUERY_ARG_SORT_COLUMNS = "android:query-arg-sort-columns";
  public static final String QUERY_ARG_SORT_DIRECTION = "android:query-arg-sort-direction";
  public static final String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
  public static final String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
  public static final String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";
  public static final int QUERY_SORT_DIRECTION_ASCENDING = 0;
  public static final int QUERY_SORT_DIRECTION_DESCENDING = 1;
  public static final String SCHEME_ANDROID_RESOURCE = "android.resource";
  public static final String SCHEME_CONTENT = "content";
  public static final String SCHEME_FILE = "file";
  private static final int SLOW_THRESHOLD_MILLIS = 500;
  public static final int SYNC_ERROR_AUTHENTICATION = 2;
  public static final int SYNC_ERROR_CONFLICT = 5;
  public static final int SYNC_ERROR_INTERNAL = 8;
  public static final int SYNC_ERROR_IO = 3;
  private static final String[] SYNC_ERROR_NAMES = { "already-in-progress", "authentication-error", "io-error", "parse-error", "conflict", "too-many-deletions", "too-many-retries", "internal-error" };
  public static final int SYNC_ERROR_PARSE = 4;
  public static final int SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS = 1;
  public static final int SYNC_ERROR_TOO_MANY_DELETIONS = 6;
  public static final int SYNC_ERROR_TOO_MANY_RETRIES = 7;
  public static final int SYNC_EXEMPTION_NONE = 0;
  public static final int SYNC_EXEMPTION_PROMOTE_BUCKET = 1;
  public static final int SYNC_EXEMPTION_PROMOTE_BUCKET_WITH_TEMP = 2;
  @Deprecated
  public static final String SYNC_EXTRAS_ACCOUNT = "account";
  public static final String SYNC_EXTRAS_DISALLOW_METERED = "allow_metered";
  public static final String SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS = "discard_deletions";
  public static final String SYNC_EXTRAS_DO_NOT_RETRY = "do_not_retry";
  public static final String SYNC_EXTRAS_EXPECTED_DOWNLOAD = "expected_download";
  public static final String SYNC_EXTRAS_EXPECTED_UPLOAD = "expected_upload";
  public static final String SYNC_EXTRAS_EXPEDITED = "expedited";
  @Deprecated
  public static final String SYNC_EXTRAS_FORCE = "force";
  public static final String SYNC_EXTRAS_IGNORE_BACKOFF = "ignore_backoff";
  public static final String SYNC_EXTRAS_IGNORE_SETTINGS = "ignore_settings";
  public static final String SYNC_EXTRAS_INITIALIZE = "initialize";
  public static final String SYNC_EXTRAS_MANUAL = "force";
  public static final String SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS = "deletions_override";
  public static final String SYNC_EXTRAS_PRIORITY = "sync_priority";
  public static final String SYNC_EXTRAS_REQUIRE_CHARGING = "require_charging";
  public static final String SYNC_EXTRAS_UPLOAD = "upload";
  public static final int SYNC_OBSERVER_TYPE_ACTIVE = 4;
  public static final int SYNC_OBSERVER_TYPE_ALL = Integer.MAX_VALUE;
  public static final int SYNC_OBSERVER_TYPE_PENDING = 2;
  public static final int SYNC_OBSERVER_TYPE_SETTINGS = 1;
  public static final int SYNC_OBSERVER_TYPE_STATUS = 8;
  public static final String SYNC_VIRTUAL_EXTRAS_EXEMPTION_FLAG = "v_exemption";
  private static final String TAG = "ContentResolver";
  private static volatile IContentService sContentService;
  private final int MSG_TWINAPPS_SEND = 4616;
  private final Context mContext;
  final String mPackageName;
  private final Random mRandom = new Random();
  final int mTargetSdkVersion;
  Handler mTwinAppsHandler = new Handler(ActivityThread.currentActivityThread().getLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what == 4616)
      {
        Bundle localBundle1 = paramAnonymousMessage.getData();
        if (localBundle1 == null) {
          return;
        }
        paramAnonymousMessage = new Intent("asus.intent.action.TWINAPPS_SCANFILE");
        paramAnonymousMessage.addFlags(268435456);
        Bundle localBundle2 = new Bundle();
        localBundle2.putInt("fromUserId", localBundle1.getInt("fromUserId"));
        localBundle2.putInt("scanType", localBundle1.getInt("scanType"));
        localBundle2.putString("path", localBundle1.getString("path"));
        localBundle2.putString("mimeType", localBundle1.getString("mimeType"));
        paramAnonymousMessage.putExtras(localBundle2);
        mContext.sendBroadcast(paramAnonymousMessage);
      }
    }
  };
  
  public ContentResolver(Context paramContext)
  {
    if (paramContext == null) {
      paramContext = ActivityThread.currentApplication();
    }
    mContext = paramContext;
    mPackageName = mContext.getOpPackageName();
    mTargetSdkVersion = mContext.getApplicationInfo().targetSdkVersion;
  }
  
  public static void addPeriodicSync(Account paramAccount, String paramString, Bundle paramBundle, long paramLong)
  {
    validateSyncExtrasBundle(paramBundle);
    if (!invalidPeriodicExtras(paramBundle)) {
      try
      {
        getContentService().addPeriodicSync(paramAccount, paramString, paramBundle, paramLong);
        return;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("illegal extras were set");
  }
  
  public static Object addStatusChangeListener(int paramInt, SyncStatusObserver paramSyncStatusObserver)
  {
    if (paramSyncStatusObserver != null) {
      try
      {
        ISyncStatusObserver.Stub local1 = new android/content/ContentResolver$1;
        local1.<init>(paramSyncStatusObserver);
        getContentService().addStatusChangeListener(paramInt, local1);
        return local1;
      }
      catch (RemoteException paramSyncStatusObserver)
      {
        throw paramSyncStatusObserver.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("you passed in a null callback");
  }
  
  public static void cancelSync(Account paramAccount, String paramString)
  {
    try
    {
      getContentService().cancelSync(paramAccount, paramString, null);
      return;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static void cancelSync(SyncRequest paramSyncRequest)
  {
    if (paramSyncRequest != null) {
      try
      {
        getContentService().cancelRequest(paramSyncRequest);
        return;
      }
      catch (RemoteException paramSyncRequest)
      {
        throw paramSyncRequest.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("request cannot be null");
  }
  
  public static void cancelSyncAsUser(Account paramAccount, String paramString, int paramInt)
  {
    try
    {
      getContentService().cancelSyncAsUser(paramAccount, paramString, null, paramInt);
      return;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static Bundle createSqlQueryBundle(String paramString1, String[] paramArrayOfString, String paramString2)
  {
    if ((paramString1 == null) && (paramArrayOfString == null) && (paramString2 == null)) {
      return null;
    }
    Bundle localBundle = new Bundle();
    if (paramString1 != null) {
      localBundle.putString("android:query-arg-sql-selection", paramString1);
    }
    if (paramArrayOfString != null) {
      localBundle.putStringArray("android:query-arg-sql-selection-args", paramArrayOfString);
    }
    if (paramString2 != null) {
      localBundle.putString("android:query-arg-sql-sort-order", paramString2);
    }
    return localBundle;
  }
  
  public static String createSqlSortClause(Bundle paramBundle)
  {
    Object localObject = paramBundle.getStringArray("android:query-arg-sort-columns");
    if ((localObject != null) && (localObject.length != 0))
    {
      String str = TextUtils.join(", ", (Object[])localObject);
      int i = paramBundle.getInt("android:query-arg-sort-collation", 3);
      if (i != 0)
      {
        localObject = str;
        if (i != 1) {}
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(str);
        ((StringBuilder)localObject).append(" COLLATE NOCASE");
        localObject = ((StringBuilder)localObject).toString();
      }
      i = paramBundle.getInt("android:query-arg-sort-direction", Integer.MIN_VALUE);
      paramBundle = (Bundle)localObject;
      if (i != Integer.MIN_VALUE) {
        switch (i)
        {
        default: 
          throw new IllegalArgumentException("Unsupported sort direction value. See ContentResolver documentation for details.");
        case 1: 
          paramBundle = new StringBuilder();
          paramBundle.append((String)localObject);
          paramBundle.append(" DESC");
          paramBundle = paramBundle.toString();
          break;
        case 0: 
          paramBundle = new StringBuilder();
          paramBundle.append((String)localObject);
          paramBundle.append(" ASC");
          paramBundle = paramBundle.toString();
        }
      }
      return paramBundle;
    }
    throw new IllegalArgumentException("Can't create sort clause without columns.");
  }
  
  public static IContentService getContentService()
  {
    if (sContentService != null) {
      return sContentService;
    }
    sContentService = IContentService.Stub.asInterface(ServiceManager.getService("content"));
    return sContentService;
  }
  
  @Deprecated
  public static SyncInfo getCurrentSync()
  {
    try
    {
      Object localObject = getContentService().getCurrentSyncs();
      if (((List)localObject).isEmpty()) {
        return null;
      }
      localObject = (SyncInfo)((List)localObject).get(0);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static List<SyncInfo> getCurrentSyncs()
  {
    try
    {
      List localList = getContentService().getCurrentSyncs();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static List<SyncInfo> getCurrentSyncsAsUser(int paramInt)
  {
    try
    {
      List localList = getContentService().getCurrentSyncsAsUser(paramInt);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static int getIsSyncable(Account paramAccount, String paramString)
  {
    try
    {
      int i = getContentService().getIsSyncable(paramAccount, paramString);
      return i;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static int getIsSyncableAsUser(Account paramAccount, String paramString, int paramInt)
  {
    try
    {
      paramInt = getContentService().getIsSyncableAsUser(paramAccount, paramString, paramInt);
      return paramInt;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static boolean getMasterSyncAutomatically()
  {
    try
    {
      boolean bool = getContentService().getMasterSyncAutomatically();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static boolean getMasterSyncAutomaticallyAsUser(int paramInt)
  {
    try
    {
      boolean bool = getContentService().getMasterSyncAutomaticallyAsUser(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static List<PeriodicSync> getPeriodicSyncs(Account paramAccount, String paramString)
  {
    try
    {
      paramAccount = getContentService().getPeriodicSyncs(paramAccount, paramString, null);
      return paramAccount;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static String[] getSyncAdapterPackagesForAuthorityAsUser(String paramString, int paramInt)
  {
    try
    {
      paramString = getContentService().getSyncAdapterPackagesForAuthorityAsUser(paramString, paramInt);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public static SyncAdapterType[] getSyncAdapterTypes()
  {
    try
    {
      SyncAdapterType[] arrayOfSyncAdapterType = getContentService().getSyncAdapterTypes();
      return arrayOfSyncAdapterType;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static SyncAdapterType[] getSyncAdapterTypesAsUser(int paramInt)
  {
    try
    {
      SyncAdapterType[] arrayOfSyncAdapterType = getContentService().getSyncAdapterTypesAsUser(paramInt);
      return arrayOfSyncAdapterType;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static boolean getSyncAutomatically(Account paramAccount, String paramString)
  {
    try
    {
      boolean bool = getContentService().getSyncAutomatically(paramAccount, paramString);
      return bool;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static boolean getSyncAutomaticallyAsUser(Account paramAccount, String paramString, int paramInt)
  {
    try
    {
      boolean bool = getContentService().getSyncAutomaticallyAsUser(paramAccount, paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static SyncStatusInfo getSyncStatus(Account paramAccount, String paramString)
  {
    try
    {
      paramAccount = getContentService().getSyncStatus(paramAccount, paramString, null);
      return paramAccount;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static SyncStatusInfo getSyncStatusAsUser(Account paramAccount, String paramString, int paramInt)
  {
    try
    {
      paramAccount = getContentService().getSyncStatusAsUser(paramAccount, paramString, null, paramInt);
      return paramAccount;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static boolean invalidPeriodicExtras(Bundle paramBundle)
  {
    return (paramBundle.getBoolean("force", false)) || (paramBundle.getBoolean("do_not_retry", false)) || (paramBundle.getBoolean("ignore_backoff", false)) || (paramBundle.getBoolean("ignore_settings", false)) || (paramBundle.getBoolean("initialize", false)) || (paramBundle.getBoolean("force", false)) || (paramBundle.getBoolean("expedited", false));
  }
  
  public static boolean isSyncActive(Account paramAccount, String paramString)
  {
    if (paramAccount != null)
    {
      if (paramString != null) {
        try
        {
          boolean bool = getContentService().isSyncActive(paramAccount, paramString, null);
          return bool;
        }
        catch (RemoteException paramAccount)
        {
          throw paramAccount.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("authority must not be null");
    }
    throw new IllegalArgumentException("account must not be null");
  }
  
  public static boolean isSyncPending(Account paramAccount, String paramString)
  {
    return isSyncPendingAsUser(paramAccount, paramString, UserHandle.myUserId());
  }
  
  public static boolean isSyncPendingAsUser(Account paramAccount, String paramString, int paramInt)
  {
    try
    {
      boolean bool = getContentService().isSyncPendingAsUser(paramAccount, paramString, null, paramInt);
      return bool;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  private void maybeLogQueryToEventLog(long paramLong, Uri paramUri, String[] paramArrayOfString, Bundle paramBundle)
  {
    if (!Build.isPerformanceDebugging()) {
      return;
    }
    int i = samplePercentForDuration(paramLong);
    if (i < 100) {
      synchronized (mRandom)
      {
        if (mRandom.nextInt(100) >= i) {
          return;
        }
      }
    }
    if (paramBundle == null) {
      paramBundle = Bundle.EMPTY;
    }
    Object localObject2 = new StringBuilder(100);
    if (paramArrayOfString != null) {
      for (int j = 0; j < paramArrayOfString.length; j++)
      {
        if (j != 0) {
          ((StringBuilder)localObject2).append('/');
        }
        ((StringBuilder)localObject2).append(paramArrayOfString[j]);
      }
    }
    paramArrayOfString = AppGlobals.getInitialPackage();
    ??? = paramUri.toString();
    localObject2 = ((StringBuilder)localObject2).toString();
    String str = paramBundle.getString("android:query-arg-sql-selection", "");
    paramBundle = paramBundle.getString("android:query-arg-sql-sort-order", "");
    if (paramArrayOfString != null) {
      paramUri = paramArrayOfString;
    } else {
      paramUri = "";
    }
    EventLog.writeEvent(52002, new Object[] { ???, localObject2, str, paramBundle, Long.valueOf(paramLong), paramUri, Integer.valueOf(i) });
  }
  
  private void maybeLogUpdateToEventLog(long paramLong, Uri paramUri, String paramString1, String paramString2) {}
  
  public static void removePeriodicSync(Account paramAccount, String paramString, Bundle paramBundle)
  {
    validateSyncExtrasBundle(paramBundle);
    try
    {
      getContentService().removePeriodicSync(paramAccount, paramString, paramBundle);
      return;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static void removeStatusChangeListener(Object paramObject)
  {
    if (paramObject != null) {
      try
      {
        getContentService().removeStatusChangeListener((ISyncStatusObserver.Stub)paramObject);
        return;
      }
      catch (RemoteException paramObject)
      {
        throw paramObject.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("you passed in a null handle");
  }
  
  public static void requestSync(Account paramAccount, String paramString, Bundle paramBundle)
  {
    requestSyncAsUser(paramAccount, paramString, UserHandle.myUserId(), paramBundle);
  }
  
  public static void requestSync(SyncRequest paramSyncRequest)
  {
    try
    {
      getContentService().sync(paramSyncRequest);
      return;
    }
    catch (RemoteException paramSyncRequest)
    {
      throw paramSyncRequest.rethrowFromSystemServer();
    }
  }
  
  public static void requestSyncAsUser(Account paramAccount, String paramString, int paramInt, Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      paramAccount = new SyncRequest.Builder().setSyncAdapter(paramAccount, paramString).setExtras(paramBundle).syncOnce().build();
      try
      {
        getContentService().syncAsUser(paramAccount, paramInt);
        return;
      }
      catch (RemoteException paramAccount)
      {
        throw paramAccount.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("Must specify extras.");
  }
  
  private int samplePercentForDuration(long paramLong)
  {
    if ((Build.isPerformanceDebugging()) && (paramLong >= Build.getQueryTimeLimit())) {
      return 100;
    }
    if (paramLong >= 500L) {
      return 100;
    }
    return (int)(100L * paramLong / 500L) + 1;
  }
  
  public static void setIsSyncable(Account paramAccount, String paramString, int paramInt)
  {
    try
    {
      getContentService().setIsSyncable(paramAccount, paramString, paramInt);
      return;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static void setMasterSyncAutomatically(boolean paramBoolean)
  {
    setMasterSyncAutomaticallyAsUser(paramBoolean, UserHandle.myUserId());
  }
  
  public static void setMasterSyncAutomaticallyAsUser(boolean paramBoolean, int paramInt)
  {
    try
    {
      getContentService().setMasterSyncAutomaticallyAsUser(paramBoolean, paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static void setSyncAutomatically(Account paramAccount, String paramString, boolean paramBoolean)
  {
    setSyncAutomaticallyAsUser(paramAccount, paramString, paramBoolean, UserHandle.myUserId());
  }
  
  public static void setSyncAutomaticallyAsUser(Account paramAccount, String paramString, boolean paramBoolean, int paramInt)
  {
    try
    {
      getContentService().setSyncAutomaticallyAsUser(paramAccount, paramString, paramBoolean, paramInt);
      return;
    }
    catch (RemoteException paramAccount)
    {
      throw paramAccount.rethrowFromSystemServer();
    }
  }
  
  public static int syncErrorStringToInt(String paramString)
  {
    int i = 0;
    int j = SYNC_ERROR_NAMES.length;
    while (i < j)
    {
      if (SYNC_ERROR_NAMES[i].equals(paramString)) {
        return i + 1;
      }
      i++;
    }
    if (paramString != null) {
      try
      {
        i = Integer.parseInt(paramString);
        return i;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("error parsing sync error: ");
        localStringBuilder.append(paramString);
        Log.d("ContentResolver", localStringBuilder.toString());
      }
    }
    return 0;
  }
  
  public static String syncErrorToString(int paramInt)
  {
    if ((paramInt >= 1) && (paramInt <= SYNC_ERROR_NAMES.length)) {
      return SYNC_ERROR_NAMES[(paramInt - 1)];
    }
    return String.valueOf(paramInt);
  }
  
  public static void validateSyncExtrasBundle(Bundle paramBundle)
  {
    try
    {
      Object localObject1 = paramBundle.keySet().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        Object localObject2 = paramBundle.get((String)((Iterator)localObject1).next());
        if ((localObject2 != null) && (!(localObject2 instanceof Long)) && (!(localObject2 instanceof Integer)) && (!(localObject2 instanceof Boolean)) && (!(localObject2 instanceof Float)) && (!(localObject2 instanceof Double)) && (!(localObject2 instanceof String)) && (!(localObject2 instanceof Account)))
        {
          paramBundle = new java/lang/IllegalArgumentException;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("unexpected value type: ");
          ((StringBuilder)localObject1).append(localObject2.getClass().getName());
          paramBundle.<init>(((StringBuilder)localObject1).toString());
          throw paramBundle;
        }
      }
      return;
    }
    catch (RuntimeException paramBundle)
    {
      throw new IllegalArgumentException("error unparceling Bundle", paramBundle);
    }
    catch (IllegalArgumentException paramBundle)
    {
      throw paramBundle;
    }
  }
  
  public final ContentProviderClient acquireContentProviderClient(Uri paramUri)
  {
    Preconditions.checkNotNull(paramUri, "uri");
    paramUri = acquireProvider(paramUri);
    if (paramUri != null) {
      return new ContentProviderClient(this, paramUri, true);
    }
    return null;
  }
  
  public final ContentProviderClient acquireContentProviderClient(String paramString)
  {
    Preconditions.checkNotNull(paramString, "name");
    paramString = acquireProvider(paramString);
    if (paramString != null) {
      return new ContentProviderClient(this, paramString, true);
    }
    return null;
  }
  
  protected IContentProvider acquireExistingProvider(Context paramContext, String paramString)
  {
    return acquireProvider(paramContext, paramString);
  }
  
  public final IContentProvider acquireExistingProvider(Uri paramUri)
  {
    if (!"content".equals(paramUri.getScheme())) {
      return null;
    }
    paramUri = paramUri.getAuthority();
    if (paramUri != null) {
      return acquireExistingProvider(mContext, paramUri);
    }
    return null;
  }
  
  protected abstract IContentProvider acquireProvider(Context paramContext, String paramString);
  
  public final IContentProvider acquireProvider(Uri paramUri)
  {
    if (!"content".equals(paramUri.getScheme())) {
      return null;
    }
    paramUri = paramUri.getAuthority();
    if (paramUri != null) {
      return acquireProvider(mContext, paramUri);
    }
    return null;
  }
  
  public final IContentProvider acquireProvider(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return acquireProvider(mContext, paramString);
  }
  
  public final ContentProviderClient acquireUnstableContentProviderClient(Uri paramUri)
  {
    Preconditions.checkNotNull(paramUri, "uri");
    paramUri = acquireUnstableProvider(paramUri);
    if (paramUri != null) {
      return new ContentProviderClient(this, paramUri, false);
    }
    return null;
  }
  
  public final ContentProviderClient acquireUnstableContentProviderClient(String paramString)
  {
    Preconditions.checkNotNull(paramString, "name");
    paramString = acquireUnstableProvider(paramString);
    if (paramString != null) {
      return new ContentProviderClient(this, paramString, false);
    }
    return null;
  }
  
  protected abstract IContentProvider acquireUnstableProvider(Context paramContext, String paramString);
  
  public final IContentProvider acquireUnstableProvider(Uri paramUri)
  {
    if (!"content".equals(paramUri.getScheme())) {
      return null;
    }
    if (paramUri.getAuthority() != null) {
      return acquireUnstableProvider(mContext, paramUri.getAuthority());
    }
    return null;
  }
  
  public final IContentProvider acquireUnstableProvider(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return acquireUnstableProvider(mContext, paramString);
  }
  
  public void appNotRespondingViaProvider(IContentProvider paramIContentProvider)
  {
    throw new UnsupportedOperationException("appNotRespondingViaProvider");
  }
  
  public ContentProviderResult[] applyBatch(String paramString, ArrayList<ContentProviderOperation> paramArrayList)
    throws RemoteException, OperationApplicationException
  {
    Preconditions.checkNotNull(paramString, "authority");
    Preconditions.checkNotNull(paramArrayList, "operations");
    ContentProviderClient localContentProviderClient = acquireContentProviderClient(paramString);
    if (localContentProviderClient != null) {
      try
      {
        paramString = localContentProviderClient.applyBatch(paramArrayList);
        return paramString;
      }
      finally
      {
        localContentProviderClient.release();
      }
    }
    paramArrayList = new StringBuilder();
    paramArrayList.append("Unknown authority ");
    paramArrayList.append(paramString);
    throw new IllegalArgumentException(paramArrayList.toString());
  }
  
  /* Error */
  public final int bulkInsert(@android.annotation.RequiresPermission.Write Uri paramUri, ContentValues[] paramArrayOfContentValues)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 772
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_2
    //   9: ldc_w 774
    //   12: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_0
    //   17: aload_1
    //   18: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnull +64 -> 87
    //   26: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   29: lstore 4
    //   31: aload_3
    //   32: aload_0
    //   33: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   36: aload_1
    //   37: aload_2
    //   38: invokeinterface 785 4 0
    //   43: istore 6
    //   45: aload_0
    //   46: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   49: lload 4
    //   51: lsub
    //   52: aload_1
    //   53: ldc_w 787
    //   56: aconst_null
    //   57: invokespecial 789	android/content/ContentResolver:maybeLogUpdateToEventLog	(JLandroid/net/Uri;Ljava/lang/String;Ljava/lang/String;)V
    //   60: aload_0
    //   61: aload_3
    //   62: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   65: pop
    //   66: iload 6
    //   68: ireturn
    //   69: astore_1
    //   70: aload_0
    //   71: aload_3
    //   72: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   75: pop
    //   76: aload_1
    //   77: athrow
    //   78: astore_1
    //   79: aload_0
    //   80: aload_3
    //   81: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   84: pop
    //   85: iconst_0
    //   86: ireturn
    //   87: new 361	java/lang/StringBuilder
    //   90: dup
    //   91: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   94: astore_2
    //   95: aload_2
    //   96: ldc_w 795
    //   99: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: pop
    //   103: aload_2
    //   104: aload_1
    //   105: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: new 299	java/lang/IllegalArgumentException
    //   112: dup
    //   113: aload_2
    //   114: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   117: invokespecial 302	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	ContentResolver
    //   0	121	1	paramUri	Uri
    //   0	121	2	paramArrayOfContentValues	ContentValues[]
    //   21	60	3	localIContentProvider	IContentProvider
    //   29	21	4	l	long
    //   43	24	6	i	int
    // Exception table:
    //   from	to	target	type
    //   26	60	69	finally
    //   26	60	78	android/os/RemoteException
  }
  
  /* Error */
  public final Bundle call(Uri paramUri, String paramString1, String paramString2, Bundle paramBundle)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 700
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_2
    //   9: ldc_w 803
    //   12: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_0
    //   17: aload_1
    //   18: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   21: astore 5
    //   23: aload 5
    //   25: ifnull +54 -> 79
    //   28: aload 5
    //   30: aload_0
    //   31: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   34: aload_2
    //   35: aload_3
    //   36: aload 4
    //   38: invokeinterface 806 5 0
    //   43: astore_1
    //   44: aload_1
    //   45: iconst_1
    //   46: invokestatic 810	android/os/Bundle:setDefusable	(Landroid/os/Bundle;Z)Landroid/os/Bundle;
    //   49: pop
    //   50: aload_0
    //   51: aload 5
    //   53: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   56: pop
    //   57: aload_1
    //   58: areturn
    //   59: astore_1
    //   60: aload_0
    //   61: aload 5
    //   63: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   66: pop
    //   67: aload_1
    //   68: athrow
    //   69: astore_1
    //   70: aload_0
    //   71: aload 5
    //   73: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   76: pop
    //   77: aconst_null
    //   78: areturn
    //   79: new 361	java/lang/StringBuilder
    //   82: dup
    //   83: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   86: astore_2
    //   87: aload_2
    //   88: ldc_w 812
    //   91: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: pop
    //   95: aload_2
    //   96: aload_1
    //   97: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: new 299	java/lang/IllegalArgumentException
    //   104: dup
    //   105: aload_2
    //   106: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   109: invokespecial 302	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   112: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	113	0	this	ContentResolver
    //   0	113	1	paramUri	Uri
    //   0	113	2	paramString1	String
    //   0	113	3	paramString2	String
    //   0	113	4	paramBundle	Bundle
    //   21	51	5	localIContentProvider	IContentProvider
    // Exception table:
    //   from	to	target	type
    //   28	50	59	finally
    //   28	50	69	android/os/RemoteException
  }
  
  @Deprecated
  public void cancelSync(Uri paramUri)
  {
    if (paramUri != null) {
      paramUri = paramUri.getAuthority();
    } else {
      paramUri = null;
    }
    cancelSync(null, paramUri);
  }
  
  /* Error */
  public final Uri canonicalize(Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 772
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   13: astore_2
    //   14: aload_2
    //   15: ifnonnull +5 -> 20
    //   18: aconst_null
    //   19: areturn
    //   20: aload_2
    //   21: aload_0
    //   22: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   25: aload_1
    //   26: invokeinterface 820 3 0
    //   31: astore_1
    //   32: aload_0
    //   33: aload_2
    //   34: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   37: pop
    //   38: aload_1
    //   39: areturn
    //   40: astore_1
    //   41: aload_0
    //   42: aload_2
    //   43: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   46: pop
    //   47: aload_1
    //   48: athrow
    //   49: astore_1
    //   50: aload_0
    //   51: aload_2
    //   52: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   55: pop
    //   56: aconst_null
    //   57: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	58	0	this	ContentResolver
    //   0	58	1	paramUri	Uri
    //   13	39	2	localIContentProvider	IContentProvider
    // Exception table:
    //   from	to	target	type
    //   20	32	40	finally
    //   20	32	49	android/os/RemoteException
  }
  
  /* Error */
  public final int delete(@android.annotation.RequiresPermission.Write Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 772
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   13: astore 4
    //   15: aload 4
    //   17: ifnull +338 -> 355
    //   20: aload_0
    //   21: getfield 252	android/content/ContentResolver:mContext	Landroid/content/Context;
    //   24: invokevirtual 826	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   27: ldc_w 828
    //   30: invokevirtual 834	android/content/pm/PackageManager:hasSystemFeature	(Ljava/lang/String;)Z
    //   33: ifeq +256 -> 289
    //   36: ldc_w 836
    //   39: aload_1
    //   40: invokevirtual 529	android/net/Uri:toString	()Ljava/lang/String;
    //   43: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   46: ifne +29 -> 75
    //   49: ldc_w 838
    //   52: aload_1
    //   53: invokevirtual 529	android/net/Uri:toString	()Ljava/lang/String;
    //   56: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   59: ifne +16 -> 75
    //   62: ldc_w 840
    //   65: aload_1
    //   66: invokevirtual 529	android/net/Uri:toString	()Ljava/lang/String;
    //   69: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   72: ifeq +217 -> 289
    //   75: aload_0
    //   76: getfield 252	android/content/ContentResolver:mContext	Landroid/content/Context;
    //   79: ldc_w 842
    //   82: invokevirtual 845	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   85: checkcast 847	android/os/UserManager
    //   88: invokevirtual 850	android/os/UserManager:getTwinAppsId	()I
    //   91: istore 5
    //   93: iload 5
    //   95: iconst_m1
    //   96: if_icmpeq +193 -> 289
    //   99: aload_2
    //   100: aload_3
    //   101: aconst_null
    //   102: invokestatic 852	android/content/ContentResolver:createSqlQueryBundle	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/os/Bundle;
    //   105: astore 6
    //   107: aload 4
    //   109: aload_0
    //   110: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   113: aload_1
    //   114: aconst_null
    //   115: aload 6
    //   117: aconst_null
    //   118: invokeinterface 856 6 0
    //   123: astore 7
    //   125: aload 7
    //   127: ifnull +162 -> 289
    //   130: aload 7
    //   132: invokeinterface 861 1 0
    //   137: ifeq +145 -> 282
    //   140: aload 7
    //   142: aload 7
    //   144: ldc_w 863
    //   147: invokeinterface 866 2 0
    //   152: invokeinterface 868 2 0
    //   157: astore 6
    //   159: aload 7
    //   161: aload 7
    //   163: ldc_w 870
    //   166: invokeinterface 866 2 0
    //   171: invokeinterface 868 2 0
    //   176: astore 8
    //   178: invokestatic 490	android/os/UserHandle:myUserId	()I
    //   181: istore 9
    //   183: iload 9
    //   185: ifeq +10 -> 195
    //   188: iload 9
    //   190: iload 5
    //   192: if_icmpne +87 -> 279
    //   195: new 872	android/os/Message
    //   198: astore 10
    //   200: aload 10
    //   202: invokespecial 873	android/os/Message:<init>	()V
    //   205: aload 10
    //   207: sipush 4616
    //   210: putfield 876	android/os/Message:what	I
    //   213: new 332	android/os/Bundle
    //   216: astore 11
    //   218: aload 11
    //   220: invokespecial 333	android/os/Bundle:<init>	()V
    //   223: aload 11
    //   225: ldc_w 878
    //   228: iload 9
    //   230: invokevirtual 882	android/os/Bundle:putInt	(Ljava/lang/String;I)V
    //   233: aload 11
    //   235: ldc_w 884
    //   238: iconst_2
    //   239: invokevirtual 882	android/os/Bundle:putInt	(Ljava/lang/String;I)V
    //   242: aload 11
    //   244: ldc_w 886
    //   247: aload 6
    //   249: invokevirtual 337	android/os/Bundle:putString	(Ljava/lang/String;Ljava/lang/String;)V
    //   252: aload 11
    //   254: ldc_w 888
    //   257: aload 8
    //   259: invokevirtual 337	android/os/Bundle:putString	(Ljava/lang/String;Ljava/lang/String;)V
    //   262: aload 10
    //   264: aload 11
    //   266: invokevirtual 891	android/os/Message:setData	(Landroid/os/Bundle;)V
    //   269: aload_0
    //   270: getfield 246	android/content/ContentResolver:mTwinAppsHandler	Landroid/os/Handler;
    //   273: aload 10
    //   275: invokevirtual 897	android/os/Handler:sendMessage	(Landroid/os/Message;)Z
    //   278: pop
    //   279: goto -149 -> 130
    //   282: aload 7
    //   284: invokeinterface 900 1 0
    //   289: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   292: lstore 12
    //   294: aload 4
    //   296: aload_0
    //   297: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   300: aload_1
    //   301: aload_2
    //   302: aload_3
    //   303: invokeinterface 903 5 0
    //   308: istore 5
    //   310: aload_0
    //   311: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   314: lload 12
    //   316: lsub
    //   317: aload_1
    //   318: ldc_w 904
    //   321: aload_2
    //   322: invokespecial 789	android/content/ContentResolver:maybeLogUpdateToEventLog	(JLandroid/net/Uri;Ljava/lang/String;Ljava/lang/String;)V
    //   325: aload_0
    //   326: aload 4
    //   328: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   331: pop
    //   332: iload 5
    //   334: ireturn
    //   335: astore_1
    //   336: aload_0
    //   337: aload 4
    //   339: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   342: pop
    //   343: aload_1
    //   344: athrow
    //   345: astore_1
    //   346: aload_0
    //   347: aload 4
    //   349: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   352: pop
    //   353: iconst_m1
    //   354: ireturn
    //   355: new 361	java/lang/StringBuilder
    //   358: dup
    //   359: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   362: astore_2
    //   363: aload_2
    //   364: ldc_w 795
    //   367: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   370: pop
    //   371: aload_2
    //   372: aload_1
    //   373: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   376: pop
    //   377: new 299	java/lang/IllegalArgumentException
    //   380: dup
    //   381: aload_2
    //   382: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   385: invokespecial 302	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   388: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	389	0	this	ContentResolver
    //   0	389	1	paramUri	Uri
    //   0	389	2	paramString	String
    //   0	389	3	paramArrayOfString	String[]
    //   13	335	4	localIContentProvider	IContentProvider
    //   91	242	5	i	int
    //   105	143	6	localObject	Object
    //   123	160	7	localCursor	Cursor
    //   176	82	8	str	String
    //   181	48	9	j	int
    //   198	76	10	localMessage	Message
    //   216	49	11	localBundle	Bundle
    //   292	23	12	l	long
    // Exception table:
    //   from	to	target	type
    //   20	75	335	finally
    //   75	93	335	finally
    //   99	125	335	finally
    //   130	183	335	finally
    //   195	279	335	finally
    //   282	289	335	finally
    //   289	325	335	finally
    //   20	75	345	android/os/RemoteException
    //   75	93	345	android/os/RemoteException
    //   99	125	345	android/os/RemoteException
    //   130	183	345	android/os/RemoteException
    //   195	279	345	android/os/RemoteException
    //   282	289	345	android/os/RemoteException
    //   289	325	345	android/os/RemoteException
  }
  
  public Bundle getCache(Uri paramUri)
  {
    try
    {
      paramUri = getContentService().getCache(mContext.getPackageName(), paramUri, mContext.getUserId());
      if (paramUri != null) {
        paramUri.setClassLoader(mContext.getClassLoader());
      }
      return paramUri;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public List<UriPermission> getOutgoingPersistedUriPermissions()
  {
    try
    {
      List localList = ActivityManager.getService().getPersistedUriPermissions(mPackageName, false).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public List<UriPermission> getPersistedUriPermissions()
  {
    try
    {
      List localList = ActivityManager.getService().getPersistedUriPermissions(mPackageName, true).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public OpenResourceIdResult getResourceId(Uri paramUri)
    throws FileNotFoundException
  {
    String str = paramUri.getAuthority();
    if (!TextUtils.isEmpty(str)) {
      try
      {
        Resources localResources = mContext.getPackageManager().getResourcesForApplication(str);
        List localList = paramUri.getPathSegments();
        if (localList != null)
        {
          int i = localList.size();
          if (i == 1)
          {
            try
            {
              i = Integer.parseInt((String)localList.get(0));
            }
            catch (NumberFormatException localNumberFormatException)
            {
              localStringBuilder1 = new StringBuilder();
              localStringBuilder1.append("Single path segment is not a resource ID: ");
              localStringBuilder1.append(paramUri);
              throw new FileNotFoundException(localStringBuilder1.toString());
            }
          }
          else
          {
            if (i != 2) {
              break label200;
            }
            i = localStringBuilder1.getIdentifier((String)localList.get(1), (String)localList.get(0), str);
          }
          if (i != 0)
          {
            paramUri = new OpenResourceIdResult();
            r = localStringBuilder1;
            id = i;
            return paramUri;
          }
          localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append("No resource found for: ");
          localStringBuilder1.append(paramUri);
          throw new FileNotFoundException(localStringBuilder1.toString());
          label200:
          localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append("More than two path segments: ");
          localStringBuilder1.append(paramUri);
          throw new FileNotFoundException(localStringBuilder1.toString());
        }
        StringBuilder localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("No path: ");
        localStringBuilder1.append(paramUri);
        throw new FileNotFoundException(localStringBuilder1.toString());
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("No package found for authority: ");
        localStringBuilder2.append(paramUri);
        throw new FileNotFoundException(localStringBuilder2.toString());
      }
    }
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("No authority: ");
    localStringBuilder2.append(paramUri);
    throw new FileNotFoundException(localStringBuilder2.toString());
  }
  
  /* Error */
  public String[] getStreamTypes(Uri paramUri, String paramString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 772
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_2
    //   9: ldc_w 993
    //   12: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_0
    //   17: aload_1
    //   18: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnonnull +5 -> 28
    //   26: aconst_null
    //   27: areturn
    //   28: aload_3
    //   29: aload_1
    //   30: aload_2
    //   31: invokeinterface 995 3 0
    //   36: astore_1
    //   37: aload_0
    //   38: aload_3
    //   39: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   42: pop
    //   43: aload_1
    //   44: areturn
    //   45: astore_1
    //   46: aload_0
    //   47: aload_3
    //   48: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   51: pop
    //   52: aload_1
    //   53: athrow
    //   54: astore_1
    //   55: aload_0
    //   56: aload_3
    //   57: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   60: pop
    //   61: aconst_null
    //   62: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	63	0	this	ContentResolver
    //   0	63	1	paramUri	Uri
    //   0	63	2	paramString	String
    //   21	36	3	localIContentProvider	IContentProvider
    // Exception table:
    //   from	to	target	type
    //   28	37	45	finally
    //   28	37	54	android/os/RemoteException
  }
  
  public int getTargetSdkVersion()
  {
    return mTargetSdkVersion;
  }
  
  /* Error */
  public final String getType(Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 772
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual 1002	android/content/ContentResolver:acquireExistingProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   13: astore_2
    //   14: aload_2
    //   15: ifnull +114 -> 129
    //   18: aload_2
    //   19: aload_1
    //   20: invokeinterface 1004 2 0
    //   25: astore_3
    //   26: aload_0
    //   27: aload_2
    //   28: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   31: pop
    //   32: aload_3
    //   33: areturn
    //   34: astore_1
    //   35: goto +77 -> 112
    //   38: astore_3
    //   39: new 361	java/lang/StringBuilder
    //   42: astore 4
    //   44: aload 4
    //   46: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   49: aload 4
    //   51: ldc_w 1006
    //   54: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: pop
    //   58: aload 4
    //   60: aload_1
    //   61: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   64: pop
    //   65: aload 4
    //   67: ldc_w 1008
    //   70: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: pop
    //   74: aload 4
    //   76: aload_3
    //   77: invokevirtual 1011	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   80: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload 4
    //   86: ldc_w 1013
    //   89: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: ldc -79
    //   95: aload 4
    //   97: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   100: invokestatic 1016	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   103: pop
    //   104: aload_0
    //   105: aload_2
    //   106: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   109: pop
    //   110: aconst_null
    //   111: areturn
    //   112: aload_0
    //   113: aload_2
    //   114: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   117: pop
    //   118: aload_1
    //   119: athrow
    //   120: astore_1
    //   121: aload_0
    //   122: aload_2
    //   123: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   126: pop
    //   127: aconst_null
    //   128: areturn
    //   129: ldc 38
    //   131: aload_1
    //   132: invokevirtual 728	android/net/Uri:getScheme	()Ljava/lang/String;
    //   135: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   138: ifne +5 -> 143
    //   141: aconst_null
    //   142: areturn
    //   143: invokestatic 929	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   146: aload_1
    //   147: invokestatic 1021	android/content/ContentProvider:getUriWithoutUserId	(Landroid/net/Uri;)Landroid/net/Uri;
    //   150: aload_0
    //   151: aload_1
    //   152: invokevirtual 1025	android/content/ContentResolver:resolveUserId	(Landroid/net/Uri;)I
    //   155: invokeinterface 1029 3 0
    //   160: astore_2
    //   161: aload_2
    //   162: areturn
    //   163: astore_2
    //   164: new 361	java/lang/StringBuilder
    //   167: dup
    //   168: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   171: astore_3
    //   172: aload_3
    //   173: ldc_w 1006
    //   176: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: aload_3
    //   181: aload_1
    //   182: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   185: pop
    //   186: aload_3
    //   187: ldc_w 1008
    //   190: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: aload_3
    //   195: aload_2
    //   196: invokevirtual 1011	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   199: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   202: pop
    //   203: aload_3
    //   204: ldc_w 1013
    //   207: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: ldc -79
    //   213: aload_3
    //   214: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   217: invokestatic 1016	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   220: pop
    //   221: aconst_null
    //   222: areturn
    //   223: astore_1
    //   224: aload_1
    //   225: invokevirtual 297	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   228: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	229	0	this	ContentResolver
    //   0	229	1	paramUri	Uri
    //   13	149	2	localObject	Object
    //   163	33	2	localException1	Exception
    //   25	8	3	str	String
    //   38	39	3	localException2	Exception
    //   171	43	3	localStringBuilder1	StringBuilder
    //   42	54	4	localStringBuilder2	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   18	26	34	finally
    //   39	104	34	finally
    //   18	26	38	java/lang/Exception
    //   18	26	120	android/os/RemoteException
    //   143	161	163	java/lang/Exception
    //   143	161	223	android/os/RemoteException
  }
  
  public Drawable getTypeDrawable(String paramString)
  {
    return MimeIconUtils.loadMimeIcon(mContext, paramString);
  }
  
  public int getUserId()
  {
    return mContext.getUserId();
  }
  
  /* Error */
  public final Uri insert(@android.annotation.RequiresPermission.Write Uri paramUri, ContentValues paramContentValues)
  {
    // Byte code:
    //   0: bipush 37
    //   2: aload_1
    //   3: invokestatic 1045	android/util/SeempLog:record_uri	(ILandroid/net/Uri;)I
    //   6: pop
    //   7: aload_1
    //   8: ldc_w 772
    //   11: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   14: pop
    //   15: aload_0
    //   16: aload_1
    //   17: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   20: astore_3
    //   21: aload_3
    //   22: ifnull +62 -> 84
    //   25: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   28: lstore 4
    //   30: aload_3
    //   31: aload_0
    //   32: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   35: aload_1
    //   36: aload_2
    //   37: invokeinterface 1048 4 0
    //   42: astore_2
    //   43: aload_0
    //   44: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   47: lload 4
    //   49: lsub
    //   50: aload_1
    //   51: ldc_w 1049
    //   54: aconst_null
    //   55: invokespecial 789	android/content/ContentResolver:maybeLogUpdateToEventLog	(JLandroid/net/Uri;Ljava/lang/String;Ljava/lang/String;)V
    //   58: aload_0
    //   59: aload_3
    //   60: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   63: pop
    //   64: aload_2
    //   65: areturn
    //   66: astore_1
    //   67: aload_0
    //   68: aload_3
    //   69: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   72: pop
    //   73: aload_1
    //   74: athrow
    //   75: astore_1
    //   76: aload_0
    //   77: aload_3
    //   78: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   81: pop
    //   82: aconst_null
    //   83: areturn
    //   84: new 361	java/lang/StringBuilder
    //   87: dup
    //   88: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   91: astore_2
    //   92: aload_2
    //   93: ldc_w 795
    //   96: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_2
    //   101: aload_1
    //   102: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: new 299	java/lang/IllegalArgumentException
    //   109: dup
    //   110: aload_2
    //   111: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   114: invokespecial 302	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   117: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	118	0	this	ContentResolver
    //   0	118	1	paramUri	Uri
    //   0	118	2	paramContentValues	ContentValues
    //   20	58	3	localIContentProvider	IContentProvider
    //   28	20	4	l	long
    // Exception table:
    //   from	to	target	type
    //   25	58	66	finally
    //   25	58	75	android/os/RemoteException
  }
  
  public void notifyChange(Uri paramUri, ContentObserver paramContentObserver)
  {
    notifyChange(paramUri, paramContentObserver, true);
  }
  
  public void notifyChange(Uri paramUri, ContentObserver paramContentObserver, int paramInt)
  {
    Preconditions.checkNotNull(paramUri, "uri");
    notifyChange(ContentProvider.getUriWithoutUserId(paramUri), paramContentObserver, paramInt, ContentProvider.getUserIdFromUri(paramUri, mContext.getUserId()));
  }
  
  public void notifyChange(Uri paramUri, ContentObserver paramContentObserver, int paramInt1, int paramInt2)
  {
    try
    {
      IContentService localIContentService = getContentService();
      if (paramContentObserver == null) {}
      for (IContentObserver localIContentObserver = null;; localIContentObserver = paramContentObserver.getContentObserver()) {
        break;
      }
      boolean bool;
      if ((paramContentObserver != null) && (paramContentObserver.deliverSelfNotifications())) {
        bool = true;
      } else {
        bool = false;
      }
      localIContentService.notifyChange(paramUri, localIContentObserver, bool, paramInt1, paramInt2, mTargetSdkVersion);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public void notifyChange(Uri paramUri, ContentObserver paramContentObserver, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramUri, "uri");
    notifyChange(ContentProvider.getUriWithoutUserId(paramUri), paramContentObserver, paramBoolean, ContentProvider.getUserIdFromUri(paramUri, mContext.getUserId()));
  }
  
  public void notifyChange(Uri paramUri, ContentObserver paramContentObserver, boolean paramBoolean, int paramInt)
  {
    try
    {
      IContentService localIContentService = getContentService();
      if (paramContentObserver == null) {}
      for (IContentObserver localIContentObserver = null;; localIContentObserver = paramContentObserver.getContentObserver()) {
        break;
      }
      boolean bool;
      if ((paramContentObserver != null) && (paramContentObserver.deliverSelfNotifications())) {
        bool = true;
      } else {
        bool = false;
      }
      localIContentService.notifyChange(paramUri, localIContentObserver, bool, paramBoolean, paramInt, mTargetSdkVersion);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public final AssetFileDescriptor openAssetFileDescriptor(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    return openAssetFileDescriptor(paramUri, paramString, null);
  }
  
  /* Error */
  public final AssetFileDescriptor openAssetFileDescriptor(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 700
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_2
    //   9: ldc_w 1088
    //   12: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_1
    //   17: invokevirtual 728	android/net/Uri:getScheme	()Ljava/lang/String;
    //   20: astore 4
    //   22: ldc 93
    //   24: aload 4
    //   26: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   29: ifeq +102 -> 131
    //   32: ldc_w 1089
    //   35: aload_2
    //   36: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   39: ifeq +58 -> 97
    //   42: aload_0
    //   43: aload_1
    //   44: invokevirtual 1091	android/content/ContentResolver:getResourceId	(Landroid/net/Uri;)Landroid/content/ContentResolver$OpenResourceIdResult;
    //   47: astore_2
    //   48: aload_2
    //   49: getfield 976	android/content/ContentResolver$OpenResourceIdResult:r	Landroid/content/res/Resources;
    //   52: aload_2
    //   53: getfield 979	android/content/ContentResolver$OpenResourceIdResult:id	I
    //   56: invokevirtual 1095	android/content/res/Resources:openRawResourceFd	(I)Landroid/content/res/AssetFileDescriptor;
    //   59: astore_2
    //   60: aload_2
    //   61: areturn
    //   62: astore_2
    //   63: new 361	java/lang/StringBuilder
    //   66: dup
    //   67: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   70: astore_2
    //   71: aload_2
    //   72: ldc_w 1097
    //   75: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: pop
    //   79: aload_2
    //   80: aload_1
    //   81: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: new 945	java/io/FileNotFoundException
    //   88: dup
    //   89: aload_2
    //   90: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   93: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   96: athrow
    //   97: new 361	java/lang/StringBuilder
    //   100: dup
    //   101: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   104: astore_2
    //   105: aload_2
    //   106: ldc_w 1099
    //   109: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload_2
    //   114: aload_1
    //   115: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: new 945	java/io/FileNotFoundException
    //   122: dup
    //   123: aload_2
    //   124: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   127: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   130: athrow
    //   131: ldc 97
    //   133: aload 4
    //   135: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   138: ifeq +33 -> 171
    //   141: new 1101	android/content/res/AssetFileDescriptor
    //   144: dup
    //   145: new 1103	java/io/File
    //   148: dup
    //   149: aload_1
    //   150: invokevirtual 1106	android/net/Uri:getPath	()Ljava/lang/String;
    //   153: invokespecial 1107	java/io/File:<init>	(Ljava/lang/String;)V
    //   156: aload_2
    //   157: invokestatic 1112	android/os/ParcelFileDescriptor:parseMode	(Ljava/lang/String;)I
    //   160: invokestatic 1116	android/os/ParcelFileDescriptor:open	(Ljava/io/File;I)Landroid/os/ParcelFileDescriptor;
    //   163: lconst_0
    //   164: ldc2_w 1117
    //   167: invokespecial 1121	android/content/res/AssetFileDescriptor:<init>	(Landroid/os/ParcelFileDescriptor;JJ)V
    //   170: areturn
    //   171: ldc_w 1089
    //   174: aload_2
    //   175: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   178: ifeq +229 -> 407
    //   181: aload_0
    //   182: getfield 252	android/content/ContentResolver:mContext	Landroid/content/Context;
    //   185: invokevirtual 826	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   188: ldc_w 828
    //   191: invokevirtual 834	android/content/pm/PackageManager:hasSystemFeature	(Ljava/lang/String;)Z
    //   194: ifeq +202 -> 396
    //   197: aload_0
    //   198: getfield 252	android/content/ContentResolver:mContext	Landroid/content/Context;
    //   201: ldc_w 842
    //   204: invokevirtual 845	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   207: checkcast 847	android/os/UserManager
    //   210: invokevirtual 850	android/os/UserManager:getTwinAppsId	()I
    //   213: istore 5
    //   215: iload 5
    //   217: iconst_m1
    //   218: if_icmpeq +178 -> 396
    //   221: aload_1
    //   222: ifnull +174 -> 396
    //   225: aload_0
    //   226: getfield 252	android/content/ContentResolver:mContext	Landroid/content/Context;
    //   229: invokevirtual 912	android/content/Context:getUserId	()I
    //   232: iload 5
    //   234: if_icmpne +32 -> 266
    //   237: aload_1
    //   238: invokevirtual 1124	android/net/Uri:getEncodedUserInfo	()Ljava/lang/String;
    //   241: ifnull +57 -> 298
    //   244: aload_1
    //   245: invokevirtual 1124	android/net/Uri:getEncodedUserInfo	()Ljava/lang/String;
    //   248: ifnull +18 -> 266
    //   251: aload_1
    //   252: invokevirtual 1124	android/net/Uri:getEncodedUserInfo	()Ljava/lang/String;
    //   255: iload 5
    //   257: invokestatic 648	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   260: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   263: ifeq +35 -> 298
    //   266: aload_0
    //   267: getfield 252	android/content/ContentResolver:mContext	Landroid/content/Context;
    //   270: invokevirtual 912	android/content/Context:getUserId	()I
    //   273: ifne +123 -> 396
    //   276: aload_1
    //   277: invokevirtual 1124	android/net/Uri:getEncodedUserInfo	()Ljava/lang/String;
    //   280: ifnull +116 -> 396
    //   283: aload_1
    //   284: invokevirtual 1124	android/net/Uri:getEncodedUserInfo	()Ljava/lang/String;
    //   287: iload 5
    //   289: invokestatic 648	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   292: invokevirtual 633	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   295: ifeq +101 -> 396
    //   298: aload_1
    //   299: invokevirtual 1106	android/net/Uri:getPath	()Ljava/lang/String;
    //   302: astore_2
    //   303: aload_2
    //   304: ldc_w 1126
    //   307: invokevirtual 1129	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   310: ifeq +86 -> 396
    //   313: new 361	java/lang/StringBuilder
    //   316: dup
    //   317: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   320: astore_3
    //   321: aload_3
    //   322: ldc_w 1131
    //   325: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   328: pop
    //   329: aload_0
    //   330: getfield 252	android/content/ContentResolver:mContext	Landroid/content/Context;
    //   333: invokevirtual 912	android/content/Context:getUserId	()I
    //   336: iload 5
    //   338: if_icmpne +10 -> 348
    //   341: ldc_w 1133
    //   344: astore_1
    //   345: goto +9 -> 354
    //   348: iload 5
    //   350: invokestatic 547	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   353: astore_1
    //   354: aload_3
    //   355: aload_1
    //   356: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   359: pop
    //   360: new 1101	android/content/res/AssetFileDescriptor
    //   363: dup
    //   364: new 1103	java/io/File
    //   367: dup
    //   368: aload_2
    //   369: ldc_w 1126
    //   372: aload_3
    //   373: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   376: invokevirtual 1137	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   379: invokespecial 1107	java/io/File:<init>	(Ljava/lang/String;)V
    //   382: ldc_w 1138
    //   385: invokestatic 1116	android/os/ParcelFileDescriptor:open	(Ljava/io/File;I)Landroid/os/ParcelFileDescriptor;
    //   388: lconst_0
    //   389: ldc2_w 1117
    //   392: invokespecial 1121	android/content/res/AssetFileDescriptor:<init>	(Landroid/os/ParcelFileDescriptor;JJ)V
    //   395: areturn
    //   396: aload_0
    //   397: aload_1
    //   398: ldc_w 1140
    //   401: aconst_null
    //   402: aload_3
    //   403: invokevirtual 1144	android/content/ContentResolver:openTypedAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;
    //   406: areturn
    //   407: aload_0
    //   408: aload_1
    //   409: invokevirtual 737	android/content/ContentResolver:acquireUnstableProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   412: astore 6
    //   414: aload 6
    //   416: ifnull +921 -> 1337
    //   419: aconst_null
    //   420: astore 7
    //   422: aconst_null
    //   423: astore 8
    //   425: aconst_null
    //   426: astore 9
    //   428: aconst_null
    //   429: astore 10
    //   431: aconst_null
    //   432: astore 11
    //   434: aload_3
    //   435: ifnull +109 -> 544
    //   438: aload 6
    //   440: astore 12
    //   442: aload 10
    //   444: astore 4
    //   446: aload 6
    //   448: astore 13
    //   450: aload 7
    //   452: astore 14
    //   454: aload 6
    //   456: astore 15
    //   458: aload 8
    //   460: astore 16
    //   462: aload_3
    //   463: invokevirtual 1149	android/os/CancellationSignal:throwIfCanceled	()V
    //   466: aload 6
    //   468: astore 12
    //   470: aload 10
    //   472: astore 4
    //   474: aload 6
    //   476: astore 13
    //   478: aload 7
    //   480: astore 14
    //   482: aload 6
    //   484: astore 15
    //   486: aload 8
    //   488: astore 16
    //   490: aload 6
    //   492: invokeinterface 1153 1 0
    //   497: astore 11
    //   499: aload 6
    //   501: astore 12
    //   503: aload 10
    //   505: astore 4
    //   507: aload 6
    //   509: astore 13
    //   511: aload 7
    //   513: astore 14
    //   515: aload 6
    //   517: astore 15
    //   519: aload 8
    //   521: astore 16
    //   523: aload_3
    //   524: aload 11
    //   526: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   529: goto +15 -> 544
    //   532: astore_1
    //   533: goto +769 -> 1302
    //   536: astore_1
    //   537: goto +660 -> 1197
    //   540: astore_2
    //   541: goto +666 -> 1207
    //   544: aload 6
    //   546: astore 12
    //   548: aload 10
    //   550: astore 4
    //   552: aload 6
    //   554: astore 13
    //   556: aload 7
    //   558: astore 14
    //   560: aload 6
    //   562: astore 15
    //   564: aload 8
    //   566: astore 16
    //   568: aload 6
    //   570: aload_0
    //   571: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   574: aload_1
    //   575: aload_2
    //   576: aload 11
    //   578: invokeinterface 1161 5 0
    //   583: astore 17
    //   585: aload 17
    //   587: ifnonnull +36 -> 623
    //   590: aload_3
    //   591: ifnull +8 -> 599
    //   594: aload_3
    //   595: aconst_null
    //   596: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   599: iconst_0
    //   600: ifeq +9 -> 609
    //   603: aload_0
    //   604: aconst_null
    //   605: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   608: pop
    //   609: aload 6
    //   611: ifnull +10 -> 621
    //   614: aload_0
    //   615: aload 6
    //   617: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   620: pop
    //   621: aconst_null
    //   622: areturn
    //   623: aload 17
    //   625: astore 11
    //   627: aload 9
    //   629: astore 16
    //   631: goto +157 -> 788
    //   634: astore 4
    //   636: aload 6
    //   638: astore 12
    //   640: aload 10
    //   642: astore 4
    //   644: aload 6
    //   646: astore 13
    //   648: aload 7
    //   650: astore 14
    //   652: aload 6
    //   654: astore 15
    //   656: aload 8
    //   658: astore 16
    //   660: aload_0
    //   661: aload 6
    //   663: invokevirtual 1167	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   666: aload 6
    //   668: astore 12
    //   670: aload 10
    //   672: astore 4
    //   674: aload 6
    //   676: astore 13
    //   678: aload 7
    //   680: astore 14
    //   682: aload 6
    //   684: astore 15
    //   686: aload 8
    //   688: astore 16
    //   690: aload_0
    //   691: aload_1
    //   692: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   695: astore 10
    //   697: aload 10
    //   699: ifnull +291 -> 990
    //   702: aload 6
    //   704: astore 12
    //   706: aload 10
    //   708: astore 4
    //   710: aload 6
    //   712: astore 13
    //   714: aload 10
    //   716: astore 14
    //   718: aload 6
    //   720: astore 15
    //   722: aload 10
    //   724: astore 16
    //   726: aload 10
    //   728: aload_0
    //   729: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   732: aload_1
    //   733: aload_2
    //   734: aload 11
    //   736: invokeinterface 1161 5 0
    //   741: astore_2
    //   742: aload 10
    //   744: astore 16
    //   746: aload_2
    //   747: astore 11
    //   749: aload_2
    //   750: ifnonnull +38 -> 788
    //   753: aload_3
    //   754: ifnull +8 -> 762
    //   757: aload_3
    //   758: aconst_null
    //   759: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   762: aload 10
    //   764: ifnull +10 -> 774
    //   767: aload_0
    //   768: aload 10
    //   770: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   773: pop
    //   774: aload 6
    //   776: ifnull +10 -> 786
    //   779: aload_0
    //   780: aload 6
    //   782: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   785: pop
    //   786: aconst_null
    //   787: areturn
    //   788: aload 16
    //   790: astore_2
    //   791: aload 16
    //   793: ifnonnull +29 -> 822
    //   796: aload 6
    //   798: astore 12
    //   800: aload 16
    //   802: astore 4
    //   804: aload 6
    //   806: astore 13
    //   808: aload 16
    //   810: astore 14
    //   812: aload 6
    //   814: astore 15
    //   816: aload_0
    //   817: aload_1
    //   818: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   821: astore_2
    //   822: aload 6
    //   824: astore 12
    //   826: aload_2
    //   827: astore 4
    //   829: aload 6
    //   831: astore 13
    //   833: aload_2
    //   834: astore 14
    //   836: aload 6
    //   838: astore 15
    //   840: aload_2
    //   841: astore 16
    //   843: aload_0
    //   844: aload 6
    //   846: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   849: pop
    //   850: aconst_null
    //   851: astore 6
    //   853: aconst_null
    //   854: astore 7
    //   856: aconst_null
    //   857: astore 10
    //   859: aload 10
    //   861: astore 12
    //   863: aload_2
    //   864: astore 4
    //   866: aload 6
    //   868: astore 13
    //   870: aload_2
    //   871: astore 14
    //   873: aload 7
    //   875: astore 15
    //   877: aload_2
    //   878: astore 16
    //   880: new 19	android/content/ContentResolver$ParcelFileDescriptorInner
    //   883: astore 8
    //   885: aload 10
    //   887: astore 12
    //   889: aload_2
    //   890: astore 4
    //   892: aload 6
    //   894: astore 13
    //   896: aload_2
    //   897: astore 14
    //   899: aload 7
    //   901: astore 15
    //   903: aload_2
    //   904: astore 16
    //   906: aload 8
    //   908: aload_0
    //   909: aload 11
    //   911: invokevirtual 1171	android/content/res/AssetFileDescriptor:getParcelFileDescriptor	()Landroid/os/ParcelFileDescriptor;
    //   914: aload_2
    //   915: invokespecial 1174	android/content/ContentResolver$ParcelFileDescriptorInner:<init>	(Landroid/content/ContentResolver;Landroid/os/ParcelFileDescriptor;Landroid/content/IContentProvider;)V
    //   918: aconst_null
    //   919: astore 14
    //   921: aconst_null
    //   922: astore 16
    //   924: aconst_null
    //   925: astore 4
    //   927: aload 10
    //   929: astore 12
    //   931: aload 6
    //   933: astore 13
    //   935: aload 7
    //   937: astore 15
    //   939: new 1101	android/content/res/AssetFileDescriptor
    //   942: dup
    //   943: aload 8
    //   945: aload 11
    //   947: invokevirtual 1177	android/content/res/AssetFileDescriptor:getStartOffset	()J
    //   950: aload 11
    //   952: invokevirtual 1180	android/content/res/AssetFileDescriptor:getDeclaredLength	()J
    //   955: invokespecial 1121	android/content/res/AssetFileDescriptor:<init>	(Landroid/os/ParcelFileDescriptor;JJ)V
    //   958: astore_2
    //   959: aload_3
    //   960: ifnull +8 -> 968
    //   963: aload_3
    //   964: aconst_null
    //   965: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   968: iconst_0
    //   969: ifeq +9 -> 978
    //   972: aload_0
    //   973: aconst_null
    //   974: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   977: pop
    //   978: iconst_0
    //   979: ifeq +9 -> 988
    //   982: aload_0
    //   983: aconst_null
    //   984: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   987: pop
    //   988: aload_2
    //   989: areturn
    //   990: aload 6
    //   992: astore 12
    //   994: aload 10
    //   996: astore 4
    //   998: aload 6
    //   1000: astore 13
    //   1002: aload 10
    //   1004: astore 14
    //   1006: aload 6
    //   1008: astore 15
    //   1010: aload 10
    //   1012: astore 16
    //   1014: new 945	java/io/FileNotFoundException
    //   1017: astore 11
    //   1019: aload 6
    //   1021: astore 12
    //   1023: aload 10
    //   1025: astore 4
    //   1027: aload 6
    //   1029: astore 13
    //   1031: aload 10
    //   1033: astore 14
    //   1035: aload 6
    //   1037: astore 15
    //   1039: aload 10
    //   1041: astore 16
    //   1043: new 361	java/lang/StringBuilder
    //   1046: astore_2
    //   1047: aload 6
    //   1049: astore 12
    //   1051: aload 10
    //   1053: astore 4
    //   1055: aload 6
    //   1057: astore 13
    //   1059: aload 10
    //   1061: astore 14
    //   1063: aload 6
    //   1065: astore 15
    //   1067: aload 10
    //   1069: astore 16
    //   1071: aload_2
    //   1072: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   1075: aload 6
    //   1077: astore 12
    //   1079: aload 10
    //   1081: astore 4
    //   1083: aload 6
    //   1085: astore 13
    //   1087: aload 10
    //   1089: astore 14
    //   1091: aload 6
    //   1093: astore 15
    //   1095: aload 10
    //   1097: astore 16
    //   1099: aload_2
    //   1100: ldc_w 1182
    //   1103: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1106: pop
    //   1107: aload 6
    //   1109: astore 12
    //   1111: aload 10
    //   1113: astore 4
    //   1115: aload 6
    //   1117: astore 13
    //   1119: aload 10
    //   1121: astore 14
    //   1123: aload 6
    //   1125: astore 15
    //   1127: aload 10
    //   1129: astore 16
    //   1131: aload_2
    //   1132: aload_1
    //   1133: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1136: pop
    //   1137: aload 6
    //   1139: astore 12
    //   1141: aload 10
    //   1143: astore 4
    //   1145: aload 6
    //   1147: astore 13
    //   1149: aload 10
    //   1151: astore 14
    //   1153: aload 6
    //   1155: astore 15
    //   1157: aload 10
    //   1159: astore 16
    //   1161: aload 11
    //   1163: aload_2
    //   1164: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1167: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   1170: aload 6
    //   1172: astore 12
    //   1174: aload 10
    //   1176: astore 4
    //   1178: aload 6
    //   1180: astore 13
    //   1182: aload 10
    //   1184: astore 14
    //   1186: aload 6
    //   1188: astore 15
    //   1190: aload 10
    //   1192: astore 16
    //   1194: aload 11
    //   1196: athrow
    //   1197: aload 13
    //   1199: astore 12
    //   1201: aload 14
    //   1203: astore 4
    //   1205: aload_1
    //   1206: athrow
    //   1207: aload 15
    //   1209: astore 12
    //   1211: aload 16
    //   1213: astore 4
    //   1215: new 945	java/io/FileNotFoundException
    //   1218: astore 14
    //   1220: aload 15
    //   1222: astore 12
    //   1224: aload 16
    //   1226: astore 4
    //   1228: new 361	java/lang/StringBuilder
    //   1231: astore_2
    //   1232: aload 15
    //   1234: astore 12
    //   1236: aload 16
    //   1238: astore 4
    //   1240: aload_2
    //   1241: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   1244: aload 15
    //   1246: astore 12
    //   1248: aload 16
    //   1250: astore 4
    //   1252: aload_2
    //   1253: ldc_w 1184
    //   1256: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1259: pop
    //   1260: aload 15
    //   1262: astore 12
    //   1264: aload 16
    //   1266: astore 4
    //   1268: aload_2
    //   1269: aload_1
    //   1270: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1273: pop
    //   1274: aload 15
    //   1276: astore 12
    //   1278: aload 16
    //   1280: astore 4
    //   1282: aload 14
    //   1284: aload_2
    //   1285: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1288: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   1291: aload 15
    //   1293: astore 12
    //   1295: aload 16
    //   1297: astore 4
    //   1299: aload 14
    //   1301: athrow
    //   1302: aload_3
    //   1303: ifnull +8 -> 1311
    //   1306: aload_3
    //   1307: aconst_null
    //   1308: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   1311: aload 4
    //   1313: ifnull +10 -> 1323
    //   1316: aload_0
    //   1317: aload 4
    //   1319: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   1322: pop
    //   1323: aload 12
    //   1325: ifnull +10 -> 1335
    //   1328: aload_0
    //   1329: aload 12
    //   1331: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   1334: pop
    //   1335: aload_1
    //   1336: athrow
    //   1337: new 361	java/lang/StringBuilder
    //   1340: dup
    //   1341: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   1344: astore_2
    //   1345: aload_2
    //   1346: ldc_w 1182
    //   1349: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1352: pop
    //   1353: aload_2
    //   1354: aload_1
    //   1355: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1358: pop
    //   1359: new 945	java/io/FileNotFoundException
    //   1362: dup
    //   1363: aload_2
    //   1364: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1367: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   1370: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1371	0	this	ContentResolver
    //   0	1371	1	paramUri	Uri
    //   0	1371	2	paramString	String
    //   0	1371	3	paramCancellationSignal	CancellationSignal
    //   20	531	4	localObject1	Object
    //   634	1	4	localDeadObjectException	android.os.DeadObjectException
    //   642	676	4	localObject2	Object
    //   213	136	5	i	int
    //   412	775	6	localIContentProvider1	IContentProvider
    //   420	516	7	localObject3	Object
    //   423	521	8	localParcelFileDescriptorInner	ParcelFileDescriptorInner
    //   426	202	9	localObject4	Object
    //   429	762	10	localIContentProvider2	IContentProvider
    //   432	763	11	localObject5	Object
    //   440	890	12	localObject6	Object
    //   448	750	13	localIContentProvider3	IContentProvider
    //   452	848	14	localObject7	Object
    //   456	836	15	localObject8	Object
    //   460	836	16	localObject9	Object
    //   583	41	17	localAssetFileDescriptor	AssetFileDescriptor
    // Exception table:
    //   from	to	target	type
    //   48	60	62	android/content/res/Resources$NotFoundException
    //   462	466	532	finally
    //   490	499	532	finally
    //   523	529	532	finally
    //   568	585	532	finally
    //   660	666	532	finally
    //   690	697	532	finally
    //   726	742	532	finally
    //   816	822	532	finally
    //   843	850	532	finally
    //   880	885	532	finally
    //   906	918	532	finally
    //   939	959	532	finally
    //   1014	1019	532	finally
    //   1043	1047	532	finally
    //   1071	1075	532	finally
    //   1099	1107	532	finally
    //   1131	1137	532	finally
    //   1161	1170	532	finally
    //   1194	1197	532	finally
    //   1205	1207	532	finally
    //   1215	1220	532	finally
    //   1228	1232	532	finally
    //   1240	1244	532	finally
    //   1252	1260	532	finally
    //   1268	1274	532	finally
    //   1282	1291	532	finally
    //   1299	1302	532	finally
    //   462	466	536	java/io/FileNotFoundException
    //   490	499	536	java/io/FileNotFoundException
    //   523	529	536	java/io/FileNotFoundException
    //   568	585	536	java/io/FileNotFoundException
    //   660	666	536	java/io/FileNotFoundException
    //   690	697	536	java/io/FileNotFoundException
    //   726	742	536	java/io/FileNotFoundException
    //   816	822	536	java/io/FileNotFoundException
    //   843	850	536	java/io/FileNotFoundException
    //   880	885	536	java/io/FileNotFoundException
    //   906	918	536	java/io/FileNotFoundException
    //   939	959	536	java/io/FileNotFoundException
    //   1014	1019	536	java/io/FileNotFoundException
    //   1043	1047	536	java/io/FileNotFoundException
    //   1071	1075	536	java/io/FileNotFoundException
    //   1099	1107	536	java/io/FileNotFoundException
    //   1131	1137	536	java/io/FileNotFoundException
    //   1161	1170	536	java/io/FileNotFoundException
    //   1194	1197	536	java/io/FileNotFoundException
    //   462	466	540	android/os/RemoteException
    //   490	499	540	android/os/RemoteException
    //   523	529	540	android/os/RemoteException
    //   568	585	540	android/os/RemoteException
    //   660	666	540	android/os/RemoteException
    //   690	697	540	android/os/RemoteException
    //   726	742	540	android/os/RemoteException
    //   816	822	540	android/os/RemoteException
    //   843	850	540	android/os/RemoteException
    //   880	885	540	android/os/RemoteException
    //   906	918	540	android/os/RemoteException
    //   939	959	540	android/os/RemoteException
    //   1014	1019	540	android/os/RemoteException
    //   1043	1047	540	android/os/RemoteException
    //   1071	1075	540	android/os/RemoteException
    //   1099	1107	540	android/os/RemoteException
    //   1131	1137	540	android/os/RemoteException
    //   1161	1170	540	android/os/RemoteException
    //   1194	1197	540	android/os/RemoteException
    //   568	585	634	android/os/DeadObjectException
  }
  
  public final ParcelFileDescriptor openFileDescriptor(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    return openFileDescriptor(paramUri, paramString, null);
  }
  
  public final ParcelFileDescriptor openFileDescriptor(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    paramUri = openAssetFileDescriptor(paramUri, paramString, paramCancellationSignal);
    if (paramUri == null) {
      return null;
    }
    if (paramUri.getDeclaredLength() < 0L) {
      return paramUri.getParcelFileDescriptor();
    }
    try
    {
      paramUri.close();
    }
    catch (IOException paramUri) {}
    throw new FileNotFoundException("Not a whole file");
  }
  
  public final InputStream openInputStream(Uri paramUri)
    throws FileNotFoundException
  {
    Preconditions.checkNotNull(paramUri, "uri");
    Object localObject = paramUri.getScheme();
    if ("android.resource".equals(localObject))
    {
      localObject = getResourceId(paramUri);
      try
      {
        localObject = r.openRawResource(id);
        return localObject;
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Resource does not exist: ");
        localStringBuilder.append(paramUri);
        throw new FileNotFoundException(localStringBuilder.toString());
      }
    }
    if ("file".equals(localStringBuilder)) {
      return new FileInputStream(paramUri.getPath());
    }
    StringBuilder localStringBuilder = null;
    AssetFileDescriptor localAssetFileDescriptor = openAssetFileDescriptor(paramUri, "r", null);
    paramUri = localStringBuilder;
    if (localAssetFileDescriptor != null) {
      try
      {
        paramUri = localAssetFileDescriptor.createInputStream();
      }
      catch (IOException paramUri)
      {
        throw new FileNotFoundException("Unable to create stream");
      }
    }
    return paramUri;
  }
  
  public final OutputStream openOutputStream(Uri paramUri)
    throws FileNotFoundException
  {
    return openOutputStream(paramUri, "w");
  }
  
  public final OutputStream openOutputStream(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    Object localObject = null;
    paramString = openAssetFileDescriptor(paramUri, paramString, null);
    paramUri = localObject;
    if (paramString != null) {
      try
      {
        paramUri = paramString.createOutputStream();
      }
      catch (IOException paramUri)
      {
        throw new FileNotFoundException("Unable to create stream");
      }
    }
    return paramUri;
  }
  
  public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri paramUri, String paramString, Bundle paramBundle)
    throws FileNotFoundException
  {
    return openTypedAssetFileDescriptor(paramUri, paramString, paramBundle, null);
  }
  
  /* Error */
  public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri paramUri, String paramString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 700
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_2
    //   9: ldc_w 888
    //   12: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_0
    //   17: aload_1
    //   18: invokevirtual 737	android/content/ContentResolver:acquireUnstableProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   21: astore 5
    //   23: aload 5
    //   25: ifnull +921 -> 946
    //   28: aconst_null
    //   29: astore 6
    //   31: aconst_null
    //   32: astore 7
    //   34: aconst_null
    //   35: astore 8
    //   37: aconst_null
    //   38: astore 9
    //   40: aconst_null
    //   41: astore 10
    //   43: aload 4
    //   45: ifnull +111 -> 156
    //   48: aload 5
    //   50: astore 11
    //   52: aload 9
    //   54: astore 12
    //   56: aload 5
    //   58: astore 13
    //   60: aload 6
    //   62: astore 14
    //   64: aload 5
    //   66: astore 15
    //   68: aload 7
    //   70: astore 16
    //   72: aload 4
    //   74: invokevirtual 1149	android/os/CancellationSignal:throwIfCanceled	()V
    //   77: aload 5
    //   79: astore 11
    //   81: aload 9
    //   83: astore 12
    //   85: aload 5
    //   87: astore 13
    //   89: aload 6
    //   91: astore 14
    //   93: aload 5
    //   95: astore 15
    //   97: aload 7
    //   99: astore 16
    //   101: aload 5
    //   103: invokeinterface 1153 1 0
    //   108: astore 10
    //   110: aload 5
    //   112: astore 11
    //   114: aload 9
    //   116: astore 12
    //   118: aload 5
    //   120: astore 13
    //   122: aload 6
    //   124: astore 14
    //   126: aload 5
    //   128: astore 15
    //   130: aload 7
    //   132: astore 16
    //   134: aload 4
    //   136: aload 10
    //   138: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   141: goto +15 -> 156
    //   144: astore_1
    //   145: goto +764 -> 909
    //   148: astore_1
    //   149: goto +658 -> 807
    //   152: astore_2
    //   153: goto +664 -> 817
    //   156: aload 5
    //   158: astore 11
    //   160: aload 9
    //   162: astore 12
    //   164: aload 5
    //   166: astore 13
    //   168: aload 6
    //   170: astore 14
    //   172: aload 5
    //   174: astore 15
    //   176: aload 7
    //   178: astore 16
    //   180: aload 5
    //   182: aload_0
    //   183: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   186: aload_1
    //   187: aload_2
    //   188: aload_3
    //   189: aload 10
    //   191: invokeinterface 1224 6 0
    //   196: astore 17
    //   198: aload 17
    //   200: ifnonnull +38 -> 238
    //   203: aload 4
    //   205: ifnull +9 -> 214
    //   208: aload 4
    //   210: aconst_null
    //   211: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   214: iconst_0
    //   215: ifeq +9 -> 224
    //   218: aload_0
    //   219: aconst_null
    //   220: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   223: pop
    //   224: aload 5
    //   226: ifnull +10 -> 236
    //   229: aload_0
    //   230: aload 5
    //   232: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   235: pop
    //   236: aconst_null
    //   237: areturn
    //   238: aload 17
    //   240: astore_3
    //   241: aload 8
    //   243: astore 16
    //   245: goto +157 -> 402
    //   248: astore 12
    //   250: aload 5
    //   252: astore 11
    //   254: aload 9
    //   256: astore 12
    //   258: aload 5
    //   260: astore 13
    //   262: aload 6
    //   264: astore 14
    //   266: aload 5
    //   268: astore 15
    //   270: aload 7
    //   272: astore 16
    //   274: aload_0
    //   275: aload 5
    //   277: invokevirtual 1167	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   280: aload 5
    //   282: astore 11
    //   284: aload 9
    //   286: astore 12
    //   288: aload 5
    //   290: astore 13
    //   292: aload 6
    //   294: astore 14
    //   296: aload 5
    //   298: astore 15
    //   300: aload 7
    //   302: astore 16
    //   304: aload_0
    //   305: aload_1
    //   306: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   309: astore 9
    //   311: aload 9
    //   313: ifnull +290 -> 603
    //   316: aload 5
    //   318: astore 11
    //   320: aload 9
    //   322: astore 12
    //   324: aload 5
    //   326: astore 13
    //   328: aload 9
    //   330: astore 14
    //   332: aload 5
    //   334: astore 15
    //   336: aload 9
    //   338: astore 16
    //   340: aload 9
    //   342: aload_0
    //   343: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   346: aload_1
    //   347: aload_2
    //   348: aload_3
    //   349: aload 10
    //   351: invokeinterface 1224 6 0
    //   356: astore_3
    //   357: aload_3
    //   358: ifnonnull +40 -> 398
    //   361: aload 4
    //   363: ifnull +9 -> 372
    //   366: aload 4
    //   368: aconst_null
    //   369: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   372: aload 9
    //   374: ifnull +10 -> 384
    //   377: aload_0
    //   378: aload 9
    //   380: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   383: pop
    //   384: aload 5
    //   386: ifnull +10 -> 396
    //   389: aload_0
    //   390: aload 5
    //   392: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   395: pop
    //   396: aconst_null
    //   397: areturn
    //   398: aload 9
    //   400: astore 16
    //   402: aload 16
    //   404: astore_2
    //   405: aload 16
    //   407: ifnonnull +29 -> 436
    //   410: aload 5
    //   412: astore 11
    //   414: aload 16
    //   416: astore 12
    //   418: aload 5
    //   420: astore 13
    //   422: aload 16
    //   424: astore 14
    //   426: aload 5
    //   428: astore 15
    //   430: aload_0
    //   431: aload_1
    //   432: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   435: astore_2
    //   436: aload 5
    //   438: astore 11
    //   440: aload_2
    //   441: astore 12
    //   443: aload 5
    //   445: astore 13
    //   447: aload_2
    //   448: astore 14
    //   450: aload 5
    //   452: astore 15
    //   454: aload_2
    //   455: astore 16
    //   457: aload_0
    //   458: aload 5
    //   460: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   463: pop
    //   464: aconst_null
    //   465: astore 5
    //   467: aconst_null
    //   468: astore 10
    //   470: aconst_null
    //   471: astore 9
    //   473: aload 9
    //   475: astore 11
    //   477: aload_2
    //   478: astore 12
    //   480: aload 5
    //   482: astore 13
    //   484: aload_2
    //   485: astore 14
    //   487: aload 10
    //   489: astore 15
    //   491: aload_2
    //   492: astore 16
    //   494: new 19	android/content/ContentResolver$ParcelFileDescriptorInner
    //   497: astore 6
    //   499: aload 9
    //   501: astore 11
    //   503: aload_2
    //   504: astore 12
    //   506: aload 5
    //   508: astore 13
    //   510: aload_2
    //   511: astore 14
    //   513: aload 10
    //   515: astore 15
    //   517: aload_2
    //   518: astore 16
    //   520: aload 6
    //   522: aload_0
    //   523: aload_3
    //   524: invokevirtual 1171	android/content/res/AssetFileDescriptor:getParcelFileDescriptor	()Landroid/os/ParcelFileDescriptor;
    //   527: aload_2
    //   528: invokespecial 1174	android/content/ContentResolver$ParcelFileDescriptorInner:<init>	(Landroid/content/ContentResolver;Landroid/os/ParcelFileDescriptor;Landroid/content/IContentProvider;)V
    //   531: aconst_null
    //   532: astore 14
    //   534: aconst_null
    //   535: astore 16
    //   537: aconst_null
    //   538: astore 12
    //   540: aload 9
    //   542: astore 11
    //   544: aload 5
    //   546: astore 13
    //   548: aload 10
    //   550: astore 15
    //   552: new 1101	android/content/res/AssetFileDescriptor
    //   555: dup
    //   556: aload 6
    //   558: aload_3
    //   559: invokevirtual 1177	android/content/res/AssetFileDescriptor:getStartOffset	()J
    //   562: aload_3
    //   563: invokevirtual 1180	android/content/res/AssetFileDescriptor:getDeclaredLength	()J
    //   566: invokespecial 1121	android/content/res/AssetFileDescriptor:<init>	(Landroid/os/ParcelFileDescriptor;JJ)V
    //   569: astore_2
    //   570: aload 4
    //   572: ifnull +9 -> 581
    //   575: aload 4
    //   577: aconst_null
    //   578: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   581: iconst_0
    //   582: ifeq +9 -> 591
    //   585: aload_0
    //   586: aconst_null
    //   587: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   590: pop
    //   591: iconst_0
    //   592: ifeq +9 -> 601
    //   595: aload_0
    //   596: aconst_null
    //   597: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   600: pop
    //   601: aload_2
    //   602: areturn
    //   603: aload 5
    //   605: astore 11
    //   607: aload 9
    //   609: astore 12
    //   611: aload 5
    //   613: astore 13
    //   615: aload 9
    //   617: astore 14
    //   619: aload 5
    //   621: astore 15
    //   623: aload 9
    //   625: astore 16
    //   627: new 945	java/io/FileNotFoundException
    //   630: astore_3
    //   631: aload 5
    //   633: astore 11
    //   635: aload 9
    //   637: astore 12
    //   639: aload 5
    //   641: astore 13
    //   643: aload 9
    //   645: astore 14
    //   647: aload 5
    //   649: astore 15
    //   651: aload 9
    //   653: astore 16
    //   655: new 361	java/lang/StringBuilder
    //   658: astore_2
    //   659: aload 5
    //   661: astore 11
    //   663: aload 9
    //   665: astore 12
    //   667: aload 5
    //   669: astore 13
    //   671: aload 9
    //   673: astore 14
    //   675: aload 5
    //   677: astore 15
    //   679: aload 9
    //   681: astore 16
    //   683: aload_2
    //   684: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   687: aload 5
    //   689: astore 11
    //   691: aload 9
    //   693: astore 12
    //   695: aload 5
    //   697: astore 13
    //   699: aload 9
    //   701: astore 14
    //   703: aload 5
    //   705: astore 15
    //   707: aload 9
    //   709: astore 16
    //   711: aload_2
    //   712: ldc_w 1182
    //   715: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   718: pop
    //   719: aload 5
    //   721: astore 11
    //   723: aload 9
    //   725: astore 12
    //   727: aload 5
    //   729: astore 13
    //   731: aload 9
    //   733: astore 14
    //   735: aload 5
    //   737: astore 15
    //   739: aload 9
    //   741: astore 16
    //   743: aload_2
    //   744: aload_1
    //   745: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   748: pop
    //   749: aload 5
    //   751: astore 11
    //   753: aload 9
    //   755: astore 12
    //   757: aload 5
    //   759: astore 13
    //   761: aload 9
    //   763: astore 14
    //   765: aload 5
    //   767: astore 15
    //   769: aload 9
    //   771: astore 16
    //   773: aload_3
    //   774: aload_2
    //   775: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   778: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   781: aload 5
    //   783: astore 11
    //   785: aload 9
    //   787: astore 12
    //   789: aload 5
    //   791: astore 13
    //   793: aload 9
    //   795: astore 14
    //   797: aload 5
    //   799: astore 15
    //   801: aload 9
    //   803: astore 16
    //   805: aload_3
    //   806: athrow
    //   807: aload 13
    //   809: astore 11
    //   811: aload 14
    //   813: astore 12
    //   815: aload_1
    //   816: athrow
    //   817: aload 15
    //   819: astore 11
    //   821: aload 16
    //   823: astore 12
    //   825: new 945	java/io/FileNotFoundException
    //   828: astore_3
    //   829: aload 15
    //   831: astore 11
    //   833: aload 16
    //   835: astore 12
    //   837: new 361	java/lang/StringBuilder
    //   840: astore_2
    //   841: aload 15
    //   843: astore 11
    //   845: aload 16
    //   847: astore 12
    //   849: aload_2
    //   850: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   853: aload 15
    //   855: astore 11
    //   857: aload 16
    //   859: astore 12
    //   861: aload_2
    //   862: ldc_w 1184
    //   865: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   868: pop
    //   869: aload 15
    //   871: astore 11
    //   873: aload 16
    //   875: astore 12
    //   877: aload_2
    //   878: aload_1
    //   879: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   882: pop
    //   883: aload 15
    //   885: astore 11
    //   887: aload 16
    //   889: astore 12
    //   891: aload_3
    //   892: aload_2
    //   893: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   896: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   899: aload 15
    //   901: astore 11
    //   903: aload 16
    //   905: astore 12
    //   907: aload_3
    //   908: athrow
    //   909: aload 4
    //   911: ifnull +9 -> 920
    //   914: aload 4
    //   916: aconst_null
    //   917: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   920: aload 12
    //   922: ifnull +10 -> 932
    //   925: aload_0
    //   926: aload 12
    //   928: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   931: pop
    //   932: aload 11
    //   934: ifnull +10 -> 944
    //   937: aload_0
    //   938: aload 11
    //   940: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   943: pop
    //   944: aload_1
    //   945: athrow
    //   946: new 361	java/lang/StringBuilder
    //   949: dup
    //   950: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   953: astore_2
    //   954: aload_2
    //   955: ldc_w 1182
    //   958: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   961: pop
    //   962: aload_2
    //   963: aload_1
    //   964: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   967: pop
    //   968: new 945	java/io/FileNotFoundException
    //   971: dup
    //   972: aload_2
    //   973: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   976: invokespecial 963	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   979: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	980	0	this	ContentResolver
    //   0	980	1	paramUri	Uri
    //   0	980	2	paramString	String
    //   0	980	3	paramBundle	Bundle
    //   0	980	4	paramCancellationSignal	CancellationSignal
    //   21	777	5	localIContentProvider1	IContentProvider
    //   29	528	6	localParcelFileDescriptorInner	ParcelFileDescriptorInner
    //   32	269	7	localObject1	Object
    //   35	207	8	localObject2	Object
    //   38	764	9	localIContentProvider2	IContentProvider
    //   41	508	10	localICancellationSignal	android.os.ICancellationSignal
    //   50	889	11	localObject3	Object
    //   54	109	12	localIContentProvider3	IContentProvider
    //   248	1	12	localDeadObjectException	android.os.DeadObjectException
    //   256	671	12	localObject4	Object
    //   58	750	13	localIContentProvider4	IContentProvider
    //   62	750	14	localObject5	Object
    //   66	834	15	localObject6	Object
    //   70	834	16	localObject7	Object
    //   196	43	17	localAssetFileDescriptor	AssetFileDescriptor
    // Exception table:
    //   from	to	target	type
    //   72	77	144	finally
    //   101	110	144	finally
    //   134	141	144	finally
    //   180	198	144	finally
    //   274	280	144	finally
    //   304	311	144	finally
    //   340	357	144	finally
    //   430	436	144	finally
    //   457	464	144	finally
    //   494	499	144	finally
    //   520	531	144	finally
    //   552	570	144	finally
    //   627	631	144	finally
    //   655	659	144	finally
    //   683	687	144	finally
    //   711	719	144	finally
    //   743	749	144	finally
    //   773	781	144	finally
    //   805	807	144	finally
    //   815	817	144	finally
    //   825	829	144	finally
    //   837	841	144	finally
    //   849	853	144	finally
    //   861	869	144	finally
    //   877	883	144	finally
    //   891	899	144	finally
    //   907	909	144	finally
    //   72	77	148	java/io/FileNotFoundException
    //   101	110	148	java/io/FileNotFoundException
    //   134	141	148	java/io/FileNotFoundException
    //   180	198	148	java/io/FileNotFoundException
    //   274	280	148	java/io/FileNotFoundException
    //   304	311	148	java/io/FileNotFoundException
    //   340	357	148	java/io/FileNotFoundException
    //   430	436	148	java/io/FileNotFoundException
    //   457	464	148	java/io/FileNotFoundException
    //   494	499	148	java/io/FileNotFoundException
    //   520	531	148	java/io/FileNotFoundException
    //   552	570	148	java/io/FileNotFoundException
    //   627	631	148	java/io/FileNotFoundException
    //   655	659	148	java/io/FileNotFoundException
    //   683	687	148	java/io/FileNotFoundException
    //   711	719	148	java/io/FileNotFoundException
    //   743	749	148	java/io/FileNotFoundException
    //   773	781	148	java/io/FileNotFoundException
    //   805	807	148	java/io/FileNotFoundException
    //   72	77	152	android/os/RemoteException
    //   101	110	152	android/os/RemoteException
    //   134	141	152	android/os/RemoteException
    //   180	198	152	android/os/RemoteException
    //   274	280	152	android/os/RemoteException
    //   304	311	152	android/os/RemoteException
    //   340	357	152	android/os/RemoteException
    //   430	436	152	android/os/RemoteException
    //   457	464	152	android/os/RemoteException
    //   494	499	152	android/os/RemoteException
    //   520	531	152	android/os/RemoteException
    //   552	570	152	android/os/RemoteException
    //   627	631	152	android/os/RemoteException
    //   655	659	152	android/os/RemoteException
    //   683	687	152	android/os/RemoteException
    //   711	719	152	android/os/RemoteException
    //   743	749	152	android/os/RemoteException
    //   773	781	152	android/os/RemoteException
    //   805	807	152	android/os/RemoteException
    //   180	198	248	android/os/DeadObjectException
  }
  
  public void putCache(Uri paramUri, Bundle paramBundle)
  {
    try
    {
      getContentService().putCache(mContext.getPackageName(), paramUri, paramBundle, mContext.getUserId());
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public final Cursor query(@RequiresPermission.Read Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: bipush 13
    //   2: aload_1
    //   3: invokestatic 1045	android/util/SeempLog:record_uri	(ILandroid/net/Uri;)I
    //   6: pop
    //   7: aload_1
    //   8: ldc_w 700
    //   11: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   14: pop
    //   15: aload_0
    //   16: aload_1
    //   17: invokevirtual 737	android/content/ContentResolver:acquireUnstableProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   20: astore 5
    //   22: aload 5
    //   24: ifnonnull +5 -> 29
    //   27: aconst_null
    //   28: areturn
    //   29: aconst_null
    //   30: astore 6
    //   32: aconst_null
    //   33: astore 7
    //   35: aconst_null
    //   36: astore 8
    //   38: aconst_null
    //   39: astore 9
    //   41: aconst_null
    //   42: astore 10
    //   44: aload 6
    //   46: astore 11
    //   48: aload 10
    //   50: astore 12
    //   52: aload 7
    //   54: astore 13
    //   56: aload 9
    //   58: astore 14
    //   60: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   63: lstore 15
    //   65: aconst_null
    //   66: astore 17
    //   68: aload 4
    //   70: ifnull +72 -> 142
    //   73: aload 6
    //   75: astore 11
    //   77: aload 10
    //   79: astore 12
    //   81: aload 7
    //   83: astore 13
    //   85: aload 9
    //   87: astore 14
    //   89: aload 4
    //   91: invokevirtual 1149	android/os/CancellationSignal:throwIfCanceled	()V
    //   94: aload 6
    //   96: astore 11
    //   98: aload 10
    //   100: astore 12
    //   102: aload 7
    //   104: astore 13
    //   106: aload 9
    //   108: astore 14
    //   110: aload 5
    //   112: invokeinterface 1153 1 0
    //   117: astore 17
    //   119: aload 6
    //   121: astore 11
    //   123: aload 10
    //   125: astore 12
    //   127: aload 7
    //   129: astore 13
    //   131: aload 9
    //   133: astore 14
    //   135: aload 4
    //   137: aload 17
    //   139: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   142: aload 6
    //   144: astore 11
    //   146: aload 10
    //   148: astore 12
    //   150: aload 7
    //   152: astore 13
    //   154: aload 9
    //   156: astore 14
    //   158: aload 5
    //   160: aload_0
    //   161: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   164: aload_1
    //   165: aload_2
    //   166: aload_3
    //   167: aload 17
    //   169: invokeinterface 856 6 0
    //   174: astore 18
    //   176: aload 8
    //   178: astore 6
    //   180: aload 18
    //   182: astore 17
    //   184: goto +139 -> 323
    //   187: astore 13
    //   189: aload 6
    //   191: astore 11
    //   193: aload 10
    //   195: astore 12
    //   197: aload 7
    //   199: astore 13
    //   201: aload 9
    //   203: astore 14
    //   205: aload_0
    //   206: aload 5
    //   208: invokevirtual 1167	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   211: aload 6
    //   213: astore 11
    //   215: aload 10
    //   217: astore 12
    //   219: aload 7
    //   221: astore 13
    //   223: aload 9
    //   225: astore 14
    //   227: aload_0
    //   228: aload_1
    //   229: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   232: astore 6
    //   234: aload 6
    //   236: ifnonnull +50 -> 286
    //   239: iconst_0
    //   240: ifeq +9 -> 249
    //   243: aconst_null
    //   244: invokeinterface 900 1 0
    //   249: aload 4
    //   251: ifnull +9 -> 260
    //   254: aload 4
    //   256: aconst_null
    //   257: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   260: aload 5
    //   262: ifnull +10 -> 272
    //   265: aload_0
    //   266: aload 5
    //   268: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   271: pop
    //   272: aload 6
    //   274: ifnull +10 -> 284
    //   277: aload_0
    //   278: aload 6
    //   280: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   283: pop
    //   284: aconst_null
    //   285: areturn
    //   286: aload 6
    //   288: astore 11
    //   290: aload 10
    //   292: astore 12
    //   294: aload 6
    //   296: astore 13
    //   298: aload 9
    //   300: astore 14
    //   302: aload 6
    //   304: aload_0
    //   305: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   308: aload_1
    //   309: aload_2
    //   310: aload_3
    //   311: aload 17
    //   313: invokeinterface 856 6 0
    //   318: astore 17
    //   320: goto -136 -> 184
    //   323: aload 17
    //   325: ifnonnull +52 -> 377
    //   328: aload 17
    //   330: ifnull +10 -> 340
    //   333: aload 17
    //   335: invokeinterface 900 1 0
    //   340: aload 4
    //   342: ifnull +9 -> 351
    //   345: aload 4
    //   347: aconst_null
    //   348: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   351: aload 5
    //   353: ifnull +10 -> 363
    //   356: aload_0
    //   357: aload 5
    //   359: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   362: pop
    //   363: aload 6
    //   365: ifnull +10 -> 375
    //   368: aload_0
    //   369: aload 6
    //   371: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   374: pop
    //   375: aconst_null
    //   376: areturn
    //   377: aload 6
    //   379: astore 11
    //   381: aload 17
    //   383: astore 12
    //   385: aload 6
    //   387: astore 13
    //   389: aload 17
    //   391: astore 14
    //   393: aload 17
    //   395: invokeinterface 1234 1 0
    //   400: pop
    //   401: aload 6
    //   403: astore 11
    //   405: aload 17
    //   407: astore 12
    //   409: aload 6
    //   411: astore 13
    //   413: aload 17
    //   415: astore 14
    //   417: aload_0
    //   418: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   421: lload 15
    //   423: lsub
    //   424: aload_1
    //   425: aload_2
    //   426: aload_3
    //   427: invokespecial 1236	android/content/ContentResolver:maybeLogQueryToEventLog	(JLandroid/net/Uri;[Ljava/lang/String;Landroid/os/Bundle;)V
    //   430: aload 6
    //   432: ifnull +9 -> 441
    //   435: aload 6
    //   437: astore_1
    //   438: goto +25 -> 463
    //   441: aload 6
    //   443: astore 11
    //   445: aload 17
    //   447: astore 12
    //   449: aload 6
    //   451: astore 13
    //   453: aload 17
    //   455: astore 14
    //   457: aload_0
    //   458: aload_1
    //   459: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   462: astore_1
    //   463: aload 6
    //   465: astore 11
    //   467: aload 17
    //   469: astore 12
    //   471: aload 6
    //   473: astore 13
    //   475: aload 17
    //   477: astore 14
    //   479: new 10	android/content/ContentResolver$CursorWrapperInner
    //   482: dup
    //   483: aload_0
    //   484: aload 17
    //   486: aload_1
    //   487: invokespecial 1239	android/content/ContentResolver$CursorWrapperInner:<init>	(Landroid/content/ContentResolver;Landroid/database/Cursor;Landroid/content/IContentProvider;)V
    //   490: astore_1
    //   491: iconst_0
    //   492: ifeq +11 -> 503
    //   495: new 1241	java/lang/NullPointerException
    //   498: dup
    //   499: invokespecial 1242	java/lang/NullPointerException:<init>	()V
    //   502: athrow
    //   503: aload 4
    //   505: ifnull +9 -> 514
    //   508: aload 4
    //   510: aconst_null
    //   511: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   514: aload 5
    //   516: ifnull +10 -> 526
    //   519: aload_0
    //   520: aload 5
    //   522: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   525: pop
    //   526: iconst_0
    //   527: ifeq +9 -> 536
    //   530: aload_0
    //   531: aconst_null
    //   532: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   535: pop
    //   536: aload_1
    //   537: areturn
    //   538: astore_1
    //   539: aload 12
    //   541: ifnull +10 -> 551
    //   544: aload 12
    //   546: invokeinterface 900 1 0
    //   551: aload 4
    //   553: ifnull +9 -> 562
    //   556: aload 4
    //   558: aconst_null
    //   559: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   562: aload 5
    //   564: ifnull +10 -> 574
    //   567: aload_0
    //   568: aload 5
    //   570: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   573: pop
    //   574: aload 11
    //   576: ifnull +10 -> 586
    //   579: aload_0
    //   580: aload 11
    //   582: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   585: pop
    //   586: aload_1
    //   587: athrow
    //   588: astore_1
    //   589: aload 14
    //   591: ifnull +10 -> 601
    //   594: aload 14
    //   596: invokeinterface 900 1 0
    //   601: aload 4
    //   603: ifnull +9 -> 612
    //   606: aload 4
    //   608: aconst_null
    //   609: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   612: aload 5
    //   614: ifnull +10 -> 624
    //   617: aload_0
    //   618: aload 5
    //   620: invokevirtual 1164	android/content/ContentResolver:releaseUnstableProvider	(Landroid/content/IContentProvider;)Z
    //   623: pop
    //   624: aload 13
    //   626: ifnull +10 -> 636
    //   629: aload_0
    //   630: aload 13
    //   632: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   635: pop
    //   636: aconst_null
    //   637: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	638	0	this	ContentResolver
    //   0	638	1	paramUri	Uri
    //   0	638	2	paramArrayOfString	String[]
    //   0	638	3	paramBundle	Bundle
    //   0	638	4	paramCancellationSignal	CancellationSignal
    //   20	599	5	localIContentProvider	IContentProvider
    //   30	442	6	localObject1	Object
    //   33	187	7	localObject2	Object
    //   36	141	8	localObject3	Object
    //   39	260	9	localObject4	Object
    //   42	249	10	localObject5	Object
    //   46	535	11	localObject6	Object
    //   50	495	12	localObject7	Object
    //   54	99	13	localObject8	Object
    //   187	1	13	localDeadObjectException	android.os.DeadObjectException
    //   199	432	13	localObject9	Object
    //   58	537	14	localObject10	Object
    //   63	359	15	l	long
    //   66	419	17	localObject11	Object
    //   174	7	18	localCursor	Cursor
    // Exception table:
    //   from	to	target	type
    //   158	176	187	android/os/DeadObjectException
    //   60	65	538	finally
    //   89	94	538	finally
    //   110	119	538	finally
    //   135	142	538	finally
    //   158	176	538	finally
    //   205	211	538	finally
    //   227	234	538	finally
    //   302	320	538	finally
    //   393	401	538	finally
    //   417	430	538	finally
    //   457	463	538	finally
    //   479	491	538	finally
    //   60	65	588	android/os/RemoteException
    //   89	94	588	android/os/RemoteException
    //   110	119	588	android/os/RemoteException
    //   135	142	588	android/os/RemoteException
    //   158	176	588	android/os/RemoteException
    //   205	211	588	android/os/RemoteException
    //   227	234	588	android/os/RemoteException
    //   302	320	588	android/os/RemoteException
    //   393	401	588	android/os/RemoteException
    //   417	430	588	android/os/RemoteException
    //   457	463	588	android/os/RemoteException
    //   479	491	588	android/os/RemoteException
  }
  
  public final Cursor query(@RequiresPermission.Read Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    SeempLog.record_uri(13, paramUri);
    return query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, null);
  }
  
  public final Cursor query(@RequiresPermission.Read Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    return query(paramUri, paramArrayOfString1, createSqlQueryBundle(paramString1, paramArrayOfString2, paramString2), paramCancellationSignal);
  }
  
  /* Error */
  public final boolean refresh(Uri paramUri, Bundle paramBundle, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 772
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   13: astore 4
    //   15: aload 4
    //   17: ifnonnull +5 -> 22
    //   20: iconst_0
    //   21: ireturn
    //   22: aconst_null
    //   23: astore 5
    //   25: aload_3
    //   26: ifnull +33 -> 59
    //   29: aload_3
    //   30: invokevirtual 1149	android/os/CancellationSignal:throwIfCanceled	()V
    //   33: aload 4
    //   35: invokeinterface 1153 1 0
    //   40: astore 5
    //   42: aload_3
    //   43: aload 5
    //   45: invokevirtual 1157	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   48: goto +11 -> 59
    //   51: astore_1
    //   52: goto +34 -> 86
    //   55: astore_1
    //   56: goto +39 -> 95
    //   59: aload 4
    //   61: aload_0
    //   62: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   65: aload_1
    //   66: aload_2
    //   67: aload 5
    //   69: invokeinterface 1253 5 0
    //   74: istore 6
    //   76: aload_0
    //   77: aload 4
    //   79: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   82: pop
    //   83: iload 6
    //   85: ireturn
    //   86: aload_0
    //   87: aload 4
    //   89: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   92: pop
    //   93: aload_1
    //   94: athrow
    //   95: aload_0
    //   96: aload 4
    //   98: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   101: pop
    //   102: iconst_0
    //   103: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	104	0	this	ContentResolver
    //   0	104	1	paramUri	Uri
    //   0	104	2	paramBundle	Bundle
    //   0	104	3	paramCancellationSignal	CancellationSignal
    //   13	84	4	localIContentProvider	IContentProvider
    //   23	45	5	localICancellationSignal	android.os.ICancellationSignal
    //   74	10	6	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   29	48	51	finally
    //   59	76	51	finally
    //   29	48	55	android/os/RemoteException
    //   59	76	55	android/os/RemoteException
  }
  
  public final void registerContentObserver(Uri paramUri, boolean paramBoolean, ContentObserver paramContentObserver)
  {
    Preconditions.checkNotNull(paramUri, "uri");
    Preconditions.checkNotNull(paramContentObserver, "observer");
    registerContentObserver(ContentProvider.getUriWithoutUserId(paramUri), paramBoolean, paramContentObserver, ContentProvider.getUserIdFromUri(paramUri, mContext.getUserId()));
  }
  
  public final void registerContentObserver(Uri paramUri, boolean paramBoolean, ContentObserver paramContentObserver, int paramInt)
  {
    try
    {
      getContentService().registerContentObserver(paramUri, paramBoolean, paramContentObserver.getContentObserver(), paramInt, mTargetSdkVersion);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public void releasePersistableUriPermission(Uri paramUri, int paramInt)
  {
    Preconditions.checkNotNull(paramUri, "uri");
    try
    {
      ActivityManager.getService().releasePersistableUriPermission(ContentProvider.getUriWithoutUserId(paramUri), paramInt, null, resolveUserId(paramUri));
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public abstract boolean releaseProvider(IContentProvider paramIContentProvider);
  
  public abstract boolean releaseUnstableProvider(IContentProvider paramIContentProvider);
  
  public int resolveUserId(Uri paramUri)
  {
    return ContentProvider.getUserIdFromUri(paramUri, mContext.getUserId());
  }
  
  @Deprecated
  public void startSync(Uri paramUri, Bundle paramBundle)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramBundle != null)
    {
      String str = paramBundle.getString("account");
      localObject1 = localObject2;
      if (!TextUtils.isEmpty(str)) {
        localObject1 = new Account(str, "com.google");
      }
      paramBundle.remove("account");
    }
    if (paramUri != null) {
      paramUri = paramUri.getAuthority();
    } else {
      paramUri = null;
    }
    requestSync((Account)localObject1, paramUri, paramBundle);
  }
  
  public void takePersistableUriPermission(Uri paramUri, int paramInt)
  {
    Preconditions.checkNotNull(paramUri, "uri");
    try
    {
      ActivityManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(paramUri), paramInt, null, resolveUserId(paramUri));
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public void takePersistableUriPermission(String paramString, Uri paramUri, int paramInt)
  {
    Preconditions.checkNotNull(paramString, "toPackage");
    Preconditions.checkNotNull(paramUri, "uri");
    try
    {
      ActivityManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(paramUri), paramInt, paramString, resolveUserId(paramUri));
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public final Uri uncanonicalize(Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 772
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   13: astore_2
    //   14: aload_2
    //   15: ifnonnull +5 -> 20
    //   18: aconst_null
    //   19: areturn
    //   20: aload_2
    //   21: aload_0
    //   22: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   25: aload_1
    //   26: invokeinterface 1290 3 0
    //   31: astore_1
    //   32: aload_0
    //   33: aload_2
    //   34: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   37: pop
    //   38: aload_1
    //   39: areturn
    //   40: astore_1
    //   41: aload_0
    //   42: aload_2
    //   43: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   46: pop
    //   47: aload_1
    //   48: athrow
    //   49: astore_1
    //   50: aload_0
    //   51: aload_2
    //   52: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   55: pop
    //   56: aconst_null
    //   57: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	58	0	this	ContentResolver
    //   0	58	1	paramUri	Uri
    //   13	39	2	localIContentProvider	IContentProvider
    // Exception table:
    //   from	to	target	type
    //   20	32	40	finally
    //   20	32	49	android/os/RemoteException
  }
  
  public final void unregisterContentObserver(ContentObserver paramContentObserver)
  {
    Preconditions.checkNotNull(paramContentObserver, "observer");
    try
    {
      paramContentObserver = paramContentObserver.releaseContentObserver();
      if (paramContentObserver != null) {
        getContentService().unregisterContentObserver(paramContentObserver);
      }
      return;
    }
    catch (RemoteException paramContentObserver)
    {
      throw paramContentObserver.rethrowFromSystemServer();
    }
  }
  
  public abstract void unstableProviderDied(IContentProvider paramIContentProvider);
  
  /* Error */
  public final int update(@android.annotation.RequiresPermission.Write Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 700
    //   4: invokestatic 706	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual 710	android/content/ContentResolver:acquireProvider	(Landroid/net/Uri;)Landroid/content/IContentProvider;
    //   13: astore 5
    //   15: aload 5
    //   17: ifnull +71 -> 88
    //   20: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   23: lstore 6
    //   25: aload 5
    //   27: aload_0
    //   28: getfield 260	android/content/ContentResolver:mPackageName	Ljava/lang/String;
    //   31: aload_1
    //   32: aload_2
    //   33: aload_3
    //   34: aload 4
    //   36: invokeinterface 1303 6 0
    //   41: istore 8
    //   43: aload_0
    //   44: invokestatic 780	android/os/SystemClock:uptimeMillis	()J
    //   47: lload 6
    //   49: lsub
    //   50: aload_1
    //   51: ldc_w 1304
    //   54: aload_3
    //   55: invokespecial 789	android/content/ContentResolver:maybeLogUpdateToEventLog	(JLandroid/net/Uri;Ljava/lang/String;Ljava/lang/String;)V
    //   58: aload_0
    //   59: aload 5
    //   61: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   64: pop
    //   65: iload 8
    //   67: ireturn
    //   68: astore_1
    //   69: aload_0
    //   70: aload 5
    //   72: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   75: pop
    //   76: aload_1
    //   77: athrow
    //   78: astore_1
    //   79: aload_0
    //   80: aload 5
    //   82: invokevirtual 793	android/content/ContentResolver:releaseProvider	(Landroid/content/IContentProvider;)Z
    //   85: pop
    //   86: iconst_m1
    //   87: ireturn
    //   88: new 361	java/lang/StringBuilder
    //   91: dup
    //   92: invokespecial 362	java/lang/StringBuilder:<init>	()V
    //   95: astore_2
    //   96: aload_2
    //   97: ldc_w 812
    //   100: invokevirtual 366	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload_2
    //   105: aload_1
    //   106: invokevirtual 798	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   109: pop
    //   110: new 299	java/lang/IllegalArgumentException
    //   113: dup
    //   114: aload_2
    //   115: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   118: invokespecial 302	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   121: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	122	0	this	ContentResolver
    //   0	122	1	paramUri	Uri
    //   0	122	2	paramContentValues	ContentValues
    //   0	122	3	paramString	String
    //   0	122	4	paramArrayOfString	String[]
    //   13	68	5	localIContentProvider	IContentProvider
    //   23	25	6	l	long
    //   41	25	8	i	int
    // Exception table:
    //   from	to	target	type
    //   20	58	68	finally
    //   20	58	78	android/os/RemoteException
  }
  
  private final class CursorWrapperInner
    extends CrossProcessCursorWrapper
  {
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final IContentProvider mContentProvider;
    private final AtomicBoolean mProviderReleased = new AtomicBoolean();
    
    CursorWrapperInner(Cursor paramCursor, IContentProvider paramIContentProvider)
    {
      super();
      mContentProvider = paramIContentProvider;
      mCloseGuard.open("close");
    }
    
    public void close()
    {
      mCloseGuard.close();
      super.close();
      if (mProviderReleased.compareAndSet(false, true)) {
        releaseProvider(mContentProvider);
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if (mCloseGuard != null) {
          mCloseGuard.warnIfOpen();
        }
        close();
        return;
      }
      finally
      {
        super.finalize();
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface NotifyFlags {}
  
  public class OpenResourceIdResult
  {
    public int id;
    public Resources r;
    
    public OpenResourceIdResult() {}
  }
  
  private final class ParcelFileDescriptorInner
    extends ParcelFileDescriptor
  {
    private final IContentProvider mContentProvider;
    private final AtomicBoolean mProviderReleased = new AtomicBoolean();
    
    ParcelFileDescriptorInner(ParcelFileDescriptor paramParcelFileDescriptor, IContentProvider paramIContentProvider)
    {
      super();
      mContentProvider = paramIContentProvider;
    }
    
    public void releaseResources()
    {
      if (mProviderReleased.compareAndSet(false, true)) {
        releaseProvider(mContentProvider);
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface QueryCollator {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SortDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SyncExemption {}
}
