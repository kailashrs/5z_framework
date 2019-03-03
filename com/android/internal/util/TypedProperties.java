package com.android.internal.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypedProperties
  extends HashMap<String, Object>
{
  static final String NULL_STRING = new String("<TypedProperties:NULL_STRING>");
  public static final int STRING_NOT_SET = -1;
  public static final int STRING_NULL = 0;
  public static final int STRING_SET = 1;
  public static final int STRING_TYPE_MISMATCH = -2;
  static final int TYPE_BOOLEAN = 90;
  static final int TYPE_BYTE = 329;
  static final int TYPE_DOUBLE = 2118;
  static final int TYPE_ERROR = -1;
  static final int TYPE_FLOAT = 1094;
  static final int TYPE_INT = 1097;
  static final int TYPE_LONG = 2121;
  static final int TYPE_SHORT = 585;
  static final int TYPE_STRING = 29516;
  static final int TYPE_UNSET = 120;
  
  public TypedProperties() {}
  
  static StreamTokenizer initTokenizer(Reader paramReader)
  {
    paramReader = new StreamTokenizer(paramReader);
    paramReader.resetSyntax();
    paramReader.wordChars(48, 57);
    paramReader.wordChars(65, 90);
    paramReader.wordChars(97, 122);
    paramReader.wordChars(95, 95);
    paramReader.wordChars(36, 36);
    paramReader.wordChars(46, 46);
    paramReader.wordChars(45, 45);
    paramReader.wordChars(43, 43);
    paramReader.ordinaryChar(61);
    paramReader.whitespaceChars(32, 32);
    paramReader.whitespaceChars(9, 9);
    paramReader.whitespaceChars(10, 10);
    paramReader.whitespaceChars(13, 13);
    paramReader.quoteChar(34);
    paramReader.slashStarComments(true);
    paramReader.slashSlashComments(true);
    return paramReader;
  }
  
  static int interpretType(String paramString)
  {
    if ("unset".equals(paramString)) {
      return 120;
    }
    if ("boolean".equals(paramString)) {
      return 90;
    }
    if ("byte".equals(paramString)) {
      return 329;
    }
    if ("short".equals(paramString)) {
      return 585;
    }
    if ("int".equals(paramString)) {
      return 1097;
    }
    if ("long".equals(paramString)) {
      return 2121;
    }
    if ("float".equals(paramString)) {
      return 1094;
    }
    if ("double".equals(paramString)) {
      return 2118;
    }
    if ("String".equals(paramString)) {
      return 29516;
    }
    return -1;
  }
  
  static void parse(Reader paramReader, Map<String, Object> paramMap)
    throws TypedProperties.ParseException, IOException
  {
    StreamTokenizer localStreamTokenizer = initTokenizer(paramReader);
    Pattern localPattern = Pattern.compile("([a-zA-Z_$][0-9a-zA-Z_$]*\\.)*[a-zA-Z_$][0-9a-zA-Z_$]*");
    do
    {
      int i = localStreamTokenizer.nextToken();
      if (i == -1) {
        return;
      }
      if (i != -3) {
        break label286;
      }
      i = interpretType(sval);
      if (i == -1) {
        break label275;
      }
      sval = null;
      if ((i == 120) && (localStreamTokenizer.nextToken() != 40)) {
        throw new ParseException(localStreamTokenizer, "'('");
      }
      if (localStreamTokenizer.nextToken() != -3) {
        break label264;
      }
      String str = sval;
      if (!localPattern.matcher(str).matches()) {
        break label253;
      }
      sval = null;
      if (i == 120)
      {
        if (localStreamTokenizer.nextToken() == 41) {
          paramMap.remove(str);
        } else {
          throw new ParseException(localStreamTokenizer, "')'");
        }
      }
      else
      {
        if (localStreamTokenizer.nextToken() != 61) {
          break;
        }
        paramReader = parseValue(localStreamTokenizer, i);
        Object localObject = paramMap.remove(str);
        if ((localObject != null) && (paramReader.getClass() != localObject.getClass())) {
          throw new ParseException(localStreamTokenizer, "(property previously declared as a different type)");
        }
        paramMap.put(str, paramReader);
      }
    } while (localStreamTokenizer.nextToken() == 59);
    throw new ParseException(localStreamTokenizer, "';'");
    throw new ParseException(localStreamTokenizer, "'='");
    label253:
    throw new ParseException(localStreamTokenizer, "valid property name");
    label264:
    throw new ParseException(localStreamTokenizer, "property name");
    label275:
    throw new ParseException(localStreamTokenizer, "valid type name");
    label286:
    throw new ParseException(localStreamTokenizer, "type name");
  }
  
  static Object parseValue(StreamTokenizer paramStreamTokenizer, int paramInt)
    throws IOException
  {
    int i = paramStreamTokenizer.nextToken();
    if (paramInt == 90)
    {
      if (i == -3)
      {
        if ("true".equals(sval)) {
          return Boolean.TRUE;
        }
        if ("false".equals(sval)) {
          return Boolean.FALSE;
        }
        throw new ParseException(paramStreamTokenizer, "boolean constant");
      }
      throw new ParseException(paramStreamTokenizer, "boolean constant");
    }
    if ((paramInt & 0xFF) == 73)
    {
      if (i == -3) {
        try
        {
          long l = Long.decode(sval).longValue();
          paramInt = paramInt >> 8 & 0xFF;
          if (paramInt != 4)
          {
            if (paramInt != 8)
            {
              switch (paramInt)
              {
              default: 
                paramStreamTokenizer = new StringBuilder();
                paramStreamTokenizer.append("Internal error; unexpected integer type width ");
                paramStreamTokenizer.append(paramInt);
                throw new IllegalStateException(paramStreamTokenizer.toString());
              case 2: 
                if ((l >= -32768L) && (l <= 32767L)) {
                  return new Short((short)(int)l);
                }
                throw new ParseException(paramStreamTokenizer, "16-bit integer constant");
              }
              if ((l >= -128L) && (l <= 127L)) {
                return new Byte((byte)(int)l);
              }
              throw new ParseException(paramStreamTokenizer, "8-bit integer constant");
            }
            if ((l >= Long.MIN_VALUE) && (l <= Long.MAX_VALUE)) {
              return new Long(l);
            }
            throw new ParseException(paramStreamTokenizer, "64-bit integer constant");
          }
          if ((l >= -2147483648L) && (l <= 2147483647L)) {
            return new Integer((int)l);
          }
          throw new ParseException(paramStreamTokenizer, "32-bit integer constant");
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          throw new ParseException(paramStreamTokenizer, "integer constant");
        }
      }
      throw new ParseException(paramStreamTokenizer, "integer constant");
    }
    if ((paramInt & 0xFF) == 70)
    {
      if (i == -3) {
        try
        {
          double d1 = Double.parseDouble(sval);
          if ((paramInt >> 8 & 0xFF) == 4)
          {
            double d2 = Math.abs(d1);
            if ((d2 != 0.0D) && (!Double.isInfinite(d1)) && (!Double.isNaN(d1)) && ((d2 < 1.401298464324817E-45D) || (d2 > 3.4028234663852886E38D))) {
              throw new ParseException(paramStreamTokenizer, "32-bit float constant");
            }
            return new Float((float)d1);
          }
          return new Double(d1);
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          throw new ParseException(paramStreamTokenizer, "float constant");
        }
      }
      throw new ParseException(paramStreamTokenizer, "float constant");
    }
    if (paramInt == 29516)
    {
      if (i == 34) {
        return sval;
      }
      if ((i == -3) && ("null".equals(sval))) {
        return NULL_STRING;
      }
      throw new ParseException(paramStreamTokenizer, "double-quoted string or 'null'");
    }
    paramStreamTokenizer = new StringBuilder();
    paramStreamTokenizer.append("Internal error; unknown type ");
    paramStreamTokenizer.append(paramInt);
    throw new IllegalStateException(paramStreamTokenizer.toString());
  }
  
  public Object get(Object paramObject)
  {
    paramObject = super.get(paramObject);
    if (paramObject == NULL_STRING) {
      return null;
    }
    return paramObject;
  }
  
  public boolean getBoolean(String paramString)
  {
    return getBoolean(paramString, false);
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    Object localObject = super.get(paramString);
    if (localObject == null) {
      return paramBoolean;
    }
    if ((localObject instanceof Boolean)) {
      return ((Boolean)localObject).booleanValue();
    }
    throw new TypeException(paramString, localObject, "boolean");
  }
  
  public byte getByte(String paramString)
  {
    return getByte(paramString, (byte)0);
  }
  
  public byte getByte(String paramString, byte paramByte)
  {
    Object localObject = super.get(paramString);
    if (localObject == null) {
      return paramByte;
    }
    if ((localObject instanceof Byte)) {
      return ((Byte)localObject).byteValue();
    }
    throw new TypeException(paramString, localObject, "byte");
  }
  
  public double getDouble(String paramString)
  {
    return getDouble(paramString, 0.0D);
  }
  
  public double getDouble(String paramString, double paramDouble)
  {
    Object localObject = super.get(paramString);
    if (localObject == null) {
      return paramDouble;
    }
    if ((localObject instanceof Double)) {
      return ((Double)localObject).doubleValue();
    }
    throw new TypeException(paramString, localObject, "double");
  }
  
  public float getFloat(String paramString)
  {
    return getFloat(paramString, 0.0F);
  }
  
  public float getFloat(String paramString, float paramFloat)
  {
    Object localObject = super.get(paramString);
    if (localObject == null) {
      return paramFloat;
    }
    if ((localObject instanceof Float)) {
      return ((Float)localObject).floatValue();
    }
    throw new TypeException(paramString, localObject, "float");
  }
  
  public int getInt(String paramString)
  {
    return getInt(paramString, 0);
  }
  
  public int getInt(String paramString, int paramInt)
  {
    Object localObject = super.get(paramString);
    if (localObject == null) {
      return paramInt;
    }
    if ((localObject instanceof Integer)) {
      return ((Integer)localObject).intValue();
    }
    throw new TypeException(paramString, localObject, "int");
  }
  
  public long getLong(String paramString)
  {
    return getLong(paramString, 0L);
  }
  
  public long getLong(String paramString, long paramLong)
  {
    Object localObject = super.get(paramString);
    if (localObject == null) {
      return paramLong;
    }
    if ((localObject instanceof Long)) {
      return ((Long)localObject).longValue();
    }
    throw new TypeException(paramString, localObject, "long");
  }
  
  public short getShort(String paramString)
  {
    return getShort(paramString, (short)0);
  }
  
  public short getShort(String paramString, short paramShort)
  {
    Object localObject = super.get(paramString);
    if (localObject == null) {
      return paramShort;
    }
    if ((localObject instanceof Short)) {
      return ((Short)localObject).shortValue();
    }
    throw new TypeException(paramString, localObject, "short");
  }
  
  public String getString(String paramString)
  {
    return getString(paramString, "");
  }
  
  public String getString(String paramString1, String paramString2)
  {
    Object localObject = super.get(paramString1);
    if (localObject == null) {
      return paramString2;
    }
    if (localObject == NULL_STRING) {
      return null;
    }
    if ((localObject instanceof String)) {
      return (String)localObject;
    }
    throw new TypeException(paramString1, localObject, "string");
  }
  
  public int getStringInfo(String paramString)
  {
    paramString = super.get(paramString);
    if (paramString == null) {
      return -1;
    }
    if (paramString == NULL_STRING) {
      return 0;
    }
    if ((paramString instanceof String)) {
      return 1;
    }
    return -2;
  }
  
  public void load(Reader paramReader)
    throws IOException
  {
    parse(paramReader, this);
  }
  
  public static class ParseException
    extends IllegalArgumentException
  {
    ParseException(StreamTokenizer paramStreamTokenizer, String paramString)
    {
      super();
    }
  }
  
  public static class TypeException
    extends IllegalArgumentException
  {
    TypeException(String paramString1, Object paramObject, String paramString2)
    {
      super();
    }
  }
}
