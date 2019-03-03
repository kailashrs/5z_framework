package android.nfc;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;

public final class BeamShareData
  implements Parcelable
{
  public static final Parcelable.Creator<BeamShareData> CREATOR = new Parcelable.Creator()
  {
    public BeamShareData createFromParcel(Parcel paramAnonymousParcel)
    {
      Uri[] arrayOfUri = null;
      NdefMessage localNdefMessage = (NdefMessage)paramAnonymousParcel.readParcelable(NdefMessage.class.getClassLoader());
      int i = paramAnonymousParcel.readInt();
      if (i > 0)
      {
        arrayOfUri = new Uri[i];
        paramAnonymousParcel.readTypedArray(arrayOfUri, Uri.CREATOR);
      }
      return new BeamShareData(localNdefMessage, arrayOfUri, (UserHandle)paramAnonymousParcel.readParcelable(UserHandle.class.getClassLoader()), paramAnonymousParcel.readInt());
    }
    
    public BeamShareData[] newArray(int paramAnonymousInt)
    {
      return new BeamShareData[paramAnonymousInt];
    }
  };
  public final int flags;
  public final NdefMessage ndefMessage;
  public final Uri[] uris;
  public final UserHandle userHandle;
  
  public BeamShareData(NdefMessage paramNdefMessage, Uri[] paramArrayOfUri, UserHandle paramUserHandle, int paramInt)
  {
    ndefMessage = paramNdefMessage;
    uris = paramArrayOfUri;
    userHandle = paramUserHandle;
    flags = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (uris != null) {
      paramInt = uris.length;
    } else {
      paramInt = 0;
    }
    paramParcel.writeParcelable(ndefMessage, 0);
    paramParcel.writeInt(paramInt);
    if (paramInt > 0) {
      paramParcel.writeTypedArray(uris, 0);
    }
    paramParcel.writeParcelable(userHandle, 0);
    paramParcel.writeInt(flags);
  }
}
