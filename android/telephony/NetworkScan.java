package android.telephony;

import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NetworkScan
{
  public static final int ERROR_INTERRUPTED = 10002;
  public static final int ERROR_INVALID_SCAN = 2;
  public static final int ERROR_INVALID_SCANID = 10001;
  public static final int ERROR_MODEM_ERROR = 1;
  public static final int ERROR_MODEM_UNAVAILABLE = 3;
  public static final int ERROR_RADIO_INTERFACE_ERROR = 10000;
  public static final int ERROR_UNSUPPORTED = 4;
  public static final int SUCCESS = 0;
  private static final String TAG = "NetworkScan";
  private final int mScanId;
  private final int mSubId;
  
  public NetworkScan(int paramInt1, int paramInt2)
  {
    mScanId = paramInt1;
    mSubId = paramInt2;
  }
  
  private ITelephony getITelephony()
  {
    return ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
  }
  
  @Deprecated
  public void stop()
    throws RemoteException
  {
    try
    {
      stopScan();
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to stop the network scan with id ");
      localStringBuilder.append(mScanId);
      throw new RemoteException(localStringBuilder.toString());
    }
  }
  
  public void stopScan()
  {
    ITelephony localITelephony = getITelephony();
    if (localITelephony == null) {
      Rlog.e("NetworkScan", "Failed to get the ITelephony instance.");
    }
    try
    {
      localITelephony.stopNetworkScan(mSubId, mScanId);
    }
    catch (RuntimeException localRuntimeException)
    {
      Rlog.e("NetworkScan", "stopNetworkScan  RuntimeException", localRuntimeException);
    }
    catch (RemoteException localRemoteException)
    {
      Rlog.e("NetworkScan", "stopNetworkScan  RemoteException", localRemoteException);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScanErrorCode {}
}
