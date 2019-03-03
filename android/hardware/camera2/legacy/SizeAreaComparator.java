package android.hardware.camera2.legacy;

import android.hardware.Camera.Size;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SizeAreaComparator
  implements Comparator<Camera.Size>
{
  public SizeAreaComparator() {}
  
  public static Camera.Size findLargestByArea(List<Camera.Size> paramList)
  {
    Preconditions.checkNotNull(paramList, "sizes must not be null");
    return (Camera.Size)Collections.max(paramList, new SizeAreaComparator());
  }
  
  public int compare(Camera.Size paramSize1, Camera.Size paramSize2)
  {
    Preconditions.checkNotNull(paramSize1, "size must not be null");
    Preconditions.checkNotNull(paramSize2, "size2 must not be null");
    if (paramSize1.equals(paramSize2)) {
      return 0;
    }
    long l1 = width;
    long l2 = width;
    long l3 = height * l1;
    long l4 = height * l2;
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
