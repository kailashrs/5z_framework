package android.telephony.ims.feature;

import android.annotation.SystemApi;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRcsFeature.Stub;

@SystemApi
public class RcsFeature
  extends ImsFeature
{
  private final IImsRcsFeature mImsRcsBinder = new IImsRcsFeature.Stub() {};
  
  public RcsFeature() {}
  
  public void changeEnabledCapabilities(CapabilityChangeRequest paramCapabilityChangeRequest, ImsFeature.CapabilityCallbackProxy paramCapabilityCallbackProxy) {}
  
  public final IImsRcsFeature getBinder()
  {
    return mImsRcsBinder;
  }
  
  public void onFeatureReady() {}
  
  public void onFeatureRemoved() {}
}
