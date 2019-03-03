package android.util;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import libcore.internal.StringPool;

public final class JsonReader
  implements Closeable
{
  private static final String FALSE = "false";
  private static final String TRUE = "true";
  private final char[] buffer = new char['Ѐ'];
  private int bufferStartColumn = 1;
  private int bufferStartLine = 1;
  private final Reader in;
  private boolean lenient = false;
  private int limit = 0;
  private String name;
  private int pos = 0;
  private boolean skipping;
  private final List<JsonScope> stack = new ArrayList();
  private final StringPool stringPool = new StringPool();
  private JsonToken token;
  private String value;
  private int valueLength;
  private int valuePos;
  
  public JsonReader(Reader paramReader)
  {
    push(JsonScope.EMPTY_DOCUMENT);
    skipping = false;
    if (paramReader != null)
    {
      in = paramReader;
      return;
    }
    throw new NullPointerException("in == null");
  }
  
  private JsonToken advance()
    throws IOException
  {
    peek();
    JsonToken localJsonToken = token;
    token = null;
    value = null;
    name = null;
    return localJsonToken;
  }
  
  private void checkLenient()
    throws IOException
  {
    if (lenient) {
      return;
    }
    throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
  }
  
  private JsonToken decodeLiteral()
    throws IOException
  {
    if (valuePos == -1) {
      return JsonToken.STRING;
    }
    if ((valueLength == 4) && (('n' == buffer[valuePos]) || ('N' == buffer[valuePos])) && (('u' == buffer[(valuePos + 1)]) || ('U' == buffer[(valuePos + 1)])) && (('l' == buffer[(valuePos + 2)]) || ('L' == buffer[(valuePos + 2)])) && (('l' == buffer[(valuePos + 3)]) || ('L' == buffer[(valuePos + 3)])))
    {
      value = "null";
      return JsonToken.NULL;
    }
    if ((valueLength == 4) && (('t' == buffer[valuePos]) || ('T' == buffer[valuePos])) && (('r' == buffer[(valuePos + 1)]) || ('R' == buffer[(valuePos + 1)])) && (('u' == buffer[(valuePos + 2)]) || ('U' == buffer[(valuePos + 2)])) && (('e' == buffer[(valuePos + 3)]) || ('E' == buffer[(valuePos + 3)])))
    {
      value = "true";
      return JsonToken.BOOLEAN;
    }
    if ((valueLength == 5) && (('f' == buffer[valuePos]) || ('F' == buffer[valuePos])) && (('a' == buffer[(valuePos + 1)]) || ('A' == buffer[(valuePos + 1)])) && (('l' == buffer[(valuePos + 2)]) || ('L' == buffer[(valuePos + 2)])) && (('s' == buffer[(valuePos + 3)]) || ('S' == buffer[(valuePos + 3)])) && (('e' == buffer[(valuePos + 4)]) || ('E' == buffer[(valuePos + 4)])))
    {
      value = "false";
      return JsonToken.BOOLEAN;
    }
    value = stringPool.get(buffer, valuePos, valueLength);
    return decodeNumber(buffer, valuePos, valueLength);
  }
  
  private JsonToken decodeNumber(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramArrayOfChar[i];
    int k = i;
    int m = j;
    if (j == 45)
    {
      k = i + 1;
      m = paramArrayOfChar[k];
    }
    if (m == 48)
    {
      m = k + 1;
      i = paramArrayOfChar[m];
    }
    else
    {
      if ((m < 49) || (m > 57)) {
        break label339;
      }
      j = k + 1;
      for (k = paramArrayOfChar[j];; k = paramArrayOfChar[j])
      {
        m = j;
        i = k;
        if (k < 48) {
          break;
        }
        m = j;
        i = k;
        if (k > 57) {
          break;
        }
        j++;
      }
    }
    k = m;
    j = i;
    if (i == 46)
    {
      i = m + 1;
      for (m = paramArrayOfChar[i];; m = paramArrayOfChar[i])
      {
        k = i;
        j = m;
        if (m < 48) {
          break;
        }
        k = i;
        j = m;
        if (m > 57) {
          break;
        }
        i++;
      }
    }
    if (j != 101)
    {
      i = k;
      if (j != 69) {}
    }
    else
    {
      i = k + 1;
      j = paramArrayOfChar[i];
      if (j != 43)
      {
        k = i;
        m = j;
        if (j != 45) {}
      }
      else
      {
        k = i + 1;
        m = paramArrayOfChar[k];
      }
      if ((m < 48) || (m > 57)) {
        break label335;
      }
      m = k + 1;
      for (k = paramArrayOfChar[m];; k = paramArrayOfChar[m])
      {
        i = m;
        if (k < 48) {
          break;
        }
        i = m;
        if (k > 57) {
          break;
        }
        m++;
      }
    }
    if (i == paramInt1 + paramInt2) {
      return JsonToken.NUMBER;
    }
    return JsonToken.STRING;
    label335:
    return JsonToken.STRING;
    label339:
    return JsonToken.STRING;
  }
  
  private void expect(JsonToken paramJsonToken)
    throws IOException
  {
    peek();
    if (token == paramJsonToken)
    {
      advance();
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Expected ");
    localStringBuilder.append(paramJsonToken);
    localStringBuilder.append(" but was ");
    localStringBuilder.append(peek());
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private boolean fillBuffer(int paramInt)
    throws IOException
  {
    for (int i = 0; i < pos; i++) {
      if (buffer[i] == '\n')
      {
        bufferStartLine += 1;
        bufferStartColumn = 1;
      }
      else
      {
        bufferStartColumn += 1;
      }
    }
    if (limit != pos)
    {
      limit -= pos;
      System.arraycopy(buffer, pos, buffer, 0, limit);
    }
    else
    {
      limit = 0;
    }
    pos = 0;
    do
    {
      i = in.read(buffer, limit, buffer.length - limit);
      if (i == -1) {
        break;
      }
      limit += i;
      if ((bufferStartLine == 1) && (bufferStartColumn == 1) && (limit > 0) && (buffer[0] == 65279))
      {
        pos += 1;
        bufferStartColumn -= 1;
      }
    } while (limit < paramInt);
    return true;
    return false;
  }
  
  private int getColumnNumber()
  {
    int i = bufferStartColumn;
    for (int j = 0; j < pos; j++) {
      if (buffer[j] == '\n') {
        i = 1;
      } else {
        i++;
      }
    }
    return i;
  }
  
  private int getLineNumber()
  {
    int i = bufferStartLine;
    int j = 0;
    while (j < pos)
    {
      int k = i;
      if (buffer[j] == '\n') {
        k = i + 1;
      }
      j++;
      i = k;
    }
    return i;
  }
  
  private CharSequence getSnippet()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = Math.min(pos, 20);
    localStringBuilder.append(buffer, pos - i, i);
    i = Math.min(limit - pos, 20);
    localStringBuilder.append(buffer, pos, i);
    return localStringBuilder;
  }
  
  private JsonToken nextInArray(boolean paramBoolean)
    throws IOException
  {
    if (paramBoolean)
    {
      replaceTop(JsonScope.NONEMPTY_ARRAY);
    }
    else
    {
      i = nextNonWhitespace();
      if (i != 44)
      {
        if (i != 59)
        {
          if (i == 93)
          {
            pop();
            localJsonToken = JsonToken.END_ARRAY;
            token = localJsonToken;
            return localJsonToken;
          }
          throw syntaxError("Unterminated array");
        }
        checkLenient();
      }
    }
    int i = nextNonWhitespace();
    if ((i != 44) && (i != 59))
    {
      if (i != 93)
      {
        pos -= 1;
        return nextValue();
      }
      if (paramBoolean)
      {
        pop();
        localJsonToken = JsonToken.END_ARRAY;
        token = localJsonToken;
        return localJsonToken;
      }
    }
    checkLenient();
    pos -= 1;
    value = "null";
    JsonToken localJsonToken = JsonToken.NULL;
    token = localJsonToken;
    return localJsonToken;
  }
  
  private JsonToken nextInObject(boolean paramBoolean)
    throws IOException
  {
    if (paramBoolean)
    {
      if (nextNonWhitespace() != 125)
      {
        pos -= 1;
      }
      else
      {
        pop();
        localJsonToken = JsonToken.END_OBJECT;
        token = localJsonToken;
        return localJsonToken;
      }
    }
    else
    {
      i = nextNonWhitespace();
      if ((i != 44) && (i != 59))
      {
        if (i == 125)
        {
          pop();
          localJsonToken = JsonToken.END_OBJECT;
          token = localJsonToken;
          return localJsonToken;
        }
        throw syntaxError("Unterminated object");
      }
    }
    int i = nextNonWhitespace();
    if (i != 34)
    {
      if (i != 39)
      {
        checkLenient();
        pos -= 1;
        name = nextLiteral(false);
        if (name.isEmpty()) {
          throw syntaxError("Expected name");
        }
      }
      else
      {
        checkLenient();
      }
    }
    else {
      name = nextString((char)i);
    }
    replaceTop(JsonScope.DANGLING_NAME);
    JsonToken localJsonToken = JsonToken.NAME;
    token = localJsonToken;
    return localJsonToken;
  }
  
  private String nextLiteral(boolean paramBoolean)
    throws IOException
  {
    valuePos = -1;
    valueLength = 0;
    Object localObject1 = null;
    int i = 0;
    Object localObject2;
    int j;
    do
    {
      do
      {
        while (pos + i < limit) {
          switch (buffer[(pos + i)])
          {
          default: 
            i++;
            break;
          case '#': 
          case '/': 
          case ';': 
          case '=': 
          case '\\': 
            checkLenient();
          case '\t': 
          case '\n': 
          case '\f': 
          case '\r': 
          case ' ': 
          case ',': 
          case ':': 
          case '[': 
          case ']': 
          case '{': 
          case '}': 
            break label298;
          }
        }
        if (i >= buffer.length) {
          break;
        }
      } while (fillBuffer(i + 1));
      buffer[limit] = ((char)0);
      break;
      localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = new StringBuilder();
      }
      ((StringBuilder)localObject2).append(buffer, pos, i);
      valueLength += i;
      pos += i;
      j = 0;
      i = 0;
      localObject1 = localObject2;
    } while (fillBuffer(1));
    localObject1 = localObject2;
    i = j;
    label298:
    if ((paramBoolean) && (localObject1 == null))
    {
      valuePos = pos;
      localObject1 = null;
    }
    for (;;)
    {
      break;
      if (skipping)
      {
        localObject1 = "skipped!";
      }
      else if (localObject1 == null)
      {
        localObject1 = stringPool.get(buffer, pos, i);
      }
      else
      {
        ((StringBuilder)localObject1).append(buffer, pos, i);
        localObject1 = ((StringBuilder)localObject1).toString();
      }
    }
    valueLength += i;
    pos += i;
    return localObject1;
  }
  
  private int nextNonWhitespace()
    throws IOException
  {
    for (;;)
    {
      if ((pos >= limit) && (!fillBuffer(1))) {
        throw new EOFException("End of input");
      }
      char[] arrayOfChar = buffer;
      int i = pos;
      pos = (i + 1);
      i = arrayOfChar[i];
      if ((i != 13) && (i != 32))
      {
        if (i != 35) {
          if (i == 47) {}
        }
        switch (i)
        {
        default: 
          return i;
          if ((pos == limit) && (!fillBuffer(1))) {
            return i;
          }
          checkLenient();
          int j = buffer[pos];
          if (j != 42)
          {
            if (j != 47) {
              return i;
            }
            pos += 1;
            skipToEndOfLine();
          }
          else
          {
            pos += 1;
            if (skipTo("*/"))
            {
              pos += 2;
            }
            else
            {
              throw syntaxError("Unterminated comment");
              checkLenient();
              skipToEndOfLine();
            }
          }
          break;
        }
      }
    }
  }
  
  private String nextString(char paramChar)
    throws IOException
  {
    Object localObject2;
    for (Object localObject1 = null;; localObject1 = localObject2)
    {
      int j;
      for (int i = pos; pos < limit; i = j)
      {
        localObject2 = buffer;
        j = pos;
        pos = (j + 1);
        char c = localObject2[j];
        if (c == paramChar)
        {
          if (skipping) {
            return "skipped!";
          }
          if (localObject1 == null) {
            return stringPool.get(buffer, i, pos - i - 1);
          }
          localObject1.append(buffer, i, pos - i - 1);
          return localObject1.toString();
        }
        localObject2 = localObject1;
        j = i;
        if (c == '\\')
        {
          localObject2 = localObject1;
          if (localObject1 == null) {
            localObject2 = new StringBuilder();
          }
          ((StringBuilder)localObject2).append(buffer, i, pos - i - 1);
          ((StringBuilder)localObject2).append(readEscapeCharacter());
          j = pos;
        }
        localObject1 = localObject2;
      }
      localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = new StringBuilder();
      }
      ((StringBuilder)localObject2).append(buffer, i, pos - i);
      if (!fillBuffer(1)) {
        break;
      }
    }
    throw syntaxError("Unterminated string");
  }
  
  private JsonToken nextValue()
    throws IOException
  {
    int i = nextNonWhitespace();
    if (i != 34)
    {
      if (i != 39)
      {
        if (i != 91)
        {
          if (i != 123)
          {
            pos -= 1;
            return readLiteral();
          }
          push(JsonScope.EMPTY_OBJECT);
          localJsonToken = JsonToken.BEGIN_OBJECT;
          token = localJsonToken;
          return localJsonToken;
        }
        push(JsonScope.EMPTY_ARRAY);
        localJsonToken = JsonToken.BEGIN_ARRAY;
        token = localJsonToken;
        return localJsonToken;
      }
      checkLenient();
    }
    value = nextString((char)i);
    JsonToken localJsonToken = JsonToken.STRING;
    token = localJsonToken;
    return localJsonToken;
  }
  
  private JsonToken objectValue()
    throws IOException
  {
    int i = nextNonWhitespace();
    if (i != 58) {
      if (i == 61)
      {
        checkLenient();
        if (((pos < limit) || (fillBuffer(1))) && (buffer[pos] == '>')) {
          pos += 1;
        }
      }
      else
      {
        throw syntaxError("Expected ':'");
      }
    }
    replaceTop(JsonScope.NONEMPTY_OBJECT);
    return nextValue();
  }
  
  private JsonScope peekStack()
  {
    return (JsonScope)stack.get(stack.size() - 1);
  }
  
  private JsonScope pop()
  {
    return (JsonScope)stack.remove(stack.size() - 1);
  }
  
  private void push(JsonScope paramJsonScope)
  {
    stack.add(paramJsonScope);
  }
  
  private char readEscapeCharacter()
    throws IOException
  {
    if ((pos == limit) && (!fillBuffer(1))) {
      throw syntaxError("Unterminated escape sequence");
    }
    Object localObject = buffer;
    int i = pos;
    pos = (i + 1);
    char c = localObject[i];
    if (c != 'b')
    {
      if (c != 'f')
      {
        if (c != 'n')
        {
          if (c != 'r')
          {
            switch (c)
            {
            default: 
              return c;
            case 'u': 
              if ((pos + 4 > limit) && (!fillBuffer(4))) {
                throw syntaxError("Unterminated escape sequence");
              }
              localObject = stringPool.get(buffer, pos, 4);
              pos += 4;
              return (char)Integer.parseInt((String)localObject, 16);
            }
            return '\t';
          }
          return '\r';
        }
        return '\n';
      }
      return '\f';
    }
    return '\b';
  }
  
  private JsonToken readLiteral()
    throws IOException
  {
    value = nextLiteral(true);
    if (valueLength != 0)
    {
      token = decodeLiteral();
      if (token == JsonToken.STRING) {
        checkLenient();
      }
      return token;
    }
    throw syntaxError("Expected literal value");
  }
  
  private void replaceTop(JsonScope paramJsonScope)
  {
    stack.set(stack.size() - 1, paramJsonScope);
  }
  
  private boolean skipTo(String paramString)
    throws IOException
  {
    int i = pos;
    int j = paramString.length();
    int k = limit;
    int m = 0;
    if ((i + j > k) && (!fillBuffer(paramString.length()))) {
      return false;
    }
    for (;;)
    {
      if (m >= paramString.length()) {
        break label92;
      }
      if (buffer[(pos + m)] != paramString.charAt(m))
      {
        pos += 1;
        break;
      }
      m++;
    }
    label92:
    return true;
  }
  
  private void skipToEndOfLine()
    throws IOException
  {
    while ((pos < limit) || (fillBuffer(1)))
    {
      char[] arrayOfChar = buffer;
      int i = pos;
      pos = (i + 1);
      i = arrayOfChar[i];
      if ((i == 13) || (i == 10)) {
        break;
      }
    }
  }
  
  private IOException syntaxError(String paramString)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" at line ");
    localStringBuilder.append(getLineNumber());
    localStringBuilder.append(" column ");
    localStringBuilder.append(getColumnNumber());
    throw new MalformedJsonException(localStringBuilder.toString());
  }
  
  public void beginArray()
    throws IOException
  {
    expect(JsonToken.BEGIN_ARRAY);
  }
  
  public void beginObject()
    throws IOException
  {
    expect(JsonToken.BEGIN_OBJECT);
  }
  
  public void close()
    throws IOException
  {
    value = null;
    token = null;
    stack.clear();
    stack.add(JsonScope.CLOSED);
    in.close();
  }
  
  public void endArray()
    throws IOException
  {
    expect(JsonToken.END_ARRAY);
  }
  
  public void endObject()
    throws IOException
  {
    expect(JsonToken.END_OBJECT);
  }
  
  public boolean hasNext()
    throws IOException
  {
    peek();
    boolean bool;
    if ((token != JsonToken.END_OBJECT) && (token != JsonToken.END_ARRAY)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLenient()
  {
    return lenient;
  }
  
  public boolean nextBoolean()
    throws IOException
  {
    peek();
    if (token == JsonToken.BOOLEAN)
    {
      boolean bool;
      if (value == "true") {
        bool = true;
      } else {
        bool = false;
      }
      advance();
      return bool;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Expected a boolean but was ");
    localStringBuilder.append(token);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public double nextDouble()
    throws IOException
  {
    peek();
    if ((token != JsonToken.STRING) && (token != JsonToken.NUMBER))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Expected a double but was ");
      localStringBuilder.append(token);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    double d = Double.parseDouble(value);
    advance();
    return d;
  }
  
  public int nextInt()
    throws IOException
  {
    peek();
    if ((token != JsonToken.STRING) && (token != JsonToken.NUMBER))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Expected an int but was ");
      localStringBuilder.append(token);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    int i;
    try
    {
      i = Integer.parseInt(value);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      double d = Double.parseDouble(value);
      i = (int)d;
      if (i != d) {
        break label102;
      }
    }
    advance();
    return i;
    label102:
    throw new NumberFormatException(value);
  }
  
  public long nextLong()
    throws IOException
  {
    peek();
    if ((token != JsonToken.STRING) && (token != JsonToken.NUMBER))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Expected a long but was ");
      localStringBuilder.append(token);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    long l;
    try
    {
      l = Long.parseLong(value);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      double d = Double.parseDouble(value);
      l = d;
      if (l != d) {
        break label105;
      }
    }
    advance();
    return l;
    label105:
    throw new NumberFormatException(value);
  }
  
  public String nextName()
    throws IOException
  {
    peek();
    if (token == JsonToken.NAME)
    {
      localObject = name;
      advance();
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Expected a name but was ");
    ((StringBuilder)localObject).append(peek());
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  public void nextNull()
    throws IOException
  {
    peek();
    if (token == JsonToken.NULL)
    {
      advance();
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Expected null but was ");
    localStringBuilder.append(token);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public String nextString()
    throws IOException
  {
    peek();
    if ((token != JsonToken.STRING) && (token != JsonToken.NUMBER))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Expected a string but was ");
      ((StringBuilder)localObject).append(peek());
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    Object localObject = value;
    advance();
    return localObject;
  }
  
  public JsonToken peek()
    throws IOException
  {
    if (token != null) {
      return token;
    }
    switch (1.$SwitchMap$android$util$JsonScope[peekStack().ordinal()])
    {
    default: 
      throw new AssertionError();
    case 8: 
      throw new IllegalStateException("JsonReader is closed");
    case 7: 
      try
      {
        JsonToken localJsonToken = nextValue();
        if (lenient) {
          return localJsonToken;
        }
        throw syntaxError("Expected EOF");
      }
      catch (EOFException localEOFException)
      {
        localObject = JsonToken.END_DOCUMENT;
        token = ((JsonToken)localObject);
        return localObject;
      }
    case 6: 
      return nextInObject(false);
    case 5: 
      return objectValue();
    case 4: 
      return nextInObject(true);
    case 3: 
      return nextInArray(false);
    case 2: 
      return nextInArray(true);
    }
    replaceTop(JsonScope.NONEMPTY_DOCUMENT);
    Object localObject = nextValue();
    if ((!lenient) && (token != JsonToken.BEGIN_ARRAY) && (token != JsonToken.BEGIN_OBJECT))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Expected JSON document to start with '[' or '{' but was ");
      ((StringBuilder)localObject).append(token);
      throw new IOException(((StringBuilder)localObject).toString());
    }
    return localObject;
  }
  
  public void setLenient(boolean paramBoolean)
  {
    lenient = paramBoolean;
  }
  
  public void skipValue()
    throws IOException
  {
    skipping = true;
    try
    {
      if ((hasNext()) && (peek() != JsonToken.END_DOCUMENT))
      {
        int i = 0;
        int j;
        do
        {
          localObject1 = advance();
          if ((localObject1 != JsonToken.BEGIN_ARRAY) && (localObject1 != JsonToken.BEGIN_OBJECT))
          {
            if (localObject1 != JsonToken.END_ARRAY)
            {
              JsonToken localJsonToken = JsonToken.END_OBJECT;
              j = i;
              if (localObject1 != localJsonToken) {}
            }
            else
            {
              j = i - 1;
            }
          }
          else {
            j = i + 1;
          }
          i = j;
        } while (j != 0);
        return;
      }
      Object localObject1 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject1).<init>("No element left to skip");
      throw ((Throwable)localObject1);
    }
    finally
    {
      skipping = false;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append(" near ");
    localStringBuilder.append(getSnippet());
    return localStringBuilder.toString();
  }
}
