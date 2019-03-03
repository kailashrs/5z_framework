package android.os;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ZygoteProcess
{
  private static final String LOG_TAG = "ZygoteProcess";
  static final int ZYGOTE_RETRY_MILLIS = 500;
  private List<String> mApiBlacklistExemptions = Collections.emptyList();
  private int mHiddenApiAccessLogSampleRate;
  private final Object mLock = new Object();
  private final LocalSocketAddress mSecondarySocket;
  private final LocalSocketAddress mSocket;
  private ZygoteState primaryZygoteState;
  private ZygoteState secondaryZygoteState;
  
  public ZygoteProcess(LocalSocketAddress paramLocalSocketAddress1, LocalSocketAddress paramLocalSocketAddress2)
  {
    mSocket = paramLocalSocketAddress1;
    mSecondarySocket = paramLocalSocketAddress2;
  }
  
  public ZygoteProcess(String paramString1, String paramString2)
  {
    this(new LocalSocketAddress(paramString1, LocalSocketAddress.Namespace.RESERVED), new LocalSocketAddress(paramString2, LocalSocketAddress.Namespace.RESERVED));
  }
  
  @GuardedBy("mLock")
  private static String getAbiList(BufferedWriter paramBufferedWriter, DataInputStream paramDataInputStream)
    throws IOException
  {
    paramBufferedWriter.write("1");
    paramBufferedWriter.newLine();
    paramBufferedWriter.write("--query-abi-list");
    paramBufferedWriter.newLine();
    paramBufferedWriter.flush();
    paramBufferedWriter = new byte[paramDataInputStream.readInt()];
    paramDataInputStream.readFully(paramBufferedWriter);
    return new String(paramBufferedWriter, StandardCharsets.US_ASCII);
  }
  
  @GuardedBy("mLock")
  private boolean maybeSetApiBlacklistExemptions(ZygoteState paramZygoteState, boolean paramBoolean)
  {
    if ((paramZygoteState != null) && (!paramZygoteState.isClosed()))
    {
      if ((!paramBoolean) && (mApiBlacklistExemptions.isEmpty())) {
        return true;
      }
      try
      {
        writer.write(Integer.toString(mApiBlacklistExemptions.size() + 1));
        writer.newLine();
        writer.write("--set-api-blacklist-exemptions");
        writer.newLine();
        for (int i = 0; i < mApiBlacklistExemptions.size(); i++)
        {
          writer.write((String)mApiBlacklistExemptions.get(i));
          writer.newLine();
        }
        writer.flush();
        i = inputStream.readInt();
        if (i != 0)
        {
          paramZygoteState = new java/lang/StringBuilder;
          paramZygoteState.<init>();
          paramZygoteState.append("Failed to set API blacklist exemptions; status ");
          paramZygoteState.append(i);
          Slog.e("ZygoteProcess", paramZygoteState.toString());
        }
        return true;
      }
      catch (IOException paramZygoteState)
      {
        Slog.e("ZygoteProcess", "Failed to set API blacklist exemptions", paramZygoteState);
        mApiBlacklistExemptions = Collections.emptyList();
        return false;
      }
    }
    Slog.e("ZygoteProcess", "Can't set API blacklist exemptions: no zygote connection");
    return false;
  }
  
  private void maybeSetHiddenApiAccessLogSampleRate(ZygoteState paramZygoteState)
  {
    if ((paramZygoteState != null) && (!paramZygoteState.isClosed()))
    {
      if (mHiddenApiAccessLogSampleRate == -1) {
        return;
      }
      try
      {
        writer.write(Integer.toString(1));
        writer.newLine();
        BufferedWriter localBufferedWriter = writer;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("--hidden-api-log-sampling-rate=");
        localStringBuilder.append(Integer.toString(mHiddenApiAccessLogSampleRate));
        localBufferedWriter.write(localStringBuilder.toString());
        writer.newLine();
        writer.flush();
        int i = inputStream.readInt();
        if (i != 0)
        {
          paramZygoteState = new java/lang/StringBuilder;
          paramZygoteState.<init>();
          paramZygoteState.append("Failed to set hidden API log sampling rate; status ");
          paramZygoteState.append(i);
          Slog.e("ZygoteProcess", paramZygoteState.toString());
        }
      }
      catch (IOException paramZygoteState)
      {
        Slog.e("ZygoteProcess", "Failed to set hidden API log sampling rate", paramZygoteState);
      }
      return;
    }
  }
  
  /* Error */
  @GuardedBy("mLock")
  private ZygoteState openZygoteSocketIfNeeded(String paramString)
    throws ZygoteStartFailedEx
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 33	android/os/ZygoteProcess:mLock	Ljava/lang/Object;
    //   4: invokestatic 190	java/lang/Thread:holdsLock	(Ljava/lang/Object;)Z
    //   7: ldc -64
    //   9: invokestatic 198	com/android/internal/util/Preconditions:checkState	(ZLjava/lang/String;)V
    //   12: aload_0
    //   13: getfield 200	android/os/ZygoteProcess:primaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   16: ifnull +13 -> 29
    //   19: aload_0
    //   20: getfield 200	android/os/ZygoteProcess:primaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   23: invokevirtual 114	android/os/ZygoteProcess$ZygoteState:isClosed	()Z
    //   26: ifeq +32 -> 58
    //   29: aload_0
    //   30: aload_0
    //   31: getfield 43	android/os/ZygoteProcess:mSocket	Landroid/net/LocalSocketAddress;
    //   34: invokestatic 204	android/os/ZygoteProcess$ZygoteState:connect	(Landroid/net/LocalSocketAddress;)Landroid/os/ZygoteProcess$ZygoteState;
    //   37: putfield 200	android/os/ZygoteProcess:primaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   40: aload_0
    //   41: aload_0
    //   42: getfield 200	android/os/ZygoteProcess:primaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   45: iconst_0
    //   46: invokespecial 206	android/os/ZygoteProcess:maybeSetApiBlacklistExemptions	(Landroid/os/ZygoteProcess$ZygoteState;Z)Z
    //   49: pop
    //   50: aload_0
    //   51: aload_0
    //   52: getfield 200	android/os/ZygoteProcess:primaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   55: invokespecial 208	android/os/ZygoteProcess:maybeSetHiddenApiAccessLogSampleRate	(Landroid/os/ZygoteProcess$ZygoteState;)V
    //   58: aload_0
    //   59: getfield 200	android/os/ZygoteProcess:primaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   62: aload_1
    //   63: invokevirtual 212	android/os/ZygoteProcess$ZygoteState:matches	(Ljava/lang/String;)Z
    //   66: ifeq +8 -> 74
    //   69: aload_0
    //   70: getfield 200	android/os/ZygoteProcess:primaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   73: areturn
    //   74: aload_0
    //   75: getfield 214	android/os/ZygoteProcess:secondaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   78: ifnull +13 -> 91
    //   81: aload_0
    //   82: getfield 214	android/os/ZygoteProcess:secondaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   85: invokevirtual 114	android/os/ZygoteProcess$ZygoteState:isClosed	()Z
    //   88: ifeq +32 -> 120
    //   91: aload_0
    //   92: aload_0
    //   93: getfield 45	android/os/ZygoteProcess:mSecondarySocket	Landroid/net/LocalSocketAddress;
    //   96: invokestatic 204	android/os/ZygoteProcess$ZygoteState:connect	(Landroid/net/LocalSocketAddress;)Landroid/os/ZygoteProcess$ZygoteState;
    //   99: putfield 214	android/os/ZygoteProcess:secondaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   102: aload_0
    //   103: aload_0
    //   104: getfield 214	android/os/ZygoteProcess:secondaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   107: iconst_0
    //   108: invokespecial 206	android/os/ZygoteProcess:maybeSetApiBlacklistExemptions	(Landroid/os/ZygoteProcess$ZygoteState;Z)Z
    //   111: pop
    //   112: aload_0
    //   113: aload_0
    //   114: getfield 214	android/os/ZygoteProcess:secondaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   117: invokespecial 208	android/os/ZygoteProcess:maybeSetHiddenApiAccessLogSampleRate	(Landroid/os/ZygoteProcess$ZygoteState;)V
    //   120: aload_0
    //   121: getfield 214	android/os/ZygoteProcess:secondaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   124: aload_1
    //   125: invokevirtual 212	android/os/ZygoteProcess$ZygoteState:matches	(Ljava/lang/String;)Z
    //   128: ifeq +8 -> 136
    //   131: aload_0
    //   132: getfield 214	android/os/ZygoteProcess:secondaryZygoteState	Landroid/os/ZygoteProcess$ZygoteState;
    //   135: areturn
    //   136: new 144	java/lang/StringBuilder
    //   139: dup
    //   140: invokespecial 145	java/lang/StringBuilder:<init>	()V
    //   143: astore_2
    //   144: aload_2
    //   145: ldc -40
    //   147: invokevirtual 151	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: pop
    //   151: aload_2
    //   152: aload_1
    //   153: invokevirtual 151	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: pop
    //   157: new 184	android/os/ZygoteStartFailedEx
    //   160: dup
    //   161: aload_2
    //   162: invokevirtual 157	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   165: invokespecial 218	android/os/ZygoteStartFailedEx:<init>	(Ljava/lang/String;)V
    //   168: athrow
    //   169: astore_1
    //   170: new 184	android/os/ZygoteStartFailedEx
    //   173: dup
    //   174: ldc -36
    //   176: aload_1
    //   177: invokespecial 223	android/os/ZygoteStartFailedEx:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   180: athrow
    //   181: astore_1
    //   182: new 184	android/os/ZygoteStartFailedEx
    //   185: dup
    //   186: ldc -31
    //   188: aload_1
    //   189: invokespecial 223	android/os/ZygoteStartFailedEx:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   192: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	193	0	this	ZygoteProcess
    //   0	193	1	paramString	String
    //   143	19	2	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   91	102	169	java/io/IOException
    //   29	40	181	java/io/IOException
  }
  
  private Process.ProcessStartResult startViaZygote(String arg1, String paramString2, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, boolean paramBoolean, String[] paramArrayOfString)
    throws ZygoteStartFailedEx
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("--runtime-args");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("--setuid=");
    localStringBuilder.append(paramInt1);
    localArrayList.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("--setgid=");
    localStringBuilder.append(paramInt2);
    localArrayList.add(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("--runtime-flags=");
    localStringBuilder.append(paramInt3);
    localArrayList.add(localStringBuilder.toString());
    if (paramInt4 == 1) {
      localArrayList.add("--mount-external-default");
    } else if (paramInt4 == 2) {
      localArrayList.add("--mount-external-read");
    } else if (paramInt4 == 3) {
      localArrayList.add("--mount-external-write");
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("--target-sdk-version=");
    localStringBuilder.append(paramInt5);
    localArrayList.add(localStringBuilder.toString());
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length > 0))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("--setgroups=");
      paramInt1 = paramArrayOfInt.length;
      for (paramInt2 = 0; paramInt2 < paramInt1; paramInt2++)
      {
        if (paramInt2 != 0) {
          localStringBuilder.append(',');
        }
        localStringBuilder.append(paramArrayOfInt[paramInt2]);
      }
      localArrayList.add(localStringBuilder.toString());
    }
    if (paramString2 != null)
    {
      paramArrayOfInt = new StringBuilder();
      paramArrayOfInt.append("--nice-name=");
      paramArrayOfInt.append(paramString2);
      localArrayList.add(paramArrayOfInt.toString());
    }
    if (paramString3 != null)
    {
      paramString2 = new StringBuilder();
      paramString2.append("--seinfo=");
      paramString2.append(paramString3);
      localArrayList.add(paramString2.toString());
    }
    if (paramString5 != null)
    {
      paramString2 = new StringBuilder();
      paramString2.append("--instruction-set=");
      paramString2.append(paramString5);
      localArrayList.add(paramString2.toString());
    }
    if (paramString6 != null)
    {
      paramString2 = new StringBuilder();
      paramString2.append("--app-data-dir=");
      paramString2.append(paramString6);
      localArrayList.add(paramString2.toString());
    }
    if (paramString7 != null)
    {
      localArrayList.add("--invoke-with");
      localArrayList.add(paramString7);
    }
    if (paramBoolean) {
      localArrayList.add("--start-child-zygote");
    }
    localArrayList.add(???);
    if (paramArrayOfString != null)
    {
      paramInt2 = paramArrayOfString.length;
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        localArrayList.add(paramArrayOfString[paramInt1]);
      }
    }
    synchronized (mLock)
    {
      paramString2 = zygoteSendArgsAndGetResult(openZygoteSocketIfNeeded(paramString4), localArrayList);
      return paramString2;
    }
  }
  
  public static void waitForConnectionToZygote(LocalSocketAddress paramLocalSocketAddress)
  {
    int i = 20;
    while (i >= 0) {
      try
      {
        ZygoteState.connect(paramLocalSocketAddress).close();
        return;
      }
      catch (IOException localIOException)
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("Got error connecting to zygote, retrying. msg= ");
        localStringBuilder2.append(localIOException.getMessage());
        Log.w("ZygoteProcess", localStringBuilder2.toString());
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException) {}
        i--;
      }
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("Failed to connect to Zygote through socket ");
    localStringBuilder1.append(paramLocalSocketAddress.getName());
    Slog.wtf("ZygoteProcess", localStringBuilder1.toString());
  }
  
  public static void waitForConnectionToZygote(String paramString)
  {
    waitForConnectionToZygote(new LocalSocketAddress(paramString, LocalSocketAddress.Namespace.RESERVED));
  }
  
  @GuardedBy("mLock")
  private static Process.ProcessStartResult zygoteSendArgsAndGetResult(ZygoteState paramZygoteState, ArrayList<String> paramArrayList)
    throws ZygoteStartFailedEx
  {
    try
    {
      int i = paramArrayList.size();
      int j = 0;
      int k = 0;
      while (k < i) {
        if (((String)paramArrayList.get(k)).indexOf('\n') < 0)
        {
          k++;
        }
        else
        {
          paramArrayList = new android/os/ZygoteStartFailedEx;
          paramArrayList.<init>("embedded newlines not allowed");
          throw paramArrayList;
        }
      }
      BufferedWriter localBufferedWriter = writer;
      DataInputStream localDataInputStream = inputStream;
      localBufferedWriter.write(Integer.toString(paramArrayList.size()));
      localBufferedWriter.newLine();
      for (k = j; k < i; k++)
      {
        localBufferedWriter.write((String)paramArrayList.get(k));
        localBufferedWriter.newLine();
      }
      localBufferedWriter.flush();
      paramArrayList = new android/os/Process$ProcessStartResult;
      paramArrayList.<init>();
      pid = localDataInputStream.readInt();
      usingWrapper = localDataInputStream.readBoolean();
      if (pid >= 0) {
        return paramArrayList;
      }
      paramArrayList = new android/os/ZygoteStartFailedEx;
      paramArrayList.<init>("fork() failed");
      throw paramArrayList;
    }
    catch (IOException paramArrayList)
    {
      paramZygoteState.close();
      throw new ZygoteStartFailedEx(paramArrayList);
    }
  }
  
  public void close()
  {
    if (primaryZygoteState != null) {
      primaryZygoteState.close();
    }
    if (secondaryZygoteState != null) {
      secondaryZygoteState.close();
    }
  }
  
  /* Error */
  public void establishZygoteConnectionForAbi(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 33	android/os/ZygoteProcess:mLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: aload_1
    //   9: invokespecial 268	android/os/ZygoteProcess:openZygoteSocketIfNeeded	(Ljava/lang/String;)Landroid/os/ZygoteProcess$ZygoteState;
    //   12: pop
    //   13: aload_2
    //   14: monitorexit
    //   15: return
    //   16: astore_3
    //   17: aload_2
    //   18: monitorexit
    //   19: aload_3
    //   20: athrow
    //   21: astore_2
    //   22: new 144	java/lang/StringBuilder
    //   25: dup
    //   26: invokespecial 145	java/lang/StringBuilder:<init>	()V
    //   29: astore_3
    //   30: aload_3
    //   31: ldc_w 336
    //   34: invokevirtual 151	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: pop
    //   38: aload_3
    //   39: aload_1
    //   40: invokevirtual 151	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: new 338	java/lang/RuntimeException
    //   47: dup
    //   48: aload_3
    //   49: invokevirtual 157	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   52: aload_2
    //   53: invokespecial 339	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	ZygoteProcess
    //   0	57	1	paramString	String
    //   21	32	2	localZygoteStartFailedEx	ZygoteStartFailedEx
    //   16	4	3	localObject2	Object
    //   29	20	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   7	15	16	finally
    //   17	19	16	finally
    //   0	7	21	android/os/ZygoteStartFailedEx
    //   19	21	21	android/os/ZygoteStartFailedEx
  }
  
  public LocalSocketAddress getPrimarySocketAddress()
  {
    return mSocket;
  }
  
  public boolean preloadDefault(String paramString)
    throws ZygoteStartFailedEx, IOException
  {
    synchronized (mLock)
    {
      paramString = openZygoteSocketIfNeeded(paramString);
      writer.write("1");
      writer.newLine();
      writer.write("--preload-default");
      writer.newLine();
      writer.flush();
      boolean bool;
      if (inputStream.readInt() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public boolean preloadPackageForAbi(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws ZygoteStartFailedEx, IOException
  {
    synchronized (mLock)
    {
      paramString5 = openZygoteSocketIfNeeded(paramString5);
      writer.write("5");
      writer.newLine();
      writer.write("--preload-package");
      writer.newLine();
      writer.write(paramString1);
      writer.newLine();
      writer.write(paramString2);
      writer.newLine();
      writer.write(paramString3);
      writer.newLine();
      writer.write(paramString4);
      writer.newLine();
      writer.flush();
      boolean bool;
      if (inputStream.readInt() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public boolean setApiBlacklistExemptions(List<String> paramList)
  {
    synchronized (mLock)
    {
      mApiBlacklistExemptions = paramList;
      boolean bool1 = maybeSetApiBlacklistExemptions(primaryZygoteState, true);
      boolean bool2 = bool1;
      if (bool1) {
        bool2 = maybeSetApiBlacklistExemptions(secondaryZygoteState, true);
      }
      return bool2;
    }
  }
  
  public void setHiddenApiAccessLogSampleRate(int paramInt)
  {
    synchronized (mLock)
    {
      mHiddenApiAccessLogSampleRate = paramInt;
      maybeSetHiddenApiAccessLogSampleRate(primaryZygoteState);
      maybeSetHiddenApiAccessLogSampleRate(secondaryZygoteState);
      return;
    }
  }
  
  public final Process.ProcessStartResult start(String paramString1, String paramString2, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String[] paramArrayOfString)
  {
    try
    {
      paramString1 = startViaZygote(paramString1, paramString2, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramString3, paramString4, paramString5, paramString6, paramString7, false, paramArrayOfString);
      return paramString1;
    }
    catch (ZygoteStartFailedEx paramString1)
    {
      Log.e("ZygoteProcess", "Starting VM process through Zygote failed");
      throw new RuntimeException("Starting VM process through Zygote failed", paramString1);
    }
  }
  
  public ChildZygoteProcess startChildZygote(String paramString1, String paramString2, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, String paramString3, String paramString4, String paramString5)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(paramString1);
    ((StringBuilder)localObject1).append("/");
    ((StringBuilder)localObject1).append(UUID.randomUUID().toString());
    localObject1 = new LocalSocketAddress(((StringBuilder)localObject1).toString());
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("--zygote-socket=");
    ((StringBuilder)localObject2).append(((LocalSocketAddress)localObject1).getName());
    localObject2 = ((StringBuilder)localObject2).toString();
    try
    {
      paramString1 = startViaZygote(paramString1, paramString2, paramInt1, paramInt2, paramArrayOfInt, paramInt3, 0, 0, paramString3, paramString4, paramString5, null, null, true, new String[] { localObject2 });
      return new ChildZygoteProcess((LocalSocketAddress)localObject1, pid);
    }
    catch (ZygoteStartFailedEx paramString1)
    {
      throw new RuntimeException("Starting child-zygote through Zygote failed", paramString1);
    }
  }
  
  public static class ZygoteState
  {
    final List<String> abiList;
    final DataInputStream inputStream;
    boolean mClosed;
    final LocalSocket socket;
    final BufferedWriter writer;
    
    private ZygoteState(LocalSocket paramLocalSocket, DataInputStream paramDataInputStream, BufferedWriter paramBufferedWriter, List<String> paramList)
    {
      socket = paramLocalSocket;
      inputStream = paramDataInputStream;
      writer = paramBufferedWriter;
      abiList = paramList;
    }
    
    public static ZygoteState connect(LocalSocketAddress paramLocalSocketAddress)
      throws IOException
    {
      LocalSocket localLocalSocket = new LocalSocket();
      try
      {
        localLocalSocket.connect(paramLocalSocketAddress);
        DataInputStream localDataInputStream = new java/io/DataInputStream;
        localDataInputStream.<init>(localLocalSocket.getInputStream());
        Object localObject = new java/io/OutputStreamWriter;
        ((OutputStreamWriter)localObject).<init>(localLocalSocket.getOutputStream());
        BufferedWriter localBufferedWriter = new BufferedWriter((Writer)localObject, 256);
        String str = ZygoteProcess.getAbiList(localBufferedWriter, localDataInputStream);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Process: zygote socket ");
        ((StringBuilder)localObject).append(paramLocalSocketAddress.getNamespace());
        ((StringBuilder)localObject).append("/");
        ((StringBuilder)localObject).append(paramLocalSocketAddress.getName());
        ((StringBuilder)localObject).append(" opened, supported ABIS: ");
        ((StringBuilder)localObject).append(str);
        Log.i("Zygote", ((StringBuilder)localObject).toString());
        return new ZygoteState(localLocalSocket, localDataInputStream, localBufferedWriter, Arrays.asList(str.split(",")));
      }
      catch (IOException paramLocalSocketAddress)
      {
        try
        {
          localLocalSocket.close();
        }
        catch (IOException localIOException) {}
        throw paramLocalSocketAddress;
      }
    }
    
    public void close()
    {
      try
      {
        socket.close();
      }
      catch (IOException localIOException)
      {
        Log.e("ZygoteProcess", "I/O exception on routine close", localIOException);
      }
      mClosed = true;
    }
    
    boolean isClosed()
    {
      return mClosed;
    }
    
    boolean matches(String paramString)
    {
      return abiList.contains(paramString);
    }
  }
}
