package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Range;
import android.util.RecurrenceRule;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Objects;

@SystemApi
public final class SubscriptionPlan
  implements Parcelable
{
  public static final long BYTES_UNKNOWN = -1L;
  public static final long BYTES_UNLIMITED = Long.MAX_VALUE;
  public static final Parcelable.Creator<SubscriptionPlan> CREATOR = new Parcelable.Creator()
  {
    public SubscriptionPlan createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SubscriptionPlan(paramAnonymousParcel, null);
    }
    
    public SubscriptionPlan[] newArray(int paramAnonymousInt)
    {
      return new SubscriptionPlan[paramAnonymousInt];
    }
  };
  public static final int LIMIT_BEHAVIOR_BILLED = 1;
  public static final int LIMIT_BEHAVIOR_DISABLED = 0;
  public static final int LIMIT_BEHAVIOR_THROTTLED = 2;
  public static final int LIMIT_BEHAVIOR_UNKNOWN = -1;
  public static final long TIME_UNKNOWN = -1L;
  private final RecurrenceRule cycleRule;
  private int dataLimitBehavior = -1;
  private long dataLimitBytes = -1L;
  private long dataUsageBytes = -1L;
  private long dataUsageTime = -1L;
  private CharSequence summary;
  private CharSequence title;
  
  private SubscriptionPlan(Parcel paramParcel)
  {
    cycleRule = ((RecurrenceRule)paramParcel.readParcelable(null));
    title = paramParcel.readCharSequence();
    summary = paramParcel.readCharSequence();
    dataLimitBytes = paramParcel.readLong();
    dataLimitBehavior = paramParcel.readInt();
    dataUsageBytes = paramParcel.readLong();
    dataUsageTime = paramParcel.readLong();
  }
  
  private SubscriptionPlan(RecurrenceRule paramRecurrenceRule)
  {
    cycleRule = ((RecurrenceRule)Preconditions.checkNotNull(paramRecurrenceRule));
  }
  
  public Iterator<Range<ZonedDateTime>> cycleIterator()
  {
    return cycleRule.cycleIterator();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof SubscriptionPlan;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (SubscriptionPlan)paramObject;
      bool1 = bool2;
      if (Objects.equals(cycleRule, cycleRule))
      {
        bool1 = bool2;
        if (Objects.equals(title, title))
        {
          bool1 = bool2;
          if (Objects.equals(summary, summary))
          {
            bool1 = bool2;
            if (dataLimitBytes == dataLimitBytes)
            {
              bool1 = bool2;
              if (dataLimitBehavior == dataLimitBehavior)
              {
                bool1 = bool2;
                if (dataUsageBytes == dataUsageBytes)
                {
                  bool1 = bool2;
                  if (dataUsageTime == dataUsageTime) {
                    bool1 = true;
                  }
                }
              }
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public RecurrenceRule getCycleRule()
  {
    return cycleRule;
  }
  
  public int getDataLimitBehavior()
  {
    return dataLimitBehavior;
  }
  
  public long getDataLimitBytes()
  {
    return dataLimitBytes;
  }
  
  public long getDataUsageBytes()
  {
    return dataUsageBytes;
  }
  
  public long getDataUsageTime()
  {
    return dataUsageTime;
  }
  
  public CharSequence getSummary()
  {
    return summary;
  }
  
  public CharSequence getTitle()
  {
    return title;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { cycleRule, title, summary, Long.valueOf(dataLimitBytes), Integer.valueOf(dataLimitBehavior), Long.valueOf(dataUsageBytes), Long.valueOf(dataUsageTime) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("SubscriptionPlan{");
    localStringBuilder.append("cycleRule=");
    localStringBuilder.append(cycleRule);
    localStringBuilder.append(" title=");
    localStringBuilder.append(title);
    localStringBuilder.append(" summary=");
    localStringBuilder.append(summary);
    localStringBuilder.append(" dataLimitBytes=");
    localStringBuilder.append(dataLimitBytes);
    localStringBuilder.append(" dataLimitBehavior=");
    localStringBuilder.append(dataLimitBehavior);
    localStringBuilder.append(" dataUsageBytes=");
    localStringBuilder.append(dataUsageBytes);
    localStringBuilder.append(" dataUsageTime=");
    localStringBuilder.append(dataUsageTime);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(cycleRule, paramInt);
    paramParcel.writeCharSequence(title);
    paramParcel.writeCharSequence(summary);
    paramParcel.writeLong(dataLimitBytes);
    paramParcel.writeInt(dataLimitBehavior);
    paramParcel.writeLong(dataUsageBytes);
    paramParcel.writeLong(dataUsageTime);
  }
  
  public static class Builder
  {
    private final SubscriptionPlan plan;
    
    public Builder(ZonedDateTime paramZonedDateTime1, ZonedDateTime paramZonedDateTime2, Period paramPeriod)
    {
      plan = new SubscriptionPlan(new RecurrenceRule(paramZonedDateTime1, paramZonedDateTime2, paramPeriod), null);
    }
    
    public static Builder createNonrecurring(ZonedDateTime paramZonedDateTime1, ZonedDateTime paramZonedDateTime2)
    {
      if (paramZonedDateTime2.isAfter(paramZonedDateTime1)) {
        return new Builder(paramZonedDateTime1, paramZonedDateTime2, null);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("End ");
      localStringBuilder.append(paramZonedDateTime2);
      localStringBuilder.append(" isn't after start ");
      localStringBuilder.append(paramZonedDateTime1);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public static Builder createRecurring(ZonedDateTime paramZonedDateTime, Period paramPeriod)
    {
      if ((!paramPeriod.isZero()) && (!paramPeriod.isNegative())) {
        return new Builder(paramZonedDateTime, null, paramPeriod);
      }
      paramZonedDateTime = new StringBuilder();
      paramZonedDateTime.append("Period ");
      paramZonedDateTime.append(paramPeriod);
      paramZonedDateTime.append(" must be positive");
      throw new IllegalArgumentException(paramZonedDateTime.toString());
    }
    
    @SystemApi
    @Deprecated
    public static Builder createRecurringDaily(ZonedDateTime paramZonedDateTime)
    {
      return new Builder(paramZonedDateTime, null, Period.ofDays(1));
    }
    
    @SystemApi
    @Deprecated
    public static Builder createRecurringMonthly(ZonedDateTime paramZonedDateTime)
    {
      return new Builder(paramZonedDateTime, null, Period.ofMonths(1));
    }
    
    @SystemApi
    @Deprecated
    public static Builder createRecurringWeekly(ZonedDateTime paramZonedDateTime)
    {
      return new Builder(paramZonedDateTime, null, Period.ofDays(7));
    }
    
    public SubscriptionPlan build()
    {
      return plan;
    }
    
    public Builder setDataLimit(long paramLong, int paramInt)
    {
      if (paramLong >= 0L)
      {
        if (paramInt >= 0)
        {
          SubscriptionPlan.access$402(plan, paramLong);
          SubscriptionPlan.access$502(plan, paramInt);
          return this;
        }
        throw new IllegalArgumentException("Limit behavior must be defined");
      }
      throw new IllegalArgumentException("Limit bytes must be positive");
    }
    
    public Builder setDataUsage(long paramLong1, long paramLong2)
    {
      if (paramLong1 >= 0L)
      {
        if (paramLong2 >= 0L)
        {
          SubscriptionPlan.access$602(plan, paramLong1);
          SubscriptionPlan.access$702(plan, paramLong2);
          return this;
        }
        throw new IllegalArgumentException("Usage time must be positive");
      }
      throw new IllegalArgumentException("Usage bytes must be positive");
    }
    
    public Builder setSummary(CharSequence paramCharSequence)
    {
      SubscriptionPlan.access$302(plan, paramCharSequence);
      return this;
    }
    
    public Builder setTitle(CharSequence paramCharSequence)
    {
      SubscriptionPlan.access$202(plan, paramCharSequence);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LimitBehavior {}
}
