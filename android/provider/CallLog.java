package android.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.location.Country;
import android.location.CountryDetector;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.UserHandle;
import android.os.UserManager;
import android.telecom.PhoneAccountHandle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CallerInfo;

public class CallLog
{
  public static final String AUTHORITY = "call_log";
  public static final Uri CONTENT_URI = Uri.parse("content://call_log");
  private static final String LOG_TAG = "CallLog";
  public static final String SHADOW_AUTHORITY = "call_log_shadow";
  private static final boolean VERBOSE_LOG = false;
  
  public CallLog() {}
  
  public static class Calls
    implements BaseColumns
  {
    public static final String ADD_FOR_ALL_USERS = "add_for_all_users";
    public static final String ALLOW_VOICEMAILS_PARAM_KEY = "allow_voicemails";
    public static final int ANSWERED_EXTERNALLY_TYPE = 7;
    public static final int BLOCKED_TYPE = 6;
    public static final String CACHED_FORMATTED_NUMBER = "formatted_number";
    public static final String CACHED_LOOKUP_URI = "lookup_uri";
    public static final String CACHED_MATCHED_NUMBER = "matched_number";
    public static final String CACHED_NAME = "name";
    public static final String CACHED_NORMALIZED_NUMBER = "normalized_number";
    public static final String CACHED_NUMBER_LABEL = "numberlabel";
    public static final String CACHED_NUMBER_TYPE = "numbertype";
    public static final String CACHED_PHOTO_ID = "photo_id";
    public static final String CACHED_PHOTO_URI = "photo_uri";
    public static final String CITY_ID = "city_id";
    public static final String CONTACTID = "block";
    public static final Uri CONTENT_FILTER_URI = Uri.parse("content://call_log/calls/filter");
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/calls";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/calls";
    public static final Uri CONTENT_URI = Uri.parse("content://call_log/calls");
    public static final Uri CONTENT_URI_WITH_VOICEMAIL = CONTENT_URI.buildUpon().appendQueryParameter("allow_voicemails", "true").build();
    public static final String COUNTRY_ISO = "countryiso";
    public static final String DATA_USAGE = "data_usage";
    public static final String DATE = "date";
    public static final String DEFAULT_SORT_ORDER = "date DESC";
    public static final String DURATION = "duration";
    public static final String EXTRA_CALL_TYPE_FILTER = "android.provider.extra.CALL_TYPE_FILTER";
    public static final String FEATURES = "features";
    public static final int FEATURES_ASSISTED_DIALING_USED = 16;
    public static final int FEATURES_HD_CALL = 4;
    public static final int FEATURES_PULLED_EXTERNALLY = 2;
    public static final int FEATURES_RTT = 32;
    public static final int FEATURES_VIDEO = 1;
    public static final int FEATURES_WIFI = 8;
    public static final String GEOCODED_LOCATION = "geocoded_location";
    public static final int INCOMING_TYPE = 1;
    public static final String ISBLOCK = "contact_id";
    public static final String IS_READ = "is_read";
    public static final String LAST_MODIFIED = "last_modified";
    public static final String LIMIT_PARAM_KEY = "limit";
    private static final int MIN_DURATION_FOR_NORMALIZED_NUMBER_UPDATE_MS = 10000;
    public static final int MISSED_TYPE = 3;
    public static final String NEW = "new";
    public static final String NUMBER = "number";
    public static final String NUMBER_PRESENTATION = "presentation";
    public static final String OFFSET_PARAM_KEY = "offset";
    public static final int OUTGOING_TYPE = 2;
    public static final String PHONE_ACCOUNT_ADDRESS = "phone_account_address";
    public static final String PHONE_ACCOUNT_COMPONENT_NAME = "subscription_component_name";
    public static final String PHONE_ACCOUNT_HIDDEN = "phone_account_hidden";
    public static final String PHONE_ACCOUNT_ID = "subscription_id";
    public static final String POST_DIAL_DIGITS = "post_dial_digits";
    public static final int PRESENTATION_ALLOWED = 1;
    public static final int PRESENTATION_PAYPHONE = 4;
    public static final int PRESENTATION_RESTRICTED = 2;
    public static final int PRESENTATION_UNKNOWN = 3;
    public static final int REJECTED_TYPE = 5;
    public static final Uri SHADOW_CONTENT_URI = Uri.parse("content://call_log_shadow/calls");
    public static final String SIM_INDEX = "sim_index";
    public static final String SUB_ID = "sub_id";
    public static final String TRANSCRIPTION = "transcription";
    public static final String TRANSCRIPTION_STATE = "transcription_state";
    public static final String TYPE = "type";
    public static final String VIA_NUMBER = "via_number";
    public static final int VOICEMAIL_TYPE = 4;
    public static final String VOICEMAIL_URI = "voicemail_uri";
    
    public Calls() {}
    
    public static Uri addCall(CallerInfo paramCallerInfo, Context paramContext, String paramString, int paramInt1, int paramInt2, int paramInt3, PhoneAccountHandle paramPhoneAccountHandle, long paramLong, int paramInt4, Long paramLong1)
    {
      return addCall(paramCallerInfo, paramContext, paramString, "", "", paramInt1, paramInt2, paramInt3, paramPhoneAccountHandle, paramLong, paramInt4, paramLong1, false, null, false);
    }
    
    public static Uri addCall(CallerInfo paramCallerInfo, Context paramContext, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3, PhoneAccountHandle paramPhoneAccountHandle, long paramLong, int paramInt4, Long paramLong1, boolean paramBoolean, UserHandle paramUserHandle)
    {
      return addCall(paramCallerInfo, paramContext, paramString1, paramString2, paramString3, paramInt1, paramInt2, paramInt3, paramPhoneAccountHandle, paramLong, paramInt4, paramLong1, paramBoolean, paramUserHandle, false);
    }
    
    public static Uri addCall(CallerInfo paramCallerInfo, Context paramContext, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3, PhoneAccountHandle paramPhoneAccountHandle, long paramLong, int paramInt4, Long paramLong1, boolean paramBoolean1, UserHandle paramUserHandle, boolean paramBoolean2)
    {
      return addCall(paramCallerInfo, paramContext, paramString1, paramString2, paramString3, paramInt1, paramInt2, paramInt3, paramPhoneAccountHandle, paramLong, paramInt4, paramLong1, paramBoolean1, paramUserHandle, paramBoolean2, 0, 0, null);
    }
    
    /* Error */
    public static Uri addCall(CallerInfo paramCallerInfo, Context paramContext, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3, PhoneAccountHandle paramPhoneAccountHandle, long paramLong, int paramInt4, Long paramLong1, boolean paramBoolean1, UserHandle paramUserHandle, boolean paramBoolean2, int paramInt5, int paramInt6, String paramString4)
    {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual 235	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   4: astore 19
      //   6: iconst_1
      //   7: istore 20
      //   9: aconst_null
      //   10: astore 21
      //   12: aload_1
      //   13: invokestatic 241	android/telecom/TelecomManager:from	(Landroid/content/Context;)Landroid/telecom/TelecomManager;
      //   16: astore 22
      //   18: aload 22
      //   20: astore 21
      //   22: goto +8 -> 30
      //   25: astore 22
      //   27: goto -5 -> 22
      //   30: aconst_null
      //   31: astore 23
      //   33: aload 23
      //   35: astore 22
      //   37: aload 21
      //   39: ifnull +53 -> 92
      //   42: aload 23
      //   44: astore 22
      //   46: aload 8
      //   48: ifnull +44 -> 92
      //   51: aload 21
      //   53: aload 8
      //   55: invokevirtual 245	android/telecom/TelecomManager:getPhoneAccount	(Landroid/telecom/PhoneAccountHandle;)Landroid/telecom/PhoneAccount;
      //   58: astore 21
      //   60: aload 23
      //   62: astore 22
      //   64: aload 21
      //   66: ifnull +26 -> 92
      //   69: aload 21
      //   71: invokevirtual 250	android/telecom/PhoneAccount:getSubscriptionAddress	()Landroid/net/Uri;
      //   74: astore 21
      //   76: aload 23
      //   78: astore 22
      //   80: aload 21
      //   82: ifnull +10 -> 92
      //   85: aload 21
      //   87: invokevirtual 254	android/net/Uri:getSchemeSpecificPart	()Ljava/lang/String;
      //   90: astore 22
      //   92: iload 5
      //   94: iconst_2
      //   95: if_icmpne +9 -> 104
      //   98: iconst_2
      //   99: istore 20
      //   101: goto +34 -> 135
      //   104: iload 5
      //   106: iconst_4
      //   107: if_icmpne +9 -> 116
      //   110: iconst_4
      //   111: istore 20
      //   113: goto -12 -> 101
      //   116: aload_2
      //   117: invokestatic 260	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   120: ifne +9 -> 129
      //   123: iload 5
      //   125: iconst_3
      //   126: if_icmpne -25 -> 101
      //   129: iconst_3
      //   130: istore 20
      //   132: goto -31 -> 101
      //   135: iload 20
      //   137: iconst_1
      //   138: if_icmpeq +19 -> 157
      //   141: aload_0
      //   142: ifnull +9 -> 151
      //   145: aload_0
      //   146: ldc -36
      //   148: putfield 264	com/android/internal/telephony/CallerInfo:name	Ljava/lang/String;
      //   151: ldc -36
      //   153: astore_2
      //   154: goto +3 -> 157
      //   157: aconst_null
      //   158: astore 23
      //   160: aconst_null
      //   161: astore 21
      //   163: aload 8
      //   165: ifnull +20 -> 185
      //   168: aload 8
      //   170: invokevirtual 270	android/telecom/PhoneAccountHandle:getComponentName	()Landroid/content/ComponentName;
      //   173: invokevirtual 275	android/content/ComponentName:flattenToString	()Ljava/lang/String;
      //   176: astore 23
      //   178: aload 8
      //   180: invokevirtual 278	android/telecom/PhoneAccountHandle:getId	()Ljava/lang/String;
      //   183: astore 21
      //   185: new 280	android/content/ContentValues
      //   188: dup
      //   189: bipush 6
      //   191: invokespecial 283	android/content/ContentValues:<init>	(I)V
      //   194: astore 8
      //   196: aload 8
      //   198: ldc 123
      //   200: aload_2
      //   201: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   204: aload 8
      //   206: ldc -111
      //   208: aload_3
      //   209: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   212: aload 8
      //   214: ldc -86
      //   216: aload 4
      //   218: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   221: aload 8
      //   223: ldc 126
      //   225: iload 20
      //   227: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   230: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   233: aload 8
      //   235: ldc -89
      //   237: iload 6
      //   239: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   242: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   245: aload 8
      //   247: ldc 104
      //   249: iload 16
      //   251: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   254: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   257: aload 8
      //   259: ldc -101
      //   261: iload 17
      //   263: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   266: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   269: aload 8
      //   271: ldc 51
      //   273: aload 18
      //   275: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   278: aload 8
      //   280: ldc 85
      //   282: iload 7
      //   284: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   287: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   290: aload 8
      //   292: ldc 73
      //   294: lload 9
      //   296: invokestatic 301	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   299: invokevirtual 304	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
      //   302: aload 8
      //   304: ldc 79
      //   306: iload 11
      //   308: i2l
      //   309: invokestatic 301	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   312: invokevirtual 304	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
      //   315: aload 12
      //   317: ifnull +12 -> 329
      //   320: aload 8
      //   322: ldc 70
      //   324: aload 12
      //   326: invokevirtual 304	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
      //   329: aload 8
      //   331: ldc -120
      //   333: aload 23
      //   335: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   338: aload 8
      //   340: ldc -114
      //   342: aload 21
      //   344: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   347: aload 8
      //   349: ldc -123
      //   351: aload 22
      //   353: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   356: aload 8
      //   358: ldc 120
      //   360: iconst_1
      //   361: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   364: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   367: aload 8
      //   369: ldc 13
      //   371: iload 13
      //   373: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   376: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   379: iload 6
      //   381: iconst_3
      //   382: if_icmpne +15 -> 397
      //   385: aload 8
      //   387: ldc 107
      //   389: iload 15
      //   391: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   394: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   397: aload_0
      //   398: ifnull +39 -> 437
      //   401: aload 8
      //   403: ldc 33
      //   405: aload_0
      //   406: getfield 264	com/android/internal/telephony/CallerInfo:name	Ljava/lang/String;
      //   409: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   412: aload 8
      //   414: ldc 42
      //   416: aload_0
      //   417: getfield 307	com/android/internal/telephony/CallerInfo:numberType	I
      //   420: invokestatic 293	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   423: invokevirtual 296	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   426: aload 8
      //   428: ldc 39
      //   430: aload_0
      //   431: getfield 310	com/android/internal/telephony/CallerInfo:numberLabel	Ljava/lang/String;
      //   434: invokevirtual 287	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   437: aload_0
      //   438: ifnull +263 -> 701
      //   441: aload_0
      //   442: getfield 314	com/android/internal/telephony/CallerInfo:contactIdOrZero	J
      //   445: lconst_0
      //   446: lcmp
      //   447: ifle +254 -> 701
      //   450: aload 8
      //   452: ldc 54
      //   454: aload_0
      //   455: getfield 314	com/android/internal/telephony/CallerInfo:contactIdOrZero	J
      //   458: invokestatic 301	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   461: invokevirtual 304	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
      //   464: aload_0
      //   465: getfield 317	com/android/internal/telephony/CallerInfo:normalizedNumber	Ljava/lang/String;
      //   468: ifnull +60 -> 528
      //   471: aload_0
      //   472: getfield 317	com/android/internal/telephony/CallerInfo:normalizedNumber	Ljava/lang/String;
      //   475: astore 4
      //   477: getstatic 320	android/provider/ContactsContract$CommonDataKinds$Phone:CONTENT_URI	Landroid/net/Uri;
      //   480: astore_3
      //   481: aload_0
      //   482: getfield 314	com/android/internal/telephony/CallerInfo:contactIdOrZero	J
      //   485: lstore 9
      //   487: aload 19
      //   489: aload_3
      //   490: iconst_1
      //   491: anewarray 322	java/lang/String
      //   494: dup
      //   495: iconst_0
      //   496: ldc_w 324
      //   499: aastore
      //   500: ldc_w 326
      //   503: iconst_2
      //   504: anewarray 322	java/lang/String
      //   507: dup
      //   508: iconst_0
      //   509: lload 9
      //   511: invokestatic 329	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   514: aastore
      //   515: dup
      //   516: iconst_1
      //   517: aload 4
      //   519: aastore
      //   520: aconst_null
      //   521: invokevirtual 335	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   524: astore_3
      //   525: goto +70 -> 595
      //   528: aload_0
      //   529: getfield 338	com/android/internal/telephony/CallerInfo:phoneNumber	Ljava/lang/String;
      //   532: ifnull +11 -> 543
      //   535: aload_0
      //   536: getfield 338	com/android/internal/telephony/CallerInfo:phoneNumber	Ljava/lang/String;
      //   539: astore_3
      //   540: goto +5 -> 545
      //   543: aload_2
      //   544: astore_3
      //   545: getstatic 341	android/provider/ContactsContract$CommonDataKinds$Callable:CONTENT_FILTER_URI	Landroid/net/Uri;
      //   548: aload_3
      //   549: invokestatic 345	android/net/Uri:encode	(Ljava/lang/String;)Ljava/lang/String;
      //   552: invokestatic 349	android/net/Uri:withAppendedPath	(Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
      //   555: astore_3
      //   556: aload_0
      //   557: getfield 314	com/android/internal/telephony/CallerInfo:contactIdOrZero	J
      //   560: lstore 9
      //   562: aload 19
      //   564: aload_3
      //   565: iconst_1
      //   566: anewarray 322	java/lang/String
      //   569: dup
      //   570: iconst_0
      //   571: ldc_w 324
      //   574: aastore
      //   575: ldc_w 351
      //   578: iconst_1
      //   579: anewarray 322	java/lang/String
      //   582: dup
      //   583: iconst_0
      //   584: lload 9
      //   586: invokestatic 329	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   589: aastore
      //   590: aconst_null
      //   591: invokevirtual 335	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   594: astore_3
      //   595: aload_3
      //   596: ifnull +102 -> 698
      //   599: aload_3
      //   600: invokeinterface 357 1 0
      //   605: ifle +75 -> 680
      //   608: aload_3
      //   609: invokeinterface 361 1 0
      //   614: ifeq +66 -> 680
      //   617: aload_3
      //   618: iconst_0
      //   619: invokeinterface 365 2 0
      //   624: astore 4
      //   626: aload 19
      //   628: aload 4
      //   630: invokestatic 369	android/provider/CallLog$Calls:updateDataUsageStatForData	(Landroid/content/ContentResolver;Ljava/lang/String;)V
      //   633: iload 11
      //   635: sipush 10000
      //   638: if_icmplt +39 -> 677
      //   641: iload 6
      //   643: iconst_2
      //   644: if_icmpne +33 -> 677
      //   647: aload_0
      //   648: getfield 317	com/android/internal/telephony/CallerInfo:normalizedNumber	Ljava/lang/String;
      //   651: invokestatic 260	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   654: istore 24
      //   656: iload 24
      //   658: ifeq +19 -> 677
      //   661: aload_1
      //   662: aload 19
      //   664: aload 4
      //   666: aload_2
      //   667: invokestatic 373	android/provider/CallLog$Calls:updateNormalizedNumber	(Landroid/content/Context;Landroid/content/ContentResolver;Ljava/lang/String;Ljava/lang/String;)V
      //   670: goto +10 -> 680
      //   673: astore_0
      //   674: goto +16 -> 690
      //   677: goto +3 -> 680
      //   680: aload_3
      //   681: invokeinterface 376 1 0
      //   686: goto +15 -> 701
      //   689: astore_0
      //   690: aload_3
      //   691: invokeinterface 376 1 0
      //   696: aload_0
      //   697: athrow
      //   698: goto +3 -> 701
      //   701: aconst_null
      //   702: astore_0
      //   703: aload_1
      //   704: ldc_w 378
      //   707: invokevirtual 382	android/content/Context:getSystemService	(Ljava/lang/Class;)Ljava/lang/Object;
      //   710: checkcast 378	android/os/UserManager
      //   713: astore_3
      //   714: aload_3
      //   715: invokevirtual 385	android/os/UserManager:getUserHandle	()I
      //   718: istore 6
      //   720: iload 13
      //   722: ifeq +160 -> 882
      //   725: aload_1
      //   726: aload_3
      //   727: getstatic 391	android/os/UserHandle:SYSTEM	Landroid/os/UserHandle;
      //   730: aload 8
      //   732: invokestatic 395	android/provider/CallLog$Calls:addEntryAndRemoveExpiredEntries	(Landroid/content/Context;Landroid/os/UserManager;Landroid/os/UserHandle;Landroid/content/ContentValues;)Landroid/net/Uri;
      //   735: astore_2
      //   736: aload_2
      //   737: ifnull +143 -> 880
      //   740: ldc_w 397
      //   743: aload_2
      //   744: invokevirtual 400	android/net/Uri:getAuthority	()Ljava/lang/String;
      //   747: invokevirtual 404	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   750: ifeq +6 -> 756
      //   753: goto +127 -> 880
      //   756: iload 6
      //   758: ifne +5 -> 763
      //   761: aload_2
      //   762: astore_0
      //   763: aload_3
      //   764: iconst_1
      //   765: invokevirtual 408	android/os/UserManager:getUsers	(Z)Ljava/util/List;
      //   768: astore 4
      //   770: aload 4
      //   772: invokeinterface 413 1 0
      //   777: istore 7
      //   779: iconst_0
      //   780: istore 5
      //   782: iload 5
      //   784: iload 7
      //   786: if_icmpge +91 -> 877
      //   789: aload 4
      //   791: iload 5
      //   793: invokeinterface 417 2 0
      //   798: checkcast 419	android/content/pm/UserInfo
      //   801: invokevirtual 422	android/content/pm/UserInfo:getUserHandle	()Landroid/os/UserHandle;
      //   804: astore_2
      //   805: aload_2
      //   806: invokevirtual 425	android/os/UserHandle:getIdentifier	()I
      //   809: istore 11
      //   811: aload_2
      //   812: invokevirtual 428	android/os/UserHandle:isSystem	()Z
      //   815: ifeq +6 -> 821
      //   818: goto +53 -> 871
      //   821: aload_1
      //   822: aload_3
      //   823: iload 11
      //   825: invokestatic 432	android/provider/CallLog$Calls:shouldHaveSharedCallLogEntries	(Landroid/content/Context;Landroid/os/UserManager;I)Z
      //   828: ifne +6 -> 834
      //   831: goto +40 -> 871
      //   834: aload_3
      //   835: aload_2
      //   836: invokevirtual 436	android/os/UserManager:isUserRunning	(Landroid/os/UserHandle;)Z
      //   839: ifeq +32 -> 871
      //   842: aload_3
      //   843: aload_2
      //   844: invokevirtual 439	android/os/UserManager:isUserUnlocked	(Landroid/os/UserHandle;)Z
      //   847: ifeq +24 -> 871
      //   850: aload_1
      //   851: aload_3
      //   852: aload_2
      //   853: aload 8
      //   855: invokestatic 395	android/provider/CallLog$Calls:addEntryAndRemoveExpiredEntries	(Landroid/content/Context;Landroid/os/UserManager;Landroid/os/UserHandle;Landroid/content/ContentValues;)Landroid/net/Uri;
      //   858: astore_2
      //   859: iload 11
      //   861: iload 6
      //   863: if_icmpne +8 -> 871
      //   866: aload_2
      //   867: astore_0
      //   868: goto +3 -> 871
      //   871: iinc 5 1
      //   874: goto -92 -> 782
      //   877: goto +30 -> 907
      //   880: aconst_null
      //   881: areturn
      //   882: aload 14
      //   884: ifnull +6 -> 890
      //   887: goto +10 -> 897
      //   890: iload 6
      //   892: invokestatic 443	android/os/UserHandle:of	(I)Landroid/os/UserHandle;
      //   895: astore 14
      //   897: aload_1
      //   898: aload_3
      //   899: aload 14
      //   901: aload 8
      //   903: invokestatic 395	android/provider/CallLog$Calls:addEntryAndRemoveExpiredEntries	(Landroid/content/Context;Landroid/os/UserManager;Landroid/os/UserHandle;Landroid/content/ContentValues;)Landroid/net/Uri;
      //   906: astore_0
      //   907: aload_0
      //   908: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	909	0	paramCallerInfo	CallerInfo
      //   0	909	1	paramContext	Context
      //   0	909	2	paramString1	String
      //   0	909	3	paramString2	String
      //   0	909	4	paramString3	String
      //   0	909	5	paramInt1	int
      //   0	909	6	paramInt2	int
      //   0	909	7	paramInt3	int
      //   0	909	8	paramPhoneAccountHandle	PhoneAccountHandle
      //   0	909	9	paramLong	long
      //   0	909	11	paramInt4	int
      //   0	909	12	paramLong1	Long
      //   0	909	13	paramBoolean1	boolean
      //   0	909	14	paramUserHandle	UserHandle
      //   0	909	15	paramBoolean2	boolean
      //   0	909	16	paramInt5	int
      //   0	909	17	paramInt6	int
      //   0	909	18	paramString4	String
      //   4	659	19	localContentResolver	ContentResolver
      //   7	219	20	i	int
      //   10	333	21	localObject1	Object
      //   16	3	22	localTelecomManager	android.telecom.TelecomManager
      //   25	1	22	localUnsupportedOperationException	UnsupportedOperationException
      //   35	317	22	localObject2	Object
      //   31	303	23	str	String
      //   654	3	24	bool	boolean
      // Exception table:
      //   from	to	target	type
      //   12	18	25	java/lang/UnsupportedOperationException
      //   661	670	673	finally
      //   599	633	689	finally
      //   647	656	689	finally
    }
    
    private static Uri addEntryAndRemoveExpiredEntries(Context paramContext, UserManager paramUserManager, UserHandle paramUserHandle, ContentValues paramContentValues)
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      if (paramUserManager.isUserUnlocked(paramUserHandle)) {
        paramContext = CONTENT_URI;
      } else {
        paramContext = SHADOW_CONTENT_URI;
      }
      paramContext = ContentProvider.maybeAddUserId(paramContext, paramUserHandle.getIdentifier());
      try
      {
        paramUserManager = localContentResolver.insert(paramContext, paramContentValues);
        if ((paramContentValues.containsKey("subscription_id")) && (!TextUtils.isEmpty(paramContentValues.getAsString("subscription_id"))) && (paramContentValues.containsKey("subscription_component_name")) && (!TextUtils.isEmpty(paramContentValues.getAsString("subscription_component_name")))) {
          localContentResolver.delete(paramContext, "_id IN (SELECT _id FROM calls WHERE subscription_component_name = ? AND subscription_id = ? ORDER BY date DESC LIMIT -1 OFFSET 500)", new String[] { paramContentValues.getAsString("subscription_component_name"), paramContentValues.getAsString("subscription_id") });
        } else {
          localContentResolver.delete(paramContext, "_id IN (SELECT _id FROM calls ORDER BY date DESC LIMIT -1 OFFSET 500)", null);
        }
        return paramUserManager;
      }
      catch (IllegalArgumentException paramContext)
      {
        Log.w("CallLog", "Failed to insert calllog", paramContext);
      }
      return null;
    }
    
