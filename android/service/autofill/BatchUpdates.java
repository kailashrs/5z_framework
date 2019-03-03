package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pair;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;

public final class BatchUpdates
  implements Parcelable
{
  public static final Parcelable.Creator<BatchUpdates> CREATOR = new Parcelable.Creator()
  {
    public BatchUpdates createFromParcel(Parcel paramAnonymousParcel)
    {
      BatchUpdates.Builder localBuilder = new BatchUpdates.Builder();
      int[] arrayOfInt = paramAnonymousParcel.createIntArray();
      if (arrayOfInt != null)
      {
        InternalTransformation[] arrayOfInternalTransformation = (InternalTransformation[])paramAnonymousParcel.readParcelableArray(null, InternalTransformation.class);
        int i = arrayOfInt.length;
        for (int j = 0; j < i; j++) {
          localBuilder.transformChild(arrayOfInt[j], arrayOfInternalTransformation[j]);
        }
      }
      paramAnonymousParcel = (RemoteViews)paramAnonymousParcel.readParcelable(null);
      if (paramAnonymousParcel != null) {
        localBuilder.updateTemplate(paramAnonymousParcel);
      }
      return localBuilder.build();
    }
    
    public BatchUpdates[] newArray(int paramAnonymousInt)
    {
      return new BatchUpdates[paramAnonymousInt];
    }
  };
  private final ArrayList<Pair<Integer, InternalTransformation>> mTransformations;
  private final RemoteViews mUpdates;
  
  private BatchUpdates(Builder paramBuilder)
  {
    mTransformations = mTransformations;
    mUpdates = mUpdates;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ArrayList<Pair<Integer, InternalTransformation>> getTransformations()
  {
    return mTransformations;
  }
  
  public RemoteViews getUpdates()
  {
    return mUpdates;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder("BatchUpdates: [");
    localStringBuilder.append(", transformations=");
    Object localObject;
    if (mTransformations == null) {
      localObject = "N/A";
    } else {
      localObject = Integer.valueOf(mTransformations.size());
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", updates=");
    localStringBuilder.append(mUpdates);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mTransformations == null)
    {
      paramParcel.writeIntArray(null);
    }
    else
    {
      int i = mTransformations.size();
      int[] arrayOfInt = new int[i];
      InternalTransformation[] arrayOfInternalTransformation = new InternalTransformation[i];
      for (int j = 0; j < i; j++)
      {
        Pair localPair = (Pair)mTransformations.get(j);
        arrayOfInt[j] = ((Integer)first).intValue();
        arrayOfInternalTransformation[j] = ((InternalTransformation)second);
      }
      paramParcel.writeIntArray(arrayOfInt);
      paramParcel.writeParcelableArray(arrayOfInternalTransformation, paramInt);
    }
    paramParcel.writeParcelable(mUpdates, paramInt);
  }
  
  public static class Builder
  {
    private boolean mDestroyed;
    private ArrayList<Pair<Integer, InternalTransformation>> mTransformations;
    private RemoteViews mUpdates;
    
    public Builder() {}
    
    private void throwIfDestroyed()
    {
      if (!mDestroyed) {
        return;
      }
      throw new IllegalStateException("Already called #build()");
    }
    
    public BatchUpdates build()
    {
      throwIfDestroyed();
      boolean bool;
      if ((mUpdates == null) && (mTransformations == null)) {
        bool = false;
      } else {
        bool = true;
      }
      Preconditions.checkState(bool, "must call either updateTemplate() or transformChild() at least once");
      mDestroyed = true;
      return new BatchUpdates(this, null);
    }
    
    public Builder transformChild(int paramInt, Transformation paramTransformation)
    {
      throwIfDestroyed();
      boolean bool = paramTransformation instanceof InternalTransformation;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("not provided by Android System: ");
      localStringBuilder.append(paramTransformation);
      Preconditions.checkArgument(bool, localStringBuilder.toString());
      if (mTransformations == null) {
        mTransformations = new ArrayList();
      }
      mTransformations.add(new Pair(Integer.valueOf(paramInt), (InternalTransformation)paramTransformation));
      return this;
    }
    
    public Builder updateTemplate(RemoteViews paramRemoteViews)
    {
      throwIfDestroyed();
      mUpdates = ((RemoteViews)Preconditions.checkNotNull(paramRemoteViews));
      return this;
    }
  }
}
