package android.os;

import android.util.Log;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Arrays;

public class BestClock
  extends SimpleClock
{
  private static final String TAG = "BestClock";
  private final Clock[] clocks;
  
  public BestClock(ZoneId paramZoneId, Clock... paramVarArgs)
  {
    super(paramZoneId);
    clocks = paramVarArgs;
  }
  
  public long millis()
  {
    Object localObject1 = clocks;
    int i = localObject1.length;
    int j = 0;
    while (j < i)
    {
      Object localObject2 = localObject1[j];
      try
      {
        long l = localObject2.millis();
        return l;
      }
      catch (DateTimeException localDateTimeException)
      {
        Log.w("BestClock", localDateTimeException.toString());
        j++;
      }
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("No clocks in ");
    ((StringBuilder)localObject1).append(Arrays.toString(clocks));
    ((StringBuilder)localObject1).append(" were able to provide time");
    throw new DateTimeException(((StringBuilder)localObject1).toString());
  }
}
