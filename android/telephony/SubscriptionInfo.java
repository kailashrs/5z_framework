package android.telephony;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import java.util.Arrays;
import java.util.List;

public class SubscriptionInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SubscriptionInfo> CREATOR = new Parcelable.Creator()
  {
    public SubscriptionInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      String str1 = paramAnonymousParcel.readString();
      int j = paramAnonymousParcel.readInt();
      CharSequence localCharSequence1 = paramAnonymousParcel.readCharSequence();
      CharSequence localCharSequence2 = paramAnonymousParcel.readCharSequence();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      String str2 = paramAnonymousParcel.readString();
      int n = paramAnonymousParcel.readInt();
      int i1 = paramAnonymousParcel.readInt();
      int i2 = paramAnonymousParcel.readInt();
      String str3 = paramAnonymousParcel.readString();
      return new SubscriptionInfo(i, str1, j, localCharSequence1, localCharSequence2, k, m, str2, n, (Bitmap)Bitmap.CREATOR.createFromParcel(paramAnonymousParcel), i1, i2, str3, paramAnonymousParcel.readBoolean(), (UiccAccessRule[])paramAnonymousParcel.createTypedArray(UiccAccessRule.CREATOR), paramAnonymousParcel.readString());
    }
    
    public SubscriptionInfo[] newArray(int paramAnonymousInt)
    {
      return new SubscriptionInfo[paramAnonymousInt];
    }
  };
  private static final int TEXT_SIZE = 16;
  private UiccAccessRule[] mAccessRules;
  private String mCardId;
  private CharSequence mCarrierName;
  private String mCountryIso;
  private int mDataRoaming;
  private CharSequence mDisplayName;
  private String mIccId;
  private Bitmap mIconBitmap;
  private int mIconTint;
  private int mId;
  private boolean mIsEmbedded;
  private int mMcc;
  private int mMnc;
  private int mNameSource;
  private String mNumber;
  private int mSimSlotIndex;
  
  public SubscriptionInfo(int paramInt1, String paramString1, int paramInt2, CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt3, int paramInt4, String paramString2, int paramInt5, Bitmap paramBitmap, int paramInt6, int paramInt7, String paramString3)
  {
    this(paramInt1, paramString1, paramInt2, paramCharSequence1, paramCharSequence2, paramInt3, paramInt4, paramString2, paramInt5, paramBitmap, paramInt6, paramInt7, paramString3, false, null, null);
  }
  
  public SubscriptionInfo(int paramInt1, String paramString1, int paramInt2, CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt3, int paramInt4, String paramString2, int paramInt5, Bitmap paramBitmap, int paramInt6, int paramInt7, String paramString3, boolean paramBoolean, UiccAccessRule[] paramArrayOfUiccAccessRule)
  {
    this(paramInt1, paramString1, paramInt2, paramCharSequence1, paramCharSequence2, paramInt3, paramInt4, paramString2, paramInt5, paramBitmap, paramInt6, paramInt7, paramString3, paramBoolean, paramArrayOfUiccAccessRule, null);
  }
  
  public SubscriptionInfo(int paramInt1, String paramString1, int paramInt2, CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt3, int paramInt4, String paramString2, int paramInt5, Bitmap paramBitmap, int paramInt6, int paramInt7, String paramString3, boolean paramBoolean, UiccAccessRule[] paramArrayOfUiccAccessRule, String paramString4)
  {
    mId = paramInt1;
    mIccId = paramString1;
    mSimSlotIndex = paramInt2;
    mDisplayName = paramCharSequence1;
    mCarrierName = paramCharSequence2;
    mNameSource = paramInt3;
    mIconTint = paramInt4;
    mNumber = paramString2;
    mDataRoaming = paramInt5;
    mIconBitmap = paramBitmap;
    mMcc = paramInt6;
    mMnc = paramInt7;
    mCountryIso = paramString3;
    mIsEmbedded = paramBoolean;
    mAccessRules = paramArrayOfUiccAccessRule;
    mCardId = paramString4;
  }
  
  public static String givePrintableIccid(String paramString)
  {
    Object localObject = null;
    if (paramString != null) {
      if ((paramString.length() > 9) && (!Build.IS_DEBUGGABLE))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString.substring(0, 9));
        ((StringBuilder)localObject).append(Rlog.pii(false, paramString.substring(9)));
        localObject = ((StringBuilder)localObject).toString();
      }
      else
      {
        localObject = paramString;
      }
    }
    return localObject;
  }
  
  @Deprecated
  public boolean canManageSubscription(Context paramContext)
  {
    return canManageSubscription(paramContext, paramContext.getPackageName());
  }
  
  @Deprecated
  public boolean canManageSubscription(Context paramContext, String paramString)
  {
    if (isEmbedded())
    {
      if (mAccessRules == null) {
        return false;
      }
      paramContext = paramContext.getPackageManager();
      try
      {
        paramContext = paramContext.getPackageInfo(paramString, 64);
        paramString = mAccessRules;
        int i = paramString.length;
        for (int j = 0; j < i; j++) {
          if (paramString[j].getCarrierPrivilegeStatus(paramContext) == 1) {
            return true;
          }
        }
        return false;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        paramContext = new StringBuilder();
        paramContext.append("Unknown package: ");
        paramContext.append(paramString);
        throw new IllegalArgumentException(paramContext.toString(), localNameNotFoundException);
      }
    }
    throw new UnsupportedOperationException("Not an embedded subscription");
  }
  
  public Bitmap createIconBitmap(Context paramContext)
  {
    int i = mIconBitmap.getWidth();
    int j = mIconBitmap.getHeight();
    Object localObject = paramContext.getResources().getDisplayMetrics();
    paramContext = Bitmap.createBitmap((DisplayMetrics)localObject, i, j, mIconBitmap.getConfig());
    Canvas localCanvas = new Canvas(paramContext);
    Paint localPaint = new Paint();
    localPaint.setColorFilter(new PorterDuffColorFilter(mIconTint, PorterDuff.Mode.SRC_ATOP));
    localCanvas.drawBitmap(mIconBitmap, 0.0F, 0.0F, localPaint);
    localPaint.setColorFilter(null);
    localPaint.setAntiAlias(true);
    localPaint.setTypeface(Typeface.create("sans-serif", 0));
    localPaint.setColor(-1);
    localPaint.setTextSize(16.0F * density);
    localObject = String.format("%d", new Object[] { Integer.valueOf(mSimSlotIndex + 1) });
    Rect localRect = new Rect();
    localPaint.getTextBounds((String)localObject, 0, 1, localRect);
    localCanvas.drawText((String)localObject, i / 2.0F - localRect.centerX(), j / 2.0F - localRect.centerY(), localPaint);
    return paramContext;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @SystemApi
  public List<UiccAccessRule> getAccessRules()
  {
    if (isEmbedded())
    {
      if (mAccessRules == null) {
        return null;
      }
      return Arrays.asList(mAccessRules);
    }
    throw new UnsupportedOperationException("Not an embedded subscription");
  }
  
  public String getCardId()
  {
    return mCardId;
  }
  
  public CharSequence getCarrierName()
  {
    return mCarrierName;
  }
  
  public String getCountryIso()
  {
    return mCountryIso;
  }
  
  public int getDataRoaming()
  {
    return mDataRoaming;
  }
  
  public CharSequence getDisplayName()
  {
    return mDisplayName;
  }
  
  public String getIccId()
  {
    return mIccId;
  }
  
  public int getIconTint()
  {
    return mIconTint;
  }
  
  public int getMcc()
  {
    return mMcc;
  }
  
  public int getMnc()
  {
    return mMnc;
  }
  
  public int getNameSource()
  {
    return mNameSource;
  }
  
  public String getNumber()
  {
    return mNumber;
  }
  
  public int getSimSlotIndex()
  {
    return mSimSlotIndex;
  }
  
  public int getSubscriptionId()
  {
    return mId;
  }
  
  public boolean isEmbedded()
  {
    return mIsEmbedded;
  }
  
  public void setCarrierName(CharSequence paramCharSequence)
  {
    mCarrierName = paramCharSequence;
  }
  
  public void setDisplayName(CharSequence paramCharSequence)
  {
    mDisplayName = paramCharSequence;
  }
  
  public void setIconTint(int paramInt)
  {
    mIconTint = paramInt;
  }
  
  public String toString()
  {
    String str1 = givePrintableIccid(mIccId);
    String str2 = givePrintableIccid(mCardId);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", iccId=");
    localStringBuilder.append(str1);
    localStringBuilder.append(" simSlotIndex=");
    localStringBuilder.append(mSimSlotIndex);
    localStringBuilder.append(" displayName=");
    localStringBuilder.append(mDisplayName);
    localStringBuilder.append(" carrierName=");
    localStringBuilder.append(mCarrierName);
    localStringBuilder.append(" nameSource=");
    localStringBuilder.append(mNameSource);
    localStringBuilder.append(" iconTint=");
    localStringBuilder.append(mIconTint);
    localStringBuilder.append(" dataRoaming=");
    localStringBuilder.append(mDataRoaming);
    localStringBuilder.append(" iconBitmap=");
    localStringBuilder.append(mIconBitmap);
    localStringBuilder.append(" mcc ");
    localStringBuilder.append(mMcc);
    localStringBuilder.append(" mnc ");
    localStringBuilder.append(mMnc);
    localStringBuilder.append(" isEmbedded ");
    localStringBuilder.append(mIsEmbedded);
    localStringBuilder.append(" accessRules ");
    localStringBuilder.append(Arrays.toString(mAccessRules));
    localStringBuilder.append(" cardId=");
    localStringBuilder.append(str2);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeString(mIccId);
    paramParcel.writeInt(mSimSlotIndex);
    paramParcel.writeCharSequence(mDisplayName);
    paramParcel.writeCharSequence(mCarrierName);
    paramParcel.writeInt(mNameSource);
    paramParcel.writeInt(mIconTint);
    paramParcel.writeString(mNumber);
    paramParcel.writeInt(mDataRoaming);
    paramParcel.writeInt(mMcc);
    paramParcel.writeInt(mMnc);
    paramParcel.writeString(mCountryIso);
    mIconBitmap.writeToParcel(paramParcel, paramInt);
    paramParcel.writeBoolean(mIsEmbedded);
    paramParcel.writeTypedArray(mAccessRules, paramInt);
    paramParcel.writeString(mCardId);
  }
}
