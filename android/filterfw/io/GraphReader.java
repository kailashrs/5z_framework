package android.filterfw.io;

import android.content.Context;
import android.content.res.Resources;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.KeyValueMap;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public abstract class GraphReader
{
  protected KeyValueMap mReferences = new KeyValueMap();
  
  public GraphReader() {}
  
  public void addReference(String paramString, Object paramObject)
  {
    mReferences.put(paramString, paramObject);
  }
  
  public void addReferencesByKeysAndValues(Object... paramVarArgs)
  {
    mReferences.setKeyValues(paramVarArgs);
  }
  
  public void addReferencesByMap(KeyValueMap paramKeyValueMap)
  {
    mReferences.putAll(paramKeyValueMap);
  }
  
  public FilterGraph readGraphResource(Context paramContext, int paramInt)
    throws GraphIOException
  {
    InputStreamReader localInputStreamReader = new InputStreamReader(paramContext.getResources().openRawResource(paramInt));
    paramContext = new StringWriter();
    char[] arrayOfChar = new char['Ѐ'];
    try
    {
      for (;;)
      {
        paramInt = localInputStreamReader.read(arrayOfChar, 0, 1024);
        if (paramInt <= 0) {
          break;
        }
        paramContext.write(arrayOfChar, 0, paramInt);
      }
      return readGraphString(paramContext.toString());
    }
    catch (IOException paramContext)
    {
      throw new RuntimeException("Could not read specified resource file!");
    }
  }
  
  public abstract FilterGraph readGraphString(String paramString)
    throws GraphIOException;
  
  public abstract KeyValueMap readKeyValueAssignments(String paramString)
    throws GraphIOException;
}
