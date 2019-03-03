package com.android.internal.telephony;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.android.internal.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;
import java.util.zip.GZIPInputStream;

public class CarrierKeyDownloadManager
{
  private static final int[] CARRIER_KEY_TYPES = { 1, 2 };
  private static final int DAY_IN_MILLIS = 86400000;
  private static final int END_RENEWAL_WINDOW_DAYS = 7;
  private static final String INTENT_KEY_RENEWAL_ALARM_PREFIX = "com.android.internal.telephony.carrier_key_download_alarm";
  private static final String JSON_CARRIER_KEYS = "carrier-keys";
  private static final String JSON_CERTIFICATE = "certificate";
  private static final String JSON_CERTIFICATE_ALTERNATE = "public-key";
  private static final String JSON_IDENTIFIER = "key-identifier";
  private static final String JSON_TYPE = "key-type";
  private static final String JSON_TYPE_VALUE_EPDG = "EPDG";
  private static final String JSON_TYPE_VALUE_WLAN = "WLAN";
  private static final String LOG_TAG = "CarrierKeyDownloadManager";
  public static final String MCC = "MCC";
  private static final String MCC_MNC_PREF_TAG = "CARRIER_KEY_DM_MCC_MNC";
  public static final String MNC = "MNC";
  private static final String SEPARATOR = ":";
  private static final int START_RENEWAL_WINDOW_DAYS = 21;
  private static final int UNINITIALIZED_KEY_TYPE = -1;
  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      int i = mPhone.getPhoneId();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("com.android.internal.telephony.carrier_key_download_alarm");
      localStringBuilder.append(i);
      if (paramAnonymousContext.equals(localStringBuilder.toString()))
      {
        paramAnonymousIntent = new StringBuilder();
        paramAnonymousIntent.append("Handling key renewal alarm: ");
        paramAnonymousIntent.append(paramAnonymousContext);
        Log.d("CarrierKeyDownloadManager", paramAnonymousIntent.toString());
        CarrierKeyDownloadManager.this.handleAlarmOrConfigChange();
      }
      else if (paramAnonymousContext.equals("com.android.internal.telephony.ACTION_CARRIER_CERTIFICATE_DOWNLOAD"))
      {
        if (i == paramAnonymousIntent.getIntExtra("phone", -1))
        {
          paramAnonymousIntent = new StringBuilder();
          paramAnonymousIntent.append("Handling reset intent: ");
          paramAnonymousIntent.append(paramAnonymousContext);
          Log.d("CarrierKeyDownloadManager", paramAnonymousIntent.toString());
          CarrierKeyDownloadManager.this.handleAlarmOrConfigChange();
        }
      }
      else if (paramAnonymousContext.equals("android.telephony.action.CARRIER_CONFIG_CHANGED"))
      {
        if (i == paramAnonymousIntent.getIntExtra("phone", -1))
        {
          paramAnonymousIntent = new StringBuilder();
          paramAnonymousIntent.append("Carrier Config changed: ");
          paramAnonymousIntent.append(paramAnonymousContext);
          Log.d("CarrierKeyDownloadManager", paramAnonymousIntent.toString());
          CarrierKeyDownloadManager.this.handleAlarmOrConfigChange();
        }
      }
      else if (paramAnonymousContext.equals("android.intent.action.DOWNLOAD_COMPLETE"))
      {
        Log.d("CarrierKeyDownloadManager", "Download Complete");
        long l = paramAnonymousIntent.getLongExtra("extra_download_id", 0L);
        paramAnonymousContext = CarrierKeyDownloadManager.this.getMccMncSetFromPref();
        if (isValidDownload(paramAnonymousContext))
        {
          CarrierKeyDownloadManager.this.onDownloadComplete(l, paramAnonymousContext);
          CarrierKeyDownloadManager.this.onPostDownloadProcessing(l);
        }
      }
    }
  };
  private final Context mContext;
  public final DownloadManager mDownloadManager;
  @VisibleForTesting
  public int mKeyAvailability = 0;
  private final Phone mPhone;
  private String mURL;
  
  public CarrierKeyDownloadManager(Phone paramPhone)
  {
    mPhone = paramPhone;
    mContext = paramPhone.getContext();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
    localIntentFilter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("com.android.internal.telephony.carrier_key_download_alarm");
    localStringBuilder.append(mPhone.getPhoneId());
    localIntentFilter.addAction(localStringBuilder.toString());
    localIntentFilter.addAction("com.android.internal.telephony.ACTION_CARRIER_CERTIFICATE_DOWNLOAD");
    mContext.registerReceiver(mBroadcastReceiver, localIntentFilter, null, paramPhone);
    mDownloadManager = ((DownloadManager)mContext.getSystemService("download"));
  }
  
  private boolean carrierUsesKeys()
  {
    Object localObject = (CarrierConfigManager)mContext.getSystemService("carrier_config");
    if (localObject == null) {
      return false;
    }
    localObject = ((CarrierConfigManager)localObject).getConfigForSubId(mPhone.getSubId());
    if (localObject == null) {
      return false;
    }
    mKeyAvailability = ((PersistableBundle)localObject).getInt("imsi_key_availability_int");
    mURL = ((PersistableBundle)localObject).getString("imsi_key_download_url_string");
    if ((!TextUtils.isEmpty(mURL)) && (mKeyAvailability != 0))
    {
      localObject = CARRIER_KEY_TYPES;
      int i = localObject.length;
      for (int j = 0; j < i; j++) {
        if (isKeyEnabled(localObject[j])) {
          return true;
        }
      }
      return false;
    }
    Log.d("CarrierKeyDownloadManager", "Carrier not enabled or invalid values");
    return false;
  }
  
  private void cleanupDownloadPreferences(long paramLong)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Cleaning up download preferences: ");
    ((StringBuilder)localObject).append(paramLong);
    Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject).toString());
    localObject = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
    ((SharedPreferences.Editor)localObject).remove(String.valueOf(paramLong));
    ((SharedPreferences.Editor)localObject).commit();
  }
  
  private void cleanupRenewalAlarms()
  {
    Log.d("CarrierKeyDownloadManager", "Cleaning up existing renewal alarms");
    int i = mPhone.getPhoneId();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("com.android.internal.telephony.carrier_key_download_alarm");
    ((StringBuilder)localObject).append(i);
    localObject = new Intent(((StringBuilder)localObject).toString());
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(mContext, 0, (Intent)localObject, 134217728);
    Context localContext = mContext;
    localObject = mContext;
    ((AlarmManager)localContext.getSystemService("alarm")).cancel(localPendingIntent);
  }
  
  private static String convertToString(InputStream paramInputStream)
  {
    try
    {
      Object localObject1 = new java/util/zip/GZIPInputStream;
      ((GZIPInputStream)localObject1).<init>(paramInputStream);
      paramInputStream = new java/io/BufferedReader;
      Object localObject2 = new java/io/InputStreamReader;
      ((InputStreamReader)localObject2).<init>((InputStream)localObject1, StandardCharsets.UTF_8);
      paramInputStream.<init>((Reader)localObject2);
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      for (;;)
      {
        localObject1 = paramInputStream.readLine();
        if (localObject1 == null) {
          break;
        }
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append('\n');
      }
      paramInputStream = ((StringBuilder)localObject2).toString();
      return paramInputStream;
    }
    catch (IOException paramInputStream)
    {
      paramInputStream.printStackTrace();
    }
    return null;
  }
  
  private boolean downloadKey()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("starting download from: ");
    ((StringBuilder)localObject1).append(mURL);
    Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject1).toString());
    String str = getSimOperator();
    if (!TextUtils.isEmpty(str))
    {
      localObject1 = str.substring(0, 3);
      str = str.substring(3);
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("using values for mcc, mnc: ");
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(",");
      ((StringBuilder)localObject2).append(str);
      Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject2).toString());
      try
      {
        localObject2 = new android/app/DownloadManager$Request;
        ((DownloadManager.Request)localObject2).<init>(Uri.parse(mURL));
        ((DownloadManager.Request)localObject2).setAllowedOverMetered(false);
        ((DownloadManager.Request)localObject2).setVisibleInDownloadsUi(false);
        ((DownloadManager.Request)localObject2).setNotificationVisibility(2);
        long l = mDownloadManager.enqueue((DownloadManager.Request)localObject2);
        localObject2 = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        Object localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append((String)localObject1);
        ((StringBuilder)localObject3).append(":");
        ((StringBuilder)localObject3).append(str);
        localObject3 = ((StringBuilder)localObject3).toString();
        int i = mPhone.getPhoneId();
        StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
        localStringBuilder2.<init>();
        localStringBuilder2.append("storing values in sharedpref mcc, mnc, days: ");
        localStringBuilder2.append((String)localObject1);
        localStringBuilder2.append(",");
        localStringBuilder2.append(str);
        localStringBuilder2.append(",");
        localStringBuilder2.append(Long.valueOf(l));
        Log.d("CarrierKeyDownloadManager", localStringBuilder2.toString());
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("CARRIER_KEY_DM_MCC_MNC");
        ((StringBuilder)localObject1).append(i);
        ((SharedPreferences.Editor)localObject2).putString(((StringBuilder)localObject1).toString(), (String)localObject3);
        ((SharedPreferences.Editor)localObject2).commit();
        return true;
      }
      catch (Exception localException)
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("exception trying to dowload key from url: ");
        localStringBuilder1.append(mURL);
        Log.e("CarrierKeyDownloadManager", localStringBuilder1.toString());
        return false;
      }
    }
    Log.e("CarrierKeyDownloadManager", "mcc, mnc: is empty");
    return false;
  }
  
  @VisibleForTesting
  public static Pair<PublicKey, Long> getKeyInformation(byte[] paramArrayOfByte)
    throws Exception
  {
    paramArrayOfByte = new ByteArrayInputStream(paramArrayOfByte);
    paramArrayOfByte = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(paramArrayOfByte);
    return new Pair(paramArrayOfByte.getPublicKey(), Long.valueOf(paramArrayOfByte.getNotAfter().getTime()));
  }
  
  private String getMccMncSetFromPref()
  {
    int i = mPhone.getPhoneId();
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CARRIER_KEY_DM_MCC_MNC");
    localStringBuilder.append(i);
    return localSharedPreferences.getString(localStringBuilder.toString(), null);
  }
  
  private void handleAlarmOrConfigChange()
  {
    if (carrierUsesKeys())
    {
      if (areCarrierKeysAbsentOrExpiring()) {
        if (!downloadKey()) {
          resetRenewalAlarm();
        }
      }
    }
    else {
      cleanupRenewalAlarms();
    }
  }
  
  /* Error */
  private void onDownloadComplete(long paramLong, String paramString)
  {
    // Byte code:
    //   0: new 105	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   7: astore 4
    //   9: aload 4
    //   11: ldc_w 425
    //   14: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   17: pop
    //   18: aload 4
    //   20: lload_1
    //   21: invokevirtual 215	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: ldc 41
    //   27: aload 4
    //   29: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   32: invokestatic 209	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   35: pop
    //   36: new 427	android/app/DownloadManager$Query
    //   39: dup
    //   40: invokespecial 428	android/app/DownloadManager$Query:<init>	()V
    //   43: astore 4
    //   45: aload 4
    //   47: iconst_1
    //   48: newarray long
    //   50: dup
    //   51: iconst_0
    //   52: lload_1
    //   53: lastore
    //   54: invokevirtual 432	android/app/DownloadManager$Query:setFilterById	([J)Landroid/app/DownloadManager$Query;
    //   57: pop
    //   58: aload_0
    //   59: getfield 139	com/android/internal/telephony/CarrierKeyDownloadManager:mDownloadManager	Landroid/app/DownloadManager;
    //   62: aload 4
    //   64: invokevirtual 436	android/app/DownloadManager:query	(Landroid/app/DownloadManager$Query;)Landroid/database/Cursor;
    //   67: astore 5
    //   69: aconst_null
    //   70: astore 6
    //   72: aconst_null
    //   73: astore 7
    //   75: aload 5
    //   77: ifnonnull +4 -> 81
    //   80: return
    //   81: aload 5
    //   83: invokeinterface 441 1 0
    //   88: ifeq +261 -> 349
    //   91: bipush 8
    //   93: aload 5
    //   95: aload 5
    //   97: ldc_w 443
    //   100: invokeinterface 446 2 0
    //   105: invokeinterface 449 2 0
    //   110: if_icmpne +230 -> 340
    //   113: aload 7
    //   115: astore 4
    //   117: aload 6
    //   119: astore 8
    //   121: new 451	java/io/FileInputStream
    //   124: astore 9
    //   126: aload 7
    //   128: astore 4
    //   130: aload 6
    //   132: astore 8
    //   134: aload 9
    //   136: aload_0
    //   137: getfield 139	com/android/internal/telephony/CarrierKeyDownloadManager:mDownloadManager	Landroid/app/DownloadManager;
    //   140: lload_1
    //   141: invokevirtual 455	android/app/DownloadManager:openDownloadedFile	(J)Landroid/os/ParcelFileDescriptor;
    //   144: invokevirtual 461	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   147: invokespecial 464	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   150: aload 9
    //   152: astore 4
    //   154: aload 9
    //   156: astore 8
    //   158: aload_0
    //   159: aload 9
    //   161: invokestatic 466	com/android/internal/telephony/CarrierKeyDownloadManager:convertToString	(Ljava/io/InputStream;)Ljava/lang/String;
    //   164: aload_3
    //   165: invokevirtual 470	com/android/internal/telephony/CarrierKeyDownloadManager:parseJsonAndPersistKey	(Ljava/lang/String;Ljava/lang/String;)V
    //   168: aload_0
    //   169: getfield 139	com/android/internal/telephony/CarrierKeyDownloadManager:mDownloadManager	Landroid/app/DownloadManager;
    //   172: iconst_1
    //   173: newarray long
    //   175: dup
    //   176: iconst_0
    //   177: lload_1
    //   178: lastore
    //   179: invokevirtual 473	android/app/DownloadManager:remove	([J)I
    //   182: pop
    //   183: aload 9
    //   185: invokevirtual 478	java/io/InputStream:close	()V
    //   188: goto +152 -> 340
    //   191: astore_3
    //   192: aload_3
    //   193: invokevirtual 298	java/io/IOException:printStackTrace	()V
    //   196: goto +144 -> 340
    //   199: astore_3
    //   200: goto +108 -> 308
    //   203: astore_3
    //   204: aload 8
    //   206: astore 4
    //   208: new 105	java/lang/StringBuilder
    //   211: astore 9
    //   213: aload 8
    //   215: astore 4
    //   217: aload 9
    //   219: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   222: aload 8
    //   224: astore 4
    //   226: aload 9
    //   228: ldc_w 480
    //   231: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: pop
    //   235: aload 8
    //   237: astore 4
    //   239: aload 9
    //   241: lload_1
    //   242: invokevirtual 215	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   245: pop
    //   246: aload 8
    //   248: astore 4
    //   250: aload 9
    //   252: ldc_w 482
    //   255: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   258: pop
    //   259: aload 8
    //   261: astore 4
    //   263: aload 9
    //   265: aload_3
    //   266: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload 8
    //   272: astore 4
    //   274: ldc 41
    //   276: aload 9
    //   278: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   281: invokestatic 362	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   284: pop
    //   285: aload_0
    //   286: getfield 139	com/android/internal/telephony/CarrierKeyDownloadManager:mDownloadManager	Landroid/app/DownloadManager;
    //   289: iconst_1
    //   290: newarray long
    //   292: dup
    //   293: iconst_0
    //   294: lload_1
    //   295: lastore
    //   296: invokevirtual 473	android/app/DownloadManager:remove	([J)I
    //   299: pop
    //   300: aload 8
    //   302: invokevirtual 478	java/io/InputStream:close	()V
    //   305: goto -117 -> 188
    //   308: aload_0
    //   309: getfield 139	com/android/internal/telephony/CarrierKeyDownloadManager:mDownloadManager	Landroid/app/DownloadManager;
    //   312: iconst_1
    //   313: newarray long
    //   315: dup
    //   316: iconst_0
    //   317: lload_1
    //   318: lastore
    //   319: invokevirtual 473	android/app/DownloadManager:remove	([J)I
    //   322: pop
    //   323: aload 4
    //   325: invokevirtual 478	java/io/InputStream:close	()V
    //   328: goto +10 -> 338
    //   331: astore 4
    //   333: aload 4
    //   335: invokevirtual 298	java/io/IOException:printStackTrace	()V
    //   338: aload_3
    //   339: athrow
    //   340: ldc 41
    //   342: ldc_w 484
    //   345: invokestatic 209	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   348: pop
    //   349: aload 5
    //   351: invokeinterface 485 1 0
    //   356: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	357	0	this	CarrierKeyDownloadManager
    //   0	357	1	paramLong	long
    //   0	357	3	paramString	String
    //   7	317	4	localObject1	Object
    //   331	3	4	localIOException	IOException
    //   67	283	5	localCursor	android.database.Cursor
    //   70	61	6	localObject2	Object
    //   73	54	7	localObject3	Object
    //   119	182	8	localObject4	Object
    //   124	153	9	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   183	188	191	java/io/IOException
    //   300	305	191	java/io/IOException
    //   121	126	199	finally
    //   134	150	199	finally
    //   158	168	199	finally
    //   208	213	199	finally
    //   217	222	199	finally
    //   226	235	199	finally
    //   239	246	199	finally
    //   250	259	199	finally
    //   263	270	199	finally
    //   274	285	199	finally
    //   121	126	203	java/lang/Exception
    //   134	150	203	java/lang/Exception
    //   158	168	203	java/lang/Exception
    //   323	328	331	java/io/IOException
  }
  
  private void onPostDownloadProcessing(long paramLong)
  {
    resetRenewalAlarm();
    cleanupDownloadPreferences(paramLong);
  }
  
  @VisibleForTesting
  public boolean areCarrierKeysAbsentOrExpiring()
  {
    Object localObject = CARRIER_KEY_TYPES;
    int i = localObject.length;
    int j = 0;
    while (j < i)
    {
      int k = localObject[j];
      if (!isKeyEnabled(k))
      {
        j++;
      }
      else
      {
        localObject = mPhone.getCarrierInfoForImsiEncryption(k);
        boolean bool = true;
        if (localObject == null)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Key not found for: ");
          ((StringBuilder)localObject).append(k);
          Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject).toString());
          return true;
        }
        if (((ImsiEncryptionInfo)localObject).getExpirationTime().getTime() - System.currentTimeMillis() >= 1814400000L) {
          bool = false;
        }
        return bool;
      }
    }
    return false;
  }
  
  @VisibleForTesting
  public long getExpirationDate()
  {
    long l1 = Long.MAX_VALUE;
    int[] arrayOfInt = CARRIER_KEY_TYPES;
    int i = arrayOfInt.length;
    int j = 0;
    while (j < i)
    {
      int k = arrayOfInt[j];
      long l2;
      if (!isKeyEnabled(k))
      {
        l2 = l1;
      }
      else
      {
        ImsiEncryptionInfo localImsiEncryptionInfo = mPhone.getCarrierInfoForImsiEncryption(k);
        l2 = l1;
        if (localImsiEncryptionInfo != null)
        {
          l2 = l1;
          if (localImsiEncryptionInfo.getExpirationTime() != null)
          {
            l2 = l1;
            if (l1 > localImsiEncryptionInfo.getExpirationTime().getTime()) {
              l2 = localImsiEncryptionInfo.getExpirationTime().getTime();
            }
          }
        }
      }
      j++;
      l1 = l2;
    }
    if ((l1 != Long.MAX_VALUE) && (l1 >= System.currentTimeMillis() + 604800000L)) {
      l1 -= new Random().nextInt(1814400000 - 604800000) + 604800000;
    } else {
      l1 = System.currentTimeMillis() + 86400000L;
    }
    return l1;
  }
  
  @VisibleForTesting
  public String getSimOperator()
  {
    return ((TelephonyManager)mContext.getSystemService("phone")).getSimOperator(mPhone.getSubId());
  }
  
  @VisibleForTesting
  public boolean isKeyEnabled(int paramInt)
  {
    int i = mKeyAvailability;
    boolean bool = true;
    if ((i >> paramInt - 1 & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  @VisibleForTesting
  public boolean isValidDownload(String paramString)
  {
    Object localObject1 = getSimOperator();
    if ((!TextUtils.isEmpty((CharSequence)localObject1)) && (!TextUtils.isEmpty(paramString)))
    {
      Object localObject2 = paramString.split(":");
      paramString = localObject2[0];
      localObject2 = localObject2[1];
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("values from sharedPrefs mcc, mnc: ");
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append(",");
      ((StringBuilder)localObject3).append((String)localObject2);
      Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject3).toString());
      localObject3 = ((String)localObject1).substring(0, 3);
      String str = ((String)localObject1).substring(3);
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("using values for mcc, mnc: ");
      ((StringBuilder)localObject1).append((String)localObject3);
      ((StringBuilder)localObject1).append(",");
      ((StringBuilder)localObject1).append(str);
      Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject1).toString());
      return (TextUtils.equals((CharSequence)localObject2, str)) && (TextUtils.equals(paramString, (CharSequence)localObject3));
    }
    Log.e("CarrierKeyDownloadManager", "simOperator or mcc/mnc is empty");
    return false;
  }
  
  /* Error */
  @VisibleForTesting
  public void parseJsonAndPersistKey(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 197	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   4: ifne +779 -> 783
    //   7: aload_2
    //   8: invokestatic 197	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   11: ifeq +6 -> 17
    //   14: goto +769 -> 783
    //   17: aconst_null
    //   18: astore_3
    //   19: aconst_null
    //   20: astore 4
    //   22: aconst_null
    //   23: astore 5
    //   25: aconst_null
    //   26: astore 6
    //   28: aconst_null
    //   29: astore 7
    //   31: aconst_null
    //   32: astore 8
    //   34: aconst_null
    //   35: astore 9
    //   37: aload_2
    //   38: ldc 51
    //   40: invokevirtual 532	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   43: astore_2
    //   44: iconst_0
    //   45: istore 10
    //   47: aload_2
    //   48: iconst_0
    //   49: aaload
    //   50: astore 11
    //   52: aload_2
    //   53: iconst_1
    //   54: aaload
    //   55: astore 12
    //   57: new 544	org/json/JSONObject
    //   60: astore 13
    //   62: aload_3
    //   63: astore 6
    //   65: aload 4
    //   67: astore 5
    //   69: aload 8
    //   71: astore_2
    //   72: aload 13
    //   74: aload_1
    //   75: invokespecial 545	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   78: aload_3
    //   79: astore 6
    //   81: aload 4
    //   83: astore 5
    //   85: aload 8
    //   87: astore_2
    //   88: aload 13
    //   90: ldc 20
    //   92: invokevirtual 549	org/json/JSONObject:getJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   95: astore_3
    //   96: aload 9
    //   98: astore_1
    //   99: aload 13
    //   101: astore 9
    //   103: aload_1
    //   104: astore 6
    //   106: aload_1
    //   107: astore 5
    //   109: aload_1
    //   110: astore_2
    //   111: iload 10
    //   113: aload_3
    //   114: invokevirtual 554	org/json/JSONArray:length	()I
    //   117: if_icmpge +376 -> 493
    //   120: aload_1
    //   121: astore 6
    //   123: aload_1
    //   124: astore 5
    //   126: aload_1
    //   127: astore_2
    //   128: aload_3
    //   129: iload 10
    //   131: invokevirtual 558	org/json/JSONArray:getJSONObject	(I)Lorg/json/JSONObject;
    //   134: astore 4
    //   136: aload_1
    //   137: astore 6
    //   139: aload_1
    //   140: astore 5
    //   142: aload_1
    //   143: astore_2
    //   144: aload 4
    //   146: ldc 23
    //   148: invokevirtual 561	org/json/JSONObject:has	(Ljava/lang/String;)Z
    //   151: ifeq +23 -> 174
    //   154: aload_1
    //   155: astore 6
    //   157: aload_1
    //   158: astore 5
    //   160: aload_1
    //   161: astore_2
    //   162: aload 4
    //   164: ldc 23
    //   166: invokevirtual 562	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   169: astore 13
    //   171: goto +23 -> 194
    //   174: aload_1
    //   175: astore 6
    //   177: aload_1
    //   178: astore 5
    //   180: aload_1
    //   181: astore_2
    //   182: aload 4
    //   184: ldc 26
    //   186: invokevirtual 562	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   189: astore 13
    //   191: goto -20 -> 171
    //   194: aload_1
    //   195: astore 6
    //   197: aload_1
    //   198: astore 5
    //   200: aload_1
    //   201: astore_2
    //   202: aload 4
    //   204: ldc 32
    //   206: invokevirtual 562	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   209: astore 7
    //   211: iconst_m1
    //   212: istore 14
    //   214: aload_1
    //   215: astore 6
    //   217: aload_1
    //   218: astore 5
    //   220: aload_1
    //   221: astore_2
    //   222: aload 7
    //   224: ldc 38
    //   226: invokevirtual 565	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   229: ifeq +9 -> 238
    //   232: iconst_2
    //   233: istore 14
    //   235: goto +27 -> 262
    //   238: aload_1
    //   239: astore 6
    //   241: aload_1
    //   242: astore 5
    //   244: aload_1
    //   245: astore_2
    //   246: aload 7
    //   248: ldc 35
    //   250: invokevirtual 565	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   253: ifeq -18 -> 235
    //   256: iconst_1
    //   257: istore 14
    //   259: goto -24 -> 235
    //   262: aload_1
    //   263: astore 6
    //   265: aload_1
    //   266: astore 5
    //   268: aload_1
    //   269: astore_2
    //   270: aload 4
    //   272: ldc 29
    //   274: invokevirtual 562	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   277: astore 4
    //   279: aload_1
    //   280: astore 6
    //   282: aload_1
    //   283: astore 5
    //   285: aload_1
    //   286: astore_2
    //   287: new 368	java/io/ByteArrayInputStream
    //   290: astore 7
    //   292: aload_1
    //   293: astore 6
    //   295: aload_1
    //   296: astore 5
    //   298: aload_1
    //   299: astore_2
    //   300: aload 7
    //   302: aload 13
    //   304: invokevirtual 569	java/lang/String:getBytes	()[B
    //   307: invokespecial 371	java/io/ByteArrayInputStream:<init>	([B)V
    //   310: aload_1
    //   311: astore 6
    //   313: aload_1
    //   314: astore 5
    //   316: aload_1
    //   317: astore_2
    //   318: new 275	java/io/BufferedReader
    //   321: astore 8
    //   323: aload_1
    //   324: astore 6
    //   326: aload_1
    //   327: astore 5
    //   329: aload_1
    //   330: astore_2
    //   331: new 277	java/io/InputStreamReader
    //   334: astore 13
    //   336: aload_1
    //   337: astore 6
    //   339: aload_1
    //   340: astore 5
    //   342: aload_1
    //   343: astore_2
    //   344: aload 13
    //   346: aload 7
    //   348: invokespecial 570	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   351: aload_1
    //   352: astore 6
    //   354: aload_1
    //   355: astore 5
    //   357: aload_1
    //   358: astore_2
    //   359: aload 8
    //   361: aload 13
    //   363: invokespecial 289	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   366: aload_1
    //   367: astore 6
    //   369: aload_1
    //   370: astore 5
    //   372: aload_1
    //   373: astore_2
    //   374: new 572	com/android/org/bouncycastle/util/io/pem/PemReader
    //   377: astore 13
    //   379: aload_1
    //   380: astore 6
    //   382: aload_1
    //   383: astore 5
    //   385: aload_1
    //   386: astore_2
    //   387: aload 13
    //   389: aload 8
    //   391: invokespecial 573	com/android/org/bouncycastle/util/io/pem/PemReader:<init>	(Ljava/io/Reader;)V
    //   394: aload 13
    //   396: astore_1
    //   397: aload_1
    //   398: invokevirtual 577	com/android/org/bouncycastle/util/io/pem/PemReader:readPemObject	()Lcom/android/org/bouncycastle/util/io/pem/PemObject;
    //   401: invokevirtual 582	com/android/org/bouncycastle/util/io/pem/PemObject:getContent	()[B
    //   404: invokestatic 584	com/android/internal/telephony/CarrierKeyDownloadManager:getKeyInformation	([B)Landroid/util/Pair;
    //   407: astore_2
    //   408: aload_1
    //   409: invokevirtual 585	com/android/org/bouncycastle/util/io/pem/PemReader:close	()V
    //   412: aload_2
    //   413: getfield 589	android/util/Pair:first	Ljava/lang/Object;
    //   416: checkcast 591	java/security/PublicKey
    //   419: astore 5
    //   421: aload_0
    //   422: aload 5
    //   424: iload 14
    //   426: aload 4
    //   428: aload_2
    //   429: getfield 594	android/util/Pair:second	Ljava/lang/Object;
    //   432: checkcast 347	java/lang/Long
    //   435: invokevirtual 597	java/lang/Long:longValue	()J
    //   438: aload 11
    //   440: aload 12
    //   442: invokevirtual 601	com/android/internal/telephony/CarrierKeyDownloadManager:savePublicKey	(Ljava/security/PublicKey;ILjava/lang/String;JLjava/lang/String;Ljava/lang/String;)V
    //   445: iinc 10 1
    //   448: goto -345 -> 103
    //   451: astore 9
    //   453: aload_1
    //   454: astore_2
    //   455: aload 9
    //   457: astore_1
    //   458: goto +277 -> 735
    //   461: astore 9
    //   463: goto +122 -> 585
    //   466: astore 9
    //   468: goto +188 -> 656
    //   471: astore_2
    //   472: aload_1
    //   473: astore 9
    //   475: aload_2
    //   476: astore_1
    //   477: aload 9
    //   479: astore_2
    //   480: goto +255 -> 735
    //   483: astore 9
    //   485: goto +100 -> 585
    //   488: astore 9
    //   490: goto +166 -> 656
    //   493: aload_1
    //   494: ifnull +46 -> 540
    //   497: aload_1
    //   498: invokevirtual 585	com/android/org/bouncycastle/util/io/pem/PemReader:close	()V
    //   501: goto +39 -> 540
    //   504: astore_1
    //   505: new 105	java/lang/StringBuilder
    //   508: dup
    //   509: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   512: astore_2
    //   513: aload_2
    //   514: ldc_w 603
    //   517: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   520: pop
    //   521: aload_2
    //   522: aload_1
    //   523: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   526: pop
    //   527: ldc 41
    //   529: aload_2
    //   530: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   533: invokestatic 362	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   536: pop
    //   537: goto +193 -> 730
    //   540: goto +190 -> 730
    //   543: astore 9
    //   545: aload 6
    //   547: astore_1
    //   548: goto +37 -> 585
    //   551: astore 9
    //   553: aload 5
    //   555: astore_1
    //   556: goto +100 -> 656
    //   559: astore_1
    //   560: aload 5
    //   562: astore_2
    //   563: goto +19 -> 582
    //   566: astore 9
    //   568: aload 6
    //   570: astore_1
    //   571: goto +14 -> 585
    //   574: astore 9
    //   576: aload 7
    //   578: astore_1
    //   579: goto +77 -> 656
    //   582: goto +153 -> 735
    //   585: aload_1
    //   586: astore_2
    //   587: new 105	java/lang/StringBuilder
    //   590: astore 5
    //   592: aload_1
    //   593: astore_2
    //   594: aload 5
    //   596: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   599: aload_1
    //   600: astore_2
    //   601: aload 5
    //   603: ldc_w 603
    //   606: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   609: pop
    //   610: aload_1
    //   611: astore_2
    //   612: aload 5
    //   614: aload 9
    //   616: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   619: pop
    //   620: aload_1
    //   621: astore_2
    //   622: ldc 41
    //   624: aload 5
    //   626: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   629: invokestatic 362	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   632: pop
    //   633: aload_1
    //   634: ifnull -94 -> 540
    //   637: aload_1
    //   638: invokevirtual 585	com/android/org/bouncycastle/util/io/pem/PemReader:close	()V
    //   641: goto -101 -> 540
    //   644: astore_1
    //   645: new 105	java/lang/StringBuilder
    //   648: dup
    //   649: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   652: astore_2
    //   653: goto -140 -> 513
    //   656: aload_1
    //   657: astore_2
    //   658: new 105	java/lang/StringBuilder
    //   661: astore 5
    //   663: aload_1
    //   664: astore_2
    //   665: aload 5
    //   667: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   670: aload_1
    //   671: astore_2
    //   672: aload 5
    //   674: ldc_w 605
    //   677: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   680: pop
    //   681: aload_1
    //   682: astore_2
    //   683: aload 5
    //   685: aload 9
    //   687: invokevirtual 608	org/json/JSONException:getMessage	()Ljava/lang/String;
    //   690: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   693: pop
    //   694: aload_1
    //   695: astore_2
    //   696: ldc 41
    //   698: aload 5
    //   700: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   703: invokestatic 362	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   706: pop
    //   707: aload_1
    //   708: ifnull -168 -> 540
    //   711: aload_1
    //   712: invokevirtual 585	com/android/org/bouncycastle/util/io/pem/PemReader:close	()V
    //   715: goto -175 -> 540
    //   718: astore_1
    //   719: new 105	java/lang/StringBuilder
    //   722: dup
    //   723: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   726: astore_2
    //   727: goto -214 -> 513
    //   730: return
    //   731: astore_1
    //   732: goto -150 -> 582
    //   735: aload_2
    //   736: ifnull +45 -> 781
    //   739: aload_2
    //   740: invokevirtual 585	com/android/org/bouncycastle/util/io/pem/PemReader:close	()V
    //   743: goto +38 -> 781
    //   746: astore 9
    //   748: new 105	java/lang/StringBuilder
    //   751: dup
    //   752: invokespecial 106	java/lang/StringBuilder:<init>	()V
    //   755: astore_2
    //   756: aload_2
    //   757: ldc_w 603
    //   760: invokevirtual 110	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   763: pop
    //   764: aload_2
    //   765: aload 9
    //   767: invokevirtual 353	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   770: pop
    //   771: ldc 41
    //   773: aload_2
    //   774: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   777: invokestatic 362	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   780: pop
    //   781: aload_1
    //   782: athrow
    //   783: ldc 41
    //   785: ldc_w 610
    //   788: invokestatic 362	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   791: pop
    //   792: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	793	0	this	CarrierKeyDownloadManager
    //   0	793	1	paramString1	String
    //   0	793	2	paramString2	String
    //   18	111	3	localJSONArray	org.json.JSONArray
    //   20	407	4	localObject1	Object
    //   23	676	5	localObject2	Object
    //   26	543	6	localObject3	Object
    //   29	548	7	localObject4	Object
    //   32	358	8	localBufferedReader	BufferedReader
    //   35	67	9	localObject5	Object
    //   451	5	9	localObject6	Object
    //   461	1	9	localException1	Exception
    //   466	1	9	localJSONException1	org.json.JSONException
    //   473	5	9	str1	String
    //   483	1	9	localException2	Exception
    //   488	1	9	localJSONException2	org.json.JSONException
    //   543	1	9	localException3	Exception
    //   551	1	9	localJSONException3	org.json.JSONException
    //   566	1	9	localException4	Exception
    //   574	112	9	localJSONException4	org.json.JSONException
    //   746	20	9	localException5	Exception
    //   45	401	10	i	int
    //   50	389	11	str2	String
    //   55	386	12	str3	String
    //   60	335	13	localObject7	Object
    //   212	213	14	j	int
    // Exception table:
    //   from	to	target	type
    //   421	445	451	finally
    //   421	445	461	java/lang/Exception
    //   421	445	466	org/json/JSONException
    //   397	421	471	finally
    //   397	421	483	java/lang/Exception
    //   397	421	488	org/json/JSONException
    //   497	501	504	java/lang/Exception
    //   72	78	543	java/lang/Exception
    //   88	96	543	java/lang/Exception
    //   111	120	543	java/lang/Exception
    //   128	136	543	java/lang/Exception
    //   144	154	543	java/lang/Exception
    //   162	171	543	java/lang/Exception
    //   182	191	543	java/lang/Exception
    //   202	211	543	java/lang/Exception
    //   222	232	543	java/lang/Exception
    //   246	256	543	java/lang/Exception
    //   270	279	543	java/lang/Exception
    //   287	292	543	java/lang/Exception
    //   300	310	543	java/lang/Exception
    //   318	323	543	java/lang/Exception
    //   331	336	543	java/lang/Exception
    //   344	351	543	java/lang/Exception
    //   359	366	543	java/lang/Exception
    //   374	379	543	java/lang/Exception
    //   387	394	543	java/lang/Exception
    //   72	78	551	org/json/JSONException
    //   88	96	551	org/json/JSONException
    //   111	120	551	org/json/JSONException
    //   128	136	551	org/json/JSONException
    //   144	154	551	org/json/JSONException
    //   162	171	551	org/json/JSONException
    //   182	191	551	org/json/JSONException
    //   202	211	551	org/json/JSONException
    //   222	232	551	org/json/JSONException
    //   246	256	551	org/json/JSONException
    //   270	279	551	org/json/JSONException
    //   287	292	551	org/json/JSONException
    //   300	310	551	org/json/JSONException
    //   318	323	551	org/json/JSONException
    //   331	336	551	org/json/JSONException
    //   344	351	551	org/json/JSONException
    //   359	366	551	org/json/JSONException
    //   374	379	551	org/json/JSONException
    //   387	394	551	org/json/JSONException
    //   37	44	559	finally
    //   57	62	559	finally
    //   37	44	566	java/lang/Exception
    //   57	62	566	java/lang/Exception
    //   37	44	574	org/json/JSONException
    //   57	62	574	org/json/JSONException
    //   637	641	644	java/lang/Exception
    //   711	715	718	java/lang/Exception
    //   72	78	731	finally
    //   88	96	731	finally
    //   111	120	731	finally
    //   128	136	731	finally
    //   144	154	731	finally
    //   162	171	731	finally
    //   182	191	731	finally
    //   202	211	731	finally
    //   222	232	731	finally
    //   246	256	731	finally
    //   270	279	731	finally
    //   287	292	731	finally
    //   300	310	731	finally
    //   318	323	731	finally
    //   331	336	731	finally
    //   344	351	731	finally
    //   359	366	731	finally
    //   374	379	731	finally
    //   387	394	731	finally
    //   587	592	731	finally
    //   594	599	731	finally
    //   601	610	731	finally
    //   612	620	731	finally
    //   622	633	731	finally
    //   658	663	731	finally
    //   665	670	731	finally
    //   672	681	731	finally
    //   683	694	731	finally
    //   696	707	731	finally
    //   739	743	746	java/lang/Exception
  }
  
  @VisibleForTesting
  public void resetRenewalAlarm()
  {
    cleanupRenewalAlarms();
    int i = mPhone.getPhoneId();
    long l = getExpirationDate();
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("minExpirationDate: ");
    ((StringBuilder)localObject1).append(new Date(l));
    Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject1).toString());
    Object localObject2 = (AlarmManager)mContext.getSystemService("alarm");
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("com.android.internal.telephony.carrier_key_download_alarm");
    ((StringBuilder)localObject1).append(i);
    localObject1 = new Intent(((StringBuilder)localObject1).toString());
    ((AlarmManager)localObject2).set(2, l, PendingIntent.getBroadcast(mContext, 0, (Intent)localObject1, 134217728));
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("setRenewelAlarm: action=");
    ((StringBuilder)localObject2).append(((Intent)localObject1).getAction());
    ((StringBuilder)localObject2).append(" time=");
    ((StringBuilder)localObject2).append(new Date(l));
    Log.d("CarrierKeyDownloadManager", ((StringBuilder)localObject2).toString());
  }
  
  @VisibleForTesting
  public void savePublicKey(PublicKey paramPublicKey, int paramInt, String paramString1, long paramLong, String paramString2, String paramString3)
  {
    paramPublicKey = new ImsiEncryptionInfo(paramString2, paramString3, paramInt, paramString1, paramPublicKey, new Date(paramLong));
    mPhone.setCarrierInfoForImsiEncryption(paramPublicKey);
  }
}
