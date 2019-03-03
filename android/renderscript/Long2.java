package android.renderscript;

public class Long2
{
  public long x;
  public long y;
  
  public Long2() {}
  
  public Long2(long paramLong)
  {
    y = paramLong;
    x = paramLong;
  }
  
  public Long2(long paramLong1, long paramLong2)
  {
    x = paramLong1;
    y = paramLong2;
  }
  
  public Long2(Long2 paramLong2)
  {
    x = x;
    y = y;
  }
  
  public static Long2 add(Long2 paramLong2, long paramLong)
  {
    Long2 localLong2 = new Long2();
    x += paramLong;
    y += paramLong;
    return localLong2;
  }
  
  public static Long2 add(Long2 paramLong21, Long2 paramLong22)
  {
    Long2 localLong2 = new Long2();
    x += x;
    y += y;
    return localLong2;
  }
  
  public static Long2 div(Long2 paramLong2, long paramLong)
  {
    Long2 localLong2 = new Long2();
    x /= paramLong;
    y /= paramLong;
    return localLong2;
  }
  
  public static Long2 div(Long2 paramLong21, Long2 paramLong22)
  {
    Long2 localLong2 = new Long2();
    x /= x;
    y /= y;
    return localLong2;
  }
  
  public static long dotProduct(Long2 paramLong21, Long2 paramLong22)
  {
    return x * x + y * y;
  }
  
  public static Long2 mod(Long2 paramLong2, long paramLong)
  {
    Long2 localLong2 = new Long2();
    x %= paramLong;
    y %= paramLong;
    return localLong2;
  }
  
  public static Long2 mod(Long2 paramLong21, Long2 paramLong22)
  {
    Long2 localLong2 = new Long2();
    x %= x;
    y %= y;
    return localLong2;
  }
  
  public static Long2 mul(Long2 paramLong2, long paramLong)
  {
    Long2 localLong2 = new Long2();
    x *= paramLong;
    y *= paramLong;
    return localLong2;
  }
  
  public static Long2 mul(Long2 paramLong21, Long2 paramLong22)
  {
    Long2 localLong2 = new Long2();
    x *= x;
    y *= y;
    return localLong2;
  }
  
  public static Long2 sub(Long2 paramLong2, long paramLong)
  {
    Long2 localLong2 = new Long2();
    x -= paramLong;
    y -= paramLong;
    return localLong2;
  }
  
  public static Long2 sub(Long2 paramLong21, Long2 paramLong22)
  {
    Long2 localLong2 = new Long2();
    x -= x;
    y -= y;
    return localLong2;
  }
  
  public void add(long paramLong)
  {
    x += paramLong;
    y += paramLong;
  }
  
  public void add(Long2 paramLong2)
  {
    x += x;
    y += y;
  }
  
  public void addAt(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y += paramLong;
      return;
    }
    x += paramLong;
  }
  
  public void addMultiple(Long2 paramLong2, long paramLong)
  {
    x += x * paramLong;
    y += y * paramLong;
  }
  
  public void copyTo(long[] paramArrayOfLong, int paramInt)
  {
    paramArrayOfLong[paramInt] = x;
    paramArrayOfLong[(paramInt + 1)] = y;
  }
  
  public void div(long paramLong)
  {
    x /= paramLong;
    y /= paramLong;
  }
  
  public void div(Long2 paramLong2)
  {
    x /= x;
    y /= y;
  }
  
  public long dotProduct(Long2 paramLong2)
  {
    return x * x + y * y;
  }
  
  public long elementSum()
  {
    return x + y;
  }
  
  public long get(int paramInt)
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
  
  public long length()
  {
    return 2L;
  }
  
  public void mod(long paramLong)
  {
    x %= paramLong;
    y %= paramLong;
  }
  
  public void mod(Long2 paramLong2)
  {
    x %= x;
    y %= y;
  }
  
  public void mul(long paramLong)
  {
    x *= paramLong;
    y *= paramLong;
  }
  
  public void mul(Long2 paramLong2)
  {
    x *= x;
    y *= y;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
  }
  
  public void set(Long2 paramLong2)
  {
    x = x;
    y = y;
  }
  
  public void setAt(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = paramLong;
      return;
    }
    x = paramLong;
  }
  
  public void setValues(long paramLong1, long paramLong2)
  {
    x = paramLong1;
    y = paramLong2;
  }
  
  public void sub(long paramLong)
  {
    x -= paramLong;
    y -= paramLong;
  }
  
  public void sub(Long2 paramLong2)
  {
    x -= x;
    y -= y;
  }
}
