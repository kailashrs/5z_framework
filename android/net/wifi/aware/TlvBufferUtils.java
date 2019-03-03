package android.net.wifi.aware;

import java.nio.BufferOverflowException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import libcore.io.Memory;

public class TlvBufferUtils
{
  private TlvBufferUtils() {}
  
  public static boolean isValid(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 2))
    {
      if ((paramInt2 > 0) && (paramInt2 <= 2))
      {
        boolean bool = true;
        if (paramArrayOfByte == null) {
          return true;
        }
        int i = 0;
        while (i + paramInt1 + paramInt2 <= paramArrayOfByte.length)
        {
          i += paramInt1;
          if (paramInt2 == 1) {
            i += paramArrayOfByte[i] + paramInt2;
          } else {
            i += Memory.peekShort(paramArrayOfByte, i, ByteOrder.BIG_ENDIAN) + paramInt2;
          }
        }
        if (i != paramArrayOfByte.length) {
          bool = false;
        }
        return bool;
      }
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Invalid arguments - lengthSize must be 1 or 2: lengthSize=");
      paramArrayOfByte.append(paramInt2);
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Invalid arguments - typeSize must be 0, 1, or 2: typeSize=");
    paramArrayOfByte.append(paramInt1);
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  public static class TlvConstructor
  {
    private byte[] mArray;
    private int mArrayLength;
    private int mLengthSize;
    private int mPosition;
    private int mTypeSize;
    
    public TlvConstructor(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt1 <= 2) && (paramInt2 > 0) && (paramInt2 <= 2))
      {
        mTypeSize = paramInt1;
        mLengthSize = paramInt2;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid sizes - typeSize=");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", lengthSize=");
      localStringBuilder.append(paramInt2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    private void addHeader(int paramInt1, int paramInt2)
    {
      if (mTypeSize == 1) {
        mArray[mPosition] = ((byte)(byte)paramInt1);
      } else if (mTypeSize == 2) {
        Memory.pokeShort(mArray, mPosition, (short)paramInt1, ByteOrder.BIG_ENDIAN);
      }
      mPosition += mTypeSize;
      if (mLengthSize == 1) {
        mArray[mPosition] = ((byte)(byte)paramInt2);
      } else if (mLengthSize == 2) {
        Memory.pokeShort(mArray, mPosition, (short)paramInt2, ByteOrder.BIG_ENDIAN);
      }
      mPosition += mLengthSize;
    }
    
    private void checkLength(int paramInt)
    {
      if (mPosition + mTypeSize + mLengthSize + paramInt <= mArrayLength) {
        return;
      }
      throw new BufferOverflowException();
    }
    
    private int getActualLength()
    {
      return mPosition;
    }
    
    public TlvConstructor allocate(int paramInt)
    {
      mArray = new byte[paramInt];
      mArrayLength = paramInt;
      return this;
    }
    
    public TlvConstructor allocateAndPut(List<byte[]> paramList)
    {
      if (paramList != null)
      {
        int i = 0;
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          byte[] arrayOfByte = (byte[])localIterator.next();
          int j = i + (mTypeSize + mLengthSize);
          i = j;
          if (arrayOfByte != null) {
            i = j + arrayOfByte.length;
          }
        }
        allocate(i);
        paramList = paramList.iterator();
        while (paramList.hasNext()) {
          putByteArray(0, (byte[])paramList.next());
        }
      }
      return this;
    }
    
    public byte[] getArray()
    {
      return Arrays.copyOf(mArray, getActualLength());
    }
    
    public TlvConstructor putByte(int paramInt, byte paramByte)
    {
      checkLength(1);
      addHeader(paramInt, 1);
      byte[] arrayOfByte = mArray;
      paramInt = mPosition;
      mPosition = (paramInt + 1);
      arrayOfByte[paramInt] = ((byte)paramByte);
      return this;
    }
    
    public TlvConstructor putByteArray(int paramInt, byte[] paramArrayOfByte)
    {
      int i;
      if (paramArrayOfByte == null) {
        i = 0;
      } else {
        i = paramArrayOfByte.length;
      }
      return putByteArray(paramInt, paramArrayOfByte, 0, i);
    }
    
    public TlvConstructor putByteArray(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
    {
      checkLength(paramInt3);
      addHeader(paramInt1, paramInt3);
      if (paramInt3 != 0) {
        System.arraycopy(paramArrayOfByte, paramInt2, mArray, mPosition, paramInt3);
      }
      mPosition += paramInt3;
      return this;
    }
    
    public TlvConstructor putInt(int paramInt1, int paramInt2)
    {
      checkLength(4);
      addHeader(paramInt1, 4);
      Memory.pokeInt(mArray, mPosition, paramInt2, ByteOrder.BIG_ENDIAN);
      mPosition += 4;
      return this;
    }
    
    public TlvConstructor putShort(int paramInt, short paramShort)
    {
      checkLength(2);
      addHeader(paramInt, 2);
      Memory.pokeShort(mArray, mPosition, paramShort, ByteOrder.BIG_ENDIAN);
      mPosition += 2;
      return this;
    }
    
    public TlvConstructor putString(int paramInt, String paramString)
    {
      byte[] arrayOfByte = null;
      int i = 0;
      if (paramString != null)
      {
        arrayOfByte = paramString.getBytes();
        i = arrayOfByte.length;
      }
      return putByteArray(paramInt, arrayOfByte, 0, i);
    }
    
    public TlvConstructor putZeroLengthElement(int paramInt)
    {
      checkLength(0);
      addHeader(paramInt, 0);
      return this;
    }
    
    public TlvConstructor wrap(byte[] paramArrayOfByte)
    {
      mArray = paramArrayOfByte;
      int i;
      if (paramArrayOfByte == null) {
        i = 0;
      } else {
        i = paramArrayOfByte.length;
      }
      mArrayLength = i;
      return this;
    }
  }
  
  public static class TlvElement
  {
    public int length;
    public int offset;
    public byte[] refArray;
    public int type;
    
    private TlvElement(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
    {
      type = paramInt1;
      length = paramInt2;
      refArray = paramArrayOfByte;
      offset = paramInt3;
      if (paramInt3 + paramInt2 <= paramArrayOfByte.length) {
        return;
      }
      throw new BufferOverflowException();
    }
    
    public byte getByte()
    {
      if (length == 1) {
        return refArray[offset];
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Accesing a byte from a TLV element of length ");
      localStringBuilder.append(length);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public int getInt()
    {
      if (length == 4) {
        return Memory.peekInt(refArray, offset, ByteOrder.BIG_ENDIAN);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Accesing an int from a TLV element of length ");
      localStringBuilder.append(length);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public short getShort()
    {
      if (length == 2) {
        return Memory.peekShort(refArray, offset, ByteOrder.BIG_ENDIAN);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Accesing a short from a TLV element of length ");
      localStringBuilder.append(length);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public String getString()
    {
      return new String(refArray, offset, length);
    }
  }
  
  public static class TlvIterable
    implements Iterable<TlvBufferUtils.TlvElement>
  {
    private byte[] mArray;
    private int mArrayLength;
    private int mLengthSize;
    private int mTypeSize;
    
    public TlvIterable(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    {
      if ((paramInt1 >= 0) && (paramInt1 <= 2) && (paramInt2 > 0) && (paramInt2 <= 2))
      {
        mTypeSize = paramInt1;
        mLengthSize = paramInt2;
        mArray = paramArrayOfByte;
        if (paramArrayOfByte == null) {
          paramInt1 = 0;
        } else {
          paramInt1 = paramArrayOfByte.length;
        }
        mArrayLength = paramInt1;
        return;
      }
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Invalid sizes - typeSize=");
      paramArrayOfByte.append(paramInt1);
      paramArrayOfByte.append(", lengthSize=");
      paramArrayOfByte.append(paramInt2);
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    
    public Iterator<TlvBufferUtils.TlvElement> iterator()
    {
      new Iterator()
      {
        private int mOffset = 0;
        
        public boolean hasNext()
        {
          boolean bool;
          if (mOffset < mArrayLength) {
            bool = true;
          } else {
            bool = false;
          }
          return bool;
        }
        
        public TlvBufferUtils.TlvElement next()
        {
          if (hasNext())
          {
            int i = 0;
            if (mTypeSize == 1) {
              i = mArray[mOffset];
            } else if (mTypeSize == 2) {
              i = Memory.peekShort(mArray, mOffset, ByteOrder.BIG_ENDIAN);
            }
            mOffset += mTypeSize;
            int j = 0;
            if (mLengthSize == 1) {
              j = mArray[mOffset];
            } else if (mLengthSize == 2) {
              j = Memory.peekShort(mArray, mOffset, ByteOrder.BIG_ENDIAN);
            }
            mOffset += mLengthSize;
            TlvBufferUtils.TlvElement localTlvElement = new TlvBufferUtils.TlvElement(i, j, mArray, mOffset, null);
            mOffset += j;
            return localTlvElement;
          }
          throw new NoSuchElementException();
        }
        
        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      };
    }
    
    public List<byte[]> toList()
    {
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator = iterator();
      while (localIterator.hasNext())
      {
        TlvBufferUtils.TlvElement localTlvElement = (TlvBufferUtils.TlvElement)localIterator.next();
        localArrayList.add(Arrays.copyOfRange(refArray, offset, offset + length));
      }
      return localArrayList;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("[");
      int i = 1;
      Iterator localIterator = iterator();
      while (localIterator.hasNext())
      {
        TlvBufferUtils.TlvElement localTlvElement = (TlvBufferUtils.TlvElement)localIterator.next();
        if (i == 0) {
          localStringBuilder1.append(",");
        }
        i = 0;
        localStringBuilder1.append(" (");
        if (mTypeSize != 0)
        {
          localStringBuilder2 = new StringBuilder();
          localStringBuilder2.append("T=");
          localStringBuilder2.append(type);
          localStringBuilder2.append(",");
          localStringBuilder1.append(localStringBuilder2.toString());
        }
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("L=");
        localStringBuilder2.append(length);
        localStringBuilder2.append(") ");
        localStringBuilder1.append(localStringBuilder2.toString());
        if (length == 0) {
          localStringBuilder1.append("<null>");
        } else if (length == 1) {
          localStringBuilder1.append(localTlvElement.getByte());
        } else if (length == 2) {
          localStringBuilder1.append(localTlvElement.getShort());
        } else if (length == 4) {
          localStringBuilder1.append(localTlvElement.getInt());
        } else {
          localStringBuilder1.append("<bytes>");
        }
        if (length != 0)
        {
          localStringBuilder2 = new StringBuilder();
          localStringBuilder2.append(" (S='");
          localStringBuilder2.append(localTlvElement.getString());
          localStringBuilder2.append("')");
          localStringBuilder1.append(localStringBuilder2.toString());
        }
      }
      localStringBuilder1.append("]");
      return localStringBuilder1.toString();
    }
  }
}
