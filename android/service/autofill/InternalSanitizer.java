package android.service.autofill;

import android.os.Parcelable;
import android.view.autofill.AutofillValue;

public abstract class InternalSanitizer
  implements Sanitizer, Parcelable
{
  public InternalSanitizer() {}
  
  public abstract AutofillValue sanitize(AutofillValue paramAutofillValue);
}
