package com.android.internal.telephony;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.app.LocaleStore;
import com.android.internal.app.LocaleStore.LocaleInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import libcore.icu.ICU;
import libcore.util.TimeZoneFinder;

public final class MccTable
{
  private static final Map<Locale, Locale> FALLBACKS = new HashMap();
  static final String LOG_TAG = "MccTable";
  static ArrayList<MccEntry> sTable;
  
  static
  {
    FALLBACKS.put(Locale.ENGLISH, Locale.US);
    sTable = new ArrayList(240);
    sTable.add(new MccEntry(202, "gr", 2));
    sTable.add(new MccEntry(204, "nl", 2));
    sTable.add(new MccEntry(206, "be", 2));
    sTable.add(new MccEntry(208, "fr", 2));
    sTable.add(new MccEntry(212, "mc", 2));
    sTable.add(new MccEntry(213, "ad", 2));
    sTable.add(new MccEntry(214, "es", 2));
    sTable.add(new MccEntry(216, "hu", 2));
    sTable.add(new MccEntry(218, "ba", 2));
    sTable.add(new MccEntry(219, "hr", 2));
    sTable.add(new MccEntry(220, "rs", 2));
    sTable.add(new MccEntry(222, "it", 2));
    sTable.add(new MccEntry(225, "va", 2));
    sTable.add(new MccEntry(226, "ro", 2));
    sTable.add(new MccEntry(228, "ch", 2));
    sTable.add(new MccEntry(230, "cz", 2));
    sTable.add(new MccEntry(231, "sk", 2));
    sTable.add(new MccEntry(232, "at", 2));
    sTable.add(new MccEntry(234, "gb", 2));
    sTable.add(new MccEntry(235, "gb", 2));
    sTable.add(new MccEntry(238, "dk", 2));
    sTable.add(new MccEntry(240, "se", 2));
    sTable.add(new MccEntry(242, "no", 2));
    sTable.add(new MccEntry(244, "fi", 2));
    sTable.add(new MccEntry(246, "lt", 2));
    sTable.add(new MccEntry(247, "lv", 2));
    sTable.add(new MccEntry(248, "ee", 2));
    sTable.add(new MccEntry(250, "ru", 2));
    sTable.add(new MccEntry(255, "ua", 2));
    sTable.add(new MccEntry(257, "by", 2));
    sTable.add(new MccEntry(259, "md", 2));
    sTable.add(new MccEntry(260, "pl", 2));
    sTable.add(new MccEntry(262, "de", 2));
    sTable.add(new MccEntry(266, "gi", 2));
    sTable.add(new MccEntry(268, "pt", 2));
    sTable.add(new MccEntry(270, "lu", 2));
    sTable.add(new MccEntry(272, "ie", 2));
    sTable.add(new MccEntry(274, "is", 2));
    sTable.add(new MccEntry(276, "al", 2));
    sTable.add(new MccEntry(278, "mt", 2));
    sTable.add(new MccEntry(280, "cy", 2));
    sTable.add(new MccEntry(282, "ge", 2));
    sTable.add(new MccEntry(283, "am", 2));
    sTable.add(new MccEntry(284, "bg", 2));
    sTable.add(new MccEntry(286, "tr", 2));
    sTable.add(new MccEntry(288, "fo", 2));
    sTable.add(new MccEntry(289, "ge", 2));
    sTable.add(new MccEntry(290, "gl", 2));
    sTable.add(new MccEntry(292, "sm", 2));
    sTable.add(new MccEntry(293, "si", 2));
    sTable.add(new MccEntry(294, "mk", 2));
    sTable.add(new MccEntry(295, "li", 2));
    sTable.add(new MccEntry(297, "me", 2));
    sTable.add(new MccEntry(302, "ca", 3));
    sTable.add(new MccEntry(308, "pm", 2));
    sTable.add(new MccEntry(310, "us", 3));
    sTable.add(new MccEntry(311, "us", 3));
    sTable.add(new MccEntry(312, "us", 3));
    sTable.add(new MccEntry(313, "us", 3));
    sTable.add(new MccEntry(314, "us", 3));
    sTable.add(new MccEntry(315, "us", 3));
    sTable.add(new MccEntry(316, "us", 3));
    sTable.add(new MccEntry(330, "pr", 2));
    sTable.add(new MccEntry(332, "vi", 2));
    sTable.add(new MccEntry(334, "mx", 3));
    sTable.add(new MccEntry(338, "jm", 3));
    sTable.add(new MccEntry(340, "gp", 2));
    sTable.add(new MccEntry(342, "bb", 3));
    sTable.add(new MccEntry(344, "ag", 3));
    sTable.add(new MccEntry(346, "ky", 3));
    sTable.add(new MccEntry(348, "vg", 3));
    sTable.add(new MccEntry(350, "bm", 2));
    sTable.add(new MccEntry(352, "gd", 2));
    sTable.add(new MccEntry(354, "ms", 2));
    sTable.add(new MccEntry(356, "kn", 2));
    sTable.add(new MccEntry(358, "lc", 2));
    sTable.add(new MccEntry(360, "vc", 2));
    sTable.add(new MccEntry(362, "ai", 2));
    sTable.add(new MccEntry(363, "aw", 2));
    sTable.add(new MccEntry(364, "bs", 2));
    sTable.add(new MccEntry(365, "ai", 3));
    sTable.add(new MccEntry(366, "dm", 2));
    sTable.add(new MccEntry(368, "cu", 2));
    sTable.add(new MccEntry(370, "do", 2));
    sTable.add(new MccEntry(372, "ht", 2));
    sTable.add(new MccEntry(374, "tt", 2));
    sTable.add(new MccEntry(376, "tc", 2));
    sTable.add(new MccEntry(400, "az", 2));
    sTable.add(new MccEntry(401, "kz", 2));
    sTable.add(new MccEntry(402, "bt", 2));
    sTable.add(new MccEntry(404, "in", 2));
    sTable.add(new MccEntry(405, "in", 2));
    sTable.add(new MccEntry(406, "in", 2));
    sTable.add(new MccEntry(410, "pk", 2));
    sTable.add(new MccEntry(412, "af", 2));
    sTable.add(new MccEntry(413, "lk", 2));
    sTable.add(new MccEntry(414, "mm", 2));
    sTable.add(new MccEntry(415, "lb", 2));
    sTable.add(new MccEntry(416, "jo", 2));
    sTable.add(new MccEntry(417, "sy", 2));
    sTable.add(new MccEntry(418, "iq", 2));
    sTable.add(new MccEntry(419, "kw", 2));
    sTable.add(new MccEntry(420, "sa", 2));
    sTable.add(new MccEntry(421, "ye", 2));
    sTable.add(new MccEntry(422, "om", 2));
    sTable.add(new MccEntry(423, "ps", 2));
    sTable.add(new MccEntry(424, "ae", 2));
    sTable.add(new MccEntry(425, "il", 2));
    sTable.add(new MccEntry(426, "bh", 2));
    sTable.add(new MccEntry(427, "qa", 2));
    sTable.add(new MccEntry(428, "mn", 2));
    sTable.add(new MccEntry(429, "np", 2));
    sTable.add(new MccEntry(430, "ae", 2));
    sTable.add(new MccEntry(431, "ae", 2));
    sTable.add(new MccEntry(432, "ir", 2));
    sTable.add(new MccEntry(434, "uz", 2));
    sTable.add(new MccEntry(436, "tj", 2));
    sTable.add(new MccEntry(437, "kg", 2));
    sTable.add(new MccEntry(438, "tm", 2));
    sTable.add(new MccEntry(440, "jp", 2));
    sTable.add(new MccEntry(441, "jp", 2));
    sTable.add(new MccEntry(450, "kr", 2));
    sTable.add(new MccEntry(452, "vn", 2));
    sTable.add(new MccEntry(454, "hk", 2));
    sTable.add(new MccEntry(455, "mo", 2));
    sTable.add(new MccEntry(456, "kh", 2));
    sTable.add(new MccEntry(457, "la", 2));
    sTable.add(new MccEntry(460, "cn", 2));
    sTable.add(new MccEntry(461, "cn", 2));
    sTable.add(new MccEntry(466, "tw", 2));
    sTable.add(new MccEntry(467, "kp", 2));
    sTable.add(new MccEntry(470, "bd", 2));
    sTable.add(new MccEntry(472, "mv", 2));
    sTable.add(new MccEntry(502, "my", 2));
    sTable.add(new MccEntry(505, "au", 2));
    sTable.add(new MccEntry(510, "id", 2));
    sTable.add(new MccEntry(514, "tl", 2));
    sTable.add(new MccEntry(515, "ph", 2));
    sTable.add(new MccEntry(520, "th", 2));
    sTable.add(new MccEntry(525, "sg", 2));
    sTable.add(new MccEntry(528, "bn", 2));
    sTable.add(new MccEntry(530, "nz", 2));
    sTable.add(new MccEntry(534, "mp", 2));
    sTable.add(new MccEntry(535, "gu", 2));
    sTable.add(new MccEntry(536, "nr", 2));
    sTable.add(new MccEntry(537, "pg", 2));
    sTable.add(new MccEntry(539, "to", 2));
    sTable.add(new MccEntry(540, "sb", 2));
    sTable.add(new MccEntry(541, "vu", 2));
    sTable.add(new MccEntry(542, "fj", 2));
    sTable.add(new MccEntry(543, "wf", 2));
    sTable.add(new MccEntry(544, "as", 2));
    sTable.add(new MccEntry(545, "ki", 2));
    sTable.add(new MccEntry(546, "nc", 2));
    sTable.add(new MccEntry(547, "pf", 2));
    sTable.add(new MccEntry(548, "ck", 2));
    sTable.add(new MccEntry(549, "ws", 2));
    sTable.add(new MccEntry(550, "fm", 2));
    sTable.add(new MccEntry(551, "mh", 2));
    sTable.add(new MccEntry(552, "pw", 2));
    sTable.add(new MccEntry(553, "tv", 2));
    sTable.add(new MccEntry(555, "nu", 2));
    sTable.add(new MccEntry(602, "eg", 2));
    sTable.add(new MccEntry(603, "dz", 2));
    sTable.add(new MccEntry(604, "ma", 2));
    sTable.add(new MccEntry(605, "tn", 2));
    sTable.add(new MccEntry(606, "ly", 2));
    sTable.add(new MccEntry(607, "gm", 2));
    sTable.add(new MccEntry(608, "sn", 2));
    sTable.add(new MccEntry(609, "mr", 2));
    sTable.add(new MccEntry(610, "ml", 2));
    sTable.add(new MccEntry(611, "gn", 2));
    sTable.add(new MccEntry(612, "ci", 2));
    sTable.add(new MccEntry(613, "bf", 2));
    sTable.add(new MccEntry(614, "ne", 2));
    sTable.add(new MccEntry(615, "tg", 2));
    sTable.add(new MccEntry(616, "bj", 2));
    sTable.add(new MccEntry(617, "mu", 2));
    sTable.add(new MccEntry(618, "lr", 2));
    sTable.add(new MccEntry(619, "sl", 2));
    sTable.add(new MccEntry(620, "gh", 2));
    sTable.add(new MccEntry(621, "ng", 2));
    sTable.add(new MccEntry(622, "td", 2));
    sTable.add(new MccEntry(623, "cf", 2));
    sTable.add(new MccEntry(624, "cm", 2));
    sTable.add(new MccEntry(625, "cv", 2));
    sTable.add(new MccEntry(626, "st", 2));
    sTable.add(new MccEntry(627, "gq", 2));
    sTable.add(new MccEntry(628, "ga", 2));
    sTable.add(new MccEntry(629, "cg", 2));
    sTable.add(new MccEntry(630, "cd", 2));
    sTable.add(new MccEntry(631, "ao", 2));
    sTable.add(new MccEntry(632, "gw", 2));
    sTable.add(new MccEntry(633, "sc", 2));
    sTable.add(new MccEntry(634, "sd", 2));
    sTable.add(new MccEntry(635, "rw", 2));
    sTable.add(new MccEntry(636, "et", 2));
    sTable.add(new MccEntry(637, "so", 2));
    sTable.add(new MccEntry(638, "dj", 2));
    sTable.add(new MccEntry(639, "ke", 2));
    sTable.add(new MccEntry(640, "tz", 2));
    sTable.add(new MccEntry(641, "ug", 2));
    sTable.add(new MccEntry(642, "bi", 2));
    sTable.add(new MccEntry(643, "mz", 2));
    sTable.add(new MccEntry(645, "zm", 2));
    sTable.add(new MccEntry(646, "mg", 2));
    sTable.add(new MccEntry(647, "re", 2));
    sTable.add(new MccEntry(648, "zw", 2));
    sTable.add(new MccEntry(649, "na", 2));
    sTable.add(new MccEntry(650, "mw", 2));
    sTable.add(new MccEntry(651, "ls", 2));
    sTable.add(new MccEntry(652, "bw", 2));
    sTable.add(new MccEntry(653, "sz", 2));
    sTable.add(new MccEntry(654, "km", 2));
    sTable.add(new MccEntry(655, "za", 2));
    sTable.add(new MccEntry(657, "er", 2));
    sTable.add(new MccEntry(658, "sh", 2));
    sTable.add(new MccEntry(659, "ss", 2));
    sTable.add(new MccEntry(702, "bz", 2));
    sTable.add(new MccEntry(704, "gt", 2));
    sTable.add(new MccEntry(706, "sv", 2));
    sTable.add(new MccEntry(708, "hn", 3));
    sTable.add(new MccEntry(710, "ni", 2));
    sTable.add(new MccEntry(712, "cr", 2));
    sTable.add(new MccEntry(714, "pa", 2));
    sTable.add(new MccEntry(716, "pe", 2));
    sTable.add(new MccEntry(722, "ar", 3));
    sTable.add(new MccEntry(724, "br", 2));
    sTable.add(new MccEntry(730, "cl", 2));
    sTable.add(new MccEntry(732, "co", 3));
    sTable.add(new MccEntry(734, "ve", 2));
    sTable.add(new MccEntry(736, "bo", 2));
    sTable.add(new MccEntry(738, "gy", 2));
    sTable.add(new MccEntry(740, "ec", 2));
    sTable.add(new MccEntry(742, "gf", 2));
    sTable.add(new MccEntry(744, "py", 2));
    sTable.add(new MccEntry(746, "sr", 2));
    sTable.add(new MccEntry(748, "uy", 2));
    sTable.add(new MccEntry(750, "fk", 2));
    Collections.sort(sTable);
  }
  
