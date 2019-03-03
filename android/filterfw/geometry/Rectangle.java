package android.filterfw.geometry;

public class Rectangle
  extends Quad
{
  public Rectangle() {}
  
  public Rectangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    super(new Point(paramFloat1, paramFloat2), new Point(paramFloat1 + paramFloat3, paramFloat2), new Point(paramFloat1, paramFloat2 + paramFloat4), new Point(paramFloat1 + paramFloat3, paramFloat2 + paramFloat4));
  }
  
  public Rectangle(Point paramPoint1, Point paramPoint2)
  {
    super(paramPoint1, paramPoint1.plus(x, 0.0F), paramPoint1.plus(0.0F, y), paramPoint1.plus(x, y));
  }
  
  private Rectangle(Point paramPoint1, Point paramPoint2, Point paramPoint3, Point paramPoint4)
  {
    super(paramPoint1, paramPoint2, paramPoint3, paramPoint4);
  }
  
  public static Rectangle fromCenterVerticalAxis(Point paramPoint1, Point paramPoint2, Point paramPoint3)
  {
    Point localPoint = paramPoint2.scaledTo(y / 2.0F);
    paramPoint2 = paramPoint2.rotated90(1).scaledTo(x / 2.0F);
    return new Rectangle(paramPoint1.minus(paramPoint2).minus(localPoint), paramPoint1.plus(paramPoint2).minus(localPoint), paramPoint1.minus(paramPoint2).plus(localPoint), paramPoint1.plus(paramPoint2).plus(localPoint));
  }
  
  public static Rectangle fromRotatedRect(Point paramPoint1, Point paramPoint2, float paramFloat)
  {
    Point localPoint1 = new Point(x - x / 2.0F, y - y / 2.0F);
    Point localPoint2 = new Point(x + x / 2.0F, y - y / 2.0F);
    Point localPoint3 = new Point(x - x / 2.0F, y + y / 2.0F);
    paramPoint2 = new Point(x + x / 2.0F, y + y / 2.0F);
    return new Rectangle(localPoint1.rotatedAround(paramPoint1, paramFloat), localPoint2.rotatedAround(paramPoint1, paramFloat), localPoint3.rotatedAround(paramPoint1, paramFloat), paramPoint2.rotatedAround(paramPoint1, paramFloat));
  }
  
  public Point center()
  {
    return p0.plus(p1).plus(p2).plus(p3).times(0.25F);
  }
  
  public float getHeight()
  {
    return p2.minus(p0).length();
  }
  
  public float getWidth()
  {
    return p1.minus(p0).length();
  }
  
  public Rectangle scaled(float paramFloat)
  {
    return new Rectangle(p0.times(paramFloat), p1.times(paramFloat), p2.times(paramFloat), p3.times(paramFloat));
  }
  
  public Rectangle scaled(float paramFloat1, float paramFloat2)
  {
    return new Rectangle(p0.mult(paramFloat1, paramFloat2), p1.mult(paramFloat1, paramFloat2), p2.mult(paramFloat1, paramFloat2), p3.mult(paramFloat1, paramFloat2));
  }
}
