package android.app;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Rational;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class PictureInPictureArgs
  implements Parcelable
{
  public static final Parcelable.Creator<PictureInPictureArgs> CREATOR = new Parcelable.Creator()
  {
    public PictureInPictureArgs createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PictureInPictureArgs(paramAnonymousParcel, null);
    }
    
    public PictureInPictureArgs[] newArray(int paramAnonymousInt)
    {
      return new PictureInPictureArgs[paramAnonymousInt];
    }
  };
  private Rational mAspectRatio;
  private Rect mSourceRectHint;
  private Rect mSourceRectHintInsets;
  private List<RemoteAction> mUserActions;
  
  @Deprecated
  public PictureInPictureArgs() {}
  
  @Deprecated
  public PictureInPictureArgs(float paramFloat, List<RemoteAction> paramList)
  {
    setAspectRatio(paramFloat);
    setActions(paramList);
  }
  
  private PictureInPictureArgs(Parcel paramParcel)
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
  
  private PictureInPictureArgs(Rational paramRational, List<RemoteAction> paramList, Rect paramRect)
  {
    mAspectRatio = paramRational;
    mUserActions = paramList;
    mSourceRectHint = paramRect;
  }
  
  public static PictureInPictureArgs convert(PictureInPictureParams paramPictureInPictureParams)
  {
    return new PictureInPictureArgs(paramPictureInPictureParams.getAspectRatioRational(), paramPictureInPictureParams.getActions(), paramPictureInPictureParams.getSourceRectHint());
  }
  
  public static PictureInPictureParams convert(PictureInPictureArgs paramPictureInPictureArgs)
  {
    return new PictureInPictureParams(paramPictureInPictureArgs.getAspectRatioRational(), paramPictureInPictureArgs.getActions(), paramPictureInPictureArgs.getSourceRectHint());
  }
  
  public void copyOnlySet(PictureInPictureArgs paramPictureInPictureArgs)
  {
    if (paramPictureInPictureArgs.hasSetAspectRatio()) {
      mAspectRatio = mAspectRatio;
    }
    if (paramPictureInPictureArgs.hasSetActions()) {
      mUserActions = mUserActions;
    }
    if (paramPictureInPictureArgs.hasSourceBoundsHint()) {
      mSourceRectHint = new Rect(paramPictureInPictureArgs.getSourceRectHint());
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
  
  public Rect getSourceRectHintInsets()
  {
    return mSourceRectHintInsets;
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
  
  public boolean hasSourceBoundsHintInsets()
  {
    boolean bool;
    if (mSourceRectHintInsets != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public void setActions(List<RemoteAction> paramList)
  {
    if (mUserActions != null) {
      mUserActions = null;
    }
    if (paramList != null) {
      mUserActions = new ArrayList(paramList);
    }
  }
  
  @Deprecated
  public void setAspectRatio(float paramFloat)
  {
    mAspectRatio = new Rational((int)(1.0E9F * paramFloat), 1000000000);
  }
  
  @Deprecated
  public void setSourceRectHint(Rect paramRect)
  {
    if (paramRect == null) {
      mSourceRectHint = null;
    } else {
      mSourceRectHint = new Rect(paramRect);
    }
  }
  
  @Deprecated
  public void setSourceRectHintInsets(Rect paramRect)
  {
    if (paramRect == null) {
      mSourceRectHintInsets = null;
    } else {
      mSourceRectHintInsets = new Rect(paramRect);
    }
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
    
    public PictureInPictureArgs build()
    {
      return new PictureInPictureArgs(mAspectRatio, mUserActions, mSourceRectHint, null);
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
