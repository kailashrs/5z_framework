package android.text;

import java.text.CharacterIterator;

public class CharSequenceCharacterIterator
  implements CharacterIterator
{
  private final int mBeginIndex;
  private final CharSequence mCharSeq;
  private final int mEndIndex;
  private int mIndex;
  
  public CharSequenceCharacterIterator(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    mCharSeq = paramCharSequence;
    mIndex = paramInt1;
    mBeginIndex = paramInt1;
    mEndIndex = paramInt2;
  }
  
  public Object clone()
  {
    try
    {
      Object localObject = super.clone();
      return localObject;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new InternalError();
    }
  }
  
  public char current()
  {
    int i;
    int j;
    if (mIndex == mEndIndex)
    {
      i = 65535;
      j = i;
    }
    else
    {
      i = mCharSeq.charAt(mIndex);
      j = i;
    }
    return j;
  }
  
  public char first()
  {
    mIndex = mBeginIndex;
    return current();
  }
  
  public int getBeginIndex()
  {
    return mBeginIndex;
  }
  
  public int getEndIndex()
  {
    return mEndIndex;
  }
  
  public int getIndex()
  {
    return mIndex;
  }
  
  public char last()
  {
    if (mBeginIndex == mEndIndex)
    {
      mIndex = mEndIndex;
      return 65535;
    }
    mIndex = (mEndIndex - 1);
    return mCharSeq.charAt(mIndex);
  }
  
  public char next()
  {
    mIndex += 1;
    if (mIndex >= mEndIndex)
    {
      mIndex = mEndIndex;
      return 65535;
    }
    return mCharSeq.charAt(mIndex);
  }
  
  public char previous()
  {
    if (mIndex <= mBeginIndex) {
      return 65535;
    }
    mIndex -= 1;
    return mCharSeq.charAt(mIndex);
  }
  
  public char setIndex(int paramInt)
  {
    if ((mBeginIndex <= paramInt) && (paramInt <= mEndIndex))
    {
      mIndex = paramInt;
      return current();
    }
    throw new IllegalArgumentException("invalid position");
  }
}
