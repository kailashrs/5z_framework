package android.service.autofill;

import android.view.autofill.AutofillId;
import com.android.internal.util.Preconditions;

final class AutofillServiceHelper
{
  private AutofillServiceHelper()
  {
    throw new UnsupportedOperationException("contains static members only");
  }
  
  static AutofillId[] assertValid(AutofillId[] paramArrayOfAutofillId)
  {
    int i = 0;
    boolean bool;
    if ((paramArrayOfAutofillId != null) && (paramArrayOfAutofillId.length > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "must have at least one id");
    while (i < paramArrayOfAutofillId.length) {
      if (paramArrayOfAutofillId[i] != null)
      {
        i++;
      }
      else
      {
        paramArrayOfAutofillId = new StringBuilder();
        paramArrayOfAutofillId.append("ids[");
        paramArrayOfAutofillId.append(i);
        paramArrayOfAutofillId.append("] must not be null");
        throw new IllegalArgumentException(paramArrayOfAutofillId.toString());
      }
    }
    return paramArrayOfAutofillId;
  }
}
