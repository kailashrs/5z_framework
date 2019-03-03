package android.provider;

import android.content.ClipDescription;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.ParcelableException;
import android.util.Log;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import libcore.io.IoUtils;

public abstract class DocumentsProvider
  extends ContentProvider
{
  private static final int MATCH_CHILDREN = 6;
  private static final int MATCH_CHILDREN_TREE = 8;
  private static final int MATCH_DOCUMENT = 5;
  private static final int MATCH_DOCUMENT_TREE = 7;
  private static final int MATCH_RECENT = 3;
  private static final int MATCH_ROOT = 2;
  private static final int MATCH_ROOTS = 1;
  private static final int MATCH_SEARCH = 4;
  private static final String TAG = "DocumentsProvider";
  private String mAuthority;
  private UriMatcher mMatcher;
  
  public DocumentsProvider() {}
  
  private Bundle callUnchecked(String paramString1, String paramString2, Bundle paramBundle)
    throws FileNotFoundException
  {
    Context localContext = getContext();
    Bundle localBundle = new Bundle();
    if ("android:ejectRoot".equals(paramString1))
    {
      paramString1 = (Uri)paramBundle.getParcelable("uri");
      enforceWritePermissionInner(paramString1, getCallingPackage(), null);
      ejectRoot(DocumentsContract.getRootId(paramString1));
      return localBundle;
    }
    Object localObject1 = (Uri)paramBundle.getParcelable("uri");
    Object localObject2 = ((Uri)localObject1).getAuthority();
    paramString2 = DocumentsContract.getDocumentId((Uri)localObject1);
    if (mAuthority.equals(localObject2))
    {
      enforceTree((Uri)localObject1);
      boolean bool1 = "android:isChildDocument".equals(paramString1);
      boolean bool2 = true;
      if (bool1)
      {
        enforceReadPermissionInner((Uri)localObject1, getCallingPackage(), null);
        paramBundle = (Uri)paramBundle.getParcelable("android.content.extra.TARGET_URI");
        paramString1 = paramBundle.getAuthority();
        paramBundle = DocumentsContract.getDocumentId(paramBundle);
        if ((!mAuthority.equals(paramString1)) || (!isChildDocument(paramString2, paramBundle))) {
          bool2 = false;
        }
        localBundle.putBoolean("result", bool2);
      }
      else if ("android:createDocument".equals(paramString1))
      {
        enforceWritePermissionInner((Uri)localObject1, getCallingPackage(), null);
        localBundle.putParcelable("uri", DocumentsContract.buildDocumentUriMaybeUsingTree((Uri)localObject1, createDocument(paramString2, paramBundle.getString("mime_type"), paramBundle.getString("_display_name"))));
      }
      else if ("android:createWebLinkIntent".equals(paramString1))
      {
        enforceWritePermissionInner((Uri)localObject1, getCallingPackage(), null);
        localBundle.putParcelable("result", createWebLinkIntent(paramString2, paramBundle.getBundle("options")));
      }
      else
      {
        int i;
        if ("android:renameDocument".equals(paramString1))
        {
          enforceWritePermissionInner((Uri)localObject1, getCallingPackage(), null);
          paramString1 = renameDocument(paramString2, paramBundle.getString("_display_name"));
          if (paramString1 != null)
          {
            paramString1 = DocumentsContract.buildDocumentUriMaybeUsingTree((Uri)localObject1, paramString1);
            if (!DocumentsContract.isTreeUri(paramString1))
            {
              i = getCallingOrSelfUriPermissionModeFlags(localContext, (Uri)localObject1);
              localContext.grantUriPermission(getCallingPackage(), paramString1, i);
            }
            localBundle.putParcelable("uri", paramString1);
            revokeDocumentPermission(paramString2);
          }
        }
        else if ("android:deleteDocument".equals(paramString1))
        {
          enforceWritePermissionInner((Uri)localObject1, getCallingPackage(), null);
          deleteDocument(paramString2);
          revokeDocumentPermission(paramString2);
        }
        else if ("android:copyDocument".equals(paramString1))
        {
          paramBundle = (Uri)paramBundle.getParcelable("android.content.extra.TARGET_URI");
          paramString1 = DocumentsContract.getDocumentId(paramBundle);
          enforceReadPermissionInner((Uri)localObject1, getCallingPackage(), null);
          enforceWritePermissionInner(paramBundle, getCallingPackage(), null);
          paramString1 = copyDocument(paramString2, paramString1);
          if (paramString1 != null)
          {
            paramString1 = DocumentsContract.buildDocumentUriMaybeUsingTree((Uri)localObject1, paramString1);
            if (!DocumentsContract.isTreeUri(paramString1))
            {
              i = getCallingOrSelfUriPermissionModeFlags(localContext, (Uri)localObject1);
              localContext.grantUriPermission(getCallingPackage(), paramString1, i);
            }
            localBundle.putParcelable("uri", paramString1);
          }
        }
        else if ("android:moveDocument".equals(paramString1))
        {
          localObject2 = (Uri)paramBundle.getParcelable("parentUri");
          paramString1 = DocumentsContract.getDocumentId((Uri)localObject2);
          Uri localUri = (Uri)paramBundle.getParcelable("android.content.extra.TARGET_URI");
          paramBundle = DocumentsContract.getDocumentId(localUri);
          enforceWritePermissionInner((Uri)localObject1, getCallingPackage(), null);
          enforceReadPermissionInner((Uri)localObject2, getCallingPackage(), null);
          enforceWritePermissionInner(localUri, getCallingPackage(), null);
          paramString1 = moveDocument(paramString2, paramString1, paramBundle);
          if (paramString1 != null)
          {
            paramString1 = DocumentsContract.buildDocumentUriMaybeUsingTree((Uri)localObject1, paramString1);
            if (!DocumentsContract.isTreeUri(paramString1))
            {
              i = getCallingOrSelfUriPermissionModeFlags(localContext, (Uri)localObject1);
              localContext.grantUriPermission(getCallingPackage(), paramString1, i);
            }
            localBundle.putParcelable("uri", paramString1);
          }
        }
        else if ("android:removeDocument".equals(paramString1))
        {
          paramString1 = (Uri)paramBundle.getParcelable("parentUri");
          paramBundle = DocumentsContract.getDocumentId(paramString1);
          enforceReadPermissionInner(paramString1, getCallingPackage(), null);
          enforceWritePermissionInner((Uri)localObject1, getCallingPackage(), null);
          removeDocument(paramString2, paramBundle);
        }
        else
        {
          if (!"android:findDocumentPath".equals(paramString1)) {
            break label980;
          }
          bool2 = DocumentsContract.isTreeUri((Uri)localObject1);
          if (bool2) {
            enforceReadPermissionInner((Uri)localObject1, getCallingPackage(), null);
          } else {
            getContext().enforceCallingPermission("android.permission.MANAGE_DOCUMENTS", null);
          }
          if (bool2) {
            paramBundle = DocumentsContract.getTreeDocumentId((Uri)localObject1);
          } else {
            paramBundle = null;
          }
          localObject1 = findDocumentPath(paramBundle, paramString2);
          paramString2 = (String)localObject1;
          if (bool2)
          {
            paramString1 = (String)localObject1;
            if (!Objects.equals(((DocumentsContract.Path)localObject1).getPath().get(0), paramBundle))
            {
              paramString1 = new StringBuilder();
              paramString1.append("Provider doesn't return path from the tree root. Expected: ");
              paramString1.append(paramBundle);
              paramString1.append(" found: ");
              paramString1.append((String)((DocumentsContract.Path)localObject1).getPath().get(0));
              Log.wtf("DocumentsProvider", paramString1.toString());
              paramString1 = new LinkedList(((DocumentsContract.Path)localObject1).getPath());
              while ((paramString1.size() > 1) && (!Objects.equals(paramString1.getFirst(), paramBundle))) {
                paramString1.removeFirst();
              }
              paramString1 = new DocumentsContract.Path(null, paramString1);
            }
            paramString2 = paramString1;
            if (paramString1.getRootId() != null)
            {
              paramString2 = new StringBuilder();
              paramString2.append("Provider returns root id :");
              paramString2.append(paramString1.getRootId());
              paramString2.append(" unexpectedly. Erase root id.");
              Log.wtf("DocumentsProvider", paramString2.toString());
              paramString2 = new DocumentsContract.Path(null, paramString1.getPath());
            }
          }
          localBundle.putParcelable("result", paramString2);
        }
      }
      return localBundle;
      label980:
      if ("android:getDocumentMetadata".equals(paramString1)) {
        return getDocumentMetadata(paramString2);
      }
      paramString2 = new StringBuilder();
      paramString2.append("Method not supported ");
      paramString2.append(paramString1);
      throw new UnsupportedOperationException(paramString2.toString());
    }
    paramString1 = new StringBuilder();
    paramString1.append("Requested authority ");
    paramString1.append((String)localObject2);
    paramString1.append(" doesn't match provider ");
    paramString1.append(mAuthority);
    throw new SecurityException(paramString1.toString());
  }
  
  private void enforceTree(Uri paramUri)
  {
    if (DocumentsContract.isTreeUri(paramUri))
    {
      String str1 = DocumentsContract.getTreeDocumentId(paramUri);
      String str2 = DocumentsContract.getDocumentId(paramUri);
      if (Objects.equals(str1, str2)) {
        return;
      }
      if (!isChildDocument(str1, str2))
      {
        paramUri = new StringBuilder();
        paramUri.append("Document ");
        paramUri.append(str2);
        paramUri.append(" is not a descendant of ");
        paramUri.append(str1);
        throw new SecurityException(paramUri.toString());
      }
    }
  }
  
  private static int getCallingOrSelfUriPermissionModeFlags(Context paramContext, Uri paramUri)
  {
    int i = 0;
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 1) == 0) {
      i = 0x0 | 0x1;
    }
    int j = i;
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 2) == 0) {
      j = i | 0x2;
    }
    i = j;
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 65) == 0) {
      i = j | 0x40;
    }
    return i;
  }
  
  private static String getSortClause(Bundle paramBundle)
  {
    Bundle localBundle;
    if (paramBundle != null) {
      localBundle = paramBundle;
    } else {
      localBundle = Bundle.EMPTY;
    }
    String str = localBundle.getString("android:query-arg-sql-sort-order");
    paramBundle = str;
    if (str == null)
    {
      paramBundle = str;
      if (localBundle.containsKey("android:query-arg-sort-columns")) {
        paramBundle = ContentResolver.createSqlSortClause(localBundle);
      }
    }
    return paramBundle;
  }
  
  public static boolean mimeTypeMatches(String paramString1, String paramString2)
  {
    if (paramString2 == null) {
      return false;
    }
    if ((paramString1 != null) && (!"*/*".equals(paramString1)))
    {
      if (paramString1.equals(paramString2)) {
        return true;
      }
      if (paramString1.endsWith("/*")) {
        return paramString1.regionMatches(0, paramString2, 0, paramString1.indexOf('/'));
      }
      return false;
    }
    return true;
  }
  
  private final AssetFileDescriptor openTypedAssetFileImpl(Uri paramUri, String paramString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    enforceTree(paramUri);
    String str1 = DocumentsContract.getDocumentId(paramUri);
    if ((paramBundle != null) && (paramBundle.containsKey("android.content.extra.SIZE"))) {
      return openDocumentThumbnail(str1, (Point)paramBundle.getParcelable("android.content.extra.SIZE"), paramCancellationSignal);
    }
    if ("*/*".equals(paramString)) {
      return openAssetFile(paramUri, "r");
    }
    String str2 = getType(paramUri);
    if ((str2 != null) && (ClipDescription.compareMimeTypes(str2, paramString))) {
      return openAssetFile(paramUri, "r");
    }
    return openTypedDocument(str1, paramString, paramBundle, paramCancellationSignal);
  }
  
  private void registerAuthority(String paramString)
  {
    mAuthority = paramString;
    mMatcher = new UriMatcher(-1);
    mMatcher.addURI(mAuthority, "root", 1);
    mMatcher.addURI(mAuthority, "root/*", 2);
    mMatcher.addURI(mAuthority, "root/*/recent", 3);
    mMatcher.addURI(mAuthority, "root/*/search", 4);
    mMatcher.addURI(mAuthority, "document/*", 5);
    mMatcher.addURI(mAuthority, "document/*/children", 6);
    mMatcher.addURI(mAuthority, "tree/*/document/*", 7);
    mMatcher.addURI(mAuthority, "tree/*/document/*/children", 8);
  }
  
  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo)
  {
    registerAuthority(authority);
    if (exported)
    {
      if (grantUriPermissions)
      {
        if (("android.permission.MANAGE_DOCUMENTS".equals(readPermission)) && ("android.permission.MANAGE_DOCUMENTS".equals(writePermission)))
        {
          super.attachInfo(paramContext, paramProviderInfo);
          return;
        }
        throw new SecurityException("Provider must be protected by MANAGE_DOCUMENTS");
      }
      throw new SecurityException("Provider must grantUriPermissions");
    }
    throw new SecurityException("Provider must be exported");
  }
  
  public void attachInfoForTesting(Context paramContext, ProviderInfo paramProviderInfo)
  {
    registerAuthority(authority);
    super.attachInfoForTesting(paramContext, paramProviderInfo);
  }
  
  public Bundle call(String paramString1, String paramString2, Bundle paramBundle)
  {
    if (!paramString1.startsWith("android:")) {
      return super.call(paramString1, paramString2, paramBundle);
    }
    try
    {
      paramString1 = callUnchecked(paramString1, paramString2, paramBundle);
      return paramString1;
    }
    catch (FileNotFoundException paramString1)
    {
      throw new ParcelableException(paramString1);
    }
  }
  
  public Uri canonicalize(Uri paramUri)
  {
    Context localContext = getContext();
    if (mMatcher.match(paramUri) != 7) {
      return null;
    }
    enforceTree(paramUri);
    Uri localUri = DocumentsContract.buildDocumentUri(paramUri.getAuthority(), DocumentsContract.getDocumentId(paramUri));
    int i = getCallingOrSelfUriPermissionModeFlags(localContext, paramUri);
    localContext.grantUriPermission(getCallingPackage(), localUri, i);
    return localUri;
  }
  
  public String copyDocument(String paramString1, String paramString2)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Copy not supported");
  }
  
  public String createDocument(String paramString1, String paramString2, String paramString3)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Create not supported");
  }
  
  public IntentSender createWebLinkIntent(String paramString, Bundle paramBundle)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("createWebLink is not supported.");
  }
  
  public final int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Delete not supported");
  }
  
  public void deleteDocument(String paramString)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Delete not supported");
  }
  
  public void ejectRoot(String paramString)
  {
    throw new UnsupportedOperationException("Eject not supported");
  }
  
  public DocumentsContract.Path findDocumentPath(String paramString1, String paramString2)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("findDocumentPath not supported.");
  }
  
  public Bundle getDocumentMetadata(String paramString)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Metadata not supported");
  }
  
  /* Error */
  public String[] getDocumentStreamTypes(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aload_0
    //   6: aload_1
    //   7: aconst_null
    //   8: invokevirtual 461	android/provider/DocumentsProvider:queryDocument	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   11: astore_1
    //   12: aload_1
    //   13: astore 4
    //   15: aload_1
    //   16: astore_3
    //   17: aload_1
    //   18: invokeinterface 467 1 0
    //   23: ifeq +86 -> 109
    //   26: aload_1
    //   27: astore 4
    //   29: aload_1
    //   30: astore_3
    //   31: aload_1
    //   32: aload_1
    //   33: ldc 111
    //   35: invokeinterface 471 2 0
    //   40: invokeinterface 474 2 0
    //   45: astore 5
    //   47: aload_1
    //   48: astore 4
    //   50: aload_1
    //   51: astore_3
    //   52: ldc2_w 475
    //   55: aload_1
    //   56: aload_1
    //   57: ldc_w 478
    //   60: invokeinterface 471 2 0
    //   65: invokeinterface 482 2 0
    //   70: land
    //   71: lconst_0
    //   72: lcmp
    //   73: ifne +36 -> 109
    //   76: aload 5
    //   78: ifnull +31 -> 109
    //   81: aload_1
    //   82: astore 4
    //   84: aload_1
    //   85: astore_3
    //   86: aload_2
    //   87: aload 5
    //   89: invokestatic 484	android/provider/DocumentsProvider:mimeTypeMatches	(Ljava/lang/String;Ljava/lang/String;)Z
    //   92: ifeq +17 -> 109
    //   95: aload_1
    //   96: invokestatic 490	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   99: iconst_1
    //   100: anewarray 48	java/lang/String
    //   103: dup
    //   104: iconst_0
    //   105: aload 5
    //   107: aastore
    //   108: areturn
    //   109: aload_1
    //   110: invokestatic 490	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   113: aconst_null
    //   114: areturn
    //   115: astore_1
    //   116: aload 4
    //   118: invokestatic 490	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   121: aload_1
    //   122: athrow
    //   123: astore_1
    //   124: aload_3
    //   125: invokestatic 490	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   128: aconst_null
    //   129: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	130	0	this	DocumentsProvider
    //   0	130	1	paramString1	String
    //   0	130	2	paramString2	String
    //   1	124	3	str1	String
    //   3	114	4	str2	String
    //   45	61	5	str3	String
    // Exception table:
    //   from	to	target	type
    //   5	12	115	finally
    //   17	26	115	finally
    //   31	47	115	finally
    //   52	76	115	finally
    //   86	95	115	finally
    //   5	12	123	java/io/FileNotFoundException
    //   17	26	123	java/io/FileNotFoundException
    //   31	47	123	java/io/FileNotFoundException
    //   52	76	123	java/io/FileNotFoundException
    //   86	95	123	java/io/FileNotFoundException
  }
  
  public String getDocumentType(String paramString)
    throws FileNotFoundException
  {
    paramString = queryDocument(paramString, null);
    try
    {
      if (paramString.moveToFirst())
      {
        String str = paramString.getString(paramString.getColumnIndexOrThrow("mime_type"));
        return str;
      }
      return null;
    }
    finally
    {
      IoUtils.closeQuietly(paramString);
    }
  }
  
  public String[] getStreamTypes(Uri paramUri, String paramString)
  {
    enforceTree(paramUri);
    return getDocumentStreamTypes(DocumentsContract.getDocumentId(paramUri), paramString);
  }
  
  public final String getType(Uri paramUri)
  {
    try
    {
      int i = mMatcher.match(paramUri);
      if (i != 2)
      {
        if ((i != 5) && (i != 7)) {
          return null;
        }
        enforceTree(paramUri);
        return getDocumentType(DocumentsContract.getDocumentId(paramUri));
      }
      return "vnd.android.document/root";
    }
    catch (FileNotFoundException paramUri)
    {
      Log.w("DocumentsProvider", "Failed during getType", paramUri);
    }
    return null;
  }
  
  public final Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException("Insert not supported");
  }
  
  public boolean isChildDocument(String paramString1, String paramString2)
  {
    return false;
  }
  
  public String moveDocument(String paramString1, String paramString2, String paramString3)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Move not supported");
  }
  
  public final AssetFileDescriptor openAssetFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    enforceTree(paramUri);
    String str = DocumentsContract.getDocumentId(paramUri);
    paramUri = null;
    paramString = openDocument(str, paramString, null);
    if (paramString != null) {
      paramUri = new AssetFileDescriptor(paramString, 0L, -1L);
    }
    return paramUri;
  }
  
  public final AssetFileDescriptor openAssetFile(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    enforceTree(paramUri);
    paramUri = openDocument(DocumentsContract.getDocumentId(paramUri), paramString, paramCancellationSignal);
    if (paramUri != null) {
      paramUri = new AssetFileDescriptor(paramUri, 0L, -1L);
    } else {
      paramUri = null;
    }
    return paramUri;
  }
  
  public abstract ParcelFileDescriptor openDocument(String paramString1, String paramString2, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException;
  
  public AssetFileDescriptor openDocumentThumbnail(String paramString, Point paramPoint, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Thumbnails not supported");
  }
  
  public final ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    enforceTree(paramUri);
    return openDocument(DocumentsContract.getDocumentId(paramUri), paramString, null);
  }
  
  public final ParcelFileDescriptor openFile(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    enforceTree(paramUri);
    return openDocument(DocumentsContract.getDocumentId(paramUri), paramString, paramCancellationSignal);
  }
  
  public final AssetFileDescriptor openTypedAssetFile(Uri paramUri, String paramString, Bundle paramBundle)
    throws FileNotFoundException
  {
    return openTypedAssetFileImpl(paramUri, paramString, paramBundle, null);
  }
  
  public final AssetFileDescriptor openTypedAssetFile(Uri paramUri, String paramString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    return openTypedAssetFileImpl(paramUri, paramString, paramBundle, paramCancellationSignal);
  }
  
  public AssetFileDescriptor openTypedDocument(String paramString1, String paramString2, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    throw new FileNotFoundException("The requested MIME type is not supported.");
  }
  
  public final Cursor query(Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
  {
    try
    {
      int i = mMatcher.match(paramUri);
      if (i != 1)
      {
        switch (i)
        {
        default: 
          paramArrayOfString = new java/lang/UnsupportedOperationException;
          paramBundle = new java/lang/StringBuilder;
          paramBundle.<init>();
          paramBundle.append("Unsupported Uri ");
          paramBundle.append(paramUri);
          paramArrayOfString.<init>(paramBundle.toString());
          throw paramArrayOfString;
        case 6: 
        case 8: 
          enforceTree(paramUri);
          if (DocumentsContract.isManageMode(paramUri)) {
            return queryChildDocumentsForManage(DocumentsContract.getDocumentId(paramUri), paramArrayOfString, getSortClause(paramBundle));
          }
          return queryChildDocuments(DocumentsContract.getDocumentId(paramUri), paramArrayOfString, paramBundle);
        case 5: 
        case 7: 
          enforceTree(paramUri);
          return queryDocument(DocumentsContract.getDocumentId(paramUri), paramArrayOfString);
        case 4: 
          return querySearchDocuments(DocumentsContract.getRootId(paramUri), DocumentsContract.getSearchDocumentsQuery(paramUri), paramArrayOfString);
        }
        return queryRecentDocuments(DocumentsContract.getRootId(paramUri), paramArrayOfString);
      }
      paramUri = queryRoots(paramArrayOfString);
      return paramUri;
    }
    catch (FileNotFoundException paramUri)
    {
      Log.w("DocumentsProvider", "Failed during query", paramUri);
    }
    return null;
  }
  
  public final Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    throw new UnsupportedOperationException("Pre-Android-O query format not supported.");
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    throw new UnsupportedOperationException("Pre-Android-O query format not supported.");
  }
  
  public Cursor queryChildDocuments(String paramString, String[] paramArrayOfString, Bundle paramBundle)
    throws FileNotFoundException
  {
    return queryChildDocuments(paramString, paramArrayOfString, getSortClause(paramBundle));
  }
  
  public abstract Cursor queryChildDocuments(String paramString1, String[] paramArrayOfString, String paramString2)
    throws FileNotFoundException;
  
  public Cursor queryChildDocumentsForManage(String paramString1, String[] paramArrayOfString, String paramString2)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Manage not supported");
  }
  
  public abstract Cursor queryDocument(String paramString, String[] paramArrayOfString)
    throws FileNotFoundException;
  
  public Cursor queryRecentDocuments(String paramString, String[] paramArrayOfString)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Recent not supported");
  }
  
  public abstract Cursor queryRoots(String[] paramArrayOfString)
    throws FileNotFoundException;
  
  public Cursor querySearchDocuments(String paramString1, String paramString2, String[] paramArrayOfString)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Search not supported");
  }
  
  public void removeDocument(String paramString1, String paramString2)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Remove not supported");
  }
  
  public String renameDocument(String paramString1, String paramString2)
    throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Rename not supported");
  }
  
  public final void revokeDocumentPermission(String paramString)
  {
    Context localContext = getContext();
    localContext.revokeUriPermission(DocumentsContract.buildDocumentUri(mAuthority, paramString), -1);
    localContext.revokeUriPermission(DocumentsContract.buildTreeDocumentUri(mAuthority, paramString), -1);
  }
  
  public final int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Update not supported");
  }
}
