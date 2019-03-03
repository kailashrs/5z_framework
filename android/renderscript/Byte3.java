package android.renderscript;

public class Byte3
{
  public byte x;
  public byte y;
  public byte z;
  
  public Byte3() {}
  
  public Byte3(byte paramByte1, byte paramByte2, byte paramByte3)
  {
    x = ((byte)paramByte1);
    y = ((byte)paramByte2);
    z = ((byte)paramByte3);
  }
  
  public Byte3(Byte3 paramByte3)
  {
    x = ((byte)x);
    y = ((byte)y);
    z = ((byte)z);
  }
  
  public static Byte3 add(Byte3 paramByte3, byte paramByte)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x + paramByte));
    y = ((byte)(byte)(y + paramByte));
    z = ((byte)(byte)(z + paramByte));
    return localByte3;
  }
  
  public static Byte3 add(Byte3 paramByte31, Byte3 paramByte32)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x + x));
    y = ((byte)(byte)(y + y));
    z = ((byte)(byte)(z + z));
    return localByte3;
  }
  
  public static Byte3 div(Byte3 paramByte3, byte paramByte)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x / paramByte));
    y = ((byte)(byte)(y / paramByte));
    z = ((byte)(byte)(z / paramByte));
    return localByte3;
  }
  
  public static Byte3 div(Byte3 paramByte31, Byte3 paramByte32)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x / x));
    y = ((byte)(byte)(y / y));
    z = ((byte)(byte)(z / z));
    return localByte3;
  }
  
  public static byte dotProduct(Byte3 paramByte31, Byte3 paramByte32)
  {
    return (byte)((byte)((byte)(x * x) + (byte)(y * y)) + (byte)(z * z));
  }
  
  public static Byte3 mul(Byte3 paramByte3, byte paramByte)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x * paramByte));
    y = ((byte)(byte)(y * paramByte));
    z = ((byte)(byte)(z * paramByte));
    return localByte3;
  }
  
  public static Byte3 mul(Byte3 paramByte31, Byte3 paramByte32)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x * x));
    y = ((byte)(byte)(y * y));
    z = ((byte)(byte)(z * z));
    return localByte3;
  }
  
  public static Byte3 sub(Byte3 paramByte3, byte paramByte)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x - paramByte));
    y = ((byte)(byte)(y - paramByte));
    z = ((byte)(byte)(z - paramByte));
    return localByte3;
  }
  
  public static Byte3 sub(Byte3 paramByte31, Byte3 paramByte32)
  {
    Byte3 localByte3 = new Byte3();
    x = ((byte)(byte)(x - x));
    y = ((byte)(byte)(y - y));
    z = ((byte)(byte)(z - z));
    return localByte3;
  }
  
  public void add(byte paramByte)
  {
    x = ((byte)(byte)(x + paramByte));
    y = ((byte)(byte)(y + paramByte));
    z = ((byte)(byte)(z + paramByte));
  }
  
  public void add(Byte3 paramByte3)
  {
    x = ((byte)(byte)(x + x));
    y = ((byte)(byte)(y + y));
    z = ((byte)(byte)(z + z));
  }
  
  public void addAt(int paramInt, byte paramByte)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = ((byte)(byte)(z + paramByte));
      return;
    case 1: 
      y = ((byte)(byte)(y + paramByte));
      return;
    }
    x = ((byte)(byte)(x + paramByte));
  }
  
  public void addMultiple(Byte3 paramByte3, byte paramByte)
  {
    x = ((byte)(byte)(x + x * paramByte));
    y = ((byte)(byte)(y + y * paramByte));
    z = ((byte)(byte)(z + z * paramByte));
  }
  
  public void copyTo(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)x);
    paramArrayOfByte[(paramInt + 1)] = ((byte)y);
    paramArrayOfByte[(paramInt + 2)] = ((byte)z);
  }
  
  public void div(byte paramByte)
  {
    x = ((byte)(byte)(x / paramByte));
    y = ((byte)(byte)(y / paramByte));
    z = ((byte)(byte)(z / paramByte));
  }
  
  public void div(Byte3 paramByte3)
  {
    x = ((byte)(byte)(x / x));
    y = ((byte)(byte)(y / y));
    z = ((byte)(byte)(z / z));
  }
  
  public byte dotProduct(Byte3 paramByte3)
  {
    return (byte)((byte)((byte)(x * x) + (byte)(y * y)) + (byte)(z * z));
  }
  
  public byte elementSum()
  {
    return (byte)(x + y + z);
  }
  
  public byte get(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      return z;
    case 1: 
      return y;
    }
    return x;
  }
  
  public byte length()
  {
    return 3;
  }
  
  public void mul(byte paramByte)
  {
    x = ((byte)(byte)(x * paramByte));
    y = ((byte)(byte)(y * paramByte));
    z = ((byte)(byte)(z * paramByte));
  }
  
  public void mul(Byte3 paramByte3)
  {
    x = ((byte)(byte)(x * x));
    y = ((byte)(byte)(y * y));
    z = ((byte)(byte)(z * z));
  }
  
  public void negate()
  {
    x = ((byte)(byte)-x);
    y = ((byte)(byte)-y);
    z = ((byte)(byte)-z);
  }
  
  public void set(Byte3 paramByte3)
  {
    x = ((byte)x);
    y = ((byte)y);
    z = ((byte)z);
  }
  
  public void setAt(int paramInt, byte paramByte)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = ((byte)paramByte);
      return;
    case 1: 
      y = ((byte)paramByte);
      return;
    }
    x = ((byte)paramByte);
  }
  
  public void setValues(byte paramByte1, byte paramByte2, byte paramByte3)
  {
    x = ((byte)paramByte1);
    y = ((byte)paramByte2);
    z = ((byte)paramByte3);
  }
  
  public void sub(byte paramByte)
  {
    x = ((byte)(byte)(x - paramByte));
    y = ((byte)(byte)(y - paramByte));
    z = ((byte)(byte)(z - paramByte));
  }
  
  public void sub(Byte3 paramByte3)
  {
    x = ((byte)(byte)(x - x));
    y = ((byte)(byte)(y - y));
    z = ((byte)(byte)(z - z));
  }
}
