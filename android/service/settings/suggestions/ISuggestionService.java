package android.service.settings.suggestions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface ISuggestionService
  extends IInterface
{
  public abstract void dismissSuggestion(Suggestion paramSuggestion)
    throws RemoteException;
  
  public abstract List<Suggestion> getSuggestions()
    throws RemoteException;
  
  public abstract void launchSuggestion(Suggestion paramSuggestion)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISuggestionService
  {
    private static final String DESCRIPTOR = "android.service.settings.suggestions.ISuggestionService";
    static final int TRANSACTION_dismissSuggestion = 3;
    static final int TRANSACTION_getSuggestions = 2;
    static final int TRANSACTION_launchSuggestion = 4;
    
    public Stub()
    {
      attachInterface(this, "android.service.settings.suggestions.ISuggestionService");
    }
    
    public static ISuggestionService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.settings.suggestions.ISuggestionService");
      if ((localIInterface != null) && ((localIInterface instanceof ISuggestionService))) {
        return (ISuggestionService)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.service.settings.suggestions.ISuggestionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Suggestion)Suggestion.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          launchSuggestion(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.settings.suggestions.ISuggestionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Suggestion)Suggestion.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          dismissSuggestion(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.service.settings.suggestions.ISuggestionService");
        paramParcel1 = getSuggestions();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.service.settings.suggestions.ISuggestionService");
      return true;
    }
    
    private static class Proxy
      implements ISuggestionService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void dismissSuggestion(Suggestion paramSuggestion)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.settings.suggestions.ISuggestionService");
          if (paramSuggestion != null)
          {
            localParcel1.writeInt(1);
            paramSuggestion.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.settings.suggestions.ISuggestionService";
      }
      
      public List<Suggestion> getSuggestions()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.settings.suggestions.ISuggestionService");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(Suggestion.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void launchSuggestion(Suggestion paramSuggestion)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.settings.suggestions.ISuggestionService");
          if (paramSuggestion != null)
          {
            localParcel1.writeInt(1);
            paramSuggestion.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
