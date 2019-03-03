package android.app;

import android.annotation.SystemApi;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.System;
import android.service.notification.NotificationListenerService.Ranking;
import android.text.TextUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public final class NotificationChannel
  implements Parcelable
{
  private static final String ATT_BLOCKABLE_SYSTEM = "blockable_system";
  private static final String ATT_CONTENT_TYPE = "content_type";
  private static final String ATT_DELETED = "deleted";
  private static final String ATT_DESC = "desc";
  private static final String ATT_FG_SERVICE_SHOWN = "fgservice";
  private static final String ATT_FLAGS = "flags";
  private static final String ATT_GROUP = "group";
  private static final String ATT_ID = "id";
  private static final String ATT_IMPORTANCE = "importance";
  private static final String ATT_LIGHTS = "lights";
  private static final String ATT_LIGHT_COLOR = "light_color";
  private static final String ATT_NAME = "name";
  private static final String ATT_PRIORITY = "priority";
  private static final String ATT_SHOW_BADGE = "show_badge";
  private static final String ATT_SOUND = "sound";
  private static final String ATT_USAGE = "usage";
  private static final String ATT_USER_LOCKED = "locked";
  private static final String ATT_VIBRATION = "vibration";
  private static final String ATT_VIBRATION_ENABLED = "vibration_enabled";
  private static final String ATT_VISIBILITY = "visibility";
  public static final Parcelable.Creator<NotificationChannel> CREATOR = new Parcelable.Creator()
  {
    public NotificationChannel createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NotificationChannel(paramAnonymousParcel);
    }
    
    public NotificationChannel[] newArray(int paramAnonymousInt)
    {
      return new NotificationChannel[paramAnonymousInt];
    }
  };
  public static final String DEFAULT_CHANNEL_ID = "miscellaneous";
  private static final boolean DEFAULT_DELETED = false;
  private static final int DEFAULT_IMPORTANCE = -1000;
  private static final int DEFAULT_LIGHT_COLOR = 0;
  private static final boolean DEFAULT_SHOW_BADGE = true;
  private static final int DEFAULT_VISIBILITY = -1000;
  private static final String DELIMITER = ",";
  public static final int[] LOCKABLE_FIELDS = { 1, 2, 4, 8, 16, 32, 128 };
  private static final int MAX_TEXT_LENGTH = 1000;
  private static final String TAG_CHANNEL = "channel";
  public static final int USER_LOCKED_IMPORTANCE = 4;
  public static final int USER_LOCKED_LIGHTS = 8;
  public static final int USER_LOCKED_PRIORITY = 1;
  public static final int USER_LOCKED_SHOW_BADGE = 128;
  public static final int USER_LOCKED_SOUND = 32;
  public static final int USER_LOCKED_VIBRATION = 16;
  public static final int USER_LOCKED_VISIBILITY = 2;
  private AudioAttributes mAudioAttributes;
  private boolean mBlockableSystem;
  private boolean mBypassDnd;
  private boolean mDeleted;
  private String mDesc;
  private boolean mFgServiceShown;
  private String mGroup;
  private final String mId;
  private int mImportance = 64536;
  private int mLightColor;
  private boolean mLights;
  private int mLockscreenVisibility = 64536;
  private String mName;
  private boolean mShowBadge;
  private Uri mSound = Settings.System.DEFAULT_NOTIFICATION_URI;
  private int mUserLockedFields;
  private long[] mVibration;
  private boolean mVibrationEnabled;
  
  protected NotificationChannel(Parcel paramParcel)
  {
    boolean bool1 = false;
    mLightColor = 0;
    mShowBadge = true;
    mDeleted = false;
    mAudioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
    mBlockableSystem = false;
    int i = paramParcel.readByte();
    AudioAttributes localAudioAttributes = null;
    if (i != 0) {
      mId = paramParcel.readString();
    } else {
      mId = null;
    }
    if (paramParcel.readByte() != 0) {
      mName = paramParcel.readString();
    } else {
      mName = null;
    }
    if (paramParcel.readByte() != 0) {
      mDesc = paramParcel.readString();
    } else {
      mDesc = null;
    }
    mImportance = paramParcel.readInt();
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mBypassDnd = bool2;
    mLockscreenVisibility = paramParcel.readInt();
    if (paramParcel.readByte() != 0) {
      mSound = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    } else {
      mSound = null;
    }
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mLights = bool2;
    mVibration = paramParcel.createLongArray();
    mUserLockedFields = paramParcel.readInt();
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mFgServiceShown = bool2;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mVibrationEnabled = bool2;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mShowBadge = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    }
    mDeleted = bool2;
    if (paramParcel.readByte() != 0) {
      mGroup = paramParcel.readString();
    } else {
      mGroup = null;
    }
    if (paramParcel.readInt() > 0) {
      localAudioAttributes = (AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel);
    }
    mAudioAttributes = localAudioAttributes;
    mLightColor = paramParcel.readInt();
    mBlockableSystem = paramParcel.readBoolean();
  }
  
  public NotificationChannel(String paramString, CharSequence paramCharSequence, int paramInt)
  {
    mLightColor = 0;
    mShowBadge = true;
    mDeleted = false;
    mAudioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
    mBlockableSystem = false;
    mId = getTrimmedString(paramString);
    if (paramCharSequence != null) {
      paramString = getTrimmedString(paramCharSequence.toString());
    } else {
      paramString = null;
    }
    mName = paramString;
    mImportance = paramInt;
  }
  
  private Uri getSoundForBackup(Context paramContext)
  {
    Uri localUri = getSound();
    if (localUri == null) {
      return null;
    }
    paramContext = paramContext.getContentResolver().canonicalize(localUri);
    if (paramContext == null) {
      return Settings.System.DEFAULT_NOTIFICATION_URI;
    }
    return paramContext;
  }
  
  private String getTrimmedString(String paramString)
  {
    if ((paramString != null) && (paramString.length() > 1000)) {
      return paramString.substring(0, 1000);
    }
    return paramString;
  }
  
  private static String longArrayToString(long[] paramArrayOfLong)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if ((paramArrayOfLong != null) && (paramArrayOfLong.length > 0))
    {
      for (int i = 0; i < paramArrayOfLong.length - 1; i++)
      {
        localStringBuffer.append(paramArrayOfLong[i]);
        localStringBuffer.append(",");
      }
      localStringBuffer.append(paramArrayOfLong[(paramArrayOfLong.length - 1)]);
    }
    return localStringBuffer.toString();
  }
  
  private void populateFromXml(XmlPullParser paramXmlPullParser, boolean paramBoolean, Context paramContext)
  {
    boolean bool1 = true;
    boolean bool2;
    if ((paramBoolean) && (paramContext == null)) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    Preconditions.checkArgument(bool2, "forRestore is true but got null context");
    setDescription(paramXmlPullParser.getAttributeValue(null, "desc"));
    if (safeInt(paramXmlPullParser, "priority", 0) != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    setBypassDnd(bool2);
    setLockscreenVisibility(safeInt(paramXmlPullParser, "visibility", 64536));
    Uri localUri = safeUri(paramXmlPullParser, "sound");
    if (paramBoolean) {
      paramContext = restoreSoundUri(paramContext, localUri);
    } else {
      paramContext = localUri;
    }
    setSound(paramContext, safeAudioAttributes(paramXmlPullParser));
    enableLights(safeBool(paramXmlPullParser, "lights", false));
    setLightColor(safeInt(paramXmlPullParser, "light_color", 0));
    setVibrationPattern(safeLongArray(paramXmlPullParser, "vibration", null));
    enableVibration(safeBool(paramXmlPullParser, "vibration_enabled", false));
    setShowBadge(safeBool(paramXmlPullParser, "show_badge", false));
    setDeleted(safeBool(paramXmlPullParser, "deleted", false));
    setGroup(paramXmlPullParser.getAttributeValue(null, "group"));
    lockFields(safeInt(paramXmlPullParser, "locked", 0));
    setFgServiceShown(safeBool(paramXmlPullParser, "fgservice", false));
    setBlockableSystem(safeBool(paramXmlPullParser, "blockable_system", false));
  }
  
  private Uri restoreSoundUri(Context paramContext, Uri paramUri)
  {
    if (paramUri == null) {
      return null;
    }
    paramContext = paramContext.getContentResolver();
    paramUri = paramContext.canonicalize(paramUri);
    if (paramUri == null) {
      return Settings.System.DEFAULT_NOTIFICATION_URI;
    }
    return paramContext.uncanonicalize(paramUri);
  }
  
  private static AudioAttributes safeAudioAttributes(XmlPullParser paramXmlPullParser)
  {
    int i = safeInt(paramXmlPullParser, "usage", 5);
    int j = safeInt(paramXmlPullParser, "content_type", 4);
    int k = safeInt(paramXmlPullParser, "flags", 0);
    return new AudioAttributes.Builder().setUsage(i).setContentType(j).setFlags(k).build();
  }
  
  private static boolean safeBool(XmlPullParser paramXmlPullParser, String paramString, boolean paramBoolean)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return paramBoolean;
    }
    return Boolean.parseBoolean(paramXmlPullParser);
  }
  
  private static int safeInt(XmlPullParser paramXmlPullParser, String paramString, int paramInt)
  {
    return tryParseInt(paramXmlPullParser.getAttributeValue(null, paramString), paramInt);
  }
  
  private static long[] safeLongArray(XmlPullParser paramXmlPullParser, String paramString, long[] paramArrayOfLong)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return paramArrayOfLong;
    }
    paramArrayOfLong = paramXmlPullParser.split(",");
    paramXmlPullParser = new long[paramArrayOfLong.length];
    for (int i = 0; i < paramArrayOfLong.length; i++) {
      try
      {
        paramXmlPullParser[i] = Long.parseLong(paramArrayOfLong[i]);
      }
      catch (NumberFormatException paramString)
      {
        paramXmlPullParser[i] = 0L;
      }
    }
    return paramXmlPullParser;
  }
  
  private static Uri safeUri(XmlPullParser paramXmlPullParser, String paramString)
  {
    Object localObject = null;
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (paramXmlPullParser == null) {
      paramXmlPullParser = localObject;
    } else {
      paramXmlPullParser = Uri.parse(paramXmlPullParser);
    }
    return paramXmlPullParser;
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
  
  private void writeXml(XmlSerializer paramXmlSerializer, boolean paramBoolean, Context paramContext)
    throws IOException
  {
    boolean bool;
    if ((paramBoolean) && (paramContext == null)) {
      bool = false;
    } else {
      bool = true;
    }
    Preconditions.checkArgument(bool, "forBackup is true but got null context");
    paramXmlSerializer.startTag(null, "channel");
    paramXmlSerializer.attribute(null, "id", getId());
    if (getName() != null) {
      paramXmlSerializer.attribute(null, "name", getName().toString());
    }
    if (getDescription() != null) {
      paramXmlSerializer.attribute(null, "desc", getDescription());
    }
    if (getImportance() != 64536) {
      paramXmlSerializer.attribute(null, "importance", Integer.toString(getImportance()));
    }
    if (canBypassDnd()) {
      paramXmlSerializer.attribute(null, "priority", Integer.toString(2));
    }
    if (getLockscreenVisibility() != 64536) {
      paramXmlSerializer.attribute(null, "visibility", Integer.toString(getLockscreenVisibility()));
    }
    if (paramBoolean) {
      paramContext = getSoundForBackup(paramContext);
    } else {
      paramContext = getSound();
    }
    if (paramContext != null) {
      paramXmlSerializer.attribute(null, "sound", paramContext.toString());
    }
    if (getAudioAttributes() != null)
    {
      paramXmlSerializer.attribute(null, "usage", Integer.toString(getAudioAttributes().getUsage()));
      paramXmlSerializer.attribute(null, "content_type", Integer.toString(getAudioAttributes().getContentType()));
      paramXmlSerializer.attribute(null, "flags", Integer.toString(getAudioAttributes().getFlags()));
    }
    if (shouldShowLights()) {
      paramXmlSerializer.attribute(null, "lights", Boolean.toString(shouldShowLights()));
    }
    if (getLightColor() != 0) {
      paramXmlSerializer.attribute(null, "light_color", Integer.toString(getLightColor()));
    }
    if (shouldVibrate()) {
      paramXmlSerializer.attribute(null, "vibration_enabled", Boolean.toString(shouldVibrate()));
    }
    if (getVibrationPattern() != null) {
      paramXmlSerializer.attribute(null, "vibration", longArrayToString(getVibrationPattern()));
    }
    if (getUserLockedFields() != 0) {
      paramXmlSerializer.attribute(null, "locked", Integer.toString(getUserLockedFields()));
    }
    if (isFgServiceShown()) {
      paramXmlSerializer.attribute(null, "fgservice", Boolean.toString(isFgServiceShown()));
    }
    if (canShowBadge()) {
      paramXmlSerializer.attribute(null, "show_badge", Boolean.toString(canShowBadge()));
    }
    if (isDeleted()) {
      paramXmlSerializer.attribute(null, "deleted", Boolean.toString(isDeleted()));
    }
    if (getGroup() != null) {
      paramXmlSerializer.attribute(null, "group", getGroup());
    }
    if (isBlockableSystem()) {
      paramXmlSerializer.attribute(null, "blockable_system", Boolean.toString(isBlockableSystem()));
    }
    paramXmlSerializer.endTag(null, "channel");
  }
  
  public boolean canBypassDnd()
  {
    return mBypassDnd;
  }
  
  public boolean canShowBadge()
  {
    return mShowBadge;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void enableLights(boolean paramBoolean)
  {
    mLights = paramBoolean;
  }
  
  public void enableVibration(boolean paramBoolean)
  {
    mVibrationEnabled = paramBoolean;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (NotificationChannel)paramObject;
      if (getImportance() != paramObject.getImportance()) {
        return false;
      }
      if (mBypassDnd != mBypassDnd) {
        return false;
      }
      if (getLockscreenVisibility() != paramObject.getLockscreenVisibility()) {
        return false;
      }
      if (mLights != mLights) {
        return false;
      }
      if (getLightColor() != paramObject.getLightColor()) {
        return false;
      }
      if (getUserLockedFields() != paramObject.getUserLockedFields()) {
        return false;
      }
      if (mVibrationEnabled != mVibrationEnabled) {
        return false;
      }
      if (mShowBadge != mShowBadge) {
        return false;
      }
      if (isDeleted() != paramObject.isDeleted()) {
        return false;
      }
      if (isBlockableSystem() != paramObject.isBlockableSystem()) {
        return false;
      }
      if (getId() != null ? !getId().equals(paramObject.getId()) : paramObject.getId() != null) {
        return false;
      }
      if (getName() != null ? !getName().equals(paramObject.getName()) : paramObject.getName() != null) {
        return false;
      }
      if (getDescription() != null ? !getDescription().equals(paramObject.getDescription()) : paramObject.getDescription() != null) {
        return false;
      }
      if (getSound() != null ? !getSound().equals(paramObject.getSound()) : paramObject.getSound() != null) {
        return false;
      }
      if (!Arrays.equals(mVibration, mVibration)) {
        return false;
      }
      if (getGroup() != null ? !getGroup().equals(paramObject.getGroup()) : paramObject.getGroup() != null) {
        return false;
      }
      if (getAudioAttributes() != null) {
        bool = getAudioAttributes().equals(paramObject.getAudioAttributes());
      } else if (paramObject.getAudioAttributes() != null) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public AudioAttributes getAudioAttributes()
  {
    return mAudioAttributes;
  }
  
  public String getDescription()
  {
    return mDesc;
  }
  
  public String getGroup()
  {
    return mGroup;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public int getImportance()
  {
    return mImportance;
  }
  
  public int getLightColor()
  {
    return mLightColor;
  }
  
  public int getLockscreenVisibility()
  {
    return mLockscreenVisibility;
  }
  
  public CharSequence getName()
  {
    return mName;
  }
  
  public Uri getSound()
  {
    return mSound;
  }
  
  @SystemApi
  public int getUserLockedFields()
  {
    return mUserLockedFields;
  }
  
  public long[] getVibrationPattern()
  {
    return mVibration;
  }
  
  public int hashCode()
  {
    String str = getId();
    int i = 0;
    int j;
    if (str != null) {
      j = getId().hashCode();
    } else {
      j = 0;
    }
    int k;
    if (getName() != null) {
      k = getName().hashCode();
    } else {
      k = 0;
    }
    int m;
    if (getDescription() != null) {
      m = getDescription().hashCode();
    } else {
      m = 0;
    }
    int n = getImportance();
    int i1 = mBypassDnd;
    int i2 = getLockscreenVisibility();
    int i3;
    if (getSound() != null) {
      i3 = getSound().hashCode();
    } else {
      i3 = 0;
    }
    int i4 = mLights;
    int i5 = getLightColor();
    int i6 = Arrays.hashCode(mVibration);
    int i7 = getUserLockedFields();
    int i8 = mVibrationEnabled;
    int i9 = mShowBadge;
    int i10 = isDeleted();
    int i11;
    if (getGroup() != null) {
      i11 = getGroup().hashCode();
    } else {
      i11 = 0;
    }
    if (getAudioAttributes() != null) {
      i = getAudioAttributes().hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * j + k) + m) + n) + i1) + i2) + i3) + i4) + i5) + i6) + i7) + i8) + i9) + i10) + i11) + i) + isBlockableSystem();
  }
  
  public boolean isBlockableSystem()
  {
    return mBlockableSystem;
  }
  
  @SystemApi
  public boolean isDeleted()
  {
    return mDeleted;
  }
  
  public boolean isFgServiceShown()
  {
    return mFgServiceShown;
  }
  
  public void lockFields(int paramInt)
  {
    mUserLockedFields |= paramInt;
  }
  
  @SystemApi
  public void populateFromXml(XmlPullParser paramXmlPullParser)
  {
    populateFromXml(paramXmlPullParser, false, null);
  }
  
  public void populateFromXmlForRestore(XmlPullParser paramXmlPullParser, Context paramContext)
  {
    populateFromXml(paramXmlPullParser, true, paramContext);
  }
  
  public void setBlockableSystem(boolean paramBoolean)
  {
    mBlockableSystem = paramBoolean;
  }
  
  public void setBypassDnd(boolean paramBoolean)
  {
    mBypassDnd = paramBoolean;
  }
  
  public void setDeleted(boolean paramBoolean)
  {
    mDeleted = paramBoolean;
  }
  
  public void setDescription(String paramString)
  {
    mDesc = getTrimmedString(paramString);
  }
  
  public void setFgServiceShown(boolean paramBoolean)
  {
    mFgServiceShown = paramBoolean;
  }
  
  public void setGroup(String paramString)
  {
    mGroup = paramString;
  }
  
  public void setImportance(int paramInt)
  {
    mImportance = paramInt;
  }
  
  public void setLightColor(int paramInt)
  {
    mLightColor = paramInt;
  }
  
  public void setLockscreenVisibility(int paramInt)
  {
    mLockscreenVisibility = paramInt;
  }
  
  public void setName(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null) {
      paramCharSequence = getTrimmedString(paramCharSequence.toString());
    } else {
      paramCharSequence = null;
    }
    mName = paramCharSequence;
  }
  
  public void setShowBadge(boolean paramBoolean)
  {
    mShowBadge = paramBoolean;
  }
  
  public void setSound(Uri paramUri, AudioAttributes paramAudioAttributes)
  {
    mSound = paramUri;
    mAudioAttributes = paramAudioAttributes;
  }
  
  public void setVibrationPattern(long[] paramArrayOfLong)
  {
    boolean bool;
    if ((paramArrayOfLong != null) && (paramArrayOfLong.length > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    mVibrationEnabled = bool;
    mVibration = paramArrayOfLong;
  }
  
  public boolean shouldShowLights()
  {
    return mLights;
  }
  
  public boolean shouldVibrate()
  {
    return mVibrationEnabled;
  }
  
  @SystemApi
  public JSONObject toJson()
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject();
    localJSONObject.put("id", getId());
    localJSONObject.put("name", getName());
    localJSONObject.put("desc", getDescription());
    if (getImportance() != 64536) {
      localJSONObject.put("importance", NotificationListenerService.Ranking.importanceToString(getImportance()));
    }
    if (canBypassDnd()) {
      localJSONObject.put("priority", 2);
    }
    if (getLockscreenVisibility() != 64536) {
      localJSONObject.put("visibility", Notification.visibilityToString(getLockscreenVisibility()));
    }
    if (getSound() != null) {
      localJSONObject.put("sound", getSound().toString());
    }
    if (getAudioAttributes() != null)
    {
      localJSONObject.put("usage", Integer.toString(getAudioAttributes().getUsage()));
      localJSONObject.put("content_type", Integer.toString(getAudioAttributes().getContentType()));
      localJSONObject.put("flags", Integer.toString(getAudioAttributes().getFlags()));
    }
    localJSONObject.put("lights", Boolean.toString(shouldShowLights()));
    localJSONObject.put("light_color", Integer.toString(getLightColor()));
    localJSONObject.put("vibration_enabled", Boolean.toString(shouldVibrate()));
    localJSONObject.put("locked", Integer.toString(getUserLockedFields()));
    localJSONObject.put("fgservice", Boolean.toString(isFgServiceShown()));
    localJSONObject.put("vibration", longArrayToString(getVibrationPattern()));
    localJSONObject.put("show_badge", Boolean.toString(canShowBadge()));
    localJSONObject.put("deleted", Boolean.toString(isDeleted()));
    localJSONObject.put("group", getGroup());
    localJSONObject.put("blockable_system", isBlockableSystem());
    return localJSONObject;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NotificationChannel{mId='");
    localStringBuilder.append(mId);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mName=");
    localStringBuilder.append(mName);
    localStringBuilder.append(", mDescription=");
    String str;
    if (!TextUtils.isEmpty(mDesc)) {
      str = "hasDescription ";
    } else {
      str = "";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", mImportance=");
    localStringBuilder.append(mImportance);
    localStringBuilder.append(", mBypassDnd=");
    localStringBuilder.append(mBypassDnd);
    localStringBuilder.append(", mLockscreenVisibility=");
    localStringBuilder.append(mLockscreenVisibility);
    localStringBuilder.append(", mSound=");
    localStringBuilder.append(mSound);
    localStringBuilder.append(", mLights=");
    localStringBuilder.append(mLights);
    localStringBuilder.append(", mLightColor=");
    localStringBuilder.append(mLightColor);
    localStringBuilder.append(", mVibration=");
    localStringBuilder.append(Arrays.toString(mVibration));
    localStringBuilder.append(", mUserLockedFields=");
    localStringBuilder.append(Integer.toHexString(mUserLockedFields));
    localStringBuilder.append(", mFgServiceShown=");
    localStringBuilder.append(mFgServiceShown);
    localStringBuilder.append(", mVibrationEnabled=");
    localStringBuilder.append(mVibrationEnabled);
    localStringBuilder.append(", mShowBadge=");
    localStringBuilder.append(mShowBadge);
    localStringBuilder.append(", mDeleted=");
    localStringBuilder.append(mDeleted);
    localStringBuilder.append(", mGroup='");
    localStringBuilder.append(mGroup);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mAudioAttributes=");
    localStringBuilder.append(mAudioAttributes);
    localStringBuilder.append(", mBlockableSystem=");
    localStringBuilder.append(mBlockableSystem);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void unlockFields(int paramInt)
  {
    mUserLockedFields &= paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mId != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mId);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    if (mName != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mName);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    if (mDesc != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mDesc);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeInt(mImportance);
    paramParcel.writeByte(mBypassDnd);
    paramParcel.writeInt(mLockscreenVisibility);
    if (mSound != null)
    {
      paramParcel.writeByte((byte)1);
      mSound.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeByte(mLights);
    paramParcel.writeLongArray(mVibration);
    paramParcel.writeInt(mUserLockedFields);
    paramParcel.writeByte(mFgServiceShown);
    paramParcel.writeByte(mVibrationEnabled);
    paramParcel.writeByte(mShowBadge);
    paramParcel.writeByte(mDeleted);
    if (mGroup != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mGroup);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    if (mAudioAttributes != null)
    {
      paramParcel.writeInt(1);
      mAudioAttributes.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mLightColor);
    paramParcel.writeBoolean(mBlockableSystem);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, mId);
    paramProtoOutputStream.write(1138166333442L, mName);
    paramProtoOutputStream.write(1138166333443L, mDesc);
    paramProtoOutputStream.write(1120986464260L, mImportance);
    paramProtoOutputStream.write(1133871366149L, mBypassDnd);
    paramProtoOutputStream.write(1120986464262L, mLockscreenVisibility);
    if (mSound != null) {
      paramProtoOutputStream.write(1138166333447L, mSound.toString());
    }
    paramProtoOutputStream.write(1133871366152L, mLights);
    paramProtoOutputStream.write(1120986464265L, mLightColor);
    if (mVibration != null)
    {
      long[] arrayOfLong = mVibration;
      int i = arrayOfLong.length;
      for (int j = 0; j < i; j++) {
        paramProtoOutputStream.write(2211908157450L, arrayOfLong[j]);
      }
    }
    paramProtoOutputStream.write(1120986464267L, mUserLockedFields);
    paramProtoOutputStream.write(1133871366162L, mFgServiceShown);
    paramProtoOutputStream.write(1133871366156L, mVibrationEnabled);
    paramProtoOutputStream.write(1133871366157L, mShowBadge);
    paramProtoOutputStream.write(1133871366158L, mDeleted);
    paramProtoOutputStream.write(1138166333455L, mGroup);
    if (mAudioAttributes != null) {
      mAudioAttributes.writeToProto(paramProtoOutputStream, 1146756268048L);
    }
    paramProtoOutputStream.write(1133871366161L, mBlockableSystem);
    paramProtoOutputStream.end(paramLong);
  }
  
  @SystemApi
  public void writeXml(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    writeXml(paramXmlSerializer, false, null);
  }
  
  public void writeXmlForBackup(XmlSerializer paramXmlSerializer, Context paramContext)
    throws IOException
  {
    writeXml(paramXmlSerializer, true, paramContext);
  }
}
