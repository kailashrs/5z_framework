package com.android.internal.telephony.ims;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.telephony.ims.stub.ImsFeatureConfiguration.FeatureSlotPair;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImsResolver
  implements ImsServiceController.ImsServiceControllerCallbacks
{
  private static final int DELAY_DYNAMIC_QUERY_MS = 5000;
  private static final int HANDLER_ADD_PACKAGE = 0;
  private static final int HANDLER_CONFIG_CHANGED = 2;
  private static final int HANDLER_DYNAMIC_FEATURE_CHANGE = 4;
  private static final int HANDLER_OVERRIDE_IMS_SERVICE_CONFIG = 5;
  private static final int HANDLER_REMOVE_PACKAGE = 1;
  private static final int HANDLER_START_DYNAMIC_FEATURE_QUERY = 3;
  public static final String METADATA_EMERGENCY_MMTEL_FEATURE = "android.telephony.ims.EMERGENCY_MMTEL_FEATURE";
  public static final String METADATA_MMTEL_FEATURE = "android.telephony.ims.MMTEL_FEATURE";
  private static final String METADATA_OVERRIDE_PERM_CHECK = "override_bind_check";
  public static final String METADATA_RCS_FEATURE = "android.telephony.ims.RCS_FEATURE";
  private static final String TAG = "ImsResolver";
  private Map<ComponentName, ImsServiceController> mActiveControllers = new HashMap();
  private BroadcastReceiver mAppChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      paramAnonymousIntent = paramAnonymousIntent.getData().getSchemeSpecificPart();
      int i = paramAnonymousContext.hashCode();
      if (i != -810471698)
      {
        if (i != 172491798)
        {
          if (i != 525384130)
          {
            if ((i == 1544582882) && (paramAnonymousContext.equals("android.intent.action.PACKAGE_ADDED")))
            {
              i = 0;
              break label103;
            }
          }
          else if (paramAnonymousContext.equals("android.intent.action.PACKAGE_REMOVED"))
          {
            i = 3;
            break label103;
          }
        }
        else if (paramAnonymousContext.equals("android.intent.action.PACKAGE_CHANGED"))
        {
          i = 2;
          break label103;
        }
      }
      else if (paramAnonymousContext.equals("android.intent.action.PACKAGE_REPLACED"))
      {
        i = 1;
        break label103;
      }
      i = -1;
      switch (i)
      {
      default: 
        
      case 3: 
        mHandler.obtainMessage(1, paramAnonymousIntent).sendToTarget();
        break;
      case 0: 
      case 1: 
      case 2: 
        label103:
        mHandler.obtainMessage(0, paramAnonymousIntent).sendToTarget();
      }
    }
  };
  private List<SparseArray<ImsServiceController>> mBoundImsServicesByFeature;
  private final Object mBoundServicesLock = new Object();
  private final CarrierConfigManager mCarrierConfigManager;
  private String[] mCarrierServices;
  private BroadcastReceiver mConfigChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      int i = paramAnonymousIntent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
      if (i == -1)
      {
        Log.i("ImsResolver", "Received SIM change for invalid slot id.");
        return;
      }
      paramAnonymousContext = new StringBuilder();
      paramAnonymousContext.append("Received Carrier Config Changed for SlotId: ");
      paramAnonymousContext.append(i);
      Log.i("ImsResolver", paramAnonymousContext.toString());
      mHandler.obtainMessage(2, Integer.valueOf(i)).sendToTarget();
    }
  };
  private final Context mContext;
  private String mDeviceService;
  private ImsServiceFeatureQueryManager.Listener mDynamicQueryListener = new ImsServiceFeatureQueryManager.Listener()
  {
    public void onComplete(ComponentName paramAnonymousComponentName, Set<ImsFeatureConfiguration.FeatureSlotPair> paramAnonymousSet)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onComplete called for name: ");
      localStringBuilder.append(paramAnonymousComponentName);
      localStringBuilder.append("features:");
      localStringBuilder.append(ImsResolver.this.printFeatures(paramAnonymousSet));
      Log.d("ImsResolver", localStringBuilder.toString());
      ImsResolver.this.handleFeaturesChanged(paramAnonymousComponentName, paramAnonymousSet);
    }
    
    public void onError(ComponentName paramAnonymousComponentName)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onError: ");
      localStringBuilder.append(paramAnonymousComponentName);
      localStringBuilder.append("returned with an error result");
      Log.w("ImsResolver", localStringBuilder.toString());
      ImsResolver.this.scheduleQueryForFeatures(paramAnonymousComponentName, 5000);
    }
  };
  private ImsDynamicQueryManagerFactory mDynamicQueryManagerFactory = _..Lambda.WamP7BPq0j01TgYE3GvUqU3b_rs.INSTANCE;
  private ImsServiceFeatureQueryManager mFeatureQueryManager;
  private Handler mHandler = new Handler(Looper.getMainLooper(), new _..Lambda.ImsResolver.pNx4XUM9FmR6cV_MCAGiEt8F4pg(this));
  private ImsServiceControllerFactory mImsServiceControllerFactory = new ImsServiceControllerFactory()
  {
    public ImsServiceController create(Context paramAnonymousContext, ComponentName paramAnonymousComponentName, ImsServiceController.ImsServiceControllerCallbacks paramAnonymousImsServiceControllerCallbacks)
    {
      return new ImsServiceController(paramAnonymousContext, paramAnonymousComponentName, paramAnonymousImsServiceControllerCallbacks);
    }
    
    public String getServiceInterface()
    {
      return "android.telephony.ims.ImsService";
    }
  };
  private ImsServiceControllerFactory mImsServiceControllerFactoryCompat = new ImsServiceControllerFactory()
  {
    public ImsServiceController create(Context paramAnonymousContext, ComponentName paramAnonymousComponentName, ImsServiceController.ImsServiceControllerCallbacks paramAnonymousImsServiceControllerCallbacks)
    {
      return new ImsServiceControllerCompat(paramAnonymousContext, paramAnonymousComponentName, paramAnonymousImsServiceControllerCallbacks);
    }
    
    public String getServiceInterface()
    {
      return "android.telephony.ims.compat.ImsService";
    }
  };
  private ImsServiceControllerFactory mImsServiceControllerFactoryStaticBindingCompat = new ImsServiceControllerFactory()
  {
    public ImsServiceController create(Context paramAnonymousContext, ComponentName paramAnonymousComponentName, ImsServiceController.ImsServiceControllerCallbacks paramAnonymousImsServiceControllerCallbacks)
    {
      return new ImsServiceControllerStaticCompat(paramAnonymousContext, paramAnonymousComponentName, paramAnonymousImsServiceControllerCallbacks);
    }
    
    public String getServiceInterface()
    {
      return null;
    }
  };
  private Map<ComponentName, ImsServiceInfo> mInstalledServicesCache = new HashMap();
  private final boolean mIsDynamicBinding;
  private final int mNumSlots;
  private final ComponentName mStaticComponent;
  private SubscriptionManagerProxy mSubscriptionManagerProxy = new SubscriptionManagerProxy()
  {
    public int getSlotIndex(int paramAnonymousInt)
    {
      return SubscriptionManager.getSlotIndex(paramAnonymousInt);
    }
    
    public int getSubId(int paramAnonymousInt)
    {
      int[] arrayOfInt = SubscriptionManager.getSubId(paramAnonymousInt);
      if (arrayOfInt != null) {
        return arrayOfInt[0];
      }
      return -1;
    }
  };
  
  public ImsResolver(Context paramContext, String paramString, int paramInt, boolean paramBoolean)
  {
    mContext = paramContext;
    mDeviceService = paramString;
    mNumSlots = paramInt;
    mIsDynamicBinding = paramBoolean;
    mStaticComponent = new ComponentName(mContext, ImsResolver.class);
    if (!mIsDynamicBinding)
    {
      Log.i("ImsResolver", "ImsResolver initialized with static binding.");
      mDeviceService = mStaticComponent.getPackageName();
    }
    mCarrierConfigManager = ((CarrierConfigManager)mContext.getSystemService("carrier_config"));
    mCarrierServices = new String[paramInt];
    mBoundImsServicesByFeature = ((List)Stream.generate(_..Lambda.ImsResolver.WVd6ghNMbVDukmkxia3ZwNeZzEY.INSTANCE).limit(mNumSlots).collect(Collectors.toList()));
    if (mIsDynamicBinding)
    {
      paramString = new IntentFilter();
      paramString.addAction("android.intent.action.PACKAGE_CHANGED");
      paramString.addAction("android.intent.action.PACKAGE_REMOVED");
      paramString.addAction("android.intent.action.PACKAGE_ADDED");
      paramString.addDataScheme("package");
      paramContext.registerReceiverAsUser(mAppChangedReceiver, UserHandle.ALL, paramString, null, null);
      paramContext.registerReceiver(mConfigChangedReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    }
  }
  
  private void bindImsService(ImsServiceInfo paramImsServiceInfo)
  {
    if (paramImsServiceInfo == null) {
      return;
    }
    bindImsServiceWithFeatures(paramImsServiceInfo, calculateFeaturesToCreate(paramImsServiceInfo));
  }
  
  private void bindImsServiceWithFeatures(ImsServiceInfo paramImsServiceInfo, HashSet<ImsFeatureConfiguration.FeatureSlotPair> paramHashSet)
  {
    if (shouldFeaturesCauseBind(paramHashSet))
    {
      ImsServiceController localImsServiceController = getControllerByServiceInfo(mActiveControllers, paramImsServiceInfo);
      StringBuilder localStringBuilder;
      if (localImsServiceController != null)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("ImsService connection exists, updating features ");
        localStringBuilder.append(paramHashSet);
        Log.i("ImsResolver", localStringBuilder.toString());
        try
        {
          localImsServiceController.changeImsServiceFeatures(paramHashSet);
          paramHashSet = localImsServiceController;
        }
        catch (RemoteException paramHashSet)
        {
          for (;;)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("bindImsService: error=");
            localStringBuilder.append(paramHashSet.getMessage());
            Log.w("ImsResolver", localStringBuilder.toString());
          }
        }
      }
      else
      {
        localImsServiceController = controllerFactory.create(mContext, name, this);
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Binding ImsService: ");
        localStringBuilder.append(localImsServiceController.getComponentName());
        localStringBuilder.append(" with features: ");
        localStringBuilder.append(paramHashSet);
        Log.i("ImsResolver", localStringBuilder.toString());
        localImsServiceController.bind(paramHashSet);
        paramHashSet = localImsServiceController;
      }
      mActiveControllers.put(name, paramHashSet);
    }
  }
  
  private HashSet<ImsFeatureConfiguration.FeatureSlotPair> calculateFeaturesToCreate(ImsServiceInfo paramImsServiceInfo)
  {
    HashSet localHashSet1 = new HashSet();
    int i = getSlotForActiveCarrierService(paramImsServiceInfo);
    if (i != -1) {
      localHashSet1.addAll((Collection)paramImsServiceInfo.getSupportedFeatures().stream().filter(new _..Lambda.ImsResolver._jFhgP_NotuFSwzjQBXWuvls4x4(i)).collect(Collectors.toList()));
    } else if (isDeviceService(paramImsServiceInfo)) {
      for (i = 0; i < mNumSlots; i++)
      {
        ImsServiceInfo localImsServiceInfo = getImsServiceInfoFromCache(mCarrierServices[i]);
        if (localImsServiceInfo == null)
        {
          localHashSet1.addAll((Collection)paramImsServiceInfo.getSupportedFeatures().stream().filter(new _..Lambda.ImsResolver.VfY5To_kbbTJevLzywTg__S1JhA(i)).collect(Collectors.toList()));
        }
        else
        {
          HashSet localHashSet2 = new HashSet(paramImsServiceInfo.getSupportedFeatures());
          localHashSet2.removeAll(localImsServiceInfo.getSupportedFeatures());
          localHashSet1.addAll((Collection)localHashSet2.stream().filter(new _..Lambda.ImsResolver.kF808g2NWzNL8H1SwzDc1FxiQdQ(i)).collect(Collectors.toList()));
        }
      }
    }
    return localHashSet1;
  }
  
  private void carrierConfigChanged(int paramInt)
  {
    int i = mSubscriptionManagerProxy.getSubId(paramInt);
    PersistableBundle localPersistableBundle = mCarrierConfigManager.getConfigForSubId(i);
    if (localPersistableBundle != null) {
      maybeRebindService(paramInt, localPersistableBundle.getString("config_ims_package_override_string", null));
    } else {
      Log.w("ImsResolver", "carrierConfigChanged: CarrierConfig is null!");
    }
  }
  
  private void dynamicQueryComplete(ComponentName paramComponentName, Set<ImsFeatureConfiguration.FeatureSlotPair> paramSet)
  {
    ImsServiceInfo localImsServiceInfo = getImsServiceInfoFromCache(paramComponentName.getPackageName());
    if (localImsServiceInfo == null)
    {
      paramSet = new StringBuilder();
      paramSet.append("handleFeaturesChanged: Couldn't find cached info for name: ");
      paramSet.append(paramComponentName);
      Log.w("ImsResolver", paramSet.toString());
      return;
    }
    localImsServiceInfo.replaceFeatures(paramSet);
    if (isActiveCarrierService(localImsServiceInfo))
    {
      bindImsService(localImsServiceInfo);
      updateImsServiceFeatures(getImsServiceInfoFromCache(mDeviceService));
    }
    else if (isDeviceService(localImsServiceInfo))
    {
      bindImsService(localImsServiceInfo);
    }
  }
  
  private ImsServiceController getControllerByServiceInfo(Map<ComponentName, ImsServiceController> paramMap, ImsServiceInfo paramImsServiceInfo)
  {
    return (ImsServiceController)paramMap.values().stream().filter(new _..Lambda.ImsResolver.aWLlEvfonhYSfDR8cVsM6A5pmqI(paramImsServiceInfo)).findFirst().orElse(null);
  }
  
  private SparseArray<ImsServiceController> getImsServiceControllers(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mNumSlots)) {
      synchronized (mBoundServicesLock)
      {
        SparseArray localSparseArray = (SparseArray)mBoundImsServicesByFeature.get(paramInt);
        if (localSparseArray == null) {
          return null;
        }
        return localSparseArray;
      }
    }
    return null;
  }
  
  private List<ImsServiceInfo> getImsServiceInfo(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    if (!mIsDynamicBinding)
    {
      localArrayList.addAll(getStaticImsService());
    }
    else
    {
      localArrayList.addAll(searchForImsServices(paramString, mImsServiceControllerFactory));
      localArrayList.addAll(searchForImsServices(paramString, mImsServiceControllerFactoryCompat));
    }
    return localArrayList;
  }
  
  private ImsServiceInfo getInfoByComponentName(Map<ComponentName, ImsServiceInfo> paramMap, ComponentName paramComponentName)
  {
    return (ImsServiceInfo)paramMap.get(paramComponentName);
  }
  
  private ImsServiceInfo getInfoByPackageName(Map<ComponentName, ImsServiceInfo> paramMap, String paramString)
  {
    return (ImsServiceInfo)paramMap.values().stream().filter(new _..Lambda.ImsResolver.rPjfocpARQ2sab24iic4o3kTTgw(paramString)).findFirst().orElse(null);
  }
  
  private int getSlotForActiveCarrierService(ImsServiceInfo paramImsServiceInfo)
  {
    for (int i = 0; i < mNumSlots; i++) {
      if (TextUtils.equals(mCarrierServices[i], name.getPackageName())) {
        return i;
      }
    }
    return -1;
  }
  
  private List<ImsServiceInfo> getStaticImsService()
  {
    ArrayList localArrayList = new ArrayList();
    ImsServiceInfo localImsServiceInfo = new ImsServiceInfo(mNumSlots);
    name = mStaticComponent;
    controllerFactory = mImsServiceControllerFactoryStaticBindingCompat;
    localImsServiceInfo.addFeatureForAllSlots(0);
    localImsServiceInfo.addFeatureForAllSlots(1);
    localArrayList.add(localImsServiceInfo);
    return localArrayList;
  }
  
  private void handleFeaturesChanged(ComponentName paramComponentName, Set<ImsFeatureConfiguration.FeatureSlotPair> paramSet)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramComponentName;
    arg2 = paramSet;
    mHandler.obtainMessage(4, localSomeArgs).sendToTarget();
  }
  
  private boolean isActiveCarrierService(ImsServiceInfo paramImsServiceInfo)
  {
    for (int i = 0; i < mNumSlots; i++) {
      if (TextUtils.equals(mCarrierServices[i], name.getPackageName())) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isDeviceService(ImsServiceInfo paramImsServiceInfo)
  {
    return TextUtils.equals(mDeviceService, name.getPackageName());
  }
  
  private void maybeAddedImsService(String paramString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("maybeAddedImsService, packageName: ");
    ((StringBuilder)localObject1).append(paramString);
    Log.d("ImsResolver", ((StringBuilder)localObject1).toString());
    localObject1 = getImsServiceInfo(paramString);
    paramString = new ArrayList();
    Iterator localIterator = ((List)localObject1).iterator();
    while (localIterator.hasNext())
    {
      localObject1 = (ImsServiceInfo)localIterator.next();
      Object localObject2 = getInfoByComponentName(mInstalledServicesCache, name);
      if (localObject2 != null)
      {
        if (featureFromMetadata)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Updating features in cached ImsService: ");
          localStringBuilder.append(name);
          Log.i("ImsResolver", localStringBuilder.toString());
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Updating features - Old features: ");
          localStringBuilder.append(localObject2);
          localStringBuilder.append(" new features: ");
          localStringBuilder.append(localObject1);
          Log.d("ImsResolver", localStringBuilder.toString());
          ((ImsServiceInfo)localObject2).replaceFeatures(((ImsServiceInfo)localObject1).getSupportedFeatures());
          updateImsServiceFeatures((ImsServiceInfo)localObject1);
        }
        else
        {
          scheduleQueryForFeatures((ImsServiceInfo)localObject1);
        }
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Adding newly added ImsService to cache: ");
        ((StringBuilder)localObject2).append(name);
        Log.i("ImsResolver", ((StringBuilder)localObject2).toString());
        mInstalledServicesCache.put(name, localObject1);
        if (featureFromMetadata) {
          paramString.add(localObject1);
        } else {
          scheduleQueryForFeatures((ImsServiceInfo)localObject1);
        }
      }
    }
    paramString = paramString.iterator();
    while (paramString.hasNext())
    {
      localObject1 = (ImsServiceInfo)paramString.next();
      if (isActiveCarrierService((ImsServiceInfo)localObject1))
      {
        bindImsService((ImsServiceInfo)localObject1);
        updateImsServiceFeatures(getImsServiceInfoFromCache(mDeviceService));
      }
      else if (isDeviceService((ImsServiceInfo)localObject1))
      {
        bindImsService((ImsServiceInfo)localObject1);
      }
    }
  }
  
  private void maybeRebindService(int paramInt, String paramString)
  {
    if (paramInt <= -1) {
      for (paramInt = 0; paramInt < mNumSlots; paramInt++) {
        updateBoundCarrierServices(paramInt, paramString);
      }
    }
    updateBoundCarrierServices(paramInt, paramString);
  }
  
  private boolean maybeRemovedImsService(String paramString)
  {
    paramString = getInfoByPackageName(mInstalledServicesCache, paramString);
    if (paramString != null)
    {
      mInstalledServicesCache.remove(name);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Removing ImsService: ");
      localStringBuilder.append(name);
      Log.i("ImsResolver", localStringBuilder.toString());
      unbindImsService(paramString);
      updateImsServiceFeatures(getImsServiceInfoFromCache(mDeviceService));
      return true;
    }
    return false;
  }
  
  private String printFeatures(Set<ImsFeatureConfiguration.FeatureSlotPair> paramSet)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("features: [");
    if (paramSet != null)
    {
      Iterator localIterator = paramSet.iterator();
      while (localIterator.hasNext())
      {
        paramSet = (ImsFeatureConfiguration.FeatureSlotPair)localIterator.next();
        localStringBuilder.append("{");
        localStringBuilder.append(slotId);
        localStringBuilder.append(",");
        localStringBuilder.append(featureType);
        localStringBuilder.append("} ");
      }
      localStringBuilder.append("]");
    }
    return localStringBuilder.toString();
  }
  
  private void putImsController(int paramInt1, int paramInt2, ImsServiceController paramImsServiceController)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mNumSlots) && (paramInt2 > -1) && (paramInt2 < 3)) {
      synchronized (mBoundServicesLock)
      {
        Object localObject2 = (SparseArray)mBoundImsServicesByFeature.get(paramInt1);
        Object localObject3 = localObject2;
        if (localObject2 == null)
        {
          localObject3 = new android/util/SparseArray;
          ((SparseArray)localObject3).<init>();
          mBoundImsServicesByFeature.add(paramInt1, localObject3);
        }
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("ImsServiceController added on slot: ");
        ((StringBuilder)localObject2).append(paramInt1);
        ((StringBuilder)localObject2).append(" with feature: ");
        ((StringBuilder)localObject2).append(paramInt2);
        ((StringBuilder)localObject2).append(" using package: ");
        ((StringBuilder)localObject2).append(paramImsServiceController.getComponentName());
        Log.i("ImsResolver", ((StringBuilder)localObject2).toString());
        ((SparseArray)localObject3).put(paramInt2, paramImsServiceController);
        return;
      }
    }
    paramImsServiceController = new StringBuilder();
    paramImsServiceController.append("putImsController received invalid parameters - slot: ");
    paramImsServiceController.append(paramInt1);
    paramImsServiceController.append(", feature: ");
    paramImsServiceController.append(paramInt2);
    Log.w("ImsResolver", paramImsServiceController.toString());
  }
  
  private ImsServiceController removeImsController(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mNumSlots) && (paramInt2 > -1) && (paramInt2 < 3)) {
      synchronized (mBoundServicesLock)
      {
        SparseArray localSparseArray = (SparseArray)mBoundImsServicesByFeature.get(paramInt1);
        if (localSparseArray == null) {
          return null;
        }
        ImsServiceController localImsServiceController = (ImsServiceController)localSparseArray.get(paramInt2, null);
        if (localImsServiceController != null)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("ImsServiceController removed on slot: ");
          localStringBuilder.append(paramInt1);
          localStringBuilder.append(" with feature: ");
          localStringBuilder.append(paramInt2);
          localStringBuilder.append(" using package: ");
          localStringBuilder.append(localImsServiceController.getComponentName());
          Log.i("ImsResolver", localStringBuilder.toString());
          localSparseArray.remove(paramInt2);
        }
        return localImsServiceController;
      }
    }
    ??? = new StringBuilder();
    ((StringBuilder)???).append("removeImsController received invalid parameters - slot: ");
    ((StringBuilder)???).append(paramInt1);
    ((StringBuilder)???).append(", feature: ");
    ((StringBuilder)???).append(paramInt2);
    Log.w("ImsResolver", ((StringBuilder)???).toString());
    return null;
  }
  
  private void scheduleQueryForFeatures(ComponentName paramComponentName, int paramInt)
  {
    Object localObject = getImsServiceInfoFromCache(paramComponentName.getPackageName());
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("scheduleQueryForFeatures: Couldn't find cached info for name: ");
      ((StringBuilder)localObject).append(paramComponentName);
      Log.w("ImsResolver", ((StringBuilder)localObject).toString());
      return;
    }
    scheduleQueryForFeatures((ImsServiceInfo)localObject, paramInt);
  }
  
  private void scheduleQueryForFeatures(ImsServiceInfo paramImsServiceInfo)
  {
    scheduleQueryForFeatures(paramImsServiceInfo, 0);
  }
  
  private void scheduleQueryForFeatures(ImsServiceInfo paramImsServiceInfo, int paramInt)
  {
    if ((!isDeviceService(paramImsServiceInfo)) && (getSlotForActiveCarrierService(paramImsServiceInfo) == -1))
    {
      Log.i("ImsResolver", "scheduleQueryForFeatures: skipping query for ImsService that is not set as carrier/device ImsService.");
      return;
    }
    Message localMessage = Message.obtain(mHandler, 3, paramImsServiceInfo);
    if (mHandler.hasMessages(3, paramImsServiceInfo))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("scheduleQueryForFeatures: dynamic query for ");
      localStringBuilder.append(name);
      localStringBuilder.append(" already scheduled");
      Log.d("ImsResolver", localStringBuilder.toString());
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("scheduleQueryForFeatures: starting dynamic query for ");
    localStringBuilder.append(name);
    localStringBuilder.append(" in ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("ms.");
    Log.d("ImsResolver", localStringBuilder.toString());
    mHandler.sendMessageDelayed(localMessage, paramInt);
  }
  
  private List<ImsServiceInfo> searchForImsServices(String paramString, ImsServiceControllerFactory paramImsServiceControllerFactory)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = new Intent(paramImsServiceControllerFactory.getServiceInterface());
    ((Intent)localObject).setPackage(paramString);
    localObject = mContext.getPackageManager().queryIntentServicesAsUser((Intent)localObject, 128, mContext.getUserId()).iterator();
    while (((Iterator)localObject).hasNext())
    {
      ServiceInfo localServiceInfo = nextserviceInfo;
      if (localServiceInfo != null)
      {
        paramString = new ImsServiceInfo(mNumSlots);
        name = new ComponentName(packageName, name);
        controllerFactory = paramImsServiceControllerFactory;
        if ((!isDeviceService(paramString)) && (mImsServiceControllerFactoryCompat != paramImsServiceControllerFactory))
        {
          featureFromMetadata = false;
        }
        else
        {
          if (metaData != null)
          {
            if (metaData.getBoolean("android.telephony.ims.EMERGENCY_MMTEL_FEATURE", false)) {
              paramString.addFeatureForAllSlots(0);
            }
            if (metaData.getBoolean("android.telephony.ims.MMTEL_FEATURE", false)) {
              paramString.addFeatureForAllSlots(1);
            }
            if (metaData.getBoolean("android.telephony.ims.RCS_FEATURE", false)) {
              paramString.addFeatureForAllSlots(2);
            }
          }
          if ((mImsServiceControllerFactoryCompat != paramImsServiceControllerFactory) && (paramString.getSupportedFeatures().isEmpty())) {
            featureFromMetadata = false;
          }
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("service name: ");
        localStringBuilder.append(name);
        localStringBuilder.append(", manifest query: ");
        localStringBuilder.append(featureFromMetadata);
        Log.i("ImsResolver", localStringBuilder.toString());
        if ((!TextUtils.equals(permission, "android.permission.BIND_IMS_SERVICE")) && (!metaData.getBoolean("override_bind_check", false)))
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("ImsService is not protected with BIND_IMS_SERVICE permission: ");
          localStringBuilder.append(name);
          Log.w("ImsResolver", localStringBuilder.toString());
        }
        else
        {
          localArrayList.add(paramString);
        }
      }
    }
    return localArrayList;
  }
  
  private boolean shouldFeaturesCauseBind(HashSet<ImsFeatureConfiguration.FeatureSlotPair> paramHashSet)
  {
    boolean bool;
    if (paramHashSet.stream().filter(_..Lambda.ImsResolver.SIkPixr_qGLIK_usUJIKu6S5BBs.INSTANCE).count() > 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void startDynamicQuery(ImsServiceInfo paramImsServiceInfo)
  {
    if (!mFeatureQueryManager.startQuery(name, controllerFactory.getServiceInterface()))
    {
      Log.w("ImsResolver", "startDynamicQuery: service could not connect. Retrying after delay.");
      scheduleQueryForFeatures(paramImsServiceInfo, 5000);
    }
    else
    {
      Log.d("ImsResolver", "startDynamicQuery: Service queried, waiting for response.");
    }
  }
  
  private void unbindImsService(ImsServiceInfo paramImsServiceInfo)
  {
    if (paramImsServiceInfo == null) {
      return;
    }
    ImsServiceController localImsServiceController = getControllerByServiceInfo(mActiveControllers, paramImsServiceInfo);
    if (localImsServiceController != null)
    {
      try
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Unbinding ImsService: ");
        localStringBuilder.append(localImsServiceController.getComponentName());
        Log.i("ImsResolver", localStringBuilder.toString());
        localImsServiceController.unbind();
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("unbindImsService: Remote Exception: ");
        localStringBuilder.append(localRemoteException.getMessage());
        Log.e("ImsResolver", localStringBuilder.toString());
      }
      mActiveControllers.remove(name);
    }
  }
  
  private void updateBoundCarrierServices(int paramInt, String paramString)
  {
    if ((paramInt > -1) && (paramInt < mNumSlots))
    {
      String str = mCarrierServices[paramInt];
      mCarrierServices[paramInt] = paramString;
      if (!TextUtils.equals(paramString, str))
      {
        Log.i("ImsResolver", "Carrier Config updated, binding new ImsService");
        unbindImsService(getImsServiceInfoFromCache(str));
        paramString = getImsServiceInfoFromCache(paramString);
        if ((paramString != null) && (!featureFromMetadata))
        {
          scheduleQueryForFeatures(paramString);
        }
        else
        {
          bindImsService(paramString);
          updateImsServiceFeatures(getImsServiceInfoFromCache(mDeviceService));
        }
      }
    }
  }
  
  private void updateImsServiceFeatures(ImsServiceInfo paramImsServiceInfo)
  {
    if (paramImsServiceInfo == null) {
      return;
    }
    Object localObject1 = getControllerByServiceInfo(mActiveControllers, paramImsServiceInfo);
    Object localObject2 = calculateFeaturesToCreate(paramImsServiceInfo);
    if (shouldFeaturesCauseBind((HashSet)localObject2))
    {
      if (localObject1 != null)
      {
        try
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Updating features for ImsService: ");
          localStringBuilder.append(((ImsServiceController)localObject1).getComponentName());
          Log.i("ImsResolver", localStringBuilder.toString());
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Updating Features - New Features: ");
          localStringBuilder.append(localObject2);
          Log.d("ImsResolver", localStringBuilder.toString());
          ((ImsServiceController)localObject1).changeImsServiceFeatures((HashSet)localObject2);
        }
        catch (RemoteException paramImsServiceInfo)
        {
          break label186;
        }
      }
      else
      {
        Log.i("ImsResolver", "updateImsServiceFeatures: unbound with active features, rebinding");
        bindImsServiceWithFeatures(paramImsServiceInfo, (HashSet)localObject2);
      }
      if ((isActiveCarrierService(paramImsServiceInfo)) && (!TextUtils.equals(name.getPackageName(), mDeviceService)))
      {
        Log.i("ImsResolver", "Updating device default");
        updateImsServiceFeatures(getImsServiceInfoFromCache(mDeviceService));
        return;
        label186:
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("updateImsServiceFeatures: Remote Exception: ");
        ((StringBuilder)localObject1).append(paramImsServiceInfo.getMessage());
        Log.e("ImsResolver", ((StringBuilder)localObject1).toString());
      }
    }
    else if (localObject1 != null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Unbinding: features = 0 for ImsService: ");
      ((StringBuilder)localObject2).append(((ImsServiceController)localObject1).getComponentName());
      Log.i("ImsResolver", ((StringBuilder)localObject2).toString());
      unbindImsService(paramImsServiceInfo);
    }
  }
  
  public void disableIms(int paramInt)
  {
    SparseArray localSparseArray = getImsServiceControllers(paramInt);
    if (localSparseArray != null) {
      for (int i = 0; i < localSparseArray.size(); i++) {
        ((ImsServiceController)localSparseArray.get(localSparseArray.keyAt(i))).disableIms(paramInt);
      }
    }
  }
  
  public void enableIms(int paramInt)
  {
    SparseArray localSparseArray = getImsServiceControllers(paramInt);
    if (localSparseArray != null) {
      for (int i = 0; i < localSparseArray.size(); i++) {
        ((ImsServiceController)localSparseArray.get(localSparseArray.keyAt(i))).enableIms(paramInt);
      }
    }
  }
  
  @VisibleForTesting
  public Handler getHandler()
  {
    return mHandler;
  }
  
  public IImsConfig getImsConfig(int paramInt1, int paramInt2)
    throws RemoteException
  {
    ImsServiceController localImsServiceController = getImsServiceController(paramInt1, paramInt2);
    if (localImsServiceController != null) {
      return localImsServiceController.getConfig(paramInt1);
    }
    return null;
  }
  
  public IImsRegistration getImsRegistration(int paramInt1, int paramInt2)
    throws RemoteException
  {
    ImsServiceController localImsServiceController = getImsServiceController(paramInt1, paramInt2);
    if (localImsServiceController != null) {
      return localImsServiceController.getRegistration(paramInt1);
    }
    return null;
  }
  
  public String getImsServiceConfiguration(int paramInt, boolean paramBoolean)
  {
    if ((paramInt >= 0) && (paramInt < mNumSlots))
    {
      String str;
      if (paramBoolean) {
        str = mCarrierServices[paramInt];
      } else {
        str = mDeviceService;
      }
      return str;
    }
    Log.w("ImsResolver", "getImsServiceConfiguration: invalid slotId!");
    return "";
  }
  
  @VisibleForTesting
  public ImsServiceController getImsServiceController(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mNumSlots)) {
      synchronized (mBoundServicesLock)
      {
        Object localObject2 = (SparseArray)mBoundImsServicesByFeature.get(paramInt1);
        if (localObject2 == null) {
          return null;
        }
        localObject2 = (ImsServiceController)((SparseArray)localObject2).get(paramInt2);
        return localObject2;
      }
    }
    return null;
  }
  
  @VisibleForTesting
  public ImsServiceController getImsServiceControllerAndListen(int paramInt1, int paramInt2, IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
  {
    ImsServiceController localImsServiceController = getImsServiceController(paramInt1, paramInt2);
    if (localImsServiceController != null)
    {
      localImsServiceController.addImsServiceFeatureCallback(paramIImsServiceFeatureCallback);
      return localImsServiceController;
    }
    return null;
  }
  
  @VisibleForTesting
  public ImsServiceInfo getImsServiceInfoFromCache(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    paramString = getInfoByPackageName(mInstalledServicesCache, paramString);
    if (paramString != null) {
      return paramString;
    }
    return null;
  }
  
  public IImsMmTelFeature getMmTelFeatureAndListen(int paramInt, IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
  {
    paramIImsServiceFeatureCallback = getImsServiceControllerAndListen(paramInt, 1, paramIImsServiceFeatureCallback);
    if (paramIImsServiceFeatureCallback != null) {
      paramIImsServiceFeatureCallback = paramIImsServiceFeatureCallback.getMmTelFeature(paramInt);
    } else {
      paramIImsServiceFeatureCallback = null;
    }
    return paramIImsServiceFeatureCallback;
  }
  
  public IImsRcsFeature getRcsFeatureAndListen(int paramInt, IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
  {
    paramIImsServiceFeatureCallback = getImsServiceControllerAndListen(paramInt, 2, paramIImsServiceFeatureCallback);
    if (paramIImsServiceFeatureCallback != null) {
      paramIImsServiceFeatureCallback = paramIImsServiceFeatureCallback.getRcsFeature(paramInt);
    } else {
      paramIImsServiceFeatureCallback = null;
    }
    return paramIImsServiceFeatureCallback;
  }
  
  public void imsServiceFeatureCreated(int paramInt1, int paramInt2, ImsServiceController paramImsServiceController)
  {
    putImsController(paramInt1, paramInt2, paramImsServiceController);
  }
  
  public void imsServiceFeatureRemoved(int paramInt1, int paramInt2, ImsServiceController paramImsServiceController)
  {
    removeImsController(paramInt1, paramInt2);
  }
  
  public void imsServiceFeaturesChanged(ImsFeatureConfiguration paramImsFeatureConfiguration, ImsServiceController paramImsServiceController)
  {
    if ((paramImsServiceController != null) && (paramImsFeatureConfiguration != null))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("imsServiceFeaturesChanged: config=");
      localStringBuilder.append(paramImsFeatureConfiguration.getServiceFeatures());
      localStringBuilder.append(", ComponentName=");
      localStringBuilder.append(paramImsServiceController.getComponentName());
      Log.i("ImsResolver", localStringBuilder.toString());
      handleFeaturesChanged(paramImsServiceController.getComponentName(), paramImsFeatureConfiguration.getServiceFeatures());
      return;
    }
  }
  
  public void initPopulateCacheAndStartBind()
  {
    Log.i("ImsResolver", "Initializing cache and binding.");
    mFeatureQueryManager = mDynamicQueryManagerFactory.create(mContext, mDynamicQueryListener);
    mHandler.obtainMessage(2, Integer.valueOf(-1)).sendToTarget();
    mHandler.obtainMessage(0, null).sendToTarget();
  }
  
  public boolean isResolvingBinding()
  {
    boolean bool;
    if ((!mHandler.hasMessages(3)) && (!mHandler.hasMessages(4)) && (!mFeatureQueryManager.isQueryInProgress())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean overrideImsServiceConfiguration(int paramInt, boolean paramBoolean, String paramString)
  {
    if ((paramInt >= 0) && (paramInt < mNumSlots))
    {
      if (paramString == null)
      {
        Log.w("ImsResolver", "overrideImsServiceConfiguration: null packageName!");
        return false;
      }
      Message.obtain(mHandler, 5, paramInt, paramBoolean, paramString).sendToTarget();
      return true;
    }
    Log.w("ImsResolver", "overrideImsServiceConfiguration: invalid slotId!");
    return false;
  }
  
  @VisibleForTesting
  public void setImsDynamicQueryManagerFactory(ImsDynamicQueryManagerFactory paramImsDynamicQueryManagerFactory)
  {
    mDynamicQueryManagerFactory = paramImsDynamicQueryManagerFactory;
  }
  
  @VisibleForTesting
  public void setImsServiceControllerFactory(ImsServiceControllerFactory paramImsServiceControllerFactory)
  {
    mImsServiceControllerFactory = paramImsServiceControllerFactory;
  }
  
  @VisibleForTesting
  public void setSubscriptionManagerProxy(SubscriptionManagerProxy paramSubscriptionManagerProxy)
  {
    mSubscriptionManagerProxy = paramSubscriptionManagerProxy;
  }
  
  @VisibleForTesting
  public static abstract interface ImsDynamicQueryManagerFactory
  {
    public abstract ImsServiceFeatureQueryManager create(Context paramContext, ImsServiceFeatureQueryManager.Listener paramListener);
  }
  
  @VisibleForTesting
  public static abstract interface ImsServiceControllerFactory
  {
    public abstract ImsServiceController create(Context paramContext, ComponentName paramComponentName, ImsServiceController.ImsServiceControllerCallbacks paramImsServiceControllerCallbacks);
    
    public abstract String getServiceInterface();
  }
  
  @VisibleForTesting
  public static class ImsServiceInfo
  {
    public ImsResolver.ImsServiceControllerFactory controllerFactory;
    public boolean featureFromMetadata = true;
    private final int mNumSlots;
    private final HashSet<ImsFeatureConfiguration.FeatureSlotPair> mSupportedFeatures;
    public ComponentName name;
    
    public ImsServiceInfo(int paramInt)
    {
      mNumSlots = paramInt;
      mSupportedFeatures = new HashSet();
    }
    
    void addFeatureForAllSlots(int paramInt)
    {
      for (int i = 0; i < mNumSlots; i++) {
        mSupportedFeatures.add(new ImsFeatureConfiguration.FeatureSlotPair(i, paramInt));
      }
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (ImsServiceInfo)paramObject;
        if (name != null ? !name.equals(name) : name != null) {
          return false;
        }
        if (!mSupportedFeatures.equals(mSupportedFeatures)) {
          return false;
        }
        if (controllerFactory != null) {
          bool = controllerFactory.equals(controllerFactory);
        } else if (controllerFactory != null) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    @VisibleForTesting
    public HashSet<ImsFeatureConfiguration.FeatureSlotPair> getSupportedFeatures()
    {
      return mSupportedFeatures;
    }
    
    public int hashCode()
    {
      ComponentName localComponentName = name;
      int i = 0;
      int j;
      if (localComponentName != null) {
        j = name.hashCode();
      } else {
        j = 0;
      }
      if (controllerFactory != null) {
        i = controllerFactory.hashCode();
      }
      return 31 * j + i;
    }
    
    void replaceFeatures(Set<ImsFeatureConfiguration.FeatureSlotPair> paramSet)
    {
      mSupportedFeatures.clear();
      mSupportedFeatures.addAll(paramSet);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[ImsServiceInfo] name=");
      localStringBuilder.append(name);
      localStringBuilder.append(", supportedFeatures=[ ");
      Iterator localIterator = mSupportedFeatures.iterator();
      while (localIterator.hasNext())
      {
        ImsFeatureConfiguration.FeatureSlotPair localFeatureSlotPair = (ImsFeatureConfiguration.FeatureSlotPair)localIterator.next();
        localStringBuilder.append("(");
        localStringBuilder.append(slotId);
        localStringBuilder.append(",");
        localStringBuilder.append(featureType);
        localStringBuilder.append(") ");
      }
      return localStringBuilder.toString();
    }
  }
  
  @VisibleForTesting
  public static abstract interface SubscriptionManagerProxy
  {
    public abstract int getSlotIndex(int paramInt);
    
    public abstract int getSubId(int paramInt);
  }
}
