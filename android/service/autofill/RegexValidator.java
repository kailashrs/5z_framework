package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexValidator
  extends InternalValidator
  implements Validator, Parcelable
{
  public static final Parcelable.Creator<RegexValidator> CREATOR = new Parcelable.Creator()
  {
    public RegexValidator createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RegexValidator((AutofillId)paramAnonymousParcel.readParcelable(null), (Pattern)paramAnonymousParcel.readSerializable());
    }
    
    public RegexValidator[] newArray(int paramAnonymousInt)
    {
      return new RegexValidator[paramAnonymousInt];
    }
  };
  private static final String TAG = "RegexValidator";
  private final AutofillId mId;
  private final Pattern mRegex;
  
  public RegexValidator(AutofillId paramAutofillId, Pattern paramPattern)
  {
    mId = ((AutofillId)Preconditions.checkNotNull(paramAutofillId));
    mRegex = ((Pattern)Preconditions.checkNotNull(paramPattern));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isValid(ValueFinder paramValueFinder)
  {
    paramValueFinder = paramValueFinder.findByAutofillId(mId);
    if (paramValueFinder == null)
    {
      paramValueFinder = new StringBuilder();
      paramValueFinder.append("No view for id ");
      paramValueFinder.append(mId);
      Log.w("RegexValidator", paramValueFinder.toString());
      return false;
    }
    boolean bool = mRegex.matcher(paramValueFinder).matches();
    if (Helper.sDebug)
    {
      paramValueFinder = new StringBuilder();
      paramValueFinder.append("isValid(): ");
      paramValueFinder.append(bool);
      Log.d("RegexValidator", paramValueFinder.toString());
    }
    return bool;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RegexValidator: [id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", regex=");
    localStringBuilder.append(mRegex);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mId, paramInt);
    paramParcel.writeSerializable(mRegex);
  }
}