    private static String getCurrentCountryIso(Context paramContext)
    {
      Object localObject1 = null;
      Object localObject2 = (CountryDetector)paramContext.getSystemService("country_detector");
      paramContext = localObject1;
      if (localObject2 != null)
      {
        localObject2 = ((CountryDetector)localObject2).detectCountry();
        paramContext = localObject1;
        if (localObject2 != null) {
          paramContext = ((Country)localObject2).getCountryIso();
        }
      }
      return paramContext;
    }
    
    public static String getLastOutgoingCall(Context paramContext)
    {
      Object localObject1 = paramContext.getContentResolver();
      paramContext = null;
      try
      {
        localObject1 = ((ContentResolver)localObject1).query(CONTENT_URI, new String[] { "number" }, "type = 2", null, "date DESC LIMIT 1");
        if (localObject1 != null)
        {
          paramContext = (Context)localObject1;
          if (((Cursor)localObject1).moveToFirst())
          {
            paramContext = (Context)localObject1;
            String str = ((Cursor)localObject1).getString(0);
            if (localObject1 != null) {
              ((Cursor)localObject1).close();
            }
            return str;
          }
        }
        if (localObject1 != null) {
          ((Cursor)localObject1).close();
        }
        return "";
      }
      finally
      {
        if (paramContext != null) {
          paramContext.close();
        }
      }
    }
    
