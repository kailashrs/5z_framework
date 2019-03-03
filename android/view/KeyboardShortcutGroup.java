package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class KeyboardShortcutGroup
  implements Parcelable
{
  public static final Parcelable.Creator<KeyboardShortcutGroup> CREATOR = new Parcelable.Creator()
  {
    public KeyboardShortcutGroup createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyboardShortcutGroup(paramAnonymousParcel, null);
    }
    
    public KeyboardShortcutGroup[] newArray(int paramAnonymousInt)
    {
      return new KeyboardShortcutGroup[paramAnonymousInt];
    }
  };
  private final List<KeyboardShortcutInfo> mItems;
  private final CharSequence mLabel;
  private boolean mSystemGroup;
  
  private KeyboardShortcutGroup(Parcel paramParcel)
  {
    mItems = new ArrayList();
    mLabel = paramParcel.readCharSequence();
    paramParcel.readTypedList(mItems, KeyboardShortcutInfo.CREATOR);
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mSystemGroup = bool;
  }
  
  public KeyboardShortcutGroup(CharSequence paramCharSequence)
  {
    this(paramCharSequence, Collections.emptyList());
  }
  
  public KeyboardShortcutGroup(CharSequence paramCharSequence, List<KeyboardShortcutInfo> paramList)
  {
    mLabel = paramCharSequence;
    mItems = new ArrayList((Collection)Preconditions.checkNotNull(paramList));
  }
  
  public KeyboardShortcutGroup(CharSequence paramCharSequence, List<KeyboardShortcutInfo> paramList, boolean paramBoolean)
  {
    mLabel = paramCharSequence;
    mItems = new ArrayList((Collection)Preconditions.checkNotNull(paramList));
    mSystemGroup = paramBoolean;
  }
  
  public KeyboardShortcutGroup(CharSequence paramCharSequence, boolean paramBoolean)
  {
    this(paramCharSequence, Collections.emptyList(), paramBoolean);
  }
  
  public void addItem(KeyboardShortcutInfo paramKeyboardShortcutInfo)
  {
    mItems.add(paramKeyboardShortcutInfo);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<KeyboardShortcutInfo> getItems()
  {
    return mItems;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public boolean isSystemGroup()
  {
    return mSystemGroup;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeCharSequence(mLabel);
    paramParcel.writeTypedList(mItems);
    paramParcel.writeInt(mSystemGroup);
  }
}
