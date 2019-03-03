package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.telephony.SmsMessage;
import android.telephony.ims.aidl.IImsSmsListener;
import android.util.Log;
import com.android.internal.telephony.SmsMessageBase;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public class ImsSmsImplBase
{
  public static final int DELIVER_STATUS_ERROR_GENERIC = 2;
  public static final int DELIVER_STATUS_ERROR_NO_MEMORY = 3;
  public static final int DELIVER_STATUS_ERROR_REQUEST_NOT_SUPPORTED = 4;
  public static final int DELIVER_STATUS_OK = 1;
  private static final String LOG_TAG = "SmsImplBase";
  public static final int SEND_STATUS_ERROR = 2;
  public static final int SEND_STATUS_ERROR_FALLBACK = 4;
  public static final int SEND_STATUS_ERROR_RETRY = 3;
  public static final int SEND_STATUS_OK = 1;
  public static final int STATUS_REPORT_STATUS_ERROR = 2;
  public static final int STATUS_REPORT_STATUS_OK = 1;
  private IImsSmsListener mListener;
  private final Object mLock = new Object();
  
  public ImsSmsImplBase() {}
  
  public void acknowledgeSms(int paramInt1, int paramInt2, int paramInt3)
  {
    Log.e("SmsImplBase", "acknowledgeSms() not implemented.");
  }
  
  public void acknowledgeSmsReport(int paramInt1, int paramInt2, int paramInt3)
  {
    Log.e("SmsImplBase", "acknowledgeSmsReport() not implemented.");
  }
  
  public String getSmsFormat()
  {
    return "3gpp";
  }
  
  public void onReady() {}
  
  public final void onSendSmsResult(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RuntimeException
  {
    synchronized (mLock)
    {
      IImsSmsListener localIImsSmsListener = mListener;
      if (localIImsSmsListener != null)
      {
        try
        {
          mListener.onSendSmsResult(paramInt1, paramInt2, paramInt3, paramInt4);
        }
        catch (RemoteException localRemoteException)
        {
          localRemoteException.rethrowFromSystemServer();
        }
        return;
      }
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      localRuntimeException.<init>("Feature not ready.");
      throw localRuntimeException;
    }
  }
  
  public final void onSmsReceived(int paramInt, String paramString, byte[] paramArrayOfByte)
    throws RuntimeException
  {
    synchronized (mLock)
    {
      Object localObject2 = mListener;
      if (localObject2 != null)
      {
        try
        {
          mListener.onSmsReceived(paramInt, paramString, paramArrayOfByte);
        }
        catch (RemoteException localRemoteException)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Can not deliver sms: ");
          ((StringBuilder)localObject2).append(localRemoteException.getMessage());
          Log.e("SmsImplBase", ((StringBuilder)localObject2).toString());
          paramString = SmsMessage.createFromPdu(paramArrayOfByte, paramString);
          if ((paramString != null) && (mWrappedSmsMessage != null))
          {
            acknowledgeSms(paramInt, mWrappedSmsMessage.mMessageRef, 2);
          }
          else
          {
            Log.w("SmsImplBase", "onSmsReceived: Invalid pdu entered.");
            acknowledgeSms(paramInt, 0, 2);
          }
        }
        return;
      }
      paramString = new java/lang/RuntimeException;
      paramString.<init>("Feature not ready.");
      throw paramString;
    }
  }
  
  public final void onSmsStatusReportReceived(int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte)
    throws RuntimeException
  {
    synchronized (mLock)
    {
      IImsSmsListener localIImsSmsListener = mListener;
      if (localIImsSmsListener != null)
      {
        try
        {
          mListener.onSmsStatusReportReceived(paramInt1, paramInt2, paramString, paramArrayOfByte);
        }
        catch (RemoteException paramString)
        {
          paramArrayOfByte = new java/lang/StringBuilder;
          paramArrayOfByte.<init>();
          paramArrayOfByte.append("Can not process sms status report: ");
          paramArrayOfByte.append(paramString.getMessage());
          Log.e("SmsImplBase", paramArrayOfByte.toString());
          acknowledgeSmsReport(paramInt1, paramInt2, 2);
        }
        return;
      }
      paramString = new java/lang/RuntimeException;
      paramString.<init>("Feature not ready.");
      throw paramString;
    }
  }
  
  public final void registerSmsListener(IImsSmsListener paramIImsSmsListener)
  {
    synchronized (mLock)
    {
      mListener = paramIImsSmsListener;
      return;
    }
  }
  
  public void sendSms(int paramInt1, int paramInt2, String paramString1, String paramString2, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    try
    {
      onSendSmsResult(paramInt1, paramInt2, 2, 1);
    }
    catch (RuntimeException paramString1)
    {
      paramString2 = new StringBuilder();
      paramString2.append("Can not send sms: ");
      paramString2.append(paramString1.getMessage());
      Log.e("SmsImplBase", paramString2.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DeliverStatusResult {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SendStatusResult {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StatusReportResult {}
}
