package android.gesture;

import java.io.DataInputStream;
import java.io.IOException;

public class GesturePoint
{
  public final long timestamp;
  public final float x;
  public final float y;
  
  public GesturePoint(float paramFloat1, float paramFloat2, long paramLong)
  {
    x = paramFloat1;
    y = paramFloat2;
    timestamp = paramLong;
  }
  
  static GesturePoint deserialize(DataInputStream paramDataInputStream)
    throws IOException
  {
    return new GesturePoint(paramDataInputStream.readFloat(), paramDataInputStream.readFloat(), paramDataInputStream.readLong());
  }
  
  public Object clone()
  {
    return new GesturePoint(x, y, timestamp);
  }
}
