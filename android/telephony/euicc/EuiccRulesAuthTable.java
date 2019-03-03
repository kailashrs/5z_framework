package android.telephony.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.service.carrier.CarrierIdentifier;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

@SystemApi
public final class EuiccRulesAuthTable
  implements Parcelable
{
  public static final Parcelable.Creator<EuiccRulesAuthTable> CREATOR = new Parcelable.Creator()
  {
    public EuiccRulesAuthTable createFromParcel(Parcel paramAnonymousParcel)
    {
      return new EuiccRulesAuthTable(paramAnonymousParcel, null);
    }
    
    public EuiccRulesAuthTable[] newArray(int paramAnonymousInt)
    {
      return new EuiccRulesAuthTable[paramAnonymousInt];
    }
  };
  public static final int POLICY_RULE_FLAG_CONSENT_REQUIRED = 1;
  private final CarrierIdentifier[][] mCarrierIds;
  private final int[] mPolicyRuleFlags;
  private final int[] mPolicyRules;
  
  private EuiccRulesAuthTable(Parcel paramParcel)
  {
    mPolicyRules = paramParcel.createIntArray();
    int i = mPolicyRules.length;
    mCarrierIds = new CarrierIdentifier[i][];
    for (int j = 0; j < i; j++) {
      mCarrierIds[j] = ((CarrierIdentifier[])paramParcel.createTypedArray(CarrierIdentifier.CREATOR));
    }
    mPolicyRuleFlags = paramParcel.createIntArray();
  }
  
  private EuiccRulesAuthTable(int[] paramArrayOfInt1, CarrierIdentifier[][] paramArrayOfCarrierIdentifier, int[] paramArrayOfInt2)
  {
    mPolicyRules = paramArrayOfInt1;
    mCarrierIds = paramArrayOfCarrierIdentifier;
    mPolicyRuleFlags = paramArrayOfInt2;
  }
  
  @VisibleForTesting
  public static boolean match(String paramString1, String paramString2)
  {
    if (paramString1.length() < paramString2.length()) {
      return false;
    }
    for (int i = 0; i < paramString1.length(); i++) {
      if ((paramString1.charAt(i) != 'E') && ((i >= paramString2.length()) || (paramString1.charAt(i) != paramString2.charAt(i)))) {
        return false;
      }
    }
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (EuiccRulesAuthTable)paramObject;
      if (mCarrierIds.length != mCarrierIds.length) {
        return false;
      }
      int i = 0;
      while (i < mCarrierIds.length)
      {
        CarrierIdentifier[] arrayOfCarrierIdentifier1 = mCarrierIds[i];
        CarrierIdentifier[] arrayOfCarrierIdentifier2 = mCarrierIds[i];
        if ((arrayOfCarrierIdentifier1 != null) && (arrayOfCarrierIdentifier2 != null))
        {
          if (arrayOfCarrierIdentifier1.length != arrayOfCarrierIdentifier2.length) {
            return false;
          }
          for (int j = 0; j < arrayOfCarrierIdentifier1.length; j++) {
            if (!arrayOfCarrierIdentifier1[j].equals(arrayOfCarrierIdentifier2[j])) {
              return false;
            }
          }
        }
        else
        {
          if ((arrayOfCarrierIdentifier1 != null) || (arrayOfCarrierIdentifier2 != null)) {
            break label149;
          }
        }
        i++;
        continue;
        label149:
        return false;
      }
      if ((!Arrays.equals(mPolicyRules, mPolicyRules)) || (!Arrays.equals(mPolicyRuleFlags, mPolicyRuleFlags))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int findIndex(int paramInt, CarrierIdentifier paramCarrierIdentifier)
  {
    for (int i = 0; i < mPolicyRules.length; i++) {
      if ((mPolicyRules[i] & paramInt) != 0)
      {
        CarrierIdentifier[] arrayOfCarrierIdentifier = mCarrierIds[i];
        if ((arrayOfCarrierIdentifier != null) && (arrayOfCarrierIdentifier.length != 0)) {
          for (int j = 0; j < arrayOfCarrierIdentifier.length; j++)
          {
            Object localObject = arrayOfCarrierIdentifier[j];
            if ((match(((CarrierIdentifier)localObject).getMcc(), paramCarrierIdentifier.getMcc())) && (match(((CarrierIdentifier)localObject).getMnc(), paramCarrierIdentifier.getMnc())))
            {
              String str = ((CarrierIdentifier)localObject).getGid1();
              if ((TextUtils.isEmpty(str)) || (str.equals(paramCarrierIdentifier.getGid1())))
              {
                localObject = ((CarrierIdentifier)localObject).getGid2();
                if ((TextUtils.isEmpty((CharSequence)localObject)) || (((String)localObject).equals(paramCarrierIdentifier.getGid2()))) {
                  return i;
                }
              }
            }
          }
        }
      }
    }
    return -1;
  }
  
  public boolean hasPolicyRuleFlag(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mPolicyRules.length))
    {
      boolean bool;
      if ((mPolicyRuleFlags[paramInt1] & paramInt2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    throw new ArrayIndexOutOfBoundsException(paramInt1);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeIntArray(mPolicyRules);
    CarrierIdentifier[][] arrayOfCarrierIdentifier = mCarrierIds;
    int i = arrayOfCarrierIdentifier.length;
    for (int j = 0; j < i; j++) {
      paramParcel.writeTypedArray(arrayOfCarrierIdentifier[j], paramInt);
    }
    paramParcel.writeIntArray(mPolicyRuleFlags);
  }
  
  public static final class Builder
  {
    private CarrierIdentifier[][] mCarrierIds;
    private int[] mPolicyRuleFlags;
    private int[] mPolicyRules;
    private int mPosition;
    
    public Builder(int paramInt)
    {
      mPolicyRules = new int[paramInt];
      mCarrierIds = new CarrierIdentifier[paramInt][];
      mPolicyRuleFlags = new int[paramInt];
    }
    
    public Builder add(int paramInt1, List<CarrierIdentifier> paramList, int paramInt2)
    {
      if (mPosition < mPolicyRules.length)
      {
        mPolicyRules[mPosition] = paramInt1;
        if ((paramList != null) && (paramList.size() > 0)) {
          mCarrierIds[mPosition] = ((CarrierIdentifier[])paramList.toArray(new CarrierIdentifier[paramList.size()]));
        }
        mPolicyRuleFlags[mPosition] = paramInt2;
        mPosition += 1;
        return this;
      }
      throw new ArrayIndexOutOfBoundsException(mPosition);
    }
    
    public EuiccRulesAuthTable build()
    {
      if (mPosition == mPolicyRules.length) {
        return new EuiccRulesAuthTable(mPolicyRules, mCarrierIds, mPolicyRuleFlags, null);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Not enough rules are added, expected: ");
      localStringBuilder.append(mPolicyRules.length);
      localStringBuilder.append(", added: ");
      localStringBuilder.append(mPosition);
      throw new IllegalStateException(localStringBuilder.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PolicyRuleFlag {}
}
