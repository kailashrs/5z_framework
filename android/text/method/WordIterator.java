package android.text.method;

import android.icu.lang.UCharacter;
import android.icu.text.BreakIterator;
import android.text.CharSequenceCharacterIterator;
import android.text.Selection.PositionIterator;
import java.util.Locale;

public class WordIterator
  implements Selection.PositionIterator
{
  private static final int WINDOW_WIDTH = 50;
  private CharSequence mCharSeq;
  private int mEnd;
  private final BreakIterator mIterator;
  private int mStart;
  
  public WordIterator()
  {
    this(Locale.getDefault());
  }
  
  public WordIterator(Locale paramLocale)
  {
    mIterator = BreakIterator.getWordInstance(paramLocale);
  }
  
  private void checkOffsetIsValid(int paramInt)
  {
    if ((mStart <= paramInt) && (paramInt <= mEnd)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid offset: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(". Valid range is [");
    localStringBuilder.append(mStart);
    localStringBuilder.append(", ");
    localStringBuilder.append(mEnd);
    localStringBuilder.append("]");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private int getBeginning(int paramInt, boolean paramBoolean)
  {
    checkOffsetIsValid(paramInt);
    if (isOnLetterOrDigit(paramInt))
    {
      if ((mIterator.isBoundary(paramInt)) && ((!isAfterLetterOrDigit(paramInt)) || (!paramBoolean))) {
        return paramInt;
      }
      return mIterator.preceding(paramInt);
    }
    if (isAfterLetterOrDigit(paramInt)) {
      return mIterator.preceding(paramInt);
    }
    return -1;
  }
  
  private int getEnd(int paramInt, boolean paramBoolean)
  {
    checkOffsetIsValid(paramInt);
    if (isAfterLetterOrDigit(paramInt))
    {
      if ((mIterator.isBoundary(paramInt)) && ((!isOnLetterOrDigit(paramInt)) || (!paramBoolean))) {
        return paramInt;
      }
      return mIterator.following(paramInt);
    }
    if (isOnLetterOrDigit(paramInt)) {
      return mIterator.following(paramInt);
    }
    return -1;
  }
  
  private boolean isAfterLetterOrDigit(int paramInt)
  {
    return (mStart < paramInt) && (paramInt <= mEnd) && (Character.isLetterOrDigit(Character.codePointBefore(mCharSeq, paramInt)));
  }
  
  public static boolean isMidWordPunctuation(Locale paramLocale, int paramInt)
  {
    paramInt = UCharacter.getIntPropertyValue(paramInt, 4116);
    boolean bool;
    if ((paramInt != 4) && (paramInt != 11) && (paramInt != 15)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isOnLetterOrDigit(int paramInt)
  {
    return (mStart <= paramInt) && (paramInt < mEnd) && (Character.isLetterOrDigit(Character.codePointAt(mCharSeq, paramInt)));
  }
  
  private static boolean isPunctuation(int paramInt)
  {
    paramInt = Character.getType(paramInt);
    boolean bool;
    if ((paramInt != 23) && (paramInt != 20) && (paramInt != 22) && (paramInt != 30) && (paramInt != 29) && (paramInt != 24) && (paramInt != 21)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isPunctuationEndBoundary(int paramInt)
  {
    boolean bool;
    if ((!isOnPunctuation(paramInt)) && (isAfterPunctuation(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isPunctuationStartBoundary(int paramInt)
  {
    boolean bool;
    if ((isOnPunctuation(paramInt)) && (!isAfterPunctuation(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int following(int paramInt)
  {
    checkOffsetIsValid(paramInt);
    int i;
    do
    {
      i = mIterator.following(paramInt);
      if (i == -1) {
        break;
      }
      paramInt = i;
    } while (!isAfterLetterOrDigit(i));
    return i;
  }
  
  public int getBeginning(int paramInt)
  {
    return getBeginning(paramInt, false);
  }
  
  public int getEnd(int paramInt)
  {
    return getEnd(paramInt, false);
  }
  
  public int getNextWordEndOnTwoWordBoundary(int paramInt)
  {
    return getEnd(paramInt, true);
  }
  
  public int getPrevWordBeginningOnTwoWordsBoundary(int paramInt)
  {
    return getBeginning(paramInt, true);
  }
  
  public int getPunctuationBeginning(int paramInt)
  {
    checkOffsetIsValid(paramInt);
    while ((paramInt != -1) && (!isPunctuationStartBoundary(paramInt))) {
      paramInt = prevBoundary(paramInt);
    }
    return paramInt;
  }
  
  public int getPunctuationEnd(int paramInt)
  {
    checkOffsetIsValid(paramInt);
    while ((paramInt != -1) && (!isPunctuationEndBoundary(paramInt))) {
      paramInt = nextBoundary(paramInt);
    }
    return paramInt;
  }
  
  public boolean isAfterPunctuation(int paramInt)
  {
    if ((mStart < paramInt) && (paramInt <= mEnd)) {
      return isPunctuation(Character.codePointBefore(mCharSeq, paramInt));
    }
    return false;
  }
  
  public boolean isBoundary(int paramInt)
  {
    checkOffsetIsValid(paramInt);
    return mIterator.isBoundary(paramInt);
  }
  
  public boolean isOnPunctuation(int paramInt)
  {
    if ((mStart <= paramInt) && (paramInt < mEnd)) {
      return isPunctuation(Character.codePointAt(mCharSeq, paramInt));
    }
    return false;
  }
  
  public int nextBoundary(int paramInt)
  {
    checkOffsetIsValid(paramInt);
    return mIterator.following(paramInt);
  }
  
  public int preceding(int paramInt)
  {
    checkOffsetIsValid(paramInt);
    int i;
    do
    {
      i = mIterator.preceding(paramInt);
      if (i == -1) {
        break;
      }
      paramInt = i;
    } while (!isOnLetterOrDigit(i));
    return i;
  }
  
  public int prevBoundary(int paramInt)
  {
    checkOffsetIsValid(paramInt);
    return mIterator.preceding(paramInt);
  }
  
  public void setCharSequence(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt2 <= paramCharSequence.length()))
    {
      mCharSeq = paramCharSequence;
      mStart = Math.max(0, paramInt1 - 50);
      mEnd = Math.min(paramCharSequence.length(), paramInt2 + 50);
      mIterator.setText(new CharSequenceCharacterIterator(paramCharSequence, mStart, mEnd));
      return;
    }
    throw new IndexOutOfBoundsException("input indexes are outside the CharSequence");
  }
}
