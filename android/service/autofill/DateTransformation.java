package android.service.autofill;

import android.icu.text.DateFormat;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.Date;

public final class DateTransformation
  extends InternalTransformation
  implements Transformation, Parcelable
{
  public static final Parcelable.Creator<DateTransformation> CREATOR = new Parcelable.Creator()
  {
    public DateTransformation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DateTransformation((AutofillId)paramAnonymousParcel.readParcelable(null), (DateFormat)paramAnonymousParcel.readSerializable());
    }
    
    public DateTransformation[] newArray(int paramAnonymousInt)
    {
      return new DateTransformation[paramAnonymousInt];
    }
  };
  private static final String TAG = "DateTransformation";
  private final DateFormat mDateFormat;
  private final AutofillId mFieldId;
  
  public DateTransformation(AutofillId paramAutofillId, DateFormat paramDateFormat)
  {
    mFieldId = ((AutofillId)Preconditions.checkNotNull(paramAutofillId));
    mDateFormat = ((DateFormat)Preconditions.checkNotNull(paramDateFormat));
  }
  
  public void apply(ValueFinder paramValueFinder, RemoteViews paramRemoteViews, int paramInt)
    throws Exception
  {
    paramValueFinder = paramValueFinder.findRawValueByAutofillId(mFieldId);
    if (paramValueFinder == null)
    {
      paramValueFinder = new StringBuilder();
      paramValueFinder.append("No value for id ");
      paramValueFinder.append(mFieldId);
      Log.w("DateTransformation", paramValueFinder.toString());
      return;
    }
    if (!paramValueFinder.isDate())
    {
      paramRemoteViews = new StringBuilder();
      paramRemoteViews.append("Value for ");
      paramRemoteViews.append(mFieldId);
      paramRemoteViews.append(" is not date: ");
      paramRemoteViews.append(paramValueFinder);
      Log.w("DateTransformation", paramRemoteViews.toString());
      return;
    }
    try
    {
      localObject = new java/util/Date;
      ((Date)localObject).<init>(paramValueFinder.getDateValue());
      String str = mDateFormat.format((Date)localObject);
      if (Helper.sDebug)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Transformed ");
        localStringBuilder.append(localObject);
        localStringBuilder.append(" to ");
        localStringBuilder.append(str);
        Log.d("DateTransformation", localStringBuilder.toString());
      }
      paramRemoteViews.setCharSequence(paramInt, "setText", str);
    }
    catch (Exception paramRemoteViews)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Could not apply ");
      ((StringBuilder)localObject).append(mDateFormat);
      ((StringBuilder)localObject).append(" to ");
      ((StringBuilder)localObject).append(paramValueFinder);
      ((StringBuilder)localObject).append(": ");
      ((StringBuilder)localObject).append(paramRemoteViews);
      Log.w("DateTransformation", ((StringBuilder)localObject).toString());
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DateTransformation: [id=");
    localStringBuilder.append(mFieldId);
    localStringBuilder.append(", format=");
    localStringBuilder.append(mDateFormat);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mFieldId, paramInt);
    paramParcel.writeSerializable(mDateFormat);
  }
}
