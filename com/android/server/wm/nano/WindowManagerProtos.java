package com.android.server.wm.nano;

import com.android.framework.protobuf.nano.CodedInputByteBufferNano;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.android.framework.protobuf.nano.InternalNano;
import com.android.framework.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.framework.protobuf.nano.WireFormatNano;
import java.io.IOException;

public abstract interface WindowManagerProtos
{
  public static final class TaskSnapshotProto
    extends MessageNano
  {
    private static volatile TaskSnapshotProto[] _emptyArray;
    public int insetBottom;
    public int insetLeft;
    public int insetRight;
    public int insetTop;
    public boolean isRealSnapshot;
    public boolean isTranslucent;
    public int orientation;
    public int systemUiVisibility;
    public int windowingMode;
    
    public TaskSnapshotProto()
    {
      clear();
    }
    
    public static TaskSnapshotProto[] emptyArray()
    {
      if (_emptyArray == null) {
        synchronized (InternalNano.LAZY_INIT_LOCK)
        {
          if (_emptyArray == null) {
            _emptyArray = new TaskSnapshotProto[0];
          }
        }
      }
      return _emptyArray;
    }
    
    public static TaskSnapshotProto parseFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      return new TaskSnapshotProto().mergeFrom(paramCodedInputByteBufferNano);
    }
    
    public static TaskSnapshotProto parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (TaskSnapshotProto)MessageNano.mergeFrom(new TaskSnapshotProto(), paramArrayOfByte);
    }
    
    public TaskSnapshotProto clear()
    {
      orientation = 0;
      insetLeft = 0;
      insetTop = 0;
      insetRight = 0;
      insetBottom = 0;
      isRealSnapshot = false;
      windowingMode = 0;
      systemUiVisibility = 0;
      isTranslucent = false;
      cachedSize = -1;
      return this;
    }
    
    protected int computeSerializedSize()
    {
      int i = super.computeSerializedSize();
      int j = i;
      if (orientation != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(1, orientation);
      }
      i = j;
      if (insetLeft != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(2, insetLeft);
      }
      j = i;
      if (insetTop != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(3, insetTop);
      }
      i = j;
      if (insetRight != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(4, insetRight);
      }
      j = i;
      if (insetBottom != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(5, insetBottom);
      }
      i = j;
      if (isRealSnapshot) {
        i = j + CodedOutputByteBufferNano.computeBoolSize(6, isRealSnapshot);
      }
      j = i;
      if (windowingMode != 0) {
        j = i + CodedOutputByteBufferNano.computeInt32Size(7, windowingMode);
      }
      i = j;
      if (systemUiVisibility != 0) {
        i = j + CodedOutputByteBufferNano.computeInt32Size(8, systemUiVisibility);
      }
      j = i;
      if (isTranslucent) {
        j = i + CodedOutputByteBufferNano.computeBoolSize(9, isTranslucent);
      }
      return j;
    }
    
    public TaskSnapshotProto mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        if (i == 0) {
          break;
        }
        if (i != 8)
        {
          if (i != 16)
          {
            if (i != 24)
            {
              if (i != 32)
              {
                if (i != 40)
                {
                  if (i != 48)
                  {
                    if (i != 56)
                    {
                      if (i != 64)
                      {
                        if (i != 72)
                        {
                          if (!WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
                            return this;
                          }
                        }
                        else {
                          isTranslucent = paramCodedInputByteBufferNano.readBool();
                        }
                      }
                      else {
                        systemUiVisibility = paramCodedInputByteBufferNano.readInt32();
                      }
                    }
                    else {
                      windowingMode = paramCodedInputByteBufferNano.readInt32();
                    }
                  }
                  else {
                    isRealSnapshot = paramCodedInputByteBufferNano.readBool();
                  }
                }
                else {
                  insetBottom = paramCodedInputByteBufferNano.readInt32();
                }
              }
              else {
                insetRight = paramCodedInputByteBufferNano.readInt32();
              }
            }
            else {
              insetTop = paramCodedInputByteBufferNano.readInt32();
            }
          }
          else {
            insetLeft = paramCodedInputByteBufferNano.readInt32();
          }
        }
        else {
          orientation = paramCodedInputByteBufferNano.readInt32();
        }
      }
      return this;
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (orientation != 0) {
        paramCodedOutputByteBufferNano.writeInt32(1, orientation);
      }
      if (insetLeft != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, insetLeft);
      }
      if (insetTop != 0) {
        paramCodedOutputByteBufferNano.writeInt32(3, insetTop);
      }
      if (insetRight != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, insetRight);
      }
      if (insetBottom != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, insetBottom);
      }
      if (isRealSnapshot) {
        paramCodedOutputByteBufferNano.writeBool(6, isRealSnapshot);
      }
      if (windowingMode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, windowingMode);
      }
      if (systemUiVisibility != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, systemUiVisibility);
      }
      if (isTranslucent) {
        paramCodedOutputByteBufferNano.writeBool(9, isTranslucent);
      }
      super.writeTo(paramCodedOutputByteBufferNano);
    }
  }
}
