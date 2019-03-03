package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Country;
import android.location.CountryDetector;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SeempLog;
import com.android.i18n.phonenumbers.NumberParseException;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import java.util.Locale;

public class CallerInfo
{
  private static final String TAG = "CallerInfo";
  public static final long USER_TYPE_CURRENT = 0L;
  public static final long USER_TYPE_WORK = 1L;
  private static final boolean VDBG = Rlog.isLoggable("CallerInfo", 2);
  public Drawable cachedPhoto;
  public Bitmap cachedPhotoIcon;
  public String cnapName;
  public Uri contactDisplayPhotoUri;
  public boolean contactExists;
  public long contactIdOrZero;
  public Uri contactRefUri;
  public Uri contactRingtoneUri;
  public String geoDescription;
  public boolean isCachedPhotoCurrent;
  public String lookupKey;
  private boolean mIsEmergency = false;
  private boolean mIsVoiceMail = false;
  public String name;
  public int namePresentation;
  public boolean needUpdate;
  public String normalizedNumber;
  public String numberLabel;
  public int numberPresentation;
  public int numberType;
  public String phoneLabel;
  public String phoneNumber;
  public int photoResource;
  public boolean shouldSendToVoicemail;
  public long userType = 0L;
  
  public CallerInfo() {}
  
  static CallerInfo doSecondaryLookupIfNecessary(Context paramContext, String paramString, CallerInfo paramCallerInfo)
  {
    CallerInfo localCallerInfo = paramCallerInfo;
    if (!contactExists)
    {
      localCallerInfo = paramCallerInfo;
      if (PhoneNumberUtils.isUriNumber(paramString))
      {
        paramString = PhoneNumberUtils.getUsernameFromUriNumber(paramString);
        localCallerInfo = paramCallerInfo;
        if (PhoneNumberUtils.isGlobalPhoneNumber(paramString)) {
          localCallerInfo = getCallerInfo(paramContext, Uri.withAppendedPath(ContactsContract.PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI, Uri.encode(paramString)));
        }
      }
    }
    return localCallerInfo;
  }
  
  public static CallerInfo getCallerInfo(Context paramContext, Uri paramUri)
  {
    Object localObject1 = null;
    ContentResolver localContentResolver = CallerInfoAsyncQuery.getCurrentProfileContentResolver(paramContext);
    Object localObject2 = localObject1;
    if (localContentResolver != null) {
      try
      {
        localObject2 = getCallerInfo(paramContext, paramUri, localContentResolver.query(paramUri, null, null, null, null));
      }
      catch (RuntimeException paramContext)
      {
        Rlog.e("CallerInfo", "Error getting caller info.", paramContext);
        localObject2 = localObject1;
      }
    }
    return localObject2;
  }
  
