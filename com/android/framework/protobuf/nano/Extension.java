package com.android.framework.protobuf.nano;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Extension<M extends ExtendableMessageNano<M>, T>
{
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
  protected final Class<T> clazz;
  protected final boolean repeated;
  public final int tag;
  protected final int type;
  
  private Extension(int paramInt1, Class<T> paramClass, int paramInt2, boolean paramBoolean)
  {
    type = paramInt1;
    clazz = paramClass;
    tag = paramInt2;
    repeated = paramBoolean;
  }
  
  @Deprecated
  public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int paramInt1, Class<T> paramClass, int paramInt2)
  {
    return new Extension(paramInt1, paramClass, paramInt2, false);
  }
  
  public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int paramInt, Class<T> paramClass, long paramLong)
  {
    return new Extension(paramInt, paramClass, (int)paramLong, false);
  }
  
  public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createPrimitiveTyped(int paramInt, Class<T> paramClass, long paramLong)
  {
    return new PrimitiveExtension(paramInt, paramClass, (int)paramLong, false, 0, 0);
  }
  
  public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T[]> createRepeatedMessageTyped(int paramInt, Class<T[]> paramClass, long paramLong)
  {
    return new Extension(paramInt, paramClass, (int)paramLong, true);
  }
  
  public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createRepeatedPrimitiveTyped(int paramInt, Class<T> paramClass, long paramLong1, long paramLong2, long paramLong3)
  {
    return new PrimitiveExtension(paramInt, paramClass, (int)paramLong1, true, (int)paramLong2, (int)paramLong3);
  }
  
  private T getRepeatedValueFrom(List<UnknownFieldData> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    for (int j = 0; j < paramList.size(); j++)
    {
      UnknownFieldData localUnknownFieldData = (UnknownFieldData)paramList.get(j);
      if (bytes.length != 0) {
        readDataInto(localUnknownFieldData, localArrayList);
      }
    }
    int k = localArrayList.size();
    if (k == 0) {
      return null;
    }
    paramList = clazz.cast(Array.newInstance(clazz.getComponentType(), k));
    for (j = i; j < k; j++) {
      Array.set(paramList, j, localArrayList.get(j));
    }
    return paramList;
  }
  
  private T getSingularValueFrom(List<UnknownFieldData> paramList)
  {
    if (paramList.isEmpty()) {
      return null;
    }
    paramList = (UnknownFieldData)paramList.get(paramList.size() - 1);
    return clazz.cast(readData(CodedInputByteBufferNano.newInstance(bytes)));
  }
  
  protected int computeRepeatedSerializedSize(Object paramObject)
  {
    int i = 0;
    int j = Array.getLength(paramObject);
    int k = 0;
    while (k < j)
    {
      int m = i;
      if (Array.get(paramObject, k) != null) {
        m = i + computeSingularSerializedSize(Array.get(paramObject, k));
      }
      k++;
      i = m;
    }
    return i;
  }
  
  int computeSerializedSize(Object paramObject)
  {
    if (repeated) {
      return computeRepeatedSerializedSize(paramObject);
    }
    return computeSingularSerializedSize(paramObject);
  }
  
  protected int computeSingularSerializedSize(Object paramObject)
  {
    int i = WireFormatNano.getTagFieldNumber(tag);
    switch (type)
    {
    default: 
      paramObject = new StringBuilder();
      paramObject.append("Unknown type ");
      paramObject.append(type);
      throw new IllegalArgumentException(paramObject.toString());
    case 11: 
      return CodedOutputByteBufferNano.computeMessageSize(i, (MessageNano)paramObject);
    }
    return CodedOutputByteBufferNano.computeGroupSize(i, (MessageNano)paramObject);
  }
  
  final T getValueFrom(List<UnknownFieldData> paramList)
  {
    if (paramList == null) {
      return null;
    }
    if (repeated) {
      paramList = getRepeatedValueFrom(paramList);
    } else {
      paramList = getSingularValueFrom(paramList);
    }
    return paramList;
  }
  
  protected Object readData(CodedInputByteBufferNano paramCodedInputByteBufferNano)
  {
    Class localClass;
    if (repeated) {
      localClass = clazz.getComponentType();
    } else {
      localClass = clazz;
    }
    try
    {
      Object localObject;
      switch (type)
      {
      default: 
        localObject = new java/lang/IllegalArgumentException;
        break;
      case 11: 
        localObject = (MessageNano)localClass.newInstance();
        paramCodedInputByteBufferNano.readMessage((MessageNano)localObject);
        return localObject;
      case 10: 
        localObject = (MessageNano)localClass.newInstance();
        paramCodedInputByteBufferNano.readGroup((MessageNano)localObject, WireFormatNano.getTagFieldNumber(tag));
        return localObject;
      }
      paramCodedInputByteBufferNano = new java/lang/StringBuilder;
      paramCodedInputByteBufferNano.<init>();
      paramCodedInputByteBufferNano.append("Unknown type ");
      paramCodedInputByteBufferNano.append(type);
      ((IllegalArgumentException)localObject).<init>(paramCodedInputByteBufferNano.toString());
      throw ((Throwable)localObject);
    }
    catch (IOException paramCodedInputByteBufferNano)
    {
      throw new IllegalArgumentException("Error reading extension field", paramCodedInputByteBufferNano);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      paramCodedInputByteBufferNano = new StringBuilder();
      paramCodedInputByteBufferNano.append("Error creating instance of class ");
      paramCodedInputByteBufferNano.append(localClass);
      throw new IllegalArgumentException(paramCodedInputByteBufferNano.toString(), localIllegalAccessException);
    }
    catch (InstantiationException localInstantiationException)
    {
      paramCodedInputByteBufferNano = new StringBuilder();
      paramCodedInputByteBufferNano.append("Error creating instance of class ");
      paramCodedInputByteBufferNano.append(localClass);
      throw new IllegalArgumentException(paramCodedInputByteBufferNano.toString(), localInstantiationException);
    }
  }
  
  protected void readDataInto(UnknownFieldData paramUnknownFieldData, List<Object> paramList)
  {
    paramList.add(readData(CodedInputByteBufferNano.newInstance(bytes)));
  }
  
  protected void writeRepeatedData(Object paramObject, CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
  {
    int i = Array.getLength(paramObject);
    for (int j = 0; j < i; j++)
    {
      Object localObject = Array.get(paramObject, j);
      if (localObject != null) {
        writeSingularData(localObject, paramCodedOutputByteBufferNano);
      }
    }
  }
  
  protected void writeSingularData(Object paramObject, CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
  {
    try
    {
      paramCodedOutputByteBufferNano.writeRawVarint32(tag);
      switch (type)
      {
      default: 
        paramObject = new java/lang/IllegalArgumentException;
        break;
      case 11: 
        paramCodedOutputByteBufferNano.writeMessageNoTag((MessageNano)paramObject);
        break;
      case 10: 
        paramObject = (MessageNano)paramObject;
        int i = WireFormatNano.getTagFieldNumber(tag);
        paramCodedOutputByteBufferNano.writeGroupNoTag(paramObject);
        paramCodedOutputByteBufferNano.writeTag(i, 4);
      }
      return;
      paramCodedOutputByteBufferNano = new java/lang/StringBuilder;
      paramCodedOutputByteBufferNano.<init>();
      paramCodedOutputByteBufferNano.append("Unknown type ");
      paramCodedOutputByteBufferNano.append(type);
      paramObject.<init>(paramCodedOutputByteBufferNano.toString());
      throw paramObject;
    }
    catch (IOException paramObject)
    {
      throw new IllegalStateException(paramObject);
    }
  }
  
  void writeTo(Object paramObject, CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
    throws IOException
  {
    if (repeated) {
      writeRepeatedData(paramObject, paramCodedOutputByteBufferNano);
    } else {
      writeSingularData(paramObject, paramCodedOutputByteBufferNano);
    }
  }
  
  private static class PrimitiveExtension<M extends ExtendableMessageNano<M>, T>
    extends Extension<M, T>
  {
    private final int nonPackedTag;
    private final int packedTag;
    
    public PrimitiveExtension(int paramInt1, Class<T> paramClass, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4)
    {
      super(paramClass, paramInt2, paramBoolean, null);
      nonPackedTag = paramInt3;
      packedTag = paramInt4;
    }
    
    private int computePackedDataSize(Object paramObject)
    {
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      int i1 = 0;
      int i2 = 0;
      int i3 = Array.getLength(paramObject);
      int i4 = type;
      int i5 = 0;
      int i6 = 0;
      int i7 = 0;
      int i8 = 0;
      int i9 = 0;
      int i10 = 0;
      int i11 = 0;
      switch (i4)
      {
      default: 
        switch (i4)
        {
        default: 
          paramObject = new StringBuilder();
          paramObject.append("Unexpected non-packable type ");
          paramObject.append(type);
          throw new IllegalArgumentException(paramObject.toString());
        case 18: 
          while (i11 < i3)
          {
            i2 += CodedOutputByteBufferNano.computeSInt64SizeNoTag(Array.getLong(paramObject, i11));
            i11++;
          }
          break;
        case 17: 
          i11 = i5;
          i2 = i;
          while (i11 < i3)
          {
            i2 += CodedOutputByteBufferNano.computeSInt32SizeNoTag(Array.getInt(paramObject, i11));
            i11++;
          }
          break;
        case 14: 
          i11 = i6;
          i2 = j;
          while (i11 < i3)
          {
            i2 += CodedOutputByteBufferNano.computeEnumSizeNoTag(Array.getInt(paramObject, i11));
            i11++;
          }
          break;
        case 13: 
          i11 = i7;
          i2 = k;
          while (i11 < i3)
          {
            i2 += CodedOutputByteBufferNano.computeUInt32SizeNoTag(Array.getInt(paramObject, i11));
            i11++;
          }
        }
        break;
      case 8: 
        i2 = i3;
        break;
      case 5: 
        i11 = i8;
        i2 = m;
        while (i11 < i3)
        {
          i2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(Array.getInt(paramObject, i11));
          i11++;
        }
        break;
      case 4: 
        i11 = i9;
        i2 = n;
        while (i11 < i3)
        {
          i2 += CodedOutputByteBufferNano.computeUInt64SizeNoTag(Array.getLong(paramObject, i11));
          i11++;
        }
        break;
      case 3: 
        i11 = i10;
        i2 = i1;
        while (i11 < i3)
        {
          i2 += CodedOutputByteBufferNano.computeInt64SizeNoTag(Array.getLong(paramObject, i11));
          i11++;
        }
        break;
      case 2: 
      case 7: 
        i2 = i3 * 4;
        break;
      case 1: 
      case 6: 
        i2 = i3 * 8;
      }
      return i2;
    }
    
    protected int computeRepeatedSerializedSize(Object paramObject)
    {
      if (tag == nonPackedTag) {
        return super.computeRepeatedSerializedSize(paramObject);
      }
      if (tag == packedTag)
      {
        int i = computePackedDataSize(paramObject);
        int j = CodedOutputByteBufferNano.computeRawVarint32Size(i);
        return CodedOutputByteBufferNano.computeRawVarint32Size(tag) + (j + i);
      }
      paramObject = new StringBuilder();
      paramObject.append("Unexpected repeated extension tag ");
      paramObject.append(tag);
      paramObject.append(", unequal to both non-packed variant ");
      paramObject.append(nonPackedTag);
      paramObject.append(" and packed variant ");
      paramObject.append(packedTag);
      throw new IllegalArgumentException(paramObject.toString());
    }
    
    protected final int computeSingularSerializedSize(Object paramObject)
    {
      int i = WireFormatNano.getTagFieldNumber(tag);
      switch (type)
      {
      case 10: 
      case 11: 
      default: 
        paramObject = new StringBuilder();
        paramObject.append("Unknown type ");
        paramObject.append(type);
        throw new IllegalArgumentException(paramObject.toString());
      case 18: 
        return CodedOutputByteBufferNano.computeSInt64Size(i, ((Long)paramObject).longValue());
      case 17: 
        return CodedOutputByteBufferNano.computeSInt32Size(i, ((Integer)paramObject).intValue());
      case 16: 
        paramObject = (Long)paramObject;
        return CodedOutputByteBufferNano.computeSFixed64Size(i, paramObject.longValue());
      case 15: 
        paramObject = (Integer)paramObject;
        return CodedOutputByteBufferNano.computeSFixed32Size(i, paramObject.intValue());
      case 14: 
        return CodedOutputByteBufferNano.computeEnumSize(i, ((Integer)paramObject).intValue());
      case 13: 
        return CodedOutputByteBufferNano.computeUInt32Size(i, ((Integer)paramObject).intValue());
      case 12: 
        return CodedOutputByteBufferNano.computeBytesSize(i, (byte[])paramObject);
      case 9: 
        return CodedOutputByteBufferNano.computeStringSize(i, (String)paramObject);
      case 8: 
        return CodedOutputByteBufferNano.computeBoolSize(i, ((Boolean)paramObject).booleanValue());
      case 7: 
        return CodedOutputByteBufferNano.computeFixed32Size(i, ((Integer)paramObject).intValue());
      case 6: 
        return CodedOutputByteBufferNano.computeFixed64Size(i, ((Long)paramObject).longValue());
      case 5: 
        return CodedOutputByteBufferNano.computeInt32Size(i, ((Integer)paramObject).intValue());
      case 4: 
        return CodedOutputByteBufferNano.computeUInt64Size(i, ((Long)paramObject).longValue());
      case 3: 
        return CodedOutputByteBufferNano.computeInt64Size(i, ((Long)paramObject).longValue());
      case 2: 
        return CodedOutputByteBufferNano.computeFloatSize(i, ((Float)paramObject).floatValue());
      }
      return CodedOutputByteBufferNano.computeDoubleSize(i, ((Double)paramObject).doubleValue());
    }
    
    protected Object readData(CodedInputByteBufferNano paramCodedInputByteBufferNano)
    {
      try
      {
        paramCodedInputByteBufferNano = paramCodedInputByteBufferNano.readPrimitiveField(type);
        return paramCodedInputByteBufferNano;
      }
      catch (IOException paramCodedInputByteBufferNano)
      {
        throw new IllegalArgumentException("Error reading extension field", paramCodedInputByteBufferNano);
      }
    }
    
    protected void readDataInto(UnknownFieldData paramUnknownFieldData, List<Object> paramList)
    {
      if (tag == nonPackedTag) {
        paramList.add(readData(CodedInputByteBufferNano.newInstance(bytes)));
      } else {
        paramUnknownFieldData = CodedInputByteBufferNano.newInstance(bytes);
      }
      try
      {
        paramUnknownFieldData.pushLimit(paramUnknownFieldData.readRawVarint32());
        while (!paramUnknownFieldData.isAtEnd()) {
          paramList.add(readData(paramUnknownFieldData));
        }
        return;
      }
      catch (IOException paramUnknownFieldData)
      {
        throw new IllegalArgumentException("Error reading extension field", paramUnknownFieldData);
      }
    }
    
    protected void writeRepeatedData(Object paramObject, CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
    {
      int i;
      int j;
      if (tag == nonPackedTag)
      {
        super.writeRepeatedData(paramObject, paramCodedOutputByteBufferNano);
      }
      else
      {
        if (tag != packedTag) {
          break label641;
        }
        i = Array.getLength(paramObject);
        j = computePackedDataSize(paramObject);
      }
      try
      {
        paramCodedOutputByteBufferNano.writeRawVarint32(tag);
        paramCodedOutputByteBufferNano.writeRawVarint32(j);
        int k = type;
        int m = 0;
        int n = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        j = 0;
        switch (k)
        {
        default: 
          switch (k)
          {
          default: 
            paramObject = new java/lang/IllegalArgumentException;
            paramCodedOutputByteBufferNano = new java/lang/StringBuilder;
            paramCodedOutputByteBufferNano.<init>();
            paramCodedOutputByteBufferNano.append("Unpackable type ");
            paramCodedOutputByteBufferNano.append(type);
            paramObject.<init>(paramCodedOutputByteBufferNano.toString());
            throw paramObject;
          case 18: 
            while (j < i)
            {
              paramCodedOutputByteBufferNano.writeSInt64NoTag(Array.getLong(paramObject, j));
              j++;
            }
            break;
          case 17: 
            for (j = m; j < i; j++) {
              paramCodedOutputByteBufferNano.writeSInt32NoTag(Array.getInt(paramObject, j));
            }
            break;
          case 16: 
            for (j = n; j < i; j++) {
              paramCodedOutputByteBufferNano.writeSFixed64NoTag(Array.getLong(paramObject, j));
            }
            break;
          case 15: 
            for (j = i1; j < i; j++) {
              paramCodedOutputByteBufferNano.writeSFixed32NoTag(Array.getInt(paramObject, j));
            }
            break;
          case 14: 
            for (j = i2; j < i; j++) {
              paramCodedOutputByteBufferNano.writeEnumNoTag(Array.getInt(paramObject, j));
            }
            break;
          case 13: 
            for (j = i3; j < i; j++) {
              paramCodedOutputByteBufferNano.writeUInt32NoTag(Array.getInt(paramObject, j));
            }
          }
          break;
        case 8: 
          for (j = i4; j < i; j++) {
            paramCodedOutputByteBufferNano.writeBoolNoTag(Array.getBoolean(paramObject, j));
          }
          break;
        case 7: 
          for (j = i5; j < i; j++) {
            paramCodedOutputByteBufferNano.writeFixed32NoTag(Array.getInt(paramObject, j));
          }
          break;
        case 6: 
          for (j = i6; j < i; j++) {
            paramCodedOutputByteBufferNano.writeFixed64NoTag(Array.getLong(paramObject, j));
          }
          break;
        case 5: 
          for (j = i7; j < i; j++) {
            paramCodedOutputByteBufferNano.writeInt32NoTag(Array.getInt(paramObject, j));
          }
          break;
        case 4: 
          for (j = i8; j < i; j++) {
            paramCodedOutputByteBufferNano.writeUInt64NoTag(Array.getLong(paramObject, j));
          }
          break;
        case 3: 
          for (j = i9; j < i; j++) {
            paramCodedOutputByteBufferNano.writeInt64NoTag(Array.getLong(paramObject, j));
          }
          break;
        case 2: 
          for (j = i10; j < i; j++) {
            paramCodedOutputByteBufferNano.writeFloatNoTag(Array.getFloat(paramObject, j));
          }
          break;
        case 1: 
          for (j = i11; j < i; j++) {
            paramCodedOutputByteBufferNano.writeDoubleNoTag(Array.getDouble(paramObject, j));
          }
        }
        return;
      }
      catch (IOException paramObject)
      {
        throw new IllegalStateException(paramObject);
      }
      label641:
      paramObject = new StringBuilder();
      paramObject.append("Unexpected repeated extension tag ");
      paramObject.append(tag);
      paramObject.append(", unequal to both non-packed variant ");
      paramObject.append(nonPackedTag);
      paramObject.append(" and packed variant ");
      paramObject.append(packedTag);
      throw new IllegalArgumentException(paramObject.toString());
    }
    
    protected final void writeSingularData(Object paramObject, CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
    {
      try
      {
        paramCodedOutputByteBufferNano.writeRawVarint32(tag);
        switch (type)
        {
        case 10: 
        case 11: 
        default: 
          paramObject = new java/lang/IllegalArgumentException;
          break;
        case 18: 
          paramCodedOutputByteBufferNano.writeSInt64NoTag(((Long)paramObject).longValue());
          break;
        case 17: 
          paramCodedOutputByteBufferNano.writeSInt32NoTag(((Integer)paramObject).intValue());
          break;
        case 16: 
          paramCodedOutputByteBufferNano.writeSFixed64NoTag(((Long)paramObject).longValue());
          break;
        case 15: 
          paramCodedOutputByteBufferNano.writeSFixed32NoTag(((Integer)paramObject).intValue());
          break;
        case 14: 
          paramCodedOutputByteBufferNano.writeEnumNoTag(((Integer)paramObject).intValue());
          break;
        case 13: 
          paramCodedOutputByteBufferNano.writeUInt32NoTag(((Integer)paramObject).intValue());
          break;
        case 12: 
          paramCodedOutputByteBufferNano.writeBytesNoTag((byte[])paramObject);
          break;
        case 9: 
          paramCodedOutputByteBufferNano.writeStringNoTag((String)paramObject);
          break;
        case 8: 
          paramCodedOutputByteBufferNano.writeBoolNoTag(((Boolean)paramObject).booleanValue());
          break;
        case 7: 
          paramCodedOutputByteBufferNano.writeFixed32NoTag(((Integer)paramObject).intValue());
          break;
        case 6: 
          paramCodedOutputByteBufferNano.writeFixed64NoTag(((Long)paramObject).longValue());
          break;
        case 5: 
          paramCodedOutputByteBufferNano.writeInt32NoTag(((Integer)paramObject).intValue());
          break;
        case 4: 
          paramCodedOutputByteBufferNano.writeUInt64NoTag(((Long)paramObject).longValue());
          break;
        case 3: 
          paramCodedOutputByteBufferNano.writeInt64NoTag(((Long)paramObject).longValue());
          break;
        case 2: 
          paramCodedOutputByteBufferNano.writeFloatNoTag(((Float)paramObject).floatValue());
          break;
        case 1: 
          paramCodedOutputByteBufferNano.writeDoubleNoTag(((Double)paramObject).doubleValue());
        }
        return;
        paramCodedOutputByteBufferNano = new java/lang/StringBuilder;
        paramCodedOutputByteBufferNano.<init>();
        paramCodedOutputByteBufferNano.append("Unknown type ");
        paramCodedOutputByteBufferNano.append(type);
        paramObject.<init>(paramCodedOutputByteBufferNano.toString());
        throw paramObject;
      }
      catch (IOException paramObject)
      {
        throw new IllegalStateException(paramObject);
      }
    }
  }
}
