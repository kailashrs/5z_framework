package android.service.quicksettings;

import android.graphics.drawable.Icon;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

public final class Tile
  implements Parcelable
{
  public static final Parcelable.Creator<Tile> CREATOR = new Parcelable.Creator()
  {
    public Tile createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Tile(paramAnonymousParcel);
    }
    
    public Tile[] newArray(int paramAnonymousInt)
    {
      return new Tile[paramAnonymousInt];
    }
  };
  public static final int STATE_ACTIVE = 2;
  public static final int STATE_INACTIVE = 1;
  public static final int STATE_UNAVAILABLE = 0;
  private static final String TAG = "Tile";
  private CharSequence mContentDescription;
  private Icon mIcon;
  private CharSequence mLabel;
  private IQSService mService;
  private int mState = 2;
  private IBinder mToken;
  
  public Tile() {}
  
  public Tile(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    if (paramParcel.readByte() != 0) {
      mIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
    } else {
      mIcon = null;
    }
    mState = paramParcel.readInt();
    mLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mContentDescription = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CharSequence getContentDescription()
  {
    return mContentDescription;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public void setContentDescription(CharSequence paramCharSequence)
  {
    mContentDescription = paramCharSequence;
  }
  
  public void setIcon(Icon paramIcon)
  {
    mIcon = paramIcon;
  }
  
  public void setLabel(CharSequence paramCharSequence)
  {
    mLabel = paramCharSequence;
  }
  
  public void setService(IQSService paramIQSService, IBinder paramIBinder)
  {
    mService = paramIQSService;
    mToken = paramIBinder;
  }
  
  public void setState(int paramInt)
  {
    mState = paramInt;
  }
  
  public void updateTile()
  {
    try
    {
      mService.updateQsTile(this, mToken);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("Tile", "Couldn't update tile");
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mIcon != null)
    {
      paramParcel.writeByte((byte)1);
      mIcon.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeInt(mState);
    TextUtils.writeToParcel(mLabel, paramParcel, paramInt);
    TextUtils.writeToParcel(mContentDescription, paramParcel, paramInt);
  }
}
