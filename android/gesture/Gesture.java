package android.gesture;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Gesture
  implements Parcelable
{
  private static final boolean BITMAP_RENDERING_ANTIALIAS = true;
  private static final boolean BITMAP_RENDERING_DITHER = true;
  private static final int BITMAP_RENDERING_WIDTH = 2;
  public static final Parcelable.Creator<Gesture> CREATOR = new Parcelable.Creator()
  {
    /* Error */
    public Gesture createFromParcel(android.os.Parcel paramAnonymousParcel)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_2
      //   2: aload_1
      //   3: invokevirtual 24	android/os/Parcel:readLong	()J
      //   6: lstore_3
      //   7: new 26	java/io/DataInputStream
      //   10: dup
      //   11: new 28	java/io/ByteArrayInputStream
      //   14: dup
      //   15: aload_1
      //   16: invokevirtual 32	android/os/Parcel:createByteArray	()[B
      //   19: invokespecial 35	java/io/ByteArrayInputStream:<init>	([B)V
      //   22: invokespecial 38	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
      //   25: astore 5
      //   27: aload 5
      //   29: invokestatic 42	android/gesture/Gesture:deserialize	(Ljava/io/DataInputStream;)Landroid/gesture/Gesture;
      //   32: astore_1
      //   33: aload 5
      //   35: invokestatic 48	android/gesture/GestureUtils:closeStream	(Ljava/io/Closeable;)V
      //   38: goto +22 -> 60
      //   41: astore_1
      //   42: goto +30 -> 72
      //   45: astore_1
      //   46: ldc 50
      //   48: ldc 52
      //   50: aload_1
      //   51: invokestatic 58	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   54: pop
      //   55: aload_2
      //   56: astore_1
      //   57: goto -24 -> 33
      //   60: aload_1
      //   61: ifnull +9 -> 70
      //   64: aload_1
      //   65: lload_3
      //   66: invokestatic 62	android/gesture/Gesture:access$002	(Landroid/gesture/Gesture;J)J
      //   69: pop2
      //   70: aload_1
      //   71: areturn
      //   72: aload 5
      //   74: invokestatic 48	android/gesture/GestureUtils:closeStream	(Ljava/io/Closeable;)V
      //   77: aload_1
      //   78: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	79	0	this	1
      //   0	79	1	paramAnonymousParcel	android.os.Parcel
      //   1	55	2	localObject	Object
      //   6	60	3	l	long
      //   25	48	5	localDataInputStream	DataInputStream
      // Exception table:
      //   from	to	target	type
      //   27	33	41	finally
      //   46	55	41	finally
      //   27	33	45	java/io/IOException
    }
    
    public Gesture[] newArray(int paramAnonymousInt)
    {
      return new Gesture[paramAnonymousInt];
    }
  };
  private static final long GESTURE_ID_BASE = ;
  private static final AtomicInteger sGestureCount = new AtomicInteger(0);
  private final RectF mBoundingBox = new RectF();
  private long mGestureID = GESTURE_ID_BASE + sGestureCount.incrementAndGet();
  private final ArrayList<GestureStroke> mStrokes = new ArrayList();
  
  public Gesture() {}
  
  static Gesture deserialize(DataInputStream paramDataInputStream)
    throws IOException
  {
    Gesture localGesture = new Gesture();
    mGestureID = paramDataInputStream.readLong();
    int i = paramDataInputStream.readInt();
    for (int j = 0; j < i; j++) {
      localGesture.addStroke(GestureStroke.deserialize(paramDataInputStream));
    }
    return localGesture;
  }
  
  public void addStroke(GestureStroke paramGestureStroke)
  {
    mStrokes.add(paramGestureStroke);
    mBoundingBox.union(boundingBox);
  }
  
  public Object clone()
  {
    Gesture localGesture = new Gesture();
    mBoundingBox.set(mBoundingBox.left, mBoundingBox.top, mBoundingBox.right, mBoundingBox.bottom);
    int i = mStrokes.size();
    for (int j = 0; j < i; j++)
    {
      GestureStroke localGestureStroke = (GestureStroke)mStrokes.get(j);
      mStrokes.add((GestureStroke)localGestureStroke.clone());
    }
    return localGesture;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public RectF getBoundingBox()
  {
    return mBoundingBox;
  }
  
  public long getID()
  {
    return mGestureID;
  }
  
  public float getLength()
  {
    int i = 0;
    ArrayList localArrayList = mStrokes;
    int j = localArrayList.size();
    for (int k = 0; k < j; k++) {
      i = (int)(i + getlength);
    }
    return i;
  }
  
  public ArrayList<GestureStroke> getStrokes()
  {
    return mStrokes;
  }
  
  public int getStrokesCount()
  {
    return mStrokes.size();
  }
  
  void serialize(DataOutputStream paramDataOutputStream)
    throws IOException
  {
    ArrayList localArrayList = mStrokes;
    int i = localArrayList.size();
    paramDataOutputStream.writeLong(mGestureID);
    paramDataOutputStream.writeInt(i);
    for (int j = 0; j < i; j++) {
      ((GestureStroke)localArrayList.get(j)).serialize(paramDataOutputStream);
    }
  }
  
  void setID(long paramLong)
  {
    mGestureID = paramLong;
  }
  
  public Bitmap toBitmap(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    localPaint.setDither(true);
    localPaint.setColor(paramInt4);
    localPaint.setStyle(Paint.Style.STROKE);
    localPaint.setStrokeJoin(Paint.Join.ROUND);
    localPaint.setStrokeCap(Paint.Cap.ROUND);
    localPaint.setStrokeWidth(2.0F);
    Path localPath = toPath();
    RectF localRectF = new RectF();
    localPath.computeBounds(localRectF, true);
    float f1 = (paramInt1 - 2 * paramInt3) / localRectF.width();
    float f2 = (paramInt2 - 2 * paramInt3) / localRectF.height();
    if (f1 <= f2) {
      f2 = f1;
    }
    localPaint.setStrokeWidth(2.0F / f2);
    localPath.offset(-left + (paramInt1 - localRectF.width() * f2) / 2.0F, -top + (paramInt2 - localRectF.height() * f2) / 2.0F);
    localCanvas.translate(paramInt3, paramInt3);
    localCanvas.scale(f2, f2);
    localCanvas.drawPath(localPath, localPaint);
    return localBitmap;
  }
  
  public Bitmap toBitmap(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    localCanvas.translate(paramInt3, paramInt3);
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    localPaint.setDither(true);
    localPaint.setColor(paramInt5);
    localPaint.setStyle(Paint.Style.STROKE);
    localPaint.setStrokeJoin(Paint.Join.ROUND);
    localPaint.setStrokeCap(Paint.Cap.ROUND);
    localPaint.setStrokeWidth(2.0F);
    ArrayList localArrayList = mStrokes;
    int i = localArrayList.size();
    for (paramInt5 = 0; paramInt5 < i; paramInt5++) {
      localCanvas.drawPath(((GestureStroke)localArrayList.get(paramInt5)).toPath(paramInt1 - 2 * paramInt3, paramInt2 - 2 * paramInt3, paramInt4), localPaint);
    }
    return localBitmap;
  }
  
  public Path toPath()
  {
    return toPath(null);
  }
  
  public Path toPath(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return toPath(null, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public Path toPath(Path paramPath)
  {
    Path localPath = paramPath;
    if (paramPath == null) {
      localPath = new Path();
    }
    paramPath = mStrokes;
    int i = paramPath.size();
    for (int j = 0; j < i; j++) {
      localPath.addPath(((GestureStroke)paramPath.get(j)).getPath());
    }
    return localPath;
  }
  
  public Path toPath(Path paramPath, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Path localPath = paramPath;
    if (paramPath == null) {
      localPath = new Path();
    }
    paramPath = mStrokes;
    int i = paramPath.size();
    for (int j = 0; j < i; j++) {
      localPath.addPath(((GestureStroke)paramPath.get(j)).toPath(paramInt1 - 2 * paramInt3, paramInt2 - 2 * paramInt3, paramInt4));
    }
    return localPath;
  }
  
  /* Error */
  public void writeToParcel(android.os.Parcel paramParcel, int paramInt)
  {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield 68	android/gesture/Gesture:mGestureID	J
    //   5: invokevirtual 279	android/os/Parcel:writeLong	(J)V
    //   8: iconst_0
    //   9: istore_2
    //   10: new 281	java/io/ByteArrayOutputStream
    //   13: dup
    //   14: ldc_w 282
    //   17: invokespecial 283	java/io/ByteArrayOutputStream:<init>	(I)V
    //   20: astore_3
    //   21: new 150	java/io/DataOutputStream
    //   24: dup
    //   25: aload_3
    //   26: invokespecial 286	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   29: astore 4
    //   31: aload_0
    //   32: aload 4
    //   34: invokevirtual 287	android/gesture/Gesture:serialize	(Ljava/io/DataOutputStream;)V
    //   37: iconst_1
    //   38: istore_2
    //   39: aload 4
    //   41: invokestatic 293	android/gesture/GestureUtils:closeStream	(Ljava/io/Closeable;)V
    //   44: aload_3
    //   45: invokestatic 293	android/gesture/GestureUtils:closeStream	(Ljava/io/Closeable;)V
    //   48: goto +24 -> 72
    //   51: astore_1
    //   52: goto +33 -> 85
    //   55: astore 5
    //   57: ldc_w 295
    //   60: ldc_w 297
    //   63: aload 5
    //   65: invokestatic 303	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   68: pop
    //   69: goto -30 -> 39
    //   72: iload_2
    //   73: ifeq +11 -> 84
    //   76: aload_1
    //   77: aload_3
    //   78: invokevirtual 307	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   81: invokevirtual 311	android/os/Parcel:writeByteArray	([B)V
    //   84: return
    //   85: aload 4
    //   87: invokestatic 293	android/gesture/GestureUtils:closeStream	(Ljava/io/Closeable;)V
    //   90: aload_3
    //   91: invokestatic 293	android/gesture/GestureUtils:closeStream	(Ljava/io/Closeable;)V
    //   94: aload_1
    //   95: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	96	0	this	Gesture
    //   0	96	1	paramParcel	android.os.Parcel
    //   0	96	2	paramInt	int
    //   20	71	3	localByteArrayOutputStream	java.io.ByteArrayOutputStream
    //   29	57	4	localDataOutputStream	DataOutputStream
    //   55	9	5	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   31	37	51	finally
    //   57	69	51	finally
    //   31	37	55	java/io/IOException
  }
}
