package android.location;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.location.ProviderProperties;
import java.util.ArrayList;
import java.util.List;

public abstract interface ILocationManager
  extends IInterface
{
  public abstract boolean addGnssBatchingCallback(IBatchedLocationCallback paramIBatchedLocationCallback, String paramString)
    throws RemoteException;
  
  public abstract boolean addGnssMeasurementsListener(IGnssMeasurementsListener paramIGnssMeasurementsListener, String paramString)
    throws RemoteException;
  
  public abstract boolean addGnssNavigationMessageListener(IGnssNavigationMessageListener paramIGnssNavigationMessageListener, String paramString)
    throws RemoteException;
  
  public abstract void addTestProvider(String paramString1, ProviderProperties paramProviderProperties, String paramString2)
    throws RemoteException;
  
  public abstract void clearTestProviderEnabled(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void clearTestProviderLocation(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void clearTestProviderStatus(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void flushGnssBatch(String paramString)
    throws RemoteException;
  
  public abstract boolean geocoderIsPresent()
    throws RemoteException;
  
  public abstract List<String> getAllProviders()
    throws RemoteException;
  
  public abstract String[] getBackgroundThrottlingWhitelist()
    throws RemoteException;
  
  public abstract String getBestProvider(Criteria paramCriteria, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String getFromLocation(double paramDouble1, double paramDouble2, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
    throws RemoteException;
  
  public abstract String getFromLocationName(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
    throws RemoteException;
  
  public abstract int getGnssBatchSize(String paramString)
    throws RemoteException;
  
  public abstract String getGnssHardwareModelName()
    throws RemoteException;
  
  public abstract int getGnssYearOfHardware()
    throws RemoteException;
  
  public abstract Location getLastLocation(LocationRequest paramLocationRequest, String paramString)
    throws RemoteException;
  
  public abstract String getNetworkProviderPackage()
    throws RemoteException;
  
  public abstract ProviderProperties getProviderProperties(String paramString)
    throws RemoteException;
  
  public abstract List<String> getProviders(Criteria paramCriteria, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean injectLocation(Location paramLocation)
    throws RemoteException;
  
  public abstract boolean isLocationEnabledForUser(int paramInt)
    throws RemoteException;
  
  public abstract boolean isProviderEnabledForUser(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void locationCallbackFinished(ILocationListener paramILocationListener)
    throws RemoteException;
  
  public abstract boolean providerMeetsCriteria(String paramString, Criteria paramCriteria)
    throws RemoteException;
  
  public abstract boolean registerGnssStatusCallback(IGnssStatusListener paramIGnssStatusListener, String paramString)
    throws RemoteException;
  
  public abstract void removeGeofence(Geofence paramGeofence, PendingIntent paramPendingIntent, String paramString)
    throws RemoteException;
  
  public abstract void removeGnssBatchingCallback()
    throws RemoteException;
  
  public abstract void removeGnssMeasurementsListener(IGnssMeasurementsListener paramIGnssMeasurementsListener)
    throws RemoteException;
  
  public abstract void removeGnssNavigationMessageListener(IGnssNavigationMessageListener paramIGnssNavigationMessageListener)
    throws RemoteException;
  
  public abstract void removeTestProvider(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void removeUpdates(ILocationListener paramILocationListener, PendingIntent paramPendingIntent, String paramString)
    throws RemoteException;
  
  public abstract void reportLocation(Location paramLocation, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void reportLocationBatch(List<Location> paramList)
    throws RemoteException;
  
  public abstract void requestGeofence(LocationRequest paramLocationRequest, Geofence paramGeofence, PendingIntent paramPendingIntent, String paramString)
    throws RemoteException;
  
  public abstract void requestLocationUpdates(LocationRequest paramLocationRequest, ILocationListener paramILocationListener, PendingIntent paramPendingIntent, String paramString)
    throws RemoteException;
  
  public abstract boolean sendExtraCommand(String paramString1, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean sendNiResponse(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setLocationEnabledForUser(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract boolean setProviderEnabledForUser(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setTestProviderEnabled(String paramString1, boolean paramBoolean, String paramString2)
    throws RemoteException;
  
  public abstract void setTestProviderLocation(String paramString1, Location paramLocation, String paramString2)
    throws RemoteException;
  
  public abstract void setTestProviderStatus(String paramString1, int paramInt, Bundle paramBundle, long paramLong, String paramString2)
    throws RemoteException;
  
  public abstract boolean startGnssBatch(long paramLong, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract boolean stopGnssBatch()
    throws RemoteException;
  
  public abstract void unregisterGnssStatusCallback(IGnssStatusListener paramIGnssStatusListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILocationManager
  {
    private static final String DESCRIPTOR = "android.location.ILocationManager";
    static final int TRANSACTION_addGnssBatchingCallback = 19;
    static final int TRANSACTION_addGnssMeasurementsListener = 12;
    static final int TRANSACTION_addGnssNavigationMessageListener = 14;
    static final int TRANSACTION_addTestProvider = 35;
    static final int TRANSACTION_clearTestProviderEnabled = 40;
    static final int TRANSACTION_clearTestProviderLocation = 38;
    static final int TRANSACTION_clearTestProviderStatus = 42;
    static final int TRANSACTION_flushGnssBatch = 22;
    static final int TRANSACTION_geocoderIsPresent = 8;
    static final int TRANSACTION_getAllProviders = 25;
    static final int TRANSACTION_getBackgroundThrottlingWhitelist = 47;
    static final int TRANSACTION_getBestProvider = 27;
    static final int TRANSACTION_getFromLocation = 9;
    static final int TRANSACTION_getFromLocationName = 10;
    static final int TRANSACTION_getGnssBatchSize = 18;
    static final int TRANSACTION_getGnssHardwareModelName = 17;
    static final int TRANSACTION_getGnssYearOfHardware = 16;
    static final int TRANSACTION_getLastLocation = 5;
    static final int TRANSACTION_getNetworkProviderPackage = 30;
    static final int TRANSACTION_getProviderProperties = 29;
    static final int TRANSACTION_getProviders = 26;
    static final int TRANSACTION_injectLocation = 24;
    static final int TRANSACTION_isLocationEnabledForUser = 33;
    static final int TRANSACTION_isProviderEnabledForUser = 31;
    static final int TRANSACTION_locationCallbackFinished = 46;
    static final int TRANSACTION_providerMeetsCriteria = 28;
    static final int TRANSACTION_registerGnssStatusCallback = 6;
    static final int TRANSACTION_removeGeofence = 4;
    static final int TRANSACTION_removeGnssBatchingCallback = 20;
    static final int TRANSACTION_removeGnssMeasurementsListener = 13;
    static final int TRANSACTION_removeGnssNavigationMessageListener = 15;
    static final int TRANSACTION_removeTestProvider = 36;
    static final int TRANSACTION_removeUpdates = 2;
    static final int TRANSACTION_reportLocation = 44;
    static final int TRANSACTION_reportLocationBatch = 45;
    static final int TRANSACTION_requestGeofence = 3;
    static final int TRANSACTION_requestLocationUpdates = 1;
    static final int TRANSACTION_sendExtraCommand = 43;
    static final int TRANSACTION_sendNiResponse = 11;
    static final int TRANSACTION_setLocationEnabledForUser = 34;
    static final int TRANSACTION_setProviderEnabledForUser = 32;
    static final int TRANSACTION_setTestProviderEnabled = 39;
    static final int TRANSACTION_setTestProviderLocation = 37;
    static final int TRANSACTION_setTestProviderStatus = 41;
    static final int TRANSACTION_startGnssBatch = 21;
    static final int TRANSACTION_stopGnssBatch = 23;
    static final int TRANSACTION_unregisterGnssStatusCallback = 7;
    
    public Stub()
    {
      attachInterface(this, "android.location.ILocationManager");
    }
    
    public static ILocationManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.ILocationManager");
      if ((localIInterface != null) && ((localIInterface instanceof ILocationManager))) {
        return (ILocationManager)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        double d1;
        double d2;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 47: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramParcel1 = getBackgroundThrottlingWhitelist();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          locationCallbackFinished(ILocationListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          reportLocationBatch(paramParcel1.createTypedArrayList(Location.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Location)Location.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject13;
          }
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          reportLocation((Location)localObject1, bool7);
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject12 = paramParcel1.readString();
          localObject10 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          paramInt1 = sendExtraCommand((String)localObject12, (String)localObject10, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          clearTestProviderStatus(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject12 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          setTestProviderStatus((String)localObject12, paramInt1, (Bundle)localObject1, paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          clearTestProviderEnabled(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject1 = paramParcel1.readString();
          bool7 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          setTestProviderEnabled((String)localObject1, bool7, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          clearTestProviderLocation(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject12 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Location)Location.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject2;
          }
          setTestProviderLocation((String)localObject12, (Location)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          removeTestProvider(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject12 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ProviderProperties)ProviderProperties.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject3;
          }
          addTestProvider((String)localObject12, (ProviderProperties)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          bool7 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          setLocationEnabledForUser(bool7, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = isLocationEnabledForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject1 = paramParcel1.readString();
          bool7 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          paramInt1 = setProviderEnabledForUser((String)localObject1, bool7, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = isProviderEnabledForUser(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramParcel1 = getNetworkProviderPackage();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramParcel1 = getProviderProperties(paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Criteria)Criteria.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = providerMeetsCriteria((String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Criteria)Criteria.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject5;
          }
          bool7 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          paramParcel1 = getBestProvider((Criteria)localObject1, bool7);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Criteria)Criteria.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject6;
          }
          bool7 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          paramParcel1 = getProviders((Criteria)localObject1, bool7);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramParcel1 = getAllProviders();
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Location)Location.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramInt1 = injectLocation(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = stopGnssBatch();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          flushGnssBatch(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          long l = paramParcel1.readLong();
          bool7 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool7 = true;
          }
          paramInt1 = startGnssBatch(l, bool7, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          removeGnssBatchingCallback();
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = addGnssBatchingCallback(IBatchedLocationCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = getGnssBatchSize(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramParcel1 = getGnssHardwareModelName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = getGnssYearOfHardware();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          removeGnssNavigationMessageListener(IGnssNavigationMessageListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = addGnssNavigationMessageListener(IGnssNavigationMessageListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          removeGnssMeasurementsListener(IGnssMeasurementsListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = addGnssMeasurementsListener(IGnssMeasurementsListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = sendNiResponse(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject1 = paramParcel1.readString();
          d1 = paramParcel1.readDouble();
          d2 = paramParcel1.readDouble();
          double d3 = paramParcel1.readDouble();
          double d4 = paramParcel1.readDouble();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (GeocoderParams)GeocoderParams.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localObject12 = new ArrayList();
          paramParcel1 = getFromLocationName((String)localObject1, d1, d2, d3, d4, paramInt1, paramParcel1, (List)localObject12);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          paramParcel2.writeTypedList((List)localObject12);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          d2 = paramParcel1.readDouble();
          d1 = paramParcel1.readDouble();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (GeocoderParams)GeocoderParams.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localObject1 = new ArrayList();
          paramParcel1 = getFromLocation(d2, d1, paramInt1, paramParcel1, (List)localObject1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          paramParcel2.writeTypedList((List)localObject1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = geocoderIsPresent();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          unregisterGnssStatusCallback(IGnssStatusListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          paramInt1 = registerGnssStatusCallback(IGnssStatusListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (LocationRequest)LocationRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject8;
          }
          paramParcel1 = getLastLocation((LocationRequest)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Geofence)Geofence.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject12 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject9;
          }
          removeGeofence((Geofence)localObject1, (PendingIntent)localObject12, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (LocationRequest)LocationRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject12 = (Geofence)Geofence.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject10 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          }
          requestGeofence((LocationRequest)localObject1, (Geofence)localObject12, (PendingIntent)localObject10, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.location.ILocationManager");
          localObject12 = ILocationListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject11;
          }
          removeUpdates((ILocationListener)localObject12, (PendingIntent)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.location.ILocationManager");
        if (paramParcel1.readInt() != 0) {
          localObject1 = (LocationRequest)LocationRequest.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject1 = null;
        }
        localObject10 = ILocationListener.Stub.asInterface(paramParcel1.readStrongBinder());
        if (paramParcel1.readInt() != 0) {
          localObject12 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
        }
        requestLocationUpdates((LocationRequest)localObject1, (ILocationListener)localObject10, (PendingIntent)localObject12, paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.location.ILocationManager");
      return true;
    }
    
    private static class Proxy
      implements ILocationManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean addGnssBatchingCallback(IBatchedLocationCallback paramIBatchedLocationCallback, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramIBatchedLocationCallback != null) {
            paramIBatchedLocationCallback = paramIBatchedLocationCallback.asBinder();
          } else {
            paramIBatchedLocationCallback = null;
          }
          localParcel1.writeStrongBinder(paramIBatchedLocationCallback);
          localParcel1.writeString(paramString);
          paramIBatchedLocationCallback = mRemote;
          boolean bool = false;
          paramIBatchedLocationCallback.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean addGnssMeasurementsListener(IGnssMeasurementsListener paramIGnssMeasurementsListener, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramIGnssMeasurementsListener != null) {
            paramIGnssMeasurementsListener = paramIGnssMeasurementsListener.asBinder();
          } else {
            paramIGnssMeasurementsListener = null;
          }
          localParcel1.writeStrongBinder(paramIGnssMeasurementsListener);
          localParcel1.writeString(paramString);
          paramIGnssMeasurementsListener = mRemote;
          boolean bool = false;
          paramIGnssMeasurementsListener.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean addGnssNavigationMessageListener(IGnssNavigationMessageListener paramIGnssNavigationMessageListener, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramIGnssNavigationMessageListener != null) {
            paramIGnssNavigationMessageListener = paramIGnssNavigationMessageListener.asBinder();
          } else {
            paramIGnssNavigationMessageListener = null;
          }
          localParcel1.writeStrongBinder(paramIGnssNavigationMessageListener);
          localParcel1.writeString(paramString);
          paramIGnssNavigationMessageListener = mRemote;
          boolean bool = false;
          paramIGnssNavigationMessageListener.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addTestProvider(String paramString1, ProviderProperties paramProviderProperties, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          if (paramProviderProperties != null)
          {
            localParcel1.writeInt(1);
            paramProviderProperties.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void clearTestProviderEnabled(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearTestProviderLocation(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearTestProviderStatus(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void flushGnssBatch(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean geocoderIsPresent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getAllProviders()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createStringArrayList();
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getBackgroundThrottlingWhitelist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getBestProvider(Criteria paramCriteria, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramCriteria != null)
          {
            localParcel1.writeInt(1);
            paramCriteria.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramCriteria = localParcel2.readString();
          return paramCriteria;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getFromLocation(double paramDouble1, double paramDouble2, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeDouble(paramDouble1);
          localParcel1.writeDouble(paramDouble2);
          localParcel1.writeInt(paramInt);
          if (paramGeocoderParams != null)
          {
            localParcel1.writeInt(1);
            paramGeocoderParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramGeocoderParams = localParcel2.readString();
          localParcel2.readTypedList(paramList, Address.CREATOR);
          return paramGeocoderParams;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public String getFromLocationName(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt, GeocoderParams paramGeocoderParams, List<Address> paramList)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 13
        //   5: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 14
        //   10: aload 13
        //   12: ldc 32
        //   14: invokevirtual 36	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 13
        //   19: aload_1
        //   20: invokevirtual 48	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   23: aload 13
        //   25: dload_2
        //   26: invokevirtual 122	android/os/Parcel:writeDouble	(D)V
        //   29: aload 13
        //   31: dload 4
        //   33: invokevirtual 122	android/os/Parcel:writeDouble	(D)V
        //   36: aload 13
        //   38: dload 6
        //   40: invokevirtual 122	android/os/Parcel:writeDouble	(D)V
        //   43: aload 13
        //   45: dload 8
        //   47: invokevirtual 122	android/os/Parcel:writeDouble	(D)V
        //   50: aload 13
        //   52: iload 10
        //   54: invokevirtual 81	android/os/Parcel:writeInt	(I)V
        //   57: aload 11
        //   59: ifnull +20 -> 79
        //   62: aload 13
        //   64: iconst_1
        //   65: invokevirtual 81	android/os/Parcel:writeInt	(I)V
        //   68: aload 11
        //   70: aload 13
        //   72: iconst_0
        //   73: invokevirtual 125	android/location/GeocoderParams:writeToParcel	(Landroid/os/Parcel;I)V
        //   76: goto +9 -> 85
        //   79: aload 13
        //   81: iconst_0
        //   82: invokevirtual 81	android/os/Parcel:writeInt	(I)V
        //   85: aload_0
        //   86: getfield 19	android/location/ILocationManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   89: bipush 10
        //   91: aload 13
        //   93: aload 14
        //   95: iconst_0
        //   96: invokeinterface 54 5 0
        //   101: pop
        //   102: aload 14
        //   104: invokevirtual 57	android/os/Parcel:readException	()V
        //   107: aload 14
        //   109: invokevirtual 116	android/os/Parcel:readString	()Ljava/lang/String;
        //   112: astore_1
        //   113: getstatic 131	android/location/Address:CREATOR	Landroid/os/Parcelable$Creator;
        //   116: astore 11
        //   118: aload 14
        //   120: aload 12
        //   122: aload 11
        //   124: invokevirtual 135	android/os/Parcel:readTypedList	(Ljava/util/List;Landroid/os/Parcelable$Creator;)V
        //   127: aload 14
        //   129: invokevirtual 64	android/os/Parcel:recycle	()V
        //   132: aload 13
        //   134: invokevirtual 64	android/os/Parcel:recycle	()V
        //   137: aload_1
        //   138: areturn
        //   139: astore_1
        //   140: goto +32 -> 172
        //   143: astore_1
        //   144: goto +28 -> 172
        //   147: astore_1
        //   148: goto +24 -> 172
        //   151: astore_1
        //   152: goto +20 -> 172
        //   155: astore_1
        //   156: goto +16 -> 172
        //   159: astore_1
        //   160: goto +12 -> 172
        //   163: astore_1
        //   164: goto +8 -> 172
        //   167: astore_1
        //   168: goto +4 -> 172
        //   171: astore_1
        //   172: aload 14
        //   174: invokevirtual 64	android/os/Parcel:recycle	()V
        //   177: aload 13
        //   179: invokevirtual 64	android/os/Parcel:recycle	()V
        //   182: aload_1
        //   183: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	184	0	this	Proxy
        //   0	184	1	paramString	String
        //   0	184	2	paramDouble1	double
        //   0	184	4	paramDouble2	double
        //   0	184	6	paramDouble3	double
        //   0	184	8	paramDouble4	double
        //   0	184	10	paramInt	int
        //   0	184	11	paramGeocoderParams	GeocoderParams
        //   0	184	12	paramList	List<Address>
        //   3	175	13	localParcel1	Parcel
        //   8	165	14	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   118	127	139	finally
        //   85	118	143	finally
        //   50	57	147	finally
        //   62	76	147	finally
        //   79	85	147	finally
        //   43	50	151	finally
        //   36	43	155	finally
        //   29	36	159	finally
        //   23	29	163	finally
        //   17	23	167	finally
        //   10	17	171	finally
      }
      
      public int getGnssBatchSize(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getGnssHardwareModelName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getGnssYearOfHardware()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.location.ILocationManager";
      }
      
      public Location getLastLocation(LocationRequest paramLocationRequest, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramLocationRequest != null)
          {
            localParcel1.writeInt(1);
            paramLocationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramLocationRequest = (Location)Location.CREATOR.createFromParcel(localParcel2);
          } else {
            paramLocationRequest = null;
          }
          return paramLocationRequest;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getNetworkProviderPackage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ProviderProperties getProviderProperties(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ProviderProperties)ProviderProperties.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getProviders(Criteria paramCriteria, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramCriteria != null)
          {
            localParcel1.writeInt(1);
            paramCriteria.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramCriteria = localParcel2.createStringArrayList();
          return paramCriteria;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean injectLocation(Location paramLocation)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          boolean bool = true;
          if (paramLocation != null)
          {
            localParcel1.writeInt(1);
            paramLocation.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLocationEnabledForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isProviderEnabledForUser(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void locationCallbackFinished(ILocationListener paramILocationListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramILocationListener != null) {
            paramILocationListener = paramILocationListener.asBinder();
          } else {
            paramILocationListener = null;
          }
          localParcel1.writeStrongBinder(paramILocationListener);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean providerMeetsCriteria(String paramString, Criteria paramCriteria)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramCriteria != null)
          {
            localParcel1.writeInt(1);
            paramCriteria.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean registerGnssStatusCallback(IGnssStatusListener paramIGnssStatusListener, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramIGnssStatusListener != null) {
            paramIGnssStatusListener = paramIGnssStatusListener.asBinder();
          } else {
            paramIGnssStatusListener = null;
          }
          localParcel1.writeStrongBinder(paramIGnssStatusListener);
          localParcel1.writeString(paramString);
          paramIGnssStatusListener = mRemote;
          boolean bool = false;
          paramIGnssStatusListener.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeGeofence(Geofence paramGeofence, PendingIntent paramPendingIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramGeofence != null)
          {
            localParcel1.writeInt(1);
            paramGeofence.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeGnssBatchingCallback()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeGnssMeasurementsListener(IGnssMeasurementsListener paramIGnssMeasurementsListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramIGnssMeasurementsListener != null) {
            paramIGnssMeasurementsListener = paramIGnssMeasurementsListener.asBinder();
          } else {
            paramIGnssMeasurementsListener = null;
          }
          localParcel1.writeStrongBinder(paramIGnssMeasurementsListener);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeGnssNavigationMessageListener(IGnssNavigationMessageListener paramIGnssNavigationMessageListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramIGnssNavigationMessageListener != null) {
            paramIGnssNavigationMessageListener = paramIGnssNavigationMessageListener.asBinder();
          } else {
            paramIGnssNavigationMessageListener = null;
          }
          localParcel1.writeStrongBinder(paramIGnssNavigationMessageListener);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeTestProvider(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeUpdates(ILocationListener paramILocationListener, PendingIntent paramPendingIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramILocationListener != null) {
            paramILocationListener = paramILocationListener.asBinder();
          } else {
            paramILocationListener = null;
          }
          localParcel1.writeStrongBinder(paramILocationListener);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportLocation(Location paramLocation, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramLocation != null)
          {
            localParcel1.writeInt(1);
            paramLocation.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportLocationBatch(List<Location> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeTypedList(paramList);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestGeofence(LocationRequest paramLocationRequest, Geofence paramGeofence, PendingIntent paramPendingIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramLocationRequest != null)
          {
            localParcel1.writeInt(1);
            paramLocationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramGeofence != null)
          {
            localParcel1.writeInt(1);
            paramGeofence.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestLocationUpdates(LocationRequest paramLocationRequest, ILocationListener paramILocationListener, PendingIntent paramPendingIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramLocationRequest != null)
          {
            localParcel1.writeInt(1);
            paramLocationRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramILocationListener != null) {
            paramLocationRequest = paramILocationListener.asBinder();
          } else {
            paramLocationRequest = null;
          }
          localParcel1.writeStrongBinder(paramLocationRequest);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean sendExtraCommand(String paramString1, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() == 0) {
            bool = false;
          }
          if (localParcel2.readInt() != 0) {
            paramBundle.readFromParcel(localParcel2);
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean sendNiResponse(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setLocationEnabledForUser(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setProviderEnabledForUser(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTestProviderEnabled(String paramString1, boolean paramBoolean, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString2);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTestProviderLocation(String paramString1, Location paramLocation, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          if (paramLocation != null)
          {
            localParcel1.writeInt(1);
            paramLocation.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTestProviderStatus(String paramString1, int paramInt, Bundle paramBundle, long paramLong, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString2);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startGnssBatch(long paramLong, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean stopGnssBatch()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterGnssStatusCallback(IGnssStatusListener paramIGnssStatusListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ILocationManager");
          if (paramIGnssStatusListener != null) {
            paramIGnssStatusListener = paramIGnssStatusListener.asBinder();
          } else {
            paramIGnssStatusListener = null;
          }
          localParcel1.writeStrongBinder(paramIGnssStatusListener);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
