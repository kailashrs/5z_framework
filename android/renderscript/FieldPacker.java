package android.renderscript;

import android.util.Log;
import java.util.BitSet;

public class FieldPacker
{
  private BitSet mAlignment;
  private byte[] mData;
  private int mLen;
  private int mPos;
  
  public FieldPacker(int paramInt)
  {
    mPos = 0;
    mLen = paramInt;
    mData = new byte[paramInt];
    mAlignment = new BitSet();
  }
  
  public FieldPacker(byte[] paramArrayOfByte)
  {
    mPos = paramArrayOfByte.length;
    mLen = paramArrayOfByte.length;
    mData = paramArrayOfByte;
    mAlignment = new BitSet();
  }
  
  private void add(Object paramObject)
  {
    if ((paramObject instanceof Boolean))
    {
      addBoolean(((Boolean)paramObject).booleanValue());
      return;
    }
    if ((paramObject instanceof Byte))
    {
      addI8(((Byte)paramObject).byteValue());
      return;
    }
    if ((paramObject instanceof Short))
    {
      addI16(((Short)paramObject).shortValue());
      return;
    }
    if ((paramObject instanceof Integer))
    {
      addI32(((Integer)paramObject).intValue());
      return;
    }
    if ((paramObject instanceof Long))
    {
      addI64(((Long)paramObject).longValue());
      return;
    }
    if ((paramObject instanceof Float))
    {
      addF32(((Float)paramObject).floatValue());
      return;
    }
    if ((paramObject instanceof Double))
    {
      addF64(((Double)paramObject).doubleValue());
      return;
    }
    if ((paramObject instanceof Byte2))
    {
      addI8((Byte2)paramObject);
      return;
    }
    if ((paramObject instanceof Byte3))
    {
      addI8((Byte3)paramObject);
      return;
    }
    if ((paramObject instanceof Byte4))
    {
      addI8((Byte4)paramObject);
      return;
    }
    if ((paramObject instanceof Short2))
    {
      addI16((Short2)paramObject);
      return;
    }
    if ((paramObject instanceof Short3))
    {
      addI16((Short3)paramObject);
      return;
    }
    if ((paramObject instanceof Short4))
    {
      addI16((Short4)paramObject);
      return;
    }
    if ((paramObject instanceof Int2))
    {
      addI32((Int2)paramObject);
      return;
    }
    if ((paramObject instanceof Int3))
    {
      addI32((Int3)paramObject);
      return;
    }
    if ((paramObject instanceof Int4))
    {
      addI32((Int4)paramObject);
      return;
    }
    if ((paramObject instanceof Long2))
    {
      addI64((Long2)paramObject);
      return;
    }
    if ((paramObject instanceof Long3))
    {
      addI64((Long3)paramObject);
      return;
    }
    if ((paramObject instanceof Long4))
    {
      addI64((Long4)paramObject);
      return;
    }
    if ((paramObject instanceof Float2))
    {
      addF32((Float2)paramObject);
      return;
    }
    if ((paramObject instanceof Float3))
    {
      addF32((Float3)paramObject);
      return;
    }
    if ((paramObject instanceof Float4))
    {
      addF32((Float4)paramObject);
      return;
    }
    if ((paramObject instanceof Double2))
    {
      addF64((Double2)paramObject);
      return;
    }
    if ((paramObject instanceof Double3))
    {
      addF64((Double3)paramObject);
      return;
    }
    if ((paramObject instanceof Double4))
    {
      addF64((Double4)paramObject);
      return;
    }
    if ((paramObject instanceof Matrix2f))
    {
      addMatrix((Matrix2f)paramObject);
      return;
    }
    if ((paramObject instanceof Matrix3f))
    {
      addMatrix((Matrix3f)paramObject);
      return;
    }
    if ((paramObject instanceof Matrix4f))
    {
      addMatrix((Matrix4f)paramObject);
      return;
    }
    if ((paramObject instanceof BaseObj))
    {
      addObj((BaseObj)paramObject);
      return;
    }
  }
  
  private void addSafely(Object paramObject)
  {
    int i = mPos;
    int j;
    do
    {
      j = 0;
      try
      {
        add(paramObject);
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
        mPos = i;
        resize(mLen * 2);
        j = 1;
      }
    } while (j != 0);
  }
  
