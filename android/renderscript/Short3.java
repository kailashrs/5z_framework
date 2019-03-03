package android.renderscript;

public class Short3
{
  public short x;
  public short y;
  public short z;
  
  public Short3() {}
  
  public Short3(Short3 paramShort3)
  {
    x = ((short)x);
    y = ((short)y);
    z = ((short)z);
  }
  
  public Short3(short paramShort)
  {
    z = ((short)paramShort);
    y = ((short)paramShort);
    x = ((short)paramShort);
  }
  
  public Short3(short paramShort1, short paramShort2, short paramShort3)
  {
    x = ((short)paramShort1);
    y = ((short)paramShort2);
    z = ((short)paramShort3);
  }
  
  public static Short3 add(Short3 paramShort31, Short3 paramShort32)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x + x));
    y = ((short)(short)(y + y));
    z = ((short)(short)(z + z));
    return localShort3;
  }
  
  public static Short3 add(Short3 paramShort3, short paramShort)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x + paramShort));
    y = ((short)(short)(y + paramShort));
    z = ((short)(short)(z + paramShort));
    return localShort3;
  }
  
  public static Short3 div(Short3 paramShort31, Short3 paramShort32)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x / x));
    y = ((short)(short)(y / y));
    z = ((short)(short)(z / z));
    return localShort3;
  }
  
  public static Short3 div(Short3 paramShort3, short paramShort)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x / paramShort));
    y = ((short)(short)(y / paramShort));
    z = ((short)(short)(z / paramShort));
    return localShort3;
  }
  
  public static short dotProduct(Short3 paramShort31, Short3 paramShort32)
  {
    return (short)(x * x + y * y + z * z);
  }
  
  public static Short3 mod(Short3 paramShort31, Short3 paramShort32)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x % x));
    y = ((short)(short)(y % y));
    z = ((short)(short)(z % z));
    return localShort3;
  }
  
  public static Short3 mod(Short3 paramShort3, short paramShort)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x % paramShort));
    y = ((short)(short)(y % paramShort));
    z = ((short)(short)(z % paramShort));
    return localShort3;
  }
  
  public static Short3 mul(Short3 paramShort31, Short3 paramShort32)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x * x));
    y = ((short)(short)(y * y));
    z = ((short)(short)(z * z));
    return localShort3;
  }
  
  public static Short3 mul(Short3 paramShort3, short paramShort)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x * paramShort));
    y = ((short)(short)(y * paramShort));
    z = ((short)(short)(z * paramShort));
    return localShort3;
  }
  
  public static Short3 sub(Short3 paramShort31, Short3 paramShort32)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x - x));
    y = ((short)(short)(y - y));
    z = ((short)(short)(z - z));
    return localShort3;
  }
  
  public static Short3 sub(Short3 paramShort3, short paramShort)
  {
    Short3 localShort3 = new Short3();
    x = ((short)(short)(x - paramShort));
    y = ((short)(short)(y - paramShort));
    z = ((short)(short)(z - paramShort));
    return localShort3;
  }
  
  public void add(Short3 paramShort3)
  {
    x = ((short)(short)(x + x));
    y = ((short)(short)(y + y));
    z = ((short)(short)(z + z));
  }
  
  public void add(short paramShort)
  {
    x = ((short)(short)(x + paramShort));
    y = ((short)(short)(y + paramShort));
    z = ((short)(short)(z + paramShort));
  }
  
  public void addAt(int paramInt, short paramShort)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = ((short)(short)(z + paramShort));
      return;
    case 1: 
      y = ((short)(short)(y + paramShort));
      return;
    }
    x = ((short)(short)(x + paramShort));
  }
  
  public void addMultiple(Short3 paramShort3, short paramShort)
  {
    x = ((short)(short)(x + x * paramShort));
    y = ((short)(short)(y + y * paramShort));
    z = ((short)(short)(z + z * paramShort));
  }
  
  public void copyTo(short[] paramArrayOfShort, int paramInt)
  {
    paramArrayOfShort[paramInt] = ((short)x);
    paramArrayOfShort[(paramInt + 1)] = ((short)y);
    paramArrayOfShort[(paramInt + 2)] = ((short)z);
  }
  
  public void div(Short3 paramShort3)
  {
    x = ((short)(short)(x / x));
    y = ((short)(short)(y / y));
    z = ((short)(short)(z / z));
  }
  
  public void div(short paramShort)
  {
    x = ((short)(short)(x / paramShort));
    y = ((short)(short)(y / paramShort));
    z = ((short)(short)(z / paramShort));
  }
  
  public short dotProduct(Short3 paramShort3)
  {
    return (short)(x * x + y * y + z * z);
  }
  
  public short elementSum()
  {
    return (short)(x + y + z);
  }
  
  public short get(int paramInt)
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
  
  public short length()
  {
    return 3;
  }
  
  public void mod(Short3 paramShort3)
  {
    x = ((short)(short)(x % x));
    y = ((short)(short)(y % y));
    z = ((short)(short)(z % z));
  }
  
  public void mod(short paramShort)
  {
    x = ((short)(short)(x % paramShort));
    y = ((short)(short)(y % paramShort));
    z = ((short)(short)(z % paramShort));
  }
  
  public void mul(Short3 paramShort3)
  {
    x = ((short)(short)(x * x));
    y = ((short)(short)(y * y));
    z = ((short)(short)(z * z));
  }
  
  public void mul(short paramShort)
  {
    x = ((short)(short)(x * paramShort));
    y = ((short)(short)(y * paramShort));
    z = ((short)(short)(z * paramShort));
  }
  
  public void negate()
  {
    x = ((short)(short)-x);
    y = ((short)(short)-y);
    z = ((short)(short)-z);
  }
  
  public void set(Short3 paramShort3)
  {
    x = ((short)x);
    y = ((short)y);
    z = ((short)z);
  }
  
  public void setAt(int paramInt, short paramShort)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = ((short)paramShort);
      return;
    case 1: 
      y = ((short)paramShort);
      return;
    }
    x = ((short)paramShort);
  }
  
  public void setValues(short paramShort1, short paramShort2, short paramShort3)
  {
    x = ((short)paramShort1);
    y = ((short)paramShort2);
    z = ((short)paramShort3);
  }
  
  public void sub(Short3 paramShort3)
  {
    x = ((short)(short)(x - x));
    y = ((short)(short)(y - y));
    z = ((short)(short)(z - z));
  }
  
  public void sub(short paramShort)
  {
    x = ((short)(short)(x - paramShort));
    y = ((short)(short)(y - paramShort));
    z = ((short)(short)(z - paramShort));
  }
}
