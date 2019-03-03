package android.filterfw.geometry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Quad
{
  public Point p0;
  public Point p1;
  public Point p2;
  public Point p3;
  
  public Quad() {}
  
  public Quad(Point paramPoint1, Point paramPoint2, Point paramPoint3, Point paramPoint4)
  {
    p0 = paramPoint1;
    p1 = paramPoint2;
    p2 = paramPoint3;
    p3 = paramPoint4;
  }
  
  public boolean IsInUnitRange()
  {
    boolean bool;
    if ((p0.IsInUnitRange()) && (p1.IsInUnitRange()) && (p2.IsInUnitRange()) && (p3.IsInUnitRange())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Rectangle boundingBox()
  {
    List localList1 = Arrays.asList(new Float[] { Float.valueOf(p0.x), Float.valueOf(p1.x), Float.valueOf(p2.x), Float.valueOf(p3.x) });
    List localList2 = Arrays.asList(new Float[] { Float.valueOf(p0.y), Float.valueOf(p1.y), Float.valueOf(p2.y), Float.valueOf(p3.y) });
    float f1 = ((Float)Collections.min(localList1)).floatValue();
    float f2 = ((Float)Collections.min(localList2)).floatValue();
    return new Rectangle(f1, f2, ((Float)Collections.max(localList1)).floatValue() - f1, ((Float)Collections.max(localList2)).floatValue() - f2);
  }
  
  public float getBoundingHeight()
  {
    List localList = Arrays.asList(new Float[] { Float.valueOf(p0.y), Float.valueOf(p1.y), Float.valueOf(p2.y), Float.valueOf(p3.y) });
    return ((Float)Collections.max(localList)).floatValue() - ((Float)Collections.min(localList)).floatValue();
  }
  
  public float getBoundingWidth()
  {
    List localList = Arrays.asList(new Float[] { Float.valueOf(p0.x), Float.valueOf(p1.x), Float.valueOf(p2.x), Float.valueOf(p3.x) });
    return ((Float)Collections.max(localList)).floatValue() - ((Float)Collections.min(localList)).floatValue();
  }
  
  public Quad scaled(float paramFloat)
  {
    return new Quad(p0.times(paramFloat), p1.times(paramFloat), p2.times(paramFloat), p3.times(paramFloat));
  }
  
  public Quad scaled(float paramFloat1, float paramFloat2)
  {
    return new Quad(p0.mult(paramFloat1, paramFloat2), p1.mult(paramFloat1, paramFloat2), p2.mult(paramFloat1, paramFloat2), p3.mult(paramFloat1, paramFloat2));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(p0);
    localStringBuilder.append(", ");
    localStringBuilder.append(p1);
    localStringBuilder.append(", ");
    localStringBuilder.append(p2);
    localStringBuilder.append(", ");
    localStringBuilder.append(p3);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public Quad translated(float paramFloat1, float paramFloat2)
  {
    return new Quad(p0.plus(paramFloat1, paramFloat2), p1.plus(paramFloat1, paramFloat2), p2.plus(paramFloat1, paramFloat2), p3.plus(paramFloat1, paramFloat2));
  }
  
  public Quad translated(Point paramPoint)
  {
    return new Quad(p0.plus(paramPoint), p1.plus(paramPoint), p2.plus(paramPoint), p3.plus(paramPoint));
  }
}
