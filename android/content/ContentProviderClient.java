package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import dalvik.system.CloseGuard;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ContentProviderClient
  implements AutoCloseable
{
  private static final String TAG = "ContentProviderClient";
  @GuardedBy("ContentProviderClient.class")
  private static Handler sAnrHandler;
  private NotRespondingRunnable mAnrRunnable;
  private long mAnrTimeout;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mClosed = new AtomicBoolean();
  private final IContentProvider mContentProvider;
  private final ContentResolver mContentResolver;
  private final String mPackageName;
  private final boolean mStable;
  
  @VisibleForTesting
  public ContentProviderClient(ContentResolver paramContentResolver, IContentProvider paramIContentProvider, boolean paramBoolean)
  {
    mContentResolver = paramContentResolver;
    mContentProvider = paramIContentProvider;
    mPackageName = mPackageName;
    mStable = paramBoolean;
    mCloseGuard.open("close");
  }
  
  private void afterRemote()
  {
    if (mAnrRunnable != null) {
      sAnrHandler.removeCallbacks(mAnrRunnable);
    }
  }
  
  private void beforeRemote()
  {
    if (mAnrRunnable != null) {
      sAnrHandler.postDelayed(mAnrRunnable, mAnrTimeout);
    }
  }
  
  private boolean closeInternal()
  {
    mCloseGuard.close();
    if (mClosed.compareAndSet(false, true))
    {
      setDetectNotResponding(0L);
      if (mStable) {
        return mContentResolver.releaseProvider(mContentProvider);
      }
      return mContentResolver.releaseUnstableProvider(mContentProvider);
    }
    return false;
  }
  
  public static void releaseQuietly(ContentProviderClient paramContentProviderClient)
  {
    if (paramContentProviderClient != null) {
      try
      {
        paramContentProviderClient.release();
      }
      catch (Exception paramContentProviderClient) {}
    }
  }
  
  /* Error */
  public ContentProviderResult[] applyBatch(java.util.ArrayList<ContentProviderOperation> paramArrayList)
    throws RemoteException, OperationApplicationException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -122
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_0
    //   16: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   19: aload_1
    //   20: invokeinterface 147 3 0
    //   25: astore_1
    //   26: aload_0
    //   27: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   30: aload_1
    //   31: areturn
    //   32: astore_1
    //   33: goto +24 -> 57
    //   36: astore_1
    //   37: aload_0
    //   38: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   41: ifne +14 -> 55
    //   44: aload_0
    //   45: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   48: aload_0
    //   49: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   52: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   55: aload_1
    //   56: athrow
    //   57: aload_0
    //   58: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   61: aload_1
    //   62: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	63	0	this	ContentProviderClient
    //   0	63	1	paramArrayList	java.util.ArrayList<ContentProviderOperation>
    // Exception table:
    //   from	to	target	type
    //   11	26	32	finally
    //   37	55	32	finally
    //   55	57	32	finally
    //   11	26	36	android/os/DeadObjectException
  }
  
  /* Error */
  public int bulkInsert(Uri paramUri, ContentValues[] paramArrayOfContentValues)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_2
    //   8: ldc -94
    //   10: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_0
    //   15: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   18: aload_0
    //   19: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   22: aload_0
    //   23: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   26: aload_1
    //   27: aload_2
    //   28: invokeinterface 165 4 0
    //   33: istore_3
    //   34: aload_0
    //   35: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   38: iload_3
    //   39: ireturn
    //   40: astore_1
    //   41: goto +24 -> 65
    //   44: astore_1
    //   45: aload_0
    //   46: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   49: ifne +14 -> 63
    //   52: aload_0
    //   53: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   56: aload_0
    //   57: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   60: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   63: aload_1
    //   64: athrow
    //   65: aload_0
    //   66: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   69: aload_1
    //   70: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	71	0	this	ContentProviderClient
    //   0	71	1	paramUri	Uri
    //   0	71	2	paramArrayOfContentValues	ContentValues[]
    //   33	6	3	i	int
    // Exception table:
    //   from	to	target	type
    //   18	34	40	finally
    //   45	63	40	finally
    //   63	65	40	finally
    //   18	34	44	android/os/DeadObjectException
  }
  
  /* Error */
  public Bundle call(String paramString1, String paramString2, Bundle paramBundle)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -87
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_0
    //   16: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   19: aload_1
    //   20: aload_2
    //   21: aload_3
    //   22: invokeinterface 172 5 0
    //   27: astore_1
    //   28: aload_0
    //   29: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   32: aload_1
    //   33: areturn
    //   34: astore_1
    //   35: goto +24 -> 59
    //   38: astore_1
    //   39: aload_0
    //   40: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   43: ifne +14 -> 57
    //   46: aload_0
    //   47: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   50: aload_0
    //   51: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   54: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   57: aload_1
    //   58: athrow
    //   59: aload_0
    //   60: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   63: aload_1
    //   64: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	65	0	this	ContentProviderClient
    //   0	65	1	paramString1	String
    //   0	65	2	paramString2	String
    //   0	65	3	paramBundle	Bundle
    // Exception table:
    //   from	to	target	type
    //   11	28	34	finally
    //   39	57	34	finally
    //   57	59	34	finally
    //   11	28	38	android/os/DeadObjectException
  }
  
  /* Error */
  public final Uri canonicalize(Uri paramUri)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_0
    //   16: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   19: aload_1
    //   20: invokeinterface 177 3 0
    //   25: astore_1
    //   26: aload_0
    //   27: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   30: aload_1
    //   31: areturn
    //   32: astore_1
    //   33: goto +24 -> 57
    //   36: astore_1
    //   37: aload_0
    //   38: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   41: ifne +14 -> 55
    //   44: aload_0
    //   45: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   48: aload_0
    //   49: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   52: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   55: aload_1
    //   56: athrow
    //   57: aload_0
    //   58: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   61: aload_1
    //   62: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	63	0	this	ContentProviderClient
    //   0	63	1	paramUri	Uri
    // Exception table:
    //   from	to	target	type
    //   11	26	32	finally
    //   37	55	32	finally
    //   55	57	32	finally
    //   11	26	36	android/os/DeadObjectException
  }
  
  public void close()
  {
    closeInternal();
  }
  
  /* Error */
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_0
    //   16: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   19: aload_1
    //   20: aload_2
    //   21: aload_3
    //   22: invokeinterface 184 5 0
    //   27: istore 4
    //   29: aload_0
    //   30: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   33: iload 4
    //   35: ireturn
    //   36: astore_1
    //   37: goto +24 -> 61
    //   40: astore_1
    //   41: aload_0
    //   42: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   45: ifne +14 -> 59
    //   48: aload_0
    //   49: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   52: aload_0
    //   53: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   56: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   59: aload_1
    //   60: athrow
    //   61: aload_0
    //   62: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   65: aload_1
    //   66: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	67	0	this	ContentProviderClient
    //   0	67	1	paramUri	Uri
    //   0	67	2	paramString	String
    //   0	67	3	paramArrayOfString	String[]
    //   27	7	4	i	int
    // Exception table:
    //   from	to	target	type
    //   11	29	36	finally
    //   41	59	36	finally
    //   59	61	36	finally
    //   11	29	40	android/os/DeadObjectException
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
  
  public ContentProvider getLocalContentProvider()
  {
    return ContentProvider.coerceToLocalContentProvider(mContentProvider);
  }
  
  /* Error */
  public String[] getStreamTypes(Uri paramUri, String paramString)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_2
    //   8: ldc -51
    //   10: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_0
    //   15: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   18: aload_0
    //   19: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   22: aload_1
    //   23: aload_2
    //   24: invokeinterface 207 3 0
    //   29: astore_1
    //   30: aload_0
    //   31: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   34: aload_1
    //   35: areturn
    //   36: astore_1
    //   37: goto +24 -> 61
    //   40: astore_1
    //   41: aload_0
    //   42: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   45: ifne +14 -> 59
    //   48: aload_0
    //   49: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   52: aload_0
    //   53: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   56: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   59: aload_1
    //   60: athrow
    //   61: aload_0
    //   62: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   65: aload_1
    //   66: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	67	0	this	ContentProviderClient
    //   0	67	1	paramUri	Uri
    //   0	67	2	paramString	String
    // Exception table:
    //   from	to	target	type
    //   18	30	36	finally
    //   41	59	36	finally
    //   59	61	36	finally
    //   18	30	40	android/os/DeadObjectException
  }
  
  /* Error */
  public String getType(Uri paramUri)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_1
    //   16: invokeinterface 211 2 0
    //   21: astore_1
    //   22: aload_0
    //   23: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   26: aload_1
    //   27: areturn
    //   28: astore_1
    //   29: goto +24 -> 53
    //   32: astore_1
    //   33: aload_0
    //   34: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   37: ifne +14 -> 51
    //   40: aload_0
    //   41: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   44: aload_0
    //   45: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   48: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   51: aload_1
    //   52: athrow
    //   53: aload_0
    //   54: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   57: aload_1
    //   58: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	59	0	this	ContentProviderClient
    //   0	59	1	paramUri	Uri
    // Exception table:
    //   from	to	target	type
    //   11	22	28	finally
    //   33	51	28	finally
    //   51	53	28	finally
    //   11	22	32	android/os/DeadObjectException
  }
  
  /* Error */
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_0
    //   16: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   19: aload_1
    //   20: aload_2
    //   21: invokeinterface 216 4 0
    //   26: astore_1
    //   27: aload_0
    //   28: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   31: aload_1
    //   32: areturn
    //   33: astore_1
    //   34: goto +24 -> 58
    //   37: astore_1
    //   38: aload_0
    //   39: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   42: ifne +14 -> 56
    //   45: aload_0
    //   46: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   49: aload_0
    //   50: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   53: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   56: aload_1
    //   57: athrow
    //   58: aload_0
    //   59: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   62: aload_1
    //   63: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	64	0	this	ContentProviderClient
    //   0	64	1	paramUri	Uri
    //   0	64	2	paramContentValues	ContentValues
    // Exception table:
    //   from	to	target	type
    //   11	27	33	finally
    //   38	56	33	finally
    //   56	58	33	finally
    //   11	27	37	android/os/DeadObjectException
  }
  
  public AssetFileDescriptor openAssetFile(Uri paramUri, String paramString)
    throws RemoteException, FileNotFoundException
  {
    return openAssetFile(paramUri, paramString, null);
  }
  
  /* Error */
  public AssetFileDescriptor openAssetFile(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws RemoteException, FileNotFoundException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_2
    //   8: ldc -31
    //   10: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_0
    //   15: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   18: aconst_null
    //   19: astore 4
    //   21: aload_3
    //   22: ifnull +35 -> 57
    //   25: aload_3
    //   26: invokevirtual 230	android/os/CancellationSignal:throwIfCanceled	()V
    //   29: aload_0
    //   30: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   33: invokeinterface 234 1 0
    //   38: astore 4
    //   40: aload_3
    //   41: aload 4
    //   43: invokevirtual 238	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   46: goto +11 -> 57
    //   49: astore_1
    //   50: goto +51 -> 101
    //   53: astore_1
    //   54: goto +27 -> 81
    //   57: aload_0
    //   58: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   61: aload_0
    //   62: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   65: aload_1
    //   66: aload_2
    //   67: aload 4
    //   69: invokeinterface 241 5 0
    //   74: astore_1
    //   75: aload_0
    //   76: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   79: aload_1
    //   80: areturn
    //   81: aload_0
    //   82: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   85: ifne +14 -> 99
    //   88: aload_0
    //   89: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   92: aload_0
    //   93: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   96: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   99: aload_1
    //   100: athrow
    //   101: aload_0
    //   102: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   105: aload_1
    //   106: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	107	0	this	ContentProviderClient
    //   0	107	1	paramUri	Uri
    //   0	107	2	paramString	String
    //   0	107	3	paramCancellationSignal	CancellationSignal
    //   19	49	4	localICancellationSignal	android.os.ICancellationSignal
    // Exception table:
    //   from	to	target	type
    //   25	46	49	finally
    //   57	75	49	finally
    //   81	99	49	finally
    //   99	101	49	finally
    //   25	46	53	android/os/DeadObjectException
    //   57	75	53	android/os/DeadObjectException
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws RemoteException, FileNotFoundException
  {
    return openFile(paramUri, paramString, null);
  }
  
  /* Error */
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString, CancellationSignal paramCancellationSignal)
    throws RemoteException, FileNotFoundException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_2
    //   8: ldc -31
    //   10: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_0
    //   15: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   18: aconst_null
    //   19: astore 4
    //   21: aload_3
    //   22: ifnull +35 -> 57
    //   25: aload_3
    //   26: invokevirtual 230	android/os/CancellationSignal:throwIfCanceled	()V
    //   29: aload_0
    //   30: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   33: invokeinterface 234 1 0
    //   38: astore 4
    //   40: aload_3
    //   41: aload 4
    //   43: invokevirtual 238	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   46: goto +11 -> 57
    //   49: astore_1
    //   50: goto +52 -> 102
    //   53: astore_1
    //   54: goto +28 -> 82
    //   57: aload_0
    //   58: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   61: aload_0
    //   62: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   65: aload_1
    //   66: aload_2
    //   67: aload 4
    //   69: aconst_null
    //   70: invokeinterface 249 6 0
    //   75: astore_1
    //   76: aload_0
    //   77: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   80: aload_1
    //   81: areturn
    //   82: aload_0
    //   83: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   86: ifne +14 -> 100
    //   89: aload_0
    //   90: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   93: aload_0
    //   94: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   97: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   100: aload_1
    //   101: athrow
    //   102: aload_0
    //   103: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   106: aload_1
    //   107: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	108	0	this	ContentProviderClient
    //   0	108	1	paramUri	Uri
    //   0	108	2	paramString	String
    //   0	108	3	paramCancellationSignal	CancellationSignal
    //   19	49	4	localICancellationSignal	android.os.ICancellationSignal
    // Exception table:
    //   from	to	target	type
    //   25	46	49	finally
    //   57	76	49	finally
    //   82	100	49	finally
    //   100	102	49	finally
    //   25	46	53	android/os/DeadObjectException
    //   57	76	53	android/os/DeadObjectException
  }
  
  public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri paramUri, String paramString, Bundle paramBundle)
    throws RemoteException, FileNotFoundException
  {
    return openTypedAssetFileDescriptor(paramUri, paramString, paramBundle, null);
  }
  
  /* Error */
  public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri paramUri, String paramString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws RemoteException, FileNotFoundException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc_w 256
    //   4: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   7: pop
    //   8: aload_2
    //   9: ldc_w 258
    //   12: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   15: pop
    //   16: aload_0
    //   17: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   20: aconst_null
    //   21: astore 5
    //   23: aload 4
    //   25: ifnull +37 -> 62
    //   28: aload 4
    //   30: invokevirtual 230	android/os/CancellationSignal:throwIfCanceled	()V
    //   33: aload_0
    //   34: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   37: invokeinterface 234 1 0
    //   42: astore 5
    //   44: aload 4
    //   46: aload 5
    //   48: invokevirtual 238	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   51: goto +11 -> 62
    //   54: astore_1
    //   55: goto +52 -> 107
    //   58: astore_1
    //   59: goto +28 -> 87
    //   62: aload_0
    //   63: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   66: aload_0
    //   67: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   70: aload_1
    //   71: aload_2
    //   72: aload_3
    //   73: aload 5
    //   75: invokeinterface 262 6 0
    //   80: astore_1
    //   81: aload_0
    //   82: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   85: aload_1
    //   86: areturn
    //   87: aload_0
    //   88: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   91: ifne +14 -> 105
    //   94: aload_0
    //   95: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   98: aload_0
    //   99: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   102: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   105: aload_1
    //   106: athrow
    //   107: aload_0
    //   108: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   111: aload_1
    //   112: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	113	0	this	ContentProviderClient
    //   0	113	1	paramUri	Uri
    //   0	113	2	paramString	String
    //   0	113	3	paramBundle	Bundle
    //   0	113	4	paramCancellationSignal	CancellationSignal
    //   21	53	5	localICancellationSignal	android.os.ICancellationSignal
    // Exception table:
    //   from	to	target	type
    //   28	51	54	finally
    //   62	81	54	finally
    //   87	105	54	finally
    //   105	107	54	finally
    //   28	51	58	android/os/DeadObjectException
    //   62	81	58	android/os/DeadObjectException
  }
  
  /* Error */
  public Cursor query(Uri paramUri, String[] paramArrayOfString, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aconst_null
    //   12: astore 5
    //   14: aload 4
    //   16: ifnull +37 -> 53
    //   19: aload 4
    //   21: invokevirtual 230	android/os/CancellationSignal:throwIfCanceled	()V
    //   24: aload_0
    //   25: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   28: invokeinterface 234 1 0
    //   33: astore 5
    //   35: aload 4
    //   37: aload 5
    //   39: invokevirtual 238	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   42: goto +11 -> 53
    //   45: astore_1
    //   46: goto +72 -> 118
    //   49: astore_1
    //   50: goto +48 -> 98
    //   53: aload_0
    //   54: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   57: aload_0
    //   58: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   61: aload_1
    //   62: aload_2
    //   63: aload_3
    //   64: aload 5
    //   66: invokeinterface 267 6 0
    //   71: astore_1
    //   72: aload_1
    //   73: ifnonnull +9 -> 82
    //   76: aload_0
    //   77: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   80: aconst_null
    //   81: areturn
    //   82: new 10	android/content/ContentProviderClient$CursorWrapperInner
    //   85: dup
    //   86: aload_0
    //   87: aload_1
    //   88: invokespecial 270	android/content/ContentProviderClient$CursorWrapperInner:<init>	(Landroid/content/ContentProviderClient;Landroid/database/Cursor;)V
    //   91: astore_1
    //   92: aload_0
    //   93: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   96: aload_1
    //   97: areturn
    //   98: aload_0
    //   99: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   102: ifne +14 -> 116
    //   105: aload_0
    //   106: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   109: aload_0
    //   110: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   113: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   116: aload_1
    //   117: athrow
    //   118: aload_0
    //   119: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   122: aload_1
    //   123: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	124	0	this	ContentProviderClient
    //   0	124	1	paramUri	Uri
    //   0	124	2	paramArrayOfString	String[]
    //   0	124	3	paramBundle	Bundle
    //   0	124	4	paramCancellationSignal	CancellationSignal
    //   12	53	5	localICancellationSignal	android.os.ICancellationSignal
    // Exception table:
    //   from	to	target	type
    //   19	42	45	finally
    //   53	72	45	finally
    //   82	92	45	finally
    //   98	116	45	finally
    //   116	118	45	finally
    //   19	42	49	android/os/DeadObjectException
    //   53	72	49	android/os/DeadObjectException
    //   82	92	49	android/os/DeadObjectException
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
    throws RemoteException
  {
    return query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, null);
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
    throws RemoteException
  {
    return query(paramUri, paramArrayOfString1, ContentResolver.createSqlQueryBundle(paramString1, paramArrayOfString2, paramString2), paramCancellationSignal);
  }
  
  /* Error */
  public boolean refresh(Uri paramUri, Bundle paramBundle, CancellationSignal paramCancellationSignal)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aconst_null
    //   12: astore 4
    //   14: aload_3
    //   15: ifnull +35 -> 50
    //   18: aload_3
    //   19: invokevirtual 230	android/os/CancellationSignal:throwIfCanceled	()V
    //   22: aload_0
    //   23: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   26: invokeinterface 234 1 0
    //   31: astore 4
    //   33: aload_3
    //   34: aload 4
    //   36: invokevirtual 238	android/os/CancellationSignal:setRemote	(Landroid/os/ICancellationSignal;)V
    //   39: goto +11 -> 50
    //   42: astore_1
    //   43: goto +53 -> 96
    //   46: astore_1
    //   47: goto +29 -> 76
    //   50: aload_0
    //   51: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   54: aload_0
    //   55: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   58: aload_1
    //   59: aload_2
    //   60: aload 4
    //   62: invokeinterface 285 5 0
    //   67: istore 5
    //   69: aload_0
    //   70: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   73: iload 5
    //   75: ireturn
    //   76: aload_0
    //   77: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   80: ifne +14 -> 94
    //   83: aload_0
    //   84: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   87: aload_0
    //   88: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   91: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   94: aload_1
    //   95: athrow
    //   96: aload_0
    //   97: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   100: aload_1
    //   101: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	102	0	this	ContentProviderClient
    //   0	102	1	paramUri	Uri
    //   0	102	2	paramBundle	Bundle
    //   0	102	3	paramCancellationSignal	CancellationSignal
    //   12	49	4	localICancellationSignal	android.os.ICancellationSignal
    //   67	7	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   18	39	42	finally
    //   50	69	42	finally
    //   76	94	42	finally
    //   94	96	42	finally
    //   18	39	46	android/os/DeadObjectException
    //   50	69	46	android/os/DeadObjectException
  }
  
  @Deprecated
  public boolean release()
  {
    return closeInternal();
  }
  
  public void setDetectNotResponding(long paramLong)
  {
    try
    {
      mAnrTimeout = paramLong;
      if (paramLong > 0L)
      {
        Object localObject1;
        if (mAnrRunnable == null)
        {
          localObject1 = new android/content/ContentProviderClient$NotRespondingRunnable;
          ((NotRespondingRunnable)localObject1).<init>(this, null);
          mAnrRunnable = ((NotRespondingRunnable)localObject1);
        }
        if (sAnrHandler == null)
        {
          localObject1 = new android/os/Handler;
          ((Handler)localObject1).<init>(Looper.getMainLooper(), null, true);
          sAnrHandler = (Handler)localObject1;
        }
        Binder.allowBlocking(mContentProvider.asBinder());
      }
      else
      {
        mAnrRunnable = null;
        Binder.defaultBlocking(mContentProvider.asBinder());
      }
      return;
    }
    finally {}
  }
  
  /* Error */
  public final Uri uncanonicalize(Uri paramUri)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_0
    //   16: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   19: aload_1
    //   20: invokeinterface 315 3 0
    //   25: astore_1
    //   26: aload_0
    //   27: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   30: aload_1
    //   31: areturn
    //   32: astore_1
    //   33: goto +24 -> 57
    //   36: astore_1
    //   37: aload_0
    //   38: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   41: ifne +14 -> 55
    //   44: aload_0
    //   45: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   48: aload_0
    //   49: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   52: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   55: aload_1
    //   56: athrow
    //   57: aload_0
    //   58: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   61: aload_1
    //   62: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	63	0	this	ContentProviderClient
    //   0	63	1	paramUri	Uri
    // Exception table:
    //   from	to	target	type
    //   11	26	32	finally
    //   37	55	32	finally
    //   55	57	32	finally
    //   11	26	36	android/os/DeadObjectException
  }
  
  /* Error */
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc -96
    //   3: invokestatic 140	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokespecial 142	android/content/ContentProviderClient:beforeRemote	()V
    //   11: aload_0
    //   12: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   15: aload_0
    //   16: getfield 66	android/content/ContentProviderClient:mPackageName	Ljava/lang/String;
    //   19: aload_1
    //   20: aload_2
    //   21: aload_3
    //   22: aload 4
    //   24: invokeinterface 320 6 0
    //   29: istore 5
    //   31: aload_0
    //   32: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   35: iload 5
    //   37: ireturn
    //   38: astore_1
    //   39: goto +24 -> 63
    //   42: astore_1
    //   43: aload_0
    //   44: getfield 68	android/content/ContentProviderClient:mStable	Z
    //   47: ifne +14 -> 61
    //   50: aload_0
    //   51: getfield 59	android/content/ContentProviderClient:mContentResolver	Landroid/content/ContentResolver;
    //   54: aload_0
    //   55: getfield 61	android/content/ContentProviderClient:mContentProvider	Landroid/content/IContentProvider;
    //   58: invokevirtual 153	android/content/ContentResolver:unstableProviderDied	(Landroid/content/IContentProvider;)V
    //   61: aload_1
    //   62: athrow
    //   63: aload_0
    //   64: invokespecial 149	android/content/ContentProviderClient:afterRemote	()V
    //   67: aload_1
    //   68: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	69	0	this	ContentProviderClient
    //   0	69	1	paramUri	Uri
    //   0	69	2	paramContentValues	ContentValues
    //   0	69	3	paramString	String
    //   0	69	4	paramArrayOfString	String[]
    //   29	7	5	i	int
    // Exception table:
    //   from	to	target	type
    //   11	31	38	finally
    //   43	61	38	finally
    //   61	63	38	finally
    //   11	31	42	android/os/DeadObjectException
  }
  
  private final class CursorWrapperInner
    extends CrossProcessCursorWrapper
  {
    private final CloseGuard mCloseGuard = CloseGuard.get();
    
    CursorWrapperInner(Cursor paramCursor)
    {
      super();
      mCloseGuard.open("close");
    }
    
    public void close()
    {
      mCloseGuard.close();
      super.close();
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
  
  private class NotRespondingRunnable
    implements Runnable
  {
    private NotRespondingRunnable() {}
    
    public void run()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Detected provider not responding: ");
      localStringBuilder.append(mContentProvider);
      Log.w("ContentProviderClient", localStringBuilder.toString());
      mContentResolver.appNotRespondingViaProvider(mContentProvider);
    }
  }
}
