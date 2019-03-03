package android.text.method;

import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

public class DateKeyListener
  extends NumberKeyListener
{
  @Deprecated
  public static final char[] CHARACTERS;
  private static final String[] SKELETONS = { "yMd", "yM", "Md" };
  private static final String SYMBOLS_TO_IGNORE = "yMLd";
  @GuardedBy("sLock")
  private static final HashMap<Locale, DateKeyListener> sInstanceCache = new HashMap();
  private static final Object sLock;
  private final char[] mCharacters;
  private final boolean mNeedsAdvancedInput;
  
  static
  {
    CHARACTERS = new char[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 47, 45, 46 };
    sLock = new Object();
  }
  
  @Deprecated
  public DateKeyListener()
  {
    this(null);
  }
  
  public DateKeyListener(Locale paramLocale)
  {
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    int i;
    if ((NumberKeyListener.addDigits(localLinkedHashSet, paramLocale)) && (NumberKeyListener.addFormatCharsFromSkeletons(localLinkedHashSet, paramLocale, SKELETONS, "yMLd"))) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      mCharacters = NumberKeyListener.collectionToArray(localLinkedHashSet);
      mNeedsAdvancedInput = (ArrayUtils.containsAll(CHARACTERS, mCharacters) ^ true);
    }
    else
    {
      mCharacters = CHARACTERS;
      mNeedsAdvancedInput = false;
    }
  }
  
  @Deprecated
  public static DateKeyListener getInstance()
  {
    return getInstance(null);
  }
  
  public static DateKeyListener getInstance(Locale paramLocale)
  {
    synchronized (sLock)
    {
      DateKeyListener localDateKeyListener1 = (DateKeyListener)sInstanceCache.get(paramLocale);
      DateKeyListener localDateKeyListener2 = localDateKeyListener1;
      if (localDateKeyListener1 == null)
      {
        localDateKeyListener2 = new android/text/method/DateKeyListener;
        localDateKeyListener2.<init>(paramLocale);
        sInstanceCache.put(paramLocale, localDateKeyListener2);
      }
      return localDateKeyListener2;
    }
  }
  
  protected char[] getAcceptedChars()
  {
    return mCharacters;
  }
  
  public int getInputType()
  {
    if (mNeedsAdvancedInput) {
      return 1;
    }
    return 20;
  }
}
