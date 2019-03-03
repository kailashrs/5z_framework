package android.content;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
import java.util.List;

public class ClipData
  implements Parcelable
{
  public static final Parcelable.Creator<ClipData> CREATOR = new Parcelable.Creator()
  {
    public ClipData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ClipData(paramAnonymousParcel);
    }
    
    public ClipData[] newArray(int paramAnonymousInt)
    {
      return new ClipData[paramAnonymousInt];
    }
  };
  static final String[] MIMETYPES_TEXT_HTML;
  static final String[] MIMETYPES_TEXT_INTENT;
  static final String[] MIMETYPES_TEXT_PLAIN = { "text/plain" };
  static final String[] MIMETYPES_TEXT_URILIST;
  final ClipDescription mClipDescription;
  final Bitmap mIcon;
  final ArrayList<Item> mItems;
  
  static
  {
    MIMETYPES_TEXT_HTML = new String[] { "text/html" };
    MIMETYPES_TEXT_URILIST = new String[] { "text/uri-list" };
    MIMETYPES_TEXT_INTENT = new String[] { "text/vnd.android.intent" };
  }
  
  public ClipData(ClipData paramClipData)
  {
    mClipDescription = mClipDescription;
    mIcon = mIcon;
    mItems = new ArrayList(mItems);
  }
  
  public ClipData(ClipDescription paramClipDescription, Item paramItem)
  {
    mClipDescription = paramClipDescription;
    if (paramItem != null)
    {
      mIcon = null;
      mItems = new ArrayList();
      mItems.add(paramItem);
      return;
    }
    throw new NullPointerException("item is null");
  }
  
  public ClipData(ClipDescription paramClipDescription, ArrayList<Item> paramArrayList)
  {
    mClipDescription = paramClipDescription;
    if (paramArrayList != null)
    {
      mIcon = null;
      mItems = paramArrayList;
      return;
    }
    throw new NullPointerException("item is null");
  }
  
  ClipData(Parcel paramParcel)
  {
    mClipDescription = new ClipDescription(paramParcel);
    if (paramParcel.readInt() != 0) {
      mIcon = ((Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel));
    } else {
      mIcon = null;
    }
    mItems = new ArrayList();
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      CharSequence localCharSequence = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
      String str = paramParcel.readString();
      Intent localIntent;
      if (paramParcel.readInt() != 0) {
        localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel);
      } else {
        localIntent = null;
      }
      Uri localUri;
      if (paramParcel.readInt() != 0) {
        localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel);
      } else {
        localUri = null;
      }
      mItems.add(new Item(localCharSequence, str, localIntent, localUri));
    }
  }
  
  public ClipData(CharSequence paramCharSequence, String[] paramArrayOfString, Item paramItem)
  {
    mClipDescription = new ClipDescription(paramCharSequence, paramArrayOfString);
    if (paramItem != null)
    {
      mIcon = null;
      mItems = new ArrayList();
      mItems.add(paramItem);
      return;
    }
    throw new NullPointerException("item is null");
  }
  
  private static String[] getMimeTypes(ContentResolver paramContentResolver, Uri paramUri)
  {
    Object localObject = null;
    if ("content".equals(paramUri.getScheme()))
    {
      String str = paramContentResolver.getType(paramUri);
      paramContentResolver = paramContentResolver.getStreamTypes(paramUri, "*/*");
      localObject = paramContentResolver;
      if (str != null) {
        if (paramContentResolver == null)
        {
          localObject = new String[] { str };
        }
        else
        {
          localObject = paramContentResolver;
          if (!ArrayUtils.contains(paramContentResolver, str))
          {
            localObject = new String[paramContentResolver.length + 1];
            localObject[0] = str;
            System.arraycopy(paramContentResolver, 0, localObject, 1, paramContentResolver.length);
          }
        }
      }
    }
    paramContentResolver = (ContentResolver)localObject;
    if (localObject == null) {
      paramContentResolver = MIMETYPES_TEXT_URILIST;
    }
    return paramContentResolver;
  }
  
  public static ClipData newHtmlText(CharSequence paramCharSequence1, CharSequence paramCharSequence2, String paramString)
  {
    paramCharSequence2 = new Item(paramCharSequence2, paramString);
    return new ClipData(paramCharSequence1, MIMETYPES_TEXT_HTML, paramCharSequence2);
  }
  
  public static ClipData newIntent(CharSequence paramCharSequence, Intent paramIntent)
  {
    paramIntent = new Item(paramIntent);
    return new ClipData(paramCharSequence, MIMETYPES_TEXT_INTENT, paramIntent);
  }
  
  public static ClipData newPlainText(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    paramCharSequence2 = new Item(paramCharSequence2);
    return new ClipData(paramCharSequence1, MIMETYPES_TEXT_PLAIN, paramCharSequence2);
  }
  
  public static ClipData newRawUri(CharSequence paramCharSequence, Uri paramUri)
  {
    paramUri = new Item(paramUri);
    return new ClipData(paramCharSequence, MIMETYPES_TEXT_URILIST, paramUri);
  }
  
  public static ClipData newUri(ContentResolver paramContentResolver, CharSequence paramCharSequence, Uri paramUri)
  {
    Item localItem = new Item(paramUri);
    return new ClipData(paramCharSequence, getMimeTypes(paramContentResolver, paramUri), localItem);
  }
  
  public void addItem(Item paramItem)
  {
    if (paramItem != null)
    {
      mItems.add(paramItem);
      return;
    }
    throw new NullPointerException("item is null");
  }
  
  @Deprecated
  public void addItem(Item paramItem, ContentResolver paramContentResolver)
  {
    addItem(paramContentResolver, paramItem);
  }
  
  public void addItem(ContentResolver paramContentResolver, Item paramItem)
  {
    addItem(paramItem);
    if (paramItem.getHtmlText() != null) {
      mClipDescription.addMimeTypes(MIMETYPES_TEXT_HTML);
    } else if (paramItem.getText() != null) {
      mClipDescription.addMimeTypes(MIMETYPES_TEXT_PLAIN);
    }
    if (paramItem.getIntent() != null) {
      mClipDescription.addMimeTypes(MIMETYPES_TEXT_INTENT);
    }
    if (paramItem.getUri() != null) {
      mClipDescription.addMimeTypes(getMimeTypes(paramContentResolver, paramItem.getUri()));
    }
  }
  
  public void collectUris(List<Uri> paramList)
  {
    for (int i = 0; i < mItems.size(); i++)
    {
      Object localObject = getItemAt(i);
      if (((Item)localObject).getUri() != null) {
        paramList.add(((Item)localObject).getUri());
      }
      localObject = ((Item)localObject).getIntent();
      if (localObject != null)
      {
        if (((Intent)localObject).getData() != null) {
          paramList.add(((Intent)localObject).getData());
        }
        if (((Intent)localObject).getClipData() != null) {
          ((Intent)localObject).getClipData().collectUris(paramList);
        }
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void fixUris(int paramInt)
  {
    int i = mItems.size();
    for (int j = 0; j < i; j++)
    {
      Item localItem = (Item)mItems.get(j);
      if (mIntent != null) {
        mIntent.fixUris(paramInt);
      }
      if (mUri != null) {
        mUri = ContentProvider.maybeAddUserId(mUri, paramInt);
      }
    }
  }
  
  public void fixUrisLight(int paramInt)
  {
    int i = mItems.size();
    for (int j = 0; j < i; j++)
    {
      Item localItem = (Item)mItems.get(j);
      if (mIntent != null)
      {
        Uri localUri = mIntent.getData();
        if (localUri != null) {
          mIntent.setData(ContentProvider.maybeAddUserId(localUri, paramInt));
        }
      }
      if (mUri != null) {
        mUri = ContentProvider.maybeAddUserId(mUri, paramInt);
      }
    }
  }
  
  public ClipDescription getDescription()
  {
    return mClipDescription;
  }
  
  public Bitmap getIcon()
  {
    return mIcon;
  }
  
  public Item getItemAt(int paramInt)
  {
    return (Item)mItems.get(paramInt);
  }
  
  public int getItemCount()
  {
    return mItems.size();
  }
  
  public void prepareToEnterProcess()
  {
    int i = mItems.size();
    for (int j = 0; j < i; j++)
    {
      Item localItem = (Item)mItems.get(j);
      if (mIntent != null) {
        mIntent.prepareToEnterProcess();
      }
    }
  }
  
  public void prepareToLeaveProcess(boolean paramBoolean)
  {
    prepareToLeaveProcess(paramBoolean, 1);
  }
  
  public void prepareToLeaveProcess(boolean paramBoolean, int paramInt)
  {
    int i = mItems.size();
    for (int j = 0; j < i; j++)
    {
      Item localItem = (Item)mItems.get(j);
      if (mIntent != null) {
        mIntent.prepareToLeaveProcess(paramBoolean);
      }
      if ((mUri != null) && (paramBoolean))
      {
        if (StrictMode.vmFileUriExposureEnabled()) {
          mUri.checkFileUriExposed("ClipData.Item.getUri()");
        }
        if (StrictMode.vmContentUriWithoutPermissionEnabled()) {
          mUri.checkContentUriWithoutPermission("ClipData.Item.getUri()", paramInt);
        }
      }
    }
  }
  
  public void setItemAt(int paramInt, Item paramItem)
  {
    mItems.set(paramInt, paramItem);
  }
  
  public void toShortString(StringBuilder paramStringBuilder)
  {
    ClipDescription localClipDescription = mClipDescription;
    int i = 1;
    if (localClipDescription != null) {
      i = mClipDescription.toShortString(paramStringBuilder) ^ true;
    }
    int j = i;
    i = j;
    if (mIcon != null)
    {
      if (j == 0) {
        paramStringBuilder.append(' ');
      }
      i = 0;
      paramStringBuilder.append("I:");
      paramStringBuilder.append(mIcon.getWidth());
      paramStringBuilder.append('x');
      paramStringBuilder.append(mIcon.getHeight());
    }
    for (j = 0; j < mItems.size(); j++)
    {
      if (i == 0) {
        paramStringBuilder.append(' ');
      }
      i = 0;
      paramStringBuilder.append('{');
      ((Item)mItems.get(j)).toShortString(paramStringBuilder);
      paramStringBuilder.append('}');
    }
  }
  
  public void toShortStringShortItems(StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    if (mItems.size() > 0)
    {
      if (!paramBoolean) {
        paramStringBuilder.append(' ');
      }
      ((Item)mItems.get(0)).toShortString(paramStringBuilder);
      if (mItems.size() > 1) {
        paramStringBuilder.append(" ...");
      }
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("ClipData { ");
    toShortString(localStringBuilder);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mClipDescription.writeToParcel(paramParcel, paramInt);
    if (mIcon != null)
    {
      paramParcel.writeInt(1);
      mIcon.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    int i = mItems.size();
    paramParcel.writeInt(i);
    for (int j = 0; j < i; j++)
    {
      Item localItem = (Item)mItems.get(j);
      TextUtils.writeToParcel(mText, paramParcel, paramInt);
      paramParcel.writeString(mHtmlText);
      if (mIntent != null)
      {
        paramParcel.writeInt(1);
        mIntent.writeToParcel(paramParcel, paramInt);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mUri != null)
      {
        paramParcel.writeInt(1);
        mUri.writeToParcel(paramParcel, paramInt);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    long l = paramProtoOutputStream.start(paramLong);
    if (mClipDescription != null) {
      mClipDescription.writeToProto(paramProtoOutputStream, 1146756268033L);
    }
    if (mIcon != null)
    {
      paramLong = paramProtoOutputStream.start(1146756268034L);
      paramProtoOutputStream.write(1120986464257L, mIcon.getWidth());
      paramProtoOutputStream.write(1120986464258L, mIcon.getHeight());
      paramProtoOutputStream.end(paramLong);
    }
    for (int i = 0; i < mItems.size(); i++) {
      ((Item)mItems.get(i)).writeToProto(paramProtoOutputStream, 2246267895811L);
    }
    paramProtoOutputStream.end(l);
  }
  
  public static class Item
  {
    final String mHtmlText;
    final Intent mIntent;
    final CharSequence mText;
    Uri mUri;
    
    public Item(Item paramItem)
    {
      mText = mText;
      mHtmlText = mHtmlText;
      mIntent = mIntent;
      mUri = mUri;
    }
    
    public Item(Intent paramIntent)
    {
      mText = null;
      mHtmlText = null;
      mIntent = paramIntent;
      mUri = null;
    }
    
    public Item(Uri paramUri)
    {
      mText = null;
      mHtmlText = null;
      mIntent = null;
      mUri = paramUri;
    }
    
    public Item(CharSequence paramCharSequence)
    {
      mText = paramCharSequence;
      mHtmlText = null;
      mIntent = null;
      mUri = null;
    }
    
    public Item(CharSequence paramCharSequence, Intent paramIntent, Uri paramUri)
    {
      mText = paramCharSequence;
      mHtmlText = null;
      mIntent = paramIntent;
      mUri = paramUri;
    }
    
    public Item(CharSequence paramCharSequence, String paramString)
    {
      mText = paramCharSequence;
      mHtmlText = paramString;
      mIntent = null;
      mUri = null;
    }
    
    public Item(CharSequence paramCharSequence, String paramString, Intent paramIntent, Uri paramUri)
    {
      if ((paramString != null) && (paramCharSequence == null)) {
        throw new IllegalArgumentException("Plain text must be supplied if HTML text is supplied");
      }
      mText = paramCharSequence;
      mHtmlText = paramString;
      mIntent = paramIntent;
      mUri = paramUri;
    }
    
    /* Error */
    private CharSequence coerceToHtmlOrStyledText(Context paramContext, boolean paramBoolean)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 28	android/content/ClipData$Item:mUri	Landroid/net/Uri;
      //   4: ifnull +700 -> 704
      //   7: aconst_null
      //   8: astore_3
      //   9: aload_1
      //   10: invokevirtual 58	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   13: aload_0
      //   14: getfield 28	android/content/ClipData$Item:mUri	Landroid/net/Uri;
      //   17: ldc 60
      //   19: invokevirtual 66	android/content/ContentResolver:getStreamTypes	(Landroid/net/Uri;Ljava/lang/String;)[Ljava/lang/String;
      //   22: astore 4
      //   24: aload 4
      //   26: astore_3
      //   27: goto +5 -> 32
      //   30: astore 4
      //   32: iconst_0
      //   33: istore 5
      //   35: iconst_0
      //   36: istore 6
      //   38: aload_3
      //   39: ifnull +76 -> 115
      //   42: aload_3
      //   43: arraylength
      //   44: istore 7
      //   46: iconst_0
      //   47: istore 6
      //   49: iconst_0
      //   50: istore 5
      //   52: iconst_0
      //   53: istore 8
      //   55: iload 8
      //   57: iload 7
      //   59: if_icmpge +56 -> 115
      //   62: aload_3
      //   63: iload 8
      //   65: aaload
      //   66: astore 4
      //   68: ldc 68
      //   70: aload 4
      //   72: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   75: ifeq +9 -> 84
      //   78: iconst_1
      //   79: istore 9
      //   81: goto +24 -> 105
      //   84: iload 5
      //   86: istore 9
      //   88: aload 4
      //   90: ldc 76
      //   92: invokevirtual 80	java/lang/String:startsWith	(Ljava/lang/String;)Z
      //   95: ifeq +10 -> 105
      //   98: iconst_1
      //   99: istore 6
      //   101: iload 5
      //   103: istore 9
      //   105: iinc 8 1
      //   108: iload 9
      //   110: istore 5
      //   112: goto -57 -> 55
      //   115: iload 5
      //   117: ifne +8 -> 125
      //   120: iload 6
      //   122: ifeq +499 -> 621
      //   125: aconst_null
      //   126: astore 10
      //   128: aconst_null
      //   129: astore 11
      //   131: aconst_null
      //   132: astore 12
      //   134: aconst_null
      //   135: astore 13
      //   137: aload 13
      //   139: astore_3
      //   140: aload 10
      //   142: astore 4
      //   144: aload 11
      //   146: astore 14
      //   148: aload 12
      //   150: astore 15
      //   152: aload_1
      //   153: invokevirtual 58	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   156: astore 16
      //   158: aload 13
      //   160: astore_3
      //   161: aload 10
      //   163: astore 4
      //   165: aload 11
      //   167: astore 14
      //   169: aload 12
      //   171: astore 15
      //   173: aload_0
      //   174: getfield 28	android/content/ClipData$Item:mUri	Landroid/net/Uri;
      //   177: astore 17
      //   179: iload 5
      //   181: ifeq +9 -> 190
      //   184: ldc 68
      //   186: astore_1
      //   187: goto +6 -> 193
      //   190: ldc 82
      //   192: astore_1
      //   193: aload 13
      //   195: astore_3
      //   196: aload 10
      //   198: astore 4
      //   200: aload 11
      //   202: astore 14
      //   204: aload 12
      //   206: astore 15
      //   208: aload 16
      //   210: aload 17
      //   212: aload_1
      //   213: aconst_null
      //   214: invokevirtual 86	android/content/ContentResolver:openTypedAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/res/AssetFileDescriptor;
      //   217: invokevirtual 92	android/content/res/AssetFileDescriptor:createInputStream	()Ljava/io/FileInputStream;
      //   220: astore_1
      //   221: aload_1
      //   222: astore_3
      //   223: aload_1
      //   224: astore 4
      //   226: aload_1
      //   227: astore 14
      //   229: aload_1
      //   230: astore 15
      //   232: new 94	java/io/InputStreamReader
      //   235: astore 11
      //   237: aload_1
      //   238: astore_3
      //   239: aload_1
      //   240: astore 4
      //   242: aload_1
      //   243: astore 14
      //   245: aload_1
      //   246: astore 15
      //   248: aload 11
      //   250: aload_1
      //   251: ldc 96
      //   253: invokespecial 99	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
      //   256: aload_1
      //   257: astore_3
      //   258: aload_1
      //   259: astore 4
      //   261: aload_1
      //   262: astore 14
      //   264: aload_1
      //   265: astore 15
      //   267: new 101	java/lang/StringBuilder
      //   270: astore 10
      //   272: aload_1
      //   273: astore_3
      //   274: aload_1
      //   275: astore 4
      //   277: aload_1
      //   278: astore 14
      //   280: aload_1
      //   281: astore 15
      //   283: aload 10
      //   285: sipush 128
      //   288: invokespecial 104	java/lang/StringBuilder:<init>	(I)V
      //   291: aload_1
      //   292: astore_3
      //   293: aload_1
      //   294: astore 4
      //   296: aload_1
      //   297: astore 14
      //   299: aload_1
      //   300: astore 15
      //   302: sipush 8192
      //   305: newarray char
      //   307: astore 13
      //   309: aload_1
      //   310: astore_3
      //   311: aload_1
      //   312: astore 4
      //   314: aload_1
      //   315: astore 14
      //   317: aload_1
      //   318: astore 15
      //   320: aload 11
      //   322: aload 13
      //   324: invokevirtual 108	java/io/InputStreamReader:read	([C)I
      //   327: istore 6
      //   329: iload 6
      //   331: ifle +28 -> 359
      //   334: aload_1
      //   335: astore_3
      //   336: aload_1
      //   337: astore 4
      //   339: aload_1
      //   340: astore 14
      //   342: aload_1
      //   343: astore 15
      //   345: aload 10
      //   347: aload 13
      //   349: iconst_0
      //   350: iload 6
      //   352: invokevirtual 112	java/lang/StringBuilder:append	([CII)Ljava/lang/StringBuilder;
      //   355: pop
      //   356: goto -47 -> 309
      //   359: aload_1
      //   360: astore_3
      //   361: aload_1
      //   362: astore 4
      //   364: aload_1
      //   365: astore 14
      //   367: aload_1
      //   368: astore 15
      //   370: aload 10
      //   372: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   375: astore 13
      //   377: iload 5
      //   379: ifeq +102 -> 481
      //   382: iload_2
      //   383: ifeq +65 -> 448
      //   386: aload_1
      //   387: astore_3
      //   388: aload_1
      //   389: astore 4
      //   391: aload_1
      //   392: astore 14
      //   394: aload_1
      //   395: astore 15
      //   397: aload 13
      //   399: invokestatic 122	android/text/Html:fromHtml	(Ljava/lang/String;)Landroid/text/Spanned;
      //   402: astore 10
      //   404: aload 10
      //   406: ifnull +9 -> 415
      //   409: aload 10
      //   411: astore_3
      //   412: goto +6 -> 418
      //   415: aload 13
      //   417: astore_3
      //   418: aload_1
      //   419: ifnull +11 -> 430
      //   422: aload_1
      //   423: invokevirtual 127	java/io/FileInputStream:close	()V
      //   426: goto +4 -> 430
      //   429: astore_1
      //   430: aload_3
      //   431: areturn
      //   432: astore_3
      //   433: aload_1
      //   434: ifnull +11 -> 445
      //   437: aload_1
      //   438: invokevirtual 127	java/io/FileInputStream:close	()V
      //   441: goto +4 -> 445
      //   444: astore_1
      //   445: aload 13
      //   447: areturn
      //   448: aload_1
      //   449: astore_3
      //   450: aload_1
      //   451: astore 4
      //   453: aload_1
      //   454: astore 14
      //   456: aload_1
      //   457: astore 15
      //   459: aload 13
      //   461: invokevirtual 128	java/lang/String:toString	()Ljava/lang/String;
      //   464: astore 13
      //   466: aload_1
      //   467: ifnull +11 -> 478
      //   470: aload_1
      //   471: invokevirtual 127	java/io/FileInputStream:close	()V
      //   474: goto +4 -> 478
      //   477: astore_1
      //   478: aload 13
      //   480: areturn
      //   481: iload_2
      //   482: ifeq +18 -> 500
      //   485: aload_1
      //   486: ifnull +11 -> 497
      //   489: aload_1
      //   490: invokevirtual 127	java/io/FileInputStream:close	()V
      //   493: goto +4 -> 497
      //   496: astore_1
      //   497: aload 13
      //   499: areturn
      //   500: aload_1
      //   501: astore_3
      //   502: aload_1
      //   503: astore 4
      //   505: aload_1
      //   506: astore 14
      //   508: aload_1
      //   509: astore 15
      //   511: aload 13
      //   513: invokestatic 132	android/text/Html:escapeHtml	(Ljava/lang/CharSequence;)Ljava/lang/String;
      //   516: astore 13
      //   518: aload_1
      //   519: ifnull +11 -> 530
      //   522: aload_1
      //   523: invokevirtual 127	java/io/FileInputStream:close	()V
      //   526: goto +4 -> 530
      //   529: astore_1
      //   530: aload 13
      //   532: areturn
      //   533: astore_1
      //   534: goto +156 -> 690
      //   537: astore_1
      //   538: aload 4
      //   540: astore_3
      //   541: ldc -122
      //   543: ldc -120
      //   545: aload_1
      //   546: invokestatic 142	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   549: pop
      //   550: aload 4
      //   552: astore_3
      //   553: aload_1
      //   554: invokevirtual 143	java/io/IOException:toString	()Ljava/lang/String;
      //   557: invokestatic 132	android/text/Html:escapeHtml	(Ljava/lang/CharSequence;)Ljava/lang/String;
      //   560: astore_1
      //   561: aload 4
      //   563: ifnull +12 -> 575
      //   566: aload 4
      //   568: invokevirtual 127	java/io/FileInputStream:close	()V
      //   571: goto +4 -> 575
      //   574: astore_3
      //   575: aload_1
      //   576: areturn
      //   577: astore_1
      //   578: aload 14
      //   580: ifnull +41 -> 621
      //   583: aload 14
      //   585: invokevirtual 127	java/io/FileInputStream:close	()V
      //   588: goto +33 -> 621
      //   591: astore_1
      //   592: goto -4 -> 588
      //   595: astore_1
      //   596: aload 15
      //   598: astore_3
      //   599: ldc -122
      //   601: ldc -111
      //   603: aload_1
      //   604: invokestatic 142	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   607: pop
      //   608: aload 15
      //   610: ifnull +11 -> 621
      //   613: aload 15
      //   615: invokevirtual 127	java/io/FileInputStream:close	()V
      //   618: goto -30 -> 588
      //   621: aload_0
      //   622: getfield 28	android/content/ClipData$Item:mUri	Landroid/net/Uri;
      //   625: invokevirtual 150	android/net/Uri:getScheme	()Ljava/lang/String;
      //   628: astore_1
      //   629: ldc -104
      //   631: aload_1
      //   632: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   635: ifne +52 -> 687
      //   638: ldc -102
      //   640: aload_1
      //   641: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   644: ifne +43 -> 687
      //   647: ldc -100
      //   649: aload_1
      //   650: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   653: ifeq +6 -> 659
      //   656: goto +31 -> 687
      //   659: iload_2
      //   660: ifeq +15 -> 675
      //   663: aload_0
      //   664: aload_0
      //   665: getfield 28	android/content/ClipData$Item:mUri	Landroid/net/Uri;
      //   668: invokevirtual 157	android/net/Uri:toString	()Ljava/lang/String;
      //   671: invokespecial 161	android/content/ClipData$Item:uriToStyledText	(Ljava/lang/String;)Ljava/lang/CharSequence;
      //   674: areturn
      //   675: aload_0
      //   676: aload_0
      //   677: getfield 28	android/content/ClipData$Item:mUri	Landroid/net/Uri;
      //   680: invokevirtual 157	android/net/Uri:toString	()Ljava/lang/String;
      //   683: invokespecial 165	android/content/ClipData$Item:uriToHtml	(Ljava/lang/String;)Ljava/lang/String;
      //   686: areturn
      //   687: ldc -89
      //   689: areturn
      //   690: aload_3
      //   691: ifnull +11 -> 702
      //   694: aload_3
      //   695: invokevirtual 127	java/io/FileInputStream:close	()V
      //   698: goto +4 -> 702
      //   701: astore_3
      //   702: aload_1
      //   703: athrow
      //   704: aload_0
      //   705: getfield 26	android/content/ClipData$Item:mIntent	Landroid/content/Intent;
      //   708: ifnull +33 -> 741
      //   711: iload_2
      //   712: ifeq +16 -> 728
      //   715: aload_0
      //   716: aload_0
      //   717: getfield 26	android/content/ClipData$Item:mIntent	Landroid/content/Intent;
      //   720: iconst_1
      //   721: invokevirtual 173	android/content/Intent:toUri	(I)Ljava/lang/String;
      //   724: invokespecial 161	android/content/ClipData$Item:uriToStyledText	(Ljava/lang/String;)Ljava/lang/CharSequence;
      //   727: areturn
      //   728: aload_0
      //   729: aload_0
      //   730: getfield 26	android/content/ClipData$Item:mIntent	Landroid/content/Intent;
      //   733: iconst_1
      //   734: invokevirtual 173	android/content/Intent:toUri	(I)Ljava/lang/String;
      //   737: invokespecial 165	android/content/ClipData$Item:uriToHtml	(Ljava/lang/String;)Ljava/lang/String;
      //   740: areturn
      //   741: ldc -89
      //   743: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	744	0	this	Item
      //   0	744	1	paramContext	Context
      //   0	744	2	paramBoolean	boolean
      //   8	423	3	localObject1	Object
      //   432	1	3	localRuntimeException	RuntimeException
      //   449	104	3	localObject2	Object
      //   574	1	3	localIOException1	java.io.IOException
      //   598	97	3	localObject3	Object
      //   701	1	3	localIOException2	java.io.IOException
      //   22	3	4	arrayOfString	String[]
      //   30	1	4	localSecurityException	SecurityException
      //   66	501	4	localObject4	Object
      //   33	345	5	i	int
      //   36	315	6	j	int
      //   44	16	7	k	int
      //   53	53	8	m	int
      //   79	30	9	n	int
      //   126	284	10	localObject5	Object
      //   129	192	11	localInputStreamReader	java.io.InputStreamReader
      //   132	73	12	localObject6	Object
      //   135	396	13	localObject7	Object
      //   146	438	14	localObject8	Object
      //   150	464	15	localObject9	Object
      //   156	53	16	localContentResolver	ContentResolver
      //   177	34	17	localUri	Uri
      // Exception table:
      //   from	to	target	type
      //   9	24	30	java/lang/SecurityException
      //   422	426	429	java/io/IOException
      //   397	404	432	java/lang/RuntimeException
      //   437	441	444	java/io/IOException
      //   470	474	477	java/io/IOException
      //   489	493	496	java/io/IOException
      //   522	526	529	java/io/IOException
      //   152	158	533	finally
      //   173	179	533	finally
      //   208	221	533	finally
      //   232	237	533	finally
      //   248	256	533	finally
      //   267	272	533	finally
      //   283	291	533	finally
      //   302	309	533	finally
      //   320	329	533	finally
      //   345	356	533	finally
      //   370	377	533	finally
      //   397	404	533	finally
      //   459	466	533	finally
      //   511	518	533	finally
      //   541	550	533	finally
      //   553	561	533	finally
      //   599	608	533	finally
      //   152	158	537	java/io/IOException
      //   173	179	537	java/io/IOException
      //   208	221	537	java/io/IOException
      //   232	237	537	java/io/IOException
      //   248	256	537	java/io/IOException
      //   267	272	537	java/io/IOException
      //   283	291	537	java/io/IOException
      //   302	309	537	java/io/IOException
      //   320	329	537	java/io/IOException
      //   345	356	537	java/io/IOException
      //   370	377	537	java/io/IOException
      //   397	404	537	java/io/IOException
      //   459	466	537	java/io/IOException
      //   511	518	537	java/io/IOException
      //   566	571	574	java/io/IOException
      //   152	158	577	java/io/FileNotFoundException
      //   173	179	577	java/io/FileNotFoundException
      //   208	221	577	java/io/FileNotFoundException
      //   232	237	577	java/io/FileNotFoundException
      //   248	256	577	java/io/FileNotFoundException
      //   267	272	577	java/io/FileNotFoundException
      //   283	291	577	java/io/FileNotFoundException
      //   302	309	577	java/io/FileNotFoundException
      //   320	329	577	java/io/FileNotFoundException
      //   345	356	577	java/io/FileNotFoundException
      //   370	377	577	java/io/FileNotFoundException
      //   397	404	577	java/io/FileNotFoundException
      //   459	466	577	java/io/FileNotFoundException
      //   511	518	577	java/io/FileNotFoundException
      //   583	588	591	java/io/IOException
      //   613	618	591	java/io/IOException
      //   152	158	595	java/lang/SecurityException
      //   173	179	595	java/lang/SecurityException
      //   208	221	595	java/lang/SecurityException
      //   232	237	595	java/lang/SecurityException
      //   248	256	595	java/lang/SecurityException
      //   267	272	595	java/lang/SecurityException
      //   283	291	595	java/lang/SecurityException
      //   302	309	595	java/lang/SecurityException
      //   320	329	595	java/lang/SecurityException
      //   345	356	595	java/lang/SecurityException
      //   370	377	595	java/lang/SecurityException
      //   397	404	595	java/lang/SecurityException
      //   459	466	595	java/lang/SecurityException
      //   511	518	595	java/lang/SecurityException
      //   694	698	701	java/io/IOException
    }
    
    private String uriToHtml(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder(256);
      localStringBuilder.append("<a href=\"");
      localStringBuilder.append(Html.escapeHtml(paramString));
      localStringBuilder.append("\">");
      localStringBuilder.append(Html.escapeHtml(paramString));
      localStringBuilder.append("</a>");
      return localStringBuilder.toString();
    }
    
    private CharSequence uriToStyledText(String paramString)
    {
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
      localSpannableStringBuilder.append(paramString);
      localSpannableStringBuilder.setSpan(new URLSpan(paramString), 0, localSpannableStringBuilder.length(), 33);
      return localSpannableStringBuilder;
    }
    
    public String coerceToHtmlText(Context paramContext)
    {
      Object localObject = getHtmlText();
      if (localObject != null) {
        return localObject;
      }
      localObject = getText();
      if (localObject != null)
      {
        if ((localObject instanceof Spanned)) {
          return Html.toHtml((Spanned)localObject);
        }
        return Html.escapeHtml((CharSequence)localObject);
      }
      paramContext = coerceToHtmlOrStyledText(paramContext, false);
      if (paramContext != null) {
        paramContext = paramContext.toString();
      } else {
        paramContext = null;
      }
      return paramContext;
    }
    
    public CharSequence coerceToStyledText(Context paramContext)
    {
      CharSequence localCharSequence = getText();
      if ((localCharSequence instanceof Spanned)) {
        return localCharSequence;
      }
      Object localObject = getHtmlText();
      if (localObject != null) {
        try
        {
          localObject = Html.fromHtml((String)localObject);
          if (localObject != null) {
            return localObject;
          }
        }
        catch (RuntimeException localRuntimeException) {}
      }
      if (localCharSequence != null) {
        return localCharSequence;
      }
      return coerceToHtmlOrStyledText(paramContext, true);
    }
    
    /* Error */
    public CharSequence coerceToText(Context paramContext)
    {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual 208	android/content/ClipData$Item:getText	()Ljava/lang/CharSequence;
      //   4: astore_2
      //   5: aload_2
      //   6: ifnull +5 -> 11
      //   9: aload_2
      //   10: areturn
      //   11: aload_0
      //   12: invokevirtual 226	android/content/ClipData$Item:getUri	()Landroid/net/Uri;
      //   15: astore_3
      //   16: aload_3
      //   17: ifnull +487 -> 504
      //   20: aload_1
      //   21: invokevirtual 58	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   24: astore 4
      //   26: aconst_null
      //   27: astore 5
      //   29: aconst_null
      //   30: astore 6
      //   32: aconst_null
      //   33: astore 7
      //   35: aconst_null
      //   36: astore 8
      //   38: aconst_null
      //   39: astore 9
      //   41: aconst_null
      //   42: astore 10
      //   44: aconst_null
      //   45: astore 11
      //   47: aload 5
      //   49: astore 12
      //   51: aload 9
      //   53: astore_2
      //   54: aload 11
      //   56: astore_1
      //   57: aload 4
      //   59: aload_3
      //   60: ldc 60
      //   62: aconst_null
      //   63: invokevirtual 86	android/content/ContentResolver:openTypedAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/res/AssetFileDescriptor;
      //   66: astore 4
      //   68: aload 4
      //   70: astore 7
      //   72: goto +41 -> 113
      //   75: astore 7
      //   77: goto +411 -> 488
      //   80: astore_1
      //   81: aload 6
      //   83: astore 7
      //   85: goto +28 -> 113
      //   88: astore 6
      //   90: aload 5
      //   92: astore 12
      //   94: aload 9
      //   96: astore_2
      //   97: aload 11
      //   99: astore_1
      //   100: ldc -122
      //   102: ldc -111
      //   104: aload 6
      //   106: invokestatic 142	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   109: pop
      //   110: goto -38 -> 72
      //   113: aload 7
      //   115: ifnull +316 -> 431
      //   118: aload 7
      //   120: astore 12
      //   122: aload 9
      //   124: astore_2
      //   125: aload 11
      //   127: astore_1
      //   128: aload 10
      //   130: astore 9
      //   132: aload 7
      //   134: invokevirtual 92	android/content/res/AssetFileDescriptor:createInputStream	()Ljava/io/FileInputStream;
      //   137: astore 5
      //   139: aload 7
      //   141: astore 12
      //   143: aload 5
      //   145: astore_2
      //   146: aload 11
      //   148: astore_1
      //   149: aload 5
      //   151: astore 8
      //   153: aload 10
      //   155: astore 9
      //   157: new 94	java/io/InputStreamReader
      //   160: astore 6
      //   162: aload 7
      //   164: astore 12
      //   166: aload 5
      //   168: astore_2
      //   169: aload 11
      //   171: astore_1
      //   172: aload 5
      //   174: astore 8
      //   176: aload 10
      //   178: astore 9
      //   180: aload 6
      //   182: aload 5
      //   184: ldc 96
      //   186: invokespecial 99	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
      //   189: aload 6
      //   191: astore 11
      //   193: aload 7
      //   195: astore 12
      //   197: aload 5
      //   199: astore_2
      //   200: aload 11
      //   202: astore_1
      //   203: aload 5
      //   205: astore 8
      //   207: aload 11
      //   209: astore 9
      //   211: new 101	java/lang/StringBuilder
      //   214: astore 10
      //   216: aload 7
      //   218: astore 12
      //   220: aload 5
      //   222: astore_2
      //   223: aload 11
      //   225: astore_1
      //   226: aload 5
      //   228: astore 8
      //   230: aload 11
      //   232: astore 9
      //   234: aload 10
      //   236: sipush 128
      //   239: invokespecial 104	java/lang/StringBuilder:<init>	(I)V
      //   242: aload 7
      //   244: astore 12
      //   246: aload 5
      //   248: astore_2
      //   249: aload 11
      //   251: astore_1
      //   252: aload 5
      //   254: astore 8
      //   256: aload 11
      //   258: astore 9
      //   260: sipush 8192
      //   263: newarray char
      //   265: astore 6
      //   267: aload 7
      //   269: astore 12
      //   271: aload 5
      //   273: astore_2
      //   274: aload 11
      //   276: astore_1
      //   277: aload 5
      //   279: astore 8
      //   281: aload 11
      //   283: astore 9
      //   285: aload 11
      //   287: aload 6
      //   289: invokevirtual 108	java/io/InputStreamReader:read	([C)I
      //   292: istore 13
      //   294: iload 13
      //   296: ifle +35 -> 331
      //   299: aload 7
      //   301: astore 12
      //   303: aload 5
      //   305: astore_2
      //   306: aload 11
      //   308: astore_1
      //   309: aload 5
      //   311: astore 8
      //   313: aload 11
      //   315: astore 9
      //   317: aload 10
      //   319: aload 6
      //   321: iconst_0
      //   322: iload 13
      //   324: invokevirtual 112	java/lang/StringBuilder:append	([CII)Ljava/lang/StringBuilder;
      //   327: pop
      //   328: goto -61 -> 267
      //   331: aload 7
      //   333: astore 12
      //   335: aload 5
      //   337: astore_2
      //   338: aload 11
      //   340: astore_1
      //   341: aload 5
      //   343: astore 8
      //   345: aload 11
      //   347: astore 9
      //   349: aload 10
      //   351: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   354: astore 10
      //   356: aload 7
      //   358: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   361: aload 5
      //   363: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   366: aload 11
      //   368: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   371: aload 10
      //   373: areturn
      //   374: astore 11
      //   376: aload 7
      //   378: astore 12
      //   380: aload 8
      //   382: astore_2
      //   383: aload 9
      //   385: astore_1
      //   386: ldc -122
      //   388: ldc -120
      //   390: aload 11
      //   392: invokestatic 142	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   395: pop
      //   396: aload 7
      //   398: astore 12
      //   400: aload 8
      //   402: astore_2
      //   403: aload 9
      //   405: astore_1
      //   406: aload 11
      //   408: invokevirtual 143	java/io/IOException:toString	()Ljava/lang/String;
      //   411: astore 11
      //   413: aload 7
      //   415: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   418: aload 8
      //   420: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   423: aload 9
      //   425: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   428: aload 11
      //   430: areturn
      //   431: aload 7
      //   433: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   436: aconst_null
      //   437: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   440: aload 11
      //   442: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   445: aload_3
      //   446: invokevirtual 150	android/net/Uri:getScheme	()Ljava/lang/String;
      //   449: astore_1
      //   450: ldc -104
      //   452: aload_1
      //   453: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   456: ifne +29 -> 485
      //   459: ldc -102
      //   461: aload_1
      //   462: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   465: ifne +20 -> 485
      //   468: ldc -100
      //   470: aload_1
      //   471: invokevirtual 74	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   474: ifeq +6 -> 480
      //   477: goto +8 -> 485
      //   480: aload_3
      //   481: invokevirtual 157	android/net/Uri:toString	()Ljava/lang/String;
      //   484: areturn
      //   485: ldc -89
      //   487: areturn
      //   488: aload 12
      //   490: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   493: aload_2
      //   494: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   497: aload_1
      //   498: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   501: aload 7
      //   503: athrow
      //   504: aload_0
      //   505: invokevirtual 236	android/content/ClipData$Item:getIntent	()Landroid/content/Intent;
      //   508: astore_1
      //   509: aload_1
      //   510: ifnull +9 -> 519
      //   513: aload_1
      //   514: iconst_1
      //   515: invokevirtual 173	android/content/Intent:toUri	(I)Ljava/lang/String;
      //   518: areturn
      //   519: ldc -89
      //   521: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	522	0	this	Item
      //   0	522	1	paramContext	Context
      //   4	490	2	localObject1	Object
      //   15	466	3	localUri	Uri
      //   24	45	4	localObject2	Object
      //   27	335	5	localFileInputStream1	java.io.FileInputStream
      //   30	52	6	localObject3	Object
      //   88	17	6	localSecurityException	SecurityException
      //   160	160	6	localObject4	Object
      //   33	38	7	localObject5	Object
      //   75	1	7	localObject6	Object
      //   83	419	7	localObject7	Object
      //   36	383	8	localFileInputStream2	java.io.FileInputStream
      //   39	385	9	localObject8	Object
      //   42	330	10	localObject9	Object
      //   45	322	11	localObject10	Object
      //   374	33	11	localIOException	java.io.IOException
      //   411	30	11	str	String
      //   49	440	12	localObject11	Object
      //   292	31	13	i	int
      // Exception table:
      //   from	to	target	type
      //   57	68	75	finally
      //   100	110	75	finally
      //   132	139	75	finally
      //   157	162	75	finally
      //   180	189	75	finally
      //   211	216	75	finally
      //   234	242	75	finally
      //   260	267	75	finally
      //   285	294	75	finally
      //   317	328	75	finally
      //   349	356	75	finally
      //   386	396	75	finally
      //   406	413	75	finally
      //   57	68	80	java/io/FileNotFoundException
      //   57	68	80	java/lang/RuntimeException
      //   57	68	88	java/lang/SecurityException
      //   132	139	374	java/io/IOException
      //   157	162	374	java/io/IOException
      //   180	189	374	java/io/IOException
      //   211	216	374	java/io/IOException
      //   234	242	374	java/io/IOException
      //   260	267	374	java/io/IOException
      //   285	294	374	java/io/IOException
      //   317	328	374	java/io/IOException
      //   349	356	374	java/io/IOException
    }
    
    public String getHtmlText()
    {
      return mHtmlText;
    }
    
    public Intent getIntent()
    {
      return mIntent;
    }
    
    public CharSequence getText()
    {
      return mText;
    }
    
    public Uri getUri()
    {
      return mUri;
    }
    
    public void toShortString(StringBuilder paramStringBuilder)
    {
      if (mHtmlText != null)
      {
        paramStringBuilder.append("H:");
        paramStringBuilder.append(mHtmlText);
      }
      else if (mText != null)
      {
        paramStringBuilder.append("T:");
        paramStringBuilder.append(mText);
      }
      else if (mUri != null)
      {
        paramStringBuilder.append("U:");
        paramStringBuilder.append(mUri);
      }
      else if (mIntent != null)
      {
        paramStringBuilder.append("I:");
        mIntent.toShortString(paramStringBuilder, true, true, true, true);
      }
      else
      {
        paramStringBuilder.append("NULL");
      }
    }
    
    public void toShortSummaryString(StringBuilder paramStringBuilder)
    {
      if (mHtmlText != null)
      {
        paramStringBuilder.append("HTML");
      }
      else if (mText != null)
      {
        paramStringBuilder.append("TEXT");
      }
      else if (mUri != null)
      {
        paramStringBuilder.append("U:");
        paramStringBuilder.append(mUri);
      }
      else if (mIntent != null)
      {
        paramStringBuilder.append("I:");
        mIntent.toShortString(paramStringBuilder, true, true, true, true);
      }
      else
      {
        paramStringBuilder.append("NULL");
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("ClipData.Item { ");
      toShortString(localStringBuilder);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
    
    public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
    {
      paramLong = paramProtoOutputStream.start(paramLong);
      if (mHtmlText != null) {
        paramProtoOutputStream.write(1138166333441L, mHtmlText);
      } else if (mText != null) {
        paramProtoOutputStream.write(1138166333442L, mText.toString());
      } else if (mUri != null) {
        paramProtoOutputStream.write(1138166333443L, mUri.toString());
      } else if (mIntent != null) {
        mIntent.writeToProto(paramProtoOutputStream, 1146756268036L, true, true, true, true);
      } else {
        paramProtoOutputStream.write(1133871366149L, true);
      }
      paramProtoOutputStream.end(paramLong);
    }
  }
}
