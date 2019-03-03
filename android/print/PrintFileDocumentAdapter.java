package android.print;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import java.io.File;

public class PrintFileDocumentAdapter
  extends PrintDocumentAdapter
{
  private static final String LOG_TAG = "PrintedFileDocAdapter";
  private final Context mContext;
  private final PrintDocumentInfo mDocumentInfo;
  private final File mFile;
  private WriteFileAsyncTask mWriteFileAsyncTask;
  
  public PrintFileDocumentAdapter(Context paramContext, File paramFile, PrintDocumentInfo paramPrintDocumentInfo)
  {
    if (paramFile != null)
    {
      if (paramPrintDocumentInfo != null)
      {
        mContext = paramContext;
        mFile = paramFile;
        mDocumentInfo = paramPrintDocumentInfo;
        return;
      }
      throw new IllegalArgumentException("documentInfo cannot be null!");
    }
    throw new IllegalArgumentException("File cannot be null!");
  }
  
  public void onLayout(PrintAttributes paramPrintAttributes1, PrintAttributes paramPrintAttributes2, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.LayoutResultCallback paramLayoutResultCallback, Bundle paramBundle)
  {
    paramLayoutResultCallback.onLayoutFinished(mDocumentInfo, false);
  }
  
  public void onWrite(PageRange[] paramArrayOfPageRange, ParcelFileDescriptor paramParcelFileDescriptor, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.WriteResultCallback paramWriteResultCallback)
  {
    mWriteFileAsyncTask = new WriteFileAsyncTask(paramParcelFileDescriptor, paramCancellationSignal, paramWriteResultCallback);
    mWriteFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
  }
  
  private final class WriteFileAsyncTask
    extends AsyncTask<Void, Void, Void>
  {
    private final CancellationSignal mCancellationSignal;
    private final ParcelFileDescriptor mDestination;
    private final PrintDocumentAdapter.WriteResultCallback mResultCallback;
    
    public WriteFileAsyncTask(ParcelFileDescriptor paramParcelFileDescriptor, CancellationSignal paramCancellationSignal, PrintDocumentAdapter.WriteResultCallback paramWriteResultCallback)
    {
      mDestination = paramParcelFileDescriptor;
      mResultCallback = paramWriteResultCallback;
      mCancellationSignal = paramCancellationSignal;
      mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener()
      {
        public void onCancel()
        {
          cancel(true);
        }
      });
    }
    
    /* Error */
    protected Void doInBackground(Void... paramVarArgs)
    {
      // Byte code:
      //   0: new 67	java/io/FileInputStream
      //   3: astore_2
      //   4: aload_2
      //   5: aload_0
      //   6: getfield 37	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:this$0	Landroid/print/PrintFileDocumentAdapter;
      //   9: invokestatic 71	android/print/PrintFileDocumentAdapter:access$000	(Landroid/print/PrintFileDocumentAdapter;)Ljava/io/File;
      //   12: invokespecial 74	java/io/FileInputStream:<init>	(Ljava/io/File;)V
      //   15: new 76	java/io/FileOutputStream
      //   18: astore_3
      //   19: aload_3
      //   20: aload_0
      //   21: getfield 41	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:mDestination	Landroid/os/ParcelFileDescriptor;
      //   24: invokevirtual 82	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   27: invokespecial 85	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
      //   30: aload_2
      //   31: aload_3
      //   32: aconst_null
      //   33: aload_0
      //   34: getfield 45	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:mCancellationSignal	Landroid/os/CancellationSignal;
      //   37: invokestatic 91	android/os/FileUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;Landroid/os/FileUtils$ProgressListener;Landroid/os/CancellationSignal;)J
      //   40: pop2
      //   41: aconst_null
      //   42: aload_3
      //   43: invokestatic 93	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
      //   46: aconst_null
      //   47: aload_2
      //   48: invokestatic 93	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
      //   51: goto +77 -> 128
      //   54: astore_1
      //   55: aconst_null
      //   56: astore 4
      //   58: goto +9 -> 67
      //   61: astore 4
      //   63: aload 4
      //   65: athrow
      //   66: astore_1
      //   67: aload 4
      //   69: aload_3
      //   70: invokestatic 93	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
      //   73: aload_1
      //   74: athrow
      //   75: astore 4
      //   77: aconst_null
      //   78: astore_1
      //   79: goto +8 -> 87
      //   82: astore_1
      //   83: aload_1
      //   84: athrow
      //   85: astore 4
      //   87: aload_1
      //   88: aload_2
      //   89: invokestatic 93	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
      //   92: aload 4
      //   94: athrow
      //   95: astore_1
      //   96: ldc 95
      //   98: ldc 97
      //   100: aload_1
      //   101: invokestatic 103	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   104: pop
      //   105: aload_0
      //   106: getfield 43	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:mResultCallback	Landroid/print/PrintDocumentAdapter$WriteResultCallback;
      //   109: aload_0
      //   110: getfield 37	android/print/PrintFileDocumentAdapter$WriteFileAsyncTask:this$0	Landroid/print/PrintFileDocumentAdapter;
      //   113: invokestatic 107	android/print/PrintFileDocumentAdapter:access$100	(Landroid/print/PrintFileDocumentAdapter;)Landroid/content/Context;
      //   116: ldc 108
      //   118: invokevirtual 114	android/content/Context:getString	(I)Ljava/lang/String;
      //   121: invokevirtual 120	android/print/PrintDocumentAdapter$WriteResultCallback:onWriteFailed	(Ljava/lang/CharSequence;)V
      //   124: goto +4 -> 128
      //   127: astore_1
      //   128: aconst_null
      //   129: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	130	0	this	WriteFileAsyncTask
      //   0	130	1	paramVarArgs	Void[]
      //   3	86	2	localFileInputStream	java.io.FileInputStream
      //   18	52	3	localFileOutputStream	java.io.FileOutputStream
      //   56	1	4	localObject1	Object
      //   61	7	4	localThrowable	Throwable
      //   75	1	4	localObject2	Object
      //   85	8	4	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   30	41	54	finally
      //   30	41	61	java/lang/Throwable
      //   63	66	66	finally
      //   15	30	75	finally
      //   41	46	75	finally
      //   67	75	75	finally
      //   15	30	82	java/lang/Throwable
      //   41	46	82	java/lang/Throwable
      //   67	75	82	java/lang/Throwable
      //   83	85	85	finally
      //   0	15	95	java/io/IOException
      //   46	51	95	java/io/IOException
      //   87	95	95	java/io/IOException
      //   0	15	127	android/os/OperationCanceledException
      //   46	51	127	android/os/OperationCanceledException
      //   87	95	127	android/os/OperationCanceledException
    }
    
    protected void onCancelled(Void paramVoid)
    {
      mResultCallback.onWriteFailed(mContext.getString(17041309));
    }
    
    protected void onPostExecute(Void paramVoid)
    {
      mResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
    }
  }
}
