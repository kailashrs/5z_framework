package android.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BulkCursorDescriptor
  implements Parcelable
{
  public static final Parcelable.Creator<BulkCursorDescriptor> CREATOR = new Parcelable.Creator()
  {
    public BulkCursorDescriptor createFromParcel(Parcel paramAnonymousParcel)
    {
      BulkCursorDescriptor localBulkCursorDescriptor = new BulkCursorDescriptor();
      localBulkCursorDescriptor.readFromParcel(paramAnonymousParcel);
      return localBulkCursorDescriptor;
    }
    
    public BulkCursorDescriptor[] newArray(int paramAnonymousInt)
    {
      return new BulkCursorDescriptor[paramAnonymousInt];
    }
  };
  public String[] columnNames;
  public int count;
  public IBulkCursor cursor;
  public boolean wantsAllOnMoveCalls;
  public CursorWindow window;
  
  public BulkCursorDescriptor() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    cursor = BulkCursorNative.asInterface(paramParcel.readStrongBinder());
    columnNames = paramParcel.readStringArray();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    wantsAllOnMoveCalls = bool;
    count = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      window = ((CursorWindow)CursorWindow.CREATOR.createFromParcel(paramParcel));
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(cursor.asBinder());
    paramParcel.writeStringArray(columnNames);
    paramParcel.writeInt(wantsAllOnMoveCalls);
    paramParcel.writeInt(count);
    if (window != null)
    {
      paramParcel.writeInt(1);
      window.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
