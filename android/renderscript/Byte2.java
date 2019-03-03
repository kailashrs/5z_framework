package android.renderscript;

public class Byte2
{
  public byte x;
  public byte y;
  
  public Byte2() {}
  
  public Byte2(byte paramByte1, byte paramByte2)
  {
    x = ((byte)paramByte1);
    y = ((byte)paramByte2);
  }
  
  public Byte2(Byte2 paramByte2)
  {
    x = ((byte)x);
    y = ((byte)y);
  }
  
  public static Byte2 add(Byte2 paramByte2, byte paramByte)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x + paramByte));
    y = ((byte)(byte)(y + paramByte));
    return localByte2;
  }
  
  public static Byte2 add(Byte2 paramByte21, Byte2 paramByte22)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x + x));
    y = ((byte)(byte)(y + y));
    return localByte2;
  }
  
  public static Byte2 div(Byte2 paramByte2, byte paramByte)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x / paramByte));
    y = ((byte)(byte)(y / paramByte));
    return localByte2;
  }
  
  public static Byte2 div(Byte2 paramByte21, Byte2 paramByte22)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x / x));
    y = ((byte)(byte)(y / y));
    return localByte2;
  }
  
  public static byte dotProduct(Byte2 paramByte21, Byte2 paramByte22)
  {
    return (byte)(x * x + y * y);
  }
  
  public static Byte2 mul(Byte2 paramByte2, byte paramByte)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x * paramByte));
    y = ((byte)(byte)(y * paramByte));
    return localByte2;
  }
  
  public static Byte2 mul(Byte2 paramByte21, Byte2 paramByte22)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x * x));
    y = ((byte)(byte)(y * y));
    return localByte2;
  }
  
  public static Byte2 sub(Byte2 paramByte2, byte paramByte)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x - paramByte));
    y = ((byte)(byte)(y - paramByte));
    return localByte2;
  }
  
  public static Byte2 sub(Byte2 paramByte21, Byte2 paramByte22)
  {
    Byte2 localByte2 = new Byte2();
    x = ((byte)(byte)(x - x));
    y = ((byte)(byte)(y - y));
    return localByte2;
  }
  
  public void add(byte paramByte)
  {
    x = ((byte)(byte)(x + paramByte));
    y = ((byte)(byte)(y + paramByte));
  }
  
  public void add(Byte2 paramByte2)
  {
    x = ((byte)(byte)(x + x));
    y = ((byte)(byte)(y + y));
  }
  
  public void addAt(int paramInt, byte paramByte)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = ((byte)(byte)(y + paramByte));
      return;
    }
    x = ((byte)(byte)(x + paramByte));
  }
  
  public void addMultiple(Byte2 paramByte2, byte paramByte)
  {
    x = ((byte)(byte)(x + x * paramByte));
    y = ((byte)(byte)(y + y * paramByte));
  }
  
  public void copyTo(byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)x);
    paramArrayOfByte[(paramInt + 1)] = ((byte)y);
  }
  
  public void div(byte paramByte)
  {
    x = ((byte)(byte)(x / paramByte));
    y = ((byte)(byte)(y / paramByte));
  }
  
  public void div(Byte2 paramByte2)
  {
    x = ((byte)(byte)(x / x));
    y = ((byte)(byte)(y / y));
  }
  
  public byte dotProduct(Byte2 paramByte2)
  {
    return (byte)(x * x + y * y);
  }
  
  public byte elementSum()
  {
    return (byte)(x + y);
  }
  
  public byte get(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      return y;
    }
    return x;
  }
  
  public byte length()
  {
    return 2;
  }
  
  public void mul(byte paramByte)
  {
    x = ((byte)(byte)(x * paramByte));
    y = ((byte)(byte)(y * paramByte));
  }
  
  public void mul(Byte2 paramByte2)
  {
    x = ((byte)(byte)(x * x));
    y = ((byte)(byte)(y * y));
  }
  
  public void negate()
  {
    x = ((byte)(byte)-x);
    y = ((byte)(byte)-y);
  }
  
  public void set(Byte2 paramByte2)
  {
    x = ((byte)x);
    y = ((byte)y);
  }
  
  public void setAt(int paramInt, byte paramByte)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = ((byte)paramByte);
      return;
    }
    x = ((byte)paramByte);
  }
  
  public void setValues(byte paramByte1, byte paramByte2)
  {
    x = ((byte)paramByte1);
    y = ((byte)paramByte2);
  }
  
  public void sub(byte paramByte)
  {
    x = ((byte)(byte)(x - paramByte));
    y = ((byte)(byte)(y - paramByte));
  }
  
  public void sub(Byte2 paramByte2)
  {
    x = ((byte)(byte)(x - x));
    y = ((byte)(byte)(y - y));
  }
}
