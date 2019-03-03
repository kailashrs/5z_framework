package android.telephony.data;

import android.os.RemoteException;
import android.telephony.Rlog;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.List;

public class DataServiceCallback
{
  public static final int RESULT_ERROR_BUSY = 3;
  public static final int RESULT_ERROR_ILLEGAL_STATE = 4;
  public static final int RESULT_ERROR_INVALID_ARG = 2;
  public static final int RESULT_ERROR_UNSUPPORTED = 1;
  public static final int RESULT_SUCCESS = 0;
  private static final String TAG = DataServiceCallback.class.getSimpleName();
  private final WeakReference<IDataServiceCallback> mCallback;
  
  public DataServiceCallback(IDataServiceCallback paramIDataServiceCallback)
  {
    mCallback = new WeakReference(paramIDataServiceCallback);
  }
  
  public void onDataCallListChanged(List<DataCallResponse> paramList)
  {
    IDataServiceCallback localIDataServiceCallback = (IDataServiceCallback)mCallback.get();
    if (localIDataServiceCallback != null) {
      try
      {
        localIDataServiceCallback.onDataCallListChanged(paramList);
      }
      catch (RemoteException paramList)
      {
        Rlog.e(TAG, "Failed to onDataCallListChanged on the remote");
      }
    }
  }
  
  public void onDeactivateDataCallComplete(int paramInt)
  {
    IDataServiceCallback localIDataServiceCallback = (IDataServiceCallback)mCallback.get();
    if (localIDataServiceCallback != null) {
      try
      {
        localIDataServiceCallback.onDeactivateDataCallComplete(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        Rlog.e(TAG, "Failed to onDeactivateDataCallComplete on the remote");
      }
    }
  }
  
  public void onGetDataCallListComplete(int paramInt, List<DataCallResponse> paramList)
  {
    IDataServiceCallback localIDataServiceCallback = (IDataServiceCallback)mCallback.get();
    if (localIDataServiceCallback != null) {
      try
      {
        localIDataServiceCallback.onGetDataCallListComplete(paramInt, paramList);
      }
      catch (RemoteException paramList)
      {
        Rlog.e(TAG, "Failed to onGetDataCallListComplete on the remote");
      }
    }
  }
  
  public void onSetDataProfileComplete(int paramInt)
  {
    IDataServiceCallback localIDataServiceCallback = (IDataServiceCallback)mCallback.get();
    if (localIDataServiceCallback != null) {
      try
      {
        localIDataServiceCallback.onSetDataProfileComplete(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        Rlog.e(TAG, "Failed to onSetDataProfileComplete on the remote");
      }
    }
  }
  
  public void onSetInitialAttachApnComplete(int paramInt)
  {
    IDataServiceCallback localIDataServiceCallback = (IDataServiceCallback)mCallback.get();
    if (localIDataServiceCallback != null) {
      try
      {
        localIDataServiceCallback.onSetInitialAttachApnComplete(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        Rlog.e(TAG, "Failed to onSetInitialAttachApnComplete on the remote");
      }
    }
  }
  
  public void onSetupDataCallComplete(int paramInt, DataCallResponse paramDataCallResponse)
  {
    IDataServiceCallback localIDataServiceCallback = (IDataServiceCallback)mCallback.get();
    if (localIDataServiceCallback != null) {
      try
      {
        localIDataServiceCallback.onSetupDataCallComplete(paramInt, paramDataCallResponse);
      }
      catch (RemoteException paramDataCallResponse)
      {
        Rlog.e(TAG, "Failed to onSetupDataCallComplete on the remote");
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResultCode {}
}
