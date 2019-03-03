package android.view.inputmethod;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.text.DisplayContext;
import android.icu.text.LocaleDisplayNames;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.inputmethod.InputMethodUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class InputMethodSubtype
  implements Parcelable
{
  public static final Parcelable.Creator<InputMethodSubtype> CREATOR = new Parcelable.Creator()
  {
    public InputMethodSubtype createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InputMethodSubtype(paramAnonymousParcel);
    }
    
    public InputMethodSubtype[] newArray(int paramAnonymousInt)
    {
      return new InputMethodSubtype[paramAnonymousInt];
    }
  };
  private static final String EXTRA_KEY_UNTRANSLATABLE_STRING_IN_SUBTYPE_NAME = "UntranslatableReplacementStringInSubtypeName";
  private static final String EXTRA_VALUE_KEY_VALUE_SEPARATOR = "=";
  private static final String EXTRA_VALUE_PAIR_SEPARATOR = ",";
  private static final String LANGUAGE_TAG_NONE = "";
  private static final int SUBTYPE_ID_NONE = 0;
  private static final String TAG = InputMethodSubtype.class.getSimpleName();
  private volatile Locale mCachedLocaleObj;
  private volatile HashMap<String, String> mExtraValueHashMapCache;
  private final boolean mIsAsciiCapable;
  private final boolean mIsAuxiliary;
  private final Object mLock = new Object();
  private final boolean mOverridesImplicitlyEnabledSubtype;
  private final String mSubtypeExtraValue;
  private final int mSubtypeHashCode;
  private final int mSubtypeIconResId;
  private final int mSubtypeId;
  private final String mSubtypeLanguageTag;
  private final String mSubtypeLocale;
  private final String mSubtypeMode;
  private final int mSubtypeNameResId;
  
  @Deprecated
  public InputMethodSubtype(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2)
  {
    this(paramInt1, paramInt2, paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2, 0);
  }
  
  @Deprecated
  public InputMethodSubtype(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, int paramInt3)
  {
    this(getBuilder(paramInt1, paramInt2, paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2, paramInt3, false));
  }
  
  InputMethodSubtype(Parcel paramParcel)
  {
    mSubtypeNameResId = paramParcel.readInt();
    mSubtypeIconResId = paramParcel.readInt();
    String str = paramParcel.readString();
    if (str == null) {
      str = "";
    }
    mSubtypeLocale = str;
    str = paramParcel.readString();
    if (str == null) {
      str = "";
    }
    mSubtypeLanguageTag = str;
    str = paramParcel.readString();
    if (str == null) {
      str = "";
    }
    mSubtypeMode = str;
    str = paramParcel.readString();
    if (str == null) {
      str = "";
    }
    mSubtypeExtraValue = str;
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsAuxiliary = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mOverridesImplicitlyEnabledSubtype = bool2;
    mSubtypeHashCode = paramParcel.readInt();
    mSubtypeId = paramParcel.readInt();
    boolean bool2 = bool1;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    }
    mIsAsciiCapable = bool2;
  }
  
  private InputMethodSubtype(InputMethodSubtypeBuilder paramInputMethodSubtypeBuilder)
  {
    mSubtypeNameResId = mSubtypeNameResId;
    mSubtypeIconResId = mSubtypeIconResId;
    mSubtypeLocale = mSubtypeLocale;
    mSubtypeLanguageTag = mSubtypeLanguageTag;
    mSubtypeMode = mSubtypeMode;
    mSubtypeExtraValue = mSubtypeExtraValue;
    mIsAuxiliary = mIsAuxiliary;
    mOverridesImplicitlyEnabledSubtype = mOverridesImplicitlyEnabledSubtype;
    mSubtypeId = mSubtypeId;
    mIsAsciiCapable = mIsAsciiCapable;
    if (mSubtypeId != 0) {
      mSubtypeHashCode = mSubtypeId;
    } else {
      mSubtypeHashCode = hashCodeInternal(mSubtypeLocale, mSubtypeMode, mSubtypeExtraValue, mIsAuxiliary, mOverridesImplicitlyEnabledSubtype, mIsAsciiCapable);
    }
  }
  
  private static InputMethodSubtypeBuilder getBuilder(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, boolean paramBoolean3)
  {
    InputMethodSubtypeBuilder localInputMethodSubtypeBuilder = new InputMethodSubtypeBuilder();
    InputMethodSubtypeBuilder.access$102(localInputMethodSubtypeBuilder, paramInt1);
    InputMethodSubtypeBuilder.access$202(localInputMethodSubtypeBuilder, paramInt2);
    InputMethodSubtypeBuilder.access$302(localInputMethodSubtypeBuilder, paramString1);
    InputMethodSubtypeBuilder.access$402(localInputMethodSubtypeBuilder, paramString2);
    InputMethodSubtypeBuilder.access$502(localInputMethodSubtypeBuilder, paramString3);
    InputMethodSubtypeBuilder.access$602(localInputMethodSubtypeBuilder, paramBoolean1);
    InputMethodSubtypeBuilder.access$702(localInputMethodSubtypeBuilder, paramBoolean2);
    InputMethodSubtypeBuilder.access$802(localInputMethodSubtypeBuilder, paramInt3);
    InputMethodSubtypeBuilder.access$902(localInputMethodSubtypeBuilder, paramBoolean3);
    return localInputMethodSubtypeBuilder;
  }
  
  private HashMap<String, String> getExtraValueHashMap()
  {
    try
    {
      Object localObject1 = mExtraValueHashMapCache;
      if (localObject1 != null) {
        return localObject1;
      }
      HashMap localHashMap = new java/util/HashMap;
      localHashMap.<init>();
      localObject1 = mSubtypeExtraValue.split(",");
      for (int i = 0; i < localObject1.length; i++)
      {
        String[] arrayOfString = localObject1[i].split("=");
        if (arrayOfString.length == 1)
        {
          localHashMap.put(arrayOfString[0], null);
        }
        else if (arrayOfString.length > 1)
        {
          if (arrayOfString.length > 2) {
            Slog.w(TAG, "ExtraValue has two or more '='s");
          }
          localHashMap.put(arrayOfString[0], arrayOfString[1]);
        }
      }
      mExtraValueHashMapCache = localHashMap;
      return localHashMap;
    }
    finally {}
  }
  
  private static String getLocaleDisplayName(Locale paramLocale1, Locale paramLocale2, DisplayContext paramDisplayContext)
  {
    if (paramLocale2 == null) {
      return "";
    }
    if (paramLocale1 == null) {
      paramLocale1 = Locale.getDefault();
    }
    return LocaleDisplayNames.getInstance(paramLocale1, new DisplayContext[] { paramDisplayContext }).localeDisplayName(paramLocale2);
  }
  
  private static Locale getLocaleFromContext(Context paramContext)
  {
    if (paramContext == null) {
      return null;
    }
    if (paramContext.getResources() == null) {
      return null;
    }
    paramContext = paramContext.getResources().getConfiguration();
    if (paramContext == null) {
      return null;
    }
    return paramContext.getLocales().get(0);
  }
  
  private static int hashCodeInternal(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if ((paramBoolean3 ^ true)) {
      return Arrays.hashCode(new Object[] { paramString1, paramString2, paramString3, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2) });
    }
    return Arrays.hashCode(new Object[] { paramString1, paramString2, paramString3, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2), Boolean.valueOf(paramBoolean3) });
  }
  
  public static List<InputMethodSubtype> sort(Context paramContext, int paramInt, InputMethodInfo paramInputMethodInfo, List<InputMethodSubtype> paramList)
  {
    if (paramInputMethodInfo == null) {
      return paramList;
    }
    HashSet localHashSet = new HashSet(paramList);
    paramContext = new ArrayList();
    int i = paramInputMethodInfo.getSubtypeCount();
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      paramList = paramInputMethodInfo.getSubtypeAt(paramInt);
      if (localHashSet.contains(paramList))
      {
        paramContext.add(paramList);
        localHashSet.remove(paramList);
      }
    }
    paramInputMethodInfo = localHashSet.iterator();
    while (paramInputMethodInfo.hasNext()) {
      paramContext.add((InputMethodSubtype)paramInputMethodInfo.next());
    }
    return paramContext;
  }
  
  public boolean containsExtraValueKey(String paramString)
  {
    return getExtraValueHashMap().containsKey(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof InputMethodSubtype;
    boolean bool2 = false;
    boolean bool3 = false;
    if (bool1)
    {
      paramObject = (InputMethodSubtype)paramObject;
      if ((mSubtypeId == 0) && (mSubtypeId == 0))
      {
        if ((paramObject.hashCode() == hashCode()) && (paramObject.getLocale().equals(getLocale())) && (paramObject.getLanguageTag().equals(getLanguageTag())) && (paramObject.getMode().equals(getMode())) && (paramObject.getExtraValue().equals(getExtraValue())) && (paramObject.isAuxiliary() == isAuxiliary()) && (paramObject.overridesImplicitlyEnabledSubtype() == overridesImplicitlyEnabledSubtype()) && (paramObject.isAsciiCapable() == isAsciiCapable())) {
          bool3 = true;
        }
        return bool3;
      }
      bool3 = bool2;
      if (paramObject.hashCode() == hashCode()) {
        bool3 = true;
      }
      return bool3;
    }
    return false;
  }
  
  public CharSequence getDisplayName(Context paramContext, String paramString, ApplicationInfo paramApplicationInfo)
  {
    if (mSubtypeNameResId == 0) {
      return getLocaleDisplayName(getLocaleFromContext(paramContext), getLocaleObject(), DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU);
    }
    paramApplicationInfo = paramContext.getPackageManager().getText(paramString, mSubtypeNameResId, paramApplicationInfo);
    if (TextUtils.isEmpty(paramApplicationInfo)) {
      return "";
    }
    String str = paramApplicationInfo.toString();
    if (containsExtraValueKey("UntranslatableReplacementStringInSubtypeName"))
    {
      paramContext = getExtraValueOf("UntranslatableReplacementStringInSubtypeName");
    }
    else
    {
      if (TextUtils.equals(str, "%s")) {
        paramString = DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU;
      }
      for (;;)
      {
        break;
        if (str.startsWith("%s")) {
          paramString = DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE;
        } else {
          paramString = DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE;
        }
      }
      paramContext = getLocaleDisplayName(getLocaleFromContext(paramContext), getLocaleObject(), paramString);
    }
    paramString = paramContext;
    if (paramContext == null) {
      paramString = "";
    }
    try
    {
      paramContext = String.format(str, new Object[] { paramString });
      return paramContext;
    }
    catch (IllegalFormatException paramContext)
    {
      str = TAG;
      paramString = new StringBuilder();
      paramString.append("Found illegal format in subtype name(");
      paramString.append(paramApplicationInfo);
      paramString.append("): ");
      paramString.append(paramContext);
      Slog.w(str, paramString.toString());
    }
    return "";
  }
  
  public String getExtraValue()
  {
    return mSubtypeExtraValue;
  }
  
  public String getExtraValueOf(String paramString)
  {
    return (String)getExtraValueHashMap().get(paramString);
  }
  
  public int getIconResId()
  {
    return mSubtypeIconResId;
  }
  
  public String getLanguageTag()
  {
    return mSubtypeLanguageTag;
  }
  
  @Deprecated
  public String getLocale()
  {
    return mSubtypeLocale;
  }
  
  public Locale getLocaleObject()
  {
    if (mCachedLocaleObj != null) {
      return mCachedLocaleObj;
    }
    synchronized (mLock)
    {
      if (mCachedLocaleObj != null)
      {
        localLocale = mCachedLocaleObj;
        return localLocale;
      }
      if (!TextUtils.isEmpty(mSubtypeLanguageTag)) {
        mCachedLocaleObj = Locale.forLanguageTag(mSubtypeLanguageTag);
      } else {
        mCachedLocaleObj = InputMethodUtils.constructLocaleFromString(mSubtypeLocale);
      }
      Locale localLocale = mCachedLocaleObj;
      return localLocale;
    }
  }
  
  public String getMode()
  {
    return mSubtypeMode;
  }
  
  public int getNameResId()
  {
    return mSubtypeNameResId;
  }
  
  public final int getSubtypeId()
  {
    return mSubtypeId;
  }
  
  public final boolean hasSubtypeId()
  {
    boolean bool;
    if (mSubtypeId != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return mSubtypeHashCode;
  }
  
  public boolean isAsciiCapable()
  {
    return mIsAsciiCapable;
  }
  
  public boolean isAuxiliary()
  {
    return mIsAuxiliary;
  }
  
  public boolean overridesImplicitlyEnabledSubtype()
  {
    return mOverridesImplicitlyEnabledSubtype;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSubtypeNameResId);
    paramParcel.writeInt(mSubtypeIconResId);
    paramParcel.writeString(mSubtypeLocale);
    paramParcel.writeString(mSubtypeLanguageTag);
    paramParcel.writeString(mSubtypeMode);
    paramParcel.writeString(mSubtypeExtraValue);
    paramParcel.writeInt(mIsAuxiliary);
    paramParcel.writeInt(mOverridesImplicitlyEnabledSubtype);
    paramParcel.writeInt(mSubtypeHashCode);
    paramParcel.writeInt(mSubtypeId);
    paramParcel.writeInt(mIsAsciiCapable);
  }
  
  public static class InputMethodSubtypeBuilder
  {
    private boolean mIsAsciiCapable = false;
    private boolean mIsAuxiliary = false;
    private boolean mOverridesImplicitlyEnabledSubtype = false;
    private String mSubtypeExtraValue = "";
    private int mSubtypeIconResId = 0;
    private int mSubtypeId = 0;
    private String mSubtypeLanguageTag = "";
    private String mSubtypeLocale = "";
    private String mSubtypeMode = "";
    private int mSubtypeNameResId = 0;
    
    public InputMethodSubtypeBuilder() {}
    
    public InputMethodSubtype build()
    {
      return new InputMethodSubtype(this, null);
    }
    
    public InputMethodSubtypeBuilder setIsAsciiCapable(boolean paramBoolean)
    {
      mIsAsciiCapable = paramBoolean;
      return this;
    }
    
    public InputMethodSubtypeBuilder setIsAuxiliary(boolean paramBoolean)
    {
      mIsAuxiliary = paramBoolean;
      return this;
    }
    
    public InputMethodSubtypeBuilder setLanguageTag(String paramString)
    {
      if (paramString == null) {
        paramString = "";
      }
      mSubtypeLanguageTag = paramString;
      return this;
    }
    
    public InputMethodSubtypeBuilder setOverridesImplicitlyEnabledSubtype(boolean paramBoolean)
    {
      mOverridesImplicitlyEnabledSubtype = paramBoolean;
      return this;
    }
    
    public InputMethodSubtypeBuilder setSubtypeExtraValue(String paramString)
    {
      if (paramString == null) {
        paramString = "";
      }
      mSubtypeExtraValue = paramString;
      return this;
    }
    
    public InputMethodSubtypeBuilder setSubtypeIconResId(int paramInt)
    {
      mSubtypeIconResId = paramInt;
      return this;
    }
    
    public InputMethodSubtypeBuilder setSubtypeId(int paramInt)
    {
      mSubtypeId = paramInt;
      return this;
    }
    
    public InputMethodSubtypeBuilder setSubtypeLocale(String paramString)
    {
      if (paramString == null) {
        paramString = "";
      }
      mSubtypeLocale = paramString;
      return this;
    }
    
    public InputMethodSubtypeBuilder setSubtypeMode(String paramString)
    {
      if (paramString == null) {
        paramString = "";
      }
      mSubtypeMode = paramString;
      return this;
    }
    
    public InputMethodSubtypeBuilder setSubtypeNameResId(int paramInt)
    {
      mSubtypeNameResId = paramInt;
      return this;
    }
  }
}
