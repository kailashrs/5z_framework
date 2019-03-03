package android.content.pm;

import android.annotation.SystemApi;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Printer;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.Collator;
import java.util.BitSet;
import java.util.Comparator;

public class PackageItemInfo
{
  public static final int DUMP_FLAG_ALL = 3;
  public static final int DUMP_FLAG_APPLICATION = 2;
  public static final int DUMP_FLAG_DETAILS = 1;
  private static final int LINE_FEED_CODE_POINT = 10;
  private static final float MAX_LABEL_SIZE_PX = 500.0F;
  private static final int MAX_SAFE_LABEL_LENGTH = 50000;
  private static final int NBSP_CODE_POINT = 160;
  public static final int SAFE_LABEL_FLAG_FIRST_LINE = 4;
  public static final int SAFE_LABEL_FLAG_SINGLE_LINE = 2;
  public static final int SAFE_LABEL_FLAG_TRIM = 1;
  private static volatile boolean sForceSafeLabels = false;
  public int banner;
  public int icon;
  public int labelRes;
  public int logo;
  public Bundle metaData;
  public String name;
  public CharSequence nonLocalizedLabel;
  public String packageName;
  public int showUserIcon;
  
  public PackageItemInfo()
  {
    showUserIcon = 55536;
  }
  
  public PackageItemInfo(PackageItemInfo paramPackageItemInfo)
  {
    name = name;
    if (name != null) {
      name = name.trim();
    }
    packageName = packageName;
    labelRes = labelRes;
    nonLocalizedLabel = nonLocalizedLabel;
    if (nonLocalizedLabel != null) {
      nonLocalizedLabel = nonLocalizedLabel.toString().trim();
    }
    icon = icon;
    banner = banner;
    logo = logo;
    metaData = metaData;
    showUserIcon = showUserIcon;
  }
  
  protected PackageItemInfo(Parcel paramParcel)
  {
    name = paramParcel.readString();
    packageName = paramParcel.readString();
    labelRes = paramParcel.readInt();
    nonLocalizedLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    icon = paramParcel.readInt();
    logo = paramParcel.readInt();
    metaData = paramParcel.readBundle();
    banner = paramParcel.readInt();
    showUserIcon = paramParcel.readInt();
  }
  
