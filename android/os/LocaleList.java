package android.os;

import android.icu.util.ULocale;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public final class LocaleList
  implements Parcelable
{
  public static final Parcelable.Creator<LocaleList> CREATOR;
  private static final Locale EN_LATN;
  private static final Locale LOCALE_AR_XB;
  private static final Locale LOCALE_EN_XA;
  private static final int NUM_PSEUDO_LOCALES = 2;
  private static final String STRING_AR_XB = "ar-XB";
  private static final String STRING_EN_XA = "en-XA";
  @GuardedBy("sLock")
  private static LocaleList sDefaultAdjustedLocaleList = null;
  @GuardedBy("sLock")
  private static LocaleList sDefaultLocaleList;
  private static final Locale[] sEmptyList = new Locale[0];
  private static final LocaleList sEmptyLocaleList = new LocaleList(new Locale[0]);
  @GuardedBy("sLock")
  private static Locale sLastDefaultLocale = null;
  @GuardedBy("sLock")
  private static LocaleList sLastExplicitlySetLocaleList;
  private static final Object sLock;
  private final Locale[] mList;
  private final String mStringRepresentation;
  
  static
  {
    CREATOR = new Parcelable.Creator()
    {
      public LocaleList createFromParcel(Parcel paramAnonymousParcel)
      {
        return LocaleList.forLanguageTags(paramAnonymousParcel.readString());
      }
      
      public LocaleList[] newArray(int paramAnonymousInt)
      {
        return new LocaleList[paramAnonymousInt];
      }
    };
    LOCALE_EN_XA = new Locale("en", "XA");
    LOCALE_AR_XB = new Locale("ar", "XB");
    EN_LATN = Locale.forLanguageTag("en-Latn");
    sLock = new Object();
    sLastExplicitlySetLocaleList = null;
    sDefaultLocaleList = null;
  }
  
  public LocaleList(Locale paramLocale, LocaleList paramLocaleList)
  {
    if (paramLocale != null)
    {
      int i = 0;
      int j;
      if (paramLocaleList == null) {
        j = 0;
      } else {
        j = mList.length;
      }
      int k = -1;
      int n;
      for (int m = 0;; m++)
      {
        n = k;
        if (m >= j) {
          break;
        }
        if (paramLocale.equals(mList[m]))
        {
          n = m;
          break;
        }
      }
      if (n == -1) {
        m = 1;
      } else {
        m = 0;
      }
      k = m + j;
      Locale[] arrayOfLocale = new Locale[k];
      arrayOfLocale[0] = ((Locale)paramLocale.clone());
      if (n == -1) {
        for (m = 0; m < j; m++) {
          arrayOfLocale[(m + 1)] = ((Locale)mList[m].clone());
        }
      }
      for (m = 0; m < n; m++) {
        arrayOfLocale[(m + 1)] = ((Locale)mList[m].clone());
      }
      for (m = n + 1; m < j; m++) {
        arrayOfLocale[m] = ((Locale)mList[m].clone());
      }
      paramLocale = new StringBuilder();
      for (m = i; m < k; m++)
      {
        paramLocale.append(arrayOfLocale[m].toLanguageTag());
        if (m < k - 1) {
          paramLocale.append(',');
        }
      }
      mList = arrayOfLocale;
      mStringRepresentation = paramLocale.toString();
      return;
    }
    throw new NullPointerException("topLocale is null");
  }
  
  public LocaleList(Locale... paramVarArgs)
  {
    if (paramVarArgs.length == 0)
    {
      mList = sEmptyList;
      mStringRepresentation = "";
    }
    else
    {
      Locale[] arrayOfLocale = new Locale[paramVarArgs.length];
      HashSet localHashSet = new HashSet();
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 0;
      while (i < paramVarArgs.length)
      {
        Locale localLocale = paramVarArgs[i];
        if (localLocale != null)
        {
          if (!localHashSet.contains(localLocale))
          {
            localLocale = (Locale)localLocale.clone();
            arrayOfLocale[i] = localLocale;
            localStringBuilder.append(localLocale.toLanguageTag());
            if (i < paramVarArgs.length - 1) {
              localStringBuilder.append(',');
            }
            localHashSet.add(localLocale);
            i++;
          }
          else
          {
            paramVarArgs = new StringBuilder();
            paramVarArgs.append("list[");
            paramVarArgs.append(i);
            paramVarArgs.append("] is a repetition");
            throw new IllegalArgumentException(paramVarArgs.toString());
          }
        }
        else
        {
          paramVarArgs = new StringBuilder();
          paramVarArgs.append("list[");
          paramVarArgs.append(i);
          paramVarArgs.append("] is null");
          throw new NullPointerException(paramVarArgs.toString());
        }
      }
      mList = arrayOfLocale;
      mStringRepresentation = localStringBuilder.toString();
    }
  }
  
  private Locale computeFirstMatch(Collection<String> paramCollection, boolean paramBoolean)
  {
    int i = computeFirstMatchIndex(paramCollection, paramBoolean);
    if (i == -1) {
      paramCollection = null;
    } else {
      paramCollection = mList[i];
    }
    return paramCollection;
  }
  
  private int computeFirstMatchIndex(Collection<String> paramCollection, boolean paramBoolean)
  {
    if (mList.length == 1) {
      return 0;
    }
    if (mList.length == 0) {
      return -1;
    }
    int i = Integer.MAX_VALUE;
    int j = i;
    int k;
    if (paramBoolean)
    {
      k = findFirstMatchIndex(EN_LATN);
      if (k == 0) {
        return 0;
      }
      j = i;
      if (k < Integer.MAX_VALUE) {
        j = k;
      }
    }
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      k = findFirstMatchIndex(Locale.forLanguageTag((String)paramCollection.next()));
      if (k == 0) {
        return 0;
      }
      i = j;
      if (k < j) {
        i = k;
      }
      j = i;
    }
    if (j == Integer.MAX_VALUE) {
      return 0;
    }
    return j;
  }
  
  private int findFirstMatchIndex(Locale paramLocale)
  {
    for (int i = 0; i < mList.length; i++) {
      if (matchScore(paramLocale, mList[i]) > 0) {
        return i;
      }
    }
    return Integer.MAX_VALUE;
  }
  
  public static LocaleList forLanguageTags(String paramString)
  {
    if ((paramString != null) && (!paramString.equals("")))
    {
      paramString = paramString.split(",");
      Locale[] arrayOfLocale = new Locale[paramString.length];
      for (int i = 0; i < arrayOfLocale.length; i++) {
        arrayOfLocale[i] = Locale.forLanguageTag(paramString[i]);
      }
      return new LocaleList(arrayOfLocale);
    }
    return getEmptyLocaleList();
  }
  
  public static LocaleList getAdjustedDefault()
  {
    getDefault();
    synchronized (sLock)
    {
      LocaleList localLocaleList = sDefaultAdjustedLocaleList;
      return localLocaleList;
    }
  }
  
  public static LocaleList getDefault()
  {
    Object localObject1 = Locale.getDefault();
    synchronized (sLock)
    {
      if (!((Locale)localObject1).equals(sLastDefaultLocale))
      {
        sLastDefaultLocale = (Locale)localObject1;
        if ((sDefaultLocaleList != null) && (((Locale)localObject1).equals(sDefaultLocaleList.get(0))))
        {
          localObject1 = sDefaultLocaleList;
          return localObject1;
        }
        LocaleList localLocaleList = new android/os/LocaleList;
        localLocaleList.<init>((Locale)localObject1, sLastExplicitlySetLocaleList);
        sDefaultLocaleList = localLocaleList;
        sDefaultAdjustedLocaleList = sDefaultLocaleList;
      }
      localObject1 = sDefaultLocaleList;
      return localObject1;
    }
  }
  
  public static LocaleList getEmptyLocaleList()
  {
    return sEmptyLocaleList;
  }
  
  private static String getLikelyScript(Locale paramLocale)
  {
    String str = paramLocale.getScript();
    if (!str.isEmpty()) {
      return str;
    }
    return ULocale.addLikelySubtags(ULocale.forLocale(paramLocale)).getScript();
  }
  
  private static boolean isPseudoLocale(String paramString)
  {
    boolean bool;
    if ((!"en-XA".equals(paramString)) && (!"ar-XB".equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isPseudoLocale(Locale paramLocale)
  {
    boolean bool;
    if ((!LOCALE_EN_XA.equals(paramLocale)) && (!LOCALE_AR_XB.equals(paramLocale))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isPseudoLocalesOnly(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      return true;
    }
    if (paramArrayOfString.length > 3) {
      return false;
    }
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if ((!str.isEmpty()) && (!isPseudoLocale(str))) {
        return false;
      }
    }
    return true;
  }
  
  private static int matchScore(Locale paramLocale1, Locale paramLocale2)
  {
    boolean bool = paramLocale1.equals(paramLocale2);
    int i = 1;
    if (bool) {
      return 1;
    }
    if (!paramLocale1.getLanguage().equals(paramLocale2.getLanguage())) {
      return 0;
    }
    if ((!isPseudoLocale(paramLocale1)) && (!isPseudoLocale(paramLocale2)))
    {
      String str = getLikelyScript(paramLocale1);
      if (str.isEmpty())
      {
        paramLocale1 = paramLocale1.getCountry();
        if ((!paramLocale1.isEmpty()) && (!paramLocale1.equals(paramLocale2.getCountry()))) {
          i = 0;
        }
        return i;
      }
      return str.equals(getLikelyScript(paramLocale2));
    }
    return 0;
  }
  
  public static void setDefault(LocaleList paramLocaleList)
  {
    setDefault(paramLocaleList, 0);
  }
  
  public static void setDefault(LocaleList paramLocaleList, int paramInt)
  {
    if (paramLocaleList != null)
    {
      if (!paramLocaleList.isEmpty()) {
        synchronized (sLock)
        {
          sLastDefaultLocale = paramLocaleList.get(paramInt);
          Locale.setDefault(sLastDefaultLocale);
          sLastExplicitlySetLocaleList = paramLocaleList;
          sDefaultLocaleList = paramLocaleList;
          if (paramInt == 0)
          {
            sDefaultAdjustedLocaleList = sDefaultLocaleList;
          }
          else
          {
            paramLocaleList = new android/os/LocaleList;
            paramLocaleList.<init>(sLastDefaultLocale, sDefaultLocaleList);
            sDefaultAdjustedLocaleList = paramLocaleList;
          }
          return;
        }
      }
      throw new IllegalArgumentException("locales is empty");
    }
    throw new NullPointerException("locales is null");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof LocaleList)) {
      return false;
    }
    paramObject = mList;
    if (mList.length != paramObject.length) {
      return false;
    }
    for (int i = 0; i < mList.length; i++) {
      if (!mList[i].equals(paramObject[i])) {
        return false;
      }
    }
    return true;
  }
  
  public Locale get(int paramInt)
  {
    Locale localLocale;
    if ((paramInt >= 0) && (paramInt < mList.length)) {
      localLocale = mList[paramInt];
    } else {
      localLocale = null;
    }
    return localLocale;
  }
  
  public Locale getFirstMatch(String[] paramArrayOfString)
  {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), false);
  }
  
  public int getFirstMatchIndex(String[] paramArrayOfString)
  {
    return computeFirstMatchIndex(Arrays.asList(paramArrayOfString), false);
  }
  
  public int getFirstMatchIndexWithEnglishSupported(Collection<String> paramCollection)
  {
    return computeFirstMatchIndex(paramCollection, true);
  }
  
  public int getFirstMatchIndexWithEnglishSupported(String[] paramArrayOfString)
  {
    return getFirstMatchIndexWithEnglishSupported(Arrays.asList(paramArrayOfString));
  }
  
  public Locale getFirstMatchWithEnglishSupported(String[] paramArrayOfString)
  {
    return computeFirstMatch(Arrays.asList(paramArrayOfString), true);
  }
  
  public int hashCode()
  {
    int i = 1;
    for (int j = 0; j < mList.length; j++) {
      i = 31 * i + mList[j].hashCode();
    }
    return i;
  }
  
  public int indexOf(Locale paramLocale)
  {
    for (int i = 0; i < mList.length; i++) {
      if (mList[i].equals(paramLocale)) {
        return i;
      }
    }
    return -1;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if (mList.length == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int size()
  {
    return mList.length;
  }
  
  public String toLanguageTags()
  {
    return mStringRepresentation;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    for (int i = 0; i < mList.length; i++)
    {
      localStringBuilder.append(mList[i]);
      if (i < mList.length - 1) {
        localStringBuilder.append(',');
      }
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mStringRepresentation);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    for (int i = 0; i < mList.length; i++)
    {
      Locale localLocale = mList[i];
      long l = paramProtoOutputStream.start(paramLong);
      paramProtoOutputStream.write(1138166333441L, localLocale.getLanguage());
      paramProtoOutputStream.write(1138166333442L, localLocale.getCountry());
      paramProtoOutputStream.write(1138166333443L, localLocale.getVariant());
      paramProtoOutputStream.end(l);
    }
  }
}
