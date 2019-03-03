package android.content;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class UndoOperation<DATA>
  implements Parcelable
{
  UndoOwner mOwner;
  
  public UndoOperation(UndoOwner paramUndoOwner)
  {
    mOwner = paramUndoOwner;
  }
  
  protected UndoOperation(Parcel paramParcel, ClassLoader paramClassLoader) {}
  
  public boolean allowMerge()
  {
    return true;
  }
  
  public abstract void commit();
  
  public int describeContents()
  {
    return 0;
  }
  
  public UndoOwner getOwner()
  {
    return mOwner;
  }
  
  public DATA getOwnerData()
  {
    return mOwner.getData();
  }
  
  public boolean hasData()
  {
    return true;
  }
  
  public boolean matchOwner(UndoOwner paramUndoOwner)
  {
    boolean bool;
    if (paramUndoOwner == getOwner()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public abstract void redo();
  
  public abstract void undo();
}
