package android.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Set;

public final class ShortcutInfo
  implements Parcelable
{
  private static final String ANDROID_PACKAGE_NAME = "android";
  public static final int CLONE_REMOVE_FOR_CREATOR = 9;
  public static final int CLONE_REMOVE_FOR_LAUNCHER = 11;
  public static final int CLONE_REMOVE_FOR_LAUNCHER_APPROVAL = 10;
  private static final int CLONE_REMOVE_ICON = 1;
  private static final int CLONE_REMOVE_INTENT = 2;
  public static final int CLONE_REMOVE_NON_KEY_INFO = 4;
  public static final int CLONE_REMOVE_RES_NAMES = 8;
  public static final Parcelable.Creator<ShortcutInfo> CREATOR = new Parcelable.Creator()
  {
    public ShortcutInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ShortcutInfo(paramAnonymousParcel, null);
    }
    
    public ShortcutInfo[] newArray(int paramAnonymousInt)
    {
      return new ShortcutInfo[paramAnonymousInt];
    }
  };
  public static final int DISABLED_REASON_APP_CHANGED = 2;
  public static final int DISABLED_REASON_BACKUP_NOT_SUPPORTED = 101;
  public static final int DISABLED_REASON_BY_APP = 1;
  public static final int DISABLED_REASON_NOT_DISABLED = 0;
  public static final int DISABLED_REASON_OTHER_RESTORE_ISSUE = 103;
  private static final int DISABLED_REASON_RESTORE_ISSUE_START = 100;
  public static final int DISABLED_REASON_SIGNATURE_MISMATCH = 102;
  public static final int DISABLED_REASON_UNKNOWN = 3;
  public static final int DISABLED_REASON_VERSION_LOWER = 100;
  public static final int FLAG_ADAPTIVE_BITMAP = 512;
  public static final int FLAG_DISABLED = 64;
  public static final int FLAG_DYNAMIC = 1;
  public static final int FLAG_HAS_ICON_FILE = 8;
  public static final int FLAG_HAS_ICON_RES = 4;
  public static final int FLAG_ICON_FILE_PENDING_SAVE = 2048;
  public static final int FLAG_IMMUTABLE = 256;
  public static final int FLAG_KEY_FIELDS_ONLY = 16;
  public static final int FLAG_MANIFEST = 32;
  public static final int FLAG_PINNED = 2;
  public static final int FLAG_RETURNED_BY_SERVICE = 1024;
  public static final int FLAG_SHADOW = 4096;
  public static final int FLAG_STRINGS_RESOLVED = 128;
  private static final int IMPLICIT_RANK_MASK = Integer.MAX_VALUE;
  private static final int RANK_CHANGED_BIT = Integer.MIN_VALUE;
  public static final int RANK_NOT_SET = Integer.MAX_VALUE;
  private static final String RES_TYPE_STRING = "string";
  public static final String SHORTCUT_CATEGORY_CONVERSATION = "android.shortcut.conversation";
  static final String TAG = "Shortcut";
  public static final int VERSION_CODE_UNKNOWN = -1;
  private ComponentName mActivity;
  private String mBitmapPath;
  private ArraySet<String> mCategories;
  private CharSequence mDisabledMessage;
  private int mDisabledMessageResId;
  private String mDisabledMessageResName;
  private int mDisabledReason;
  private PersistableBundle mExtras;
  private int mFlags;
  private Icon mIcon;
  private int mIconResId;
  private String mIconResName;
  private final String mId;
  private int mImplicitRank;
  private PersistableBundle[] mIntentPersistableExtrases;
  private Intent[] mIntents;
  private long mLastChangedTimestamp;
  private final String mPackageName;
  private int mRank;
  private CharSequence mText;
  private int mTextResId;
  private String mTextResName;
  private CharSequence mTitle;
  private int mTitleResId;
  private String mTitleResName;
  private final int mUserId;
  
  public ShortcutInfo(int paramInt1, String paramString1, String paramString2, ComponentName paramComponentName, Icon paramIcon, CharSequence paramCharSequence1, int paramInt2, String paramString3, CharSequence paramCharSequence2, int paramInt3, String paramString4, CharSequence paramCharSequence3, int paramInt4, String paramString5, Set<String> paramSet, Intent[] paramArrayOfIntent, int paramInt5, PersistableBundle paramPersistableBundle, long paramLong, int paramInt6, int paramInt7, String paramString6, String paramString7, int paramInt8)
  {
    mUserId = paramInt1;
    mId = paramString1;
    mPackageName = paramString2;
    mActivity = paramComponentName;
    mIcon = paramIcon;
    mTitle = paramCharSequence1;
    mTitleResId = paramInt2;
    mTitleResName = paramString3;
    mText = paramCharSequence2;
    mTextResId = paramInt3;
    mTextResName = paramString4;
    mDisabledMessage = paramCharSequence3;
    mDisabledMessageResId = paramInt4;
    mDisabledMessageResName = paramString5;
    mCategories = cloneCategories(paramSet);
    mIntents = cloneIntents(paramArrayOfIntent);
    fixUpIntentExtras();
    mRank = paramInt5;
    mExtras = paramPersistableBundle;
    mLastChangedTimestamp = paramLong;
    mFlags = paramInt6;
    mIconResId = paramInt7;
    mIconResName = paramString6;
    mBitmapPath = paramString7;
    mDisabledReason = paramInt8;
  }
  
  private ShortcutInfo(Builder paramBuilder)
  {
    mUserId = mContext.getUserId();
    mId = ((String)Preconditions.checkStringNotEmpty(mId, "Shortcut ID must be provided"));
    mPackageName = mContext.getPackageName();
    mActivity = mActivity;
    mIcon = mIcon;
    mTitle = mTitle;
    mTitleResId = mTitleResId;
    mText = mText;
    mTextResId = mTextResId;
    mDisabledMessage = mDisabledMessage;
    mDisabledMessageResId = mDisabledMessageResId;
    mCategories = cloneCategories(mCategories);
    mIntents = cloneIntents(mIntents);
    fixUpIntentExtras();
    mRank = mRank;
    mExtras = mExtras;
    updateTimestamp();
  }
  
  private ShortcutInfo(ShortcutInfo paramShortcutInfo, int paramInt)
  {
    mUserId = mUserId;
    mId = mId;
    mPackageName = mPackageName;
    mActivity = mActivity;
    mFlags = mFlags;
    mLastChangedTimestamp = mLastChangedTimestamp;
    mDisabledReason = mDisabledReason;
    mIconResId = mIconResId;
    if ((paramInt & 0x4) == 0)
    {
      if ((paramInt & 0x1) == 0)
      {
        mIcon = mIcon;
        mBitmapPath = mBitmapPath;
      }
      mTitle = mTitle;
      mTitleResId = mTitleResId;
      mText = mText;
      mTextResId = mTextResId;
      mDisabledMessage = mDisabledMessage;
      mDisabledMessageResId = mDisabledMessageResId;
      mCategories = cloneCategories(mCategories);
      if ((paramInt & 0x2) == 0)
      {
        mIntents = cloneIntents(mIntents);
        mIntentPersistableExtrases = clonePersistableBundle(mIntentPersistableExtrases);
      }
      mRank = mRank;
      mExtras = mExtras;
      if ((paramInt & 0x8) == 0)
      {
        mTitleResName = mTitleResName;
        mTextResName = mTextResName;
        mDisabledMessageResName = mDisabledMessageResName;
        mIconResName = mIconResName;
      }
    }
    else
    {
      mFlags |= 0x10;
    }
  }
  
  private ShortcutInfo(Parcel paramParcel)
  {
    ClassLoader localClassLoader = getClass().getClassLoader();
    mUserId = paramParcel.readInt();
    mId = paramParcel.readString();
    mPackageName = paramParcel.readString();
    mActivity = ((ComponentName)paramParcel.readParcelable(localClassLoader));
    mFlags = paramParcel.readInt();
    mIconResId = paramParcel.readInt();
    mLastChangedTimestamp = paramParcel.readLong();
    mDisabledReason = paramParcel.readInt();
    if (paramParcel.readInt() == 0) {
      return;
    }
    mIcon = ((Icon)paramParcel.readParcelable(localClassLoader));
    mTitle = paramParcel.readCharSequence();
    mTitleResId = paramParcel.readInt();
    mText = paramParcel.readCharSequence();
    mTextResId = paramParcel.readInt();
    mDisabledMessage = paramParcel.readCharSequence();
    mDisabledMessageResId = paramParcel.readInt();
    mIntents = ((Intent[])paramParcel.readParcelableArray(localClassLoader, Intent.class));
    mIntentPersistableExtrases = ((PersistableBundle[])paramParcel.readParcelableArray(localClassLoader, PersistableBundle.class));
    mRank = paramParcel.readInt();
    mExtras = ((PersistableBundle)paramParcel.readParcelable(localClassLoader));
    mBitmapPath = paramParcel.readString();
    mIconResName = paramParcel.readString();
    mTitleResName = paramParcel.readString();
    mTextResName = paramParcel.readString();
    mDisabledMessageResName = paramParcel.readString();
    int i = paramParcel.readInt();
    if (i == 0)
    {
      mCategories = null;
    }
    else
    {
      mCategories = new ArraySet(i);
      for (int j = 0; j < i; j++) {
        mCategories.add(paramParcel.readString().intern());
      }
    }
  }
  
  private void addIndentOrComma(StringBuilder paramStringBuilder, String paramString)
  {
    if (paramString != null)
    {
      paramStringBuilder.append("\n  ");
      paramStringBuilder.append(paramString);
    }
    else
    {
      paramStringBuilder.append(", ");
    }
  }
  
  private static ArraySet<String> cloneCategories(Set<String> paramSet)
  {
    if (paramSet == null) {
      return null;
    }
    ArraySet localArraySet = new ArraySet(paramSet.size());
    paramSet = paramSet.iterator();
    while (paramSet.hasNext())
    {
      CharSequence localCharSequence = (CharSequence)paramSet.next();
      if (!TextUtils.isEmpty(localCharSequence)) {
        localArraySet.add(localCharSequence.toString().intern());
      }
    }
    return localArraySet;
  }
  
  private static Intent[] cloneIntents(Intent[] paramArrayOfIntent)
  {
    if (paramArrayOfIntent == null) {
      return null;
    }
    Intent[] arrayOfIntent = new Intent[paramArrayOfIntent.length];
    for (int i = 0; i < arrayOfIntent.length; i++) {
      if (paramArrayOfIntent[i] != null) {
        arrayOfIntent[i] = new Intent(paramArrayOfIntent[i]);
      }
    }
    return arrayOfIntent;
  }
  
  private static PersistableBundle[] clonePersistableBundle(PersistableBundle[] paramArrayOfPersistableBundle)
  {
    if (paramArrayOfPersistableBundle == null) {
      return null;
    }
    PersistableBundle[] arrayOfPersistableBundle = new PersistableBundle[paramArrayOfPersistableBundle.length];
    for (int i = 0; i < arrayOfPersistableBundle.length; i++) {
      if (paramArrayOfPersistableBundle[i] != null) {
        arrayOfPersistableBundle[i] = new PersistableBundle(paramArrayOfPersistableBundle[i]);
      }
    }
    return arrayOfPersistableBundle;
  }
  
  private void fixUpIntentExtras()
  {
    if (mIntents == null)
    {
      mIntentPersistableExtrases = null;
      return;
    }
    mIntentPersistableExtrases = new PersistableBundle[mIntents.length];
    for (int i = 0; i < mIntents.length; i++)
    {
      Intent localIntent = mIntents[i];
      Bundle localBundle = localIntent.getExtras();
      if (localBundle == null)
      {
        mIntentPersistableExtrases[i] = null;
      }
      else
      {
        mIntentPersistableExtrases[i] = new PersistableBundle(localBundle);
        localIntent.replaceExtras((Bundle)null);
      }
    }
  }
  
  public static String getDisabledReasonDebugString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("[Disabled: unknown reason:");
        localStringBuilder.append(paramInt);
        localStringBuilder.append("]");
        return localStringBuilder.toString();
      case 103: 
        return "[Disabled: unknown restore issue]";
      case 102: 
        return "[Disabled: signature mismatch]";
      case 101: 
        return "[Disabled: backup not supported]";
      }
      return "[Disabled: lower version]";
    case 2: 
      return "[Disabled: app changed]";
    case 1: 
      return "[Disabled: by app]";
    }
    return "[Not disabled]";
  }
  
  public static String getDisabledReasonForRestoreIssue(Context paramContext, int paramInt)
  {
    paramContext = paramContext.getResources();
    if (paramInt != 3)
    {
      switch (paramInt)
      {
      default: 
        return null;
      case 103: 
        return paramContext.getString(17040998);
      case 102: 
        return paramContext.getString(17040997);
      case 101: 
        return paramContext.getString(17040996);
      }
      return paramContext.getString(17040999);
    }
    return paramContext.getString(17040995);
  }
  
  public static IllegalArgumentException getInvalidIconException()
  {
    return new IllegalArgumentException("Unsupported icon type: only the bitmap and resource types are supported");
  }
  
  @VisibleForTesting
  public static String getResourceEntryName(String paramString)
  {
    int i = paramString.indexOf('/');
    if (i < 0) {
      return null;
    }
    return paramString.substring(i + 1);
  }
  
  @VisibleForTesting
  public static String getResourcePackageName(String paramString)
  {
    int i = paramString.indexOf(':');
    if (i < 0) {
      return null;
    }
    return paramString.substring(0, i);
  }
  
  private CharSequence getResourceString(Resources paramResources, int paramInt, CharSequence paramCharSequence)
  {
    try
    {
      paramResources = paramResources.getString(paramInt);
      return paramResources;
    }
    catch (Resources.NotFoundException paramResources)
    {
      paramResources = new StringBuilder();
      paramResources.append("Resource for ID=");
      paramResources.append(paramInt);
      paramResources.append(" not found in package ");
      paramResources.append(mPackageName);
      Log.e("Shortcut", paramResources.toString());
    }
    return paramCharSequence;
  }
  
  @VisibleForTesting
  public static String getResourceTypeAndEntryName(String paramString)
  {
    int i = paramString.indexOf(':');
    if (i < 0) {
      return null;
    }
    return paramString.substring(i + 1);
  }
  
  @VisibleForTesting
  public static String getResourceTypeName(String paramString)
  {
    int i = paramString.indexOf(':');
    if (i < 0) {
      return null;
    }
    int j = paramString.indexOf('/', i + 1);
    if (j < 0) {
      return null;
    }
    return paramString.substring(i + 1, j);
  }
  
  public static boolean isDisabledForRestoreIssue(int paramInt)
  {
    boolean bool;
    if (paramInt >= 100) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @VisibleForTesting
  public static int lookUpResourceId(Resources paramResources, String paramString1, String paramString2, String paramString3)
  {
    if (paramString1 == null) {
      return 0;
    }
    try
    {
      i = Integer.parseInt(paramString1);
      return i;
    }
    catch (Resources.NotFoundException paramResources) {}catch (NumberFormatException localNumberFormatException)
    {
      int i = paramResources.getIdentifier(paramString1, paramString2, paramString3);
      return i;
    }
    paramResources = new StringBuilder();
    paramResources.append("Resource ID for name=");
    paramResources.append(paramString1);
    paramResources.append(" not found in package ");
    paramResources.append(paramString3);
    Log.e("Shortcut", paramResources.toString());
    return 0;
  }
  
  @VisibleForTesting
  public static String lookUpResourceName(Resources paramResources, int paramInt, boolean paramBoolean, String paramString)
  {
    if (paramInt == 0) {
      return null;
    }
    try
    {
      paramResources = paramResources.getResourceName(paramInt);
      if ("android".equals(getResourcePackageName(paramResources))) {
        return String.valueOf(paramInt);
      }
      if (paramBoolean) {
        paramResources = getResourceTypeAndEntryName(paramResources);
      } else {
        paramResources = getResourceEntryName(paramResources);
      }
      return paramResources;
    }
    catch (Resources.NotFoundException paramResources)
    {
      paramResources = new StringBuilder();
      paramResources.append("Resource name for ID=");
      paramResources.append(paramInt);
      paramResources.append(" not found in package ");
      paramResources.append(paramString);
      paramResources.append(". Resource IDs may change when the application is upgraded, and the system may not be able to find the correct resource.");
      Log.e("Shortcut", paramResources.toString());
    }
    return null;
  }
  
  public static Intent setIntentExtras(Intent paramIntent, PersistableBundle paramPersistableBundle)
  {
    if (paramPersistableBundle == null) {
      paramIntent.replaceExtras((Bundle)null);
    } else {
      paramIntent.replaceExtras(new Bundle(paramPersistableBundle));
    }
    return paramIntent;
  }
  
  private String toStringInner(boolean paramBoolean1, boolean paramBoolean2, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramString != null) {
      localStringBuilder.append(paramString);
    }
    localStringBuilder.append("ShortcutInfo {");
    localStringBuilder.append("id=");
    Object localObject;
    if (paramBoolean1) {
      localObject = "***";
    } else {
      localObject = mId;
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", flags=0x");
    localStringBuilder.append(Integer.toHexString(mFlags));
    localStringBuilder.append(" [");
    if ((mFlags & 0x1000) != 0) {
      localStringBuilder.append("Sdw");
    }
    if (!isEnabled()) {
      localStringBuilder.append("Dis");
    }
    if (isImmutable()) {
      localStringBuilder.append("Im");
    }
    if (isManifestShortcut()) {
      localStringBuilder.append("Man");
    }
    if (isDynamic()) {
      localStringBuilder.append("Dyn");
    }
    if (isPinned()) {
      localStringBuilder.append("Pin");
    }
    if (hasIconFile()) {
      localStringBuilder.append("Ic-f");
    }
    if (isIconPendingSave()) {
      localStringBuilder.append("Pens");
    }
    if (hasIconResource()) {
      localStringBuilder.append("Ic-r");
    }
    if (hasKeyFieldsOnly()) {
      localStringBuilder.append("Key");
    }
    if (hasStringResourcesResolved()) {
      localStringBuilder.append("Str");
    }
    if (isReturnedByServer()) {
      localStringBuilder.append("Rets");
    }
    localStringBuilder.append("]");
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("packageName=");
    localStringBuilder.append(mPackageName);
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("activity=");
    localStringBuilder.append(mActivity);
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("shortLabel=");
    if (paramBoolean1) {
      localObject = "***";
    } else {
      localObject = mTitle;
    }
    localStringBuilder.append((CharSequence)localObject);
    localStringBuilder.append(", resId=");
    localStringBuilder.append(mTitleResId);
    localStringBuilder.append("[");
    localStringBuilder.append(mTitleResName);
    localStringBuilder.append("]");
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("longLabel=");
    if (paramBoolean1) {
      localObject = "***";
    } else {
      localObject = mText;
    }
    localStringBuilder.append((CharSequence)localObject);
    localStringBuilder.append(", resId=");
    localStringBuilder.append(mTextResId);
    localStringBuilder.append("[");
    localStringBuilder.append(mTextResName);
    localStringBuilder.append("]");
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("disabledMessage=");
    if (paramBoolean1) {
      localObject = "***";
    } else {
      localObject = mDisabledMessage;
    }
    localStringBuilder.append((CharSequence)localObject);
    localStringBuilder.append(", resId=");
    localStringBuilder.append(mDisabledMessageResId);
    localStringBuilder.append("[");
    localStringBuilder.append(mDisabledMessageResName);
    localStringBuilder.append("]");
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("disabledReason=");
    localStringBuilder.append(getDisabledReasonDebugString(mDisabledReason));
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("categories=");
    localStringBuilder.append(mCategories);
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("icon=");
    localStringBuilder.append(mIcon);
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("rank=");
    localStringBuilder.append(mRank);
    localStringBuilder.append(", timestamp=");
    localStringBuilder.append(mLastChangedTimestamp);
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("intents=");
    if (mIntents == null)
    {
      localStringBuilder.append("null");
    }
    else if (paramBoolean1)
    {
      localStringBuilder.append("size:");
      localStringBuilder.append(mIntents.length);
    }
    else
    {
      int i = mIntents.length;
      localStringBuilder.append("[");
      localObject = "";
      for (int j = 0; j < i; j++)
      {
        localStringBuilder.append((String)localObject);
        localObject = ", ";
        localStringBuilder.append(mIntents[j]);
        localStringBuilder.append("/");
        localStringBuilder.append(mIntentPersistableExtrases[j]);
      }
      localStringBuilder.append("]");
    }
    addIndentOrComma(localStringBuilder, paramString);
    localStringBuilder.append("extras=");
    localStringBuilder.append(mExtras);
    if (paramBoolean2)
    {
      addIndentOrComma(localStringBuilder, paramString);
      localStringBuilder.append("iconRes=");
      localStringBuilder.append(mIconResId);
      localStringBuilder.append("[");
      localStringBuilder.append(mIconResName);
      localStringBuilder.append("]");
      localStringBuilder.append(", bitmapPath=");
      localStringBuilder.append(mBitmapPath);
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public static Icon validateIcon(Icon paramIcon)
  {
    int i = paramIcon.getType();
    if (i != 5) {
      switch (i)
      {
      default: 
        throw getInvalidIconException();
      }
    }
    if (!paramIcon.hasTint()) {
      return paramIcon;
    }
    throw new IllegalArgumentException("Icons with tints are not supported");
  }
  
  public void addFlags(int paramInt)
  {
    mFlags |= paramInt;
  }
  
  public void clearFlags(int paramInt)
  {
    mFlags &= paramInt;
  }
  
  public void clearIcon()
  {
    mIcon = null;
  }
  
  public void clearIconPendingSave()
  {
    clearFlags(2048);
  }
  
  public void clearImplicitRankAndRankChangedFlag()
  {
    mImplicitRank = 0;
  }
  
  public ShortcutInfo clone(int paramInt)
  {
    return new ShortcutInfo(this, paramInt);
  }
  
  public void copyNonNullFieldsFrom(ShortcutInfo paramShortcutInfo)
  {
    ensureUpdatableWith(paramShortcutInfo, true);
    if (mActivity != null) {
      mActivity = mActivity;
    }
    if (mIcon != null)
    {
      mIcon = mIcon;
      mIconResId = 0;
      mIconResName = null;
      mBitmapPath = null;
    }
    if (mTitle != null)
    {
      mTitle = mTitle;
      mTitleResId = 0;
      mTitleResName = null;
    }
    else if (mTitleResId != 0)
    {
      mTitle = null;
      mTitleResId = mTitleResId;
      mTitleResName = null;
    }
    if (mText != null)
    {
      mText = mText;
      mTextResId = 0;
      mTextResName = null;
    }
    else if (mTextResId != 0)
    {
      mText = null;
      mTextResId = mTextResId;
      mTextResName = null;
    }
    if (mDisabledMessage != null)
    {
      mDisabledMessage = mDisabledMessage;
      mDisabledMessageResId = 0;
      mDisabledMessageResName = null;
    }
    else if (mDisabledMessageResId != 0)
    {
      mDisabledMessage = null;
      mDisabledMessageResId = mDisabledMessageResId;
      mDisabledMessageResName = null;
    }
    if (mCategories != null) {
      mCategories = cloneCategories(mCategories);
    }
    if (mIntents != null)
    {
      mIntents = cloneIntents(mIntents);
      mIntentPersistableExtrases = clonePersistableBundle(mIntentPersistableExtrases);
    }
    if (mRank != Integer.MAX_VALUE) {
      mRank = mRank;
    }
    if (mExtras != null) {
      mExtras = mExtras;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void enforceMandatoryFields(boolean paramBoolean)
  {
    Preconditions.checkStringNotEmpty(mId, "Shortcut ID must be provided");
    if (!paramBoolean) {
      Preconditions.checkNotNull(mActivity, "Activity must be provided");
    }
    if ((mTitle == null) && (mTitleResId == 0)) {
      throw new IllegalArgumentException("Short label must be provided");
    }
    Preconditions.checkNotNull(mIntents, "Shortcut Intent must be provided");
    if (mIntents.length > 0) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    Preconditions.checkArgument(paramBoolean, "Shortcut Intent must be provided");
  }
  
  public void ensureUpdatableWith(ShortcutInfo paramShortcutInfo, boolean paramBoolean)
  {
    if (paramBoolean) {
      Preconditions.checkState(isVisibleToPublisher(), "[Framework BUG] Invisible shortcuts can't be updated");
    }
    if (mUserId == mUserId) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    Preconditions.checkState(paramBoolean, "Owner User ID must match");
    Preconditions.checkState(mId.equals(mId), "ID must match");
    Preconditions.checkState(mPackageName.equals(mPackageName), "Package name must match");
    if (isVisibleToPublisher()) {
      Preconditions.checkState(isImmutable() ^ true, "Target ShortcutInfo is immutable");
    }
  }
  
  public ComponentName getActivity()
  {
    return mActivity;
  }
  
  public String getBitmapPath()
  {
    return mBitmapPath;
  }
  
  public Set<String> getCategories()
  {
    return mCategories;
  }
  
  public CharSequence getDisabledMessage()
  {
    return mDisabledMessage;
  }
  
  public String getDisabledMessageResName()
  {
    return mDisabledMessageResName;
  }
  
  public int getDisabledMessageResourceId()
  {
    return mDisabledMessageResId;
  }
  
  public int getDisabledReason()
  {
    return mDisabledReason;
  }
  
  public PersistableBundle getExtras()
  {
    return mExtras;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public String getIconResName()
  {
    return mIconResName;
  }
  
  public int getIconResourceId()
  {
    return mIconResId;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public int getImplicitRank()
  {
    return mImplicitRank & 0x7FFFFFFF;
  }
  
  public Intent getIntent()
  {
    if ((mIntents != null) && (mIntents.length != 0))
    {
      int i = mIntents.length - 1;
      return setIntentExtras(new Intent(mIntents[i]), mIntentPersistableExtrases[i]);
    }
    return null;
  }
  
  public PersistableBundle[] getIntentPersistableExtrases()
  {
    return mIntentPersistableExtrases;
  }
  
  public Intent[] getIntents()
  {
    Intent[] arrayOfIntent = new Intent[mIntents.length];
    for (int i = 0; i < arrayOfIntent.length; i++)
    {
      arrayOfIntent[i] = new Intent(mIntents[i]);
      setIntentExtras(arrayOfIntent[i], mIntentPersistableExtrases[i]);
    }
    return arrayOfIntent;
  }
  
  public Intent[] getIntentsNoExtras()
  {
    return mIntents;
  }
  
  public long getLastChangedTimestamp()
  {
    return mLastChangedTimestamp;
  }
  
  public CharSequence getLongLabel()
  {
    return mText;
  }
  
  public int getLongLabelResourceId()
  {
    return mTextResId;
  }
  
  public String getPackage()
  {
    return mPackageName;
  }
  
  public int getRank()
  {
    return mRank;
  }
  
  public CharSequence getShortLabel()
  {
    return mTitle;
  }
  
  public int getShortLabelResourceId()
  {
    return mTitleResId;
  }
  
  @Deprecated
  public CharSequence getText()
  {
    return mText;
  }
  
  @Deprecated
  public int getTextResId()
  {
    return mTextResId;
  }
  
  public String getTextResName()
  {
    return mTextResName;
  }
  
  @Deprecated
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  @Deprecated
  public int getTitleResId()
  {
    return mTitleResId;
  }
  
  public String getTitleResName()
  {
    return mTitleResName;
  }
  
  public UserHandle getUserHandle()
  {
    return UserHandle.of(mUserId);
  }
  
  public int getUserId()
  {
    return mUserId;
  }
  
  public boolean hasAdaptiveBitmap()
  {
    return hasFlags(512);
  }
  
  public boolean hasAnyResources()
  {
    boolean bool;
    if ((!hasIconResource()) && (!hasStringResources())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasFlags(int paramInt)
  {
    boolean bool;
    if ((mFlags & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasIconFile()
  {
    return hasFlags(8);
  }
  
  public boolean hasIconResource()
  {
    return hasFlags(4);
  }
  
  public boolean hasKeyFieldsOnly()
  {
    return hasFlags(16);
  }
  
  public boolean hasRank()
  {
    boolean bool;
    if (mRank != Integer.MAX_VALUE) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasStringResources()
  {
    boolean bool;
    if ((mTitleResId == 0) && (mTextResId == 0) && (mDisabledMessageResId == 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasStringResourcesResolved()
  {
    return hasFlags(128);
  }
  
  public boolean isAlive()
  {
    boolean bool1 = hasFlags(2);
    boolean bool2 = true;
    boolean bool3 = bool2;
    if (!bool1)
    {
      bool3 = bool2;
      if (!hasFlags(1)) {
        if (hasFlags(32)) {
          bool3 = bool2;
        } else {
          bool3 = false;
        }
      }
    }
    return bool3;
  }
  
  public boolean isDeclaredInManifest()
  {
    return hasFlags(32);
  }
  
  public boolean isDynamic()
  {
    return hasFlags(1);
  }
  
  public boolean isDynamicVisible()
  {
    boolean bool;
    if ((isDynamic()) && (isVisibleToPublisher())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEnabled()
  {
    return hasFlags(64) ^ true;
  }
  
  public boolean isFloating()
  {
    boolean bool;
    if ((isPinned()) && (!isDynamic()) && (!isManifestShortcut())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isIconPendingSave()
  {
    return hasFlags(2048);
  }
  
  public boolean isImmutable()
  {
    return hasFlags(256);
  }
  
  @Deprecated
  public boolean isManifestShortcut()
  {
    return isDeclaredInManifest();
  }
  
  public boolean isManifestVisible()
  {
    boolean bool;
    if ((isDeclaredInManifest()) && (isVisibleToPublisher())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOriginallyFromManifest()
  {
    return hasFlags(256);
  }
  
  public boolean isPinned()
  {
    return hasFlags(2);
  }
  
  public boolean isPinnedVisible()
  {
    boolean bool;
    if ((isPinned()) && (isVisibleToPublisher())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRankChanged()
  {
    boolean bool;
    if ((mImplicitRank & 0x80000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isReturnedByServer()
  {
    return hasFlags(1024);
  }
  
  public boolean isVisibleToPublisher()
  {
    return isDisabledForRestoreIssue(mDisabledReason) ^ true;
  }
  
  public void lookupAndFillInResourceIds(Resources paramResources)
  {
    if ((mTitleResName == null) && (mTextResName == null) && (mDisabledMessageResName == null) && (mIconResName == null)) {
      return;
    }
    mTitleResId = lookUpResourceId(paramResources, mTitleResName, "string", mPackageName);
    mTextResId = lookUpResourceId(paramResources, mTextResName, "string", mPackageName);
    mDisabledMessageResId = lookUpResourceId(paramResources, mDisabledMessageResName, "string", mPackageName);
    mIconResId = lookUpResourceId(paramResources, mIconResName, null, mPackageName);
  }
  
  public void lookupAndFillInResourceNames(Resources paramResources)
  {
    if ((mTitleResId == 0) && (mTextResId == 0) && (mDisabledMessageResId == 0) && (mIconResId == 0)) {
      return;
    }
    mTitleResName = lookUpResourceName(paramResources, mTitleResId, false, mPackageName);
    mTextResName = lookUpResourceName(paramResources, mTextResId, false, mPackageName);
    mDisabledMessageResName = lookUpResourceName(paramResources, mDisabledMessageResId, false, mPackageName);
    mIconResName = lookUpResourceName(paramResources, mIconResId, true, mPackageName);
  }
  
  public void replaceFlags(int paramInt)
  {
    mFlags = paramInt;
  }
  
  public void resolveResourceStrings(Resources paramResources)
  {
    mFlags |= 0x80;
    if ((mTitleResId == 0) && (mTextResId == 0) && (mDisabledMessageResId == 0)) {
      return;
    }
    if (mTitleResId != 0) {
      mTitle = getResourceString(paramResources, mTitleResId, mTitle);
    }
    if (mTextResId != 0) {
      mText = getResourceString(paramResources, mTextResId, mText);
    }
    if (mDisabledMessageResId != 0) {
      mDisabledMessage = getResourceString(paramResources, mDisabledMessageResId, mDisabledMessage);
    }
  }
  
  public void setActivity(ComponentName paramComponentName)
  {
    mActivity = paramComponentName;
  }
  
  public void setBitmapPath(String paramString)
  {
    mBitmapPath = paramString;
  }
  
  public void setCategories(Set<String> paramSet)
  {
    mCategories = cloneCategories(paramSet);
  }
  
  public void setDisabledMessage(String paramString)
  {
    mDisabledMessage = paramString;
    mDisabledMessageResId = 0;
    mDisabledMessageResName = null;
  }
  
  public void setDisabledMessageResId(int paramInt)
  {
    if (mDisabledMessageResId != paramInt) {
      mDisabledMessageResName = null;
    }
    mDisabledMessageResId = paramInt;
    mDisabledMessage = null;
  }
  
  public void setDisabledMessageResName(String paramString)
  {
    mDisabledMessageResName = paramString;
  }
  
  public void setDisabledReason(int paramInt)
  {
    mDisabledReason = paramInt;
  }
  
  public void setIconPendingSave()
  {
    addFlags(2048);
  }
  
  public void setIconResName(String paramString)
  {
    mIconResName = paramString;
  }
  
  public void setIconResourceId(int paramInt)
  {
    if (mIconResId != paramInt) {
      mIconResName = null;
    }
    mIconResId = paramInt;
  }
  
  public void setImplicitRank(int paramInt)
  {
    mImplicitRank = (mImplicitRank & 0x80000000 | 0x7FFFFFFF & paramInt);
  }
  
  public void setIntents(Intent[] paramArrayOfIntent)
    throws IllegalArgumentException
  {
    Preconditions.checkNotNull(paramArrayOfIntent);
    boolean bool;
    if (paramArrayOfIntent.length > 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool);
    mIntents = cloneIntents(paramArrayOfIntent);
    fixUpIntentExtras();
  }
  
  public void setRank(int paramInt)
  {
    mRank = paramInt;
  }
  
  public void setRankChanged()
  {
    mImplicitRank |= 0x80000000;
  }
  
  public void setReturnedByServer()
  {
    addFlags(1024);
  }
  
  public void setTextResName(String paramString)
  {
    mTextResName = paramString;
  }
  
  public void setTimestamp(long paramLong)
  {
    mLastChangedTimestamp = paramLong;
  }
  
  public void setTitleResName(String paramString)
  {
    mTitleResName = paramString;
  }
  
  public String toDumpString(String paramString)
  {
    return toStringInner(false, true, paramString);
  }
  
  public String toInsecureString()
  {
    return toStringInner(false, true, null);
  }
  
  public String toString()
  {
    return toStringInner(true, false, null);
  }
  
  public void updateTimestamp()
  {
    mLastChangedTimestamp = System.currentTimeMillis();
  }
  
  public boolean usesQuota()
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (!hasFlags(1)) {
      if (hasFlags(32)) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mUserId);
    paramParcel.writeString(mId);
    paramParcel.writeString(mPackageName);
    paramParcel.writeParcelable(mActivity, paramInt);
    paramParcel.writeInt(mFlags);
    paramParcel.writeInt(mIconResId);
    paramParcel.writeLong(mLastChangedTimestamp);
    paramParcel.writeInt(mDisabledReason);
    boolean bool = hasKeyFieldsOnly();
    int i = 0;
    if (bool)
    {
      paramParcel.writeInt(0);
      return;
    }
    paramParcel.writeInt(1);
    paramParcel.writeParcelable(mIcon, paramInt);
    paramParcel.writeCharSequence(mTitle);
    paramParcel.writeInt(mTitleResId);
    paramParcel.writeCharSequence(mText);
    paramParcel.writeInt(mTextResId);
    paramParcel.writeCharSequence(mDisabledMessage);
    paramParcel.writeInt(mDisabledMessageResId);
    paramParcel.writeParcelableArray(mIntents, paramInt);
    paramParcel.writeParcelableArray(mIntentPersistableExtrases, paramInt);
    paramParcel.writeInt(mRank);
    paramParcel.writeParcelable(mExtras, paramInt);
    paramParcel.writeString(mBitmapPath);
    paramParcel.writeString(mIconResName);
    paramParcel.writeString(mTitleResName);
    paramParcel.writeString(mTextResName);
    paramParcel.writeString(mDisabledMessageResName);
    if (mCategories != null)
    {
      int j = mCategories.size();
      paramParcel.writeInt(j);
      for (paramInt = i; paramInt < j; paramInt++) {
        paramParcel.writeString((String)mCategories.valueAt(paramInt));
      }
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
  
  public static class Builder
  {
    private ComponentName mActivity;
    private Set<String> mCategories;
    private final Context mContext;
    private CharSequence mDisabledMessage;
    private int mDisabledMessageResId;
    private PersistableBundle mExtras;
    private Icon mIcon;
    private String mId;
    private Intent[] mIntents;
    private int mRank = Integer.MAX_VALUE;
    private CharSequence mText;
    private int mTextResId;
    private CharSequence mTitle;
    private int mTitleResId;
    
    @Deprecated
    public Builder(Context paramContext)
    {
      mContext = paramContext;
    }
    
    public Builder(Context paramContext, String paramString)
    {
      mContext = paramContext;
      mId = ((String)Preconditions.checkStringNotEmpty(paramString, "id cannot be empty"));
    }
    
    public ShortcutInfo build()
    {
      return new ShortcutInfo(this, null);
    }
    
    public Builder setActivity(ComponentName paramComponentName)
    {
      mActivity = ((ComponentName)Preconditions.checkNotNull(paramComponentName, "activity cannot be null"));
      return this;
    }
    
    public Builder setCategories(Set<String> paramSet)
    {
      mCategories = paramSet;
      return this;
    }
    
    public Builder setDisabledMessage(CharSequence paramCharSequence)
    {
      boolean bool;
      if (mDisabledMessageResId == 0) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "disabledMessageResId already set");
      mDisabledMessage = Preconditions.checkStringNotEmpty(paramCharSequence, "disabledMessage cannot be empty");
      return this;
    }
    
    @Deprecated
    public Builder setDisabledMessageResId(int paramInt)
    {
      boolean bool;
      if (mDisabledMessage == null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "disabledMessage already set");
      mDisabledMessageResId = paramInt;
      return this;
    }
    
    public Builder setExtras(PersistableBundle paramPersistableBundle)
    {
      mExtras = paramPersistableBundle;
      return this;
    }
    
    public Builder setIcon(Icon paramIcon)
    {
      mIcon = ShortcutInfo.validateIcon(paramIcon);
      return this;
    }
    
    @Deprecated
    public Builder setId(String paramString)
    {
      mId = ((String)Preconditions.checkStringNotEmpty(paramString, "id cannot be empty"));
      return this;
    }
    
    public Builder setIntent(Intent paramIntent)
    {
      return setIntents(new Intent[] { paramIntent });
    }
    
    public Builder setIntents(Intent[] paramArrayOfIntent)
    {
      Preconditions.checkNotNull(paramArrayOfIntent, "intents cannot be null");
      Preconditions.checkNotNull(Integer.valueOf(paramArrayOfIntent.length), "intents cannot be empty");
      int i = paramArrayOfIntent.length;
      for (int j = 0; j < i; j++)
      {
        Intent localIntent = paramArrayOfIntent[j];
        Preconditions.checkNotNull(localIntent, "intents cannot contain null");
        Preconditions.checkNotNull(localIntent.getAction(), "intent's action must be set");
      }
      mIntents = ShortcutInfo.cloneIntents(paramArrayOfIntent);
      return this;
    }
    
    public Builder setLongLabel(CharSequence paramCharSequence)
    {
      boolean bool;
      if (mTextResId == 0) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "longLabelResId already set");
      mText = Preconditions.checkStringNotEmpty(paramCharSequence, "longLabel cannot be empty");
      return this;
    }
    
    @Deprecated
    public Builder setLongLabelResId(int paramInt)
    {
      boolean bool;
      if (mText == null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "longLabel already set");
      mTextResId = paramInt;
      return this;
    }
    
    public Builder setRank(int paramInt)
    {
      boolean bool;
      if (paramInt >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool, "Rank cannot be negative or bigger than MAX_RANK");
      mRank = paramInt;
      return this;
    }
    
    public Builder setShortLabel(CharSequence paramCharSequence)
    {
      boolean bool;
      if (mTitleResId == 0) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "shortLabelResId already set");
      mTitle = Preconditions.checkStringNotEmpty(paramCharSequence, "shortLabel cannot be empty");
      return this;
    }
    
    @Deprecated
    public Builder setShortLabelResId(int paramInt)
    {
      boolean bool;
      if (mTitle == null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "shortLabel already set");
      mTitleResId = paramInt;
      return this;
    }
    
    @Deprecated
    public Builder setText(CharSequence paramCharSequence)
    {
      return setLongLabel(paramCharSequence);
    }
    
    @Deprecated
    public Builder setTextResId(int paramInt)
    {
      return setLongLabelResId(paramInt);
    }
    
    @Deprecated
    public Builder setTitle(CharSequence paramCharSequence)
    {
      return setShortLabel(paramCharSequence);
    }
    
    @Deprecated
    public Builder setTitleResId(int paramInt)
    {
      return setShortLabelResId(paramInt);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CloneFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DisabledReason {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ShortcutFlags {}
}
