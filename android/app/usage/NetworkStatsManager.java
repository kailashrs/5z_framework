package android.app.usage;

import android.content.Context;
import android.net.DataUsageRequest;
import android.net.INetworkStatsService;
import android.net.INetworkStatsService.Stub;
import android.net.NetworkIdentity;
import android.net.NetworkTemplate;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.util.DataUnit;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;

public class NetworkStatsManager
{
  public static final int CALLBACK_LIMIT_REACHED = 0;
  public static final int CALLBACK_RELEASED = 1;
  private static final boolean DBG = false;
  public static final int FLAG_AUGMENT_WITH_SUBSCRIPTION_PLAN = 4;
  public static final int FLAG_POLL_FORCE = 2;
  public static final int FLAG_POLL_ON_OPEN = 1;
  public static final long MIN_THRESHOLD_BYTES = DataUnit.MEBIBYTES.toBytes(2L);
  private static final String TAG = "NetworkStatsManager";
  private final Context mContext;
  private int mFlags;
  private final INetworkStatsService mService;
  
  public NetworkStatsManager(Context paramContext)
    throws ServiceManager.ServiceNotFoundException
  {
    this(paramContext, INetworkStatsService.Stub.asInterface(ServiceManager.getServiceOrThrow("netstats")));
  }
  
  @VisibleForTesting
  public NetworkStatsManager(Context paramContext, INetworkStatsService paramINetworkStatsService)
  {
    mContext = paramContext;
    mService = paramINetworkStatsService;
    setPollOnOpen(true);
  }
  
