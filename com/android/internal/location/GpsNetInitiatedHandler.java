package com.android.internal.location;

import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.INetInitiatedListener;
import android.location.LocationManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.app.NetInitiatedActivity;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;

public class GpsNetInitiatedHandler
{
  public static final String ACTION_NI_VERIFY = "android.intent.action.NETWORK_INITIATED_VERIFY";
  private static final boolean DEBUG = true;
  public static final int GPS_ENC_NONE = 0;
  public static final int GPS_ENC_SUPL_GSM_DEFAULT = 1;
  public static final int GPS_ENC_SUPL_UCS2 = 3;
  public static final int GPS_ENC_SUPL_UTF8 = 2;
  public static final int GPS_ENC_UNKNOWN = -1;
  public static final int GPS_NI_NEED_NOTIFY = 1;
  public static final int GPS_NI_NEED_VERIFY = 2;
  public static final int GPS_NI_PRIVACY_OVERRIDE = 4;
  public static final int GPS_NI_RESPONSE_ACCEPT = 1;
  public static final int GPS_NI_RESPONSE_DENY = 2;
  public static final int GPS_NI_RESPONSE_IGNORE = 4;
  public static final int GPS_NI_RESPONSE_NORESP = 3;
  public static final int GPS_NI_TYPE_EMERGENCY_SUPL = 4;
  public static final int GPS_NI_TYPE_UMTS_CTRL_PLANE = 3;
  public static final int GPS_NI_TYPE_UMTS_SUPL = 2;
  public static final int GPS_NI_TYPE_VOICE = 1;
  public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
  public static final String NI_EXTRA_CMD_RESPONSE = "response";
  public static final String NI_INTENT_KEY_DEFAULT_RESPONSE = "default_resp";
  public static final String NI_INTENT_KEY_MESSAGE = "message";
  public static final String NI_INTENT_KEY_NOTIF_ID = "notif_id";
  public static final String NI_INTENT_KEY_TIMEOUT = "timeout";
  public static final String NI_INTENT_KEY_TITLE = "title";
  public static final String NI_RESPONSE_EXTRA_CMD = "send_ni_response";
  private static final String TAG = "GpsNetInitiatedHandler";
  private static final boolean VERBOSE = false;
  private static boolean mIsHexInput = true;
  private final BroadcastReceiver mBroadcastReciever = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      if (paramAnonymousContext.equals("android.intent.action.NEW_OUTGOING_CALL"))
      {
        paramAnonymousContext = paramAnonymousIntent.getStringExtra("android.intent.extra.PHONE_NUMBER");
        setInEmergency(PhoneNumberUtils.isEmergencyNumber(paramAnonymousContext));
        paramAnonymousContext = new StringBuilder();
        paramAnonymousContext.append("ACTION_NEW_OUTGOING_CALL - ");
        paramAnonymousContext.append(getInEmergency());
        Log.v("GpsNetInitiatedHandler", paramAnonymousContext.toString());
      }
      else if (paramAnonymousContext.equals("android.location.MODE_CHANGED"))
      {
        updateLocationMode();
        paramAnonymousContext = new StringBuilder();
        paramAnonymousContext.append("location enabled :");
        paramAnonymousContext.append(getLocationEnabled());
        Log.d("GpsNetInitiatedHandler", paramAnonymousContext.toString());
      }
    }
  };
  private final Context mContext;
  private volatile boolean mIsInEmergency;
  private volatile boolean mIsLocationEnabled = false;
  private volatile boolean mIsSuplEsEnabled;
  private final LocationManager mLocationManager;
  private final INetInitiatedListener mNetInitiatedListener;
  private Notification.Builder mNiNotificationBuilder;
  private final PhoneStateListener mPhoneStateListener;
  private boolean mPlaySounds = false;
  private boolean mPopupImmediately = true;
  private final TelephonyManager mTelephonyManager;
  
  public GpsNetInitiatedHandler(Context paramContext, INetInitiatedListener paramINetInitiatedListener, boolean paramBoolean)
  {
    mContext = paramContext;
    if (paramINetInitiatedListener != null)
    {
      mNetInitiatedListener = paramINetInitiatedListener;
      setSuplEsEnabled(paramBoolean);
      mLocationManager = ((LocationManager)paramContext.getSystemService("location"));
      updateLocationMode();
      mTelephonyManager = ((TelephonyManager)paramContext.getSystemService("phone"));
      mPhoneStateListener = new PhoneStateListener()
      {
        public void onCallStateChanged(int paramAnonymousInt, String paramAnonymousString)
        {
          paramAnonymousString = new StringBuilder();
          paramAnonymousString.append("onCallStateChanged(): state is ");
          paramAnonymousString.append(paramAnonymousInt);
          Log.d("GpsNetInitiatedHandler", paramAnonymousString.toString());
          if (paramAnonymousInt == 0) {
            setInEmergency(false);
          }
        }
      };
      mTelephonyManager.listen(mPhoneStateListener, 32);
      paramContext = new IntentFilter();
      paramContext.addAction("android.intent.action.NEW_OUTGOING_CALL");
      paramContext.addAction("android.location.MODE_CHANGED");
      mContext.registerReceiver(mBroadcastReciever, paramContext);
      return;
    }
    throw new IllegalArgumentException("netInitiatedListener is null");
  }
  
  static String decodeGSMPackedString(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    int j = i * 8 / 7;
    int k = j;
    if (i % 7 == 0)
    {
      k = j;
      if (i > 0)
      {
        k = j;
        if (paramArrayOfByte[(i - 1)] >> 1 == 0) {
          k = j - 1;
        }
      }
    }
    String str = GsmAlphabet.gsm7BitPackedToString(paramArrayOfByte, 0, k);
    paramArrayOfByte = str;
    if (str == null)
    {
      Log.e("GpsNetInitiatedHandler", "Decoding of GSM packed string failed");
      paramArrayOfByte = "";
    }
    return paramArrayOfByte;
  }
  
  private static String decodeString(String paramString, boolean paramBoolean, int paramInt)
  {
    String str = paramString;
    Object localObject = stringToByteArray(paramString, paramBoolean);
    switch (paramInt)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown encoding ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(" for NI text ");
      ((StringBuilder)localObject).append(paramString);
      Log.e("GpsNetInitiatedHandler", ((StringBuilder)localObject).toString());
      paramString = str;
      break;
    case 3: 
      paramString = decodeUCS2String((byte[])localObject);
      break;
    case 2: 
      paramString = decodeUTF8String((byte[])localObject);
      break;
    case 1: 
      paramString = decodeGSMPackedString((byte[])localObject);
      break;
    case 0: 
      break;
    }
    return paramString;
  }
  
  static String decodeUCS2String(byte[] paramArrayOfByte)
  {
    try
    {
      paramArrayOfByte = new String(paramArrayOfByte, "UTF-16");
      return paramArrayOfByte;
    }
    catch (UnsupportedEncodingException paramArrayOfByte)
    {
      throw new AssertionError();
    }
  }
  
  static String decodeUTF8String(byte[] paramArrayOfByte)
  {
    try
    {
      paramArrayOfByte = new String(paramArrayOfByte, "UTF-8");
      return paramArrayOfByte;
    }
    catch (UnsupportedEncodingException paramArrayOfByte)
    {
      throw new AssertionError();
    }
  }
  
  private static String getDialogMessage(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return getNotifMessage(paramGpsNiNotification, paramContext);
  }
  
  public static String getDialogTitle(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return getNotifTitle(paramGpsNiNotification, paramContext);
  }
  
  private Intent getDlgIntent(GpsNiNotification paramGpsNiNotification)
  {
    Intent localIntent = new Intent();
    String str1 = getDialogTitle(paramGpsNiNotification, mContext);
    String str2 = getDialogMessage(paramGpsNiNotification, mContext);
    localIntent.setFlags(268468224);
    localIntent.setClass(mContext, NetInitiatedActivity.class);
    localIntent.putExtra("notif_id", notificationId);
    localIntent.putExtra("title", str1);
    localIntent.putExtra("message", str2);
    localIntent.putExtra("timeout", timeout);
    localIntent.putExtra("default_resp", defaultResponse);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("generateIntent, title: ");
    localStringBuilder.append(str1);
    localStringBuilder.append(", message: ");
    localStringBuilder.append(str2);
    localStringBuilder.append(", timeout: ");
    localStringBuilder.append(timeout);
    Log.d("GpsNetInitiatedHandler", localStringBuilder.toString());
    return localIntent;
  }
  
  private static String getNotifMessage(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return String.format(paramContext.getString(17040066), new Object[] { decodeString(requestorId, mIsHexInput, requestorIdEncoding), decodeString(text, mIsHexInput, textEncoding) });
  }
  
  private static String getNotifTicker(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return String.format(paramContext.getString(17040067), new Object[] { decodeString(requestorId, mIsHexInput, requestorIdEncoding), decodeString(text, mIsHexInput, textEncoding) });
  }
  
  private static String getNotifTitle(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return String.format(paramContext.getString(17040068), new Object[0]);
  }
  
  private void handleNi(GpsNiNotification paramGpsNiNotification)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("in handleNi () : needNotify: ");
    localStringBuilder.append(needNotify);
    localStringBuilder.append(" needVerify: ");
    localStringBuilder.append(needVerify);
    localStringBuilder.append(" privacyOverride: ");
    localStringBuilder.append(privacyOverride);
    localStringBuilder.append(" mPopupImmediately: ");
    localStringBuilder.append(mPopupImmediately);
    localStringBuilder.append(" mInEmergency: ");
    localStringBuilder.append(getInEmergency());
    Log.d("GpsNetInitiatedHandler", localStringBuilder.toString());
    if ((!getLocationEnabled()) && (!getInEmergency())) {
      try
      {
        mNetInitiatedListener.sendNiResponse(notificationId, 4);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("GpsNetInitiatedHandler", "RemoteException in sendNiResponse");
      }
    }
    if (needNotify) {
      if ((needVerify) && (mPopupImmediately)) {
        openNiDialog(paramGpsNiNotification);
      } else {
        setNiNotification(paramGpsNiNotification);
      }
    }
    if ((!needVerify) || (privacyOverride)) {
      try
      {
        mNetInitiatedListener.sendNiResponse(notificationId, 1);
      }
      catch (RemoteException paramGpsNiNotification)
      {
        Log.e("GpsNetInitiatedHandler", "RemoteException in sendNiResponse");
      }
    }
  }
  
  private void handleNiInEs(GpsNiNotification paramGpsNiNotification)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("in handleNiInEs () : niType: ");
    localStringBuilder.append(niType);
    localStringBuilder.append(" notificationId: ");
    localStringBuilder.append(notificationId);
    Log.d("GpsNetInitiatedHandler", localStringBuilder.toString());
    int i;
    if (niType == 4) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != getInEmergency()) {
      try
      {
        mNetInitiatedListener.sendNiResponse(notificationId, 4);
      }
      catch (RemoteException paramGpsNiNotification)
      {
        Log.e("GpsNetInitiatedHandler", "RemoteException in sendNiResponse");
      }
    } else {
      handleNi(paramGpsNiNotification);
    }
  }
  
  private void openNiDialog(GpsNiNotification paramGpsNiNotification)
  {
    Intent localIntent = getDlgIntent(paramGpsNiNotification);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("openNiDialog, notifyId: ");
    localStringBuilder.append(notificationId);
    localStringBuilder.append(", requestorId: ");
    localStringBuilder.append(requestorId);
    localStringBuilder.append(", text: ");
    localStringBuilder.append(text);
    Log.d("GpsNetInitiatedHandler", localStringBuilder.toString());
    mContext.startActivity(localIntent);
  }
  
  private void setNiNotification(GpsNiNotification paramGpsNiNotification)
  {
    try
    {
      NotificationManager localNotificationManager = (NotificationManager)mContext.getSystemService("notification");
      if (localNotificationManager == null) {
        return;
      }
      String str1 = getNotifTitle(paramGpsNiNotification, mContext);
      String str2 = getNotifMessage(paramGpsNiNotification, mContext);
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("setNiNotification, notifyId: ");
      ((StringBuilder)localObject).append(notificationId);
      ((StringBuilder)localObject).append(", title: ");
      ((StringBuilder)localObject).append(str1);
      ((StringBuilder)localObject).append(", message: ");
      ((StringBuilder)localObject).append(str2);
      Log.d("GpsNetInitiatedHandler", ((StringBuilder)localObject).toString());
      if (mNiNotificationBuilder == null)
      {
        localObject = new android/app/Notification$Builder;
        ((Notification.Builder)localObject).<init>(mContext, SystemNotificationChannels.NETWORK_ALERTS);
        mNiNotificationBuilder = ((Notification.Builder)localObject).setSmallIcon(17303766).setWhen(0L).setOngoing(true).setAutoCancel(true).setColor(mContext.getColor(17170876));
      }
      if (mPlaySounds) {
        mNiNotificationBuilder.setDefaults(1);
      } else {
        mNiNotificationBuilder.setDefaults(0);
      }
      if (!mPopupImmediately) {
        localObject = getDlgIntent(paramGpsNiNotification);
      } else {
        localObject = new Intent();
      }
      localObject = PendingIntent.getBroadcast(mContext, 0, (Intent)localObject, 0);
      mNiNotificationBuilder.setTicker(getNotifTicker(paramGpsNiNotification, mContext)).setContentTitle(str1).setContentText(str2).setContentIntent((PendingIntent)localObject);
      localNotificationManager.notifyAsUser(null, notificationId, mNiNotificationBuilder.build(), UserHandle.ALL);
      return;
    }
    finally {}
  }
  
  static byte[] stringToByteArray(String paramString, boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = paramString.length() / 2;
    } else {
      i = paramString.length();
    }
    byte[] arrayOfByte = new byte[i];
    int j = 0;
    int k = 0;
    if (paramBoolean) {
      while (k < i)
      {
        arrayOfByte[k] = ((byte)(byte)Integer.parseInt(paramString.substring(k * 2, k * 2 + 2), 16));
        k++;
      }
    }
    for (k = j; k < i; k++) {
      arrayOfByte[k] = ((byte)(byte)paramString.charAt(k));
    }
    return arrayOfByte;
  }
  
  public boolean getInEmergency()
  {
    boolean bool = mTelephonyManager.getEmergencyCallbackMode();
    if ((!mIsInEmergency) && (!bool)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean getLocationEnabled()
  {
    return mIsLocationEnabled;
  }
  
  public boolean getSuplEsEnabled()
  {
    return mIsSuplEsEnabled;
  }
  
  public void handleNiNotification(GpsNiNotification paramGpsNiNotification)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("in handleNiNotification () : notificationId: ");
    localStringBuilder.append(notificationId);
    localStringBuilder.append(" requestorId: ");
    localStringBuilder.append(requestorId);
    localStringBuilder.append(" text: ");
    localStringBuilder.append(text);
    localStringBuilder.append(" mIsSuplEsEnabled");
    localStringBuilder.append(getSuplEsEnabled());
    localStringBuilder.append(" mIsLocationEnabled");
    localStringBuilder.append(getLocationEnabled());
    Log.d("GpsNetInitiatedHandler", localStringBuilder.toString());
    if (getSuplEsEnabled()) {
      handleNiInEs(paramGpsNiNotification);
    } else {
      handleNi(paramGpsNiNotification);
    }
  }
  
  public void setInEmergency(boolean paramBoolean)
  {
    mIsInEmergency = paramBoolean;
  }
  
  public void setSuplEsEnabled(boolean paramBoolean)
  {
    mIsSuplEsEnabled = paramBoolean;
  }
  
  public void updateLocationMode()
  {
    mIsLocationEnabled = mLocationManager.isProviderEnabled("gps");
  }
  
  public static class GpsNiNotification
  {
    public int defaultResponse;
    public boolean needNotify;
    public boolean needVerify;
    public int niType;
    public int notificationId;
    public boolean privacyOverride;
    public String requestorId;
    public int requestorIdEncoding;
    public String text;
    public int textEncoding;
    public int timeout;
    
    public GpsNiNotification() {}
  }
  
  public static class GpsNiResponse
  {
    int userResponse;
    
    public GpsNiResponse() {}
  }
}
