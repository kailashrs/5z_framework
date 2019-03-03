package android.service.autofill;

import com.android.internal.util.Preconditions;

public final class Validators
{
  private Validators()
  {
    throw new UnsupportedOperationException("contains static methods only");
  }
  
  public static Validator and(Validator... paramVarArgs)
  {
    return new RequiredValidators(getInternalValidators(paramVarArgs));
  }
  
  private static InternalValidator[] getInternalValidators(Validator[] paramArrayOfValidator)
  {
    Preconditions.checkArrayElementsNotNull(paramArrayOfValidator, "validators");
    InternalValidator[] arrayOfInternalValidator = new InternalValidator[paramArrayOfValidator.length];
    for (int i = 0; i < paramArrayOfValidator.length; i++)
    {
      boolean bool = paramArrayOfValidator[i] instanceof InternalValidator;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("element ");
      localStringBuilder.append(i);
      localStringBuilder.append(" not provided by Android System: ");
      localStringBuilder.append(paramArrayOfValidator[i]);
      Preconditions.checkArgument(bool, localStringBuilder.toString());
      arrayOfInternalValidator[i] = ((InternalValidator)paramArrayOfValidator[i]);
    }
    return arrayOfInternalValidator;
  }
  
  public static Validator not(Validator paramValidator)
  {
    boolean bool = paramValidator instanceof InternalValidator;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("validator not provided by Android System: ");
    localStringBuilder.append(paramValidator);
    Preconditions.checkArgument(bool, localStringBuilder.toString());
    return new NegationValidator((InternalValidator)paramValidator);
  }
  
  public static Validator or(Validator... paramVarArgs)
  {
    return new OptionalValidators(getInternalValidators(paramVarArgs));
  }
}
