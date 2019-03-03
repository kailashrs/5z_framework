package android.util;

import java.io.BufferedReader;
import java.io.IOException;

@Deprecated
public class EventLogTags
{
  public EventLogTags()
    throws IOException
  {}
  
  public EventLogTags(BufferedReader paramBufferedReader)
    throws IOException
  {}
  
  public Description get(int paramInt)
  {
    return null;
  }
  
  public Description get(String paramString)
  {
    return null;
  }
  
  public static class Description
  {
    public final String mName;
    public final int mTag;
    
    Description(int paramInt, String paramString)
    {
      mTag = paramInt;
      mName = paramString;
    }
  }
}
