package android.service.autofill;

import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class Dataset
  implements Parcelable
{
  public static final Parcelable.Creator<Dataset> CREATOR = new Parcelable.Creator()
  {
    public Dataset createFromParcel(Parcel paramAnonymousParcel)
    {
      Object localObject = (RemoteViews)paramAnonymousParcel.readParcelable(null);
      if (localObject == null) {
        localObject = new Dataset.Builder();
      } else {
        localObject = new Dataset.Builder((RemoteViews)localObject);
      }
      ArrayList localArrayList1 = paramAnonymousParcel.createTypedArrayList(AutofillId.CREATOR);
      ArrayList localArrayList2 = paramAnonymousParcel.createTypedArrayList(AutofillValue.CREATOR);
      ArrayList localArrayList3 = paramAnonymousParcel.createTypedArrayList(RemoteViews.CREATOR);
      ArrayList localArrayList4 = paramAnonymousParcel.createTypedArrayList(Dataset.DatasetFieldFilter.CREATOR);
      for (int i = 0; i < localArrayList1.size(); i++) {
        Dataset.Builder.access$900((Dataset.Builder)localObject, (AutofillId)localArrayList1.get(i), (AutofillValue)localArrayList2.get(i), (RemoteViews)localArrayList3.get(i), (Dataset.DatasetFieldFilter)localArrayList4.get(i));
      }
      ((Dataset.Builder)localObject).setAuthentication((IntentSender)paramAnonymousParcel.readParcelable(null));
      ((Dataset.Builder)localObject).setId(paramAnonymousParcel.readString());
      return ((Dataset.Builder)localObject).build();
    }
    
    public Dataset[] newArray(int paramAnonymousInt)
    {
      return new Dataset[paramAnonymousInt];
    }
  };
  private final IntentSender mAuthentication;
  private final ArrayList<DatasetFieldFilter> mFieldFilters;
  private final ArrayList<AutofillId> mFieldIds;
  private final ArrayList<RemoteViews> mFieldPresentations;
  private final ArrayList<AutofillValue> mFieldValues;
  String mId;
  private final RemoteViews mPresentation;
  
  private Dataset(Builder paramBuilder)
  {
    mFieldIds = mFieldIds;
    mFieldValues = mFieldValues;
    mFieldPresentations = mFieldPresentations;
    mFieldFilters = mFieldFilters;
    mPresentation = mPresentation;
    mAuthentication = mAuthentication;
    mId = mId;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public IntentSender getAuthentication()
  {
    return mAuthentication;
  }
  
  public ArrayList<AutofillId> getFieldIds()
  {
    return mFieldIds;
  }
  
  public RemoteViews getFieldPresentation(int paramInt)
  {
    RemoteViews localRemoteViews = (RemoteViews)mFieldPresentations.get(paramInt);
    if (localRemoteViews == null) {
      localRemoteViews = mPresentation;
    }
    return localRemoteViews;
  }
  
  public ArrayList<AutofillValue> getFieldValues()
  {
    return mFieldValues;
  }
  
  public DatasetFieldFilter getFilter(int paramInt)
  {
    return (DatasetFieldFilter)mFieldFilters.get(paramInt);
  }
  
  public String getId()
  {
    return mId;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((mFieldIds != null) && (!mFieldIds.isEmpty())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder("Dataset[");
    if (mId == null)
    {
      localStringBuilder.append("noId");
    }
    else
    {
      localStringBuilder.append("id=");
      localStringBuilder.append(mId.length());
      localStringBuilder.append("_chars");
    }
    if (mFieldIds != null)
    {
      localStringBuilder.append(", fieldIds=");
      localStringBuilder.append(mFieldIds);
    }
    if (mFieldValues != null)
    {
      localStringBuilder.append(", fieldValues=");
      localStringBuilder.append(mFieldValues);
    }
    if (mFieldPresentations != null)
    {
      localStringBuilder.append(", fieldPresentations=");
      localStringBuilder.append(mFieldPresentations.size());
    }
    if (mFieldFilters != null)
    {
      localStringBuilder.append(", fieldFilters=");
      localStringBuilder.append(mFieldFilters.size());
    }
    if (mPresentation != null) {
      localStringBuilder.append(", hasPresentation");
    }
    if (mAuthentication != null) {
      localStringBuilder.append(", hasAuthentication");
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mPresentation, paramInt);
    paramParcel.writeTypedList(mFieldIds, paramInt);
    paramParcel.writeTypedList(mFieldValues, paramInt);
    paramParcel.writeTypedList(mFieldPresentations, paramInt);
    paramParcel.writeTypedList(mFieldFilters, paramInt);
    paramParcel.writeParcelable(mAuthentication, paramInt);
    paramParcel.writeString(mId);
  }
  
  public static final class Builder
  {
    private IntentSender mAuthentication;
    private boolean mDestroyed;
    private ArrayList<Dataset.DatasetFieldFilter> mFieldFilters;
    private ArrayList<AutofillId> mFieldIds;
    private ArrayList<RemoteViews> mFieldPresentations;
    private ArrayList<AutofillValue> mFieldValues;
    private String mId;
    private RemoteViews mPresentation;
    
    public Builder() {}
    
    public Builder(RemoteViews paramRemoteViews)
    {
      Preconditions.checkNotNull(paramRemoteViews, "presentation must be non-null");
      mPresentation = paramRemoteViews;
    }
    
    private void setLifeTheUniverseAndEverything(AutofillId paramAutofillId, AutofillValue paramAutofillValue, RemoteViews paramRemoteViews, Dataset.DatasetFieldFilter paramDatasetFieldFilter)
    {
      Preconditions.checkNotNull(paramAutofillId, "id cannot be null");
      if (mFieldIds != null)
      {
        int i = mFieldIds.indexOf(paramAutofillId);
        if (i >= 0)
        {
          mFieldValues.set(i, paramAutofillValue);
          mFieldPresentations.set(i, paramRemoteViews);
          mFieldFilters.set(i, paramDatasetFieldFilter);
          return;
        }
      }
      else
      {
        mFieldIds = new ArrayList();
        mFieldValues = new ArrayList();
        mFieldPresentations = new ArrayList();
        mFieldFilters = new ArrayList();
      }
      mFieldIds.add(paramAutofillId);
      mFieldValues.add(paramAutofillValue);
      mFieldPresentations.add(paramRemoteViews);
      mFieldFilters.add(paramDatasetFieldFilter);
    }
    
    private void throwIfDestroyed()
    {
      if (!mDestroyed) {
        return;
      }
      throw new IllegalStateException("Already called #build()");
    }
    
    public Dataset build()
    {
      throwIfDestroyed();
      mDestroyed = true;
      if (mFieldIds != null) {
        return new Dataset(this, null);
      }
      throw new IllegalStateException("at least one value must be set");
    }
    
    public Builder setAuthentication(IntentSender paramIntentSender)
    {
      throwIfDestroyed();
      mAuthentication = paramIntentSender;
      return this;
    }
    
    public Builder setId(String paramString)
    {
      throwIfDestroyed();
      mId = paramString;
      return this;
    }
    
    public Builder setValue(AutofillId paramAutofillId, AutofillValue paramAutofillValue)
    {
      throwIfDestroyed();
      setLifeTheUniverseAndEverything(paramAutofillId, paramAutofillValue, null, null);
      return this;
    }
    
    public Builder setValue(AutofillId paramAutofillId, AutofillValue paramAutofillValue, RemoteViews paramRemoteViews)
    {
      throwIfDestroyed();
      Preconditions.checkNotNull(paramRemoteViews, "presentation cannot be null");
      setLifeTheUniverseAndEverything(paramAutofillId, paramAutofillValue, paramRemoteViews, null);
      return this;
    }
    
    public Builder setValue(AutofillId paramAutofillId, AutofillValue paramAutofillValue, Pattern paramPattern)
    {
      throwIfDestroyed();
      boolean bool;
      if (mPresentation != null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool, "Dataset presentation not set on constructor");
      setLifeTheUniverseAndEverything(paramAutofillId, paramAutofillValue, null, new Dataset.DatasetFieldFilter(paramPattern, null));
      return this;
    }
    
    public Builder setValue(AutofillId paramAutofillId, AutofillValue paramAutofillValue, Pattern paramPattern, RemoteViews paramRemoteViews)
    {
      throwIfDestroyed();
      Preconditions.checkNotNull(paramRemoteViews, "presentation cannot be null");
      setLifeTheUniverseAndEverything(paramAutofillId, paramAutofillValue, paramRemoteViews, new Dataset.DatasetFieldFilter(paramPattern, null));
      return this;
    }
  }
  
  public static final class DatasetFieldFilter
    implements Parcelable
  {
    public static final Parcelable.Creator<DatasetFieldFilter> CREATOR = new Parcelable.Creator()
    {
      public Dataset.DatasetFieldFilter createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Dataset.DatasetFieldFilter((Pattern)paramAnonymousParcel.readSerializable(), null);
      }
      
      public Dataset.DatasetFieldFilter[] newArray(int paramAnonymousInt)
      {
        return new Dataset.DatasetFieldFilter[paramAnonymousInt];
      }
    };
    public final Pattern pattern;
    
    private DatasetFieldFilter(Pattern paramPattern)
    {
      pattern = paramPattern;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      if (!Helper.sDebug) {
        return super.toString();
      }
      Object localObject;
      if (pattern == null)
      {
        localObject = "null";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(pattern.pattern().length());
        ((StringBuilder)localObject).append("_chars");
        localObject = ((StringBuilder)localObject).toString();
      }
      return localObject;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeSerializable(pattern);
    }
  }
}
