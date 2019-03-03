package android.provider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorEntityIterator;
import android.content.Entity;
import android.content.EntityIterator;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.RemoteException;
import android.util.SeempLog;

public final class CalendarContract
{
  public static final String ACCOUNT_TYPE_LOCAL = "LOCAL";
  public static final String ACTION_EVENT_REMINDER = "android.intent.action.EVENT_REMINDER";
  public static final String ACTION_HANDLE_CUSTOM_EVENT = "android.provider.calendar.action.HANDLE_CUSTOM_EVENT";
  public static final String AUTHORITY = "com.android.calendar";
  public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";
  public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar");
  public static final String EXTRA_CUSTOM_APP_URI = "customAppUri";
  public static final String EXTRA_EVENT_ALL_DAY = "allDay";
  public static final String EXTRA_EVENT_BEGIN_TIME = "beginTime";
  public static final String EXTRA_EVENT_END_TIME = "endTime";
  private static final String TAG = "Calendar";
  
  private CalendarContract() {}
  
  public static final class Attendees
    implements BaseColumns, CalendarContract.AttendeesColumns, CalendarContract.EventsColumns
  {
    private static final String ATTENDEES_WHERE = "event_id=?";
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/attendees");
    
    private Attendees() {}
    
    public static final Cursor query(ContentResolver paramContentResolver, long paramLong, String[] paramArrayOfString)
    {
      SeempLog.record(54);
      String str = Long.toString(paramLong);
      return paramContentResolver.query(CONTENT_URI, paramArrayOfString, "event_id=?", new String[] { str }, null);
    }
  }
  
  protected static abstract interface AttendeesColumns
  {
    public static final String ATTENDEE_EMAIL = "attendeeEmail";
    public static final String ATTENDEE_IDENTITY = "attendeeIdentity";
    public static final String ATTENDEE_ID_NAMESPACE = "attendeeIdNamespace";
    public static final String ATTENDEE_NAME = "attendeeName";
    public static final String ATTENDEE_RELATIONSHIP = "attendeeRelationship";
    public static final String ATTENDEE_STATUS = "attendeeStatus";
    public static final int ATTENDEE_STATUS_ACCEPTED = 1;
    public static final int ATTENDEE_STATUS_DECLINED = 2;
    public static final int ATTENDEE_STATUS_INVITED = 3;
    public static final int ATTENDEE_STATUS_NONE = 0;
    public static final int ATTENDEE_STATUS_TENTATIVE = 4;
    public static final String ATTENDEE_TYPE = "attendeeType";
    public static final String EVENT_ID = "event_id";
    public static final int RELATIONSHIP_ATTENDEE = 1;
    public static final int RELATIONSHIP_NONE = 0;
    public static final int RELATIONSHIP_ORGANIZER = 2;
    public static final int RELATIONSHIP_PERFORMER = 3;
    public static final int RELATIONSHIP_SPEAKER = 4;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_OPTIONAL = 2;
    public static final int TYPE_REQUIRED = 1;
    public static final int TYPE_RESOURCE = 3;
  }
  
  public static final class CalendarAlerts
    implements BaseColumns, CalendarContract.CalendarAlertsColumns, CalendarContract.EventsColumns, CalendarContract.CalendarColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/calendar_alerts");
    public static final Uri CONTENT_URI_BY_INSTANCE = Uri.parse("content://com.android.calendar/calendar_alerts/by_instance");
    private static final boolean DEBUG = false;
    private static final String SORT_ORDER_ALARMTIME_ASC = "alarmTime ASC";
    public static final String TABLE_NAME = "CalendarAlerts";
    private static final String WHERE_ALARM_EXISTS = "event_id=? AND begin=? AND alarmTime=?";
    private static final String WHERE_FINDNEXTALARMTIME = "alarmTime>=?";
    private static final String WHERE_RESCHEDULE_MISSED_ALARMS = "state=0 AND alarmTime<? AND alarmTime>? AND end>=?";
    
    private CalendarAlerts() {}
    
    public static final boolean alarmExists(ContentResolver paramContentResolver, long paramLong1, long paramLong2, long paramLong3)
    {
      SeempLog.record(52);
      Uri localUri = CONTENT_URI;
      String str1 = Long.toString(paramLong1);
      Object localObject = Long.toString(paramLong2);
      String str2 = Long.toString(paramLong3);
      localObject = paramContentResolver.query(localUri, new String[] { "alarmTime" }, "event_id=? AND begin=? AND alarmTime=?", new String[] { str1, localObject, str2 }, null);
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (localObject != null) {}
      try
      {
        int i = ((Cursor)localObject).getCount();
        bool2 = bool1;
        if (i > 0) {
          bool2 = true;
        }
      }
      finally
      {
        if (localObject != null) {
          ((Cursor)localObject).close();
        }
      }
      return bool2;
    }
    
    public static final long findNextAlarmTime(ContentResolver paramContentResolver, long paramLong)
    {
      SeempLog.record(53);
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("alarmTime>=");
      ((StringBuilder)localObject).append(paramLong);
      ((StringBuilder)localObject).toString();
      localObject = CONTENT_URI;
      String str = Long.toString(paramLong);
      localObject = paramContentResolver.query((Uri)localObject, new String[] { "alarmTime" }, "alarmTime>=?", new String[] { str }, "alarmTime ASC");
      long l = -1L;
      paramLong = l;
      if (localObject != null) {
        paramLong = l;
      }
      try
      {
        if (((Cursor)localObject).moveToFirst()) {
          paramLong = ((Cursor)localObject).getLong(0);
        }
      }
      finally
      {
        if (localObject != null) {
          ((Cursor)localObject).close();
        }
      }
      return paramLong;
    }
    
