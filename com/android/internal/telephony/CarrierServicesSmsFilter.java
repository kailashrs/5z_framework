package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.RemoteException;
import android.service.carrier.ICarrierMessagingCallback.Stub;
import android.service.carrier.ICarrierMessagingService;
import android.service.carrier.MessagePdu;
import android.telephony.CarrierMessagingServiceManager;
import android.telephony.Rlog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CarrierServicesSmsFilter
{
  protected static final boolean DBG = true;
  private final CarrierServicesSmsFilterCallbackInterface mCarrierServicesSmsFilterCallback;
  private final Context mContext;
  private final int mDestPort;
  private final String mLogTag;
  private final String mPduFormat;
  private final byte[][] mPdus;
  private final Phone mPhone;
  
  @VisibleForTesting
  public CarrierServicesSmsFilter(Context paramContext, Phone paramPhone, byte[][] paramArrayOfByte, int paramInt, String paramString1, CarrierServicesSmsFilterCallbackInterface paramCarrierServicesSmsFilterCallbackInterface, String paramString2)
  {
    mContext = paramContext;
    mPhone = paramPhone;
    mPdus = paramArrayOfByte;
    mDestPort = paramInt;
    mPduFormat = paramString1;
    mCarrierServicesSmsFilterCallback = paramCarrierServicesSmsFilterCallbackInterface;
    mLogTag = paramString2;
  }
  
  private void filterWithPackage(String paramString, FilterAggregator paramFilterAggregator)
  {
    CarrierSmsFilter localCarrierSmsFilter = new CarrierSmsFilter(mPdus, mDestPort, mPduFormat);
    localCarrierSmsFilter.filterSms(paramString, new CarrierSmsFilterCallback(paramFilterAggregator, localCarrierSmsFilter));
  }
  
  private Optional<String> getCarrierAppPackageForFiltering()
  {
    List localList = null;
    Object localObject = UiccController.getInstance().getUiccCard(mPhone.getPhoneId());
    if (localObject != null) {
      localList = ((UiccCard)localObject).getCarrierPackageNamesForIntent(mContext.getPackageManager(), new Intent("android.service.carrier.CarrierMessagingService"));
    } else {
      Rlog.e(mLogTag, "UiccCard not initialized.");
    }
    if ((localList != null) && (localList.size() == 1))
    {
      log("Found carrier package.");
      return Optional.of((String)localList.get(0));
    }
    localObject = getSystemAppForIntent(new Intent("android.service.carrier.CarrierMessagingService"));
    if ((localObject != null) && (((List)localObject).size() == 1))
    {
      log("Found system package.");
      return Optional.of((String)((List)localObject).get(0));
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unable to find carrier package: ");
    localStringBuilder.append(localList);
    localStringBuilder.append(", nor systemPackages: ");
    localStringBuilder.append(localObject);
    logv(localStringBuilder.toString());
    return Optional.empty();
  }
  
  private List<String> getSystemAppForIntent(Intent paramIntent)
  {
    ArrayList localArrayList = new ArrayList();
    PackageManager localPackageManager = mContext.getPackageManager();
    paramIntent = localPackageManager.queryIntentServices(paramIntent, 0).iterator();
    while (paramIntent.hasNext())
    {
      Object localObject = (ResolveInfo)paramIntent.next();
      StringBuilder localStringBuilder;
      if (serviceInfo == null)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Can't get service information from ");
        localStringBuilder.append(localObject);
        loge(localStringBuilder.toString());
      }
      else
      {
        localObject = serviceInfo.packageName;
        if (localPackageManager.checkPermission("android.permission.CARRIER_FILTER_SMS", (String)localObject) == 0)
        {
          localArrayList.add(localObject);
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("getSystemAppForIntent: added package ");
          localStringBuilder.append((String)localObject);
          log(localStringBuilder.toString());
        }
      }
    }
    return localArrayList;
  }
  
  private void log(String paramString)
  {
    Rlog.d(mLogTag, paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e(mLogTag, paramString);
  }
  
  private void logv(String paramString)
  {
    Rlog.e(mLogTag, paramString);
  }
  
  @VisibleForTesting
  public boolean filter()
  {
    Object localObject = getCarrierAppPackageForFiltering();
    ArrayList localArrayList = new ArrayList();
    if (((Optional)localObject).isPresent()) {
      localArrayList.add((String)((Optional)localObject).get());
    }
    localObject = CarrierSmsUtils.getCarrierImsPackageForIntent(mContext, mPhone, new Intent("android.service.carrier.CarrierMessagingService"));
    if (localObject != null) {
      localArrayList.add(localObject);
    }
    FilterAggregator localFilterAggregator = new FilterAggregator(localArrayList.size());
    localObject = localArrayList.iterator();
    while (((Iterator)localObject).hasNext()) {
      filterWithPackage((String)((Iterator)localObject).next(), localFilterAggregator);
    }
    boolean bool;
    if (localArrayList.size() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @VisibleForTesting
  public static abstract interface CarrierServicesSmsFilterCallbackInterface
  {
    public abstract void onFilterComplete(int paramInt);
  }
  
  private final class CarrierSmsFilter
    extends CarrierMessagingServiceManager
  {
    private final int mDestPort;
    private final byte[][] mPdus;
    private volatile CarrierServicesSmsFilter.CarrierSmsFilterCallback mSmsFilterCallback;
    private final String mSmsFormat;
    
    CarrierSmsFilter(byte[][] paramArrayOfByte, int paramInt, String paramString)
    {
      mPdus = paramArrayOfByte;
      mDestPort = paramInt;
      mSmsFormat = paramString;
    }
    
    void filterSms(String paramString, CarrierServicesSmsFilter.CarrierSmsFilterCallback paramCarrierSmsFilterCallback)
    {
      mSmsFilterCallback = paramCarrierSmsFilterCallback;
      if (!bindToCarrierMessagingService(mContext, paramString))
      {
        CarrierServicesSmsFilter.this.loge("bindService() for carrier messaging service failed");
        paramCarrierSmsFilterCallback.onFilterComplete(0);
      }
      else
      {
        CarrierServicesSmsFilter.this.logv("bindService() for carrier messaging service succeeded");
      }
    }
    
    protected void onServiceReady(ICarrierMessagingService paramICarrierMessagingService)
    {
      try
      {
        localObject = new android/service/carrier/MessagePdu;
        ((MessagePdu)localObject).<init>(Arrays.asList(mPdus));
        paramICarrierMessagingService.filterSms((MessagePdu)localObject, mSmsFormat, mDestPort, mPhone.getSubId(), mSmsFilterCallback);
      }
      catch (RemoteException paramICarrierMessagingService)
      {
        CarrierServicesSmsFilter localCarrierServicesSmsFilter = CarrierServicesSmsFilter.this;
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Exception filtering the SMS: ");
        ((StringBuilder)localObject).append(paramICarrierMessagingService);
        localCarrierServicesSmsFilter.loge(((StringBuilder)localObject).toString());
        mSmsFilterCallback.onFilterComplete(0);
      }
    }
  }
  
  private final class CarrierSmsFilterCallback
    extends ICarrierMessagingCallback.Stub
  {
    private final CarrierMessagingServiceManager mCarrierMessagingServiceManager;
    private final CarrierServicesSmsFilter.FilterAggregator mFilterAggregator;
    
    CarrierSmsFilterCallback(CarrierServicesSmsFilter.FilterAggregator paramFilterAggregator, CarrierMessagingServiceManager paramCarrierMessagingServiceManager)
    {
      mFilterAggregator = paramFilterAggregator;
      mCarrierMessagingServiceManager = paramCarrierMessagingServiceManager;
    }
    
    public void onDownloadMmsComplete(int paramInt)
    {
      CarrierServicesSmsFilter localCarrierServicesSmsFilter = CarrierServicesSmsFilter.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected onDownloadMmsComplete call with result: ");
      localStringBuilder.append(paramInt);
      localCarrierServicesSmsFilter.loge(localStringBuilder.toString());
    }
    
    public void onFilterComplete(int paramInt)
    {
      mCarrierMessagingServiceManager.disposeConnection(mContext);
      mFilterAggregator.onFilterComplete(paramInt);
    }
    
    public void onSendMmsComplete(int paramInt, byte[] paramArrayOfByte)
    {
      CarrierServicesSmsFilter localCarrierServicesSmsFilter = CarrierServicesSmsFilter.this;
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unexpected onSendMmsComplete call with result: ");
      paramArrayOfByte.append(paramInt);
      localCarrierServicesSmsFilter.loge(paramArrayOfByte.toString());
    }
    
    public void onSendMultipartSmsComplete(int paramInt, int[] paramArrayOfInt)
    {
      CarrierServicesSmsFilter localCarrierServicesSmsFilter = CarrierServicesSmsFilter.this;
      paramArrayOfInt = new StringBuilder();
      paramArrayOfInt.append("Unexpected onSendMultipartSmsComplete call with result: ");
      paramArrayOfInt.append(paramInt);
      localCarrierServicesSmsFilter.loge(paramArrayOfInt.toString());
    }
    
    public void onSendSmsComplete(int paramInt1, int paramInt2)
    {
      CarrierServicesSmsFilter localCarrierServicesSmsFilter = CarrierServicesSmsFilter.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected onSendSmsComplete call with result: ");
      localStringBuilder.append(paramInt1);
      localCarrierServicesSmsFilter.loge(localStringBuilder.toString());
    }
  }
  
  private final class FilterAggregator
  {
    private final Object mFilterLock = new Object();
    private int mFilterResult;
    private int mNumPendingFilters;
    
    FilterAggregator(int paramInt)
    {
      mNumPendingFilters = paramInt;
      mFilterResult = 0;
    }
    
    private void combine(int paramInt)
    {
      mFilterResult |= paramInt;
    }
    
    void onFilterComplete(int paramInt)
    {
      synchronized (mFilterLock)
      {
        mNumPendingFilters -= 1;
        combine(paramInt);
        long l;
        if (mNumPendingFilters == 0) {
          l = Binder.clearCallingIdentity();
        }
        try
        {
          mCarrierServicesSmsFilterCallback.onFilterComplete(mFilterResult);
          Binder.restoreCallingIdentity(l);
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
        return;
      }
    }
  }
}
