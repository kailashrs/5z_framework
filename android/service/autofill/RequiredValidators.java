package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;

final class RequiredValidators
  extends InternalValidator
{
  public static final Parcelable.Creator<RequiredValidators> CREATOR = new Parcelable.Creator()
  {
    public RequiredValidators createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RequiredValidators((InternalValidator[])paramAnonymousParcel.readParcelableArray(null, InternalValidator.class));
    }
    
    public RequiredValidators[] newArray(int paramAnonymousInt)
    {
      return new RequiredValidators[paramAnonymousInt];
    }
  };
  private static final String TAG = "RequiredValidators";
  private final InternalValidator[] mValidators;
  
  RequiredValidators(InternalValidator[] paramArrayOfInternalValidator)
  {
    mValidators = ((InternalValidator[])Preconditions.checkArrayElementsNotNull(paramArrayOfInternalValidator, "validators"));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isValid(ValueFinder paramValueFinder)
  {
    for (InternalValidator localInternalValidator : mValidators)
    {
      boolean bool = localInternalValidator.isValid(paramValueFinder);
      if (Helper.sDebug)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("isValid(");
        localStringBuilder.append(localInternalValidator);
        localStringBuilder.append("): ");
        localStringBuilder.append(bool);
        Log.d("RequiredValidators", localStringBuilder.toString());
      }
      if (!bool) {
        return false;
      }
    }
    return true;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder("RequiredValidators: [validators=");
    localStringBuilder.append(mValidators);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelableArray(mValidators, paramInt);
  }
}
