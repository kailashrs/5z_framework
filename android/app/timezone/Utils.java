package android.app.timezone;

final class Utils
{
  private Utils() {}
  
  static <T> T validateConditionalNull(boolean paramBoolean, String paramString, T paramT)
  {
    if (paramBoolean) {
      return validateNotNull(paramString, paramT);
    }
    return validateNull(paramString, paramT);
  }
  
  static <T> T validateNotNull(String paramString, T paramT)
  {
    if (paramT != null) {
      return paramT;
    }
    paramT = new StringBuilder();
    paramT.append(paramString);
    paramT.append(" == null");
    throw new NullPointerException(paramT.toString());
  }
  
  static <T> T validateNull(String paramString, T paramT)
  {
    if (paramT == null) {
      return null;
    }
    paramT = new StringBuilder();
    paramT.append(paramString);
    paramT.append(" != null");
    throw new IllegalArgumentException(paramT.toString());
  }
  
  static String validateRulesVersion(String paramString1, String paramString2)
  {
    validateNotNull(paramString1, paramString2);
    if (!paramString2.isEmpty()) {
      return paramString2;
    }
    paramString2 = new StringBuilder();
    paramString2.append(paramString1);
    paramString2.append(" must not be empty");
    throw new IllegalArgumentException(paramString2.toString());
  }
  
  static int validateVersion(String paramString, int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 999)) {
      return paramInt;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(" version=");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
}
