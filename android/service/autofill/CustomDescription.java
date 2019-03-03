package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pair;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;

public final class CustomDescription
  implements Parcelable
{
  public static final Parcelable.Creator<CustomDescription> CREATOR = new Parcelable.Creator()
  {
    public CustomDescription createFromParcel(Parcel paramAnonymousParcel)
    {
      Object localObject1 = (RemoteViews)paramAnonymousParcel.readParcelable(null);
      if (localObject1 == null) {
        return null;
      }
      localObject1 = new CustomDescription.Builder((RemoteViews)localObject1);
      int[] arrayOfInt = paramAnonymousParcel.createIntArray();
      int i = 0;
      int j;
      int k;
      if (arrayOfInt != null)
      {
        localObject2 = (InternalTransformation[])paramAnonymousParcel.readParcelableArray(null, InternalTransformation.class);
        j = arrayOfInt.length;
        for (k = 0; k < j; k++) {
          ((CustomDescription.Builder)localObject1).addChild(arrayOfInt[k], localObject2[k]);
        }
      }
      Object localObject2 = (InternalValidator[])paramAnonymousParcel.readParcelableArray(null, InternalValidator.class);
      if (localObject2 != null)
      {
        paramAnonymousParcel = (BatchUpdates[])paramAnonymousParcel.readParcelableArray(null, BatchUpdates.class);
        j = localObject2.length;
        for (k = i; k < j; k++) {
          ((CustomDescription.Builder)localObject1).batchUpdate(localObject2[k], paramAnonymousParcel[k]);
        }
      }
      return ((CustomDescription.Builder)localObject1).build();
    }
    
    public CustomDescription[] newArray(int paramAnonymousInt)
    {
      return new CustomDescription[paramAnonymousInt];
    }
  };
  private final RemoteViews mPresentation;
  private final ArrayList<Pair<Integer, InternalTransformation>> mTransformations;
  private final ArrayList<Pair<InternalValidator, BatchUpdates>> mUpdates;
  
  private CustomDescription(Builder paramBuilder)
  {
    mPresentation = mPresentation;
    mTransformations = mTransformations;
    mUpdates = mUpdates;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public RemoteViews getPresentation()
  {
    return mPresentation;
  }
  
  public ArrayList<Pair<Integer, InternalTransformation>> getTransformations()
  {
    return mTransformations;
  }
  
  public ArrayList<Pair<InternalValidator, BatchUpdates>> getUpdates()
  {
    return mUpdates;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder("CustomDescription: [presentation=");
    localStringBuilder.append(mPresentation);
    localStringBuilder.append(", transformations=");
    Object localObject;
    if (mTransformations == null) {
      localObject = "N/A";
    } else {
      localObject = Integer.valueOf(mTransformations.size());
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", updates=");
    if (mUpdates == null) {
      localObject = "N/A";
    } else {
      localObject = Integer.valueOf(mUpdates.size());
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mPresentation, paramInt);
    if (mPresentation == null) {
      return;
    }
    Object localObject1 = mTransformations;
    int i = 0;
    int j;
    Object localObject2;
    int k;
    Pair localPair;
    if (localObject1 == null)
    {
      paramParcel.writeIntArray(null);
    }
    else
    {
      j = mTransformations.size();
      localObject2 = new int[j];
      localObject1 = new InternalTransformation[j];
      for (k = 0; k < j; k++)
      {
        localPair = (Pair)mTransformations.get(k);
        localObject2[k] = ((Integer)first).intValue();
        localObject1[k] = ((InternalTransformation)second);
      }
      paramParcel.writeIntArray((int[])localObject2);
      paramParcel.writeParcelableArray((Parcelable[])localObject1, paramInt);
    }
    if (mUpdates == null)
    {
      paramParcel.writeParcelableArray(null, paramInt);
    }
    else
    {
      j = mUpdates.size();
      localObject1 = new InternalValidator[j];
      localObject2 = new BatchUpdates[j];
      for (k = i; k < j; k++)
      {
        localPair = (Pair)mUpdates.get(k);
        localObject1[k] = ((InternalValidator)first);
        localObject2[k] = ((BatchUpdates)second);
      }
      paramParcel.writeParcelableArray((Parcelable[])localObject1, paramInt);
      paramParcel.writeParcelableArray((Parcelable[])localObject2, paramInt);
    }
  }
  
  public static class Builder
  {
    private boolean mDestroyed;
    private final RemoteViews mPresentation;
    private ArrayList<Pair<Integer, InternalTransformation>> mTransformations;
    private ArrayList<Pair<InternalValidator, BatchUpdates>> mUpdates;
    
    public Builder(RemoteViews paramRemoteViews)
    {
      mPresentation = ((RemoteViews)Preconditions.checkNotNull(paramRemoteViews));
    }
    
    private void throwIfDestroyed()
    {
      if (!mDestroyed) {
        return;
      }
      throw new IllegalStateException("Already called #build()");
    }
    
    public Builder addChild(int paramInt, Transformation paramTransformation)
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
    
    public Builder batchUpdate(Validator paramValidator, BatchUpdates paramBatchUpdates)
    {
      throwIfDestroyed();
      boolean bool = paramValidator instanceof InternalValidator;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("not provided by Android System: ");
      localStringBuilder.append(paramValidator);
      Preconditions.checkArgument(bool, localStringBuilder.toString());
      Preconditions.checkNotNull(paramBatchUpdates);
      if (mUpdates == null) {
        mUpdates = new ArrayList();
      }
      mUpdates.add(new Pair((InternalValidator)paramValidator, paramBatchUpdates));
      return this;
    }
    
    public CustomDescription build()
    {
      throwIfDestroyed();
      mDestroyed = true;
      return new CustomDescription(this, null);
    }
  }
}
