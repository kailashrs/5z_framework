package com.android.internal.telephony.protobuf.nano;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class FieldData
  implements Cloneable
{
  private Extension<?, ?> cachedExtension;
  private List<UnknownFieldData> unknownFieldData;
  private Object value;
  
  FieldData()
  {
    unknownFieldData = new ArrayList();
  }
  
  <T> FieldData(Extension<?, T> paramExtension, T paramT)
  {
    cachedExtension = paramExtension;
    value = paramT;
  }
  
  private byte[] toByteArray()
    throws IOException
  {
    byte[] arrayOfByte = new byte[computeSerializedSize()];
    writeTo(CodedOutputByteBufferNano.newInstance(arrayOfByte));
    return arrayOfByte;
  }
  
  void addUnknownField(UnknownFieldData paramUnknownFieldData)
  {
    unknownFieldData.add(paramUnknownFieldData);
  }
  
  public final FieldData clone()
  {
    FieldData localFieldData = new FieldData();
    try
    {
      cachedExtension = cachedExtension;
      if (unknownFieldData == null) {
        unknownFieldData = null;
      } else {
        unknownFieldData.addAll(unknownFieldData);
      }
      if (value != null) {
        if ((value instanceof MessageNano))
        {
          value = ((MessageNano)value).clone();
        }
        else if ((value instanceof byte[]))
        {
          value = ((byte[])value).clone();
        }
        else
        {
          boolean bool = value instanceof byte[][];
          int i = 0;
          int j = 0;
          Object localObject1;
          Object localObject2;
          if (bool)
          {
            localObject1 = (byte[][])value;
            localObject2 = new byte[localObject1.length][];
            value = localObject2;
            while (j < localObject1.length)
            {
              localObject2[j] = ((byte[])localObject1[j].clone());
              j++;
            }
          }
          else if ((value instanceof boolean[]))
          {
            value = ((boolean[])value).clone();
          }
          else if ((value instanceof int[]))
          {
            value = ((int[])value).clone();
          }
          else if ((value instanceof long[]))
          {
            value = ((long[])value).clone();
          }
          else if ((value instanceof float[]))
          {
            value = ((float[])value).clone();
          }
          else if ((value instanceof double[]))
          {
            value = ((double[])value).clone();
          }
          else if ((value instanceof MessageNano[]))
          {
            localObject2 = (MessageNano[])value;
            localObject1 = new MessageNano[localObject2.length];
            value = localObject1;
            for (j = i; j < localObject2.length; j++) {
              localObject1[j] = localObject2[j].clone();
            }
          }
        }
      }
      return localFieldData;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new AssertionError(localCloneNotSupportedException);
    }
  }
  
  int computeSerializedSize()
  {
    int i = 0;
    int j;
    if (value != null)
    {
      j = cachedExtension.computeSerializedSize(value);
    }
    else
    {
      Iterator localIterator = unknownFieldData.iterator();
      for (;;)
      {
        j = i;
        if (!localIterator.hasNext()) {
          break;
        }
        i += ((UnknownFieldData)localIterator.next()).computeSerializedSize();
      }
    }
    return j;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof FieldData)) {
      return false;
    }
    paramObject = (FieldData)paramObject;
    if ((value != null) && (value != null))
    {
      if (cachedExtension != cachedExtension) {
        return false;
      }
      if (!cachedExtension.clazz.isArray()) {
        return value.equals(value);
      }
      if ((value instanceof byte[])) {
        return Arrays.equals((byte[])value, (byte[])value);
      }
      if ((value instanceof int[])) {
        return Arrays.equals((int[])value, (int[])value);
      }
      if ((value instanceof long[])) {
        return Arrays.equals((long[])value, (long[])value);
      }
      if ((value instanceof float[])) {
        return Arrays.equals((float[])value, (float[])value);
      }
      if ((value instanceof double[])) {
        return Arrays.equals((double[])value, (double[])value);
      }
      if ((value instanceof boolean[])) {
        return Arrays.equals((boolean[])value, (boolean[])value);
      }
      return Arrays.deepEquals((Object[])value, (Object[])value);
    }
    if ((unknownFieldData != null) && (unknownFieldData != null)) {
      return unknownFieldData.equals(unknownFieldData);
    }
    try
    {
      boolean bool = Arrays.equals(toByteArray(), paramObject.toByteArray());
      return bool;
    }
    catch (IOException paramObject)
    {
      throw new IllegalStateException(paramObject);
    }
  }
  
  UnknownFieldData getUnknownField(int paramInt)
  {
    if (unknownFieldData == null) {
      return null;
    }
    if (paramInt < unknownFieldData.size()) {
      return (UnknownFieldData)unknownFieldData.get(paramInt);
    }
    return null;
  }
  
  int getUnknownFieldSize()
  {
    if (unknownFieldData == null) {
      return 0;
    }
    return unknownFieldData.size();
  }
  
  <T> T getValue(Extension<?, T> paramExtension)
  {
    if (value != null)
    {
      if (cachedExtension != paramExtension) {
        throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
      }
    }
    else
    {
      cachedExtension = paramExtension;
      value = paramExtension.getValueFrom(unknownFieldData);
      unknownFieldData = null;
    }
    return value;
  }
  
  public int hashCode()
  {
    try
    {
      int i = Arrays.hashCode(toByteArray());
      return 31 * 17 + i;
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException(localIOException);
    }
  }
  
  <T> void setValue(Extension<?, T> paramExtension, T paramT)
  {
    cachedExtension = paramExtension;
    value = paramT;
    unknownFieldData = null;
  }
  
  void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
    throws IOException
  {
    if (value != null)
    {
      cachedExtension.writeTo(value, paramCodedOutputByteBufferNano);
    }
    else
    {
      Iterator localIterator = unknownFieldData.iterator();
      while (localIterator.hasNext()) {
        ((UnknownFieldData)localIterator.next()).writeTo(paramCodedOutputByteBufferNano);
      }
    }
  }
}
