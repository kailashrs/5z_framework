package android.telephony.ims.compat.feature;

import com.android.ims.internal.IImsRcsFeature;
import com.android.ims.internal.IImsRcsFeature.Stub;

public class RcsFeature
  extends ImsFeature
{
  private final IImsRcsFeature mImsRcsBinder = new IImsRcsFeature.Stub() {};
  
  public RcsFeature() {}
  
  public final IImsRcsFeature getBinder()
  {
    return mImsRcsBinder;
  }
  
  public void onFeatureReady() {}
  
  public void onFeatureRemoved() {}
}