  static FieldPacker createFromArray(Object[] paramArrayOfObject)
  {
    FieldPacker localFieldPacker = new FieldPacker(RenderScript.sPointerSize * 8);
    int i = paramArrayOfObject.length;
    for (int j = 0; j < i; j++) {
      localFieldPacker.addSafely(paramArrayOfObject[j]);
    }
    localFieldPacker.resize(mPos);
    return localFieldPacker;
  }
  
  private boolean resize(int paramInt)
  {
    if (paramInt == mLen) {
      return false;
    }
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(mData, 0, arrayOfByte, 0, mPos);
    mData = arrayOfByte;
    mLen = paramInt;
    return true;
  }
  
  public void addBoolean(boolean paramBoolean)
  {
    addI8((byte)paramBoolean);
  }
  
  public void addF32(float paramFloat)
  {
    addI32(Float.floatToRawIntBits(paramFloat));
  }
  
  public void addF32(Float2 paramFloat2)
  {
    addF32(x);
    addF32(y);
  }
  
  public void addF32(Float3 paramFloat3)
  {
    addF32(x);
    addF32(y);
    addF32(z);
  }
  
  public void addF32(Float4 paramFloat4)
  {
    addF32(x);
    addF32(y);
    addF32(z);
    addF32(w);
  }
  
  public void addF64(double paramDouble)
  {
    addI64(Double.doubleToRawLongBits(paramDouble));
  }
  
  public void addF64(Double2 paramDouble2)
  {
    addF64(x);
    addF64(y);
  }
  
  public void addF64(Double3 paramDouble3)
  {
    addF64(x);
    addF64(y);
    addF64(z);
  }
  
  public void addF64(Double4 paramDouble4)
  {
    addF64(x);
    addF64(y);
    addF64(z);
    addF64(w);
  }
  
  public void addI16(Short2 paramShort2)
  {
    addI16(x);
    addI16(y);
  }
  
  public void addI16(Short3 paramShort3)
  {
    addI16(x);
    addI16(y);
    addI16(z);
  }
  
  public void addI16(Short4 paramShort4)
  {
    addI16(x);
    addI16(y);
    addI16(z);
    addI16(w);
  }
  
  public void addI16(short paramShort)
  {
    align(2);
    byte[] arrayOfByte = mData;
    int i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(paramShort & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(paramShort >> 8));
  }
  
