package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsEcbm.Stub;
import com.android.ims.internal.IImsEcbmListener;

@SystemApi
public class ImsEcbmImplBase
{
  private static final String TAG = "ImsEcbmImplBase";
  private IImsEcbm mImsEcbm = new IImsEcbm.Stub()
  {
    public void exitEmergencyCallbackMode()
    {
      ImsEcbmImplBase.this.exitEmergencyCallbackMode();
    }
    
    public void setListener(IImsEcbmListener paramAnonymousIImsEcbmListener)
    {
      ImsEcbmImplBase.access$002(ImsEcbmImplBase.this, paramAnonymousIImsEcbmListener);
    }
  };
  private IImsEcbmListener mListener;
  
  public ImsEcbmImplBase() {}
  
  public final void enteredEcbm()
  {
    Log.d("ImsEcbmImplBase", "Entered ECBM.");
    if (mListener != null) {
      try
      {
        mListener.enteredECBM();
      }
      catch (RemoteException localRemoteException)
      {
        throw new RuntimeException(localRemoteException);
      }
    }
  }
  
  public void exitEmergencyCallbackMode()
  {
    Log.d("ImsEcbmImplBase", "exitEmergencyCallbackMode() not implemented");
  }
  
  public final void exitedEcbm()
  {
    Log.d("ImsEcbmImplBase", "Exited ECBM.");
    if (mListener != null) {
      try
      {
        mListener.exitedECBM();
      }
      catch (RemoteException localRemoteException)
      {
        throw new RuntimeException(localRemoteException);
      }
    }
  }
  
  public IImsEcbm getImsEcbm()
  {
    return mImsEcbm;
  }
}
