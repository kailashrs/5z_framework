package android.view.autofill;

import java.util.Collection;

public final class Helper
{
  public static boolean sDebug = false;
  public static boolean sVerbose = false;
  
  private Helper()
  {
    throw new UnsupportedOperationException("contains static members only");
  }
  
  public static void appendRedacted(StringBuilder paramStringBuilder, CharSequence paramCharSequence)
  {
    paramStringBuilder.append(getRedacted(paramCharSequence));
  }
  
  public static void appendRedacted(StringBuilder paramStringBuilder, String[] paramArrayOfString)
  {
    if (paramArrayOfString == null)
    {
      paramStringBuilder.append("N/A");
      return;
    }
    paramStringBuilder.append("[");
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      paramStringBuilder.append(" '");
      appendRedacted(paramStringBuilder, str);
      paramStringBuilder.append("'");
    }
    paramStringBuilder.append(" ]");
  }
  
  public static String getRedacted(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null)
    {
      paramCharSequence = "null";
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramCharSequence.length());
      localStringBuilder.append("_chars");
      paramCharSequence = localStringBuilder.toString();
    }
    return paramCharSequence;
  }
  
  public static AutofillId[] toArray(Collection<AutofillId> paramCollection)
  {
    if (paramCollection == null) {
      return new AutofillId[0];
    }
    AutofillId[] arrayOfAutofillId = new AutofillId[paramCollection.size()];
    paramCollection.toArray(arrayOfAutofillId);
    return arrayOfAutofillId;
  }
}
