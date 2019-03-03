package org.codeaurora.ims.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import java.io.File;
import org.codeaurora.ims.QtiImsException;

public class QtiImsExtUtils
{
  public static final String ACTION_VOPS_SSAC_STATUS = "org.codeaurora.VOIP_VOPS_SSAC_STATUS";
  public static final String CARRIER_ONE_DEFAULT_MCC_MNC = "405854";
  public static final String EXTRA_SSAC = "Ssac";
  public static final String EXTRA_VOPS = "Vops";
  private static String LOG_TAG = "QtiImsExtUtils";
  public static final String PROPERTY_RADIO_ATEL_CARRIER = "persist.vendor.radio.atel.carrier";
  public static final int QTI_IMS_ASSURED_TRANSFER = 2;
  public static final int QTI_IMS_BLIND_TRANSFER = 1;
  public static final String QTI_IMS_CALL_DEFLECT_NUMBER = "ims_call_deflect_number";
  public static final int QTI_IMS_CONSULTATIVE_TRANSFER = 4;
  public static final int QTI_IMS_HO_DISABLE_ALL = 2;
  public static final int QTI_IMS_HO_ENABLED_WLAN_TO_WWAN_ONLY = 3;
  public static final int QTI_IMS_HO_ENABLED_WWAN_TO_WLAN_ONLY = 4;
  public static final int QTI_IMS_HO_ENABLE_ALL = 1;
  public static final int QTI_IMS_HO_INVALID = 0;
  public static final String QTI_IMS_INCOMING_CONF_EXTRA_KEY = "incomingConference";
  public static final String QTI_IMS_PHONE_ID_EXTRA_KEY = "phoneId";
  public static final int QTI_IMS_REQUEST_ERROR = 1;
  public static final int QTI_IMS_REQUEST_SUCCESS = 0;
  public static final int QTI_IMS_SMS_APP_INVALID = -1;
  public static final int QTI_IMS_SMS_APP_NOT_RCS = 2;
  public static final int QTI_IMS_SMS_APP_RCS = 1;
  public static final int QTI_IMS_SMS_APP_SELECTION_NOT_ALLOWED = 0;
  public static final String QTI_IMS_STATIC_IMAGE_SETTING = "ims_vt_call_static_image";
  public static final String QTI_IMS_TRANSFER_EXTRA_KEY = "transferType";
  public static final int QTI_IMS_VOLTE_PREF_OFF = 0;
  public static final int QTI_IMS_VOLTE_PREF_ON = 1;
  public static final int QTI_IMS_VOLTE_PREF_UNKNOWN = 2;
  public static final int QTI_IMS_VVM_APP_INVALID = -1;
  public static final int QTI_IMS_VVM_APP_NOT_RCS = 0;
  public static final int QTI_IMS_VVM_APP_RCS = 1;
  
  private QtiImsExtUtils() {}
  
