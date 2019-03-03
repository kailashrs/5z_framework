package android.app;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.PlayerBase;
import android.media.session.MediaSession.Token;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.BidiFormatter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.widget.RemoteViews;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.NotificationColorUtil;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class Notification
  implements Parcelable
{
  public static final AudioAttributes AUDIO_ATTRIBUTES_DEFAULT = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
  public static final int BADGE_ICON_LARGE = 2;
  public static final int BADGE_ICON_NONE = 0;
  public static final int BADGE_ICON_SMALL = 1;
  public static final String CATEGORY_ALARM = "alarm";
  public static final String CATEGORY_CALL = "call";
  @SystemApi
  public static final String CATEGORY_CAR_EMERGENCY = "car_emergency";
  @SystemApi
  public static final String CATEGORY_CAR_INFORMATION = "car_information";
  @SystemApi
  public static final String CATEGORY_CAR_WARNING = "car_warning";
  public static final String CATEGORY_EMAIL = "email";
  public static final String CATEGORY_ERROR = "err";
  public static final String CATEGORY_EVENT = "event";
  public static final String CATEGORY_MESSAGE = "msg";
  public static final String CATEGORY_NAVIGATION = "navigation";
  public static final String CATEGORY_PROGRESS = "progress";
  public static final String CATEGORY_PROMO = "promo";
  public static final String CATEGORY_RECOMMENDATION = "recommendation";
  public static final String CATEGORY_REMINDER = "reminder";
  public static final String CATEGORY_SERVICE = "service";
  public static final String CATEGORY_SOCIAL = "social";
  public static final String CATEGORY_STATUS = "status";
  public static final String CATEGORY_SYSTEM = "sys";
  public static final String CATEGORY_TRANSPORT = "transport";
  public static final String CATEGORY_URGENT = "urgent";
  public static final int COLOR_DEFAULT = 0;
  public static final int COLOR_INVALID = 1;
  public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator()
  {
    public Notification createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Notification(paramAnonymousParcel);
    }
    
    public Notification[] newArray(int paramAnonymousInt)
    {
      return new Notification[paramAnonymousInt];
    }
  };
  public static final int DEFAULT_ALL = -1;
  public static final int DEFAULT_LIGHTS = 4;
  public static final int DEFAULT_SOUND = 1;
  public static final int DEFAULT_VIBRATE = 2;
  @SystemApi
  public static final String EXTRA_ALLOW_DURING_SETUP = "android.allowDuringSetup";
  public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";
  public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
  public static final String EXTRA_BIG_TEXT = "android.bigText";
  public static final String EXTRA_BUILDER_APPLICATION_INFO = "android.appInfo";
  public static final String EXTRA_CHANNEL_GROUP_ID = "android.intent.extra.CHANNEL_GROUP_ID";
  public static final String EXTRA_CHANNEL_ID = "android.intent.extra.CHANNEL_ID";
  public static final String EXTRA_CHRONOMETER_COUNT_DOWN = "android.chronometerCountDown";
  public static final String EXTRA_COLORIZED = "android.colorized";
  public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
  public static final String EXTRA_CONTAINS_CUSTOM_VIEW = "android.contains.customView";
  public static final String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";
  public static final String EXTRA_FOREGROUND_APPS = "android.foregroundApps";
  public static final String EXTRA_HIDE_SMART_REPLIES = "android.hideSmartReplies";
  public static final String EXTRA_HISTORIC_MESSAGES = "android.messages.historic";
  public static final String EXTRA_INFO_TEXT = "android.infoText";
  public static final String EXTRA_IS_GROUP_CONVERSATION = "android.isGroupConversation";
  @Deprecated
  public static final String EXTRA_LARGE_ICON = "android.largeIcon";
  public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
  public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
  public static final String EXTRA_MESSAGES = "android.messages";
  public static final String EXTRA_MESSAGING_PERSON = "android.messagingUser";
  public static final String EXTRA_NOTIFICATION_ID = "android.intent.extra.NOTIFICATION_ID";
  public static final String EXTRA_NOTIFICATION_TAG = "android.intent.extra.NOTIFICATION_TAG";
  public static final String EXTRA_PEOPLE = "android.people";
  public static final String EXTRA_PEOPLE_LIST = "android.people.list";
  public static final String EXTRA_PICTURE = "android.picture";
  public static final String EXTRA_PROGRESS = "android.progress";
  public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
  public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
  public static final String EXTRA_REDUCED_IMAGES = "android.reduced.images";
  public static final String EXTRA_REMOTE_INPUT_DRAFT = "android.remoteInputDraft";
  public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";
  public static final String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";
  public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
  public static final String EXTRA_SHOW_REMOTE_INPUT_SPINNER = "android.remoteInputSpinner";
  public static final String EXTRA_SHOW_WHEN = "android.showWhen";
  @Deprecated
  public static final String EXTRA_SMALL_ICON = "android.icon";
  @SystemApi
  public static final String EXTRA_SUBSTITUTE_APP_NAME = "android.substName";
  public static final String EXTRA_SUB_TEXT = "android.subText";
  public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
  public static final String EXTRA_TEMPLATE = "android.template";
  public static final String EXTRA_TEXT = "android.text";
  public static final String EXTRA_TEXT_LINES = "android.textLines";
  public static final String EXTRA_TITLE = "android.title";
  public static final String EXTRA_TITLE_BIG = "android.title.big";
  @SystemApi
  public static final int FLAG_AUTOGROUP_SUMMARY = 1024;
  public static final int FLAG_AUTO_CANCEL = 16;
  public static final int FLAG_CAN_COLORIZE = 2048;
  public static final int FLAG_FOREGROUND_SERVICE = 64;
  public static final int FLAG_GROUP_SUMMARY = 512;
  @Deprecated
  public static final int FLAG_HIGH_PRIORITY = 128;
  public static final int FLAG_INSISTENT = 4;
  public static final int FLAG_LOCAL_ONLY = 256;
  public static final int FLAG_NO_CLEAR = 32;
  public static final int FLAG_ONGOING_EVENT = 2;
  public static final int FLAG_ONLY_ALERT_ONCE = 8;
  @Deprecated
  public static final int FLAG_SHOW_LIGHTS = 1;
  public static final int FLAG_URGENT = 268435456;
  public static final int GROUP_ALERT_ALL = 0;
  public static final int GROUP_ALERT_CHILDREN = 2;
  public static final int GROUP_ALERT_SUMMARY = 1;
  public static final String INTENT_CATEGORY_NOTIFICATION_PREFERENCES = "android.intent.category.NOTIFICATION_PREFERENCES";
  private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
  private static final int MAX_REPLY_HISTORY = 5;
  @Deprecated
  public static final int PRIORITY_DEFAULT = 0;
  @Deprecated
  public static final int PRIORITY_HIGH = 1;
  @Deprecated
  public static final int PRIORITY_LOW = -1;
  @Deprecated
  public static final int PRIORITY_MAX = 2;
  @Deprecated
  public static final int PRIORITY_MIN = -2;
  private static final ArraySet<Integer> STANDARD_LAYOUTS = new ArraySet();
  @Deprecated
  public static final int STREAM_DEFAULT = -1;
  private static final String TAG = "Notification";
  public static final int VISIBILITY_PRIVATE = 0;
  public static final int VISIBILITY_PUBLIC = 1;
  public static final int VISIBILITY_SECRET = -1;
  public static IBinder processWhitelistToken;
  public Action[] actions;
  public ArraySet<PendingIntent> allPendingIntents;
  @Deprecated
  public AudioAttributes audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
  @Deprecated
  public int audioStreamType = -1;
  @Deprecated
  public RemoteViews bigContentView;
  public String category;
  public int color = 0;
  public PendingIntent contentIntent;
  @Deprecated
  public RemoteViews contentView;
  private long creationTime;
  @Deprecated
  public int defaults;
  public PendingIntent deleteIntent;
  public Bundle extras = new Bundle();
  public int flags;
  public PendingIntent fullScreenIntent;
  @Deprecated
  public RemoteViews headsUpContentView;
  @Deprecated
  public int icon;
  public int iconLevel;
  @Deprecated
  public Bitmap largeIcon;
  @Deprecated
  public int ledARGB;
  @Deprecated
  public int ledOffMS;
  @Deprecated
  public int ledOnMS;
  private int mBadgeIcon = 0;
  private String mChannelId;
  private int mGroupAlertBehavior = 0;
  private String mGroupKey;
  private Icon mLargeIcon;
  private CharSequence mSettingsText;
  private String mShortcutId;
  private Icon mSmallIcon;
  private String mSortKey;
  private long mTimeout;
  private boolean mUsesStandardHeader;
  private IBinder mWhitelistToken;
  public int number = 0;
  @Deprecated
  public int priority;
  public Notification publicVersion;
  @Deprecated
  public Uri sound;
  public CharSequence tickerText;
  @Deprecated
  public RemoteViews tickerView;
  @Deprecated
  public long[] vibrate;
  public int visibility;
  public long when;
  
  static
  {
    STANDARD_LAYOUTS.add(Integer.valueOf(17367219));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367220));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367222));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367223));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367224));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367226));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367225));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367221));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367216));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367217));
    STANDARD_LAYOUTS.add(Integer.valueOf(17367218));
  }
  
  public Notification()
  {
    when = System.currentTimeMillis();
    creationTime = System.currentTimeMillis();
    priority = 0;
  }
  
  @Deprecated
  public Notification(int paramInt, CharSequence paramCharSequence, long paramLong)
  {
    icon = paramInt;
    tickerText = paramCharSequence;
    when = paramLong;
    creationTime = System.currentTimeMillis();
  }
  
  public Notification(Context paramContext, int paramInt, CharSequence paramCharSequence1, long paramLong, CharSequence paramCharSequence2, CharSequence paramCharSequence3, Intent paramIntent)
  {
    new Builder(paramContext).setWhen(paramLong).setSmallIcon(paramInt).setTicker(paramCharSequence1).setContentTitle(paramCharSequence2).setContentText(paramCharSequence3).setContentIntent(PendingIntent.getActivity(paramContext, 0, paramIntent, 0)).buildInto(this);
  }
  
  public Notification(Parcel paramParcel)
  {
    readFromParcelImpl(paramParcel);
    allPendingIntents = paramParcel.readArraySet(null);
  }
  
  public static void addFieldsFromContext(Context paramContext, Notification paramNotification)
  {
    addFieldsFromContext(paramContext.getApplicationInfo(), paramNotification);
  }
  
  public static void addFieldsFromContext(ApplicationInfo paramApplicationInfo, Notification paramNotification)
  {
    extras.putParcelable("android.appInfo", paramApplicationInfo);
  }
  
  public static boolean areActionsVisiblyDifferent(Notification paramNotification1, Notification paramNotification2)
  {
    Action[] arrayOfAction1 = actions;
    Action[] arrayOfAction2 = actions;
    if (((arrayOfAction1 == null) && (arrayOfAction2 != null)) || ((arrayOfAction1 != null) && (arrayOfAction2 == null))) {
      return true;
    }
    if ((arrayOfAction1 != null) && (arrayOfAction2 != null))
    {
      if (arrayOfAction1.length != arrayOfAction2.length) {
        return true;
      }
      for (int i = 0; i < arrayOfAction1.length; i++)
      {
        if (!Objects.equals(String.valueOf(title), String.valueOf(title))) {
          return true;
        }
        paramNotification2 = arrayOfAction1[i].getRemoteInputs();
        RemoteInput[] arrayOfRemoteInput = arrayOfAction2[i].getRemoteInputs();
        paramNotification1 = paramNotification2;
        if (paramNotification2 == null) {
          paramNotification1 = new RemoteInput[0];
        }
        paramNotification2 = arrayOfRemoteInput;
        if (arrayOfRemoteInput == null) {
          paramNotification2 = new RemoteInput[0];
        }
        if (paramNotification1.length != paramNotification2.length) {
          return true;
        }
        for (int j = 0; j < paramNotification1.length; j++) {
          if (!Objects.equals(String.valueOf(paramNotification1[j].getLabel()), String.valueOf(paramNotification2[j].getLabel()))) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public static boolean areRemoteViewsChanged(Builder paramBuilder1, Builder paramBuilder2)
  {
    if (!Objects.equals(Boolean.valueOf(paramBuilder1.usesStandardHeader()), Boolean.valueOf(paramBuilder2.usesStandardHeader()))) {
      return true;
    }
    if (areRemoteViewsChanged(mN.contentView, mN.contentView)) {
      return true;
    }
    if (areRemoteViewsChanged(mN.bigContentView, mN.bigContentView)) {
      return true;
    }
    return areRemoteViewsChanged(mN.headsUpContentView, mN.headsUpContentView);
  }
  
  private static boolean areRemoteViewsChanged(RemoteViews paramRemoteViews1, RemoteViews paramRemoteViews2)
  {
    if ((paramRemoteViews1 == null) && (paramRemoteViews2 == null)) {
      return false;
    }
    if (((paramRemoteViews1 == null) && (paramRemoteViews2 != null)) || ((paramRemoteViews1 != null) && (paramRemoteViews2 == null))) {
      return true;
    }
    if (!Objects.equals(Integer.valueOf(paramRemoteViews1.getLayoutId()), Integer.valueOf(paramRemoteViews2.getLayoutId()))) {
      return true;
    }
    return !Objects.equals(Integer.valueOf(paramRemoteViews1.getSequenceNumber()), Integer.valueOf(paramRemoteViews2.getSequenceNumber()));
  }
  
  public static boolean areStyledNotificationsVisiblyDifferent(Builder paramBuilder1, Builder paramBuilder2)
  {
    Style localStyle = paramBuilder1.getStyle();
    boolean bool = true;
    if (localStyle == null)
    {
      if (paramBuilder2.getStyle() == null) {
        bool = false;
      }
      return bool;
    }
    if (paramBuilder2.getStyle() == null) {
      return true;
    }
    return paramBuilder1.getStyle().areNotificationsVisiblyDifferent(paramBuilder2.getStyle());
  }
  
  private void fixDuplicateExtra(Parcelable paramParcelable, String paramString)
  {
    if ((paramParcelable != null) && (extras.getParcelable(paramString) != null)) {
      extras.putParcelable(paramString, paramParcelable);
    }
  }
  
  private void fixDuplicateExtras()
  {
    if (extras != null)
    {
      fixDuplicateExtra(mSmallIcon, "android.icon");
      fixDuplicateExtra(mLargeIcon, "android.largeIcon");
    }
  }
  
  private static Notification[] getNotificationArrayFromBundle(Bundle paramBundle, String paramString)
  {
    Object localObject = paramBundle.getParcelableArray(paramString);
    if ((!(localObject instanceof Notification[])) && (localObject != null))
    {
      localObject = (Notification[])Arrays.copyOf((Object[])localObject, localObject.length, [Landroid.app.Notification.class);
      paramBundle.putParcelableArray(paramString, (Parcelable[])localObject);
      return localObject;
    }
    return (Notification[])localObject;
  }
  
  @SystemApi
  public static Class<? extends Style> getNotificationStyleClass(String paramString)
  {
    Class[] arrayOfClass = new Class[7];
    int i = 0;
    arrayOfClass[0] = BigTextStyle.class;
    arrayOfClass[1] = BigPictureStyle.class;
    arrayOfClass[2] = InboxStyle.class;
    arrayOfClass[3] = MediaStyle.class;
    arrayOfClass[4] = DecoratedCustomViewStyle.class;
    arrayOfClass[5] = DecoratedMediaCustomViewStyle.class;
    arrayOfClass[6] = MessagingStyle.class;
    int j = arrayOfClass.length;
    while (i < j)
    {
      Class localClass = arrayOfClass[i];
      if (paramString.equals(localClass.getName())) {
        return localClass;
      }
      i++;
    }
    return null;
  }
  
  private boolean hasColorizedPermission()
  {
    boolean bool;
    if ((flags & 0x800) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean hasLargeIcon()
  {
    boolean bool;
    if ((mLargeIcon == null) && (largeIcon == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isForegroundService()
  {
    boolean bool;
    if ((flags & 0x40) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static String priorityToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UNKNOWN(");
      localStringBuilder.append(String.valueOf(paramInt));
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    case 2: 
      return "MAX";
    case 1: 
      return "HIGH";
    case 0: 
      return "DEFAULT";
    case -1: 
      return "LOW";
    }
    return "MIN";
  }
  
  private void readFromParcelImpl(Parcel paramParcel)
  {
    paramParcel.readInt();
    mWhitelistToken = paramParcel.readStrongBinder();
    if (mWhitelistToken == null) {
      mWhitelistToken = processWhitelistToken;
    }
    paramParcel.setClassCookie(PendingIntent.class, mWhitelistToken);
    when = paramParcel.readLong();
    creationTime = paramParcel.readLong();
    if (paramParcel.readInt() != 0)
    {
      mSmallIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
      if (mSmallIcon.getType() == 2) {
        icon = mSmallIcon.getResId();
      }
    }
    number = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      contentIntent = ((PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      deleteIntent = ((PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      tickerText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      tickerView = ((RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      contentView = ((RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      mLargeIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
    }
    defaults = paramParcel.readInt();
    flags = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      sound = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    }
    audioStreamType = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      audioAttributes = ((AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel));
    }
    vibrate = paramParcel.createLongArray();
    ledARGB = paramParcel.readInt();
    ledOnMS = paramParcel.readInt();
    ledOffMS = paramParcel.readInt();
    iconLevel = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      fullScreenIntent = ((PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel));
    }
    priority = paramParcel.readInt();
    category = paramParcel.readString();
    mGroupKey = paramParcel.readString();
    mSortKey = paramParcel.readString();
    extras = Bundle.setDefusable(paramParcel.readBundle(), true);
    fixDuplicateExtras();
    actions = ((Action[])paramParcel.createTypedArray(Action.CREATOR));
    if (paramParcel.readInt() != 0) {
      bigContentView = ((RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      headsUpContentView = ((RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel));
    }
    visibility = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      publicVersion = ((Notification)CREATOR.createFromParcel(paramParcel));
    }
    color = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      mChannelId = paramParcel.readString();
    }
    mTimeout = paramParcel.readLong();
    if (paramParcel.readInt() != 0) {
      mShortcutId = paramParcel.readString();
    }
    mBadgeIcon = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      mSettingsText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    }
    mGroupAlertBehavior = paramParcel.readInt();
  }
  
  private void reduceImageSizesForRemoteView(RemoteViews paramRemoteViews, Context paramContext, boolean paramBoolean)
  {
    if (paramRemoteViews != null)
    {
      paramContext = paramContext.getResources();
      int i;
      if (paramBoolean) {
        i = 17105323;
      } else {
        i = 17105322;
      }
      int j = paramContext.getDimensionPixelSize(i);
      if (paramBoolean) {
        i = 17105321;
      } else {
        i = 17105320;
      }
      paramRemoteViews.reduceImageSizes(j, paramContext.getDimensionPixelSize(i));
    }
  }
  
  private static CharSequence removeTextSizeSpans(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof Spanned))
    {
      Spanned localSpanned = (Spanned)paramCharSequence;
      int i = localSpanned.length();
      int j = 0;
      Object[] arrayOfObject = localSpanned.getSpans(0, i, Object.class);
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(localSpanned.toString());
      i = arrayOfObject.length;
      while (j < i)
      {
        paramCharSequence = arrayOfObject[j];
        CharSequence localCharSequence = paramCharSequence;
        Object localObject = localCharSequence;
        if ((localCharSequence instanceof CharacterStyle)) {
          localObject = ((CharacterStyle)paramCharSequence).getUnderlying();
        }
        if ((localObject instanceof TextAppearanceSpan))
        {
          localObject = (TextAppearanceSpan)localObject;
          localObject = new TextAppearanceSpan(((TextAppearanceSpan)localObject).getFamily(), ((TextAppearanceSpan)localObject).getTextStyle(), -1, ((TextAppearanceSpan)localObject).getTextColor(), ((TextAppearanceSpan)localObject).getLinkTextColor());
        }
        else
        {
          if (((localObject instanceof RelativeSizeSpan)) || ((localObject instanceof AbsoluteSizeSpan))) {
            break label182;
          }
          localObject = paramCharSequence;
        }
        localSpannableStringBuilder.setSpan(localObject, localSpanned.getSpanStart(paramCharSequence), localSpanned.getSpanEnd(paramCharSequence), localSpanned.getSpanFlags(paramCharSequence));
        label182:
        j++;
      }
      return localSpannableStringBuilder;
    }
    return paramCharSequence;
  }
  
  public static CharSequence safeCharSequence(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {
      return paramCharSequence;
    }
    CharSequence localCharSequence = paramCharSequence;
    if (paramCharSequence.length() > 5120) {
      localCharSequence = paramCharSequence.subSequence(0, 5120);
    }
    if ((localCharSequence instanceof Parcelable))
    {
      paramCharSequence = new StringBuilder();
      paramCharSequence.append("warning: ");
      paramCharSequence.append(localCharSequence.getClass().getCanonicalName());
      paramCharSequence.append(" instance is a custom Parcelable and not allowed in Notification");
      Log.e("Notification", paramCharSequence.toString());
      return localCharSequence.toString();
    }
    return removeTextSizeSpans(localCharSequence);
  }
  
  public static String visibilityToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UNKNOWN(");
      localStringBuilder.append(String.valueOf(paramInt));
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    case 1: 
      return "PUBLIC";
    case 0: 
      return "PRIVATE";
    }
    return "SECRET";
  }
  
  private void writeToParcelImpl(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(1);
    paramParcel.writeStrongBinder(mWhitelistToken);
    paramParcel.writeLong(when);
    paramParcel.writeLong(creationTime);
    if ((mSmallIcon == null) && (icon != 0)) {
      mSmallIcon = Icon.createWithResource("", icon);
    }
    if (mSmallIcon != null)
    {
      paramParcel.writeInt(1);
      mSmallIcon.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(number);
    if (contentIntent != null)
    {
      paramParcel.writeInt(1);
      contentIntent.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (deleteIntent != null)
    {
      paramParcel.writeInt(1);
      deleteIntent.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (tickerText != null)
    {
      paramParcel.writeInt(1);
      TextUtils.writeToParcel(tickerText, paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (tickerView != null)
    {
      paramParcel.writeInt(1);
      tickerView.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (contentView != null)
    {
      paramParcel.writeInt(1);
      contentView.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if ((mLargeIcon == null) && (largeIcon != null)) {
      mLargeIcon = Icon.createWithBitmap(largeIcon);
    }
    if (mLargeIcon != null)
    {
      paramParcel.writeInt(1);
      mLargeIcon.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(defaults);
    paramParcel.writeInt(flags);
    if (sound != null)
    {
      paramParcel.writeInt(1);
      sound.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(audioStreamType);
    if (audioAttributes != null)
    {
      paramParcel.writeInt(1);
      audioAttributes.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeLongArray(vibrate);
    paramParcel.writeInt(ledARGB);
    paramParcel.writeInt(ledOnMS);
    paramParcel.writeInt(ledOffMS);
    paramParcel.writeInt(iconLevel);
    if (fullScreenIntent != null)
    {
      paramParcel.writeInt(1);
      fullScreenIntent.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(priority);
    paramParcel.writeString(category);
    paramParcel.writeString(mGroupKey);
    paramParcel.writeString(mSortKey);
    paramParcel.writeBundle(extras);
    paramParcel.writeTypedArray(actions, 0);
    if (bigContentView != null)
    {
      paramParcel.writeInt(1);
      bigContentView.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (headsUpContentView != null)
    {
      paramParcel.writeInt(1);
      headsUpContentView.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(visibility);
    if (publicVersion != null)
    {
      paramParcel.writeInt(1);
      publicVersion.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(color);
    if (mChannelId != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(mChannelId);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeLong(mTimeout);
    if (mShortcutId != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(mShortcutId);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mBadgeIcon);
    if (mSettingsText != null)
    {
      paramParcel.writeInt(1);
      TextUtils.writeToParcel(mSettingsText, paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mGroupAlertBehavior);
  }
  
  public Notification clone()
  {
    Notification localNotification = new Notification();
    cloneInto(localNotification, true);
    return localNotification;
  }
  
  public void cloneInto(Notification paramNotification, boolean paramBoolean)
  {
    mWhitelistToken = mWhitelistToken;
    when = when;
    creationTime = creationTime;
    mSmallIcon = mSmallIcon;
    number = number;
    contentIntent = contentIntent;
    deleteIntent = deleteIntent;
    fullScreenIntent = fullScreenIntent;
    if (tickerText != null) {
      tickerText = tickerText.toString();
    }
    if ((paramBoolean) && (tickerView != null)) {
      tickerView = tickerView.clone();
    }
    if ((paramBoolean) && (contentView != null)) {
      contentView = contentView.clone();
    }
    if ((paramBoolean) && (mLargeIcon != null)) {
      mLargeIcon = mLargeIcon;
    }
    iconLevel = iconLevel;
    sound = sound;
    audioStreamType = audioStreamType;
    if (audioAttributes != null) {
      audioAttributes = new AudioAttributes.Builder(audioAttributes).build();
    }
    long[] arrayOfLong = vibrate;
    int i = 0;
    Object localObject;
    if (arrayOfLong != null)
    {
      int j = arrayOfLong.length;
      localObject = new long[j];
      vibrate = ((long[])localObject);
      System.arraycopy(arrayOfLong, 0, localObject, 0, j);
    }
    ledARGB = ledARGB;
    ledOnMS = ledOnMS;
    ledOffMS = ledOffMS;
    defaults = defaults;
    flags = flags;
    priority = priority;
    category = category;
    mGroupKey = mGroupKey;
    mSortKey = mSortKey;
    if (extras != null) {
      try
      {
        localObject = new android/os/Bundle;
        ((Bundle)localObject).<init>(extras);
        extras = ((Bundle)localObject);
        extras.size();
      }
      catch (BadParcelableException localBadParcelableException)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("could not unparcel extras from notification: ");
        ((StringBuilder)localObject).append(this);
        Log.e("Notification", ((StringBuilder)localObject).toString(), localBadParcelableException);
        extras = null;
      }
    }
    if (!ArrayUtils.isEmpty(allPendingIntents)) {
      allPendingIntents = new ArraySet(allPendingIntents);
    }
    if (actions != null)
    {
      actions = new Action[actions.length];
      while (i < actions.length)
      {
        if (actions[i] != null) {
          actions[i] = actions[i].clone();
        }
        i++;
      }
    }
    if ((paramBoolean) && (bigContentView != null)) {
      bigContentView = bigContentView.clone();
    }
    if ((paramBoolean) && (headsUpContentView != null)) {
      headsUpContentView = headsUpContentView.clone();
    }
    visibility = visibility;
    if (publicVersion != null)
    {
      publicVersion = new Notification();
      publicVersion.cloneInto(publicVersion, paramBoolean);
    }
    color = color;
    mChannelId = mChannelId;
    mTimeout = mTimeout;
    mShortcutId = mShortcutId;
    mBadgeIcon = mBadgeIcon;
    mSettingsText = mSettingsText;
    mGroupAlertBehavior = mGroupAlertBehavior;
    if (!paramBoolean) {
      paramNotification.lightenPayload();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getBadgeIconType()
  {
    return mBadgeIcon;
  }
  
  @Deprecated
  public String getChannel()
  {
    return mChannelId;
  }
  
  public String getChannelId()
  {
    return mChannelId;
  }
  
  public String getGroup()
  {
    return mGroupKey;
  }
  
  public int getGroupAlertBehavior()
  {
    return mGroupAlertBehavior;
  }
  
  public Icon getLargeIcon()
  {
    return mLargeIcon;
  }
  
  public Class<? extends Style> getNotificationStyle()
  {
    String str = extras.getString("android.template");
    if (!TextUtils.isEmpty(str)) {
      return getNotificationStyleClass(str);
    }
    return null;
  }
  
  public CharSequence getSettingsText()
  {
    return mSettingsText;
  }
  
  public String getShortcutId()
  {
    return mShortcutId;
  }
  
  public Icon getSmallIcon()
  {
    return mSmallIcon;
  }
  
  public String getSortKey()
  {
    return mSortKey;
  }
  
  @Deprecated
  public long getTimeout()
  {
    return mTimeout;
  }
  
  public long getTimeoutAfter()
  {
    return mTimeout;
  }
  
  public boolean hasCompletedProgress()
  {
    boolean bool1 = extras.containsKey("android.progress");
    boolean bool2 = false;
    if ((bool1) && (extras.containsKey("android.progressMax")))
    {
      if (extras.getInt("android.progressMax") == 0) {
        return false;
      }
      if (extras.getInt("android.progress") == extras.getInt("android.progressMax")) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public boolean hasMediaSession()
  {
    boolean bool;
    if (extras.getParcelable("android.mediaSession") != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isColorized()
  {
    boolean bool1 = isColorizedMedia();
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if ((!extras.getBoolean("android.colorized")) || ((hasColorizedPermission()) || (!isForegroundService()))) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean isColorizedMedia()
  {
    Object localObject = getNotificationStyle();
    if (MediaStyle.class.equals(localObject))
    {
      localObject = (Boolean)extras.get("android.colorized");
      if (((localObject == null) || (((Boolean)localObject).booleanValue())) && (hasMediaSession())) {
        return true;
      }
    }
    else if ((DecoratedMediaCustomViewStyle.class.equals(localObject)) && (extras.getBoolean("android.colorized")) && (hasMediaSession()))
    {
      return true;
    }
    return false;
  }
  
  public boolean isGroupChild()
  {
    boolean bool;
    if ((mGroupKey != null) && ((flags & 0x200) == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGroupSummary()
  {
    boolean bool;
    if ((mGroupKey != null) && ((flags & 0x200) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMediaNotification()
  {
    Class localClass = getNotificationStyle();
    if (MediaStyle.class.equals(localClass)) {
      return true;
    }
    return DecoratedMediaCustomViewStyle.class.equals(localClass);
  }
  
  public final void lightenPayload()
  {
    tickerView = null;
    contentView = null;
    bigContentView = null;
    headsUpContentView = null;
    mLargeIcon = null;
    if ((extras != null) && (!extras.isEmpty()))
    {
      Set localSet = extras.keySet();
      int i = localSet.size();
      String[] arrayOfString = (String[])localSet.toArray(new String[i]);
      for (int j = 0; j < i; j++)
      {
        localSet = arrayOfString[j];
        if (!"android.tv.EXTENSIONS".equals(localSet))
        {
          Object localObject = extras.get(localSet);
          if ((localObject != null) && (((localObject instanceof Parcelable)) || ((localObject instanceof Parcelable[])) || ((localObject instanceof SparseArray)) || ((localObject instanceof ArrayList)))) {
            extras.remove(localSet);
          }
        }
      }
    }
  }
  
  void reduceImageSizes(Context paramContext)
  {
    if (extras.getBoolean("android.reduced.images")) {
      return;
    }
    boolean bool = ActivityManager.isLowRamDeviceStatic();
    if ((mLargeIcon != null) || (largeIcon != null))
    {
      Resources localResources = paramContext.getResources();
      Class localClass = getNotificationStyle();
      if (bool) {
        i = 17105353;
      } else {
        i = 17105352;
      }
      int j = localResources.getDimensionPixelSize(i);
      int i = j;
      if ((MediaStyle.class.equals(localClass)) || (DecoratedMediaCustomViewStyle.class.equals(localClass)))
      {
        if (bool) {
          i = 17105343;
        } else {
          i = 17105342;
        }
        j = localResources.getDimensionPixelSize(i);
        if (bool) {
          i = 17105345;
        } else {
          i = 17105344;
        }
        int k = localResources.getDimensionPixelSize(i);
        i = j;
        j = k;
      }
      if (mLargeIcon != null) {
        mLargeIcon.scaleDownIfNecessary(j, i);
      }
      if (largeIcon != null) {
        largeIcon = Icon.scaleDownIfNecessary(largeIcon, j, i);
      }
    }
    reduceImageSizesForRemoteView(contentView, paramContext, bool);
    reduceImageSizesForRemoteView(headsUpContentView, paramContext, bool);
    reduceImageSizesForRemoteView(bigContentView, paramContext, bool);
    extras.putBoolean("android.reduced.images", true);
  }
  
  @Deprecated
  public void setLatestEventInfo(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, PendingIntent paramPendingIntent)
  {
    if (getApplicationInfotargetSdkVersion > 22) {
      Log.e("Notification", "setLatestEventInfo() is deprecated and you should feel deprecated.", new Throwable());
    }
    if (getApplicationInfotargetSdkVersion < 24) {
      extras.putBoolean("android.showWhen", true);
    }
    paramContext = new Builder(paramContext, this);
    if (paramCharSequence1 != null) {
      paramContext.setContentTitle(paramCharSequence1);
    }
    if (paramCharSequence2 != null) {
      paramContext.setContentText(paramCharSequence2);
    }
    paramContext.setContentIntent(paramPendingIntent);
    paramContext.build();
  }
  
  public void setSmallIcon(Icon paramIcon)
  {
    mSmallIcon = paramIcon;
  }
  
  public boolean showsChronometer()
  {
    boolean bool;
    if ((when != 0L) && (extras.getBoolean("android.showChronometer"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean showsTime()
  {
    boolean bool;
    if ((when != 0L) && (extras.getBoolean("android.showWhen"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean suppressAlertingDueToGrouping()
  {
    if ((isGroupSummary()) && (getGroupAlertBehavior() == 2)) {
      return true;
    }
    return (isGroupChild()) && (getGroupAlertBehavior() == 1);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Notification(channel=");
    localStringBuilder.append(getChannelId());
    localStringBuilder.append(" pri=");
    localStringBuilder.append(priority);
    localStringBuilder.append(" contentView=");
    if (contentView != null)
    {
      localStringBuilder.append(contentView.getPackage());
      localStringBuilder.append("/0x");
      localStringBuilder.append(Integer.toHexString(contentView.getLayoutId()));
    }
    else
    {
      localStringBuilder.append("null");
    }
    localStringBuilder.append(" vibrate=");
    if ((defaults & 0x2) != 0)
    {
      localStringBuilder.append("default");
    }
    else if (vibrate != null)
    {
      int i = vibrate.length - 1;
      localStringBuilder.append("[");
      for (int j = 0; j < i; j++)
      {
        localStringBuilder.append(vibrate[j]);
        localStringBuilder.append(',');
      }
      if (i != -1) {
        localStringBuilder.append(vibrate[i]);
      }
      localStringBuilder.append("]");
    }
    else
    {
      localStringBuilder.append("null");
    }
    localStringBuilder.append(" sound=");
    if ((defaults & 0x1) != 0) {
      localStringBuilder.append("default");
    } else if (sound != null) {
      localStringBuilder.append(sound.toString());
    } else {
      localStringBuilder.append("null");
    }
    if (tickerText != null) {
      localStringBuilder.append(" tick");
    }
    localStringBuilder.append(" defaults=0x");
    localStringBuilder.append(Integer.toHexString(defaults));
    localStringBuilder.append(" flags=0x");
    localStringBuilder.append(Integer.toHexString(flags));
    localStringBuilder.append(String.format(" color=0x%08x", new Object[] { Integer.valueOf(color) }));
    if (category != null)
    {
      localStringBuilder.append(" category=");
      localStringBuilder.append(category);
    }
    if (mGroupKey != null)
    {
      localStringBuilder.append(" groupKey=");
      localStringBuilder.append(mGroupKey);
    }
    if (mSortKey != null)
    {
      localStringBuilder.append(" sortKey=");
      localStringBuilder.append(mSortKey);
    }
    if (actions != null)
    {
      localStringBuilder.append(" actions=");
      localStringBuilder.append(actions.length);
    }
    localStringBuilder.append(" vis=");
    localStringBuilder.append(visibilityToString(visibility));
    if (publicVersion != null)
    {
      localStringBuilder.append(" publicVersion=");
      localStringBuilder.append(publicVersion.toString());
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void visitUris(Consumer<Uri> paramConsumer)
  {
    paramConsumer.accept(sound);
    if (tickerView != null) {
      tickerView.visitUris(paramConsumer);
    }
    if (contentView != null) {
      contentView.visitUris(paramConsumer);
    }
    if (bigContentView != null) {
      bigContentView.visitUris(paramConsumer);
    }
    if (headsUpContentView != null) {
      headsUpContentView.visitUris(paramConsumer);
    }
    if (extras != null)
    {
      paramConsumer.accept((Uri)extras.getParcelable("android.audioContents"));
      paramConsumer.accept((Uri)extras.getParcelable("android.backgroundImageUri"));
    }
    if ((MessagingStyle.class.equals(getNotificationStyle())) && (extras != null))
    {
      Object localObject = extras.getParcelableArray("android.messages");
      if (!ArrayUtils.isEmpty((Object[])localObject))
      {
        localObject = Notification.MessagingStyle.Message.getMessagesFromBundleArray((Parcelable[])localObject).iterator();
        while (((Iterator)localObject).hasNext()) {
          paramConsumer.accept(((Notification.MessagingStyle.Message)((Iterator)localObject).next()).getDataUri());
        }
      }
      localObject = extras.getParcelableArray("android.messages.historic");
      if (!ArrayUtils.isEmpty((Object[])localObject))
      {
        localObject = Notification.MessagingStyle.Message.getMessagesFromBundleArray((Parcelable[])localObject).iterator();
        while (((Iterator)localObject).hasNext()) {
          paramConsumer.accept(((Notification.MessagingStyle.Message)((Iterator)localObject).next()).getDataUri());
        }
      }
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i;
    if (allPendingIntents == null) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      PendingIntent.setOnMarshaledListener(new _..Lambda.Notification.hOCsSZH8tWalFSbIzQ9x9IcPa9M(this, paramParcel));
    }
    try
    {
      writeToParcelImpl(paramParcel, paramInt);
      paramParcel.writeArraySet(allPendingIntents);
      return;
    }
    finally
    {
      if (i != 0) {
        PendingIntent.setOnMarshaledListener(null);
      }
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, getChannelId());
    boolean bool;
    if (tickerText != null) {
      bool = true;
    } else {
      bool = false;
    }
    paramProtoOutputStream.write(1133871366146L, bool);
    paramProtoOutputStream.write(1120986464259L, flags);
    paramProtoOutputStream.write(1120986464260L, color);
    paramProtoOutputStream.write(1138166333445L, category);
    paramProtoOutputStream.write(1138166333446L, mGroupKey);
    paramProtoOutputStream.write(1138166333447L, mSortKey);
    if (actions != null) {
      paramProtoOutputStream.write(1120986464264L, actions.length);
    }
    if ((visibility >= -1) && (visibility <= 1)) {
      paramProtoOutputStream.write(1159641169929L, visibility);
    }
    if (publicVersion != null) {
      publicVersion.writeToProto(paramProtoOutputStream, 1146756268042L);
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  public static class Action
    implements Parcelable
  {
    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator()
    {
      public Notification.Action createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Notification.Action(paramAnonymousParcel, null);
      }
      
      public Notification.Action[] newArray(int paramAnonymousInt)
      {
        return new Notification.Action[paramAnonymousInt];
      }
    };
    private static final String EXTRA_DATA_ONLY_INPUTS = "android.extra.DATA_ONLY_INPUTS";
    public static final int SEMANTIC_ACTION_ARCHIVE = 5;
    public static final int SEMANTIC_ACTION_CALL = 10;
    public static final int SEMANTIC_ACTION_DELETE = 4;
    public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;
    public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;
    public static final int SEMANTIC_ACTION_MUTE = 6;
    public static final int SEMANTIC_ACTION_NONE = 0;
    public static final int SEMANTIC_ACTION_REPLY = 1;
    public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;
    public static final int SEMANTIC_ACTION_THUMBS_UP = 8;
    public static final int SEMANTIC_ACTION_UNMUTE = 7;
    public PendingIntent actionIntent;
    @Deprecated
    public int icon;
    private boolean mAllowGeneratedReplies;
    private final Bundle mExtras;
    private Icon mIcon;
    private final RemoteInput[] mRemoteInputs;
    private final int mSemanticAction;
    public CharSequence title;
    
    @Deprecated
    public Action(int paramInt, CharSequence paramCharSequence, PendingIntent paramPendingIntent)
    {
      this(Icon.createWithResource("", paramInt), paramCharSequence, paramPendingIntent, new Bundle(), null, true, 0);
    }
    
    private Action(Icon paramIcon, CharSequence paramCharSequence, PendingIntent paramPendingIntent, Bundle paramBundle, RemoteInput[] paramArrayOfRemoteInput, boolean paramBoolean, int paramInt)
    {
      mAllowGeneratedReplies = true;
      mIcon = paramIcon;
      if ((paramIcon != null) && (paramIcon.getType() == 2)) {
        icon = paramIcon.getResId();
      }
      title = paramCharSequence;
      actionIntent = paramPendingIntent;
      if (paramBundle != null) {
        paramIcon = paramBundle;
      } else {
        paramIcon = new Bundle();
      }
      mExtras = paramIcon;
      mRemoteInputs = paramArrayOfRemoteInput;
      mAllowGeneratedReplies = paramBoolean;
      mSemanticAction = paramInt;
    }
    
    private Action(Parcel paramParcel)
    {
      boolean bool = true;
      mAllowGeneratedReplies = true;
      if (paramParcel.readInt() != 0)
      {
        mIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
        if (mIcon.getType() == 2) {
          icon = mIcon.getResId();
        }
      }
      title = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      if (paramParcel.readInt() == 1) {
        actionIntent = ((PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel));
      }
      mExtras = Bundle.setDefusable(paramParcel.readBundle(), true);
      mRemoteInputs = ((RemoteInput[])paramParcel.createTypedArray(RemoteInput.CREATOR));
      if (paramParcel.readInt() != 1) {
        bool = false;
      }
      mAllowGeneratedReplies = bool;
      mSemanticAction = paramParcel.readInt();
    }
    
    public Action clone()
    {
      Icon localIcon = getIcon();
      CharSequence localCharSequence = title;
      PendingIntent localPendingIntent = actionIntent;
      if (mExtras == null) {}
      for (Bundle localBundle = new Bundle();; localBundle = new Bundle(mExtras)) {
        break;
      }
      return new Action(localIcon, localCharSequence, localPendingIntent, localBundle, getRemoteInputs(), getAllowGeneratedReplies(), getSemanticAction());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean getAllowGeneratedReplies()
    {
      return mAllowGeneratedReplies;
    }
    
    public RemoteInput[] getDataOnlyRemoteInputs()
    {
      return (RemoteInput[])mExtras.getParcelableArray("android.extra.DATA_ONLY_INPUTS");
    }
    
    public Bundle getExtras()
    {
      return mExtras;
    }
    
    public Icon getIcon()
    {
      if ((mIcon == null) && (icon != 0)) {
        mIcon = Icon.createWithResource("", icon);
      }
      return mIcon;
    }
    
    public RemoteInput[] getRemoteInputs()
    {
      return mRemoteInputs;
    }
    
    public int getSemanticAction()
    {
      return mSemanticAction;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      Icon localIcon = getIcon();
      if (localIcon != null)
      {
        paramParcel.writeInt(1);
        localIcon.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      TextUtils.writeToParcel(title, paramParcel, paramInt);
      if (actionIntent != null)
      {
        paramParcel.writeInt(1);
        actionIntent.writeToParcel(paramParcel, paramInt);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      paramParcel.writeBundle(mExtras);
      paramParcel.writeTypedArray(mRemoteInputs, paramInt);
      paramParcel.writeInt(mAllowGeneratedReplies);
      paramParcel.writeInt(mSemanticAction);
    }
    
    public static final class Builder
    {
      private boolean mAllowGeneratedReplies = true;
      private final Bundle mExtras;
      private final Icon mIcon;
      private final PendingIntent mIntent;
      private ArrayList<RemoteInput> mRemoteInputs;
      private int mSemanticAction;
      private final CharSequence mTitle;
      
      @Deprecated
      public Builder(int paramInt, CharSequence paramCharSequence, PendingIntent paramPendingIntent)
      {
        this(Icon.createWithResource("", paramInt), paramCharSequence, paramPendingIntent);
      }
      
      public Builder(Notification.Action paramAction)
      {
        this(paramAction.getIcon(), title, actionIntent, new Bundle(mExtras), paramAction.getRemoteInputs(), paramAction.getAllowGeneratedReplies(), paramAction.getSemanticAction());
      }
      
      public Builder(Icon paramIcon, CharSequence paramCharSequence, PendingIntent paramPendingIntent)
      {
        this(paramIcon, paramCharSequence, paramPendingIntent, new Bundle(), null, true, 0);
      }
      
      private Builder(Icon paramIcon, CharSequence paramCharSequence, PendingIntent paramPendingIntent, Bundle paramBundle, RemoteInput[] paramArrayOfRemoteInput, boolean paramBoolean, int paramInt)
      {
        mIcon = paramIcon;
        mTitle = paramCharSequence;
        mIntent = paramPendingIntent;
        mExtras = paramBundle;
        if (paramArrayOfRemoteInput != null)
        {
          mRemoteInputs = new ArrayList(paramArrayOfRemoteInput.length);
          Collections.addAll(mRemoteInputs, paramArrayOfRemoteInput);
        }
        mAllowGeneratedReplies = paramBoolean;
        mSemanticAction = paramInt;
      }
      
      public Builder addExtras(Bundle paramBundle)
      {
        if (paramBundle != null) {
          mExtras.putAll(paramBundle);
        }
        return this;
      }
      
      public Builder addRemoteInput(RemoteInput paramRemoteInput)
      {
        if (mRemoteInputs == null) {
          mRemoteInputs = new ArrayList();
        }
        mRemoteInputs.add(paramRemoteInput);
        return this;
      }
      
      public Notification.Action build()
      {
        Object localObject1 = new ArrayList();
        Object localObject2 = (RemoteInput[])mExtras.getParcelableArray("android.extra.DATA_ONLY_INPUTS");
        if (localObject2 != null)
        {
          int i = localObject2.length;
          for (int j = 0; j < i; j++) {
            ((ArrayList)localObject1).add(localObject2[j]);
          }
        }
        localObject2 = new ArrayList();
        if (mRemoteInputs != null)
        {
          Iterator localIterator = mRemoteInputs.iterator();
          while (localIterator.hasNext())
          {
            RemoteInput localRemoteInput = (RemoteInput)localIterator.next();
            if (localRemoteInput.isDataOnly()) {
              ((ArrayList)localObject1).add(localRemoteInput);
            } else {
              ((List)localObject2).add(localRemoteInput);
            }
          }
        }
        if (!((ArrayList)localObject1).isEmpty())
        {
          localObject1 = (RemoteInput[])((ArrayList)localObject1).toArray(new RemoteInput[((ArrayList)localObject1).size()]);
          mExtras.putParcelableArray("android.extra.DATA_ONLY_INPUTS", (Parcelable[])localObject1);
        }
        if (((List)localObject2).isEmpty()) {}
        for (localObject2 = null;; localObject2 = (RemoteInput[])((List)localObject2).toArray(new RemoteInput[((List)localObject2).size()])) {
          break;
        }
        return new Notification.Action(mIcon, mTitle, mIntent, mExtras, (RemoteInput[])localObject2, mAllowGeneratedReplies, mSemanticAction, null);
      }
      
      public Builder extend(Notification.Action.Extender paramExtender)
      {
        paramExtender.extend(this);
        return this;
      }
      
      public Bundle getExtras()
      {
        return mExtras;
      }
      
      public Builder setAllowGeneratedReplies(boolean paramBoolean)
      {
        mAllowGeneratedReplies = paramBoolean;
        return this;
      }
      
      public Builder setSemanticAction(int paramInt)
      {
        mSemanticAction = paramInt;
        return this;
      }
    }
    
    public static abstract interface Extender
    {
      public abstract Notification.Action.Builder extend(Notification.Action.Builder paramBuilder);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface SemanticAction {}
    
    public static final class WearableExtender
      implements Notification.Action.Extender
    {
      private static final int DEFAULT_FLAGS = 1;
      private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
      private static final int FLAG_AVAILABLE_OFFLINE = 1;
      private static final int FLAG_HINT_DISPLAY_INLINE = 4;
      private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 2;
      private static final String KEY_CANCEL_LABEL = "cancelLabel";
      private static final String KEY_CONFIRM_LABEL = "confirmLabel";
      private static final String KEY_FLAGS = "flags";
      private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
      private CharSequence mCancelLabel;
      private CharSequence mConfirmLabel;
      private int mFlags = 1;
      private CharSequence mInProgressLabel;
      
      public WearableExtender() {}
      
      public WearableExtender(Notification.Action paramAction)
      {
        paramAction = paramAction.getExtras().getBundle("android.wearable.EXTENSIONS");
        if (paramAction != null)
        {
          mFlags = paramAction.getInt("flags", 1);
          mInProgressLabel = paramAction.getCharSequence("inProgressLabel");
          mConfirmLabel = paramAction.getCharSequence("confirmLabel");
          mCancelLabel = paramAction.getCharSequence("cancelLabel");
        }
      }
      
      private void setFlag(int paramInt, boolean paramBoolean)
      {
        if (paramBoolean) {
          mFlags |= paramInt;
        } else {
          mFlags &= paramInt;
        }
      }
      
      public WearableExtender clone()
      {
        WearableExtender localWearableExtender = new WearableExtender();
        mFlags = mFlags;
        mInProgressLabel = mInProgressLabel;
        mConfirmLabel = mConfirmLabel;
        mCancelLabel = mCancelLabel;
        return localWearableExtender;
      }
      
      public Notification.Action.Builder extend(Notification.Action.Builder paramBuilder)
      {
        Bundle localBundle = new Bundle();
        if (mFlags != 1) {
          localBundle.putInt("flags", mFlags);
        }
        if (mInProgressLabel != null) {
          localBundle.putCharSequence("inProgressLabel", mInProgressLabel);
        }
        if (mConfirmLabel != null) {
          localBundle.putCharSequence("confirmLabel", mConfirmLabel);
        }
        if (mCancelLabel != null) {
          localBundle.putCharSequence("cancelLabel", mCancelLabel);
        }
        paramBuilder.getExtras().putBundle("android.wearable.EXTENSIONS", localBundle);
        return paramBuilder;
      }
      
      @Deprecated
      public CharSequence getCancelLabel()
      {
        return mCancelLabel;
      }
      
      @Deprecated
      public CharSequence getConfirmLabel()
      {
        return mConfirmLabel;
      }
      
      public boolean getHintDisplayActionInline()
      {
        boolean bool;
        if ((mFlags & 0x4) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      
      public boolean getHintLaunchesActivity()
      {
        boolean bool;
        if ((mFlags & 0x2) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      
      @Deprecated
      public CharSequence getInProgressLabel()
      {
        return mInProgressLabel;
      }
      
      public boolean isAvailableOffline()
      {
        int i = mFlags;
        boolean bool = true;
        if ((i & 0x1) == 0) {
          bool = false;
        }
        return bool;
      }
      
      public WearableExtender setAvailableOffline(boolean paramBoolean)
      {
        setFlag(1, paramBoolean);
        return this;
      }
      
      @Deprecated
      public WearableExtender setCancelLabel(CharSequence paramCharSequence)
      {
        mCancelLabel = paramCharSequence;
        return this;
      }
      
      @Deprecated
      public WearableExtender setConfirmLabel(CharSequence paramCharSequence)
      {
        mConfirmLabel = paramCharSequence;
        return this;
      }
      
      public WearableExtender setHintDisplayActionInline(boolean paramBoolean)
      {
        setFlag(4, paramBoolean);
        return this;
      }
      
      public WearableExtender setHintLaunchesActivity(boolean paramBoolean)
      {
        setFlag(2, paramBoolean);
        return this;
      }
      
      @Deprecated
      public WearableExtender setInProgressLabel(CharSequence paramCharSequence)
      {
        mInProgressLabel = paramCharSequence;
        return this;
      }
    }
  }
  
  public static class BigPictureStyle
    extends Notification.Style
  {
    public static final int MIN_ASHMEM_BITMAP_SIZE = 131072;
    private Icon mBigLargeIcon;
    private boolean mBigLargeIconSet = false;
    private Bitmap mPicture;
    
    public BigPictureStyle() {}
    
    @Deprecated
    public BigPictureStyle(Notification.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }
    
    private static boolean areBitmapsObviouslyDifferent(Bitmap paramBitmap1, Bitmap paramBitmap2)
    {
      boolean bool = false;
      if (paramBitmap1 == paramBitmap2) {
        return false;
      }
      if ((paramBitmap1 != null) && (paramBitmap2 != null))
      {
        if ((paramBitmap1.getWidth() == paramBitmap2.getWidth()) && (paramBitmap1.getHeight() == paramBitmap2.getHeight()) && (paramBitmap1.getConfig() == paramBitmap2.getConfig()) && (paramBitmap1.getGenerationId() == paramBitmap2.getGenerationId())) {
          break label72;
        }
        bool = true;
        label72:
        return bool;
      }
      return true;
    }
    
    public void addExtras(Bundle paramBundle)
    {
      super.addExtras(paramBundle);
      if (mBigLargeIconSet) {
        paramBundle.putParcelable("android.largeIcon.big", mBigLargeIcon);
      }
      paramBundle.putParcelable("android.picture", mPicture);
    }
    
    public boolean areNotificationsVisiblyDifferent(Notification.Style paramStyle)
    {
      if ((paramStyle != null) && (getClass() == paramStyle.getClass()))
      {
        paramStyle = (BigPictureStyle)paramStyle;
        return areBitmapsObviouslyDifferent(getBigPicture(), paramStyle.getBigPicture());
      }
      return true;
    }
    
    public BigPictureStyle bigLargeIcon(Bitmap paramBitmap)
    {
      if (paramBitmap != null) {
        paramBitmap = Icon.createWithBitmap(paramBitmap);
      } else {
        paramBitmap = null;
      }
      return bigLargeIcon(paramBitmap);
    }
    
    public BigPictureStyle bigLargeIcon(Icon paramIcon)
    {
      mBigLargeIconSet = true;
      mBigLargeIcon = paramIcon;
      return this;
    }
    
    public BigPictureStyle bigPicture(Bitmap paramBitmap)
    {
      mPicture = paramBitmap;
      return this;
    }
    
    public Bitmap getBigPicture()
    {
      return mPicture;
    }
    
    public boolean hasSummaryInHeader()
    {
      return false;
    }
    
    public RemoteViews makeBigContentView()
    {
      Icon localIcon = null;
      Bitmap localBitmap = null;
      if (mBigLargeIconSet)
      {
        localIcon = access$300mBuilder).mLargeIcon;
        Notification.access$1102(Notification.Builder.access$300(mBuilder), mBigLargeIcon);
        localBitmap = access$300mBuilder).largeIcon;
        access$300mBuilder).largeIcon = null;
      }
      RemoteViews localRemoteViews = getStandardView(Notification.Builder.access$2200(mBuilder), null);
      if (mSummaryTextSet)
      {
        localRemoteViews.setTextViewText(16909437, Notification.Builder.access$2400(mBuilder, Notification.Builder.access$2300(mBuilder, mSummaryText)));
        Notification.Builder.access$2500(mBuilder, localRemoteViews, 16909437);
        localRemoteViews.setViewVisibility(16909437, 0);
      }
      mBuilder.setContentMinHeight(localRemoteViews, Notification.Builder.access$300(mBuilder).hasLargeIcon());
      if (mBigLargeIconSet)
      {
        Notification.access$1102(Notification.Builder.access$300(mBuilder), localIcon);
        access$300mBuilder).largeIcon = localBitmap;
      }
      localRemoteViews.setImageViewBitmap(16908808, mPicture);
      return localRemoteViews;
    }
    
    public void purgeResources()
    {
      super.purgeResources();
      if ((mPicture != null) && (mPicture.isMutable()) && (mPicture.getAllocationByteCount() >= 131072)) {
        mPicture = mPicture.createAshmemBitmap();
      }
      if (mBigLargeIcon != null) {
        mBigLargeIcon.convertToAshmem();
      }
    }
    
    public void reduceImageSizes(Context paramContext)
    {
      super.reduceImageSizes(paramContext);
      paramContext = paramContext.getResources();
      boolean bool = ActivityManager.isLowRamDeviceStatic();
      int i;
      if (mPicture != null)
      {
        if (bool) {
          i = 17105312;
        } else {
          i = 17105311;
        }
        int j = paramContext.getDimensionPixelSize(i);
        if (bool) {
          i = 17105314;
        } else {
          i = 17105313;
        }
        i = paramContext.getDimensionPixelSize(i);
        mPicture = Icon.scaleDownIfNecessary(mPicture, j, i);
      }
      if (mBigLargeIcon != null)
      {
        if (bool) {
          i = 17105353;
        } else {
          i = 17105352;
        }
        i = paramContext.getDimensionPixelSize(i);
        mBigLargeIcon.scaleDownIfNecessary(i, i);
      }
    }
    
    protected void restoreFromExtras(Bundle paramBundle)
    {
      super.restoreFromExtras(paramBundle);
      if (paramBundle.containsKey("android.largeIcon.big"))
      {
        mBigLargeIconSet = true;
        mBigLargeIcon = ((Icon)paramBundle.getParcelable("android.largeIcon.big"));
      }
      mPicture = ((Bitmap)paramBundle.getParcelable("android.picture"));
    }
    
    public BigPictureStyle setBigContentTitle(CharSequence paramCharSequence)
    {
      internalSetBigContentTitle(Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public BigPictureStyle setSummaryText(CharSequence paramCharSequence)
    {
      internalSetSummaryText(Notification.safeCharSequence(paramCharSequence));
      return this;
    }
  }
  
  public static class BigTextStyle
    extends Notification.Style
  {
    private CharSequence mBigText;
    
    public BigTextStyle() {}
    
    @Deprecated
    public BigTextStyle(Notification.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }
    
    static void applyBigTextContentView(Notification.Builder paramBuilder, RemoteViews paramRemoteViews, CharSequence paramCharSequence)
    {
      paramRemoteViews.setTextViewText(16908809, Notification.Builder.access$2400(paramBuilder, paramCharSequence));
      Notification.Builder.access$2500(paramBuilder, paramRemoteViews, 16908809);
      int i;
      if (TextUtils.isEmpty(paramCharSequence)) {
        i = 8;
      } else {
        i = 0;
      }
      paramRemoteViews.setViewVisibility(16908809, i);
      paramRemoteViews.setBoolean(16908809, "setHasImage", Notification.Builder.access$300(paramBuilder).hasLargeIcon());
    }
    
    public void addExtras(Bundle paramBundle)
    {
      super.addExtras(paramBundle);
      paramBundle.putCharSequence("android.bigText", mBigText);
    }
    
    public boolean areNotificationsVisiblyDifferent(Notification.Style paramStyle)
    {
      if ((paramStyle != null) && (getClass() == paramStyle.getClass()))
      {
        paramStyle = (BigTextStyle)paramStyle;
        return true ^ Objects.equals(String.valueOf(getBigText()), String.valueOf(paramStyle.getBigText()));
      }
      return true;
    }
    
    public BigTextStyle bigText(CharSequence paramCharSequence)
    {
      mBigText = Notification.safeCharSequence(paramCharSequence);
      return this;
    }
    
    public CharSequence getBigText()
    {
      return mBigText;
    }
    
    public RemoteViews makeBigContentView()
    {
      CharSequence localCharSequence1 = Notification.Builder.access$2000(mBuilder).getCharSequence("android.text");
      Notification.Builder.access$2000(mBuilder).putCharSequence("android.text", null);
      Object localObject = new Notification.TemplateBindResult(null);
      RemoteViews localRemoteViews = getStandardView(Notification.Builder.access$2900(mBuilder), (Notification.TemplateBindResult)localObject);
      localRemoteViews.setInt(16908809, "setImageEndMargin", ((Notification.TemplateBindResult)localObject).getIconMarginEnd());
      Notification.Builder.access$2000(mBuilder).putCharSequence("android.text", localCharSequence1);
      CharSequence localCharSequence2 = Notification.Builder.access$2300(mBuilder, mBigText);
      localObject = localCharSequence2;
      if (TextUtils.isEmpty(localCharSequence2)) {
        localObject = Notification.Builder.access$2300(mBuilder, localCharSequence1);
      }
      applyBigTextContentView(mBuilder, localRemoteViews, (CharSequence)localObject);
      return localRemoteViews;
    }
    
    public RemoteViews makeContentView(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        Notification.Builder.access$2602(mBuilder, Notification.Builder.access$2700(mBuilder));
        Notification.Builder.access$2702(mBuilder, new ArrayList());
        RemoteViews localRemoteViews = makeBigContentView();
        Notification.Builder.access$2702(mBuilder, Notification.Builder.access$2600(mBuilder));
        Notification.Builder.access$2602(mBuilder, null);
        return localRemoteViews;
      }
      return super.makeContentView(paramBoolean);
    }
    
    public RemoteViews makeHeadsUpContentView(boolean paramBoolean)
    {
      if ((paramBoolean) && (Notification.Builder.access$2700(mBuilder).size() > 0)) {
        return makeBigContentView();
      }
      return super.makeHeadsUpContentView(paramBoolean);
    }
    
    protected void restoreFromExtras(Bundle paramBundle)
    {
      super.restoreFromExtras(paramBundle);
      mBigText = paramBundle.getCharSequence("android.bigText");
    }
    
    public BigTextStyle setBigContentTitle(CharSequence paramCharSequence)
    {
      internalSetBigContentTitle(Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public BigTextStyle setSummaryText(CharSequence paramCharSequence)
    {
      internalSetSummaryText(Notification.safeCharSequence(paramCharSequence));
      return this;
    }
  }
  
  public static class Builder
  {
    public static final String EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.bigViewActionCount";
    public static final String EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.contentViewActionCount";
    public static final String EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.hudViewActionCount";
    private static final int LIGHTNESS_TEXT_DIFFERENCE_DARK = -10;
    private static final int LIGHTNESS_TEXT_DIFFERENCE_LIGHT = 20;
    private static final int MAX_ACTION_BUTTONS = 3;
    private static final boolean USE_ONLY_TITLE_IN_LOW_PRIORITY_SUMMARY = SystemProperties.getBoolean("notifications.only_title", true);
    private ArrayList<Notification.Action> mActions = new ArrayList(3);
    private int mBackgroundColor = 1;
    private int mCachedAmbientColor = 1;
    private int mCachedAmbientColorIsFor = 1;
    private int mCachedContrastColor = 1;
    private int mCachedContrastColorIsFor = 1;
    private NotificationColorUtil mColorUtil;
    private Context mContext;
    private int mForegroundColor = 1;
    private boolean mInNightMode;
    private boolean mIsLegacy;
    private boolean mIsLegacyInitialized;
    private Notification mN;
    private int mNeutralColor = 1;
    private ArrayList<Notification.Action> mOriginalActions;
    Notification.StandardTemplateParams mParams = new Notification.StandardTemplateParams(null);
    private ArrayList<Person> mPersonList = new ArrayList();
    private int mPrimaryTextColor = 1;
    private boolean mRebuildStyledRemoteViews;
    private int mSecondaryTextColor = 1;
    private Notification.Style mStyle;
    private int mTextColorsAreForBackground = 1;
    private boolean mTintActionButtons;
    private Bundle mUserExtras = new Bundle();
    
    @Deprecated
    public Builder(Context paramContext)
    {
      this(paramContext, (Notification)null);
    }
    
    public Builder(Context paramContext, Notification paramNotification)
    {
      mContext = paramContext;
      Resources localResources = mContext.getResources();
      mTintActionButtons = localResources.getBoolean(17957060);
      if (localResources.getBoolean(17956968))
      {
        boolean bool;
        if ((getConfigurationuiMode & 0x30) == 32) {
          bool = true;
        } else {
          bool = false;
        }
        mInNightMode = bool;
      }
      if (paramNotification == null)
      {
        mN = new Notification();
        if (getApplicationInfotargetSdkVersion < 24) {
          mN.extras.putBoolean("android.showWhen", true);
        }
        mN.priority = 0;
        mN.visibility = 0;
      }
      else
      {
        mN = paramNotification;
        if (mN.actions != null) {
          Collections.addAll(mActions, mN.actions);
        }
        if (mN.extras.containsKey("android.people.list"))
        {
          paramContext = mN.extras.getParcelableArrayList("android.people.list");
          mPersonList.addAll(paramContext);
        }
        if ((mN.getSmallIcon() == null) && (mN.icon != 0)) {
          setSmallIcon(mN.icon);
        }
        if ((mN.getLargeIcon() == null) && (mN.largeIcon != null)) {
          setLargeIcon(mN.largeIcon);
        }
        paramContext = mN.extras.getString("android.template");
        if (!TextUtils.isEmpty(paramContext))
        {
          paramNotification = Notification.getNotificationStyleClass(paramContext);
          if (paramNotification == null)
          {
            paramNotification = new StringBuilder();
            paramNotification.append("Unknown style class: ");
            paramNotification.append(paramContext);
            Log.d("Notification", paramNotification.toString());
          }
          else
          {
            try
            {
              paramContext = paramNotification.getDeclaredConstructor(new Class[0]);
              paramContext.setAccessible(true);
              paramContext = (Notification.Style)paramContext.newInstance(new Object[0]);
              paramContext.restoreFromExtras(mN.extras);
              if (paramContext != null) {
                setStyle(paramContext);
              }
            }
            catch (Throwable paramContext)
            {
              Log.e("Notification", "Could not create Style", paramContext);
            }
          }
        }
      }
    }
    
    public Builder(Context paramContext, String paramString)
    {
      this(paramContext, (Notification)null);
      Notification.access$502(mN, paramString);
    }
    
    private RemoteViews applyStandardTemplate(int paramInt, Notification.StandardTemplateParams paramStandardTemplateParams, Notification.TemplateBindResult paramTemplateBindResult)
    {
      Notification.BuilderRemoteViews localBuilderRemoteViews = new Notification.BuilderRemoteViews(mContext.getApplicationInfo(), paramInt);
      resetStandardTemplate(localBuilderRemoteViews);
      Bundle localBundle = mN.extras;
      updateBackgroundColor(localBuilderRemoteViews);
      bindNotificationHeader(localBuilderRemoteViews, ambient, headerTextSecondary);
      bindLargeIconAndReply(localBuilderRemoteViews, paramStandardTemplateParams, paramTemplateBindResult);
      boolean bool1 = handleProgressBar(hasProgress, localBuilderRemoteViews, localBundle);
      paramTemplateBindResult = title;
      boolean bool2 = false;
      if (paramTemplateBindResult != null)
      {
        localBuilderRemoteViews.setViewVisibility(16908310, 0);
        localBuilderRemoteViews.setTextViewText(16908310, processTextSpans(title));
        if (!ambient) {
          setTextViewColorPrimary(localBuilderRemoteViews, 16908310);
        }
        if (bool1) {
          paramInt = -2;
        } else {
          paramInt = -1;
        }
        localBuilderRemoteViews.setViewLayoutWidth(16908310, paramInt);
      }
      if (text != null)
      {
        if (bool1) {
          paramInt = 16909465;
        } else {
          paramInt = 16909437;
        }
        localBuilderRemoteViews.setTextViewText(paramInt, processTextSpans(text));
        if (!ambient) {
          setTextViewColorSecondary(localBuilderRemoteViews, paramInt);
        }
        localBuilderRemoteViews.setViewVisibility(paramInt, 0);
      }
      if ((!bool1) && (!mN.hasLargeIcon())) {
        break label231;
      }
      bool2 = true;
      label231:
      setContentMinHeight(localBuilderRemoteViews, bool2);
      return localBuilderRemoteViews;
    }
    
    private RemoteViews applyStandardTemplate(int paramInt, Notification.TemplateBindResult paramTemplateBindResult)
    {
      return applyStandardTemplate(paramInt, mParams.reset().fillTextsFrom(this), paramTemplateBindResult);
    }
    
    private RemoteViews applyStandardTemplate(int paramInt, boolean paramBoolean, Notification.TemplateBindResult paramTemplateBindResult)
    {
      return applyStandardTemplate(paramInt, mParams.reset().hasProgress(paramBoolean).fillTextsFrom(this), paramTemplateBindResult);
    }
    
    private RemoteViews applyStandardTemplateWithActions(int paramInt, Notification.StandardTemplateParams paramStandardTemplateParams, Notification.TemplateBindResult paramTemplateBindResult)
    {
      paramTemplateBindResult = applyStandardTemplate(paramInt, paramStandardTemplateParams, paramTemplateBindResult);
      resetStandardTemplateWithActions(paramTemplateBindResult);
      paramInt = mActions.size();
      boolean bool;
      if ((mN.fullScreenIntent != null) && (!ambient)) {
        bool = true;
      } else {
        bool = false;
      }
      paramTemplateBindResult.setBoolean(16908709, "setEmphasizedMode", bool);
      int i = 8;
      if (paramInt > 0)
      {
        paramTemplateBindResult.setViewVisibility(16908710, 0);
        paramTemplateBindResult.setViewVisibility(16908709, 0);
        paramTemplateBindResult.setViewLayoutMarginBottomDimen(16909175, 0);
        int j = paramInt;
        if (paramInt > 3) {
          j = 3;
        }
        paramInt = 0;
        for (int k = 0;; k++)
        {
          m = paramInt;
          if (k >= j) {
            break;
          }
          localObject = (Notification.Action)mActions.get(k);
          int n = hasValidRemoteInput((Notification.Action)localObject);
          paramInt |= n;
          localObject = generateActionButton((Notification.Action)localObject, bool, ambient);
          if ((n != 0) && (!bool)) {
            ((RemoteViews)localObject).setInt(16908686, "setBackgroundResource", 0);
          }
          paramTemplateBindResult.addView(16908709, (RemoteViews)localObject);
        }
      }
      paramTemplateBindResult.setViewVisibility(16908710, 8);
      int m = 0;
      Object localObject = mN.extras.getCharSequenceArray("android.remoteInputHistory");
      if ((!ambient) && (m != 0) && (localObject != null) && (localObject.length > 0) && (!TextUtils.isEmpty(localObject[0])) && (maxRemoteInputHistory > 0))
      {
        bool = mN.extras.getBoolean("android.remoteInputSpinner");
        paramTemplateBindResult.setViewVisibility(16909179, 0);
        paramTemplateBindResult.setViewVisibility(16909182, 0);
        paramTemplateBindResult.setTextViewText(16909181, processTextSpans(localObject[0]));
        setTextViewColorSecondary(paramTemplateBindResult, 16909181);
        if (bool) {
          paramInt = 0;
        } else {
          paramInt = i;
        }
        paramTemplateBindResult.setViewVisibility(16909180, paramInt);
        if (isColorized()) {
          paramInt = getPrimaryTextColor();
        } else {
          paramInt = resolveContrastColor();
        }
        paramTemplateBindResult.setProgressIndeterminateTintList(16909180, ColorStateList.valueOf(paramInt));
        if ((localObject.length > 1) && (!TextUtils.isEmpty(localObject[1])) && (maxRemoteInputHistory > 1))
        {
          paramTemplateBindResult.setViewVisibility(16909183, 0);
          paramTemplateBindResult.setTextViewText(16909183, processTextSpans(localObject[1]));
          setTextViewColorSecondary(paramTemplateBindResult, 16909183);
          if ((localObject.length > 2) && (!TextUtils.isEmpty(localObject[2])) && (maxRemoteInputHistory > 2))
          {
            paramTemplateBindResult.setViewVisibility(16909184, 0);
            paramTemplateBindResult.setTextViewText(16909184, processTextSpans(localObject[2]));
            setTextViewColorSecondary(paramTemplateBindResult, 16909184);
          }
        }
      }
      return paramTemplateBindResult;
    }
    
    private RemoteViews applyStandardTemplateWithActions(int paramInt, Notification.TemplateBindResult paramTemplateBindResult)
    {
      return applyStandardTemplateWithActions(paramInt, mParams.reset().fillTextsFrom(this), paramTemplateBindResult);
    }
    
    private void bindActivePermissions(RemoteViews paramRemoteViews, boolean paramBoolean)
    {
      int i;
      if (paramBoolean) {
        i = resolveAmbientColor();
      } else {
        i = getNeutralColor();
      }
      paramRemoteViews.setDrawableTint(16908839, false, i, PorterDuff.Mode.SRC_ATOP);
      paramRemoteViews.setDrawableTint(16909126, false, i, PorterDuff.Mode.SRC_ATOP);
      paramRemoteViews.setDrawableTint(16909213, false, i, PorterDuff.Mode.SRC_ATOP);
    }
    
    private void bindExpandButton(RemoteViews paramRemoteViews)
    {
      int i;
      if (isColorized()) {
        i = getPrimaryTextColor();
      } else {
        i = getSecondaryTextColor();
      }
      paramRemoteViews.setDrawableTint(16908922, false, i, PorterDuff.Mode.SRC_ATOP);
      paramRemoteViews.setInt(16909177, "setOriginalNotificationColor", i);
    }
    
    private void bindHeaderAppName(RemoteViews paramRemoteViews, boolean paramBoolean)
    {
      paramRemoteViews.setTextViewText(16908744, loadHeaderAppName());
      if ((isColorized()) && (!paramBoolean))
      {
        setTextViewColorPrimary(paramRemoteViews, 16908744);
      }
      else
      {
        int i;
        if (paramBoolean) {
          i = resolveAmbientColor();
        } else {
          i = getSecondaryTextColor();
        }
        paramRemoteViews.setTextColor(16908744, i);
      }
    }
    
    private void bindHeaderChronometerAndTime(RemoteViews paramRemoteViews)
    {
      if (showsTimeOrChronometer())
      {
        paramRemoteViews.setViewVisibility(16909472, 0);
        setTextViewColorSecondary(paramRemoteViews, 16909472);
        if (mN.extras.getBoolean("android.showChronometer"))
        {
          paramRemoteViews.setViewVisibility(16908854, 0);
          paramRemoteViews.setLong(16908854, "setBase", mN.when + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
          paramRemoteViews.setBoolean(16908854, "setStarted", true);
          paramRemoteViews.setChronometerCountDown(16908854, mN.extras.getBoolean("android.chronometerCountDown"));
          setTextViewColorSecondary(paramRemoteViews, 16908854);
        }
        else
        {
          paramRemoteViews.setViewVisibility(16909468, 0);
          paramRemoteViews.setLong(16909468, "setTime", mN.when);
          setTextViewColorSecondary(paramRemoteViews, 16909468);
        }
      }
      else
      {
        long l;
        if (mN.when != 0L) {
          l = mN.when;
        } else {
          l = mN.creationTime;
        }
        paramRemoteViews.setLong(16909468, "setTime", l);
      }
    }
    
    private void bindHeaderText(RemoteViews paramRemoteViews)
    {
      Object localObject1 = mN.extras.getCharSequence("android.subText");
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = localObject1;
        if (mStyle != null)
        {
          localObject2 = localObject1;
          if (mStyle.mSummaryTextSet)
          {
            localObject2 = localObject1;
            if (mStyle.hasSummaryInHeader()) {
              localObject2 = mStyle.mSummaryText;
            }
          }
        }
      }
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = localObject2;
        if (mContext.getApplicationInfo().targetSdkVersion < 24)
        {
          localObject1 = localObject2;
          if (mN.extras.getCharSequence("android.infoText") != null) {
            localObject1 = mN.extras.getCharSequence("android.infoText");
          }
        }
      }
      if (localObject1 != null)
      {
        paramRemoteViews.setTextViewText(16908998, processTextSpans(processLegacyText((CharSequence)localObject1)));
        setTextViewColorSecondary(paramRemoteViews, 16908998);
        paramRemoteViews.setViewVisibility(16908998, 0);
        paramRemoteViews.setViewVisibility(16908999, 0);
        setTextViewColorSecondary(paramRemoteViews, 16908999);
      }
    }
    
    private void bindHeaderTextSecondary(RemoteViews paramRemoteViews, CharSequence paramCharSequence)
    {
      if (!TextUtils.isEmpty(paramCharSequence))
      {
        paramRemoteViews.setTextViewText(16909000, processTextSpans(processLegacyText(paramCharSequence)));
        setTextViewColorSecondary(paramRemoteViews, 16909000);
        paramRemoteViews.setViewVisibility(16909000, 0);
        paramRemoteViews.setViewVisibility(16909001, 0);
        setTextViewColorSecondary(paramRemoteViews, 16909001);
      }
    }
    
    private boolean bindLargeIcon(RemoteViews paramRemoteViews, boolean paramBoolean)
    {
      if ((mN.mLargeIcon == null) && (mN.largeIcon != null)) {
        Notification.access$1102(mN, Icon.createWithBitmap(mN.largeIcon));
      }
      if ((mN.mLargeIcon != null) && (!paramBoolean)) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      if (paramBoolean)
      {
        paramRemoteViews.setViewVisibility(16909299, 0);
        paramRemoteViews.setImageViewIcon(16909299, mN.mLargeIcon);
        processLargeLegacyIcon(mN.mLargeIcon, paramRemoteViews);
      }
      return paramBoolean;
    }
    
    private void bindLargeIconAndReply(RemoteViews paramRemoteViews, Notification.StandardTemplateParams paramStandardTemplateParams, Notification.TemplateBindResult paramTemplateBindResult)
    {
      boolean bool1 = hideLargeIcon;
      boolean bool2 = true;
      int i = 0;
      if ((!bool1) && (!ambient)) {
        bool1 = false;
      } else {
        bool1 = true;
      }
      boolean bool3 = bindLargeIcon(paramRemoteViews, bool1);
      bool1 = bool2;
      if (!hideReplyIcon) {
        if (ambient) {
          bool1 = bool2;
        } else {
          bool1 = false;
        }
      }
      bool1 = bindReplyIcon(paramRemoteViews, bool1);
      int j = i;
      if (!bool3) {
        if (bool1) {
          j = i;
        } else {
          j = 8;
        }
      }
      paramRemoteViews.setViewVisibility(16909300, j);
      j = calculateMarginEnd(bool3, bool1);
      paramRemoteViews.setViewLayoutMarginEnd(16909087, j);
      paramRemoteViews.setViewLayoutMarginEnd(16909437, j);
      paramRemoteViews.setViewLayoutMarginEnd(16908301, j);
      if (paramTemplateBindResult != null) {
        paramTemplateBindResult.setIconMarginEnd(j);
      }
    }
    
    private void bindNotificationHeader(RemoteViews paramRemoteViews, boolean paramBoolean, CharSequence paramCharSequence)
    {
      bindSmallIcon(paramRemoteViews, paramBoolean);
      bindHeaderAppName(paramRemoteViews, paramBoolean);
      if (!paramBoolean)
      {
        bindHeaderText(paramRemoteViews);
        bindHeaderTextSecondary(paramRemoteViews, paramCharSequence);
        bindHeaderChronometerAndTime(paramRemoteViews);
        bindProfileBadge(paramRemoteViews);
      }
      bindActivePermissions(paramRemoteViews, paramBoolean);
      bindExpandButton(paramRemoteViews);
      Notification.access$1402(mN, true);
    }
    
    private void bindProfileBadge(RemoteViews paramRemoteViews)
    {
      Bitmap localBitmap = getProfileBadge();
      if (localBitmap != null)
      {
        paramRemoteViews.setImageViewBitmap(16909262, localBitmap);
        paramRemoteViews.setViewVisibility(16909262, 0);
        if (isColorized()) {
          paramRemoteViews.setDrawableTint(16909262, false, getPrimaryTextColor(), PorterDuff.Mode.SRC_ATOP);
        }
      }
    }
    
    private boolean bindReplyIcon(RemoteViews paramRemoteViews, boolean paramBoolean)
    {
      boolean bool = paramBoolean ^ true;
      Notification.Action localAction = null;
      int i = 0;
      paramBoolean = bool;
      if (bool)
      {
        localAction = findReplyAction();
        if (localAction != null) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
      }
      if (paramBoolean)
      {
        paramRemoteViews.setViewVisibility(16909289, 0);
        paramRemoteViews.setDrawableTint(16909289, false, getNeutralColor(), PorterDuff.Mode.SRC_ATOP);
        paramRemoteViews.setOnClickPendingIntent(16909289, actionIntent);
        paramRemoteViews.setRemoteInputs(16909289, mRemoteInputs);
      }
      else
      {
        paramRemoteViews.setRemoteInputs(16909289, null);
      }
      if (!paramBoolean) {
        i = 8;
      }
      paramRemoteViews.setViewVisibility(16909289, i);
      return paramBoolean;
    }
    
    private void bindSmallIcon(RemoteViews paramRemoteViews, boolean paramBoolean)
    {
      if ((mN.mSmallIcon == null) && (mN.icon != 0)) {
        Notification.access$1902(mN, Icon.createWithResource(mContext, mN.icon));
      }
      paramRemoteViews.setImageViewIcon(16908294, mN.mSmallIcon);
      paramRemoteViews.setInt(16908294, "setImageLevel", mN.iconLevel);
      processSmallIconColor(mN.mSmallIcon, paramRemoteViews, paramBoolean);
    }
    
    private int calculateMarginEnd(boolean paramBoolean1, boolean paramBoolean2)
    {
      int i = 0;
      int j = mContext.getResources().getDimensionPixelSize(17105317);
      int k = mContext.getResources().getDimensionPixelSize(17105352);
      if (paramBoolean2) {
        i = 0 + k - mContext.getResources().getDimensionPixelSize(17105351) * 2;
      }
      int m = i;
      if (paramBoolean1)
      {
        i += k;
        m = i;
        if (paramBoolean2) {
          m = i + j;
        }
      }
      if (!paramBoolean2)
      {
        i = m;
        if (!paramBoolean1) {}
      }
      else
      {
        i = m + j;
      }
      return i;
    }
    
    private CharSequence createSummaryText()
    {
      CharSequence localCharSequence1 = mN.extras.getCharSequence("android.title");
      if (USE_ONLY_TITLE_IN_LOW_PRIORITY_SUMMARY) {
        return localCharSequence1;
      }
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
      CharSequence localCharSequence2 = localCharSequence1;
      if (localCharSequence1 == null) {
        localCharSequence2 = mN.extras.getCharSequence("android.title.big");
      }
      BidiFormatter localBidiFormatter = BidiFormatter.getInstance();
      if (localCharSequence2 != null) {
        localSpannableStringBuilder.append(localBidiFormatter.unicodeWrap(localCharSequence2));
      }
      localCharSequence1 = mN.extras.getCharSequence("android.text");
      if ((localCharSequence2 != null) && (localCharSequence1 != null)) {
        localSpannableStringBuilder.append(localBidiFormatter.unicodeWrap(mContext.getText(17040512)));
      }
      if (localCharSequence1 != null) {
        localSpannableStringBuilder.append(localBidiFormatter.unicodeWrap(localCharSequence1));
      }
      return localSpannableStringBuilder;
    }
    
    private CharSequence ensureColorSpanContrast(CharSequence paramCharSequence, int paramInt, ColorStateList[] paramArrayOfColorStateList)
    {
      if ((paramCharSequence instanceof Spanned))
      {
        Spanned localSpanned = (Spanned)paramCharSequence;
        int i = localSpanned.length();
        int j = 0;
        Object[] arrayOfObject = localSpanned.getSpans(0, i, Object.class);
        SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(localSpanned.toString());
        int k = arrayOfObject.length;
        i = 0;
        while (i < k)
        {
          Object localObject1 = arrayOfObject[i];
          Object localObject2 = localObject1;
          int m = localSpanned.getSpanStart(localObject1);
          int n = localSpanned.getSpanEnd(localObject1);
          if (n - m == paramCharSequence.length()) {
            j = 1;
          }
          Object localObject3 = localObject2;
          if ((localObject2 instanceof CharacterStyle)) {
            localObject3 = ((CharacterStyle)localObject1).getUnderlying();
          }
          int i1;
          if ((localObject3 instanceof TextAppearanceSpan))
          {
            TextAppearanceSpan localTextAppearanceSpan = (TextAppearanceSpan)localObject3;
            localObject2 = localTextAppearanceSpan.getTextColor();
            if (localObject2 != null)
            {
              localObject3 = ((ColorStateList)localObject2).getColors();
              int[] arrayOfInt = new int[localObject3.length];
              for (i1 = 0;; i1++)
              {
                int i2 = arrayOfInt.length;
                if (i1 >= i2) {
                  break;
                }
                arrayOfInt[i1] = NotificationColorUtil.ensureLargeTextContrast(localObject3[i1], paramInt, mInNightMode);
              }
              localObject2 = new ColorStateList((int[][])((ColorStateList)localObject2).getStates().clone(), arrayOfInt);
              localObject3 = localObject2;
              if (j != 0)
              {
                paramArrayOfColorStateList[0] = localObject2;
                localObject3 = null;
              }
              localObject3 = new TextAppearanceSpan(localTextAppearanceSpan.getFamily(), localTextAppearanceSpan.getTextStyle(), localTextAppearanceSpan.getTextSize(), (ColorStateList)localObject3, localTextAppearanceSpan.getLinkTextColor());
            }
          }
          else if ((localObject3 instanceof ForegroundColorSpan))
          {
            i1 = NotificationColorUtil.ensureLargeTextContrast(((ForegroundColorSpan)localObject3).getForegroundColor(), paramInt, mInNightMode);
            if (j != 0)
            {
              paramArrayOfColorStateList[0] = ColorStateList.valueOf(i1);
              localObject3 = null;
            }
            else
            {
              localObject3 = new ForegroundColorSpan(i1);
            }
          }
          else
          {
            localObject3 = localObject1;
          }
          if (localObject3 != null) {
            localSpannableStringBuilder.setSpan(localObject3, m, n, localSpanned.getSpanFlags(localObject1));
          }
          i++;
          j = 0;
        }
        return localSpannableStringBuilder;
      }
      return paramCharSequence;
    }
    
    private void ensureColors()
    {
      int i = getBackgroundColor();
      if ((mPrimaryTextColor == 1) || (mSecondaryTextColor == 1) || (mTextColorsAreForBackground != i))
      {
        mTextColorsAreForBackground = i;
        if ((hasForegroundColor()) && (isColorized()))
        {
          double d1 = NotificationColorUtil.calculateLuminance(i);
          double d2 = NotificationColorUtil.calculateLuminance(mForegroundColor);
          double d3 = NotificationColorUtil.calculateContrast(mForegroundColor, i);
          int j;
          if (((d1 > d2) && (NotificationColorUtil.satisfiesTextContrast(i, -16777216))) || ((d1 <= d2) && (!NotificationColorUtil.satisfiesTextContrast(i, -1)))) {
            j = 1;
          } else {
            j = 0;
          }
          int k = 10;
          if (d3 < 4.5D)
          {
            if (j != 0)
            {
              mSecondaryTextColor = NotificationColorUtil.findContrastColor(mForegroundColor, i, true, 4.5D);
              mPrimaryTextColor = NotificationColorUtil.changeColorLightness(mSecondaryTextColor, -20);
            }
            else
            {
              mSecondaryTextColor = NotificationColorUtil.findContrastColorAgainstDark(mForegroundColor, i, true, 4.5D);
              mPrimaryTextColor = NotificationColorUtil.changeColorLightness(mSecondaryTextColor, 10);
            }
          }
          else
          {
            mPrimaryTextColor = mForegroundColor;
            int m = mPrimaryTextColor;
            int n;
            if (j != 0) {
              n = 20;
            } else {
              n = -10;
            }
            mSecondaryTextColor = NotificationColorUtil.changeColorLightness(m, n);
            if (NotificationColorUtil.calculateContrast(mSecondaryTextColor, i) < 4.5D)
            {
              if (j != 0) {
                mSecondaryTextColor = NotificationColorUtil.findContrastColor(mSecondaryTextColor, i, true, 4.5D);
              } else {
                mSecondaryTextColor = NotificationColorUtil.findContrastColorAgainstDark(mSecondaryTextColor, i, true, 4.5D);
              }
              n = mSecondaryTextColor;
              if (j != 0) {
                j = -20;
              } else {
                j = k;
              }
              mPrimaryTextColor = NotificationColorUtil.changeColorLightness(n, j);
            }
          }
        }
        else
        {
          mPrimaryTextColor = NotificationColorUtil.resolvePrimaryColor(mContext, i);
          mSecondaryTextColor = NotificationColorUtil.resolveSecondaryColor(mContext, i);
          if ((i != 0) && (isColorized()))
          {
            mPrimaryTextColor = NotificationColorUtil.findAlphaToMeetContrast(mPrimaryTextColor, i, 4.5D);
            mSecondaryTextColor = NotificationColorUtil.findAlphaToMeetContrast(mSecondaryTextColor, i, 4.5D);
          }
        }
      }
    }
    
    private Notification.Action findReplyAction()
    {
      ArrayList localArrayList = mActions;
      if (mOriginalActions != null) {
        localArrayList = mOriginalActions;
      }
      int i = localArrayList.size();
      for (int j = 0; j < i; j++)
      {
        Notification.Action localAction = (Notification.Action)localArrayList.get(j);
        if (hasValidRemoteInput(localAction)) {
          return localAction;
        }
      }
      return null;
    }
    
    private RemoteViews generateActionButton(Notification.Action paramAction, boolean paramBoolean1, boolean paramBoolean2)
    {
      Object localObject = actionIntent;
      boolean bool = true;
      int i;
      if (localObject == null) {
        i = 1;
      } else {
        i = 0;
      }
      localObject = mContext.getApplicationInfo();
      int j;
      if (paramBoolean1) {
        j = getEmphasizedActionLayoutResource();
      } else if (i != 0) {
        j = getActionTombstoneLayoutResource();
      } else {
        j = getActionLayoutResource();
      }
      Notification.BuilderRemoteViews localBuilderRemoteViews = new Notification.BuilderRemoteViews((ApplicationInfo)localObject, j);
      if (i == 0) {
        localBuilderRemoteViews.setOnClickPendingIntent(16908686, actionIntent);
      }
      localBuilderRemoteViews.setContentDescription(16908686, title);
      if (mRemoteInputs != null) {
        localBuilderRemoteViews.setRemoteInputs(16908686, mRemoteInputs);
      }
      if (paramBoolean1)
      {
        paramAction = title;
        localObject = null;
        int k = resolveBackgroundColor();
        if (isLegacy())
        {
          paramAction = NotificationColorUtil.clearColorSpans(paramAction);
        }
        else
        {
          localObject = new ColorStateList[1];
          paramAction = ensureColorSpanContrast(paramAction, k, (ColorStateList[])localObject);
        }
        localBuilderRemoteViews.setTextViewText(16908686, processTextSpans(paramAction));
        setTextViewColorPrimary(localBuilderRemoteViews, 16908686);
        if ((localObject != null) && (localObject[0] != null)) {
          i = 1;
        } else {
          i = 0;
        }
        if (i != 0)
        {
          k = localObject[0].getDefaultColor();
          j = NotificationColorUtil.resolvePrimaryColor(mContext, k);
          localBuilderRemoteViews.setTextColor(16908686, j);
        }
        else if ((mN.color != 0) && (!isColorized()) && (mTintActionButtons))
        {
          j = resolveContrastColor();
          localBuilderRemoteViews.setTextColor(16908686, j);
        }
        else
        {
          j = getPrimaryTextColor();
        }
        localBuilderRemoteViews.setColorStateList(16908686, "setRippleColor", ColorStateList.valueOf(0xFFFFFF & j | 0x33000000));
        localBuilderRemoteViews.setColorStateList(16908686, "setButtonBackground", ColorStateList.valueOf(k));
        if (i == 0) {
          paramBoolean1 = bool;
        } else {
          paramBoolean1 = false;
        }
        localBuilderRemoteViews.setBoolean(16908686, "setHasStroke", paramBoolean1);
      }
      else
      {
        localBuilderRemoteViews.setTextViewText(16908686, processTextSpans(processLegacyText(title)));
        if ((isColorized()) && (!paramBoolean2))
        {
          setTextViewColorPrimary(localBuilderRemoteViews, 16908686);
        }
        else if ((mN.color != 0) && (mTintActionButtons))
        {
          if (paramBoolean2) {
            j = resolveAmbientColor();
          } else {
            j = resolveContrastColor();
          }
          localBuilderRemoteViews.setTextColor(16908686, j);
        }
      }
      return localBuilderRemoteViews;
    }
    
    private int getActionLayoutResource()
    {
      return 17367210;
    }
    
    private int getActionTombstoneLayoutResource()
    {
      return 17367213;
    }
    
    private Bundle getAllExtras()
    {
      Bundle localBundle = (Bundle)mUserExtras.clone();
      localBundle.putAll(mN.extras);
      return localBundle;
    }
    
    private int getBackgroundColor()
    {
      if (isColorized())
      {
        int i;
        if (mBackgroundColor != 1) {
          i = mBackgroundColor;
        } else {
          i = mN.color;
        }
        return i;
      }
      return 0;
    }
    
    private int getBaseLayoutResource()
    {
      return 17367219;
    }
    
    private int getBigBaseLayoutResource()
    {
      return 17367220;
    }
    
    private int getBigPictureLayoutResource()
    {
      return 17367222;
    }
    
    private int getBigTextLayoutResource()
    {
      return 17367223;
    }
    
    private NotificationColorUtil getColorUtil()
    {
      if (mColorUtil == null) {
        mColorUtil = NotificationColorUtil.getInstance(mContext);
      }
      return mColorUtil;
    }
    
    private int getEmphasizedActionLayoutResource()
    {
      return 17367211;
    }
    
    private int getInboxLayoutResource()
    {
      return 17367224;
    }
    
    private int getMessagingLayoutResource()
    {
      return 17367226;
    }
    
    private int getNeutralColor()
    {
      if (isColorized()) {
        return getSecondaryTextColor();
      }
      return resolveNeutralColor();
    }
    
    private Bitmap getProfileBadge()
    {
      Drawable localDrawable = getProfileBadgeDrawable();
      if (localDrawable == null) {
        return null;
      }
      int i = mContext.getResources().getDimensionPixelSize(17105310);
      Bitmap localBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      localDrawable.setBounds(0, 0, i, i);
      localDrawable.draw(localCanvas);
      return localBitmap;
    }
    
    private Drawable getProfileBadgeDrawable()
    {
      if (mContext.getUserId() == 0) {
        return null;
      }
      return mContext.getPackageManager().getUserBadgeForDensityNoBackground(new UserHandle(mContext.getUserId()), 0);
    }
    
    private boolean handleProgressBar(boolean paramBoolean, RemoteViews paramRemoteViews, Bundle paramBundle)
    {
      int i = paramBundle.getInt("android.progressMax", 0);
      int j = paramBundle.getInt("android.progress", 0);
      boolean bool = paramBundle.getBoolean("android.progressIndeterminate");
      if ((paramBoolean) && ((i != 0) || (bool)))
      {
        paramRemoteViews.setViewVisibility(16908301, 0);
        paramRemoteViews.setProgressBar(16908301, i, j, bool);
        paramRemoteViews.setProgressBackgroundTintList(16908301, ColorStateList.valueOf(mContext.getColor(17170780)));
        if (mN.color != 0)
        {
          paramBundle = ColorStateList.valueOf(resolveContrastColor());
          paramRemoteViews.setProgressTintList(16908301, paramBundle);
          paramRemoteViews.setProgressIndeterminateTintList(16908301, paramBundle);
        }
        return true;
      }
      paramRemoteViews.setViewVisibility(16908301, 8);
      return false;
    }
    
    private boolean hasForegroundColor()
    {
      int i = mForegroundColor;
      boolean bool = true;
      if (i == 1) {
        bool = false;
      }
      return bool;
    }
    
    private boolean hasValidRemoteInput(Notification.Action paramAction)
    {
      if ((!TextUtils.isEmpty(title)) && (actionIntent != null))
      {
        RemoteInput[] arrayOfRemoteInput = paramAction.getRemoteInputs();
        if (arrayOfRemoteInput == null) {
          return false;
        }
        int i = arrayOfRemoteInput.length;
        int j = 0;
        while (j < i)
        {
          paramAction = arrayOfRemoteInput[j];
          CharSequence[] arrayOfCharSequence = paramAction.getChoices();
          if ((!paramAction.getAllowFreeFormInput()) && ((arrayOfCharSequence == null) || (arrayOfCharSequence.length == 0))) {
            j++;
          } else {
            return true;
          }
        }
        return false;
      }
      return false;
    }
    
    private void hideLine1Text(RemoteViews paramRemoteViews)
    {
      if (paramRemoteViews != null) {
        paramRemoteViews.setViewVisibility(16909465, 8);
      }
    }
    
    private boolean isColorized()
    {
      return mN.isColorized();
    }
    
    private boolean isLegacy()
    {
      if (!mIsLegacyInitialized)
      {
        boolean bool;
        if (mContext.getApplicationInfo().targetSdkVersion < 21) {
          bool = true;
        } else {
          bool = false;
        }
        mIsLegacy = bool;
        mIsLegacyInitialized = true;
      }
      return mIsLegacy;
    }
    
    public static void makeHeaderExpanded(RemoteViews paramRemoteViews)
    {
      if (paramRemoteViews != null) {
        paramRemoteViews.setBoolean(16909177, "setExpanded", true);
      }
    }
    
    private RemoteViews makePublicView(boolean paramBoolean)
    {
      if (mN.publicVersion != null)
      {
        localObject = recoverBuilder(mContext, mN.publicVersion);
        if (paramBoolean) {
          localObject = ((Builder)localObject).makeAmbientNotification();
        } else {
          localObject = ((Builder)localObject).createContentView();
        }
        return localObject;
      }
      Bundle localBundle = mN.extras;
      Notification.Style localStyle = mStyle;
      mStyle = null;
      Icon localIcon = mN.mLargeIcon;
      Notification.access$1102(mN, null);
      Bitmap localBitmap = mN.largeIcon;
      mN.largeIcon = null;
      ArrayList localArrayList = mActions;
      mActions = new ArrayList();
      Object localObject = new Bundle();
      ((Bundle)localObject).putBoolean("android.showWhen", localBundle.getBoolean("android.showWhen"));
      ((Bundle)localObject).putBoolean("android.showChronometer", localBundle.getBoolean("android.showChronometer"));
      ((Bundle)localObject).putBoolean("android.chronometerCountDown", localBundle.getBoolean("android.chronometerCountDown"));
      String str = localBundle.getString("android.substName");
      if (str != null) {
        ((Bundle)localObject).putString("android.substName", str);
      }
      mN.extras = ((Bundle)localObject);
      if (paramBoolean)
      {
        ((Bundle)localObject).putCharSequence("android.title", mContext.getString(17040513));
        localObject = makeAmbientNotification();
      }
      else
      {
        localObject = makeNotificationHeader(false);
        ((RemoteViews)localObject).setBoolean(16909177, "setExpandOnlyOnButton", true);
      }
      mN.extras = localBundle;
      Notification.access$1102(mN, localIcon);
      mN.largeIcon = localBitmap;
      mActions = localArrayList;
      mStyle = localStyle;
      return localObject;
    }
    
    public static Notification maybeCloneStrippedForDelivery(Notification paramNotification, boolean paramBoolean, Context paramContext)
    {
      String str = extras.getString("android.template");
      if ((!paramBoolean) && (!TextUtils.isEmpty(str)) && (Notification.getNotificationStyleClass(str) == null)) {
        return paramNotification;
      }
      boolean bool = contentView instanceof Notification.BuilderRemoteViews;
      int i = 0;
      int j;
      if ((bool) && (extras.getInt("android.rebuild.contentViewActionCount", -1) == contentView.getSequenceNumber())) {
        j = 1;
      } else {
        j = 0;
      }
      int k;
      if (((bigContentView instanceof Notification.BuilderRemoteViews)) && (extras.getInt("android.rebuild.bigViewActionCount", -1) == bigContentView.getSequenceNumber())) {
        k = 1;
      } else {
        k = 0;
      }
      int m = i;
      if ((headsUpContentView instanceof Notification.BuilderRemoteViews))
      {
        m = i;
        if (extras.getInt("android.rebuild.hudViewActionCount", -1) == headsUpContentView.getSequenceNumber()) {
          m = 1;
        }
      }
      if ((!paramBoolean) && (j == 0) && (k == 0) && (m == 0)) {
        return paramNotification;
      }
      paramNotification = paramNotification.clone();
      if (j != 0)
      {
        contentView = null;
        extras.remove("android.rebuild.contentViewActionCount");
      }
      if (k != 0)
      {
        bigContentView = null;
        extras.remove("android.rebuild.bigViewActionCount");
      }
      if (m != 0)
      {
        headsUpContentView = null;
        extras.remove("android.rebuild.hudViewActionCount");
      }
      if ((paramBoolean) && (paramContext.getResources().getStringArray(17235977).length == 0))
      {
        extras.remove("android.tv.EXTENSIONS");
        extras.remove("android.wearable.EXTENSIONS");
        extras.remove("android.car.EXTENSIONS");
      }
      return paramNotification;
    }
    
    private void processLargeLegacyIcon(Icon paramIcon, RemoteViews paramRemoteViews)
    {
      if ((paramIcon != null) && (isLegacy()) && (getColorUtil().isGrayscaleIcon(mContext, paramIcon))) {
        paramRemoteViews.setDrawableTint(16908294, false, resolveContrastColor(), PorterDuff.Mode.SRC_ATOP);
      }
    }
    
    private CharSequence processLegacyText(CharSequence paramCharSequence)
    {
      return processLegacyText(paramCharSequence, false);
    }
    
    private CharSequence processLegacyText(CharSequence paramCharSequence, boolean paramBoolean)
    {
      boolean bool;
      if ((!isLegacy()) && (!textColorsNeedInversion())) {
        bool = false;
      } else {
        bool = true;
      }
      if (bool != paramBoolean) {
        return getColorUtil().invertCharSequenceColors(paramCharSequence);
      }
      return paramCharSequence;
    }
    
    private void processSmallIconColor(Icon paramIcon, RemoteViews paramRemoteViews, boolean paramBoolean)
    {
      boolean bool = isLegacy();
      int i = 1;
      int j;
      if ((bool) && (!getColorUtil().isGrayscaleIcon(mContext, paramIcon))) {
        j = 0;
      } else {
        j = 1;
      }
      int k;
      if (paramBoolean) {
        k = resolveAmbientColor();
      }
      for (;;)
      {
        break;
        if (isColorized()) {
          k = getPrimaryTextColor();
        } else {
          k = resolveContrastColor();
        }
      }
      if (j != 0) {
        paramRemoteViews.setDrawableTint(16908294, false, k, PorterDuff.Mode.SRC_ATOP);
      }
      if (j == 0) {
        k = i;
      }
      paramRemoteViews.setInt(16909177, "setOriginalIconColor", k);
    }
    
    private CharSequence processTextSpans(CharSequence paramCharSequence)
    {
      if (hasForegroundColor()) {
        return NotificationColorUtil.clearColorSpans(paramCharSequence);
      }
      return paramCharSequence;
    }
    
    public static Builder recoverBuilder(Context paramContext, Notification paramNotification)
    {
      ApplicationInfo localApplicationInfo = (ApplicationInfo)extras.getParcelable("android.appInfo");
      if (localApplicationInfo != null) {
        try
        {
          Context localContext = paramContext.createApplicationContext(localApplicationInfo, 4);
          paramContext = localContext;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          for (;;)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("ApplicationInfo ");
            localStringBuilder.append(localApplicationInfo);
            localStringBuilder.append(" not found");
            Log.e("Notification", localStringBuilder.toString());
          }
        }
      }
      return new Builder(paramContext, paramNotification);
    }
    
    private void resetNotificationHeader(RemoteViews paramRemoteViews)
    {
      paramRemoteViews.setBoolean(16909177, "setExpanded", false);
      paramRemoteViews.setTextViewText(16908744, null);
      paramRemoteViews.setViewVisibility(16908854, 8);
      paramRemoteViews.setViewVisibility(16908998, 8);
      paramRemoteViews.setTextViewText(16908998, null);
      paramRemoteViews.setViewVisibility(16909000, 8);
      paramRemoteViews.setTextViewText(16909000, null);
      paramRemoteViews.setViewVisibility(16908999, 8);
      paramRemoteViews.setViewVisibility(16909001, 8);
      paramRemoteViews.setViewVisibility(16909472, 8);
      paramRemoteViews.setViewVisibility(16909468, 8);
      paramRemoteViews.setImageViewIcon(16909262, null);
      paramRemoteViews.setViewVisibility(16909262, 8);
      Notification.access$1402(mN, false);
    }
    
    private void resetStandardTemplate(RemoteViews paramRemoteViews)
    {
      resetNotificationHeader(paramRemoteViews);
      paramRemoteViews.setViewVisibility(16909299, 8);
      paramRemoteViews.setViewVisibility(16908310, 8);
      paramRemoteViews.setTextViewText(16908310, null);
      paramRemoteViews.setViewVisibility(16909437, 8);
      paramRemoteViews.setTextViewText(16909437, null);
      paramRemoteViews.setViewVisibility(16909465, 8);
      paramRemoteViews.setTextViewText(16909465, null);
    }
    
    private void resetStandardTemplateWithActions(RemoteViews paramRemoteViews)
    {
      paramRemoteViews.setViewVisibility(16908709, 8);
      paramRemoteViews.removeAllViews(16908709);
      paramRemoteViews.setViewVisibility(16909179, 8);
      paramRemoteViews.setTextViewText(16909181, null);
      paramRemoteViews.setViewVisibility(16909182, 8);
      paramRemoteViews.setViewVisibility(16909180, 8);
      paramRemoteViews.setViewVisibility(16909183, 8);
      paramRemoteViews.setTextViewText(16909183, null);
      paramRemoteViews.setViewVisibility(16909184, 8);
      paramRemoteViews.setTextViewText(16909184, null);
      paramRemoteViews.setViewLayoutMarginBottomDimen(16909175, 17105316);
    }
    
    private int resolveBackgroundColor()
    {
      int i = getBackgroundColor();
      int j = i;
      if (i == 0) {
        j = mContext.getColor(17170777);
      }
      return j;
    }
    
    private void sanitizeColor()
    {
      if (mN.color != 0)
      {
        Notification localNotification = mN;
        color |= 0xFF000000;
      }
    }
    
    private void setTextViewColorPrimary(RemoteViews paramRemoteViews, int paramInt)
    {
      ensureColors();
      paramRemoteViews.setTextColor(paramInt, mPrimaryTextColor);
    }
    
    private void setTextViewColorSecondary(RemoteViews paramRemoteViews, int paramInt)
    {
      ensureColors();
      paramRemoteViews.setTextColor(paramInt, mSecondaryTextColor);
    }
    
    private boolean shouldTintActionButtons()
    {
      return mTintActionButtons;
    }
    
    private boolean showsTimeOrChronometer()
    {
      boolean bool;
      if ((!mN.showsTime()) && (!mN.showsChronometer())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    private boolean textColorsNeedInversion()
    {
      Notification.Style localStyle = mStyle;
      boolean bool1 = false;
      if ((localStyle != null) && (Notification.MediaStyle.class.equals(mStyle.getClass())))
      {
        int i = mContext.getApplicationInfo().targetSdkVersion;
        boolean bool2 = bool1;
        if (i > 23)
        {
          bool2 = bool1;
          if (i < 26) {
            bool2 = true;
          }
        }
        return bool2;
      }
      return false;
    }
    
    private void updateBackgroundColor(RemoteViews paramRemoteViews)
    {
      if (isColorized()) {
        paramRemoteViews.setInt(16909411, "setBackgroundColor", getBackgroundColor());
      } else {
        paramRemoteViews.setInt(16909411, "setBackgroundResource", 0);
      }
    }
    
    private boolean useExistingRemoteView()
    {
      boolean bool;
      if ((mStyle != null) && ((mStyle.displayCustomViewInline()) || (mRebuildStyledRemoteViews))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    @Deprecated
    public Builder addAction(int paramInt, CharSequence paramCharSequence, PendingIntent paramPendingIntent)
    {
      mActions.add(new Notification.Action(paramInt, Notification.safeCharSequence(paramCharSequence), paramPendingIntent));
      return this;
    }
    
    public Builder addAction(Notification.Action paramAction)
    {
      if (paramAction != null) {
        mActions.add(paramAction);
      }
      return this;
    }
    
    public Builder addExtras(Bundle paramBundle)
    {
      if (paramBundle != null) {
        mUserExtras.putAll(paramBundle);
      }
      return this;
    }
    
    public Builder addPerson(Person paramPerson)
    {
      mPersonList.add(paramPerson);
      return this;
    }
    
    public Builder addPerson(String paramString)
    {
      addPerson(new Person.Builder().setUri(paramString).build());
      return this;
    }
    
    public Notification build()
    {
      if (mUserExtras != null) {
        mN.extras = getAllExtras();
      }
      Notification.access$1802(mN, System.currentTimeMillis());
      Notification.addFieldsFromContext(mContext, mN);
      buildUnstyled();
      if (mStyle != null)
      {
        mStyle.reduceImageSizes(mContext);
        mStyle.purgeResources();
        mStyle.validate(mContext);
        mStyle.buildStyled(mN);
      }
      mN.reduceImageSizes(mContext);
      if ((mContext.getApplicationInfo().targetSdkVersion < 24) && (useExistingRemoteView()))
      {
        if (mN.contentView == null)
        {
          mN.contentView = createContentView();
          mN.extras.putInt("android.rebuild.contentViewActionCount", mN.contentView.getSequenceNumber());
        }
        if (mN.bigContentView == null)
        {
          mN.bigContentView = createBigContentView();
          if (mN.bigContentView != null) {
            mN.extras.putInt("android.rebuild.bigViewActionCount", mN.bigContentView.getSequenceNumber());
          }
        }
        if (mN.headsUpContentView == null)
        {
          mN.headsUpContentView = createHeadsUpContentView();
          if (mN.headsUpContentView != null) {
            mN.extras.putInt("android.rebuild.hudViewActionCount", mN.headsUpContentView.getSequenceNumber());
          }
        }
      }
      if ((mN.defaults & 0x4) != 0)
      {
        Notification localNotification = mN;
        flags |= 0x1;
      }
      mN.allPendingIntents = null;
      return mN;
    }
    
    public Notification buildInto(Notification paramNotification)
    {
      build().cloneInto(paramNotification, true);
      return paramNotification;
    }
    
    public Notification buildUnstyled()
    {
      if (mActions.size() > 0)
      {
        mN.actions = new Notification.Action[mActions.size()];
        mActions.toArray(mN.actions);
      }
      if (!mPersonList.isEmpty()) {
        mN.extras.putParcelableArrayList("android.people.list", mPersonList);
      }
      if ((mN.bigContentView != null) || (mN.contentView != null) || (mN.headsUpContentView != null)) {
        mN.extras.putBoolean("android.contains.customView", true);
      }
      return mN;
    }
    
    public RemoteViews createBigContentView()
    {
      RemoteViews localRemoteViews = null;
      if ((mN.bigContentView != null) && (useExistingRemoteView())) {
        return mN.bigContentView;
      }
      if (mStyle != null)
      {
        localRemoteViews = mStyle.makeBigContentView();
        hideLine1Text(localRemoteViews);
      }
      else if (mActions.size() != 0)
      {
        localRemoteViews = applyStandardTemplateWithActions(getBigBaseLayoutResource(), null);
      }
      makeHeaderExpanded(localRemoteViews);
      return localRemoteViews;
    }
    
    public RemoteViews createContentView()
    {
      return createContentView(false);
    }
    
    public RemoteViews createContentView(boolean paramBoolean)
    {
      if ((mN.contentView != null) && (useExistingRemoteView())) {
        return mN.contentView;
      }
      if (mStyle != null)
      {
        RemoteViews localRemoteViews = mStyle.makeContentView(paramBoolean);
        if (localRemoteViews != null) {
          return localRemoteViews;
        }
      }
      return applyStandardTemplate(getBaseLayoutResource(), null);
    }
    
    public RemoteViews createHeadsUpContentView()
    {
      return createHeadsUpContentView(false);
    }
    
    public RemoteViews createHeadsUpContentView(boolean paramBoolean)
    {
      if ((mN.headsUpContentView != null) && (useExistingRemoteView())) {
        return mN.headsUpContentView;
      }
      if (mStyle != null)
      {
        localObject = mStyle.makeHeadsUpContentView(paramBoolean);
        if (localObject != null) {
          return localObject;
        }
      }
      else if (mActions.size() == 0)
      {
        return null;
      }
      Object localObject = mParams.reset().fillTextsFrom(this).setMaxRemoteInputHistory(1);
      return applyStandardTemplateWithActions(getBigBaseLayoutResource(), (Notification.StandardTemplateParams)localObject, null);
    }
    
    public Builder extend(Notification.Extender paramExtender)
    {
      paramExtender.extend(this);
      return this;
    }
    
    public Bundle getExtras()
    {
      return mUserExtras;
    }
    
    public CharSequence getHeadsUpStatusBarText(boolean paramBoolean)
    {
      if ((mStyle != null) && (!paramBoolean))
      {
        CharSequence localCharSequence = mStyle.getHeadsUpStatusBarText();
        if (!TextUtils.isEmpty(localCharSequence)) {
          return localCharSequence;
        }
      }
      return loadHeaderAppName();
    }
    
    @Deprecated
    public Notification getNotification()
    {
      return build();
    }
    
    @VisibleForTesting
    public int getPrimaryTextColor()
    {
      ensureColors();
      return mPrimaryTextColor;
    }
    
    @VisibleForTesting
    public int getSecondaryTextColor()
    {
      ensureColors();
      return mSecondaryTextColor;
    }
    
    public Notification.Style getStyle()
    {
      return mStyle;
    }
    
    public String loadHeaderAppName()
    {
      Object localObject1 = null;
      PackageManager localPackageManager = mContext.getPackageManager();
      Object localObject2 = localObject1;
      if (mN.extras.containsKey("android.substName"))
      {
        String str = mContext.getPackageName();
        localObject2 = mN.extras.getString("android.substName");
        if (localPackageManager.checkPermission("android.permission.SUBSTITUTE_NOTIFICATION_APP_NAME", str) != 0)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("warning: pkg ");
          localStringBuilder.append(str);
          localStringBuilder.append(" attempting to substitute app name '");
          localStringBuilder.append((String)localObject2);
          localStringBuilder.append("' without holding perm ");
          localStringBuilder.append("android.permission.SUBSTITUTE_NOTIFICATION_APP_NAME");
          Log.w("Notification", localStringBuilder.toString());
          localObject2 = localObject1;
        }
      }
      localObject1 = localObject2;
      if (TextUtils.isEmpty((CharSequence)localObject2)) {
        localObject1 = localPackageManager.getApplicationLabel(mContext.getApplicationInfo());
      }
      if (TextUtils.isEmpty((CharSequence)localObject1)) {
        return null;
      }
      return String.valueOf(localObject1);
    }
    
    public RemoteViews makeAmbientNotification()
    {
      return applyStandardTemplateWithActions(17367218, mParams.reset().ambient(true).fillTextsFrom(this).hasProgress(false), null);
    }
    
    public RemoteViews makeLowPriorityContentView(boolean paramBoolean)
    {
      int i = mN.color;
      mN.color = 0;
      CharSequence localCharSequence = mN.extras.getCharSequence("android.subText");
      if ((!paramBoolean) || (TextUtils.isEmpty(localCharSequence)))
      {
        localObject = createSummaryText();
        if (!TextUtils.isEmpty((CharSequence)localObject)) {
          mN.extras.putCharSequence("android.subText", (CharSequence)localObject);
        }
      }
      Object localObject = makeNotificationHeader(false);
      ((RemoteViews)localObject).setBoolean(16909177, "setAcceptAllTouches", true);
      if (localCharSequence != null) {
        mN.extras.putCharSequence("android.subText", localCharSequence);
      } else {
        mN.extras.remove("android.subText");
      }
      mN.color = i;
      return localObject;
    }
    
    public RemoteViews makeNotificationHeader(boolean paramBoolean)
    {
      Boolean localBoolean = (Boolean)mN.extras.get("android.colorized");
      mN.extras.putBoolean("android.colorized", false);
      Object localObject = mContext.getApplicationInfo();
      int i;
      if (paramBoolean) {
        i = 17367216;
      } else {
        i = 17367217;
      }
      localObject = new Notification.BuilderRemoteViews((ApplicationInfo)localObject, i);
      resetNotificationHeader((RemoteViews)localObject);
      bindNotificationHeader((RemoteViews)localObject, paramBoolean, null);
      if (localBoolean != null) {
        mN.extras.putBoolean("android.colorized", localBoolean.booleanValue());
      } else {
        mN.extras.remove("android.colorized");
      }
      return localObject;
    }
    
    public RemoteViews makePublicAmbientNotification()
    {
      return makePublicView(true);
    }
    
    public RemoteViews makePublicContentView()
    {
      return makePublicView(false);
    }
    
    int resolveAmbientColor()
    {
      if ((mCachedAmbientColorIsFor == mN.color) && (mCachedAmbientColorIsFor != 1)) {
        return mCachedAmbientColor;
      }
      int i = NotificationColorUtil.resolveAmbientColor(mContext, mN.color);
      mCachedAmbientColorIsFor = mN.color;
      mCachedAmbientColor = i;
      return i;
    }
    
    int resolveContrastColor()
    {
      if ((mCachedContrastColorIsFor == mN.color) && (mCachedContrastColor != 1)) {
        return mCachedContrastColor;
      }
      int i = mContext.getColor(17170777);
      int j;
      if (mN.color == 0)
      {
        ensureColors();
        j = NotificationColorUtil.resolveDefaultColor(mContext, i);
      }
      else
      {
        j = NotificationColorUtil.resolveContrastColor(mContext, mN.color, i, mInNightMode);
      }
      int k = j;
      if (Color.alpha(j) < 255) {
        k = NotificationColorUtil.compositeColors(j, i);
      }
      mCachedContrastColorIsFor = mN.color;
      mCachedContrastColor = k;
      return k;
    }
    
    int resolveNeutralColor()
    {
      if (mNeutralColor != 1) {
        return mNeutralColor;
      }
      int i = mContext.getColor(17170777);
      mNeutralColor = NotificationColorUtil.resolveDefaultColor(mContext, i);
      if (Color.alpha(mNeutralColor) < 255) {
        mNeutralColor = NotificationColorUtil.compositeColors(mNeutralColor, i);
      }
      return mNeutralColor;
    }
    
    public Builder setActions(Notification.Action... paramVarArgs)
    {
      mActions.clear();
      for (int i = 0; i < paramVarArgs.length; i++) {
        if (paramVarArgs[i] != null) {
          mActions.add(paramVarArgs[i]);
        }
      }
      return this;
    }
    
    public Builder setAutoCancel(boolean paramBoolean)
    {
      setFlag(16, paramBoolean);
      return this;
    }
    
    public Builder setBadgeIconType(int paramInt)
    {
      Notification.access$702(mN, paramInt);
      return this;
    }
    
    public Builder setCategory(String paramString)
    {
      mN.category = paramString;
      return this;
    }
    
    @Deprecated
    public Builder setChannel(String paramString)
    {
      Notification.access$502(mN, paramString);
      return this;
    }
    
    public Builder setChannelId(String paramString)
    {
      Notification.access$502(mN, paramString);
      return this;
    }
    
    public Builder setChronometerCountDown(boolean paramBoolean)
    {
      mN.extras.putBoolean("android.chronometerCountDown", paramBoolean);
      return this;
    }
    
    public Builder setColor(int paramInt)
    {
      mN.color = paramInt;
      sanitizeColor();
      return this;
    }
    
    public void setColorPalette(int paramInt1, int paramInt2)
    {
      mBackgroundColor = paramInt1;
      mForegroundColor = paramInt2;
      mTextColorsAreForBackground = 1;
      ensureColors();
    }
    
    public Builder setColorized(boolean paramBoolean)
    {
      mN.extras.putBoolean("android.colorized", paramBoolean);
      return this;
    }
    
    @Deprecated
    public Builder setContent(RemoteViews paramRemoteViews)
    {
      return setCustomContentView(paramRemoteViews);
    }
    
    @Deprecated
    public Builder setContentInfo(CharSequence paramCharSequence)
    {
      mN.extras.putCharSequence("android.infoText", Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public Builder setContentIntent(PendingIntent paramPendingIntent)
    {
      mN.contentIntent = paramPendingIntent;
      return this;
    }
    
    void setContentMinHeight(RemoteViews paramRemoteViews, boolean paramBoolean)
    {
      int i = 0;
      if (paramBoolean) {
        i = mContext.getResources().getDimensionPixelSize(17105347);
      }
      paramRemoteViews.setInt(16909178, "setMinimumHeight", i);
    }
    
    public Builder setContentText(CharSequence paramCharSequence)
    {
      mN.extras.putCharSequence("android.text", Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public Builder setContentTitle(CharSequence paramCharSequence)
    {
      mN.extras.putCharSequence("android.title", Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public Builder setCustomBigContentView(RemoteViews paramRemoteViews)
    {
      mN.bigContentView = paramRemoteViews;
      return this;
    }
    
    public Builder setCustomContentView(RemoteViews paramRemoteViews)
    {
      mN.contentView = paramRemoteViews;
      return this;
    }
    
    public Builder setCustomHeadsUpContentView(RemoteViews paramRemoteViews)
    {
      mN.headsUpContentView = paramRemoteViews;
      return this;
    }
    
    @Deprecated
    public Builder setDefaults(int paramInt)
    {
      mN.defaults = paramInt;
      return this;
    }
    
    public Builder setDeleteIntent(PendingIntent paramPendingIntent)
    {
      mN.deleteIntent = paramPendingIntent;
      return this;
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      if (paramBundle != null) {
        mUserExtras = paramBundle;
      }
      return this;
    }
    
    public Builder setFlag(int paramInt, boolean paramBoolean)
    {
      Notification localNotification;
      if (paramBoolean)
      {
        localNotification = mN;
        flags |= paramInt;
      }
      else
      {
        localNotification = mN;
        flags &= paramInt;
      }
      return this;
    }
    
    public Builder setFullScreenIntent(PendingIntent paramPendingIntent, boolean paramBoolean)
    {
      mN.fullScreenIntent = paramPendingIntent;
      setFlag(128, paramBoolean);
      return this;
    }
    
    public Builder setGroup(String paramString)
    {
      Notification.access$1202(mN, paramString);
      return this;
    }
    
    public Builder setGroupAlertBehavior(int paramInt)
    {
      Notification.access$802(mN, paramInt);
      return this;
    }
    
    public Builder setGroupSummary(boolean paramBoolean)
    {
      setFlag(512, paramBoolean);
      return this;
    }
    
    public Builder setHideSmartReplies(boolean paramBoolean)
    {
      mN.extras.putBoolean("android.hideSmartReplies", paramBoolean);
      return this;
    }
    
    public Builder setLargeIcon(Bitmap paramBitmap)
    {
      if (paramBitmap != null) {
        paramBitmap = Icon.createWithBitmap(paramBitmap);
      } else {
        paramBitmap = null;
      }
      return setLargeIcon(paramBitmap);
    }
    
    public Builder setLargeIcon(Icon paramIcon)
    {
      Notification.access$1102(mN, paramIcon);
      mN.extras.putParcelable("android.largeIcon", paramIcon);
      return this;
    }
    
    @Deprecated
    public Builder setLights(int paramInt1, int paramInt2, int paramInt3)
    {
      mN.ledARGB = paramInt1;
      mN.ledOnMS = paramInt2;
      mN.ledOffMS = paramInt3;
      if ((paramInt2 != 0) || (paramInt3 != 0))
      {
        Notification localNotification = mN;
        flags |= 0x1;
      }
      return this;
    }
    
    public Builder setLocalOnly(boolean paramBoolean)
    {
      setFlag(256, paramBoolean);
      return this;
    }
    
    public Builder setNumber(int paramInt)
    {
      mN.number = paramInt;
      return this;
    }
    
    public Builder setOngoing(boolean paramBoolean)
    {
      setFlag(2, paramBoolean);
      return this;
    }
    
    public Builder setOnlyAlertOnce(boolean paramBoolean)
    {
      setFlag(8, paramBoolean);
      return this;
    }
    
    @Deprecated
    public Builder setPriority(int paramInt)
    {
      mN.priority = paramInt;
      return this;
    }
    
    public Builder setProgress(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      mN.extras.putInt("android.progress", paramInt2);
      mN.extras.putInt("android.progressMax", paramInt1);
      mN.extras.putBoolean("android.progressIndeterminate", paramBoolean);
      return this;
    }
    
    public Builder setPublicVersion(Notification paramNotification)
    {
      if (paramNotification != null)
      {
        mN.publicVersion = new Notification();
        paramNotification.cloneInto(mN.publicVersion, true);
      }
      else
      {
        mN.publicVersion = null;
      }
      return this;
    }
    
    public void setRebuildStyledRemoteViews(boolean paramBoolean)
    {
      mRebuildStyledRemoteViews = paramBoolean;
    }
    
    public Builder setRemoteInputHistory(CharSequence[] paramArrayOfCharSequence)
    {
      if (paramArrayOfCharSequence == null)
      {
        mN.extras.putCharSequenceArray("android.remoteInputHistory", null);
      }
      else
      {
        int i = Math.min(5, paramArrayOfCharSequence.length);
        CharSequence[] arrayOfCharSequence = new CharSequence[i];
        for (int j = 0; j < i; j++) {
          arrayOfCharSequence[j] = Notification.safeCharSequence(paramArrayOfCharSequence[j]);
        }
        mN.extras.putCharSequenceArray("android.remoteInputHistory", arrayOfCharSequence);
      }
      return this;
    }
    
    public Builder setSettingsText(CharSequence paramCharSequence)
    {
      Notification.access$1002(mN, Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public Builder setShortcutId(String paramString)
    {
      Notification.access$602(mN, paramString);
      return this;
    }
    
    public Builder setShowRemoteInputSpinner(boolean paramBoolean)
    {
      mN.extras.putBoolean("android.remoteInputSpinner", paramBoolean);
      return this;
    }
    
    public Builder setShowWhen(boolean paramBoolean)
    {
      mN.extras.putBoolean("android.showWhen", paramBoolean);
      return this;
    }
    
    public Builder setSmallIcon(int paramInt)
    {
      Icon localIcon;
      if (paramInt != 0) {
        localIcon = Icon.createWithResource(mContext, paramInt);
      } else {
        localIcon = null;
      }
      return setSmallIcon(localIcon);
    }
    
    public Builder setSmallIcon(int paramInt1, int paramInt2)
    {
      mN.iconLevel = paramInt2;
      return setSmallIcon(paramInt1);
    }
    
    public Builder setSmallIcon(Icon paramIcon)
    {
      mN.setSmallIcon(paramIcon);
      if ((paramIcon != null) && (paramIcon.getType() == 2)) {
        mN.icon = paramIcon.getResId();
      }
      return this;
    }
    
    public Builder setSortKey(String paramString)
    {
      Notification.access$1302(mN, paramString);
      return this;
    }
    
    @Deprecated
    public Builder setSound(Uri paramUri)
    {
      mN.sound = paramUri;
      mN.audioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
      return this;
    }
    
    @Deprecated
    public Builder setSound(Uri paramUri, int paramInt)
    {
      PlayerBase.deprecateStreamTypeForPlayback(paramInt, "Notification", "setSound()");
      mN.sound = paramUri;
      mN.audioStreamType = paramInt;
      return this;
    }
    
    @Deprecated
    public Builder setSound(Uri paramUri, AudioAttributes paramAudioAttributes)
    {
      mN.sound = paramUri;
      mN.audioAttributes = paramAudioAttributes;
      return this;
    }
    
    public Builder setStyle(Notification.Style paramStyle)
    {
      if (mStyle != paramStyle)
      {
        mStyle = paramStyle;
        if (mStyle != null)
        {
          mStyle.setBuilder(this);
          mN.extras.putString("android.template", paramStyle.getClass().getName());
        }
        else
        {
          mN.extras.remove("android.template");
        }
      }
      return this;
    }
    
    public Builder setSubText(CharSequence paramCharSequence)
    {
      mN.extras.putCharSequence("android.subText", Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public Builder setTicker(CharSequence paramCharSequence)
    {
      mN.tickerText = Notification.safeCharSequence(paramCharSequence);
      return this;
    }
    
    @Deprecated
    public Builder setTicker(CharSequence paramCharSequence, RemoteViews paramRemoteViews)
    {
      setTicker(paramCharSequence);
      return this;
    }
    
    @Deprecated
    public Builder setTimeout(long paramLong)
    {
      Notification.access$902(mN, paramLong);
      return this;
    }
    
    public Builder setTimeoutAfter(long paramLong)
    {
      Notification.access$902(mN, paramLong);
      return this;
    }
    
    public Builder setUsesChronometer(boolean paramBoolean)
    {
      mN.extras.putBoolean("android.showChronometer", paramBoolean);
      return this;
    }
    
    @Deprecated
    public Builder setVibrate(long[] paramArrayOfLong)
    {
      mN.vibrate = paramArrayOfLong;
      return this;
    }
    
    public Builder setVisibility(int paramInt)
    {
      mN.visibility = paramInt;
      return this;
    }
    
    public Builder setWhen(long paramLong)
    {
      mN.when = paramLong;
      return this;
    }
    
    public boolean usesStandardHeader()
    {
      boolean bool1 = mN.mUsesStandardHeader;
      boolean bool2 = true;
      if (bool1) {
        return true;
      }
      if ((mContext.getApplicationInfo().targetSdkVersion >= 24) && (mN.contentView == null) && (mN.bigContentView == null)) {
        return true;
      }
      int i;
      if ((mN.contentView != null) && (!Notification.STANDARD_LAYOUTS.contains(Integer.valueOf(mN.contentView.getLayoutId())))) {
        i = 0;
      } else {
        i = 1;
      }
      int j;
      if ((mN.bigContentView != null) && (!Notification.STANDARD_LAYOUTS.contains(Integer.valueOf(mN.bigContentView.getLayoutId())))) {
        j = 0;
      } else {
        j = 1;
      }
      if ((i == 0) || (j == 0)) {
        bool2 = false;
      }
      return bool2;
    }
  }
  
  private static class BuilderRemoteViews
    extends RemoteViews
  {
    public BuilderRemoteViews(ApplicationInfo paramApplicationInfo, int paramInt)
    {
      super(paramInt);
    }
    
    public BuilderRemoteViews(Parcel paramParcel)
    {
      super();
    }
    
    public BuilderRemoteViews clone()
    {
      Parcel localParcel = Parcel.obtain();
      writeToParcel(localParcel, 0);
      localParcel.setDataPosition(0);
      BuilderRemoteViews localBuilderRemoteViews = new BuilderRemoteViews(localParcel);
      localParcel.recycle();
      return localBuilderRemoteViews;
    }
  }
  
  public static final class CarExtender
    implements Notification.Extender
  {
    private static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
    private static final String EXTRA_COLOR = "app_color";
    private static final String EXTRA_CONVERSATION = "car_conversation";
    private static final String EXTRA_LARGE_ICON = "large_icon";
    private static final String TAG = "CarExtender";
    private int mColor = 0;
    private Bitmap mLargeIcon;
    private UnreadConversation mUnreadConversation;
    
    public CarExtender() {}
    
    public CarExtender(Notification paramNotification)
    {
      if (extras == null) {
        paramNotification = null;
      } else {
        paramNotification = extras.getBundle("android.car.EXTENSIONS");
      }
      if (paramNotification != null)
      {
        mLargeIcon = ((Bitmap)paramNotification.getParcelable("large_icon"));
        mColor = paramNotification.getInt("app_color", 0);
        mUnreadConversation = UnreadConversation.getUnreadConversationFromBundle(paramNotification.getBundle("car_conversation"));
      }
    }
    
    public Notification.Builder extend(Notification.Builder paramBuilder)
    {
      Bundle localBundle = new Bundle();
      if (mLargeIcon != null) {
        localBundle.putParcelable("large_icon", mLargeIcon);
      }
      if (mColor != 0) {
        localBundle.putInt("app_color", mColor);
      }
      if (mUnreadConversation != null) {
        localBundle.putBundle("car_conversation", mUnreadConversation.getBundleForUnreadConversation());
      }
      paramBuilder.getExtras().putBundle("android.car.EXTENSIONS", localBundle);
      return paramBuilder;
    }
    
    public int getColor()
    {
      return mColor;
    }
    
    public Bitmap getLargeIcon()
    {
      return mLargeIcon;
    }
    
    public UnreadConversation getUnreadConversation()
    {
      return mUnreadConversation;
    }
    
    public CarExtender setColor(int paramInt)
    {
      mColor = paramInt;
      return this;
    }
    
    public CarExtender setLargeIcon(Bitmap paramBitmap)
    {
      mLargeIcon = paramBitmap;
      return this;
    }
    
    public CarExtender setUnreadConversation(UnreadConversation paramUnreadConversation)
    {
      mUnreadConversation = paramUnreadConversation;
      return this;
    }
    
    public static class Builder
    {
      private long mLatestTimestamp;
      private final List<String> mMessages = new ArrayList();
      private final String mParticipant;
      private PendingIntent mReadPendingIntent;
      private RemoteInput mRemoteInput;
      private PendingIntent mReplyPendingIntent;
      
      public Builder(String paramString)
      {
        mParticipant = paramString;
      }
      
      public Builder addMessage(String paramString)
      {
        mMessages.add(paramString);
        return this;
      }
      
      public Notification.CarExtender.UnreadConversation build()
      {
        String[] arrayOfString = (String[])mMessages.toArray(new String[mMessages.size()]);
        String str = mParticipant;
        RemoteInput localRemoteInput = mRemoteInput;
        PendingIntent localPendingIntent1 = mReplyPendingIntent;
        PendingIntent localPendingIntent2 = mReadPendingIntent;
        long l = mLatestTimestamp;
        return new Notification.CarExtender.UnreadConversation(arrayOfString, localRemoteInput, localPendingIntent1, localPendingIntent2, new String[] { str }, l);
      }
      
      public Builder setLatestTimestamp(long paramLong)
      {
        mLatestTimestamp = paramLong;
        return this;
      }
      
      public Builder setReadPendingIntent(PendingIntent paramPendingIntent)
      {
        mReadPendingIntent = paramPendingIntent;
        return this;
      }
      
      public Builder setReplyAction(PendingIntent paramPendingIntent, RemoteInput paramRemoteInput)
      {
        mRemoteInput = paramRemoteInput;
        mReplyPendingIntent = paramPendingIntent;
        return this;
      }
    }
    
    public static class UnreadConversation
    {
      private static final String KEY_AUTHOR = "author";
      private static final String KEY_MESSAGES = "messages";
      private static final String KEY_ON_READ = "on_read";
      private static final String KEY_ON_REPLY = "on_reply";
      private static final String KEY_PARTICIPANTS = "participants";
      private static final String KEY_REMOTE_INPUT = "remote_input";
      private static final String KEY_TEXT = "text";
      private static final String KEY_TIMESTAMP = "timestamp";
      private final long mLatestTimestamp;
      private final String[] mMessages;
      private final String[] mParticipants;
      private final PendingIntent mReadPendingIntent;
      private final RemoteInput mRemoteInput;
      private final PendingIntent mReplyPendingIntent;
      
      UnreadConversation(String[] paramArrayOfString1, RemoteInput paramRemoteInput, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, String[] paramArrayOfString2, long paramLong)
      {
        mMessages = paramArrayOfString1;
        mRemoteInput = paramRemoteInput;
        mReadPendingIntent = paramPendingIntent2;
        mReplyPendingIntent = paramPendingIntent1;
        mParticipants = paramArrayOfString2;
        mLatestTimestamp = paramLong;
      }
      
      static UnreadConversation getUnreadConversationFromBundle(Bundle paramBundle)
      {
        if (paramBundle == null) {
          return null;
        }
        Object localObject = paramBundle.getParcelableArray("messages");
        String[] arrayOfString1 = null;
        if (localObject != null)
        {
          arrayOfString1 = new String[localObject.length];
          int i = 1;
          int k;
          for (int j = 0;; j++)
          {
            k = i;
            if (j >= arrayOfString1.length) {
              break;
            }
            if (!(localObject[j] instanceof Bundle))
            {
              k = 0;
              break;
            }
            arrayOfString1[j] = ((Bundle)localObject[j]).getString("text");
            if (arrayOfString1[j] == null)
            {
              k = 0;
              break;
            }
          }
          if (k == 0) {
            return null;
          }
        }
        PendingIntent localPendingIntent1 = (PendingIntent)paramBundle.getParcelable("on_read");
        PendingIntent localPendingIntent2 = (PendingIntent)paramBundle.getParcelable("on_reply");
        localObject = (RemoteInput)paramBundle.getParcelable("remote_input");
        String[] arrayOfString2 = paramBundle.getStringArray("participants");
        if ((arrayOfString2 != null) && (arrayOfString2.length == 1)) {
          return new UnreadConversation(arrayOfString1, (RemoteInput)localObject, localPendingIntent2, localPendingIntent1, arrayOfString2, paramBundle.getLong("timestamp"));
        }
        return null;
      }
      
      Bundle getBundleForUnreadConversation()
      {
        Bundle localBundle = new Bundle();
        Parcelable[] arrayOfParcelable = null;
        Object localObject1 = mParticipants;
        int i = 0;
        Object localObject2 = arrayOfParcelable;
        if (localObject1 != null)
        {
          localObject2 = arrayOfParcelable;
          if (mParticipants.length > 1) {
            localObject2 = mParticipants[0];
          }
        }
        arrayOfParcelable = new Parcelable[mMessages.length];
        while (i < arrayOfParcelable.length)
        {
          localObject1 = new Bundle();
          ((Bundle)localObject1).putString("text", mMessages[i]);
          ((Bundle)localObject1).putString("author", (String)localObject2);
          arrayOfParcelable[i] = localObject1;
          i++;
        }
        localBundle.putParcelableArray("messages", arrayOfParcelable);
        if (mRemoteInput != null) {
          localBundle.putParcelable("remote_input", mRemoteInput);
        }
        localBundle.putParcelable("on_reply", mReplyPendingIntent);
        localBundle.putParcelable("on_read", mReadPendingIntent);
        localBundle.putStringArray("participants", mParticipants);
        localBundle.putLong("timestamp", mLatestTimestamp);
        return localBundle;
      }
      
      public long getLatestTimestamp()
      {
        return mLatestTimestamp;
      }
      
      public String[] getMessages()
      {
        return mMessages;
      }
      
      public String getParticipant()
      {
        String str;
        if (mParticipants.length > 0) {
          str = mParticipants[0];
        } else {
          str = null;
        }
        return str;
      }
      
      public String[] getParticipants()
      {
        return mParticipants;
      }
      
      public PendingIntent getReadPendingIntent()
      {
        return mReadPendingIntent;
      }
      
      public RemoteInput getRemoteInput()
      {
        return mRemoteInput;
      }
      
      public PendingIntent getReplyPendingIntent()
      {
        return mReplyPendingIntent;
      }
    }
  }
  
  public static class DecoratedCustomViewStyle
    extends Notification.Style
  {
    public DecoratedCustomViewStyle() {}
    
    private void buildIntoRemoteViewContent(RemoteViews paramRemoteViews1, RemoteViews paramRemoteViews2, Notification.TemplateBindResult paramTemplateBindResult)
    {
      if (paramRemoteViews2 != null)
      {
        paramRemoteViews2 = paramRemoteViews2.clone();
        paramRemoteViews1.removeAllViewsExceptId(16909178, 16908301);
        paramRemoteViews1.addView(16909178, paramRemoteViews2, 0);
        paramRemoteViews1.setReapplyDisallowed();
      }
      paramRemoteViews1.setViewLayoutMarginEnd(16909178, mBuilder.mContext.getResources().getDimensionPixelSize(17105317) + paramTemplateBindResult.getIconMarginEnd());
    }
    
    private RemoteViews makeDecoratedBigContentView()
    {
      RemoteViews localRemoteViews1;
      if (mBuilder.mN.bigContentView == null) {
        localRemoteViews1 = mBuilder.mN.contentView;
      } else {
        localRemoteViews1 = mBuilder.mN.bigContentView;
      }
      if (mBuilder.mActions.size() == 0) {
        return makeStandardTemplateWithCustomContent(localRemoteViews1);
      }
      Notification.TemplateBindResult localTemplateBindResult = new Notification.TemplateBindResult(null);
      RemoteViews localRemoteViews2 = mBuilder.applyStandardTemplateWithActions(Notification.Builder.access$4000(mBuilder), localTemplateBindResult);
      buildIntoRemoteViewContent(localRemoteViews2, localRemoteViews1, localTemplateBindResult);
      return localRemoteViews2;
    }
    
    private RemoteViews makeDecoratedHeadsUpContentView()
    {
      RemoteViews localRemoteViews1;
      if (mBuilder.mN.headsUpContentView == null) {
        localRemoteViews1 = mBuilder.mN.contentView;
      } else {
        localRemoteViews1 = mBuilder.mN.headsUpContentView;
      }
      if (mBuilder.mActions.size() == 0) {
        return makeStandardTemplateWithCustomContent(localRemoteViews1);
      }
      Notification.TemplateBindResult localTemplateBindResult = new Notification.TemplateBindResult(null);
      RemoteViews localRemoteViews2 = mBuilder.applyStandardTemplateWithActions(Notification.Builder.access$4000(mBuilder), localTemplateBindResult);
      buildIntoRemoteViewContent(localRemoteViews2, localRemoteViews1, localTemplateBindResult);
      return localRemoteViews2;
    }
    
    private RemoteViews makeStandardTemplateWithCustomContent(RemoteViews paramRemoteViews)
    {
      Notification.TemplateBindResult localTemplateBindResult = new Notification.TemplateBindResult(null);
      RemoteViews localRemoteViews = mBuilder.applyStandardTemplate(Notification.Builder.access$4100(mBuilder), localTemplateBindResult);
      buildIntoRemoteViewContent(localRemoteViews, paramRemoteViews, localTemplateBindResult);
      return localRemoteViews;
    }
    
    public boolean areNotificationsVisiblyDifferent(Notification.Style paramStyle)
    {
      return (paramStyle == null) || (getClass() != paramStyle.getClass());
    }
    
    public boolean displayCustomViewInline()
    {
      return true;
    }
    
    public RemoteViews makeBigContentView()
    {
      return makeDecoratedBigContentView();
    }
    
    public RemoteViews makeContentView(boolean paramBoolean)
    {
      return makeStandardTemplateWithCustomContent(mBuilder.mN.contentView);
    }
    
    public RemoteViews makeHeadsUpContentView(boolean paramBoolean)
    {
      return makeDecoratedHeadsUpContentView();
    }
  }
  
  public static class DecoratedMediaCustomViewStyle
    extends Notification.MediaStyle
  {
    public DecoratedMediaCustomViewStyle() {}
    
    private RemoteViews buildIntoRemoteView(RemoteViews paramRemoteViews1, int paramInt, RemoteViews paramRemoteViews2)
    {
      if (paramRemoteViews2 != null)
      {
        paramRemoteViews2 = paramRemoteViews2.clone();
        paramRemoteViews2.overrideTextColors(mBuilder.getPrimaryTextColor());
        paramRemoteViews1.removeAllViews(paramInt);
        paramRemoteViews1.addView(paramInt, paramRemoteViews2);
        paramRemoteViews1.setReapplyDisallowed();
      }
      return paramRemoteViews1;
    }
    
    private RemoteViews makeBigContentViewWithCustomContent(RemoteViews paramRemoteViews)
    {
      RemoteViews localRemoteViews = super.makeBigContentView();
      if (localRemoteViews != null) {
        return buildIntoRemoteView(localRemoteViews, 16909178, paramRemoteViews);
      }
      if (paramRemoteViews != mBuilder.mN.contentView) {
        return buildIntoRemoteView(super.makeContentView(false), 16909176, paramRemoteViews);
      }
      return null;
    }
    
    public boolean areNotificationsVisiblyDifferent(Notification.Style paramStyle)
    {
      return (paramStyle == null) || (getClass() != paramStyle.getClass());
    }
    
    public boolean displayCustomViewInline()
    {
      return true;
    }
    
    public RemoteViews makeBigContentView()
    {
      RemoteViews localRemoteViews;
      if (mBuilder.mN.bigContentView != null) {
        localRemoteViews = mBuilder.mN.bigContentView;
      } else {
        localRemoteViews = mBuilder.mN.contentView;
      }
      return makeBigContentViewWithCustomContent(localRemoteViews);
    }
    
    public RemoteViews makeContentView(boolean paramBoolean)
    {
      return buildIntoRemoteView(super.makeContentView(false), 16909176, mBuilder.mN.contentView);
    }
    
    public RemoteViews makeHeadsUpContentView(boolean paramBoolean)
    {
      RemoteViews localRemoteViews;
      if (mBuilder.mN.headsUpContentView != null) {
        localRemoteViews = mBuilder.mN.headsUpContentView;
      } else {
        localRemoteViews = mBuilder.mN.contentView;
      }
      return makeBigContentViewWithCustomContent(localRemoteViews);
    }
  }
  
  public static abstract interface Extender
  {
    public abstract Notification.Builder extend(Notification.Builder paramBuilder);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GroupAlertBehavior {}
  
  public static class InboxStyle
    extends Notification.Style
  {
    private static final int NUMBER_OF_HISTORY_ALLOWED_UNTIL_REDUCTION = 1;
    private ArrayList<CharSequence> mTexts = new ArrayList(5);
    
    public InboxStyle() {}
    
    @Deprecated
    public InboxStyle(Notification.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }
    
    private void handleInboxImageMargin(RemoteViews paramRemoteViews, int paramInt1, boolean paramBoolean, int paramInt2)
    {
      int i = 0;
      int j = i;
      if (paramBoolean)
      {
        Bundle localBundle = mBuilder.mN.extras;
        int k = 0;
        j = localBundle.getInt("android.progressMax", 0);
        paramBoolean = mBuilder.mN.extras.getBoolean("android.progressIndeterminate");
        if ((j == 0) && (!paramBoolean)) {
          break label70;
        }
        k = 1;
        label70:
        j = i;
        if (k == 0) {
          j = paramInt2;
        }
      }
      paramRemoteViews.setViewLayoutMarginEnd(paramInt1, j);
    }
    
    public void addExtras(Bundle paramBundle)
    {
      super.addExtras(paramBundle);
      CharSequence[] arrayOfCharSequence = new CharSequence[mTexts.size()];
      paramBundle.putCharSequenceArray("android.textLines", (CharSequence[])mTexts.toArray(arrayOfCharSequence));
    }
    
    public InboxStyle addLine(CharSequence paramCharSequence)
    {
      mTexts.add(Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public boolean areNotificationsVisiblyDifferent(Notification.Style paramStyle)
    {
      if ((paramStyle != null) && (getClass() == paramStyle.getClass()))
      {
        Object localObject = (InboxStyle)paramStyle;
        paramStyle = getLines();
        localObject = ((InboxStyle)localObject).getLines();
        int i = paramStyle.size();
        if (i != ((ArrayList)localObject).size()) {
          return true;
        }
        for (int j = 0; j < i; j++) {
          if (!Objects.equals(String.valueOf(paramStyle.get(j)), String.valueOf(((ArrayList)localObject).get(j)))) {
            return true;
          }
        }
        return false;
      }
      return true;
    }
    
    public ArrayList<CharSequence> getLines()
    {
      return mTexts;
    }
    
    public RemoteViews makeBigContentView()
    {
      Object localObject1 = mBuilder.mN.extras.getCharSequence("android.text");
      mBuilder.getAllExtras().putCharSequence("android.text", null);
      Notification.TemplateBindResult localTemplateBindResult = new Notification.TemplateBindResult(null);
      RemoteViews localRemoteViews = getStandardView(mBuilder.getInboxLayoutResource(), localTemplateBindResult);
      mBuilder.getAllExtras().putCharSequence("android.text", (CharSequence)localObject1);
      localObject1 = new int[7];
      Object tmp70_69 = localObject1;
      tmp70_69[0] = 16909027;
      Object tmp75_70 = tmp70_69;
      tmp75_70[1] = 16909028;
      Object tmp80_75 = tmp75_70;
      tmp80_75[2] = 16909029;
      Object tmp85_80 = tmp80_75;
      tmp85_80[3] = 16909030;
      Object tmp90_85 = tmp85_80;
      tmp90_85[4] = 16909031;
      Object tmp95_90 = tmp90_85;
      tmp95_90[5] = 16909032;
      Object tmp100_95 = tmp95_90;
      tmp100_95[6] = 16909033;
      tmp100_95;
      int i = localObject1.length;
      for (int j = 0; j < i; j++) {
        localRemoteViews.setViewVisibility(localObject1[j], 8);
      }
      int k = 0;
      int m = mBuilder.mContext.getResources().getDimensionPixelSize(17105339);
      i = localObject1.length;
      j = i;
      if (mBuilder.mActions.size() > 0) {
        j = i - 1;
      }
      Object localObject2 = mBuilder.mN.extras.getCharSequenceArray("android.remoteInputHistory");
      i = k;
      int n = j;
      if (localObject2 != null)
      {
        i = k;
        n = j;
        if (localObject2.length > 1)
        {
          i = Math.min(localObject2.length, 3);
          int i1 = mTexts.size() + i - 1;
          i = k;
          n = j;
          if (i1 > j)
          {
            i = i1 - j;
            if (mTexts.size() > j)
            {
              n = j - i;
              i = k;
            }
            else
            {
              n = j;
            }
          }
        }
      }
      boolean bool = true;
      j = 0;
      while ((i < mTexts.size()) && (i < n))
      {
        localObject2 = (CharSequence)mTexts.get(i);
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          localRemoteViews.setViewVisibility(localObject1[i], 0);
          localRemoteViews.setTextViewText(localObject1[i], mBuilder.processTextSpans(Notification.Builder.access$2300(mBuilder, (CharSequence)localObject2)));
          mBuilder.setTextViewColorSecondary(localRemoteViews, localObject1[i]);
          localRemoteViews.setViewPadding(localObject1[i], 0, m, 0, 0);
          handleInboxImageMargin(localRemoteViews, localObject1[i], bool, localTemplateBindResult.getIconMarginEnd());
          if (bool) {}
          for (j = localObject1[i];; j = 0) {
            break;
          }
          bool = false;
        }
        i++;
      }
      if (j != 0) {
        localRemoteViews.setViewPadding(j, 0, mBuilder.mContext.getResources().getDimensionPixelSize(17105356), 0, 0);
      }
      return localRemoteViews;
    }
    
    protected void restoreFromExtras(Bundle paramBundle)
    {
      super.restoreFromExtras(paramBundle);
      mTexts.clear();
      if (paramBundle.containsKey("android.textLines")) {
        Collections.addAll(mTexts, paramBundle.getCharSequenceArray("android.textLines"));
      }
    }
    
    public InboxStyle setBigContentTitle(CharSequence paramCharSequence)
    {
      internalSetBigContentTitle(Notification.safeCharSequence(paramCharSequence));
      return this;
    }
    
    public InboxStyle setSummaryText(CharSequence paramCharSequence)
    {
      internalSetSummaryText(Notification.safeCharSequence(paramCharSequence));
      return this;
    }
  }
  
  public static class MediaStyle
    extends Notification.Style
  {
    static final int MAX_MEDIA_BUTTONS = 5;
    static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
    private int[] mActionsToShowInCompact = null;
    private MediaSession.Token mToken;
    
    public MediaStyle() {}
    
    @Deprecated
    public MediaStyle(Notification.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }
    
    private RemoteViews generateMediaActionButton(Notification.Action paramAction, int paramInt)
    {
      int i;
      if (actionIntent == null) {
        i = 1;
      } else {
        i = 0;
      }
      Notification.BuilderRemoteViews localBuilderRemoteViews = new Notification.BuilderRemoteViews(mBuilder.mContext.getApplicationInfo(), 17367214);
      localBuilderRemoteViews.setImageViewIcon(16908686, paramAction.getIcon());
      if ((!mBuilder.shouldTintActionButtons()) && (!mBuilder.isColorized())) {
        paramInt = NotificationColorUtil.resolveColor(mBuilder.mContext, 0);
      }
      localBuilderRemoteViews.setDrawableTint(16908686, false, paramInt, PorterDuff.Mode.SRC_ATOP);
      if (i == 0) {
        localBuilderRemoteViews.setOnClickPendingIntent(16908686, actionIntent);
      }
      localBuilderRemoteViews.setContentDescription(16908686, title);
      return localBuilderRemoteViews;
    }
    
    private int getActionColor()
    {
      int i;
      if (mBuilder.isColorized()) {
        i = mBuilder.getPrimaryTextColor();
      } else {
        i = mBuilder.resolveContrastColor();
      }
      return i;
    }
    
    private void handleImage(RemoteViews paramRemoteViews)
    {
      if (mBuilder.mN.hasLargeIcon())
      {
        paramRemoteViews.setViewLayoutMarginEndDimen(16909087, 0);
        paramRemoteViews.setViewLayoutMarginEndDimen(16909437, 0);
      }
    }
    
    private RemoteViews makeMediaBigContentView()
    {
      int i = Math.min(mBuilder.mActions.size(), 5);
      Object localObject = mActionsToShowInCompact;
      int j = 0;
      int k;
      if (localObject == null) {
        k = 0;
      } else {
        k = Math.min(mActionsToShowInCompact.length, 3);
      }
      if ((!mBuilder.mN.hasLargeIcon()) && (i <= k)) {
        return null;
      }
      localObject = mBuilder.applyStandardTemplate(17367221, false, null);
      if (i > 0)
      {
        ((RemoteViews)localObject).removeAllViews(16909112);
        for (k = j; k < i; k++) {
          ((RemoteViews)localObject).addView(16909112, generateMediaActionButton((Notification.Action)mBuilder.mActions.get(k), getActionColor()));
        }
      }
      handleImage((RemoteViews)localObject);
      return localObject;
    }
    
    private RemoteViews makeMediaContentView()
    {
      RemoteViews localRemoteViews = mBuilder.applyStandardTemplate(17367225, false, null);
      int i = mBuilder.mActions.size();
      if (mActionsToShowInCompact == null) {
        j = 0;
      } else {
        j = Math.min(mActionsToShowInCompact.length, 3);
      }
      if (j > 0)
      {
        localRemoteViews.removeAllViews(16909112);
        int k = 0;
        while (k < j) {
          if (k < i)
          {
            localRemoteViews.addView(16909112, generateMediaActionButton((Notification.Action)mBuilder.mActions.get(mActionsToShowInCompact[k]), getActionColor()));
            k++;
          }
          else
          {
            throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", new Object[] { Integer.valueOf(k), Integer.valueOf(i - 1) }));
          }
        }
      }
      handleImage(localRemoteViews);
      int j = 17105317;
      if (mBuilder.mN.hasLargeIcon()) {
        j = 17105341;
      }
      localRemoteViews.setViewLayoutMarginEndDimen(16909178, j);
      return localRemoteViews;
    }
    
    public void addExtras(Bundle paramBundle)
    {
      super.addExtras(paramBundle);
      if (mToken != null) {
        paramBundle.putParcelable("android.mediaSession", mToken);
      }
      if (mActionsToShowInCompact != null) {
        paramBundle.putIntArray("android.compactActions", mActionsToShowInCompact);
      }
    }
    
    public boolean areNotificationsVisiblyDifferent(Notification.Style paramStyle)
    {
      return (paramStyle == null) || (getClass() != paramStyle.getClass());
    }
    
    public Notification buildStyled(Notification paramNotification)
    {
      super.buildStyled(paramNotification);
      if (category == null) {
        category = "transport";
      }
      return paramNotification;
    }
    
    protected boolean hasProgress()
    {
      return false;
    }
    
    public RemoteViews makeBigContentView()
    {
      return makeMediaBigContentView();
    }
    
    public RemoteViews makeContentView(boolean paramBoolean)
    {
      return makeMediaContentView();
    }
    
    public RemoteViews makeHeadsUpContentView(boolean paramBoolean)
    {
      RemoteViews localRemoteViews = makeMediaBigContentView();
      if (localRemoteViews == null) {
        localRemoteViews = makeMediaContentView();
      }
      return localRemoteViews;
    }
    
    protected void restoreFromExtras(Bundle paramBundle)
    {
      super.restoreFromExtras(paramBundle);
      if (paramBundle.containsKey("android.mediaSession")) {
        mToken = ((MediaSession.Token)paramBundle.getParcelable("android.mediaSession"));
      }
      if (paramBundle.containsKey("android.compactActions")) {
        mActionsToShowInCompact = paramBundle.getIntArray("android.compactActions");
      }
    }
    
    public MediaStyle setMediaSession(MediaSession.Token paramToken)
    {
      mToken = paramToken;
      return this;
    }
    
    public MediaStyle setShowActionsInCompactView(int... paramVarArgs)
    {
      mActionsToShowInCompact = paramVarArgs;
      return this;
    }
  }
  
  public static class MessagingStyle
    extends Notification.Style
  {
    public static final int MAXIMUM_RETAINED_MESSAGES = 25;
    CharSequence mConversationTitle;
    List<Message> mHistoricMessages = new ArrayList();
    boolean mIsGroupConversation;
    List<Message> mMessages = new ArrayList();
    Person mUser;
    
    MessagingStyle() {}
    
    public MessagingStyle(Person paramPerson)
    {
      mUser = paramPerson;
    }
    
    public MessagingStyle(CharSequence paramCharSequence)
    {
      this(new Person.Builder().setName(paramCharSequence).build());
    }
    
    private CharSequence createConversationTitleFromMessages()
    {
      ArraySet localArraySet = new ArraySet();
      int i = 0;
      for (int j = 0; j < mMessages.size(); j++)
      {
        localObject = ((Message)mMessages.get(j)).getSenderPerson();
        if (localObject != null) {
          localArraySet.add(((Person)localObject).getName());
        }
      }
      Object localObject = new SpannableStringBuilder();
      int k = localArraySet.size();
      for (j = i; j < k; j++)
      {
        CharSequence localCharSequence = (CharSequence)localArraySet.valueAt(j);
        if (!TextUtils.isEmpty((CharSequence)localObject)) {
          ((SpannableStringBuilder)localObject).append(", ");
        }
        ((SpannableStringBuilder)localObject).append(BidiFormatter.getInstance().unicodeWrap(localCharSequence));
      }
      return localObject;
    }
    
    private Message findLatestIncomingMessage()
    {
      return findLatestIncomingMessage(mMessages);
    }
    
    public static Message findLatestIncomingMessage(List<Message> paramList)
    {
      for (int i = paramList.size() - 1; i >= 0; i--)
      {
        Message localMessage = (Message)paramList.get(i);
        if ((mSender != null) && (!TextUtils.isEmpty(mSender.getName()))) {
          return localMessage;
        }
      }
      if (!paramList.isEmpty()) {
        return (Message)paramList.get(paramList.size() - 1);
      }
      return null;
    }
    
    private void fixTitleAndTextExtras(Bundle paramBundle)
    {
      Object localObject1 = findLatestIncomingMessage();
      Object localObject2 = null;
      CharSequence localCharSequence;
      if (localObject1 == null) {
        localCharSequence = null;
      } else {
        localCharSequence = mText;
      }
      if (localObject1 != null)
      {
        if ((mSender != null) && (!TextUtils.isEmpty(mSender.getName()))) {
          localObject2 = mSender;
        } else {
          localObject2 = mUser;
        }
        localObject2 = ((Person)localObject2).getName();
      }
      if (!TextUtils.isEmpty(mConversationTitle)) {
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          localObject1 = BidiFormatter.getInstance();
          localObject2 = mBuilder.mContext.getString(17040516, new Object[] { ((BidiFormatter)localObject1).unicodeWrap(mConversationTitle), ((BidiFormatter)localObject1).unicodeWrap((CharSequence)localObject2) });
        }
        else
        {
          localObject2 = mConversationTitle;
        }
      }
      if (localObject2 != null) {
        paramBundle.putCharSequence("android.title", (CharSequence)localObject2);
      }
      if (localCharSequence != null) {
        paramBundle.putCharSequence("android.text", localCharSequence);
      }
    }
    
    private boolean hasOnlyWhiteSpaceSenders()
    {
      for (int i = 0; i < mMessages.size(); i++)
      {
        Person localPerson = ((Message)mMessages.get(i)).getSenderPerson();
        if ((localPerson != null) && (!isWhiteSpace(localPerson.getName()))) {
          return false;
        }
      }
      return true;
    }
    
    private boolean isWhiteSpace(CharSequence paramCharSequence)
    {
      if (TextUtils.isEmpty(paramCharSequence)) {
        return true;
      }
      if (paramCharSequence.toString().matches("^\\s*$")) {
        return true;
      }
      for (int i = 0; i < paramCharSequence.length(); i++) {
        if (paramCharSequence.charAt(i) != '​') {
          return false;
        }
      }
      return true;
    }
    
    private static TextAppearanceSpan makeFontColorSpan(int paramInt)
    {
      return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(paramInt), null);
    }
    
    private RemoteViews makeMessagingView(boolean paramBoolean1, boolean paramBoolean2)
    {
      if (!TextUtils.isEmpty(Notification.Style.access$3000(this))) {
        localObject1 = Notification.Style.access$3000(this);
      } else {
        localObject1 = mConversationTitle;
      }
      int i = mBuilder.mContext.getApplicationInfo().targetSdkVersion;
      boolean bool1 = false;
      if (i >= 28) {
        i = 1;
      } else {
        i = 0;
      }
      Object localObject2 = null;
      Object localObject3 = null;
      boolean bool2;
      if (i == 0)
      {
        bool2 = TextUtils.isEmpty((CharSequence)localObject1);
        localObject4 = access$300mBuilder).mLargeIcon;
        localObject5 = localObject1;
        localObject3 = localObject4;
        if (hasOnlyWhiteSpaceSenders())
        {
          bool2 = true;
          localObject5 = null;
          localObject2 = localObject1;
          localObject3 = localObject4;
        }
      }
      else
      {
        bool2 = isGroupConversation() ^ true;
        localObject5 = localObject1;
      }
      Object localObject1 = new Notification.TemplateBindResult(null);
      Notification.Builder localBuilder = mBuilder;
      i = mBuilder.getMessagingLayoutResource();
      Object localObject4 = mBuilder.mParams.reset().hasProgress(false).title((CharSequence)localObject5).text(null);
      if ((!paramBoolean2) && (!bool2)) {
        break label194;
      }
      bool1 = true;
      label194:
      Object localObject5 = localBuilder.applyStandardTemplateWithActions(i, ((Notification.StandardTemplateParams)localObject4).hideLargeIcon(bool1).hideReplyIcon(paramBoolean2).headerTextSecondary((CharSequence)localObject5), (Notification.TemplateBindResult)localObject1);
      addExtras(mBuilder.mN.extras);
      ((RemoteViews)localObject5).setViewLayoutMarginEnd(16909185, ((Notification.TemplateBindResult)localObject1).getIconMarginEnd());
      if (mBuilder.isColorized()) {
        i = mBuilder.getPrimaryTextColor();
      } else {
        i = mBuilder.resolveContrastColor();
      }
      ((RemoteViews)localObject5).setInt(16909411, "setLayoutColor", i);
      ((RemoteViews)localObject5).setInt(16909411, "setSenderTextColor", mBuilder.getPrimaryTextColor());
      ((RemoteViews)localObject5).setInt(16909411, "setMessageTextColor", mBuilder.getSecondaryTextColor());
      ((RemoteViews)localObject5).setBoolean(16909411, "setDisplayImagesAtEnd", paramBoolean1);
      ((RemoteViews)localObject5).setIcon(16909411, "setAvatarReplacement", localObject3);
      ((RemoteViews)localObject5).setCharSequence(16909411, "setNameReplacement", localObject2);
      ((RemoteViews)localObject5).setBoolean(16909411, "setIsOneToOne", bool2);
      ((RemoteViews)localObject5).setBundle(16909411, "setData", mBuilder.mN.extras);
      return localObject5;
    }
    
    public void addExtras(Bundle paramBundle)
    {
      super.addExtras(paramBundle);
      if (mUser != null)
      {
        paramBundle.putCharSequence("android.selfDisplayName", mUser.getName());
        paramBundle.putParcelable("android.messagingUser", mUser);
      }
      if (mConversationTitle != null) {
        paramBundle.putCharSequence("android.conversationTitle", mConversationTitle);
      }
      if (!mMessages.isEmpty()) {
        paramBundle.putParcelableArray("android.messages", Message.getBundleArrayForMessages(mMessages));
      }
      if (!mHistoricMessages.isEmpty()) {
        paramBundle.putParcelableArray("android.messages.historic", Message.getBundleArrayForMessages(mHistoricMessages));
      }
      fixTitleAndTextExtras(paramBundle);
      paramBundle.putBoolean("android.isGroupConversation", mIsGroupConversation);
    }
    
    public MessagingStyle addHistoricMessage(Message paramMessage)
    {
      mHistoricMessages.add(paramMessage);
      if (mHistoricMessages.size() > 25) {
        mHistoricMessages.remove(0);
      }
      return this;
    }
    
    public MessagingStyle addMessage(Message paramMessage)
    {
      mMessages.add(paramMessage);
      if (mMessages.size() > 25) {
        mMessages.remove(0);
      }
      return this;
    }
    
    public MessagingStyle addMessage(CharSequence paramCharSequence, long paramLong, Person paramPerson)
    {
      return addMessage(new Message(paramCharSequence, paramLong, paramPerson));
    }
    
    public MessagingStyle addMessage(CharSequence paramCharSequence1, long paramLong, CharSequence paramCharSequence2)
    {
      if (paramCharSequence2 == null) {
        paramCharSequence2 = null;
      } else {
        paramCharSequence2 = new Person.Builder().setName(paramCharSequence2).build();
      }
      return addMessage(paramCharSequence1, paramLong, paramCharSequence2);
    }
    
    public boolean areNotificationsVisiblyDifferent(Notification.Style paramStyle)
    {
      if ((paramStyle != null) && (getClass() == paramStyle.getClass()))
      {
        paramStyle = (MessagingStyle)paramStyle;
        List localList = getMessages();
        Object localObject = paramStyle.getMessages();
        if (localList != null)
        {
          paramStyle = (Notification.Style)localObject;
          if (localObject != null) {}
        }
        else
        {
          paramStyle = new ArrayList();
        }
        int i = localList.size();
        if (i != paramStyle.size()) {
          return true;
        }
        for (int j = 0; j < i; j++)
        {
          Message localMessage1 = (Message)localList.get(j);
          Message localMessage2 = (Message)paramStyle.get(j);
          if (!Objects.equals(String.valueOf(localMessage1.getText()), String.valueOf(localMessage2.getText()))) {
            return true;
          }
          if (!Objects.equals(localMessage1.getDataUri(), localMessage2.getDataUri())) {
            return true;
          }
          if (localMessage1.getSenderPerson() == null) {
            localObject = localMessage1.getSender();
          } else {
            localObject = localMessage1.getSenderPerson().getName();
          }
          String str = String.valueOf(localObject);
          if (localMessage2.getSenderPerson() == null) {
            localObject = localMessage2.getSender();
          } else {
            localObject = localMessage2.getSenderPerson().getName();
          }
          if (!Objects.equals(str, String.valueOf(localObject))) {
            return true;
          }
          localObject = localMessage1.getSenderPerson();
          str = null;
          if (localObject == null) {
            localObject = null;
          } else {
            localObject = localMessage1.getSenderPerson().getKey();
          }
          if (localMessage2.getSenderPerson() != null) {
            str = localMessage2.getSenderPerson().getKey();
          }
          if (!Objects.equals(localObject, str)) {
            return true;
          }
        }
        return false;
      }
      return true;
    }
    
    public CharSequence getConversationTitle()
    {
      return mConversationTitle;
    }
    
    public CharSequence getHeadsUpStatusBarText()
    {
      CharSequence localCharSequence;
      if (!TextUtils.isEmpty(Notification.Style.access$3000(this))) {
        localCharSequence = Notification.Style.access$3000(this);
      } else {
        localCharSequence = mConversationTitle;
      }
      if ((!TextUtils.isEmpty(localCharSequence)) && (!hasOnlyWhiteSpaceSenders())) {
        return localCharSequence;
      }
      return null;
    }
    
    public List<Message> getHistoricMessages()
    {
      return mHistoricMessages;
    }
    
    public List<Message> getMessages()
    {
      return mMessages;
    }
    
    public Person getUser()
    {
      return mUser;
    }
    
    public CharSequence getUserDisplayName()
    {
      return mUser.getName();
    }
    
    public boolean isGroupConversation()
    {
      if ((mBuilder != null) && (mBuilder.mContext.getApplicationInfo().targetSdkVersion < 28))
      {
        boolean bool;
        if (mConversationTitle != null) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      return mIsGroupConversation;
    }
    
    public RemoteViews makeBigContentView()
    {
      return makeMessagingView(false, true);
    }
    
    public RemoteViews makeContentView(boolean paramBoolean)
    {
      Notification.Builder.access$2602(mBuilder, mBuilder.mActions);
      Notification.Builder.access$2702(mBuilder, new ArrayList());
      RemoteViews localRemoteViews = makeMessagingView(true, false);
      Notification.Builder.access$2702(mBuilder, mBuilder.mOriginalActions);
      Notification.Builder.access$2602(mBuilder, null);
      return localRemoteViews;
    }
    
    public RemoteViews makeHeadsUpContentView(boolean paramBoolean)
    {
      RemoteViews localRemoteViews = makeMessagingView(true, true);
      localRemoteViews.setInt(16909185, "setMaxDisplayedLines", 1);
      return localRemoteViews;
    }
    
    protected void restoreFromExtras(Bundle paramBundle)
    {
      super.restoreFromExtras(paramBundle);
      mUser = ((Person)paramBundle.getParcelable("android.messagingUser"));
      if (mUser == null)
      {
        CharSequence localCharSequence = paramBundle.getCharSequence("android.selfDisplayName");
        mUser = new Person.Builder().setName(localCharSequence).build();
      }
      mConversationTitle = paramBundle.getCharSequence("android.conversationTitle");
      mMessages = Message.getMessagesFromBundleArray(paramBundle.getParcelableArray("android.messages"));
      mHistoricMessages = Message.getMessagesFromBundleArray(paramBundle.getParcelableArray("android.messages.historic"));
      mIsGroupConversation = paramBundle.getBoolean("android.isGroupConversation");
    }
    
    public MessagingStyle setConversationTitle(CharSequence paramCharSequence)
    {
      mConversationTitle = paramCharSequence;
      return this;
    }
    
    public MessagingStyle setGroupConversation(boolean paramBoolean)
    {
      mIsGroupConversation = paramBoolean;
      return this;
    }
    
    public void validate(Context paramContext)
    {
      super.validate(paramContext);
      if ((getApplicationInfotargetSdkVersion >= 28) && ((mUser == null) || (mUser.getName() == null))) {
        throw new RuntimeException("User must be valid and have a name.");
      }
    }
    
    public static final class Message
    {
      static final String KEY_DATA_MIME_TYPE = "type";
      static final String KEY_DATA_URI = "uri";
      static final String KEY_EXTRAS_BUNDLE = "extras";
      static final String KEY_REMOTE_INPUT_HISTORY = "remote_input_history";
      static final String KEY_SENDER = "sender";
      static final String KEY_SENDER_PERSON = "sender_person";
      static final String KEY_TEXT = "text";
      static final String KEY_TIMESTAMP = "time";
      private String mDataMimeType;
      private Uri mDataUri;
      private Bundle mExtras = new Bundle();
      private final boolean mRemoteInputHistory;
      private final Person mSender;
      private final CharSequence mText;
      private final long mTimestamp;
      
      public Message(CharSequence paramCharSequence, long paramLong, Person paramPerson)
      {
        this(paramCharSequence, paramLong, paramPerson, false);
      }
      
      public Message(CharSequence paramCharSequence, long paramLong, Person paramPerson, boolean paramBoolean)
      {
        mText = paramCharSequence;
        mTimestamp = paramLong;
        mSender = paramPerson;
        mRemoteInputHistory = paramBoolean;
      }
      
      public Message(CharSequence paramCharSequence1, long paramLong, CharSequence paramCharSequence2)
      {
        this(paramCharSequence1, paramLong, paramCharSequence2);
      }
      
      static Bundle[] getBundleArrayForMessages(List<Message> paramList)
      {
        Bundle[] arrayOfBundle = new Bundle[paramList.size()];
        int i = paramList.size();
        for (int j = 0; j < i; j++) {
          arrayOfBundle[j] = ((Message)paramList.get(j)).toBundle();
        }
        return arrayOfBundle;
      }
      
      public static Message getMessageFromBundle(Bundle paramBundle)
      {
        try
        {
          if ((paramBundle.containsKey("text")) && (paramBundle.containsKey("time")))
          {
            Object localObject1 = (Person)paramBundle.getParcelable("sender_person");
            Object localObject2 = localObject1;
            if (localObject1 == null)
            {
              CharSequence localCharSequence = paramBundle.getCharSequence("sender");
              localObject2 = localObject1;
              if (localCharSequence != null)
              {
                localObject2 = new android/app/Person$Builder;
                ((Person.Builder)localObject2).<init>();
                localObject2 = ((Person.Builder)localObject2).setName(localCharSequence).build();
              }
            }
            localObject1 = new android/app/Notification$MessagingStyle$Message;
            ((Message)localObject1).<init>(paramBundle.getCharSequence("text"), paramBundle.getLong("time"), (Person)localObject2, paramBundle.getBoolean("remote_input_history", false));
            if ((paramBundle.containsKey("type")) && (paramBundle.containsKey("uri"))) {
              ((Message)localObject1).setData(paramBundle.getString("type"), (Uri)paramBundle.getParcelable("uri"));
            }
            if (paramBundle.containsKey("extras")) {
              ((Message)localObject1).getExtras().putAll(paramBundle.getBundle("extras"));
            }
            return localObject1;
          }
          return null;
        }
        catch (ClassCastException paramBundle) {}
        return null;
      }
      
      public static List<Message> getMessagesFromBundleArray(Parcelable[] paramArrayOfParcelable)
      {
        if (paramArrayOfParcelable == null) {
          return new ArrayList();
        }
        ArrayList localArrayList = new ArrayList(paramArrayOfParcelable.length);
        for (int i = 0; i < paramArrayOfParcelable.length; i++) {
          if ((paramArrayOfParcelable[i] instanceof Bundle))
          {
            Message localMessage = getMessageFromBundle((Bundle)paramArrayOfParcelable[i]);
            if (localMessage != null) {
              localArrayList.add(localMessage);
            }
          }
        }
        return localArrayList;
      }
      
      private Bundle toBundle()
      {
        Bundle localBundle = new Bundle();
        if (mText != null) {
          localBundle.putCharSequence("text", mText);
        }
        localBundle.putLong("time", mTimestamp);
        if (mSender != null)
        {
          localBundle.putCharSequence("sender", mSender.getName());
          localBundle.putParcelable("sender_person", mSender);
        }
        if (mDataMimeType != null) {
          localBundle.putString("type", mDataMimeType);
        }
        if (mDataUri != null) {
          localBundle.putParcelable("uri", mDataUri);
        }
        if (mExtras != null) {
          localBundle.putBundle("extras", mExtras);
        }
        if (mRemoteInputHistory) {
          localBundle.putBoolean("remote_input_history", mRemoteInputHistory);
        }
        return localBundle;
      }
      
      public String getDataMimeType()
      {
        return mDataMimeType;
      }
      
      public Uri getDataUri()
      {
        return mDataUri;
      }
      
      public Bundle getExtras()
      {
        return mExtras;
      }
      
      public CharSequence getSender()
      {
        CharSequence localCharSequence;
        if (mSender == null) {
          localCharSequence = null;
        } else {
          localCharSequence = mSender.getName();
        }
        return localCharSequence;
      }
      
      public Person getSenderPerson()
      {
        return mSender;
      }
      
      public CharSequence getText()
      {
        return mText;
      }
      
      public long getTimestamp()
      {
        return mTimestamp;
      }
      
      public boolean isRemoteInputHistory()
      {
        return mRemoteInputHistory;
      }
      
      public Message setData(String paramString, Uri paramUri)
      {
        mDataMimeType = paramString;
        mDataUri = paramUri;
        return this;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Priority {}
  
  private static class StandardTemplateParams
  {
    boolean ambient = false;
    boolean hasProgress = true;
    CharSequence headerTextSecondary;
    boolean hideLargeIcon;
    boolean hideReplyIcon;
    int maxRemoteInputHistory = 3;
    CharSequence text;
    CharSequence title;
    
    private StandardTemplateParams() {}
    
    final StandardTemplateParams ambient(boolean paramBoolean)
    {
      boolean bool;
      if ((title == null) && (text == null)) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "must set ambient before text");
      ambient = paramBoolean;
      return this;
    }
    
    final StandardTemplateParams fillTextsFrom(Notification.Builder paramBuilder)
    {
      Bundle localBundle = mN.extras;
      title = paramBuilder.processLegacyText(localBundle.getCharSequence("android.title"), ambient);
      CharSequence localCharSequence1 = localBundle.getCharSequence("android.bigText");
      CharSequence localCharSequence2;
      if (ambient)
      {
        localCharSequence2 = localCharSequence1;
        if (!TextUtils.isEmpty(localCharSequence1)) {}
      }
      else
      {
        localCharSequence2 = localBundle.getCharSequence("android.text");
      }
      text = paramBuilder.processLegacyText(localCharSequence2, ambient);
      return this;
    }
    
    final StandardTemplateParams hasProgress(boolean paramBoolean)
    {
      hasProgress = paramBoolean;
      return this;
    }
    
    final StandardTemplateParams headerTextSecondary(CharSequence paramCharSequence)
    {
      headerTextSecondary = paramCharSequence;
      return this;
    }
    
    final StandardTemplateParams hideLargeIcon(boolean paramBoolean)
    {
      hideLargeIcon = paramBoolean;
      return this;
    }
    
    final StandardTemplateParams hideReplyIcon(boolean paramBoolean)
    {
      hideReplyIcon = paramBoolean;
      return this;
    }
    
    final StandardTemplateParams reset()
    {
      hasProgress = true;
      ambient = false;
      title = null;
      text = null;
      headerTextSecondary = null;
      maxRemoteInputHistory = 3;
      return this;
    }
    
    public StandardTemplateParams setMaxRemoteInputHistory(int paramInt)
    {
      maxRemoteInputHistory = paramInt;
      return this;
    }
    
    final StandardTemplateParams text(CharSequence paramCharSequence)
    {
      text = paramCharSequence;
      return this;
    }
    
    final StandardTemplateParams title(CharSequence paramCharSequence)
    {
      title = paramCharSequence;
      return this;
    }
  }
  
  public static abstract class Style
  {
    static final int MAX_REMOTE_INPUT_HISTORY_LINES = 3;
    private CharSequence mBigContentTitle;
    protected Notification.Builder mBuilder;
    protected CharSequence mSummaryText = null;
    protected boolean mSummaryTextSet = false;
    
    public Style() {}
    
    public void addExtras(Bundle paramBundle)
    {
      if (mSummaryTextSet) {
        paramBundle.putCharSequence("android.summaryText", mSummaryText);
      }
      if (mBigContentTitle != null) {
        paramBundle.putCharSequence("android.title.big", mBigContentTitle);
      }
      paramBundle.putString("android.template", getClass().getName());
    }
    
    public abstract boolean areNotificationsVisiblyDifferent(Style paramStyle);
    
    public Notification build()
    {
      checkBuilder();
      return mBuilder.build();
    }
    
    public Notification buildStyled(Notification paramNotification)
    {
      addExtras(extras);
      return paramNotification;
    }
    
    protected void checkBuilder()
    {
      if (mBuilder != null) {
        return;
      }
      throw new IllegalArgumentException("Style requires a valid Builder object");
    }
    
    public boolean displayCustomViewInline()
    {
      return false;
    }
    
    public CharSequence getHeadsUpStatusBarText()
    {
      return null;
    }
    
    protected RemoteViews getStandardView(int paramInt)
    {
      return getStandardView(paramInt, null);
    }
    
    protected RemoteViews getStandardView(int paramInt, Notification.TemplateBindResult paramTemplateBindResult)
    {
      checkBuilder();
      CharSequence localCharSequence = mBuilder.getAllExtras().getCharSequence("android.title");
      if (mBigContentTitle != null) {
        mBuilder.setContentTitle(mBigContentTitle);
      }
      paramTemplateBindResult = mBuilder.applyStandardTemplateWithActions(paramInt, paramTemplateBindResult);
      mBuilder.getAllExtras().putCharSequence("android.title", localCharSequence);
      if ((mBigContentTitle != null) && (mBigContentTitle.equals(""))) {
        paramTemplateBindResult.setViewVisibility(16909087, 8);
      } else {
        paramTemplateBindResult.setViewVisibility(16909087, 0);
      }
      return paramTemplateBindResult;
    }
    
    protected boolean hasProgress()
    {
      return true;
    }
    
    public boolean hasSummaryInHeader()
    {
      return true;
    }
    
    protected void internalSetBigContentTitle(CharSequence paramCharSequence)
    {
      mBigContentTitle = paramCharSequence;
    }
    
    protected void internalSetSummaryText(CharSequence paramCharSequence)
    {
      mSummaryText = paramCharSequence;
      mSummaryTextSet = true;
    }
    
    public RemoteViews makeBigContentView()
    {
      return null;
    }
    
    public RemoteViews makeContentView(boolean paramBoolean)
    {
      return null;
    }
    
    public RemoteViews makeHeadsUpContentView(boolean paramBoolean)
    {
      return null;
    }
    
    public void purgeResources() {}
    
    public void reduceImageSizes(Context paramContext) {}
    
    protected void restoreFromExtras(Bundle paramBundle)
    {
      if (paramBundle.containsKey("android.summaryText"))
      {
        mSummaryText = paramBundle.getCharSequence("android.summaryText");
        mSummaryTextSet = true;
      }
      if (paramBundle.containsKey("android.title.big")) {
        mBigContentTitle = paramBundle.getCharSequence("android.title.big");
      }
    }
    
    public void setBuilder(Notification.Builder paramBuilder)
    {
      if (mBuilder != paramBuilder)
      {
        mBuilder = paramBuilder;
        if (mBuilder != null) {
          mBuilder.setStyle(this);
        }
      }
    }
    
    public void validate(Context paramContext) {}
  }
  
  private static class TemplateBindResult
  {
    int mIconMarginEnd;
    
    private TemplateBindResult() {}
    
    public int getIconMarginEnd()
    {
      return mIconMarginEnd;
    }
    
    public void setIconMarginEnd(int paramInt)
    {
      mIconMarginEnd = paramInt;
    }
  }
  
  @SystemApi
  public static final class TvExtender
    implements Notification.Extender
  {
    private static final String EXTRA_CHANNEL_ID = "channel_id";
    private static final String EXTRA_CONTENT_INTENT = "content_intent";
    private static final String EXTRA_DELETE_INTENT = "delete_intent";
    private static final String EXTRA_FLAGS = "flags";
    private static final String EXTRA_SUPPRESS_SHOW_OVER_APPS = "suppressShowOverApps";
    private static final String EXTRA_TV_EXTENDER = "android.tv.EXTENSIONS";
    private static final int FLAG_AVAILABLE_ON_TV = 1;
    private static final String TAG = "TvExtender";
    private String mChannelId;
    private PendingIntent mContentIntent;
    private PendingIntent mDeleteIntent;
    private int mFlags;
    private boolean mSuppressShowOverApps;
    
    public TvExtender()
    {
      mFlags = 1;
    }
    
    public TvExtender(Notification paramNotification)
    {
      if (extras == null) {
        paramNotification = null;
      } else {
        paramNotification = extras.getBundle("android.tv.EXTENSIONS");
      }
      if (paramNotification != null)
      {
        mFlags = paramNotification.getInt("flags");
        mChannelId = paramNotification.getString("channel_id");
        mSuppressShowOverApps = paramNotification.getBoolean("suppressShowOverApps");
        mContentIntent = ((PendingIntent)paramNotification.getParcelable("content_intent"));
        mDeleteIntent = ((PendingIntent)paramNotification.getParcelable("delete_intent"));
      }
    }
    
    public Notification.Builder extend(Notification.Builder paramBuilder)
    {
      Bundle localBundle = new Bundle();
      localBundle.putInt("flags", mFlags);
      localBundle.putString("channel_id", mChannelId);
      localBundle.putBoolean("suppressShowOverApps", mSuppressShowOverApps);
      if (mContentIntent != null) {
        localBundle.putParcelable("content_intent", mContentIntent);
      }
      if (mDeleteIntent != null) {
        localBundle.putParcelable("delete_intent", mDeleteIntent);
      }
      paramBuilder.getExtras().putBundle("android.tv.EXTENSIONS", localBundle);
      return paramBuilder;
    }
    
    @Deprecated
    public String getChannel()
    {
      return mChannelId;
    }
    
    public String getChannelId()
    {
      return mChannelId;
    }
    
    public PendingIntent getContentIntent()
    {
      return mContentIntent;
    }
    
    public PendingIntent getDeleteIntent()
    {
      return mDeleteIntent;
    }
    
    public boolean getSuppressShowOverApps()
    {
      return mSuppressShowOverApps;
    }
    
    public boolean isAvailableOnTv()
    {
      int i = mFlags;
      boolean bool = true;
      if ((i & 0x1) == 0) {
        bool = false;
      }
      return bool;
    }
    
    public TvExtender setChannel(String paramString)
    {
      mChannelId = paramString;
      return this;
    }
    
    public TvExtender setChannelId(String paramString)
    {
      mChannelId = paramString;
      return this;
    }
    
    public TvExtender setContentIntent(PendingIntent paramPendingIntent)
    {
      mContentIntent = paramPendingIntent;
      return this;
    }
    
    public TvExtender setDeleteIntent(PendingIntent paramPendingIntent)
    {
      mDeleteIntent = paramPendingIntent;
      return this;
    }
    
    public TvExtender setSuppressShowOverApps(boolean paramBoolean)
    {
      mSuppressShowOverApps = paramBoolean;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Visibility {}
  
  public static final class WearableExtender
    implements Notification.Extender
  {
    private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
    private static final int DEFAULT_FLAGS = 1;
    private static final int DEFAULT_GRAVITY = 80;
    private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
    private static final int FLAG_BIG_PICTURE_AMBIENT = 32;
    private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
    private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
    private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 64;
    private static final int FLAG_HINT_HIDE_ICON = 2;
    private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
    private static final int FLAG_START_SCROLL_BOTTOM = 8;
    private static final String KEY_ACTIONS = "actions";
    private static final String KEY_BACKGROUND = "background";
    private static final String KEY_BRIDGE_TAG = "bridgeTag";
    private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
    private static final String KEY_CONTENT_ICON = "contentIcon";
    private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
    private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
    private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
    private static final String KEY_DISMISSAL_ID = "dismissalId";
    private static final String KEY_DISPLAY_INTENT = "displayIntent";
    private static final String KEY_FLAGS = "flags";
    private static final String KEY_GRAVITY = "gravity";
    private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
    private static final String KEY_PAGES = "pages";
    public static final int SCREEN_TIMEOUT_LONG = -1;
    public static final int SCREEN_TIMEOUT_SHORT = 0;
    public static final int SIZE_DEFAULT = 0;
    public static final int SIZE_FULL_SCREEN = 5;
    public static final int SIZE_LARGE = 4;
    public static final int SIZE_MEDIUM = 3;
    public static final int SIZE_SMALL = 2;
    public static final int SIZE_XSMALL = 1;
    public static final int UNSET_ACTION_INDEX = -1;
    private ArrayList<Notification.Action> mActions = new ArrayList();
    private Bitmap mBackground;
    private String mBridgeTag;
    private int mContentActionIndex = -1;
    private int mContentIcon;
    private int mContentIconGravity = 8388613;
    private int mCustomContentHeight;
    private int mCustomSizePreset = 0;
    private String mDismissalId;
    private PendingIntent mDisplayIntent;
    private int mFlags = 1;
    private int mGravity = 80;
    private int mHintScreenTimeout;
    private ArrayList<Notification> mPages = new ArrayList();
    
    public WearableExtender() {}
    
    public WearableExtender(Notification paramNotification)
    {
      paramNotification = extras.getBundle("android.wearable.EXTENSIONS");
      if (paramNotification != null)
      {
        Object localObject = paramNotification.getParcelableArrayList("actions");
        if (localObject != null) {
          mActions.addAll((Collection)localObject);
        }
        mFlags = paramNotification.getInt("flags", 1);
        mDisplayIntent = ((PendingIntent)paramNotification.getParcelable("displayIntent"));
        localObject = Notification.getNotificationArrayFromBundle(paramNotification, "pages");
        if (localObject != null) {
          Collections.addAll(mPages, (Object[])localObject);
        }
        mBackground = ((Bitmap)paramNotification.getParcelable("background"));
        mContentIcon = paramNotification.getInt("contentIcon");
        mContentIconGravity = paramNotification.getInt("contentIconGravity", 8388613);
        mContentActionIndex = paramNotification.getInt("contentActionIndex", -1);
        mCustomSizePreset = paramNotification.getInt("customSizePreset", 0);
        mCustomContentHeight = paramNotification.getInt("customContentHeight");
        mGravity = paramNotification.getInt("gravity", 80);
        mHintScreenTimeout = paramNotification.getInt("hintScreenTimeout");
        mDismissalId = paramNotification.getString("dismissalId");
        mBridgeTag = paramNotification.getString("bridgeTag");
      }
    }
    
    private void setFlag(int paramInt, boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= paramInt;
      } else {
        mFlags &= paramInt;
      }
    }
    
    public WearableExtender addAction(Notification.Action paramAction)
    {
      mActions.add(paramAction);
      return this;
    }
    
    public WearableExtender addActions(List<Notification.Action> paramList)
    {
      mActions.addAll(paramList);
      return this;
    }
    
    public WearableExtender addPage(Notification paramNotification)
    {
      mPages.add(paramNotification);
      return this;
    }
    
    public WearableExtender addPages(List<Notification> paramList)
    {
      mPages.addAll(paramList);
      return this;
    }
    
    public WearableExtender clearActions()
    {
      mActions.clear();
      return this;
    }
    
    public WearableExtender clearPages()
    {
      mPages.clear();
      return this;
    }
    
    public WearableExtender clone()
    {
      WearableExtender localWearableExtender = new WearableExtender();
      mActions = new ArrayList(mActions);
      mFlags = mFlags;
      mDisplayIntent = mDisplayIntent;
      mPages = new ArrayList(mPages);
      mBackground = mBackground;
      mContentIcon = mContentIcon;
      mContentIconGravity = mContentIconGravity;
      mContentActionIndex = mContentActionIndex;
      mCustomSizePreset = mCustomSizePreset;
      mCustomContentHeight = mCustomContentHeight;
      mGravity = mGravity;
      mHintScreenTimeout = mHintScreenTimeout;
      mDismissalId = mDismissalId;
      mBridgeTag = mBridgeTag;
      return localWearableExtender;
    }
    
    public Notification.Builder extend(Notification.Builder paramBuilder)
    {
      Bundle localBundle = new Bundle();
      if (!mActions.isEmpty()) {
        localBundle.putParcelableArrayList("actions", mActions);
      }
      if (mFlags != 1) {
        localBundle.putInt("flags", mFlags);
      }
      if (mDisplayIntent != null) {
        localBundle.putParcelable("displayIntent", mDisplayIntent);
      }
      if (!mPages.isEmpty()) {
        localBundle.putParcelableArray("pages", (Parcelable[])mPages.toArray(new Notification[mPages.size()]));
      }
      if (mBackground != null) {
        localBundle.putParcelable("background", mBackground);
      }
      if (mContentIcon != 0) {
        localBundle.putInt("contentIcon", mContentIcon);
      }
      if (mContentIconGravity != 8388613) {
        localBundle.putInt("contentIconGravity", mContentIconGravity);
      }
      if (mContentActionIndex != -1) {
        localBundle.putInt("contentActionIndex", mContentActionIndex);
      }
      if (mCustomSizePreset != 0) {
        localBundle.putInt("customSizePreset", mCustomSizePreset);
      }
      if (mCustomContentHeight != 0) {
        localBundle.putInt("customContentHeight", mCustomContentHeight);
      }
      if (mGravity != 80) {
        localBundle.putInt("gravity", mGravity);
      }
      if (mHintScreenTimeout != 0) {
        localBundle.putInt("hintScreenTimeout", mHintScreenTimeout);
      }
      if (mDismissalId != null) {
        localBundle.putString("dismissalId", mDismissalId);
      }
      if (mBridgeTag != null) {
        localBundle.putString("bridgeTag", mBridgeTag);
      }
      paramBuilder.getExtras().putBundle("android.wearable.EXTENSIONS", localBundle);
      return paramBuilder;
    }
    
    public List<Notification.Action> getActions()
    {
      return mActions;
    }
    
    public Bitmap getBackground()
    {
      return mBackground;
    }
    
    public String getBridgeTag()
    {
      return mBridgeTag;
    }
    
    public int getContentAction()
    {
      return mContentActionIndex;
    }
    
    @Deprecated
    public int getContentIcon()
    {
      return mContentIcon;
    }
    
    @Deprecated
    public int getContentIconGravity()
    {
      return mContentIconGravity;
    }
    
    public boolean getContentIntentAvailableOffline()
    {
      int i = mFlags;
      boolean bool = true;
      if ((i & 0x1) == 0) {
        bool = false;
      }
      return bool;
    }
    
    @Deprecated
    public int getCustomContentHeight()
    {
      return mCustomContentHeight;
    }
    
    @Deprecated
    public int getCustomSizePreset()
    {
      return mCustomSizePreset;
    }
    
    public String getDismissalId()
    {
      return mDismissalId;
    }
    
    public PendingIntent getDisplayIntent()
    {
      return mDisplayIntent;
    }
    
    @Deprecated
    public int getGravity()
    {
      return mGravity;
    }
    
    public boolean getHintAmbientBigPicture()
    {
      boolean bool;
      if ((mFlags & 0x20) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    @Deprecated
    public boolean getHintAvoidBackgroundClipping()
    {
      boolean bool;
      if ((mFlags & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean getHintContentIntentLaunchesActivity()
    {
      boolean bool;
      if ((mFlags & 0x40) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    @Deprecated
    public boolean getHintHideIcon()
    {
      boolean bool;
      if ((mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    @Deprecated
    public int getHintScreenTimeout()
    {
      return mHintScreenTimeout;
    }
    
    @Deprecated
    public boolean getHintShowBackgroundOnly()
    {
      boolean bool;
      if ((mFlags & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public List<Notification> getPages()
    {
      return mPages;
    }
    
    public boolean getStartScrollBottom()
    {
      boolean bool;
      if ((mFlags & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public WearableExtender setBackground(Bitmap paramBitmap)
    {
      mBackground = paramBitmap;
      return this;
    }
    
    public WearableExtender setBridgeTag(String paramString)
    {
      mBridgeTag = paramString;
      return this;
    }
    
    public WearableExtender setContentAction(int paramInt)
    {
      mContentActionIndex = paramInt;
      return this;
    }
    
    @Deprecated
    public WearableExtender setContentIcon(int paramInt)
    {
      mContentIcon = paramInt;
      return this;
    }
    
    @Deprecated
    public WearableExtender setContentIconGravity(int paramInt)
    {
      mContentIconGravity = paramInt;
      return this;
    }
    
    public WearableExtender setContentIntentAvailableOffline(boolean paramBoolean)
    {
      setFlag(1, paramBoolean);
      return this;
    }
    
    @Deprecated
    public WearableExtender setCustomContentHeight(int paramInt)
    {
      mCustomContentHeight = paramInt;
      return this;
    }
    
    @Deprecated
    public WearableExtender setCustomSizePreset(int paramInt)
    {
      mCustomSizePreset = paramInt;
      return this;
    }
    
    public WearableExtender setDismissalId(String paramString)
    {
      mDismissalId = paramString;
      return this;
    }
    
    public WearableExtender setDisplayIntent(PendingIntent paramPendingIntent)
    {
      mDisplayIntent = paramPendingIntent;
      return this;
    }
    
    @Deprecated
    public WearableExtender setGravity(int paramInt)
    {
      mGravity = paramInt;
      return this;
    }
    
    public WearableExtender setHintAmbientBigPicture(boolean paramBoolean)
    {
      setFlag(32, paramBoolean);
      return this;
    }
    
    @Deprecated
    public WearableExtender setHintAvoidBackgroundClipping(boolean paramBoolean)
    {
      setFlag(16, paramBoolean);
      return this;
    }
    
    public WearableExtender setHintContentIntentLaunchesActivity(boolean paramBoolean)
    {
      setFlag(64, paramBoolean);
      return this;
    }
    
    @Deprecated
    public WearableExtender setHintHideIcon(boolean paramBoolean)
    {
      setFlag(2, paramBoolean);
      return this;
    }
    
    @Deprecated
    public WearableExtender setHintScreenTimeout(int paramInt)
    {
      mHintScreenTimeout = paramInt;
      return this;
    }
    
    @Deprecated
    public WearableExtender setHintShowBackgroundOnly(boolean paramBoolean)
    {
      setFlag(4, paramBoolean);
      return this;
    }
    
    public WearableExtender setStartScrollBottom(boolean paramBoolean)
    {
      setFlag(8, paramBoolean);
      return this;
    }
  }
}
