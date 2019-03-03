package android.widget;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.SparseIntArray;
import java.text.Collator;

public class AlphabetIndexer
  extends DataSetObserver
  implements SectionIndexer
{
  private SparseIntArray mAlphaMap;
  protected CharSequence mAlphabet;
  private String[] mAlphabetArray;
  private int mAlphabetLength;
  private Collator mCollator;
  protected int mColumnIndex;
  protected Cursor mDataCursor;
  
  public AlphabetIndexer(Cursor paramCursor, int paramInt, CharSequence paramCharSequence)
  {
    mDataCursor = paramCursor;
    mColumnIndex = paramInt;
    mAlphabet = paramCharSequence;
    mAlphabetLength = paramCharSequence.length();
    mAlphabetArray = new String[mAlphabetLength];
    for (paramInt = 0; paramInt < mAlphabetLength; paramInt++) {
      mAlphabetArray[paramInt] = Character.toString(mAlphabet.charAt(paramInt));
    }
    mAlphaMap = new SparseIntArray(mAlphabetLength);
    if (paramCursor != null) {
      paramCursor.registerDataSetObserver(this);
    }
    mCollator = Collator.getInstance();
    mCollator.setStrength(0);
  }
  
  protected int compare(String paramString1, String paramString2)
  {
    if (paramString1.length() == 0) {
      paramString1 = " ";
    } else {
      paramString1 = paramString1.substring(0, 1);
    }
    return mCollator.compare(paramString1, paramString2);
  }
  
  public int getPositionForSection(int paramInt)
  {
    SparseIntArray localSparseIntArray = mAlphaMap;
    Cursor localCursor = mDataCursor;
    if ((localCursor != null) && (mAlphabet != null))
    {
      if (paramInt <= 0) {
        return 0;
      }
      int i = paramInt;
      if (paramInt >= mAlphabetLength) {
        i = mAlphabetLength - 1;
      }
      int j = localCursor.getPosition();
      int k = localCursor.getCount();
      paramInt = 0;
      int m = k;
      char c = mAlphabet.charAt(i);
      String str1 = Character.toString(c);
      int n = localSparseIntArray.get(c, Integer.MIN_VALUE);
      if (Integer.MIN_VALUE != n) {
        if (n < 0) {
          m = -n;
        } else {
          return n;
        }
      }
      n = paramInt;
      if (i > 0)
      {
        i = localSparseIntArray.get(mAlphabet.charAt(i - 1), Integer.MIN_VALUE);
        n = paramInt;
        if (i != Integer.MIN_VALUE) {
          n = Math.abs(i);
        }
      }
      paramInt = (m + n) / 2;
      for (;;)
      {
        i = paramInt;
        if (paramInt >= m) {
          break;
        }
        localCursor.moveToPosition(paramInt);
        String str2 = localCursor.getString(mColumnIndex);
        if (str2 == null)
        {
          if (paramInt == 0)
          {
            i = paramInt;
            break;
          }
          paramInt--;
        }
        else
        {
          i = compare(str2, str1);
          if (i != 0)
          {
            if (i < 0)
            {
              i = paramInt + 1;
              n = i;
              paramInt = m;
              if (i < k) {
                break label289;
              }
              i = k;
              break;
            }
          }
          else if (n == paramInt)
          {
            i = paramInt;
            break;
          }
          label289:
          i = (n + paramInt) / 2;
          m = paramInt;
          paramInt = i;
        }
      }
      localSparseIntArray.put(c, i);
      localCursor.moveToPosition(j);
      return i;
    }
    return 0;
  }
  
  public int getSectionForPosition(int paramInt)
  {
    int i = mDataCursor.getPosition();
    mDataCursor.moveToPosition(paramInt);
    String str = mDataCursor.getString(mColumnIndex);
    mDataCursor.moveToPosition(i);
    for (paramInt = 0; paramInt < mAlphabetLength; paramInt++) {
      if (compare(str, Character.toString(mAlphabet.charAt(paramInt))) == 0) {
        return paramInt;
      }
    }
    return 0;
  }
  
  public Object[] getSections()
  {
    return mAlphabetArray;
  }
  
  public void onChanged()
  {
    super.onChanged();
    mAlphaMap.clear();
  }
  
  public void onInvalidated()
  {
    super.onInvalidated();
    mAlphaMap.clear();
  }
  
  public void setCursor(Cursor paramCursor)
  {
    if (mDataCursor != null) {
      mDataCursor.unregisterDataSetObserver(this);
    }
    mDataCursor = paramCursor;
    if (paramCursor != null) {
      mDataCursor.registerDataSetObserver(this);
    }
    mAlphaMap.clear();
  }
}
