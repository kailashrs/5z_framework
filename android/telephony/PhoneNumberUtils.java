package android.telephony;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Country;
import android.location.CountryDetector;
import android.net.Uri;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.text.style.TtsSpan.TelephoneBuilder;
import android.util.SparseIntArray;
import com.android.i18n.phonenumbers.NumberParseException;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.android.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.android.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.android.i18n.phonenumbers.ShortNumberInfo;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtils
{
  private static final String BCD_CALLED_PARTY_EXTENDED = "*#abc";
  private static final String BCD_EF_ADN_EXTENDED = "*#,N;";
  public static final int BCD_EXTENDED_TYPE_CALLED_PARTY = 2;
  public static final int BCD_EXTENDED_TYPE_EF_ADN = 1;
  private static final int CCC_LENGTH = COUNTRY_CALLING_CALL.length;
  private static final String CLIR_OFF = "#31#";
  private static final String CLIR_ON = "*31#";
  private static final boolean[] COUNTRY_CALLING_CALL;
  private static final boolean DBG = false;
  public static final int FORMAT_JAPAN = 2;
  public static final int FORMAT_NANP = 1;
  public static final int FORMAT_UNKNOWN = 0;
  private static final Pattern GLOBAL_PHONE_NUMBER_PATTERN;
  private static final String JAPAN_ISO_COUNTRY_CODE = "JP";
  private static final SparseIntArray KEYPAD_MAP;
  private static final String KOREA_ISO_COUNTRY_CODE = "KR";
  static final String LOG_TAG = "PhoneNumberUtils";
  static final int MIN_MATCH;
  private static final String[] NANP_COUNTRIES;
  private static final String NANP_IDP_STRING = "011";
  private static final int NANP_LENGTH = 10;
  private static final int NANP_STATE_DASH = 4;
  private static final int NANP_STATE_DIGIT = 1;
  private static final int NANP_STATE_ONE = 3;
  private static final int NANP_STATE_PLUS = 2;
  public static final char PAUSE = ',';
  private static final char PLUS_SIGN_CHAR = '+';
  private static final String PLUS_SIGN_STRING = "+";
  public static final int TOA_International = 145;
  public static final int TOA_Unknown = 129;
  public static final char WAIT = ';';
  public static final char WILD = 'N';
  private static String[] sConvertToEmergencyMap = null;
  private static Country sCountryDetector = null;
  
  static
  {
    GLOBAL_PHONE_NUMBER_PATTERN = Pattern.compile("[\\+]?[0-9.-]+");
    NANP_COUNTRIES = new String[] { "US", "CA", "AS", "AI", "AG", "BS", "BB", "BM", "VG", "KY", "DM", "DO", "GD", "GU", "JM", "PR", "MS", "MP", "KN", "LC", "VC", "TT", "TC", "VI" };
    int i;
    if ((!SystemProperties.get("ro.build.asus.sku", "").equals("CN")) && (!SystemProperties.get("ro.build.asus.sku", "").equals("CTA"))) {
      i = SystemProperties.getInt("persist.env.c.phone.matchnum", 7);
    } else {
      i = 11;
    }
    MIN_MATCH = i;
    KEYPAD_MAP = new SparseIntArray();
    KEYPAD_MAP.put(97, 50);
    KEYPAD_MAP.put(98, 50);
    KEYPAD_MAP.put(99, 50);
    KEYPAD_MAP.put(65, 50);
    KEYPAD_MAP.put(66, 50);
    KEYPAD_MAP.put(67, 50);
    KEYPAD_MAP.put(100, 51);
    KEYPAD_MAP.put(101, 51);
    KEYPAD_MAP.put(102, 51);
    KEYPAD_MAP.put(68, 51);
    KEYPAD_MAP.put(69, 51);
    KEYPAD_MAP.put(70, 51);
    KEYPAD_MAP.put(103, 52);
    KEYPAD_MAP.put(104, 52);
    KEYPAD_MAP.put(105, 52);
    KEYPAD_MAP.put(71, 52);
    KEYPAD_MAP.put(72, 52);
    KEYPAD_MAP.put(73, 52);
    KEYPAD_MAP.put(106, 53);
    KEYPAD_MAP.put(107, 53);
    KEYPAD_MAP.put(108, 53);
    KEYPAD_MAP.put(74, 53);
    KEYPAD_MAP.put(75, 53);
    KEYPAD_MAP.put(76, 53);
    KEYPAD_MAP.put(109, 54);
    KEYPAD_MAP.put(110, 54);
    KEYPAD_MAP.put(111, 54);
    KEYPAD_MAP.put(77, 54);
    KEYPAD_MAP.put(78, 54);
    KEYPAD_MAP.put(79, 54);
    KEYPAD_MAP.put(112, 55);
    KEYPAD_MAP.put(113, 55);
    KEYPAD_MAP.put(114, 55);
    KEYPAD_MAP.put(115, 55);
    KEYPAD_MAP.put(80, 55);
    KEYPAD_MAP.put(81, 55);
    KEYPAD_MAP.put(82, 55);
    KEYPAD_MAP.put(83, 55);
    KEYPAD_MAP.put(116, 56);
    KEYPAD_MAP.put(117, 56);
    KEYPAD_MAP.put(118, 56);
    KEYPAD_MAP.put(84, 56);
    KEYPAD_MAP.put(85, 56);
    KEYPAD_MAP.put(86, 56);
    KEYPAD_MAP.put(119, 57);
    KEYPAD_MAP.put(120, 57);
    KEYPAD_MAP.put(121, 57);
    KEYPAD_MAP.put(122, 57);
    KEYPAD_MAP.put(87, 57);
    KEYPAD_MAP.put(88, 57);
    KEYPAD_MAP.put(89, 57);
    KEYPAD_MAP.put(90, 57);
    COUNTRY_CALLING_CALL = new boolean[] { 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0 };
  }
  
  public PhoneNumberUtils() {}
  
  public static void addTtsSpan(Spannable paramSpannable, int paramInt1, int paramInt2)
  {
    paramSpannable.setSpan(createTtsSpan(paramSpannable.subSequence(paramInt1, paramInt2).toString()), paramInt1, paramInt2, 33);
  }
  
  private static String appendPwCharBackToOrigDialStr(int paramInt, String paramString1, String paramString2)
  {
    if (paramInt == 1) {
      paramString1 = paramString1 + paramString2.charAt(0);
    } else {
      paramString1 = paramString1.concat(paramString2.substring(0, paramInt));
    }
    return paramString1;
  }
  
  private static char bcdToChar(byte paramByte, int paramInt)
  {
    if (paramByte < 10) {
      return (char)(48 + paramByte);
    }
    String str = null;
    if (1 == paramInt) {
      str = "*#,N;";
    } else if (2 == paramInt) {
      str = "*#abc";
    }
    if ((str != null) && (paramByte - 10 < str.length())) {
      return str.charAt(paramByte - 10);
    }
    return '\000';
  }
  
  @Deprecated
  public static String calledPartyBCDFragmentToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return calledPartyBCDFragmentToString(paramArrayOfByte, paramInt1, paramInt2, 1);
  }
  
  public static String calledPartyBCDFragmentToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramInt2 * 2);
    internalCalledPartyBCDFragmentToString(localStringBuilder, paramArrayOfByte, paramInt1, paramInt2, paramInt3);
    return localStringBuilder.toString();
  }
  
  @Deprecated
  public static String calledPartyBCDToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return calledPartyBCDToString(paramArrayOfByte, paramInt1, paramInt2, 1);
  }
  
  public static String calledPartyBCDToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    Object localObject = new StringBuilder(paramInt2 * 2 + 1);
    if (paramInt2 < 2) {
      return "";
    }
    if ((paramArrayOfByte[paramInt1] & 0xF0) == 144) {
      i = 1;
    }
    internalCalledPartyBCDFragmentToString((StringBuilder)localObject, paramArrayOfByte, paramInt1 + 1, paramInt2 - 1, paramInt3);
    if ((i != 0) && (((StringBuilder)localObject).length() == 0)) {
      return "";
    }
    paramArrayOfByte = (byte[])localObject;
    if (i != 0)
    {
      String str = ((StringBuilder)localObject).toString();
      localObject = Pattern.compile("(^[#*])(.*)([#*])(.*)(#)$").matcher(str);
      if (((Matcher)localObject).matches())
      {
        if ("".equals(((Matcher)localObject).group(2)))
        {
          paramArrayOfByte = new StringBuilder();
          paramArrayOfByte.append(((Matcher)localObject).group(1));
          paramArrayOfByte.append(((Matcher)localObject).group(3));
          paramArrayOfByte.append(((Matcher)localObject).group(4));
          paramArrayOfByte.append(((Matcher)localObject).group(5));
          paramArrayOfByte.append("+");
        }
        else
        {
          paramArrayOfByte = new StringBuilder();
          paramArrayOfByte.append(((Matcher)localObject).group(1));
          paramArrayOfByte.append(((Matcher)localObject).group(2));
          paramArrayOfByte.append(((Matcher)localObject).group(3));
          paramArrayOfByte.append("+");
          paramArrayOfByte.append(((Matcher)localObject).group(4));
          paramArrayOfByte.append(((Matcher)localObject).group(5));
        }
      }
      else
      {
        localObject = Pattern.compile("(^[#*])(.*)([#*])(.*)").matcher(str);
        if (((Matcher)localObject).matches())
        {
          paramArrayOfByte = new StringBuilder();
          paramArrayOfByte.append(((Matcher)localObject).group(1));
          paramArrayOfByte.append(((Matcher)localObject).group(2));
          paramArrayOfByte.append(((Matcher)localObject).group(3));
          paramArrayOfByte.append("+");
          paramArrayOfByte.append(((Matcher)localObject).group(4));
        }
        else
        {
          paramArrayOfByte = new StringBuilder();
          paramArrayOfByte.append('+');
          paramArrayOfByte.append(str);
        }
      }
    }
    return paramArrayOfByte.toString();
  }
  
  public static String cdmaCheckAndProcessPlusCode(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (isReallyDialable(paramString.charAt(0))) && (isNonSeparator(paramString)))
    {
      String str1 = TelephonyManager.getDefault().getNetworkCountryIso();
      String str2 = TelephonyManager.getDefault().getSimCountryIso();
      if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2))) {
        return cdmaCheckAndProcessPlusCodeByNumberFormat(paramString, getFormatTypeFromCountryCode(str1), getFormatTypeFromCountryCode(str2));
      }
    }
    return paramString;
  }
  
  public static String cdmaCheckAndProcessPlusCodeByNumberFormat(String paramString, int paramInt1, int paramInt2)
  {
    Object localObject1 = paramString;
    boolean bool;
    if ((paramInt1 == paramInt2) && (paramInt1 == 1)) {
      bool = true;
    } else {
      bool = false;
    }
    Object localObject2 = localObject1;
    if (paramString != null)
    {
      localObject2 = localObject1;
      if (paramString.lastIndexOf("+") != -1)
      {
        Object localObject3 = paramString;
        localObject2 = null;
        Object localObject5;
        do
        {
          if (bool) {
            localObject1 = extractNetworkPortion((String)localObject3);
          } else {
            localObject1 = extractNetworkPortionAlt((String)localObject3);
          }
          localObject1 = processPlusCode((String)localObject1, bool);
          if (TextUtils.isEmpty((CharSequence)localObject1)) {
            break;
          }
          if (localObject2 == null) {
            localObject2 = localObject1;
          } else {
            localObject2 = ((String)localObject2).concat((String)localObject1);
          }
          String str = extractPostDialPortion((String)localObject3);
          localObject1 = localObject2;
          Object localObject4 = str;
          localObject5 = localObject3;
          if (!TextUtils.isEmpty(str))
          {
            paramInt1 = findDialableIndexFromPostDialStr(str);
            if (paramInt1 >= 1)
            {
              localObject1 = appendPwCharBackToOrigDialStr(paramInt1, (String)localObject2, str);
              localObject5 = str.substring(paramInt1);
              localObject4 = str;
            }
            else
            {
              localObject1 = str;
              if (paramInt1 < 0) {
                localObject1 = "";
              }
              Rlog.e("wrong postDialStr=", (String)localObject1);
              localObject5 = localObject3;
              localObject4 = localObject1;
              localObject1 = localObject2;
            }
          }
          localObject2 = localObject1;
          if (TextUtils.isEmpty((CharSequence)localObject4)) {
            break label231;
          }
          localObject2 = localObject1;
          localObject3 = localObject5;
        } while (!TextUtils.isEmpty((CharSequence)localObject5));
        localObject2 = localObject1;
        break label231;
        Rlog.e("checkAndProcessPlusCode: null newDialStr", (String)localObject1);
        return paramString;
      }
    }
    label231:
    return localObject2;
  }
  
  public static String cdmaCheckAndProcessPlusCodeForSms(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (isReallyDialable(paramString.charAt(0))) && (isNonSeparator(paramString)))
    {
      String str = TelephonyManager.getDefault().getSimCountryIso();
      if (!TextUtils.isEmpty(str))
      {
        int i = getFormatTypeFromCountryCode(str);
        return cdmaCheckAndProcessPlusCodeByNumberFormat(paramString, i, i);
      }
    }
    return paramString;
  }
  
  private static int charToBCD(char paramChar, int paramInt)
  {
    if (('0' <= paramChar) && (paramChar <= '9')) {
      return paramChar - '0';
    }
    Object localObject = null;
    if (1 == paramInt) {
      localObject = "*#,N;";
    } else if (2 == paramInt) {
      localObject = "*#abc";
    }
    if ((localObject != null) && (((String)localObject).indexOf(paramChar) != -1)) {
      return 10 + ((String)localObject).indexOf(paramChar);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("invalid char for BCD ");
    ((StringBuilder)localObject).append(paramChar);
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  private static boolean checkPrefixIsIgnorable(String paramString, int paramInt1, int paramInt2)
  {
    int i = 0;
    while (paramInt2 >= paramInt1)
    {
      if (tryGetISODigit(paramString.charAt(paramInt2)) >= 0)
      {
        if (i != 0) {
          return false;
        }
        i = 1;
      }
      else if (isDialable(paramString.charAt(paramInt2)))
      {
        return false;
      }
      paramInt2--;
    }
    return true;
  }
  
  public static boolean compare(Context paramContext, String paramString1, String paramString2)
  {
    return compare(paramString1, paramString2, paramContext.getResources().getBoolean(17957073));
  }
  
  public static boolean compare(String paramString1, String paramString2)
  {
    return compare(paramString1, paramString2, false);
  }
  
  public static boolean compare(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramBoolean = compareStrictly(paramString1, paramString2);
    } else {
      paramBoolean = compareLoosely(paramString1, paramString2);
    }
    return paramBoolean;
  }
  
  public static boolean compareLoosely(String paramString1, String paramString2)
  {
    boolean bool = false;
    if ((paramString1 != null) && (paramString2 != null))
    {
      if ((paramString1.length() != 0) && (paramString2.length() != 0))
      {
        int i = indexOfLastNetworkChar(paramString1);
        int j = indexOfLastNetworkChar(paramString2);
        int k = 0;
        int m = 0;
        int n = 0;
        int i1;
        int i2;
        int i3;
        int i4;
        for (;;)
        {
          i1 = m;
          i2 = i;
          i3 = j;
          i4 = k;
          if (i < 0) {
            break;
          }
          i1 = m;
          i2 = i;
          i3 = j;
          i4 = k;
          if (j < 0) {
            break;
          }
          i3 = 0;
          char c1 = paramString1.charAt(i);
          i1 = m;
          i2 = i;
          if (!isDialable(c1))
          {
            i2 = i - 1;
            i3 = 1;
            i1 = m + 1;
          }
          char c2 = paramString2.charAt(j);
          i = j;
          i4 = k;
          if (!isDialable(c2))
          {
            i = j - 1;
            i3 = 1;
            i4 = k + 1;
          }
          m = n;
          k = i2;
          j = i;
          if (i3 == 0)
          {
            if ((c2 != c1) && (c1 != 'N') && (c2 != 'N'))
            {
              i3 = i;
              break;
            }
            k = i2 - 1;
            j = i - 1;
            m = n + 1;
          }
          n = m;
          m = i1;
          i = k;
          k = i4;
        }
        if (n < MIN_MATCH)
        {
          j = paramString1.length() - i1;
          return (j == paramString2.length() - i4) && (j == n);
        }
        if ((n >= MIN_MATCH) && ((i2 < 0) || (i3 < 0))) {
          return true;
        }
        if ((matchIntlPrefix(paramString1, i2 + 1)) && (matchIntlPrefix(paramString2, i3 + 1))) {
          return true;
        }
        if ((matchTrunkPrefix(paramString1, i2 + 1)) && (matchIntlPrefixAndCC(paramString2, i3 + 1))) {
          return true;
        }
        return (matchTrunkPrefix(paramString2, i3 + 1)) && (matchIntlPrefixAndCC(paramString1, i2 + 1));
      }
      return false;
    }
    if (paramString1 == paramString2) {
      bool = true;
    }
    return bool;
  }
  
  public static boolean compareStrictly(String paramString1, String paramString2)
  {
    return compareStrictly(paramString1, paramString2, true);
  }
  
  public static boolean compareStrictly(String paramString1, String paramString2, boolean paramBoolean)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      if ((paramString1.length() == 0) && (paramString2.length() == 0)) {
        return false;
      }
      int i = 0;
      int j = 0;
      int k = 0;
      CountryCallingCodeAndNewIndex localCountryCallingCodeAndNewIndex1 = tryGetCountryCallingCodeAndNewIndex(paramString1, paramBoolean);
      CountryCallingCodeAndNewIndex localCountryCallingCodeAndNewIndex2 = tryGetCountryCallingCodeAndNewIndex(paramString2, paramBoolean);
      int m = 0;
      int n = 1;
      int i1 = 0;
      int i2 = 0;
      int i3 = 0;
      int i4;
      int i5;
      int i6;
      if ((localCountryCallingCodeAndNewIndex1 != null) && (localCountryCallingCodeAndNewIndex2 != null))
      {
        if (countryCallingCode != countryCallingCode) {
          return false;
        }
        i4 = 0;
        i5 = 1;
        i = newIndex;
        k = newIndex;
      }
      else if ((localCountryCallingCodeAndNewIndex1 == null) && (localCountryCallingCodeAndNewIndex2 == null))
      {
        i4 = 0;
        i5 = m;
      }
      else
      {
        if (localCountryCallingCodeAndNewIndex1 != null)
        {
          j = newIndex;
        }
        else
        {
          i4 = tryGetTrunkPrefixOmittedIndex(paramString2, 0);
          if (i4 >= 0)
          {
            j = i4;
            i2 = 1;
          }
        }
        if (localCountryCallingCodeAndNewIndex2 != null)
        {
          k = newIndex;
          i = j;
          i5 = m;
          i4 = n;
          i1 = i2;
        }
        else
        {
          i6 = tryGetTrunkPrefixOmittedIndex(paramString2, 0);
          i = j;
          i5 = m;
          i4 = n;
          i1 = i2;
          if (i6 >= 0)
          {
            k = i6;
            i3 = 1;
            i1 = i2;
            i4 = n;
            i5 = m;
            i = j;
          }
        }
      }
      i2 = paramString1.length() - 1;
      j = paramString2.length() - 1;
      char c2;
      while ((i2 >= i) && (j >= k))
      {
        n = 0;
        char c1 = paramString1.charAt(i2);
        c2 = paramString2.charAt(j);
        m = i2;
        if (isSeparator(c1))
        {
          m = i2 - 1;
          n = 1;
        }
        i2 = j;
        i6 = n;
        if (isSeparator(c2))
        {
          i2 = j - 1;
          i6 = 1;
        }
        n = m;
        j = i2;
        if (i6 == 0)
        {
          if (c1 != c2) {
            return false;
          }
          n = m - 1;
          j = i2 - 1;
        }
        i2 = n;
      }
      if (i4 != 0)
      {
        if (((i1 != 0) && (i <= i2)) || (!checkPrefixIsIgnorable(paramString1, i, i2)))
        {
          if (paramBoolean) {
            return compare(paramString1, paramString2, false);
          }
          return false;
        }
        if (((i3 != 0) && (k <= j)) || (!checkPrefixIsIgnorable(paramString2, i, j)))
        {
          if (paramBoolean) {
            return compare(paramString1, paramString2, false);
          }
          return false;
        }
      }
      else
      {
        if (i5 == 0) {
          i4 = 1;
        }
        for (i4 = 0;; i4 = m)
        {
          n = i4;
          m = j;
          if (i2 < i) {
            break;
          }
          c2 = paramString1.charAt(i2);
          m = i4;
          if (isDialable(c2)) {
            if ((i4 != 0) && (tryGetISODigit(c2) == 1)) {
              m = 0;
            } else {
              return false;
            }
          }
          i2--;
        }
        while (m >= k)
        {
          c2 = paramString2.charAt(m);
          j = n;
          if (isDialable(c2)) {
            if ((n != 0) && (tryGetISODigit(c2) == 1)) {
              j = 0;
            } else {
              return false;
            }
          }
          m--;
          n = j;
        }
      }
      return true;
    }
    paramBoolean = true;
    if (paramString1 != paramString2) {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  public static String convertAndStrip(String paramString)
  {
    return stripSeparators(convertKeypadLettersToDigits(paramString));
  }
  
  public static String convertKeypadLettersToDigits(String paramString)
  {
    if (paramString == null) {
      return paramString;
    }
    int i = paramString.length();
    if (i == 0) {
      return paramString;
    }
    paramString = paramString.toCharArray();
    for (int j = 0; j < i; j++)
    {
      int k = paramString[j];
      paramString[j] = ((char)(char)KEYPAD_MAP.get(k, k));
    }
    return new String(paramString);
  }
  
  public static String convertPreDial(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(i);
    for (int j = 0; j < i; j++)
    {
      char c1 = paramString.charAt(j);
      char c2;
      char c3;
      if (isPause(c1))
      {
        c2 = ',';
        c3 = c2;
      }
      else
      {
        c3 = c1;
        if (isToneWait(c1))
        {
          c2 = ';';
          c3 = c2;
        }
      }
      localStringBuilder.append(c3);
    }
    return localStringBuilder.toString();
  }
  
  public static Uri convertSipUriToTelUri(Uri paramUri)
  {
    if (!"sip".equals(paramUri.getScheme())) {
      return paramUri;
    }
    String[] arrayOfString = paramUri.getSchemeSpecificPart().split("[@;:]");
    if (arrayOfString.length == 0) {
      return paramUri;
    }
    return Uri.fromParts("tel", arrayOfString[0], null);
  }
  
  public static String convertToEmergencyNumber(Context paramContext, String paramString)
  {
    if ((paramContext != null) && (!TextUtils.isEmpty(paramString)))
    {
      String str1 = normalizeNumber(paramString);
      if (isEmergencyNumber(str1)) {
        return paramString;
      }
      if (sConvertToEmergencyMap == null) {
        sConvertToEmergencyMap = paramContext.getResources().getStringArray(17236000);
      }
      if ((sConvertToEmergencyMap != null) && (sConvertToEmergencyMap.length != 0))
      {
        for (paramContext : sConvertToEmergencyMap)
        {
          String[] arrayOfString2 = null;
          Object localObject1 = null;
          String str2 = null;
          if (!TextUtils.isEmpty(paramContext)) {
            arrayOfString2 = paramContext.split(":");
          }
          Object localObject2 = localObject1;
          paramContext = str2;
          if (arrayOfString2 != null)
          {
            localObject2 = localObject1;
            paramContext = str2;
            if (arrayOfString2.length == 2)
            {
              str2 = arrayOfString2[1];
              localObject2 = localObject1;
              paramContext = str2;
              if (!TextUtils.isEmpty(arrayOfString2[0]))
              {
                localObject2 = arrayOfString2[0].split(",");
                paramContext = str2;
              }
            }
          }
          if ((!TextUtils.isEmpty(paramContext)) && (localObject2 != null) && (localObject2.length != 0))
          {
            int k = localObject2.length;
            for (int m = 0; m < k; m++)
            {
              arrayOfString2 = localObject2[m];
              if ((!TextUtils.isEmpty(arrayOfString2)) && (arrayOfString2.equals(str1))) {
                return paramContext;
              }
            }
          }
        }
        return paramString;
      }
      return paramString;
    }
    return paramString;
  }
  
  public static TtsSpan createTtsSpan(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    Object localObject1 = PhoneNumberUtil.getInstance();
    Object localObject2 = null;
    try
    {
      localObject1 = ((PhoneNumberUtil)localObject1).parse(paramString, null);
      localObject2 = localObject1;
    }
    catch (NumberParseException localNumberParseException) {}
    TtsSpan.TelephoneBuilder localTelephoneBuilder = new TtsSpan.TelephoneBuilder();
    if (localObject2 == null)
    {
      localTelephoneBuilder.setNumberParts(splitAtNonNumerics(paramString));
    }
    else
    {
      if (localObject2.hasCountryCode()) {
        localTelephoneBuilder.setCountryCode(Integer.toString(localObject2.getCountryCode()));
      }
      localTelephoneBuilder.setNumberParts(Long.toString(localObject2.getNationalNumber()));
    }
    return localTelephoneBuilder.build();
  }
  
  public static CharSequence createTtsSpannable(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {
      return null;
    }
    paramCharSequence = Spannable.Factory.getInstance().newSpannable(paramCharSequence);
    addTtsSpan(paramCharSequence, 0, paramCharSequence.length());
    return paramCharSequence;
  }
  
  public static String extractNetworkPortion(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(i);
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      int k = Character.digit(c, 10);
      if (k != -1)
      {
        localStringBuilder.append(k);
      }
      else if (c == '+')
      {
        String str = localStringBuilder.toString();
        if ((str.length() == 0) || (str.equals("*31#")) || (str.equals("#31#"))) {
          localStringBuilder.append(c);
        }
      }
      else if (isDialable(c))
      {
        localStringBuilder.append(c);
      }
      else
      {
        if (isStartsPostDial(c)) {
          break;
        }
      }
    }
    return localStringBuilder.toString();
  }
  
  public static String extractNetworkPortionAlt(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(i);
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      char c = paramString.charAt(k);
      int m = j;
      if (c == '+')
      {
        if (j == 0) {
          m = 1;
        }
      }
      else if (isDialable(c))
      {
        localStringBuilder.append(c);
        j = m;
      }
      else
      {
        j = m;
        if (isStartsPostDial(c)) {
          break;
        }
      }
    }
    return localStringBuilder.toString();
  }
  
  public static String extractPostDialPortion(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    int i = indexOfLastNetworkChar(paramString) + 1;
    int j = paramString.length();
    while (i < j)
    {
      char c = paramString.charAt(i);
      if (isNonSeparator(c)) {
        localStringBuilder.append(c);
      }
      i++;
    }
    return localStringBuilder.toString();
  }
  
  private static int findDialableIndexFromPostDialStr(String paramString)
  {
    for (int i = 0; i < paramString.length(); i++) {
      if (isReallyDialable(paramString.charAt(i))) {
        return i;
      }
    }
    return -1;
  }
  
  @Deprecated
  public static void formatJapaneseNumber(Editable paramEditable)
  {
    JapanesePhoneNumberFormatter.format(paramEditable);
  }
  
  @Deprecated
  public static void formatNanpNumber(Editable paramEditable)
  {
    int i = paramEditable.length();
    if (i > "+1-nnn-nnn-nnnn".length()) {
      return;
    }
    if (i <= 5) {
      return;
    }
    int j = 0;
    CharSequence localCharSequence = paramEditable.subSequence(0, i);
    removeDashes(paramEditable);
    int k = paramEditable.length();
    int[] arrayOfInt = new int[3];
    int m = 0;
    i = 1;
    int n = 0;
    int i1 = 0;
    while (i1 < k)
    {
      int i2 = paramEditable.charAt(i1);
      if (i2 != 43)
      {
        if (i2 != 45)
        {
          switch (i2)
          {
          default: 
            break;
          case 49: 
            if ((m == 0) || (i == 2)) {
              i = 3;
            }
            break;
          }
          if (i == 2)
          {
            paramEditable.replace(0, k, localCharSequence);
            return;
          }
          if (i == 3)
          {
            i = n + 1;
            arrayOfInt[n] = i1;
          }
          for (;;)
          {
            break;
            if ((i != 4) && ((m == 3) || (m == 6)))
            {
              i = n + 1;
              arrayOfInt[n] = i1;
            }
            else
            {
              i = n;
            }
          }
          m++;
          i2 = 1;
          n = i;
          i = i2;
        }
        else
        {
          i = 4;
        }
      }
      else
      {
        if (i1 != 0) {
          break label276;
        }
        i = 2;
      }
      i1++;
      continue;
      label276:
      paramEditable.replace(0, k, localCharSequence);
      return;
    }
    i = n;
    if (m == 7) {
      i = n - 1;
    }
    for (n = j; n < i; n++)
    {
      i1 = arrayOfInt[n];
      paramEditable.replace(i1 + n, i1 + n, "-");
    }
    for (i = paramEditable.length(); (i > 0) && (paramEditable.charAt(i - 1) == '-'); i--) {
      paramEditable.delete(i - 1, i);
    }
  }
  
  @Deprecated
  public static String formatNumber(String paramString)
  {
    paramString = new SpannableStringBuilder(paramString);
    formatNumber(paramString, getFormatTypeForLocale(Locale.getDefault()));
    return paramString.toString();
  }
  
  @Deprecated
  public static String formatNumber(String paramString, int paramInt)
  {
    paramString = new SpannableStringBuilder(paramString);
    formatNumber(paramString, paramInt);
    return paramString.toString();
  }
  
  public static String formatNumber(String paramString1, String paramString2)
  {
    if ((!paramString1.startsWith("#")) && (!paramString1.startsWith("*")))
    {
      PhoneNumberUtil localPhoneNumberUtil = PhoneNumberUtil.getInstance();
      Object localObject = null;
      try
      {
        paramString1 = localPhoneNumberUtil.parseAndKeepRawInput(paramString1, paramString2);
        if (("KR".equalsIgnoreCase(paramString2)) && (paramString1.getCountryCode() == localPhoneNumberUtil.getCountryCodeForRegion("KR")) && (paramString1.getCountryCodeSource() == Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN)) {
          paramString1 = localPhoneNumberUtil.format(paramString1, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } else if (("JP".equalsIgnoreCase(paramString2)) && (paramString1.getCountryCode() == localPhoneNumberUtil.getCountryCodeForRegion("JP")) && (paramString1.getCountryCodeSource() == Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN)) {
          paramString1 = localPhoneNumberUtil.format(paramString1, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } else {
          paramString1 = localPhoneNumberUtil.formatInOriginalFormat(paramString1, paramString2);
        }
      }
      catch (NumberParseException paramString1)
      {
        paramString1 = localObject;
      }
      return paramString1;
    }
    return paramString1;
  }
  
  public static String formatNumber(String paramString1, String paramString2, String paramString3)
  {
    int i = paramString1.length();
    for (int j = 0; j < i; j++) {
      if (!isDialable(paramString1.charAt(j))) {
        return paramString1;
      }
    }
    Object localObject1 = PhoneNumberUtil.getInstance();
    Object localObject2 = paramString3;
    if (paramString2 != null)
    {
      localObject2 = paramString3;
      if (paramString2.length() >= 2)
      {
        localObject2 = paramString3;
        if (paramString2.charAt(0) == '+') {
          try
          {
            localObject1 = ((PhoneNumberUtil)localObject1).getRegionCodeForNumber(((PhoneNumberUtil)localObject1).parse(paramString2, "ZZ"));
            localObject2 = paramString3;
            if (!TextUtils.isEmpty((CharSequence)localObject1))
            {
              j = normalizeNumber(paramString1).indexOf(paramString2.substring(1));
              localObject2 = paramString3;
              if (j <= 0) {
                localObject2 = localObject1;
              }
            }
          }
          catch (NumberParseException paramString2)
          {
            localObject2 = paramString3;
          }
        }
      }
    }
    paramString2 = formatNumber(paramString1, (String)localObject2);
    if (paramString2 != null) {
      paramString1 = paramString2;
    }
    return paramString1;
  }
  
  @Deprecated
  public static void formatNumber(Editable paramEditable, int paramInt)
  {
    int i = paramInt;
    paramInt = i;
    if (paramEditable.length() > 2)
    {
      paramInt = i;
      if (paramEditable.charAt(0) == '+') {
        if (paramEditable.charAt(1) == '1') {
          paramInt = 1;
        } else if ((paramEditable.length() >= 3) && (paramEditable.charAt(1) == '8') && (paramEditable.charAt(2) == '1')) {
          paramInt = 2;
        } else {
          paramInt = 0;
        }
      }
    }
    switch (paramInt)
    {
    default: 
      return;
    case 2: 
      formatJapaneseNumber(paramEditable);
      return;
    case 1: 
      formatNanpNumber(paramEditable);
      return;
    }
    removeDashes(paramEditable);
  }
  
  private static String formatNumberInternal(String paramString1, String paramString2, PhoneNumberUtil.PhoneNumberFormat paramPhoneNumberFormat)
  {
    PhoneNumberUtil localPhoneNumberUtil = PhoneNumberUtil.getInstance();
    try
    {
      paramString1 = localPhoneNumberUtil.parse(paramString1, paramString2);
      if (localPhoneNumberUtil.isValidNumber(paramString1))
      {
        paramString1 = localPhoneNumberUtil.format(paramString1, paramPhoneNumberFormat);
        return paramString1;
      }
    }
    catch (NumberParseException paramString1) {}
    return null;
  }
  
  public static String formatNumberToE164(String paramString1, String paramString2)
  {
    return formatNumberInternal(paramString1, paramString2, PhoneNumberUtil.PhoneNumberFormat.E164);
  }
  
  public static String formatNumberToRFC3966(String paramString1, String paramString2)
  {
    return formatNumberInternal(paramString1, paramString2, PhoneNumberUtil.PhoneNumberFormat.RFC3966);
  }
  
  private static String getCountryIso(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getCountryIso ");
    localStringBuilder.append(sCountryDetector);
    Rlog.w("PhoneNumberUtils", localStringBuilder.toString());
    if (sCountryDetector == null)
    {
      paramContext = (CountryDetector)paramContext.getSystemService("country_detector");
      if (paramContext != null) {
        sCountryDetector = paramContext.detectCountry();
      }
    }
    if (sCountryDetector == null) {
      return null;
    }
    return sCountryDetector.getCountryIso();
  }
  
  private static String getCurrentIdp(boolean paramBoolean)
  {
    String str;
    if (paramBoolean) {
      str = "011";
    } else {
      str = SystemProperties.get("gsm.operator.idpstring", "+");
    }
    return str;
  }
  
  private static int getDefaultVoiceSubId()
  {
    return SubscriptionManager.getDefaultVoiceSubscriptionId();
  }
  
  @Deprecated
  public static int getFormatTypeForLocale(Locale paramLocale)
  {
    return getFormatTypeFromCountryCode(paramLocale.getCountry());
  }
  
  private static int getFormatTypeFromCountryCode(String paramString)
  {
    int i = NANP_COUNTRIES.length;
    for (int j = 0; j < i; j++) {
      if (NANP_COUNTRIES[j].compareToIgnoreCase(paramString) == 0) {
        return 1;
      }
    }
    if ("jp".compareToIgnoreCase(paramString) == 0) {
      return 2;
    }
    return 0;
  }
  
  /* Error */
  public static String getNumberFromIntent(android.content.Intent paramIntent, Context paramContext)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aload_0
    //   8: invokevirtual 746	android/content/Intent:getData	()Landroid/net/Uri;
    //   11: astore 5
    //   13: aload 5
    //   15: ifnonnull +5 -> 20
    //   18: aconst_null
    //   19: areturn
    //   20: aload 5
    //   22: invokevirtual 466	android/net/Uri:getScheme	()Ljava/lang/String;
    //   25: astore 6
    //   27: aload 6
    //   29: ldc_w 477
    //   32: invokevirtual 165	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   35: ifne +224 -> 259
    //   38: aload 6
    //   40: ldc_w 461
    //   43: invokevirtual 165	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   46: ifeq +6 -> 52
    //   49: goto +210 -> 259
    //   52: aload_1
    //   53: ifnonnull +5 -> 58
    //   56: aconst_null
    //   57: areturn
    //   58: aload_0
    //   59: aload_1
    //   60: invokevirtual 749	android/content/Intent:resolveType	(Landroid/content/Context;)Ljava/lang/String;
    //   63: pop
    //   64: aconst_null
    //   65: astore 7
    //   67: aload 5
    //   69: invokevirtual 752	android/net/Uri:getAuthority	()Ljava/lang/String;
    //   72: astore_0
    //   73: ldc_w 754
    //   76: aload_0
    //   77: invokevirtual 165	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   80: ifeq +11 -> 91
    //   83: ldc_w 756
    //   86: astore 7
    //   88: goto +21 -> 109
    //   91: ldc_w 758
    //   94: aload_0
    //   95: invokevirtual 165	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   98: ifeq -10 -> 88
    //   101: ldc_w 760
    //   104: astore 7
    //   106: goto -18 -> 88
    //   109: aconst_null
    //   110: astore_0
    //   111: aconst_null
    //   112: astore 6
    //   114: aload_1
    //   115: invokevirtual 764	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   118: aload 5
    //   120: iconst_1
    //   121: anewarray 99	java/lang/String
    //   124: dup
    //   125: iconst_0
    //   126: aload 7
    //   128: aastore
    //   129: aconst_null
    //   130: aconst_null
    //   131: aconst_null
    //   132: invokevirtual 770	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   135: astore 5
    //   137: aload 4
    //   139: astore_1
    //   140: aload 5
    //   142: ifnull +47 -> 189
    //   145: aload 4
    //   147: astore_1
    //   148: aload 5
    //   150: astore 6
    //   152: aload 5
    //   154: astore_0
    //   155: aload 5
    //   157: invokeinterface 775 1 0
    //   162: ifeq +27 -> 189
    //   165: aload 5
    //   167: astore 6
    //   169: aload 5
    //   171: astore_0
    //   172: aload 5
    //   174: aload 5
    //   176: aload 7
    //   178: invokeinterface 778 2 0
    //   183: invokeinterface 781 2 0
    //   188: astore_1
    //   189: aload_1
    //   190: astore 6
    //   192: aload 5
    //   194: ifnull +48 -> 242
    //   197: aload 5
    //   199: astore_0
    //   200: aload_0
    //   201: invokeinterface 784 1 0
    //   206: aload_1
    //   207: astore 6
    //   209: goto +33 -> 242
    //   212: astore_0
    //   213: goto +32 -> 245
    //   216: astore_1
    //   217: aload_0
    //   218: astore 6
    //   220: ldc 50
    //   222: ldc_w 786
    //   225: aload_1
    //   226: invokestatic 789	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   229: pop
    //   230: aload_3
    //   231: astore 6
    //   233: aload_0
    //   234: ifnull +8 -> 242
    //   237: aload_2
    //   238: astore_1
    //   239: goto -39 -> 200
    //   242: aload 6
    //   244: areturn
    //   245: aload 6
    //   247: ifnull +10 -> 257
    //   250: aload 6
    //   252: invokeinterface 784 1 0
    //   257: aload_0
    //   258: athrow
    //   259: aload 5
    //   261: invokevirtual 469	android/net/Uri:getSchemeSpecificPart	()Ljava/lang/String;
    //   264: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	265	0	paramIntent	android.content.Intent
    //   0	265	1	paramContext	Context
    //   1	237	2	localObject1	Object
    //   3	228	3	localObject2	Object
    //   5	141	4	localObject3	Object
    //   11	249	5	localObject4	Object
    //   25	226	6	localObject5	Object
    //   65	112	7	str	String
    // Exception table:
    //   from	to	target	type
    //   114	137	212	finally
    //   155	165	212	finally
    //   172	189	212	finally
    //   220	230	212	finally
    //   114	137	216	java/lang/RuntimeException
    //   155	165	216	java/lang/RuntimeException
    //   172	189	216	java/lang/RuntimeException
  }
  
  public static String getStrippedReversed(String paramString)
  {
    paramString = extractNetworkPortionAlt(paramString);
    if (paramString == null) {
      return null;
    }
    return internalGetStrippedReversed(paramString, paramString.length());
  }
  
  public static String getUsernameFromUriNumber(String paramString)
  {
    int i = paramString.indexOf('@');
    int j = i;
    if (i < 0) {
      j = paramString.indexOf("%40");
    }
    i = j;
    if (j < 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getUsernameFromUriNumber: no delimiter found in SIP addr '");
      localStringBuilder.append(paramString);
      localStringBuilder.append("'");
      Rlog.w("PhoneNumberUtils", localStringBuilder.toString());
      i = paramString.length();
    }
    return paramString.substring(0, i);
  }
  
  private static int indexOfLastNetworkChar(String paramString)
  {
    int i = paramString.length();
    int j = minPositive(paramString.indexOf(','), paramString.indexOf(';'));
    if (j < 0) {
      return i - 1;
    }
    return j - 1;
  }
  
  private static void internalCalledPartyBCDFragmentToString(StringBuilder paramStringBuilder, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    for (int i = paramInt1; i < paramInt2 + paramInt1; i++)
    {
      char c = bcdToChar((byte)(paramArrayOfByte[i] & 0xF), paramInt3);
      if (c == 0) {
        return;
      }
      paramStringBuilder.append(c);
      byte b = (byte)(paramArrayOfByte[i] >> 4 & 0xF);
      if ((b == 15) && (i + 1 == paramInt2 + paramInt1)) {
        break;
      }
      c = bcdToChar(b, paramInt3);
      if (c == 0) {
        return;
      }
      paramStringBuilder.append(c);
    }
  }
  
  private static String internalGetStrippedReversed(String paramString, int paramInt)
  {
    if (paramString == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder(paramInt);
    int i = paramString.length();
    for (int j = i - 1; (j >= 0) && (i - j <= paramInt); j--) {
      localStringBuilder.append(paramString.charAt(j));
    }
    return localStringBuilder.toString();
  }
  
  public static final boolean is12Key(char paramChar)
  {
    boolean bool;
    if (((paramChar < '0') || (paramChar > '9')) && (paramChar != '*') && (paramChar != '#')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isCountryCallingCode(int paramInt)
  {
    boolean bool;
    if ((paramInt > 0) && (paramInt < CCC_LENGTH) && (COUNTRY_CALLING_CALL[paramInt] != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static final boolean isDialable(char paramChar)
  {
    boolean bool;
    if (((paramChar < '0') || (paramChar > '9')) && (paramChar != '*') && (paramChar != '#') && (paramChar != '+') && (paramChar != 'N')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isDialable(String paramString)
  {
    int i = 0;
    int j = paramString.length();
    while (i < j)
    {
      if (!isDialable(paramString.charAt(i))) {
        return false;
      }
      i++;
    }
    return true;
  }
  
  public static boolean isEmergencyNumber(int paramInt, String paramString)
  {
    return isEmergencyNumberInternal(paramInt, paramString, true);
  }
  
  public static boolean isEmergencyNumber(int paramInt, String paramString1, String paramString2)
  {
    return isEmergencyNumberInternal(paramInt, paramString1, paramString2, true);
  }
  
  public static boolean isEmergencyNumber(String paramString)
  {
    return isEmergencyNumber(getDefaultVoiceSubId(), paramString);
  }
  
  public static boolean isEmergencyNumber(String paramString1, String paramString2)
  {
    return isEmergencyNumber(getDefaultVoiceSubId(), paramString1, paramString2);
  }
  
  private static boolean isEmergencyNumberInternal(int paramInt, String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramString1 == null) {
      return false;
    }
    if (isUriNumber(paramString1)) {
      return false;
    }
    String str = extractNetworkPortionAlt(paramString1);
    int i = SubscriptionManager.getSlotIndex(paramInt);
    if (i <= 0)
    {
      paramString1 = "ril.ecclist";
    }
    else
    {
      paramString1 = new StringBuilder();
      paramString1.append("ril.ecclist");
      paramString1.append(i);
      paramString1 = paramString1.toString();
    }
    Object localObject = SystemProperties.get(paramString1, "");
    if (paramInt < 2147483643)
    {
      paramString1 = (String)localObject;
      if (paramInt >= 0) {}
    }
    else
    {
      paramString1 = (String)localObject;
      if (!TextUtils.isEmpty(SystemProperties.get("ril.ecclist1", "")))
      {
        paramString1 = new StringBuilder();
        paramString1.append(SystemProperties.get("ril.ecclist", ""));
        paramString1.append(",");
        paramString1.append(SystemProperties.get("ril.ecclist1", ""));
        paramString1 = paramString1.toString();
      }
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("slotId:");
    ((StringBuilder)localObject).append(i);
    ((StringBuilder)localObject).append(" subId:");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" country:");
    ((StringBuilder)localObject).append(paramString2);
    ((StringBuilder)localObject).append(" emergencyNumbers: ");
    ((StringBuilder)localObject).append(paramString1);
    Rlog.d("PhoneNumberUtils", ((StringBuilder)localObject).toString());
    localObject = paramString1;
    if (TextUtils.isEmpty(paramString1)) {
      localObject = SystemProperties.get("ro.ril.ecclist");
    }
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      for (paramString1 : ((String)localObject).split(",")) {
        if ((!paramBoolean) && (!"BR".equalsIgnoreCase(paramString2)))
        {
          if (str.startsWith(paramString1)) {
            return true;
          }
        }
        else if (str.equals(paramString1)) {
          return true;
        }
      }
      return false;
    }
    Rlog.d("PhoneNumberUtils", "System property doesn't provide any emergency numbers. Use embedded logic for determining ones.");
    if (i < 0) {
      paramString1 = "112,911,000,08,110,118,119,999";
    } else {
      paramString1 = "112,911";
    }
    for (paramString1 : paramString1.split(",")) {
      if (paramBoolean)
      {
        if (str.equals(paramString1)) {
          return true;
        }
      }
      else if (str.startsWith(paramString1)) {
        return true;
      }
    }
    if (paramString2 != null)
    {
      paramString1 = ShortNumberInfo.getInstance();
      if (paramBoolean) {
        return paramString1.isEmergencyNumber(str, paramString2);
      }
      return paramString1.connectsToEmergencyNumber(str, paramString2);
    }
    return false;
  }
  
  private static boolean isEmergencyNumberInternal(int paramInt, String paramString, boolean paramBoolean)
  {
    return isEmergencyNumberInternal(paramInt, paramString, null, paramBoolean);
  }
  
  private static boolean isEmergencyNumberInternal(String paramString1, String paramString2, boolean paramBoolean)
  {
    return isEmergencyNumberInternal(getDefaultVoiceSubId(), paramString1, paramString2, paramBoolean);
  }
  
  private static boolean isEmergencyNumberInternal(String paramString, boolean paramBoolean)
  {
    return isEmergencyNumberInternal(getDefaultVoiceSubId(), paramString, paramBoolean);
  }
  
  public static boolean isGlobalPhoneNumber(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return false;
    }
    return GLOBAL_PHONE_NUMBER_PATTERN.matcher(paramString).matches();
  }
  
  public static boolean isISODigit(char paramChar)
  {
    boolean bool;
    if ((paramChar >= '0') && (paramChar <= '9')) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isInternationalNumber(String paramString1, String paramString2)
  {
    boolean bool1 = TextUtils.isEmpty(paramString1);
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    if ((!paramString1.startsWith("#")) && (!paramString1.startsWith("*")))
    {
      PhoneNumberUtil localPhoneNumberUtil = PhoneNumberUtil.getInstance();
      try
      {
        int i = localPhoneNumberUtil.parseAndKeepRawInput(paramString1, paramString2).getCountryCode();
        int j = localPhoneNumberUtil.getCountryCodeForRegion(paramString2);
        if (i != j) {
          bool2 = true;
        }
        return bool2;
      }
      catch (NumberParseException paramString1)
      {
        return false;
      }
    }
    return false;
  }
  
  public static boolean isLocalEmergencyNumber(Context paramContext, int paramInt, String paramString)
  {
    return isLocalEmergencyNumberInternal(paramInt, paramString, paramContext, true);
  }
  
  public static boolean isLocalEmergencyNumber(Context paramContext, String paramString)
  {
    return isLocalEmergencyNumber(paramContext, getDefaultVoiceSubId(), paramString);
  }
  
  private static boolean isLocalEmergencyNumberInternal(int paramInt, String paramString, Context paramContext, boolean paramBoolean)
  {
    String str = getCountryIso(paramContext);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("isLocalEmergencyNumberInternal");
    ((StringBuilder)localObject).append(str);
    Rlog.w("PhoneNumberUtils", ((StringBuilder)localObject).toString());
    localObject = str;
    if (str == null)
    {
      localObject = getResourcesgetConfigurationlocale.getCountry();
      paramContext = new StringBuilder();
      paramContext.append("No CountryDetector; falling back to countryIso based on locale: ");
      paramContext.append((String)localObject);
      Rlog.w("PhoneNumberUtils", paramContext.toString());
    }
    return isEmergencyNumberInternal(paramInt, paramString, (String)localObject, paramBoolean);
  }
  
  private static boolean isLocalEmergencyNumberInternal(String paramString, Context paramContext, boolean paramBoolean)
  {
    return isLocalEmergencyNumberInternal(getDefaultVoiceSubId(), paramString, paramContext, paramBoolean);
  }
  
  public static boolean isNanp(String paramString)
  {
    boolean bool1 = false;
    boolean bool2;
    if (paramString != null)
    {
      bool2 = bool1;
      if (paramString.length() == 10)
      {
        bool2 = bool1;
        if (isTwoToNine(paramString.charAt(0)))
        {
          bool2 = bool1;
          if (isTwoToNine(paramString.charAt(3)))
          {
            bool1 = true;
            for (int i = 1;; i++)
            {
              bool2 = bool1;
              if (i >= 10) {
                break;
              }
              if (!isISODigit(paramString.charAt(i)))
              {
                bool2 = false;
                break;
              }
            }
          }
        }
      }
    }
    else
    {
      Rlog.e("isNanp: null dialStr passed in", paramString);
      bool2 = bool1;
    }
    return bool2;
  }
  
  public static final boolean isNonSeparator(char paramChar)
  {
    boolean bool;
    if (((paramChar < '0') || (paramChar > '9')) && (paramChar != '*') && (paramChar != '#') && (paramChar != '+') && (paramChar != 'N') && (paramChar != ';') && (paramChar != ',')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isNonSeparator(String paramString)
  {
    int i = 0;
    int j = paramString.length();
    while (i < j)
    {
      if (!isNonSeparator(paramString.charAt(i))) {
        return false;
      }
      i++;
    }
    return true;
  }
  
  private static boolean isOneNanp(String paramString)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramString != null)
    {
      String str = paramString.substring(1);
      bool1 = bool2;
      if (paramString.charAt(0) == '1')
      {
        bool1 = bool2;
        if (isNanp(str)) {
          bool1 = true;
        }
      }
    }
    else
    {
      Rlog.e("isOneNanp: null dialStr passed in", paramString);
    }
    return bool1;
  }
  
  private static boolean isPause(char paramChar)
  {
    boolean bool;
    if ((paramChar != 'p') && (paramChar != 'P')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isPotentialEmergencyNumber(int paramInt, String paramString)
  {
    return isEmergencyNumberInternal(paramInt, paramString, false);
  }
  
  public static boolean isPotentialEmergencyNumber(int paramInt, String paramString1, String paramString2)
  {
    return isEmergencyNumberInternal(paramInt, paramString1, paramString2, false);
  }
  
  public static boolean isPotentialEmergencyNumber(String paramString)
  {
    return isPotentialEmergencyNumber(getDefaultVoiceSubId(), paramString);
  }
  
  public static boolean isPotentialEmergencyNumber(String paramString1, String paramString2)
  {
    return isPotentialEmergencyNumber(getDefaultVoiceSubId(), paramString1, paramString2);
  }
  
  public static boolean isPotentialLocalEmergencyNumber(Context paramContext, int paramInt, String paramString)
  {
    return isLocalEmergencyNumberInternal(paramInt, paramString, paramContext, false);
  }
  
  public static boolean isPotentialLocalEmergencyNumber(Context paramContext, String paramString)
  {
    return isPotentialLocalEmergencyNumber(paramContext, getDefaultVoiceSubId(), paramString);
  }
  
  public static final boolean isReallyDialable(char paramChar)
  {
    boolean bool;
    if (((paramChar < '0') || (paramChar > '9')) && (paramChar != '*') && (paramChar != '#') && (paramChar != '+')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isSeparator(char paramChar)
  {
    boolean bool;
    if ((!isDialable(paramChar)) && (('a' > paramChar) || (paramChar > 'z')) && (('A' > paramChar) || (paramChar > 'Z'))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static final boolean isStartsPostDial(char paramChar)
  {
    boolean bool;
    if ((paramChar != ',') && (paramChar != ';')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isToneWait(char paramChar)
  {
    boolean bool;
    if ((paramChar != 'w') && (paramChar != 'W')) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isTwoToNine(char paramChar)
  {
    return (paramChar >= '2') && (paramChar <= '9');
  }
  
  public static boolean isUriNumber(String paramString)
  {
    boolean bool;
    if ((paramString != null) && ((paramString.contains("@")) || (paramString.contains("%40")))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isVoiceMailNumber(int paramInt, String paramString)
  {
    return isVoiceMailNumber(null, paramInt, paramString);
  }
  
  public static boolean isVoiceMailNumber(Context paramContext, int paramInt, String paramString)
  {
    boolean bool1 = false;
    if (paramContext == null) {
      try
      {
        localObject = TelephonyManager.getDefault();
      }
      catch (SecurityException paramContext)
      {
        break label152;
      }
    } else {
      localObject = TelephonyManager.from(paramContext);
    }
    String str = ((TelephonyManager)localObject).getVoiceMailNumber(paramInt);
    Object localObject = ((TelephonyManager)localObject).getLine1Number(paramInt);
    paramString = extractNetworkPortionAlt(paramString);
    if (TextUtils.isEmpty(paramString)) {
      return false;
    }
    boolean bool2 = false;
    boolean bool3 = bool2;
    if (paramContext != null)
    {
      paramContext = (CarrierConfigManager)paramContext.getSystemService("carrier_config");
      bool3 = bool2;
      if (paramContext != null)
      {
        paramContext = paramContext.getConfigForSubId(paramInt);
        bool3 = bool2;
        if (paramContext != null) {
          bool3 = paramContext.getBoolean("mdn_is_additional_voicemail_number_bool");
        }
      }
    }
    if (bool3)
    {
      if ((!compare(paramString, str)) && (!compare(paramString, (String)localObject))) {
        bool3 = bool1;
      } else {
        bool3 = true;
      }
      return bool3;
    }
    return compare(paramString, str);
    label152:
    return false;
  }
  
  public static boolean isVoiceMailNumber(String paramString)
  {
    return isVoiceMailNumber(SubscriptionManager.getDefaultSubscriptionId(), paramString);
  }
  
  public static boolean isWellFormedSmsAddress(String paramString)
  {
    paramString = extractNetworkPortion(paramString);
    boolean bool;
    if ((!paramString.equals("+")) && (!TextUtils.isEmpty(paramString)) && (isDialable(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static void log(String paramString)
  {
    Rlog.d("PhoneNumberUtils", paramString);
  }
  
  private static boolean matchIntlPrefix(String paramString, int paramInt)
  {
    int i = 0;
    for (int j = 0; j < paramInt; j++)
    {
      char c = paramString.charAt(j);
      if (i != 0)
      {
        if (i != 2)
        {
          if (i != 4)
          {
            if (isNonSeparator(c)) {
              return false;
            }
          }
          else if (c == '1') {
            i = 5;
          } else if (isNonSeparator(c)) {
            return false;
          }
        }
        else if (c == '0') {
          i = 3;
        } else if (c == '1') {
          i = 4;
        } else if (isNonSeparator(c)) {
          return false;
        }
      }
      else if (c == '+') {
        i = 1;
      } else if (c == '0') {
        i = 2;
      } else if (isNonSeparator(c)) {
        return false;
      }
    }
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 1)
    {
      bool2 = bool1;
      if (i != 3) {
        if (i == 5) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  private static boolean matchIntlPrefixAndCC(String paramString, int paramInt)
  {
    boolean bool = false;
    int i = 0;
    for (int j = 0; j < paramInt; j++)
    {
      char c = paramString.charAt(j);
      switch (i)
      {
      default: 
        if (isNonSeparator(c)) {
          return false;
        }
        break;
      case 6: 
      case 7: 
        if (isISODigit(c)) {
          i++;
        } else if (isNonSeparator(c)) {
          return false;
        }
        break;
      case 4: 
        if (c == '1') {
          i = 5;
        } else if (isNonSeparator(c)) {
          return false;
        }
        break;
      case 2: 
        if (c == '0') {
          i = 3;
        } else if (c == '1') {
          i = 4;
        } else if (isNonSeparator(c)) {
          return false;
        }
        break;
      case 1: 
      case 3: 
      case 5: 
        if (isISODigit(c)) {
          i = 6;
        } else if (isNonSeparator(c)) {
          return false;
        }
        break;
      case 0: 
        if (c == '+') {
          i = 1;
        } else if (c == '0') {
          i = 2;
        } else if (isNonSeparator(c)) {
          return false;
        }
        break;
      }
    }
    if ((i != 6) && (i != 7) && (i != 8)) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  private static boolean matchTrunkPrefix(String paramString, int paramInt)
  {
    boolean bool = false;
    for (int i = 0; i < paramInt; i++)
    {
      char c = paramString.charAt(i);
      if ((c == '0') && (!bool)) {
        bool = true;
      } else if (isNonSeparator(c)) {
        return false;
      }
    }
    return bool;
  }
  
  private static int minPositive(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt2 >= 0))
    {
      if (paramInt1 >= paramInt2) {
        paramInt1 = paramInt2;
      }
      return paramInt1;
    }
    if (paramInt1 >= 0) {
      return paramInt1;
    }
    if (paramInt2 >= 0) {
      return paramInt2;
    }
    return -1;
  }
  
  public static byte[] networkPortionToCalledPartyBCD(String paramString)
  {
    return numberToCalledPartyBCDHelper(extractNetworkPortion(paramString), false, 1);
  }
  
  public static byte[] networkPortionToCalledPartyBCDWithLength(String paramString)
  {
    return numberToCalledPartyBCDHelper(extractNetworkPortion(paramString), true, 1);
  }
  
  public static String normalizeNumber(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return "";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      int k = Character.digit(c, 10);
      if (k != -1) {
        localStringBuilder.append(k);
      } else if ((localStringBuilder.length() == 0) && (c == '+')) {
        localStringBuilder.append(c);
      } else if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
        return normalizeNumber(convertKeypadLettersToDigits(paramString));
      }
    }
    return localStringBuilder.toString();
  }
  
  @Deprecated
  public static byte[] numberToCalledPartyBCD(String paramString)
  {
    return numberToCalledPartyBCD(paramString, 1);
  }
  
  public static byte[] numberToCalledPartyBCD(String paramString, int paramInt)
  {
    return numberToCalledPartyBCDHelper(paramString, false, paramInt);
  }
  
  private static byte[] numberToCalledPartyBCDHelper(String paramString, boolean paramBoolean, int paramInt)
  {
    int i = paramString.length();
    int j = i;
    int k;
    if (paramString.indexOf('+') != -1) {
      k = 1;
    } else {
      k = 0;
    }
    int m = j;
    if (k != 0) {
      m = j - 1;
    }
    if (m == 0) {
      return null;
    }
    m = (m + 1) / 2;
    j = 1;
    if (paramBoolean) {
      j = 1 + 1;
    }
    int n = m + j;
    byte[] arrayOfByte = new byte[n];
    int i1 = 0;
    for (m = 0; m < i; m++)
    {
      char c = paramString.charAt(m);
      if (c != '+')
      {
        int i2;
        if ((i1 & 0x1) == 1) {
          i2 = 4;
        } else {
          i2 = 0;
        }
        int i3 = (i1 >> 1) + j;
        int i4 = arrayOfByte[i3];
        arrayOfByte[i3] = ((byte)(byte)((byte)((charToBCD(c, paramInt) & 0xF) << i2) | i4));
        i1++;
      }
    }
    if ((i1 & 0x1) == 1)
    {
      paramInt = (i1 >> 1) + j;
      arrayOfByte[paramInt] = ((byte)(byte)(arrayOfByte[paramInt] | 0xF0));
    }
    paramInt = 0;
    if (paramBoolean)
    {
      arrayOfByte[0] = ((byte)(byte)(n - 1));
      paramInt = 0 + 1;
    }
    if (k != 0) {
      k = 145;
    } else {
      k = 129;
    }
    arrayOfByte[paramInt] = ((byte)(byte)k);
    return arrayOfByte;
  }
  
  private static String processPlusCode(String paramString, boolean paramBoolean)
  {
    String str1 = paramString;
    String str2 = str1;
    if (paramString != null)
    {
      str2 = str1;
      if (paramString.charAt(0) == '+')
      {
        str2 = str1;
        if (paramString.length() > 1)
        {
          str2 = paramString.substring(1);
          if ((!paramBoolean) || (!isOneNanp(str2))) {
            str2 = paramString.replaceFirst("[+]", getCurrentIdp(paramBoolean));
          }
        }
      }
    }
    return str2;
  }
  
  private static void removeDashes(Editable paramEditable)
  {
    int i = 0;
    while (i < paramEditable.length()) {
      if (paramEditable.charAt(i) == '-') {
        paramEditable.delete(i, i + 1);
      } else {
        i++;
      }
    }
  }
  
  public static String replaceUnicodeDigits(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString.length());
    for (char c : paramString.toCharArray())
    {
      int k = Character.digit(c, 10);
      if (k != -1) {
        localStringBuilder.append(k);
      } else {
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }
  
  public static void resetCountryDetectorInfo()
  {
    sCountryDetector = null;
  }
  
  private static String splitAtNonNumerics(CharSequence paramCharSequence)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramCharSequence.length());
    for (int i = 0; i < paramCharSequence.length(); i++)
    {
      Object localObject;
      if (is12Key(paramCharSequence.charAt(i))) {
        localObject = Character.valueOf(paramCharSequence.charAt(i));
      } else {
        localObject = " ";
      }
      localStringBuilder.append(localObject);
    }
    return localStringBuilder.toString().replaceAll(" +", " ").trim();
  }
  
  public static String stringFromStringAndTOA(String paramString, int paramInt)
  {
    if (paramString == null) {
      return null;
    }
    if ((paramInt == 145) && (paramString.length() > 0) && (paramString.charAt(0) != '+'))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("+");
      localStringBuilder.append(paramString);
      return localStringBuilder.toString();
    }
    return paramString;
  }
  
  public static String stripSeparators(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(i);
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      int k = Character.digit(c, 10);
      if (k != -1) {
        localStringBuilder.append(k);
      } else if (isNonSeparator(c)) {
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }
  
  public static String toCallerIDMinMatch(String paramString)
  {
    return internalGetStrippedReversed(extractNetworkPortionAlt(paramString), MIN_MATCH);
  }
  
  public static int toaFromString(String paramString)
  {
    if ((paramString != null) && (paramString.length() > 0) && (paramString.charAt(0) == '+')) {
      return 145;
    }
    return 129;
  }
  
  private static CountryCallingCodeAndNewIndex tryGetCountryCallingCodeAndNewIndex(String paramString, boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    int k = paramString.length();
    for (int m = 0; m < k; m++)
    {
      char c = paramString.charAt(m);
      switch (i)
      {
      default: 
        return null;
      case 9: 
        if (c == '6') {
          return new CountryCallingCodeAndNewIndex(66, m + 1);
        }
        return null;
      case 8: 
        if (c == '6') {
          i = 9;
        } else if (isDialable(c)) {
          return null;
        }
        break;
      case 4: 
        if (c == '1') {
          i = 5;
        } else if (isDialable(c)) {
          return null;
        }
        break;
      case 2: 
        if (c == '0') {
          i = 3;
        } else if (c == '1') {
          i = 4;
        } else if (isDialable(c)) {
          return null;
        }
        break;
      case 1: 
      case 3: 
      case 5: 
      case 6: 
      case 7: 
        int n = tryGetISODigit(c);
        if (n > 0)
        {
          j = j * 10 + n;
          if ((j < 100) && (!isCountryCallingCode(j)))
          {
            if ((i != 1) && (i != 3) && (i != 5)) {
              i++;
            } else {
              i = 6;
            }
          }
          else {
            return new CountryCallingCodeAndNewIndex(j, m + 1);
          }
        }
        else if (isDialable(c))
        {
          return null;
        }
        break;
      case 0: 
        if (c == '+') {
          i = 1;
        } else if (c == '0') {
          i = 2;
        } else if (c == '1')
        {
          if (paramBoolean) {
            i = 8;
          } else {
            return null;
          }
        }
        else if (isDialable(c)) {
          return null;
        }
        break;
      }
    }
    return null;
  }
  
  private static int tryGetISODigit(char paramChar)
  {
    if (('0' <= paramChar) && (paramChar <= '9')) {
      return paramChar - '0';
    }
    return -1;
  }
  
  private static int tryGetTrunkPrefixOmittedIndex(String paramString, int paramInt)
  {
    int i = paramString.length();
    while (paramInt < i)
    {
      char c = paramString.charAt(paramInt);
      if (tryGetISODigit(c) >= 0) {
        return paramInt + 1;
      }
      if (isDialable(c)) {
        return -1;
      }
      paramInt++;
    }
    return -1;
  }
  
  @Deprecated
  public static CharSequence ttsSpanAsPhoneNumber(CharSequence paramCharSequence)
  {
    return createTtsSpannable(paramCharSequence);
  }
  
  @Deprecated
  public static void ttsSpanAsPhoneNumber(Spannable paramSpannable, int paramInt1, int paramInt2)
  {
    addTtsSpan(paramSpannable, paramInt1, paramInt2);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BcdExtendType {}
  
  private static class CountryCallingCodeAndNewIndex
  {
    public final int countryCallingCode;
    public final int newIndex;
    
    public CountryCallingCodeAndNewIndex(int paramInt1, int paramInt2)
    {
      countryCallingCode = paramInt1;
      newIndex = paramInt2;
    }
  }
}
