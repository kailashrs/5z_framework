package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.telephony.CellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessSpecifier;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class NetworkScanRequestTracker
{
  private static final int CMD_INTERRUPT_NETWORK_SCAN = 6;
  private static final int CMD_START_NETWORK_SCAN = 1;
  private static final int CMD_STOP_NETWORK_SCAN = 4;
  private static final int EVENT_INTERRUPT_NETWORK_SCAN_DONE = 7;
  private static final int EVENT_RECEIVE_NETWORK_SCAN_RESULT = 3;
  private static final int EVENT_START_NETWORK_SCAN_DONE = 2;
  private static final int EVENT_STOP_NETWORK_SCAN_DONE = 5;
  private static final String TAG = "ScanRequestTracker";
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 7: 
        NetworkScanRequestTracker.NetworkScanRequestScheduler.access$700(mScheduler, (AsyncResult)obj);
        break;
      case 6: 
        NetworkScanRequestTracker.NetworkScanRequestScheduler.access$600(mScheduler, arg1);
        break;
      case 5: 
        NetworkScanRequestTracker.NetworkScanRequestScheduler.access$500(mScheduler, (AsyncResult)obj);
        break;
      case 4: 
        NetworkScanRequestTracker.NetworkScanRequestScheduler.access$400(mScheduler, arg1);
        break;
      case 3: 
        NetworkScanRequestTracker.NetworkScanRequestScheduler.access$300(mScheduler, (AsyncResult)obj);
        break;
      case 2: 
        NetworkScanRequestTracker.NetworkScanRequestScheduler.access$200(mScheduler, (AsyncResult)obj);
        break;
      case 1: 
        NetworkScanRequestTracker.NetworkScanRequestScheduler.access$100(mScheduler, (NetworkScanRequestTracker.NetworkScanRequestInfo)obj);
      }
    }
  };
  private final AtomicInteger mNextNetworkScanRequestId = new AtomicInteger(1);
  private final NetworkScanRequestScheduler mScheduler = new NetworkScanRequestScheduler(null);
  
  public NetworkScanRequestTracker() {}
  
  private void interruptNetworkScan(int paramInt)
  {
    mHandler.obtainMessage(6, paramInt, 0).sendToTarget();
  }
  
  private boolean isValidScan(NetworkScanRequestInfo paramNetworkScanRequestInfo)
  {
    if ((mRequest != null) && (mRequest.getSpecifiers() != null))
    {
      if (mRequest.getSpecifiers().length > 8) {
        return false;
      }
      for (RadioAccessSpecifier localRadioAccessSpecifier : mRequest.getSpecifiers())
      {
        if ((localRadioAccessSpecifier.getRadioAccessNetwork() != 1) && (localRadioAccessSpecifier.getRadioAccessNetwork() != 2) && (localRadioAccessSpecifier.getRadioAccessNetwork() != 3)) {
          return false;
        }
        if ((localRadioAccessSpecifier.getBands() != null) && (localRadioAccessSpecifier.getBands().length > 8)) {
          return false;
        }
        if ((localRadioAccessSpecifier.getChannels() != null) && (localRadioAccessSpecifier.getChannels().length > 32)) {
          return false;
        }
      }
      if ((mRequest.getSearchPeriodicity() >= 5) && (mRequest.getSearchPeriodicity() <= 300))
      {
        if ((mRequest.getMaxSearchTime() >= 60) && (mRequest.getMaxSearchTime() <= 3600))
        {
          if ((mRequest.getIncrementalResultsPeriodicity() >= 1) && (mRequest.getIncrementalResultsPeriodicity() <= 10))
          {
            if ((mRequest.getSearchPeriodicity() <= mRequest.getMaxSearchTime()) && (mRequest.getIncrementalResultsPeriodicity() <= mRequest.getMaxSearchTime())) {
              return (mRequest.getPlmns() == null) || (mRequest.getPlmns().size() <= 20);
            }
            return false;
          }
          return false;
        }
        return false;
      }
      return false;
    }
    return false;
  }
  
  private void logEmptyResultOrException(AsyncResult paramAsyncResult)
  {
    if (result == null)
    {
      Log.e("ScanRequestTracker", "NetworkScanResult: Empty result");
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("NetworkScanResult: Exception: ");
      localStringBuilder.append(exception);
      Log.e("ScanRequestTracker", localStringBuilder.toString());
    }
  }
  
  private void notifyMessenger(NetworkScanRequestInfo paramNetworkScanRequestInfo, int paramInt1, int paramInt2, List<CellInfo> paramList)
  {
    Messenger localMessenger = mMessenger;
    Message localMessage = Message.obtain();
    what = paramInt1;
    arg1 = paramInt2;
    arg2 = mScanId;
    if (paramList != null)
    {
      paramNetworkScanRequestInfo = (CellInfo[])paramList.toArray(new CellInfo[paramList.size()]);
      paramList = new Bundle();
      paramList.putParcelableArray("scanResult", paramNetworkScanRequestInfo);
      localMessage.setData(paramList);
    }
    else
    {
      obj = null;
    }
    try
    {
      localMessenger.send(localMessage);
    }
    catch (RemoteException paramNetworkScanRequestInfo)
    {
      paramList = new StringBuilder();
      paramList.append("Exception in notifyMessenger: ");
      paramList.append(paramNetworkScanRequestInfo);
      Log.e("ScanRequestTracker", paramList.toString());
    }
  }
  
  public int startNetworkScan(NetworkScanRequest paramNetworkScanRequest, Messenger paramMessenger, IBinder paramIBinder, Phone paramPhone)
  {
    int i = mNextNetworkScanRequestId.getAndIncrement();
    paramNetworkScanRequest = new NetworkScanRequestInfo(paramNetworkScanRequest, paramMessenger, paramIBinder, i, paramPhone);
    mHandler.obtainMessage(1, paramNetworkScanRequest).sendToTarget();
    return i;
  }
  
  public void stopNetworkScan(int paramInt)
  {
    synchronized (mScheduler)
    {
      if (((mScheduler.mLiveRequestInfo != null) && (paramInt == access$1900mScheduler).mScanId) && (Binder.getCallingUid() == access$1900mScheduler).mUid)) || ((mScheduler.mPendingRequestInfo != null) && (paramInt == access$2000mScheduler).mScanId) && (Binder.getCallingUid() == access$2000mScheduler).mUid)))
      {
        mHandler.obtainMessage(4, paramInt, 0).sendToTarget();
        return;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Scan with id: ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" does not exist!");
      localIllegalArgumentException.<init>(localStringBuilder.toString());
      throw localIllegalArgumentException;
    }
  }
  
  class NetworkScanRequestInfo
    implements IBinder.DeathRecipient
  {
    private final IBinder mBinder;
    private boolean mIsBinderDead;
    private final Messenger mMessenger;
    private final Phone mPhone;
    private final int mPid;
    private final NetworkScanRequest mRequest;
    private final int mScanId;
    private final int mUid;
    
    NetworkScanRequestInfo(NetworkScanRequest paramNetworkScanRequest, Messenger paramMessenger, IBinder paramIBinder, int paramInt, Phone paramPhone)
    {
      mRequest = paramNetworkScanRequest;
      mMessenger = paramMessenger;
      mBinder = paramIBinder;
      mScanId = paramInt;
      mPhone = paramPhone;
      mUid = Binder.getCallingUid();
      mPid = Binder.getCallingPid();
      mIsBinderDead = false;
      try
      {
        mBinder.linkToDeath(this, 0);
      }
      catch (RemoteException this$1)
      {
        binderDied();
      }
    }
    
    public void binderDied()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PhoneInterfaceManager NetworkScanRequestInfo binderDied(");
      localStringBuilder.append(mRequest);
      localStringBuilder.append(", ");
      localStringBuilder.append(mBinder);
      localStringBuilder.append(")");
      Log.e("ScanRequestTracker", localStringBuilder.toString());
      setIsBinderDead(true);
      NetworkScanRequestTracker.this.interruptNetworkScan(mScanId);
    }
    
    boolean getIsBinderDead()
    {
      try
      {
        boolean bool = mIsBinderDead;
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    NetworkScanRequest getRequest()
    {
      return mRequest;
    }
    
    void setIsBinderDead(boolean paramBoolean)
    {
      try
      {
        mIsBinderDead = paramBoolean;
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    void unlinkDeathRecipient()
    {
      if (mBinder != null) {
        mBinder.unlinkToDeath(this, 0);
      }
    }
  }
  
  private class NetworkScanRequestScheduler
  {
    private NetworkScanRequestTracker.NetworkScanRequestInfo mLiveRequestInfo;
    private NetworkScanRequestTracker.NetworkScanRequestInfo mPendingRequestInfo;
    
    private NetworkScanRequestScheduler() {}
    
    private boolean cacheScan(NetworkScanRequestTracker.NetworkScanRequestInfo paramNetworkScanRequestInfo)
    {
      return false;
    }
    
    private int commandExceptionErrorToScanError(CommandException.Error paramError)
    {
      switch (NetworkScanRequestTracker.2.$SwitchMap$com$android$internal$telephony$CommandException$Error[paramError.ordinal()])
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("commandExceptionErrorToScanError: Unexpected CommandExceptionError ");
        localStringBuilder.append(paramError);
        Log.e("ScanRequestTracker", localStringBuilder.toString());
        return 10000;
      case 8: 
        Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: DEVICE_IN_USE");
        return 3;
      case 7: 
        Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: INVALID_ARGUMENTS");
        return 2;
      case 6: 
        Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: OPERATION_NOT_ALLOWED");
        return 1;
      case 5: 
        Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: MODEM_ERR");
        return 1;
      case 4: 
        Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: INTERNAL_ERR");
        return 1;
      case 3: 
        Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: NO_MEMORY");
        return 1;
      case 2: 
        Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: REQUEST_NOT_SUPPORTED");
        return 4;
      }
      Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: RADIO_NOT_AVAILABLE");
      return 1;
    }
    
    private void deleteScanAndMayNotify(NetworkScanRequestTracker.NetworkScanRequestInfo paramNetworkScanRequestInfo, int paramInt, boolean paramBoolean)
    {
      try
      {
        if ((mLiveRequestInfo != null) && (mScanId == mLiveRequestInfo.mScanId))
        {
          if (paramBoolean) {
            if (paramInt == 0) {
              NetworkScanRequestTracker.this.notifyMessenger(paramNetworkScanRequestInfo, 3, paramInt, null);
            } else {
              NetworkScanRequestTracker.this.notifyMessenger(paramNetworkScanRequestInfo, 2, paramInt, null);
            }
          }
          mLiveRequestInfo = null;
          if (mPendingRequestInfo != null)
          {
            startNewScan(mPendingRequestInfo);
            mPendingRequestInfo = null;
          }
        }
        return;
      }
      finally {}
    }
    
    private void doInterruptScan(int paramInt)
    {
      try
      {
        if ((mLiveRequestInfo != null) && (paramInt == mLiveRequestInfo.mScanId))
        {
          mLiveRequestInfo.mPhone.stopNetworkScan(mHandler.obtainMessage(7, mLiveRequestInfo));
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("doInterruptScan: scan ");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(" does not exist!");
          Log.e("ScanRequestTracker", localStringBuilder.toString());
        }
        return;
      }
      finally {}
    }
    
    private void doStartScan(NetworkScanRequestTracker.NetworkScanRequestInfo paramNetworkScanRequestInfo)
    {
      if (paramNetworkScanRequestInfo == null)
      {
        Log.e("ScanRequestTracker", "CMD_START_NETWORK_SCAN: nsri is null");
        return;
      }
      if (!NetworkScanRequestTracker.this.isValidScan(paramNetworkScanRequestInfo))
      {
        NetworkScanRequestTracker.this.notifyMessenger(paramNetworkScanRequestInfo, 2, 2, null);
        return;
      }
      if (paramNetworkScanRequestInfo.getIsBinderDead())
      {
        Log.e("ScanRequestTracker", "CMD_START_NETWORK_SCAN: Binder has died");
        return;
      }
      if ((!startNewScan(paramNetworkScanRequestInfo)) && (!interruptLiveScan(paramNetworkScanRequestInfo)) && (!cacheScan(paramNetworkScanRequestInfo))) {
        NetworkScanRequestTracker.this.notifyMessenger(paramNetworkScanRequestInfo, 2, 3, null);
      }
    }
    
    private void doStopScan(int paramInt)
    {
      try
      {
        if ((mLiveRequestInfo != null) && (paramInt == mLiveRequestInfo.mScanId))
        {
          mLiveRequestInfo.mPhone.stopNetworkScan(mHandler.obtainMessage(5, mLiveRequestInfo));
        }
        else if ((mPendingRequestInfo != null) && (paramInt == mPendingRequestInfo.mScanId))
        {
          NetworkScanRequestTracker.this.notifyMessenger(mPendingRequestInfo, 3, 0, null);
          mPendingRequestInfo = null;
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("stopScan: scan ");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(" does not exist!");
          Log.e("ScanRequestTracker", localStringBuilder.toString());
        }
        return;
      }
      finally {}
    }
    
    private boolean interruptLiveScan(NetworkScanRequestTracker.NetworkScanRequestInfo paramNetworkScanRequestInfo)
    {
      try
      {
        if ((mLiveRequestInfo != null) && (mPendingRequestInfo == null) && (mUid == 1001) && (mLiveRequestInfo.mUid != 1001))
        {
          doInterruptScan(mLiveRequestInfo.mScanId);
          mPendingRequestInfo = paramNetworkScanRequestInfo;
          NetworkScanRequestTracker.this.notifyMessenger(mLiveRequestInfo, 2, 10002, null);
          return true;
        }
        return false;
      }
      finally
      {
        paramNetworkScanRequestInfo = finally;
        throw paramNetworkScanRequestInfo;
      }
    }
    
    private void interruptScanDone(AsyncResult paramAsyncResult)
    {
      paramAsyncResult = (NetworkScanRequestTracker.NetworkScanRequestInfo)userObj;
      if (paramAsyncResult == null)
      {
        Log.e("ScanRequestTracker", "EVENT_INTERRUPT_NETWORK_SCAN_DONE: nsri is null");
        return;
      }
      mPhone.mCi.unregisterForNetworkScanResult(mHandler);
      deleteScanAndMayNotify(paramAsyncResult, 0, false);
    }
    
    private void receiveResult(AsyncResult paramAsyncResult)
    {
      NetworkScanRequestTracker.NetworkScanRequestInfo localNetworkScanRequestInfo = (NetworkScanRequestTracker.NetworkScanRequestInfo)userObj;
      if (localNetworkScanRequestInfo == null)
      {
        Log.e("ScanRequestTracker", "EVENT_RECEIVE_NETWORK_SCAN_RESULT: nsri is null");
        return;
      }
      if ((exception == null) && (result != null))
      {
        paramAsyncResult = (NetworkScanResult)result;
        if (scanError == 0)
        {
          NetworkScanRequestTracker.this.notifyMessenger(localNetworkScanRequestInfo, 1, rilErrorToScanError(scanError), networkInfos);
          if (scanStatus == 2)
          {
            deleteScanAndMayNotify(localNetworkScanRequestInfo, 0, true);
            mPhone.mCi.unregisterForNetworkScanResult(mHandler);
          }
        }
        else
        {
          if (networkInfos != null) {
            NetworkScanRequestTracker.this.notifyMessenger(localNetworkScanRequestInfo, 1, 0, networkInfos);
          }
          deleteScanAndMayNotify(localNetworkScanRequestInfo, rilErrorToScanError(scanError), true);
          mPhone.mCi.unregisterForNetworkScanResult(mHandler);
        }
      }
      else
      {
        NetworkScanRequestTracker.this.logEmptyResultOrException(paramAsyncResult);
        deleteScanAndMayNotify(localNetworkScanRequestInfo, 10000, true);
        mPhone.mCi.unregisterForNetworkScanResult(mHandler);
      }
    }
    
    private int rilErrorToScanError(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("rilErrorToScanError: Unexpected RadioError ");
        localStringBuilder.append(paramInt);
        Log.e("ScanRequestTracker", localStringBuilder.toString());
        return 10000;
      case 64: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: DEVICE_IN_USE");
        return 3;
      case 54: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: OPERATION_NOT_ALLOWED");
        return 1;
      case 44: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: INVALID_ARGUMENTS");
        return 2;
      case 40: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: MODEM_ERR");
        return 1;
      case 38: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: INTERNAL_ERR");
        return 1;
      case 37: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: NO_MEMORY");
        return 1;
      case 6: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: REQUEST_NOT_SUPPORTED");
        return 4;
      case 1: 
        Log.e("ScanRequestTracker", "rilErrorToScanError: RADIO_NOT_AVAILABLE");
        return 1;
      }
      return 0;
    }
    
    private boolean startNewScan(NetworkScanRequestTracker.NetworkScanRequestInfo paramNetworkScanRequestInfo)
    {
      try
      {
        if (mLiveRequestInfo == null)
        {
          mLiveRequestInfo = paramNetworkScanRequestInfo;
          mPhone.startNetworkScan(paramNetworkScanRequestInfo.getRequest(), mHandler.obtainMessage(2, paramNetworkScanRequestInfo));
          return true;
        }
        return false;
      }
      finally
      {
        paramNetworkScanRequestInfo = finally;
        throw paramNetworkScanRequestInfo;
      }
    }
    
    private void startScanDone(AsyncResult paramAsyncResult)
    {
      try
      {
        NetworkScanRequestTracker.NetworkScanRequestInfo localNetworkScanRequestInfo = (NetworkScanRequestTracker.NetworkScanRequestInfo)userObj;
        if (localNetworkScanRequestInfo == null)
        {
          Log.e("ScanRequestTracker", "EVENT_START_NETWORK_SCAN_DONE: nsri is null");
          return;
        }
        if ((mLiveRequestInfo != null) && (mScanId == mLiveRequestInfo.mScanId))
        {
          if ((exception == null) && (result != null))
          {
            mPhone.mCi.registerForNetworkScanResult(mHandler, 3, localNetworkScanRequestInfo);
          }
          else
          {
            NetworkScanRequestTracker.this.logEmptyResultOrException(paramAsyncResult);
            if (exception != null) {
              deleteScanAndMayNotify(localNetworkScanRequestInfo, commandExceptionErrorToScanError(((CommandException)exception).getCommandError()), true);
            } else {
              Log.wtf("ScanRequestTracker", "EVENT_START_NETWORK_SCAN_DONE: ar.exception can not be null!");
            }
          }
          return;
        }
        Log.e("ScanRequestTracker", "EVENT_START_NETWORK_SCAN_DONE: nsri does not match mLiveRequestInfo");
        return;
      }
      finally {}
    }
    
    private void stopScanDone(AsyncResult paramAsyncResult)
    {
      NetworkScanRequestTracker.NetworkScanRequestInfo localNetworkScanRequestInfo = (NetworkScanRequestTracker.NetworkScanRequestInfo)userObj;
      if (localNetworkScanRequestInfo == null)
      {
        Log.e("ScanRequestTracker", "EVENT_STOP_NETWORK_SCAN_DONE: nsri is null");
        return;
      }
      if ((exception == null) && (result != null))
      {
        deleteScanAndMayNotify(localNetworkScanRequestInfo, 0, true);
      }
      else
      {
        NetworkScanRequestTracker.this.logEmptyResultOrException(paramAsyncResult);
        if (exception != null) {
          deleteScanAndMayNotify(localNetworkScanRequestInfo, commandExceptionErrorToScanError(((CommandException)exception).getCommandError()), true);
        } else {
          Log.wtf("ScanRequestTracker", "EVENT_STOP_NETWORK_SCAN_DONE: ar.exception can not be null!");
        }
      }
      mPhone.mCi.unregisterForNetworkScanResult(mHandler);
    }
  }
}