    public static final Uri insert(ContentResolver paramContentResolver, long paramLong1, long paramLong2, long paramLong3, long paramLong4, int paramInt)
    {
      SeempLog.record(51);
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("event_id", Long.valueOf(paramLong1));
      localContentValues.put("begin", Long.valueOf(paramLong2));
      localContentValues.put("end", Long.valueOf(paramLong3));
      localContentValues.put("alarmTime", Long.valueOf(paramLong4));
      localContentValues.put("creationTime", Long.valueOf(System.currentTimeMillis()));
      localContentValues.put("receivedTime", Integer.valueOf(0));
      localContentValues.put("notifyTime", Integer.valueOf(0));
      localContentValues.put("state", Integer.valueOf(0));
      localContentValues.put("minutes", Integer.valueOf(paramInt));
      return paramContentResolver.insert(CONTENT_URI, localContentValues);
    }
    
    public static final void rescheduleMissedAlarms(ContentResolver paramContentResolver, Context paramContext, AlarmManager paramAlarmManager)
    {
      long l1 = System.currentTimeMillis();
      Uri localUri = CONTENT_URI;
      String str1 = Long.toString(l1);
      String str2 = Long.toString(l1 - 86400000L);
      String str3 = Long.toString(l1);
      paramContentResolver = paramContentResolver.query(localUri, new String[] { "alarmTime" }, "state=0 AND alarmTime<? AND alarmTime>? AND end>=?", new String[] { str1, str2, str3 }, "alarmTime ASC");
      if (paramContentResolver == null) {
        return;
      }
      long l2 = -1L;
      try
      {
        while (paramContentResolver.moveToNext())
        {
          long l3 = paramContentResolver.getLong(0);
          l1 = l2;
          if (l2 != l3)
          {
            scheduleAlarm(paramContext, paramAlarmManager, l3);
            l1 = l3;
          }
          l2 = l1;
        }
        return;
      }
      finally
      {
        paramContentResolver.close();
      }
    }
    
