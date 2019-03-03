package android.app.slice;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.ArraySet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class SliceProvider
  extends ContentProvider
{
  private static final boolean DEBUG = false;
  public static final String EXTRA_BIND_URI = "slice_uri";
  public static final String EXTRA_INTENT = "slice_intent";
  public static final String EXTRA_PKG = "pkg";
  public static final String EXTRA_PROVIDER_PKG = "provider_pkg";
  public static final String EXTRA_RESULT = "result";
  public static final String EXTRA_SLICE = "slice";
  public static final String EXTRA_SLICE_DESCENDANTS = "slice_descendants";
  public static final String EXTRA_SUPPORTED_SPECS = "supported_specs";
  public static final String METHOD_GET_DESCENDANTS = "get_descendants";
  public static final String METHOD_GET_PERMISSIONS = "get_permissions";
  public static final String METHOD_MAP_INTENT = "map_slice";
  public static final String METHOD_MAP_ONLY_INTENT = "map_only";
  public static final String METHOD_PIN = "pin";
  public static final String METHOD_SLICE = "bind_slice";
  public static final String METHOD_UNPIN = "unpin";
  private static final long SLICE_BIND_ANR = 2000L;
  public static final String SLICE_TYPE = "vnd.android.slice";
  private static final String TAG = "SliceProvider";
  private final Runnable mAnr = new _..Lambda.SliceProvider.bIgM5f4PsMvz_YYWEeFTjvTqevw(this);
  private final String[] mAutoGrantPermissions;
  private String mCallback;
  private SliceManager mSliceManager;
  
  public SliceProvider()
  {
    mAutoGrantPermissions = new String[0];
  }
  
  public SliceProvider(String... paramVarArgs)
  {
    mAutoGrantPermissions = paramVarArgs;
  }
  
  public static PendingIntent createPermissionIntent(Context paramContext, Uri paramUri, String paramString)
  {
    Intent localIntent = new Intent("com.android.intent.action.REQUEST_SLICE_PERMISSION");
    localIntent.setComponent(new ComponentName("com.android.systemui", "com.android.systemui.SlicePermissionActivity"));
    localIntent.putExtra("slice_uri", paramUri);
    localIntent.putExtra("pkg", paramString);
    localIntent.putExtra("provider_pkg", paramContext.getPackageName());
    localIntent.setData(paramUri.buildUpon().appendQueryParameter("package", paramString).build());
    return PendingIntent.getActivity(paramContext, 0, localIntent, 0);
  }
  
  public static CharSequence getPermissionString(Context paramContext, String paramString)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    try
    {
      paramContext = paramContext.getString(17041024, new Object[] { localPackageManager.getApplicationInfo(paramString, 0).loadLabel(localPackageManager), paramContext.getApplicationInfo().loadLabel(localPackageManager) });
      return paramContext;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      throw new RuntimeException("Unknown calling app", paramContext);
    }
  }
  
  private Slice handleBindSlice(Uri paramUri, List<SliceSpec> paramList, String paramString, int paramInt1, int paramInt2)
  {
    if (paramString == null) {
      paramString = getContext().getPackageManager().getNameForUid(paramInt1);
    }
    try
    {
      mSliceManager.enforceSlicePermission(paramUri, paramString, paramInt2, paramInt1, mAutoGrantPermissions);
      mCallback = "onBindSlice";
      Handler.getMain().postDelayed(mAnr, 2000L);
      try
      {
        paramUri = onBindSliceStrict(paramUri, paramList);
        return paramUri;
      }
      finally
      {
        Handler.getMain().removeCallbacks(mAnr);
      }
      return createPermissionSlice(getContext(), paramUri, paramString);
    }
    catch (SecurityException paramList) {}
  }
  
  private Collection<Uri> handleGetDescendants(Uri paramUri)
  {
    mCallback = "onGetSliceDescendants";
    return onGetSliceDescendants(paramUri);
  }
  
  private void handlePinSlice(Uri paramUri)
  {
    mCallback = "onSlicePinned";
    Handler.getMain().postDelayed(mAnr, 2000L);
    try
    {
      onSlicePinned(paramUri);
      return;
    }
    finally
    {
      Handler.getMain().removeCallbacks(mAnr);
    }
  }
  
  private void handleUnpinSlice(Uri paramUri)
  {
    mCallback = "onSliceUnpinned";
    Handler.getMain().postDelayed(mAnr, 2000L);
    try
    {
      onSliceUnpinned(paramUri);
      return;
    }
    finally
    {
      Handler.getMain().removeCallbacks(mAnr);
    }
  }
  
  private Slice onBindSliceStrict(Uri paramUri, List<SliceSpec> paramList)
  {
    StrictMode.ThreadPolicy localThreadPolicy = StrictMode.getThreadPolicy();
    try
    {
      Object localObject = new android/os/StrictMode$ThreadPolicy$Builder;
      ((StrictMode.ThreadPolicy.Builder)localObject).<init>();
      StrictMode.setThreadPolicy(((StrictMode.ThreadPolicy.Builder)localObject).detectAll().penaltyDeath().build());
      localObject = new android/util/ArraySet;
      ((ArraySet)localObject).<init>(paramList);
      paramUri = onBindSlice(paramUri, (Set)localObject);
      return paramUri;
    }
    finally
    {
      StrictMode.setThreadPolicy(localThreadPolicy);
    }
  }
  
  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo)
  {
    super.attachInfo(paramContext, paramProviderInfo);
    mSliceManager = ((SliceManager)paramContext.getSystemService(SliceManager.class));
  }
  
  public Bundle call(String paramString1, String paramString2, Bundle paramBundle)
  {
    if (paramString1.equals("bind_slice"))
    {
      paramString1 = handleBindSlice(getUriWithoutUserId((Uri)paramBundle.getParcelable("slice_uri")), paramBundle.getParcelableArrayList("supported_specs"), getCallingPackage(), Binder.getCallingUid(), Binder.getCallingPid());
      paramString2 = new Bundle();
      paramString2.putParcelable("slice", paramString1);
      return paramString2;
    }
    if (paramString1.equals("map_slice"))
    {
      paramString1 = (Intent)paramBundle.getParcelable("slice_intent");
      if (paramString1 == null) {
        return null;
      }
      paramString1 = onMapIntentToUri(paramString1);
      paramString2 = paramBundle.getParcelableArrayList("supported_specs");
      paramBundle = new Bundle();
      if (paramString1 != null) {
        paramBundle.putParcelable("slice", handleBindSlice(paramString1, paramString2, getCallingPackage(), Binder.getCallingUid(), Binder.getCallingPid()));
      } else {
        paramBundle.putParcelable("slice", null);
      }
      return paramBundle;
    }
    if (paramString1.equals("map_only"))
    {
      paramString1 = (Intent)paramBundle.getParcelable("slice_intent");
      if (paramString1 == null) {
        return null;
      }
      paramString1 = onMapIntentToUri(paramString1);
      paramString2 = new Bundle();
      paramString2.putParcelable("slice", paramString1);
      return paramString2;
    }
    Uri localUri;
    if (paramString1.equals("pin"))
    {
      localUri = getUriWithoutUserId((Uri)paramBundle.getParcelable("slice_uri"));
      if (Binder.getCallingUid() == 1000) {
        handlePinSlice(localUri);
      } else {
        throw new SecurityException("Only the system can pin/unpin slices");
      }
    }
    else if (paramString1.equals("unpin"))
    {
      localUri = getUriWithoutUserId((Uri)paramBundle.getParcelable("slice_uri"));
      if (Binder.getCallingUid() == 1000) {
        handleUnpinSlice(localUri);
      } else {
        throw new SecurityException("Only the system can pin/unpin slices");
      }
    }
    else
    {
      if (paramString1.equals("get_descendants"))
      {
        paramString1 = getUriWithoutUserId((Uri)paramBundle.getParcelable("slice_uri"));
        paramString2 = new Bundle();
        paramString2.putParcelableArrayList("slice_descendants", new ArrayList(handleGetDescendants(paramString1)));
        return paramString2;
      }
      if (paramString1.equals("get_permissions"))
      {
        if (Binder.getCallingUid() == 1000)
        {
          paramString1 = new Bundle();
          paramString1.putStringArray("result", mAutoGrantPermissions);
          return paramString1;
        }
        throw new SecurityException("Only the system can get permissions");
      }
    }
    return super.call(paramString1, paramString2, paramBundle);
  }
  
  public Slice createPermissionSlice(Context paramContext, Uri paramUri, String paramString)
  {
    mCallback = "onCreatePermissionRequest";
    Handler.getMain().postDelayed(mAnr, 2000L);
    try
    {
      Object localObject = onCreatePermissionRequest(paramUri);
      Handler.getMain().removeCallbacks(mAnr);
      Slice.Builder localBuilder1 = new Slice.Builder(paramUri);
      Slice.Builder localBuilder2 = new Slice.Builder(localBuilder1).addIcon(Icon.createWithResource(paramContext, 17302991), null, Collections.emptyList()).addHints(Arrays.asList(new String[] { "title", "shortcut" })).addAction((PendingIntent)localObject, new Slice.Builder(localBuilder1).build(), null);
      localObject = new TypedValue();
      new ContextThemeWrapper(paramContext, 16974123).getTheme().resolveAttribute(16843829, (TypedValue)localObject, true);
      int i = data;
      localBuilder1.addSubSlice(new Slice.Builder(paramUri.buildUpon().appendPath("permission").build()).addIcon(Icon.createWithResource(paramContext, 17302533), null, Collections.emptyList()).addText(getPermissionString(paramContext, paramString), null, Collections.emptyList()).addInt(i, "color", Collections.emptyList()).addSubSlice(localBuilder2.build(), null).build(), null);
      return localBuilder1.addHints(Arrays.asList(new String[] { "permission_request" })).build();
    }
    finally
    {
      Handler.getMain().removeCallbacks(mAnr);
    }
  }
  
  public final int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }
  
  public final String getType(Uri paramUri)
  {
    return "vnd.android.slice";
  }
  
  public final Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    return null;
  }
  
  @Deprecated
  public Slice onBindSlice(Uri paramUri, List<SliceSpec> paramList)
  {
    return null;
  }
  
  public Slice onBindSlice(Uri paramUri, Set<SliceSpec> paramSet)
  {
    return onBindSlice(paramUri, new ArrayList(paramSet));
  }
  
  public PendingIntent onCreatePermissionRequest(Uri paramUri)
  {
    return createPermissionIntent(getContext(), paramUri, getCallingPackage());
  }
  
  public Collection<Uri> onGetSliceDescendants(Uri paramUri)
  {
    return Collections.emptyList();
  }
  
  public Uri onMapIntentToUri(Intent paramIntent)
  {
    throw new UnsupportedOperationException("This provider has not implemented intent to uri mapping");
  }
  
  public void onSlicePinned(Uri paramUri) {}
  
  public void onSliceUnpinned(Uri paramUri) {}
  
  public final Cursor query(Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
  {
    return null;
  }
  
  public final Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    return null;
  }
  
  public final Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    return null;
  }
  
  public final int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }
}
