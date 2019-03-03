package android.provider;

import android.content.ComponentName;
import android.net.Uri;
import com.android.internal.util.ArrayUtils;
import java.util.Locale;

public class SettingsValidators
{
  public static final Validator ANY_INTEGER_VALIDATOR;
  public static final Validator ANY_STRING_VALIDATOR;
  public static final Validator BOOLEAN_VALIDATOR = new DiscreteValueValidator(new String[] { "0", "1" });
  public static final Validator COMPONENT_NAME_VALIDATOR;
  public static final Validator LENIENT_IP_ADDRESS_VALIDATOR = new Validator()
  {
    private static final int MAX_IPV6_LENGTH = 45;
    
    public boolean validate(String paramAnonymousString)
    {
      boolean bool = false;
      if (paramAnonymousString == null) {
        return false;
      }
      if (paramAnonymousString.length() <= 45) {
        bool = true;
      }
      return bool;
    }
  };
  public static final Validator LOCALE_VALIDATOR = new Validator()
  {
    public boolean validate(String paramAnonymousString)
    {
      if (paramAnonymousString == null) {
        return false;
      }
      Locale[] arrayOfLocale = Locale.getAvailableLocales();
      int i = arrayOfLocale.length;
      for (int j = 0; j < i; j++) {
        if (paramAnonymousString.equals(arrayOfLocale[j].toString())) {
          return true;
        }
      }
      return false;
    }
  };
  public static final Validator NON_NEGATIVE_INTEGER_VALIDATOR;
  public static final Validator NULLABLE_COMPONENT_NAME_VALIDATOR;
  public static final Validator PACKAGE_NAME_VALIDATOR;
  public static final Validator URI_VALIDATOR;
  
