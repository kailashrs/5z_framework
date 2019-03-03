package android.util.proto;

public class ProtoUtils
{
  public ProtoUtils() {}
  
  public static void toAggStatsProto(ProtoOutputStream paramProtoOutputStream, long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    paramLong1 = paramProtoOutputStream.start(paramLong1);
    paramProtoOutputStream.write(1112396529665L, paramLong2);
    paramProtoOutputStream.write(1112396529666L, paramLong3);
    paramProtoOutputStream.write(1112396529667L, paramLong4);
    paramProtoOutputStream.end(paramLong1);
  }
  
  public static void toDuration(ProtoOutputStream paramProtoOutputStream, long paramLong1, long paramLong2, long paramLong3)
  {
    paramLong1 = paramProtoOutputStream.start(paramLong1);
    paramProtoOutputStream.write(1112396529665L, paramLong2);
    paramProtoOutputStream.write(1112396529666L, paramLong3);
    paramProtoOutputStream.end(paramLong1);
  }
  
  public static void writeBitWiseFlagsToProtoEnum(ProtoOutputStream paramProtoOutputStream, long paramLong, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if (paramArrayOfInt2.length == paramArrayOfInt1.length)
    {
      int i = paramArrayOfInt1.length;
      for (int j = 0; j < i; j++)
      {
        if ((paramArrayOfInt1[j] == 0) && (paramInt == 0))
        {
          paramProtoOutputStream.write(paramLong, paramArrayOfInt2[j]);
          return;
        }
        if ((paramArrayOfInt1[j] & paramInt) != 0) {
          paramProtoOutputStream.write(paramLong, paramArrayOfInt2[j]);
        }
      }
      return;
    }
    throw new IllegalArgumentException("The length of origEnums must match protoEnums");
  }
}
