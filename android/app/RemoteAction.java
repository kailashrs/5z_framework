package android.app;

import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.io.PrintWriter;

public final class RemoteAction
  implements Parcelable
{
  public static final Parcelable.Creator<RemoteAction> CREATOR = new Parcelable.Creator()
  {
    public RemoteAction createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RemoteAction(paramAnonymousParcel);
    }
    
    public RemoteAction[] newArray(int paramAnonymousInt)
    {
      return new RemoteAction[paramAnonymousInt];
    }
  };
  private static final String TAG = "RemoteAction";
  private final PendingIntent mActionIntent;
  private final CharSequence mContentDescription;
  private boolean mEnabled;
  private final Icon mIcon;
  private boolean mShouldShowIcon;
  private final CharSequence mTitle;
  
  public RemoteAction(Icon paramIcon, CharSequence paramCharSequence1, CharSequence paramCharSequence2, PendingIntent paramPendingIntent)
  {
    if ((paramIcon != null) && (paramCharSequence1 != null) && (paramCharSequence2 != null) && (paramPendingIntent != null))
    {
      mIcon = paramIcon;
      mTitle = paramCharSequence1;
      mContentDescription = paramCharSequence2;
      mActionIntent = paramPendingIntent;
      mEnabled = true;
      mShouldShowIcon = true;
      return;
    }
    throw new IllegalArgumentException("Expected icon, title, content description and action callback");
  }
  
  RemoteAction(Parcel paramParcel)
  {
    mIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
    mTitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mContentDescription = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mActionIntent = ((PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel));
    mEnabled = paramParcel.readBoolean();
    mShouldShowIcon = paramParcel.readBoolean();
  }
  
  public RemoteAction clone()
  {
    RemoteAction localRemoteAction = new RemoteAction(mIcon, mTitle, mContentDescription, mActionIntent);
    localRemoteAction.setEnabled(mEnabled);
    localRemoteAction.setShouldShowIcon(mShouldShowIcon);
    return localRemoteAction;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramString = new StringBuilder();
    paramString.append("title=");
    paramString.append(mTitle);
    paramPrintWriter.print(paramString.toString());
    paramString = new StringBuilder();
    paramString.append(" enabled=");
    paramString.append(mEnabled);
    paramPrintWriter.print(paramString.toString());
    paramString = new StringBuilder();
    paramString.append(" contentDescription=");
    paramString.append(mContentDescription);
    paramPrintWriter.print(paramString.toString());
    paramString = new StringBuilder();
    paramString.append(" icon=");
    paramString.append(mIcon);
    paramPrintWriter.print(paramString.toString());
    paramString = new StringBuilder();
    paramString.append(" action=");
    paramString.append(mActionIntent.getIntent());
    paramPrintWriter.print(paramString.toString());
    paramString = new StringBuilder();
    paramString.append(" shouldShowIcon=");
    paramString.append(mShouldShowIcon);
    paramPrintWriter.print(paramString.toString());
    paramPrintWriter.println();
  }
  
  public PendingIntent getActionIntent()
  {
    return mActionIntent;
  }
  
  public CharSequence getContentDescription()
  {
    return mContentDescription;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public boolean isEnabled()
  {
    return mEnabled;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    mEnabled = paramBoolean;
  }
  
  public void setShouldShowIcon(boolean paramBoolean)
  {
    mShouldShowIcon = paramBoolean;
  }
  
  public boolean shouldShowIcon()
  {
    return mShouldShowIcon;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mIcon.writeToParcel(paramParcel, 0);
    TextUtils.writeToParcel(mTitle, paramParcel, paramInt);
    TextUtils.writeToParcel(mContentDescription, paramParcel, paramInt);
    mActionIntent.writeToParcel(paramParcel, paramInt);
    paramParcel.writeBoolean(mEnabled);
    paramParcel.writeBoolean(mShouldShowIcon);
  }
}
