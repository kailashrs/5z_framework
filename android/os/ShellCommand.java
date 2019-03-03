package android.os;

import com.android.internal.util.FastPrintWriter;
import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class ShellCommand
{
  static final boolean DEBUG = false;
  static final String TAG = "ShellCommand";
  private int mArgPos;
  private String[] mArgs;
  private String mCmd;
  private String mCurArgData;
  private FileDescriptor mErr;
  private FastPrintWriter mErrPrintWriter;
  private FileOutputStream mFileErr;
  private FileInputStream mFileIn;
  private FileOutputStream mFileOut;
  private FileDescriptor mIn;
  private InputStream mInputStream;
  private FileDescriptor mOut;
  private FastPrintWriter mOutPrintWriter;
  private ResultReceiver mResultReceiver;
  private ShellCallback mShellCallback;
  private Binder mTarget;
  
  public ShellCommand() {}
  
  public ResultReceiver adoptResultReceiver()
  {
    ResultReceiver localResultReceiver = mResultReceiver;
    mResultReceiver = null;
    return localResultReceiver;
  }
  
  /* Error */
  public int exec(Binder paramBinder, FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, FileDescriptor paramFileDescriptor3, String[] paramArrayOfString, ShellCallback paramShellCallback, ResultReceiver paramResultReceiver)
  {
    // Byte code:
    //   0: aload 5
    //   2: ifnull +21 -> 23
    //   5: aload 5
    //   7: arraylength
    //   8: ifle +15 -> 23
    //   11: aload 5
    //   13: iconst_0
    //   14: aaload
    //   15: astore 8
    //   17: iconst_1
    //   18: istore 9
    //   20: goto +9 -> 29
    //   23: iconst_0
    //   24: istore 9
    //   26: aconst_null
    //   27: astore 8
    //   29: aload_0
    //   30: aload_1
    //   31: aload_2
    //   32: aload_3
    //   33: aload 4
    //   35: aload 5
    //   37: aload 6
    //   39: iload 9
    //   41: invokevirtual 56	android/os/ShellCommand:init	(Landroid/os/Binder;Ljava/io/FileDescriptor;Ljava/io/FileDescriptor;Ljava/io/FileDescriptor;[Ljava/lang/String;Landroid/os/ShellCallback;I)V
    //   44: aload_0
    //   45: aload 8
    //   47: putfield 58	android/os/ShellCommand:mCmd	Ljava/lang/String;
    //   50: aload_0
    //   51: aload 7
    //   53: putfield 46	android/os/ShellCommand:mResultReceiver	Landroid/os/ResultReceiver;
    //   56: iconst_m1
    //   57: istore 10
    //   59: aload_0
    //   60: aload_0
    //   61: getfield 58	android/os/ShellCommand:mCmd	Ljava/lang/String;
    //   64: invokevirtual 62	android/os/ShellCommand:onCommand	(Ljava/lang/String;)I
    //   67: istore 9
    //   69: iload 9
    //   71: istore 10
    //   73: aload_0
    //   74: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   77: ifnull +10 -> 87
    //   80: aload_0
    //   81: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   84: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   87: aload_0
    //   88: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   91: ifnull +10 -> 101
    //   94: aload_0
    //   95: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   98: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   101: iload 10
    //   103: istore 9
    //   105: aload_0
    //   106: getfield 46	android/os/ShellCommand:mResultReceiver	Landroid/os/ResultReceiver;
    //   109: ifnull +184 -> 293
    //   112: iload 10
    //   114: istore 9
    //   116: aload_0
    //   117: getfield 46	android/os/ShellCommand:mResultReceiver	Landroid/os/ResultReceiver;
    //   120: iload 9
    //   122: aconst_null
    //   123: invokevirtual 77	android/os/ResultReceiver:send	(ILandroid/os/Bundle;)V
    //   126: goto +167 -> 293
    //   129: astore_1
    //   130: goto +166 -> 296
    //   133: astore_2
    //   134: aload_0
    //   135: invokevirtual 81	android/os/ShellCommand:getErrPrintWriter	()Ljava/io/PrintWriter;
    //   138: astore_1
    //   139: aload_1
    //   140: invokevirtual 86	java/io/PrintWriter:println	()V
    //   143: aload_1
    //   144: ldc 88
    //   146: invokevirtual 91	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   149: aload_2
    //   150: aload_1
    //   151: invokevirtual 95	java/lang/Throwable:printStackTrace	(Ljava/io/PrintWriter;)V
    //   154: aload_0
    //   155: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   158: ifnull +10 -> 168
    //   161: aload_0
    //   162: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   165: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   168: aload_0
    //   169: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   172: ifnull +10 -> 182
    //   175: aload_0
    //   176: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   179: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   182: iload 10
    //   184: istore 9
    //   186: aload_0
    //   187: getfield 46	android/os/ShellCommand:mResultReceiver	Landroid/os/ResultReceiver;
    //   190: ifnull +103 -> 293
    //   193: iload 10
    //   195: istore 9
    //   197: goto -81 -> 116
    //   200: astore_3
    //   201: aload_0
    //   202: invokevirtual 81	android/os/ShellCommand:getErrPrintWriter	()Ljava/io/PrintWriter;
    //   205: astore_1
    //   206: new 97	java/lang/StringBuilder
    //   209: astore_2
    //   210: aload_2
    //   211: invokespecial 98	java/lang/StringBuilder:<init>	()V
    //   214: aload_2
    //   215: ldc 100
    //   217: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: pop
    //   221: aload_2
    //   222: aload_3
    //   223: invokevirtual 108	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   226: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   229: pop
    //   230: aload_1
    //   231: aload_2
    //   232: invokevirtual 111	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   235: invokevirtual 91	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   238: aload_1
    //   239: invokevirtual 86	java/io/PrintWriter:println	()V
    //   242: aload_3
    //   243: aload_1
    //   244: invokevirtual 112	java/lang/SecurityException:printStackTrace	(Ljava/io/PrintWriter;)V
    //   247: aload_0
    //   248: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   251: ifnull +10 -> 261
    //   254: aload_0
    //   255: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   258: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   261: aload_0
    //   262: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   265: ifnull +10 -> 275
    //   268: aload_0
    //   269: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   272: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   275: iload 10
    //   277: istore 9
    //   279: aload_0
    //   280: getfield 46	android/os/ShellCommand:mResultReceiver	Landroid/os/ResultReceiver;
    //   283: ifnull +10 -> 293
    //   286: iload 10
    //   288: istore 9
    //   290: goto -174 -> 116
    //   293: iload 9
    //   295: ireturn
    //   296: aload_0
    //   297: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   300: ifnull +10 -> 310
    //   303: aload_0
    //   304: getfield 64	android/os/ShellCommand:mOutPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   307: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   310: aload_0
    //   311: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   314: ifnull +10 -> 324
    //   317: aload_0
    //   318: getfield 71	android/os/ShellCommand:mErrPrintWriter	Lcom/android/internal/util/FastPrintWriter;
    //   321: invokevirtual 69	com/android/internal/util/FastPrintWriter:flush	()V
    //   324: aload_0
    //   325: getfield 46	android/os/ShellCommand:mResultReceiver	Landroid/os/ResultReceiver;
    //   328: ifnull +13 -> 341
    //   331: aload_0
    //   332: getfield 46	android/os/ShellCommand:mResultReceiver	Landroid/os/ResultReceiver;
    //   335: iload 10
    //   337: aconst_null
    //   338: invokevirtual 77	android/os/ResultReceiver:send	(ILandroid/os/Bundle;)V
    //   341: aload_1
    //   342: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	343	0	this	ShellCommand
    //   0	343	1	paramBinder	Binder
    //   0	343	2	paramFileDescriptor1	FileDescriptor
    //   0	343	3	paramFileDescriptor2	FileDescriptor
    //   0	343	4	paramFileDescriptor3	FileDescriptor
    //   0	343	5	paramArrayOfString	String[]
    //   0	343	6	paramShellCallback	ShellCallback
    //   0	343	7	paramResultReceiver	ResultReceiver
    //   15	31	8	str	String
    //   18	276	9	i	int
    //   57	279	10	j	int
    // Exception table:
    //   from	to	target	type
    //   59	69	129	finally
    //   134	154	129	finally
    //   201	247	129	finally
    //   59	69	133	java/lang/Throwable
    //   59	69	200	java/lang/SecurityException
  }
  
  public InputStream getBufferedInputStream()
  {
    if (mInputStream == null) {
      mInputStream = new BufferedInputStream(getRawInputStream());
    }
    return mInputStream;
  }
  
  public FileDescriptor getErrFileDescriptor()
  {
    return mErr;
  }
  
  public PrintWriter getErrPrintWriter()
  {
    if (mErr == null) {
      return getOutPrintWriter();
    }
    if (mErrPrintWriter == null) {
      mErrPrintWriter = new FastPrintWriter(getRawErrorStream());
    }
    return mErrPrintWriter;
  }
  
  public FileDescriptor getInFileDescriptor()
  {
    return mIn;
  }
  
  public String getNextArg()
  {
    Object localObject;
    if (mCurArgData != null)
    {
      localObject = mCurArgData;
      mCurArgData = null;
      return localObject;
    }
    if (mArgPos < mArgs.length)
    {
      localObject = mArgs;
      int i = mArgPos;
      mArgPos = (i + 1);
      return localObject[i];
    }
    return null;
  }
  
  public String getNextArgRequired()
  {
    Object localObject = getNextArg();
    if (localObject != null) {
      return localObject;
    }
    String str = mArgs[(mArgPos - 1)];
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Argument expected after \"");
    ((StringBuilder)localObject).append(str);
    ((StringBuilder)localObject).append("\"");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public String getNextOption()
  {
    if (mCurArgData == null)
    {
      if (mArgPos >= mArgs.length) {
        return null;
      }
      localObject = mArgs[mArgPos];
      if (!((String)localObject).startsWith("-")) {
        return null;
      }
      mArgPos += 1;
      if (((String)localObject).equals("--")) {
        return null;
      }
      if ((((String)localObject).length() > 1) && (((String)localObject).charAt(1) != '-'))
      {
        if (((String)localObject).length() > 2)
        {
          mCurArgData = ((String)localObject).substring(2);
          return ((String)localObject).substring(0, 2);
        }
        mCurArgData = null;
        return localObject;
      }
      mCurArgData = null;
      return localObject;
    }
    String str = mArgs[(mArgPos - 1)];
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("No argument expected after \"");
    ((StringBuilder)localObject).append(str);
    ((StringBuilder)localObject).append("\"");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public FileDescriptor getOutFileDescriptor()
  {
    return mOut;
  }
  
  public PrintWriter getOutPrintWriter()
  {
    if (mOutPrintWriter == null) {
      mOutPrintWriter = new FastPrintWriter(getRawOutputStream());
    }
    return mOutPrintWriter;
  }
  
  public OutputStream getRawErrorStream()
  {
    if (mFileErr == null) {
      mFileErr = new FileOutputStream(mErr);
    }
    return mFileErr;
  }
  
  public InputStream getRawInputStream()
  {
    if (mFileIn == null) {
      mFileIn = new FileInputStream(mIn);
    }
    return mFileIn;
  }
  
  public OutputStream getRawOutputStream()
  {
    if (mFileOut == null) {
      mFileOut = new FileOutputStream(mOut);
    }
    return mFileOut;
  }
  
  public ShellCallback getShellCallback()
  {
    return mShellCallback;
  }
  
  public int handleDefaultCommands(String paramString)
  {
    if ("dump".equals(paramString))
    {
      paramString = new String[mArgs.length - 1];
      System.arraycopy(mArgs, 1, paramString, 0, mArgs.length - 1);
      mTarget.doDump(mOut, getOutPrintWriter(), paramString);
      return 0;
    }
    if ((paramString != null) && (!"help".equals(paramString)) && (!"-h".equals(paramString)))
    {
      PrintWriter localPrintWriter = getOutPrintWriter();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown command: ");
      localStringBuilder.append(paramString);
      localPrintWriter.println(localStringBuilder.toString());
    }
    else
    {
      onHelp();
    }
    return -1;
  }
  
  public void init(Binder paramBinder, FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, FileDescriptor paramFileDescriptor3, String[] paramArrayOfString, ShellCallback paramShellCallback, int paramInt)
  {
    mTarget = paramBinder;
    mIn = paramFileDescriptor1;
    mOut = paramFileDescriptor2;
    mErr = paramFileDescriptor3;
    mArgs = paramArrayOfString;
    mShellCallback = paramShellCallback;
    mResultReceiver = null;
    mCmd = null;
    mArgPos = paramInt;
    mCurArgData = null;
    mFileIn = null;
    mFileOut = null;
    mFileErr = null;
    mOutPrintWriter = null;
    mErrPrintWriter = null;
    mInputStream = null;
  }
  
  public abstract int onCommand(String paramString);
  
  public abstract void onHelp();
  
  public ParcelFileDescriptor openFileForSystem(String paramString1, String paramString2)
  {
    try
    {
      paramString2 = getShellCallback().openFile(paramString1, "u:r:system_server:s0", paramString2);
      if (paramString2 != null) {
        return paramString2;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      paramString2 = getErrPrintWriter();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failure opening file: ");
      localStringBuilder.append(localRuntimeException.getMessage());
      paramString2.println(localStringBuilder.toString());
    }
    PrintWriter localPrintWriter = getErrPrintWriter();
    paramString2 = new StringBuilder();
    paramString2.append("Error: Unable to open file: ");
    paramString2.append(paramString1);
    localPrintWriter.println(paramString2.toString());
    getErrPrintWriter().println("Consider using a file under /data/local/tmp/");
    return null;
  }
  
  public String peekNextArg()
  {
    if (mCurArgData != null) {
      return mCurArgData;
    }
    if (mArgPos < mArgs.length) {
      return mArgs[mArgPos];
    }
    return null;
  }
}
