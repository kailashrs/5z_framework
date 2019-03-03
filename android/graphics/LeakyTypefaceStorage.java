package android.graphics;

import android.os.Parcel;
import android.os.Process;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;

public class LeakyTypefaceStorage
{
  private static final Object sLock = new Object();
  @GuardedBy("sLock")
  private static final ArrayList<Typeface> sStorage = new ArrayList();
  @GuardedBy("sLock")
  private static final ArrayMap<Typeface, Integer> sTypefaceMap = new ArrayMap();
  
  public LeakyTypefaceStorage() {}
  
  public static Typeface readTypefaceFromParcel(Parcel arg0)
  {
    int i = ???.readInt();
    int j = ???.readInt();
    if (i != Process.myPid()) {
      return null;
    }
    synchronized (sLock)
    {
      Typeface localTypeface = (Typeface)sStorage.get(j);
      return localTypeface;
    }
  }
  
  public static void writeTypefaceToParcel(Typeface paramTypeface, Parcel paramParcel)
  {
    paramParcel.writeInt(Process.myPid());
    synchronized (sLock)
    {
      Integer localInteger = (Integer)sTypefaceMap.get(paramTypeface);
      int i;
      if (localInteger != null)
      {
        i = localInteger.intValue();
      }
      else
      {
        i = sStorage.size();
        sStorage.add(paramTypeface);
        sTypefaceMap.put(paramTypeface, Integer.valueOf(i));
      }
      paramParcel.writeInt(i);
      return;
    }
  }
}
