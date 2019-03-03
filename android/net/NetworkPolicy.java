package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.BackupUtils;
import android.util.BackupUtils.BadVersionException;
import android.util.Range;
import android.util.RecurrenceRule;
import com.android.internal.util.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Objects;

public class NetworkPolicy
  implements Parcelable, Comparable<NetworkPolicy>
{
  public static final Parcelable.Creator<NetworkPolicy> CREATOR = new Parcelable.Creator()
  {
    public NetworkPolicy createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkPolicy(paramAnonymousParcel, null);
    }
    
    public NetworkPolicy[] newArray(int paramAnonymousInt)
    {
      return new NetworkPolicy[paramAnonymousInt];
    }
  };
  public static final int CYCLE_NONE = -1;
  private static final long DEFAULT_MTU = 1500L;
  public static final long LIMIT_DISABLED = -1L;
  public static final long SNOOZE_NEVER = -1L;
  private static final int VERSION_INIT = 1;
  private static final int VERSION_RAPID = 3;
  private static final int VERSION_RULE = 2;
  public static final long WARNING_DISABLED = -1L;
  public RecurrenceRule cycleRule;
  public boolean inferred;
  public long lastLimitSnooze = -1L;
  public long lastRapidSnooze = -1L;
  public long lastWarningSnooze = -1L;
  public long limitBytes = -1L;
  @Deprecated
  public boolean metered;
  public NetworkTemplate template;
  public long warningBytes = -1L;
  
  @Deprecated
  public NetworkPolicy(NetworkTemplate paramNetworkTemplate, int paramInt, String paramString, long paramLong1, long paramLong2, long paramLong3, long paramLong4, boolean paramBoolean1, boolean paramBoolean2)
  {
    this(paramNetworkTemplate, buildRule(paramInt, ZoneId.of(paramString)), paramLong1, paramLong2, paramLong3, paramLong4, paramBoolean1, paramBoolean2);
  }
  
  @Deprecated
  public NetworkPolicy(NetworkTemplate paramNetworkTemplate, int paramInt, String paramString, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    this(paramNetworkTemplate, paramInt, paramString, paramLong1, paramLong2, -1L, -1L, paramBoolean, false);
  }
  
  public NetworkPolicy(NetworkTemplate paramNetworkTemplate, RecurrenceRule paramRecurrenceRule, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, boolean paramBoolean1, boolean paramBoolean2)
  {
    metered = true;
    inferred = false;
    template = ((NetworkTemplate)Preconditions.checkNotNull(paramNetworkTemplate, "missing NetworkTemplate"));
    cycleRule = ((RecurrenceRule)Preconditions.checkNotNull(paramRecurrenceRule, "missing RecurrenceRule"));
    warningBytes = paramLong1;
    limitBytes = paramLong2;
    lastWarningSnooze = paramLong3;
    lastLimitSnooze = paramLong4;
    lastRapidSnooze = paramLong5;
    metered = paramBoolean1;
    inferred = paramBoolean2;
  }
  
  @Deprecated
  public NetworkPolicy(NetworkTemplate paramNetworkTemplate, RecurrenceRule paramRecurrenceRule, long paramLong1, long paramLong2, long paramLong3, long paramLong4, boolean paramBoolean1, boolean paramBoolean2)
  {
    this(paramNetworkTemplate, paramRecurrenceRule, paramLong1, paramLong2, paramLong3, paramLong4, -1L, paramBoolean1, paramBoolean2);
  }
  
  private NetworkPolicy(Parcel paramParcel)
  {
    boolean bool1 = true;
    metered = true;
    inferred = false;
    template = ((NetworkTemplate)paramParcel.readParcelable(null));
    cycleRule = ((RecurrenceRule)paramParcel.readParcelable(null));
    warningBytes = paramParcel.readLong();
    limitBytes = paramParcel.readLong();
    lastWarningSnooze = paramParcel.readLong();
    lastLimitSnooze = paramParcel.readLong();
    lastRapidSnooze = paramParcel.readLong();
    boolean bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    metered = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    inferred = bool2;
  }
  
  public static RecurrenceRule buildRule(int paramInt, ZoneId paramZoneId)
  {
    if (paramInt != -1) {
      return RecurrenceRule.buildRecurringMonthly(paramInt, paramZoneId);
    }
    return RecurrenceRule.buildNever();
  }
  
  public static NetworkPolicy getNetworkPolicyFromBackup(DataInputStream paramDataInputStream)
    throws IOException, BackupUtils.BadVersionException
  {
    int i = paramDataInputStream.readInt();
    if ((i >= 1) && (i <= 3))
    {
      NetworkTemplate localNetworkTemplate = NetworkTemplate.getNetworkTemplateFromBackup(paramDataInputStream);
      if (i >= 2) {}
      for (RecurrenceRule localRecurrenceRule = new RecurrenceRule(paramDataInputStream);; localRecurrenceRule = buildRule(paramDataInputStream.readInt(), ZoneId.of(BackupUtils.readString(paramDataInputStream)))) {
        break;
      }
      long l1 = paramDataInputStream.readLong();
      long l2 = paramDataInputStream.readLong();
      long l3 = paramDataInputStream.readLong();
      long l4 = paramDataInputStream.readLong();
      if (i >= 3) {}
      for (long l5 = paramDataInputStream.readLong();; l5 = -1L) {
        break;
      }
      boolean bool1;
      if (paramDataInputStream.readInt() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramDataInputStream.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      return new NetworkPolicy(localNetworkTemplate, localRecurrenceRule, l1, l2, l3, l4, l5, bool1, bool2);
    }
    paramDataInputStream = new StringBuilder();
    paramDataInputStream.append("Unknown backup version: ");
    paramDataInputStream.append(i);
    throw new BackupUtils.BadVersionException(paramDataInputStream.toString());
  }
  
  public void clearSnooze()
  {
    lastWarningSnooze = -1L;
    lastLimitSnooze = -1L;
    lastRapidSnooze = -1L;
  }
  
  public int compareTo(NetworkPolicy paramNetworkPolicy)
  {
    if ((paramNetworkPolicy != null) && (limitBytes != -1L))
    {
      if ((limitBytes != -1L) && (limitBytes >= limitBytes)) {
        return 0;
      }
      return 1;
    }
    return -1;
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
    boolean bool1 = paramObject instanceof NetworkPolicy;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (NetworkPolicy)paramObject;
      bool1 = bool2;
      if (warningBytes == warningBytes)
      {
        bool1 = bool2;
        if (limitBytes == limitBytes)
        {
          bool1 = bool2;
          if (lastWarningSnooze == lastWarningSnooze)
          {
            bool1 = bool2;
            if (lastLimitSnooze == lastLimitSnooze)
            {
              bool1 = bool2;
              if (lastRapidSnooze == lastRapidSnooze)
              {
                bool1 = bool2;
                if (metered == metered)
                {
                  bool1 = bool2;
                  if (inferred == inferred)
                  {
                    bool1 = bool2;
                    if (Objects.equals(template, template))
                    {
                      bool1 = bool2;
                      if (Objects.equals(cycleRule, cycleRule)) {
                        bool1 = true;
                      }
                    }
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
  
  public byte[] getBytesForBackup()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    localDataOutputStream.writeInt(3);
    localDataOutputStream.write(template.getBytesForBackup());
    cycleRule.writeToStream(localDataOutputStream);
    localDataOutputStream.writeLong(warningBytes);
    localDataOutputStream.writeLong(limitBytes);
    localDataOutputStream.writeLong(lastWarningSnooze);
    localDataOutputStream.writeLong(lastLimitSnooze);
    localDataOutputStream.writeLong(lastRapidSnooze);
    localDataOutputStream.writeInt(metered);
    localDataOutputStream.writeInt(inferred);
    return localByteArrayOutputStream.toByteArray();
  }
  
  public boolean hasCycle()
  {
    return cycleRule.cycleIterator().hasNext();
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { template, cycleRule, Long.valueOf(warningBytes), Long.valueOf(limitBytes), Long.valueOf(lastWarningSnooze), Long.valueOf(lastLimitSnooze), Long.valueOf(lastRapidSnooze), Boolean.valueOf(metered), Boolean.valueOf(inferred) });
  }
  
  public boolean isOverLimit(long paramLong)
  {
    boolean bool;
    if ((limitBytes != -1L) && (paramLong + 3000L >= limitBytes)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOverWarning(long paramLong)
  {
    boolean bool;
    if ((warningBytes != -1L) && (paramLong >= warningBytes)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("NetworkPolicy{");
    localStringBuilder.append("template=");
    localStringBuilder.append(template);
    localStringBuilder.append(" cycleRule=");
    localStringBuilder.append(cycleRule);
    localStringBuilder.append(" warningBytes=");
    localStringBuilder.append(warningBytes);
    localStringBuilder.append(" limitBytes=");
    localStringBuilder.append(limitBytes);
    localStringBuilder.append(" lastWarningSnooze=");
    localStringBuilder.append(lastWarningSnooze);
    localStringBuilder.append(" lastLimitSnooze=");
    localStringBuilder.append(lastLimitSnooze);
    localStringBuilder.append(" lastRapidSnooze=");
    localStringBuilder.append(lastRapidSnooze);
    localStringBuilder.append(" metered=");
    localStringBuilder.append(metered);
    localStringBuilder.append(" inferred=");
    localStringBuilder.append(inferred);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(template, paramInt);
    paramParcel.writeParcelable(cycleRule, paramInt);
    paramParcel.writeLong(warningBytes);
    paramParcel.writeLong(limitBytes);
    paramParcel.writeLong(lastWarningSnooze);
    paramParcel.writeLong(lastLimitSnooze);
    paramParcel.writeLong(lastRapidSnooze);
    paramParcel.writeInt(metered);
    paramParcel.writeInt(inferred);
  }
}
