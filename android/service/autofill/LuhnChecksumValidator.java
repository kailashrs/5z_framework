package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class LuhnChecksumValidator
  extends InternalValidator
  implements Validator, Parcelable
{
  public static final Parcelable.Creator<LuhnChecksumValidator> CREATOR = new Parcelable.Creator()
  {
    public LuhnChecksumValidator createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LuhnChecksumValidator((AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class));
    }
    
    public LuhnChecksumValidator[] newArray(int paramAnonymousInt)
    {
      return new LuhnChecksumValidator[paramAnonymousInt];
    }
  };
  private static final String TAG = "LuhnChecksumValidator";
  private final AutofillId[] mIds;
  
  public LuhnChecksumValidator(AutofillId... paramVarArgs)
  {
    mIds = ((AutofillId[])Preconditions.checkArrayElementsNotNull(paramVarArgs, "ids"));
  }
  
  private static boolean isLuhnChecksumValid(String paramString)
  {
    int i = 0;
    int j = 0;
    int k = paramString.length();
    boolean bool = true;
    int m = k - 1;
    for (;;)
    {
      int n = 0;
      if (m < 0) {
        break;
      }
      int i1 = paramString.charAt(m) - '0';
      int i2 = i;
      k = j;
      if (i1 >= 0) {
        if (i1 > 9)
        {
          i2 = i;
          k = j;
        }
        else
        {
          if (j != 0)
          {
            k = i1 * 2;
            if (k > 9) {
              k -= 9;
            }
          }
          else
          {
            k = i1;
          }
          i2 = i + k;
          k = n;
          if (j == 0) {
            k = 1;
          }
        }
      }
      m--;
      i = i2;
      j = k;
    }
    if (i % 10 != 0) {
      bool = false;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isValid(ValueFinder paramValueFinder)
  {
    if ((mIds != null) && (mIds.length != 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Object localObject;
      for (localObject : mIds)
      {
        String str = paramValueFinder.findByAutofillId((AutofillId)localObject);
        if (str == null)
        {
          if (Helper.sDebug)
          {
            paramValueFinder = new StringBuilder();
            paramValueFinder.append("No partial number for id ");
            paramValueFinder.append(localObject);
            Log.d("LuhnChecksumValidator", paramValueFinder.toString());
          }
          return false;
        }
        localStringBuilder.append(str);
      }
      paramValueFinder = localStringBuilder.toString();
      boolean bool = isLuhnChecksumValid(paramValueFinder);
      if (Helper.sDebug)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("isValid(");
        ((StringBuilder)localObject).append(paramValueFinder.length());
        ((StringBuilder)localObject).append(" chars): ");
        ((StringBuilder)localObject).append(bool);
        Log.d("LuhnChecksumValidator", ((StringBuilder)localObject).toString());
      }
      return bool;
    }
    return false;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("LuhnChecksumValidator: [ids=");
    localStringBuilder.append(Arrays.toString(mIds));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelableArray(mIds, paramInt);
  }
}
