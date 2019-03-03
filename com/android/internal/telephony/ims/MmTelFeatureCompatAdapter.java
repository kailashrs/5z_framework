package com.android.internal.telephony.ims;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.feature.CapabilityChangeRequest;
import android.telephony.ims.feature.CapabilityChangeRequest.CapabilityPair;
import android.telephony.ims.feature.ImsFeature.CapabilityCallbackProxy;
import android.telephony.ims.feature.MmTelFeature;
import android.telephony.ims.feature.MmTelFeature.MmTelCapabilities;
import android.util.Log;
import com.android.ims.ImsConfigListener;
import com.android.ims.ImsConfigListener.Stub;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsRegistrationListener.Stub;
import com.android.ims.internal.IImsUt;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MmTelFeatureCompatAdapter
  extends MmTelFeature
{
  public static final String ACTION_IMS_INCOMING_CALL = "com.android.ims.IMS_INCOMING_CALL";
  public static final int FEATURE_DISABLED = 0;
  public static final int FEATURE_ENABLED = 1;
  public static final int FEATURE_TYPE_UNKNOWN = -1;
  public static final int FEATURE_TYPE_UT_OVER_LTE = 4;
  public static final int FEATURE_TYPE_UT_OVER_WIFI = 5;
  public static final int FEATURE_TYPE_VIDEO_OVER_LTE = 1;
  public static final int FEATURE_TYPE_VIDEO_OVER_WIFI = 3;
  public static final int FEATURE_TYPE_VOICE_OVER_LTE = 0;
  public static final int FEATURE_TYPE_VOICE_OVER_WIFI = 2;
  public static final int FEATURE_UNKNOWN = -1;
  private static final Map<Integer, Integer> REG_TECH_TO_NET_TYPE = new HashMap(2);
  private static final String TAG = "MmTelFeatureCompat";
  private static final int WAIT_TIMEOUT_MS = 2000;
  private final MmTelInterfaceAdapter mCompatFeature;
  private final IImsRegistrationListener mListener = new IImsRegistrationListener.Stub()
  {
    public void registrationAssociatedUriChanged(Uri[] paramAnonymousArrayOfUri)
      throws RemoteException
    {}
    
    public void registrationChangeFailed(int paramAnonymousInt, ImsReasonInfo paramAnonymousImsReasonInfo)
      throws RemoteException
    {}
    
    public void registrationConnected()
      throws RemoteException
    {}
    
    public void registrationConnectedWithRadioTech(int paramAnonymousInt)
      throws RemoteException
    {}
    
    public void registrationDisconnected(ImsReasonInfo paramAnonymousImsReasonInfo)
      throws RemoteException
    {
      Log.i("MmTelFeatureCompat", "registrationDisconnected: resetting MMTEL capabilities.");
      notifyCapabilitiesStatusChanged(new MmTelFeature.MmTelCapabilities());
    }
    
    public void registrationFeatureCapabilityChanged(int paramAnonymousInt, int[] paramAnonymousArrayOfInt1, int[] paramAnonymousArrayOfInt2)
      throws RemoteException
    {
      notifyCapabilitiesStatusChanged(MmTelFeatureCompatAdapter.this.convertCapabilities(paramAnonymousArrayOfInt1));
    }
    
    public void registrationProgressing()
      throws RemoteException
    {}
    
    public void registrationProgressingWithRadioTech(int paramAnonymousInt)
      throws RemoteException
    {}
    
    public void registrationResumed()
      throws RemoteException
    {}
    
    public void registrationServiceCapabilityChanged(int paramAnonymousInt1, int paramAnonymousInt2)
      throws RemoteException
    {}
    
    public void registrationSuspended()
      throws RemoteException
    {}
    
    public void voiceMessageCountUpdate(int paramAnonymousInt)
      throws RemoteException
    {
      notifyVoiceMessageCountUpdate(paramAnonymousInt);
    }
  };
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      Log.i("MmTelFeatureCompat", "onReceive");
      if (paramAnonymousIntent.getAction().equals("com.android.ims.IMS_INCOMING_CALL"))
      {
        Log.i("MmTelFeatureCompat", "onReceive : incoming call intent.");
        paramAnonymousContext = paramAnonymousIntent.getStringExtra("android:imsCallID");
        try
        {
          paramAnonymousContext = mCompatFeature.getPendingCallSession(mSessionId, paramAnonymousContext);
          notifyIncomingCallSession(paramAnonymousContext, paramAnonymousIntent.getExtras());
        }
        catch (RemoteException paramAnonymousContext)
        {
          Log.w("MmTelFeatureCompat", "onReceive: Couldn't get Incoming call session.");
        }
      }
    }
  };
  private ImsRegistrationCompatAdapter mRegCompatAdapter;
  private int mSessionId = -1;
  
  static
  {
    REG_TECH_TO_NET_TYPE.put(Integer.valueOf(0), Integer.valueOf(13));
    REG_TECH_TO_NET_TYPE.put(Integer.valueOf(1), Integer.valueOf(18));
  }
  
  public MmTelFeatureCompatAdapter(Context paramContext, int paramInt, MmTelInterfaceAdapter paramMmTelInterfaceAdapter)
  {
    initialize(paramContext, paramInt);
    mCompatFeature = paramMmTelInterfaceAdapter;
  }
  
  private MmTelFeature.MmTelCapabilities convertCapabilities(int[] paramArrayOfInt)
  {
    Object localObject = new boolean[paramArrayOfInt.length];
    for (int i = 0; (i <= 5) && (i < paramArrayOfInt.length); i++) {
      if (paramArrayOfInt[i] == i) {
        localObject[i] = 1;
      } else if (paramArrayOfInt[i] == -1) {
        localObject[i] = 0;
      }
    }
    paramArrayOfInt = new MmTelFeature.MmTelCapabilities();
    if ((localObject[0] != 0) || (localObject[2] != 0)) {
      paramArrayOfInt.addCapabilities(1);
    }
    if ((localObject[1] != 0) || (localObject[3] != 0)) {
      paramArrayOfInt.addCapabilities(2);
    }
    if ((localObject[4] != 0) || (localObject[5] != 0)) {
      paramArrayOfInt.addCapabilities(4);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("convertCapabilities - capabilities: ");
    ((StringBuilder)localObject).append(paramArrayOfInt);
    Log.i("MmTelFeatureCompat", ((StringBuilder)localObject).toString());
    return paramArrayOfInt;
  }
  
  private int convertCapability(int paramInt1, int paramInt2)
  {
    int i = -1;
    int j;
    if (paramInt2 == 0)
    {
      if (paramInt1 != 4) {
        switch (paramInt1)
        {
        default: 
          j = i;
          break;
        case 2: 
          j = 1;
          break;
        case 1: 
          j = 0;
          break;
        }
      } else {
        j = 4;
      }
    }
    else
    {
      j = i;
      if (paramInt2 == 1) {
        if (paramInt1 != 4) {
          switch (paramInt1)
          {
          default: 
            j = i;
            break;
          case 2: 
            j = 3;
            break;
          case 1: 
            j = 2;
            break;
          }
        } else {
          j = 5;
        }
      }
    }
    return j;
  }
  
  private PendingIntent createIncomingCallPendingIntent()
  {
    Intent localIntent = new Intent("com.android.ims.IMS_INCOMING_CALL");
    localIntent.setPackage("com.android.phone");
    return PendingIntent.getBroadcast(mContext, 0, localIntent, 134217728);
  }
  
  public void addRegistrationAdapter(ImsRegistrationCompatAdapter paramImsRegistrationCompatAdapter)
    throws RemoteException
  {
    mRegCompatAdapter = paramImsRegistrationCompatAdapter;
  }
  
  public void changeEnabledCapabilities(CapabilityChangeRequest paramCapabilityChangeRequest, ImsFeature.CapabilityCallbackProxy paramCapabilityCallbackProxy)
  {
    if (paramCapabilityChangeRequest == null) {
      return;
    }
    try
    {
      IImsConfig localIImsConfig = mCompatFeature.getConfigInterface();
      Iterator localIterator = paramCapabilityChangeRequest.getCapabilitiesToDisable().iterator();
      Object localObject1;
      Object localObject2;
      int i;
      int j;
      while (localIterator.hasNext())
      {
        localObject1 = (CapabilityChangeRequest.CapabilityPair)localIterator.next();
        localObject2 = new java/util/concurrent/CountDownLatch;
        ((CountDownLatch)localObject2).<init>(1);
        i = convertCapability(((CapabilityChangeRequest.CapabilityPair)localObject1).getCapability(), ((CapabilityChangeRequest.CapabilityPair)localObject1).getRadioTech());
        j = ((Integer)REG_TECH_TO_NET_TYPE.getOrDefault(Integer.valueOf(((CapabilityChangeRequest.CapabilityPair)localObject1).getRadioTech()), Integer.valueOf(-1))).intValue();
        Object localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append("changeEnabledCapabilities - cap: ");
        ((StringBuilder)localObject3).append(i);
        ((StringBuilder)localObject3).append(" radioTech: ");
        ((StringBuilder)localObject3).append(j);
        ((StringBuilder)localObject3).append(" disabled");
        Log.i("MmTelFeatureCompat", ((StringBuilder)localObject3).toString());
        localObject3 = new com/android/internal/telephony/ims/MmTelFeatureCompatAdapter$4;
        ((4)localObject3).<init>(this, i, j, (CountDownLatch)localObject2, paramCapabilityCallbackProxy, (CapabilityChangeRequest.CapabilityPair)localObject1);
        localIImsConfig.setFeatureValue(i, j, 0, (ImsConfigListener)localObject3);
        ((CountDownLatch)localObject2).await(2000L, TimeUnit.MILLISECONDS);
      }
      localIterator = paramCapabilityChangeRequest.getCapabilitiesToEnable().iterator();
      while (localIterator.hasNext())
      {
        localObject2 = (CapabilityChangeRequest.CapabilityPair)localIterator.next();
        paramCapabilityChangeRequest = new java/util/concurrent/CountDownLatch;
        paramCapabilityChangeRequest.<init>(1);
        j = convertCapability(((CapabilityChangeRequest.CapabilityPair)localObject2).getCapability(), ((CapabilityChangeRequest.CapabilityPair)localObject2).getRadioTech());
        i = ((Integer)REG_TECH_TO_NET_TYPE.getOrDefault(Integer.valueOf(((CapabilityChangeRequest.CapabilityPair)localObject2).getRadioTech()), Integer.valueOf(-1))).intValue();
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("changeEnabledCapabilities - cap: ");
        ((StringBuilder)localObject1).append(j);
        ((StringBuilder)localObject1).append(" radioTech: ");
        ((StringBuilder)localObject1).append(i);
        ((StringBuilder)localObject1).append(" enabled");
        Log.i("MmTelFeatureCompat", ((StringBuilder)localObject1).toString());
        localObject1 = new com/android/internal/telephony/ims/MmTelFeatureCompatAdapter$5;
        ((5)localObject1).<init>(this, j, i, paramCapabilityChangeRequest, paramCapabilityCallbackProxy, (CapabilityChangeRequest.CapabilityPair)localObject2);
        localIImsConfig.setFeatureValue(j, i, 1, (ImsConfigListener)localObject1);
        paramCapabilityChangeRequest.await(2000L, TimeUnit.MILLISECONDS);
      }
    }
    catch (RemoteException|InterruptedException paramCapabilityCallbackProxy)
    {
      paramCapabilityChangeRequest = new StringBuilder();
      paramCapabilityChangeRequest.append("changeEnabledCapabilities: Error processing: ");
      paramCapabilityChangeRequest.append(paramCapabilityCallbackProxy.getMessage());
      Log.w("MmTelFeatureCompat", paramCapabilityChangeRequest.toString());
    }
  }
  
  public ImsCallProfile createCallProfile(int paramInt1, int paramInt2)
  {
    try
    {
      ImsCallProfile localImsCallProfile = mCompatFeature.createCallProfile(mSessionId, paramInt1, paramInt2);
      return localImsCallProfile;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException.getMessage());
    }
  }
  
  public IImsCallSession createCallSessionInterface(ImsCallProfile paramImsCallProfile)
    throws RemoteException
  {
    return mCompatFeature.createCallSession(mSessionId, paramImsCallProfile);
  }
  
  public void disableIms()
    throws RemoteException
  {
    mCompatFeature.turnOffIms();
  }
  
  public void enableIms()
    throws RemoteException
  {
    mCompatFeature.turnOnIms();
  }
  
  public IImsEcbm getEcbmInterface()
    throws RemoteException
  {
    return mCompatFeature.getEcbmInterface();
  }
  
  public int getFeatureState()
  {
    try
    {
      int i = mCompatFeature.getFeatureState();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException.getMessage());
    }
  }
  
  public IImsMultiEndpoint getMultiEndpointInterface()
    throws RemoteException
  {
    return mCompatFeature.getMultiEndpointInterface();
  }
  
  public IImsConfig getOldConfigInterface()
  {
    try
    {
      IImsConfig localIImsConfig = mCompatFeature.getConfigInterface();
      return localIImsConfig;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getOldConfigInterface(): ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("MmTelFeatureCompat", localStringBuilder.toString());
    }
    return null;
  }
  
  public IImsUt getUtInterface()
    throws RemoteException
  {
    return mCompatFeature.getUtInterface();
  }
  
  public void onFeatureReady()
  {
    Log.i("MmTelFeatureCompat", "onFeatureReady called!");
    Object localObject1 = new IntentFilter("com.android.ims.IMS_INCOMING_CALL");
    mContext.registerReceiver(mReceiver, (IntentFilter)localObject1);
    try
    {
      localObject2 = mCompatFeature;
      localObject1 = createIncomingCallPendingIntent();
      ImsRegistrationListenerBase localImsRegistrationListenerBase = new com/android/internal/telephony/ims/MmTelFeatureCompatAdapter$ImsRegistrationListenerBase;
      localImsRegistrationListenerBase.<init>(this, null);
      mSessionId = ((MmTelInterfaceAdapter)localObject2).startSession((PendingIntent)localObject1, localImsRegistrationListenerBase);
      mCompatFeature.addRegistrationListener(mListener);
      mCompatFeature.addRegistrationListener(mRegCompatAdapter.getRegistrationListener());
    }
    catch (RemoteException localRemoteException)
    {
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Couldn't start compat feature: ");
      ((StringBuilder)localObject2).append(localRemoteException.getMessage());
      Log.e("MmTelFeatureCompat", ((StringBuilder)localObject2).toString());
    }
  }
  
  public void onFeatureRemoved()
  {
    mContext.unregisterReceiver(mReceiver);
    try
    {
      mCompatFeature.endSession(mSessionId);
      mCompatFeature.removeRegistrationListener(mListener);
      if (mRegCompatAdapter != null) {
        mCompatFeature.removeRegistrationListener(mRegCompatAdapter.getRegistrationListener());
      }
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onFeatureRemoved: Couldn't end session: ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("MmTelFeatureCompat", localStringBuilder.toString());
    }
  }
  
  public boolean queryCapabilityConfiguration(int paramInt1, int paramInt2)
  {
    paramInt1 = convertCapability(paramInt1, paramInt2);
    Object localObject = new CountDownLatch(1);
    int[] arrayOfInt = new int[1];
    boolean bool = false;
    arrayOfInt[0] = -1;
    paramInt2 = ((Integer)REG_TECH_TO_NET_TYPE.getOrDefault(Integer.valueOf(paramInt2), Integer.valueOf(-1))).intValue();
    try
    {
      IImsConfig localIImsConfig = mCompatFeature.getConfigInterface();
      ConfigListener local3 = new com/android/internal/telephony/ims/MmTelFeatureCompatAdapter$3;
      local3.<init>(this, paramInt1, paramInt2, (CountDownLatch)localObject, arrayOfInt);
      localIImsConfig.getFeatureValue(paramInt1, paramInt2, local3);
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("MmTelFeatureCompat", "queryCapabilityConfiguration");
    }
    try
    {
      ((CountDownLatch)localObject).await(2000L, TimeUnit.MILLISECONDS);
    }
    catch (InterruptedException localInterruptedException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("queryCapabilityConfiguration - error waiting: ");
      ((StringBuilder)localObject).append(localInterruptedException.getMessage());
      Log.w("MmTelFeatureCompat", ((StringBuilder)localObject).toString());
    }
    if (arrayOfInt[0] == 1) {
      bool = true;
    }
    return bool;
  }
  
  public void setUiTtyMode(int paramInt, Message paramMessage)
  {
    try
    {
      mCompatFeature.setUiTTYMode(paramInt, paramMessage);
      return;
    }
    catch (RemoteException paramMessage)
    {
      throw new RuntimeException(paramMessage.getMessage());
    }
  }
  
  private static class ConfigListener
    extends ImsConfigListener.Stub
  {
    private final int mCapability;
    private final CountDownLatch mLatch;
    private final int mTech;
    
    public ConfigListener(int paramInt1, int paramInt2, CountDownLatch paramCountDownLatch)
    {
      mCapability = paramInt1;
      mTech = paramInt2;
      mLatch = paramCountDownLatch;
    }
    
    public void getFeatureValueReceived(int paramInt) {}
    
    public void onGetFeatureResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      throws RemoteException
    {
      if ((paramInt1 == mCapability) && (paramInt2 == mTech))
      {
        mLatch.countDown();
        getFeatureValueReceived(paramInt3);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onGetFeatureResponse: response different than requested: feature=");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" and network=");
        localStringBuilder.append(paramInt2);
        Log.i("MmTelFeatureCompat", localStringBuilder.toString());
      }
    }
    
    public void onGetVideoQuality(int paramInt1, int paramInt2)
      throws RemoteException
    {}
    
    public void onSetFeatureResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      throws RemoteException
    {
      if ((paramInt1 == mCapability) && (paramInt2 == mTech))
      {
        mLatch.countDown();
        setFeatureValueReceived(paramInt3);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onSetFeatureResponse: response different than requested: feature=");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" and network=");
        localStringBuilder.append(paramInt2);
        Log.i("MmTelFeatureCompat", localStringBuilder.toString());
      }
    }
    
    public void onSetVideoQuality(int paramInt)
      throws RemoteException
    {}
    
    public void setFeatureValueReceived(int paramInt) {}
  }
  
  private class ImsRegistrationListenerBase
    extends IImsRegistrationListener.Stub
  {
    private ImsRegistrationListenerBase() {}
    
    public void registrationAssociatedUriChanged(Uri[] paramArrayOfUri)
      throws RemoteException
    {}
    
    public void registrationChangeFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {}
    
    public void registrationConnected()
      throws RemoteException
    {}
    
    public void registrationConnectedWithRadioTech(int paramInt)
      throws RemoteException
    {}
    
    public void registrationDisconnected(ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {}
    
    public void registrationFeatureCapabilityChanged(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
      throws RemoteException
    {}
    
    public void registrationProgressing()
      throws RemoteException
    {}
    
    public void registrationProgressingWithRadioTech(int paramInt)
      throws RemoteException
    {}
    
    public void registrationResumed()
      throws RemoteException
    {}
    
    public void registrationServiceCapabilityChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {}
    
    public void registrationSuspended()
      throws RemoteException
    {}
    
    public void voiceMessageCountUpdate(int paramInt)
      throws RemoteException
    {}
  }
}
