package com.android.internal.telephony.dataconnection;

import android.hardware.radio.V1_0.SetupDataCallResult;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkUtils;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.DataService;
import android.telephony.data.DataService.DataServiceProvider;
import android.telephony.data.DataServiceCallback;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CellularDataService
  extends DataService
{
  private static final int DATA_CALL_LIST_CHANGED = 6;
  private static final boolean DBG = false;
  private static final int DEACTIVATE_DATA_ALL_COMPLETE = 2;
  private static final int GET_DATA_CALL_LIST_COMPLETE = 5;
  private static final int SETUP_DATA_CALL_COMPLETE = 1;
  private static final int SET_DATA_PROFILE_COMPLETE = 4;
  private static final int SET_INITIAL_ATTACH_APN_COMPLETE = 3;
  private static final String TAG = CellularDataService.class.getSimpleName();
  
  public CellularDataService() {}
  
  private void log(String paramString)
  {
    Rlog.d(TAG, paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e(TAG, paramString);
  }
  
  @VisibleForTesting
  public DataCallResponse convertDataCallResult(SetupDataCallResult paramSetupDataCallResult)
  {
    if (paramSetupDataCallResult == null) {
      return null;
    }
    String[] arrayOfString = null;
    if (!TextUtils.isEmpty(addresses)) {
      arrayOfString = addresses.split("\\s+");
    }
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int k;
    int m;
    if (arrayOfString != null)
    {
      int j = arrayOfString.length;
      for (k = 0; k < j; k++)
      {
        localObject1 = arrayOfString[k].trim();
        if (!((String)localObject1).isEmpty()) {
          try
          {
            if (((String)localObject1).split("/").length == 2)
            {
              localObject2 = new android/net/LinkAddress;
              ((LinkAddress)localObject2).<init>((String)localObject1);
            }
            else
            {
              localObject2 = NetworkUtils.numericToInetAddress((String)localObject1);
              if ((localObject2 instanceof Inet4Address)) {
                m = 32;
              } else {
                m = 128;
              }
              localObject2 = new LinkAddress((InetAddress)localObject2, m);
            }
            localArrayList.add(localObject2);
          }
          catch (IllegalArgumentException localIllegalArgumentException1)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Unknown address: ");
            ((StringBuilder)localObject2).append((String)localObject1);
            ((StringBuilder)localObject2).append(", exception = ");
            ((StringBuilder)localObject2).append(localIllegalArgumentException1);
            loge(((StringBuilder)localObject2).toString());
          }
        }
      }
    }
    arrayOfString = null;
    if (!TextUtils.isEmpty(dnses)) {
      arrayOfString = dnses.split("\\s+");
    }
    Object localObject2 = new ArrayList();
    String str;
    if (arrayOfString != null)
    {
      m = arrayOfString.length;
      for (k = 0; k < m; k++)
      {
        str = arrayOfString[k].trim();
        try
        {
          ((List)localObject2).add(NetworkUtils.numericToInetAddress(str));
        }
        catch (IllegalArgumentException localIllegalArgumentException2)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unknown dns: ");
          ((StringBuilder)localObject1).append(str);
          ((StringBuilder)localObject1).append(", exception = ");
          ((StringBuilder)localObject1).append(localIllegalArgumentException2);
          loge(((StringBuilder)localObject1).toString());
        }
      }
    }
    arrayOfString = null;
    if (!TextUtils.isEmpty(gateways)) {
      arrayOfString = gateways.split("\\s+");
    }
    Object localObject1 = new ArrayList();
    if (arrayOfString != null)
    {
      m = arrayOfString.length;
      for (k = i; k < m; k++)
      {
        str = arrayOfString[k].trim();
        try
        {
          ((List)localObject1).add(NetworkUtils.numericToInetAddress(str));
        }
        catch (IllegalArgumentException localIllegalArgumentException3)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown gateway: ");
          localStringBuilder.append(str);
          localStringBuilder.append(", exception = ");
          localStringBuilder.append(localIllegalArgumentException3);
          loge(localStringBuilder.toString());
        }
      }
    }
    return new DataCallResponse(status, suggestedRetryTime, cid, active, type, ifname, localArrayList, (List)localObject2, (List)localObject1, new ArrayList(Arrays.asList(pcscf.trim().split("\\s+"))), mtu);
  }
  
  public DataService.DataServiceProvider createDataServiceProvider(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Cellular data service created for slot ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    if (!SubscriptionManager.isValidSlotIndex(paramInt))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Tried to cellular data service with invalid slotId ");
      localStringBuilder.append(paramInt);
      loge(localStringBuilder.toString());
      return null;
    }
    return new CellularDataServiceProvider(paramInt, null);
  }
  
  private class CellularDataServiceProvider
    extends DataService.DataServiceProvider
  {
    private final Map<Message, DataServiceCallback> mCallbackMap = new HashMap();
    private final Handler mHandler;
    private final Looper mLooper;
    private final Phone mPhone = PhoneFactory.getPhone(getSlotId());
    
    private CellularDataServiceProvider(int paramInt)
    {
      super(paramInt);
      HandlerThread localHandlerThread = new HandlerThread(CellularDataService.class.getSimpleName());
      localHandlerThread.start();
      mLooper = localHandlerThread.getLooper();
      mHandler = new Handler(mLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          Object localObject1 = (DataServiceCallback)mCallbackMap.remove(paramAnonymousMessage);
          Object localObject2 = (AsyncResult)obj;
          int i = what;
          int j = 0;
          int k = 0;
          int m = 0;
          int n = 0;
          int i1 = 0;
          switch (i)
          {
          default: 
            localObject2 = CellularDataService.this;
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Unexpected event: ");
            ((StringBuilder)localObject1).append(what);
            ((CellularDataService)localObject2).loge(((StringBuilder)localObject1).toString());
            return;
          case 6: 
            notifyDataCallListChanged(CellularDataService.CellularDataServiceProvider.this.getDataCallList((List)result));
            break;
          case 5: 
            if (exception != null) {
              i1 = 4;
            }
            if (exception != null) {
              paramAnonymousMessage = null;
            } else {
              paramAnonymousMessage = CellularDataService.CellularDataServiceProvider.this.getDataCallList((List)result);
            }
            ((DataServiceCallback)localObject1).onGetDataCallListComplete(i1, paramAnonymousMessage);
            break;
          case 4: 
            if (exception != null) {
              i1 = 4;
            } else {
              i1 = j;
            }
            ((DataServiceCallback)localObject1).onSetDataProfileComplete(i1);
            break;
          case 3: 
            if (exception != null) {
              i1 = 4;
            } else {
              i1 = k;
            }
            ((DataServiceCallback)localObject1).onSetInitialAttachApnComplete(i1);
            break;
          case 2: 
            if (exception != null) {
              i1 = 4;
            } else {
              i1 = m;
            }
            ((DataServiceCallback)localObject1).onDeactivateDataCallComplete(i1);
            break;
          case 1: 
            paramAnonymousMessage = (SetupDataCallResult)result;
            i1 = n;
            if (exception != null) {
              i1 = 4;
            }
            ((DataServiceCallback)localObject1).onSetupDataCallComplete(i1, convertDataCallResult(paramAnonymousMessage));
          }
        }
      };
      mPhone.mCi.registerForDataCallListChanged(mHandler, 6, null);
    }
    
    private List<DataCallResponse> getDataCallList(List<SetupDataCallResult> paramList)
    {
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        paramList = (SetupDataCallResult)localIterator.next();
        localArrayList.add(convertDataCallResult(paramList));
      }
      return localArrayList;
    }
    
    public void deactivateDataCall(int paramInt1, int paramInt2, DataServiceCallback paramDataServiceCallback)
    {
      Message localMessage = null;
      if (paramDataServiceCallback != null)
      {
        localMessage = Message.obtain(mHandler, 2);
        mCallbackMap.put(localMessage, paramDataServiceCallback);
      }
      mPhone.mCi.deactivateDataCall(paramInt1, paramInt2, localMessage);
    }
    
    public void getDataCallList(DataServiceCallback paramDataServiceCallback)
    {
      Message localMessage = null;
      if (paramDataServiceCallback != null)
      {
        localMessage = Message.obtain(mHandler, 5);
        mCallbackMap.put(localMessage, paramDataServiceCallback);
      }
      mPhone.mCi.getDataCallList(localMessage);
    }
    
    public void setDataProfile(List<DataProfile> paramList, boolean paramBoolean, DataServiceCallback paramDataServiceCallback)
    {
      Message localMessage = null;
      if (paramDataServiceCallback != null)
      {
        localMessage = Message.obtain(mHandler, 4);
        mCallbackMap.put(localMessage, paramDataServiceCallback);
      }
      mPhone.mCi.setDataProfile((DataProfile[])paramList.toArray(new DataProfile[paramList.size()]), paramBoolean, localMessage);
    }
    
    public void setInitialAttachApn(DataProfile paramDataProfile, boolean paramBoolean, DataServiceCallback paramDataServiceCallback)
    {
      Message localMessage = null;
      if (paramDataServiceCallback != null)
      {
        localMessage = Message.obtain(mHandler, 3);
        mCallbackMap.put(localMessage, paramDataServiceCallback);
      }
      mPhone.mCi.setInitialAttachApn(paramDataProfile, paramBoolean, localMessage);
    }
    
    public void setupDataCall(int paramInt1, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, LinkProperties paramLinkProperties, DataServiceCallback paramDataServiceCallback)
    {
      Message localMessage = null;
      if (paramDataServiceCallback != null)
      {
        localMessage = Message.obtain(mHandler, 1);
        mCallbackMap.put(localMessage, paramDataServiceCallback);
      }
      mPhone.mCi.setupDataCall(paramInt1, paramDataProfile, paramBoolean1, paramBoolean2, paramInt2, paramLinkProperties, localMessage);
    }
  }
}
