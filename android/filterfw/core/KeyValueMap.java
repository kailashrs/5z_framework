package android.filterfw.core;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class KeyValueMap
  extends HashMap<String, Object>
{
  public KeyValueMap() {}
  
  public static KeyValueMap fromKeyValues(Object... paramVarArgs)
  {
    KeyValueMap localKeyValueMap = new KeyValueMap();
    localKeyValueMap.setKeyValues(paramVarArgs);
    return localKeyValueMap;
  }
  
  public float getFloat(String paramString)
  {
    paramString = get(paramString);
    if (paramString != null) {
      paramString = (Float)paramString;
    } else {
      paramString = null;
    }
    return paramString.floatValue();
  }
  
  public int getInt(String paramString)
  {
    paramString = get(paramString);
    if (paramString != null) {
      paramString = (Integer)paramString;
    } else {
      paramString = null;
    }
    return paramString.intValue();
  }
  
  public String getString(String paramString)
  {
    paramString = get(paramString);
    if (paramString != null) {
      paramString = (String)paramString;
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public void setKeyValues(Object... paramVarArgs)
  {
    if (paramVarArgs.length % 2 == 0)
    {
      int i = 0;
      while (i < paramVarArgs.length) {
        if ((paramVarArgs[i] instanceof String))
        {
          put((String)paramVarArgs[i], paramVarArgs[(i + 1)]);
          i += 2;
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Key-value argument ");
          localStringBuilder.append(i);
          localStringBuilder.append(" must be a key of type String, but found an object of type ");
          localStringBuilder.append(paramVarArgs[i].getClass());
          localStringBuilder.append("!");
          throw new RuntimeException(localStringBuilder.toString());
        }
      }
      return;
    }
    throw new RuntimeException("Key-Value arguments passed into setKeyValues must be an alternating list of keys and values!");
  }
  
  public String toString()
  {
    StringWriter localStringWriter = new StringWriter();
    Iterator localIterator = entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getValue();
      Object localObject2;
      if ((localObject1 instanceof String))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("\"");
        ((StringBuilder)localObject2).append(localObject1);
        ((StringBuilder)localObject2).append("\"");
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      else
      {
        localObject2 = localObject1.toString();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localEntry.getKey());
      ((StringBuilder)localObject1).append(" = ");
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append(";\n");
      localStringWriter.write(((StringBuilder)localObject1).toString());
    }
    return localStringWriter.toString();
  }
}
