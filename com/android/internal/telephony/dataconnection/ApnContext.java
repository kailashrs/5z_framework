package com.android.internal.telephony.dataconnection;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkCapabilities;
import android.net.NetworkConfig;
import android.net.NetworkRequest;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.LocalLog;
import android.util.SparseIntArray;
import com.android.internal.telephony.DctConstants.State;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RetryManager;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ApnContext
{
  protected static final boolean DBG = false;
  private static final String SLOG_TAG = "ApnContext";
  public final String LOG_TAG;
  private ApnSetting mApnSetting;
  private final String mApnType;
  private boolean mConcurrentVoiceAndDataAllowed;
  private final AtomicInteger mConnectionGeneration = new AtomicInteger(0);
  AtomicBoolean mDataEnabled;
  DcAsyncChannel mDcAc;
  private final DcTracker mDcTracker;
  AtomicBoolean mDependencyMet;
  private boolean mEnableUnderPolicy = true;
  private final ArrayList<LocalLog> mLocalLogs = new ArrayList();
  private final ArrayList<NetworkRequest> mNetworkRequests = new ArrayList();
  private final Phone mPhone;
  String mReason;
  PendingIntent mReconnectAlarmIntent;
  private int mRefCount = 0;
  private final Object mRefCountLock = new Object();
  private final SparseIntArray mRetriesLeftPerErrorCode = new SparseIntArray();
  private final RetryManager mRetryManager;
  private DctConstants.State mState;
  private final LocalLog mStateLocalLog = new LocalLog(50);
  public final int priority;
  
  public ApnContext(Phone paramPhone, String paramString1, String paramString2, NetworkConfig paramNetworkConfig, DcTracker paramDcTracker)
  {
    mPhone = paramPhone;
    mApnType = paramString1;
    mState = DctConstants.State.IDLE;
    setReason("dataEnabled");
    mDataEnabled = new AtomicBoolean(false);
    mDependencyMet = new AtomicBoolean(dependencyMet);
    priority = priority;
    LOG_TAG = paramString2;
    mDcTracker = paramDcTracker;
    mRetryManager = new RetryManager(paramPhone, paramString1);
  }
  
  public static int apnIdForApnName(String paramString)
  {
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 1629013393: 
      if (paramString.equals("emergency")) {
        i = 9;
      }
      break;
    case 1544803905: 
      if (paramString.equals("default")) {
        i = 0;
      }
      break;
    case 99285510: 
      if (paramString.equals("hipri")) {
        i = 4;
      }
      break;
    case 3541982: 
      if (paramString.equals("supl")) {
        i = 2;
      }
      break;
    case 3149046: 
      if (paramString.equals("fota")) {
        i = 6;
      }
      break;
    case 108243: 
      if (paramString.equals("mms")) {
        i = 1;
      }
      break;
    case 104399: 
      if (paramString.equals("ims")) {
        i = 5;
      }
      break;
    case 99837: 
      if (paramString.equals("dun")) {
        i = 3;
      }
      break;
    case 98292: 
      if (paramString.equals("cbs")) {
        i = 7;
      }
      break;
    case 3352: 
      if (paramString.equals("ia")) {
        i = 8;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      return -1;
    case 9: 
      return 9;
    case 8: 
      return 8;
    case 7: 
      return 7;
    case 6: 
      return 6;
    case 5: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  public static int apnIdForNetworkRequest(NetworkRequest paramNetworkRequest)
  {
    Object localObject = networkCapabilities;
    if ((((NetworkCapabilities)localObject).getTransportTypes().length > 0) && (!((NetworkCapabilities)localObject).hasTransport(0))) {
      return -1;
    }
    int i = -1;
    int j = 0;
    int k = 0;
    if (((NetworkCapabilities)localObject).hasCapability(12)) {
      i = 0;
    }
    int m = i;
    if (((NetworkCapabilities)localObject).hasCapability(0))
    {
      j = k;
      if (i != -1) {
        j = 1;
      }
      m = 1;
    }
    k = m;
    i = j;
    if (((NetworkCapabilities)localObject).hasCapability(1))
    {
      if (m != -1) {
        j = 1;
      }
      k = 2;
      i = j;
    }
    int n = k;
    j = i;
    if (((NetworkCapabilities)localObject).hasCapability(2))
    {
      j = i;
      if (k != -1) {
        j = 1;
      }
      n = 3;
    }
    k = n;
    m = j;
    if (((NetworkCapabilities)localObject).hasCapability(3))
    {
      if (n != -1) {
        j = 1;
      }
      k = 6;
      m = j;
    }
    n = k;
    i = m;
    if (((NetworkCapabilities)localObject).hasCapability(4))
    {
      i = m;
      if (k != -1) {
        i = 1;
      }
      n = 5;
    }
    m = n;
    j = i;
    if (((NetworkCapabilities)localObject).hasCapability(5))
    {
      j = i;
      if (n != -1) {
        j = 1;
      }
      m = 7;
    }
    k = m;
    i = j;
    if (((NetworkCapabilities)localObject).hasCapability(7))
    {
      if (m != -1) {
        j = 1;
      }
      k = 8;
      i = j;
    }
    m = i;
    if (((NetworkCapabilities)localObject).hasCapability(8))
    {
      if (k != -1) {
        i = 1;
      }
      Rlog.d("ApnContext", "RCS APN type not yet supported");
      m = i;
    }
    j = m;
    if (((NetworkCapabilities)localObject).hasCapability(9))
    {
      j = m;
      if (k != -1) {
        j = 1;
      }
      Rlog.d("ApnContext", "XCAP APN type not yet supported");
    }
    m = k;
    i = j;
    if (((NetworkCapabilities)localObject).hasCapability(10))
    {
      if (k != -1) {
        j = 1;
      }
      m = 9;
      i = j;
    }
    if (i != 0) {
      Rlog.d("ApnContext", "Multiple apn types specified in request - result is unspecified!");
    }
    if (m == -1)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unsupported NetworkRequest in Telephony: nr=");
      ((StringBuilder)localObject).append(paramNetworkRequest);
      Rlog.d("ApnContext", ((StringBuilder)localObject).toString());
    }
    return m;
  }
  
  public static int apnIdForType(int paramInt)
  {
    switch (paramInt)
    {
    case 1: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 13: 
    default: 
      return -1;
    case 15: 
      return 9;
    case 14: 
      return 8;
    case 12: 
      return 7;
    case 11: 
      return 5;
    case 10: 
      return 6;
    case 4: 
      return 3;
    case 3: 
      return 2;
    case 2: 
      return 1;
    }
    return 0;
  }
  
  private static String apnNameForApnId(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown id (");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(") in apnIdToType");
      Rlog.d("ApnContext", localStringBuilder.toString());
      return "default";
    case 9: 
      return "emergency";
    case 8: 
      return "ia";
    case 7: 
      return "cbs";
    case 6: 
      return "fota";
    case 5: 
      return "ims";
    case 4: 
      return "hipri";
    case 3: 
      return "dun";
    case 2: 
      return "supl";
    case 1: 
      return "mms";
    }
    return "default";
  }
  
  private boolean isFastRetryReason()
  {
    boolean bool;
    if ((!"nwTypeChanged".equals(mReason)) && (!"apnChanged".equals(mReason))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void log(String paramString)
  {
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ApnContext:");
    localStringBuilder.append(mApnType);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.d(str, localStringBuilder.toString());
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter arg2, String[] paramArrayOfString)
  {
    IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(???, "  ");
    synchronized (mRefCountLock)
    {
      localIndentingPrintWriter.println(toString());
      if (mNetworkRequests.size() > 0)
      {
        localIndentingPrintWriter.println("NetworkRequests:");
        localIndentingPrintWriter.increaseIndent();
        localIterator = mNetworkRequests.iterator();
        while (localIterator.hasNext()) {
          localIndentingPrintWriter.println((NetworkRequest)localIterator.next());
        }
        localIndentingPrintWriter.decreaseIndent();
      }
      localIndentingPrintWriter.increaseIndent();
      Iterator localIterator = mLocalLogs.iterator();
      while (localIterator.hasNext())
      {
        ((LocalLog)localIterator.next()).dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
        localIndentingPrintWriter.println("-----");
      }
      localIndentingPrintWriter.decreaseIndent();
      localIndentingPrintWriter.println("Historical APN state:");
      localIndentingPrintWriter.increaseIndent();
      mStateLocalLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
      localIndentingPrintWriter.decreaseIndent();
      localIndentingPrintWriter.println(mRetryManager);
      localIndentingPrintWriter.println("--------------------------");
      return;
    }
  }
  
  public ApnSetting getApnSetting()
  {
    try
    {
      ApnSetting localApnSetting = mApnSetting;
      return localApnSetting;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public String getApnType()
  {
    return mApnType;
  }
  
  public int getConnectionGeneration()
  {
    return mConnectionGeneration.get();
  }
  
  public DcAsyncChannel getDcAc()
  {
    try
    {
      DcAsyncChannel localDcAsyncChannel = mDcAc;
      return localDcAsyncChannel;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public long getDelayForNextApn(boolean paramBoolean)
  {
    RetryManager localRetryManager = mRetryManager;
    if ((!paramBoolean) && (!isFastRetryReason())) {
      paramBoolean = false;
    } else {
      paramBoolean = true;
    }
    return localRetryManager.getDelayForNextApn(paramBoolean);
  }
  
  public boolean getDependencyMet()
  {
    return mDependencyMet.get();
  }
  
  public List<NetworkRequest> getNetworkRequests()
  {
    synchronized (mRefCountLock)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mNetworkRequests);
      return localArrayList;
    }
  }
  
  public ApnSetting getNextApnSetting()
  {
    return mRetryManager.getNextApnSetting();
  }
  
  public String getReason()
  {
    try
    {
      String str = mReason;
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public PendingIntent getReconnectIntent()
  {
    try
    {
      PendingIntent localPendingIntent = mReconnectAlarmIntent;
      return localPendingIntent;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  long getRetryAfterDisconnectDelay()
  {
    return mRetryManager.getRetryAfterDisconnectDelay();
  }
  
  public DctConstants.State getState()
  {
    try
    {
      DctConstants.State localState = mState;
      return localState;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public ArrayList<ApnSetting> getWaitingApns()
  {
    return mRetryManager.getWaitingApns();
  }
  
  public boolean hasNoRestrictedRequests(boolean paramBoolean)
  {
    synchronized (mRefCountLock)
    {
      Iterator localIterator = mNetworkRequests.iterator();
      while (localIterator.hasNext())
      {
        NetworkRequest localNetworkRequest = (NetworkRequest)localIterator.next();
        if ((!paramBoolean) || (!networkCapabilities.hasCapability(2))) {
          if (!networkCapabilities.hasCapability(13)) {
            return false;
          }
        }
      }
      return true;
    }
  }
  
  public int incAndGetConnectionGeneration()
  {
    return mConnectionGeneration.incrementAndGet();
  }
  
  public boolean isConcurrentVoiceAndDataAllowed()
  {
    try
    {
      boolean bool = mConcurrentVoiceAndDataAllowed;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean isConnectable()
  {
    boolean bool;
    if ((isReady()) && ((mState == DctConstants.State.IDLE) || (mState == DctConstants.State.SCANNING) || (mState == DctConstants.State.RETRYING) || (mState == DctConstants.State.FAILED))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isConnectedOrConnecting()
  {
    boolean bool;
    if ((isReady()) && ((mState == DctConstants.State.CONNECTED) || (mState == DctConstants.State.CONNECTING) || (mState == DctConstants.State.SCANNING) || (mState == DctConstants.State.RETRYING))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDisconnected()
  {
    DctConstants.State localState = getState();
    boolean bool;
    if ((localState != DctConstants.State.IDLE) && (localState != DctConstants.State.FAILED)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isEnabled()
  {
    return mDataEnabled.get();
  }
  
  public boolean isLingering()
  {
    boolean bool;
    if ((mDcAc != null) && (mDcAc.isLingering())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isProvisioningApn()
  {
    String str = mPhone.getContext().getResources().getString(17040452);
    if ((!TextUtils.isEmpty(str)) && (mApnSetting != null) && (mApnSetting.apn != null)) {
      return mApnSetting.apn.equals(str);
    }
    return false;
  }
  
  public boolean isReady()
  {
    boolean bool;
    if ((mDataEnabled.get()) && (mDependencyMet.get()) && (mEnableUnderPolicy)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void markApnPermanentFailed(ApnSetting paramApnSetting)
  {
    mRetryManager.markApnPermanentFailed(paramApnSetting);
  }
  
  public void releaseDataConnection(String paramString)
  {
    try
    {
      if (mDcAc != null)
      {
        mDcAc.tearDown(this, paramString, null);
        mDcAc = null;
      }
      setState(DctConstants.State.IDLE);
      return;
    }
    finally {}
  }
  
  public void releaseNetwork(NetworkRequest paramNetworkRequest, LocalLog paramLocalLog)
  {
    synchronized (mRefCountLock)
    {
      if (!mLocalLogs.contains(paramLocalLog)) {
        paramLocalLog.log("ApnContext.releaseNetwork can't find this log");
      } else {
        mLocalLogs.remove(paramLocalLog);
      }
      if (!mNetworkRequests.contains(paramNetworkRequest))
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("ApnContext.releaseNetwork can't find this request (");
        localStringBuilder.append(paramNetworkRequest);
        localStringBuilder.append(")");
        paramLocalLog.log(localStringBuilder.toString());
      }
      else
      {
        mNetworkRequests.remove(paramNetworkRequest);
        paramNetworkRequest = new java/lang/StringBuilder;
        paramNetworkRequest.<init>();
        paramNetworkRequest.append("ApnContext.releaseNetwork left with ");
        paramNetworkRequest.append(mNetworkRequests.size());
        paramNetworkRequest.append(" requests.");
        paramLocalLog.log(paramNetworkRequest.toString());
        if (mNetworkRequests.size() == 0) {
          mDcTracker.setEnabled(apnIdForApnName(mApnType), false);
        }
      }
      return;
    }
  }
  
  public void requestLog(String paramString)
  {
    synchronized (mRefCountLock)
    {
      Iterator localIterator = mLocalLogs.iterator();
      while (localIterator.hasNext()) {
        ((LocalLog)localIterator.next()).log(paramString);
      }
      return;
    }
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, LocalLog paramLocalLog)
  {
    synchronized (mRefCountLock)
    {
      if ((!mLocalLogs.contains(paramLocalLog)) && (!mNetworkRequests.contains(paramNetworkRequest)))
      {
        mLocalLogs.add(paramLocalLog);
        mNetworkRequests.add(paramNetworkRequest);
        mDcTracker.setEnabled(apnIdForApnName(mApnType), true);
      }
      else
      {
        paramNetworkRequest = new java/lang/StringBuilder;
        paramNetworkRequest.<init>();
        paramNetworkRequest.append("ApnContext.requestNetwork has duplicate add - ");
        paramNetworkRequest.append(mNetworkRequests.size());
        paramLocalLog.log(paramNetworkRequest.toString());
      }
      return;
    }
  }
  
  public void resetErrorCodeRetries()
  {
    requestLog("ApnContext.resetErrorCodeRetries");
    String[] arrayOfString = mPhone.getContext().getResources().getStringArray(17235998);
    synchronized (mRetriesLeftPerErrorCode)
    {
      mRetriesLeftPerErrorCode.clear();
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        String str = arrayOfString[j];
        Object localObject2 = str.split(",");
        if (localObject2 != null)
        {
          int k = localObject2.length;
          if (k == 2) {
            try
            {
              k = Integer.parseInt(localObject2[0]);
              int m = Integer.parseInt(localObject2[1]);
              if ((m > 0) && (k > 0)) {
                mRetriesLeftPerErrorCode.put(k, m);
              }
            }
            catch (NumberFormatException localNumberFormatException)
            {
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("Exception parsing config_retries_per_error_code: ");
              ((StringBuilder)localObject2).append(localNumberFormatException);
              log(((StringBuilder)localObject2).toString());
            }
          }
        }
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Exception parsing config_retries_per_error_code: ");
        ((StringBuilder)localObject2).append(localNumberFormatException);
        log(((StringBuilder)localObject2).toString());
      }
      return;
    }
  }
  
  public boolean restartOnError(int paramInt)
  {
    boolean bool = false;
    synchronized (mRetriesLeftPerErrorCode)
    {
      int i = mRetriesLeftPerErrorCode.get(paramInt);
      SparseIntArray localSparseIntArray;
      switch (i)
      {
      default: 
        localSparseIntArray = mRetriesLeftPerErrorCode;
        break;
      case 1: 
        resetErrorCodeRetries();
        bool = true;
        break;
      case 0: 
        break;
      }
      localSparseIntArray.put(paramInt, i - 1);
      bool = false;
      ??? = new StringBuilder();
      ((StringBuilder)???).append("ApnContext.restartOnError(");
      ((StringBuilder)???).append(paramInt);
      ((StringBuilder)???).append(") found ");
      ((StringBuilder)???).append(i);
      ((StringBuilder)???).append(" and returned ");
      ((StringBuilder)???).append(bool);
      requestLog(((StringBuilder)???).toString());
      return bool;
    }
  }
  
  public void setApnSetting(ApnSetting paramApnSetting)
  {
    try
    {
      mApnSetting = paramApnSetting;
      return;
    }
    finally
    {
      paramApnSetting = finally;
      throw paramApnSetting;
    }
  }
  
  public void setConcurrentVoiceAndDataAllowed(boolean paramBoolean)
  {
    try
    {
      mConcurrentVoiceAndDataAllowed = paramBoolean;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setDataConnectionAc(DcAsyncChannel paramDcAsyncChannel)
  {
    try
    {
      mDcAc = paramDcAsyncChannel;
      return;
    }
    finally
    {
      paramDcAsyncChannel = finally;
      throw paramDcAsyncChannel;
    }
  }
  
  public void setDependencyMet(boolean paramBoolean)
  {
    mDependencyMet.set(paramBoolean);
  }
  
  public void setEnableUnderPolicy(boolean paramBoolean)
  {
    try
    {
      mEnableUnderPolicy = paramBoolean;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    mDataEnabled.set(paramBoolean);
  }
  
  public void setModemSuggestedDelay(long paramLong)
  {
    mRetryManager.setModemSuggestedDelay(paramLong);
  }
  
  public void setReason(String paramString)
  {
    try
    {
      mReason = paramString;
      return;
    }
    finally
    {
      paramString = finally;
      throw paramString;
    }
  }
  
  public void setReconnectIntent(PendingIntent paramPendingIntent)
  {
    try
    {
      mReconnectAlarmIntent = paramPendingIntent;
      return;
    }
    finally
    {
      paramPendingIntent = finally;
      throw paramPendingIntent;
    }
  }
  
  public void setState(DctConstants.State paramState)
  {
    try
    {
      if (mState != paramState)
      {
        LocalLog localLocalLog = mStateLocalLog;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("State changed from ");
        localStringBuilder.append(mState);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramState);
        localLocalLog.log(localStringBuilder.toString());
        mState = paramState;
      }
      if ((mState == DctConstants.State.FAILED) && (mRetryManager.getWaitingApns() != null)) {
        mRetryManager.getWaitingApns().clear();
      }
      return;
    }
    finally {}
  }
  
  public void setWaitingApns(ArrayList<ApnSetting> paramArrayList)
  {
    try
    {
      mRetryManager.setWaitingApns(paramArrayList);
      return;
    }
    finally
    {
      paramArrayList = finally;
      throw paramArrayList;
    }
  }
  
  public String toString()
  {
    try
    {
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("{mApnType=");
      ((StringBuilder)localObject1).append(mApnType);
      ((StringBuilder)localObject1).append(" mState=");
      ((StringBuilder)localObject1).append(getState());
      ((StringBuilder)localObject1).append(" mWaitingApns={");
      ((StringBuilder)localObject1).append(mRetryManager.getWaitingApns());
      ((StringBuilder)localObject1).append("} mApnSetting={");
      ((StringBuilder)localObject1).append(mApnSetting);
      ((StringBuilder)localObject1).append("} mReason=");
      ((StringBuilder)localObject1).append(mReason);
      ((StringBuilder)localObject1).append(" mDataEnabled=");
      ((StringBuilder)localObject1).append(mDataEnabled);
      ((StringBuilder)localObject1).append(" mDependencyMet=");
      ((StringBuilder)localObject1).append(mDependencyMet);
      ((StringBuilder)localObject1).append("}");
      localObject1 = ((StringBuilder)localObject1).toString();
      return localObject1;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }
}
