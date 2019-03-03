package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.telephony.ims.ImsExternalCallState;
import android.util.Log;
import com.android.ims.internal.IImsExternalCallStateListener;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsMultiEndpoint.Stub;
import java.util.List;

@SystemApi
public class ImsMultiEndpointImplBase
{
  private static final String TAG = "MultiEndpointImplBase";
  private IImsMultiEndpoint mImsMultiEndpoint = new IImsMultiEndpoint.Stub()
  {
    public void requestImsExternalCallStateInfo()
      throws RemoteException
    {
      ImsMultiEndpointImplBase.this.requestImsExternalCallStateInfo();
    }
    
    public void setListener(IImsExternalCallStateListener paramAnonymousIImsExternalCallStateListener)
      throws RemoteException
    {
      ImsMultiEndpointImplBase.access$002(ImsMultiEndpointImplBase.this, paramAnonymousIImsExternalCallStateListener);
    }
  };
  private IImsExternalCallStateListener mListener;
  
  public ImsMultiEndpointImplBase() {}
  
  public IImsMultiEndpoint getIImsMultiEndpoint()
  {
    return mImsMultiEndpoint;
  }
  
  public final void onImsExternalCallStateUpdate(List<ImsExternalCallState> paramList)
  {
    Log.d("MultiEndpointImplBase", "ims external call state update triggered.");
    if (mListener != null) {
      try
      {
        mListener.onImsExternalCallStateUpdate(paramList);
      }
      catch (RemoteException paramList)
      {
        throw new RuntimeException(paramList);
      }
    }
  }
  
  public void requestImsExternalCallStateInfo()
  {
    Log.d("MultiEndpointImplBase", "requestImsExternalCallStateInfo() not implemented");
  }
}
