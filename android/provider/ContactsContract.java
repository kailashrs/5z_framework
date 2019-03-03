package android.provider;

import android.accounts.Account;
import android.annotation.SystemApi;
import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.CursorEntityIterator;
import android.content.Entity;
import android.content.EntityIterator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SeempLog;
import android.view.View;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public final class ContactsContract
{
  public static final String AUTHORITY = "com.android.contacts";
  public static final Uri AUTHORITY_URI = Uri.parse("content://com.android.contacts");
  public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";
  public static final String DEFERRED_SNIPPETING = "deferred_snippeting";
  public static final String DEFERRED_SNIPPETING_QUERY = "deferred_snippeting_query";
  public static final String DIRECTORY_PARAM_KEY = "directory";
  public static final String HIDDEN_COLUMN_PREFIX = "x_";
  public static final String LIMIT_PARAM_KEY = "limit";
  public static final String PRIMARY_ACCOUNT_NAME = "name_for_primary_account";
  public static final String PRIMARY_ACCOUNT_TYPE = "type_for_primary_account";
  public static final String REMOVE_DUPLICATE_ENTRIES = "remove_duplicate_entries";
  public static final String STREQUENT_PHONE_ONLY = "strequent_phone_only";
  
  public ContactsContract() {}
  
  public static boolean isProfileId(long paramLong)
  {
    boolean bool;
    if (paramLong >= 9223372034707292160L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static final class AggregationExceptions
    implements BaseColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/aggregation_exception";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/aggregation_exception";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "aggregation_exceptions");
    public static final String RAW_CONTACT_ID1 = "raw_contact_id1";
    public static final String RAW_CONTACT_ID2 = "raw_contact_id2";
    public static final String TYPE = "type";
    public static final int TYPE_AUTOMATIC = 0;
    public static final int TYPE_KEEP_SEPARATE = 2;
    public static final int TYPE_KEEP_TOGETHER = 1;
    
    private AggregationExceptions() {}
  }
  
  public static final class Authorization
  {
    public static final String AUTHORIZATION_METHOD = "authorize";
    public static final String KEY_AUTHORIZED_URI = "authorized_uri";
    public static final String KEY_URI_TO_AUTHORIZE = "uri_to_authorize";
    
    public Authorization() {}
  }
  
  protected static abstract interface BaseSyncColumns
  {
    public static final String SYNC1 = "sync1";
    public static final String SYNC2 = "sync2";
    public static final String SYNC3 = "sync3";
    public static final String SYNC4 = "sync4";
  }
  
  public static final class CommonDataKinds
  {
    public static final String PACKAGE_COMMON = "common";
    
    private CommonDataKinds() {}
    
    public static abstract interface BaseTypes
    {
      public static final int TYPE_CUSTOM = 0;
    }
    
    public static final class Callable
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
      public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "callables");
      public static final Uri ENTERPRISE_CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter_enterprise");
      
      public Callable() {}
    }
    
    protected static abstract interface CommonColumns
      extends ContactsContract.CommonDataKinds.BaseTypes
    {
      public static final String DATA = "data1";
      public static final String LABEL = "data3";
      public static final String TYPE = "data2";
    }
    
    public static final class Contactables
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
      public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "contactables");
      public static final String VISIBLE_CONTACTS_ONLY = "visible_contacts_only";
      
      public Contactables() {}
    }
    
    public static final class Email
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String ADDRESS = "data1";
      public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/email_v2";
      public static final Uri CONTENT_LOOKUP_URI;
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/email_v2";
      public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "emails");
      public static final String DISPLAY_NAME = "data4";
      public static final Uri ENTERPRISE_CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter_enterprise");
      public static final Uri ENTERPRISE_CONTENT_LOOKUP_URI;
      public static final int TYPE_HOME = 1;
      public static final int TYPE_MOBILE = 4;
      public static final int TYPE_OTHER = 3;
      public static final int TYPE_WORK = 2;
      
      static
      {
        CONTENT_LOOKUP_URI = Uri.withAppendedPath(CONTENT_URI, "lookup");
        ENTERPRISE_CONTENT_LOOKUP_URI = Uri.withAppendedPath(CONTENT_URI, "lookup_enterprise");
      }
      
      private Email() {}
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == 0) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeLabelResource(paramInt));
      }
      
      public static final int getTypeLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17039911;
        case 4: 
          return 17039913;
        case 3: 
          return 17039914;
        case 2: 
          return 17039915;
        }
        return 17039912;
      }
    }
    
    public static final class Event
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact_event";
      public static final String START_DATE = "data1";
      public static final int TYPE_ANNIVERSARY = 1;
      public static final int TYPE_BIRTHDAY = 3;
      public static final int TYPE_OTHER = 2;
      
      private Event() {}
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == 0) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeResource(Integer.valueOf(paramInt)));
      }
      
      public static int getTypeResource(Integer paramInteger)
      {
        if (paramInteger == null) {
          return 17039937;
        }
        switch (paramInteger.intValue())
        {
        default: 
          return 17039936;
        case 3: 
          return 17039935;
        case 2: 
          return 17039937;
        }
        return 17039934;
      }
    }
    
    public static final class GroupMembership
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/group_membership";
      public static final String GROUP_ROW_ID = "data1";
      public static final String GROUP_SOURCE_ID = "group_sourceid";
      
      private GroupMembership() {}
    }
    
    public static final class Identity
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/identity";
      public static final String IDENTITY = "data1";
      public static final String NAMESPACE = "data2";
      
      private Identity() {}
    }
    
    public static final class Im
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/im";
      public static final String CUSTOM_PROTOCOL = "data6";
      public static final String PROTOCOL = "data5";
      public static final int PROTOCOL_AIM = 0;
      public static final int PROTOCOL_CUSTOM = -1;
      public static final int PROTOCOL_GOOGLE_TALK = 5;
      public static final int PROTOCOL_ICQ = 6;
      public static final int PROTOCOL_JABBER = 7;
      public static final int PROTOCOL_MSN = 1;
      public static final int PROTOCOL_NETMEETING = 8;
      public static final int PROTOCOL_QQ = 4;
      public static final int PROTOCOL_SKYPE = 3;
      public static final int PROTOCOL_YAHOO = 2;
      public static final int TYPE_HOME = 1;
      public static final int TYPE_OTHER = 3;
      public static final int TYPE_WORK = 2;
      
      private Im() {}
      
      public static final CharSequence getProtocolLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == -1) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getProtocolLabelResource(paramInt));
      }
      
      public static final int getProtocolLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17040108;
        case 8: 
          return 17040113;
        case 7: 
          return 17040111;
        case 6: 
          return 17040110;
        case 5: 
          return 17040109;
        case 4: 
          return 17040114;
        case 3: 
          return 17040115;
        case 2: 
          return 17040116;
        case 1: 
          return 17040112;
        }
        return 17040107;
      }
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == 0) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeLabelResource(paramInt));
      }
      
      public static final int getTypeLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17040117;
        case 3: 
          return 17040119;
        case 2: 
          return 17040120;
        }
        return 17040118;
      }
    }
    
    public static final class Nickname
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/nickname";
      public static final String NAME = "data1";
      public static final int TYPE_DEFAULT = 1;
      public static final int TYPE_INITIALS = 5;
      public static final int TYPE_MAIDEN_NAME = 3;
      @Deprecated
      public static final int TYPE_MAINDEN_NAME = 3;
      public static final int TYPE_OTHER_NAME = 2;
      public static final int TYPE_SHORT_NAME = 4;
      
      private Nickname() {}
    }
    
    public static final class Note
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/note";
      public static final String NOTE = "data1";
      
      private Note() {}
    }
    
    public static final class Organization
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String COMPANY = "data1";
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/organization";
      public static final String DEPARTMENT = "data5";
      public static final String JOB_DESCRIPTION = "data6";
      public static final String OFFICE_LOCATION = "data9";
      public static final String PHONETIC_NAME = "data8";
      public static final String PHONETIC_NAME_STYLE = "data10";
      public static final String SYMBOL = "data7";
      public static final String TITLE = "data4";
      public static final int TYPE_OTHER = 2;
      public static final int TYPE_WORK = 1;
      
      private Organization() {}
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == 0) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeLabelResource(paramInt));
      }
      
      public static final int getTypeLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17040530;
        case 2: 
          return 17040531;
        }
        return 17040532;
      }
    }
    
    public static final class Phone
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/phone_v2";
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/phone_v2";
      public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "phones");
      public static final Uri ENTERPRISE_CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter_enterprise");
      public static final Uri ENTERPRISE_CONTENT_URI = Uri.withAppendedPath(ContactsContract.Data.ENTERPRISE_CONTENT_URI, "phones");
      public static final String NORMALIZED_NUMBER = "data4";
      public static final String NUMBER = "data1";
      public static final String SEARCH_DISPLAY_NAME_KEY = "search_display_name";
      public static final String SEARCH_PHONE_NUMBER_KEY = "search_phone_number";
      public static final int TYPE_ASSISTANT = 19;
      public static final int TYPE_CALLBACK = 8;
      public static final int TYPE_CAR = 9;
      public static final int TYPE_COMPANY_MAIN = 10;
      public static final int TYPE_FAX_HOME = 5;
      public static final int TYPE_FAX_WORK = 4;
      public static final int TYPE_HOME = 1;
      public static final int TYPE_ISDN = 11;
      public static final int TYPE_MAIN = 12;
      public static final int TYPE_MMS = 20;
      public static final int TYPE_MOBILE = 2;
      public static final int TYPE_OTHER = 7;
      public static final int TYPE_OTHER_FAX = 13;
      public static final int TYPE_PAGER = 6;
      public static final int TYPE_RADIO = 14;
      public static final int TYPE_TELEX = 15;
      public static final int TYPE_TTY_TDD = 16;
      public static final int TYPE_WORK = 3;
      public static final int TYPE_WORK_MOBILE = 17;
      public static final int TYPE_WORK_PAGER = 18;
      
      private Phone() {}
      
      @Deprecated
      public static final CharSequence getDisplayLabel(Context paramContext, int paramInt, CharSequence paramCharSequence)
      {
        return getTypeLabel(paramContext.getResources(), paramInt, paramCharSequence);
      }
      
      @Deprecated
      public static final CharSequence getDisplayLabel(Context paramContext, int paramInt, CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence)
      {
        return getTypeLabel(paramContext.getResources(), paramInt, paramCharSequence);
      }
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if (((paramInt == 0) || (paramInt == 19)) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeLabelResource(paramInt));
      }
      
      public static final int getTypeLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17040805;
        case 20: 
          return 17040811;
        case 19: 
          return 17040801;
        case 18: 
          return 17040821;
        case 17: 
          return 17040820;
        case 16: 
          return 17040818;
        case 15: 
          return 17040817;
        case 14: 
          return 17040816;
        case 13: 
          return 17040814;
        case 12: 
          return 17040810;
        case 11: 
          return 17040809;
        case 10: 
          return 17040804;
        case 9: 
          return 17040803;
        case 8: 
          return 17040802;
        case 7: 
          return 17040813;
        case 6: 
          return 17040815;
        case 5: 
          return 17040806;
        case 4: 
          return 17040807;
        case 3: 
          return 17040819;
        case 2: 
          return 17040812;
        }
        return 17040808;
      }
    }
    
    public static final class Photo
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/photo";
      public static final String PHOTO = "data15";
      public static final String PHOTO_FILE_ID = "data14";
      
      private Photo() {}
    }
    
    public static final class Relation
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/relation";
      public static final String NAME = "data1";
      public static final int TYPE_ASSISTANT = 1;
      public static final int TYPE_BROTHER = 2;
      public static final int TYPE_CHILD = 3;
      public static final int TYPE_DOMESTIC_PARTNER = 4;
      public static final int TYPE_FATHER = 5;
      public static final int TYPE_FRIEND = 6;
      public static final int TYPE_MANAGER = 7;
      public static final int TYPE_MOTHER = 8;
      public static final int TYPE_PARENT = 9;
      public static final int TYPE_PARTNER = 10;
      public static final int TYPE_REFERRED_BY = 11;
      public static final int TYPE_RELATIVE = 12;
      public static final int TYPE_SISTER = 13;
      public static final int TYPE_SPOUSE = 14;
      
      private Relation() {}
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == 0) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeLabelResource(paramInt));
      }
      
      public static final int getTypeLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17040530;
        case 14: 
          return 17040900;
        case 13: 
          return 17040899;
        case 12: 
          return 17040898;
        case 11: 
          return 17040897;
        case 10: 
          return 17040896;
        case 9: 
          return 17040895;
        case 8: 
          return 17040894;
        case 7: 
          return 17040893;
        case 6: 
          return 17040892;
        case 5: 
          return 17040891;
        case 4: 
          return 17040890;
        case 3: 
          return 17040888;
        case 2: 
          return 17040887;
        }
        return 17040886;
      }
    }
    
    public static final class SipAddress
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/sip_address";
      public static final String SIP_ADDRESS = "data1";
      public static final int TYPE_HOME = 1;
      public static final int TYPE_OTHER = 3;
      public static final int TYPE_WORK = 2;
      
      private SipAddress() {}
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == 0) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeLabelResource(paramInt));
      }
      
      public static final int getTypeLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17041018;
        case 3: 
          return 17041020;
        case 2: 
          return 17041021;
        }
        return 17041019;
      }
    }
    
    public static final class StructuredName
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/name";
      public static final String DISPLAY_NAME = "data1";
      public static final String FAMILY_NAME = "data3";
      public static final String FULL_NAME_STYLE = "data10";
      public static final String GIVEN_NAME = "data2";
      public static final String MIDDLE_NAME = "data5";
      public static final String PHONETIC_FAMILY_NAME = "data9";
      public static final String PHONETIC_GIVEN_NAME = "data7";
      public static final String PHONETIC_MIDDLE_NAME = "data8";
      public static final String PHONETIC_NAME_STYLE = "data11";
      public static final String PREFIX = "data4";
      public static final String SUFFIX = "data6";
      
      private StructuredName() {}
    }
    
    public static final class StructuredPostal
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String CITY = "data7";
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/postal-address_v2";
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/postal-address_v2";
      public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "postals");
      public static final String COUNTRY = "data10";
      public static final String FORMATTED_ADDRESS = "data1";
      public static final String NEIGHBORHOOD = "data6";
      public static final String POBOX = "data5";
      public static final String POSTCODE = "data9";
      public static final String REGION = "data8";
      public static final String STREET = "data4";
      public static final int TYPE_HOME = 1;
      public static final int TYPE_OTHER = 3;
      public static final int TYPE_WORK = 2;
      
      private StructuredPostal() {}
      
      public static final CharSequence getTypeLabel(Resources paramResources, int paramInt, CharSequence paramCharSequence)
      {
        if ((paramInt == 0) && (!TextUtils.isEmpty(paramCharSequence))) {
          return paramCharSequence;
        }
        return paramResources.getText(getTypeLabelResource(paramInt));
      }
      
      public static final int getTypeLabelResource(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          return 17040849;
        case 3: 
          return 17040851;
        case 2: 
          return 17040852;
        }
        return 17040850;
      }
    }
    
    public static final class Website
      implements ContactsContract.DataColumnsWithJoins, ContactsContract.CommonDataKinds.CommonColumns, ContactsContract.ContactCounts
    {
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/website";
      public static final int TYPE_BLOG = 2;
      public static final int TYPE_FTP = 6;
      public static final int TYPE_HOME = 4;
      public static final int TYPE_HOMEPAGE = 1;
      public static final int TYPE_OTHER = 7;
      public static final int TYPE_PROFILE = 3;
      public static final int TYPE_WORK = 5;
      public static final String URL = "data1";
      
      private Website() {}
    }
  }
  
  static abstract interface ContactCounts
  {
    public static final String EXTRA_ADDRESS_BOOK_INDEX = "android.provider.extra.ADDRESS_BOOK_INDEX";
    public static final String EXTRA_ADDRESS_BOOK_INDEX_COUNTS = "android.provider.extra.ADDRESS_BOOK_INDEX_COUNTS";
    public static final String EXTRA_ADDRESS_BOOK_INDEX_TITLES = "android.provider.extra.ADDRESS_BOOK_INDEX_TITLES";
  }
  
  protected static abstract interface ContactNameColumns
  {
    public static final String DISPLAY_NAME_ALTERNATIVE = "display_name_alt";
    public static final String DISPLAY_NAME_PRIMARY = "display_name";
    public static final String DISPLAY_NAME_SOURCE = "display_name_source";
    public static final String PHONETIC_NAME = "phonetic_name";
    public static final String PHONETIC_NAME_STYLE = "phonetic_name_style";
    public static final String SORT_KEY_ALTERNATIVE = "sort_key_alt";
    public static final String SORT_KEY_PRIMARY = "sort_key";
  }
  
  protected static abstract interface ContactOptionsColumns
  {
    public static final String CUSTOM_RINGTONE = "custom_ringtone";
    public static final String LAST_TIME_CONTACTED = "last_time_contacted";
    public static final String LR_LAST_TIME_CONTACTED = "last_time_contacted";
    public static final String LR_TIMES_CONTACTED = "times_contacted";
    public static final String PINNED = "pinned";
    public static final String RAW_LAST_TIME_CONTACTED = "x_last_time_contacted";
    public static final String RAW_TIMES_CONTACTED = "x_times_contacted";
    public static final String SEND_TO_VOICEMAIL = "send_to_voicemail";
    public static final String STARRED = "starred";
    public static final String TIMES_CONTACTED = "times_contacted";
  }
  
  protected static abstract interface ContactStatusColumns
  {
    public static final String CONTACT_CHAT_CAPABILITY = "contact_chat_capability";
    public static final String CONTACT_PRESENCE = "contact_presence";
    public static final String CONTACT_STATUS = "contact_status";
    public static final String CONTACT_STATUS_ICON = "contact_status_icon";
    public static final String CONTACT_STATUS_LABEL = "contact_status_label";
    public static final String CONTACT_STATUS_RES_PACKAGE = "contact_status_res_package";
    public static final String CONTACT_STATUS_TIMESTAMP = "contact_status_ts";
  }
  
  public static class Contacts
    implements BaseColumns, ContactsContract.ContactsColumns, ContactsContract.ContactOptionsColumns, ContactsContract.ContactNameColumns, ContactsContract.ContactStatusColumns, ContactsContract.ContactCounts
  {
    public static final Uri CONTENT_FILTER_URI;
    public static final Uri CONTENT_FREQUENT_URI;
    public static final Uri CONTENT_GROUP_URI = Uri.withAppendedPath(CONTENT_URI, "group");
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact";
    public static final Uri CONTENT_LOOKUP_URI;
    public static final Uri CONTENT_MULTI_VCARD_URI;
    public static final Uri CONTENT_STREQUENT_FILTER_URI;
    public static final Uri CONTENT_STREQUENT_URI;
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contact";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "contacts");
    public static final String CONTENT_VCARD_TYPE = "text/x-vcard";
    public static final Uri CONTENT_VCARD_URI;
    public static final Uri CORP_CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "contacts_corp");
    public static long ENTERPRISE_CONTACT_ID_BASE = 1000000000L;
    public static String ENTERPRISE_CONTACT_LOOKUP_PREFIX = "c-";
    public static final Uri ENTERPRISE_CONTENT_FILTER_URI;
    public static final String QUERY_PARAMETER_VCARD_NO_PHOTO = "no_photo";
    
    static
    {
      CONTENT_LOOKUP_URI = Uri.withAppendedPath(CONTENT_URI, "lookup");
      CONTENT_VCARD_URI = Uri.withAppendedPath(CONTENT_URI, "as_vcard");
      CONTENT_MULTI_VCARD_URI = Uri.withAppendedPath(CONTENT_URI, "as_multi_vcard");
      CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter");
      ENTERPRISE_CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, "filter_enterprise");
      CONTENT_STREQUENT_URI = Uri.withAppendedPath(CONTENT_URI, "strequent");
      CONTENT_FREQUENT_URI = Uri.withAppendedPath(CONTENT_URI, "frequent");
      CONTENT_STREQUENT_FILTER_URI = Uri.withAppendedPath(CONTENT_STREQUENT_URI, "filter");
    }
    
    private Contacts() {}
    
    public static Uri getLookupUri(long paramLong, String paramString)
    {
      SeempLog.record(86);
      if (TextUtils.isEmpty(paramString)) {
        return null;
      }
      return ContentUris.withAppendedId(Uri.withAppendedPath(CONTENT_LOOKUP_URI, paramString), paramLong);
    }
    
    public static Uri getLookupUri(ContentResolver paramContentResolver, Uri paramUri)
    {
      SeempLog.record(86);
      paramContentResolver = paramContentResolver.query(paramUri, new String[] { "lookup", "_id" }, null, null, null);
      if (paramContentResolver == null) {
        return null;
      }
      try
      {
        if (paramContentResolver.moveToFirst())
        {
          paramUri = paramContentResolver.getString(0);
          paramUri = getLookupUri(paramContentResolver.getLong(1), paramUri);
          return paramUri;
        }
        return null;
      }
      finally
      {
        paramContentResolver.close();
      }
    }
    
    public static boolean isEnterpriseContactId(long paramLong)
    {
      boolean bool;
      if ((paramLong >= ENTERPRISE_CONTACT_ID_BASE) && (paramLong < 9223372034707292160L)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public static Uri lookupContact(ContentResolver paramContentResolver, Uri paramUri)
    {
      SeempLog.record(87);
      if (paramUri == null) {
        return null;
      }
      paramContentResolver = paramContentResolver.query(paramUri, new String[] { "_id" }, null, null, null);
      if (paramContentResolver == null) {
        return null;
      }
      try
      {
        if (paramContentResolver.moveToFirst())
        {
          long l = paramContentResolver.getLong(0);
          paramUri = ContentUris.withAppendedId(CONTENT_URI, l);
          return paramUri;
        }
        return null;
      }
      finally
      {
        paramContentResolver.close();
      }
    }
    
    @Deprecated
    public static void markAsContacted(ContentResolver paramContentResolver, long paramLong)
    {
      Uri localUri = ContentUris.withAppendedId(CONTENT_URI, paramLong);
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("last_time_contacted", Long.valueOf(System.currentTimeMillis()));
      paramContentResolver.update(localUri, localContentValues, null, null);
    }
    
    public static InputStream openContactPhotoInputStream(ContentResolver paramContentResolver, Uri paramUri)
    {
      SeempLog.record(88);
      return openContactPhotoInputStream(paramContentResolver, paramUri, false);
    }
    
    public static InputStream openContactPhotoInputStream(ContentResolver paramContentResolver, Uri paramUri, boolean paramBoolean)
    {
      SeempLog.record(88);
      if (paramBoolean)
      {
        Object localObject = Uri.withAppendedPath(paramUri, "display_photo");
        try
        {
          localObject = paramContentResolver.openAssetFileDescriptor((Uri)localObject, "r");
          if (localObject != null)
          {
            localObject = ((AssetFileDescriptor)localObject).createInputStream();
            return localObject;
          }
        }
        catch (IOException localIOException) {}
      }
      paramUri = Uri.withAppendedPath(paramUri, "photo");
      if (paramUri == null) {
        return null;
      }
      paramContentResolver = paramContentResolver.query(paramUri, new String[] { "data15" }, null, null, null);
      if (paramContentResolver != null) {
        try
        {
          if (paramContentResolver.moveToNext())
          {
            paramUri = paramContentResolver.getBlob(0);
            if (paramUri == null) {
              return null;
            }
            paramUri = new ByteArrayInputStream(paramUri);
            return paramUri;
          }
        }
        finally
        {
          if (paramContentResolver != null) {
            paramContentResolver.close();
          }
        }
      }
      if (paramContentResolver != null) {
        paramContentResolver.close();
      }
      return null;
    }
    
    public static final class AggregationSuggestions
      implements BaseColumns, ContactsContract.ContactsColumns, ContactsContract.ContactOptionsColumns, ContactsContract.ContactStatusColumns
    {
      public static final String CONTENT_DIRECTORY = "suggestions";
      public static final String PARAMETER_MATCH_NAME = "name";
      
      private AggregationSuggestions() {}
      
      public static final Builder builder()
      {
        return new Builder();
      }
      
      public static final class Builder
      {
        private long mContactId;
        private int mLimit;
        private final ArrayList<String> mValues = new ArrayList();
        
        public Builder() {}
        
        public Builder addNameParameter(String paramString)
        {
          mValues.add(paramString);
          return this;
        }
        
        public Uri build()
        {
          Uri.Builder localBuilder = ContactsContract.Contacts.CONTENT_URI.buildUpon();
          localBuilder.appendEncodedPath(String.valueOf(mContactId));
          localBuilder.appendPath("suggestions");
          if (mLimit != 0) {
            localBuilder.appendQueryParameter("limit", String.valueOf(mLimit));
          }
          int i = mValues.size();
          for (int j = 0; j < i; j++)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("name:");
            localStringBuilder.append((String)mValues.get(j));
            localBuilder.appendQueryParameter("query", localStringBuilder.toString());
          }
          return localBuilder.build();
        }
        
        public Builder setContactId(long paramLong)
        {
          mContactId = paramLong;
          return this;
        }
        
        public Builder setLimit(int paramInt)
        {
          mLimit = paramInt;
          return this;
        }
      }
    }
    
    public static final class Data
      implements BaseColumns, ContactsContract.DataColumns
    {
      public static final String CONTENT_DIRECTORY = "data";
      
      private Data() {}
    }
    
    public static final class Entity
      implements BaseColumns, ContactsContract.ContactsColumns, ContactsContract.ContactNameColumns, ContactsContract.RawContactsColumns, ContactsContract.BaseSyncColumns, ContactsContract.SyncColumns, ContactsContract.DataColumns, ContactsContract.StatusColumns, ContactsContract.ContactOptionsColumns, ContactsContract.ContactStatusColumns, ContactsContract.DataUsageStatColumns
    {
      public static final String CONTENT_DIRECTORY = "entities";
      public static final String DATA_ID = "data_id";
      public static final String RAW_CONTACT_ID = "raw_contact_id";
      
      private Entity() {}
    }
    
    public static final class Photo
      implements BaseColumns, ContactsContract.DataColumnsWithJoins
    {
      public static final String CONTENT_DIRECTORY = "photo";
      public static final String DISPLAY_PHOTO = "display_photo";
      public static final String PHOTO = "data15";
      public static final String PHOTO_FILE_ID = "data14";
      
      private Photo() {}
    }
    
    @Deprecated
    public static final class StreamItems
      implements ContactsContract.StreamItemsColumns
    {
      @Deprecated
      public static final String CONTENT_DIRECTORY = "stream_items";
      
      @Deprecated
      private StreamItems() {}
    }
  }
  
  protected static abstract interface ContactsColumns
  {
    public static final String CONTACT_LAST_UPDATED_TIMESTAMP = "contact_last_updated_timestamp";
    public static final String DISPLAY_NAME = "display_name";
    public static final String HAS_PHONE_NUMBER = "has_phone_number";
    public static final String IN_DEFAULT_DIRECTORY = "in_default_directory";
    public static final String IN_VISIBLE_GROUP = "in_visible_group";
    public static final String IS_USER_PROFILE = "is_user_profile";
    public static final String LOOKUP_KEY = "lookup";
    public static final String NAME_RAW_CONTACT_ID = "name_raw_contact_id";
    public static final String PHOTO_FILE_ID = "photo_file_id";
    public static final String PHOTO_ID = "photo_id";
    public static final String PHOTO_THUMBNAIL_URI = "photo_thumb_uri";
    public static final String PHOTO_URI = "photo_uri";
  }
  
  public static final class Data
    implements ContactsContract.DataColumnsWithJoins, ContactsContract.ContactCounts
  {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/data";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "data");
    static final Uri ENTERPRISE_CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "data_enterprise");
    public static final String VISIBLE_CONTACTS_ONLY = "visible_contacts_only";
    
    private Data() {}
    
    public static Uri getContactLookupUri(ContentResolver paramContentResolver, Uri paramUri)
    {
      SeempLog.record(89);
      paramContentResolver = paramContentResolver.query(paramUri, new String[] { "contact_id", "lookup" }, null, null, null);
      if (paramContentResolver != null) {
        try
        {
          if (paramContentResolver.moveToFirst())
          {
            paramUri = ContactsContract.Contacts.getLookupUri(paramContentResolver.getLong(0), paramContentResolver.getString(1));
            return paramUri;
          }
        }
        finally
        {
          if (paramContentResolver != null) {
            paramContentResolver.close();
          }
        }
      }
      if (paramContentResolver != null) {
        paramContentResolver.close();
      }
      return null;
    }
  }
  
  protected static abstract interface DataColumns
  {
    public static final String CARRIER_PRESENCE = "carrier_presence";
    public static final int CARRIER_PRESENCE_VT_CAPABLE = 1;
    public static final String DATA1 = "data1";
    public static final String DATA10 = "data10";
    public static final String DATA11 = "data11";
    public static final String DATA12 = "data12";
    public static final String DATA13 = "data13";
    public static final String DATA14 = "data14";
    public static final String DATA15 = "data15";
    public static final String DATA2 = "data2";
    public static final String DATA3 = "data3";
    public static final String DATA4 = "data4";
    public static final String DATA5 = "data5";
    public static final String DATA6 = "data6";
    public static final String DATA7 = "data7";
    public static final String DATA8 = "data8";
    public static final String DATA9 = "data9";
    public static final String DATA_VERSION = "data_version";
    public static final String HASH_ID = "hash_id";
    public static final String IS_PRIMARY = "is_primary";
    public static final String IS_READ_ONLY = "is_read_only";
    public static final String IS_SUPER_PRIMARY = "is_super_primary";
    public static final String MIMETYPE = "mimetype";
    public static final String PREFERRED_PHONE_ACCOUNT_COMPONENT_NAME = "preferred_phone_account_component_name";
    public static final String PREFERRED_PHONE_ACCOUNT_ID = "preferred_phone_account_id";
    public static final String RAW_CONTACT_ID = "raw_contact_id";
    public static final String RES_PACKAGE = "res_package";
    public static final String SYNC1 = "data_sync1";
    public static final String SYNC2 = "data_sync2";
    public static final String SYNC3 = "data_sync3";
    public static final String SYNC4 = "data_sync4";
  }
  
  protected static abstract interface DataColumnsWithJoins
    extends BaseColumns, ContactsContract.DataColumns, ContactsContract.StatusColumns, ContactsContract.RawContactsColumns, ContactsContract.ContactsColumns, ContactsContract.ContactNameColumns, ContactsContract.ContactOptionsColumns, ContactsContract.ContactStatusColumns, ContactsContract.DataUsageStatColumns
  {}
  
  public static final class DataUsageFeedback
  {
    public static final Uri DELETE_USAGE_URI = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, "delete_usage");
    public static final Uri FEEDBACK_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "usagefeedback");
    public static final String USAGE_TYPE = "type";
    public static final String USAGE_TYPE_CALL = "call";
    public static final String USAGE_TYPE_LONG_TEXT = "long_text";
    public static final String USAGE_TYPE_SHORT_TEXT = "short_text";
    
    public DataUsageFeedback() {}
  }
  
  protected static abstract interface DataUsageStatColumns
  {
    public static final String LAST_TIME_USED = "last_time_used";
    public static final String LR_LAST_TIME_USED = "last_time_used";
    public static final String LR_TIMES_USED = "times_used";
    public static final String RAW_LAST_TIME_USED = "x_last_time_used";
    public static final String RAW_TIMES_USED = "x_times_used";
    public static final String TIMES_USED = "times_used";
  }
  
  public static final class DeletedContacts
    implements ContactsContract.DeletedContactsColumns
  {
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "deleted_contacts");
    private static final int DAYS_KEPT = 30;
    public static final long DAYS_KEPT_MILLISECONDS = 2592000000L;
    
    private DeletedContacts() {}
  }
  
  protected static abstract interface DeletedContactsColumns
  {
    public static final String CONTACT_DELETED_TIMESTAMP = "contact_deleted_timestamp";
    public static final String CONTACT_ID = "contact_id";
  }
  
  public static final class Directory
    implements BaseColumns
  {
    public static final String ACCOUNT_NAME = "accountName";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String CALLER_PACKAGE_PARAM_KEY = "callerPackage";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact_directory";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contact_directories";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "directories");
    public static final long DEFAULT = 0L;
    public static final String DIRECTORY_AUTHORITY = "authority";
    public static final String DISPLAY_NAME = "displayName";
    public static final Uri ENTERPRISE_CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "directories_enterprise");
    public static final long ENTERPRISE_DEFAULT = 1000000000L;
    public static final long ENTERPRISE_DIRECTORY_ID_BASE = 1000000000L;
    public static final Uri ENTERPRISE_FILE_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "directory_file_enterprise");
    public static final long ENTERPRISE_LOCAL_INVISIBLE = 1000000001L;
    public static final String EXPORT_SUPPORT = "exportSupport";
    public static final int EXPORT_SUPPORT_ANY_ACCOUNT = 2;
    public static final int EXPORT_SUPPORT_NONE = 0;
    public static final int EXPORT_SUPPORT_SAME_ACCOUNT_ONLY = 1;
    public static final long LOCAL_INVISIBLE = 1L;
    public static final String PACKAGE_NAME = "packageName";
    public static final String PHOTO_SUPPORT = "photoSupport";
    public static final int PHOTO_SUPPORT_FULL = 3;
    public static final int PHOTO_SUPPORT_FULL_SIZE_ONLY = 2;
    public static final int PHOTO_SUPPORT_NONE = 0;
    public static final int PHOTO_SUPPORT_THUMBNAIL_ONLY = 1;
    public static final String SHORTCUT_SUPPORT = "shortcutSupport";
    public static final int SHORTCUT_SUPPORT_DATA_ITEMS_ONLY = 1;
    public static final int SHORTCUT_SUPPORT_FULL = 2;
    public static final int SHORTCUT_SUPPORT_NONE = 0;
    public static final String TYPE_RESOURCE_ID = "typeResourceId";
    
    private Directory() {}
    
    public static boolean isEnterpriseDirectoryId(long paramLong)
    {
      boolean bool;
      if (paramLong >= 1000000000L) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public static boolean isRemoteDirectory(long paramLong)
    {
      return isRemoteDirectoryId(paramLong);
    }
    
    public static boolean isRemoteDirectoryId(long paramLong)
    {
      boolean bool;
      if ((paramLong != 0L) && (paramLong != 1L) && (paramLong != 1000000000L) && (paramLong != 1000000001L)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public static void notifyDirectoryChange(ContentResolver paramContentResolver)
    {
      ContentValues localContentValues = new ContentValues();
      paramContentResolver.update(CONTENT_URI, localContentValues, null, null);
    }
  }
  
  public static abstract interface DisplayNameSources
  {
    public static final int EMAIL = 10;
    public static final int NICKNAME = 35;
    public static final int ORGANIZATION = 30;
    public static final int PHONE = 20;
    public static final int STRUCTURED_NAME = 40;
    public static final int STRUCTURED_PHONETIC_NAME = 37;
    public static final int UNDEFINED = 0;
  }
  
  public static final class DisplayPhoto
  {
    public static final Uri CONTENT_MAX_DIMENSIONS_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "photo_dimensions");
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "display_photo");
    public static final String DISPLAY_MAX_DIM = "display_max_dim";
    public static final String THUMBNAIL_MAX_DIM = "thumbnail_max_dim";
    
    private DisplayPhoto() {}
  }
  
  public static abstract interface FullNameStyle
  {
    public static final int CHINESE = 3;
    public static final int CJK = 2;
    public static final int JAPANESE = 4;
    public static final int KOREAN = 5;
    public static final int UNDEFINED = 0;
    public static final int WESTERN = 1;
  }
  
  public static final class Groups
    implements BaseColumns, ContactsContract.GroupsColumns, ContactsContract.SyncColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/group";
    public static final Uri CONTENT_SUMMARY_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "groups_summary");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/group";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "groups");
    
    private Groups() {}
    
    public static EntityIterator newEntityIterator(Cursor paramCursor)
    {
      return new EntityIteratorImpl(paramCursor);
    }
    
    private static class EntityIteratorImpl
      extends CursorEntityIterator
    {
      public EntityIteratorImpl(Cursor paramCursor)
      {
        super();
      }
      
      public Entity getEntityAndIncrementCursor(Cursor paramCursor)
        throws RemoteException
      {
        ContentValues localContentValues = new ContentValues();
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "_id");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "account_name");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "account_type");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "dirty");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "version");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "sourceid");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "res_package");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "title");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "title_res");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "group_visible");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "sync1");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "sync2");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "sync3");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "sync4");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "system_id");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "deleted");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "notes");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "should_sync");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "favorites");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "auto_add");
        paramCursor.moveToNext();
        return new Entity(localContentValues);
      }
    }
  }
  
  protected static abstract interface GroupsColumns
  {
    public static final String ACCOUNT_TYPE_AND_DATA_SET = "account_type_and_data_set";
    public static final String AUTO_ADD = "auto_add";
    public static final String DATA_SET = "data_set";
    public static final String DELETED = "deleted";
    public static final String FAVORITES = "favorites";
    public static final String GROUP_IS_READ_ONLY = "group_is_read_only";
    public static final String GROUP_VISIBLE = "group_visible";
    public static final String NOTES = "notes";
    public static final String PARAM_RETURN_GROUP_COUNT_PER_ACCOUNT = "return_group_count_per_account";
    public static final String RES_PACKAGE = "res_package";
    public static final String SHOULD_SYNC = "should_sync";
    public static final String SUMMARY_COUNT = "summ_count";
    public static final String SUMMARY_GROUP_COUNT_PER_ACCOUNT = "group_count_per_account";
    public static final String SUMMARY_WITH_PHONES = "summ_phones";
    public static final String SYSTEM_ID = "system_id";
    public static final String TITLE = "title";
    public static final String TITLE_RES = "title_res";
  }
  
  public static final class Intents
  {
    public static final String ACTION_GET_MULTIPLE_PHONES = "com.android.contacts.action.GET_MULTIPLE_PHONES";
    public static final String ACTION_PROFILE_CHANGED = "android.provider.Contacts.PROFILE_CHANGED";
    public static final String ACTION_VOICE_SEND_MESSAGE_TO_CONTACTS = "android.provider.action.VOICE_SEND_MESSAGE_TO_CONTACTS";
    public static final String ATTACH_IMAGE = "com.android.contacts.action.ATTACH_IMAGE";
    public static final String CONTACTS_DATABASE_CREATED = "android.provider.Contacts.DATABASE_CREATED";
    public static final String EXTRA_CREATE_DESCRIPTION = "com.android.contacts.action.CREATE_DESCRIPTION";
    @Deprecated
    public static final String EXTRA_EXCLUDE_MIMES = "exclude_mimes";
    public static final String EXTRA_FORCE_CREATE = "com.android.contacts.action.FORCE_CREATE";
    @Deprecated
    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_PHONE_URIS = "com.android.contacts.extra.PHONE_URIS";
    public static final String EXTRA_RECIPIENT_CONTACT_CHAT_ID = "android.provider.extra.RECIPIENT_CONTACT_CHAT_ID";
    public static final String EXTRA_RECIPIENT_CONTACT_NAME = "android.provider.extra.RECIPIENT_CONTACT_NAME";
    public static final String EXTRA_RECIPIENT_CONTACT_URI = "android.provider.extra.RECIPIENT_CONTACT_URI";
    @Deprecated
    public static final String EXTRA_TARGET_RECT = "target_rect";
    public static final String INVITE_CONTACT = "com.android.contacts.action.INVITE_CONTACT";
    public static final String METADATA_ACCOUNT_TYPE = "android.provider.account_type";
    public static final String METADATA_MIMETYPE = "android.provider.mimetype";
    @Deprecated
    public static final int MODE_LARGE = 3;
    @Deprecated
    public static final int MODE_MEDIUM = 2;
    @Deprecated
    public static final int MODE_SMALL = 1;
    public static final String SEARCH_SUGGESTION_CLICKED = "android.provider.Contacts.SEARCH_SUGGESTION_CLICKED";
    public static final String SEARCH_SUGGESTION_CREATE_CONTACT_CLICKED = "android.provider.Contacts.SEARCH_SUGGESTION_CREATE_CONTACT_CLICKED";
    public static final String SEARCH_SUGGESTION_DIAL_NUMBER_CLICKED = "android.provider.Contacts.SEARCH_SUGGESTION_DIAL_NUMBER_CLICKED";
    public static final String SHOW_OR_CREATE_CONTACT = "com.android.contacts.action.SHOW_OR_CREATE_CONTACT";
    
    public Intents() {}
    
    public static final class Insert
    {
      public static final String ACTION = "android.intent.action.INSERT";
      public static final String COMPANY = "company";
      public static final String DATA = "data";
      public static final String EMAIL = "email";
      public static final String EMAIL_ISPRIMARY = "email_isprimary";
      public static final String EMAIL_TYPE = "email_type";
      public static final String EXTRA_ACCOUNT = "android.provider.extra.ACCOUNT";
      public static final String EXTRA_DATA_SET = "android.provider.extra.DATA_SET";
      public static final String FULL_MODE = "full_mode";
      public static final String IM_HANDLE = "im_handle";
      public static final String IM_ISPRIMARY = "im_isprimary";
      public static final String IM_PROTOCOL = "im_protocol";
      public static final String JOB_TITLE = "job_title";
      public static final String NAME = "name";
      public static final String NOTES = "notes";
      public static final String PHONE = "phone";
      public static final String PHONETIC_NAME = "phonetic_name";
      public static final String PHONE_ISPRIMARY = "phone_isprimary";
      public static final String PHONE_TYPE = "phone_type";
      public static final String POSTAL = "postal";
      public static final String POSTAL_ISPRIMARY = "postal_isprimary";
      public static final String POSTAL_TYPE = "postal_type";
      public static final String SECONDARY_EMAIL = "secondary_email";
      public static final String SECONDARY_EMAIL_TYPE = "secondary_email_type";
      public static final String SECONDARY_PHONE = "secondary_phone";
      public static final String SECONDARY_PHONE_TYPE = "secondary_phone_type";
      public static final String TERTIARY_EMAIL = "tertiary_email";
      public static final String TERTIARY_EMAIL_TYPE = "tertiary_email_type";
      public static final String TERTIARY_PHONE = "tertiary_phone";
      public static final String TERTIARY_PHONE_TYPE = "tertiary_phone_type";
      
      public Insert() {}
    }
  }
  
  @SystemApi
  public static final class MetadataSync
    implements BaseColumns, ContactsContract.MetadataSyncColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact_metadata";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contact_metadata";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(METADATA_AUTHORITY_URI, "metadata_sync");
    public static final String METADATA_AUTHORITY = "com.android.contacts.metadata";
    public static final Uri METADATA_AUTHORITY_URI = Uri.parse("content://com.android.contacts.metadata");
    
    private MetadataSync() {}
  }
  
  @SystemApi
  protected static abstract interface MetadataSyncColumns
  {
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String DATA = "data";
    public static final String DATA_SET = "data_set";
    public static final String DELETED = "deleted";
    public static final String RAW_CONTACT_BACKUP_ID = "raw_contact_backup_id";
  }
  
  @SystemApi
  public static final class MetadataSyncState
    implements BaseColumns, ContactsContract.MetadataSyncStateColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact_metadata_sync_state";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contact_metadata_sync_state";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.MetadataSync.METADATA_AUTHORITY_URI, "metadata_sync_state");
    
    private MetadataSyncState() {}
  }
  
  @SystemApi
  protected static abstract interface MetadataSyncStateColumns
  {
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String DATA_SET = "data_set";
    public static final String STATE = "state";
  }
  
  public static final class PhoneLookup
    implements BaseColumns, ContactsContract.PhoneLookupColumns, ContactsContract.ContactsColumns, ContactsContract.ContactOptionsColumns, ContactsContract.ContactNameColumns
  {
    public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "phone_lookup");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/phone_lookup";
    public static final Uri ENTERPRISE_CONTENT_FILTER_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "phone_lookup_enterprise");
    public static final String QUERY_PARAMETER_SIP_ADDRESS = "sip";
    
    private PhoneLookup() {}
  }
  
  protected static abstract interface PhoneLookupColumns
  {
    public static final String CONTACT_ID = "contact_id";
    public static final String DATA_ID = "data_id";
    public static final String LABEL = "label";
    public static final String NORMALIZED_NUMBER = "normalized_number";
    public static final String NUMBER = "number";
    public static final String TYPE = "type";
  }
  
  public static abstract interface PhoneticNameStyle
  {
    public static final int JAPANESE = 4;
    public static final int KOREAN = 5;
    public static final int PINYIN = 3;
    public static final int UNDEFINED = 0;
  }
  
  public static final class PhotoFiles
    implements BaseColumns, ContactsContract.PhotoFilesColumns
  {
    private PhotoFiles() {}
  }
  
  protected static abstract interface PhotoFilesColumns
  {
    public static final String FILESIZE = "filesize";
    public static final String HEIGHT = "height";
    public static final String WIDTH = "width";
  }
  
  public static final class PinnedPositions
  {
    public static final int DEMOTED = -1;
    public static final String UNDEMOTE_METHOD = "undemote";
    public static final int UNPINNED = 0;
    
    public PinnedPositions() {}
    
    public static void pin(ContentResolver paramContentResolver, long paramLong, int paramInt)
    {
      Uri localUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(paramLong));
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("pinned", Integer.valueOf(paramInt));
      paramContentResolver.update(localUri, localContentValues, null, null);
    }
    
    public static void undemote(ContentResolver paramContentResolver, long paramLong)
    {
      paramContentResolver.call(ContactsContract.AUTHORITY_URI, "undemote", String.valueOf(paramLong), null);
    }
  }
  
  @Deprecated
  public static final class Presence
    extends ContactsContract.StatusUpdates
  {
    public Presence()
    {
      super();
    }
  }
  
  protected static abstract interface PresenceColumns
  {
    public static final String CUSTOM_PROTOCOL = "custom_protocol";
    public static final String DATA_ID = "presence_data_id";
    public static final String IM_ACCOUNT = "im_account";
    public static final String IM_HANDLE = "im_handle";
    public static final String PROTOCOL = "protocol";
  }
  
  public static final class Profile
    implements BaseColumns, ContactsContract.ContactsColumns, ContactsContract.ContactOptionsColumns, ContactsContract.ContactNameColumns, ContactsContract.ContactStatusColumns
  {
    public static final Uri CONTENT_RAW_CONTACTS_URI = Uri.withAppendedPath(CONTENT_URI, "raw_contacts");
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "profile");
    public static final Uri CONTENT_VCARD_URI = Uri.withAppendedPath(CONTENT_URI, "as_vcard");
    public static final long MIN_ID = 9223372034707292160L;
    
    private Profile() {}
  }
  
  public static final class ProfileSyncState
    implements SyncStateContract.Columns
  {
    public static final String CONTENT_DIRECTORY = "syncstate";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, "syncstate");
    
    private ProfileSyncState() {}
    
    public static byte[] get(ContentProviderClient paramContentProviderClient, Account paramAccount)
      throws RemoteException
    {
      return SyncStateContract.Helpers.get(paramContentProviderClient, CONTENT_URI, paramAccount);
    }
    
    public static Pair<Uri, byte[]> getWithUri(ContentProviderClient paramContentProviderClient, Account paramAccount)
      throws RemoteException
    {
      return SyncStateContract.Helpers.getWithUri(paramContentProviderClient, CONTENT_URI, paramAccount);
    }
    
    public static ContentProviderOperation newSetOperation(Account paramAccount, byte[] paramArrayOfByte)
    {
      return SyncStateContract.Helpers.newSetOperation(CONTENT_URI, paramAccount, paramArrayOfByte);
    }
    
    public static void set(ContentProviderClient paramContentProviderClient, Account paramAccount, byte[] paramArrayOfByte)
      throws RemoteException
    {
      SyncStateContract.Helpers.set(paramContentProviderClient, CONTENT_URI, paramAccount, paramArrayOfByte);
    }
  }
  
  public static final class ProviderStatus
  {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/provider_status";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "provider_status");
    public static final String DATABASE_CREATION_TIMESTAMP = "database_creation_timestamp";
    public static final String STATUS = "status";
    public static final int STATUS_BUSY = 1;
    public static final int STATUS_EMPTY = 2;
    public static final int STATUS_NORMAL = 0;
    
    private ProviderStatus() {}
  }
  
  public static final class QuickContact
  {
    public static final String ACTION_QUICK_CONTACT = "android.provider.action.QUICK_CONTACT";
    public static final String EXTRA_EXCLUDE_MIMES = "android.provider.extra.EXCLUDE_MIMES";
    public static final String EXTRA_MODE = "android.provider.extra.MODE";
    public static final String EXTRA_PRIORITIZED_MIMETYPE = "android.provider.extra.PRIORITIZED_MIMETYPE";
    @Deprecated
    public static final String EXTRA_TARGET_RECT = "android.provider.extra.TARGET_RECT";
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_LARGE = 3;
    public static final int MODE_MEDIUM = 2;
    public static final int MODE_SMALL = 1;
    
    public QuickContact() {}
    
    public static Intent composeQuickContactsIntent(Context paramContext, Rect paramRect, Uri paramUri, int paramInt, String[] paramArrayOfString)
    {
      while (((paramContext instanceof ContextWrapper)) && (!(paramContext instanceof Activity))) {
        paramContext = ((ContextWrapper)paramContext).getBaseContext();
      }
      int i;
      if ((paramContext instanceof Activity)) {
        i = 0;
      } else {
        i = 268468224;
      }
      paramContext = new Intent("android.provider.action.QUICK_CONTACT").addFlags(i | 0x20000000);
      paramContext.setData(paramUri);
      paramContext.setSourceBounds(paramRect);
      paramContext.putExtra("android.provider.extra.MODE", paramInt);
      paramContext.putExtra("android.provider.extra.EXCLUDE_MIMES", paramArrayOfString);
      return paramContext;
    }
    
    public static Intent composeQuickContactsIntent(Context paramContext, View paramView, Uri paramUri, int paramInt, String[] paramArrayOfString)
    {
      float f = getResourcesgetCompatibilityInfoapplicationScale;
      int[] arrayOfInt = new int[2];
      paramView.getLocationOnScreen(arrayOfInt);
      Rect localRect = new Rect();
      left = ((int)(arrayOfInt[0] * f + 0.5F));
      top = ((int)(arrayOfInt[1] * f + 0.5F));
      right = ((int)((arrayOfInt[0] + paramView.getWidth()) * f + 0.5F));
      bottom = ((int)((arrayOfInt[1] + paramView.getHeight()) * f + 0.5F));
      return composeQuickContactsIntent(paramContext, localRect, paramUri, paramInt, paramArrayOfString);
    }
    
    public static Intent rebuildManagedQuickContactsIntent(String paramString, long paramLong1, boolean paramBoolean, long paramLong2, Intent paramIntent)
    {
      Intent localIntent = new Intent("android.provider.action.QUICK_CONTACT");
      String str = null;
      if (!TextUtils.isEmpty(paramString))
      {
        if (paramBoolean) {
          paramString = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, paramString);
        } else {
          paramString = ContactsContract.Contacts.getLookupUri(paramLong1, paramString);
        }
        str = paramString;
      }
      paramString = str;
      if (str != null)
      {
        paramString = str;
        if (paramLong2 != 0L) {
          paramString = str.buildUpon().appendQueryParameter("directory", String.valueOf(paramLong2)).build();
        }
      }
      localIntent.setData(paramString);
      localIntent.setFlags(paramIntent.getFlags() | 0x10000000);
      localIntent.setSourceBounds(paramIntent.getSourceBounds());
      localIntent.putExtra("android.provider.extra.MODE", paramIntent.getIntExtra("android.provider.extra.MODE", 3));
      localIntent.putExtra("android.provider.extra.EXCLUDE_MIMES", paramIntent.getStringArrayExtra("android.provider.extra.EXCLUDE_MIMES"));
      return localIntent;
    }
    
    public static void showQuickContact(Context paramContext, Rect paramRect, Uri paramUri, int paramInt, String[] paramArrayOfString)
    {
      ContactsInternal.startQuickContactWithErrorToast(paramContext, composeQuickContactsIntent(paramContext, paramRect, paramUri, paramInt, paramArrayOfString));
    }
    
    public static void showQuickContact(Context paramContext, Rect paramRect, Uri paramUri, String[] paramArrayOfString, String paramString)
    {
      paramRect = composeQuickContactsIntent(paramContext, paramRect, paramUri, 3, paramArrayOfString);
      paramRect.putExtra("android.provider.extra.PRIORITIZED_MIMETYPE", paramString);
      ContactsInternal.startQuickContactWithErrorToast(paramContext, paramRect);
    }
    
    public static void showQuickContact(Context paramContext, View paramView, Uri paramUri, int paramInt, String[] paramArrayOfString)
    {
      ContactsInternal.startQuickContactWithErrorToast(paramContext, composeQuickContactsIntent(paramContext, paramView, paramUri, paramInt, paramArrayOfString));
    }
    
    public static void showQuickContact(Context paramContext, View paramView, Uri paramUri, String[] paramArrayOfString, String paramString)
    {
      paramView = composeQuickContactsIntent(paramContext, paramView, paramUri, 3, paramArrayOfString);
      paramView.putExtra("android.provider.extra.PRIORITIZED_MIMETYPE", paramString);
      ContactsInternal.startQuickContactWithErrorToast(paramContext, paramView);
    }
  }
  
  public static final class RawContacts
    implements BaseColumns, ContactsContract.RawContactsColumns, ContactsContract.ContactOptionsColumns, ContactsContract.ContactNameColumns, ContactsContract.SyncColumns
  {
    public static final int AGGREGATION_MODE_DEFAULT = 0;
    public static final int AGGREGATION_MODE_DISABLED = 3;
    @Deprecated
    public static final int AGGREGATION_MODE_IMMEDIATE = 1;
    public static final int AGGREGATION_MODE_SUSPENDED = 2;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/raw_contact";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/raw_contact";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "raw_contacts");
    
    private RawContacts() {}
    
    public static Uri getContactLookupUri(ContentResolver paramContentResolver, Uri paramUri)
    {
      SeempLog.record(89);
      paramContentResolver = paramContentResolver.query(Uri.withAppendedPath(paramUri, "data"), new String[] { "contact_id", "lookup" }, null, null, null);
      if (paramContentResolver != null) {
        try
        {
          if (paramContentResolver.moveToFirst())
          {
            paramUri = ContactsContract.Contacts.getLookupUri(paramContentResolver.getLong(0), paramContentResolver.getString(1));
            return paramUri;
          }
        }
        finally
        {
          if (paramContentResolver != null) {
            paramContentResolver.close();
          }
        }
      }
      if (paramContentResolver != null) {
        paramContentResolver.close();
      }
      return null;
    }
    
    public static EntityIterator newEntityIterator(Cursor paramCursor)
    {
      return new EntityIteratorImpl(paramCursor);
    }
    
    public static final class Data
      implements BaseColumns, ContactsContract.DataColumns
    {
      public static final String CONTENT_DIRECTORY = "data";
      
      private Data() {}
    }
    
    public static final class DisplayPhoto
    {
      public static final String CONTENT_DIRECTORY = "display_photo";
      
      private DisplayPhoto() {}
    }
    
    public static final class Entity
      implements BaseColumns, ContactsContract.DataColumns
    {
      public static final String CONTENT_DIRECTORY = "entity";
      public static final String DATA_ID = "data_id";
      
      private Entity() {}
    }
    
    private static class EntityIteratorImpl
      extends CursorEntityIterator
    {
      private static final String[] DATA_KEYS = { "data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10", "data11", "data12", "data13", "data14", "data15", "data_sync1", "data_sync2", "data_sync3", "data_sync4" };
      
      public EntityIteratorImpl(Cursor paramCursor)
      {
        super();
      }
      
      public Entity getEntityAndIncrementCursor(Cursor paramCursor)
        throws RemoteException
      {
        int i = paramCursor.getColumnIndexOrThrow("_id");
        long l = paramCursor.getLong(i);
        Object localObject = new ContentValues();
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "account_name");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "account_type");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "data_set");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "_id");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "dirty");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "version");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "sourceid");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "sync1");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "sync2");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "sync3");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "sync4");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "deleted");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "contact_id");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "starred");
        Entity localEntity = new Entity((ContentValues)localObject);
        do
        {
          if (l != paramCursor.getLong(i)) {
            break;
          }
          ContentValues localContentValues = new ContentValues();
          localContentValues.put("_id", Long.valueOf(paramCursor.getLong(paramCursor.getColumnIndexOrThrow("data_id"))));
          DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "res_package");
          DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "mimetype");
          DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "is_primary");
          DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "is_super_primary");
          DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, localContentValues, "data_version");
          DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "group_sourceid");
          DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, localContentValues, "data_version");
          for (String str : DATA_KEYS)
          {
            int m = paramCursor.getColumnIndexOrThrow(str);
            switch (paramCursor.getType(m))
            {
            default: 
              throw new IllegalStateException("Invalid or unhandled data type");
            case 4: 
              localContentValues.put(str, paramCursor.getBlob(m));
              break;
            case 1: 
            case 2: 
            case 3: 
              localContentValues.put(str, paramCursor.getString(m));
            }
          }
          localEntity.addSubValue(ContactsContract.Data.CONTENT_URI, localContentValues);
        } while (paramCursor.moveToNext());
        return localEntity;
      }
    }
    
    @Deprecated
    public static final class StreamItems
      implements BaseColumns, ContactsContract.StreamItemsColumns
    {
      @Deprecated
      public static final String CONTENT_DIRECTORY = "stream_items";
      
      @Deprecated
      private StreamItems() {}
    }
  }
  
  protected static abstract interface RawContactsColumns
  {
    public static final String ACCOUNT_TYPE_AND_DATA_SET = "account_type_and_data_set";
    public static final String AGGREGATION_MODE = "aggregation_mode";
    public static final String BACKUP_ID = "backup_id";
    public static final String CONTACT_ID = "contact_id";
    public static final String DATA_SET = "data_set";
    public static final String DELETED = "deleted";
    public static final String METADATA_DIRTY = "metadata_dirty";
    public static final String RAW_CONTACT_IS_READ_ONLY = "raw_contact_is_read_only";
    public static final String RAW_CONTACT_IS_USER_PROFILE = "raw_contact_is_user_profile";
  }
  
  public static final class RawContactsEntity
    implements BaseColumns, ContactsContract.DataColumns, ContactsContract.RawContactsColumns
  {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/raw_contact_entity";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "raw_contact_entities");
    public static final Uri CORP_CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "raw_contact_entities_corp");
    public static final String DATA_ID = "data_id";
    public static final String FOR_EXPORT_ONLY = "for_export_only";
    public static final Uri PROFILE_CONTENT_URI = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, "raw_contact_entities");
    
    private RawContactsEntity() {}
  }
  
  public static class SearchSnippets
  {
    public static final String DEFERRED_SNIPPETING_KEY = "deferred_snippeting";
    public static final String SNIPPET = "snippet";
    public static final String SNIPPET_ARGS_PARAM_KEY = "snippet_args";
    
    public SearchSnippets() {}
  }
  
  public static final class Settings
    implements ContactsContract.SettingsColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/setting";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/setting";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "settings");
    
    private Settings() {}
  }
  
  protected static abstract interface SettingsColumns
  {
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String ANY_UNSYNCED = "any_unsynced";
    public static final String DATA_SET = "data_set";
    public static final String SHOULD_SYNC = "should_sync";
    public static final String UNGROUPED_COUNT = "summ_count";
    public static final String UNGROUPED_VISIBLE = "ungrouped_visible";
    public static final String UNGROUPED_WITH_PHONES = "summ_phones";
  }
  
  protected static abstract interface StatusColumns
  {
    public static final int AVAILABLE = 5;
    public static final int AWAY = 2;
    public static final int CAPABILITY_HAS_CAMERA = 4;
    public static final int CAPABILITY_HAS_VIDEO = 2;
    public static final int CAPABILITY_HAS_VOICE = 1;
    public static final String CHAT_CAPABILITY = "chat_capability";
    public static final int DO_NOT_DISTURB = 4;
    public static final int IDLE = 3;
    public static final int INVISIBLE = 1;
    public static final int OFFLINE = 0;
    public static final String PRESENCE = "mode";
    @Deprecated
    public static final String PRESENCE_CUSTOM_STATUS = "status";
    @Deprecated
    public static final String PRESENCE_STATUS = "mode";
    public static final String STATUS = "status";
    public static final String STATUS_ICON = "status_icon";
    public static final String STATUS_LABEL = "status_label";
    public static final String STATUS_RES_PACKAGE = "status_res_package";
    public static final String STATUS_TIMESTAMP = "status_ts";
  }
  
  public static class StatusUpdates
    implements ContactsContract.StatusColumns, ContactsContract.PresenceColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/status-update";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/status-update";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "status_updates");
    public static final Uri PROFILE_CONTENT_URI = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, "status_updates");
    
    private StatusUpdates() {}
    
    public static final int getPresenceIconResourceId(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 17301610;
      case 5: 
        return 17301611;
      case 4: 
        return 17301608;
      case 2: 
      case 3: 
        return 17301607;
      }
      return 17301609;
    }
    
    public static final int getPresencePrecedence(int paramInt)
    {
      return paramInt;
    }
  }
  
  @Deprecated
  public static final class StreamItemPhotos
    implements BaseColumns, ContactsContract.StreamItemPhotosColumns
  {
    @Deprecated
    public static final String PHOTO = "photo";
    
    @Deprecated
    private StreamItemPhotos() {}
  }
  
  @Deprecated
  protected static abstract interface StreamItemPhotosColumns
  {
    @Deprecated
    public static final String PHOTO_FILE_ID = "photo_file_id";
    @Deprecated
    public static final String PHOTO_URI = "photo_uri";
    @Deprecated
    public static final String SORT_INDEX = "sort_index";
    @Deprecated
    public static final String STREAM_ITEM_ID = "stream_item_id";
    @Deprecated
    public static final String SYNC1 = "stream_item_photo_sync1";
    @Deprecated
    public static final String SYNC2 = "stream_item_photo_sync2";
    @Deprecated
    public static final String SYNC3 = "stream_item_photo_sync3";
    @Deprecated
    public static final String SYNC4 = "stream_item_photo_sync4";
  }
  
  @Deprecated
  public static final class StreamItems
    implements BaseColumns, ContactsContract.StreamItemsColumns
  {
    @Deprecated
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/stream_item";
    @Deprecated
    public static final Uri CONTENT_LIMIT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "stream_items_limit");
    @Deprecated
    public static final Uri CONTENT_PHOTO_URI;
    @Deprecated
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/stream_item";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "stream_items");
    @Deprecated
    public static final String MAX_ITEMS = "max_items";
    
    static
    {
      CONTENT_PHOTO_URI = Uri.withAppendedPath(CONTENT_URI, "photo");
    }
    
    @Deprecated
    private StreamItems() {}
    
    @Deprecated
    public static final class StreamItemPhotos
      implements BaseColumns, ContactsContract.StreamItemPhotosColumns
    {
      @Deprecated
      public static final String CONTENT_DIRECTORY = "photo";
      @Deprecated
      public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/stream_item_photo";
      @Deprecated
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/stream_item_photo";
      
      @Deprecated
      private StreamItemPhotos() {}
    }
  }
  
  @Deprecated
  protected static abstract interface StreamItemsColumns
  {
    @Deprecated
    public static final String ACCOUNT_NAME = "account_name";
    @Deprecated
    public static final String ACCOUNT_TYPE = "account_type";
    @Deprecated
    public static final String COMMENTS = "comments";
    @Deprecated
    public static final String CONTACT_ID = "contact_id";
    @Deprecated
    public static final String CONTACT_LOOKUP_KEY = "contact_lookup";
    @Deprecated
    public static final String DATA_SET = "data_set";
    @Deprecated
    public static final String RAW_CONTACT_ID = "raw_contact_id";
    @Deprecated
    public static final String RAW_CONTACT_SOURCE_ID = "raw_contact_source_id";
    @Deprecated
    public static final String RES_ICON = "icon";
    @Deprecated
    public static final String RES_LABEL = "label";
    @Deprecated
    public static final String RES_PACKAGE = "res_package";
    @Deprecated
    public static final String SYNC1 = "stream_item_sync1";
    @Deprecated
    public static final String SYNC2 = "stream_item_sync2";
    @Deprecated
    public static final String SYNC3 = "stream_item_sync3";
    @Deprecated
    public static final String SYNC4 = "stream_item_sync4";
    @Deprecated
    public static final String TEXT = "text";
    @Deprecated
    public static final String TIMESTAMP = "timestamp";
  }
  
  protected static abstract interface SyncColumns
    extends ContactsContract.BaseSyncColumns
  {
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String DIRTY = "dirty";
    public static final String SOURCE_ID = "sourceid";
    public static final String VERSION = "version";
  }
  
  public static final class SyncState
    implements SyncStateContract.Columns
  {
    public static final String CONTENT_DIRECTORY = "syncstate";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "syncstate");
    
    private SyncState() {}
    
    public static byte[] get(ContentProviderClient paramContentProviderClient, Account paramAccount)
      throws RemoteException
    {
      return SyncStateContract.Helpers.get(paramContentProviderClient, CONTENT_URI, paramAccount);
    }
    
    public static Pair<Uri, byte[]> getWithUri(ContentProviderClient paramContentProviderClient, Account paramAccount)
      throws RemoteException
    {
      return SyncStateContract.Helpers.getWithUri(paramContentProviderClient, CONTENT_URI, paramAccount);
    }
    
    public static ContentProviderOperation newSetOperation(Account paramAccount, byte[] paramArrayOfByte)
    {
      return SyncStateContract.Helpers.newSetOperation(CONTENT_URI, paramAccount, paramArrayOfByte);
    }
    
    public static void set(ContentProviderClient paramContentProviderClient, Account paramAccount, byte[] paramArrayOfByte)
      throws RemoteException
    {
      SyncStateContract.Helpers.set(paramContentProviderClient, CONTENT_URI, paramAccount, paramArrayOfByte);
    }
  }
  
  @Deprecated
  public static abstract interface SyncStateColumns
    extends SyncStateContract.Columns
  {}
}
