package android.telephony;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SeempLog;
import com.android.internal.telephony.IMms;
import com.android.internal.telephony.IMms.Stub;
import com.android.internal.telephony.ISms;
import com.android.internal.telephony.ISms.Stub;
import com.android.internal.telephony.SmsRawData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class SmsManager
{
  private static final boolean ASUS_DEBUG_PRIVACY;
  public static final int CDMA_SMS_RECORD_LENGTH = 255;
  public static final int CELL_BROADCAST_RAN_TYPE_CDMA = 1;
  public static final int CELL_BROADCAST_RAN_TYPE_GSM = 0;
  private static final int DEFAULT_SUBSCRIPTION_ID = -1002;
  private static String DIALOG_TYPE_KEY;
  public static final String EXTRA_MMS_DATA = "android.telephony.extra.MMS_DATA";
  public static final String EXTRA_MMS_HTTP_STATUS = "android.telephony.extra.MMS_HTTP_STATUS";
  public static final String MESSAGE_STATUS_READ = "read";
  public static final String MESSAGE_STATUS_SEEN = "seen";
  public static final String MMS_CONFIG_ALIAS_ENABLED = "aliasEnabled";
  public static final String MMS_CONFIG_ALIAS_MAX_CHARS = "aliasMaxChars";
  public static final String MMS_CONFIG_ALIAS_MIN_CHARS = "aliasMinChars";
  public static final String MMS_CONFIG_ALLOW_ATTACH_AUDIO = "allowAttachAudio";
  public static final String MMS_CONFIG_APPEND_TRANSACTION_ID = "enabledTransID";
  public static final String MMS_CONFIG_CLOSE_CONNECTION = "mmsCloseConnection";
  public static final String MMS_CONFIG_EMAIL_GATEWAY_NUMBER = "emailGatewayNumber";
  public static final String MMS_CONFIG_GROUP_MMS_ENABLED = "enableGroupMms";
  public static final String MMS_CONFIG_HTTP_PARAMS = "httpParams";
  public static final String MMS_CONFIG_HTTP_SOCKET_TIMEOUT = "httpSocketTimeout";
  public static final String MMS_CONFIG_MAX_IMAGE_HEIGHT = "maxImageHeight";
  public static final String MMS_CONFIG_MAX_IMAGE_WIDTH = "maxImageWidth";
  public static final String MMS_CONFIG_MAX_MESSAGE_SIZE = "maxMessageSize";
  public static final String MMS_CONFIG_MESSAGE_TEXT_MAX_SIZE = "maxMessageTextSize";
  public static final String MMS_CONFIG_MMS_DELIVERY_REPORT_ENABLED = "enableMMSDeliveryReports";
  public static final String MMS_CONFIG_MMS_ENABLED = "enabledMMS";
  public static final String MMS_CONFIG_MMS_READ_REPORT_ENABLED = "enableMMSReadReports";
  public static final String MMS_CONFIG_MULTIPART_SMS_ENABLED = "enableMultipartSMS";
  public static final String MMS_CONFIG_NAI_SUFFIX = "naiSuffix";
  public static final String MMS_CONFIG_NOTIFY_WAP_MMSC_ENABLED = "enabledNotifyWapMMSC";
  public static final String MMS_CONFIG_RECIPIENT_LIMIT = "recipientLimit";
  public static final String MMS_CONFIG_SEND_MULTIPART_SMS_AS_SEPARATE_MESSAGES = "sendMultipartSmsAsSeparateMessages";
  public static final String MMS_CONFIG_SHOW_CELL_BROADCAST_APP_LINKS = "config_cellBroadcastAppLinks";
  public static final String MMS_CONFIG_SMS_DELIVERY_REPORT_ENABLED = "enableSMSDeliveryReports";
  public static final String MMS_CONFIG_SMS_TO_MMS_TEXT_LENGTH_THRESHOLD = "smsToMmsTextLengthThreshold";
  public static final String MMS_CONFIG_SMS_TO_MMS_TEXT_THRESHOLD = "smsToMmsTextThreshold";
  public static final String MMS_CONFIG_SUBJECT_MAX_LENGTH = "maxSubjectLength";
  public static final String MMS_CONFIG_SUPPORT_HTTP_CHARSET_HEADER = "supportHttpCharsetHeader";
  public static final String MMS_CONFIG_SUPPORT_MMS_CONTENT_DISPOSITION = "supportMmsContentDisposition";
  public static final String MMS_CONFIG_UA_PROF_TAG_NAME = "uaProfTagName";
  public static final String MMS_CONFIG_UA_PROF_URL = "uaProfUrl";
  public static final String MMS_CONFIG_USER_AGENT = "userAgent";
  public static final int MMS_ERROR_CONFIGURATION_ERROR = 7;
  public static final int MMS_ERROR_HTTP_FAILURE = 4;
  public static final int MMS_ERROR_INVALID_APN = 2;
  public static final int MMS_ERROR_IO_ERROR = 5;
  public static final int MMS_ERROR_NO_DATA_NETWORK = 8;
  public static final int MMS_ERROR_RETRY = 6;
  public static final int MMS_ERROR_UNABLE_CONNECT_MMS = 3;
  public static final int MMS_ERROR_UNSPECIFIED = 1;
  private static final String PHONE_PACKAGE_NAME = "com.android.phone";
  @SystemApi
  public static final int RESULT_CANCELLED = 23;
  @SystemApi
  public static final int RESULT_ENCODING_ERROR = 18;
  @SystemApi
  public static final int RESULT_ERROR_FDN_CHECK_FAILURE = 6;
  public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
  public static final int RESULT_ERROR_LIMIT_EXCEEDED = 5;
  @SystemApi
  public static final int RESULT_ERROR_NONE = 0;
  public static final int RESULT_ERROR_NO_SERVICE = 4;
  public static final int RESULT_ERROR_NULL_PDU = 3;
  public static final int RESULT_ERROR_RADIO_OFF = 2;
  public static final int RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED = 8;
  public static final int RESULT_ERROR_SHORT_CODE_NOT_ALLOWED = 7;
  @SystemApi
  public static final int RESULT_INTERNAL_ERROR = 21;
  @SystemApi
  public static final int RESULT_INVALID_ARGUMENTS = 11;
  @SystemApi
  public static final int RESULT_INVALID_SMSC_ADDRESS = 19;
  @SystemApi
  public static final int RESULT_INVALID_SMS_FORMAT = 14;
  @SystemApi
  public static final int RESULT_INVALID_STATE = 12;
  @SystemApi
  public static final int RESULT_MODEM_ERROR = 16;
  @SystemApi
  public static final int RESULT_NETWORK_ERROR = 17;
  @SystemApi
  public static final int RESULT_NETWORK_REJECT = 10;
  @SystemApi
  public static final int RESULT_NO_MEMORY = 13;
  @SystemApi
  public static final int RESULT_NO_RESOURCES = 22;
  @SystemApi
  public static final int RESULT_OPERATION_NOT_ALLOWED = 20;
  @SystemApi
  public static final int RESULT_RADIO_NOT_AVAILABLE = 9;
  @SystemApi
  public static final int RESULT_REQUEST_NOT_SUPPORTED = 24;
  @SystemApi
  public static final int RESULT_SYSTEM_ERROR = 15;
  public static final int SMS_MESSAGE_PERIOD_NOT_SPECIFIED = -1;
  public static final int SMS_MESSAGE_PRIORITY_NOT_SPECIFIED = -1;
  private static final int SMS_PICK = 2;
  public static final int SMS_RECORD_LENGTH = 176;
  public static final int SMS_TYPE_INCOMING = 0;
  public static final int SMS_TYPE_OUTGOING = 1;
  public static final int STATUS_ON_ICC_FREE = 0;
  public static final int STATUS_ON_ICC_READ = 1;
  public static final int STATUS_ON_ICC_SENT = 5;
  public static final int STATUS_ON_ICC_UNREAD = 3;
  public static final int STATUS_ON_ICC_UNSENT = 7;
  private static final String TAG = "SmsManager";
  private static final SmsManager sInstance = new SmsManager(64534);
  private static final Object sLockObject = new Object();
  private static final Map<Integer, SmsManager> sSubInstances = new ArrayMap();
  private int mSubId;
  
  static
  {
    DIALOG_TYPE_KEY = "dialog_type";
    boolean bool = false;
    if ((SystemProperties.getInt("persist.asus.calldata.dbg", 0) != 1) && (!"userdebug".equalsIgnoreCase(SystemProperties.get("ro.build.type", ""))) && (!"eng".equalsIgnoreCase(SystemProperties.get("ro.build.type", "")))) {
      break label89;
    }
    bool = true;
    label89:
    ASUS_DEBUG_PRIVACY = bool;
  }
  
  private SmsManager(int paramInt)
  {
    mSubId = paramInt;
  }
  
  private ArrayList<SmsMessage> createMessageListFromRawRecords(List<SmsRawData> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramList != null)
    {
      int i = paramList.size();
      for (int j = 0; j < i; j++)
      {
        Object localObject = (SmsRawData)paramList.get(j);
        if (localObject != null)
        {
          localObject = SmsMessage.createFromEfRecord(j + 1, ((SmsRawData)localObject).getBytes(), getSubscriptionId());
          if (localObject != null) {
            localArrayList.add(localObject);
          }
        }
      }
    }
    return localArrayList;
  }
  
  public static SmsManager getDefault()
  {
    return sInstance;
  }
  
  public static int getDefaultSmsSubscriptionId()
  {
    try
    {
      int i = ISms.Stub.asInterface(ServiceManager.getService("isms")).getPreferredSmsSubscription();
      return i;
    }
    catch (NullPointerException localNullPointerException)
    {
      return -1;
    }
    catch (RemoteException localRemoteException) {}
    return -1;
  }
  
  private static ISms getISmsService()
  {
    return ISms.Stub.asInterface(ServiceManager.getService("isms"));
  }
  
  private static ISms getISmsServiceOrThrow()
  {
    ISms localISms = getISmsService();
    if (localISms != null) {
      return localISms;
    }
    throw new UnsupportedOperationException("Sms is not supported");
  }
  
  public static Bundle getMmsConfig(BaseBundle paramBaseBundle)
  {
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("enabledTransID", paramBaseBundle.getBoolean("enabledTransID"));
    localBundle.putBoolean("enabledMMS", paramBaseBundle.getBoolean("enabledMMS"));
    localBundle.putBoolean("enableGroupMms", paramBaseBundle.getBoolean("enableGroupMms"));
    localBundle.putBoolean("enabledNotifyWapMMSC", paramBaseBundle.getBoolean("enabledNotifyWapMMSC"));
    localBundle.putBoolean("aliasEnabled", paramBaseBundle.getBoolean("aliasEnabled"));
    localBundle.putBoolean("allowAttachAudio", paramBaseBundle.getBoolean("allowAttachAudio"));
    localBundle.putBoolean("enableMultipartSMS", paramBaseBundle.getBoolean("enableMultipartSMS"));
    localBundle.putBoolean("enableSMSDeliveryReports", paramBaseBundle.getBoolean("enableSMSDeliveryReports"));
    localBundle.putBoolean("supportMmsContentDisposition", paramBaseBundle.getBoolean("supportMmsContentDisposition"));
    localBundle.putBoolean("sendMultipartSmsAsSeparateMessages", paramBaseBundle.getBoolean("sendMultipartSmsAsSeparateMessages"));
    localBundle.putBoolean("enableMMSReadReports", paramBaseBundle.getBoolean("enableMMSReadReports"));
    localBundle.putBoolean("enableMMSDeliveryReports", paramBaseBundle.getBoolean("enableMMSDeliveryReports"));
    localBundle.putBoolean("mmsCloseConnection", paramBaseBundle.getBoolean("mmsCloseConnection"));
    localBundle.putInt("maxMessageSize", paramBaseBundle.getInt("maxMessageSize"));
    localBundle.putInt("maxImageWidth", paramBaseBundle.getInt("maxImageWidth"));
    localBundle.putInt("maxImageHeight", paramBaseBundle.getInt("maxImageHeight"));
    localBundle.putInt("recipientLimit", paramBaseBundle.getInt("recipientLimit"));
    localBundle.putInt("aliasMinChars", paramBaseBundle.getInt("aliasMinChars"));
    localBundle.putInt("aliasMaxChars", paramBaseBundle.getInt("aliasMaxChars"));
    localBundle.putInt("smsToMmsTextThreshold", paramBaseBundle.getInt("smsToMmsTextThreshold"));
    localBundle.putInt("smsToMmsTextLengthThreshold", paramBaseBundle.getInt("smsToMmsTextLengthThreshold"));
    localBundle.putInt("maxMessageTextSize", paramBaseBundle.getInt("maxMessageTextSize"));
    localBundle.putInt("maxSubjectLength", paramBaseBundle.getInt("maxSubjectLength"));
    localBundle.putInt("httpSocketTimeout", paramBaseBundle.getInt("httpSocketTimeout"));
    localBundle.putString("uaProfTagName", paramBaseBundle.getString("uaProfTagName"));
    localBundle.putString("userAgent", paramBaseBundle.getString("userAgent"));
    localBundle.putString("uaProfUrl", paramBaseBundle.getString("uaProfUrl"));
    localBundle.putString("httpParams", paramBaseBundle.getString("httpParams"));
    localBundle.putString("emailGatewayNumber", paramBaseBundle.getString("emailGatewayNumber"));
    localBundle.putString("naiSuffix", paramBaseBundle.getString("naiSuffix"));
    localBundle.putBoolean("config_cellBroadcastAppLinks", paramBaseBundle.getBoolean("config_cellBroadcastAppLinks"));
    localBundle.putBoolean("supportHttpCharsetHeader", paramBaseBundle.getBoolean("supportHttpCharsetHeader"));
    return localBundle;
  }
  
  public static SmsManager getSmsManagerForSubscriptionId(int paramInt)
  {
    synchronized (sLockObject)
    {
      SmsManager localSmsManager1 = (SmsManager)sSubInstances.get(Integer.valueOf(paramInt));
      SmsManager localSmsManager2 = localSmsManager1;
      if (localSmsManager1 == null)
      {
        localSmsManager2 = new android/telephony/SmsManager;
        localSmsManager2.<init>(paramInt);
        sSubInstances.put(Integer.valueOf(paramInt), localSmsManager2);
      }
      return localSmsManager2;
    }
  }
  
  private void sendMultipartTextMessageInternal(String paramString1, String paramString2, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if ((paramList != null) && (paramList.size() >= 1))
      {
        if (paramList.size() > 1)
        {
          try
          {
            getISmsServiceOrThrow().sendMultipartTextForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), paramString1, paramString2, paramList, paramList1, paramList2, paramBoolean);
          }
          catch (RemoteException paramString1) {}
        }
        else
        {
          Object localObject1 = null;
          Object localObject2 = null;
          Object localObject3 = localObject1;
          if (paramList1 != null)
          {
            localObject3 = localObject1;
            if (paramList1.size() > 0) {
              localObject3 = (PendingIntent)paramList1.get(0);
            }
          }
          paramList1 = localObject2;
          if (paramList2 != null)
          {
            paramList1 = localObject2;
            if (paramList2.size() > 0) {
              paramList1 = (PendingIntent)paramList2.get(0);
            }
          }
          sendTextMessage(paramString1, paramString2, (String)paramList.get(0), (PendingIntent)localObject3, paramList1);
        }
        return;
      }
      throw new IllegalArgumentException("Invalid message body");
    }
    throw new IllegalArgumentException("Invalid destinationAddress");
  }
  
  private void sendMultipartTextMessageInternal(String paramString1, String paramString2, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    int i = paramInt1;
    if (!TextUtils.isEmpty(paramString1))
    {
      if ((paramList != null) && (paramList.size() >= 1))
      {
        if (i >= 0)
        {
          paramInt1 = i;
          if (i > 3) {}
        }
        for (;;)
        {
          break;
          paramInt1 = -1;
        }
        if ((paramInt2 >= 5) && (paramInt2 <= 635040)) {
          break label75;
        }
        paramInt2 = -1;
        label75:
        Object localObject1;
        if (paramList.size() > 1)
        {
          try
          {
            localObject1 = getISmsServiceOrThrow();
            if (localObject1 != null) {
              ((ISms)localObject1).sendMultipartTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), paramString1, paramString2, paramList, paramList1, paramList2, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
            }
          }
          catch (RemoteException paramString1) {}
        }
        else
        {
          Object localObject2 = null;
          Object localObject3 = null;
          localObject1 = localObject2;
          if (paramList1 != null)
          {
            localObject1 = localObject2;
            if (paramList1.size() > 0) {
              localObject1 = (PendingIntent)paramList1.get(0);
            }
          }
          paramList1 = localObject3;
          if (paramList2 != null)
          {
            paramList1 = localObject3;
            if (paramList2.size() > 0) {
              paramList1 = (PendingIntent)paramList2.get(0);
            }
          }
          sendTextMessageInternal(paramString1, paramString2, (String)paramList.get(0), (PendingIntent)localObject1, paramList1, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
        }
        return;
      }
      throw new IllegalArgumentException("Invalid message body");
    }
    throw new IllegalArgumentException("Invalid destinationAddress");
  }
  
  private void sendTextMessageInternal(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
  {
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString3))
      {
        try
        {
          getISmsServiceOrThrow().sendTextForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, paramBoolean);
        }
        catch (RemoteException paramString1) {}
        return;
      }
      throw new IllegalArgumentException("Invalid message body");
    }
    throw new IllegalArgumentException("Invalid destinationAddress");
  }
  
  private void sendTextMessageInternal(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    int i = paramInt1;
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString3))
      {
        if (i >= 0)
        {
          paramInt1 = i;
          if (i > 3) {}
        }
        for (;;)
        {
          break;
          paramInt1 = -1;
        }
        if ((paramInt2 >= 5) && (paramInt2 <= 635040)) {
          break label68;
        }
        paramInt2 = -1;
        try
        {
          label68:
          ISms localISms = getISmsServiceOrThrow();
          if (localISms != null) {
            localISms.sendTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
          }
        }
        catch (RemoteException paramString1) {}
        return;
      }
      throw new IllegalArgumentException("Invalid message body");
    }
    throw new IllegalArgumentException("Invalid destinationAddress");
  }
  
  public Uri addMultimediaMessageDraft(Uri paramUri)
  {
    if (paramUri != null)
    {
      try
      {
        IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
        if (localIMms != null)
        {
          paramUri = localIMms.addMultimediaMessageDraft(ActivityThread.currentPackageName(), paramUri);
          return paramUri;
        }
      }
      catch (RemoteException paramUri) {}
      return null;
    }
    throw new IllegalArgumentException("Uri contentUri null");
  }
  
  public Uri addTextMessageDraft(String paramString1, String paramString2)
  {
    try
    {
      IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
      if (localIMms != null)
      {
        paramString1 = localIMms.addTextMessageDraft(ActivityThread.currentPackageName(), paramString1, paramString2);
        return paramString1;
      }
    }
    catch (RemoteException paramString1) {}
    return null;
  }
  
  public boolean archiveStoredConversation(long paramLong, boolean paramBoolean)
  {
    try
    {
      IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
      if (localIMms != null)
      {
        paramBoolean = localIMms.archiveStoredConversation(ActivityThread.currentPackageName(), paramLong, paramBoolean);
        return paramBoolean;
      }
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean copyMessageToIcc(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    SeempLog.record(79);
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramArrayOfByte2 != null)
    {
      try
      {
        ISms localISms = getISmsService();
        if (localISms != null) {
          bool2 = localISms.copyMessageToIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), paramInt, paramArrayOfByte2, paramArrayOfByte1);
        }
      }
      catch (RemoteException paramArrayOfByte1)
      {
        bool2 = bool1;
      }
      return bool2;
    }
    throw new IllegalArgumentException("pdu is NULL");
  }
  
  public String createAppSpecificSmsToken(PendingIntent paramPendingIntent)
  {
    try
    {
      paramPendingIntent = getISmsServiceOrThrow().createAppSpecificSmsToken(getSubscriptionId(), ActivityThread.currentPackageName(), paramPendingIntent);
      return paramPendingIntent;
    }
    catch (RemoteException paramPendingIntent)
    {
      paramPendingIntent.rethrowFromSystemServer();
    }
    return null;
  }
  
  public boolean deleteMessageFromIcc(int paramInt)
  {
    SeempLog.record(80);
    boolean bool1 = false;
    boolean bool2 = false;
    byte[] arrayOfByte = new byte['¯'];
    Arrays.fill(arrayOfByte, (byte)-1);
    try
    {
      ISms localISms = getISmsService();
      if (localISms != null) {
        bool2 = localISms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), paramInt, 0, arrayOfByte);
      }
    }
    catch (RemoteException localRemoteException)
    {
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean deleteStoredConversation(long paramLong)
  {
    try
    {
      IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
      if (localIMms != null)
      {
        boolean bool = localIMms.deleteStoredConversation(ActivityThread.currentPackageName(), paramLong);
        return bool;
      }
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean deleteStoredMessage(Uri paramUri)
  {
    if (paramUri != null)
    {
      try
      {
        IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
        if (localIMms != null)
        {
          boolean bool = localIMms.deleteStoredMessage(ActivityThread.currentPackageName(), paramUri);
          return bool;
        }
      }
      catch (RemoteException paramUri) {}
      return false;
    }
    throw new IllegalArgumentException("Empty message URI");
  }
  
  public boolean disableCellBroadcast(int paramInt1, int paramInt2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    try
    {
      ISms localISms = getISmsService();
      if (localISms != null) {
        bool2 = localISms.disableCellBroadcastForSubscriber(getSubscriptionId(), paramInt1, paramInt2);
      }
    }
    catch (RemoteException localRemoteException)
    {
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean disableCellBroadcastRange(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramInt2 >= paramInt1)
    {
      try
      {
        ISms localISms = getISmsService();
        if (localISms != null) {
          bool2 = localISms.disableCellBroadcastRangeForSubscriber(getSubscriptionId(), paramInt1, paramInt2, paramInt3);
        }
      }
      catch (RemoteException localRemoteException)
      {
        bool2 = bool1;
      }
      return bool2;
    }
    throw new IllegalArgumentException("endMessageId < startMessageId");
  }
  
  public ArrayList<String> divideMessage(String paramString)
  {
    if (paramString != null) {
      return SmsMessage.fragmentText(paramString);
    }
    throw new IllegalArgumentException("text is null");
  }
  
  public void downloadMultimediaMessage(Context paramContext, String paramString, Uri paramUri, Bundle paramBundle, PendingIntent paramPendingIntent)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (paramUri != null)
      {
        try
        {
          paramContext = IMms.Stub.asInterface(ServiceManager.getService("imms"));
          if (paramContext == null) {
            return;
          }
          paramContext.downloadMessage(getSubscriptionId(), ActivityThread.currentPackageName(), paramString, paramUri, paramBundle, paramPendingIntent);
        }
        catch (RemoteException paramContext) {}
        return;
      }
      throw new IllegalArgumentException("Uri contentUri null");
    }
    throw new IllegalArgumentException("Empty MMS location URL");
  }
  
  public boolean enableCellBroadcast(int paramInt1, int paramInt2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    try
    {
      ISms localISms = getISmsService();
      if (localISms != null) {
        bool2 = localISms.enableCellBroadcastForSubscriber(getSubscriptionId(), paramInt1, paramInt2);
      }
    }
    catch (RemoteException localRemoteException)
    {
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean enableCellBroadcastRange(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramInt2 >= paramInt1)
    {
      try
      {
        ISms localISms = getISmsService();
        if (localISms != null) {
          bool2 = localISms.enableCellBroadcastRangeForSubscriber(getSubscriptionId(), paramInt1, paramInt2, paramInt3);
        }
      }
      catch (RemoteException localRemoteException)
      {
        bool2 = bool1;
      }
      return bool2;
    }
    throw new IllegalArgumentException("endMessageId < startMessageId");
  }
  
  public ArrayList<SmsMessage> getAllMessagesFromIcc()
  {
    Object localObject1 = null;
    List localList = null;
    Object localObject2;
    try
    {
      ISms localISms = getISmsService();
      if (localISms != null) {
        localList = localISms.getAllMessagesFromIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    return createMessageListFromRawRecords(localObject2);
  }
  
  public boolean getAutoPersisting()
  {
    try
    {
      IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
      if (localIMms != null)
      {
        boolean bool = localIMms.getAutoPersisting();
        return bool;
      }
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public Bundle getCarrierConfigValues()
  {
    try
    {
      Object localObject = IMms.Stub.asInterface(ServiceManager.getService("imms"));
      if (localObject != null)
      {
        localObject = ((IMms)localObject).getCarrierConfigValues(getSubscriptionId());
        return localObject;
      }
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public String getImsSmsFormat()
  {
    String str1 = "unknown";
    String str3;
    try
    {
      ISms localISms = getISmsService();
      String str2 = str1;
      if (localISms != null) {
        str2 = localISms.getImsSmsFormatForSubscriber(getSubscriptionId());
      }
    }
    catch (RemoteException localRemoteException)
    {
      str3 = str1;
    }
    return str3;
  }
  
  public int getSmsCapacityOnIcc()
  {
    int i = -1;
    int j;
    try
    {
      ISms localISms = getISmsService();
      j = i;
      if (localISms != null) {
        j = localISms.getSmsCapacityOnIccForSubscriber(getSubscriptionId());
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public int getSubscriptionId()
  {
    int i;
    if (mSubId == 64534) {
      i = getDefaultSmsSubscriptionId();
    } else {
      i = mSubId;
    }
    boolean bool1 = false;
    boolean bool2 = false;
    ActivityThread.currentApplication().getApplicationContext();
    try
    {
      ISms localISms = getISmsService();
      if (localISms != null) {
        bool2 = localISms.isSmsSimPickActivityNeeded(i);
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("SmsManager", "Exception in getSubscriptionId");
      bool2 = bool1;
    }
    if (ASUS_DEBUG_PRIVACY)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Asus Phone Team will handle this by itself isSmsSimPickActivityNeeded = ");
      localStringBuilder.append(bool2);
      Log.d("SmsManager", localStringBuilder.toString());
    }
    return i;
  }
  
  public Uri importMultimediaMessage(Uri paramUri, String paramString, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramUri != null)
    {
      try
      {
        IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
        if (localIMms != null)
        {
          paramUri = localIMms.importMultimediaMessage(ActivityThread.currentPackageName(), paramUri, paramString, paramLong, paramBoolean1, paramBoolean2);
          return paramUri;
        }
      }
      catch (RemoteException paramUri) {}
      return null;
    }
    throw new IllegalArgumentException("Uri contentUri null");
  }
  
  public Uri importTextMessage(String paramString1, int paramInt, String paramString2, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
      if (localIMms != null)
      {
        paramString1 = localIMms.importTextMessage(ActivityThread.currentPackageName(), paramString1, paramInt, paramString2, paramLong, paramBoolean1, paramBoolean2);
        return paramString1;
      }
    }
    catch (RemoteException paramString1) {}
    return null;
  }
  
  public void injectSmsPdu(byte[] paramArrayOfByte, String paramString, PendingIntent paramPendingIntent)
  {
    if ((!paramString.equals("3gpp")) && (!paramString.equals("3gpp2"))) {
      throw new IllegalArgumentException("Invalid pdu format. format must be either 3gpp or 3gpp2");
    }
    try
    {
      ISms localISms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
      if (localISms != null) {
        localISms.injectSmsPduForSubscriber(getSubscriptionId(), paramArrayOfByte, paramString, paramPendingIntent);
      }
    }
    catch (RemoteException paramArrayOfByte) {}
  }
  
  public boolean isImsSmsSupported()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    try
    {
      ISms localISms = getISmsService();
      if (localISms != null) {
        bool2 = localISms.isImsSmsSupportedForSubscriber(getSubscriptionId());
      }
    }
    catch (RemoteException localRemoteException)
    {
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean isSMSPromptEnabled()
  {
    try
    {
      boolean bool = ISms.Stub.asInterface(ServiceManager.getService("isms")).isSMSPromptEnabled();
      return bool;
    }
    catch (NullPointerException localNullPointerException)
    {
      return false;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void sendDataMessage(String paramString1, String paramString2, short paramShort, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    SeempLog.record_str(73, paramString1);
    if (!TextUtils.isEmpty(paramString1))
    {
      if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0))
      {
        try
        {
          getISmsServiceOrThrow().sendDataForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), paramString1, paramString2, paramShort & 0xFFFF, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
        }
        catch (RemoteException paramString1) {}
        return;
      }
      throw new IllegalArgumentException("Invalid message data");
    }
    throw new IllegalArgumentException("Invalid destinationAddress");
  }
  
  public void sendDataMessageWithSelfPermissions(String paramString1, String paramString2, short paramShort, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    SeempLog.record_str(73, paramString1);
    if (!TextUtils.isEmpty(paramString1))
    {
      if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0))
      {
        try
        {
          getISmsServiceOrThrow().sendDataForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), paramString1, paramString2, paramShort & 0xFFFF, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
        }
        catch (RemoteException paramString1) {}
        return;
      }
      throw new IllegalArgumentException("Invalid message data");
    }
    throw new IllegalArgumentException("Invalid destinationAddress");
  }
  
  public void sendMultimediaMessage(Context paramContext, Uri paramUri, String paramString, Bundle paramBundle, PendingIntent paramPendingIntent)
  {
    if (paramUri != null)
    {
      try
      {
        paramContext = IMms.Stub.asInterface(ServiceManager.getService("imms"));
        if (paramContext == null) {
          return;
        }
        paramContext.sendMessage(getSubscriptionId(), ActivityThread.currentPackageName(), paramUri, paramString, paramBundle, paramPendingIntent);
      }
      catch (RemoteException paramContext) {}
      return;
    }
    throw new IllegalArgumentException("Uri contentUri null");
  }
  
  public void sendMultipartTextMessage(String paramString1, String paramString2, ArrayList<String> paramArrayList, ArrayList<PendingIntent> paramArrayList1, ArrayList<PendingIntent> paramArrayList2)
  {
    sendMultipartTextMessageInternal(paramString1, paramString2, paramArrayList, paramArrayList1, paramArrayList2, true);
  }
  
  public void sendMultipartTextMessage(String paramString1, String paramString2, ArrayList<String> paramArrayList, ArrayList<PendingIntent> paramArrayList1, ArrayList<PendingIntent> paramArrayList2, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    sendMultipartTextMessageInternal(paramString1, paramString2, paramArrayList, paramArrayList1, paramArrayList2, true, paramInt1, paramBoolean, paramInt2);
  }
  
  @SystemApi
  public void sendMultipartTextMessageWithoutPersisting(String paramString1, String paramString2, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2)
  {
    sendMultipartTextMessageInternal(paramString1, paramString2, paramList, paramList1, paramList2, false);
  }
  
  public void sendMultipartTextMessageWithoutPersisting(String paramString1, String paramString2, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    sendMultipartTextMessageInternal(paramString1, paramString2, paramList, paramList1, paramList2, false, paramInt1, paramBoolean, paramInt2);
  }
  
  public void sendStoredMultimediaMessage(Uri paramUri, Bundle paramBundle, PendingIntent paramPendingIntent)
  {
    if (paramUri != null)
    {
      try
      {
        IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
        if (localIMms != null) {
          localIMms.sendStoredMessage(getSubscriptionId(), ActivityThread.currentPackageName(), paramUri, paramBundle, paramPendingIntent);
        }
      }
      catch (RemoteException paramUri) {}
      return;
    }
    throw new IllegalArgumentException("Empty message URI");
  }
  
  public void sendStoredMultipartTextMessage(Uri paramUri, String paramString, ArrayList<PendingIntent> paramArrayList1, ArrayList<PendingIntent> paramArrayList2)
  {
    if (paramUri != null)
    {
      try
      {
        ISms localISms = getISmsServiceOrThrow();
        localISms.sendStoredMultipartText(getSubscriptionId(), ActivityThread.currentPackageName(), paramUri, paramString, paramArrayList1, paramArrayList2);
      }
      catch (RemoteException paramUri) {}
      return;
    }
    throw new IllegalArgumentException("Empty message URI");
  }
  
  public void sendStoredTextMessage(Uri paramUri, String paramString, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    if (paramUri != null)
    {
      try
      {
        ISms localISms = getISmsServiceOrThrow();
        localISms.sendStoredText(getSubscriptionId(), ActivityThread.currentPackageName(), paramUri, paramString, paramPendingIntent1, paramPendingIntent2);
      }
      catch (RemoteException paramUri) {}
      return;
    }
    throw new IllegalArgumentException("Empty message URI");
  }
  
  public void sendTextMessage(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    SeempLog.record_str(75, paramString1);
    sendTextMessageInternal(paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, true);
  }
  
  public void sendTextMessage(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    sendTextMessageInternal(paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, true, paramInt1, paramBoolean, paramInt2);
  }
  
  public void sendTextMessageWithSelfPermissions(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
  {
    SeempLog.record_str(75, paramString1);
    if (!TextUtils.isEmpty(paramString1))
    {
      if (!TextUtils.isEmpty(paramString3))
      {
        try
        {
          getISmsServiceOrThrow().sendTextForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, paramBoolean);
        }
        catch (RemoteException paramString1) {}
        return;
      }
      throw new IllegalArgumentException("Invalid message body");
    }
    throw new IllegalArgumentException("Invalid destinationAddress");
  }
  
  @SystemApi
  public void sendTextMessageWithoutPersisting(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    sendTextMessageInternal(paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, false);
  }
  
  public void sendTextMessageWithoutPersisting(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    sendTextMessageInternal(paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, false, paramInt1, paramBoolean, paramInt2);
  }
  
  public void setAutoPersisting(boolean paramBoolean)
  {
    try
    {
      IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
      if (localIMms != null) {
        localIMms.setAutoPersisting(ActivityThread.currentPackageName(), paramBoolean);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public boolean updateMessageOnIcc(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    SeempLog.record(81);
    boolean bool1 = false;
    boolean bool2 = false;
    try
    {
      ISms localISms = getISmsService();
      if (localISms != null) {
        bool2 = localISms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), paramInt1, paramInt2, paramArrayOfByte);
      }
    }
    catch (RemoteException paramArrayOfByte)
    {
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean updateStoredMessageStatus(Uri paramUri, ContentValues paramContentValues)
  {
    if (paramUri != null)
    {
      try
      {
        IMms localIMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
        if (localIMms != null)
        {
          boolean bool = localIMms.updateStoredMessageStatus(ActivityThread.currentPackageName(), paramUri, paramContentValues);
          return bool;
        }
      }
      catch (RemoteException paramUri) {}
      return false;
    }
    throw new IllegalArgumentException("Empty message URI");
  }
}
