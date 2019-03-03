package android.print;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class PrintJobInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PrintJobInfo> CREATOR = new Parcelable.Creator()
  {
    public PrintJobInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PrintJobInfo(paramAnonymousParcel, null);
    }
    
    public PrintJobInfo[] newArray(int paramAnonymousInt)
    {
      return new PrintJobInfo[paramAnonymousInt];
    }
  };
  public static final int STATE_ANY = -1;
  public static final int STATE_ANY_ACTIVE = -3;
  public static final int STATE_ANY_SCHEDULED = -4;
  public static final int STATE_ANY_VISIBLE_TO_CLIENTS = -2;
  public static final int STATE_BLOCKED = 4;
  public static final int STATE_CANCELED = 7;
  public static final int STATE_COMPLETED = 5;
  public static final int STATE_CREATED = 1;
  public static final int STATE_FAILED = 6;
  public static final int STATE_QUEUED = 2;
  public static final int STATE_STARTED = 3;
  private Bundle mAdvancedOptions;
  private int mAppId;
  private PrintAttributes mAttributes;
  private boolean mCanceling;
  private int mCopies;
  private long mCreationTime;
  private PrintDocumentInfo mDocumentInfo;
  private PrintJobId mId;
  private String mLabel;
  private PageRange[] mPageRanges;
  private PrinterId mPrinterId;
  private String mPrinterName;
  private float mProgress;
  private int mState;
  private CharSequence mStatus;
  private int mStatusRes;
  private CharSequence mStatusResAppPackageName;
  private String mTag;
  
  public PrintJobInfo()
  {
    mProgress = -1.0F;
  }
  
  private PrintJobInfo(Parcel paramParcel)
  {
    mId = ((PrintJobId)paramParcel.readParcelable(null));
    mLabel = paramParcel.readString();
    mPrinterId = ((PrinterId)paramParcel.readParcelable(null));
    mPrinterName = paramParcel.readString();
    mState = paramParcel.readInt();
    mAppId = paramParcel.readInt();
    mTag = paramParcel.readString();
    mCreationTime = paramParcel.readLong();
    mCopies = paramParcel.readInt();
    Parcelable[] arrayOfParcelable = paramParcel.readParcelableArray(null);
    boolean bool = false;
    if (arrayOfParcelable != null)
    {
      mPageRanges = new PageRange[arrayOfParcelable.length];
      for (int i = 0; i < arrayOfParcelable.length; i++) {
        mPageRanges[i] = ((PageRange)arrayOfParcelable[i]);
      }
    }
    mAttributes = ((PrintAttributes)paramParcel.readParcelable(null));
    mDocumentInfo = ((PrintDocumentInfo)paramParcel.readParcelable(null));
    mProgress = paramParcel.readFloat();
    mStatus = paramParcel.readCharSequence();
    mStatusRes = paramParcel.readInt();
    mStatusResAppPackageName = paramParcel.readCharSequence();
    if (paramParcel.readInt() == 1) {
      bool = true;
    }
    mCanceling = bool;
    mAdvancedOptions = paramParcel.readBundle();
    if (mAdvancedOptions != null) {
      Preconditions.checkArgument(mAdvancedOptions.containsKey(null) ^ true);
    }
  }
  
  public PrintJobInfo(PrintJobInfo paramPrintJobInfo)
  {
    mId = mId;
    mLabel = mLabel;
    mPrinterId = mPrinterId;
    mPrinterName = mPrinterName;
    mState = mState;
    mAppId = mAppId;
    mTag = mTag;
    mCreationTime = mCreationTime;
    mCopies = mCopies;
    mPageRanges = mPageRanges;
    mAttributes = mAttributes;
    mDocumentInfo = mDocumentInfo;
    mProgress = mProgress;
    mStatus = mStatus;
    mStatusRes = mStatusRes;
    mStatusResAppPackageName = mStatusResAppPackageName;
    mCanceling = mCanceling;
    mAdvancedOptions = mAdvancedOptions;
  }
  
  public static String stateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "STATE_UNKNOWN";
    case 7: 
      return "STATE_CANCELED";
    case 6: 
      return "STATE_FAILED";
    case 5: 
      return "STATE_COMPLETED";
    case 4: 
      return "STATE_BLOCKED";
    case 3: 
      return "STATE_STARTED";
    case 2: 
      return "STATE_QUEUED";
    }
    return "STATE_CREATED";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAdvancedIntOption(String paramString)
  {
    if (mAdvancedOptions != null) {
      return mAdvancedOptions.getInt(paramString);
    }
    return 0;
  }
  
  public Bundle getAdvancedOptions()
  {
    return mAdvancedOptions;
  }
  
  public String getAdvancedStringOption(String paramString)
  {
    if (mAdvancedOptions != null) {
      return mAdvancedOptions.getString(paramString);
    }
    return null;
  }
  
  public int getAppId()
  {
    return mAppId;
  }
  
  public PrintAttributes getAttributes()
  {
    return mAttributes;
  }
  
  public int getCopies()
  {
    return mCopies;
  }
  
  public long getCreationTime()
  {
    return mCreationTime;
  }
  
  public PrintDocumentInfo getDocumentInfo()
  {
    return mDocumentInfo;
  }
  
  public PrintJobId getId()
  {
    return mId;
  }
  
  public String getLabel()
  {
    return mLabel;
  }
  
  public PageRange[] getPages()
  {
    return mPageRanges;
  }
  
  public PrinterId getPrinterId()
  {
    return mPrinterId;
  }
  
  public String getPrinterName()
  {
    return mPrinterName;
  }
  
  public float getProgress()
  {
    return mProgress;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public CharSequence getStatus(PackageManager paramPackageManager)
  {
    if (mStatusRes == 0) {
      return mStatus;
    }
    try
    {
      paramPackageManager = paramPackageManager.getResourcesForApplication(mStatusResAppPackageName.toString()).getString(mStatusRes);
      return paramPackageManager;
    }
    catch (PackageManager.NameNotFoundException|Resources.NotFoundException paramPackageManager) {}
    return null;
  }
  
  public String getTag()
  {
    return mTag;
  }
  
  public boolean hasAdvancedOption(String paramString)
  {
    boolean bool;
    if ((mAdvancedOptions != null) && (mAdvancedOptions.containsKey(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCancelling()
  {
    return mCanceling;
  }
  
  public void setAdvancedOptions(Bundle paramBundle)
  {
    mAdvancedOptions = paramBundle;
  }
  
  public void setAppId(int paramInt)
  {
    mAppId = paramInt;
  }
  
  public void setAttributes(PrintAttributes paramPrintAttributes)
  {
    mAttributes = paramPrintAttributes;
  }
  
  public void setCancelling(boolean paramBoolean)
  {
    mCanceling = paramBoolean;
  }
  
  public void setCopies(int paramInt)
  {
    if (paramInt >= 1)
    {
      mCopies = paramInt;
      return;
    }
    throw new IllegalArgumentException("Copies must be more than one.");
  }
  
  public void setCreationTime(long paramLong)
  {
    if (paramLong >= 0L)
    {
      mCreationTime = paramLong;
      return;
    }
    throw new IllegalArgumentException("creationTime must be non-negative.");
  }
  
  public void setDocumentInfo(PrintDocumentInfo paramPrintDocumentInfo)
  {
    mDocumentInfo = paramPrintDocumentInfo;
  }
  
  public void setId(PrintJobId paramPrintJobId)
  {
    mId = paramPrintJobId;
  }
  
  public void setLabel(String paramString)
  {
    mLabel = paramString;
  }
  
  public void setPages(PageRange[] paramArrayOfPageRange)
  {
    mPageRanges = paramArrayOfPageRange;
  }
  
  public void setPrinterId(PrinterId paramPrinterId)
  {
    mPrinterId = paramPrinterId;
  }
  
  public void setPrinterName(String paramString)
  {
    mPrinterName = paramString;
  }
  
  public void setProgress(float paramFloat)
  {
    Preconditions.checkArgumentInRange(paramFloat, 0.0F, 1.0F, "progress");
    mProgress = paramFloat;
  }
  
  public void setState(int paramInt)
  {
    mState = paramInt;
  }
  
  public void setStatus(int paramInt, CharSequence paramCharSequence)
  {
    mStatus = null;
    mStatusRes = paramInt;
    mStatusResAppPackageName = paramCharSequence;
  }
  
  public void setStatus(CharSequence paramCharSequence)
  {
    mStatusRes = 0;
    mStatusResAppPackageName = null;
    mStatus = paramCharSequence;
  }
  
  public void setTag(String paramString)
  {
    mTag = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("PrintJobInfo{");
    localStringBuilder1.append("label: ");
    localStringBuilder1.append(mLabel);
    localStringBuilder1.append(", id: ");
    localStringBuilder1.append(mId);
    localStringBuilder1.append(", state: ");
    localStringBuilder1.append(stateToString(mState));
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(", printer: ");
    ((StringBuilder)localObject1).append(mPrinterId);
    localStringBuilder1.append(((StringBuilder)localObject1).toString());
    localStringBuilder1.append(", tag: ");
    localStringBuilder1.append(mTag);
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(", creationTime: ");
    ((StringBuilder)localObject1).append(mCreationTime);
    localStringBuilder1.append(((StringBuilder)localObject1).toString());
    localStringBuilder1.append(", copies: ");
    localStringBuilder1.append(mCopies);
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", attributes: ");
    localObject1 = mAttributes;
    Object localObject2 = null;
    if (localObject1 != null) {
      localObject1 = mAttributes.toString();
    } else {
      localObject1 = null;
    }
    localStringBuilder2.append((String)localObject1);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", documentInfo: ");
    if (mDocumentInfo != null) {
      localObject1 = mDocumentInfo.toString();
    } else {
      localObject1 = null;
    }
    localStringBuilder2.append((String)localObject1);
    localStringBuilder1.append(localStringBuilder2.toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(", cancelling: ");
    ((StringBuilder)localObject1).append(mCanceling);
    localStringBuilder1.append(((StringBuilder)localObject1).toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", pages: ");
    if (mPageRanges != null) {
      localObject1 = Arrays.toString(mPageRanges);
    } else {
      localObject1 = null;
    }
    localStringBuilder2.append((String)localObject1);
    localStringBuilder1.append(localStringBuilder2.toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(", hasAdvancedOptions: ");
    boolean bool;
    if (mAdvancedOptions != null) {
      bool = true;
    } else {
      bool = false;
    }
    ((StringBuilder)localObject1).append(bool);
    localStringBuilder1.append(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(", progress: ");
    ((StringBuilder)localObject1).append(mProgress);
    localStringBuilder1.append(((StringBuilder)localObject1).toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", status: ");
    if (mStatus != null) {
      localObject1 = mStatus.toString();
    } else {
      localObject1 = null;
    }
    localStringBuilder2.append((String)localObject1);
    localStringBuilder1.append(localStringBuilder2.toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(", statusRes: ");
    ((StringBuilder)localObject1).append(mStatusRes);
    localStringBuilder1.append(((StringBuilder)localObject1).toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", statusResAppPackageName: ");
    localObject1 = localObject2;
    if (mStatusResAppPackageName != null) {
      localObject1 = mStatusResAppPackageName.toString();
    }
    localStringBuilder2.append((String)localObject1);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mId, paramInt);
    paramParcel.writeString(mLabel);
    paramParcel.writeParcelable(mPrinterId, paramInt);
    paramParcel.writeString(mPrinterName);
    paramParcel.writeInt(mState);
    paramParcel.writeInt(mAppId);
    paramParcel.writeString(mTag);
    paramParcel.writeLong(mCreationTime);
    paramParcel.writeInt(mCopies);
    paramParcel.writeParcelableArray(mPageRanges, paramInt);
    paramParcel.writeParcelable(mAttributes, paramInt);
    paramParcel.writeParcelable(mDocumentInfo, 0);
    paramParcel.writeFloat(mProgress);
    paramParcel.writeCharSequence(mStatus);
    paramParcel.writeInt(mStatusRes);
    paramParcel.writeCharSequence(mStatusResAppPackageName);
    paramParcel.writeInt(mCanceling);
    paramParcel.writeBundle(mAdvancedOptions);
  }
  
  public static final class Builder
  {
    private final PrintJobInfo mPrototype;
    
    public Builder(PrintJobInfo paramPrintJobInfo)
    {
      if (paramPrintJobInfo != null) {
        paramPrintJobInfo = new PrintJobInfo(paramPrintJobInfo);
      } else {
        paramPrintJobInfo = new PrintJobInfo();
      }
      mPrototype = paramPrintJobInfo;
    }
    
    public PrintJobInfo build()
    {
      return mPrototype;
    }
    
    public void putAdvancedOption(String paramString, int paramInt)
    {
      if (mPrototype.mAdvancedOptions == null) {
        PrintJobInfo.access$502(mPrototype, new Bundle());
      }
      mPrototype.mAdvancedOptions.putInt(paramString, paramInt);
    }
    
    public void putAdvancedOption(String paramString1, String paramString2)
    {
      Preconditions.checkNotNull(paramString1, "key cannot be null");
      if (mPrototype.mAdvancedOptions == null) {
        PrintJobInfo.access$502(mPrototype, new Bundle());
      }
      mPrototype.mAdvancedOptions.putString(paramString1, paramString2);
    }
    
    public void setAttributes(PrintAttributes paramPrintAttributes)
    {
      PrintJobInfo.access$102(mPrototype, paramPrintAttributes);
    }
    
    public void setCopies(int paramInt)
    {
      PrintJobInfo.access$002(mPrototype, paramInt);
    }
    
    public void setPages(PageRange[] paramArrayOfPageRange)
    {
      PrintJobInfo.access$202(mPrototype, paramArrayOfPageRange);
    }
    
    public void setProgress(float paramFloat)
    {
      Preconditions.checkArgumentInRange(paramFloat, 0.0F, 1.0F, "progress");
      PrintJobInfo.access$302(mPrototype, paramFloat);
    }
    
    public void setStatus(CharSequence paramCharSequence)
    {
      PrintJobInfo.access$402(mPrototype, paramCharSequence);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}