  public static boolean allowVideoCallsInLowBattery(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "allow_video_call_in_low_battery");
  }
  
  private static int calculateInSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
  {
    int i = outHeight;
    int j = outWidth;
    int k = 1;
    int m = 1;
    paramOptions = LOG_TAG;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("calculateInSampleSize: reqWidth = ");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(" reqHeight = ");
    ((StringBuilder)localObject).append(paramInt2);
    ((StringBuilder)localObject).append(" raw width = ");
    ((StringBuilder)localObject).append(j);
    ((StringBuilder)localObject).append(" raw height = ");
    ((StringBuilder)localObject).append(i);
    Log.d(paramOptions, ((StringBuilder)localObject).toString());
    if ((i > paramInt2) || (j > paramInt1))
    {
      i /= 2;
      j /= 2;
      for (;;)
      {
        k = m;
        if (i / m <= paramInt2) {
          break;
        }
        k = m;
        if (j / m <= paramInt1) {
          break;
        }
        m *= 2;
      }
    }
    localObject = LOG_TAG;
    paramOptions = new StringBuilder();
    paramOptions.append("calculateInSampleSize: inSampleSize = ");
    paramOptions.append(k);
    Log.d((String)localObject, paramOptions.toString());
    return k;
  }
  
  public static boolean canHoldVideoCall(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "allow_holding_video_call");
  }
  
  public static Bitmap decodeImage(Resources paramResources, int paramInt1, int paramInt2, int paramInt3)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    inPreferredConfig = Bitmap.Config.ARGB_8888;
    inJustDecodeBounds = true;
    BitmapFactory.decodeResource(paramResources, paramInt1, localOptions);
    inSampleSize = calculateInSampleSize(localOptions, paramInt2, paramInt3);
    inJustDecodeBounds = false;
    return scaleImage(BitmapFactory.decodeResource(paramResources, paramInt1, localOptions), paramInt2, paramInt3);
  }
  
  public static Bitmap decodeImage(String paramString, int paramInt1, int paramInt2)
  {
    if (paramString == null) {
      return null;
    }
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    inPreferredConfig = Bitmap.Config.ARGB_8888;
    inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramString, localOptions);
    inSampleSize = calculateInSampleSize(localOptions, paramInt1, paramInt2);
    inJustDecodeBounds = false;
    return scaleImage(BitmapFactory.decodeFile(paramString, localOptions), paramInt1, paramInt2);
  }
  
  public static String getCallDeflectNumber(ContentResolver paramContentResolver)
  {
    String str = Settings.Global.getString(paramContentResolver, "ims_call_deflect_number");
    paramContentResolver = str;
    if (str != null)
    {
      paramContentResolver = str;
      if (str.isEmpty()) {
        paramContentResolver = null;
      }
    }
    return paramContentResolver;
  }
  
  private static PersistableBundle getConfigForPhoneId(Context paramContext, int paramInt)
  {
    if (paramContext == null)
    {
      Log.e(LOG_TAG, "getConfigForPhoneId context is null");
      return null;
    }
    CarrierConfigManager localCarrierConfigManager = (CarrierConfigManager)paramContext.getSystemService("carrier_config");
    if (localCarrierConfigManager == null)
    {
      Log.e(LOG_TAG, "getConfigForPhoneId configManager is null");
      return null;
    }
    if (paramInt == -1)
    {
      Log.e(LOG_TAG, "getConfigForPhoneId phoneId is invalid");
      return null;
    }
    paramInt = getSubscriptionIdFromPhoneId(paramContext, paramInt);
    if (!SubscriptionManager.isValidSubscriptionId(paramInt))
    {
      Log.e(LOG_TAG, "getConfigForPhoneId subId is invalid");
      return null;
    }
    return localCarrierConfigManager.getConfigForSubId(paramInt);
  }
  
  public static int getRttMode(Context paramContext)
  {
    return Settings.Global.getInt(paramContext.getContentResolver(), "rtt_mode", 0);
  }
  
  public static int getRttOperatingMode(Context paramContext)
  {
    return SystemProperties.getInt("persist.vendor.radio.rtt.operval", 0);
  }
  
  public static Bitmap getStaticImage(Context paramContext, int paramInt1, int paramInt2)
    throws QtiImsException
  {
    String str = getStaticImageUriStr(paramContext.getContentResolver());
    paramContext = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getStaticImage: uriStr = ");
    localStringBuilder.append(str);
    localStringBuilder.append(" reqWidth = ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" reqHeight = ");
    localStringBuilder.append(paramInt2);
    Log.d(paramContext, localStringBuilder.toString());
    if (isValidUriStr(str))
    {
      paramContext = decodeImage(str, paramInt1, paramInt2);
      if (paramContext != null) {
        return paramContext;
      }
      throw new QtiImsException("image decoding error");
    }
    throw new QtiImsException("invalid file path");
  }
  
  public static String getStaticImageUriStr(ContentResolver paramContentResolver)
  {
    return Settings.Global.getString(paramContentResolver, "ims_vt_call_static_image");
  }
  
  private static int getSubscriptionIdFromPhoneId(Context paramContext, int paramInt)
  {
    paramContext = SubscriptionManager.from(paramContext);
    if (paramContext == null) {
      return -1;
    }
    paramContext = paramContext.getActiveSubscriptionInfoForSimSlotIndex(paramInt);
    if (paramContext == null) {
      return -1;
    }
    return paramContext.getSubscriptionId();
  }
  
  public static boolean isCallTransferEnabled(Context paramContext)
  {
    return SystemProperties.getBoolean("persist.vendor.radio.ims_call_transfer", false);
  }
  
  public static boolean isCancelModifyCallSupported(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "support_cancel_modify_call");
  }
  
  public static boolean isCarrierConfigEnabled(int paramInt, Context paramContext, String paramString)
  {
    return QtiCarrierConfigHelper.getInstance().getBoolean(paramContext, paramInt, paramString);
  }
  
  public static boolean isCarrierOneSupported()
  {
    return "405854".equals(SystemProperties.get("persist.vendor.radio.atel.carrier"));
  }
  
  public static boolean isCsRetryConfigEnabled(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "config_carrier_cs_retry_available");
  }
  
  public static boolean isRttAutoUpgradeSupported(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "carrier_rtt_auto_upgrade");
  }
  
  public static boolean isRttDowngradeSupported(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "carrier_rtt_downgrade_supported");
  }
  
  public static boolean isRttOn(Context paramContext)
  {
    boolean bool;
    if (getRttMode(paramContext) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isRttSupported(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "carrier_rtt_supported");
  }
  
  public static boolean isRttSupportedOnVtCalls(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "carrier_rtt_supported_on_vtcalls");
  }
  
  public static boolean isRttUpgradeSupported(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "carrier_rtt_upgrade_supported");
  }
  
  private static boolean isValidUriStr(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (!paramString.isEmpty()) && (new File(paramString).exists())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isVoWiFiCallQualityEnabled(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "vowifi_call_quality");
  }
  
  private static Bitmap scaleImage(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    if (paramBitmap == null) {
      return null;
    }
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    float f1 = paramInt1 / i;
    float f2 = paramInt2 / j;
    Object localObject = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("scaleImage bitmap w = ");
    localStringBuilder.append(i);
    localStringBuilder.append(" bitmap h = ");
    localStringBuilder.append(j);
    Log.d((String)localObject, localStringBuilder.toString());
    localObject = new Matrix();
    ((Matrix)localObject).postScale(f1, f2);
    return Bitmap.createBitmap(paramBitmap, 0, 0, i, j, (Matrix)localObject, false);
  }
  
  public static void setCallDeflectNumber(ContentResolver paramContentResolver, String paramString)
  {
    String str = paramString;
    if ((paramString == null) || (paramString.isEmpty())) {
      str = "";
    }
    Settings.Global.putString(paramContentResolver, "ims_call_deflect_number", str);
  }
  
  public static void setRttMode(boolean paramBoolean, Context paramContext)
  {
    paramContext = paramContext.getContentResolver();
    Settings.Global.putInt(paramContext, "rtt_mode", paramBoolean);
  }
  
  public static boolean shallHidePreviewInVtConference(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "config_hide_preview_in_vt_confcall");
  }
  
  public static boolean shallRemoveModifyCallCapability(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "remove_modify_call_capability");
  }
  
  public static boolean shallRemoveModifyCallCapability(Context paramContext)
  {
    return shallRemoveModifyCallCapability(-1, paramContext);
  }
  
  public static boolean shallShowStaticImageUi(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "show_static_image_ui");
  }
  
  public static boolean shallShowVideoQuality(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "show_video_quality_ui");
  }
  
  public static boolean shallTransmitStaticImage(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "transmit_static_image");
  }
  
  public static boolean useCustomVideoUi(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "use_custom_video_ui");
  }
  
  public static boolean useExt(int paramInt, Context paramContext)
  {
    return isCarrierConfigEnabled(paramInt, paramContext, "video_call_use_ext");
  }
  
  public static class VideoQualityFeatureValuesConstants
  {
    public static final int HIGH = 2;
    public static final int LOW = 0;
    public static final int MEDIUM = 1;
    
    public VideoQualityFeatureValuesConstants() {}
  }
}
