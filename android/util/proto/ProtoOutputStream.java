package android.util.proto;

import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class ProtoOutputStream
{
  public static final long FIELD_COUNT_MASK = 16492674416640L;
  public static final long FIELD_COUNT_PACKED = 5497558138880L;
  public static final long FIELD_COUNT_REPEATED = 2199023255552L;
  public static final int FIELD_COUNT_SHIFT = 40;
  public static final long FIELD_COUNT_SINGLE = 1099511627776L;
  public static final long FIELD_COUNT_UNKNOWN = 0L;
  public static final int FIELD_ID_MASK = -8;
  public static final int FIELD_ID_SHIFT = 3;
  public static final long FIELD_TYPE_BOOL = 34359738368L;
  public static final long FIELD_TYPE_BYTES = 51539607552L;
  public static final long FIELD_TYPE_DOUBLE = 4294967296L;
  public static final long FIELD_TYPE_ENUM = 60129542144L;
  public static final long FIELD_TYPE_FIXED32 = 30064771072L;
  public static final long FIELD_TYPE_FIXED64 = 25769803776L;
  public static final long FIELD_TYPE_FLOAT = 8589934592L;
  public static final long FIELD_TYPE_INT32 = 21474836480L;
  public static final long FIELD_TYPE_INT64 = 12884901888L;
  public static final long FIELD_TYPE_MASK = 1095216660480L;
  public static final long FIELD_TYPE_MESSAGE = 47244640256L;
  private static final String[] FIELD_TYPE_NAMES = { "Double", "Float", "Int64", "UInt64", "Int32", "Fixed64", "Fixed32", "Bool", "String", "Group", "Message", "Bytes", "UInt32", "Enum", "SFixed32", "SFixed64", "SInt32", "SInt64" };
  public static final long FIELD_TYPE_SFIXED32 = 64424509440L;
  public static final long FIELD_TYPE_SFIXED64 = 68719476736L;
  public static final int FIELD_TYPE_SHIFT = 32;
  public static final long FIELD_TYPE_SINT32 = 73014444032L;
  public static final long FIELD_TYPE_SINT64 = 77309411328L;
  public static final long FIELD_TYPE_STRING = 38654705664L;
  public static final long FIELD_TYPE_UINT32 = 55834574848L;
  public static final long FIELD_TYPE_UINT64 = 17179869184L;
  public static final long FIELD_TYPE_UNKNOWN = 0L;
  public static final String TAG = "ProtoOutputStream";
  public static final int WIRE_TYPE_END_GROUP = 4;
  public static final int WIRE_TYPE_FIXED32 = 5;
  public static final int WIRE_TYPE_FIXED64 = 1;
  public static final int WIRE_TYPE_LENGTH_DELIMITED = 2;
  public static final int WIRE_TYPE_MASK = 7;
  public static final int WIRE_TYPE_START_GROUP = 3;
  public static final int WIRE_TYPE_VARINT = 0;
  private EncodedBuffer mBuffer;
  private boolean mCompacted;
  private int mCopyBegin;
  private int mDepth;
  private long mExpectedObjectToken;
  private int mNextObjectId = -1;
  private OutputStream mStream;
  
  public ProtoOutputStream()
  {
    this(0);
  }
  
  public ProtoOutputStream(int paramInt)
  {
    mBuffer = new EncodedBuffer(paramInt);
  }
  
  public ProtoOutputStream(FileDescriptor paramFileDescriptor)
  {
    this(new FileOutputStream(paramFileDescriptor));
  }
  
  public ProtoOutputStream(OutputStream paramOutputStream)
  {
    this();
    mStream = paramOutputStream;
  }
  
  private void assertNotCompacted()
  {
    if (!mCompacted) {
      return;
    }
    throw new IllegalArgumentException("write called after compact");
  }
  
  public static int checkFieldId(long paramLong1, long paramLong2)
  {
    long l1 = paramLong1 & 0xF0000000000;
    long l2 = paramLong1 & 0xFF00000000;
    long l3 = paramLong2 & 0xF0000000000;
    paramLong2 &= 0xFF00000000;
    if ((int)paramLong1 != 0)
    {
      if ((l2 == paramLong2) && ((l1 == l3) || ((l1 == 5497558138880L) && (l3 == 2199023255552L)))) {
        return (int)paramLong1;
      }
      String str = getFieldCountString(l1);
      localObject = getFieldTypeString(l2);
      if ((localObject != null) && (str != null))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        if (paramLong2 == 47244640256L) {
          localStringBuilder.append("start");
        } else {
          localStringBuilder.append("write");
        }
        localStringBuilder.append(getFieldCountString(l3));
        localStringBuilder.append(getFieldTypeString(paramLong2));
        localStringBuilder.append(" called for field ");
        localStringBuilder.append((int)paramLong1);
        localStringBuilder.append(" which should be used with ");
        if (l2 == 47244640256L) {
          localStringBuilder.append("start");
        } else {
          localStringBuilder.append("write");
        }
        localStringBuilder.append(str);
        localStringBuilder.append((String)localObject);
        if (l1 == 5497558138880L)
        {
          localStringBuilder.append(" or writeRepeated");
          localStringBuilder.append((String)localObject);
        }
        localStringBuilder.append('.');
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      localObject = new StringBuilder();
      if (paramLong2 == 47244640256L) {
        ((StringBuilder)localObject).append("start");
      } else {
        ((StringBuilder)localObject).append("write");
      }
      ((StringBuilder)localObject).append(getFieldCountString(l3));
      ((StringBuilder)localObject).append(getFieldTypeString(paramLong2));
      ((StringBuilder)localObject).append(" called with an invalid fieldId: 0x");
      ((StringBuilder)localObject).append(Long.toHexString(paramLong1));
      ((StringBuilder)localObject).append(". The proto field ID might be ");
      ((StringBuilder)localObject).append((int)paramLong1);
      ((StringBuilder)localObject).append('.');
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid proto field ");
    ((StringBuilder)localObject).append((int)paramLong1);
    ((StringBuilder)localObject).append(" fieldId=");
    ((StringBuilder)localObject).append(Long.toHexString(paramLong1));
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  private void compactIfNecessary()
  {
    if (!mCompacted) {
      if (mDepth == 0)
      {
        mBuffer.startEditing();
        int i = mBuffer.getReadableSize();
        editEncodedSize(i);
        mBuffer.rewindRead();
        compactSizes(i);
        if (mCopyBegin < i) {
          mBuffer.writeFromThisBuffer(mCopyBegin, i - mCopyBegin);
        }
        mBuffer.startEditing();
        mCompacted = true;
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Trying to compact with ");
        localStringBuilder.append(mDepth);
        localStringBuilder.append(" missing calls to endObject");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
  }
  
  private void compactSizes(int paramInt)
  {
    int i = mBuffer.getReadPos();
    for (;;)
    {
      int j = mBuffer.getReadPos();
      if (j >= i + paramInt) {
        break;
      }
      int k = readRawTag();
      int m = k & 0x7;
      StringBuilder localStringBuilder;
      switch (m)
      {
      default: 
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("compactSizes Bad tag tag=0x");
        localStringBuilder.append(Integer.toHexString(k));
        localStringBuilder.append(" wireType=");
        localStringBuilder.append(m);
        localStringBuilder.append(" -- ");
        localStringBuilder.append(mBuffer.getDebugString());
        throw new ProtoParseException(localStringBuilder.toString());
      case 5: 
        mBuffer.skipRead(4);
        break;
      case 3: 
      case 4: 
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("groups not supported at index ");
        localStringBuilder.append(j);
        throw new RuntimeException(localStringBuilder.toString());
      case 2: 
        mBuffer.writeFromThisBuffer(mCopyBegin, mBuffer.getReadPos() - mCopyBegin);
        k = mBuffer.readRawFixed32();
        m = mBuffer.readRawFixed32();
        mBuffer.writeRawVarint32(m);
        mCopyBegin = mBuffer.getReadPos();
        if (k >= 0) {
          mBuffer.skipRead(m);
        } else {
          compactSizes(-k);
        }
        break;
      case 1: 
        mBuffer.skipRead(8);
        break;
      case 0: 
        while ((mBuffer.readRawByte() & 0x80) != 0) {}
      }
    }
  }
  
  public static int convertObjectIdToOrdinal(int paramInt)
  {
    return 524287 - paramInt;
  }
  
  private int editEncodedSize(int paramInt)
  {
    int i = mBuffer.getReadPos();
    int j = 0;
    for (;;)
    {
      int k = mBuffer.getReadPos();
      if (k >= i + paramInt) {
        break;
      }
      int m = readRawTag();
      int n = j + EncodedBuffer.getRawVarint32Size(m);
      j = m & 0x7;
      StringBuilder localStringBuilder;
      switch (j)
      {
      default: 
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("editEncodedSize Bad tag tag=0x");
        localStringBuilder.append(Integer.toHexString(m));
        localStringBuilder.append(" wireType=");
        localStringBuilder.append(j);
        localStringBuilder.append(" -- ");
        localStringBuilder.append(mBuffer.getDebugString());
        throw new ProtoParseException(localStringBuilder.toString());
      case 5: 
        j = n + 4;
        mBuffer.skipRead(4);
        break;
      case 3: 
      case 4: 
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("groups not supported at index ");
        localStringBuilder.append(k);
        throw new RuntimeException(localStringBuilder.toString());
      case 2: 
        m = mBuffer.readRawFixed32();
        k = mBuffer.getReadPos();
        j = mBuffer.readRawFixed32();
        if (m >= 0)
        {
          if (j == m)
          {
            mBuffer.skipRead(m);
          }
          else
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Pre-computed size where the precomputed size and the raw size in the buffer don't match! childRawSize=");
            localStringBuilder.append(m);
            localStringBuilder.append(" childEncodedSize=");
            localStringBuilder.append(j);
            localStringBuilder.append(" childEncodedSizePos=");
            localStringBuilder.append(k);
            throw new RuntimeException(localStringBuilder.toString());
          }
        }
        else
        {
          j = editEncodedSize(-m);
          mBuffer.editRawFixed32(k, j);
        }
        j = n + (EncodedBuffer.getRawVarint32Size(j) + j);
        break;
      case 1: 
        j = n + 8;
        mBuffer.skipRead(8);
        break;
      case 0: 
        n++;
        for (;;)
        {
          j = n;
          if ((mBuffer.readRawByte() & 0x80) == 0) {
            break;
          }
          n++;
        }
      }
    }
    return j;
  }
  
  private void endObjectImpl(long paramLong, boolean paramBoolean)
  {
    int i = getDepthFromToken(paramLong);
    boolean bool = getRepeatedFromToken(paramLong);
    int j = getSizePosFromToken(paramLong);
    int k = mBuffer.getWritePos() - j - 8;
    if (paramBoolean != bool)
    {
      if (paramBoolean) {
        throw new IllegalArgumentException("endRepeatedObject called where endObject should have been");
      }
      throw new IllegalArgumentException("endObject called where endRepeatedObject should have been");
    }
    if (((mDepth & 0x1FF) == i) && (mExpectedObjectToken == paramLong))
    {
      mExpectedObjectToken = (mBuffer.getRawFixed32At(j) << 32 | 0xFFFFFFFF & mBuffer.getRawFixed32At(j + 4));
      mDepth -= 1;
      if (k > 0)
      {
        mBuffer.editRawFixed32(j, -k);
        mBuffer.editRawFixed32(j + 4, -1);
      }
      else if (paramBoolean)
      {
        mBuffer.editRawFixed32(j, 0);
        mBuffer.editRawFixed32(j + 4, 0);
      }
      else
      {
        mBuffer.rewindWriteTo(j - getTagSizeFromToken(paramLong));
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Mismatched startObject/endObject calls. Current depth ");
    localStringBuilder.append(mDepth);
    localStringBuilder.append(" token=");
    localStringBuilder.append(token2String(paramLong));
    localStringBuilder.append(" expectedToken=");
    localStringBuilder.append(token2String(mExpectedObjectToken));
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static int getDepthFromToken(long paramLong)
  {
    return (int)(paramLong >> 51 & 0x1FF);
  }
  
  private static String getFieldCountString(long paramLong)
  {
    if (paramLong == 1099511627776L) {
      return "";
    }
    if (paramLong == 2199023255552L) {
      return "Repeated";
    }
    if (paramLong == 5497558138880L) {
      return "Packed";
    }
    return null;
  }
  
  private String getFieldIdString(long paramLong)
  {
    long l = 0xF0000000000 & paramLong;
    Object localObject1 = getFieldCountString(l);
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("fieldCount=");
      ((StringBuilder)localObject2).append(l);
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if (((String)localObject2).length() > 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append(" ");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    l = 0xFF00000000 & paramLong;
    Object localObject3 = getFieldTypeString(l);
    localObject2 = localObject3;
    if (localObject3 == null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("fieldType=");
      ((StringBuilder)localObject2).append(l);
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append((String)localObject1);
    ((StringBuilder)localObject3).append((String)localObject2);
    ((StringBuilder)localObject3).append(" tag=");
    ((StringBuilder)localObject3).append((int)paramLong);
    ((StringBuilder)localObject3).append(" fieldId=0x");
    ((StringBuilder)localObject3).append(Long.toHexString(paramLong));
    return ((StringBuilder)localObject3).toString();
  }
  
  private static String getFieldTypeString(long paramLong)
  {
    int i = (int)((0xFF00000000 & paramLong) >>> 32) - 1;
    if ((i >= 0) && (i < FIELD_TYPE_NAMES.length)) {
      return FIELD_TYPE_NAMES[i];
    }
    return null;
  }
  
  public static int getObjectIdFromToken(long paramLong)
  {
    return (int)(paramLong >> 32 & 0x7FFFF);
  }
  
  public static boolean getRepeatedFromToken(long paramLong)
  {
    boolean bool;
    if ((paramLong >> 60 & 1L) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int getSizePosFromToken(long paramLong)
  {
    return (int)paramLong;
  }
  
  private static int getTagSize(int paramInt)
  {
    return EncodedBuffer.getRawVarint32Size(paramInt << 3);
  }
  
  public static int getTagSizeFromToken(long paramLong)
  {
    return (int)(paramLong >> 61 & 0x7);
  }
  
  public static long makeFieldId(int paramInt, long paramLong)
  {
    return paramInt & 0xFFFFFFFF | paramLong;
  }
  
  public static long makeToken(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4)
  {
    long l1 = paramInt1;
    long l2;
    if (paramBoolean) {
      l2 = 1152921504606846976L;
    } else {
      l2 = 0L;
    }
    return (l1 & 0x7) << 61 | l2 | (0x1FF & paramInt2) << 51 | (0x7FFFF & paramInt3) << 32 | 0xFFFFFFFF & paramInt4;
  }
  
  private int readRawTag()
  {
    if (mBuffer.getReadPos() == mBuffer.getReadableSize()) {
      return 0;
    }
    return (int)mBuffer.readRawUnsigned();
  }
  
  private long startObjectImpl(int paramInt, boolean paramBoolean)
  {
    writeTag(paramInt, 2);
    int i = mBuffer.getWritePos();
    mDepth += 1;
    mNextObjectId -= 1;
    mBuffer.writeRawFixed32((int)(mExpectedObjectToken >> 32));
    mBuffer.writeRawFixed32((int)mExpectedObjectToken);
    long l = mExpectedObjectToken;
    mExpectedObjectToken = makeToken(getTagSize(paramInt), paramBoolean, mDepth, mNextObjectId, i);
    return mExpectedObjectToken;
  }
  
  public static String token2String(long paramLong)
  {
    if (paramLong == 0L) {
      return "Token(0)";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Token(val=0x");
    localStringBuilder.append(Long.toHexString(paramLong));
    localStringBuilder.append(" depth=");
    localStringBuilder.append(getDepthFromToken(paramLong));
    localStringBuilder.append(" object=");
    localStringBuilder.append(convertObjectIdToOrdinal(getObjectIdFromToken(paramLong)));
    localStringBuilder.append(" tagSize=");
    localStringBuilder.append(getTagSizeFromToken(paramLong));
    localStringBuilder.append(" sizePos=");
    localStringBuilder.append(getSizePosFromToken(paramLong));
    localStringBuilder.append(')');
    return localStringBuilder.toString();
  }
  
  private void writeBoolImpl(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      writeTag(paramInt, 0);
      mBuffer.writeRawByte((byte)1);
    }
  }
  
  private void writeBytesImpl(int paramInt, byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0))
    {
      writeKnownLengthHeader(paramInt, paramArrayOfByte.length);
      mBuffer.writeRawBuffer(paramArrayOfByte);
    }
  }
  
  private void writeDoubleImpl(int paramInt, double paramDouble)
  {
    if (paramDouble != 0.0D)
    {
      writeTag(paramInt, 1);
      mBuffer.writeRawFixed64(Double.doubleToLongBits(paramDouble));
    }
  }
  
  private void writeEnumImpl(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      writeTag(paramInt1, 0);
      writeUnsignedVarintFromSignedInt(paramInt2);
    }
  }
  
  private void writeFixed32Impl(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      writeTag(paramInt1, 5);
      mBuffer.writeRawFixed32(paramInt2);
    }
  }
  
  private void writeFixed64Impl(int paramInt, long paramLong)
  {
    if (paramLong != 0L)
    {
      writeTag(paramInt, 1);
      mBuffer.writeRawFixed64(paramLong);
    }
  }
  
  private void writeFloatImpl(int paramInt, float paramFloat)
  {
    if (paramFloat != 0.0F)
    {
      writeTag(paramInt, 5);
      mBuffer.writeRawFixed32(Float.floatToIntBits(paramFloat));
    }
  }
  
  private void writeInt32Impl(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      writeTag(paramInt1, 0);
      writeUnsignedVarintFromSignedInt(paramInt2);
    }
  }
  
  private void writeInt64Impl(int paramInt, long paramLong)
  {
    if (paramLong != 0L)
    {
      writeTag(paramInt, 0);
      mBuffer.writeRawVarint64(paramLong);
    }
  }
  
  private void writeKnownLengthHeader(int paramInt1, int paramInt2)
  {
    writeTag(paramInt1, 2);
    mBuffer.writeRawFixed32(paramInt2);
    mBuffer.writeRawFixed32(paramInt2);
  }
  
  private void writeRepeatedBoolImpl(int paramInt, boolean paramBoolean)
  {
    writeTag(paramInt, 0);
    mBuffer.writeRawByte((byte)paramBoolean);
  }
  
  private void writeRepeatedBytesImpl(int paramInt, byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte == null) {
      i = 0;
    } else {
      i = paramArrayOfByte.length;
    }
    writeKnownLengthHeader(paramInt, i);
    mBuffer.writeRawBuffer(paramArrayOfByte);
  }
  
  private void writeRepeatedDoubleImpl(int paramInt, double paramDouble)
  {
    writeTag(paramInt, 1);
    mBuffer.writeRawFixed64(Double.doubleToLongBits(paramDouble));
  }
  
  private void writeRepeatedEnumImpl(int paramInt1, int paramInt2)
  {
    writeTag(paramInt1, 0);
    writeUnsignedVarintFromSignedInt(paramInt2);
  }
  
  private void writeRepeatedFixed32Impl(int paramInt1, int paramInt2)
  {
    writeTag(paramInt1, 5);
    mBuffer.writeRawFixed32(paramInt2);
  }
  
  private void writeRepeatedFixed64Impl(int paramInt, long paramLong)
  {
    writeTag(paramInt, 1);
    mBuffer.writeRawFixed64(paramLong);
  }
  
  private void writeRepeatedFloatImpl(int paramInt, float paramFloat)
  {
    writeTag(paramInt, 5);
    mBuffer.writeRawFixed32(Float.floatToIntBits(paramFloat));
  }
  
  private void writeRepeatedInt32Impl(int paramInt1, int paramInt2)
  {
    writeTag(paramInt1, 0);
    writeUnsignedVarintFromSignedInt(paramInt2);
  }
  
  private void writeRepeatedInt64Impl(int paramInt, long paramLong)
  {
    writeTag(paramInt, 0);
    mBuffer.writeRawVarint64(paramLong);
  }
  
  private void writeRepeatedSFixed32Impl(int paramInt1, int paramInt2)
  {
    writeTag(paramInt1, 5);
    mBuffer.writeRawFixed32(paramInt2);
  }
  
  private void writeRepeatedSFixed64Impl(int paramInt, long paramLong)
  {
    writeTag(paramInt, 1);
    mBuffer.writeRawFixed64(paramLong);
  }
  
  private void writeRepeatedSInt32Impl(int paramInt1, int paramInt2)
  {
    writeTag(paramInt1, 0);
    mBuffer.writeRawZigZag32(paramInt2);
  }
  
  private void writeRepeatedSInt64Impl(int paramInt, long paramLong)
  {
    writeTag(paramInt, 0);
    mBuffer.writeRawZigZag64(paramLong);
  }
  
  private void writeRepeatedStringImpl(int paramInt, String paramString)
  {
    if ((paramString != null) && (paramString.length() != 0)) {
      writeUtf8String(paramInt, paramString);
    } else {
      writeKnownLengthHeader(paramInt, 0);
    }
  }
  
  private void writeRepeatedUInt32Impl(int paramInt1, int paramInt2)
  {
    writeTag(paramInt1, 0);
    mBuffer.writeRawVarint32(paramInt2);
  }
  
  private void writeRepeatedUInt64Impl(int paramInt, long paramLong)
  {
    writeTag(paramInt, 0);
    mBuffer.writeRawVarint64(paramLong);
  }
  
  private void writeSFixed32Impl(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      writeTag(paramInt1, 5);
      mBuffer.writeRawFixed32(paramInt2);
    }
  }
  
  private void writeSFixed64Impl(int paramInt, long paramLong)
  {
    if (paramLong != 0L)
    {
      writeTag(paramInt, 1);
      mBuffer.writeRawFixed64(paramLong);
    }
  }
  
  private void writeSInt32Impl(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      writeTag(paramInt1, 0);
      mBuffer.writeRawZigZag32(paramInt2);
    }
  }
  
  private void writeSInt64Impl(int paramInt, long paramLong)
  {
    if (paramLong != 0L)
    {
      writeTag(paramInt, 0);
      mBuffer.writeRawZigZag64(paramLong);
    }
  }
  
  private void writeStringImpl(int paramInt, String paramString)
  {
    if ((paramString != null) && (paramString.length() > 0)) {
      writeUtf8String(paramInt, paramString);
    }
  }
  
  private void writeUInt32Impl(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      writeTag(paramInt1, 0);
      mBuffer.writeRawVarint32(paramInt2);
    }
  }
  
  private void writeUInt64Impl(int paramInt, long paramLong)
  {
    if (paramLong != 0L)
    {
      writeTag(paramInt, 0);
      mBuffer.writeRawVarint64(paramLong);
    }
  }
  
  private void writeUnsignedVarintFromSignedInt(int paramInt)
  {
    if (paramInt >= 0) {
      mBuffer.writeRawVarint32(paramInt);
    } else {
      mBuffer.writeRawVarint64(paramInt);
    }
  }
  
  private void writeUtf8String(int paramInt, String paramString)
  {
    try
    {
      paramString = paramString.getBytes("UTF-8");
      writeKnownLengthHeader(paramInt, paramString.length);
      mBuffer.writeRawBuffer(paramString);
      return;
    }
    catch (UnsupportedEncodingException paramString)
    {
      throw new RuntimeException("not possible");
    }
  }
  
  public void dump(String paramString)
  {
    Log.d(paramString, mBuffer.getDebugString());
    mBuffer.dumpBuffers(paramString);
  }
  
  public void end(long paramLong)
  {
    endObjectImpl(paramLong, getRepeatedFromToken(paramLong));
  }
  
  @Deprecated
  public void endObject(long paramLong)
  {
    assertNotCompacted();
    endObjectImpl(paramLong, false);
  }
  
  @Deprecated
  public void endRepeatedObject(long paramLong)
  {
    assertNotCompacted();
    endObjectImpl(paramLong, true);
  }
  
  public void flush()
  {
    if (mStream == null) {
      return;
    }
    if (mDepth != 0) {
      return;
    }
    if (mCompacted) {
      return;
    }
    compactIfNecessary();
    byte[] arrayOfByte = mBuffer.getBytes(mBuffer.getReadableSize());
    try
    {
      mStream.write(arrayOfByte);
      mStream.flush();
      return;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("Error flushing proto to stream", localIOException);
    }
  }
  
  public byte[] getBytes()
  {
    compactIfNecessary();
    return mBuffer.getBytes(mBuffer.getReadableSize());
  }
  
  public long start(long paramLong)
  {
    assertNotCompacted();
    int i = (int)paramLong;
    if ((0xFF00000000 & paramLong) == 47244640256L)
    {
      long l = 0xF0000000000 & paramLong;
      if (l == 1099511627776L) {
        return startObjectImpl(i, false);
      }
      if ((l == 2199023255552L) || (l == 5497558138880L)) {
        return startObjectImpl(i, true);
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Attempt to call start(long) with ");
    localStringBuilder.append(getFieldIdString(paramLong));
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  @Deprecated
  public long startObject(long paramLong)
  {
    assertNotCompacted();
    return startObjectImpl(checkFieldId(paramLong, 1146756268032L), false);
  }
  
  @Deprecated
  public long startRepeatedObject(long paramLong)
  {
    assertNotCompacted();
    return startObjectImpl(checkFieldId(paramLong, 2246267895808L), true);
  }
  
  public void write(long paramLong, double paramDouble)
  {
    assertNotCompacted();
    int i = (int)paramLong;
    int j = (int)((0xFFF00000000 & paramLong) >> 32);
    boolean bool1 = false;
    boolean bool2 = false;
    switch (j)
    {
    default: 
      switch (j)
      {
      default: 
        switch (j)
        {
        default: 
          switch (j)
          {
          default: 
            switch (j)
            {
            default: 
              switch (j)
              {
              default: 
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("Attempt to call write(long, double) with ");
                localStringBuilder.append(getFieldIdString(paramLong));
                throw new IllegalArgumentException(localStringBuilder.toString());
              }
              break;
            }
          case 530: 
            writeRepeatedSInt64Impl(i, paramDouble);
            break;
          case 529: 
            writeRepeatedSInt32Impl(i, (int)paramDouble);
            break;
          case 528: 
            writeRepeatedSFixed64Impl(i, paramDouble);
            break;
          case 527: 
            writeRepeatedSFixed32Impl(i, (int)paramDouble);
            break;
          case 526: 
            writeRepeatedEnumImpl(i, (int)paramDouble);
            break;
          case 525: 
            writeRepeatedUInt32Impl(i, (int)paramDouble);
          }
          break;
        case 520: 
          if (paramDouble != 0.0D) {
            bool2 = true;
          }
          writeRepeatedBoolImpl(i, bool2);
          break;
        case 519: 
          writeRepeatedFixed32Impl(i, (int)paramDouble);
          break;
        case 518: 
          writeRepeatedFixed64Impl(i, paramDouble);
          break;
        case 517: 
          writeRepeatedInt32Impl(i, (int)paramDouble);
          break;
        case 516: 
          writeRepeatedUInt64Impl(i, paramDouble);
          break;
        case 515: 
          writeRepeatedInt64Impl(i, paramDouble);
          break;
        case 514: 
          writeRepeatedFloatImpl(i, (float)paramDouble);
          break;
        case 513: 
          writeRepeatedDoubleImpl(i, paramDouble);
        }
        break;
      case 274: 
        writeSInt64Impl(i, paramDouble);
        break;
      case 273: 
        writeSInt32Impl(i, (int)paramDouble);
        break;
      case 272: 
        writeSFixed64Impl(i, paramDouble);
        break;
      case 271: 
        writeSFixed32Impl(i, (int)paramDouble);
        break;
      case 270: 
        writeEnumImpl(i, (int)paramDouble);
        break;
      case 269: 
        writeUInt32Impl(i, (int)paramDouble);
      }
      break;
    case 264: 
      bool2 = bool1;
      if (paramDouble != 0.0D) {
        bool2 = true;
      }
      writeBoolImpl(i, bool2);
      break;
    case 263: 
      writeFixed32Impl(i, (int)paramDouble);
      break;
    case 262: 
      writeFixed64Impl(i, paramDouble);
      break;
    case 261: 
      writeInt32Impl(i, (int)paramDouble);
      break;
    case 260: 
      writeUInt64Impl(i, paramDouble);
      break;
    case 259: 
      writeInt64Impl(i, paramDouble);
      break;
    case 258: 
      writeFloatImpl(i, (float)paramDouble);
      break;
    case 257: 
      writeDoubleImpl(i, paramDouble);
    }
  }
  
  public void write(long paramLong, float paramFloat)
  {
    assertNotCompacted();
    int i = (int)paramLong;
    int j = (int)((0xFFF00000000 & paramLong) >> 32);
    boolean bool1 = false;
    boolean bool2 = false;
    switch (j)
    {
    default: 
      switch (j)
      {
      default: 
        switch (j)
        {
        default: 
          switch (j)
          {
          default: 
            switch (j)
            {
            default: 
              switch (j)
              {
              default: 
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("Attempt to call write(long, float) with ");
                localStringBuilder.append(getFieldIdString(paramLong));
                throw new IllegalArgumentException(localStringBuilder.toString());
              }
              break;
            }
          case 530: 
            writeRepeatedSInt64Impl(i, paramFloat);
            break;
          case 529: 
            writeRepeatedSInt32Impl(i, (int)paramFloat);
            break;
          case 528: 
            writeRepeatedSFixed64Impl(i, paramFloat);
            break;
          case 527: 
            writeRepeatedSFixed32Impl(i, (int)paramFloat);
            break;
          case 526: 
            writeRepeatedEnumImpl(i, (int)paramFloat);
            break;
          case 525: 
            writeRepeatedUInt32Impl(i, (int)paramFloat);
          }
          break;
        case 520: 
          if (paramFloat != 0.0F) {
            bool2 = true;
          }
          writeRepeatedBoolImpl(i, bool2);
          break;
        case 519: 
          writeRepeatedFixed32Impl(i, (int)paramFloat);
          break;
        case 518: 
          writeRepeatedFixed64Impl(i, paramFloat);
          break;
        case 517: 
          writeRepeatedInt32Impl(i, (int)paramFloat);
          break;
        case 516: 
          writeRepeatedUInt64Impl(i, paramFloat);
          break;
        case 515: 
          writeRepeatedInt64Impl(i, paramFloat);
          break;
        case 514: 
          writeRepeatedFloatImpl(i, paramFloat);
          break;
        case 513: 
          writeRepeatedDoubleImpl(i, paramFloat);
        }
        break;
      case 274: 
        writeSInt64Impl(i, paramFloat);
        break;
      case 273: 
        writeSInt32Impl(i, (int)paramFloat);
        break;
      case 272: 
        writeSFixed64Impl(i, paramFloat);
        break;
      case 271: 
        writeSFixed32Impl(i, (int)paramFloat);
        break;
      case 270: 
        writeEnumImpl(i, (int)paramFloat);
        break;
      case 269: 
        writeUInt32Impl(i, (int)paramFloat);
      }
      break;
    case 264: 
      bool2 = bool1;
      if (paramFloat != 0.0F) {
        bool2 = true;
      }
      writeBoolImpl(i, bool2);
      break;
    case 263: 
      writeFixed32Impl(i, (int)paramFloat);
      break;
    case 262: 
      writeFixed64Impl(i, paramFloat);
      break;
    case 261: 
      writeInt32Impl(i, (int)paramFloat);
      break;
    case 260: 
      writeUInt64Impl(i, paramFloat);
      break;
    case 259: 
      writeInt64Impl(i, paramFloat);
      break;
    case 258: 
      writeFloatImpl(i, paramFloat);
      break;
    case 257: 
      writeDoubleImpl(i, paramFloat);
    }
  }
  
  public void write(long paramLong, int paramInt)
  {
    assertNotCompacted();
    int i = (int)paramLong;
    int j = (int)((0xFFF00000000 & paramLong) >> 32);
    boolean bool1 = false;
    boolean bool2 = false;
    switch (j)
    {
    default: 
      switch (j)
      {
      default: 
        switch (j)
        {
        default: 
          switch (j)
          {
          default: 
            switch (j)
            {
            default: 
              switch (j)
              {
              default: 
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("Attempt to call write(long, int) with ");
                localStringBuilder.append(getFieldIdString(paramLong));
                throw new IllegalArgumentException(localStringBuilder.toString());
              }
              break;
            }
          case 530: 
            writeRepeatedSInt64Impl(i, paramInt);
            break;
          case 529: 
            writeRepeatedSInt32Impl(i, paramInt);
            break;
          case 528: 
            writeRepeatedSFixed64Impl(i, paramInt);
            break;
          case 527: 
            writeRepeatedSFixed32Impl(i, paramInt);
            break;
          case 526: 
            writeRepeatedEnumImpl(i, paramInt);
            break;
          case 525: 
            writeRepeatedUInt32Impl(i, paramInt);
          }
          break;
        case 520: 
          if (paramInt != 0) {
            bool2 = true;
          }
          writeRepeatedBoolImpl(i, bool2);
          break;
        case 519: 
          writeRepeatedFixed32Impl(i, paramInt);
          break;
        case 518: 
          writeRepeatedFixed64Impl(i, paramInt);
          break;
        case 517: 
          writeRepeatedInt32Impl(i, paramInt);
          break;
        case 516: 
          writeRepeatedUInt64Impl(i, paramInt);
          break;
        case 515: 
          writeRepeatedInt64Impl(i, paramInt);
          break;
        case 514: 
          writeRepeatedFloatImpl(i, paramInt);
          break;
        case 513: 
          writeRepeatedDoubleImpl(i, paramInt);
        }
        break;
      case 274: 
        writeSInt64Impl(i, paramInt);
        break;
      case 273: 
        writeSInt32Impl(i, paramInt);
        break;
      case 272: 
        writeSFixed64Impl(i, paramInt);
        break;
      case 271: 
        writeSFixed32Impl(i, paramInt);
        break;
      case 270: 
        writeEnumImpl(i, paramInt);
        break;
      case 269: 
        writeUInt32Impl(i, paramInt);
      }
      break;
    case 264: 
      bool2 = bool1;
      if (paramInt != 0) {
        bool2 = true;
      }
      writeBoolImpl(i, bool2);
      break;
    case 263: 
      writeFixed32Impl(i, paramInt);
      break;
    case 262: 
      writeFixed64Impl(i, paramInt);
      break;
    case 261: 
      writeInt32Impl(i, paramInt);
      break;
    case 260: 
      writeUInt64Impl(i, paramInt);
      break;
    case 259: 
      writeInt64Impl(i, paramInt);
      break;
    case 258: 
      writeFloatImpl(i, paramInt);
      break;
    case 257: 
      writeDoubleImpl(i, paramInt);
    }
  }
  
  public void write(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    int i = (int)paramLong1;
    int j = (int)((0xFFF00000000 & paramLong1) >> 32);
    boolean bool1 = false;
    boolean bool2 = false;
    switch (j)
    {
    default: 
      switch (j)
      {
      default: 
        switch (j)
        {
        default: 
          switch (j)
          {
          default: 
            switch (j)
            {
            default: 
              switch (j)
              {
              default: 
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("Attempt to call write(long, long) with ");
                localStringBuilder.append(getFieldIdString(paramLong1));
                throw new IllegalArgumentException(localStringBuilder.toString());
              }
              break;
            }
          case 530: 
            writeRepeatedSInt64Impl(i, paramLong2);
            break;
          case 529: 
            writeRepeatedSInt32Impl(i, (int)paramLong2);
            break;
          case 528: 
            writeRepeatedSFixed64Impl(i, paramLong2);
            break;
          case 527: 
            writeRepeatedSFixed32Impl(i, (int)paramLong2);
            break;
          case 526: 
            writeRepeatedEnumImpl(i, (int)paramLong2);
            break;
          case 525: 
            writeRepeatedUInt32Impl(i, (int)paramLong2);
          }
          break;
        case 520: 
          if (paramLong2 != 0L) {
            bool2 = true;
          }
          writeRepeatedBoolImpl(i, bool2);
          break;
        case 519: 
          writeRepeatedFixed32Impl(i, (int)paramLong2);
          break;
        case 518: 
          writeRepeatedFixed64Impl(i, paramLong2);
          break;
        case 517: 
          writeRepeatedInt32Impl(i, (int)paramLong2);
          break;
        case 516: 
          writeRepeatedUInt64Impl(i, paramLong2);
          break;
        case 515: 
          writeRepeatedInt64Impl(i, paramLong2);
          break;
        case 514: 
          writeRepeatedFloatImpl(i, (float)paramLong2);
          break;
        case 513: 
          writeRepeatedDoubleImpl(i, paramLong2);
        }
        break;
      case 274: 
        writeSInt64Impl(i, paramLong2);
        break;
      case 273: 
        writeSInt32Impl(i, (int)paramLong2);
        break;
      case 272: 
        writeSFixed64Impl(i, paramLong2);
        break;
      case 271: 
        writeSFixed32Impl(i, (int)paramLong2);
        break;
      case 270: 
        writeEnumImpl(i, (int)paramLong2);
        break;
      case 269: 
        writeUInt32Impl(i, (int)paramLong2);
      }
      break;
    case 264: 
      bool2 = bool1;
      if (paramLong2 != 0L) {
        bool2 = true;
      }
      writeBoolImpl(i, bool2);
      break;
    case 263: 
      writeFixed32Impl(i, (int)paramLong2);
      break;
    case 262: 
      writeFixed64Impl(i, paramLong2);
      break;
    case 261: 
      writeInt32Impl(i, (int)paramLong2);
      break;
    case 260: 
      writeUInt64Impl(i, paramLong2);
      break;
    case 259: 
      writeInt64Impl(i, paramLong2);
      break;
    case 258: 
      writeFloatImpl(i, (float)paramLong2);
      break;
    case 257: 
      writeDoubleImpl(i, paramLong2);
    }
  }
  
  public void write(long paramLong, String paramString)
  {
    assertNotCompacted();
    int i = (int)paramLong;
    int j = (int)((0xFFF00000000 & paramLong) >> 32);
    if (j != 265)
    {
      if ((j != 521) && (j != 1289))
      {
        paramString = new StringBuilder();
        paramString.append("Attempt to call write(long, String) with ");
        paramString.append(getFieldIdString(paramLong));
        throw new IllegalArgumentException(paramString.toString());
      }
      writeRepeatedStringImpl(i, paramString);
    }
    else
    {
      writeStringImpl(i, paramString);
    }
  }
  
  public void write(long paramLong, boolean paramBoolean)
  {
    assertNotCompacted();
    int i = (int)paramLong;
    int j = (int)((0xFFF00000000 & paramLong) >> 32);
    if (j != 264)
    {
      if ((j != 520) && (j != 1288))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Attempt to call write(long, boolean) with ");
        localStringBuilder.append(getFieldIdString(paramLong));
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      writeRepeatedBoolImpl(i, paramBoolean);
    }
    else
    {
      writeBoolImpl(i, paramBoolean);
    }
  }
  
  public void write(long paramLong, byte[] paramArrayOfByte)
  {
    assertNotCompacted();
    int i = (int)paramLong;
    switch ((int)((0xFFF00000000 & paramLong) >> 32))
    {
    default: 
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Attempt to call write(long, byte[]) with ");
      paramArrayOfByte.append(getFieldIdString(paramLong));
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    case 524: 
    case 1292: 
      writeRepeatedBytesImpl(i, paramArrayOfByte);
      break;
    case 523: 
    case 1291: 
      writeRepeatedObjectImpl(i, paramArrayOfByte);
      break;
    case 268: 
      writeBytesImpl(i, paramArrayOfByte);
      break;
    case 267: 
      writeObjectImpl(i, paramArrayOfByte);
    }
  }
  
  @Deprecated
  public void writeBool(long paramLong, boolean paramBoolean)
  {
    assertNotCompacted();
    writeBoolImpl(checkFieldId(paramLong, 1133871366144L), paramBoolean);
  }
  
  @Deprecated
  public void writeBytes(long paramLong, byte[] paramArrayOfByte)
  {
    assertNotCompacted();
    writeBytesImpl(checkFieldId(paramLong, 1151051235328L), paramArrayOfByte);
  }
  
  @Deprecated
  public void writeDouble(long paramLong, double paramDouble)
  {
    assertNotCompacted();
    writeDoubleImpl(checkFieldId(paramLong, 1103806595072L), paramDouble);
  }
  
  @Deprecated
  public void writeEnum(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeEnumImpl(checkFieldId(paramLong, 1159641169920L), paramInt);
  }
  
  @Deprecated
  public void writeFixed32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeFixed32Impl(checkFieldId(paramLong, 1129576398848L), paramInt);
  }
  
  @Deprecated
  public void writeFixed64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeFixed64Impl(checkFieldId(paramLong1, 1125281431552L), paramLong2);
  }
  
  @Deprecated
  public void writeFloat(long paramLong, float paramFloat)
  {
    assertNotCompacted();
    writeFloatImpl(checkFieldId(paramLong, 1108101562368L), paramFloat);
  }
  
  @Deprecated
  public void writeInt32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeInt32Impl(checkFieldId(paramLong, 1120986464256L), paramInt);
  }
  
  @Deprecated
  public void writeInt64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeInt64Impl(checkFieldId(paramLong1, 1112396529664L), paramLong2);
  }
  
  @Deprecated
  public void writeObject(long paramLong, byte[] paramArrayOfByte)
  {
    assertNotCompacted();
    writeObjectImpl(checkFieldId(paramLong, 1146756268032L), paramArrayOfByte);
  }
  
  void writeObjectImpl(int paramInt, byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0))
    {
      writeKnownLengthHeader(paramInt, paramArrayOfByte.length);
      mBuffer.writeRawBuffer(paramArrayOfByte);
    }
  }
  
  @Deprecated
  public void writePackedBool(long paramLong, boolean[] paramArrayOfBoolean)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5531917877248L);
    int j = 0;
    int k;
    if (paramArrayOfBoolean != null) {
      k = paramArrayOfBoolean.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      writeKnownLengthHeader(i, k);
      while (j < k)
      {
        mBuffer.writeRawByte((byte)paramArrayOfBoolean[j]);
        j++;
      }
    }
  }
  
  @Deprecated
  public void writePackedDouble(long paramLong, double[] paramArrayOfDouble)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5501853106176L);
    int j = 0;
    int k;
    if (paramArrayOfDouble != null) {
      k = paramArrayOfDouble.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      writeKnownLengthHeader(i, k * 8);
      while (j < k)
      {
        mBuffer.writeRawFixed64(Double.doubleToLongBits(paramArrayOfDouble[j]));
        j++;
      }
    }
  }
  
  @Deprecated
  public void writePackedEnum(long paramLong, int[] paramArrayOfInt)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5557687681024L);
    int j = 0;
    int k;
    if (paramArrayOfInt != null) {
      k = paramArrayOfInt.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      int m = 0;
      for (int n = 0; n < k; n++)
      {
        int i1 = paramArrayOfInt[n];
        if (i1 >= 0) {
          i1 = EncodedBuffer.getRawVarint32Size(i1);
        } else {
          i1 = 10;
        }
        m += i1;
      }
      writeKnownLengthHeader(i, m);
      for (n = j; n < k; n++) {
        writeUnsignedVarintFromSignedInt(paramArrayOfInt[n]);
      }
    }
  }
  
  @Deprecated
  public void writePackedFixed32(long paramLong, int[] paramArrayOfInt)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5527622909952L);
    int j = 0;
    int k;
    if (paramArrayOfInt != null) {
      k = paramArrayOfInt.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      writeKnownLengthHeader(i, k * 4);
      while (j < k)
      {
        mBuffer.writeRawFixed32(paramArrayOfInt[j]);
        j++;
      }
    }
  }
  
  @Deprecated
  public void writePackedFixed64(long paramLong, long[] paramArrayOfLong)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5523327942656L);
    int j = 0;
    int k;
    if (paramArrayOfLong != null) {
      k = paramArrayOfLong.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      writeKnownLengthHeader(i, k * 8);
      while (j < k)
      {
        mBuffer.writeRawFixed64(paramArrayOfLong[j]);
        j++;
      }
    }
  }
  
  @Deprecated
  public void writePackedFloat(long paramLong, float[] paramArrayOfFloat)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5506148073472L);
    int j = 0;
    int k;
    if (paramArrayOfFloat != null) {
      k = paramArrayOfFloat.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      writeKnownLengthHeader(i, k * 4);
      while (j < k)
      {
        mBuffer.writeRawFixed32(Float.floatToIntBits(paramArrayOfFloat[j]));
        j++;
      }
    }
  }
  
  @Deprecated
  public void writePackedInt32(long paramLong, int[] paramArrayOfInt)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5519032975360L);
    int j = 0;
    int k;
    if (paramArrayOfInt != null) {
      k = paramArrayOfInt.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      int m = 0;
      for (int n = 0; n < k; n++)
      {
        int i1 = paramArrayOfInt[n];
        if (i1 >= 0) {
          i1 = EncodedBuffer.getRawVarint32Size(i1);
        } else {
          i1 = 10;
        }
        m += i1;
      }
      writeKnownLengthHeader(i, m);
      for (m = j; m < k; m++) {
        writeUnsignedVarintFromSignedInt(paramArrayOfInt[m]);
      }
    }
  }
  
  @Deprecated
  public void writePackedInt64(long paramLong, long[] paramArrayOfLong)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5510443040768L);
    int j = 0;
    int k;
    if (paramArrayOfLong != null) {
      k = paramArrayOfLong.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      int m = 0;
      for (int n = 0; n < k; n++) {
        m += EncodedBuffer.getRawVarint64Size(paramArrayOfLong[n]);
      }
      writeKnownLengthHeader(i, m);
      for (m = j; m < k; m++) {
        mBuffer.writeRawVarint64(paramArrayOfLong[m]);
      }
    }
  }
  
  @Deprecated
  public void writePackedSFixed32(long paramLong, int[] paramArrayOfInt)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5561982648320L);
    int j = 0;
    int k;
    if (paramArrayOfInt != null) {
      k = paramArrayOfInt.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      writeKnownLengthHeader(i, k * 4);
      while (j < k)
      {
        mBuffer.writeRawFixed32(paramArrayOfInt[j]);
        j++;
      }
    }
  }
  
  @Deprecated
  public void writePackedSFixed64(long paramLong, long[] paramArrayOfLong)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5566277615616L);
    int j = 0;
    int k;
    if (paramArrayOfLong != null) {
      k = paramArrayOfLong.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      writeKnownLengthHeader(i, k * 8);
      while (j < k)
      {
        mBuffer.writeRawFixed64(paramArrayOfLong[j]);
        j++;
      }
    }
  }
  
  @Deprecated
  public void writePackedSInt32(long paramLong, int[] paramArrayOfInt)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5570572582912L);
    int j = 0;
    int k;
    if (paramArrayOfInt != null) {
      k = paramArrayOfInt.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      int m = 0;
      for (int n = 0; n < k; n++) {
        m += EncodedBuffer.getRawZigZag32Size(paramArrayOfInt[n]);
      }
      writeKnownLengthHeader(i, m);
      for (m = j; m < k; m++) {
        mBuffer.writeRawZigZag32(paramArrayOfInt[m]);
      }
    }
  }
  
  @Deprecated
  public void writePackedSInt64(long paramLong, long[] paramArrayOfLong)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5574867550208L);
    int j = 0;
    int k;
    if (paramArrayOfLong != null) {
      k = paramArrayOfLong.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      int m = 0;
      for (int n = 0; n < k; n++) {
        m += EncodedBuffer.getRawZigZag64Size(paramArrayOfLong[n]);
      }
      writeKnownLengthHeader(i, m);
      for (m = j; m < k; m++) {
        mBuffer.writeRawZigZag64(paramArrayOfLong[m]);
      }
    }
  }
  
  @Deprecated
  public void writePackedUInt32(long paramLong, int[] paramArrayOfInt)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5553392713728L);
    int j = 0;
    int k;
    if (paramArrayOfInt != null) {
      k = paramArrayOfInt.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      int m = 0;
      for (int n = 0; n < k; n++) {
        m += EncodedBuffer.getRawVarint32Size(paramArrayOfInt[n]);
      }
      writeKnownLengthHeader(i, m);
      for (n = j; n < k; n++) {
        mBuffer.writeRawVarint32(paramArrayOfInt[n]);
      }
    }
  }
  
  @Deprecated
  public void writePackedUInt64(long paramLong, long[] paramArrayOfLong)
  {
    assertNotCompacted();
    int i = checkFieldId(paramLong, 5514738008064L);
    int j = 0;
    int k;
    if (paramArrayOfLong != null) {
      k = paramArrayOfLong.length;
    } else {
      k = 0;
    }
    if (k > 0)
    {
      int m = 0;
      for (int n = 0; n < k; n++) {
        m += EncodedBuffer.getRawVarint64Size(paramArrayOfLong[n]);
      }
      writeKnownLengthHeader(i, m);
      for (m = j; m < k; m++) {
        mBuffer.writeRawVarint64(paramArrayOfLong[m]);
      }
    }
  }
  
  @Deprecated
  public void writeRepeatedBool(long paramLong, boolean paramBoolean)
  {
    assertNotCompacted();
    writeRepeatedBoolImpl(checkFieldId(paramLong, 2233382993920L), paramBoolean);
  }
  
  @Deprecated
  public void writeRepeatedBytes(long paramLong, byte[] paramArrayOfByte)
  {
    assertNotCompacted();
    writeRepeatedBytesImpl(checkFieldId(paramLong, 2250562863104L), paramArrayOfByte);
  }
  
  @Deprecated
  public void writeRepeatedDouble(long paramLong, double paramDouble)
  {
    assertNotCompacted();
    writeRepeatedDoubleImpl(checkFieldId(paramLong, 2203318222848L), paramDouble);
  }
  
  @Deprecated
  public void writeRepeatedEnum(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeRepeatedEnumImpl(checkFieldId(paramLong, 2259152797696L), paramInt);
  }
  
  @Deprecated
  public void writeRepeatedFixed32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeRepeatedFixed32Impl(checkFieldId(paramLong, 2229088026624L), paramInt);
  }
  
  @Deprecated
  public void writeRepeatedFixed64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeRepeatedFixed64Impl(checkFieldId(paramLong1, 2224793059328L), paramLong2);
  }
  
  @Deprecated
  public void writeRepeatedFloat(long paramLong, float paramFloat)
  {
    assertNotCompacted();
    writeRepeatedFloatImpl(checkFieldId(paramLong, 2207613190144L), paramFloat);
  }
  
  @Deprecated
  public void writeRepeatedInt32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeRepeatedInt32Impl(checkFieldId(paramLong, 2220498092032L), paramInt);
  }
  
  @Deprecated
  public void writeRepeatedInt64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeRepeatedInt64Impl(checkFieldId(paramLong1, 2211908157440L), paramLong2);
  }
  
  @Deprecated
  public void writeRepeatedObject(long paramLong, byte[] paramArrayOfByte)
  {
    assertNotCompacted();
    writeRepeatedObjectImpl(checkFieldId(paramLong, 2246267895808L), paramArrayOfByte);
  }
  
  void writeRepeatedObjectImpl(int paramInt, byte[] paramArrayOfByte)
  {
    int i;
    if (paramArrayOfByte == null) {
      i = 0;
    } else {
      i = paramArrayOfByte.length;
    }
    writeKnownLengthHeader(paramInt, i);
    mBuffer.writeRawBuffer(paramArrayOfByte);
  }
  
  @Deprecated
  public void writeRepeatedSFixed32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeRepeatedSFixed32Impl(checkFieldId(paramLong, 2263447764992L), paramInt);
  }
  
  @Deprecated
  public void writeRepeatedSFixed64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeRepeatedSFixed64Impl(checkFieldId(paramLong1, 2267742732288L), paramLong2);
  }
  
  @Deprecated
  public void writeRepeatedSInt32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeRepeatedSInt32Impl(checkFieldId(paramLong, 2272037699584L), paramInt);
  }
  
  @Deprecated
  public void writeRepeatedSInt64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeRepeatedSInt64Impl(checkFieldId(paramLong1, 2276332666880L), paramLong2);
  }
  
  @Deprecated
  public void writeRepeatedString(long paramLong, String paramString)
  {
    assertNotCompacted();
    writeRepeatedStringImpl(checkFieldId(paramLong, 2237677961216L), paramString);
  }
  
  @Deprecated
  public void writeRepeatedUInt32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeRepeatedUInt32Impl(checkFieldId(paramLong, 2254857830400L), paramInt);
  }
  
  @Deprecated
  public void writeRepeatedUInt64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeRepeatedUInt64Impl(checkFieldId(paramLong1, 2216203124736L), paramLong2);
  }
  
  @Deprecated
  public void writeSFixed32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeSFixed32Impl(checkFieldId(paramLong, 1163936137216L), paramInt);
  }
  
  @Deprecated
  public void writeSFixed64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeSFixed64Impl(checkFieldId(paramLong1, 1168231104512L), paramLong2);
  }
  
  @Deprecated
  public void writeSInt32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeSInt32Impl(checkFieldId(paramLong, 1172526071808L), paramInt);
  }
  
  @Deprecated
  public void writeSInt64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeSInt64Impl(checkFieldId(paramLong1, 1176821039104L), paramLong2);
  }
  
  @Deprecated
  public void writeString(long paramLong, String paramString)
  {
    assertNotCompacted();
    writeStringImpl(checkFieldId(paramLong, 1138166333440L), paramString);
  }
  
  public void writeTag(int paramInt1, int paramInt2)
  {
    mBuffer.writeRawVarint32(paramInt1 << 3 | paramInt2);
  }
  
  @Deprecated
  public void writeUInt32(long paramLong, int paramInt)
  {
    assertNotCompacted();
    writeUInt32Impl(checkFieldId(paramLong, 1155346202624L), paramInt);
  }
  
  @Deprecated
  public void writeUInt64(long paramLong1, long paramLong2)
  {
    assertNotCompacted();
    writeUInt64Impl(checkFieldId(paramLong1, 1116691496960L), paramLong2);
  }
}
