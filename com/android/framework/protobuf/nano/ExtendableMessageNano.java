package com.android.framework.protobuf.nano;

import java.io.IOException;

public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>>
  extends MessageNano
{
  protected FieldArray unknownFieldData;
  
  public ExtendableMessageNano() {}
  
  public M clone()
    throws CloneNotSupportedException
  {
    ExtendableMessageNano localExtendableMessageNano = (ExtendableMessageNano)super.clone();
    InternalNano.cloneUnknownFieldData(this, localExtendableMessageNano);
    return localExtendableMessageNano;
  }
  
  protected int computeSerializedSize()
  {
    int i = 0;
    int j = 0;
    if (unknownFieldData != null) {
      for (int k = 0;; k++)
      {
        i = j;
        if (k >= unknownFieldData.size()) {
          break;
        }
        j += unknownFieldData.dataAt(k).computeSerializedSize();
      }
    }
    return i;
  }
  
  public final <T> T getExtension(Extension<M, T> paramExtension)
  {
    Object localObject1 = unknownFieldData;
    Object localObject2 = null;
    if (localObject1 == null) {
      return null;
    }
    localObject1 = unknownFieldData.get(WireFormatNano.getTagFieldNumber(tag));
    if (localObject1 == null) {
      paramExtension = localObject2;
    } else {
      paramExtension = ((FieldData)localObject1).getValue(paramExtension);
    }
    return paramExtension;
  }
  
  public final boolean hasExtension(Extension<M, ?> paramExtension)
  {
    FieldArray localFieldArray = unknownFieldData;
    boolean bool = false;
    if (localFieldArray == null) {
      return false;
    }
    if (unknownFieldData.get(WireFormatNano.getTagFieldNumber(tag)) != null) {
      bool = true;
    }
    return bool;
  }
  
  public final <T> M setExtension(Extension<M, T> paramExtension, T paramT)
  {
    int i = WireFormatNano.getTagFieldNumber(tag);
    if (paramT == null)
    {
      if (unknownFieldData != null)
      {
        unknownFieldData.remove(i);
        if (unknownFieldData.isEmpty()) {
          unknownFieldData = null;
        }
      }
    }
    else
    {
      FieldData localFieldData = null;
      if (unknownFieldData == null) {
        unknownFieldData = new FieldArray();
      } else {
        localFieldData = unknownFieldData.get(i);
      }
      if (localFieldData == null) {
        unknownFieldData.put(i, new FieldData(paramExtension, paramT));
      } else {
        localFieldData.setValue(paramExtension, paramT);
      }
    }
    return this;
  }
  
  protected final boolean storeUnknownField(CodedInputByteBufferNano paramCodedInputByteBufferNano, int paramInt)
    throws IOException
  {
    int i = paramCodedInputByteBufferNano.getPosition();
    if (!paramCodedInputByteBufferNano.skipField(paramInt)) {
      return false;
    }
    int j = WireFormatNano.getTagFieldNumber(paramInt);
    UnknownFieldData localUnknownFieldData = new UnknownFieldData(paramInt, paramCodedInputByteBufferNano.getData(i, paramCodedInputByteBufferNano.getPosition() - i));
    paramCodedInputByteBufferNano = null;
    if (unknownFieldData == null) {
      unknownFieldData = new FieldArray();
    } else {
      paramCodedInputByteBufferNano = unknownFieldData.get(j);
    }
    Object localObject = paramCodedInputByteBufferNano;
    if (paramCodedInputByteBufferNano == null)
    {
      localObject = new FieldData();
      unknownFieldData.put(j, (FieldData)localObject);
    }
    ((FieldData)localObject).addUnknownField(localUnknownFieldData);
    return true;
  }
  
  public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
    throws IOException
  {
    if (unknownFieldData == null) {
      return;
    }
    for (int i = 0; i < unknownFieldData.size(); i++) {
      unknownFieldData.dataAt(i).writeTo(paramCodedOutputByteBufferNano);
    }
  }
}
