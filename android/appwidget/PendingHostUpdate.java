package android.appwidget;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;

public class PendingHostUpdate
  implements Parcelable
{
  public static final Parcelable.Creator<PendingHostUpdate> CREATOR = new Parcelable.Creator()
  {
    public PendingHostUpdate createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PendingHostUpdate(paramAnonymousParcel, null);
    }
    
    public PendingHostUpdate[] newArray(int paramAnonymousInt)
    {
      return new PendingHostUpdate[paramAnonymousInt];
    }
  };
  static final int TYPE_PROVIDER_CHANGED = 1;
  static final int TYPE_VIEWS_UPDATE = 0;
  static final int TYPE_VIEW_DATA_CHANGED = 2;
  final int appWidgetId;
  final int type;
  int viewId;
  RemoteViews views;
  AppWidgetProviderInfo widgetInfo;
  
  private PendingHostUpdate(int paramInt1, int paramInt2)
  {
    appWidgetId = paramInt1;
    type = paramInt2;
  }
  
  private PendingHostUpdate(Parcel paramParcel)
  {
    appWidgetId = paramParcel.readInt();
    type = paramParcel.readInt();
    switch (type)
    {
    default: 
      break;
    case 2: 
      viewId = paramParcel.readInt();
      break;
    case 1: 
      if (paramParcel.readInt() != 0) {
        widgetInfo = new AppWidgetProviderInfo(paramParcel);
      }
      break;
    case 0: 
      if (paramParcel.readInt() != 0) {
        views = new RemoteViews(paramParcel);
      }
      break;
    }
  }
  
  public static PendingHostUpdate providerChanged(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    PendingHostUpdate localPendingHostUpdate = new PendingHostUpdate(paramInt, 1);
    widgetInfo = paramAppWidgetProviderInfo;
    return localPendingHostUpdate;
  }
  
  public static PendingHostUpdate updateAppWidget(int paramInt, RemoteViews paramRemoteViews)
  {
    PendingHostUpdate localPendingHostUpdate = new PendingHostUpdate(paramInt, 0);
    views = paramRemoteViews;
    return localPendingHostUpdate;
  }
  
  public static PendingHostUpdate viewDataChanged(int paramInt1, int paramInt2)
  {
    PendingHostUpdate localPendingHostUpdate = new PendingHostUpdate(paramInt1, 2);
    viewId = paramInt2;
    return localPendingHostUpdate;
  }
  
  private void writeNullParcelable(Parcelable paramParcelable, Parcel paramParcel, int paramInt)
  {
    if (paramParcelable != null)
    {
      paramParcel.writeInt(1);
      paramParcelable.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(appWidgetId);
    paramParcel.writeInt(type);
    switch (type)
    {
    default: 
      break;
    case 2: 
      paramParcel.writeInt(viewId);
      break;
    case 1: 
      writeNullParcelable(widgetInfo, paramParcel, paramInt);
      break;
    case 0: 
      writeNullParcelable(views, paramParcel, paramInt);
    }
  }
}
