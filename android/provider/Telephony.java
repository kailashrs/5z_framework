package android.provider;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.net.Uri.Builder;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.SeempLog;
import com.android.internal.telephony.SmsApplication;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Telephony
{
  private static final String TAG = "Telephony";
  
  private Telephony() {}
  
  public static abstract interface BaseMmsColumns
    extends BaseColumns
  {
    @Deprecated
    public static final String ADAPTATION_ALLOWED = "adp_a";
    @Deprecated
    public static final String APPLIC_ID = "apl_id";
    @Deprecated
    public static final String AUX_APPLIC_ID = "aux_apl_id";
    @Deprecated
    public static final String CANCEL_ID = "cl_id";
    @Deprecated
    public static final String CANCEL_STATUS = "cl_st";
    public static final String CONTENT_CLASS = "ct_cls";
    public static final String CONTENT_LOCATION = "ct_l";
    public static final String CONTENT_TYPE = "ct_t";
    public static final String CREATOR = "creator";
    public static final String DATE = "date";
    public static final String DATE_SENT = "date_sent";
    public static final String DELIVERY_REPORT = "d_rpt";
    public static final String DELIVERY_TIME = "d_tm";
    @Deprecated
    public static final String DELIVERY_TIME_TOKEN = "d_tm_tok";
    @Deprecated
    public static final String DISTRIBUTION_INDICATOR = "d_ind";
    @Deprecated
    public static final String DRM_CONTENT = "drm_c";
    @Deprecated
    public static final String ELEMENT_DESCRIPTOR = "e_des";
    public static final String EXPIRY = "exp";
    @Deprecated
    public static final String LIMIT = "limit";
    public static final String LOCKED = "locked";
    @Deprecated
    public static final String MBOX_QUOTAS = "mb_qt";
    @Deprecated
    public static final String MBOX_QUOTAS_TOKEN = "mb_qt_tok";
    @Deprecated
    public static final String MBOX_TOTALS = "mb_t";
    @Deprecated
    public static final String MBOX_TOTALS_TOKEN = "mb_t_tok";
    public static final String MESSAGE_BOX = "msg_box";
    public static final int MESSAGE_BOX_ALL = 0;
    public static final int MESSAGE_BOX_DRAFTS = 3;
    public static final int MESSAGE_BOX_FAILED = 5;
    public static final int MESSAGE_BOX_INBOX = 1;
    public static final int MESSAGE_BOX_OUTBOX = 4;
    public static final int MESSAGE_BOX_SENT = 2;
    public static final String MESSAGE_CLASS = "m_cls";
    @Deprecated
    public static final String MESSAGE_COUNT = "m_cnt";
    public static final String MESSAGE_ID = "m_id";
    public static final String MESSAGE_SIZE = "m_size";
    public static final String MESSAGE_TYPE = "m_type";
    public static final String MMS_VERSION = "v";
    @Deprecated
    public static final String MM_FLAGS = "mm_flg";
    @Deprecated
    public static final String MM_FLAGS_TOKEN = "mm_flg_tok";
    @Deprecated
    public static final String MM_STATE = "mm_st";
    @Deprecated
    public static final String PREVIOUSLY_SENT_BY = "p_s_by";
    @Deprecated
    public static final String PREVIOUSLY_SENT_DATE = "p_s_d";
    public static final String PRIORITY = "pri";
    @Deprecated
    public static final String QUOTAS = "qt";
    public static final String READ = "read";
    public static final String READ_REPORT = "rr";
    public static final String READ_STATUS = "read_status";
    @Deprecated
    public static final String RECOMMENDED_RETRIEVAL_MODE = "r_r_mod";
    @Deprecated
    public static final String RECOMMENDED_RETRIEVAL_MODE_TEXT = "r_r_mod_txt";
    @Deprecated
    public static final String REPLACE_ID = "repl_id";
    @Deprecated
    public static final String REPLY_APPLIC_ID = "r_apl_id";
    @Deprecated
    public static final String REPLY_CHARGING = "r_chg";
    @Deprecated
    public static final String REPLY_CHARGING_DEADLINE = "r_chg_dl";
    @Deprecated
    public static final String REPLY_CHARGING_DEADLINE_TOKEN = "r_chg_dl_tok";
    @Deprecated
    public static final String REPLY_CHARGING_ID = "r_chg_id";
    @Deprecated
    public static final String REPLY_CHARGING_SIZE = "r_chg_sz";
    public static final String REPORT_ALLOWED = "rpt_a";
    public static final String RESPONSE_STATUS = "resp_st";
    public static final String RESPONSE_TEXT = "resp_txt";
    public static final String RETRIEVE_STATUS = "retr_st";
    public static final String RETRIEVE_TEXT = "retr_txt";
    public static final String RETRIEVE_TEXT_CHARSET = "retr_txt_cs";
    public static final String SEEN = "seen";
    @Deprecated
    public static final String SENDER_VISIBILITY = "s_vis";
    @Deprecated
    public static final String START = "start";
    public static final String STATUS = "st";
    @Deprecated
    public static final String STATUS_TEXT = "st_txt";
    @Deprecated
    public static final String STORE = "store";
    @Deprecated
    public static final String STORED = "stored";
    @Deprecated
    public static final String STORE_STATUS = "store_st";
    @Deprecated
    public static final String STORE_STATUS_TEXT = "store_st_txt";
    public static final String SUBJECT = "sub";
    public static final String SUBJECT_CHARSET = "sub_cs";
    public static final String SUBSCRIPTION_ID = "sub_id";
    public static final String TEXT_ONLY = "text_only";
    public static final String THREAD_ID = "thread_id";
    @Deprecated
    public static final String TOTALS = "totals";
    public static final String TRANSACTION_ID = "tr_id";
  }
  
  public static abstract interface CanonicalAddressesColumns
    extends BaseColumns
  {
    public static final String ADDRESS = "address";
  }
  
  public static abstract interface CarrierColumns
    extends BaseColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://carrier_information/carrier");
    public static final String EXPIRATION_TIME = "expiration_time";
    public static final String KEY_IDENTIFIER = "key_identifier";
    public static final String KEY_TYPE = "key_type";
    public static final String LAST_MODIFIED = "last_modified";
    public static final String MCC = "mcc";
    public static final String MNC = "mnc";
    public static final String MVNO_MATCH_DATA = "mvno_match_data";
    public static final String MVNO_TYPE = "mvno_type";
    public static final String PUBLIC_KEY = "public_key";
  }
  
  public static final class CarrierId
    implements BaseColumns
  {
    public static final String AUTHORITY = "carrier_id";
    public static final String CARRIER_ID = "carrier_id";
    public static final String CARRIER_NAME = "carrier_name";
    public static final Uri CONTENT_URI = Uri.parse("content://carrier_id");
    
    private CarrierId() {}
    
    public static Uri getUriForSubscriptionId(int paramInt)
    {
      return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(paramInt)).build();
    }
    
    public static final class All
      implements BaseColumns
    {
      public static final String APN = "apn";
      public static final Uri CONTENT_URI = Uri.parse("content://carrier_id/all");
      public static final String GID1 = "gid1";
      public static final String GID2 = "gid2";
      public static final String ICCID_PREFIX = "iccid_prefix";
      public static final String IMSI_PREFIX_XPATTERN = "imsi_prefix_xpattern";
      public static final String MCCMNC = "mccmnc";
      public static final String PLMN = "plmn";
      public static final String SPN = "spn";
      
      public All() {}
    }
  }
  
  public static final class Carriers
    implements BaseColumns
  {
    public static final String APN = "apn";
    public static final String APN_SET_ID = "apn_set_id";
    public static final String AUTH_TYPE = "authtype";
    @Deprecated
    public static final String BEARER = "bearer";
    @Deprecated
    public static final String BEARER_BITMASK = "bearer_bitmask";
    public static final int CARRIER_DELETED = 5;
    public static final int CARRIER_DELETED_BUT_PRESENT_IN_XML = 6;
    public static final int CARRIER_EDITED = 4;
    public static final String CARRIER_ENABLED = "carrier_enabled";
    public static final Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
    public static final String CURRENT = "current";
    public static final String DEFAULT_SORT_ORDER = "name ASC";
    public static final Uri DPC_URI = Uri.parse("content://telephony/carriers/dpc");
    public static final String EDITED = "edited";
    public static final String ENFORCE_KEY = "enforced";
    public static final Uri ENFORCE_MANAGED_URI = Uri.parse("content://telephony/carriers/enforce_managed");
    public static final Uri FILTERED_URI = Uri.parse("content://telephony/carriers/filtered");
    public static final String MAX_CONNS = "max_conns";
    public static final String MAX_CONNS_TIME = "max_conns_time";
    public static final String MCC = "mcc";
    public static final String MMSC = "mmsc";
    public static final String MMSPORT = "mmsport";
    public static final String MMSPROXY = "mmsproxy";
    public static final String MNC = "mnc";
    public static final String MODEM_COGNITIVE = "modem_cognitive";
    public static final String MTU = "mtu";
    public static final String MVNO_MATCH_DATA = "mvno_match_data";
    public static final String MVNO_TYPE = "mvno_type";
    public static final String NAME = "name";
    public static final String NETWORK_TYPE_BITMASK = "network_type_bitmask";
    public static final int NO_SET_SET = 0;
    public static final String NUMERIC = "numeric";
    public static final String OWNED_BY = "owned_by";
    public static final int OWNED_BY_DPC = 0;
    public static final int OWNED_BY_OTHERS = 1;
    public static final String PASSWORD = "password";
    public static final String PORT = "port";
    public static final String PROFILE_ID = "profile_id";
    public static final String PROTOCOL = "protocol";
    public static final String PROXY = "proxy";
    public static final String ROAMING_PROTOCOL = "roaming_protocol";
    public static final String SERVER = "server";
    public static final String SUBSCRIPTION_ID = "sub_id";
    public static final String TYPE = "type";
    public static final int UNEDITED = 0;
    public static final String USER = "user";
    public static final int USER_DELETED = 2;
    public static final int USER_DELETED_BUT_PRESENT_IN_XML = 3;
    public static final String USER_EDITABLE = "user_editable";
    public static final int USER_EDITED = 1;
    public static final String USER_VISIBLE = "user_visible";
    public static final String WAIT_TIME = "wait_time";
    
    private Carriers() {}
  }
  
  public static final class CellBroadcasts
    implements BaseColumns
  {
    public static final String CID = "cid";
    public static final String CMAS_CATEGORY = "cmas_category";
    public static final String CMAS_CERTAINTY = "cmas_certainty";
    public static final String CMAS_MESSAGE_CLASS = "cmas_message_class";
    public static final String CMAS_RESPONSE_TYPE = "cmas_response_type";
    public static final String CMAS_SEVERITY = "cmas_severity";
    public static final String CMAS_URGENCY = "cmas_urgency";
    public static final Uri CONTENT_URI = Uri.parse("content://cellbroadcasts");
    public static final String DEFAULT_SORT_ORDER = "date DESC";
    public static final String DELIVERY_TIME = "date";
    public static final String ETWS_WARNING_TYPE = "etws_warning_type";
    public static final String GEOGRAPHICAL_SCOPE = "geo_scope";
    public static final String LAC = "lac";
    public static final String LANGUAGE_CODE = "language";
    public static final String MESSAGE_BODY = "body";
    public static final String MESSAGE_FORMAT = "format";
    public static final String MESSAGE_PRIORITY = "priority";
    public static final String MESSAGE_READ = "read";
    public static final String PLMN = "plmn";
    public static final String[] QUERY_COLUMNS = { "_id", "geo_scope", "plmn", "lac", "cid", "serial_number", "service_category", "language", "body", "date", "read", "format", "priority", "etws_warning_type", "cmas_message_class", "cmas_category", "cmas_response_type", "cmas_severity", "cmas_urgency", "cmas_certainty" };
    public static final String SERIAL_NUMBER = "serial_number";
    public static final String SERVICE_CATEGORY = "service_category";
    public static final String V1_MESSAGE_CODE = "message_code";
    public static final String V1_MESSAGE_IDENTIFIER = "message_id";
    
    private CellBroadcasts() {}
  }
  
  public static final class Mms
    implements Telephony.BaseMmsColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://mms");
    public static final String DEFAULT_SORT_ORDER = "date DESC";
    public static final Pattern NAME_ADDR_EMAIL_PATTERN = Pattern.compile("\\s*(\"[^\"]*\"|[^<>\"]+)\\s*<([^<>]+)>\\s*");
    public static final Uri REPORT_REQUEST_URI = Uri.withAppendedPath(CONTENT_URI, "report-request");
    public static final Uri REPORT_STATUS_URI = Uri.withAppendedPath(CONTENT_URI, "report-status");
    
    private Mms() {}
    
    public static String extractAddrSpec(String paramString)
    {
      Matcher localMatcher = NAME_ADDR_EMAIL_PATTERN.matcher(paramString);
      if (localMatcher.matches()) {
        return localMatcher.group(2);
      }
      return paramString;
    }
    
    public static boolean isEmailAddress(String paramString)
    {
      if (TextUtils.isEmpty(paramString)) {
        return false;
      }
      paramString = extractAddrSpec(paramString);
      return Patterns.EMAIL_ADDRESS.matcher(paramString).matches();
    }
    
    public static boolean isPhoneNumber(String paramString)
    {
      if (TextUtils.isEmpty(paramString)) {
        return false;
      }
      return Patterns.PHONE.matcher(paramString).matches();
    }
    
    public static Cursor query(ContentResolver paramContentResolver, String[] paramArrayOfString)
    {
      SeempLog.record(10);
      return paramContentResolver.query(CONTENT_URI, paramArrayOfString, null, null, "date DESC");
    }
    
    public static Cursor query(ContentResolver paramContentResolver, String[] paramArrayOfString, String paramString1, String paramString2)
    {
      SeempLog.record(10);
      Uri localUri = CONTENT_URI;
      if (paramString2 == null) {
        paramString2 = "date DESC";
      }
      return paramContentResolver.query(localUri, paramArrayOfString, paramString1, null, paramString2);
    }
    
    public static final class Addr
      implements BaseColumns
    {
      public static final String ADDRESS = "address";
      public static final String CHARSET = "charset";
      public static final String CONTACT_ID = "contact_id";
      public static final String MSG_ID = "msg_id";
      public static final String TYPE = "type";
      
      private Addr() {}
    }
    
    public static final class Draft
      implements Telephony.BaseMmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://mms/drafts");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Draft() {}
    }
    
    public static final class Inbox
      implements Telephony.BaseMmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://mms/inbox");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Inbox() {}
    }
    
    public static final class Intents
    {
      public static final String CONTENT_CHANGED_ACTION = "android.intent.action.CONTENT_CHANGED";
      public static final String DELETED_CONTENTS = "deleted_contents";
      
      private Intents() {}
    }
    
    public static final class Outbox
      implements Telephony.BaseMmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://mms/outbox");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Outbox() {}
    }
    
    public static final class Part
      implements BaseColumns
    {
      public static final String CHARSET = "chset";
      public static final String CONTENT_DISPOSITION = "cd";
      public static final String CONTENT_ID = "cid";
      public static final String CONTENT_LOCATION = "cl";
      public static final String CONTENT_TYPE = "ct";
      public static final String CT_START = "ctt_s";
      public static final String CT_TYPE = "ctt_t";
      public static final String FILENAME = "fn";
      public static final String MSG_ID = "mid";
      public static final String NAME = "name";
      public static final String SEQ = "seq";
      public static final String TEXT = "text";
      public static final String _DATA = "_data";
      
      private Part() {}
    }
    
    public static final class Rate
    {
      public static final Uri CONTENT_URI = Uri.withAppendedPath(Telephony.Mms.CONTENT_URI, "rate");
      public static final String SENT_TIME = "sent_time";
      
      private Rate() {}
    }
    
    public static final class Sent
      implements Telephony.BaseMmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://mms/sent");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Sent() {}
    }
  }
  
  public static final class MmsSms
    implements BaseColumns
  {
    public static final Uri CONTENT_CONVERSATIONS_URI;
    public static final Uri CONTENT_DRAFT_URI = Uri.parse("content://mms-sms/draft");
    public static final Uri CONTENT_FILTER_BYPHONE_URI;
    public static final Uri CONTENT_LOCKED_URI = Uri.parse("content://mms-sms/locked");
    public static final Uri CONTENT_UNDELIVERED_URI;
    public static final Uri CONTENT_URI = Uri.parse("content://mms-sms/");
    public static final int ERR_TYPE_GENERIC = 1;
    public static final int ERR_TYPE_GENERIC_PERMANENT = 10;
    public static final int ERR_TYPE_MMS_PROTO_PERMANENT = 12;
    public static final int ERR_TYPE_MMS_PROTO_TRANSIENT = 3;
    public static final int ERR_TYPE_SMS_PROTO_PERMANENT = 11;
    public static final int ERR_TYPE_SMS_PROTO_TRANSIENT = 2;
    public static final int ERR_TYPE_TRANSPORT_FAILURE = 4;
    public static final int MMS_PROTO = 1;
    public static final int NO_ERROR = 0;
    public static final Uri SEARCH_URI = Uri.parse("content://mms-sms/search");
    public static final int SMS_PROTO = 0;
    public static final String TYPE_DISCRIMINATOR_COLUMN = "transport_type";
    
    static
    {
      CONTENT_CONVERSATIONS_URI = Uri.parse("content://mms-sms/conversations");
      CONTENT_FILTER_BYPHONE_URI = Uri.parse("content://mms-sms/messages/byphone");
      CONTENT_UNDELIVERED_URI = Uri.parse("content://mms-sms/undelivered");
    }
    
    private MmsSms() {}
    
    public static final class PendingMessages
      implements BaseColumns
    {
      public static final Uri CONTENT_URI = Uri.withAppendedPath(Telephony.MmsSms.CONTENT_URI, "pending");
      public static final String DUE_TIME = "due_time";
      public static final String ERROR_CODE = "err_code";
      public static final String ERROR_TYPE = "err_type";
      public static final String LAST_TRY = "last_try";
      public static final String MSG_ID = "msg_id";
      public static final String MSG_TYPE = "msg_type";
      public static final String PROTO_TYPE = "proto_type";
      public static final String RETRY_INDEX = "retry_index";
      public static final String SUBSCRIPTION_ID = "pending_sub_id";
      
      private PendingMessages() {}
    }
    
    public static final class WordsTable
    {
      public static final String ID = "_id";
      public static final String INDEXED_TEXT = "index_text";
      public static final String SOURCE_ROW_ID = "source_id";
      public static final String TABLE_ID = "table_to_use";
      
      private WordsTable() {}
    }
  }
  
  public static final class ServiceStateTable
  {
    public static final String AUTHORITY = "service-state";
    public static final String CDMA_DEFAULT_ROAMING_INDICATOR = "cdma_default_roaming_indicator";
    public static final String CDMA_ERI_ICON_INDEX = "cdma_eri_icon_index";
    public static final String CDMA_ERI_ICON_MODE = "cdma_eri_icon_mode";
    public static final String CDMA_ROAMING_INDICATOR = "cdma_roaming_indicator";
    public static final Uri CONTENT_URI = Uri.parse("content://service-state/");
    public static final String CSS_INDICATOR = "css_indicator";
    public static final String DATA_OPERATOR_ALPHA_LONG = "data_operator_alpha_long";
    public static final String DATA_OPERATOR_ALPHA_SHORT = "data_operator_alpha_short";
    public static final String DATA_OPERATOR_NUMERIC = "data_operator_numeric";
    public static final String DATA_REG_STATE = "data_reg_state";
    public static final String DATA_ROAMING_TYPE = "data_roaming_type";
    public static final String IS_DATA_ROAMING_FROM_REGISTRATION = "is_data_roaming_from_registration";
    public static final String IS_EMERGENCY_ONLY = "is_emergency_only";
    public static final String IS_MANUAL_NETWORK_SELECTION = "is_manual_network_selection";
    public static final String IS_USING_CARRIER_AGGREGATION = "is_using_carrier_aggregation";
    public static final String NETWORK_ID = "network_id";
    public static final String RIL_DATA_RADIO_TECHNOLOGY = "ril_data_radio_technology";
    public static final String RIL_VOICE_RADIO_TECHNOLOGY = "ril_voice_radio_technology";
    public static final String SYSTEM_ID = "system_id";
    public static final String VOICE_OPERATOR_ALPHA_LONG = "voice_operator_alpha_long";
    public static final String VOICE_OPERATOR_ALPHA_SHORT = "voice_operator_alpha_short";
    public static final String VOICE_OPERATOR_NUMERIC = "voice_operator_numeric";
    public static final String VOICE_REG_STATE = "voice_reg_state";
    public static final String VOICE_ROAMING_TYPE = "voice_roaming_type";
    
    private ServiceStateTable() {}
    
    public static ContentValues getContentValuesForServiceState(ServiceState paramServiceState)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("voice_reg_state", Integer.valueOf(paramServiceState.getVoiceRegState()));
      localContentValues.put("data_reg_state", Integer.valueOf(paramServiceState.getDataRegState()));
      localContentValues.put("voice_roaming_type", Integer.valueOf(paramServiceState.getVoiceRoamingType()));
      localContentValues.put("data_roaming_type", Integer.valueOf(paramServiceState.getDataRoamingType()));
      localContentValues.put("voice_operator_alpha_long", paramServiceState.getVoiceOperatorAlphaLong());
      localContentValues.put("voice_operator_alpha_short", paramServiceState.getVoiceOperatorAlphaShort());
      localContentValues.put("voice_operator_numeric", paramServiceState.getVoiceOperatorNumeric());
      localContentValues.put("data_operator_alpha_long", paramServiceState.getDataOperatorAlphaLong());
      localContentValues.put("data_operator_alpha_short", paramServiceState.getDataOperatorAlphaShort());
      localContentValues.put("data_operator_numeric", paramServiceState.getDataOperatorNumeric());
      localContentValues.put("is_manual_network_selection", Boolean.valueOf(paramServiceState.getIsManualSelection()));
      localContentValues.put("ril_voice_radio_technology", Integer.valueOf(paramServiceState.getRilVoiceRadioTechnology()));
      localContentValues.put("ril_data_radio_technology", Integer.valueOf(paramServiceState.getRilDataRadioTechnology()));
      localContentValues.put("css_indicator", Integer.valueOf(paramServiceState.getCssIndicator()));
      localContentValues.put("network_id", Integer.valueOf(paramServiceState.getCdmaNetworkId()));
      localContentValues.put("system_id", Integer.valueOf(paramServiceState.getCdmaSystemId()));
      localContentValues.put("cdma_roaming_indicator", Integer.valueOf(paramServiceState.getCdmaRoamingIndicator()));
      localContentValues.put("cdma_default_roaming_indicator", Integer.valueOf(paramServiceState.getCdmaDefaultRoamingIndicator()));
      localContentValues.put("cdma_eri_icon_index", Integer.valueOf(paramServiceState.getCdmaEriIconIndex()));
      localContentValues.put("cdma_eri_icon_mode", Integer.valueOf(paramServiceState.getCdmaEriIconMode()));
      localContentValues.put("is_emergency_only", Boolean.valueOf(paramServiceState.isEmergencyOnly()));
      localContentValues.put("is_data_roaming_from_registration", Boolean.valueOf(paramServiceState.getDataRoamingFromRegistration()));
      localContentValues.put("is_using_carrier_aggregation", Boolean.valueOf(paramServiceState.isUsingCarrierAggregation()));
      return localContentValues;
    }
    
    public static Uri getUriForSubscriptionId(int paramInt)
    {
      return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(paramInt)).build();
    }
    
    public static Uri getUriForSubscriptionIdAndField(int paramInt, String paramString)
    {
      return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(paramInt)).appendEncodedPath(paramString).build();
    }
  }
  
  public static final class Sms
    implements BaseColumns, Telephony.TextBasedSmsColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://sms");
    public static final String DEFAULT_SORT_ORDER = "date DESC";
    
    private Sms() {}
    
    public static Uri addMessageToUri(int paramInt, ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    {
      return addMessageToUri(paramInt, paramContentResolver, paramUri, paramString1, paramString2, paramString3, paramLong, paramBoolean1, paramBoolean2, -1L);
    }
    
    public static Uri addMessageToUri(int paramInt, ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean1, boolean paramBoolean2, long paramLong1)
    {
      return addMessageToUri(paramInt, paramContentResolver, paramUri, paramString1, paramString2, paramString3, paramLong, paramBoolean1, paramBoolean2, paramLong1, -1);
    }
    
    public static Uri addMessageToUri(int paramInt1, ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean1, boolean paramBoolean2, long paramLong1, int paramInt2)
    {
      ContentValues localContentValues = new ContentValues(8);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Telephony addMessageToUri sub id: ");
      localStringBuilder.append(paramInt1);
      Rlog.v("Telephony", localStringBuilder.toString());
      localContentValues.put("sub_id", Integer.valueOf(paramInt1));
      localContentValues.put("address", paramString1);
      if (paramLong != null) {
        localContentValues.put("date", paramLong);
      }
      if (paramBoolean1) {}
      for (paramInt1 = 1;; paramInt1 = 0) {
        break;
      }
      localContentValues.put("read", Integer.valueOf(paramInt1));
      localContentValues.put("subject", paramString3);
      localContentValues.put("body", paramString2);
      localContentValues.put("priority", Integer.valueOf(paramInt2));
      if (paramBoolean2) {
        localContentValues.put("status", Integer.valueOf(32));
      }
      if (paramLong1 != -1L) {
        localContentValues.put("thread_id", Long.valueOf(paramLong1));
      }
      return paramContentResolver.insert(paramUri, localContentValues);
    }
    
    public static Uri addMessageToUri(ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    {
      return addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), paramContentResolver, paramUri, paramString1, paramString2, paramString3, paramLong, paramBoolean1, paramBoolean2, -1L);
    }
    
    public static Uri addMessageToUri(ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean1, boolean paramBoolean2, long paramLong1)
    {
      return addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), paramContentResolver, paramUri, paramString1, paramString2, paramString3, paramLong, paramBoolean1, paramBoolean2, paramLong1);
    }
    
    public static String getDefaultSmsPackage(Context paramContext)
    {
      paramContext = SmsApplication.getDefaultSmsApplication(paramContext, false);
      if (paramContext != null) {
        return paramContext.getPackageName();
      }
      return null;
    }
    
    public static boolean isOutgoingFolder(int paramInt)
    {
      boolean bool;
      if ((paramInt != 5) && (paramInt != 4) && (paramInt != 2) && (paramInt != 6)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public static boolean moveMessageToFolder(Context paramContext, Uri paramUri, int paramInt1, int paramInt2)
    {
      boolean bool = false;
      if (paramUri == null) {
        return false;
      }
      int i = 0;
      int j = 0;
      switch (paramInt1)
      {
      default: 
        return false;
      case 5: 
      case 6: 
        i = 1;
        break;
      case 2: 
      case 4: 
        j = 1;
        break;
      }
      ContentValues localContentValues = new ContentValues(3);
      localContentValues.put("type", Integer.valueOf(paramInt1));
      if (i != 0) {
        localContentValues.put("read", Integer.valueOf(0));
      } else if (j != 0) {
        localContentValues.put("read", Integer.valueOf(1));
      }
      localContentValues.put("error_code", Integer.valueOf(paramInt2));
      if (1 == SqliteWrapper.update(paramContext, paramContext.getContentResolver(), paramUri, localContentValues, null, null)) {
        bool = true;
      }
      return bool;
    }
    
    public static Cursor query(ContentResolver paramContentResolver, String[] paramArrayOfString)
    {
      SeempLog.record(10);
      return paramContentResolver.query(CONTENT_URI, paramArrayOfString, null, null, "date DESC");
    }
    
    public static Cursor query(ContentResolver paramContentResolver, String[] paramArrayOfString, String paramString1, String paramString2)
    {
      SeempLog.record(10);
      Uri localUri = CONTENT_URI;
      if (paramString2 == null) {
        paramString2 = "date DESC";
      }
      return paramContentResolver.query(localUri, paramArrayOfString, paramString1, null, paramString2);
    }
    
    public static final class Conversations
      implements BaseColumns, Telephony.TextBasedSmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://sms/conversations");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      public static final String MESSAGE_COUNT = "msg_count";
      public static final String SNIPPET = "snippet";
      
      private Conversations() {}
    }
    
    public static final class Draft
      implements BaseColumns, Telephony.TextBasedSmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://sms/draft");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Draft() {}
      
      public static Uri addMessage(int paramInt, ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong)
      {
        return Telephony.Sms.addMessageToUri(paramInt, paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, true, false);
      }
      
      public static Uri addMessage(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong)
      {
        return Telephony.Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, true, false);
      }
    }
    
    public static final class Inbox
      implements BaseColumns, Telephony.TextBasedSmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://sms/inbox");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Inbox() {}
      
      public static Uri addMessage(int paramInt, ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean)
      {
        return Telephony.Sms.addMessageToUri(paramInt, paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, paramBoolean, false);
      }
      
      public static Uri addMessage(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean)
      {
        return Telephony.Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, paramBoolean, false);
      }
    }
    
    public static final class Intents
    {
      public static final String ACTION_CHANGE_DEFAULT = "android.provider.Telephony.ACTION_CHANGE_DEFAULT";
      public static final String ACTION_DEFAULT_SMS_PACKAGE_CHANGED = "android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED";
      public static final String ACTION_EXTERNAL_PROVIDER_CHANGE = "android.provider.action.EXTERNAL_PROVIDER_CHANGE";
      public static final String DATA_SMS_RECEIVED_ACTION = "android.intent.action.DATA_SMS_RECEIVED";
      public static final String EXTRA_IS_DEFAULT_SMS_APP = "android.provider.extra.IS_DEFAULT_SMS_APP";
      public static final String EXTRA_PACKAGE_NAME = "package";
      public static final String MMS_DOWNLOADED_ACTION = "android.provider.Telephony.MMS_DOWNLOADED";
      public static final int RESULT_SMS_DUPLICATED = 5;
      public static final int RESULT_SMS_GENERIC_ERROR = 2;
      public static final int RESULT_SMS_HANDLED = 1;
      public static final int RESULT_SMS_OUT_OF_MEMORY = 3;
      public static final int RESULT_SMS_UNSUPPORTED = 4;
      public static final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
      public static final String SIM_FULL_ACTION = "android.provider.Telephony.SIM_FULL";
      public static final String SMS_CARRIER_PROVISION_ACTION = "android.provider.Telephony.SMS_CARRIER_PROVISION";
      public static final String SMS_CB_RECEIVED_ACTION = "android.provider.Telephony.SMS_CB_RECEIVED";
      public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";
      public static final String SMS_EMERGENCY_CB_RECEIVED_ACTION = "android.provider.Telephony.SMS_EMERGENCY_CB_RECEIVED";
      public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
      public static final String SMS_REJECTED_ACTION = "android.provider.Telephony.SMS_REJECTED";
      public static final String SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED_ACTION = "android.provider.Telephony.SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED";
      public static final String WAP_PUSH_DELIVER_ACTION = "android.provider.Telephony.WAP_PUSH_DELIVER";
      public static final String WAP_PUSH_RECEIVED_ACTION = "android.provider.Telephony.WAP_PUSH_RECEIVED";
      
      private Intents() {}
      
      public static SmsMessage[] getMessagesFromIntent(Intent paramIntent)
      {
        try
        {
          localObject = (Object[])paramIntent.getSerializableExtra("pdus");
          if (localObject == null)
          {
            Rlog.e("Telephony", "pdus does not exist in the intent");
            return null;
          }
          String str = paramIntent.getStringExtra("format");
          int i = paramIntent.getIntExtra("subscription", SubscriptionManager.getDefaultSmsSubscriptionId());
          paramIntent = new StringBuilder();
          paramIntent.append(" getMessagesFromIntent sub_id : ");
          paramIntent.append(i);
          Rlog.v("Telephony", paramIntent.toString());
          int j = localObject.length;
          paramIntent = new SmsMessage[j];
          for (int k = 0; k < j; k++)
          {
            paramIntent[k] = SmsMessage.createFromPdu((byte[])localObject[k], str);
            if (paramIntent[k] != null) {
              paramIntent[k].setSubId(i);
            }
          }
          return paramIntent;
        }
        catch (ClassCastException paramIntent)
        {
          Object localObject = new StringBuilder();
          ((StringBuilder)localObject).append("getMessagesFromIntent: ");
          ((StringBuilder)localObject).append(paramIntent);
          Rlog.e("Telephony", ((StringBuilder)localObject).toString());
        }
        return null;
      }
    }
    
    public static final class Outbox
      implements BaseColumns, Telephony.TextBasedSmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://sms/outbox");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Outbox() {}
      
      public static Uri addMessage(int paramInt, ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean, long paramLong1)
      {
        return Telephony.Sms.addMessageToUri(paramInt, paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, true, paramBoolean, paramLong1);
      }
      
      public static Uri addMessage(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong, boolean paramBoolean, long paramLong1)
      {
        return Telephony.Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, true, paramBoolean, paramLong1);
      }
    }
    
    public static final class Sent
      implements BaseColumns, Telephony.TextBasedSmsColumns
    {
      public static final Uri CONTENT_URI = Uri.parse("content://sms/sent");
      public static final String DEFAULT_SORT_ORDER = "date DESC";
      
      private Sent() {}
      
      public static Uri addMessage(int paramInt, ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong)
      {
        return Telephony.Sms.addMessageToUri(paramInt, paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, true, false);
      }
      
      public static Uri addMessage(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, Long paramLong)
      {
        return Telephony.Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), paramContentResolver, CONTENT_URI, paramString1, paramString2, paramString3, paramLong, true, false);
      }
    }
  }
  
  public static abstract interface TextBasedSmsColumns
  {
    public static final String ADDRESS = "address";
    public static final String BODY = "body";
    public static final String CREATOR = "creator";
    public static final String DATE = "date";
    public static final String DATE_SENT = "date_sent";
    public static final String ERROR_CODE = "error_code";
    public static final String LOCKED = "locked";
    public static final int MESSAGE_TYPE_ALL = 0;
    public static final int MESSAGE_TYPE_DRAFT = 3;
    public static final int MESSAGE_TYPE_FAILED = 5;
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_OUTBOX = 4;
    public static final int MESSAGE_TYPE_QUEUED = 6;
    public static final int MESSAGE_TYPE_SENT = 2;
    public static final String MTU = "mtu";
    public static final String PERSON = "person";
    public static final String PRIORITY = "priority";
    public static final String PROTOCOL = "protocol";
    public static final String READ = "read";
    public static final String REPLY_PATH_PRESENT = "reply_path_present";
    public static final String SEEN = "seen";
    public static final String SERVICE_CENTER = "service_center";
    public static final String STATUS = "status";
    public static final int STATUS_COMPLETE = 0;
    public static final int STATUS_FAILED = 64;
    public static final int STATUS_NONE = -1;
    public static final int STATUS_PENDING = 32;
    public static final String SUBJECT = "subject";
    public static final String SUBSCRIPTION_ID = "sub_id";
    public static final String THREAD_ID = "thread_id";
    public static final String TYPE = "type";
  }
  
  public static final class Threads
    implements Telephony.ThreadsColumns
  {
    public static final int BROADCAST_THREAD = 1;
    public static final int COMMON_THREAD = 0;
    public static final Uri CONTENT_URI = Uri.withAppendedPath(Telephony.MmsSms.CONTENT_URI, "conversations");
    private static final String[] ID_PROJECTION = { "_id" };
    public static final Uri OBSOLETE_THREADS_URI = Uri.withAppendedPath(CONTENT_URI, "obsolete");
    private static final Uri THREAD_ID_CONTENT_URI = Uri.parse("content://mms-sms/threadID");
    
    private Threads() {}
    
    public static long getOrCreateThreadId(Context paramContext, String paramString)
    {
      HashSet localHashSet = new HashSet();
      localHashSet.add(paramString);
      return getOrCreateThreadId(paramContext, localHashSet);
    }
    
    public static long getOrCreateThreadId(Context paramContext, Set<String> paramSet)
    {
      Uri.Builder localBuilder = THREAD_ID_CONTENT_URI.buildUpon();
      Iterator localIterator = paramSet.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localObject = str;
        if (Telephony.Mms.isEmailAddress(str)) {
          localObject = Telephony.Mms.extractAddrSpec(str);
        }
        localBuilder.appendQueryParameter("recipient", (String)localObject);
      }
      Object localObject = localBuilder.build();
      paramContext = SqliteWrapper.query(paramContext, paramContext.getContentResolver(), (Uri)localObject, ID_PROJECTION, null, null, null);
      if (paramContext != null) {
        try
        {
          if (paramContext.moveToFirst())
          {
            long l = paramContext.getLong(0);
            return l;
          }
          Rlog.e("Telephony", "getOrCreateThreadId returned no rows!");
        }
        finally
        {
          paramContext.close();
        }
      }
      paramContext = new StringBuilder();
      paramContext.append("getOrCreateThreadId failed with ");
      paramContext.append(paramSet.size());
      paramContext.append(" recipients");
      Rlog.e("Telephony", paramContext.toString());
      throw new IllegalArgumentException("Unable to find or allocate a thread ID.");
    }
  }
  
  public static abstract interface ThreadsColumns
    extends BaseColumns
  {
    public static final String ARCHIVED = "archived";
    public static final String ATTACHMENT_INFO = "attachment_info";
    public static final String DATE = "date";
    public static final String ERROR = "error";
    public static final String HAS_ATTACHMENT = "has_attachment";
    public static final String MESSAGE_COUNT = "message_count";
    public static final String NOTIFICATION = "notification";
    public static final String READ = "read";
    public static final String RECIPIENT_IDS = "recipient_ids";
    public static final String SNIPPET = "snippet";
    public static final String SNIPPET_CHARSET = "snippet_cs";
    public static final String TYPE = "type";
  }
}