    public static boolean shouldHaveSharedCallLogEntries(Context paramContext, UserManager paramUserManager, int paramInt)
    {
      boolean bool1 = paramUserManager.hasUserRestriction("no_outgoing_calls", UserHandle.of(paramInt));
      boolean bool2 = false;
      if (bool1) {
        return false;
      }
      paramContext = paramUserManager.getUserInfo(paramInt);
      bool1 = bool2;
      if (paramContext != null)
      {
        bool1 = bool2;
        if (!paramContext.isManagedProfile()) {
          bool1 = true;
        }
      }
      return bool1;
    }
    
    private static void updateDataUsageStatForData(ContentResolver paramContentResolver, String paramString)
    {
      paramContentResolver.update(ContactsContract.DataUsageFeedback.FEEDBACK_URI.buildUpon().appendPath(paramString).appendQueryParameter("type", "call").build(), new ContentValues(), null, null);
    }
    
    private static void updateNormalizedNumber(Context paramContext, ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      if ((!TextUtils.isEmpty(paramString2)) && (!TextUtils.isEmpty(paramString1)))
      {
        if (TextUtils.isEmpty(getCurrentCountryIso(paramContext))) {
          return;
        }
        paramString2 = PhoneNumberUtils.formatNumberToE164(paramString2, getCurrentCountryIso(paramContext));
        if (TextUtils.isEmpty(paramString2)) {
          return;
        }
        paramContext = new ContentValues();
        paramContext.put("data4", paramString2);
        paramContentResolver.update(ContactsContract.Data.CONTENT_URI, paramContext, "_id=?", new String[] { paramString1 });
        return;
      }
    }
  }
}
