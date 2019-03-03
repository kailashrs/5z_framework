package android.view.textclassifier;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class EntityConfidence
  implements Parcelable
{
  public static final Parcelable.Creator<EntityConfidence> CREATOR = new Parcelable.Creator()
  {
    public EntityConfidence createFromParcel(Parcel paramAnonymousParcel)
    {
      return new EntityConfidence(paramAnonymousParcel, null);
    }
    
    public EntityConfidence[] newArray(int paramAnonymousInt)
    {
      return new EntityConfidence[paramAnonymousInt];
    }
  };
  private final ArrayMap<String, Float> mEntityConfidence = new ArrayMap();
  private final ArrayList<String> mSortedEntities = new ArrayList();
  
  EntityConfidence() {}
  
  private EntityConfidence(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    mEntityConfidence.ensureCapacity(i);
    for (int j = 0; j < i; j++) {
      mEntityConfidence.put(paramParcel.readString(), Float.valueOf(paramParcel.readFloat()));
    }
    resetSortedEntitiesFromMap();
  }
  
  EntityConfidence(EntityConfidence paramEntityConfidence)
  {
    Preconditions.checkNotNull(paramEntityConfidence);
    mEntityConfidence.putAll(mEntityConfidence);
    mSortedEntities.addAll(mSortedEntities);
  }
  
  EntityConfidence(Map<String, Float> paramMap)
  {
    Preconditions.checkNotNull(paramMap);
    mEntityConfidence.ensureCapacity(paramMap.size());
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      paramMap = (Map.Entry)localIterator.next();
      if (((Float)paramMap.getValue()).floatValue() > 0.0F) {
        mEntityConfidence.put((String)paramMap.getKey(), Float.valueOf(Math.min(1.0F, ((Float)paramMap.getValue()).floatValue())));
      }
    }
    resetSortedEntitiesFromMap();
  }
  
  private void resetSortedEntitiesFromMap()
  {
    mSortedEntities.clear();
    mSortedEntities.ensureCapacity(mEntityConfidence.size());
    mSortedEntities.addAll(mEntityConfidence.keySet());
    mSortedEntities.sort(new _..Lambda.EntityConfidence.YPh8hwgSYYK8OyQ1kFlQngc71Q0(this));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public float getConfidenceScore(String paramString)
  {
    if (mEntityConfidence.containsKey(paramString)) {
      return ((Float)mEntityConfidence.get(paramString)).floatValue();
    }
    return 0.0F;
  }
  
  public List<String> getEntities()
  {
    return Collections.unmodifiableList(mSortedEntities);
  }
  
  public String toString()
  {
    return mEntityConfidence.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mEntityConfidence.size());
    Iterator localIterator = mEntityConfidence.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramParcel.writeString((String)localEntry.getKey());
      paramParcel.writeFloat(((Float)localEntry.getValue()).floatValue());
    }
  }
}
