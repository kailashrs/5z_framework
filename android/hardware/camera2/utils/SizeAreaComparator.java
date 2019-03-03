package android.hardware.camera2.utils;

import android.util.Size;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SizeAreaComparator
  implements Comparator<Size>
{
  public SizeAreaComparator() {}
  
  public static Size findLargestByArea(List<Size> paramList)
  {
    Preconditions.checkNotNull(paramList, "sizes must not be null");
    return (Size)Collections.max(paramList, new SizeAreaComparator());
  }
  
  public int compare(Size paramSize1, Size paramSize2)
  {
    Preconditions.checkNotNull(paramSize1, "size must not be null");
    Preconditions.checkNotNull(paramSize2, "size2 must not be null");
    if (paramSize1.equals(paramSize2)) {
      return 0;
    }
    long l1 = paramSize1.getWidth();
    long l2 = paramSize2.getWidth();
    long l3 = paramSize1.getHeight() * l1;
    long l4 = paramSize2.getHeight() * l2;
    int i = -1;
    if (l3 == l4)
    {
      if (l1 > l2) {
        i = 1;
      }
      return i;
    }
    if (l3 > l4) {
      i = 1;
    }
    return i;
  }
}
