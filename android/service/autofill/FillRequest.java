package android.service.autofill;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public final class FillRequest
  implements Parcelable
{
  public static final Parcelable.Creator<FillRequest> CREATOR = new Parcelable.Creator()
  {
    public FillRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FillRequest(paramAnonymousParcel, null);
    }
    
    public FillRequest[] newArray(int paramAnonymousInt)
    {
      return new FillRequest[paramAnonymousInt];
    }
  };
  public static final int FLAG_MANUAL_REQUEST = 1;
  public static final int INVALID_REQUEST_ID = Integer.MIN_VALUE;
  private final Bundle mClientState;
  private final ArrayList<FillContext> mContexts;
  private final int mFlags;
  private final int mId;
  
  public FillRequest(int paramInt1, ArrayList<FillContext> paramArrayList, Bundle paramBundle, int paramInt2)
  {
    mId = paramInt1;
    mFlags = Preconditions.checkFlagsArgument(paramInt2, 1);
    mContexts = ((ArrayList)Preconditions.checkCollectionElementsNotNull(paramArrayList, "contexts"));
    mClientState = paramBundle;
  }
  
  private FillRequest(Parcel paramParcel)
  {
    mId = paramParcel.readInt();
    mContexts = new ArrayList();
    paramParcel.readParcelableList(mContexts, null);
    mClientState = paramParcel.readBundle();
    mFlags = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Bundle getClientState()
  {
    return mClientState;
  }
  
  public List<FillContext> getFillContexts()
  {
    return mContexts;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("FillRequest: [id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", flags=");
    localStringBuilder.append(mFlags);
    localStringBuilder.append(", ctxts= ");
    localStringBuilder.append(mContexts);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeParcelableList(mContexts, paramInt);
    paramParcel.writeBundle(mClientState);
    paramParcel.writeInt(mFlags);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface RequestFlags {}
}
