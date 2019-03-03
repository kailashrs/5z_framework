package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

abstract class BaseParceledListSlice<T>
  implements Parcelable
{
  private static boolean DEBUG = false;
  private static final int MAX_IPC_SIZE = 65536;
  private static String TAG = "ParceledListSlice";
  private int mInlineCountLimit = Integer.MAX_VALUE;
  private final List<T> mList;
  
  BaseParceledListSlice(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    int i = paramParcel.readInt();
    mList = new ArrayList(i);
    if (DEBUG)
    {
      localObject1 = TAG;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Retrieving ");
      ((StringBuilder)localObject2).append(i);
      ((StringBuilder)localObject2).append(" items");
      Log.d((String)localObject1, ((StringBuilder)localObject2).toString());
    }
    if (i <= 0) {
      return;
    }
    Object localObject2 = readParcelableCreator(paramParcel, paramClassLoader);
    Object localObject1 = null;
    Object localObject3;
    Object localObject4;
    for (int j = 0; (j < i) && (paramParcel.readInt() != 0); j++)
    {
      localObject3 = readCreator((Parcelable.Creator)localObject2, paramParcel, paramClassLoader);
      if (localObject1 == null) {
        localObject1 = localObject3.getClass();
      } else {
        verifySameType((Class)localObject1, localObject3.getClass());
      }
      mList.add(localObject3);
      if (DEBUG)
      {
        localObject4 = TAG;
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Read inline #");
        ((StringBuilder)localObject3).append(j);
        ((StringBuilder)localObject3).append(": ");
        ((StringBuilder)localObject3).append(mList.get(mList.size() - 1));
        Log.d((String)localObject4, ((StringBuilder)localObject3).toString());
      }
    }
    if (j >= i) {
      return;
    }
    paramParcel = paramParcel.readStrongBinder();
    while (j < i)
    {
      if (DEBUG)
      {
        localObject4 = TAG;
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Reading more @");
        ((StringBuilder)localObject3).append(j);
        ((StringBuilder)localObject3).append(" of ");
        ((StringBuilder)localObject3).append(i);
        ((StringBuilder)localObject3).append(": retriever=");
        ((StringBuilder)localObject3).append(paramParcel);
        Log.d((String)localObject4, ((StringBuilder)localObject3).toString());
      }
      localObject4 = Parcel.obtain();
      localObject3 = Parcel.obtain();
      ((Parcel)localObject4).writeInt(j);
      try
      {
        paramParcel.transact(1, (Parcel)localObject4, (Parcel)localObject3, 0);
        while ((j < i) && (((Parcel)localObject3).readInt() != 0))
        {
          Object localObject5 = readCreator((Parcelable.Creator)localObject2, (Parcel)localObject3, paramClassLoader);
          verifySameType((Class)localObject1, localObject5.getClass());
          mList.add(localObject5);
          if (DEBUG)
          {
            String str = TAG;
            localObject5 = new StringBuilder();
            ((StringBuilder)localObject5).append("Read extra #");
            ((StringBuilder)localObject5).append(j);
            ((StringBuilder)localObject5).append(": ");
            ((StringBuilder)localObject5).append(mList.get(mList.size() - 1));
            Log.d(str, ((StringBuilder)localObject5).toString());
          }
          j++;
        }
        ((Parcel)localObject3).recycle();
        ((Parcel)localObject4).recycle();
      }
      catch (RemoteException localRemoteException)
      {
        paramParcel = TAG;
        paramClassLoader = new StringBuilder();
        paramClassLoader.append("Failure retrieving array; only received ");
        paramClassLoader.append(j);
        paramClassLoader.append(" of ");
        paramClassLoader.append(i);
        Log.w(paramParcel, paramClassLoader.toString(), localRemoteException);
        return;
      }
    }
  }
  
  public BaseParceledListSlice(List<T> paramList)
  {
    mList = paramList;
  }
  
  private T readCreator(Parcelable.Creator<?> paramCreator, Parcel paramParcel, ClassLoader paramClassLoader)
  {
    if ((paramCreator instanceof Parcelable.ClassLoaderCreator)) {
      return ((Parcelable.ClassLoaderCreator)paramCreator).createFromParcel(paramParcel, paramClassLoader);
    }
    return paramCreator.createFromParcel(paramParcel);
  }
  
  private static void verifySameType(Class<?> paramClass1, Class<?> paramClass2)
  {
    if (paramClass2.equals(paramClass1)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Can't unparcel type ");
    localStringBuilder.append(paramClass2.getName());
    localStringBuilder.append(" in list of type ");
    localStringBuilder.append(paramClass1.getName());
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public List<T> getList()
  {
    return mList;
  }
  
  protected abstract Parcelable.Creator<?> readParcelableCreator(Parcel paramParcel, ClassLoader paramClassLoader);
  
  public void setInlineCountLimit(int paramInt)
  {
    mInlineCountLimit = paramInt;
  }
  
  protected abstract void writeElement(T paramT, Parcel paramParcel, int paramInt);
  
  protected abstract void writeParcelableCreator(T paramT, Parcel paramParcel);
  
  public void writeToParcel(Parcel paramParcel, final int paramInt)
  {
    final int i = mList.size();
    paramParcel.writeInt(i);
    Object localObject1;
    Object localObject2;
    if (DEBUG)
    {
      localObject1 = TAG;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Writing ");
      ((StringBuilder)localObject2).append(i);
      ((StringBuilder)localObject2).append(" items");
      Log.d((String)localObject1, ((StringBuilder)localObject2).toString());
    }
    if (i > 0)
    {
      localObject2 = mList.get(0).getClass();
      writeParcelableCreator(mList.get(0), paramParcel);
      String str;
      for (int j = 0; (j < i) && (j < mInlineCountLimit) && (paramParcel.dataSize() < 65536); j++)
      {
        paramParcel.writeInt(1);
        localObject1 = mList.get(j);
        verifySameType((Class)localObject2, localObject1.getClass());
        writeElement(localObject1, paramParcel, paramInt);
        if (DEBUG)
        {
          str = TAG;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Wrote inline #");
          ((StringBuilder)localObject1).append(j);
          ((StringBuilder)localObject1).append(": ");
          ((StringBuilder)localObject1).append(mList.get(j));
          Log.d(str, ((StringBuilder)localObject1).toString());
        }
      }
      if (j < i)
      {
        paramParcel.writeInt(0);
        localObject2 = new Binder()
        {
          protected boolean onTransact(int paramAnonymousInt1, Parcel paramAnonymousParcel1, Parcel paramAnonymousParcel2, int paramAnonymousInt2)
            throws RemoteException
          {
            if (paramAnonymousInt1 != 1) {
              return super.onTransact(paramAnonymousInt1, paramAnonymousParcel1, paramAnonymousParcel2, paramAnonymousInt2);
            }
            paramAnonymousInt2 = paramAnonymousParcel1.readInt();
            paramAnonymousInt1 = paramAnonymousInt2;
            Object localObject;
            if (BaseParceledListSlice.DEBUG)
            {
              localObject = BaseParceledListSlice.TAG;
              paramAnonymousParcel1 = new StringBuilder();
              paramAnonymousParcel1.append("Writing more @");
              paramAnonymousParcel1.append(paramAnonymousInt2);
              paramAnonymousParcel1.append(" of ");
              paramAnonymousParcel1.append(i);
              Log.d((String)localObject, paramAnonymousParcel1.toString());
            }
            for (paramAnonymousInt1 = paramAnonymousInt2; (paramAnonymousInt1 < i) && (paramAnonymousParcel2.dataSize() < 65536); paramAnonymousInt1++)
            {
              paramAnonymousParcel2.writeInt(1);
              paramAnonymousParcel1 = mList.get(paramAnonymousInt1);
              BaseParceledListSlice.verifySameType(val$listElementClass, paramAnonymousParcel1.getClass());
              writeElement(paramAnonymousParcel1, paramAnonymousParcel2, paramInt);
              if (BaseParceledListSlice.DEBUG)
              {
                localObject = BaseParceledListSlice.TAG;
                paramAnonymousParcel1 = new StringBuilder();
                paramAnonymousParcel1.append("Wrote extra #");
                paramAnonymousParcel1.append(paramAnonymousInt1);
                paramAnonymousParcel1.append(": ");
                paramAnonymousParcel1.append(mList.get(paramAnonymousInt1));
                Log.d((String)localObject, paramAnonymousParcel1.toString());
              }
            }
            if (paramAnonymousInt1 < i)
            {
              if (BaseParceledListSlice.DEBUG)
              {
                paramAnonymousParcel1 = BaseParceledListSlice.TAG;
                localObject = new StringBuilder();
                ((StringBuilder)localObject).append("Breaking @");
                ((StringBuilder)localObject).append(paramAnonymousInt1);
                ((StringBuilder)localObject).append(" of ");
                ((StringBuilder)localObject).append(i);
                Log.d(paramAnonymousParcel1, ((StringBuilder)localObject).toString());
              }
              paramAnonymousParcel2.writeInt(0);
            }
            return true;
          }
        };
        if (DEBUG)
        {
          str = TAG;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Breaking @");
          ((StringBuilder)localObject1).append(j);
          ((StringBuilder)localObject1).append(" of ");
          ((StringBuilder)localObject1).append(i);
          ((StringBuilder)localObject1).append(": retriever=");
          ((StringBuilder)localObject1).append(localObject2);
          Log.d(str, ((StringBuilder)localObject1).toString());
        }
        paramParcel.writeStrongBinder((IBinder)localObject2);
      }
    }
  }
}
