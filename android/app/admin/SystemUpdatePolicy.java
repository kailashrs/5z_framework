package android.app.admin;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.Pair;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class SystemUpdatePolicy
  implements Parcelable
{
  @SystemApi
  public static final Parcelable.Creator<SystemUpdatePolicy> CREATOR = new Parcelable.Creator()
  {
    public SystemUpdatePolicy createFromParcel(Parcel paramAnonymousParcel)
    {
      SystemUpdatePolicy localSystemUpdatePolicy = new SystemUpdatePolicy(null);
      SystemUpdatePolicy.access$202(localSystemUpdatePolicy, paramAnonymousParcel.readInt());
      SystemUpdatePolicy.access$302(localSystemUpdatePolicy, paramAnonymousParcel.readInt());
      SystemUpdatePolicy.access$402(localSystemUpdatePolicy, paramAnonymousParcel.readInt());
      int i = paramAnonymousParcel.readInt();
      mFreezePeriods.ensureCapacity(i);
      for (int j = 0; j < i; j++)
      {
        MonthDay localMonthDay1 = MonthDay.of(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
        MonthDay localMonthDay2 = MonthDay.of(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
        mFreezePeriods.add(new FreezePeriod(localMonthDay1, localMonthDay2));
      }
      return localSystemUpdatePolicy;
    }
    
    public SystemUpdatePolicy[] newArray(int paramAnonymousInt)
    {
      return new SystemUpdatePolicy[paramAnonymousInt];
    }
  };
  static final int FREEZE_PERIOD_MAX_LENGTH = 90;
  static final int FREEZE_PERIOD_MIN_SEPARATION = 60;
  private static final String KEY_FREEZE_END = "end";
  private static final String KEY_FREEZE_START = "start";
  private static final String KEY_FREEZE_TAG = "freeze";
  private static final String KEY_INSTALL_WINDOW_END = "install_window_end";
  private static final String KEY_INSTALL_WINDOW_START = "install_window_start";
  private static final String KEY_POLICY_TYPE = "policy_type";
  private static final String TAG = "SystemUpdatePolicy";
  public static final int TYPE_INSTALL_AUTOMATIC = 1;
  public static final int TYPE_INSTALL_WINDOWED = 2;
  @SystemApi
  public static final int TYPE_PAUSE = 4;
  public static final int TYPE_POSTPONE = 3;
  private static final int TYPE_UNKNOWN = -1;
  private static final int WINDOW_BOUNDARY = 1440;
  private final ArrayList<FreezePeriod> mFreezePeriods = new ArrayList();
  private int mMaintenanceWindowEnd;
  private int mMaintenanceWindowStart;
  private int mPolicyType = -1;
  
  private SystemUpdatePolicy() {}
  
  public static SystemUpdatePolicy createAutomaticInstallPolicy()
  {
    SystemUpdatePolicy localSystemUpdatePolicy = new SystemUpdatePolicy();
    mPolicyType = 1;
    return localSystemUpdatePolicy;
  }
  
  public static SystemUpdatePolicy createPostponeInstallPolicy()
  {
    SystemUpdatePolicy localSystemUpdatePolicy = new SystemUpdatePolicy();
    mPolicyType = 3;
    return localSystemUpdatePolicy;
  }
  
  public static SystemUpdatePolicy createWindowedInstallPolicy(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < 1440) && (paramInt2 >= 0) && (paramInt2 < 1440))
    {
      SystemUpdatePolicy localSystemUpdatePolicy = new SystemUpdatePolicy();
      mPolicyType = 2;
      mMaintenanceWindowStart = paramInt1;
      mMaintenanceWindowEnd = paramInt2;
      return localSystemUpdatePolicy;
    }
    throw new IllegalArgumentException("startTime and endTime must be inside [0, 1440)");
  }
  
  private static long dateToMillis(LocalDate paramLocalDate)
  {
    return LocalDateTime.of(paramLocalDate, LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
  
  private InstallationOption getInstallationOptionRegardlessFreezeAt(long paramLong)
  {
    if ((mPolicyType != 1) && (mPolicyType != 3))
    {
      if (mPolicyType == 2)
      {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(paramLong);
        long l1 = TimeUnit.HOURS.toMillis(localCalendar.get(11)) + TimeUnit.MINUTES.toMillis(localCalendar.get(12)) + TimeUnit.SECONDS.toMillis(localCalendar.get(13)) + localCalendar.get(14);
        paramLong = TimeUnit.MINUTES.toMillis(mMaintenanceWindowStart);
        long l2 = TimeUnit.MINUTES.toMillis(mMaintenanceWindowEnd);
        long l3 = TimeUnit.DAYS.toMillis(1L);
        if (((paramLong <= l1) && (l1 <= l2)) || ((paramLong > l2) && ((paramLong <= l1) || (l1 <= l2)))) {
          return new InstallationOption(1, (l2 - l1 + l3) % l3);
        }
        return new InstallationOption(4, (paramLong - l1 + l3) % l3);
      }
      throw new RuntimeException("Unknown policy type");
    }
    return new InstallationOption(mPolicyType, Long.MAX_VALUE);
  }
  
  private static LocalDate millisToDate(long paramLong)
  {
    return Instant.ofEpochMilli(paramLong).atZone(ZoneId.systemDefault()).toLocalDate();
  }
  
  public static SystemUpdatePolicy restoreFromXml(XmlPullParser paramXmlPullParser)
  {
    try
    {
      SystemUpdatePolicy localSystemUpdatePolicy = new android/app/admin/SystemUpdatePolicy;
      localSystemUpdatePolicy.<init>();
      Object localObject = paramXmlPullParser.getAttributeValue(null, "policy_type");
      if (localObject != null)
      {
        mPolicyType = Integer.parseInt((String)localObject);
        localObject = paramXmlPullParser.getAttributeValue(null, "install_window_start");
        if (localObject != null) {
          mMaintenanceWindowStart = Integer.parseInt((String)localObject);
        }
        localObject = paramXmlPullParser.getAttributeValue(null, "install_window_end");
        if (localObject != null) {
          mMaintenanceWindowEnd = Integer.parseInt((String)localObject);
        }
        int i = paramXmlPullParser.getDepth();
        for (;;)
        {
          int j = paramXmlPullParser.next();
          if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
            break;
          }
          if ((j != 3) && (j != 4) && (paramXmlPullParser.getName().equals("freeze")))
          {
            localObject = mFreezePeriods;
            FreezePeriod localFreezePeriod = new android/app/admin/FreezePeriod;
            localFreezePeriod.<init>(MonthDay.parse(paramXmlPullParser.getAttributeValue(null, "start")), MonthDay.parse(paramXmlPullParser.getAttributeValue(null, "end")));
            ((ArrayList)localObject).add(localFreezePeriod);
          }
        }
        return localSystemUpdatePolicy;
      }
    }
    catch (NumberFormatException|XmlPullParserException|IOException paramXmlPullParser)
    {
      Log.w("SystemUpdatePolicy", "Load xml failed", paramXmlPullParser);
    }
    return null;
  }
  
  private static LocalDate roundUpLeapDay(LocalDate paramLocalDate)
  {
    if ((paramLocalDate.isLeapYear()) && (paramLocalDate.getMonthValue() == 2) && (paramLocalDate.getDayOfMonth() == 28)) {
      return paramLocalDate.plusDays(1L);
    }
    return paramLocalDate;
  }
  
  private long timeUntilNextFreezePeriod(long paramLong)
  {
    List localList = FreezePeriod.canonicalizePeriods(mFreezePeriods);
    LocalDate localLocalDate = millisToDate(paramLong);
    Object localObject1 = null;
    Iterator localIterator = localList.iterator();
    Object localObject2;
    do
    {
      localObject2 = localObject1;
      if (!localIterator.hasNext()) {
        break;
      }
      localObject2 = (FreezePeriod)localIterator.next();
      if (((FreezePeriod)localObject2).after(localLocalDate))
      {
        localObject2 = (LocalDate)toCurrentOrFutureRealDatesfirst;
        break;
      }
    } while (!((FreezePeriod)localObject2).contains(localLocalDate));
    throw new IllegalArgumentException("Given date is inside a freeze period");
    localObject1 = localObject2;
    if (localObject2 == null) {
      localObject1 = (LocalDate)get0toCurrentOrFutureRealDatesfirst;
    }
    return dateToMillis((LocalDate)localObject1) - paramLong;
  }
  
  @SystemApi
  public int describeContents()
  {
    return 0;
  }
  
  public Pair<LocalDate, LocalDate> getCurrentFreezePeriod(LocalDate paramLocalDate)
  {
    Iterator localIterator = mFreezePeriods.iterator();
    while (localIterator.hasNext())
    {
      FreezePeriod localFreezePeriod = (FreezePeriod)localIterator.next();
      if (localFreezePeriod.contains(paramLocalDate)) {
        return localFreezePeriod.toCurrentOrFutureRealDates(paramLocalDate);
      }
    }
    return null;
  }
  
  public List<FreezePeriod> getFreezePeriods()
  {
    return Collections.unmodifiableList(mFreezePeriods);
  }
  
  public int getInstallWindowEnd()
  {
    if (mPolicyType == 2) {
      return mMaintenanceWindowEnd;
    }
    return -1;
  }
  
  public int getInstallWindowStart()
  {
    if (mPolicyType == 2) {
      return mMaintenanceWindowStart;
    }
    return -1;
  }
  
  @SystemApi
  public InstallationOption getInstallationOptionAt(long paramLong)
  {
    Object localObject = getCurrentFreezePeriod(millisToDate(paramLong));
    if (localObject != null) {
      return new InstallationOption(4, dateToMillis(roundUpLeapDay((LocalDate)second).plusDays(1L)) - paramLong);
    }
    localObject = getInstallationOptionRegardlessFreezeAt(paramLong);
    if (mFreezePeriods.size() > 0) {
      ((InstallationOption)localObject).limitEffectiveTime(timeUntilNextFreezePeriod(paramLong));
    }
    return localObject;
  }
  
  public int getPolicyType()
  {
    return mPolicyType;
  }
  
  public boolean isValid()
  {
    try
    {
      validateType();
      validateFreezePeriods();
      return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return false;
  }
  
  public void saveToXml(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.attribute(null, "policy_type", Integer.toString(mPolicyType));
    paramXmlSerializer.attribute(null, "install_window_start", Integer.toString(mMaintenanceWindowStart));
    paramXmlSerializer.attribute(null, "install_window_end", Integer.toString(mMaintenanceWindowEnd));
    for (int i = 0; i < mFreezePeriods.size(); i++)
    {
      FreezePeriod localFreezePeriod = (FreezePeriod)mFreezePeriods.get(i);
      paramXmlSerializer.startTag(null, "freeze");
      paramXmlSerializer.attribute(null, "start", localFreezePeriod.getStart().toString());
      paramXmlSerializer.attribute(null, "end", localFreezePeriod.getEnd().toString());
      paramXmlSerializer.endTag(null, "freeze");
    }
  }
  
  public SystemUpdatePolicy setFreezePeriods(List<FreezePeriod> paramList)
  {
    FreezePeriod.validatePeriods(paramList);
    mFreezePeriods.clear();
    mFreezePeriods.addAll(paramList);
    return this;
  }
  
  public String toString()
  {
    return String.format("SystemUpdatePolicy (type: %d, windowStart: %d, windowEnd: %d, freezes: [%s])", new Object[] { Integer.valueOf(mPolicyType), Integer.valueOf(mMaintenanceWindowStart), Integer.valueOf(mMaintenanceWindowEnd), mFreezePeriods.stream().map(_..Lambda.SystemUpdatePolicy.cfrSWvZcAu30PIPvKA2LGQbmTew.INSTANCE).collect(Collectors.joining(",")) });
  }
  
  public void validateAgainstPreviousFreezePeriod(LocalDate paramLocalDate1, LocalDate paramLocalDate2, LocalDate paramLocalDate3)
  {
    FreezePeriod.validateAgainstPreviousFreezePeriod(mFreezePeriods, paramLocalDate1, paramLocalDate2, paramLocalDate3);
  }
  
  public void validateFreezePeriods()
  {
    FreezePeriod.validatePeriods(mFreezePeriods);
  }
  
  public void validateType()
  {
    if ((mPolicyType != 1) && (mPolicyType != 3))
    {
      if (mPolicyType == 2)
      {
        if ((mMaintenanceWindowStart >= 0) && (mMaintenanceWindowStart < 1440) && (mMaintenanceWindowEnd >= 0) && (mMaintenanceWindowEnd < 1440)) {
          return;
        }
        throw new IllegalArgumentException("Invalid maintenance window");
      }
      throw new IllegalArgumentException("Invalid system update policy type.");
    }
  }
  
  @SystemApi
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPolicyType);
    paramParcel.writeInt(mMaintenanceWindowStart);
    paramParcel.writeInt(mMaintenanceWindowEnd);
    int i = mFreezePeriods.size();
    paramParcel.writeInt(i);
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      FreezePeriod localFreezePeriod = (FreezePeriod)mFreezePeriods.get(paramInt);
      paramParcel.writeInt(localFreezePeriod.getStart().getMonthValue());
      paramParcel.writeInt(localFreezePeriod.getStart().getDayOfMonth());
      paramParcel.writeInt(localFreezePeriod.getEnd().getMonthValue());
      paramParcel.writeInt(localFreezePeriod.getEnd().getDayOfMonth());
    }
  }
  
  @SystemApi
  public static class InstallationOption
  {
    private long mEffectiveTime;
    private final int mType;
    
    InstallationOption(int paramInt, long paramLong)
    {
      mType = paramInt;
      mEffectiveTime = paramLong;
    }
    
    public long getEffectiveTime()
    {
      return mEffectiveTime;
    }
    
    public int getType()
    {
      return mType;
    }
    
    protected void limitEffectiveTime(long paramLong)
    {
      mEffectiveTime = Long.min(mEffectiveTime, paramLong);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    static @interface InstallationOptionType {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface SystemUpdatePolicyType {}
  
  public static final class ValidationFailedException
    extends IllegalArgumentException
    implements Parcelable
  {
    public static final Parcelable.Creator<ValidationFailedException> CREATOR = new Parcelable.Creator()
    {
      public SystemUpdatePolicy.ValidationFailedException createFromParcel(Parcel paramAnonymousParcel)
      {
        return new SystemUpdatePolicy.ValidationFailedException(paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), null);
      }
      
      public SystemUpdatePolicy.ValidationFailedException[] newArray(int paramAnonymousInt)
      {
        return new SystemUpdatePolicy.ValidationFailedException[paramAnonymousInt];
      }
    };
    public static final int ERROR_COMBINED_FREEZE_PERIOD_TOO_CLOSE = 6;
    public static final int ERROR_COMBINED_FREEZE_PERIOD_TOO_LONG = 5;
    public static final int ERROR_DUPLICATE_OR_OVERLAP = 2;
    public static final int ERROR_NEW_FREEZE_PERIOD_TOO_CLOSE = 4;
    public static final int ERROR_NEW_FREEZE_PERIOD_TOO_LONG = 3;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_UNKNOWN = 1;
    private final int mErrorCode;
    
    private ValidationFailedException(int paramInt, String paramString)
    {
      super();
      mErrorCode = paramInt;
    }
    
    public static ValidationFailedException combinedPeriodTooClose(String paramString)
    {
      return new ValidationFailedException(6, paramString);
    }
    
    public static ValidationFailedException combinedPeriodTooLong(String paramString)
    {
      return new ValidationFailedException(5, paramString);
    }
    
    public static ValidationFailedException duplicateOrOverlapPeriods()
    {
      return new ValidationFailedException(2, "Found duplicate or overlapping periods");
    }
    
    public static ValidationFailedException freezePeriodTooClose(String paramString)
    {
      return new ValidationFailedException(4, paramString);
    }
    
    public static ValidationFailedException freezePeriodTooLong(String paramString)
    {
      return new ValidationFailedException(3, paramString);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getErrorCode()
    {
      return mErrorCode;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mErrorCode);
      paramParcel.writeString(getMessage());
    }
    
    @Retention(RetentionPolicy.SOURCE)
    static @interface ValidationFailureType {}
  }
}