  public void addI32(int paramInt)
  {
    align(4);
    byte[] arrayOfByte = mData;
    int i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(paramInt & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(paramInt >> 8 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(paramInt >> 16 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(paramInt >> 24 & 0xFF));
  }
  
  public void addI32(Int2 paramInt2)
  {
    addI32(x);
    addI32(y);
  }
  
  public void addI32(Int3 paramInt3)
  {
    addI32(x);
    addI32(y);
    addI32(z);
  }
  
  public void addI32(Int4 paramInt4)
  {
    addI32(x);
    addI32(y);
    addI32(z);
    addI32(w);
  }
  
  public void addI64(long paramLong)
  {
    align(8);
    byte[] arrayOfByte = mData;
    int i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 8 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 16 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 24 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 32 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 40 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 48 & 0xFF));
    arrayOfByte = mData;
    i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 56 & 0xFF));
  }
  
  public void addI64(Long2 paramLong2)
  {
    addI64(x);
    addI64(y);
  }
  
  public void addI64(Long3 paramLong3)
  {
    addI64(x);
    addI64(y);
    addI64(z);
  }
  
  public void addI64(Long4 paramLong4)
  {
    addI64(x);
    addI64(y);
    addI64(z);
    addI64(w);
  }
  
  public void addI8(byte paramByte)
  {
    byte[] arrayOfByte = mData;
    int i = mPos;
    mPos = (i + 1);
    arrayOfByte[i] = ((byte)paramByte);
  }
  
  public void addI8(Byte2 paramByte2)
  {
    addI8(x);
    addI8(y);
  }
  
  public void addI8(Byte3 paramByte3)
  {
    addI8(x);
    addI8(y);
    addI8(z);
  }
  
  public void addI8(Byte4 paramByte4)
  {
    addI8(x);
    addI8(y);
    addI8(z);
    addI8(w);
  }
  
  public void addMatrix(Matrix2f paramMatrix2f)
  {
    for (int i = 0; i < mMat.length; i++) {
      addF32(mMat[i]);
    }
  }
  
  public void addMatrix(Matrix3f paramMatrix3f)
  {
    for (int i = 0; i < mMat.length; i++) {
      addF32(mMat[i]);
    }
  }
  
  public void addMatrix(Matrix4f paramMatrix4f)
  {
    for (int i = 0; i < mMat.length; i++) {
      addF32(mMat[i]);
    }
  }
  
  public void addObj(BaseObj paramBaseObj)
  {
    if (paramBaseObj != null)
    {
      if (RenderScript.sPointerSize == 8)
      {
        addI64(paramBaseObj.getID(null));
        addI64(0L);
        addI64(0L);
        addI64(0L);
      }
      else
      {
        addI32((int)paramBaseObj.getID(null));
      }
    }
    else if (RenderScript.sPointerSize == 8)
    {
      addI64(0L);
      addI64(0L);
      addI64(0L);
      addI64(0L);
    }
    else
    {
      addI32(0);
    }
  }
  
  public void addU16(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 65535))
    {
      align(2);
      localObject = mData;
      int i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(paramInt & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(paramInt >> 8));
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("FieldPacker.addU16( ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" )");
    Log.e("rs", ((StringBuilder)localObject).toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void addU16(Int2 paramInt2)
  {
    addU16(x);
    addU16(y);
  }
  
  public void addU16(Int3 paramInt3)
  {
    addU16(x);
    addU16(y);
    addU16(z);
  }
  
  public void addU16(Int4 paramInt4)
  {
    addU16(x);
    addU16(y);
    addU16(z);
    addU16(w);
  }
  
  public void addU32(long paramLong)
  {
    if ((paramLong >= 0L) && (paramLong <= 4294967295L))
    {
      align(4);
      localObject = mData;
      int i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 8 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 16 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(0xFF & paramLong >> 24));
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("FieldPacker.addU32( ");
    ((StringBuilder)localObject).append(paramLong);
    ((StringBuilder)localObject).append(" )");
    Log.e("rs", ((StringBuilder)localObject).toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void addU32(Long2 paramLong2)
  {
    addU32(x);
    addU32(y);
  }
  
  public void addU32(Long3 paramLong3)
  {
    addU32(x);
    addU32(y);
    addU32(z);
  }
  
  public void addU32(Long4 paramLong4)
  {
    addU32(x);
    addU32(y);
    addU32(z);
    addU32(w);
  }
  
  public void addU64(long paramLong)
  {
    if (paramLong >= 0L)
    {
      align(8);
      localObject = mData;
      int i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 8 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 16 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 24 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 32 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 40 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 48 & 0xFF));
      localObject = mData;
      i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)(int)(paramLong >> 56 & 0xFF));
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("FieldPacker.addU64( ");
    ((StringBuilder)localObject).append(paramLong);
    ((StringBuilder)localObject).append(" )");
    Log.e("rs", ((StringBuilder)localObject).toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void addU64(Long2 paramLong2)
  {
    addU64(x);
    addU64(y);
  }
  
  public void addU64(Long3 paramLong3)
  {
    addU64(x);
    addU64(y);
    addU64(z);
  }
  
  public void addU64(Long4 paramLong4)
  {
    addU64(x);
    addU64(y);
    addU64(z);
    addU64(w);
  }
  
  public void addU8(Short2 paramShort2)
  {
    addU8(x);
    addU8(y);
  }
  
  public void addU8(Short3 paramShort3)
  {
    addU8(x);
    addU8(y);
    addU8(z);
  }
  
  public void addU8(Short4 paramShort4)
  {
    addU8(x);
    addU8(y);
    addU8(z);
    addU8(w);
  }
  
  public void addU8(short paramShort)
  {
    if ((paramShort >= 0) && (paramShort <= 255))
    {
      localObject = mData;
      int i = mPos;
      mPos = (i + 1);
      localObject[i] = ((byte)(byte)paramShort);
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("FieldPacker.addU8( ");
    ((StringBuilder)localObject).append(paramShort);
    ((StringBuilder)localObject).append(" )");
    Log.e("rs", ((StringBuilder)localObject).toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void align(int paramInt)
  {
    if ((paramInt > 0) && ((paramInt - 1 & paramInt) == 0))
    {
      while ((mPos & paramInt - 1) != 0)
      {
        mAlignment.flip(mPos);
        localObject = mData;
        int i = mPos;
        mPos = (i + 1);
        localObject[i] = ((byte)0);
      }
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("argument must be a non-negative non-zero power of 2: ");
    ((StringBuilder)localObject).append(paramInt);
    throw new RSIllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public final byte[] getData()
  {
    return mData;
  }
  
  public int getPos()
  {
    return mPos;
  }
  
  public void reset()
  {
    mPos = 0;
  }
  
  public void reset(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= mLen))
    {
      mPos = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("out of range argument: ");
    localStringBuilder.append(paramInt);
    throw new RSIllegalArgumentException(localStringBuilder.toString());
  }
  
  public void skip(int paramInt)
  {
    int i = mPos + paramInt;
    if ((i >= 0) && (i <= mLen))
    {
      mPos = i;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("out of range argument: ");
    localStringBuilder.append(paramInt);
    throw new RSIllegalArgumentException(localStringBuilder.toString());
  }
  
  public boolean subBoolean()
  {
    return subI8() == 1;
  }
  
  public Byte2 subByte2()
  {
    Byte2 localByte2 = new Byte2();
    y = subI8();
    x = subI8();
    return localByte2;
  }
  
  public Byte3 subByte3()
  {
    Byte3 localByte3 = new Byte3();
    z = subI8();
    y = subI8();
    x = subI8();
    return localByte3;
  }
  
  public Byte4 subByte4()
  {
    Byte4 localByte4 = new Byte4();
    w = subI8();
    z = subI8();
    y = subI8();
    x = subI8();
    return localByte4;
  }
  
  public Double2 subDouble2()
  {
    Double2 localDouble2 = new Double2();
    y = subF64();
    x = subF64();
    return localDouble2;
  }
  
  public Double3 subDouble3()
  {
    Double3 localDouble3 = new Double3();
    z = subF64();
    y = subF64();
    x = subF64();
    return localDouble3;
  }
  
  public Double4 subDouble4()
  {
    Double4 localDouble4 = new Double4();
    w = subF64();
    z = subF64();
    y = subF64();
    x = subF64();
    return localDouble4;
  }
  
  public float subF32()
  {
    return Float.intBitsToFloat(subI32());
  }
  
  public double subF64()
  {
    return Double.longBitsToDouble(subI64());
  }
  
  public Float2 subFloat2()
  {
    Float2 localFloat2 = new Float2();
    y = subF32();
    x = subF32();
    return localFloat2;
  }
  
  public Float3 subFloat3()
  {
    Float3 localFloat3 = new Float3();
    z = subF32();
    y = subF32();
    x = subF32();
    return localFloat3;
  }
  
  public Float4 subFloat4()
  {
    Float4 localFloat4 = new Float4();
    w = subF32();
    z = subF32();
    y = subF32();
    x = subF32();
    return localFloat4;
  }
  
  public short subI16()
  {
    subalign(2);
    byte[] arrayOfByte = mData;
    int i = mPos - 1;
    mPos = i;
    i = (short)((arrayOfByte[i] & 0xFF) << 8);
    arrayOfByte = mData;
    int j = mPos - 1;
    mPos = j;
    return (short)((short)(arrayOfByte[j] & 0xFF) | i);
  }
  
  public int subI32()
  {
    subalign(4);
    byte[] arrayOfByte = mData;
    int i = mPos - 1;
    mPos = i;
    i = arrayOfByte[i];
    arrayOfByte = mData;
    int j = mPos - 1;
    mPos = j;
    j = arrayOfByte[j];
    arrayOfByte = mData;
    int k = mPos - 1;
    mPos = k;
    k = arrayOfByte[k];
    arrayOfByte = mData;
    int m = mPos - 1;
    mPos = m;
    return (i & 0xFF) << 24 | (j & 0xFF) << 16 | (k & 0xFF) << 8 | arrayOfByte[m] & 0xFF;
  }
  
  public long subI64()
  {
    subalign(8);
    byte[] arrayOfByte = mData;
    int i = mPos - 1;
    mPos = i;
    long l1 = arrayOfByte[i];
    arrayOfByte = mData;
    i = mPos - 1;
    mPos = i;
    long l2 = arrayOfByte[i];
    arrayOfByte = mData;
    i = mPos - 1;
    mPos = i;
    long l3 = arrayOfByte[i];
    arrayOfByte = mData;
    i = mPos - 1;
    mPos = i;
    long l4 = arrayOfByte[i];
    arrayOfByte = mData;
    i = mPos - 1;
    mPos = i;
    long l5 = arrayOfByte[i];
    arrayOfByte = mData;
    i = mPos - 1;
    mPos = i;
    long l6 = arrayOfByte[i];
    arrayOfByte = mData;
    i = mPos - 1;
    mPos = i;
    long l7 = arrayOfByte[i];
    arrayOfByte = mData;
    i = mPos - 1;
    mPos = i;
    return 0L | (l1 & 0xFF) << 56 | (l2 & 0xFF) << 48 | (l3 & 0xFF) << 40 | (l4 & 0xFF) << 32 | (l5 & 0xFF) << 24 | (l6 & 0xFF) << 16 | (l7 & 0xFF) << 8 | arrayOfByte[i] & 0xFF;
  }
  
  public byte subI8()
  {
    subalign(1);
    byte[] arrayOfByte = mData;
    int i = mPos - 1;
    mPos = i;
    return arrayOfByte[i];
  }
  
  public Int2 subInt2()
  {
    Int2 localInt2 = new Int2();
    y = subI32();
    x = subI32();
    return localInt2;
  }
  
  public Int3 subInt3()
  {
    Int3 localInt3 = new Int3();
    z = subI32();
    y = subI32();
    x = subI32();
    return localInt3;
  }
  
  public Int4 subInt4()
  {
    Int4 localInt4 = new Int4();
    w = subI32();
    z = subI32();
    y = subI32();
    x = subI32();
    return localInt4;
  }
  
  public Long2 subLong2()
  {
    Long2 localLong2 = new Long2();
    y = subI64();
    x = subI64();
    return localLong2;
  }
  
  public Long3 subLong3()
  {
    Long3 localLong3 = new Long3();
    z = subI64();
    y = subI64();
    x = subI64();
    return localLong3;
  }
  
  public Long4 subLong4()
  {
    Long4 localLong4 = new Long4();
    w = subI64();
    z = subI64();
    y = subI64();
    x = subI64();
    return localLong4;
  }
  
  public Matrix2f subMatrix2f()
  {
    Matrix2f localMatrix2f = new Matrix2f();
    for (int i = mMat.length - 1; i >= 0; i--) {
      mMat[i] = subF32();
    }
    return localMatrix2f;
  }
  
  public Matrix3f subMatrix3f()
  {
    Matrix3f localMatrix3f = new Matrix3f();
    for (int i = mMat.length - 1; i >= 0; i--) {
      mMat[i] = subF32();
    }
    return localMatrix3f;
  }
  
  public Matrix4f subMatrix4f()
  {
    Matrix4f localMatrix4f = new Matrix4f();
    for (int i = mMat.length - 1; i >= 0; i--) {
      mMat[i] = subF32();
    }
    return localMatrix4f;
  }
  
  public Short2 subShort2()
  {
    Short2 localShort2 = new Short2();
    y = subI16();
    x = subI16();
    return localShort2;
  }
  
  public Short3 subShort3()
  {
    Short3 localShort3 = new Short3();
    z = subI16();
    y = subI16();
    x = subI16();
    return localShort3;
  }
  
  public Short4 subShort4()
  {
    Short4 localShort4 = new Short4();
    w = subI16();
    z = subI16();
    y = subI16();
    x = subI16();
    return localShort4;
  }
  
  public void subalign(int paramInt)
  {
    if ((paramInt - 1 & paramInt) == 0)
    {
      while ((mPos & paramInt - 1) != 0) {
        mPos -= 1;
      }
      if (mPos > 0) {
        while (mAlignment.get(mPos - 1) == true)
        {
          mPos -= 1;
          mAlignment.flip(mPos);
        }
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("argument must be a non-negative non-zero power of 2: ");
    localStringBuilder.append(paramInt);
    throw new RSIllegalArgumentException(localStringBuilder.toString());
  }
}
