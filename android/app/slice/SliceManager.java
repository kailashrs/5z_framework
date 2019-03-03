package android.app.slice;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.UserHandle;
import android.util.ArraySet;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SliceManager
{
  public static final String ACTION_REQUEST_SLICE_PERMISSION = "com.android.intent.action.REQUEST_SLICE_PERMISSION";
  public static final String CATEGORY_SLICE = "android.app.slice.category.SLICE";
  public static final String SLICE_METADATA_KEY = "android.metadata.SLICE_URI";
  private static final String TAG = "SliceManager";
  private final Context mContext;
  private final ISliceManager mService;
  private final IBinder mToken = new Binder();
  
  public SliceManager(Context paramContext, Handler paramHandler)
    throws ServiceManager.ServiceNotFoundException
  {
    mContext = paramContext;
    mService = ISliceManager.Stub.asInterface(ServiceManager.getServiceOrThrow("slice"));
  }
  
  private String getAuthority(Intent paramIntent)
  {
    paramIntent = new Intent(paramIntent);
    if (!paramIntent.hasCategory("android.app.slice.category.SLICE")) {
      paramIntent.addCategory("android.app.slice.category.SLICE");
    }
    paramIntent = mContext.getPackageManager().queryIntentContentProviders(paramIntent, 0);
    if ((paramIntent != null) && (!paramIntent.isEmpty())) {
      paramIntent = get0providerInfo.authority;
    } else {
      paramIntent = null;
    }
    return paramIntent;
  }
  
  private Uri resolveStatic(Intent paramIntent, ContentResolver paramContentResolver)
  {
    Preconditions.checkNotNull(paramIntent, "intent");
    boolean bool;
    if ((paramIntent.getComponent() == null) && (paramIntent.getPackage() == null) && (paramIntent.getData() == null)) {
      bool = false;
    } else {
      bool = true;
    }
    Preconditions.checkArgument(bool, "Slice intent must be explicit %s", new Object[] { paramIntent });
    Uri localUri = paramIntent.getData();
    if ((localUri != null) && ("vnd.android.slice".equals(paramContentResolver.getType(localUri)))) {
      return localUri;
    }
    paramIntent = mContext.getPackageManager().resolveActivity(paramIntent, 128);
    if ((paramIntent != null) && (activityInfo != null) && (activityInfo.metaData != null) && (activityInfo.metaData.containsKey("android.metadata.SLICE_URI"))) {
      return Uri.parse(activityInfo.metaData.getString("android.metadata.SLICE_URI"));
    }
    return null;
  }
  
  @Deprecated
  public Slice bindSlice(Intent paramIntent, List<SliceSpec> paramList)
  {
    return bindSlice(paramIntent, new ArraySet(paramList));
  }
  
  /* Error */
  public Slice bindSlice(Intent paramIntent, Set<SliceSpec> paramSet)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 120
    //   3: invokestatic 126	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_1
    //   8: invokevirtual 130	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   11: ifnonnull +25 -> 36
    //   14: aload_1
    //   15: invokevirtual 134	android/content/Intent:getPackage	()Ljava/lang/String;
    //   18: ifnonnull +18 -> 36
    //   21: aload_1
    //   22: invokevirtual 138	android/content/Intent:getData	()Landroid/net/Uri;
    //   25: ifnull +6 -> 31
    //   28: goto +8 -> 36
    //   31: iconst_0
    //   32: istore_3
    //   33: goto +5 -> 38
    //   36: iconst_1
    //   37: istore_3
    //   38: iload_3
    //   39: ldc -116
    //   41: iconst_1
    //   42: anewarray 4	java/lang/Object
    //   45: dup
    //   46: iconst_0
    //   47: aload_1
    //   48: aastore
    //   49: invokestatic 144	com/android/internal/util/Preconditions:checkArgument	(ZLjava/lang/String;[Ljava/lang/Object;)V
    //   52: aload_0
    //   53: getfield 51	android/app/slice/SliceManager:mContext	Landroid/content/Context;
    //   56: invokevirtual 207	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   59: astore 4
    //   61: aload_0
    //   62: aload_1
    //   63: aload 4
    //   65: invokespecial 209	android/app/slice/SliceManager:resolveStatic	(Landroid/content/Intent;Landroid/content/ContentResolver;)Landroid/net/Uri;
    //   68: astore 5
    //   70: aload 5
    //   72: ifnull +11 -> 83
    //   75: aload_0
    //   76: aload 5
    //   78: aload_2
    //   79: invokevirtual 212	android/app/slice/SliceManager:bindSlice	(Landroid/net/Uri;Ljava/util/Set;)Landroid/app/slice/Slice;
    //   82: areturn
    //   83: aload_0
    //   84: aload_1
    //   85: invokespecial 214	android/app/slice/SliceManager:getAuthority	(Landroid/content/Intent;)Ljava/lang/String;
    //   88: astore_2
    //   89: aload_2
    //   90: ifnonnull +5 -> 95
    //   93: aconst_null
    //   94: areturn
    //   95: new 216	android/net/Uri$Builder
    //   98: dup
    //   99: invokespecial 217	android/net/Uri$Builder:<init>	()V
    //   102: ldc -37
    //   104: invokevirtual 223	android/net/Uri$Builder:scheme	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   107: aload_2
    //   108: invokevirtual 225	android/net/Uri$Builder:authority	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   111: invokevirtual 228	android/net/Uri$Builder:build	()Landroid/net/Uri;
    //   114: astore_2
    //   115: aload 4
    //   117: aload_2
    //   118: invokevirtual 232	android/content/ContentResolver:acquireUnstableContentProviderClient	(Landroid/net/Uri;)Landroid/content/ContentProviderClient;
    //   121: astore 4
    //   123: aload 4
    //   125: ifnonnull +45 -> 170
    //   128: ldc 17
    //   130: ldc -22
    //   132: iconst_1
    //   133: anewarray 4	java/lang/Object
    //   136: dup
    //   137: iconst_0
    //   138: aload_2
    //   139: aastore
    //   140: invokestatic 238	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   143: invokestatic 244	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   146: pop
    //   147: aload 4
    //   149: ifnull +9 -> 158
    //   152: aconst_null
    //   153: aload 4
    //   155: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   158: aconst_null
    //   159: areturn
    //   160: astore_2
    //   161: aconst_null
    //   162: astore_1
    //   163: goto +75 -> 238
    //   166: astore_1
    //   167: goto +68 -> 235
    //   170: new 174	android/os/Bundle
    //   173: astore_2
    //   174: aload_2
    //   175: invokespecial 247	android/os/Bundle:<init>	()V
    //   178: aload_2
    //   179: ldc -7
    //   181: aload_1
    //   182: invokevirtual 253	android/os/Bundle:putParcelable	(Ljava/lang/String;Landroid/os/Parcelable;)V
    //   185: aload 4
    //   187: ldc -1
    //   189: aconst_null
    //   190: aload_2
    //   191: invokevirtual 261	android/content/ContentProviderClient:call	(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;
    //   194: astore_1
    //   195: aload_1
    //   196: ifnonnull +16 -> 212
    //   199: aload 4
    //   201: ifnull +9 -> 210
    //   204: aconst_null
    //   205: aload 4
    //   207: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   210: aconst_null
    //   211: areturn
    //   212: aload_1
    //   213: ldc 53
    //   215: invokevirtual 265	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   218: checkcast 267	android/app/slice/Slice
    //   221: astore_1
    //   222: aload 4
    //   224: ifnull +9 -> 233
    //   227: aconst_null
    //   228: aload 4
    //   230: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   233: aload_1
    //   234: areturn
    //   235: aload_1
    //   236: athrow
    //   237: astore_2
    //   238: aload 4
    //   240: ifnull +9 -> 249
    //   243: aload_1
    //   244: aload 4
    //   246: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   249: aload_2
    //   250: athrow
    //   251: astore_1
    //   252: aconst_null
    //   253: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	254	0	this	SliceManager
    //   0	254	1	paramIntent	Intent
    //   0	254	2	paramSet	Set<SliceSpec>
    //   32	7	3	bool	boolean
    //   59	186	4	localObject	Object
    //   68	9	5	localUri	Uri
    // Exception table:
    //   from	to	target	type
    //   128	147	160	finally
    //   170	195	160	finally
    //   212	222	160	finally
    //   128	147	166	java/lang/Throwable
    //   170	195	166	java/lang/Throwable
    //   212	222	166	java/lang/Throwable
    //   235	237	237	finally
    //   115	123	251	android/os/RemoteException
    //   152	158	251	android/os/RemoteException
    //   204	210	251	android/os/RemoteException
    //   227	233	251	android/os/RemoteException
    //   243	249	251	android/os/RemoteException
    //   249	251	251	android/os/RemoteException
  }
  
  @Deprecated
  public Slice bindSlice(Uri paramUri, List<SliceSpec> paramList)
  {
    return bindSlice(paramUri, new ArraySet(paramList));
  }
  
  /* Error */
  public Slice bindSlice(Uri paramUri, Set<SliceSpec> paramSet)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 272
    //   4: invokestatic 126	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_0
    //   9: getfield 51	android/app/slice/SliceManager:mContext	Landroid/content/Context;
    //   12: invokevirtual 207	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   15: astore_3
    //   16: aload_3
    //   17: aload_1
    //   18: invokevirtual 232	android/content/ContentResolver:acquireUnstableContentProviderClient	(Landroid/net/Uri;)Landroid/content/ContentProviderClient;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnonnull +43 -> 66
    //   26: ldc 17
    //   28: ldc -22
    //   30: iconst_1
    //   31: anewarray 4	java/lang/Object
    //   34: dup
    //   35: iconst_0
    //   36: aload_1
    //   37: aastore
    //   38: invokestatic 238	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   41: invokestatic 244	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   44: pop
    //   45: aload_3
    //   46: ifnull +8 -> 54
    //   49: aconst_null
    //   50: aload_3
    //   51: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   54: aconst_null
    //   55: areturn
    //   56: astore_2
    //   57: aconst_null
    //   58: astore_1
    //   59: goto +100 -> 159
    //   62: astore_1
    //   63: goto +93 -> 156
    //   66: new 174	android/os/Bundle
    //   69: astore 4
    //   71: aload 4
    //   73: invokespecial 247	android/os/Bundle:<init>	()V
    //   76: aload 4
    //   78: ldc_w 274
    //   81: aload_1
    //   82: invokevirtual 253	android/os/Bundle:putParcelable	(Ljava/lang/String;Landroid/os/Parcelable;)V
    //   85: new 276	java/util/ArrayList
    //   88: astore_1
    //   89: aload_1
    //   90: aload_2
    //   91: invokespecial 277	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
    //   94: aload 4
    //   96: ldc_w 279
    //   99: aload_1
    //   100: invokevirtual 283	android/os/Bundle:putParcelableArrayList	(Ljava/lang/String;Ljava/util/ArrayList;)V
    //   103: aload_3
    //   104: ldc_w 285
    //   107: aconst_null
    //   108: aload 4
    //   110: invokevirtual 261	android/content/ContentProviderClient:call	(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;
    //   113: astore_1
    //   114: aload_1
    //   115: iconst_1
    //   116: invokestatic 289	android/os/Bundle:setDefusable	(Landroid/os/Bundle;Z)Landroid/os/Bundle;
    //   119: pop
    //   120: aload_1
    //   121: ifnonnull +14 -> 135
    //   124: aload_3
    //   125: ifnull +8 -> 133
    //   128: aconst_null
    //   129: aload_3
    //   130: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   133: aconst_null
    //   134: areturn
    //   135: aload_1
    //   136: ldc 53
    //   138: invokevirtual 265	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   141: checkcast 267	android/app/slice/Slice
    //   144: astore_1
    //   145: aload_3
    //   146: ifnull +8 -> 154
    //   149: aconst_null
    //   150: aload_3
    //   151: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   154: aload_1
    //   155: areturn
    //   156: aload_1
    //   157: athrow
    //   158: astore_2
    //   159: aload_3
    //   160: ifnull +8 -> 168
    //   163: aload_1
    //   164: aload_3
    //   165: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   168: aload_2
    //   169: athrow
    //   170: astore_1
    //   171: aconst_null
    //   172: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	173	0	this	SliceManager
    //   0	173	1	paramUri	Uri
    //   0	173	2	paramSet	Set<SliceSpec>
    //   15	150	3	localObject	Object
    //   69	40	4	localBundle	Bundle
    // Exception table:
    //   from	to	target	type
    //   26	45	56	finally
    //   66	120	56	finally
    //   135	145	56	finally
    //   26	45	62	java/lang/Throwable
    //   66	120	62	java/lang/Throwable
    //   135	145	62	java/lang/Throwable
    //   156	158	158	finally
    //   16	22	170	android/os/RemoteException
    //   49	54	170	android/os/RemoteException
    //   128	133	170	android/os/RemoteException
    //   149	154	170	android/os/RemoteException
    //   163	168	170	android/os/RemoteException
    //   168	170	170	android/os/RemoteException
  }
  
  public int checkSlicePermission(Uri paramUri, int paramInt1, int paramInt2)
  {
    try
    {
      paramInt1 = mService.checkSlicePermission(paramUri, null, paramInt1, paramInt2, null);
      return paramInt1;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public void enforceSlicePermission(Uri paramUri, String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    try
    {
      if (UserHandle.isSameApp(paramInt2, Process.myUid())) {
        return;
      }
      if (paramString != null)
      {
        if (mService.checkSlicePermission(paramUri, paramString, paramInt1, paramInt2, paramArrayOfString) != -1) {
          return;
        }
        paramArrayOfString = new java/lang/SecurityException;
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("User ");
        paramString.append(paramInt2);
        paramString.append(" does not have slice permission for ");
        paramString.append(paramUri);
        paramString.append(".");
        paramArrayOfString.<init>(paramString.toString());
        throw paramArrayOfString;
      }
      paramUri = new java/lang/SecurityException;
      paramUri.<init>("No pkg specified");
      throw paramUri;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public List<Uri> getPinnedSlices()
  {
    try
    {
      List localList = Arrays.asList(mService.getPinnedSlices(mContext.getPackageName()));
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Set<SliceSpec> getPinnedSpecs(Uri paramUri)
  {
    try
    {
      paramUri = new ArraySet(Arrays.asList(mService.getPinnedSpecs(paramUri, mContext.getPackageName())));
      return paramUri;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public java.util.Collection<Uri> getSliceDescendants(Uri paramUri)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 51	android/app/slice/SliceManager:mContext	Landroid/content/Context;
    //   4: invokevirtual 207	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   7: astore_2
    //   8: aload_2
    //   9: aload_1
    //   10: invokevirtual 232	android/content/ContentResolver:acquireUnstableContentProviderClient	(Landroid/net/Uri;)Landroid/content/ContentProviderClient;
    //   13: astore_3
    //   14: aconst_null
    //   15: astore 4
    //   17: aload 4
    //   19: astore_2
    //   20: new 174	android/os/Bundle
    //   23: astore 5
    //   25: aload 4
    //   27: astore_2
    //   28: aload 5
    //   30: invokespecial 247	android/os/Bundle:<init>	()V
    //   33: aload 4
    //   35: astore_2
    //   36: aload 5
    //   38: ldc_w 274
    //   41: aload_1
    //   42: invokevirtual 253	android/os/Bundle:putParcelable	(Ljava/lang/String;Landroid/os/Parcelable;)V
    //   45: aload 4
    //   47: astore_2
    //   48: aload_3
    //   49: ldc_w 369
    //   52: aconst_null
    //   53: aload 5
    //   55: invokevirtual 261	android/content/ContentProviderClient:call	(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;
    //   58: ldc_w 371
    //   61: invokevirtual 375	android/os/Bundle:getParcelableArrayList	(Ljava/lang/String;)Ljava/util/ArrayList;
    //   64: astore_1
    //   65: aload_3
    //   66: ifnull +8 -> 74
    //   69: aconst_null
    //   70: aload_3
    //   71: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   74: aload_1
    //   75: areturn
    //   76: astore_1
    //   77: goto +8 -> 85
    //   80: astore_1
    //   81: aload_1
    //   82: astore_2
    //   83: aload_1
    //   84: athrow
    //   85: aload_3
    //   86: ifnull +8 -> 94
    //   89: aload_2
    //   90: aload_3
    //   91: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   94: aload_1
    //   95: athrow
    //   96: astore_1
    //   97: ldc 17
    //   99: ldc_w 377
    //   102: aload_1
    //   103: invokestatic 381	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   106: pop
    //   107: invokestatic 386	java/util/Collections:emptyList	()Ljava/util/List;
    //   110: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	111	0	this	SliceManager
    //   0	111	1	paramUri	Uri
    //   7	83	2	localObject1	Object
    //   13	78	3	localContentProviderClient	android.content.ContentProviderClient
    //   15	31	4	localObject2	Object
    //   23	31	5	localBundle	Bundle
    // Exception table:
    //   from	to	target	type
    //   20	25	76	finally
    //   28	33	76	finally
    //   36	45	76	finally
    //   48	65	76	finally
    //   83	85	76	finally
    //   20	25	80	java/lang/Throwable
    //   28	33	80	java/lang/Throwable
    //   36	45	80	java/lang/Throwable
    //   48	65	80	java/lang/Throwable
    //   8	14	96	android/os/RemoteException
    //   69	74	96	android/os/RemoteException
    //   89	94	96	android/os/RemoteException
    //   94	96	96	android/os/RemoteException
  }
  
  public void grantPermissionFromUser(Uri paramUri, String paramString, boolean paramBoolean)
  {
    try
    {
      mService.grantPermissionFromUser(paramUri, paramString, mContext.getPackageName(), paramBoolean);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public void grantSlicePermission(String paramString, Uri paramUri)
  {
    try
    {
      mService.grantSlicePermission(mContext.getPackageName(), paramString, paramUri);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean hasSliceAccess()
  {
    try
    {
      boolean bool = mService.hasSliceAccess(mContext.getPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public Uri mapIntentToUri(Intent paramIntent)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 51	android/app/slice/SliceManager:mContext	Landroid/content/Context;
    //   4: invokevirtual 207	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   7: astore_2
    //   8: aload_0
    //   9: aload_1
    //   10: aload_2
    //   11: invokespecial 209	android/app/slice/SliceManager:resolveStatic	(Landroid/content/Intent;Landroid/content/ContentResolver;)Landroid/net/Uri;
    //   14: astore_3
    //   15: aload_3
    //   16: ifnull +5 -> 21
    //   19: aload_3
    //   20: areturn
    //   21: aload_0
    //   22: aload_1
    //   23: invokespecial 214	android/app/slice/SliceManager:getAuthority	(Landroid/content/Intent;)Ljava/lang/String;
    //   26: astore_3
    //   27: aload_3
    //   28: ifnonnull +5 -> 33
    //   31: aconst_null
    //   32: areturn
    //   33: new 216	android/net/Uri$Builder
    //   36: dup
    //   37: invokespecial 217	android/net/Uri$Builder:<init>	()V
    //   40: ldc -37
    //   42: invokevirtual 223	android/net/Uri$Builder:scheme	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   45: aload_3
    //   46: invokevirtual 225	android/net/Uri$Builder:authority	(Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   49: invokevirtual 228	android/net/Uri$Builder:build	()Landroid/net/Uri;
    //   52: astore 4
    //   54: aload_2
    //   55: aload 4
    //   57: invokevirtual 232	android/content/ContentResolver:acquireUnstableContentProviderClient	(Landroid/net/Uri;)Landroid/content/ContentProviderClient;
    //   60: astore_3
    //   61: aload_3
    //   62: ifnonnull +44 -> 106
    //   65: ldc 17
    //   67: ldc -22
    //   69: iconst_1
    //   70: anewarray 4	java/lang/Object
    //   73: dup
    //   74: iconst_0
    //   75: aload 4
    //   77: aastore
    //   78: invokestatic 238	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   81: invokestatic 244	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   84: pop
    //   85: aload_3
    //   86: ifnull +8 -> 94
    //   89: aconst_null
    //   90: aload_3
    //   91: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   94: aconst_null
    //   95: areturn
    //   96: astore_2
    //   97: aconst_null
    //   98: astore_1
    //   99: goto +71 -> 170
    //   102: astore_1
    //   103: goto +64 -> 167
    //   106: new 174	android/os/Bundle
    //   109: astore_2
    //   110: aload_2
    //   111: invokespecial 247	android/os/Bundle:<init>	()V
    //   114: aload_2
    //   115: ldc -7
    //   117: aload_1
    //   118: invokevirtual 253	android/os/Bundle:putParcelable	(Ljava/lang/String;Landroid/os/Parcelable;)V
    //   121: aload_3
    //   122: ldc_w 404
    //   125: aconst_null
    //   126: aload_2
    //   127: invokevirtual 261	android/content/ContentProviderClient:call	(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;
    //   130: astore_1
    //   131: aload_1
    //   132: ifnonnull +14 -> 146
    //   135: aload_3
    //   136: ifnull +8 -> 144
    //   139: aconst_null
    //   140: aload_3
    //   141: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   144: aconst_null
    //   145: areturn
    //   146: aload_1
    //   147: ldc 53
    //   149: invokevirtual 265	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   152: checkcast 183	android/net/Uri
    //   155: astore_1
    //   156: aload_3
    //   157: ifnull +8 -> 165
    //   160: aconst_null
    //   161: aload_3
    //   162: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   165: aload_1
    //   166: areturn
    //   167: aload_1
    //   168: athrow
    //   169: astore_2
    //   170: aload_3
    //   171: ifnull +8 -> 179
    //   174: aload_1
    //   175: aload_3
    //   176: invokestatic 246	android/app/slice/SliceManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   179: aload_2
    //   180: athrow
    //   181: astore_1
    //   182: aconst_null
    //   183: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	184	0	this	SliceManager
    //   0	184	1	paramIntent	Intent
    //   7	48	2	localContentResolver	ContentResolver
    //   96	1	2	localObject1	Object
    //   109	18	2	localBundle	Bundle
    //   169	11	2	localObject2	Object
    //   14	162	3	localObject3	Object
    //   52	24	4	localUri	Uri
    // Exception table:
    //   from	to	target	type
    //   65	85	96	finally
    //   106	131	96	finally
    //   146	156	96	finally
    //   65	85	102	java/lang/Throwable
    //   106	131	102	java/lang/Throwable
    //   146	156	102	java/lang/Throwable
    //   167	169	169	finally
    //   54	61	181	android/os/RemoteException
    //   89	94	181	android/os/RemoteException
    //   139	144	181	android/os/RemoteException
    //   160	165	181	android/os/RemoteException
    //   174	179	181	android/os/RemoteException
    //   179	181	181	android/os/RemoteException
  }
  
  @Deprecated
  public void pinSlice(Uri paramUri, List<SliceSpec> paramList)
  {
    pinSlice(paramUri, new ArraySet(paramList));
  }
  
  public void pinSlice(Uri paramUri, Set<SliceSpec> paramSet)
  {
    try
    {
      mService.pinSlice(mContext.getPackageName(), paramUri, (SliceSpec[])paramSet.toArray(new SliceSpec[paramSet.size()]), mToken);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public void revokeSlicePermission(String paramString, Uri paramUri)
  {
    try
    {
      mService.revokeSlicePermission(mContext.getPackageName(), paramString, paramUri);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void unpinSlice(Uri paramUri)
  {
    try
    {
      mService.unpinSlice(mContext.getPackageName(), paramUri, mToken);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
}
