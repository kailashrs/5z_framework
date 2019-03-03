package org.codeaurora.ims;

import java.util.List;
import org.codeaurora.ims.internal.IImsMultiIdentityListener;
import org.codeaurora.ims.internal.IImsMultiIdentityListener.Stub;

public abstract class ImsMultiIdentityListenerBase
{
  private MultiIdentityListener mListener;
  
  public ImsMultiIdentityListenerBase() {}
  
  public IImsMultiIdentityListener getListener()
  {
    if (mListener == null) {
      mListener = new MultiIdentityListener(null);
    }
    return mListener;
  }
  
  protected void onQueryVirtualLineInfoResponse(int paramInt, String paramString, List<String> paramList) {}
  
  protected void onRegistrationStatusChange(int paramInt, List<MultiIdentityLineInfo> paramList) {}
  
  protected void onUpdateRegistrationInfoResponse(int paramInt1, int paramInt2) {}
  
  private final class MultiIdentityListener
    extends IImsMultiIdentityListener.Stub
  {
    private MultiIdentityListener() {}
    
    public void onQueryVirtualLineInfoResponse(int paramInt, String paramString, List<String> paramList)
    {
      ImsMultiIdentityListenerBase.this.onQueryVirtualLineInfoResponse(paramInt, paramString, paramList);
    }
    
    public void onRegistrationStatusChange(int paramInt, List<MultiIdentityLineInfo> paramList)
    {
      ImsMultiIdentityListenerBase.this.onRegistrationStatusChange(paramInt, paramList);
    }
    
    public void onUpdateRegistrationInfoResponse(int paramInt1, int paramInt2)
    {
      ImsMultiIdentityListenerBase.this.onUpdateRegistrationInfoResponse(paramInt1, paramInt2);
    }
  }
}
