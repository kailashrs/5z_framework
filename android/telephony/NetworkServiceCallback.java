package android.telephony;

import android.os.RemoteException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class NetworkServiceCallback
{
  public static final int RESULT_ERROR_BUSY = 3;
  public static final int RESULT_ERROR_FAILED = 5;
  public static final int RESULT_ERROR_ILLEGAL_STATE = 4;
  public static final int RESULT_ERROR_INVALID_ARG = 2;
  public static final int RESULT_ERROR_UNSUPPORTED = 1;
  public static final int RESULT_SUCCESS = 0;
  private static final String mTag = NetworkServiceCallback.class.getSimpleName();
  private final WeakReference<INetworkServiceCallback> mCallback;
  
  public NetworkServiceCallback(INetworkServiceCallback paramINetworkServiceCallback)
  {
    mCallback = new WeakReference(paramINetworkServiceCallback);
  }
  
  public void onGetNetworkRegistrationStateComplete(int paramInt, NetworkRegistrationState paramNetworkRegistrationState)
  {
    INetworkServiceCallback localINetworkServiceCallback = (INetworkServiceCallback)mCallback.get();
    if (localINetworkServiceCallback != null) {
      try
      {
        localINetworkServiceCallback.onGetNetworkRegistrationStateComplete(paramInt, paramNetworkRegistrationState);
      }
      catch (RemoteException paramNetworkRegistrationState)
      {
        for (;;)
        {
          Rlog.e(mTag, "Failed to onGetNetworkRegistrationStateComplete on the remote");
        }
      }
    } else {
      Rlog.e(mTag, "Weak reference of callback is null.");
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Result {}
}
