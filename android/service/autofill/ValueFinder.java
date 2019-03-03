package android.service.autofill;

import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;

public abstract interface ValueFinder
{
  public String findByAutofillId(AutofillId paramAutofillId)
  {
    paramAutofillId = findRawValueByAutofillId(paramAutofillId);
    if ((paramAutofillId != null) && (paramAutofillId.isText())) {
      paramAutofillId = paramAutofillId.getTextValue().toString();
    } else {
      paramAutofillId = null;
    }
    return paramAutofillId;
  }
  
  public abstract AutofillValue findRawValueByAutofillId(AutofillId paramAutofillId);
}
