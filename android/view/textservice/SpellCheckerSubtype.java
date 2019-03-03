package android.view.textservice;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class SpellCheckerSubtype
  implements Parcelable
{
  public static final Parcelable.Creator<SpellCheckerSubtype> CREATOR = new Parcelable.Creator()
  {
    public SpellCheckerSubtype createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SpellCheckerSubtype(paramAnonymousParcel);
    }
    
    public SpellCheckerSubtype[] newArray(int paramAnonymousInt)
    {
      return new SpellCheckerSubtype[paramAnonymousInt];
    }
  };
  private static final String EXTRA_VALUE_KEY_VALUE_SEPARATOR = "=";
  private static final String EXTRA_VALUE_PAIR_SEPARATOR = ",";
  public static final int SUBTYPE_ID_NONE = 0;
  private static final String SUBTYPE_LANGUAGE_TAG_NONE = "";
  private static final String TAG = SpellCheckerSubtype.class.getSimpleName();
  private HashMap<String, String> mExtraValueHashMapCache;
  private final String mSubtypeExtraValue;
  private final int mSubtypeHashCode;
  private final int mSubtypeId;
  private final String mSubtypeLanguageTag;
  private final String mSubtypeLocale;
  private final int mSubtypeNameResId;
  
  @Deprecated
  public SpellCheckerSubtype(int paramInt, String paramString1, String paramString2)
  {
    this(paramInt, paramString1, "", paramString2, 0);
  }
  
  public SpellCheckerSubtype(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2)
  {
    mSubtypeNameResId = paramInt1;
    if (paramString1 == null) {
      paramString1 = "";
    }
    mSubtypeLocale = paramString1;
    if (paramString2 == null) {
      paramString2 = "";
    }
    mSubtypeLanguageTag = paramString2;
    if (paramString3 != null) {
      paramString1 = paramString3;
    } else {
      paramString1 = "";
    }
    mSubtypeExtraValue = paramString1;
    mSubtypeId = paramInt2;
    if (mSubtypeId != 0) {
      paramInt1 = mSubtypeId;
    } else {
      paramInt1 = hashCodeInternal(mSubtypeLocale, mSubtypeExtraValue);
    }
    mSubtypeHashCode = paramInt1;
  }
  
  SpellCheckerSubtype(Parcel paramParcel)
  {
    mSubtypeNameResId = paramParcel.readInt();
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
    mSubtypeExtraValue = str;
    mSubtypeId = paramParcel.readInt();
    int i;
    if (mSubtypeId != 0) {
      i = mSubtypeId;
    } else {
      i = hashCodeInternal(mSubtypeLocale, mSubtypeExtraValue);
    }
    mSubtypeHashCode = i;
  }
  
  private HashMap<String, String> getExtraValueHashMap()
  {
    if (mExtraValueHashMapCache == null)
    {
      mExtraValueHashMapCache = new HashMap();
      String[] arrayOfString1 = mSubtypeExtraValue.split(",");
      int i = arrayOfString1.length;
      for (int j = 0; j < i; j++)
      {
        String[] arrayOfString2 = arrayOfString1[j].split("=");
        if (arrayOfString2.length == 1)
        {
          mExtraValueHashMapCache.put(arrayOfString2[0], null);
        }
        else if (arrayOfString2.length > 1)
        {
          if (arrayOfString2.length > 2) {
            Slog.w(TAG, "ExtraValue has two or more '='s");
          }
          mExtraValueHashMapCache.put(arrayOfString2[0], arrayOfString2[1]);
        }
      }
    }
    return mExtraValueHashMapCache;
  }
  
  private static int hashCodeInternal(String paramString1, String paramString2)
  {
    return Arrays.hashCode(new Object[] { paramString1, paramString2 });
  }
  
  public static List<SpellCheckerSubtype> sort(Context paramContext, int paramInt, SpellCheckerInfo paramSpellCheckerInfo, List<SpellCheckerSubtype> paramList)
  {
    if (paramSpellCheckerInfo == null) {
      return paramList;
    }
    paramList = new HashSet(paramList);
    paramContext = new ArrayList();
    int i = paramSpellCheckerInfo.getSubtypeCount();
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      SpellCheckerSubtype localSpellCheckerSubtype = paramSpellCheckerInfo.getSubtypeAt(paramInt);
      if (paramList.contains(localSpellCheckerSubtype))
      {
        paramContext.add(localSpellCheckerSubtype);
        paramList.remove(localSpellCheckerSubtype);
      }
    }
    paramSpellCheckerInfo = paramList.iterator();
    while (paramSpellCheckerInfo.hasNext()) {
      paramContext.add((SpellCheckerSubtype)paramSpellCheckerInfo.next());
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
    boolean bool1 = paramObject instanceof SpellCheckerSubtype;
    boolean bool2 = false;
    boolean bool3 = false;
    if (bool1)
    {
      paramObject = (SpellCheckerSubtype)paramObject;
      if ((mSubtypeId == 0) && (mSubtypeId == 0))
      {
        if ((paramObject.hashCode() == hashCode()) && (paramObject.getNameResId() == getNameResId()) && (paramObject.getLocale().equals(getLocale())) && (paramObject.getLanguageTag().equals(getLanguageTag())) && (paramObject.getExtraValue().equals(getExtraValue()))) {
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
    Object localObject = getLocaleObject();
    if (localObject != null) {
      localObject = ((Locale)localObject).getDisplayName();
    } else {
      localObject = mSubtypeLocale;
    }
    if (mSubtypeNameResId == 0) {
      return localObject;
    }
    paramContext = paramContext.getPackageManager().getText(paramString, mSubtypeNameResId, paramApplicationInfo);
    if (!TextUtils.isEmpty(paramContext)) {
      return String.format(paramContext.toString(), new Object[] { localObject });
    }
    return localObject;
  }
  
  public String getExtraValue()
  {
    return mSubtypeExtraValue;
  }
  
  public String getExtraValueOf(String paramString)
  {
    return (String)getExtraValueHashMap().get(paramString);
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
    if (!TextUtils.isEmpty(mSubtypeLanguageTag)) {
      return Locale.forLanguageTag(mSubtypeLanguageTag);
    }
    return InputMethodUtils.constructLocaleFromString(mSubtypeLocale);
  }
  
  public int getNameResId()
  {
    return mSubtypeNameResId;
  }
  
  public int hashCode()
  {
    return mSubtypeHashCode;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSubtypeNameResId);
    paramParcel.writeString(mSubtypeLocale);
    paramParcel.writeString(mSubtypeLanguageTag);
    paramParcel.writeString(mSubtypeExtraValue);
    paramParcel.writeInt(mSubtypeId);
  }
}
