package com.android.internal.app.procstats;

public class PssTable
  extends SparseMappingTable.Table
{
  public PssTable(SparseMappingTable paramSparseMappingTable)
  {
    super(paramSparseMappingTable);
  }
  
  public void mergeStats(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long paramLong7, long paramLong8, long paramLong9)
  {
    paramInt1 = getOrAddKey((byte)paramInt1, 10);
    long l = getValue(paramInt1, 0);
    if (l == 0L)
    {
      setValue(paramInt1, 0, paramInt2);
      setValue(paramInt1, 1, paramLong1);
      setValue(paramInt1, 2, paramLong2);
      setValue(paramInt1, 3, paramLong3);
      setValue(paramInt1, 4, paramLong4);
      setValue(paramInt1, 5, paramLong5);
      setValue(paramInt1, 6, paramLong6);
      setValue(paramInt1, 7, paramLong7);
      setValue(paramInt1, 8, paramLong8);
      setValue(paramInt1, 9, paramLong9);
    }
    else
    {
      setValue(paramInt1, 0, paramInt2 + l);
      if (getValue(paramInt1, 1) > paramLong1) {
        setValue(paramInt1, 1, paramLong1);
      }
      setValue(paramInt1, 2, ((getValue(paramInt1, 2) * l + paramLong2 * paramInt2) / (paramInt2 + l)));
      if (getValue(paramInt1, 3) < paramLong3) {
        setValue(paramInt1, 3, paramLong3);
      }
      if (getValue(paramInt1, 4) > paramLong4) {
        setValue(paramInt1, 4, paramLong4);
      }
      setValue(paramInt1, 5, ((getValue(paramInt1, 5) * l + paramLong5 * paramInt2) / (paramInt2 + l)));
      if (getValue(paramInt1, 6) < paramLong6) {
        setValue(paramInt1, 6, paramLong6);
      }
      if (getValue(paramInt1, 7) > paramLong4) {
        setValue(paramInt1, 7, paramLong4);
      }
      setValue(paramInt1, 8, ((getValue(paramInt1, 8) * l + paramLong5 * paramInt2) / (paramInt2 + l)));
      if (getValue(paramInt1, 9) < paramLong6) {
        setValue(paramInt1, 9, paramLong6);
      }
    }
  }
  
  public void mergeStats(PssTable paramPssTable)
  {
    int i = paramPssTable.getKeyCount();
    for (int j = 0; j < i; j++)
    {
      int k = paramPssTable.getKeyAt(j);
      mergeStats(SparseMappingTable.getIdFromKey(k), (int)paramPssTable.getValue(k, 0), paramPssTable.getValue(k, 1), paramPssTable.getValue(k, 2), paramPssTable.getValue(k, 3), paramPssTable.getValue(k, 4), paramPssTable.getValue(k, 5), paramPssTable.getValue(k, 6), paramPssTable.getValue(k, 7), paramPssTable.getValue(k, 8), paramPssTable.getValue(k, 9));
    }
  }
}
