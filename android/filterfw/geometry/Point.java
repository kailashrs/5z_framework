package android.filterfw.geometry;

public class Point
{
  public float x;
  public float y;
  
  public Point() {}
  
  public Point(float paramFloat1, float paramFloat2)
  {
    x = paramFloat1;
    y = paramFloat2;
  }
  
  public boolean IsInUnitRange()
  {
    boolean bool;
    if ((x >= 0.0F) && (x <= 1.0F) && (y >= 0.0F) && (y <= 1.0F)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public float distanceTo(Point paramPoint)
  {
    return paramPoint.minus(this).length();
  }
  
  public float length()
  {
    return (float)Math.hypot(x, y);
  }
  
  public Point minus(float paramFloat1, float paramFloat2)
  {
    return new Point(x - paramFloat1, y - paramFloat2);
  }
  
  public Point minus(Point paramPoint)
  {
    return minus(x, y);
  }
  
  public Point mult(float paramFloat1, float paramFloat2)
  {
    return new Point(x * paramFloat1, y * paramFloat2);
  }
  
  public Point normalize()
  {
    return scaledTo(1.0F);
  }
  
  public Point plus(float paramFloat1, float paramFloat2)
  {
    return new Point(x + paramFloat1, y + paramFloat2);
  }
  
  public Point plus(Point paramPoint)
  {
    return plus(x, y);
  }
  
  public Point rotated(float paramFloat)
  {
    return new Point((float)(Math.cos(paramFloat) * x - Math.sin(paramFloat) * y), (float)(Math.sin(paramFloat) * x + Math.cos(paramFloat) * y));
  }
  
  public Point rotated90(int paramInt)
  {
    float f1 = x;
    float f2 = y;
    int i = 0;
    while (i < paramInt)
    {
      float f3 = -f1;
      i++;
      f1 = f2;
      f2 = f3;
    }
    return new Point(f1, f2);
  }
  
  public Point rotatedAround(Point paramPoint, float paramFloat)
  {
    return minus(paramPoint).rotated(paramFloat).plus(paramPoint);
  }
  
  public Point scaledTo(float paramFloat)
  {
    return times(paramFloat / length());
  }
  
  public void set(float paramFloat1, float paramFloat2)
  {
    x = paramFloat1;
    y = paramFloat2;
  }
  
  public Point times(float paramFloat)
  {
    return new Point(x * paramFloat, y * paramFloat);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(");
    localStringBuilder.append(x);
    localStringBuilder.append(", ");
    localStringBuilder.append(y);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}