    public static void scheduleAlarm(Context paramContext, AlarmManager paramAlarmManager, long paramLong)
    {
      AlarmManager localAlarmManager = paramAlarmManager;
      if (paramAlarmManager == null) {
        localAlarmManager = (AlarmManager)paramContext.getSystemService("alarm");
      }
      paramAlarmManager = new Intent("android.intent.action.EVENT_REMINDER");
      paramAlarmManager.setData(ContentUris.withAppendedId(CalendarContract.CONTENT_URI, paramLong));
      paramAlarmManager.putExtra("alarmTime", paramLong);
      paramAlarmManager.setFlags(16777216);
      localAlarmManager.setExactAndAllowWhileIdle(0, paramLong, PendingIntent.getBroadcast(paramContext, 0, paramAlarmManager, 0));
    }
  }
  
  protected static abstract interface CalendarAlertsColumns
  {
    public static final String ALARM_TIME = "alarmTime";
    public static final String BEGIN = "begin";
    public static final String CREATION_TIME = "creationTime";
    public static final String DEFAULT_SORT_ORDER = "begin ASC,title ASC";
    public static final String END = "end";
    public static final String EVENT_ID = "event_id";
    public static final String MINUTES = "minutes";
    public static final String NOTIFY_TIME = "notifyTime";
    public static final String RECEIVED_TIME = "receivedTime";
    public static final String STATE = "state";
    public static final int STATE_DISMISSED = 2;
    public static final int STATE_FIRED = 1;
    public static final int STATE_SCHEDULED = 0;
  }
  
  public static final class CalendarCache
    implements CalendarContract.CalendarCacheColumns
  {
    public static final String KEY_TIMEZONE_INSTANCES = "timezoneInstances";
    public static final String KEY_TIMEZONE_INSTANCES_PREVIOUS = "timezoneInstancesPrevious";
    public static final String KEY_TIMEZONE_TYPE = "timezoneType";
    public static final String TIMEZONE_TYPE_AUTO = "auto";
    public static final String TIMEZONE_TYPE_HOME = "home";
    public static final Uri URI = Uri.parse("content://com.android.calendar/properties");
    
    private CalendarCache() {}
  }
  
  protected static abstract interface CalendarCacheColumns
  {
    public static final String KEY = "key";
    public static final String VALUE = "value";
  }
  
  protected static abstract interface CalendarColumns
  {
    public static final String ALLOWED_ATTENDEE_TYPES = "allowedAttendeeTypes";
    public static final String ALLOWED_AVAILABILITY = "allowedAvailability";
    public static final String ALLOWED_REMINDERS = "allowedReminders";
    public static final String CALENDAR_ACCESS_LEVEL = "calendar_access_level";
    public static final String CALENDAR_COLOR = "calendar_color";
    public static final String CALENDAR_COLOR_KEY = "calendar_color_index";
    public static final String CALENDAR_DISPLAY_NAME = "calendar_displayName";
    public static final String CALENDAR_TIME_ZONE = "calendar_timezone";
    public static final int CAL_ACCESS_CONTRIBUTOR = 500;
    public static final int CAL_ACCESS_EDITOR = 600;
    public static final int CAL_ACCESS_FREEBUSY = 100;
    public static final int CAL_ACCESS_NONE = 0;
    public static final int CAL_ACCESS_OVERRIDE = 400;
    public static final int CAL_ACCESS_OWNER = 700;
    public static final int CAL_ACCESS_READ = 200;
    public static final int CAL_ACCESS_RESPOND = 300;
    public static final int CAL_ACCESS_ROOT = 800;
    public static final String CAN_MODIFY_TIME_ZONE = "canModifyTimeZone";
    public static final String CAN_ORGANIZER_RESPOND = "canOrganizerRespond";
    public static final String IS_PRIMARY = "isPrimary";
    public static final String MAX_REMINDERS = "maxReminders";
    public static final String OWNER_ACCOUNT = "ownerAccount";
    public static final String SYNC_EVENTS = "sync_events";
    public static final String VISIBLE = "visible";
  }
  
  public static final class CalendarEntity
    implements BaseColumns, CalendarContract.SyncColumns, CalendarContract.CalendarColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/calendar_entities");
    
    private CalendarEntity() {}
    
    public static EntityIterator newEntityIterator(Cursor paramCursor)
    {
      return new EntityIteratorImpl(paramCursor);
    }
    
    private static class EntityIteratorImpl
      extends CursorEntityIterator
    {
      public EntityIteratorImpl(Cursor paramCursor)
      {
        super();
      }
      
      public Entity getEntityAndIncrementCursor(Cursor paramCursor)
        throws RemoteException
      {
        long l = paramCursor.getLong(paramCursor.getColumnIndexOrThrow("_id"));
        Object localObject = new ContentValues();
        ((ContentValues)localObject).put("_id", Long.valueOf(l));
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "account_name");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "account_type");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "_sync_id");
        DatabaseUtils.cursorLongToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "dirty");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "mutators");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync1");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync2");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync3");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync4");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync5");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync6");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync7");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync8");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync9");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "cal_sync10");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "name");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "calendar_displayName");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "calendar_color");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "calendar_color_index");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "calendar_access_level");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "visible");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "sync_events");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "calendar_location");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "calendar_timezone");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "ownerAccount");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "canOrganizerRespond");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "canModifyTimeZone");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "maxReminders");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "canPartiallyUpdate");
        DatabaseUtils.cursorStringToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "allowedReminders");
        DatabaseUtils.cursorIntToContentValuesIfPresent(paramCursor, (ContentValues)localObject, "deleted");
        localObject = new Entity((ContentValues)localObject);
        paramCursor.moveToNext();
        return localObject;
      }
    }
  }
  
  public static final class CalendarMetaData
    implements CalendarContract.CalendarMetaDataColumns, BaseColumns
  {
    private CalendarMetaData() {}
  }
  
  protected static abstract interface CalendarMetaDataColumns
  {
    public static final String LOCAL_TIMEZONE = "localTimezone";
    public static final String MAX_EVENTDAYS = "maxEventDays";
    public static final String MAX_INSTANCE = "maxInstance";
    public static final String MIN_EVENTDAYS = "minEventDays";
    public static final String MIN_INSTANCE = "minInstance";
  }
  
  protected static abstract interface CalendarSyncColumns
  {
    public static final String CAL_SYNC1 = "cal_sync1";
    public static final String CAL_SYNC10 = "cal_sync10";
    public static final String CAL_SYNC2 = "cal_sync2";
    public static final String CAL_SYNC3 = "cal_sync3";
    public static final String CAL_SYNC4 = "cal_sync4";
    public static final String CAL_SYNC5 = "cal_sync5";
    public static final String CAL_SYNC6 = "cal_sync6";
    public static final String CAL_SYNC7 = "cal_sync7";
    public static final String CAL_SYNC8 = "cal_sync8";
    public static final String CAL_SYNC9 = "cal_sync9";
  }
  
  public static final class Calendars
    implements BaseColumns, CalendarContract.SyncColumns, CalendarContract.CalendarColumns
  {
    public static final String CALENDAR_LOCATION = "calendar_location";
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/calendars");
    public static final String DEFAULT_SORT_ORDER = "calendar_displayName";
    public static final String NAME = "name";
    public static final String[] SYNC_WRITABLE_COLUMNS = { "account_name", "account_type", "_sync_id", "dirty", "mutators", "ownerAccount", "maxReminders", "allowedReminders", "canModifyTimeZone", "canOrganizerRespond", "canPartiallyUpdate", "calendar_location", "calendar_timezone", "calendar_access_level", "deleted", "cal_sync1", "cal_sync2", "cal_sync3", "cal_sync4", "cal_sync5", "cal_sync6", "cal_sync7", "cal_sync8", "cal_sync9", "cal_sync10" };
    
    private Calendars() {}
  }
  
  public static final class Colors
    implements CalendarContract.ColorsColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/colors");
    public static final String TABLE_NAME = "Colors";
    
    private Colors() {}
  }
  
  protected static abstract interface ColorsColumns
    extends SyncStateContract.Columns
  {
    public static final String COLOR = "color";
    public static final String COLOR_KEY = "color_index";
    public static final String COLOR_TYPE = "color_type";
    public static final int TYPE_CALENDAR = 0;
    public static final int TYPE_EVENT = 1;
  }
  
  public static final class EventDays
    implements CalendarContract.EventDaysColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/instances/groupbyday");
    private static final String SELECTION = "selected=1";
    
    private EventDays() {}
    
    public static final Cursor query(ContentResolver paramContentResolver, int paramInt1, int paramInt2, String[] paramArrayOfString)
    {
      SeempLog.record(54);
      if (paramInt2 < 1) {
        return null;
      }
      Uri.Builder localBuilder = CONTENT_URI.buildUpon();
      ContentUris.appendId(localBuilder, paramInt1);
      ContentUris.appendId(localBuilder, paramInt1 + paramInt2 - 1);
      return paramContentResolver.query(localBuilder.build(), paramArrayOfString, "selected=1", null, "startDay");
    }
  }
  
  protected static abstract interface EventDaysColumns
  {
    public static final String ENDDAY = "endDay";
    public static final String STARTDAY = "startDay";
  }
  
  public static final class Events
    implements BaseColumns, CalendarContract.SyncColumns, CalendarContract.EventsColumns, CalendarContract.CalendarColumns
  {
    public static final Uri CONTENT_EXCEPTION_URI = Uri.parse("content://com.android.calendar/exception");
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/events");
    private static final String DEFAULT_SORT_ORDER = "";
    public static String[] PROVIDER_WRITABLE_COLUMNS = { "account_name", "account_type", "cal_sync1", "cal_sync2", "cal_sync3", "cal_sync4", "cal_sync5", "cal_sync6", "cal_sync7", "cal_sync8", "cal_sync9", "cal_sync10", "allowedReminders", "allowedAttendeeTypes", "allowedAvailability", "calendar_access_level", "calendar_color", "calendar_timezone", "canModifyTimeZone", "canOrganizerRespond", "calendar_displayName", "canPartiallyUpdate", "sync_events", "visible" };
    public static final String[] SYNC_WRITABLE_COLUMNS = { "_sync_id", "dirty", "mutators", "sync_data1", "sync_data2", "sync_data3", "sync_data4", "sync_data5", "sync_data6", "sync_data7", "sync_data8", "sync_data9", "sync_data10" };
    
    private Events() {}
  }
  
  protected static abstract interface EventsColumns
  {
    public static final int ACCESS_CONFIDENTIAL = 1;
    public static final int ACCESS_DEFAULT = 0;
    public static final String ACCESS_LEVEL = "accessLevel";
    public static final int ACCESS_PRIVATE = 2;
    public static final int ACCESS_PUBLIC = 3;
    public static final String ALL_DAY = "allDay";
    public static final String AVAILABILITY = "availability";
    public static final int AVAILABILITY_BUSY = 0;
    public static final int AVAILABILITY_FREE = 1;
    public static final int AVAILABILITY_TENTATIVE = 2;
    public static final String CALENDAR_ID = "calendar_id";
    public static final String CAN_INVITE_OTHERS = "canInviteOthers";
    public static final String CUSTOM_APP_PACKAGE = "customAppPackage";
    public static final String CUSTOM_APP_URI = "customAppUri";
    public static final String DESCRIPTION = "description";
    public static final String DISPLAY_COLOR = "displayColor";
    public static final String DTEND = "dtend";
    public static final String DTSTART = "dtstart";
    public static final String DURATION = "duration";
    public static final String EVENT_COLOR = "eventColor";
    public static final String EVENT_COLOR_KEY = "eventColor_index";
    public static final String EVENT_END_TIMEZONE = "eventEndTimezone";
    public static final String EVENT_LOCATION = "eventLocation";
    public static final String EVENT_TIMEZONE = "eventTimezone";
    public static final String EXDATE = "exdate";
    public static final String EXRULE = "exrule";
    public static final String GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";
    public static final String GUESTS_CAN_MODIFY = "guestsCanModify";
    public static final String GUESTS_CAN_SEE_GUESTS = "guestsCanSeeGuests";
    public static final String HAS_ALARM = "hasAlarm";
    public static final String HAS_ATTENDEE_DATA = "hasAttendeeData";
    public static final String HAS_EXTENDED_PROPERTIES = "hasExtendedProperties";
    public static final String IS_ORGANIZER = "isOrganizer";
    public static final String LAST_DATE = "lastDate";
    public static final String LAST_SYNCED = "lastSynced";
    public static final String ORGANIZER = "organizer";
    public static final String ORIGINAL_ALL_DAY = "originalAllDay";
    public static final String ORIGINAL_ID = "original_id";
    public static final String ORIGINAL_INSTANCE_TIME = "originalInstanceTime";
    public static final String ORIGINAL_SYNC_ID = "original_sync_id";
    public static final String RDATE = "rdate";
    public static final String RRULE = "rrule";
    public static final String SELF_ATTENDEE_STATUS = "selfAttendeeStatus";
    public static final String STATUS = "eventStatus";
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_CONFIRMED = 1;
    public static final int STATUS_TENTATIVE = 0;
    public static final String SYNC_DATA1 = "sync_data1";
    public static final String SYNC_DATA10 = "sync_data10";
    public static final String SYNC_DATA2 = "sync_data2";
    public static final String SYNC_DATA3 = "sync_data3";
    public static final String SYNC_DATA4 = "sync_data4";
    public static final String SYNC_DATA5 = "sync_data5";
    public static final String SYNC_DATA6 = "sync_data6";
    public static final String SYNC_DATA7 = "sync_data7";
    public static final String SYNC_DATA8 = "sync_data8";
    public static final String SYNC_DATA9 = "sync_data9";
    public static final String TITLE = "title";
    public static final String UID_2445 = "uid2445";
  }
  
  public static final class EventsEntity
    implements BaseColumns, CalendarContract.SyncColumns, CalendarContract.EventsColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/event_entities");
    
    private EventsEntity() {}
    
    public static EntityIterator newEntityIterator(Cursor paramCursor, ContentProviderClient paramContentProviderClient)
    {
      return new EntityIteratorImpl(paramCursor, paramContentProviderClient);
    }
    
    public static EntityIterator newEntityIterator(Cursor paramCursor, ContentResolver paramContentResolver)
    {
      return new EntityIteratorImpl(paramCursor, paramContentResolver);
    }
    
    private static class EntityIteratorImpl
      extends CursorEntityIterator
    {
      private static final String[] ATTENDEES_PROJECTION = { "attendeeName", "attendeeEmail", "attendeeRelationship", "attendeeType", "attendeeStatus", "attendeeIdentity", "attendeeIdNamespace" };
      private static final int COLUMN_ATTENDEE_EMAIL = 1;
      private static final int COLUMN_ATTENDEE_IDENTITY = 5;
      private static final int COLUMN_ATTENDEE_ID_NAMESPACE = 6;
      private static final int COLUMN_ATTENDEE_NAME = 0;
      private static final int COLUMN_ATTENDEE_RELATIONSHIP = 2;
      private static final int COLUMN_ATTENDEE_STATUS = 4;
      private static final int COLUMN_ATTENDEE_TYPE = 3;
      private static final int COLUMN_ID = 0;
      private static final int COLUMN_METHOD = 1;
      private static final int COLUMN_MINUTES = 0;
      private static final int COLUMN_NAME = 1;
      private static final int COLUMN_VALUE = 2;
      private static final String[] EXTENDED_PROJECTION = { "_id", "name", "value" };
      private static final String[] REMINDERS_PROJECTION = { "minutes", "method" };
      private static final String WHERE_EVENT_ID = "event_id=?";
      private final ContentProviderClient mProvider;
      private final ContentResolver mResolver;
      
      public EntityIteratorImpl(Cursor paramCursor, ContentProviderClient paramContentProviderClient)
      {
        super();
        mResolver = null;
        mProvider = paramContentProviderClient;
      }
      
      public EntityIteratorImpl(Cursor paramCursor, ContentResolver paramContentResolver)
      {
        super();
        mResolver = paramContentResolver;
        mProvider = null;
      }
      
      /* Error */
      public Entity getEntityAndIncrementCursor(Cursor paramCursor)
        throws RemoteException
      {
        // Byte code:
        //   0: aload_1
        //   1: aload_1
        //   2: ldc 70
        //   4: invokeinterface 97 2 0
        //   9: invokeinterface 101 2 0
        //   14: lstore_2
        //   15: new 103	android/content/ContentValues
        //   18: dup
        //   19: invokespecial 105	android/content/ContentValues:<init>	()V
        //   22: astore 4
        //   24: aload 4
        //   26: ldc 70
        //   28: lload_2
        //   29: invokestatic 111	java/lang/Long:valueOf	(J)Ljava/lang/Long;
        //   32: invokevirtual 115	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
        //   35: aload_1
        //   36: aload 4
        //   38: ldc 117
        //   40: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   43: aload_1
        //   44: aload 4
        //   46: ldc 125
        //   48: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   51: aload_1
        //   52: aload 4
        //   54: ldc -126
        //   56: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   59: aload_1
        //   60: aload 4
        //   62: ldc -124
        //   64: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   67: aload_1
        //   68: aload 4
        //   70: ldc -122
        //   72: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   75: aload_1
        //   76: aload 4
        //   78: ldc -120
        //   80: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   83: aload_1
        //   84: aload 4
        //   86: ldc -118
        //   88: invokestatic 141	android/database/DatabaseUtils:cursorLongToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   91: aload_1
        //   92: aload 4
        //   94: ldc -113
        //   96: invokestatic 141	android/database/DatabaseUtils:cursorLongToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   99: aload_1
        //   100: aload 4
        //   102: ldc -111
        //   104: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   107: aload_1
        //   108: aload 4
        //   110: ldc -109
        //   112: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   115: aload_1
        //   116: aload 4
        //   118: ldc -107
        //   120: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   123: aload_1
        //   124: aload 4
        //   126: ldc -105
        //   128: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   131: aload_1
        //   132: aload 4
        //   134: ldc -103
        //   136: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   139: aload_1
        //   140: aload 4
        //   142: ldc -101
        //   144: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   147: aload_1
        //   148: aload 4
        //   150: ldc -99
        //   152: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   155: aload_1
        //   156: aload 4
        //   158: ldc -97
        //   160: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   163: aload_1
        //   164: aload 4
        //   166: ldc -95
        //   168: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   171: aload_1
        //   172: aload 4
        //   174: ldc -93
        //   176: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   179: aload_1
        //   180: aload 4
        //   182: ldc -91
        //   184: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   187: aload_1
        //   188: aload 4
        //   190: ldc -89
        //   192: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   195: aload_1
        //   196: aload 4
        //   198: ldc -87
        //   200: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   203: aload_1
        //   204: aload 4
        //   206: ldc -85
        //   208: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   211: aload_1
        //   212: aload 4
        //   214: ldc -83
        //   216: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   219: aload_1
        //   220: aload 4
        //   222: ldc -81
        //   224: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   227: aload_1
        //   228: aload 4
        //   230: ldc -79
        //   232: invokestatic 141	android/database/DatabaseUtils:cursorLongToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   235: aload_1
        //   236: aload 4
        //   238: ldc -77
        //   240: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   243: aload_1
        //   244: aload 4
        //   246: ldc -75
        //   248: invokestatic 141	android/database/DatabaseUtils:cursorLongToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   251: aload_1
        //   252: aload 4
        //   254: ldc -73
        //   256: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   259: aload_1
        //   260: aload 4
        //   262: ldc -71
        //   264: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   267: aload_1
        //   268: aload 4
        //   270: ldc -69
        //   272: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   275: aload_1
        //   276: aload 4
        //   278: ldc -67
        //   280: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   283: aload_1
        //   284: aload 4
        //   286: ldc -65
        //   288: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   291: aload_1
        //   292: aload 4
        //   294: ldc -63
        //   296: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   299: aload_1
        //   300: aload 4
        //   302: ldc -61
        //   304: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   307: aload_1
        //   308: aload 4
        //   310: ldc -59
        //   312: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   315: aload_1
        //   316: aload 4
        //   318: ldc -57
        //   320: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   323: aload_1
        //   324: aload 4
        //   326: ldc -55
        //   328: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   331: aload_1
        //   332: aload 4
        //   334: ldc -53
        //   336: invokestatic 141	android/database/DatabaseUtils:cursorLongToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   339: aload_1
        //   340: aload 4
        //   342: ldc -51
        //   344: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   347: aload_1
        //   348: aload 4
        //   350: ldc -49
        //   352: invokestatic 141	android/database/DatabaseUtils:cursorLongToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   355: aload_1
        //   356: aload 4
        //   358: ldc -47
        //   360: invokestatic 123	android/database/DatabaseUtils:cursorIntToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   363: aload_1
        //   364: aload 4
        //   366: ldc -45
        //   368: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   371: aload_1
        //   372: aload 4
        //   374: ldc -43
        //   376: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   379: aload_1
        //   380: aload 4
        //   382: ldc -41
        //   384: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   387: aload_1
        //   388: aload 4
        //   390: ldc -39
        //   392: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   395: aload_1
        //   396: aload 4
        //   398: ldc -37
        //   400: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   403: aload_1
        //   404: aload 4
        //   406: ldc -35
        //   408: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   411: aload_1
        //   412: aload 4
        //   414: ldc -33
        //   416: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   419: aload_1
        //   420: aload 4
        //   422: ldc -31
        //   424: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   427: aload_1
        //   428: aload 4
        //   430: ldc -29
        //   432: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   435: aload_1
        //   436: aload 4
        //   438: ldc -27
        //   440: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   443: aload_1
        //   444: aload 4
        //   446: ldc -25
        //   448: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   451: aload_1
        //   452: aload 4
        //   454: ldc -23
        //   456: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   459: aload_1
        //   460: aload 4
        //   462: ldc -21
        //   464: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   467: aload_1
        //   468: aload 4
        //   470: ldc -19
        //   472: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   475: aload_1
        //   476: aload 4
        //   478: ldc -17
        //   480: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   483: aload_1
        //   484: aload 4
        //   486: ldc -15
        //   488: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   491: aload_1
        //   492: aload 4
        //   494: ldc -13
        //   496: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   499: aload_1
        //   500: aload 4
        //   502: ldc -11
        //   504: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   507: aload_1
        //   508: aload 4
        //   510: ldc -9
        //   512: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   515: aload_1
        //   516: aload 4
        //   518: ldc -7
        //   520: invokestatic 128	android/database/DatabaseUtils:cursorStringToContentValuesIfPresent	(Landroid/database/Cursor;Landroid/content/ContentValues;Ljava/lang/String;)V
        //   523: new 251	android/content/Entity
        //   526: dup
        //   527: aload 4
        //   529: invokespecial 254	android/content/Entity:<init>	(Landroid/content/ContentValues;)V
        //   532: astore 5
        //   534: aload_0
        //   535: getfield 84	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mResolver	Landroid/content/ContentResolver;
        //   538: ifnull +35 -> 573
        //   541: aload_0
        //   542: getfield 84	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mResolver	Landroid/content/ContentResolver;
        //   545: getstatic 260	android/provider/CalendarContract$Reminders:CONTENT_URI	Landroid/net/Uri;
        //   548: getstatic 52	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:REMINDERS_PROJECTION	[Ljava/lang/String;
        //   551: ldc 38
        //   553: iconst_1
        //   554: anewarray 46	java/lang/String
        //   557: dup
        //   558: iconst_0
        //   559: lload_2
        //   560: invokestatic 264	java/lang/Long:toString	(J)Ljava/lang/String;
        //   563: aastore
        //   564: aconst_null
        //   565: invokevirtual 270	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   568: astore 4
        //   570: goto +32 -> 602
        //   573: aload_0
        //   574: getfield 86	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mProvider	Landroid/content/ContentProviderClient;
        //   577: getstatic 260	android/provider/CalendarContract$Reminders:CONTENT_URI	Landroid/net/Uri;
        //   580: getstatic 52	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:REMINDERS_PROJECTION	[Ljava/lang/String;
        //   583: ldc 38
        //   585: iconst_1
        //   586: anewarray 46	java/lang/String
        //   589: dup
        //   590: iconst_0
        //   591: lload_2
        //   592: invokestatic 264	java/lang/Long:toString	(J)Ljava/lang/String;
        //   595: aastore
        //   596: aconst_null
        //   597: invokevirtual 273	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   600: astore 4
        //   602: aload 4
        //   604: invokeinterface 277 1 0
        //   609: ifeq +62 -> 671
        //   612: new 103	android/content/ContentValues
        //   615: astore 6
        //   617: aload 6
        //   619: invokespecial 105	android/content/ContentValues:<init>	()V
        //   622: aload 6
        //   624: ldc 48
        //   626: aload 4
        //   628: iconst_0
        //   629: invokeinterface 281 2 0
        //   634: invokestatic 286	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
        //   637: invokevirtual 289	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
        //   640: aload 6
        //   642: ldc 50
        //   644: aload 4
        //   646: iconst_1
        //   647: invokeinterface 281 2 0
        //   652: invokestatic 286	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
        //   655: invokevirtual 289	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
        //   658: aload 5
        //   660: getstatic 260	android/provider/CalendarContract$Reminders:CONTENT_URI	Landroid/net/Uri;
        //   663: aload 6
        //   665: invokevirtual 293	android/content/Entity:addSubValue	(Landroid/net/Uri;Landroid/content/ContentValues;)V
        //   668: goto -66 -> 602
        //   671: aload 4
        //   673: invokeinterface 296 1 0
        //   678: aload_0
        //   679: getfield 84	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mResolver	Landroid/content/ContentResolver;
        //   682: ifnull +35 -> 717
        //   685: aload_0
        //   686: getfield 84	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mResolver	Landroid/content/ContentResolver;
        //   689: getstatic 299	android/provider/CalendarContract$Attendees:CONTENT_URI	Landroid/net/Uri;
        //   692: getstatic 68	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:ATTENDEES_PROJECTION	[Ljava/lang/String;
        //   695: ldc 38
        //   697: iconst_1
        //   698: anewarray 46	java/lang/String
        //   701: dup
        //   702: iconst_0
        //   703: lload_2
        //   704: invokestatic 264	java/lang/Long:toString	(J)Ljava/lang/String;
        //   707: aastore
        //   708: aconst_null
        //   709: invokevirtual 270	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   712: astore 4
        //   714: goto +35 -> 749
        //   717: aload_0
        //   718: getfield 86	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mProvider	Landroid/content/ContentProviderClient;
        //   721: getstatic 299	android/provider/CalendarContract$Attendees:CONTENT_URI	Landroid/net/Uri;
        //   724: getstatic 68	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:ATTENDEES_PROJECTION	[Ljava/lang/String;
        //   727: ldc 38
        //   729: iconst_1
        //   730: anewarray 46	java/lang/String
        //   733: dup
        //   734: iconst_0
        //   735: lload_2
        //   736: invokestatic 264	java/lang/Long:toString	(J)Ljava/lang/String;
        //   739: aastore
        //   740: aconst_null
        //   741: invokevirtual 273	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   744: astore 4
        //   746: goto -32 -> 714
        //   749: aload 4
        //   751: invokeinterface 277 1 0
        //   756: ifeq +141 -> 897
        //   759: new 103	android/content/ContentValues
        //   762: astore 6
        //   764: aload 6
        //   766: invokespecial 105	android/content/ContentValues:<init>	()V
        //   769: aload 6
        //   771: ldc 54
        //   773: aload 4
        //   775: iconst_0
        //   776: invokeinterface 303 2 0
        //   781: invokevirtual 306	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   784: aload 6
        //   786: ldc 56
        //   788: aload 4
        //   790: iconst_1
        //   791: invokeinterface 303 2 0
        //   796: invokevirtual 306	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   799: aload 6
        //   801: ldc 58
        //   803: aload 4
        //   805: iconst_2
        //   806: invokeinterface 281 2 0
        //   811: invokestatic 286	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
        //   814: invokevirtual 289	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
        //   817: aload 6
        //   819: ldc 60
        //   821: aload 4
        //   823: iconst_3
        //   824: invokeinterface 281 2 0
        //   829: invokestatic 286	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
        //   832: invokevirtual 289	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
        //   835: aload 6
        //   837: ldc 62
        //   839: aload 4
        //   841: iconst_4
        //   842: invokeinterface 281 2 0
        //   847: invokestatic 286	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
        //   850: invokevirtual 289	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
        //   853: aload 6
        //   855: ldc 64
        //   857: aload 4
        //   859: iconst_5
        //   860: invokeinterface 303 2 0
        //   865: invokevirtual 306	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   868: aload 6
        //   870: ldc 66
        //   872: aload 4
        //   874: bipush 6
        //   876: invokeinterface 303 2 0
        //   881: invokevirtual 306	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   884: aload 5
        //   886: getstatic 299	android/provider/CalendarContract$Attendees:CONTENT_URI	Landroid/net/Uri;
        //   889: aload 6
        //   891: invokevirtual 293	android/content/Entity:addSubValue	(Landroid/net/Uri;Landroid/content/ContentValues;)V
        //   894: goto -145 -> 749
        //   897: aload 4
        //   899: invokeinterface 296 1 0
        //   904: aload_0
        //   905: getfield 84	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mResolver	Landroid/content/ContentResolver;
        //   908: ifnull +35 -> 943
        //   911: aload_0
        //   912: getfield 84	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mResolver	Landroid/content/ContentResolver;
        //   915: getstatic 309	android/provider/CalendarContract$ExtendedProperties:CONTENT_URI	Landroid/net/Uri;
        //   918: getstatic 76	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:EXTENDED_PROJECTION	[Ljava/lang/String;
        //   921: ldc 38
        //   923: iconst_1
        //   924: anewarray 46	java/lang/String
        //   927: dup
        //   928: iconst_0
        //   929: lload_2
        //   930: invokestatic 264	java/lang/Long:toString	(J)Ljava/lang/String;
        //   933: aastore
        //   934: aconst_null
        //   935: invokevirtual 270	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   938: astore 4
        //   940: goto +35 -> 975
        //   943: aload_0
        //   944: getfield 86	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:mProvider	Landroid/content/ContentProviderClient;
        //   947: getstatic 309	android/provider/CalendarContract$ExtendedProperties:CONTENT_URI	Landroid/net/Uri;
        //   950: getstatic 76	android/provider/CalendarContract$EventsEntity$EntityIteratorImpl:EXTENDED_PROJECTION	[Ljava/lang/String;
        //   953: ldc 38
        //   955: iconst_1
        //   956: anewarray 46	java/lang/String
        //   959: dup
        //   960: iconst_0
        //   961: lload_2
        //   962: invokestatic 264	java/lang/Long:toString	(J)Ljava/lang/String;
        //   965: aastore
        //   966: aconst_null
        //   967: invokevirtual 273	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   970: astore 4
        //   972: goto -32 -> 940
        //   975: aload 4
        //   977: invokeinterface 277 1 0
        //   982: ifeq +71 -> 1053
        //   985: new 103	android/content/ContentValues
        //   988: astore 6
        //   990: aload 6
        //   992: invokespecial 105	android/content/ContentValues:<init>	()V
        //   995: aload 6
        //   997: ldc 70
        //   999: aload 4
        //   1001: iconst_0
        //   1002: invokeinterface 303 2 0
        //   1007: invokevirtual 306	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   1010: aload 6
        //   1012: ldc 72
        //   1014: aload 4
        //   1016: iconst_1
        //   1017: invokeinterface 303 2 0
        //   1022: invokevirtual 306	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   1025: aload 6
        //   1027: ldc 74
        //   1029: aload 4
        //   1031: iconst_2
        //   1032: invokeinterface 303 2 0
        //   1037: invokevirtual 306	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   1040: aload 5
        //   1042: getstatic 309	android/provider/CalendarContract$ExtendedProperties:CONTENT_URI	Landroid/net/Uri;
        //   1045: aload 6
        //   1047: invokevirtual 293	android/content/Entity:addSubValue	(Landroid/net/Uri;Landroid/content/ContentValues;)V
        //   1050: goto -75 -> 975
        //   1053: aload 4
        //   1055: invokeinterface 296 1 0
        //   1060: aload_1
        //   1061: invokeinterface 277 1 0
        //   1066: pop
        //   1067: aload 5
        //   1069: areturn
        //   1070: astore_1
        //   1071: aload 4
        //   1073: invokeinterface 296 1 0
        //   1078: aload_1
        //   1079: athrow
        //   1080: astore_1
        //   1081: aload 4
        //   1083: invokeinterface 296 1 0
        //   1088: aload_1
        //   1089: athrow
        //   1090: astore_1
        //   1091: aload 4
        //   1093: invokeinterface 296 1 0
        //   1098: aload_1
        //   1099: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	1100	0	this	EntityIteratorImpl
        //   0	1100	1	paramCursor	Cursor
        //   14	948	2	l	long
        //   22	1070	4	localObject	Object
        //   532	536	5	localEntity	Entity
        //   615	431	6	localContentValues	ContentValues
        // Exception table:
        //   from	to	target	type
        //   975	1050	1070	finally
        //   749	894	1080	finally
        //   602	668	1090	finally
      }
    }
  }
  
  public static final class EventsRawTimes
    implements BaseColumns, CalendarContract.EventsRawTimesColumns
  {
    private EventsRawTimes() {}
  }
  
  protected static abstract interface EventsRawTimesColumns
  {
    public static final String DTEND_2445 = "dtend2445";
    public static final String DTSTART_2445 = "dtstart2445";
    public static final String EVENT_ID = "event_id";
    public static final String LAST_DATE_2445 = "lastDate2445";
    public static final String ORIGINAL_INSTANCE_TIME_2445 = "originalInstanceTime2445";
  }
  
  public static final class ExtendedProperties
    implements BaseColumns, CalendarContract.ExtendedPropertiesColumns, CalendarContract.EventsColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/extendedproperties");
    
    private ExtendedProperties() {}
  }
  
  protected static abstract interface ExtendedPropertiesColumns
  {
    public static final String EVENT_ID = "event_id";
    public static final String NAME = "name";
    public static final String VALUE = "value";
  }
  
  public static final class Instances
    implements BaseColumns, CalendarContract.EventsColumns, CalendarContract.CalendarColumns
  {
    public static final String BEGIN = "begin";
    public static final Uri CONTENT_BY_DAY_URI;
    public static final Uri CONTENT_SEARCH_BY_DAY_URI = Uri.parse("content://com.android.calendar/instances/searchbyday");
    public static final Uri CONTENT_SEARCH_URI;
    public static final Uri CONTENT_URI;
    private static final String DEFAULT_SORT_ORDER = "begin ASC";
    public static final String END = "end";
    public static final String END_DAY = "endDay";
    public static final String END_MINUTE = "endMinute";
    public static final String EVENT_ID = "event_id";
    public static final String START_DAY = "startDay";
    public static final String START_MINUTE = "startMinute";
    private static final String[] WHERE_CALENDARS_ARGS = { "1" };
    private static final String WHERE_CALENDARS_SELECTED = "visible=?";
    
    static
    {
      CONTENT_URI = Uri.parse("content://com.android.calendar/instances/when");
      CONTENT_BY_DAY_URI = Uri.parse("content://com.android.calendar/instances/whenbyday");
      CONTENT_SEARCH_URI = Uri.parse("content://com.android.calendar/instances/search");
    }
    
    private Instances() {}
    
    public static final Cursor query(ContentResolver paramContentResolver, String[] paramArrayOfString, long paramLong1, long paramLong2)
    {
      SeempLog.record(54);
      Uri.Builder localBuilder = CONTENT_URI.buildUpon();
      ContentUris.appendId(localBuilder, paramLong1);
      ContentUris.appendId(localBuilder, paramLong2);
      return paramContentResolver.query(localBuilder.build(), paramArrayOfString, "visible=?", WHERE_CALENDARS_ARGS, "begin ASC");
    }
    
    public static final Cursor query(ContentResolver paramContentResolver, String[] paramArrayOfString, long paramLong1, long paramLong2, String paramString)
    {
      SeempLog.record(54);
      Uri.Builder localBuilder = CONTENT_SEARCH_URI.buildUpon();
      ContentUris.appendId(localBuilder, paramLong1);
      ContentUris.appendId(localBuilder, paramLong2);
      return paramContentResolver.query(localBuilder.appendPath(paramString).build(), paramArrayOfString, "visible=?", WHERE_CALENDARS_ARGS, "begin ASC");
    }
  }
  
  public static final class Reminders
    implements BaseColumns, CalendarContract.RemindersColumns, CalendarContract.EventsColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/reminders");
    private static final String REMINDERS_WHERE = "event_id=?";
    
    private Reminders() {}
    
    public static final Cursor query(ContentResolver paramContentResolver, long paramLong, String[] paramArrayOfString)
    {
      SeempLog.record(54);
      String str = Long.toString(paramLong);
      return paramContentResolver.query(CONTENT_URI, paramArrayOfString, "event_id=?", new String[] { str }, null);
    }
  }
  
  protected static abstract interface RemindersColumns
  {
    public static final String EVENT_ID = "event_id";
    public static final String METHOD = "method";
    public static final int METHOD_ALARM = 4;
    public static final int METHOD_ALERT = 1;
    public static final int METHOD_DEFAULT = 0;
    public static final int METHOD_EMAIL = 2;
    public static final int METHOD_SMS = 3;
    public static final String MINUTES = "minutes";
    public static final int MINUTES_DEFAULT = -1;
  }
  
  protected static abstract interface SyncColumns
    extends CalendarContract.CalendarSyncColumns
  {
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String CAN_PARTIALLY_UPDATE = "canPartiallyUpdate";
    public static final String DELETED = "deleted";
    public static final String DIRTY = "dirty";
    public static final String MUTATORS = "mutators";
    public static final String _SYNC_ID = "_sync_id";
  }
  
  public static final class SyncState
    implements SyncStateContract.Columns
  {
    private static final String CONTENT_DIRECTORY = "syncstate";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CalendarContract.CONTENT_URI, "syncstate");
    
    private SyncState() {}
  }
}
