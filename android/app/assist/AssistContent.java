package android.app.assist;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AssistContent
  implements Parcelable
{
  public static final Parcelable.Creator<AssistContent> CREATOR = new Parcelable.Creator()
  {
    public AssistContent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AssistContent(paramAnonymousParcel);
    }
    
    public AssistContent[] newArray(int paramAnonymousInt)
    {
      return new AssistContent[paramAnonymousInt];
    }
  };
  private ClipData mClipData;
  private final Bundle mExtras;
  private Intent mIntent;
  private boolean mIsAppProvidedIntent;
  private boolean mIsAppProvidedWebUri;
  private String mStructuredData;
  private Uri mUri;
  
  public AssistContent()
  {
    mIsAppProvidedIntent = false;
    mIsAppProvidedWebUri = false;
    mExtras = new Bundle();
  }
  
  AssistContent(Parcel paramParcel)
  {
    boolean bool1 = false;
    mIsAppProvidedIntent = false;
    mIsAppProvidedWebUri = false;
    if (paramParcel.readInt() != 0) {
      mIntent = ((Intent)Intent.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      mClipData = ((ClipData)ClipData.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      mUri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      mStructuredData = paramParcel.readString();
    }
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsAppProvidedIntent = bool2;
    mExtras = paramParcel.readBundle();
    boolean bool2 = bool1;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    }
    mIsAppProvidedWebUri = bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ClipData getClipData()
  {
    return mClipData;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public Intent getIntent()
  {
    return mIntent;
  }
  
  public String getStructuredData()
  {
    return mStructuredData;
  }
  
  public Uri getWebUri()
  {
    return mUri;
  }
  
  public boolean isAppProvidedIntent()
  {
    return mIsAppProvidedIntent;
  }
  
  public boolean isAppProvidedWebUri()
  {
    return mIsAppProvidedWebUri;
  }
  
  public void setClipData(ClipData paramClipData)
  {
    mClipData = paramClipData;
  }
  
  public void setDefaultIntent(Intent paramIntent)
  {
    mIntent = paramIntent;
    mIsAppProvidedIntent = false;
    mIsAppProvidedWebUri = false;
    mUri = null;
    if ((paramIntent != null) && ("android.intent.action.VIEW".equals(paramIntent.getAction())))
    {
      paramIntent = paramIntent.getData();
      if ((paramIntent != null) && (("http".equals(paramIntent.getScheme())) || ("https".equals(paramIntent.getScheme())))) {
        mUri = paramIntent;
      }
    }
  }
  
  public void setIntent(Intent paramIntent)
  {
    mIsAppProvidedIntent = true;
    mIntent = paramIntent;
  }
  
  public void setStructuredData(String paramString)
  {
    mStructuredData = paramString;
  }
  
  public void setWebUri(Uri paramUri)
  {
    mIsAppProvidedWebUri = true;
    mUri = paramUri;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    if (mIntent != null)
    {
      paramParcel.writeInt(1);
      mIntent.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mClipData != null)
    {
      paramParcel.writeInt(1);
      mClipData.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mUri != null)
    {
      paramParcel.writeInt(1);
      mUri.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mStructuredData != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(mStructuredData);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mIsAppProvidedIntent);
    paramParcel.writeBundle(mExtras);
    paramParcel.writeInt(mIsAppProvidedWebUri);
  }
}
