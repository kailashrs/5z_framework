package com.android.internal.telephony;

import android.hardware.radio.V1_2.CellIdentityOperatorNames;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.NetworkRegistrationState;
import android.telephony.NetworkService;
import android.telephony.NetworkService.NetworkServiceProvider;
import android.telephony.NetworkServiceCallback;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class CellularNetworkService
  extends NetworkService
{
  private static final boolean DBG = false;
  private static final int GET_CS_REGISTRATION_STATE_DONE = 1;
  private static final int GET_PS_REGISTRATION_STATE_DONE = 2;
  private static final int NETWORK_REGISTRATION_STATE_CHANGED = 3;
  private static final String TAG = CellularNetworkService.class.getSimpleName();
  
  public CellularNetworkService() {}
  
  private void log(String paramString)
  {
    Rlog.d(TAG, paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e(TAG, paramString);
  }
  
  protected NetworkService.NetworkServiceProvider createNetworkServiceProvider(int paramInt)
  {
    if (!SubscriptionManager.isValidSlotIndex(paramInt))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Tried to Cellular network service with invalid slotId ");
      localStringBuilder.append(paramInt);
      loge(localStringBuilder.toString());
      return null;
    }
    return new CellularNetworkServiceProvider(paramInt);
  }
  
  private class CellularNetworkServiceProvider
    extends NetworkService.NetworkServiceProvider
  {
    private final ConcurrentHashMap<Message, NetworkServiceCallback> mCallbackMap = new ConcurrentHashMap();
    private final Handler mHandler;
    private final HandlerThread mHandlerThread = new HandlerThread(CellularNetworkService.class.getSimpleName());
    private final Looper mLooper;
    private final Phone mPhone = PhoneFactory.getPhone(getSlotId());
    
    CellularNetworkServiceProvider(int paramInt)
    {
      super(paramInt);
      mHandlerThread.start();
      mLooper = mHandlerThread.getLooper();
      mHandler = new Handler(mLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          NetworkServiceCallback localNetworkServiceCallback = (NetworkServiceCallback)mCallbackMap.remove(paramAnonymousMessage);
          switch (what)
          {
          default: 
            
          case 3: 
            notifyNetworkRegistrationStateChanged();
            break;
          case 1: 
          case 2: 
            if (localNetworkServiceCallback == null) {
              return;
            }
            Object localObject = (AsyncResult)obj;
            int i = what;
            int j = 1;
            if (i != 1) {
              j = 2;
            }
            paramAnonymousMessage = CellularNetworkService.CellularNetworkServiceProvider.this.getRegistrationStateFromResult(result, j);
            if ((exception == null) && (paramAnonymousMessage != null)) {
              j = 0;
            } else {
              j = 5;
            }
            try
            {
              localNetworkServiceCallback.onGetNetworkRegistrationStateComplete(j, paramAnonymousMessage);
            }
            catch (Exception localException)
            {
              localObject = CellularNetworkService.this;
              paramAnonymousMessage = new StringBuilder();
              paramAnonymousMessage.append("Exception: ");
              paramAnonymousMessage.append(localException);
              ((CellularNetworkService)localObject).loge(paramAnonymousMessage.toString());
            }
          }
        }
      };
      mPhone.mCi.registerForNetworkStateChanged(mHandler, 3, null);
    }
    
    private android.telephony.CellIdentity convertHalCellIdentityToCellIdentity(android.hardware.radio.V1_0.CellIdentity paramCellIdentity)
    {
      if (paramCellIdentity == null) {
        return null;
      }
      Object localObject = null;
      switch (cellInfoType)
      {
      default: 
        break;
      case 5: 
        if (cellIdentityTdscdma.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_0.CellIdentityTdscdma)cellIdentityTdscdma.get(0);
          localObject = new android.telephony.CellIdentityTdscdma(mcc, mnc, lac, cid, cpid);
        }
        break;
      case 4: 
        if (cellIdentityWcdma.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_0.CellIdentityWcdma)cellIdentityWcdma.get(0);
          localObject = new android.telephony.CellIdentityWcdma(lac, cid, psc, uarfcn, mcc, mnc, null, null);
        }
        break;
      case 3: 
        if (cellIdentityLte.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_0.CellIdentityLte)cellIdentityLte.get(0);
          localObject = new android.telephony.CellIdentityLte(ci, pci, tac, earfcn, Integer.MAX_VALUE, mcc, mnc, null, null);
        }
        break;
      case 2: 
        if (cellIdentityCdma.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_0.CellIdentityCdma)cellIdentityCdma.get(0);
          localObject = new android.telephony.CellIdentityCdma(networkId, systemId, baseStationId, longitude, latitude);
        }
        break;
      case 1: 
        if (cellIdentityGsm.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_0.CellIdentityGsm)cellIdentityGsm.get(0);
          localObject = new android.telephony.CellIdentityGsm(lac, cid, arfcn, bsic, mcc, mnc, null, null);
        }
        break;
      }
      return localObject;
    }
    
    private android.telephony.CellIdentity convertHalCellIdentityToCellIdentity(android.hardware.radio.V1_2.CellIdentity paramCellIdentity)
    {
      if (paramCellIdentity == null) {
        return null;
      }
      Object localObject = null;
      switch (cellInfoType)
      {
      default: 
        break;
      case 5: 
        if (cellIdentityTdscdma.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_2.CellIdentityTdscdma)cellIdentityTdscdma.get(0);
          localObject = new android.telephony.CellIdentityTdscdma(base.mcc, base.mnc, base.lac, base.cid, base.cpid, operatorNames.alphaLong, operatorNames.alphaShort);
        }
        break;
      case 4: 
        if (cellIdentityWcdma.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_2.CellIdentityWcdma)cellIdentityWcdma.get(0);
          localObject = new android.telephony.CellIdentityWcdma(base.lac, base.cid, base.psc, base.uarfcn, base.mcc, base.mnc, operatorNames.alphaLong, operatorNames.alphaShort);
        }
        break;
      case 3: 
        if (cellIdentityLte.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_2.CellIdentityLte)cellIdentityLte.get(0);
          localObject = new android.telephony.CellIdentityLte(base.ci, base.pci, base.tac, base.earfcn, bandwidth, base.mcc, base.mnc, operatorNames.alphaLong, operatorNames.alphaShort);
        }
        break;
      case 2: 
        if (cellIdentityCdma.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_2.CellIdentityCdma)cellIdentityCdma.get(0);
          localObject = new android.telephony.CellIdentityCdma(base.networkId, base.systemId, base.baseStationId, base.longitude, base.latitude, operatorNames.alphaLong, operatorNames.alphaShort);
        }
        break;
      case 1: 
        if (cellIdentityGsm.size() == 1)
        {
          paramCellIdentity = (android.hardware.radio.V1_2.CellIdentityGsm)cellIdentityGsm.get(0);
          localObject = new android.telephony.CellIdentityGsm(base.lac, base.cid, base.arfcn, base.bsic, base.mcc, base.mnc, operatorNames.alphaLong, operatorNames.alphaShort);
        }
        break;
      }
      return localObject;
    }
    
    private NetworkRegistrationState createRegistrationStateFromDataRegState(Object paramObject)
    {
      int i;
      int j;
      int k;
      boolean bool;
      int m;
      if ((paramObject instanceof android.hardware.radio.V1_0.DataRegStateResult))
      {
        paramObject = (android.hardware.radio.V1_0.DataRegStateResult)paramObject;
        i = getRegStateFromHalRegState(regState);
        j = getAccessNetworkTechnologyFromRat(rat);
        k = reasonDataDenied;
        bool = isEmergencyOnly(regState);
        m = maxDataCalls;
        return new NetworkRegistrationState(1, 2, i, j, k, bool, getAvailableServices(i, 2, bool), convertHalCellIdentityToCellIdentity(cellIdentity), m);
      }
      if ((paramObject instanceof android.hardware.radio.V1_2.DataRegStateResult))
      {
        paramObject = (android.hardware.radio.V1_2.DataRegStateResult)paramObject;
        k = getRegStateFromHalRegState(regState);
        m = getAccessNetworkTechnologyFromRat(rat);
        j = reasonDataDenied;
        bool = isEmergencyOnly(regState);
        i = maxDataCalls;
        return new NetworkRegistrationState(1, 2, k, m, j, bool, getAvailableServices(k, 2, bool), convertHalCellIdentityToCellIdentity(cellIdentity), i);
      }
      return null;
    }
    
    private NetworkRegistrationState createRegistrationStateFromVoiceRegState(Object paramObject)
    {
      int i;
      int j;
      int k;
      boolean bool1;
      boolean bool2;
      int m;
      int n;
      int i1;
      if ((paramObject instanceof android.hardware.radio.V1_0.VoiceRegStateResult))
      {
        paramObject = (android.hardware.radio.V1_0.VoiceRegStateResult)paramObject;
        i = getRegStateFromHalRegState(regState);
        j = getAccessNetworkTechnologyFromRat(rat);
        k = reasonForDenial;
        bool1 = isEmergencyOnly(regState);
        bool2 = cssSupported;
        m = roamingIndicator;
        n = systemIsInPrl;
        i1 = defaultRoamingIndicator;
        return new NetworkRegistrationState(1, 1, i, j, k, bool1, getAvailableServices(i, 1, bool1), convertHalCellIdentityToCellIdentity(cellIdentity), bool2, m, n, i1);
      }
      if ((paramObject instanceof android.hardware.radio.V1_2.VoiceRegStateResult))
      {
        paramObject = (android.hardware.radio.V1_2.VoiceRegStateResult)paramObject;
        i = getRegStateFromHalRegState(regState);
        n = getAccessNetworkTechnologyFromRat(rat);
        k = reasonForDenial;
        bool2 = isEmergencyOnly(regState);
        bool1 = cssSupported;
        m = roamingIndicator;
        i1 = systemIsInPrl;
        j = defaultRoamingIndicator;
        return new NetworkRegistrationState(1, 1, i, n, k, bool2, getAvailableServices(i, 1, bool2), convertHalCellIdentityToCellIdentity(cellIdentity), bool1, m, i1, j);
      }
      return null;
    }
    
    private int getAccessNetworkTechnologyFromRat(int paramInt)
    {
      return ServiceState.rilRadioTechnologyToNetworkType(paramInt);
    }
    
    private int[] getAvailableServices(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      Object localObject1 = null;
      Object localObject2;
      if (paramBoolean)
      {
        localObject2 = new int[] { 5 };
      }
      else if (paramInt1 != 5)
      {
        localObject2 = localObject1;
        if (paramInt1 != 1) {}
      }
      else if (paramInt2 == 2)
      {
        localObject2 = new int[] { 2 };
      }
      else
      {
        localObject2 = localObject1;
        if (paramInt2 == 1) {
          localObject2 = new int[] { 1, 3, 4 };
        }
      }
      return localObject2;
    }
    
    private int getRegStateFromHalRegState(int paramInt)
    {
      if (paramInt != 10) {
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            return 0;
          }
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
      }
      return 0;
    }
    
    private NetworkRegistrationState getRegistrationStateFromResult(Object paramObject, int paramInt)
    {
      if (paramObject == null) {
        return null;
      }
      if (paramInt == 1) {
        return createRegistrationStateFromVoiceRegState(paramObject);
      }
      if (paramInt == 2) {
        return createRegistrationStateFromDataRegState(paramObject);
      }
      return null;
    }
    
    private boolean isEmergencyOnly(int paramInt)
    {
      switch (paramInt)
      {
      case 11: 
      default: 
        return false;
      }
      return true;
    }
    
    public void getNetworkRegistrationState(int paramInt, NetworkServiceCallback paramNetworkServiceCallback)
    {
      Object localObject;
      if (paramInt == 1)
      {
        localObject = Message.obtain(mHandler, 1);
        mCallbackMap.put(localObject, paramNetworkServiceCallback);
        mPhone.mCi.getVoiceRegistrationState((Message)localObject);
      }
      else if (paramInt == 2)
      {
        localObject = Message.obtain(mHandler, 2);
        mCallbackMap.put(localObject, paramNetworkServiceCallback);
        mPhone.mCi.getDataRegistrationState((Message)localObject);
      }
      else
      {
        CellularNetworkService localCellularNetworkService = CellularNetworkService.this;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("getNetworkRegistrationState invalid domain ");
        ((StringBuilder)localObject).append(paramInt);
        localCellularNetworkService.loge(((StringBuilder)localObject).toString());
        paramNetworkServiceCallback.onGetNetworkRegistrationStateComplete(2, null);
      }
    }
    
    protected void onDestroy()
    {
      super.onDestroy();
      mCallbackMap.clear();
      mHandlerThread.quit();
      mPhone.mCi.unregisterForNetworkStateChanged(mHandler);
    }
  }
}
