package android.service.autofill;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.List;

public final class SaveRequest
  implements Parcelable
{
  public static final Parcelable.Creator<SaveRequest> CREATOR = new Parcelable.Creator()
  {
    public SaveRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SaveRequest(paramAnonymousParcel, null);
    }
    
    public SaveRequest[] newArray(int paramAnonymousInt)
    {
      return new SaveRequest[paramAnonymousInt];
    }
  };
  private final Bundle mClientState;
  private final ArrayList<String> mDatasetIds;
  private final ArrayList<FillContext> mFillContexts;
  
  private SaveRequest(Parcel paramParcel)
  {
    this(paramParcel.createTypedArrayList(FillContext.CREATOR), paramParcel.readBundle(), paramParcel.createStringArrayList());
  }
  
  public SaveRequest(ArrayList<FillContext> paramArrayList, Bundle paramBundle, ArrayList<String> paramArrayList1)
  {
    mFillContexts = ((ArrayList)Preconditions.checkNotNull(paramArrayList, "fillContexts"));
    mClientState = paramBundle;
    mDatasetIds = paramArrayList1;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Bundle getClientState()
  {
    return mClientState;
  }
  
  public List<String> getDatasetIds()
  {
    return mDatasetIds;
  }
  
  public List<FillContext> getFillContexts()
  {
    return mFillContexts;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(mFillContexts, paramInt);
    paramParcel.writeBundle(mClientState);
    paramParcel.writeStringList(mDatasetIds);
  }
}
