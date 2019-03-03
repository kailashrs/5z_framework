package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalHelpers;
import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Rational;
import java.nio.ByteBuffer;

public final class MarshalQueryablePrimitive<T>
  implements MarshalQueryable<T>
{
  public MarshalQueryablePrimitive() {}
  
  public Marshaler<T> createMarshaler(TypeReference<T> paramTypeReference, int paramInt)
  {
    return new MarshalerPrimitive(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<T> paramTypeReference, int paramInt)
  {
    boolean bool1 = paramTypeReference.getType() instanceof Class;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool6 = false;
    boolean bool7 = false;
    if (bool1)
    {
      paramTypeReference = (Class)paramTypeReference.getType();
      if ((paramTypeReference != Byte.TYPE) && (paramTypeReference != Byte.class))
      {
        if ((paramTypeReference != Integer.TYPE) && (paramTypeReference != Integer.class))
        {
          if ((paramTypeReference != Float.TYPE) && (paramTypeReference != Float.class))
          {
            if ((paramTypeReference != Long.TYPE) && (paramTypeReference != Long.class))
            {
              if ((paramTypeReference != Double.TYPE) && (paramTypeReference != Double.class))
              {
                if (paramTypeReference == Rational.class)
                {
                  if (paramInt == 5) {
                    bool7 = true;
                  }
                  return bool7;
                }
              }
              else
              {
                bool7 = bool2;
                if (paramInt == 4) {
                  bool7 = true;
                }
                return bool7;
              }
            }
            else
            {
              bool7 = bool3;
              if (paramInt == 3) {
                bool7 = true;
              }
              return bool7;
            }
          }
          else
          {
            bool7 = bool4;
            if (paramInt == 2) {
              bool7 = true;
            }
            return bool7;
          }
        }
        else
        {
          bool7 = bool5;
          if (paramInt == 1) {
            bool7 = true;
          }
          return bool7;
        }
      }
      else
      {
        bool7 = bool6;
        if (paramInt == 0) {
          bool7 = true;
        }
        return bool7;
      }
    }
    return false;
  }
  
  private class MarshalerPrimitive
    extends Marshaler<T>
  {
    private final Class<T> mClass;
    
    protected MarshalerPrimitive(int paramInt)
    {
      super(paramInt, i);
      mClass = MarshalHelpers.wrapClassIfPrimitive(paramInt.getRawType());
    }
    
    private void marshalPrimitive(byte paramByte, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(paramByte);
    }
    
    private void marshalPrimitive(double paramDouble, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putDouble(paramDouble);
    }
    
    private void marshalPrimitive(float paramFloat, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putFloat(paramFloat);
    }
    
    private void marshalPrimitive(int paramInt, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putInt(paramInt);
    }
    
    private void marshalPrimitive(long paramLong, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putLong(paramLong);
    }
    
    private void marshalPrimitive(Rational paramRational, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putInt(paramRational.getNumerator());
      paramByteBuffer.putInt(paramRational.getDenominator());
    }
    
    private Object unmarshalObject(ByteBuffer paramByteBuffer)
    {
      switch (mNativeType)
      {
      default: 
        paramByteBuffer = new StringBuilder();
        paramByteBuffer.append("Can't unmarshal native type ");
        paramByteBuffer.append(mNativeType);
        throw new UnsupportedOperationException(paramByteBuffer.toString());
      case 5: 
        return new Rational(paramByteBuffer.getInt(), paramByteBuffer.getInt());
      case 4: 
        return Double.valueOf(paramByteBuffer.getDouble());
      case 3: 
        return Long.valueOf(paramByteBuffer.getLong());
      case 2: 
        return Float.valueOf(paramByteBuffer.getFloat());
      case 1: 
        return Integer.valueOf(paramByteBuffer.getInt());
      }
      return Byte.valueOf(paramByteBuffer.get());
    }
    
    public int calculateMarshalSize(T paramT)
    {
      return MarshalHelpers.getPrimitiveTypeSize(mNativeType);
    }
    
    public int getNativeSize()
    {
      return MarshalHelpers.getPrimitiveTypeSize(mNativeType);
    }
    
    public void marshal(T paramT, ByteBuffer paramByteBuffer)
    {
      if ((paramT instanceof Integer))
      {
        MarshalHelpers.checkNativeTypeEquals(1, mNativeType);
        marshalPrimitive(((Integer)paramT).intValue(), paramByteBuffer);
      }
      else if ((paramT instanceof Float))
      {
        MarshalHelpers.checkNativeTypeEquals(2, mNativeType);
        marshalPrimitive(((Float)paramT).floatValue(), paramByteBuffer);
      }
      else if ((paramT instanceof Long))
      {
        MarshalHelpers.checkNativeTypeEquals(3, mNativeType);
        marshalPrimitive(((Long)paramT).longValue(), paramByteBuffer);
      }
      else if ((paramT instanceof Rational))
      {
        MarshalHelpers.checkNativeTypeEquals(5, mNativeType);
        marshalPrimitive((Rational)paramT, paramByteBuffer);
      }
      else if ((paramT instanceof Double))
      {
        MarshalHelpers.checkNativeTypeEquals(4, mNativeType);
        marshalPrimitive(((Double)paramT).doubleValue(), paramByteBuffer);
      }
      else
      {
        if (!(paramT instanceof Byte)) {
          break label181;
        }
        MarshalHelpers.checkNativeTypeEquals(0, mNativeType);
        marshalPrimitive(((Byte)paramT).byteValue(), paramByteBuffer);
      }
      return;
      label181:
      paramT = new StringBuilder();
      paramT.append("Can't marshal managed type ");
      paramT.append(mTypeReference);
      throw new UnsupportedOperationException(paramT.toString());
    }
    
    public T unmarshal(ByteBuffer paramByteBuffer)
    {
      return mClass.cast(unmarshalObject(paramByteBuffer));
    }
  }
}
