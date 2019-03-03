package android.content;

import android.app.AppOpsManager;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public abstract class ContentProvider
  implements ComponentCallbacks2
{
  private static final String TAG = "ContentProvider";
  private String[] mAuthorities;
  private String mAuthority;
  private final ThreadLocal<String> mCallingPackage = new ThreadLocal();
  private Context mContext = null;
  private boolean mExported;
  private int mMyUid;
  private boolean mNoPerms;
  private PathPermission[] mPathPermissions;
  private String mReadPermission;
  private boolean mSingleUser;
  private Transport mTransport = new Transport();
  private String mWritePermission;
  
  public ContentProvider() {}
  
  public ContentProvider(Context paramContext, String paramString1, String paramString2, PathPermission[] paramArrayOfPathPermission)
  {
    mContext = paramContext;
    mReadPermission = paramString1;
    mWritePermission = paramString2;
    mPathPermissions = paramArrayOfPathPermission;
  }
  
  private void attachInfo(Context paramContext, ProviderInfo paramProviderInfo, boolean paramBoolean)
  {
    mNoPerms = paramBoolean;
    if (mContext == null)
    {
      mContext = paramContext;
      if ((paramContext != null) && (mTransport != null)) {
        mTransport.mAppOpsManager = ((AppOpsManager)paramContext.getSystemService("appops"));
      }
      mMyUid = Process.myUid();
      if (paramProviderInfo != null)
      {
        setReadPermission(readPermission);
        setWritePermission(writePermission);
        setPathPermissions(pathPermissions);
        mExported = exported;
        if ((flags & 0x40000000) != 0) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        mSingleUser = paramBoolean;
        setAuthorities(authority);
      }
      onCreate();
    }
  }
  
  private int checkPermissionAndAppOp(String paramString1, String paramString2, IBinder paramIBinder)
  {
    if (getContext().checkPermission(paramString1, Binder.getCallingPid(), Binder.getCallingUid(), paramIBinder) != 0) {
      return 2;
    }
    int i = AppOpsManager.permissionToOpCode(paramString1);
    if (i != -1) {
      return mTransport.mAppOpsManager.noteProxyOp(i, paramString2);
    }
    return 0;
  }
  
  public static ContentProvider coerceToLocalContentProvider(IContentProvider paramIContentProvider)
  {
    if ((paramIContentProvider instanceof Transport)) {
      return ((Transport)paramIContentProvider).getContentProvider();
    }
    return null;
  }
  
  public static String getAuthorityWithoutUserId(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return paramString.substring(paramString.lastIndexOf('@') + 1);
  }
  
  public static Uri getUriWithoutUserId(Uri paramUri)
  {
    if (paramUri == null) {
      return null;
    }
    Uri.Builder localBuilder = paramUri.buildUpon();
    localBuilder.authority(getAuthorityWithoutUserId(paramUri.getAuthority()));
    return localBuilder.build();
  }
  
  public static int getUserIdFromAuthority(String paramString)
  {
    return getUserIdFromAuthority(paramString, -2);
  }
  
  public static int getUserIdFromAuthority(String paramString, int paramInt)
  {
    if (paramString == null) {
      return paramInt;
    }
    int i = paramString.lastIndexOf('@');
    if (i == -1) {
      return paramInt;
    }
    paramString = paramString.substring(0, i);
    try
    {
      paramInt = Integer.parseInt(paramString);
      return paramInt;
    }
    catch (NumberFormatException paramString)
    {
      Log.w("ContentProvider", "Error parsing userId.", paramString);
    }
    return 55536;
  }
  
  public static int getUserIdFromUri(Uri paramUri)
  {
    return getUserIdFromUri(paramUri, -2);
  }
  
  public static int getUserIdFromUri(Uri paramUri, int paramInt)
  {
    if (paramUri == null) {
      return paramInt;
    }
    return getUserIdFromAuthority(paramUri.getAuthority(), paramInt);
  }
  
  public static Uri maybeAddUserId(Uri paramUri, int paramInt)
  {
    if (paramUri == null) {
      return null;
    }
    if ((paramInt != -2) && ("content".equals(paramUri.getScheme())) && (!uriHasUserId(paramUri)))
    {
      Uri.Builder localBuilder = paramUri.buildUpon();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("@");
      localStringBuilder.append(paramUri.getEncodedAuthority());
      localBuilder.encodedAuthority(localStringBuilder.toString());
      return localBuilder.build();
    }
    return paramUri;
  }
  
  private Uri maybeGetUriWithoutUserId(Uri paramUri)
  {
    if (mSingleUser) {
      return paramUri;
    }
    return getUriWithoutUserId(paramUri);
  }
  
  private String setCallingPackage(String paramString)
  {
    String str = (String)mCallingPackage.get();
    mCallingPackage.set(paramString);
    return str;
  }
  
  public static boolean uriHasUserId(Uri paramUri)
  {
    if (paramUri == null) {
      return false;
    }
    return TextUtils.isEmpty(paramUri.getUserInfo()) ^ true;
  }
  
  public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> paramArrayList)
    throws OperationApplicationException
  {
    int i = paramArrayList.size();
    ContentProviderResult[] arrayOfContentProviderResult = new ContentProviderResult[i];
    for (int j = 0; j < i; j++) {
      arrayOfContentProviderResult[j] = ((ContentProviderOperation)paramArrayList.get(j)).apply(this, arrayOfContentProviderResult, j);
    }
    return arrayOfContentProviderResult;
  }
  
  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo)
  {
    attachInfo(paramContext, paramProviderInfo, false);
  }
  
  public void attachInfoForTesting(Context paramContext, ProviderInfo paramProviderInfo)
  {
    attachInfo(paramContext, paramProviderInfo, true);
  }
  
  public int bulkInsert(Uri paramUri, ContentValues[] paramArrayOfContentValues)
  {
    int i = paramArrayOfContentValues.length;
    for (int j = 0; j < i; j++) {
      insert(paramUri, paramArrayOfContentValues[j]);
    }
    return i;
  }
  
  public Bundle call(String paramString1, String paramString2, Bundle paramBundle)
  {
    return null;
  }
  
  public Uri canonicalize(Uri paramUri)
  {
    return null;
  }
  
  boolean checkUser(int paramInt1, int paramInt2, Context paramContext)
  {
    boolean bool;
    if ((UserHandle.getUserId(paramInt2) != paramContext.getUserId()) && (!mSingleUser) && (paramContext.checkPermission("android.permission.INTERACT_ACROSS_USERS", paramInt1, paramInt2) != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public abstract int delete(Uri paramUri, String paramString, String[] paramArrayOfString);
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("nothing to dump");
  }
  
  protected int enforceReadPermissionInner(Uri paramUri, String paramString, IBinder paramIBinder)
    throws SecurityException
  {
    Context localContext = getContext();
    int i = Binder.getCallingPid();
    int j = Binder.getCallingUid();
    String str1 = null;
    String str2 = null;
    int k = 0;
    int m = 0;
    if (UserHandle.isSameApp(j, mMyUid)) {
      return 0;
    }
    String str3 = str1;
    int n = k;
    if (mExported)
    {
      str3 = str1;
      n = k;
      if (checkUser(i, j, localContext))
      {
        str1 = getReadPermission();
        str3 = str2;
        n = m;
        if (str1 != null)
        {
          n = checkPermissionAndAppOp(str1, paramString, paramIBinder);
          if (n == 0) {
            return 0;
          }
          str3 = str1;
          n = Math.max(0, n);
        }
        if (str1 == null) {
          m = 1;
        } else {
          m = 0;
        }
        PathPermission[] arrayOfPathPermission = getPathPermissions();
        if (arrayOfPathPermission != null)
        {
          str1 = paramUri.getPath();
          int i1 = arrayOfPathPermission.length;
          for (k = 0; k < i1; k++)
          {
            PathPermission localPathPermission = arrayOfPathPermission[k];
            str2 = localPathPermission.getReadPermission();
            if ((str2 != null) && (localPathPermission.match(str1)))
            {
              int i2 = checkPermissionAndAppOp(str2, paramString, paramIBinder);
              if (i2 == 0) {
                return 0;
              }
              m = 0;
              n = Math.max(n, i2);
              str3 = str2;
            }
          }
        }
        if (m != 0) {
          return 0;
        }
      }
    }
    m = UserHandle.getUserId(j);
    if ((mSingleUser) && (!UserHandle.isSameUser(mMyUid, j))) {
      paramString = maybeAddUserId(paramUri, m);
    } else {
      paramString = paramUri;
    }
    if (localContext.checkUriPermission(paramString, i, j, 1, paramIBinder) == 0) {
      return 0;
    }
    if (n == 1) {
      return 1;
    }
    if (!"android.permission.MANAGE_DOCUMENTS".equals(mReadPermission))
    {
      if (mExported)
      {
        paramString = new StringBuilder();
        paramString.append(" requires ");
        paramString.append(str3);
        paramString.append(", or grantUriPermission()");
        paramString = paramString.toString();
      }
      else
      {
        paramString = " requires the provider be exported, or grantUriPermission()";
      }
    }
    else {
      paramString = " requires that you obtain access using ACTION_OPEN_DOCUMENT or related APIs";
    }
    paramIBinder = new StringBuilder();
    paramIBinder.append("Permission Denial: reading ");
    paramIBinder.append(getClass().getName());
    paramIBinder.append(" uri ");
    paramIBinder.append(paramUri);
    paramIBinder.append(" from pid=");
    paramIBinder.append(i);
    paramIBinder.append(", uid=");
    paramIBinder.append(j);
    paramIBinder.append(paramString);
    throw new SecurityException(paramIBinder.toString());
  }
  
  protected int enforceWritePermissionInner(Uri paramUri, String paramString, IBinder paramIBinder)
    throws SecurityException
  {
    Context localContext = getContext();
    int i = Binder.getCallingPid();
    int j = Binder.getCallingUid();
    String str1 = null;
    String str2 = null;
    int k = 0;
    int m = 0;
    if (UserHandle.isSameApp(j, mMyUid)) {
      return 0;
    }
    String str3 = str1;
    int n = k;
    if (mExported)
    {
      str3 = str1;
      n = k;
      if (checkUser(i, j, localContext))
      {
        str1 = getWritePermission();
        str3 = str2;
        if (str1 != null)
        {
          m = checkPermissionAndAppOp(str1, paramString, paramIBinder);
          if (m == 0) {
            return 0;
          }
          str3 = str1;
          m = Math.max(0, m);
        }
        if (str1 == null) {
          n = 1;
        } else {
          n = 0;
        }
        PathPermission[] arrayOfPathPermission = getPathPermissions();
        if (arrayOfPathPermission != null)
        {
          str2 = paramUri.getPath();
          int i1 = arrayOfPathPermission.length;
          k = n;
          n = m;
          int i2 = 0;
          m = k;
          for (k = i2; k < i1; k++)
          {
            PathPermission localPathPermission = arrayOfPathPermission[k];
            str1 = localPathPermission.getWritePermission();
            if ((str1 != null) && (localPathPermission.match(str2)))
            {
              i2 = checkPermissionAndAppOp(str1, paramString, paramIBinder);
              if (i2 == 0) {
                return 0;
              }
              m = 0;
              n = Math.max(n, i2);
              str3 = str1;
            }
          }
          k = m;
        }
        else
        {
          k = n;
          n = m;
        }
        if (k != 0) {
          return 0;
        }
      }
    }
    if (localContext.checkUriPermission(paramUri, i, j, 2, paramIBinder) == 0) {
      return 0;
    }
    if (n == 1) {
      return 1;
    }
    if (mExported)
    {
      paramString = new StringBuilder();
      paramString.append(" requires ");
      paramString.append(str3);
      paramString.append(", or grantUriPermission()");
      paramString = paramString.toString();
    }
    else
    {
      paramString = " requires the provider be exported, or grantUriPermission()";
    }
    paramIBinder = new StringBuilder();
    paramIBinder.append("Permission Denial: writing ");
    paramIBinder.append(getClass().getName());
    paramIBinder.append(" uri ");
    paramIBinder.append(paramUri);
    paramIBinder.append(" from pid=");
    paramIBinder.append(i);
    paramIBinder.append(", uid=");
    paramIBinder.append(j);
    paramIBinder.append(paramString);
    throw new SecurityException(paramIBinder.toString());
  }
  
  public AppOpsManager getAppOpsManager()
  {
    return mTransport.mAppOpsManager;
  }
  
  public final String getCallingPackage()
  {
    String str = (String)mCallingPackage.get();
    if (str != null) {
      mTransport.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
    }
    return str;
  }
  
  public final Context getContext()
  {
    return mContext;
  }
  
  public IContentProvider getIContentProvider()
  {
    return mTransport;
  }
  
  public final PathPermission[] getPathPermissions()
  {
    return mPathPermissions;
  }
  
  public final String getReadPermission()
  {
    return mReadPermission;
  }
  
  public String[] getStreamTypes(Uri paramUri, String paramString)
  {
    return null;
  }
  
  public abstract String getType(Uri paramUri);
  
  public final String getWritePermission()
  {
    return mWritePermission;
  }
  
  public abstract Uri insert(Uri paramUri, ContentValues paramContentValues);
  
  protected boolean isTemporary()
  {
    return false;
  }
  
  protected final boolean matchesOurAuthorities(String paramString)
  {
    if (mAuthority != null) {
      return mAuthority.equals(paramString);
    }
    if (mAuthorities != null)
    {
      int i = mAuthorities.length;
      for (int j = 0; j < i; j++) {
        if (mAuthorities[j].equals(paramString)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {}
  
  public abstract boolean onCreate();
  
  public void onLowMemory() {}
  
  public void onTrimMemory(int paramInt) {}
  
  public AssetFileDescriptor openAssetFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    paramUri = openFile(paramUri, paramString);
    if (paramUri != null) {
      paramUri = new AssetFileDescriptor(paramUri, 0L, -1L);
    } else {
      paramUri = null;
    }
    return paramUri;
  }
  
  public AssetFileDescriptor openAssetFile(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    return openAssetFile(paramUri, paramString);
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    paramString = new StringBuilder();
    paramString.append("No files supported by provider at ");
    paramString.append(paramUri);
    throw new FileNotFoundException(paramString.toString());
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    return openFile(paramUri, paramString);
  }
  
  protected final ParcelFileDescriptor openFileHelper(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    Cursor localCursor = query(paramUri, new String[] { "_data" }, null, null, null);
    if (localCursor != null) {
      i = localCursor.getCount();
    } else {
      i = 0;
    }
    if (i != 1)
    {
      if (localCursor != null) {
        localCursor.close();
      }
      if (i == 0)
      {
        paramString = new StringBuilder();
        paramString.append("No entry for ");
        paramString.append(paramUri);
        throw new FileNotFoundException(paramString.toString());
      }
      paramString = new StringBuilder();
      paramString.append("Multiple items at ");
      paramString.append(paramUri);
      throw new FileNotFoundException(paramString.toString());
    }
    localCursor.moveToFirst();
    int i = localCursor.getColumnIndex("_data");
    if (i >= 0) {
      paramUri = localCursor.getString(i);
    } else {
      paramUri = null;
    }
    localCursor.close();
    if (paramUri != null)
    {
      i = ParcelFileDescriptor.parseMode(paramString);
      return ParcelFileDescriptor.open(new File(paramUri), i);
    }
    throw new FileNotFoundException("Column _data not found.");
  }
  
  public <T> ParcelFileDescriptor openPipeHelper(Uri paramUri, String paramString, Bundle paramBundle, T paramT, PipeDataWriter<T> paramPipeDataWriter)
    throws FileNotFoundException
  {
    try
    {
      ParcelFileDescriptor[] arrayOfParcelFileDescriptor = ParcelFileDescriptor.createPipe();
      AsyncTask local1 = new android/content/ContentProvider$1;
      local1.<init>(this, paramPipeDataWriter, arrayOfParcelFileDescriptor, paramUri, paramString, paramBundle, paramT);
      local1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])null);
      paramUri = arrayOfParcelFileDescriptor[0];
      return paramUri;
    }
    catch (IOException paramUri)
    {
      throw new FileNotFoundException("failure making pipe");
    }
  }
  
  public AssetFileDescriptor openTypedAssetFile(Uri paramUri, String paramString, Bundle paramBundle)
    throws FileNotFoundException
  {
    if ("*/*".equals(paramString)) {
      return openAssetFile(paramUri, "r");
    }
    paramBundle = getType(paramUri);
    if ((paramBundle != null) && (ClipDescription.compareMimeTypes(paramBundle, paramString))) {
      return openAssetFile(paramUri, "r");
    }
    paramBundle = new StringBuilder();
    paramBundle.append("Can't open ");
    paramBundle.append(paramUri);
    paramBundle.append(" as type ");
    paramBundle.append(paramString);
    throw new FileNotFoundException(paramBundle.toString());
  }
  
  public AssetFileDescriptor openTypedAssetFile(Uri paramUri, String paramString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    return openTypedAssetFile(paramUri, paramString, paramBundle);
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
  {
    if (paramBundle == null) {
      paramBundle = Bundle.EMPTY;
    }
    String str1 = paramBundle.getString("android:query-arg-sql-sort-order");
    String str2 = str1;
    if (str1 == null)
    {
      str2 = str1;
      if (paramBundle.containsKey("android:query-arg-sort-columns")) {
        str2 = ContentResolver.createSqlSortClause(paramBundle);
      }
    }
    return query(paramUri, paramArrayOfString, paramBundle.getString("android:query-arg-sql-selection"), paramBundle.getStringArray("android:query-arg-sql-selection-args"), str2, paramCancellationSignal);
  }
  
  public abstract Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2);
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    return query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
  }
  
  public boolean refresh(Uri paramUri, Bundle paramBundle, CancellationSignal paramCancellationSignal)
  {
    return false;
  }
  
  public Uri rejectInsert(Uri paramUri, ContentValues paramContentValues)
  {
    return paramUri.buildUpon().appendPath("0").build();
  }
  
  public final void setAppOps(int paramInt1, int paramInt2)
  {
    if (!mNoPerms)
    {
      mTransport.mReadOp = paramInt1;
      mTransport.mWriteOp = paramInt2;
    }
  }
  
  protected final void setAuthorities(String paramString)
  {
    if (paramString != null) {
      if (paramString.indexOf(';') == -1)
      {
        mAuthority = paramString;
        mAuthorities = null;
      }
      else
      {
        mAuthority = null;
        mAuthorities = paramString.split(";");
      }
    }
  }
  
  protected final void setPathPermissions(PathPermission[] paramArrayOfPathPermission)
  {
    mPathPermissions = paramArrayOfPathPermission;
  }
  
  protected final void setReadPermission(String paramString)
  {
    mReadPermission = paramString;
  }
  
  protected final void setWritePermission(String paramString)
  {
    mWritePermission = paramString;
  }
  
  public void shutdown()
  {
    Log.w("ContentProvider", "implement ContentProvider shutdown() to make sure all database connections are gracefully shutdown");
  }
  
  public Uri uncanonicalize(Uri paramUri)
  {
    return paramUri;
  }
  
  public abstract int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString);
  
  public Uri validateIncomingUri(Uri paramUri)
    throws SecurityException
  {
    Object localObject = paramUri.getAuthority();
    if (!mSingleUser)
    {
      int i = getUserIdFromAuthority((String)localObject, -2);
      if ((i != -2) && (i != mContext.getUserId()))
      {
        paramUri = new StringBuilder();
        paramUri.append("trying to query a ContentProvider in user ");
        paramUri.append(mContext.getUserId());
        paramUri.append(" with a uri belonging to user ");
        paramUri.append(i);
        throw new SecurityException(paramUri.toString());
      }
    }
    if (!matchesOurAuthorities(getAuthorityWithoutUserId((String)localObject)))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("The authority of the uri ");
      ((StringBuilder)localObject).append(paramUri);
      ((StringBuilder)localObject).append(" does not match the one of the contentProvider: ");
      paramUri = ((StringBuilder)localObject).toString();
      if (mAuthority != null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramUri);
        ((StringBuilder)localObject).append(mAuthority);
        paramUri = ((StringBuilder)localObject).toString();
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramUri);
        ((StringBuilder)localObject).append(Arrays.toString(mAuthorities));
        paramUri = ((StringBuilder)localObject).toString();
      }
      throw new SecurityException(paramUri);
    }
    localObject = paramUri.getEncodedPath();
    if ((localObject != null) && (((String)localObject).indexOf("//") != -1))
    {
      localObject = paramUri.buildUpon().encodedPath(((String)localObject).replaceAll("//+", "/")).build();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Normalized ");
      localStringBuilder.append(paramUri);
      localStringBuilder.append(" to ");
      localStringBuilder.append(localObject);
      localStringBuilder.append(" to avoid possible security issues");
      Log.w("ContentProvider", localStringBuilder.toString());
      return localObject;
    }
    return paramUri;
  }
  
  public static abstract interface PipeDataWriter<T>
  {
    public abstract void writeDataToPipe(ParcelFileDescriptor paramParcelFileDescriptor, Uri paramUri, String paramString, Bundle paramBundle, T paramT);
  }
  
  class Transport
    extends ContentProviderNative
  {
    AppOpsManager mAppOpsManager = null;
    int mReadOp = -1;
    int mWriteOp = -1;
    
    Transport() {}
    
    private void enforceFilePermission(String paramString1, Uri paramUri, String paramString2, IBinder paramIBinder)
      throws FileNotFoundException, SecurityException
    {
      if ((paramString2 != null) && (paramString2.indexOf('w') != -1))
      {
        if (enforceWritePermission(paramString1, paramUri, paramIBinder) != 0) {
          throw new FileNotFoundException("App op not allowed");
        }
      }
      else {
        if (enforceReadPermission(paramString1, paramUri, paramIBinder) != 0) {
          break label50;
        }
      }
      return;
      label50:
      throw new FileNotFoundException("App op not allowed");
    }
    
    private int enforceReadPermission(String paramString, Uri paramUri, IBinder paramIBinder)
      throws SecurityException
    {
      int i = enforceReadPermissionInner(paramUri, paramString, paramIBinder);
      if (i != 0) {
        return i;
      }
      if (mReadOp != -1) {
        return mAppOpsManager.noteProxyOp(mReadOp, paramString);
      }
      return 0;
    }
    
    private int enforceWritePermission(String paramString, Uri paramUri, IBinder paramIBinder)
      throws SecurityException
    {
      int i = enforceWritePermissionInner(paramUri, paramString, paramIBinder);
      if (i != 0) {
        return i;
      }
      if (mWriteOp != -1) {
        return mAppOpsManager.noteProxyOp(mWriteOp, paramString);
      }
      return 0;
    }
    
    public ContentProviderResult[] applyBatch(String paramString, ArrayList<ContentProviderOperation> paramArrayList)
      throws OperationApplicationException
    {
      int i = paramArrayList.size();
      int[] arrayOfInt = new int[i];
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        ContentProviderOperation localContentProviderOperation = (ContentProviderOperation)paramArrayList.get(k);
        Object localObject = localContentProviderOperation.getUri();
        arrayOfInt[k] = ContentProvider.getUserIdFromUri((Uri)localObject);
        localObject = validateIncomingUri((Uri)localObject);
        Uri localUri = ContentProvider.this.maybeGetUriWithoutUserId((Uri)localObject);
        localObject = localContentProviderOperation;
        if (!Objects.equals(localContentProviderOperation.getUri(), localUri))
        {
          localObject = new ContentProviderOperation(localContentProviderOperation, localUri);
          paramArrayList.set(k, localObject);
        }
        if ((((ContentProviderOperation)localObject).isReadOperation()) && (enforceReadPermission(paramString, localUri, null) != 0)) {
          throw new OperationApplicationException("App op not allowed", 0);
        }
        if ((((ContentProviderOperation)localObject).isWriteOperation()) && (enforceWritePermission(paramString, localUri, null) != 0)) {
          throw new OperationApplicationException("App op not allowed", 0);
        }
      }
      paramString = ContentProvider.this.setCallingPackage(paramString);
      try
      {
        paramArrayList = applyBatch(paramArrayList);
        if (paramArrayList != null) {
          for (k = j; k < paramArrayList.length; k++) {
            if (arrayOfInt[k] != -2) {
              paramArrayList[k] = new ContentProviderResult(paramArrayList[k], arrayOfInt[k]);
            }
          }
        }
        return paramArrayList;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString);
      }
    }
    
    public int bulkInsert(String paramString, Uri paramUri, ContentValues[] paramArrayOfContentValues)
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      if (enforceWritePermission(paramString, paramUri, null) != 0) {
        return 0;
      }
      paramString = ContentProvider.this.setCallingPackage(paramString);
      try
      {
        int i = bulkInsert(paramUri, paramArrayOfContentValues);
        return i;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString);
      }
    }
    
    public Bundle call(String paramString1, String paramString2, String paramString3, Bundle paramBundle)
    {
      Bundle.setDefusable(paramBundle, true);
      paramString1 = ContentProvider.this.setCallingPackage(paramString1);
      try
      {
        paramString2 = call(paramString2, paramString3, paramBundle);
        return paramString2;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString1);
      }
    }
    
    public Uri canonicalize(String paramString, Uri paramUri)
    {
      paramUri = validateIncomingUri(paramUri);
      int i = ContentProvider.getUserIdFromUri(paramUri);
      paramUri = ContentProvider.getUriWithoutUserId(paramUri);
      if (enforceReadPermission(paramString, paramUri, null) != 0) {
        return null;
      }
      paramString = ContentProvider.this.setCallingPackage(paramString);
      try
      {
        paramUri = ContentProvider.maybeAddUserId(canonicalize(paramUri), i);
        return paramUri;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString);
      }
    }
    
    public ICancellationSignal createCancellationSignal()
    {
      return CancellationSignal.createTransport();
    }
    
    public int delete(String paramString1, Uri paramUri, String paramString2, String[] paramArrayOfString)
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      if (enforceWritePermission(paramString1, paramUri, null) != 0) {
        return 0;
      }
      paramString1 = ContentProvider.this.setCallingPackage(paramString1);
      try
      {
        int i = delete(paramUri, paramString2, paramArrayOfString);
        return i;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString1);
      }
    }
    
    ContentProvider getContentProvider()
    {
      return ContentProvider.this;
    }
    
    public String getProviderName()
    {
      return getContentProvider().getClass().getName();
    }
    
    public String[] getStreamTypes(Uri paramUri, String paramString)
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      return ContentProvider.this.getStreamTypes(paramUri, paramString);
    }
    
    public String getType(Uri paramUri)
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      return ContentProvider.this.getType(paramUri);
    }
    
    public Uri insert(String paramString, Uri paramUri, ContentValues paramContentValues)
    {
      paramUri = validateIncomingUri(paramUri);
      int i = ContentProvider.getUserIdFromUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      if (enforceWritePermission(paramString, paramUri, null) != 0) {
        return rejectInsert(paramUri, paramContentValues);
      }
      paramString = ContentProvider.this.setCallingPackage(paramString);
      if (paramContentValues != null) {
        try
        {
          if ((paramContentValues.get("_data") != null) && (paramContentValues.get("_data").toString().startsWith("storage/emulated")))
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("/");
            localStringBuilder.append(paramContentValues.get("_data").toString());
            paramContentValues.put("_data", localStringBuilder.toString());
          }
        }
        finally
        {
          break label160;
        }
      }
      paramUri = ContentProvider.maybeAddUserId(insert(paramUri, paramContentValues), i);
      ContentProvider.this.setCallingPackage(paramString);
      return paramUri;
      label160:
      ContentProvider.this.setCallingPackage(paramString);
      throw paramUri;
    }
    
    public AssetFileDescriptor openAssetFile(String paramString1, Uri paramUri, String paramString2, ICancellationSignal paramICancellationSignal)
      throws FileNotFoundException
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      enforceFilePermission(paramString1, paramUri, paramString2, null);
      paramString1 = ContentProvider.this.setCallingPackage(paramString1);
      try
      {
        paramUri = openAssetFile(paramUri, paramString2, CancellationSignal.fromTransport(paramICancellationSignal));
        return paramUri;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString1);
      }
    }
    
    public ParcelFileDescriptor openFile(String paramString1, Uri paramUri, String paramString2, ICancellationSignal paramICancellationSignal, IBinder paramIBinder)
      throws FileNotFoundException
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      enforceFilePermission(paramString1, paramUri, paramString2, paramIBinder);
      paramString1 = ContentProvider.this.setCallingPackage(paramString1);
      try
      {
        paramUri = openFile(paramUri, paramString2, CancellationSignal.fromTransport(paramICancellationSignal));
        return paramUri;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString1);
      }
    }
    
    public AssetFileDescriptor openTypedAssetFile(String paramString1, Uri paramUri, String paramString2, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
      throws FileNotFoundException
    {
      Bundle.setDefusable(paramBundle, true);
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      enforceFilePermission(paramString1, paramUri, "r", null);
      paramString1 = ContentProvider.this.setCallingPackage(paramString1);
      try
      {
        paramUri = openTypedAssetFile(paramUri, paramString2, paramBundle, CancellationSignal.fromTransport(paramICancellationSignal));
        return paramUri;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString1);
      }
    }
    
    public Cursor query(String paramString, Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      if (enforceReadPermission(paramString, paramUri, null) != 0)
      {
        if (paramArrayOfString != null) {
          return new MatrixCursor(paramArrayOfString, 0);
        }
        paramString = query(paramUri, paramArrayOfString, paramBundle, CancellationSignal.fromTransport(paramICancellationSignal));
        if (paramString == null) {
          return null;
        }
        return new MatrixCursor(paramString.getColumnNames(), 0);
      }
      paramString = ContentProvider.this.setCallingPackage(paramString);
      try
      {
        paramUri = query(paramUri, paramArrayOfString, paramBundle, CancellationSignal.fromTransport(paramICancellationSignal));
        return paramUri;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString);
      }
    }
    
    public boolean refresh(String paramString, Uri paramUri, Bundle paramBundle, ICancellationSignal paramICancellationSignal)
      throws RemoteException
    {
      paramUri = ContentProvider.getUriWithoutUserId(validateIncomingUri(paramUri));
      if (enforceReadPermission(paramString, paramUri, null) != 0) {
        return false;
      }
      paramString = ContentProvider.this.setCallingPackage(paramString);
      try
      {
        boolean bool = refresh(paramUri, paramBundle, CancellationSignal.fromTransport(paramICancellationSignal));
        return bool;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString);
      }
    }
    
    public Uri uncanonicalize(String paramString, Uri paramUri)
    {
      paramUri = validateIncomingUri(paramUri);
      int i = ContentProvider.getUserIdFromUri(paramUri);
      paramUri = ContentProvider.getUriWithoutUserId(paramUri);
      if (enforceReadPermission(paramString, paramUri, null) != 0) {
        return null;
      }
      paramString = ContentProvider.this.setCallingPackage(paramString);
      try
      {
        paramUri = ContentProvider.maybeAddUserId(uncanonicalize(paramUri), i);
        return paramUri;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString);
      }
    }
    
    public int update(String paramString1, Uri paramUri, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString)
    {
      paramUri = validateIncomingUri(paramUri);
      paramUri = ContentProvider.this.maybeGetUriWithoutUserId(paramUri);
      if (enforceWritePermission(paramString1, paramUri, null) != 0) {
        return 0;
      }
      paramString1 = ContentProvider.this.setCallingPackage(paramString1);
      try
      {
        int i = update(paramUri, paramContentValues, paramString2, paramArrayOfString);
        return i;
      }
      finally
      {
        ContentProvider.this.setCallingPackage(paramString1);
      }
    }
  }
}