  static
  {
    ANY_STRING_VALIDATOR = new Validator()
    {
      public boolean validate(String paramAnonymousString)
      {
        return true;
      }
    };
    NON_NEGATIVE_INTEGER_VALIDATOR = new Validator()
    {
      public boolean validate(String paramAnonymousString)
      {
        boolean bool = false;
        try
        {
          int i = Integer.parseInt(paramAnonymousString);
          if (i >= 0) {
            bool = true;
          }
          return bool;
        }
        catch (NumberFormatException paramAnonymousString) {}
        return false;
      }
    };
    ANY_INTEGER_VALIDATOR = new Validator()
    {
      public boolean validate(String paramAnonymousString)
      {
        try
        {
          Integer.parseInt(paramAnonymousString);
          return true;
        }
        catch (NumberFormatException paramAnonymousString) {}
        return false;
      }
    };
    URI_VALIDATOR = new Validator()
    {
      public boolean validate(String paramAnonymousString)
      {
        try
        {
          Uri.decode(paramAnonymousString);
          return true;
        }
        catch (IllegalArgumentException paramAnonymousString) {}
        return false;
      }
    };
    COMPONENT_NAME_VALIDATOR = new Validator()
    {
      public boolean validate(String paramAnonymousString)
      {
        boolean bool;
        if ((paramAnonymousString != null) && (ComponentName.unflattenFromString(paramAnonymousString) != null)) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
    };
    NULLABLE_COMPONENT_NAME_VALIDATOR = new Validator()
    {
      public boolean validate(String paramAnonymousString)
      {
        boolean bool;
        if ((paramAnonymousString != null) && (!SettingsValidators.COMPONENT_NAME_VALIDATOR.validate(paramAnonymousString))) {
          bool = false;
        } else {
          bool = true;
        }
        return bool;
      }
    };
    PACKAGE_NAME_VALIDATOR = new Validator()
    {
      private boolean isStringPackageName(String paramAnonymousString)
      {
        int i = 0;
        if (paramAnonymousString == null) {
          return false;
        }
        paramAnonymousString = paramAnonymousString.split("\\.");
        boolean bool1 = true;
        int j = paramAnonymousString.length;
        boolean bool2;
        for (;;)
        {
          bool2 = bool1;
          if (i >= j) {
            break;
          }
          bool1 &= isSubpartValidForPackageName(paramAnonymousString[i]);
          if (!bool1)
          {
            bool2 = bool1;
            break;
          }
          i++;
        }
        return bool2;
      }
      
      private boolean isSubpartValidForPackageName(String paramAnonymousString)
      {
        if (paramAnonymousString.length() == 0) {
          return false;
        }
        boolean bool1 = Character.isLetter(paramAnonymousString.charAt(0));
        boolean bool2;
        for (int i = 1;; i++)
        {
          bool2 = bool1;
          if (i >= paramAnonymousString.length()) {
            break;
          }
          boolean bool3;
          if ((!Character.isLetterOrDigit(paramAnonymousString.charAt(i))) && (paramAnonymousString.charAt(i) != '_')) {
            bool3 = false;
          } else {
            bool3 = true;
          }
          bool1 &= bool3;
          if (!bool1)
          {
            bool2 = bool1;
            break;
          }
        }
        return bool2;
      }
      
      public boolean validate(String paramAnonymousString)
      {
        boolean bool;
        if ((paramAnonymousString != null) && (isStringPackageName(paramAnonymousString))) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
    };
  }
  
  public SettingsValidators() {}
  
  public static final class ComponentNameListValidator
    implements SettingsValidators.Validator
  {
    private final String mSeparator;
    
    public ComponentNameListValidator(String paramString)
    {
      mSeparator = paramString;
    }
    
    public boolean validate(String paramString)
    {
      if (paramString == null) {
        return false;
      }
      for (String str : paramString.split(mSeparator)) {
        if (!SettingsValidators.COMPONENT_NAME_VALIDATOR.validate(str)) {
          return false;
        }
      }
      return true;
    }
  }
  
  public static final class DiscreteValueValidator
    implements SettingsValidators.Validator
  {
    private final String[] mValues;
    
    public DiscreteValueValidator(String[] paramArrayOfString)
    {
      mValues = paramArrayOfString;
    }
    
    public boolean validate(String paramString)
    {
      return ArrayUtils.contains(mValues, paramString);
    }
  }
  
  public static final class InclusiveFloatRangeValidator
    implements SettingsValidators.Validator
  {
    private final float mMax;
    private final float mMin;
    
    public InclusiveFloatRangeValidator(float paramFloat1, float paramFloat2)
    {
      mMin = paramFloat1;
      mMax = paramFloat2;
    }
    
    public boolean validate(String paramString)
    {
      boolean bool1 = false;
      try
      {
        float f1 = Float.parseFloat(paramString);
        boolean bool2 = bool1;
        if (f1 >= mMin)
        {
          float f2 = mMax;
          bool2 = bool1;
          if (f1 <= f2) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (NumberFormatException|NullPointerException paramString) {}
      return false;
    }
  }
  
  public static final class InclusiveIntegerRangeValidator
    implements SettingsValidators.Validator
  {
    private final int mMax;
    private final int mMin;
    
    public InclusiveIntegerRangeValidator(int paramInt1, int paramInt2)
    {
      mMin = paramInt1;
      mMax = paramInt2;
    }
    
    public boolean validate(String paramString)
    {
      boolean bool1 = false;
      try
      {
        int i = Integer.parseInt(paramString);
        boolean bool2 = bool1;
        if (i >= mMin)
        {
          int j = mMax;
          bool2 = bool1;
          if (i <= j) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (NumberFormatException paramString) {}
      return false;
    }
  }
  
  public static final class PackageNameListValidator
    implements SettingsValidators.Validator
  {
    private final String mSeparator;
    
    public PackageNameListValidator(String paramString)
    {
      mSeparator = paramString;
    }
    
    public boolean validate(String paramString)
    {
      if (paramString == null) {
        return false;
      }
      for (paramString : paramString.split(mSeparator)) {
        if (!SettingsValidators.PACKAGE_NAME_VALIDATOR.validate(paramString)) {
          return false;
        }
      }
      return true;
    }
  }
  
  public static abstract interface Validator
  {
    public abstract boolean validate(String paramString);
  }
}
