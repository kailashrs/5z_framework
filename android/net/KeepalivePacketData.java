package android.net;

import android.net.util.IpUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.system.OsConstants;
import android.util.Log;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class KeepalivePacketData
  implements Parcelable
{
  public static final Parcelable.Creator<KeepalivePacketData> CREATOR = new Parcelable.Creator()
  {
    public KeepalivePacketData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeepalivePacketData(paramAnonymousParcel, null);
    }
    
    public KeepalivePacketData[] newArray(int paramAnonymousInt)
    {
      return new KeepalivePacketData[paramAnonymousInt];
    }
  };
  private static final int IPV4_HEADER_LENGTH = 20;
  private static final String TAG = "KeepalivePacketData";
  private static final int UDP_HEADER_LENGTH = 8;
  public final InetAddress dstAddress;
  public final int dstPort;
  private final byte[] mPacket;
  public final InetAddress srcAddress;
  public final int srcPort;
  
  private KeepalivePacketData(Parcel paramParcel)
  {
    srcAddress = NetworkUtils.numericToInetAddress(paramParcel.readString());
    dstAddress = NetworkUtils.numericToInetAddress(paramParcel.readString());
    srcPort = paramParcel.readInt();
    dstPort = paramParcel.readInt();
    mPacket = paramParcel.createByteArray();
  }
  
  protected KeepalivePacketData(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2, byte[] paramArrayOfByte)
    throws KeepalivePacketData.InvalidPacketException
  {
    srcAddress = paramInetAddress1;
    dstAddress = paramInetAddress2;
    srcPort = paramInt1;
    dstPort = paramInt2;
    mPacket = paramArrayOfByte;
    if ((paramInetAddress1 != null) && (paramInetAddress2 != null) && (paramInetAddress1.getClass().getName().equals(paramInetAddress2.getClass().getName())))
    {
      if ((IpUtils.isValidUdpOrTcpPort(paramInt1)) && (IpUtils.isValidUdpOrTcpPort(paramInt2))) {
        return;
      }
      Log.e("KeepalivePacketData", "Invalid ports in KeepalivePacketData");
      throw new InvalidPacketException(-22);
    }
    Log.e("KeepalivePacketData", "Invalid or mismatched InetAddresses in KeepalivePacketData");
    throw new InvalidPacketException(-21);
  }
  
  public static KeepalivePacketData nattKeepalivePacket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
    throws KeepalivePacketData.InvalidPacketException
  {
    if (((paramInetAddress1 instanceof Inet4Address)) && ((paramInetAddress2 instanceof Inet4Address)))
    {
      if (paramInt2 == 4500)
      {
        ByteBuffer localByteBuffer = ByteBuffer.allocate(29);
        localByteBuffer.order(ByteOrder.BIG_ENDIAN);
        localByteBuffer.putShort((short)17664);
        localByteBuffer.putShort((short)29);
        localByteBuffer.putInt(0);
        localByteBuffer.put((byte)64);
        localByteBuffer.put((byte)OsConstants.IPPROTO_UDP);
        int i = localByteBuffer.position();
        localByteBuffer.putShort((short)0);
        localByteBuffer.put(paramInetAddress1.getAddress());
        localByteBuffer.put(paramInetAddress2.getAddress());
        localByteBuffer.putShort((short)paramInt1);
        localByteBuffer.putShort((short)paramInt2);
        localByteBuffer.putShort((short)(29 - 20));
        int j = localByteBuffer.position();
        localByteBuffer.putShort((short)0);
        localByteBuffer.put((byte)-1);
        localByteBuffer.putShort(i, IpUtils.ipChecksum(localByteBuffer, 0));
        localByteBuffer.putShort(j, IpUtils.udpChecksum(localByteBuffer, 0, 20));
        return new KeepalivePacketData(paramInetAddress1, paramInt1, paramInetAddress2, paramInt2, localByteBuffer.array());
      }
      throw new InvalidPacketException(-22);
    }
    throw new InvalidPacketException(-21);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getPacket()
  {
    return (byte[])mPacket.clone();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(srcAddress.getHostAddress());
    paramParcel.writeString(dstAddress.getHostAddress());
    paramParcel.writeInt(srcPort);
    paramParcel.writeInt(dstPort);
    paramParcel.writeByteArray(mPacket);
  }
  
  public static class InvalidPacketException
    extends Exception
  {
    public final int error;
    
    public InvalidPacketException(int paramInt)
    {
      error = paramInt;
    }
  }
}
