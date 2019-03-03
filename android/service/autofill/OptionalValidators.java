package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;

final class OptionalValidators
  extends InternalValidator
{
  public static final Parcelable.Creator<OptionalValidators> CREATOR = new Parcelable.Creator()
  {
    public OptionalValidators createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OptionalValidators((InternalValidator[])paramAnonymousParcel.readParcelableArray(null, InternalValidator.class));
    }
    
    public OptionalValidators[] newArray(int paramAnonymousInt)
    {
      return new OptionalValidators[paramAnonymousInt];
    }
  };
  private static final String TAG = "OptionalValidators";
  private final InternalValidator[] mValidators;
  
  OptionalValidators(InternalValidator[] paramArrayOfInternalValidator)
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
        Log.d("OptionalValidators", localStringBuilder.toString());
      }
      if (bool) {
        return true;
      }
    }
    return false;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder("OptionalValidators: [validators=");
    localStringBuilder.append(mValidators);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelableArray(mValidators, paramInt);
  }
}
