package org.codeaurora.ims;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import org.codeaurora.ims.internal.IImsMultiIdentityInterface;
import org.codeaurora.ims.internal.IQtiImsExt;
import org.codeaurora.ims.internal.IQtiImsExt.Stub;
import org.codeaurora.ims.internal.IQtiImsExtListener;

public class QtiImsExtManager
{
  private static String LOG_TAG = "QtiImsExtManager";
  public static final String SERVICE_ID = "qti.ims.ext";
  private Context mContext;
  private IQtiImsExt mQtiImsExt;
  
  public QtiImsExtManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private void checkFeatureStatus(int paramInt)
    throws QtiImsException
  {
    if (mContext != null) {
      try
      {
        if (ImsManager.getInstance(mContext, paramInt).getImsServiceState() == 2) {
          return;
        }
        Object localObject1 = LOG_TAG;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Feature status for phoneId ");
        ((StringBuilder)localObject2).append(paramInt);
        ((StringBuilder)localObject2).append(" is not ready");
        Log.e((String)localObject1, ((StringBuilder)localObject2).toString());
        localObject1 = new org/codeaurora/ims/QtiImsException;
        ((QtiImsException)localObject1).<init>("Feature state is NOT_READY");
        throw ((Throwable)localObject1);
      }
      catch (ImsException localImsException)
      {
        Object localObject2 = LOG_TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Got ImsException for phoneId ");
        localStringBuilder.append(paramInt);
        Log.e((String)localObject2, localStringBuilder.toString());
        throw new QtiImsException("Feature state is NOT_READY");
      }
    }
    throw new QtiImsException("Context is null");
  }
  
