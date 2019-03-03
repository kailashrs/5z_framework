package android.renderscript;

public class Long3
{
  public long x;
  public long y;
  public long z;
  
  public Long3() {}
  
  public Long3(long paramLong)
  {
    z = paramLong;
    y = paramLong;
    x = paramLong;
  }
  
  public Long3(long paramLong1, long paramLong2, long paramLong3)
  {
    x = paramLong1;
    y = paramLong2;
    z = paramLong3;
  }
  
  public Long3(Long3 paramLong3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public static Long3 add(Long3 paramLong3, long paramLong)
  {
    Long3 localLong3 = new Long3();
    x += paramLong;
    y += paramLong;
    z += paramLong;
    return localLong3;
  }
  
  public static Long3 add(Long3 paramLong31, Long3 paramLong32)
  {
    Long3 localLong3 = new Long3();
    x += x;
    y += y;
    z += z;
    return localLong3;
  }
  
  public static Long3 div(Long3 paramLong3, long paramLong)
  {
    Long3 localLong3 = new Long3();
    x /= paramLong;
    y /= paramLong;
    z /= paramLong;
    return localLong3;
  }
  
  public static Long3 div(Long3 paramLong31, Long3 paramLong32)
  {
    Long3 localLong3 = new Long3();
    x /= x;
    y /= y;
    z /= z;
    return localLong3;
  }
  
  public static long dotProduct(Long3 paramLong31, Long3 paramLong32)
  {
    return x * x + y * y + z * z;
  }
  
  public static Long3 mod(Long3 paramLong3, long paramLong)
  {
    Long3 localLong3 = new Long3();
    x %= paramLong;
    y %= paramLong;
    z %= paramLong;
    return localLong3;
  }
  
  public static Long3 mod(Long3 paramLong31, Long3 paramLong32)
  {
    Long3 localLong3 = new Long3();
    x %= x;
    y %= y;
    z %= z;
    return localLong3;
  }
  
  public static Long3 mul(Long3 paramLong3, long paramLong)
  {
    Long3 localLong3 = new Long3();
    x *= paramLong;
    y *= paramLong;
    z *= paramLong;
    return localLong3;
  }
  
  public static Long3 mul(Long3 paramLong31, Long3 paramLong32)
  {
    Long3 localLong3 = new Long3();
    x *= x;
    y *= y;
    z *= z;
    return localLong3;
  }
  
  public static Long3 sub(Long3 paramLong3, long paramLong)
  {
    Long3 localLong3 = new Long3();
    x -= paramLong;
    y -= paramLong;
    z -= paramLong;
    return localLong3;
  }
  
  public static Long3 sub(Long3 paramLong31, Long3 paramLong32)
  {
    Long3 localLong3 = new Long3();
    x -= x;
    y -= y;
    z -= z;
    return localLong3;
  }
  
  public void add(long paramLong)
  {
    x += paramLong;
    y += paramLong;
    z += paramLong;
  }
  
  public void add(Long3 paramLong3)
  {
    x += x;
    y += y;
    z += z;
  }
  
  public void addAt(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z += paramLong;
      return;
    case 1: 
      y += paramLong;
      return;
    }
    x += paramLong;
  }
  
  public void addMultiple(Long3 paramLong3, long paramLong)
  {
    x += x * paramLong;
    y += y * paramLong;
    z += z * paramLong;
  }
  
  public void copyTo(long[] paramArrayOfLong, int paramInt)
  {
    paramArrayOfLong[paramInt] = x;
    paramArrayOfLong[(paramInt + 1)] = y;
    paramArrayOfLong[(paramInt + 2)] = z;
  }
  
  public void div(long paramLong)
  {
    x /= paramLong;
    y /= paramLong;
    z /= paramLong;
  }
  
  public void div(Long3 paramLong3)
  {
    x /= x;
    y /= y;
    z /= z;
  }
  
  public long dotProduct(Long3 paramLong3)
  {
    return x * x + y * y + z * z;
  }
  
  public long elementSum()
  {
    return x + y + z;
  }
  
  public long get(int paramInt)
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
  
  public long length()
  {
    return 3L;
  }
  
  public void mod(long paramLong)
  {
    x %= paramLong;
    y %= paramLong;
    z %= paramLong;
  }
  
  public void mod(Long3 paramLong3)
  {
    x %= x;
    y %= y;
    z %= z;
  }
  
  public void mul(long paramLong)
  {
    x *= paramLong;
    y *= paramLong;
    z *= paramLong;
  }
  
  public void mul(Long3 paramLong3)
  {
    x *= x;
    y *= y;
    z *= z;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
    z = (-z);
  }
  
  public void set(Long3 paramLong3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public void setAt(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = paramLong;
      return;
    case 1: 
      y = paramLong;
      return;
    }
    x = paramLong;
  }
  
  public void setValues(long paramLong1, long paramLong2, long paramLong3)
  {
    x = paramLong1;
    y = paramLong2;
    z = paramLong3;
  }
  
  public void sub(long paramLong)
  {
    x -= paramLong;
    y -= paramLong;
    z -= paramLong;
  }
  
  public void sub(Long3 paramLong3)
  {
    x -= x;
    y -= y;
    z -= z;
  }
}
