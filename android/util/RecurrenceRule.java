package android.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Objects;

public class RecurrenceRule
  implements Parcelable
{
  public static final Parcelable.Creator<RecurrenceRule> CREATOR = new Parcelable.Creator()
  {
    public RecurrenceRule createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RecurrenceRule(paramAnonymousParcel, null);
    }
    
    public RecurrenceRule[] newArray(int paramAnonymousInt)
    {
      return new RecurrenceRule[paramAnonymousInt];
    }
  };
  private static final boolean LOGD = Log.isLoggable("RecurrenceRule", 3);
  private static final String TAG = "RecurrenceRule";
  private static final int VERSION_INIT = 0;
  @VisibleForTesting
  public static Clock sClock = Clock.systemDefaultZone();
  public final ZonedDateTime end;
  public final Period period;
  public final ZonedDateTime start;
  
  private RecurrenceRule(Parcel paramParcel)
  {
    start = convertZonedDateTime(paramParcel.readString());
    end = convertZonedDateTime(paramParcel.readString());
    period = convertPeriod(paramParcel.readString());
  }
  
  public RecurrenceRule(DataInputStream paramDataInputStream)
    throws IOException
  {
    int i = paramDataInputStream.readInt();
    if (i == 0)
    {
      start = convertZonedDateTime(BackupUtils.readString(paramDataInputStream));
      end = convertZonedDateTime(BackupUtils.readString(paramDataInputStream));
      period = convertPeriod(BackupUtils.readString(paramDataInputStream));
      return;
    }
    paramDataInputStream = new StringBuilder();
    paramDataInputStream.append("Unknown version ");
    paramDataInputStream.append(i);
    throw new ProtocolException(paramDataInputStream.toString());
  }
  
  public RecurrenceRule(ZonedDateTime paramZonedDateTime1, ZonedDateTime paramZonedDateTime2, Period paramPeriod)
  {
    start = paramZonedDateTime1;
    end = paramZonedDateTime2;
    period = paramPeriod;
  }
  
  @Deprecated
  public static RecurrenceRule buildNever()
  {
    return new RecurrenceRule(null, null, null);
  }
  
  @Deprecated
  public static RecurrenceRule buildRecurringMonthly(int paramInt, ZoneId paramZoneId)
  {
    ZonedDateTime localZonedDateTime = ZonedDateTime.now(sClock).withZoneSameInstant(paramZoneId);
    return new RecurrenceRule(ZonedDateTime.of(localZonedDateTime.toLocalDate().minusYears(1L).withMonth(1).withDayOfMonth(paramInt), LocalTime.MIDNIGHT, paramZoneId), null, Period.ofMonths(1));
  }
  
  public static String convertPeriod(Period paramPeriod)
  {
    if (paramPeriod != null) {
      paramPeriod = paramPeriod.toString();
    } else {
      paramPeriod = null;
    }
    return paramPeriod;
  }
  
  public static Period convertPeriod(String paramString)
  {
    if (paramString != null) {
      paramString = Period.parse(paramString);
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public static String convertZonedDateTime(ZonedDateTime paramZonedDateTime)
  {
    if (paramZonedDateTime != null) {
      paramZonedDateTime = paramZonedDateTime.toString();
    } else {
      paramZonedDateTime = null;
    }
    return paramZonedDateTime;
  }
  
  public static ZonedDateTime convertZonedDateTime(String paramString)
  {
    if (paramString != null) {
      paramString = ZonedDateTime.parse(paramString);
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public Iterator<Range<ZonedDateTime>> cycleIterator()
  {
    if (period != null) {
      return new RecurringIterator();
    }
    return new NonrecurringIterator();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof RecurrenceRule;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (RecurrenceRule)paramObject;
      bool1 = bool2;
      if (Objects.equals(start, start))
      {
        bool1 = bool2;
        if (Objects.equals(end, end))
        {
          bool1 = bool2;
          if (Objects.equals(period, period)) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { start, end, period });
  }
  
  @Deprecated
  public boolean isMonthly()
  {
    ZonedDateTime localZonedDateTime = start;
    boolean bool = true;
    if ((localZonedDateTime == null) || (period == null) || (period.getYears() != 0) || (period.getMonths() != 1) || (period.getDays() != 0)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRecurring()
  {
    boolean bool;
    if (period != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("RecurrenceRule{");
    localStringBuilder.append("start=");
    localStringBuilder.append(start);
    localStringBuilder.append(" end=");
    localStringBuilder.append(end);
    localStringBuilder.append(" period=");
    localStringBuilder.append(period);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(convertZonedDateTime(start));
    paramParcel.writeString(convertZonedDateTime(end));
    paramParcel.writeString(convertPeriod(period));
  }
  
  public void writeToStream(DataOutputStream paramDataOutputStream)
    throws IOException
  {
    paramDataOutputStream.writeInt(0);
    BackupUtils.writeString(paramDataOutputStream, convertZonedDateTime(start));
    BackupUtils.writeString(paramDataOutputStream, convertZonedDateTime(end));
    BackupUtils.writeString(paramDataOutputStream, convertPeriod(period));
  }
  
  private class NonrecurringIterator
    implements Iterator<Range<ZonedDateTime>>
  {
    boolean hasNext;
    
    public NonrecurringIterator()
    {
      boolean bool;
      if ((start != null) && (end != null)) {
        bool = true;
      } else {
        bool = false;
      }
      hasNext = bool;
    }
    
    public boolean hasNext()
    {
      return hasNext;
    }
    
    public Range<ZonedDateTime> next()
    {
      hasNext = false;
      return new Range(start, end);
    }
  }
  
  private class RecurringIterator
    implements Iterator<Range<ZonedDateTime>>
  {
    ZonedDateTime cycleEnd;
    ZonedDateTime cycleStart;
    int i;
    
    public RecurringIterator()
    {
      if (end != null) {
        this$1 = end;
      } else {
        this$1 = ZonedDateTime.now(RecurrenceRule.sClock).withZoneSameInstant(start.getZone());
      }
      if (RecurrenceRule.LOGD)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Resolving using anchor ");
        localStringBuilder.append(RecurrenceRule.this);
        Log.d("RecurrenceRule", localStringBuilder.toString());
      }
      updateCycle();
      while (toEpochSecond() > cycleEnd.toEpochSecond())
      {
        i += 1;
        updateCycle();
      }
      while (toEpochSecond() <= cycleStart.toEpochSecond())
      {
        i -= 1;
        updateCycle();
      }
    }
    
    private ZonedDateTime roundBoundaryTime(ZonedDateTime paramZonedDateTime)
    {
      if ((isMonthly()) && (paramZonedDateTime.getDayOfMonth() < start.getDayOfMonth())) {
        return ZonedDateTime.of(paramZonedDateTime.toLocalDate(), LocalTime.MAX, start.getZone());
      }
      return paramZonedDateTime;
    }
    
    private void updateCycle()
    {
      cycleStart = roundBoundaryTime(start.plus(period.multipliedBy(i)));
      cycleEnd = roundBoundaryTime(start.plus(period.multipliedBy(i + 1)));
    }
    
    public boolean hasNext()
    {
      boolean bool;
      if (cycleStart.toEpochSecond() >= start.toEpochSecond()) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public Range<ZonedDateTime> next()
    {
      if (RecurrenceRule.LOGD)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Cycle ");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" from ");
        ((StringBuilder)localObject).append(cycleStart);
        ((StringBuilder)localObject).append(" to ");
        ((StringBuilder)localObject).append(cycleEnd);
        Log.d("RecurrenceRule", ((StringBuilder)localObject).toString());
      }
      Object localObject = new Range(cycleStart, cycleEnd);
      i -= 1;
      updateCycle();
      return localObject;
    }
  }
}
