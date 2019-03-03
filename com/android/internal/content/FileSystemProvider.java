package com.android.internal.content;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.graphics.Point;
import android.net.Uri;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.provider.MediaStore.Files;
import android.provider.MetadataReader;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.webkit.MimeTypeMap;
import com.android.internal.annotations.GuardedBy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class FileSystemProvider
  extends DocumentsProvider
{
  private static final boolean LOG_INOTIFY = false;
  private static final String MIMETYPE_JPEG = "image/jpeg";
  private static final String MIMETYPE_JPG = "image/jpg";
  private static final String MIMETYPE_OCTET_STREAM = "application/octet-stream";
  private static final String TAG = "FileSystemProvider";
  private String[] mDefaultProjection;
  private Handler mHandler;
  @GuardedBy("mObservers")
  private final ArrayMap<File, DirectoryObserver> mObservers = new ArrayMap();
  
  public FileSystemProvider() {}
  
  private void addFolderToMediaStore(File paramFile)
  {
    if (paramFile != null)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        ContentResolver localContentResolver = getContext().getContentResolver();
        Uri localUri = MediaStore.Files.getDirectoryUri("external");
        ContentValues localContentValues = new android/content/ContentValues;
        localContentValues.<init>();
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentResolver.insert(localUri, localContentValues);
        Binder.restoreCallingIdentity(l);
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  private static String getTypeForFile(File paramFile)
  {
    if (paramFile.isDirectory()) {
      return "vnd.android.document/directory";
    }
    return getTypeForName(paramFile.getName());
  }
  
  private static String getTypeForName(String paramString)
  {
    int i = paramString.lastIndexOf('.');
    if (i >= 0)
    {
      paramString = paramString.substring(i + 1).toLowerCase();
      paramString = MimeTypeMap.getSingleton().getMimeTypeFromExtension(paramString);
      if (paramString != null) {
        return paramString;
      }
    }
    return "application/octet-stream";
  }
  
  private void moveInMediaStore(File paramFile1, File paramFile2)
  {
    if ((paramFile1 != null) && (paramFile2 != null))
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        ContentResolver localContentResolver = getContext().getContentResolver();
        Uri localUri;
        if (paramFile2.isDirectory()) {
          localUri = MediaStore.Files.getDirectoryUri("external");
        } else {
          localUri = MediaStore.Files.getContentUri("external");
        }
        ContentValues localContentValues = new android/content/ContentValues;
        localContentValues.<init>();
        localContentValues.put("_data", paramFile2.getAbsolutePath());
        paramFile1 = paramFile1.getAbsolutePath();
        localContentResolver.update(localUri, localContentValues, "_data LIKE ? AND lower(_data)=lower(?)", new String[] { paramFile1, paramFile1 });
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  private void removeFromMediaStore(File paramFile, boolean paramBoolean)
    throws FileNotFoundException
  {
    if (paramFile != null)
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        ContentResolver localContentResolver = getContext().getContentResolver();
        Uri localUri = MediaStore.Files.getContentUri("external");
        if (paramBoolean)
        {
          Object localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append(paramFile.getAbsolutePath());
          ((StringBuilder)localObject).append("/");
          localObject = ((StringBuilder)localObject).toString();
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append((String)localObject);
          localStringBuilder.append("%");
          localContentResolver.delete(localUri, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", new String[] { localStringBuilder.toString(), Integer.toString(((String)localObject).length()), localObject });
        }
        paramFile = paramFile.getAbsolutePath();
        localContentResolver.delete(localUri, "_data LIKE ?1 AND lower(_data)=lower(?2)", new String[] { paramFile, paramFile });
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  private String[] resolveProjection(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      paramArrayOfString = mDefaultProjection;
    }
    return paramArrayOfString;
  }
  
  private void scanFile(File paramFile)
  {
    Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
    localIntent.setData(Uri.fromFile(paramFile));
    getContext().sendBroadcast(localIntent);
  }
  
  private void startObserving(File paramFile, Uri paramUri)
  {
    synchronized (mObservers)
    {
      DirectoryObserver localDirectoryObserver1 = (DirectoryObserver)mObservers.get(paramFile);
      DirectoryObserver localDirectoryObserver2 = localDirectoryObserver1;
      if (localDirectoryObserver1 == null)
      {
        localDirectoryObserver2 = new com/android/internal/content/FileSystemProvider$DirectoryObserver;
        localDirectoryObserver2.<init>(paramFile, getContext().getContentResolver(), paramUri);
        localDirectoryObserver2.startWatching();
        mObservers.put(paramFile, localDirectoryObserver2);
      }
      DirectoryObserver.access$008(localDirectoryObserver2);
      return;
    }
  }
  
  private void stopObserving(File paramFile)
  {
    synchronized (mObservers)
    {
      DirectoryObserver localDirectoryObserver = (DirectoryObserver)mObservers.get(paramFile);
      if (localDirectoryObserver == null) {
        return;
      }
      DirectoryObserver.access$010(localDirectoryObserver);
      if (mRefCount == 0)
      {
        mObservers.remove(paramFile);
        localDirectoryObserver.stopWatching();
      }
      return;
    }
  }
  
  protected abstract Uri buildNotificationUri(String paramString);
  
  public String createDocument(String paramString1, String paramString2, String paramString3)
    throws FileNotFoundException
  {
    paramString3 = FileUtils.buildValidFatFilename(paramString3);
    paramString1 = getFileForDocId(paramString1);
    if (paramString1.isDirectory())
    {
      paramString3 = FileUtils.buildUniqueFile(paramString1, paramString2, paramString3);
      if ("vnd.android.document/directory".equals(paramString2))
      {
        if (paramString3.mkdir())
        {
          paramString1 = getDocIdForFile(paramString3);
          onDocIdChanged(paramString1);
          addFolderToMediaStore(getFileForDocId(paramString1, true));
          break label117;
        }
        paramString1 = new StringBuilder();
        paramString1.append("Failed to mkdir ");
        paramString1.append(paramString3);
        throw new IllegalStateException(paramString1.toString());
      }
      try
      {
        if (paramString3.createNewFile())
        {
          paramString1 = getDocIdForFile(paramString3);
          onDocIdChanged(paramString1);
          label117:
          return paramString1;
        }
        paramString2 = new java/lang/IllegalStateException;
        paramString1 = new java/lang/StringBuilder;
        paramString1.<init>();
        paramString1.append("Failed to touch ");
        paramString1.append(paramString3);
        paramString2.<init>(paramString1.toString());
        throw paramString2;
      }
      catch (IOException paramString1)
      {
        paramString2 = new StringBuilder();
        paramString2.append("Failed to touch ");
        paramString2.append(paramString3);
        paramString2.append(": ");
        paramString2.append(paramString1);
        throw new IllegalStateException(paramString2.toString());
      }
    }
    throw new IllegalArgumentException("Parent document isn't a directory");
  }
  
  public void deleteDocument(String paramString)
    throws FileNotFoundException
  {
    File localFile1 = getFileForDocId(paramString);
    File localFile2 = getFileForDocId(paramString, true);
    boolean bool = localFile1.isDirectory();
    if (bool) {
      FileUtils.deleteContents(localFile1);
    }
    if (localFile1.delete())
    {
      onDocIdChanged(paramString);
      removeFromMediaStore(localFile2, bool);
      return;
    }
    paramString = new StringBuilder();
    paramString.append("Failed to delete ");
    paramString.append(localFile1);
    throw new IllegalStateException(paramString.toString());
  }
  
  protected final List<String> findDocumentPath(File paramFile1, File paramFile2)
    throws FileNotFoundException
  {
    if (paramFile2.exists())
    {
      if (FileUtils.contains(paramFile1, paramFile2))
      {
        localObject = new LinkedList();
        while ((paramFile2 != null) && (FileUtils.contains(paramFile1, paramFile2)))
        {
          ((LinkedList)localObject).addFirst(getDocIdForFile(paramFile2));
          paramFile2 = paramFile2.getParentFile();
        }
        return localObject;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramFile2);
      ((StringBuilder)localObject).append(" is not found under ");
      ((StringBuilder)localObject).append(paramFile1);
      throw new FileNotFoundException(((StringBuilder)localObject).toString());
    }
    paramFile1 = new StringBuilder();
    paramFile1.append(paramFile2);
    paramFile1.append(" is not found.");
    throw new FileNotFoundException(paramFile1.toString());
  }
  
  protected abstract String getDocIdForFile(File paramFile)
    throws FileNotFoundException;
  
  /* Error */
  public android.os.Bundle getDocumentMetadata(String paramString)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 268	com/android/internal/content/FileSystemProvider:getFileForDocId	(Ljava/lang/String;)Ljava/io/File;
    //   5: astore_2
    //   6: aload_2
    //   7: invokevirtual 323	java/io/File:exists	()Z
    //   10: ifeq +174 -> 184
    //   13: aload_2
    //   14: invokevirtual 350	java/io/File:isFile	()Z
    //   17: ifne +14 -> 31
    //   20: ldc 27
    //   22: ldc_w 352
    //   25: invokestatic 358	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   28: pop
    //   29: aconst_null
    //   30: areturn
    //   31: aload_2
    //   32: invokevirtual 361	java/io/File:canRead	()Z
    //   35: ifne +14 -> 49
    //   38: ldc 27
    //   40: ldc_w 363
    //   43: invokestatic 358	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   46: pop
    //   47: aconst_null
    //   48: areturn
    //   49: aload_2
    //   50: invokestatic 365	com/android/internal/content/FileSystemProvider:getTypeForFile	(Ljava/io/File;)Ljava/lang/String;
    //   53: astore_3
    //   54: aload_3
    //   55: invokestatic 371	android/provider/MetadataReader:isSupportedMimeType	(Ljava/lang/String;)Z
    //   58: ifne +5 -> 63
    //   61: aconst_null
    //   62: areturn
    //   63: aconst_null
    //   64: astore 4
    //   66: aconst_null
    //   67: astore 5
    //   69: aload 5
    //   71: astore_1
    //   72: aload 4
    //   74: astore 6
    //   76: new 373	android/os/Bundle
    //   79: astore 7
    //   81: aload 5
    //   83: astore_1
    //   84: aload 4
    //   86: astore 6
    //   88: aload 7
    //   90: invokespecial 374	android/os/Bundle:<init>	()V
    //   93: aload 5
    //   95: astore_1
    //   96: aload 4
    //   98: astore 6
    //   100: new 376	java/io/FileInputStream
    //   103: astore 8
    //   105: aload 5
    //   107: astore_1
    //   108: aload 4
    //   110: astore 6
    //   112: aload 8
    //   114: aload_2
    //   115: invokevirtual 95	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   118: invokespecial 377	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   121: aload 8
    //   123: astore 5
    //   125: aload 5
    //   127: astore_1
    //   128: aload 5
    //   130: astore 6
    //   132: aload 7
    //   134: aload 5
    //   136: aload_3
    //   137: aconst_null
    //   138: invokestatic 381	android/provider/MetadataReader:getMetadata	(Landroid/os/Bundle;Ljava/io/InputStream;Ljava/lang/String;[Ljava/lang/String;)V
    //   141: aload 5
    //   143: invokestatic 387	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   146: aload 7
    //   148: areturn
    //   149: astore 6
    //   151: goto +26 -> 177
    //   154: astore 5
    //   156: aload 6
    //   158: astore_1
    //   159: ldc 27
    //   161: ldc_w 389
    //   164: aload 5
    //   166: invokestatic 393	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   169: pop
    //   170: aload 6
    //   172: invokestatic 387	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   175: aconst_null
    //   176: areturn
    //   177: aload_1
    //   178: invokestatic 387	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   181: aload 6
    //   183: athrow
    //   184: new 172	java/lang/StringBuilder
    //   187: dup
    //   188: invokespecial 173	java/lang/StringBuilder:<init>	()V
    //   191: astore 6
    //   193: aload 6
    //   195: ldc_w 395
    //   198: invokevirtual 177	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload 6
    //   204: aload_1
    //   205: invokevirtual 177	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: pop
    //   209: new 170	java/io/FileNotFoundException
    //   212: dup
    //   213: aload 6
    //   215: invokevirtual 182	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   218: invokespecial 341	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   221: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	222	0	this	FileSystemProvider
    //   0	222	1	paramString	String
    //   5	110	2	localFile	File
    //   53	84	3	str	String
    //   64	45	4	localObject1	Object
    //   67	75	5	localObject2	Object
    //   154	11	5	localIOException	IOException
    //   74	57	6	localObject3	Object
    //   149	33	6	localAutoCloseable	AutoCloseable
    //   191	23	6	localStringBuilder	StringBuilder
    //   79	68	7	localBundle	android.os.Bundle
    //   103	19	8	localFileInputStream	java.io.FileInputStream
    // Exception table:
    //   from	to	target	type
    //   76	81	149	finally
    //   88	93	149	finally
    //   100	105	149	finally
    //   112	121	149	finally
    //   132	141	149	finally
    //   159	170	149	finally
    //   76	81	154	java/io/IOException
    //   88	93	154	java/io/IOException
    //   100	105	154	java/io/IOException
    //   112	121	154	java/io/IOException
    //   132	141	154	java/io/IOException
  }
  
  public String getDocumentType(String paramString)
    throws FileNotFoundException
  {
    return getTypeForFile(getFileForDocId(paramString));
  }
  
  protected final File getFileForDocId(String paramString)
    throws FileNotFoundException
  {
    return getFileForDocId(paramString, false);
  }
  
  protected abstract File getFileForDocId(String paramString, boolean paramBoolean)
    throws FileNotFoundException;
  
  protected MatrixCursor.RowBuilder includeFile(MatrixCursor paramMatrixCursor, String paramString, File paramFile)
    throws FileNotFoundException
  {
    if (paramString == null) {
      paramString = getDocIdForFile(paramFile);
    } else {
      paramFile = getFileForDocId(paramString);
    }
    int i = 0;
    if (paramFile.canWrite()) {
      if (paramFile.isDirectory()) {
        i = 0x0 | 0x8 | 0x4 | 0x40 | 0x100;
      } else {
        i = 0x0 | 0x2 | 0x4 | 0x40 | 0x100;
      }
    }
    String str1 = getTypeForFile(paramFile);
    String str2 = paramFile.getName();
    int j = i;
    if (str1.startsWith("image/")) {
      j = i | 0x1;
    }
    i = j;
    if (typeSupportsMetadata(str1)) {
      i = j | 0x20000;
    }
    paramMatrixCursor = paramMatrixCursor.newRow();
    paramMatrixCursor.add("document_id", paramString);
    paramMatrixCursor.add("_display_name", str2);
    paramMatrixCursor.add("_size", Long.valueOf(paramFile.length()));
    paramMatrixCursor.add("mime_type", str1);
    paramMatrixCursor.add("flags", Integer.valueOf(i));
    long l = paramFile.lastModified();
    if (l > 31536000000L) {
      paramMatrixCursor.add("last_modified", Long.valueOf(l));
    }
    return paramMatrixCursor;
  }
  
  public boolean isChildDocument(String paramString1, String paramString2)
  {
    try
    {
      boolean bool = FileUtils.contains(getFileForDocId(paramString1).getCanonicalFile(), getFileForDocId(paramString2).getCanonicalFile());
      return bool;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to determine if ");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(" is child of ");
      localStringBuilder.append(paramString1);
      localStringBuilder.append(": ");
      localStringBuilder.append(localIOException);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  public String moveDocument(String paramString1, String paramString2, String paramString3)
    throws FileNotFoundException
  {
    File localFile = getFileForDocId(paramString1);
    paramString2 = new File(getFileForDocId(paramString3), localFile.getName());
    paramString3 = getFileForDocId(paramString1, true);
    if (!paramString2.exists())
    {
      if (localFile.renameTo(paramString2))
      {
        paramString2 = getDocIdForFile(paramString2);
        onDocIdChanged(paramString1);
        onDocIdChanged(paramString2);
        moveInMediaStore(paramString3, getFileForDocId(paramString2, true));
        return paramString2;
      }
      paramString1 = new StringBuilder();
      paramString1.append("Failed to move to ");
      paramString1.append(paramString2);
      throw new IllegalStateException(paramString1.toString());
    }
    paramString1 = new StringBuilder();
    paramString1.append("Already exists ");
    paramString1.append(paramString2);
    throw new IllegalStateException(paramString1.toString());
  }
  
  protected void onCreate(String[] paramArrayOfString)
  {
    mHandler = new Handler();
    mDefaultProjection = paramArrayOfString;
  }
  
  public boolean onCreate()
  {
    throw new UnsupportedOperationException("Subclass should override this and call onCreate(defaultDocumentProjection)");
  }
  
  protected void onDocIdChanged(String paramString) {}
  
  public ParcelFileDescriptor openDocument(String paramString1, String paramString2, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    File localFile = getFileForDocId(paramString1);
    paramCancellationSignal = getFileForDocId(paramString1, true);
    int i = ParcelFileDescriptor.parseMode(paramString2);
    if ((i != 268435456) && (paramCancellationSignal != null)) {
      try
      {
        Handler localHandler = mHandler;
        paramString2 = new com/android/internal/content/_$$Lambda$FileSystemProvider$y9rjeYFpkvVjwD2Whw_ujCM_C7Y;
        paramString2.<init>(this, paramString1, paramCancellationSignal);
        paramString1 = ParcelFileDescriptor.open(localFile, i, localHandler, paramString2);
        return paramString1;
      }
      catch (IOException paramString1)
      {
        paramString2 = new StringBuilder();
        paramString2.append("Failed to open for writing: ");
        paramString2.append(paramString1);
        throw new FileNotFoundException(paramString2.toString());
      }
    }
    return ParcelFileDescriptor.open(localFile, i);
  }
  
  public AssetFileDescriptor openDocumentThumbnail(String paramString, Point paramPoint, CancellationSignal paramCancellationSignal)
    throws FileNotFoundException
  {
    return DocumentsContract.openImageThumbnail(getFileForDocId(paramString));
  }
  
  public Cursor queryChildDocuments(String paramString1, String[] paramArrayOfString, String paramString2)
    throws FileNotFoundException
  {
    paramString2 = getFileForDocId(paramString1);
    paramString1 = new DirectoryCursor(resolveProjection(paramArrayOfString), paramString1, paramString2);
    paramArrayOfString = paramString2.listFiles();
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      includeFile(paramString1, null, paramArrayOfString[j]);
    }
    return paramString1;
  }
  
  public Cursor queryDocument(String paramString, String[] paramArrayOfString)
    throws FileNotFoundException
  {
    paramArrayOfString = new MatrixCursor(resolveProjection(paramArrayOfString));
    includeFile(paramArrayOfString, paramString, null);
    return paramArrayOfString;
  }
  
  protected final Cursor querySearchDocuments(File paramFile, String paramString, String[] paramArrayOfString, Set<String> paramSet)
    throws FileNotFoundException
  {
    paramString = paramString.toLowerCase();
    paramArrayOfString = new MatrixCursor(resolveProjection(paramArrayOfString));
    LinkedList localLinkedList = new LinkedList();
    localLinkedList.add(paramFile);
    while ((!localLinkedList.isEmpty()) && (paramArrayOfString.getCount() < 24))
    {
      File localFile = (File)localLinkedList.removeFirst();
      if (localFile.isDirectory())
      {
        paramFile = localFile.listFiles();
        int i = paramFile.length;
        for (int j = 0; j < i; j++) {
          localLinkedList.add(paramFile[j]);
        }
      }
      if ((localFile.getName().toLowerCase().contains(paramString)) && (!paramSet.contains(localFile.getAbsolutePath()))) {
        includeFile(paramArrayOfString, null, localFile);
      }
    }
    return paramArrayOfString;
  }
  
  public String renameDocument(String paramString1, String paramString2)
    throws FileNotFoundException
  {
    Object localObject1 = FileUtils.buildValidFatFilename(paramString2);
    Object localObject2 = getFileForDocId(paramString1);
    paramString2 = getFileForDocId(paramString1, true);
    localObject1 = FileUtils.buildUniqueFile(((File)localObject2).getParentFile(), (String)localObject1);
    if (((File)localObject2).renameTo((File)localObject1))
    {
      localObject2 = getDocIdForFile((File)localObject1);
      onDocIdChanged(paramString1);
      onDocIdChanged((String)localObject2);
      localObject1 = getFileForDocId((String)localObject2, true);
      moveInMediaStore(paramString2, (File)localObject1);
      if (!TextUtils.equals(paramString1, (CharSequence)localObject2))
      {
        scanFile((File)localObject1);
        return localObject2;
      }
      return null;
    }
    paramString1 = new StringBuilder();
    paramString1.append("Failed to rename to ");
    paramString1.append(localObject1);
    throw new IllegalStateException(paramString1.toString());
  }
  
  protected boolean typeSupportsMetadata(String paramString)
  {
    return MetadataReader.isSupportedMimeType(paramString);
  }
  
  private class DirectoryCursor
    extends MatrixCursor
  {
    private final File mFile;
    
    public DirectoryCursor(String[] paramArrayOfString, String paramString, File paramFile)
    {
      super();
      paramArrayOfString = buildNotificationUri(paramString);
      setNotificationUri(getContext().getContentResolver(), paramArrayOfString);
      mFile = paramFile;
      FileSystemProvider.this.startObserving(mFile, paramArrayOfString);
    }
    
    public void close()
    {
      super.close();
      FileSystemProvider.this.stopObserving(mFile);
    }
  }
  
  private static class DirectoryObserver
    extends FileObserver
  {
    private static final int NOTIFY_EVENTS = 4044;
    private final File mFile;
    private final Uri mNotifyUri;
    private int mRefCount = 0;
    private final ContentResolver mResolver;
    
    public DirectoryObserver(File paramFile, ContentResolver paramContentResolver, Uri paramUri)
    {
      super(4044);
      mFile = paramFile;
      mResolver = paramContentResolver;
      mNotifyUri = paramUri;
    }
    
    public void onEvent(int paramInt, String paramString)
    {
      if ((paramInt & 0xFCC) != 0) {
        mResolver.notifyChange(mNotifyUri, null, false);
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("DirectoryObserver{file=");
      localStringBuilder.append(mFile.getAbsolutePath());
      localStringBuilder.append(", ref=");
      localStringBuilder.append(mRefCount);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
