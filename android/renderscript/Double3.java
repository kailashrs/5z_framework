package android.renderscript;

public class Double3
{
  public double x;
  public double y;
  public double z;
  
  public Double3() {}
  
  public Double3(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    x = paramDouble1;
    y = paramDouble2;
    z = paramDouble3;
  }
  
  public Double3(Double3 paramDouble3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public static Double3 add(Double3 paramDouble3, double paramDouble)
  {
    Double3 localDouble3 = new Double3();
    x += paramDouble;
    y += paramDouble;
    z += paramDouble;
    return localDouble3;
  }
  
  public static Double3 add(Double3 paramDouble31, Double3 paramDouble32)
  {
    Double3 localDouble3 = new Double3();
    x += x;
    y += y;
    z += z;
    return localDouble3;
  }
  
  public static Double3 div(Double3 paramDouble3, double paramDouble)
  {
    Double3 localDouble3 = new Double3();
    x /= paramDouble;
    y /= paramDouble;
    z /= paramDouble;
    return localDouble3;
  }
  
  public static Double3 div(Double3 paramDouble31, Double3 paramDouble32)
  {
    Double3 localDouble3 = new Double3();
    x /= x;
    y /= y;
    z /= z;
    return localDouble3;
  }
  
  public static double dotProduct(Double3 paramDouble31, Double3 paramDouble32)
  {
    return x * x + y * y + z * z;
  }
  
  public static Double3 mul(Double3 paramDouble3, double paramDouble)
  {
    Double3 localDouble3 = new Double3();
    x *= paramDouble;
    y *= paramDouble;
    z *= paramDouble;
    return localDouble3;
  }
  
  public static Double3 mul(Double3 paramDouble31, Double3 paramDouble32)
  {
    Double3 localDouble3 = new Double3();
    x *= x;
    y *= y;
    z *= z;
    return localDouble3;
  }
  
  public static Double3 sub(Double3 paramDouble3, double paramDouble)
  {
    Double3 localDouble3 = new Double3();
    x -= paramDouble;
    y -= paramDouble;
    z -= paramDouble;
    return localDouble3;
  }
  
  public static Double3 sub(Double3 paramDouble31, Double3 paramDouble32)
  {
    Double3 localDouble3 = new Double3();
    x -= x;
    y -= y;
    z -= z;
    return localDouble3;
  }
  
  public void add(double paramDouble)
  {
    x += paramDouble;
    y += paramDouble;
    z += paramDouble;
  }
  
  public void add(Double3 paramDouble3)
  {
    x += x;
    y += y;
    z += z;
  }
  
  public void addAt(int paramInt, double paramDouble)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z += paramDouble;
      return;
    case 1: 
      y += paramDouble;
      return;
    }
    x += paramDouble;
  }
  
  public void addMultiple(Double3 paramDouble3, double paramDouble)
  {
    x += x * paramDouble;
    y += y * paramDouble;
    z += z * paramDouble;
  }
  
  public void copyTo(double[] paramArrayOfDouble, int paramInt)
  {
    paramArrayOfDouble[paramInt] = x;
    paramArrayOfDouble[(paramInt + 1)] = y;
    paramArrayOfDouble[(paramInt + 2)] = z;
  }
  
  public void div(double paramDouble)
  {
    x /= paramDouble;
    y /= paramDouble;
    z /= paramDouble;
  }
  
  public void div(Double3 paramDouble3)
  {
    x /= x;
    y /= y;
    z /= z;
  }
  
  public double dotProduct(Double3 paramDouble3)
  {
    return x * x + y * y + z * z;
  }
  
  public double elementSum()
  {
    return x + y + z;
  }
  
  public double get(int paramInt)
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
  
  public int length()
  {
    return 3;
  }
  
  public void mul(double paramDouble)
  {
    x *= paramDouble;
    y *= paramDouble;
    z *= paramDouble;
  }
  
  public void mul(Double3 paramDouble3)
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
  
  public void set(Double3 paramDouble3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public void setAt(int paramInt, double paramDouble)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = paramDouble;
      return;
    case 1: 
      y = paramDouble;
      return;
    }
    x = paramDouble;
  }
  
  public void setValues(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    x = paramDouble1;
    y = paramDouble2;
    z = paramDouble3;
  }
  
  public void sub(double paramDouble)
  {
    x -= paramDouble;
    y -= paramDouble;
    z -= paramDouble;
  }
  
  public void sub(Double3 paramDouble3)
  {
    x -= x;
    y -= y;
    z -= z;
  }
}
