package android.app;

import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Person
  implements Parcelable
{
  public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator()
  {
    public Person createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Person(paramAnonymousParcel, null);
    }
    
    public Person[] newArray(int paramAnonymousInt)
    {
      return new Person[paramAnonymousInt];
    }
  };
  private Icon mIcon;
  private boolean mIsBot;
  private boolean mIsImportant;
  private String mKey;
  private CharSequence mName;
  private String mUri;
  
  private Person(Builder paramBuilder)
  {
    mName = mName;
    mIcon = mIcon;
    mUri = mUri;
    mKey = mKey;
    mIsBot = mIsBot;
    mIsImportant = mIsImportant;
  }
  
  private Person(Parcel paramParcel)
  {
    mName = paramParcel.readCharSequence();
    if (paramParcel.readInt() != 0) {
      mIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
    }
    mUri = paramParcel.readString();
    mKey = paramParcel.readString();
    mIsImportant = paramParcel.readBoolean();
    mIsBot = paramParcel.readBoolean();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public String getKey()
  {
    return mKey;
  }
  
  public CharSequence getName()
  {
    return mName;
  }
  
  public String getUri()
  {
    return mUri;
  }
  
  public boolean isBot()
  {
    return mIsBot;
  }
  
  public boolean isImportant()
  {
    return mIsImportant;
  }
  
  public String resolveToLegacyUri()
  {
    if (mUri != null) {
      return mUri;
    }
    if (mName != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("name:");
      localStringBuilder.append(mName);
      return localStringBuilder.toString();
    }
    return "";
  }
  
  public Builder toBuilder()
  {
    return new Builder(this, null);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeCharSequence(mName);
    if (mIcon != null)
    {
      paramParcel.writeInt(1);
      mIcon.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeString(mUri);
    paramParcel.writeString(mKey);
    paramParcel.writeBoolean(mIsImportant);
    paramParcel.writeBoolean(mIsBot);
  }
  
  public static class Builder
  {
    private Icon mIcon;
    private boolean mIsBot;
    private boolean mIsImportant;
    private String mKey;
    private CharSequence mName;
    private String mUri;
    
    public Builder() {}
    
    private Builder(Person paramPerson)
    {
      mName = mName;
      mIcon = mIcon;
      mUri = mUri;
      mKey = mKey;
      mIsBot = mIsBot;
      mIsImportant = mIsImportant;
    }
    
    public Person build()
    {
      return new Person(this, null);
    }
    
    public Builder setBot(boolean paramBoolean)
    {
      mIsBot = paramBoolean;
      return this;
    }
    
    public Builder setIcon(Icon paramIcon)
    {
      mIcon = paramIcon;
      return this;
    }
    
    public Builder setImportant(boolean paramBoolean)
    {
      mIsImportant = paramBoolean;
      return this;
    }
    
    public Builder setKey(String paramString)
    {
      mKey = paramString;
      return this;
    }
    
    public Builder setName(CharSequence paramCharSequence)
    {
      mName = paramCharSequence;
      return this;
    }
    
    public Builder setUri(String paramString)
    {
      mUri = paramString;
      return this;
    }
  }
}
