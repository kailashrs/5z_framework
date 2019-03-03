package android.service.autofill;

import android.icu.text.DateFormat;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.Date;

public final class DateValueSanitizer
  extends InternalSanitizer
  implements Sanitizer, Parcelable
{
  public static final Parcelable.Creator<DateValueSanitizer> CREATOR = new Parcelable.Creator()
  {
    public DateValueSanitizer createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DateValueSanitizer((DateFormat)paramAnonymousParcel.readSerializable());
    }
    
    public DateValueSanitizer[] newArray(int paramAnonymousInt)
    {
      return new DateValueSanitizer[paramAnonymousInt];
    }
  };
  private static final String TAG = "DateValueSanitizer";
  private final DateFormat mDateFormat;
  
  public DateValueSanitizer(DateFormat paramDateFormat)
  {
    mDateFormat = ((DateFormat)Preconditions.checkNotNull(paramDateFormat));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public AutofillValue sanitize(AutofillValue paramAutofillValue)
  {
    if (paramAutofillValue == null)
    {
      Log.w("DateValueSanitizer", "sanitize() called with null value");
      return null;
    }
    Object localObject1;
    if (!paramAutofillValue.isDate())
    {
      if (Helper.sDebug)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append(paramAutofillValue);
        ((StringBuilder)localObject1).append(" is not a date");
        Log.d("DateValueSanitizer", ((StringBuilder)localObject1).toString());
      }
      return null;
    }
    try
    {
      Object localObject2 = new java/util/Date;
      ((Date)localObject2).<init>(paramAutofillValue.getDateValue());
      localObject1 = mDateFormat.format((Date)localObject2);
      if (Helper.sDebug)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Transformed ");
        localStringBuilder.append(localObject2);
        localStringBuilder.append(" to ");
        localStringBuilder.append((String)localObject1);
        Log.d("DateValueSanitizer", localStringBuilder.toString());
      }
      localObject1 = mDateFormat.parse((String)localObject1);
      if (Helper.sDebug)
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Sanitized to ");
        ((StringBuilder)localObject2).append(localObject1);
        Log.d("DateValueSanitizer", ((StringBuilder)localObject2).toString());
      }
      localObject1 = AutofillValue.forDate(((Date)localObject1).getTime());
      return localObject1;
    }
    catch (Exception localException)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Could not apply ");
      ((StringBuilder)localObject1).append(mDateFormat);
      ((StringBuilder)localObject1).append(" to ");
      ((StringBuilder)localObject1).append(paramAutofillValue);
      ((StringBuilder)localObject1).append(": ");
      ((StringBuilder)localObject1).append(localException);
      Log.w("DateValueSanitizer", ((StringBuilder)localObject1).toString());
    }
    return null;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DateValueSanitizer: [dateFormat=");
    localStringBuilder.append(mDateFormat);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeSerializable(mDateFormat);
  }
}
