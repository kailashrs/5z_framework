package com.google.android.mms.pdu;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.drm.DrmManagerClient;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.Telephony.Mms.Draft;
import android.provider.Telephony.Mms.Inbox;
import android.provider.Telephony.Mms.Outbox;
import android.provider.Telephony.Mms.Sent;
import android.provider.Telephony.MmsSms.PendingMessages;
import android.provider.Telephony.Threads;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.mms.InvalidHeaderValueException;
import com.google.android.mms.MmsException;
import com.google.android.mms.util.PduCache;
import com.google.android.mms.util.SqliteWrapper;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class PduPersister
{
  private static final int[] ADDRESS_FIELDS = { 129, 130, 137, 151 };
  private static final HashMap<Integer, Integer> CHARSET_COLUMN_INDEX_MAP;
  private static final HashMap<Integer, String> CHARSET_COLUMN_NAME_MAP;
  private static final boolean DEBUG = false;
  private static final long DUMMY_THREAD_ID = Long.MAX_VALUE;
  private static final HashMap<Integer, Integer> ENCODED_STRING_COLUMN_INDEX_MAP;
  private static final HashMap<Integer, String> ENCODED_STRING_COLUMN_NAME_MAP;
  private static final boolean LOCAL_LOGV = false;
  private static final HashMap<Integer, Integer> LONG_COLUMN_INDEX_MAP;
  private static final HashMap<Integer, String> LONG_COLUMN_NAME_MAP;
  private static final HashMap<Uri, Integer> MESSAGE_BOX_MAP;
  private static final HashMap<Integer, Integer> OCTET_COLUMN_INDEX_MAP;
  private static final HashMap<Integer, String> OCTET_COLUMN_NAME_MAP;
  private static final int PART_COLUMN_CHARSET = 1;
  private static final int PART_COLUMN_CONTENT_DISPOSITION = 2;
  private static final int PART_COLUMN_CONTENT_ID = 3;
  private static final int PART_COLUMN_CONTENT_LOCATION = 4;
  private static final int PART_COLUMN_CONTENT_TYPE = 5;
  private static final int PART_COLUMN_FILENAME = 6;
  private static final int PART_COLUMN_ID = 0;
  private static final int PART_COLUMN_NAME = 7;
  private static final int PART_COLUMN_TEXT = 8;
  private static final String[] PART_PROJECTION;
  private static final PduCache PDU_CACHE_INSTANCE = PduCache.getInstance();
  private static final int PDU_COLUMN_CONTENT_CLASS = 11;
  private static final int PDU_COLUMN_CONTENT_LOCATION = 5;
  private static final int PDU_COLUMN_CONTENT_TYPE = 6;
  private static final int PDU_COLUMN_DATE = 21;
  private static final int PDU_COLUMN_DELIVERY_REPORT = 12;
  private static final int PDU_COLUMN_DELIVERY_TIME = 22;
  private static final int PDU_COLUMN_EXPIRY = 23;
  private static final int PDU_COLUMN_ID = 0;
  private static final int PDU_COLUMN_MESSAGE_BOX = 1;
  private static final int PDU_COLUMN_MESSAGE_CLASS = 7;
  private static final int PDU_COLUMN_MESSAGE_ID = 8;
  private static final int PDU_COLUMN_MESSAGE_SIZE = 24;
  private static final int PDU_COLUMN_MESSAGE_TYPE = 13;
  private static final int PDU_COLUMN_MMS_VERSION = 14;
  private static final int PDU_COLUMN_PRIORITY = 15;
  private static final int PDU_COLUMN_READ_REPORT = 16;
  private static final int PDU_COLUMN_READ_STATUS = 17;
  private static final int PDU_COLUMN_REPORT_ALLOWED = 18;
  private static final int PDU_COLUMN_RESPONSE_TEXT = 9;
  private static final int PDU_COLUMN_RETRIEVE_STATUS = 19;
  private static final int PDU_COLUMN_RETRIEVE_TEXT = 3;
  private static final int PDU_COLUMN_RETRIEVE_TEXT_CHARSET = 26;
  private static final int PDU_COLUMN_STATUS = 20;
  private static final int PDU_COLUMN_SUBJECT = 4;
  private static final int PDU_COLUMN_SUBJECT_CHARSET = 25;
  private static final int PDU_COLUMN_THREAD_ID = 2;
  private static final int PDU_COLUMN_TRANSACTION_ID = 10;
  private static final String[] PDU_PROJECTION = { "_id", "msg_box", "thread_id", "retr_txt", "sub", "ct_l", "ct_t", "m_cls", "m_id", "resp_txt", "tr_id", "ct_cls", "d_rpt", "m_type", "v", "pri", "rr", "read_status", "rpt_a", "retr_st", "st", "date", "d_tm", "exp", "m_size", "sub_cs", "retr_txt_cs" };
  public static final int PROC_STATUS_COMPLETED = 3;
  public static final int PROC_STATUS_PERMANENTLY_FAILURE = 2;
  public static final int PROC_STATUS_TRANSIENT_FAILURE = 1;
  private static final String TAG = "PduPersister";
  public static final String TEMPORARY_DRM_OBJECT_URI = "content://mms/9223372036854775807/part";
  private static final HashMap<Integer, Integer> TEXT_STRING_COLUMN_INDEX_MAP;
  private static final HashMap<Integer, String> TEXT_STRING_COLUMN_NAME_MAP;
  private static PduPersister sPersister;
  private final ContentResolver mContentResolver;
  private final Context mContext;
  private final DrmManagerClient mDrmManagerClient;
  private final TelephonyManager mTelephonyManager;
  
  static
  {
    PART_PROJECTION = new String[] { "_id", "chset", "cd", "cid", "cl", "ct", "fn", "name", "text" };
    MESSAGE_BOX_MAP = new HashMap();
    MESSAGE_BOX_MAP.put(Telephony.Mms.Inbox.CONTENT_URI, Integer.valueOf(1));
    MESSAGE_BOX_MAP.put(Telephony.Mms.Sent.CONTENT_URI, Integer.valueOf(2));
    MESSAGE_BOX_MAP.put(Telephony.Mms.Draft.CONTENT_URI, Integer.valueOf(3));
    MESSAGE_BOX_MAP.put(Telephony.Mms.Outbox.CONTENT_URI, Integer.valueOf(4));
    CHARSET_COLUMN_INDEX_MAP = new HashMap();
    CHARSET_COLUMN_INDEX_MAP.put(Integer.valueOf(150), Integer.valueOf(25));
    CHARSET_COLUMN_INDEX_MAP.put(Integer.valueOf(154), Integer.valueOf(26));
    CHARSET_COLUMN_NAME_MAP = new HashMap();
    CHARSET_COLUMN_NAME_MAP.put(Integer.valueOf(150), "sub_cs");
    CHARSET_COLUMN_NAME_MAP.put(Integer.valueOf(154), "retr_txt_cs");
    ENCODED_STRING_COLUMN_INDEX_MAP = new HashMap();
    ENCODED_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(154), Integer.valueOf(3));
    ENCODED_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(150), Integer.valueOf(4));
    ENCODED_STRING_COLUMN_NAME_MAP = new HashMap();
    ENCODED_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(154), "retr_txt");
    ENCODED_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(150), "sub");
    TEXT_STRING_COLUMN_INDEX_MAP = new HashMap();
    TEXT_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(131), Integer.valueOf(5));
    TEXT_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(132), Integer.valueOf(6));
    TEXT_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(138), Integer.valueOf(7));
    TEXT_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(139), Integer.valueOf(8));
    TEXT_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(147), Integer.valueOf(9));
    TEXT_STRING_COLUMN_INDEX_MAP.put(Integer.valueOf(152), Integer.valueOf(10));
    TEXT_STRING_COLUMN_NAME_MAP = new HashMap();
    TEXT_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(131), "ct_l");
    TEXT_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(132), "ct_t");
    TEXT_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(138), "m_cls");
    TEXT_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(139), "m_id");
    TEXT_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(147), "resp_txt");
    TEXT_STRING_COLUMN_NAME_MAP.put(Integer.valueOf(152), "tr_id");
    OCTET_COLUMN_INDEX_MAP = new HashMap();
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(186), Integer.valueOf(11));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(134), Integer.valueOf(12));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(140), Integer.valueOf(13));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(141), Integer.valueOf(14));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(143), Integer.valueOf(15));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(144), Integer.valueOf(16));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(155), Integer.valueOf(17));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(145), Integer.valueOf(18));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(153), Integer.valueOf(19));
    OCTET_COLUMN_INDEX_MAP.put(Integer.valueOf(149), Integer.valueOf(20));
    OCTET_COLUMN_NAME_MAP = new HashMap();
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(186), "ct_cls");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(134), "d_rpt");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(140), "m_type");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(141), "v");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(143), "pri");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(144), "rr");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(155), "read_status");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(145), "rpt_a");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(153), "retr_st");
    OCTET_COLUMN_NAME_MAP.put(Integer.valueOf(149), "st");
    LONG_COLUMN_INDEX_MAP = new HashMap();
    LONG_COLUMN_INDEX_MAP.put(Integer.valueOf(133), Integer.valueOf(21));
    LONG_COLUMN_INDEX_MAP.put(Integer.valueOf(135), Integer.valueOf(22));
    LONG_COLUMN_INDEX_MAP.put(Integer.valueOf(136), Integer.valueOf(23));
    LONG_COLUMN_INDEX_MAP.put(Integer.valueOf(142), Integer.valueOf(24));
    LONG_COLUMN_NAME_MAP = new HashMap();
    LONG_COLUMN_NAME_MAP.put(Integer.valueOf(133), "date");
    LONG_COLUMN_NAME_MAP.put(Integer.valueOf(135), "d_tm");
    LONG_COLUMN_NAME_MAP.put(Integer.valueOf(136), "exp");
    LONG_COLUMN_NAME_MAP.put(Integer.valueOf(142), "m_size");
  }
  
  private PduPersister(Context paramContext)
  {
    mContext = paramContext;
    mContentResolver = paramContext.getContentResolver();
    mDrmManagerClient = new DrmManagerClient(paramContext);
    mTelephonyManager = ((TelephonyManager)paramContext.getSystemService("phone"));
  }
  
  /* Error */
  public static String convertUriToPath(Context paramContext, Uri paramUri)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_1
    //   3: ifnull +231 -> 234
    //   6: aload_1
    //   7: invokevirtual 297	android/net/Uri:getScheme	()Ljava/lang/String;
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull +217 -> 229
    //   15: aload_2
    //   16: ldc_w 299
    //   19: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   22: ifne +207 -> 229
    //   25: aload_2
    //   26: ldc_w 305
    //   29: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   32: ifeq +6 -> 38
    //   35: goto +194 -> 229
    //   38: aload_2
    //   39: ldc_w 307
    //   42: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   45: ifeq +11 -> 56
    //   48: aload_1
    //   49: invokevirtual 310	android/net/Uri:toString	()Ljava/lang/String;
    //   52: astore_2
    //   53: goto +181 -> 234
    //   56: aload_2
    //   57: ldc_w 312
    //   60: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   63: ifeq +155 -> 218
    //   66: aconst_null
    //   67: astore_3
    //   68: aconst_null
    //   69: astore_2
    //   70: aload_0
    //   71: invokevirtual 269	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   74: aload_1
    //   75: iconst_1
    //   76: anewarray 124	java/lang/String
    //   79: dup
    //   80: iconst_0
    //   81: ldc_w 314
    //   84: aastore
    //   85: aconst_null
    //   86: aconst_null
    //   87: aconst_null
    //   88: invokevirtual 320	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   91: astore_0
    //   92: aload_0
    //   93: ifnull +64 -> 157
    //   96: aload_0
    //   97: astore_2
    //   98: aload_0
    //   99: astore_3
    //   100: aload_0
    //   101: invokeinterface 326 1 0
    //   106: ifeq +51 -> 157
    //   109: aload_0
    //   110: astore_2
    //   111: aload_0
    //   112: astore_3
    //   113: aload_0
    //   114: invokeinterface 330 1 0
    //   119: ifeq +38 -> 157
    //   122: aload_0
    //   123: astore_2
    //   124: aload_0
    //   125: astore_3
    //   126: aload_0
    //   127: aload_0
    //   128: ldc_w 314
    //   131: invokeinterface 334 2 0
    //   136: invokeinterface 338 2 0
    //   141: astore_1
    //   142: aload_1
    //   143: astore_2
    //   144: aload_0
    //   145: ifnull +9 -> 154
    //   148: aload_0
    //   149: invokeinterface 341 1 0
    //   154: goto +80 -> 234
    //   157: aload_0
    //   158: astore_2
    //   159: aload_0
    //   160: astore_3
    //   161: new 343	java/lang/IllegalArgumentException
    //   164: astore_1
    //   165: aload_0
    //   166: astore_2
    //   167: aload_0
    //   168: astore_3
    //   169: aload_1
    //   170: ldc_w 345
    //   173: invokespecial 348	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   176: aload_0
    //   177: astore_2
    //   178: aload_0
    //   179: astore_3
    //   180: aload_1
    //   181: athrow
    //   182: astore_0
    //   183: goto +23 -> 206
    //   186: astore_0
    //   187: aload_3
    //   188: astore_2
    //   189: new 343	java/lang/IllegalArgumentException
    //   192: astore_0
    //   193: aload_3
    //   194: astore_2
    //   195: aload_0
    //   196: ldc_w 350
    //   199: invokespecial 348	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   202: aload_3
    //   203: astore_2
    //   204: aload_0
    //   205: athrow
    //   206: aload_2
    //   207: ifnull +9 -> 216
    //   210: aload_2
    //   211: invokeinterface 341 1 0
    //   216: aload_0
    //   217: athrow
    //   218: new 343	java/lang/IllegalArgumentException
    //   221: dup
    //   222: ldc_w 352
    //   225: invokespecial 348	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   228: athrow
    //   229: aload_1
    //   230: invokevirtual 355	android/net/Uri:getPath	()Ljava/lang/String;
    //   233: astore_2
    //   234: aload_2
    //   235: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	236	0	paramContext	Context
    //   0	236	1	paramUri	Uri
    //   1	234	2	localObject	Object
    //   67	136	3	localContext	Context
    // Exception table:
    //   from	to	target	type
    //   70	92	182	finally
    //   100	109	182	finally
    //   113	122	182	finally
    //   126	142	182	finally
    //   161	165	182	finally
    //   169	176	182	finally
    //   180	182	182	finally
    //   189	193	182	finally
    //   195	202	182	finally
    //   204	206	182	finally
    //   70	92	186	android/database/sqlite/SQLiteException
    //   100	109	186	android/database/sqlite/SQLiteException
    //   113	122	186	android/database/sqlite/SQLiteException
    //   126	142	186	android/database/sqlite/SQLiteException
    //   161	165	186	android/database/sqlite/SQLiteException
    //   169	176	186	android/database/sqlite/SQLiteException
    //   180	182	186	android/database/sqlite/SQLiteException
  }
  
  private byte[] getByteArrayFromPartColumn(Cursor paramCursor, int paramInt)
  {
    if (!paramCursor.isNull(paramInt)) {
      return getBytes(paramCursor.getString(paramInt));
    }
    return null;
  }
  
  public static byte[] getBytes(String paramString)
  {
    try
    {
      paramString = paramString.getBytes("iso-8859-1");
      return paramString;
    }
    catch (UnsupportedEncodingException paramString)
    {
      Log.e("PduPersister", "ISO_8859_1 must be supported!", paramString);
    }
    return new byte[0];
  }
  
  private Integer getIntegerFromPartColumn(Cursor paramCursor, int paramInt)
  {
    if (!paramCursor.isNull(paramInt)) {
      return Integer.valueOf(paramCursor.getInt(paramInt));
    }
    return null;
  }
  
  private static String getPartContentType(PduPart paramPduPart)
  {
    if (paramPduPart.getContentType() == null) {
      paramPduPart = null;
    } else {
      paramPduPart = toIsoString(paramPduPart.getContentType());
    }
    return paramPduPart;
  }
  
  public static PduPersister getPduPersister(Context paramContext)
  {
    if (sPersister == null)
    {
      sPersister = new PduPersister(paramContext);
    }
    else if (!paramContext.equals(sPersistermContext))
    {
      sPersister.release();
      sPersister = new PduPersister(paramContext);
    }
    return sPersister;
  }
  
  private void loadAddress(long paramLong, PduHeaders paramPduHeaders)
  {
    Object localObject1 = mContext;
    Object localObject2 = mContentResolver;
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("content://mms/");
    ((StringBuilder)localObject3).append(paramLong);
    ((StringBuilder)localObject3).append("/addr");
    localObject3 = SqliteWrapper.query((Context)localObject1, (ContentResolver)localObject2, Uri.parse(((StringBuilder)localObject3).toString()), new String[] { "address", "charset", "type" }, null, null, null);
    if (localObject3 != null) {
      try
      {
        while (((Cursor)localObject3).moveToNext())
        {
          localObject2 = ((Cursor)localObject3).getString(0);
          if (!TextUtils.isEmpty((CharSequence)localObject2))
          {
            int i = ((Cursor)localObject3).getInt(2);
            if (i != 137)
            {
              if (i != 151)
              {
                switch (i)
                {
                default: 
                  localObject2 = new java/lang/StringBuilder;
                  ((StringBuilder)localObject2).<init>();
                  ((StringBuilder)localObject2).append("Unknown address type: ");
                  ((StringBuilder)localObject2).append(i);
                  Log.e("PduPersister", ((StringBuilder)localObject2).toString());
                  break;
                }
              }
              else
              {
                localObject1 = new com/google/android/mms/pdu/EncodedStringValue;
                ((EncodedStringValue)localObject1).<init>(((Cursor)localObject3).getInt(1), getBytes((String)localObject2));
                paramPduHeaders.appendEncodedStringValue((EncodedStringValue)localObject1, i);
              }
            }
            else
            {
              localObject1 = new com/google/android/mms/pdu/EncodedStringValue;
              ((EncodedStringValue)localObject1).<init>(((Cursor)localObject3).getInt(1), getBytes((String)localObject2));
              paramPduHeaders.setEncodedStringValue((EncodedStringValue)localObject1, i);
            }
          }
        }
      }
      finally
      {
        ((Cursor)localObject3).close();
      }
    }
  }
  
  /* Error */
  private PduPart[] loadParts(long paramLong)
    throws MmsException
  {
    // Byte code:
    //   0: aload_0
    //   1: astore_3
    //   2: aload_3
    //   3: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   6: astore 4
    //   8: aload_3
    //   9: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   12: astore 5
    //   14: new 409	java/lang/StringBuilder
    //   17: dup
    //   18: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   21: astore_3
    //   22: aload_3
    //   23: ldc_w 412
    //   26: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: pop
    //   30: aload_3
    //   31: lload_1
    //   32: invokevirtual 419	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: aload_3
    //   37: ldc_w 476
    //   40: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: aload 4
    //   46: aload 5
    //   48: aload_3
    //   49: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   52: invokestatic 426	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   55: getstatic 198	com/google/android/mms/pdu/PduPersister:PART_PROJECTION	[Ljava/lang/String;
    //   58: aconst_null
    //   59: aconst_null
    //   60: aconst_null
    //   61: invokestatic 437	com/google/android/mms/util/SqliteWrapper:query	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   64: astore 6
    //   66: aload 6
    //   68: ifnull +672 -> 740
    //   71: aload 6
    //   73: invokeinterface 326 1 0
    //   78: ifne +6 -> 84
    //   81: goto +659 -> 740
    //   84: aload 6
    //   86: invokeinterface 326 1 0
    //   91: istore 7
    //   93: iload 7
    //   95: anewarray 388	com/google/android/mms/pdu/PduPart
    //   98: astore 8
    //   100: iconst_0
    //   101: istore 9
    //   103: aload_0
    //   104: astore_3
    //   105: aload 6
    //   107: invokeinterface 440 1 0
    //   112: ifeq +598 -> 710
    //   115: new 388	com/google/android/mms/pdu/PduPart
    //   118: astore 10
    //   120: aload 10
    //   122: invokespecial 477	com/google/android/mms/pdu/PduPart:<init>	()V
    //   125: aload_3
    //   126: aload 6
    //   128: iconst_1
    //   129: invokespecial 479	com/google/android/mms/pdu/PduPersister:getIntegerFromPartColumn	(Landroid/database/Cursor;I)Ljava/lang/Integer;
    //   132: astore 4
    //   134: aload 4
    //   136: ifnull +13 -> 149
    //   139: aload 10
    //   141: aload 4
    //   143: invokevirtual 482	java/lang/Integer:intValue	()I
    //   146: invokevirtual 486	com/google/android/mms/pdu/PduPart:setCharset	(I)V
    //   149: aload_3
    //   150: aload 6
    //   152: iconst_2
    //   153: invokespecial 488	com/google/android/mms/pdu/PduPersister:getByteArrayFromPartColumn	(Landroid/database/Cursor;I)[B
    //   156: astore 4
    //   158: aload 4
    //   160: ifnull +10 -> 170
    //   163: aload 10
    //   165: aload 4
    //   167: invokevirtual 492	com/google/android/mms/pdu/PduPart:setContentDisposition	([B)V
    //   170: aload_3
    //   171: aload 6
    //   173: iconst_3
    //   174: invokespecial 488	com/google/android/mms/pdu/PduPersister:getByteArrayFromPartColumn	(Landroid/database/Cursor;I)[B
    //   177: astore 4
    //   179: aload 4
    //   181: ifnull +10 -> 191
    //   184: aload 10
    //   186: aload 4
    //   188: invokevirtual 495	com/google/android/mms/pdu/PduPart:setContentId	([B)V
    //   191: aload_3
    //   192: aload 6
    //   194: iconst_4
    //   195: invokespecial 488	com/google/android/mms/pdu/PduPersister:getByteArrayFromPartColumn	(Landroid/database/Cursor;I)[B
    //   198: astore 4
    //   200: aload 4
    //   202: ifnull +10 -> 212
    //   205: aload 10
    //   207: aload 4
    //   209: invokevirtual 498	com/google/android/mms/pdu/PduPart:setContentLocation	([B)V
    //   212: aload_3
    //   213: aload 6
    //   215: iconst_5
    //   216: invokespecial 488	com/google/android/mms/pdu/PduPersister:getByteArrayFromPartColumn	(Landroid/database/Cursor;I)[B
    //   219: astore 4
    //   221: aload 4
    //   223: ifnull +474 -> 697
    //   226: aload 10
    //   228: aload 4
    //   230: invokevirtual 501	com/google/android/mms/pdu/PduPart:setContentType	([B)V
    //   233: aload_3
    //   234: aload 6
    //   236: bipush 6
    //   238: invokespecial 488	com/google/android/mms/pdu/PduPersister:getByteArrayFromPartColumn	(Landroid/database/Cursor;I)[B
    //   241: astore 5
    //   243: aload 5
    //   245: ifnull +10 -> 255
    //   248: aload 10
    //   250: aload 5
    //   252: invokevirtual 504	com/google/android/mms/pdu/PduPart:setFilename	([B)V
    //   255: aload_3
    //   256: aload 6
    //   258: bipush 7
    //   260: invokespecial 488	com/google/android/mms/pdu/PduPersister:getByteArrayFromPartColumn	(Landroid/database/Cursor;I)[B
    //   263: astore 5
    //   265: aload 5
    //   267: ifnull +10 -> 277
    //   270: aload 10
    //   272: aload 5
    //   274: invokevirtual 507	com/google/android/mms/pdu/PduPart:setName	([B)V
    //   277: aload 6
    //   279: iconst_0
    //   280: invokeinterface 511 2 0
    //   285: lstore_1
    //   286: new 409	java/lang/StringBuilder
    //   289: astore 5
    //   291: aload 5
    //   293: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   296: aload 5
    //   298: ldc_w 513
    //   301: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   304: pop
    //   305: aload 5
    //   307: lload_1
    //   308: invokevirtual 419	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   311: pop
    //   312: aload 5
    //   314: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   317: invokestatic 426	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   320: astore 5
    //   322: aload 10
    //   324: aload 5
    //   326: invokevirtual 517	com/google/android/mms/pdu/PduPart:setDataUri	(Landroid/net/Uri;)V
    //   329: aload 4
    //   331: invokestatic 396	com/google/android/mms/pdu/PduPersister:toIsoString	([B)Ljava/lang/String;
    //   334: astore 4
    //   336: aload 4
    //   338: invokestatic 523	com/google/android/mms/ContentType:isImageType	(Ljava/lang/String;)Z
    //   341: ifne +343 -> 684
    //   344: aload 4
    //   346: invokestatic 526	com/google/android/mms/ContentType:isAudioType	(Ljava/lang/String;)Z
    //   349: ifne +335 -> 684
    //   352: aload 4
    //   354: invokestatic 529	com/google/android/mms/ContentType:isVideoType	(Ljava/lang/String;)Z
    //   357: ifne +327 -> 684
    //   360: new 531	java/io/ByteArrayOutputStream
    //   363: astore 11
    //   365: aload 11
    //   367: invokespecial 532	java/io/ByteArrayOutputStream:<init>	()V
    //   370: aconst_null
    //   371: astore 12
    //   373: ldc_w 534
    //   376: aload 4
    //   378: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   381: ifne +243 -> 624
    //   384: ldc_w 536
    //   387: aload 4
    //   389: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   392: ifne +232 -> 624
    //   395: ldc_w 538
    //   398: aload 4
    //   400: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   403: istore 13
    //   405: iload 13
    //   407: ifeq +6 -> 413
    //   410: goto +214 -> 624
    //   413: aload_3
    //   414: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   417: aload 5
    //   419: invokevirtual 542	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   422: astore_3
    //   423: sipush 256
    //   426: newarray byte
    //   428: astore 12
    //   430: aload_3
    //   431: aload 12
    //   433: invokevirtual 548	java/io/InputStream:read	([B)I
    //   436: istore 14
    //   438: iload 14
    //   440: iflt +40 -> 480
    //   443: aload 11
    //   445: aload 12
    //   447: iconst_0
    //   448: iload 14
    //   450: invokevirtual 552	java/io/ByteArrayOutputStream:write	([BII)V
    //   453: aload_3
    //   454: aload 12
    //   456: invokevirtual 548	java/io/InputStream:read	([B)I
    //   459: istore 14
    //   461: goto -23 -> 438
    //   464: astore 5
    //   466: aload_3
    //   467: astore 4
    //   469: aload 5
    //   471: astore_3
    //   472: goto +121 -> 593
    //   475: astore 4
    //   477: goto +75 -> 552
    //   480: aload_3
    //   481: ifnull +24 -> 505
    //   484: aload_3
    //   485: invokevirtual 553	java/io/InputStream:close	()V
    //   488: goto +17 -> 505
    //   491: astore_3
    //   492: ldc 103
    //   494: ldc_w 555
    //   497: aload_3
    //   498: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   501: pop
    //   502: goto -14 -> 488
    //   505: goto +166 -> 671
    //   508: astore 5
    //   510: aload_3
    //   511: astore 4
    //   513: aload 5
    //   515: astore_3
    //   516: goto +28 -> 544
    //   519: astore 4
    //   521: goto +31 -> 552
    //   524: astore 5
    //   526: aload_3
    //   527: astore 4
    //   529: aload 5
    //   531: astore_3
    //   532: goto +61 -> 593
    //   535: astore 4
    //   537: goto +15 -> 552
    //   540: astore_3
    //   541: aconst_null
    //   542: astore 4
    //   544: goto +49 -> 593
    //   547: astore 4
    //   549: aload 12
    //   551: astore_3
    //   552: ldc 103
    //   554: ldc_w 557
    //   557: aload 4
    //   559: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   562: pop
    //   563: aload 6
    //   565: invokeinterface 341 1 0
    //   570: new 472	com/google/android/mms/MmsException
    //   573: astore 5
    //   575: aload 5
    //   577: aload 4
    //   579: invokespecial 560	com/google/android/mms/MmsException:<init>	(Ljava/lang/Throwable;)V
    //   582: aload 5
    //   584: athrow
    //   585: astore 5
    //   587: aload_3
    //   588: astore 4
    //   590: aload 5
    //   592: astore_3
    //   593: aload 4
    //   595: ifnull +27 -> 622
    //   598: aload 4
    //   600: invokevirtual 553	java/io/InputStream:close	()V
    //   603: goto +19 -> 622
    //   606: astore 4
    //   608: ldc 103
    //   610: ldc_w 555
    //   613: aload 4
    //   615: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   618: pop
    //   619: goto +3 -> 622
    //   622: aload_3
    //   623: athrow
    //   624: aload 6
    //   626: bipush 8
    //   628: invokeinterface 338 2 0
    //   633: astore_3
    //   634: new 456	com/google/android/mms/pdu/EncodedStringValue
    //   637: astore 4
    //   639: aload_3
    //   640: ifnull +6 -> 646
    //   643: goto +7 -> 650
    //   646: ldc_w 299
    //   649: astore_3
    //   650: aload 4
    //   652: aload_3
    //   653: invokespecial 561	com/google/android/mms/pdu/EncodedStringValue:<init>	(Ljava/lang/String;)V
    //   656: aload 4
    //   658: invokevirtual 564	com/google/android/mms/pdu/EncodedStringValue:getTextString	()[B
    //   661: astore_3
    //   662: aload 11
    //   664: aload_3
    //   665: iconst_0
    //   666: aload_3
    //   667: arraylength
    //   668: invokevirtual 552	java/io/ByteArrayOutputStream:write	([BII)V
    //   671: aload 10
    //   673: aload 11
    //   675: invokevirtual 567	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   678: invokevirtual 570	com/google/android/mms/pdu/PduPart:setData	([B)V
    //   681: goto +3 -> 684
    //   684: aload 8
    //   686: iload 9
    //   688: aload 10
    //   690: aastore
    //   691: iinc 9 1
    //   694: goto -591 -> 103
    //   697: new 472	com/google/android/mms/MmsException
    //   700: astore_3
    //   701: aload_3
    //   702: ldc_w 572
    //   705: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   708: aload_3
    //   709: athrow
    //   710: aload 6
    //   712: ifnull +10 -> 722
    //   715: aload 6
    //   717: invokeinterface 341 1 0
    //   722: aload 8
    //   724: areturn
    //   725: astore_3
    //   726: aload 6
    //   728: ifnull +10 -> 738
    //   731: aload 6
    //   733: invokeinterface 341 1 0
    //   738: aload_3
    //   739: athrow
    //   740: aload 6
    //   742: ifnull +10 -> 752
    //   745: aload 6
    //   747: invokeinterface 341 1 0
    //   752: aconst_null
    //   753: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	754	0	this	PduPersister
    //   0	754	1	paramLong	long
    //   1	484	3	localObject1	Object
    //   491	20	3	localIOException1	java.io.IOException
    //   515	17	3	localObject2	Object
    //   540	1	3	localObject3	Object
    //   551	158	3	localObject4	Object
    //   725	14	3	localObject5	Object
    //   6	462	4	localObject6	Object
    //   475	1	4	localIOException2	java.io.IOException
    //   511	1	4	localIOException3	java.io.IOException
    //   519	1	4	localIOException4	java.io.IOException
    //   527	1	4	localObject7	Object
    //   535	1	4	localIOException5	java.io.IOException
    //   542	1	4	localObject8	Object
    //   547	31	4	localIOException6	java.io.IOException
    //   588	11	4	localObject9	Object
    //   606	8	4	localIOException7	java.io.IOException
    //   637	20	4	localEncodedStringValue	EncodedStringValue
    //   12	406	5	localObject10	Object
    //   464	6	5	localObject11	Object
    //   508	6	5	localObject12	Object
    //   524	6	5	localObject13	Object
    //   573	10	5	localMmsException	MmsException
    //   585	6	5	localObject14	Object
    //   64	682	6	localCursor	Cursor
    //   91	3	7	i	int
    //   98	625	8	arrayOfPduPart	PduPart[]
    //   101	591	9	j	int
    //   118	571	10	localPduPart	PduPart
    //   363	311	11	localByteArrayOutputStream	java.io.ByteArrayOutputStream
    //   371	179	12	arrayOfByte	byte[]
    //   403	3	13	bool	boolean
    //   436	24	14	k	int
    // Exception table:
    //   from	to	target	type
    //   443	461	464	finally
    //   443	461	475	java/io/IOException
    //   484	488	491	java/io/IOException
    //   430	438	508	finally
    //   430	438	519	java/io/IOException
    //   423	430	524	finally
    //   423	430	535	java/io/IOException
    //   413	423	540	finally
    //   413	423	547	java/io/IOException
    //   552	585	585	finally
    //   598	603	606	java/io/IOException
    //   71	81	725	finally
    //   84	100	725	finally
    //   105	134	725	finally
    //   139	149	725	finally
    //   149	158	725	finally
    //   163	170	725	finally
    //   170	179	725	finally
    //   184	191	725	finally
    //   191	200	725	finally
    //   205	212	725	finally
    //   212	221	725	finally
    //   226	243	725	finally
    //   248	255	725	finally
    //   255	265	725	finally
    //   270	277	725	finally
    //   277	370	725	finally
    //   373	405	725	finally
    //   484	488	725	finally
    //   492	502	725	finally
    //   598	603	725	finally
    //   608	619	725	finally
    //   622	624	725	finally
    //   624	639	725	finally
    //   650	671	725	finally
    //   671	681	725	finally
    //   697	710	725	finally
  }
  
  private void loadRecipients(int paramInt, HashSet<String> paramHashSet, HashMap<Integer, EncodedStringValue[]> paramHashMap, boolean paramBoolean)
  {
    paramHashMap = (EncodedStringValue[])paramHashMap.get(Integer.valueOf(paramInt));
    if (paramHashMap == null) {
      return;
    }
    if ((paramBoolean) && (paramHashMap.length == 1)) {
      return;
    }
    Object localObject1 = SubscriptionManager.from(mContext);
    HashSet localHashSet = new HashSet();
    int i = 0;
    if (paramBoolean) {
      for (int k : ((SubscriptionManager)localObject1).getActiveSubscriptionIdList())
      {
        localObject1 = mTelephonyManager.getLine1Number(k);
        if (localObject1 != null) {
          localHashSet.add(localObject1);
        }
      }
    }
    ??? = paramHashMap.length;
    for (paramInt = i; paramInt < ???; paramInt++)
    {
      localObject1 = paramHashMap[paramInt];
      if (localObject1 != null)
      {
        localObject1 = ((EncodedStringValue)localObject1).getString();
        if (paramBoolean)
        {
          ??? = localHashSet.iterator();
          while (((Iterator)???).hasNext()) {
            if ((!PhoneNumberUtils.compare((String)localObject1, (String)((Iterator)???).next())) && (!paramHashSet.contains(localObject1)))
            {
              paramHashSet.add(localObject1);
              break;
            }
          }
        }
        if (!paramHashSet.contains(localObject1)) {
          paramHashSet.add(localObject1);
        }
      }
    }
  }
  
  private void persistAddress(long paramLong, int paramInt, EncodedStringValue[] paramArrayOfEncodedStringValue)
  {
    ContentValues localContentValues = new ContentValues(3);
    int i = paramArrayOfEncodedStringValue.length;
    for (int j = 0; j < i; j++)
    {
      Object localObject = paramArrayOfEncodedStringValue[j];
      localContentValues.clear();
      localContentValues.put("address", toIsoString(((EncodedStringValue)localObject).getTextString()));
      localContentValues.put("charset", Integer.valueOf(((EncodedStringValue)localObject).getCharacterSet()));
      localContentValues.put("type", Integer.valueOf(paramInt));
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("content://mms/");
      ((StringBuilder)localObject).append(paramLong);
      ((StringBuilder)localObject).append("/addr");
      localObject = Uri.parse(((StringBuilder)localObject).toString());
      SqliteWrapper.insert(mContext, mContentResolver, (Uri)localObject, localContentValues);
    }
  }
  
  /* Error */
  private void persistData(PduPart paramPduPart, Uri paramUri, String paramString, HashMap<Uri, InputStream> paramHashMap)
    throws MmsException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 5
    //   3: aconst_null
    //   4: astore 6
    //   6: aconst_null
    //   7: astore 7
    //   9: aconst_null
    //   10: astore 8
    //   12: aconst_null
    //   13: astore 9
    //   15: aconst_null
    //   16: astore 10
    //   18: aconst_null
    //   19: astore 11
    //   21: aconst_null
    //   22: astore 12
    //   24: aconst_null
    //   25: astore 13
    //   27: aconst_null
    //   28: astore 14
    //   30: aconst_null
    //   31: astore 15
    //   33: aconst_null
    //   34: astore 16
    //   36: aconst_null
    //   37: astore 17
    //   39: aconst_null
    //   40: astore 18
    //   42: aconst_null
    //   43: astore 19
    //   45: aconst_null
    //   46: astore 20
    //   48: aconst_null
    //   49: astore 21
    //   51: aconst_null
    //   52: astore 22
    //   54: aconst_null
    //   55: astore 23
    //   57: aconst_null
    //   58: astore 24
    //   60: aconst_null
    //   61: astore 25
    //   63: aconst_null
    //   64: astore 26
    //   66: aconst_null
    //   67: astore 27
    //   69: aconst_null
    //   70: astore 28
    //   72: aconst_null
    //   73: astore 29
    //   75: aconst_null
    //   76: astore 30
    //   78: aconst_null
    //   79: astore 31
    //   81: aconst_null
    //   82: astore 32
    //   84: aconst_null
    //   85: astore 33
    //   87: aconst_null
    //   88: astore 34
    //   90: aload 31
    //   92: astore 35
    //   94: aload 32
    //   96: astore 36
    //   98: aload 33
    //   100: astore 37
    //   102: aload_1
    //   103: invokevirtual 661	com/google/android/mms/pdu/PduPart:getData	()[B
    //   106: astore 38
    //   108: aload 31
    //   110: astore 35
    //   112: aload 32
    //   114: astore 36
    //   116: aload 33
    //   118: astore 37
    //   120: ldc_w 534
    //   123: aload_3
    //   124: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   127: ifne +1845 -> 1972
    //   130: aload 31
    //   132: astore 35
    //   134: aload 32
    //   136: astore 36
    //   138: aload 33
    //   140: astore 37
    //   142: ldc_w 536
    //   145: aload_3
    //   146: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   149: ifne +1823 -> 1972
    //   152: aload 31
    //   154: astore 35
    //   156: aload 32
    //   158: astore 36
    //   160: aload 33
    //   162: astore 37
    //   164: ldc_w 538
    //   167: aload_3
    //   168: invokevirtual 303	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   171: ifeq +6 -> 177
    //   174: goto +1798 -> 1972
    //   177: aload 31
    //   179: astore 35
    //   181: aload 32
    //   183: astore 36
    //   185: aload 33
    //   187: astore 37
    //   189: aload_3
    //   190: invokestatic 666	com/google/android/mms/util/DownloadDrmHelper:isDrmConvertNeeded	(Ljava/lang/String;)Z
    //   193: istore 39
    //   195: iload 39
    //   197: ifeq +674 -> 871
    //   200: aload_2
    //   201: ifnull +385 -> 586
    //   204: aload 34
    //   206: astore 30
    //   208: aload 31
    //   210: astore 35
    //   212: aload 32
    //   214: astore 36
    //   216: aload 33
    //   218: astore 37
    //   220: aload_0
    //   221: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   224: aload_2
    //   225: invokestatic 668	com/google/android/mms/pdu/PduPersister:convertUriToPath	(Landroid/content/Context;Landroid/net/Uri;)Ljava/lang/String;
    //   228: astore 16
    //   230: aload 16
    //   232: astore 30
    //   234: aload 16
    //   236: astore 35
    //   238: aload 16
    //   240: astore 36
    //   242: aload 16
    //   244: astore 37
    //   246: new 670	java/io/File
    //   249: astore 28
    //   251: aload 16
    //   253: astore 30
    //   255: aload 16
    //   257: astore 35
    //   259: aload 16
    //   261: astore 36
    //   263: aload 16
    //   265: astore 37
    //   267: aload 28
    //   269: aload 16
    //   271: invokespecial 671	java/io/File:<init>	(Ljava/lang/String;)V
    //   274: aload 16
    //   276: astore 30
    //   278: aload 16
    //   280: astore 35
    //   282: aload 16
    //   284: astore 36
    //   286: aload 16
    //   288: astore 37
    //   290: aload 28
    //   292: invokevirtual 675	java/io/File:length	()J
    //   295: lstore 40
    //   297: lload 40
    //   299: lconst_0
    //   300: lcmp
    //   301: ifle +111 -> 412
    //   304: iconst_0
    //   305: ifeq +48 -> 353
    //   308: new 677	java/lang/NullPointerException
    //   311: dup
    //   312: invokespecial 678	java/lang/NullPointerException:<init>	()V
    //   315: athrow
    //   316: astore_2
    //   317: new 409	java/lang/StringBuilder
    //   320: dup
    //   321: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   324: astore_1
    //   325: aload_1
    //   326: ldc_w 680
    //   329: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   332: pop
    //   333: aload_1
    //   334: aconst_null
    //   335: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   338: pop
    //   339: ldc 103
    //   341: aload_1
    //   342: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   345: aload_2
    //   346: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   349: pop
    //   350: goto +3 -> 353
    //   353: iconst_0
    //   354: ifeq +45 -> 399
    //   357: new 677	java/lang/NullPointerException
    //   360: dup
    //   361: invokespecial 678	java/lang/NullPointerException:<init>	()V
    //   364: athrow
    //   365: astore_2
    //   366: new 409	java/lang/StringBuilder
    //   369: dup
    //   370: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   373: astore_1
    //   374: aload_1
    //   375: ldc_w 680
    //   378: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   381: pop
    //   382: aload_1
    //   383: aconst_null
    //   384: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   387: pop
    //   388: ldc 103
    //   390: aload_1
    //   391: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   394: aload_2
    //   395: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   398: pop
    //   399: iconst_0
    //   400: ifeq +11 -> 411
    //   403: new 677	java/lang/NullPointerException
    //   406: dup
    //   407: invokespecial 678	java/lang/NullPointerException:<init>	()V
    //   410: athrow
    //   411: return
    //   412: aload 16
    //   414: astore 30
    //   416: goto +174 -> 590
    //   419: astore 16
    //   421: aload 17
    //   423: astore 6
    //   425: aload 30
    //   427: astore 37
    //   429: aload 18
    //   431: astore 5
    //   433: aload 30
    //   435: astore 35
    //   437: aload 19
    //   439: astore 21
    //   441: aload 30
    //   443: astore 36
    //   445: new 409	java/lang/StringBuilder
    //   448: astore 28
    //   450: aload 17
    //   452: astore 6
    //   454: aload 30
    //   456: astore 37
    //   458: aload 18
    //   460: astore 5
    //   462: aload 30
    //   464: astore 35
    //   466: aload 19
    //   468: astore 21
    //   470: aload 30
    //   472: astore 36
    //   474: aload 28
    //   476: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   479: aload 17
    //   481: astore 6
    //   483: aload 30
    //   485: astore 37
    //   487: aload 18
    //   489: astore 5
    //   491: aload 30
    //   493: astore 35
    //   495: aload 19
    //   497: astore 21
    //   499: aload 30
    //   501: astore 36
    //   503: aload 28
    //   505: ldc_w 685
    //   508: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   511: pop
    //   512: aload 17
    //   514: astore 6
    //   516: aload 30
    //   518: astore 37
    //   520: aload 18
    //   522: astore 5
    //   524: aload 30
    //   526: astore 35
    //   528: aload 19
    //   530: astore 21
    //   532: aload 30
    //   534: astore 36
    //   536: aload 28
    //   538: aload_1
    //   539: invokevirtual 689	com/google/android/mms/pdu/PduPart:getDataUri	()Landroid/net/Uri;
    //   542: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   545: pop
    //   546: aload 17
    //   548: astore 6
    //   550: aload 30
    //   552: astore 37
    //   554: aload 18
    //   556: astore 5
    //   558: aload 30
    //   560: astore 35
    //   562: aload 19
    //   564: astore 21
    //   566: aload 30
    //   568: astore 36
    //   570: ldc 103
    //   572: aload 28
    //   574: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   577: aload 16
    //   579: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   582: pop
    //   583: goto +7 -> 590
    //   586: aload 25
    //   588: astore 30
    //   590: aload 17
    //   592: astore 6
    //   594: aload 30
    //   596: astore 37
    //   598: aload 18
    //   600: astore 5
    //   602: aload 30
    //   604: astore 35
    //   606: aload 19
    //   608: astore 21
    //   610: aload 30
    //   612: astore 36
    //   614: aload_0
    //   615: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   618: aload_3
    //   619: invokestatic 695	com/google/android/mms/util/DrmConvertSession:open	(Landroid/content/Context;Ljava/lang/String;)Lcom/google/android/mms/util/DrmConvertSession;
    //   622: astore 16
    //   624: aload 16
    //   626: ifnull +9 -> 635
    //   629: aload 30
    //   631: astore_3
    //   632: goto +246 -> 878
    //   635: aload 16
    //   637: astore 6
    //   639: aload 30
    //   641: astore 37
    //   643: aload 16
    //   645: astore 5
    //   647: aload 30
    //   649: astore 35
    //   651: aload 16
    //   653: astore 21
    //   655: aload 30
    //   657: astore 36
    //   659: new 472	com/google/android/mms/MmsException
    //   662: astore_1
    //   663: aload 16
    //   665: astore 6
    //   667: aload 30
    //   669: astore 37
    //   671: aload 16
    //   673: astore 5
    //   675: aload 30
    //   677: astore 35
    //   679: aload 16
    //   681: astore 21
    //   683: aload 30
    //   685: astore 36
    //   687: new 409	java/lang/StringBuilder
    //   690: astore_2
    //   691: aload 16
    //   693: astore 6
    //   695: aload 30
    //   697: astore 37
    //   699: aload 16
    //   701: astore 5
    //   703: aload 30
    //   705: astore 35
    //   707: aload 16
    //   709: astore 21
    //   711: aload 30
    //   713: astore 36
    //   715: aload_2
    //   716: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   719: aload 16
    //   721: astore 6
    //   723: aload 30
    //   725: astore 37
    //   727: aload 16
    //   729: astore 5
    //   731: aload 30
    //   733: astore 35
    //   735: aload 16
    //   737: astore 21
    //   739: aload 30
    //   741: astore 36
    //   743: aload_2
    //   744: ldc_w 697
    //   747: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   750: pop
    //   751: aload 16
    //   753: astore 6
    //   755: aload 30
    //   757: astore 37
    //   759: aload 16
    //   761: astore 5
    //   763: aload 30
    //   765: astore 35
    //   767: aload 16
    //   769: astore 21
    //   771: aload 30
    //   773: astore 36
    //   775: aload_2
    //   776: aload_3
    //   777: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   780: pop
    //   781: aload 16
    //   783: astore 6
    //   785: aload 30
    //   787: astore 37
    //   789: aload 16
    //   791: astore 5
    //   793: aload 30
    //   795: astore 35
    //   797: aload 16
    //   799: astore 21
    //   801: aload 30
    //   803: astore 36
    //   805: aload_2
    //   806: ldc_w 699
    //   809: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   812: pop
    //   813: aload 16
    //   815: astore 6
    //   817: aload 30
    //   819: astore 37
    //   821: aload 16
    //   823: astore 5
    //   825: aload 30
    //   827: astore 35
    //   829: aload 16
    //   831: astore 21
    //   833: aload 30
    //   835: astore 36
    //   837: aload_1
    //   838: aload_2
    //   839: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   842: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   845: aload 16
    //   847: astore 6
    //   849: aload 30
    //   851: astore 37
    //   853: aload 16
    //   855: astore 5
    //   857: aload 30
    //   859: astore 35
    //   861: aload 16
    //   863: astore 21
    //   865: aload 30
    //   867: astore 36
    //   869: aload_1
    //   870: athrow
    //   871: aload 26
    //   873: astore_3
    //   874: aload 22
    //   876: astore 16
    //   878: aload 16
    //   880: astore 6
    //   882: aload_3
    //   883: astore 37
    //   885: aload 16
    //   887: astore 5
    //   889: aload_3
    //   890: astore 35
    //   892: aload 16
    //   894: astore 21
    //   896: aload_3
    //   897: astore 36
    //   899: aload_0
    //   900: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   903: aload_2
    //   904: invokevirtual 703	android/content/ContentResolver:openOutputStream	(Landroid/net/Uri;)Ljava/io/OutputStream;
    //   907: astore 30
    //   909: aload 38
    //   911: ifnonnull +820 -> 1731
    //   914: aload_1
    //   915: invokevirtual 689	com/google/android/mms/pdu/PduPart:getDataUri	()Landroid/net/Uri;
    //   918: astore 35
    //   920: aload 35
    //   922: ifnull +555 -> 1477
    //   925: aload 8
    //   927: astore 21
    //   929: aload 35
    //   931: astore 11
    //   933: aload 9
    //   935: astore 6
    //   937: aload 35
    //   939: astore 12
    //   941: aload 30
    //   943: astore 37
    //   945: aload 13
    //   947: astore_1
    //   948: aload 16
    //   950: astore 36
    //   952: aload 35
    //   954: astore 5
    //   956: aload_3
    //   957: astore 5
    //   959: aload 35
    //   961: aload_2
    //   962: invokevirtual 704	android/net/Uri:equals	(Ljava/lang/Object;)Z
    //   965: ifeq +6 -> 971
    //   968: goto +509 -> 1477
    //   971: aload 15
    //   973: astore_2
    //   974: aload 4
    //   976: ifnull +95 -> 1071
    //   979: aload 15
    //   981: astore_2
    //   982: aload 8
    //   984: astore 21
    //   986: aload 35
    //   988: astore 11
    //   990: aload 9
    //   992: astore 6
    //   994: aload 35
    //   996: astore 12
    //   998: aload 30
    //   1000: astore 37
    //   1002: aload 13
    //   1004: astore_1
    //   1005: aload 16
    //   1007: astore 36
    //   1009: aload 35
    //   1011: astore 5
    //   1013: aload_3
    //   1014: astore 5
    //   1016: aload 4
    //   1018: aload 35
    //   1020: invokevirtual 707	java/util/HashMap:containsKey	(Ljava/lang/Object;)Z
    //   1023: ifeq +48 -> 1071
    //   1026: aload 8
    //   1028: astore 21
    //   1030: aload 35
    //   1032: astore 11
    //   1034: aload 9
    //   1036: astore 6
    //   1038: aload 35
    //   1040: astore 12
    //   1042: aload 30
    //   1044: astore 37
    //   1046: aload 13
    //   1048: astore_1
    //   1049: aload 16
    //   1051: astore 36
    //   1053: aload 35
    //   1055: astore 5
    //   1057: aload_3
    //   1058: astore 5
    //   1060: aload 4
    //   1062: aload 35
    //   1064: invokevirtual 580	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   1067: checkcast 544	java/io/InputStream
    //   1070: astore_2
    //   1071: aload_2
    //   1072: astore 4
    //   1074: aload_2
    //   1075: ifnonnull +45 -> 1120
    //   1078: aload_2
    //   1079: astore 21
    //   1081: aload 35
    //   1083: astore 11
    //   1085: aload_2
    //   1086: astore 6
    //   1088: aload 35
    //   1090: astore 12
    //   1092: aload 30
    //   1094: astore 37
    //   1096: aload_2
    //   1097: astore_1
    //   1098: aload 16
    //   1100: astore 36
    //   1102: aload 35
    //   1104: astore 5
    //   1106: aload_3
    //   1107: astore 5
    //   1109: aload_0
    //   1110: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   1113: aload 35
    //   1115: invokevirtual 542	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   1118: astore 4
    //   1120: aload 4
    //   1122: astore 21
    //   1124: aload 35
    //   1126: astore 11
    //   1128: aload 4
    //   1130: astore 6
    //   1132: aload 35
    //   1134: astore 12
    //   1136: aload 30
    //   1138: astore 37
    //   1140: aload 4
    //   1142: astore_1
    //   1143: aload 16
    //   1145: astore 36
    //   1147: aload 35
    //   1149: astore 5
    //   1151: aload_3
    //   1152: astore 5
    //   1154: sipush 8192
    //   1157: newarray byte
    //   1159: astore 13
    //   1161: aload 4
    //   1163: astore 21
    //   1165: aload 35
    //   1167: astore 11
    //   1169: aload 4
    //   1171: astore 6
    //   1173: aload 35
    //   1175: astore 12
    //   1177: aload 30
    //   1179: astore 37
    //   1181: aload 4
    //   1183: astore_1
    //   1184: aload 16
    //   1186: astore 36
    //   1188: aload 35
    //   1190: astore 5
    //   1192: aload_3
    //   1193: astore 5
    //   1195: aload 4
    //   1197: aload 13
    //   1199: invokevirtual 548	java/io/InputStream:read	([B)I
    //   1202: istore 42
    //   1204: iload 42
    //   1206: iconst_m1
    //   1207: if_icmpeq +264 -> 1471
    //   1210: iload 39
    //   1212: ifne +50 -> 1262
    //   1215: aload 4
    //   1217: astore 21
    //   1219: aload 35
    //   1221: astore 11
    //   1223: aload 4
    //   1225: astore 6
    //   1227: aload 35
    //   1229: astore 12
    //   1231: aload 30
    //   1233: astore 37
    //   1235: aload 4
    //   1237: astore_1
    //   1238: aload 16
    //   1240: astore 36
    //   1242: aload 35
    //   1244: astore 5
    //   1246: aload_3
    //   1247: astore 5
    //   1249: aload 30
    //   1251: aload 13
    //   1253: iconst_0
    //   1254: iload 42
    //   1256: invokevirtual 710	java/io/OutputStream:write	([BII)V
    //   1259: goto -98 -> 1161
    //   1262: aload 4
    //   1264: astore 21
    //   1266: aload 35
    //   1268: astore 11
    //   1270: aload 4
    //   1272: astore 6
    //   1274: aload 35
    //   1276: astore 12
    //   1278: aload 30
    //   1280: astore 37
    //   1282: aload 4
    //   1284: astore_1
    //   1285: aload 16
    //   1287: astore 36
    //   1289: aload 35
    //   1291: astore 5
    //   1293: aload_3
    //   1294: astore 5
    //   1296: aload 16
    //   1298: aload 13
    //   1300: iload 42
    //   1302: invokevirtual 714	com/google/android/mms/util/DrmConvertSession:convert	([BI)[B
    //   1305: astore_2
    //   1306: aload_2
    //   1307: ifnull +49 -> 1356
    //   1310: aload 4
    //   1312: astore 21
    //   1314: aload 35
    //   1316: astore 11
    //   1318: aload 4
    //   1320: astore 6
    //   1322: aload 35
    //   1324: astore 12
    //   1326: aload 30
    //   1328: astore 37
    //   1330: aload 4
    //   1332: astore_1
    //   1333: aload 16
    //   1335: astore 36
    //   1337: aload 35
    //   1339: astore 5
    //   1341: aload_3
    //   1342: astore 5
    //   1344: aload 30
    //   1346: aload_2
    //   1347: iconst_0
    //   1348: aload_2
    //   1349: arraylength
    //   1350: invokevirtual 710	java/io/OutputStream:write	([BII)V
    //   1353: goto -192 -> 1161
    //   1356: aload 4
    //   1358: astore 21
    //   1360: aload 35
    //   1362: astore 11
    //   1364: aload 4
    //   1366: astore 6
    //   1368: aload 35
    //   1370: astore 12
    //   1372: aload 30
    //   1374: astore 37
    //   1376: aload 4
    //   1378: astore_1
    //   1379: aload 16
    //   1381: astore 36
    //   1383: aload 35
    //   1385: astore 5
    //   1387: aload_3
    //   1388: astore 5
    //   1390: new 472	com/google/android/mms/MmsException
    //   1393: astore_2
    //   1394: aload 4
    //   1396: astore 21
    //   1398: aload 35
    //   1400: astore 11
    //   1402: aload 4
    //   1404: astore 6
    //   1406: aload 35
    //   1408: astore 12
    //   1410: aload 30
    //   1412: astore 37
    //   1414: aload 4
    //   1416: astore_1
    //   1417: aload 16
    //   1419: astore 36
    //   1421: aload 35
    //   1423: astore 5
    //   1425: aload_3
    //   1426: astore 5
    //   1428: aload_2
    //   1429: ldc_w 716
    //   1432: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   1435: aload 4
    //   1437: astore 21
    //   1439: aload 35
    //   1441: astore 11
    //   1443: aload 4
    //   1445: astore 6
    //   1447: aload 35
    //   1449: astore 12
    //   1451: aload 30
    //   1453: astore 37
    //   1455: aload 4
    //   1457: astore_1
    //   1458: aload 16
    //   1460: astore 36
    //   1462: aload 35
    //   1464: astore 5
    //   1466: aload_3
    //   1467: astore 5
    //   1469: aload_2
    //   1470: athrow
    //   1471: aload 35
    //   1473: astore_2
    //   1474: goto +370 -> 1844
    //   1477: aload 8
    //   1479: astore 21
    //   1481: aload 35
    //   1483: astore 11
    //   1485: aload 9
    //   1487: astore 6
    //   1489: aload 35
    //   1491: astore 12
    //   1493: aload 30
    //   1495: astore 37
    //   1497: aload 13
    //   1499: astore_1
    //   1500: aload 16
    //   1502: astore 36
    //   1504: aload 35
    //   1506: astore 5
    //   1508: aload_3
    //   1509: astore 5
    //   1511: ldc 103
    //   1513: ldc_w 718
    //   1516: invokestatic 721	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1519: pop
    //   1520: aload 30
    //   1522: ifnull +46 -> 1568
    //   1525: aload 30
    //   1527: invokevirtual 722	java/io/OutputStream:close	()V
    //   1530: goto +38 -> 1568
    //   1533: astore_2
    //   1534: new 409	java/lang/StringBuilder
    //   1537: dup
    //   1538: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   1541: astore_1
    //   1542: aload_1
    //   1543: ldc_w 680
    //   1546: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1549: pop
    //   1550: aload_1
    //   1551: aload 30
    //   1553: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1556: pop
    //   1557: ldc 103
    //   1559: aload_1
    //   1560: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1563: aload_2
    //   1564: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1567: pop
    //   1568: iconst_0
    //   1569: ifeq +45 -> 1614
    //   1572: new 677	java/lang/NullPointerException
    //   1575: dup
    //   1576: invokespecial 678	java/lang/NullPointerException:<init>	()V
    //   1579: athrow
    //   1580: astore_2
    //   1581: new 409	java/lang/StringBuilder
    //   1584: dup
    //   1585: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   1588: astore_1
    //   1589: aload_1
    //   1590: ldc_w 680
    //   1593: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1596: pop
    //   1597: aload_1
    //   1598: aconst_null
    //   1599: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1602: pop
    //   1603: ldc 103
    //   1605: aload_1
    //   1606: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1609: aload_2
    //   1610: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1613: pop
    //   1614: aload 16
    //   1616: ifnull +85 -> 1701
    //   1619: aload 16
    //   1621: aload_3
    //   1622: invokevirtual 724	com/google/android/mms/util/DrmConvertSession:close	(Ljava/lang/String;)I
    //   1625: pop
    //   1626: new 670	java/io/File
    //   1629: dup
    //   1630: aload_3
    //   1631: invokespecial 671	java/io/File:<init>	(Ljava/lang/String;)V
    //   1634: astore_3
    //   1635: new 634	android/content/ContentValues
    //   1638: dup
    //   1639: iconst_0
    //   1640: invokespecial 636	android/content/ContentValues:<init>	(I)V
    //   1643: astore_1
    //   1644: aload_0
    //   1645: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   1648: astore_2
    //   1649: aload_0
    //   1650: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   1653: astore 4
    //   1655: new 409	java/lang/StringBuilder
    //   1658: dup
    //   1659: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   1662: astore 16
    //   1664: aload 16
    //   1666: ldc_w 726
    //   1669: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1672: pop
    //   1673: aload 16
    //   1675: aload_3
    //   1676: invokevirtual 729	java/io/File:getName	()Ljava/lang/String;
    //   1679: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1682: pop
    //   1683: aload_2
    //   1684: aload 4
    //   1686: aload 16
    //   1688: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1691: invokestatic 426	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   1694: aload_1
    //   1695: aconst_null
    //   1696: aconst_null
    //   1697: invokestatic 733	com/google/android/mms/util/SqliteWrapper:update	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   1700: pop
    //   1701: return
    //   1702: astore_1
    //   1703: aload 14
    //   1705: astore_2
    //   1706: goto +1272 -> 2978
    //   1709: astore 4
    //   1711: aconst_null
    //   1712: astore_2
    //   1713: aload 11
    //   1715: astore 21
    //   1717: goto +1035 -> 2752
    //   1720: astore 4
    //   1722: aconst_null
    //   1723: astore_2
    //   1724: aload 12
    //   1726: astore 6
    //   1728: goto +1138 -> 2866
    //   1731: iload 39
    //   1733: ifne +17 -> 1750
    //   1736: aload 30
    //   1738: aload 38
    //   1740: invokevirtual 735	java/io/OutputStream:write	([B)V
    //   1743: aload 10
    //   1745: astore 4
    //   1747: goto +527 -> 2274
    //   1750: aload 8
    //   1752: astore 21
    //   1754: aload_2
    //   1755: astore 11
    //   1757: aload 9
    //   1759: astore 6
    //   1761: aload_2
    //   1762: astore 12
    //   1764: aload 30
    //   1766: astore 37
    //   1768: aload 13
    //   1770: astore_1
    //   1771: aload 16
    //   1773: astore 36
    //   1775: aload_2
    //   1776: astore 5
    //   1778: aload_3
    //   1779: astore 5
    //   1781: aload 16
    //   1783: aload 38
    //   1785: aload 38
    //   1787: arraylength
    //   1788: invokevirtual 714	com/google/android/mms/util/DrmConvertSession:convert	([BI)[B
    //   1791: astore 4
    //   1793: aload 4
    //   1795: ifnull +52 -> 1847
    //   1798: aload 8
    //   1800: astore 21
    //   1802: aload_2
    //   1803: astore 11
    //   1805: aload 9
    //   1807: astore 6
    //   1809: aload_2
    //   1810: astore 12
    //   1812: aload 30
    //   1814: astore 37
    //   1816: aload 13
    //   1818: astore_1
    //   1819: aload 16
    //   1821: astore 36
    //   1823: aload_2
    //   1824: astore 5
    //   1826: aload_3
    //   1827: astore 5
    //   1829: aload 30
    //   1831: aload 4
    //   1833: iconst_0
    //   1834: aload 4
    //   1836: arraylength
    //   1837: invokevirtual 710	java/io/OutputStream:write	([BII)V
    //   1840: aload 7
    //   1842: astore 4
    //   1844: goto +430 -> 2274
    //   1847: aload 8
    //   1849: astore 21
    //   1851: aload_2
    //   1852: astore 11
    //   1854: aload 9
    //   1856: astore 6
    //   1858: aload_2
    //   1859: astore 12
    //   1861: aload 30
    //   1863: astore 37
    //   1865: aload 13
    //   1867: astore_1
    //   1868: aload 16
    //   1870: astore 36
    //   1872: aload_2
    //   1873: astore 5
    //   1875: aload_3
    //   1876: astore 5
    //   1878: new 472	com/google/android/mms/MmsException
    //   1881: astore 4
    //   1883: aload 8
    //   1885: astore 21
    //   1887: aload_2
    //   1888: astore 11
    //   1890: aload 9
    //   1892: astore 6
    //   1894: aload_2
    //   1895: astore 12
    //   1897: aload 30
    //   1899: astore 37
    //   1901: aload 13
    //   1903: astore_1
    //   1904: aload 16
    //   1906: astore 36
    //   1908: aload_2
    //   1909: astore 5
    //   1911: aload_3
    //   1912: astore 5
    //   1914: aload 4
    //   1916: ldc_w 716
    //   1919: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   1922: aload 8
    //   1924: astore 21
    //   1926: aload_2
    //   1927: astore 11
    //   1929: aload 9
    //   1931: astore 6
    //   1933: aload_2
    //   1934: astore 12
    //   1936: aload 30
    //   1938: astore 37
    //   1940: aload 13
    //   1942: astore_1
    //   1943: aload 16
    //   1945: astore 36
    //   1947: aload_2
    //   1948: astore 5
    //   1950: aload_3
    //   1951: astore 5
    //   1953: aload 4
    //   1955: athrow
    //   1956: astore 4
    //   1958: aload 11
    //   1960: astore_2
    //   1961: goto +791 -> 2752
    //   1964: astore 4
    //   1966: aload 12
    //   1968: astore_2
    //   1969: goto +897 -> 2866
    //   1972: aload 17
    //   1974: astore 6
    //   1976: aload 28
    //   1978: astore 37
    //   1980: aload 18
    //   1982: astore 5
    //   1984: aload 29
    //   1986: astore 35
    //   1988: aload 19
    //   1990: astore 21
    //   1992: aload 30
    //   1994: astore 36
    //   1996: new 634	android/content/ContentValues
    //   1999: astore_3
    //   2000: aload 17
    //   2002: astore 6
    //   2004: aload 28
    //   2006: astore 37
    //   2008: aload 18
    //   2010: astore 5
    //   2012: aload 29
    //   2014: astore 35
    //   2016: aload 19
    //   2018: astore 21
    //   2020: aload 30
    //   2022: astore 36
    //   2024: aload_3
    //   2025: invokespecial 736	android/content/ContentValues:<init>	()V
    //   2028: aload 38
    //   2030: astore_1
    //   2031: aload 38
    //   2033: ifnonnull +94 -> 2127
    //   2036: aload 17
    //   2038: astore 6
    //   2040: aload 28
    //   2042: astore 37
    //   2044: aload 18
    //   2046: astore 5
    //   2048: aload 29
    //   2050: astore 35
    //   2052: aload 19
    //   2054: astore 21
    //   2056: aload 30
    //   2058: astore 36
    //   2060: new 124	java/lang/String
    //   2063: astore_1
    //   2064: aload 17
    //   2066: astore 6
    //   2068: aload 28
    //   2070: astore 37
    //   2072: aload 18
    //   2074: astore 5
    //   2076: aload 29
    //   2078: astore 35
    //   2080: aload 19
    //   2082: astore 21
    //   2084: aload 30
    //   2086: astore 36
    //   2088: aload_1
    //   2089: ldc_w 299
    //   2092: invokespecial 737	java/lang/String:<init>	(Ljava/lang/String;)V
    //   2095: aload 17
    //   2097: astore 6
    //   2099: aload 28
    //   2101: astore 37
    //   2103: aload 18
    //   2105: astore 5
    //   2107: aload 29
    //   2109: astore 35
    //   2111: aload 19
    //   2113: astore 21
    //   2115: aload 30
    //   2117: astore 36
    //   2119: aload_1
    //   2120: ldc_w 739
    //   2123: invokevirtual 370	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   2126: astore_1
    //   2127: aload 17
    //   2129: astore 6
    //   2131: aload 28
    //   2133: astore 37
    //   2135: aload 18
    //   2137: astore 5
    //   2139: aload 29
    //   2141: astore 35
    //   2143: aload 19
    //   2145: astore 21
    //   2147: aload 30
    //   2149: astore 36
    //   2151: new 456	com/google/android/mms/pdu/EncodedStringValue
    //   2154: astore 4
    //   2156: aload 17
    //   2158: astore 6
    //   2160: aload 28
    //   2162: astore 37
    //   2164: aload 18
    //   2166: astore 5
    //   2168: aload 29
    //   2170: astore 35
    //   2172: aload 19
    //   2174: astore 21
    //   2176: aload 30
    //   2178: astore 36
    //   2180: aload 4
    //   2182: aload_1
    //   2183: invokespecial 741	com/google/android/mms/pdu/EncodedStringValue:<init>	([B)V
    //   2186: aload 17
    //   2188: astore 6
    //   2190: aload 28
    //   2192: astore 37
    //   2194: aload 18
    //   2196: astore 5
    //   2198: aload 29
    //   2200: astore 35
    //   2202: aload 19
    //   2204: astore 21
    //   2206: aload 30
    //   2208: astore 36
    //   2210: aload_3
    //   2211: ldc -60
    //   2213: aload 4
    //   2215: invokevirtual 605	com/google/android/mms/pdu/EncodedStringValue:getString	()Ljava/lang/String;
    //   2218: invokevirtual 642	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   2221: aload 17
    //   2223: astore 6
    //   2225: aload 28
    //   2227: astore 37
    //   2229: aload 18
    //   2231: astore 5
    //   2233: aload 29
    //   2235: astore 35
    //   2237: aload 19
    //   2239: astore 21
    //   2241: aload 30
    //   2243: astore 36
    //   2245: aload_0
    //   2246: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   2249: aload_2
    //   2250: aload_3
    //   2251: aconst_null
    //   2252: aconst_null
    //   2253: invokevirtual 744	android/content/ContentResolver:update	(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   2256: istore 42
    //   2258: iload 42
    //   2260: iconst_1
    //   2261: if_icmpne +195 -> 2456
    //   2264: aconst_null
    //   2265: astore 30
    //   2267: aload 27
    //   2269: astore_3
    //   2270: aload 10
    //   2272: astore 4
    //   2274: aload 30
    //   2276: ifnull +46 -> 2322
    //   2279: aload 30
    //   2281: invokevirtual 722	java/io/OutputStream:close	()V
    //   2284: goto +38 -> 2322
    //   2287: astore_1
    //   2288: new 409	java/lang/StringBuilder
    //   2291: dup
    //   2292: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   2295: astore_2
    //   2296: aload_2
    //   2297: ldc_w 680
    //   2300: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2303: pop
    //   2304: aload_2
    //   2305: aload 30
    //   2307: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2310: pop
    //   2311: ldc 103
    //   2313: aload_2
    //   2314: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2317: aload_1
    //   2318: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2321: pop
    //   2322: aload 4
    //   2324: ifnull +46 -> 2370
    //   2327: aload 4
    //   2329: invokevirtual 553	java/io/InputStream:close	()V
    //   2332: goto +38 -> 2370
    //   2335: astore_2
    //   2336: new 409	java/lang/StringBuilder
    //   2339: dup
    //   2340: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   2343: astore_1
    //   2344: aload_1
    //   2345: ldc_w 680
    //   2348: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2351: pop
    //   2352: aload_1
    //   2353: aload 4
    //   2355: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2358: pop
    //   2359: ldc 103
    //   2361: aload_1
    //   2362: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2365: aload_2
    //   2366: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2369: pop
    //   2370: aload 16
    //   2372: ifnull +83 -> 2455
    //   2375: aload 16
    //   2377: aload_3
    //   2378: invokevirtual 724	com/google/android/mms/util/DrmConvertSession:close	(Ljava/lang/String;)I
    //   2381: pop
    //   2382: new 670	java/io/File
    //   2385: dup
    //   2386: aload_3
    //   2387: invokespecial 671	java/io/File:<init>	(Ljava/lang/String;)V
    //   2390: astore 4
    //   2392: new 634	android/content/ContentValues
    //   2395: dup
    //   2396: iconst_0
    //   2397: invokespecial 636	android/content/ContentValues:<init>	(I)V
    //   2400: astore_3
    //   2401: aload_0
    //   2402: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   2405: astore 16
    //   2407: aload_0
    //   2408: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   2411: astore_1
    //   2412: new 409	java/lang/StringBuilder
    //   2415: dup
    //   2416: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   2419: astore_2
    //   2420: aload_2
    //   2421: ldc_w 726
    //   2424: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2427: pop
    //   2428: aload_2
    //   2429: aload 4
    //   2431: invokevirtual 729	java/io/File:getName	()Ljava/lang/String;
    //   2434: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2437: pop
    //   2438: aload 16
    //   2440: aload_1
    //   2441: aload_2
    //   2442: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2445: invokestatic 426	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   2448: aload_3
    //   2449: aconst_null
    //   2450: aconst_null
    //   2451: invokestatic 733	com/google/android/mms/util/SqliteWrapper:update	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   2454: pop
    //   2455: return
    //   2456: aload 17
    //   2458: astore 6
    //   2460: aload 28
    //   2462: astore 37
    //   2464: aload 18
    //   2466: astore 5
    //   2468: aload 29
    //   2470: astore 35
    //   2472: aload 19
    //   2474: astore 21
    //   2476: aload 30
    //   2478: astore 36
    //   2480: new 472	com/google/android/mms/MmsException
    //   2483: astore_3
    //   2484: aload 17
    //   2486: astore 6
    //   2488: aload 28
    //   2490: astore 37
    //   2492: aload 18
    //   2494: astore 5
    //   2496: aload 29
    //   2498: astore 35
    //   2500: aload 19
    //   2502: astore 21
    //   2504: aload 30
    //   2506: astore 36
    //   2508: new 409	java/lang/StringBuilder
    //   2511: astore_1
    //   2512: aload 17
    //   2514: astore 6
    //   2516: aload 28
    //   2518: astore 37
    //   2520: aload 18
    //   2522: astore 5
    //   2524: aload 29
    //   2526: astore 35
    //   2528: aload 19
    //   2530: astore 21
    //   2532: aload 30
    //   2534: astore 36
    //   2536: aload_1
    //   2537: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   2540: aload 17
    //   2542: astore 6
    //   2544: aload 28
    //   2546: astore 37
    //   2548: aload 18
    //   2550: astore 5
    //   2552: aload 29
    //   2554: astore 35
    //   2556: aload 19
    //   2558: astore 21
    //   2560: aload 30
    //   2562: astore 36
    //   2564: aload_1
    //   2565: ldc_w 746
    //   2568: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2571: pop
    //   2572: aload 17
    //   2574: astore 6
    //   2576: aload 28
    //   2578: astore 37
    //   2580: aload 18
    //   2582: astore 5
    //   2584: aload 29
    //   2586: astore 35
    //   2588: aload 19
    //   2590: astore 21
    //   2592: aload 30
    //   2594: astore 36
    //   2596: aload_1
    //   2597: aload_2
    //   2598: invokevirtual 310	android/net/Uri:toString	()Ljava/lang/String;
    //   2601: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2604: pop
    //   2605: aload 17
    //   2607: astore 6
    //   2609: aload 28
    //   2611: astore 37
    //   2613: aload 18
    //   2615: astore 5
    //   2617: aload 29
    //   2619: astore 35
    //   2621: aload 19
    //   2623: astore 21
    //   2625: aload 30
    //   2627: astore 36
    //   2629: aload_3
    //   2630: aload_1
    //   2631: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2634: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   2637: aload 17
    //   2639: astore 6
    //   2641: aload 28
    //   2643: astore 37
    //   2645: aload 18
    //   2647: astore 5
    //   2649: aload 29
    //   2651: astore 35
    //   2653: aload 19
    //   2655: astore 21
    //   2657: aload 30
    //   2659: astore 36
    //   2661: aload_3
    //   2662: athrow
    //   2663: astore_1
    //   2664: aload 6
    //   2666: astore_3
    //   2667: aconst_null
    //   2668: astore_2
    //   2669: aconst_null
    //   2670: astore 4
    //   2672: aload 37
    //   2674: astore 35
    //   2676: goto +312 -> 2988
    //   2679: astore 4
    //   2681: aconst_null
    //   2682: astore_2
    //   2683: aconst_null
    //   2684: astore 30
    //   2686: aload 11
    //   2688: astore 21
    //   2690: aload 5
    //   2692: astore 16
    //   2694: aload 35
    //   2696: astore_3
    //   2697: goto +55 -> 2752
    //   2700: astore 4
    //   2702: aconst_null
    //   2703: astore_2
    //   2704: aconst_null
    //   2705: astore 30
    //   2707: aload 12
    //   2709: astore 6
    //   2711: aload 21
    //   2713: astore 16
    //   2715: aload 36
    //   2717: astore_3
    //   2718: goto +148 -> 2866
    //   2721: astore_1
    //   2722: aconst_null
    //   2723: astore_3
    //   2724: aconst_null
    //   2725: astore_2
    //   2726: aconst_null
    //   2727: astore 4
    //   2729: goto +259 -> 2988
    //   2732: astore 4
    //   2734: aload 36
    //   2736: astore_3
    //   2737: aload 24
    //   2739: astore_2
    //   2740: aload 20
    //   2742: astore 16
    //   2744: aload 11
    //   2746: astore 21
    //   2748: aload 6
    //   2750: astore 30
    //   2752: aload 30
    //   2754: astore 37
    //   2756: aload 21
    //   2758: astore_1
    //   2759: aload 16
    //   2761: astore 36
    //   2763: aload_2
    //   2764: astore 5
    //   2766: aload_3
    //   2767: astore 5
    //   2769: ldc 103
    //   2771: ldc_w 748
    //   2774: aload 4
    //   2776: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2779: pop
    //   2780: aload 30
    //   2782: astore 37
    //   2784: aload 21
    //   2786: astore_1
    //   2787: aload 16
    //   2789: astore 36
    //   2791: aload_2
    //   2792: astore 5
    //   2794: aload_3
    //   2795: astore 5
    //   2797: new 472	com/google/android/mms/MmsException
    //   2800: astore 35
    //   2802: aload 30
    //   2804: astore 37
    //   2806: aload 21
    //   2808: astore_1
    //   2809: aload 16
    //   2811: astore 36
    //   2813: aload_2
    //   2814: astore 5
    //   2816: aload_3
    //   2817: astore 5
    //   2819: aload 35
    //   2821: aload 4
    //   2823: invokespecial 560	com/google/android/mms/MmsException:<init>	(Ljava/lang/Throwable;)V
    //   2826: aload 30
    //   2828: astore 37
    //   2830: aload 21
    //   2832: astore_1
    //   2833: aload 16
    //   2835: astore 36
    //   2837: aload_2
    //   2838: astore 5
    //   2840: aload_3
    //   2841: astore 5
    //   2843: aload 35
    //   2845: athrow
    //   2846: astore 4
    //   2848: aload 37
    //   2850: astore_3
    //   2851: aload 23
    //   2853: astore_2
    //   2854: aload 21
    //   2856: astore 16
    //   2858: aload 12
    //   2860: astore 6
    //   2862: aload 5
    //   2864: astore 30
    //   2866: aload 30
    //   2868: astore 37
    //   2870: aload 6
    //   2872: astore_1
    //   2873: aload 16
    //   2875: astore 36
    //   2877: aload_2
    //   2878: astore 5
    //   2880: aload_3
    //   2881: astore 5
    //   2883: ldc 103
    //   2885: ldc_w 750
    //   2888: aload 4
    //   2890: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   2893: pop
    //   2894: aload 30
    //   2896: astore 37
    //   2898: aload 6
    //   2900: astore_1
    //   2901: aload 16
    //   2903: astore 36
    //   2905: aload_2
    //   2906: astore 5
    //   2908: aload_3
    //   2909: astore 5
    //   2911: new 472	com/google/android/mms/MmsException
    //   2914: astore 35
    //   2916: aload 30
    //   2918: astore 37
    //   2920: aload 6
    //   2922: astore_1
    //   2923: aload 16
    //   2925: astore 36
    //   2927: aload_2
    //   2928: astore 5
    //   2930: aload_3
    //   2931: astore 5
    //   2933: aload 35
    //   2935: aload 4
    //   2937: invokespecial 560	com/google/android/mms/MmsException:<init>	(Ljava/lang/Throwable;)V
    //   2940: aload 30
    //   2942: astore 37
    //   2944: aload 6
    //   2946: astore_1
    //   2947: aload 16
    //   2949: astore 36
    //   2951: aload_2
    //   2952: astore 5
    //   2954: aload_3
    //   2955: astore 5
    //   2957: aload 35
    //   2959: athrow
    //   2960: astore 4
    //   2962: aload 5
    //   2964: astore_3
    //   2965: aload 36
    //   2967: astore 16
    //   2969: aload_1
    //   2970: astore_2
    //   2971: aload 37
    //   2973: astore 30
    //   2975: aload 4
    //   2977: astore_1
    //   2978: aload 30
    //   2980: astore 4
    //   2982: aload_3
    //   2983: astore 35
    //   2985: aload 16
    //   2987: astore_3
    //   2988: aload 4
    //   2990: ifnull +52 -> 3042
    //   2993: aload 4
    //   2995: invokevirtual 722	java/io/OutputStream:close	()V
    //   2998: goto +44 -> 3042
    //   3001: astore 30
    //   3003: new 409	java/lang/StringBuilder
    //   3006: dup
    //   3007: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   3010: astore 16
    //   3012: aload 16
    //   3014: ldc_w 680
    //   3017: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3020: pop
    //   3021: aload 16
    //   3023: aload 4
    //   3025: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   3028: pop
    //   3029: ldc 103
    //   3031: aload 16
    //   3033: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3036: aload 30
    //   3038: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   3041: pop
    //   3042: aload_2
    //   3043: ifnull +50 -> 3093
    //   3046: aload_2
    //   3047: invokevirtual 553	java/io/InputStream:close	()V
    //   3050: goto +43 -> 3093
    //   3053: astore 4
    //   3055: new 409	java/lang/StringBuilder
    //   3058: dup
    //   3059: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   3062: astore 16
    //   3064: aload 16
    //   3066: ldc_w 680
    //   3069: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3072: pop
    //   3073: aload 16
    //   3075: aload_2
    //   3076: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   3079: pop
    //   3080: ldc 103
    //   3082: aload 16
    //   3084: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3087: aload 4
    //   3089: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   3092: pop
    //   3093: aload_3
    //   3094: ifnull +86 -> 3180
    //   3097: aload_3
    //   3098: aload 35
    //   3100: invokevirtual 724	com/google/android/mms/util/DrmConvertSession:close	(Ljava/lang/String;)I
    //   3103: pop
    //   3104: new 670	java/io/File
    //   3107: dup
    //   3108: aload 35
    //   3110: invokespecial 671	java/io/File:<init>	(Ljava/lang/String;)V
    //   3113: astore_2
    //   3114: new 634	android/content/ContentValues
    //   3117: dup
    //   3118: iconst_0
    //   3119: invokespecial 636	android/content/ContentValues:<init>	(I)V
    //   3122: astore 30
    //   3124: aload_0
    //   3125: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   3128: astore 16
    //   3130: aload_0
    //   3131: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   3134: astore 4
    //   3136: new 409	java/lang/StringBuilder
    //   3139: dup
    //   3140: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   3143: astore_3
    //   3144: aload_3
    //   3145: ldc_w 726
    //   3148: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3151: pop
    //   3152: aload_3
    //   3153: aload_2
    //   3154: invokevirtual 729	java/io/File:getName	()Ljava/lang/String;
    //   3157: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3160: pop
    //   3161: aload 16
    //   3163: aload 4
    //   3165: aload_3
    //   3166: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3169: invokestatic 426	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   3172: aload 30
    //   3174: aconst_null
    //   3175: aconst_null
    //   3176: invokestatic 733	com/google/android/mms/util/SqliteWrapper:update	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   3179: pop
    //   3180: aload_1
    //   3181: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	3182	0	this	PduPersister
    //   0	3182	1	paramPduPart	PduPart
    //   0	3182	2	paramUri	Uri
    //   0	3182	3	paramString	String
    //   0	3182	4	paramHashMap	HashMap<Uri, InputStream>
    //   1	2962	5	localObject1	Object
    //   4	2941	6	localObject2	Object
    //   7	1834	7	localObject3	Object
    //   10	1913	8	localObject4	Object
    //   13	1917	9	localObject5	Object
    //   16	2255	10	localObject6	Object
    //   19	2726	11	localObject7	Object
    //   22	2837	12	localObject8	Object
    //   25	1916	13	arrayOfByte1	byte[]
    //   28	1676	14	localObject9	Object
    //   31	949	15	localObject10	Object
    //   34	379	16	str	String
    //   419	159	16	localException	Exception
    //   622	2540	16	localObject11	Object
    //   37	2601	17	localObject12	Object
    //   40	2606	18	localObject13	Object
    //   43	2611	19	localObject14	Object
    //   46	2695	20	localObject15	Object
    //   49	2806	21	localObject16	Object
    //   52	823	22	localObject17	Object
    //   55	2797	23	localObject18	Object
    //   58	2680	24	localObject19	Object
    //   61	526	25	localObject20	Object
    //   64	808	26	localObject21	Object
    //   67	2201	27	localObject22	Object
    //   70	2572	28	localObject23	Object
    //   73	2577	29	localObject24	Object
    //   76	2903	30	localObject25	Object
    //   3001	36	30	localIOException	java.io.IOException
    //   3122	51	30	localContentValues	ContentValues
    //   79	130	31	localObject26	Object
    //   82	131	32	localObject27	Object
    //   85	132	33	localObject28	Object
    //   88	117	34	localObject29	Object
    //   92	3017	35	localObject30	Object
    //   96	2870	36	localObject31	Object
    //   100	2872	37	localObject32	Object
    //   106	1926	38	arrayOfByte2	byte[]
    //   193	1539	39	bool	boolean
    //   295	3	40	l	long
    //   1202	1060	42	i	int
    // Exception table:
    //   from	to	target	type
    //   308	316	316	java/io/IOException
    //   357	365	365	java/io/IOException
    //   220	230	419	java/lang/Exception
    //   246	251	419	java/lang/Exception
    //   267	274	419	java/lang/Exception
    //   290	297	419	java/lang/Exception
    //   1525	1530	1533	java/io/IOException
    //   1572	1580	1580	java/io/IOException
    //   914	920	1702	finally
    //   1736	1743	1702	finally
    //   914	920	1709	java/io/IOException
    //   1736	1743	1709	java/io/IOException
    //   914	920	1720	java/io/FileNotFoundException
    //   1736	1743	1720	java/io/FileNotFoundException
    //   959	968	1956	java/io/IOException
    //   1016	1026	1956	java/io/IOException
    //   1060	1071	1956	java/io/IOException
    //   1109	1120	1956	java/io/IOException
    //   1154	1161	1956	java/io/IOException
    //   1195	1204	1956	java/io/IOException
    //   1249	1259	1956	java/io/IOException
    //   1296	1306	1956	java/io/IOException
    //   1344	1353	1956	java/io/IOException
    //   1390	1394	1956	java/io/IOException
    //   1428	1435	1956	java/io/IOException
    //   1469	1471	1956	java/io/IOException
    //   1511	1520	1956	java/io/IOException
    //   1781	1793	1956	java/io/IOException
    //   1829	1840	1956	java/io/IOException
    //   1878	1883	1956	java/io/IOException
    //   1914	1922	1956	java/io/IOException
    //   1953	1956	1956	java/io/IOException
    //   959	968	1964	java/io/FileNotFoundException
    //   1016	1026	1964	java/io/FileNotFoundException
    //   1060	1071	1964	java/io/FileNotFoundException
    //   1109	1120	1964	java/io/FileNotFoundException
    //   1154	1161	1964	java/io/FileNotFoundException
    //   1195	1204	1964	java/io/FileNotFoundException
    //   1249	1259	1964	java/io/FileNotFoundException
    //   1296	1306	1964	java/io/FileNotFoundException
    //   1344	1353	1964	java/io/FileNotFoundException
    //   1390	1394	1964	java/io/FileNotFoundException
    //   1428	1435	1964	java/io/FileNotFoundException
    //   1469	1471	1964	java/io/FileNotFoundException
    //   1511	1520	1964	java/io/FileNotFoundException
    //   1781	1793	1964	java/io/FileNotFoundException
    //   1829	1840	1964	java/io/FileNotFoundException
    //   1878	1883	1964	java/io/FileNotFoundException
    //   1914	1922	1964	java/io/FileNotFoundException
    //   1953	1956	1964	java/io/FileNotFoundException
    //   2279	2284	2287	java/io/IOException
    //   2327	2332	2335	java/io/IOException
    //   445	450	2663	finally
    //   474	479	2663	finally
    //   503	512	2663	finally
    //   536	546	2663	finally
    //   570	583	2663	finally
    //   614	624	2663	finally
    //   659	663	2663	finally
    //   687	691	2663	finally
    //   715	719	2663	finally
    //   743	751	2663	finally
    //   775	781	2663	finally
    //   805	813	2663	finally
    //   837	845	2663	finally
    //   869	871	2663	finally
    //   899	909	2663	finally
    //   1996	2000	2663	finally
    //   2024	2028	2663	finally
    //   2060	2064	2663	finally
    //   2088	2095	2663	finally
    //   2119	2127	2663	finally
    //   2151	2156	2663	finally
    //   2180	2186	2663	finally
    //   2210	2221	2663	finally
    //   2245	2258	2663	finally
    //   2480	2484	2663	finally
    //   2508	2512	2663	finally
    //   2536	2540	2663	finally
    //   2564	2572	2663	finally
    //   2596	2605	2663	finally
    //   2629	2637	2663	finally
    //   2661	2663	2663	finally
    //   445	450	2679	java/io/IOException
    //   474	479	2679	java/io/IOException
    //   503	512	2679	java/io/IOException
    //   536	546	2679	java/io/IOException
    //   570	583	2679	java/io/IOException
    //   614	624	2679	java/io/IOException
    //   659	663	2679	java/io/IOException
    //   687	691	2679	java/io/IOException
    //   715	719	2679	java/io/IOException
    //   743	751	2679	java/io/IOException
    //   775	781	2679	java/io/IOException
    //   805	813	2679	java/io/IOException
    //   837	845	2679	java/io/IOException
    //   869	871	2679	java/io/IOException
    //   899	909	2679	java/io/IOException
    //   1996	2000	2679	java/io/IOException
    //   2024	2028	2679	java/io/IOException
    //   2060	2064	2679	java/io/IOException
    //   2088	2095	2679	java/io/IOException
    //   2119	2127	2679	java/io/IOException
    //   2151	2156	2679	java/io/IOException
    //   2180	2186	2679	java/io/IOException
    //   2210	2221	2679	java/io/IOException
    //   2245	2258	2679	java/io/IOException
    //   2480	2484	2679	java/io/IOException
    //   2508	2512	2679	java/io/IOException
    //   2536	2540	2679	java/io/IOException
    //   2564	2572	2679	java/io/IOException
    //   2596	2605	2679	java/io/IOException
    //   2629	2637	2679	java/io/IOException
    //   2661	2663	2679	java/io/IOException
    //   445	450	2700	java/io/FileNotFoundException
    //   474	479	2700	java/io/FileNotFoundException
    //   503	512	2700	java/io/FileNotFoundException
    //   536	546	2700	java/io/FileNotFoundException
    //   570	583	2700	java/io/FileNotFoundException
    //   614	624	2700	java/io/FileNotFoundException
    //   659	663	2700	java/io/FileNotFoundException
    //   687	691	2700	java/io/FileNotFoundException
    //   715	719	2700	java/io/FileNotFoundException
    //   743	751	2700	java/io/FileNotFoundException
    //   775	781	2700	java/io/FileNotFoundException
    //   805	813	2700	java/io/FileNotFoundException
    //   837	845	2700	java/io/FileNotFoundException
    //   869	871	2700	java/io/FileNotFoundException
    //   899	909	2700	java/io/FileNotFoundException
    //   1996	2000	2700	java/io/FileNotFoundException
    //   2024	2028	2700	java/io/FileNotFoundException
    //   2060	2064	2700	java/io/FileNotFoundException
    //   2088	2095	2700	java/io/FileNotFoundException
    //   2119	2127	2700	java/io/FileNotFoundException
    //   2151	2156	2700	java/io/FileNotFoundException
    //   2180	2186	2700	java/io/FileNotFoundException
    //   2210	2221	2700	java/io/FileNotFoundException
    //   2245	2258	2700	java/io/FileNotFoundException
    //   2480	2484	2700	java/io/FileNotFoundException
    //   2508	2512	2700	java/io/FileNotFoundException
    //   2536	2540	2700	java/io/FileNotFoundException
    //   2564	2572	2700	java/io/FileNotFoundException
    //   2596	2605	2700	java/io/FileNotFoundException
    //   2629	2637	2700	java/io/FileNotFoundException
    //   2661	2663	2700	java/io/FileNotFoundException
    //   102	108	2721	finally
    //   120	130	2721	finally
    //   142	152	2721	finally
    //   164	174	2721	finally
    //   189	195	2721	finally
    //   220	230	2721	finally
    //   246	251	2721	finally
    //   267	274	2721	finally
    //   290	297	2721	finally
    //   102	108	2732	java/io/IOException
    //   120	130	2732	java/io/IOException
    //   142	152	2732	java/io/IOException
    //   164	174	2732	java/io/IOException
    //   189	195	2732	java/io/IOException
    //   220	230	2732	java/io/IOException
    //   246	251	2732	java/io/IOException
    //   267	274	2732	java/io/IOException
    //   290	297	2732	java/io/IOException
    //   102	108	2846	java/io/FileNotFoundException
    //   120	130	2846	java/io/FileNotFoundException
    //   142	152	2846	java/io/FileNotFoundException
    //   164	174	2846	java/io/FileNotFoundException
    //   189	195	2846	java/io/FileNotFoundException
    //   220	230	2846	java/io/FileNotFoundException
    //   246	251	2846	java/io/FileNotFoundException
    //   267	274	2846	java/io/FileNotFoundException
    //   290	297	2846	java/io/FileNotFoundException
    //   959	968	2960	finally
    //   1016	1026	2960	finally
    //   1060	1071	2960	finally
    //   1109	1120	2960	finally
    //   1154	1161	2960	finally
    //   1195	1204	2960	finally
    //   1249	1259	2960	finally
    //   1296	1306	2960	finally
    //   1344	1353	2960	finally
    //   1390	1394	2960	finally
    //   1428	1435	2960	finally
    //   1469	1471	2960	finally
    //   1511	1520	2960	finally
    //   1781	1793	2960	finally
    //   1829	1840	2960	finally
    //   1878	1883	2960	finally
    //   1914	1922	2960	finally
    //   1953	1956	2960	finally
    //   2769	2780	2960	finally
    //   2797	2802	2960	finally
    //   2819	2826	2960	finally
    //   2843	2846	2960	finally
    //   2883	2894	2960	finally
    //   2911	2916	2960	finally
    //   2933	2940	2960	finally
    //   2957	2960	2960	finally
    //   2993	2998	3001	java/io/IOException
    //   3046	3050	3053	java/io/IOException
  }
  
  private void setEncodedStringValueToHeaders(Cursor paramCursor, int paramInt1, PduHeaders paramPduHeaders, int paramInt2)
  {
    String str = paramCursor.getString(paramInt1);
    if ((str != null) && (str.length() > 0)) {
      paramPduHeaders.setEncodedStringValue(new EncodedStringValue(paramCursor.getInt(((Integer)CHARSET_COLUMN_INDEX_MAP.get(Integer.valueOf(paramInt2))).intValue()), getBytes(str)), paramInt2);
    }
  }
  
  private void setLongToHeaders(Cursor paramCursor, int paramInt1, PduHeaders paramPduHeaders, int paramInt2)
  {
    if (!paramCursor.isNull(paramInt1)) {
      paramPduHeaders.setLongInteger(paramCursor.getLong(paramInt1), paramInt2);
    }
  }
  
  private void setOctetToHeaders(Cursor paramCursor, int paramInt1, PduHeaders paramPduHeaders, int paramInt2)
    throws InvalidHeaderValueException
  {
    if (!paramCursor.isNull(paramInt1)) {
      paramPduHeaders.setOctet(paramCursor.getInt(paramInt1), paramInt2);
    }
  }
  
  private void setTextStringToHeaders(Cursor paramCursor, int paramInt1, PduHeaders paramPduHeaders, int paramInt2)
  {
    paramCursor = paramCursor.getString(paramInt1);
    if (paramCursor != null) {
      paramPduHeaders.setTextString(getBytes(paramCursor), paramInt2);
    }
  }
  
  public static String toIsoString(byte[] paramArrayOfByte)
  {
    try
    {
      paramArrayOfByte = new String(paramArrayOfByte, "iso-8859-1");
      return paramArrayOfByte;
    }
    catch (UnsupportedEncodingException paramArrayOfByte)
    {
      Log.e("PduPersister", "ISO_8859_1 must be supported!", paramArrayOfByte);
    }
    return "";
  }
  
  private void updateAddress(long paramLong, int paramInt, EncodedStringValue[] paramArrayOfEncodedStringValue)
  {
    Context localContext = mContext;
    ContentResolver localContentResolver = mContentResolver;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("content://mms/");
    ((StringBuilder)localObject).append(paramLong);
    ((StringBuilder)localObject).append("/addr");
    localObject = Uri.parse(((StringBuilder)localObject).toString());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("type=");
    localStringBuilder.append(paramInt);
    SqliteWrapper.delete(localContext, localContentResolver, (Uri)localObject, localStringBuilder.toString(), null);
    persistAddress(paramLong, paramInt, paramArrayOfEncodedStringValue);
  }
  
  private void updatePart(Uri paramUri, PduPart paramPduPart, HashMap<Uri, InputStream> paramHashMap)
    throws MmsException
  {
    ContentValues localContentValues = new ContentValues(7);
    int i = paramPduPart.getCharset();
    if (i != 0) {
      localContentValues.put("chset", Integer.valueOf(i));
    }
    if (paramPduPart.getContentType() != null)
    {
      String str1 = toIsoString(paramPduPart.getContentType());
      localContentValues.put("ct", str1);
      if (paramPduPart.getFilename() != null) {
        localContentValues.put("fn", new String(paramPduPart.getFilename()));
      }
      if (paramPduPart.getName() != null) {
        localContentValues.put("name", new String(paramPduPart.getName()));
      }
      String str2 = null;
      if (paramPduPart.getContentDisposition() != null)
      {
        str2 = toIsoString(paramPduPart.getContentDisposition());
        localContentValues.put("cd", (String)str2);
      }
      if (paramPduPart.getContentId() != null)
      {
        str2 = toIsoString(paramPduPart.getContentId());
        localContentValues.put("cid", (String)str2);
      }
      if (paramPduPart.getContentLocation() != null)
      {
        str2 = toIsoString(paramPduPart.getContentLocation());
        localContentValues.put("cl", (String)str2);
      }
      SqliteWrapper.update(mContext, mContentResolver, paramUri, localContentValues, null, null);
      if ((paramPduPart.getData() != null) || (!paramUri.equals(paramPduPart.getDataUri()))) {
        persistData(paramPduPart, paramUri, str1, paramHashMap);
      }
      return;
    }
    throw new MmsException("MIME type of the part must be set.");
  }
  
  public Cursor getPendingMessages(long paramLong)
  {
    Uri.Builder localBuilder = Telephony.MmsSms.PendingMessages.CONTENT_URI.buildUpon();
    localBuilder.appendQueryParameter("protocol", "mms");
    return SqliteWrapper.query(mContext, mContentResolver, localBuilder.build(), null, "err_type < ? AND due_time <= ?", new String[] { String.valueOf(10), String.valueOf(paramLong) }, "due_time");
  }
  
  /* Error */
  public GenericPdu load(Uri paramUri)
    throws MmsException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: iconst_0
    //   8: istore 5
    //   10: iconst_0
    //   11: istore 6
    //   13: iconst_0
    //   14: istore 7
    //   16: ldc2_w 845
    //   19: lstore 8
    //   21: aload_3
    //   22: astore 10
    //   24: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   27: astore 11
    //   29: aload_3
    //   30: astore 10
    //   32: aload 11
    //   34: monitorenter
    //   35: aload_2
    //   36: astore_3
    //   37: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   40: aload_1
    //   41: invokevirtual 850	com/google/android/mms/util/PduCache:isUpdating	(Landroid/net/Uri;)Z
    //   44: istore 12
    //   46: aload 4
    //   48: astore_3
    //   49: iload 12
    //   51: ifeq +123 -> 174
    //   54: aload_2
    //   55: astore_3
    //   56: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   59: invokevirtual 853	java/lang/Object:wait	()V
    //   62: goto +18 -> 80
    //   65: astore 10
    //   67: aload_2
    //   68: astore_3
    //   69: ldc 103
    //   71: ldc_w 855
    //   74: aload 10
    //   76: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   79: pop
    //   80: aload_2
    //   81: astore_3
    //   82: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   85: aload_1
    //   86: invokevirtual 856	com/google/android/mms/util/PduCache:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   89: checkcast 858	com/google/android/mms/util/PduCacheEntry
    //   92: astore 10
    //   94: aload 10
    //   96: astore_3
    //   97: aload 10
    //   99: ifnull +75 -> 174
    //   102: aload 10
    //   104: astore_3
    //   105: aload 10
    //   107: invokevirtual 862	com/google/android/mms/util/PduCacheEntry:getPdu	()Lcom/google/android/mms/pdu/GenericPdu;
    //   110: astore_2
    //   111: aload 10
    //   113: astore_3
    //   114: aload 11
    //   116: monitorexit
    //   117: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   120: astore_3
    //   121: aload_3
    //   122: monitorenter
    //   123: iconst_0
    //   124: ifeq +27 -> 151
    //   127: new 858	com/google/android/mms/util/PduCacheEntry
    //   130: astore 10
    //   132: aload 10
    //   134: aconst_null
    //   135: iconst_0
    //   136: lload 8
    //   138: invokespecial 865	com/google/android/mms/util/PduCacheEntry:<init>	(Lcom/google/android/mms/pdu/GenericPdu;IJ)V
    //   141: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   144: aload_1
    //   145: aload 10
    //   147: invokevirtual 868	com/google/android/mms/util/PduCache:put	(Landroid/net/Uri;Lcom/google/android/mms/util/PduCacheEntry;)Z
    //   150: pop
    //   151: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   154: aload_1
    //   155: iconst_0
    //   156: invokevirtual 872	com/google/android/mms/util/PduCache:setUpdating	(Landroid/net/Uri;Z)V
    //   159: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   162: invokevirtual 875	java/lang/Object:notifyAll	()V
    //   165: aload_3
    //   166: monitorexit
    //   167: aload_2
    //   168: areturn
    //   169: astore_1
    //   170: aload_3
    //   171: monitorexit
    //   172: aload_1
    //   173: athrow
    //   174: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   177: aload_1
    //   178: iconst_1
    //   179: invokevirtual 872	com/google/android/mms/util/PduCache:setUpdating	(Landroid/net/Uri;Z)V
    //   182: aload 11
    //   184: monitorexit
    //   185: iload 5
    //   187: istore 6
    //   189: lload 8
    //   191: lstore 13
    //   193: aload_0
    //   194: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   197: aload_0
    //   198: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   201: aload_1
    //   202: getstatic 180	com/google/android/mms/pdu/PduPersister:PDU_PROJECTION	[Ljava/lang/String;
    //   205: aconst_null
    //   206: aconst_null
    //   207: aconst_null
    //   208: invokestatic 437	com/google/android/mms/util/SqliteWrapper:query	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   211: astore 10
    //   213: iload 5
    //   215: istore 6
    //   217: lload 8
    //   219: lstore 13
    //   221: new 461	com/google/android/mms/pdu/PduHeaders
    //   224: astore_2
    //   225: iload 5
    //   227: istore 6
    //   229: lload 8
    //   231: lstore 13
    //   233: aload_2
    //   234: invokespecial 876	com/google/android/mms/pdu/PduHeaders:<init>	()V
    //   237: iload 5
    //   239: istore 6
    //   241: lload 8
    //   243: lstore 13
    //   245: aload_1
    //   246: invokestatic 882	android/content/ContentUris:parseId	(Landroid/net/Uri;)J
    //   249: lstore 15
    //   251: aload 10
    //   253: ifnull +1251 -> 1504
    //   256: iload 7
    //   258: istore 5
    //   260: lload 8
    //   262: lstore 17
    //   264: aload 10
    //   266: invokeinterface 326 1 0
    //   271: iconst_1
    //   272: if_icmpne +1232 -> 1504
    //   275: iload 7
    //   277: istore 5
    //   279: lload 8
    //   281: lstore 17
    //   283: aload 10
    //   285: invokeinterface 330 1 0
    //   290: ifeq +1214 -> 1504
    //   293: iload 7
    //   295: istore 5
    //   297: lload 8
    //   299: lstore 17
    //   301: aload 10
    //   303: iconst_1
    //   304: invokeinterface 384 2 0
    //   309: istore 7
    //   311: iload 7
    //   313: istore 5
    //   315: lload 8
    //   317: lstore 17
    //   319: aload 10
    //   321: iconst_2
    //   322: invokeinterface 511 2 0
    //   327: lstore 8
    //   329: iload 7
    //   331: istore 5
    //   333: lload 8
    //   335: lstore 17
    //   337: getstatic 236	com/google/android/mms/pdu/PduPersister:ENCODED_STRING_COLUMN_INDEX_MAP	Ljava/util/HashMap;
    //   340: invokevirtual 886	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   343: invokeinterface 609 1 0
    //   348: astore_3
    //   349: iload 7
    //   351: istore 5
    //   353: lload 8
    //   355: lstore 17
    //   357: aload_3
    //   358: invokeinterface 614 1 0
    //   363: ifeq +66 -> 429
    //   366: iload 7
    //   368: istore 5
    //   370: lload 8
    //   372: lstore 17
    //   374: aload_3
    //   375: invokeinterface 618 1 0
    //   380: checkcast 888	java/util/Map$Entry
    //   383: astore 4
    //   385: iload 7
    //   387: istore 5
    //   389: lload 8
    //   391: lstore 17
    //   393: aload_0
    //   394: aload 10
    //   396: aload 4
    //   398: invokeinterface 891 1 0
    //   403: checkcast 213	java/lang/Integer
    //   406: invokevirtual 482	java/lang/Integer:intValue	()I
    //   409: aload_2
    //   410: aload 4
    //   412: invokeinterface 894 1 0
    //   417: checkcast 213	java/lang/Integer
    //   420: invokevirtual 482	java/lang/Integer:intValue	()I
    //   423: invokespecial 896	com/google/android/mms/pdu/PduPersister:setEncodedStringValueToHeaders	(Landroid/database/Cursor;ILcom/google/android/mms/pdu/PduHeaders;I)V
    //   426: goto -77 -> 349
    //   429: iload 7
    //   431: istore 5
    //   433: lload 8
    //   435: lstore 17
    //   437: getstatic 240	com/google/android/mms/pdu/PduPersister:TEXT_STRING_COLUMN_INDEX_MAP	Ljava/util/HashMap;
    //   440: invokevirtual 886	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   443: invokeinterface 609 1 0
    //   448: astore 4
    //   450: iload 7
    //   452: istore 5
    //   454: lload 8
    //   456: lstore 17
    //   458: aload 4
    //   460: invokeinterface 614 1 0
    //   465: ifeq +64 -> 529
    //   468: iload 7
    //   470: istore 5
    //   472: lload 8
    //   474: lstore 17
    //   476: aload 4
    //   478: invokeinterface 618 1 0
    //   483: checkcast 888	java/util/Map$Entry
    //   486: astore_3
    //   487: iload 7
    //   489: istore 5
    //   491: lload 8
    //   493: lstore 17
    //   495: aload_0
    //   496: aload 10
    //   498: aload_3
    //   499: invokeinterface 891 1 0
    //   504: checkcast 213	java/lang/Integer
    //   507: invokevirtual 482	java/lang/Integer:intValue	()I
    //   510: aload_2
    //   511: aload_3
    //   512: invokeinterface 894 1 0
    //   517: checkcast 213	java/lang/Integer
    //   520: invokevirtual 482	java/lang/Integer:intValue	()I
    //   523: invokespecial 898	com/google/android/mms/pdu/PduPersister:setTextStringToHeaders	(Landroid/database/Cursor;ILcom/google/android/mms/pdu/PduHeaders;I)V
    //   526: goto -76 -> 450
    //   529: iload 7
    //   531: istore 5
    //   533: lload 8
    //   535: lstore 17
    //   537: getstatic 244	com/google/android/mms/pdu/PduPersister:OCTET_COLUMN_INDEX_MAP	Ljava/util/HashMap;
    //   540: invokevirtual 886	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   543: invokeinterface 609 1 0
    //   548: astore 4
    //   550: iload 7
    //   552: istore 5
    //   554: lload 8
    //   556: lstore 17
    //   558: aload 4
    //   560: invokeinterface 614 1 0
    //   565: ifeq +64 -> 629
    //   568: iload 7
    //   570: istore 5
    //   572: lload 8
    //   574: lstore 17
    //   576: aload 4
    //   578: invokeinterface 618 1 0
    //   583: checkcast 888	java/util/Map$Entry
    //   586: astore_3
    //   587: iload 7
    //   589: istore 5
    //   591: lload 8
    //   593: lstore 17
    //   595: aload_0
    //   596: aload 10
    //   598: aload_3
    //   599: invokeinterface 891 1 0
    //   604: checkcast 213	java/lang/Integer
    //   607: invokevirtual 482	java/lang/Integer:intValue	()I
    //   610: aload_2
    //   611: aload_3
    //   612: invokeinterface 894 1 0
    //   617: checkcast 213	java/lang/Integer
    //   620: invokevirtual 482	java/lang/Integer:intValue	()I
    //   623: invokespecial 900	com/google/android/mms/pdu/PduPersister:setOctetToHeaders	(Landroid/database/Cursor;ILcom/google/android/mms/pdu/PduHeaders;I)V
    //   626: goto -76 -> 550
    //   629: iload 7
    //   631: istore 5
    //   633: lload 8
    //   635: lstore 17
    //   637: getstatic 248	com/google/android/mms/pdu/PduPersister:LONG_COLUMN_INDEX_MAP	Ljava/util/HashMap;
    //   640: invokevirtual 886	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   643: astore_3
    //   644: iload 7
    //   646: istore 5
    //   648: lload 8
    //   650: lstore 17
    //   652: aload_3
    //   653: invokeinterface 609 1 0
    //   658: astore 4
    //   660: iload 7
    //   662: istore 5
    //   664: lload 8
    //   666: lstore 17
    //   668: aload 4
    //   670: invokeinterface 614 1 0
    //   675: ifeq +67 -> 742
    //   678: iload 7
    //   680: istore 5
    //   682: lload 8
    //   684: lstore 17
    //   686: aload 4
    //   688: invokeinterface 618 1 0
    //   693: checkcast 888	java/util/Map$Entry
    //   696: astore 11
    //   698: iload 7
    //   700: istore 5
    //   702: lload 8
    //   704: lstore 17
    //   706: aload_0
    //   707: aload 10
    //   709: aload 11
    //   711: invokeinterface 891 1 0
    //   716: checkcast 213	java/lang/Integer
    //   719: invokevirtual 482	java/lang/Integer:intValue	()I
    //   722: aload_2
    //   723: aload 11
    //   725: invokeinterface 894 1 0
    //   730: checkcast 213	java/lang/Integer
    //   733: invokevirtual 482	java/lang/Integer:intValue	()I
    //   736: invokespecial 902	com/google/android/mms/pdu/PduPersister:setLongToHeaders	(Landroid/database/Cursor;ILcom/google/android/mms/pdu/PduHeaders;I)V
    //   739: goto -79 -> 660
    //   742: aload 10
    //   744: ifnull +18 -> 762
    //   747: iload 7
    //   749: istore 6
    //   751: lload 8
    //   753: lstore 13
    //   755: aload 10
    //   757: invokeinterface 341 1 0
    //   762: lload 15
    //   764: ldc2_w 845
    //   767: lcmp
    //   768: ifeq +695 -> 1463
    //   771: iload 7
    //   773: istore 6
    //   775: lload 8
    //   777: lstore 13
    //   779: aload_0
    //   780: lload 15
    //   782: aload_2
    //   783: invokespecial 904	com/google/android/mms/pdu/PduPersister:loadAddress	(JLcom/google/android/mms/pdu/PduHeaders;)V
    //   786: iload 7
    //   788: istore 6
    //   790: lload 8
    //   792: lstore 13
    //   794: aload_2
    //   795: sipush 140
    //   798: invokevirtual 907	com/google/android/mms/pdu/PduHeaders:getOctet	(I)I
    //   801: istore 19
    //   803: iload 7
    //   805: istore 6
    //   807: lload 8
    //   809: lstore 13
    //   811: new 909	com/google/android/mms/pdu/PduBody
    //   814: astore 11
    //   816: iload 7
    //   818: istore 6
    //   820: lload 8
    //   822: lstore 13
    //   824: aload 11
    //   826: invokespecial 910	com/google/android/mms/pdu/PduBody:<init>	()V
    //   829: iload 19
    //   831: sipush 132
    //   834: if_icmpeq +17 -> 851
    //   837: iload 19
    //   839: sipush 128
    //   842: if_icmpne +6 -> 848
    //   845: goto +6 -> 851
    //   848: goto +78 -> 926
    //   851: iload 7
    //   853: istore 6
    //   855: lload 8
    //   857: lstore 13
    //   859: aload_0
    //   860: lload 15
    //   862: invokespecial 912	com/google/android/mms/pdu/PduPersister:loadParts	(J)[Lcom/google/android/mms/pdu/PduPart;
    //   865: astore 4
    //   867: aload_3
    //   868: astore 10
    //   870: aload 4
    //   872: ifnull +54 -> 926
    //   875: iload 7
    //   877: istore 6
    //   879: lload 8
    //   881: lstore 13
    //   883: aload 4
    //   885: arraylength
    //   886: istore 20
    //   888: iconst_0
    //   889: istore 5
    //   891: aload_3
    //   892: astore 10
    //   894: iload 5
    //   896: iload 20
    //   898: if_icmpge +28 -> 926
    //   901: iload 7
    //   903: istore 6
    //   905: lload 8
    //   907: lstore 13
    //   909: aload 11
    //   911: aload 4
    //   913: iload 5
    //   915: aaload
    //   916: invokevirtual 916	com/google/android/mms/pdu/PduBody:addPart	(Lcom/google/android/mms/pdu/PduPart;)Z
    //   919: pop
    //   920: iinc 5 1
    //   923: goto -32 -> 891
    //   926: iload 19
    //   928: tableswitch	default:+112->1040, 128:+371->1299, 129:+270->1198, 130:+250->1178, 131:+230->1158, 132:+208->1136, 133:+188->1116, 134:+168->1096, 135:+148->1076, 136:+128->1056, 137:+270->1198, 138:+270->1198, 139:+270->1198, 140:+270->1198, 141:+270->1198, 142:+270->1198, 143:+270->1198, 144:+270->1198, 145:+270->1198, 146:+270->1198, 147:+270->1198, 148:+270->1198, 149:+270->1198, 150:+270->1198, 151:+270->1198
    //   1040: iload 7
    //   1042: istore 6
    //   1044: lload 8
    //   1046: lstore 13
    //   1048: new 472	com/google/android/mms/MmsException
    //   1051: astore 10
    //   1053: goto +324 -> 1377
    //   1056: iload 7
    //   1058: istore 6
    //   1060: lload 8
    //   1062: lstore 13
    //   1064: new 918	com/google/android/mms/pdu/ReadOrigInd
    //   1067: dup
    //   1068: aload_2
    //   1069: invokespecial 921	com/google/android/mms/pdu/ReadOrigInd:<init>	(Lcom/google/android/mms/pdu/PduHeaders;)V
    //   1072: astore_3
    //   1073: goto +245 -> 1318
    //   1076: iload 7
    //   1078: istore 6
    //   1080: lload 8
    //   1082: lstore 13
    //   1084: new 923	com/google/android/mms/pdu/ReadRecInd
    //   1087: dup
    //   1088: aload_2
    //   1089: invokespecial 924	com/google/android/mms/pdu/ReadRecInd:<init>	(Lcom/google/android/mms/pdu/PduHeaders;)V
    //   1092: astore_3
    //   1093: goto +225 -> 1318
    //   1096: iload 7
    //   1098: istore 6
    //   1100: lload 8
    //   1102: lstore 13
    //   1104: new 926	com/google/android/mms/pdu/DeliveryInd
    //   1107: dup
    //   1108: aload_2
    //   1109: invokespecial 927	com/google/android/mms/pdu/DeliveryInd:<init>	(Lcom/google/android/mms/pdu/PduHeaders;)V
    //   1112: astore_3
    //   1113: goto +205 -> 1318
    //   1116: iload 7
    //   1118: istore 6
    //   1120: lload 8
    //   1122: lstore 13
    //   1124: new 929	com/google/android/mms/pdu/AcknowledgeInd
    //   1127: dup
    //   1128: aload_2
    //   1129: invokespecial 930	com/google/android/mms/pdu/AcknowledgeInd:<init>	(Lcom/google/android/mms/pdu/PduHeaders;)V
    //   1132: astore_3
    //   1133: goto +185 -> 1318
    //   1136: iload 7
    //   1138: istore 6
    //   1140: lload 8
    //   1142: lstore 13
    //   1144: new 932	com/google/android/mms/pdu/RetrieveConf
    //   1147: dup
    //   1148: aload_2
    //   1149: aload 11
    //   1151: invokespecial 935	com/google/android/mms/pdu/RetrieveConf:<init>	(Lcom/google/android/mms/pdu/PduHeaders;Lcom/google/android/mms/pdu/PduBody;)V
    //   1154: astore_3
    //   1155: goto +163 -> 1318
    //   1158: iload 7
    //   1160: istore 6
    //   1162: lload 8
    //   1164: lstore 13
    //   1166: new 937	com/google/android/mms/pdu/NotifyRespInd
    //   1169: dup
    //   1170: aload_2
    //   1171: invokespecial 938	com/google/android/mms/pdu/NotifyRespInd:<init>	(Lcom/google/android/mms/pdu/PduHeaders;)V
    //   1174: astore_3
    //   1175: goto +143 -> 1318
    //   1178: iload 7
    //   1180: istore 6
    //   1182: lload 8
    //   1184: lstore 13
    //   1186: new 940	com/google/android/mms/pdu/NotificationInd
    //   1189: dup
    //   1190: aload_2
    //   1191: invokespecial 941	com/google/android/mms/pdu/NotificationInd:<init>	(Lcom/google/android/mms/pdu/PduHeaders;)V
    //   1194: astore_3
    //   1195: goto +123 -> 1318
    //   1198: iload 7
    //   1200: istore 6
    //   1202: lload 8
    //   1204: lstore 13
    //   1206: new 472	com/google/android/mms/MmsException
    //   1209: astore_3
    //   1210: iload 7
    //   1212: istore 6
    //   1214: lload 8
    //   1216: lstore 13
    //   1218: new 409	java/lang/StringBuilder
    //   1221: astore 10
    //   1223: iload 7
    //   1225: istore 6
    //   1227: lload 8
    //   1229: lstore 13
    //   1231: aload 10
    //   1233: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   1236: iload 7
    //   1238: istore 6
    //   1240: lload 8
    //   1242: lstore 13
    //   1244: aload 10
    //   1246: ldc_w 943
    //   1249: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1252: pop
    //   1253: iload 7
    //   1255: istore 6
    //   1257: lload 8
    //   1259: lstore 13
    //   1261: aload 10
    //   1263: iload 19
    //   1265: invokestatic 946	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   1268: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1271: pop
    //   1272: iload 7
    //   1274: istore 6
    //   1276: lload 8
    //   1278: lstore 13
    //   1280: aload_3
    //   1281: aload 10
    //   1283: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1286: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   1289: iload 7
    //   1291: istore 6
    //   1293: lload 8
    //   1295: lstore 13
    //   1297: aload_3
    //   1298: athrow
    //   1299: iload 7
    //   1301: istore 6
    //   1303: lload 8
    //   1305: lstore 13
    //   1307: new 948	com/google/android/mms/pdu/SendReq
    //   1310: dup
    //   1311: aload_2
    //   1312: aload 11
    //   1314: invokespecial 949	com/google/android/mms/pdu/SendReq:<init>	(Lcom/google/android/mms/pdu/PduHeaders;Lcom/google/android/mms/pdu/PduBody;)V
    //   1317: astore_3
    //   1318: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1321: astore 10
    //   1323: aload 10
    //   1325: monitorenter
    //   1326: new 858	com/google/android/mms/util/PduCacheEntry
    //   1329: astore_2
    //   1330: aload_2
    //   1331: aload_3
    //   1332: iload 7
    //   1334: lload 8
    //   1336: invokespecial 865	com/google/android/mms/util/PduCacheEntry:<init>	(Lcom/google/android/mms/pdu/GenericPdu;IJ)V
    //   1339: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1342: aload_1
    //   1343: aload_2
    //   1344: invokevirtual 868	com/google/android/mms/util/PduCache:put	(Landroid/net/Uri;Lcom/google/android/mms/util/PduCacheEntry;)Z
    //   1347: pop
    //   1348: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1351: aload_1
    //   1352: iconst_0
    //   1353: invokevirtual 872	com/google/android/mms/util/PduCache:setUpdating	(Landroid/net/Uri;Z)V
    //   1356: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1359: invokevirtual 875	java/lang/Object:notifyAll	()V
    //   1362: aload 10
    //   1364: monitorexit
    //   1365: aload_3
    //   1366: areturn
    //   1367: astore_1
    //   1368: aload 10
    //   1370: monitorexit
    //   1371: aload_1
    //   1372: athrow
    //   1373: astore_1
    //   1374: goto -6 -> 1368
    //   1377: iload 7
    //   1379: istore 6
    //   1381: lload 8
    //   1383: lstore 13
    //   1385: new 409	java/lang/StringBuilder
    //   1388: astore_3
    //   1389: iload 7
    //   1391: istore 6
    //   1393: lload 8
    //   1395: lstore 13
    //   1397: aload_3
    //   1398: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   1401: iload 7
    //   1403: istore 6
    //   1405: lload 8
    //   1407: lstore 13
    //   1409: aload_3
    //   1410: ldc_w 951
    //   1413: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1416: pop
    //   1417: iload 7
    //   1419: istore 6
    //   1421: lload 8
    //   1423: lstore 13
    //   1425: aload_3
    //   1426: iload 19
    //   1428: invokestatic 946	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   1431: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1434: pop
    //   1435: iload 7
    //   1437: istore 6
    //   1439: lload 8
    //   1441: lstore 13
    //   1443: aload 10
    //   1445: aload_3
    //   1446: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1449: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   1452: iload 7
    //   1454: istore 6
    //   1456: lload 8
    //   1458: lstore 13
    //   1460: aload 10
    //   1462: athrow
    //   1463: iload 7
    //   1465: istore 6
    //   1467: lload 8
    //   1469: lstore 13
    //   1471: new 472	com/google/android/mms/MmsException
    //   1474: astore_3
    //   1475: iload 7
    //   1477: istore 6
    //   1479: lload 8
    //   1481: lstore 13
    //   1483: aload_3
    //   1484: ldc_w 953
    //   1487: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   1490: iload 7
    //   1492: istore 6
    //   1494: lload 8
    //   1496: lstore 13
    //   1498: aload_3
    //   1499: athrow
    //   1500: astore_3
    //   1501: goto +95 -> 1596
    //   1504: iload 7
    //   1506: istore 5
    //   1508: lload 8
    //   1510: lstore 17
    //   1512: new 472	com/google/android/mms/MmsException
    //   1515: astore_3
    //   1516: iload 7
    //   1518: istore 5
    //   1520: lload 8
    //   1522: lstore 17
    //   1524: new 409	java/lang/StringBuilder
    //   1527: astore_2
    //   1528: iload 7
    //   1530: istore 5
    //   1532: lload 8
    //   1534: lstore 17
    //   1536: aload_2
    //   1537: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   1540: iload 7
    //   1542: istore 5
    //   1544: lload 8
    //   1546: lstore 17
    //   1548: aload_2
    //   1549: ldc_w 955
    //   1552: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1555: pop
    //   1556: iload 7
    //   1558: istore 5
    //   1560: lload 8
    //   1562: lstore 17
    //   1564: aload_2
    //   1565: aload_1
    //   1566: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1569: pop
    //   1570: iload 7
    //   1572: istore 5
    //   1574: lload 8
    //   1576: lstore 17
    //   1578: aload_3
    //   1579: aload_2
    //   1580: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1583: invokespecial 573	com/google/android/mms/MmsException:<init>	(Ljava/lang/String;)V
    //   1586: iload 7
    //   1588: istore 5
    //   1590: lload 8
    //   1592: lstore 17
    //   1594: aload_3
    //   1595: athrow
    //   1596: aload 10
    //   1598: ifnull +18 -> 1616
    //   1601: iload 5
    //   1603: istore 6
    //   1605: lload 17
    //   1607: lstore 13
    //   1609: aload 10
    //   1611: invokeinterface 341 1 0
    //   1616: iload 5
    //   1618: istore 6
    //   1620: lload 17
    //   1622: lstore 13
    //   1624: aload_3
    //   1625: athrow
    //   1626: astore_3
    //   1627: goto +27 -> 1654
    //   1630: astore_2
    //   1631: aload_3
    //   1632: astore 10
    //   1634: goto +7 -> 1641
    //   1637: astore_2
    //   1638: aload_3
    //   1639: astore 10
    //   1641: aload 10
    //   1643: astore_3
    //   1644: aload 11
    //   1646: monitorexit
    //   1647: aload_2
    //   1648: athrow
    //   1649: astore_3
    //   1650: lload 8
    //   1652: lstore 13
    //   1654: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1657: astore 10
    //   1659: aload 10
    //   1661: monitorenter
    //   1662: iconst_0
    //   1663: ifeq +32 -> 1695
    //   1666: new 858	com/google/android/mms/util/PduCacheEntry
    //   1669: astore_2
    //   1670: aload_2
    //   1671: aconst_null
    //   1672: iload 6
    //   1674: lload 13
    //   1676: invokespecial 865	com/google/android/mms/util/PduCacheEntry:<init>	(Lcom/google/android/mms/pdu/GenericPdu;IJ)V
    //   1679: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1682: aload_1
    //   1683: aload_2
    //   1684: invokevirtual 868	com/google/android/mms/util/PduCache:put	(Landroid/net/Uri;Lcom/google/android/mms/util/PduCacheEntry;)Z
    //   1687: pop
    //   1688: goto +7 -> 1695
    //   1691: astore_1
    //   1692: goto +23 -> 1715
    //   1695: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1698: aload_1
    //   1699: iconst_0
    //   1700: invokevirtual 872	com/google/android/mms/util/PduCache:setUpdating	(Landroid/net/Uri;Z)V
    //   1703: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   1706: invokevirtual 875	java/lang/Object:notifyAll	()V
    //   1709: aload 10
    //   1711: monitorexit
    //   1712: aload_3
    //   1713: athrow
    //   1714: astore_1
    //   1715: aload 10
    //   1717: monitorexit
    //   1718: aload_1
    //   1719: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1720	0	this	PduPersister
    //   0	1720	1	paramUri	Uri
    //   1	1579	2	localObject1	Object
    //   1630	1	2	localObject2	Object
    //   1637	11	2	localObject3	Object
    //   1669	15	2	localPduCacheEntry	com.google.android.mms.util.PduCacheEntry
    //   3	1496	3	localObject4	Object
    //   1500	1	3	localObject5	Object
    //   1515	110	3	localMmsException	MmsException
    //   1626	13	3	localObject6	Object
    //   1643	1	3	localObject7	Object
    //   1649	64	3	localObject8	Object
    //   5	907	4	localObject9	Object
    //   8	1609	5	i	int
    //   11	1662	6	j	int
    //   14	1573	7	k	int
    //   19	1632	8	l1	long
    //   22	9	10	localObject10	Object
    //   65	10	10	localInterruptedException	InterruptedException
    //   92	1624	10	localObject11	Object
    //   27	1618	11	localObject12	Object
    //   44	6	12	bool	boolean
    //   191	1484	13	l2	long
    //   249	612	15	l3	long
    //   262	1359	17	l4	long
    //   801	626	19	m	int
    //   886	13	20	n	int
    // Exception table:
    //   from	to	target	type
    //   56	62	65	java/lang/InterruptedException
    //   127	151	169	finally
    //   151	167	169	finally
    //   170	172	169	finally
    //   1339	1365	1367	finally
    //   1326	1339	1373	finally
    //   1368	1371	1373	finally
    //   264	275	1500	finally
    //   283	293	1500	finally
    //   301	311	1500	finally
    //   319	329	1500	finally
    //   337	349	1500	finally
    //   357	366	1500	finally
    //   374	385	1500	finally
    //   393	426	1500	finally
    //   437	450	1500	finally
    //   458	468	1500	finally
    //   476	487	1500	finally
    //   495	526	1500	finally
    //   537	550	1500	finally
    //   558	568	1500	finally
    //   576	587	1500	finally
    //   595	626	1500	finally
    //   637	644	1500	finally
    //   652	660	1500	finally
    //   668	678	1500	finally
    //   686	698	1500	finally
    //   706	739	1500	finally
    //   1512	1516	1500	finally
    //   1524	1528	1500	finally
    //   1536	1540	1500	finally
    //   1548	1556	1500	finally
    //   1564	1570	1500	finally
    //   1578	1586	1500	finally
    //   1594	1596	1500	finally
    //   193	213	1626	finally
    //   221	225	1626	finally
    //   233	237	1626	finally
    //   245	251	1626	finally
    //   755	762	1626	finally
    //   779	786	1626	finally
    //   794	803	1626	finally
    //   811	816	1626	finally
    //   824	829	1626	finally
    //   859	867	1626	finally
    //   883	888	1626	finally
    //   909	920	1626	finally
    //   1048	1053	1626	finally
    //   1064	1073	1626	finally
    //   1084	1093	1626	finally
    //   1104	1113	1626	finally
    //   1124	1133	1626	finally
    //   1144	1155	1626	finally
    //   1166	1175	1626	finally
    //   1186	1195	1626	finally
    //   1206	1210	1626	finally
    //   1218	1223	1626	finally
    //   1231	1236	1626	finally
    //   1244	1253	1626	finally
    //   1261	1272	1626	finally
    //   1280	1289	1626	finally
    //   1297	1299	1626	finally
    //   1307	1318	1626	finally
    //   1385	1389	1626	finally
    //   1397	1401	1626	finally
    //   1409	1417	1626	finally
    //   1425	1435	1626	finally
    //   1443	1452	1626	finally
    //   1460	1463	1626	finally
    //   1471	1475	1626	finally
    //   1483	1490	1626	finally
    //   1498	1500	1626	finally
    //   1609	1616	1626	finally
    //   1624	1626	1626	finally
    //   174	185	1630	finally
    //   37	46	1637	finally
    //   56	62	1637	finally
    //   69	80	1637	finally
    //   82	94	1637	finally
    //   105	111	1637	finally
    //   114	117	1637	finally
    //   1644	1647	1637	finally
    //   24	29	1649	finally
    //   32	35	1649	finally
    //   1647	1649	1649	finally
    //   1679	1688	1691	finally
    //   1666	1679	1714	finally
    //   1695	1712	1714	finally
    //   1715	1718	1714	finally
  }
  
  public Uri move(Uri paramUri1, Uri paramUri2)
    throws MmsException
  {
    long l = ContentUris.parseId(paramUri1);
    if (l != -1L)
    {
      Integer localInteger = (Integer)MESSAGE_BOX_MAP.get(paramUri2);
      if (localInteger != null)
      {
        ContentValues localContentValues = new ContentValues(1);
        localContentValues.put("msg_box", localInteger);
        SqliteWrapper.update(mContext, mContentResolver, paramUri1, localContentValues, null, null);
        return ContentUris.withAppendedId(paramUri2, l);
      }
      throw new MmsException("Bad destination, must be one of content://mms/inbox, content://mms/sent, content://mms/drafts, content://mms/outbox, content://mms/temp.");
    }
    throw new MmsException("Error! ID of the message: -1.");
  }
  
  public Uri persist(GenericPdu paramGenericPdu, Uri paramUri, boolean paramBoolean1, boolean paramBoolean2, HashMap<Uri, InputStream> paramHashMap)
    throws MmsException
  {
    if (paramUri != null)
    {
      long l1 = -1L;
      long l2;
      try
      {
        l2 = ContentUris.parseId(paramUri);
        l1 = l2;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        for (;;) {}
      }
      int i;
      if (l1 != -1L) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i == 0) && (MESSAGE_BOX_MAP.get(paramUri) == null)) {
        throw new MmsException("Bad destination, must be one of content://mms/inbox, content://mms/sent, content://mms/drafts, content://mms/outbox, content://mms/temp.");
      }
      synchronized (PDU_CACHE_INSTANCE)
      {
        boolean bool = PDU_CACHE_INSTANCE.isUpdating(paramUri);
        if (bool) {
          try
          {
            PDU_CACHE_INSTANCE.wait();
          }
          catch (InterruptedException localInterruptedException)
          {
            Log.e("PduPersister", "persist1: ", localInterruptedException);
          }
        }
        PDU_CACHE_INSTANCE.purge(paramUri);
        PduHeaders localPduHeaders = paramGenericPdu.getPduHeaders();
        bool = false;
        Object localObject3 = new ContentValues();
        Object localObject2 = ENCODED_STRING_COLUMN_NAME_MAP.entrySet().iterator();
        EncodedStringValue localEncodedStringValue;
        while (((Iterator)localObject2).hasNext())
        {
          ??? = (Map.Entry)((Iterator)localObject2).next();
          j = ((Integer)((Map.Entry)???).getKey()).intValue();
          localEncodedStringValue = localPduHeaders.getEncodedStringValue(j);
          if (localEncodedStringValue != null)
          {
            localObject4 = (String)CHARSET_COLUMN_NAME_MAP.get(Integer.valueOf(j));
            ((ContentValues)localObject3).put((String)((Map.Entry)???).getValue(), toIsoString(localEncodedStringValue.getTextString()));
            ((ContentValues)localObject3).put((String)localObject4, Integer.valueOf(localEncodedStringValue.getCharacterSet()));
          }
        }
        localObject2 = TEXT_STRING_COLUMN_NAME_MAP.entrySet().iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject4 = (Map.Entry)((Iterator)localObject2).next();
          ??? = localPduHeaders.getTextString(((Integer)((Map.Entry)localObject4).getKey()).intValue());
          if (??? != null) {
            ((ContentValues)localObject3).put((String)((Map.Entry)localObject4).getValue(), toIsoString((byte[])???));
          }
        }
        ??? = OCTET_COLUMN_NAME_MAP.entrySet().iterator();
        while (((Iterator)???).hasNext())
        {
          localObject2 = (Map.Entry)((Iterator)???).next();
          j = localPduHeaders.getOctet(((Integer)((Map.Entry)localObject2).getKey()).intValue());
          if (j != 0) {
            ((ContentValues)localObject3).put((String)((Map.Entry)localObject2).getValue(), Integer.valueOf(j));
          }
        }
        localObject2 = LONG_COLUMN_NAME_MAP.entrySet().iterator();
        while (((Iterator)localObject2).hasNext())
        {
          ??? = (Map.Entry)((Iterator)localObject2).next();
          l2 = localPduHeaders.getLongInteger(((Integer)((Map.Entry)???).getKey()).intValue());
          if (l2 != -1L) {
            ((ContentValues)localObject3).put((String)((Map.Entry)???).getValue(), Long.valueOf(l2));
          }
        }
        Object localObject4 = new HashMap(ADDRESS_FIELDS.length);
        int m;
        for (m : ADDRESS_FIELDS)
        {
          ??? = null;
          if (m == 137)
          {
            localEncodedStringValue = localPduHeaders.getEncodedStringValue(m);
            if (localEncodedStringValue != null)
            {
              ??? = new EncodedStringValue[1];
              ???[0] = localEncodedStringValue;
            }
          }
          else
          {
            ??? = localPduHeaders.getEncodedStringValues(m);
          }
          ((HashMap)localObject4).put(Integer.valueOf(m), ???);
        }
        ??? = new HashSet();
        int j = paramGenericPdu.getMessageType();
        if ((j == 130) || (j == 132) || (j == 128))
        {
          if (j != 128)
          {
            if ((j == 130) || (j == 132))
            {
              loadRecipients(137, (HashSet)???, (HashMap)localObject4, false);
              if (paramBoolean2)
              {
                loadRecipients(151, (HashSet)???, (HashMap)localObject4, true);
                loadRecipients(130, (HashSet)???, (HashMap)localObject4, true);
              }
            }
          }
          else {
            loadRecipients(151, (HashSet)???, (HashMap)localObject4, false);
          }
          long l3 = 0L;
          l2 = l3;
          if (paramBoolean1)
          {
            l2 = l3;
            if (!((HashSet)???).isEmpty()) {
              l2 = Telephony.Threads.getOrCreateThreadId(mContext, (Set)???);
            }
          }
          ((ContentValues)localObject3).put("thread_id", Long.valueOf(l2));
        }
        l2 = System.currentTimeMillis();
        j = 0;
        if ((paramGenericPdu instanceof MultimediaMessagePdu))
        {
          localObject2 = ((MultimediaMessagePdu)paramGenericPdu).getBody();
          if (localObject2 != null)
          {
            int n = ((PduBody)localObject2).getPartsNum();
            ??? = 1;
            if (n > 2) {
              ??? = 0;
            }
            int i1 = 0;
            paramGenericPdu = (GenericPdu)???;
            ??? = localObject2;
            while (i1 < n)
            {
              localObject2 = ((PduBody)???).getPart(i1);
              j += ((PduPart)localObject2).getDataLength();
              persistPart((PduPart)localObject2, l2, paramHashMap);
              localObject2 = getPartContentType((PduPart)localObject2);
              m = ???;
              if (localObject2 != null)
              {
                m = ???;
                if (!"application/smil".equals(localObject2))
                {
                  m = ???;
                  if (!"text/plain".equals(localObject2)) {
                    m = 0;
                  }
                }
              }
              i1++;
              ??? = m;
            }
          }
          else
          {
            ??? = 1;
            paramGenericPdu = (GenericPdu)???;
            j = 0;
          }
        }
        else
        {
          ??? = 1;
          paramGenericPdu = (GenericPdu)???;
          j = 0;
        }
        if (??? != 0) {
          ??? = 1;
        } else {
          ??? = 0;
        }
        ((ContentValues)localObject3).put("text_only", Integer.valueOf(???));
        if (((ContentValues)localObject3).getAsInteger("m_size") == null) {
          ((ContentValues)localObject3).put("m_size", Integer.valueOf(j));
        }
        if (i != 0)
        {
          SqliteWrapper.update(mContext, mContentResolver, paramUri, (ContentValues)localObject3, null, null);
          paramGenericPdu = paramUri;
        }
        else
        {
          paramGenericPdu = SqliteWrapper.insert(mContext, mContentResolver, paramUri, (ContentValues)localObject3);
          if (paramGenericPdu == null) {
            break label1276;
          }
          l1 = ContentUris.parseId(paramGenericPdu);
        }
        localObject2 = new ContentValues(1);
        ((ContentValues)localObject2).put("mid", Long.valueOf(l1));
        paramHashMap = mContext;
        ??? = mContentResolver;
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("content://mms/");
        ((StringBuilder)localObject3).append(l2);
        ((StringBuilder)localObject3).append("/part");
        SqliteWrapper.update(paramHashMap, (ContentResolver)???, Uri.parse(((StringBuilder)localObject3).toString()), (ContentValues)localObject2, null, null);
        if (i == 0)
        {
          paramGenericPdu = new StringBuilder();
          paramGenericPdu.append(paramUri);
          paramGenericPdu.append("/");
          paramGenericPdu.append(l1);
          paramGenericPdu = Uri.parse(paramGenericPdu.toString());
        }
        for (m : ADDRESS_FIELDS)
        {
          paramUri = (EncodedStringValue[])((HashMap)localObject4).get(Integer.valueOf(m));
          if (paramUri != null) {
            persistAddress(l1, m, paramUri);
          }
        }
        return paramGenericPdu;
        label1276:
        throw new MmsException("persist() failed: return null.");
      }
    }
    throw new MmsException("Uri may not be null.");
  }
  
  public Uri persistPart(PduPart paramPduPart, long paramLong, HashMap<Uri, InputStream> paramHashMap)
    throws MmsException
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("content://mms/");
    ((StringBuilder)localObject1).append(paramLong);
    ((StringBuilder)localObject1).append("/part");
    Uri localUri = Uri.parse(((StringBuilder)localObject1).toString());
    ContentValues localContentValues = new ContentValues(8);
    int i = paramPduPart.getCharset();
    if (i != 0) {
      localContentValues.put("chset", Integer.valueOf(i));
    }
    Object localObject2 = getPartContentType(paramPduPart);
    if (localObject2 != null)
    {
      localObject1 = localObject2;
      if ("image/jpg".equals(localObject2)) {
        localObject1 = "image/jpeg";
      }
      localContentValues.put("ct", (String)localObject1);
      if ("application/smil".equals(localObject1)) {
        localContentValues.put("seq", Integer.valueOf(-1));
      }
      if (paramPduPart.getFilename() != null) {
        localContentValues.put("fn", new String(paramPduPart.getFilename()));
      }
      if (paramPduPart.getName() != null) {
        localContentValues.put("name", new String(paramPduPart.getName()));
      }
      if (paramPduPart.getContentDisposition() != null) {
        localContentValues.put("cd", (String)toIsoString(paramPduPart.getContentDisposition()));
      }
      if (paramPduPart.getContentId() != null) {
        localContentValues.put("cid", (String)toIsoString(paramPduPart.getContentId()));
      }
      if (paramPduPart.getContentLocation() != null) {
        localContentValues.put("cl", (String)toIsoString(paramPduPart.getContentLocation()));
      }
      localObject2 = SqliteWrapper.insert(mContext, mContentResolver, localUri, localContentValues);
      if (localObject2 != null)
      {
        persistData(paramPduPart, (Uri)localObject2, (String)localObject1, paramHashMap);
        paramPduPart.setDataUri((Uri)localObject2);
        return localObject2;
      }
      throw new MmsException("Failed to persist part, return null.");
    }
    throw new MmsException("MIME type of the part must be set.");
  }
  
  public void release()
  {
    Uri localUri = Uri.parse("content://mms/9223372036854775807/part");
    SqliteWrapper.delete(mContext, mContentResolver, localUri, null, null);
  }
  
  public void updateHeaders(Uri paramUri, SendReq paramSendReq)
  {
    synchronized (PDU_CACHE_INSTANCE)
    {
      boolean bool = PDU_CACHE_INSTANCE.isUpdating(paramUri);
      if (bool) {
        try
        {
          PDU_CACHE_INSTANCE.wait();
        }
        catch (InterruptedException localInterruptedException)
        {
          Log.e("PduPersister", "updateHeaders: ", localInterruptedException);
        }
      }
      PDU_CACHE_INSTANCE.purge(paramUri);
      ContentValues localContentValues = new ContentValues(10);
      ??? = paramSendReq.getContentType();
      if (??? != null) {
        localContentValues.put("ct_t", toIsoString((byte[])???));
      }
      long l1 = paramSendReq.getDate();
      if (l1 != -1L) {
        localContentValues.put("date", Long.valueOf(l1));
      }
      int i = paramSendReq.getDeliveryReport();
      if (i != 0) {
        localContentValues.put("d_rpt", Integer.valueOf(i));
      }
      long l2 = paramSendReq.getExpiry();
      if (l2 != -1L) {
        localContentValues.put("exp", Long.valueOf(l2));
      }
      Object localObject2 = paramSendReq.getMessageClass();
      if (localObject2 != null) {
        localContentValues.put("m_cls", toIsoString((byte[])localObject2));
      }
      i = paramSendReq.getPriority();
      if (i != 0) {
        localContentValues.put("pri", Integer.valueOf(i));
      }
      int j = paramSendReq.getReadReport();
      if (j != 0) {
        localContentValues.put("rr", Integer.valueOf(j));
      }
      localObject2 = paramSendReq.getTransactionId();
      if (localObject2 != null) {
        localContentValues.put("tr_id", toIsoString((byte[])localObject2));
      }
      localObject2 = paramSendReq.getSubject();
      if (localObject2 != null)
      {
        localContentValues.put("sub", toIsoString(((EncodedStringValue)localObject2).getTextString()));
        localContentValues.put("sub_cs", Integer.valueOf(((EncodedStringValue)localObject2).getCharacterSet()));
      }
      else
      {
        localContentValues.put("sub", "");
      }
      l2 = paramSendReq.getMessageSize();
      if (l2 > 0L) {
        localContentValues.put("m_size", Long.valueOf(l2));
      }
      PduHeaders localPduHeaders = paramSendReq.getPduHeaders();
      HashSet localHashSet = new HashSet();
      for (int m : ADDRESS_FIELDS)
      {
        paramSendReq = null;
        EncodedStringValue localEncodedStringValue;
        if (m == 137)
        {
          localEncodedStringValue = localPduHeaders.getEncodedStringValue(m);
          if (localEncodedStringValue != null) {
            paramSendReq = new EncodedStringValue[] { localEncodedStringValue };
          }
        }
        else
        {
          paramSendReq = localPduHeaders.getEncodedStringValues(m);
        }
        if (paramSendReq != null)
        {
          updateAddress(ContentUris.parseId(paramUri), m, paramSendReq);
          if (m == 151)
          {
            int n = paramSendReq.length;
            for (int i1 = 0; i1 < n; i1++)
            {
              localEncodedStringValue = paramSendReq[i1];
              if (localEncodedStringValue != null) {
                localHashSet.add(localEncodedStringValue.getString());
              }
            }
          }
        }
      }
      if (!localHashSet.isEmpty()) {
        localContentValues.put("thread_id", Long.valueOf(Telephony.Threads.getOrCreateThreadId(mContext, localHashSet)));
      }
      SqliteWrapper.update(mContext, mContentResolver, paramUri, localContentValues, null, null);
      return;
    }
  }
  
  /* Error */
  public void updateParts(Uri paramUri, PduBody arg2, HashMap<Uri, InputStream> paramHashMap)
    throws MmsException
  {
    // Byte code:
    //   0: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   3: astore 4
    //   5: aload 4
    //   7: monitorenter
    //   8: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   11: aload_1
    //   12: invokevirtual 850	com/google/android/mms/util/PduCache:isUpdating	(Landroid/net/Uri;)Z
    //   15: istore 5
    //   17: iload 5
    //   19: ifeq +54 -> 73
    //   22: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   25: invokevirtual 853	java/lang/Object:wait	()V
    //   28: goto +16 -> 44
    //   31: astore 6
    //   33: ldc 103
    //   35: ldc_w 1106
    //   38: aload 6
    //   40: invokestatic 378	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   43: pop
    //   44: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   47: aload_1
    //   48: invokevirtual 856	com/google/android/mms/util/PduCache:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   51: checkcast 858	com/google/android/mms/util/PduCacheEntry
    //   54: astore 6
    //   56: aload 6
    //   58: ifnull +15 -> 73
    //   61: aload 6
    //   63: invokevirtual 862	com/google/android/mms/util/PduCacheEntry:getPdu	()Lcom/google/android/mms/pdu/GenericPdu;
    //   66: checkcast 1022	com/google/android/mms/pdu/MultimediaMessagePdu
    //   69: aload_2
    //   70: invokevirtual 1110	com/google/android/mms/pdu/MultimediaMessagePdu:setBody	(Lcom/google/android/mms/pdu/PduBody;)V
    //   73: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   76: aload_1
    //   77: iconst_1
    //   78: invokevirtual 872	com/google/android/mms/util/PduCache:setUpdating	(Landroid/net/Uri;Z)V
    //   81: aload 4
    //   83: monitorexit
    //   84: new 1112	java/util/ArrayList
    //   87: astore 6
    //   89: aload 6
    //   91: invokespecial 1113	java/util/ArrayList:<init>	()V
    //   94: new 200	java/util/HashMap
    //   97: astore 4
    //   99: aload 4
    //   101: invokespecial 203	java/util/HashMap:<init>	()V
    //   104: aload_2
    //   105: invokevirtual 1029	com/google/android/mms/pdu/PduBody:getPartsNum	()I
    //   108: istore 7
    //   110: new 409	java/lang/StringBuilder
    //   113: astore 8
    //   115: aload 8
    //   117: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   120: aload 8
    //   122: bipush 40
    //   124: invokevirtual 1116	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   127: pop
    //   128: iconst_0
    //   129: istore 9
    //   131: iload 9
    //   133: iload 7
    //   135: if_icmpge +123 -> 258
    //   138: aload_2
    //   139: iload 9
    //   141: invokevirtual 1033	com/google/android/mms/pdu/PduBody:getPart	(I)Lcom/google/android/mms/pdu/PduPart;
    //   144: astore 10
    //   146: aload 10
    //   148: invokevirtual 689	com/google/android/mms/pdu/PduPart:getDataUri	()Landroid/net/Uri;
    //   151: astore 11
    //   153: aload 11
    //   155: ifnull +89 -> 244
    //   158: aload 11
    //   160: invokevirtual 1119	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   163: invokestatic 446	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   166: ifne +78 -> 244
    //   169: aload 11
    //   171: invokevirtual 1119	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   174: ldc_w 822
    //   177: invokevirtual 1122	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   180: ifne +6 -> 186
    //   183: goto +61 -> 244
    //   186: aload 4
    //   188: aload 11
    //   190: aload 10
    //   192: invokevirtual 221	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   195: pop
    //   196: aload 8
    //   198: invokevirtual 1123	java/lang/StringBuilder:length	()I
    //   201: iconst_1
    //   202: if_icmple +12 -> 214
    //   205: aload 8
    //   207: ldc_w 1125
    //   210: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: pop
    //   214: aload 8
    //   216: ldc 126
    //   218: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: pop
    //   222: aload 8
    //   224: ldc_w 1127
    //   227: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: pop
    //   231: aload 8
    //   233: aload 11
    //   235: invokevirtual 1130	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   238: invokestatic 1136	android/database/DatabaseUtils:appendEscapedSQLString	(Ljava/lang/StringBuilder;Ljava/lang/String;)V
    //   241: goto +11 -> 252
    //   244: aload 6
    //   246: aload 10
    //   248: invokevirtual 1137	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   251: pop
    //   252: iinc 9 1
    //   255: goto -124 -> 131
    //   258: aload 8
    //   260: bipush 41
    //   262: invokevirtual 1116	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   265: pop
    //   266: aload_1
    //   267: invokestatic 882	android/content/ContentUris:parseId	(Landroid/net/Uri;)J
    //   270: lstore 12
    //   272: aload_0
    //   273: getfield 263	com/google/android/mms/pdu/PduPersister:mContext	Landroid/content/Context;
    //   276: astore 11
    //   278: aload_0
    //   279: getfield 271	com/google/android/mms/pdu/PduPersister:mContentResolver	Landroid/content/ContentResolver;
    //   282: astore 10
    //   284: new 409	java/lang/StringBuilder
    //   287: astore_2
    //   288: aload_2
    //   289: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   292: aload_2
    //   293: getstatic 1140	android/provider/Telephony$Mms:CONTENT_URI	Landroid/net/Uri;
    //   296: invokevirtual 683	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   299: pop
    //   300: aload_2
    //   301: ldc_w 1052
    //   304: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   307: pop
    //   308: aload_2
    //   309: lload 12
    //   311: invokevirtual 419	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   314: pop
    //   315: aload_2
    //   316: ldc_w 476
    //   319: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   322: pop
    //   323: aload_2
    //   324: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   327: invokestatic 426	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   330: astore 14
    //   332: aload 8
    //   334: invokevirtual 1123	java/lang/StringBuilder:length	()I
    //   337: iconst_2
    //   338: if_icmple +12 -> 350
    //   341: aload 8
    //   343: invokevirtual 422	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   346: astore_2
    //   347: goto +5 -> 352
    //   350: aconst_null
    //   351: astore_2
    //   352: aload 11
    //   354: aload 10
    //   356: aload 14
    //   358: aload_2
    //   359: aconst_null
    //   360: invokestatic 782	com/google/android/mms/util/SqliteWrapper:delete	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
    //   363: pop
    //   364: aload 6
    //   366: invokevirtual 1141	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   369: astore_2
    //   370: aload_2
    //   371: invokeinterface 614 1 0
    //   376: ifeq +23 -> 399
    //   379: aload_0
    //   380: aload_2
    //   381: invokeinterface 618 1 0
    //   386: checkcast 388	com/google/android/mms/pdu/PduPart
    //   389: lload 12
    //   391: aload_3
    //   392: invokevirtual 1040	com/google/android/mms/pdu/PduPersister:persistPart	(Lcom/google/android/mms/pdu/PduPart;JLjava/util/HashMap;)Landroid/net/Uri;
    //   395: pop
    //   396: goto -26 -> 370
    //   399: aload 4
    //   401: invokevirtual 886	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   404: invokeinterface 609 1 0
    //   409: astore 4
    //   411: aload 4
    //   413: invokeinterface 614 1 0
    //   418: ifeq +40 -> 458
    //   421: aload 4
    //   423: invokeinterface 618 1 0
    //   428: checkcast 888	java/util/Map$Entry
    //   431: astore_2
    //   432: aload_0
    //   433: aload_2
    //   434: invokeinterface 894 1 0
    //   439: checkcast 293	android/net/Uri
    //   442: aload_2
    //   443: invokeinterface 891 1 0
    //   448: checkcast 388	com/google/android/mms/pdu/PduPart
    //   451: aload_3
    //   452: invokespecial 1143	com/google/android/mms/pdu/PduPersister:updatePart	(Landroid/net/Uri;Lcom/google/android/mms/pdu/PduPart;Ljava/util/HashMap;)V
    //   455: goto -44 -> 411
    //   458: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   461: astore_2
    //   462: aload_2
    //   463: monitorenter
    //   464: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   467: aload_1
    //   468: iconst_0
    //   469: invokevirtual 872	com/google/android/mms/util/PduCache:setUpdating	(Landroid/net/Uri;Z)V
    //   472: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   475: invokevirtual 875	java/lang/Object:notifyAll	()V
    //   478: aload_2
    //   479: monitorexit
    //   480: return
    //   481: astore_1
    //   482: aload_2
    //   483: monitorexit
    //   484: aload_1
    //   485: athrow
    //   486: astore_2
    //   487: aload 4
    //   489: monitorexit
    //   490: aload_2
    //   491: athrow
    //   492: astore_3
    //   493: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   496: astore_2
    //   497: aload_2
    //   498: monitorenter
    //   499: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   502: aload_1
    //   503: iconst_0
    //   504: invokevirtual 872	com/google/android/mms/util/PduCache:setUpdating	(Landroid/net/Uri;Z)V
    //   507: getstatic 258	com/google/android/mms/pdu/PduPersister:PDU_CACHE_INSTANCE	Lcom/google/android/mms/util/PduCache;
    //   510: invokevirtual 875	java/lang/Object:notifyAll	()V
    //   513: aload_2
    //   514: monitorexit
    //   515: aload_3
    //   516: athrow
    //   517: astore_1
    //   518: aload_2
    //   519: monitorexit
    //   520: aload_1
    //   521: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	522	0	this	PduPersister
    //   0	522	1	paramUri	Uri
    //   0	522	3	paramHashMap	HashMap<Uri, InputStream>
    //   15	3	5	bool	boolean
    //   31	8	6	localInterruptedException	InterruptedException
    //   54	311	6	localObject2	Object
    //   108	28	7	i	int
    //   113	229	8	localStringBuilder	StringBuilder
    //   129	124	9	j	int
    //   144	211	10	localObject3	Object
    //   151	202	11	localObject4	Object
    //   270	120	12	l	long
    //   330	27	14	localUri	Uri
    // Exception table:
    //   from	to	target	type
    //   22	28	31	java/lang/InterruptedException
    //   464	480	481	finally
    //   482	484	481	finally
    //   8	17	486	finally
    //   22	28	486	finally
    //   33	44	486	finally
    //   44	56	486	finally
    //   61	73	486	finally
    //   73	84	486	finally
    //   487	490	486	finally
    //   0	8	492	finally
    //   84	128	492	finally
    //   138	153	492	finally
    //   158	183	492	finally
    //   186	214	492	finally
    //   214	241	492	finally
    //   244	252	492	finally
    //   258	347	492	finally
    //   352	370	492	finally
    //   370	396	492	finally
    //   399	411	492	finally
    //   411	455	492	finally
    //   490	492	492	finally
    //   499	515	517	finally
    //   518	520	517	finally
  }
}
