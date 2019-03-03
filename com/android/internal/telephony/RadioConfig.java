package com.android.internal.telephony;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.hardware.radio.V1_0.RadioResponseInfo;
import android.hardware.radio.config.V1_0.IRadioConfig;
import android.hardware.radio.config.V1_0.SimSlotStatus;
import android.net.ConnectivityManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IHwBinder.DeathRecipient;
import android.os.Message;
import android.os.Registrant;
import android.os.RemoteException;
import android.os.WorkSource;
import android.telephony.Rlog;
import android.util.SparseArray;
import com.android.internal.telephony.uicc.IccSlotStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

public class RadioConfig
  extends Handler
{
  private static final boolean DBG = true;
  private static final int EVENT_SERVICE_DEAD = 1;
  private static final String TAG = "RadioConfig";
  private static final boolean VDBG = false;
  private static RadioConfig sRadioConfig;
  private final WorkSource mDefaultWorkSource;
  private final boolean mIsMobileNetworkSupported;
  private final RadioConfigIndication mRadioConfigIndication;
  private volatile IRadioConfig mRadioConfigProxy = null;
  private final AtomicLong mRadioConfigProxyCookie = new AtomicLong(0L);
  private final RadioConfigResponse mRadioConfigResponse;
  private final SparseArray<RILRequest> mRequestList = new SparseArray();
  private final ServiceDeathRecipient mServiceDeathRecipient;
  protected Registrant mSimSlotStatusRegistrant;
  
  private RadioConfig(Context paramContext)
  {
    mIsMobileNetworkSupported = ((ConnectivityManager)paramContext.getSystemService("connectivity")).isNetworkSupported(0);
    mRadioConfigResponse = new RadioConfigResponse(this);
    mRadioConfigIndication = new RadioConfigIndication(this);
    mServiceDeathRecipient = new ServiceDeathRecipient();
    mDefaultWorkSource = new WorkSource(getApplicationInfouid, paramContext.getPackageName());
  }
  
  private void clearRequestList(int paramInt, boolean paramBoolean)
  {
    synchronized (mRequestList)
    {
      int i = mRequestList.size();
      StringBuilder localStringBuilder;
      if (paramBoolean)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("clearRequestList: mRequestList=");
        localStringBuilder.append(i);
        logd(localStringBuilder.toString());
      }
      for (int j = 0; j < i; j++)
      {
        RILRequest localRILRequest = (RILRequest)mRequestList.valueAt(j);
        if (paramBoolean)
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append(j);
          localStringBuilder.append(": [");
          localStringBuilder.append(mSerial);
          localStringBuilder.append("] ");
          localStringBuilder.append(requestToString(mRequest));
          logd(localStringBuilder.toString());
        }
        localRILRequest.onError(paramInt, null);
        localRILRequest.release();
      }
      mRequestList.clear();
      return;
    }
  }
  
  static ArrayList<IccSlotStatus> convertHalSlotStatus(ArrayList<SimSlotStatus> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList(paramArrayList.size());
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
    {
      SimSlotStatus localSimSlotStatus = (SimSlotStatus)paramArrayList.next();
      IccSlotStatus localIccSlotStatus = new IccSlotStatus();
      localIccSlotStatus.setCardState(cardState);
      localIccSlotStatus.setSlotState(slotState);
      logicalSlotIndex = logicalSlotId;
      atr = atr;
      iccid = iccid;
      localArrayList.add(localIccSlotStatus);
    }
    return localArrayList;
  }
  
  private RILRequest findAndRemoveRequestFromList(int paramInt)
  {
    synchronized (mRequestList)
    {
      RILRequest localRILRequest = (RILRequest)mRequestList.get(paramInt);
      if (localRILRequest != null) {
        mRequestList.remove(paramInt);
      }
      return localRILRequest;
    }
  }
  
  public static RadioConfig getInstance(Context paramContext)
  {
    if (sRadioConfig == null) {
      sRadioConfig = new RadioConfig(paramContext);
    }
    return sRadioConfig;
  }
  
  private static void logd(String paramString)
  {
    Rlog.d("RadioConfig", paramString);
  }
  
  private static void loge(String paramString)
  {
    Rlog.e("RadioConfig", paramString);
  }
  
  private RILRequest obtainRequest(int paramInt, Message arg2, WorkSource paramWorkSource)
  {
    paramWorkSource = RILRequest.obtain(paramInt, ???, paramWorkSource);
    synchronized (mRequestList)
    {
      mRequestList.append(mSerial, paramWorkSource);
      return paramWorkSource;
    }
  }
  
  private static ArrayList<Integer> primitiveArrayToArrayList(int[] paramArrayOfInt)
  {
    ArrayList localArrayList = new ArrayList(paramArrayOfInt.length);
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      localArrayList.add(Integer.valueOf(paramArrayOfInt[j]));
    }
    return localArrayList;
  }
  
  static String requestToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "<unknown request>";
    case 145: 
      return "SET_LOGICAL_TO_PHYSICAL_SLOT_MAPPING";
    }
    return "GET_SLOT_STATUS";
  }
  
  private void resetProxyAndRequestList(String paramString, Exception paramException)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(": ");
    localStringBuilder.append(paramException);
    loge(localStringBuilder.toString());
    mRadioConfigProxy = null;
    mRadioConfigProxyCookie.incrementAndGet();
    RILRequest.resetSerial();
    clearRequestList(1, false);
    getRadioConfigProxy(null);
  }
  
  public IRadioConfig getRadioConfigProxy(Message paramMessage)
  {
    if (!mIsMobileNetworkSupported)
    {
      if (paramMessage != null)
      {
        AsyncResult.forMessage(paramMessage, null, CommandException.fromRilErrno(1));
        paramMessage.sendToTarget();
      }
      return null;
    }
    if (mRadioConfigProxy != null) {
      return mRadioConfigProxy;
    }
    try
    {
      mRadioConfigProxy = IRadioConfig.getService(true);
      if (mRadioConfigProxy != null)
      {
        mRadioConfigProxy.linkToDeath(mServiceDeathRecipient, mRadioConfigProxyCookie.incrementAndGet());
        mRadioConfigProxy.setResponseFunctions(mRadioConfigResponse, mRadioConfigIndication);
      }
      else
      {
        loge("getRadioConfigProxy: mRadioConfigProxy == null");
      }
    }
    catch (RemoteException|RuntimeException localRemoteException)
    {
      mRadioConfigProxy = null;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getRadioConfigProxy: RadioConfigProxy getService/setResponseFunctions: ");
      localStringBuilder.append(localRemoteException);
      loge(localStringBuilder.toString());
    }
    if (mRadioConfigProxy == null)
    {
      loge("getRadioConfigProxy: mRadioConfigProxy == null");
      if (paramMessage != null)
      {
        AsyncResult.forMessage(paramMessage, null, CommandException.fromRilErrno(1));
        paramMessage.sendToTarget();
      }
    }
    return mRadioConfigProxy;
  }
  
  public void getSimSlotsStatus(Message paramMessage)
  {
    IRadioConfig localIRadioConfig = getRadioConfigProxy(paramMessage);
    if (localIRadioConfig != null)
    {
      paramMessage = obtainRequest(144, paramMessage, mDefaultWorkSource);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramMessage.serialString());
      localStringBuilder.append("> ");
      localStringBuilder.append(requestToString(mRequest));
      logd(localStringBuilder.toString());
      try
      {
        localIRadioConfig.getSimSlotsStatus(mSerial);
      }
      catch (RemoteException|RuntimeException paramMessage)
      {
        resetProxyAndRequestList("getSimSlotsStatus", paramMessage);
      }
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    if (what == 1)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleMessage: EVENT_SERVICE_DEAD cookie = ");
      localStringBuilder.append(obj);
      localStringBuilder.append(" mRadioConfigProxyCookie = ");
      localStringBuilder.append(mRadioConfigProxyCookie.get());
      logd(localStringBuilder.toString());
      if (((Long)obj).longValue() == mRadioConfigProxyCookie.get()) {
        resetProxyAndRequestList("EVENT_SERVICE_DEAD", null);
      }
    }
  }
  
  public RILRequest processResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    int i = serial;
    int j = error;
    int k = type;
    if (k != 0)
    {
      paramRadioResponseInfo = new StringBuilder();
      paramRadioResponseInfo.append("processResponse: Unexpected response type ");
      paramRadioResponseInfo.append(k);
      loge(paramRadioResponseInfo.toString());
    }
    paramRadioResponseInfo = findAndRemoveRequestFromList(i);
    if (paramRadioResponseInfo == null)
    {
      paramRadioResponseInfo = new StringBuilder();
      paramRadioResponseInfo.append("processResponse: Unexpected response! serial: ");
      paramRadioResponseInfo.append(i);
      paramRadioResponseInfo.append(" error: ");
      paramRadioResponseInfo.append(j);
      loge(paramRadioResponseInfo.toString());
      return null;
    }
    return paramRadioResponseInfo;
  }
  
  public void registerForSimSlotStatusChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSimSlotStatusRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setSimSlotsMapping(int[] paramArrayOfInt, Message paramMessage)
  {
    IRadioConfig localIRadioConfig = getRadioConfigProxy(paramMessage);
    if (localIRadioConfig != null)
    {
      paramMessage = obtainRequest(145, paramMessage, mDefaultWorkSource);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramMessage.serialString());
      localStringBuilder.append("> ");
      localStringBuilder.append(requestToString(mRequest));
      localStringBuilder.append(" ");
      localStringBuilder.append(Arrays.toString(paramArrayOfInt));
      logd(localStringBuilder.toString());
      try
      {
        localIRadioConfig.setSimSlotsMapping(mSerial, primitiveArrayToArrayList(paramArrayOfInt));
      }
      catch (RemoteException|RuntimeException paramArrayOfInt)
      {
        resetProxyAndRequestList("setSimSlotsMapping", paramArrayOfInt);
      }
    }
  }
  
  public void unregisterForSimSlotStatusChanged(Handler paramHandler)
  {
    if ((mSimSlotStatusRegistrant != null) && (mSimSlotStatusRegistrant.getHandler() == paramHandler))
    {
      mSimSlotStatusRegistrant.clear();
      mSimSlotStatusRegistrant = null;
    }
  }
  
  final class ServiceDeathRecipient
    implements IHwBinder.DeathRecipient
  {
    ServiceDeathRecipient() {}
    
    public void serviceDied(long paramLong)
    {
      RadioConfig.logd("serviceDied");
      sendMessage(obtainMessage(1, Long.valueOf(paramLong)));
    }
  }
}
