package android.util.apk;

import java.nio.ByteBuffer;

class SignatureInfo
{
  public final long apkSigningBlockOffset;
  public final long centralDirOffset;
  public final ByteBuffer eocd;
  public final long eocdOffset;
  public final ByteBuffer signatureBlock;
  
  SignatureInfo(ByteBuffer paramByteBuffer1, long paramLong1, long paramLong2, long paramLong3, ByteBuffer paramByteBuffer2)
  {
    signatureBlock = paramByteBuffer1;
    apkSigningBlockOffset = paramLong1;
    centralDirOffset = paramLong2;
    eocdOffset = paramLong3;
    eocd = paramByteBuffer2;
  }
}
