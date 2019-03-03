package com.android.internal.telephony.ims;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.ims.ImsService.Listener;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsServiceController;
import android.telephony.ims.aidl.IImsServiceController.Stub;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.telephony.ims.stub.ImsFeatureConfiguration.FeatureSlotPair;
import android.util.Log;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.ims.internal.IImsFeatureStatusCallback.Stub;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.ExponentialBackoff;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ImsServiceController
{
  private static final String LOG_TAG = "ImsServiceController";
  private static final int REBIND_MAXIMUM_DELAY_MS = 60000;
  private static final int REBIND_START_DELAY_MS = 2000;
  private ExponentialBackoff mBackoff;
  private ImsServiceControllerCallbacks mCallbacks;
  private final ComponentName mComponentName;
  protected final Context mContext;
  private ImsService.Listener mFeatureChangedListener = new ImsService.Listener()
  {
    public void onUpdateSupportedImsFeatures(ImsFeatureConfiguration paramAnonymousImsFeatureConfiguration)
    {
      if (mCallbacks == null) {
        return;
      }
      mCallbacks.imsServiceFeaturesChanged(paramAnonymousImsFeatureConfiguration, ImsServiceController.this);
    }
  };
  private Set<ImsFeatureStatusCallback> mFeatureStatusCallbacks = new HashSet();
  private final HandlerThread mHandlerThread = new HandlerThread("ImsServiceControllerHandler");
  private IImsServiceController mIImsServiceController;
  private ImsDeathRecipient mImsDeathRecipient;
  private HashSet<ImsFeatureContainer> mImsFeatureBinders = new HashSet();
  private HashSet<ImsFeatureConfiguration.FeatureSlotPair> mImsFeatures;
  private ImsServiceConnection mImsServiceConnection;
  private IBinder mImsServiceControllerBinder;
  private Set<IImsServiceFeatureCallback> mImsStatusCallbacks = ConcurrentHashMap.newKeySet();
  private boolean mIsBinding = false;
  private boolean mIsBound = false;
  protected final Object mLock = new Object();
  private final IPackageManager mPackageManager;
  private RebindRetry mRebindRetry = new RebindRetry()
  {
    public long getMaximumDelay()
    {
      return 60000L;
    }
    
    public long getStartDelay()
    {
      return 2000L;
    }
  };
  private Runnable mRestartImsServiceRunnable = new Runnable()
  {
    public void run()
    {
      synchronized (mLock)
      {
        if (mIsBound) {
          return;
        }
        bind(mImsFeatures);
        return;
      }
    }
  };
  
  public ImsServiceController(Context paramContext, ComponentName paramComponentName, ImsServiceControllerCallbacks paramImsServiceControllerCallbacks)
  {
    mContext = paramContext;
    mComponentName = paramComponentName;
    mCallbacks = paramImsServiceControllerCallbacks;
    mHandlerThread.start();
    mBackoff = new ExponentialBackoff(mRebindRetry.getStartDelay(), mRebindRetry.getMaximumDelay(), 2, mHandlerThread.getLooper(), mRestartImsServiceRunnable);
    mPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
  }
  
  @VisibleForTesting
  public ImsServiceController(Context paramContext, ComponentName paramComponentName, ImsServiceControllerCallbacks paramImsServiceControllerCallbacks, Handler paramHandler, RebindRetry paramRebindRetry)
  {
    mContext = paramContext;
    mComponentName = paramComponentName;
    mCallbacks = paramImsServiceControllerCallbacks;
    mBackoff = new ExponentialBackoff(paramRebindRetry.getStartDelay(), paramRebindRetry.getMaximumDelay(), 2, paramHandler, mRestartImsServiceRunnable);
    mPackageManager = null;
  }
  
  private void addImsFeatureBinder(int paramInt1, int paramInt2, IInterface paramIInterface)
  {
    mImsFeatureBinders.add(new ImsFeatureContainer(paramInt1, paramInt2, paramIInterface));
  }
  
  private void addImsServiceFeature(ImsFeatureConfiguration.FeatureSlotPair paramFeatureSlotPair)
    throws RemoteException
  {
    if ((isServiceControllerAvailable()) && (mCallbacks != null))
    {
      Object localObject;
      if (featureType != 0)
      {
        localObject = new ImsFeatureStatusCallback(slotId, featureType);
        mFeatureStatusCallbacks.add(localObject);
        localObject = createImsFeature(slotId, featureType, ((ImsFeatureStatusCallback)localObject).getCallback());
        addImsFeatureBinder(slotId, featureType, (IInterface)localObject);
        mCallbacks.imsServiceFeatureCreated(slotId, featureType, this);
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("supports emergency calling on slot ");
        ((StringBuilder)localObject).append(slotId);
        Log.i("ImsServiceController", ((StringBuilder)localObject).toString());
      }
      sendImsFeatureCreatedCallback(slotId, featureType);
      return;
    }
    Log.w("ImsServiceController", "addImsServiceFeature called with null values.");
  }
  
  private void cleanUpService()
  {
    synchronized (mLock)
    {
      mImsDeathRecipient = null;
      mImsServiceConnection = null;
      mImsServiceControllerBinder = null;
      setServiceController(null);
      return;
    }
  }
  
  private ImsFeatureContainer getImsFeatureContainer(int paramInt1, int paramInt2)
  {
    return (ImsFeatureContainer)mImsFeatureBinders.stream().filter(new _..Lambda.ImsServiceController.w3xbtqEhKr7IY81qFuw0e94p84Y(paramInt1, paramInt2)).findFirst().orElse(null);
  }
  
  private void grantPermissionsToService()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Granting Runtime permissions to:");
    ((StringBuilder)localObject).append(getComponentName());
    Log.i("ImsServiceController", ((StringBuilder)localObject).toString());
    String str = mComponentName.getPackageName();
    try
    {
      if (mPackageManager != null)
      {
        localObject = mPackageManager;
        int i = mContext.getUserId();
        ((IPackageManager)localObject).grantDefaultPermissionsToEnabledImsServices(new String[] { str }, i);
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("ImsServiceController", "Unable to grant permissions, binder died.");
    }
  }
  
  private void notifyAllFeaturesRemoved()
  {
    if (mCallbacks == null)
    {
      Log.w("ImsServiceController", "notifyAllFeaturesRemoved called with invalid callbacks.");
      return;
    }
    synchronized (mLock)
    {
      Iterator localIterator = mImsFeatures.iterator();
      while (localIterator.hasNext())
      {
        ImsFeatureConfiguration.FeatureSlotPair localFeatureSlotPair = (ImsFeatureConfiguration.FeatureSlotPair)localIterator.next();
        if (featureType != 0) {
          mCallbacks.imsServiceFeatureRemoved(slotId, featureType, this);
        }
        sendImsFeatureRemovedCallback(slotId, featureType);
      }
      return;
    }
  }
  
  private void removeImsFeatureBinder(int paramInt1, int paramInt2)
  {
    ImsFeatureContainer localImsFeatureContainer = (ImsFeatureContainer)mImsFeatureBinders.stream().filter(new _..Lambda.ImsServiceController.rO36xbdAp6IQ5hFqLNNXDJPMers(paramInt1, paramInt2)).findFirst().orElse(null);
    if (localImsFeatureContainer != null) {
      mImsFeatureBinders.remove(localImsFeatureContainer);
    }
  }
  
  private void removeImsServiceFeature(ImsFeatureConfiguration.FeatureSlotPair paramFeatureSlotPair)
    throws RemoteException
  {
    if ((isServiceControllerAvailable()) && (mCallbacks != null))
    {
      Object localObject2;
      if (featureType != 0)
      {
        Object localObject1 = mFeatureStatusCallbacks.stream().filter(new _..Lambda.ImsServiceController.8NvoVXkZRS5LCradATGpNMBXAqg(paramFeatureSlotPair)).findFirst();
        localObject2 = null;
        localObject1 = (ImsFeatureStatusCallback)((Optional)localObject1).orElse(null);
        if (localObject1 != null) {
          mFeatureStatusCallbacks.remove(localObject1);
        }
        int i = slotId;
        int j = featureType;
        if (localObject1 != null) {
          localObject2 = ((ImsFeatureStatusCallback)localObject1).getCallback();
        }
        removeImsFeature(i, j, (IImsFeatureStatusCallback)localObject2);
        removeImsFeatureBinder(slotId, featureType);
        mCallbacks.imsServiceFeatureRemoved(slotId, featureType, this);
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("doesn't support emergency calling on slot ");
        ((StringBuilder)localObject2).append(slotId);
        Log.i("ImsServiceController", ((StringBuilder)localObject2).toString());
      }
      sendImsFeatureRemovedCallback(slotId, featureType);
      return;
    }
    Log.w("ImsServiceController", "removeImsServiceFeature called with null values.");
  }
  
  private void sendImsFeatureCreatedCallback(int paramInt1, int paramInt2)
  {
    Iterator localIterator = mImsStatusCallbacks.iterator();
    while (localIterator.hasNext())
    {
      IImsServiceFeatureCallback localIImsServiceFeatureCallback = (IImsServiceFeatureCallback)localIterator.next();
      try
      {
        localIImsServiceFeatureCallback.imsFeatureCreated(paramInt1, paramInt2);
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("sendImsFeatureCreatedCallback: Binder died, removing callback. Exception:");
        localStringBuilder.append(localRemoteException.getMessage());
        Log.w("ImsServiceController", localStringBuilder.toString());
        localIterator.remove();
      }
    }
  }
  
  private void sendImsFeatureRemovedCallback(int paramInt1, int paramInt2)
  {
    Iterator localIterator = mImsStatusCallbacks.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (IImsServiceFeatureCallback)localIterator.next();
      try
      {
        ((IImsServiceFeatureCallback)localObject).imsFeatureRemoved(paramInt1, paramInt2);
      }
      catch (RemoteException localRemoteException)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("sendImsFeatureRemovedCallback: Binder died, removing callback. Exception:");
        ((StringBuilder)localObject).append(localRemoteException.getMessage());
        Log.w("ImsServiceController", ((StringBuilder)localObject).toString());
        localIterator.remove();
      }
    }
  }
  
  private void sendImsFeatureStatusChanged(int paramInt1, int paramInt2, int paramInt3)
  {
    Iterator localIterator = mImsStatusCallbacks.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (IImsServiceFeatureCallback)localIterator.next();
      try
      {
        ((IImsServiceFeatureCallback)localObject).imsStatusChanged(paramInt1, paramInt2, paramInt3);
      }
      catch (RemoteException localRemoteException)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("sendImsFeatureStatusChanged: Binder died, removing callback. Exception:");
        ((StringBuilder)localObject).append(localRemoteException.getMessage());
        Log.w("ImsServiceController", ((StringBuilder)localObject).toString());
        localIterator.remove();
      }
    }
  }
  
  private void startDelayedRebindToService()
  {
    mBackoff.start();
  }
  
  public void addImsServiceFeatureCallback(IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
  {
    mImsStatusCallbacks.add(paramIImsServiceFeatureCallback);
    synchronized (mLock)
    {
      if (mImsFeatures != null)
      {
        boolean bool = mImsFeatures.isEmpty();
        if (!bool)
        {
          try
          {
            Iterator localIterator = mImsFeatures.iterator();
            while (localIterator.hasNext())
            {
              ImsFeatureConfiguration.FeatureSlotPair localFeatureSlotPair = (ImsFeatureConfiguration.FeatureSlotPair)localIterator.next();
              paramIImsServiceFeatureCallback.imsFeatureCreated(slotId, featureType);
            }
          }
          catch (RemoteException paramIImsServiceFeatureCallback)
          {
            Log.w("ImsServiceController", "addImsServiceFeatureCallback: exception notifying callback");
          }
          return;
        }
      }
      return;
    }
  }
  
  public boolean bind(HashSet<ImsFeatureConfiguration.FeatureSlotPair> paramHashSet)
  {
    synchronized (mLock)
    {
      if ((!mIsBound) && (!mIsBinding))
      {
        mIsBinding = true;
        mImsFeatures = paramHashSet;
        grantPermissionsToService();
        paramHashSet = new android/content/Intent;
        paramHashSet.<init>(getServiceInterface());
        paramHashSet = paramHashSet.setComponent(mComponentName);
        Object localObject2 = new com/android/internal/telephony/ims/ImsServiceController$ImsServiceConnection;
        ((ImsServiceConnection)localObject2).<init>(this);
        mImsServiceConnection = ((ImsServiceConnection)localObject2);
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Binding ImsService:");
        ((StringBuilder)localObject2).append(mComponentName);
        Log.i("ImsServiceController", ((StringBuilder)localObject2).toString());
        try
        {
          boolean bool = startBindToService(paramHashSet, mImsServiceConnection, 67108929);
          if (!bool) {
            mBackoff.notifyFailed();
          }
          return bool;
        }
        catch (Exception paramHashSet)
        {
          mBackoff.notifyFailed();
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Error binding (");
          ((StringBuilder)localObject2).append(mComponentName);
          ((StringBuilder)localObject2).append(") with exception: ");
          ((StringBuilder)localObject2).append(paramHashSet.getMessage());
          ((StringBuilder)localObject2).append(", rebinding in ");
          ((StringBuilder)localObject2).append(mBackoff.getCurrentDelay());
          ((StringBuilder)localObject2).append(" ms");
          Log.e("ImsServiceController", ((StringBuilder)localObject2).toString());
          return false;
        }
      }
      return false;
    }
  }
  
  public void changeImsServiceFeatures(HashSet<ImsFeatureConfiguration.FeatureSlotPair> paramHashSet)
    throws RemoteException
  {
    synchronized (mLock)
    {
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Features changed (");
      ((StringBuilder)localObject2).append(mImsFeatures);
      ((StringBuilder)localObject2).append("->");
      ((StringBuilder)localObject2).append(paramHashSet);
      ((StringBuilder)localObject2).append(") for ImsService: ");
      ((StringBuilder)localObject2).append(mComponentName);
      Log.i("ImsServiceController", ((StringBuilder)localObject2).toString());
      localObject2 = new java/util/HashSet;
      ((HashSet)localObject2).<init>(mImsFeatures);
      mImsFeatures = paramHashSet;
      if (mIsBound)
      {
        paramHashSet = new java/util/HashSet;
        paramHashSet.<init>(mImsFeatures);
        paramHashSet.removeAll((Collection)localObject2);
        paramHashSet = paramHashSet.iterator();
        while (paramHashSet.hasNext()) {
          addImsServiceFeature((ImsFeatureConfiguration.FeatureSlotPair)paramHashSet.next());
        }
        paramHashSet = new java/util/HashSet;
        paramHashSet.<init>((Collection)localObject2);
        paramHashSet.removeAll(mImsFeatures);
        paramHashSet = paramHashSet.iterator();
        while (paramHashSet.hasNext()) {
          removeImsServiceFeature((ImsFeatureConfiguration.FeatureSlotPair)paramHashSet.next());
        }
      }
      return;
    }
  }
  
  protected IInterface createImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException
  {
    switch (paramInt2)
    {
    default: 
      return null;
    case 2: 
      return mIImsServiceController.createRcsFeature(paramInt1, paramIImsFeatureStatusCallback);
    }
    return mIImsServiceController.createMmTelFeature(paramInt1, paramIImsFeatureStatusCallback);
  }
  
  public void disableIms(int paramInt)
  {
    try
    {
      synchronized (mLock)
      {
        if (isServiceControllerAvailable()) {
          mIImsServiceController.disableIms(paramInt);
        }
      }
      return;
    }
    catch (RemoteException localRemoteException)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Couldn't disable IMS: ");
      ((StringBuilder)???).append(localRemoteException.getMessage());
      Log.w("ImsServiceController", ((StringBuilder)???).toString());
    }
  }
  
  public void enableIms(int paramInt)
  {
    try
    {
      synchronized (mLock)
      {
        if (isServiceControllerAvailable()) {
          mIImsServiceController.enableIms(paramInt);
        }
      }
      StringBuilder localStringBuilder;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't enable IMS: ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("ImsServiceController", localStringBuilder.toString());
    }
  }
  
  public ComponentName getComponentName()
  {
    return mComponentName;
  }
  
  public IImsConfig getConfig(int paramInt)
    throws RemoteException
  {
    synchronized (mLock)
    {
      IImsConfig localIImsConfig;
      if (isServiceControllerAvailable()) {
        localIImsConfig = mIImsServiceController.getConfig(paramInt);
      } else {
        localIImsConfig = null;
      }
      return localIImsConfig;
    }
  }
  
  @VisibleForTesting
  public IImsServiceController getImsServiceController()
  {
    return mIImsServiceController;
  }
  
  @VisibleForTesting
  public IBinder getImsServiceControllerBinder()
  {
    return mImsServiceControllerBinder;
  }
  
  public IImsMmTelFeature getMmTelFeature(int paramInt)
  {
    synchronized (mLock)
    {
      Object localObject2 = getImsFeatureContainer(paramInt, 1);
      if (localObject2 == null)
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Requested null MMTelFeature on slot ");
        ((StringBuilder)localObject2).append(paramInt);
        Log.w("ImsServiceController", ((StringBuilder)localObject2).toString());
        return null;
      }
      localObject2 = (IImsMmTelFeature)((ImsFeatureContainer)localObject2).resolve(IImsMmTelFeature.class);
      return localObject2;
    }
  }
  
  public IImsRcsFeature getRcsFeature(int paramInt)
  {
    synchronized (mLock)
    {
      Object localObject2 = getImsFeatureContainer(paramInt, 2);
      if (localObject2 == null)
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Requested null RcsFeature on slot ");
        ((StringBuilder)localObject2).append(paramInt);
        Log.w("ImsServiceController", ((StringBuilder)localObject2).toString());
        return null;
      }
      localObject2 = (IImsRcsFeature)((ImsFeatureContainer)localObject2).resolve(IImsRcsFeature.class);
      return localObject2;
    }
  }
  
  @VisibleForTesting
  public long getRebindDelay()
  {
    return mBackoff.getCurrentDelay();
  }
  
  public IImsRegistration getRegistration(int paramInt)
    throws RemoteException
  {
    synchronized (mLock)
    {
      IImsRegistration localIImsRegistration;
      if (isServiceControllerAvailable()) {
        localIImsRegistration = mIImsServiceController.getRegistration(paramInt);
      } else {
        localIImsRegistration = null;
      }
      return localIImsRegistration;
    }
  }
  
  protected String getServiceInterface()
  {
    return "android.telephony.ims.ImsService";
  }
  
  public boolean isBound()
  {
    synchronized (mLock)
    {
      boolean bool = mIsBound;
      return bool;
    }
  }
  
  protected boolean isServiceControllerAvailable()
  {
    boolean bool;
    if (mIImsServiceController != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void notifyImsServiceReady()
    throws RemoteException
  {
    synchronized (mLock)
    {
      if (isServiceControllerAvailable())
      {
        Log.d("ImsServiceController", "notifyImsServiceReady");
        mIImsServiceController.setListener(mFeatureChangedListener);
        mIImsServiceController.notifyImsServiceReadyForFeatureCreation();
      }
      return;
    }
  }
  
  protected void removeImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException
  {
    mIImsServiceController.removeImsFeature(paramInt1, paramInt2, paramIImsFeatureStatusCallback);
  }
  
  @VisibleForTesting
  public void removeImsServiceFeatureCallbacks()
  {
    mImsStatusCallbacks.clear();
  }
  
  protected void setServiceController(IBinder paramIBinder)
  {
    mIImsServiceController = IImsServiceController.Stub.asInterface(paramIBinder);
  }
  
  protected boolean startBindToService(Intent paramIntent, ImsServiceConnection paramImsServiceConnection, int paramInt)
  {
    return mContext.bindService(paramIntent, paramImsServiceConnection, paramInt);
  }
  
  public void unbind()
    throws RemoteException
  {
    synchronized (mLock)
    {
      mBackoff.stop();
      if ((mImsServiceConnection != null) && (mImsDeathRecipient != null))
      {
        Object localObject2 = new java/util/HashSet;
        ((HashSet)localObject2).<init>();
        changeImsServiceFeatures((HashSet)localObject2);
        removeImsServiceFeatureCallbacks();
        mImsServiceControllerBinder.unlinkToDeath(mImsDeathRecipient, 0);
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Unbinding ImsService: ");
        ((StringBuilder)localObject2).append(mComponentName);
        Log.i("ImsServiceController", ((StringBuilder)localObject2).toString());
        mContext.unbindService(mImsServiceConnection);
        cleanUpService();
        return;
      }
      return;
    }
  }
  
  class ImsDeathRecipient
    implements IBinder.DeathRecipient
  {
    private ComponentName mComponentName;
    
    ImsDeathRecipient(ComponentName paramComponentName)
    {
      mComponentName = paramComponentName;
    }
    
    public void binderDied()
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("ImsService(");
      ((StringBuilder)???).append(mComponentName);
      ((StringBuilder)???).append(") died. Restarting...");
      Log.e("ImsServiceController", ((StringBuilder)???).toString());
      synchronized (mLock)
      {
        ImsServiceController.access$002(ImsServiceController.this, false);
        ImsServiceController.access$102(ImsServiceController.this, false);
        ImsServiceController.this.notifyAllFeaturesRemoved();
        ImsServiceController.this.cleanUpService();
        ImsServiceController.this.startDelayedRebindToService();
        return;
      }
    }
  }
  
  private class ImsFeatureContainer
  {
    public int featureType;
    private IInterface mBinder;
    public int slotId;
    
    ImsFeatureContainer(int paramInt1, int paramInt2, IInterface paramIInterface)
    {
      slotId = paramInt1;
      featureType = paramInt2;
      mBinder = paramIInterface;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (ImsFeatureContainer)paramObject;
        if (slotId != slotId) {
          return false;
        }
        if (featureType != featureType) {
          return false;
        }
        if (mBinder != null) {
          bool = mBinder.equals(mBinder);
        } else if (mBinder != null) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public int hashCode()
    {
      int i = slotId;
      int j = featureType;
      int k;
      if (mBinder != null) {
        k = mBinder.hashCode();
      } else {
        k = 0;
      }
      return 31 * (31 * i + j) + k;
    }
    
    public <T extends IInterface> T resolve(Class<T> paramClass)
    {
      return (IInterface)paramClass.cast(mBinder);
    }
  }
  
  private class ImsFeatureStatusCallback
  {
    private final IImsFeatureStatusCallback mCallback = new IImsFeatureStatusCallback.Stub()
    {
      public void notifyImsFeatureStatus(int paramAnonymousInt)
        throws RemoteException
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("notifyImsFeatureStatus: slot=");
        localStringBuilder.append(mSlotId);
        localStringBuilder.append(", feature=");
        localStringBuilder.append(mFeatureType);
        localStringBuilder.append(", status=");
        localStringBuilder.append(paramAnonymousInt);
        Log.i("ImsServiceController", localStringBuilder.toString());
        ImsServiceController.this.sendImsFeatureStatusChanged(mSlotId, mFeatureType, paramAnonymousInt);
      }
    };
    private int mFeatureType;
    private int mSlotId;
    
    ImsFeatureStatusCallback(int paramInt1, int paramInt2)
    {
      mSlotId = paramInt1;
      mFeatureType = paramInt2;
    }
    
    public IImsFeatureStatusCallback getCallback()
    {
      return mCallback;
    }
  }
  
  class ImsServiceConnection
    implements ServiceConnection
  {
    ImsServiceConnection() {}
    
    private void cleanupConnection()
    {
      if (isServiceControllerAvailable()) {
        mImsServiceControllerBinder.unlinkToDeath(mImsDeathRecipient, 0);
      }
      ImsServiceController.this.notifyAllFeaturesRemoved();
      ImsServiceController.this.cleanUpService();
    }
    
    public void onBindingDied(ComponentName paramComponentName)
    {
      synchronized (mLock)
      {
        ImsServiceController.access$002(ImsServiceController.this, false);
        ImsServiceController.access$102(ImsServiceController.this, false);
        cleanupConnection();
        ??? = new StringBuilder();
        ((StringBuilder)???).append("ImsService(");
        ((StringBuilder)???).append(paramComponentName);
        ((StringBuilder)???).append("): onBindingDied. Starting rebind...");
        Log.w("ImsServiceController", ((StringBuilder)???).toString());
        ImsServiceController.this.startDelayedRebindToService();
        return;
      }
    }
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      mBackoff.stop();
      synchronized (mLock)
      {
        ImsServiceController.access$102(ImsServiceController.this, true);
        ImsServiceController.access$002(ImsServiceController.this, false);
        Object localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("ImsService(");
        ((StringBuilder)localObject2).append(paramComponentName);
        ((StringBuilder)localObject2).append("): onServiceConnected with binder: ");
        ((StringBuilder)localObject2).append(paramIBinder);
        Log.d("ImsServiceController", ((StringBuilder)localObject2).toString());
        if (paramIBinder != null)
        {
          ImsServiceController localImsServiceController = ImsServiceController.this;
          localObject2 = new com/android/internal/telephony/ims/ImsServiceController$ImsDeathRecipient;
          ((ImsServiceController.ImsDeathRecipient)localObject2).<init>(ImsServiceController.this, paramComponentName);
          ImsServiceController.access$602(localImsServiceController, (ImsServiceController.ImsDeathRecipient)localObject2);
          try
          {
            paramIBinder.linkToDeath(mImsDeathRecipient, 0);
            ImsServiceController.access$702(ImsServiceController.this, paramIBinder);
            setServiceController(paramIBinder);
            notifyImsServiceReady();
            localObject2 = mImsFeatures.iterator();
            while (((Iterator)localObject2).hasNext())
            {
              paramIBinder = (ImsFeatureConfiguration.FeatureSlotPair)((Iterator)localObject2).next();
              ImsServiceController.this.addImsServiceFeature(paramIBinder);
            }
          }
          catch (RemoteException paramIBinder)
          {
            ImsServiceController.access$102(ImsServiceController.this, false);
            ImsServiceController.access$002(ImsServiceController.this, false);
            if (mImsDeathRecipient != null) {
              mImsDeathRecipient.binderDied();
            }
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("ImsService(");
            ((StringBuilder)localObject2).append(paramComponentName);
            ((StringBuilder)localObject2).append(") RemoteException:");
            ((StringBuilder)localObject2).append(paramIBinder.getMessage());
            Log.e("ImsServiceController", ((StringBuilder)localObject2).toString());
          }
        }
        return;
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      synchronized (mLock)
      {
        ImsServiceController.access$002(ImsServiceController.this, false);
        cleanupConnection();
        ??? = new StringBuilder();
        ((StringBuilder)???).append("ImsService(");
        ((StringBuilder)???).append(paramComponentName);
        ((StringBuilder)???).append("): onServiceDisconnected. Waiting...");
        Log.w("ImsServiceController", ((StringBuilder)???).toString());
        return;
      }
    }
  }
  
  public static abstract interface ImsServiceControllerCallbacks
  {
    public abstract void imsServiceFeatureCreated(int paramInt1, int paramInt2, ImsServiceController paramImsServiceController);
    
    public abstract void imsServiceFeatureRemoved(int paramInt1, int paramInt2, ImsServiceController paramImsServiceController);
    
    public abstract void imsServiceFeaturesChanged(ImsFeatureConfiguration paramImsFeatureConfiguration, ImsServiceController paramImsServiceController);
  }
  
  @VisibleForTesting
  public static abstract interface RebindRetry
  {
    public abstract long getMaximumDelay();
    
    public abstract long getStartDelay();
  }
}
