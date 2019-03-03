package android.service.resolver;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class ResolverTarget
  implements Parcelable
{
  public static final Parcelable.Creator<ResolverTarget> CREATOR = new Parcelable.Creator()
  {
    public ResolverTarget createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ResolverTarget(paramAnonymousParcel);
    }
    
    public ResolverTarget[] newArray(int paramAnonymousInt)
    {
      return new ResolverTarget[paramAnonymousInt];
    }
  };
  private static final String TAG = "ResolverTarget";
  private float mChooserScore;
  private float mLaunchScore;
  private float mRecencyScore;
  private float mSelectProbability;
  private float mTimeSpentScore;
  
  public ResolverTarget() {}
  
  ResolverTarget(Parcel paramParcel)
  {
    mRecencyScore = paramParcel.readFloat();
    mTimeSpentScore = paramParcel.readFloat();
    mLaunchScore = paramParcel.readFloat();
    mChooserScore = paramParcel.readFloat();
    mSelectProbability = paramParcel.readFloat();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public float getChooserScore()
  {
    return mChooserScore;
  }
  
  public float getLaunchScore()
  {
    return mLaunchScore;
  }
  
  public float getRecencyScore()
  {
    return mRecencyScore;
  }
  
  public float getSelectProbability()
  {
    return mSelectProbability;
  }
  
  public float getTimeSpentScore()
  {
    return mTimeSpentScore;
  }
  
  public void setChooserScore(float paramFloat)
  {
    mChooserScore = paramFloat;
  }
  
  public void setLaunchScore(float paramFloat)
  {
    mLaunchScore = paramFloat;
  }
  
  public void setRecencyScore(float paramFloat)
  {
    mRecencyScore = paramFloat;
  }
  
  public void setSelectProbability(float paramFloat)
  {
    mSelectProbability = paramFloat;
  }
  
  public void setTimeSpentScore(float paramFloat)
  {
    mTimeSpentScore = paramFloat;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ResolverTarget{");
    localStringBuilder.append(mRecencyScore);
    localStringBuilder.append(", ");
    localStringBuilder.append(mTimeSpentScore);
    localStringBuilder.append(", ");
    localStringBuilder.append(mLaunchScore);
    localStringBuilder.append(", ");
    localStringBuilder.append(mChooserScore);
    localStringBuilder.append(", ");
    localStringBuilder.append(mSelectProbability);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(mRecencyScore);
    paramParcel.writeFloat(mTimeSpentScore);
    paramParcel.writeFloat(mLaunchScore);
    paramParcel.writeFloat(mChooserScore);
    paramParcel.writeFloat(mSelectProbability);
  }
}
