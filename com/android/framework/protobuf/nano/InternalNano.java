package com.android.framework.protobuf.nano;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class InternalNano
{
  protected static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
  public static final Object LAZY_INIT_LOCK = new Object();
  public static final int TYPE_BOOL = 8;
  public static final int TYPE_BYTES = 12;
  public static final int TYPE_DOUBLE = 1;
  public static final int TYPE_ENUM = 14;
  public static final int TYPE_FIXED32 = 7;
  public static final int TYPE_FIXED64 = 6;
  public static final int TYPE_FLOAT = 2;
  public static final int TYPE_GROUP = 10;
  public static final int TYPE_INT32 = 5;
  public static final int TYPE_INT64 = 3;
  public static final int TYPE_MESSAGE = 11;
  public static final int TYPE_SFIXED32 = 15;
  public static final int TYPE_SFIXED64 = 16;
  public static final int TYPE_SINT32 = 17;
  public static final int TYPE_SINT64 = 18;
  public static final int TYPE_STRING = 9;
  public static final int TYPE_UINT32 = 13;
  public static final int TYPE_UINT64 = 4;
  protected static final Charset UTF_8 = Charset.forName("UTF-8");
  
  private InternalNano() {}
  
  public static byte[] bytesDefaultValue(String paramString)
  {
    return paramString.getBytes(ISO_8859_1);
  }
  
  public static void cloneUnknownFieldData(ExtendableMessageNano paramExtendableMessageNano1, ExtendableMessageNano paramExtendableMessageNano2)
  {
    if (unknownFieldData != null) {
      unknownFieldData = unknownFieldData.clone();
    }
  }
  
  public static <K, V> int computeMapFieldSize(Map<K, V> paramMap, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j = CodedOutputByteBufferNano.computeTagSize(paramInt1);
    paramMap = paramMap.entrySet().iterator();
    paramInt1 = i;
    while (paramMap.hasNext())
    {
      Object localObject1 = (Map.Entry)paramMap.next();
      Object localObject2 = ((Map.Entry)localObject1).getKey();
      localObject1 = ((Map.Entry)localObject1).getValue();
      if ((localObject2 != null) && (localObject1 != null))
      {
        i = CodedOutputByteBufferNano.computeFieldSize(1, paramInt2, localObject2) + CodedOutputByteBufferNano.computeFieldSize(2, paramInt3, localObject1);
        paramInt1 += j + i + CodedOutputByteBufferNano.computeRawVarint32Size(i);
      }
      else
      {
        throw new IllegalStateException("keys and values in maps cannot be null");
      }
    }
    return paramInt1;
  }
  
  public static byte[] copyFromUtf8(String paramString)
  {
    return paramString.getBytes(UTF_8);
  }
  
  public static <K, V> boolean equals(Map<K, V> paramMap1, Map<K, V> paramMap2)
  {
    boolean bool1 = true;
    boolean bool2 = true;
    if (paramMap1 == paramMap2) {
      return true;
    }
    if (paramMap1 == null)
    {
      if (paramMap2.size() != 0) {
        bool2 = false;
      }
      return bool2;
    }
    if (paramMap2 == null)
    {
      if (paramMap1.size() == 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      return bool2;
    }
    if (paramMap1.size() != paramMap2.size()) {
      return false;
    }
    Iterator localIterator = paramMap1.entrySet().iterator();
    while (localIterator.hasNext())
    {
      paramMap1 = (Map.Entry)localIterator.next();
      if (!paramMap2.containsKey(paramMap1.getKey())) {
        return false;
      }
      if (!equalsMapValue(paramMap1.getValue(), paramMap2.get(paramMap1.getKey()))) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean equals(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    if ((paramArrayOfDouble1 != null) && (paramArrayOfDouble1.length != 0)) {
      return Arrays.equals(paramArrayOfDouble1, paramArrayOfDouble2);
    }
    boolean bool;
    if ((paramArrayOfDouble2 != null) && (paramArrayOfDouble2.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean equals(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if ((paramArrayOfFloat1 != null) && (paramArrayOfFloat1.length != 0)) {
      return Arrays.equals(paramArrayOfFloat1, paramArrayOfFloat2);
    }
    boolean bool;
    if ((paramArrayOfFloat2 != null) && (paramArrayOfFloat2.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean equals(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if ((paramArrayOfInt1 != null) && (paramArrayOfInt1.length != 0)) {
      return Arrays.equals(paramArrayOfInt1, paramArrayOfInt2);
    }
    boolean bool;
    if ((paramArrayOfInt2 != null) && (paramArrayOfInt2.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean equals(long[] paramArrayOfLong1, long[] paramArrayOfLong2)
  {
    if ((paramArrayOfLong1 != null) && (paramArrayOfLong1.length != 0)) {
      return Arrays.equals(paramArrayOfLong1, paramArrayOfLong2);
    }
    boolean bool;
    if ((paramArrayOfLong2 != null) && (paramArrayOfLong2.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean equals(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2)
  {
    int i = 0;
    int j;
    if (paramArrayOfObject1 == null) {
      j = 0;
    } else {
      j = paramArrayOfObject1.length;
    }
    int k = 0;
    int m;
    if (paramArrayOfObject2 == null) {
      m = 0;
    } else {
      m = paramArrayOfObject2.length;
    }
    for (;;)
    {
      int n = k;
      if (i < j)
      {
        n = k;
        if (paramArrayOfObject1[i] == null)
        {
          i++;
          continue;
        }
      }
      while ((n < m) && (paramArrayOfObject2[n] == null)) {
        n++;
      }
      if (i >= j) {
        k = 1;
      } else {
        k = 0;
      }
      int i1;
      if (n >= m) {
        i1 = 1;
      } else {
        i1 = 0;
      }
      if ((k != 0) && (i1 != 0)) {
        return true;
      }
      if (k != i1) {
        return false;
      }
      if (!paramArrayOfObject1[i].equals(paramArrayOfObject2[n])) {
        return false;
      }
      i++;
      k = n + 1;
    }
  }
  
  public static boolean equals(boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2)
  {
    if ((paramArrayOfBoolean1 != null) && (paramArrayOfBoolean1.length != 0)) {
      return Arrays.equals(paramArrayOfBoolean1, paramArrayOfBoolean2);
    }
    boolean bool;
    if ((paramArrayOfBoolean2 != null) && (paramArrayOfBoolean2.length != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean equals(byte[][] paramArrayOfByte1, byte[][] paramArrayOfByte2)
  {
    int i = 0;
    int j;
    if (paramArrayOfByte1 == null) {
      j = 0;
    } else {
      j = paramArrayOfByte1.length;
    }
    int k = 0;
    int m;
    if (paramArrayOfByte2 == null) {
      m = 0;
    } else {
      m = paramArrayOfByte2.length;
    }
    for (;;)
    {
      int n = k;
      if (i < j)
      {
        n = k;
        if (paramArrayOfByte1[i] == null)
        {
          i++;
          continue;
        }
      }
      while ((n < m) && (paramArrayOfByte2[n] == null)) {
        n++;
      }
      if (i >= j) {
        k = 1;
      } else {
        k = 0;
      }
      int i1;
      if (n >= m) {
        i1 = 1;
      } else {
        i1 = 0;
      }
      if ((k != 0) && (i1 != 0)) {
        return true;
      }
      if (k != i1) {
        return false;
      }
      if (!Arrays.equals(paramArrayOfByte1[i], paramArrayOfByte2[n])) {
        return false;
      }
      i++;
      k = n + 1;
    }
  }
  
  private static boolean equalsMapValue(Object paramObject1, Object paramObject2)
  {
    if ((paramObject1 != null) && (paramObject2 != null))
    {
      if (((paramObject1 instanceof byte[])) && ((paramObject2 instanceof byte[]))) {
        return Arrays.equals((byte[])paramObject1, (byte[])paramObject2);
      }
      return paramObject1.equals(paramObject2);
    }
    throw new IllegalStateException("keys and values in maps cannot be null");
  }
  
  public static <K, V> int hashCode(Map<K, V> paramMap)
  {
    if (paramMap == null) {
      return 0;
    }
    int i = 0;
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      i += (hashCodeForMap(localEntry.getKey()) ^ hashCodeForMap(localEntry.getValue()));
    }
    return i;
  }
  
  public static int hashCode(double[] paramArrayOfDouble)
  {
    int i;
    if ((paramArrayOfDouble != null) && (paramArrayOfDouble.length != 0)) {
      i = Arrays.hashCode(paramArrayOfDouble);
    } else {
      i = 0;
    }
    return i;
  }
  
  public static int hashCode(float[] paramArrayOfFloat)
  {
    int i;
    if ((paramArrayOfFloat != null) && (paramArrayOfFloat.length != 0)) {
      i = Arrays.hashCode(paramArrayOfFloat);
    } else {
      i = 0;
    }
    return i;
  }
  
  public static int hashCode(int[] paramArrayOfInt)
  {
    int i;
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length != 0)) {
      i = Arrays.hashCode(paramArrayOfInt);
    } else {
      i = 0;
    }
    return i;
  }
  
  public static int hashCode(long[] paramArrayOfLong)
  {
    int i;
    if ((paramArrayOfLong != null) && (paramArrayOfLong.length != 0)) {
      i = Arrays.hashCode(paramArrayOfLong);
    } else {
      i = 0;
    }
    return i;
  }
  
  public static int hashCode(Object[] paramArrayOfObject)
  {
    int i = 0;
    int j = 0;
    int k;
    if (paramArrayOfObject == null) {
      k = 0;
    } else {
      k = paramArrayOfObject.length;
    }
    while (j < k)
    {
      Object localObject = paramArrayOfObject[j];
      int m = i;
      if (localObject != null) {
        m = 31 * i + localObject.hashCode();
      }
      j++;
      i = m;
    }
    return i;
  }
  
  public static int hashCode(boolean[] paramArrayOfBoolean)
  {
    int i;
    if ((paramArrayOfBoolean != null) && (paramArrayOfBoolean.length != 0)) {
      i = Arrays.hashCode(paramArrayOfBoolean);
    } else {
      i = 0;
    }
    return i;
  }
  
  public static int hashCode(byte[][] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    int k;
    if (paramArrayOfByte == null) {
      k = 0;
    } else {
      k = paramArrayOfByte.length;
    }
    while (j < k)
    {
      byte[] arrayOfByte = paramArrayOfByte[j];
      int m = i;
      if (arrayOfByte != null) {
        m = 31 * i + Arrays.hashCode(arrayOfByte);
      }
      j++;
      i = m;
    }
    return i;
  }
  
  private static int hashCodeForMap(Object paramObject)
  {
    if ((paramObject instanceof byte[])) {
      return Arrays.hashCode((byte[])paramObject);
    }
    return paramObject.hashCode();
  }
  
  public static final <K, V> Map<K, V> mergeMapEntry(CodedInputByteBufferNano paramCodedInputByteBufferNano, Map<K, V> paramMap, MapFactories.MapFactory paramMapFactory, int paramInt1, int paramInt2, V paramV, int paramInt3, int paramInt4)
    throws IOException
  {
    Map localMap = paramMapFactory.forMap(paramMap);
    int i = paramCodedInputByteBufferNano.pushLimit(paramCodedInputByteBufferNano.readRawVarint32());
    paramMap = null;
    for (;;)
    {
      int j = paramCodedInputByteBufferNano.readTag();
      Object localObject;
      if (j != 0)
      {
        if (j == paramInt3)
        {
          paramMapFactory = paramCodedInputByteBufferNano.readPrimitiveField(paramInt1);
          localObject = paramV;
        }
        else if (j == paramInt4)
        {
          if (paramInt2 == 11)
          {
            paramCodedInputByteBufferNano.readMessage((MessageNano)paramV);
            paramMapFactory = paramMap;
            localObject = paramV;
          }
          else
          {
            localObject = paramCodedInputByteBufferNano.readPrimitiveField(paramInt2);
            paramMapFactory = paramMap;
          }
        }
        else
        {
          paramMapFactory = paramMap;
          localObject = paramV;
          if (paramCodedInputByteBufferNano.skipField(j)) {}
        }
      }
      else
      {
        paramCodedInputByteBufferNano.checkLastTagWas(0);
        paramCodedInputByteBufferNano.popLimit(i);
        paramCodedInputByteBufferNano = paramMap;
        if (paramMap == null) {
          paramCodedInputByteBufferNano = primitiveDefaultValue(paramInt1);
        }
        paramMap = paramV;
        if (paramV == null) {
          paramMap = primitiveDefaultValue(paramInt2);
        }
        localMap.put(paramCodedInputByteBufferNano, paramMap);
        return localMap;
      }
      paramMap = paramMapFactory;
      paramV = (TV)localObject;
    }
  }
  
  private static Object primitiveDefaultValue(int paramInt)
  {
    switch (paramInt)
    {
    case 10: 
    case 11: 
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Type: ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" is not a primitive type.");
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 12: 
      return WireFormatNano.EMPTY_BYTES;
    case 9: 
      return "";
    case 8: 
      return Boolean.FALSE;
    case 5: 
    case 7: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
      return Integer.valueOf(0);
    case 3: 
    case 4: 
    case 6: 
    case 16: 
    case 18: 
      return Long.valueOf(0L);
    case 2: 
      return Float.valueOf(0.0F);
    }
    return Double.valueOf(0.0D);
  }
  
  public static <K, V> void serializeMapField(CodedOutputByteBufferNano paramCodedOutputByteBufferNano, Map<K, V> paramMap, int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Object localObject1 = (Map.Entry)paramMap.next();
      Object localObject2 = ((Map.Entry)localObject1).getKey();
      localObject1 = ((Map.Entry)localObject1).getValue();
      if ((localObject2 != null) && (localObject1 != null))
      {
        int i = CodedOutputByteBufferNano.computeFieldSize(1, paramInt2, localObject2);
        int j = CodedOutputByteBufferNano.computeFieldSize(2, paramInt3, localObject1);
        paramCodedOutputByteBufferNano.writeTag(paramInt1, 2);
        paramCodedOutputByteBufferNano.writeRawVarint32(i + j);
        paramCodedOutputByteBufferNano.writeField(1, paramInt2, localObject2);
        paramCodedOutputByteBufferNano.writeField(2, paramInt3, localObject1);
      }
      else
      {
        throw new IllegalStateException("keys and values in maps cannot be null");
      }
    }
  }
  
  public static String stringDefaultValue(String paramString)
  {
    return new String(paramString.getBytes(ISO_8859_1), UTF_8);
  }
}
