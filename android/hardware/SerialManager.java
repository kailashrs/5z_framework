package android.hardware;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.io.IOException;

public class SerialManager
{
  private static final String TAG = "SerialManager";
  private final Context mContext;
  private final ISerialManager mService;
  
  public SerialManager(Context paramContext, ISerialManager paramISerialManager)
  {
    mContext = paramContext;
    mService = paramISerialManager;
  }
  
  public String[] getSerialPorts()
  {
    try
    {
      String[] arrayOfString = mService.getSerialPorts();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public SerialPort openSerialPort(String paramString, int paramInt)
    throws IOException
  {
    try
    {
      Object localObject1 = mService.openSerialPort(paramString);
      if (localObject1 != null)
      {
        localObject2 = new android/hardware/SerialPort;
        ((SerialPort)localObject2).<init>(paramString);
        ((SerialPort)localObject2).open((ParcelFileDescriptor)localObject1, paramInt);
        return localObject2;
      }
      Object localObject2 = new java/io/IOException;
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Could not open serial port ");
      ((StringBuilder)localObject1).append(paramString);
      ((IOException)localObject2).<init>(((StringBuilder)localObject1).toString());
      throw ((Throwable)localObject2);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
}
