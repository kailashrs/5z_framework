package com.android.internal.inputmethod;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.LocaleList;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import android.util.Printer;
import android.util.Slog;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import android.view.textservice.SpellCheckerInfo;
import android.view.textservice.TextServicesManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

public class InputMethodUtils
{
  public static final boolean DEBUG = false;
  private static final Locale ENGLISH_LOCALE;
  private static final char INPUT_METHOD_SEPARATOR = ':';
  private static final char INPUT_METHOD_SUBTYPE_SEPARATOR = ';';
  private static final Locale LOCALE_EN_GB = new Locale("en", "GB");
  private static final Locale LOCALE_EN_US;
  public static final int NOT_A_SUBTYPE_ID = -1;
  private static final String NOT_A_SUBTYPE_ID_STR;
  private static final Locale[] SEARCH_ORDER_OF_FALLBACK_LOCALES;
  public static final String SUBTYPE_MODE_ANY = null;
  public static final String SUBTYPE_MODE_KEYBOARD = "keyboard";
  public static final String SUBTYPE_MODE_VOICE = "voice";
  private static final String TAG = "InputMethodUtils";
  private static final String TAG_ASCII_CAPABLE = "AsciiCapable";
  private static final String TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE = "EnabledWhenDefaultIsNotAsciiCapable";
  private static final Object sCacheLock;
  @GuardedBy("sCacheLock")
  private static InputMethodInfo sCachedInputMethodInfo;
  @GuardedBy("sCacheLock")
  private static ArrayList<InputMethodSubtype> sCachedResult;
  @GuardedBy("sCacheLock")
  private static LocaleList sCachedSystemLocales;
  private static final LocaleUtils.LocaleExtractor<InputMethodSubtype> sSubtypeToLocale;
  
  static
  {
    ENGLISH_LOCALE = new Locale("en");
    NOT_A_SUBTYPE_ID_STR = String.valueOf(-1);
    SEARCH_ORDER_OF_FALLBACK_LOCALES = new Locale[] { Locale.ENGLISH, Locale.US, Locale.UK };
    sCacheLock = new Object();
    sSubtypeToLocale = new LocaleUtils.LocaleExtractor()
    {
      public Locale get(InputMethodSubtype paramAnonymousInputMethodSubtype)
      {
        if (paramAnonymousInputMethodSubtype != null) {
          paramAnonymousInputMethodSubtype = paramAnonymousInputMethodSubtype.getLocaleObject();
        } else {
          paramAnonymousInputMethodSubtype = null;
        }
        return paramAnonymousInputMethodSubtype;
      }
    };
    LOCALE_EN_US = new Locale("en", "US");
  }
  
  private InputMethodUtils() {}
  
