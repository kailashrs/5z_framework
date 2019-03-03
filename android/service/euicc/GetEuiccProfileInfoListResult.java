package android.service.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;
import java.util.List;

@SystemApi
public final class GetEuiccProfileInfoListResult
  implements Parcelable
{
  public static final Parcelable.Creator<GetEuiccProfileInfoListResult> CREATOR = new Parcelable.Creator()
  {
    public GetEuiccProfileInfoListResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new GetEuiccProfileInfoListResult(paramAnonymousParcel, null);
    }
    
    public GetEuiccProfileInfoListResult[] newArray(int paramAnonymousInt)
    {
      return new GetEuiccProfileInfoListResult[paramAnonymousInt];
    }
  };
  private final boolean mIsRemovable;
  private final EuiccProfileInfo[] mProfiles;
  @Deprecated
  public final int result;
  
  public GetEuiccProfileInfoListResult(int paramInt, EuiccProfileInfo[] paramArrayOfEuiccProfileInfo, boolean paramBoolean)
  {
    result = paramInt;
    mIsRemovable = paramBoolean;
    if (result == 0)
    {
      mProfiles = paramArrayOfEuiccProfileInfo;
    }
    else
    {
      if (paramArrayOfEuiccProfileInfo != null) {
        break label39;
      }
      mProfiles = null;
    }
    return;
    label39:
    paramArrayOfEuiccProfileInfo = new StringBuilder();
    paramArrayOfEuiccProfileInfo.append("Error result with non-null profiles: ");
    paramArrayOfEuiccProfileInfo.append(paramInt);
    throw new IllegalArgumentException(paramArrayOfEuiccProfileInfo.toString());
  }
  
  private GetEuiccProfileInfoListResult(Parcel paramParcel)
  {
    result = paramParcel.readInt();
    mProfiles = ((EuiccProfileInfo[])paramParcel.createTypedArray(EuiccProfileInfo.CREATOR));
    mIsRemovable = paramParcel.readBoolean();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getIsRemovable()
  {
    return mIsRemovable;
  }
  
  public List<EuiccProfileInfo> getProfiles()
  {
    if (mProfiles == null) {
      return null;
    }
    return Arrays.asList(mProfiles);
  }
  
  public int getResult()
  {
    return result;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(result);
    paramParcel.writeTypedArray(mProfiles, paramInt);
    paramParcel.writeBoolean(mIsRemovable);
  }
}
