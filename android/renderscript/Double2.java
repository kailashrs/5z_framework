package android.renderscript;

public class Double2
{
  public double x;
  public double y;
  
  public Double2() {}
  
  public Double2(double paramDouble1, double paramDouble2)
  {
    x = paramDouble1;
    y = paramDouble2;
  }
  
  public Double2(Double2 paramDouble2)
  {
    x = x;
    y = y;
  }
  
  public static Double2 add(Double2 paramDouble2, double paramDouble)
  {
    Double2 localDouble2 = new Double2();
    x += paramDouble;
    y += paramDouble;
    return localDouble2;
  }
  
  public static Double2 add(Double2 paramDouble21, Double2 paramDouble22)
  {
    Double2 localDouble2 = new Double2();
    x += x;
    y += y;
    return localDouble2;
  }
  
  public static Double2 div(Double2 paramDouble2, double paramDouble)
  {
    Double2 localDouble2 = new Double2();
    x /= paramDouble;
    y /= paramDouble;
    return localDouble2;
  }
  
  public static Double2 div(Double2 paramDouble21, Double2 paramDouble22)
  {
    Double2 localDouble2 = new Double2();
    x /= x;
    y /= y;
    return localDouble2;
  }
  
  public static Double dotProduct(Double2 paramDouble21, Double2 paramDouble22)
  {
    return Double.valueOf(x * x + y * y);
  }
  
  public static Double2 mul(Double2 paramDouble2, double paramDouble)
  {
    Double2 localDouble2 = new Double2();
    x *= paramDouble;
    y *= paramDouble;
    return localDouble2;
  }
  
  public static Double2 mul(Double2 paramDouble21, Double2 paramDouble22)
  {
    Double2 localDouble2 = new Double2();
    x *= x;
    y *= y;
    return localDouble2;
  }
  
  public static Double2 sub(Double2 paramDouble2, double paramDouble)
  {
    Double2 localDouble2 = new Double2();
    x -= paramDouble;
    y -= paramDouble;
    return localDouble2;
  }
  
  public static Double2 sub(Double2 paramDouble21, Double2 paramDouble22)
  {
    Double2 localDouble2 = new Double2();
    x -= x;
    y -= y;
    return localDouble2;
  }
  
  public void add(double paramDouble)
  {
    x += paramDouble;
    y += paramDouble;
  }
  
  public void add(Double2 paramDouble2)
  {
    x += x;
    y += y;
  }
  
  public void addAt(int paramInt, double paramDouble)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y += paramDouble;
      return;
    }
    x += paramDouble;
  }
  
  public void addMultiple(Double2 paramDouble2, double paramDouble)
  {
    x += x * paramDouble;
    y += y * paramDouble;
  }
  
  public void copyTo(double[] paramArrayOfDouble, int paramInt)
  {
    paramArrayOfDouble[paramInt] = x;
    paramArrayOfDouble[(paramInt + 1)] = y;
  }
  
  public void div(double paramDouble)
  {
    x /= paramDouble;
    y /= paramDouble;
  }
  
  public void div(Double2 paramDouble2)
  {
    x /= x;
    y /= y;
  }
  
  public double dotProduct(Double2 paramDouble2)
  {
    return x * x + y * y;
  }
  
  public double elementSum()
  {
    return x + y;
  }
  
  public double get(int paramInt)
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
  
  public int length()
  {
    return 2;
  }
  
  public void mul(double paramDouble)
  {
    x *= paramDouble;
    y *= paramDouble;
  }
  
  public void mul(Double2 paramDouble2)
  {
    x *= x;
    y *= y;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
  }
  
  public void set(Double2 paramDouble2)
  {
    x = x;
    y = y;
  }
  
  public void setAt(int paramInt, double paramDouble)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = paramDouble;
      return;
    }
    x = paramDouble;
  }
  
  public void setValues(double paramDouble1, double paramDouble2)
  {
    x = paramDouble1;
    y = paramDouble2;
  }
  
  public void sub(double paramDouble)
  {
    x -= paramDouble;
    y -= paramDouble;
  }
  
  public void sub(Double2 paramDouble2)
  {
    x -= x;
    y -= y;
  }
}
