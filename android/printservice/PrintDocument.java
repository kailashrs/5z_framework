package android.printservice;

import android.print.PrintDocumentInfo;
import android.print.PrintJobId;

public final class PrintDocument
{
  private static final String LOG_TAG = "PrintDocument";
  private final PrintDocumentInfo mInfo;
  private final PrintJobId mPrintJobId;
  private final IPrintServiceClient mPrintServiceClient;
  
  PrintDocument(PrintJobId paramPrintJobId, IPrintServiceClient paramIPrintServiceClient, PrintDocumentInfo paramPrintDocumentInfo)
  {
    mPrintJobId = paramPrintJobId;
    mPrintServiceClient = paramIPrintServiceClient;
    mInfo = paramPrintDocumentInfo;
  }
  
  /* Error */
  public android.os.ParcelFileDescriptor getData()
  {
    // Byte code:
    //   0: invokestatic 37	android/printservice/PrintService:throwIfNotCalledOnMainThread	()V
    //   3: aconst_null
    //   4: astore_1
    //   5: aconst_null
    //   6: astore_2
    //   7: aconst_null
    //   8: astore_3
    //   9: invokestatic 43	android/os/ParcelFileDescriptor:createPipe	()[Landroid/os/ParcelFileDescriptor;
    //   12: astore 4
    //   14: aload 4
    //   16: iconst_0
    //   17: aaload
    //   18: astore 5
    //   20: aload 4
    //   22: iconst_1
    //   23: aaload
    //   24: astore 4
    //   26: aload 4
    //   28: astore_3
    //   29: aload 4
    //   31: astore_1
    //   32: aload 4
    //   34: astore_2
    //   35: aload_0
    //   36: getfield 23	android/printservice/PrintDocument:mPrintServiceClient	Landroid/printservice/IPrintServiceClient;
    //   39: aload 4
    //   41: aload_0
    //   42: getfield 21	android/printservice/PrintDocument:mPrintJobId	Landroid/print/PrintJobId;
    //   45: invokeinterface 49 3 0
    //   50: aload 4
    //   52: ifnull +12 -> 64
    //   55: aload 4
    //   57: invokevirtual 52	android/os/ParcelFileDescriptor:close	()V
    //   60: goto +4 -> 64
    //   63: astore_3
    //   64: aload 5
    //   66: areturn
    //   67: astore_1
    //   68: goto +55 -> 123
    //   71: astore_2
    //   72: aload_1
    //   73: astore_3
    //   74: ldc 8
    //   76: ldc 54
    //   78: aload_2
    //   79: invokestatic 60	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   82: pop
    //   83: aload_1
    //   84: ifnull +37 -> 121
    //   87: aload_1
    //   88: invokevirtual 52	android/os/ParcelFileDescriptor:close	()V
    //   91: goto +30 -> 121
    //   94: astore_3
    //   95: goto -4 -> 91
    //   98: astore_1
    //   99: aload_2
    //   100: astore_3
    //   101: ldc 8
    //   103: ldc 54
    //   105: aload_1
    //   106: invokestatic 60	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   109: pop
    //   110: aload_2
    //   111: ifnull +10 -> 121
    //   114: aload_2
    //   115: invokevirtual 52	android/os/ParcelFileDescriptor:close	()V
    //   118: goto -27 -> 91
    //   121: aconst_null
    //   122: areturn
    //   123: aload_3
    //   124: ifnull +11 -> 135
    //   127: aload_3
    //   128: invokevirtual 52	android/os/ParcelFileDescriptor:close	()V
    //   131: goto +4 -> 135
    //   134: astore_3
    //   135: aload_1
    //   136: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	this	PrintDocument
    //   4	28	1	localObject1	Object
    //   67	21	1	localObject2	Object
    //   98	38	1	localIOException1	java.io.IOException
    //   6	29	2	localObject3	Object
    //   71	44	2	localRemoteException1	android.os.RemoteException
    //   8	21	3	localObject4	Object
    //   63	1	3	localIOException2	java.io.IOException
    //   73	1	3	localObject5	Object
    //   94	1	3	localIOException3	java.io.IOException
    //   100	28	3	localRemoteException2	android.os.RemoteException
    //   134	1	3	localIOException4	java.io.IOException
    //   12	44	4	localObject6	Object
    //   18	47	5	localParcelFileDescriptor	android.os.ParcelFileDescriptor
    // Exception table:
    //   from	to	target	type
    //   55	60	63	java/io/IOException
    //   9	14	67	finally
    //   35	50	67	finally
    //   74	83	67	finally
    //   101	110	67	finally
    //   9	14	71	android/os/RemoteException
    //   35	50	71	android/os/RemoteException
    //   87	91	94	java/io/IOException
    //   114	118	94	java/io/IOException
    //   9	14	98	java/io/IOException
    //   35	50	98	java/io/IOException
    //   127	131	134	java/io/IOException
  }
  
  public PrintDocumentInfo getInfo()
  {
    PrintService.throwIfNotCalledOnMainThread();
    return mInfo;
  }
}
