package android.text.method;

import android.icu.lang.UCharacter;
import android.icu.text.DecimalFormatSymbols;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

public class DigitsKeyListener
  extends NumberKeyListener
{
  private static final char[][] COMPATIBILITY_CHARACTERS;
  private static final int DECIMAL = 2;
  private static final String DEFAULT_DECIMAL_POINT_CHARS = ".";
  private static final String DEFAULT_SIGN_CHARS = "-+";
  private static final char EN_DASH = '–';
  private static final char HYPHEN_MINUS = '-';
  private static final char MINUS_SIGN = '−';
  private static final int SIGN = 1;
  private static final Object sLocaleCacheLock = new Object();
  @GuardedBy("sLocaleCacheLock")
  private static final HashMap<Locale, DigitsKeyListener[]> sLocaleInstanceCache = new HashMap();
  private static final Object sStringCacheLock = new Object();
  @GuardedBy("sStringCacheLock")
  private static final HashMap<String, DigitsKeyListener> sStringInstanceCache = new HashMap();
  private char[] mAccepted;
  private final boolean mDecimal;
  private String mDecimalPointChars = ".";
  private final Locale mLocale;
  private boolean mNeedsAdvancedInput;
  private final boolean mSign;
  private String mSignChars = "-+";
  private final boolean mStringMode;
  
  static
  {
    char[] arrayOfChar1 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 43 };
    char[] arrayOfChar2 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 46 };
    COMPATIBILITY_CHARACTERS = new char[][] { { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 }, arrayOfChar1, arrayOfChar2, { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 43, 46 } };
  }
  
  @Deprecated
  public DigitsKeyListener()
  {
    this(null, false, false);
  }
  
  private DigitsKeyListener(String paramString)
  {
    mSign = false;
    mDecimal = false;
    mStringMode = true;
    mLocale = null;
    mAccepted = new char[paramString.length()];
    paramString.getChars(0, paramString.length(), mAccepted, 0);
    mNeedsAdvancedInput = false;
  }
  
  public DigitsKeyListener(Locale paramLocale)
  {
    this(paramLocale, false, false);
  }
  
  public DigitsKeyListener(Locale paramLocale, boolean paramBoolean1, boolean paramBoolean2)
  {
    mSign = paramBoolean1;
    mDecimal = paramBoolean2;
    mStringMode = false;
    mLocale = paramLocale;
    if (paramLocale == null)
    {
      setToCompat();
      return;
    }
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    if (!NumberKeyListener.addDigits(localLinkedHashSet, paramLocale))
    {
      setToCompat();
      return;
    }
    if ((paramBoolean1) || (paramBoolean2))
    {
      paramLocale = DecimalFormatSymbols.getInstance(paramLocale);
      if (paramBoolean1)
      {
        String str = stripBidiControls(paramLocale.getMinusSignString());
        Object localObject = stripBidiControls(paramLocale.getPlusSignString());
        if ((str.length() <= 1) && (((String)localObject).length() <= 1))
        {
          char c1 = str.charAt(0);
          char c2 = ((String)localObject).charAt(0);
          localLinkedHashSet.add(Character.valueOf(c1));
          localLinkedHashSet.add(Character.valueOf(c2));
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("");
          ((StringBuilder)localObject).append(c1);
          ((StringBuilder)localObject).append(c2);
          mSignChars = ((StringBuilder)localObject).toString();
          if ((c1 == '−') || (c1 == '–'))
          {
            localLinkedHashSet.add(Character.valueOf('-'));
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append(mSignChars);
            ((StringBuilder)localObject).append('-');
            mSignChars = ((StringBuilder)localObject).toString();
          }
        }
        else
        {
          setToCompat();
          return;
        }
      }
      if (paramBoolean2)
      {
        paramLocale = paramLocale.getDecimalSeparatorString();
        if (paramLocale.length() > 1)
        {
          setToCompat();
          return;
        }
        paramLocale = Character.valueOf(paramLocale.charAt(0));
        localLinkedHashSet.add(paramLocale);
        mDecimalPointChars = paramLocale.toString();
      }
    }
    mAccepted = NumberKeyListener.collectionToArray(localLinkedHashSet);
    calculateNeedForAdvancedInput();
  }
  
  @Deprecated
  public DigitsKeyListener(boolean paramBoolean1, boolean paramBoolean2)
  {
    this(null, paramBoolean1, paramBoolean2);
  }
  
  private void calculateNeedForAdvancedInput()
  {
    int i = mSign;
    int j;
    if (mDecimal) {
      j = 2;
    } else {
      j = 0;
    }
    mNeedsAdvancedInput = (ArrayUtils.containsAll(COMPATIBILITY_CHARACTERS[(i | j)], mAccepted) ^ true);
  }
  
  @Deprecated
  public static DigitsKeyListener getInstance()
  {
    return getInstance(false, false);
  }
  
  public static DigitsKeyListener getInstance(String paramString)
  {
    synchronized (sStringCacheLock)
    {
      DigitsKeyListener localDigitsKeyListener1 = (DigitsKeyListener)sStringInstanceCache.get(paramString);
      DigitsKeyListener localDigitsKeyListener2 = localDigitsKeyListener1;
      if (localDigitsKeyListener1 == null)
      {
        localDigitsKeyListener2 = new android/text/method/DigitsKeyListener;
        localDigitsKeyListener2.<init>(paramString);
        sStringInstanceCache.put(paramString, localDigitsKeyListener2);
      }
      return localDigitsKeyListener2;
    }
  }
  
  public static DigitsKeyListener getInstance(Locale paramLocale)
  {
    return getInstance(paramLocale, false, false);
  }
  
  public static DigitsKeyListener getInstance(Locale paramLocale, DigitsKeyListener paramDigitsKeyListener)
  {
    if (mStringMode) {
      return paramDigitsKeyListener;
    }
    return getInstance(paramLocale, mSign, mDecimal);
  }
  
  public static DigitsKeyListener getInstance(Locale paramLocale, boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool;
    if (paramBoolean2) {
      bool = true;
    } else {
      bool = false;
    }
    bool |= paramBoolean1;
    synchronized (sLocaleCacheLock)
    {
      Object localObject2 = (DigitsKeyListener[])sLocaleInstanceCache.get(paramLocale);
      if ((localObject2 != null) && (localObject2[bool] != null))
      {
        paramLocale = localObject2[bool];
        return paramLocale;
      }
      Object localObject3 = localObject2;
      if (localObject2 == null)
      {
        localObject3 = new DigitsKeyListener[4];
        sLocaleInstanceCache.put(paramLocale, localObject3);
      }
      localObject2 = new android/text/method/DigitsKeyListener;
      ((DigitsKeyListener)localObject2).<init>(paramLocale, paramBoolean1, paramBoolean2);
      localObject3[bool] = localObject2;
      return localObject2;
    }
  }
  
  @Deprecated
  public static DigitsKeyListener getInstance(boolean paramBoolean1, boolean paramBoolean2)
  {
    return getInstance(null, paramBoolean1, paramBoolean2);
  }
  
  private boolean isDecimalPointChar(char paramChar)
  {
    boolean bool;
    if (mDecimalPointChars.indexOf(paramChar) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isSignChar(char paramChar)
  {
    boolean bool;
    if (mSignChars.indexOf(paramChar) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void setToCompat()
  {
    mDecimalPointChars = ".";
    mSignChars = "-+";
    int i = mSign;
    int j;
    if (mDecimal) {
      j = 2;
    } else {
      j = 0;
    }
    mAccepted = COMPATIBILITY_CHARACTERS[(i | j)];
    mNeedsAdvancedInput = false;
  }
  
  private static String stripBidiControls(String paramString)
  {
    Object localObject1 = "";
    int i = 0;
    while (i < paramString.length())
    {
      char c = paramString.charAt(i);
      Object localObject2 = localObject1;
      if (!UCharacter.hasBinaryProperty(c, 2)) {
        if (((String)localObject1).isEmpty())
        {
          localObject2 = String.valueOf(c);
        }
        else
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append((String)localObject1);
          ((StringBuilder)localObject2).append(c);
          localObject2 = ((StringBuilder)localObject2).toString();
        }
      }
      i++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
  {
    CharSequence localCharSequence1 = super.filter(paramCharSequence, paramInt1, paramInt2, paramSpanned, paramInt3, paramInt4);
    if ((!mSign) && (!mDecimal)) {
      return localCharSequence1;
    }
    CharSequence localCharSequence2;
    int i;
    int j;
    if (localCharSequence1 != null)
    {
      localCharSequence2 = localCharSequence1;
      i = 0;
      j = localCharSequence1.length();
    }
    else
    {
      j = paramInt2;
      i = paramInt1;
      localCharSequence2 = paramCharSequence;
    }
    paramInt2 = -1;
    int k = -1;
    int m = paramSpanned.length();
    paramInt1 = 0;
    char c;
    int n;
    while (paramInt1 < paramInt3)
    {
      c = paramSpanned.charAt(paramInt1);
      if (isSignChar(c))
      {
        n = paramInt1;
      }
      else
      {
        n = paramInt2;
        if (isDecimalPointChar(c))
        {
          k = paramInt1;
          n = paramInt2;
        }
      }
      paramInt1++;
      paramInt2 = n;
    }
    paramInt1 = paramInt4;
    paramInt4 = k;
    while (paramInt1 < m)
    {
      c = paramSpanned.charAt(paramInt1);
      if (isSignChar(c)) {
        return "";
      }
      if (isDecimalPointChar(c)) {
        paramInt4 = paramInt1;
      }
      paramInt1++;
    }
    paramCharSequence = null;
    paramInt1 = j - 1;
    k = paramInt2;
    while (paramInt1 >= i)
    {
      c = localCharSequence2.charAt(paramInt1);
      int i1 = 0;
      if (isSignChar(c))
      {
        if ((paramInt1 == i) && (paramInt3 == 0))
        {
          if (k >= 0)
          {
            paramInt2 = 1;
            m = k;
            n = paramInt4;
          }
          else
          {
            m = paramInt1;
            n = paramInt4;
            paramInt2 = i1;
          }
        }
        else
        {
          paramInt2 = 1;
          m = k;
          n = paramInt4;
        }
      }
      else
      {
        m = k;
        n = paramInt4;
        paramInt2 = i1;
        if (isDecimalPointChar(c)) {
          if (paramInt4 >= 0)
          {
            paramInt2 = 1;
            m = k;
            n = paramInt4;
          }
          else
          {
            n = paramInt1;
            paramInt2 = i1;
            m = k;
          }
        }
      }
      paramSpanned = paramCharSequence;
      if (paramInt2 != 0)
      {
        if (j == i + 1) {
          return "";
        }
        paramSpanned = paramCharSequence;
        if (paramCharSequence == null) {
          paramSpanned = new SpannableStringBuilder(localCharSequence2, i, j);
        }
        paramSpanned.delete(paramInt1 - i, paramInt1 + 1 - i);
      }
      paramInt1--;
      k = m;
      paramCharSequence = paramSpanned;
      paramInt4 = n;
    }
    if (paramCharSequence != null) {
      return paramCharSequence;
    }
    if (localCharSequence1 != null) {
      return localCharSequence1;
    }
    return null;
  }
  
  protected char[] getAcceptedChars()
  {
    return mAccepted;
  }
  
  public int getInputType()
  {
    int i;
    if (mNeedsAdvancedInput)
    {
      i = 1;
    }
    else
    {
      int j = 2;
      if (mSign) {
        j = 0x2 | 0x1000;
      }
      i = j;
      if (mDecimal) {
        i = j | 0x2000;
      }
    }
    return i;
  }
}
