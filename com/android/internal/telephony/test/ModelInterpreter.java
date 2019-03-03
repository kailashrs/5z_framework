package com.android.internal.telephony.test;

import android.os.HandlerThread;
import android.os.Looper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;

public class ModelInterpreter
  implements Runnable, SimulatedRadioControl
{
  static final int CONNECTING_PAUSE_MSEC = 500;
  static final String LOG_TAG = "ModelInterpreter";
  static final int MAX_CALLS = 6;
  static final int PROGRESS_CALL_STATE = 1;
  static final String[][] sDefaultResponses;
  private String mFinalResponse;
  HandlerThread mHandlerThread;
  InputStream mIn;
  LineReader mLineReader;
  OutputStream mOut;
  int mPausedResponseCount;
  Object mPausedResponseMonitor = new Object();
  ServerSocket mSS;
  SimulatedGsmCallState mSimulatedCallState;
  
  static
  {
    String[] arrayOfString1 = { "+CGREG=2", null };
    String[] arrayOfString2 = { "+COPS=0", null };
    String[] arrayOfString3 = { "+CFUN=1", null };
    String[] arrayOfString4 = { "+CGMI", "+CGMI: Android Model AT Interpreter\r" };
    String[] arrayOfString5 = { "+CGMM", "+CGMM: Android Model AT Interpreter\r" };
    String[] arrayOfString6 = { "+CGMR", "+CGMR: 1.0\r" };
    String[] arrayOfString7 = { "+CGSN", "000000000000000\r" };
    String[] arrayOfString8 = { "+CIMI", "320720000000000\r" };
    String[] arrayOfString9 = { "+CSCS=?", "+CSCS: (\"HEX\",\"UCS2\")\r" };
    String[] arrayOfString10 = { "+CFUN?", "+CFUN: 1\r" };
    String[] arrayOfString11 = { "+COPS=3,0;+COPS?;+COPS=3,1;+COPS?;+COPS=3,2;+COPS?", "+COPS: 0,0,\"Android\"\r+COPS: 0,1,\"Android\"\r+COPS: 0,2,\"310995\"\r" };
    String[] arrayOfString12 = { "+CREG?", "+CREG: 2,5, \"0113\", \"6614\"\r" };
    String[] arrayOfString13 = { "+CLIR?", "+CLIR: 1,3\r" };
    String[] arrayOfString14 = { "%CPVWI=2", "%CPVWI: 0\r" };
    String[] arrayOfString15 = { "+CUSD=1,\"#646#\"", "+CUSD=0,\"You have used 23 minutes\"\r" };
    String[] arrayOfString16 = { "+CRSM=192,12258,0,0,15", "+CRSM: 144,0,0000000A2FE204000FF55501020000\r" };
    String[] arrayOfString17 = { "+CRSM=192,28474,0,0,15", "+CRSM: 144,0,0000005a6f3a040011f5220102011e\r" };
    String[] arrayOfString18 = { "+CRSM=178,28474,1,4,30", "+CRSM: 144,0,437573746f6d65722043617265ffffff07818100398799f7ffffffffffff\r" };
    String[] arrayOfString19 = { "+CRSM=178,28474,2,4,30", "+CRSM: 144,0,566f696365204d61696cffffffffffff07918150367742f3ffffffffffff\r" };
    String[] arrayOfString20 = { "+CRSM=178,28474,3,4,30", "+CRSM: 144,0,4164676a6dffffffffffffffffffffff0b918188551512c221436587ff01\r" };
    String[] arrayOfString21 = { "+CRSM=178,28474,4,4,30", "+CRSM: 144,0,810101c1ffffffffffffffffffffffff068114455245f8ffffffffffffff\r" };
    String[] arrayOfString22 = { "+CRSM=192,28490,0,0,15", "+CRSM: 144,0,000000416f4a040011f5550102010d\r" };
    String[] arrayOfString23 = { "+CRSM=178,28490,1,4,13", "+CRSM: 144,0,0206092143658709ffffffffff\r" };
    sDefaultResponses = new String[][] { { "E0Q0V1", null }, { "+CMEE=2", null }, { "+CREG=2", null }, arrayOfString1, { "+CCWA=1", null }, arrayOfString2, arrayOfString3, arrayOfString4, arrayOfString5, arrayOfString6, arrayOfString7, arrayOfString8, arrayOfString9, arrayOfString10, arrayOfString11, arrayOfString12, { "+CGREG?", "+CGREG: 2,0\r" }, { "+CSQ", "+CSQ: 16,99\r" }, { "+CNMI?", "+CNMI: 1,2,2,1,1\r" }, arrayOfString13, arrayOfString14, arrayOfString15, { "+CRSM=176,12258,0,0,10", "+CRSM: 144,0,981062200050259429F6\r" }, arrayOfString16, arrayOfString17, arrayOfString18, arrayOfString19, arrayOfString20, arrayOfString21, arrayOfString22, arrayOfString23 };
  }
  
  public ModelInterpreter(InputStream paramInputStream, OutputStream paramOutputStream)
  {
    mIn = paramInputStream;
    mOut = paramOutputStream;
    init();
  }
  
  public ModelInterpreter(InetSocketAddress paramInetSocketAddress)
    throws IOException
  {
    mSS = new ServerSocket();
    mSS.setReuseAddress(true);
    mSS.bind(paramInetSocketAddress);
    init();
  }
  
  private void init()
  {
    new Thread(this, "ModelInterpreter").start();
    mHandlerThread = new HandlerThread("ModelInterpreter");
    mHandlerThread.start();
    mSimulatedCallState = new SimulatedGsmCallState(mHandlerThread.getLooper());
  }
  
  private void onAnswer()
    throws InterpreterEx
  {
    if (mSimulatedCallState.onAnswer()) {
      return;
    }
    throw new InterpreterEx("ERROR");
  }
  
  private void onCHLD(String paramString)
    throws InterpreterEx
  {
    char c1 = '\000';
    char c2 = paramString.charAt(6);
    char c3 = c1;
    if (paramString.length() >= 8)
    {
      c1 = paramString.charAt(7);
      c3 = c1;
    }
    if (mSimulatedCallState.onChld(c2, c3)) {
      return;
    }
    throw new InterpreterEx("ERROR");
  }
  
  private void onCLCC()
  {
    List localList = mSimulatedCallState.getClccLines();
    int i = 0;
    int j = localList.size();
    while (i < j)
    {
      println((String)localList.get(i));
      i++;
    }
  }
  
  private void onDial(String paramString)
    throws InterpreterEx
  {
    if (mSimulatedCallState.onDial(paramString.substring(1))) {
      return;
    }
    throw new InterpreterEx("ERROR");
  }
  
  private void onHangup()
    throws InterpreterEx
  {
    if (mSimulatedCallState.onAnswer())
    {
      mFinalResponse = "NO CARRIER";
      return;
    }
    throw new InterpreterEx("ERROR");
  }
  
  private void onSMSSend(String paramString)
  {
    print("> ");
    mLineReader.getNextLineCtrlZ();
    println("+CMGS: 1");
  }
  
  public void pauseResponses()
  {
    synchronized (mPausedResponseMonitor)
    {
      mPausedResponseCount += 1;
      return;
    }
  }
  
  /* Error */
  void print(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ldc_w 286
    //   6: invokevirtual 290	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   9: astore_1
    //   10: aload_0
    //   11: getfield 166	com/android/internal/telephony/test/ModelInterpreter:mOut	Ljava/io/OutputStream;
    //   14: aload_1
    //   15: invokevirtual 296	java/io/OutputStream:write	([B)V
    //   18: goto +12 -> 30
    //   21: astore_1
    //   22: goto +11 -> 33
    //   25: astore_1
    //   26: aload_1
    //   27: invokevirtual 299	java/io/IOException:printStackTrace	()V
    //   30: aload_0
    //   31: monitorexit
    //   32: return
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	37	0	this	ModelInterpreter
    //   0	37	1	paramString	String
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
    //   26	30	21	finally
    //   30	32	21	finally
    //   33	35	21	finally
    //   2	18	25	java/io/IOException
  }
  
  /* Error */
  void println(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ldc_w 286
    //   6: invokevirtual 290	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   9: astore_1
    //   10: aload_0
    //   11: getfield 166	com/android/internal/telephony/test/ModelInterpreter:mOut	Ljava/io/OutputStream;
    //   14: aload_1
    //   15: invokevirtual 296	java/io/OutputStream:write	([B)V
    //   18: aload_0
    //   19: getfield 166	com/android/internal/telephony/test/ModelInterpreter:mOut	Ljava/io/OutputStream;
    //   22: bipush 13
    //   24: invokevirtual 302	java/io/OutputStream:write	(I)V
    //   27: goto +12 -> 39
    //   30: astore_1
    //   31: goto +11 -> 42
    //   34: astore_1
    //   35: aload_1
    //   36: invokevirtual 299	java/io/IOException:printStackTrace	()V
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_1
    //   45: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	46	0	this	ModelInterpreter
    //   0	46	1	paramString	String
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
    //   35	39	30	finally
    //   39	41	30	finally
    //   42	44	30	finally
    //   2	27	34	java/io/IOException
  }
  
  void processLine(String paramString)
    throws InterpreterEx
  {
    paramString = splitCommands(paramString);
    int i = 0;
    while (i < paramString.length)
    {
      String str = paramString[i];
      if (str.equals("A"))
      {
        onAnswer();
      }
      else if (str.equals("H"))
      {
        onHangup();
      }
      else if (str.startsWith("+CHLD="))
      {
        onCHLD(str);
      }
      else if (str.equals("+CLCC"))
      {
        onCLCC();
      }
      else if (str.startsWith("D"))
      {
        onDial(str);
      }
      else if (str.startsWith("+CMGS="))
      {
        onSMSSend(str);
      }
      else
      {
        int j = 0;
        int m;
        for (int k = 0;; k++)
        {
          m = j;
          if (k >= sDefaultResponses.length) {
            break;
          }
          if (str.equals(sDefaultResponses[k][0]))
          {
            str = sDefaultResponses[k][1];
            if (str != null) {
              println(str);
            }
            m = 1;
            break;
          }
        }
        if (m == 0) {
          break label198;
        }
      }
      i++;
      continue;
      label198:
      throw new InterpreterEx("ERROR");
    }
  }
  
  public void progressConnectingCallState()
  {
    mSimulatedCallState.progressConnectingCallState();
  }
  
  public void progressConnectingToActive()
  {
    mSimulatedCallState.progressConnectingToActive();
  }
  
  public void resumeResponses()
  {
    synchronized (mPausedResponseMonitor)
    {
      mPausedResponseCount -= 1;
      if (mPausedResponseCount == 0) {
        mPausedResponseMonitor.notifyAll();
      }
      return;
    }
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 177	com/android/internal/telephony/test/ModelInterpreter:mSS	Ljava/net/ServerSocket;
    //   4: ifnull +65 -> 69
    //   7: aload_0
    //   8: getfield 177	com/android/internal/telephony/test/ModelInterpreter:mSS	Ljava/net/ServerSocket;
    //   11: invokevirtual 357	java/net/ServerSocket:accept	()Ljava/net/Socket;
    //   14: astore_1
    //   15: aload_0
    //   16: aload_1
    //   17: invokevirtual 363	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   20: putfield 164	com/android/internal/telephony/test/ModelInterpreter:mIn	Ljava/io/InputStream;
    //   23: aload_0
    //   24: aload_1
    //   25: invokevirtual 367	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   28: putfield 166	com/android/internal/telephony/test/ModelInterpreter:mOut	Ljava/io/OutputStream;
    //   31: ldc 15
    //   33: ldc_w 369
    //   36: invokestatic 375	android/telephony/Rlog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   39: pop
    //   40: goto +29 -> 69
    //   43: astore_1
    //   44: ldc 15
    //   46: ldc_w 377
    //   49: aload_1
    //   50: invokestatic 381	android/telephony/Rlog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   53: pop
    //   54: goto -54 -> 0
    //   57: astore_1
    //   58: ldc 15
    //   60: ldc_w 383
    //   63: aload_1
    //   64: invokestatic 381	android/telephony/Rlog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   67: pop
    //   68: return
    //   69: aload_0
    //   70: new 275	com/android/internal/telephony/test/LineReader
    //   73: dup
    //   74: aload_0
    //   75: getfield 164	com/android/internal/telephony/test/ModelInterpreter:mIn	Ljava/io/InputStream;
    //   78: invokespecial 386	com/android/internal/telephony/test/LineReader:<init>	(Ljava/io/InputStream;)V
    //   81: putfield 273	com/android/internal/telephony/test/ModelInterpreter:mLineReader	Lcom/android/internal/telephony/test/LineReader;
    //   84: aload_0
    //   85: ldc_w 388
    //   88: invokevirtual 252	com/android/internal/telephony/test/ModelInterpreter:println	(Ljava/lang/String;)V
    //   91: aload_0
    //   92: getfield 273	com/android/internal/telephony/test/ModelInterpreter:mLineReader	Lcom/android/internal/telephony/test/LineReader;
    //   95: invokevirtual 391	com/android/internal/telephony/test/LineReader:getNextLine	()Ljava/lang/String;
    //   98: astore_2
    //   99: aload_2
    //   100: ifnonnull +20 -> 120
    //   103: ldc 15
    //   105: ldc_w 393
    //   108: invokestatic 375	android/telephony/Rlog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   111: pop
    //   112: aload_0
    //   113: getfield 177	com/android/internal/telephony/test/ModelInterpreter:mSS	Ljava/net/ServerSocket;
    //   116: ifnonnull -116 -> 0
    //   119: return
    //   120: aload_0
    //   121: getfield 162	com/android/internal/telephony/test/ModelInterpreter:mPausedResponseMonitor	Ljava/lang/Object;
    //   124: astore_1
    //   125: aload_1
    //   126: monitorenter
    //   127: aload_0
    //   128: getfield 284	com/android/internal/telephony/test/ModelInterpreter:mPausedResponseCount	I
    //   131: istore_3
    //   132: iload_3
    //   133: ifle +18 -> 151
    //   136: aload_0
    //   137: getfield 162	com/android/internal/telephony/test/ModelInterpreter:mPausedResponseMonitor	Ljava/lang/Object;
    //   140: invokevirtual 396	java/lang/Object:wait	()V
    //   143: goto +5 -> 148
    //   146: astore 4
    //   148: goto -21 -> 127
    //   151: aload_1
    //   152: monitorexit
    //   153: aload_0
    //   154: monitorenter
    //   155: aload_0
    //   156: ldc_w 398
    //   159: putfield 265	com/android/internal/telephony/test/ModelInterpreter:mFinalResponse	Ljava/lang/String;
    //   162: aload_0
    //   163: aload_2
    //   164: invokevirtual 400	com/android/internal/telephony/test/ModelInterpreter:processLine	(Ljava/lang/String;)V
    //   167: aload_0
    //   168: aload_0
    //   169: getfield 265	com/android/internal/telephony/test/ModelInterpreter:mFinalResponse	Ljava/lang/String;
    //   172: invokevirtual 252	com/android/internal/telephony/test/ModelInterpreter:println	(Ljava/lang/String;)V
    //   175: goto +30 -> 205
    //   178: astore_1
    //   179: goto +31 -> 210
    //   182: astore_1
    //   183: aload_1
    //   184: invokevirtual 401	java/lang/RuntimeException:printStackTrace	()V
    //   187: aload_0
    //   188: ldc -35
    //   190: invokevirtual 252	com/android/internal/telephony/test/ModelInterpreter:println	(Ljava/lang/String;)V
    //   193: goto +12 -> 205
    //   196: astore_1
    //   197: aload_0
    //   198: aload_1
    //   199: getfield 404	com/android/internal/telephony/test/InterpreterEx:mResult	Ljava/lang/String;
    //   202: invokevirtual 252	com/android/internal/telephony/test/ModelInterpreter:println	(Ljava/lang/String;)V
    //   205: aload_0
    //   206: monitorexit
    //   207: goto -116 -> 91
    //   210: aload_0
    //   211: monitorexit
    //   212: aload_1
    //   213: athrow
    //   214: astore 4
    //   216: aload_1
    //   217: monitorexit
    //   218: aload 4
    //   220: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	221	0	this	ModelInterpreter
    //   14	11	1	localSocket	java.net.Socket
    //   43	7	1	localIOException1	IOException
    //   57	7	1	localIOException2	IOException
    //   124	28	1	localObject1	Object
    //   178	1	1	localObject2	Object
    //   182	2	1	localRuntimeException	RuntimeException
    //   196	21	1	localInterpreterEx	InterpreterEx
    //   98	66	2	str	String
    //   131	2	3	i	int
    //   146	1	4	localInterruptedException	InterruptedException
    //   214	5	4	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   15	31	43	java/io/IOException
    //   7	15	57	java/io/IOException
    //   136	143	146	java/lang/InterruptedException
    //   155	175	178	finally
    //   183	193	178	finally
    //   197	205	178	finally
    //   205	207	178	finally
    //   210	212	178	finally
    //   155	175	182	java/lang/RuntimeException
    //   155	175	196	com/android/internal/telephony/test/InterpreterEx
    //   127	132	214	finally
    //   136	143	214	finally
    //   151	153	214	finally
    //   216	218	214	finally
  }
  
  public void sendUnsolicited(String paramString)
  {
    try
    {
      println(paramString);
      return;
    }
    finally {}
  }
  
  public void setAutoProgressConnectingCall(boolean paramBoolean)
  {
    mSimulatedCallState.setAutoProgressConnectingCall(paramBoolean);
  }
  
  public void setNextCallFailCause(int paramInt) {}
  
  public void setNextDialFailImmediately(boolean paramBoolean)
  {
    mSimulatedCallState.setNextDialFailImmediately(paramBoolean);
  }
  
  public void shutdown()
  {
    Looper localLooper = mHandlerThread.getLooper();
    if (localLooper != null) {
      localLooper.quit();
    }
    try
    {
      mIn.close();
    }
    catch (IOException localIOException1) {}
    try
    {
      mOut.close();
    }
    catch (IOException localIOException2) {}
  }
  
  String[] splitCommands(String paramString)
    throws InterpreterEx
  {
    if (paramString.startsWith("AT"))
    {
      if (paramString.length() == 2) {
        return new String[0];
      }
      return new String[] { paramString.substring(2) };
    }
    throw new InterpreterEx("ERROR");
  }
  
  public void triggerHangupAll()
  {
    if (mSimulatedCallState.triggerHangupAll()) {
      println("NO CARRIER");
    }
  }
  
  public void triggerHangupBackground()
  {
    if (mSimulatedCallState.triggerHangupBackground()) {
      println("NO CARRIER");
    }
  }
  
  public void triggerHangupForeground()
  {
    if (mSimulatedCallState.triggerHangupForeground()) {
      println("NO CARRIER");
    }
  }
  
  public void triggerIncomingSMS(String paramString) {}
  
  public void triggerIncomingUssd(String paramString1, String paramString2) {}
  
  public void triggerRing(String paramString)
  {
    try
    {
      if (mSimulatedCallState.triggerRing(paramString)) {
        println("RING");
      }
      return;
    }
    finally {}
  }
  
  public void triggerSsn(int paramInt1, int paramInt2) {}
}
