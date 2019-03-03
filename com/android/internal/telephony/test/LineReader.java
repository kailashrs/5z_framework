package com.android.internal.telephony.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

class LineReader
{
  static final int BUFFER_SIZE = 4096;
  byte[] mBuffer = new byte['က'];
  InputStream mInStream;
  
  LineReader(InputStream paramInputStream)
  {
    mInStream = paramInputStream;
  }
  
  String getNextLine()
  {
    return getNextLine(false);
  }
  
  String getNextLine(boolean paramBoolean)
  {
    int i = 0;
    try
    {
      try
      {
        for (;;)
        {
          int j = mInStream.read();
          if (j < 0) {
            return null;
          }
          if ((paramBoolean) && (j == 26)) {
            break;
          }
          if ((j != 13) && (j != 10))
          {
            byte[] arrayOfByte = mBuffer;
            j = (byte)j;
            arrayOfByte[i] = ((byte)j);
            i++;
          }
          else
          {
            if (i != 0) {
              break;
            }
          }
        }
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        System.err.println("ATChannel: buffer overflow");
      }
      try
      {
        String str = new String(mBuffer, 0, i, "US-ASCII");
        return str;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        System.err.println("ATChannel: implausable UnsupportedEncodingException");
        return null;
      }
      return null;
    }
    catch (IOException localIOException) {}
  }
  
  String getNextLineCtrlZ()
  {
    return getNextLine(true);
  }
}
