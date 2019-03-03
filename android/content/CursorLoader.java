package android.content;

import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

@Deprecated
public class CursorLoader
  extends AsyncTaskLoader<Cursor>
{
  CancellationSignal mCancellationSignal;
  Cursor mCursor;
  final Loader<Cursor>.ForceLoadContentObserver mObserver = new Loader.ForceLoadContentObserver(this);
  String[] mProjection;
  String mSelection;
  String[] mSelectionArgs;
  String mSortOrder;
  Uri mUri;
  
  public CursorLoader(Context paramContext)
  {
    super(paramContext);
  }
  
  public CursorLoader(Context paramContext, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    super(paramContext);
    mUri = paramUri;
    mProjection = paramArrayOfString1;
    mSelection = paramString1;
    mSelectionArgs = paramArrayOfString2;
    mSortOrder = paramString2;
  }
  
  public void cancelLoadInBackground()
  {
    super.cancelLoadInBackground();
    try
    {
      if (mCancellationSignal != null) {
        mCancellationSignal.cancel();
      }
      return;
    }
    finally {}
  }
  
  public void deliverResult(Cursor paramCursor)
  {
    if (isReset())
    {
      if (paramCursor != null) {
        paramCursor.close();
      }
      return;
    }
    Cursor localCursor = mCursor;
    mCursor = paramCursor;
    if (isStarted()) {
      super.deliverResult(paramCursor);
    }
    if ((localCursor != null) && (localCursor != paramCursor) && (!localCursor.isClosed())) {
      localCursor.close();
    }
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUri=");
    paramPrintWriter.println(mUri);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mProjection=");
    paramPrintWriter.println(Arrays.toString(mProjection));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelection=");
    paramPrintWriter.println(mSelection);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelectionArgs=");
    paramPrintWriter.println(Arrays.toString(mSelectionArgs));
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSortOrder=");
    paramPrintWriter.println(mSortOrder);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mCursor=");
    paramPrintWriter.println(mCursor);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mContentChanged=");
    paramPrintWriter.println(mContentChanged);
  }
  
  public String[] getProjection()
  {
    return mProjection;
  }
  
  public String getSelection()
  {
    return mSelection;
  }
  
  public String[] getSelectionArgs()
  {
    return mSelectionArgs;
  }
  
  public String getSortOrder()
  {
    return mSortOrder;
  }
  
  public Uri getUri()
  {
    return mUri;
  }
  
  /* Error */
  public Cursor loadInBackground()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual 136	android/content/CursorLoader:isLoadInBackgroundCanceled	()Z
    //   6: ifne +119 -> 125
    //   9: new 52	android/os/CancellationSignal
    //   12: astore_1
    //   13: aload_1
    //   14: invokespecial 138	android/os/CancellationSignal:<init>	()V
    //   17: aload_0
    //   18: aload_1
    //   19: putfield 50	android/content/CursorLoader:mCancellationSignal	Landroid/os/CancellationSignal;
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_0
    //   25: invokevirtual 142	android/content/CursorLoader:getContext	()Landroid/content/Context;
    //   28: invokevirtual 148	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   31: aload_0
    //   32: getfield 36	android/content/CursorLoader:mUri	Landroid/net/Uri;
    //   35: aload_0
    //   36: getfield 38	android/content/CursorLoader:mProjection	[Ljava/lang/String;
    //   39: aload_0
    //   40: getfield 40	android/content/CursorLoader:mSelection	Ljava/lang/String;
    //   43: aload_0
    //   44: getfield 42	android/content/CursorLoader:mSelectionArgs	[Ljava/lang/String;
    //   47: aload_0
    //   48: getfield 44	android/content/CursorLoader:mSortOrder	Ljava/lang/String;
    //   51: aload_0
    //   52: getfield 50	android/content/CursorLoader:mCancellationSignal	Landroid/os/CancellationSignal;
    //   55: invokevirtual 154	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   58: astore_1
    //   59: aload_1
    //   60: ifnull +32 -> 92
    //   63: aload_1
    //   64: invokeinterface 158 1 0
    //   69: pop
    //   70: aload_1
    //   71: aload_0
    //   72: getfield 32	android/content/CursorLoader:mObserver	Landroid/content/Loader$ForceLoadContentObserver;
    //   75: invokeinterface 162 2 0
    //   80: goto +12 -> 92
    //   83: astore_2
    //   84: aload_1
    //   85: invokeinterface 66 1 0
    //   90: aload_2
    //   91: athrow
    //   92: aload_0
    //   93: monitorenter
    //   94: aload_0
    //   95: aconst_null
    //   96: putfield 50	android/content/CursorLoader:mCancellationSignal	Landroid/os/CancellationSignal;
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: areturn
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: astore_1
    //   109: aload_0
    //   110: monitorenter
    //   111: aload_0
    //   112: aconst_null
    //   113: putfield 50	android/content/CursorLoader:mCancellationSignal	Landroid/os/CancellationSignal;
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_1
    //   119: athrow
    //   120: astore_1
    //   121: aload_0
    //   122: monitorexit
    //   123: aload_1
    //   124: athrow
    //   125: new 164	android/os/OperationCanceledException
    //   128: astore_1
    //   129: aload_1
    //   130: invokespecial 165	android/os/OperationCanceledException:<init>	()V
    //   133: aload_1
    //   134: athrow
    //   135: astore_1
    //   136: aload_0
    //   137: monitorexit
    //   138: aload_1
    //   139: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	140	0	this	CursorLoader
    //   12	90	1	localObject1	Object
    //   103	4	1	localObject2	Object
    //   108	11	1	localObject3	Object
    //   120	4	1	localObject4	Object
    //   128	6	1	localOperationCanceledException	android.os.OperationCanceledException
    //   135	4	1	localObject5	Object
    //   83	8	2	localRuntimeException	RuntimeException
    // Exception table:
    //   from	to	target	type
    //   63	80	83	java/lang/RuntimeException
    //   94	101	103	finally
    //   104	106	103	finally
    //   24	59	108	finally
    //   63	80	108	finally
    //   84	92	108	finally
    //   111	118	120	finally
    //   121	123	120	finally
    //   2	24	135	finally
    //   125	135	135	finally
    //   136	138	135	finally
  }
  
  public void onCanceled(Cursor paramCursor)
  {
    if ((paramCursor != null) && (!paramCursor.isClosed())) {
      paramCursor.close();
    }
  }
  
  protected void onReset()
  {
    super.onReset();
    onStopLoading();
    if ((mCursor != null) && (!mCursor.isClosed())) {
      mCursor.close();
    }
    mCursor = null;
  }
  
  protected void onStartLoading()
  {
    if (mCursor != null) {
      deliverResult(mCursor);
    }
    if ((takeContentChanged()) || (mCursor == null)) {
      forceLoad();
    }
  }
  
  protected void onStopLoading()
  {
    cancelLoad();
  }
  
  public void setProjection(String[] paramArrayOfString)
  {
    mProjection = paramArrayOfString;
  }
  
  public void setSelection(String paramString)
  {
    mSelection = paramString;
  }
  
  public void setSelectionArgs(String[] paramArrayOfString)
  {
    mSelectionArgs = paramArrayOfString;
  }
  
  public void setSortOrder(String paramString)
  {
    mSortOrder = paramString;
  }
  
  public void setUri(Uri paramUri)
  {
    mUri = paramUri;
  }
}
