package com.android.internal.app;

import android.content.ContentResolver;
import android.content.Context;
import android.os.LocaleList;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IllformedLocaleException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Set;

public class LocaleStore
{
  private static boolean sFullyInitialized = false;
  private static final HashMap<String, LocaleInfo> sLocaleCache = new HashMap();
  
  public LocaleStore() {}
  
  private static void addSuggestedLocalesForRegion(Locale paramLocale)
  {
    if (paramLocale == null) {
      return;
    }
    paramLocale = paramLocale.getCountry();
    if (paramLocale.isEmpty()) {
      return;
    }
    Iterator localIterator = sLocaleCache.values().iterator();
    while (localIterator.hasNext())
    {
      LocaleInfo localLocaleInfo = (LocaleInfo)localIterator.next();
      if (paramLocale.equals(localLocaleInfo.getLocale().getCountry())) {
        LocaleInfo.access$076(localLocaleInfo, 1);
      }
    }
  }
  
  public static void fillCache(Context paramContext)
  {
    if (sFullyInitialized) {
      return;
    }
    Object localObject1 = getSimCountries(paramContext);
    Object localObject2 = paramContext.getContentResolver();
    int i = 0;
    if (Settings.Global.getInt((ContentResolver)localObject2, "development_settings_enabled", 0) != 0) {
      j = 1;
    } else {
      j = 0;
    }
    paramContext = LocalePicker.getSupportedLocales(paramContext);
    int k = paramContext.length;
    int m = 0;
    Object localObject3;
    while (m < k)
    {
      localObject2 = paramContext[m];
      if (!((String)localObject2).isEmpty())
      {
        localObject2 = new LocaleInfo((String)localObject2, null);
        if (LocaleList.isPseudoLocale(((LocaleInfo)localObject2).getLocale()))
        {
          if (j != 0)
          {
            ((LocaleInfo)localObject2).setTranslated(true);
            LocaleInfo.access$202((LocaleInfo)localObject2, true);
            LocaleInfo.access$076((LocaleInfo)localObject2, 1);
          }
        }
        else
        {
          if (((Set)localObject1).contains(((LocaleInfo)localObject2).getLocale().getCountry())) {
            LocaleInfo.access$076((LocaleInfo)localObject2, 1);
          }
          sLocaleCache.put(((LocaleInfo)localObject2).getId(), localObject2);
          localObject2 = ((LocaleInfo)localObject2).getParent();
          if (localObject2 != null)
          {
            localObject3 = ((Locale)localObject2).toLanguageTag();
            if (!sLocaleCache.containsKey(localObject3)) {
              sLocaleCache.put(localObject3, new LocaleInfo((Locale)localObject2, null));
            }
          }
        }
        m++;
      }
      else
      {
        throw new IllformedLocaleException("Bad locale entry in locale_config.xml");
      }
    }
    localObject1 = new HashSet();
    localObject2 = LocalePicker.getSystemAssetLocales();
    m = localObject2.length;
    for (int j = i; j < m; j++)
    {
      localObject3 = new LocaleInfo(localObject2[j], null);
      String str = ((LocaleInfo)localObject3).getLocale().getCountry();
      if (!str.isEmpty())
      {
        paramContext = null;
        if (sLocaleCache.containsKey(((LocaleInfo)localObject3).getId()))
        {
          paramContext = (LocaleInfo)sLocaleCache.get(((LocaleInfo)localObject3).getId());
        }
        else
        {
          Object localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append(((LocaleInfo)localObject3).getLangScriptKey());
          ((StringBuilder)localObject4).append("-");
          ((StringBuilder)localObject4).append(str);
          localObject4 = ((StringBuilder)localObject4).toString();
          if (sLocaleCache.containsKey(localObject4)) {
            paramContext = (LocaleInfo)sLocaleCache.get(localObject4);
          }
        }
        if (paramContext != null) {
          LocaleInfo.access$076(paramContext, 2);
        }
      }
      ((HashSet)localObject1).add(((LocaleInfo)localObject3).getLangScriptKey());
    }
    localObject2 = sLocaleCache.values().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      paramContext = (LocaleInfo)((Iterator)localObject2).next();
      paramContext.setTranslated(((HashSet)localObject1).contains(paramContext.getLangScriptKey()));
    }
    addSuggestedLocalesForRegion(Locale.getDefault());
    sFullyInitialized = true;
  }
  
  private static int getLevel(Set<String> paramSet, LocaleInfo paramLocaleInfo, boolean paramBoolean)
  {
    if (paramSet.contains(paramLocaleInfo.getId())) {
      return 0;
    }
    if (mIsPseudo) {
      return 0;
    }
    if ((paramBoolean) && (!paramLocaleInfo.isTranslated())) {
      return 0;
    }
    if (paramLocaleInfo.getParent() != null) {
      return 2;
    }
    return 0;
  }
  
  public static Set<LocaleInfo> getLevelLocales(Context paramContext, Set<String> paramSet, LocaleInfo paramLocaleInfo, boolean paramBoolean)
  {
    fillCache(paramContext);
    if (paramLocaleInfo == null) {
      paramContext = null;
    } else {
      paramContext = paramLocaleInfo.getId();
    }
    HashSet localHashSet = new HashSet();
    Iterator localIterator = sLocaleCache.values().iterator();
    while (localIterator.hasNext())
    {
      LocaleInfo localLocaleInfo = (LocaleInfo)localIterator.next();
      if (getLevel(paramSet, localLocaleInfo, paramBoolean) == 2) {
        if (paramLocaleInfo != null)
        {
          if (localLocaleInfo.toString().equals("zg-US"))
          {
            if (paramContext.equals("en")) {
              localHashSet.add(localLocaleInfo);
            }
          }
          else if (paramContext.equals(localLocaleInfo.getParent().toLanguageTag())) {
            localHashSet.add(localLocaleInfo);
          }
        }
        else if (localLocaleInfo.isSuggestionOfType(1)) {
          localHashSet.add(localLocaleInfo);
        } else {
          localHashSet.add(getLocaleInfo(localLocaleInfo.getParent()));
        }
      }
    }
    return localHashSet;
  }
  
  public static LocaleInfo getLocaleInfo(Locale paramLocale)
  {
    String str = paramLocale.toLanguageTag();
    if (!sLocaleCache.containsKey(str))
    {
      paramLocale = new LocaleInfo(paramLocale, null);
      if ((!"zh".equals(str)) && (!"zh-TW".equals(str)) && (!"zh-CN".equals(str)) && (!"zh-HK".equals(str))) {
        sLocaleCache.put(str, paramLocale);
      } else {
        return paramLocale;
      }
    }
    else
    {
      paramLocale = (LocaleInfo)sLocaleCache.get(str);
    }
    return paramLocale;
  }
  
  private static Set<String> getSimCountries(Context paramContext)
  {
    HashSet localHashSet = new HashSet();
    TelephonyManager localTelephonyManager = TelephonyManager.from(paramContext);
    if (localTelephonyManager != null)
    {
      paramContext = localTelephonyManager.getSimCountryIso().toUpperCase(Locale.US);
      if (!paramContext.isEmpty()) {
        localHashSet.add(paramContext);
      }
      paramContext = localTelephonyManager.getNetworkCountryIso().toUpperCase(Locale.US);
      if (!paramContext.isEmpty()) {
        localHashSet.add(paramContext);
      }
    }
    return localHashSet;
  }
  
  public static void updateSimCountries(Context paramContext)
  {
    paramContext = getSimCountries(paramContext);
    Iterator localIterator = sLocaleCache.values().iterator();
    while (localIterator.hasNext())
    {
      LocaleInfo localLocaleInfo = (LocaleInfo)localIterator.next();
      if (paramContext.contains(localLocaleInfo.getLocale().getCountry())) {
        LocaleInfo.access$076(localLocaleInfo, 1);
      }
    }
  }
  
  public static class LocaleInfo
  {
    private static final int SUGGESTION_TYPE_CFG = 2;
    private static final int SUGGESTION_TYPE_NONE = 0;
    private static final int SUGGESTION_TYPE_SIM = 1;
    private String mFullCountryNameNative;
    private String mFullNameNative;
    private final String mId;
    private boolean mIsChecked;
    private boolean mIsPseudo;
    private boolean mIsTranslated;
    private String mLangScriptKey;
    private final Locale mLocale;
    private final Locale mParent;
    private int mSuggestionFlags;
    
    private LocaleInfo(String paramString)
    {
      this(Locale.forLanguageTag(paramString));
    }
    
    private LocaleInfo(Locale paramLocale)
    {
      mLocale = paramLocale;
      mId = paramLocale.toLanguageTag();
      mParent = getParent(paramLocale);
      mIsChecked = false;
      mSuggestionFlags = 0;
      mIsTranslated = false;
      mIsPseudo = false;
    }
    
    private String getLangScriptKey()
    {
      if (mLangScriptKey == null)
      {
        Object localObject = getParent(LocaleHelper.addLikelySubtags(new Locale.Builder().setLocale(mLocale).setExtension('u', "").build()));
        if (localObject == null) {
          localObject = mLocale.toLanguageTag();
        } else {
          localObject = ((Locale)localObject).toLanguageTag();
        }
        mLangScriptKey = ((String)localObject);
      }
      return mLangScriptKey;
    }
    
    private static Locale getParent(Locale paramLocale)
    {
      if (paramLocale.getCountry().isEmpty()) {
        return null;
      }
      return new Locale.Builder().setLocale(paramLocale).setRegion("").setExtension('u', "").build();
    }
    
    private boolean isSuggestionOfType(int paramInt)
    {
      boolean bool1 = mIsTranslated;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      if ((mSuggestionFlags & paramInt) == paramInt) {
        bool2 = true;
      }
      return bool2;
    }
    
    public boolean getChecked()
    {
      return mIsChecked;
    }
    
    String getContentDescription(boolean paramBoolean)
    {
      if (paramBoolean) {
        return getFullCountryNameInUiLanguage();
      }
      return getFullNameInUiLanguage();
    }
    
    String getFullCountryNameInUiLanguage()
    {
      return LocaleHelper.getDisplayCountry(mLocale);
    }
    
    String getFullCountryNameNative()
    {
      if (mFullCountryNameNative == null) {
        mFullCountryNameNative = LocaleHelper.getDisplayCountry(mLocale, mLocale);
      }
      return mFullCountryNameNative;
    }
    
    public String getFullNameInUiLanguage()
    {
      return LocaleHelper.getDisplayName(mLocale, true);
    }
    
    public String getFullNameNative()
    {
      if (mFullNameNative == null) {
        mFullNameNative = LocaleHelper.getDisplayName(mLocale, mLocale, true);
      }
      return mFullNameNative;
    }
    
    public String getId()
    {
      return mId;
    }
    
    String getLabel(boolean paramBoolean)
    {
      if (paramBoolean) {
        return getFullCountryNameNative();
      }
      return getFullNameNative();
    }
    
    public Locale getLocale()
    {
      return mLocale;
    }
    
    public Locale getParent()
    {
      return mParent;
    }
    
    boolean isSuggested()
    {
      boolean bool1 = mIsTranslated;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      if (mSuggestionFlags != 0) {
        bool2 = true;
      }
      return bool2;
    }
    
    public boolean isTranslated()
    {
      return mIsTranslated;
    }
    
    public void setChecked(boolean paramBoolean)
    {
      mIsChecked = paramBoolean;
    }
    
    public void setTranslated(boolean paramBoolean)
    {
      mIsTranslated = paramBoolean;
    }
    
    public String toString()
    {
      return mId;
    }
  }
}
