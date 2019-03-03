package android.nfc.tech;

import android.nfc.INfcTag;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class MifareClassic
  extends BasicTagTechnology
{
  public static final int BLOCK_SIZE = 16;
  public static final byte[] KEY_DEFAULT = { -1, -1, -1, -1, -1, -1 };
  public static final byte[] KEY_MIFARE_APPLICATION_DIRECTORY = { -96, -95, -94, -93, -92, -91 };
  public static final byte[] KEY_NFC_FORUM = { -45, -9, -45, -9, -45, -9 };
  private static final int MAX_BLOCK_COUNT = 256;
  private static final int MAX_SECTOR_COUNT = 40;
  public static final int SIZE_1K = 1024;
  public static final int SIZE_2K = 2048;
  public static final int SIZE_4K = 4096;
  public static final int SIZE_MINI = 320;
  private static final String TAG = "NFC";
  public static final int TYPE_CLASSIC = 0;
  public static final int TYPE_PLUS = 1;
  public static final int TYPE_PRO = 2;
  public static final int TYPE_UNKNOWN = -1;
  private boolean mIsEmulated;
  private int mSize;
  private int mType;
  
  public MifareClassic(Tag paramTag)
    throws RemoteException
  {
    super(paramTag, 8);
    paramTag = NfcA.get(paramTag);
    mIsEmulated = false;
    switch (paramTag.getSak())
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Tag incorrectly enumerated as MIFARE Classic, SAK = ");
      localStringBuilder.append(paramTag.getSak());
      throw new RuntimeException(localStringBuilder.toString());
    case 152: 
    case 184: 
      mType = 2;
      mSize = 4096;
      break;
    case 136: 
      mType = 0;
      mSize = 1024;
      break;
    case 56: 
      mType = 0;
      mSize = 4096;
      mIsEmulated = true;
      break;
    case 40: 
      mType = 0;
      mSize = 1024;
      mIsEmulated = true;
      break;
    case 25: 
      mType = 0;
      mSize = 2048;
      break;
    case 24: 
      mType = 0;
      mSize = 4096;
      break;
    case 17: 
      mType = 1;
      mSize = 4096;
      break;
    case 16: 
      mType = 1;
      mSize = 2048;
      break;
    case 9: 
      mType = 0;
      mSize = 320;
      break;
    case 1: 
    case 8: 
      mType = 0;
      mSize = 1024;
    }
  }
  
  private boolean authenticate(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException
  {
    validateSector(paramInt);
    checkConnected();
    byte[] arrayOfByte1 = new byte[12];
    if (paramBoolean) {
      arrayOfByte1[0] = ((byte)96);
    } else {
      arrayOfByte1[0] = ((byte)97);
    }
    arrayOfByte1[1] = ((byte)(byte)sectorToBlock(paramInt));
    byte[] arrayOfByte2 = getTag().getId();
    System.arraycopy(arrayOfByte2, arrayOfByte2.length - 4, arrayOfByte1, 2, 4);
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 6, 6);
    try
    {
      try
      {
        paramArrayOfByte = transceive(arrayOfByte1, false);
        if (paramArrayOfByte != null) {
          return true;
        }
      }
      catch (IOException paramArrayOfByte) {}
      return false;
    }
    catch (TagLostException paramArrayOfByte)
    {
      throw paramArrayOfByte;
    }
  }
  
  public static MifareClassic get(Tag paramTag)
  {
    if (!paramTag.hasTech(8)) {
      return null;
    }
    try
    {
      paramTag = new MifareClassic(paramTag);
      return paramTag;
    }
    catch (RemoteException paramTag) {}
    return null;
  }
  
  private static void validateBlock(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < 256)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("block out of bounds: ");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  private static void validateSector(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < 40)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("sector out of bounds: ");
    localStringBuilder.append(paramInt);
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  private static void validateValueOperand(int paramInt)
  {
    if (paramInt >= 0) {
      return;
    }
    throw new IllegalArgumentException("value operand negative");
  }
  
  public boolean authenticateSectorWithKeyA(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    return authenticate(paramInt, paramArrayOfByte, true);
  }
  
  public boolean authenticateSectorWithKeyB(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    return authenticate(paramInt, paramArrayOfByte, false);
  }
  
  public int blockToSector(int paramInt)
  {
    validateBlock(paramInt);
    if (paramInt < 128) {
      return paramInt / 4;
    }
    return 32 + (paramInt - 128) / 16;
  }
  
  public void decrement(int paramInt1, int paramInt2)
    throws IOException
  {
    validateBlock(paramInt1);
    validateValueOperand(paramInt2);
    checkConnected();
    ByteBuffer localByteBuffer = ByteBuffer.allocate(6);
    localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    localByteBuffer.put((byte)-64);
    localByteBuffer.put((byte)paramInt1);
    localByteBuffer.putInt(paramInt2);
    transceive(localByteBuffer.array(), false);
  }
  
  public int getBlockCount()
  {
    return mSize / 16;
  }
  
  public int getBlockCountInSector(int paramInt)
  {
    validateSector(paramInt);
    if (paramInt < 32) {
      return 4;
    }
    return 16;
  }
  
  public int getMaxTransceiveLength()
  {
    return getMaxTransceiveLengthInternal();
  }
  
  public int getSectorCount()
  {
    int i = mSize;
    if (i != 320)
    {
      if (i != 1024)
      {
        if (i != 2048)
        {
          if (i != 4096) {
            return 0;
          }
          return 40;
        }
        return 32;
      }
      return 16;
    }
    return 5;
  }
  
  public int getSize()
  {
    return mSize;
  }
  
  public int getTimeout()
  {
    try
    {
      int i = mTag.getTagService().getTimeout(8);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
    return 0;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public void increment(int paramInt1, int paramInt2)
    throws IOException
  {
    validateBlock(paramInt1);
    validateValueOperand(paramInt2);
    checkConnected();
    ByteBuffer localByteBuffer = ByteBuffer.allocate(6);
    localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    localByteBuffer.put((byte)-63);
    localByteBuffer.put((byte)paramInt1);
    localByteBuffer.putInt(paramInt2);
    transceive(localByteBuffer.array(), false);
  }
  
  public boolean isEmulated()
  {
    return mIsEmulated;
  }
  
  public byte[] readBlock(int paramInt)
    throws IOException
  {
    validateBlock(paramInt);
    checkConnected();
    return transceive(new byte[] { 48, (byte)paramInt }, false);
  }
  
  public void restore(int paramInt)
    throws IOException
  {
    validateBlock(paramInt);
    checkConnected();
    transceive(new byte[] { -62, (byte)paramInt }, false);
  }
  
  public int sectorToBlock(int paramInt)
  {
    if (paramInt < 32) {
      return paramInt * 4;
    }
    return 128 + (paramInt - 32) * 16;
  }
  
  public void setTimeout(int paramInt)
  {
    try
    {
      if (mTag.getTagService().setTimeout(8, paramInt) != 0)
      {
        IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
        localIllegalArgumentException.<init>("The supplied timeout is not valid");
        throw localIllegalArgumentException;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NFC", "NFC service dead", localRemoteException);
    }
  }
  
  public byte[] transceive(byte[] paramArrayOfByte)
    throws IOException
  {
    return transceive(paramArrayOfByte, true);
  }
  
  public void transfer(int paramInt)
    throws IOException
  {
    validateBlock(paramInt);
    checkConnected();
    transceive(new byte[] { -80, (byte)paramInt }, false);
  }
  
  public void writeBlock(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    validateBlock(paramInt);
    checkConnected();
    if (paramArrayOfByte.length == 16)
    {
      byte[] arrayOfByte = new byte[paramArrayOfByte.length + 2];
      arrayOfByte[0] = ((byte)-96);
      arrayOfByte[1] = ((byte)(byte)paramInt);
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 2, paramArrayOfByte.length);
      transceive(arrayOfByte, false);
      return;
    }
    throw new IllegalArgumentException("must write 16-bytes");
  }
}