  private void checkPhoneId(int paramInt)
    throws QtiImsException
  {
    if (SubscriptionManager.isValidPhoneId(paramInt)) {
      return;
    }
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("phoneId ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" is not valid");
    Log.e(str, localStringBuilder.toString());
    throw new QtiImsException("invalid phoneId");
  }
  
  public static ImsMultiIdentityManager createImsMultiIdentityManager(int paramInt, Context paramContext)
  {
    return new ImsMultiIdentityManager(paramInt, new QtiImsExtManager(paramContext));
  }
  
  private void handleQtiImsExtServiceDeath()
  {
    mQtiImsExt = null;
    Log.i(LOG_TAG, "qtiImsExtDeathListener QtiImsExt binder died");
  }
  
  private IQtiImsExt obtainBinder()
    throws QtiImsException
  {
    if (mQtiImsExt == null)
    {
      IBinder localIBinder = ServiceManager.getService("qti.ims.ext");
      mQtiImsExt = IQtiImsExt.Stub.asInterface(localIBinder);
      if (mQtiImsExt != null)
      {
        try
        {
          _..Lambda.QtiImsExtManager.JZBJzzRO39aEEHSt3af1pa3tl_c localJZBJzzRO39aEEHSt3af1pa3tl_c = new org/codeaurora/ims/_$$Lambda$QtiImsExtManager$JZBJzzRO39aEEHSt3af1pa3tl_c;
          localJZBJzzRO39aEEHSt3af1pa3tl_c.<init>(this);
          localIBinder.linkToDeath(localJZBJzzRO39aEEHSt3af1pa3tl_c, 0);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e(LOG_TAG, "Unable to listen for QtiImsExt service death");
        }
        return mQtiImsExt;
      }
      throw new QtiImsException("ImsService is not running");
    }
    return mQtiImsExt;
  }
  
  public void getCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      mQtiImsExt.getCallForwardUncondTimer(paramInt1, paramInt2, paramInt3, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService getCallForwardUncondTimer : ");
      paramIQtiImsExtListener.append(localRemoteException);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  public void getHandoverConfig(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.getHandoverConfig(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService getHandoverConfig : ");
      paramIQtiImsExtListener.append(localRemoteException);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  IImsMultiIdentityInterface getMultiIdentityInterface(int paramInt)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      IImsMultiIdentityInterface localIImsMultiIdentityInterface = mQtiImsExt.getMultiIdentityInterface(paramInt);
      return localIImsMultiIdentityInterface;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to retrieve MultiIdentityInterface : ");
      localStringBuilder.append(localRemoteException);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public void getPacketCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.getPacketCount(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService getPacketCount : ");
      paramIQtiImsExtListener.append(localRemoteException);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  public void getPacketErrorCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.getPacketErrorCount(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException paramIQtiImsExtListener)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService getPacketErrorCount : ");
      localStringBuilder.append(paramIQtiImsExtListener);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public int getRcsAppConfig(int paramInt)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      paramInt = mQtiImsExt.getRcsAppConfig(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService getRcsAppConfig : ");
      localStringBuilder.append(localRemoteException);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public int getVvmAppConfig(int paramInt)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      paramInt = mQtiImsExt.getVvmAppConfig(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService getVvmAppConfig : ");
      localStringBuilder.append(localRemoteException);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public boolean isImsRegistered(int paramInt)
    throws QtiImsException
  {
    int[] arrayOfInt = SubscriptionManager.getSubId(paramInt);
    int i = -1;
    paramInt = i;
    if (arrayOfInt != null)
    {
      paramInt = i;
      if (arrayOfInt.length >= 1) {
        paramInt = arrayOfInt[0];
      }
    }
    return ((TelephonyManager)mContext.getSystemService("phone")).isImsRegistered(paramInt);
  }
  
  public void querySsacStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.querySsacStatus(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService querySsacStatus : ");
      paramIQtiImsExtListener.append(localRemoteException);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  public void queryVoltePreference(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.queryVoltePreference(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService queryVoltePreference : ");
      paramIQtiImsExtListener.append(localRemoteException);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  public void queryVopsStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.queryVopsStatus(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException paramIQtiImsExtListener)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService queryVopsStatus : ");
      localStringBuilder.append(paramIQtiImsExtListener);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public void registerForParticipantStatusInfo(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.registerForParticipantStatusInfo(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException paramIQtiImsExtListener)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService registerForParticipantStatusInfo : ");
      localStringBuilder.append(paramIQtiImsExtListener);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public void resumePendingCall(int paramInt1, int paramInt2)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      mQtiImsExt.resumePendingCall(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService resumePendingCall : ");
      localStringBuilder.append(localRemoteException);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public void sendCallTransferRequest(int paramInt1, int paramInt2, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      mQtiImsExt.sendCallTransferRequest(paramInt1, paramInt2, paramString, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException paramString)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService sendCallTransferRequest : ");
      paramIQtiImsExtListener.append(paramString);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  public void sendCancelModifyCall(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
    try
    {
      mQtiImsExt.sendCancelModifyCall(paramInt, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException paramIQtiImsExtListener)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService sendCancelModifyCall : ");
      localStringBuilder.append(paramIQtiImsExtListener);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public void setCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      mQtiImsExt.setCallForwardUncondTimer(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramString, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException paramString)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService setCallForwardUncondTimer : ");
      paramIQtiImsExtListener.append(paramString);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  public void setHandoverConfig(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      mQtiImsExt.setHandoverConfig(paramInt1, paramInt2, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      paramIQtiImsExtListener = new StringBuilder();
      paramIQtiImsExtListener.append("Remote ImsService setHandoverConfig : ");
      paramIQtiImsExtListener.append(localRemoteException);
      throw new QtiImsException(paramIQtiImsExtListener.toString());
    }
  }
  
  public int setRcsAppConfig(int paramInt1, int paramInt2)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      paramInt1 = mQtiImsExt.setRcsAppConfig(paramInt1, paramInt2);
      return paramInt1;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService setRcsAppConfig : ");
      localStringBuilder.append(localRemoteException);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public int setVvmAppConfig(int paramInt1, int paramInt2)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      paramInt1 = mQtiImsExt.setVvmAppConfig(paramInt1, paramInt2);
      return paramInt1;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService setVvmAppConfig : ");
      localStringBuilder.append(localRemoteException);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  public void updateVoltePreference(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
    throws QtiImsException
  {
    obtainBinder();
    checkPhoneId(paramInt1);
    checkFeatureStatus(paramInt1);
    try
    {
      mQtiImsExt.updateVoltePreference(paramInt1, paramInt2, paramIQtiImsExtListener);
      return;
    }
    catch (RemoteException paramIQtiImsExtListener)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Remote ImsService updateVoltePreference : ");
      localStringBuilder.append(paramIQtiImsExtListener);
      throw new QtiImsException(localStringBuilder.toString());
    }
  }
  
  void validateInvariants(int paramInt)
    throws QtiImsException
  {
    checkPhoneId(paramInt);
    checkFeatureStatus(paramInt);
  }
}
