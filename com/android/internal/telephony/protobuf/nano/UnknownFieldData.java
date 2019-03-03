package com.android.internal.telephony.protobuf.nano;

import java.io.IOException;
import java.util.Arrays;

final class UnknownFieldData
{
  final byte[] bytes;
  final int tag;
  
  UnknownFieldData(int paramInt, byte[] paramArrayOfByte)
  {
    tag = paramInt;
    bytes = paramArrayOfByte;
  }
  
  int computeSerializedSize()
  {
    return 0 + CodedOutputByteBufferNano.computeRawVarint32Size(tag) + bytes.length;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof UnknownFieldData)) {
      return false;
    }
    paramObject = (UnknownFieldData)paramObject;
    if ((tag != tag) || (!Arrays.equals(bytes, bytes))) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + tag) + Arrays.hashCode(bytes);
  }
  
  void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
    throws IOException
  {
    paramCodedOutputByteBufferNano.writeRawVarint32(tag);
    paramCodedOutputByteBufferNano.writeRawBytes(bytes);
  }
}
