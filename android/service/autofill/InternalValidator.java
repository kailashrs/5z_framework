package android.service.autofill;

import android.os.Parcelable;

public abstract class InternalValidator
  implements Validator, Parcelable
{
  public InternalValidator() {}
  
  public abstract boolean isValid(ValueFinder paramValueFinder);
}
