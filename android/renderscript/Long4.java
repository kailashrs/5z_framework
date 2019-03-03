package android.renderscript;

public class Long4
{
  public long w;
  public long x;
  public long y;
  public long z;
  
  public Long4() {}
  
  public Long4(long paramLong)
  {
    w = paramLong;
    z = paramLong;
    y = paramLong;
    x = paramLong;
  }
  
  public Long4(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    x = paramLong1;
    y = paramLong2;
    z = paramLong3;
    w = paramLong4;
  }
  
  public Long4(Long4 paramLong4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public static Long4 add(Long4 paramLong4, long paramLong)
  {
    Long4 localLong4 = new Long4();
    x += paramLong;
    y += paramLong;
    z += paramLong;
    w += paramLong;
    return localLong4;
  }
  
  public static Long4 add(Long4 paramLong41, Long4 paramLong42)
  {
    Long4 localLong4 = new Long4();
    x += x;
    y += y;
    z += z;
    w += w;
    return localLong4;
  }
  
  public static Long4 div(Long4 paramLong4, long paramLong)
  {
    Long4 localLong4 = new Long4();
    x /= paramLong;
    y /= paramLong;
    z /= paramLong;
    w /= paramLong;
    return localLong4;
  }
  
  public static Long4 div(Long4 paramLong41, Long4 paramLong42)
  {
    Long4 localLong4 = new Long4();
    x /= x;
    y /= y;
    z /= z;
    w /= w;
    return localLong4;
  }
  
  public static long dotProduct(Long4 paramLong41, Long4 paramLong42)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public static Long4 mod(Long4 paramLong4, long paramLong)
  {
    Long4 localLong4 = new Long4();
    x %= paramLong;
    y %= paramLong;
    z %= paramLong;
    w %= paramLong;
    return localLong4;
  }
  
  public static Long4 mod(Long4 paramLong41, Long4 paramLong42)
  {
    Long4 localLong4 = new Long4();
    x %= x;
    y %= y;
    z %= z;
    w %= w;
    return localLong4;
  }
  
  public static Long4 mul(Long4 paramLong4, long paramLong)
  {
    Long4 localLong4 = new Long4();
    x *= paramLong;
    y *= paramLong;
    z *= paramLong;
    w *= paramLong;
    return localLong4;
  }
  
  public static Long4 mul(Long4 paramLong41, Long4 paramLong42)
  {
    Long4 localLong4 = new Long4();
    x *= x;
    y *= y;
    z *= z;
    w *= w;
    return localLong4;
  }
  
  public static Long4 sub(Long4 paramLong4, long paramLong)
  {
    Long4 localLong4 = new Long4();
    x -= paramLong;
    y -= paramLong;
    z -= paramLong;
    w -= paramLong;
    return localLong4;
  }
  
  public static Long4 sub(Long4 paramLong41, Long4 paramLong42)
  {
    Long4 localLong4 = new Long4();
    x -= x;
    y -= y;
    z -= z;
    w -= w;
    return localLong4;
  }
  
  public void add(long paramLong)
  {
    x += paramLong;
    y += paramLong;
    z += paramLong;
    w += paramLong;
  }
  
  public void add(Long4 paramLong4)
  {
    x += x;
    y += y;
    z += z;
    w += w;
  }
  
  public void addAt(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w += paramLong;
      return;
    case 2: 
      z += paramLong;
      return;
    case 1: 
      y += paramLong;
      return;
    }
    x += paramLong;
  }
  
  public void addMultiple(Long4 paramLong4, long paramLong)
  {
    x += x * paramLong;
    y += y * paramLong;
    z += z * paramLong;
    w += w * paramLong;
  }
  
  public void copyTo(long[] paramArrayOfLong, int paramInt)
  {
    paramArrayOfLong[paramInt] = x;
    paramArrayOfLong[(paramInt + 1)] = y;
    paramArrayOfLong[(paramInt + 2)] = z;
    paramArrayOfLong[(paramInt + 3)] = w;
  }
  
  public void div(long paramLong)
  {
    x /= paramLong;
    y /= paramLong;
    z /= paramLong;
    w /= paramLong;
  }
  
  public void div(Long4 paramLong4)
  {
    x /= x;
    y /= y;
    z /= z;
    w /= w;
  }
  
  public long dotProduct(Long4 paramLong4)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public long elementSum()
  {
    return x + y + z + w;
  }
  
  public long get(int paramInt)
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
  
  public long length()
  {
    return 4L;
  }
  
  public void mod(long paramLong)
  {
    x %= paramLong;
    y %= paramLong;
    z %= paramLong;
    w %= paramLong;
  }
  
  public void mod(Long4 paramLong4)
  {
    x %= x;
    y %= y;
    z %= z;
    w %= w;
  }
  
  public void mul(long paramLong)
  {
    x *= paramLong;
    y *= paramLong;
    z *= paramLong;
    w *= paramLong;
  }
  
  public void mul(Long4 paramLong4)
  {
    x *= x;
    y *= y;
    z *= z;
    w *= w;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
    z = (-z);
    w = (-w);
  }
  
  public void set(Long4 paramLong4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public void setAt(int paramInt, long paramLong)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = paramLong;
      return;
    case 2: 
      z = paramLong;
      return;
    case 1: 
      y = paramLong;
      return;
    }
    x = paramLong;
  }
  
  public void setValues(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    x = paramLong1;
    y = paramLong2;
    z = paramLong3;
    w = paramLong4;
  }
  
  public void sub(long paramLong)
  {
    x -= paramLong;
    y -= paramLong;
    z -= paramLong;
    w -= paramLong;
  }
  
  public void sub(Long4 paramLong4)
  {
    x -= x;
    y -= y;
    z -= z;
    w -= w;
  }
}
