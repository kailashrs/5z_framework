package com.android.internal.app.procstats;

public class DurationsTable
  extends SparseMappingTable.Table
{
  public DurationsTable(SparseMappingTable paramSparseMappingTable)
  {
    super(paramSparseMappingTable);
  }
  
  public void addDuration(int paramInt, long paramLong)
  {
    paramInt = getOrAddKey((byte)paramInt, 1);
    setValue(paramInt, getValue(paramInt) + paramLong);
  }
  
  public void addDurations(DurationsTable paramDurationsTable)
  {
    int i = paramDurationsTable.getKeyCount();
    for (int j = 0; j < i; j++)
    {
      int k = paramDurationsTable.getKeyAt(j);
      addDuration(SparseMappingTable.getIdFromKey(k), paramDurationsTable.getValue(k));
    }
  }
}