  private static boolean isNewline(int paramInt)
  {
    int i = Character.getType(paramInt);
    boolean bool;
    if ((i != 14) && (i != 13) && (paramInt != 10)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isWhiteSpace(int paramInt)
  {
    boolean bool;
    if ((!Character.isWhitespace(paramInt)) && (paramInt != 160)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static void setForceSafeLabels(boolean paramBoolean)
  {
    sForceSafeLabels = paramBoolean;
  }
  
  protected void dumpBack(Printer paramPrinter, String paramString) {}
  
  protected void dumpFront(Printer paramPrinter, String paramString)
  {
    if (name != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("name=");
      localStringBuilder.append(name);
      paramPrinter.println(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("packageName=");
    localStringBuilder.append(packageName);
    paramPrinter.println(localStringBuilder.toString());
    if ((labelRes != 0) || (nonLocalizedLabel != null) || (icon != 0) || (banner != 0))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("labelRes=0x");
      localStringBuilder.append(Integer.toHexString(labelRes));
      localStringBuilder.append(" nonLocalizedLabel=");
      localStringBuilder.append(nonLocalizedLabel);
      localStringBuilder.append(" icon=0x");
      localStringBuilder.append(Integer.toHexString(icon));
      localStringBuilder.append(" banner=0x");
      localStringBuilder.append(Integer.toHexString(banner));
      paramPrinter.println(localStringBuilder.toString());
    }
  }
  
  protected ApplicationInfo getApplicationInfo()
  {
    return null;
  }
  
  public Drawable loadBanner(PackageManager paramPackageManager)
  {
    if (banner != 0)
    {
      Drawable localDrawable = paramPackageManager.getDrawable(packageName, banner, getApplicationInfo());
      if (localDrawable != null) {
        return localDrawable;
      }
    }
    return loadDefaultBanner(paramPackageManager);
  }
  
  protected Drawable loadDefaultBanner(PackageManager paramPackageManager)
  {
    return null;
  }
  
  public Drawable loadDefaultIcon(PackageManager paramPackageManager)
  {
    return paramPackageManager.getDefaultActivityIcon();
  }
  
  protected Drawable loadDefaultLogo(PackageManager paramPackageManager)
  {
    return null;
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    return paramPackageManager.loadItemIcon(this, getApplicationInfo());
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    if (sForceSafeLabels) {
      return loadSafeLabel(paramPackageManager);
    }
    return loadUnsafeLabel(paramPackageManager);
  }
  
  public Drawable loadLogo(PackageManager paramPackageManager)
  {
    if (logo != 0)
    {
      Drawable localDrawable = paramPackageManager.getDrawable(packageName, logo, getApplicationInfo());
      if (localDrawable != null) {
        return localDrawable;
      }
    }
    return loadDefaultLogo(paramPackageManager);
  }
  
  @SystemApi
  public CharSequence loadSafeLabel(PackageManager paramPackageManager)
  {
    String str = Html.fromHtml(loadUnsafeLabel(paramPackageManager).toString()).toString();
    int i = Math.min(str.length(), 50000);
    paramPackageManager = new StringBuffer(i);
    int j = 0;
    while (j < i)
    {
      int k = str.codePointAt(j);
      int m = Character.getType(k);
      if ((m != 13) && (m != 15) && (m != 14))
      {
        k = Character.charCount(k);
        if (m == 12)
        {
          paramPackageManager.append(' ');
        }
        else
        {
          paramPackageManager.append(str.charAt(j));
          if (k == 2) {
            paramPackageManager.append(str.charAt(j + 1));
          }
        }
        j += k;
      }
      else
      {
        str.substring(0, j);
      }
    }
    str = paramPackageManager.toString().trim();
    if (str.isEmpty()) {
      return packageName;
    }
    paramPackageManager = new TextPaint();
    paramPackageManager.setTextSize(42.0F);
    return TextUtils.ellipsize(str, paramPackageManager, 500.0F, TextUtils.TruncateAt.END);
  }
  
  public CharSequence loadSafeLabel(PackageManager paramPackageManager, float paramFloat, int paramInt)
  {
    boolean bool1 = true;
    int i;
    if ((paramInt & 0x4) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if ((paramInt & 0x2) != 0) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if ((paramInt & 0x1) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    Preconditions.checkNotNull(paramPackageManager);
    if (paramFloat >= 0.0F) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2);
    Preconditions.checkFlagsArgument(paramInt, 7);
    boolean bool2 = bool1;
    if (i != 0) {
      if (j == 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    Preconditions.checkArgument(bool2, "Cannot set SAFE_LABEL_FLAG_SINGLE_LINE and SAFE_LABEL_FLAG_FIRST_LINE at the same time");
    StringWithRemovedChars localStringWithRemovedChars = new StringWithRemovedChars(Html.fromHtml(loadUnsafeLabel(paramPackageManager).toString()).toString());
    int m = localStringWithRemovedChars.length();
    int n = -1;
    int i1 = -1;
    paramInt = 0;
    while (paramInt < m)
    {
      int i2 = localStringWithRemovedChars.codePointAt(paramInt);
      int i3 = Character.getType(i2);
      int i4 = Character.charCount(i2);
      bool2 = isNewline(i2);
      if ((paramInt <= 50000) && ((i == 0) || (!bool2)))
      {
        int i5;
        if ((j != 0) && (bool2))
        {
          localStringWithRemovedChars.removeRange(paramInt, paramInt + i4);
          i5 = i1;
          i3 = n;
        }
        else if ((i3 == 15) && (!bool2))
        {
          localStringWithRemovedChars.removeRange(paramInt, paramInt + i4);
          i5 = i1;
          i3 = n;
        }
        else
        {
          i5 = i1;
          i3 = n;
          if (k != 0)
          {
            i5 = i1;
            i3 = n;
            if (!isWhiteSpace(i2))
            {
              n = i1;
              if (i1 == -1) {
                n = paramInt;
              }
              i3 = paramInt + i4;
              i5 = n;
            }
          }
        }
        paramInt += i4;
        i1 = i5;
        n = i3;
      }
      else
      {
        localStringWithRemovedChars.removeAllCharAfter(paramInt);
      }
    }
    if (k != 0) {
      if (i1 == -1)
      {
        localStringWithRemovedChars.removeAllCharAfter(0);
      }
      else
      {
        if (i1 > 0) {
          localStringWithRemovedChars.removeAllCharBefore(i1);
        }
        if (n < m) {
          localStringWithRemovedChars.removeAllCharAfter(n);
        }
      }
    }
    if (paramFloat == 0.0F) {
      return localStringWithRemovedChars.toString();
    }
    paramPackageManager = new TextPaint();
    paramPackageManager.setTextSize(42.0F);
    return TextUtils.ellipsize(localStringWithRemovedChars.toString(), paramPackageManager, paramFloat, TextUtils.TruncateAt.END);
  }
  
  public Drawable loadUnbadgedIcon(PackageManager paramPackageManager)
  {
    return paramPackageManager.loadUnbadgedItemIcon(this, getApplicationInfo());
  }
  
  public CharSequence loadUnsafeLabel(PackageManager paramPackageManager)
  {
    if (nonLocalizedLabel != null) {
      return nonLocalizedLabel;
    }
    if (labelRes != 0)
    {
      paramPackageManager = paramPackageManager.getText(packageName, labelRes, getApplicationInfo());
      if (paramPackageManager != null) {
        return paramPackageManager.toString().trim();
      }
    }
    if (name != null) {
      return name;
    }
    return packageName;
  }
  
  public XmlResourceParser loadXmlMetaData(PackageManager paramPackageManager, String paramString)
  {
    if (metaData != null)
    {
      int i = metaData.getInt(paramString);
      if (i != 0) {
        return paramPackageManager.getXml(packageName, i, getApplicationInfo());
      }
    }
    return null;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(name);
    paramParcel.writeString(packageName);
    paramParcel.writeInt(labelRes);
    TextUtils.writeToParcel(nonLocalizedLabel, paramParcel, paramInt);
    paramParcel.writeInt(icon);
    paramParcel.writeInt(logo);
    paramParcel.writeBundle(metaData);
    paramParcel.writeInt(banner);
    paramParcel.writeInt(showUserIcon);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    if (name != null) {
      paramProtoOutputStream.write(1138166333441L, name);
    }
    paramProtoOutputStream.write(1138166333442L, packageName);
    if ((labelRes != 0) || (nonLocalizedLabel != null) || (icon != 0) || (banner != 0))
    {
      paramProtoOutputStream.write(1120986464259L, labelRes);
      paramProtoOutputStream.write(1138166333444L, nonLocalizedLabel.toString());
      paramProtoOutputStream.write(1120986464261L, icon);
      paramProtoOutputStream.write(1120986464262L, banner);
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  public static class DisplayNameComparator
    implements Comparator<PackageItemInfo>
  {
    private PackageManager mPM;
    private final Collator sCollator = Collator.getInstance();
    
    public DisplayNameComparator(PackageManager paramPackageManager)
    {
      mPM = paramPackageManager;
    }
    
    public final int compare(PackageItemInfo paramPackageItemInfo1, PackageItemInfo paramPackageItemInfo2)
    {
      CharSequence localCharSequence = paramPackageItemInfo1.loadLabel(mPM);
      Object localObject = localCharSequence;
      if (localCharSequence == null) {
        localObject = name;
      }
      localCharSequence = paramPackageItemInfo2.loadLabel(mPM);
      paramPackageItemInfo1 = localCharSequence;
      if (localCharSequence == null) {
        paramPackageItemInfo1 = name;
      }
      return sCollator.compare(((CharSequence)localObject).toString(), paramPackageItemInfo1.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SafeLabelFlags {}
  
  private static class StringWithRemovedChars
  {
    private final String mOriginal;
    private BitSet mRemovedChars;
    
    StringWithRemovedChars(String paramString)
    {
      mOriginal = paramString;
    }
    
    int codePointAt(int paramInt)
    {
      return mOriginal.codePointAt(paramInt);
    }
    
    boolean isRemoved(int paramInt)
    {
      boolean bool;
      if ((mRemovedChars != null) && (mRemovedChars.get(paramInt))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    int length()
    {
      return mOriginal.length();
    }
    
    void removeAllCharAfter(int paramInt)
    {
      if (mRemovedChars == null) {
        mRemovedChars = new BitSet(mOriginal.length());
      }
      mRemovedChars.set(paramInt, mOriginal.length());
    }
    
    void removeAllCharBefore(int paramInt)
    {
      if (mRemovedChars == null) {
        mRemovedChars = new BitSet(mOriginal.length());
      }
      mRemovedChars.set(0, paramInt);
    }
    
    void removeRange(int paramInt1, int paramInt2)
    {
      if (mRemovedChars == null) {
        mRemovedChars = new BitSet(mOriginal.length());
      }
      mRemovedChars.set(paramInt1, paramInt2);
    }
    
    public String toString()
    {
      if (mRemovedChars == null) {
        return mOriginal;
      }
      StringBuilder localStringBuilder = new StringBuilder(mOriginal.length());
      for (int i = 0; i < mOriginal.length(); i++) {
        if (!mRemovedChars.get(i)) {
          localStringBuilder.append(mOriginal.charAt(i));
        }
      }
      return localStringBuilder.toString();
    }
  }
}
