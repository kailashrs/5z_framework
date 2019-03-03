package android.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class NetworkFactory
  extends Handler
{
  private static final int BASE = 536576;
  public static final int CMD_CANCEL_REQUEST = 536577;
  public static final int CMD_REQUEST_NETWORK = 536576;
  private static final int CMD_SET_FILTER = 536579;
  private static final int CMD_SET_SCORE = 536578;
  private static final boolean DBG = true;
  private static final boolean VDBG = false;
  private final String LOG_TAG;
  private NetworkCapabilities mCapabilityFilter;
  private final Context mContext;
  private Messenger mMessenger = null;
  private final SparseArray<NetworkRequestInfo> mNetworkRequests = new SparseArray();
  private int mRefCount = 0;
  private int mScore;
  
  public NetworkFactory(Looper paramLooper, Context paramContext, String paramString, NetworkCapabilities paramNetworkCapabilities)
  {
    super(paramLooper);
    LOG_TAG = paramString;
    mContext = paramContext;
    mCapabilityFilter = paramNetworkCapabilities;
  }
  
  private void evalRequest(NetworkRequestInfo paramNetworkRequestInfo)
  {
    if ((!requested) && (score < mScore) && (request.networkCapabilities.satisfiedByNetworkCapabilities(mCapabilityFilter)) && (acceptRequest(request, score)))
    {
      needNetworkFor(request, score);
      requested = true;
    }
    else if ((requested == true) && ((score > mScore) || (!request.networkCapabilities.satisfiedByNetworkCapabilities(mCapabilityFilter)) || (!acceptRequest(request, score))))
    {
      releaseNetworkFor(request);
      requested = false;
    }
  }
  
  private void evalRequests()
  {
    for (int i = 0; i < mNetworkRequests.size(); i++) {
      evalRequest((NetworkRequestInfo)mNetworkRequests.valueAt(i));
    }
  }
  
  private void handleSetFilter(NetworkCapabilities paramNetworkCapabilities)
  {
    mCapabilityFilter = paramNetworkCapabilities;
    evalRequests();
  }
  
  private void handleSetScore(int paramInt)
  {
    mScore = paramInt;
    evalRequests();
  }
  
  public boolean acceptRequest(NetworkRequest paramNetworkRequest, int paramInt)
  {
    return true;
  }
  
  public void addNetworkRequest(NetworkRequest paramNetworkRequest, int paramInt)
  {
    sendMessage(obtainMessage(536576, new NetworkRequestInfo(paramNetworkRequest, paramInt)));
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramFileDescriptor.println(toString());
    paramFileDescriptor.increaseIndent();
    for (int i = 0; i < mNetworkRequests.size(); i++) {
      paramFileDescriptor.println(mNetworkRequests.valueAt(i));
    }
    paramFileDescriptor.decreaseIndent();
  }
  
  @VisibleForTesting
  protected int getRequestCount()
  {
    return mNetworkRequests.size();
  }
  
  @VisibleForTesting
  protected void handleAddRequest(NetworkRequest paramNetworkRequest, int paramInt)
  {
    Object localObject = (NetworkRequestInfo)mNetworkRequests.get(requestId);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("got request ");
      ((StringBuilder)localObject).append(paramNetworkRequest);
      ((StringBuilder)localObject).append(" with score ");
      ((StringBuilder)localObject).append(paramInt);
      log(((StringBuilder)localObject).toString());
      paramNetworkRequest = new NetworkRequestInfo(paramNetworkRequest, paramInt);
      mNetworkRequests.put(request.requestId, paramNetworkRequest);
    }
    else
    {
      score = paramInt;
      paramNetworkRequest = (NetworkRequest)localObject;
    }
    evalRequest(paramNetworkRequest);
  }
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      break;
    case 536579: 
      handleSetFilter((NetworkCapabilities)obj);
      break;
    case 536578: 
      handleSetScore(arg1);
      break;
    case 536577: 
      handleRemoveRequest((NetworkRequest)obj);
      break;
    case 536576: 
      handleAddRequest((NetworkRequest)obj, arg1);
    }
  }
  
  @VisibleForTesting
  protected void handleRemoveRequest(NetworkRequest paramNetworkRequest)
  {
    NetworkRequestInfo localNetworkRequestInfo = (NetworkRequestInfo)mNetworkRequests.get(requestId);
    if (localNetworkRequestInfo != null)
    {
      mNetworkRequests.remove(requestId);
      if (requested) {
        releaseNetworkFor(request);
      }
    }
  }
  
  protected void log(String paramString)
  {
    Log.d(LOG_TAG, paramString);
  }
  
  protected void needNetworkFor(NetworkRequest paramNetworkRequest, int paramInt)
  {
    paramInt = mRefCount + 1;
    mRefCount = paramInt;
    if (paramInt == 1) {
      startNetwork();
    }
  }
  
  protected void reevaluateAllRequests()
  {
    post(new _..Lambda.NetworkFactory.HfslgqyaKc_n0wXX5_qRYVZoGfI(this));
  }
  
  public void register()
  {
    log("Registering NetworkFactory");
    if (mMessenger == null)
    {
      mMessenger = new Messenger(this);
      ConnectivityManager.from(mContext).registerNetworkFactory(mMessenger, LOG_TAG);
    }
  }
  
  protected void releaseNetworkFor(NetworkRequest paramNetworkRequest)
  {
    int i = mRefCount - 1;
    mRefCount = i;
    if (i == 0) {
      stopNetwork();
    }
  }
  
  public void removeNetworkRequest(NetworkRequest paramNetworkRequest)
  {
    sendMessage(obtainMessage(536577, paramNetworkRequest));
  }
  
  public void setCapabilityFilter(NetworkCapabilities paramNetworkCapabilities)
  {
    sendMessage(obtainMessage(536579, new NetworkCapabilities(paramNetworkCapabilities)));
  }
  
  public void setScoreFilter(int paramInt)
  {
    sendMessage(obtainMessage(536578, paramInt, 0));
  }
  
  protected void startNetwork() {}
  
  protected void stopNetwork() {}
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("{");
    localStringBuilder.append(LOG_TAG);
    localStringBuilder.append(" - ScoreFilter=");
    localStringBuilder.append(mScore);
    localStringBuilder.append(", Filter=");
    localStringBuilder.append(mCapabilityFilter);
    localStringBuilder.append(", requests=");
    localStringBuilder.append(mNetworkRequests.size());
    localStringBuilder.append(", refCount=");
    localStringBuilder.append(mRefCount);
    return "}";
  }
  
  public void unregister()
  {
    log("Unregistering NetworkFactory");
    if (mMessenger != null)
    {
      ConnectivityManager.from(mContext).unregisterNetworkFactory(mMessenger);
      mMessenger = null;
    }
  }
  
  private class NetworkRequestInfo
  {
    public final NetworkRequest request;
    public boolean requested;
    public int score;
    
    public NetworkRequestInfo(NetworkRequest paramNetworkRequest, int paramInt)
    {
      request = paramNetworkRequest;
      score = paramInt;
      requested = false;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{");
      localStringBuilder.append(request);
      localStringBuilder.append(", score=");
      localStringBuilder.append(score);
      localStringBuilder.append(", requested=");
      localStringBuilder.append(requested);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
