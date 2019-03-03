package android.renderscript;

public class Byte4
{
  public byte w;
  public byte x;
  public byte y;
  public byte z;
  
  public Byte4() {}
  
  public Byte4(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
  {
    x = ((byte)paramByte1);
    y = ((byte)paramByte2);
    z = ((byte)paramByte3);
    w = ((byte)paramByte4);
  }
  
  public Byte4(Byte4 paramByte4)
  {
    x = ((byte)x);
    y = ((byte)y);
    z = ((byte)z);
    w = ((byte)w);
  }
  
  public static Byte4 add(Byte4 paramByte4, byte paramByte)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x + paramByte));
    y = ((byte)(byte)(y + paramByte));
    z = ((byte)(byte)(z + paramByte));
    w = ((byte)(byte)(w + paramByte));
    return localByte4;
  }
  
  public static Byte4 add(Byte4 paramByte41, Byte4 paramByte42)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x + x));
    y = ((byte)(byte)(y + y));
    z = ((byte)(byte)(z + z));
    w = ((byte)(byte)(w + w));
    return localByte4;
  }
  
  public static Byte4 div(Byte4 paramByte4, byte paramByte)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x / paramByte));
    y = ((byte)(byte)(y / paramByte));
    z = ((byte)(byte)(z / paramByte));
    w = ((byte)(byte)(w / paramByte));
    return localByte4;
  }
  
  public static Byte4 div(Byte4 paramByte41, Byte4 paramByte42)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x / x));
    y = ((byte)(byte)(y / y));
    z = ((byte)(byte)(z / z));
    w = ((byte)(byte)(w / w));
    return localByte4;
  }
  
  public static byte dotProduct(Byte4 paramByte41, Byte4 paramByte42)
  {
    return (byte)(x * x + y * y + z * z + w * w);
  }
  
  public static Byte4 mul(Byte4 paramByte4, byte paramByte)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x * paramByte));
    y = ((byte)(byte)(y * paramByte));
    z = ((byte)(byte)(z * paramByte));
    w = ((byte)(byte)(w * paramByte));
    return localByte4;
  }
  
  public static Byte4 mul(Byte4 paramByte41, Byte4 paramByte42)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x * x));
    y = ((byte)(byte)(y * y));
    z = ((byte)(byte)(z * z));
    w = ((byte)(byte)(w * w));
    return localByte4;
  }
  
  public static Byte4 sub(Byte4 paramByte4, byte paramByte)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x - paramByte));
    y = ((byte)(byte)(y - paramByte));
    z = ((byte)(byte)(z - paramByte));
    w = ((byte)(byte)(w - paramByte));
    return localByte4;
  }
  
  public static Byte4 sub(Byte4 paramByte41, Byte4 paramByte42)
  {
    Byte4 localByte4 = new Byte4();
    x = ((byte)(byte)(x - x));
    y = ((byte)(byte)(y - y));
    z = ((byte)(byte)(z - z));
    w = ((byte)(byte)(w - w));
    return localByte4;
  }
  
  public void add(byte paramByte)
  {
    x = ((byte)(byte)(x + paramByte));
    y = ((byte)(byte)(y + paramByte));
    z = ((byte)(byte)(z + paramByte));
    w = ((byte)(byte)(w + paramByte));
  }
  
  public void add(Byte4 paramByte4)
  {
    x = ((byte)(byte)(x + x));
    y = ((byte)(byte)(y + y));
    z = ((byte)(byte)(z + z));
    w = ((byte)(byte)(w + w));
  }
  
  public void addAt(int paramInt, byte paramByte)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = ((byte)(byte)(w + paramByte));
      return;
    case 2: 
      z = ((byte)(byte)(z + paramByte));
      return;
    case 1: 
      y = ((byte)(byte)(y + paramByte));
      return;
    }
    x = ((byte)(byte)(x + paramByte));
  }
  
  public void addMultiple(Byte4 paramByte4, byte paramByte)
  {
    x = ((byte)(byte)(x + x * paramByte));
    y = ((byte)(byte)(y + y * paramByte));
    z = ((byte)(byte)(z + z * paramByte));
    w = ((byte)(byte)(w + w * paramByte));
  }
  
  public void copyTo(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)x);
    paramArrayOfByte[(paramInt + 1)] = ((byte)y);
    paramArrayOfByte[(paramInt + 2)] = ((byte)z);
    paramArrayOfByte[(paramInt + 3)] = ((byte)w);
  }
  
  public void div(byte paramByte)
  {
    x = ((byte)(byte)(x / paramByte));
    y = ((byte)(byte)(y / paramByte));
    z = ((byte)(byte)(z / paramByte));
    w = ((byte)(byte)(w / paramByte));
  }
  
  public void div(Byte4 paramByte4)
  {
    x = ((byte)(byte)(x / x));
    y = ((byte)(byte)(y / y));
    z = ((byte)(byte)(z / z));
    w = ((byte)(byte)(w / w));
  }
  
  public byte dotProduct(Byte4 paramByte4)
  {
    return (byte)(x * x + y * y + z * z + w * w);
  }
  
  public byte elementSum()
  {
    return (byte)(x + y + z + w);
  }
  
  public byte get(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      return w;
    case 2: 
      return z;
    case 1: 
      return y;
    }
    return x;
  }
  
  public byte length()
  {
    return 4;
  }
  
  public void mul(byte paramByte)
  {
    x = ((byte)(byte)(x * paramByte));
    y = ((byte)(byte)(y * paramByte));
    z = ((byte)(byte)(z * paramByte));
    w = ((byte)(byte)(w * paramByte));
  }
  
  public void mul(Byte4 paramByte4)
  {
    x = ((byte)(byte)(x * x));
    y = ((byte)(byte)(y * y));
    z = ((byte)(byte)(z * z));
    w = ((byte)(byte)(w * w));
  }
  
  public void negate()
  {
    x = ((byte)(byte)-x);
    y = ((byte)(byte)-y);
    z = ((byte)(byte)-z);
    w = ((byte)(byte)-w);
  }
  
  public void set(Byte4 paramByte4)
  {
    x = ((byte)x);
    y = ((byte)y);
    z = ((byte)z);
    w = ((byte)w);
  }
  
  public void setAt(int paramInt, byte paramByte)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = ((byte)paramByte);
      return;
    case 2: 
      z = ((byte)paramByte);
      return;
    case 1: 
      y = ((byte)paramByte);
      return;
    }
    x = ((byte)paramByte);
  }
  
  public void setValues(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
  {
    x = ((byte)paramByte1);
    y = ((byte)paramByte2);
    z = ((byte)paramByte3);
    w = ((byte)paramByte4);
  }
  
  public void sub(byte paramByte)
  {
    x = ((byte)(byte)(x - paramByte));
    y = ((byte)(byte)(y - paramByte));
    z = ((byte)(byte)(z - paramByte));
    w = ((byte)(byte)(w - paramByte));
  }
  
  public void sub(Byte4 paramByte4)
  {
    x = ((byte)(byte)(x - x));
    y = ((byte)(byte)(y - y));
    z = ((byte)(byte)(z - z));
    w = ((byte)(byte)(w - w));
  }
}