  public MccTable() {}
  
  public static String countryCodeForMcc(int paramInt)
  {
    MccEntry localMccEntry = entryForMcc(paramInt);
    if (localMccEntry == null) {
      return "";
    }
    return mIso;
  }
  
  public static String defaultLanguageForMcc(int paramInt)
  {
    Object localObject = entryForMcc(paramInt);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("defaultLanguageForMcc(");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append("): no country for mcc");
      Slog.d("MccTable", ((StringBuilder)localObject).toString());
      return null;
    }
    localObject = mIso;
    if ("in".equals(localObject)) {
      return "en";
    }
    String str = ICU.addLikelySubtags(new Locale("und", (String)localObject)).getLanguage();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("defaultLanguageForMcc(");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("): country ");
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" uses ");
    localStringBuilder.append(str);
    Slog.d("MccTable", localStringBuilder.toString());
    return str;
  }
  
  public static String defaultTimeZoneForMcc(int paramInt)
  {
    Object localObject = entryForMcc(paramInt);
    if (localObject == null) {
      return null;
    }
    localObject = mIso;
    return TimeZoneFinder.getInstance().lookupDefaultTimeZoneIdByCountry((String)localObject);
  }
  
  private static MccEntry entryForMcc(int paramInt)
  {
    MccEntry localMccEntry = new MccEntry(paramInt, "", 0);
    paramInt = Collections.binarySearch(sTable, localMccEntry);
    if (paramInt < 0) {
      return null;
    }
    return (MccEntry)sTable.get(paramInt);
  }
  
  private static Locale getLocaleForLanguageCountry(Context paramContext, String paramString1, String paramString2)
  {
    if (paramString1 == null)
    {
      Slog.d("MccTable", "getLocaleForLanguageCountry: skipping no language");
      return null;
    }
    Object localObject1 = paramString2;
    if (paramString2 == null) {
      localObject1 = "";
    }
    paramString2 = new Locale(paramString1, (String)localObject1);
    try
    {
      localObject1 = paramContext.getAssets().getLocales();
      Object localObject2 = new java/util/ArrayList;
      ((ArrayList)localObject2).<init>(Arrays.asList((Object[])localObject1));
      ((List)localObject2).remove("ar-XB");
      ((List)localObject2).remove("en-XA");
      localObject1 = new java/util/ArrayList;
      ((ArrayList)localObject1).<init>();
      Iterator localIterator = ((List)localObject2).iterator();
      while (localIterator.hasNext())
      {
        localObject2 = Locale.forLanguageTag(((String)localIterator.next()).replace('_', '-'));
        if ((localObject2 != null) && (!"und".equals(((Locale)localObject2).getLanguage())) && (!((Locale)localObject2).getLanguage().isEmpty()) && (!((Locale)localObject2).getCountry().isEmpty())) {
          if (((Locale)localObject2).getLanguage().equals(paramString2.getLanguage()))
          {
            if (((Locale)localObject2).getCountry().equals(paramString2.getCountry()))
            {
              paramContext = new java/lang/StringBuilder;
              paramContext.<init>();
              paramContext.append("getLocaleForLanguageCountry: got perfect match: ");
              paramContext.append(((Locale)localObject2).toLanguageTag());
              Slog.d("MccTable", paramContext.toString());
              return localObject2;
            }
            ((List)localObject1).add(localObject2);
          }
        }
      }
      if (((List)localObject1).isEmpty())
      {
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append("getLocaleForLanguageCountry: no locales for language ");
        paramContext.append(paramString1);
        Slog.d("MccTable", paramContext.toString());
        return null;
      }
      localObject2 = lookupFallback(paramString2, (List)localObject1);
      if (localObject2 != null)
      {
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append("getLocaleForLanguageCountry: got a fallback match: ");
        paramContext.append(((Locale)localObject2).toLanguageTag());
        Slog.d("MccTable", paramContext.toString());
        return localObject2;
      }
      if (!TextUtils.isEmpty(paramString2.getCountry()))
      {
        LocaleStore.fillCache(paramContext);
        if (LocaleStore.getLocaleInfo(paramString2).isTranslated())
        {
          paramContext = new java/lang/StringBuilder;
          paramContext.<init>();
          paramContext.append("getLocaleForLanguageCountry: target locale is translated: ");
          paramContext.append(paramString2);
          Slog.d("MccTable", paramContext.toString());
          return paramString2;
        }
      }
      paramContext = new java/lang/StringBuilder;
      paramContext.<init>();
      paramContext.append("getLocaleForLanguageCountry: got language-only match: ");
      paramContext.append(paramString1);
      Slog.d("MccTable", paramContext.toString());
      paramContext = (Locale)((List)localObject1).get(0);
      return paramContext;
    }
    catch (Exception paramContext)
    {
      Slog.d("MccTable", "getLocaleForLanguageCountry: exception", paramContext);
    }
    return null;
  }
  
  public static Locale getLocaleFromMcc(Context paramContext, int paramInt, String paramString)
  {
    boolean bool = TextUtils.isEmpty(paramString) ^ true;
    if (!bool) {
      paramString = defaultLanguageForMcc(paramInt);
    }
    String str = countryCodeForMcc(paramInt);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getLocaleFromMcc(");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(str);
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramInt);
    Slog.d("MccTable", ((StringBuilder)localObject).toString());
    paramString = getLocaleForLanguageCountry(paramContext, paramString, str);
    if ((paramString == null) && (bool))
    {
      localObject = defaultLanguageForMcc(paramInt);
      paramString = new StringBuilder();
      paramString.append("[retry ] getLocaleFromMcc(");
      paramString.append((String)localObject);
      paramString.append(", ");
      paramString.append(str);
      paramString.append(", ");
      paramString.append(paramInt);
      Slog.d("MccTable", paramString.toString());
      return getLocaleForLanguageCountry(paramContext, (String)localObject, str);
    }
    return paramString;
  }
  
  private static boolean isInvalidOperatorNumeric(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (paramString.length() >= 5) && (!paramString.startsWith("000"))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static Locale lookupFallback(Locale paramLocale, List<Locale> paramList)
  {
    Locale localLocale = paramLocale;
    do
    {
      localLocale = (Locale)FALLBACKS.get(localLocale);
      paramLocale = localLocale;
      if (localLocale == null) {
        break;
      }
      localLocale = paramLocale;
    } while (!paramList.contains(paramLocale));
    return paramLocale;
    return null;
  }
  
  private static void setTimezoneFromMccIfNeeded(Context paramContext, int paramInt)
  {
    if (!TimeServiceHelper.isTimeZoneSettingInitializedStatic())
    {
      String str = defaultTimeZoneForMcc(paramInt);
      if ((str != null) && (str.length() > 0))
      {
        TimeServiceHelper.setDeviceTimeZoneStatic(paramContext, str);
        paramContext = new StringBuilder();
        paramContext.append("timezone set to ");
        paramContext.append(str);
        Slog.d("MccTable", paramContext.toString());
      }
    }
  }
  
  private static void setWifiCountryCodeFromMcc(Context paramContext, int paramInt)
  {
    SubscriptionManager localSubscriptionManager = SubscriptionManager.from(paramContext);
    Object localObject1 = TelephonyManager.from(paramContext);
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("setWifiCountryCodeFromMcc mcc = ");
    ((StringBuilder)localObject2).append(paramInt);
    Slog.d("MccTable", ((StringBuilder)localObject2).toString());
    localObject2 = localSubscriptionManager.getActiveSubscriptionIdList();
    int i = 0;
    if ((paramInt == 0) && (localObject2 != null) && (localObject2.length > 0))
    {
      int j = localObject2.length;
      int m;
      for (int k = 0;; k++)
      {
        m = i;
        if (k >= j) {
          break;
        }
        m = localObject2[k];
        if (!isInvalidOperatorNumeric(((TelephonyManager)localObject1).getNetworkOperatorForPhone(SubscriptionController.getInstance().getPhoneId(m))))
        {
          Slog.d("MccTable", "operatornumeric is valid, Do not overwrite");
          m = 1;
          break;
        }
      }
      if (m != 0) {
        return;
      }
    }
    localObject2 = countryCodeForMcc(paramInt);
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("WIFI_COUNTRY_CODE set to ");
    ((StringBuilder)localObject1).append((String)localObject2);
    Slog.d("MccTable", ((StringBuilder)localObject1).toString());
    ((WifiManager)paramContext.getSystemService("wifi")).setCountryCode((String)localObject2);
  }
  
  public static int smallestDigitsMccForMnc(int paramInt)
  {
    MccEntry localMccEntry = entryForMcc(paramInt);
    if (localMccEntry == null) {
      return 2;
    }
    return mSmallestDigitsMnc;
  }
  
  public static void updateMccMncConfiguration(Context paramContext, String paramString, boolean paramBoolean)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("updateMccMncConfiguration mccmnc='");
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append("' fromServiceState=");
    ((StringBuilder)localObject1).append(paramBoolean);
    Slog.d("MccTable", ((StringBuilder)localObject1).toString());
    localObject1 = paramString;
    Object localObject2;
    if (Build.IS_DEBUGGABLE)
    {
      localObject2 = SystemProperties.get("persist.sys.override_mcc");
      localObject1 = paramString;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject1 = localObject2;
        paramString = new StringBuilder();
        paramString.append("updateMccMncConfiguration overriding mccmnc='");
        paramString.append((String)localObject1);
        paramString.append("'");
        Slog.d("MccTable", paramString.toString());
      }
    }
    if (!TextUtils.isEmpty((CharSequence)localObject1))
    {
      paramString = TelephonyManager.getDefault().getSimOperatorNumeric();
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("updateMccMncConfiguration defaultMccMnc=");
      ((StringBuilder)localObject2).append(paramString);
      Slog.d("MccTable", ((StringBuilder)localObject2).toString());
      try
      {
        int i = Integer.parseInt(((String)localObject1).substring(0, 3));
        int j = Integer.parseInt(((String)localObject1).substring(3));
        paramString = new StringBuilder();
        paramString.append("updateMccMncConfiguration: mcc=");
        paramString.append(i);
        paramString.append(", mnc=");
        paramString.append(j);
        Slog.d("MccTable", paramString.toString());
        if (i != 0) {
          setTimezoneFromMccIfNeeded(paramContext, i);
        }
        if (paramBoolean) {
          setWifiCountryCodeFromMcc(paramContext, i);
        } else {
          try
          {
            paramContext = new android/content/res/Configuration;
            paramContext.<init>();
            int k = 0;
            if (i != 0)
            {
              mcc = i;
              if (j == 0) {
                k = 65535;
              } else {
                k = j;
              }
              mnc = k;
              k = 1;
            }
            if (k != 0)
            {
              paramString = new java/lang/StringBuilder;
              paramString.<init>();
              paramString.append("updateMccMncConfiguration updateConfig config=");
              paramString.append(paramContext);
              Slog.d("MccTable", paramString.toString());
              ActivityManager.getService().updateConfiguration(paramContext);
            }
            else
            {
              Slog.d("MccTable", "updateMccMncConfiguration nothing to update");
            }
          }
          catch (RemoteException paramContext)
          {
            Slog.e("MccTable", "Can't update configuration", paramContext);
          }
        }
      }
      catch (NumberFormatException paramContext)
      {
        paramContext = new StringBuilder();
        paramContext.append("Error parsing IMSI: ");
        paramContext.append((String)localObject1);
        Slog.e("MccTable", paramContext.toString());
        return;
      }
    }
    if (paramBoolean) {
      setWifiCountryCodeFromMcc(paramContext, 0);
    }
  }
  
  static class MccEntry
    implements Comparable<MccEntry>
  {
    final String mIso;
    final int mMcc;
    final int mSmallestDigitsMnc;
    
    MccEntry(int paramInt1, String paramString, int paramInt2)
    {
      if (paramString != null)
      {
        mMcc = paramInt1;
        mIso = paramString;
        mSmallestDigitsMnc = paramInt2;
        return;
      }
      throw new NullPointerException();
    }
    
    public int compareTo(MccEntry paramMccEntry)
    {
      return mMcc - mMcc;
    }
  }
}
