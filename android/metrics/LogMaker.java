package android.metrics;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;

@SystemApi
public class LogMaker
{
  @VisibleForTesting
  public static final int MAX_SERIALIZED_SIZE = 4000;
  private static final String TAG = "LogBuilder";
  private SparseArray<Object> entries = new SparseArray();
  
  public LogMaker(int paramInt)
  {
    setCategory(paramInt);
  }
  
  public LogMaker(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject != null) {
      deserialize(paramArrayOfObject);
    } else {
      setCategory(0);
    }
  }
  
  public LogMaker addTaggedData(int paramInt, Object paramObject)
  {
    if (paramObject == null) {
      return clearTaggedData(paramInt);
    }
    if (isValidValue(paramObject))
    {
      if (paramObject.toString().getBytes().length > 4000)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Log value too long, omitted: ");
        localStringBuilder.append(paramObject.toString());
        Log.i("LogBuilder", localStringBuilder.toString());
      }
      else
      {
        entries.put(paramInt, paramObject);
      }
      return this;
    }
    throw new IllegalArgumentException("Value must be loggable type - int, long, float, String");
  }
  
  public LogMaker clearCategory()
  {
    entries.remove(757);
    return this;
  }
  
  public LogMaker clearPackageName()
  {
    entries.remove(806);
    return this;
  }
  
  public LogMaker clearProcessId()
  {
    entries.remove(865);
    return this;
  }
  
  public LogMaker clearSubtype()
  {
    entries.remove(759);
    return this;
  }
  
  public LogMaker clearTaggedData(int paramInt)
  {
    entries.delete(paramInt);
    return this;
  }
  
  public LogMaker clearTimestamp()
  {
    entries.remove(805);
    return this;
  }
  
  public LogMaker clearType()
  {
    entries.remove(758);
    return this;
  }
  
  public LogMaker clearUid()
  {
    entries.remove(943);
    return this;
  }
  
  public void deserialize(Object[] paramArrayOfObject)
  {
    int i = 0;
    while ((paramArrayOfObject != null) && (i < paramArrayOfObject.length))
    {
      int j = i + 1;
      Object localObject1 = paramArrayOfObject[i];
      Object localObject2;
      if (j < paramArrayOfObject.length)
      {
        i = j + 1;
        localObject2 = paramArrayOfObject[j];
      }
      else
      {
        i = j;
        localObject2 = null;
      }
      if ((localObject1 instanceof Integer))
      {
        entries.put(((Integer)localObject1).intValue(), localObject2);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid key ");
        if (localObject1 == null) {
          localObject2 = "null";
        } else {
          localObject2 = localObject1.toString();
        }
        localStringBuilder.append((String)localObject2);
        Log.i("LogBuilder", localStringBuilder.toString());
      }
    }
  }
  
  public int getCategory()
  {
    Object localObject = entries.get(757);
    if ((localObject instanceof Integer)) {
      return ((Integer)localObject).intValue();
    }
    return 0;
  }
  
  public long getCounterBucket()
  {
    Object localObject = entries.get(801);
    if ((localObject instanceof Number)) {
      return ((Number)localObject).longValue();
    }
    return 0L;
  }
  
  public String getCounterName()
  {
    Object localObject = entries.get(799);
    if ((localObject instanceof String)) {
      return (String)localObject;
    }
    return null;
  }
  
  public int getCounterValue()
  {
    Object localObject = entries.get(802);
    if ((localObject instanceof Integer)) {
      return ((Integer)localObject).intValue();
    }
    return 0;
  }
  
  public String getPackageName()
  {
    Object localObject = entries.get(806);
    if ((localObject instanceof String)) {
      return (String)localObject;
    }
    return null;
  }
  
  public int getProcessId()
  {
    Object localObject = entries.get(865);
    if ((localObject instanceof Integer)) {
      return ((Integer)localObject).intValue();
    }
    return -1;
  }
  
  public int getSubtype()
  {
    Object localObject = entries.get(759);
    if ((localObject instanceof Integer)) {
      return ((Integer)localObject).intValue();
    }
    return 0;
  }
  
  public Object getTaggedData(int paramInt)
  {
    return entries.get(paramInt);
  }
  
  public long getTimestamp()
  {
    Object localObject = entries.get(805);
    if ((localObject instanceof Long)) {
      return ((Long)localObject).longValue();
    }
    return 0L;
  }
  
  public int getType()
  {
    Object localObject = entries.get(758);
    if ((localObject instanceof Integer)) {
      return ((Integer)localObject).intValue();
    }
    return 0;
  }
  
  public int getUid()
  {
    Object localObject = entries.get(943);
    if ((localObject instanceof Integer)) {
      return ((Integer)localObject).intValue();
    }
    return -1;
  }
  
  public boolean isLongCounterBucket()
  {
    return entries.get(801) instanceof Long;
  }
  
  public boolean isSubsetOf(LogMaker paramLogMaker)
  {
    if (paramLogMaker == null) {
      return false;
    }
    for (int i = 0; i < entries.size(); i++)
    {
      int j = entries.keyAt(i);
      Object localObject1 = entries.valueAt(i);
      Object localObject2 = entries.get(j);
      if (((localObject1 == null) && (localObject2 != null)) || (!localObject1.equals(localObject2))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isValidValue(Object paramObject)
  {
    boolean bool;
    if ((!(paramObject instanceof Integer)) && (!(paramObject instanceof String)) && (!(paramObject instanceof Long)) && (!(paramObject instanceof Float))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public Object[] serialize()
  {
    Object localObject = new Object[entries.size() * 2];
    for (int i = 0; i < entries.size(); i++)
    {
      localObject[(i * 2)] = Integer.valueOf(entries.keyAt(i));
      localObject[(i * 2 + 1)] = entries.valueAt(i);
    }
    i = localObject.toString().getBytes().length;
    if (i <= 4000) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Log line too long, did not emit: ");
    ((StringBuilder)localObject).append(i);
    ((StringBuilder)localObject).append(" bytes.");
    Log.i("LogBuilder", ((StringBuilder)localObject).toString());
    throw new RuntimeException();
  }
  
  public LogMaker setCategory(int paramInt)
  {
    entries.put(757, Integer.valueOf(paramInt));
    return this;
  }
  
  public LogMaker setComponentName(ComponentName paramComponentName)
  {
    entries.put(806, paramComponentName.getPackageName());
    entries.put(871, paramComponentName.getClassName());
    return this;
  }
  
  public LogMaker setCounterBucket(int paramInt)
  {
    entries.put(801, Integer.valueOf(paramInt));
    return this;
  }
  
  public LogMaker setCounterBucket(long paramLong)
  {
    entries.put(801, Long.valueOf(paramLong));
    return this;
  }
  
  public LogMaker setCounterName(String paramString)
  {
    entries.put(799, paramString);
    return this;
  }
  
  public LogMaker setCounterValue(int paramInt)
  {
    entries.put(802, Integer.valueOf(paramInt));
    return this;
  }
  
  public LogMaker setLatency(long paramLong)
  {
    entries.put(1359, Long.valueOf(paramLong));
    return this;
  }
  
  public LogMaker setPackageName(String paramString)
  {
    entries.put(806, paramString);
    return this;
  }
  
  public LogMaker setProcessId(int paramInt)
  {
    entries.put(865, Integer.valueOf(paramInt));
    return this;
  }
  
  public LogMaker setSubtype(int paramInt)
  {
    entries.put(759, Integer.valueOf(paramInt));
    return this;
  }
  
  public LogMaker setTimestamp(long paramLong)
  {
    entries.put(805, Long.valueOf(paramLong));
    return this;
  }
  
  public LogMaker setType(int paramInt)
  {
    entries.put(758, Integer.valueOf(paramInt));
    return this;
  }
  
  public LogMaker setUid(int paramInt)
  {
    entries.put(943, Integer.valueOf(paramInt));
    return this;
  }
}
