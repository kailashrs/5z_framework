package com.android.internal.telephony.imsphone;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.Connection.RttTextStream;
import android.telephony.Rlog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ImsRttTextHandler
  extends Handler
{
  private static final int APPEND_TO_NETWORK_BUFFER = 2;
  private static final int ATTEMPT_SEND_TO_NETWORK = 4;
  private static final int EXPIRE_SENT_CODEPOINT_COUNT = 5;
  private static final int INITIALIZE = 1;
  private static final String LOG_TAG = "ImsRttTextHandler";
  public static final int MAX_BUFFERED_CHARACTER_COUNT = 5;
  public static final int MAX_BUFFERING_DELAY_MILLIS = 200;
  public static final int MAX_CODEPOINTS_PER_SECOND = 30;
  private static final int MILLIS_PER_SECOND = 1000;
  private static final int SEND_TO_INCALL = 3;
  private static final int TEARDOWN = 9999;
  private StringBuffer mBufferedTextToIncall = new StringBuffer();
  private StringBuffer mBufferedTextToNetwork = new StringBuffer();
  private int mCodepointsAvailableForTransmission = 30;
  private final NetworkWriter mNetworkWriter;
  private CountDownLatch mReadNotifier;
  private InCallReaderThread mReaderThread;
  private Connection.RttTextStream mRttTextStream;
  
  public ImsRttTextHandler(Looper paramLooper, NetworkWriter paramNetworkWriter)
  {
    super(paramLooper);
    mNetworkWriter = paramNetworkWriter;
  }
  
  public String getNetworkBufferText()
  {
    return mBufferedTextToNetwork.toString();
  }
  
  public void handleMessage(Message paramMessage)
  {
    int i = what;
    if (i != 9999)
    {
      switch (i)
      {
      default: 
        break;
      case 5: 
        mCodepointsAvailableForTransmission += arg1;
        if (mCodepointsAvailableForTransmission <= 0) {
          break;
        }
        sendMessageAtFrontOfQueue(obtainMessage(4));
        break;
      case 4: 
        i = Math.min(mBufferedTextToNetwork.codePointCount(0, mBufferedTextToNetwork.length()), mCodepointsAvailableForTransmission);
        if (i == 0) {
          break;
        }
        int j = mBufferedTextToNetwork.offsetByCodePoints(0, i);
        paramMessage = mBufferedTextToNetwork.substring(0, j);
        mBufferedTextToNetwork.delete(0, j);
        mNetworkWriter.write(paramMessage);
        mCodepointsAvailableForTransmission -= i;
        sendMessageDelayed(obtainMessage(5, i, 0), 1000L);
        break;
      case 3: 
        paramMessage = (String)obj;
        if (mRttTextStream == null)
        {
          Rlog.e("ImsRttTextHandler", "RTT text stream is null");
          return;
        }
        try
        {
          mRttTextStream.write(paramMessage);
        }
        catch (IOException localIOException)
        {
          Rlog.e("ImsRttTextHandler", "IOException encountered writing to in-call: %s", localIOException);
          obtainMessage(9999).sendToTarget();
          mBufferedTextToIncall.append(paramMessage);
        }
      case 2: 
        mBufferedTextToNetwork.append((String)obj);
        if (mBufferedTextToNetwork.codePointCount(0, mBufferedTextToNetwork.length()) >= 5) {
          sendMessageAtFrontOfQueue(obtainMessage(4));
        } else {
          sendEmptyMessageDelayed(4, 200L);
        }
        break;
      case 1: 
        if ((mRttTextStream == null) && (mReaderThread == null))
        {
          mRttTextStream = ((Connection.RttTextStream)obj);
          if (mRttTextStream == null)
          {
            Rlog.e("ImsRttTextHandler", "RTT text stream is null");
            return;
          }
          mReaderThread = new InCallReaderThread(mRttTextStream);
          mReaderThread.start();
          break;
        }
        Rlog.e("ImsRttTextHandler", "RTT text stream already initialized. Ignoring.");
        return;
      }
    }
    else
    {
      try
      {
        if (mReaderThread != null) {
          mReaderThread.join(1000L);
        }
      }
      catch (InterruptedException paramMessage) {}
      mReaderThread = null;
      mRttTextStream = null;
    }
  }
  
  public void initialize(Connection.RttTextStream paramRttTextStream)
  {
    obtainMessage(1, paramRttTextStream).sendToTarget();
  }
  
  public void sendToInCall(String paramString)
  {
    obtainMessage(3, paramString).sendToTarget();
  }
  
  @VisibleForTesting
  public void setReadNotifier(CountDownLatch paramCountDownLatch)
  {
    mReadNotifier = paramCountDownLatch;
  }
  
  public void tearDown()
  {
    obtainMessage(9999).sendToTarget();
  }
  
  private class InCallReaderThread
    extends Thread
  {
    private final Connection.RttTextStream mReaderThreadRttTextStream;
    
    public InCallReaderThread(Connection.RttTextStream paramRttTextStream)
    {
      mReaderThreadRttTextStream = paramRttTextStream;
    }
    
    public void run()
    {
      try
      {
        for (;;)
        {
          String str = mReaderThreadRttTextStream.read();
          if (str == null)
          {
            if (Thread.currentThread().isInterrupted())
            {
              Rlog.i("ImsRttTextHandler", "RttReaderThread - Thread interrupted. Finishing.");
              break;
            }
            Rlog.e("ImsRttTextHandler", "RttReaderThread - Stream closed unexpectedly. Attempt to reinitialize.");
            obtainMessage(9999).sendToTarget();
            break;
          }
          if (str.length() != 0)
          {
            obtainMessage(2, str).sendToTarget();
            if (mReadNotifier != null) {
              mReadNotifier.countDown();
            }
          }
        }
        return;
      }
      catch (IOException localIOException)
      {
        Rlog.e("ImsRttTextHandler", "RttReaderThread - IOException encountered reading from in-call: %s", localIOException);
        obtainMessage(9999).sendToTarget();
      }
    }
  }
  
  public static abstract interface NetworkWriter
  {
    public abstract void write(String paramString);
  }
}
