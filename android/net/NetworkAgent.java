package android.net;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import com.android.internal.util.AsyncChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class NetworkAgent
  extends Handler
{
  private static final int BASE = 528384;
  private static final long BW_REFRESH_MIN_WIN_MS = 500L;
  public static final int CMD_PREVENT_AUTOMATIC_RECONNECT = 528399;
  public static final int CMD_REPORT_NETWORK_STATUS = 528391;
  public static final int CMD_REQUEST_BANDWIDTH_UPDATE = 528394;
  public static final int CMD_SAVE_ACCEPT_UNVALIDATED = 528393;
  public static final int CMD_SET_SIGNAL_STRENGTH_THRESHOLDS = 528398;
  public static final int CMD_START_PACKET_KEEPALIVE = 528395;
  public static final int CMD_STOP_PACKET_KEEPALIVE = 528396;
  public static final int CMD_SUSPECT_BAD = 528384;
  private static final boolean DBG = true;
  public static final int EVENT_NETWORK_CAPABILITIES_CHANGED = 528386;
  public static final int EVENT_NETWORK_INFO_CHANGED = 528385;
  public static final int EVENT_NETWORK_PROPERTIES_CHANGED = 528387;
  public static final int EVENT_NETWORK_SCORE_CHANGED = 528388;
  public static final int EVENT_PACKET_KEEPALIVE = 528397;
  public static final int EVENT_SET_EXPLICITLY_SELECTED = 528392;
  public static final int INVALID_NETWORK = 2;
  public static String REDIRECT_URL_KEY = "redirect URL";
  public static final int VALID_NETWORK = 1;
  private static final boolean VDBG = false;
  public static final int WIFI_BASE_SCORE = 60;
  private final String LOG_TAG;
  private volatile AsyncChannel mAsyncChannel;
  private final Context mContext;
  private volatile long mLastBwRefreshTime = 0L;
  private AtomicBoolean mPollLcePending = new AtomicBoolean(false);
  private boolean mPollLceScheduled = false;
  private final ArrayList<Message> mPreConnectedQueue = new ArrayList();
  public final int netId;
  
  public NetworkAgent(Looper paramLooper, Context paramContext, String paramString, NetworkInfo paramNetworkInfo, NetworkCapabilities paramNetworkCapabilities, LinkProperties paramLinkProperties, int paramInt)
  {
    this(paramLooper, paramContext, paramString, paramNetworkInfo, paramNetworkCapabilities, paramLinkProperties, paramInt, null);
  }
  
  public NetworkAgent(Looper paramLooper, Context paramContext, String paramString, NetworkInfo paramNetworkInfo, NetworkCapabilities paramNetworkCapabilities, LinkProperties paramLinkProperties, int paramInt, NetworkMisc paramNetworkMisc)
  {
    super(paramLooper);
    LOG_TAG = paramString;
    mContext = paramContext;
    if ((paramNetworkInfo != null) && (paramNetworkCapabilities != null) && (paramLinkProperties != null))
    {
      netId = ((ConnectivityManager)mContext.getSystemService("connectivity")).registerNetworkAgent(new Messenger(this), new NetworkInfo(paramNetworkInfo), new LinkProperties(paramLinkProperties), new NetworkCapabilities(paramNetworkCapabilities), paramInt, paramNetworkMisc);
      return;
    }
    throw new IllegalArgumentException();
  }
  
  private void queueOrSendMessage(int paramInt1, int paramInt2, int paramInt3)
  {
    queueOrSendMessage(paramInt1, paramInt2, paramInt3, null);
  }
  
  private void queueOrSendMessage(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = paramInt3;
    obj = paramObject;
    queueOrSendMessage(localMessage);
  }
  
  private void queueOrSendMessage(int paramInt, Object paramObject)
  {
    queueOrSendMessage(paramInt, 0, 0, paramObject);
  }
  
  private void queueOrSendMessage(Message paramMessage)
  {
    synchronized (mPreConnectedQueue)
    {
      if (mAsyncChannel != null) {
        mAsyncChannel.sendMessage(paramMessage);
      } else {
        mPreConnectedQueue.add(paramMessage);
      }
      return;
    }
  }
  
  public void explicitlySelected(boolean paramBoolean)
  {
    queueOrSendMessage(528392, Boolean.valueOf(paramBoolean));
  }
  
  public void handleMessage(Message arg1)
  {
    int i = what;
    boolean bool = true;
    int j = 0;
    switch (i)
    {
    default: 
      break;
    case 528399: 
      preventAutomaticReconnect();
      break;
    case 528398: 
      ??? = ((Bundle)obj).getIntegerArrayList("thresholds");
      if (??? != null) {
        i = ((ArrayList)???).size();
      } else {
        i = 0;
      }
      ??? = new int[i];
      for (i = j; i < ???.length; i++) {
        ???[i] = ((Integer)((ArrayList)???).get(i)).intValue();
      }
      setSignalStrengthThresholds(???);
      break;
    case 528396: 
      stopPacketKeepalive(???);
      break;
    case 528395: 
      startPacketKeepalive(???);
      break;
    case 528394: 
      long l = System.currentTimeMillis();
      if (l >= mLastBwRefreshTime + 500L)
      {
        mPollLceScheduled = false;
        if (!mPollLcePending.getAndSet(true)) {
          pollLceData();
        }
      }
      else if (!mPollLceScheduled)
      {
        mPollLceScheduled = sendEmptyMessageDelayed(528394, mLastBwRefreshTime + 500L - l + 1L);
      }
      break;
    case 528393: 
      if (arg1 == 0) {
        bool = false;
      }
      saveAcceptUnvalidated(bool);
      break;
    case 528391: 
      ??? = ((Bundle)obj).getString(REDIRECT_URL_KEY);
      networkStatus(arg1, (String)???);
      break;
    case 528384: 
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Unhandled Message ");
      ((StringBuilder)???).append(???);
      log(((StringBuilder)???).toString());
      break;
    case 69636: 
      log("NetworkAgent channel lost");
      unwanted();
      synchronized (mPreConnectedQueue)
      {
        mAsyncChannel = null;
      }
    case 69635: 
      if (mAsyncChannel != null) {
        mAsyncChannel.disconnect();
      }
      break;
    case 69633: 
      if (mAsyncChannel != null)
      {
        log("Received new connection while already connected!");
      }
      else
      {
        ??? = new AsyncChannel();
        ((AsyncChannel)???).connected(null, this, replyTo);
        ((AsyncChannel)???).replyToMessage(???, 69634, 0);
        synchronized (mPreConnectedQueue)
        {
          mAsyncChannel = ((AsyncChannel)???);
          Iterator localIterator = mPreConnectedQueue.iterator();
          while (localIterator.hasNext()) {
            ((AsyncChannel)???).sendMessage((Message)localIterator.next());
          }
          mPreConnectedQueue.clear();
        }
      }
      break;
    }
  }
  
  protected void log(String paramString)
  {
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NetworkAgent: ");
    localStringBuilder.append(paramString);
    Log.d(str, localStringBuilder.toString());
  }
  
  protected void networkStatus(int paramInt, String paramString) {}
  
  public void onPacketKeepaliveEvent(int paramInt1, int paramInt2)
  {
    queueOrSendMessage(528397, paramInt1, paramInt2);
  }
  
  protected void pollLceData() {}
  
  protected void preventAutomaticReconnect() {}
  
  protected void saveAcceptUnvalidated(boolean paramBoolean) {}
  
  public void sendLinkProperties(LinkProperties paramLinkProperties)
  {
    queueOrSendMessage(528387, new LinkProperties(paramLinkProperties));
  }
  
  public void sendNetworkCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    mPollLcePending.set(false);
    mLastBwRefreshTime = System.currentTimeMillis();
    queueOrSendMessage(528386, new NetworkCapabilities(paramNetworkCapabilities));
  }
  
  public void sendNetworkInfo(NetworkInfo paramNetworkInfo)
  {
    queueOrSendMessage(528385, new NetworkInfo(paramNetworkInfo));
  }
  
  public void sendNetworkScore(int paramInt)
  {
    if (paramInt >= 0)
    {
      queueOrSendMessage(528388, paramInt, 0);
      return;
    }
    throw new IllegalArgumentException("Score must be >= 0");
  }
  
  protected void setSignalStrengthThresholds(int[] paramArrayOfInt) {}
  
  protected void startPacketKeepalive(Message paramMessage)
  {
    onPacketKeepaliveEvent(arg1, -30);
  }
  
  protected void stopPacketKeepalive(Message paramMessage)
  {
    onPacketKeepaliveEvent(arg1, -30);
  }
  
  protected abstract void unwanted();
}
