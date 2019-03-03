package com.android.internal.os;

import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Formatter;
import java.util.Locale;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public abstract class LoggingPrintStream
  extends PrintStream
{
  private final StringBuilder builder = new StringBuilder();
  private CharBuffer decodedChars;
  private CharsetDecoder decoder;
  private ByteBuffer encodedBytes;
  private final Formatter formatter = new Formatter(builder, null);
  
  protected LoggingPrintStream()
  {
    super(new OutputStream()
    {
      public void write(int paramAnonymousInt)
        throws IOException
      {
        throw new AssertionError();
      }
    });
  }
  
  private void flush(boolean paramBoolean)
  {
    int i = builder.length();
    int k;
    for (int j = 0; j < i; j = k + 1)
    {
      k = builder.indexOf("\n", j);
      if (k == -1) {
        break;
      }
      log(builder.substring(j, k));
    }
    if (paramBoolean)
    {
      if (j < i) {
        log(builder.substring(j));
      }
      builder.setLength(0);
    }
    else
    {
      builder.delete(0, j);
    }
  }
  
  public PrintStream append(char paramChar)
  {
    try
    {
      print(paramChar);
      return this;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public PrintStream append(CharSequence paramCharSequence)
  {
    try
    {
      builder.append(paramCharSequence);
      flush(false);
      return this;
    }
    finally
    {
      paramCharSequence = finally;
      throw paramCharSequence;
    }
  }
  
  public PrintStream append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    try
    {
      builder.append(paramCharSequence, paramInt1, paramInt2);
      flush(false);
      return this;
    }
    finally
    {
      paramCharSequence = finally;
      throw paramCharSequence;
    }
  }
  
  public boolean checkError()
  {
    return false;
  }
  
  public void close() {}
  
  public void flush()
  {
    try
    {
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public PrintStream format(String paramString, Object... paramVarArgs)
  {
    return format(Locale.getDefault(), paramString, paramVarArgs);
  }
  
  public PrintStream format(Locale paramLocale, String paramString, Object... paramVarArgs)
  {
    if (paramString != null) {}
    try
    {
      formatter.format(paramLocale, paramString, paramVarArgs);
      flush(false);
      return this;
    }
    finally {}
    paramLocale = new java/lang/NullPointerException;
    paramLocale.<init>("format");
    throw paramLocale;
  }
  
  protected abstract void log(String paramString);
  
  public void print(char paramChar)
  {
    try
    {
      builder.append(paramChar);
      if (paramChar == '\n') {
        flush(false);
      }
      return;
    }
    finally {}
  }
  
  public void print(double paramDouble)
  {
    try
    {
      builder.append(paramDouble);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void print(float paramFloat)
  {
    try
    {
      builder.append(paramFloat);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void print(int paramInt)
  {
    try
    {
      builder.append(paramInt);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void print(long paramLong)
  {
    try
    {
      builder.append(paramLong);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void print(Object paramObject)
  {
    try
    {
      builder.append(paramObject);
      flush(false);
      return;
    }
    finally
    {
      paramObject = finally;
      throw paramObject;
    }
  }
  
  public void print(String paramString)
  {
    try
    {
      builder.append(paramString);
      flush(false);
      return;
    }
    finally
    {
      paramString = finally;
      throw paramString;
    }
  }
  
  public void print(boolean paramBoolean)
  {
    try
    {
      builder.append(paramBoolean);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void print(char[] paramArrayOfChar)
  {
    try
    {
      builder.append(paramArrayOfChar);
      flush(false);
      return;
    }
    finally
    {
      paramArrayOfChar = finally;
      throw paramArrayOfChar;
    }
  }
  
  public PrintStream printf(String paramString, Object... paramVarArgs)
  {
    return format(paramString, paramVarArgs);
  }
  
  public PrintStream printf(Locale paramLocale, String paramString, Object... paramVarArgs)
  {
    return format(paramLocale, paramString, paramVarArgs);
  }
  
  public void println()
  {
    try
    {
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void println(char paramChar)
  {
    try
    {
      builder.append(paramChar);
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void println(double paramDouble)
  {
    try
    {
      builder.append(paramDouble);
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void println(float paramFloat)
  {
    try
    {
      builder.append(paramFloat);
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void println(int paramInt)
  {
    try
    {
      builder.append(paramInt);
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void println(long paramLong)
  {
    try
    {
      builder.append(paramLong);
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void println(Object paramObject)
  {
    try
    {
      builder.append(paramObject);
      flush(true);
      return;
    }
    finally
    {
      paramObject = finally;
      throw paramObject;
    }
  }
  
  public void println(String paramString)
  {
    try
    {
      if ((builder.length() == 0) && (paramString != null))
      {
        int i = paramString.length();
        int k;
        for (int j = 0; j < i; j = k + 1)
        {
          k = paramString.indexOf('\n', j);
          if (k == -1) {
            break;
          }
          log(paramString.substring(j, k));
        }
        if (j < i) {
          log(paramString.substring(j));
        }
      }
      else
      {
        builder.append(paramString);
        flush(true);
      }
      return;
    }
    finally {}
  }
  
  public void println(boolean paramBoolean)
  {
    try
    {
      builder.append(paramBoolean);
      flush(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void println(char[] paramArrayOfChar)
  {
    try
    {
      builder.append(paramArrayOfChar);
      flush(true);
      return;
    }
    finally
    {
      paramArrayOfChar = finally;
      throw paramArrayOfChar;
    }
  }
  
  protected void setError() {}
  
  public void write(int paramInt)
  {
    write(new byte[] { (byte)paramInt }, 0, 1);
  }
  
  public void write(byte[] paramArrayOfByte)
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      if (decoder == null)
      {
        encodedBytes = ByteBuffer.allocate(80);
        decodedChars = CharBuffer.allocate(80);
        decoder = Charset.defaultCharset().newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
      }
      paramInt2 = paramInt1 + paramInt2;
      while (paramInt1 < paramInt2)
      {
        int i = Math.min(encodedBytes.remaining(), paramInt2 - paramInt1);
        encodedBytes.put(paramArrayOfByte, paramInt1, i);
        paramInt1 += i;
        encodedBytes.flip();
        CoderResult localCoderResult;
        do
        {
          localCoderResult = decoder.decode(encodedBytes, decodedChars, false);
          decodedChars.flip();
          builder.append(decodedChars);
          decodedChars.clear();
        } while (localCoderResult.isOverflow());
        encodedBytes.compact();
      }
      flush(false);
      return;
    }
    finally {}
  }
}
