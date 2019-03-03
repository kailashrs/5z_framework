package android.provider;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ParcelableException;
import android.os.RemoteException;
import android.util.DataUnit;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class DocumentsContract
{
  public static final String ACTION_DOCUMENT_ROOT_SETTINGS = "android.provider.action.DOCUMENT_ROOT_SETTINGS";
  public static final String ACTION_DOCUMENT_SETTINGS = "android.provider.action.DOCUMENT_SETTINGS";
  public static final String ACTION_MANAGE_DOCUMENT = "android.provider.action.MANAGE_DOCUMENT";
  public static final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";
  public static final String EXTRA_ERROR = "error";
  public static final String EXTRA_EXCLUDE_SELF = "android.provider.extra.EXCLUDE_SELF";
  public static final String EXTRA_INFO = "info";
  public static final String EXTRA_INITIAL_URI = "android.provider.extra.INITIAL_URI";
  public static final String EXTRA_LOADING = "loading";
  public static final String EXTRA_OPTIONS = "options";
  public static final String EXTRA_ORIENTATION = "android.provider.extra.ORIENTATION";
  public static final String EXTRA_PACKAGE_NAME = "android.content.extra.PACKAGE_NAME";
  public static final String EXTRA_PARENT_URI = "parentUri";
  public static final String EXTRA_PROMPT = "android.provider.extra.PROMPT";
  public static final String EXTRA_RESULT = "result";
  public static final String EXTRA_SHOW_ADVANCED = "android.content.extra.SHOW_ADVANCED";
  public static final String EXTRA_TARGET_URI = "android.content.extra.TARGET_URI";
  public static final String EXTRA_URI = "uri";
  public static final String METADATA_EXIF = "android:documentExif";
  public static final String METADATA_TYPES = "android:documentMetadataType";
  public static final String METHOD_COPY_DOCUMENT = "android:copyDocument";
  public static final String METHOD_CREATE_DOCUMENT = "android:createDocument";
  public static final String METHOD_CREATE_WEB_LINK_INTENT = "android:createWebLinkIntent";
  public static final String METHOD_DELETE_DOCUMENT = "android:deleteDocument";
  public static final String METHOD_EJECT_ROOT = "android:ejectRoot";
  public static final String METHOD_FIND_DOCUMENT_PATH = "android:findDocumentPath";
  public static final String METHOD_GET_DOCUMENT_METADATA = "android:getDocumentMetadata";
  public static final String METHOD_IS_CHILD_DOCUMENT = "android:isChildDocument";
  public static final String METHOD_MOVE_DOCUMENT = "android:moveDocument";
  public static final String METHOD_REMOVE_DOCUMENT = "android:removeDocument";
  public static final String METHOD_RENAME_DOCUMENT = "android:renameDocument";
  public static final String PACKAGE_DOCUMENTS_UI = "com.android.documentsui";
  private static final String PARAM_MANAGE = "manage";
  private static final String PARAM_QUERY = "query";
  private static final String PATH_CHILDREN = "children";
  private static final String PATH_DOCUMENT = "document";
  private static final String PATH_RECENT = "recent";
  private static final String PATH_ROOT = "root";
  private static final String PATH_SEARCH = "search";
  public static final String PATH_TREE = "tree";
  public static final String PROVIDER_INTERFACE = "android.content.action.DOCUMENTS_PROVIDER";
  private static final String TAG = "DocumentsContract";
  private static final int THUMBNAIL_BUFFER_SIZE = (int)DataUnit.KIBIBYTES.toBytes(128L);
  
  private DocumentsContract() {}
  
  public static Uri buildChildDocumentsUri(String paramString1, String paramString2)
  {
    return new Uri.Builder().scheme("content").authority(paramString1).appendPath("document").appendPath(paramString2).appendPath("children").build();
  }
  
  public static Uri buildChildDocumentsUriUsingTree(Uri paramUri, String paramString)
  {
    return new Uri.Builder().scheme("content").authority(paramUri.getAuthority()).appendPath("tree").appendPath(getTreeDocumentId(paramUri)).appendPath("document").appendPath(paramString).appendPath("children").build();
  }
  
  public static Uri buildDocumentUri(String paramString1, String paramString2)
  {
    return new Uri.Builder().scheme("content").authority(paramString1).appendPath("document").appendPath(paramString2).build();
  }
  
  public static Uri buildDocumentUriMaybeUsingTree(Uri paramUri, String paramString)
  {
    if (isTreeUri(paramUri)) {
      return buildDocumentUriUsingTree(paramUri, paramString);
    }
    return buildDocumentUri(paramUri.getAuthority(), paramString);
  }
  
  public static Uri buildDocumentUriUsingTree(Uri paramUri, String paramString)
  {
    return new Uri.Builder().scheme("content").authority(paramUri.getAuthority()).appendPath("tree").appendPath(getTreeDocumentId(paramUri)).appendPath("document").appendPath(paramString).build();
  }
  
  public static Uri buildHomeUri()
  {
    return buildRootUri("com.android.externalstorage.documents", "home");
  }
  
  public static Uri buildRecentDocumentsUri(String paramString1, String paramString2)
  {
    return new Uri.Builder().scheme("content").authority(paramString1).appendPath("root").appendPath(paramString2).appendPath("recent").build();
  }
  
  public static Uri buildRootUri(String paramString1, String paramString2)
  {
    return new Uri.Builder().scheme("content").authority(paramString1).appendPath("root").appendPath(paramString2).build();
  }
  
  public static Uri buildRootsUri(String paramString)
  {
    return new Uri.Builder().scheme("content").authority(paramString).appendPath("root").build();
  }
  
  public static Uri buildSearchDocumentsUri(String paramString1, String paramString2, String paramString3)
  {
    return new Uri.Builder().scheme("content").authority(paramString1).appendPath("root").appendPath(paramString2).appendPath("search").appendQueryParameter("query", paramString3).build();
  }
  
  public static Uri buildTreeDocumentUri(String paramString1, String paramString2)
  {
    return new Uri.Builder().scheme("content").authority(paramString1).appendPath("tree").appendPath(paramString2).build();
  }
  
  public static Uri copyDocument(ContentProviderClient paramContentProviderClient, Uri paramUri1, Uri paramUri2)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri1);
    localBundle.putParcelable("android.content.extra.TARGET_URI", paramUri2);
    return (Uri)paramContentProviderClient.call("android:copyDocument", null, localBundle).getParcelable("uri");
  }
  
  /* Error */
  public static Uri copyDocument(ContentResolver paramContentResolver, Uri paramUri1, Uri paramUri2)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore_3
    //   9: aload_3
    //   10: aload_1
    //   11: aload_2
    //   12: invokestatic 259	android/provider/DocumentsContract:copyDocument	(Landroid/content/ContentProviderClient;Landroid/net/Uri;Landroid/net/Uri;)Landroid/net/Uri;
    //   15: astore_1
    //   16: aload_3
    //   17: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   20: aload_1
    //   21: areturn
    //   22: astore_0
    //   23: goto +25 -> 48
    //   26: astore_1
    //   27: ldc -114
    //   29: ldc_w 265
    //   32: aload_1
    //   33: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   36: pop
    //   37: aload_0
    //   38: aload_1
    //   39: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   42: aload_3
    //   43: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   46: aconst_null
    //   47: areturn
    //   48: aload_3
    //   49: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   52: aload_0
    //   53: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	54	0	paramContentResolver	ContentResolver
    //   0	54	1	paramUri1	Uri
    //   0	54	2	paramUri2	Uri
    //   8	41	3	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   9	16	22	finally
    //   27	42	22	finally
    //   9	16	26	java/lang/Exception
  }
  
  public static Uri createDocument(ContentProviderClient paramContentProviderClient, Uri paramUri, String paramString1, String paramString2)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri);
    localBundle.putString("mime_type", paramString1);
    localBundle.putString("_display_name", paramString2);
    return (Uri)paramContentProviderClient.call("android:createDocument", null, localBundle).getParcelable("uri");
  }
  
  /* Error */
  public static Uri createDocument(ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore 4
    //   10: aload 4
    //   12: aload_1
    //   13: aload_2
    //   14: aload_3
    //   15: invokestatic 288	android/provider/DocumentsContract:createDocument	(Landroid/content/ContentProviderClient;Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri;
    //   18: astore_1
    //   19: aload 4
    //   21: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   24: aload_1
    //   25: areturn
    //   26: astore_0
    //   27: goto +26 -> 53
    //   30: astore_1
    //   31: ldc -114
    //   33: ldc_w 290
    //   36: aload_1
    //   37: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   40: pop
    //   41: aload_0
    //   42: aload_1
    //   43: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   46: aload 4
    //   48: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   51: aconst_null
    //   52: areturn
    //   53: aload 4
    //   55: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   58: aload_0
    //   59: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	60	0	paramContentResolver	ContentResolver
    //   0	60	1	paramUri	Uri
    //   0	60	2	paramString1	String
    //   0	60	3	paramString2	String
    //   8	46	4	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   10	19	26	finally
    //   31	46	26	finally
    //   10	19	30	java/lang/Exception
  }
  
  public static IntentSender createWebLinkIntent(ContentProviderClient paramContentProviderClient, Uri paramUri, Bundle paramBundle)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri);
    if (paramBundle != null) {
      localBundle.putBundle("options", paramBundle);
    }
    return (IntentSender)paramContentProviderClient.call("android:createWebLinkIntent", null, localBundle).getParcelable("result");
  }
  
  /* Error */
  public static IntentSender createWebLinkIntent(ContentResolver paramContentResolver, Uri paramUri, Bundle paramBundle)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore_3
    //   9: aload_3
    //   10: aload_1
    //   11: aload_2
    //   12: invokestatic 301	android/provider/DocumentsContract:createWebLinkIntent	(Landroid/content/ContentProviderClient;Landroid/net/Uri;Landroid/os/Bundle;)Landroid/content/IntentSender;
    //   15: astore_1
    //   16: aload_3
    //   17: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   20: aload_1
    //   21: areturn
    //   22: astore_0
    //   23: goto +25 -> 48
    //   26: astore_1
    //   27: ldc -114
    //   29: ldc_w 303
    //   32: aload_1
    //   33: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   36: pop
    //   37: aload_0
    //   38: aload_1
    //   39: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   42: aload_3
    //   43: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   46: aconst_null
    //   47: areturn
    //   48: aload_3
    //   49: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   52: aload_0
    //   53: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	54	0	paramContentResolver	ContentResolver
    //   0	54	1	paramUri	Uri
    //   0	54	2	paramBundle	Bundle
    //   8	41	3	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   9	16	22	finally
    //   27	42	22	finally
    //   9	16	26	java/lang/Exception
  }
  
  public static void deleteDocument(ContentProviderClient paramContentProviderClient, Uri paramUri)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri);
    paramContentProviderClient.call("android:deleteDocument", null, localBundle);
  }
  
  /* Error */
  public static boolean deleteDocument(ContentResolver paramContentResolver, Uri paramUri)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore_2
    //   9: aload_2
    //   10: aload_1
    //   11: invokestatic 308	android/provider/DocumentsContract:deleteDocument	(Landroid/content/ContentProviderClient;Landroid/net/Uri;)V
    //   14: aload_2
    //   15: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   18: iconst_1
    //   19: ireturn
    //   20: astore_0
    //   21: goto +25 -> 46
    //   24: astore_1
    //   25: ldc -114
    //   27: ldc_w 310
    //   30: aload_1
    //   31: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   34: pop
    //   35: aload_0
    //   36: aload_1
    //   37: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   40: aload_2
    //   41: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   44: iconst_0
    //   45: ireturn
    //   46: aload_2
    //   47: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   50: aload_0
    //   51: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	52	0	paramContentResolver	ContentResolver
    //   0	52	1	paramUri	Uri
    //   8	39	2	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   9	14	20	finally
    //   25	40	20	finally
    //   9	14	24	java/lang/Exception
  }
  
  public static void ejectRoot(ContentProviderClient paramContentProviderClient, Uri paramUri)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri);
    paramContentProviderClient.call("android:ejectRoot", null, localBundle);
  }
  
  /* Error */
  public static void ejectRoot(ContentResolver paramContentResolver, Uri paramUri)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore_0
    //   9: aload_0
    //   10: aload_1
    //   11: invokestatic 314	android/provider/DocumentsContract:ejectRoot	(Landroid/content/ContentProviderClient;Landroid/net/Uri;)V
    //   14: aload_0
    //   15: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   18: goto +16 -> 34
    //   21: astore_1
    //   22: goto +13 -> 35
    //   25: astore_1
    //   26: aload_1
    //   27: invokevirtual 318	android/os/RemoteException:rethrowAsRuntimeException	()Ljava/lang/RuntimeException;
    //   30: pop
    //   31: goto -17 -> 14
    //   34: return
    //   35: aload_0
    //   36: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   39: aload_1
    //   40: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	41	0	paramContentResolver	ContentResolver
    //   0	41	1	paramUri	Uri
    // Exception table:
    //   from	to	target	type
    //   9	14	21	finally
    //   26	31	21	finally
    //   9	14	25	android/os/RemoteException
  }
  
  public static Path findDocumentPath(ContentProviderClient paramContentProviderClient, Uri paramUri)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri);
    return (Path)paramContentProviderClient.call("android:findDocumentPath", null, localBundle).getParcelable("result");
  }
  
  /* Error */
  public static Path findDocumentPath(ContentResolver paramContentResolver, Uri paramUri)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 203	android/provider/DocumentsContract:isTreeUri	(Landroid/net/Uri;)Z
    //   4: istore_2
    //   5: new 323	java/lang/StringBuilder
    //   8: dup
    //   9: invokespecial 324	java/lang/StringBuilder:<init>	()V
    //   12: astore_3
    //   13: aload_3
    //   14: aload_1
    //   15: invokevirtual 328	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   18: pop
    //   19: aload_3
    //   20: ldc_w 330
    //   23: invokevirtual 333	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: pop
    //   27: iload_2
    //   28: aload_3
    //   29: invokevirtual 336	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   32: invokestatic 342	com/android/internal/util/Preconditions:checkArgument	(ZLjava/lang/Object;)V
    //   35: aload_0
    //   36: aload_1
    //   37: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   40: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   43: astore_3
    //   44: aload_3
    //   45: aload_1
    //   46: invokestatic 344	android/provider/DocumentsContract:findDocumentPath	(Landroid/content/ContentProviderClient;Landroid/net/Uri;)Landroid/provider/DocumentsContract$Path;
    //   49: astore_1
    //   50: aload_3
    //   51: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   54: aload_1
    //   55: areturn
    //   56: astore_0
    //   57: goto +25 -> 82
    //   60: astore_1
    //   61: ldc -114
    //   63: ldc_w 346
    //   66: aload_1
    //   67: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   70: pop
    //   71: aload_0
    //   72: aload_1
    //   73: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   76: aload_3
    //   77: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   80: aconst_null
    //   81: areturn
    //   82: aload_3
    //   83: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   86: aload_0
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	paramContentResolver	ContentResolver
    //   0	88	1	paramUri	Uri
    //   4	24	2	bool	boolean
    //   12	71	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   44	50	56	finally
    //   61	76	56	finally
    //   44	50	60	java/lang/Exception
  }
  
  public static String getDocumentId(Uri paramUri)
  {
    Object localObject = paramUri.getPathSegments();
    if ((((List)localObject).size() >= 2) && ("document".equals(((List)localObject).get(0)))) {
      return (String)((List)localObject).get(1);
    }
    if ((((List)localObject).size() >= 4) && ("tree".equals(((List)localObject).get(0))) && ("document".equals(((List)localObject).get(2)))) {
      return (String)((List)localObject).get(3);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid URI: ");
    ((StringBuilder)localObject).append(paramUri);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public static Bundle getDocumentMetadata(ContentProviderClient paramContentProviderClient, Uri paramUri)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri);
    paramContentProviderClient = paramContentProviderClient.call("android:getDocumentMetadata", null, localBundle);
    if (paramContentProviderClient != null) {
      return paramContentProviderClient;
    }
    throw new RemoteException("Failed to get a response from getDocumentMetadata");
  }
  
  /* Error */
  public static Bundle getDocumentMetadata(ContentResolver paramContentResolver, Uri paramUri)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore_2
    //   9: aload_2
    //   10: aload_1
    //   11: invokestatic 382	android/provider/DocumentsContract:getDocumentMetadata	(Landroid/content/ContentProviderClient;Landroid/net/Uri;)Landroid/os/Bundle;
    //   14: astore_1
    //   15: aload_2
    //   16: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   19: aload_1
    //   20: areturn
    //   21: astore_0
    //   22: goto +24 -> 46
    //   25: astore_1
    //   26: ldc -114
    //   28: ldc_w 384
    //   31: invokestatic 387	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   34: pop
    //   35: aload_0
    //   36: aload_1
    //   37: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   40: aload_2
    //   41: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   44: aconst_null
    //   45: areturn
    //   46: aload_2
    //   47: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   50: aload_0
    //   51: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	52	0	paramContentResolver	ContentResolver
    //   0	52	1	paramUri	Uri
    //   8	39	2	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   9	15	21	finally
    //   26	40	21	finally
    //   9	15	25	java/lang/Exception
  }
  
  /* Error */
  public static android.graphics.Bitmap getDocumentThumbnail(ContentProviderClient paramContentProviderClient, Uri paramUri, android.graphics.Point paramPoint, android.os.CancellationSignal paramCancellationSignal)
    throws RemoteException, IOException
  {
    // Byte code:
    //   0: new 230	android/os/Bundle
    //   3: dup
    //   4: invokespecial 231	android/os/Bundle:<init>	()V
    //   7: astore 4
    //   9: aload 4
    //   11: ldc_w 395
    //   14: aload_2
    //   15: invokevirtual 235	android/os/Bundle:putParcelable	(Ljava/lang/String;Landroid/os/Parcelable;)V
    //   18: aconst_null
    //   19: astore 5
    //   21: aconst_null
    //   22: astore 5
    //   24: aload_0
    //   25: aload_1
    //   26: ldc_w 397
    //   29: aload 4
    //   31: aload_3
    //   32: invokevirtual 401	android/content/ContentProviderClient:openTypedAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;Landroid/os/CancellationSignal;)Landroid/content/res/AssetFileDescriptor;
    //   35: astore_1
    //   36: aload_1
    //   37: astore 5
    //   39: aload_1
    //   40: invokevirtual 407	android/content/res/AssetFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   43: astore_3
    //   44: aload_1
    //   45: astore 5
    //   47: aload_1
    //   48: invokevirtual 411	android/content/res/AssetFileDescriptor:getStartOffset	()J
    //   51: lstore 6
    //   53: aconst_null
    //   54: astore_0
    //   55: aload_3
    //   56: lload 6
    //   58: getstatic 416	android/system/OsConstants:SEEK_SET	I
    //   61: invokestatic 422	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
    //   64: pop2
    //   65: goto +54 -> 119
    //   68: astore_0
    //   69: goto +321 -> 390
    //   72: astore_0
    //   73: aload_1
    //   74: astore 5
    //   76: new 424	java/io/BufferedInputStream
    //   79: astore_0
    //   80: aload_1
    //   81: astore 5
    //   83: new 426	java/io/FileInputStream
    //   86: astore 4
    //   88: aload_1
    //   89: astore 5
    //   91: aload 4
    //   93: aload_3
    //   94: invokespecial 429	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   97: aload_1
    //   98: astore 5
    //   100: aload_0
    //   101: aload 4
    //   103: getstatic 160	android/provider/DocumentsContract:THUMBNAIL_BUFFER_SIZE	I
    //   106: invokespecial 432	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   109: aload_1
    //   110: astore 5
    //   112: aload_0
    //   113: getstatic 160	android/provider/DocumentsContract:THUMBNAIL_BUFFER_SIZE	I
    //   116: invokevirtual 436	java/io/BufferedInputStream:mark	(I)V
    //   119: aload_1
    //   120: astore 5
    //   122: new 438	android/graphics/BitmapFactory$Options
    //   125: astore 4
    //   127: aload_1
    //   128: astore 5
    //   130: aload 4
    //   132: invokespecial 439	android/graphics/BitmapFactory$Options:<init>	()V
    //   135: aload_1
    //   136: astore 5
    //   138: aload 4
    //   140: iconst_1
    //   141: putfield 443	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   144: aload_0
    //   145: ifnull +14 -> 159
    //   148: aload_0
    //   149: aconst_null
    //   150: aload 4
    //   152: invokestatic 449	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   155: pop
    //   156: goto +14 -> 170
    //   159: aload_1
    //   160: astore 5
    //   162: aload_3
    //   163: aconst_null
    //   164: aload 4
    //   166: invokestatic 453	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   169: pop
    //   170: aload_1
    //   171: astore 5
    //   173: aload 4
    //   175: getfield 456	android/graphics/BitmapFactory$Options:outWidth	I
    //   178: aload_2
    //   179: getfield 461	android/graphics/Point:x	I
    //   182: idiv
    //   183: istore 8
    //   185: aload_1
    //   186: astore 5
    //   188: aload 4
    //   190: getfield 464	android/graphics/BitmapFactory$Options:outHeight	I
    //   193: aload_2
    //   194: getfield 467	android/graphics/Point:y	I
    //   197: idiv
    //   198: istore 9
    //   200: aload_1
    //   201: astore 5
    //   203: aload 4
    //   205: iconst_0
    //   206: putfield 443	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   209: aload_1
    //   210: astore 5
    //   212: aload 4
    //   214: iload 8
    //   216: iload 9
    //   218: invokestatic 473	java/lang/Math:min	(II)I
    //   221: putfield 476	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   224: aload_0
    //   225: ifnull +18 -> 243
    //   228: aload_0
    //   229: invokevirtual 479	java/io/BufferedInputStream:reset	()V
    //   232: aload_0
    //   233: aconst_null
    //   234: aload 4
    //   236: invokestatic 449	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   239: astore_0
    //   240: goto +39 -> 279
    //   243: aload_3
    //   244: lload 6
    //   246: getstatic 416	android/system/OsConstants:SEEK_SET	I
    //   249: invokestatic 422	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
    //   252: pop2
    //   253: goto +12 -> 265
    //   256: astore_0
    //   257: aload_1
    //   258: astore 5
    //   260: aload_0
    //   261: invokevirtual 483	android/system/ErrnoException:rethrowAsIOException	()Ljava/io/IOException;
    //   264: pop
    //   265: aload_1
    //   266: astore 5
    //   268: aload_3
    //   269: aconst_null
    //   270: aload 4
    //   272: invokestatic 453	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   275: astore_0
    //   276: goto -36 -> 240
    //   279: aload_1
    //   280: invokevirtual 487	android/content/res/AssetFileDescriptor:getExtras	()Landroid/os/Bundle;
    //   283: astore_2
    //   284: aload_2
    //   285: ifnull +19 -> 304
    //   288: aload_2
    //   289: ldc 49
    //   291: iconst_0
    //   292: invokevirtual 491	android/os/Bundle:getInt	(Ljava/lang/String;I)I
    //   295: istore 8
    //   297: goto +10 -> 307
    //   300: astore_0
    //   301: goto +89 -> 390
    //   304: iconst_0
    //   305: istore 8
    //   307: iload 8
    //   309: ifeq +64 -> 373
    //   312: aload_0
    //   313: invokevirtual 496	android/graphics/Bitmap:getWidth	()I
    //   316: istore 10
    //   318: aload_0
    //   319: invokevirtual 499	android/graphics/Bitmap:getHeight	()I
    //   322: istore 9
    //   324: new 501	android/graphics/Matrix
    //   327: astore_2
    //   328: aload_2
    //   329: invokespecial 502	android/graphics/Matrix:<init>	()V
    //   332: iload 8
    //   334: i2f
    //   335: fstore 11
    //   337: aload_2
    //   338: fload 11
    //   340: iload 10
    //   342: iconst_2
    //   343: idiv
    //   344: i2f
    //   345: iload 9
    //   347: iconst_2
    //   348: idiv
    //   349: i2f
    //   350: invokevirtual 506	android/graphics/Matrix:setRotate	(FFF)V
    //   353: aload_0
    //   354: iconst_0
    //   355: iconst_0
    //   356: iload 10
    //   358: iload 9
    //   360: aload_2
    //   361: iconst_0
    //   362: invokestatic 510	android/graphics/Bitmap:createBitmap	(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   365: astore_0
    //   366: goto +7 -> 373
    //   369: astore_0
    //   370: goto -69 -> 301
    //   373: aload_1
    //   374: invokestatic 516	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   377: aload_0
    //   378: areturn
    //   379: astore_0
    //   380: goto +10 -> 390
    //   383: astore_0
    //   384: aload 5
    //   386: astore_1
    //   387: goto +3 -> 390
    //   390: aload_1
    //   391: invokestatic 516	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   394: aload_0
    //   395: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	396	0	paramContentProviderClient	ContentProviderClient
    //   0	396	1	paramUri	Uri
    //   0	396	2	paramPoint	android.graphics.Point
    //   0	396	3	paramCancellationSignal	android.os.CancellationSignal
    //   7	264	4	localObject	Object
    //   19	366	5	localUri	Uri
    //   51	194	6	l	long
    //   183	150	8	i	int
    //   198	161	9	j	int
    //   316	41	10	k	int
    //   335	4	11	f	float
    // Exception table:
    //   from	to	target	type
    //   55	65	68	finally
    //   148	156	68	finally
    //   228	240	68	finally
    //   243	253	68	finally
    //   55	65	72	android/system/ErrnoException
    //   243	253	256	android/system/ErrnoException
    //   288	297	300	finally
    //   337	366	369	finally
    //   279	284	379	finally
    //   312	332	379	finally
    //   24	36	383	finally
    //   39	44	383	finally
    //   47	53	383	finally
    //   76	80	383	finally
    //   83	88	383	finally
    //   91	97	383	finally
    //   100	109	383	finally
    //   112	119	383	finally
    //   122	127	383	finally
    //   130	135	383	finally
    //   138	144	383	finally
    //   162	170	383	finally
    //   173	185	383	finally
    //   188	200	383	finally
    //   203	209	383	finally
    //   212	224	383	finally
    //   260	265	383	finally
    //   268	276	383	finally
  }
  
  /* Error */
  public static android.graphics.Bitmap getDocumentThumbnail(ContentResolver paramContentResolver, Uri paramUri, android.graphics.Point paramPoint, android.os.CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore 4
    //   10: aload 4
    //   12: aload_1
    //   13: aload_2
    //   14: aload_3
    //   15: invokestatic 519	android/provider/DocumentsContract:getDocumentThumbnail	(Landroid/content/ContentProviderClient;Landroid/net/Uri;Landroid/graphics/Point;Landroid/os/CancellationSignal;)Landroid/graphics/Bitmap;
    //   18: astore_2
    //   19: aload 4
    //   21: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   24: aload_2
    //   25: areturn
    //   26: astore_0
    //   27: goto +69 -> 96
    //   30: astore_2
    //   31: aload_2
    //   32: instanceof 521
    //   35: ifne +49 -> 84
    //   38: new 323	java/lang/StringBuilder
    //   41: astore_3
    //   42: aload_3
    //   43: invokespecial 324	java/lang/StringBuilder:<init>	()V
    //   46: aload_3
    //   47: ldc_w 523
    //   50: invokevirtual 333	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_3
    //   55: aload_1
    //   56: invokevirtual 328	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload_3
    //   61: ldc_w 525
    //   64: invokevirtual 333	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload_3
    //   69: aload_2
    //   70: invokevirtual 328	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   73: pop
    //   74: ldc -114
    //   76: aload_3
    //   77: invokevirtual 336	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   80: invokestatic 387	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   83: pop
    //   84: aload_0
    //   85: aload_2
    //   86: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   89: aload 4
    //   91: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   94: aconst_null
    //   95: areturn
    //   96: aload 4
    //   98: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   101: aload_0
    //   102: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	103	0	paramContentResolver	ContentResolver
    //   0	103	1	paramUri	Uri
    //   0	103	2	paramPoint	android.graphics.Point
    //   0	103	3	paramCancellationSignal	android.os.CancellationSignal
    //   8	89	4	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   10	19	26	finally
    //   31	84	26	finally
    //   84	89	26	finally
    //   10	19	30	java/lang/Exception
  }
  
  public static String getRootId(Uri paramUri)
  {
    Object localObject = paramUri.getPathSegments();
    if ((((List)localObject).size() >= 2) && ("root".equals(((List)localObject).get(0)))) {
      return (String)((List)localObject).get(1);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid URI: ");
    ((StringBuilder)localObject).append(paramUri);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public static String getSearchDocumentsQuery(Uri paramUri)
  {
    return paramUri.getQueryParameter("query");
  }
  
  public static String getTreeDocumentId(Uri paramUri)
  {
    Object localObject = paramUri.getPathSegments();
    if ((((List)localObject).size() >= 2) && ("tree".equals(((List)localObject).get(0)))) {
      return (String)((List)localObject).get(1);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid URI: ");
    ((StringBuilder)localObject).append(paramUri);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public static boolean isChildDocument(ContentProviderClient paramContentProviderClient, Uri paramUri1, Uri paramUri2)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri1);
    localBundle.putParcelable("android.content.extra.TARGET_URI", paramUri2);
    paramContentProviderClient = paramContentProviderClient.call("android:isChildDocument", null, localBundle);
    if (paramContentProviderClient != null)
    {
      if (paramContentProviderClient.containsKey("result")) {
        return paramContentProviderClient.getBoolean("result");
      }
      throw new RemoteException("Response did not include result field..");
    }
    throw new RemoteException("Failed to get a reponse from isChildDocument query.");
  }
  
  public static boolean isContentUri(Uri paramUri)
  {
    boolean bool;
    if ((paramUri != null) && ("content".equals(paramUri.getScheme()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isDocumentUri(Context paramContext, Uri paramUri)
  {
    boolean bool1 = isContentUri(paramUri);
    boolean bool2 = false;
    if ((bool1) && (isDocumentsProvider(paramContext, paramUri.getAuthority())))
    {
      paramContext = paramUri.getPathSegments();
      if (paramContext.size() == 2) {
        return "document".equals(paramContext.get(0));
      }
      if (paramContext.size() == 4)
      {
        bool1 = bool2;
        if ("tree".equals(paramContext.get(0)))
        {
          bool1 = bool2;
          if ("document".equals(paramContext.get(2))) {
            bool1 = true;
          }
        }
        return bool1;
      }
    }
    return false;
  }
  
  private static boolean isDocumentsProvider(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent("android.content.action.DOCUMENTS_PROVIDER");
    paramContext = paramContext.getPackageManager().queryIntentContentProviders(localIntent, 0).iterator();
    while (paramContext.hasNext()) {
      if (paramString.equals(nextproviderInfo.authority)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isManageMode(Uri paramUri)
  {
    return paramUri.getBooleanQueryParameter("manage", false);
  }
  
  public static boolean isRootUri(Context paramContext, Uri paramUri)
  {
    boolean bool1 = isContentUri(paramUri);
    boolean bool2 = false;
    if ((bool1) && (isDocumentsProvider(paramContext, paramUri.getAuthority())))
    {
      paramContext = paramUri.getPathSegments();
      bool1 = bool2;
      if (paramContext.size() == 2)
      {
        bool1 = bool2;
        if ("root".equals(paramContext.get(0))) {
          bool1 = true;
        }
      }
      return bool1;
    }
    return false;
  }
  
  public static boolean isTreeUri(Uri paramUri)
  {
    paramUri = paramUri.getPathSegments();
    int i = paramUri.size();
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (i >= 2)
    {
      bool2 = bool1;
      if ("tree".equals(paramUri.get(0))) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public static Uri moveDocument(ContentProviderClient paramContentProviderClient, Uri paramUri1, Uri paramUri2, Uri paramUri3)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri1);
    localBundle.putParcelable("parentUri", paramUri2);
    localBundle.putParcelable("android.content.extra.TARGET_URI", paramUri3);
    return (Uri)paramContentProviderClient.call("android:moveDocument", null, localBundle).getParcelable("uri");
  }
  
  /* Error */
  public static Uri moveDocument(ContentResolver paramContentResolver, Uri paramUri1, Uri paramUri2, Uri paramUri3)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore 4
    //   10: aload 4
    //   12: aload_1
    //   13: aload_2
    //   14: aload_3
    //   15: invokestatic 606	android/provider/DocumentsContract:moveDocument	(Landroid/content/ContentProviderClient;Landroid/net/Uri;Landroid/net/Uri;Landroid/net/Uri;)Landroid/net/Uri;
    //   18: astore_1
    //   19: aload 4
    //   21: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   24: aload_1
    //   25: areturn
    //   26: astore_0
    //   27: goto +26 -> 53
    //   30: astore_1
    //   31: ldc -114
    //   33: ldc_w 608
    //   36: aload_1
    //   37: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   40: pop
    //   41: aload_0
    //   42: aload_1
    //   43: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   46: aload 4
    //   48: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   51: aconst_null
    //   52: areturn
    //   53: aload 4
    //   55: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   58: aload_0
    //   59: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	60	0	paramContentResolver	ContentResolver
    //   0	60	1	paramUri1	Uri
    //   0	60	2	paramUri2	Uri
    //   0	60	3	paramUri3	Uri
    //   8	46	4	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   10	19	26	finally
    //   31	46	26	finally
    //   10	19	30	java/lang/Exception
  }
  
  public static AssetFileDescriptor openImageThumbnail(File paramFile)
    throws FileNotFoundException
  {
    ParcelFileDescriptor localParcelFileDescriptor = ParcelFileDescriptor.open(paramFile, 268435456);
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = localObject1;
    try
    {
      ExifInterface localExifInterface = new android/media/ExifInterface;
      localObject3 = localObject1;
      localExifInterface.<init>(paramFile.getAbsolutePath());
      localObject3 = localObject1;
      int i = localExifInterface.getAttributeInt("Orientation", -1);
      if (i != 3) {
        if (i != 6) {
          if (i != 8) {
            paramFile = localObject2;
          }
        }
      }
      for (;;)
      {
        break;
        localObject3 = localObject1;
        paramFile = new android/os/Bundle;
        localObject3 = localObject1;
        paramFile.<init>(1);
        localObject3 = paramFile;
        paramFile.putInt("android.provider.extra.ORIENTATION", 270);
        continue;
        localObject3 = localObject1;
        paramFile = new android/os/Bundle;
        localObject3 = localObject1;
        paramFile.<init>(1);
        localObject3 = paramFile;
        paramFile.putInt("android.provider.extra.ORIENTATION", 90);
        continue;
        localObject3 = localObject1;
        paramFile = new android/os/Bundle;
        localObject3 = localObject1;
        paramFile.<init>(1);
        localObject3 = paramFile;
        paramFile.putInt("android.provider.extra.ORIENTATION", 180);
      }
      File localFile;
      try
      {
        localObject3 = localExifInterface.getThumbnailRange();
        if (localObject3 != null)
        {
          localObject3 = new AssetFileDescriptor(localParcelFileDescriptor, localObject3[0], localObject3[1], paramFile);
          return localObject3;
        }
      }
      catch (IOException localIOException)
      {
        localFile = paramFile;
      }
      paramFile = localFile;
    }
    catch (IOException paramFile) {}
    return new AssetFileDescriptor(localParcelFileDescriptor, 0L, -1L, paramFile);
  }
  
  public static void removeDocument(ContentProviderClient paramContentProviderClient, Uri paramUri1, Uri paramUri2)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri1);
    localBundle.putParcelable("parentUri", paramUri2);
    paramContentProviderClient.call("android:removeDocument", null, localBundle);
  }
  
  /* Error */
  public static boolean removeDocument(ContentResolver paramContentResolver, Uri paramUri1, Uri paramUri2)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore_3
    //   9: aload_3
    //   10: aload_1
    //   11: aload_2
    //   12: invokestatic 650	android/provider/DocumentsContract:removeDocument	(Landroid/content/ContentProviderClient;Landroid/net/Uri;Landroid/net/Uri;)V
    //   15: aload_3
    //   16: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   19: iconst_1
    //   20: ireturn
    //   21: astore_0
    //   22: goto +25 -> 47
    //   25: astore_1
    //   26: ldc -114
    //   28: ldc_w 652
    //   31: aload_1
    //   32: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   35: pop
    //   36: aload_0
    //   37: aload_1
    //   38: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   41: aload_3
    //   42: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   45: iconst_0
    //   46: ireturn
    //   47: aload_3
    //   48: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   51: aload_0
    //   52: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	53	0	paramContentResolver	ContentResolver
    //   0	53	1	paramUri1	Uri
    //   0	53	2	paramUri2	Uri
    //   8	40	3	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   9	15	21	finally
    //   26	41	21	finally
    //   9	15	25	java/lang/Exception
  }
  
  public static Uri renameDocument(ContentProviderClient paramContentProviderClient, Uri paramUri, String paramString)
    throws RemoteException
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("uri", paramUri);
    localBundle.putString("_display_name", paramString);
    paramContentProviderClient = (Uri)paramContentProviderClient.call("android:renameDocument", null, localBundle).getParcelable("uri");
    if (paramContentProviderClient == null) {
      paramContentProviderClient = paramUri;
    }
    return paramContentProviderClient;
  }
  
  /* Error */
  public static Uri renameDocument(ContentResolver paramContentResolver, Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 193	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   5: invokevirtual 257	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   8: astore_3
    //   9: aload_3
    //   10: aload_1
    //   11: aload_2
    //   12: invokestatic 657	android/provider/DocumentsContract:renameDocument	(Landroid/content/ContentProviderClient;Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   15: astore_1
    //   16: aload_3
    //   17: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   20: aload_1
    //   21: areturn
    //   22: astore_0
    //   23: goto +25 -> 48
    //   26: astore_1
    //   27: ldc -114
    //   29: ldc_w 659
    //   32: aload_1
    //   33: invokestatic 271	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   36: pop
    //   37: aload_0
    //   38: aload_1
    //   39: invokestatic 275	android/provider/DocumentsContract:rethrowIfNecessary	(Landroid/content/ContentResolver;Ljava/lang/Exception;)V
    //   42: aload_3
    //   43: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   46: aconst_null
    //   47: areturn
    //   48: aload_3
    //   49: invokestatic 263	android/content/ContentProviderClient:releaseQuietly	(Landroid/content/ContentProviderClient;)V
    //   52: aload_0
    //   53: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	54	0	paramContentResolver	ContentResolver
    //   0	54	1	paramUri	Uri
    //   0	54	2	paramString	String
    //   8	41	3	localContentProviderClient	ContentProviderClient
    // Exception table:
    //   from	to	target	type
    //   9	16	22	finally
    //   27	42	22	finally
    //   9	16	26	java/lang/Exception
  }
  
  private static void rethrowIfNecessary(ContentResolver paramContentResolver, Exception paramException)
    throws FileNotFoundException
  {
    if (paramContentResolver.getTargetSdkVersion() >= 26) {
      if ((paramException instanceof ParcelableException)) {
        ((ParcelableException)paramException).maybeRethrow(FileNotFoundException.class);
      } else if ((paramException instanceof RemoteException)) {
        ((RemoteException)paramException).rethrowAsRuntimeException();
      } else if ((paramException instanceof RuntimeException)) {
        throw ((RuntimeException)paramException);
      }
    }
  }
  
  public static Uri setManageMode(Uri paramUri)
  {
    return paramUri.buildUpon().appendQueryParameter("manage", "true").build();
  }
  
  public static final class Document
  {
    public static final String COLUMN_DISPLAY_NAME = "_display_name";
    public static final String COLUMN_DOCUMENT_ID = "document_id";
    public static final String COLUMN_FLAGS = "flags";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_LAST_MODIFIED = "last_modified";
    public static final String COLUMN_MIME_TYPE = "mime_type";
    public static final String COLUMN_SIZE = "_size";
    public static final String COLUMN_SUMMARY = "summary";
    public static final int FLAG_DIR_PREFERS_GRID = 16;
    public static final int FLAG_DIR_PREFERS_LAST_MODIFIED = 32;
    public static final int FLAG_DIR_SUPPORTS_CREATE = 8;
    public static final int FLAG_PARTIAL = 65536;
    public static final int FLAG_SUPPORTS_COPY = 128;
    public static final int FLAG_SUPPORTS_DELETE = 4;
    public static final int FLAG_SUPPORTS_METADATA = 131072;
    public static final int FLAG_SUPPORTS_MOVE = 256;
    public static final int FLAG_SUPPORTS_REMOVE = 1024;
    public static final int FLAG_SUPPORTS_RENAME = 64;
    public static final int FLAG_SUPPORTS_SETTINGS = 2048;
    public static final int FLAG_SUPPORTS_THUMBNAIL = 1;
    public static final int FLAG_SUPPORTS_WRITE = 2;
    public static final int FLAG_VIRTUAL_DOCUMENT = 512;
    public static final int FLAG_WEB_LINKABLE = 4096;
    public static final String MIME_TYPE_DIR = "vnd.android.document/directory";
    
    private Document() {}
  }
  
  public static final class Path
    implements Parcelable
  {
    public static final Parcelable.Creator<Path> CREATOR = new Parcelable.Creator()
    {
      public DocumentsContract.Path createFromParcel(Parcel paramAnonymousParcel)
      {
        return new DocumentsContract.Path(paramAnonymousParcel.readString(), paramAnonymousParcel.createStringArrayList());
      }
      
      public DocumentsContract.Path[] newArray(int paramAnonymousInt)
      {
        return new DocumentsContract.Path[paramAnonymousInt];
      }
    };
    private final List<String> mPath;
    private final String mRootId;
    
    public Path(String paramString, List<String> paramList)
    {
      Preconditions.checkCollectionNotEmpty(paramList, "path");
      Preconditions.checkCollectionElementsNotNull(paramList, "path");
      mRootId = paramString;
      mPath = paramList;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && ((paramObject instanceof Path)))
      {
        paramObject = (Path)paramObject;
        if ((!Objects.equals(mRootId, mRootId)) || (!Objects.equals(mPath, mPath))) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public List<String> getPath()
    {
      return mPath;
    }
    
    public String getRootId()
    {
      return mRootId;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mRootId, mPath });
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("DocumentsContract.Path{");
      localStringBuilder.append("rootId=");
      localStringBuilder.append(mRootId);
      localStringBuilder.append(", path=");
      localStringBuilder.append(mPath);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mRootId);
      paramParcel.writeStringList(mPath);
    }
  }
  
  public static final class Root
  {
    public static final String COLUMN_AVAILABLE_BYTES = "available_bytes";
    public static final String COLUMN_CAPACITY_BYTES = "capacity_bytes";
    public static final String COLUMN_DOCUMENT_ID = "document_id";
    public static final String COLUMN_FLAGS = "flags";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_MIME_TYPES = "mime_types";
    public static final String COLUMN_ROOT_ID = "root_id";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_TITLE = "title";
    public static final int FLAG_ADVANCED = 131072;
    public static final int FLAG_EMPTY = 65536;
    public static final int FLAG_HAS_SETTINGS = 262144;
    public static final int FLAG_LOCAL_ONLY = 2;
    public static final int FLAG_REMOVABLE_SD = 524288;
    public static final int FLAG_REMOVABLE_USB = 1048576;
    public static final int FLAG_SUPPORTS_CREATE = 1;
    public static final int FLAG_SUPPORTS_EJECT = 32;
    public static final int FLAG_SUPPORTS_IS_CHILD = 16;
    public static final int FLAG_SUPPORTS_RECENTS = 4;
    public static final int FLAG_SUPPORTS_SEARCH = 8;
    public static final String MIME_TYPE_ITEM = "vnd.android.document/root";
    
    private Root() {}
  }
}
