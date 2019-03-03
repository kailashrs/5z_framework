package com.android.internal.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;

public class LineBreakBufferedWriter
  extends PrintWriter
{
  private char[] buffer;
  private int bufferIndex;
  private final int bufferSize;
  private int lastNewline = -1;
  private final String lineSeparator;
  
  public LineBreakBufferedWriter(Writer paramWriter, int paramInt)
  {
    this(paramWriter, paramInt, 16);
  }
  
  public LineBreakBufferedWriter(Writer paramWriter, int paramInt1, int paramInt2)
  {
    super(paramWriter);
    buffer = new char[Math.min(paramInt2, paramInt1)];
    bufferIndex = 0;
    bufferSize = paramInt1;
    lineSeparator = System.getProperty("line.separator");
  }
  
  private void appendToBuffer(String paramString, int paramInt1, int paramInt2)
  {
    if (bufferIndex + paramInt2 > buffer.length) {
      ensureCapacity(bufferIndex + paramInt2);
    }
    paramString.getChars(paramInt1, paramInt1 + paramInt2, buffer, bufferIndex);
    bufferIndex += paramInt2;
  }
  
  private void appendToBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (bufferIndex + paramInt2 > buffer.length) {
      ensureCapacity(bufferIndex + paramInt2);
    }
    System.arraycopy(paramArrayOfChar, paramInt1, buffer, bufferIndex, paramInt2);
    bufferIndex += paramInt2;
  }
  
  private void ensureCapacity(int paramInt)
  {
    int i = buffer.length * 2 + 2;
    int j = i;
    if (i < paramInt) {
      j = paramInt;
    }
    buffer = Arrays.copyOf(buffer, j);
  }
  
  private void removeFromBuffer(int paramInt)
  {
    paramInt = bufferIndex - paramInt;
    if (paramInt > 0)
    {
      System.arraycopy(buffer, bufferIndex - paramInt, buffer, 0, paramInt);
      bufferIndex = paramInt;
    }
    else
    {
      bufferIndex = 0;
    }
  }
  
  private void writeBuffer(int paramInt)
  {
    if (paramInt > 0) {
      super.write(buffer, 0, paramInt);
    }
  }
  
  public void flush()
  {
    writeBuffer(bufferIndex);
    bufferIndex = 0;
    super.flush();
  }
  
  public void println()
  {
    write(lineSeparator);
  }
  
  public void write(int paramInt)
  {
    if (bufferIndex < buffer.length)
    {
      buffer[bufferIndex] = ((char)(char)paramInt);
      bufferIndex += 1;
      if ((char)paramInt == '\n') {
        lastNewline = bufferIndex;
      }
    }
    else
    {
      write(new char[] { (char)paramInt }, 0, 1);
    }
  }
  
  public void write(String paramString, int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    paramInt2 = paramInt1;
    while (bufferIndex + i > bufferSize)
    {
      int j = bufferSize;
      int k = bufferIndex;
      int m = -1;
      paramInt1 = 0;
      while (paramInt1 < j - k)
      {
        int n = m;
        if (paramString.charAt(paramInt2 + paramInt1) == '\n')
        {
          if (bufferIndex + paramInt1 >= bufferSize) {
            break;
          }
          n = paramInt1;
        }
        paramInt1++;
        m = n;
      }
      if (m != -1)
      {
        appendToBuffer(paramString, paramInt2, m);
        writeBuffer(bufferIndex);
        bufferIndex = 0;
        lastNewline = -1;
        paramInt2 += m + 1;
        paramInt1 = i - (m + 1);
      }
      else if (lastNewline != -1)
      {
        writeBuffer(lastNewline);
        removeFromBuffer(lastNewline + 1);
        lastNewline = -1;
        paramInt1 = i;
      }
      else
      {
        paramInt1 = bufferSize - bufferIndex;
        appendToBuffer(paramString, paramInt2, paramInt1);
        writeBuffer(bufferIndex);
        bufferIndex = 0;
        paramInt2 += paramInt1;
        paramInt1 = i - paramInt1;
      }
      i = paramInt1;
    }
    if (i > 0)
    {
      appendToBuffer(paramString, paramInt2, i);
      for (paramInt1 = i - 1; paramInt1 >= 0; paramInt1--) {
        if (paramString.charAt(paramInt2 + paramInt1) == '\n')
        {
          lastNewline = (bufferIndex - i + paramInt1);
          break;
        }
      }
    }
  }
  
  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    paramInt2 = paramInt1;
    while (bufferIndex + i > bufferSize)
    {
      int j = bufferSize;
      int k = bufferIndex;
      int m = -1;
      paramInt1 = 0;
      while (paramInt1 < j - k)
      {
        int n = m;
        if (paramArrayOfChar[(paramInt2 + paramInt1)] == '\n')
        {
          if (bufferIndex + paramInt1 >= bufferSize) {
            break;
          }
          n = paramInt1;
        }
        paramInt1++;
        m = n;
      }
      if (m != -1)
      {
        appendToBuffer(paramArrayOfChar, paramInt2, m);
        writeBuffer(bufferIndex);
        bufferIndex = 0;
        lastNewline = -1;
        paramInt2 += m + 1;
        paramInt1 = i - (m + 1);
      }
      else if (lastNewline != -1)
      {
        writeBuffer(lastNewline);
        removeFromBuffer(lastNewline + 1);
        lastNewline = -1;
        paramInt1 = i;
      }
      else
      {
        paramInt1 = bufferSize - bufferIndex;
        appendToBuffer(paramArrayOfChar, paramInt2, paramInt1);
        writeBuffer(bufferIndex);
        bufferIndex = 0;
        paramInt2 += paramInt1;
        paramInt1 = i - paramInt1;
      }
      i = paramInt1;
    }
    if (i > 0)
    {
      appendToBuffer(paramArrayOfChar, paramInt2, i);
      for (paramInt1 = i - 1; paramInt1 >= 0; paramInt1--) {
        if (paramArrayOfChar[(paramInt2 + paramInt1)] == '\n')
        {
          lastNewline = (bufferIndex - i + paramInt1);
          break;
        }
      }
    }
  }
}
