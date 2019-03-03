package android.media.tv;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public final class TvInputInfo
  implements Parcelable
{
  public static final Parcelable.Creator<TvInputInfo> CREATOR = new Parcelable.Creator()
  {
    public TvInputInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TvInputInfo(paramAnonymousParcel, null);
    }
    
    public TvInputInfo[] newArray(int paramAnonymousInt)
    {
      return new TvInputInfo[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  public static final String EXTRA_INPUT_ID = "android.media.tv.extra.INPUT_ID";
  private static final String TAG = "TvInputInfo";
  public static final int TYPE_COMPONENT = 1004;
  public static final int TYPE_COMPOSITE = 1001;
  public static final int TYPE_DISPLAY_PORT = 1008;
  public static final int TYPE_DVI = 1006;
  public static final int TYPE_HDMI = 1007;
  public static final int TYPE_OTHER = 1000;
  public static final int TYPE_SCART = 1003;
  public static final int TYPE_SVIDEO = 1002;
  public static final int TYPE_TUNER = 0;
  public static final int TYPE_VGA = 1005;
  private final boolean mCanRecord;
  private final Bundle mExtras;
  private final HdmiDeviceInfo mHdmiDeviceInfo;
  private final Icon mIcon;
  private final Icon mIconDisconnected;
  private final Icon mIconStandby;
  private Uri mIconUri;
  private final String mId;
  private final boolean mIsConnectedToHdmiSwitch;
  private final boolean mIsHardwareInput;
  private final CharSequence mLabel;
  private final int mLabelResId;
  private final String mParentId;
  private final ResolveInfo mService;
  private final String mSetupActivity;
  private final int mTunerCount;
  private final int mType;
  
  private TvInputInfo(ResolveInfo paramResolveInfo, String paramString1, int paramInt1, boolean paramBoolean1, CharSequence paramCharSequence, int paramInt2, Icon paramIcon1, Icon paramIcon2, Icon paramIcon3, String paramString2, boolean paramBoolean2, int paramInt3, HdmiDeviceInfo paramHdmiDeviceInfo, boolean paramBoolean3, String paramString3, Bundle paramBundle)
  {
    mService = paramResolveInfo;
    mId = paramString1;
    mType = paramInt1;
    mIsHardwareInput = paramBoolean1;
    mLabel = paramCharSequence;
    mLabelResId = paramInt2;
    mIcon = paramIcon1;
    mIconStandby = paramIcon2;
    mIconDisconnected = paramIcon3;
    mSetupActivity = paramString2;
    mCanRecord = paramBoolean2;
    mTunerCount = paramInt3;
    mHdmiDeviceInfo = paramHdmiDeviceInfo;
    mIsConnectedToHdmiSwitch = paramBoolean3;
    mParentId = paramString3;
    mExtras = paramBundle;
  }
  
  private TvInputInfo(Parcel paramParcel)
  {
    mService = ((ResolveInfo)ResolveInfo.CREATOR.createFromParcel(paramParcel));
    mId = paramParcel.readString();
    mType = paramParcel.readInt();
    int i = paramParcel.readByte();
    boolean bool1 = false;
    if (i == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsHardwareInput = bool2;
    mLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mIconUri = ((Uri)paramParcel.readParcelable(null));
    mLabelResId = paramParcel.readInt();
    mIcon = ((Icon)paramParcel.readParcelable(null));
    mIconStandby = ((Icon)paramParcel.readParcelable(null));
    mIconDisconnected = ((Icon)paramParcel.readParcelable(null));
    mSetupActivity = paramParcel.readString();
    if (paramParcel.readByte() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mCanRecord = bool2;
    mTunerCount = paramParcel.readInt();
    mHdmiDeviceInfo = ((HdmiDeviceInfo)paramParcel.readParcelable(null));
    boolean bool2 = bool1;
    if (paramParcel.readByte() == 1) {
      bool2 = true;
    }
    mIsConnectedToHdmiSwitch = bool2;
    mParentId = paramParcel.readString();
    mExtras = paramParcel.readBundle();
  }
  
  @SystemApi
  @Deprecated
  public static TvInputInfo createTvInputInfo(Context paramContext, ResolveInfo paramResolveInfo, HdmiDeviceInfo paramHdmiDeviceInfo, String paramString, int paramInt, Icon paramIcon)
    throws XmlPullParserException, IOException
  {
    return new Builder(paramContext, paramResolveInfo).setHdmiDeviceInfo(paramHdmiDeviceInfo).setParentId(paramString).setLabel(paramInt).setIcon(paramIcon).build();
  }
  
  @SystemApi
  @Deprecated
  public static TvInputInfo createTvInputInfo(Context paramContext, ResolveInfo paramResolveInfo, HdmiDeviceInfo paramHdmiDeviceInfo, String paramString1, String paramString2, Uri paramUri)
    throws XmlPullParserException, IOException
  {
    paramContext = new Builder(paramContext, paramResolveInfo).setHdmiDeviceInfo(paramHdmiDeviceInfo).setParentId(paramString1).setLabel(paramString2).build();
    mIconUri = paramUri;
    return paramContext;
  }
  
  @SystemApi
  @Deprecated
  public static TvInputInfo createTvInputInfo(Context paramContext, ResolveInfo paramResolveInfo, TvInputHardwareInfo paramTvInputHardwareInfo, int paramInt, Icon paramIcon)
    throws XmlPullParserException, IOException
  {
    return new Builder(paramContext, paramResolveInfo).setTvInputHardwareInfo(paramTvInputHardwareInfo).setLabel(paramInt).setIcon(paramIcon).build();
  }
  
  @SystemApi
  @Deprecated
  public static TvInputInfo createTvInputInfo(Context paramContext, ResolveInfo paramResolveInfo, TvInputHardwareInfo paramTvInputHardwareInfo, String paramString, Uri paramUri)
    throws XmlPullParserException, IOException
  {
    paramContext = new Builder(paramContext, paramResolveInfo).setTvInputHardwareInfo(paramTvInputHardwareInfo).setLabel(paramString).build();
    mIconUri = paramUri;
    return paramContext;
  }
  
  private Drawable loadServiceIcon(Context paramContext)
  {
    if ((mService.serviceInfo.icon == 0) && (mService.serviceInfo.applicationInfo.icon == 0)) {
      return null;
    }
    return mService.serviceInfo.loadIcon(paramContext.getPackageManager());
  }
  
  public boolean canRecord()
  {
    return mCanRecord;
  }
  
  @Deprecated
  public Intent createSettingsIntent()
  {
    return null;
  }
  
  public Intent createSetupIntent()
  {
    if (!TextUtils.isEmpty(mSetupActivity))
    {
      Intent localIntent = new Intent("android.intent.action.MAIN");
      localIntent.setClassName(mService.serviceInfo.packageName, mSetupActivity);
      localIntent.putExtra("android.media.tv.extra.INPUT_ID", getId());
      return localIntent;
    }
    return null;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof TvInputInfo)) {
      return false;
    }
    paramObject = (TvInputInfo)paramObject;
    if ((!Objects.equals(mService, mService)) || (!TextUtils.equals(mId, mId)) || (mType != mType) || (mIsHardwareInput != mIsHardwareInput) || (!TextUtils.equals(mLabel, mLabel)) || (!Objects.equals(mIconUri, mIconUri)) || (mLabelResId != mLabelResId) || (!Objects.equals(mIcon, mIcon)) || (!Objects.equals(mIconStandby, mIconStandby)) || (!Objects.equals(mIconDisconnected, mIconDisconnected)) || (!TextUtils.equals(mSetupActivity, mSetupActivity)) || (mCanRecord != mCanRecord) || (mTunerCount != mTunerCount) || (!Objects.equals(mHdmiDeviceInfo, mHdmiDeviceInfo)) || (mIsConnectedToHdmiSwitch != mIsConnectedToHdmiSwitch) || (!TextUtils.equals(mParentId, mParentId)) || (!Objects.equals(mExtras, mExtras))) {
      bool = false;
    }
    return bool;
  }
  
  public ComponentName getComponent()
  {
    return new ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  @SystemApi
  public HdmiDeviceInfo getHdmiDeviceInfo()
  {
    if (mType == 1007) {
      return mHdmiDeviceInfo;
    }
    return null;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public String getParentId()
  {
    return mParentId;
  }
  
  public ServiceInfo getServiceInfo()
  {
    return mService.serviceInfo;
  }
  
  public int getTunerCount()
  {
    return mTunerCount;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    return mId.hashCode();
  }
  
  @SystemApi
  public boolean isConnectedToHdmiSwitch()
  {
    return mIsConnectedToHdmiSwitch;
  }
  
  @SystemApi
  public boolean isHardwareInput()
  {
    return mIsHardwareInput;
  }
  
  public boolean isHidden(Context paramContext)
  {
    return TvInputSettings.isHidden(paramContext, mId, UserHandle.myUserId());
  }
  
  public boolean isPassthroughInput()
  {
    boolean bool;
    if (mType != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public CharSequence loadCustomLabel(Context paramContext)
  {
    return TvInputSettings.getCustomLabel(paramContext, mId, UserHandle.myUserId());
  }
  
  /* Error */
  public Drawable loadIcon(Context paramContext)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 110	android/media/tv/TvInputInfo:mIcon	Landroid/graphics/drawable/Icon;
    //   4: ifnull +12 -> 16
    //   7: aload_0
    //   8: getfield 110	android/media/tv/TvInputInfo:mIcon	Landroid/graphics/drawable/Icon;
    //   11: aload_1
    //   12: invokevirtual 338	android/graphics/drawable/Icon:loadDrawable	(Landroid/content/Context;)Landroid/graphics/drawable/Drawable;
    //   15: areturn
    //   16: aload_0
    //   17: getfield 170	android/media/tv/TvInputInfo:mIconUri	Landroid/net/Uri;
    //   20: ifnull +117 -> 137
    //   23: aload_1
    //   24: invokevirtual 342	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   27: aload_0
    //   28: getfield 170	android/media/tv/TvInputInfo:mIconUri	Landroid/net/Uri;
    //   31: invokevirtual 348	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   34: astore_2
    //   35: aconst_null
    //   36: astore_3
    //   37: aload_2
    //   38: aconst_null
    //   39: invokestatic 354	android/graphics/drawable/Drawable:createFromStream	(Ljava/io/InputStream;Ljava/lang/String;)Landroid/graphics/drawable/Drawable;
    //   42: astore 4
    //   44: aload 4
    //   46: ifnull +15 -> 61
    //   49: aload_2
    //   50: ifnull +8 -> 58
    //   53: aconst_null
    //   54: aload_2
    //   55: invokestatic 356	android/media/tv/TvInputInfo:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   58: aload 4
    //   60: areturn
    //   61: aload_2
    //   62: ifnull +8 -> 70
    //   65: aconst_null
    //   66: aload_2
    //   67: invokestatic 356	android/media/tv/TvInputInfo:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   70: goto +67 -> 137
    //   73: astore 4
    //   75: goto +11 -> 86
    //   78: astore 4
    //   80: aload 4
    //   82: astore_3
    //   83: aload 4
    //   85: athrow
    //   86: aload_2
    //   87: ifnull +8 -> 95
    //   90: aload_3
    //   91: aload_2
    //   92: invokestatic 356	android/media/tv/TvInputInfo:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   95: aload 4
    //   97: athrow
    //   98: astore 4
    //   100: new 358	java/lang/StringBuilder
    //   103: dup
    //   104: invokespecial 359	java/lang/StringBuilder:<init>	()V
    //   107: astore_3
    //   108: aload_3
    //   109: ldc_w 361
    //   112: invokevirtual 365	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload_3
    //   117: aload_0
    //   118: getfield 170	android/media/tv/TvInputInfo:mIconUri	Landroid/net/Uri;
    //   121: invokevirtual 368	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: ldc 30
    //   127: aload_3
    //   128: invokevirtual 371	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   131: aload 4
    //   133: invokestatic 377	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   136: pop
    //   137: aload_0
    //   138: aload_1
    //   139: invokespecial 379	android/media/tv/TvInputInfo:loadServiceIcon	(Landroid/content/Context;)Landroid/graphics/drawable/Drawable;
    //   142: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	143	0	this	TvInputInfo
    //   0	143	1	paramContext	Context
    //   34	58	2	localInputStream	java.io.InputStream
    //   36	92	3	localObject1	Object
    //   42	17	4	localDrawable	Drawable
    //   73	1	4	localObject2	Object
    //   78	18	4	localThrowable	Throwable
    //   98	34	4	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   37	44	73	finally
    //   83	86	73	finally
    //   37	44	78	java/lang/Throwable
    //   23	35	98	java/io/IOException
    //   53	58	98	java/io/IOException
    //   65	70	98	java/io/IOException
    //   90	95	98	java/io/IOException
    //   95	98	98	java/io/IOException
  }
  
  @SystemApi
  public Drawable loadIcon(Context paramContext, int paramInt)
  {
    if (paramInt == 0) {
      return loadIcon(paramContext);
    }
    if (paramInt == 1)
    {
      if (mIconStandby != null) {
        return mIconStandby.loadDrawable(paramContext);
      }
    }
    else
    {
      if (paramInt != 2) {
        break label54;
      }
      if (mIconDisconnected != null) {
        return mIconDisconnected.loadDrawable(paramContext);
      }
    }
    return null;
    label54:
    paramContext = new StringBuilder();
    paramContext.append("Unknown state: ");
    paramContext.append(paramInt);
    throw new IllegalArgumentException(paramContext.toString());
  }
  
  public CharSequence loadLabel(Context paramContext)
  {
    if (mLabelResId != 0) {
      return paramContext.getPackageManager().getText(mService.serviceInfo.packageName, mLabelResId, null);
    }
    if (!TextUtils.isEmpty(mLabel)) {
      return mLabel;
    }
    return mService.loadLabel(paramContext.getPackageManager());
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TvInputInfo{id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", pkg=");
    localStringBuilder.append(mService.serviceInfo.packageName);
    localStringBuilder.append(", service=");
    localStringBuilder.append(mService.serviceInfo.name);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mService.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mId);
    paramParcel.writeInt(mType);
    paramParcel.writeByte(mIsHardwareInput);
    TextUtils.writeToParcel(mLabel, paramParcel, paramInt);
    paramParcel.writeParcelable(mIconUri, paramInt);
    paramParcel.writeInt(mLabelResId);
    paramParcel.writeParcelable(mIcon, paramInt);
    paramParcel.writeParcelable(mIconStandby, paramInt);
    paramParcel.writeParcelable(mIconDisconnected, paramInt);
    paramParcel.writeString(mSetupActivity);
    paramParcel.writeByte(mCanRecord);
    paramParcel.writeInt(mTunerCount);
    paramParcel.writeParcelable(mHdmiDeviceInfo, paramInt);
    paramParcel.writeByte(mIsConnectedToHdmiSwitch);
    paramParcel.writeString(mParentId);
    paramParcel.writeBundle(mExtras);
  }
  
  public static final class Builder
  {
    private static final String DELIMITER_INFO_IN_ID = "/";
    private static final int LENGTH_HDMI_DEVICE_ID = 2;
    private static final int LENGTH_HDMI_PHYSICAL_ADDRESS = 4;
    private static final String PREFIX_HARDWARE_DEVICE = "HW";
    private static final String PREFIX_HDMI_DEVICE = "HDMI";
    private static final String XML_START_TAG_NAME = "tv-input";
    private static final SparseIntArray sHardwareTypeToTvInputType = new SparseIntArray();
    private Boolean mCanRecord;
    private final Context mContext;
    private Bundle mExtras;
    private HdmiDeviceInfo mHdmiDeviceInfo;
    private Icon mIcon;
    private Icon mIconDisconnected;
    private Icon mIconStandby;
    private CharSequence mLabel;
    private int mLabelResId;
    private String mParentId;
    private final ResolveInfo mResolveInfo;
    private String mSetupActivity;
    private Integer mTunerCount;
    private TvInputHardwareInfo mTvInputHardwareInfo;
    
    static
    {
      sHardwareTypeToTvInputType.put(1, 1000);
      sHardwareTypeToTvInputType.put(2, 0);
      sHardwareTypeToTvInputType.put(3, 1001);
      sHardwareTypeToTvInputType.put(4, 1002);
      sHardwareTypeToTvInputType.put(5, 1003);
      sHardwareTypeToTvInputType.put(6, 1004);
      sHardwareTypeToTvInputType.put(7, 1005);
      sHardwareTypeToTvInputType.put(8, 1006);
      sHardwareTypeToTvInputType.put(9, 1007);
      sHardwareTypeToTvInputType.put(10, 1008);
    }
    
    public Builder(Context paramContext, ComponentName paramComponentName)
    {
      if (paramContext != null)
      {
        paramComponentName = new Intent("android.media.tv.TvInputService").setComponent(paramComponentName);
        mResolveInfo = paramContext.getPackageManager().resolveService(paramComponentName, 132);
        if (mResolveInfo != null)
        {
          mContext = paramContext;
          return;
        }
        throw new IllegalArgumentException("Invalid component. Can't find the service.");
      }
      throw new IllegalArgumentException("context cannot be null.");
    }
    
    public Builder(Context paramContext, ResolveInfo paramResolveInfo)
    {
      if (paramContext != null)
      {
        if (paramResolveInfo != null)
        {
          mContext = paramContext;
          mResolveInfo = paramResolveInfo;
          return;
        }
        throw new IllegalArgumentException("resolveInfo cannot be null");
      }
      throw new IllegalArgumentException("context cannot be null");
    }
    
    private static String generateInputId(ComponentName paramComponentName)
    {
      return paramComponentName.flattenToShortString();
    }
    
    private static String generateInputId(ComponentName paramComponentName, HdmiDeviceInfo paramHdmiDeviceInfo)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramComponentName.flattenToShortString());
      localStringBuilder.append(String.format(Locale.ENGLISH, "/HDMI%04X%02X", new Object[] { Integer.valueOf(paramHdmiDeviceInfo.getPhysicalAddress()), Integer.valueOf(paramHdmiDeviceInfo.getId()) }));
      return localStringBuilder.toString();
    }
    
    private static String generateInputId(ComponentName paramComponentName, TvInputHardwareInfo paramTvInputHardwareInfo)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramComponentName.flattenToShortString());
      localStringBuilder.append("/");
      localStringBuilder.append("HW");
      localStringBuilder.append(paramTvInputHardwareInfo.getDeviceId());
      return localStringBuilder.toString();
    }
    
    /* Error */
    private void parseServiceMetadata(int paramInt)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 91	android/media/tv/TvInputInfo$Builder:mResolveInfo	Landroid/content/pm/ResolveInfo;
      //   4: getfield 178	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
      //   7: astore_2
      //   8: aload_0
      //   9: getfield 93	android/media/tv/TvInputInfo$Builder:mContext	Landroid/content/Context;
      //   12: invokevirtual 83	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
      //   15: astore_3
      //   16: aload_2
      //   17: aload_3
      //   18: ldc -76
      //   20: invokevirtual 186	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
      //   23: astore 4
      //   25: aconst_null
      //   26: astore 5
      //   28: aload 4
      //   30: ifnull +254 -> 284
      //   33: aload 5
      //   35: astore 6
      //   37: aload_3
      //   38: aload_2
      //   39: getfield 190	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
      //   42: invokevirtual 194	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
      //   45: astore_3
      //   46: aload 5
      //   48: astore 6
      //   50: aload 4
      //   52: invokestatic 200	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
      //   55: astore 7
      //   57: aload 5
      //   59: astore 6
      //   61: aload 4
      //   63: invokeinterface 205 1 0
      //   68: istore 8
      //   70: iload 8
      //   72: iconst_1
      //   73: if_icmpeq +12 -> 85
      //   76: iload 8
      //   78: iconst_2
      //   79: if_icmpeq +6 -> 85
      //   82: goto -25 -> 57
      //   85: aload 5
      //   87: astore 6
      //   89: ldc 25
      //   91: aload 4
      //   93: invokeinterface 208 1 0
      //   98: invokevirtual 212	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   101: ifeq +111 -> 212
      //   104: aload 5
      //   106: astore 6
      //   108: aload_3
      //   109: aload 7
      //   111: getstatic 218	com/android/internal/R$styleable:TvInputService	[I
      //   114: invokevirtual 224	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
      //   117: astore_3
      //   118: aload 5
      //   120: astore 6
      //   122: aload_0
      //   123: aload_3
      //   124: iconst_1
      //   125: invokevirtual 230	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
      //   128: putfield 232	android/media/tv/TvInputInfo$Builder:mSetupActivity	Ljava/lang/String;
      //   131: aload 5
      //   133: astore 6
      //   135: aload_0
      //   136: getfield 234	android/media/tv/TvInputInfo$Builder:mCanRecord	Ljava/lang/Boolean;
      //   139: ifnonnull +20 -> 159
      //   142: aload 5
      //   144: astore 6
      //   146: aload_0
      //   147: aload_3
      //   148: iconst_2
      //   149: iconst_0
      //   150: invokevirtual 238	android/content/res/TypedArray:getBoolean	(IZ)Z
      //   153: invokestatic 243	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
      //   156: putfield 234	android/media/tv/TvInputInfo$Builder:mCanRecord	Ljava/lang/Boolean;
      //   159: aload 5
      //   161: astore 6
      //   163: aload_0
      //   164: getfield 245	android/media/tv/TvInputInfo$Builder:mTunerCount	Ljava/lang/Integer;
      //   167: ifnonnull +24 -> 191
      //   170: iload_1
      //   171: ifne +20 -> 191
      //   174: aload 5
      //   176: astore 6
      //   178: aload_0
      //   179: aload_3
      //   180: iconst_3
      //   181: iconst_1
      //   182: invokevirtual 249	android/content/res/TypedArray:getInt	(II)I
      //   185: invokestatic 141	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   188: putfield 245	android/media/tv/TvInputInfo$Builder:mTunerCount	Ljava/lang/Integer;
      //   191: aload 5
      //   193: astore 6
      //   195: aload_3
      //   196: invokevirtual 252	android/content/res/TypedArray:recycle	()V
      //   199: aload 4
      //   201: ifnull +10 -> 211
      //   204: aload 4
      //   206: invokeinterface 255 1 0
      //   211: return
      //   212: aload 5
      //   214: astore 6
      //   216: new 257	java/lang/IllegalStateException
      //   219: astore_3
      //   220: aload 5
      //   222: astore 6
      //   224: new 116	java/lang/StringBuilder
      //   227: astore 7
      //   229: aload 5
      //   231: astore 6
      //   233: aload 7
      //   235: invokespecial 117	java/lang/StringBuilder:<init>	()V
      //   238: aload 5
      //   240: astore 6
      //   242: aload 7
      //   244: ldc_w 259
      //   247: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   250: pop
      //   251: aload 5
      //   253: astore 6
      //   255: aload 7
      //   257: aload_2
      //   258: getfield 262	android/content/pm/ServiceInfo:name	Ljava/lang/String;
      //   261: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   264: pop
      //   265: aload 5
      //   267: astore 6
      //   269: aload_3
      //   270: aload 7
      //   272: invokevirtual 153	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   275: invokespecial 263	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
      //   278: aload 5
      //   280: astore 6
      //   282: aload_3
      //   283: athrow
      //   284: aload 5
      //   286: astore 6
      //   288: new 257	java/lang/IllegalStateException
      //   291: astore_3
      //   292: aload 5
      //   294: astore 6
      //   296: new 116	java/lang/StringBuilder
      //   299: astore 7
      //   301: aload 5
      //   303: astore 6
      //   305: aload 7
      //   307: invokespecial 117	java/lang/StringBuilder:<init>	()V
      //   310: aload 5
      //   312: astore 6
      //   314: aload 7
      //   316: ldc_w 265
      //   319: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   322: pop
      //   323: aload 5
      //   325: astore 6
      //   327: aload 7
      //   329: aload_2
      //   330: getfield 262	android/content/pm/ServiceInfo:name	Ljava/lang/String;
      //   333: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   336: pop
      //   337: aload 5
      //   339: astore 6
      //   341: aload_3
      //   342: aload 7
      //   344: invokevirtual 153	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   347: invokespecial 263	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
      //   350: aload 5
      //   352: astore 6
      //   354: aload_3
      //   355: athrow
      //   356: astore 5
      //   358: goto +12 -> 370
      //   361: astore 5
      //   363: aload 5
      //   365: astore 6
      //   367: aload 5
      //   369: athrow
      //   370: aload 4
      //   372: ifnull +37 -> 409
      //   375: aload 6
      //   377: ifnull +25 -> 402
      //   380: aload 4
      //   382: invokeinterface 255 1 0
      //   387: goto +22 -> 409
      //   390: astore 4
      //   392: aload 6
      //   394: aload 4
      //   396: invokevirtual 269	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
      //   399: goto +10 -> 409
      //   402: aload 4
      //   404: invokeinterface 255 1 0
      //   409: aload 5
      //   411: athrow
      //   412: astore 6
      //   414: new 116	java/lang/StringBuilder
      //   417: dup
      //   418: invokespecial 117	java/lang/StringBuilder:<init>	()V
      //   421: astore 5
      //   423: aload 5
      //   425: ldc_w 271
      //   428: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   431: pop
      //   432: aload 5
      //   434: aload_2
      //   435: getfield 274	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
      //   438: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   441: pop
      //   442: new 257	java/lang/IllegalStateException
      //   445: dup
      //   446: aload 5
      //   448: invokevirtual 153	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   451: aload 6
      //   453: invokespecial 277	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   456: athrow
      //   457: astore 6
      //   459: new 116	java/lang/StringBuilder
      //   462: dup
      //   463: invokespecial 117	java/lang/StringBuilder:<init>	()V
      //   466: astore 5
      //   468: aload 5
      //   470: ldc_w 279
      //   473: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   476: pop
      //   477: aload 5
      //   479: aload_2
      //   480: getfield 274	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
      //   483: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   486: pop
      //   487: new 257	java/lang/IllegalStateException
      //   490: dup
      //   491: aload 5
      //   493: invokevirtual 153	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   496: aload 6
      //   498: invokespecial 277	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   501: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	502	0	this	Builder
      //   0	502	1	paramInt	int
      //   7	473	2	localServiceInfo	ServiceInfo
      //   15	340	3	localObject1	Object
      //   23	358	4	localXmlResourceParser	android.content.res.XmlResourceParser
      //   390	13	4	localThrowable1	Throwable
      //   26	325	5	localObject2	Object
      //   356	1	5	localObject3	Object
      //   361	49	5	localThrowable2	Throwable
      //   421	71	5	localStringBuilder	StringBuilder
      //   35	358	6	localObject4	Object
      //   412	40	6	localNameNotFoundException	android.content.pm.PackageManager.NameNotFoundException
      //   457	40	6	localIOException	IOException
      //   55	288	7	localObject5	Object
      //   68	12	8	i	int
      // Exception table:
      //   from	to	target	type
      //   37	46	356	finally
      //   50	57	356	finally
      //   61	70	356	finally
      //   89	104	356	finally
      //   108	118	356	finally
      //   122	131	356	finally
      //   135	142	356	finally
      //   146	159	356	finally
      //   163	170	356	finally
      //   178	191	356	finally
      //   195	199	356	finally
      //   216	220	356	finally
      //   224	229	356	finally
      //   233	238	356	finally
      //   242	251	356	finally
      //   255	265	356	finally
      //   269	278	356	finally
      //   282	284	356	finally
      //   288	292	356	finally
      //   296	301	356	finally
      //   305	310	356	finally
      //   314	323	356	finally
      //   327	337	356	finally
      //   341	350	356	finally
      //   354	356	356	finally
      //   367	370	356	finally
      //   37	46	361	java/lang/Throwable
      //   50	57	361	java/lang/Throwable
      //   61	70	361	java/lang/Throwable
      //   89	104	361	java/lang/Throwable
      //   108	118	361	java/lang/Throwable
      //   122	131	361	java/lang/Throwable
      //   135	142	361	java/lang/Throwable
      //   146	159	361	java/lang/Throwable
      //   163	170	361	java/lang/Throwable
      //   178	191	361	java/lang/Throwable
      //   195	199	361	java/lang/Throwable
      //   216	220	361	java/lang/Throwable
      //   224	229	361	java/lang/Throwable
      //   233	238	361	java/lang/Throwable
      //   242	251	361	java/lang/Throwable
      //   255	265	361	java/lang/Throwable
      //   269	278	361	java/lang/Throwable
      //   282	284	361	java/lang/Throwable
      //   288	292	361	java/lang/Throwable
      //   296	301	361	java/lang/Throwable
      //   305	310	361	java/lang/Throwable
      //   314	323	361	java/lang/Throwable
      //   327	337	361	java/lang/Throwable
      //   341	350	361	java/lang/Throwable
      //   354	356	361	java/lang/Throwable
      //   380	387	390	java/lang/Throwable
      //   16	25	412	android/content/pm/PackageManager$NameNotFoundException
      //   204	211	412	android/content/pm/PackageManager$NameNotFoundException
      //   380	387	412	android/content/pm/PackageManager$NameNotFoundException
      //   392	399	412	android/content/pm/PackageManager$NameNotFoundException
      //   402	409	412	android/content/pm/PackageManager$NameNotFoundException
      //   409	412	412	android/content/pm/PackageManager$NameNotFoundException
      //   16	25	457	java/io/IOException
      //   16	25	457	org/xmlpull/v1/XmlPullParserException
      //   204	211	457	java/io/IOException
      //   204	211	457	org/xmlpull/v1/XmlPullParserException
      //   380	387	457	java/io/IOException
      //   380	387	457	org/xmlpull/v1/XmlPullParserException
      //   392	399	457	java/io/IOException
      //   392	399	457	org/xmlpull/v1/XmlPullParserException
      //   402	409	457	java/io/IOException
      //   402	409	457	org/xmlpull/v1/XmlPullParserException
      //   409	412	457	java/io/IOException
      //   409	412	457	org/xmlpull/v1/XmlPullParserException
    }
    
    public TvInputInfo build()
    {
      Object localObject1 = new ComponentName(mResolveInfo.serviceInfo.packageName, mResolveInfo.serviceInfo.name);
      boolean bool1 = false;
      boolean bool2 = false;
      Object localObject2 = mHdmiDeviceInfo;
      int i = 0;
      int j;
      boolean bool3;
      if (localObject2 != null)
      {
        localObject2 = generateInputId((ComponentName)localObject1, mHdmiDeviceInfo);
        j = 1007;
        bool3 = true;
        if ((mHdmiDeviceInfo.getPhysicalAddress() & 0xFFF) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        bool2 = bool1;
        bool1 = bool3;
      }
      else if (mTvInputHardwareInfo != null)
      {
        localObject2 = generateInputId((ComponentName)localObject1, mTvInputHardwareInfo);
        j = sHardwareTypeToTvInputType.get(mTvInputHardwareInfo.getType(), 0);
        bool1 = true;
      }
      else
      {
        localObject2 = generateInputId((ComponentName)localObject1);
        j = 0;
      }
      parseServiceMetadata(j);
      ResolveInfo localResolveInfo = mResolveInfo;
      CharSequence localCharSequence = mLabel;
      int k = mLabelResId;
      localObject1 = mIcon;
      Icon localIcon1 = mIconStandby;
      Icon localIcon2 = mIconDisconnected;
      String str = mSetupActivity;
      if (mCanRecord == null) {
        bool3 = false;
      } else {
        bool3 = mCanRecord.booleanValue();
      }
      if (mTunerCount != null) {
        for (;;)
        {
          i = mTunerCount.intValue();
        }
      }
      return new TvInputInfo(localResolveInfo, (String)localObject2, j, bool1, localCharSequence, k, (Icon)localObject1, localIcon1, localIcon2, str, bool3, i, mHdmiDeviceInfo, bool2, mParentId, mExtras, null);
    }
    
    public Builder setCanRecord(boolean paramBoolean)
    {
      mCanRecord = Boolean.valueOf(paramBoolean);
      return this;
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      mExtras = paramBundle;
      return this;
    }
    
    @SystemApi
    public Builder setHdmiDeviceInfo(HdmiDeviceInfo paramHdmiDeviceInfo)
    {
      if (mTvInputHardwareInfo != null)
      {
        Log.w("TvInputInfo", "TvInputHardwareInfo will not be used to build this TvInputInfo");
        mTvInputHardwareInfo = null;
      }
      mHdmiDeviceInfo = paramHdmiDeviceInfo;
      return this;
    }
    
    @SystemApi
    public Builder setIcon(Icon paramIcon)
    {
      mIcon = paramIcon;
      return this;
    }
    
    @SystemApi
    public Builder setIcon(Icon paramIcon, int paramInt)
    {
      if (paramInt == 0)
      {
        mIcon = paramIcon;
      }
      else if (paramInt == 1)
      {
        mIconStandby = paramIcon;
      }
      else
      {
        if (paramInt != 2) {
          break label37;
        }
        mIconDisconnected = paramIcon;
      }
      return this;
      label37:
      paramIcon = new StringBuilder();
      paramIcon.append("Unknown state: ");
      paramIcon.append(paramInt);
      throw new IllegalArgumentException(paramIcon.toString());
    }
    
    @SystemApi
    public Builder setLabel(int paramInt)
    {
      if (mLabel == null)
      {
        mLabelResId = paramInt;
        return this;
      }
      throw new IllegalStateException("Label text is already set.");
    }
    
    @SystemApi
    public Builder setLabel(CharSequence paramCharSequence)
    {
      if (mLabelResId == 0)
      {
        mLabel = paramCharSequence;
        return this;
      }
      throw new IllegalStateException("Resource ID for label is already set.");
    }
    
    @SystemApi
    public Builder setParentId(String paramString)
    {
      mParentId = paramString;
      return this;
    }
    
    public Builder setTunerCount(int paramInt)
    {
      mTunerCount = Integer.valueOf(paramInt);
      return this;
    }
    
    @SystemApi
    public Builder setTvInputHardwareInfo(TvInputHardwareInfo paramTvInputHardwareInfo)
    {
      if (mHdmiDeviceInfo != null)
      {
        Log.w("TvInputInfo", "mHdmiDeviceInfo will not be used to build this TvInputInfo");
        mHdmiDeviceInfo = null;
      }
      mTvInputHardwareInfo = paramTvInputHardwareInfo;
      return this;
    }
  }
  
  @SystemApi
  public static final class TvInputSettings
  {
    private static final String CUSTOM_NAME_SEPARATOR = ",";
    private static final String TV_INPUT_SEPARATOR = ":";
    
    private TvInputSettings() {}
    
    private static void ensureValidField(String paramString)
    {
      if (!TextUtils.isEmpty(paramString)) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" should not empty ");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    private static String getCustomLabel(Context paramContext, String paramString, int paramInt)
    {
      return (String)getCustomLabels(paramContext, paramInt).get(paramString);
    }
    
    @SystemApi
    public static Map<String, String> getCustomLabels(Context paramContext, int paramInt)
    {
      Object localObject = Settings.Secure.getStringForUser(paramContext.getContentResolver(), "tv_input_custom_labels", paramInt);
      paramContext = new HashMap();
      if (TextUtils.isEmpty((CharSequence)localObject)) {
        return paramContext;
      }
      localObject = ((String)localObject).split(":");
      int i = localObject.length;
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        String[] arrayOfString = localObject[paramInt].split(",");
        paramContext.put(Uri.decode(arrayOfString[0]), Uri.decode(arrayOfString[1]));
      }
      return paramContext;
    }
    
    @SystemApi
    public static Set<String> getHiddenTvInputIds(Context paramContext, int paramInt)
    {
      Object localObject = Settings.Secure.getStringForUser(paramContext.getContentResolver(), "tv_input_hidden_inputs", paramInt);
      paramContext = new HashSet();
      if (TextUtils.isEmpty((CharSequence)localObject)) {
        return paramContext;
      }
      localObject = ((String)localObject).split(":");
      int i = localObject.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        paramContext.add(Uri.decode(localObject[paramInt]));
      }
      return paramContext;
    }
    
    private static boolean isHidden(Context paramContext, String paramString, int paramInt)
    {
      return getHiddenTvInputIds(paramContext, paramInt).contains(paramString);
    }
    
    @SystemApi
    public static void putCustomLabels(Context paramContext, Map<String, String> paramMap, int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 1;
      Iterator localIterator = paramMap.entrySet().iterator();
      Object localObject;
      while (localIterator.hasNext())
      {
        localObject = (Map.Entry)localIterator.next();
        ensureValidField((String)((Map.Entry)localObject).getKey());
        ensureValidField((String)((Map.Entry)localObject).getValue());
        if (i != 0) {
          i = 0;
        } else {
          localStringBuilder.append(":");
        }
        localStringBuilder.append(Uri.encode((String)((Map.Entry)localObject).getKey()));
        localStringBuilder.append(",");
        localStringBuilder.append(Uri.encode((String)((Map.Entry)localObject).getValue()));
      }
      Settings.Secure.putStringForUser(paramContext.getContentResolver(), "tv_input_custom_labels", localStringBuilder.toString(), paramInt);
      paramContext = (TvInputManager)paramContext.getSystemService("tv_input");
      paramMap = paramMap.keySet().iterator();
      while (paramMap.hasNext())
      {
        localObject = paramContext.getTvInputInfo((String)paramMap.next());
        if (localObject != null) {
          paramContext.updateTvInputInfo((TvInputInfo)localObject);
        }
      }
    }
    
    @SystemApi
    public static void putHiddenTvInputs(Context paramContext, Set<String> paramSet, int paramInt)
    {
      Object localObject = new StringBuilder();
      int i = 1;
      Iterator localIterator = paramSet.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        ensureValidField(str);
        if (i != 0) {
          i = 0;
        } else {
          ((StringBuilder)localObject).append(":");
        }
        ((StringBuilder)localObject).append(Uri.encode(str));
      }
      Settings.Secure.putStringForUser(paramContext.getContentResolver(), "tv_input_hidden_inputs", ((StringBuilder)localObject).toString(), paramInt);
      paramContext = (TvInputManager)paramContext.getSystemService("tv_input");
      localObject = paramSet.iterator();
      while (((Iterator)localObject).hasNext())
      {
        paramSet = paramContext.getTvInputInfo((String)((Iterator)localObject).next());
        if (paramSet != null) {
          paramContext.updateTvInputInfo(paramSet);
        }
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Type {}
}