  public static CallerInfo getCallerInfo(Context paramContext, Uri paramUri, Cursor paramCursor)
  {
    SeempLog.record_uri(12, paramUri);
    CallerInfo localCallerInfo = new CallerInfo();
    photoResource = 0;
    phoneLabel = null;
    numberType = 0;
    numberLabel = null;
    cachedPhoto = null;
    isCachedPhotoCurrent = false;
    contactExists = false;
    userType = 0L;
    if (VDBG) {
      Rlog.v("CallerInfo", "getCallerInfo() based on cursor...");
    }
    if (paramCursor != null)
    {
      Cursor localCursor = paramCursor;
      if (paramCursor.moveToFirst())
      {
        localCursor = getDevicesContactFirst(paramCursor, -1, null);
        int i = localCursor.getColumnIndex("display_name");
        if (i != -1) {
          name = localCursor.getString(i);
        }
        i = localCursor.getColumnIndex("number");
        if (i != -1) {
          phoneNumber = localCursor.getString(i);
        }
        i = localCursor.getColumnIndex("normalized_number");
        if (i != -1) {
          normalizedNumber = localCursor.getString(i);
        }
        int j = localCursor.getColumnIndex("label");
        if (j != -1)
        {
          i = localCursor.getColumnIndex("type");
          if (i != -1)
          {
            numberType = localCursor.getInt(i);
            numberLabel = localCursor.getString(j);
            phoneLabel = ContactsContract.CommonDataKinds.Phone.getDisplayLabel(paramContext, numberType, numberLabel).toString();
          }
        }
        i = getColumnIndexForPersonId(paramUri, localCursor);
        if (i != -1)
        {
          long l = localCursor.getLong(i);
          if ((l != 0L) && (!ContactsContract.Contacts.isEnterpriseContactId(l)))
          {
            contactIdOrZero = l;
            if (VDBG)
            {
              paramContext = new StringBuilder();
              paramContext.append("==> got info.contactIdOrZero: ");
              paramContext.append(contactIdOrZero);
              Rlog.v("CallerInfo", paramContext.toString());
            }
          }
          if (ContactsContract.Contacts.isEnterpriseContactId(l)) {
            userType = 1L;
          }
        }
        else
        {
          paramContext = new StringBuilder();
          paramContext.append("Couldn't find contact_id column for ");
          paramContext.append(paramUri);
          Rlog.w("CallerInfo", paramContext.toString());
        }
        i = localCursor.getColumnIndex("lookup");
        if (i != -1) {
          lookupKey = localCursor.getString(i);
        }
        i = localCursor.getColumnIndex("photo_uri");
        if ((i != -1) && (localCursor.getString(i) != null)) {
          contactDisplayPhotoUri = Uri.parse(localCursor.getString(i));
        } else {
          contactDisplayPhotoUri = null;
        }
        i = localCursor.getColumnIndex("custom_ringtone");
        if ((i != -1) && (localCursor.getString(i) != null))
        {
          if (TextUtils.isEmpty(localCursor.getString(i))) {
            contactRingtoneUri = Uri.EMPTY;
          } else {
            contactRingtoneUri = Uri.parse(localCursor.getString(i));
          }
        }
        else {
          contactRingtoneUri = null;
        }
        i = localCursor.getColumnIndex("send_to_voicemail");
        boolean bool;
        if ((i != -1) && (localCursor.getInt(i) == 1)) {
          bool = true;
        } else {
          bool = false;
        }
        shouldSendToVoicemail = bool;
        contactExists = true;
      }
      localCursor.close();
    }
    needUpdate = false;
    name = normalize(name);
    contactRefUri = paramUri;
    return localCallerInfo;
  }
  
  public static CallerInfo getCallerInfo(Context paramContext, Uri paramUri, Cursor paramCursor, int paramInt, String paramString)
  {
    CallerInfo localCallerInfo = new CallerInfo();
    photoResource = 0;
    phoneLabel = null;
    numberType = 0;
    numberLabel = null;
    cachedPhoto = null;
    isCachedPhotoCurrent = false;
    contactExists = false;
    userType = 0L;
    if (VDBG) {
      Rlog.v("CallerInfo", "getCallerInfo() based on cursor...");
    }
    if (paramCursor != null)
    {
      Cursor localCursor = paramCursor;
      if (paramCursor.moveToFirst())
      {
        localCursor = getDevicesContactFirst(paramCursor, paramInt, paramString);
        paramInt = localCursor.getColumnIndex("display_name");
        if (paramInt != -1) {
          name = localCursor.getString(paramInt);
        }
        paramInt = localCursor.getColumnIndex("number");
        if (paramInt != -1) {
          phoneNumber = localCursor.getString(paramInt);
        }
        paramInt = localCursor.getColumnIndex("normalized_number");
        if (paramInt != -1) {
          normalizedNumber = localCursor.getString(paramInt);
        }
        paramInt = localCursor.getColumnIndex("label");
        if (paramInt != -1)
        {
          int i = localCursor.getColumnIndex("type");
          if (i != -1)
          {
            numberType = localCursor.getInt(i);
            numberLabel = localCursor.getString(paramInt);
            phoneLabel = ContactsContract.CommonDataKinds.Phone.getDisplayLabel(paramContext, numberType, numberLabel).toString();
          }
        }
        paramInt = getColumnIndexForPersonId(paramUri, localCursor);
        if (paramInt != -1)
        {
          long l = localCursor.getLong(paramInt);
          if ((l != 0L) && (!ContactsContract.Contacts.isEnterpriseContactId(l)))
          {
            contactIdOrZero = l;
            if (VDBG)
            {
              paramContext = new StringBuilder();
              paramContext.append("==> got info.contactIdOrZero: ");
              paramContext.append(contactIdOrZero);
              Rlog.v("CallerInfo", paramContext.toString());
            }
          }
          if (ContactsContract.Contacts.isEnterpriseContactId(l)) {
            userType = 1L;
          }
        }
        else
        {
          paramContext = new StringBuilder();
          paramContext.append("Couldn't find contact_id column for ");
          paramContext.append(paramUri);
          Rlog.w("CallerInfo", paramContext.toString());
        }
        paramInt = localCursor.getColumnIndex("lookup");
        if (paramInt != -1) {
          lookupKey = localCursor.getString(paramInt);
        }
        paramInt = localCursor.getColumnIndex("photo_uri");
        if ((paramInt != -1) && (localCursor.getString(paramInt) != null)) {
          contactDisplayPhotoUri = Uri.parse(localCursor.getString(paramInt));
        } else {
          contactDisplayPhotoUri = null;
        }
        paramInt = localCursor.getColumnIndex("custom_ringtone");
        if ((paramInt != -1) && (localCursor.getString(paramInt) != null))
        {
          if (TextUtils.isEmpty(localCursor.getString(paramInt))) {
            contactRingtoneUri = Uri.EMPTY;
          } else {
            contactRingtoneUri = Uri.parse(localCursor.getString(paramInt));
          }
        }
        else {
          contactRingtoneUri = null;
        }
        paramInt = localCursor.getColumnIndex("send_to_voicemail");
        boolean bool;
        if ((paramInt != -1) && (localCursor.getInt(paramInt) == 1)) {
          bool = true;
        } else {
          bool = false;
        }
        shouldSendToVoicemail = bool;
        contactExists = true;
      }
      localCursor.close();
    }
    needUpdate = false;
    name = normalize(name);
    contactRefUri = paramUri;
    return localCallerInfo;
  }
  
