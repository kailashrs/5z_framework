package android.nfc;

import java.io.IOException;

public class TagLostException
  extends IOException
{
  public TagLostException() {}
  
  public TagLostException(String paramString)
  {
    super(paramString);
  }
}
