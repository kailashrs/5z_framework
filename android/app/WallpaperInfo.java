package android.app;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Printer;

public final class WallpaperInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WallpaperInfo> CREATOR = new Parcelable.Creator()
  {
    public WallpaperInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WallpaperInfo(paramAnonymousParcel);
    }
    
    public WallpaperInfo[] newArray(int paramAnonymousInt)
    {
      return new WallpaperInfo[paramAnonymousInt];
    }
  };
  static final String TAG = "WallpaperInfo";
  final int mAuthorResource;
  final int mContextDescriptionResource;
  final int mContextUriResource;
  final int mDescriptionResource;
  final ResolveInfo mService;
  final String mSettingsActivityName;
  final boolean mShowMetadataInPreview;
  final boolean mSupportsAmbientMode;
  final int mThumbnailResource;
  
  /* Error */
  public WallpaperInfo(android.content.Context paramContext, ResolveInfo paramResolveInfo)
    throws org.xmlpull.v1.XmlPullParserException, java.io.IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 43	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: aload_2
    //   6: putfield 45	android/app/WallpaperInfo:mService	Landroid/content/pm/ResolveInfo;
    //   9: aload_2
    //   10: getfield 51	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   13: astore_3
    //   14: aload_1
    //   15: invokevirtual 57	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   18: astore 4
    //   20: aconst_null
    //   21: astore_2
    //   22: aconst_null
    //   23: astore_1
    //   24: aload_3
    //   25: aload 4
    //   27: ldc 59
    //   29: invokevirtual 65	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   32: astore 5
    //   34: aload 5
    //   36: ifnull +296 -> 332
    //   39: aload 5
    //   41: astore_1
    //   42: aload 5
    //   44: astore_2
    //   45: aload 4
    //   47: aload_3
    //   48: getfield 69	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   51: invokevirtual 75	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   54: astore 4
    //   56: aload 5
    //   58: astore_1
    //   59: aload 5
    //   61: astore_2
    //   62: aload 5
    //   64: invokestatic 81	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   67: astore 6
    //   69: aload 5
    //   71: astore_1
    //   72: aload 5
    //   74: astore_2
    //   75: aload 5
    //   77: invokeinterface 87 1 0
    //   82: istore 7
    //   84: iload 7
    //   86: iconst_1
    //   87: if_icmpeq +12 -> 99
    //   90: iload 7
    //   92: iconst_2
    //   93: if_icmpeq +6 -> 99
    //   96: goto -27 -> 69
    //   99: aload 5
    //   101: astore_1
    //   102: aload 5
    //   104: astore_2
    //   105: ldc 89
    //   107: aload 5
    //   109: invokeinterface 93 1 0
    //   114: invokevirtual 99	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   117: ifeq +182 -> 299
    //   120: aload 5
    //   122: astore_1
    //   123: aload 5
    //   125: astore_2
    //   126: aload 4
    //   128: aload 6
    //   130: getstatic 105	com/android/internal/R$styleable:Wallpaper	[I
    //   133: invokevirtual 111	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   136: astore 4
    //   138: aload 5
    //   140: astore_1
    //   141: aload 5
    //   143: astore_2
    //   144: aload_0
    //   145: aload 4
    //   147: iconst_1
    //   148: invokevirtual 117	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   151: putfield 119	android/app/WallpaperInfo:mSettingsActivityName	Ljava/lang/String;
    //   154: aload 5
    //   156: astore_1
    //   157: aload 5
    //   159: astore_2
    //   160: aload_0
    //   161: aload 4
    //   163: iconst_2
    //   164: iconst_m1
    //   165: invokevirtual 123	android/content/res/TypedArray:getResourceId	(II)I
    //   168: putfield 125	android/app/WallpaperInfo:mThumbnailResource	I
    //   171: aload 5
    //   173: astore_1
    //   174: aload 5
    //   176: astore_2
    //   177: aload_0
    //   178: aload 4
    //   180: iconst_3
    //   181: iconst_m1
    //   182: invokevirtual 123	android/content/res/TypedArray:getResourceId	(II)I
    //   185: putfield 127	android/app/WallpaperInfo:mAuthorResource	I
    //   188: aload 5
    //   190: astore_1
    //   191: aload 5
    //   193: astore_2
    //   194: aload_0
    //   195: aload 4
    //   197: iconst_0
    //   198: iconst_m1
    //   199: invokevirtual 123	android/content/res/TypedArray:getResourceId	(II)I
    //   202: putfield 129	android/app/WallpaperInfo:mDescriptionResource	I
    //   205: aload 5
    //   207: astore_1
    //   208: aload 5
    //   210: astore_2
    //   211: aload_0
    //   212: aload 4
    //   214: iconst_4
    //   215: iconst_m1
    //   216: invokevirtual 123	android/content/res/TypedArray:getResourceId	(II)I
    //   219: putfield 131	android/app/WallpaperInfo:mContextUriResource	I
    //   222: aload 5
    //   224: astore_1
    //   225: aload 5
    //   227: astore_2
    //   228: aload_0
    //   229: aload 4
    //   231: iconst_5
    //   232: iconst_m1
    //   233: invokevirtual 123	android/content/res/TypedArray:getResourceId	(II)I
    //   236: putfield 133	android/app/WallpaperInfo:mContextDescriptionResource	I
    //   239: aload 5
    //   241: astore_1
    //   242: aload 5
    //   244: astore_2
    //   245: aload_0
    //   246: aload 4
    //   248: bipush 6
    //   250: iconst_0
    //   251: invokevirtual 137	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   254: putfield 139	android/app/WallpaperInfo:mShowMetadataInPreview	Z
    //   257: aload 5
    //   259: astore_1
    //   260: aload 5
    //   262: astore_2
    //   263: aload_0
    //   264: aload 4
    //   266: bipush 7
    //   268: iconst_0
    //   269: invokevirtual 137	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   272: putfield 141	android/app/WallpaperInfo:mSupportsAmbientMode	Z
    //   275: aload 5
    //   277: astore_1
    //   278: aload 5
    //   280: astore_2
    //   281: aload 4
    //   283: invokevirtual 144	android/content/res/TypedArray:recycle	()V
    //   286: aload 5
    //   288: ifnull +10 -> 298
    //   291: aload 5
    //   293: invokeinterface 147 1 0
    //   298: return
    //   299: aload 5
    //   301: astore_1
    //   302: aload 5
    //   304: astore_2
    //   305: new 38	org/xmlpull/v1/XmlPullParserException
    //   308: astore 4
    //   310: aload 5
    //   312: astore_1
    //   313: aload 5
    //   315: astore_2
    //   316: aload 4
    //   318: ldc -107
    //   320: invokespecial 152	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   323: aload 5
    //   325: astore_1
    //   326: aload 5
    //   328: astore_2
    //   329: aload 4
    //   331: athrow
    //   332: aload 5
    //   334: astore_1
    //   335: aload 5
    //   337: astore_2
    //   338: new 38	org/xmlpull/v1/XmlPullParserException
    //   341: astore 4
    //   343: aload 5
    //   345: astore_1
    //   346: aload 5
    //   348: astore_2
    //   349: aload 4
    //   351: ldc -102
    //   353: invokespecial 152	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   356: aload 5
    //   358: astore_1
    //   359: aload 5
    //   361: astore_2
    //   362: aload 4
    //   364: athrow
    //   365: astore_2
    //   366: goto +64 -> 430
    //   369: astore_1
    //   370: aload_2
    //   371: astore_1
    //   372: new 38	org/xmlpull/v1/XmlPullParserException
    //   375: astore 5
    //   377: aload_2
    //   378: astore_1
    //   379: new 156	java/lang/StringBuilder
    //   382: astore 4
    //   384: aload_2
    //   385: astore_1
    //   386: aload 4
    //   388: invokespecial 157	java/lang/StringBuilder:<init>	()V
    //   391: aload_2
    //   392: astore_1
    //   393: aload 4
    //   395: ldc -97
    //   397: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   400: pop
    //   401: aload_2
    //   402: astore_1
    //   403: aload 4
    //   405: aload_3
    //   406: getfield 166	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   409: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   412: pop
    //   413: aload_2
    //   414: astore_1
    //   415: aload 5
    //   417: aload 4
    //   419: invokevirtual 169	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   422: invokespecial 152	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   425: aload_2
    //   426: astore_1
    //   427: aload 5
    //   429: athrow
    //   430: aload_1
    //   431: ifnull +9 -> 440
    //   434: aload_1
    //   435: invokeinterface 147 1 0
    //   440: aload_2
    //   441: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	442	0	this	WallpaperInfo
    //   0	442	1	paramContext	android.content.Context
    //   0	442	2	paramResolveInfo	ResolveInfo
    //   13	393	3	localServiceInfo	ServiceInfo
    //   18	400	4	localObject1	Object
    //   32	396	5	localObject2	Object
    //   67	62	6	localAttributeSet	android.util.AttributeSet
    //   82	12	7	i	int
    // Exception table:
    //   from	to	target	type
    //   24	34	365	finally
    //   45	56	365	finally
    //   62	69	365	finally
    //   75	84	365	finally
    //   105	120	365	finally
    //   126	138	365	finally
    //   144	154	365	finally
    //   160	171	365	finally
    //   177	188	365	finally
    //   194	205	365	finally
    //   211	222	365	finally
    //   228	239	365	finally
    //   245	257	365	finally
    //   263	275	365	finally
    //   281	286	365	finally
    //   305	310	365	finally
    //   316	323	365	finally
    //   329	332	365	finally
    //   338	343	365	finally
    //   349	356	365	finally
    //   362	365	365	finally
    //   372	377	365	finally
    //   379	384	365	finally
    //   386	391	365	finally
    //   393	401	365	finally
    //   403	413	365	finally
    //   415	425	365	finally
    //   427	430	365	finally
    //   24	34	369	android/content/pm/PackageManager$NameNotFoundException
    //   45	56	369	android/content/pm/PackageManager$NameNotFoundException
    //   62	69	369	android/content/pm/PackageManager$NameNotFoundException
    //   75	84	369	android/content/pm/PackageManager$NameNotFoundException
    //   105	120	369	android/content/pm/PackageManager$NameNotFoundException
    //   126	138	369	android/content/pm/PackageManager$NameNotFoundException
    //   144	154	369	android/content/pm/PackageManager$NameNotFoundException
    //   160	171	369	android/content/pm/PackageManager$NameNotFoundException
    //   177	188	369	android/content/pm/PackageManager$NameNotFoundException
    //   194	205	369	android/content/pm/PackageManager$NameNotFoundException
    //   211	222	369	android/content/pm/PackageManager$NameNotFoundException
    //   228	239	369	android/content/pm/PackageManager$NameNotFoundException
    //   245	257	369	android/content/pm/PackageManager$NameNotFoundException
    //   263	275	369	android/content/pm/PackageManager$NameNotFoundException
    //   281	286	369	android/content/pm/PackageManager$NameNotFoundException
    //   305	310	369	android/content/pm/PackageManager$NameNotFoundException
    //   316	323	369	android/content/pm/PackageManager$NameNotFoundException
    //   329	332	369	android/content/pm/PackageManager$NameNotFoundException
    //   338	343	369	android/content/pm/PackageManager$NameNotFoundException
    //   349	356	369	android/content/pm/PackageManager$NameNotFoundException
    //   362	365	369	android/content/pm/PackageManager$NameNotFoundException
  }
  
  WallpaperInfo(Parcel paramParcel)
  {
    mSettingsActivityName = paramParcel.readString();
    mThumbnailResource = paramParcel.readInt();
    mAuthorResource = paramParcel.readInt();
    mDescriptionResource = paramParcel.readInt();
    mContextUriResource = paramParcel.readInt();
    mContextDescriptionResource = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mShowMetadataInPreview = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mSupportsAmbientMode = bool2;
    mService = ((ResolveInfo)ResolveInfo.CREATOR.createFromParcel(paramParcel));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("Service:");
    paramPrinter.println(localStringBuilder.toString());
    ResolveInfo localResolveInfo = mService;
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  ");
    localResolveInfo.dump(paramPrinter, localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("mSettingsActivityName=");
    localStringBuilder.append(mSettingsActivityName);
    paramPrinter.println(localStringBuilder.toString());
  }
  
  public ComponentName getComponent()
  {
    return new ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
  }
  
  public String getPackageName()
  {
    return mService.serviceInfo.packageName;
  }
  
  public ServiceInfo getServiceInfo()
  {
    return mService.serviceInfo;
  }
  
  public String getServiceName()
  {
    return mService.serviceInfo.name;
  }
  
  public String getSettingsActivity()
  {
    return mSettingsActivityName;
  }
  
  public boolean getShowMetadataInPreview()
  {
    return mShowMetadataInPreview;
  }
  
  public boolean getSupportsAmbientMode()
  {
    return mSupportsAmbientMode;
  }
  
  public CharSequence loadAuthor(PackageManager paramPackageManager)
    throws Resources.NotFoundException
  {
    if (mAuthorResource > 0)
    {
      String str1 = mService.resolvePackageName;
      ApplicationInfo localApplicationInfo = null;
      String str2 = str1;
      if (str1 == null)
      {
        str2 = mService.serviceInfo.packageName;
        localApplicationInfo = mService.serviceInfo.applicationInfo;
      }
      return paramPackageManager.getText(str2, mAuthorResource, localApplicationInfo);
    }
    throw new Resources.NotFoundException();
  }
  
  public CharSequence loadContextDescription(PackageManager paramPackageManager)
    throws Resources.NotFoundException
  {
    if (mContextDescriptionResource > 0)
    {
      String str1 = mService.resolvePackageName;
      ApplicationInfo localApplicationInfo = null;
      String str2 = str1;
      if (str1 == null)
      {
        str2 = mService.serviceInfo.packageName;
        localApplicationInfo = mService.serviceInfo.applicationInfo;
      }
      return paramPackageManager.getText(str2, mContextDescriptionResource, localApplicationInfo).toString();
    }
    throw new Resources.NotFoundException();
  }
  
  public Uri loadContextUri(PackageManager paramPackageManager)
    throws Resources.NotFoundException
  {
    if (mContextUriResource > 0)
    {
      String str1 = mService.resolvePackageName;
      ApplicationInfo localApplicationInfo = null;
      String str2 = str1;
      if (str1 == null)
      {
        str2 = mService.serviceInfo.packageName;
        localApplicationInfo = mService.serviceInfo.applicationInfo;
      }
      paramPackageManager = paramPackageManager.getText(str2, mContextUriResource, localApplicationInfo).toString();
      if (paramPackageManager == null) {
        return null;
      }
      return Uri.parse(paramPackageManager);
    }
    throw new Resources.NotFoundException();
  }
  
  public CharSequence loadDescription(PackageManager paramPackageManager)
    throws Resources.NotFoundException
  {
    String str1 = mService.resolvePackageName;
    ApplicationInfo localApplicationInfo = null;
    String str2 = str1;
    if (str1 == null)
    {
      str2 = mService.serviceInfo.packageName;
      localApplicationInfo = mService.serviceInfo.applicationInfo;
    }
    if (mService.serviceInfo.descriptionRes != 0) {
      return paramPackageManager.getText(str2, mService.serviceInfo.descriptionRes, localApplicationInfo);
    }
    if (mDescriptionResource > 0) {
      return paramPackageManager.getText(str2, mDescriptionResource, mService.serviceInfo.applicationInfo);
    }
    throw new Resources.NotFoundException();
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    return mService.loadIcon(paramPackageManager);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    return mService.loadLabel(paramPackageManager);
  }
  
  public Drawable loadThumbnail(PackageManager paramPackageManager)
  {
    if (mThumbnailResource < 0) {
      return null;
    }
    return paramPackageManager.getDrawable(mService.serviceInfo.packageName, mThumbnailResource, mService.serviceInfo.applicationInfo);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WallpaperInfo{");
    localStringBuilder.append(mService.serviceInfo.name);
    localStringBuilder.append(", settings: ");
    localStringBuilder.append(mSettingsActivityName);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mSettingsActivityName);
    paramParcel.writeInt(mThumbnailResource);
    paramParcel.writeInt(mAuthorResource);
    paramParcel.writeInt(mDescriptionResource);
    paramParcel.writeInt(mContextUriResource);
    paramParcel.writeInt(mContextDescriptionResource);
    paramParcel.writeInt(mShowMetadataInPreview);
    paramParcel.writeInt(mSupportsAmbientMode);
    mService.writeToParcel(paramParcel, paramInt);
  }
}
