package android.service.textclassifier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.TextClassification.Request;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationSessionId;
import android.view.textclassifier.TextLinks.Request;
import android.view.textclassifier.TextSelection.Request;

public abstract interface ITextClassifierService
  extends IInterface
{
  public abstract void onClassifyText(TextClassificationSessionId paramTextClassificationSessionId, TextClassification.Request paramRequest, ITextClassificationCallback paramITextClassificationCallback)
    throws RemoteException;
  
  public abstract void onCreateTextClassificationSession(TextClassificationContext paramTextClassificationContext, TextClassificationSessionId paramTextClassificationSessionId)
    throws RemoteException;
  
  public abstract void onDestroyTextClassificationSession(TextClassificationSessionId paramTextClassificationSessionId)
    throws RemoteException;
  
  public abstract void onGenerateLinks(TextClassificationSessionId paramTextClassificationSessionId, TextLinks.Request paramRequest, ITextLinksCallback paramITextLinksCallback)
    throws RemoteException;
  
  public abstract void onSelectionEvent(TextClassificationSessionId paramTextClassificationSessionId, SelectionEvent paramSelectionEvent)
    throws RemoteException;
  
  public abstract void onSuggestSelection(TextClassificationSessionId paramTextClassificationSessionId, TextSelection.Request paramRequest, ITextSelectionCallback paramITextSelectionCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextClassifierService
  {
    private static final String DESCRIPTOR = "android.service.textclassifier.ITextClassifierService";
    static final int TRANSACTION_onClassifyText = 2;
    static final int TRANSACTION_onCreateTextClassificationSession = 5;
    static final int TRANSACTION_onDestroyTextClassificationSession = 6;
    static final int TRANSACTION_onGenerateLinks = 3;
    static final int TRANSACTION_onSelectionEvent = 4;
    static final int TRANSACTION_onSuggestSelection = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.textclassifier.ITextClassifierService");
    }
    
    public static ITextClassifierService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.textclassifier.ITextClassifierService");
      if ((localIInterface != null) && ((localIInterface instanceof ITextClassifierService))) {
        return (ITextClassifierService)localIInterface;
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
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.service.textclassifier.ITextClassifierService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TextClassificationSessionId)TextClassificationSessionId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          onDestroyTextClassificationSession(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.textclassifier.ITextClassifierService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (TextClassificationContext)TextClassificationContext.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TextClassificationSessionId)TextClassificationSessionId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          onCreateTextClassificationSession(paramParcel2, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.textclassifier.ITextClassifierService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (TextClassificationSessionId)TextClassificationSessionId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SelectionEvent)SelectionEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onSelectionEvent(paramParcel2, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.textclassifier.ITextClassifierService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (TextClassificationSessionId)TextClassificationSessionId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (TextLinks.Request)TextLinks.Request.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject3;
          }
          onGenerateLinks(paramParcel2, (TextLinks.Request)localObject1, ITextLinksCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.textclassifier.ITextClassifierService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (TextClassificationSessionId)TextClassificationSessionId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (TextClassification.Request)TextClassification.Request.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject4;
          }
          onClassifyText(paramParcel2, (TextClassification.Request)localObject1, ITextClassificationCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.service.textclassifier.ITextClassifierService");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (TextClassificationSessionId)TextClassificationSessionId.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localObject1 = (TextSelection.Request)TextSelection.Request.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject1 = localObject5;
        }
        onSuggestSelection(paramParcel2, (TextSelection.Request)localObject1, ITextSelectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.service.textclassifier.ITextClassifierService");
      return true;
    }
    
    private static class Proxy
      implements ITextClassifierService
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
      
      public String getInterfaceDescriptor()
      {
        return "android.service.textclassifier.ITextClassifierService";
      }
      
      public void onClassifyText(TextClassificationSessionId paramTextClassificationSessionId, TextClassification.Request paramRequest, ITextClassificationCallback paramITextClassificationCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassifierService");
          if (paramTextClassificationSessionId != null)
          {
            localParcel.writeInt(1);
            paramTextClassificationSessionId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramRequest != null)
          {
            localParcel.writeInt(1);
            paramRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramITextClassificationCallback != null) {
            paramTextClassificationSessionId = paramITextClassificationCallback.asBinder();
          } else {
            paramTextClassificationSessionId = null;
          }
          localParcel.writeStrongBinder(paramTextClassificationSessionId);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCreateTextClassificationSession(TextClassificationContext paramTextClassificationContext, TextClassificationSessionId paramTextClassificationSessionId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassifierService");
          if (paramTextClassificationContext != null)
          {
            localParcel.writeInt(1);
            paramTextClassificationContext.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramTextClassificationSessionId != null)
          {
            localParcel.writeInt(1);
            paramTextClassificationSessionId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDestroyTextClassificationSession(TextClassificationSessionId paramTextClassificationSessionId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassifierService");
          if (paramTextClassificationSessionId != null)
          {
            localParcel.writeInt(1);
            paramTextClassificationSessionId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGenerateLinks(TextClassificationSessionId paramTextClassificationSessionId, TextLinks.Request paramRequest, ITextLinksCallback paramITextLinksCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassifierService");
          if (paramTextClassificationSessionId != null)
          {
            localParcel.writeInt(1);
            paramTextClassificationSessionId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramRequest != null)
          {
            localParcel.writeInt(1);
            paramRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramITextLinksCallback != null) {
            paramTextClassificationSessionId = paramITextLinksCallback.asBinder();
          } else {
            paramTextClassificationSessionId = null;
          }
          localParcel.writeStrongBinder(paramTextClassificationSessionId);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSelectionEvent(TextClassificationSessionId paramTextClassificationSessionId, SelectionEvent paramSelectionEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassifierService");
          if (paramTextClassificationSessionId != null)
          {
            localParcel.writeInt(1);
            paramTextClassificationSessionId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramSelectionEvent != null)
          {
            localParcel.writeInt(1);
            paramSelectionEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSuggestSelection(TextClassificationSessionId paramTextClassificationSessionId, TextSelection.Request paramRequest, ITextSelectionCallback paramITextSelectionCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassifierService");
          if (paramTextClassificationSessionId != null)
          {
            localParcel.writeInt(1);
            paramTextClassificationSessionId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramRequest != null)
          {
            localParcel.writeInt(1);
            paramRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramITextSelectionCallback != null) {
            paramTextClassificationSessionId = paramITextSelectionCallback.asBinder();
          } else {
            paramTextClassificationSessionId = null;
          }
          localParcel.writeStrongBinder(paramTextClassificationSessionId);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
