package com.android.internal.app;

import android.icu.text.ListFormatter;
import android.icu.util.ULocale;
import android.os.LocaleList;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import libcore.icu.ICU;

public class LocaleHelper
{
  public LocaleHelper() {}
  
  public static Locale addLikelySubtags(Locale paramLocale)
  {
    return ICU.addLikelySubtags(paramLocale);
  }
  
  public static String getDisplayCountry(Locale paramLocale)
  {
    return ULocale.getDisplayCountry(paramLocale.toLanguageTag(), ULocale.getDefault());
  }
  
  public static String getDisplayCountry(Locale paramLocale1, Locale paramLocale2)
  {
    String str1 = paramLocale1.toLanguageTag();
    ULocale localULocale = ULocale.forLocale(paramLocale2);
    String str2 = ULocale.getDisplayCountry(str1, localULocale);
    if (paramLocale1.getUnicodeLocaleType("nu") != null) {
      return String.format("%s (%s)", new Object[] { str2, ULocale.getDisplayKeywordValue(str1, "numbers", localULocale) });
    }
    if ("zg_US".equals(paramLocale1.toString())) {
      return ULocale.getDisplayCountry("zg_MM", ULocale.forLocale(paramLocale2)).concat(" (Zawgyi)");
    }
    return str2;
  }
  
  public static String getDisplayLocaleList(LocaleList paramLocaleList, Locale paramLocale, int paramInt)
  {
    if (paramLocale == null) {
      paramLocale = Locale.getDefault();
    }
    int i;
    if (paramLocaleList.size() > paramInt) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if (i != 0)
    {
      j = paramInt;
      k = paramInt + 1;
    }
    else
    {
      k = paramLocaleList.size();
      j = k;
    }
    String[] arrayOfString = new String[k];
    for (int k = 0; k < j; k++) {
      arrayOfString[k] = getDisplayName(paramLocaleList.get(k), paramLocale, false);
    }
    if (i != 0) {
      arrayOfString[paramInt] = TextUtils.getEllipsisString(TextUtils.TruncateAt.END);
    }
    return ListFormatter.getInstance(paramLocale).format((Object[])arrayOfString);
  }
  
  public static String getDisplayName(Locale paramLocale1, Locale paramLocale2, boolean paramBoolean)
  {
    ULocale localULocale = ULocale.forLocale(paramLocale2);
    String str1;
    if (shouldUseDialectName(paramLocale1)) {
      str1 = ULocale.getDisplayNameWithDialect(paramLocale1.toLanguageTag(), localULocale);
    } else {
      str1 = ULocale.getDisplayName(paramLocale1.toLanguageTag(), localULocale);
    }
    String str2 = str1;
    if ("zg_MM".equals(paramLocale1.toString())) {
      str2 = str1.replaceFirst("zg", "Zawgyi");
    }
    if ("zg_US".equals(paramLocale1.toString())) {
      str2 = ULocale.getDisplayLanguage("en_US", localULocale).concat(" (Zawgyi)");
    }
    if (paramBoolean) {
      str2 = toSentenceCase(str2, paramLocale2);
    }
    return str2;
  }
  
  public static String getDisplayName(Locale paramLocale, boolean paramBoolean)
  {
    return getDisplayName(paramLocale, Locale.getDefault(), paramBoolean);
  }
  
  public static String normalizeForSearch(String paramString, Locale paramLocale)
  {
    return paramString.toUpperCase();
  }
  
  private static boolean shouldUseDialectName(Locale paramLocale)
  {
    paramLocale = paramLocale.getLanguage();
    boolean bool;
    if ((!"fa".equals(paramLocale)) && (!"ro".equals(paramLocale)) && (!"zh".equals(paramLocale))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static String toSentenceCase(String paramString, Locale paramLocale)
  {
    if (paramString.isEmpty()) {
      return paramString;
    }
    int i = paramString.offsetByCodePoints(0, 1);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString.substring(0, i).toUpperCase(paramLocale));
    localStringBuilder.append(paramString.substring(i));
    return localStringBuilder.toString();
  }
  
  public static final class LocaleInfoComparator
    implements Comparator<LocaleStore.LocaleInfo>
  {
    private static final String PREFIX_ARABIC = "ال";
    private final Collator mCollator;
    private final boolean mCountryMode;
    
    public LocaleInfoComparator(Locale paramLocale, boolean paramBoolean)
    {
      mCollator = Collator.getInstance(paramLocale);
      mCountryMode = paramBoolean;
    }
    
    private String removePrefixForCompare(Locale paramLocale, String paramString)
    {
      if (("ar".equals(paramLocale.getLanguage())) && (paramString.startsWith("ال"))) {
        return paramString.substring("ال".length());
      }
      return paramString;
    }
    
    public int compare(LocaleStore.LocaleInfo paramLocaleInfo1, LocaleStore.LocaleInfo paramLocaleInfo2)
    {
      if (paramLocaleInfo1.isSuggested() == paramLocaleInfo2.isSuggested()) {
        return mCollator.compare(removePrefixForCompare(paramLocaleInfo1.getLocale(), paramLocaleInfo1.getLabel(mCountryMode)), removePrefixForCompare(paramLocaleInfo2.getLocale(), paramLocaleInfo2.getLabel(mCountryMode)));
      }
      int i;
      if (paramLocaleInfo1.isSuggested()) {
        i = -1;
      } else {
        i = 1;
      }
      return i;
    }
  }
}
