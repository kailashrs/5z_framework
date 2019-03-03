package android.text;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.io.PrintStream;
import libcore.util.EmptyArray;

class PackedObjectVector<E>
{
  private int mColumns;
  private int mRowGapLength;
  private int mRowGapStart;
  private int mRows;
  private Object[] mValues;
  
  public PackedObjectVector(int paramInt)
  {
    mColumns = paramInt;
    mValues = EmptyArray.OBJECT;
    mRows = 0;
    mRowGapStart = 0;
    mRowGapLength = mRows;
  }
  
  private void growBuffer()
  {
    Object[] arrayOfObject = ArrayUtils.newUnpaddedObjectArray(GrowingArrayUtils.growSize(size()) * mColumns);
    int i = arrayOfObject.length / mColumns;
    int j = mRows - (mRowGapStart + mRowGapLength);
    System.arraycopy(mValues, 0, arrayOfObject, 0, mColumns * mRowGapStart);
    System.arraycopy(mValues, (mRows - j) * mColumns, arrayOfObject, (i - j) * mColumns, mColumns * j);
    mRowGapLength += i - mRows;
    mRows = i;
    mValues = arrayOfObject;
  }
  
  private void moveRowGapTo(int paramInt)
  {
    if (paramInt == mRowGapStart) {
      return;
    }
    int i;
    int j;
    int k;
    int m;
    int i3;
    Object localObject;
    if (paramInt > mRowGapStart)
    {
      i = mRowGapLength;
      j = mRowGapStart;
      k = mRowGapLength;
      for (m = mRowGapStart + mRowGapLength; m < mRowGapStart + mRowGapLength + (i + paramInt - (j + k)); m++)
      {
        int n = mRowGapStart;
        int i1 = mRowGapLength;
        int i2 = mRowGapStart;
        for (i3 = 0; i3 < mColumns; i3++)
        {
          localObject = mValues[(mColumns * m + i3)];
          mValues[(mColumns * (m - (n + i1) + i2) + i3)] = localObject;
        }
      }
    }
    else
    {
      k = mRowGapStart - paramInt;
      for (m = paramInt + k - 1; m >= paramInt; m--)
      {
        j = mRowGapStart;
        i = mRowGapLength;
        for (i3 = 0; i3 < mColumns; i3++)
        {
          localObject = mValues[(mColumns * m + i3)];
          mValues[(mColumns * (m - paramInt + j + i - k) + i3)] = localObject;
        }
      }
    }
    mRowGapStart = paramInt;
  }
  
  public void deleteAt(int paramInt1, int paramInt2)
  {
    moveRowGapTo(paramInt1 + paramInt2);
    mRowGapStart -= paramInt2;
    mRowGapLength += paramInt2;
    paramInt1 = mRowGapLength;
    size();
  }
  
  public void dump()
  {
    for (int i = 0; i < mRows; i++)
    {
      for (int j = 0; j < mColumns; j++)
      {
        Object localObject = mValues[(mColumns * i + j)];
        PrintStream localPrintStream;
        StringBuilder localStringBuilder;
        if ((i >= mRowGapStart) && (i < mRowGapStart + mRowGapLength))
        {
          localPrintStream = System.out;
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("(");
          localStringBuilder.append(localObject);
          localStringBuilder.append(") ");
          localPrintStream.print(localStringBuilder.toString());
        }
        else
        {
          localPrintStream = System.out;
          localStringBuilder = new StringBuilder();
          localStringBuilder.append(localObject);
          localStringBuilder.append(" ");
          localPrintStream.print(localStringBuilder.toString());
        }
      }
      System.out.print(" << \n");
    }
    System.out.print("-----\n\n");
  }
  
  public E getValue(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    if (paramInt1 >= mRowGapStart) {
      i = paramInt1 + mRowGapLength;
    }
    return mValues[(mColumns * i + paramInt2)];
  }
  
  public void insertAt(int paramInt, E[] paramArrayOfE)
  {
    moveRowGapTo(paramInt);
    if (mRowGapLength == 0) {
      growBuffer();
    }
    mRowGapStart += 1;
    mRowGapLength -= 1;
    int i = 0;
    int j = 0;
    if (paramArrayOfE == null) {
      while (j < mColumns)
      {
        setValue(paramInt, j, null);
        j++;
      }
    }
    for (j = i; j < mColumns; j++) {
      setValue(paramInt, j, paramArrayOfE[j]);
    }
  }
  
  public void setValue(int paramInt1, int paramInt2, E paramE)
  {
    int i = paramInt1;
    if (paramInt1 >= mRowGapStart) {
      i = paramInt1 + mRowGapLength;
    }
    mValues[(mColumns * i + paramInt2)] = paramE;
  }
  
  public int size()
  {
    return mRows - mRowGapLength;
  }
  
  public int width()
  {
    return mColumns;
  }
}
