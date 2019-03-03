package android.renderscript;

public class Double4
{
  public double w;
  public double x;
  public double y;
  public double z;
  
  public Double4() {}
  
  public Double4(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    x = paramDouble1;
    y = paramDouble2;
    z = paramDouble3;
    w = paramDouble4;
  }
  
  public Double4(Double4 paramDouble4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public static Double4 add(Double4 paramDouble4, double paramDouble)
  {
    Double4 localDouble4 = new Double4();
    x += paramDouble;
    y += paramDouble;
    z += paramDouble;
    w += paramDouble;
    return localDouble4;
  }
  
  public static Double4 add(Double4 paramDouble41, Double4 paramDouble42)
  {
    Double4 localDouble4 = new Double4();
    x += x;
    y += y;
    z += z;
    w += w;
    return localDouble4;
  }
  
  public static Double4 div(Double4 paramDouble4, double paramDouble)
  {
    Double4 localDouble4 = new Double4();
    x /= paramDouble;
    y /= paramDouble;
    z /= paramDouble;
    w /= paramDouble;
    return localDouble4;
  }
  
  public static Double4 div(Double4 paramDouble41, Double4 paramDouble42)
  {
    Double4 localDouble4 = new Double4();
    x /= x;
    y /= y;
    z /= z;
    w /= w;
    return localDouble4;
  }
  
  public static double dotProduct(Double4 paramDouble41, Double4 paramDouble42)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public static Double4 mul(Double4 paramDouble4, double paramDouble)
  {
    Double4 localDouble4 = new Double4();
    x *= paramDouble;
    y *= paramDouble;
    z *= paramDouble;
    w *= paramDouble;
    return localDouble4;
  }
  
  public static Double4 mul(Double4 paramDouble41, Double4 paramDouble42)
  {
    Double4 localDouble4 = new Double4();
    x *= x;
    y *= y;
    z *= z;
    w *= w;
    return localDouble4;
  }
  
  public static Double4 sub(Double4 paramDouble4, double paramDouble)
  {
    Double4 localDouble4 = new Double4();
    x -= paramDouble;
    y -= paramDouble;
    z -= paramDouble;
    w -= paramDouble;
    return localDouble4;
  }
  
  public static Double4 sub(Double4 paramDouble41, Double4 paramDouble42)
  {
    Double4 localDouble4 = new Double4();
    x -= x;
    y -= y;
    z -= z;
    w -= w;
    return localDouble4;
  }
  
  public void add(double paramDouble)
  {
    x += paramDouble;
    y += paramDouble;
    z += paramDouble;
    w += paramDouble;
  }
  
  public void add(Double4 paramDouble4)
  {
    x += x;
    y += y;
    z += z;
    w += w;
  }
  
  public void addAt(int paramInt, double paramDouble)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w += paramDouble;
      return;
    case 2: 
      z += paramDouble;
      return;
    case 1: 
      y += paramDouble;
      return;
    }
    x += paramDouble;
  }
  
  public void addMultiple(Double4 paramDouble4, double paramDouble)
  {
    x += x * paramDouble;
    y += y * paramDouble;
    z += z * paramDouble;
    w += w * paramDouble;
  }
  
  public void copyTo(double[] paramArrayOfDouble, int paramInt)
  {
    paramArrayOfDouble[paramInt] = x;
    paramArrayOfDouble[(paramInt + 1)] = y;
    paramArrayOfDouble[(paramInt + 2)] = z;
    paramArrayOfDouble[(paramInt + 3)] = w;
  }
  
  public void div(double paramDouble)
  {
    x /= paramDouble;
    y /= paramDouble;
    z /= paramDouble;
    w /= paramDouble;
  }
  
  public void div(Double4 paramDouble4)
  {
    x /= x;
    y /= y;
    z /= z;
    w /= w;
  }
  
  public double dotProduct(Double4 paramDouble4)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public double elementSum()
  {
    return x + y + z + w;
  }
  
  public double get(int paramInt)
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
  
  public int length()
  {
    return 4;
  }
  
  public void mul(double paramDouble)
  {
    x *= paramDouble;
    y *= paramDouble;
    z *= paramDouble;
    w *= paramDouble;
  }
  
  public void mul(Double4 paramDouble4)
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
  
  public void set(Double4 paramDouble4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public void setAt(int paramInt, double paramDouble)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = paramDouble;
      return;
    case 2: 
      z = paramDouble;
      return;
    case 1: 
      y = paramDouble;
      return;
    }
    x = paramDouble;
  }
  
  public void setValues(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    x = paramDouble1;
    y = paramDouble2;
    z = paramDouble3;
    w = paramDouble4;
  }
  
  public void sub(double paramDouble)
  {
    x -= paramDouble;
    y -= paramDouble;
    z -= paramDouble;
    w -= paramDouble;
  }
  
  public void sub(Double4 paramDouble4)
  {
    x -= x;
    y -= y;
    z -= z;
    w -= w;
  }
}