  public static CallerInfo getCallerInfo(Context paramContext, String paramString)
  {
    if (VDBG) {
      Rlog.v("CallerInfo", "getCallerInfo() based on number...");
    }
    return getCallerInfo(paramContext, paramString, SubscriptionManager.getDefaultSubscriptionId());
  }
  
  public static CallerInfo getCallerInfo(Context paramContext, String paramString, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("number=");
    localStringBuilder.append(paramString);
    localStringBuilder.append(",subId=");
    localStringBuilder.append(paramInt);
    SeempLog.record_str(12, localStringBuilder.toString());
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    if (PhoneNumberUtils.isLocalEmergencyNumber(paramContext, paramString)) {
      return new CallerInfo().markAsEmergency(paramContext);
    }
    if (PhoneNumberUtils.isVoiceMailNumber(paramInt, paramString)) {
      return new CallerInfo().markAsVoiceMail();
    }
    paramContext = doSecondaryLookupIfNecessary(paramContext, paramString, getCallerInfo(paramContext, Uri.withAppendedPath(ContactsContract.PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI, Uri.encode(paramString))));
    if (TextUtils.isEmpty(phoneNumber)) {
      phoneNumber = paramString;
    }
    return paramContext;
  }
  
  private static int getColumnIndexForPersonId(Uri paramUri, Cursor paramCursor)
  {
    StringBuilder localStringBuilder;
    if (VDBG)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("- getColumnIndexForPersonId: contactRef URI = '");
      localStringBuilder.append(paramUri);
      localStringBuilder.append("'...");
      Rlog.v("CallerInfo", localStringBuilder.toString());
    }
    String str = paramUri.toString();
    paramUri = null;
    if (str.startsWith("content://com.android.contacts/data/phones"))
    {
      if (VDBG) {
        Rlog.v("CallerInfo", "'data/phones' URI; using RawContacts.CONTACT_ID");
      }
      paramUri = "contact_id";
    }
    else if (str.startsWith("content://com.android.contacts/data"))
    {
      if (VDBG) {
        Rlog.v("CallerInfo", "'data' URI; using Data.CONTACT_ID");
      }
      paramUri = "contact_id";
    }
    else if (str.startsWith("content://com.android.contacts/phone_lookup"))
    {
      if (VDBG) {
        Rlog.v("CallerInfo", "'phone_lookup' URI; using PhoneLookup._ID");
      }
      paramUri = "_id";
    }
    else if (str.startsWith("content://com.android.contacts/contacts/"))
    {
      Log.v("CallerInfo", "'contacts' URI; using Contacts._ID");
      paramUri = "_id";
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected prefix for contactRef '");
      localStringBuilder.append(str);
      localStringBuilder.append("'");
      Rlog.w("CallerInfo", localStringBuilder.toString());
    }
    int i;
    if (paramUri != null) {
      i = paramCursor.getColumnIndex(paramUri);
    } else {
      i = -1;
    }
    if (VDBG)
    {
      paramCursor = new StringBuilder();
      paramCursor.append("==> Using column '");
      paramCursor.append(paramUri);
      paramCursor.append("' (columnIndex = ");
      paramCursor.append(i);
      paramCursor.append(") for person_id lookup...");
      Rlog.v("CallerInfo", paramCursor.toString());
    }
    return i;
  }
  
  protected static String getCurrentCountryIso(Context paramContext)
  {
    return getCurrentCountryIso(paramContext, Locale.getDefault());
  }
  
  private static String getCurrentCountryIso(Context paramContext, Locale paramLocale)
  {
    Object localObject = null;
    CountryDetector localCountryDetector = (CountryDetector)paramContext.getSystemService("country_detector");
    paramContext = (Context)localObject;
    if (localCountryDetector != null)
    {
      paramContext = localCountryDetector.detectCountry();
      if (paramContext != null)
      {
        paramContext = paramContext.getCountryIso();
      }
      else
      {
        Rlog.e("CallerInfo", "CountryDetector.detectCountry() returned null.");
        paramContext = (Context)localObject;
      }
    }
    localObject = paramContext;
    if (paramContext == null)
    {
      localObject = paramLocale.getCountry();
      paramContext = new StringBuilder();
      paramContext.append("No CountryDetector; falling back to countryIso based on locale: ");
      paramContext.append((String)localObject);
      Rlog.w("CallerInfo", paramContext.toString());
    }
    return localObject;
  }
  
  public static Cursor getDevicesContactFirst(Cursor paramCursor, int paramInt, String paramString)
  {
    try
    {
      int i = paramCursor.getColumnIndex("isSim");
      if (paramInt == -1) {
        paramInt = SubscriptionManager.getDefaultSubscriptionId();
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getDevicesContactFirst(): preferContactId = ");
      localStringBuilder.append(paramString);
      Log.d("CallerInfo", localStringBuilder.toString());
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getDevicesContactFirst(): subId = ");
      localStringBuilder.append(paramInt);
      Log.d("CallerInfo", localStringBuilder.toString());
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("getDevicesContactFirst(): isSimIndex = ");
      localStringBuilder.append(i);
      Log.d("CallerInfo", localStringBuilder.toString());
      int j = paramCursor.getColumnIndex("contact_id");
      if (paramString != null)
      {
        paramCursor.moveToFirst();
        do
        {
          int k = paramCursor.getInt(j);
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("getDevicesContactFirst(): contactId = ");
          localStringBuilder.append(k);
          Log.d("CallerInfo", localStringBuilder.toString());
          if (k == Integer.valueOf(paramString).intValue())
          {
            paramString = new java/lang/StringBuilder;
            paramString.<init>();
            paramString.append("getDevicesContactFirst(): contactId is ");
            paramString.append(k);
            Log.d("CallerInfo", paramString.toString());
            return paramCursor;
          }
        } while (paramCursor.moveToNext());
      }
      if ((i > -1) && (paramCursor.getCount() > 1))
      {
        Log.d("CallerInfo", "getDevicesContactFirst(): has isSIm field and exists multip contacts");
        paramCursor.moveToFirst();
        do
        {
          j = paramCursor.getInt(i);
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("getDevicesContactFirst(): isSim = ");
          paramString.append(j);
          Log.d("CallerInfo", paramString.toString());
          if (j == 0)
          {
            Log.d("CallerInfo", "getDevicesContactFirst(): isSim = 0");
            return paramCursor;
          }
        } while (paramCursor.moveToNext());
        Log.d("CallerInfo", "getDevicesContactFirst(): All contacts is in sim card");
        try
        {
          j = SubscriptionManager.getSlotIndex(paramInt);
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("getDevicesContactFirst(): slotId = ");
          paramString.append(j);
          Log.d("CallerInfo", paramString.toString());
          paramCursor.moveToFirst();
          boolean bool;
          do
          {
            paramInt = paramCursor.getInt(i);
            paramString = new java/lang/StringBuilder;
            paramString.<init>();
            paramString.append("getDevicesContactFirst(): isSim = ");
            paramString.append(paramInt);
            Log.d("CallerInfo", paramString.toString());
            if (paramInt == j + 1)
            {
              paramString = new java/lang/StringBuilder;
              paramString.<init>();
              paramString.append("getDevicesContactFirst(): return cursor,  isSim = ");
              paramString.append(paramInt);
              paramString.append(", slotId = ");
              paramString.append(j);
              Log.d("CallerInfo", paramString.toString());
              return paramCursor;
            }
            bool = paramCursor.moveToNext();
          } while (bool);
        }
        catch (Exception paramString)
        {
          paramString.printStackTrace();
        }
      }
      Log.d("CallerInfo", "getDevicesContactFirst(): return cursor.moveToFirst()");
      paramCursor.moveToFirst();
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
    return paramCursor;
  }
  
  public static String getGeoDescription(Context paramContext, String paramString)
  {
    if (VDBG)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getGeoDescription('");
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("')...");
      Rlog.v("CallerInfo", ((StringBuilder)localObject1).toString());
    }
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    Object localObject2 = PhoneNumberUtil.getInstance();
    PhoneNumberOfflineGeocoder localPhoneNumberOfflineGeocoder = PhoneNumberOfflineGeocoder.getInstance();
    Locale localLocale = getResourcesgetConfigurationlocale;
    String str = getCurrentCountryIso(paramContext, localLocale);
    Object localObject1 = null;
    paramContext = (Context)localObject1;
    try
    {
      if (VDBG)
      {
        paramContext = (Context)localObject1;
        StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
        paramContext = (Context)localObject1;
        localStringBuilder2.<init>();
        paramContext = (Context)localObject1;
        localStringBuilder2.append("parsing '");
        paramContext = (Context)localObject1;
        localStringBuilder2.append(paramString);
        paramContext = (Context)localObject1;
        localStringBuilder2.append("' for countryIso '");
        paramContext = (Context)localObject1;
        localStringBuilder2.append(str);
        paramContext = (Context)localObject1;
        localStringBuilder2.append("'...");
        paramContext = (Context)localObject1;
        Rlog.v("CallerInfo", localStringBuilder2.toString());
      }
      paramContext = (Context)localObject1;
      localObject1 = ((PhoneNumberUtil)localObject2).parse(paramString, str);
      paramContext = (Context)localObject1;
      if (VDBG)
      {
        paramContext = (Context)localObject1;
        localObject2 = new java/lang/StringBuilder;
        paramContext = (Context)localObject1;
        ((StringBuilder)localObject2).<init>();
        paramContext = (Context)localObject1;
        ((StringBuilder)localObject2).append("- parsed number: ");
        paramContext = (Context)localObject1;
        ((StringBuilder)localObject2).append(localObject1);
        paramContext = (Context)localObject1;
        Rlog.v("CallerInfo", ((StringBuilder)localObject2).toString());
      }
      paramContext = (Context)localObject1;
    }
    catch (NumberParseException localNumberParseException)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("getGeoDescription: NumberParseException for incoming number '");
      localStringBuilder1.append(Rlog.pii("CallerInfo", paramString));
      localStringBuilder1.append("'");
      Rlog.w("CallerInfo", localStringBuilder1.toString());
    }
    if (paramContext != null)
    {
      paramContext = localPhoneNumberOfflineGeocoder.getDescriptionForNumber(paramContext, localLocale);
      if (VDBG)
      {
        paramString = new StringBuilder();
        paramString.append("- got description: '");
        paramString.append(paramContext);
        paramString.append("'");
        Rlog.v("CallerInfo", paramString.toString());
      }
      return paramContext;
    }
    return null;
  }
  
  private static String normalize(String paramString)
  {
    if ((paramString != null) && (paramString.length() <= 0)) {
      return null;
    }
    return paramString;
  }
  
  public boolean isEmergencyNumber()
  {
    return mIsEmergency;
  }
  
  public boolean isVoiceMailNumber()
  {
    return mIsVoiceMail;
  }
  
  CallerInfo markAsEmergency(Context paramContext)
  {
    phoneNumber = paramContext.getString(17039917);
    photoResource = 17303325;
    mIsEmergency = true;
    return this;
  }
  
  CallerInfo markAsVoiceMail()
  {
    return markAsVoiceMail(SubscriptionManager.getDefaultSubscriptionId());
  }
  
  CallerInfo markAsVoiceMail(int paramInt)
  {
    mIsVoiceMail = true;
    try
    {
      phoneNumber = TelephonyManager.getDefault().getVoiceMailAlphaTag(paramInt);
    }
    catch (SecurityException localSecurityException)
    {
      Rlog.e("CallerInfo", "Cannot access VoiceMail.", localSecurityException);
    }
    return this;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(128);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(super.toString());
    ((StringBuilder)localObject).append(" { ");
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("name ");
    if (name == null) {
      localObject = "null";
    } else {
      localObject = "non-null";
    }
    localStringBuilder2.append((String)localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", phoneNumber ");
    if (phoneNumber == null) {
      localObject = "null";
    } else {
      localObject = "non-null";
    }
    localStringBuilder2.append((String)localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }
  
  public void updateGeoDescription(Context paramContext, String paramString)
  {
    if (!TextUtils.isEmpty(phoneNumber)) {
      paramString = phoneNumber;
    }
    geoDescription = getGeoDescription(paramContext, paramString);
  }
}
