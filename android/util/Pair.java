package android.util;

import java.util.Objects;

public class Pair<F, S>
{
  public final F first;
  public final S second;
  
  public Pair(F paramF, S paramS)
  {
    first = paramF;
    second = paramS;
  }
  
  public static <A, B> Pair<A, B> create(A paramA, B paramB)
  {
    return new Pair(paramA, paramB);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof Pair;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (Pair)paramObject;
    bool1 = bool2;
    if (Objects.equals(first, first))
    {
      bool1 = bool2;
      if (Objects.equals(second, second)) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public int hashCode()
  {
    Object localObject = first;
    int i = 0;
    int j;
    if (localObject == null) {
      j = 0;
    } else {
      j = first.hashCode();
    }
    if (second != null) {
      i = second.hashCode();
    }
    return j ^ i;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Pair{");
    localStringBuilder.append(String.valueOf(first));
    localStringBuilder.append(" ");
    localStringBuilder.append(String.valueOf(second));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
