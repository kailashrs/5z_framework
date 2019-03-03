package android.provider;

import android.annotation.SystemApi;
import android.content.Context;

@SystemApi
public class SearchIndexableResource
  extends SearchIndexableData
{
  public int xmlResId;
  
  public SearchIndexableResource(int paramInt1, int paramInt2, String paramString, int paramInt3)
  {
    rank = paramInt1;
    xmlResId = paramInt2;
    className = paramString;
    iconResId = paramInt3;
  }
  
  public SearchIndexableResource(Context paramContext)
  {
    super(paramContext);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SearchIndexableResource[");
    localStringBuilder.append(super.toString());
    localStringBuilder.append(", ");
    localStringBuilder.append("xmlResId: ");
    localStringBuilder.append(xmlResId);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