  private static NetworkTemplate createTemplate(int paramInt, String paramString)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Cannot create template for network type ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(", subscriberId '");
      localStringBuilder.append(NetworkIdentity.scrubSubscriberId(paramString));
      localStringBuilder.append("'.");
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 1: 
      paramString = NetworkTemplate.buildTemplateWifiWildcard();
      break;
    case 0: 
      if (paramString == null) {
        paramString = NetworkTemplate.buildTemplateMobileWildcard();
      } else {
        paramString = NetworkTemplate.buildTemplateMobileAll(paramString);
      }
      break;
    }
    return paramString;
  }
  
  public NetworkStats queryDetails(int paramInt, String paramString, long paramLong1, long paramLong2)
    throws SecurityException, RemoteException
  {
    try
    {
      paramString = createTemplate(paramInt, paramString);
      paramString = new NetworkStats(mContext, paramString, mFlags, paramLong1, paramLong2, mService);
      paramString.startUserUidEnumeration();
      return paramString;
    }
    catch (IllegalArgumentException paramString) {}
    return null;
  }
  
  public NetworkStats queryDetailsForUid(int paramInt1, String paramString, long paramLong1, long paramLong2, int paramInt2)
    throws SecurityException
  {
    return queryDetailsForUidTagState(paramInt1, paramString, paramLong1, paramLong2, paramInt2, 0, -1);
  }
  
  public NetworkStats queryDetailsForUidTag(int paramInt1, String paramString, long paramLong1, long paramLong2, int paramInt2, int paramInt3)
    throws SecurityException
  {
    return queryDetailsForUidTagState(paramInt1, paramString, paramLong1, paramLong2, paramInt2, paramInt3, -1);
  }
  
  public NetworkStats queryDetailsForUidTagState(int paramInt1, String paramString, long paramLong1, long paramLong2, int paramInt2, int paramInt3, int paramInt4)
    throws SecurityException
  {
    paramString = createTemplate(paramInt1, paramString);
    try
    {
      NetworkStats localNetworkStats = new android/app/usage/NetworkStats;
      localNetworkStats.<init>(mContext, paramString, mFlags, paramLong1, paramLong2, mService);
      localNetworkStats.startHistoryEnumeration(paramInt2, paramInt3, paramInt4);
      return localNetworkStats;
    }
    catch (RemoteException localRemoteException)
    {
      paramString = new StringBuilder();
      paramString.append("Error while querying stats for uid=");
      paramString.append(paramInt2);
      paramString.append(" tag=");
      paramString.append(paramInt3);
      paramString.append(" state=");
      paramString.append(paramInt4);
      Log.e("NetworkStatsManager", paramString.toString(), localRemoteException);
    }
    return null;
  }
  
  public NetworkStats querySummary(int paramInt, String paramString, long paramLong1, long paramLong2)
    throws SecurityException, RemoteException
  {
    try
    {
      paramString = createTemplate(paramInt, paramString);
      paramString = new NetworkStats(mContext, paramString, mFlags, paramLong1, paramLong2, mService);
      paramString.startSummaryEnumeration();
      return paramString;
    }
    catch (IllegalArgumentException paramString) {}
    return null;
  }
  
  public NetworkStats.Bucket querySummaryForDevice(int paramInt, String paramString, long paramLong1, long paramLong2)
    throws SecurityException, RemoteException
  {
    try
    {
      paramString = createTemplate(paramInt, paramString);
      return querySummaryForDevice(paramString, paramLong1, paramLong2);
    }
    catch (IllegalArgumentException paramString) {}
    return null;
  }
  
  public NetworkStats.Bucket querySummaryForDevice(NetworkTemplate paramNetworkTemplate, long paramLong1, long paramLong2)
    throws SecurityException, RemoteException
  {
    NetworkStats localNetworkStats = new NetworkStats(mContext, paramNetworkTemplate, mFlags, paramLong1, paramLong2, mService);
    paramNetworkTemplate = localNetworkStats.getDeviceSummaryForNetwork();
    localNetworkStats.close();
    return paramNetworkTemplate;
  }
  
  public NetworkStats.Bucket querySummaryForUser(int paramInt, String paramString, long paramLong1, long paramLong2)
    throws SecurityException, RemoteException
  {
    try
    {
      paramString = createTemplate(paramInt, paramString);
      paramString = new NetworkStats(mContext, paramString, mFlags, paramLong1, paramLong2, mService);
      paramString.startSummaryEnumeration();
      paramString.close();
      return paramString.getSummaryAggregate();
    }
    catch (IllegalArgumentException paramString) {}
    return null;
  }
  
  public void registerUsageCallback(int paramInt, String paramString, long paramLong, UsageCallback paramUsageCallback)
  {
    registerUsageCallback(paramInt, paramString, paramLong, paramUsageCallback, null);
  }
  
  public void registerUsageCallback(int paramInt, String paramString, long paramLong, UsageCallback paramUsageCallback, Handler paramHandler)
  {
    registerUsageCallback(createTemplate(paramInt, paramString), paramInt, paramLong, paramUsageCallback, paramHandler);
  }
  
  public void registerUsageCallback(NetworkTemplate paramNetworkTemplate, int paramInt, long paramLong, UsageCallback paramUsageCallback, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramUsageCallback, "UsageCallback cannot be null");
    if (paramHandler == null) {
      paramHandler = Looper.myLooper();
    } else {
      paramHandler = paramHandler.getLooper();
    }
    DataUsageRequest localDataUsageRequest = new DataUsageRequest(0, paramNetworkTemplate, paramLong);
    try
    {
      Object localObject = new android/app/usage/NetworkStatsManager$CallbackHandler;
      ((CallbackHandler)localObject).<init>(paramHandler, paramInt, paramNetworkTemplate.getSubscriberId(), paramUsageCallback);
      paramHandler = mService;
      paramNetworkTemplate = mContext.getOpPackageName();
      Messenger localMessenger = new android/os/Messenger;
      localMessenger.<init>((Handler)localObject);
      localObject = new android/os/Binder;
      ((Binder)localObject).<init>();
      UsageCallback.access$002(paramUsageCallback, paramHandler.registerUsageCallback(paramNetworkTemplate, localDataUsageRequest, localMessenger, (IBinder)localObject));
      if (request == null) {
        Log.e("NetworkStatsManager", "Request from callback is null; should not happen");
      }
      return;
    }
    catch (RemoteException paramNetworkTemplate)
    {
      throw paramNetworkTemplate.rethrowFromSystemServer();
    }
  }
  
  public void setAugmentWithSubscriptionPlan(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFlags |= 0x4;
    } else {
      mFlags &= 0xFFFFFFFB;
    }
  }
  
  public void setPollForce(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFlags |= 0x2;
    } else {
      mFlags &= 0xFFFFFFFD;
    }
  }
  
  public void setPollOnOpen(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFlags |= 0x1;
    } else {
      mFlags &= 0xFFFFFFFE;
    }
  }
  
  public void unregisterUsageCallback(UsageCallback paramUsageCallback)
  {
    if ((paramUsageCallback != null) && (request != null) && (request.requestId != 0)) {
      try
      {
        mService.unregisterUsageRequest(request);
        return;
      }
      catch (RemoteException paramUsageCallback)
      {
        throw paramUsageCallback.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("Invalid UsageCallback");
  }
  
  private static class CallbackHandler
    extends Handler
  {
    private NetworkStatsManager.UsageCallback mCallback;
    private final int mNetworkType;
    private final String mSubscriberId;
    
    CallbackHandler(Looper paramLooper, int paramInt, String paramString, NetworkStatsManager.UsageCallback paramUsageCallback)
    {
      super();
      mNetworkType = paramInt;
      mSubscriberId = paramString;
      mCallback = paramUsageCallback;
    }
    
    private static Object getObject(Message paramMessage, String paramString)
    {
      return paramMessage.getData().getParcelable(paramString);
    }
    
    public void handleMessage(Message paramMessage)
    {
      DataUsageRequest localDataUsageRequest = (DataUsageRequest)getObject(paramMessage, "DataUsageRequest");
      switch (what)
      {
      default: 
        break;
      case 1: 
        mCallback = null;
        break;
      case 0: 
        if (mCallback != null)
        {
          mCallback.onThresholdReached(mNetworkType, mSubscriberId);
        }
        else
        {
          paramMessage = new StringBuilder();
          paramMessage.append("limit reached with released callback for ");
          paramMessage.append(localDataUsageRequest);
          Log.e("NetworkStatsManager", paramMessage.toString());
        }
        break;
      }
    }
  }
  
  public static abstract class UsageCallback
  {
    private DataUsageRequest request;
    
    public UsageCallback() {}
    
    public abstract void onThresholdReached(int paramInt, String paramString);
  }
}
