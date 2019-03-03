package android.app;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Rational;
import java.util.ArrayList;
import java.util.List;

public final class PictureInPictureParams
  implements Parcelable
{
  public static final Parcelable.Creator<PictureInPictureParams> CREATOR = new Parcelable.Creator()
  {
    public PictureInPictureParams createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PictureInPictureParams(paramAnonymousParcel);
    }
    
    public PictureInPictureParams[] newArray(int paramAnonymousInt)
    {
      return new PictureInPictureParams[paramAnonymousInt];
    }
  };
  private Rational mAspectRatio;
  private Rect mSourceRectHint;
  private List<RemoteAction> mUserActions;
  
  PictureInPictureParams() {}
  
  PictureInPictureParams(Parcel paramParcel)
  {
    if (paramParcel.readInt() != 0) {
      mAspectRatio = new Rational(paramParcel.readInt(), paramParcel.readInt());
    }
    if (paramParcel.readInt() != 0)
    {
      mUserActions = new ArrayList();
      paramParcel.readParcelableList(mUserActions, RemoteAction.class.getClassLoader());
    }
    if (paramParcel.readInt() != 0) {
      mSourceRectHint = ((Rect)Rect.CREATOR.createFromParcel(paramParcel));
    }
  }
  
  PictureInPictureParams(Rational paramRational, List<RemoteAction> paramList, Rect paramRect)
  {
    mAspectRatio = paramRational;
    mUserActions = paramList;
    mSourceRectHint = paramRect;
  }
  
  public void copyOnlySet(PictureInPictureParams paramPictureInPictureParams)
  {
    if (paramPictureInPictureParams.hasSetAspectRatio()) {
      mAspectRatio = mAspectRatio;
    }
    if (paramPictureInPictureParams.hasSetActions()) {
      mUserActions = mUserActions;
    }
    if (paramPictureInPictureParams.hasSourceBoundsHint()) {
      mSourceRectHint = new Rect(paramPictureInPictureParams.getSourceRectHint());
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<RemoteAction> getActions()
  {
    return mUserActions;
  }
  
  public float getAspectRatio()
  {
    if (mAspectRatio != null) {
      return mAspectRatio.floatValue();
    }
    return 0.0F;
  }
  
  public Rational getAspectRatioRational()
  {
    return mAspectRatio;
  }
  
  public Rect getSourceRectHint()
  {
    return mSourceRectHint;
  }
  
  public boolean hasSetActions()
  {
    boolean bool;
    if (mUserActions != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasSetAspectRatio()
  {
    boolean bool;
    if (mAspectRatio != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasSourceBoundsHint()
  {
    boolean bool;
    if ((mSourceRectHint != null) && (!mSourceRectHint.isEmpty())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void truncateActions(int paramInt)
  {
    if (hasSetActions()) {
      mUserActions = mUserActions.subList(0, Math.min(mUserActions.size(), paramInt));
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mAspectRatio != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeInt(mAspectRatio.getNumerator());
      paramParcel.writeInt(mAspectRatio.getDenominator());
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mUserActions != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeParcelableList(mUserActions, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mSourceRectHint != null)
    {
      paramParcel.writeInt(1);
      mSourceRectHint.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
  
  public static class Builder
  {
    private Rational mAspectRatio;
    private Rect mSourceRectHint;
    private List<RemoteAction> mUserActions;
    
    public Builder() {}
    
    public PictureInPictureParams build()
    {
      return new PictureInPictureParams(mAspectRatio, mUserActions, mSourceRectHint);
    }
    
    public Builder setActions(List<RemoteAction> paramList)
    {
      if (mUserActions != null) {
        mUserActions = null;
      }
      if (paramList != null) {
        mUserActions = new ArrayList(paramList);
      }
      return this;
    }
    
    public Builder setAspectRatio(Rational paramRational)
    {
      mAspectRatio = paramRational;
      return this;
    }
    
    public Builder setSourceRectHint(Rect paramRect)
    {
      if (paramRect == null) {
        mSourceRectHint = null;
      } else {
        mSourceRectHint = new Rect(paramRect);
      }
      return this;
    }
  }
}