  public static String buildInputMethodsAndSubtypesString(ArrayMap<String, ArraySet<String>> paramArrayMap)
  {
    ArrayList localArrayList1 = new ArrayList(4);
    Iterator localIterator = paramArrayMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (Map.Entry)localIterator.next();
      paramArrayMap = (String)((Map.Entry)localObject).getKey();
      localObject = (ArraySet)((Map.Entry)localObject).getValue();
      ArrayList localArrayList2 = new ArrayList(2);
      if (localObject != null) {
        localArrayList2.addAll((Collection)localObject);
      }
      localArrayList1.add(new Pair(paramArrayMap, localArrayList2));
    }
    return InputMethodSettings.buildInputMethodsSettingString(localArrayList1);
  }
  
  public static boolean canAddToLastInputMethod(InputMethodSubtype paramInputMethodSubtype)
  {
    if (paramInputMethodSubtype == null) {
      return true;
    }
    return true ^ paramInputMethodSubtype.isAuxiliary();
  }
  
  public static boolean checkIfPackageBelongsToUid(AppOpsManager paramAppOpsManager, int paramInt, String paramString)
  {
    try
    {
      paramAppOpsManager.checkPackage(paramInt, paramString);
      return true;
    }
    catch (SecurityException paramAppOpsManager) {}
    return false;
  }
  
  public static Locale constructLocaleFromString(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    paramString = paramString.split("_", 3);
    if ((paramString.length >= 1) && ("tl".equals(paramString[0]))) {
      paramString[0] = "fil";
    }
    if (paramString.length == 1) {
      return new Locale(paramString[0]);
    }
    if (paramString.length == 2) {
      return new Locale(paramString[0], paramString[1]);
    }
    if (paramString.length == 3) {
      return new Locale(paramString[0], paramString[1], paramString[2]);
    }
    return null;
  }
  
  public static boolean containsSubtypeOf(InputMethodInfo paramInputMethodInfo, Locale paramLocale, boolean paramBoolean, String paramString)
  {
    if (paramLocale == null) {
      return false;
    }
    int i = paramInputMethodInfo.getSubtypeCount();
    int j = 0;
    while (j < i)
    {
      InputMethodSubtype localInputMethodSubtype = paramInputMethodInfo.getSubtypeAt(j);
      if (paramBoolean)
      {
        Locale localLocale = localInputMethodSubtype.getLocaleObject();
        if ((localLocale == null) || (!TextUtils.equals(localLocale.getLanguage(), paramLocale.getLanguage())) || (!TextUtils.equals(localLocale.getCountry(), paramLocale.getCountry()))) {
          break label142;
        }
      }
      if ((!TextUtils.equals(new Locale(getLanguageFromLocaleString(localInputMethodSubtype.getLocale())).getLanguage(), paramLocale.getLanguage())) || ((paramString != SUBTYPE_MODE_ANY) && (!TextUtils.isEmpty(paramString)) && (!paramString.equalsIgnoreCase(localInputMethodSubtype.getMode())))) {
        label142:
        j++;
      } else {
        return true;
      }
    }
    return false;
  }
  
  public static InputMethodSubtype findLastResortApplicableSubtypeLocked(Resources paramResources, List<InputMethodSubtype> paramList, String paramString1, String paramString2, boolean paramBoolean)
  {
    if ((paramList != null) && (paramList.size() != 0))
    {
      String str1 = paramString2;
      if (TextUtils.isEmpty(paramString2)) {
        str1 = getConfigurationlocale.toString();
      }
      String str2 = getLanguageFromLocaleString(str1);
      int i = 0;
      Object localObject1 = null;
      paramString2 = null;
      int j = paramList.size();
      int k = 0;
      Object localObject2;
      for (;;)
      {
        localObject2 = localObject1;
        paramResources = paramString2;
        if (k >= j) {
          break;
        }
        localObject2 = (InputMethodSubtype)paramList.get(k);
        String str3 = ((InputMethodSubtype)localObject2).getLocale();
        String str4 = getLanguageFromLocaleString(str3);
        int m;
        Object localObject3;
        Object localObject4;
        if (paramString1 != null)
        {
          m = i;
          localObject3 = localObject1;
          localObject4 = paramString2;
          if (!((InputMethodSubtype)paramList.get(k)).getMode().equalsIgnoreCase(paramString1)) {}
        }
        else
        {
          paramResources = paramString2;
          if (paramString2 == null) {
            paramResources = (Resources)localObject2;
          }
          if (str1.equals(str3)) {
            break;
          }
          m = i;
          localObject3 = localObject1;
          localObject4 = paramResources;
          if (i == 0)
          {
            m = i;
            localObject3 = localObject1;
            localObject4 = paramResources;
            if (str2.equals(str4))
            {
              m = 1;
              localObject4 = paramResources;
              localObject3 = localObject2;
            }
          }
        }
        k++;
        i = m;
        localObject1 = localObject3;
        paramString2 = (String)localObject4;
      }
      if ((localObject2 == null) && (paramBoolean)) {
        return paramResources;
      }
      return localObject2;
    }
    return null;
  }
  
  public static String getApiCallStack()
  {
    Object localObject = "";
    try
    {
      RuntimeException localRuntimeException1 = new java/lang/RuntimeException;
      localRuntimeException1.<init>();
      throw localRuntimeException1;
    }
    catch (RuntimeException localRuntimeException2)
    {
      StackTraceElement[] arrayOfStackTraceElement = localRuntimeException2.getStackTrace();
      for (int i = 1; i < arrayOfStackTraceElement.length; i++)
      {
        String str = arrayOfStackTraceElement[i].toString();
        if ((!TextUtils.isEmpty((CharSequence)localObject)) && (str.indexOf("Transact(") >= 0)) {
          break;
        }
        localObject = str;
      }
    }
    return localObject;
  }
  
  public static ArrayList<InputMethodInfo> getDefaultEnabledImes(Context paramContext, ArrayList<InputMethodInfo> paramArrayList)
  {
    return getDefaultEnabledImes(paramContext, paramArrayList, false);
  }
  
  public static ArrayList<InputMethodInfo> getDefaultEnabledImes(Context paramContext, ArrayList<InputMethodInfo> paramArrayList, boolean paramBoolean)
  {
    Object localObject = getFallbackLocaleForDefaultIme(paramArrayList, paramContext);
    Locale localLocale = getSystemLocaleFromContext(paramContext);
    localObject = getMinimumKeyboardSetWithSystemLocale(paramArrayList, paramContext, localLocale, (Locale)localObject);
    if (!paramBoolean) {
      ((InputMethodListBuilder)localObject).fillImes(paramArrayList, paramContext, true, localLocale, true, SUBTYPE_MODE_ANY).fillAuxiliaryImes(paramArrayList, paramContext);
    }
    return ((InputMethodListBuilder)localObject).build();
  }
  
  public static Locale getFallbackLocaleForDefaultIme(ArrayList<InputMethodInfo> paramArrayList, Context paramContext)
  {
    Object localObject;
    int k;
    for (localObject : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
      for (k = 0; k < paramArrayList.size(); k++) {
        if (isSystemImeThatHasSubtypeOf((InputMethodInfo)paramArrayList.get(k), paramContext, true, (Locale)localObject, true, "keyboard")) {
          return localObject;
        }
      }
    }
    for (??? : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
      for (k = 0; k < paramArrayList.size(); k++) {
        if (isSystemImeThatHasSubtypeOf((InputMethodInfo)paramArrayList.get(k), paramContext, false, ???, true, "keyboard")) {
          return ???;
        }
      }
    }
    paramContext = new StringBuilder();
    paramContext.append("Found no fallback locale. imis=");
    paramContext.append(Arrays.toString(paramArrayList.toArray()));
    Slog.w("InputMethodUtils", paramContext.toString());
    return null;
  }
  
  public static CharSequence getImeAndSubtypeDisplayName(Context paramContext, InputMethodInfo paramInputMethodInfo, InputMethodSubtype paramInputMethodSubtype)
  {
    CharSequence localCharSequence = paramInputMethodInfo.loadLabel(paramContext.getPackageManager());
    if (paramInputMethodSubtype != null)
    {
      paramInputMethodInfo = paramInputMethodSubtype.getDisplayName(paramContext, paramInputMethodInfo.getPackageName(), getServiceInfoapplicationInfo);
      if (TextUtils.isEmpty(localCharSequence))
      {
        paramContext = "";
      }
      else
      {
        paramContext = new StringBuilder();
        paramContext.append(" - ");
        paramContext.append(localCharSequence);
        paramContext = paramContext.toString();
      }
      paramContext = TextUtils.concat(new CharSequence[] { paramInputMethodInfo, paramContext });
    }
    else
    {
      paramContext = localCharSequence;
    }
    return paramContext;
  }
  
  @VisibleForTesting
  public static ArrayList<InputMethodSubtype> getImplicitlyApplicableSubtypesLocked(Resources arg0, InputMethodInfo paramInputMethodInfo)
  {
    LocaleList localLocaleList = ???.getConfiguration().getLocales();
    synchronized (sCacheLock)
    {
      if ((localLocaleList.equals(sCachedSystemLocales)) && (sCachedInputMethodInfo == paramInputMethodInfo))
      {
        ??? = new java/util/ArrayList;
        ???.<init>(sCachedResult);
        return ???;
      }
      ??? = getImplicitlyApplicableSubtypesLockedImpl(???, paramInputMethodInfo);
      synchronized (sCacheLock)
      {
        sCachedSystemLocales = localLocaleList;
        sCachedInputMethodInfo = paramInputMethodInfo;
        paramInputMethodInfo = new java/util/ArrayList;
        paramInputMethodInfo.<init>((Collection)???);
        sCachedResult = paramInputMethodInfo;
        return ???;
      }
    }
  }
  
  private static ArrayList<InputMethodSubtype> getImplicitlyApplicableSubtypesLockedImpl(Resources paramResources, InputMethodInfo paramInputMethodInfo)
  {
    ArrayList localArrayList = getSubtypes(paramInputMethodInfo);
    paramInputMethodInfo = paramResources.getConfiguration().getLocales();
    int i = 0;
    String str = paramInputMethodInfo.get(0).toString();
    if (TextUtils.isEmpty(str)) {
      return new ArrayList();
    }
    int j = localArrayList.size();
    HashMap localHashMap = new HashMap();
    for (int k = 0; k < j; k++)
    {
      localObject1 = (InputMethodSubtype)localArrayList.get(k);
      if (((InputMethodSubtype)localObject1).overridesImplicitlyEnabledSubtype())
      {
        localObject2 = ((InputMethodSubtype)localObject1).getMode();
        if (!localHashMap.containsKey(localObject2)) {
          localHashMap.put(localObject2, localObject1);
        }
      }
    }
    if (localHashMap.size() > 0) {
      return new ArrayList(localHashMap.values());
    }
    localHashMap = new HashMap();
    Object localObject2 = new ArrayList();
    Object localObject3;
    for (k = 0; k < j; k++)
    {
      localObject1 = (InputMethodSubtype)localArrayList.get(k);
      localObject3 = ((InputMethodSubtype)localObject1).getMode();
      if ("keyboard".equals(localObject3))
      {
        ((ArrayList)localObject2).add(localObject1);
      }
      else
      {
        if (!localHashMap.containsKey(localObject3)) {
          localHashMap.put(localObject3, new ArrayList());
        }
        ((ArrayList)localHashMap.get(localObject3)).add(localObject1);
      }
    }
    Object localObject1 = new ArrayList();
    LocaleUtils.filterByLanguage((List)localObject2, sSubtypeToLocale, paramInputMethodInfo, (ArrayList)localObject1);
    if (!((ArrayList)localObject1).isEmpty())
    {
      int m = 0;
      int n = ((ArrayList)localObject1).size();
      for (j = 0;; j++)
      {
        k = m;
        if (j >= n) {
          break;
        }
        if (((InputMethodSubtype)((ArrayList)localObject1).get(j)).containsExtraValueKey("AsciiCapable"))
        {
          k = 1;
          break;
        }
      }
      if (k == 0)
      {
        j = ((ArrayList)localObject2).size();
        for (k = i; k < j; k++)
        {
          localObject3 = (InputMethodSubtype)((ArrayList)localObject2).get(k);
          if (("keyboard".equals(((InputMethodSubtype)localObject3).getMode())) && (((InputMethodSubtype)localObject3).containsExtraValueKey("EnabledWhenDefaultIsNotAsciiCapable"))) {
            ((ArrayList)localObject1).add(localObject3);
          }
        }
      }
    }
    if (((ArrayList)localObject1).isEmpty())
    {
      paramResources = findLastResortApplicableSubtypeLocked(paramResources, localArrayList, "keyboard", str, true);
      if (paramResources != null) {
        ((ArrayList)localObject1).add(paramResources);
      }
    }
    paramResources = localHashMap.values().iterator();
    while (paramResources.hasNext()) {
      LocaleUtils.filterByLanguage((ArrayList)paramResources.next(), sSubtypeToLocale, paramInputMethodInfo, (ArrayList)localObject1);
    }
    return localObject1;
  }
  
  public static String getLanguageFromLocaleString(String paramString)
  {
    int i = paramString.indexOf('_');
    if (i < 0) {
      return paramString;
    }
    return paramString.substring(0, i);
  }
  
  private static InputMethodListBuilder getMinimumKeyboardSetWithSystemLocale(ArrayList<InputMethodInfo> paramArrayList, Context paramContext, Locale paramLocale1, Locale paramLocale2)
  {
    InputMethodListBuilder localInputMethodListBuilder = new InputMethodListBuilder(null);
    localInputMethodListBuilder.fillImes(paramArrayList, paramContext, true, paramLocale1, true, "keyboard");
    if (!localInputMethodListBuilder.isEmpty()) {
      return localInputMethodListBuilder;
    }
    localInputMethodListBuilder.fillImes(paramArrayList, paramContext, true, paramLocale1, false, "keyboard");
    if (!localInputMethodListBuilder.isEmpty()) {
      return localInputMethodListBuilder;
    }
    localInputMethodListBuilder.fillImes(paramArrayList, paramContext, true, paramLocale2, true, "keyboard");
    if (!localInputMethodListBuilder.isEmpty()) {
      return localInputMethodListBuilder;
    }
    localInputMethodListBuilder.fillImes(paramArrayList, paramContext, true, paramLocale2, false, "keyboard");
    if (!localInputMethodListBuilder.isEmpty()) {
      return localInputMethodListBuilder;
    }
    localInputMethodListBuilder.fillImes(paramArrayList, paramContext, false, paramLocale2, true, "keyboard");
    if (!localInputMethodListBuilder.isEmpty()) {
      return localInputMethodListBuilder;
    }
    localInputMethodListBuilder.fillImes(paramArrayList, paramContext, false, paramLocale2, false, "keyboard");
    if (!localInputMethodListBuilder.isEmpty()) {
      return localInputMethodListBuilder;
    }
    paramContext = new StringBuilder();
    paramContext.append("No software keyboard is found. imis=");
    paramContext.append(Arrays.toString(paramArrayList.toArray()));
    paramContext.append(" systemLocale=");
    paramContext.append(paramLocale1);
    paramContext.append(" fallbackLocale=");
    paramContext.append(paramLocale2);
    Slog.w("InputMethodUtils", paramContext.toString());
    return localInputMethodListBuilder;
  }
  
  public static InputMethodInfo getMostApplicableDefaultIME(List<InputMethodInfo> paramList)
  {
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      int i = paramList.size();
      int j = -1;
      while (i > 0)
      {
        i--;
        InputMethodInfo localInputMethodInfo = (InputMethodInfo)paramList.get(i);
        if (!localInputMethodInfo.isAuxiliaryIme())
        {
          if ((isSystemIme(localInputMethodInfo)) && (containsSubtypeOf(localInputMethodInfo, ENGLISH_LOCALE, false, "keyboard"))) {
            return localInputMethodInfo;
          }
          int k = j;
          if (j < 0)
          {
            k = j;
            if (isSystemIme(localInputMethodInfo)) {
              k = i;
            }
          }
          j = k;
        }
      }
      return (InputMethodInfo)paramList.get(Math.max(j, 0));
    }
    return null;
  }
  
  public static ArrayList<InputMethodSubtype> getOverridingImplicitlyEnabledSubtypes(InputMethodInfo paramInputMethodInfo, String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramInputMethodInfo.getSubtypeCount();
    for (int j = 0; j < i; j++)
    {
      InputMethodSubtype localInputMethodSubtype = paramInputMethodInfo.getSubtypeAt(j);
      if ((localInputMethodSubtype.overridesImplicitlyEnabledSubtype()) && (localInputMethodSubtype.getMode().equals(paramString))) {
        localArrayList.add(localInputMethodSubtype);
      }
    }
    return localArrayList;
  }
  
  public static int getSubtypeIdFromHashCode(InputMethodInfo paramInputMethodInfo, int paramInt)
  {
    if (paramInputMethodInfo != null)
    {
      int i = paramInputMethodInfo.getSubtypeCount();
      for (int j = 0; j < i; j++) {
        if (paramInt == paramInputMethodInfo.getSubtypeAt(j).hashCode()) {
          return j;
        }
      }
    }
    return -1;
  }
  
  public static ArrayList<InputMethodSubtype> getSubtypes(InputMethodInfo paramInputMethodInfo)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramInputMethodInfo.getSubtypeCount();
    for (int j = 0; j < i; j++) {
      localArrayList.add(paramInputMethodInfo.getSubtypeAt(j));
    }
    return localArrayList;
  }
  
  @VisibleForTesting
  public static ArrayList<Locale> getSuitableLocalesForSpellChecker(Locale paramLocale)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3;
    if (paramLocale != null)
    {
      localObject1 = paramLocale.getLanguage();
      boolean bool1 = TextUtils.isEmpty((CharSequence)localObject1) ^ true;
      localObject3 = paramLocale.getCountry();
      boolean bool2 = TextUtils.isEmpty((CharSequence)localObject3) ^ true;
      paramLocale = paramLocale.getVariant();
      boolean bool3 = TextUtils.isEmpty(paramLocale);
      if ((bool1) && (bool2) && ((bool3 ^ true))) {
        paramLocale = new Locale((String)localObject1, (String)localObject3, paramLocale);
      } else {
        paramLocale = null;
      }
      if ((bool1) && (bool2)) {
        localObject3 = new Locale((String)localObject1, (String)localObject3);
      } else {
        localObject3 = null;
      }
      if (bool1) {
        localObject2 = new Locale((String)localObject1);
      }
    }
    else
    {
      paramLocale = null;
      localObject3 = null;
      localObject2 = localObject1;
    }
    localObject1 = new ArrayList();
    if (paramLocale != null) {
      ((ArrayList)localObject1).add(paramLocale);
    }
    if (Locale.ENGLISH.equals(localObject2))
    {
      if (localObject3 != null)
      {
        if (localObject3 != null) {
          ((ArrayList)localObject1).add(localObject3);
        }
        if (!LOCALE_EN_US.equals(localObject3)) {
          ((ArrayList)localObject1).add(LOCALE_EN_US);
        }
        if (!LOCALE_EN_GB.equals(localObject3)) {
          ((ArrayList)localObject1).add(LOCALE_EN_GB);
        }
        ((ArrayList)localObject1).add(Locale.ENGLISH);
      }
      else
      {
        ((ArrayList)localObject1).add(Locale.ENGLISH);
        ((ArrayList)localObject1).add(LOCALE_EN_US);
        ((ArrayList)localObject1).add(LOCALE_EN_GB);
      }
    }
    else
    {
      if (localObject3 != null) {
        ((ArrayList)localObject1).add(localObject3);
      }
      if (localObject2 != null) {
        ((ArrayList)localObject1).add(localObject2);
      }
      ((ArrayList)localObject1).add(LOCALE_EN_US);
      ((ArrayList)localObject1).add(LOCALE_EN_GB);
      ((ArrayList)localObject1).add(Locale.ENGLISH);
    }
    return localObject1;
  }
  
  public static Locale getSystemLocaleFromContext(Context paramContext)
  {
    try
    {
      paramContext = getResourcesgetConfigurationlocale;
      return paramContext;
    }
    catch (Resources.NotFoundException paramContext) {}
    return null;
  }
  
  public static boolean isSoftInputModeStateVisibleAllowed(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 28) {
      return true;
    }
    if ((paramInt2 & 0x1) == 0) {
      return false;
    }
    return (paramInt2 & 0x2) != 0;
  }
  
  private static boolean isSystemAuxilialyImeThatHasAutomaticSubtype(InputMethodInfo paramInputMethodInfo, Context paramContext, boolean paramBoolean)
  {
    if (!isSystemIme(paramInputMethodInfo)) {
      return false;
    }
    if ((paramBoolean) && (!paramInputMethodInfo.isDefault(paramContext))) {
      return false;
    }
    if (!paramInputMethodInfo.isAuxiliaryIme()) {
      return false;
    }
    int i = paramInputMethodInfo.getSubtypeCount();
    for (int j = 0; j < i; j++) {
      if (paramInputMethodInfo.getSubtypeAt(j).overridesImplicitlyEnabledSubtype()) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isSystemIme(InputMethodInfo paramInputMethodInfo)
  {
    int i = getServiceInfoapplicationInfo.flags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isSystemImeThatHasSubtypeOf(InputMethodInfo paramInputMethodInfo, Context paramContext, boolean paramBoolean1, Locale paramLocale, boolean paramBoolean2, String paramString)
  {
    if (!isSystemIme(paramInputMethodInfo)) {
      return false;
    }
    if ((paramBoolean1) && (!paramInputMethodInfo.isDefault(paramContext))) {
      return false;
    }
    return containsSubtypeOf(paramInputMethodInfo, paramLocale, paramBoolean2, paramString);
  }
  
  public static boolean isValidSubtypeId(InputMethodInfo paramInputMethodInfo, int paramInt)
  {
    boolean bool;
    if (getSubtypeIdFromHashCode(paramInputMethodInfo, paramInt) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @VisibleForTesting
  public static ArrayMap<String, ArraySet<String>> parseInputMethodsAndSubtypesString(String paramString)
  {
    ArrayMap localArrayMap = new ArrayMap();
    if (TextUtils.isEmpty(paramString)) {
      return localArrayMap;
    }
    Object localObject1 = new TextUtils.SimpleStringSplitter(':');
    Object localObject2 = new TextUtils.SimpleStringSplitter(';');
    localObject1 = InputMethodSettings.buildInputMethodsAndSubtypeList(paramString, (TextUtils.SimpleStringSplitter)localObject1, (TextUtils.SimpleStringSplitter)localObject2).iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Pair)((Iterator)localObject1).next();
      paramString = new ArraySet();
      if (second != null) {
        paramString.addAll((Collection)second);
      }
      localArrayMap.put((String)first, paramString);
    }
    return localArrayMap;
  }
  
  private static void setDisabledUntilUsed(IPackageManager paramIPackageManager, String paramString1, int paramInt, String paramString2)
  {
    try
    {
      int i = paramIPackageManager.getApplicationEnabledSetting(paramString1, paramInt);
      if ((i == 0) || (i == 1)) {}
      try
      {
        paramIPackageManager.setApplicationEnabledSetting(paramString1, 4, 0, paramInt, paramString2);
        return;
      }
      catch (RemoteException paramIPackageManager)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("setApplicationEnabledSetting failed. packageName=");
        localStringBuilder.append(paramString1);
        localStringBuilder.append(" userId=");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" callingPackage=");
        localStringBuilder.append(paramString2);
        Slog.w("InputMethodUtils", localStringBuilder.toString(), paramIPackageManager);
        return;
      }
      return;
    }
    catch (RemoteException paramString2)
    {
      paramIPackageManager = new StringBuilder();
      paramIPackageManager.append("getApplicationEnabledSetting failed. packageName=");
      paramIPackageManager.append(paramString1);
      paramIPackageManager.append(" userId=");
      paramIPackageManager.append(paramInt);
      Slog.w("InputMethodUtils", paramIPackageManager.toString(), paramString2);
    }
  }
  
  public static void setNonSelectedSystemImesDisabledUntilUsed(IPackageManager paramIPackageManager, List<InputMethodInfo> paramList, int paramInt, String paramString)
  {
    String[] arrayOfString = Resources.getSystem().getStringArray(17236006);
    if ((arrayOfString != null) && (arrayOfString.length != 0))
    {
      SpellCheckerInfo localSpellCheckerInfo = TextServicesManager.getInstance().getCurrentSpellChecker();
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        String str = arrayOfString[j];
        int k = 0;
        int n;
        for (int m = 0;; m++)
        {
          n = k;
          if (m >= paramList.size()) {
            break;
          }
          if (str.equals(((InputMethodInfo)paramList.get(m)).getPackageName()))
          {
            n = 1;
            break;
          }
        }
        if ((n == 0) && ((localSpellCheckerInfo == null) || (!str.equals(localSpellCheckerInfo.getPackageName())))) {
          try
          {
            ApplicationInfo localApplicationInfo = paramIPackageManager.getApplicationInfo(str, 32768, paramInt);
            if (localApplicationInfo != null)
            {
              m = flags;
              n = 1;
              if ((m & 0x1) == 0) {
                n = 0;
              }
              if (n != 0) {
                setDisabledUntilUsed(paramIPackageManager, str, paramInt, paramString);
              }
            }
          }
          catch (RemoteException localRemoteException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("getApplicationInfo failed. packageName=");
            localStringBuilder.append(str);
            localStringBuilder.append(" userId=");
            localStringBuilder.append(paramInt);
            Slog.w("InputMethodUtils", localStringBuilder.toString(), localRemoteException);
          }
        }
      }
      return;
    }
  }
  
  private static final class InputMethodListBuilder
  {
    private final LinkedHashSet<InputMethodInfo> mInputMethodSet = new LinkedHashSet();
    
    private InputMethodListBuilder() {}
    
    public ArrayList<InputMethodInfo> build()
    {
      return new ArrayList(mInputMethodSet);
    }
    
    public InputMethodListBuilder fillAuxiliaryImes(ArrayList<InputMethodInfo> paramArrayList, Context paramContext)
    {
      Object localObject = mInputMethodSet.iterator();
      while (((Iterator)localObject).hasNext()) {
        if (((InputMethodInfo)((Iterator)localObject).next()).isAuxiliaryIme()) {
          return this;
        }
      }
      int i = 0;
      for (int j = 0; j < paramArrayList.size(); j++)
      {
        localObject = (InputMethodInfo)paramArrayList.get(j);
        if (InputMethodUtils.isSystemAuxilialyImeThatHasAutomaticSubtype((InputMethodInfo)localObject, paramContext, true))
        {
          mInputMethodSet.add(localObject);
          i = 1;
        }
      }
      if (i != 0) {
        return this;
      }
      for (j = 0; j < paramArrayList.size(); j++)
      {
        localObject = (InputMethodInfo)paramArrayList.get(j);
        if (InputMethodUtils.isSystemAuxilialyImeThatHasAutomaticSubtype((InputMethodInfo)localObject, paramContext, false)) {
          mInputMethodSet.add(localObject);
        }
      }
      return this;
    }
    
    public InputMethodListBuilder fillImes(ArrayList<InputMethodInfo> paramArrayList, Context paramContext, boolean paramBoolean1, Locale paramLocale, boolean paramBoolean2, String paramString)
    {
      for (int i = 0; i < paramArrayList.size(); i++)
      {
        InputMethodInfo localInputMethodInfo = (InputMethodInfo)paramArrayList.get(i);
        if (InputMethodUtils.isSystemImeThatHasSubtypeOf(localInputMethodInfo, paramContext, paramBoolean1, paramLocale, paramBoolean2, paramString)) {
          mInputMethodSet.add(localInputMethodInfo);
        }
      }
      return this;
    }
    
    public boolean isEmpty()
    {
      return mInputMethodSet.isEmpty();
    }
  }
  
  public static class InputMethodSettings
  {
    private boolean mCopyOnWrite = false;
    private final HashMap<String, String> mCopyOnWriteDataStore = new HashMap();
    private int[] mCurrentProfileIds = new int[0];
    private int mCurrentUserId;
    private String mEnabledInputMethodsStrCache = "";
    private final TextUtils.SimpleStringSplitter mInputMethodSplitter = new TextUtils.SimpleStringSplitter(':');
    private final HashMap<String, InputMethodInfo> mMethodMap;
    private final Resources mRes;
    private final ContentResolver mResolver;
    private final TextUtils.SimpleStringSplitter mSubtypeSplitter = new TextUtils.SimpleStringSplitter(';');
    
    public InputMethodSettings(Resources paramResources, ContentResolver paramContentResolver, HashMap<String, InputMethodInfo> paramHashMap, ArrayList<InputMethodInfo> paramArrayList, int paramInt, boolean paramBoolean)
    {
      mRes = paramResources;
      mResolver = paramContentResolver;
      mMethodMap = paramHashMap;
      switchCurrentUser(paramInt, paramBoolean);
    }
    
    private void addSubtypeToHistory(String paramString1, String paramString2)
    {
      List localList = loadInputMethodAndSubtypeHistoryLocked();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if (((String)first).equals(paramString1))
        {
          localList.remove(localPair);
          break;
        }
      }
      saveSubtypeHistory(localList, paramString1, paramString2);
    }
    
    private static void buildEnabledInputMethodsSettingString(StringBuilder paramStringBuilder, Pair<String, ArrayList<String>> paramPair)
    {
      paramStringBuilder.append((String)first);
      paramPair = ((ArrayList)second).iterator();
      while (paramPair.hasNext())
      {
        String str = (String)paramPair.next();
        paramStringBuilder.append(';');
        paramStringBuilder.append(str);
      }
    }
    
    public static List<Pair<String, ArrayList<String>>> buildInputMethodsAndSubtypeList(String paramString, TextUtils.SimpleStringSplitter paramSimpleStringSplitter1, TextUtils.SimpleStringSplitter paramSimpleStringSplitter2)
    {
      ArrayList localArrayList1 = new ArrayList();
      if (TextUtils.isEmpty(paramString)) {
        return localArrayList1;
      }
      paramSimpleStringSplitter1.setString(paramString);
      while (paramSimpleStringSplitter1.hasNext())
      {
        paramSimpleStringSplitter2.setString(paramSimpleStringSplitter1.next());
        if (paramSimpleStringSplitter2.hasNext())
        {
          ArrayList localArrayList2 = new ArrayList();
          paramString = paramSimpleStringSplitter2.next();
          while (paramSimpleStringSplitter2.hasNext()) {
            localArrayList2.add(paramSimpleStringSplitter2.next());
          }
          localArrayList1.add(new Pair(paramString, localArrayList2));
        }
      }
      return localArrayList1;
    }
    
    public static String buildInputMethodsSettingString(List<Pair<String, ArrayList<String>>> paramList)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 0;
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        Pair localPair = (Pair)paramList.next();
        if (i != 0) {
          localStringBuilder.append(':');
        }
        buildEnabledInputMethodsSettingString(localStringBuilder, localPair);
        i = 1;
      }
      return localStringBuilder.toString();
    }
    
    private ArrayList<InputMethodInfo> createEnabledInputMethodListLocked(List<Pair<String, ArrayList<String>>> paramList)
    {
      ArrayList localArrayList = new ArrayList();
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        Object localObject = (Pair)paramList.next();
        localObject = (InputMethodInfo)mMethodMap.get(first);
        if ((localObject != null) && (!((InputMethodInfo)localObject).isVrOnly())) {
          localArrayList.add(localObject);
        }
      }
      return localArrayList;
    }
    
    private boolean getBoolean(String paramString, boolean paramBoolean)
    {
      paramBoolean = getInt(paramString, paramBoolean);
      boolean bool = true;
      if (paramBoolean != true) {
        bool = false;
      }
      return bool;
    }
    
    private String getEnabledSubtypeHashCodeForInputMethodAndSubtypeLocked(List<Pair<String, ArrayList<String>>> paramList, String paramString1, String paramString2)
    {
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        Object localObject = (Pair)paramList.next();
        if (((String)first).equals(paramString1))
        {
          localObject = (ArrayList)second;
          paramList = (InputMethodInfo)mMethodMap.get(paramString1);
          if (((ArrayList)localObject).size() == 0)
          {
            if ((paramList != null) && (paramList.getSubtypeCount() > 0))
            {
              paramList = InputMethodUtils.getImplicitlyApplicableSubtypesLocked(mRes, paramList);
              if (paramList != null)
              {
                int i = paramList.size();
                for (int j = 0; j < i; j++) {
                  if (String.valueOf(((InputMethodSubtype)paramList.get(j)).hashCode()).equals(paramString2)) {
                    return paramString2;
                  }
                }
              }
            }
          }
          else
          {
            localObject = ((ArrayList)localObject).iterator();
            while (((Iterator)localObject).hasNext())
            {
              paramString1 = (String)((Iterator)localObject).next();
              if (paramString1.equals(paramString2)) {
                try
                {
                  if (InputMethodUtils.isValidSubtypeId(paramList, Integer.parseInt(paramString2))) {
                    return paramString1;
                  }
                  paramList = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                  return paramList;
                }
                catch (NumberFormatException paramList)
                {
                  return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                }
              }
            }
          }
          return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
        }
      }
      return null;
    }
    
    private int getInt(String paramString, int paramInt)
    {
      if ((mCopyOnWrite) && (mCopyOnWriteDataStore.containsKey(paramString)))
      {
        paramString = (String)mCopyOnWriteDataStore.get(paramString);
        if (paramString != null) {
          paramInt = Integer.parseInt(paramString);
        } else {
          paramInt = 0;
        }
        return paramInt;
      }
      return Settings.Secure.getIntForUser(mResolver, paramString, paramInt, mCurrentUserId);
    }
    
    private Pair<String, String> getLastSubtypeForInputMethodLockedInternal(String paramString)
    {
      List localList = getEnabledInputMethodsAndSubtypeListLocked();
      Iterator localIterator = loadInputMethodAndSubtypeHistoryLocked().iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (Pair)localIterator.next();
        String str = (String)first;
        if ((TextUtils.isEmpty(paramString)) || (str.equals(paramString)))
        {
          localObject = (String)second;
          localObject = getEnabledSubtypeHashCodeForInputMethodAndSubtypeLocked(localList, str, (String)localObject);
          if (!TextUtils.isEmpty((CharSequence)localObject)) {
            return new Pair(str, localObject);
          }
        }
      }
      return null;
    }
    
    private int getSelectedInputMethodSubtypeHashCode()
    {
      return getInt("selected_input_method_subtype", -1);
    }
    
    private String getString(String paramString1, String paramString2)
    {
      if ((mCopyOnWrite) && (mCopyOnWriteDataStore.containsKey(paramString1))) {
        paramString1 = (String)mCopyOnWriteDataStore.get(paramString1);
      } else {
        paramString1 = Settings.Secure.getStringForUser(mResolver, paramString1, mCurrentUserId);
      }
      if (paramString1 == null) {
        paramString1 = paramString2;
      }
      return paramString1;
    }
    
    private String getSubtypeHistoryStr()
    {
      return getString("input_methods_subtype_history", "");
    }
    
    private List<Pair<String, String>> loadInputMethodAndSubtypeHistoryLocked()
    {
      ArrayList localArrayList = new ArrayList();
      String str1 = getSubtypeHistoryStr();
      if (TextUtils.isEmpty(str1)) {
        return localArrayList;
      }
      mInputMethodSplitter.setString(str1);
      while (mInputMethodSplitter.hasNext())
      {
        str1 = mInputMethodSplitter.next();
        mSubtypeSplitter.setString(str1);
        if (mSubtypeSplitter.hasNext())
        {
          str1 = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
          String str2 = mSubtypeSplitter.next();
          if (mSubtypeSplitter.hasNext()) {
            str1 = mSubtypeSplitter.next();
          }
          localArrayList.add(new Pair(str2, str1));
        }
      }
      return localArrayList;
    }
    
    private void putBoolean(String paramString, boolean paramBoolean)
    {
      putInt(paramString, paramBoolean);
    }
    
    private void putEnabledInputMethodsStr(String paramString)
    {
      if (TextUtils.isEmpty(paramString)) {
        putString("enabled_input_methods", null);
      } else {
        putString("enabled_input_methods", paramString);
      }
      if (paramString == null) {
        paramString = "";
      }
      mEnabledInputMethodsStrCache = paramString;
    }
    
    private void putInt(String paramString, int paramInt)
    {
      if (mCopyOnWrite) {
        mCopyOnWriteDataStore.put(paramString, String.valueOf(paramInt));
      } else {
        Settings.Secure.putIntForUser(mResolver, paramString, paramInt, mCurrentUserId);
      }
    }
    
    private void putString(String paramString1, String paramString2)
    {
      if (mCopyOnWrite) {
        mCopyOnWriteDataStore.put(paramString1, paramString2);
      } else {
        Settings.Secure.putStringForUser(mResolver, paramString1, paramString2, mCurrentUserId);
      }
    }
    
    private void putSubtypeHistoryStr(String paramString)
    {
      if (TextUtils.isEmpty(paramString)) {
        putString("input_methods_subtype_history", null);
      } else {
        putString("input_methods_subtype_history", paramString);
      }
    }
    
    private void saveSubtypeHistory(List<Pair<String, String>> paramList, String paramString1, String paramString2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 0;
      int j = i;
      if (!TextUtils.isEmpty(paramString1))
      {
        j = i;
        if (!TextUtils.isEmpty(paramString2))
        {
          localStringBuilder.append(paramString1);
          localStringBuilder.append(';');
          localStringBuilder.append(paramString2);
          j = 1;
        }
      }
      paramString2 = paramList.iterator();
      while (paramString2.hasNext())
      {
        paramList = (Pair)paramString2.next();
        String str = (String)first;
        paramString1 = (String)second;
        paramList = paramString1;
        if (TextUtils.isEmpty(paramString1)) {
          paramList = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
        }
        if (j != 0) {
          localStringBuilder.append(':');
        } else {
          j = 1;
        }
        localStringBuilder.append(str);
        localStringBuilder.append(';');
        localStringBuilder.append(paramList);
      }
      putSubtypeHistoryStr(localStringBuilder.toString());
    }
    
    public void appendAndPutEnabledInputMethodLocked(String paramString, boolean paramBoolean)
    {
      if (paramBoolean) {
        getEnabledInputMethodsStr();
      }
      if (TextUtils.isEmpty(mEnabledInputMethodsStrCache))
      {
        putEnabledInputMethodsStr(paramString);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(mEnabledInputMethodsStrCache);
        localStringBuilder.append(':');
        localStringBuilder.append(paramString);
        putEnabledInputMethodsStr(localStringBuilder.toString());
      }
    }
    
    public boolean buildAndPutEnabledInputMethodsStrRemovingIdLocked(StringBuilder paramStringBuilder, List<Pair<String, ArrayList<String>>> paramList, String paramString)
    {
      boolean bool = false;
      int i = 0;
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        Pair localPair = (Pair)paramList.next();
        if (((String)first).equals(paramString))
        {
          bool = true;
        }
        else
        {
          if (i != 0) {
            paramStringBuilder.append(':');
          } else {
            i = 1;
          }
          buildEnabledInputMethodsSettingString(paramStringBuilder, localPair);
        }
      }
      if (bool) {
        putEnabledInputMethodsStr(paramStringBuilder.toString());
      }
      return bool;
    }
    
    public void dumpLocked(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCurrentUserId=");
      localStringBuilder.append(mCurrentUserId);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCurrentProfileIds=");
      localStringBuilder.append(Arrays.toString(mCurrentProfileIds));
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCopyOnWrite=");
      localStringBuilder.append(mCopyOnWrite);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mEnabledInputMethodsStrCache=");
      localStringBuilder.append(mEnabledInputMethodsStrCache);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public int getCurrentUserId()
    {
      return mCurrentUserId;
    }
    
    public ArrayList<InputMethodInfo> getEnabledInputMethodListLocked()
    {
      return createEnabledInputMethodListLocked(getEnabledInputMethodsAndSubtypeListLocked());
    }
    
    public List<InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(Context paramContext, InputMethodInfo paramInputMethodInfo, boolean paramBoolean)
    {
      List localList = getEnabledInputMethodSubtypeListLocked(paramInputMethodInfo);
      Object localObject = localList;
      if (paramBoolean)
      {
        localObject = localList;
        if (localList.isEmpty()) {
          localObject = InputMethodUtils.getImplicitlyApplicableSubtypesLocked(paramContext.getResources(), paramInputMethodInfo);
        }
      }
      return InputMethodSubtype.sort(paramContext, 0, paramInputMethodInfo, (List)localObject);
    }
    
    public List<InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(InputMethodInfo paramInputMethodInfo)
    {
      Object localObject = getEnabledInputMethodsAndSubtypeListLocked();
      ArrayList localArrayList = new ArrayList();
      if (paramInputMethodInfo != null)
      {
        Iterator localIterator = ((List)localObject).iterator();
        while (localIterator.hasNext())
        {
          Pair localPair = (Pair)localIterator.next();
          localObject = (InputMethodInfo)mMethodMap.get(first);
          if ((localObject != null) && (((InputMethodInfo)localObject).getId().equals(paramInputMethodInfo.getId())))
          {
            int i = ((InputMethodInfo)localObject).getSubtypeCount();
            for (int j = 0; j < i; j++)
            {
              InputMethodSubtype localInputMethodSubtype = ((InputMethodInfo)localObject).getSubtypeAt(j);
              localIterator = ((ArrayList)second).iterator();
              while (localIterator.hasNext())
              {
                paramInputMethodInfo = (String)localIterator.next();
                if (String.valueOf(localInputMethodSubtype.hashCode()).equals(paramInputMethodInfo)) {
                  localArrayList.add(localInputMethodSubtype);
                }
              }
            }
            break;
          }
        }
      }
      return localArrayList;
    }
    
    public List<Pair<String, ArrayList<String>>> getEnabledInputMethodsAndSubtypeListLocked()
    {
      return buildInputMethodsAndSubtypeList(getEnabledInputMethodsStr(), mInputMethodSplitter, mSubtypeSplitter);
    }
    
    public String getEnabledInputMethodsStr()
    {
      mEnabledInputMethodsStrCache = getString("enabled_input_methods", "");
      return mEnabledInputMethodsStrCache;
    }
    
    public HashMap<InputMethodInfo, List<InputMethodSubtype>> getExplicitlyOrImplicitlyEnabledInputMethodsAndSubtypeListLocked(Context paramContext)
    {
      HashMap localHashMap = new HashMap();
      Iterator localIterator = getEnabledInputMethodListLocked().iterator();
      while (localIterator.hasNext())
      {
        InputMethodInfo localInputMethodInfo = (InputMethodInfo)localIterator.next();
        localHashMap.put(localInputMethodInfo, getEnabledInputMethodSubtypeListLocked(paramContext, localInputMethodInfo, true));
      }
      return localHashMap;
    }
    
    public Pair<String, String> getLastInputMethodAndSubtypeLocked()
    {
      return getLastSubtypeForInputMethodLockedInternal(null);
    }
    
    public String getLastSubtypeForInputMethodLocked(String paramString)
    {
      paramString = getLastSubtypeForInputMethodLockedInternal(paramString);
      if (paramString != null) {
        return (String)second;
      }
      return null;
    }
    
    public String getSelectedInputMethod()
    {
      return getString("default_input_method", null);
    }
    
    public int getSelectedInputMethodSubtypeId(String paramString)
    {
      paramString = (InputMethodInfo)mMethodMap.get(paramString);
      if (paramString == null) {
        return -1;
      }
      return InputMethodUtils.getSubtypeIdFromHashCode(paramString, getSelectedInputMethodSubtypeHashCode());
    }
    
    public boolean isCurrentProfile(int paramInt)
    {
      try
      {
        if (paramInt == mCurrentUserId) {
          return true;
        }
        for (int i = 0; i < mCurrentProfileIds.length; i++) {
          if (paramInt == mCurrentProfileIds[i]) {
            return true;
          }
        }
        return false;
      }
      finally {}
    }
    
    public boolean isShowImeWithHardKeyboardEnabled()
    {
      return getBoolean("show_ime_with_hard_keyboard", false);
    }
    
    public boolean isSubtypeSelected()
    {
      boolean bool;
      if (getSelectedInputMethodSubtypeHashCode() != -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void putSelectedInputMethod(String paramString)
    {
      putString("default_input_method", paramString);
    }
    
    public void putSelectedSubtype(int paramInt)
    {
      putInt("selected_input_method_subtype", paramInt);
    }
    
    public void saveCurrentInputMethodAndSubtypeToHistory(String paramString, InputMethodSubtype paramInputMethodSubtype)
    {
      String str = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
      if (paramInputMethodSubtype != null) {
        str = String.valueOf(paramInputMethodSubtype.hashCode());
      }
      if (InputMethodUtils.canAddToLastInputMethod(paramInputMethodSubtype)) {
        addSubtypeToHistory(paramString, str);
      }
    }
    
    public void setCurrentProfileIds(int[] paramArrayOfInt)
    {
      try
      {
        mCurrentProfileIds = paramArrayOfInt;
        return;
      }
      finally {}
    }
    
    public void setShowImeWithHardKeyboard(boolean paramBoolean)
    {
      putBoolean("show_ime_with_hard_keyboard", paramBoolean);
    }
    
    public void switchCurrentUser(int paramInt, boolean paramBoolean)
    {
      if ((mCurrentUserId != paramInt) || (mCopyOnWrite != paramBoolean))
      {
        mCopyOnWriteDataStore.clear();
        mEnabledInputMethodsStrCache = "";
      }
      mCurrentUserId = paramInt;
      mCopyOnWrite = paramBoolean;
    }
  }
}
