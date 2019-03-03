package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.Arrays;
import java.util.stream.IntStream;

public class RatRatcheter
{
  private static final String LOG_TAG = "RilRatcheter";
  private BroadcastReceiver mConfigChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(paramAnonymousIntent.getAction())) {
        RatRatcheter.this.resetRatFamilyMap();
      }
    }
  };
  private boolean mDataRatchetEnabled = true;
  private final Phone mPhone;
  private final SparseArray<SparseIntArray> mRatFamilyMap = new SparseArray();
  private boolean mVoiceRatchetEnabled = true;
  
  public RatRatcheter(Phone paramPhone)
  {
    mPhone = paramPhone;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
    paramPhone.getContext().registerReceiverAsUser(mConfigChangedReceiver, UserHandle.ALL, localIntentFilter, null, null);
    resetRatFamilyMap();
  }
  
  private boolean isSameRatFamily(ServiceState paramServiceState1, ServiceState paramServiceState2)
  {
    synchronized (mRatFamilyMap)
    {
      if (paramServiceState1.getRilDataRadioTechnology() == paramServiceState2.getRilDataRadioTechnology()) {
        return true;
      }
      Object localObject = mRatFamilyMap.get(paramServiceState1.getRilDataRadioTechnology());
      boolean bool = false;
      if (localObject == null) {
        return false;
      }
      if (mRatFamilyMap.get(paramServiceState1.getRilDataRadioTechnology()) == mRatFamilyMap.get(paramServiceState2.getRilDataRadioTechnology())) {
        bool = true;
      }
      return bool;
    }
  }
  
  private int ratchetRat(int paramInt1, int paramInt2)
  {
    synchronized (mRatFamilyMap)
    {
      SparseIntArray localSparseIntArray1 = (SparseIntArray)mRatFamilyMap.get(paramInt1);
      if (localSparseIntArray1 == null) {
        return paramInt2;
      }
      SparseIntArray localSparseIntArray2 = (SparseIntArray)mRatFamilyMap.get(paramInt2);
      if (localSparseIntArray2 != localSparseIntArray1) {
        return paramInt2;
      }
      if (localSparseIntArray2.get(paramInt1, -1) <= localSparseIntArray2.get(paramInt2, -1)) {
        paramInt1 = paramInt2;
      }
      return paramInt1;
    }
  }
  
  private void resetRatFamilyMap()
  {
    synchronized (mRatFamilyMap)
    {
      mRatFamilyMap.clear();
      Object localObject1 = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
      if (localObject1 == null) {
        return;
      }
      localObject1 = ((CarrierConfigManager)localObject1).getConfig();
      if (localObject1 == null) {
        return;
      }
      localObject1 = ((PersistableBundle)localObject1).getStringArray("ratchet_rat_families");
      if (localObject1 == null) {
        return;
      }
      int i = localObject1.length;
      for (int j = 0; j < i; j++)
      {
        Object localObject3 = localObject1[j].split(",");
        if (localObject3.length >= 2)
        {
          SparseIntArray localSparseIntArray = new android/util/SparseIntArray;
          localSparseIntArray.<init>(localObject3.length);
          int k = localObject3.length;
          int m = 0;
          int n = 0;
          while (n < k)
          {
            String str = localObject3[n];
            try
            {
              int i1 = Integer.parseInt(str.trim());
              if (mRatFamilyMap.get(i1) != null)
              {
                localObject3 = new java/lang/StringBuilder;
                ((StringBuilder)localObject3).<init>();
                ((StringBuilder)localObject3).append("RAT listed twice: ");
                ((StringBuilder)localObject3).append(str);
                Rlog.e("RilRatcheter", ((StringBuilder)localObject3).toString());
              }
              else
              {
                localSparseIntArray.put(i1, m);
                mRatFamilyMap.put(i1, localSparseIntArray);
                n++;
                m++;
              }
            }
            catch (NumberFormatException localNumberFormatException)
            {
              StringBuilder localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("NumberFormatException on ");
              localStringBuilder.append(str);
              Rlog.e("RilRatcheter", localStringBuilder.toString());
            }
          }
        }
      }
      return;
    }
  }
  
  public static boolean updateBandwidths(int[] paramArrayOfInt, ServiceState paramServiceState)
  {
    if (paramArrayOfInt == null) {
      return false;
    }
    int i = Arrays.stream(paramServiceState.getCellBandwidths()).sum();
    if (Arrays.stream(paramArrayOfInt).sum() > i)
    {
      paramServiceState.setCellBandwidths(paramArrayOfInt);
      return true;
    }
    return false;
  }
  
  public void ratchet(ServiceState paramServiceState1, ServiceState paramServiceState2, boolean paramBoolean)
  {
    if ((!paramBoolean) && (isSameRatFamily(paramServiceState1, paramServiceState2))) {
      updateBandwidths(paramServiceState1.getCellBandwidths(), paramServiceState2);
    }
    boolean bool = false;
    if (paramBoolean)
    {
      mVoiceRatchetEnabled = false;
      mDataRatchetEnabled = false;
      return;
    }
    if (mVoiceRatchetEnabled) {
      paramServiceState2.setRilVoiceRadioTechnology(ratchetRat(paramServiceState1.getRilVoiceRadioTechnology(), paramServiceState2.getRilVoiceRadioTechnology()));
    } else if (paramServiceState1.getRilVoiceRadioTechnology() != paramServiceState2.getRilVoiceRadioTechnology()) {
      mVoiceRatchetEnabled = true;
    }
    if (mDataRatchetEnabled) {
      paramServiceState2.setRilDataRadioTechnology(ratchetRat(paramServiceState1.getRilDataRadioTechnology(), paramServiceState2.getRilDataRadioTechnology()));
    } else if (paramServiceState1.getRilDataRadioTechnology() != paramServiceState2.getRilDataRadioTechnology()) {
      mDataRatchetEnabled = true;
    }
    if ((!paramServiceState1.isUsingCarrierAggregation()) && (!paramServiceState2.isUsingCarrierAggregation()) && (paramServiceState2.getCellBandwidths().length <= 1)) {
      paramBoolean = bool;
    } else {
      paramBoolean = true;
    }
    paramServiceState2.setIsUsingCarrierAggregation(paramBoolean);
  }
}
