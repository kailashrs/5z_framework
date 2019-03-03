package android.os;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public abstract class SimpleClock
  extends Clock
{
  private final ZoneId zone;
  
  public SimpleClock(ZoneId paramZoneId)
  {
    zone = paramZoneId;
  }
  
  public ZoneId getZone()
  {
    return zone;
  }
  
  public Instant instant()
  {
    return Instant.ofEpochMilli(millis());
  }
  
  public abstract long millis();
  
  public Clock withZone(ZoneId paramZoneId)
  {
    new SimpleClock(paramZoneId)
    {
      public long millis()
      {
        return SimpleClock.this.millis();
      }
    };
  }
}
