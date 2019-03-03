package android.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Deprecated
public class Contacts
{
  @Deprecated
  public static final String AUTHORITY = "contacts";
  @Deprecated
  public static final Uri CONTENT_URI = Uri.parse("content://contacts");
  @Deprecated
  public static final int KIND_EMAIL = 1;
  @Deprecated
  public static final int KIND_IM = 3;
  @Deprecated
  public static final int KIND_ORGANIZATION = 4;
  @Deprecated
  public static final int KIND_PHONE = 5;
  @Deprecated
  public static final int KIND_POSTAL = 2;
  private static final String TAG = "Contacts";
  
  private Contacts() {}
  
  @Deprecated
  public static final class ContactMethods
    implements BaseColumns, Contacts.ContactMethodsColumns, Contacts.PeopleColumns
  {
    @Deprecated
    public static final String CONTENT_EMAIL_ITEM_TYPE = "vnd.android.cursor.item/email";
    @Deprecated
    public static final String CONTENT_EMAIL_TYPE = "vnd.android.cursor.dir/email";
    @Deprecated
    public static final Uri CONTENT_EMAIL_URI = Uri.parse("content://contacts/contact_methods/email");
    @Deprecated
    public static final String CONTENT_IM_ITEM_TYPE = "vnd.android.cursor.item/jabber-im";
    @Deprecated
    public static final String CONTENT_POSTAL_ITEM_TYPE = "vnd.android.cursor.item/postal-address";
    @Deprecated
    public static final String CONTENT_POSTAL_TYPE = "vnd.android.cursor.dir/postal-address";
    @Deprecated
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contact-methods";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/contact_methods");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "name ASC";
    @Deprecated
    public static final String PERSON_ID = "person";
    @Deprecated
    public static final String POSTAL_LOCATION_LATITUDE = "data";
    @Deprecated
    public static final String POSTAL_LOCATION_LONGITUDE = "aux_data";
    @Deprecated
    public static final int PROTOCOL_AIM = 0;
    @Deprecated
    public static final int PROTOCOL_GOOGLE_TALK = 5;
    @Deprecated
    public static final int PROTOCOL_ICQ = 6;
    @Deprecated
    public static final int PROTOCOL_JABBER = 7;
    @Deprecated
    public static final int PROTOCOL_MSN = 1;
    @Deprecated
    public static final int PROTOCOL_QQ = 4;
    @Deprecated
    public static final int PROTOCOL_SKYPE = 3;
    @Deprecated
    public static final int PROTOCOL_YAHOO = 2;
    
    private ContactMethods() {}
    
