package android.graphics;

public class Insets
{
  public static final Insets NONE = new Insets(0, 0, 0, 0);
  public final int bottom;
  public final int left;
  public final int right;
  public final int top;
  
  private Insets(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    left = paramInt1;
    top = paramInt2;
    right = paramInt3;
    bottom = paramInt4;
  }
  
  public static Insets of(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt1 == 0) && (paramInt2 == 0) && (paramInt3 == 0) && (paramInt4 == 0)) {
      return NONE;
    }
    return new Insets(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static Insets of(Rect paramRect)
  {
    if (paramRect == null) {
      paramRect = NONE;
    } else {
      paramRect = of(left, top, right, bottom);
    }
    return paramRect;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (Insets)paramObject;
      if (bottom != bottom) {
        return false;
      }
      if (left != left) {
        return false;
      }
      if (right != right) {
        return false;
      }
      return top == top;
    }
    return false;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * left + top) + right) + bottom;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Insets{left=");
    localStringBuilder.append(left);
    localStringBuilder.append(", top=");
    localStringBuilder.append(top);
    localStringBuilder.append(", right=");
    localStringBuilder.append(right);
    localStringBuilder.append(", bottom=");
    localStringBuilder.append(bottom);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
