package android.os;

public final class Messenger
  implements Parcelable
{
  public static final Parcelable.Creator<Messenger> CREATOR = new Parcelable.Creator()
  {
    public Messenger createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel = paramAnonymousParcel.readStrongBinder();
      if (paramAnonymousParcel != null) {
        paramAnonymousParcel = new Messenger(paramAnonymousParcel);
      } else {
        paramAnonymousParcel = null;
      }
      return paramAnonymousParcel;
    }
    
    public Messenger[] newArray(int paramAnonymousInt)
    {
      return new Messenger[paramAnonymousInt];
    }
  };
  private final IMessenger mTarget;
  
  public Messenger(Handler paramHandler)
  {
    mTarget = paramHandler.getIMessenger();
  }
  
  public Messenger(IBinder paramIBinder)
  {
    mTarget = IMessenger.Stub.asInterface(paramIBinder);
  }
  
  public static Messenger readMessengerOrNullFromParcel(Parcel paramParcel)
  {
    paramParcel = paramParcel.readStrongBinder();
    if (paramParcel != null) {
      paramParcel = new Messenger(paramParcel);
    } else {
      paramParcel = null;
    }
    return paramParcel;
  }
  
  public static void writeMessengerOrNullToParcel(Messenger paramMessenger, Parcel paramParcel)
  {
    if (paramMessenger != null) {
      paramMessenger = mTarget.asBinder();
    } else {
      paramMessenger = null;
    }
    paramParcel.writeStrongBinder(paramMessenger);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    try
    {
      boolean bool = mTarget.asBinder().equals(mTarget.asBinder());
      return bool;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public IBinder getBinder()
  {
    return mTarget.asBinder();
  }
  
  public int hashCode()
  {
    return mTarget.asBinder().hashCode();
  }
  
  public void send(Message paramMessage)
    throws RemoteException
  {
    mTarget.send(paramMessage);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mTarget.asBinder());
  }
}
