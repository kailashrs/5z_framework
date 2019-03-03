package android.location;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Geocoder
{
  private static final String TAG = "Geocoder";
  private GeocoderParams mParams;
  private ILocationManager mService;
  
  public Geocoder(Context paramContext)
  {
    this(paramContext, Locale.getDefault());
  }
  
  public Geocoder(Context paramContext, Locale paramLocale)
  {
    if (paramLocale != null)
    {
      mParams = new GeocoderParams(paramContext, paramLocale);
      mService = ILocationManager.Stub.asInterface(ServiceManager.getService("location"));
      return;
    }
    throw new NullPointerException("locale == null");
  }
  
  public static boolean isPresent()
  {
    ILocationManager localILocationManager = ILocationManager.Stub.asInterface(ServiceManager.getService("location"));
    try
    {
      boolean bool = localILocationManager.geocoderIsPresent();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("Geocoder", "isPresent: got RemoteException", localRemoteException);
    }
    return false;
  }
  
  public List<Address> getFromLocation(double paramDouble1, double paramDouble2, int paramInt)
    throws IOException
  {
    if ((paramDouble1 >= -90.0D) && (paramDouble1 <= 90.0D))
    {
      if ((paramDouble2 >= -180.0D) && (paramDouble2 <= 180.0D)) {
        try
        {
          Object localObject = new java/util/ArrayList;
          ((ArrayList)localObject).<init>();
          String str = mService.getFromLocation(paramDouble1, paramDouble2, paramInt, mParams, (List)localObject);
          if (str == null) {
            return localObject;
          }
          localObject = new java/io/IOException;
          ((IOException)localObject).<init>(str);
          throw ((Throwable)localObject);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("Geocoder", "getFromLocation: got RemoteException", localRemoteException);
          return null;
        }
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("longitude == ");
      localStringBuilder.append(paramDouble2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("latitude == ");
    localStringBuilder.append(paramDouble1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public List<Address> getFromLocationName(String paramString, int paramInt)
    throws IOException
  {
    if (paramString != null) {
      try
      {
        Object localObject = new java/util/ArrayList;
        ((ArrayList)localObject).<init>();
        paramString = mService.getFromLocationName(paramString, 0.0D, 0.0D, 0.0D, 0.0D, paramInt, mParams, (List)localObject);
        if (paramString == null) {
          return localObject;
        }
        localObject = new java/io/IOException;
        ((IOException)localObject).<init>(paramString);
        throw ((Throwable)localObject);
      }
      catch (RemoteException paramString)
      {
        Log.e("Geocoder", "getFromLocationName: got RemoteException", paramString);
        return null;
      }
    }
    throw new IllegalArgumentException("locationName == null");
  }
  
  public List<Address> getFromLocationName(String paramString, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
    throws IOException
  {
    if (paramString != null)
    {
      if ((paramDouble1 >= -90.0D) && (paramDouble1 <= 90.0D))
      {
        if ((paramDouble2 >= -180.0D) && (paramDouble2 <= 180.0D))
        {
          if ((paramDouble3 >= -90.0D) && (paramDouble3 <= 90.0D))
          {
            if ((paramDouble4 >= -180.0D) && (paramDouble4 <= 180.0D)) {
              try
              {
                Object localObject = new java/util/ArrayList;
                ((ArrayList)localObject).<init>();
                paramString = mService.getFromLocationName(paramString, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramInt, mParams, (List)localObject);
                if (paramString == null) {
                  return localObject;
                }
                localObject = new java/io/IOException;
                ((IOException)localObject).<init>(paramString);
                throw ((Throwable)localObject);
              }
              catch (RemoteException paramString)
              {
                Log.e("Geocoder", "getFromLocationName: got RemoteException", paramString);
                return null;
              }
            }
            paramString = new StringBuilder();
            paramString.append("upperRightLongitude == ");
            paramString.append(paramDouble4);
            throw new IllegalArgumentException(paramString.toString());
          }
          paramString = new StringBuilder();
          paramString.append("upperRightLatitude == ");
          paramString.append(paramDouble3);
          throw new IllegalArgumentException(paramString.toString());
        }
        paramString = new StringBuilder();
        paramString.append("lowerLeftLongitude == ");
        paramString.append(paramDouble2);
        throw new IllegalArgumentException(paramString.toString());
      }
      paramString = new StringBuilder();
      paramString.append("lowerLeftLatitude == ");
      paramString.append(paramDouble1);
      throw new IllegalArgumentException(paramString.toString());
    }
    throw new IllegalArgumentException("locationName == null");
  }
}
