package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;

final class NegationValidator
  extends InternalValidator
{
  public static final Parcelable.Creator<NegationValidator> CREATOR = new Parcelable.Creator()
  {
    public NegationValidator createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NegationValidator((InternalValidator)paramAnonymousParcel.readParcelable(null));
    }
    
    public NegationValidator[] newArray(int paramAnonymousInt)
    {
      return new NegationValidator[paramAnonymousInt];
    }
  };
  private final InternalValidator mValidator;
  
  NegationValidator(InternalValidator paramInternalValidator)
  {
    mValidator = ((InternalValidator)Preconditions.checkNotNull(paramInternalValidator));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isValid(ValueFinder paramValueFinder)
  {
    return mValidator.isValid(paramValueFinder) ^ true;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NegationValidator: [validator=");
    localStringBuilder.append(mValidator);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mValidator, paramInt);
  }
}
