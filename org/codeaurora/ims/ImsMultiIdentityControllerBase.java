package org.codeaurora.ims;

import android.os.RemoteException;
import java.util.List;
import org.codeaurora.ims.internal.IImsMultiIdentityInterface;
import org.codeaurora.ims.internal.IImsMultiIdentityInterface.Stub;
import org.codeaurora.ims.internal.IImsMultiIdentityListener;

public abstract class ImsMultiIdentityControllerBase
{
  private IImsMultiIdentityInterface mBinder;
  
  public ImsMultiIdentityControllerBase() {}
  
  public IImsMultiIdentityInterface getBinder()
  {
    if (mBinder == null) {
      mBinder = new MultiIdentityBinder();
    }
    return mBinder;
  }
  
  protected void queryVirtualLineInfo(String paramString)
    throws RemoteException
  {}
  
  protected void setMultiIdentityListener(IImsMultiIdentityListener paramIImsMultiIdentityListener)
    throws RemoteException
  {}
  
  protected void updateRegistrationStatus(List<MultiIdentityLineInfo> paramList) {}
  
  public final class MultiIdentityBinder
    extends IImsMultiIdentityInterface.Stub
  {
    public MultiIdentityBinder() {}
    
    public void queryVirtualLineInfo(String paramString)
      throws RemoteException
    {
      ImsMultiIdentityControllerBase.this.queryVirtualLineInfo(paramString);
    }
    
    public void setMultiIdentityListener(IImsMultiIdentityListener paramIImsMultiIdentityListener)
      throws RemoteException
    {
      ImsMultiIdentityControllerBase.this.setMultiIdentityListener(paramIImsMultiIdentityListener);
    }
    
    public void updateRegistrationStatus(List<MultiIdentityLineInfo> paramList)
    {
      ImsMultiIdentityControllerBase.this.updateRegistrationStatus(paramList);
    }
  }
}
