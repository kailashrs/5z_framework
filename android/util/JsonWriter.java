package android.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class JsonWriter
  implements Closeable
{
  private String indent;
  private boolean lenient;
  private final Writer out;
  private String separator;
  private final List<JsonScope> stack = new ArrayList();
  
  public JsonWriter(Writer paramWriter)
  {
    stack.add(JsonScope.EMPTY_DOCUMENT);
    separator = ":";
    if (paramWriter != null)
    {
      out = paramWriter;
      return;
    }
    throw new NullPointerException("out == null");
  }
  
  private void beforeName()
    throws IOException
  {
    Object localObject = peek();
    if (localObject == JsonScope.NONEMPTY_OBJECT) {
      out.write(44);
    } else {
      if (localObject != JsonScope.EMPTY_OBJECT) {
        break label43;
      }
    }
    newline();
    replaceTop(JsonScope.DANGLING_NAME);
    return;
    label43:
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Nesting problem: ");
    ((StringBuilder)localObject).append(stack);
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  private void beforeValue(boolean paramBoolean)
    throws IOException
  {
    switch (1.$SwitchMap$android$util$JsonScope[peek().ordinal()])
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Nesting problem: ");
      localStringBuilder.append(stack);
      throw new IllegalStateException(localStringBuilder.toString());
    case 5: 
      throw new IllegalStateException("JSON must have only one top-level value.");
    case 4: 
      out.append(separator);
      replaceTop(JsonScope.NONEMPTY_OBJECT);
      break;
    case 3: 
      out.append(',');
      newline();
      break;
    case 2: 
      replaceTop(JsonScope.NONEMPTY_ARRAY);
      newline();
      break;
    case 1: 
      if ((!lenient) && (!paramBoolean)) {
        throw new IllegalStateException("JSON must start with an array or an object.");
      }
      replaceTop(JsonScope.NONEMPTY_DOCUMENT);
    }
  }
  
  private JsonWriter close(JsonScope paramJsonScope1, JsonScope paramJsonScope2, String paramString)
    throws IOException
  {
    JsonScope localJsonScope = peek();
    if ((localJsonScope != paramJsonScope2) && (localJsonScope != paramJsonScope1))
    {
      paramJsonScope1 = new StringBuilder();
      paramJsonScope1.append("Nesting problem: ");
      paramJsonScope1.append(stack);
      throw new IllegalStateException(paramJsonScope1.toString());
    }
    stack.remove(stack.size() - 1);
    if (localJsonScope == paramJsonScope2) {
      newline();
    }
    out.write(paramString);
    return this;
  }
  
  private void newline()
    throws IOException
  {
    if (indent == null) {
      return;
    }
    out.write("\n");
    for (int i = 1; i < stack.size(); i++) {
      out.write(indent);
    }
  }
  
  private JsonWriter open(JsonScope paramJsonScope, String paramString)
    throws IOException
  {
    beforeValue(true);
    stack.add(paramJsonScope);
    out.write(paramString);
    return this;
  }
  
  private JsonScope peek()
  {
    return (JsonScope)stack.get(stack.size() - 1);
  }
  
  private void replaceTop(JsonScope paramJsonScope)
  {
    stack.set(stack.size() - 1, paramJsonScope);
  }
  
  private void string(String paramString)
    throws IOException
  {
    out.write("\"");
    int i = 0;
    int j = paramString.length();
    while (i < j)
    {
      int k = paramString.charAt(i);
      switch (k)
      {
      default: 
        if (k <= 31) {
          out.write(String.format("\\u%04x", new Object[] { Integer.valueOf(k) }));
        }
        break;
      case 8232: 
      case 8233: 
        out.write(String.format("\\u%04x", new Object[] { Integer.valueOf(k) }));
        break;
      case 34: 
      case 92: 
        out.write(92);
        out.write(k);
        break;
      case 13: 
        out.write("\\r");
        break;
      case 12: 
        out.write("\\f");
        break;
      case 10: 
        out.write("\\n");
        break;
      case 9: 
        out.write("\\t");
        break;
      case 8: 
        out.write("\\b");
        break;
      }
      out.write(k);
      i++;
    }
    out.write("\"");
  }
  
  public JsonWriter beginArray()
    throws IOException
  {
    return open(JsonScope.EMPTY_ARRAY, "[");
  }
  
  public JsonWriter beginObject()
    throws IOException
  {
    return open(JsonScope.EMPTY_OBJECT, "{");
  }
  
  public void close()
    throws IOException
  {
    out.close();
    if (peek() == JsonScope.NONEMPTY_DOCUMENT) {
      return;
    }
    throw new IOException("Incomplete document");
  }
  
  public JsonWriter endArray()
    throws IOException
  {
    return close(JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, "]");
  }
  
  public JsonWriter endObject()
    throws IOException
  {
    return close(JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, "}");
  }
  
  public void flush()
    throws IOException
  {
    out.flush();
  }
  
  public boolean isLenient()
  {
    return lenient;
  }
  
  public JsonWriter name(String paramString)
    throws IOException
  {
    if (paramString != null)
    {
      beforeName();
      string(paramString);
      return this;
    }
    throw new NullPointerException("name == null");
  }
  
  public JsonWriter nullValue()
    throws IOException
  {
    beforeValue(false);
    out.write("null");
    return this;
  }
  
  public void setIndent(String paramString)
  {
    if (paramString.isEmpty())
    {
      indent = null;
      separator = ":";
    }
    else
    {
      indent = paramString;
      separator = ": ";
    }
  }
  
  public void setLenient(boolean paramBoolean)
  {
    lenient = paramBoolean;
  }
  
  public JsonWriter value(double paramDouble)
    throws IOException
  {
    if ((!lenient) && ((Double.isNaN(paramDouble)) || (Double.isInfinite(paramDouble))))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Numeric values must be finite, but was ");
      localStringBuilder.append(paramDouble);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    beforeValue(false);
    out.append(Double.toString(paramDouble));
    return this;
  }
  
  public JsonWriter value(long paramLong)
    throws IOException
  {
    beforeValue(false);
    out.write(Long.toString(paramLong));
    return this;
  }
  
  public JsonWriter value(Number paramNumber)
    throws IOException
  {
    if (paramNumber == null) {
      return nullValue();
    }
    Object localObject = paramNumber.toString();
    if ((!lenient) && ((((String)localObject).equals("-Infinity")) || (((String)localObject).equals("Infinity")) || (((String)localObject).equals("NaN"))))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Numeric values must be finite, but was ");
      ((StringBuilder)localObject).append(paramNumber);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    beforeValue(false);
    out.append((CharSequence)localObject);
    return this;
  }
  
  public JsonWriter value(String paramString)
    throws IOException
  {
    if (paramString == null) {
      return nullValue();
    }
    beforeValue(false);
    string(paramString);
    return this;
  }
  
  public JsonWriter value(boolean paramBoolean)
    throws IOException
  {
    beforeValue(false);
    Writer localWriter = out;
    String str;
    if (paramBoolean) {
      str = "true";
    } else {
      str = "false";
    }
    localWriter.write(str);
    return this;
  }
}
