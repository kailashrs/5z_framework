package android.provider;

import android.annotation.SystemApi;
import android.content.Context;
import java.util.Locale;

@SystemApi
public abstract class SearchIndexableData
{
  public String className;
  public Context context;
  public boolean enabled = true;
  public int iconResId;
  public String intentAction;
  public String intentTargetClass;
  public String intentTargetPackage;
  public String key;
  public Locale locale = Locale.getDefault();
  public String packageName;
  public int rank;
  public int userId = -1;
  
  public SearchIndexableData() {}
  
  public SearchIndexableData(Context paramContext)
  {
    this();
    context = paramContext;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SearchIndexableData[context: ");
    localStringBuilder.append(context);
    localStringBuilder.append(", ");
    localStringBuilder.append("locale: ");
    localStringBuilder.append(locale);
    localStringBuilder.append(", ");
    localStringBuilder.append("enabled: ");
    localStringBuilder.append(enabled);
    localStringBuilder.append(", ");
    localStringBuilder.append("rank: ");
    localStringBuilder.append(rank);
    localStringBuilder.append(", ");
    localStringBuilder.append("key: ");
    localStringBuilder.append(key);
    localStringBuilder.append(", ");
    localStringBuilder.append("userId: ");
    localStringBuilder.append(userId);
    localStringBuilder.append(", ");
    localStringBuilder.append("className: ");
    localStringBuilder.append(className);
    localStringBuilder.append(", ");
    localStringBuilder.append("packageName: ");
    localStringBuilder.append(packageName);
    localStringBuilder.append(", ");
    localStringBuilder.append("iconResId: ");
    localStringBuilder.append(iconResId);
    localStringBuilder.append(", ");
    localStringBuilder.append("intentAction: ");
    localStringBuilder.append(intentAction);
    localStringBuilder.append(", ");
    localStringBuilder.append("intentTargetPackage: ");
    localStringBuilder.append(intentTargetPackage);
    localStringBuilder.append(", ");
    localStringBuilder.append("intentTargetClass: ");
    localStringBuilder.append(intentTargetClass);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
