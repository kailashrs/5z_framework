package android.telephony.mbms;

import android.net.Uri;
import android.os.RemoteException;
import android.telephony.MbmsStreamingSession;
import android.telephony.mbms.vendor.IMbmsStreamingService;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StreamingService
  implements AutoCloseable
{
  public static final int BROADCAST_METHOD = 1;
  private static final String LOG_TAG = "MbmsStreamingService";
  public static final int REASON_BY_USER_REQUEST = 1;
  public static final int REASON_END_OF_SESSION = 2;
  public static final int REASON_FREQUENCY_CONFLICT = 3;
  public static final int REASON_LEFT_MBMS_BROADCAST_AREA = 6;
  public static final int REASON_NONE = 0;
  public static final int REASON_NOT_CONNECTED_TO_HOMECARRIER_LTE = 5;
  public static final int REASON_OUT_OF_MEMORY = 4;
  public static final int STATE_STALLED = 3;
  public static final int STATE_STARTED = 2;
  public static final int STATE_STOPPED = 1;
  public static final int UNICAST_METHOD = 2;
  private final InternalStreamingServiceCallback mCallback;
  private final MbmsStreamingSession mParentSession;
  private IMbmsStreamingService mService;
  private final StreamingServiceInfo mServiceInfo;
  private final int mSubscriptionId;
  
  public StreamingService(int paramInt, IMbmsStreamingService paramIMbmsStreamingService, MbmsStreamingSession paramMbmsStreamingSession, StreamingServiceInfo paramStreamingServiceInfo, InternalStreamingServiceCallback paramInternalStreamingServiceCallback)
  {
    mSubscriptionId = paramInt;
    mParentSession = paramMbmsStreamingSession;
    mService = paramIMbmsStreamingService;
    mServiceInfo = paramStreamingServiceInfo;
    mCallback = paramInternalStreamingServiceCallback;
  }
  
  private void sendErrorToApp(int paramInt, String paramString)
  {
    try
    {
      mCallback.onError(paramInt, paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 56	android/telephony/mbms/StreamingService:mService	Landroid/telephony/mbms/vendor/IMbmsStreamingService;
    //   4: ifnull +72 -> 76
    //   7: aload_0
    //   8: getfield 56	android/telephony/mbms/StreamingService:mService	Landroid/telephony/mbms/vendor/IMbmsStreamingService;
    //   11: aload_0
    //   12: getfield 52	android/telephony/mbms/StreamingService:mSubscriptionId	I
    //   15: aload_0
    //   16: getfield 58	android/telephony/mbms/StreamingService:mServiceInfo	Landroid/telephony/mbms/StreamingServiceInfo;
    //   19: invokevirtual 77	android/telephony/mbms/StreamingServiceInfo:getServiceId	()Ljava/lang/String;
    //   22: invokeinterface 82 3 0
    //   27: aload_0
    //   28: getfield 54	android/telephony/mbms/StreamingService:mParentSession	Landroid/telephony/MbmsStreamingSession;
    //   31: aload_0
    //   32: invokevirtual 88	android/telephony/MbmsStreamingSession:onStreamingServiceStopped	(Landroid/telephony/mbms/StreamingService;)V
    //   35: goto +30 -> 65
    //   38: astore_1
    //   39: goto +27 -> 66
    //   42: astore_1
    //   43: ldc 19
    //   45: ldc 90
    //   47: invokestatic 96	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   50: pop
    //   51: aload_0
    //   52: aconst_null
    //   53: putfield 56	android/telephony/mbms/StreamingService:mService	Landroid/telephony/mbms/vendor/IMbmsStreamingService;
    //   56: aload_0
    //   57: iconst_3
    //   58: aconst_null
    //   59: invokespecial 98	android/telephony/mbms/StreamingService:sendErrorToApp	(ILjava/lang/String;)V
    //   62: goto -35 -> 27
    //   65: return
    //   66: aload_0
    //   67: getfield 54	android/telephony/mbms/StreamingService:mParentSession	Landroid/telephony/MbmsStreamingSession;
    //   70: aload_0
    //   71: invokevirtual 88	android/telephony/MbmsStreamingSession:onStreamingServiceStopped	(Landroid/telephony/mbms/StreamingService;)V
    //   74: aload_1
    //   75: athrow
    //   76: new 100	java/lang/IllegalStateException
    //   79: dup
    //   80: ldc 102
    //   82: invokespecial 105	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   85: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	86	0	this	StreamingService
    //   38	1	1	localObject	Object
    //   42	33	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   7	27	38	finally
    //   43	62	38	finally
    //   7	27	42	android/os/RemoteException
  }
  
  public InternalStreamingServiceCallback getCallback()
  {
    return mCallback;
  }
  
  public StreamingServiceInfo getInfo()
  {
    return mServiceInfo;
  }
  
  public Uri getPlaybackUri()
  {
    if (mService != null) {
      try
      {
        Uri localUri = mService.getPlaybackUri(mSubscriptionId, mServiceInfo.getServiceId());
        return localUri;
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("MbmsStreamingService", "Remote process died");
        mService = null;
        mParentSession.onStreamingServiceStopped(this);
        sendErrorToApp(3, null);
        return null;
      }
    }
    throw new IllegalStateException("No streaming service attached");
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StreamingState {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StreamingStateChangeReason {}
}
