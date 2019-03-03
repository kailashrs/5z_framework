package android.text;

public abstract class LoginFilter
  implements InputFilter
{
  private boolean mAppendInvalid;
  
  LoginFilter()
  {
    mAppendInvalid = false;
  }
  
  LoginFilter(boolean paramBoolean)
  {
    mAppendInvalid = paramBoolean;
  }
  
  public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
  {
    onStart();
    char c;
    for (int i = 0; i < paramInt3; i++)
    {
      c = paramSpanned.charAt(i);
      if (!isAllowed(c)) {
        onInvalidCharacter(c);
      }
    }
    paramInt3 = 0;
    Object localObject1 = null;
    for (i = paramInt1; i < paramInt2; i++)
    {
      c = paramCharSequence.charAt(i);
      if (isAllowed(c))
      {
        paramInt3++;
      }
      else
      {
        if (mAppendInvalid)
        {
          paramInt3++;
        }
        else
        {
          Object localObject2 = localObject1;
          if (localObject1 == null)
          {
            localObject2 = new SpannableStringBuilder(paramCharSequence, paramInt1, paramInt2);
            paramInt3 = i - paramInt1;
          }
          ((SpannableStringBuilder)localObject2).delete(paramInt3, paramInt3 + 1);
          localObject1 = localObject2;
        }
        onInvalidCharacter(c);
      }
    }
    for (paramInt1 = paramInt4; paramInt1 < paramSpanned.length(); paramInt1++)
    {
      c = paramSpanned.charAt(paramInt1);
      if (!isAllowed(c)) {
        onInvalidCharacter(c);
      }
    }
    onStop();
    return localObject1;
  }
  
  public abstract boolean isAllowed(char paramChar);
  
  public void onInvalidCharacter(char paramChar) {}
  
  public void onStart() {}
  
  public void onStop() {}
  
  public static class PasswordFilterGMail
    extends LoginFilter
  {
    public PasswordFilterGMail()
    {
      super();
    }
    
    public PasswordFilterGMail(boolean paramBoolean)
    {
      super();
    }
    
    public boolean isAllowed(char paramChar)
    {
      if ((' ' <= paramChar) && (paramChar <= '')) {
        return true;
      }
      return (' ' <= paramChar) && (paramChar <= 'ÿ');
    }
  }
  
  public static class UsernameFilterGMail
    extends LoginFilter
  {
    public UsernameFilterGMail()
    {
      super();
    }
    
    public UsernameFilterGMail(boolean paramBoolean)
    {
      super();
    }
    
    public boolean isAllowed(char paramChar)
    {
      if (('0' <= paramChar) && (paramChar <= '9')) {
        return true;
      }
      if (('a' <= paramChar) && (paramChar <= 'z')) {
        return true;
      }
      if (('A' <= paramChar) && (paramChar <= 'Z')) {
        return true;
      }
      return '.' == paramChar;
    }
  }
  
  public static class UsernameFilterGeneric
    extends LoginFilter
  {
    private static final String mAllowed = "@_-+.";
    
    public UsernameFilterGeneric()
    {
      super();
    }
    
    public UsernameFilterGeneric(boolean paramBoolean)
    {
      super();
    }
    
    public boolean isAllowed(char paramChar)
    {
      if (('0' <= paramChar) && (paramChar <= '9')) {
        return true;
      }
      if (('a' <= paramChar) && (paramChar <= 'z')) {
        return true;
      }
      if (('A' <= paramChar) && (paramChar <= 'Z')) {
        return true;
      }
      return "@_-+.".indexOf(paramChar) != -1;
    }
  }
}
