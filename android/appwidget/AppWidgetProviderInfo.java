package android.appwidget;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ResourceId;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppWidgetProviderInfo
  implements Parcelable
{
  public static final Parcelable.Creator<AppWidgetProviderInfo> CREATOR = new Parcelable.Creator()
  {
    public AppWidgetProviderInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AppWidgetProviderInfo(paramAnonymousParcel);
    }
    
    public AppWidgetProviderInfo[] newArray(int paramAnonymousInt)
    {
      return new AppWidgetProviderInfo[paramAnonymousInt];
    }
  };
  public static final int RESIZE_BOTH = 3;
  public static final int RESIZE_HORIZONTAL = 1;
  public static final int RESIZE_NONE = 0;
  public static final int RESIZE_VERTICAL = 2;
  public static final int WIDGET_CATEGORY_HOME_SCREEN = 1;
  public static final int WIDGET_CATEGORY_KEYGUARD = 2;
  public static final int WIDGET_CATEGORY_SEARCHBOX = 4;
  public static final int WIDGET_FEATURE_HIDE_FROM_PICKER = 2;
  public static final int WIDGET_FEATURE_RECONFIGURABLE = 1;
  public int autoAdvanceViewId;
  public ComponentName configure;
  public int icon;
  public int initialKeyguardLayout;
  public int initialLayout;
  @Deprecated
  public String label;
  public int minHeight;
  public int minResizeHeight;
  public int minResizeWidth;
  public int minWidth;
  public int previewImage;
  public ComponentName provider;
  public ActivityInfo providerInfo;
  public int resizeMode;
  public int updatePeriodMillis;
  public int widgetCategory;
  public int widgetFeatures;
  
  public AppWidgetProviderInfo() {}
  
  public AppWidgetProviderInfo(Parcel paramParcel)
  {
    provider = ((ComponentName)paramParcel.readTypedObject(ComponentName.CREATOR));
    minWidth = paramParcel.readInt();
    minHeight = paramParcel.readInt();
    minResizeWidth = paramParcel.readInt();
    minResizeHeight = paramParcel.readInt();
    updatePeriodMillis = paramParcel.readInt();
    initialLayout = paramParcel.readInt();
    initialKeyguardLayout = paramParcel.readInt();
    configure = ((ComponentName)paramParcel.readTypedObject(ComponentName.CREATOR));
    label = paramParcel.readString();
    icon = paramParcel.readInt();
    previewImage = paramParcel.readInt();
    autoAdvanceViewId = paramParcel.readInt();
    resizeMode = paramParcel.readInt();
    widgetCategory = paramParcel.readInt();
    providerInfo = ((ActivityInfo)paramParcel.readTypedObject(ActivityInfo.CREATOR));
    widgetFeatures = paramParcel.readInt();
  }
  
  private Drawable loadDrawable(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Drawable localDrawable = null;
    try
    {
      Object localObject = paramContext.getPackageManager().getResourcesForApplication(providerInfo.applicationInfo);
      if (ResourceId.isValid(paramInt2))
      {
        int i = paramInt1;
        if (paramInt1 < 0) {
          i = 0;
        }
        localObject = ((Resources)localObject).getDrawableForDensity(paramInt2, i, null);
        return localObject;
      }
    }
    catch (PackageManager.NameNotFoundException|Resources.NotFoundException localNameNotFoundException) {}
    if (paramBoolean) {
      localDrawable = providerInfo.loadIcon(paramContext.getPackageManager());
    }
    return localDrawable;
  }
  
  public AppWidgetProviderInfo clone()
  {
    AppWidgetProviderInfo localAppWidgetProviderInfo = new AppWidgetProviderInfo();
    Object localObject1 = provider;
    Object localObject2 = null;
    if (localObject1 == null) {
      localObject1 = null;
    } else {
      localObject1 = provider.clone();
    }
    provider = ((ComponentName)localObject1);
    minWidth = minWidth;
    minHeight = minHeight;
    minResizeWidth = minResizeHeight;
    minResizeHeight = minResizeHeight;
    updatePeriodMillis = updatePeriodMillis;
    initialLayout = initialLayout;
    initialKeyguardLayout = initialKeyguardLayout;
    if (configure == null) {
      localObject1 = null;
    } else {
      localObject1 = configure.clone();
    }
    configure = ((ComponentName)localObject1);
    if (label == null) {
      localObject1 = localObject2;
    } else {
      localObject1 = label.substring(0);
    }
    label = ((String)localObject1);
    icon = icon;
    previewImage = previewImage;
    autoAdvanceViewId = autoAdvanceViewId;
    resizeMode = resizeMode;
    widgetCategory = widgetCategory;
    providerInfo = providerInfo;
    widgetFeatures = widgetFeatures;
    return localAppWidgetProviderInfo;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public final UserHandle getProfile()
  {
    return new UserHandle(UserHandle.getUserId(providerInfo.applicationInfo.uid));
  }
  
  public final Drawable loadIcon(Context paramContext, int paramInt)
  {
    return loadDrawable(paramContext, paramInt, providerInfo.getIconResource(), true);
  }
  
  public final String loadLabel(PackageManager paramPackageManager)
  {
    paramPackageManager = providerInfo.loadLabel(paramPackageManager);
    if (paramPackageManager != null) {
      return paramPackageManager.toString().trim();
    }
    return null;
  }
  
  public final Drawable loadPreviewImage(Context paramContext, int paramInt)
  {
    return loadDrawable(paramContext, paramInt, previewImage, false);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AppWidgetProviderInfo(");
    localStringBuilder.append(getProfile());
    localStringBuilder.append('/');
    localStringBuilder.append(provider);
    localStringBuilder.append(')');
    return localStringBuilder.toString();
  }
  
  public void updateDimensions(DisplayMetrics paramDisplayMetrics)
  {
    minWidth = TypedValue.complexToDimensionPixelSize(minWidth, paramDisplayMetrics);
    minHeight = TypedValue.complexToDimensionPixelSize(minHeight, paramDisplayMetrics);
    minResizeWidth = TypedValue.complexToDimensionPixelSize(minResizeWidth, paramDisplayMetrics);
    minResizeHeight = TypedValue.complexToDimensionPixelSize(minResizeHeight, paramDisplayMetrics);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedObject(provider, paramInt);
    paramParcel.writeInt(minWidth);
    paramParcel.writeInt(minHeight);
    paramParcel.writeInt(minResizeWidth);
    paramParcel.writeInt(minResizeHeight);
    paramParcel.writeInt(updatePeriodMillis);
    paramParcel.writeInt(initialLayout);
    paramParcel.writeInt(initialKeyguardLayout);
    paramParcel.writeTypedObject(configure, paramInt);
    paramParcel.writeString(label);
    paramParcel.writeInt(icon);
    paramParcel.writeInt(previewImage);
    paramParcel.writeInt(autoAdvanceViewId);
    paramParcel.writeInt(resizeMode);
    paramParcel.writeInt(widgetCategory);
    paramParcel.writeTypedObject(providerInfo, paramInt);
    paramParcel.writeInt(widgetFeatures);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CategoryFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FeatureFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResizeModeFlags {}
}
