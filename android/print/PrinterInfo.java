package android.print;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrinterInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PrinterInfo> CREATOR = new Parcelable.Creator()
  {
    public PrinterInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PrinterInfo(paramAnonymousParcel, null);
    }
    
    public PrinterInfo[] newArray(int paramAnonymousInt)
    {
      return new PrinterInfo[paramAnonymousInt];
    }
  };
  public static final int STATUS_BUSY = 2;
  public static final int STATUS_IDLE = 1;
  public static final int STATUS_UNAVAILABLE = 3;
  private final PrinterCapabilitiesInfo mCapabilities;
  private final int mCustomPrinterIconGen;
  private final String mDescription;
  private final boolean mHasCustomPrinterIcon;
  private final int mIconResourceId;
  private final PrinterId mId;
  private final PendingIntent mInfoIntent;
  private final String mName;
  private final int mStatus;
  
  private PrinterInfo(Parcel paramParcel)
  {
    mId = checkPrinterId((PrinterId)paramParcel.readParcelable(null));
    mName = checkName(paramParcel.readString());
    mStatus = checkStatus(paramParcel.readInt());
    mDescription = paramParcel.readString();
    mCapabilities = ((PrinterCapabilitiesInfo)paramParcel.readParcelable(null));
    mIconResourceId = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readByte() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mHasCustomPrinterIcon = bool;
    mCustomPrinterIconGen = paramParcel.readInt();
    mInfoIntent = ((PendingIntent)paramParcel.readParcelable(null));
  }
  
  private PrinterInfo(PrinterId paramPrinterId, String paramString1, int paramInt1, int paramInt2, boolean paramBoolean, String paramString2, PendingIntent paramPendingIntent, PrinterCapabilitiesInfo paramPrinterCapabilitiesInfo, int paramInt3)
  {
    mId = paramPrinterId;
    mName = paramString1;
    mStatus = paramInt1;
    mIconResourceId = paramInt2;
    mHasCustomPrinterIcon = paramBoolean;
    mDescription = paramString2;
    mInfoIntent = paramPendingIntent;
    mCapabilities = paramPrinterCapabilitiesInfo;
    mCustomPrinterIconGen = paramInt3;
  }
  
  private static String checkName(String paramString)
  {
    return (String)Preconditions.checkStringNotEmpty(paramString, "name cannot be empty.");
  }
  
  private static PrinterId checkPrinterId(PrinterId paramPrinterId)
  {
    return (PrinterId)Preconditions.checkNotNull(paramPrinterId, "printerId cannot be null.");
  }
  
  private static int checkStatus(int paramInt)
  {
    if ((paramInt != 1) && (paramInt != 2) && (paramInt != 3)) {
      throw new IllegalArgumentException("status is invalid.");
    }
    return paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (PrinterInfo)paramObject;
    if (!equalsIgnoringStatus(paramObject)) {
      return false;
    }
    return mStatus == mStatus;
  }
  
  public boolean equalsIgnoringStatus(PrinterInfo paramPrinterInfo)
  {
    if (!mId.equals(mId)) {
      return false;
    }
    if (!mName.equals(mName)) {
      return false;
    }
    if (!TextUtils.equals(mDescription, mDescription)) {
      return false;
    }
    if (mCapabilities == null)
    {
      if (mCapabilities != null) {
        return false;
      }
    }
    else if (!mCapabilities.equals(mCapabilities)) {
      return false;
    }
    if (mIconResourceId != mIconResourceId) {
      return false;
    }
    if (mHasCustomPrinterIcon != mHasCustomPrinterIcon) {
      return false;
    }
    if (mCustomPrinterIconGen != mCustomPrinterIconGen) {
      return false;
    }
    if (mInfoIntent == null)
    {
      if (mInfoIntent != null) {
        return false;
      }
    }
    else if (!mInfoIntent.equals(mInfoIntent)) {
      return false;
    }
    return true;
  }
  
  public PrinterCapabilitiesInfo getCapabilities()
  {
    return mCapabilities;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public boolean getHasCustomPrinterIcon()
  {
    return mHasCustomPrinterIcon;
  }
  
  public PrinterId getId()
  {
    return mId;
  }
  
  public PendingIntent getInfoIntent()
  {
    return mInfoIntent;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public int hashCode()
  {
    int i = mId.hashCode();
    int j = mName.hashCode();
    int k = mStatus;
    String str = mDescription;
    int m = 0;
    int n;
    if (str != null) {
      n = mDescription.hashCode();
    } else {
      n = 0;
    }
    int i1;
    if (mCapabilities != null) {
      i1 = mCapabilities.hashCode();
    } else {
      i1 = 0;
    }
    int i2 = mIconResourceId;
    int i3 = mHasCustomPrinterIcon;
    int i4 = mCustomPrinterIconGen;
    if (mInfoIntent != null) {
      m = mInfoIntent.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 1 + i) + j) + k) + n) + i1) + i2) + i3) + i4) + m;
  }
  
  public Drawable loadIcon(Context paramContext)
  {
    Object localObject1 = null;
    PackageManager localPackageManager = paramContext.getPackageManager();
    Object localObject2 = localObject1;
    Object localObject3;
    if (mHasCustomPrinterIcon)
    {
      localObject3 = ((PrintManager)paramContext.getSystemService("print")).getCustomPrinterIcon(mId);
      localObject2 = localObject1;
      if (localObject3 != null) {
        localObject2 = ((Icon)localObject3).loadDrawable(paramContext);
      }
    }
    localObject1 = localObject2;
    if (localObject2 == null)
    {
      localObject1 = localObject2;
      try
      {
        String str = mId.getServiceName().getPackageName();
        localObject1 = localObject2;
        localObject3 = getPackageInfo0applicationInfo;
        paramContext = (Context)localObject2;
        localObject1 = localObject2;
        if (mIconResourceId != 0)
        {
          localObject1 = localObject2;
          paramContext = localPackageManager.getDrawable(str, mIconResourceId, (ApplicationInfo)localObject3);
        }
        localObject2 = paramContext;
        if (paramContext == null)
        {
          localObject1 = paramContext;
          localObject2 = ((ApplicationInfo)localObject3).loadIcon(localPackageManager);
        }
        localObject1 = localObject2;
      }
      catch (PackageManager.NameNotFoundException paramContext) {}
    }
    return localObject1;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PrinterInfo{");
    localStringBuilder.append("id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", name=");
    localStringBuilder.append(mName);
    localStringBuilder.append(", status=");
    localStringBuilder.append(mStatus);
    localStringBuilder.append(", description=");
    localStringBuilder.append(mDescription);
    localStringBuilder.append(", capabilities=");
    localStringBuilder.append(mCapabilities);
    localStringBuilder.append(", iconResId=");
    localStringBuilder.append(mIconResourceId);
    localStringBuilder.append(", hasCustomPrinterIcon=");
    localStringBuilder.append(mHasCustomPrinterIcon);
    localStringBuilder.append(", customPrinterIconGen=");
    localStringBuilder.append(mCustomPrinterIconGen);
    localStringBuilder.append(", infoIntent=");
    localStringBuilder.append(mInfoIntent);
    localStringBuilder.append("\"}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mId, paramInt);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mStatus);
    paramParcel.writeString(mDescription);
    paramParcel.writeParcelable(mCapabilities, paramInt);
    paramParcel.writeInt(mIconResourceId);
    paramParcel.writeByte((byte)mHasCustomPrinterIcon);
    paramParcel.writeInt(mCustomPrinterIconGen);
    paramParcel.writeParcelable(mInfoIntent, paramInt);
  }
  
  public static final class Builder
  {
    private PrinterCapabilitiesInfo mCapabilities;
    private int mCustomPrinterIconGen;
    private String mDescription;
    private boolean mHasCustomPrinterIcon;
    private int mIconResourceId;
    private PendingIntent mInfoIntent;
    private String mName;
    private PrinterId mPrinterId;
    private int mStatus;
    
    public Builder(PrinterId paramPrinterId, String paramString, int paramInt)
    {
      mPrinterId = PrinterInfo.checkPrinterId(paramPrinterId);
      mName = PrinterInfo.checkName(paramString);
      mStatus = PrinterInfo.checkStatus(paramInt);
    }
    
    public Builder(PrinterInfo paramPrinterInfo)
    {
      mPrinterId = mId;
      mName = mName;
      mStatus = mStatus;
      mIconResourceId = mIconResourceId;
      mHasCustomPrinterIcon = mHasCustomPrinterIcon;
      mDescription = mDescription;
      mInfoIntent = mInfoIntent;
      mCapabilities = mCapabilities;
      mCustomPrinterIconGen = mCustomPrinterIconGen;
    }
    
    public PrinterInfo build()
    {
      return new PrinterInfo(mPrinterId, mName, mStatus, mIconResourceId, mHasCustomPrinterIcon, mDescription, mInfoIntent, mCapabilities, mCustomPrinterIconGen, null);
    }
    
    public Builder incCustomPrinterIconGen()
    {
      mCustomPrinterIconGen += 1;
      return this;
    }
    
    public Builder setCapabilities(PrinterCapabilitiesInfo paramPrinterCapabilitiesInfo)
    {
      mCapabilities = paramPrinterCapabilitiesInfo;
      return this;
    }
    
    public Builder setDescription(String paramString)
    {
      mDescription = paramString;
      return this;
    }
    
    public Builder setHasCustomPrinterIcon(boolean paramBoolean)
    {
      mHasCustomPrinterIcon = paramBoolean;
      return this;
    }
    
    public Builder setIconResourceId(int paramInt)
    {
      mIconResourceId = Preconditions.checkArgumentNonnegative(paramInt, "iconResourceId can't be negative");
      return this;
    }
    
    public Builder setInfoIntent(PendingIntent paramPendingIntent)
    {
      mInfoIntent = paramPendingIntent;
      return this;
    }
    
    public Builder setName(String paramString)
    {
      mName = PrinterInfo.checkName(paramString);
      return this;
    }
    
    public Builder setStatus(int paramInt)
    {
      mStatus = PrinterInfo.checkStatus(paramInt);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Status {}
}