    @Deprecated
    public static Object decodeImProtocol(String paramString)
    {
      if (paramString == null) {
        return null;
      }
      if (paramString.startsWith("pre:")) {
        return Integer.valueOf(Integer.parseInt(paramString.substring(4)));
      }
      if (paramString.startsWith("custom:")) {
        return paramString.substring(7);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("the value is not a valid encoded protocol, ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    @Deprecated
    public static String encodeCustomImProtocol(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("custom:");
      localStringBuilder.append(paramString);
      return localStringBuilder.toString();
    }
    
    @Deprecated
    public static String encodePredefinedImProtocol(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("pre:");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    }
    
    @Deprecated
    public static final CharSequence getDisplayLabel(Context paramContext, int paramInt1, int paramInt2, CharSequence paramCharSequence)
    {
      String str = "";
      switch (paramInt1)
      {
      default: 
        paramContext = paramContext.getString(17039375);
        break;
      case 2: 
        if (paramInt2 != 0)
        {
          paramContext = paramContext.getResources().getTextArray(17235972);
          paramContext = paramContext[(paramInt2 - 1)];
        }
        else
        {
          paramContext = str;
          if (!TextUtils.isEmpty(paramCharSequence)) {
            paramContext = paramCharSequence;
          }
        }
        break;
      case 1: 
        if (paramInt2 != 0)
        {
          paramContext = paramContext.getResources().getTextArray(17235968);
          paramContext = paramContext[(paramInt2 - 1)];
        }
        else
        {
          paramContext = str;
          if (!TextUtils.isEmpty(paramCharSequence)) {
            paramContext = paramCharSequence;
          }
        }
        break;
      }
      return paramContext;
    }
    
    @Deprecated
    public static String lookupProviderNameFromId(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return null;
      case 7: 
        return "JABBER";
      case 6: 
        return "ICQ";
      case 5: 
        return "GTalk";
      case 4: 
        return "QQ";
      case 3: 
        return "SKYPE";
      case 2: 
        return "Yahoo";
      case 1: 
        return "MSN";
      }
      return "AIM";
    }
    
    @Deprecated
    public void addPostalLocation(Context paramContext, long paramLong, double paramDouble1, double paramDouble2)
    {
      paramContext = paramContext.getContentResolver();
      ContentValues localContentValues = new ContentValues(2);
      localContentValues.put("data", Double.valueOf(paramDouble1));
      localContentValues.put("aux_data", Double.valueOf(paramDouble2));
      long l = ContentUris.parseId(paramContext.insert(CONTENT_URI, localContentValues));
      localContentValues.clear();
      localContentValues.put("aux_data", Long.valueOf(l));
      paramContext.update(ContentUris.withAppendedId(CONTENT_URI, paramLong), localContentValues, null, null);
    }
    
    static abstract interface ProviderNames
    {
      public static final String AIM = "AIM";
      public static final String GTALK = "GTalk";
      public static final String ICQ = "ICQ";
      public static final String JABBER = "JABBER";
      public static final String MSN = "MSN";
      public static final String QQ = "QQ";
      public static final String SKYPE = "SKYPE";
      public static final String XMPP = "XMPP";
      public static final String YAHOO = "Yahoo";
    }
  }
  
  @Deprecated
  public static abstract interface ContactMethodsColumns
  {
    @Deprecated
    public static final String AUX_DATA = "aux_data";
    @Deprecated
    public static final String DATA = "data";
    @Deprecated
    public static final String ISPRIMARY = "isprimary";
    @Deprecated
    public static final String KIND = "kind";
    @Deprecated
    public static final String LABEL = "label";
    @Deprecated
    public static final int MOBILE_EMAIL_TYPE_INDEX = 2;
    @Deprecated
    public static final String MOBILE_EMAIL_TYPE_NAME = "_AUTO_CELL";
    @Deprecated
    public static final String TYPE = "type";
    @Deprecated
    public static final int TYPE_CUSTOM = 0;
    @Deprecated
    public static final int TYPE_HOME = 1;
    @Deprecated
    public static final int TYPE_OTHER = 3;
    @Deprecated
    public static final int TYPE_WORK = 2;
  }
  
  @Deprecated
  public static final class Extensions
    implements BaseColumns, Contacts.ExtensionsColumns
  {
    @Deprecated
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contact_extensions";
    @Deprecated
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contact_extensions";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/extensions");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "person, name ASC";
    @Deprecated
    public static final String PERSON_ID = "person";
    
    private Extensions() {}
  }
  
  @Deprecated
  public static abstract interface ExtensionsColumns
  {
    @Deprecated
    public static final String NAME = "name";
    @Deprecated
    public static final String VALUE = "value";
  }
  
  @Deprecated
  public static final class GroupMembership
    implements BaseColumns, Contacts.GroupsColumns
  {
    @Deprecated
    public static final String CONTENT_DIRECTORY = "groupmembership";
    @Deprecated
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contactsgroupmembership";
    @Deprecated
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contactsgroupmembership";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/groupmembership");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "group_id ASC";
    @Deprecated
    public static final String GROUP_ID = "group_id";
    @Deprecated
    public static final String GROUP_SYNC_ACCOUNT = "group_sync_account";
    @Deprecated
    public static final String GROUP_SYNC_ACCOUNT_TYPE = "group_sync_account_type";
    @Deprecated
    public static final String GROUP_SYNC_ID = "group_sync_id";
    @Deprecated
    public static final String PERSON_ID = "person";
    @Deprecated
    public static final Uri RAW_CONTENT_URI = Uri.parse("content://contacts/groupmembershipraw");
    
