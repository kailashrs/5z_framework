package com.android.internal.telephony;

import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SmsNumberUtils
{
  private static int[] ALL_COUNTRY_CODES = null;
  private static final int CDMA_HOME_NETWORK = 1;
  private static final int CDMA_ROAMING_NETWORK = 2;
  private static final boolean DBG = Build.IS_DEBUGGABLE;
  private static final int GSM_UMTS_NETWORK = 0;
  private static HashMap<String, ArrayList<String>> IDDS_MAPS = new HashMap();
  private static int MAX_COUNTRY_CODES_LENGTH = 0;
  private static final int MIN_COUNTRY_AREA_LOCAL_LENGTH = 10;
  private static final int NANP_CC = 1;
  private static final String NANP_IDD = "011";
  private static final int NANP_LONG_LENGTH = 11;
  private static final int NANP_MEDIUM_LENGTH = 10;
  private static final String NANP_NDD = "1";
  private static final int NANP_SHORT_LENGTH = 7;
  private static final int NP_CC_AREA_LOCAL = 104;
  private static final int NP_HOMEIDD_CC_AREA_LOCAL = 101;
  private static final int NP_INTERNATIONAL_BEGIN = 100;
  private static final int NP_LOCALIDD_CC_AREA_LOCAL = 103;
  private static final int NP_NANP_AREA_LOCAL = 2;
  private static final int NP_NANP_BEGIN = 1;
  private static final int NP_NANP_LOCAL = 1;
  private static final int NP_NANP_LOCALIDD_CC_AREA_LOCAL = 5;
  private static final int NP_NANP_NBPCD_CC_AREA_LOCAL = 4;
  private static final int NP_NANP_NBPCD_HOMEIDD_CC_AREA_LOCAL = 6;
  private static final int NP_NANP_NDD_AREA_LOCAL = 3;
  private static final int NP_NBPCD_CC_AREA_LOCAL = 102;
  private static final int NP_NBPCD_HOMEIDD_CC_AREA_LOCAL = 100;
  private static final int NP_NONE = 0;
  private static final String PLUS_SIGN = "+";
  private static final String TAG = "SmsNumberUtils";
  
  public SmsNumberUtils() {}
  
  private static int checkInternationalNumberPlan(Context paramContext, NumberEntry paramNumberEntry, ArrayList<String> paramArrayList, String paramString)
  {
    String str = number;
    int i;
    if (str.startsWith("+"))
    {
      paramArrayList = str.substring(1);
      if (paramArrayList.startsWith(paramString))
      {
        i = getCountryCode(paramContext, paramArrayList.substring(paramString.length()));
        if (i > 0)
        {
          countryCode = i;
          return 100;
        }
      }
      else
      {
        i = getCountryCode(paramContext, paramArrayList);
        if (i > 0)
        {
          countryCode = i;
          return 102;
        }
      }
    }
    else if (str.startsWith(paramString))
    {
      i = getCountryCode(paramContext, str.substring(paramString.length()));
      if (i > 0)
      {
        countryCode = i;
        return 101;
      }
    }
    else
    {
      paramString = paramArrayList.iterator();
      while (paramString.hasNext())
      {
        paramArrayList = (String)paramString.next();
        if (str.startsWith(paramArrayList))
        {
          i = getCountryCode(paramContext, str.substring(paramArrayList.length()));
          if (i > 0)
          {
            countryCode = i;
            IDD = paramArrayList;
            return 103;
          }
        }
      }
      if (!str.startsWith("0"))
      {
        i = getCountryCode(paramContext, str);
        if (i > 0)
        {
          countryCode = i;
          return 104;
        }
      }
    }
    return 0;
  }
  
  private static int checkNANP(NumberEntry paramNumberEntry, ArrayList<String> paramArrayList)
  {
    int i = 0;
    String str1 = number;
    if (str1.length() == 7)
    {
      int j = str1.charAt(0);
      int k = i;
      if (j >= 50)
      {
        k = i;
        if (j <= 57)
        {
          j = 1;
          for (i = 1;; i++)
          {
            k = j;
            if (i >= 7) {
              break;
            }
            if (!PhoneNumberUtils.isISODigit(str1.charAt(i)))
            {
              k = 0;
              break;
            }
          }
        }
      }
      if (k != 0) {
        return 1;
      }
    }
    else if (str1.length() == 10)
    {
      if (isNANP(str1)) {
        return 2;
      }
    }
    else if (str1.length() == 11)
    {
      if (isNANP(str1)) {
        return 3;
      }
    }
    else if (str1.startsWith("+"))
    {
      paramNumberEntry = str1.substring(1);
      if (paramNumberEntry.length() == 11)
      {
        if (isNANP(paramNumberEntry)) {
          return 4;
        }
      }
      else if ((paramNumberEntry.startsWith("011")) && (paramNumberEntry.length() == 14) && (isNANP(paramNumberEntry.substring(3)))) {
        return 6;
      }
    }
    else
    {
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        String str2 = (String)localIterator.next();
        if (str1.startsWith(str2))
        {
          paramArrayList = str1.substring(str2.length());
          if ((paramArrayList != null) && (paramArrayList.startsWith(String.valueOf(1))) && (isNANP(paramArrayList)))
          {
            IDD = str2;
            return 5;
          }
        }
      }
    }
    return 0;
  }
  
  private static boolean compareGid1(Phone paramPhone, String paramString)
  {
    paramPhone = paramPhone.getGroupIdLevel1();
    boolean bool = true;
    if (TextUtils.isEmpty(paramString))
    {
      if (DBG)
      {
        paramPhone = new StringBuilder();
        paramPhone.append("compareGid1 serviceGid is empty, return ");
        paramPhone.append(true);
        Rlog.d("SmsNumberUtils", paramPhone.toString());
      }
      return true;
    }
    int i = paramString.length();
    if ((paramPhone == null) || (paramPhone.length() < i) || (!paramPhone.substring(0, i).equalsIgnoreCase(paramString)))
    {
      if (DBG)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(" gid1 ");
        localStringBuilder.append(paramPhone);
        localStringBuilder.append(" serviceGid1 ");
        localStringBuilder.append(paramString);
        Rlog.d("SmsNumberUtils", localStringBuilder.toString());
      }
      bool = false;
    }
    if (DBG)
    {
      paramString = new StringBuilder();
      paramString.append("compareGid1 is ");
      if (bool) {
        paramPhone = "Same";
      } else {
        paramPhone = "Different";
      }
      paramString.append(paramPhone);
      Rlog.d("SmsNumberUtils", paramString.toString());
    }
    return bool;
  }
  
  public static String filterDestAddr(Phone paramPhone, String paramString)
  {
    Object localObject;
    if (DBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("enter filterDestAddr. destAddr=\"");
      ((StringBuilder)localObject).append(Rlog.pii("SmsNumberUtils", paramString));
      ((StringBuilder)localObject).append("\"");
      Rlog.d("SmsNumberUtils", ((StringBuilder)localObject).toString());
    }
    if ((paramString != null) && (PhoneNumberUtils.isGlobalPhoneNumber(paramString)))
    {
      String str = TelephonyManager.from(paramPhone.getContext()).getNetworkOperator(paramPhone.getSubId());
      StringBuilder localStringBuilder = null;
      localObject = localStringBuilder;
      if (needToConvert(paramPhone))
      {
        int i = getNetworkType(paramPhone);
        localObject = localStringBuilder;
        if (i != -1)
        {
          localObject = localStringBuilder;
          if (!TextUtils.isEmpty(str))
          {
            str = str.substring(0, 3);
            localObject = localStringBuilder;
            if (str != null)
            {
              localObject = localStringBuilder;
              if (str.trim().length() > 0) {
                localObject = formatNumber(paramPhone.getContext(), paramString, str, i);
              }
            }
          }
        }
      }
      if (DBG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("destAddr is ");
        if (localObject != null) {
          paramPhone = "formatted.";
        } else {
          paramPhone = "not formatted.";
        }
        localStringBuilder.append(paramPhone);
        Rlog.d("SmsNumberUtils", localStringBuilder.toString());
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("leave filterDestAddr, new destAddr=\"");
        if (localObject != null) {
          paramPhone = Rlog.pii("SmsNumberUtils", localObject);
        } else {
          paramPhone = Rlog.pii("SmsNumberUtils", paramString);
        }
        localStringBuilder.append(paramPhone);
        localStringBuilder.append("\"");
        Rlog.d("SmsNumberUtils", localStringBuilder.toString());
      }
      if (localObject != null) {
        paramString = (String)localObject;
      }
      return paramString;
    }
    paramPhone = new StringBuilder();
    paramPhone.append("destAddr");
    paramPhone.append(Rlog.pii("SmsNumberUtils", paramString));
    paramPhone.append(" is not a global phone number! Nothing changed.");
    Rlog.w("SmsNumberUtils", paramPhone.toString());
    return paramString;
  }
  
  private static String formatNumber(Context paramContext, String paramString1, String paramString2, int paramInt)
  {
    if (paramString1 != null)
    {
      if ((paramString2 != null) && (paramString2.trim().length() != 0))
      {
        paramString1 = PhoneNumberUtils.extractNetworkPortion(paramString1);
        if ((paramString1 != null) && (paramString1.length() != 0))
        {
          NumberEntry localNumberEntry = new NumberEntry(paramString1);
          paramString2 = getAllIDDs(paramContext, paramString2);
          int i = checkNANP(localNumberEntry, paramString2);
          if (DBG)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("NANP type: ");
            localStringBuilder.append(getNumberPlanType(i));
            Rlog.d("SmsNumberUtils", localStringBuilder.toString());
          }
          if ((i != 1) && (i != 2) && (i != 3))
          {
            if (i == 4)
            {
              if ((paramInt != 1) && (paramInt != 2)) {
                return paramString1;
              }
              return paramString1.substring(1);
            }
            int j = 0;
            int k = 0;
            int m = 0;
            if (i == 5)
            {
              if (paramInt == 1) {
                return paramString1;
              }
              if (paramInt == 0)
              {
                paramInt = m;
                if (IDD != null) {
                  paramInt = IDD.length();
                }
                paramContext = new StringBuilder();
                paramContext.append("+");
                paramContext.append(paramString1.substring(paramInt));
                return paramContext.toString();
              }
              if (paramInt == 2)
              {
                paramInt = j;
                if (IDD != null) {
                  paramInt = IDD.length();
                }
                return paramString1.substring(paramInt);
              }
            }
            m = checkInternationalNumberPlan(paramContext, localNumberEntry, paramString2, "011");
            if (DBG)
            {
              paramContext = new StringBuilder();
              paramContext.append("International type: ");
              paramContext.append(getNumberPlanType(m));
              Rlog.d("SmsNumberUtils", paramContext.toString());
            }
            paramString2 = null;
            switch (m)
            {
            default: 
              paramContext = paramString2;
              if (!paramString1.startsWith("+")) {
                break label607;
              }
              if (paramInt != 1)
              {
                paramContext = paramString2;
                if (paramInt != 2) {
                  break label607;
                }
              }
              break;
            case 104: 
              paramInt = countryCode;
              paramContext = paramString2;
              if (inExceptionListForNpCcAreaLocal(localNumberEntry)) {
                break label607;
              }
              paramContext = paramString2;
              if (paramString1.length() < 11) {
                break label607;
              }
              paramContext = paramString2;
              if (paramInt == 1) {
                break label607;
              }
              paramContext = new StringBuilder();
              paramContext.append("011");
              paramContext.append(paramString1);
              paramContext = paramContext.toString();
              break;
            case 103: 
              if (paramInt != 0)
              {
                paramContext = paramString2;
                if (paramInt != 2) {}
              }
              else
              {
                paramInt = k;
                if (IDD != null) {
                  paramInt = IDD.length();
                }
                paramContext = new StringBuilder();
                paramContext.append("011");
                paramContext.append(paramString1.substring(paramInt));
                paramContext = paramContext.toString();
              }
              break;
            case 102: 
              paramContext = new StringBuilder();
              paramContext.append("011");
              paramContext.append(paramString1.substring(1));
              paramContext = paramContext.toString();
              break;
            case 101: 
              paramContext = paramString1;
              break;
            case 100: 
              paramContext = paramString2;
              if (paramInt != 0) {
                break label607;
              }
              paramContext = paramString1.substring(1);
              break;
            }
            if (paramString1.startsWith("+011"))
            {
              paramContext = paramString1.substring(1);
            }
            else
            {
              paramContext = new StringBuilder();
              paramContext.append("011");
              paramContext.append(paramString1.substring(1));
              paramContext = paramContext.toString();
            }
            label607:
            paramString2 = paramContext;
            if (paramContext == null) {
              paramString2 = paramString1;
            }
            return paramString2;
          }
          return paramString1;
        }
        throw new IllegalArgumentException("Number is invalid!");
      }
      throw new IllegalArgumentException("activeMcc is null or empty!");
    }
    throw new IllegalArgumentException("number is null");
  }
  
  /* Error */
  private static int[] getAllCountryCodes(Context paramContext)
  {
    // Byte code:
    //   0: getstatic 77	com/android/internal/telephony/SmsNumberUtils:ALL_COUNTRY_CODES	[I
    //   3: ifnull +7 -> 10
    //   6: getstatic 77	com/android/internal/telephony/SmsNumberUtils:ALL_COUNTRY_CODES	[I
    //   9: areturn
    //   10: aconst_null
    //   11: astore_1
    //   12: aconst_null
    //   13: astore_2
    //   14: aload_0
    //   15: invokevirtual 311	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   18: getstatic 317	com/android/internal/telephony/HbpcdLookup$MccLookup:CONTENT_URI	Landroid/net/Uri;
    //   21: iconst_1
    //   22: anewarray 93	java/lang/String
    //   25: dup
    //   26: iconst_0
    //   27: ldc_w 319
    //   30: aastore
    //   31: aconst_null
    //   32: aconst_null
    //   33: aconst_null
    //   34: invokevirtual 325	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   37: astore_0
    //   38: aload_0
    //   39: astore_2
    //   40: aload_0
    //   41: astore_1
    //   42: aload_0
    //   43: invokeinterface 330 1 0
    //   48: ifle +101 -> 149
    //   51: aload_0
    //   52: astore_2
    //   53: aload_0
    //   54: astore_1
    //   55: aload_0
    //   56: invokeinterface 330 1 0
    //   61: newarray int
    //   63: putstatic 77	com/android/internal/telephony/SmsNumberUtils:ALL_COUNTRY_CODES	[I
    //   66: iconst_0
    //   67: istore_3
    //   68: aload_0
    //   69: astore_2
    //   70: aload_0
    //   71: astore_1
    //   72: aload_0
    //   73: invokeinterface 333 1 0
    //   78: ifeq +71 -> 149
    //   81: aload_0
    //   82: astore_2
    //   83: aload_0
    //   84: astore_1
    //   85: aload_0
    //   86: iconst_0
    //   87: invokeinterface 337 2 0
    //   92: istore 4
    //   94: aload_0
    //   95: astore_2
    //   96: aload_0
    //   97: astore_1
    //   98: getstatic 77	com/android/internal/telephony/SmsNumberUtils:ALL_COUNTRY_CODES	[I
    //   101: iload_3
    //   102: iload 4
    //   104: iastore
    //   105: aload_0
    //   106: astore_2
    //   107: aload_0
    //   108: astore_1
    //   109: iload 4
    //   111: invokestatic 153	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   114: invokevirtual 245	java/lang/String:trim	()Ljava/lang/String;
    //   117: invokevirtual 105	java/lang/String:length	()I
    //   120: istore 4
    //   122: aload_0
    //   123: astore_2
    //   124: aload_0
    //   125: astore_1
    //   126: iload 4
    //   128: getstatic 339	com/android/internal/telephony/SmsNumberUtils:MAX_COUNTRY_CODES_LENGTH	I
    //   131: if_icmple +12 -> 143
    //   134: aload_0
    //   135: astore_2
    //   136: aload_0
    //   137: astore_1
    //   138: iload 4
    //   140: putstatic 339	com/android/internal/telephony/SmsNumberUtils:MAX_COUNTRY_CODES_LENGTH	I
    //   143: iinc 3 1
    //   146: goto -78 -> 68
    //   149: aload_0
    //   150: ifnull +38 -> 188
    //   153: aload_0
    //   154: invokeinterface 342 1 0
    //   159: goto +29 -> 188
    //   162: astore_0
    //   163: goto +29 -> 192
    //   166: astore_0
    //   167: aload_1
    //   168: astore_2
    //   169: ldc 66
    //   171: ldc_w 344
    //   174: aload_0
    //   175: invokestatic 348	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   178: pop
    //   179: aload_1
    //   180: ifnull +8 -> 188
    //   183: aload_1
    //   184: astore_0
    //   185: goto -32 -> 153
    //   188: getstatic 77	com/android/internal/telephony/SmsNumberUtils:ALL_COUNTRY_CODES	[I
    //   191: areturn
    //   192: aload_2
    //   193: ifnull +9 -> 202
    //   196: aload_2
    //   197: invokeinterface 342 1 0
    //   202: aload_0
    //   203: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	204	0	paramContext	Context
    //   11	173	1	localContext1	Context
    //   13	184	2	localContext2	Context
    //   67	77	3	i	int
    //   92	47	4	j	int
    // Exception table:
    //   from	to	target	type
    //   14	38	162	finally
    //   42	51	162	finally
    //   55	66	162	finally
    //   72	81	162	finally
    //   85	94	162	finally
    //   98	105	162	finally
    //   109	122	162	finally
    //   126	134	162	finally
    //   138	143	162	finally
    //   169	179	162	finally
    //   14	38	166	android/database/SQLException
    //   42	51	166	android/database/SQLException
    //   55	66	166	android/database/SQLException
    //   72	81	166	android/database/SQLException
    //   85	94	166	android/database/SQLException
    //   98	105	166	android/database/SQLException
    //   109	122	166	android/database/SQLException
    //   126	134	166	android/database/SQLException
    //   138	143	166	android/database/SQLException
  }
  
  /* Error */
  private static ArrayList<String> getAllIDDs(Context paramContext, String paramString)
  {
    // Byte code:
    //   0: getstatic 84	com/android/internal/telephony/SmsNumberUtils:IDDS_MAPS	Ljava/util/HashMap;
    //   3: aload_1
    //   4: invokevirtual 352	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   7: checkcast 114	java/util/ArrayList
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull +5 -> 17
    //   15: aload_2
    //   16: areturn
    //   17: new 114	java/util/ArrayList
    //   20: dup
    //   21: invokespecial 353	java/util/ArrayList:<init>	()V
    //   24: astore_3
    //   25: aconst_null
    //   26: astore 4
    //   28: aconst_null
    //   29: astore 5
    //   31: aload_1
    //   32: ifnull +18 -> 50
    //   35: ldc_w 355
    //   38: astore 4
    //   40: iconst_1
    //   41: anewarray 93	java/lang/String
    //   44: dup
    //   45: iconst_0
    //   46: aload_1
    //   47: aastore
    //   48: astore 5
    //   50: aconst_null
    //   51: astore_2
    //   52: aconst_null
    //   53: astore 6
    //   55: aload_0
    //   56: invokevirtual 311	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   59: getstatic 358	com/android/internal/telephony/HbpcdLookup$MccIdd:CONTENT_URI	Landroid/net/Uri;
    //   62: iconst_2
    //   63: anewarray 93	java/lang/String
    //   66: dup
    //   67: iconst_0
    //   68: ldc_w 359
    //   71: aastore
    //   72: dup
    //   73: iconst_1
    //   74: ldc_w 361
    //   77: aastore
    //   78: aload 4
    //   80: aload 5
    //   82: aconst_null
    //   83: invokevirtual 325	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   86: astore_0
    //   87: aload_0
    //   88: astore 6
    //   90: aload_0
    //   91: astore_2
    //   92: aload_0
    //   93: invokeinterface 330 1 0
    //   98: ifle +60 -> 158
    //   101: aload_0
    //   102: astore 6
    //   104: aload_0
    //   105: astore_2
    //   106: aload_0
    //   107: invokeinterface 333 1 0
    //   112: ifeq +46 -> 158
    //   115: aload_0
    //   116: astore 6
    //   118: aload_0
    //   119: astore_2
    //   120: aload_0
    //   121: iconst_0
    //   122: invokeinterface 364 2 0
    //   127: astore 5
    //   129: aload_0
    //   130: astore 6
    //   132: aload_0
    //   133: astore_2
    //   134: aload_3
    //   135: aload 5
    //   137: invokevirtual 368	java/util/ArrayList:contains	(Ljava/lang/Object;)Z
    //   140: ifne +15 -> 155
    //   143: aload_0
    //   144: astore 6
    //   146: aload_0
    //   147: astore_2
    //   148: aload_3
    //   149: aload 5
    //   151: invokevirtual 371	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   154: pop
    //   155: goto -54 -> 101
    //   158: aload_0
    //   159: ifnull +39 -> 198
    //   162: aload_0
    //   163: invokeinterface 342 1 0
    //   168: goto +30 -> 198
    //   171: astore_0
    //   172: goto +89 -> 261
    //   175: astore_0
    //   176: aload_2
    //   177: astore 6
    //   179: ldc 66
    //   181: ldc_w 344
    //   184: aload_0
    //   185: invokestatic 348	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   188: pop
    //   189: aload_2
    //   190: ifnull +8 -> 198
    //   193: aload_2
    //   194: astore_0
    //   195: goto -33 -> 162
    //   198: getstatic 84	com/android/internal/telephony/SmsNumberUtils:IDDS_MAPS	Ljava/util/HashMap;
    //   201: aload_1
    //   202: aload_3
    //   203: invokevirtual 375	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   206: pop
    //   207: getstatic 75	com/android/internal/telephony/SmsNumberUtils:DBG	Z
    //   210: ifeq +49 -> 259
    //   213: new 170	java/lang/StringBuilder
    //   216: dup
    //   217: invokespecial 171	java/lang/StringBuilder:<init>	()V
    //   220: astore_0
    //   221: aload_0
    //   222: ldc_w 377
    //   225: invokevirtual 177	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   228: pop
    //   229: aload_0
    //   230: aload_1
    //   231: invokevirtual 177	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: pop
    //   235: aload_0
    //   236: ldc_w 379
    //   239: invokevirtual 177	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: pop
    //   243: aload_0
    //   244: aload_3
    //   245: invokevirtual 382	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   248: pop
    //   249: ldc 66
    //   251: aload_0
    //   252: invokevirtual 183	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   255: invokestatic 189	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   258: pop
    //   259: aload_3
    //   260: areturn
    //   261: aload 6
    //   263: ifnull +10 -> 273
    //   266: aload 6
    //   268: invokeinterface 342 1 0
    //   273: aload_0
    //   274: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	275	0	paramContext	Context
    //   0	275	1	paramString	String
    //   10	184	2	localObject1	Object
    //   24	236	3	localArrayList	ArrayList
    //   26	53	4	str	String
    //   29	121	5	localObject2	Object
    //   53	214	6	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   55	87	171	finally
    //   92	101	171	finally
    //   106	115	171	finally
    //   120	129	171	finally
    //   134	143	171	finally
    //   148	155	171	finally
    //   179	189	171	finally
    //   55	87	175	android/database/SQLException
    //   92	101	175	android/database/SQLException
    //   106	115	175	android/database/SQLException
    //   120	129	175	android/database/SQLException
    //   134	143	175	android/database/SQLException
    //   148	155	175	android/database/SQLException
  }
  
  private static int getCountryCode(Context paramContext, String paramString)
  {
    if (paramString.length() >= 10)
    {
      int[] arrayOfInt = getAllCountryCodes(paramContext);
      if (arrayOfInt == null) {
        return -1;
      }
      paramContext = new int[MAX_COUNTRY_CODES_LENGTH];
      for (int i = 0; i < MAX_COUNTRY_CODES_LENGTH; i++) {
        paramContext[i] = Integer.parseInt(paramString.substring(0, i + 1));
      }
      for (i = 0; i < arrayOfInt.length; i++)
      {
        int j = arrayOfInt[i];
        for (int k = 0; k < MAX_COUNTRY_CODES_LENGTH; k++) {
          if (j == paramContext[k])
          {
            if (DBG)
            {
              paramContext = new StringBuilder();
              paramContext.append("Country code = ");
              paramContext.append(j);
              Rlog.d("SmsNumberUtils", paramContext.toString());
            }
            return j;
          }
        }
      }
    }
    return -1;
  }
  
  private static int getNetworkType(Phone paramPhone)
  {
    int i = -1;
    int j = paramPhone.getPhoneType();
    int k;
    if (j == 1)
    {
      k = 0;
    }
    else if (j == 2)
    {
      if (isInternationalRoaming(paramPhone)) {
        k = 2;
      } else {
        k = 1;
      }
    }
    else
    {
      k = i;
      if (DBG)
      {
        paramPhone = new StringBuilder();
        paramPhone.append("warning! unknown mPhoneType value=");
        paramPhone.append(j);
        Rlog.w("SmsNumberUtils", paramPhone.toString());
        k = i;
      }
    }
    return k;
  }
  
  private static String getNumberPlanType(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Number Plan type (");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append("): ");
    ((StringBuilder)localObject).toString();
    if (paramInt == 1) {
      localObject = "NP_NANP_LOCAL";
    } else if (paramInt == 2) {
      localObject = "NP_NANP_AREA_LOCAL";
    } else if (paramInt == 3) {
      localObject = "NP_NANP_NDD_AREA_LOCAL";
    } else if (paramInt == 4) {
      localObject = "NP_NANP_NBPCD_CC_AREA_LOCAL";
    } else if (paramInt == 5) {
      localObject = "NP_NANP_LOCALIDD_CC_AREA_LOCAL";
    } else if (paramInt == 6) {
      localObject = "NP_NANP_NBPCD_HOMEIDD_CC_AREA_LOCAL";
    } else if (paramInt == 100) {
      localObject = "NP_NBPCD_HOMEIDD_CC_AREA_LOCAL";
    } else if (paramInt == 101) {
      localObject = "NP_HOMEIDD_CC_AREA_LOCAL";
    } else if (paramInt == 102) {
      localObject = "NP_NBPCD_CC_AREA_LOCAL";
    } else if (paramInt == 103) {
      localObject = "NP_LOCALIDD_CC_AREA_LOCAL";
    } else if (paramInt == 104) {
      localObject = "NP_CC_AREA_LOCAL";
    } else {
      localObject = "Unknown type";
    }
    return localObject;
  }
  
  private static boolean inExceptionListForNpCcAreaLocal(NumberEntry paramNumberEntry)
  {
    int i = countryCode;
    boolean bool;
    if ((number.length() == 12) && ((i == 7) || (i == 20) || (i == 65) || (i == 90))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isInternationalRoaming(Phone paramPhone)
  {
    String str = TelephonyManager.from(paramPhone.getContext()).getNetworkCountryIsoForPhone(paramPhone.getPhoneId());
    paramPhone = TelephonyManager.from(paramPhone.getContext()).getSimCountryIsoForPhone(paramPhone.getPhoneId());
    boolean bool1;
    if ((!TextUtils.isEmpty(str)) && (!TextUtils.isEmpty(paramPhone)) && (!paramPhone.equals(str))) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2 = bool1;
    if (bool1) {
      if ("us".equals(paramPhone))
      {
        bool2 = true ^ "vi".equals(str);
      }
      else
      {
        bool2 = bool1;
        if ("vi".equals(paramPhone)) {
          bool2 = true ^ "us".equals(str);
        }
      }
    }
    return bool2;
  }
  
  private static boolean isNANP(String paramString)
  {
    if ((paramString.length() != 10) && ((paramString.length() != 11) || (!paramString.startsWith("1")))) {
      return false;
    }
    String str = paramString;
    if (paramString.length() == 11) {
      str = paramString.substring(1);
    }
    return PhoneNumberUtils.isNanp(str);
  }
  
  private static boolean needToConvert(Phone paramPhone)
  {
    long l = Binder.clearCallingIdentity();
    try
    {
      paramPhone = (CarrierConfigManager)paramPhone.getContext().getSystemService("carrier_config");
      if (paramPhone != null)
      {
        paramPhone = paramPhone.getConfig();
        if (paramPhone != null)
        {
          boolean bool = paramPhone.getBoolean("sms_requires_destination_number_conversion_bool");
          return bool;
        }
      }
      return false;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private static class NumberEntry
  {
    public String IDD;
    public int countryCode;
    public String number;
    
    public NumberEntry(String paramString)
    {
      number = paramString;
    }
  }
}
