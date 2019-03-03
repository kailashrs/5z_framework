package android.content;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.ref.WeakReference;

public abstract class AsyncQueryHandler
  extends Handler
{
  private static final int EVENT_ARG_DELETE = 4;
  private static final int EVENT_ARG_INSERT = 2;
  private static final int EVENT_ARG_QUERY = 1;
  private static final int EVENT_ARG_UPDATE = 3;
  private static final String TAG = "AsyncQuery";
  private static final boolean localLOGV = false;
  private static Looper sLooper = null;
  final WeakReference<ContentResolver> mResolver;
  private Handler mWorkerThreadHandler;
  
  public AsyncQueryHandler(ContentResolver paramContentResolver)
  {
    mResolver = new WeakReference(paramContentResolver);
    try
    {
      if (sLooper == null)
      {
        paramContentResolver = new android/os/HandlerThread;
        paramContentResolver.<init>("AsyncQueryWorker");
        paramContentResolver.start();
        sLooper = paramContentResolver.getLooper();
      }
      mWorkerThreadHandler = createHandler(sLooper);
      return;
    }
    finally {}
  }
  
  public final void cancelOperation(int paramInt)
  {
    mWorkerThreadHandler.removeMessages(paramInt);
  }
  
  protected Handler createHandler(Looper paramLooper)
  {
    return new WorkerHandler(paramLooper);
  }
  
  public void handleMessage(Message paramMessage)
  {
    WorkerArgs localWorkerArgs = (WorkerArgs)obj;
    int i = what;
    switch (arg1)
    {
    default: 
      break;
    case 4: 
      onDeleteComplete(i, cookie, ((Integer)result).intValue());
      break;
    case 3: 
      onUpdateComplete(i, cookie, ((Integer)result).intValue());
      break;
    case 2: 
      onInsertComplete(i, cookie, (Uri)result);
      break;
    case 1: 
      onQueryComplete(i, cookie, (Cursor)result);
    }
  }
  
  protected void onDeleteComplete(int paramInt1, Object paramObject, int paramInt2) {}
  
  protected void onInsertComplete(int paramInt, Object paramObject, Uri paramUri) {}
  
  protected void onQueryComplete(int paramInt, Object paramObject, Cursor paramCursor) {}
  
  protected void onUpdateComplete(int paramInt1, Object paramObject, int paramInt2) {}
  
  public final void startDelete(int paramInt, Object paramObject, Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    Message localMessage = mWorkerThreadHandler.obtainMessage(paramInt);
    arg1 = 4;
    WorkerArgs localWorkerArgs = new WorkerArgs();
    handler = this;
    uri = paramUri;
    cookie = paramObject;
    selection = paramString;
    selectionArgs = paramArrayOfString;
    obj = localWorkerArgs;
    mWorkerThreadHandler.sendMessage(localMessage);
  }
  
  public final void startInsert(int paramInt, Object paramObject, Uri paramUri, ContentValues paramContentValues)
  {
    Message localMessage = mWorkerThreadHandler.obtainMessage(paramInt);
    arg1 = 2;
    WorkerArgs localWorkerArgs = new WorkerArgs();
    handler = this;
    uri = paramUri;
    cookie = paramObject;
    values = paramContentValues;
    obj = localWorkerArgs;
    mWorkerThreadHandler.sendMessage(localMessage);
  }
  
  public void startQuery(int paramInt, Object paramObject, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    Message localMessage = mWorkerThreadHandler.obtainMessage(paramInt);
    arg1 = 1;
    WorkerArgs localWorkerArgs = new WorkerArgs();
    handler = this;
    uri = paramUri;
    projection = paramArrayOfString1;
    selection = paramString1;
    selectionArgs = paramArrayOfString2;
    orderBy = paramString2;
    cookie = paramObject;
    obj = localWorkerArgs;
    mWorkerThreadHandler.sendMessage(localMessage);
  }
  
  public final void startUpdate(int paramInt, Object paramObject, Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    Message localMessage = mWorkerThreadHandler.obtainMessage(paramInt);
    arg1 = 3;
    WorkerArgs localWorkerArgs = new WorkerArgs();
    handler = this;
    uri = paramUri;
    cookie = paramObject;
    values = paramContentValues;
    selection = paramString;
    selectionArgs = paramArrayOfString;
    obj = localWorkerArgs;
    mWorkerThreadHandler.sendMessage(localMessage);
  }
  
  protected static final class WorkerArgs
  {
    public Object cookie;
    public Handler handler;
    public String orderBy;
    public String[] projection;
    public Object result;
    public String selection;
    public String[] selectionArgs;
    public Uri uri;
    public ContentValues values;
    
    protected WorkerArgs() {}
  }
  
  protected class WorkerHandler
    extends Handler
  {
    public WorkerHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      Object localObject = (ContentResolver)mResolver.get();
      if (localObject == null) {
        return;
      }
      AsyncQueryHandler.WorkerArgs localWorkerArgs = (AsyncQueryHandler.WorkerArgs)obj;
      int i = what;
      switch (arg1)
      {
      default: 
        break;
      case 4: 
        result = Integer.valueOf(((ContentResolver)localObject).delete(uri, selection, selectionArgs));
        break;
      case 3: 
        result = Integer.valueOf(((ContentResolver)localObject).update(uri, values, selection, selectionArgs));
        break;
      case 2: 
        result = ((ContentResolver)localObject).insert(uri, values);
        break;
      case 1: 
        try
        {
          localObject = ((ContentResolver)localObject).query(uri, projection, selection, selectionArgs, orderBy);
          if (localObject != null) {
            ((Cursor)localObject).getCount();
          }
        }
        catch (Exception localException)
        {
          Log.w("AsyncQuery", "Exception thrown during handling EVENT_ARG_QUERY", localException);
          localMessage = null;
        }
        result = localMessage;
      }
      Message localMessage = handler.obtainMessage(i);
      obj = localWorkerArgs;
      arg1 = arg1;
      localMessage.sendToTarget();
    }
  }
}
