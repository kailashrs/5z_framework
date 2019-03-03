package android.view;

import android.content.res.Configuration;
import android.os.LocaleList;
import java.text.BreakIterator;
import java.util.Locale;

public final class AccessibilityIterators
{
  public AccessibilityIterators() {}
  
  public static abstract class AbstractTextSegmentIterator
    implements AccessibilityIterators.TextSegmentIterator
  {
    private final int[] mSegment = new int[2];
    protected String mText;
    
    public AbstractTextSegmentIterator() {}
    
    protected int[] getRange(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 != paramInt2))
      {
        mSegment[0] = paramInt1;
        mSegment[1] = paramInt2;
        return mSegment;
      }
      return null;
    }
    
    public void initialize(String paramString)
    {
      mText = paramString;
    }
  }
  
  static class CharacterTextSegmentIterator
    extends AccessibilityIterators.AbstractTextSegmentIterator
    implements ViewRootImpl.ConfigChangedCallback
  {
    private static CharacterTextSegmentIterator sInstance;
    protected BreakIterator mImpl;
    private Locale mLocale;
    
    private CharacterTextSegmentIterator(Locale paramLocale)
    {
      mLocale = paramLocale;
      onLocaleChanged(paramLocale);
      ViewRootImpl.addConfigCallback(this);
    }
    
    public static CharacterTextSegmentIterator getInstance(Locale paramLocale)
    {
      if (sInstance == null) {
        sInstance = new CharacterTextSegmentIterator(paramLocale);
      }
      return sInstance;
    }
    
    public int[] following(int paramInt)
    {
      int i = mText.length();
      if (i <= 0) {
        return null;
      }
      if (paramInt >= i) {
        return null;
      }
      i = paramInt;
      paramInt = i;
      if (i < 0) {
        paramInt = 0;
      }
      while (!mImpl.isBoundary(paramInt))
      {
        i = mImpl.following(paramInt);
        paramInt = i;
        if (i == -1) {
          return null;
        }
      }
      i = mImpl.following(paramInt);
      if (i == -1) {
        return null;
      }
      return getRange(paramInt, i);
    }
    
    public void initialize(String paramString)
    {
      super.initialize(paramString);
      mImpl.setText(paramString);
    }
    
    public void onConfigurationChanged(Configuration paramConfiguration)
    {
      paramConfiguration = paramConfiguration.getLocales().get(0);
      if (!mLocale.equals(paramConfiguration))
      {
        mLocale = paramConfiguration;
        onLocaleChanged(paramConfiguration);
      }
    }
    
    protected void onLocaleChanged(Locale paramLocale)
    {
      mImpl = BreakIterator.getCharacterInstance(paramLocale);
    }
    
    public int[] preceding(int paramInt)
    {
      int i = mText.length();
      if (i <= 0) {
        return null;
      }
      if (paramInt <= 0) {
        return null;
      }
      int j = paramInt;
      paramInt = j;
      if (j > i) {
        paramInt = i;
      }
      while (!mImpl.isBoundary(paramInt))
      {
        j = mImpl.preceding(paramInt);
        paramInt = j;
        if (j == -1) {
          return null;
        }
      }
      j = mImpl.preceding(paramInt);
      if (j == -1) {
        return null;
      }
      return getRange(j, paramInt);
    }
  }
  
  static class ParagraphTextSegmentIterator
    extends AccessibilityIterators.AbstractTextSegmentIterator
  {
    private static ParagraphTextSegmentIterator sInstance;
    
    ParagraphTextSegmentIterator() {}
    
    public static ParagraphTextSegmentIterator getInstance()
    {
      if (sInstance == null) {
        sInstance = new ParagraphTextSegmentIterator();
      }
      return sInstance;
    }
    
    private boolean isEndBoundary(int paramInt)
    {
      boolean bool;
      if ((paramInt > 0) && (mText.charAt(paramInt - 1) != '\n') && ((paramInt == mText.length()) || (mText.charAt(paramInt) == '\n'))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isStartBoundary(int paramInt)
    {
      boolean bool;
      if ((mText.charAt(paramInt) != '\n') && ((paramInt == 0) || (mText.charAt(paramInt - 1) == '\n'))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int[] following(int paramInt)
    {
      int i = mText.length();
      if (i <= 0) {
        return null;
      }
      if (paramInt >= i) {
        return null;
      }
      int j = paramInt;
      paramInt = j;
      if (j < 0) {}
      for (paramInt = 0; (paramInt < i) && (mText.charAt(paramInt) == '\n') && (!isStartBoundary(paramInt)); paramInt++) {}
      if (paramInt >= i) {
        return null;
      }
      for (j = paramInt + 1; (j < i) && (!isEndBoundary(j)); j++) {}
      return getRange(paramInt, j);
    }
    
    public int[] preceding(int paramInt)
    {
      int i = mText.length();
      if (i <= 0) {
        return null;
      }
      if (paramInt <= 0) {
        return null;
      }
      int j = paramInt;
      paramInt = j;
      if (j > i) {}
      for (paramInt = i; (paramInt > 0) && (mText.charAt(paramInt - 1) == '\n') && (!isEndBoundary(paramInt)); paramInt--) {}
      if (paramInt <= 0) {
        return null;
      }
      for (j = paramInt - 1; (j > 0) && (!isStartBoundary(j)); j--) {}
      return getRange(j, paramInt);
    }
  }
  
  public static abstract interface TextSegmentIterator
  {
    public abstract int[] following(int paramInt);
    
    public abstract int[] preceding(int paramInt);
  }
  
  static class WordTextSegmentIterator
    extends AccessibilityIterators.CharacterTextSegmentIterator
  {
    private static WordTextSegmentIterator sInstance;
    
    private WordTextSegmentIterator(Locale paramLocale)
    {
      super(null);
    }
    
    public static WordTextSegmentIterator getInstance(Locale paramLocale)
    {
      if (sInstance == null) {
        sInstance = new WordTextSegmentIterator(paramLocale);
      }
      return sInstance;
    }
    
    private boolean isEndBoundary(int paramInt)
    {
      boolean bool;
      if ((paramInt > 0) && (isLetterOrDigit(paramInt - 1)) && ((paramInt == mText.length()) || (!isLetterOrDigit(paramInt)))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isLetterOrDigit(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < mText.length())) {
        return Character.isLetterOrDigit(mText.codePointAt(paramInt));
      }
      return false;
    }
    
    private boolean isStartBoundary(int paramInt)
    {
      boolean bool;
      if ((isLetterOrDigit(paramInt)) && ((paramInt == 0) || (!isLetterOrDigit(paramInt - 1)))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int[] following(int paramInt)
    {
      if (mText.length() <= 0) {
        return null;
      }
      if (paramInt >= mText.length()) {
        return null;
      }
      int i = paramInt;
      paramInt = i;
      if (i < 0) {
        paramInt = 0;
      }
      while ((!isLetterOrDigit(paramInt)) && (!isStartBoundary(paramInt)))
      {
        i = mImpl.following(paramInt);
        paramInt = i;
        if (i == -1) {
          return null;
        }
      }
      i = mImpl.following(paramInt);
      if ((i != -1) && (isEndBoundary(i))) {
        return getRange(paramInt, i);
      }
      return null;
    }
    
    protected void onLocaleChanged(Locale paramLocale)
    {
      mImpl = BreakIterator.getWordInstance(paramLocale);
    }
    
    public int[] preceding(int paramInt)
    {
      int i = mText.length();
      if (i <= 0) {
        return null;
      }
      if (paramInt <= 0) {
        return null;
      }
      int j = paramInt;
      paramInt = j;
      if (j > i) {
        paramInt = i;
      }
      while ((paramInt > 0) && (!isLetterOrDigit(paramInt - 1)) && (!isEndBoundary(paramInt)))
      {
        j = mImpl.preceding(paramInt);
        paramInt = j;
        if (j == -1) {
          return null;
        }
      }
      j = mImpl.preceding(paramInt);
      if ((j != -1) && (isStartBoundary(j))) {
        return getRange(j, paramInt);
      }
      return null;
    }
  }
}
