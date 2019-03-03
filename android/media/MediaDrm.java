package android.media;

import android.app.ActivityThread;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MediaDrm
  implements AutoCloseable
{
  public static final int CERTIFICATE_TYPE_NONE = 0;
  public static final int CERTIFICATE_TYPE_X509 = 1;
  private static final int DRM_EVENT = 200;
  public static final int EVENT_KEY_EXPIRED = 3;
  public static final int EVENT_KEY_REQUIRED = 2;
  public static final int EVENT_PROVISION_REQUIRED = 1;
  public static final int EVENT_SESSION_RECLAIMED = 5;
  public static final int EVENT_VENDOR_DEFINED = 4;
  private static final int EXPIRATION_UPDATE = 201;
  public static final int HDCP_LEVEL_UNKNOWN = 0;
  public static final int HDCP_NONE = 1;
  public static final int HDCP_NO_DIGITAL_OUTPUT = Integer.MAX_VALUE;
  public static final int HDCP_V1 = 2;
  public static final int HDCP_V2 = 3;
  public static final int HDCP_V2_1 = 4;
  public static final int HDCP_V2_2 = 5;
  private static final int KEY_STATUS_CHANGE = 202;
  public static final int KEY_TYPE_OFFLINE = 2;
  public static final int KEY_TYPE_RELEASE = 3;
  public static final int KEY_TYPE_STREAMING = 1;
  private static final String PERMISSION = "android.permission.ACCESS_DRM_CERTIFICATES";
  public static final String PROPERTY_ALGORITHMS = "algorithms";
  public static final String PROPERTY_DESCRIPTION = "description";
  public static final String PROPERTY_DEVICE_UNIQUE_ID = "deviceUniqueId";
  public static final String PROPERTY_VENDOR = "vendor";
  public static final String PROPERTY_VERSION = "version";
  public static final int SECURITY_LEVEL_HW_SECURE_ALL = 5;
  public static final int SECURITY_LEVEL_HW_SECURE_CRYPTO = 3;
  public static final int SECURITY_LEVEL_HW_SECURE_DECODE = 4;
  public static final int SECURITY_LEVEL_MAX = 6;
  public static final int SECURITY_LEVEL_SW_SECURE_CRYPTO = 1;
  public static final int SECURITY_LEVEL_SW_SECURE_DECODE = 2;
  public static final int SECURITY_LEVEL_UNKNOWN = 0;
  private static final String TAG = "MediaDrm";
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mClosed = new AtomicBoolean();
  private EventHandler mEventHandler;
  private long mNativeContext;
  private OnEventListener mOnEventListener;
  private EventHandler mOnExpirationUpdateEventHandler;
  private OnExpirationUpdateListener mOnExpirationUpdateListener;
  private EventHandler mOnKeyStatusChangeEventHandler;
  private OnKeyStatusChangeListener mOnKeyStatusChangeListener;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public MediaDrm(UUID paramUUID)
    throws UnsupportedSchemeException
  {
    Looper localLooper = Looper.myLooper();
    if (localLooper != null)
    {
      mEventHandler = new EventHandler(this, localLooper);
    }
    else
    {
      localLooper = Looper.getMainLooper();
      if (localLooper != null) {
        mEventHandler = new EventHandler(this, localLooper);
      } else {
        mEventHandler = null;
      }
    }
    native_setup(new WeakReference(this), getByteArrayFromUUID(paramUUID), ActivityThread.currentOpPackageName());
    mCloseGuard.open("release");
  }
  
  private static final native byte[] decryptNative(MediaDrm paramMediaDrm, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4);
  
  private static final native byte[] encryptNative(MediaDrm paramMediaDrm, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4);
  
  private static final byte[] getByteArrayFromUUID(UUID paramUUID)
  {
    long l1 = paramUUID.getMostSignificantBits();
    long l2 = paramUUID.getLeastSignificantBits();
    paramUUID = new byte[16];
    for (int i = 0; i < 8; i++)
    {
      paramUUID[i] = ((byte)(byte)(int)(l1 >>> (7 - i) * 8));
      paramUUID[(8 + i)] = ((byte)(byte)(int)(l2 >>> 8 * (7 - i)));
    }
    return paramUUID;
  }
  
  public static final int getMaxSecurityLevel()
  {
    return 6;
  }
  
  private native PersistableBundle getMetricsNative();
  
  private native ProvisionRequest getProvisionRequestNative(int paramInt, String paramString);
  
  public static final boolean isCryptoSchemeSupported(UUID paramUUID)
  {
    return isCryptoSchemeSupportedNative(getByteArrayFromUUID(paramUUID), null);
  }
  
  public static final boolean isCryptoSchemeSupported(UUID paramUUID, String paramString)
  {
    return isCryptoSchemeSupportedNative(getByteArrayFromUUID(paramUUID), paramString);
  }
  
  private static final native boolean isCryptoSchemeSupportedNative(byte[] paramArrayOfByte, String paramString);
  
  private List<KeyStatus> keyStatusListFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    ArrayList localArrayList = new ArrayList(i);
    while (i > 0)
    {
      localArrayList.add(new KeyStatus(paramParcel.createByteArray(), paramParcel.readInt()));
      i--;
    }
    return localArrayList;
  }
  
  private static final native void native_init();
  
  private final native void native_setup(Object paramObject, byte[] paramArrayOfByte, String paramString);
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2)
  {
    paramObject1 = (MediaDrm)((WeakReference)paramObject1).get();
    if (paramObject1 == null) {
      return;
    }
    if (mEventHandler != null)
    {
      paramObject2 = mEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
      mEventHandler.sendMessage(paramObject2);
    }
  }
  
  private native Certificate provideProvisionResponseNative(byte[] paramArrayOfByte)
    throws DeniedByServerException;
  
  private static final native void setCipherAlgorithmNative(MediaDrm paramMediaDrm, byte[] paramArrayOfByte, String paramString);
  
  private static final native void setMacAlgorithmNative(MediaDrm paramMediaDrm, byte[] paramArrayOfByte, String paramString);
  
  private static final native byte[] signNative(MediaDrm paramMediaDrm, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);
  
  private static final native byte[] signRSANative(MediaDrm paramMediaDrm, byte[] paramArrayOfByte1, String paramString, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);
  
  private static final native boolean verifyNative(MediaDrm paramMediaDrm, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4);
  
  public void close()
  {
    release();
  }
  
  public native void closeSession(byte[] paramArrayOfByte);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      release();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public CertificateRequest getCertificateRequest(int paramInt, String paramString)
  {
    paramString = getProvisionRequestNative(paramInt, paramString);
    return new CertificateRequest(paramString.getData(), paramString.getDefaultUrl());
  }
  
  public native int getConnectedHdcpLevel();
  
  public CryptoSession getCryptoSession(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    return new CryptoSession(paramArrayOfByte, paramString1, paramString2);
  }
  
  public native KeyRequest getKeyRequest(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString, int paramInt, HashMap<String, String> paramHashMap)
    throws NotProvisionedException;
  
  public native int getMaxHdcpLevel();
  
  public native int getMaxSessionCount();
  
  public PersistableBundle getMetrics()
  {
    return getMetricsNative();
  }
  
  public native int getOpenSessionCount();
  
  public native byte[] getPropertyByteArray(String paramString);
  
  public native String getPropertyString(String paramString);
  
  public ProvisionRequest getProvisionRequest()
  {
    return getProvisionRequestNative(0, "");
  }
  
  public native byte[] getSecureStop(byte[] paramArrayOfByte);
  
  public native List<byte[]> getSecureStopIds();
  
  public native List<byte[]> getSecureStops();
  
  public native int getSecurityLevel(byte[] paramArrayOfByte);
  
  public final native void native_release();
  
  public byte[] openSession()
    throws NotProvisionedException, ResourceBusyException
  {
    return openSession(getMaxSecurityLevel());
  }
  
  public native byte[] openSession(int paramInt)
    throws NotProvisionedException, ResourceBusyException;
  
  public Certificate provideCertificateResponse(byte[] paramArrayOfByte)
    throws DeniedByServerException
  {
    return provideProvisionResponseNative(paramArrayOfByte);
  }
  
  public native byte[] provideKeyResponse(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws NotProvisionedException, DeniedByServerException;
  
  public void provideProvisionResponse(byte[] paramArrayOfByte)
    throws DeniedByServerException
  {
    provideProvisionResponseNative(paramArrayOfByte);
  }
  
  public native HashMap<String, String> queryKeyStatus(byte[] paramArrayOfByte);
  
  @Deprecated
  public void release()
  {
    mCloseGuard.close();
    if (mClosed.compareAndSet(false, true)) {
      native_release();
    }
  }
  
  public void releaseAllSecureStops()
  {
    removeAllSecureStops();
  }
  
  public native void releaseSecureStops(byte[] paramArrayOfByte);
  
  public native void removeAllSecureStops();
  
  public native void removeKeys(byte[] paramArrayOfByte);
  
  public native void removeSecureStop(byte[] paramArrayOfByte);
  
  public native void restoreKeys(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
  
  public void setOnEventListener(OnEventListener paramOnEventListener)
  {
    mOnEventListener = paramOnEventListener;
  }
  
  public void setOnExpirationUpdateListener(OnExpirationUpdateListener paramOnExpirationUpdateListener, Handler paramHandler)
  {
    if (paramOnExpirationUpdateListener != null)
    {
      if (paramHandler != null) {
        paramHandler = paramHandler.getLooper();
      } else {
        paramHandler = Looper.myLooper();
      }
      if ((paramHandler != null) && ((mEventHandler == null) || (mEventHandler.getLooper() != paramHandler))) {
        mEventHandler = new EventHandler(this, paramHandler);
      }
    }
    mOnExpirationUpdateListener = paramOnExpirationUpdateListener;
  }
  
  public void setOnKeyStatusChangeListener(OnKeyStatusChangeListener paramOnKeyStatusChangeListener, Handler paramHandler)
  {
    if (paramOnKeyStatusChangeListener != null)
    {
      if (paramHandler != null) {
        paramHandler = paramHandler.getLooper();
      } else {
        paramHandler = Looper.myLooper();
      }
      if ((paramHandler != null) && ((mEventHandler == null) || (mEventHandler.getLooper() != paramHandler))) {
        mEventHandler = new EventHandler(this, paramHandler);
      }
    }
    mOnKeyStatusChangeListener = paramOnKeyStatusChangeListener;
  }
  
  public native void setPropertyByteArray(String paramString, byte[] paramArrayOfByte);
  
  public native void setPropertyString(String paramString1, String paramString2);
  
  public byte[] signRSA(byte[] paramArrayOfByte1, String paramString, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    return signRSANative(this, paramArrayOfByte1, paramString, paramArrayOfByte2, paramArrayOfByte3);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ArrayProperty {}
  
  public static final class Certificate
  {
    private byte[] mCertificateData;
    private byte[] mWrappedKey;
    
    Certificate() {}
    
    public byte[] getContent()
    {
      if (mCertificateData != null) {
        return mCertificateData;
      }
      throw new RuntimeException("Cerfificate is not initialized");
    }
    
    public byte[] getWrappedPrivateKey()
    {
      if (mWrappedKey != null) {
        return mWrappedKey;
      }
      throw new RuntimeException("Cerfificate is not initialized");
    }
  }
  
  public static final class CertificateRequest
  {
    private byte[] mData;
    private String mDefaultUrl;
    
    CertificateRequest(byte[] paramArrayOfByte, String paramString)
    {
      mData = paramArrayOfByte;
      mDefaultUrl = paramString;
    }
    
    public byte[] getData()
    {
      return mData;
    }
    
    public String getDefaultUrl()
    {
      return mDefaultUrl;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CertificateType {}
  
  public final class CryptoSession
  {
    private byte[] mSessionId;
    
    CryptoSession(byte[] paramArrayOfByte, String paramString1, String paramString2)
    {
      mSessionId = paramArrayOfByte;
      MediaDrm.setCipherAlgorithmNative(MediaDrm.this, paramArrayOfByte, paramString1);
      MediaDrm.setMacAlgorithmNative(MediaDrm.this, paramArrayOfByte, paramString2);
    }
    
    public byte[] decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    {
      return MediaDrm.decryptNative(MediaDrm.this, mSessionId, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }
    
    public byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    {
      return MediaDrm.encryptNative(MediaDrm.this, mSessionId, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }
    
    public byte[] sign(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      return MediaDrm.signNative(MediaDrm.this, mSessionId, paramArrayOfByte1, paramArrayOfByte2);
    }
    
    public boolean verify(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    {
      return MediaDrm.verifyNative(MediaDrm.this, mSessionId, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DrmEvent {}
  
  private class EventHandler
    extends Handler
  {
    private MediaDrm mMediaDrm;
    
    public EventHandler(MediaDrm paramMediaDrm, Looper paramLooper)
    {
      super();
      mMediaDrm = paramMediaDrm;
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (mMediaDrm.mNativeContext == 0L)
      {
        Log.w("MediaDrm", "MediaDrm went away with unhandled events");
        return;
      }
      Object localObject1;
      Object localObject2;
      switch (what)
      {
      default: 
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Unknown message type ");
        ((StringBuilder)localObject1).append(what);
        Log.e("MediaDrm", ((StringBuilder)localObject1).toString());
        return;
      case 202: 
        if ((mOnKeyStatusChangeListener != null) && (obj != null) && ((obj instanceof Parcel)))
        {
          paramMessage = (Parcel)obj;
          localObject1 = paramMessage.createByteArray();
          if (localObject1.length > 0)
          {
            localObject2 = MediaDrm.this.keyStatusListFromParcel(paramMessage);
            boolean bool;
            if (paramMessage.readInt() != 0) {
              bool = true;
            } else {
              bool = false;
            }
            Log.i("MediaDrm", "Drm key status changed");
            mOnKeyStatusChangeListener.onKeyStatusChange(mMediaDrm, (byte[])localObject1, (List)localObject2, bool);
          }
        }
        return;
      case 201: 
        if ((mOnExpirationUpdateListener != null) && (obj != null) && ((obj instanceof Parcel)))
        {
          localObject1 = (Parcel)obj;
          paramMessage = ((Parcel)localObject1).createByteArray();
          if (paramMessage.length > 0)
          {
            long l = ((Parcel)localObject1).readLong();
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Drm key expiration update: ");
            ((StringBuilder)localObject1).append(l);
            Log.i("MediaDrm", ((StringBuilder)localObject1).toString());
            mOnExpirationUpdateListener.onExpirationUpdate(mMediaDrm, paramMessage, l);
          }
        }
        return;
      }
      if ((mOnEventListener != null) && (obj != null) && ((obj instanceof Parcel)))
      {
        Object localObject3 = (Parcel)obj;
        localObject2 = ((Parcel)localObject3).createByteArray();
        localObject1 = localObject2;
        if (localObject2.length == 0) {
          localObject1 = null;
        }
        localObject3 = ((Parcel)localObject3).createByteArray();
        localObject2 = localObject3;
        if (localObject3.length == 0) {
          localObject2 = null;
        }
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Drm event (");
        ((StringBuilder)localObject3).append(arg1);
        ((StringBuilder)localObject3).append(",");
        ((StringBuilder)localObject3).append(arg2);
        ((StringBuilder)localObject3).append(")");
        Log.i("MediaDrm", ((StringBuilder)localObject3).toString());
        mOnEventListener.onEvent(mMediaDrm, (byte[])localObject1, arg1, arg2, (byte[])localObject2);
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface HdcpLevel {}
  
  public static final class KeyRequest
  {
    public static final int REQUEST_TYPE_INITIAL = 0;
    public static final int REQUEST_TYPE_NONE = 3;
    public static final int REQUEST_TYPE_RELEASE = 2;
    public static final int REQUEST_TYPE_RENEWAL = 1;
    public static final int REQUEST_TYPE_UPDATE = 4;
    private byte[] mData;
    private String mDefaultUrl;
    private int mRequestType;
    
    KeyRequest() {}
    
    public byte[] getData()
    {
      if (mData != null) {
        return mData;
      }
      throw new RuntimeException("KeyRequest is not initialized");
    }
    
    public String getDefaultUrl()
    {
      if (mDefaultUrl != null) {
        return mDefaultUrl;
      }
      throw new RuntimeException("KeyRequest is not initialized");
    }
    
    public int getRequestType()
    {
      return mRequestType;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface RequestType {}
  }
  
  public static final class KeyStatus
  {
    public static final int STATUS_EXPIRED = 1;
    public static final int STATUS_INTERNAL_ERROR = 4;
    public static final int STATUS_OUTPUT_NOT_ALLOWED = 2;
    public static final int STATUS_PENDING = 3;
    public static final int STATUS_USABLE = 0;
    private final byte[] mKeyId;
    private final int mStatusCode;
    
    KeyStatus(byte[] paramArrayOfByte, int paramInt)
    {
      mKeyId = paramArrayOfByte;
      mStatusCode = paramInt;
    }
    
    public byte[] getKeyId()
    {
      return mKeyId;
    }
    
    public int getStatusCode()
    {
      return mStatusCode;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface KeyStatusCode {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface KeyType {}
  
  public static final class MediaDrmStateException
    extends IllegalStateException
  {
    private final String mDiagnosticInfo;
    private final int mErrorCode;
    
    public MediaDrmStateException(int paramInt, String paramString)
    {
      super();
      mErrorCode = paramInt;
      if (paramInt < 0) {
        paramString = "neg_";
      } else {
        paramString = "";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("android.media.MediaDrm.error_");
      localStringBuilder.append(paramString);
      localStringBuilder.append(Math.abs(paramInt));
      mDiagnosticInfo = localStringBuilder.toString();
    }
    
    public String getDiagnosticInfo()
    {
      return mDiagnosticInfo;
    }
    
    public int getErrorCode()
    {
      return mErrorCode;
    }
  }
  
  public static final class MetricsConstants
  {
    public static final String CLOSE_SESSION_ERROR_COUNT = "drm.mediadrm.close_session.error.count";
    public static final String CLOSE_SESSION_ERROR_LIST = "drm.mediadrm.close_session.error.list";
    public static final String CLOSE_SESSION_OK_COUNT = "drm.mediadrm.close_session.ok.count";
    public static final String EVENT_KEY_EXPIRED_COUNT = "drm.mediadrm.event.KEY_EXPIRED.count";
    public static final String EVENT_KEY_NEEDED_COUNT = "drm.mediadrm.event.KEY_NEEDED.count";
    public static final String EVENT_PROVISION_REQUIRED_COUNT = "drm.mediadrm.event.PROVISION_REQUIRED.count";
    public static final String EVENT_SESSION_RECLAIMED_COUNT = "drm.mediadrm.event.SESSION_RECLAIMED.count";
    public static final String EVENT_VENDOR_DEFINED_COUNT = "drm.mediadrm.event.VENDOR_DEFINED.count";
    public static final String GET_DEVICE_UNIQUE_ID_ERROR_COUNT = "drm.mediadrm.get_device_unique_id.error.count";
    public static final String GET_DEVICE_UNIQUE_ID_ERROR_LIST = "drm.mediadrm.get_device_unique_id.error.list";
    public static final String GET_DEVICE_UNIQUE_ID_OK_COUNT = "drm.mediadrm.get_device_unique_id.ok.count";
    public static final String GET_KEY_REQUEST_ERROR_COUNT = "drm.mediadrm.get_key_request.error.count";
    public static final String GET_KEY_REQUEST_ERROR_LIST = "drm.mediadrm.get_key_request.error.list";
    public static final String GET_KEY_REQUEST_OK_COUNT = "drm.mediadrm.get_key_request.ok.count";
    public static final String GET_KEY_REQUEST_OK_TIME_MICROS = "drm.mediadrm.get_key_request.ok.average_time_micros";
    public static final String GET_PROVISION_REQUEST_ERROR_COUNT = "drm.mediadrm.get_provision_request.error.count";
    public static final String GET_PROVISION_REQUEST_ERROR_LIST = "drm.mediadrm.get_provision_request.error.list";
    public static final String GET_PROVISION_REQUEST_OK_COUNT = "drm.mediadrm.get_provision_request.ok.count";
    public static final String KEY_STATUS_EXPIRED_COUNT = "drm.mediadrm.key_status.EXPIRED.count";
    public static final String KEY_STATUS_INTERNAL_ERROR_COUNT = "drm.mediadrm.key_status.INTERNAL_ERROR.count";
    public static final String KEY_STATUS_OUTPUT_NOT_ALLOWED_COUNT = "drm.mediadrm.key_status_change.OUTPUT_NOT_ALLOWED.count";
    public static final String KEY_STATUS_PENDING_COUNT = "drm.mediadrm.key_status_change.PENDING.count";
    public static final String KEY_STATUS_USABLE_COUNT = "drm.mediadrm.key_status_change.USABLE.count";
    public static final String OPEN_SESSION_ERROR_COUNT = "drm.mediadrm.open_session.error.count";
    public static final String OPEN_SESSION_ERROR_LIST = "drm.mediadrm.open_session.error.list";
    public static final String OPEN_SESSION_OK_COUNT = "drm.mediadrm.open_session.ok.count";
    public static final String PROVIDE_KEY_RESPONSE_ERROR_COUNT = "drm.mediadrm.provide_key_response.error.count";
    public static final String PROVIDE_KEY_RESPONSE_ERROR_LIST = "drm.mediadrm.provide_key_response.error.list";
    public static final String PROVIDE_KEY_RESPONSE_OK_COUNT = "drm.mediadrm.provide_key_response.ok.count";
    public static final String PROVIDE_KEY_RESPONSE_OK_TIME_MICROS = "drm.mediadrm.provide_key_response.ok.average_time_micros";
    public static final String PROVIDE_PROVISION_RESPONSE_ERROR_COUNT = "drm.mediadrm.provide_provision_response.error.count";
    public static final String PROVIDE_PROVISION_RESPONSE_ERROR_LIST = "drm.mediadrm.provide_provision_response.error.list";
    public static final String PROVIDE_PROVISION_RESPONSE_OK_COUNT = "drm.mediadrm.provide_provision_response.ok.count";
    public static final String SESSION_END_TIMES_MS = "drm.mediadrm.session_end_times_ms";
    public static final String SESSION_START_TIMES_MS = "drm.mediadrm.session_start_times_ms";
    
    private MetricsConstants() {}
  }
  
  public static abstract interface OnEventListener
  {
    public abstract void onEvent(MediaDrm paramMediaDrm, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2);
  }
  
  public static abstract interface OnExpirationUpdateListener
  {
    public abstract void onExpirationUpdate(MediaDrm paramMediaDrm, byte[] paramArrayOfByte, long paramLong);
  }
  
  public static abstract interface OnKeyStatusChangeListener
  {
    public abstract void onKeyStatusChange(MediaDrm paramMediaDrm, byte[] paramArrayOfByte, List<MediaDrm.KeyStatus> paramList, boolean paramBoolean);
  }
  
  public static final class ProvisionRequest
  {
    private byte[] mData;
    private String mDefaultUrl;
    
    ProvisionRequest() {}
    
    public byte[] getData()
    {
      if (mData != null) {
        return mData;
      }
      throw new RuntimeException("ProvisionRequest is not initialized");
    }
    
    public String getDefaultUrl()
    {
      if (mDefaultUrl != null) {
        return mDefaultUrl;
      }
      throw new RuntimeException("ProvisionRequest is not initialized");
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SecurityLevel {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StringProperty {}
}
