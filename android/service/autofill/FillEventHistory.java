package android.service.autofill;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FillEventHistory
  implements Parcelable
{
  public static final Parcelable.Creator<FillEventHistory> CREATOR = new Parcelable.Creator()
  {
    public FillEventHistory createFromParcel(Parcel paramAnonymousParcel)
    {
      FillEventHistory localFillEventHistory = new FillEventHistory(0, paramAnonymousParcel.readBundle());
      int i = paramAnonymousParcel.readInt();
      for (int j = 0; j < i; j++)
      {
        int k = paramAnonymousParcel.readInt();
        String str = paramAnonymousParcel.readString();
        Bundle localBundle = paramAnonymousParcel.readBundle();
        ArrayList localArrayList1 = paramAnonymousParcel.createStringArrayList();
        FieldClassification[] arrayOfFieldClassification = null;
        ArraySet localArraySet = paramAnonymousParcel.readArraySet(null);
        ArrayList localArrayList2 = paramAnonymousParcel.createTypedArrayList(AutofillId.CREATOR);
        ArrayList localArrayList3 = paramAnonymousParcel.createStringArrayList();
        ArrayList localArrayList4 = paramAnonymousParcel.createTypedArrayList(AutofillId.CREATOR);
        ArrayList localArrayList5;
        if (localArrayList4 != null)
        {
          int m = localArrayList4.size();
          localArrayList5 = new ArrayList(m);
          for (int n = 0; n < m; n++) {
            localArrayList5.add(paramAnonymousParcel.createStringArrayList());
          }
        }
        else
        {
          localArrayList5 = null;
        }
        AutofillId[] arrayOfAutofillId = (AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class);
        if (arrayOfAutofillId != null) {
          arrayOfFieldClassification = FieldClassification.readArrayFromParcel(paramAnonymousParcel);
        }
        for (;;)
        {
          break;
        }
        localFillEventHistory.addEvent(new FillEventHistory.Event(k, str, localBundle, localArrayList1, localArraySet, localArrayList2, localArrayList3, localArrayList4, localArrayList5, arrayOfAutofillId, arrayOfFieldClassification));
      }
      return localFillEventHistory;
    }
    
    public FillEventHistory[] newArray(int paramAnonymousInt)
    {
      return new FillEventHistory[paramAnonymousInt];
    }
  };
  private static final String TAG = "FillEventHistory";
  private final Bundle mClientState;
  List<Event> mEvents;
  private final int mSessionId;
  
  public FillEventHistory(int paramInt, Bundle paramBundle)
  {
    mClientState = paramBundle;
    mSessionId = paramInt;
  }
  
  public void addEvent(Event paramEvent)
  {
    if (mEvents == null) {
      mEvents = new ArrayList(1);
    }
    mEvents.add(paramEvent);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @Deprecated
  public Bundle getClientState()
  {
    return mClientState;
  }
  
  public List<Event> getEvents()
  {
    return mEvents;
  }
  
  public int getSessionId()
  {
    return mSessionId;
  }
  
  public String toString()
  {
    String str;
    if (mEvents == null) {
      str = "no events";
    } else {
      str = mEvents.toString();
    }
    return str;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBundle(mClientState);
    if (mEvents == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(mEvents.size());
      int i = mEvents.size();
      for (int j = 0; j < i; j++)
      {
        Event localEvent = (Event)mEvents.get(j);
        paramParcel.writeInt(mEventType);
        paramParcel.writeString(mDatasetId);
        paramParcel.writeBundle(mClientState);
        paramParcel.writeStringList(mSelectedDatasetIds);
        paramParcel.writeArraySet(mIgnoredDatasetIds);
        paramParcel.writeTypedList(mChangedFieldIds);
        paramParcel.writeStringList(mChangedDatasetIds);
        paramParcel.writeTypedList(mManuallyFilledFieldIds);
        if (mManuallyFilledFieldIds != null)
        {
          int k = mManuallyFilledFieldIds.size();
          for (int m = 0; m < k; m++) {
            paramParcel.writeStringList((List)mManuallyFilledDatasetIds.get(m));
          }
        }
        AutofillId[] arrayOfAutofillId = mDetectedFieldIds;
        paramParcel.writeParcelableArray(arrayOfAutofillId, paramInt);
        if (arrayOfAutofillId != null) {
          FieldClassification.writeArrayToParcel(paramParcel, mDetectedFieldClassifications);
        }
      }
    }
  }
  
  public static final class Event
  {
    public static final int TYPE_AUTHENTICATION_SELECTED = 2;
    public static final int TYPE_CONTEXT_COMMITTED = 4;
    public static final int TYPE_DATASET_AUTHENTICATION_SELECTED = 1;
    public static final int TYPE_DATASET_SELECTED = 0;
    public static final int TYPE_SAVE_SHOWN = 3;
    private final ArrayList<String> mChangedDatasetIds;
    private final ArrayList<AutofillId> mChangedFieldIds;
    private final Bundle mClientState;
    private final String mDatasetId;
    private final FieldClassification[] mDetectedFieldClassifications;
    private final AutofillId[] mDetectedFieldIds;
    private final int mEventType;
    private final ArraySet<String> mIgnoredDatasetIds;
    private final ArrayList<ArrayList<String>> mManuallyFilledDatasetIds;
    private final ArrayList<AutofillId> mManuallyFilledFieldIds;
    private final List<String> mSelectedDatasetIds;
    
    public Event(int paramInt, String paramString, Bundle paramBundle, List<String> paramList, ArraySet<String> paramArraySet, ArrayList<AutofillId> paramArrayList1, ArrayList<String> paramArrayList, ArrayList<AutofillId> paramArrayList2, ArrayList<ArrayList<String>> paramArrayList3, AutofillId[] paramArrayOfAutofillId, FieldClassification[] paramArrayOfFieldClassification)
    {
      mEventType = Preconditions.checkArgumentInRange(paramInt, 0, 4, "eventType");
      mDatasetId = paramString;
      mClientState = paramBundle;
      mSelectedDatasetIds = paramList;
      mIgnoredDatasetIds = paramArraySet;
      boolean bool1 = true;
      boolean bool2;
      if (paramArrayList1 != null)
      {
        if ((!ArrayUtils.isEmpty(paramArrayList1)) && (paramArrayList != null) && (paramArrayList1.size() == paramArrayList.size())) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        Preconditions.checkArgument(bool2, "changed ids must have same length and not be empty");
      }
      mChangedFieldIds = paramArrayList1;
      mChangedDatasetIds = paramArrayList;
      if (paramArrayList2 != null)
      {
        if ((!ArrayUtils.isEmpty(paramArrayList2)) && (paramArrayList3 != null) && (paramArrayList2.size() == paramArrayList3.size())) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
        Preconditions.checkArgument(bool2, "manually filled ids must have same length and not be empty");
      }
      mManuallyFilledFieldIds = paramArrayList2;
      mManuallyFilledDatasetIds = paramArrayList3;
      mDetectedFieldIds = paramArrayOfAutofillId;
      mDetectedFieldClassifications = paramArrayOfFieldClassification;
    }
    
    public Map<AutofillId, String> getChangedFields()
    {
      if ((mChangedFieldIds != null) && (mChangedDatasetIds != null))
      {
        int i = mChangedFieldIds.size();
        ArrayMap localArrayMap = new ArrayMap(i);
        for (int j = 0; j < i; j++) {
          localArrayMap.put((AutofillId)mChangedFieldIds.get(j), (String)mChangedDatasetIds.get(j));
        }
        return localArrayMap;
      }
      return Collections.emptyMap();
    }
    
    public Bundle getClientState()
    {
      return mClientState;
    }
    
    public String getDatasetId()
    {
      return mDatasetId;
    }
    
    public Map<AutofillId, FieldClassification> getFieldsClassification()
    {
      if (mDetectedFieldIds == null) {
        return Collections.emptyMap();
      }
      int i = mDetectedFieldIds.length;
      ArrayMap localArrayMap = new ArrayMap(i);
      for (int j = 0; j < i; j++)
      {
        AutofillId localAutofillId = mDetectedFieldIds[j];
        FieldClassification localFieldClassification = mDetectedFieldClassifications[j];
        if (Helper.sVerbose)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("getFieldsClassification[");
          localStringBuilder.append(j);
          localStringBuilder.append("]: id=");
          localStringBuilder.append(localAutofillId);
          localStringBuilder.append(", fc=");
          localStringBuilder.append(localFieldClassification);
          Log.v("FillEventHistory", localStringBuilder.toString());
        }
        localArrayMap.put(localAutofillId, localFieldClassification);
      }
      return localArrayMap;
    }
    
    public Set<String> getIgnoredDatasetIds()
    {
      Object localObject;
      if (mIgnoredDatasetIds == null) {
        localObject = Collections.emptySet();
      } else {
        localObject = mIgnoredDatasetIds;
      }
      return localObject;
    }
    
    public Map<AutofillId, Set<String>> getManuallyEnteredField()
    {
      if ((mManuallyFilledFieldIds != null) && (mManuallyFilledDatasetIds != null))
      {
        int i = mManuallyFilledFieldIds.size();
        ArrayMap localArrayMap = new ArrayMap(i);
        for (int j = 0; j < i; j++) {
          localArrayMap.put((AutofillId)mManuallyFilledFieldIds.get(j), new ArraySet((ArrayList)mManuallyFilledDatasetIds.get(j)));
        }
        return localArrayMap;
      }
      return Collections.emptyMap();
    }
    
    public Set<String> getSelectedDatasetIds()
    {
      Object localObject;
      if (mSelectedDatasetIds == null) {
        localObject = Collections.emptySet();
      } else {
        localObject = new ArraySet(mSelectedDatasetIds);
      }
      return localObject;
    }
    
    public int getType()
    {
      return mEventType;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("FillEvent [datasetId=");
      localStringBuilder.append(mDatasetId);
      localStringBuilder.append(", type=");
      localStringBuilder.append(mEventType);
      localStringBuilder.append(", selectedDatasets=");
      localStringBuilder.append(mSelectedDatasetIds);
      localStringBuilder.append(", ignoredDatasetIds=");
      localStringBuilder.append(mIgnoredDatasetIds);
      localStringBuilder.append(", changedFieldIds=");
      localStringBuilder.append(mChangedFieldIds);
      localStringBuilder.append(", changedDatasetsIds=");
      localStringBuilder.append(mChangedDatasetIds);
      localStringBuilder.append(", manuallyFilledFieldIds=");
      localStringBuilder.append(mManuallyFilledFieldIds);
      localStringBuilder.append(", manuallyFilledDatasetIds=");
      localStringBuilder.append(mManuallyFilledDatasetIds);
      localStringBuilder.append(", detectedFieldIds=");
      localStringBuilder.append(Arrays.toString(mDetectedFieldIds));
      localStringBuilder.append(", detectedFieldClassifications =");
      localStringBuilder.append(Arrays.toString(mDetectedFieldClassifications));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    static @interface EventIds {}
  }
}