    private GroupMembership() {}
  }
  
  @Deprecated
  public static final class Groups
    implements BaseColumns, Contacts.GroupsColumns
  {
    @Deprecated
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/contactsgroup";
    @Deprecated
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/contactsgroup";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/groups");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "name ASC";
    @Deprecated
    public static final Uri DELETED_CONTENT_URI = Uri.parse("content://contacts/deleted_groups");
    @Deprecated
    public static final String GROUP_ANDROID_STARRED = "Starred in Android";
    @Deprecated
    public static final String GROUP_MY_CONTACTS = "Contacts";
    
    private Groups() {}
  }
  
  @Deprecated
  public static abstract interface GroupsColumns
  {
    @Deprecated
    public static final String NAME = "name";
    @Deprecated
    public static final String NOTES = "notes";
    @Deprecated
    public static final String SHOULD_SYNC = "should_sync";
    @Deprecated
    public static final String SYSTEM_ID = "system_id";
  }
  
  @Deprecated
  public static final class Intents
  {
    @Deprecated
    public static final String ATTACH_IMAGE = "com.android.contacts.action.ATTACH_IMAGE";
    @Deprecated
    public static final String EXTRA_CREATE_DESCRIPTION = "com.android.contacts.action.CREATE_DESCRIPTION";
    @Deprecated
    public static final String EXTRA_FORCE_CREATE = "com.android.contacts.action.FORCE_CREATE";
    @Deprecated
    public static final String EXTRA_TARGET_RECT = "target_rect";
    @Deprecated
    public static final String SEARCH_SUGGESTION_CLICKED = "android.provider.Contacts.SEARCH_SUGGESTION_CLICKED";
    @Deprecated
    public static final String SEARCH_SUGGESTION_CREATE_CONTACT_CLICKED = "android.provider.Contacts.SEARCH_SUGGESTION_CREATE_CONTACT_CLICKED";
    @Deprecated
    public static final String SEARCH_SUGGESTION_DIAL_NUMBER_CLICKED = "android.provider.Contacts.SEARCH_SUGGESTION_DIAL_NUMBER_CLICKED";
    @Deprecated
    public static final String SHOW_OR_CREATE_CONTACT = "com.android.contacts.action.SHOW_OR_CREATE_CONTACT";
    
    @Deprecated
    public Intents() {}
    
    @Deprecated
    public static final class Insert
    {
      @Deprecated
      public static final String ACTION = "android.intent.action.INSERT";
      @Deprecated
      public static final String COMPANY = "company";
      @Deprecated
      public static final String EMAIL = "email";
      @Deprecated
      public static final String EMAIL_ISPRIMARY = "email_isprimary";
      @Deprecated
      public static final String EMAIL_TYPE = "email_type";
      @Deprecated
      public static final String FULL_MODE = "full_mode";
      @Deprecated
      public static final String IM_HANDLE = "im_handle";
      @Deprecated
      public static final String IM_ISPRIMARY = "im_isprimary";
      @Deprecated
      public static final String IM_PROTOCOL = "im_protocol";
      @Deprecated
      public static final String JOB_TITLE = "job_title";
      @Deprecated
      public static final String NAME = "name";
      @Deprecated
      public static final String NOTES = "notes";
      @Deprecated
      public static final String PHONE = "phone";
      @Deprecated
      public static final String PHONETIC_NAME = "phonetic_name";
      @Deprecated
      public static final String PHONE_ISPRIMARY = "phone_isprimary";
      @Deprecated
      public static final String PHONE_TYPE = "phone_type";
      @Deprecated
      public static final String POSTAL = "postal";
      @Deprecated
      public static final String POSTAL_ISPRIMARY = "postal_isprimary";
      @Deprecated
      public static final String POSTAL_TYPE = "postal_type";
      @Deprecated
      public static final String SECONDARY_EMAIL = "secondary_email";
      @Deprecated
      public static final String SECONDARY_EMAIL_TYPE = "secondary_email_type";
      @Deprecated
      public static final String SECONDARY_PHONE = "secondary_phone";
      @Deprecated
      public static final String SECONDARY_PHONE_TYPE = "secondary_phone_type";
      @Deprecated
      public static final String TERTIARY_EMAIL = "tertiary_email";
      @Deprecated
      public static final String TERTIARY_EMAIL_TYPE = "tertiary_email_type";
      @Deprecated
      public static final String TERTIARY_PHONE = "tertiary_phone";
      @Deprecated
      public static final String TERTIARY_PHONE_TYPE = "tertiary_phone_type";
      
      @Deprecated
      public Insert() {}
    }
    
    @Deprecated
    public static final class UI
    {
      @Deprecated
      public static final String FILTER_CONTACTS_ACTION = "com.android.contacts.action.FILTER_CONTACTS";
      @Deprecated
      public static final String FILTER_TEXT_EXTRA_KEY = "com.android.contacts.extra.FILTER_TEXT";
      @Deprecated
      public static final String GROUP_NAME_EXTRA_KEY = "com.android.contacts.extra.GROUP";
      @Deprecated
      public static final String LIST_ALL_CONTACTS_ACTION = "com.android.contacts.action.LIST_ALL_CONTACTS";
      @Deprecated
      public static final String LIST_CONTACTS_WITH_PHONES_ACTION = "com.android.contacts.action.LIST_CONTACTS_WITH_PHONES";
      @Deprecated
      public static final String LIST_DEFAULT = "com.android.contacts.action.LIST_DEFAULT";
      @Deprecated
      public static final String LIST_FREQUENT_ACTION = "com.android.contacts.action.LIST_FREQUENT";
      @Deprecated
      public static final String LIST_GROUP_ACTION = "com.android.contacts.action.LIST_GROUP";
      @Deprecated
      public static final String LIST_STARRED_ACTION = "com.android.contacts.action.LIST_STARRED";
      @Deprecated
      public static final String LIST_STREQUENT_ACTION = "com.android.contacts.action.LIST_STREQUENT";
      @Deprecated
      public static final String TITLE_EXTRA_KEY = "com.android.contacts.extra.TITLE_EXTRA";
      
      @Deprecated
      public UI() {}
    }
  }
  
  @Deprecated
  public static abstract interface OrganizationColumns
  {
    @Deprecated
    public static final String COMPANY = "company";
    @Deprecated
    public static final String ISPRIMARY = "isprimary";
    @Deprecated
    public static final String LABEL = "label";
    @Deprecated
    public static final String PERSON_ID = "person";
    @Deprecated
    public static final String TITLE = "title";
    @Deprecated
    public static final String TYPE = "type";
    @Deprecated
    public static final int TYPE_CUSTOM = 0;
    @Deprecated
    public static final int TYPE_OTHER = 2;
    @Deprecated
    public static final int TYPE_WORK = 1;
  }
  
  @Deprecated
  public static final class Organizations
    implements BaseColumns, Contacts.OrganizationColumns
  {
    @Deprecated
    public static final String CONTENT_DIRECTORY = "organizations";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/organizations");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "company, title, isprimary ASC";
    
    private Organizations() {}
    
    @Deprecated
    public static final CharSequence getDisplayLabel(Context paramContext, int paramInt, CharSequence paramCharSequence)
    {
      String str = "";
      if (paramInt != 0)
      {
        paramContext = paramContext.getResources().getTextArray(17235970);
        paramContext = paramContext[(paramInt - 1)];
      }
      else
      {
        paramContext = str;
        if (!TextUtils.isEmpty(paramCharSequence)) {
          paramContext = paramCharSequence;
        }
      }
      return paramContext;
    }
  }
  
  @Deprecated
  public static final class People
    implements BaseColumns, Contacts.PeopleColumns, Contacts.PhonesColumns, Contacts.PresenceColumns
  {
    @Deprecated
    public static final Uri CONTENT_FILTER_URI;
    @Deprecated
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/person";
    @Deprecated
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/person";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/people");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "name ASC";
    @Deprecated
    public static final Uri DELETED_CONTENT_URI;
    private static final String[] GROUPS_PROJECTION = { "_id" };
    @Deprecated
    public static final String PRIMARY_EMAIL_ID = "primary_email";
    @Deprecated
    public static final String PRIMARY_ORGANIZATION_ID = "primary_organization";
    @Deprecated
    public static final String PRIMARY_PHONE_ID = "primary_phone";
    @Deprecated
    public static final Uri WITH_EMAIL_OR_IM_FILTER_URI;
    
    static
    {
      CONTENT_FILTER_URI = Uri.parse("content://contacts/people/filter");
      DELETED_CONTENT_URI = Uri.parse("content://contacts/deleted_people");
      WITH_EMAIL_OR_IM_FILTER_URI = Uri.parse("content://contacts/people/with_email_or_im_filter");
    }
    
    @Deprecated
    private People() {}
    
    @Deprecated
    public static Uri addToGroup(ContentResolver paramContentResolver, long paramLong1, long paramLong2)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("person", Long.valueOf(paramLong1));
      localContentValues.put("group_id", Long.valueOf(paramLong2));
      return paramContentResolver.insert(Contacts.GroupMembership.CONTENT_URI, localContentValues);
    }
    
    @Deprecated
    public static Uri addToGroup(ContentResolver paramContentResolver, long paramLong, String paramString)
    {
      long l1 = 0L;
      paramString = paramContentResolver.query(Contacts.Groups.CONTENT_URI, GROUPS_PROJECTION, "name=?", new String[] { paramString }, null);
      long l2 = l1;
      if (paramString != null) {
        l2 = l1;
      }
      try
      {
        if (paramString.moveToFirst()) {
          l2 = paramString.getLong(0);
        }
        paramString.close();
      }
      finally
      {
        paramString.close();
      }
      return addToGroup(paramContentResolver, paramLong, l2);
      throw new IllegalStateException("Failed to find the My Contacts group");
    }
    
    @Deprecated
    public static Uri addToMyContactsGroup(ContentResolver paramContentResolver, long paramLong)
    {
      long l = tryGetMyContactsGroupId(paramContentResolver);
      if (l != 0L) {
        return addToGroup(paramContentResolver, paramLong, l);
      }
      throw new IllegalStateException("Failed to find the My Contacts group");
    }
    
    @Deprecated
    public static Uri createPersonInMyContactsGroup(ContentResolver paramContentResolver, ContentValues paramContentValues)
    {
      paramContentValues = paramContentResolver.insert(CONTENT_URI, paramContentValues);
      if (paramContentValues == null)
      {
        Log.e("Contacts", "Failed to create the contact");
        return null;
      }
      if (addToMyContactsGroup(paramContentResolver, ContentUris.parseId(paramContentValues)) == null)
      {
        paramContentResolver.delete(paramContentValues, null, null);
        return null;
      }
      return paramContentValues;
    }
    
    @Deprecated
    public static Bitmap loadContactPhoto(Context paramContext, Uri paramUri, int paramInt, BitmapFactory.Options paramOptions)
    {
      if (paramUri == null) {
        return loadPlaceholderPhoto(paramInt, paramContext, paramOptions);
      }
      Object localObject = openContactPhotoInputStream(paramContext.getContentResolver(), paramUri);
      paramUri = null;
      if (localObject != null) {
        paramUri = BitmapFactory.decodeStream((InputStream)localObject, null, paramOptions);
      }
      localObject = paramUri;
      if (paramUri == null) {
        localObject = loadPlaceholderPhoto(paramInt, paramContext, paramOptions);
      }
      return localObject;
    }
    
    private static Bitmap loadPlaceholderPhoto(int paramInt, Context paramContext, BitmapFactory.Options paramOptions)
    {
      if (paramInt == 0) {
        return null;
      }
      return BitmapFactory.decodeResource(paramContext.getResources(), paramInt, paramOptions);
    }
    
    @Deprecated
    public static void markAsContacted(ContentResolver paramContentResolver, long paramLong) {}
    
    @Deprecated
    public static InputStream openContactPhotoInputStream(ContentResolver paramContentResolver, Uri paramUri)
    {
      paramContentResolver = paramContentResolver.query(Uri.withAppendedPath(paramUri, "photo"), new String[] { "data" }, null, null, null);
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
    
    @Deprecated
    public static Cursor queryGroups(ContentResolver paramContentResolver, long paramLong)
    {
      return paramContentResolver.query(Contacts.GroupMembership.CONTENT_URI, null, "person=?", new String[] { String.valueOf(paramLong) }, "name ASC");
    }
    
    @Deprecated
    public static void setPhotoData(ContentResolver paramContentResolver, Uri paramUri, byte[] paramArrayOfByte)
    {
      paramUri = Uri.withAppendedPath(paramUri, "photo");
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("data", paramArrayOfByte);
      paramContentResolver.update(paramUri, localContentValues, null, null);
    }
    
    @Deprecated
    public static long tryGetMyContactsGroupId(ContentResolver paramContentResolver)
    {
      Cursor localCursor = paramContentResolver.query(Contacts.Groups.CONTENT_URI, GROUPS_PROJECTION, "system_id='Contacts'", null, null);
      if (localCursor != null) {
        try
        {
          if (localCursor.moveToFirst())
          {
            long l = localCursor.getLong(0);
            return l;
          }
          localCursor.close();
        }
        finally
        {
          localCursor.close();
        }
      }
      return 0L;
    }
    
    @Deprecated
    public static final class ContactMethods
      implements BaseColumns, Contacts.ContactMethodsColumns, Contacts.PeopleColumns
    {
      @Deprecated
      public static final String CONTENT_DIRECTORY = "contact_methods";
      @Deprecated
      public static final String DEFAULT_SORT_ORDER = "data ASC";
      
      private ContactMethods() {}
    }
    
    @Deprecated
    public static class Extensions
      implements BaseColumns, Contacts.ExtensionsColumns
    {
      @Deprecated
      public static final String CONTENT_DIRECTORY = "extensions";
      @Deprecated
      public static final String DEFAULT_SORT_ORDER = "name ASC";
      @Deprecated
      public static final String PERSON_ID = "person";
      
      @Deprecated
      private Extensions() {}
    }
    
    @Deprecated
    public static final class Phones
      implements BaseColumns, Contacts.PhonesColumns, Contacts.PeopleColumns
    {
      @Deprecated
      public static final String CONTENT_DIRECTORY = "phones";
      @Deprecated
      public static final String DEFAULT_SORT_ORDER = "number ASC";
      
      private Phones() {}
    }
  }
  
  @Deprecated
  public static abstract interface PeopleColumns
  {
    @Deprecated
    public static final String CUSTOM_RINGTONE = "custom_ringtone";
    @Deprecated
    public static final String DISPLAY_NAME = "display_name";
    @Deprecated
    public static final String LAST_TIME_CONTACTED = "last_time_contacted";
    @Deprecated
    public static final String NAME = "name";
    @Deprecated
    public static final String NOTES = "notes";
    @Deprecated
    public static final String PHONETIC_NAME = "phonetic_name";
    @Deprecated
    public static final String PHOTO_VERSION = "photo_version";
    @Deprecated
    public static final String SEND_TO_VOICEMAIL = "send_to_voicemail";
    @Deprecated
    public static final String SORT_STRING = "sort_string";
    @Deprecated
    public static final String STARRED = "starred";
    @Deprecated
    public static final String TIMES_CONTACTED = "times_contacted";
  }
  
  @Deprecated
  public static final class Phones
    implements BaseColumns, Contacts.PhonesColumns, Contacts.PeopleColumns
  {
    @Deprecated
    public static final Uri CONTENT_FILTER_URL = Uri.parse("content://contacts/phones/filter");
    @Deprecated
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/phone";
    @Deprecated
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/phone";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/phones");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "name ASC";
    @Deprecated
    public static final String PERSON_ID = "person";
    
    private Phones() {}
    
    @Deprecated
    public static final CharSequence getDisplayLabel(Context paramContext, int paramInt, CharSequence paramCharSequence)
    {
      return getDisplayLabel(paramContext, paramInt, paramCharSequence, null);
    }
    
    @Deprecated
    public static final CharSequence getDisplayLabel(Context paramContext, int paramInt, CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence)
    {
      String str = "";
      if (paramInt != 0)
      {
        if (paramArrayOfCharSequence != null) {
          paramContext = paramArrayOfCharSequence;
        } else {
          paramContext = paramContext.getResources().getTextArray(17235971);
        }
        paramContext = paramContext[(paramInt - 1)];
      }
      else
      {
        paramContext = str;
        if (!TextUtils.isEmpty(paramCharSequence)) {
          paramContext = paramCharSequence;
        }
      }
      return paramContext;
    }
  }
  
  @Deprecated
  public static abstract interface PhonesColumns
  {
    @Deprecated
    public static final String ISPRIMARY = "isprimary";
    @Deprecated
    public static final String LABEL = "label";
    @Deprecated
    public static final String NUMBER = "number";
    @Deprecated
    public static final String NUMBER_KEY = "number_key";
    @Deprecated
    public static final String TYPE = "type";
    @Deprecated
    public static final int TYPE_CUSTOM = 0;
    @Deprecated
    public static final int TYPE_FAX_HOME = 5;
    @Deprecated
    public static final int TYPE_FAX_WORK = 4;
    @Deprecated
    public static final int TYPE_HOME = 1;
    @Deprecated
    public static final int TYPE_MOBILE = 2;
    @Deprecated
    public static final int TYPE_OTHER = 7;
    @Deprecated
    public static final int TYPE_PAGER = 6;
    @Deprecated
    public static final int TYPE_WORK = 3;
  }
  
  @Deprecated
  public static final class Photos
    implements BaseColumns, Contacts.PhotosColumns
  {
    @Deprecated
    public static final String CONTENT_DIRECTORY = "photo";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/photos");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "person ASC";
    
    private Photos() {}
  }
  
  @Deprecated
  public static abstract interface PhotosColumns
  {
    @Deprecated
    public static final String DATA = "data";
    @Deprecated
    public static final String DOWNLOAD_REQUIRED = "download_required";
    @Deprecated
    public static final String EXISTS_ON_SERVER = "exists_on_server";
    @Deprecated
    public static final String LOCAL_VERSION = "local_version";
    @Deprecated
    public static final String PERSON_ID = "person";
    @Deprecated
    public static final String SYNC_ERROR = "sync_error";
  }
  
  @Deprecated
  public static final class Presence
    implements BaseColumns, Contacts.PresenceColumns, Contacts.PeopleColumns
  {
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/presence");
    @Deprecated
    public static final String PERSON_ID = "person";
    
    public Presence() {}
    
    @Deprecated
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
    
    @Deprecated
    public static final void setPresenceIcon(ImageView paramImageView, int paramInt)
    {
      paramImageView.setImageResource(getPresenceIconResourceId(paramInt));
    }
  }
  
  @Deprecated
  public static abstract interface PresenceColumns
  {
    public static final int AVAILABLE = 5;
    public static final int AWAY = 2;
    public static final int DO_NOT_DISTURB = 4;
    public static final int IDLE = 3;
    @Deprecated
    public static final String IM_ACCOUNT = "im_account";
    @Deprecated
    public static final String IM_HANDLE = "im_handle";
    @Deprecated
    public static final String IM_PROTOCOL = "im_protocol";
    public static final int INVISIBLE = 1;
    public static final int OFFLINE = 0;
    public static final String PRESENCE_CUSTOM_STATUS = "status";
    public static final String PRESENCE_STATUS = "mode";
    public static final String PRIORITY = "priority";
  }
  
  @Deprecated
  public static final class Settings
    implements BaseColumns, Contacts.SettingsColumns
  {
    @Deprecated
    public static final String CONTENT_DIRECTORY = "settings";
    @Deprecated
    public static final Uri CONTENT_URI = Uri.parse("content://contacts/settings");
    @Deprecated
    public static final String DEFAULT_SORT_ORDER = "key ASC";
    @Deprecated
    public static final String SYNC_EVERYTHING = "syncEverything";
    
    private Settings() {}
    
    @Deprecated
    public static String getSetting(ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      paramContentResolver = paramContentResolver.query(CONTENT_URI, new String[] { "value" }, "key=?", new String[] { paramString2 }, null);
      try
      {
        boolean bool = paramContentResolver.moveToNext();
        if (!bool) {
          return null;
        }
        paramString1 = paramContentResolver.getString(0);
        return paramString1;
      }
      finally
      {
        paramContentResolver.close();
      }
    }
    
    @Deprecated
    public static void setSetting(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3)
    {
      paramString1 = new ContentValues();
      paramString1.put("key", paramString2);
      paramString1.put("value", paramString3);
      paramContentResolver.update(CONTENT_URI, paramString1, null, null);
    }
  }
  
  @Deprecated
  public static abstract interface SettingsColumns
  {
    @Deprecated
    public static final String KEY = "key";
    @Deprecated
    public static final String VALUE = "value";
    @Deprecated
    public static final String _SYNC_ACCOUNT = "_sync_account";
    @Deprecated
    public static final String _SYNC_ACCOUNT_TYPE = "_sync_account_type";
  }
}
