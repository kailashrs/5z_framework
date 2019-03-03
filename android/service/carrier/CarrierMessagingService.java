package android.service.carrier;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.List;

public abstract class CarrierMessagingService
  extends Service
{
  public static final int DOWNLOAD_STATUS_ERROR = 2;
  public static final int DOWNLOAD_STATUS_OK = 0;
  public static final int DOWNLOAD_STATUS_RETRY_ON_CARRIER_NETWORK = 1;
  public static final int RECEIVE_OPTIONS_DEFAULT = 0;
  public static final int RECEIVE_OPTIONS_DROP = 1;
  public static final int RECEIVE_OPTIONS_SKIP_NOTIFY_WHEN_CREDENTIAL_PROTECTED_STORAGE_UNAVAILABLE = 2;
  public static final int SEND_FLAG_REQUEST_DELIVERY_STATUS = 1;
  public static final int SEND_STATUS_ERROR = 2;
  public static final int SEND_STATUS_OK = 0;
  public static final int SEND_STATUS_RETRY_ON_CARRIER_NETWORK = 1;
  public static final String SERVICE_INTERFACE = "android.service.carrier.CarrierMessagingService";
  private final ICarrierMessagingWrapper mWrapper = new ICarrierMessagingWrapper(null);
  
  public CarrierMessagingService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    if (!"android.service.carrier.CarrierMessagingService".equals(paramIntent.getAction())) {
      return null;
    }
    return mWrapper;
  }
  
  public void onDownloadMms(Uri paramUri1, int paramInt, Uri paramUri2, ResultCallback<Integer> paramResultCallback)
  {
    try
    {
      paramResultCallback.onReceiveResult(Integer.valueOf(1));
    }
    catch (RemoteException paramUri1) {}
  }
  
  @Deprecated
  public void onFilterSms(MessagePdu paramMessagePdu, String paramString, int paramInt1, int paramInt2, ResultCallback<Boolean> paramResultCallback)
  {
    try
    {
      paramResultCallback.onReceiveResult(Boolean.valueOf(true));
    }
    catch (RemoteException paramMessagePdu) {}
  }
  
  public void onReceiveTextSms(MessagePdu paramMessagePdu, String paramString, int paramInt1, int paramInt2, final ResultCallback<Integer> paramResultCallback)
  {
    onFilterSms(paramMessagePdu, paramString, paramInt1, paramInt2, new ResultCallback()
    {
      public void onReceiveResult(Boolean paramAnonymousBoolean)
        throws RemoteException
      {
        CarrierMessagingService.ResultCallback localResultCallback = paramResultCallback;
        int i;
        if (paramAnonymousBoolean.booleanValue()) {
          i = 0;
        } else {
          i = 3;
        }
        localResultCallback.onReceiveResult(Integer.valueOf(i));
      }
    });
  }
  
  public void onSendDataSms(byte[] paramArrayOfByte, int paramInt1, String paramString, int paramInt2, int paramInt3, ResultCallback<SendSmsResult> paramResultCallback)
  {
    onSendDataSms(paramArrayOfByte, paramInt1, paramString, paramInt2, paramResultCallback);
  }
  
  @Deprecated
  public void onSendDataSms(byte[] paramArrayOfByte, int paramInt1, String paramString, int paramInt2, ResultCallback<SendSmsResult> paramResultCallback)
  {
    try
    {
      paramArrayOfByte = new android/service/carrier/CarrierMessagingService$SendSmsResult;
      paramArrayOfByte.<init>(1, 0);
      paramResultCallback.onReceiveResult(paramArrayOfByte);
    }
    catch (RemoteException paramArrayOfByte) {}
  }
  
  public void onSendMms(Uri paramUri1, int paramInt, Uri paramUri2, ResultCallback<SendMmsResult> paramResultCallback)
  {
    try
    {
      paramUri1 = new android/service/carrier/CarrierMessagingService$SendMmsResult;
      paramUri1.<init>(1, null);
      paramResultCallback.onReceiveResult(paramUri1);
    }
    catch (RemoteException paramUri1) {}
  }
  
  public void onSendMultipartTextSms(List<String> paramList, int paramInt1, String paramString, int paramInt2, ResultCallback<SendMultipartSmsResult> paramResultCallback)
  {
    onSendMultipartTextSms(paramList, paramInt1, paramString, paramResultCallback);
  }
  
  @Deprecated
  public void onSendMultipartTextSms(List<String> paramList, int paramInt, String paramString, ResultCallback<SendMultipartSmsResult> paramResultCallback)
  {
    try
    {
      paramList = new android/service/carrier/CarrierMessagingService$SendMultipartSmsResult;
      paramList.<init>(1, null);
      paramResultCallback.onReceiveResult(paramList);
    }
    catch (RemoteException paramList) {}
  }
  
  public void onSendTextSms(String paramString1, int paramInt1, String paramString2, int paramInt2, ResultCallback<SendSmsResult> paramResultCallback)
  {
    onSendTextSms(paramString1, paramInt1, paramString2, paramResultCallback);
  }
  
  @Deprecated
  public void onSendTextSms(String paramString1, int paramInt, String paramString2, ResultCallback<SendSmsResult> paramResultCallback)
  {
    try
    {
      paramString1 = new android/service/carrier/CarrierMessagingService$SendSmsResult;
      paramString1.<init>(1, 0);
      paramResultCallback.onReceiveResult(paramString1);
    }
    catch (RemoteException paramString1) {}
  }
  
  private class ICarrierMessagingWrapper
    extends ICarrierMessagingService.Stub
  {
    private ICarrierMessagingWrapper() {}
    
    public void downloadMms(Uri paramUri1, int paramInt, Uri paramUri2, final ICarrierMessagingCallback paramICarrierMessagingCallback)
    {
      onDownloadMms(paramUri1, paramInt, paramUri2, new CarrierMessagingService.ResultCallback()
      {
        public void onReceiveResult(Integer paramAnonymousInteger)
          throws RemoteException
        {
          paramICarrierMessagingCallback.onDownloadMmsComplete(paramAnonymousInteger.intValue());
        }
      });
    }
    
    public void filterSms(MessagePdu paramMessagePdu, String paramString, int paramInt1, int paramInt2, final ICarrierMessagingCallback paramICarrierMessagingCallback)
    {
      onReceiveTextSms(paramMessagePdu, paramString, paramInt1, paramInt2, new CarrierMessagingService.ResultCallback()
      {
        public void onReceiveResult(Integer paramAnonymousInteger)
          throws RemoteException
        {
          paramICarrierMessagingCallback.onFilterComplete(paramAnonymousInteger.intValue());
        }
      });
    }
    
    public void sendDataSms(byte[] paramArrayOfByte, int paramInt1, String paramString, int paramInt2, int paramInt3, final ICarrierMessagingCallback paramICarrierMessagingCallback)
    {
      onSendDataSms(paramArrayOfByte, paramInt1, paramString, paramInt2, paramInt3, new CarrierMessagingService.ResultCallback()
      {
        public void onReceiveResult(CarrierMessagingService.SendSmsResult paramAnonymousSendSmsResult)
          throws RemoteException
        {
          paramICarrierMessagingCallback.onSendSmsComplete(paramAnonymousSendSmsResult.getSendStatus(), paramAnonymousSendSmsResult.getMessageRef());
        }
      });
    }
    
    public void sendMms(Uri paramUri1, int paramInt, Uri paramUri2, final ICarrierMessagingCallback paramICarrierMessagingCallback)
    {
      onSendMms(paramUri1, paramInt, paramUri2, new CarrierMessagingService.ResultCallback()
      {
        public void onReceiveResult(CarrierMessagingService.SendMmsResult paramAnonymousSendMmsResult)
          throws RemoteException
        {
          paramICarrierMessagingCallback.onSendMmsComplete(paramAnonymousSendMmsResult.getSendStatus(), paramAnonymousSendMmsResult.getSendConfPdu());
        }
      });
    }
    
    public void sendMultipartTextSms(List<String> paramList, int paramInt1, String paramString, int paramInt2, final ICarrierMessagingCallback paramICarrierMessagingCallback)
    {
      onSendMultipartTextSms(paramList, paramInt1, paramString, paramInt2, new CarrierMessagingService.ResultCallback()
      {
        public void onReceiveResult(CarrierMessagingService.SendMultipartSmsResult paramAnonymousSendMultipartSmsResult)
          throws RemoteException
        {
          paramICarrierMessagingCallback.onSendMultipartSmsComplete(paramAnonymousSendMultipartSmsResult.getSendStatus(), paramAnonymousSendMultipartSmsResult.getMessageRefs());
        }
      });
    }
    
    public void sendTextSms(String paramString1, int paramInt1, String paramString2, int paramInt2, final ICarrierMessagingCallback paramICarrierMessagingCallback)
    {
      onSendTextSms(paramString1, paramInt1, paramString2, paramInt2, new CarrierMessagingService.ResultCallback()
      {
        public void onReceiveResult(CarrierMessagingService.SendSmsResult paramAnonymousSendSmsResult)
          throws RemoteException
        {
          paramICarrierMessagingCallback.onSendSmsComplete(paramAnonymousSendSmsResult.getSendStatus(), paramAnonymousSendSmsResult.getMessageRef());
        }
      });
    }
  }
  
  public static abstract interface ResultCallback<T>
  {
    public abstract void onReceiveResult(T paramT)
      throws RemoteException;
  }
  
  public static final class SendMmsResult
  {
    private byte[] mSendConfPdu;
    private int mSendStatus;
    
    public SendMmsResult(int paramInt, byte[] paramArrayOfByte)
    {
      mSendStatus = paramInt;
      mSendConfPdu = paramArrayOfByte;
    }
    
    public byte[] getSendConfPdu()
    {
      return mSendConfPdu;
    }
    
    public int getSendStatus()
    {
      return mSendStatus;
    }
  }
  
  public static final class SendMultipartSmsResult
  {
    private final int[] mMessageRefs;
    private final int mSendStatus;
    
    public SendMultipartSmsResult(int paramInt, int[] paramArrayOfInt)
    {
      mSendStatus = paramInt;
      mMessageRefs = paramArrayOfInt;
    }
    
    public int[] getMessageRefs()
    {
      return mMessageRefs;
    }
    
    public int getSendStatus()
    {
      return mSendStatus;
    }
  }
  
  public static final class SendSmsResult
  {
    private final int mMessageRef;
    private final int mSendStatus;
    
    public SendSmsResult(int paramInt1, int paramInt2)
    {
      mSendStatus = paramInt1;
      mMessageRef = paramInt2;
    }
    
    public int getMessageRef()
    {
      return mMessageRef;
    }
    
    public int getSendStatus()
    {
      return mSendStatus;
    }
  }
}
