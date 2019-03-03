package android.accounts;

public class AccountAndUser
{
  public Account account;
  public int userId;
  
  public AccountAndUser(Account paramAccount, int paramInt)
  {
    account = paramAccount;
    userId = paramInt;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof AccountAndUser)) {
      return false;
    }
    paramObject = (AccountAndUser)paramObject;
    if ((!account.equals(account)) || (userId != userId)) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return account.hashCode() + userId;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(account.toString());
    localStringBuilder.append(" u");
    localStringBuilder.append(userId);
    return localStringBuilder.toString();
  }
}
