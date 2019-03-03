package android.renderscript;

public class Short4
{
  public short w;
  public short x;
  public short y;
  public short z;
  
  public Short4() {}
  
  public Short4(Short4 paramShort4)
  {
    x = ((short)x);
    y = ((short)y);
    z = ((short)z);
    w = ((short)w);
  }
  
  public Short4(short paramShort)
  {
    w = ((short)paramShort);
    z = ((short)paramShort);
    y = ((short)paramShort);
    x = ((short)paramShort);
  }
  
  public Short4(short paramShort1, short paramShort2, short paramShort3, short paramShort4)
  {
    x = ((short)paramShort1);
    y = ((short)paramShort2);
    z = ((short)paramShort3);
    w = ((short)paramShort4);
  }
  
  public static Short4 add(Short4 paramShort41, Short4 paramShort42)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x + x));
    y = ((short)(short)(y + y));
    z = ((short)(short)(z + z));
    w = ((short)(short)(w + w));
    return localShort4;
  }
  
  public static Short4 add(Short4 paramShort4, short paramShort)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x + paramShort));
    y = ((short)(short)(y + paramShort));
    z = ((short)(short)(z + paramShort));
    w = ((short)(short)(w + paramShort));
    return localShort4;
  }
  
  public static Short4 div(Short4 paramShort41, Short4 paramShort42)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x / x));
    y = ((short)(short)(y / y));
    z = ((short)(short)(z / z));
    w = ((short)(short)(w / w));
    return localShort4;
  }
  
  public static Short4 div(Short4 paramShort4, short paramShort)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x / paramShort));
    y = ((short)(short)(y / paramShort));
    z = ((short)(short)(z / paramShort));
    w = ((short)(short)(w / paramShort));
    return localShort4;
  }
  
  public static short dotProduct(Short4 paramShort41, Short4 paramShort42)
  {
    return (short)(x * x + y * y + z * z + w * w);
  }
  
  public static Short4 mod(Short4 paramShort41, Short4 paramShort42)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x % x));
    y = ((short)(short)(y % y));
    z = ((short)(short)(z % z));
    w = ((short)(short)(w % w));
    return localShort4;
  }
  
  public static Short4 mod(Short4 paramShort4, short paramShort)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x % paramShort));
    y = ((short)(short)(y % paramShort));
    z = ((short)(short)(z % paramShort));
    w = ((short)(short)(w % paramShort));
    return localShort4;
  }
  
  public static Short4 mul(Short4 paramShort41, Short4 paramShort42)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x * x));
    y = ((short)(short)(y * y));
    z = ((short)(short)(z * z));
    w = ((short)(short)(w * w));
    return localShort4;
  }
  
  public static Short4 mul(Short4 paramShort4, short paramShort)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x * paramShort));
    y = ((short)(short)(y * paramShort));
    z = ((short)(short)(z * paramShort));
    w = ((short)(short)(w * paramShort));
    return localShort4;
  }
  
  public static Short4 sub(Short4 paramShort41, Short4 paramShort42)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x - x));
    y = ((short)(short)(y - y));
    z = ((short)(short)(z - z));
    w = ((short)(short)(w - w));
    return localShort4;
  }
  
  public static Short4 sub(Short4 paramShort4, short paramShort)
  {
    Short4 localShort4 = new Short4();
    x = ((short)(short)(x - paramShort));
    y = ((short)(short)(y - paramShort));
    z = ((short)(short)(z - paramShort));
    w = ((short)(short)(w - paramShort));
    return localShort4;
  }
  
  public void add(Short4 paramShort4)
  {
    x = ((short)(short)(x + x));
    y = ((short)(short)(y + y));
    z = ((short)(short)(z + z));
    w = ((short)(short)(w + w));
  }
  
  public void add(short paramShort)
  {
    x = ((short)(short)(x + paramShort));
    y = ((short)(short)(y + paramShort));
    z = ((short)(short)(z + paramShort));
    w = ((short)(short)(w + paramShort));
  }
  
  public void addAt(int paramInt, short paramShort)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = ((short)(short)(w + paramShort));
      return;
    case 2: 
      z = ((short)(short)(z + paramShort));
      return;
    case 1: 
      y = ((short)(short)(y + paramShort));
      return;
    }
    x = ((short)(short)(x + paramShort));
  }
  
  public void addMultiple(Short4 paramShort4, short paramShort)
  {
    x = ((short)(short)(x + x * paramShort));
    y = ((short)(short)(y + y * paramShort));
    z = ((short)(short)(z + z * paramShort));
    w = ((short)(short)(w + w * paramShort));
  }
  
  public void copyTo(short[] paramArrayOfShort, int paramInt)
  {
    paramArrayOfShort[paramInt] = ((short)x);
    paramArrayOfShort[(paramInt + 1)] = ((short)y);
    paramArrayOfShort[(paramInt + 2)] = ((short)z);
    paramArrayOfShort[(paramInt + 3)] = ((short)w);
  }
  
  public void div(Short4 paramShort4)
  {
    x = ((short)(short)(x / x));
    y = ((short)(short)(y / y));
    z = ((short)(short)(z / z));
    w = ((short)(short)(w / w));
  }
  
  public void div(short paramShort)
  {
    x = ((short)(short)(x / paramShort));
    y = ((short)(short)(y / paramShort));
    z = ((short)(short)(z / paramShort));
    w = ((short)(short)(w / paramShort));
  }
  
  public short dotProduct(Short4 paramShort4)
  {
    return (short)(x * x + y * y + z * z + w * w);
  }
  
  public short elementSum()
  {
    return (short)(x + y + z + w);
  }
  
  public short get(int paramInt)
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
  
  public short length()
  {
    return 4;
  }
  
  public void mod(Short4 paramShort4)
  {
    x = ((short)(short)(x % x));
    y = ((short)(short)(y % y));
    z = ((short)(short)(z % z));
    w = ((short)(short)(w % w));
  }
  
  public void mod(short paramShort)
  {
    x = ((short)(short)(x % paramShort));
    y = ((short)(short)(y % paramShort));
    z = ((short)(short)(z % paramShort));
    w = ((short)(short)(w % paramShort));
  }
  
  public void mul(Short4 paramShort4)
  {
    x = ((short)(short)(x * x));
    y = ((short)(short)(y * y));
    z = ((short)(short)(z * z));
    w = ((short)(short)(w * w));
  }
  
  public void mul(short paramShort)
  {
    x = ((short)(short)(x * paramShort));
    y = ((short)(short)(y * paramShort));
    z = ((short)(short)(z * paramShort));
    w = ((short)(short)(w * paramShort));
  }
  
  public void negate()
  {
    x = ((short)(short)-x);
    y = ((short)(short)-y);
    z = ((short)(short)-z);
    w = ((short)(short)-w);
  }
  
  public void set(Short4 paramShort4)
  {
    x = ((short)x);
    y = ((short)y);
    z = ((short)z);
    w = ((short)w);
  }
  
  public void setAt(int paramInt, short paramShort)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = ((short)paramShort);
      return;
    case 2: 
      z = ((short)paramShort);
      return;
    case 1: 
      y = ((short)paramShort);
      return;
    }
    x = ((short)paramShort);
  }
  
  public void setValues(short paramShort1, short paramShort2, short paramShort3, short paramShort4)
  {
    x = ((short)paramShort1);
    y = ((short)paramShort2);
    z = ((short)paramShort3);
    w = ((short)paramShort4);
  }
  
  public void sub(Short4 paramShort4)
  {
    x = ((short)(short)(x - x));
    y = ((short)(short)(y - y));
    z = ((short)(short)(z - z));
    w = ((short)(short)(w - w));
  }
  
  public void sub(short paramShort)
  {
    x = ((short)(short)(x - paramShort));
    y = ((short)(short)(y - paramShort));
    z = ((short)(short)(z - paramShort));
    w = ((short)(short)(w - paramShort));
  }
}
