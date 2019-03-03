package com.google.android.mms.pdu;

import java.io.ByteArrayOutputStream;

public class QuotedPrintable
{
  private static byte ESCAPE_CHAR = (byte)61;
  
  public QuotedPrintable() {}
  
  public static final byte[] decodeQuotedPrintable(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      int j = paramArrayOfByte[i];
      if (j == ESCAPE_CHAR)
      {
        if (('\r' == (char)paramArrayOfByte[(i + 1)]) && ('\n' == (char)paramArrayOfByte[(i + 2)]))
        {
          i += 2;
        }
        else
        {
          i++;
          try
          {
            j = Character.digit((char)paramArrayOfByte[i], 16);
            i++;
            int k = Character.digit((char)paramArrayOfByte[i], 16);
            if ((j != -1) && (k != -1))
            {
              localByteArrayOutputStream.write((char)((j << 4) + k));
              continue;
            }
            return null;
          }
          catch (ArrayIndexOutOfBoundsException paramArrayOfByte)
          {
            return null;
          }
        }
      }
      else {
        localByteArrayOutputStream.write(j);
      }
    }
    return localByteArrayOutputStream.toByteArray();
  }
}
