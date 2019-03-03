package android.view.inputmethod;

import android.content.ClipDescription;
import android.content.ContentProvider;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import com.android.internal.inputmethod.IInputContentUriToken;
import com.android.internal.inputmethod.IInputContentUriToken.Stub;
import java.security.InvalidParameterException;

public final class InputContentInfo
  implements Parcelable
{
  public static final Parcelable.Creator<InputContentInfo> CREATOR = new Parcelable.Creator()
  {
    public InputContentInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InputContentInfo(paramAnonymousParcel, null);
    }
    
    public InputContentInfo[] newArray(int paramAnonymousInt)
    {
      return new InputContentInfo[paramAnonymousInt];
    }
  };
  private final Uri mContentUri;
  private final int mContentUriOwnerUserId;
  private final ClipDescription mDescription;
  private final Uri mLinkUri;
  private IInputContentUriToken mUriToken;
  
  public InputContentInfo(Uri paramUri, ClipDescription paramClipDescription)
  {
    this(paramUri, paramClipDescription, null);
  }
  
  public InputContentInfo(Uri paramUri1, ClipDescription paramClipDescription, Uri paramUri2)
  {
    validateInternal(paramUri1, paramClipDescription, paramUri2, true);
    mContentUri = paramUri1;
    mContentUriOwnerUserId = ContentProvider.getUserIdFromUri(mContentUri, UserHandle.myUserId());
    mDescription = paramClipDescription;
    mLinkUri = paramUri2;
  }
  
  private InputContentInfo(Parcel paramParcel)
  {
    mContentUri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    mContentUriOwnerUserId = paramParcel.readInt();
    mDescription = ((ClipDescription)ClipDescription.CREATOR.createFromParcel(paramParcel));
    mLinkUri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    if (paramParcel.readInt() == 1) {
      mUriToken = IInputContentUriToken.Stub.asInterface(paramParcel.readStrongBinder());
    } else {
      mUriToken = null;
    }
  }
  
  private static boolean validateInternal(Uri paramUri1, ClipDescription paramClipDescription, Uri paramUri2, boolean paramBoolean)
  {
    if (paramUri1 == null)
    {
      if (!paramBoolean) {
        return false;
      }
      throw new NullPointerException("contentUri");
    }
    if (paramClipDescription == null)
    {
      if (!paramBoolean) {
        return false;
      }
      throw new NullPointerException("description");
    }
    if (!"content".equals(paramUri1.getScheme()))
    {
      if (!paramBoolean) {
        return false;
      }
      throw new InvalidParameterException("contentUri must have content scheme");
    }
    if (paramUri2 != null)
    {
      paramUri1 = paramUri2.getScheme();
      if ((paramUri1 == null) || ((!paramUri1.equalsIgnoreCase("http")) && (!paramUri1.equalsIgnoreCase("https"))))
      {
        if (!paramBoolean) {
          return false;
        }
        throw new InvalidParameterException("linkUri must have either http or https scheme");
      }
    }
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Uri getContentUri()
  {
    if (mContentUriOwnerUserId != UserHandle.myUserId()) {
      return ContentProvider.maybeAddUserId(mContentUri, mContentUriOwnerUserId);
    }
    return mContentUri;
  }
  
  public ClipDescription getDescription()
  {
    return mDescription;
  }
  
  public Uri getLinkUri()
  {
    return mLinkUri;
  }
  
  public void releasePermission()
  {
    if (mUriToken == null) {
      return;
    }
    try
    {
      mUriToken.release();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void requestPermission()
  {
    if (mUriToken == null) {
      return;
    }
    try
    {
      mUriToken.take();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
  }
  
  void setUriToken(IInputContentUriToken paramIInputContentUriToken)
  {
    if (mUriToken == null)
    {
      mUriToken = paramIInputContentUriToken;
      return;
    }
    throw new IllegalStateException("URI token is already set");
  }
  
  public boolean validate()
  {
    return validateInternal(mContentUri, mDescription, mLinkUri, false);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Uri.writeToParcel(paramParcel, mContentUri);
    paramParcel.writeInt(mContentUriOwnerUserId);
    mDescription.writeToParcel(paramParcel, paramInt);
    Uri.writeToParcel(paramParcel, mLinkUri);
    if (mUriToken != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeStrongBinder(mUriToken.asBinder());
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
