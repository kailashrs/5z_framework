package android.service.autofill;

import android.os.Parcel;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class FieldClassification
{
  private final ArrayList<Match> mMatches;
  
  public FieldClassification(ArrayList<Match> paramArrayList)
  {
    mMatches = ((ArrayList)Preconditions.checkNotNull(paramArrayList));
    Collections.sort(mMatches, new Comparator()
    {
      public int compare(FieldClassification.Match paramAnonymousMatch1, FieldClassification.Match paramAnonymousMatch2)
      {
        if (FieldClassification.Match.access$000(paramAnonymousMatch1) > FieldClassification.Match.access$000(paramAnonymousMatch2)) {
          return -1;
        }
        if (FieldClassification.Match.access$000(paramAnonymousMatch1) < FieldClassification.Match.access$000(paramAnonymousMatch2)) {
          return 1;
        }
        return 0;
      }
    });
  }
  
  static FieldClassification[] readArrayFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    FieldClassification[] arrayOfFieldClassification = new FieldClassification[i];
    for (int j = 0; j < i; j++) {
      arrayOfFieldClassification[j] = readFromParcel(paramParcel);
    }
    return arrayOfFieldClassification;
  }
  
  private static FieldClassification readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < i; j++) {
      localArrayList.add(j, Match.readFromParcel(paramParcel));
    }
    return new FieldClassification(localArrayList);
  }
  
  static void writeArrayToParcel(Parcel paramParcel, FieldClassification[] paramArrayOfFieldClassification)
  {
    paramParcel.writeInt(paramArrayOfFieldClassification.length);
    for (int i = 0; i < paramArrayOfFieldClassification.length; i++) {
      paramArrayOfFieldClassification[i].writeToParcel(paramParcel);
    }
  }
  
  private void writeToParcel(Parcel paramParcel)
  {
    paramParcel.writeInt(mMatches.size());
    for (int i = 0; i < mMatches.size(); i++) {
      ((Match)mMatches.get(i)).writeToParcel(paramParcel);
    }
  }
  
  public List<Match> getMatches()
  {
    return mMatches;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("FieldClassification: ");
    localStringBuilder.append(mMatches);
    return localStringBuilder.toString();
  }
  
  public static final class Match
  {
    private final String mCategoryId;
    private final float mScore;
    
    public Match(String paramString, float paramFloat)
    {
      mCategoryId = ((String)Preconditions.checkNotNull(paramString));
      mScore = paramFloat;
    }
    
    private static Match readFromParcel(Parcel paramParcel)
    {
      return new Match(paramParcel.readString(), paramParcel.readFloat());
    }
    
    private void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeString(mCategoryId);
      paramParcel.writeFloat(mScore);
    }
    
    public String getCategoryId()
    {
      return mCategoryId;
    }
    
    public float getScore()
    {
      return mScore;
    }
    
    public String toString()
    {
      if (!Helper.sDebug) {
        return super.toString();
      }
      StringBuilder localStringBuilder = new StringBuilder("Match: categoryId=");
      Helper.appendRedacted(localStringBuilder, mCategoryId);
      localStringBuilder.append(", score=");
      localStringBuilder.append(mScore);
      return localStringBuilder.toString();
    }
  }
}
