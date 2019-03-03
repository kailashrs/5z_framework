package android.service.notification;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlarmManager.AlarmClockInfo;
import android.app.NotificationManager.Policy;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class ZenModeConfig
  implements Parcelable
{
  private static final String ALLOW_ATT_ALARMS = "alarms";
  private static final String ALLOW_ATT_CALLS = "calls";
  private static final String ALLOW_ATT_CALLS_FROM = "callsFrom";
  private static final String ALLOW_ATT_EVENTS = "events";
  private static final String ALLOW_ATT_FROM = "from";
  private static final String ALLOW_ATT_MEDIA = "media";
  private static final String ALLOW_ATT_MESSAGES = "messages";
  private static final String ALLOW_ATT_MESSAGES_FROM = "messagesFrom";
  private static final String ALLOW_ATT_REMINDERS = "reminders";
  private static final String ALLOW_ATT_REPEAT_CALLERS = "repeatCallers";
  private static final String ALLOW_ATT_SCREEN_OFF = "visualScreenOff";
  private static final String ALLOW_ATT_SCREEN_ON = "visualScreenOn";
  private static final String ALLOW_ATT_SYSTEM = "system";
  private static final String ALLOW_TAG = "allow";
  public static final int[] ALL_DAYS;
  private static final String AUTOMATIC_TAG = "automatic";
  private static final String CONDITION_ATT_FLAGS = "flags";
  private static final String CONDITION_ATT_ICON = "icon";
  private static final String CONDITION_ATT_ID = "id";
  private static final String CONDITION_ATT_LINE1 = "line1";
  private static final String CONDITION_ATT_LINE2 = "line2";
  private static final String CONDITION_ATT_STATE = "state";
  private static final String CONDITION_ATT_SUMMARY = "summary";
  public static final String COUNTDOWN_PATH = "countdown";
  public static final Parcelable.Creator<ZenModeConfig> CREATOR = new Parcelable.Creator()
  {
    public ZenModeConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ZenModeConfig(paramAnonymousParcel);
    }
    
    public ZenModeConfig[] newArray(int paramAnonymousInt)
    {
      return new ZenModeConfig[paramAnonymousInt];
    }
  };
  private static final int DAY_MINUTES = 1440;
  private static final boolean DEFAULT_ALLOW_ALARMS = true;
  private static final boolean DEFAULT_ALLOW_CALLS = true;
  private static final boolean DEFAULT_ALLOW_EVENTS = false;
  private static final boolean DEFAULT_ALLOW_MEDIA = true;
  private static final boolean DEFAULT_ALLOW_MESSAGES = false;
  private static final boolean DEFAULT_ALLOW_REMINDERS = false;
  private static final boolean DEFAULT_ALLOW_REPEAT_CALLERS = true;
  private static final boolean DEFAULT_ALLOW_SYSTEM = false;
  private static final int DEFAULT_CALLS_SOURCE = 2;
  private static final boolean DEFAULT_CHANNELS_BYPASSING_DND = false;
  public static final boolean DEFAULT_O_ALLOW_CALLS = true;
  public static final int DEFAULT_O_ALLOW_CALLS_FROM = 1;
  public static final boolean DEFAULT_O_ALLOW_EVENTS = true;
  public static final boolean DEFAULT_O_ALLOW_MESSAGES = false;
  public static final boolean DEFAULT_O_ALLOW_REMINDERS = true;
  public static final boolean DEFAULT_O_ALLOW_REPEAT_CALLERS = false;
  public static final List<String> DEFAULT_RULE_IDS;
  private static final int DEFAULT_SOURCE = 1;
  private static final int DEFAULT_SUPPRESSED_VISUAL_EFFECTS = 0;
  private static final String DISALLOW_ATT_VISUAL_EFFECTS = "visualEffects";
  private static final String DISALLOW_TAG = "disallow";
  public static final String EVENTS_DEFAULT_RULE_ID = "EVENTS_DEFAULT_RULE";
  public static final String EVENT_PATH = "event";
  public static final String EVERY_NIGHT_DEFAULT_RULE_ID = "EVERY_NIGHT_DEFAULT_RULE";
  public static final String IS_ALARM_PATH = "alarm";
  private static final String MANUAL_TAG = "manual";
  public static final int MAX_SOURCE = 2;
  private static final int MINUTES_MS = 60000;
  public static final int[] MINUTE_BUCKETS;
  private static final String RULE_ATT_COMPONENT = "component";
  private static final String RULE_ATT_CONDITION_ID = "conditionId";
  private static final String RULE_ATT_CREATION_TIME = "creationTime";
  private static final String RULE_ATT_ENABLED = "enabled";
  private static final String RULE_ATT_ENABLER = "enabler";
  private static final String RULE_ATT_ID = "ruleId";
  private static final String RULE_ATT_NAME = "name";
  private static final String RULE_ATT_SNOOZING = "snoozing";
  private static final String RULE_ATT_ZEN = "zen";
  public static final String SCHEDULE_PATH = "schedule";
  private static final int SECONDS_MS = 1000;
  public static final int SOURCE_ANYONE = 0;
  public static final int SOURCE_CONTACT = 1;
  public static final int SOURCE_STAR = 2;
  private static final String STATE_ATT_CHANNELS_BYPASSING_DND = "areChannelsBypassingDnd";
  private static final String STATE_TAG = "state";
  public static final String SYSTEM_AUTHORITY = "android";
  private static String TAG = "ZenModeConfig";
  public static final int XML_ASUS_INIT_VERSION = 0;
  public static final int XML_ASUS_VERSION = 1;
  public static final int XML_P_VERSION = 8;
  public static final int XML_VERSION = 8;
  private static final String ZEN_ATT_ASUS_VERSION = "asus_version";
  private static final String ZEN_ATT_USER = "user";
  private static final String ZEN_ATT_VERSION = "version";
  public static final String ZEN_TAG = "zen";
  private static final int ZERO_VALUE_MS = 10000;
  public boolean allowAlarms;
  public boolean allowCalls;
  public int allowCallsFrom;
  public boolean allowEvents;
  public boolean allowMedia;
  public boolean allowMessages;
  public int allowMessagesFrom;
  public boolean allowReminders;
  public boolean allowRepeatCallers;
  public boolean allowSystem;
  public boolean areChannelsBypassingDnd;
  public int asus_version;
  public ArrayMap<String, ZenRule> automaticRules;
  public ZenRule manualRule;
  public int suppressedVisualEffects;
  public int user;
  public int version;
  
  static
  {
    DEFAULT_RULE_IDS = Arrays.asList(new String[] { "EVERY_NIGHT_DEFAULT_RULE", "EVENTS_DEFAULT_RULE" });
    ALL_DAYS = new int[] { 1, 2, 3, 4, 5, 6, 7 };
    MINUTE_BUCKETS = generateMinuteBuckets();
  }
  
  public ZenModeConfig()
  {
    allowAlarms = true;
    allowMedia = true;
    allowSystem = false;
    allowCalls = true;
    allowRepeatCallers = true;
    allowMessages = false;
    allowReminders = false;
    allowEvents = false;
    allowCallsFrom = 2;
    allowMessagesFrom = 1;
    user = 0;
    suppressedVisualEffects = 0;
    areChannelsBypassingDnd = false;
    automaticRules = new ArrayMap();
  }
  
  public ZenModeConfig(Parcel paramParcel)
  {
    boolean bool1 = true;
    allowAlarms = true;
    allowMedia = true;
    allowSystem = false;
    allowCalls = true;
    allowRepeatCallers = true;
    allowMessages = false;
    allowReminders = false;
    allowEvents = false;
    allowCallsFrom = 2;
    allowMessagesFrom = 1;
    user = 0;
    suppressedVisualEffects = 0;
    areChannelsBypassingDnd = false;
    automaticRules = new ArrayMap();
    boolean bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowCalls = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowRepeatCallers = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowMessages = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowReminders = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowEvents = bool2;
    allowCallsFrom = paramParcel.readInt();
    allowMessagesFrom = paramParcel.readInt();
    user = paramParcel.readInt();
    manualRule = ((ZenRule)paramParcel.readParcelable(null));
    int i = paramParcel.readInt();
    if (i > 0)
    {
      String[] arrayOfString = new String[i];
      ZenRule[] arrayOfZenRule = new ZenRule[i];
      paramParcel.readStringArray(arrayOfString);
      paramParcel.readTypedArray(arrayOfZenRule, ZenRule.CREATOR);
      for (int j = 0; j < i; j++) {
        automaticRules.put(arrayOfString[j], arrayOfZenRule[j]);
      }
    }
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowAlarms = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowMedia = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowSystem = bool2;
    suppressedVisualEffects = paramParcel.readInt();
    if (paramParcel.readInt() == 1) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    areChannelsBypassingDnd = bool2;
  }
  
  private static <T> void addKeys(ArraySet<T> paramArraySet, ArrayMap<T, ?> paramArrayMap)
  {
    if (paramArrayMap != null) {
      for (int i = 0; i < paramArrayMap.size(); i++) {
        paramArraySet.add(paramArrayMap.keyAt(i));
      }
    }
  }
  
  public static boolean areAllPriorityOnlyNotificationZenSoundsMuted(NotificationManager.Policy paramPolicy)
  {
    int i = priorityCategories;
    boolean bool = true;
    if ((i & 0x1) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if ((priorityCategories & 0x8) != 0) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if ((priorityCategories & 0x4) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if ((priorityCategories & 0x2) != 0) {
      m = 1;
    } else {
      m = 0;
    }
    int n;
    if ((priorityCategories & 0x10) != 0) {
      n = 1;
    } else {
      n = 0;
    }
    int i1;
    if ((state & 0x1) != 0) {
      i1 = 1;
    } else {
      i1 = 0;
    }
    if ((i != 0) || (j != 0) || (k != 0) || (m != 0) || (n != 0) || (i1 != 0)) {
      bool = false;
    }
    return bool;
  }
  
  public static boolean areAllPriorityOnlyNotificationZenSoundsMuted(ZenModeConfig paramZenModeConfig)
  {
    boolean bool;
    if ((!allowReminders) && (!allowCalls) && (!allowMessages) && (!allowEvents) && (!allowRepeatCallers) && (!areChannelsBypassingDnd)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean areAllZenBehaviorSoundsMuted(ZenModeConfig paramZenModeConfig)
  {
    boolean bool;
    if ((!allowAlarms) && (!allowMedia) && (!allowSystem) && (areAllPriorityOnlyNotificationZenSoundsMuted(paramZenModeConfig))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private Diff diff(ZenModeConfig paramZenModeConfig)
  {
    Diff localDiff = new Diff();
    if (paramZenModeConfig == null) {
      return localDiff.addLine("config", "delete");
    }
    if (user != user) {
      localDiff.addLine("user", Integer.valueOf(user), Integer.valueOf(user));
    }
    if (allowAlarms != allowAlarms) {
      localDiff.addLine("allowAlarms", Boolean.valueOf(allowAlarms), Boolean.valueOf(allowAlarms));
    }
    if (allowMedia != allowMedia) {
      localDiff.addLine("allowMedia", Boolean.valueOf(allowMedia), Boolean.valueOf(allowMedia));
    }
    if (allowSystem != allowSystem) {
      localDiff.addLine("allowSystem", Boolean.valueOf(allowSystem), Boolean.valueOf(allowSystem));
    }
    if (allowCalls != allowCalls) {
      localDiff.addLine("allowCalls", Boolean.valueOf(allowCalls), Boolean.valueOf(allowCalls));
    }
    if (allowReminders != allowReminders) {
      localDiff.addLine("allowReminders", Boolean.valueOf(allowReminders), Boolean.valueOf(allowReminders));
    }
    if (allowEvents != allowEvents) {
      localDiff.addLine("allowEvents", Boolean.valueOf(allowEvents), Boolean.valueOf(allowEvents));
    }
    if (allowRepeatCallers != allowRepeatCallers) {
      localDiff.addLine("allowRepeatCallers", Boolean.valueOf(allowRepeatCallers), Boolean.valueOf(allowRepeatCallers));
    }
    if (allowMessages != allowMessages) {
      localDiff.addLine("allowMessages", Boolean.valueOf(allowMessages), Boolean.valueOf(allowMessages));
    }
    if (allowCallsFrom != allowCallsFrom) {
      localDiff.addLine("allowCallsFrom", Integer.valueOf(allowCallsFrom), Integer.valueOf(allowCallsFrom));
    }
    if (allowMessagesFrom != allowMessagesFrom) {
      localDiff.addLine("allowMessagesFrom", Integer.valueOf(allowMessagesFrom), Integer.valueOf(allowMessagesFrom));
    }
    if (suppressedVisualEffects != suppressedVisualEffects) {
      localDiff.addLine("suppressedVisualEffects", Integer.valueOf(suppressedVisualEffects), Integer.valueOf(suppressedVisualEffects));
    }
    ArraySet localArraySet = new ArraySet();
    addKeys(localArraySet, automaticRules);
    addKeys(localArraySet, automaticRules);
    int i = localArraySet.size();
    for (int j = 0; j < i; j++)
    {
      String str = (String)localArraySet.valueAt(j);
      Object localObject = automaticRules;
      ZenRule localZenRule = null;
      if (localObject != null) {
        localObject = (ZenRule)automaticRules.get(str);
      } else {
        localObject = null;
      }
      if (automaticRules != null) {
        localZenRule = (ZenRule)automaticRules.get(str);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("automaticRule[");
      localStringBuilder.append(str);
      localStringBuilder.append("]");
      ZenRule.appendDiff(localDiff, localStringBuilder.toString(), (ZenRule)localObject, localZenRule);
    }
    ZenRule.appendDiff(localDiff, "manualRule", manualRule, manualRule);
    if (areChannelsBypassingDnd != areChannelsBypassingDnd) {
      localDiff.addLine("areChannelsBypassingDnd", Boolean.valueOf(areChannelsBypassingDnd), Boolean.valueOf(areChannelsBypassingDnd));
    }
    return localDiff;
  }
  
  public static Diff diff(ZenModeConfig paramZenModeConfig1, ZenModeConfig paramZenModeConfig2)
  {
    if (paramZenModeConfig1 == null)
    {
      paramZenModeConfig1 = new Diff();
      if (paramZenModeConfig2 != null) {
        paramZenModeConfig1.addLine("config", "insert");
      }
      return paramZenModeConfig1;
    }
    return paramZenModeConfig1.diff(paramZenModeConfig2);
  }
  
  private static int[] generateMinuteBuckets()
  {
    int[] arrayOfInt = new int[15];
    arrayOfInt[0] = 15;
    int i = 1;
    arrayOfInt[1] = 30;
    arrayOfInt[2] = 45;
    while (i <= 12)
    {
      arrayOfInt[(2 + i)] = (60 * i);
      i++;
    }
    return arrayOfInt;
  }
  
  private static String getConditionLine(Context paramContext, ZenModeConfig paramZenModeConfig, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramZenModeConfig == null) {
      return "";
    }
    Object localObject = "";
    if (manualRule != null)
    {
      localObject = manualRule.conditionId;
      if (manualRule.enabler != null)
      {
        localObject = getOwnerCaption(paramContext, manualRule.enabler);
      }
      else if (localObject == null)
      {
        localObject = paramContext.getString(17041322);
      }
      else
      {
        long l = tryParseCountdownConditionId((Uri)localObject);
        localObject = manualRule.condition;
        if (l > 0L) {
          localObject = toTimeCondition(paramContext, l, Math.round((float)(l - System.currentTimeMillis()) / 60000.0F), paramInt, paramBoolean2);
        }
        if (localObject == null) {
          localObject = "";
        } else if (paramBoolean1) {
          localObject = line1;
        } else {
          localObject = summary;
        }
        if (TextUtils.isEmpty((CharSequence)localObject)) {
          localObject = "";
        }
      }
    }
    Iterator localIterator = automaticRules.values().iterator();
    while (localIterator.hasNext())
    {
      ZenRule localZenRule = (ZenRule)localIterator.next();
      paramZenModeConfig = (ZenModeConfig)localObject;
      if (localZenRule.isAutomaticActive()) {
        if (((String)localObject).isEmpty()) {
          paramZenModeConfig = name;
        } else {
          paramZenModeConfig = paramContext.getResources().getString(17041324, new Object[] { localObject, name });
        }
      }
      localObject = paramZenModeConfig;
    }
    return localObject;
  }
  
  public static String getConditionSummary(Context paramContext, ZenModeConfig paramZenModeConfig, int paramInt, boolean paramBoolean)
  {
    return getConditionLine(paramContext, paramZenModeConfig, paramInt, false, paramBoolean);
  }
  
  public static String getDescription(Context paramContext, boolean paramBoolean1, ZenModeConfig paramZenModeConfig, boolean paramBoolean2)
  {
    Object localObject1 = null;
    if ((paramBoolean1) && (paramZenModeConfig != null))
    {
      Object localObject2 = "";
      long l1 = -1L;
      Object localObject3 = localObject2;
      long l2 = l1;
      Object localObject4;
      if (manualRule != null)
      {
        localObject3 = manualRule.conditionId;
        if (manualRule.enabler != null)
        {
          localObject4 = getOwnerCaption(paramContext, manualRule.enabler);
          localObject3 = localObject2;
          if (!((String)localObject4).isEmpty()) {
            localObject3 = localObject4;
          }
          l2 = l1;
        }
        else
        {
          if (localObject3 == null)
          {
            if (paramBoolean2) {
              return paramContext.getString(17041322);
            }
            return null;
          }
          l1 = tryParseCountdownConditionId((Uri)localObject3);
          localObject3 = localObject2;
          l2 = l1;
          if (l1 > 0L)
          {
            localObject3 = paramContext.getString(17041325, new Object[] { getFormattedTime(paramContext, l1, isToday(l1), paramContext.getUserId()) });
            l2 = l1;
          }
        }
      }
      localObject2 = automaticRules.values().iterator();
      paramZenModeConfig = (ZenModeConfig)localObject3;
      while (((Iterator)localObject2).hasNext())
      {
        localObject4 = (ZenRule)((Iterator)localObject2).next();
        localObject3 = paramZenModeConfig;
        l1 = l2;
        if (((ZenRule)localObject4).isAutomaticActive())
        {
          if ((!isValidEventConditionId(conditionId)) && (!isValidScheduleConditionId(conditionId))) {
            return name;
          }
          long l3 = parseAutomaticRuleEndTime(paramContext, conditionId);
          l1 = l2;
          if (l3 > l2)
          {
            l1 = l3;
            paramZenModeConfig = name;
          }
          localObject3 = paramZenModeConfig;
        }
        paramZenModeConfig = (ZenModeConfig)localObject3;
        l2 = l1;
      }
      paramContext = localObject1;
      if (!paramZenModeConfig.equals("")) {
        paramContext = paramZenModeConfig;
      }
      return paramContext;
    }
    return null;
  }
  
  public static ComponentName getEventConditionProvider()
  {
    return new ComponentName("android", "EventConditionProvider");
  }
  
  public static CharSequence getFormattedTime(Context paramContext, long paramLong, boolean paramBoolean, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str;
    if (!paramBoolean) {
      str = "EEE ";
    } else {
      str = "";
    }
    localStringBuilder.append(str);
    if (DateFormat.is24HourFormat(paramContext, paramInt)) {
      paramContext = "Hm";
    } else {
      paramContext = "hma";
    }
    localStringBuilder.append(paramContext);
    paramContext = localStringBuilder.toString();
    return DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), paramContext), paramLong);
  }
  
  private static long getNextAlarm(Context paramContext)
  {
    paramContext = ((AlarmManager)paramContext.getSystemService("alarm")).getNextAlarmClock(paramContext.getUserId());
    long l;
    if (paramContext != null) {
      l = paramContext.getTriggerTime();
    } else {
      l = 0L;
    }
    return l;
  }
  
  public static String getOwnerCaption(Context paramContext, String paramString)
  {
    paramContext = paramContext.getPackageManager();
    try
    {
      paramString = paramContext.getApplicationInfo(paramString, 0);
      if (paramString != null)
      {
        paramContext = paramString.loadLabel(paramContext);
        if (paramContext != null)
        {
          paramContext = paramContext.toString().trim();
          int i = paramContext.length();
          if (i > 0) {
            return paramContext;
          }
        }
      }
    }
    catch (Throwable paramContext)
    {
      Slog.w(TAG, "Error loading owner caption", paramContext);
    }
    return "";
  }
  
  public static ComponentName getScheduleConditionProvider()
  {
    return new ComponentName("android", "ScheduleConditionProvider");
  }
  
  public static boolean isToday(long paramLong)
  {
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar();
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar();
    localGregorianCalendar2.setTimeInMillis(paramLong);
    return (localGregorianCalendar1.get(1) == localGregorianCalendar2.get(1)) && (localGregorianCalendar1.get(2) == localGregorianCalendar2.get(2)) && (localGregorianCalendar1.get(5) == localGregorianCalendar2.get(5));
  }
  
  private static boolean isValidAutomaticRule(ZenRule paramZenRule)
  {
    boolean bool;
    if ((paramZenRule != null) && (!TextUtils.isEmpty(name)) && (Settings.Global.isValidZenMode(zenMode)) && (conditionId != null) && (sameCondition(paramZenRule))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidCountdownConditionId(Uri paramUri)
  {
    boolean bool;
    if (tryParseCountdownConditionId(paramUri) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidCountdownToAlarmConditionId(Uri paramUri)
  {
    if (tryParseCountdownConditionId(paramUri) != 0L)
    {
      if ((paramUri.getPathSegments().size() >= 4) && ("alarm".equals(paramUri.getPathSegments().get(2)))) {
        try
        {
          boolean bool = Boolean.parseBoolean((String)paramUri.getPathSegments().get(3));
          return bool;
        }
        catch (RuntimeException localRuntimeException)
        {
          String str = TAG;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Error parsing countdown alarm condition: ");
          localStringBuilder.append(paramUri);
          Slog.w(str, localStringBuilder.toString(), localRuntimeException);
          return false;
        }
      }
      return false;
    }
    return false;
  }
  
  public static boolean isValidEventConditionId(Uri paramUri)
  {
    boolean bool;
    if (tryParseEventConditionId(paramUri) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidHour(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < 24)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidManualRule(ZenRule paramZenRule)
  {
    boolean bool;
    if ((paramZenRule != null) && ((!Settings.Global.isValidZenMode(zenMode)) || (!sameCondition(paramZenRule)))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isValidMinute(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < 60)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidScheduleConditionId(Uri paramUri)
  {
    try
    {
      paramUri = tryParseScheduleConditionId(paramUri);
      return (paramUri != null) && (days != null) && (days.length != 0);
    }
    catch (NullPointerException|ArrayIndexOutOfBoundsException paramUri) {}
    return false;
  }
  
  private static boolean isValidSource(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isZenOverridingRinger(int paramInt, ZenModeConfig paramZenModeConfig)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 2)
    {
      bool2 = bool1;
      if (paramInt != 3) {
        if ((paramInt == 1) && (areAllPriorityOnlyNotificationZenSoundsMuted(paramZenModeConfig))) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  public static String newRuleId()
  {
    return UUID.randomUUID().toString().replace("-", "");
  }
  
  private static long parseAutomaticRuleEndTime(Context paramContext, Uri paramUri)
  {
    if (isValidEventConditionId(paramUri)) {
      return Long.MAX_VALUE;
    }
    if (isValidScheduleConditionId(paramUri))
    {
      paramUri = toScheduleCalendar(paramUri);
      long l1 = paramUri.getNextChangeTime(System.currentTimeMillis());
      if (paramUri.exitAtAlarm())
      {
        long l2 = getNextAlarm(paramContext);
        paramUri.maybeSetNextAlarm(System.currentTimeMillis(), l2);
        if (paramUri.shouldExitForAlarm(l1)) {
          return l2;
        }
      }
      return l1;
    }
    return -1L;
  }
  
  private static int prioritySendersToSource(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return paramInt2;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  public static Condition readConditionXml(XmlPullParser paramXmlPullParser)
  {
    Uri localUri = safeUri(paramXmlPullParser, "id");
    if (localUri == null) {
      return null;
    }
    String str1 = paramXmlPullParser.getAttributeValue(null, "summary");
    String str2 = paramXmlPullParser.getAttributeValue(null, "line1");
    String str3 = paramXmlPullParser.getAttributeValue(null, "line2");
    int i = safeInt(paramXmlPullParser, "icon", -1);
    int j = safeInt(paramXmlPullParser, "state", -1);
    int k = safeInt(paramXmlPullParser, "flags", -1);
    try
    {
      paramXmlPullParser = new Condition(localUri, str1, str2, str3, i, j, k);
      return paramXmlPullParser;
    }
    catch (IllegalArgumentException paramXmlPullParser)
    {
      Slog.w(TAG, "Unable to read condition xml", paramXmlPullParser);
    }
    return null;
  }
  
  public static ZenRule readRuleXml(XmlPullParser paramXmlPullParser)
  {
    Object localObject1 = new ZenRule();
    enabled = safeBoolean(paramXmlPullParser, "enabled", true);
    snoozing = safeBoolean(paramXmlPullParser, "snoozing", false);
    name = paramXmlPullParser.getAttributeValue(null, "name");
    Object localObject2 = paramXmlPullParser.getAttributeValue(null, "zen");
    zenMode = tryParseZenMode((String)localObject2, -1);
    if (zenMode == -1)
    {
      paramXmlPullParser = TAG;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Bad zen mode in rule xml:");
      ((StringBuilder)localObject1).append((String)localObject2);
      Slog.w(paramXmlPullParser, ((StringBuilder)localObject1).toString());
      return null;
    }
    conditionId = safeUri(paramXmlPullParser, "conditionId");
    component = safeComponentName(paramXmlPullParser, "component");
    creationTime = safeLong(paramXmlPullParser, "creationTime", 0L);
    enabler = paramXmlPullParser.getAttributeValue(null, "enabler");
    condition = readConditionXml(paramXmlPullParser);
    if ((zenMode != 1) && (Condition.isValidId(conditionId, "android")))
    {
      paramXmlPullParser = TAG;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Updating zenMode of automatic rule ");
      ((StringBuilder)localObject2).append(name);
      Slog.i(paramXmlPullParser, ((StringBuilder)localObject2).toString());
      zenMode = 1;
    }
    return localObject1;
  }
  
  public static ZenModeConfig readXml(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    if (paramXmlPullParser.getEventType() != 2) {
      return null;
    }
    if (!"zen".equals(paramXmlPullParser.getName())) {
      return null;
    }
    ZenModeConfig localZenModeConfig = new ZenModeConfig();
    version = safeInt(paramXmlPullParser, "version", 8);
    asus_version = safeInt(paramXmlPullParser, "asus_version", 0);
    user = safeInt(paramXmlPullParser, "user", user);
    int i = 0;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (j == 1) {
        break;
      }
      Object localObject1 = paramXmlPullParser.getName();
      if ((j == 3) && ("zen".equals(localObject1))) {
        return localZenModeConfig;
      }
      if (j == 2)
      {
        Object localObject2;
        if ("allow".equals(localObject1))
        {
          allowCalls = safeBoolean(paramXmlPullParser, "calls", true);
          allowRepeatCallers = safeBoolean(paramXmlPullParser, "repeatCallers", true);
          allowMessages = safeBoolean(paramXmlPullParser, "messages", false);
          allowReminders = safeBoolean(paramXmlPullParser, "reminders", false);
          allowEvents = safeBoolean(paramXmlPullParser, "events", false);
          int k = safeInt(paramXmlPullParser, "from", -1);
          j = safeInt(paramXmlPullParser, "callsFrom", -1);
          int m = safeInt(paramXmlPullParser, "messagesFrom", -1);
          if ((isValidSource(j)) && (isValidSource(m)))
          {
            allowCallsFrom = j;
            allowMessagesFrom = m;
          }
          else if (isValidSource(k))
          {
            localObject1 = TAG;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Migrating existing shared 'from': ");
            ((StringBuilder)localObject2).append(sourceToString(k));
            Slog.i((String)localObject1, ((StringBuilder)localObject2).toString());
            allowCallsFrom = k;
            allowMessagesFrom = k;
          }
          else
          {
            allowCallsFrom = 2;
            allowMessagesFrom = 1;
          }
          allowAlarms = safeBoolean(paramXmlPullParser, "alarms", true);
          allowMedia = safeBoolean(paramXmlPullParser, "media", true);
          allowSystem = safeBoolean(paramXmlPullParser, "system", false);
          localObject1 = unsafeBoolean(paramXmlPullParser, "visualScreenOff");
          if (localObject1 != null)
          {
            j = 1;
            i = j;
            if (((Boolean)localObject1).booleanValue())
            {
              suppressedVisualEffects |= 0xC;
              i = j;
            }
          }
          localObject1 = unsafeBoolean(paramXmlPullParser, "visualScreenOn");
          if (localObject1 != null)
          {
            j = 1;
            i = j;
            if (((Boolean)localObject1).booleanValue())
            {
              suppressedVisualEffects |= 0x10;
              i = j;
            }
          }
          if (i != 0)
          {
            localObject2 = TAG;
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Migrated visual effects to ");
            ((StringBuilder)localObject1).append(suppressedVisualEffects);
            Slog.d((String)localObject2, ((StringBuilder)localObject1).toString());
          }
        }
        else if (("disallow".equals(localObject1)) && (i == 0))
        {
          suppressedVisualEffects = safeInt(paramXmlPullParser, "visualEffects", 0);
        }
        else if ("manual".equals(localObject1))
        {
          manualRule = readRuleXml(paramXmlPullParser);
          if ((manualRule != null) && (manualRule.zenMode != 1) && (manualRule.conditionId == null))
          {
            localObject2 = TAG;
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Updating zenMode of manual rule ");
            ((StringBuilder)localObject1).append(manualRule.name);
            Slog.i((String)localObject2, ((StringBuilder)localObject1).toString());
            manualRule.zenMode = 1;
          }
        }
        else if ("automatic".equals(localObject1))
        {
          localObject2 = paramXmlPullParser.getAttributeValue(null, "ruleId");
          localObject1 = readRuleXml(paramXmlPullParser);
          if ((localObject2 != null) && (localObject1 != null))
          {
            id = ((String)localObject2);
            automaticRules.put(localObject2, localObject1);
          }
        }
        else if ("state".equals(localObject1))
        {
          areChannelsBypassingDnd = safeBoolean(paramXmlPullParser, "areChannelsBypassingDnd", false);
        }
      }
    }
    throw new IllegalStateException("Failed to reach END_DOCUMENT");
  }
  
  private static boolean safeBoolean(String paramString, boolean paramBoolean)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramBoolean;
    }
    return Boolean.parseBoolean(paramString);
  }
  
  private static boolean safeBoolean(XmlPullParser paramXmlPullParser, String paramString, boolean paramBoolean)
  {
    return safeBoolean(paramXmlPullParser.getAttributeValue(null, paramString), paramBoolean);
  }
  
  private static ComponentName safeComponentName(XmlPullParser paramXmlPullParser, String paramString)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return null;
    }
    return ComponentName.unflattenFromString(paramXmlPullParser);
  }
  
  private static int safeInt(XmlPullParser paramXmlPullParser, String paramString, int paramInt)
  {
    return tryParseInt(paramXmlPullParser.getAttributeValue(null, paramString), paramInt);
  }
  
  private static long safeLong(XmlPullParser paramXmlPullParser, String paramString, long paramLong)
  {
    return tryParseLong(paramXmlPullParser.getAttributeValue(null, paramString), paramLong);
  }
  
  private static Uri safeUri(XmlPullParser paramXmlPullParser, String paramString)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return null;
    }
    return Uri.parse(paramXmlPullParser);
  }
  
  private static boolean sameCondition(ZenRule paramZenRule)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramZenRule == null) {
      return false;
    }
    if (conditionId == null)
    {
      if (condition == null) {
        bool2 = true;
      }
      return bool2;
    }
    if ((condition != null) && (!conditionId.equals(condition.id))) {
      bool2 = bool1;
    } else {
      bool2 = true;
    }
    return bool2;
  }
  
  private static int sourceToPrioritySenders(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return paramInt2;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  public static String sourceToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 2: 
      return "stars";
    case 1: 
      return "contacts";
    }
    return "anyone";
  }
  
  public static Uri toCountdownConditionId(long paramLong, boolean paramBoolean)
  {
    return new Uri.Builder().scheme("condition").authority("android").appendPath("countdown").appendPath(Long.toString(paramLong)).appendPath("alarm").appendPath(Boolean.toString(paramBoolean)).build();
  }
  
  private static String toDayList(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length != 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      for (int i = 0; i < paramArrayOfInt.length; i++)
      {
        if (i > 0) {
          localStringBuilder.append('.');
        }
        localStringBuilder.append(paramArrayOfInt[i]);
      }
      return localStringBuilder.toString();
    }
    return "";
  }
  
  public static Uri toEventConditionId(EventInfo paramEventInfo)
  {
    Uri.Builder localBuilder = new Uri.Builder().scheme("condition").authority("android").appendPath("event").appendQueryParameter("userId", Long.toString(userId));
    String str;
    if (calendar != null) {
      str = calendar;
    } else {
      str = "";
    }
    return localBuilder.appendQueryParameter("calendar", str).appendQueryParameter("reply", Integer.toString(reply)).build();
  }
  
  public static Condition toNextAlarmCondition(Context paramContext, long paramLong, int paramInt)
  {
    CharSequence localCharSequence = getFormattedTime(paramContext, paramLong, isToday(paramLong), paramInt);
    paramContext = paramContext.getResources().getString(17041325, new Object[] { localCharSequence });
    return new Condition(toCountdownConditionId(paramLong, true), "", paramContext, "", 0, 1, 1);
  }
  
  public static ScheduleCalendar toScheduleCalendar(Uri paramUri)
  {
    ScheduleInfo localScheduleInfo = tryParseScheduleConditionId(paramUri);
    if ((localScheduleInfo != null) && (days != null) && (days.length != 0))
    {
      paramUri = new ScheduleCalendar();
      paramUri.setSchedule(localScheduleInfo);
      paramUri.setTimeZone(TimeZone.getDefault());
      return paramUri;
    }
    return null;
  }
  
  public static Uri toScheduleConditionId(ScheduleInfo paramScheduleInfo)
  {
    Uri.Builder localBuilder = new Uri.Builder().scheme("condition").authority("android").appendPath("schedule").appendQueryParameter("days", toDayList(days));
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(startHour);
    localStringBuilder.append(".");
    localStringBuilder.append(startMinute);
    localBuilder = localBuilder.appendQueryParameter("start", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(endHour);
    localStringBuilder.append(".");
    localStringBuilder.append(endMinute);
    return localBuilder.appendQueryParameter("end", localStringBuilder.toString()).appendQueryParameter("exitAtAlarm", String.valueOf(exitAtAlarm)).build();
  }
  
  public static Condition toTimeCondition(Context paramContext, int paramInt1, int paramInt2)
  {
    return toTimeCondition(paramContext, paramInt1, paramInt2, false);
  }
  
  public static Condition toTimeCondition(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    long l1 = System.currentTimeMillis();
    long l2;
    if (paramInt1 == 0) {
      l2 = 10000L;
    } else {
      l2 = 60000 * paramInt1;
    }
    return toTimeCondition(paramContext, l1 + l2, paramInt1, paramInt2, paramBoolean);
  }
  
  public static Condition toTimeCondition(Context paramContext, long paramLong, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    CharSequence localCharSequence = getFormattedTime(paramContext, paramLong, isToday(paramLong), paramInt2);
    paramContext = paramContext.getResources();
    String str1;
    String str2;
    if (paramInt1 < 60)
    {
      if (paramBoolean) {
        paramInt2 = 18153506;
      } else {
        paramInt2 = 18153505;
      }
      str1 = paramContext.getQuantityString(paramInt2, paramInt1, new Object[] { Integer.valueOf(paramInt1), localCharSequence });
      if (paramBoolean) {
        paramInt2 = 18153504;
      } else {
        paramInt2 = 18153503;
      }
      str2 = paramContext.getQuantityString(paramInt2, paramInt1, new Object[] { Integer.valueOf(paramInt1), localCharSequence });
      paramContext = paramContext.getString(17041325, new Object[] { localCharSequence });
    }
    else if (paramInt1 < 1440)
    {
      paramInt2 = Math.round(paramInt1 / 60.0F);
      if (paramBoolean) {
        paramInt1 = 18153502;
      } else {
        paramInt1 = 18153501;
      }
      str1 = paramContext.getQuantityString(paramInt1, paramInt2, new Object[] { Integer.valueOf(paramInt2), localCharSequence });
      if (paramBoolean) {
        paramInt1 = 18153500;
      } else {
        paramInt1 = 18153499;
      }
      str2 = paramContext.getQuantityString(paramInt1, paramInt2, new Object[] { Integer.valueOf(paramInt2), localCharSequence });
      paramContext = paramContext.getString(17041325, new Object[] { localCharSequence });
    }
    else
    {
      str1 = paramContext.getString(17041325, new Object[] { localCharSequence });
      paramContext = str1;
      str2 = str1;
    }
    return new Condition(toCountdownConditionId(paramLong, false), str1, str2, paramContext, 0, 1, 1);
  }
  
  public static long tryParseCountdownConditionId(Uri paramUri)
  {
    if (!Condition.isValidId(paramUri, "android")) {
      return 0L;
    }
    if ((paramUri.getPathSegments().size() >= 2) && ("countdown".equals(paramUri.getPathSegments().get(0)))) {
      try
      {
        long l = Long.parseLong((String)paramUri.getPathSegments().get(1));
        return l;
      }
      catch (RuntimeException localRuntimeException)
      {
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error parsing countdown condition: ");
        localStringBuilder.append(paramUri);
        Slog.w(str, localStringBuilder.toString(), localRuntimeException);
        return 0L;
      }
    }
    return 0L;
  }
  
  private static int[] tryParseDayList(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return null;
    }
    paramString1 = paramString1.split(paramString2);
    if (paramString1.length == 0) {
      return null;
    }
    paramString2 = new int[paramString1.length];
    for (int i = 0; i < paramString1.length; i++)
    {
      int j = tryParseInt(paramString1[i], -1);
      if (j == -1) {
        return null;
      }
      paramString2[i] = j;
    }
    return paramString2;
  }
  
  public static EventInfo tryParseEventConditionId(Uri paramUri)
  {
    int i = 1;
    if ((paramUri == null) || (!paramUri.getScheme().equals("condition")) || (!paramUri.getAuthority().equals("android")) || (paramUri.getPathSegments().size() != 1) || (!((String)paramUri.getPathSegments().get(0)).equals("event"))) {
      i = 0;
    }
    if (i == 0) {
      return null;
    }
    EventInfo localEventInfo = new EventInfo();
    userId = tryParseInt(paramUri.getQueryParameter("userId"), 55536);
    calendar = paramUri.getQueryParameter("calendar");
    if ((TextUtils.isEmpty(calendar)) || (tryParseLong(calendar, -1L) != -1L)) {
      calendar = null;
    }
    reply = tryParseInt(paramUri.getQueryParameter("reply"), 0);
    return localEventInfo;
  }
  
  private static int[] tryParseHourAndMinute(String paramString)
  {
    boolean bool = TextUtils.isEmpty(paramString);
    Object localObject = null;
    if (bool) {
      return null;
    }
    int i = paramString.indexOf('.');
    if ((i >= 1) && (i < paramString.length() - 1))
    {
      int j = tryParseInt(paramString.substring(0, i), -1);
      i = tryParseInt(paramString.substring(i + 1), -1);
      paramString = localObject;
      if (isValidHour(j))
      {
        paramString = localObject;
        if (isValidMinute(i))
        {
          paramString = new int[2];
          paramString[0] = j;
          paramString[1] = i;
        }
      }
      return paramString;
    }
    return null;
  }
  
  private static int tryParseInt(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramInt;
    }
    try
    {
      int i = Integer.parseInt(paramString);
      return i;
    }
    catch (NumberFormatException paramString) {}
    return paramInt;
  }
  
  private static long tryParseLong(String paramString, long paramLong)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramLong;
    }
    try
    {
      long l = Long.parseLong(paramString);
      return l;
    }
    catch (NumberFormatException paramString) {}
    return paramLong;
  }
  
  public static ScheduleInfo tryParseScheduleConditionId(Uri paramUri)
  {
    int i;
    if ((paramUri != null) && (paramUri.getScheme().equals("condition")) && (paramUri.getAuthority().equals("android")) && (paramUri.getPathSegments().size() == 1) && (((String)paramUri.getPathSegments().get(0)).equals("schedule"))) {
      i = 1;
    } else {
      i = 0;
    }
    if (i == 0) {
      return null;
    }
    int[] arrayOfInt1 = tryParseHourAndMinute(paramUri.getQueryParameter("start"));
    int[] arrayOfInt2 = tryParseHourAndMinute(paramUri.getQueryParameter("end"));
    if ((arrayOfInt1 != null) && (arrayOfInt2 != null))
    {
      ScheduleInfo localScheduleInfo = new ScheduleInfo();
      days = tryParseDayList(paramUri.getQueryParameter("days"), "\\.");
      startHour = arrayOfInt1[0];
      startMinute = arrayOfInt1[1];
      endHour = arrayOfInt2[0];
      endMinute = arrayOfInt2[1];
      exitAtAlarm = safeBoolean(paramUri.getQueryParameter("exitAtAlarm"), false);
      return localScheduleInfo;
    }
    return null;
  }
  
  private static int tryParseZenMode(String paramString, int paramInt)
  {
    int i = tryParseInt(paramString, paramInt);
    if (Settings.Global.isValidZenMode(i)) {
      paramInt = i;
    }
    return paramInt;
  }
  
  private static Boolean unsafeBoolean(XmlPullParser paramXmlPullParser, String paramString)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return null;
    }
    return Boolean.valueOf(Boolean.parseBoolean(paramXmlPullParser));
  }
  
  public static void writeConditionXml(Condition paramCondition, XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.attribute(null, "id", id.toString());
    paramXmlSerializer.attribute(null, "summary", summary);
    paramXmlSerializer.attribute(null, "line1", line1);
    paramXmlSerializer.attribute(null, "line2", line2);
    paramXmlSerializer.attribute(null, "icon", Integer.toString(icon));
    paramXmlSerializer.attribute(null, "state", Integer.toString(state));
    paramXmlSerializer.attribute(null, "flags", Integer.toString(flags));
  }
  
  public static void writeRuleXml(ZenRule paramZenRule, XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.attribute(null, "enabled", Boolean.toString(enabled));
    paramXmlSerializer.attribute(null, "snoozing", Boolean.toString(snoozing));
    if (name != null) {
      paramXmlSerializer.attribute(null, "name", name);
    }
    paramXmlSerializer.attribute(null, "zen", Integer.toString(zenMode));
    if (component != null) {
      paramXmlSerializer.attribute(null, "component", component.flattenToString());
    }
    if (conditionId != null) {
      paramXmlSerializer.attribute(null, "conditionId", conditionId.toString());
    }
    paramXmlSerializer.attribute(null, "creationTime", Long.toString(creationTime));
    if (enabler != null) {
      paramXmlSerializer.attribute(null, "enabler", enabler);
    }
    if (condition != null) {
      writeConditionXml(condition, paramXmlSerializer);
    }
  }
  
  public void applyNotificationPolicy(NotificationManager.Policy paramPolicy)
  {
    if (paramPolicy == null) {
      return;
    }
    int i = priorityCategories;
    boolean bool1 = false;
    boolean bool2;
    if ((i & 0x20) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowAlarms = bool2;
    if ((priorityCategories & 0x40) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowMedia = bool2;
    if ((priorityCategories & 0x80) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowSystem = bool2;
    if ((priorityCategories & 0x2) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowEvents = bool2;
    if ((priorityCategories & 0x1) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowReminders = bool2;
    if ((priorityCategories & 0x8) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowCalls = bool2;
    if ((priorityCategories & 0x4) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowMessages = bool2;
    if ((priorityCategories & 0x10) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    allowRepeatCallers = bool2;
    allowCallsFrom = prioritySendersToSource(priorityCallSenders, allowCallsFrom);
    allowMessagesFrom = prioritySendersToSource(priorityMessageSenders, allowMessagesFrom);
    if (suppressedVisualEffects != -1) {
      suppressedVisualEffects = suppressedVisualEffects;
    }
    if (state != -1)
    {
      bool2 = bool1;
      if ((state & 0x1) != 0) {
        bool2 = true;
      }
      areChannelsBypassingDnd = bool2;
    }
  }
  
  public ZenModeConfig copy()
  {
    Parcel localParcel = Parcel.obtain();
    try
    {
      writeToParcel(localParcel, 0);
      localParcel.setDataPosition(0);
      ZenModeConfig localZenModeConfig = new ZenModeConfig(localParcel);
      return localZenModeConfig;
    }
    finally
    {
      localParcel.recycle();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ZenModeConfig)) {
      return false;
    }
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    paramObject = (ZenModeConfig)paramObject;
    if ((allowAlarms != allowAlarms) || (allowMedia != allowMedia) || (allowSystem != allowSystem) || (allowCalls != allowCalls) || (allowRepeatCallers != allowRepeatCallers) || (allowMessages != allowMessages) || (allowCallsFrom != allowCallsFrom) || (allowMessagesFrom != allowMessagesFrom) || (allowReminders != allowReminders) || (allowEvents != allowEvents) || (user != user) || (!Objects.equals(automaticRules, automaticRules)) || (!Objects.equals(manualRule, manualRule)) || (suppressedVisualEffects != suppressedVisualEffects) || (areChannelsBypassingDnd != areChannelsBypassingDnd)) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Boolean.valueOf(allowAlarms), Boolean.valueOf(allowMedia), Boolean.valueOf(allowSystem), Boolean.valueOf(allowCalls), Boolean.valueOf(allowRepeatCallers), Boolean.valueOf(allowMessages), Integer.valueOf(allowCallsFrom), Integer.valueOf(allowMessagesFrom), Boolean.valueOf(allowReminders), Boolean.valueOf(allowEvents), Integer.valueOf(user), automaticRules, manualRule, Integer.valueOf(suppressedVisualEffects), Boolean.valueOf(areChannelsBypassingDnd) });
  }
  
  public boolean isValid()
  {
    if (!isValidManualRule(manualRule)) {
      return false;
    }
    int i = automaticRules.size();
    for (int j = 0; j < i; j++) {
      if (!isValidAutomaticRule((ZenRule)automaticRules.valueAt(j))) {
        return false;
      }
    }
    return true;
  }
  
  public NotificationManager.Policy toNotificationPolicy()
  {
    int i = 0;
    if (allowCalls) {
      i = 0x0 | 0x8;
    }
    int j = i;
    if (allowMessages) {
      j = i | 0x4;
    }
    i = j;
    if (allowEvents) {
      i = j | 0x2;
    }
    j = i;
    if (allowReminders) {
      j = i | 0x1;
    }
    i = j;
    if (allowRepeatCallers) {
      i = j | 0x10;
    }
    j = i;
    if (allowAlarms) {
      j = i | 0x20;
    }
    i = j;
    if (allowMedia) {
      i = j | 0x40;
    }
    j = i;
    if (allowSystem) {
      j = i | 0x80;
    }
    int k = sourceToPrioritySenders(allowCallsFrom, 1);
    int m = sourceToPrioritySenders(allowMessagesFrom, 1);
    int n = suppressedVisualEffects;
    if (areChannelsBypassingDnd) {}
    for (i = 1;; i = 0) {
      break;
    }
    return new NotificationManager.Policy(j, k, m, n, i);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(ZenModeConfig.class.getSimpleName());
    localStringBuilder.append('[');
    localStringBuilder.append("user=");
    localStringBuilder.append(user);
    localStringBuilder.append(",allowAlarms=");
    localStringBuilder.append(allowAlarms);
    localStringBuilder.append(",allowMedia=");
    localStringBuilder.append(allowMedia);
    localStringBuilder.append(",allowSystem=");
    localStringBuilder.append(allowSystem);
    localStringBuilder.append(",allowReminders=");
    localStringBuilder.append(allowReminders);
    localStringBuilder.append(",allowEvents=");
    localStringBuilder.append(allowEvents);
    localStringBuilder.append(",allowCalls=");
    localStringBuilder.append(allowCalls);
    localStringBuilder.append(",allowRepeatCallers=");
    localStringBuilder.append(allowRepeatCallers);
    localStringBuilder.append(",allowMessages=");
    localStringBuilder.append(allowMessages);
    localStringBuilder.append(",allowCallsFrom=");
    localStringBuilder.append(sourceToString(allowCallsFrom));
    localStringBuilder.append(",allowMessagesFrom=");
    localStringBuilder.append(sourceToString(allowMessagesFrom));
    localStringBuilder.append(",suppressedVisualEffects=");
    localStringBuilder.append(suppressedVisualEffects);
    localStringBuilder.append(",areChannelsBypassingDnd=");
    localStringBuilder.append(areChannelsBypassingDnd);
    localStringBuilder.append(",automaticRules=");
    localStringBuilder.append(automaticRules);
    localStringBuilder.append(",manualRule=");
    localStringBuilder.append(manualRule);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(allowCalls);
    paramParcel.writeInt(allowRepeatCallers);
    paramParcel.writeInt(allowMessages);
    paramParcel.writeInt(allowReminders);
    paramParcel.writeInt(allowEvents);
    paramParcel.writeInt(allowCallsFrom);
    paramParcel.writeInt(allowMessagesFrom);
    paramParcel.writeInt(user);
    paramParcel.writeParcelable(manualRule, 0);
    if (!automaticRules.isEmpty())
    {
      int i = automaticRules.size();
      String[] arrayOfString = new String[i];
      ZenRule[] arrayOfZenRule = new ZenRule[i];
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        arrayOfString[paramInt] = ((String)automaticRules.keyAt(paramInt));
        arrayOfZenRule[paramInt] = ((ZenRule)automaticRules.valueAt(paramInt));
      }
      paramParcel.writeInt(i);
      paramParcel.writeStringArray(arrayOfString);
      paramParcel.writeTypedArray(arrayOfZenRule, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(allowAlarms);
    paramParcel.writeInt(allowMedia);
    paramParcel.writeInt(allowSystem);
    paramParcel.writeInt(suppressedVisualEffects);
    paramParcel.writeInt(areChannelsBypassingDnd);
  }
  
  public void writeXml(XmlSerializer paramXmlSerializer, Integer paramInteger)
    throws IOException
  {
    paramXmlSerializer.startTag(null, "zen");
    if (paramInteger == null) {}
    for (int i = 8;; i = paramInteger.intValue())
    {
      paramInteger = Integer.toString(i);
      break;
    }
    paramXmlSerializer.attribute(null, "version", paramInteger);
    paramXmlSerializer.attribute(null, "asus_version", Integer.toString(1));
    paramXmlSerializer.attribute(null, "user", Integer.toString(user));
    paramXmlSerializer.startTag(null, "allow");
    paramXmlSerializer.attribute(null, "calls", Boolean.toString(allowCalls));
    paramXmlSerializer.attribute(null, "repeatCallers", Boolean.toString(allowRepeatCallers));
    paramXmlSerializer.attribute(null, "messages", Boolean.toString(allowMessages));
    paramXmlSerializer.attribute(null, "reminders", Boolean.toString(allowReminders));
    paramXmlSerializer.attribute(null, "events", Boolean.toString(allowEvents));
    paramXmlSerializer.attribute(null, "callsFrom", Integer.toString(allowCallsFrom));
    paramXmlSerializer.attribute(null, "messagesFrom", Integer.toString(allowMessagesFrom));
    paramXmlSerializer.attribute(null, "alarms", Boolean.toString(allowAlarms));
    paramXmlSerializer.attribute(null, "media", Boolean.toString(allowMedia));
    paramXmlSerializer.attribute(null, "system", Boolean.toString(allowSystem));
    paramXmlSerializer.endTag(null, "allow");
    paramXmlSerializer.startTag(null, "disallow");
    paramXmlSerializer.attribute(null, "visualEffects", Integer.toString(suppressedVisualEffects));
    paramXmlSerializer.endTag(null, "disallow");
    if (manualRule != null)
    {
      paramXmlSerializer.startTag(null, "manual");
      writeRuleXml(manualRule, paramXmlSerializer);
      paramXmlSerializer.endTag(null, "manual");
    }
    int j = automaticRules.size();
    for (i = 0; i < j; i++)
    {
      paramInteger = (String)automaticRules.keyAt(i);
      ZenRule localZenRule = (ZenRule)automaticRules.valueAt(i);
      paramXmlSerializer.startTag(null, "automatic");
      paramXmlSerializer.attribute(null, "ruleId", paramInteger);
      writeRuleXml(localZenRule, paramXmlSerializer);
      paramXmlSerializer.endTag(null, "automatic");
    }
    paramXmlSerializer.startTag(null, "state");
    paramXmlSerializer.attribute(null, "areChannelsBypassingDnd", Boolean.toString(areChannelsBypassingDnd));
    paramXmlSerializer.endTag(null, "state");
    paramXmlSerializer.endTag(null, "zen");
  }
  
  public static class Diff
  {
    private final ArrayList<String> lines = new ArrayList();
    
    public Diff() {}
    
    private Diff addLine(String paramString1, String paramString2)
    {
      ArrayList localArrayList = lines;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append(":");
      localStringBuilder.append(paramString2);
      localArrayList.add(localStringBuilder.toString());
      return this;
    }
    
    public Diff addLine(String paramString, Object paramObject1, Object paramObject2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramObject1);
      localStringBuilder.append("->");
      localStringBuilder.append(paramObject2);
      return addLine(paramString, localStringBuilder.toString());
    }
    
    public Diff addLine(String paramString1, String paramString2, Object paramObject1, Object paramObject2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append(".");
      localStringBuilder.append(paramString2);
      return addLine(localStringBuilder.toString(), paramObject1, paramObject2);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("Diff[");
      int i = lines.size();
      for (int j = 0; j < i; j++)
      {
        if (j > 0) {
          localStringBuilder.append(',');
        }
        localStringBuilder.append((String)lines.get(j));
      }
      localStringBuilder.append(']');
      return localStringBuilder.toString();
    }
  }
  
  public static class EventInfo
  {
    public static final int REPLY_ANY_EXCEPT_NO = 0;
    public static final int REPLY_YES = 2;
    public static final int REPLY_YES_OR_MAYBE = 1;
    public String calendar;
    public int reply;
    public int userId = 55536;
    
    public EventInfo() {}
    
    public static int resolveUserId(int paramInt)
    {
      if (paramInt == 55536) {
        paramInt = ActivityManager.getCurrentUser();
      }
      return paramInt;
    }
    
    public EventInfo copy()
    {
      EventInfo localEventInfo = new EventInfo();
      userId = userId;
      calendar = calendar;
      reply = reply;
      return localEventInfo;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof EventInfo;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (EventInfo)paramObject;
      bool1 = bool2;
      if (userId == userId)
      {
        bool1 = bool2;
        if (Objects.equals(calendar, calendar))
        {
          bool1 = bool2;
          if (reply == reply) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    
    public int hashCode()
    {
      return 0;
    }
  }
  
  public static class ScheduleInfo
  {
    public int[] days;
    public int endHour;
    public int endMinute;
    public boolean exitAtAlarm;
    public long nextAlarm;
    public int startHour;
    public int startMinute;
    
    public ScheduleInfo() {}
    
    protected static String ts(long paramLong)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(new Date(paramLong));
      localStringBuilder.append(" (");
      localStringBuilder.append(paramLong);
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    }
    
    public ScheduleInfo copy()
    {
      ScheduleInfo localScheduleInfo = new ScheduleInfo();
      if (days != null)
      {
        days = new int[days.length];
        System.arraycopy(days, 0, days, 0, days.length);
      }
      startHour = startHour;
      startMinute = startMinute;
      endHour = endHour;
      endMinute = endMinute;
      exitAtAlarm = exitAtAlarm;
      nextAlarm = nextAlarm;
      return localScheduleInfo;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof ScheduleInfo;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (ScheduleInfo)paramObject;
      bool1 = bool2;
      if (ZenModeConfig.toDayList(days).equals(ZenModeConfig.toDayList(days)))
      {
        bool1 = bool2;
        if (startHour == startHour)
        {
          bool1 = bool2;
          if (startMinute == startMinute)
          {
            bool1 = bool2;
            if (endHour == endHour)
            {
              bool1 = bool2;
              if (endMinute == endMinute)
              {
                bool1 = bool2;
                if (exitAtAlarm == exitAtAlarm) {
                  bool1 = true;
                }
              }
            }
          }
        }
      }
      return bool1;
    }
    
    public int hashCode()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ScheduleInfo{days=");
      localStringBuilder.append(Arrays.toString(days));
      localStringBuilder.append(", startHour=");
      localStringBuilder.append(startHour);
      localStringBuilder.append(", startMinute=");
      localStringBuilder.append(startMinute);
      localStringBuilder.append(", endHour=");
      localStringBuilder.append(endHour);
      localStringBuilder.append(", endMinute=");
      localStringBuilder.append(endMinute);
      localStringBuilder.append(", exitAtAlarm=");
      localStringBuilder.append(exitAtAlarm);
      localStringBuilder.append(", nextAlarm=");
      localStringBuilder.append(ts(nextAlarm));
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
  
  public static class ZenRule
    implements Parcelable
  {
    public static final Parcelable.Creator<ZenRule> CREATOR = new Parcelable.Creator()
    {
      public ZenModeConfig.ZenRule createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ZenModeConfig.ZenRule(paramAnonymousParcel);
      }
      
      public ZenModeConfig.ZenRule[] newArray(int paramAnonymousInt)
      {
        return new ZenModeConfig.ZenRule[paramAnonymousInt];
      }
    };
    public ComponentName component;
    public Condition condition;
    public Uri conditionId;
    public long creationTime;
    public boolean enabled;
    public String enabler;
    public String id;
    public String name;
    public boolean snoozing;
    public int zenMode;
    
    public ZenRule() {}
    
    public ZenRule(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      boolean bool1 = false;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      enabled = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      }
      snoozing = bool2;
      if (paramParcel.readInt() == 1) {
        name = paramParcel.readString();
      }
      zenMode = paramParcel.readInt();
      conditionId = ((Uri)paramParcel.readParcelable(null));
      condition = ((Condition)paramParcel.readParcelable(null));
      component = ((ComponentName)paramParcel.readParcelable(null));
      if (paramParcel.readInt() == 1) {
        id = paramParcel.readString();
      }
      creationTime = paramParcel.readLong();
      if (paramParcel.readInt() == 1) {
        enabler = paramParcel.readString();
      }
    }
    
    private void appendDiff(ZenModeConfig.Diff paramDiff, String paramString, ZenRule paramZenRule)
    {
      if (paramZenRule == null)
      {
        paramDiff.addLine(paramString, "delete");
        return;
      }
      if (enabled != enabled) {
        paramDiff.addLine(paramString, "enabled", Boolean.valueOf(enabled), Boolean.valueOf(enabled));
      }
      if (snoozing != snoozing) {
        paramDiff.addLine(paramString, "snoozing", Boolean.valueOf(snoozing), Boolean.valueOf(snoozing));
      }
      if (!Objects.equals(name, name)) {
        paramDiff.addLine(paramString, "name", name, name);
      }
      if (zenMode != zenMode) {
        paramDiff.addLine(paramString, "zenMode", Integer.valueOf(zenMode), Integer.valueOf(zenMode));
      }
      if (!Objects.equals(conditionId, conditionId)) {
        paramDiff.addLine(paramString, "conditionId", conditionId, conditionId);
      }
      if (!Objects.equals(condition, condition)) {
        paramDiff.addLine(paramString, "condition", condition, condition);
      }
      if (!Objects.equals(component, component)) {
        paramDiff.addLine(paramString, "component", component, component);
      }
      if (!Objects.equals(id, id)) {
        paramDiff.addLine(paramString, "id", id, id);
      }
      if (creationTime != creationTime) {
        paramDiff.addLine(paramString, "creationTime", Long.valueOf(creationTime), Long.valueOf(creationTime));
      }
      if (enabler != enabler) {
        paramDiff.addLine(paramString, "enabler", enabler, enabler);
      }
    }
    
    private static void appendDiff(ZenModeConfig.Diff paramDiff, String paramString, ZenRule paramZenRule1, ZenRule paramZenRule2)
    {
      if (paramDiff == null) {
        return;
      }
      if (paramZenRule1 == null)
      {
        if (paramZenRule2 != null) {
          paramDiff.addLine(paramString, "insert");
        }
        return;
      }
      paramZenRule1.appendDiff(paramDiff, paramString, paramZenRule2);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof ZenRule)) {
        return false;
      }
      boolean bool = true;
      if (paramObject == this) {
        return true;
      }
      paramObject = (ZenRule)paramObject;
      if ((enabled != enabled) || (snoozing != snoozing) || (!Objects.equals(name, name)) || (zenMode != zenMode) || (!Objects.equals(conditionId, conditionId)) || (!Objects.equals(condition, condition)) || (!Objects.equals(component, component)) || (!Objects.equals(id, id)) || (creationTime != creationTime) || (!Objects.equals(enabler, enabler))) {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Boolean.valueOf(enabled), Boolean.valueOf(snoozing), name, Integer.valueOf(zenMode), conditionId, condition, component, id, Long.valueOf(creationTime), enabler });
    }
    
    public boolean isAutomaticActive()
    {
      boolean bool;
      if ((enabled) && (!snoozing) && (component != null) && (isTrueOrUnknown())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isTrueOrUnknown()
    {
      Condition localCondition = condition;
      boolean bool = true;
      if ((localCondition == null) || ((condition.state == 1) || (condition.state != 2))) {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(ZenRule.class.getSimpleName());
      localStringBuilder.append('[');
      localStringBuilder.append("enabled=");
      localStringBuilder.append(enabled);
      localStringBuilder.append(",snoozing=");
      localStringBuilder.append(snoozing);
      localStringBuilder.append(",name=");
      localStringBuilder.append(name);
      localStringBuilder.append(",zenMode=");
      localStringBuilder.append(Settings.Global.zenModeToString(zenMode));
      localStringBuilder.append(",conditionId=");
      localStringBuilder.append(conditionId);
      localStringBuilder.append(",condition=");
      localStringBuilder.append(condition);
      localStringBuilder.append(",component=");
      localStringBuilder.append(component);
      localStringBuilder.append(",id=");
      localStringBuilder.append(id);
      localStringBuilder.append(",creationTime=");
      localStringBuilder.append(creationTime);
      localStringBuilder.append(",enabler=");
      localStringBuilder.append(enabler);
      localStringBuilder.append(']');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(enabled);
      paramParcel.writeInt(snoozing);
      if (name != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString(name);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      paramParcel.writeInt(zenMode);
      paramParcel.writeParcelable(conditionId, 0);
      paramParcel.writeParcelable(condition, 0);
      paramParcel.writeParcelable(component, 0);
      if (id != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString(id);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      paramParcel.writeLong(creationTime);
      if (enabler != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString(enabler);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    
    public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
    {
      paramLong = paramProtoOutputStream.start(paramLong);
      paramProtoOutputStream.write(1138166333441L, id);
      paramProtoOutputStream.write(1138166333442L, name);
      paramProtoOutputStream.write(1112396529667L, creationTime);
      paramProtoOutputStream.write(1133871366148L, enabled);
      paramProtoOutputStream.write(1138166333445L, enabler);
      paramProtoOutputStream.write(1133871366150L, snoozing);
      paramProtoOutputStream.write(1159641169927L, zenMode);
      if (conditionId != null) {
        paramProtoOutputStream.write(1138166333448L, conditionId.toString());
      }
      if (condition != null) {
        condition.writeToProto(paramProtoOutputStream, 1146756268041L);
      }
      if (component != null) {
        component.writeToProto(paramProtoOutputStream, 1146756268042L);
      }
      paramProtoOutputStream.end(paramLong);
    }
  }
}
