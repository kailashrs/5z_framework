package android.view;

import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public final class KeyboardShortcutInfo
  implements Parcelable
{
  public static final Parcelable.Creator<KeyboardShortcutInfo> CREATOR = new Parcelable.Creator()
  {
    public KeyboardShortcutInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyboardShortcutInfo(paramAnonymousParcel, null);
    }
    
    public KeyboardShortcutInfo[] newArray(int paramAnonymousInt)
    {
      return new KeyboardShortcutInfo[paramAnonymousInt];
    }
  };
  private final char mBaseCharacter;
  private final Icon mIcon;
  private final int mKeycode;
  private final CharSequence mLabel;
  private final int mModifiers;
  
  private KeyboardShortcutInfo(Parcel paramParcel)
  {
    mLabel = paramParcel.readCharSequence();
    mIcon = ((Icon)paramParcel.readParcelable(null));
    mBaseCharacter = ((char)(char)paramParcel.readInt());
    mKeycode = paramParcel.readInt();
    mModifiers = paramParcel.readInt();
  }
  
  public KeyboardShortcutInfo(CharSequence paramCharSequence, char paramChar, int paramInt)
  {
    mLabel = paramCharSequence;
    boolean bool;
    if (paramChar != 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool);
    mBaseCharacter = ((char)paramChar);
    mKeycode = 0;
    mModifiers = paramInt;
    mIcon = null;
  }
  
  public KeyboardShortcutInfo(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    this(paramCharSequence, null, paramInt1, paramInt2);
  }
  
  public KeyboardShortcutInfo(CharSequence paramCharSequence, Icon paramIcon, int paramInt1, int paramInt2)
  {
    mLabel = paramCharSequence;
    mIcon = paramIcon;
    boolean bool1 = false;
    mBaseCharacter = ((char)0);
    boolean bool2 = bool1;
    if (paramInt1 >= 0)
    {
      bool2 = bool1;
      if (paramInt1 <= KeyEvent.getMaxKeyCode()) {
        bool2 = true;
      }
    }
    Preconditions.checkArgument(bool2);
    mKeycode = paramInt1;
    mModifiers = paramInt2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public char getBaseCharacter()
  {
    return mBaseCharacter;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public int getKeycode()
  {
    return mKeycode;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public int getModifiers()
  {
    return mModifiers;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeCharSequence(mLabel);
    paramParcel.writeParcelable(mIcon, 0);
    paramParcel.writeInt(mBaseCharacter);
    paramParcel.writeInt(mKeycode);
    paramParcel.writeInt(mModifiers);
  }
}
