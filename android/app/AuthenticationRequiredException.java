package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public final class AuthenticationRequiredException
  extends SecurityException
  implements Parcelable
{
  public static final Parcelable.Creator<AuthenticationRequiredException> CREATOR = new Parcelable.Creator()
  {
    public AuthenticationRequiredException createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AuthenticationRequiredException(paramAnonymousParcel);
    }
    
    public AuthenticationRequiredException[] newArray(int paramAnonymousInt)
    {
      return new AuthenticationRequiredException[paramAnonymousInt];
    }
  };
  private static final String TAG = "AuthenticationRequiredException";
  private final PendingIntent mUserAction;
  
  public AuthenticationRequiredException(Parcel paramParcel)
  {
    this(new SecurityException(paramParcel.readString()), (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel));
  }
  
  public AuthenticationRequiredException(Throwable paramThrowable, PendingIntent paramPendingIntent)
  {
    super(paramThrowable.getMessage());
    mUserAction = ((PendingIntent)Preconditions.checkNotNull(paramPendingIntent));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PendingIntent getUserAction()
  {
    return mUserAction;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(getMessage());
    mUserAction.writeToParcel(paramParcel, paramInt);
  }
}
