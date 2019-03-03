package android.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BackupUtils
{
  public static final int NOT_NULL = 1;
  public static final int NULL = 0;
  
  public BackupUtils() {}
  
  public static String readString(DataInputStream paramDataInputStream)
    throws IOException
  {
    if (paramDataInputStream.readByte() == 1) {
      paramDataInputStream = paramDataInputStream.readUTF();
    } else {
      paramDataInputStream = null;
    }
    return paramDataInputStream;
  }
  
  public static void writeString(DataOutputStream paramDataOutputStream, String paramString)
    throws IOException
  {
    if (paramString != null)
    {
      paramDataOutputStream.writeByte(1);
      paramDataOutputStream.writeUTF(paramString);
    }
    else
    {
      paramDataOutputStream.writeByte(0);
    }
  }
  
  public static class BadVersionException
    extends Exception
  {
    public BadVersionException(String paramString)
    {
      super();
    }
  }
}
