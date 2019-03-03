package android.text.style;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.util.Log;
import android.view.View;

public class URLSpan
  extends ClickableSpan
  implements ParcelableSpan
{
  private final String mURL;
  
  public URLSpan(Parcel paramParcel)
  {
    mURL = paramParcel.readString();
  }
  
  public URLSpan(String paramString)
  {
    mURL = paramString;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 11;
  }
  
  public String getURL()
  {
    return mURL;
  }
  
  public void onClick(View paramView)
  {
    Uri localUri = Uri.parse(getURL());
    Context localContext = paramView.getContext();
    paramView = new Intent("android.intent.action.VIEW", localUri);
    paramView.putExtra("com.android.browser.application_id", localContext.getPackageName());
    try
    {
      localContext.startActivity(paramView);
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Actvity was not found for intent, ");
      localStringBuilder.append(paramView.toString());
      Log.w("URLSpan", localStringBuilder.toString());
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mURL);
  }
}
