package android.telephony.mbms;

import android.annotation.SystemApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public final class DownloadRequest
  implements Parcelable
{
  public static final Parcelable.Creator<DownloadRequest> CREATOR = new Parcelable.Creator()
  {
    public DownloadRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DownloadRequest(paramAnonymousParcel, null);
    }
    
    public DownloadRequest[] newArray(int paramAnonymousInt)
    {
      return new DownloadRequest[paramAnonymousInt];
    }
  };
  private static final int CURRENT_VERSION = 1;
  private static final String LOG_TAG = "MbmsDownloadRequest";
  public static final int MAX_APP_INTENT_SIZE = 50000;
  public static final int MAX_DESTINATION_URI_SIZE = 50000;
  private final Uri destinationUri;
  private final String fileServiceId;
  private final String serializedResultIntentForApp;
  private final Uri sourceUri;
  private final int subscriptionId;
  private final int version;
  
  private DownloadRequest(Parcel paramParcel)
  {
    fileServiceId = paramParcel.readString();
    sourceUri = ((Uri)paramParcel.readParcelable(getClass().getClassLoader()));
    destinationUri = ((Uri)paramParcel.readParcelable(getClass().getClassLoader()));
    subscriptionId = paramParcel.readInt();
    serializedResultIntentForApp = paramParcel.readString();
    version = paramParcel.readInt();
  }
  
  private DownloadRequest(String paramString1, Uri paramUri1, Uri paramUri2, int paramInt1, String paramString2, int paramInt2)
  {
    fileServiceId = paramString1;
    sourceUri = paramUri1;
    subscriptionId = paramInt1;
    destinationUri = paramUri2;
    serializedResultIntentForApp = paramString2;
    version = paramInt2;
  }
  
  public static int getMaxAppIntentSize()
  {
    return 50000;
  }
  
  public static int getMaxDestinationUriSize()
  {
    return 50000;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof DownloadRequest)) {
      return false;
    }
    paramObject = (DownloadRequest)paramObject;
    if ((subscriptionId != subscriptionId) || (version != version) || (!Objects.equals(fileServiceId, fileServiceId)) || (!Objects.equals(sourceUri, sourceUri)) || (!Objects.equals(destinationUri, destinationUri)) || (!Objects.equals(serializedResultIntentForApp, serializedResultIntentForApp))) {
      bool = false;
    }
    return bool;
  }
  
  public Uri getDestinationUri()
  {
    return destinationUri;
  }
  
  public String getFileServiceId()
  {
    return fileServiceId;
  }
  
  public String getHash()
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
      if (version >= 1)
      {
        localMessageDigest.update(sourceUri.toString().getBytes(StandardCharsets.UTF_8));
        localMessageDigest.update(destinationUri.toString().getBytes(StandardCharsets.UTF_8));
        if (serializedResultIntentForApp != null) {
          localMessageDigest.update(serializedResultIntentForApp.getBytes(StandardCharsets.UTF_8));
        }
      }
      return Base64.encodeToString(localMessageDigest.digest(), 10);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new RuntimeException("Could not get sha256 hash object");
    }
  }
  
  public Intent getIntentForApp()
  {
    try
    {
      Intent localIntent = Intent.parseUri(serializedResultIntentForApp, 0);
      return localIntent;
    }
    catch (URISyntaxException localURISyntaxException) {}
    return null;
  }
  
  public Uri getSourceUri()
  {
    return sourceUri;
  }
  
  public int getSubscriptionId()
  {
    return subscriptionId;
  }
  
  public int getVersion()
  {
    return version;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { fileServiceId, sourceUri, destinationUri, Integer.valueOf(subscriptionId), serializedResultIntentForApp, Integer.valueOf(version) });
  }
  
  public byte[] toByteArray()
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new java/io/ByteArrayOutputStream;
      localByteArrayOutputStream.<init>();
      ObjectOutputStream localObjectOutputStream = new java/io/ObjectOutputStream;
      localObjectOutputStream.<init>(localByteArrayOutputStream);
      Object localObject = new android/telephony/mbms/DownloadRequest$SerializationDataContainer;
      ((SerializationDataContainer)localObject).<init>(this);
      localObjectOutputStream.writeObject(localObject);
      localObjectOutputStream.flush();
      localObject = localByteArrayOutputStream.toByteArray();
      return localObject;
    }
    catch (IOException localIOException)
    {
      Log.e("MbmsDownloadRequest", "Got IOException trying to serialize opaque data");
    }
    return null;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(fileServiceId);
    paramParcel.writeParcelable(sourceUri, paramInt);
    paramParcel.writeParcelable(destinationUri, paramInt);
    paramParcel.writeInt(subscriptionId);
    paramParcel.writeString(serializedResultIntentForApp);
    paramParcel.writeInt(version);
  }
  
  public static class Builder
  {
    private String appIntent;
    private Uri destination;
    private String fileServiceId;
    private Uri source;
    private int subscriptionId;
    private int version = 1;
    
    public Builder(Uri paramUri1, Uri paramUri2)
    {
      if ((paramUri1 != null) && (paramUri2 != null))
      {
        source = paramUri1;
        destination = paramUri2;
        return;
      }
      throw new IllegalArgumentException("Source and destination URIs must be non-null.");
    }
    
    public static Builder fromDownloadRequest(DownloadRequest paramDownloadRequest)
    {
      Builder localBuilder = new Builder(sourceUri, destinationUri).setServiceId(fileServiceId).setSubscriptionId(subscriptionId);
      appIntent = serializedResultIntentForApp;
      return localBuilder;
    }
    
    public static Builder fromSerializedRequest(byte[] paramArrayOfByte)
    {
      try
      {
        Object localObject = new java/io/ObjectInputStream;
        ByteArrayInputStream localByteArrayInputStream = new java/io/ByteArrayInputStream;
        localByteArrayInputStream.<init>(paramArrayOfByte);
        ((ObjectInputStream)localObject).<init>(localByteArrayInputStream);
        paramArrayOfByte = (DownloadRequest.SerializationDataContainer)((ObjectInputStream)localObject).readObject();
        localObject = new android/telephony/mbms/DownloadRequest$Builder;
        ((Builder)localObject).<init>(DownloadRequest.SerializationDataContainer.access$600(paramArrayOfByte), DownloadRequest.SerializationDataContainer.access$700(paramArrayOfByte));
        version = DownloadRequest.SerializationDataContainer.access$800(paramArrayOfByte);
        appIntent = DownloadRequest.SerializationDataContainer.access$900(paramArrayOfByte);
        fileServiceId = DownloadRequest.SerializationDataContainer.access$1000(paramArrayOfByte);
        subscriptionId = DownloadRequest.SerializationDataContainer.access$1100(paramArrayOfByte);
        return localObject;
      }
      catch (ClassNotFoundException paramArrayOfByte)
      {
        Log.e("MbmsDownloadRequest", "Got ClassNotFoundException trying to parse opaque data");
        throw new IllegalArgumentException(paramArrayOfByte);
      }
      catch (IOException paramArrayOfByte)
      {
        Log.e("MbmsDownloadRequest", "Got IOException trying to parse opaque data");
        throw new IllegalArgumentException(paramArrayOfByte);
      }
    }
    
    public DownloadRequest build()
    {
      return new DownloadRequest(fileServiceId, source, destination, subscriptionId, appIntent, version, null);
    }
    
    public Builder setAppIntent(Intent paramIntent)
    {
      appIntent = paramIntent.toUri(0);
      if (appIntent.length() <= 50000) {
        return this;
      }
      throw new IllegalArgumentException("App intent must not exceed length 50000");
    }
    
    @SystemApi
    public Builder setServiceId(String paramString)
    {
      fileServiceId = paramString;
      return this;
    }
    
    public Builder setServiceInfo(FileServiceInfo paramFileServiceInfo)
    {
      fileServiceId = paramFileServiceInfo.getServiceId();
      return this;
    }
    
    public Builder setSubscriptionId(int paramInt)
    {
      subscriptionId = paramInt;
      return this;
    }
  }
  
  private static class SerializationDataContainer
    implements Externalizable
  {
    private String appIntent;
    private Uri destination;
    private String fileServiceId;
    private Uri source;
    private int subscriptionId;
    private int version;
    
    public SerializationDataContainer() {}
    
    SerializationDataContainer(DownloadRequest paramDownloadRequest)
    {
      fileServiceId = fileServiceId;
      source = sourceUri;
      destination = destinationUri;
      subscriptionId = subscriptionId;
      appIntent = serializedResultIntentForApp;
      version = version;
    }
    
    public void readExternal(ObjectInput paramObjectInput)
      throws IOException
    {
      version = paramObjectInput.read();
      fileServiceId = paramObjectInput.readUTF();
      source = Uri.parse(paramObjectInput.readUTF());
      destination = Uri.parse(paramObjectInput.readUTF());
      subscriptionId = paramObjectInput.read();
      appIntent = paramObjectInput.readUTF();
    }
    
    public void writeExternal(ObjectOutput paramObjectOutput)
      throws IOException
    {
      paramObjectOutput.write(version);
      paramObjectOutput.writeUTF(fileServiceId);
      paramObjectOutput.writeUTF(source.toString());
      paramObjectOutput.writeUTF(destination.toString());
      paramObjectOutput.write(subscriptionId);
      paramObjectOutput.writeUTF(appIntent);
    }
  }
}
