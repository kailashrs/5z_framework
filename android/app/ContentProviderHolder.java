package android.app;

import android.content.ContentProviderNative;
import android.content.IContentProvider;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ContentProviderHolder
  implements Parcelable
{
  public static final Parcelable.Creator<ContentProviderHolder> CREATOR = new Parcelable.Creator()
  {
    public ContentProviderHolder createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ContentProviderHolder(paramAnonymousParcel, null);
    }
    
    public ContentProviderHolder[] newArray(int paramAnonymousInt)
    {
      return new ContentProviderHolder[paramAnonymousInt];
    }
  };
  public IBinder connection;
  public final ProviderInfo info;
  public boolean noReleaseNeeded;
  public IContentProvider provider;
  
  public ContentProviderHolder(ProviderInfo paramProviderInfo)
  {
    info = paramProviderInfo;
  }
  
  private ContentProviderHolder(Parcel paramParcel)
  {
    info = ((ProviderInfo)ProviderInfo.CREATOR.createFromParcel(paramParcel));
    provider = ContentProviderNative.asInterface(paramParcel.readStrongBinder());
    connection = paramParcel.readStrongBinder();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    noReleaseNeeded = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    info.writeToParcel(paramParcel, 0);
    if (provider != null) {
      paramParcel.writeStrongBinder(provider.asBinder());
    } else {
      paramParcel.writeStrongBinder(null);
    }
    paramParcel.writeStrongBinder(connection);
    paramParcel.writeInt(noReleaseNeeded);
  }
}
