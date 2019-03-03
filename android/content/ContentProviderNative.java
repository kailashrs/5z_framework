package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.BulkCursorDescriptor;
import android.database.Cursor;
import android.database.CursorToBulkCursorAdaptor;
import android.database.DatabaseUtils;
import android.database.IContentObserver;
import android.database.IContentObserver.Stub;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ICancellationSignal.Stub;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;

public abstract class ContentProviderNative
  extends Binder
  implements IContentProvider
{
  public ContentProviderNative()
  {
    attachInterface(this, "android.content.IContentProvider");
  }
  
  public static IContentProvider asInterface(IBinder paramIBinder)
  {
    if (paramIBinder == null) {
      return null;
    }
    IContentProvider localIContentProvider = (IContentProvider)paramIBinder.queryLocalInterface("android.content.IContentProvider");
    if (localIContentProvider != null) {
      return localIContentProvider;
    }
    return new ContentProviderProxy(paramIBinder);
  }
  
  public IBinder asBinder()
  {
    return this;
  }
  
  public abstract String getProviderName();
  
  public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
    throws RemoteException
  {
    if (paramInt1 != 10)
    {
      int i = 0;
      switch (paramInt1)
      {
      default: 
        switch (paramInt1)
        {
        default: 
          switch (paramInt1)
          {
          default: 
            return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
          }
          break;
        }
        break;
      }
      try
      {
        paramParcel1.enforceInterface("android.content.IContentProvider");
        localObject1 = paramParcel1.readString();
        localObject2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
        localObject4 = paramParcel1.readBundle();
        boolean bool = refresh((String)localObject1, (Uri)localObject2, (Bundle)localObject4, ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        if (bool) {
          paramInt1 = i;
        } else {
          paramInt1 = -1;
        }
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      catch (Exception paramParcel1) {}
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramParcel1 = uncanonicalize(paramParcel1.readString(), (Uri)Uri.CREATOR.createFromParcel(paramParcel1));
      paramParcel2.writeNoException();
      Uri.writeToParcel(paramParcel2, paramParcel1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramParcel1 = canonicalize(paramParcel1.readString(), (Uri)Uri.CREATOR.createFromParcel(paramParcel1));
      paramParcel2.writeNoException();
      Uri.writeToParcel(paramParcel2, paramParcel1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramParcel1 = createCancellationSignal();
      paramParcel2.writeNoException();
      paramParcel2.writeStrongBinder(paramParcel1.asBinder());
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      Object localObject4 = paramParcel1.readString();
      Object localObject2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
      Object localObject1 = paramParcel1.readString();
      Object localObject5 = paramParcel1.readBundle();
      paramParcel1 = openTypedAssetFile((String)localObject4, (Uri)localObject2, (String)localObject1, (Bundle)localObject5, ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()));
      paramParcel2.writeNoException();
      if (paramParcel1 != null)
      {
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
      }
      else
      {
        paramParcel2.writeInt(0);
      }
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramParcel1 = getStreamTypes((Uri)Uri.CREATOR.createFromParcel(paramParcel1), paramParcel1.readString());
      paramParcel2.writeNoException();
      paramParcel2.writeStringArray(paramParcel1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramParcel1 = call(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readBundle());
      paramParcel2.writeNoException();
      paramParcel2.writeBundle(paramParcel1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      localObject2 = paramParcel1.readString();
      paramInt2 = paramParcel1.readInt();
      localObject4 = new java/util/ArrayList;
      ((ArrayList)localObject4).<init>(paramInt2);
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
        ((ArrayList)localObject4).add(paramInt1, (ContentProviderOperation)ContentProviderOperation.CREATOR.createFromParcel(paramParcel1));
      }
      paramParcel1 = applyBatch((String)localObject2, (ArrayList)localObject4);
      paramParcel2.writeNoException();
      paramParcel2.writeTypedArray(paramParcel1, 0);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      localObject2 = paramParcel1.readString();
      localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
      localObject4 = paramParcel1.readString();
      paramParcel1 = openAssetFile((String)localObject2, (Uri)localObject1, (String)localObject4, ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()));
      paramParcel2.writeNoException();
      if (paramParcel1 != null)
      {
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
      }
      else
      {
        paramParcel2.writeInt(0);
      }
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      localObject4 = paramParcel1.readString();
      localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
      localObject2 = paramParcel1.readString();
      paramParcel1 = openFile((String)localObject4, (Uri)localObject1, (String)localObject2, ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readStrongBinder());
      paramParcel2.writeNoException();
      if (paramParcel1 != null)
      {
        paramParcel2.writeInt(1);
        paramParcel1.writeToParcel(paramParcel2, 1);
      }
      else
      {
        paramParcel2.writeInt(0);
      }
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramInt1 = bulkInsert(paramParcel1.readString(), (Uri)Uri.CREATOR.createFromParcel(paramParcel1), (ContentValues[])paramParcel1.createTypedArray(ContentValues.CREATOR));
      paramParcel2.writeNoException();
      paramParcel2.writeInt(paramInt1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramInt1 = delete(paramParcel1.readString(), (Uri)Uri.CREATOR.createFromParcel(paramParcel1), paramParcel1.readString(), paramParcel1.readStringArray());
      paramParcel2.writeNoException();
      paramParcel2.writeInt(paramInt1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramParcel1 = insert(paramParcel1.readString(), (Uri)Uri.CREATOR.createFromParcel(paramParcel1), (ContentValues)ContentValues.CREATOR.createFromParcel(paramParcel1));
      paramParcel2.writeNoException();
      Uri.writeToParcel(paramParcel2, paramParcel1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      paramParcel1 = getType((Uri)Uri.CREATOR.createFromParcel(paramParcel1));
      paramParcel2.writeNoException();
      paramParcel2.writeString(paramParcel1);
      return true;
      paramParcel1.enforceInterface("android.content.IContentProvider");
      localObject1 = paramParcel1.readString();
      localObject5 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
      paramInt2 = paramParcel1.readInt();
      localObject4 = null;
      if (paramInt2 > 0)
      {
        localObject2 = new String[paramInt2];
        for (paramInt1 = 0;; paramInt1++)
        {
          localObject4 = localObject2;
          if (paramInt1 >= paramInt2) {
            break;
          }
          localObject2[paramInt1] = paramParcel1.readString();
        }
      }
      localObject2 = paramParcel1.readBundle();
      IContentObserver localIContentObserver = IContentObserver.Stub.asInterface(paramParcel1.readStrongBinder());
      localObject2 = query((String)localObject1, (Uri)localObject5, (String[])localObject4, (Bundle)localObject2, ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()));
      if (localObject2 != null)
      {
        localObject1 = null;
        localObject4 = localObject2;
        paramParcel1 = (Parcel)localObject1;
        try
        {
          localObject5 = new android/database/CursorToBulkCursorAdaptor;
          localObject4 = localObject2;
          paramParcel1 = (Parcel)localObject1;
          ((CursorToBulkCursorAdaptor)localObject5).<init>((Cursor)localObject2, localIContentObserver, getProviderName());
          localObject2 = localObject5;
          localObject1 = null;
          localObject4 = localObject1;
          paramParcel1 = (Parcel)localObject2;
          localObject5 = ((CursorToBulkCursorAdaptor)localObject2).getBulkCursorDescriptor();
          localObject2 = null;
          localObject4 = localObject1;
          paramParcel1 = (Parcel)localObject2;
          paramParcel2.writeNoException();
          localObject4 = localObject1;
          paramParcel1 = (Parcel)localObject2;
          paramParcel2.writeInt(1);
          localObject4 = localObject1;
          paramParcel1 = (Parcel)localObject2;
          ((BulkCursorDescriptor)localObject5).writeToParcel(paramParcel2, 1);
          if (0 != 0) {
            throw new NullPointerException();
          }
          if (0 != 0) {
            throw new NullPointerException();
          }
        }
        finally
        {
          if (paramParcel1 != null) {
            paramParcel1.close();
          }
          if (localObject4 != null) {
            ((Cursor)localObject4).close();
          }
        }
      }
      paramParcel2.writeNoException();
      paramParcel2.writeInt(0);
      return true;
    }
    paramParcel1.enforceInterface("android.content.IContentProvider");
    paramInt1 = update(paramParcel1.readString(), (Uri)Uri.CREATOR.createFromParcel(paramParcel1), (ContentValues)ContentValues.CREATOR.createFromParcel(paramParcel1), paramParcel1.readString(), paramParcel1.readStringArray());
    paramParcel2.writeNoException();
    paramParcel2.writeInt(paramInt1);
    return true;
    DatabaseUtils.writeExceptionToParcel(paramParcel2, paramParcel1);
    return true;
  }
}
